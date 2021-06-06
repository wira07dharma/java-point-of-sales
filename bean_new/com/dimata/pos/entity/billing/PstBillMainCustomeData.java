/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.entity.billing;

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
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstBillMainCustomeData extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_BILLMAINCUSTOMEDATA = "cash_bill_main_custome";
    public static final int FLD_BILL_MAIN_CUSTOME_DATA_ID = 0;
    public static final int FLD_CASH_BILL_MAIN_ID = 1;
    public static final int FLD_TYPE = 2;
    public static final int FLD_NAME = 3;
    public static final int FLD_VALUE = 4;
    public static final int FLD_LATITUDE = 5;
    public static final int FLD_LONGITUDE = 6;

    public static String[] fieldNames = {
        "CASH_BILL_MAIN_CUSTOME_ID",
        "CASH_BILL_MAIN_ID",
        "TYPE",
        "NAME",
        "VALUE",
        "LATITUDE",
        "LONGITUDE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstBillMainCustomeData() {
    }

    public PstBillMainCustomeData(int i) throws DBException {
        super(new PstBillMainCustomeData());
    }

    public PstBillMainCustomeData(String sOid) throws DBException {
        super(new PstBillMainCustomeData(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstBillMainCustomeData(long lOid) throws DBException {
        super(new PstBillMainCustomeData(0));
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
        return TBL_BILLMAINCUSTOMEDATA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstBillMainCustomeData().getClass().getName();
    }

    public static BillMainCustomeData fetchExc(long oid) throws DBException {
        try {
            BillMainCustomeData entBillMainCustomeData = new BillMainCustomeData();
            PstBillMainCustomeData pstBillMainCustomeData = new PstBillMainCustomeData(oid);
            entBillMainCustomeData.setOID(oid);
            entBillMainCustomeData.setCashBillMainId(pstBillMainCustomeData.getLong(FLD_CASH_BILL_MAIN_ID));
            entBillMainCustomeData.setType(pstBillMainCustomeData.getInt(FLD_TYPE));
            entBillMainCustomeData.setName(pstBillMainCustomeData.getString(FLD_NAME));
            entBillMainCustomeData.setValue(pstBillMainCustomeData.getString(FLD_VALUE));
            entBillMainCustomeData.setLatitude(pstBillMainCustomeData.getdouble(FLD_LATITUDE));
            entBillMainCustomeData.setLongitude(pstBillMainCustomeData.getdouble(FLD_LONGITUDE));
            return entBillMainCustomeData;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillMainCustomeData(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        BillMainCustomeData entBillMainCustomeData = fetchExc(entity.getOID());
        entity = (Entity) entBillMainCustomeData;
        return entBillMainCustomeData.getOID();
    }

    public static synchronized long updateExc(BillMainCustomeData entBillMainCustomeData) throws DBException {
        try {
            if (entBillMainCustomeData.getOID() != 0) {
                PstBillMainCustomeData pstBillMainCustomeData = new PstBillMainCustomeData(entBillMainCustomeData.getOID());
                pstBillMainCustomeData.setLong(FLD_CASH_BILL_MAIN_ID, entBillMainCustomeData.getCashBillMainId());
                pstBillMainCustomeData.setInt(FLD_TYPE, entBillMainCustomeData.getType());
                pstBillMainCustomeData.setString(FLD_NAME, entBillMainCustomeData.getName());
                pstBillMainCustomeData.setString(FLD_VALUE, entBillMainCustomeData.getValue());
                pstBillMainCustomeData.setDouble(FLD_LATITUDE, entBillMainCustomeData.getLatitude());
                pstBillMainCustomeData.setDouble(FLD_LONGITUDE, entBillMainCustomeData.getLongitude());
                pstBillMainCustomeData.update();
                return entBillMainCustomeData.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillMainCustomeData(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((BillMainCustomeData) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstBillMainCustomeData pstBillMainCustomeData = new PstBillMainCustomeData(oid);
            pstBillMainCustomeData.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillMainCustomeData(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(BillMainCustomeData entBillMainCustomeData) throws DBException {
        try {
            PstBillMainCustomeData pstBillMainCustomeData = new PstBillMainCustomeData(0);
            pstBillMainCustomeData.setLong(FLD_CASH_BILL_MAIN_ID, entBillMainCustomeData.getCashBillMainId());
            pstBillMainCustomeData.setInt(FLD_TYPE, entBillMainCustomeData.getType());
            pstBillMainCustomeData.setString(FLD_NAME, entBillMainCustomeData.getName());
            pstBillMainCustomeData.setString(FLD_VALUE, entBillMainCustomeData.getValue());
            pstBillMainCustomeData.setDouble(FLD_LATITUDE, entBillMainCustomeData.getLatitude());
            pstBillMainCustomeData.setDouble(FLD_LONGITUDE, entBillMainCustomeData.getLongitude());
            pstBillMainCustomeData.insert();
            entBillMainCustomeData.setOID(pstBillMainCustomeData.getlong(FLD_BILL_MAIN_CUSTOME_DATA_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillMainCustomeData(0), DBException.UNKNOWN);
        }
        return entBillMainCustomeData.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((BillMainCustomeData) entity);
    }

    public static void resultToObject(ResultSet rs, BillMainCustomeData entBillMainCustomeData) {
        try {
            entBillMainCustomeData.setOID(rs.getLong(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_BILL_MAIN_CUSTOME_DATA_ID]));
            entBillMainCustomeData.setCashBillMainId(rs.getLong(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_CASH_BILL_MAIN_ID]));
            entBillMainCustomeData.setType(rs.getInt(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_TYPE]));
            entBillMainCustomeData.setName(rs.getString(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_NAME]));
            entBillMainCustomeData.setValue(rs.getString(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_VALUE]));
            entBillMainCustomeData.setLatitude(rs.getDouble(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_LATITUDE]));
            entBillMainCustomeData.setLongitude(rs.getDouble(PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_LONGITUDE]));
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
            String sql = "SELECT * FROM " + TBL_BILLMAINCUSTOMEDATA;
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
                BillMainCustomeData entBillMainCustomeData = new BillMainCustomeData();
                resultToObject(rs, entBillMainCustomeData);
                lists.add(entBillMainCustomeData);
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

    public static boolean checkOID(long entBillMainCustomeDataId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_BILLMAINCUSTOMEDATA + " WHERE "
                    + PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_BILL_MAIN_CUSTOME_DATA_ID] + " = " + entBillMainCustomeDataId;
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
            String sql = "SELECT COUNT(" + PstBillMainCustomeData.fieldNames[PstBillMainCustomeData.FLD_BILL_MAIN_CUSTOME_DATA_ID] + ") FROM " + TBL_BILLMAINCUSTOMEDATA;
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
                    BillMainCustomeData entBillMainCustomeData = (BillMainCustomeData) list.get(ls);
                    if (oid == entBillMainCustomeData.getOID()) {
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
