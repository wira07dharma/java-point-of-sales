/*
 * SessInventoryOnDF.java
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
 * @author gedhy
 */
public class SessInventoryOnDF {

    // define journal note for transaction Purchase On LGR (Cash or Credit)
    public static final int DF_TO_WH = 0;
    public static final int DF_TO_PROD = 1;

    public static String strJournalNote[][] =
            {
                    {"Pengiriman barang", "Transaksi pengiriman ke produksi"},
                    {"Transfer Inventory", "DF to Production transaction"}
            };

    // ------------------- Start Transfer WH to WH ------------------

    /**
     * Generate list of Inventory On DF journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     */
    public int generateDFToWhJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDFToWhJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }
        return result;
    }


    public int generateReturnInventoryJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateReturnToInventoryJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }
        return result;
    }


    /*
    * Generate list of Inventory On DF to WH journal based on selected document on selected Bo system
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
    *  1. get list of Inventory On DF Document from selected BO system
    *  2. iterate as long as document count to generate InventoryOnDFJournal
    */
    public int generateDFToWhJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR)
        Vector vectOfDFToWhDoc = new Vector(1, 1);
        try {
            // --- start get list of Inventory On DF Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfDFToWhDoc = i_bosys.getListInventoryOnDF(lLocationOid, dSelectedDate, I_IJGeneral.DF_TYPE_WAREHOUSE);
            // --- end get list of Inventory On DF Document from selected BO system ---

            // --- start iterate as long as document count to generate InventoryOnDFJournal ---
            if (vectOfDFToWhDoc != null && vectOfDFToWhDoc.size() > 0) {
                int maxDFToWhDoc = vectOfDFToWhDoc.size();
                for (int i = 0; i < maxDFToWhDoc; i++) {
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = (IjInventoryOnDFDoc) vectOfDFToWhDoc.get(i);

                    long lResult = genJournalOfObjDFToWH(lLocationOid, objIjInventoryOnDFDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling Inventory On DF process skip ... ");
            }
            // --- end iterate as long as document count to generate InventoryOnDFJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }

    public int generateReturnToInventoryJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR)
        Vector vectOfDFToWhDoc = new Vector(1, 1);
        try {
            // --- start get list of Inventory On DF Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfDFToWhDoc = i_bosys.getListInventoryOnDF(lLocationOid, dSelectedDate, I_IJGeneral.DF_TYPE_PRODUCTION);
            // --- end get list of Inventory On DF Document from selected BO system ---

            // --- start iterate as long as document count to generate InventoryOnDFJournal ---
            if (vectOfDFToWhDoc != null && vectOfDFToWhDoc.size() > 0) {
                int maxDFToWhDoc = vectOfDFToWhDoc.size();
                for (int i = 0; i < maxDFToWhDoc; i++) {
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = (IjInventoryOnDFDoc) vectOfDFToWhDoc.get(i);

                    long lResult = genJournalOfObjDFToWH(lLocationOid, objIjInventoryOnDFDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling Inventory On DF process skip ... ");
            }
            // --- end iterate as long as document count to generate InventoryOnDFJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }

    /**
     * Generate list of Inventory On DF to WH journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *         where transaction occur
     * @param <CODE>objIjInventoryOnDFDoc</CODE>IjDPOnPdODoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     * @algoritm :
     * 1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Inventory DF (object IjInventoryOnDFDoc).
     * 2. Pembuatan vector of jurnal detail posisi debet (destination loc) dan kredit (source location).
     * 3. Generate Journal dengan :
     * 3.1 Debet  : "Inventory on Destination Location"
     * 3.2 Kredit : "Inventory on Source Location"
     */
    public long genJournalOfObjDFToWH(long lLocationOid, IjInventoryOnDFDoc objIjInventoryOnDFDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen transfer barang ke gudang (object objIjInventoryOnDFDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjInventoryOnDFDoc, DF_TO_WH, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
        Vector vectOfObjIjJurDetail = genObjIjJDOfInventoryTransferWHtoWH(objIjInventoryOnDFDoc, objIjEngineParam);

        // 3. save jurnal ke database IJ
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            // 4. generate Journal
            boolean bJournalBalance = PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail);
            if ((vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 1) && bJournalBalance) {
                // generate journal
                lResult = PstIjJournalMain.generateIjJournal(objIjJournalMain, vectOfObjIjJurDetail);

                // update doc logger
                PstDocLogger.generateDocLogger(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), iDocTypeReference, objIjJournalMain.getRefBoDocLastUpdate(), "Inventory On DF : " + objIjJournalMain.getRefBoDocNumber());

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
     * Membuat detail utk posisi debet dan kredit dengan mengambil Location Mapping
     *
     * @param <CODE>objIjInventoryOnDFDoc</CODE>IjDPOnPdODoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private Vector genObjIjJDOfInventoryTransferWHtoWH(IjInventoryOnDFDoc objIjInventoryOnDFDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        Vector listOfInventory = objIjInventoryOnDFDoc.getListInventory();
        if (listOfInventory != null && listOfInventory.size() > 0) {
            PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
            int inventoryCount = listOfInventory.size();
            for (int iVal = 0; iVal < inventoryCount; iVal++) {
                IjInventoryDoc objIjInventoryDoc = (IjInventoryDoc) listOfInventory.get(iVal);

                // -- start debet --
                // Ambil Transaction Location Mapping ?nventory Location?
                IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, objIjInventoryDoc.getInvDestLocation(), objIjInventoryDoc.getInvProdDepartment());
                long locationAccChartDebet = objIjLocationMappingDebet.getAccount();

                // pembuatan jurnal detail
                if (locationAccChartDebet != 0) {
                    IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
                    objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);
                    objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
                    objIjJournalDetailDebet.setJdetTransRate(1);
                    objIjJournalDetailDebet.setJdetDebet(objIjInventoryDoc.getInvValue());
                    objIjJournalDetailDebet.setJdetCredit(0);
                    vResult.add(objIjJournalDetailDebet);
                }

                // -- start kredit --
                // Ambil Transaction Location Mapping ?nventory Location?
                IjLocationMapping objIjLocationMappingKredit = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, objIjInventoryDoc.getInvSourceLocation(), objIjInventoryDoc.getInvProdDepartment());
                long locationAccChartKredit = objIjLocationMappingKredit.getAccount();

                // pembuatan jurnal detail
                if (locationAccChartKredit != 0) {
                    IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
                    objIjJournalDetailKredit.setJdetAccChart(locationAccChartKredit);
                    objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());
                    objIjJournalDetailKredit.setJdetTransRate(1);
                    objIjJournalDetailKredit.setJdetDebet(0);
                    objIjJournalDetailKredit.setJdetCredit(objIjInventoryDoc.getInvValue());
                    vResult.add(objIjJournalDetailKredit);
                }
            }
        }

        return vResult;
    }
    // ------------------- Finish Transfer WH to WH ------------------

    // ------------------- Start Transfer WH to Production ------------------

    /**
     * Generate list of Inventory On DF journal based on selected document on selected Bo system
     *
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     */
    public int generateDFToProductionJournal(Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        Vector vLocationOid = objIjEngineParam.getVLocationOid();
        if (vLocationOid != null && vLocationOid.size() > 0) {
            int iLocationCount = vLocationOid.size();
            for (int i = 0; i < iLocationCount; i++) {
                long lLocationOid = Long.parseLong(String.valueOf(vLocationOid.get(i)));
                result = result + generateDFToProductionJournal(lLocationOid, dSelectedDate, iDocTypeReference, objIjEngineParam);
            }
        }

        return result;
    }

    /**
     * Generate list of Inventory On DF to Production journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *                                       where transaction occur
     * @param <CODE>dSelectedDate</CODE>Date of transaction selected by user
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                                       type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                                       object
     * @return
     * @created by Edhy
     * @algoritm 1. get list of Inventory On DF to Production Document from selected BO system
     * 2. iterate as long as document count to generate InventoryOnDFJournal
     */
    public int generateDFToProductionJournal(long lLocationOid, Date dSelectedDate, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        int result = 0;

        // 1. Ambil data DP (refer to IJ-Interface getListPurchaseOnLGR)
        Vector vectOfDFToWhDoc = new Vector(1, 1);
        try {
            // --- start get list of Inventory On DF Document from selected BO system ---
            String strIjImplBo = objIjEngineParam.getSIjImplBo();
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(strIjImplBo).newInstance();
            vectOfDFToWhDoc = i_bosys.getListInventoryOnDF(lLocationOid, dSelectedDate, I_IJGeneral.DF_TYPE_PRODUCTION);
            // --- end get list of Inventory On DF Document from selected BO system ---

            // --- start iterate as long as document count to generate InventoryOnDFJournal ---
            if (vectOfDFToWhDoc != null && vectOfDFToWhDoc.size() > 0) {
                int maxDFToWhDoc = vectOfDFToWhDoc.size();
                for (int i = 0; i < maxDFToWhDoc; i++) {
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = (IjInventoryOnDFDoc) vectOfDFToWhDoc.get(i);
                    long lResult = genJournalOfObjDFToProduction(lLocationOid, objIjInventoryOnDFDoc, iDocTypeReference, objIjEngineParam);
                    if (lResult != 0) {
                        result++;
                    }
                }
            } else {
                System.out.println(".::MSG : Because no document found, journaling Inventory On DF process skip ... ");
            }
            // --- end iterate as long as document count to generate InventoryOnDFJournal ---
        }
        catch (Exception e) {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }

        return result;
    }


    /**
     * Generate list of Inventory On DF On Production journal based on selected document on selected Bo system
     *
     * @param <CODE>lLocationOid</CODE>location
     *         where transaction occur
     * @param <CODE>objIjInventoryOnDFDoc</CODE>IjDPOnPdODoc
     *         object as source of journal process
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *         type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     * @algoritm :
     * 1. Pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen Inventory DF (object IjInventoryOnDFDoc).
     * 2. Pembuatan vector of jurnal detail posisi debet (destination loc) dan kredit (source location).
     * 3. Generate Journal dengan :
     * 3.1 Debet  : "Inventory on Destination Location"
     * 3.2 Kredit : "Inventory on Source Location"
     */
    public long genJournalOfObjDFToProduction(long lLocationOid, IjInventoryOnDFDoc objIjInventoryOnDFDoc, int iDocTypeReference, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // 1. pembuatan jurnal umum (object IjJournalMain) dengan mengambil dokumen transfer barang ke lokasi produksi (object objIjInventoryOnDFDoc).
        IjJournalMain objIjJournalMain = genObjIjJournalMain(objIjInventoryOnDFDoc, DF_TO_PROD, iDocTypeReference, lLocationOid, objIjEngineParam);

        // 2. pembuatan jurnal detail posisi kredit (object IjJournalDetail) dengan mengambil data payment.
        Vector vectOfObjIjJurDetail = genObjIjJDOfInventoryTransferWHtoProduction(objIjInventoryOnDFDoc, objIjEngineParam);

        // 3. save jurnal ke database IJ
        if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
            if (PstIjJournalDetail.isBalanceDebetAndCredit(vectOfObjIjJurDetail)) {
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
     * Membuat detail utk posisi debet dan kredit dengan mengambil Location Mapping
     *
     * @param <CODE>objIjInventoryOnDFDoc</CODE>IjDPOnPdODoc
     *         object as source of journal process
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return
     * @created by Edhy
     */
    private Vector genObjIjJDOfInventoryTransferWHtoProduction(IjInventoryOnDFDoc objIjInventoryOnDFDoc, IjEngineParam objIjEngineParam) {
        Vector vResult = new Vector(1, 1);

        Vector listOfInventory = objIjInventoryOnDFDoc.getListInventory();
        if (listOfInventory != null && listOfInventory.size() > 0) {
            PstIjLocationMapping objPstIjLocationMapping = new PstIjLocationMapping();
            int inventoryCount = listOfInventory.size();
            for (int iVal = 0; iVal < inventoryCount; iVal++) {
                IjInventoryDoc objIjInventoryDoc = (IjInventoryDoc) listOfInventory.get(iVal);

                // -- start debet --
                // Ambil Transaction Location Mapping ?IP Location?
                IjLocationMapping objIjLocationMappingDebet = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_WIP, -1, 0, objIjInventoryDoc.getInvDestLocation(), objIjInventoryDoc.getInvProdDepartment());
                long locationAccChartDebet = objIjLocationMappingDebet.getAccount();

                // pembuatan jurnal detail
                if (locationAccChartDebet != 0) {
                    IjJournalDetail objIjJournalDetailDebet = new IjJournalDetail();
                    objIjJournalDetailDebet.setJdetAccChart(locationAccChartDebet);
                    objIjJournalDetailDebet.setJdetTransCurrency(objIjEngineParam.getLBookType());
                    objIjJournalDetailDebet.setJdetTransRate(1);
                    objIjJournalDetailDebet.setJdetDebet(objIjInventoryDoc.getInvValue());
                    objIjJournalDetailDebet.setJdetCredit(0);
                    vResult.add(objIjJournalDetailDebet);
                }

                // -- start kredit --
                // Ambil Transaction Location Mapping ?nventory Location?
                IjLocationMapping objIjLocationMappingKredit = objPstIjLocationMapping.getObjIjLocationMapping(I_IJGeneral.TRANS_INVENTORY_LOCATION, -1, 0, objIjInventoryDoc.getInvSourceLocation(), objIjInventoryDoc.getInvProdDepartment());
                long locationAccChartKredit = objIjLocationMappingKredit.getAccount();

                // pembuatan jurnal detail
                if (locationAccChartKredit != 0) {
                    IjJournalDetail objIjJournalDetailKredit = new IjJournalDetail();
                    objIjJournalDetailKredit.setJdetAccChart(locationAccChartKredit);
                    objIjJournalDetailKredit.setJdetTransCurrency(objIjEngineParam.getLBookType());
                    objIjJournalDetailKredit.setJdetTransRate(1);
                    objIjJournalDetailKredit.setJdetDebet(0);
                    objIjJournalDetailKredit.setJdetCredit(objIjInventoryDoc.getInvValue());
                    vResult.add(objIjJournalDetailKredit);
                }
            }
        }

        return vResult;
    }
    // ------------------- Finish Transfer WH to Production ------------------


    /**
     * @param <CODE>objIjInventoryOnDFDoc</CODE>IjDPOnPdODoc
     *                               object as source of journal process
     * @param <CODE>iDFType</CODE>DF Type
     * @param <CODE>iDocTypeReference</CODE>Dococument
     *                               type of selected document that will linking it to generated journal
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *                               object
     * @return
     */
    private IjJournalMain genObjIjJournalMain(IjInventoryOnDFDoc objIjInventoryOnDFDoc, int iDFType, int iDocTypeReference, long lLocationOid, IjEngineParam objIjEngineParam) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        objIjJournalMain.setJurTransDate(objIjInventoryOnDFDoc.getDocTransDate());
        objIjJournalMain.setJurBookType(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurTransCurrency(objIjEngineParam.getLBookType());
        objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
        objIjJournalMain.setJurDesc(objIjInventoryOnDFDoc.getDesc());//strJournalNote[objIjEngineParam.getILanguage()][iDFType]);
        objIjJournalMain.setRefBoSystem(objIjEngineParam.getIBoSystem());
        objIjJournalMain.setRefBoDocType(iDocTypeReference);
        objIjJournalMain.setRefBoDocOid(objIjInventoryOnDFDoc.getDocId());
        objIjJournalMain.setRefBoDocNumber(objIjInventoryOnDFDoc.getDocNumber());
        objIjJournalMain.setRefBoLocation(lLocationOid);
        objIjJournalMain.setRefBoDocLastUpdate(objIjInventoryOnDFDoc.getDtLastUpdate());

        return objIjJournalMain;
    }

}
