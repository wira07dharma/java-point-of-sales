/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.rsvcustpackrecurring;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstResvCustomPackRecurring extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_RESV_CUSTOM_PACK_RECURRING = "rsv_custome_pack_recurring";
    public static final int FLD_RSV_CUSTOME_PACK_RECUR_ID = 0;
    public static final int FLD_REPEAT_TYPE = 1;
    public static final int FLD_REPEAT_PERIODE = 2;
    public static final int FLD_WEEK_DAYS = 3;
    public static final int FLD_MONTHLY_TYPE = 4;
    public static final int FLD_DAY_OF_MONTH = 5;
    public static final int FLD_UNTIL_TYPE = 6;
    public static final int FLD_REPEAT_NUMBER = 7;
    public static final int FLD_REPEAT_UNTIL_DATE = 8;
    public static final int FLD_CUSTOME_PACK_BILLING_ID = 9;
    public static final int FLD_INVOICE_OPTION = 10;

    public static String[] fieldNames = {
        "RSV_CUSTOME_PACK_RECUR_ID",
        "REPEAT_TYPE",
        "REPEAT_PERIODE",
        "WEEK_DAYS",
        "MONTHLY_TYPE",
        "DAY_OF_MONTH",
        "UNTIL_TYPE",
        "REPEAT_NUMBER",
        "REPEAT_UNTIL_DATE",
        "CUSTOME_PACK_BILLING_ID",
        "INVOICE_OPTION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_LONG + TYPE_FK,
        TYPE_INT
    };

    public PstResvCustomPackRecurring() {
    }

    public PstResvCustomPackRecurring(int i) throws DBException {
        super(new PstResvCustomPackRecurring());
    }

    public PstResvCustomPackRecurring(String sOid) throws DBException {
        super(new PstResvCustomPackRecurring(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstResvCustomPackRecurring(long lOid) throws DBException {
        super(new PstResvCustomPackRecurring(0));
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
        return TBL_RESV_CUSTOM_PACK_RECURRING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstResvCustomPackRecurring().getClass().getName();
    }

    public static ResvCustomPackRecurring fetchExc(long oid) throws DBException {
        try {
            ResvCustomPackRecurring entResvCustomPackRecurring = new ResvCustomPackRecurring();
            PstResvCustomPackRecurring pstResvCustomPackRecurring = new PstResvCustomPackRecurring(oid);
            entResvCustomPackRecurring.setOID(oid);
            entResvCustomPackRecurring.setRepeatType(pstResvCustomPackRecurring.getInt(FLD_REPEAT_TYPE));
            entResvCustomPackRecurring.setRepeatPeriode(pstResvCustomPackRecurring.getInt(FLD_REPEAT_PERIODE));
            entResvCustomPackRecurring.setWeekDays(pstResvCustomPackRecurring.getInt(FLD_WEEK_DAYS));
            entResvCustomPackRecurring.setMonthlyType(pstResvCustomPackRecurring.getInt(FLD_MONTHLY_TYPE));
            entResvCustomPackRecurring.setDayOfMonth(pstResvCustomPackRecurring.getInt(FLD_DAY_OF_MONTH));
            entResvCustomPackRecurring.setUntilType(pstResvCustomPackRecurring.getInt(FLD_UNTIL_TYPE));
            entResvCustomPackRecurring.setRepeatNumber(pstResvCustomPackRecurring.getInt(FLD_REPEAT_NUMBER));
            entResvCustomPackRecurring.setRepeatUntilDate(pstResvCustomPackRecurring.getDate(FLD_REPEAT_UNTIL_DATE));
            entResvCustomPackRecurring.setCustomePackBillingId(pstResvCustomPackRecurring.getlong(FLD_CUSTOME_PACK_BILLING_ID));
            entResvCustomPackRecurring.setInvoiceOption(pstResvCustomPackRecurring.getInt(FLD_INVOICE_OPTION));
            return entResvCustomPackRecurring;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackRecurring(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ResvCustomPackRecurring entResvCustomPackRecurring = fetchExc(entity.getOID());
        entity = (Entity) entResvCustomPackRecurring;
        return entResvCustomPackRecurring.getOID();
    }

    public static synchronized long updateExc(ResvCustomPackRecurring entResvCustomPackRecurring) throws DBException {
        try {
            if (entResvCustomPackRecurring.getOID() != 0) {
                PstResvCustomPackRecurring pstResvCustomPackRecurring = new PstResvCustomPackRecurring(entResvCustomPackRecurring.getOID());
                pstResvCustomPackRecurring.setInt(FLD_REPEAT_TYPE, entResvCustomPackRecurring.getRepeatType());
                pstResvCustomPackRecurring.setInt(FLD_REPEAT_PERIODE, entResvCustomPackRecurring.getRepeatPeriode());
                pstResvCustomPackRecurring.setInt(FLD_WEEK_DAYS, entResvCustomPackRecurring.getWeekDays());
                pstResvCustomPackRecurring.setInt(FLD_MONTHLY_TYPE, entResvCustomPackRecurring.getMonthlyType());
                pstResvCustomPackRecurring.setInt(FLD_DAY_OF_MONTH, entResvCustomPackRecurring.getDayOfMonth());
                pstResvCustomPackRecurring.setInt(FLD_UNTIL_TYPE, entResvCustomPackRecurring.getUntilType());
                pstResvCustomPackRecurring.setInt(FLD_REPEAT_NUMBER, entResvCustomPackRecurring.getRepeatNumber());
                pstResvCustomPackRecurring.setDate(FLD_REPEAT_UNTIL_DATE, entResvCustomPackRecurring.getRepeatUntilDate());
                pstResvCustomPackRecurring.setLong(FLD_CUSTOME_PACK_BILLING_ID, entResvCustomPackRecurring.getCustomePackBillingId());
                pstResvCustomPackRecurring.setInt(FLD_INVOICE_OPTION, entResvCustomPackRecurring.getInvoiceOption());
                pstResvCustomPackRecurring.update();
                return entResvCustomPackRecurring.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackRecurring(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ResvCustomPackRecurring) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstResvCustomPackRecurring pstResvCustomPackRecurring = new PstResvCustomPackRecurring(oid);
            pstResvCustomPackRecurring.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackRecurring(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ResvCustomPackRecurring entResvCustomPackRecurring) throws DBException {
        try {
            PstResvCustomPackRecurring pstResvCustomPackRecurring = new PstResvCustomPackRecurring(0);
            pstResvCustomPackRecurring.setInt(FLD_REPEAT_TYPE, entResvCustomPackRecurring.getRepeatType());
            pstResvCustomPackRecurring.setInt(FLD_REPEAT_PERIODE, entResvCustomPackRecurring.getRepeatPeriode());
            pstResvCustomPackRecurring.setInt(FLD_WEEK_DAYS, entResvCustomPackRecurring.getWeekDays());
            pstResvCustomPackRecurring.setInt(FLD_MONTHLY_TYPE, entResvCustomPackRecurring.getMonthlyType());
            pstResvCustomPackRecurring.setInt(FLD_DAY_OF_MONTH, entResvCustomPackRecurring.getDayOfMonth());
            pstResvCustomPackRecurring.setInt(FLD_UNTIL_TYPE, entResvCustomPackRecurring.getUntilType());
            pstResvCustomPackRecurring.setInt(FLD_REPEAT_NUMBER, entResvCustomPackRecurring.getRepeatNumber());
            pstResvCustomPackRecurring.setDate(FLD_REPEAT_UNTIL_DATE, entResvCustomPackRecurring.getRepeatUntilDate());
            pstResvCustomPackRecurring.setLong(FLD_CUSTOME_PACK_BILLING_ID, entResvCustomPackRecurring.getCustomePackBillingId());
            pstResvCustomPackRecurring.setInt(FLD_INVOICE_OPTION, entResvCustomPackRecurring.getInvoiceOption());
            pstResvCustomPackRecurring.insert();
            entResvCustomPackRecurring.setOID(pstResvCustomPackRecurring.getlong(FLD_RSV_CUSTOME_PACK_RECUR_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackRecurring(0), DBException.UNKNOWN);
        }
        return entResvCustomPackRecurring.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ResvCustomPackRecurring) entity);
    }

    public static void resultToObject(ResultSet rs, ResvCustomPackRecurring entResvCustomPackRecurring) {
        try {
            entResvCustomPackRecurring.setOID(rs.getLong(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_RSV_CUSTOME_PACK_RECUR_ID]));
            entResvCustomPackRecurring.setRepeatType(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_REPEAT_TYPE]));
            entResvCustomPackRecurring.setRepeatPeriode(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_REPEAT_PERIODE]));
            entResvCustomPackRecurring.setWeekDays(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_WEEK_DAYS]));
            entResvCustomPackRecurring.setMonthlyType(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_MONTHLY_TYPE]));
            entResvCustomPackRecurring.setDayOfMonth(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_DAY_OF_MONTH]));
            entResvCustomPackRecurring.setUntilType(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_UNTIL_TYPE]));
            entResvCustomPackRecurring.setRepeatNumber(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_REPEAT_NUMBER]));
            entResvCustomPackRecurring.setRepeatUntilDate(rs.getDate(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_REPEAT_UNTIL_DATE]));
            entResvCustomPackRecurring.setCustomePackBillingId(rs.getLong(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_CUSTOME_PACK_BILLING_ID]));
            entResvCustomPackRecurring.setInvoiceOption(rs.getInt(PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_INVOICE_OPTION]));
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
            String sql = "SELECT * FROM " + TBL_RESV_CUSTOM_PACK_RECURRING;
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
                ResvCustomPackRecurring entResvCustomPackRecurring = new ResvCustomPackRecurring();
                resultToObject(rs, entResvCustomPackRecurring);
                lists.add(entResvCustomPackRecurring);
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

    public static boolean checkOID(long entResvCustomPackRecurringId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_RESV_CUSTOM_PACK_RECURRING + " WHERE "
                    + PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_RSV_CUSTOME_PACK_RECUR_ID] + " = " + entResvCustomPackRecurringId;
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
            String sql = "SELECT COUNT(" + PstResvCustomPackRecurring.fieldNames[PstResvCustomPackRecurring.FLD_RSV_CUSTOME_PACK_RECUR_ID] + ") FROM " + TBL_RESV_CUSTOM_PACK_RECURRING;
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
                    ResvCustomPackRecurring entResvCustomPackRecurring = (ResvCustomPackRecurring) list.get(ls);
                    if (oid == entResvCustomPackRecurring.getOID()) {
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
