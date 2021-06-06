/*
 * SessCustReturnDeductDeduction.java
 *
 * Created on January 10, 2006, 7:18 AM
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
public class SessCustReturnDeduct {
    
    // define journal note for transaction CustReturnDeduct
    public static String strJournalNote[] = 
    {
        "Transaksi customer return deduction untuk ","Customer return deduction for "
    };
    
  
    /**
     * Generate list of Inventory On Customer Return Deduct journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */                                                                
    public int generateCustReturnDeductJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateCustReturnDeductJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }                
        
        return result;             
    }     

    
    /*
     * Generate list of Inventory On Customer Return Deduct journal based on selected document on selected Bo system
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
     *  1. get list of Inv On DF Costing Document from selected BO system
     *  2. iterate as long as document count to generate CustReturnDeductJournal
     */    
    public int generateCustReturnDeductJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR) 
        Vector vectOfCustReturnDeductDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of CustReturnDeduct Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfCustReturnDeductDoc = i_bosys.getListCustReturnDeduct(lLocationOid, dSelectedDate);            
            // --- end get list of CustReturnDeduct Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate CustReturnDeductJournal ---
            if(vectOfCustReturnDeductDoc!=null && vectOfCustReturnDeductDoc.size()>0)
            {  
                int maxDFToWhDoc = vectOfCustReturnDeductDoc.size();
                for(int i=0; i<maxDFToWhDoc; i++)
                {
                    IjCustReturnDeductDoc objIjCustReturnDeductDoc = (IjCustReturnDeductDoc) vectOfCustReturnDeductDoc.get(i);                                                
                    
                    long lResult = genJournalOfObjCustReturnDeduct(lLocationOid, objIjCustReturnDeductDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling CustReturnDeduct process skip ... ");
            }                
            // --- end iterate as long as document count to generate CustReturnDeductJournal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;             
    }     
    
    
    /**
     * Generate list of CustReturnDeduct journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjCustReturnDeductDoc</CODE>IjCustReturnDeductDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DF for costing (object IjInventoryOnDFDoc).
     * 	2. Pembuatan vector of jurnal detail posisi debet dengan location mapping dan kredit dengan account mapping.          
     *  3. Generate Journal dengan :
     *     3.1 Debet  : "Customer Return Deduct Value"
     *     3.2 Kredit : "Piutang"
     */    
    public long genJournalOfObjCustReturnDeduct(long lLocationOid, IjCustReturnDeductDoc objIjCustReturnDeductDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Customer Return Deduct (object objIjCustReturnDeductDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjCustReturnDeductDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data Customer Return Deduct.
        Vector vectOfObjIjJurDetail = genObjIjJDOfCustReturnDeduct(objIjCustReturnDeductDoc, objIjEngineParam);
        
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
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_CUST_RETURN_DEDUCTION, objIjJournalMain.getRefBoDocLastUpdate(), "Customer Return Deduction : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * @param <CODE>objIjCustReturnDeductDoc</CODE>IjCustReturnDeductDoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return  
     */    
    private IjJournalMain genObjIjJournalMain(IjCustReturnDeductDoc objIjCustReturnDeductDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjCustReturnDeductDoc.getDtDocDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());                 
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + objIjCustReturnDeductDoc.getSDocReference());  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjCustReturnDeductDoc.getLDocId());                
        objIjJournalMain.setRefBoDocNumber(objIjCustReturnDeductDoc.getSDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjCustReturnDeductDoc.getDtLastUpdate());                                        
        
        return objIjJournalMain;        
    }
    
    
    /**
     * Membuat detail utk posisi debet dengan mengambil Location Mapping (CUST_RETURN)
     * Membuat detail utk posisi kredit dengan mengambil Account Mapping (SALES INVOICE)
     *     
     * @param <CODE>objIjCustReturnDeductDoc</CODE>IjCustReturnDeductDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */    
    private Vector genObjIjJDOfCustReturnDeduct(IjCustReturnDeductDoc objIjCustReturnDeductDoc, IjEngineParam objIjEngineParam)
    {
        Vector vResult = new Vector(1,1);        
        

        // -- start debet --
        // Ambil Account Mapping ?ustomer Return?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingDebet = objPstIjAccountMapping.getObjIjAccountMapping(0, I_IJGeneral.TRANS_CUSTOMER_RETURN, objIjEngineParam.getLBookType());
        long accountAccChartDebet = objIjAccountMappingDebet.getAccount();                                                                                

        // pembuatan jurnal detail                
        if( accountAccChartDebet != 0 )
        {                
            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
            objIjJournalDetailDebet.setJdetAccChart(accountAccChartDebet);                            
            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailDebet.setJdetTransRate(1);                        
            objIjJournalDetailDebet.setJdetDebet(objIjCustReturnDeductDoc.getDTotalReturnValue());
            objIjJournalDetailDebet.setJdetCredit(0);                        
            vResult.add(objIjJournalDetailDebet);                                        
        }                


 
        // -- start kredit --
        // Ambil Account Mapping ?ales Invoicing?       
        IjAccountMapping objIjAccountMappingKredit = objPstIjAccountMapping.getObjIjAccountMapping(0, I_IJGeneral.TRANS_SALES, objIjEngineParam.getLBookType());
        long accountAccChartKredit = objIjAccountMappingKredit.getAccount();                                                                                

        // pembuatan jurnal detail
        if( accountAccChartKredit != 0 )
        {
            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
            objIjJournalDetailKredit.setJdetAccChart(accountAccChartKredit);                            
            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailKredit.setJdetTransRate(1);                        
            objIjJournalDetailKredit.setJdetDebet(0);
            objIjJournalDetailKredit.setJdetCredit(objIjCustReturnDeductDoc.getDTotalReturnValue());                             
            vResult.add(objIjJournalDetailKredit);                    
        }
        
        return vResult;
    }                  
}
