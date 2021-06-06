/*
 * SessPaymentOnLGR.java
 *
 * Created on January 18, 2005, 7:58 AM
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
public class SessPaymentOnLGR {
    
    // define journal note for transaction Payment on LGR
    public static String strJournalNote[] = 
    {
        "Transaksi pembayaran hutang di LGR",
        "Payment on LGR transaction"
    };    
    
    /**    
     * Generate list of Payment On LGR journal based on selected document on selected Bo system 
     *     
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of LGR Journal process
     * @created by Edhy
     *
     * @algoritm :     
     *  1. iterate as long as location count to generate PaymentOnLGRJournal
     */    
    public int generatePaymentOnLGRJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)
    {
        int result = 0;                
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid(); 
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++)
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generatePaymentOnLGRJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }        
        
        return result;         
    } 
    
    
    /** 
     * Generate list of Payment On LGR journal based on selected document on selected Bo system 
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *     
     * @return Number of LGR Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. get list of LGR Payment Document from selected BO system
     *  2. iterate as long as document count to generate PaymentOnLGRJournal
     */    
    public int generatePaymentOnLGRJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;                
        
        Vector vectOfPaymentOnLgrDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of LGR Payment Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                                
            vectOfPaymentOnLgrDoc = i_bosys.getListPaymentOnLGR(lLocationOid,dSelectedDate);            
            // --- end get list of LGR Payment Document from selected BO system ---

            
            // --- start iterate as long as document count to generate PaymentOnLGRJournal ---
            if(vectOfPaymentOnLgrDoc!=null && vectOfPaymentOnLgrDoc.size()>0)
            {
                int maxPaymentOnLgrDoc = vectOfPaymentOnLgrDoc.size();
                for(int i=0; i<maxPaymentOnLgrDoc; i++)
                {
                    IjPaymentOnLGRDoc objIjPaymentOnLGRDoc = (IjPaymentOnLGRDoc) vectOfPaymentOnLgrDoc.get(i);                
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjPaymentOnLGRDoc.getDocTransCurrency());
                    objIjPaymentOnLGRDoc.setDocTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjPaymentOnLGR(lLocationOid, objIjPaymentOnLGRDoc, iDocTypeReference, objIjEngineParam);                    
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            }        
            else
            {
                System.out.println(".::MSG : Because no document found, journaling Payment On LGR process skip ... ");
            }
            // --- end iterate as long as document count to generate PaymentOnLGRJournal ---
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        return result;         
    }               
    
    
    
    /** 
     * Generate LGR journal based on IjPurchaseOnLGRDoc
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjPaymentOnLGRDoc</CODE>IjPaymentOnLGRDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritm
     *  1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Payment On LGR (object IjPaymentOnLGRDoc).
     * 	2. Pembuatan jurnal detail posisi kredit (object IjJournalDetail) menggunakan Payment Mapping.
     *  3. Pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping "Goods Received".
     *  4. masukkan ke vector od IJ Jurnal Detail
     *  5. Generate Journal dengan :
     *     5.1 Debet  : "Goods Received"
     *     5.2 Kredit : "Payment Data"
     */    
    public long genJournalOfObjPaymentOnLGR(long lLocationOid, IjPaymentOnLGRDoc objIjPaymentOnLGRDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        String strStandartRate = (hashStandartRate.get(""+objIjPaymentOnLGRDoc.getDocTransCurrency())) != null ? ""+hashStandartRate.get(""+objIjPaymentOnLGRDoc.getDocTransCurrency()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);

        // 1. pembuatan object ij jurnal main        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjPaymentOnLGRDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. start pembuatan vector of object ij jurnal detail
        Vector vectOfObjIjJurDetail = new Vector(1,1);                 

            // 2.1 Kredit : Payment Mapping
            Vector vectObjIjJDOfPayment = genVectObjIjJDOfPayment(lLocationOid, objIjPaymentOnLGRDoc, hashStandartRate, objIjEngineParam);                                                            
             
            // 2.2 Debet : Account Mapping "Goods Received"
            double dTotPayment = getTotalPaymentData(vectObjIjJDOfPayment);  
            IjJournalDetail ObjIjJDOfPayableOnLGR = genObjIjJDOfPayableOnLGR(lLocationOid, objIjPaymentOnLGRDoc, dTotPayment, objIjEngineParam);                                            

        // 3. masukkan object masing-masing account ke dalam vector of jurnal detail    
        if(vectObjIjJDOfPayment!=null && vectObjIjJDOfPayment.size()>0)
        {
            vectOfObjIjJurDetail.addAll(vectObjIjJDOfPayment);
        }
            
        if(ObjIjJDOfPayableOnLGR.getJdetAccChart()!=0)
        {
            vectOfObjIjJurDetail.add(ObjIjJDOfPayableOnLGR);
        }            
 
        // 4. save jurnal ke database IJ 
        if(vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0)
        {
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_PAYMENT_ON_LGR, objIjJournalMain.getRefBoDocLastUpdate(), "Payment on LGR : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * @param <CODE>objIjPaymentOnLGRDoc</CODE>IjPaymentOnLGRDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Document type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalMain object 
     * @created by Edhy
     */    
    private IjJournalMain genObjIjJournalMain(IjPaymentOnLGRDoc objIjPaymentOnLGRDoc, int docTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();        
        
        objIjJournalMain.setJurTransDate(objIjPaymentOnLGRDoc.getDocTransDate());                
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjPaymentOnLGRDoc.getDocTransCurrency());                
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(docTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objIjPaymentOnLGRDoc.getDocId());                
        objIjJournalMain.setRefBoDocNumber(objIjPaymentOnLGRDoc.getDocNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objIjPaymentOnLGRDoc.getDtLastUpdate());                        
        
        return objIjJournalMain;        
    }    
    
    
    /**
     * pencarian account chart di sisi kredit berdasarkan nilai data purchase
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjPaymentOnLGRDoc</CODE>IjPaymentOnLGRDoc object as source of journal process
     * @param <CODE>hashStandartRate</CODE>Hashtable object that handle Standart Rate data
     *
     * @return vector of IjJournalDetail object 
     * @created by Edhy
     */    
    private Vector genVectObjIjJDOfPayment(long lLocationOid, IjPaymentOnLGRDoc objIjPaymentOnLGRDoc, Hashtable hashStandartRate, IjEngineParam objIjEngineParam)
    {                
        Vector vResult = new Vector(1,1);                                 
        
        Vector vectPayment = objIjPaymentOnLGRDoc.getListPayment();
        if(vectPayment!=null && vectPayment.size()>0)
        {
            PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
            
            int maxPayment = vectPayment.size();                        
            for(int j=0; j<maxPayment; j++)
            {
                IjPaymentDoc objPaymentDoc = (IjPaymentDoc) vectPayment.get(j); 
                
                IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objPaymentDoc.getPayCurrency());
                objPaymentDoc.setPayCurrency(objCurrencyMapping.getAisoCurrency());
                

                // pencarian account chart di sisi debet
                String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_LOCATION] + 
                                     " = " + lLocationOid +
                                     " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + 
                                     " = " + objPaymentDoc.getPayType();
                if(objPaymentDoc.getPayCurrency() != 0)
                {
                    whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + objPaymentDoc.getPayCurrency();
                }                        

                long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);                                                                                               
                String strStandartRate = (hashStandartRate.get(""+objPaymentDoc.getPayCurrency())) != null ? ""+hashStandartRate.get(""+objPaymentDoc.getPayCurrency()) : "1";
                double standartRate = Double.parseDouble(strStandartRate);

                // membuat detail utk posisi debet                        
                IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                objIjJournalDetail.setJdetAccChart(paymentAccChart);
                objIjJournalDetail.setJdetTransCurrency(objPaymentDoc.getPayCurrency());                        
                objIjJournalDetail.setJdetTransRate(standartRate);                        
                objIjJournalDetail.setJdetDebet(0);
                objIjJournalDetail.setJdetCredit((objPaymentDoc.getPayNominal()*standartRate));                 
                vResult.add(objIjJournalDetail); 
            }
        }        
        
        return vResult;
    }

    
    
    /**
     * @param <CODE>vIjJournalDetail</CODE>vector of IjJournalDetail which balance will be calculated
     *
     * @return TotalPaymentData
     * @created by Edhy
     */    
    private double getTotalPaymentData(Vector vIjJournalDetail)
    {
        return PstIjJournalDetail.getTotalOnCreditSide(vIjJournalDetail);    
    }
    
    
    
    /** 
     * pencarian account chart di sisi debet berdasarkan nilai data purchase
     *     
     * @param <CODE>lLocationOid</CODE>ID of location whre transaction occur     
     * @param <CODE>objIjPaymentOnLGRDoc</CODE>IjPaymentOnLGRDoc object as source of journal process
     * @param <CODE>dTotPayment</CODE>Total payment
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalDetail object on debet side
     * @created by Edhy
     */        
    private IjJournalDetail genObjIjJDOfPayableOnLGR(long lLocationOid, IjPaymentOnLGRDoc objIjPaymentOnLGRDoc, double dTotPayment, IjEngineParam objIjEngineParam)
    {                
        IjJournalDetail objIjJournalDetailCr = new IjJournalDetail(); 

        // Ambil Account Mapping ?oods Received?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(lLocationOid, I_IJGeneral.TRANS_GOODS_RECEIVE, objIjPaymentOnLGRDoc.getDocTransCurrency());                
        long goodReceiveAccChart = objIjAccountMapping.getAccount();

        if(goodReceiveAccChart != 0 && dTotPayment > 0)
        {
            objIjJournalDetailCr.setJdetAccChart(goodReceiveAccChart);                            
            objIjJournalDetailCr.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetailCr.setJdetTransRate(1);                        
            objIjJournalDetailCr.setJdetDebet(dTotPayment);                             
            objIjJournalDetailCr.setJdetCredit(0);                    
        }        
        
        return objIjJournalDetailCr;
    } 
    
}
