/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class PstCalcCogsLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CALC_COGS_LOCATION = "pos_calc_cogs_location";
    public static final int FLD_CALC_COGS_LOCATION_ID = 0;
    public static final int FLD_CALC_COGS_MAIN_ID = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final int FLD_LOCATION_CALC_COGS_TYPE = 3;

    public static String[] fieldNames = {
        "CALC_COGS_LOCATION_ID",
        "CALC_COGS_MAIN_ID",
        "LOCATION_ID",
        "LOCATION_CALC_COGS_TYPE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT
    };
    
    public static final int CALC_COGS_LOC_TYPE_COST = 0;
    public static final int CALC_COGS_LOC_TYPE_SALES = 1;

    public PstCalcCogsLocation() {
    }

    public PstCalcCogsLocation(int i) throws DBException {
        super(new PstCalcCogsLocation());
    }

    public PstCalcCogsLocation(String sOid) throws DBException {
        super(new PstCalcCogsLocation(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCalcCogsLocation(long lOid) throws DBException {
        super(new PstCalcCogsLocation(0));
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
        return TBL_CALC_COGS_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCalcCogsLocation().getClass().getName();
    }

    public static CalcCogsLocation fetchExc(long oid) throws DBException {
        try {
            CalcCogsLocation entCalcCogsLocation = new CalcCogsLocation();
            PstCalcCogsLocation pstCalcCogsLocation = new PstCalcCogsLocation(oid);
            entCalcCogsLocation.setOID(oid);
            entCalcCogsLocation.setCalcCogsMainId(pstCalcCogsLocation.getlong(FLD_CALC_COGS_MAIN_ID));
            entCalcCogsLocation.setLocationId(pstCalcCogsLocation.getlong(FLD_LOCATION_ID));
            entCalcCogsLocation.setLocationCalcCogsType(pstCalcCogsLocation.getInt(FLD_LOCATION_CALC_COGS_TYPE));
            return entCalcCogsLocation;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsLocation(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        CalcCogsLocation entCalcCogsLocation = fetchExc(entity.getOID());
        entity = (Entity) entCalcCogsLocation;
        return entCalcCogsLocation.getOID();
    }

    public static synchronized long updateExc(CalcCogsLocation entCalcCogsLocation) throws DBException {
        try {
            if (entCalcCogsLocation.getOID() != 0) {
                PstCalcCogsLocation pstCalcCogsLocation = new PstCalcCogsLocation(entCalcCogsLocation.getOID());
                pstCalcCogsLocation.setLong(FLD_CALC_COGS_MAIN_ID, entCalcCogsLocation.getCalcCogsMainId());
                pstCalcCogsLocation.setLong(FLD_LOCATION_ID, entCalcCogsLocation.getLocationId());
                pstCalcCogsLocation.setInt(FLD_LOCATION_CALC_COGS_TYPE, entCalcCogsLocation.getLocationCalcCogsType());
                pstCalcCogsLocation.update();
                return entCalcCogsLocation.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((CalcCogsLocation) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstCalcCogsLocation pstCalcCogsLocation = new PstCalcCogsLocation(oid);
            pstCalcCogsLocation.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsLocation(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(CalcCogsLocation entCalcCogsLocation) throws DBException {
        try {
            PstCalcCogsLocation pstCalcCogsLocation = new PstCalcCogsLocation(0);
            pstCalcCogsLocation.setLong(FLD_CALC_COGS_MAIN_ID, entCalcCogsLocation.getCalcCogsMainId());
            pstCalcCogsLocation.setLong(FLD_LOCATION_ID, entCalcCogsLocation.getLocationId());
            pstCalcCogsLocation.setInt(FLD_LOCATION_CALC_COGS_TYPE, entCalcCogsLocation.getLocationCalcCogsType());
            pstCalcCogsLocation.insert();
            entCalcCogsLocation.setOID(pstCalcCogsLocation.getlong(FLD_CALC_COGS_LOCATION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsLocation(0), DBException.UNKNOWN);
        }
        return entCalcCogsLocation.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((CalcCogsLocation) entity);
    }

    public static void resultToObject(ResultSet rs, CalcCogsLocation entCalcCogsLocation) {
        try {
            entCalcCogsLocation.setOID(rs.getLong(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_LOCATION_ID]));
            entCalcCogsLocation.setCalcCogsMainId(rs.getLong(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID]));
            entCalcCogsLocation.setLocationId(rs.getLong(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_ID]));
            entCalcCogsLocation.setLocationCalcCogsType(rs.getInt(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE]));
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
            String sql = "SELECT * FROM " + TBL_CALC_COGS_LOCATION;
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
                CalcCogsLocation entCalcCogsLocation = new CalcCogsLocation();
                resultToObject(rs, entCalcCogsLocation);
                lists.add(entCalcCogsLocation);
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

    public static boolean checkOID(long entCalcCogsLocationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CALC_COGS_LOCATION + " WHERE "
                    + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_LOCATION_ID] + " = " + entCalcCogsLocationId;
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
            String sql = "SELECT COUNT(" + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_LOCATION_ID] + ") FROM " + TBL_CALC_COGS_LOCATION;
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
                    CalcCogsLocation entCalcCogsLocation = (CalcCogsLocation) list.get(ls);
                    if (oid == entCalcCogsLocation.getOID()) {
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
    
            // ------------------ end added by eri  -----------------------------------
	public static JSONObject fetchJSON(long oid){
		JSONObject object = new JSONObject();
		try {
                    CalcCogsLocation calcCogsLocation = PstCalcCogsLocation.fetchExc(oid);
                    object.put(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_LOCATION_ID], calcCogsLocation.getOID());
                    object.put(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID], calcCogsLocation.getCalcCogsMainId());
                    object.put(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE], calcCogsLocation.getLocationCalcCogsType());
                    object.put(PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_ID], calcCogsLocation.getLocationId());
                }catch(Exception exc){}
            
                return object;
            }
}
