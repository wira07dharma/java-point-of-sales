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
public class PstAccessories extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ACCESSORIES = "pos_accessories";
    public static final int FLD_ACCESSORIES_ID = 0;
    public static final int FLD_ACCESSORIES_NAME = 1;
    public static final int FLD_ACCESSORIES_CODE = 2;
    public static final int FLD_ACCESSORIES_STATUS = 3;

    public static String[] fieldNames = {
        "ACCESSORIES_ID",
        "ACCESSORIES_NAME",
        "ACCESSORIES_CODE",
        "ACCESSORIES_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT
    };
    
    
    
    public static final int STATUS_AKTIF = 1;
    public static final int STATUS_NON_AKTIF = 0;
    
    public static final String[] status = {"Non Aktif","Aktif"};

    public PstAccessories() {
    }

    public PstAccessories(int i) throws DBException {
        super(new PstAccessories());
    }

    public PstAccessories(String sOid) throws DBException {
        super(new PstAccessories(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAccessories(long lOid) throws DBException {
        super(new PstAccessories(0));
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
        return TBL_ACCESSORIES;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAccessories().getClass().getName();
    }

    public static Accessories fetchExc(long oid) throws DBException {
        try {
            Accessories entAccessories = new Accessories();
            PstAccessories pstAccessories = new PstAccessories(oid);
            entAccessories.setOID(oid);
            entAccessories.setAccessories_name(pstAccessories.getString(FLD_ACCESSORIES_NAME));
            entAccessories.setAccessories_code(pstAccessories.getString(FLD_ACCESSORIES_CODE));
            entAccessories.setAccessories_status(pstAccessories.getInt(FLD_ACCESSORIES_STATUS));
            return entAccessories;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccessories(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Accessories entAccessories = fetchExc(entity.getOID());
        entity = (Entity) entAccessories;
        return entAccessories.getOID();
    }

    public static synchronized long updateExc(Accessories entAccessories) throws DBException {
        try {
            if (entAccessories.getOID() != 0) {
                PstAccessories pstAccessories = new PstAccessories(entAccessories.getOID());
                pstAccessories.setString(FLD_ACCESSORIES_NAME, entAccessories.getAccessories_name());
                pstAccessories.setString(FLD_ACCESSORIES_CODE, entAccessories.getAccessories_code());
                pstAccessories.setInt(FLD_ACCESSORIES_STATUS, entAccessories.getAccessories_status());
                pstAccessories.update();
                return entAccessories.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccessories(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Accessories) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAccessories pstAccessories = new PstAccessories(oid);
            pstAccessories.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccessories(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Accessories entAccessories) throws DBException {
        try {
            PstAccessories pstAccessories = new PstAccessories(0);
            pstAccessories.setString(FLD_ACCESSORIES_NAME, entAccessories.getAccessories_name());
            pstAccessories.setString(FLD_ACCESSORIES_CODE, entAccessories.getAccessories_code());
            pstAccessories.setInt(FLD_ACCESSORIES_STATUS, entAccessories.getAccessories_status());
            pstAccessories.insert();
            entAccessories.setOID(pstAccessories.getlong(FLD_ACCESSORIES_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccessories(0), DBException.UNKNOWN);
        }
        return entAccessories.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Accessories) entity);
    }

    public static void resultToObject(ResultSet rs, Accessories entAccessories) {
        try {
            entAccessories.setOID(rs.getLong(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_ID]));
            entAccessories.setAccessories_name(rs.getString(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_NAME]));
            entAccessories.setAccessories_code(rs.getString(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_CODE]));
            entAccessories.setAccessories_status(rs.getInt(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_STATUS]));
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
            String sql = "SELECT * FROM " + TBL_ACCESSORIES;
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
                Accessories entAccessories = new Accessories();
                resultToObject(rs, entAccessories);
                lists.add(entAccessories);
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

    public static boolean checkOID(long entAccessoriesId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ACCESSORIES + " WHERE "
                    + PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_ID] + " = " + entAccessoriesId;
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
            String sql = "SELECT COUNT(" + PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_ID] + ") FROM " + TBL_ACCESSORIES;
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
                    Accessories entAccessories = (Accessories) list.get(ls);
                    if (oid == entAccessories.getOID()) {
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
    
    
        // ------------------ end added by eri -----------------------------------
	public static JSONObject fetchJSON(long oid){
		JSONObject object = new JSONObject();
		try {
                    Accessories accessories = PstAccessories.fetchExc(oid);
                    object.put(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_ID], accessories.getOID());
                    object.put(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_NAME], accessories.getAccessories_name());
                    object.put(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_CODE], accessories.getAccessories_code());
                    object.put(PstAccessories.fieldNames[PstAccessories.FLD_ACCESSORIES_STATUS], accessories.getAccessories_status());
               
                
                }catch(Exception exc){}
            
                return object;
            }
}
