/*
 * PstPayGeneral.java
 *
 * Created on March 29, 2007, 3:33 PM
 */
package com.dimata.posbo.entity.masterdata;

/* package java */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

/* package harisma */
//import com.dimata. harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
//import com.dimata.harisma.entity.payroll.*;
//import com.dimata.harisma.entity.locker.*;
/**
 *
 * @author yunny
 */
public class PstPayGeneral extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PAY_GENERAL = "pay_general";//"PAY_GENERAL";

    public static final int FLD_GEN_ID = 0;
    public static final int FLD_COMPANY_NAME = 1;
    public static final int FLD_COMP_ADDRESS = 2;
    public static final int FLD_CITY = 3;
    public static final int FLD_ZIP_CODE = 4;
    public static final int FLD_BUSSINESS_TYPE = 5;
    public static final int FLD_TAX_OFFICE = 6;
    public static final int FLD_REG_TAX_NR = 7;
    public static final int FLD_REG_TAX_BUS_NR = 8;
    public static final int FLD_REG_TAX_DATE = 9;
    public static final int FLD_TEL = 10;
    public static final int FLD_FAX = 11;
    public static final int FLD_LEADER_NAME = 12;
    public static final int FLD_LEADER_POSITION = 13;
    public static final int FLD_TAX_REP_LOCATION = 14;
    public static final int FLD_TAX_YEAR = 15;
    public static final int FLD_TAX_MONTH = 16;
    public static final int FLD_TAX_REP_DATE = 17;
    public static final int FLD_TAX_PAID_PCT = 18;
    public static final int FLD_TAX_POS_COST_PCT = 19;
    public static final int FLD_TAX_POS_COST_MAX = 20;
    public static final int FLD_TAX_ROUND1000 = 21;
    public static final int FLD_TAX_CALC_MTD = 22;
    public static final int FLD_NON_TAX_INCOME = 23;
    public static final int FLD_NON_TAX_WIFE = 24;
    public static final int FLD_NON_TAX_DEPNT = 25;
    public static final int FLD_TAX_FORM_SIGN_BY = 26;
    public static final int FLD_LOCAL_CUR_CODE = 27;
    public static final int FLD_WORK_DAYS = 28;

    public static final String[] fieldNames = {
        "GEN_ID",
        "COMPANY", //"COMPANY_NAME",
        "COMP_ADDRESS",
        "CITY",
        "ZIP_CODE",
        "BUSSINESS_TYPE",
        "TAX_OFFICE",
        "REG_TAX_NR",
        "REG_TAX_BUS_NR",
        "REG_TAX_DATE",
        "TEL",
        "FAX",
        "LEADER_NAME",
        "LEADER_POSITION",
        "TAX_REP_LOCATION",
        "TAX_YEAR",
        "TAX_MONTH",
        "TAX_REP_DATE",
        "TAX_PAID_PCT",
        "TAX_POS_COST_PCT",
        "TAX_POS_COST_MAX",
        "TAX_ROUND1000",
        "TAX_CALC_MTD",
        "NON_TAX_INCOME",
        "NON_TAX_WIFE",
        "NON_TAX_DEPNT",
        "TAX_FORM_SIGN_BY",
        "LOCAL_CUR_CODE",
        "WORK_DAYS"

    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,};

    //round 1000
    public static final int NO_ROUND1000 = 0;
    public static final int YES_ROUND1000 = 1;

    public static final String[] resignKey = {"Ya", "Tidak"};
    public static final int[] resignValue = {1, 0};

    public static int STS_GROSS = 0;
    public static int STS_NETTO = 1;

    public static String[] stMetode = {
        "Gross", "Netto"
    };

       //pemotong pajak
    public static int PEMOTONG_PAJAK = 0;
    public static int KUASA_PEMOTONG = 1;

    public static String[] signBy = {
        "Pemotong Pajak", "Kuasa Pemotong"
    };

    /**
     * Creates a new instance of PstPayGeneral
     */
    public PstPayGeneral() {

    }

    public PstPayGeneral(int i) throws DBException {
        super(new PstPayGeneral());
    }

    public PstPayGeneral(String sOid) throws DBException {
        super(new PstPayGeneral(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPayGeneral(long lOid) throws DBException {
        super(new PstPayGeneral(0));
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
        return TBL_PAY_GENERAL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPayGeneral().getClass().getName();
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public long fetchExc(Entity ent) throws Exception {
        PayGeneral payGeneral = fetchExc(ent.getOID());
        ent = (Entity) payGeneral;
        return payGeneral.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PayGeneral) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PayGeneral) ent);
    }

    public static PayGeneral fetchExc(long oid) throws DBException {
        try {
            PayGeneral payGeneral = new PayGeneral();
            PstPayGeneral pstPayGeneral = new PstPayGeneral(oid);
            payGeneral.setOID(oid);

            payGeneral.setCompanyName(pstPayGeneral.getString(FLD_COMPANY_NAME));
            payGeneral.setCompAddress(pstPayGeneral.getString(FLD_COMP_ADDRESS));
            payGeneral.setCity(pstPayGeneral.getString(FLD_CITY));
            payGeneral.setZipCode(pstPayGeneral.getString(FLD_ZIP_CODE));
            payGeneral.setBussinessType(pstPayGeneral.getString(FLD_BUSSINESS_TYPE));
            payGeneral.setTaxOffice(pstPayGeneral.getString(FLD_TAX_OFFICE));
            payGeneral.setRegTaxNr(pstPayGeneral.getString(FLD_REG_TAX_NR));
            payGeneral.setRegTaxBusNr(pstPayGeneral.getString(FLD_REG_TAX_BUS_NR));
            payGeneral.setRegTaxDate(pstPayGeneral.getDate(FLD_REG_TAX_DATE));
            payGeneral.setTel(pstPayGeneral.getString(FLD_TEL));
            payGeneral.setFax(pstPayGeneral.getString(FLD_FAX));
            payGeneral.setLeaderName(pstPayGeneral.getString(FLD_LEADER_NAME));
            payGeneral.setLeaderPosition(pstPayGeneral.getString(FLD_LEADER_POSITION));
            payGeneral.setTaxRepLocation(pstPayGeneral.getString(FLD_TAX_REP_LOCATION));
            payGeneral.setTaxYear(pstPayGeneral.getInt(FLD_TAX_YEAR));
            payGeneral.setTaxMonth(pstPayGeneral.getInt(FLD_TAX_MONTH));
            payGeneral.setTaxRepDate(pstPayGeneral.getInt(FLD_TAX_REP_DATE));
            payGeneral.setTaxPaidPct(pstPayGeneral.getdouble(FLD_TAX_PAID_PCT));
            payGeneral.setTaxPosCostPct(pstPayGeneral.getdouble(FLD_TAX_POS_COST_PCT));
            payGeneral.setTaxPosCostMax(pstPayGeneral.getdouble(FLD_TAX_POS_COST_MAX));
            payGeneral.setTaxRound1000(pstPayGeneral.getInt(FLD_TAX_ROUND1000));
            payGeneral.setTaxCalcMtd(pstPayGeneral.getInt(FLD_TAX_CALC_MTD));
            payGeneral.setNonTaxIncome(pstPayGeneral.getdouble(FLD_NON_TAX_INCOME));
            payGeneral.setNonTaxWife(pstPayGeneral.getInt(FLD_NON_TAX_WIFE));
            payGeneral.setNonTaxDepnt(pstPayGeneral.getInt(FLD_NON_TAX_DEPNT));
            payGeneral.setTaxFormSignBy(pstPayGeneral.getInt(FLD_TAX_FORM_SIGN_BY));
            payGeneral.setLocalCurCode(pstPayGeneral.getString(FLD_LOCAL_CUR_CODE));
            payGeneral.setWorkDays(pstPayGeneral.getInt(FLD_WORK_DAYS));

            return payGeneral;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayGeneral(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PayGeneral payGeneral) throws DBException {
        try {
            PstPayGeneral pstPayGeneral = new PstPayGeneral(0);

            pstPayGeneral.setString(FLD_COMPANY_NAME, payGeneral.getCompanyName());
            pstPayGeneral.setString(FLD_COMP_ADDRESS, payGeneral.getCompAddress());
            pstPayGeneral.setString(FLD_CITY, payGeneral.getCity());
            pstPayGeneral.setString(FLD_ZIP_CODE, payGeneral.getZipCode());
            pstPayGeneral.setString(FLD_BUSSINESS_TYPE, payGeneral.getBussinessType());
            pstPayGeneral.setString(FLD_TAX_OFFICE, payGeneral.getTaxOffice());
            pstPayGeneral.setString(FLD_REG_TAX_NR, payGeneral.getRegTaxNr());
            pstPayGeneral.setString(FLD_REG_TAX_BUS_NR, payGeneral.getRegTaxBusNr());
            pstPayGeneral.setDate(FLD_REG_TAX_DATE, payGeneral.getRegTaxDate());
            pstPayGeneral.setString(FLD_TEL, payGeneral.getTel());
            pstPayGeneral.setString(FLD_FAX, payGeneral.getFax());
            pstPayGeneral.setString(FLD_LEADER_NAME, payGeneral.getLeaderName());
            pstPayGeneral.setString(FLD_LEADER_POSITION, payGeneral.getLeaderPosition());
            pstPayGeneral.setString(FLD_TAX_REP_LOCATION, payGeneral.getTaxRepLocation());
            pstPayGeneral.setInt(FLD_TAX_YEAR, payGeneral.getTaxYear());
            pstPayGeneral.setInt(FLD_TAX_MONTH, payGeneral.getTaxMonth());
            pstPayGeneral.setInt(FLD_TAX_REP_DATE, payGeneral.getTaxRepDate());
            pstPayGeneral.setDouble(FLD_TAX_PAID_PCT, payGeneral.getTaxPaidPct());
            pstPayGeneral.setDouble(FLD_TAX_POS_COST_PCT, payGeneral.getTaxPosCostPct());
            pstPayGeneral.setDouble(FLD_TAX_POS_COST_MAX, payGeneral.getTaxPosCostMax());
            pstPayGeneral.setInt(FLD_TAX_ROUND1000, payGeneral.getTaxRound1000());
            pstPayGeneral.setInt(FLD_TAX_CALC_MTD, payGeneral.getTaxCalcMtd());
            pstPayGeneral.setDouble(FLD_NON_TAX_INCOME, payGeneral.getNonTaxIncome());
            pstPayGeneral.setInt(FLD_NON_TAX_WIFE, payGeneral.getNonTaxWife());
            pstPayGeneral.setInt(FLD_NON_TAX_DEPNT, payGeneral.getNonTaxDepnt());
            pstPayGeneral.setInt(FLD_TAX_FORM_SIGN_BY, payGeneral.getTaxFormSignBy());
            pstPayGeneral.setString(FLD_LOCAL_CUR_CODE, payGeneral.getLocalCurCode());
            pstPayGeneral.setInt(FLD_WORK_DAYS, payGeneral.getWorkDays());

            pstPayGeneral.insert();
            payGeneral.setOID(pstPayGeneral.getlong(FLD_GEN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayGeneral(0), DBException.UNKNOWN);
        }
        return payGeneral.getOID();
    }

    public static long updateExc(PayGeneral payGeneral) throws DBException {
        try {
            if (payGeneral.getOID() != 0) {
                PstPayGeneral pstPayGeneral = new PstPayGeneral(payGeneral.getOID());
                pstPayGeneral.setString(FLD_COMPANY_NAME, payGeneral.getCompanyName());
                pstPayGeneral.setString(FLD_COMP_ADDRESS, payGeneral.getCompAddress());
                pstPayGeneral.setString(FLD_CITY, payGeneral.getCity());
                pstPayGeneral.setString(FLD_ZIP_CODE, payGeneral.getZipCode());
                pstPayGeneral.setString(FLD_BUSSINESS_TYPE, payGeneral.getBussinessType());
                pstPayGeneral.setString(FLD_TAX_OFFICE, payGeneral.getTaxOffice());
                pstPayGeneral.setString(FLD_REG_TAX_NR, payGeneral.getRegTaxNr());
                pstPayGeneral.setString(FLD_REG_TAX_BUS_NR, payGeneral.getRegTaxBusNr());
                pstPayGeneral.setDate(FLD_REG_TAX_DATE, payGeneral.getRegTaxDate());
                pstPayGeneral.setString(FLD_TEL, payGeneral.getTel());
                pstPayGeneral.setString(FLD_FAX, payGeneral.getFax());
                pstPayGeneral.setString(FLD_LEADER_NAME, payGeneral.getLeaderName());
                pstPayGeneral.setString(FLD_LEADER_POSITION, payGeneral.getLeaderPosition());
                pstPayGeneral.setString(FLD_TAX_REP_LOCATION, payGeneral.getTaxRepLocation());
                pstPayGeneral.setInt(FLD_TAX_YEAR, payGeneral.getTaxYear());
                pstPayGeneral.setInt(FLD_TAX_MONTH, payGeneral.getTaxMonth());
                pstPayGeneral.setInt(FLD_TAX_REP_DATE, payGeneral.getTaxRepDate());
                pstPayGeneral.setDouble(FLD_TAX_PAID_PCT, payGeneral.getTaxPaidPct());
                pstPayGeneral.setDouble(FLD_TAX_POS_COST_PCT, payGeneral.getTaxPosCostPct());
                pstPayGeneral.setDouble(FLD_TAX_POS_COST_MAX, payGeneral.getTaxPosCostMax());
                pstPayGeneral.setInt(FLD_TAX_ROUND1000, payGeneral.getTaxRound1000());
                pstPayGeneral.setInt(FLD_TAX_CALC_MTD, payGeneral.getTaxCalcMtd());
                pstPayGeneral.setDouble(FLD_NON_TAX_INCOME, payGeneral.getNonTaxIncome());
                pstPayGeneral.setInt(FLD_NON_TAX_WIFE, payGeneral.getNonTaxWife());
                pstPayGeneral.setInt(FLD_NON_TAX_DEPNT, payGeneral.getNonTaxDepnt());
                pstPayGeneral.setInt(FLD_TAX_FORM_SIGN_BY, payGeneral.getTaxFormSignBy());
                pstPayGeneral.setString(FLD_LOCAL_CUR_CODE, payGeneral.getLocalCurCode());
                pstPayGeneral.setInt(FLD_WORK_DAYS, payGeneral.getWorkDays());

                pstPayGeneral.update();
                return payGeneral.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayGeneral(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPayGeneral pstPayGeneral = new PstPayGeneral(oid);
            pstPayGeneral.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayGeneral(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 1000, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_GENERAL;
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
            System.out.println("SQL LIST" + sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PayGeneral payGeneral = new PayGeneral();
                resultToObject(rs, payGeneral);
                lists.add(payGeneral);
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

    public static void resultToObject(ResultSet rs, PayGeneral payGeneral) {
        try {
            payGeneral.setOID(rs.getLong(PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID]));
            payGeneral.setCompanyName(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMPANY_NAME]));
            payGeneral.setCompAddress(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMP_ADDRESS]));
            payGeneral.setCity(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_CITY]));
            payGeneral.setZipCode(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_ZIP_CODE]));
            payGeneral.setBussinessType(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_BUSSINESS_TYPE]));
            payGeneral.setTaxOffice(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_OFFICE]));
            payGeneral.setRegTaxNr(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_REG_TAX_NR]));
            payGeneral.setRegTaxBusNr(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_REG_TAX_BUS_NR]));
            payGeneral.setRegTaxDate(rs.getDate(PstPayGeneral.fieldNames[PstPayGeneral.FLD_REG_TAX_DATE]));
            payGeneral.setTel(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TEL]));
            payGeneral.setFax(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_FAX]));
            payGeneral.setLeaderName(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_LEADER_NAME]));
            payGeneral.setLeaderPosition(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_LEADER_POSITION]));
            payGeneral.setTaxRepLocation(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_REP_LOCATION]));
            payGeneral.setTaxYear(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_YEAR]));
            payGeneral.setTaxMonth(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_MONTH]));
            payGeneral.setTaxRepDate(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_REP_DATE]));
            payGeneral.setTaxPaidPct(rs.getDouble(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_PAID_PCT]));
            payGeneral.setTaxPosCostPct(rs.getDouble(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_POS_COST_PCT]));
            payGeneral.setTaxPosCostMax(rs.getDouble(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_POS_COST_MAX]));
            payGeneral.setTaxRound1000(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_ROUND1000]));
            payGeneral.setTaxCalcMtd(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_CALC_MTD]));
            payGeneral.setNonTaxIncome(rs.getDouble(PstPayGeneral.fieldNames[PstPayGeneral.FLD_NON_TAX_INCOME]));
            payGeneral.setNonTaxWife(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_NON_TAX_WIFE]));
            payGeneral.setNonTaxDepnt(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_NON_TAX_DEPNT]));
            payGeneral.setTaxFormSignBy(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_FORM_SIGN_BY]));
            payGeneral.setLocalCurCode(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_LOCAL_CUR_CODE]));
            payGeneral.setWorkDays(rs.getInt(PstPayGeneral.fieldNames[PstPayGeneral.FLD_WORK_DAYS]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long genId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_GENERAL + " WHERE "
                    + PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID] + " = '" + genId + "'";

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
            String sql = "SELECT COUNT(" + PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID] + ") FROM " + TBL_PAY_GENERAL;
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

    /*public static long getEmployeeIdByNum(String empNum) {
     DBResultSet dbrs = null;
     long empOid = 0;
     try{
     String sql = "SELECT " + fieldNames[FLD_GEN_ID] +
     " FROM " + TBL_PAY_GENERAL +
     " WHERE " + PstPayGeneral.fieldNames[PstPayGeneral.FLD_EMPLOYEE_NUM] + " = '" + empNum + "'";
            
     //System.out.println("sql : " + sql);
     dbrs = DBHandler.execQueryResult(sql);
     ResultSet rs = dbrs.getResultSet();
     while(rs.next()) {
     empOid = rs.getLong(fieldNames[FLD_EMPLOYEE_ID]);
     }
            
     rs.close();
     return empOid;
     }
     catch(Exception e) {
     System.out.println("err : "+e.toString());
     }
     finally {
     DBResultSet.close(dbrs);
     return empOid;
     }
     }*/
    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PayGeneral payGeneral = (PayGeneral) list.get(ls);
                    if (oid == payGeneral.getOID()) {
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
        vectSize = vectSize + mdl;
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
                PayGeneral payGeneral = PstPayGeneral.fetchExc(oid);
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID], payGeneral.getOID());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_BUSSINESS_TYPE], payGeneral.getBussinessType());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_CITY], payGeneral.getCity());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMPANY_NAME], payGeneral.getCompanyName());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMP_ADDRESS], payGeneral.getCompAddress());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_FAX], payGeneral.getFax());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_LEADER_NAME], payGeneral.getLeaderName());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_LEADER_POSITION], payGeneral.getLeaderPosition());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_LOCAL_CUR_CODE], payGeneral.getLocalCurCode());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_NON_TAX_DEPNT], payGeneral.getNonTaxDepnt());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_NON_TAX_INCOME], payGeneral.getNonTaxIncome());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_NON_TAX_WIFE], payGeneral.getNonTaxWife());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_REG_TAX_BUS_NR], payGeneral.getRegTaxBusNr());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_REG_TAX_DATE], payGeneral.getRegTaxDate());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_REG_TAX_NR], payGeneral.getRegTaxBusNr());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_CALC_MTD], payGeneral.getTaxCalcMtd());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_FORM_SIGN_BY], payGeneral.getTaxFormSignBy());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_MONTH], payGeneral.getTaxMonth());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_OFFICE], payGeneral.getTaxOffice());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_PAID_PCT], payGeneral.getTaxPosCostPct());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_POS_COST_MAX], payGeneral.getTaxPosCostMax());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_POS_COST_PCT], payGeneral.getTaxPosCostPct());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_REP_DATE], payGeneral.getTaxRepDate());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_REP_LOCATION], payGeneral.getTaxRepLocation());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_ROUND1000], payGeneral.getTaxRound1000());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TAX_YEAR], payGeneral.getTaxYear());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_TEL], payGeneral.getTel());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_WORK_DAYS], payGeneral.getWorkDays());
                object.put(PstPayGeneral.fieldNames[PstPayGeneral.FLD_ZIP_CODE], payGeneral.getZipCode());
            }catch(Exception exc){}

            return object;
        }
}
