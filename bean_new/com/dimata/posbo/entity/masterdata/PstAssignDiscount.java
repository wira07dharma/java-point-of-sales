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
public class PstAssignDiscount extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ASSIGN_DISCOUNT = "pos_assign_discount";
    public static final int FLD_ASSIGN_DISC_ID = 0;
    public static final int FLD_EMPLOYEE_ID = 1;
    public static final int FLD_MAX_DISC = 2;

    public static String[] fieldNames = {
        "ASSIGN_DISC_ID",
        "EMPLOYEE_ID",
        "MAX_DISC"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public PstAssignDiscount() {
    }

    public PstAssignDiscount(int i) throws DBException {
        super(new PstAssignDiscount());
    }

    public PstAssignDiscount(String sOid) throws DBException {
        super(new PstAssignDiscount(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAssignDiscount(long lOid) throws DBException {
        super(new PstAssignDiscount(0));
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
        return TBL_ASSIGN_DISCOUNT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAssignDiscount().getClass().getName();
    }

    public static AssignDiscount fetchExc(long oid) throws DBException {
        try {
            AssignDiscount entAssignDiscount = new AssignDiscount();
            PstAssignDiscount pstAssignDiscount = new PstAssignDiscount(oid);
            entAssignDiscount.setOID(oid);
            entAssignDiscount.setEmployeeId(pstAssignDiscount.getlong(FLD_EMPLOYEE_ID));
            entAssignDiscount.setMaxDisc(pstAssignDiscount.getdouble(FLD_MAX_DISC));
            return entAssignDiscount;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignDiscount(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        AssignDiscount entAssignDiscount = fetchExc(entity.getOID());
        entity = (Entity) entAssignDiscount;
        return entAssignDiscount.getOID();
    }

    public static synchronized long updateExc(AssignDiscount entAssignDiscount) throws DBException {
        try {
            if (entAssignDiscount.getOID() != 0) {
                PstAssignDiscount pstAssignDiscount = new PstAssignDiscount(entAssignDiscount.getOID());
                pstAssignDiscount.setLong(FLD_EMPLOYEE_ID, entAssignDiscount.getEmployeeId());
                pstAssignDiscount.setDouble(FLD_MAX_DISC, entAssignDiscount.getMaxDisc());
                pstAssignDiscount.update();
                return entAssignDiscount.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignDiscount(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((AssignDiscount) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAssignDiscount pstAssignDiscount = new PstAssignDiscount(oid);
            pstAssignDiscount.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignDiscount(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(AssignDiscount entAssignDiscount) throws DBException {
        try {
            PstAssignDiscount pstAssignDiscount = new PstAssignDiscount(0);
            pstAssignDiscount.setLong(FLD_EMPLOYEE_ID, entAssignDiscount.getEmployeeId());
            pstAssignDiscount.setDouble(FLD_MAX_DISC, entAssignDiscount.getMaxDisc());
            pstAssignDiscount.insert();
            entAssignDiscount.setOID(pstAssignDiscount.getlong(FLD_ASSIGN_DISC_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignDiscount(0), DBException.UNKNOWN);
        }
        return entAssignDiscount.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((AssignDiscount) entity);
    }

    public static void resultToObject(ResultSet rs, AssignDiscount entAssignDiscount) {
        try {
            entAssignDiscount.setOID(rs.getLong(PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_ASSIGN_DISC_ID]));
            entAssignDiscount.setEmployeeId(rs.getLong(PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_EMPLOYEE_ID]));
            entAssignDiscount.setMaxDisc(rs.getDouble(PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_MAX_DISC]));
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
            String sql = "SELECT * FROM " + TBL_ASSIGN_DISCOUNT;
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
                AssignDiscount entAssignDiscount = new AssignDiscount();
                resultToObject(rs, entAssignDiscount);
                lists.add(entAssignDiscount);
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

    public static boolean checkOID(long entAssignDiscountId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ASSIGN_DISCOUNT + " WHERE "
                    + PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_ASSIGN_DISC_ID] + " = " + entAssignDiscountId;
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
            String sql = "SELECT COUNT(" + PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_ASSIGN_DISC_ID] + ") FROM " + TBL_ASSIGN_DISCOUNT;
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
                    AssignDiscount entAssignDiscount = (AssignDiscount) list.get(ls);
                    if (oid == entAssignDiscount.getOID()) {
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
                    AssignDiscount assignDiscount = PstAssignDiscount.fetchExc(oid);
                    object.put(PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_ASSIGN_DISC_ID],assignDiscount.getOID());
                    object.put(PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_EMPLOYEE_ID],assignDiscount.getEmployeeId());
                    object.put(PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_MAX_DISC],assignDiscount.getMaxDisc());
                }catch(Exception exc){}
            
                return object;
            }
        
}
