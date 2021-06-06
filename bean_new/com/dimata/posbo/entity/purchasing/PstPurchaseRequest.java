/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.purchasing;

/* package java */
import com.dimata.common.entity.location.PstLocation;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.posbo.session.purchasing.SessPurchaseRequest;
import com.dimata.qdep.entity.*;
import java.sql.ResultSet;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class PstPurchaseRequest extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_PURCHASE_REQUEST = "pos_purchase_request";

    public static final int FLD_PURCHASE_REQUEST_ID = 0;
    public static final int FLD_PURCH_REQUEST_DATE = 1;
    public static final int FLD_REMARK = 2;
    public static final int FLD_PO_CODE_COUNTER = 3;
    public static final int FLD_LOCATION_ID = 4;
    public static final int FLD_PO_CODE = 5;
    public static final int FLD_PR_STATUS = 6;
    public static final int FLD_PR_TERM_OF_PAYMENT = 7;
    public static final int FLD_SUPPLIER_WAREHOUSE_ID = 8;
    public static final int FLD_CURRENCY_ID = 9;
    public static final int FLD_EXCHANGE_RATE = 10;
    public static final int FLD_BC_NUMBER = 11;
    public static final int FLD_DOCUMENT_TYPE = 12;
    public static final int FLD_REQUEST_SOURCE = 13;

    public static final String[] fieldNames
            = {
                "PURCHASE_REQUEST_ID",
                "PURCH_REQUEST_DATE",
                "REMARK",
                "PR_CODE_COUNTER",
                "LOCATION_ID",
                "PR_CODE",
                "PR_STATUS",
                "TERMS_OF_PAYMENT",
                "SUPPLIER_WAREHOUSE_ID",
                "CURRENCY_ID",
                "EXCHANGE_RATE",
                "BC_NUMBER",
                "DOCUMENT_TYPE",
                "REQUEST_SOURCE"
            };

    public static final int[] fieldTypes
            = {
                TYPE_LONG + TYPE_PK + TYPE_ID,//0
                TYPE_DATE,//1
                TYPE_STRING,//2
                TYPE_INT,//3
                TYPE_LONG,//4
                TYPE_STRING,//5
                TYPE_INT,//6
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_INT
            };

    public PstPurchaseRequest() {
    }

    public PstPurchaseRequest(int i) throws DBException {
        super(new PstPurchaseRequest());
    }

    public PstPurchaseRequest(String sOid) throws DBException {
        super(new PstPurchaseRequest(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPurchaseRequest(long lOid) throws DBException {
        super(new PstPurchaseRequest(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_PURCHASE_REQUEST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPurchaseRequest().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PurchaseRequest pr = fetchExc(ent.getOID());
        ent = (Entity) pr;
        return pr.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PurchaseRequest) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PurchaseRequest) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PurchaseRequest fetchExc(long oid) throws DBException {
        try {
            PurchaseRequest po = new PurchaseRequest();
            PstPurchaseRequest pstpo = new PstPurchaseRequest(oid);
            po.setOID(oid);

            po.setLocationId(pstpo.getlong(FLD_LOCATION_ID));
            po.setPrCode(pstpo.getString(FLD_PO_CODE));
            po.setPeCodeCounter(pstpo.getInt(FLD_PO_CODE_COUNTER));
            po.setRemark(pstpo.getString(FLD_REMARK));
            po.setPurchRequestDate(pstpo.getDate(FLD_PURCH_REQUEST_DATE));
            po.setPrStatus(pstpo.getInt(FLD_PR_STATUS));
            po.setTermOfPayment(pstpo.getInt(FLD_PR_TERM_OF_PAYMENT));
            po.setWarehouseSupplierId(pstpo.getlong(FLD_SUPPLIER_WAREHOUSE_ID));
            po.setCurrencyId(pstpo.getlong(FLD_CURRENCY_ID));
            po.setExhangeRate(pstpo.getdouble(FLD_EXCHANGE_RATE));
            po.setNomorBc(pstpo.getString(FLD_BC_NUMBER));
            po.setJenisDocument(pstpo.getString(FLD_DOCUMENT_TYPE));
            po.setRequestSource(pstpo.getInt(FLD_REQUEST_SOURCE));

            return po;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequest(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PurchaseRequest po) throws DBException {
        try {
            PstPurchaseRequest pstpo = new PstPurchaseRequest(0);

            pstpo.setLong(FLD_LOCATION_ID, po.getLocationId());
            pstpo.setString(FLD_PO_CODE, po.getPrCode());
            pstpo.setInt(FLD_PO_CODE_COUNTER, po.getPeCodeCounter());
            pstpo.setString(FLD_REMARK, po.getRemark());
            pstpo.setDate(FLD_PURCH_REQUEST_DATE, po.getPurchRequestDate());
            pstpo.setInt(FLD_PR_STATUS, po.getPrStatus());
            pstpo.setInt(FLD_PR_TERM_OF_PAYMENT, po.getTermOfPayment());
            pstpo.setLong(FLD_SUPPLIER_WAREHOUSE_ID, po.getWarehouseSupplierId());
            pstpo.setLong(FLD_CURRENCY_ID, po.getCurrencyId());
            pstpo.setDouble(FLD_EXCHANGE_RATE, po.getExhangeRate());
            pstpo.setString(FLD_BC_NUMBER, po.getNomorBc());
            pstpo.setString(FLD_DOCUMENT_TYPE, po.getJenisDocument());
            pstpo.setInt(FLD_REQUEST_SOURCE, po.getRequestSource());

            pstpo.insert();
            po.setOID(pstpo.getlong(FLD_PURCHASE_REQUEST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequest(0), DBException.UNKNOWN);
        }
        return po.getOID();
    }

    public static long updateExc(PurchaseRequest po) throws DBException {
        try {
            if (po.getOID() != 0) {
                PstPurchaseRequest pstpo = new PstPurchaseRequest(po.getOID());

                pstpo.setLong(FLD_LOCATION_ID, po.getLocationId());
                pstpo.setString(FLD_PO_CODE, po.getPrCode());
                pstpo.setInt(FLD_PO_CODE_COUNTER, po.getPeCodeCounter());
                pstpo.setString(FLD_REMARK, po.getRemark());
                pstpo.setDate(FLD_PURCH_REQUEST_DATE, po.getPurchRequestDate());
                pstpo.setInt(FLD_PR_STATUS, po.getPrStatus());
                pstpo.setInt(FLD_PR_TERM_OF_PAYMENT, po.getTermOfPayment());
                pstpo.setLong(FLD_SUPPLIER_WAREHOUSE_ID, po.getWarehouseSupplierId());

                pstpo.setLong(FLD_CURRENCY_ID, po.getCurrencyId());
                pstpo.setDouble(FLD_EXCHANGE_RATE, po.getExhangeRate());
                pstpo.setString(FLD_BC_NUMBER, po.getNomorBc());
                pstpo.setString(FLD_DOCUMENT_TYPE, po.getJenisDocument());
                pstpo.setInt(FLD_REQUEST_SOURCE, po.getRequestSource());

                pstpo.update();
                return po.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequest(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPurchaseRequest pstpo = new PstPurchaseRequest(oid);
            //Delete Item First
            PstPurchaseRequestItem pstPOItem = new PstPurchaseRequestItem();
            long result = pstPOItem.deleteExcByParent(oid);
            pstpo.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequest(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PURCHASE_REQUEST;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }

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
                PurchaseRequest po = new PurchaseRequest();
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

    public static void resultToObject(ResultSet rs, PurchaseRequest po) {
        try {
            po.setOID(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]));
            po.setLocationId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]));
            po.setPrCode(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]));
            po.setPeCodeCounter(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE_COUNTER]));
            po.setPurchRequestDate(rs.getDate(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]));
            po.setPrStatus(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]));
            po.setRemark(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK]));
            po.setTermOfPayment(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_TERM_OF_PAYMENT]));
            po.setWarehouseSupplierId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]));

            po.setCurrencyId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_CURRENCY_ID]));
            po.setExhangeRate(rs.getDouble(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_EXCHANGE_RATE]));
            po.setNomorBc(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_BC_NUMBER]));
            po.setJenisDocument(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_DOCUMENT_TYPE]));
            po.setRequestSource(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REQUEST_SOURCE]));

        } catch (Exception e) {
        }
    }

    public int getDocumentStatus(long documentId) {
        DBResultSet dbrs = null;
        try {
            PurchaseRequest orderMaterial = fetchExc(documentId);
            return orderMaterial.getPrStatus();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return -1;
    }

    public int setDocumentStatus(long documentId, int indexStatus) {
        return indexStatus;
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
                    // long oidsupplier = Long.parseLong((String) v1.get(3));
                    long oidUnit = ((Long) v1.get(3)).longValue();
                    if (vpo.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vpo.size(); i++) {
                            bool = false;
                            PurchaseRequest purchRequest = (PurchaseRequest) vpo.get(i);
                            if ((purchRequest.getLocationId() == oidlokasi)) {
                                bool = true;
                                PurchaseRequestItem prItem = new PurchaseRequestItem();
                                prItem.setMaterialId(oidmaterial);
                                prItem.setQuantity(qty);
//                                MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
//                                //purchOrder.setCurrencyId(matVendorPrice.getPriceCurrency());
//                                poItem.setUnitId(matVendorPrice.getBuyingUnitId());
//                                poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
//                                poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
//                                poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
//                                poItem.setDiscount(matVendorPrice.getLastDiscount());
//                                poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
//                                poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
//
                                prItem.setUnitId(oidUnit);
                                purchRequest.setListItem(prItem);
                                vpo.setElementAt(purchRequest, i);
                            }
                        }
                        if (!bool) {
                            PurchaseRequest mypo = new PurchaseRequest();
                            mypo.setPrStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            mypo.setPurchRequestDate(new Date());
                            //mypo.setSupplierId(oidsupplier);
                            mypo.setLocationId(oidlokasi);
                            //mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                            mypo.setRemark("");
                            //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                            //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                            PurchaseRequestItem prItem = new PurchaseRequestItem();
                            prItem.setMaterialId(oidmaterial);
                            prItem.setQuantity(qty);
//                            MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
//                            mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
//                            poItem.setUnitId(matVendorPrice.getBuyingUnitId());
//                            poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
//                            poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
//                            poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
//                            poItem.setDiscount(matVendorPrice.getLastDiscount());
//                            poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
//                            poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                            prItem.setUnitId(oidUnit);
                            mypo.setListItem(prItem);
                            vpo.add(mypo);
                        }
                    } else {
                        PurchaseRequest mypo = new PurchaseRequest();
                        mypo.setPrStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                        mypo.setPurchRequestDate(new Date());
                        //mypo.setSupplierId(oidsupplier);
                        mypo.setLocationId(oidlokasi);
                        //mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                        mypo.setRemark("");
                        //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                        //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                        PurchaseRequestItem prItem = new PurchaseRequestItem();
                        prItem.setMaterialId(oidmaterial);
                        prItem.setQuantity(qty);

//                        MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
//                        mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
//                        poItem.setUnitId(matVendorPrice.getBuyingUnitId());
//                        poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
//                        poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
//                        poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
//                        poItem.setDiscount(matVendorPrice.getLastDiscount());
//                        poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
//                        poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                        prItem.setUnitId(oidUnit);
                        mypo.setListItem(prItem);
                        vpo.add(mypo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vpo;
    }

    public static long autoInsertPr(Vector list) {
        long hasil = 0;
        try {
            Vector vPR = getRekapForPurchaseMain(list);
            if (vPR.size() > 0) {
                for (int i = 0; i < vPR.size(); i++) {
                    PurchaseRequest purchRequest = (PurchaseRequest) vPR.get(i);
                    purchRequest.setPeCodeCounter(SessPurchaseRequest.getIntCode(purchRequest, new Date(), 0, 0, true));
                    purchRequest.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(purchRequest));
                    Vector v1 = (Vector) purchRequest.getListItem();
                    long oidPo = PstPurchaseRequest.insertExc(purchRequest);
                    System.out.println("=>> INSERT MAIN PO");
                    if (oidPo != 0) {
                        for (int k = 0; k < v1.size(); k++) {
                            PurchaseRequestItem purchaseRequestItem = (PurchaseRequestItem) v1.get(k);
                            purchaseRequestItem.setPurchaseOrderId(oidPo);
                            purchaseRequestItem.setQuantity(purchaseRequestItem.getQuantity());
                            //purchOrderItem.setOrgBuyingPrice(purchOrderItem.getPrice());
                            PstPurchaseRequestItem.insertExc(purchaseRequestItem);
                            System.out.println("=====>> INSERT ITEM " + k);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("autoInsertPo : " + ex);
        }
        return hasil;
    }

    public static int checkCode(String code) {
        int use = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE] + ") AS CODE"
                    + " FROM " + PstPurchaseOrder.TBL_PURCHASE_ORDER
                    + " WHERE " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE] + " LIKE '%" + code + "%'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                use = rs.getInt("CODE");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Err at counter : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return use;
    }

    public static long updateDocumentPR(Vector list, long vSelected, String poNumber) {
        long hasil = 0;
        try {
            if (list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmaterial = Long.parseLong((String) v1.get(0));
                    int statusApp = Integer.parseInt((String) v1.get(5));
                    String note = (String) v1.get(6);
                    double hargaItem = Double.parseDouble((String) v1.get(15));
                    //update list itemnya
                    PstPurchaseRequestItem.updatePurchaseRequest(oidmaterial, vSelected, statusApp, note);
                    PstPurchaseRequestItem.updatePurchaseRequestPrice(oidmaterial, vSelected, hargaItem);

                }
                //update documentnya
                PstPurchaseRequest.updatePurchaseRequestClosed(vSelected, poNumber);
            }

        } catch (Exception ex) {
            //System.out.println("autoInsertPo : " + ex);
        }
        return hasil;
    }

    public static void updatePurchaseRequestClosed(long oidPurchaseRequestID, String poNumber) {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstPurchaseRequest.TBL_PURCHASE_REQUEST
                    + " SET " + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + "='5'"
                    + " , " + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK] + "='" + poNumber + "'"
                    + " WHERE " + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]
                    + " = " + oidPurchaseRequestID;
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("PstPurchaseRequestItem.deleteByPurchaseOrder() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static JSONObject fetchJSON(long oid) {
        JSONObject object = new JSONObject();
        try {
            PurchaseRequest purchaseRequest = PstPurchaseRequest.fetchExc(oid);
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID], purchaseRequest.getOID());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE], purchaseRequest.getPurchRequestDate());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK], purchaseRequest.getRemark());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE_COUNTER], purchaseRequest.getPeCodeCounter());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID], purchaseRequest.getLocationId());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE], purchaseRequest.getPrCode());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS], purchaseRequest.getPrStatus());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_TERM_OF_PAYMENT], purchaseRequest.getTermOfPayment());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID], purchaseRequest.getWarehouseSupplierId());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_CURRENCY_ID], purchaseRequest.getLocationName());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_EXCHANGE_RATE], purchaseRequest.getCurrencyId());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_BC_NUMBER], purchaseRequest.getNomorBc());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_DOCUMENT_TYPE], purchaseRequest.getJenisDocument());
            object.put(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REQUEST_SOURCE], purchaseRequest.getRequestSource());
        } catch (Exception exc) {
        }
        return object;
    }
}

