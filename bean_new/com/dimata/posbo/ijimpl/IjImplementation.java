/*
 * IjImplementation.java
 *
 * Created on January 12, 2005, 9:12 AM
 */


package com.dimata.posbo.ijimpl;

import com.dimata.ij.ibosys.*;
import com.dimata.common.entity.payment.*;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.qdep.entity.I_IJGeneral;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.payment.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;

import java.util.Vector;
import java.util.Date;
import java.util.Hashtable;
import java.sql.ResultSet;


/**
 * @author gedhy
 */
public class IjImplementation implements I_BOSystem {

    // --------------- Start Master Data  ---------------

    /**
     * this method used to get list Currency Type used in BO System
     * return 'vector of obj com.dimata.common.entity.payment.CurrencyType'
     */
    public Vector getListCurrencyType() {
        Vector listCurrencyType = new Vector(1, 1);

        // process getListCurrencyType
        String strOrder = PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX];
        Vector vctCurrencyType = PstCurrencyType.list(0, 0, "", strOrder);
        if (vctCurrencyType != null && vctCurrencyType.size() > 0) {
            int maxCurrencyType = vctCurrencyType.size();
            for (int i = 0; i < maxCurrencyType; i++) {
                CurrencyType objCurrencyType = (CurrencyType) vctCurrencyType.get(i);
                listCurrencyType.add(objCurrencyType);
            }
        }

