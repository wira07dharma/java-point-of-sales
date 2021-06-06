/*
 * SessLGRFromDepartment.java
 *
 * Created on January 10, 2006, 7:11 AM
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
public class SessLGRFromDept {
    
    // define journal note for transaction LGR From Department
    public static String strJournalNote[] = 
    {
        "Transaksi penerimaan barang dari transfer dari department lain","LGR from department's transfering"
    };
    
  
    /**
     * Generate list of LGR From Department journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */                                                                
    public int generateLGRFromDeptJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        if(objIjEngineParam.isBRptDepartmental()) 
        {
            Vector vLocationOid = objIjEngineParam.getVLocationOid();
            if(vLocationOid!=null && vLocationOid.size()>0)
            {
                int iLocationCount = vLocationOid.size();
                for(int i=0; i<iLocationCount; i++)
                {
                    long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                    result = result + generateLGRFromDeptJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
                }
            }                
        }
        return result;             
    }     

    
    /*
     * Generate list of LGR From Department journal based on selected document on selected Bo system
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
     *  1. get list of LGR From Dept Document from selected BO system
     *  2. iterate as long as document count to generate LGRFromDeptJournal
     */    
    public int generateLGRFromDeptJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        // 1. Ambil data LGR From Dept (refer to IJ-Interface getListLGRFromDepartment) 
        Vector vectOfLgrFromDeptDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of LGR From Dept Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfLgrFromDeptDoc = i_bosys.getListLGRFromDepartment(lLocationOid, dSelectedDate);            
            // --- end get list of LGR From Dept Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate LGR From Dept Journal ---
            if(vectOfLgrFromDeptDoc!=null && vectOfLgrFromDeptDoc.size()>0)
            {  
                int maxLgrFromDeptDoc = vectOfLgrFromDeptDoc.size();
                for(int i=0; i<maxLgrFromDeptDoc; i++)
                {
                    IjInventoryOnDFDoc objIjInventoryOnLgrDoc = (IjInventoryOnDFDoc) vectOfLgrFromDeptDoc.get(i);                                                
                    
                    long lResult = genJournalOfObjLGRFromDept(lLocationOid, objIjInventoryOnLgrDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling LGR From Dept process skip ... ");
            }                
            // --- end iterate as long as document count to generate LGR From Dept Journal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;             
    }     
    
    
    /**
     * Generate list of LGR From Dept journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjInventoryOnLgrFromDeptDoc</CODE>objIjInventoryOnLgrFromDeptDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen LGR From Dept (object IjInventoryOnDFDoc).
     * 	2. Pembuatan vector of jurnal detail posisi debet (destination loc) dan kredit (source location).          
     *  3. Generate Journal dengan :
     *     3.1 Debet  : "Inventory on Destination Location"
     *     3.2 Kredit : "Account Payable"
     */    
    public long genJournalOfObjLGRFromDept(long lLocationOid, IjInventoryOnDFDoc objIjInventoryOnLgrFromDeptDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen LGR From Dept (object objIjInventoryOnDFDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjInventoryOnLgrFromDeptDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data payment.
        Vector vectOfObjIjJurDetail = genObjIjJDOfLGRFromDept(objIjInventoryOnLgrFromDeptDoc, objIjEngineParam);
        
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
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_LGR_FROM_DEPT, objIjJournalMain.getRefBoDocLastUpdate(), "LGR From Department : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * @param <CODE>objIjInventoryOnLgrFromDeptDoc</CODE>objIjInventoryOnLgrFromDeptDoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return  
     */    
    private IjJournalMain genObjIjJournalMain(IjInventoryOnDFDoc objIjInventoryOnLgrFromDeptDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjInventoryOnLgrFromDeptDoc.getDocTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());                 
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjInventoryOnLgrFromDeptDoc.getDocId());                
        objIjJournalMain.setRefBoDocNumber(objIjInventoryOnLgrFromDeptDoc.getDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjInventoryOnLgrFromDeptDoc.getDtLastUpdate());                                        
        
        return objIjJournalMain;        
    }
    
    
    /**
     * Membuat detail utk posisi debet dengan mengambil Location Mapping pada Destination Location
     * Membuat detail utk posisi kredit dengan mengambil Account Mapping
     *     
     * @param <CODE>objIjInventoryOnLgrFromDeptDoc</CODE>objIjInventoryOnLgrFromDeptDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */    
    private Vector genObjIjJDOfLGRFromDept(IjInventoryOnDFDoc objIjInventoryOnLgrFromDeptDoc, IjEngineParam objIjEngineParam)
    {
        Vector vResult = new Vector(1,1);        
        
        Vector listOfInventory = objIjInventoryOnLgrFromDeptDoc.getListInventory();                    
        if(listOfInventory!=null && listOfInventory.size()>0)
        {
            PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping(); 
            int inventoryCount = listOfInventory.size();
            for(int iVal=0; iVal<inventoryCount; iVal++)
            {
                IjInventoryDoc objIjInventoryDoc = (IjInventoryDoc) listOfInventory.get(iVal);

                // -- start debet --
                // Ambil Transaction Location Mapping ?nventory Location?on destination location
                IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, objIjInventoryDoc.getInvDestLocation(), objIjInventoryDoc.getInvProdDepartment());
                long locationAccChartDebet = objIjLocationMappingDebet.getAccount();                                                                                

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
                // Ambil Transaction Account Mapping ?ccount Payable?
                PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
                IjAccountMapping objIjAccountMappingKredit = objPstIjAccountMapping.getObjIjAccountMapping(objIjInventoryDoc.getInvDestLocation(),I_IJGeneral.TRANS_LGR_FROM_DEPARTMENT, objIjEngineParam.getLBookType());
                long accountAccChartKredit = objIjAccountMappingKredit.getAccount();                                                                                

                // pembuatan jurnal detail
                if( accountAccChartKredit != 0 )
                {
                    IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
                    objIjJournalDetailKredit.setJdetAccChart(accountAccChartKredit);                            
                    objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
                    objIjJournalDetailKredit.setJdetTransRate(1);                        
                    objIjJournalDetailKredit.setJdetDebet(0);
                    objIjJournalDetailKredit.setJdetCredit(objIjInventoryDoc.getInvValue());                             
                    vResult.add(objIjJournalDetailKredit);                    
                }                        
            }                        
        }                        
        
        return vResult;
    }            
    
}
