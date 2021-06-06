/*
 * SessSuppReturn.java
 *
 * Created on January 10, 2006, 7:18 AM
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
public class SessSuppReturn {

    // define journal note for transaction SuppReturn
    public static String strJournalNote[] =
            {
                    "Transaksi pengembalian barang ke supplier", "Inventory return to supplier"
            };


    /**
     * Generate list of Inventory On Supplier Return journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     */
    public int generateSuppReturnJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateSuppReturnJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }


    /*
    * Generate list of Inventory On Supplier Return journal based on selected document on selected Bo system
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
    *  1. get list of Inv Supplier Return Document from selected BO system
    *  2. iterate as long as document count to generate SuppReturnJournal
    */
    public int generateSuppReturnJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getListSuppReturn) 
        Vector vectOfSuppReturnDoc = new Vector(1, 1);
        try {
            // --- start get list of SuppReturn Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfSuppReturnDoc = i_bosys.getListSuppReturn(lLocationOid, dSelectedDate);
            // --- end get list of SuppReturn Document from selected BO system ---

            System.out.println("vector vectOfSuppReturnDoc :" + vectOfSuppReturnDoc.size());
            // --- start iterate as long as document count to generate SuppReturnJournal ---
            if (vectOfSuppReturnDoc != null && vectOfSuppReturnDoc.size() > 0) {
                int maxDFToWhDoc = vectOfSuppReturnDoc.size();
                for (int i = 0; i < maxDFToWhDoc; i++) {
                    IjSuppReturnDoc objIjSuppReturnDoc = (IjSuppReturnDoc) vectOfSuppReturnDoc.get(i);

                    long lResult = genJournalOfObjSuppReturn(lLocationOid, objIjSuppReturnDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling SuppReturn process skip ... ");
            }
            // --- end iterate as long as document count to generate SuppReturnJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     * Generate list of SuppReturn journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *         where transaction occur
     * @param <CODE>objIjSuppReturnDoc</CODE>IjSuppReturnDoc
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
     * 3.1 Debet  : "Supplier Return Value"
     * 3.2 Kredit : "Hutang"
     */
    public long genJournalOfObjSuppReturn(long lLocationOid, IjSuppReturnDoc objIjSuppReturnDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Supplier Return (object objIjSuppReturnDoc).        
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjSuppReturnDoc, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail (object IjJournalDetail) dengan mengambil data Supplier Return.
        Vector vectOfObjIjJurDetail = genObjIjJDOfSuppReturn(lLocationOid, objIjSuppReturnDoc, objIjEngineParam);

        // 3. save jurnal ke database IJ 
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            // 4. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
                // generate journal    
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), I_IJGeneral.DOC_TYPE_SUPP_RETURN, objIjJournalMain.getRefBoDocLastUpdate(), "Supplier Return : " + objIjJournalMain.getRefBoDocNumber());

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
     * @param <CODE>objIjSuppReturnDoc</CODE>IjSuppReturnDoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     */
    private IjJournalMain genObjIjJournalMain(IjSuppReturnDoc objIjSuppReturnDoc, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjSuppReturnDoc.getDtDocDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(strJournalNote[objIjEngineParam.getILanguage()] + " " + objIjSuppReturnDoc.getSDocNumber());
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjSuppReturnDoc.getLDocId());
        objIjJournalMain.setRefBoDocNumber(objIjSuppReturnDoc.getSDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjSuppReturnDoc.getDtLastUpdate());

        return objIjJournalMain;
    }


    /**
     * Membuat detail utk posisi debet dengan mengambil Account Mapping (Supplier Return)
     * Membuat detail utk posisi kredit dengan mengambil Location Mapping (Goods Location)
     *
     * @param <CODE>objIjSuppReturnDoc</CODE>IjSuppReturnDoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private Vector genObjIjJDOfSuppReturn(long lLocationOid, IjSuppReturnDoc objIjSuppReturnDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        // -- start debet --
        // Ambil Account Mapping ?upplier Return?
        PstIjAccountMapping objPstIjAccountMapping = new PstIjAccountMapping();
        IjAccountMapping objIjAccountMappingDebet = objPstIjAccountMapping.getObjIjAccountMapping(lLocationOid, I_IJGeneral.TRANS_SUPPLIER_RETURN, objIjEngineParam.getLBookType());
        long accountAccChartDebet = objIjAccountMappingDebet.getAccount();
        System.out.println("accountAccChartDebet : " + accountAccChartDebet);
        // pembuatan jurnal detail                
        if (accountAccChartDebet != 0) {
            IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
            objIjJournalDetailDebet.setJdetAccChart(accountAccChartDebet);
            objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetailDebet.setJdetTransRate(1);
            objIjJournalDetailDebet.setJdetDebet(objIjSuppReturnDoc.getDTotalValue());
            objIjJournalDetailDebet.setJdetCredit(0);
            vResult.add(objIjJournalDetailDebet);
        }

        // -- start kredit --
        // Ambil Location Mapping ?oods Location?
        PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
        IjLocationMapping objIjLocationMappingKredit = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_SUPPLIER_RETURN, -1, 0, lLocationOid, 0); // TRANS_INVENTORY_LOCATION
        long locationAccChartKredit = objIjLocationMappingKredit.getAccount();
        System.out.println("locationAccChartKredit : " + locationAccChartKredit);
        // pembuatan jurnal detail
        if (locationAccChartKredit != 0) {
            IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
            objIjJournalDetailKredit.setJdetAccChart(locationAccChartKredit);
            objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());
            objIjJournalDetailKredit.setJdetTransRate(1);
            objIjJournalDetailKredit.setJdetDebet(0);
            objIjJournalDetailKredit.setJdetCredit(objIjSuppReturnDoc.getDTotalValue());
            vResult.add(objIjJournalDetailKredit);
        }

        return vResult;
    }

}
