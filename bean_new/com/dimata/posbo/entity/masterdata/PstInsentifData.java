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
public class PstInsentifData extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_INSENTIF_DATA = "insentif_data";
    public static final int FLD_INSENTIF_DATA_ID = 0;
    public static final int FLD_CASH_BILL_MAIN_ID = 1;
    public static final int FLD_CASH_BILL_DETAIL_ID = 2;
    public static final int FLD_EMPLOYEE_ID = 3;
    public static final int FLD_POSITION_ID = 4;
    public static final int FLD_INSENTIF_MASTER_ID = 5;
    public static final int FLD_INSENTIF_POINT = 6;
    public static final int FLD_INSENTIF_VALUE = 7;

    public static String[] fieldNames = {
        "INSENTIF_DATA_ID",
        "CASH_BILL_MAIN_ID",
        "CASH_BILL_DETAIL_ID",
        "EMPLOYEE_ID",
        "POSITION_ID",
        "INSENTIF_MASTER_ID",
        "INSENTIF_POINT",
        "INSENTIF_VALUE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstInsentifData() {
    }

    public PstInsentifData(int i) throws DBException {
        super(new PstInsentifData());
    }

    public PstInsentifData(String sOid) throws DBException {
        super(new PstInsentifData(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstInsentifData(long lOid) throws DBException {
        super(new PstInsentifData(0));
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
        return TBL_INSENTIF_DATA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstInsentifData().getClass().getName();
    }

    public static InsentifData fetchExc(long oid) throws DBException {
        try {
            InsentifData entInsentifData = new InsentifData();
            PstInsentifData pstInsentifData = new PstInsentifData(oid);
            entInsentifData.setOID(oid);
            entInsentifData.setCashBillMainId(pstInsentifData.getlong(FLD_CASH_BILL_MAIN_ID));
            entInsentifData.setCashBillDetailId(pstInsentifData.getlong(FLD_CASH_BILL_DETAIL_ID));
            entInsentifData.setEmployeeId(pstInsentifData.getlong(FLD_EMPLOYEE_ID));
            entInsentifData.setPositionId(pstInsentifData.getlong(FLD_POSITION_ID));
            entInsentifData.setInsentifMasterId(pstInsentifData.getlong(FLD_INSENTIF_MASTER_ID));
            entInsentifData.setInsentifPoint(pstInsentifData.getdouble(FLD_INSENTIF_POINT));
            entInsentifData.setInsentifValue(pstInsentifData.getdouble(FLD_INSENTIF_VALUE));
            return entInsentifData;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifData(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        InsentifData entInsentifData = fetchExc(entity.getOID());
        entity = (Entity) entInsentifData;
        return entInsentifData.getOID();
    }

    public static synchronized long updateExc(InsentifData entInsentifData) throws DBException {
        try {
            if (entInsentifData.getOID() != 0) {
                PstInsentifData pstInsentifData = new PstInsentifData(entInsentifData.getOID());
                pstInsentifData.setLong(FLD_CASH_BILL_MAIN_ID, entInsentifData.getCashBillMainId());
                pstInsentifData.setLong(FLD_CASH_BILL_DETAIL_ID, entInsentifData.getCashBillDetailId());
                pstInsentifData.setLong(FLD_EMPLOYEE_ID, entInsentifData.getEmployeeId());
                pstInsentifData.setLong(FLD_POSITION_ID, entInsentifData.getPositionId());
                pstInsentifData.setLong(FLD_INSENTIF_MASTER_ID, entInsentifData.getInsentifMasterId());
                pstInsentifData.setDouble(FLD_INSENTIF_POINT, entInsentifData.getInsentifPoint());
                pstInsentifData.setDouble(FLD_INSENTIF_VALUE, entInsentifData.getInsentifValue());
                pstInsentifData.update();
                return entInsentifData.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifData(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((InsentifData) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstInsentifData pstInsentifData = new PstInsentifData(oid);
            pstInsentifData.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifData(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(InsentifData entInsentifData) throws DBException {
        try {
            PstInsentifData pstInsentifData = new PstInsentifData(0);
            pstInsentifData.setLong(FLD_CASH_BILL_MAIN_ID, entInsentifData.getCashBillMainId());
            pstInsentifData.setLong(FLD_CASH_BILL_DETAIL_ID, entInsentifData.getCashBillDetailId());
            pstInsentifData.setLong(FLD_EMPLOYEE_ID, entInsentifData.getEmployeeId());
            pstInsentifData.setLong(FLD_POSITION_ID, entInsentifData.getPositionId());
            pstInsentifData.setLong(FLD_INSENTIF_MASTER_ID, entInsentifData.getInsentifMasterId());
            pstInsentifData.setDouble(FLD_INSENTIF_POINT, entInsentifData.getInsentifPoint());
            pstInsentifData.setDouble(FLD_INSENTIF_VALUE, entInsentifData.getInsentifValue());
            pstInsentifData.insert();
            entInsentifData.setOID(pstInsentifData.getlong(FLD_INSENTIF_DATA_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifData(0), DBException.UNKNOWN);
        }
        return entInsentifData.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((InsentifData) entity);
    }

    public static void resultToObject(ResultSet rs, InsentifData entInsentifData) {
        try {
            entInsentifData.setOID(rs.getLong(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_DATA_ID]));
            entInsentifData.setCashBillMainId(rs.getLong(PstInsentifData.fieldNames[PstInsentifData.FLD_CASH_BILL_MAIN_ID]));
            entInsentifData.setCashBillDetailId(rs.getLong(PstInsentifData.fieldNames[PstInsentifData.FLD_CASH_BILL_DETAIL_ID]));
            entInsentifData.setEmployeeId(rs.getLong(PstInsentifData.fieldNames[PstInsentifData.FLD_EMPLOYEE_ID]));
            entInsentifData.setPositionId(rs.getLong(PstInsentifData.fieldNames[PstInsentifData.FLD_POSITION_ID]));
            entInsentifData.setInsentifMasterId(rs.getLong(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_MASTER_ID]));
            entInsentifData.setInsentifPoint(rs.getDouble(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_POINT]));
            entInsentifData.setInsentifValue(rs.getDouble(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_VALUE]));
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
            String sql = "SELECT * FROM " + TBL_INSENTIF_DATA;
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
                InsentifData entInsentifData = new InsentifData();
                resultToObject(rs, entInsentifData);
                lists.add(entInsentifData);
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

    public static boolean checkOID(long entInsentifDataId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_INSENTIF_DATA + " WHERE "
                    + PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_DATA_ID] + " = " + entInsentifDataId;
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
            String sql = "SELECT COUNT(" + PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_DATA_ID] + ") FROM " + TBL_INSENTIF_DATA;
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
                    InsentifData entInsentifData = (InsentifData) list.get(ls);
                    if (oid == entInsentifData.getOID()) {
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
  
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                InsentifData insentifData = PstInsentifData.fetchExc(oid);
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_DATA_ID], insentifData.getOID());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_CASH_BILL_DETAIL_ID], insentifData.getCashBillDetailId());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_CASH_BILL_MAIN_ID], insentifData.getCashBillMainId());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_EMPLOYEE_ID], insentifData.getEmployeeId());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_MASTER_ID], insentifData.getInsentifMasterId());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_POINT], insentifData.getInsentifPoint());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_VALUE], insentifData.getInsentifValue());
                object.put(PstInsentifData.fieldNames[PstInsentifData.FLD_POSITION_ID], insentifData.getPositionId());
            }catch(Exception exc){}

            return object;
        }
}
