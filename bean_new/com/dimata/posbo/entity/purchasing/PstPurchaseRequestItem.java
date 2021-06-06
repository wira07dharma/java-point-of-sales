/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.purchasing;

/* package java */

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;
/**
 *
 * @author dimata005
 */
public class PstPurchaseRequestItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
     public static final String TBL_PURCHASE_REQUEST_ITEM = "pos_purchase_request_item";

    public static final int FLD_PURCHASE_REQUEST_ITEM_ID = 0;
    public static final int FLD_PURCHASE_REQUEST_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_UNIT_ID = 3;
    public static final int FLD_QUANTITY = 4;
    public static final int FLD_STATUS_REQUEST = 5;
    public static final int FLD_NOTE = 6;
    public static final int FLD_SUPPLIER_ID = 7;
    public static final int FLD_PRICE_BUYING = 8;
    public static final int FLD_UNIT_REQUEST_ID = 9;
    public static final int FLD_SUPPLIER_NAME = 10;
    public static final int FLD_TOTA_PRICE_BUYING=11;
    public static final int FLD_TERM_PURCHASE_REQUEST=12;

    public static final String[] fieldNames = {
        "PURCHASE_REQUEST_ITEM_ID",
        "PURCHASE_REQUEST_ID",
        "MATERIAL_ID",
        "UNIT_ID",
        "QTY",
        "STATUS_REQUEST",
        "NOTE",
        "SUPPLIER_ID",
        "PRICE_BUYING",
        "UNIT_REQUEST_ID",
        "SUPPLIER_NAME",
        "TOTA_PRICE_BUYING",
        "TERM_PURCHASE_REQUEST"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_STRING,
        //
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT
    };

    public PstPurchaseRequestItem() {
    }

     public PstPurchaseRequestItem(int i) throws DBException {
        super(new PstPurchaseRequestItem());
    }

    public PstPurchaseRequestItem(String sOid) throws DBException {
        super(new PstPurchaseRequestItem(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPurchaseRequestItem(long lOid) throws DBException {
        super(new PstPurchaseRequestItem(0));
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
        return TBL_PURCHASE_REQUEST_ITEM;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPurchaseRequestItem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PurchaseRequestItem poitem = fetchExc(ent.getOID());
        ent = (Entity) poitem;
        return poitem.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PurchaseRequestItem) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PurchaseRequestItem) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

     public static PurchaseRequestItem fetchExc(long oid) throws DBException {
        try {
            PurchaseRequestItem poitem = new PurchaseRequestItem();
            PstPurchaseRequestItem pstPoItem = new PstPurchaseRequestItem(oid);
            poitem.setOID(oid);

            poitem.setPurchaseOrderId(pstPoItem.getlong(FLD_PURCHASE_REQUEST_ID));
            poitem.setMaterialId(pstPoItem.getlong(FLD_MATERIAL_ID));
            poitem.setUnitId(pstPoItem.getlong(FLD_UNIT_ID));
            poitem.setQuantity(pstPoItem.getdouble(FLD_QUANTITY));
            poitem.setApprovalStatus(pstPoItem.getInt(FLD_STATUS_REQUEST));
            poitem.setNote(pstPoItem.getString(FLD_NOTE));
            
            //tambah
            poitem.setSupplierId(pstPoItem.getlong(FLD_SUPPLIER_ID));
            poitem.setPriceBuying(pstPoItem.getdouble( FLD_PRICE_BUYING));
            poitem.setUnitRequestId(pstPoItem.getlong(FLD_UNIT_REQUEST_ID));
            poitem.setSupplierName(pstPoItem.getString(FLD_SUPPLIER_NAME));
            poitem.setTotalPrice(pstPoItem.getdouble(FLD_TOTA_PRICE_BUYING));
            poitem.setTermPurchaseRequest(pstPoItem.getInt(FLD_TERM_PURCHASE_REQUEST));
            
            return poitem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequestItem(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PurchaseRequestItem poitem) throws DBException {
        try {
            PstPurchaseRequestItem pstPoItem = new PstPurchaseRequestItem(0);

            pstPoItem.setLong(FLD_PURCHASE_REQUEST_ID, poitem.getPurchaseOrderId());
            pstPoItem.setLong(FLD_MATERIAL_ID, poitem.getMaterialId());
            pstPoItem.setLong(FLD_UNIT_ID, poitem.getUnitId());
            pstPoItem.setDouble(FLD_QUANTITY, poitem.getQuantity());
            pstPoItem.setInt(FLD_STATUS_REQUEST, poitem.getApprovalStatus());
            pstPoItem.setString(FLD_NOTE, poitem.getNote());
            
            //tambah
            pstPoItem.setLong(FLD_SUPPLIER_ID, poitem.getSupplierId());
            pstPoItem.setDouble(FLD_PRICE_BUYING, poitem.getPriceBuying());
            pstPoItem.setLong(FLD_UNIT_REQUEST_ID, poitem.getUnitRequestId());
            pstPoItem.setString(FLD_SUPPLIER_NAME, poitem.getSupplierName());
            pstPoItem.setDouble(FLD_TOTA_PRICE_BUYING, poitem.getTotalPrice());
            pstPoItem.setInt(FLD_TERM_PURCHASE_REQUEST, poitem.getTermPurchaseRequest());
            pstPoItem.insert();
            poitem.setOID(pstPoItem.getlong(FLD_PURCHASE_REQUEST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequestItem(0), DBException.UNKNOWN);
        }
        return poitem.getOID();
    }

    public static long updateExc(PurchaseRequestItem poitem) throws DBException {
        try {
            if (poitem.getOID() != 0) {
                PstPurchaseRequestItem pstPoItem = new PstPurchaseRequestItem(poitem.getOID());

                pstPoItem.setLong(FLD_PURCHASE_REQUEST_ID, poitem.getPurchaseOrderId());
                pstPoItem.setLong(FLD_MATERIAL_ID, poitem.getMaterialId());
                pstPoItem.setLong(FLD_UNIT_ID, poitem.getUnitId());
                pstPoItem.setDouble(FLD_QUANTITY, poitem.getQuantity());
                pstPoItem.setInt(FLD_STATUS_REQUEST, poitem.getApprovalStatus());
                pstPoItem.setString(FLD_NOTE, poitem.getNote());
                //tambah
                pstPoItem.setLong(FLD_SUPPLIER_ID, poitem.getSupplierId());
                pstPoItem.setDouble(FLD_PRICE_BUYING, poitem.getPriceBuying());
                pstPoItem.setLong(FLD_UNIT_REQUEST_ID, poitem.getUnitRequestId());
                pstPoItem.setString(FLD_SUPPLIER_NAME, poitem.getSupplierName());
                pstPoItem.setDouble(FLD_TOTA_PRICE_BUYING, poitem.getTotalPrice());
                pstPoItem.setInt(FLD_TERM_PURCHASE_REQUEST, poitem.getTermPurchaseRequest());
                pstPoItem.update();
                return poitem.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequestItem(0), DBException.UNKNOWN);
        }
        return 0;
    }

     public static long deleteExc(long oid) throws DBException {
        try {
            PstPurchaseRequestItem pstPoItem = new PstPurchaseRequestItem(oid);
            pstPoItem.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequestItem(0), DBException.UNKNOWN);
        }
        return oid;
    }

      public static void deleteExc(String whereClause) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM " + TBL_PURCHASE_REQUEST_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            rs.close();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            System.out.println("Err: delete item " + e.toString());
        }
    }

   public static void deleteByPurchaseRequest(long oidPurchaseRequest) {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM " + PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM +
            " WHERE " + PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID] +
            " = " + oidPurchaseRequest;
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("PstPurchaseRequestItem.deleteByPurchaseOrder() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static long deleteExcByParent(long oid) throws DBException {
        long hasil = 0;
        try {
            String sql = "DELETE FROM " + TBL_PURCHASE_REQUEST_ITEM+
            " WHERE " + fieldNames[FLD_PURCHASE_REQUEST_ID] +
            " = " + oid;
            int result = execUpdate(sql);
            hasil = oid;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseRequestItem(0), DBException.UNKNOWN);
        }
        return hasil;
    }

     public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PURCHASE_REQUEST_ITEM;
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
                PurchaseRequestItem poitem = new PurchaseRequestItem();
                resultToObject(rs, poitem);
                lists.add(poitem);
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



     public static Vector list(int limitStart, int recordToGet, String whereClause) { 
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT POI." + fieldNames[FLD_PURCHASE_REQUEST_ITEM_ID] +
            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " ,UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " ,POI." + fieldNames[FLD_QUANTITY] +
            " ,POI." + fieldNames[FLD_MATERIAL_ID] +
            " ,POI." + fieldNames[FLD_UNIT_ID] +
            " ,POI." + fieldNames[FLD_STATUS_REQUEST] +
            " ,POI." + fieldNames[FLD_NOTE] +
            " ,POI." + fieldNames[FLD_UNIT_REQUEST_ID] +
            " ,POI." + fieldNames[FLD_PRICE_BUYING] +
            " ,POI." + fieldNames[FLD_TOTA_PRICE_BUYING] +
            " ,POI." + fieldNames[FLD_SUPPLIER_ID] +     
            " ,POI." + fieldNames[FLD_SUPPLIER_NAME] +  
            " ,POI." + fieldNames[FLD_TERM_PURCHASE_REQUEST]+        
            " ,UNT." + PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT] +
            //adding barcode di report po
            //by mirahu 20120424
            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+
            ",PR." +PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+
            ",PR." +PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+
            " FROM " + TBL_PURCHASE_REQUEST_ITEM +
            " POI INNER JOIN " + PstMaterial.TBL_MATERIAL +
            " MAT ON POI." + fieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " LEFT JOIN " + PstUnit.TBL_P2_UNIT +
            " UNT ON POI." + fieldNames[FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " LEFT JOIN " + PstPurchaseRequest.TBL_PURCHASE_REQUEST +
            " PR ON POI." + fieldNames[FLD_PURCHASE_REQUEST_ID] +
            " = PR." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
            " LEFT JOIN " + PstLocation.TBL_P2_LOCATION +
            " LOC ON PR." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

            /*" INNER JOIN " + PstMatCurrency.TBL_CURRENCY +
            " CURR ON POI." + fieldNames[FLD_CURRENCY_ID] +
            " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID]+*/

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            //sql = sql + " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

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

            System.out.println("PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem) >>>\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                PurchaseRequestItem poitem = new PurchaseRequestItem();
                Material mat = new Material();
                Unit matUnit = new Unit();
                PurchaseRequest po = new PurchaseRequest();

                poitem.setOID(rs.getLong(fieldNames[FLD_PURCHASE_REQUEST_ITEM_ID]));
                poitem.setQuantity(rs.getDouble(fieldNames[FLD_QUANTITY]));
                poitem.setMaterialId(rs.getLong(fieldNames[FLD_MATERIAL_ID]));
                poitem.setUnitId(rs.getLong(fieldNames[FLD_UNIT_ID]));
                poitem.setApprovalStatus(rs.getInt(fieldNames[FLD_STATUS_REQUEST]));
                poitem.setNote(rs.getString(fieldNames[FLD_NOTE]));
                poitem.setUnitRequestId(rs.getLong(fieldNames[FLD_UNIT_REQUEST_ID]));
                poitem.setPriceBuying(rs.getDouble(fieldNames[FLD_PRICE_BUYING]));
                poitem.setTotalPrice(rs.getDouble(fieldNames[FLD_TOTA_PRICE_BUYING]));
                poitem.setSupplierId(rs.getLong(fieldNames[FLD_SUPPLIER_ID]));
                poitem.setSupplierName(rs.getString(fieldNames[FLD_SUPPLIER_NAME]));
                poitem.setTermPurchaseRequest(rs.getInt(fieldNames[FLD_TERM_PURCHASE_REQUEST]));
                temp.add(poitem);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setBarCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                temp.add(mat);

                matUnit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE] ));
                matUnit.setQtyPerBaseUnit(rs.getDouble(PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT]));
                temp.add(matUnit);
                
                po.setLocationId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]));
                po.setWarehouseSupplierId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID])); 
                temp.add(po);
                
                lists.add(temp);
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

     public static void resultToObject(ResultSet rs, PurchaseRequestItem poitem) {
        try {
            poitem.setOID(rs.getLong(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ITEM_ID]));
            poitem.setPurchaseOrderId(rs.getLong(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]));
            poitem.setMaterialId(rs.getLong(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID]));
            poitem.setUnitId(rs.getLong(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_ID]));
            poitem.setQuantity(rs.getDouble(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_QUANTITY]));
            poitem.setApprovalStatus(rs.getInt(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_STATUS_REQUEST]));
            poitem.setNote(rs.getString(rs.getString(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_NOTE])));
            
            //tambah
            poitem.setSupplierId(rs.getLong(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_ID]));
            poitem.setPriceBuying(rs.getDouble(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PRICE_BUYING]));
            poitem.setUnitRequestId(rs.getLong(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_REQUEST_ID]));
            poitem.setSupplierName(rs.getString(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_NAME]));
            poitem.setTotalPrice(rs.getDouble(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TOTA_PRICE_BUYING]));
            poitem.setTermPurchaseRequest(rs.getInt(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TERM_PURCHASE_REQUEST]));
            
        } catch (Exception e) {
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ITEM_ID] + ") FROM " + TBL_PURCHASE_REQUEST_ITEM;
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
    
    
    public static Vector listWithStokNMinStock(int limitStart, int recordToGet, String whereClause, long locationId) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT POI." + fieldNames[FLD_PURCHASE_REQUEST_ITEM_ID] +
            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " ,UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " ,POI." + fieldNames[FLD_QUANTITY] +
            " ,POI." + fieldNames[FLD_MATERIAL_ID] +
            " ,POI." + fieldNames[FLD_UNIT_REQUEST_ID] +
            " ,UNT." + PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT] +
            //adding barcode di report po
            //by mirahu 20120424
            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+
            " ,SUM(q0)"+
            " ,SUM(qm0) " +      
            " FROM " + 
            " (SELECT ms."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+", ms."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+", qty AS q0, qty_min AS qm0 FROM "+PstMaterialStock.TBL_MATERIAL_STOCK+
            " AS ms WHERE ms."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"='"+locationId+"' ) AS tbl  " +
            " RIGHT JOIN "+TBL_PURCHASE_REQUEST_ITEM+" POI  ON tbl."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+" = POI."+fieldNames[FLD_MATERIAL_ID]+
            " INNER JOIN " + PstMaterial.TBL_MATERIAL +
            " MAT ON POI." + fieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " LEFT JOIN " + PstUnit.TBL_P2_UNIT +
            " UNT ON POI." + fieldNames[FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];


            /*" INNER JOIN " + PstMatCurrency.TBL_CURRENCY +
            " CURR ON POI." + fieldNames[FLD_CURRENCY_ID] +
            " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID]+*/

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            sql = sql + " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

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

            //System.out.println("PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem) >>>\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                PurchaseRequestItem poitem = new PurchaseRequestItem();
                Material mat = new Material();
                Unit matUnit = new Unit();
                
                poitem.setOID(rs.getLong(fieldNames[FLD_PURCHASE_REQUEST_ITEM_ID]));
                poitem.setQuantity(rs.getDouble(fieldNames[FLD_QUANTITY]));
                poitem.setMaterialId(rs.getLong(fieldNames[FLD_MATERIAL_ID]));
                poitem.setUnitId(rs.getLong(fieldNames[FLD_UNIT_REQUEST_ID]));
                poitem.setCurrentStock(rs.getDouble(10));
                poitem.setMinimStock(rs.getDouble(11));
                
                temp.add(poitem);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                //adding barcode 20120424
                mat.setBarCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                temp.add(mat);

                matUnit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE] ));
                matUnit.setQtyPerBaseUnit(rs.getDouble(PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT]));
                temp.add(matUnit);
                
                

                lists.add(temp);
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
    
    
     public static void updatePurchaseRequest(long oidMaterialId, long oidPurchaseRequestID, int statusApp, String note) {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM +
            " SET "+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_STATUS_REQUEST]+"='"+statusApp+"'"+
            " , "+ PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_NOTE]+"='"+note+"'"+
            " WHERE " + PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID] +
            " = " + oidPurchaseRequestID+
            " AND "+ PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID] +
            " = "+ oidMaterialId;
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("PstPurchaseRequestItem.deleteByPurchaseOrder() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }
      public static void updatePurchaseRequestPrice(long oidMaterialId, long oidPurchaseRequestID, Double Price) {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM +
            " SET "+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PRICE_BUYING]+"='"+Price+"'"+
            " WHERE " + PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID] +
            " = " + oidPurchaseRequestID+
            " AND "+ PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID] +
            " = "+ oidMaterialId;
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("PstPurchaseRequestItem.deleteByPurchaseOrder() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }
 public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         PurchaseRequestItem purchaseRequestItem = PstPurchaseRequestItem.fetchExc(oid);
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ITEM_ID], purchaseRequestItem.getOID());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID], purchaseRequestItem.getPurchaseOrderId());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID], purchaseRequestItem.getMaterialId());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_ID], purchaseRequestItem.getUnitId());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_QUANTITY], purchaseRequestItem.getQuantity());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_STATUS_REQUEST], purchaseRequestItem.getApprovalStatus());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_NOTE], purchaseRequestItem.getNote());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_ID], purchaseRequestItem.getSupplierId());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PRICE_BUYING], purchaseRequestItem.getPriceBuying());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_REQUEST_ID], purchaseRequestItem.getUnitRequestId());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_NAME], purchaseRequestItem.getSupplierName());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TOTA_PRICE_BUYING], purchaseRequestItem.getTotalPrice());
         object.put(PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TERM_PURCHASE_REQUEST ], purchaseRequestItem.getTermPurchaseRequest());
      }catch(Exception exc){}
      return object;
   }
}
