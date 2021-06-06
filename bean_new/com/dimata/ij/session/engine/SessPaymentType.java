/*
 * SessPaymentType.java
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
public class SessPaymentType {
    
    // define journal note for transaction DP On Production Order
    public static String strJournalNote[] = 
    {
        "Transaksi DP pada saat pencarian BG/Cheque/CC/LC",
        "BG/Cheque/CC/LC cliring transaction"
    };
    
    
    /**
     * Generate list of BGChequeCCLCCliringJournal as long as location count 
     *     
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of DP On PO Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. iterate as long as location count to generate BGChequeCCLCCliringJournal
     */        
    public int generateCliringJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;                
        
        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if(vLocationOid!=null && vLocationOid.size()>0)
        {
            int iLocationCount = vLocationOid.size();
            for(int i=0; i<iLocationCount; i++) 
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateCliringJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }
        
        return result;          
    }           

    
    /**
     * Generate list of BGChequeCCLCCliringJournal as long as location count 
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>dTransDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return Number of DP On PO Journal process
     * @created by Edhy
     *
     * @algoritm :
     *  1. get list of Payment Type Document from selected BO system
     *  2. iterate as long as document count to generate BGChequeCCLCCliringJournal
     */        
    public int generateCliringJournal(long lLocationOid, Date dTransDate, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        int result = 0;                        

        // 1. Ambil data DP (refer to IJ-Interface getListPaymentType) 
        Vector vectOfPaymentTypeDoc = new Vector(1,1); 
        try
        {            
            // --- start get list of Payment Type Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();                                    
            vectOfPaymentTypeDoc = i_bosys.getListPaymentTypePostedCleared(lLocationOid, dTransDate);           
            // --- end get list of Payment Type Document from selected BO system ---
            
            
            // --- start iterate as long as document count to generate BGChequeCCLCCliringJournal ---
            if(vectOfPaymentTypeDoc!=null && vectOfPaymentTypeDoc.size()>0)
            {
                int maxPaymentTypeDoc = vectOfPaymentTypeDoc.size();
                for(int i=0; i<maxPaymentTypeDoc; i++)
                {
                    IjPaymentTypeDoc objPaymentTypeDoc = (IjPaymentTypeDoc) vectOfPaymentTypeDoc.get(i);                
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objPaymentTypeDoc.getPayCurrency());
                    objPaymentTypeDoc.setPayCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjPaymentType(lLocationOid, objPaymentTypeDoc, iDocTypeReference, objIjEngineParam);                    
                    if(lResult != 0)
                    {
                        result++;
                    }
                }                
            }
            else
            {
                System.out.println(".::MSG : Because no document found, journaling Clearing Giro, Cheque, CC or LC process skip ... ");
            }            
            // --- end iterate as long as document count to generate BGChequeCCLCCliringJournal ---
        }
        catch(Exception e)
        {
            System.out.println(".::ERR : Exception when instantiate interface - BGChequeCCLC");
        }        
        
        return result;         
    }               
    
    
    /**
     * Generate Payment Type journal based on IjPaymentTypeDoc
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjPaymentTypeDoc</CODE>IjPaymentTypeDoc object as source of journal process          
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */    
    public long genJournalOfObjPaymentType(long lLocationOid, IjPaymentTypeDoc objPaymentTypeDoc, int iDocTypeReference, IjEngineParam objIjEngineParam)    
    {
        long lResult = 0;       

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP pada saat PO (object IjDPOnPODoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objPaymentTypeDoc, iDocTypeReference, lLocationOid, objIjEngineParam);        

        // 2. pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
        Vector vPayment = genObjIjJournalDetailBaseOnPayment(lLocationOid, objPaymentTypeDoc, objIjEngineParam);
        double totalTransValue = getTotalPaymentAmount(vPayment);                

        // 3. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping PonPurchaseOrder
        Vector vPaymentType = genObjIjJournalDetailBaseOnPaymentType(objPaymentTypeDoc, totalTransValue, objIjEngineParam);
        
        // 4. masukkan ke vector od IJ Jurnal Detail
        Vector vectOfObjIjJurDetail = new Vector(1,1); 
        if((vPayment!=null && vPayment.size()>0) && (vPaymentType!=null && vPaymentType.size()>0))
        {
            vectOfObjIjJurDetail.addAll(vPaymentType);
            vectOfObjIjJurDetail.addAll(vPayment); 
            
            // 5. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if( (vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>1) && bJournalBalance)
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_PAYMENT_TYPE_POSTED_CLEARED, objIjJournalMain.getRefBoDocLastUpdate(), "Payment Type : " + objIjJournalMain.getRefBoDocNumber());                
                
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
     * generate journal main object based on IjPaymentTypeDoc object
     *
     * @param <CODE>objIjPaymentTypeDoc</CODE>IjPaymentTypeDoc object as source of journal process     
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return IjJournalMain object
     * @created by Edhy
     */    
    private IjJournalMain genObjIjJournalMain(IjPaymentTypeDoc objPaymentTypeDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam)    
    {
        IjJournalMain objIjJournalMain = new IjJournalMain();                       
        
        objIjJournalMain.setJurTransDate(objPaymentTypeDoc.getPayTransDate());                 
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objPaymentTypeDoc.getPayCurrency());                
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);  
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);                  
        objIjJournalMain.setRefBoDocOid(objPaymentTypeDoc.getPayId());                
        objIjJournalMain.setRefBoDocNumber(objPaymentTypeDoc.getPayNumber());      
        objIjJournalMain.setRefBoLocation(lLocationOid);        
        objIjJournalMain.setRefBoDocLastUpdate(objPaymentTypeDoc.getDtLastUpdate());                                                        
        
        return objIjJournalMain;         
    }        
    
    
    /**
     * generate journal detail object on credit side based on objIjPaymentTypeDoc object
     *
     * @param <CODE>lLocationOid</CODE>location where transaction occur
     * @param <CODE>objIjPaymentTypeDoc</CODE>IjPaymentTypeDoc object as source of journal process     
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return vector of IjJournalDetail object
     * @created by Edhy
     */    
    private Vector genObjIjJournalDetailBaseOnPayment(long lLocationOid, IjPaymentTypeDoc objPaymentTypeDoc, IjEngineParam objIjEngineParam)    
    {
        Vector vResult = new Vector(1,1);        

        PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
        String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_LOCATION] + " = " + lLocationOid +
                             " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + objPaymentTypeDoc.getPayType();
        if(objPaymentTypeDoc.getPayCurrency() != 0)
        {
            whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + objPaymentTypeDoc.getPayCurrency();
        }                    

        long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);
        if(paymentAccChart != 0)
        {
            Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();            
            String strStandartRate = (hashStandartRate.get(""+objPaymentTypeDoc.getPayCurrency())) != null ? ""+hashStandartRate.get(""+objPaymentTypeDoc.getPayCurrency()) : "1";
            double standartRate = Double.parseDouble(strStandartRate);                                    
            
            // membuat detail utk posisi kredit
            IjJournalDetail objIjJournalDetail = new IjJournalDetail();
            objIjJournalDetail.setJdetAccChart(paymentAccChart);
            objIjJournalDetail.setJdetTransCurrency(objPaymentTypeDoc.getPayCurrency());                        
            objIjJournalDetail.setJdetTransRate(standartRate);                                                
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit((objPaymentTypeDoc.getPayNominal()*standartRate));                                        
            vResult.add(objIjJournalDetail); 
        }
        
        return vResult;
    }
    
    
    /**
     *     
     * @param <CODE>objIjPaymentTypeDoc</CODE>IjPaymentTypeDoc object as source of journal process     
     * @param <CODE>dTotalTransValue</CODE>total transaction
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *     
     * @return  
     * @created by Edhy 
     */     
    private Vector genObjIjJournalDetailBaseOnPaymentType(IjPaymentTypeDoc objPaymentTypeDoc, double dTotalTransValue, IjEngineParam objIjEngineParam)    
    {
        Vector vResult = new Vector(1,1);
        
        // -- start membuat detail utk posisi debet (bank sesuai dengan trans currency)                
        long paymentTypeClearingOid = objPaymentTypeDoc.getPayTypeClearing();                        

        String whereClauseBank = PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + paymentTypeClearingOid;
        if(objPaymentTypeDoc.getPayCurrency() != 0)
        {
            whereClauseBank = whereClauseBank + " AND " + PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_CURRENCY] + " = " + objPaymentTypeDoc.getPayCurrency();
        }                           
        long clearingAccChart = PstIjPaymentMapping.getAccountChart(whereClauseBank);

        if(clearingAccChart != 0)
        {
            // membuat detail utk posisi debet
            IjJournalDetail objIjJournalDetail = new IjJournalDetail();
            objIjJournalDetail.setJdetAccChart(clearingAccChart);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());                        
            objIjJournalDetail.setJdetTransRate(1);                                                
            objIjJournalDetail.setJdetDebet(dTotalTransValue);
            objIjJournalDetail.setJdetCredit(0);                        
            vResult.add(objIjJournalDetail);                           
        }
        
        return vResult;  
    }
 
    
    /**
     * get total payment based on journaldetail object
     * 
     * @param <CODE>vIjJournalDetail</CODE>vector of journal detail
     *
     * @return totalPayment
     * @created by Edhy
     */    
    private double getTotalPaymentAmount(Vector vIjJournalDetail)
    {
        return PstIjJournalDetail.getTotalOnCreditSide(vIjJournalDetail);
    }    
    
}
