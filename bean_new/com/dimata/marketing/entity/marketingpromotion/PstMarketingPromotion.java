/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotion;

import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.posbo.db.DBException;
import java.sql.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

/**
 *
 * @author Dewa
 */
public class PstMarketingPromotion extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION = "marketing_promotion";
    public static final int FLD_MARKETING_PROMOTION_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_PURPOSE = 1;
    public static final int FLD_MARKETING_PROMOTION_OBJECTIVES = 2;
    public static final int FLD_MARKETING_PROMOTION_EVENT = 3;
    public static final int FLD_MARKETING_PROMOTION_START = 4;
    public static final int FLD_MARKETING_PROMOTION_END = 5;
    public static final int FLD_MARKETING_PROMOTION_RECURRING = 6;
    public static final int FLD_MARKETING_PROMOTION_STANDARD_RATE_ID = 7;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_ID",
        "MARKETING_PROMOTION_PURPOSE",
        "MARKETING_PROMOTION_OBJECTIVES",
        "MARKETING_PROMOTION_EVENT",
        "MARKETING_PROMOTION_START",
        "MARKETING_PROMOTION_END",
        "MARKETING_PROMOTION_RECURRING",
        "MARKETING_PROMOTION_STANDARD_RATE_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_LONG
    };

    public PstMarketingPromotion() {
    }

    public PstMarketingPromotion(int i) throws DBException {
        super(new PstMarketingPromotion());
    }

    public PstMarketingPromotion(String sOid) throws DBException {
        super(new PstMarketingPromotion(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotion(long lOid) throws DBException {
        super(new PstMarketingPromotion(0));
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
        return TBL_MARKETING_PROMOTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotion().getClass().getName();
    }

    public static MarketingPromotion fetchExc(long oid) throws DBException {
        try {
            MarketingPromotion entMarketingPromotion = new MarketingPromotion();
            PstMarketingPromotion pstMarketingPromotion = new PstMarketingPromotion(oid);
            entMarketingPromotion.setOID(oid);
            entMarketingPromotion.setMarketingPromotionPurpose(pstMarketingPromotion.getDate(FLD_MARKETING_PROMOTION_PURPOSE));
            entMarketingPromotion.setMarketingPromotionObjectives(pstMarketingPromotion.getString(FLD_MARKETING_PROMOTION_OBJECTIVES));
            entMarketingPromotion.setMarketingPromotionEvent(pstMarketingPromotion.getString(FLD_MARKETING_PROMOTION_EVENT));
            entMarketingPromotion.setMarketingPromotionStart(pstMarketingPromotion.getDate(FLD_MARKETING_PROMOTION_START));
            entMarketingPromotion.setMarketingPromotionEnd(pstMarketingPromotion.getDate(FLD_MARKETING_PROMOTION_END));
            entMarketingPromotion.setMarketingPromotionRecurring(pstMarketingPromotion.getString(FLD_MARKETING_PROMOTION_RECURRING));
            entMarketingPromotion.setMarketingPromotionStandardRateId(pstMarketingPromotion.getlong(FLD_MARKETING_PROMOTION_STANDARD_RATE_ID));
            return entMarketingPromotion;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotion(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotion entMarketingPromotion = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotion;
        return entMarketingPromotion.getOID();
    }

    public static synchronized long updateExc(MarketingPromotion entMarketingPromotion) throws DBException {
        try {
            if (entMarketingPromotion.getOID() != 0) {
                PstMarketingPromotion pstMarketingPromotion = new PstMarketingPromotion(entMarketingPromotion.getOID());
                pstMarketingPromotion.setDate(FLD_MARKETING_PROMOTION_PURPOSE, entMarketingPromotion.getMarketingPromotionPurpose());
                pstMarketingPromotion.setString(FLD_MARKETING_PROMOTION_OBJECTIVES, entMarketingPromotion.getMarketingPromotionObjectives());
                pstMarketingPromotion.setString(FLD_MARKETING_PROMOTION_EVENT, entMarketingPromotion.getMarketingPromotionEvent());
                pstMarketingPromotion.setDate(FLD_MARKETING_PROMOTION_START, entMarketingPromotion.getMarketingPromotionStart());
                pstMarketingPromotion.setDate(FLD_MARKETING_PROMOTION_END, entMarketingPromotion.getMarketingPromotionEnd());
                pstMarketingPromotion.setString(FLD_MARKETING_PROMOTION_RECURRING, entMarketingPromotion.getMarketingPromotionRecurring());
                pstMarketingPromotion.setLong(FLD_MARKETING_PROMOTION_STANDARD_RATE_ID, entMarketingPromotion.getMarketingPromotionStandardRateId());
                pstMarketingPromotion.update();
                return entMarketingPromotion.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotion(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotion) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotion pstMarketingPromotion = new PstMarketingPromotion(oid);
            pstMarketingPromotion.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotion(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotion entMarketingPromotion) throws DBException {
        try {
            PstMarketingPromotion pstMarketingPromotion = new PstMarketingPromotion(0);
            pstMarketingPromotion.setDate(FLD_MARKETING_PROMOTION_PURPOSE, entMarketingPromotion.getMarketingPromotionPurpose());
            pstMarketingPromotion.setString(FLD_MARKETING_PROMOTION_OBJECTIVES, entMarketingPromotion.getMarketingPromotionObjectives());
            pstMarketingPromotion.setString(FLD_MARKETING_PROMOTION_EVENT, entMarketingPromotion.getMarketingPromotionEvent());
            pstMarketingPromotion.setDate(FLD_MARKETING_PROMOTION_START, entMarketingPromotion.getMarketingPromotionStart());
            pstMarketingPromotion.setDate(FLD_MARKETING_PROMOTION_END, entMarketingPromotion.getMarketingPromotionEnd());
            pstMarketingPromotion.setString(FLD_MARKETING_PROMOTION_RECURRING, entMarketingPromotion.getMarketingPromotionRecurring());
            pstMarketingPromotion.setLong(FLD_MARKETING_PROMOTION_STANDARD_RATE_ID, entMarketingPromotion.getMarketingPromotionStandardRateId());
            pstMarketingPromotion.insert();
            entMarketingPromotion.setOID(pstMarketingPromotion.getlong(FLD_MARKETING_PROMOTION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotion(0), DBException.UNKNOWN);
        }
        return entMarketingPromotion.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotion) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotion entMarketingPromotion) {
        try {
            entMarketingPromotion.setOID(rs.getLong(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID]));
            entMarketingPromotion.setMarketingPromotionPurpose(rs.getDate(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_PURPOSE]));
            entMarketingPromotion.setMarketingPromotionObjectives(rs.getString(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_OBJECTIVES]));
            entMarketingPromotion.setMarketingPromotionEvent(rs.getString(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_EVENT]));
            entMarketingPromotion.setMarketingPromotionStart(rs.getTimestamp(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_START]));
            entMarketingPromotion.setMarketingPromotionEnd(rs.getTimestamp(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_END]));
            entMarketingPromotion.setMarketingPromotionRecurring(rs.getString(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_RECURRING]));
            entMarketingPromotion.setMarketingPromotionStandardRateId(rs.getLong(PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_STANDARD_RATE_ID]));

            entMarketingPromotion.setCurrencyName(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]));
            entMarketingPromotion.setCurrencyCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION;
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
                MarketingPromotion entMarketingPromotion = new MarketingPromotion();
                resultToObject(rs, entMarketingPromotion);
                lists.add(entMarketingPromotion);
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
            String sql = "SELECT DISTINCT marpro.*, currency.NAME, currency.CODE"
                    + " FROM marketing_promotion AS marpro"
                    + " LEFT JOIN standard_rate AS standard"
                    + " ON marpro.MARKETING_PROMOTION_STANDARD_RATE_ID = standard.STANDARD_RATE_ID"
                    + " LEFT JOIN currency_type AS currency"
                    + " ON standard.CURRENCY_TYPE_ID = currency.CURRENCY_TYPE_ID"
                    + " LEFT JOIN marketing_promotion_location AS location"
                    + " ON location.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN location AS loc"
                    + " ON loc.LOCATION_ID = location.LOCATION_ID"
                    + " LEFT JOIN marketing_promotion_member_type AS marty"
                    + " ON marty.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN marketing_promotion_price_type AS marpy"
                    + " ON marpy.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN member_group AS member"
                    + " ON member.MEMBER_GROUP_ID = marty.MEMBER_TYPE_ID"
                    + " LEFT OUTER JOIN marketing_promotion_detail AS marde"
                    + " ON marde.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN marketing_promotion_detail_subject AS subjek"
                    + " ON subjek.MARKETING_PROMOTION_DETAIL_ID = marde.MARKETING_PROMOTION_DETAIL_ID"
                    + " LEFT JOIN marketing_promotion_detail_object AS objek"
                    + " ON objek.MARKETING_PROMOTION_DETAIL_ID = marde.MARKETING_PROMOTION_DETAIL_ID"
                    + " LEFT JOIN pos_material AS material"
                    + " ON material.MATERIAL_ID = subjek.MATERIAL_ID"
                    + " OR material.MATERIAL_ID = objek.MATERIAL_ID";
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
                MarketingPromotion entMarketingPromotion = new MarketingPromotion();
                resultToObject(rs, entMarketingPromotion);
                lists.add(entMarketingPromotion);
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

    public static boolean checkOID(long entMarketingPromotionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION + " WHERE "
                    + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = " + entMarketingPromotionId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + ") FROM " + TBL_MARKETING_PROMOTION;
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
            String sql = "SELECT COUNT(DISTINCT marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + ")"
                    + " FROM " + TBL_MARKETING_PROMOTION + " AS marpro"
                    + " LEFT JOIN standard_rate AS standard"
                    + " ON marpro.MARKETING_PROMOTION_STANDARD_RATE_ID = standard.STANDARD_RATE_ID"
                    + " LEFT JOIN currency_type AS currency"
                    + " ON standard.CURRENCY_TYPE_ID = currency.CURRENCY_TYPE_ID"
                    + " LEFT JOIN marketing_promotion_location AS location"
                    + " ON location.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN location AS loc"
                    + " ON loc.LOCATION_ID = location.LOCATION_ID"
                    + " LEFT JOIN marketing_promotion_member_type AS marty"
                    + " ON marty.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN marketing_promotion_price_type AS marpy"
                    + " ON marpy.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN member_group AS member"
                    + " ON member.MEMBER_GROUP_ID = marty.MEMBER_TYPE_ID"
                    + " LEFT JOIN marketing_promotion_detail AS marde"
                    + " ON marde.MARKETING_PROMOTION_ID = marpro.MARKETING_PROMOTION_ID"
                    + " LEFT JOIN marketing_promotion_detail_subject AS subjek"
                    + " ON subjek.MARKETING_PROMOTION_DETAIL_ID = marde.MARKETING_PROMOTION_DETAIL_ID"
                    + " LEFT JOIN marketing_promotion_detail_object AS objek"
                    + " ON objek.MARKETING_PROMOTION_DETAIL_ID = marde.MARKETING_PROMOTION_DETAIL_ID"
                    + " LEFT JOIN pos_material AS material"
                    + " ON material.MATERIAL_ID = subjek.MATERIAL_ID"
                    + " OR material.MATERIAL_ID = objek.MATERIAL_ID";
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
                    MarketingPromotion entMarketingPromotion = (MarketingPromotion) list.get(ls);
                    if (oid == entMarketingPromotion.getOID()) {
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
