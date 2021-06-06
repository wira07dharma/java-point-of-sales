/*
 * SessDPOnPO.java
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
public class SessDPOnPO {    
    
    // define journal note for transaction DP On Purchase Order
    public static String strJournalNote[] = 
    {
        "Transaksi DP pada saat order barang, dokumen referensi : ",
        "DP On Purchase Order transaction, reference document : "
    };
    
    /**
     * Generate list of Dp On PO journal as long as location count 
     *     
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of DP On PO Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. iterate as long as location count to generate DPOnPOJournal
     */                                
    public int generateDPOnPOJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)
    {
        int result = 0;                
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid();         
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDPOnPOJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }
        
        return result;             
    }     
    
    
    /**
     * Generate list of Dp On PO journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of DP On PO Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. get list of DP on PO Document from selected BO system
     *  2. iterate as long as document count to generate DPOnPOJournal
     */                                
    public int generateDPOnPOJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)
    {
        int result = 0;                
        
        Vector vectOfDPOnPODoc = new Vector(1,1); 
        try
        {            
            // --- start get list of DP on PO Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();              
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfDPOnPODoc = i_bosys.getListDPonPurchaseOrder(lLocationOid, dSelectedDate);                        
            // --- end get list of DP on PO Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate DPOnPOJournal ---
            if(vectOfDPOnPODoc!=null && vectOfDPOnPODoc.size()>0)
            {
                int maxDPOnPODoc = vectOfDPOnPODoc.size();
                for(int i=0; i<maxDPOnPODoc; i++)
                {
                    IjDPOnPODoc objIjDPOnPODoc = (IjDPOnPODoc) vectOfDPOnPODoc.get(i);    
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjDPOnPODoc.getLTransCurrency());
                    objIjDPOnPODoc.setLTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjDPOnPO(lLocationOid, objIjDPOnPODoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Skip journaling process of DP on Purchase Order because no document found ... :(( ");
            }            
            // --- end iterate as long as document count to generate DPOnPOJournal ---
        }
        catch(Exception e)
        {
            System.out.println(new SessDPOnPO().getClass().getName()+".generateDPOnPO() Exc : " + e.toString());
        }               
        
        return result;             
    }        
    
    /** 
     * Generate Dp On PO journal based on IjDPOnPODoc
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjDPOnPODoc</CODE>IjDPOnPODoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * Pembayaran dengan Giro, Cheque, CC dan LC pada DP on PO tdk perlu buku pembantu
     * akan langsung mengurangi account bank.
     * Nantinya akan ada selisih saldo antara "rekening Bank(internal)" dengan saldo di "Bank".
     * Hal inilah yang akan memerlukan proses rekonsiliasi bank
     *
     * @return ID of DP On PO Journal 
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP pada saat PO (object IjDPOnPODoc).
     * 	2. Pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
     *  3. Pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping "DPonPurchaseOrder".
     *  4. masukkan ke vector od IJ Jurnal Detail
     *  5. Generate Journal dengan :
     *     5.1 Debet  : "DPonPurchaseOrder"
     *     5.2 Kredit : "Payment Data"
     */    
    public long genJournalOfObjDPOnPO(long lLocationOid, IjDPOnPODoc objIjDPOnPODoc, int iDocTypeReference, IjEngineParam objIjEngineParam)
    {
        long lResult = 0;       

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP pada saat PO (object IjDPOnPODoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjDPOnPODoc, iDocTypeReference, lLocationOid, objIjEngineParam);                
        
        // 2. pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.        
        IjJournalDetail objIjJDCredit = genObjIjJDOfPayment(lLocationOid, objIjDPOnPODoc, objIjEngineParam);                        
                
        // 3. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping PonPurchaseOrder
        IjJournalDetail objIjJDDebet = genObjIjJDOfDPOnPO(lLocationOid, objIjDPOnPODoc, objIjJDCredit.getJdetCredit(), objIjEngineParam);                                        
                
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
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_DP_ON_PURCHASE_ORDER, objIjJournalMain.getRefBoDocLastUpdate(), "DP on PO : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * generate journal main object based on IjDPOnPODoc object
     *
     * @param <CODE>objIjDPOnPODoc</CODE>IjDPOnPODoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalMain object
     * @created by Edhy
     */    
    private IjJournalMain genObjIjJournalMain(IjDPOnPODoc objIjDPOnPODoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjDPOnPODoc.getDtTransDate());                 
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjDPOnPODoc.getLTransCurrency());                
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]+objIjDPOnPODoc.getSNumber());  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjDPOnPODoc.getLDocOid());                
        objIjJournalMain.setRefBoDocNumber(objIjDPOnPODoc.getSNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjDPOnPODoc.getDtLastUpdate());        
        
        return objIjJournalMain;        
    }    
    
    
    /** 
     * Generate journal detail object on credit side based on IjDPOnPODoc object      
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjDPOnPODoc</CODE>IjDPOnPODoc object as source of journal process     
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalDetail object
     * @created by Edhy
     *
     * @algoritm : 
     *  1. Pencarian payment's account chart utk DP On PO dari "Payment Mapping" modul
     *  2. Generate IjJournalDetail object sisi kredit
     */    
    private IjJournalDetail genObjIjJDOfPayment(long lLocationOid, IjDPOnPODoc objIjDPOnPODoc, IjEngineParam objIjEngineParam) 
    {                         
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();  
        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        
        // --- Start Pencarian payment's account chart utk DP On PO dari "Payment Mapping" modul ---
        PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();        
        String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_LOCATION] + 
                             " = " + lLocationOid +        
                             " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + 
                             " = " + objIjDPOnPODoc.getLPaymentType();       
        
        long lTransCurrency = objIjDPOnPODoc.getLTransCurrency();
        if(lTransCurrency != 0)
        {
            whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + 
                          " = " + lTransCurrency;
        }                                                

        long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);                                                                                               
        String strStandartRate = (hashStandartRate.get(""+lTransCurrency)) != null ? ""+hashStandartRate.get(""+lTransCurrency) : "1";
        double standartRate = Double.parseDouble(strStandartRate);                                                                
        double dDPNominal = objIjDPOnPODoc.getDNominal() * standartRate;
        // --- End Pencarian payment's account chart utk DP On PO dari "Payment Mapping" modul ---
        
        
        // --- Start Generate IjJournalDetail object sisi kredit ---
        if(paymentAccChart != 0 && dDPNominal > 0)
        {            
            objIjJournalDetail.setJdetAccChart(paymentAccChart);
            objIjJournalDetail.setJdetTransCurrency(lTransCurrency);                        
            objIjJournalDetail.setJdetTransRate(standartRate);                        
            objIjJournalDetail.setJdetDebet(0); 
            objIjJournalDetail.setJdetCredit(dDPNominal);                                                                                     
        }
        // --- End Generate IjJournalDetail object sisi kredit ---
        
        return objIjJournalDetail;
    }    
    
    /** 
     * Generate journal detail object on debet side based on IjDPOnPODoc object
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjDPOnPODoc</CODE>IjDPOnPODoc object as source of journal process     
     * @param <CODE>totalTransValue</CODE>Total Nominal DP on transaction DP on PO
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalDetail object
     * @created by Edhy
     *
     * @algoritm : 
     *  1. Pencarian Receivable's account chart utk DP On PO dari "Account Mapping" modul
     *  2. Generate IjJournalDetail object sisi debet
     */    
    private IjJournalDetail genObjIjJDOfDPOnPO(long lLocationOid, IjDPOnPODoc objIjDPOnPODoc, double totalTransValue, IjEngineParam objIjEngineParam)
    {       
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();        
        
        // --- Start Pencarian Receivable's account chart utk DP On PO dari "Account Mapping" modul ---
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(lLocationOid, I_IJGeneral.TRANS_DP_ON_PURCHASE_ORDER, objIjDPOnPODoc.getLTransCurrency());
        long accChartDPOnPO = objIjAccountMapping.getAccount();                    
        // --- End Pencarian Receivable's account chart utk DP On PO dari "Account Mapping" modul ---
        
        
        System.out.println("objIjEngineParam.getLBookType() : " + objIjEngineParam.getLBookType());
        
        // --- Start Generate IjJournalDetail object sisi debet ---
        if(accChartDPOnPO != 0 && totalTransValue > 0)
        {            
            objIjJournalDetail.setJdetAccChart(accChartDPOnPO);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetail.setJdetTransRate(1);
            objIjJournalDetail.setJdetDebet(totalTransValue);                                            
            objIjJournalDetail.setJdetCredit(0);                
        }
        // --- End Generate IjJournalDetail object sisi debet ---
        
        return objIjJournalDetail;
    }
    
}