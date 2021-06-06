/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotionlocation;

import com.dimata.common.entity.location.PstLocation;
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
public class PstMarketingPromotionLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_PROMOTION_LOCATION = "marketing_promotion_location";
    public static final int FLD_MARKETING_PROMOTION_LOCATION_ID = 0;
    public static final int FLD_MARKETING_PROMOTION_ID = 1;
    public static final int FLD_LOCATION_ID = 2;

    public static String[] fieldNames = {
        "MARKETING_PROMOTION_LOCATION_ID",
        "MARKETING_PROMOTION_ID",
        "LOCATION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstMarketingPromotionLocation() {
    }

    public PstMarketingPromotionLocation(int i) throws DBException {
        super(new PstMarketingPromotionLocation());
    }

    public PstMarketingPromotionLocation(String sOid) throws DBException {
        super(new PstMarketingPromotionLocation(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingPromotionLocation(long lOid) throws DBException {
        super(new PstMarketingPromotionLocation(0));
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
        return TBL_MARKETING_PROMOTION_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingPromotionLocation().getClass().getName();
    }

    public static MarketingPromotionLocation fetchExc(long oid) throws DBException {
        try {
            MarketingPromotionLocation entMarketingPromotionLocation = new MarketingPromotionLocation();
            PstMarketingPromotionLocation pstMarketingPromotionLocation = new PstMarketingPromotionLocation(oid);
            entMarketingPromotionLocation.setOID(oid);
            entMarketingPromotionLocation.setMarketingPromotionId(pstMarketingPromotionLocation.getLong(FLD_MARKETING_PROMOTION_ID));
            entMarketingPromotionLocation.setLocationId(pstMarketingPromotionLocation.getLong(FLD_LOCATION_ID));
            return entMarketingPromotionLocation;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionLocation(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingPromotionLocation entMarketingPromotionLocation = fetchExc(entity.getOID());
        entity = (Entity) entMarketingPromotionLocation;
        return entMarketingPromotionLocation.getOID();
    }

    public static synchronized long updateExc(MarketingPromotionLocation entMarketingPromotionLocation) throws DBException {
        try {
            if (entMarketingPromotionLocation.getOID() != 0) {
                PstMarketingPromotionLocation pstMarketingPromotionLocation = new PstMarketingPromotionLocation(entMarketingPromotionLocation.getOID());
                pstMarketingPromotionLocation.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionLocation.getMarketingPromotionId());
                pstMarketingPromotionLocation.setLong(FLD_LOCATION_ID, entMarketingPromotionLocation.getLocationId());
                pstMarketingPromotionLocation.update();
                return entMarketingPromotionLocation.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingPromotionLocation) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingPromotionLocation pstMarketingPromotionLocation = new PstMarketingPromotionLocation(oid);
            pstMarketingPromotionLocation.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionLocation(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingPromotionLocation entMarketingPromotionLocation) throws DBException {
        try {
            PstMarketingPromotionLocation pstMarketingPromotionLocation = new PstMarketingPromotionLocation(0);
            pstMarketingPromotionLocation.setLong(FLD_MARKETING_PROMOTION_ID, entMarketingPromotionLocation.getMarketingPromotionId());
            pstMarketingPromotionLocation.setLong(FLD_LOCATION_ID, entMarketingPromotionLocation.getLocationId());
            pstMarketingPromotionLocation.insert();
            entMarketingPromotionLocation.setOID(pstMarketingPromotionLocation.getlong(FLD_MARKETING_PROMOTION_LOCATION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingPromotionLocation(0), DBException.UNKNOWN);
        }
        return entMarketingPromotionLocation.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingPromotionLocation) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingPromotionLocation entMarketingPromotionLocation) {
        try {
            entMarketingPromotionLocation.setOID(rs.getLong(PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_MARKETING_PROMOTION_LOCATION_ID]));
            entMarketingPromotionLocation.setMarketingPromotionId(rs.getLong(PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_MARKETING_PROMOTION_ID]));
            entMarketingPromotionLocation.setLocationId(rs.getLong(PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_LOCATION_ID]));
            entMarketingPromotionLocation.setLocationName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_LOCATION;
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
                MarketingPromotionLocation entMarketingPromotionLocation = new MarketingPromotionLocation();
                resultToObject(rs, entMarketingPromotionLocation);
                lists.add(entMarketingPromotionLocation);
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
            String sql = "SELECT marloc.*,"
                    + "loc." + PstLocation.fieldNames[PstLocation.FLD_NAME]
                    + " FROM " + TBL_MARKETING_PROMOTION_LOCATION + " AS marloc"
                    + " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS loc"
                    + " ON marloc." + fieldNames[FLD_LOCATION_ID] + " = loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
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
                MarketingPromotionLocation entMarketingPromotionLocation = new MarketingPromotionLocation();
                resultToObject(rs, entMarketingPromotionLocation);
                lists.add(entMarketingPromotionLocation);
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

    public static boolean checkOID(long entMarketingPromotionLocationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_PROMOTION_LOCATION + " WHERE "
                    + PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_MARKETING_PROMOTION_LOCATION_ID] + " = " + entMarketingPromotionLocationId;
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
            String sql = "SELECT COUNT(" + PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_MARKETING_PROMOTION_LOCATION_ID] + ") FROM " + TBL_MARKETING_PROMOTION_LOCATION;
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
                    MarketingPromotionLocation entMarketingPromotionLocation = (MarketingPromotionLocation) list.get(ls);
                    if (oid == entMarketingPromotionLocation.getOID()) {
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
