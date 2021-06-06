/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.loationsetting;

/**
 *
 * @author dimata005
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstLocationSetting extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_LOCATION_SETTING = "location_setting";
    public static final int FLD_LOCATION_SETTING_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_MOBILE_ICON = 3;
    public static final int FLD_DEKSTOP_ICON = 4;
    public static final int FLD_TYPE = 5;
    public static final int FLD_DEFAULT_ITEM = 6;
    public static final int FLD_DEFAULT_CATEGORY = 7;
    public static final int FLD_DEFAULT_SUB_CATEGORY = 8;

    public static String[] fieldNames = {
        "LOCATION_SETTING_ID",
        "LOCATION_ID",
        "NAME",
        "MOBILE_ICON",
        "DESKTOP_ICON",
        "TYPE",
        "MATERIAL_ID",
        "DEFAULT_CATEGORY",
        "DEFAULT_SUB_CATEGORY"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    public PstLocationSetting() {
    }

    public PstLocationSetting(int i) throws DBException {
        super(new PstLocationSetting());
    }

    public PstLocationSetting(String sOid) throws DBException {
        super(new PstLocationSetting(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstLocationSetting(long lOid) throws DBException {
        super(new PstLocationSetting(0));
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
        return TBL_LOCATION_SETTING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstLocationSetting().getClass().getName();
    }

    public static LocationSetting fetchExc(long oid) throws DBException {
        try {
            LocationSetting entLocationSetting = new LocationSetting();
            PstLocationSetting pstLocationSetting = new PstLocationSetting(oid);
            entLocationSetting.setOID(oid);
            entLocationSetting.setLocationId(pstLocationSetting.getLong(FLD_LOCATION_ID));
            entLocationSetting.setName(pstLocationSetting.getString(FLD_NAME));
            entLocationSetting.setMobileIcon(pstLocationSetting.getString(FLD_MOBILE_ICON));
            entLocationSetting.setDesktopIcon(pstLocationSetting.getString(FLD_DEKSTOP_ICON));
            entLocationSetting.setType(pstLocationSetting.getInt(FLD_TYPE));
            entLocationSetting.setDefaultItem(pstLocationSetting.getLong(FLD_DEFAULT_ITEM));
            entLocationSetting.setDefaultCategory(pstLocationSetting.getLong(FLD_DEFAULT_CATEGORY));
            entLocationSetting.setDefaultSubCategory(pstLocationSetting.getLong(FLD_DEFAULT_SUB_CATEGORY));
            return entLocationSetting;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocationSetting(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        LocationSetting entLocationSetting = fetchExc(entity.getOID());
        entity = (Entity) entLocationSetting;
        return entLocationSetting.getOID();
    }

    public static synchronized long updateExc(LocationSetting entLocationSetting) throws DBException {
        try {
            if (entLocationSetting.getOID() != 0) {
                PstLocationSetting pstLocationSetting = new PstLocationSetting(entLocationSetting.getOID());
                pstLocationSetting.setLong(FLD_LOCATION_ID, entLocationSetting.getLocationId());
                pstLocationSetting.setString(FLD_NAME, entLocationSetting.getName());
                pstLocationSetting.setString(FLD_MOBILE_ICON, entLocationSetting.getMobileIcon());
                pstLocationSetting.setString(FLD_DEKSTOP_ICON, entLocationSetting.getDesktopIcon());
                pstLocationSetting.setInt(FLD_TYPE, entLocationSetting.getType());
                pstLocationSetting.setLong(FLD_DEFAULT_ITEM, entLocationSetting.getDefaultItem());
                pstLocationSetting.setLong(FLD_DEFAULT_CATEGORY, entLocationSetting.getDefaultCategory());
                pstLocationSetting.setLong(FLD_DEFAULT_SUB_CATEGORY, entLocationSetting.getDefaultSubCategory());
                pstLocationSetting.update();
                return entLocationSetting.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocationSetting(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((LocationSetting) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstLocationSetting pstLocationSetting = new PstLocationSetting(oid);
            pstLocationSetting.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocationSetting(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(LocationSetting entLocationSetting) throws DBException {
        try {
            PstLocationSetting pstLocationSetting = new PstLocationSetting(0);
            pstLocationSetting.setLong(FLD_LOCATION_ID, entLocationSetting.getLocationId());
            pstLocationSetting.setString(FLD_NAME, entLocationSetting.getName());
            pstLocationSetting.setString(FLD_MOBILE_ICON, entLocationSetting.getMobileIcon());
            pstLocationSetting.setString(FLD_DEKSTOP_ICON, entLocationSetting.getDesktopIcon());
            pstLocationSetting.setInt(FLD_TYPE, entLocationSetting.getType());
            pstLocationSetting.setLong(FLD_DEFAULT_ITEM, entLocationSetting.getDefaultItem());
            pstLocationSetting.setLong(FLD_DEFAULT_CATEGORY, entLocationSetting.getDefaultCategory());
            pstLocationSetting.setLong(FLD_DEFAULT_SUB_CATEGORY, entLocationSetting.getDefaultSubCategory());
            pstLocationSetting.insert();
            entLocationSetting.setOID(pstLocationSetting.getlong(FLD_LOCATION_SETTING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocationSetting(0), DBException.UNKNOWN);
        }
        return entLocationSetting.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((LocationSetting) entity);
    }

    public static void resultToObject(ResultSet rs, LocationSetting entLocationSetting) {
        try {
            entLocationSetting.setOID(rs.getLong(PstLocationSetting.fieldNames[PstLocationSetting.FLD_LOCATION_SETTING_ID]));
            entLocationSetting.setLocationId(rs.getLong(PstLocationSetting.fieldNames[PstLocationSetting.FLD_LOCATION_ID]));
            entLocationSetting.setName(rs.getString(PstLocationSetting.fieldNames[PstLocationSetting.FLD_NAME]));
            entLocationSetting.setMobileIcon(rs.getString(PstLocationSetting.fieldNames[PstLocationSetting.FLD_MOBILE_ICON]));
            entLocationSetting.setDesktopIcon(rs.getString(PstLocationSetting.fieldNames[PstLocationSetting.FLD_DEKSTOP_ICON]));
            entLocationSetting.setType(rs.getInt(PstLocationSetting.fieldNames[PstLocationSetting.FLD_TYPE]));
            entLocationSetting.setDefaultItem(rs.getLong(PstLocationSetting.fieldNames[PstLocationSetting.FLD_DEFAULT_ITEM]));
            entLocationSetting.setDefaultCategory(rs.getLong(PstLocationSetting.fieldNames[PstLocationSetting.FLD_DEFAULT_CATEGORY]));
            entLocationSetting.setDefaultSubCategory(rs.getLong(PstLocationSetting.fieldNames[PstLocationSetting.FLD_DEFAULT_SUB_CATEGORY]));
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
            String sql = "SELECT * FROM " + TBL_LOCATION_SETTING;
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
                LocationSetting entLocationSetting = new LocationSetting();
                resultToObject(rs, entLocationSetting);
                lists.add(entLocationSetting);
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

    public static boolean checkOID(long entLocationSettingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_LOCATION_SETTING + " WHERE "
                    + PstLocationSetting.fieldNames[PstLocationSetting.FLD_LOCATION_SETTING_ID] + " = " + entLocationSettingId;
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
            String sql = "SELECT COUNT(" + PstLocationSetting.fieldNames[PstLocationSetting.FLD_LOCATION_SETTING_ID] + ") FROM " + TBL_LOCATION_SETTING;
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
                    LocationSetting entLocationSetting = (LocationSetting) list.get(ls);
                    if (oid == entLocationSetting.getOID()) {
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
        } else if (start == (vectSize - recordToGet)) {
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
        return cmd;
    }
}
