/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetailsubject;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMarketingPromotionDetailSubject extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_DETAIL_SUBJECT = "marketing_promotion_detail_subject";
    public static final int FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_DETAIL_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_QUANTITY = 3;
    public static final int FLD_VALID_FOR_MULTIPLICATION = 4;
    public static final int FLD_SALES_PRICE = 5;
    public static final int FLD_PURCHASE_PRICE = 6;
    public static final int FLD_GROSS_PROFIT = 7;
    public static final int FLD_TARGET_QUANTITY = 8;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_DETAIL_SUBJECT_ID",
        "MARKETING_PROMOTION_DETAIL_ID",
        "MATERIAL_ID",
        "QUANTITY",
        "VALID_FOR_MULTIPLICATION",
        "SALES_PRICE",
        "PURCHASE_PRICE",
        "GROSS_PROFIT",
        "TARGET_QUANTITY"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public PstMarketingPromotionDetailSubject() {
    }

    public PstMarketingPromotionDetailSubject(int i) throws DBException {
        super(new PstMarketingPromotionDetailSubject());
    }

    public PstMarketingPromotionDetailSubject(String sOid) throws DBException {
        super(new PstMarketingPromotionDetailSubject(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionDetailSubject(long lOid) throws DBException {
        super(new PstMarketingPromotionDetailSubject(0));
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
        return TBL_MARKETING_PROMOTION_DETAIL_SUBJECT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionDetailSubject().getClass().getName();
    }

    public static MarketingPromotionDetailSubject fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionDetailSubject entMarketingPromotionDetailSubject = new MarketingPromotionDetailSubject();
            PstMarketingPromotionDetailSubject pstMarketingPromotionDetailSubject = new PstMarketingPromotionDetailSubject(oid);
            entMarketingPromotionDetailSubject.setOID(oid);
            entMarketingPromotionDetailSubject.setMarketingPromotionDetailId(pstMarketingPromotionDetailSubject.getlong(FLD_MARKETING_PROMOTION_DETAIL_ID));
            entMarketingPromotionDetailSubject.setMaterialId(pstMarketingPromotionDetailSubject.getlong(FLD_MATERIAL_ID));
            entMarketingPromotionDetailSubject.setQuantity(pstMarketingPromotionDetailSubject.getInt(FLD_QUANTITY));
            entMarketingPromotionDetailSubject.setValidForMultiplication(pstMarketingPromotionDetailSubject.getString(FLD_VALID_FOR_MULTIPLICATION));
            entMarketingPromotionDetailSubject.setSalesPrice(pstMarketingPromotionDetailSubject.getdouble(FLD_SALES_PRICE));
            entMarketingPromotionDetailSubject.setPurchasePrice(pstMarketingPromotionDetailSubject.getdouble(FLD_PURCHASE_PRICE));
            entMarketingPromotionDetailSubject.setGrossProfit(pstMarketingPromotionDetailSubject.getdouble(FLD_GROSS_PROFIT));
            entMarketingPromotionDetailSubject.setTargetQuantity(pstMarketingPromotionDetailSubject.getInt(FLD_TARGET_QUANTITY));
            return entMarketingPromotionDetailSubject;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailSubject(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionDetailSubject entMarketingPromotionDetailSubject = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionDetailSubject;
        return entMarketingPromotionDetailSubject.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) throws DBException {
        try {
            if (entMarketingPromotionDetailSubject.getOID() != 0) {
                PstMarketingPromotionDetailSubject pstMarketingPromotionDetailSubject = new PstMarketingPromotionDetailSubject(entMarketingPromotionDetailSubject.getOID());
                pstMarketingPromotionDetailSubject.setLong(FLD_MARKETING_PROMOTION_DETAIL_ID, entMarketingPromotionDetailSubject.getMarketingPromotionDetailId());
                pstMarketingPromotionDetailSubject.setLong(FLD_MATERIAL_ID, entMarketingPromotionDetailSubject.getMaterialId());
                pstMarketingPromotionDetailSubject.setInt(FLD_QUANTITY, entMarketingPromotionDetailSubject.getQuantity());
                pstMarketingPromotionDetailSubject.setString(FLD_VALID_FOR_MULTIPLICATION, entMarketingPromotionDetailSubject.getValidForMultiplication());
                pstMarketingPromotionDetailSubject.setDouble(FLD_SALES_PRICE, entMarketingPromotionDetailSubject.getSalesPrice());
                pstMarketingPromotionDetailSubject.setDouble(FLD_PURCHASE_PRICE, entMarketingPromotionDetailSubject.getPurchasePrice());
                pstMarketingPromotionDetailSubject.setDouble(FLD_GROSS_PROFIT, entMarketingPromotionDetailSubject.getGrossProfit());
                pstMarketingPromotionDetailSubject.setInt(FLD_TARGET_QUANTITY, entMarketingPromotionDetailSubject.getTargetQuantity());
                pstMarketingPromotionDetailSubject.update();
                return entMarketingPromotionDetailSubject.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailSubject(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionDetailSubject) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionDetailSubject pstMarketingPromotionDetailSubject = new PstMarketingPromotionDetailSubject(oid);
            pstMarketingPromotionDetailSubject.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailSubject(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) throws DBException {
        try {
            PstMarketingPromotionDetailSubject pstMarketingPromotionDetailSubject = new PstMarketingPromotionDetailSubject(0);
            pstMarketingPromotionDetailSubject.setLong(FLD_MARKETING_PROMOTION_DETAIL_ID, entMarketingPromotionDetailSubject.getMarketingPromotionDetailId());
            pstMarketingPromotionDetailSubject.setLong(FLD_MATERIAL_ID, entMarketingPromotionDetailSubject.getMaterialId());
            pstMarketingPromotionDetailSubject.setInt(FLD_QUANTITY, entMarketingPromotionDetailSubject.getQuantity());
            pstMarketingPromotionDetailSubject.setString(FLD_VALID_FOR_MULTIPLICATION, entMarketingPromotionDetailSubject.getValidForMultiplication());
            pstMarketingPromotionDetailSubject.setDouble(FLD_SALES_PRICE, entMarketingPromotionDetailSubject.getSalesPrice());
            pstMarketingPromotionDetailSubject.setDouble(FLD_PURCHASE_PRICE, entMarketingPromotionDetailSubject.getPurchasePrice());
            pstMarketingPromotionDetailSubject.setDouble(FLD_GROSS_PROFIT, entMarketingPromotionDetailSubject.getGrossProfit());
            pstMarketingPromotionDetailSubject.setInt(FLD_TARGET_QUANTITY, entMarketingPromotionDetailSubject.getTargetQuantity());
            pstMarketingPromotionDetailSubject.insert();
            entMarketingPromotionDetailSubject.setOID(pstMarketingPromotionDetailSubject.getlong(FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailSubject(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionDetailSubject.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionDetailSubject) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) {
        try {
            entMarketingPromotionDetailSubject.setOID(rs.getLong(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID]));
            entMarketingPromotionDetailSubject.setMarketingPromotionDetailId(rs.getLong(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID]));
            entMarketingPromotionDetailSubject.setMaterialId(rs.getLong(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MATERIAL_ID]));
            entMarketingPromotionDetailSubject.setQuantity(rs.getInt(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY]));
            entMarketingPromotionDetailSubject.setValidForMultiplication(rs.getString(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION]));
            entMarketingPromotionDetailSubject.setSalesPrice(rs.getDouble(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_SALES_PRICE]));
            entMarketingPromotionDetailSubject.setPurchasePrice(rs.getDouble(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_PURCHASE_PRICE]));
            entMarketingPromotionDetailSubject.setGrossProfit(rs.getDouble(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT]));
            entMarketingPromotionDetailSubject.setTargetQuantity(rs.getInt(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_TARGET_QUANTITY]));

            entMarketingPromotionDetailSubject.setItemSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
            entMarketingPromotionDetailSubject.setItemBarcode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
            entMarketingPromotionDetailSubject.setItemName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            entMarketingPromotionDetailSubject.setItemCategory(rs.getString("catname"));
        } catch (Exception e) {
        }
    }

    public static void resultToObjectJoin(ResultSet rs, MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) {
        try {            
            entMarketingPromotionDetailSubject.setSalesPrice(rs.getDouble("sales_price"));
            entMarketingPromotionDetailSubject.setPurchasePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
            
            entMarketingPromotionDetailSubject.setItemId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
            entMarketingPromotionDetailSubject.setItemSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
            entMarketingPromotionDetailSubject.setItemBarcode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
            entMarketingPromotionDetailSubject.setItemName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            entMarketingPromotionDetailSubject.setItemCategory(rs.getString("catname"));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL_SUBJECT;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MarketingPromotionDetailSubject entMarketingPromotionDetailSubject = new MarketingPromotionDetailSubject();
                resultToObject(rs, entMarketingPromotionDetailSubject);
                lists.add(entMarketingPromotionDetailSubject);
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

    public static Vector listSaveJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT material.sku, material.barcode, material.name,"
                    + "kategori.name AS catname, subjek.*"
                    + " FROM marketing_promotion_detail_subject AS subjek"
                    + " INNER JOIN pos_material AS material"
                    + " ON subjek.MATERIAL_ID = material.MATERIAL_ID"
                    + " INNER JOIN pos_category AS kategori"
                    + " ON material.CATEGORY_ID = kategori.CATEGORY_ID";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MarketingPromotionDetailSubject entMarketingPromotionDetailSubject = new MarketingPromotionDetailSubject();
                resultToObject(rs, entMarketingPromotionDetailSubject);
                lists.add(entMarketingPromotionDetailSubject);
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

    public static Vector listGetJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT material.MATERIAL_ID, material.SKU, material.BARCODE, material.NAME, kategori.NAME AS catname,"
                    + " AVG(pricemap.PRICE) AS sales_price, material.AVERAGE_PRICE, pricemap.PRICE_TYPE_ID"
                    + " FROM pos_price_type_mapping AS pricemap"
                    + " INNER JOIN pos_material AS material"
                    + " ON pricemap.MATERIAL_ID = material.MATERIAL_ID"
                    + " INNER JOIN marketing_promotion_price_type AS pricetype"
                    + " ON pricemap.PRICE_TYPE_ID = pricetype.PRICE_TYPE_ID"
                    + " INNER JOIN marketing_promotion AS marpro"
                    + " ON pricemap.STANDART_RATE_ID = marpro.MARKETING_PROMOTION_STANDARD_RATE_ID"
                    + " AND marpro.MARKETING_PROMOTION_ID = pricetype.MARKETING_PROMOTION_ID"
                    + " INNER JOIN pos_category AS kategori"
                    + " ON material.CATEGORY_ID = kategori.CATEGORY_ID";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MarketingPromotionDetailSubject entMarketingPromotionDetailSubject = new MarketingPromotionDetailSubject();
                resultToObjectJoin(rs, entMarketingPromotionDetailSubject);
                lists.add(entMarketingPromotionDetailSubject);
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

    public static boolean checkOID(long entMarketingPromotionDetailSubjectId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL_SUBJECT + " WHERE "
                    + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = " + entMarketingPromotionDetailSubjectId;
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
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + ") FROM " + TBL_MARKETING_PROMOTION_DETAIL_SUBJECT;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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
    
    public static int getCountJoin(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + ")"
                    + " FROM " + TBL_MARKETING_PROMOTION_DETAIL_SUBJECT + " AS subjek"
                    + " INNER JOIN pos_material AS material"
                    + " ON subjek.MATERIAL_ID = material.MATERIAL_ID"
                    + " INNER JOIN pos_category AS kategori"
                    + " ON material.CATEGORY_ID = kategori.CATEGORY_ID"
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MarketingPromotionDetailSubject entMarketingPromotionDetailSubject = (MarketingPromotionDetailSubject) list.get(ls);
                    if (oid == entMarketingPromotionDetailSubject.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
}
