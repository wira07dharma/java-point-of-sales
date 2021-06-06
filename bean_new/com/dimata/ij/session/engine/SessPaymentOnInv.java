/*
 * SessPaymentOnInv.java
 *
 * Created on January 18, 2005, 7:59 AM
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
public class SessPaymentOnInv {

    // define journal note for transaction Payment on LGR
    public static String strJournalNote[] = 
    {
        "Transaksi pembayaran piutang oleh customer",
        "Payment transaction on Invoice by customer"
    };
    
    
    /**    
     * Generate list of payment on invoice journal based on selected document on selected Bo system 
     *     
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of Payment On Invoice Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. iterate as long as location count to generate PaymentOnInvoiceJournal
     */                                                                                   
    public int generatePaymentOnInvoiceJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;                  

        Vector vLocationOid = objIjEngineParam.getVLocationOid();                 
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generatePaymentOnInvoiceJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }
        
        return result;             
    }
    
    
    /**    
     * Generate list of Payment On Invoice journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of Payment On Invoice Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. get list of Payment On Invoice Document from selected BO system 
     *  2. iterate as long as document count to generate PaymentOnInvoiceJournal 
     */                                                                                   
    public int generatePaymentOnInvoiceJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;                  

        // 1. Ambil data DP (refer to IJ-Interface getListSalesOnInvoice) 
        Vector vectOfPaymentOnInvDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of LGR Document from selected BO system ---                    
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                                
            vectOfPaymentOnInvDoc = i_bosys.getListPaymentOnInvoice(lLocationOid, dSelectedDate);            
            // --- end get list of LGR Document from selected BO system ---   

            // --- start iterate as long as document count to generate LGRJournal ---            
            if(vectOfPaymentOnInvDoc!=null && vectOfPaymentOnInvDoc.size()>0)
            {
                int maxPaymentOnInvDoc = vectOfPaymentOnInvDoc.size();
                for(int i=0; i<maxPaymentOnInvDoc; i++)
                {
                    IjPaymentOnInvDoc objIjPaymentOnInvDoc = (IjPaymentOnInvDoc) vectOfPaymentOnInvDoc.get(i);                
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjPaymentOnInvDoc.getDocTransCurrency());
                    objIjPaymentOnInvDoc.setDocTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjPaymentOnInvoice(lLocationOid, objIjPaymentOnInvDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling Sales on Invoice process skip ... ");
            }            
            // --- end iterate as long as document count to generate LGRJournal ---            
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }                
        
        return result;             
    }           

    
    /**
     *
     * @param <CODE>objIjPaymentOnInvDoc</CODE>object IjPaymentOnInvDoc     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return    
     */    
    public long genJournalOfObjPaymentOnInvoice(long lLocationOid, IjPaymentOnInvDoc objIjPaymentOnInvDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)
    {
        long lResult = 0;       
        
        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        String strStandartRate = (hashStandartRate.get(""+objIjPaymentOnInvDoc.getDocTransCurrency())) != null ? ""+hashStandartRate.get(""+objIjPaymentOnInvDoc.getDocTransCurrency()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Penjualan (object IjSalesOnInvDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjPaymentOnInvDoc, iDocTypeReference, lLocationOid, objIjEngineParam);                        

        // 2. start pembuatan vector of object ij jurnal detail
        Vector vectOfObjIjJurDetail = new Vector(1,1);        
        
            // 2.1 pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
            Vector vTotalTransValue = genObjIjJournalDetailBaseOnPayment(objIjPaymentOnInvDoc, objIjEngineParam);
            double totalTransValue = getTotalPaymentAmount(vTotalTransValue);                

            // 2.2 pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping PonPurchaseOrder
            Vector vSalesOnIncoive = genObjIjJournalDetailSalesOnInvoice(objIjPaymentOnInvDoc, totalTransValue, objIjEngineParam);                        
        
        // 3. masukkan object masing-masing account ke dalam vector of jurnal detail            
        if(vTotalTransValue!=null && vTotalTransValue.size()>0)
        {
            vectOfObjIjJurDetail.addAll(vTotalTransValue);
        }

        if(vSalesOnIncoive!=null && vSalesOnIncoive.size()>0)
        {
            vectOfObjIjJurDetail.addAll(vSalesOnIncoive);
        }
 
        // 4. save jurnal ke database IJ
        if(vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0)
        {
            System.out.println("Debet  : " + PstIjJournalDetail.getTotalOnDebetSide(vectOfObjIjJurDetail));
            System.out.println("Credit : " + PstIjJournalDetail.getTotalOnCreditSide(vectOfObjIjJurDetail));
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_PAYMENT_ON_INV, objIjJournalMain.getRefBoDocLastUpdate(), "Payment on Invoice : " + objIjJournalMain.getRefBoDocNumber());                
                
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
    // --- end of credit sales ---

    
    private IjJournalMain genObjIjJournalMain(IjPaymentOnInvDoc objIjPaymentOnInvDoc, int docTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objIjPaymentOnInvDoc.getDocTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjPaymentOnInvDoc.getDocTransCurrency());                
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + " "+objIjPaymentOnInvDoc.getDescription()); // strJournalNote[objIjEngineParam.getILanguage()]);
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(docTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjPaymentOnInvDoc.getDocId());
        objIjJournalMain.setRefSubBoDocOid(objIjPaymentOnInvDoc.getOidDocRefBo());
        objIjJournalMain.setRefBoDocNumber(objIjPaymentOnInvDoc.getDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjPaymentOnInvDoc.getDtLastUpdate());                                                
        
        return objIjJournalMain;        
    }    
    
    
    private Vector genObjIjJournalDetailBaseOnPayment(IjPaymentOnInvDoc objIjPaymentOnInvDoc, IjEngineParam objIjEngineParam)
    {        
        Vector vResult = new Vector(1,1);
        
        Vector vectPayment = objIjPaymentOnInvDoc.getListPayment();
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
                    objIjJournalDetail.setJdetDebet((objPaymentDoc.getPayNominal()*standartRatePay));
                    objIjJournalDetail.setJdetCredit(0);                 
                    vResult.add(objIjJournalDetail);
                }
            }
        }
        
        return vResult;
    }
    
    
    
    private Vector genObjIjJournalDetailSalesOnInvoice(IjPaymentOnInvDoc objIjPaymentOnInvDoc, double totalTransValue, IjEngineParam objIjEngineParam)
    {
        Vector vResult = new Vector(1,1);
        
        PstIjAccountMapping objPstIjAccountMappingCr = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingCr = objPstIjAccountMappingCr.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjPaymentOnInvDoc.getDocTransCurrency());                
        long salesInvAccChart = objIjAccountMappingCr.getAccount();

        if(salesInvAccChart != 0)
        {
            IjJournalDetail objIjJournalDetailCr = new IjJournalDetail(); 
            objIjJournalDetailCr.setJdetAccChart(salesInvAccChart);                            
            objIjJournalDetailCr.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailCr.setJdetTransRate(1);                        
            objIjJournalDetailCr.setJdetDebet(0);                             
            objIjJournalDetailCr.setJdetCredit(totalTransValue);                    
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
