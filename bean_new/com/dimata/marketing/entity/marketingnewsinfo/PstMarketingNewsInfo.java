package com.dimata.marketing.entity.marketingnewsinfo;

import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.Vector;

public class PstMarketingNewsInfo extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETINGNEWSINFO = "marketing_newsandinfo";
    public static final int FLD_PROMO_ID = 0;
    public static final int FLD_TITLE = 1;
    public static final int FLD_VALID_START = 2;
    public static final int FLD_VALID_END = 3;
    public static final int FLD_DESCRIPTION = 4;
    public static final int FLD_LOCATION_ID = 5;

    public static String[] fieldNames = {
        "PROMO_ID",
        "TITLE",
        "VALID_START",
        "VALID_END",
        "DESCRIPTION",
        "LOCATION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_LONG
    };

    public PstMarketingNewsInfo() {
    }

    public PstMarketingNewsInfo(int i) throws DBException {
        super(new PstMarketingNewsInfo());
    }

    public PstMarketingNewsInfo(String sOid) throws DBException {
        super(new PstMarketingNewsInfo(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingNewsInfo(long lOid) throws DBException {
        super(new PstMarketingNewsInfo(0));
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
        return TBL_MARKETINGNEWSINFO;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingNewsInfo().getClass().getName();
    }

    public static MarketingNewsInfo fetchExc(long oid) throws DBException {
        try {
            MarketingNewsInfo entMarketingNewsInfo = new MarketingNewsInfo();
            PstMarketingNewsInfo pstMarketingNewsInfo = new PstMarketingNewsInfo(oid);
            entMarketingNewsInfo.setOID(oid);
            entMarketingNewsInfo.setTitle(pstMarketingNewsInfo.getString(FLD_TITLE));
            entMarketingNewsInfo.setValidStart(pstMarketingNewsInfo.getDate(FLD_VALID_START));
            entMarketingNewsInfo.setValidEnd(pstMarketingNewsInfo.getDate(FLD_VALID_END));
            entMarketingNewsInfo.setDescription(pstMarketingNewsInfo.getString(FLD_DESCRIPTION));
            entMarketingNewsInfo.setLocationId(pstMarketingNewsInfo.getlong(FLD_LOCATION_ID));
            return entMarketingNewsInfo;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingNewsInfo(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingNewsInfo entMarketingNewsInfo = fetchExc(entity.getOID());
        entity = (Entity) entMarketingNewsInfo;
        return entMarketingNewsInfo.getOID();
    }

    public static synchronized long updateExc(MarketingNewsInfo entMarketingNewsInfo) throws DBException {
        try {
            if (entMarketingNewsInfo.getOID() != 0) {
                PstMarketingNewsInfo pstMarketingNewsInfo = new PstMarketingNewsInfo(entMarketingNewsInfo.getOID());
                pstMarketingNewsInfo.setString(FLD_TITLE, entMarketingNewsInfo.getTitle());
                pstMarketingNewsInfo.setDate(FLD_VALID_START, entMarketingNewsInfo.getValidStart());
                pstMarketingNewsInfo.setDate(FLD_VALID_END, entMarketingNewsInfo.getValidEnd());
                pstMarketingNewsInfo.setString(FLD_DESCRIPTION, entMarketingNewsInfo.getDescription());
                pstMarketingNewsInfo.setLong(FLD_LOCATION_ID, entMarketingNewsInfo.getLocationId());
                pstMarketingNewsInfo.update();
                return entMarketingNewsInfo.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingNewsInfo(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingNewsInfo) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingNewsInfo pstMarketingNewsInfo = new PstMarketingNewsInfo(oid);
            pstMarketingNewsInfo.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingNewsInfo(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingNewsInfo entMarketingNewsInfo) throws DBException {
        try {
            PstMarketingNewsInfo pstMarketingNewsInfo = new PstMarketingNewsInfo(0);
            pstMarketingNewsInfo.setString(FLD_TITLE, entMarketingNewsInfo.getTitle());
            pstMarketingNewsInfo.setDate(FLD_VALID_START, entMarketingNewsInfo.getValidStart());
            pstMarketingNewsInfo.setDate(FLD_VALID_END, entMarketingNewsInfo.getValidEnd());
            pstMarketingNewsInfo.setString(FLD_DESCRIPTION, entMarketingNewsInfo.getDescription());
            pstMarketingNewsInfo.setLong(FLD_LOCATION_ID, entMarketingNewsInfo.getLocationId());
            pstMarketingNewsInfo.insert();
            entMarketingNewsInfo.setOID(pstMarketingNewsInfo.getlong(FLD_PROMO_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingNewsInfo(0), DBException.UNKNOWN);
        }
        return entMarketingNewsInfo.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingNewsInfo) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingNewsInfo entMarketingNewsInfo) {
        try {
            entMarketingNewsInfo.setOID(rs.getLong(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_PROMO_ID]));
            entMarketingNewsInfo.setTitle(rs.getString(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE]));
            entMarketingNewsInfo.setValidStart(rs.getDate(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START]));
            entMarketingNewsInfo.setValidEnd(rs.getDate(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END]));
            entMarketingNewsInfo.setDescription(rs.getString(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION]));
            entMarketingNewsInfo.setLocationId(rs.getLong(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_LOCATION_ID]));
        } catch (Exception e) {
        }
    }
    
    public static void resultToObjectJoin(ResultSet rs, MarketingNewsInfo entMarketingNewsInfo) {
        try {
            entMarketingNewsInfo.setOID(rs.getLong(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_PROMO_ID]));
            entMarketingNewsInfo.setTitle(rs.getString(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE]));
            entMarketingNewsInfo.setValidStart(rs.getDate(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START]));
            entMarketingNewsInfo.setValidEnd(rs.getDate(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END]));
            entMarketingNewsInfo.setDescription(rs.getString(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION]));
            entMarketingNewsInfo.setLocationId(rs.getLong(PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_LOCATION_ID]));
            entMarketingNewsInfo.setLocationName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
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
            String sql = "SELECT * FROM " + TBL_MARKETINGNEWSINFO;
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
                MarketingNewsInfo entMarketingNewsInfo = new MarketingNewsInfo();
                resultToObject(rs, entMarketingNewsInfo);
                lists.add(entMarketingNewsInfo);
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
    
    public static Vector listJoinLocation(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                + " SELECT mni.*, "
                + " loc."+PstLocation.fieldNames[PstLocation.FLD_NAME]+" "
                + " FROM "+TBL_MARKETINGNEWSINFO+" mni "
                + " LEFT JOIN "+PstLocation.TBL_P2_LOCATION+" loc "
                + " ON loc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = mni."+fieldNames[FLD_LOCATION_ID]+"";
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
                MarketingNewsInfo entMarketingNewsInfo = new MarketingNewsInfo();
                resultToObjectJoin(rs, entMarketingNewsInfo);
                lists.add(entMarketingNewsInfo);
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

    public static boolean checkOID(long entMarketingNewsInfoId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETINGNEWSINFO + " WHERE "
                    + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_PROMO_ID] + " = " + entMarketingNewsInfoId;
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
            String sql = "SELECT COUNT(" + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_PROMO_ID] + ") FROM " + TBL_MARKETINGNEWSINFO;
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
    
    public static int getCountJoinLocation(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = ""       
                + " SELECT COUNT(mni."+fieldNames[FLD_PROMO_ID]+"), "
                + " FROM "+TBL_MARKETINGNEWSINFO+" mni "
                + " LEFT JOIN "+PstLocation.TBL_P2_LOCATION+" loc "
                + " ON loc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = mni."+fieldNames[FLD_LOCATION_ID]+"";
          
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
                    MarketingNewsInfo entMarketingNewsInfo = (MarketingNewsInfo) list.get(ls);
                    if (oid == entMarketingNewsInfo.getOID()) {
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
