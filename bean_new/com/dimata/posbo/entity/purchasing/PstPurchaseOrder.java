package com.dimata.posbo.entity.purchasing;

/* package java */

import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.common.session.email.SessEmail;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.session.purchasing.*;
import com.dimata.posbo.session.warehouse.SessMatReceive;
import org.json.JSONObject;
/**
 * update category
 * add opie-eyek 20140804
 * @author dimata005
 */
public class PstPurchaseOrder extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_PURCHASE_ORDER = "pos_purchase_order";

    public static final int FLD_PURCHASE_ORDER_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_LOCATION_TYPE = 2;
    public static final int FLD_PO_CODE = 3;
    public static final int FLD_PO_CODE_COUNTER = 4;
    public static final int FLD_PURCH_DATE = 5;
    public static final int FLD_SUPPLIER_ID = 6;
    public static final int FLD_PO_STATUS = 7;
    public static final int FLD_REMARK = 8;

    public static final int FLD_TERMS_OF_PAYMENT = 9;
    public static final int FLD_CREDIT_TIME = 10;
    public static final int FLD_PPN = 11;
    public static final int FLD_CURRENCY_ID = 12;
    public static final int FLD_CODE_REVISI = 13;

    //new Include PPn
    public static final int FLD_INCLUDE_PPN = 14;
    //exchange rate
    public static final int FLD_EXCHANGE_RATE = 15;
    
    //public
    public static final int FLD_CATEGORY_ID = 16;
    
    public static final String[] fieldNames =
            {
                "PURCHASE_ORDER_ID",
                "LOCATION_ID",
                "LOCATION_TYPE",
                "PO_CODE",
                "PO_CODE_COUNTER",
                "PURCH_DATE",
                "SUPPLIER_ID",
                "PO_STATUS",
                "REMARK",
                "TERMS_OF_PAYMENT",
                "CREDIT_TIME",
                "PPN",
                "CURRENCY_ID",
                "REVISI_CODE",
                //new include ppn
                "INCLUDE_PPN",
                "EXCHANGE_RATE",
                "CATEGORY_ID"
            };

    public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_LONG,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_DATE,
                TYPE_LONG,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_STRING,
                //include ppn
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_LONG
            };

    public static final int DATASTATUS_CLEAN = 0;
    public static final int DATASTATUS_ADD = 1;
    public static final int DATASTATUS_UPDATE = 2;
    public static final int DATASTATUS_DELETE = 3;

    // --------- PO STATUS --------
    public static final int PO_STATUS_DRAFT = 0;
    public static final int PO_STATUS_FINAL = 1;
    public static final int PO_STATUS_REVISED = 2;
    public static final int PO_STATUS_CLOSED = 3;
    public static final int PO_STATUS_CANCELED = 4;
    public static final String[] fieldPoStatus =
            {
                "DRAFT",
                "FINAL",
                "REVISED",
                "CLOSED",
                "CANCELED"
            };

    // PO Payment Type
    public static final int PO_PAYMENT_TYPE_CASH = 0;
    public static final int PO_PAYMENT_TYPE_CREDIT = 1;
    public static final String[] fieldsPaymentType =
            {
                "Cash",
                "Credit"
            };
    
     // PO Payment Type
    public static final int PO_APPROVAL_ONGOING = 0;
    public static final int PO_APPROVAL_YES = 1;
    public static final int PO_APPROVAL_NO = 2;
    public static final String[] fieldsApprovalType =
    {
        "None",
        "Yes",
        "No"
    };
    
     // PO Payment Type
    public static final int PR_DIRECT = 0;
    public static final int PR_SUPPLIER_CASH = 1;
    public static final int PR_SUPPLIER_CREDIT = 2;
    public static final String[] fieldsPurchaseRequestType =
    {
        "Direct",
        "Supplier Cash",
        "Supplier Credit"
    };
     // PO Payment Type For Radit
    public static final int SUPPLIER_CASH = 1;
    public static final int SUPPLIER_CREDIT = 2;
    public static final String[] purchaseRequestType =
    {
        "Supplier Cash",
        "Supplier Credit"
    };
    public PstPurchaseOrder() {
    }

    public PstPurchaseOrder(int i) throws DBException {
        super(new PstPurchaseOrder());
    }

    public PstPurchaseOrder(String sOid) throws DBException {
        super(new PstPurchaseOrder(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPurchaseOrder(long lOid) throws DBException {
        super(new PstPurchaseOrder(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_PURCHASE_ORDER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPurchaseOrder().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PurchaseOrder po = fetchExc(ent.getOID());
        ent = (Entity) po;
        return po.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PurchaseOrder) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PurchaseOrder) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PurchaseOrder fetchExc(long oid) throws DBException {
        try {
            PurchaseOrder po = new PurchaseOrder();
            PstPurchaseOrder pstpo = new PstPurchaseOrder(oid);
            po.setOID(oid);

            po.setLocationId(pstpo.getlong(FLD_LOCATION_ID));
            po.setLocationType(pstpo.getInt(FLD_LOCATION_TYPE));
            po.setPoCode(pstpo.getString(FLD_PO_CODE));
            po.setPoCodeCounter(pstpo.getInt(FLD_PO_CODE_COUNTER));
            po.setPurchDate(pstpo.getDate(FLD_PURCH_DATE));
            po.setSupplierId(pstpo.getlong(FLD_SUPPLIER_ID));
            po.setPoStatus(pstpo.getInt(FLD_PO_STATUS));
            po.setRemark(pstpo.getString(FLD_REMARK));

            po.setTermOfPayment(pstpo.getInt(FLD_TERMS_OF_PAYMENT));
            po.setCreditTime(pstpo.getInt(FLD_CREDIT_TIME));
            po.setPpn(pstpo.getdouble(FLD_PPN));
            po.setCurrencyId(pstpo.getlong(FLD_CURRENCY_ID));
            po.setCodeRevisi(pstpo.getString(FLD_CODE_REVISI));
            
            //include ppn
            po.setIncludePpn(pstpo.getInt(FLD_INCLUDE_PPN));
            //rate
            po.setExchangeRate(pstpo.getdouble(FLD_EXCHANGE_RATE));
            //category
            po.setCategoryId(pstpo.getlong(FLD_CATEGORY_ID));
            return po;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PurchaseOrder po) throws DBException {
        try {
            PstPurchaseOrder pstpo = new PstPurchaseOrder(0);

            pstpo.setLong(FLD_LOCATION_ID, po.getLocationId());
            pstpo.setInt(FLD_LOCATION_TYPE, po.getLocationType());
            pstpo.setString(FLD_PO_CODE, po.getPoCode());
            pstpo.setInt(FLD_PO_CODE_COUNTER, po.getPoCodeCounter());
            pstpo.setDate(FLD_PURCH_DATE, po.getPurchDate());
            pstpo.setLong(FLD_SUPPLIER_ID, po.getSupplierId());
            pstpo.setInt(FLD_PO_STATUS, po.getPoStatus());
            pstpo.setString(FLD_REMARK, po.getRemark());

            pstpo.setInt(FLD_TERMS_OF_PAYMENT, po.getTermOfPayment());
            pstpo.setInt(FLD_CREDIT_TIME, po.getCreditTime());
            pstpo.setDouble(FLD_PPN, po.getPpn());
            pstpo.setLong(FLD_CURRENCY_ID, po.getCurrencyId());
            pstpo.setString(FLD_CODE_REVISI, po.getCodeRevisi());
            //include ppn
            pstpo.setInt(FLD_INCLUDE_PPN, po.getIncludePpn());
            pstpo.setDouble(FLD_EXCHANGE_RATE, po.getExchangeRate());
            
            pstpo.setLong(FLD_CATEGORY_ID, po.getCategoryId());
            
            pstpo.insert();
            po.setOID(pstpo.getlong(FLD_PURCHASE_ORDER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
        return po.getOID();
    }

    public static long updateExc(PurchaseOrder po) throws DBException {
        try {
            if (po.getOID() != 0) {
                PstPurchaseOrder pstpo = new PstPurchaseOrder(po.getOID());

                pstpo.setLong(FLD_LOCATION_ID, po.getLocationId());
                pstpo.setInt(FLD_LOCATION_TYPE, po.getLocationType());
                pstpo.setString(FLD_PO_CODE, po.getPoCode());
                pstpo.setInt(FLD_PO_CODE_COUNTER, po.getPoCodeCounter());
                pstpo.setDate(FLD_PURCH_DATE, po.getPurchDate());
                pstpo.setLong(FLD_SUPPLIER_ID, po.getSupplierId());
                pstpo.setInt(FLD_PO_STATUS, po.getPoStatus());
                pstpo.setString(FLD_REMARK, po.getRemark());

                pstpo.setInt(FLD_TERMS_OF_PAYMENT, po.getTermOfPayment());
                pstpo.setInt(FLD_CREDIT_TIME, po.getCreditTime());
                pstpo.setDouble(FLD_PPN, po.getPpn());
                pstpo.setLong(FLD_CURRENCY_ID, po.getCurrencyId());
                pstpo.setString(FLD_CODE_REVISI, po.getCodeRevisi());

                //include ppn
                pstpo.setInt(FLD_INCLUDE_PPN, po.getIncludePpn());
                pstpo.setDouble(FLD_EXCHANGE_RATE, po.getExchangeRate());
                
                pstpo.setLong(FLD_CATEGORY_ID, po.getCategoryId());
                pstpo.update();
                return po.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPurchaseOrder pstpo = new PstPurchaseOrder(oid);
            //Delete Item First
            PstPurchaseOrderItem pstPOItem = new PstPurchaseOrderItem();
            long result = pstPOItem.deleteExcByParent(oid);
            pstpo.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PURCHASE_ORDER;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PurchaseOrder po = new PurchaseOrder();
                resultToObject(rs, po);
                lists.add(po);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //Untuk list PO
    public static Vector list(String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT PO." + fieldNames[FLD_PURCHASE_ORDER_ID] +
                    ", PO." + fieldNames[FLD_PO_CODE] +
                    ", PO." + fieldNames[FLD_PURCH_DATE] +
                    ", PO." + fieldNames[FLD_PO_STATUS] +
                    ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_REMARK] +
                    ", PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CURRENCY_ID] +
                    " FROM " + TBL_PURCHASE_ORDER + " AS PO" +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CNT " +
                    " ON PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID] +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                ContactList contactList = new ContactList();
                Vector vt = new Vector(1, 1);

                purchaseOrder.setOID(rs.getLong(1));
                purchaseOrder.setPoCode(rs.getString(2));
                purchaseOrder.setPurchDate(rs.getDate(3));
                purchaseOrder.setPoStatus(rs.getInt(4));
                purchaseOrder.setRemark(rs.getString(6));
                purchaseOrder.setCurrencyId(rs.getLong(7));
                vt.add(purchaseOrder);

                contactList.setCompName(rs.getString(5));
                vt.add(contactList);

                lists.add(vt);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static void resultToObject(ResultSet rs, PurchaseOrder po) {
        try {
            po.setOID(rs.getLong(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID]));
            po.setLocationId(rs.getLong(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_LOCATION_ID]));
            po.setLocationType(rs.getInt(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_LOCATION_TYPE]));
            po.setPoCode(rs.getString(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]));
            po.setPoCodeCounter(rs.getInt(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE_COUNTER]));
            po.setPurchDate(rs.getDate(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]));
            po.setSupplierId(rs.getLong(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID]));
            po.setPoStatus(rs.getInt(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]));
            po.setRemark(rs.getString(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_REMARK]));

            po.setCreditTime(rs.getInt(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CREDIT_TIME]));
            po.setTermOfPayment(rs.getInt(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_TERMS_OF_PAYMENT]));
            po.setPpn(rs.getDouble(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PPN]));
            po.setCurrencyId(rs.getLong(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CURRENCY_ID]));
            po.setCodeRevisi(rs.getString(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CODE_REVISI]));
            //include ppn
            po.setIncludePpn(rs.getInt(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_INCLUDE_PPN]));
            
            po.setExchangeRate(rs.getDouble(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_EXCHANGE_RATE]));
            
            po.setCategoryId(rs.getLong(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CATEGORY_ID]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long poId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PURCHASE_ORDER + " WHERE " +
                    PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID] + " = " + poId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_PURCHASE_ORDER_ID] +
                    ") FROM " + TBL_PURCHASE_ORDER;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PurchaseOrder po = (PurchaseOrder) list.get(ls);
                    if (oid == po.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0)
            cmd = Command.FIRST;
        else {
            if (start == (vectSize - recordToGet))
                cmd = Command.LAST;
            else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    //System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        //System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }

    /*-------------------- start implements I_Document -------------------------*/
    /**
     * this method used to get status of 'document'
     * return int of currentDocumentStatus
     */
    public int getDocumentStatus(long documentId) {
        DBResultSet dbrs = null;
        try {
            PurchaseOrder orderMaterial = fetchExc(documentId);
            return orderMaterial.getPoStatus();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return -1;
    }

    /**
     * this method used to set status of 'document'
     * return int of currentDocumentStatus
     */
    public int setDocumentStatus(long documentId, int indexStatus) {
        /*DBResultSet dbrs = null;
	try
        {
            String sql = "UPDATE " + PstPurchaseOrder.TBL_PURCHASE_ORDER +
                " SET " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = " + indexStatus +
                " WHERE " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID] + " = " + documentId;
            dbrs = DBHandler.execQueryResult(sql);*/
        return indexStatus;
        /*}
catch(Exception e)
{
System.out.println("Err : "+e.toString());
        }
finally
{
DBResultSet.close(dbrs);
        }
return -1;*/
    }


    public static long AutoInsertPo(Vector listItem, long oidLocation, long oidSupplier, int locType) {
        long hasil = 0;
        try {
            PurchaseOrder mypo = new PurchaseOrder();
            mypo.setPoStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            mypo.setPurchDate(new Date());
            mypo.setSupplierId(oidSupplier);
            mypo.setLocationId(oidLocation);
            mypo.setLocationType(locType);
            mypo.setRemark("");
            mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
            mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));
            //Insert purchase order
            hasil = PstPurchaseOrder.insertExc(mypo);
            if (hasil != 0) {
                //Insert purchase order item
                for (int i = 0; i < listItem.size(); i++) {
                    long oidPOItem = 0;
                    Vector temp = (Vector) listItem.get(i);
                    String sku = (String) temp.get(0);
                    if (sku.length() > 0) {
                        //Fetch material info
                        Material myMat = PstMaterial.fetchBySku(sku);
                        //Cek supplier utk material tsb, jika tidak sama lewati saja
                        if (myMat.getSupplierId() == oidSupplier) {
                            int quantity = Integer.parseInt((String) temp.get(1));
                            double discount = 0.00;
                            PurchaseOrderItem poItem = new PurchaseOrderItem();
                            poItem.setPurchaseOrderId(hasil);
                            poItem.setMaterialId(myMat.getOID());
                            poItem.setUnitId(myMat.getDefaultStockUnitId());
                            poItem.setQuantity(quantity);
                            poItem.setPrice(myMat.getDefaultCost());
                            poItem.setCurrencyId(myMat.getDefaultCostCurrencyId());
                            poItem.setDiscount(discount);
                            poItem.setTotal((quantity * myMat.getDefaultCost()) * (1 - (discount / 100)));
                            oidPOItem = PstPurchaseOrderItem.insertExc(poItem);
                        } else {
                            oidPOItem = 1;
                        }
                    } else {
                        oidPOItem = 1;
                    }
                    if (oidPOItem == 0) break;
                }
            }
        } catch (Exception ex) {
            System.out.println("AutoInsertPo : " + ex);
        }
        return hasil;
    }


    public static Vector getRekapForPurchaseMain(Vector list) {
        Vector vpo = new Vector();
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmaterial = Long.parseLong((String) v1.get(0));
                    double qty = Double.parseDouble((String) v1.get(1));
                    long oidlokasi = Long.parseLong((String) v1.get(2));
                    long oidsupplier = Long.parseLong((String) v1.get(3));
                    long oidUnit = ((Long) v1.get(4)).longValue();
                    if (vpo.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vpo.size(); i++) {
                            bool = false;
                            PurchaseOrder purchOrder = (PurchaseOrder) vpo.get(i);
                            if ((purchOrder.getLocationId() == oidlokasi) && (purchOrder.getSupplierId() == oidsupplier)) {
                                bool = true;
                                PurchaseOrderItem poItem = new PurchaseOrderItem();
                                poItem.setMaterialId(oidmaterial);
                                poItem.setQuantity(qty);
                                MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                                //purchOrder.setCurrencyId(matVendorPrice.getPriceCurrency());
                                poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                                poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
                                poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                                poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                                poItem.setDiscount(matVendorPrice.getLastDiscount());
                                poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                                poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                                poItem.setUnitId(oidUnit);
                                purchOrder.setListItem(poItem);
                                vpo.setElementAt(purchOrder, i);
                            }
                        }
                        if (!bool) {
                            PurchaseOrder mypo = new PurchaseOrder();
                            mypo.setPoStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            mypo.setPurchDate(new Date());
                            mypo.setSupplierId(oidsupplier);
                            mypo.setLocationId(oidlokasi);
                            mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                            mypo.setRemark("");
                            //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                            //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                            PurchaseOrderItem poItem = new PurchaseOrderItem();
                            poItem.setMaterialId(oidmaterial);
                            poItem.setQuantity(qty);
                            MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                            mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                            poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                            poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
                            poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                            poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                            poItem.setDiscount(matVendorPrice.getLastDiscount());
                            poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                            poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                            poItem.setUnitId(oidUnit);
                            mypo.setListItem(poItem);
                            vpo.add(mypo);
                        }
                    } else {
                        PurchaseOrder mypo = new PurchaseOrder();
                        mypo.setPoStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                        mypo.setPurchDate(new Date());
                        mypo.setSupplierId(oidsupplier);
                        mypo.setLocationId(oidlokasi);
                        mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                        mypo.setRemark("");
                        //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                        //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                        PurchaseOrderItem poItem = new PurchaseOrderItem();
                        poItem.setMaterialId(oidmaterial);
                        poItem.setQuantity(qty);
                        MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                        mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                        poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                        poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
                        poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                        poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                        poItem.setDiscount(matVendorPrice.getLastDiscount());
                        poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                        poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                        poItem.setUnitId(oidUnit);
                        mypo.setListItem(poItem);
                        vpo.add(mypo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vpo;
    }

    public static long autoInsertPo(Vector list) {
        long hasil = 0;
        try {
            //hasil di urutkan terlebih dahulu berdasarkan supplier
            Vector listOrderSupllier = structureListOrderSupplier(list);
            
            //proses
            Vector vPO = getRekapForPurchaseMain(listOrderSupllier);
            if (vPO.size() > 0) {
                for (int i = 0; i < vPO.size(); i++) {
                    PurchaseOrder purchOrder = (PurchaseOrder) vPO.get(i);
                    purchOrder.setPoCodeCounter(SessPurchaseOrder.getIntCode(purchOrder, new Date(), 0, 0, true));
                    purchOrder.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(purchOrder));
                    Vector v1 = (Vector)purchOrder.getListItem();
                    long oidPo = PstPurchaseOrder.insertExc(purchOrder);
                    System.out.println("=>> INSERT MAIN PO");
                    if (oidPo != 0) {
                        for (int k = 0; k < v1.size(); k++) {
                            PurchaseOrderItem purchOrderItem = (PurchaseOrderItem) v1.get(k);
                            purchOrderItem.setPurchaseOrderId(oidPo);
                            purchOrderItem.setOrgBuyingPrice(purchOrderItem.getPrice());                            
                            PstPurchaseOrderItem.insertExc(purchOrderItem);
                           // System.out.println("=====>> INSERT ITEM "+k);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("autoInsertPo : " + ex);
        }
        return hasil;
    }
    
    
    public static Vector structureListOrderSupplier(Vector list){
        
        if(list == null || list.size()<1){
            return  new Vector();
        }
        
        Vector resultTotal = new Vector();
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidsupplier = Long.parseLong((String) v1.get(3));
                    long oidlokasi = Long.parseLong((String) v1.get(2));
                    if (resultTotal.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < resultTotal.size(); i++) {
                            bool = false;
                            Vector v2 = (Vector) resultTotal.get(i);
                            long oidsupplier2 = Long.parseLong((String) v2.get(3));
                            long oidlokasi2 = Long.parseLong((String) v2.get(2));
                            if ((oidsupplier==oidsupplier2)&&(oidlokasi==oidlokasi2)) {
                                bool = true;
                                resultTotal.insertElementAt(v1, i+1);
                                break;
                            }
                        }
                        if (!bool) {
                            resultTotal.add(v1);
                            //list.remove(k);
                        }
                    } else {
                        resultTotal.add(v1);
                        //list.remove(k);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return resultTotal;
      }
    
    /**
     * membuat po dengan status draft dari pr dengan term of payment credit
     * @param list
     * @return 
     */
     public static long autoInsertPoFromPr(Vector list, Vector listPr,  long vSelected) {
        long hasil = 0;
        Vector insertPO = new  Vector();
        try {
            String poNumber="PO NUMBER CREATE : ";
            Vector vPO = getRekapForPurchaseMainFromPr(list);
            String keterangan = "";
            try {
                PurchaseRequest pr=PstPurchaseRequest.fetchExc(vSelected);
                keterangan = "PO dari ["+pr.getPrCode()+"]";
            } catch (Exception exc){}
            if (vPO.size() > 0) {
                for (int i = 0; i < vPO.size(); i++) {
                    PurchaseOrder purchOrder = (PurchaseOrder) vPO.get(i);
                    int code = SessPurchaseOrder.getIntCode(purchOrder, new Date(), 0, 0, true);
                    purchOrder.setPoCodeCounter(code);
                    String code1 = SessPurchaseOrder.getCodeOrderMaterial(purchOrder);
                    purchOrder.setPoCode(code1);
                    
                    if(purchOrder.getTermOfPayment()==PstPurchaseOrder.PO_PAYMENT_TYPE_CREDIT){
                        int termOfPayment = PstContactList.getDayOfPaymentOfSupplier(purchOrder.getSupplierId());
                        purchOrder.setCreditTime(termOfPayment);
                    }
                    
                    purchOrder.setRemark(keterangan);
                    Vector v1 = (Vector)purchOrder.getListItem();
                    long oidPo = PstPurchaseOrder.insertExc(purchOrder);
                    poNumber=poNumber+" "+purchOrder.getPoCode()+", ";
                    purchOrder.setOID(oidPo);
                    insertPO.add(purchOrder);
                    if (oidPo != 0) {
                        for (int k = 0; k < v1.size(); k++) {
                            PurchaseOrderItem purchOrderItem = (PurchaseOrderItem) v1.get(k);
                            Vector listItemPo = PstPurchaseOrderItem.list(0, 0, PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_MATERIAL_ID]+"="+purchOrderItem.getMaterialId()
                                    +" AND "+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPo, "");
                            if (listItemPo.size()>0){
                                PurchaseOrderItem nwPurchOrderItem = (PurchaseOrderItem) listItemPo.get(0);
                                nwPurchOrderItem.setQuantity(nwPurchOrderItem.getQuantity()+purchOrderItem.getQuantity());
                                nwPurchOrderItem.setQtyInputStock(nwPurchOrderItem.getQtyInputStock()+purchOrderItem.getQtyInputStock());
                                nwPurchOrderItem.setQtyRequest(nwPurchOrderItem.getQtyRequest()+purchOrderItem.getQtyRequest());
                                PstPurchaseOrderItem.updateExc(nwPurchOrderItem);
                            } else {
                                purchOrderItem.setPurchaseOrderId(oidPo);
                                purchOrderItem.setOrgBuyingPrice(purchOrderItem.getPrice());                            
                                PstPurchaseOrderItem.insertExc(purchOrderItem);
                            }
                            
                        }
                    }
                    
                }
            }
            
            PstPurchaseRequest.updateDocumentPR(listPr,vSelected,poNumber);
            
            //sent email
            int sentNotif =Integer.parseInt(PstSystemProperty.getValueByName("POS_EMAIL_NOTIFICATION"));
            if(sentNotif==1){
                if (insertPO.size() > 0) {
                    for (int i = 0; i < insertPO.size(); i++) {
                            PurchaseOrder purchOrder = (PurchaseOrder) vPO.get(i);
                            
                            //get email supplier
                            ContactList contactSupp = new ContactList();
                            Location locationOrder = new Location();
                            
                            SessFormatEmailQueenTandoor sessFormatEmailQueenTandoor = new SessFormatEmailQueenTandoor();
                            String toEmail ="";
                            String contentEmailItem="";
   
                            try{
                                contactSupp =PstContactList.fetchExc(purchOrder.getSupplierId());
                                toEmail=contactSupp.getEmail();
                            }catch(Exception ex){}
                            
                            if(!"".equals(toEmail)){
                                try{
                                    locationOrder =PstLocation.fetchExc(purchOrder.getLocationId());
                                }catch(Exception ex){}

                                Vector listItemOrder = new Vector();
                                try{
                                    String whereClause = " POI."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID]+"='"+purchOrder.getOID()+"'";
                                    listItemOrder= PstPurchaseOrderItem.listRequestPo(0,0,whereClause); 
                                }catch(Exception ex){}

                                try{
                                    contentEmailItem = sessFormatEmailQueenTandoor.getContentEmail(contactSupp,locationOrder,purchOrder,listItemOrder);

                                }catch(Exception ex){}

                                SessEmail sessEmail = new SessEmail();

                                String kirim = sessEmail.sendEamil(toEmail, "Purchase Order - "+locationOrder.getName()+" - "+purchOrder.getPoCode(), contentEmailItem, "");
                            }
                           
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("autoInsertPo : " + ex);
        }
        return hasil;
    }
     
     /**
      * membuat po dengan status draft dari pr dengan term of payment cash
      * @param list
      * @return 
      */
     public static long autoInsertReceiveWithoutPOFromPr(Vector list) {
        long hasil = 0;
        try {
            Vector vRec = getRekapForReceiveWithoutPOMain(list);
            if (vRec.size() > 0) {
                for (int i = 0; i < vRec.size(); i++) {
                    //PurchaseOrder purchOrder = (PurchaseOrder) vPO.get(i);
                    MatReceive matReceive = (MatReceive)vRec.get(i);
                    // matReceive.setPoCodeCounter(SessPurchaseOrder.getIntCode(purchOrder, new Date(), 0, 0, true));
                    matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, matReceive.getReceiveDate(), matReceive.getOID(), 0, 0, true));
                    matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                    
                    Vector v1 = (Vector)matReceive.getListItem();
                    long oidRec = PstMatReceive.insertExc(matReceive);
                   // System.out.println("=>> INSERT MAIN PO");
                    if (oidRec != 0) {
                        for (int k = 0; k < v1.size(); k++) {
                            //PurchaseOrderItem purchOrderItem = (PurchaseOrderItem) v1.get(k);
                            MatReceiveItem recItem =(MatReceiveItem) v1.get(k);
                            recItem.setReceiveMaterialId(oidRec);
                            //recItem.setOrgBuyingPrice(recItem.getCost());                            
                            PstMatReceiveItem.insertExc(recItem);
                           // System.out.println("=====>> INSERT ITEM "+k);
                            //update status item yes or no and note
                            
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("autoInsertPo : " + ex);
        }
        return hasil;
    }
     
     /**
      * membuat vector untuk menamping data penerimaan tanpa po
      */
     public static Vector getRekapForReceiveWithoutPOMain(Vector list) {
        Vector vrec = new Vector();
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmaterial = Long.parseLong((String) v1.get(0));
                    double qty = Double.parseDouble((String) v1.get(1));
                    long oidlokasi = Long.parseLong((String) v1.get(2));
                    long oidsupplier = Long.parseLong((String) v1.get(3));
                    long oidUnit = ((Long) v1.get(4)).longValue();
                    if (vrec.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vrec.size(); i++) {
                            bool = false;
                            //PurchaseOrder purchOrder = (PurchaseOrder) vrec.get(i);
                            MatReceive matReceive = (MatReceive)vrec.get(i);
                            if ((matReceive.getLocationId() == oidlokasi) && (matReceive.getSupplierId() == oidsupplier)) {
                                bool = true;
                                MatReceiveItem recItem = new MatReceiveItem();
                                //PurchaseOrderItem poItem = new PurchaseOrderItem();
                                recItem.setMaterialId(oidmaterial);
                                recItem.setQty(qty);
                                MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                                //purchOrder.setCurrencyId(matVendorPrice.getPriceCurrency());
                                recItem.setUnitId(matVendorPrice.getBuyingUnitId());
                                recItem.setCost(matVendorPrice.getCurrBuyingPrice());
                                //poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice()); ??? ini apa di receive
                                recItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                                recItem.setDiscount(matVendorPrice.getLastDiscount());
                                recItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                                recItem.setCurrBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                                recItem.setUnitId(oidUnit);
                                matReceive.setListItem(recItem);
                                vrec.setElementAt(matReceive, i);
                            }
                        }
                        if (!bool) {
                            //PurchaseOrder mypo = new PurchaseOrder();
                            MatReceive myRec = new MatReceive();
                            myRec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            myRec.setReceiveDate(new Date());
                            myRec.setSupplierId(oidsupplier);
                            myRec.setLocationId(oidlokasi);
                            myRec.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                            myRec.setRemark(" Automatic generate by sistem with email approval ");
                            myRec.setInvoiceSupplier("-");
                            
                            //PurchaseOrderItem poItem = new PurchaseOrderItem();
                            MatReceiveItem recItem = new MatReceiveItem();
                            recItem.setMaterialId(oidmaterial);
                            recItem.setQty(qty);
                            
                            MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                            myRec.setCurrencyId(matVendorPrice.getPriceCurrency());
                            
                            recItem.setUnitId(matVendorPrice.getBuyingUnitId());
                            recItem.setCost(matVendorPrice.getCurrBuyingPrice());
                            //recItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice()); ini apa di receive
                            recItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                            recItem.setDiscount(matVendorPrice.getLastDiscount());
                            recItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                            recItem.setCurrBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                            recItem.setUnitId(oidUnit);
                            
                            myRec.setListItem(recItem);
                            
                            vrec.add(myRec);
                        }
                    } else {
                        //PurchaseOrder mypo = new PurchaseOrder();
                        MatReceive myRec = new MatReceive();
                        myRec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                        myRec.setReceiveDate(new Date());
                        myRec.setSupplierId(oidsupplier);
                        myRec.setLocationId(oidlokasi);
                        myRec.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                        myRec.setRemark(" Automatic generate by sistem with email approval ");
                        myRec.setInvoiceSupplier("-");
                        //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                        //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                        //PurchaseOrderItem poItem = new PurchaseOrderItem();
                        MatReceiveItem recItem = new MatReceiveItem();
                        recItem.setMaterialId(oidmaterial);
                        recItem.setQty(qty);
                        MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                        myRec.setCurrencyId(matVendorPrice.getPriceCurrency());
                        recItem.setUnitId(matVendorPrice.getBuyingUnitId());
                        recItem.setCost(matVendorPrice.getCurrBuyingPrice());
                        //recItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                        recItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                        recItem.setDiscount(matVendorPrice.getLastDiscount());
                        recItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                        recItem.setCurrBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                        recItem.setUnitId(oidUnit);
                        myRec.setListItem(recItem);
                        vrec.add(myRec);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vrec;
    }
     
     
     
     
    public static Vector getRekapForPurchaseMainFromPr(Vector list) {
        Vector vpo = new Vector();
        long oidNewSupplier =Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmaterial = Long.parseLong((String) v1.get(0));
                    
                    long oidUnit = ((Long) v1.get(4)).longValue();
                    
                    long unitRequestId = ((Long) v1.get(12)).longValue();
                    
                    double qtyRequest = Double.parseDouble((String) v1.get(13));
                    
                    double valueKonfersi =  PstUnit.getQtyPerBaseUnit(unitRequestId,oidUnit);
                     
                    double totalQty=valueKonfersi*qtyRequest;
                    
                    double qty = totalQty;
                    
                    long oidlokasi = Long.parseLong((String) v1.get(2));
                    
                    long oidsupplier = Long.parseLong((String) v1.get(3));
                    
                    double priceKonv = Double.parseDouble((String) v1.get(7));
                    
                    double price = priceKonv/valueKonfersi;
                    
                    //double totalPrice = Double.parseDouble((String) v1.get(8));
                    int termOfPayment = 0;//Integer.parseInt((String) v1.get(8));
                    
                    //term po
                    int termPO = Integer.parseInt((String) v1.get(14));
                    
                    String nameSupplier = "";
                    if(oidNewSupplier==oidsupplier){
                            nameSupplier = nameSupplier + " New Supplier : "+String.valueOf((String) v1.get(9));
                    }
                    int status =0;
                    
                    long currencyId=Long.parseLong((String) v1.get(10));
                    double exchangeRate = Double.parseDouble((String) v1.get(11));
                    
                    //unit request dan unit qty
                    
                    if (vpo.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vpo.size(); i++) {
                            bool = false;
                            PurchaseOrder purchOrder = (PurchaseOrder) vpo.get(i);
                            
                                if ((purchOrder.getLocationId() == oidlokasi) && (purchOrder.getSupplierId() == oidsupplier) && (purchOrder.getTermOfPayment() == termPO)) {
                                    bool = true;
                                    PurchaseOrderItem poItem = new PurchaseOrderItem();
                                    poItem.setMaterialId(oidmaterial);
                                    poItem.setQuantity(qty);
                                    MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                                    //purchOrder.setCurrencyId(matVendorPrice.getPriceCurrency());
                                    poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                                    poItem.setPrice(price);
                                    poItem.setOrgBuyingPrice(price);
                                    poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                                    poItem.setDiscount(matVendorPrice.getLastDiscount());
                                    poItem.setTotal((qty * price) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                                    poItem.setCurBuyingPrice(price);
                                    poItem.setUnitId(oidUnit);
                                    
                                    poItem.setUnitRequestId(unitRequestId);
                                    poItem.setQtyRequest(qtyRequest);
                                    poItem.setPriceKonv(priceKonv);
                                    
                                    purchOrder.setListItem(poItem);
                                    vpo.setElementAt(purchOrder, i);
                                    break;
                                }
                            
                            
                        }
                        if (!bool) {
                            PurchaseOrder mypo = new PurchaseOrder();  
                            if(termPO==PstPurchaseOrder.PR_DIRECT){
                                 termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                                 status=I_DocStatus.DOCUMENT_STATUS_APPROVED;
                            }else if(termPO==PstPurchaseOrder.PR_SUPPLIER_CASH){
                                 termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                                 status=I_DocStatus.DOCUMENT_STATUS_FINAL;
                            }else{
                                 termOfPayment=PstPurchaseOrder.PO_PAYMENT_TYPE_CREDIT;//8
                                 status=I_DocStatus.DOCUMENT_STATUS_FINAL;
                            }
                            
                            mypo.setPoStatus(status);
                            mypo.setPurchDate(new Date());
                            mypo.setSupplierId(oidsupplier);
                            mypo.setLocationId(oidlokasi);
                            mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                            mypo.setRemark("");
                            mypo.setTermOfPayment(termOfPayment);
                            mypo.setRemark(nameSupplier);
                           
                            
                            mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                            //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                            PurchaseOrderItem poItem = new PurchaseOrderItem();
                            poItem.setMaterialId(oidmaterial);
                            poItem.setQuantity(qty);
                            
                            MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                            
                            if( matVendorPrice.getPriceCurrency()!=0){
                                mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                                mypo.setExchangeRate(exchangeRate);
                            }else{
                                mypo.setCurrencyId(currencyId);
                                mypo.setExchangeRate(exchangeRate);
                            }
                            
                            poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                            poItem.setPrice(price);
                            poItem.setOrgBuyingPrice(price);
                            poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                            poItem.setDiscount(matVendorPrice.getLastDiscount());
                            poItem.setTotal((qty * price) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                            poItem.setCurBuyingPrice(price);
                            poItem.setUnitId(oidUnit);
                            
                            poItem.setUnitRequestId(unitRequestId);
                            poItem.setQtyRequest(qtyRequest);
                            poItem.setPriceKonv(priceKonv);
                            
                            mypo.setListItem(poItem);
                            vpo.add(mypo);
                        }
                        
                    } else {
                        
                        PurchaseOrder mypo = new PurchaseOrder();
                        
                        if(termPO==PstPurchaseOrder.PR_DIRECT){
                             termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                             status=I_DocStatus.DOCUMENT_STATUS_APPROVED;
                        }else if(termPO==PstPurchaseOrder.PR_SUPPLIER_CASH){
                             termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                             status=I_DocStatus.DOCUMENT_STATUS_FINAL;
                        }else{
                             termOfPayment=PstPurchaseOrder.PO_PAYMENT_TYPE_CREDIT;//8
                             status=I_DocStatus.DOCUMENT_STATUS_FINAL;
                        }
                        
                        mypo.setPoStatus(status);
                        mypo.setPurchDate(new Date());
                        mypo.setSupplierId(oidsupplier);
                        mypo.setLocationId(oidlokasi);
                        mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                        mypo.setTermOfPayment(termOfPayment);
                        mypo.setRemark(nameSupplier);
                        mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                        //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                        PurchaseOrderItem poItem = new PurchaseOrderItem();
                        poItem.setMaterialId(oidmaterial);
                        poItem.setQuantity(qty);
                        MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                        
                        //mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                        if( matVendorPrice.getPriceCurrency()!=0){
                            mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                            mypo.setExchangeRate(exchangeRate);
                        }else{
                            mypo.setCurrencyId(currencyId);
                            mypo.setExchangeRate(exchangeRate);
                        }
                        
                        poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                        poItem.setPrice(price);
                        poItem.setOrgBuyingPrice(price);
                        poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                        poItem.setDiscount(matVendorPrice.getLastDiscount());
                        poItem.setTotal((qty * price) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                        poItem.setCurBuyingPrice(price);
                        poItem.setUnitId(oidUnit);
                        
                        poItem.setUnitRequestId(unitRequestId);
                        poItem.setQtyRequest(qtyRequest);
                        poItem.setPriceKonv(priceKonv);
                        
                        mypo.setListItem(poItem);
                        
                        vpo.add(mypo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vpo;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(oid);
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID], purchaseOrder.getOID());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_LOCATION_ID], purchaseOrder.getLocationId());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_LOCATION_TYPE], purchaseOrder.getLocationType());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE], purchaseOrder.getPoCode());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE_COUNTER], purchaseOrder.getPoCodeCounter());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE], purchaseOrder.getPurchDate());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID], purchaseOrder.getSupplierId());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS], purchaseOrder.getPoStatus());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_REMARK], purchaseOrder.getRemark());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_TERMS_OF_PAYMENT], purchaseOrder.getTermOfPayment());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CREDIT_TIME], purchaseOrder.getCreditTime());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PPN], purchaseOrder.getPpn());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CURRENCY_ID], purchaseOrder.getCurrencyId());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CODE_REVISI], purchaseOrder.getCodeRevisi());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_INCLUDE_PPN], purchaseOrder.getIncludePpn());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_EXCHANGE_RATE], purchaseOrder.getExchangeRate());
         object.put(PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_CATEGORY_ID], purchaseOrder.getCategoryId());
      }catch(Exception exc){}
      return object;
   }
}
