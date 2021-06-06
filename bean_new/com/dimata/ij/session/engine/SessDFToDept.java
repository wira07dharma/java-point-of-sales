/*
 * SessDFToDept.java
 *
 * Created on January 10, 2006, 7:12 AM
 */

package com.dimata.ij.session.engine;

// core java package
import java.util.Vector;
import java.util.Date;
import java.util.Hashtable; 
   
// qdep package
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_IJGeneral;

// common package 
import com.dimata.common.entity.logger.PstDocLogger;

// ij package
import com.dimata.ij.ibosys.*;
import com.dimata.ij.iaiso.*;
import com.dimata.ij.entity.mapping.*;

/**
 *
 * @author  Administrator
 */
public class SessDFToDept {
    
    // define journal note for transaction DF To Department
    public static String strJournalNote[] = 
    {
        "Transaksi transfer ke department lain","DF to department"
    };
    
  
    /**
     * Generate list of DF To Department journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */                                                                
    public int generateDFToDeptJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDFToDeptJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }                

        return result;             
    }     

    
    /*
     * Generate list of DF To Department journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm
     *  1. get list of DFToDept Document from selected BO system
     *  2. iterate as long as document count to generate DFToDeptJournal
     */    
    public int generateDFToDeptJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        // 1. Ambil data DFToDept (refer to IJ-Interface getListDFToDepartment) 
        Vector vectOfDFToDeptDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of DFToDept Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfDFToDeptDoc = i_bosys.getListDFToDepartment(lLocationOid, dSelectedDate);            
            // --- end get list of DFToDept Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate DFToDept Journal ---
            if(vectOfDFToDeptDoc!=null && vectOfDFToDeptDoc.size()>0)
            {  
                int maxDFToDeptDoc = vectOfDFToDeptDoc.size();
                for(int i=0; i<maxDFToDeptDoc; i++)
                {
                    IjInventoryOnDFDoc objIjInventoryOnDfDoc = (IjInventoryOnDFDoc) vectOfDFToDeptDoc.get(i);                                                
                    
                    long lResult = genJournalOfObjDFToDept(lLocationOid, objIjInventoryOnDfDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling DFToDept process skip ... ");
            }                
            // --- end iterate as long as document count to generate DFToDept Journal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;             
    }     
    
    
    /**
     * Generate list of DFToDept journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjInventoryOnDFToDeptDoc</CODE>objIjInventoryOnDFToDeptDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DFToDept (object IjInventoryOnDFDoc).
     * 	2. Pembuatan vector of jurnal detail posisi debet (destination loc) dan kredit (source location).          
     *  3. Generate Journal dengan :
     *     3.1 Jika 'Multi department' dan Coa berbeda lokasi, maka :
     *         3.1.1 Debet  : "Inventory on Destination Location"
     *         3.1.2 Kredit : "Account Payable"
     *
     *     3.2 Jika ('Multi department' dan Coa lokasi sama) atau 'Non Department', maka :
     *         3.2.1 Debet  : "Inventory on Destination Location"
     *         3.2.2 Kredit : "Inventory on Source Location"
     */    
    public long genJournalOfObjDFToDept(long lLocationOid, IjInventoryOnDFDoc objIjInventoryOnDFToDeptDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen LGR From Dept (object objIjInventoryOnDFDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjInventoryOnDFToDeptDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data payment.
        Vector vectOfObjIjJurDetail = genObjIjJDOfDFToDept(objIjInventoryOnDFToDeptDoc, objIjEngineParam);
        
        // 3. save jurnal ke database IJ 
        if(vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0) 
        {
            // 4. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_DF_TO_DEPT, objIjJournalMain.getRefBoDocLastUpdate(), "DF to Department : " + objIjJournalMain.getRefBoDocNumber());                
                
                // posting ke AISO dan update BO Status
                if(lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO)
                {
                    SessPosting objSessPosting = new SessPosting();
                    objIjJournalMain.setOID(lResult);
                    objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
                }                                                
            }            
        }                
        
        return lResult;
    }    
    
    
    /**
     *     
     * @param <CODE>objIjInventoryOnDFToDeptDoc</CODE>objIjInventoryOnDFToDeptDoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return  
     */    
    private IjJournalMain genObjIjJournalMain(IjInventoryOnDFDoc objIjInventoryOnDFToDeptDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjInventoryOnDFToDeptDoc.getDocTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());                 
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjInventoryOnDFToDeptDoc.getDocId());                
        objIjJournalMain.setRefBoDocNumber(objIjInventoryOnDFToDeptDoc.getDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjInventoryOnDFToDeptDoc.getDtLastUpdate());                                        
        
        return objIjJournalMain;        
    }
    
    
    /**
     * Membuat detail utk posisi debet dan kredit dengan mengambil Location Mapping
     * Jika Coa debet dan kredit berbeda lokasi, maka buat jurnal detail posisi kredit dengan mengambil Account Mapping
     *     
     * @param <CODE>objIjInventoryOnDFToDeptDoc</CODE>objIjInventoryOnDFToDeptDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */    
    private Vector genObjIjJDOfDFToDept(IjInventoryOnDFDoc objIjInventoryOnDFToDeptDoc, IjEngineParam objIjEngineParam)
    {
        Vector vResult = new Vector(1,1);
        boolean isCoaOnDifferentDepartment = false;        
        
        Vector listOfInventory = objIjInventoryOnDFToDeptDoc.getListInventory();                    
        if(listOfInventory!=null && listOfInventory.size()>0)
        {
            // instantiate object I_Aiso
            try
            {                
                I_Aiso i_aiso = (I_Aiso) Class.forName(I_Aiso.implClassName).newInstance();                                                                
                
                PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping(); 
                int inventoryCount = listOfInventory.size();
                for(int iVal=0; iVal<inventoryCount; iVal++)
                {
                    IjInventoryDoc objIjInventoryDoc = (IjInventoryDoc) listOfInventory.get(iVal);

                    // Ambil Transaction Location Mapping inventory Location on destination location
                    IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, objIjInventoryDoc.getInvDestLocation(), objIjInventoryDoc.getInvProdDepartment());
                    long locationAccChartDebet = objIjLocationMappingDebet.getAccount();                                                                                

                    // Ambil Transaction Location Mapping inventory Location on source location
                    IjLocationMapping objIjLocationMappingKredit = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, objIjInventoryDoc.getInvSourceLocation(), objIjInventoryDoc.getInvProdDepartment());
                    long locationAccChartKredit = objIjLocationMappingKredit.getAccount();                                                                                

                    isCoaOnDifferentDepartment = i_aiso.isCoaInDifferentDepartment(locationAccChartDebet, locationAccChartKredit);
                    if(isCoaOnDifferentDepartment)
                    {
                        // -- start debet --
                        // cari account mapping 'DF To Department' 
                        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping(); 
                        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(objIjInventoryDoc.getInvDestLocation(), I_IJGeneral.TRANS_DF_TO_DEPARTMENT, objIjEngineParam.getLBookType());
                        long accountAccChartDebet = objIjAccountMapping.getAccount();
                        
                        // pembuatan jurnal detail                
                        if( accountAccChartDebet != 0 )
                        {                
                            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
                            objIjJournalDetailDebet.setJdetAccChart(accountAccChartDebet);                            
                            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
                            objIjJournalDetailDebet.setJdetTransRate(1);                        
                            objIjJournalDetailDebet.setJdetDebet(objIjInventoryDoc.getInvValue());
                            objIjJournalDetailDebet.setJdetCredit(0);                        
                            vResult.add(objIjJournalDetailDebet);                                        
                        }        

                        // -- start kredit --
                        // pembuatan jurnal detail
                        if( locationAccChartKredit != 0 )
                        {
                            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
                            objIjJournalDetailKredit.setJdetAccChart(locationAccChartKredit);                            
                            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
                            objIjJournalDetailKredit.setJdetTransRate(1);                        
                            objIjJournalDetailKredit.setJdetDebet(0);
                            objIjJournalDetailKredit.setJdetCredit(objIjInventoryDoc.getInvValue());                             
                            vResult.add(objIjJournalDetailKredit);                    
                        }                        
                    }
                    else
                    {
                        // -- start debet --
                        // pembuatan jurnal detail                
                        if( locationAccChartDebet != 0 )
                        {                
                            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
                            objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);                            
                            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
                            objIjJournalDetailDebet.setJdetTransRate(1);                        
                            objIjJournalDetailDebet.setJdetDebet(objIjInventoryDoc.getInvValue());
                            objIjJournalDetailDebet.setJdetCredit(0);                        
                            vResult.add(objIjJournalDetailDebet);                                        
                        }        

                        // -- start kredit --
                        // pembuatan jurnal detail
                        if( locationAccChartKredit != 0 )
                        {
                            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
                            objIjJournalDetailKredit.setJdetAccChart(locationAccChartKredit);                            
                            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
                            objIjJournalDetailKredit.setJdetTransRate(1);                        
                            objIjJournalDetailKredit.setJdetDebet(0);
                            objIjJournalDetailKredit.setJdetCredit(objIjInventoryDoc.getInvValue());                             
                            vResult.add(objIjJournalDetailKredit);                    
                        }                                                
                    }                    
                } 
            }
            catch(Exception e)
            {
                System.out.println("Exc when instantiate I_Aiso on genObjIjJDOfDFToDept : " + e.toString());
            }                        
        }                        
        
        return vResult;
    }            
    
}
