/*
 * SessCancellation.java
 *
 * Created on January 10, 2006, 7:13 AM
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
public class SessCancellation {
    
    // define journal note for transaction Reservation Cancellation
    public static String strJournalNote[] =
    {
        "Transaksi pembatalan, dokumen referensi : ",
        "Cancellation transaction, reference document : "
    };
    
    
    /**
     * Generate list of Cancellation journal as long as location count
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */
    public int generateCancellationJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) 
    {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) 
        {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) 
            {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateCancellationJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);                
            }
        }

        return result;
    }


    /**
     * algoritma :
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     *
     * @algoritma
     *  1. get list of Cancellation Document from selected BO system
     *  2. iterate as long as document count to generate CancellationJournal
     */
    public int generateCancellationJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) 
    {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getCancellation)
        Vector vectOfCancellationDoc = new Vector(1, 1);
        try 
        {
            // --- start get list of Cancellation Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfCancellationDoc = i_bosys.getListCancellation(lLocationOid, dSelectedDate);
            // --- end get list of Cancellation Document from selected BO system ---


            // --- start iterate as long as document count to generate CancellationJournal ---
            if (vectOfCancellationDoc != null && vectOfCancellationDoc.size() > 0) 
            {
                int maxCancellationDoc = vectOfCancellationDoc.size();
                for (int i = 0; i < maxCancellationDoc; i++) 
                {
                    IjCancellationDoc objIjCancellationDoc = (IjCancellationDoc) vectOfCancellationDoc.get(i);
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjCancellationDoc.getLDocCurrency());
                    objIjCancellationDoc.setLDocCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjCancellation(lLocationOid, objIjCancellationDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {                        
                        result++;
                        lResult = genJournalOfObjRevOfCancellationCharge(lLocationOid, objIjCancellationDoc, iDocTypeReference, objIjEngineParam);
                        if(lResult != 0)
                        {
                            result++;
                        }    
                    }
                }
            }
            else 
            {
                System.out.println(".::MSG : Because no document found, journaling Cancellation process skip ... ");
            }
            // --- end iterate as long as document count to generate CancellationJournal ---
        }
        catch (Exception e) 
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    } 
    
    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjCancellationDoc</CODE>object IjCancellationDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    public long genJournalOfObjCancellation(long lLocationOid, IjCancellationDoc objIjCancellationDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) 
    {
        long lResult = 0;
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Cancellation (object IjCancellationDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjCancellationDoc, iDocTypeReference, lLocationOid, objIjEngineParam);
        
        // 2. mengambil vector of jurnal detail 
        Vector vectOfObjIjJurDetail = new Vector(1,1);        
        try            
        {  
            I_Aiso i_aiso = (I_Aiso) Class.forName(I_Aiso.implClassName).newInstance();    
            if (i_aiso.isTransactionOnSamePeriod(objIjCancellationDoc.getDtSalesDate(), objIjCancellationDoc.getDtCancellationDate())) 
            {
                vectOfObjIjJurDetail = genJournalOfObjRevAcqCancelSamePeriod(lLocationOid, objIjCancellationDoc, objIjEngineParam);                
            }
            else
            {        
                vectOfObjIjJurDetail = genJournalOfObjRevAcqCancelDiffPeriod(lLocationOid, objIjCancellationDoc, objIjEngineParam);
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception when instantiate I_Aiso ...");
        }
        
        // 3. generate journal, update logger n posting to AISO if setting is automatically        
        boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
        if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) 
        {
            // generate journal    
            lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

            // update doc logger
            PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_CANCELLATION, objIjJournalMain.getRefBoDocLastUpdate(), "Cancellation : " + objIjJournalMain.getRefBoDocNumber());                
            
            // posting ke AIRegistration dan update BO Status
            if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO) 
            {
                SessPosting objSessPosting = new SessPosting();
                objIjJournalMain.setOID(lResult);
                objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
            }
        }        
        
        return lResult;        
    }    

    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjCancellationDoc</CODE>object IjCancellationDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    public long genJournalOfObjRevOfCancellationCharge(long lLocationOid, IjCancellationDoc objIjCancellationDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) 
    {
        long lResult = 0;
        
        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Cancellation (object IjCancellationDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjCancellationDoc, iDocTypeReference, lLocationOid, objIjEngineParam);
        
        // 2. mengambil vector of jurnal detail 
        Vector vectOfObjIjJurDetail = new Vector(1,1);        
        try            
        {  
            vectOfObjIjJurDetail = genJournalOfObjRevOfCancelCharge(lLocationOid, objIjCancellationDoc, objIjEngineParam);
        }
        catch (Exception e)
        {
            System.out.println("Exception when instantiate I_Aiso ...");
        }
        
        // 3. save jurnal ke database IJ        
        boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
        if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) 
        {
            lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

            // posting ke AIRegistration dan update BO Status
            if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO) 
            {
                SessPosting objSessPosting = new SessPosting();
                objIjJournalMain.setOID(lResult);
                objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
            }
        }        
        
        return lResult;        
    }
    
    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjCancellationDoc</CODE>object IjCancellationDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private Vector genJournalOfObjRevAcqCancelSamePeriod(long lLocationOid, IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        Vector vResult = new Vector(1,1);
        
        // 1.1 Ambil Transaction Location Mapping  ?ales?
        IjJournalDetail objIjJDDebetRevenue = genObjIjJDOfLocationRevenue(objIjCancellationDoc, objIjEngineParam);

        // 1.2 Ambil Transaction Location Mapping  ?harge?
        IjJournalDetail objIjJDDebetCharge = genObjIjJDOfLocationChargeDebet(objIjCancellationDoc, objIjEngineParam);

        // 1.3 Ambil Account Mapping ?ax on Selling?
        IjJournalDetail objIjJDCreditTax = genObjIjJDOfTaxOnSales(objIjCancellationDoc, objIjEngineParam);

        // 1.4 Ambil Account Mapping ?ales (Invoicing)?
        IjJournalDetail objIjJDCreditSales = new IjJournalDetail();
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();        
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjCancellationDoc.getLDocCurrency());
        long accChartSales = objIjAccountMapping.getAccount();        
        double dTotalSales = objIjJDDebetRevenue.getJdetDebet() + objIjJDCreditTax.getJdetDebet() + objIjJDDebetCharge.getJdetDebet();
        if (accChartSales != 0 && dTotalSales > 0) 
        {
            // membuat detail utk posisi debet
            objIjJDCreditSales.setJdetAccChart(accChartSales);
            objIjJDCreditSales.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJDCreditSales.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJDCreditSales.setJdetDebet(0);
            objIjJDCreditSales.setJdetCredit(dTotalSales);
        }      

        // 2. save ke vector
        if (objIjJDDebetRevenue.getJdetAccChart() != 0 
            && objIjJDDebetCharge.getJdetAccChart() != 0 
            && objIjJDCreditTax.getJdetAccChart() != 0 
            && objIjJDCreditSales.getJdetAccChart() != 0) 
        {
            vResult.add(objIjJDDebetRevenue);
            vResult.add(objIjJDDebetCharge);
            vResult.add(objIjJDCreditTax);
            vResult.add(objIjJDCreditSales);
        }

        return vResult;
    }      
    
    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjCancellationDoc</CODE>object IjCancellationDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private Vector genJournalOfObjRevAcqCancelDiffPeriod(long lLocationOid, IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        Vector vResult = new Vector(1,1);

        // 1.1 Ambil Transaction Location Mapping  ?arn Correction?
        IjJournalDetail objIjJDDebetEarnCorrection = genObjIjJDOfEarnCorrection(objIjCancellationDoc, objIjEngineParam);

        // 1.2 Ambil Account Mapping ?ax on Selling?
        IjJournalDetail objIjJDCreditTax = genObjIjJDOfTaxOnSales(objIjCancellationDoc, objIjEngineParam);

        // 1.3 Ambil Account Mapping ?ales (Invoicing)?
        IjJournalDetail objIjJDCreditSales = new IjJournalDetail();
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();        
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjCancellationDoc.getLDocCurrency());
        long accChartSales = objIjAccountMapping.getAccount();        
        double dTotalSales = objIjJDDebetEarnCorrection.getJdetDebet() + objIjJDCreditTax.getJdetDebet();
        if (accChartSales != 0 && dTotalSales > 0) 
        {
            // membuat detail utk posisi debet
            objIjJDCreditSales.setJdetAccChart(accChartSales);
            objIjJDCreditSales.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJDCreditSales.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJDCreditSales.setJdetDebet(0);
            objIjJDCreditSales.setJdetCredit(dTotalSales);
        }      

        // 2. save to vector
        if (objIjJDDebetEarnCorrection.getJdetAccChart() != 0             
            && objIjJDCreditTax.getJdetAccChart() != 0 
            && objIjJDCreditSales.getJdetAccChart() != 0) 
        {
            vResult.add(objIjJDDebetEarnCorrection);            
            vResult.add(objIjJDCreditTax);
            vResult.add(objIjJDCreditSales);
        }

        return vResult;
    }
    
    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjCancellationDoc</CODE>object IjCancellationDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private Vector genJournalOfObjRevOfCancelCharge(long lLocationOid, IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        Vector vResult = new Vector(1,1);
        
        // check apakah ada "DP on Registration" ga ???
        Vector vListOfDpOnReg = objIjCancellationDoc.getListDPDeduction();
        
        // jika ada Dp, maka pake Dp deduction
        if (vListOfDpOnReg != null && vListOfDpOnReg.size() > 0) 
        {        
            // 1.1 Ambil Account Mapping ?P on Sales Order?
            Vector vectOfObjIjJDDebetDPDeduct = genObjIjJDOfDPDeduction(objIjCancellationDoc, objIjEngineParam);

            // 1.2 Ambil Transaction Location Mapping  ?harge?
            IjJournalDetail objIjJDCreditCharge = genObjIjJDOfLocationChargeCredit(objIjCancellationDoc, objIjEngineParam);

            // 2. save ke vector
            if (vectOfObjIjJDDebetDPDeduct != null 
                && vectOfObjIjJDDebetDPDeduct.size() > 0                 
                && objIjJDCreditCharge.getJdetAccChart() != 0) 
            {
                vResult.addAll(vectOfObjIjJDDebetDPDeduct);
                vResult.add(objIjJDCreditCharge);
            }
            
        }
        
        
        // jika tidak ada Dp, maka pake list of payment
        else
        {
            // ambil vector of payment
            Vector vectOfPayment = objIjCancellationDoc.getListPayment();
            if (vectOfPayment != null && vectOfPayment.size() > 0)
            {                
                // 1.1 Payment Mapping
                Vector vectOfObjIjJDPayment = genObjIjJDOfPayment(lLocationOid, objIjCancellationDoc, objIjEngineParam);

                // 1.2 Ambil Transaction Location Mapping ?harge?
                IjJournalDetail objIjJDCreditCharge = genObjIjJDOfLocationChargeCredit(objIjCancellationDoc, objIjEngineParam);

                // 2. save ke vector
                if (vectOfObjIjJDPayment != null 
                    && vectOfObjIjJDPayment.size() > 0                     
                    && objIjJDCreditCharge.getJdetAccChart() != 0) 
                {
                    vResult.addAll(vectOfObjIjJDPayment);
                    vResult.add(objIjJDCreditCharge);
                }            
            }
            else
            {
                System.out.println("Cannot generate journal, list Of Payment is null ...");            
            }
        }        

        return vResult;
    }      
    
    
    /**
     * generate journal main object based on IjCancellationDoc object
     *
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */
    private IjJournalMain genObjIjJournalMain(IjCancellationDoc objIjCancellationDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjCancellationDoc.getDtDocTransDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjCancellationDoc.getLDocCurrency());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + objIjCancellationDoc.getSDocNumber());
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjCancellationDoc.getLDocId());
        objIjJournalMain.setRefBoDocNumber(objIjCancellationDoc.getSDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjCancellationDoc.getDtLastUpdate());

        return objIjJournalMain;
    }


    /**
     *
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationRevenue(IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();    
        
        // Ambil Transaction Location Mapping ?ales?utk di sisi debet
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_SALES, -1, objIjCancellationDoc.getLDocCurrency(), objIjCancellationDoc.getLVillaId(), 0);
        long accChartLocationRevenue = objIjLocationMapping.getAccount();                
        double dTotalRevenue = objIjCancellationDoc.getDTotalCancellation() * objIjCancellationDoc.getDDocRate();
        if (accChartLocationRevenue != 0 && dTotalRevenue > 0) 
        {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationRevenue);
            objIjJournalDetail.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dTotalRevenue);
            objIjJournalDetail.setJdetCredit(0);
        }      
        
        return objIjJournalDetail;
    }


    /**
     *
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationChargeDebet(IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();    
        
        // Ambil Transaction Location Mapping ?harge?utk di sisi debet        
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_CHARGE, -1, objIjCancellationDoc.getLDocCurrency(), objIjCancellationDoc.getLVillaId(), 0);
        long accChartLocationCharge = objIjLocationMapping.getAccount();
        double dTotalCharge = objIjCancellationDoc.getDCancellationCharge() * objIjCancellationDoc.getDDocRate();
        if (accChartLocationCharge != 0 && dTotalCharge > 0) 
        {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationCharge);
            objIjJournalDetail.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dTotalCharge);
            objIjJournalDetail.setJdetCredit(0);
        }      
        
        return objIjJournalDetail;
    }

    /**
     *
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationChargeCredit(IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();    
        
        // Ambil Transaction Location Mapping ?harge?utk di sisi debet        
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_CHARGE, -1, objIjCancellationDoc.getLDocCurrency(), objIjCancellationDoc.getLVillaId(), 0);
        long accChartLocationCharge = objIjLocationMapping.getAccount();
        double dTotalCharge = objIjCancellationDoc.getDCancellationCharge() * objIjCancellationDoc.getDDocRate();
        if (accChartLocationCharge != 0 && dTotalCharge > 0) 
        {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationCharge);
            objIjJournalDetail.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalCharge);
        }      
        
        return objIjJournalDetail;
    }
    
    /**
     *
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfTaxOnSales(IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();            
        
        // Ambil Account Mapping ?ax On Selling?utk di sisi kredit
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_TAX_ON_SELLING, objIjCancellationDoc.getLDocCurrency());
        long accChartSalesTax = objIjAccountMapping.getAccount();
        double dTotalTax = objIjCancellationDoc.getDSalesTax() * objIjCancellationDoc.getDDocRate();
        if (accChartSalesTax != 0 && dTotalTax > 0) 
        {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartSalesTax);
            objIjJournalDetail.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalTax);
        }      
        
        return objIjJournalDetail;
    }

    /**
     *
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfEarnCorrection(IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();            
        
        // Ambil Account Mapping ?arn Correction?utk di sisi debet       
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();        
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_EARN_CORRECTION_LAST_PER, objIjCancellationDoc.getLDocCurrency());
        long accChartEarnCorrection = objIjAccountMapping.getAccount();        
        double dEarnCorrection = objIjCancellationDoc.getDCancellationCharge() * objIjCancellationDoc.getDDocRate();
        if (accChartEarnCorrection != 0 && dEarnCorrection > 0) 
        {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartEarnCorrection);
            objIjJournalDetail.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjCancellationDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dEarnCorrection);
            objIjJournalDetail.setJdetCredit(0);
        }      
        
        return objIjJournalDetail;
    }
    
    /**
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     * @return
     */
    private Vector genObjIjJDOfDPDeduction(IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        Vector vResult = new Vector(1, 1);

        // --- start membuat detail ---
        Vector listOfDpDeduction = objIjCancellationDoc.getListDPDeduction();
        if (listOfDpDeduction != null && listOfDpDeduction.size() > 0) 
        {
            Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
            PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
            int DPDeductionCount = listOfDpDeduction.size();
            for (int iDp = 0; iDp < DPDeductionCount; iDp++) 
            {
                IjDPDeductionDoc objIjDPDeductionDoc = (IjDPDeductionDoc) listOfDpDeduction.get(iDp);

                // Ambil Account Mapping ?P On Sales Order?
                IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_DP_ON_SALES_ORDER, objIjCancellationDoc.getLDocCurrency());
                long dpDeductionAccChart = objIjAccountMapping.getAccount();
                String strStandartRateDp = (hashStandartRate.get("" + objIjDPDeductionDoc.getPayCurrency())) != null ? "" + hashStandartRate.get("" + objIjDPDeductionDoc.getPayCurrency()) : "1";
                double standartRateDp = Double.parseDouble(strStandartRateDp);                
                double totalDpDeduction = (objIjDPDeductionDoc.getPayNominal() * standartRateDp);

                if (dpDeductionAccChart != 0) {
                    // pembuatan jurnal detail
                    IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                    objIjJournalDetail.setJdetAccChart(dpDeductionAccChart);
                    objIjJournalDetail.setJdetTransCurrency(objIjCancellationDoc.getLDocCurrency());
                    objIjJournalDetail.setJdetTransRate(standartRateDp);
                    objIjJournalDetail.setJdetDebet(totalDpDeduction);
                    objIjJournalDetail.setJdetCredit(0);
                    vResult.add(objIjJournalDetail);
                }
            }
        }

        return vResult;
    }    
 
    /**
     * @param <CODE>objIjCancellationDoc</CODE>IjCancellationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     * @return
     */
    private Vector genObjIjJDOfPayment(long lLocationOid, IjCancellationDoc objIjCancellationDoc, IjEngineParam objIjEngineParam) 
    {
        Vector vResult = new Vector(1, 1);

        // --- start membuat detail ---
        Vector listOfPayment = objIjCancellationDoc.getListPayment();
        if (listOfPayment != null && listOfPayment.size() > 0) 
        {
            Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
            PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
            int PaymentCount = listOfPayment.size();
            for (int i = 0; i < PaymentCount; i++) 
            {
                IjPaymentDoc objPaymentDoc = (IjPaymentDoc) listOfPayment.get(i);   
                
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

                if(paymentAccChart != 0)
                {
                    // membuat detail utk posisi debet                        
                    IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                    objIjJournalDetail.setJdetAccChart(paymentAccChart);
                    objIjJournalDetail.setJdetTransCurrency(objPaymentDoc.getPayCurrency());                        
                    objIjJournalDetail.setJdetTransRate(standartRate);                        
                    objIjJournalDetail.setJdetDebet((objPaymentDoc.getPayNominal()*standartRate));
                    objIjJournalDetail.setJdetCredit(0);                 
                    vResult.add(objIjJournalDetail); 
                }
            }
        }

        return vResult;
    }    
}
