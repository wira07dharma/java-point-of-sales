/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetailobject;

import com.dimata.marketing.entity.marketingpromotiontype.PstMarketingPromotionType;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.PstCatCommission;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMarketingPromotionDetailObject extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_DETAIL_OBJECT = "marketing_promotion_detail_object";
    public static final int FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_DETAIL_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_QUANTITY = 3;
    public static final int FLD_MARKETING_PROMOTION_TYPE_ID = 4;
    public static final int FLD_VALID_FOR_MULTIPLICATION = 5;
    public static final int FLD_REGULAR_PRICE = 6;
    public static final int FLD_PROMOTION_PRICE = 7;
    public static final int FLD_COST = 8;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_DETAIL_OBJECT_ID",
        "MARKETING_PROMOTION_DETAIL_ID",
        "MATERIAL_ID",
        "QUANTITY",
        "MARKETING_PROMOTION_TYPE_ID",
        "VALID_FOR_MULTIPLICATION",
        "REGULAR_PRICE",
        "PROMOTION_PRICE",
        "COST"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstMarketingPromotionDetailObject() {
    }

    public PstMarketingPromotionDetailObject(int i) throws DBException {
        super(new PstMarketingPromotionDetailObject());
    }

    public PstMarketingPromotionDetailObject(String sOid) throws DBException {
        super(new PstMarketingPromotionDetailObject(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionDetailObject(long lOid) throws DBException {
        super(new PstMarketingPromotionDetailObject(0));
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
        return TBL_MARKETING_PROMOTION_DETAIL_OBJECT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionDetailObject().getClass().getName();
    }

    public static MarketingPromotionDetailObject fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionDetailObject entMarketingPromotionDetailObject = new MarketingPromotionDetailObject();
            PstMarketingPromotionDetailObject pstMarketingPromotionDetailObject = new PstMarketingPromotionDetailObject(oid);
            entMarketingPromotionDetailObject.setOID(oid);
            entMarketingPromotionDetailObject.setMarketingPromotionDetailId(pstMarketingPromotionDetailObject.getlong(FLD_MARKETING_PROMOTION_DETAIL_ID));
            entMarketingPromotionDetailObject.setMaterialId(pstMarketingPromotionDetailObject.getlong(FLD_MATERIAL_ID));
            entMarketingPromotionDetailObject.setQuantity(pstMarketingPromotionDetailObject.getInt(FLD_QUANTITY));
            entMarketingPromotionDetailObject.setMarketingPromotionTypeId(pstMarketingPromotionDetailObject.getlong(FLD_MARKETING_PROMOTION_TYPE_ID));
            entMarketingPromotionDetailObject.setValidForMultiplication(pstMarketingPromotionDetailObject.getString(FLD_VALID_FOR_MULTIPLICATION));
            entMarketingPromotionDetailObject.setRegularPrice(pstMarketingPromotionDetailObject.getdouble(FLD_REGULAR_PRICE));
            entMarketingPromotionDetailObject.setPromotionPrice(pstMarketingPromotionDetailObject.getdouble(FLD_PROMOTION_PRICE));
            entMarketingPromotionDetailObject.setCost(pstMarketingPromotionDetailObject.getdouble(FLD_COST));
            return entMarketingPromotionDetailObject;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailObject(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionDetailObject entMarketingPromotionDetailObject = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionDetailObject;
        return entMarketingPromotionDetailObject.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionDetailObject entMarketingPromotionDetailObject) throws DBException {
        try {
            if (entMarketingPromotionDetailObject.getOID() != 0) {
                PstMarketingPromotionDetailObject pstMarketingPromotionDetailObject = new PstMarketingPromotionDetailObject(entMarketingPromotionDetailObject.getOID());
                pstMarketingPromotionDetailObject.setLong(FLD_MARKETING_PROMOTION_DETAIL_ID, entMarketingPromotionDetailObject.getMarketingPromotionDetailId());
                pstMarketingPromotionDetailObject.setLong(FLD_MATERIAL_ID, entMarketingPromotionDetailObject.getMaterialId());
                pstMarketingPromotionDetailObject.setInt(FLD_QUANTITY, entMarketingPromotionDetailObject.getQuantity());
                pstMarketingPromotionDetailObject.setLong(FLD_MARKETING_PROMOTION_TYPE_ID, entMarketingPromotionDetailObject.getMarketingPromotionTypeId());
                pstMarketingPromotionDetailObject.setString(FLD_VALID_FOR_MULTIPLICATION, entMarketingPromotionDetailObject.getValidForMultiplication());
                pstMarketingPromotionDetailObject.setDouble(FLD_REGULAR_PRICE, entMarketingPromotionDetailObject.getRegularPrice());
                pstMarketingPromotionDetailObject.setDouble(FLD_PROMOTION_PRICE, entMarketingPromotionDetailObject.getPromotionPrice());
                pstMarketingPromotionDetailObject.setDouble(FLD_COST, entMarketingPromotionDetailObject.getCost());
                pstMarketingPromotionDetailObject.update();
                return entMarketingPromotionDetailObject.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailObject(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionDetailObject) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionDetailObject pstMarketingPromotionDetailObject = new PstMarketingPromotionDetailObject(oid);
            pstMarketingPromotionDetailObject.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailObject(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionDetailObject entMarketingPromotionDetailObject) throws DBException {
        try {
            PstMarketingPromotionDetailObject pstMarketingPromotionDetailObject = new PstMarketingPromotionDetailObject(0);
            pstMarketingPromotionDetailObject.setLong(FLD_MARKETING_PROMOTION_DETAIL_ID, entMarketingPromotionDetailObject.getMarketingPromotionDetailId());
            pstMarketingPromotionDetailObject.setLong(FLD_MATERIAL_ID, entMarketingPromotionDetailObject.getMaterialId());
            pstMarketingPromotionDetailObject.setInt(FLD_QUANTITY, entMarketingPromotionDetailObject.getQuantity());
            pstMarketingPromotionDetailObject.setLong(FLD_MARKETING_PROMOTION_TYPE_ID, entMarketingPromotionDetailObject.getMarketingPromotionTypeId());
            pstMarketingPromotionDetailObject.setString(FLD_VALID_FOR_MULTIPLICATION, entMarketingPromotionDetailObject.getValidForMultiplication());
            pstMarketingPromotionDetailObject.setDouble(FLD_REGULAR_PRICE, entMarketingPromotionDetailObject.getRegularPrice());
            pstMarketingPromotionDetailObject.setDouble(FLD_PROMOTION_PRICE, entMarketingPromotionDetailObject.getPromotionPrice());
            pstMarketingPromotionDetailObject.setDouble(FLD_COST, entMarketingPromotionDetailObject.getCost());
            pstMarketingPromotionDetailObject.insert();
            entMarketingPromotionDetailObject.setOID(pstMarketingPromotionDetailObject.getlong(FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailObject(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionDetailObject.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionDetailObject) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionDetailObject entMarketingPromotionDetailObject) {
        try {
            entMarketingPromotionDetailObject.setOID(rs.getLong(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID]));
            entMarketingPromotionDetailObject.setMarketingPromotionDetailId(rs.getLong(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID]));
            entMarketingPromotionDetailObject.setMaterialId(rs.getLong(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MATERIAL_ID]));
            entMarketingPromotionDetailObject.setQuantity(rs.getInt(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_QUANTITY]));
            entMarketingPromotionDetailObject.setMarketingPromotionTypeId(rs.getLong(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_TYPE_ID]));
            entMarketingPromotionDetailObject.setValidForMultiplication(rs.getString(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_VALID_FOR_MULTIPLICATION]));
            entMarketingPromotionDetailObject.setRegularPrice(rs.getDouble(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_REGULAR_PRICE]));
            entMarketingPromotionDetailObject.setPromotionPrice(rs.getDouble(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_PROMOTION_PRICE]));
            entMarketingPromotionDetailObject.setCost(rs.getDouble(PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_COST]));
            // field join
            entMarketingPromotionDetailObject.setItemSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
            entMarketingPromotionDetailObject.setItemBarcode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
            entMarketingPromotionDetailObject.setItemName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            entMarketingPromotionDetailObject.setItemCategory(rs.getString("catname"));
            entMarketingPromotionDetailObject.setPromoTypeName(rs.getString(PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME]));
        } catch (Exception e) {
        }
    }

    public static void resultToObjectJoin(ResultSet rs, MarketingPromotionDetailObject entMarketingPromotionDetailObject) {
        try {
            //entMarketingPromotionDetailSubject.setOID(rs.getLong(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID]));
            //entMarketingPromotionDetailSubject.setMarketingPromotionDetailId(rs.getLong(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID]));
            //entMarketingPromotionDetailSubject.setMaterialId(rs.getLong(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MATERIAL_ID]));
            //entMarketingPromotionDetailSubject.setQuantity(rs.getInt(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY]));
            //entMarketingPromotionDetailSubject.setValidForMultiplication(rs.getString(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION]));
            entMarketingPromotionDetailObject.setRegularPrice(rs.getDouble("regular_price"));
            entMarketingPromotionDetailObject.setCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
            //entMarketingPromotionDetailSubject.setGrossProfit(rs.getDouble(PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT]));
            
            entMarketingPromotionDetailObject.setItemId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
            entMarketingPromotionDetailObject.setItemSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
            entMarketingPromotionDetailObject.setItemBarcode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
            entMarketingPromotionDetailObject.setItemName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            entMarketingPromotionDetailObject.setItemCategory(rs.getString("catname"));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL_OBJECT;
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
                MarketingPromotionDetailObject entMarketingPromotionDetailObject = new MarketingPromotionDetailObject();
                resultToObject(rs, entMarketingPromotionDetailObject);
                lists.add(entMarketingPromotionDetailObject);
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
            String sql = "SELECT material.SKU, material.BARCODE, material.NAME, category.NAME AS catname, promo.PROMOTION_TYPE_NAME, objek.*"
                    + " FROM marketing_promotion_detail_object AS objek"
                    + " INNER JOIN pos_material AS material"
                    + " ON objek.MATERIAL_ID = material.MATERIAL_ID"
                    + " INNER JOIN pos_category AS category"
                    + " ON category.CATEGORY_ID = material.CATEGORY_ID"
                    + " INNER JOIN marketing_promotion_type AS promo"
                    + " ON objek.MARKETING_PROMOTION_TYPE_ID = promo.MARKETING_PROMOTION_TYPE_ID";
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
                MarketingPromotionDetailObject entMarketingPromotionDetailObject = new MarketingPromotionDetailObject();
                resultToObject(rs, entMarketingPromotionDetailObject);
                lists.add(entMarketingPromotionDetailObject);
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
                    + " AVG(pricemap.PRICE) AS regular_price, material.AVERAGE_PRICE"
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
                MarketingPromotionDetailObject entMarketingPromotionDetailObject = new MarketingPromotionDetailObject();
                resultToObjectJoin(rs, entMarketingPromotionDetailObject);
                lists.add(entMarketingPromotionDetailObject);
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

    public static boolean checkOID(long entMarketingPromotionDetailObjectId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL_OBJECT + " WHERE "
                    + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + " = " + entMarketingPromotionDetailObjectId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + ") FROM " + TBL_MARKETING_PROMOTION_DETAIL_OBJECT;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + ")"
                    + " FROM " + TBL_MARKETING_PROMOTION_DETAIL_OBJECT + " AS objek"
                    + " INNER JOIN pos_material AS material"
                    + " ON objek.MATERIAL_ID = material.MATERIAL_ID"
                    + " INNER JOIN pos_category AS category"
                    + " ON category.CATEGORY_ID = material.CATEGORY_ID"
                    + " INNER JOIN marketing_promotion_type AS promo"
                    + " ON objek.MARKETING_PROMOTION_TYPE_ID = promo.MARKETING_PROMOTION_TYPE_ID";
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
                    MarketingPromotionDetailObject entMarketingPromotionDetailObject = (MarketingPromotionDetailObject) list.get(ls);
                    if (oid == entMarketingPromotionDetailObject.getOID()) {
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
