/*
 * SessSuppReturnDeduction.java
 *
 * Created on January 10, 2006, 7:19 AM
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
public class SessSuppReturnDeduct {

    // define journal note for transaction SuppReturnDeduct
    public static String strJournalNote[] =
            {
                    "Transaksi Supplier Return deduction untuk ", "Supplier Return deduction for "
            };


    /**
     * Generate list of Inventory On Supplier Return Deduct journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     */
    public int generateSuppReturnDeductJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateSuppReturnDeductJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /*
    * Generate list of Inventory On Supplier Return Deduct journal based on selected document on selected Bo system
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
    *  2. iterate as long as document count to generate SuppReturnDeductJournal
    */
    public int generateSuppReturnDeductJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR) 
        Vector vectOfSuppReturnDeductDoc = new Vector(1, 1);
        try {
            // --- start get list of SuppReturnDeduct Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfSuppReturnDeductDoc = i_bosys.getListSuppReturnDeduct(lLocationOid, dSelectedDate);
            // --- end get list of SuppReturnDeduct Document from selected BO system ---

            // --- start iterate as long as document count to generate SuppReturnDeductJournal ---
            if (vectOfSuppReturnDeductDoc != null && vectOfSuppReturnDeductDoc.size() > 0) {
                int maxDFToWhDoc = vectOfSuppReturnDeductDoc.size();
                for (int i = 0; i < maxDFToWhDoc; i++) {
                    IjSuppReturnDeductDoc objIjSuppReturnDeductDoc = (IjSuppReturnDeductDoc) vectOfSuppReturnDeductDoc.get(i);

                    long lResult = genJournalOfObjSuppReturnDeduct(lLocationOid, objIjSuppReturnDeductDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling SuppReturnDeduct process skip ... ");
            }
            // --- end iterate as long as document count to generate SuppReturnDeductJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     * Generate list of SuppReturnDeduct journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *         where transaction occur
     * @param <CODE>objIjSuppReturnDeductDoc</CODE>IjSuppReturnDeductDoc
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
     * 3. Generate Journal dengan :
     * 3.1 Debet  : "Supplier Return Deduct Value"
     * 3.2 Kredit : "Hutang"
     */
    public long genJournalOfObjSuppReturnDeduct(long lLocationOid, IjSuppReturnDeductDoc objIjSuppReturnDeductDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Supplier Return Deduct (object objIjSuppReturnDeductDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjSuppReturnDeductDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data Supplier Return Deduct.
        Vector vectOfObjIjJurDetail = genObjIjJDOfSuppReturnDeduct(objIjSuppReturnDeductDoc, objIjEngineParam);

        // 3. save jurnal ke database IJ 
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            // 4. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_SUPP_RETURN_DEDUCTION, objIjJournalMain.getRefBoDocLastUpdate(), "Supplier Return Deduction : " + objIjJournalMain.getRefBoDocNumber());

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
     * @param <CODE>objIjSuppReturnDeductDoc</CODE>IjSuppReturnDeductDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalMain genObjIjJournalMain(IjSuppReturnDeductDoc objIjSuppReturnDeductDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjSuppReturnDeductDoc.getDtDocDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()]);
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjSuppReturnDeductDoc.getLDocId());
        objIjJournalMain.setRefBoDocNumber(objIjSuppReturnDeductDoc.getSDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjSuppReturnDeductDoc.getDtLastUpdate());

        return objIjJournalMain;
    }


    /**
     * Membuat detail utk posisi debet dengan mengambil Account Mapping (Goods Receive)
     * Membuat detail utk posisi kredit dengan mengambil Account Mapping (Supplier Return)
     *
     * @param <CODE>objIjSuppReturnDeductDoc</CODE>IjSuppReturnDeductDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private Vector genObjIjJDOfSuppReturnDeduct(IjSuppReturnDeductDoc objIjSuppReturnDeductDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // -- start debet --
        // Ambil Account Mapping ?oods Receive?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingDebet = objPstIjAccountMapping.getObjIjAccountMapping(0, I_IJGeneral.TRANS_GOODS_RECEIVE, objIjEngineParam.getLBookType());
        long accountAccChartDebet = objIjAccountMappingDebet.getAccount();

        // pembuatan jurnal detail                
        if (accountAccChartDebet != 0) {
            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
            objIjJournalDetailDebet.setJdetAccChart(accountAccChartDebet);
            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetailDebet.setJdetTransRate(1);
            objIjJournalDetailDebet.setJdetDebet(objIjSuppReturnDeductDoc.getDTotalReturnValue());
            objIjJournalDetailDebet.setJdetCredit(0);
            vResult.add(objIjJournalDetailDebet);
        }

        // -- start kredit --
        // Ambil Account Mapping ?upplier Return?       
        IjAccountMapping objIjAccountMappingKredit = objPstIjAccountMapping.getObjIjAccountMapping(0, I_IJGeneral.TRANS_SUPPLIER_RETURN, objIjEngineParam.getLBookType());
        long accountAccChartKredit = objIjAccountMappingKredit.getAccount();

        // pembuatan jurnal detail
        if (accountAccChartKredit != 0) {
            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
            objIjJournalDetailKredit.setJdetAccChart(accountAccChartKredit);
            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetailKredit.setJdetTransRate(1);
            objIjJournalDetailKredit.setJdetDebet(0);
            objIjJournalDetailKredit.setJdetCredit(objIjSuppReturnDeductDoc.getDTotalReturnValue());
            vResult.add(objIjJournalDetailKredit);
        }

        return vResult;
    }
}
