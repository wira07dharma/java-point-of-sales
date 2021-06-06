/*
 * SessDPOnPdO.java
 *
 * Created on January 18, 2005, 7:56 AM
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
 * @author  gedhy
 */
public class SessDPOnPdO {
    
    // define journal note for transaction DP On Production Order
    public static String strJournalNote[] = 
    {
        "Transaksi DP pada saat order produksi barang, dokumen referensi : ",
        "DP On Production Order transaction, reference document : "
    };
    
    
    /** 
     * Generate list of DP On PdO journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */        
    public int generateDPOnPdOJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)   
    {
        int result = 0;        

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDPOnPdOJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }                
        
        return result;         
    }    
    
    
    /** 
     * Generate list of DP On PdO journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. get list of DP On PdO Document from selected BO system
     *  2. iterate as long as document count to generate DPOnPdOJournal
     */        
    public int generateDPOnPdOJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        

        // 1. Ambil data DP (refer to IJ-Interface getDPonPurchaseOrder) 
        Vector vectOfDPOnPdODoc = new Vector(1,1); 
        try
        {            
            // --- start get list of DP On PdO Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfDPOnPdODoc = i_bosys.getListDPonProductionOrder(lLocationOid, dSelectedDate);            
            // --- end get list of DP On PdO Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate DPOnPdOJournal ---
            if(vectOfDPOnPdODoc!=null && vectOfDPOnPdODoc.size()>0)
            {
                int maxDPOnPdODoc = vectOfDPOnPdODoc.size();
                for(int i=0; i<maxDPOnPdODoc; i++)
                {
                    IjDPOnPdODoc objDPOnPdODoc = (IjDPOnPdODoc) vectOfDPOnPdODoc.get(i);  
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objDPOnPdODoc.getLTransCurrency());
                    objDPOnPdODoc.setLTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjDPOnPdO(lLocationOid, objDPOnPdODoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling DP on Production Order process skip ... ");
            }            
            // --- end iterate as long as document count to generate DPOnPdOJournal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;         
    }       
    
    
    /**
     * Generate list of DP On PdO journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjDPOnPdODoc</CODE>IjDPOnPdODoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP On PdO (object IjDPOnPdODoc).
     * 	2. Pembuatan jurnal detail posisi kredit (object IjJournalDetail) menggunakan Payment Mapping.
     *  3. Pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping "DPonProductionOrder".
     *  4. masukkan ke vector od IJ Jurnal Detail
     *  5. Generate Journal dengan :
     *     5.1 Debet  : "Payment Data"
     *     5.2 Kredit : "DPonProductionOrder"
     */     
    public long genJournalOfObjDPOnPdO(long lLocationOid, IjDPOnPdODoc objIjDPOnPdODoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;               
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP pada saat PO (object objIjDPOnPdODoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjDPOnPdODoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.                
        IjJournalDetail objIjJDCredit = genObjIjJDOfPayment(lLocationOid, objIjDPOnPdODoc, objIjEngineParam);                        

        // 3. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping "DPonProductionOrder".
        IjJournalDetail objIjJDDebet = genObjIjJDOfDPOnPdO(lLocationOid, objIjDPOnPdODoc, objIjJDCredit.getJdetCredit(), objIjEngineParam);                                        
        
        // 4. masukkan ke vector od IJ Jurnal Detail
        Vector vectOfObjIjJurDetail = new Vector(1,1); 
        if(objIjJDCredit.getJdetAccChart()!=0 && objIjJDDebet.getJdetAccChart()!=0)
        {
            vectOfObjIjJurDetail.add(objIjJDDebet);
            vectOfObjIjJurDetail.add(objIjJDCredit); 
            
            // 5. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_DP_ON_PRODUCTION_ORDER, objIjJournalMain.getRefBoDocLastUpdate(), "DP on PdO : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * generate journal main object based on IjDPOnPdODoc object
     *
     * @param <CODE>objDPOnPdODoc</CODE>IjDPOnPdODoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Document type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalMain object
     * @created by Edhy
     */        
    private IjJournalMain genObjIjJournalMain(IjDPOnPdODoc objDPOnPdODoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)    
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                        
        
        objIjJournalMain.setJurTransDate(objDPOnPdODoc.getDtTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objDPOnPdODoc.getLTransCurrency());                
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]+objDPOnPdODoc.getSNumber());  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objDPOnPdODoc.getLDocOid());                
        objIjJournalMain.setRefBoDocNumber(objDPOnPdODoc.getSNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objDPOnPdODoc.getDtLastUpdate());                                
        
        return objIjJournalMain;        
    }    
    
    
    /** 
     * pencarian account chart di sisi kredit berdasarkan data payment
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objDPOnPdODoc</CODE>IjDPOnPdODoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalDetail object on credit side
     * @created by Edhy
     */        
    private IjJournalDetail genObjIjJDOfPayment(long lLocationOid, IjDPOnPdODoc objDPOnPdODoc, IjEngineParam objIjEngineParam)
    {     
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();                
        
        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        
        PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();        
        String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_LOCATION] + 
                             " = " + lLocationOid +
                             " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + 
                             " = " + objDPOnPdODoc.getLPaymentType();
        long lTransCurrency = objDPOnPdODoc.getLTransCurrency();
        if(lTransCurrency != 0)
        {
            whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + lTransCurrency;
        }                                                
        
        long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);                                                                                               
        String strStandartRate = (hashStandartRate.get(""+lTransCurrency)) != null ? ""+hashStandartRate.get(""+lTransCurrency) : "1";
        double standartRate = Double.parseDouble(strStandartRate);                                                                
        double dDPNominal = objDPOnPdODoc.getDNominal() * standartRate;
        
        if(paymentAccChart != 0 && dDPNominal > 0)
        {            
            objIjJournalDetail.setJdetAccChart(paymentAccChart);
            objIjJournalDetail.setJdetTransCurrency(lTransCurrency);                        
            objIjJournalDetail.setJdetTransRate(standartRate);                        
            objIjJournalDetail.setJdetDebet(0); 
            objIjJournalDetail.setJdetCredit(dDPNominal);                                                                                     
        }
        
        return objIjJournalDetail;         
    }
    
 
    /** 
     * Membuat detail utk posisi debet dengan mengambil Account Mapping PonPurchaseOrder     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objDPOnPdODoc</CODE>IjDPOnPdODoc object as source of journal process
     * @param <CODE>totalTransValue</CODE>Total transaction
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */    
    private IjJournalDetail genObjIjJDOfDPOnPdO(long lLocationOid, IjDPOnPdODoc objDPOnPdODoc, double totalTransValue, IjEngineParam objIjEngineParam)
    {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();        

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        
        // Ambil Account Mapping "DPonProductionOrder"
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(lLocationOid, I_IJGeneral.TRANS_DP_ON_PRODUCTION_ORDER, objDPOnPdODoc.getLTransCurrency());

        long accChartDPOnPdO = objIjAccountMapping.getAccount();                    
        String strStandartRate = (hashStandartRate.get(""+objIjEngineParam.getLBookType())) != null ? ""+hashStandartRate.get(""+objIjEngineParam.getLBookType()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);

        if( accChartDPOnPdO != 0)
        {            
            objIjJournalDetail.setJdetAccChart(accChartDPOnPdO);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetail.setJdetTransRate(1);
            objIjJournalDetail.setJdetDebet(totalTransValue);                                            
            objIjJournalDetail.setJdetCredit(0);                            
        }
           
        return objIjJournalDetail;
    }   
    
}
