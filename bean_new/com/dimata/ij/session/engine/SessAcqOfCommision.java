/*
 * SessAcqOfCommision.java
 *
 * Created on January 10, 2006, 7:16 AM
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
public class SessAcqOfCommision {
    
    // define journal note for transaction Commision
    public static String strJournalNote[] = 
    {
        "Transaksi pengakuan komisi","Aquisition of commision transaction"        
    };
    
  
    /**
     * Generate list of Inventory On Acquisition of Commision journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */                                                                
    public int generateCommisionJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateCommisionJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }                
        
        return result;             
    }     

    
    /*
     * Generate list of Acquisition of Commision journal based on selected document on selected Bo system
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
     *  2. iterate as long as document count to generate CommisionJournal
     */    
    public int generateCommisionJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR) 
        Vector vectOfCommisionDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of Commision Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfCommisionDoc = i_bosys.getListCommision(lLocationOid, dSelectedDate);            
            // --- end get list of Commision Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate CommisionJournal ---
            if(vectOfCommisionDoc!=null && vectOfCommisionDoc.size()>0)
            {  
                int maxDFToWhDoc = vectOfCommisionDoc.size();
                for(int i=0; i<maxDFToWhDoc; i++)
                {
                    IjCommisionDoc objIjCommisionDoc = (IjCommisionDoc) vectOfCommisionDoc.get(i);                                                
                    
                    long lResult = genJournalOfObjCommision(lLocationOid, objIjCommisionDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling Commision process skip ... ");
            }                
            // --- end iterate as long as document count to generate CommisionJournal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;             
    }     
    
    
    /**
     * Generate list of Commision journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjCommisionDoc</CODE>IjCommisionDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Commision (object IjCommisionDoc).
     * 	2. Pembuatan vector of jurnal detail posisi debet dengan location mapping dan kredit dengan account mapping.          
     *  3. Generate Journal dengan :
     *     3.1 Debet  : "Commision Expense"
     *     3.2 Kredit : "Payable"
     */    
    public long genJournalOfObjCommision(long lLocationOid, IjCommisionDoc objIjCommisionDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Acquisition of Commision (object objIjCommisionDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjCommisionDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data Acquisition of Commision.
        Vector vectOfObjIjJurDetail = genObjIjJDOfCommision(objIjCommisionDoc, objIjEngineParam);
        
        // 3. save jurnal ke database IJ 
        if(vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0)
        {
            // 4. generate Journal, update logger n posting to AISO if setting is automatically
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);        
                
                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_ACQ_OF_COMMISION, objIjJournalMain.getRefBoDocLastUpdate(), "Commission : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * @param <CODE>objIjCommisionDoc</CODE>IjCommisionDoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return  
     */    
    private IjJournalMain genObjIjJournalMain(IjCommisionDoc objIjCommisionDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjCommisionDoc.getDtDocTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());                 
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjCommisionDoc.getLDocId());                
        objIjJournalMain.setRefBoDocNumber(objIjCommisionDoc.getSDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjCommisionDoc.getDtLastUpdate());                                        
        
        return objIjJournalMain;        
    }
    
    
    /**
     * Membuat detail utk posisi debet dengan mengambil Location Mapping (Commision Expense)
     * Membuat detail utk posisi kredit dengan mengambil Account Mapping (Goods Receive / Payable)
     *     
     * @param <CODE>objIjCommisionDoc</CODE>IjCommisionDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */    
    private Vector genObjIjJDOfCommision(IjCommisionDoc objIjCommisionDoc, IjEngineParam objIjEngineParam)
    {
        Vector vResult = new Vector(1,1);        
        

        // -- start debet --
        // Ambil Transaction Location Mapping ?cquisition of Commision?
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_COMMISION, -1, 0, 0, 0);
        long locationAccChartDebet = objIjLocationMappingDebet.getAccount();                                                                                

        // pembuatan jurnal detail                
        if( locationAccChartDebet != 0 )
        {                
            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
            objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);                            
            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailDebet.setJdetTransRate(1);                        
            objIjJournalDetailDebet.setJdetDebet(objIjCommisionDoc.getDTotalCommision());
            objIjJournalDetailDebet.setJdetCredit(0);                        
            vResult.add(objIjJournalDetailDebet);                                        
        }                


 
        // -- start kredit --
        // Ambil Account Mapping ?oods Receive / Payable?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingKredit = objPstIjAccountMapping.getObjIjAccountMapping(0, I_IJGeneral.TRANS_GOODS_RECEIVE, objIjEngineParam.getLBookType());
        long accountAccChartKredit = objIjAccountMappingKredit.getAccount();                                                                                

        // pembuatan jurnal detail
        if( accountAccChartKredit != 0 )
        {
            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
            objIjJournalDetailKredit.setJdetAccChart(accountAccChartKredit);                            
            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailKredit.setJdetTransRate(1);                        
            objIjJournalDetailKredit.setJdetDebet(0);
            objIjJournalDetailKredit.setJdetCredit(objIjCommisionDoc.getDTotalCommision());                             
            vResult.add(objIjJournalDetailKredit);                    
        }
        
        return vResult;
    }                   
}