        return listCurrencyType;
    }


    /**
     * this method used to get list Standart Rate used in BO System
     * return 'vector of obj com.dimata.common.entity.payment.StandartRate'
     */
    public Vector getListStandartRate() {
        Vector listStandartRate = new Vector(1, 1);

        // process getListStandartRate
        String strOrder = PstStandartRate.fieldNames[PstStandartRate.FLD_START_DATE];
        Vector vctStandartRate = PstStandartRate.list(0, 0, "", strOrder);
        if (vctStandartRate != null && vctStandartRate.size() > 0) {
            int maxStandartRate = vctStandartRate.size();
            for (int i = 0; i < maxStandartRate; i++) {
                StandartRate objStandartRate = (StandartRate) vctStandartRate.get(i);
                listStandartRate.add(objStandartRate);
            }
        }

        return listStandartRate;
    }


    public long getOidRupiahStandartRate() {
        long oidRupiah = 0;
        String where = PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE] + "=1";
        Vector vctStandartRate = PstStandartRate.list(0, 0, where, "");
        if (vctStandartRate != null && vctStandartRate.size() > 0) {
            for (int i = 0; i < vctStandartRate.size(); i++) {
                StandartRate objStandartRate = (StandartRate) vctStandartRate.get(i);
                oidRupiah = objStandartRate.getCurrencyTypeId();
                break;
            }
        }
        return oidRupiah;
    }


    /**
     * this method used to get list Location used in BO System
     * return 'vector of obj com.dimata.common.entity.location.Location'
     */
    public Vector getListLocation() {
        //return new Vector(1,1);
        Vector listLocation = new Vector(1, 1);

        // process getListLocation
        String strOrder = PstLocation.fieldNames[PstLocation.FLD_CODE];
        Vector vctLocation = PstLocation.list(0, 0, "", strOrder);
        if (vctLocation != null && vctLocation.size() > 0) {
            int maxLocation = vctLocation.size();
            for (int i = 0; i < maxLocation; i++) {
                Location objLocation = (Location) vctLocation.get(i);
                listLocation.add(objLocation);
            }
        }

        return listLocation;
    }


    /**
     * this method used to get list Product Department
     * return 'vector of obj com.dimata.prochain.entity.masterdata.ProductDepartment'
     */
    public Vector getListProductDepartment() {
        Vector list = new Vector(1, 1);
        Vector listCateg = PstCategory.list(0, 0, "", "");
        IjProdDeptData objProdDeptData = null;
        if (listCateg.size() > 0) {
            for (int k = 0; k < listCateg.size(); k++) {
                Category categ = (Category) listCateg.get(k);
                objProdDeptData = new IjProdDeptData();
                objProdDeptData.setPdCode(categ.getCode());
                objProdDeptData.setPdName(categ.getName());
                objProdDeptData.setPdId(categ.getOID());
                list.add(objProdDeptData);
            }
        }
        return list;
    }


    /**
     * this method used to get list Sale Type
     * look reference constant index & name in 'com.dimata.qdep.entity.I_IJGeneral'
     * return 'vector of obj com.dimata.ij.ibosys.IjSaleTypeData'
     */
    public Vector getListSaleType() {
        Vector listSaleType = new Vector(1, 1);

        IjSaleTypeData objSaleTypeData = new IjSaleTypeData();
        objSaleTypeData.setStIdx(I_IJGeneral.SALE_TYPE_REGULAR);
        objSaleTypeData.setStName(I_IJGeneral.fieldSaleType[I_IJGeneral.SALE_TYPE_REGULAR]);
        listSaleType.add(objSaleTypeData);

        objSaleTypeData = new IjSaleTypeData();
        objSaleTypeData.setStIdx(I_IJGeneral.SALE_TYPE_CONSIGNMENT);
        objSaleTypeData.setStName(I_IJGeneral.fieldSaleType[I_IJGeneral.SALE_TYPE_CONSIGNMENT]);
        listSaleType.add(objSaleTypeData);

        objSaleTypeData = new IjSaleTypeData();
        objSaleTypeData.setStIdx(I_IJGeneral.SALE_TYPE_PERSONAL);
        objSaleTypeData.setStName(I_IJGeneral.fieldSaleType[I_IJGeneral.SALE_TYPE_PERSONAL]);
        listSaleType.add(objSaleTypeData);

        return listSaleType;
    }


    /**
     * this method used to get list Payment System
     * return 'vector of obj com.dimata.ij.ibosys.IjPaymentSystemData'
     */
    public Vector getListPaymentSystem() {
        Vector listPaymentSystem = new Vector(1, 1);

        // process getListPaymentSystem
        String strOrder = PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM];
        Vector vctPaymentSystem = PstPaymentSystem.list(0, 0, "", strOrder);
        if (vctPaymentSystem != null && vctPaymentSystem.size() > 0) {
            int maxPaymentSystem = vctPaymentSystem.size();
            for (int i = 0; i < maxPaymentSystem; i++) {
                PaymentSystem objPaymentSystem = (PaymentSystem) vctPaymentSystem.get(i);
                listPaymentSystem.add(objPaymentSystem);
            }
        }

        return listPaymentSystem;
    }
    // --------------- End Master Data  ---------------


    public Hashtable getAllPaymentSystem() {
        Hashtable hash = new Hashtable();
        String strOrder = PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM];
        Vector vctPaymentSystem = PstPaymentSystem.list(0, 0, "", strOrder);
        if (vctPaymentSystem != null && vctPaymentSystem.size() > 0) {
            for (int i = 0; i < vctPaymentSystem.size(); i++) {
                PaymentSystem objPaymentSystem = (PaymentSystem) vctPaymentSystem.get(i);
                hash.put(objPaymentSystem.getPaymentSystem().toUpperCase(), "" + objPaymentSystem.getOID());
            }
        }
        return hash;
    }

    public long getLongOidPaymentSystem(Hashtable has, String str) {
        long oid = 0;
        try {
            oid = Long.parseLong((String) has.get(str));
        } catch (Exception e) {
        }
        return oid;
    }

    // ***** --------- Start implement I_BOSystem
    // --------------- Start Sales Order ---------------

    /**
     * this method used to get DP on Sales Order document defend on 'objDPOnSODocOid' selected
     * * return 'obj com.dimata.ij.ibosys.DPOnSODoc'
     */
    public IjDPOnSODoc getDPonSalesOrderDoc(long objDPOnSODocOid) {
        IjDPOnSODoc objDPOnSODoc = new IjDPOnSODoc();

        try {
            PendingOrder objPendingOrder = PstPendingOrder.fetchExc(objDPOnSODocOid);
            objDPOnSODoc.setLDocOid(objPendingOrder.getOID());
            objDPOnSODoc.setSNumber(objPendingOrder.getPoNumber());
            objDPOnSODoc.setISaleType(I_IJGeneral.TRANS_SALES); // tipe penjualan : retail
            objDPOnSODoc.setLProdDepartment(0L); // product department : no prod department

            // pencarian lokasi transaction
            CashMaster cashMaster = new CashMaster();
            try {
                CashCashier cashCashier = new CashCashier();
                cashCashier = PstCashCashier.fetchExc(objPendingOrder.getCashierId());
                cashMaster = PstCashMaster.fetchExc(cashCashier.getCashMasterId());
            } catch (Exception e) {
            }
            objDPOnSODoc.setLLocation(cashMaster.getLocationId()); // lokasi : Head Office
            //objDPOnSODoc.setLContact(objPendingOrder.getMemberId()); // Contact : Mr X
            objDPOnSODoc.setLTransCurrency(objPendingOrder.getCurrencyId()); // currency : USD
            objDPOnSODoc.setDtTransDate(objPendingOrder.getCreationDate()); // tanggal transaksi : 15 Nov 2005
            objDPOnSODoc.setDtDueDate(objPendingOrder.getExpiredDate()); // tanggal jatuh tempo : 30 Nov 2005
            objDPOnSODoc.setDtLastUpdate(objPendingOrder.getCreationDate());
        } catch (Exception e) {
            System.out.println(new IjImplementation().getClass().getName() + ".getDPonSalesOrderDoc() - fetchExc exc : " + e.toString());
        }

        return objDPOnSODoc;
    }

    public int updateStatusDPonSalesOrder(IjDPOnSODoc objDPOnSODoc, int iDocStatus) {
        int hasil = 0;
        try {
            PendingOrder objPendingOrder = PstPendingOrder.fetchExc(objDPOnSODoc.getLDocOid());
            objPendingOrder.setStatusPosted(iDocStatus);
            PstPendingOrder.updateExc(objPendingOrder);
        } catch (Exception e) {
            hasil = 1;
        }
        return hasil;
    }

    public Vector getListDPonRegistration(long lLocationOid, Date transactionDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IjDPOnSODoc getDPonRegistrationDoc(long objDPOnRegistrationDocOid) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int updateStatusDPonRegistration(IjDPOnRegistrationDoc objDPOnRegistrationDoc, int iDocStatus) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * this method used to get DP on Purchase Order document defend on 'objDPOnPODocOid' selected
     * return 'obj com.dimata.ij.ibosys.DPOnPODoc'
     */
    public IjDPOnPODoc getDPonPurchaseOrderDoc(long objDPOnPODocOid) {
        IjDPOnPODoc objDPOnPODoc = new IjDPOnPODoc();

        // process getDPonPurchaseOrderDoc

        return objDPOnPODoc;
    }

    public int updateStatusDPonPurchaseOrder(IjDPOnPODoc objDPOnPODoc, int iDocStatus) {
        return 0;
    }

    public Vector getListPurchaseOnLGR(long lLocationOid, Date transactionDate) {
        Vector listall = new Vector();
        try {
            String where = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    " between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " and " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " and " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + "=" + lLocationOid +
                    " and (" + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + "=" + PstMatReceive.SOURCE_FROM_SUPPLIER +
                    " or " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + "=" + PstMatReceive.SOURCE_FROM_SUPPLIER_PO + ")";

            Vector list = PstMatReceive.list(0, 0, where, "");
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    MatReceive matReceive = (MatReceive) list.get(k);
                    IjPurchaseOnLGRDoc ijPurchaseOnLGRDoc = new IjPurchaseOnLGRDoc();
                    ijPurchaseOnLGRDoc.setDocId(matReceive.getOID());
                    ijPurchaseOnLGRDoc.setDocLocation(matReceive.getLocationId());
                    ijPurchaseOnLGRDoc.setDocNumber(matReceive.getRecCode());
                    ijPurchaseOnLGRDoc.setDocSaleType(I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR);
                    ijPurchaseOnLGRDoc.setDocTransDate(matReceive.getReceiveDate());
                    ijPurchaseOnLGRDoc.setDocContact(matReceive.getSupplierId());
                    ijPurchaseOnLGRDoc.setDtLastUpdate(matReceive.getLastUpdate());
                    try {
                        PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(matReceive.getPurchaseOrderId());
                        ijPurchaseOnLGRDoc.setRefPONumber(purchaseOrder.getPoCode());
                    } catch (Exception e) {
                    }
                    ijPurchaseOnLGRDoc.setDocContact(matReceive.getSupplierId());
                    ijPurchaseOnLGRDoc.setDocTransCurrency(getOidRupiahStandartRate()); //504404261456952182L);

                    // pencarian atau set value dari lgr
                    Vector data = new Vector();
                    where = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
                    double total = PstMatReceiveItem.getTotal(where);
                    System.out.println("======= >>>>>>>>>>>>>>>>>>>> : total dari item penerimaan " + total);
                    IjPurchaseValue ijPurchaseValue = new IjPurchaseValue();
                    ijPurchaseValue.setPurchValue(total);

                    data.add(ijPurchaseValue);
                    ijPurchaseOnLGRDoc.setListPurchaseValue(data);

                    listall.add(ijPurchaseOnLGRDoc);
                }
            }
        } catch (Exception e) {
            System.out.println("eroor getListPurchaseOnLGR : " + e.toString());
        }
        return listall;
    }

    /**
     * this method used to get DP on Production Order document defend on 'objDPOnPdODocOid' selected
     * return 'obj com.dimata.ij.ibosys.DPOnPdODoc'
     */
    public IjDPOnPdODoc getDPonProductionOrderDoc(long objDPOnPdODocOid) {
        IjDPOnPdODoc objDPOnPdODoc = new IjDPOnPdODoc();
        objDPOnPdODoc.setLDocOid(objDPOnPdODocOid);
        return objDPOnPdODoc;
    }

    /**
     * gadnyana
     * proses update return dari toko ke gudang
     * karena sebelumya proses terlupakan jadi
     * fungsi digunakan.
     */
    public int updateStatusDPonProductionOrder(IjDPOnPdODoc objDPOnPdODoc, int iDocStatus) {
        try {
            MatReturn matReturn = PstMatReturn.fetchExc(objDPOnPdODoc.getLDocOid());
            matReturn.setReturnStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
            PstMatReturn.updateExc(matReturn);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * this method used to get Purchase on LGR document defend on 'objPurchaseOnLGRDocOid' selected
     * return 'obj com.dimata.ij.ibosys.PurchaseOnLGRDoc'
     */
    public IjPurchaseOnLGRDoc getPurchaseOnLGRDoc(long objPurchaseOnLGRDocOid) {
        IjPurchaseOnLGRDoc objPurchaseOnLGRDoc = new IjPurchaseOnLGRDoc();
        try {
            MatReceive matReceive = PstMatReceive.fetchExc(objPurchaseOnLGRDocOid);

            objPurchaseOnLGRDoc.setDocId(matReceive.getOID());
            objPurchaseOnLGRDoc.setDocLocation(matReceive.getLocationId());
            objPurchaseOnLGRDoc.setDocNumber(matReceive.getRecCode());
            objPurchaseOnLGRDoc.setDocTransDate(matReceive.getReceiveDate());
            objPurchaseOnLGRDoc.setDtLastUpdate(matReceive.getLastUpdate());
            objPurchaseOnLGRDoc.setDocTransCurrency(getOidRupiahStandartRate()); // 504404261456952182L);
            objPurchaseOnLGRDoc.setDocSaleType(I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR);
            System.out.println("matReceive.getLastUpdate() : " + matReceive.getLastUpdate());

            // pencarian referensi dokumen po
            PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(matReceive.getPurchaseOrderId());
            objPurchaseOnLGRDoc.setRefPONumber(purchaseOrder.getPoCode());
            objPurchaseOnLGRDoc.setDocTransDate(purchaseOrder.getPurchDate());
            objPurchaseOnLGRDoc.setDocContact(purchaseOrder.getSupplierId());

            // pencarian atau set value dari lgr
            Vector data = new Vector();
            String where = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
            double total = PstMatReceiveItem.getTotal(where);
            IjPurchaseValue ijPurchaseValue = new IjPurchaseValue();
            ijPurchaseValue.setPurchValue(total);
            data.add(ijPurchaseValue);
            objPurchaseOnLGRDoc.setListPurchaseValue(data);

        } catch (Exception e) {
            System.out.println("IjPurchaseOnLGRDoc getPurchaseOnLGRDoc > IjImplementation : " + e.toString());
        }
        return objPurchaseOnLGRDoc;
    }

    /**
     * gadnyana
     * update receive status
     *
     * @param objPurchaseOnLGRDoc
     * @param iDocStatus
     * @return
     */
    public int updateStatusPurchaseOnLGR(IjPurchaseOnLGRDoc objPurchaseOnLGRDoc, int iDocStatus) {
        try {
            MatReceive matReceive = PstMatReceive.fetchExc(objPurchaseOnLGRDoc.getDocId());
            matReceive.setReceiveStatus(iDocStatus);
            PstMatReceive.updateExc(matReceive);
        } catch (Exception e) {
            System.out.println("Err updateStatusPurchaseOnLGR : " + e.toString());
            return 1;
        }
        return 0;
    }

    public Vector getListPaymentOnLGR(long lLocationOid, Date transactionDate) {
        // tidak ada di pos
        return null;
    }


    /**
     * this method used to get Inventory On DF document defend on 'objInventoryOnDFDocOid' selected
     * return 'obj com.dimata.ij.ibosys.ProdCostOnLGRDoc'
     */
    public IjInventoryOnDFDoc getInventoryOnDFDoc(long objInventoryOnDFDocOid) {
        IjInventoryOnDFDoc objIjInventoryOnDFDoc = new IjInventoryOnDFDoc();
        try {
            MatDispatch matDispatch = PstMatDispatch.fetchExc(objInventoryOnDFDocOid);
            objIjInventoryOnDFDoc.setDocId(matDispatch.getOID());
            objIjInventoryOnDFDoc.setDocNumber(matDispatch.getDispatchCode());
            objIjInventoryOnDFDoc.setDocTransDate(matDispatch.getDispatchDate());
            objIjInventoryOnDFDoc.setDtLastUpdate(matDispatch.getLast_update());

            IjInventoryDoc ijInventoryDoc = new IjInventoryDoc();
            ijInventoryDoc.setInvDestLocation(matDispatch.getDispatchTo());
            ijInventoryDoc.setInvSourceLocation(matDispatch.getLocationId());
            double total = PstMatDispatchItem.getTotalTransfer(objInventoryOnDFDocOid);
            Vector lists = new Vector();
            ijInventoryDoc.setInvValue(total);
            lists.add(ijInventoryDoc);
            objIjInventoryOnDFDoc.setListInventory(lists);

        } catch (Exception e) {
            System.out.println("IjInventoryOnDFDoc getInventoryOnDFDoc : " + e.toString());
        }
        return objIjInventoryOnDFDoc;
    }

    /**
     * this method used to get Production Cost On LGR document defend on 'objProdCostOnLGRDocOid' selected
     * return 'obj com.dimata.ij.ibosys.ProdCostOnLGRDoc'
     */
    public IjProdCostOnLGRDoc getProductionCostOnLGRDoc(long objProdCostOnLGRDocOid) {
        IjProdCostOnLGRDoc objIjProdCostOnLGRDoc = new IjProdCostOnLGRDoc();

        // Process getProductionCostOnLGRDoc

        return objIjProdCostOnLGRDoc;
    }

    /**
     * this method used to get Sales On Invoice document defend on 'objSalesOnInvDocOid' selected\
     * return 'obj com.dimata.ij.ibosys.SalesOnInvDoc'
     * ini hanya di gunakan penjualan kredit
     */
    public IjSalesOnInvDoc getSalesOnInvoiceDoc(long objSalesOnInvDocOid) {
        IjSalesOnInvDoc objIjSalesOnInvDoc = new IjSalesOnInvDoc();
        try {
            BillMain billMain = PstBillMain.fetchExc(objSalesOnInvDocOid);
            objIjSalesOnInvDoc.setDocId(objSalesOnInvDocOid);
            objIjSalesOnInvDoc.setDocNumber(billMain.getInvoiceNumber());
            objIjSalesOnInvDoc.setDocSaleType(billMain.getTransType());
            objIjSalesOnInvDoc.setDocLocation(billMain.getLocationId());
            objIjSalesOnInvDoc.setDocDiscount(billMain.getDiscount());
            objIjSalesOnInvDoc.setDocTax(billMain.getTaxValue());
            objIjSalesOnInvDoc.setDocTransDate(billMain.getBillDate());
            objIjSalesOnInvDoc.setRefSONumber(billMain.getRefInvoiceNum());
        } catch (Exception e) {
            System.out.println("Err getSalesOnInvoiceDoc : " + e.toString());
        }
        return objIjSalesOnInvDoc;
    }

    public IjSalesOnInvDoc getSalesOnInvoiceDoc(long docId, long lLocationOid, Date transactionDate, int salesType, int transactionType) {
        DBResultSet dbrs = null;
        IjSalesOnInvDoc iJSalesOnInvDoc = new IjSalesOnInvDoc();
        try {
            String sql = "select m.cash_cashier_id, min(m.invoice_number) as invoice, " +
                    " max(m.location_id) as loc_id, sum(m.disc) as tot_disc, " +
                    " sum(m.tax_value) as tax_val, sum(m.service_value) as service_val,min(m.bill_date) as billdate, sum(d.total_price) as totalprice" +
                    " from cash_bill_main as m" +
                    " inner join cash_bill_detail as d on m.cash_bill_main_id = d.cash_bill_main_id" +
                    " where m.trans_type = " + salesType + " and m.transaction_type = " + transactionType +
                    " and m.location_id = " + lLocationOid +
                    " and m.cash_cashier_id = " + docId +
                    " and m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " group by m.cash_cashier_id";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector list = new Vector();
            while (rs.next()) {
                iJSalesOnInvDoc.setDocId(rs.getLong("cash_cashier_id"));
                iJSalesOnInvDoc.setDocNumber(rs.getString("invoice"));
                iJSalesOnInvDoc.setDocSaleType(salesType);
                iJSalesOnInvDoc.setDocLocation(lLocationOid);
                iJSalesOnInvDoc.setDocDiscount(rs.getDouble("tot_disc"));
                iJSalesOnInvDoc.setDocTax(rs.getDouble("tax_val"));
                iJSalesOnInvDoc.setDocOtherCost(rs.getDouble("service_val"));
                iJSalesOnInvDoc.setDocContact(0);
                Date dates = DBHandler.convertDate(rs.getDate("billdate"), rs.getTime("billdate"));
                iJSalesOnInvDoc.setDocTransDate(dates);
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return iJSalesOnInvDoc;

    }

    /**
     * gadnyana
     * proses update ini hanya/khusus di gunakan oleh penjualan kas
     *
     * @param objSalesOnInvDoc
     * @param iDocStatus
     * @return
     */
    public int updateStatusSalesOnInvoice(IjSalesOnInvDoc objSalesOnInvDoc, int iDocStatus) {
        try {
            String sql = "update cash_bill_main as m set m.bill_status = " + iDocStatus +
                    " where m.transaction_type = " + PstBillMain.TRANS_TYPE_CASH +
                    " and m.transaction_status = " + PstBillMain.TRANS_STATUS_CLOSE +
                    " and m.location_id = " + objSalesOnInvDoc.getDocLocation() +
                    " and m.shift_id = " + objSalesOnInvDoc.getDocId() +
                    " and m.bill_date between '" + Formater.formatDate(objSalesOnInvDoc.getDocTransDate(), "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(objSalesOnInvDoc.getDocTransDate(), "yyyy-MM-dd 23:59:59") + "'";

            System.out.println("update penjualan cash ==>>>> : " + sql);
            DBHandler.execUpdate(sql);
            sql = "update cash_payment set payment_status = " + iDocStatus +
                    " where cash_bill_main_id in ( " +
                    " select cash_bill_main_id from cash_bill_main " +
                    " where transaction_type = " + PstBillMain.TRANS_TYPE_CASH +
                    " and transaction_status = " + PstBillMain.TRANS_STATUS_CLOSE +
                    " and location_id = " + objSalesOnInvDoc.getDocLocation() +
                    " and shift_id = " + objSalesOnInvDoc.getDocId() +
                    " and bill_date between '" + Formater.formatDate(objSalesOnInvDoc.getDocTransDate(), "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(objSalesOnInvDoc.getDocTransDate(), "yyyy-MM-dd 23:59:59") + "')";

            System.out.println("update payment penjualan cash ==>>>> : " + sql);
            DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("Error update main dan detail penjualan cash : " + e.toString());
        }

        return 0;
    }

    public int updateStatusSalesCreditOnInvoice(IjSalesOnInvDoc objSalesOnInvDoc, int iDocStatus) {
        try {
            BillMain billMain = PstBillMain.fetchExc(objSalesOnInvDoc.getDocId());
            billMain.setBillStatus(iDocStatus);
            PstBillMain.updateExc(billMain);

            // proses update dp
            if (billMain.getCashPendingOrderId() != 0) {
                PendingOrder pOrder = PstPendingOrder.fetchExc(billMain.getCashPendingOrderId());
                pOrder.setPendingOrderStatus(iDocStatus);
                PstPendingOrder.updateExc(pOrder);
            }
        } catch (Exception e) {
            System.out.println("Error update main dan detail penjualan cash : " + e.toString());
        }
        return 0;
    }


    /**
     * this method used to get Payment On LGR document defend on 'objPaymentOnLGRDocOid' selected
     * return 'obj com.dimata.ij.ibosys.PaymentOnLGRDoc'
     */
    public IjPaymentOnLGRDoc getPaymentOnLGRDoc(long objPaymentOnLGRDocOid) {
        IjPaymentOnLGRDoc objIjPaymentOnLGRDoc = new IjPaymentOnLGRDoc();

        // Process getPaymentOnLGRDoc

        return objIjPaymentOnLGRDoc;
    }

    public int updateStatusPaymentOnLGR(IjPaymentOnLGRDoc objPaymentOnLGRDoc, int iDocStatus) {
        // tidak ada di pos
        return 0;
    }


    /**
     * this method used to get Payment On Invoice document defend on 'objPaymentOnInvDocOid' selected
     * return 'obj com.dimata.ij.ibosys.PaymentOnInvDoc'
     */
    public IjPaymentOnInvDoc getPaymentOnInvoiceDoc(long objPaymentOnInvDocOid) {
        IjPaymentOnInvDoc ijPaymentDoc = new IjPaymentOnInvDoc();
        try {
            CreditPaymentMain creditPaymentMain = PstCreditPaymentMain.fetchExc(objPaymentOnInvDocOid);
            ijPaymentDoc.setDocId(creditPaymentMain.getOID());
            ijPaymentDoc.setDocNumber(creditPaymentMain.getInvoiceNumber());
            ijPaymentDoc.setDocTransDate(creditPaymentMain.getBillDate());
            ijPaymentDoc.setDtLastUpdate(creditPaymentMain.getBillDate());
            try {
                BillMain billMain = PstBillMain.fetchExc(creditPaymentMain.getBillMainId());
                ijPaymentDoc.setRefInvoiceNumber(billMain.getInvoiceNumber());
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return ijPaymentDoc;
    }

    public int updateStatusPaymentOnInvoice(IjPaymentOnInvDoc objPaymentOnInvDoc, int iDocStatus) {
        try {
            CreditPaymentMain creditPaymentMain = PstCreditPaymentMain.fetchExc(objPaymentOnInvDoc.getDocId());
            creditPaymentMain.setBillStatus(iDocStatus);
            PstCreditPaymentMain.updateExc(creditPaymentMain);
            // pencarian detail pembayaran
            String where = PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID] + "=" + creditPaymentMain.getOID();
            Vector listPayment = PstCashCreditPayment.list(0, 0, where, "");
            if (listPayment != null && listPayment.size() > 0) {
                for (int i = 0; i < listPayment.size(); i++) {
                    CashCreditPayments cashCreditPayments = (CashCreditPayments) listPayment.get(i);
                    cashCreditPayments.setPaymentStatus(iDocStatus);
                    PstCashCreditPayment.updateExc(cashCreditPayments);
                }
            }
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    /**
     * this method used to get Payment Type document defend on 'objPaymentTypeDocOid' selected
     * return 'obj com.dimata.ij.ibosys.PaymentTypeDoc'
     */
    public IjPaymentTypeDoc getPaymentTypeDoc(long objPaymentTypeDocOid) {
        IjPaymentTypeDoc objIjPaymentTypeDoc = new IjPaymentTypeDoc();

        // Process getPaymentTypeDoc

        return objIjPaymentTypeDoc;
    }

    public int updateStatusPaymentType(IjPaymentTypeDoc objPaymentTypeDoc, int iDocStatus) {
        // tidak ada di pos
        return 0;
    }

    /**
     * pencarian pembayaran cash dan kredit
     *
     * @param lLocationOid
     * @param transactionDate
     * @return
     */
    public Vector getListPaymentOnInvoice(long lLocationOid, Date transactionDate) {
        Vector vectOfPaymentOnInvoiceDoc = new Vector(1, 1);
        try {
            String where = PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_LOCATION_ID] + "=" + lLocationOid +
                    " and " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_DATE] +
                    " between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " and " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_STATUS] + "!=" + I_DocStatus.DOCUMENT_STATUS_POSTED;

            Vector listPayMain = PstCreditPaymentMain.list(0, 0, where, "");
            if (listPayMain != null && listPayMain.size() > 0) {
                Vector listPayment = new Vector();
                Hashtable has = getAllPaymentSystem();
                for (int k = 0; k < listPayMain.size(); k++) {
                    CreditPaymentMain creditPaymentMain = (CreditPaymentMain) listPayMain.get(k);

                    IjPaymentOnInvDoc ijPaymentDoc = new IjPaymentOnInvDoc();
                    ijPaymentDoc.setDocId(creditPaymentMain.getOID());
                    /**
                     * kenapa bill main ois?
                     * karena oid dimain pembayaran
                     * di replace dengan oid main di pos
                     */
                    ijPaymentDoc.setOidDocRefBo(creditPaymentMain.getBillMainId());
                    ijPaymentDoc.setDocNumber(creditPaymentMain.getInvoiceNumber());
                    ijPaymentDoc.setDocTransDate(creditPaymentMain.getBillDate());
                    ijPaymentDoc.setDtLastUpdate(creditPaymentMain.getBillDate());
                    try {
                        BillMain billMain = PstBillMain.fetchExc(creditPaymentMain.getBillMainId());
                        try {
                            ContactList contactList = PstContactList.fetchExc(billMain.getCustomerId());
                            ijPaymentDoc.setDescription(contactList.getCompName() + " [" + contactList.getPersonName() + "] Inv. Number:" + billMain.getInvoiceNumber());
                        } catch (Exception ex) {
                        }
                        ijPaymentDoc.setDocContact(billMain.getCustomerId());
                        ijPaymentDoc.setRefInvoiceNumber(billMain.getInvoiceNumber());
                    } catch (Exception e) {
                    }

                    // pencarian detail pembayaran
                    where = PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID] + "=" + creditPaymentMain.getOID();
                    listPayment = PstCashCreditPayment.list(0, 0, where, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]);
                    double returnPay = 0.0;
                    boolean bool = false;
                    if (listPayment != null && listPayment.size() > 0) {
                        Vector list = new Vector();
                        for (int i = 0; i < listPayment.size(); i++) {
                            CashCreditPayments cashCreditPayments = (CashCreditPayments) listPayment.get(i);

                            ijPaymentDoc.setDocTransCurrency(cashCreditPayments.getCurrencyId());
                            IjPaymentDoc ijPayDoc = new IjPaymentDoc();
                            ijPayDoc.setPayCurrency(cashCreditPayments.getCurrencyId());
                            ijPayDoc.setPayDueDate(cashCreditPayments.getPayDateTime());
                            ijPayDoc.setPayId(cashCreditPayments.getOID());
                            ijPayDoc.setPayNominal(cashCreditPayments.getAmount());
                            if (returnPay < 0) {
                                ijPayDoc.setPayNominal(cashCreditPayments.getAmount() + returnPay);
                                returnPay = 0.0;
                            }
                            if (cashCreditPayments.getAmount() < 0)
                                returnPay = cashCreditPayments.getAmount();

                            ijPayDoc.setPayNumber("");
                            ijPayDoc.setPayRate(cashCreditPayments.getRate());
                            ijPayDoc.setPayType(getLongOidPaymentSystem(has, PstCashPayment.paymentType[cashCreditPayments.getPaymentType()]));
                            ijPayDoc.setRefDocNumber(creditPaymentMain.getInvoiceNumber());
                            System.out.println("cashCreditPayments.getAmount()cashCreditPayments.getAmount() : " + cashCreditPayments.getAmount());
                            if (cashCreditPayments.getAmount() > 0)
                                list.add(ijPayDoc);

                            if (ijPayDoc.getPayType() != 0)
                                bool = true;
                        }
                        ijPaymentDoc.setListPayment(list);
                    }

                    if (bool) {
                        vectOfPaymentOnInvoiceDoc.add(ijPaymentDoc);
                        PstDocLogger.insertUpdateDataBo_toDocLogger(ijPaymentDoc.getDocNumber(), I_IJGeneral.DOC_TYPE_PAYMENT_ON_INV, ijPaymentDoc.getDtLastUpdate(), "Payment : " + ijPaymentDoc.getDocNumber());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("getListPaymentOnInvoice : " + e.toString());
        }
        return vectOfPaymentOnInvoiceDoc;
    }

    /**
     * gadanyana
     * untuk menampilkan data transfer barang
     * sesuai dengan lokasi yang di pilih
     *
     * @param lLocationOid
     * @param transactionDate
     * @param dfType
     * @return
     */

    public Vector getListInventoryOnDF(long lLocationOid, Date transactionDate, int dfType) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sCreationDate = Formater.formatDate(transactionDate, "yyyy-MM-dd");
            if (dfType == I_IJGeneral.DF_TYPE_WAREHOUSE) {
                String sql = "select df.* " +
                        " from " +
                        "" + PstMatDispatch.TBL_DISPATCH + " as df " +
                        " where df." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + lLocationOid +
                        " and df." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                        " and df." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " between '" + sCreationDate + " 00:00:01'" +
                        " and '" + sCreationDate + " 23:59:59'";
                System.out.println("sql transfer : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    IjInventoryOnDFDoc ijInventoryOnDFDoc = new IjInventoryOnDFDoc();

                    ijInventoryOnDFDoc.setDocId(rs.getLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]));
                    ijInventoryOnDFDoc.setDocNumber(rs.getString(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]));
                    Date dates = DBHandler.convertDate(rs.getDate(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]), rs.getTime(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]));
                    ijInventoryOnDFDoc.setDocTransDate(dates);
                    Date dateLastUpdate = DBHandler.convertDate(rs.getDate(PstMatDispatch.fieldNames[PstMatDispatch.FLD_LAST_UPDATE]), rs.getTime(PstMatDispatch.fieldNames[PstMatDispatch.FLD_LAST_UPDATE]));
                    ijInventoryOnDFDoc.setDtLastUpdate(dateLastUpdate);


                    IjInventoryDoc ijInventoryDoc = new IjInventoryDoc();
                    ijInventoryDoc.setInvDestLocation(rs.getLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]));
                    ijInventoryDoc.setInvSourceLocation(rs.getLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID]));
                    try {
                        Location location = PstLocation.fetchExc(ijInventoryDoc.getInvSourceLocation());
                        ijInventoryOnDFDoc.setDesc("Transfer Inventory " + location.getName() + " to ");
                        location = PstLocation.fetchExc(ijInventoryDoc.getInvDestLocation());
                        ijInventoryOnDFDoc.setDesc(ijInventoryOnDFDoc.getDesc() + " " + location.getName());
                    } catch (Exception ee) {
                    }
                    ijInventoryOnDFDoc.setDesc(ijInventoryOnDFDoc.getDesc() + " Date " + Formater.formatDate(ijInventoryOnDFDoc.getDocTransDate(), "dd-MM-yyyy HH:mm"));

                    double total = getTotalDispatch(ijInventoryOnDFDoc.getDocId()); // getTotalDispatch(ijInventoryOnDFDoc.getDocId()); //PstMatDispatchItem.getTotalTransfer(ijInventoryOnDFDoc.getDocId());
                    Vector lists = new Vector();
                    ijInventoryDoc.setInvValue(total);
                    lists.add(ijInventoryDoc);
                    ijInventoryOnDFDoc.setListInventory(lists);

                    list.add(ijInventoryOnDFDoc);
                }
            } else { // for get return inventory, because moving hpp
                String sql = "select ret.* " +
                        " from " +
                        "" + PstMatReturn.TBL_MAT_RETURN + " as ret " +
                        " where ret." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + lLocationOid +
                        " and ret." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                        " and ret." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] + "=" + PstLocation.TYPE_LOCATION_STORE +
                        " and ret." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " between '" + sCreationDate + " 00:00:01'" +
                        " and '" + sCreationDate + " 23:59:59'";
                System.out.println("sql return : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    IjInventoryOnDFDoc ijInventoryOnDFDoc = new IjInventoryOnDFDoc();

                    ijInventoryOnDFDoc.setDocId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]));
                    ijInventoryOnDFDoc.setDocNumber(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]));
                    Date dates = DBHandler.convertDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]), rs.getTime(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                    ijInventoryOnDFDoc.setDocTransDate(dates);
                    Date dateLastUpdate = DBHandler.convertDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_LAST_UPDATE]), rs.getTime(PstMatReturn.fieldNames[PstMatReturn.FLD_LAST_UPDATE]));
                    ijInventoryOnDFDoc.setDtLastUpdate(dateLastUpdate);


                    IjInventoryDoc ijInventoryDoc = new IjInventoryDoc();
                    ijInventoryDoc.setInvDestLocation(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO]));
                    ijInventoryDoc.setInvSourceLocation(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID]));
                    try {
                        Location location = PstLocation.fetchExc(ijInventoryDoc.getInvSourceLocation());
                        ijInventoryOnDFDoc.setDesc("Return Inventory " + location.getName() + " to ");
                        location = PstLocation.fetchExc(ijInventoryDoc.getInvDestLocation());
                        ijInventoryOnDFDoc.setDesc(ijInventoryOnDFDoc.getDesc() + " " + location.getName());
                    } catch (Exception ee) {

                    }
                    ijInventoryOnDFDoc.setDesc(ijInventoryOnDFDoc.getDesc() + " Date " + Formater.formatDate(ijInventoryOnDFDoc.getDocTransDate(), "dd-MM-yyyy HH:mm"));

                    double total = getTotalReturn(ijInventoryOnDFDoc.getDocId()); //PstMatDispatchItem.getTotalTransfer(ijInventoryOnDFDoc.getDocId());
                    Vector lists = new Vector();
                    ijInventoryDoc.setInvValue(total);
                    lists.add(ijInventoryDoc);
                    ijInventoryOnDFDoc.setListInventory(lists);

                    list.add(ijInventoryOnDFDoc);
                }
            }
        } catch (Exception e) {
            System.out.println("Err getListDPonSalesOrder : " + e.toString());
        }
        return list;
    }


    public double getTotalHppDispatch(long oidDispatch) {
        DBResultSet dbrs = null;
        double total = 0.0;
        try {
            /**
             * query ini dipakai jika data yang
             * di ambil memang berdasarkan harga average atau hpp
             */
            String sql = "select * " +
                    " from " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM +
                    " where " + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidDispatch;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Material material = new Material();
                try {
                    material = PstMaterial.fetchExc(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]));
                } catch (Exception e) {
                }
                StandartRate standartRate = PstStandartRate.getActiveStandardRate(material.getDefaultCostCurrencyId());
                total = total + ((material.getAveragePrice() * standartRate.getSellingRate()) * rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("=>>> gettotal return : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }

    public double getTotalDispatch(long oidDispatch) {
        DBResultSet dbrs = null;
        double total = 0.0;
        try {
            /**
             * query ini dipakai jika data yang
             * di ambil memang berdasarkan harga average atau hpp
             */
            String sql = "select sum(" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL] + ") " +
                    " from " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM +
                    " where " + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidDispatch;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("=>>> gettotal return : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }

    public int updateStatusProductionCostOnLGR(IjProdCostOnLGRDoc objProdCostOnLGRDoc, int iDocStatus) {
        int iResult = 0;

        // Process getPaymentTypeDoc

        return iResult;
    }

    public Vector getListDPonSalesOrder(long lLocationOid, Date transactionDate) {
        Vector vectOfDPOnSODoc = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sCreationDate = Formater.formatDate(transactionDate, "yyyy-MM-dd");
            String sql = "select " +
                    " po.* " +
                    " from " +
                    "" + PstPendingOrder.TBL_CASH_PENDING_ORDER + " as po " +
                    " inner join " + PstCashCashier.TBL_CASH_CASHIER + " as cas on " +
                    " po." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_CASHIER_ID] +
                    " = cas." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                    " inner join " + PstCashMaster.TBL_CASH_MASTER + " mas on " +
                    " cas." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] +
                    " =  mas." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] +
                    " where mas." + PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID] + "=" + lLocationOid +
                    " and po." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_STATUS_POSTED] + "!=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " and po." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CREATION_DATE] + "='" + sCreationDate + "'";

            System.out.println("sql untuk DP : >>>> : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long paymentTypeCashOid = 0;
                try {
                    paymentTypeCashOid = Long.parseLong(String.valueOf(PstSystemProperty.getValueByName("PAYMENT_TYPE_CASH")));
                } catch (Exception e) {
                }
                IjDPOnSODoc objDPOnSODoc = new IjDPOnSODoc();
                objDPOnSODoc.setLDocOid(rs.getLong(PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_PENDING_ORDER_ID]));
                objDPOnSODoc.setSNumber(rs.getString(PstPendingOrder.fieldNames[PstPendingOrder.FLD_ORDER_NUMBER]));
                objDPOnSODoc.setISaleType(I_IJGeneral.TRANS_DP_ON_SALES_ORDER); // tipe penjualan : retail
                objDPOnSODoc.setLProdDepartment(0L); // product department : no prod department
                objDPOnSODoc.setLPaymentType(paymentTypeCashOid);
                objDPOnSODoc.setDNominal(rs.getDouble(PstPendingOrder.fieldNames[PstPendingOrder.FLD_DOWN_PAYMENT]));
                objDPOnSODoc.setDtLastUpdate(rs.getDate(PstPendingOrder.fieldNames[PstPendingOrder.FLD_CREATION_DATE]));
                objDPOnSODoc.setLLocation(lLocationOid); // lokasi : Head Office
                objDPOnSODoc.setLTransCurrency(rs.getLong(PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID])); // currency : USD
                objDPOnSODoc.setDtTransDate(rs.getDate(PstPendingOrder.fieldNames[PstPendingOrder.FLD_CREATION_DATE])); // tanggal transaksi : 15 Nov 2005
                objDPOnSODoc.setDtDueDate(rs.getDate(PstPendingOrder.fieldNames[PstPendingOrder.FLD_PLAN_TAKEN_DATE])); // tanggal jatuh tempo : 30 Nov 2005
                vectOfDPOnSODoc.add(objDPOnSODoc);

                PstDocLogger.insertUpdateDataBo_toDocLogger(objDPOnSODoc.getSNumber(), I_IJGeneral.DOC_TYPE_DP_ON_SALES_ORDER, objDPOnSODoc.getDtLastUpdate(), "DP on Sales : " + objDPOnSODoc.getSNumber());
            }
        } catch (Exception e) {
            System.out.println("Err getListDPonSalesOrder : " + e.toString());
        }
        return vectOfDPOnSODoc;
    }

    public int updateStatusInventoryOnDF(IjInventoryOnDFDoc objInventoryOnDFDoc, int iDocStatus) {
        try {
            MatDispatch matDispatch = PstMatDispatch.fetchExc(objInventoryOnDFDoc.getDocId());
            matDispatch.setDispatchStatus(iDocStatus);
            PstMatDispatch.updateExc(matDispatch);
        } catch (Exception e) {
            System.out.println("Err updateStatusInventoryOnDF ; " + e.toString());
            return 1;
        }
        return 0;
    }

    public Vector getListPaymentTypePostedCleared(long lLocationOid, Date transactionDate) {
        return new Vector();
    }

    //public Vector getListPriceType() {
       // return new Vector();
   // }

    /*
     * Method getListPriceType
     * By Mirahu 24 May 2011
     */

    public Vector getListPriceType() {
          Vector listPriceType = new Vector(1, 1);

        // process getListCurrencyType
        String strOrder = PstPriceType.fieldNames[PstPriceType.FLD_INDEX];
        Vector vctPriceType = PstPriceType.list(0, 0, "", "");
        if (vctPriceType != null && vctPriceType.size() > 0) {
            int maxPriceType = vctPriceType.size();
            for (int i = 0; i < maxPriceType; i++) {
                PriceType objPriceType = (PriceType) vctPriceType.get(i);
                listPriceType.add(objPriceType);
            }
        }

        return listPriceType;
    }


    /**
     * gadnyana
     * pencarian data penjualan
     * yang kredit maupun yang cash
     *
     * @param lLocationOid
     * @param transactionDate
     * @param salesType
     * @param transactionType
     * @return
     */
    public Vector getListSalesOnInvoice(long lLocationOid, Date transactionDate, int salesType, int transactionType) {
        Vector vectOfSalesOnInvoiceDoc = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            if (transactionType == I_IJGeneral.TRANSACTION_TYPE_CASH) {
                vectOfSalesOnInvoiceDoc = getPenjualanCash(lLocationOid, transactionDate, salesType, transactionType);
            } else if (transactionType == I_IJGeneral.TRANSACTION_TYPE_CREDIT) {
                vectOfSalesOnInvoiceDoc = getPenjualanKredit(lLocationOid, transactionDate, salesType, transactionType);
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return vectOfSalesOnInvoiceDoc;
    }

    // gadnyana
    // mencari data penjualan yang di bayar tunai
    public Vector getPenjualanCash(long lLocationOid, Date transactionDate, int salesType, int transactionType) {
        Vector vectOfSalesOnInvoiceDoc = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "select m.currency_id, m.shift_id, m.cash_cashier_id, min(m.invoice_number) as invoice, " +
                    " max(m.location_id) as loc_id, sum(m.disc) as tot_disc, " +
                    " sum(m.tax_value) as tax_val, sum(m.service_value) as service_val, " +
                    " min(m.bill_date) as billdate " +
                    " from cash_bill_main as m" +
                    " where m.trans_type = " + salesType + " and m.transaction_type = " + transactionType +
                    " and m.location_id = " + lLocationOid +
                    " and (m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " or m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " or m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_FINAL + ")" +
                    " and m.doc_type = " + PstBillMain.TYPE_INVOICE +
                    " and m.transaction_status != " + PstBillMain.TRANS_STATUS_DELETED +
                    " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " group by m.shift_id";

            System.out.println("query penjualan : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector list = new Vector();
            long cashCashier = 0;
            while (rs.next()) {
                IjSalesOnInvDoc iJSalesOnInvDoc = new IjSalesOnInvDoc();
                iJSalesOnInvDoc.setDocId(rs.getLong("shift_id"));
                iJSalesOnInvDoc.setDocNumber(rs.getString("invoice"));
                iJSalesOnInvDoc.setDocSaleType(salesType);
                iJSalesOnInvDoc.setDocLocation(lLocationOid);
                iJSalesOnInvDoc.setDocDiscount(rs.getDouble("tot_disc"));
                iJSalesOnInvDoc.setDocTax(rs.getDouble("tax_val"));
                iJSalesOnInvDoc.setDocOtherCost(rs.getDouble("service_val"));
                iJSalesOnInvDoc.setDocTransCurrency(rs.getLong("currency_id"));
                iJSalesOnInvDoc.setDocContact(0);
                iJSalesOnInvDoc.setDocSaleType(salesType);
                iJSalesOnInvDoc.setTransactionType(transactionType);
                Date dates = DBHandler.convertDate(rs.getDate("billdate"), rs.getTime("billdate"));
                iJSalesOnInvDoc.setDocTransDate(dates);
                iJSalesOnInvDoc.setRefSONumber("");

                if (cashCashier != rs.getLong("shift_id")) {
                    cashCashier = rs.getLong("shift_id");

                    Vector listDp = getTotalDpOnSaleInvoice(rs.getLong("shift_id"), lLocationOid, transactionDate, salesType, transactionType);
                    iJSalesOnInvDoc.setListDPDeduction(listDp);
                    iJSalesOnInvDoc.setDtLastUpdate(dates);

                    SrcSaleReport srcSaleReport = new SrcSaleReport();
                    srcSaleReport.setLocationId(lLocationOid);
                    srcSaleReport.setShiftId(rs.getLong("shift_id"));
                    srcSaleReport.setDateFrom(transactionDate);
                    srcSaleReport.setDateTo(transactionDate);


                    PstCashPayment pstCashPayment = new PstCashPayment();
                    Vector listPayment = pstCashPayment.getListPayment(srcSaleReport, PstBillMain.TYPE_INVOICE, PstBillMain.TRANS_TYPE_CASH);

                    double totalDp = 0.0;
                    if (listDp.size() > 0) {
                        for (int i = 0; i < listDp.size(); i++) {
                            IjDPDeductionDoc IjDPDeductionDoc = (IjDPDeductionDoc) listDp.get(i);
                            totalDp = totalDp + IjDPDeductionDoc.getPayNominal();
                        }
                    }

                    double totalSale = getTotalByPayment(listPayment);
                    Vector tTotal = new Vector();
                    IjSalesValue ijSalesValue = new IjSalesValue();

                    //####### pengecekan total sale
                    double totalAllSale = 0;
                    totalAllSale = totalSale + totalDp; // + payment on dp
                    totalAllSale = totalAllSale - iJSalesOnInvDoc.getDocTax(); // + iJSalesOnInvDoc.getDocTax()
                    totalAllSale = totalAllSale - iJSalesOnInvDoc.getDocOtherCost(); // + iJSalesOnInvDoc.getDocTax()
                    totalAllSale = totalAllSale + iJSalesOnInvDoc.getDocDiscount(); // + iJSalesOnInvDoc.getDocTax()
                    // (totalSale + totalDp + iJSalesOnInvDoc.getDocTax() + iJSalesOnInvDoc.getDocOtherCost()) + iJSalesOnInvDoc.getDocDiscount()
                    ijSalesValue.setSalesValue(totalAllSale);

                    ijSalesValue.setCostValue(getCostSalesValue(rs.getLong("shift_id"), lLocationOid, transactionDate));
                    tTotal.add(ijSalesValue);
                    iJSalesOnInvDoc.setListSalesValue(tTotal); //getIjSalesValue(iJSalesOnInvDoc.getDocId(), lLocationOid, transactionDate));

                    // total penjualan
                    // iJSalesOnInvDoc.setListSalesValue(getIjSalesValue(iJSalesOnInvDoc.getDocId(),lLocationOid, transactionDate,salesType,transactionType));

                    // total pembayaran
                    // double total = getTotalSaleCash(iJSalesOnInvDoc.getListSalesValue()) + iJSalesOnInvDoc.getDocTax() + iJSalesOnInvDoc.getDocOtherCost();
                    // Vector listPay = getPaymentSaleCash(listDp, iJSalesOnInvDoc.getDocDiscount(), total, iJSalesOnInvDoc.getDocTransCurrency(), lLocationOid, iJSalesOnInvDoc.getDocId(), transactionDate, salesType, transactionType);

                    Vector listPay = getTotalByPaymentCash(listPayment);
                    iJSalesOnInvDoc.setListPayment(listPay);

                    vectOfSalesOnInvoiceDoc.add(iJSalesOnInvDoc);
                    PstDocLogger.insertUpdateDataBo_toDocLogger(iJSalesOnInvDoc.getDocNumber(), I_IJGeneral.DOC_TYPE_SALES_ON_INV, dates, "Penjualan : " + iJSalesOnInvDoc.getDocNumber());
                }
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return vectOfSalesOnInvoiceDoc;
    }

    public static double getCostSalesValue(long shiftId, long lLocationOid, Date transactionDate) {
        DBResultSet dbrs = null;
        double totalCost = 0.0;
        try {
            String sql = "select sum(d.qty * d.cost) as totalcost " +
                    " from cash_bill_detail as d " +
                    " inner join cash_bill_main as m on d.cash_bill_main_id = m.cash_bill_main_id " +
                    " where m.transaction_type = " + PstBillMain.TRANS_TYPE_CASH +
                    " and m.doc_type = " + PstBillMain.TYPE_INVOICE +
                    " and m.location_id = " + lLocationOid +
                    " and m.bill_status != " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " and m.shift_id = " + shiftId;
            sql = sql + " and m.transaction_status = " + PstBillMain.TRANS_STATUS_CLOSE;
            sql = sql + " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "' " +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " group by m.shift_id"; // cash_cashier_id

            System.out.println("===================>>>>>>>>>>>>>>>>>>>> : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                totalCost = rs.getDouble("totalcost");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return totalCost;
    }

    public static double getCostSalesValue(long cashBillMainId) {
        DBResultSet dbrs = null;
        double totalCost = 0.0;
        try {
            String sql = "select sum(d.qty * d.cost) as totalcost " +
                    " from cash_bill_detail as d " +
                    " where d.cash_bill_main_id=" + cashBillMainId;
            System.out.println("===================>>>>>>>>>>>>>>>>>>>> : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                totalCost = rs.getDouble("totalcost");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return totalCost;
    }

    public static double getTotalByPayment(Vector vListPayment) {
        double dTotalPayment = 0;
        if (vListPayment != null && vListPayment.size() > 0) {
            int iListPaymentCount = vListPayment.size();
            for (int i = 0; i < iListPaymentCount; i++) {
                Vector vObj = (Vector) vListPayment.get(i);
                if (vObj != null && vObj.size() > 0) {
                    double dPaymentPerType = 0;
                    for (int j = 0; j < vObj.size(); j++) {
                        CashPayments objCashPayment = (CashPayments) vObj.get(j);
                        dPaymentPerType = dPaymentPerType + objCashPayment.getAmount();
                    }
                    dTotalPayment = dTotalPayment + dPaymentPerType;
                }
            }
        }
        return dTotalPayment;
    }

    public static CashPayments getTotalByPaymentCash(Vector vListPayment, int paymentType) {
        double dTotalPayment = 0;
        CashPayments objCashPayment = new CashPayments();
        if (vListPayment != null && vListPayment.size() > 0) {
            int iListPaymentCount = vListPayment.size();
            boolean bool = false;
            for (int i = 0; i < iListPaymentCount; i++) {
                Vector vObj = (Vector) vListPayment.get(i);
                if (vObj != null && vObj.size() > 0) {
                    double dPaymentPerType = 0;
                    for (int j = 0; j < vObj.size(); j++) {
                        objCashPayment = (CashPayments) vObj.get(j);
                        // System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ =>>>>>>>>>>>>>>>>>>>>>>> pay type paymentType:"+paymentType+"="+objCashPayment.getPaymentType());
                        if (paymentType == objCashPayment.getPaymentType()) {
                            bool = true;
                            break;
                        }
                    }
                    dTotalPayment = dTotalPayment + dPaymentPerType;
                }
                if (bool)
                    break;
            }
        }
        return objCashPayment;
    }

    public Vector getTotalByPaymentCash(Vector vListPayment) {
        double dTotalPayment = 0;
        Vector list = new Vector();
        if (vListPayment != null && vListPayment.size() > 0) {
            int iListPaymentCount = vListPayment.size();
            boolean bool = false;
            Hashtable has = getAllPaymentSystem();
            for (int i = 0; i < iListPaymentCount; i++) {
                Vector vObj = (Vector) vListPayment.get(i);
                if (vObj != null && vObj.size() > 0) {
                    double dPaymentPerType = 0;
                    for (int j = 0; j < vObj.size(); j++) {
                        CashPayments objCashPayment = (CashPayments) vObj.get(j);
                        IjPaymentDoc ijPayDoc = new IjPaymentDoc();
                        ijPayDoc.setPayCurrency(objCashPayment.getCurrencyId());
                        ijPayDoc.setPayId(objCashPayment.getOID());
                        ijPayDoc.setPayNominal(objCashPayment.getAmount());
                        ijPayDoc.setPayNumber("");
                        ijPayDoc.setPayRate(objCashPayment.getRate());
                        ijPayDoc.setPayType(getLongOidPaymentSystem(has, PstCashPayment.paymentType[objCashPayment.getPaymentType()]));
                        System.out.println("ijPayDoc.setPayType( :" + objCashPayment.getPaymentType() + " ijPayDoc. " + ijPayDoc.getPayType());
                        ijPayDoc.setRefDocNumber("");
                        Date dates = objCashPayment.getPayDateTime();
                        ijPayDoc.setPayDueDate(dates);
                        list.add(ijPayDoc);
                    }
                    dTotalPayment = dTotalPayment + dPaymentPerType;
                }
            }
        }
        return list;
    }


    /**
     * total cash
     *
     * @param list
     * @return
     */
    public static double getTotalSaleCash(Vector list) {
        double total = 0.0;
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                IjSalesValue ijSalesValue = (IjSalesValue) list.get(k);
                total = total + ijSalesValue.getSalesValue();
            }
        }
        return total;
    }

    /**
     * @return
     */
    public static Vector getIjSalesValue(long cashCashierId, long lLocationOid, Date transactionDate, int salesType, int transactionType) {
        Vector list = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "select sum(total_price) as totalprice, sum(d.qty * d.cost) as totalcost " +
                    " from cash_bill_detail as d " +
                    " inner join cash_bill_main as m on d.cash_bill_main_id = m.cash_bill_main_id " +
                    " where m.trans_type = " + salesType + " and m.transaction_type = " + transactionType +
                    " and m.doc_type = " + PstBillMain.TYPE_INVOICE +
                    " and m.location_id = " + lLocationOid +
                    " and m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " and m.cash_cashier_id = " + cashCashierId +
                    " and m.transaction_status != " + PstBillMain.TRANS_STATUS_DELETED +
                    " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "' " +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " group by m.cash_cashier_id";

            System.out.println("query penjualan total item : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjSalesValue ijSalesValue = new IjSalesValue();
                ijSalesValue.setSalesValue(rs.getDouble("totalprice"));
                ijSalesValue.setCostValue(rs.getDouble("totalcost"));
                list.add(ijSalesValue);
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return list;
    }


    /* public Vector getTotalInvoice(long oidMain){
        Vector vTotal = new Vector(1,1);
        DBResultSet dbrs = null;
        try {
            String sql = "select sum(d.total_price) as totalprice," +
                    " sum(d.cost * d.qty) as totalcost" +
                    " from  cash_bill_detail as m" +
                    " where d.cash_bill_main_id = " + oidMain;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector list = new Vector();
            while (rs.next()) {
                vTotal.add(rs.getDouble("totalprice"));
                vTotal.add(rs.getDouble("totalcost"));
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return vTotal;
    }*/
    // gadnyana
    // mencari data penjualan yang di bayar secara kredit
    public Vector getPenjualanKredit(long lLocationOid, Date transactionDate, int salesType, int transactionType) {
        Vector vectOfSalesOnInvoiceDoc = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "select m.customer_id,m.currency_id, m.cash_bill_main_id,m.cash_pending_order_id,min(m.invoice_number) as invoice, " +
                    " max(m.location_id) as loc_id, sum(m.disc) as tot_disc, " +
                    " sum(m.tax_value) as tax_val, sum(m.service_value) as service_val," +
                    " min(m.bill_date) as billdate " +
                    " from cash_bill_main as m" +
                    " where m.transaction_type = " + transactionType +
                    " and m.location_id = " + lLocationOid +
                    " and (m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " or m.bill_status = " + I_DocStatus.DOCUMENT_STATUS_DRAFT + ")" +
                    " and m.doc_type = " + PstBillMain.TYPE_INVOICE +
                    " and m.transaction_status != " + PstBillMain.TRANS_STATUS_DELETED +
                    " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " group by m.cash_bill_main_id";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector list = new Vector();
            while (rs.next()) {
                IjSalesOnInvDoc iJSalesOnInvDoc = new IjSalesOnInvDoc();
                iJSalesOnInvDoc.setDocId(rs.getLong("cash_bill_main_id"));
                iJSalesOnInvDoc.setDocNumber(rs.getString("invoice"));
                iJSalesOnInvDoc.setDocSaleType(salesType);
                iJSalesOnInvDoc.setDocLocation(lLocationOid);
                iJSalesOnInvDoc.setDocDiscount(rs.getDouble("tot_disc"));
                iJSalesOnInvDoc.setDocTax(rs.getDouble("tax_val"));
                iJSalesOnInvDoc.setDocOtherCost(rs.getDouble("service_val"));
                iJSalesOnInvDoc.setDocTransCurrency(rs.getLong("currency_id"));
                iJSalesOnInvDoc.setDocSaleType(salesType);
                iJSalesOnInvDoc.setTransactionType(transactionType);
                Date dates = DBHandler.convertDate(rs.getDate("billdate"), rs.getTime("billdate"));
                iJSalesOnInvDoc.setDocTransDate(dates);
                iJSalesOnInvDoc.setDtLastUpdate(dates);
                iJSalesOnInvDoc.setDocContact(rs.getLong("customer_id"));
                System.out.println("############################################ : " + iJSalesOnInvDoc.getDocDiscount());
                long pending_order_id = rs.getLong("cash_pending_order_id");
                if (pending_order_id != 0) {
                    PendingOrder pendingOrder = PstPendingOrder.fetchExc(pending_order_id);
                    iJSalesOnInvDoc.setRefSONumber(pendingOrder.getOrderNumber());

                    Vector listDp = new Vector();
                    IjDPDeductionDoc IjDPDeductionDoc = new IjDPDeductionDoc();
                    IjDPDeductionDoc.setPayCurrency(pendingOrder.getCurrencyId());
                    IjDPDeductionDoc.setPayDueDate(pendingOrder.getExpiredDate());
                    IjDPDeductionDoc.setPayId(pendingOrder.getOID());
                    IjDPDeductionDoc.setPayNominal(pendingOrder.getDownPayment());
                    IjDPDeductionDoc.setPayRate(pendingOrder.getRate());
                    listDp.add(IjDPDeductionDoc);

                    iJSalesOnInvDoc.setListDPDeduction(listDp);
                }

                // total penjualan kredit
                list = new Vector();
                IjSalesValue ijSalesValue = new IjSalesValue();
                double totalSales = 0;
                totalSales = totalSales + getTotalInvoice(rs.getLong("cash_bill_main_id")); //rs.getDouble("totalprice");
                //totalSales = totalSales + rs.getDouble("tot_disc");
                //totalSales = totalSales - rs.getDouble("service_val");
                //totalSales = totalSales - rs.getDouble("tax_val");

                ijSalesValue.setSalesValue(totalSales);
                ijSalesValue.setCostValue(getCostSalesValue(rs.getLong("cash_bill_main_id"))); //rs.getDouble("totalcost")
                list.add(ijSalesValue);
                iJSalesOnInvDoc.setListSalesValue(list);

                vectOfSalesOnInvoiceDoc.add(iJSalesOnInvDoc);
                PstDocLogger.insertUpdateDataBo_toDocLogger(iJSalesOnInvDoc.getDocNumber(), I_IJGeneral.DOC_TYPE_SALES_ON_INV, dates, "Penjualan kredit : " + iJSalesOnInvDoc.getDocNumber());
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return vectOfSalesOnInvoiceDoc;
    }

    /**
     * gadnyana
     * untuk mencari pembayaran dari 1 shift
     *
     * @param lLocationOid
     * @param cashCashierOid
     * @param transactionDate
     * @param salesType
     * @param transactionType
     * @return
     */
    public Vector getPaymentSaleCash(Vector listDp, double disc, double totalCash, long oidCurrency, long lLocationOid, long cashCashierOid,
                                     Date transactionDate, int salesType, int transactionType) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        Hashtable has = getAllPaymentSystem();
        try {
            String sql = "select p.cash_payment_id,m.invoice_number as invoice_number, " +
                    " sum(p.amount) as totalpayment," +
                    " p.currency_id as currencyid," +
                    " p.pay_type as paytype, " +
                    " p.rate as prate, " +
                    " p.pay_datetime as sdate " +
                    " from cash_bill_main as m " +
                    " inner join cash_payment as p on m.cash_bill_main_id = p.cash_bill_main_id " +
                    " where m.location_id = " + lLocationOid + " and m.trans_type = " + salesType +
                    " and m.transaction_type = " + transactionType +
                    " and m.cash_cashier_id = " + cashCashierOid +
                    " and p.payment_status != " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " and m.doc_type = " + PstBillMain.TYPE_INVOICE +
                    " and m.transaction_status != " + PstBillMain.TRANS_STATUS_DELETED +
                    " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " group by m.cash_cashier_id,p.pay_type " +
                    " order by m.bill_date";

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double totalPayment = 0.0;
            while (rs.next()) {
                IjPaymentDoc ijPayDoc = new IjPaymentDoc();
                ijPayDoc.setPayCurrency(rs.getLong("currencyid"));
                ijPayDoc.setPayId(rs.getLong("cash_payment_id"));
                ijPayDoc.setPayNominal(rs.getDouble("totalpayment"));
                ijPayDoc.setPayNumber("");
                ijPayDoc.setPayRate(rs.getDouble("prate"));
                ijPayDoc.setPayType(getLongOidPaymentSystem(has, PstCashPayment.paymentType[rs.getInt("paytype")]));
                ijPayDoc.setRefDocNumber("");
                Date dates = DBHandler.convertDate(rs.getDate("sdate"), rs.getTime("sdate"));
                ijPayDoc.setPayDueDate(dates);
                list.add(ijPayDoc);

                totalPayment = totalPayment + ijPayDoc.getPayNominal();
            }
            // System.out.println("totalPayment blm tambah dp                : " + totalPayment);
            if (listDp.size() > 0) {
                for (int i = 0; i < listDp.size(); i++) {
                    IjDPDeductionDoc IjDPDeductionDoc = (IjDPDeductionDoc) listDp.get(i);
                    totalPayment = totalPayment + IjDPDeductionDoc.getPayNominal();
                }
            }
            //totalPayment = totalPayment + disc;
            System.out.println("totalPayment + dp                   : " + totalPayment);
            System.out.println("totalCash                           : " + (totalCash - disc));
            System.out.println("(totalPayment + dp - totalCash)     : " + (totalPayment - (totalCash - disc)));

            // pengecekan untuk susuk
            double sisa = totalPayment - (totalCash - disc);
            boolean boolxx = false;
            if (sisa > 0) {
                if (list.size() > 0) {
                    boolxx = false;
                    int k = 0;
                    while (k < list.size() && !boolxx) {
                        IjPaymentDoc ijPayDocx = (IjPaymentDoc) list.get(k);
                        if (ijPayDocx.getPayCurrency() == oidCurrency) {
                            if (ijPayDocx.getPayNominal() > sisa) {
                                ijPayDocx.setPayNominal(ijPayDocx.getPayNominal() - sisa);
                                list.setElementAt(ijPayDocx, k);
                                boolxx = true;
                            }
                        }
                        k++;
                    }
                    if (k < (list.size()) && !boolxx) {
                        IjPaymentDoc ijPayDocxx = (IjPaymentDoc) list.get(0);
                        ijPayDocxx.setPayNominal(ijPayDocxx.getPayNominal() - sisa);
                        list.setElementAt(ijPayDocxx, 0);
                        System.out.println(" helooo 3");
                    }/*else{
                        if (listDp.size() > 0) {
                            for (int i = 0; i < listDp.size(); i++) {
                                System.out.println(" helooo 4");
                                //IjDPDeductionDoc IjDPDeductionDoc = (IjDPDeductionDoc) listDp.get(i);
                                //totalPayment = totalPayment + IjDPDeductionDoc.getPayNominal();
                            }
                        }
                    }*/
                }
            }/*else{
                if (listDp.size() > 0) {
                    boolxx = false;
                    int k = 0;
                    if (totalPayment > totalCash) { // cek setelah di tambah dp
                        while (k < list.size() && !boolxx) {
                            IjPaymentDoc ijPayDocx = (IjPaymentDoc) list.get(k);
                            if (ijPayDocx.getPayCurrency() == oidCurrency) {
                                ijPayDocx.setPayNominal(ijPayDocx.getPayNominal() - (totalPayment - totalCash));
                                list.setElementAt(ijPayDocx, k);
                                boolxx = true;
                            }
                            k++;
                        }
                    }
                    if (k == (list.size()) && !boolxx) {
                        System.out.println(" helooo 1");
                        IjPaymentDoc ijPayDocxx = (IjPaymentDoc) list.get(0);
                        System.out.println(" helooo 2");
                        ijPayDocxx.setPayNominal(ijPayDocxx.getPayNominal() - (totalPayment - totalCash));
                        list.setElementAt(ijPayDocxx, 0);
                        System.out.println(" helooo 3");
                    }
                }
            }*/
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return list;
    }

    /**
     * gadnyana
     * untuk mencari total dp transaksi penjualan kredit
     *
     * @return
     */
    public Vector getTotalDpOnSaleInvoice(long cashCashierOid, long lLocationOid, Date transactionDate, int salesType, int transactionType) {
        Vector vectOfSalesDpDoc = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = " select sum(d." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_DOWN_PAYMENT] + ") as dp," +
                    " d." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CREATION_DATE] + "," +
                    " d." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID] + "," +
                    " d." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_EXPIRED_DATE] + "," +
                    " d." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_PENDING_ORDER_ID] + "," +
                    " d." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_RATE] +
                    " from cash_bill_main as m" +
                    " inner join cash_pending_order as d on m.cash_pending_order_id = d.cash_pending_order_id" +
                    " where m.transaction_type = " + transactionType +
                    " and m.location_id = " + lLocationOid +
                    " and m.bill_date between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " and m.shift_id = " + cashCashierOid +
                    " and m.doc_type = " + PstBillMain.TYPE_INVOICE +
                    " and m.transaction_status != " + PstBillMain.TRANS_STATUS_DELETED +
                    " group by m.shift_id"; // m.cash_pending_order_id";

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector list = new Vector();
            while (rs.next()) {
                IjDPDeductionDoc IjDPDeductionDoc = new IjDPDeductionDoc();
                IjDPDeductionDoc.setPayCurrency(rs.getLong(PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]));
                IjDPDeductionDoc.setPayDueDate(rs.getDate(PstPendingOrder.fieldNames[PstPendingOrder.FLD_EXPIRED_DATE]));
                IjDPDeductionDoc.setPayId(rs.getLong(PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_PENDING_ORDER_ID]));
                IjDPDeductionDoc.setPayNominal(rs.getDouble("dp"));
                IjDPDeductionDoc.setPayRate(rs.getDouble(PstPendingOrder.fieldNames[PstPendingOrder.FLD_RATE]));

                vectOfSalesDpDoc.add(IjDPDeductionDoc);
            }
        } catch (Exception e) {
            System.out.println("getListSalesOnInvoice : " + e.toString());
        }
        return vectOfSalesDpDoc;
    }

    public Vector getListDPonPurchaseOrder(long lLocationOid, Date transactionDate) {
        return new Vector();
    }

    public Vector getListDPonProductionOrder(long lLocationOid, Date transactionDate) {
        Vector vectOfDPOnPdODoc = new Vector(1, 1);

        // process getListDPonProductionOrder
        IjDPOnPdODoc objDPOnPdODoc = new IjDPOnPdODoc();
        objDPOnPdODoc.setLDocOid(2L);
        objDPOnPdODoc.setSNumber("DP-PdO-001");
        objDPOnPdODoc.setDtTransDate(new Date(105, 8, 25));
        objDPOnPdODoc.setDtDueDate(new Date(105, 8, 30));
        objDPOnPdODoc.setLPaymentType(504404243029127309L);
        objDPOnPdODoc.setLTransCurrency(1L);
        objDPOnPdODoc.setDRate(1);
        objDPOnPdODoc.setDNominal(1500000);
        objDPOnPdODoc.setDtLastUpdate(new Date(105, 8, 25));

        vectOfDPOnPdODoc.add(objDPOnPdODoc);

        return vectOfDPOnPdODoc;
    }

    public Vector getListProductionCostOnLGR(long lLocationOid, Date transactionDate) {
        Vector vectOfProductionCostOnLGRDoc = new Vector(1, 1);

        // process getListProductionCostOnLGR
        Vector vDp = new Vector(1, 1);
        IjDPDeductionDoc objIjDPDeduction = new IjDPDeductionDoc();
        objIjDPDeduction.setPayId(71L);
        objIjDPDeduction.setPayType(504404243029127309L);
        objIjDPDeduction.setPayDueDate(new Date(105, 8, 30));
        objIjDPDeduction.setPayCurrency(1L);
        objIjDPDeduction.setPayRate(1);
        objIjDPDeduction.setPayNominal(200000);
        vDp.add(objIjDPDeduction);

        Vector vInv = new Vector(1, 1);
        IjInventoryDoc objIjInventoryDoc = new IjInventoryDoc();
        objIjInventoryDoc.setInvSourceLocation(504404271285127208L);
        objIjInventoryDoc.setInvDestLocation(504404220387278818L);
        objIjInventoryDoc.setInvProdDepartment(0L);
        objIjInventoryDoc.setInvValue(2000000);
        vInv.add(objIjInventoryDoc);

        Vector vProdCost = new Vector(1, 1);
        IjProductionCostValue objIjProductionCostValue = new IjProductionCostValue();
        objIjProductionCostValue.setProdDepartment(0L);
        objIjProductionCostValue.setProdCostValue(500000);
        vProdCost.add(objIjProductionCostValue);

        IjProdCostOnLGRDoc objIjProdCostOnLGRDoc = new IjProdCostOnLGRDoc();
        objIjProdCostOnLGRDoc.setDocId(7L);
        objIjProdCostOnLGRDoc.setDocNumber("LGR-PD-001");
        objIjProdCostOnLGRDoc.setDocContact(0L);
        objIjProdCostOnLGRDoc.setDocTransDate(new Date(105, 8, 28));
        objIjProdCostOnLGRDoc.setDocTransCurrency(1L);
        objIjProdCostOnLGRDoc.setDocSaleType(-1);
        objIjProdCostOnLGRDoc.setDocLocation(504404220387278818L);
        objIjProdCostOnLGRDoc.setDocDueDate(new Date(105, 8, 30));
        objIjProdCostOnLGRDoc.setDocDiscount(50000);
        objIjProdCostOnLGRDoc.setDocTax(30000);
        objIjProdCostOnLGRDoc.setRefDfNumber("DF-W-001");
        objIjProdCostOnLGRDoc.setRefPdONumber("DF_p-001");
        objIjProdCostOnLGRDoc.setListDPDeduction(vDp);
        objIjProdCostOnLGRDoc.setListInventory(vInv);
        objIjProdCostOnLGRDoc.setListProdCost(vProdCost);
        objIjProdCostOnLGRDoc.setDtLastUpdate(new Date(105, 8, 28));

        vectOfProductionCostOnLGRDoc.add(objIjProdCostOnLGRDoc);

        return vectOfProductionCostOnLGRDoc;
    }

    /**
     * this method used to get list Payment Type defend on 'location' and 'date' of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Payment
     *         Type's location
     * @param <CODE>transactionDate</CODE>Payment
     *         Type's transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjPaymentTypeDoc'
     */
    public Vector getListPaymentTypeNotPosted(long lLocationOid, Date transactionDate) {
        Vector vectOfPaymentTypeDoc = new Vector(1, 1);

        // Process getListPaymentType

        return vectOfPaymentTypeDoc;
    }

    /**
     * this method used to get list Payment Type defend on 'location' and 'date' of transaction selected by user
     * @param <CODE>lLocationOid</CODE>Payment Type's location
     * @param <CODE>transactionDate</CODE>Payment Type's transaction date
     * return 'vector of obj com.dimata.ij.ibosys.IjPaymentTypeDoc'
     */
    /**
     * this method used to get list DF for Costing defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Inventory
     *         On DF's location
     * @param <CODE>transactionDate</CODE>Inventory
     *         On DF's transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjInventoryOnDFDoc'
     */
    public Vector getListLGRFromDepartment(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }

    public IjInventoryOnDFDoc getLGRFromDepartmentDoc(long objInvOnDFCostingDocOid) {
        IjInventoryOnDFDoc objIjInventoryOnDFDoc = new IjInventoryOnDFDoc();

        return objIjInventoryOnDFDoc;
    }

    public int updateStatusLGRFromDepartment(IjInventoryOnDFDoc objInvOnDFCostingDoc, int iDocStatus) {
        int iResult = 0;

        return iResult;
    }
    // --------------- End LGR From Department ---------------

    public Vector getListDFToDepartment(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }

    public IjInventoryOnDFDoc getDFToDepartmentDoc(long objInvOnDFCostingDocOid) {
        IjInventoryOnDFDoc objIjInventoryOnDFDoc = new IjInventoryOnDFDoc();
        objIjInventoryOnDFDoc.setDocId(objInvOnDFCostingDocOid);
        return objIjInventoryOnDFDoc;
    }

    public int updateStatusDFToDepartment(IjInventoryOnDFDoc objInvOnDFCostingDoc, int iDocStatus) {
        int iResult = 0;
        try {
            MatReturn matReturn = PstMatReturn.fetchExc(objInvOnDFCostingDoc.getDocId());
            matReturn.setReturnStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
            PstMatReturn.updateExc(matReturn);
        } catch (Exception e) {
        }
        return iResult;
    }
    // --------------- End DF To Department ---------------

    public Vector getListInvOnDFCosting(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sCreationDate = Formater.formatDate(transactionDate, "yyyy-MM-dd");
            String sql = "select cos.* " +
                    " from " +
                    "" + PstMatCosting.TBL_COSTING + " as cos " +
                    " where cos." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + lLocationOid +
                    " and cos." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " and cos." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " between '" + sCreationDate + " 00:00:01'" +
                    " and '" + sCreationDate + " 23:59:59'";

            System.out.println("sql transfer : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjInventoryOnDFDoc ijInventoryOnDFDoc = new IjInventoryOnDFDoc();

                ijInventoryOnDFDoc.setDocId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]));
                ijInventoryOnDFDoc.setDocNumber(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]));
                Date dates = DBHandler.convertDate(rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]), rs.getTime(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                ijInventoryOnDFDoc.setDocTransDate(dates);
                Date dateLastUpdate = DBHandler.convertDate(rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_LAST_UPDATE]), rs.getTime(PstMatCosting.fieldNames[PstMatCosting.FLD_LAST_UPDATE]));
                ijInventoryOnDFDoc.setDtLastUpdate(dateLastUpdate);

                IjInventoryDoc ijInventoryDoc = new IjInventoryDoc();
                ijInventoryDoc.setInvDestLocation(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO]));
                ijInventoryDoc.setInvSourceLocation(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]));

                double total = PstMatCostingItem.getTotalCosting(ijInventoryOnDFDoc.getDocId());
                Vector lists = new Vector();
                ijInventoryDoc.setInvValue(total);
                lists.add(ijInventoryDoc);
                ijInventoryOnDFDoc.setListInventory(lists);

                vResult.add(ijInventoryOnDFDoc);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err getListDPonSalesOrder : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }

    public IjInventoryOnDFDoc getInvOnDFCostingDoc(long objInvOnDFCostingDocOid) {
        IjInventoryOnDFDoc ijInventoryOnDFDoc = new IjInventoryOnDFDoc();
        try {
            MatCosting matCosting = PstMatCosting.fetchExc(objInvOnDFCostingDocOid);

            ijInventoryOnDFDoc.setDocId(objInvOnDFCostingDocOid);

            ijInventoryOnDFDoc.setDocNumber(matCosting.getCostingCode());
            ijInventoryOnDFDoc.setDocTransDate(matCosting.getCostingDate());
            ijInventoryOnDFDoc.setDtLastUpdate(matCosting.getLastUpdate());

            IjInventoryDoc ijInventoryDoc = new IjInventoryDoc();
            ijInventoryDoc.setInvDestLocation(matCosting.getCostingTo());
            ijInventoryDoc.setInvSourceLocation(matCosting.getLocationId());

            double total = PstMatCostingItem.getTotalCosting(ijInventoryOnDFDoc.getDocId());
            Vector lists = new Vector();
            ijInventoryDoc.setInvValue(total);
            lists.add(ijInventoryDoc);
            ijInventoryOnDFDoc.setListInventory(lists);

        } catch (Exception e) {
            System.out.println("error get object costing : " + e.toString());
        }
        return ijInventoryOnDFDoc;
    }

    /**
     * this method used to update status DF for Costing document with specified document status as parameter
     *
     * @param <CODE>objInvOnDFCostingDoc</CODE>object
     *                                      InventoryOnDFDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objInvOnDFCostingDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusInvOnDFCosting(IjInventoryOnDFDoc objInvOnDFCostingDoc, int iDocStatus) {
        int iResult = 0;
        try {
            MatCosting matCosting = PstMatCosting.fetchExc(objInvOnDFCostingDoc.getDocId());
            matCosting.setCostingStatus(iDocStatus);
            PstMatCosting.updateExc(matCosting);
        } catch (Exception e) {
            System.out.println("error costing : " + e.toString());
        }
        return iResult;
    }
    // --------------- End DF Costing ---------------

    // --------------- Start Customer Return ---------------

    /**
     * this method used to get list Customer Return defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Customer
     *         Return's location
     * @param <CODE>transactionDate</CODE>Customer
     *         Return's transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjCustReturnDoc'
     */
    public Vector getListCustReturn(long lLocationOid, Date transactionDate) {
        Vector vectOfCustReturnDoc = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sTransDate = Formater.formatDate(transactionDate, "yyyy-MM-dd");
            String sql = "select * from " + PstBillMain.TBL_CASH_BILL_MAIN +
                    " where " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR +
                    " and " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + lLocationOid +
                    " and " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + sTransDate + " 00:00:01' AND '" + sTransDate + " 23:59:59'" +
                    " and (" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " or " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_DRAFT + ")";

            System.out.println("get retur >>>> sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjCustReturnDoc objIjCustReturnDoc = new IjCustReturnDoc();
                objIjCustReturnDoc.setLDocId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                objIjCustReturnDoc.setSDocNumber(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                objIjCustReturnDoc.setIPaymentType(I_IJGeneral.TYPE_RET_PAYMENT_REFUND);
                objIjCustReturnDoc.setDTotalCost(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));

                // ini proses pencarian total vaslue
                double totalValue = getTotalInvoice(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                totalValue = totalValue + rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE]);
                totalValue = totalValue + rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE]);
                totalValue = totalValue - rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT]);
                System.out.println("=====>>>>> totalValue : " + totalValue);
                objIjCustReturnDoc.setDTotalValue(totalValue);
                objIjCustReturnDoc.setDtDocDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                objIjCustReturnDoc.setDtLastUpdate(new Date());

                Vector list = getPaymentReturn(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                if (list.size() == 0) {
                    Hashtable has = getAllPaymentSystem();
                    IjPaymentDoc ijPaymentDoc = new IjPaymentDoc();

                    // ini jika ada pembayaran dengan menggunakan dp
                    if (rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CASH_PENDING_ORDER_ID]) != 0) {
                        try {
                            PendingOrder pendingOrder = PstPendingOrder.fetchExc(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CASH_PENDING_ORDER_ID]));
                            objIjCustReturnDoc.setDTotalValue(objIjCustReturnDoc.getDTotalValue() + pendingOrder.getDownPayment());
                        } catch (Exception e) {
                        }
                    }
                    ijPaymentDoc.setPayNominal(objIjCustReturnDoc.getDTotalValue());
                    ijPaymentDoc.setPayCurrency(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]));
                    ijPaymentDoc.setPayId(0);
                    ijPaymentDoc.setPayRate(1);
                    ijPaymentDoc.setPayType(getLongOidPaymentSystem(has, PstCashPayment.paymentType[PstCashPayment.CASH]));
                    list.add(ijPaymentDoc);
                } else {
                    // ini jika ada pembayaran dengan menggunakan dp
                    if (rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CASH_PENDING_ORDER_ID]) != 0) {
                        try {
                            PendingOrder pendingOrder = PstPendingOrder.fetchExc(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CASH_PENDING_ORDER_ID]));
                            //objIjCustReturnDoc.setDTotalValue(objIjCustReturnDoc.getDTotalValue() + pendingOrder.getDownPayment());
                            IjPaymentDoc ijPaymentDoc = (IjPaymentDoc) list.get(0);
                            ijPaymentDoc.setPayNominal(ijPaymentDoc.getPayNominal() + pendingOrder.getDownPayment());
                            list.setElementAt(ijPaymentDoc, 0);
                        } catch (Exception e) {
                        }
                    }
                }

                objIjCustReturnDoc.setListPayment(list);
                objIjCustReturnDoc.setSDocRemark("retur invoice: " + rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                objIjCustReturnDoc.setSDocReturnReason("");
                vectOfCustReturnDoc.add(objIjCustReturnDoc);

                PstDocLogger.insertUpdateDataBo_toDocLogger(objIjCustReturnDoc.getSDocNumber(), I_IJGeneral.TRANS_CUSTOMER_RETURN, objIjCustReturnDoc.getDtLastUpdate(), "Customer return : " + objIjCustReturnDoc.getSDocNumber());
            }
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return vectOfCustReturnDoc;
    }

    public Vector getPaymentReturn(long oidBillMain) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "select * from " + PstCashPayment.TBL_PAYMENT +
                    " where " + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + "=" + oidBillMain;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Hashtable has = getAllPaymentSystem();
            while (rs.next()) {
                IjPaymentDoc ijPaymentDoc = new IjPaymentDoc();
                ijPaymentDoc.setPayNominal(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                ijPaymentDoc.setPayCurrency(rs.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]));
                ijPaymentDoc.setPayId(rs.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]));
                ijPaymentDoc.setPayRate(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]));
                ijPaymentDoc.setPayType(getLongOidPaymentSystem(has, PstCashPayment.paymentType[rs.getInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE])]));
                ijPaymentDoc.setPayNumber("");
                System.out.println("=========>>>>> Payment : " + ijPaymentDoc.getPayNominal());
                list.add(ijPaymentDoc);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err >>> " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static double getTotalInvoice(long oidBillMain) {
        DBResultSet dbrs = null;
        double totalValue = 0.0;
        try {
            String sql = "select sum(total_price) as total from " + PstBillDetail.TBL_CASH_BILL_DETAIL +
                    " where " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidBillMain;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                totalValue = rs.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Err >>> " + e.toString());
        }
        return totalValue;
    }

    /**
     * this method used to get Customer Return document defend on 'objCustReturnDocOid' selected
     *
     * @param <CODE>objIjCustReturnDocOid</CODE>OID
     *         of Customer Return's document
     *         return 'obj com.dimata.ij.ibosys.IjCustReturnDoc'
     */
    public IjCustReturnDoc getCustReturnDoc
            (
                    long objCustReturnDocOid) {
        IjCustReturnDoc objIjCustReturnDoc = new IjCustReturnDoc();
        try {
            PstBillMain objPstBillMain = new PstBillMain();
            BillMain objBillMain = objPstBillMain.fetchExc(objCustReturnDocOid);

            // generate object objIjCustReturnDoc to be stored as a result
            double totalValue = getTotalInvoice(objCustReturnDocOid);
            totalValue = totalValue + objBillMain.getServiceValue();
            totalValue = totalValue + objBillMain.getTaxValue();
            totalValue = totalValue - objBillMain.getDiscount();

            objIjCustReturnDoc.setDTotalValue(totalValue);
            objIjCustReturnDoc.setDtDocDate(objBillMain.getBillDate());
            objIjCustReturnDoc.setDtLastUpdate(new Date());
            objIjCustReturnDoc.setIPaymentType(I_IJGeneral.TYPE_RET_PAYMENT_REFUND);
            objIjCustReturnDoc.setLDocId(objCustReturnDocOid);
            objIjCustReturnDoc.setDtLastUpdate(new Date());

            Vector list = getPaymentReturn(objCustReturnDocOid);
            if (list.size() == 0) {
                Hashtable has = getAllPaymentSystem();
                IjPaymentDoc ijPaymentDoc = new IjPaymentDoc();
                ijPaymentDoc.setPayNominal(objIjCustReturnDoc.getDTotalValue());
                ijPaymentDoc.setPayCurrency(objBillMain.getCurrencyId());
                ijPaymentDoc.setPayId(0);
                ijPaymentDoc.setPayRate(1);
                ijPaymentDoc.setPayType(getLongOidPaymentSystem(has, PstCashPayment.paymentType[PstCashPayment.CASH]));
                list.add(ijPaymentDoc);
            }

            objIjCustReturnDoc.setListPayment(new Vector(1, 1));
            objIjCustReturnDoc.setSDocNumber("");
            objIjCustReturnDoc.setSDocRemark("");
            objIjCustReturnDoc.setSDocReturnReason("");
        } catch (Exception e) {
            System.out.println("err when fetch Dp on Registration Doc : " + e.toString());
        }

        return objIjCustReturnDoc;
    }

    /**
     * this method used to update status Customer Return document with specified document status as parameter
     *
     * @param <CODE>objCustReturnDoc</CODE>object
     *                                      CustReturnDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objCustReturnDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusCustReturn
            (IjCustReturnDoc
                    objCustReturnDoc, int iDocStatus) {
        int iResult = 0;

        // Process updateStatusCustReturn
        // mencari pembayaran dengan parameter :
        //  - bill_id = [objCustReturnDoc.getDocId]
        try {
            PstBillMain objPstBillMain = new PstBillMain();
            BillMain objBillMain = objPstBillMain.fetchExc(objCustReturnDoc.getLDocId());
            objBillMain.setBillStatus(iDocStatus);
            objPstBillMain.updateExc(objBillMain);
            iResult = iDocStatus;
        } catch (Exception e) {
            System.out.println("Err when update customer return status : " + e.toString());
        }

        return iResult;
    }
    // --------------- End Customer Return ---------------

    // --------------- Start Customer Return Deduct ---------------

    /**
     * this method used to get list Customer Return Deduct defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Customer
     *         Return Deduct's location
     * @param <CODE>transactionDate</CODE>Customer
     *         Return Deduct's transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjCustReturnDeductDoc'
     */
    public Vector getListCustReturnDeduct(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }


    /**
     * this method used to get Customer Return Deduct document defend on 'objCustReturnDocOid' selected
     *
     * @param <CODE>objIjCustReturnDeductDocOid</CODE>OID
     *         of Customer Return Deduct's document
     *         return 'obj com.dimata.ij.ibosys.IjCustReturnDeductDoc'
     */
    public IjCustReturnDeductDoc getCustReturnDeductDoc(long objCustReturnDeductDocOid) {
        IjCustReturnDeductDoc objIjCustReturnDeductDoc = new IjCustReturnDeductDoc();

        return objIjCustReturnDeductDoc;
    }

    /**
     * this method used to update status Customer Return Deduct document with specified document status as parameter
     *
     * @param <CODE>objCustReturnDeductDoc</CODE>object
     *                                      CustReturnDeductDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objCustReturnDeductDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusCustReturnDeduct(IjCustReturnDeductDoc objCustReturnDeductDoc, int iDocStatus) {
        int iResult = 0;

        return iResult;
    }
    // --------------- End Customer Return ---------------


    /**
     * this method used to get list Supplier Return defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Supplier
     *         Return's location
     * @param <CODE>transactionDate</CODE>Supplier
     *         Return's transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjSuppReturnDoc'
     */

    public Vector getListSuppReturn(long lLocationOid, Date transactionDate) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        try {
            String sql = "select * from " + PstMatReturn.TBL_MAT_RETURN +
                    " where " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + lLocationOid +
                    " and " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " between '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + " 00:00:01' and '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + " 23:59:59'" +
                    " and " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " and " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] + "=" + PstLocation.TYPE_LOCATION_WAREHOUSE;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjSuppReturnDoc ijSuppReturnDoc = new IjSuppReturnDoc();
                ijSuppReturnDoc.setDtDocDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                ijSuppReturnDoc.setIPaymentType(I_IJGeneral.TYPE_RET_PAYMENT_REFUND);
                ijSuppReturnDoc.setLDocId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]));
                ijSuppReturnDoc.setSDocNumber(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]));
                ijSuppReturnDoc.setSDocRemark(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK]));
                ijSuppReturnDoc.setSReturnReason("");
                ijSuppReturnDoc.setDtLastUpdate(new Date());
                ijSuppReturnDoc.setDTotalValue(getTotalReturn(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID])));

                vResult.add(ijSuppReturnDoc);
                PstDocLogger.insertUpdateDataBo_toDocLogger(ijSuppReturnDoc.getSDocNumber(), I_IJGeneral.TRANS_CUSTOMER_RETURN, ijSuppReturnDoc.getDtLastUpdate(), ijSuppReturnDoc.getSDocNumber());
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err >>> " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }

    /**
     * gadnyana
     * proses pencarian total dari return
     *
     * @param oidReturn
     * @return
     */
    public double getTotalReturn(long oidReturn) {
        DBResultSet dbrs = null;
        double total = 0.0;
        try {
            String sql = "select sum(" + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") " +
                    " from " + PstMatReturnItem.TBL_MAT_RETURN_ITEM +
                    " where " + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturn;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble(1);
            }

            // for handle if value hpp 0
            if (total == 0) {
                sql = "select sum(mat." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + ") " +
                        " from " + PstMaterial.TBL_MATERIAL + " as mat " +
                        " inner join " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " as ri " +
                        " on mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " = ri." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                        " where ri." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturn;
                dbrs = DBHandler.execQueryResult(sql);
                rs = dbrs.getResultSet();
                while (rs.next()) {
                    total = rs.getDouble(1);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("=>>> gettotal return : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }

    /**
     * this method used to get Supplier Return document defend on 'objSuppReturnDocOid' selected
     *
     * @param <CODE>objIjSuppReturnDocOid</CODE>OID
     *         of Supplier Return's document
     *         return 'obj com.dimata.ij.ibosys.IjSuppReturnDoc'
     */
    public IjSuppReturnDoc getSuppReturnDoc
            (
                    long objSuppReturnDocOid) {
        IjSuppReturnDoc objIjSuppReturnDoc = new IjSuppReturnDoc();
        try {
            MatReturn matReturn = PstMatReturn.fetchExc(objSuppReturnDocOid);
            objIjSuppReturnDoc.setLDocId(objSuppReturnDocOid);
            objIjSuppReturnDoc.setSDocNumber(matReturn.getRetCode());
            objIjSuppReturnDoc.setSReturnReason(matReturn.getRemark());
            objIjSuppReturnDoc.setIPaymentType(I_IJGeneral.TYPE_RET_PAYMENT_REFUND);
            objIjSuppReturnDoc.setDTotalValue(getTotalReturn(objSuppReturnDocOid));
            objIjSuppReturnDoc.setDtDocDate(matReturn.getReturnDate());
        } catch (Exception e) {
        }
        return objIjSuppReturnDoc;
    }

    /**
     * this method used to update status Supplier Return document with specified document status as parameter
     *
     * @param <CODE>objSuppReturnDoc</CODE>object
     *                                      SuppReturnDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objSuppReturnDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusSuppReturn
            (IjSuppReturnDoc
                    objSuppReturnDoc, int iDocStatus) {
        int iResult = 0;
        try {
            MatReturn matReturn = PstMatReturn.fetchExc(objSuppReturnDoc.getLDocId());
            matReturn.setReturnStatus(iDocStatus);
            PstMatReturn.updateExc(matReturn);
        } catch (Exception e) {
        }
        return iResult;
    }

    // --------------- End Supplier Return ---------------

    // --------------- Start Supplier Return Deduct ---------------

    /**
     * this method used to get list Supplier Return Deduct defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Supplier
     *         Return Deduct's location
     * @param <CODE>transactionDate</CODE>Supplier
     *         Return Deduct's transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjSuppReturnDeductDoc'
     */
    public Vector getListSuppReturnDeduct(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }

    /**
     * this method used to get Supplier Return Deduct document defend on 'objSuppReturnDocOid' selected
     *
     * @param <CODE>objIjSuppReturnDeductDocOid</CODE>OID
     *         of Supplier Return Deduct's document
     *         return 'obj com.dimata.ij.ibosys.IjSuppReturnDeductDoc'
     */
    public IjSuppReturnDeductDoc getSuppReturnDeductDoc(long objSuppReturnDeductDocOid) {
        IjSuppReturnDeductDoc objIjSuppReturnDeductDoc = new IjSuppReturnDeductDoc();

        return objIjSuppReturnDeductDoc;
    }

    /**
     * this method used to update status Supplier Return Deduct document with specified document status as parameter
     *
     * @param <CODE>objSuppReturnDeductDoc</CODE>object
     *                                      SuppReturnDeductDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objSuppReturnDeductDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusSuppReturnDeduct(IjSuppReturnDeductDoc objSuppReturnDeductDoc, int iDocStatus) {
        int iResult = 0;

        return iResult;
    }
    // --------------- End Supplier Return Deduct ---------------

    // --------------- Start Cancellation ---------------

    /**
     * this method used to get list Cancellation defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Cancellation's
     *         location
     * @param <CODE>transactionDate</CODE>Cancellation's
     *         transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjCancellationDoc'
     */
    public Vector getListCancellation(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }

    /**
     * this method used to get Cancellation document defend on 'objIjCancellationDocOid' selected
     *
     * @param <CODE>objIjCancellationDocOid</CODE>OID
     *         of Cancellation's document
     *         return 'obj com.dimata.ij.ibosys.IjCancellationDoc'
     */
    public IjCancellationDoc getCancellationDoc(long objIjCancellationDocOid) {
        IjCancellationDoc objIjCancellationDoc = new IjCancellationDoc();

        return objIjCancellationDoc;
    }

    /**
     * this method used to update status Cancellation document with specified document status as parameter
     *
     * @param <CODE>objIjCancellationDoc</CODE>object
     *                                      IjCancellationDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objIjCancellationDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusCancellation(IjCancellationDoc objIjCancellationDoc, int iDocStatus) {
        int iResult = 0;

        return iResult;
    }
    // --------------- End Cancellation ---------------

    // --------------- Start Adjustment ---------------

    /**
     * this method used to get list Adjustment defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Adjustment's
     *         location
     * @param <CODE>transactionDate</CODE>Adjustment's
     *         transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjAdjustmentDoc'
     */
    public Vector getListAdjustment(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }

    /**
     * this method used to get Adjustment document defend on 'objIjAdjustmentDocOid' selected
     *
     * @param <CODE>objIjAdjustmentDocOid</CODE>OID
     *         of Adjustment's document
     *         return 'obj com.dimata.ij.ibosys.IjAdjustmentDoc'
     */
    public IjAdjustmentDoc getAdjustmentDoc(long objIjAdjustmentDocOid) {
        IjAdjustmentDoc objIjAdjustmentDoc = new IjAdjustmentDoc();

        return objIjAdjustmentDoc;
    }

    /**
     * this method used to update status Adjustment document with specified document status as parameter
     *
     * @param <CODE>objIjAdjustmentDoc</CODE>object
     *                                      IjAdjustmentDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objIjAdjustmentDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusAdjustment(IjAdjustmentDoc objIjAdjustmentDoc, int iDocStatus) {
        int iResult = 0;

        return iResult;
    }
    // --------------- End Adjustment ---------------

    // --------------- Start Acquisition of Commision ---------------

    /**
     * this method used to get list Acquisition of Commision defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Commision's
     *         location
     * @param <CODE>transactionDate</CODE>Commision's
     *         transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjCommisionDoc'
     */
    public Vector getListCommision(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);

        return vResult;
    }

    /**
     * this method used to get Acquisition of Commision document defend on 'objIjCommisionDocOid' selected
     *
     * @param <CODE>objIjCommisionDocOid</CODE>OID
     *         of Commision's document
     *         return 'obj com.dimata.ij.ibosys.IjCommisionDoc'
     */
    public IjCommisionDoc getCommisionDoc(long objIjCommisionDocOid) {
        IjCommisionDoc objIjCommisionDoc = new IjCommisionDoc();

        return objIjCommisionDoc;
    }

    /**
     * this method used to update status Acquisition of Commision document with specified document status as parameter
     *
     * @param <CODE>objIjCommisionDoc</CODE>object
     *                                      IjCommisionDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objIjCommisionDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusCommision(IjCommisionDoc objIjCommisionDoc, int iDocStatus) {
        int iResult = 0;

        return iResult;
    }
    // --------------- End Acquisition of Commision ---------------

    // --------------- Start Payment on Commision ---------------

    /**
     * this method used to get list Payment on Commision defend on 'location', 'date' and of transaction selected by user
     *
     * @param <CODE>lLocationOid</CODE>Commision's
     *         location
     * @param <CODE>transactionDate</CODE>Commision's
     *         transaction date
     *         return 'vector of obj com.dimata.ij.ibosys.IjCommisionDoc'
     */
    public Vector getListPaymentOnCommision(long lLocationOid, Date transactionDate) {
        Vector vResult = new Vector(1, 1);
        return vResult;
    }

    /**
     * this method used to get Payment on Commision document defend on 'objIjCommisionDocOid' selected
     *
     * @param <CODE>objIjCommisionDocOid</CODE>OID
     *         of Commision's document
     *         return 'obj com.dimata.ij.ibosys.IjPaymentOnCommisionDoc'
     */
    public IjPaymentOnCommisionDoc getPaymentOnCommisionDoc(long objIjPaymentOnCommisionDocOid) {
        IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc = new IjPaymentOnCommisionDoc();
        return objIjPaymentOnCommisionDoc;
    }

    /**
     * this method used to update status Payment on Commision document with specified document status as parameter
     *
     * @param <CODE>objIjPaymentOnCommisionDoc</CODE>object
     *                                      IjPaymentOnCommisionDoc whose status will updated
     * @param <CODE>iDocStatus</CODE>status of objIjPaymentOnCommisionDoc
     *                                      return 'status of updated process'
     */
    public int updateStatusPaymentOnCommision(IjPaymentOnCommisionDoc objIjPaymentOnCommisionDoc, int iDocStatus) {
        int iResult = 0;
        return iResult;
    }

// ***** --------- Finish implement I_BOSystem
}
