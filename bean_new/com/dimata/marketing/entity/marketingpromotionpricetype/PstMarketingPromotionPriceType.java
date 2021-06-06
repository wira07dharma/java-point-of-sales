/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotionpricetype;

import com.dimata.common.entity.payment.PstPriceType;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMarketingPromotionPriceType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_PRICE_TYPE = "marketing_promotion_price_type";
    public static final int FLD_MARKETING_PROMOTION_PRICE_TYPE_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_ID = 1;
    public static final int FLD_PRICE_TYPE_ID = 2;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_PRICE_TYPE_ID",
        "MARKETING_PROMOTION_ID",
        "PRICE_TYPE_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstMarketingPromotionPriceType() {
    }

    public PstMarketingPromotionPriceType(int i) throws DBException {
        super(new PstMarketingPromotionPriceType());
    }

    public PstMarketingPromotionPriceType(String sOid) throws DBException {
        super(new PstMarketingPromotionPriceType(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionPriceType(long lOid) throws DBException {
        super(new PstMarketingPromotionPriceType(0));
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
        return TBL_MARKETING_PROMOTION_PRICE_TYPE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionPriceType().getClass().getName();
    }

    public static MarketingPromotionPriceType fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionPriceType entMarketingPromotionPriceType = new MarketingPromotionPriceType();
            PstMarketingPromotionPriceType pstMarketingPromotionPriceType = new PstMarketingPromotionPriceType(oid);
            entMarketingPromotionPriceType.setOID(oid);
            entMarketingPromotionPriceType.setMarketingPromotionId(pstMarketingPromotionPriceType.getLong(FLD_MARKETING_PROMOTION_ID));
            entMarketingPromotionPriceType.setPriceTypeId(pstMarketingPromotionPriceType.getLong(FLD_PRICE_TYPE_ID));
            return entMarketingPromotionPriceType;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionPriceType(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionPriceType entMarketingPromotionPriceType = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionPriceType;
        return entMarketingPromotionPriceType.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionPriceType entMarketingPromotionPriceType) throws DBException {
        try {
            if (entMarketingPromotionPriceType.getOID() != 0) {
                PstMarketingPromotionPriceType pstMarketingPromotionPriceType = new PstMarketingPromotionPriceType(entMarketingPromotionPriceType.getOID());
                pstMarketingPromotionPriceType.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionPriceType.getMarketingPromotionId());
                pstMarketingPromotionPriceType.setLong(FLD_PRICE_TYPE_ID, entMarketingPromotionPriceType.getPriceTypeId());
                pstMarketingPromotionPriceType.update();
                return entMarketingPromotionPriceType.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionPriceType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionPriceType) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionPriceType pstMarketingPromotionPriceType = new PstMarketingPromotionPriceType(oid);
            pstMarketingPromotionPriceType.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionPriceType(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionPriceType entMarketingPromotionPriceType) throws DBException {
        try {
            PstMarketingPromotionPriceType pstMarketingPromotionPriceType = new PstMarketingPromotionPriceType(0);
            pstMarketingPromotionPriceType.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionPriceType.getMarketingPromotionId());
            pstMarketingPromotionPriceType.setLong(FLD_PRICE_TYPE_ID, entMarketingPromotionPriceType.getPriceTypeId());
            pstMarketingPromotionPriceType.insert();
            entMarketingPromotionPriceType.setOID(pstMarketingPromotionPriceType.getlong(FLD_MARKETING_PROMOTION_PRICE_TYPE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionPriceType(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionPriceType.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionPriceType) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionPriceType entMarketingPromotionPriceType) {
        try {
            entMarketingPromotionPriceType.setOID(rs.getLong(PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_MARKETING_PROMOTION_PRICE_TYPE_ID]));
            entMarketingPromotionPriceType.setMarketingPromotionId(rs.getLong(PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_MARKETING_PROMOTION_ID]));
            entMarketingPromotionPriceType.setPriceTypeId(rs.getLong(PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_PRICE_TYPE_ID]));
            entMarketingPromotionPriceType.setPriceTypeName(rs.getString(PstPriceType.fieldNames[PstPriceType.FLD_NAME]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_PRICE_TYPE;
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
                MarketingPromotionPriceType entMarketingPromotionPriceType = new MarketingPromotionPriceType();
                resultToObject(rs, entMarketingPromotionPriceType);
                lists.add(entMarketingPromotionPriceType);
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
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT marpro.*,"
                    + "price." + PstPriceType.fieldNames[PstPriceType.FLD_NAME]
                    + " FROM " + TBL_MARKETING_PROMOTION_PRICE_TYPE + " AS marpro"
                    + " INNER JOIN " + PstPriceType.TBL_POS_PRICE_TYPE + " AS price"
                    + " ON marpro." + fieldNames[FLD_PRICE_TYPE_ID] + " = price." + PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID];
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
                MarketingPromotionPriceType entMarketingPromotionPriceType = new MarketingPromotionPriceType();
                resultToObject(rs, entMarketingPromotionPriceType);
                lists.add(entMarketingPromotionPriceType);
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

    public static boolean checkOID(long entMarketingPromotionPriceTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_PRICE_TYPE + " WHERE "
                    + PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_MARKETING_PROMOTION_PRICE_TYPE_ID] + " = " + entMarketingPromotionPriceTypeId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_MARKETING_PROMOTION_PRICE_TYPE_ID] + ") FROM " + TBL_MARKETING_PROMOTION_PRICE_TYPE;
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
                    MarketingPromotionPriceType entMarketingPromotionPriceType = (MarketingPromotionPriceType) list.get(ls);
                    if (oid == entMarketingPromotionPriceType.getOID()) {
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
