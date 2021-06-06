/*
 * SessPaymentOnCommision.java
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
public class SessPaymentOnCommision {
    
    // define journal note for transaction PaymentOnCommision
    public static String strJournalNote[] = 
    {
        "Transaksi pembayaran komisi","Payment on PaymentOnCommision transaction"        
    };
    
  
    /**
     * Generate list of Inventory On Payment on Commision journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */                                                                
    public int generatePaymentOnCommisionJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generatePaymentOnCommisionJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }                
        
        return result;             
    }     

    
    /*
     * Generate list of Payment on Commision journal based on selected document on selected Bo system
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
     *  2. iterate as long as document count to generate PaymentOnCommisionJournal
     */    
    public int generatePaymentOnCommisionJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;        
        
        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR) 
        Vector vectOfPaymentOnCommisionDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of PaymentOnCommision Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfPaymentOnCommisionDoc = i_bosys.getListPaymentOnCommision(lLocationOid, dSelectedDate);            
            // --- end get list of PaymentOnCommision Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate PaymentOnCommisionJournal ---
            if(vectOfPaymentOnCommisionDoc!=null && vectOfPaymentOnCommisionDoc.size()>0)
            {  
                int maxDFToWhDoc = vectOfPaymentOnCommisionDoc.size();
                for(int i=0; i<maxDFToWhDoc; i++)
                {
                    IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc = (IjPaymentOnCommisionDoc) vectOfPaymentOnCommisionDoc.get(i);                
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjPaymentOnCommisionDoc.getDocTransCurrency());
                    objIjPaymentOnCommisionDoc.setDocTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjPaymentOnCommision(lLocationOid, objIjPaymentOnCommisionDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling PaymentOnCommision process skip ... ");
            }                
            // --- end iterate as long as document count to generate PaymentOnCommisionJournal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;             
    }     
    
    
    /**
     * Generate list of PaymentOnCommision journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjPaymentOnCommisionDoc</CODE>IjPaymentOnCommisionDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm :
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen PaymentOnCommision (object IjPaymentOnCommisionDoc).
     * 	2. Pembuatan vector of jurnal detail posisi debet dengan location mapping dan kredit dengan account mapping.          
     *  3. Generate Journal dengan :
     *     3.1 Debet  : "PaymentOnCommision Expense"
     *     3.2 Kredit : "Payable"
     */    
    public long genJournalOfObjPaymentOnCommision(long lLocationOid, IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Payment on PaymentOnCommision (object objIjPaymentOnCommisionDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjPaymentOnCommisionDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data Payment on PaymentOnCommision.
        //Vector vectOfObjIjJurDetail = genObjIjJDOfPaymentOnCommision(objIjPaymentOnCommisionDoc, objIjEngineParam);
        
        // 2. start pembuatan vector of object ij jurnal detail
        Vector vectOfObjIjJurDetail = new Vector(1,1);        
        
            // 2.1 pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
            Vector vTotalTransValue = genObjIjJournalDetailBaseOnPayment(objIjPaymentOnCommisionDoc, objIjEngineParam);
            double totalTransValue = getTotalPaymentAmount(vTotalTransValue);                

            // 2.2 pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping Goods Receive
            Vector vPaymentOnCommision = genObjIjJournalDetailPaymentOnCommision(objIjPaymentOnCommisionDoc, totalTransValue, objIjEngineParam);                                
        
            
        // 3. masukkan object masing-masing account ke dalam vector of jurnal detail            
        if(vTotalTransValue!=null && vTotalTransValue.size()>0)
        {
            vectOfObjIjJurDetail.addAll(vTotalTransValue);
        }

        if(vPaymentOnCommision!=null && vPaymentOnCommision.size()>0)
        {
            vectOfObjIjJurDetail.addAll(vPaymentOnCommision);
        }
            
        // 4. save jurnal ke database IJ 
        if(vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0)
        {
            // 5. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_PAYMENT_ON_COMMISION, objIjJournalMain.getRefBoDocLastUpdate(), "Payment on Commission : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * @param <CODE>objIjPaymentOnCommisionDoc</CODE>IjPaymentOnCommisionDoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return  
     */    
    private IjJournalMain genObjIjJournalMain(IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjPaymentOnCommisionDoc.getDocTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());                 
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjPaymentOnCommisionDoc.getDocId());                
        objIjJournalMain.setRefBoDocNumber(objIjPaymentOnCommisionDoc.getDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjPaymentOnCommisionDoc.getDtLastUpdate());                                        
        
        return objIjJournalMain;        
    }
    
    
    private Vector genObjIjJournalDetailBaseOnPayment(IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc, IjEngineParam objIjEngineParam)
    {        
        Vector vResult = new Vector(1,1);
        
        Vector vectPayment = objIjPaymentOnCommisionDoc.getListPayment();
        if(vectPayment!=null && vectPayment.size()>0)
        {
            Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
            int maxPayment = vectPayment.size();
            PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
            for(int j=0; j<maxPayment; j++)
            {
                IjPaymentDoc objPaymentDoc = (IjPaymentDoc) vectPayment.get(j);     
                IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objPaymentDoc.getPayCurrency());
                objPaymentDoc.setPayCurrency(objCurrencyMapping.getAisoCurrency());                

                // pencarian account chart di sisi debet
                String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + objPaymentDoc.getPayType();
                if(objPaymentDoc.getPayCurrency() != 0)
                {
                    whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + objPaymentDoc.getPayCurrency();
                }                        

                long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);                                                                                               
                
                if(paymentAccChart != 0)
                {
                    String strStandartRatePay = (hashStandartRate.get(""+objPaymentDoc.getPayCurrency())) != null ? ""+hashStandartRate.get(""+objPaymentDoc.getPayCurrency()) : "1";
                    double standartRatePay = Double.parseDouble(strStandartRatePay);                            

                    // membuat detail utk posisi debet                        
                    IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                    objIjJournalDetail.setJdetAccChart(paymentAccChart);
                    objIjJournalDetail.setJdetTransCurrency(objPaymentDoc.getPayCurrency());                        
                    objIjJournalDetail.setJdetTransRate(standartRatePay);                        
                    objIjJournalDetail.setJdetDebet(0);                                     
                    objIjJournalDetail.setJdetCredit((objPaymentDoc.getPayNominal()*standartRatePay));                    
                    vResult.add(objIjJournalDetail);
                }
            }
        }
        
        return vResult;
    }
    
    
    
    private Vector genObjIjJournalDetailPaymentOnCommision(IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc, double totalTransValue, IjEngineParam objIjEngineParam)
    {
        Vector vResult = new Vector(1,1);
        
        PstIjAccountMapping objPstIjAccountMappingCr = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingCr = objPstIjAccountMappingCr.getObjIjAccountMapping(I_IJGeneral.TRANS_GOODS_RECEIVE, objIjPaymentOnCommisionDoc.getDocTransCurrency());                
        long commisionAccChart = objIjAccountMappingCr.getAccount();

        if(commisionAccChart != 0)
        {
            IjJournalDetail objIjJournalDetailCr = new IjJournalDetail(); 
            objIjJournalDetailCr.setJdetAccChart(commisionAccChart);                            
            objIjJournalDetailCr.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailCr.setJdetTransRate(1);                        
            objIjJournalDetailCr.setJdetDebet(totalTransValue);                             
            objIjJournalDetailCr.setJdetCredit(0);                    
            vResult.add(objIjJournalDetailCr);                            
        }
        return vResult;
    }
    
    
    /**
     * @param vIjJournalDetail
     * @return
     */    
    private double getTotalPaymentAmount(Vector vIjJournalDetail)
    {
        return PstIjJournalDetail.getTotalOnDebetSide(vIjJournalDetail);
    }        
}
