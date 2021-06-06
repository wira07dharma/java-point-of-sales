/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetail;

import com.dimata.marketing.entity.marketingpromotiontype.PstMarketingPromotionType;
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
public class PstMarketingPromotionDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_DETAIL = "marketing_promotion_detail";
    public static final int FLD_MARKETING_PROMOTION_DETAIL_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_ID = 1;
    public static final int FLD_PROMOTION_TYPE_ID = 2;
    public static final int FLD_REASON_OF_PROMOTION = 3;
    public static final int FLD_PROMOTION_TAGLINE = 4;
    public static final int FLD_DISCOUNT_QUANTITY_STATUS = 5;
    public static final int FLD_STATUS_APPROVE = 6;
    public static final int FLD_SUBJECT_COMBINATION = 7;
    public static final int FLD_OBJECT_COMBINATION = 8;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_DETAIL_ID",
        "MARKETING_PROMOTION_ID",
        "PROMOTION_TYPE_ID",
        "REASON_OF_PROMOTION",
        "PROMOTION_TAGLINE",
        "DISCOUNT_QUANTITY_STATUS",
        "STATUS_APPROVE",
        "SUBJECT_COMBINATION",
        "OBJECT_COMBINATION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstMarketingPromotionDetail() {
    }

    public PstMarketingPromotionDetail(int i) throws DBException {
        super(new PstMarketingPromotionDetail());
    }

    public PstMarketingPromotionDetail(String sOid) throws DBException {
        super(new PstMarketingPromotionDetail(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionDetail(long lOid) throws DBException {
        super(new PstMarketingPromotionDetail(0));
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
        return TBL_MARKETING_PROMOTION_DETAIL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionDetail().getClass().getName();
    }

    public static MarketingPromotionDetail fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionDetail entMarketingPromotionDetail = new MarketingPromotionDetail();
            PstMarketingPromotionDetail pstMarketingPromotionDetail = new PstMarketingPromotionDetail(oid);
            entMarketingPromotionDetail.setOID(oid);
            entMarketingPromotionDetail.setMarketingPromotionId(pstMarketingPromotionDetail.getlong(FLD_MARKETING_PROMOTION_ID));
            entMarketingPromotionDetail.setPromotionTypeId(pstMarketingPromotionDetail.getlong(FLD_PROMOTION_TYPE_ID));
            entMarketingPromotionDetail.setReasonOfPromotion(pstMarketingPromotionDetail.getString(FLD_REASON_OF_PROMOTION));
            entMarketingPromotionDetail.setPromotionTagline(pstMarketingPromotionDetail.getString(FLD_PROMOTION_TAGLINE));
            entMarketingPromotionDetail.setDiscountQuantityStatus(pstMarketingPromotionDetail.getString(FLD_DISCOUNT_QUANTITY_STATUS));
            entMarketingPromotionDetail.setStatusApprove(pstMarketingPromotionDetail.getString(FLD_STATUS_APPROVE));
            entMarketingPromotionDetail.setSubjectCombination(pstMarketingPromotionDetail.getString(FLD_SUBJECT_COMBINATION));
            entMarketingPromotionDetail.setObjectCombination(pstMarketingPromotionDetail.getString(FLD_OBJECT_COMBINATION));
            return entMarketingPromotionDetail;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetail(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionDetail entMarketingPromotionDetail = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionDetail;
        return entMarketingPromotionDetail.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionDetail entMarketingPromotionDetail) throws DBException {
        try {
            if (entMarketingPromotionDetail.getOID() != 0) {
                PstMarketingPromotionDetail pstMarketingPromotionDetail = new PstMarketingPromotionDetail(entMarketingPromotionDetail.getOID());
                pstMarketingPromotionDetail.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionDetail.getMarketingPromotionId());
                pstMarketingPromotionDetail.setLong(FLD_PROMOTION_TYPE_ID, entMarketingPromotionDetail.getPromotionTypeId());
                pstMarketingPromotionDetail.setString(FLD_REASON_OF_PROMOTION, entMarketingPromotionDetail.getReasonOfPromotion());
                pstMarketingPromotionDetail.setString(FLD_PROMOTION_TAGLINE, entMarketingPromotionDetail.getPromotionTagline());
                pstMarketingPromotionDetail.setString(FLD_DISCOUNT_QUANTITY_STATUS, entMarketingPromotionDetail.getDiscountQuantityStatus());
                pstMarketingPromotionDetail.setString(FLD_STATUS_APPROVE, entMarketingPromotionDetail.getStatusApprove());
                pstMarketingPromotionDetail.setString(FLD_SUBJECT_COMBINATION, entMarketingPromotionDetail.getSubjectCombination());
                pstMarketingPromotionDetail.setString(FLD_OBJECT_COMBINATION, entMarketingPromotionDetail.getObjectCombination());
                pstMarketingPromotionDetail.update();
                return entMarketingPromotionDetail.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionDetail) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionDetail pstMarketingPromotionDetail = new PstMarketingPromotionDetail(oid);
            pstMarketingPromotionDetail.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetail(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionDetail entMarketingPromotionDetail) throws DBException {
        try {
            PstMarketingPromotionDetail pstMarketingPromotionDetail = new PstMarketingPromotionDetail(0);
            pstMarketingPromotionDetail.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionDetail.getMarketingPromotionId());
            pstMarketingPromotionDetail.setLong(FLD_PROMOTION_TYPE_ID, entMarketingPromotionDetail.getPromotionTypeId());
            pstMarketingPromotionDetail.setString(FLD_REASON_OF_PROMOTION, entMarketingPromotionDetail.getReasonOfPromotion());
            pstMarketingPromotionDetail.setString(FLD_PROMOTION_TAGLINE, entMarketingPromotionDetail.getPromotionTagline());
            pstMarketingPromotionDetail.setString(FLD_DISCOUNT_QUANTITY_STATUS, entMarketingPromotionDetail.getDiscountQuantityStatus());
            pstMarketingPromotionDetail.setString(FLD_STATUS_APPROVE, entMarketingPromotionDetail.getStatusApprove());
            pstMarketingPromotionDetail.setString(FLD_SUBJECT_COMBINATION, entMarketingPromotionDetail.getSubjectCombination());
            pstMarketingPromotionDetail.setString(FLD_OBJECT_COMBINATION, entMarketingPromotionDetail.getObjectCombination());
            pstMarketingPromotionDetail.insert();
            entMarketingPromotionDetail.setOID(pstMarketingPromotionDetail.getlong(FLD_MARKETING_PROMOTION_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetail(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionDetail.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionDetail) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionDetail entMarketingPromotionDetail) {
        try {
            entMarketingPromotionDetail.setOID(rs.getLong(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID]));
            entMarketingPromotionDetail.setMarketingPromotionId(rs.getLong(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_ID]));
            entMarketingPromotionDetail.setPromotionTypeId(rs.getLong(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_PROMOTION_TYPE_ID]));
            entMarketingPromotionDetail.setReasonOfPromotion(rs.getString(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_REASON_OF_PROMOTION]));
            entMarketingPromotionDetail.setPromotionTagline(rs.getString(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_PROMOTION_TAGLINE]));
            entMarketingPromotionDetail.setPromotionName(rs.getString(PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME]));
            entMarketingPromotionDetail.setDiscountQuantityStatus(rs.getString(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_DISCOUNT_QUANTITY_STATUS]));
            entMarketingPromotionDetail.setStatusApprove(rs.getString(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_STATUS_APPROVE]));
            entMarketingPromotionDetail.setSubjectCombination(rs.getString(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_SUBJECT_COMBINATION]));
            entMarketingPromotionDetail.setObjectCombination(rs.getString(PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_OBJECT_COMBINATION]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL;
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
                MarketingPromotionDetail entMarketingPromotionDetail = new MarketingPromotionDetail();
                resultToObject(rs, entMarketingPromotionDetail);
                lists.add(entMarketingPromotionDetail);
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
            String sql = "SELECT promodetail.*,"
		    + "promotype."+ PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME]
		    + " FROM " + TBL_MARKETING_PROMOTION_DETAIL +" AS promodetail "
		    + "INNER JOIN "+PstMarketingPromotionType.TBL_MARKETING_PROMOTION_TYPE +" AS promotype "
		    + "ON promodetail."+fieldNames[FLD_PROMOTION_TYPE_ID]+" = promotype."+PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID];
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
                MarketingPromotionDetail entMarketingPromotionDetail = new MarketingPromotionDetail();
                resultToObject(rs, entMarketingPromotionDetail);
                lists.add(entMarketingPromotionDetail);
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

    public static boolean checkOID(long entMarketingPromotionDetailId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL + " WHERE "
                    + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = " + entMarketingPromotionDetailId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + ") FROM " + TBL_MARKETING_PROMOTION_DETAIL;
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
                    MarketingPromotionDetail entMarketingPromotionDetail = (MarketingPromotionDetail) list.get(ls);
                    if (oid == entMarketingPromotionDetail.getOID()) {
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
