/*
 * SessJournal.java
 *
 * Created on February 23, 2005, 8:44 AM
 */

package com.dimata.ij.session.engine;

// import core java package

import java.util.Vector;
import java.util.Date;
import java.sql.*;

// import dimata package
import com.dimata.util.*;

// system package
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.qdep.entity.I_IJGeneral;

// import ij package
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.ij.iaiso.*;
import com.dimata.ij.ibosys.*;
import com.dimata.ij.entity.configuration.*;
import com.dimata.ij.entity.mapping.*;
import com.dimata.ij.entity.search.*;
import com.dimata.ij.form.search.*;


/**
 * @author gedhy
 */
public class SessJournal {

    /**
     * generate IJ Journal with interactive process that required user trigger
     *
     * @param <CODE>objIjEngineParam</CODE>IjEngineParameter
     *         object
     * @return Number of journal created by IJ process
     * @created by Edhy
     * @algoritm 1. get configuration data from "IJ Configuration" module, store in variable
     * 2. get standart rate data from "common" store in "hashtable" object
     * 3. journal process to IJ database defend on list of task defined :
     * 3.1 Purchasing Journal
     * 3.2 Production Journal
     * 3.3 Sales Journal
     * 3.4 Cliring Journal
     */
    public int generateJournal(IjEngineParam objIjEngineParam) {
        int result = 0;

        // get parameter
        Date dStartTransactionDate = objIjEngineParam.getDStartTransactionDate();
        Date dFinishTransactionDate = objIjEngineParam.getDFinishTransactionDate();
        int iBoSystem = objIjEngineParam.getIBoSystem();

        // --- start get configuration data ---
        PstIjConfiguration objPstIjConfiguration = new PstIjConfiguration();
        IjConfiguration objIjConfiguration = new IjConfiguration();

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(iBoSystem, objPstIjConfiguration.CFG_GRP_PAY, objPstIjConfiguration.CFG_PAY_DP_PI);
        objIjEngineParam.setIConfPayment(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(iBoSystem, objPstIjConfiguration.CFG_GRP_INV, objPstIjConfiguration.CFG_GRP_INV_STORE);
        objIjEngineParam.setIConfInventory(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(iBoSystem, objPstIjConfiguration.CFG_GRP_TAX, objPstIjConfiguration.CFG_GRP_TAX_SALES);
        objIjEngineParam.setIConfTaxOnSales(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(iBoSystem, objPstIjConfiguration.CFG_GRP_TAX, objPstIjConfiguration.CFG_GRP_TAX_BUY);
        objIjEngineParam.setIConfTaxOnBuy(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(iBoSystem, objPstIjConfiguration.CFG_GRP_SYS, objPstIjConfiguration.CFG_GRP_SYS_IJ_ENG);
        objIjEngineParam.setIConfJournalSystem(objIjConfiguration.getConfigSelect());
        // --- finish get configuration data ---

        // --- start get standart rate data ---
        PstStandartRate objPstStandartRate = new PstStandartRate();
        objIjEngineParam.setHStandartRate(objPstStandartRate.getStandartRate());
        // --- finish get standart rate data ---

        // --- start journal process to IJ database defend on chart of task defined ---
        Date dTransDate = new Date();
        int maxIterate = ((int) DateCalc.dayDifference(dStartTransactionDate, dFinishTransactionDate)) + 1;
        if (maxIterate > 0) {
            // instantiate object that handle journal process
            SessDPOnPO objSessDPOnPO = new SessDPOnPO();
            SessPurchOnLGR objSessPurchOnLGR = new SessPurchOnLGR();
            SessPaymentOnLGR objSessPaymentOnLGR = new SessPaymentOnLGR();

            SessDPOnPdO objSessDPOnPdO = new SessDPOnPdO();
            SessInventoryOnDF objSessInventoryOnDF = new SessInventoryOnDF();
            SessProdCostOnLGR objSessProdCostOnLGR = new SessProdCostOnLGR();

            SessDPOnSO objSessDPOnSO = new SessDPOnSO();
            SessDPOnRegistration objSessDPOnRegistration = new SessDPOnRegistration();
            SessSalesOnInv objSessSalesOnInv = new SessSalesOnInv();
            SessPaymentOnInv objSessPaymentOnInv = new SessPaymentOnInv();

            SessPaymentType objSessPaymentType = new SessPaymentType();

            SessDFToDept objSessDFToDept = new SessDFToDept();
            SessLGRFromDept objSessLGRFromDept = new SessLGRFromDept();
            SessDFCosting objSessDFCosting = new SessDFCosting();

            SessCustReturn objSessCustReturn = new SessCustReturn();
            SessCustReturnDeduct objSessCustReturnDeduct = new SessCustReturnDeduct();

            SessSuppReturn objSessSuppReturn = new SessSuppReturn();
            SessSuppReturnDeduct objSessSuppReturnDeduct = new SessSuppReturnDeduct();

            SessCancellation objSessCancellation = new SessCancellation();
            SessAdjustment objSessAdjustment = new SessAdjustment();
            SessAcqOfCommision objSessAcqCommision = new SessAcqOfCommision();
            SessPaymentOnCommision objSessPaymentOnCommision = new SessPaymentOnCommision();

            for (int i = 0; i < maxIterate; i++) {
                // generate date of process
                dTransDate = new Date(dStartTransactionDate.getYear(), dStartTransactionDate.getMonth(), (dStartTransactionDate.getDate() + i));

                // --- Start Purchasing Journal ---
                // ### 1. Start DP on Purchase Order Transaction ### tidak di pakai
                // result = result + objSessDPOnPO.generateDPOnPOJournal(dTransDate, I_IJGeneral.DOC_TYPE_DP_ON_PURCHASE_ORDER, objIjEngineParam);
                // ### 1. Finish DP on Purchase Order Transaction ###

                // ### 2. Start Purchase on LGR Transaction ###
                result = result + objSessPurchOnLGR.generateLGRJournal(dTransDate, I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR, objIjEngineParam);
                // ### 2. Finish Purchase on LGR Transaction ###

                // ### 3. Start Payment on LGR Transaction ### tidak dipakai
                //result = result + objSessPaymentOnLGR.generatePaymentOnLGRJournal(dTransDate, I_IJGeneral.DOC_TYPE_PAYMENT_ON_LGR, objIjEngineParam);
                // ### 3. Finish Payment on LGR Transaction ###
                // --- Finish Purchasing Journal ---

                // --- Start Production Journal ---
                // ### 4. Start DP on Production Order Transaction ### tidak di pakai
                // result = result + objSessDPOnPdO.generateDPOnPdOJournal(dTransDate, I_IJGeneral.DOC_TYPE_DP_ON_PRODUCTION_ORDER, objIjEngineParam);
                // ### 4. Finish DP on Production Order Transaction ###

                // ### 5. Start Inventory on DF Transaction ###   --
                result = result + objSessInventoryOnDF.generateDFToWhJournal(dTransDate, I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, objIjEngineParam);
                //result = result + objSessInventoryOnDF.generateDFToProductionJournal(dTransDate, I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, objIjEngineParam);
                // ### 5. Finish Inventory on DF Transaction ###

                //####### for check data return dari toko ke warehouse
                result = result + objSessInventoryOnDF.generateReturnInventoryJournal(dTransDate, I_IJGeneral.DOC_TYPE_DF_TO_DEPT, objIjEngineParam);

                // ### 6. Start Production Cost on LGR Transaction ###  --  tidak di pakai
                //result = result + objSessProdCostOnLGR.generateProdCostOnLGRJournal(dTransDate, I_IJGeneral.DOC_TYPE_PROD_COST_ON_LGR, objIjEngineParam);
                // ### 6. Finish Production Cost on LGR Transaction ###
                // --- Finish Production Journal ---

                // --- Start Sales Journal ---
                // ### 7. Start DP on Sales Order Transaction ###
                result = result + objSessDPOnSO.generateDPOnSOJournal(dTransDate, I_IJGeneral.DOC_TYPE_DP_ON_SALES_ORDER, objIjEngineParam);
                // ### 7. Finish DP on Sales Order Transaction ###

                // ### 8. Start DP on Registration Transaction ### tidak di pakai
                //result = result + objSessDPOnRegistration.generateDPOnRegistrationJournal(dTransDate, I_IJGeneral.DOC_TYPE_DP_ON_SALES_ORDER, objIjEngineParam);
                // ### 8. Finish DP on Registration Transaction ###

                // ### 9. Start Sales on Invoice Transaction ###
                result = result + objSessSalesOnInv.generateCreditSalesJournal(dTransDate, I_IJGeneral.DOC_TYPE_SALES_ON_INV, objIjEngineParam);
                result = result + objSessSalesOnInv.generateCashSalesJournal(dTransDate, I_IJGeneral.DOC_TYPE_SALES_ON_INV, objIjEngineParam);
                // ### 9. Finish Sales on Invoice Transaction ###

                // ### 10. Start Payment on Invoice Transaction ###
                result = result + objSessPaymentOnInv.generatePaymentOnInvoiceJournal(dTransDate, I_IJGeneral.DOC_TYPE_PAYMENT_ON_INV, objIjEngineParam);
                // ### 10. Finish Payment on Invoice Transaction ###
                // --- Finish Sales Journal ---

                // --- Start Cliring Journal ---
                // ### 11. Start Payment Type Transaction ### tidak dipakai
               //result = result + objSessPaymentType.generateCliringJournal(dTransDate, I_IJGeneral.DOC_TYPE_PAYMENT_TYPE_POSTED_CLEARED, objIjEngineParam);
                // ### 11. Finish Payment Type Transaction ###
                // --- Finish Cliring Journal ---

                // --- Start Inventory Transfer Journal ---
                // ### 12. Start DFToDept Transaction ###
               //result = result + objSessDFToDept.generateDFToDeptJournal(dTransDate, I_IJGeneral.DOC_TYPE_DF_TO_DEPT, objIjEngineParam);
                // ### 12. Finish DFToDept Transaction ###
                // ### 13. Start LGRFromDept Transaction ###
               // result = result + objSessLGRFromDept.generateLGRFromDeptJournal(dTransDate, I_IJGeneral.DOC_TYPE_LGR_FROM_DEPT, objIjEngineParam);
                // ### 13. Finish LGRFromDept Transaction ###
                // ### 14. Start DFCosting Transaction ###
                result = result + objSessDFCosting.generateDFCostingJournal(dTransDate, I_IJGeneral.DOC_TYPE_DF_FOR_COSTING, objIjEngineParam);
                // ### 14. Finish DFCosting Transaction ###
                // --- Finish Inventory Transfer Journal ---

                // --- Start Customer Return Journal ---
                // ### 15. Start CustReturn Transaction ###
                result = result + objSessCustReturn.generateCustReturnJournal(dTransDate, I_IJGeneral.DOC_TYPE_CUST_RETURN, objIjEngineParam);
                // ### 15. Finish CustReturn Transaction ###
                // ### 16. Start CustReturnDeduct Transaction ###
               //result = result + objSessCustReturnDeduct.generateCustReturnDeductJournal(dTransDate, I_IJGeneral.DOC_TYPE_CUST_RETURN_DEDUCTION, objIjEngineParam);
                // ### 16. Finish CustReturnDeduct Transaction ###
                // --- Finish Customer Return Journal ---

                // --- Start Supplier Return Journal ---
                // ### 17. Start SuppReturn Transaction ###
                result = result + objSessSuppReturn.generateSuppReturnJournal(dTransDate, I_IJGeneral.DOC_TYPE_SUPP_RETURN, objIjEngineParam);
                // ### 17. Finish SuppReturn Transaction ###
                // ### 18. Start SuppReturnDeduct Transaction ###
               //result = result + objSessSuppReturnDeduct.generateSuppReturnDeductJournal(dTransDate, I_IJGeneral.DOC_TYPE_SUPP_RETURN_DEDUCTION, objIjEngineParam);
                // ### 18. Finish SuppReturnDeduct Transaction ###
                // --- Finish Supplier Return Journal ---

                // --- Start Hanoman Journal ---
                // ### 19. Start Cancellation Transaction ###
              //result = result + objSessCancellation.generateCancellationJournal(dTransDate, I_IJGeneral.DOC_TYPE_CANCELLATION, objIjEngineParam);
                // ### 19. Finish Cancellation Transaction ###
                // ### 20. Start Adjustment Transaction ###
              /// result = result + objSessAdjustment.generateAdjustmentJournal(dTransDate, I_IJGeneral.DOC_TYPE_ADJUSTMENT, objIjEngineParam);
                // ### 20. Finish Adjustment Transaction ###
                // ### 21. Start Acq Commision Transaction ###
               //result = result + objSessAcqCommision.generateCommisionJournal(dTransDate, I_IJGeneral.DOC_TYPE_ACQ_OF_COMMISION, objIjEngineParam);
                // ### 21. Finish Acq Commision Transaction ###
                // ### 22. Start PaymentonCommision Transaction ###
               //result = result + objSessPaymentOnCommision.generatePaymentOnCommisionJournal(dTransDate, I_IJGeneral.DOC_TYPE_PAYMENT_ON_COMMISION, objIjEngineParam);
                // ### 22. Finish PaymentonCommision Transaction ###
                // --- Finish Hanoman Journal ---

            }
        }
        // --- finish journal process to IJ database defend on chart of task defined ---

        return result;
    }


    /**
     * generate Ij Journal of last version of object on BO system
     *
     * @param objIjJournalMain
     */
    public long generateIjJournalImmediately(IjJournalMain objIjJournalMain, IjEngineParam objIjEngineParam) {
        long lResult = 0;

        // --- start get configuration data ---
        PstIjConfiguration objPstIjConfiguration = new PstIjConfiguration();
        IjConfiguration objIjConfiguration = new IjConfiguration();

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(objIjJournalMain.getRefBoSystem(), 0, 0);
        String strIjImplBoClassName = objIjConfiguration.getSIjImplClass();

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(objIjJournalMain.getRefBoSystem(), objPstIjConfiguration.CFG_GRP_PAY, objPstIjConfiguration.CFG_PAY_DP_PI);
        objIjEngineParam.setIConfPayment(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(objIjJournalMain.getRefBoSystem(), objPstIjConfiguration.CFG_GRP_INV, objPstIjConfiguration.CFG_GRP_INV_STORE);
        objIjEngineParam.setIConfInventory(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(objIjJournalMain.getRefBoSystem(), objPstIjConfiguration.CFG_GRP_TAX, objPstIjConfiguration.CFG_GRP_TAX_SALES);
        objIjEngineParam.setIConfTaxOnSales(objIjConfiguration.getConfigSelect());

        objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(objIjJournalMain.getRefBoSystem(), objPstIjConfiguration.CFG_GRP_TAX, objPstIjConfiguration.CFG_GRP_TAX_BUY);
        objIjEngineParam.setIConfTaxOnBuy(objIjConfiguration.getConfigSelect());
        objIjEngineParam.setIConfJournalSystem(I_IJGeneral.CFG_GRP_SYS_IJ_ENG_FULL_AUTO);
        // --- finish get configuration data ---

        // --- start get standart rate data ---
        PstStandartRate objPstStandartRate = new PstStandartRate();
        objIjEngineParam.setHStandartRate(objPstStandartRate.getStandartRate());
        // --- finish get standart rate data ---


        int iRefBoDocType = objIjJournalMain.getRefBoDocType();
        long lRefBoDocOid = objIjJournalMain.getRefBoDocOid();
        long lLocationOid = objIjJournalMain.getRefBoLocation();
        switch (iRefBoDocType) {
            // 1. generate IJJOurnal of "DP on Purchase Order" dengan object "objIjDPOnPODoc"
            case I_IJGeneral.DOC_TYPE_DP_ON_PURCHASE_ORDER :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjDPOnPODoc objIjDPOnPODoc = i_bosystem.getDPonPurchaseOrderDoc(lRefBoDocOid);

                    IjCurrencyMapping objCurrencyMapping = PstIjCurrencyMapping.getObjIjCurrencyMapping(objIjJournalMain.getRefBoSystem(), objIjDPOnPODoc.getLTransCurrency());
                    objIjDPOnPODoc.setLTransCurrency(objCurrencyMapping.getAisoCurrency());

                    SessDPOnPO objSessDPOnPO = new SessDPOnPO();
                    lResult = objSessDPOnPO.genJournalOfObjDPOnPO(lLocationOid, objIjDPOnPODoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - DPonPurchaseOrder document exc : " + e.toString());
                }
                break;

                // 2. generate IJJOurnal of "Purchase On LGR" dengan object "objIjPurchaseOnLGRDoc"
            case I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjPurchaseOnLGRDoc objIjPurchaseOnLGRDoc = i_bosystem.getPurchaseOnLGRDoc(lRefBoDocOid);

                    SessPurchOnLGR objSessPurchOnLGR = new SessPurchOnLGR();
                    lResult = objSessPurchOnLGR.genJournalOfObjPurchOnLGR(lLocationOid, objIjPurchaseOnLGRDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - PurchaseOnLGR document exc : " + e.toString());
                }
                break;

                // 3. generate IJJOurnal of "Payment On LGR" dengan object "objIjPaymentOnLGRDoc"
            case I_IJGeneral.DOC_TYPE_PAYMENT_ON_LGR :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjPaymentOnLGRDoc objIjPaymentOnLGRDoc = i_bosystem.getPaymentOnLGRDoc(lRefBoDocOid);

                    SessPaymentOnLGR objSessPaymentOnLGR = new SessPaymentOnLGR();
                    lResult = objSessPaymentOnLGR.genJournalOfObjPaymentOnLGR(lLocationOid, objIjPaymentOnLGRDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - PaymentOnLGR document exc : " + e.toString());
                }
                break;

                // 4. generate IJJOurnal of "DP on Production Order" dengan object "objIjDPOnPdODoc"
            case I_IJGeneral.DOC_TYPE_DP_ON_PRODUCTION_ORDER :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjDPOnPdODoc objIjDPOnPdODoc = i_bosystem.getDPonProductionOrderDoc(lRefBoDocOid);

                    SessDPOnPdO objSessDPOnPdO = new SessDPOnPdO();
                    lResult = objSessDPOnPdO.genJournalOfObjDPOnPdO(lLocationOid, objIjDPOnPdODoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - DPonProductionOrder document exc : " + e.toString());
                }
                break;

                // 5. generate IJJOurnal of "Inventory On DF" dengan object "objIjInventoryOnDFDoc"
            case I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = i_bosystem.getInventoryOnDFDoc(lRefBoDocOid);

                    SessInventoryOnDF objSessInventoryOnDF = new SessInventoryOnDF();
                    lResult = objSessInventoryOnDF.genJournalOfObjDFToWH(lLocationOid, objIjInventoryOnDFDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - InventoryOnDF document exc : " + e.toString());
                }
                break;

                // 6. generate IJJOurnal of "Production Cost On LGR" dengan object "objIjProdCostOnLGRDoc"
            case I_IJGeneral.DOC_TYPE_PROD_COST_ON_LGR :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjProdCostOnLGRDoc objIjProdCostOnLGRDoc = i_bosystem.getProductionCostOnLGRDoc(lRefBoDocOid);

                    SessProdCostOnLGR objSessProdCostOnLGR = new SessProdCostOnLGR();
                    lResult = objSessProdCostOnLGR.genJournalOfObjProdCostOnLGR(lLocationOid, objIjProdCostOnLGRDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - ProductionCostOnLGR document exc : " + e.toString());
                }
                break;

                // 7. generate IJJOurnal of "DP on Sales Order" dengan object "objIjDPOnSODoc"
            case I_IJGeneral.DOC_TYPE_DP_ON_SALES_ORDER :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjDPOnSODoc objIjDPOnSODoc = i_bosystem.getDPonSalesOrderDoc(lRefBoDocOid);

                    SessDPOnSO objSessDPOnSO = new SessDPOnSO();
                    lResult = objSessDPOnSO.genJournalOfObjDPOnSO(lLocationOid, objIjDPOnSODoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - DPonSalesOrder document exc : " + e.toString());
                }
                break;

                // 8. generate IJJOurnal of "Sales on Invoice" dengan object "objIjSalesOnInvDoc"
            case I_IJGeneral.DOC_TYPE_SALES_ON_INV :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjSalesOnInvDoc objIjSalesOnInvDoc = i_bosystem.getSalesOnInvoiceDoc(lRefBoDocOid);

                    SessSalesOnInv objSessSalesOnInv = new SessSalesOnInv();
                    lResult = objSessSalesOnInv.genJournalOfObjCreditSales(lLocationOid, objIjSalesOnInvDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - SalesOnInvoice document exc : " + e.toString());
                }
                break;

                // 9. generate IJJOurnal of "Payment On Invoice" dengan object "objIjPaymentOnInvDoc"
            case I_IJGeneral.DOC_TYPE_PAYMENT_ON_INV :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjPaymentOnInvDoc objIjPaymentOnInvDoc = i_bosystem.getPaymentOnInvoiceDoc(lRefBoDocOid);

                    SessPaymentOnInv objSessPaymentOnInv = new SessPaymentOnInv();
                    lResult = objSessPaymentOnInv.genJournalOfObjPaymentOnInvoice(lLocationOid, objIjPaymentOnInvDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - PaymentOnInv document exc : " + e.toString());
                }
                break;

                // 10. generate IJJOurnal of "Payment Type" dengan object "objIjPaymentTypeDoc"
            case I_IJGeneral.DOC_TYPE_PAYMENT_TYPE_POSTED_CLEARED :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjPaymentTypeDoc objIjPaymentTypeDoc = i_bosystem.getPaymentTypeDoc(lRefBoDocOid);

                    SessPaymentType objSessPaymentType = new SessPaymentType();
                    lResult = objSessPaymentType.genJournalOfObjPaymentType(lLocationOid, objIjPaymentTypeDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - PaymentType document exc : " + e.toString());
                }
                break;

                // 11. generate IJJournal of "DFToDept" dengan object "objIjInventoryOnDFDoc"
            case I_IJGeneral.DOC_TYPE_DF_TO_DEPT :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = i_bosystem.getDFToDepartmentDoc(lRefBoDocOid);

                    SessDFToDept objSessDFToDept = new SessDFToDept();
                    lResult = objSessDFToDept.genJournalOfObjDFToDept(lLocationOid, objIjInventoryOnDFDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - InventoryOnDF document exc : " + e.toString());
                }
                break;

                // 12. generate IJJournal of "LGRFromDept" dengan object "objIjInventoryOnDFDoc"
            case I_IJGeneral.DOC_TYPE_LGR_FROM_DEPT :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = i_bosystem.getLGRFromDepartmentDoc(lRefBoDocOid);

                    SessLGRFromDept objSessLGRFromDept = new SessLGRFromDept();
                    lResult = objSessLGRFromDept.genJournalOfObjLGRFromDept(lLocationOid, objIjInventoryOnDFDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - InventoryOnDF document exc : " + e.toString());
                }
                break;

                // 13. generate IJJournal of "DFCosting" dengan object "objIjInventoryOnDFDoc"
            case I_IJGeneral.DOC_TYPE_DF_FOR_COSTING :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjInventoryOnDFDoc objIjInventoryOnDFDoc = i_bosystem.getInvOnDFCostingDoc(lRefBoDocOid);

                    SessDFCosting objSessDFCosting = new SessDFCosting();
                    lResult = objSessDFCosting.genJournalOfObjDFCosting(lLocationOid, objIjInventoryOnDFDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - InventoryOnDF document exc : " + e.toString());
                }
                break;

                // 14. generate IJJournal of "CustReturn" dengan object "objIjCustReturnDoc"
            case I_IJGeneral.DOC_TYPE_CUST_RETURN :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjCustReturnDoc objIjCustReturnDoc = i_bosystem.getCustReturnDoc(lRefBoDocOid);

                    SessCustReturn objSessCustReturn = new SessCustReturn();
                    lResult = objSessCustReturn.genJournalOfObjCustReturn(lLocationOid, objIjCustReturnDoc, iRefBoDocType, objIjEngineParam);
                    lResult = objSessCustReturn.genJournalOfObjLgrOnCustReturn(lLocationOid, objIjCustReturnDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - CustReturn document exc : " + e.toString());
                }
                break;

                // 15. generate IJJournal of "CustReturnDeduct" dengan object "objIjCustReturnDeductDoc"
            case I_IJGeneral.DOC_TYPE_CUST_RETURN_DEDUCTION :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjCustReturnDeductDoc objIjCustReturnDeductDoc = i_bosystem.getCustReturnDeductDoc(lRefBoDocOid);

                    SessCustReturnDeduct objSessCustReturnDeduct = new SessCustReturnDeduct();
                    lResult = objSessCustReturnDeduct.genJournalOfObjCustReturnDeduct(lLocationOid, objIjCustReturnDeductDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - CustReturnDeduct document exc : " + e.toString());
                }
                break;

                // 16. generate IJJournal of "SuppReturn" dengan object "objIjSuppReturnDoc"
            case I_IJGeneral.DOC_TYPE_SUPP_RETURN :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjSuppReturnDoc objIjSuppReturnDoc = i_bosystem.getSuppReturnDoc(lRefBoDocOid);

                    SessSuppReturn objSessSuppReturn = new SessSuppReturn();
                    lResult = objSessSuppReturn.genJournalOfObjSuppReturn(lLocationOid, objIjSuppReturnDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - SuppReturn document exc : " + e.toString());
                }
                break;

                // 17. generate IJJournal of "SuppReturn" dengan object "objIjSuppReturnDoc"
            case I_IJGeneral.DOC_TYPE_SUPP_RETURN_DEDUCTION :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjSuppReturnDeductDoc objIjSuppReturnDeductDoc = i_bosystem.getSuppReturnDeductDoc(lRefBoDocOid);

                    SessSuppReturnDeduct objSessSuppReturnDeduct = new SessSuppReturnDeduct();
                    lResult = objSessSuppReturnDeduct.genJournalOfObjSuppReturnDeduct(lLocationOid, objIjSuppReturnDeductDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - SuppReturnDeduct document exc : " + e.toString());
                }
                break;

                // 18. generate IJJournal of "Cancellation" dengan object "objIjCancellationDoc"
            case I_IJGeneral.DOC_TYPE_CANCELLATION :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjCancellationDoc objIjCancellationDoc = i_bosystem.getCancellationDoc(lRefBoDocOid);

                    SessCancellation objSessCancellation = new SessCancellation();
                    lResult = objSessCancellation.genJournalOfObjCancellation(lLocationOid, objIjCancellationDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - Cancellation document exc : " + e.toString());
                }
                break;

                // 19. generate IJJournal of "Adjustment" dengan object "objIjAdjustmentDoc"
            case I_IJGeneral.DOC_TYPE_ADJUSTMENT :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjAdjustmentDoc objIjAdjustmentDoc = i_bosystem.getAdjustmentDoc(lRefBoDocOid);

                    SessAdjustment objSessAdjustment = new SessAdjustment();
                    lResult = objSessAdjustment.genJournalOfObjAdjustment(lLocationOid, objIjAdjustmentDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - Adjustment document exc : " + e.toString());
                }
                break;

                // 20. generate IJJournal of "Acq of Commision" dengan object "objIjCommisionDoc"
            case I_IJGeneral.DOC_TYPE_ACQ_OF_COMMISION :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjCommisionDoc objIjCommisionDoc = i_bosystem.getCommisionDoc(lRefBoDocOid);

                    SessAcqOfCommision objSessAcqOfCommision = new SessAcqOfCommision();
                    lResult = objSessAcqOfCommision.genJournalOfObjCommision(lLocationOid, objIjCommisionDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - AcqCommision document exc : " + e.toString());
                }
                break;

                // 21. generate IJJournal of "Payment of Commision" dengan object "objIjPaymentDoc"
            case I_IJGeneral.DOC_TYPE_PAYMENT_ON_COMMISION :
                try {
                    I_BOSystem i_bosystem = (I_BOSystem) Class.forName(strIjImplBoClassName).newInstance();
                    IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc = i_bosystem.getPaymentOnCommisionDoc(lRefBoDocOid);

                    SessPaymentOnCommision objSessPaymentOnCommision = new SessPaymentOnCommision();
                    lResult = objSessPaymentOnCommision.genJournalOfObjPaymentOnCommision(lLocationOid, objIjPaymentOnCommisionDoc, iRefBoDocType, objIjEngineParam);
                }
                catch (Exception e) {
                    System.out.println(new SessJournal().getClass().getName() + ".generateIjJournalImmediately() - PaymentCommision document exc : " + e.toString());
                }
                break;

            default :
                break;
        }

        return lResult;
    }


    /**
     * @param objSrcIjJournal
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector getJournal(SrcIjJournal objSrcIjJournal, int start, int recordToGet) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        // proses pencarian daftar jurnal sesuai dengan parameter pencarian
        try {
            String sql = "SELECT MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_BOOK_TYPE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_NOTE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_PERIOD] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_ENTRY_DATE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_USER] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_TYPE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_AISO_JOURNAL_OID] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_SYSTEM] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_LAST_UPDATE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_SUB_BO_DOC_OID] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_TRANSACTION_TYPE] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CONTACT_ID] +
                    ", MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_LOCATION] +
                    " FROM " + PstIjJournalMain.TBL_IJ_JOURNAL_MAIN + " AS MAIN ";

            String whereBillNumber = "";
            if (objSrcIjJournal.getBillNumber() != null && objSrcIjJournal.getBillNumber().length() > 0) {
                whereBillNumber = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER] + " LIKE '%"+objSrcIjJournal.getBillNumber()+"%'";
                /*sql = sql + " INNER JOIN " + PstIjJournalDetail.TBL_IJ_JOURNAL_DETAIL + " AS DETAIL " +
                        " ON MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] +
                        " = DETAIL." + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID];*/
            }


            String strTransDate = "";
            if (objSrcIjJournal.getSelectedTransDate() == FrmSrcIjJournal.SELECTED_USER_DATE) {
                if (objSrcIjJournal.getTransStartDate() != null && objSrcIjJournal.getTransEndDate() != null) {
                    String strStartDate = Formater.formatDate(objSrcIjJournal.getTransStartDate(), "yyyy-MM-dd 00:00:00");
                    String strEndDate = Formater.formatDate(objSrcIjJournal.getTransEndDate(), "yyyy-MM-dd 23:59:59");
                    strTransDate = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE] +
                            " BETWEEN '" + strStartDate + "' AND '" + strEndDate + "'";
                }
            }

            String strCurrency = "";
            if (objSrcIjJournal.getTransCurrency() != 0) {
                strCurrency = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID] +
                        " = " + objSrcIjJournal.getTransCurrency();
            }

            String strJournalStatus = "";
            if (objSrcIjJournal.getJournalStatus() != -1) {
                strJournalStatus = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS] +
                        " = " + objSrcIjJournal.getJournalStatus();
            }

            String strSortBy = "";
            switch (objSrcIjJournal.getSortBy()) {
                case FrmSrcIjJournal.SORT_BY_TRANS_DATE     :
                    strSortBy = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE];
                    break;

                case FrmSrcIjJournal.SORT_BY_BILL_NUMBER     :
                    strSortBy = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER];
                    break;

                case FrmSrcIjJournal.SORT_BY_CURRENCY       :
                    strSortBy = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID];
                    break;

