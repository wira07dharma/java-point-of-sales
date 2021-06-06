/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotionmembertype;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMarketingPromotionMemberType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_MEMBER_TYPE = "marketing_promotion_member_type";
    public static final int FLD_MARKETING_PROMOTION_MEMBER_TYPE_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_ID = 1;
    public static final int FLD_MEMBER_TYPE_ID = 2;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_MEMBER_TYPE_ID",
        "MARKETING_PROMOTION_ID",
        "MEMBER_TYPE_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstMarketingPromotionMemberType() {
    }

    public PstMarketingPromotionMemberType(int i) throws DBException {
        super(new PstMarketingPromotionMemberType());
    }

    public PstMarketingPromotionMemberType(String sOid) throws DBException {
        super(new PstMarketingPromotionMemberType(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionMemberType(long lOid) throws DBException {
        super(new PstMarketingPromotionMemberType(0));
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
        return TBL_MARKETING_PROMOTION_MEMBER_TYPE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionMemberType().getClass().getName();
    }

    public static MarketingPromotionMemberType fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionMemberType entMarketingPromotionMemberType = new MarketingPromotionMemberType();
            PstMarketingPromotionMemberType pstMarketingPromotionMemberType = new PstMarketingPromotionMemberType(oid);
            entMarketingPromotionMemberType.setOID(oid);
            entMarketingPromotionMemberType.setMarketingPromotionId(pstMarketingPromotionMemberType.getLong(FLD_MARKETING_PROMOTION_ID));
            entMarketingPromotionMemberType.setMemberTypeId(pstMarketingPromotionMemberType.getLong(FLD_MEMBER_TYPE_ID));
            return entMarketingPromotionMemberType;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionMemberType(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionMemberType entMarketingPromotionMemberType = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionMemberType;
        return entMarketingPromotionMemberType.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionMemberType entMarketingPromotionMemberType) throws DBException {
        try {
            if (entMarketingPromotionMemberType.getOID() != 0) {
                PstMarketingPromotionMemberType pstMarketingPromotionMemberType = new PstMarketingPromotionMemberType(entMarketingPromotionMemberType.getOID());
                pstMarketingPromotionMemberType.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionMemberType.getMarketingPromotionId());
                pstMarketingPromotionMemberType.setLong(FLD_MEMBER_TYPE_ID, entMarketingPromotionMemberType.getMemberTypeId());
                pstMarketingPromotionMemberType.update();
                return entMarketingPromotionMemberType.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionMemberType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionMemberType) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionMemberType pstMarketingPromotionMemberType = new PstMarketingPromotionMemberType(oid);
            pstMarketingPromotionMemberType.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionMemberType(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionMemberType entMarketingPromotionMemberType) throws DBException {
        try {
            PstMarketingPromotionMemberType pstMarketingPromotionMemberType = new PstMarketingPromotionMemberType(0);
            pstMarketingPromotionMemberType.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionMemberType.getMarketingPromotionId());
            pstMarketingPromotionMemberType.setLong(FLD_MEMBER_TYPE_ID, entMarketingPromotionMemberType.getMemberTypeId());
            pstMarketingPromotionMemberType.insert();
            entMarketingPromotionMemberType.setOID(pstMarketingPromotionMemberType.getlong(FLD_MARKETING_PROMOTION_MEMBER_TYPE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionMemberType(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionMemberType.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionMemberType) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionMemberType entMarketingPromotionMemberType) {
        try {
            entMarketingPromotionMemberType.setOID(rs.getLong(PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MARKETING_PROMOTION_MEMBER_TYPE_ID]));
            entMarketingPromotionMemberType.setMarketingPromotionId(rs.getLong(PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MARKETING_PROMOTION_ID]));
            entMarketingPromotionMemberType.setMemberTypeId(rs.getLong(PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MEMBER_TYPE_ID]));
            entMarketingPromotionMemberType.setMemberTypeName(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_MEMBER_TYPE;
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
                MarketingPromotionMemberType entMarketingPromotionMemberType = new MarketingPromotionMemberType();
                resultToObject(rs, entMarketingPromotionMemberType);
                lists.add(entMarketingPromotionMemberType);
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
                    + "member." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME]
                    + " FROM " + TBL_MARKETING_PROMOTION_MEMBER_TYPE + " AS marpro"
                    + " INNER JOIN " + PstMemberGroup.TBL_MEMBER_GROUP + " AS member"
                    + " ON marpro." + fieldNames[FLD_MEMBER_TYPE_ID] + " = member." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID];
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
                MarketingPromotionMemberType entMarketingPromotionMemberType = new MarketingPromotionMemberType();
                resultToObject(rs, entMarketingPromotionMemberType);
                lists.add(entMarketingPromotionMemberType);
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

    public static boolean checkOID(long entMarketingPromotionMemberTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_MEMBER_TYPE + " WHERE "
                    + PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MARKETING_PROMOTION_MEMBER_TYPE_ID] + " = " + entMarketingPromotionMemberTypeId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MARKETING_PROMOTION_MEMBER_TYPE_ID] + ") FROM " + TBL_MARKETING_PROMOTION_MEMBER_TYPE;
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
                    MarketingPromotionMemberType entMarketingPromotionMemberType = (MarketingPromotionMemberType) list.get(ls);
                    if (oid == entMarketingPromotionMemberType.getOID()) {
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
