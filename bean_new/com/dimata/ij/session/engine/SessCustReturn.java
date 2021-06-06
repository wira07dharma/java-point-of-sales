/*
 * SessCustomerReturn.java
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
 * @author Administrator
 */
public class SessCustReturn {

    public static final int TYPE_CUST_RETURN = 0;
    public static final int TYPE_LGR_CUST_RETURN = 1;

    // define journal note for transaction CustReturn
    public static String strJournalNote[][] =
            {
                    {"Transaksi pengembalian barang dari customer", "Inventory return by customer"},
                    {"Transaksi penerimaan barang dari customer return", "LGR from customer return"}
            };


    /**
     * Generate list of Inventory On Customer Return journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     */
    public int generateCustReturnJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        System.out.println("vLocationOid : "+vLocationOid);
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateCustReturnJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /*
    * Generate list of Inventory On Customer Return journal based on selected document on selected Bo system
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
    *  2. iterate as long as document count to generate CustReturnJournal
    */
    public int generateCustReturnJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR) 
        Vector vectOfCustReturnDoc = new Vector(1, 1);
        try {
            // --- start get list of CustReturn Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfCustReturnDoc = i_bosys.getListCustReturn(lLocationOid, dSelectedDate);
            // --- end get list of CustReturn Document from selected BO system ---
             System.out.println("==@@@@@@@@@@@@@@@@@ vectOfCustReturnDoc >>>> : "+vectOfCustReturnDoc);
            // --- start iterate as long as document count to generate CustReturnJournal ---
            if (vectOfCustReturnDoc != null && vectOfCustReturnDoc.size() > 0) {
                System.out.println("==@@@@@@@@@@@@@@@@@ >>>> Process Customer Return start");
                int maxDFToWhDoc = vectOfCustReturnDoc.size();
                for (int i = 0; i < maxDFToWhDoc; i++) {
                    IjCustReturnDoc objIjCustReturnDoc = (IjCustReturnDoc) vectOfCustReturnDoc.get(i);

                    long lResult = genJournalOfObjCustReturn(lLocationOid, objIjCustReturnDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                        lResult = genJournalOfObjLgrOnCustReturn(lLocationOid, objIjCustReturnDoc, iDocTypeReference, objIjEngineParam);
                        if (lResult != 0) {
                            result++;
                        }
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling CustReturn process skip ... ");
            }
            // --- end iterate as long as document count to generate CustReturnJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     * Generate list of CustReturn journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *         where transaction occur
     * @param <CODE>objIjCustReturnDoc</CODE>IjCustReturnDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     * @algoritm :
     * 1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen DF for costing (object IjInventoryOnDFDoc).
     * 2. Pembuatan vector of jurnal detail posisi debet dengan location mapping dan kredit dengan account mapping.
     * 3. Generate Journal dengan nilai sebenar 'return value' :
     * 3.1 Debet  : "Customer Return Value"
     * 3.2 Kredit : "Hutang"
     */
    public long genJournalOfObjCustReturn(long lLocationOid, IjCustReturnDoc objIjCustReturnDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen customer return (object objIjCustReturnDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjCustReturnDoc, iDocTypeReference, lLocationOid, objIjEngineParam, TYPE_CUST_RETURN);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data customer return.
        Vector vectOfObjIjJurDetail = genObjIjJDOfCustReturn(lLocationOid, objIjCustReturnDoc, objIjEngineParam);

        // 3. save jurnal ke database IJ 
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            // 4. generate Journal, update doc logger n posting to AISO if setting is automatically
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_CUST_RETURN, objIjJournalMain.getRefBoDocLastUpdate(), "Customer Return : " + objIjJournalMain.getRefBoDocNumber());

                // posting ke AISO dan update BO Status
                if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO)
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
     * Generate list of LGR of CustReturn journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *         where transaction occur
     * @param <CODE>objIjCustReturnDoc</CODE>IjCustReturnDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     * @algoritm :
     * 1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen LGR of CustReturn (object IjInventoryOnDFDoc).
     * 2. Pembuatan vector of jurnal detail posisi debet dan kredit dengan location mapping
     * 3. Generate Journal dengan sebesar 'cost value' :
     * 3.1 Debet  : "Inventory"
     * 3.2 Kredit : "HPP"
     */
    public long genJournalOfObjLgrOnCustReturn(long lLocationOid, IjCustReturnDoc objIjCustReturnDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen customer return (object objIjCustReturnDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjCustReturnDoc, iDocTypeReference, lLocationOid, objIjEngineParam, TYPE_LGR_CUST_RETURN);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data customer return.
        Vector vectOfObjIjJurDetail = genObjIjJDOfLgrOnCustReturn(objIjCustReturnDoc, objIjEngineParam);

        // 3. save jurnal ke database IJ 
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            // 4. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // posting ke AISO dan update BO Status
                if (lResult != 0 && objIjEngineParam.getIConfJournalSystem() == I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO)
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
     * @param <CODE>objIjCustReturnDoc</CODE>IjCustReturnDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalMain genObjIjJournalMain(IjCustReturnDoc objIjCustReturnDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam, int iType) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjCustReturnDoc.getDtDocDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[iType][objIjEngineParam.getILanguage()]+" : "+objIjCustReturnDoc.getSDocNumber());
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjCustReturnDoc.getLDocId());
        objIjJournalMain.setRefBoDocNumber(objIjCustReturnDoc.getSDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjCustReturnDoc.getDtLastUpdate());

        return objIjJournalMain;
    }


    /**
     * Membuat detail utk posisi debet dengan mengambil Location Mapping (CUST_RETURN)
     * Membuat detail utk posisi kredit dengan mengambil Account Mapping (CUST_RETURN)
     *
     * @param <CODE>objIjCustReturnDoc</CODE>IjCustReturnDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private Vector genObjIjJDOfCustReturn(long oidLocation, IjCustReturnDoc objIjCustReturnDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // if payment type = AR Deduct
        if (objIjCustReturnDoc.getIPaymentType() == I_IJGeneral.TYPE_RET_PAYMENT_AR_DEDUCT) {
            // -- start debet --
            // Ambil Transaction Location Mapping ?ustomer Return?
            PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
            IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_CUSTOMER_RETURN, -1, 0, oidLocation, 0);
            long locationAccChartDebet = objIjLocationMappingDebet.getAccount();

            System.out.println(">>>>>>> : locationAccChartDebet "+locationAccChartDebet);
            // pembuatan jurnal detail                
            if (locationAccChartDebet != 0) {
                IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
                objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);
                objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
                objIjJournalDetailDebet.setJdetTransRate(1);
                objIjJournalDetailDebet.setJdetDebet(objIjCustReturnDoc.getDTotalValue());
                objIjJournalDetailDebet.setJdetCredit(0);
                vResult.add(objIjJournalDetailDebet);
            }

            // -- start kredit --
            // Ambil Account Mapping ?ustomer Return?
            PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
            IjAccountMapping objIjAccountMappingKredit = objPstIjAccountMapping.getObjIjAccountMapping(0, I_IJGeneral.TRANS_CUSTOMER_RETURN, objIjEngineParam.getLBookType());
            long accountAccChartKredit = objIjAccountMappingKredit.getAccount();
            System.out.println(">>>>>>> : accountAccChartKredit "+accountAccChartKredit);

            // pembuatan jurnal detail
            if (accountAccChartKredit != 0) {
                IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
                objIjJournalDetailKredit.setJdetAccChart(accountAccChartKredit);
                objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());
                objIjJournalDetailKredit.setJdetTransRate(1);
                objIjJournalDetailKredit.setJdetDebet(0);
                objIjJournalDetailKredit.setJdetCredit(objIjCustReturnDoc.getDTotalValue());
                vResult.add(objIjJournalDetailKredit);
            }
        }

        // if payment type = cash
        else {
            // -- start debet --
            // Ambil Transaction Location Mapping ?ustomer Return?
            PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
            IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_CUSTOMER_RETURN, -1, 0, oidLocation, 0);
            long locationAccChartDebet = objIjLocationMappingDebet.getAccount();

            System.out.println("==>>>>> Payment Cash");
            System.out.println("locationAccChartDebet : "+locationAccChartDebet);
            // pembuatan jurnal detail
            if (locationAccChartDebet != 0) {
                IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
                objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);
                objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
                objIjJournalDetailDebet.setJdetTransRate(1);
                objIjJournalDetailDebet.setJdetDebet(objIjCustReturnDoc.getDTotalValue());
                objIjJournalDetailDebet.setJdetCredit(0);
                vResult.add(objIjJournalDetailDebet);
            }

            // -- start kredit --
            vResult.addAll(genObjIjJournalDetailBaseOnPayment(objIjCustReturnDoc, objIjEngineParam));
        }

        return vResult;
    }


    /**
     * Membuat detail utk posisi debet dan kredit dengan mengambil Location Mapping (CUST_RETURN)
     *
     * @param <CODE>objIjCustReturnDoc</CODE>IjCustReturnDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private Vector genObjIjJDOfLgrOnCustReturn(IjCustReturnDoc objIjCustReturnDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // -- start debet --
        // Ambil Location Mapping ?nventory/Goods Location?
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, 0, 0);
        long locationAccChartDebet = objIjLocationMappingDebet.getAccount();

        // pembuatan jurnal detail                
        if (locationAccChartDebet != 0) {
            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
            objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);
            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetailDebet.setJdetTransRate(1);
            objIjJournalDetailDebet.setJdetDebet(objIjCustReturnDoc.getDTotalCost());
            objIjJournalDetailDebet.setJdetCredit(0);
            vResult.add(objIjJournalDetailDebet);
        }

        // -- start kredit --
        // Ambil Location Mapping ?ost Of Goods Sold?
        IjLocationMapping objIjLocationMappingKredit = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_COGS, -1, 0, 0, 0);
        long locationAccChartKredit = objIjLocationMappingKredit.getAccount();

        // pembuatan jurnal detail
        if (locationAccChartKredit != 0) {
            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
            objIjJournalDetailKredit.setJdetAccChart(locationAccChartKredit);
            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetailKredit.setJdetTransRate(1);
            objIjJournalDetailKredit.setJdetDebet(0);
            objIjJournalDetailKredit.setJdetCredit(objIjCustReturnDoc.getDTotalCost());
            vResult.add(objIjJournalDetailKredit);
        }

        return vResult;
    }


    private Vector genObjIjJournalDetailBaseOnPayment(IjCustReturnDoc objIjCustReturnDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        Vector vectPayment = objIjCustReturnDoc.getListPayment();
        System.out.println("get vectPayment : "+vectPayment);
        if (vectPayment != null && vectPayment.size() > 0) {
            Hashtable hashStandartRate = objIjEngineParam.getHStandartRate();
            int maxPayment = vectPayment.size();
            PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
            for (int j = 0; j < maxPayment; j++) {
                IjPaymentDoc objPaymentDoc = (IjPaymentDoc) vectPayment.get(j);
                IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjEngineParam.getIBoSystem(), objPaymentDoc.getPayCurrency());
                objPaymentDoc.setPayCurrency(objCurrencyMapping.getAisoCurrency());

                // pencarian account chart di sisi kredit
                String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + objPaymentDoc.getPayType();
                if (objPaymentDoc.getPayCurrency() != 0) {
                    whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + objPaymentDoc.getPayCurrency();
                }

                long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);
                System.out.println("paymentAccChart : "+paymentAccChart);
                if (paymentAccChart != 0) {
                    String strStandartRatePay = (hashStandartRate.get("" + objPaymentDoc.getPayCurrency())) != null ? "" + hashStandartRate.get("" + objPaymentDoc.getPayCurrency()) : "1";
                    double standartRatePay = Double.parseDouble(strStandartRatePay);

                    // membuat detail utk posisi kredit                        
                    IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                    objIjJournalDetail.setJdetAccChart(paymentAccChart);
                    objIjJournalDetail.setJdetTransCurrency(objPaymentDoc.getPayCurrency());
                    objIjJournalDetail.setJdetTransRate(standartRatePay);
                    objIjJournalDetail.setJdetDebet(0);
                    objIjJournalDetail.setJdetCredit((objPaymentDoc.getPayNominal() * standartRatePay));
                    vResult.add(objIjJournalDetail);
                }
            }
        }

        return vResult;
    }
}
