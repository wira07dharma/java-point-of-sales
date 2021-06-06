/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author IanRizky
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstChainMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CHAIN_MAIN = "pos_chain_main";
    public static final int FLD_CHAIN_MAIN_ID = 0;
    public static final int FLD_CHAIN_LOCATION = 1;
    public static final int FLD_CHAIN_DATE = 2;
    public static final int FLD_CHAIN_NOTE = 3;
    public static final int FLD_CHAIN_STATUS = 4;
    public static final int FLD_CHAIN_TITLE = 5;

    public static String[] fieldNames = {
        "CHAIN_MAIN_ID",
        "CHAIN_LOCATION",
        "CHAIN_DATE",
        "CHAIN_NOTE",
        "CHAIN_STATUS",
        "CHAIN_TITLE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING
    };

    public PstChainMain() {
    }

    public PstChainMain(int i) throws DBException {
        super(new PstChainMain());
    }

    public PstChainMain(String sOid) throws DBException {
        super(new PstChainMain(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstChainMain(long lOid) throws DBException {
        super(new PstChainMain(0));
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
        return TBL_CHAIN_MAIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstChainMain().getClass().getName();
    }

    public static ChainMain fetchExc(long oid) throws DBException {
        try {
            ChainMain entChainMain = new ChainMain();
            PstChainMain pstChainMain = new PstChainMain(oid);
            entChainMain.setOID(oid);
            entChainMain.setChainLocation(pstChainMain.getlong(FLD_CHAIN_LOCATION));
            entChainMain.setChainDate(pstChainMain.getDate(FLD_CHAIN_DATE));
            entChainMain.setChainNote(pstChainMain.getString(FLD_CHAIN_NOTE));
            entChainMain.setChainStatus(pstChainMain.getInt(FLD_CHAIN_STATUS));
            entChainMain.setChainTitle(pstChainMain.getString(FLD_CHAIN_TITLE));
            return entChainMain;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMain(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ChainMain entChainMain = fetchExc(entity.getOID());
        entity = (Entity) entChainMain;
        return entChainMain.getOID();
    }

    public static synchronized long updateExc(ChainMain entChainMain) throws DBException {
        try {
            if (entChainMain.getOID() != 0) {
                PstChainMain pstChainMain = new PstChainMain(entChainMain.getOID());
                pstChainMain.setLong(FLD_CHAIN_LOCATION, entChainMain.getChainLocation());
                pstChainMain.setDate(FLD_CHAIN_DATE, entChainMain.getChainDate());
                pstChainMain.setString(FLD_CHAIN_NOTE, entChainMain.getChainNote());
                pstChainMain.setInt(FLD_CHAIN_STATUS, entChainMain.getChainStatus());
                pstChainMain.setString(FLD_CHAIN_TITLE, entChainMain.getChainTitle());
                pstChainMain.update();
                return entChainMain.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMain(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ChainMain) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstChainMain pstChainMain = new PstChainMain(oid);
            pstChainMain.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMain(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ChainMain entChainMain) throws DBException {
        try {
            PstChainMain pstChainMain = new PstChainMain(0);
            pstChainMain.setLong(FLD_CHAIN_LOCATION, entChainMain.getChainLocation());
            pstChainMain.setDate(FLD_CHAIN_DATE, entChainMain.getChainDate());
            pstChainMain.setString(FLD_CHAIN_NOTE, entChainMain.getChainNote());
            pstChainMain.setInt(FLD_CHAIN_STATUS, entChainMain.getChainStatus());
            pstChainMain.setString(FLD_CHAIN_TITLE, entChainMain.getChainTitle());
            pstChainMain.insert();
            entChainMain.setOID(pstChainMain.getlong(FLD_CHAIN_MAIN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMain(0), DBException.UNKNOWN);
        }
        return entChainMain.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ChainMain) entity);
    }

    public static void resultToObject(ResultSet rs, ChainMain entChainMain) {
        try {
            entChainMain.setOID(rs.getLong(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_MAIN_ID]));
            entChainMain.setChainLocation(rs.getLong(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_LOCATION]));
            entChainMain.setChainDate(rs.getDate(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_DATE]));
            entChainMain.setChainNote(rs.getString(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_NOTE]));
            entChainMain.setChainStatus(rs.getInt(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_STATUS]));
            entChainMain.setChainTitle(rs.getString(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_TITLE]));
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
            String sql = "SELECT * FROM " + TBL_CHAIN_MAIN;
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
                ChainMain entChainMain = new ChainMain();
                resultToObject(rs, entChainMain);
                lists.add(entChainMain);
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

    public static boolean checkOID(long entChainMainId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CHAIN_MAIN + " WHERE "
                    + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_MAIN_ID] + " = " + entChainMainId;
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
            String sql = "SELECT COUNT(" + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_MAIN_ID] + ") FROM " + TBL_CHAIN_MAIN;
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
                    ChainMain entChainMain = (ChainMain) list.get(ls);
                    if (oid == entChainMain.getOID()) {
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
    
        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                ChainMain chainMain = PstChainMain.fetchExc(oid);
                object.put(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_MAIN_ID], chainMain.getOID());
                object.put(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_LOCATION], chainMain.getChainLocation());
                object.put(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_DATE], chainMain.getChainDate());
                object.put(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_NOTE], chainMain.getChainNote());
                object.put(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_STATUS], chainMain.getChainStatus());
                object.put(PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_TITLE], chainMain.getChainTitle());
            }catch(Exception exc){}

            return object;
        } 
    
}
