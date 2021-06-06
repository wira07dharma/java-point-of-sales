/*
 * SessDPonSalesOrder.java
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
public class SessDPOnSO {

    // define journal note for transaction DP On Sales Order
    public static String strJournalNote[] =
            {
                "Transaksi DP pada penjualan, dokumen referensi : ",
                "DP On Sales Order transaction, reference document : "
            };


    /**
     * Generate list of Dp On SO journal as long as location count
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */
    public int generateDPOnSOJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDPOnSOJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /**
     * algoritma :
     *  - Ambil data DP dari back office (refer to IJ-Interface getDPonSalesOrder)
     *  - Ambil account chart yang terlibat
     *      1. Sisi debet, ambil dari payment mapping (debet), karena kita menerima uang.
     *         Bisa diberlakukan sebagai KAS/BANK atau piutang Giro, Cheque, CC atau LC
     *      2. Sisi kredit, lakukan cek konfigurasi :
     *          -> Income Receive In Advance (Pendaptana Diterima Dimuka)
     *             ambil dari account mapping (DPOnSaleOrder), buatkan buku pembantu hutang
     *          -> Direct as Sale Income
     *             ambil dari location mapping
     *      3. Lakukan jurnal
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
     *  1. get list of DP on SO Document from selected BO system
     *  2. iterate as long as document count to generate DPOnPOJournal
     */
    public int generateDPOnSOJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getDPonSalesOrder)
        Vector vectOfDPOnSODoc = new Vector(1, 1);
        try {
            // --- start get list of DP on SO Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfDPOnSODoc = i_bosys.getListDPonSalesOrder(lLocationOid, dSelectedDate);
            // --- end get list of DP on SO Document from selected BO system ---


            // --- start iterate as long as document count to generate DPOnPOJournal ---
            if (vectOfDPOnSODoc != null && vectOfDPOnSODoc.size() > 0) {
                int maxDPOnSODoc = vectOfDPOnSODoc.size();
                for (int i = 0; i < maxDPOnSODoc; i++) {
                    IjDPOnSODoc objIjDPOnSODoc = (IjDPOnSODoc) vectOfDPOnSODoc.get(i);
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjDPOnSODoc.getLTransCurrency());
                    objIjDPOnSODoc.setLTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjDPOnSO(lLocationOid, objIjDPOnSODoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling DP on Sales Order process skip ... ");
            }
            // --- end iterate as long as document count to generate DPOnPOJournal ---
        } catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjDPOnSODoc</CODE>object IjDPOnSODoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    public long genJournalOfObjDPOnSO(long lLocationOid, IjDPOnSODoc objIjDPOnSODoc, int iDocTypeReference, IjEngineParam objIjEngineParam) 
    {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP pada saat SO (object IjDPOnSODoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjDPOnSODoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2.1 pembuatan jurnal detail posisi debet (object IjJournalDetail) dengan mengambil data payment.
        IjJournalDetail objIjJDCredit = genObjIjJDOfPayment(objIjDPOnSODoc, objIjEngineParam);

        // 2.2 pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
        double totalTransValue = objIjJDCredit.getJdetDebet();
        IjJournalDetail objIjJDDebet = new IjJournalDetail();
        if (objIjEngineParam.getIConfPayment() == I_IJGeneral.CFG_PAY_DP_PI_A_P) 
        {
            // 2.2.1. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping ?PonPurchaseOrder?
            objIjJDDebet = genObjIjJDOfIncomeInAdvanced(objIjDPOnSODoc, totalTransValue, objIjEngineParam);
        }

        if (objIjEngineParam.getIConfPayment() == I_IJGeneral.CFG_PAY_DP_PI_SALES) 
        {
            // 2.2.2. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping ?PonPurchaseOrder?
            objIjJDDebet = genObjIjJDOfSalesIncome(objIjDPOnSODoc, totalTransValue, objIjEngineParam);
        }

        // 3. save jurnal ke database IJ
        Vector vectOfObjIjJurDetail = new Vector(1, 1);

        if (objIjJDCredit.getJdetAccChart() != 0 && objIjJDDebet.getJdetAccChart() != 0) 
        {
            vectOfObjIjJurDetail.add(objIjJDDebet);
            vectOfObjIjJurDetail.add(objIjJDCredit);

            // 4. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) 
            {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_DP_ON_SALES_ORDER, objIjJournalMain.getRefBoDocLastUpdate(), "DP on Sales Order : " + objIjJournalMain.getRefBoDocNumber());                

                // posting ke AISO dan update BO Status
                if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO) {
                    SessPosting objSessPosting = new SessPosting();
                    objIjJournalMain.setOID(lResult);
                    objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
                }
            }
        }

        return lResult;
    }


    /**
     * generate journal main object based on IjDPOnSODoc object
     *
     * @param <CODE>objDPOnSODoc</CODE>IjDPOnSODoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */
    private IjJournalMain genObjIjJournalMain(IjDPOnSODoc objDPOnSODoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objDPOnSODoc.getDtTransDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objDPOnSODoc.getLTransCurrency());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + objDPOnSODoc.getSNumber());
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objDPOnSODoc.getLDocOid());
        objIjJournalMain.setRefBoDocNumber(objDPOnSODoc.getSNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objDPOnSODoc.getDtLastUpdate());


        return objIjJournalMain;
    }


    /**
     *
     * @param <CODE>objDPOnSODoc</CODE>IjDPOnSODoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfPayment(IjDPOnSODoc objDPOnSODoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // pencarian account chart di sisi debet
        PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
        String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + objDPOnSODoc.getLPaymentType();
        long lTransCurrency = objDPOnSODoc.getLTransCurrency();
        if (lTransCurrency != 0) {
            whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + lTransCurrency;
        }

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);
        String strStandartRate = (hashStandartRate.get("" + lTransCurrency)) != null ? "" + hashStandartRate.get("" + lTransCurrency) : "1";
        double standartRate = Double.parseDouble(strStandartRate);
        double dDPNominal = objDPOnSODoc.getDNominal() * standartRate;

        if (paymentAccChart != 0 && dDPNominal > 0) 
        {
            // membuat detail utk posisi debet
            objIjJournalDetail.setJdetAccChart(paymentAccChart);
            objIjJournalDetail.setJdetTransCurrency(lTransCurrency);
            objIjJournalDetail.setJdetTransRate(standartRate);
            objIjJournalDetail.setJdetDebet(dDPNominal);
            objIjJournalDetail.setJdetCredit(0);
        }

        return objIjJournalDetail;
    }


    /**
     *
     * @param <CODE>objDPOnSODoc</CODE>IjDPOnSODoc object as source of journal process
     * @param <CODE>dTotalTransValue</CODE>total transaction
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfIncomeInAdvanced(IjDPOnSODoc objDPOnSODoc, double dTotalTransValue, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Account Mapping ?PonSalesOrder?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_DP_ON_SALES_ORDER, objDPOnSODoc.getLTransCurrency());

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        long accChartDPOnSO = objIjAccountMapping.getAccount();
        String strStandartRate = (hashStandartRate.get("" + objIjEngineParam.getLBookType())) != null ? "" + hashStandartRate.get("" + objIjEngineParam.getLBookType()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);

        if (accChartDPOnSO != 0 && dTotalTransValue > 0) {
            objIjJournalDetail.setJdetAccChart(accChartDPOnSO);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetail.setJdetTransRate(1);
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalTransValue);
        }

        return objIjJournalDetail;
    }


    /**
     *
     * @param <CODE>objDPOnSODoc</CODE>IjDPOnSODoc object as source of journal process
     * @param <CODE>dTotalTransValue</CODE>total transaction
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfSalesIncome(IjDPOnSODoc objDPOnSODoc, double dTotalTransValue, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        int saleType = 0;
        long transProdDept = 0;
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_DP_ON_SALES_ORDER, saleType, objDPOnSODoc.getLTransCurrency(), objDPOnSODoc.getLLocation(), transProdDept);
        long accChartDPOnSO = objIjLocationMapping.getAccount();

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        String strStandartRate = (hashStandartRate.get("" + objIjEngineParam.getLBookType())) != null ? "" + hashStandartRate.get("" + objIjEngineParam.getLBookType()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);

        if (accChartDPOnSO != 0 && dTotalTransValue > 0) {
            objIjJournalDetail.setJdetAccChart(accChartDPOnSO);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetail.setJdetTransRate(1);
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalTransValue);
        }

        return objIjJournalDetail;
    }
}
