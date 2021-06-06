/*
 * SessAdjustment.java
 *
 * Created on January 10, 2006, 7:14 AM
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
 * @author Administrator
 */
public class SessAdjustment {

    // define journal note for transaction Adjustment
    public static String strJournalNote[] =
            {
                    "Transaksi adjustment, dokumen referensi : ",
                    "Adjustment transaction, reference document : "
            };

    /**
     * Generate list of Adjustment journal as long as location count
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     */
    public int generateAdjustmentJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateAdjustmentJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /**
     * algoritma :
     *
     * @param <CODE>lLocationOid</CODE>ID    of location object where transaction occur
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     * @algoritma 1. get list of Adjustment Document from selected BO system
     * 2. iterate as long as document count to generate AdjustmentJournal
     */
    public int generateAdjustmentJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getAdjustment)
        Vector vectOfAdjustmentDoc = new Vector(1, 1);
        try {
            // --- start get list of Adjustment Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfAdjustmentDoc = i_bosys.getListAdjustment(lLocationOid, dSelectedDate);
            // --- end get list of Adjustment Document from selected BO system ---

            // --- start iterate as long as document count to generate AdjustmentJournal ---
            if (vectOfAdjustmentDoc != null && vectOfAdjustmentDoc.size() > 0) {
                int maxAdjustmentDoc = vectOfAdjustmentDoc.size();
                for (int i = 0; i < maxAdjustmentDoc; i++) {
                    IjAdjustmentDoc objIjAdjustmentDoc = (IjAdjustmentDoc) vectOfAdjustmentDoc.get(i);

                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(), objIjAdjustmentDoc.getLDocCurrency());
                    objIjAdjustmentDoc.setLDocCurrency(objCurrencyMapping.getAisoCurrency());

                    long lResult = genJournalOfObjAdjustment(lLocationOid, objIjAdjustmentDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling Adjustment process skip ... ");
            }
            // --- end iterate as long as document count to generate AdjustmentJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjAdjustmentDoc</CODE>object
     *                                    IjAdjustmentDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                    type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                    object
     * @return
     */
    public long genJournalOfObjAdjustment(long lLocationOid, IjAdjustmentDoc objIjAdjustmentDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Cancellation (object IjCancellationDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjAdjustmentDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. mengambil vector of jurnal detail 
        Vector vectOfObjIjJurDetail = new Vector(1, 1);
        try {
            I_Aiso i_aiso = (I_Aiso) Class.forName(I_Aiso.implClassName).newInstance();
            if (objIjAdjustmentDoc.getIAdjType() == I_IJGeneral.ADJUSTMENT_PLUS) {
                if (i_aiso.isTransactionOnSamePeriod(objIjAdjustmentDoc.getDtSalesDate(), objIjAdjustmentDoc.getDtAdjustDate()))
                {
                    vectOfObjIjJurDetail = genJournalOfObjAdjPlusSamePeriod(lLocationOid, objIjAdjustmentDoc, objIjEngineParam);
                } else {
                    vectOfObjIjJurDetail = genJournalOfObjAdjPlusDiffPeriod(lLocationOid, objIjAdjustmentDoc, objIjEngineParam);
                }
            }

            if (objIjAdjustmentDoc.getIAdjType() == I_IJGeneral.ADJUSTMENT_MINUS) {
                if (i_aiso.isTransactionOnSamePeriod(objIjAdjustmentDoc.getDtSalesDate(), objIjAdjustmentDoc.getDtAdjustDate()))
                {
                    vectOfObjIjJurDetail = genJournalOfObjAdjMinusSamePeriod(lLocationOid, objIjAdjustmentDoc, objIjEngineParam);
                } else {
                    vectOfObjIjJurDetail = genJournalOfObjAdjMinusDiffPeriod(lLocationOid, objIjAdjustmentDoc, objIjEngineParam);
                }
            }
        }
        catch (Exception e) {
            System.out.println("exc when instantiate I_Aiso ... ");
        }

        // 3. generate journal, update logger n posting to AISO if setting is automatically
        boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
        if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
            // generate journal    
            lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

            // update doc logger
            PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_ADJUSTMENT, objIjJournalMain.getRefBoDocLastUpdate(), "Adjustment : " + objIjJournalMain.getRefBoDocNumber());

            // posting ke AIRegistration dan update BO Status
            if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO) {
                SessPosting objSessPosting = new SessPosting();
                objIjJournalMain.setOID(lResult);
                objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
            }
        }

        return lResult;
    }

    /**
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjAdjustmentDoc</CODE>object
     *                                    IjAdjustmentDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                    type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                    object
     * @return
     */
    private Vector genJournalOfObjAdjPlusSamePeriod(long lLocationOid, IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // 1.1 Ambil Transaction Location Mapping  ?ales?
        IjJournalDetail objIjJDCreditRev = genObjIjJDOfLocationRevenueCredit(objIjAdjustmentDoc, objIjEngineParam);

        // 1.2 Ambil Transaction Location Mapping  ?harge?
        IjJournalDetail objIjJDCreditCharge = genObjIjJDOfLocationChargeCredit(objIjAdjustmentDoc, objIjEngineParam);

        // 1.3 Ambil Account Mapping ?ax on Selling?
        IjJournalDetail objIjJDCreditTax = genObjIjJDOfTaxOnSalesCredit(objIjAdjustmentDoc, objIjEngineParam);

        // 1.4 Ambil Account Mapping ?ales (Invoicing)?
        IjJournalDetail objIjJDDebetSales = new IjJournalDetail();
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjAdjustmentDoc.getLDocCurrency());
        long accChartSales = objIjAccountMapping.getAccount();
        double dTotalSales = objIjJDCreditRev.getJdetCredit() + objIjJDCreditCharge.getJdetCredit() + objIjJDCreditTax.getJdetCredit();
        if (accChartSales != 0 && dTotalSales > 0) {
            // membuat detail utk posisi debet
            objIjJDDebetSales.setJdetAccChart(accChartSales);
            objIjJDDebetSales.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJDDebetSales.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJDDebetSales.setJdetDebet(0);
            objIjJDDebetSales.setJdetCredit(dTotalSales);
        }

        // 2. save to vector
        if (objIjJDCreditRev.getJdetAccChart() != 0
                && objIjJDCreditCharge.getJdetAccChart() != 0
                && objIjJDCreditTax.getJdetAccChart() != 0
                && objIjJDDebetSales.getJdetAccChart() != 0) {
            vResult.add(objIjJDCreditRev);
            vResult.add(objIjJDCreditCharge);
            vResult.add(objIjJDCreditTax);
            vResult.add(objIjJDDebetSales);
        }

        return vResult;
    }

    /**
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjAdjustmentDoc</CODE>object
     *                                    IjAdjustmentDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                    type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                    object
     * @return
     */
    private Vector genJournalOfObjAdjMinusSamePeriod(long lLocationOid, IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // 1.1 Ambil Transaction Location Mapping  ?ales?
        IjJournalDetail objIjJDDebetRev = genObjIjJDOfLocationRevenueDebet(objIjAdjustmentDoc, objIjEngineParam);

        // 1.2 Ambil Transaction Location Mapping  ?harge?
        IjJournalDetail objIjJDDebetCharge = genObjIjJDOfLocationChargeDebet(objIjAdjustmentDoc, objIjEngineParam);

        // 1.3 Ambil Account Mapping ?ax on Selling?
        IjJournalDetail objIjJDDebetTax = genObjIjJDOfTaxOnSalesDebet(objIjAdjustmentDoc, objIjEngineParam);

        // 1.4 Ambil Account Mapping ?ales (Invoicing)?
        IjJournalDetail objIjJDCreditSales = new IjJournalDetail();
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjAdjustmentDoc.getLDocCurrency());
        long accChartSales = objIjAccountMapping.getAccount();
        double dTotalSales = objIjJDDebetRev.getJdetDebet() + objIjJDDebetCharge.getJdetDebet() + objIjJDDebetTax.getJdetDebet();
        if (accChartSales != 0 && dTotalSales > 0) {
            // membuat detail utk posisi debet
            objIjJDCreditSales.setJdetAccChart(accChartSales);
            objIjJDCreditSales.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJDCreditSales.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJDCreditSales.setJdetDebet(0);
            objIjJDCreditSales.setJdetCredit(dTotalSales);
        }

        // 2. save to vector
        if (objIjJDDebetRev.getJdetAccChart() != 0
                && objIjJDDebetCharge.getJdetAccChart() != 0
                && objIjJDDebetTax.getJdetAccChart() != 0
                && objIjJDCreditSales.getJdetAccChart() != 0) {
            vResult.add(objIjJDDebetRev);
            vResult.add(objIjJDDebetCharge);
            vResult.add(objIjJDDebetTax);
            vResult.add(objIjJDCreditSales);
        }

        return vResult;
    }

    /**
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjAdjustmentDoc</CODE>object
     *                                    IjAdjustmentDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                    type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                    object
     * @return
     */
    private Vector genJournalOfObjAdjPlusDiffPeriod(long lLocationOid, IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // 1.1 Ambil Account Mapping  ?arn Correction?
        IjJournalDetail objIjJDCreditEarn = genObjIjJDOfEarnCorrectionCredit(objIjAdjustmentDoc, objIjEngineParam);

        // 1.2 Ambil Account Mapping ?ax on Selling?
        IjJournalDetail objIjJDCreditTax = genObjIjJDOfTaxOnSalesCredit(objIjAdjustmentDoc, objIjEngineParam);

        // 1.3 Ambil Account Mapping ?ales (Invoicing)?
        IjJournalDetail objIjJDDebetSales = new IjJournalDetail();
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjAdjustmentDoc.getLDocCurrency());
        long accChartSales = objIjAccountMapping.getAccount();
        double dTotalSales = objIjJDCreditEarn.getJdetCredit() + objIjJDCreditTax.getJdetCredit();
        if (accChartSales != 0 && dTotalSales > 0) {
            // membuat detail utk posisi debet
            objIjJDDebetSales.setJdetAccChart(accChartSales);
            objIjJDDebetSales.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJDDebetSales.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJDDebetSales.setJdetDebet(0);
            objIjJDDebetSales.setJdetCredit(dTotalSales);
        }

        // 2. save to vector
        if (objIjJDCreditEarn.getJdetAccChart() != 0
                && objIjJDCreditTax.getJdetAccChart() != 0
                && objIjJDDebetSales.getJdetAccChart() != 0) {
            vResult.add(objIjJDCreditEarn);
            vResult.add(objIjJDCreditTax);
            vResult.add(objIjJDDebetSales);
        }

        return vResult;
    }

    /**
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjAdjustmentDoc</CODE>object
     *                                    IjAdjustmentDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                    type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                    object
     * @return
     */
    private Vector genJournalOfObjAdjMinusDiffPeriod(long lLocationOid, IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // 1.1 Ambil Account Mapping  ?arn Correction?
        IjJournalDetail objIjJDDebetEarn = genObjIjJDOfEarnCorrectionDebet(objIjAdjustmentDoc, objIjEngineParam);

        // 1.2 Ambil Account Mapping ?ax on Selling?
        IjJournalDetail objIjJDDebetTax = genObjIjJDOfTaxOnSalesDebet(objIjAdjustmentDoc, objIjEngineParam);

        // 1.3 Ambil Account Mapping ?ales (Invoicing)?
        IjJournalDetail objIjJDCreditSales = new IjJournalDetail();
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_SALES, objIjAdjustmentDoc.getLDocCurrency());
        long accChartSales = objIjAccountMapping.getAccount();
        double dTotalSales = objIjJDDebetEarn.getJdetDebet() + objIjJDDebetTax.getJdetDebet();
        if (accChartSales != 0 && dTotalSales > 0) {
            // membuat detail utk posisi debet
            objIjJDCreditSales.setJdetAccChart(accChartSales);
            objIjJDCreditSales.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJDCreditSales.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJDCreditSales.setJdetDebet(0);
            objIjJDCreditSales.setJdetCredit(dTotalSales);
        }

        // 2. save to vector
        if (objIjJDDebetEarn.getJdetAccChart() != 0
                && objIjJDDebetTax.getJdetAccChart() != 0
                && objIjJDCreditSales.getJdetAccChart() != 0) {
            vResult.add(objIjJDDebetEarn);
            vResult.add(objIjJDDebetTax);
            vResult.add(objIjJDCreditSales);
        }

        return vResult;
    }

    /**
     * generate journal main object based on IjAdjustmentDoc object
     *
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private IjJournalMain genObjIjJournalMain(IjAdjustmentDoc objIjAdjustmentDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjAdjustmentDoc.getDtDocTransDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + objIjAdjustmentDoc.getSDocNumber());
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjAdjustmentDoc.getLDocId());
        objIjJournalMain.setRefBoDocNumber(objIjAdjustmentDoc.getSDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjAdjustmentDoc.getDtLastUpdate());

        return objIjJournalMain;
    }


    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationRevenueDebet(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Transaction Location Mapping ?ales?utk di sisi debet
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_SALES, -1, objIjAdjustmentDoc.getLDocCurrency(), objIjAdjustmentDoc.getLVillaId(), 0);
        long accChartLocationRevenue = objIjLocationMapping.getAccount();
        double dTotalRevenue = objIjAdjustmentDoc.getDService() * objIjAdjustmentDoc.getDDocRate();
        if (accChartLocationRevenue != 0 && dTotalRevenue > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationRevenue);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dTotalRevenue);
            objIjJournalDetail.setJdetCredit(0);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationRevenueCredit(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Transaction Location Mapping ?ales?utk di sisi credit
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_SALES, -1, objIjAdjustmentDoc.getLDocCurrency(), objIjAdjustmentDoc.getLVillaId(), 0);
        long accChartLocationRevenue = objIjLocationMapping.getAccount();
        double dTotalRevenue = objIjAdjustmentDoc.getDService() * objIjAdjustmentDoc.getDDocRate();
        if (accChartLocationRevenue != 0 && dTotalRevenue > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationRevenue);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalRevenue);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationChargeDebet(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Transaction Location Mapping ?harge?utk di sisi debet        
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_CHARGE, -1, objIjAdjustmentDoc.getLDocCurrency(), objIjAdjustmentDoc.getLVillaId(), 0);
        long accChartLocationCharge = objIjLocationMapping.getAccount();
        double dTotalCharge = objIjAdjustmentDoc.getDAdjustmentValue() * objIjAdjustmentDoc.getDDocRate();
        if (accChartLocationCharge != 0 && dTotalCharge > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationCharge);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dTotalCharge);
            objIjJournalDetail.setJdetCredit(0);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfLocationChargeCredit(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Transaction Location Mapping ?harge?utk di sisi credit        
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_CHARGE, -1, objIjAdjustmentDoc.getLDocCurrency(), objIjAdjustmentDoc.getLVillaId(), 0);
        long accChartLocationCharge = objIjLocationMapping.getAccount();
        double dTotalCharge = objIjAdjustmentDoc.getDAdjustmentValue() * objIjAdjustmentDoc.getDDocRate();
        if (accChartLocationCharge != 0 && dTotalCharge > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartLocationCharge);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalCharge);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfTaxOnSalesDebet(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Account Mapping ?ax On Selling?utk di sisi debet
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_TAX_ON_SELLING, objIjAdjustmentDoc.getLDocCurrency());
        long accChartSalesTax = objIjAccountMapping.getAccount();
        double dTotalTax = objIjAdjustmentDoc.getDTax() * objIjAdjustmentDoc.getDDocRate();
        if (accChartSalesTax != 0 && dTotalTax > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartSalesTax);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dTotalTax);
            objIjJournalDetail.setJdetCredit(0);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfTaxOnSalesCredit(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Account Mapping ?ax On Selling?utk di sisi kredit
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_TAX_ON_SELLING, objIjAdjustmentDoc.getLDocCurrency());
        long accChartSalesTax = objIjAccountMapping.getAccount();
        double dTotalTax = objIjAdjustmentDoc.getDTax() * objIjAdjustmentDoc.getDDocRate();
        if (accChartSalesTax != 0 && dTotalTax > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartSalesTax);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalTax);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfEarnCorrectionDebet(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Account Mapping ?arn Correction?utk di sisi debet       
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_EARN_CORRECTION_LAST_PER, objIjAdjustmentDoc.getLDocCurrency());
        long accChartEarnCorrection = objIjAccountMapping.getAccount();
        double dEarnCorrection = objIjAdjustmentDoc.getDAdjustmentValue() * objIjAdjustmentDoc.getDDocRate();
        if (accChartEarnCorrection != 0 && dEarnCorrection > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartEarnCorrection);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(dEarnCorrection);
            objIjJournalDetail.setJdetCredit(0);
        }

        return objIjJournalDetail;
    }

    /**
     * @param <CODE>objIjAdjustmentDoc</CODE>IjAdjustmentDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalDetail genObjIjJDOfEarnCorrectionCredit(IjAdjustmentDoc objIjAdjustmentDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Account Mapping ?arn Correction?utk di sisi credit       
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_EARN_CORRECTION_LAST_PER, objIjAdjustmentDoc.getLDocCurrency());
        long accChartEarnCorrection = objIjAccountMapping.getAccount();
        double dEarnCorrection = objIjAdjustmentDoc.getDAdjustmentValue() * objIjAdjustmentDoc.getDDocRate();
        if (accChartEarnCorrection != 0 && dEarnCorrection > 0) {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(accChartEarnCorrection);
            objIjJournalDetail.setJdetTransCurrency(objIjAdjustmentDoc.getLDocCurrency());
            objIjJournalDetail.setJdetTransRate(objIjAdjustmentDoc.getDDocRate());
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dEarnCorrection);
        }

        return objIjJournalDetail;
    }
}
