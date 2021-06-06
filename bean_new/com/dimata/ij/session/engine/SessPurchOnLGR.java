/*
 * SessPurchOnLgr.java
 *
 * Created on January 18, 2005, 7:57 AM
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
 * @author gedhy
 */
public class SessPurchOnLGR {

    // define journal note for transaction Purchase On LGR (Cash or Credit)
    public static final int PURCHASE_ON_LGR_CASH = 0;
    public static final int PURCHASE_ON_LGR_CREDIT = 1;

    public static String strJournalNote[][] =
            {
                    {"Transaksi pembelian (LGR) secara cash", "Transaksi pembelian (LGR) secara kredit"},
                    {"Purchase on LGR (cash) transaction", "Purchase on LGR (credit) transaction"}
            };

    /**
     * Generate list of LGR journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return Number of LGR Journal process
     * @created by Edhy
     * @algoritm :
     * 1. iterate as long as location count to generate LGRJournal
     */
    public int generateLGRJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateLGRJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /**
     * Generate list of LGR journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>ID    of Location object of transaction
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return Number of LGR Journal process
     * @created by Edhy
     * @algoritm :
     * 1. get list of LGR Document from selected BO system
     * 2. iterate as long as document count to generate LGRJournal
     */
    public int generateLGRJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vectOfLGRCreditDoc = new Vector(1, 1);
        try {
            // --- start get list of LGR Document from selected BO system ---                                
            String strIjImplBo = objIjEngineParam.getSIjImplBo();

            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfLGRCreditDoc = i_bosys.getListPurchaseOnLGR(lLocationOid, dSelectedDate);

            // --- start iterate as long as document count to generate LGRJournal ---
            if (vectOfLGRCreditDoc != null && vectOfLGRCreditDoc.size() > 0) {
                int maxLGRCreditDoc = vectOfLGRCreditDoc.size();
                for (int i = 0; i < maxLGRCreditDoc; i++) {
                    IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc = (IjPurchaseOnLGRDoc) vectOfLGRCreditDoc.get(i);

                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(), objIjPurchaseOnLGRDoc.getDocTransCurrency());
                    objIjPurchaseOnLGRDoc.setDocTransCurrency(objCurrencyMapping.getAisoCurrency());

                    long lResult = genJournalOfObjPurchOnLGR(lLocationOid, objIjPurchaseOnLGRDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling Purchase On LGR process skip ... ");
            }
            // --- end iterate as long as document count to generate LGRJournal ---

        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     * Generate LGR journal based on IjPurchaseOnLGRDoc
     *
     * @param <CODE>lLocationOid</CODE>ID of Location object of transaction
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *                                    object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                    type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                    object
     * @return OID of LGR Journal
     * @created by Edhy
     * @algoritm :
     * 1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen LGR (object IjPurchaseOnLGRDoc).
     * 2. Pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Location Mapping "Inventory Location (Persediaan Per Product Department)".
     * 3. Pembuatan jurnal detail posisi kredit (object IjJournalDetail) menggunakan Location Mapping "Purchasing Discount (Potongan Pembelian)".
     * 4. Pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping "Tax on Buying (Pajak Yang Masih Harus Diterima)".
     * 5. Pembuatan jurnal detail posisi kredit (object IjJournalDetail) menggunakan Account Mapping "DP on Purchase Order (Piutang Usaha)".
     * 6. Pembuatan jurnal detail posisi debet (object IjJournalDetail) menggunakan Account Mapping "Goods Received (Hutang Usaha)".
     * 7. masukkan ke vector od IJ Jurnal Detail
     * 8. Generate Journal dengan :
     * 8.1 Debet  : "Inventory Location"
     * 8.2 Kredit : "Purchasing Discount"
     * 8.3 Debet  : "Tax on Buying"
     * 8.4 Kredit : "DP on Purchase Order"
     * 8.5 Debet  : "Goods Receive"
     * @rules :
     * 1. Cek konfigurasi :
     * 1.1 Jika DAT Report:
     * 1.1.1 Create Journal Pengakuan Hutang :
     * o Debet  :  Location Mapping => Inventory Location (Persediaan)
     * o Kredit :  Account Mapping  => Goods Received (Hutang Usaha)
     * <p/>
     * 1.1.2 Create Journal Pengurangan Hutang dengan Discount :
     * o Debet  :  Account Mapping  => Goods Received (Hutang Usaha)
     * o Kredit :  Location Mapping => Purchasing Discount (Potongan Pembelian)
     * <p/>
     * 1.1.3 Create Journal Penambahan Hutang dengan Ppn :
     * o Debet  :  Account Mapping  => Tax on Buying (Pajak Yang Masih Harus Diterima)
     * o Kredit :  Account Mapping  => Goods Received (Hutang Usaha)
     * <p/>
     * 1.1.4 Create Journal Pengurangan Hutang dengan Down Payment :
     * o Debet  :  Account Mapping  => Goods Received (Hutang Usaha)
     * o Kredit :  Account Mapping  => DP on Purchase Order (Piutang Usaha)
     * <p/>
     * >>> Diringkas menjadi :
     * o Debet  :  Location Mapping => Inventory Location (Persediaan)
     * o Debet  :  Account Mapping  => Tax on Buying (Pajak Yang Masih Harus Diterima)
     * o Kredit :  Account Mapping  => Goods Received (Hutang Usaha)
     * o Kredit :  Location Mapping => Purchasing Discount (Potongan Pembelian)
     * o Kredit :  Account Mapping  => DP on Purchase Order (Piutang Usaha)
     * <p/>
     * <p/>
     * 1.2. Jika ?o VAT?:
     * 1.2.1 Create Journal Pengakuan Hutang :
     * o Debet  :  Location Mapping => Inventory Location (Persediaan)
     * o Kredit :  Account Mapping  => Goods Received (Hutang Usaha)
     * <p/>
     * 1.2.2 Create Journal Pengurangan Hutang dengan Discount :
     * o Debet  :  Account Mapping  => Goods Received (Hutang Usaha)
     * o Kredit :  Location Mapping => Purchasing Discount (Potongan Pembelian)
     * <p/>
     * 1.2.3 Create Journal Pengurangan Hutang dengan Down Payment :
     * o Debet  :  Account Mapping  => Goods Received (Hutang Usaha)
     * o Kredit :  Account Mapping  => DP on Purchase Order (Piutang Usaha)
     * <p/>
     * >>> Diringkas menjadi :
     * o Debet  :  Location Mapping => Inventory Location (Persediaan)
     * o Kredit :  Account Mapping  => Goods Received (Hutang Usaha)
     * o Kredit :  Location Mapping => Purchasing Discount (Potongan Pembelian)
     * o Kredit :  Account Mapping  => DP on Purchase Order (Piutang Usaha)
     */
    public long genJournalOfObjPurchOnLGR(long lLocationOid, IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;
        long lTransCurrency = objIjPurchaseOnLGRDoc.getDocTransCurrency();
        Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
        String strStandartRateOfTransCurr = (hashStandartRate.get("" + lTransCurrency)) != null ? "" + hashStandartRate.get("" + lTransCurrency) : "1";
        double dStandartRateOfTransCurr = Double.parseDouble(strStandartRateOfTransCurr);

        // 1. pembuatan object ij jurnal main
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjPurchaseOnLGRDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. start pembuatan vector of object ij jurnal detail
        Vector vectOfObjIjJurDetail = new Vector(1, 1);

        // 2.1 Debet  :  Transaction Location Mapping - Inventory Location (Persediaan Per Product Department)
        Vector vJDPurchValue = genVectObjIjJDOfPurchaseData(objIjPurchaseOnLGRDoc, dStandartRateOfTransCurr);

        // 2.2 Kredit :  Transaction Location Mapping - Purchasing Discount (Potongan Pembelian)
        IjJournalDetail objIjJDPurchDisc = genObjIjJDOfPurchDisc(objIjPurchaseOnLGRDoc, dStandartRateOfTransCurr);

        // 2.3 Debet  :  Account Mapping - Tax on Buying (Pajak Yang Masih Harus Diterima)
        IjJournalDetail objIjJDPurchVat = new IjJournalDetail();
        if (objIjEngineParam.getIConfTaxOnBuy() == I_IJGeneral.CFG_GRP_TAX_BUY_VAT_RPT) {
            objIjJDPurchVat = genObjIjJDOfPurchVat(objIjPurchaseOnLGRDoc, dStandartRateOfTransCurr);
        }

        // 2.4 Kredit :  Account Mapping - DP on Purchase Order (Piutang Usaha)
        Vector vJDDPDeduction = genVectObjIjJDOfDPDeduction(objIjPurchaseOnLGRDoc, hashStandartRate);

        // 2.5 Debet  :  Account Mapping - Goods Received (Hutang Usaha)
        PstIjJournalDetail objPstIjJournalDetail = new PstIjJournalDetail();
        double dTotalPurchValue = objPstIjJournalDetail.getTotalOnDebetSide(vJDPurchValue);
        double dTotalPurchDisc = objIjJDPurchDisc.getJdetCredit();
        double dTotalPurchVat = objIjJDPurchVat.getJdetDebet();
        double dTotalDpDeduction = objPstIjJournalDetail.getTotalOnCreditSide(vJDDPDeduction);
        IjJournalDetail objIjJDPayableOnPurch = genObjIjJDOfPayableOnLGR(objIjPurchaseOnLGRDoc, hashStandartRate, dTotalPurchValue, dTotalPurchDisc, dTotalPurchVat, dTotalDpDeduction);
        System.out.println("dTotalPurchValue  : " + dTotalPurchValue);
        System.out.println("dTotalPurchDisc   : " + dTotalPurchDisc);
        System.out.println("dTotalPurchVat    : " + dTotalPurchVat);
        System.out.println("dTotalDpDeduction : " + dTotalDpDeduction);
        System.out.println("objIjJDPayableOnPurch : " + objIjJDPayableOnPurch.getJdetCredit());

        // 3. masukkan object masing-masing account ke dalam vector of jurnal detail    
        if (vJDPurchValue != null && vJDPurchValue.size() > 0) {
            vectOfObjIjJurDetail.addAll(vJDPurchValue);
        }

        if (objIjJDPurchDisc.getJdetAccChart() != 0) {
            vectOfObjIjJurDetail.add(objIjJDPurchDisc);
        }

        if (objIjJDPurchVat.getJdetAccChart() != 0) {
            vectOfObjIjJurDetail.add(objIjJDPurchVat);
        }

        if (vJDDPDeduction != null && vJDDPDeduction.size() > 0) {
            vectOfObjIjJurDetail.addAll(vJDDPDeduction);
        }

        if (objIjJDPayableOnPurch.getJdetAccChart() != 0) {
            vectOfObjIjJurDetail.add(objIjJDPayableOnPurch);
        }


        System.out.println("before check");
        // 4. save jurnal ke database IJ
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            System.out.println("vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0");
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);

            System.out.println("bJournalBalance : " + bJournalBalance + " and vectOfObjIjJurDetail.size() : " + vectOfObjIjJurDetail.size());
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR, objIjJournalMain.getRefBoDocLastUpdate(), "Purchase on LGR : " + objIjJournalMain.getRefBoDocNumber());

                // posting ke AISO dan update BO Status
                if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO)
                {
                    SessPosting objSessPosting = new SessPosting();
                    objIjJournalMain.setOID(lResult);
                    objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
                }
            }
        }

        System.out.println("before return");

        return lResult;
    }


    /**
     * generate journal main object based on IjDPOnPODoc object
     *
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Document
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return IjJournalMain object
     * @created by Edhy
     */
    private IjJournalMain genObjIjJournalMain(IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjPurchaseOnLGRDoc.getDocTransDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjPurchaseOnLGRDoc.getDocTransCurrency());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()][1]);
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjPurchaseOnLGRDoc.getDocId());
        objIjJournalMain.setRefBoDocNumber(objIjPurchaseOnLGRDoc.getDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjPurchaseOnLGRDoc.getDtLastUpdate());
        objIjJournalMain.setRefBoTransacTionType(I_IJGeneral.TRANSACTION_TYPE_CREDIT);
        objIjJournalMain.setContactOid(objIjPurchaseOnLGRDoc.getDocContact());

        return objIjJournalMain;
    }


    /**
     * pencarian account chart di sisi debet berdasarkan nilai data purchase
     *
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *         object as source of journal process
     * @param <CODE>dStandartRateOfTransCurr</CODE>Standart
     *         rate of transaction currency of IjPurchaseOnLGRDoc object
     * @return
     * @created by Edhy
     */
    private Vector genVectObjIjJDOfPurchaseData(IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc, double dStandartRateOfTransCurr) {
        Vector vResult = new Vector(1, 1);
        System.out.println("before purch data");

        Vector vPurchaseValue = objIjPurchaseOnLGRDoc.getListPurchaseValue();
        System.out.println("after getListPurchaseValue");
        if (vPurchaseValue != null && vPurchaseValue.size() > 0) {
            int iPurchValueCount = vPurchaseValue.size();
            for (int i = 0; i < iPurchValueCount; i++) {
                System.out.println("before casting objIjPurchaseValue");
                IjPurchaseValue objIjPurchaseValue = (IjPurchaseValue) vPurchaseValue.get(i);
                System.out.println("after casting objIjPurchaseValue");

                // Transaction Location Mapping - Inventory Location (Persediaan)                
                PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
                System.out.println("objIjPurchaseOnLGRDoc.getDocSaleType() : " + objIjPurchaseOnLGRDoc.getDocSaleType());
                // objIjPurchaseOnLGRDoc.getDocSaleType()
                IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, objIjPurchaseOnLGRDoc.getDocTransCurrency(), objIjPurchaseOnLGRDoc.getDocLocation(), objIjPurchaseValue.getProdDepartment());
                long locationAccChart = objIjLocationMapping.getAccount();
                System.out.println("after loc mapping : " + locationAccChart + " - " + objIjPurchaseValue.getPurchValue() + " - " + dStandartRateOfTransCurr);

                IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                if (locationAccChart != 0) {
                    objIjJournalDetail.setJdetAccChart(locationAccChart);
                    objIjJournalDetail.setJdetTransCurrency(objIjPurchaseOnLGRDoc.getDocTransCurrency());
                    objIjJournalDetail.setJdetTransRate(dStandartRateOfTransCurr);
                    objIjJournalDetail.setJdetDebet((objIjPurchaseValue.getPurchValue() * dStandartRateOfTransCurr));
                    objIjJournalDetail.setJdetCredit(0);

                    vResult.add(objIjJournalDetail);
                }
            }
        }

        System.out.println("after purch data");
        return vResult;
    }


    /**
     * pencarian account chart di sisi kredit berdasarkan nilai purchase discount
     *
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *         object as source of journal process
     * @param <CODE>dStandartRateOfTransCurr</CODE>Standart
     *         rate of transaction currency of IjPurchaseOnLGRDoc object
     * @return
     * @created by Edhy
     */
    private IjJournalDetail genObjIjJDOfPurchDisc(IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc, double dStandartRateOfTransCurr) {
        // Transaction Location Mapping - Purchasing Discount (Potongan Pembelian)
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMapping = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_PURCHASE_DISCOUNT, -1, objIjPurchaseOnLGRDoc.getDocTransCurrency(), objIjPurchaseOnLGRDoc.getDocLocation(), 0);
        long purchDiscountAccChart = objIjLocationMapping.getAccount();

        IjJournalDetail objIjJournalDetail = new IjJournalDetail();
        if (purchDiscountAccChart != 0) {
            objIjJournalDetail.setJdetAccChart(purchDiscountAccChart);
            objIjJournalDetail.setJdetTransCurrency(objIjPurchaseOnLGRDoc.getDocTransCurrency());
            objIjJournalDetail.setJdetTransRate(dStandartRateOfTransCurr);
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit((dStandartRateOfTransCurr * objIjPurchaseOnLGRDoc.getDocDiscount()));
        }

        return objIjJournalDetail;
    }


    /**
     * pencarian account chart di sisi debet berdasarkan nilai ppn
     *
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *         object as source of journal process
     * @param <CODE>dStandartRateOfTransCurr</CODE>Standart
     *         rate of transaction currency of IjPurchaseOnLGRDoc object
     * @return
     * @created by Edhy
     */
    private IjJournalDetail genObjIjJDOfPurchVat(IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc, double dStandartRateOfTransCurr) {
        // Account Mapping - Tax on Buying (Pajak Yang Masih Harus Diterima)
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_TAX_ON_BUYING, objIjPurchaseOnLGRDoc.getDocTransCurrency());
        long taxOnBuyingAccChart = objIjAccountMapping.getAccount();

        IjJournalDetail objIjJournalDetail = new IjJournalDetail();
        if (taxOnBuyingAccChart != 0) {
            objIjJournalDetail.setJdetAccChart(taxOnBuyingAccChart);
            objIjJournalDetail.setJdetTransCurrency(objIjPurchaseOnLGRDoc.getDocTransCurrency());
            objIjJournalDetail.setJdetTransRate(dStandartRateOfTransCurr);
            objIjJournalDetail.setJdetDebet((dStandartRateOfTransCurr * objIjPurchaseOnLGRDoc.getDocTax()));
            objIjJournalDetail.setJdetCredit(0);

            System.out.println("taxOnBuyingAccChart : " + taxOnBuyingAccChart);
            System.out.println("objIjPurchaseOnLGRDoc.getDocTransCurrency() : " + objIjPurchaseOnLGRDoc.getDocTransCurrency());
            System.out.println("objIjPurchaseOnLGRDoc.getDocTax() : " + objIjPurchaseOnLGRDoc.getDocTax());
        }


        return objIjJournalDetail;
    }


    /**
     * pencarian account chart di sisi kredit berdasarkan "Dp Deduction"
     *
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *         object as source of journal process
     * @param <CODE>hashStandartRate</CODE>Hashtable
     *         object that handle Standart Rate data
     * @return
     * @created by Edhy
     */
    private Vector genVectObjIjJDOfDPDeduction(IjPurchaseOnLGRDoc objPurchaseOnLGRDoc, Hashtable hashStandartRate) {
        Vector vResult = new Vector(1, 1);

        Vector vDPDeduction = objPurchaseOnLGRDoc.getListDPDeduction();
        if (vDPDeduction != null && vDPDeduction.size() > 0) {
            int iDPDeductionCount = vDPDeduction.size();
            for (int i = 0; i < iDPDeductionCount; i++) {
                IjDPDeductionDoc objIjDPDeductionDoc = (IjDPDeductionDoc) vDPDeduction.get(i);

                // Account Mapping - DP on Purchase Order (Piutang Usaha)
                PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
                IjAccountMapping objIjAccountMapping = objPstIjAccountMapping.getObjIjAccountMapping(I_IJGeneral.TRANS_DP_ON_PURCHASE_ORDER, objPurchaseOnLGRDoc.getDocTransCurrency());
                long dpDeductionAccChart = objIjAccountMapping.getAccount();

                IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                if (dpDeductionAccChart != 0) {
                    String strStandartRate = (hashStandartRate.get("" + objIjDPDeductionDoc.getPayCurrency())) != null ? "" + hashStandartRate.get("" + objIjDPDeductionDoc.getPayCurrency()) : "1";
                    double dStandartRate = Double.parseDouble(strStandartRate);

                    objIjJournalDetail.setJdetAccChart(dpDeductionAccChart);
                    objIjJournalDetail.setJdetTransCurrency(objPurchaseOnLGRDoc.getDocTransCurrency());
                    objIjJournalDetail.setJdetTransRate(dStandartRate);
                    objIjJournalDetail.setJdetDebet(0);
                    objIjJournalDetail.setJdetCredit((objIjDPDeductionDoc.getPayNominal() * dStandartRate));

                    System.out.println("objIjJournalDetail.setJdetAccChart() : " + dpDeductionAccChart);
                    System.out.println("objPurchaseOnLGRDoc.getDocTransCurrency() : " + objPurchaseOnLGRDoc.getDocTransCurrency());
                    System.out.println("(objIjDPDeductionDoc.getPayNominal()*dStandartRate) : " + (objIjDPDeductionDoc.getPayNominal() * dStandartRate));
                    vResult.add(objIjJournalDetail);
                }
            }
        }

        System.out.println("vResult : " + vResult.size());

        return vResult;
    }


    /**
     * pencarian account chart di sisi kredit berdasarkan Hutang karena "Goods Receive"
     *
     * @param <CODE>objIjPurchaseOnLGRDoc</CODE>IjPurchaseOnLGRDoc
     *         object as source of journal process
     * @param <CODE>hashStandartRate</CODE>Hashtable
     *         object that handle Standart Rate data
     * @param <CODE>dTotalPurchValue</CODE>Total
     *         purchase value
     * @param <CODE>dTotalPurchDisc</CODE>Total
     *         purchase discount
     * @param <CODE>dTotalPurchVat</CODE>Total
     *         VAT on purchase
     * @param <CODE>dTotalDpDeduction</CODE>Total
     *         DP Deduction
     * @return
     * @created by Edhy
     */
    private IjJournalDetail genObjIjJDOfPayableOnLGR(IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc, Hashtable hashStandartRate, double dTotalPurchValue, double dTotalPurchDisc, double dTotalPurchVat, double dTotalDpDeduction) {
        // Account Mapping - Goods Received (Hutang Usaha)  
        PstIjAccountMapping objPstIjAccountMappingCr = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingCr = objPstIjAccountMappingCr.getObjIjAccountMapping(I_IJGeneral.TRANS_GOODS_RECEIVE, objIjPurchaseOnLGRDoc.getDocTransCurrency());
        long goodReceiveAccChart = objIjAccountMappingCr.getAccount();

        IjJournalDetail objIjJournalDetail = new IjJournalDetail();
        if (goodReceiveAccChart != 0) {
            String strStandartRate = (hashStandartRate.get("" + objIjPurchaseOnLGRDoc.getDocTransCurrency())) != null ? "" + hashStandartRate.get("" + objIjPurchaseOnLGRDoc.getDocTransCurrency()) : "1";
            double dStandartRate = Double.parseDouble(strStandartRate);

            objIjJournalDetail.setJdetAccChart(goodReceiveAccChart);
            objIjJournalDetail.setJdetTransCurrency(objIjPurchaseOnLGRDoc.getDocTransCurrency());
            objIjJournalDetail.setJdetTransRate(dStandartRate);
            objIjJournalDetail.setJdetDebet(0);
            objIjJournalDetail.setJdetCredit((dTotalPurchValue - dTotalPurchDisc + dTotalPurchVat - dTotalDpDeduction));
        }

        return objIjJournalDetail;
    }
}
