/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetailassign;

import com.dimata.posbo.db.*;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMarketingPromotionDetailAssign extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_DETAIL_ASSIGN = "marketing_promotion_detail_assign";
    public static final int FLD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID = 1;
    public static final int FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID = 2;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_DETAIL_ASSIGN_ID",
        "MARKETING_PROMOTION_DETAIL_SUBJECT_ID",
        "MARKETING_PROMOTION_DETAIL_OBJECT_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstMarketingPromotionDetailAssign() {
    }

    public PstMarketingPromotionDetailAssign(int i) throws DBException {
        super(new PstMarketingPromotionDetailAssign());
    }

    public PstMarketingPromotionDetailAssign(String sOid) throws DBException {
        super(new PstMarketingPromotionDetailAssign(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionDetailAssign(long lOid) throws DBException {
        super(new PstMarketingPromotionDetailAssign(0));
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
        return TBL_MARKETING_PROMOTION_DETAIL_ASSIGN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionDetailAssign().getClass().getName();
    }

    public static MarketingPromotionDetailAssign fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionDetailAssign entMarketingPromotionDetailAssign = new MarketingPromotionDetailAssign();
            PstMarketingPromotionDetailAssign pstMarketingPromotionDetailAssign = new PstMarketingPromotionDetailAssign(oid);
            entMarketingPromotionDetailAssign.setOID(oid);
            entMarketingPromotionDetailAssign.setMarketingPromotionDetailSubjectId(pstMarketingPromotionDetailAssign.getLong(FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID));
            entMarketingPromotionDetailAssign.setMarketingPromotionDetailObjectId(pstMarketingPromotionDetailAssign.getLong(FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID));
            return entMarketingPromotionDetailAssign;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailAssign(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionDetailAssign entMarketingPromotionDetailAssign = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionDetailAssign;
        return entMarketingPromotionDetailAssign.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionDetailAssign entMarketingPromotionDetailAssign) throws DBException {
        try {
            if (entMarketingPromotionDetailAssign.getOID() != 0) {
                PstMarketingPromotionDetailAssign pstMarketingPromotionDetailAssign = new PstMarketingPromotionDetailAssign(entMarketingPromotionDetailAssign.getOID());
                pstMarketingPromotionDetailAssign.setLong(FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID, entMarketingPromotionDetailAssign.getMarketingPromotionDetailSubjectId());
                pstMarketingPromotionDetailAssign.setLong(FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID, entMarketingPromotionDetailAssign.getMarketingPromotionDetailObjectId());
                pstMarketingPromotionDetailAssign.update();
                return entMarketingPromotionDetailAssign.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailAssign(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionDetailAssign) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionDetailAssign pstMarketingPromotionDetailAssign = new PstMarketingPromotionDetailAssign(oid);
            pstMarketingPromotionDetailAssign.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailAssign(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionDetailAssign entMarketingPromotionDetailAssign) throws DBException {
        try {
            PstMarketingPromotionDetailAssign pstMarketingPromotionDetailAssign = new PstMarketingPromotionDetailAssign(0);
            pstMarketingPromotionDetailAssign.setLong(FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID, entMarketingPromotionDetailAssign.getMarketingPromotionDetailSubjectId());
            pstMarketingPromotionDetailAssign.setLong(FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID, entMarketingPromotionDetailAssign.getMarketingPromotionDetailObjectId());
            pstMarketingPromotionDetailAssign.insert();
            entMarketingPromotionDetailAssign.setOID(pstMarketingPromotionDetailAssign.getLong(FLD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionDetailAssign(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionDetailAssign.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionDetailAssign) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionDetailAssign entMarketingPromotionDetailAssign) {
        try {
            entMarketingPromotionDetailAssign.setOID(rs.getLong(PstMarketingPromotionDetailAssign.fieldNames[PstMarketingPromotionDetailAssign.FLD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID]));
            entMarketingPromotionDetailAssign.setMarketingPromotionDetailSubjectId(rs.getLong(PstMarketingPromotionDetailAssign.fieldNames[PstMarketingPromotionDetailAssign.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID]));
            entMarketingPromotionDetailAssign.setMarketingPromotionDetailObjectId(rs.getLong(PstMarketingPromotionDetailAssign.fieldNames[PstMarketingPromotionDetailAssign.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL_ASSIGN;
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
                MarketingPromotionDetailAssign entMarketingPromotionDetailAssign = new MarketingPromotionDetailAssign();
                resultToObject(rs, entMarketingPromotionDetailAssign);
                lists.add(entMarketingPromotionDetailAssign);
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

    public static boolean checkOID(long entMarketingPromotionDetailAssignId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_DETAIL_ASSIGN + " WHERE "
                    + PstMarketingPromotionDetailAssign.fieldNames[PstMarketingPromotionDetailAssign.FLD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID] + " = " + entMarketingPromotionDetailAssignId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionDetailAssign.fieldNames[PstMarketingPromotionDetailAssign.FLD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID] + ") FROM " + TBL_MARKETING_PROMOTION_DETAIL_ASSIGN;
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
                    MarketingPromotionDetailAssign entMarketingPromotionDetailAssign = (MarketingPromotionDetailAssign) list.get(ls);
                    if (oid == entMarketingPromotionDetailAssign.getOID()) {
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
