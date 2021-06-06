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
public class PstCalcCogsMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CALC_COGS_MAIN = "pos_calc_cogs_main";
    public static final int FLD_CALC_COGS_MAIN_ID = 0;
    public static final int FLD_COST_DATE_START = 1;
    public static final int FLD_COST_DATE_END = 2;
    public static final int FLD_SALES_DATE_START = 3;
    public static final int FLD_SALES_DATE_END = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_NOTE = 6;

    public static String[] fieldNames = {
        "CALC_COGS_MAIN_ID",
        "COST_DATE_START",
        "COST_DATE_END",
        "SALES_DATE_START",
        "SALES_DATE_END",
        "STATUS",
        "NOTE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING
    };

    public PstCalcCogsMain() {
    }

    public PstCalcCogsMain(int i) throws DBException {
        super(new PstCalcCogsMain());
    }

    public PstCalcCogsMain(String sOid) throws DBException {
        super(new PstCalcCogsMain(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCalcCogsMain(long lOid) throws DBException {
        super(new PstCalcCogsMain(0));
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
        return TBL_CALC_COGS_MAIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCalcCogsMain().getClass().getName();
    }

    public static CalcCogsMain fetchExc(long oid) throws DBException {
        try {
            CalcCogsMain entCalcCogsMain = new CalcCogsMain();
            PstCalcCogsMain pstCalcCogsMain = new PstCalcCogsMain(oid);
            entCalcCogsMain.setOID(oid);
            entCalcCogsMain.setCostDateStart(pstCalcCogsMain.getDate(FLD_COST_DATE_START));
            entCalcCogsMain.setCostDateEnd(pstCalcCogsMain.getDate(FLD_COST_DATE_END));
            entCalcCogsMain.setSalesDateStart(pstCalcCogsMain.getDate(FLD_SALES_DATE_START));
            entCalcCogsMain.setSalesDateEnd(pstCalcCogsMain.getDate(FLD_SALES_DATE_END));
            entCalcCogsMain.setStatus(pstCalcCogsMain.getInt(FLD_STATUS));
            entCalcCogsMain.setNote(pstCalcCogsMain.getString(FLD_NOTE));
            return entCalcCogsMain;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsMain(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        CalcCogsMain entCalcCogsMain = fetchExc(entity.getOID());
        entity = (Entity) entCalcCogsMain;
        return entCalcCogsMain.getOID();
    }

    public static synchronized long updateExc(CalcCogsMain entCalcCogsMain) throws DBException {
        try {
            if (entCalcCogsMain.getOID() != 0) {
                PstCalcCogsMain pstCalcCogsMain = new PstCalcCogsMain(entCalcCogsMain.getOID());
                pstCalcCogsMain.setDate(FLD_COST_DATE_START, entCalcCogsMain.getCostDateStart());
                pstCalcCogsMain.setDate(FLD_COST_DATE_END, entCalcCogsMain.getCostDateEnd());
                pstCalcCogsMain.setDate(FLD_SALES_DATE_START, entCalcCogsMain.getSalesDateStart());
                pstCalcCogsMain.setDate(FLD_SALES_DATE_END, entCalcCogsMain.getSalesDateEnd());
                pstCalcCogsMain.setInt(FLD_STATUS, entCalcCogsMain.getStatus());
                pstCalcCogsMain.setString(FLD_NOTE, entCalcCogsMain.getNote());
                pstCalcCogsMain.update();
                return entCalcCogsMain.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsMain(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((CalcCogsMain) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstCalcCogsMain pstCalcCogsMain = new PstCalcCogsMain(oid);
            pstCalcCogsMain.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsMain(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(CalcCogsMain entCalcCogsMain) throws DBException {
        try {
            PstCalcCogsMain pstCalcCogsMain = new PstCalcCogsMain(0);
            pstCalcCogsMain.setDate(FLD_COST_DATE_START, entCalcCogsMain.getCostDateStart());
            pstCalcCogsMain.setDate(FLD_COST_DATE_END, entCalcCogsMain.getCostDateEnd());
            pstCalcCogsMain.setDate(FLD_SALES_DATE_START, entCalcCogsMain.getSalesDateStart());
            pstCalcCogsMain.setDate(FLD_SALES_DATE_END, entCalcCogsMain.getSalesDateEnd());
            pstCalcCogsMain.setInt(FLD_STATUS, entCalcCogsMain.getStatus());
            pstCalcCogsMain.setString(FLD_NOTE, entCalcCogsMain.getNote());
            pstCalcCogsMain.insert();
            entCalcCogsMain.setOID(pstCalcCogsMain.getlong(FLD_CALC_COGS_MAIN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsMain(0), DBException.UNKNOWN);
        }
        return entCalcCogsMain.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((CalcCogsMain) entity);
    }

    public static void resultToObject(ResultSet rs, CalcCogsMain entCalcCogsMain) {
        try {
            entCalcCogsMain.setOID(rs.getLong(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_CALC_COGS_MAIN_ID]));
            entCalcCogsMain.setCostDateStart(rs.getTimestamp(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_COST_DATE_START]));
            entCalcCogsMain.setCostDateEnd(rs.getTimestamp(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_COST_DATE_END]));
            entCalcCogsMain.setSalesDateStart(rs.getTimestamp(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_SALES_DATE_START]));
            entCalcCogsMain.setSalesDateEnd(rs.getTimestamp(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_SALES_DATE_END]));
            entCalcCogsMain.setStatus(rs.getInt(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_STATUS]));
            entCalcCogsMain.setNote(rs.getString(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_NOTE]));
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
            String sql = "SELECT * FROM " + TBL_CALC_COGS_MAIN;
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
                CalcCogsMain entCalcCogsMain = new CalcCogsMain();
                resultToObject(rs, entCalcCogsMain);
                lists.add(entCalcCogsMain);
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

    public static boolean checkOID(long entCalcCogsMainId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CALC_COGS_MAIN + " WHERE "
                    + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_CALC_COGS_MAIN_ID] + " = " + entCalcCogsMainId;
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
            String sql = "SELECT COUNT(" + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_CALC_COGS_MAIN_ID] + ") FROM " + TBL_CALC_COGS_MAIN;
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
                    CalcCogsMain entCalcCogsMain = (CalcCogsMain) list.get(ls);
                    if (oid == entCalcCogsMain.getOID()) {
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

    public static Vector listJoinLocation(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CALC_COGS_MAIN + " AS ccm"
                    + " INNER JOIN " + PstCalcCogsLocation.TBL_CALC_COGS_LOCATION + " AS ccl"
                    + " ON ccl." + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID]
                    + " = ccm." + fieldNames[FLD_CALC_COGS_MAIN_ID]
                    + "";
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
                CalcCogsMain entCalcCogsMain = new CalcCogsMain();
                resultToObject(rs, entCalcCogsMain);
                lists.add(entCalcCogsMain);
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

        // ------------------ Start added by eri  -----------------------------------
	public static JSONObject fetchJSON(long oid){
		JSONObject object = new JSONObject();
		try {
                    CalcCogsMain calcCogsMain = PstCalcCogsMain.fetchExc(oid);
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_CALC_COGS_MAIN_ID], calcCogsMain.getOID());
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_COST_DATE_END], calcCogsMain.getCostDateEnd());
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_COST_DATE_START], calcCogsMain.getCostDateEnd());
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_NOTE], calcCogsMain.getNote());
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_SALES_DATE_END], calcCogsMain.getSalesDateEnd());
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_SALES_DATE_START], calcCogsMain.getSalesDateStart());
                    object.put(PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_STATUS], calcCogsMain.getStatus());
                
                }catch(Exception exc){}
            
                return object;
            }    
}
