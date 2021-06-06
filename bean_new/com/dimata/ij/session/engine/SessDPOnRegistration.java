/*
 * SessDPOnRegistration.java
 *
 * Created on February 1, 2006, 9:49 PM
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
public class SessDPOnRegistration {
    
    // define journal note for transaction DP On Registration
    public static String strJournalNote[] =
    {
        "Transaksi DP pada saat registrasi, dokumen referensi : ",
        "DP On Registration transaction, reference document : "
    };


    /**
     * Generate list of Dp On Registration journal as long as location count
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */
    public int generateDPOnRegistrationJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDPOnRegistrationJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /**
     * algoritma :
     *  - Ambil data DP dari back office (refer to IJ-Interface getDPOnRegistration)
     *  - Ambil account chart yang terlibat
     *      1. Sisi debet, ambil dari payment mapping (debet), karena kita menerima uang.
     *         Bisa diberlakukan sebagai KAS/BANK atau piutang Giro, Cheque, CC atau LC
     *      2. Sisi kredit, lakukan cek konfigurasi :
     *          -> Income Receive In Advance (Pendapatan Diterima Dimuka)
     *             ambil dari account mapping (DPOnRegistration), buatkan buku pembantu hutang
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
     *  1. get list of DP on Registration Document from selected BO system
     *  2. iterate as long as document count to generate DPOnRegistrationJournal
     */
    public int generateDPOnRegistrationJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getDPonRegistration)
        Vector vectOfDPOnRegistrationDoc = new Vector(1, 1);
        try {
            // --- start get list of DP on Registration Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfDPOnRegistrationDoc = i_bosys.getListDPonRegistration(lLocationOid, dSelectedDate);
            // --- end get list of DP on Registration Document from selected BO system ---


            // --- start iterate as long as document count to generate DPOnRegistrationJournal ---
            if (vectOfDPOnRegistrationDoc != null && vectOfDPOnRegistrationDoc.size() > 0) {
                int maxDPOnRegistrationDoc = vectOfDPOnRegistrationDoc.size();
                for (int i = 0; i < maxDPOnRegistrationDoc; i++) {
                    IjDPOnRegistrationDoc objIjDPOnRegistrationDoc = (IjDPOnRegistrationDoc) vectOfDPOnRegistrationDoc.get(i);
                    
                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(),objIjDPOnRegistrationDoc.getLTransCurrency());
                    objIjDPOnRegistrationDoc.setLTransCurrency(objCurrencyMapping.getAisoCurrency());
                    
                    long lResult = genJournalOfObjDPOnRegistration(lLocationOid, objIjDPOnRegistrationDoc, iDocTypeReference, objIjEngineParam);
                    if(lResult != 0)
                    {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling DP on Registration process skip ... ");
            }
            // --- end iterate as long as document count to generate DPOnRegistrationJournal ---
        } catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     *
     * @param <CODE>lLocationOid</CODE>ID of location object where transaction occur
     * @param <CODE>objIjDPOnRegistrationDoc</CODE>object IjDPOnRegistrationDoc
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    public long genJournalOfObjDPOnRegistration(long lLocationOid, IjDPOnRegistrationDoc objIjDPOnRegistrationDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) 
    {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DP pada saat Registration (object IjDPOnRegistrationDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjDPOnRegistrationDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2.1 pembuatan jurnal detail posisi debet (object IjJournalDetail) dengan mengambil data payment.
        IjJournalDetail objIjJDCredit = genObjIjJDOfPayment(objIjDPOnRegistrationDoc, objIjEngineParam);

        // 2.2 pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
        double totalTransValue = objIjJDCredit.getJdetDebet();
        IjJournalDetail objIjJDDebet = new IjJournalDetail();
        if (objIjEngineParam.getIConfPayment() == I_IJGeneral.CFG_PAY_DP_PI_A_P) 
        {
            // 2.2.1. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping ?PonRegistration?
            objIjJDDebet = genObjIjJDOfIncomeInAdvanced(objIjDPOnRegistrationDoc, totalTransValue, objIjEngineParam);
        }

        if (objIjEngineParam.getIConfPayment() == I_IJGeneral.CFG_PAY_DP_PI_SALES) 
        {
            // 2.2.2. pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping ?PonRegistration?
            objIjJDDebet = genObjIjJDOfSalesIncome(objIjDPOnRegistrationDoc, totalTransValue, objIjEngineParam);
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
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_DP_ON_SALES_ORDER, objIjJournalMain.getRefBoDocLastUpdate(), "DP on Registration : " + objIjJournalMain.getRefBoDocNumber());                

                // posting ke AIRegistration dan update BO Status
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
     * generate journal main object based on IjDPOnRegistrationDoc object
     *
     * @param <CODE>objDPOnRegistrationDoc</CODE>IjDPOnRegistrationDoc object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     * @created by Edhy
     */
    private IjJournalMain genObjIjJournalMain(IjDPOnRegistrationDoc objDPOnRegistrationDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objDPOnRegistrationDoc.getDtTransDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objDPOnRegistrationDoc.getLTransCurrency());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + objDPOnRegistrationDoc.getSNumber());
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objDPOnRegistrationDoc.getLDocOid());
        objIjJournalMain.setRefBoDocNumber(objDPOnRegistrationDoc.getSNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objDPOnRegistrationDoc.getDtLastUpdate());


        return objIjJournalMain;
    }


    /**
     *
     * @param <CODE>objDPOnRegistrationDoc</CODE>IjDPOnRegistrationDoc object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfPayment(IjDPOnRegistrationDoc objDPOnRegistrationDoc, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // pencarian account chart di sisi debet
        PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
        String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + objDPOnRegistrationDoc.getLPaymentType();
        long lTransCurrency = objDPOnRegistrationDoc.getLTransCurrency();
        if (lTransCurrency != 0) {
            whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + lTransCurrency;
        }

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);
        String strStandartRate = (hashStandartRate.get("" + lTransCurrency)) != null ? "" + hashStandartRate.get("" + lTransCurrency) : "1";
        double standartRate = Double.parseDouble(strStandartRate);
        double dDPNominal = objDPOnRegistrationDoc.getDNominal() * standartRate;

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
     * @param <CODE>objDPOnRegistrationDoc</CODE>IjDPOnRegistrationDoc object as source of journal process
     * @param <CODE>dTotalTransValue</CODE>total transaction
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfIncomeInAdvanced(IjDPOnRegistrationDoc objDPOnRegistrationDoc, double dTotalTransValue, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        // Ambil Account Mapping ?PonSalesOrder?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_DP_ON_SALES_ORDER, objDPOnRegistrationDoc.getLTransCurrency());

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        long accChartDPOnRegistration = objIjAccountMapping.getAccount();
        String strStandartRate = (hashStandartRate.get("" + objIjEngineParam.getLBookType())) != null ? "" + hashStandartRate.get("" + objIjEngineParam.getLBookType()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);

        if (accChartDPOnRegistration != 0 && dTotalTransValue > 0) {
            objIjJournalDetail.setJdetAccChart(accChartDPOnRegistration);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetail.setJdetTransRate(1);
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalTransValue);
        }

        return objIjJournalDetail;
    }


    /**
     *
     * @param <CODE>objDPOnRegistrationDoc</CODE>IjDPOnRegistrationDoc object as source of journal process
     * @param <CODE>dTotalTransValue</CODE>total transaction
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter object
     *
     * @return
     */
    private IjJournalDetail genObjIjJDOfSalesIncome(IjDPOnRegistrationDoc objDPOnRegistrationDoc, double dTotalTransValue, IjEngineParam objIjEngineParam) {
        IjJournalDetail objIjJournalDetail = new IjJournalDetail();

        int saleType = 0;
        long transProdDept = 0;
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_DP_ON_SALES_ORDER, saleType, objDPOnRegistrationDoc.getLTransCurrency(), objDPOnRegistrationDoc.getLLocation(), transProdDept);
        long accChartDPOnRegistration = objIjLocationMapping.getAccount();

        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        String strStandartRate = (hashStandartRate.get("" + objIjEngineParam.getLBookType())) != null ? "" + hashStandartRate.get("" + objIjEngineParam.getLBookType()) : "1";
        double standartRate = Double.parseDouble(strStandartRate);

        if (accChartDPOnRegistration != 0 && dTotalTransValue > 0) {
            objIjJournalDetail.setJdetAccChart(accChartDPOnRegistration);
            objIjJournalDetail.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetail.setJdetTransRate(1);
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit(dTotalTransValue);
        }

        return objIjJournalDetail;
    }    
}