                case FrmSrcIjJournal.SORT_BY_JOURNAL_STATUS :
                    strSortBy = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS];
                    break;
            }


            String allCondition = "";
            if (strTransDate != null && strTransDate.length() > 0) {
                allCondition = strTransDate;
            }

            if (strCurrency != null && strCurrency.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + strCurrency;
                } else {
                    allCondition = strCurrency;
                }
            }

            if (whereBillNumber != null && whereBillNumber.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + whereBillNumber;
                } else {
                    allCondition = whereBillNumber;
                }
            }

            if (strJournalStatus != null && strJournalStatus.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + strJournalStatus;
                } else {
                    allCondition = strJournalStatus;
                }
            }

            if (allCondition != null && allCondition.length() > 0) {
                sql = sql + " WHERE  " + allCondition;
            }

            if (strSortBy != null && strSortBy.length() > 0) {
                sql = sql + " ORDER BY  " + strSortBy;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL :
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

                    break;

                case DBHandler.DBSVR_SYBASE :
                    break;

                case DBHandler.DBSVR_ORACLE :
                    break;

                case DBHandler.DBSVR_MSSQL :
                    break;

                default:
                    break;
            }


            System.out.println(new SessJournal().getClass().getName() + ".getJournal() - sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjJournalMain objIjJournalMain = new IjJournalMain();

                objIjJournalMain.setOID(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID]));
                objIjJournalMain.setJurTransDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE]));
                objIjJournalMain.setJurBookType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_BOOK_TYPE]));
                objIjJournalMain.setJurTransCurrency(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID]));
                objIjJournalMain.setJurStatus(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS]));
                objIjJournalMain.setJurDesc(rs.getString(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_NOTE]));
                objIjJournalMain.setJurPeriod(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_PERIOD]));
                objIjJournalMain.setJurEntryDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_ENTRY_DATE]));
                objIjJournalMain.setJurUser(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_USER]));
                objIjJournalMain.setRefBoDocType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_TYPE]));
                objIjJournalMain.setRefBoDocOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID]));
                objIjJournalMain.setRefBoDocNumber(rs.getString(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER]));
                objIjJournalMain.setRefAisoJournalOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_AISO_JOURNAL_OID]));
                objIjJournalMain.setRefBoSystem(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_SYSTEM]));
                objIjJournalMain.setRefBoTransacTionType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_TRANSACTION_TYPE]));
                objIjJournalMain.setRefSubBoDocOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_SUB_BO_DOC_OID]));
                objIjJournalMain.setContactOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CONTACT_ID]));
                objIjJournalMain.setRefBoLocation(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_LOCATION]));

                Date dtDate = DBHandler.convertDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_LAST_UPDATE]),rs.getTime(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_LAST_UPDATE]));
                objIjJournalMain.setRefBoDocLastUpdate(dtDate);

                String whDetail = PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID] + "=" + objIjJournalMain.getOID();
                String ordDetail = PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_DEBT_VALUE];
                Vector listJournalDetail = PstIjJournalDetail.list(0, 0, whDetail, ordDetail);
                objIjJournalMain.setListOfDetails(listJournalDetail);

                result.add(objIjJournalMain);
            }
        }
        catch (Exception e) {
            System.out.println(new SessJournal().getClass().getName() + ".getJournal() - exc : " + e.toString());
        }
        return result;
    }


    /**
     * @param objSrcIjJournal
     * @param start
     * @param recordToGet
     * @return
     */
    public static int getCountJournal(SrcIjJournal objSrcIjJournal) {
        int result = 0;
        DBResultSet dbrs = null;

        // proses pencarian daftar jurnal sesuai dengan parameter pencarian
        try {
            String sql = "SELECT COUNT(MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] + ")" +
                    " FROM " + PstIjJournalMain.TBL_IJ_JOURNAL_MAIN + " AS MAIN ";

            String whereBillNumber = "";
            if (objSrcIjJournal.getBillNumber() != null && objSrcIjJournal.getBillNumber().length() > 0) {
                whereBillNumber = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER] + " LIKE '%"+objSrcIjJournal.getBillNumber()+"'";
                /*sql = sql + " INNER JOIN " + PstIjJournalDetail.TBL_IJ_JOURNAL_DETAIL + " AS DETAIL " +
                        " ON MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] +
                        " = DETAIL." + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID];*/
            }


            String strTransDate = "";
            if (objSrcIjJournal.getSelectedTransDate() == FrmSrcIjJournal.SELECTED_USER_DATE) {
                if (objSrcIjJournal.getTransStartDate() != null && objSrcIjJournal.getTransEndDate() != null) {
                    String strStartDate = Formater.formatDate(objSrcIjJournal.getTransStartDate(), "yyyy-MM-dd 00:00:00");
                    String strEndDate = Formater.formatDate(objSrcIjJournal.getTransEndDate(), "yyyy-MM-dd 23:59:59");
                    strTransDate = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE] +
                            " BETWEEN '" + strStartDate + "' AND '" + strEndDate + "'";
                }
            }

            String strCurrency = "";
            if (objSrcIjJournal.getTransCurrency() != 0) {
                strCurrency = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID] +
                        " = " + objSrcIjJournal.getTransCurrency();
            }

            String strJournalStatus = "";
            if (objSrcIjJournal.getJournalStatus() != -1) {
                strJournalStatus = " MAIN." + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS] +
                        " = " + objSrcIjJournal.getJournalStatus();
            }


            String allCondition = "";
            if (strTransDate != null && strTransDate.length() > 0) {
                allCondition = strTransDate;
            }

            if (strCurrency != null && strCurrency.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + strCurrency;
                } else {
                    allCondition = strCurrency;
                }
            }

            if (whereBillNumber != null && whereBillNumber.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + whereBillNumber;
                } else {
                    allCondition = whereBillNumber;
                }
            }

            if (strJournalStatus != null && strJournalStatus.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + strJournalStatus;
                } else {
                    allCondition = strJournalStatus;
                }
            }

            if (allCondition != null && allCondition.length() > 0) {
                sql = sql + " WHERE  " + allCondition;
            }

            System.out.println(new SessJournal().getClass().getName() + ".getJournal() - sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(new SessJournal().getClass().getName() + ".getJournal() - exc : " + e.toString());
        }

        return result;
    }


    /**
     * this method used to get list all journal main object base on specified parameter
     *
     * @param objSrcIjJournal
     * @return
     * @created by Edhy
     */
    public static Vector getListJournalMain(SrcIjJournal objSrcIjJournal) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        // proses pencarian daftar jurnal sesuai dengan parameter pencarian
        try {
            String sql = "SELECT " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_BOOK_TYPE] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_NOTE] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_PERIOD] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_ENTRY_DATE] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_USER] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_TYPE] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_AISO_JOURNAL_OID] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_SYSTEM] +
                    ", " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_LAST_UPDATE] +
                    " FROM " + PstIjJournalMain.TBL_IJ_JOURNAL_MAIN;

            String strTransDate = "";
            if (objSrcIjJournal.getSelectedTransDate() == FrmSrcIjJournal.SELECTED_USER_DATE) {
                if (objSrcIjJournal.getTransStartDate() != null && objSrcIjJournal.getTransEndDate() != null) {
                    String strStartDate = Formater.formatDate(objSrcIjJournal.getTransStartDate(), "yyyy-MM-dd");
                    String strEndDate = Formater.formatDate(objSrcIjJournal.getTransEndDate(), "yyyy-MM-dd");
                    strTransDate = PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE] +
                            " BETWEEN '" + strStartDate + "' AND '" + strEndDate + "'";
                }
            }

            String strCurrency = "";
            if (objSrcIjJournal.getTransCurrency() != 0) {
                strCurrency = PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID] +
                        " = " + objSrcIjJournal.getTransCurrency();
            }

            String strJournalStatus = "";
            if (objSrcIjJournal.getJournalStatus() != -1) {
                strJournalStatus = PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS] +
                        " = " + objSrcIjJournal.getJournalStatus();
            }


            String allCondition = "";
            if (strTransDate != null && strTransDate.length() > 0) {
                allCondition = strTransDate;
            }

            if (strCurrency != null && strCurrency.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + strCurrency;
                } else {
                    allCondition = strCurrency;
                }
            }

            if (strJournalStatus != null && strJournalStatus.length() > 0) {
                if (allCondition != null && allCondition.length() > 0) {
                    allCondition = allCondition + " AND " + strJournalStatus;
                } else {
                    allCondition = strJournalStatus;
                }
            }

            if (allCondition != null && allCondition.length() > 0) {
                sql = sql + " WHERE  " + allCondition;
            }

            System.out.println(new SessJournal().getClass().getName() + ".getListJournalMain() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjJournalMain objIjJournalMain = new IjJournalMain();

                objIjJournalMain.setOID(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID]));
                objIjJournalMain.setJurTransDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE]));
                objIjJournalMain.setJurBookType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_BOOK_TYPE]));
                objIjJournalMain.setJurTransCurrency(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID]));
                objIjJournalMain.setJurStatus(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS]));
                objIjJournalMain.setJurDesc(rs.getString(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_NOTE]));
                objIjJournalMain.setJurPeriod(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_PERIOD]));
                objIjJournalMain.setJurEntryDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_ENTRY_DATE]));
                objIjJournalMain.setJurUser(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_USER]));
                objIjJournalMain.setRefBoDocType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_TYPE]));
                objIjJournalMain.setRefBoDocOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID]));
                objIjJournalMain.setRefBoDocNumber(rs.getString(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER]));
                objIjJournalMain.setRefAisoJournalOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_AISO_JOURNAL_OID]));
                objIjJournalMain.setRefBoSystem(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_SYSTEM]));
                objIjJournalMain.setRefBoDocLastUpdate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_LAST_UPDATE]));

                result.add(objIjJournalMain);
            }
        }
        catch (Exception e) {
            System.out.println(new SessJournal().getClass().getName() + ".getListJournalMain() exc : " + e.toString());
        }

        return result;
    }


    /**
     * this method used to list all journal object (main, detail n its sub ledger) base on specified parameter
     *
     * @param objSrcIjJournal
     * @return
     * @created by Edhy
     */
    public static Vector getListJournalObj(SrcIjJournal objSrcIjJournal) {
        Vector vResult = new Vector(1, 1);

        // get list journal main
        Vector vListJournalMain = getListJournalMain(objSrcIjJournal);
        if (vListJournalMain != null && vListJournalMain.size() > 0) {
            int iListJournalMainCount = vListJournalMain.size();
            PstIjJournalMain objPstIjJournalMain = new PstIjJournalMain();
            for (int i = 0; i < iListJournalMainCount; i++) {
                IjJournalMain objIjJournalMain = (IjJournalMain) vListJournalMain.get(i);

                // get journal business object
                IjJournalMain objIjJournal = objPstIjJournalMain.getIjJournalMain(objIjJournalMain.getOID());
                vResult.add(objIjJournal);
            }
        }

        return vResult;
    }

}
