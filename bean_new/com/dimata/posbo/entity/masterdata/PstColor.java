/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.db.*;
import static com.dimata.qdep.db.I_DBType.TYPE_ID;
import static com.dimata.qdep.db.I_DBType.TYPE_INT;
import static com.dimata.qdep.db.I_DBType.TYPE_LONG;
import static com.dimata.qdep.db.I_DBType.TYPE_PK;
import static com.dimata.qdep.db.I_DBType.TYPE_STRING;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dewa
 */
public class PstColor extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_COLOR = "pos_color";
    public static final int FLD_COLOR_ID = 0;
    public static final int FLD_COLOR_CODE = 1;
    public static final int FLD_COLOR_NAME = 2;

    public static String[] fieldNames = {
        "COLOR_ID",
        "COLOR_CODE",
        "COLOR_NAME",

    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_ID + TYPE_PK, 
        TYPE_STRING ,
        TYPE_STRING,
       
    };

    public PstColor() {
    }

    public PstColor(int i) throws DBException {
        super(new PstColor());
    }

    public PstColor(String sOid) throws DBException {
        super(new PstColor(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstColor(long lOid) throws DBException {
        super(new PstColor(0));
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
        return TBL_POS_COLOR;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstColor().getClass().getName();
    }

    public static Color fetchExc(long oid) throws DBException {
        try {
            Color entColor = new Color();
            PstColor pstColor = new PstColor(oid);
            entColor.setOID(oid);
            entColor.setColorCode(pstColor.getString(FLD_COLOR_CODE));
            entColor.setColorName(pstColor.getString(FLD_COLOR_NAME));
            
            return entColor;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstColor(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Color entColor = fetchExc(entity.getOID());
        entity = (Entity) entColor;
        return entColor.getOID();
    }

    public static synchronized long updateExc(Color entColor) throws DBException {
        try {
            if (entColor.getOID() != 0) {
                PstColor pstColor = new PstColor(entColor.getOID());
                pstColor.setString(FLD_COLOR_CODE, entColor.getColorCode());
                pstColor.setString(FLD_COLOR_NAME, entColor.getColorName());
                pstColor.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstColor.getUpdateSQL());
                return entColor.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstColor(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Color) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstColor pstColor = new PstColor(oid);
            pstColor.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstColor.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstColor(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Color entColor) throws DBException {
        try {
            PstColor pstColor = new PstColor(0);
            pstColor.setString(FLD_COLOR_CODE, entColor.getColorCode());
            pstColor.setString(FLD_COLOR_NAME,entColor.getColorName());
            pstColor.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstColor.getInsertSQL());
            entColor.setOID(pstColor.getlong(FLD_COLOR_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstColor(0), DBException.UNKNOWN);
        }
        return entColor.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Color) entity);
    }

    public static void resultToObject(ResultSet rs, Color entColor) {
        try {
            entColor.setOID(rs.getLong(PstColor.fieldNames[PstColor.FLD_COLOR_ID]));
            entColor.setColorCode(rs.getString(PstColor.fieldNames[PstColor.FLD_COLOR_CODE]));
            entColor.setColorName(rs.getString(PstColor.fieldNames[PstColor.FLD_COLOR_NAME]));
            } catch (Exception e) {
        }
    }
    
    
    public static void resultToObjectTanpaOid(ResultSet rs, Color entColor) {
        try {
            entColor.setColorCode(rs.getString(PstColor.fieldNames[PstColor.FLD_COLOR_CODE]));
            entColor.setColorName(rs.getString(PstColor.fieldNames[PstColor.FLD_COLOR_NAME]));
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
            String sql = "SELECT * FROM " + TBL_POS_COLOR;
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
                Color entColor = new Color();
                resultToObject(rs, entColor);
                lists.add(entColor);
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
    
    
    public static Vector listTanpaOid(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT COLOR_CODE, COLOR_NAME  FROM " + TBL_POS_COLOR;
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
                Color entColor = new Color();
                resultToObjectTanpaOid(rs, entColor);
                lists.add(entColor);
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

    public static boolean checkOID(long entColorId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_COLOR + " WHERE "
                    + PstColor.fieldNames[PstColor.FLD_COLOR_ID] + " = " + entColorId;
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
            String sql = "SELECT COUNT(" + PstColor.fieldNames[PstColor.FLD_COLOR_ID] + ") FROM " + TBL_POS_COLOR;
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
                    Color entColor = (Color) list.get(ls);
                    if (oid == entColor.getOID()) {
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
    
    public static Color fetchByName(String name) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Color color = new Color();
        try {
            String sql = "SELECT * FROM " + TBL_POS_COLOR +
                    " WHERE " + fieldNames[FLD_COLOR_NAME] +
                    " = '" + name + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, color);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return color;
    }
	
	public static Color fetchByCode(String code) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Color color = new Color();
        try {
            String sql = "SELECT * FROM " + TBL_POS_COLOR +
                    " WHERE " + fieldNames[FLD_COLOR_CODE] +
                    " = '" + code + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, color);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return color;
    }
        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Color color = PstColor.fetchExc(oid);
                object.put(PstColor.fieldNames[PstColor.FLD_COLOR_ID], color.getOID());
                object.put(PstColor.fieldNames[PstColor.FLD_COLOR_CODE], color.getColorCode());
                object.put(PstColor.fieldNames[PstColor.FLD_COLOR_NAME], color.getColorName());
            }catch(Exception exc){}

            return object;
        }
}
