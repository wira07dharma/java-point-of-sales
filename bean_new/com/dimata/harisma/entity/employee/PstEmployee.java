
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */
/** *****************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 ****************************************************************** */
package com.dimata.harisma.entity.employee;

/* package java */
import com.dimata.aiso.entity.admin.AppUser;
import com.dimata.common.entity.system.PstSystemProperty;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

import com.dimata.qdep.entity.*;

/* package harisma */
//import com.dimata. harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.locker.*;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.posbo.entity.masterdata.PstCompany;
import com.dimata.services.WebServices;
import org.json.JSONArray;
import org.json.JSONObject;

public class PstEmployee extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_HR_EMPLOYEE = "hr_employee";

    public static final int FLD_EMPLOYEE_ID = 0;
    public static final int FLD_DEPARTMENT_ID = 1;
    public static final int FLD_POSITION_ID = 2;
    public static final int FLD_SECTION_ID = 3;
    public static final int FLD_EMPLOYEE_NUM = 4;
    public static final int FLD_EMP_CATEGORY_ID = 5;
    public static final int FLD_LEVEL_ID = 6;
    public static final int FLD_FULL_NAME = 7;
    public static final int FLD_ADDRESS = 8;
    public static final int FLD_PHONE = 9;
    public static final int FLD_HANDPHONE = 10;
    public static final int FLD_POSTAL_CODE = 11;
    public static final int FLD_SEX = 12;
    public static final int FLD_BIRTH_PLACE = 13;
    public static final int FLD_BIRTH_DATE = 14;
    public static final int FLD_RELIGION_ID = 15;
    public static final int FLD_BLOOD_TYPE = 16;
    public static final int FLD_ASTEK_NUM = 17;
    public static final int FLD_ASTEK_DATE = 18;
    public static final int FLD_MARITAL_ID = 19;
    public static final int FLD_LOCKER_ID = 20;
    public static final int FLD_COMMENCING_DATE = 21;
    public static final int FLD_RESIGNED = 22;
    public static final int FLD_RESIGNED_DATE = 23;
    public static final int FLD_BARCODE_NUMBER = 24;
    public static final int FLD_RESIGNED_REASON_ID = 25;
    public static final int FLD_RESIGNED_DESC = 26;
    public static final int FLD_BASIC_SALARY = 27;
    public static final int FLD_ASSIGN_TO_ACCOUNTING = 28;
    public static final int FLD_DIVISION_ID = 29;
    public static final int FLD_COMPANY_ID = 30;
    public static final int FLD_LOCATION_ID = 31;

    public static final String[] fieldNames = {
        "EMPLOYEE_ID",
        "DEPARTMENT_ID",
        "POSITION_ID",
        "SECTION_ID",
        "EMPLOYEE_NUM",
        "EMP_CATEGORY_ID",
        "LEVEL_ID",
        "FULL_NAME",
        "ADDRESS",
        "PHONE",
        "HANDPHONE",
        "POSTAL_CODE",
        "SEX",
        "BIRTH_PLACE",
        "BIRTH_DATE",
        "RELIGION_ID",
        "BLOOD_TYPE",
        "ASTEK_NUM",
        "ASTEK_DATE",
        "MARITAL_ID",
        "LOCKER_ID",
        "COMMENCING_DATE",
        "RESIGNED",
        "RESIGNED_DATE",
        "BARCODE_NUMBER",
        "RESIGNED_REASON_ID",
        "RESIGNED_DESC",
        "BASIC_SALARY",
        "IS_ASSIGN_TO_ACCOUNTING",
        "DIVISION_ID",
        "COMPANY_ID",
        "LOCATION_ID"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    //gender----
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final String[] sexKey = {"Male", "Female"};
    public static final int[] sexValue = {0, 1};

    //resigned
    public static final int NO_RESIGN = 0;
    public static final int YES_RESIGN = 1;

    public static final String[] resignKey = {"No", "Yes"};
    public static final int[] resignValue = {0, 1};

    //resigned
    public static final String[] blood = {"A", "B", "O", "AB"};

    public static Vector getBlood() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < blood.length; i++) {
            result.add(blood[i]);
        }
        return result;
    }

    public PstEmployee() {
    }

    public PstEmployee(int i) throws DBException {
        super(new PstEmployee());
    }

    public PstEmployee(String sOid) throws DBException {
        super(new PstEmployee(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstEmployee(long lOid) throws DBException {
        super(new PstEmployee(0));
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
        return TBL_HR_EMPLOYEE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEmployee().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Employee employee = fetchExc(ent.getOID());
        ent = (Entity) employee;
        return employee.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Employee) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Employee) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Employee fetchExc(long oid) throws DBException {
        try {
            Employee employee = new Employee();
            PstEmployee pstEmployee = new PstEmployee(oid);
            employee.setOID(oid);

            employee.setDepartmentId(pstEmployee.getlong(FLD_DEPARTMENT_ID));
            employee.setPositionId(pstEmployee.getlong(FLD_POSITION_ID));
            employee.setSectionId(pstEmployee.getlong(FLD_SECTION_ID));
            employee.setEmployeeNum(pstEmployee.getString(FLD_EMPLOYEE_NUM));
            employee.setEmpCategoryId(pstEmployee.getlong(FLD_EMP_CATEGORY_ID));
            employee.setLevelId(pstEmployee.getlong(FLD_LEVEL_ID));
            employee.setFullName(pstEmployee.getString(FLD_FULL_NAME));
            employee.setAddress(pstEmployee.getString(FLD_ADDRESS));
            employee.setPhone(pstEmployee.getString(FLD_PHONE));
            employee.setHandphone(pstEmployee.getString(FLD_HANDPHONE));
            employee.setPostalCode(pstEmployee.getInt(FLD_POSTAL_CODE));
            employee.setSex(pstEmployee.getInt(FLD_SEX));
            employee.setBirthPlace(pstEmployee.getString(FLD_BIRTH_PLACE));
            employee.setBirthDate(pstEmployee.getDate(FLD_BIRTH_DATE));
            employee.setReligionId(pstEmployee.getlong(FLD_RELIGION_ID));
            employee.setBloodType(pstEmployee.getString(FLD_BLOOD_TYPE));
            employee.setAstekNum(pstEmployee.getString(FLD_ASTEK_NUM));
            employee.setAstekDate(pstEmployee.getDate(FLD_ASTEK_DATE));
            employee.setMaritalId(pstEmployee.getlong(FLD_MARITAL_ID));
            employee.setLockerId(pstEmployee.getlong(FLD_LOCKER_ID));
            employee.setCommencingDate(pstEmployee.getDate(FLD_COMMENCING_DATE));
            employee.setResigned(pstEmployee.getInt(FLD_RESIGNED));
            employee.setResignedDate(pstEmployee.getDate(FLD_RESIGNED_DATE));
            employee.setBarcodeNumber(pstEmployee.getString(FLD_BARCODE_NUMBER));
            employee.setResignedReasonId(pstEmployee.getlong(FLD_RESIGNED_REASON_ID));
            employee.setResignedDesc(pstEmployee.getString(FLD_RESIGNED_DESC));
            employee.setBasicSalary(pstEmployee.getdouble(FLD_BASIC_SALARY));
            employee.setIsAssignToAccounting(pstEmployee.getboolean(FLD_ASSIGN_TO_ACCOUNTING));
            employee.setDivisionId(pstEmployee.getlong(FLD_DIVISION_ID));
            employee.setCompanyId(pstEmployee.getlong(FLD_COMPANY_ID));
            employee.setLocationId(pstEmployee.getlong(FLD_LOCATION_ID));

            return employee;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Employee employee) throws DBException {
        try {
            PstEmployee pstEmployee = new PstEmployee(0);

            pstEmployee.setLong(FLD_DEPARTMENT_ID, employee.getDepartmentId());
            pstEmployee.setLong(FLD_POSITION_ID, employee.getPositionId());
            pstEmployee.setLong(FLD_SECTION_ID, employee.getSectionId());
            pstEmployee.setString(FLD_EMPLOYEE_NUM, employee.getEmployeeNum());
            pstEmployee.setLong(FLD_EMP_CATEGORY_ID, employee.getEmpCategoryId());
            pstEmployee.setLong(FLD_LEVEL_ID, employee.getLevelId());
            pstEmployee.setString(FLD_FULL_NAME, employee.getFullName());
            pstEmployee.setString(FLD_ADDRESS, employee.getAddress());
            pstEmployee.setString(FLD_PHONE, employee.getPhone());
            pstEmployee.setString(FLD_HANDPHONE, employee.getHandphone());
            pstEmployee.setInt(FLD_POSTAL_CODE, employee.getPostalCode());
            pstEmployee.setInt(FLD_SEX, employee.getSex());
            pstEmployee.setString(FLD_BIRTH_PLACE, employee.getBirthPlace());
            pstEmployee.setDate(FLD_BIRTH_DATE, employee.getBirthDate());
            pstEmployee.setLong(FLD_RELIGION_ID, employee.getReligionId());
            pstEmployee.setString(FLD_BLOOD_TYPE, employee.getBloodType());
            pstEmployee.setString(FLD_ASTEK_NUM, employee.getAstekNum());
            pstEmployee.setDate(FLD_ASTEK_DATE, employee.getAstekDate());
            pstEmployee.setLong(FLD_MARITAL_ID, employee.getMaritalId());
            pstEmployee.setLong(FLD_LOCKER_ID, employee.getLockerId());
            pstEmployee.setDate(FLD_COMMENCING_DATE, employee.getCommencingDate());
            pstEmployee.setInt(FLD_RESIGNED, employee.getResigned());
            pstEmployee.setDate(FLD_RESIGNED_DATE, employee.getResignedDate());
            pstEmployee.setString(FLD_BARCODE_NUMBER, employee.getBarcodeNumber());
            pstEmployee.setLong(FLD_RESIGNED_REASON_ID, employee.getResignedReasonId());
            pstEmployee.setString(FLD_RESIGNED_DESC, employee.getResignedDesc());
            pstEmployee.setDouble(FLD_BASIC_SALARY, employee.getBasicSalary());
            pstEmployee.setboolean(FLD_ASSIGN_TO_ACCOUNTING, employee.getIsAssignToAccounting());
            pstEmployee.setLong(FLD_DIVISION_ID, employee.getDivisionId());
            pstEmployee.setLong(FLD_COMPANY_ID, employee.getCompanyId());
            pstEmployee.setLong(FLD_LOCATION_ID, employee.getLocationId());

            pstEmployee.insert();
            employee.setOID(pstEmployee.getlong(FLD_EMPLOYEE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
        return employee.getOID();
    }

    public static long updateExc(Employee employee) throws DBException {
        try {
            if (employee.getOID() != 0) {
                PstEmployee pstEmployee = new PstEmployee(employee.getOID());

                pstEmployee.setLong(FLD_DEPARTMENT_ID, employee.getDepartmentId());
                pstEmployee.setLong(FLD_POSITION_ID, employee.getPositionId());
                pstEmployee.setLong(FLD_SECTION_ID, employee.getSectionId());
                pstEmployee.setString(FLD_EMPLOYEE_NUM, employee.getEmployeeNum());
                pstEmployee.setLong(FLD_EMP_CATEGORY_ID, employee.getEmpCategoryId());
                pstEmployee.setLong(FLD_LEVEL_ID, employee.getLevelId());
                pstEmployee.setString(FLD_FULL_NAME, employee.getFullName());
                pstEmployee.setString(FLD_ADDRESS, employee.getAddress());
                pstEmployee.setString(FLD_PHONE, employee.getPhone());
                pstEmployee.setString(FLD_HANDPHONE, employee.getHandphone());
                pstEmployee.setInt(FLD_POSTAL_CODE, employee.getPostalCode());
                pstEmployee.setInt(FLD_SEX, employee.getSex());
                pstEmployee.setString(FLD_BIRTH_PLACE, employee.getBirthPlace());
                pstEmployee.setDate(FLD_BIRTH_DATE, employee.getBirthDate());
                pstEmployee.setLong(FLD_RELIGION_ID, employee.getReligionId());
                pstEmployee.setString(FLD_BLOOD_TYPE, employee.getBloodType());
                pstEmployee.setString(FLD_ASTEK_NUM, employee.getAstekNum());
                pstEmployee.setDate(FLD_ASTEK_DATE, employee.getAstekDate());
                pstEmployee.setLong(FLD_MARITAL_ID, employee.getMaritalId());
                pstEmployee.setLong(FLD_LOCKER_ID, employee.getLockerId());
                pstEmployee.setDate(FLD_COMMENCING_DATE, employee.getCommencingDate());
                pstEmployee.setInt(FLD_RESIGNED, employee.getResigned());
                pstEmployee.setDate(FLD_RESIGNED_DATE, employee.getResignedDate());
                pstEmployee.setString(FLD_BARCODE_NUMBER, employee.getBarcodeNumber());
                pstEmployee.setLong(FLD_RESIGNED_REASON_ID, employee.getResignedReasonId());
                pstEmployee.setString(FLD_RESIGNED_DESC, employee.getResignedDesc());
                pstEmployee.setDouble(FLD_BASIC_SALARY, employee.getBasicSalary());
                pstEmployee.setboolean(FLD_ASSIGN_TO_ACCOUNTING, employee.getIsAssignToAccounting());
                pstEmployee.setLong(FLD_DIVISION_ID, employee.getDivisionId());
                pstEmployee.setLong(FLD_COMPANY_ID, employee.getCompanyId());
                pstEmployee.setLong(FLD_LOCATION_ID, employee.getLocationId());

                pstEmployee.update();
                return employee.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstEmployee pstEmployee = new PstEmployee(oid);
            pstEmployee.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;
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
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
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

    public static Vector listPetugas(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT HE.* FROM " + TBL_HR_EMPLOYEE + " AS HE"
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " AS HP"
                    + " ON HE." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + " = HP." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + " INNER JOIN " + PstCompany.TBL_AISO_COMPANY + " AS AC"
                    + " ON HE." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                    + " = AC." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID];
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
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
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

    public static void resultToObject(ResultSet rs, Employee employee) {
        try {
            employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
            employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
            employee.setPositionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
            employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
            employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
            employee.setEmpCategoryId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]));
            employee.setLevelId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]));
            employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
            employee.setAddress(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]));
            employee.setPhone(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE]));
            employee.setHandphone(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]));
            employee.setPostalCode(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]));
            employee.setSex(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_SEX]));
            employee.setBirthPlace(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]));
            employee.setBirthDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]));
            employee.setReligionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]));
            employee.setBloodType(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]));
            employee.setAstekNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM]));
            employee.setAstekDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE]));
            employee.setMaritalId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]));
            employee.setLockerId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]));
            employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
            employee.setResigned(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]));
            employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
            employee.setBarcodeNumber(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]));
            employee.setResignedReasonId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID]));
            employee.setResignedDesc(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC]));
            employee.setBasicSalary(rs.getDouble(PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY]));
            employee.setIsAssignToAccounting(rs.getBoolean(PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING]));
            employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
            employee.setCompanyId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]));
            employee.setLocationId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long employeeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = '" + employeeId + "'";

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
            String sql = "SELECT COUNT(" + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ") FROM " + TBL_HR_EMPLOYEE;
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
                    Employee employee = (Employee) list.get(ls);
                    if (oid == employee.getOID()) {
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

    public static void updateBarcode(long employeeId, String barcode) {
        DBResultSet dbrs = null;
        //boolean result = false;
        //String barcode = (barcodeNumber.equals(null)) ? "null" : barcodeNumber;

        try {
            String sql = "";
            if (barcode != null) {
                sql = " UPDATE " + TBL_HR_EMPLOYEE + " SET "
                        + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = '"
                        + barcode + "' WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;
            } else {
                sql = " UPDATE " + TBL_HR_EMPLOYEE + " SET "
                        + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = NULL"
                        + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;
            }

            //dbrs = DBHandler.execQueryResult(sql);
            int status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();
            System.out.println("\tupdateBarcode : " + sql);
            //while(rs.next()) { result = true; }
            //rs.close();
        } catch (Exception e) {
            System.err.println("\tupdateBarcode error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }

    public static void deleteBarcode() {
        DBResultSet dbrs = null;
        //boolean result = false;

        try {
            String sql = " UPDATE " + TBL_HR_EMPLOYEE
                    + " SET " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = NULL ";

            int status = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.err.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static long getEmployeeByBarcode(String barcode) {
        long result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = '"
                    + barcode + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            //return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static long getEmployeeByName(String name) {
        long result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " = '"
                    + name + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            //return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static boolean checkDepartment(long employeeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = '" + employeeId + "'";

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

    public static boolean checkEmpCategory(long empCategoryId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = '" + empCategoryId + "'";

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

    public static boolean checkLevel(long levelId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = '" + levelId + "'";

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

    public static boolean checkMarital(long maritalId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID] + " = '" + maritalId + "'";

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

    public static boolean checkPosition(long positionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = '" + positionId + "'";

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

    public static boolean checkReligion(long religionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] + " = '" + religionId + "'";

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

    public static boolean checkSection(long sectionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = '" + sectionId + "'";

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

    // check Locker
    public static boolean checkLocker(Locker locker) {
        DBResultSet dbrs = null;
        boolean ifExist = false;
        try {
            String sql = " SELECT EMP.* FROM " + TBL_HR_EMPLOYEE + " EMP "
                    + " ," + PstLocker.TBL_HR_LOCKER + " LOC "
                    + " WHERE LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]
                    + " AND LOC." + PstLocker.fieldNames[PstLocker.FLD_CONDITION_ID]
                    + " = " + locker.getConditionId();

            if (locker.getLockerNumber().length() > 0) {
                sql = sql + " AND LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCKER_NUMBER]
                        + " = " + locker.getLockerNumber().length();
            }

            sql = sql + " AND LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]
                    + " != " + locker.getOID();

            System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Employee emp = new Employee();

            while (rs.next()) {
                ifExist = true;
            }
            rs.close();
            return ifExist;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }

    /**
     * Used to check if employee number already exist
     *
     * @param empNum
     * @return
     * @created by edhy
     */
    public static boolean empNumExist(String empNum) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = '" + empNum + "'";

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

    // ------------------------ start added by gedhy ---------------------------
    /**
     * this method used to get name of employee created by gedhy
     */
    public static String getEmployeeName(long employeeOid) {
        String result = "";
        try {
            PstEmployee pstEmployee = new PstEmployee();
            Employee employee = pstEmployee.fetchExc(employeeOid);
            return employee.getFullName();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return result;
    }

    /**
     * this method used to list all employee specified by his/her department,
     * section and department created by gedhy
     */
    public static Vector listEmployee(long departmentId, long sectionId, long positionId) {
        Vector result = new Vector(1, 1);
        String whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + departmentId + " AND "
                + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + sectionId + " AND "
                + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + positionId;
        Vector vectTemp = PstEmployee.list(0, 0, whereClause, "");
        if (vectTemp != null && vectTemp.size() > 0) {
            for (int i = 0; i < vectTemp.size(); i++) {
                Employee employee = (Employee) vectTemp.get(i);

                Vector temp = new Vector(1, 1);
                temp.add("" + employee.getOID());
                temp.add(employee.getFullName());

                result.add(temp);
            }
        }

        return result;
    }
    // ------------------ end added by gedhy -----------------------------------

    public static long insertByOid(Employee employee) throws DBException {
        try {
            PstEmployee pstEmployee = new PstEmployee(0);

            pstEmployee.setLong(FLD_DEPARTMENT_ID, employee.getDepartmentId());
            pstEmployee.setLong(FLD_POSITION_ID, employee.getPositionId());
            pstEmployee.setLong(FLD_SECTION_ID, employee.getSectionId());
            pstEmployee.setString(FLD_EMPLOYEE_NUM, employee.getEmployeeNum());
            pstEmployee.setLong(FLD_EMP_CATEGORY_ID, employee.getEmpCategoryId());
            pstEmployee.setLong(FLD_LEVEL_ID, employee.getLevelId());
            pstEmployee.setString(FLD_FULL_NAME, employee.getFullName());
            pstEmployee.setString(FLD_ADDRESS, employee.getAddress());
            pstEmployee.setString(FLD_PHONE, employee.getPhone());
            pstEmployee.setString(FLD_HANDPHONE, employee.getHandphone());
            pstEmployee.setInt(FLD_POSTAL_CODE, employee.getPostalCode());
            pstEmployee.setInt(FLD_SEX, employee.getSex());
            pstEmployee.setString(FLD_BIRTH_PLACE, employee.getBirthPlace());
            pstEmployee.setDate(FLD_BIRTH_DATE, employee.getBirthDate());
            pstEmployee.setLong(FLD_RELIGION_ID, employee.getReligionId());
            pstEmployee.setString(FLD_BLOOD_TYPE, employee.getBloodType());
            pstEmployee.setString(FLD_ASTEK_NUM, employee.getAstekNum());
            pstEmployee.setDate(FLD_ASTEK_DATE, employee.getAstekDate());
            pstEmployee.setLong(FLD_MARITAL_ID, employee.getMaritalId());
            pstEmployee.setLong(FLD_LOCKER_ID, employee.getLockerId());
            pstEmployee.setDate(FLD_COMMENCING_DATE, employee.getCommencingDate());
            pstEmployee.setInt(FLD_RESIGNED, employee.getResigned());
            pstEmployee.setDate(FLD_RESIGNED_DATE, employee.getResignedDate());
            pstEmployee.setString(FLD_BARCODE_NUMBER, employee.getBarcodeNumber());
            pstEmployee.setLong(FLD_RESIGNED_REASON_ID, employee.getResignedReasonId());
            pstEmployee.setString(FLD_RESIGNED_DESC, employee.getResignedDesc());
            pstEmployee.setDouble(FLD_BASIC_SALARY, employee.getBasicSalary());
            pstEmployee.setboolean(FLD_ASSIGN_TO_ACCOUNTING, employee.getIsAssignToAccounting());
            pstEmployee.setLong(FLD_DIVISION_ID, employee.getDivisionId());
            employee.setLocationId(pstEmployee.getlong(FLD_LOCATION_ID));
            pstEmployee.insertByOid(employee.getOID());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), com.dimata.qdep.db.DBException.UNKNOWN);
        }
        return employee.getOID();
    }

    public static JSONObject fetchJSON(long oid) {
        JSONObject object = new JSONObject();
        try {
            Employee employee = PstEmployee.fetchExc(oid);
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID], employee.getOID());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID], employee.getDepartmentId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID], employee.getPositionId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID], employee.getSectionId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], employee.getEmployeeNum());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID], employee.getEmpCategoryId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID], employee.getLevelId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], employee.getFullName());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS], employee.getAddress());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_PHONE], employee.getPhone());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], employee.getHandphone());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE], employee.getPostalCode());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_SEX], employee.getSex());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE], employee.getBirthPlace());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE], employee.getBirthDate());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID], employee.getReligionId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE], employee.getBloodType());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM], employee.getAstekNum());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE], employee.getAstekDate());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID], employee.getMaritalId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID], employee.getLockerId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE], employee.getCommencingDate());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED], employee.getResigned());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE], employee.getResignedDate());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER], employee.getBarcodeNumber());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID], employee.getResignedReasonId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC], employee.getResignedDesc());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY], employee.getBasicSalary());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING], employee.getIsAssignToAccounting());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID], employee.getDivisionId());
            object.put(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID], employee.getCompanyId());
        } catch (Exception exc) {
        }

        return object;
    }

    public static long syncExc(JSONObject jSONObject) {
        long oid = 0;
        if (jSONObject != null) {
            oid = jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID], 0);
            if (oid > 0) {
                Employee employee = new Employee();
                employee.setOID(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID], 0));
                employee.setDepartmentId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID], 0));
                employee.setPositionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID], 0));
                employee.setSectionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID], 0));
                employee.setEmployeeNum(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], ""));
                employee.setEmpCategoryId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID], 0));
                employee.setLevelId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID], 0));
                employee.setFullName(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], ""));
                employee.setAddress(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS], ""));
                employee.setPhone(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE], ""));
                employee.setHandphone(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], ""));
                employee.setPostalCode(jSONObject.optInt(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE], 0));
                employee.setSex(jSONObject.optInt(PstEmployee.fieldNames[PstEmployee.FLD_SEX], 0));
                employee.setBirthPlace(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE], ""));
                employee.setBirthDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE], "0000-00-00"), "yyyy-MM-dd"));
                employee.setReligionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID], 0));
                employee.setBloodType(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE], ""));
                employee.setAstekNum(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM], ""));
                employee.setAstekDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE], "0000-00-00"), "yyyy-MM-dd"));
                employee.setMaritalId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID], 0));
                employee.setLockerId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID], 0));
                employee.setCommencingDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE], "0000-00-00"), "yyyy-MM-dd"));
                employee.setResigned(jSONObject.optInt(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED], 0));
                employee.setResignedDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE], "0000-00-00"), "yyyy-MM-dd"));
                employee.setBarcodeNumber(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER], ""));
                employee.setResignedReasonId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID], 0));
                employee.setResignedDesc(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC], ""));
                employee.setBasicSalary(jSONObject.optDouble(PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY], 0));
                employee.setIsAssignToAccounting(jSONObject.optBoolean(PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING], false));
                employee.setDivisionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID], 0));
                boolean checkOidEmployee = PstEmployee.checkOID(employee.getOID());
                try {
                    if (checkOidEmployee) {
                        PstEmployee.updateExc(employee);
                    } else {
                        PstEmployee.insertByOid(employee);
                    }
                } catch (Exception exc) {
                }
            }
        }

        return oid;
    }

    public static Vector getListFromApiAll() {
        return getListFromApi(0, 0, "", "");
    }

    /**
     * use emp for employee or dv for division when create custom where clause
    *
     */
    public static Vector getListFromApi(int limitStart, int recordToGet, String whereClause, String order) {
        JSONArray lists = new JSONArray();
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String param = "limitStart=" + WebServices.encodeUrl("" + limitStart) + "&recordToGet=" + WebServices.encodeUrl("" + recordToGet)
                + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("");
        JSONObject jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
        Vector listData = new Vector(1, 1);
        try {
            lists = jo.getJSONArray("DATA");
            for (int i = 0; i < lists.length(); i++) {
                JSONArray array = lists.getJSONArray(i);
                JSONObject tempObj = array.getJSONObject(0);
                Employee emp = new Employee();
                convertJsonToObject(tempObj, emp);
                listData.add(emp);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return listData;
    }

    /**
     * use emp for employee or dv for division when create custom where clause
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return 
    *
     */
    public static JSONArray getListEmpDivFromApi(int limitStart, int recordToGet, String whereClause, String order) {
        JSONArray lists = new JSONArray();
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String param = "limitStart=" + WebServices.encodeUrl("" + limitStart) + "&recordToGet=" + WebServices.encodeUrl("" + recordToGet)
                + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("");
        JSONObject jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
        try {
            lists = jo.getJSONArray("DATA");
        } catch (Exception e) {
            System.out.println(e);
        }
        return lists;
    }

    /*
    * @return JSONObject that contain array of employee and its division
     */
    public static JSONArray fetchEmpDivFromApi(long oid) {
        JSONArray lists = new JSONArray();
        String whereClause = "emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + oid;
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String param = "limitStart=" + WebServices.encodeUrl("" + 0) + "&recordToGet=" + WebServices.encodeUrl("" + 0)
                + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("");
        JSONObject jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
        try {
            lists = jo.getJSONArray("DATA");
        } catch (Exception e) {
            System.out.println(e);
        }
        return lists.optJSONArray(0);
    }

    public static int getCountFromApi(int limitStart, int recordToGet, String whereClause, String order) {
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String param = "limitStart=" + WebServices.encodeUrl("" + limitStart) + "&recordToGet=" + WebServices.encodeUrl("" + recordToGet)
                + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("");
        JSONObject jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
        int count = 0;
        try {
            count = jo.getInt("COUNT");
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public static Employee fetchFromApi(long oid) {
        String whereClause = "emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + oid;
        Vector listData = getListFromApi(0, 0, whereClause, "");
        Employee emp = new Employee();
        if (!listData.isEmpty()) {
            emp = (Employee) listData.get(0);
        }
        return emp;
    }

    public static void objectToHashtable(Hashtable result, Employee emp) {
        try {
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID], emp.getOID());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], emp.getEmployeeNum());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], emp.getFullName());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS], emp.getAddress());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_PHONE], emp.getPhone());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE], emp.getBirthPlace());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE], emp.getBirthDate());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_SEX], emp.getSex());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE], emp.getCommencingDate());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID], emp.getDivisionId());
            result.put(PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID], emp.getLocationId());
        } catch (Exception e) {
            System.out.println("Err " + e.getMessage());
        }
    }

    public static void convertJsonToObject(JSONObject jSONObject, Employee employee) {
        try {
            employee.setOID(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID], 0));
            employee.setDepartmentId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID], 0));
            employee.setPositionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID], 0));
            employee.setSectionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID], 0));
            employee.setEmployeeNum(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], ""));
            employee.setEmpCategoryId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID], 0));
            employee.setLevelId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID], 0));
            employee.setFullName(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], ""));
            employee.setAddress(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS], ""));
            employee.setPhone(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE], ""));
            employee.setHandphone(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], ""));
            employee.setPostalCode(jSONObject.optInt(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE], 0));
            employee.setSex(jSONObject.optInt(PstEmployee.fieldNames[PstEmployee.FLD_SEX], 0));
            employee.setBirthPlace(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE], ""));
            employee.setBirthDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE], "0000-00-00"), "yyyy-MM-dd"));
            employee.setReligionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID], 0));
            employee.setBloodType(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE], ""));
            employee.setAstekNum(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM], ""));
            employee.setAstekDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE], "0000-00-00"), "yyyy-MM-dd"));
            employee.setMaritalId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID], 0));
            employee.setLockerId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID], 0));
            employee.setCommencingDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE], "0000-00-00"), "yyyy-MM-dd"));
            employee.setResigned(jSONObject.optInt(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED], 0));
            employee.setResignedDate(Formater.formatDate(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE], "0000-00-00"), "yyyy-MM-dd"));
            employee.setBarcodeNumber(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER], ""));
            employee.setResignedReasonId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID], 0));
            employee.setResignedDesc(jSONObject.optString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC], ""));
            employee.setBasicSalary(jSONObject.optDouble(PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY], 0));
            employee.setIsAssignToAccounting(jSONObject.optBoolean(PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING], false));
            employee.setDivisionId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID], 0));
            employee.setLocationId(jSONObject.optLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID], 0));

        } catch (Exception e) {
        }
    }
}
