package com.dimata.aiso.entity.masterdata;

// package java

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

// package qdep
import com.dimata.util.lang.I_Language;
import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.Entity;

// package interfaces
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;

// package aiso
import com.dimata.qdep.entity.I_PersintentExc;

public class PstPerkiraan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_ChartOfAccountGroup {
    public static String[] listNames = {"Daftar Rekening > ", "Account Chart > "};
    public static final String TBL_PERKIRAAN = "aiso_perkiraan";    
    public static final int FLD_IDPERKIRAAN = 0;
    public static final int FLD_NOPERKIRAAN = 1;
    public static final int FLD_LEVEL = 2;
    public static final int FLD_NAMA = 3;
    public static final int FLD_TANDA_DEBET_KREDIT = 4;
    public static final int FLD_POSTABLE = 5;
    public static final int FLD_ID_PARENT = 6;
    public static final int FLD_DEPARTMENT_ID = 7;
    public static final int FLD_ACCOUNT_NAME_ENGLISH = 8;
    public static final int FLD_WEIGHT = 9;
    public static final int FLD_GENERAL_ACCOUNT_LINK = 10;
    public static final int FLD_ACCOUNT_GROUP = 11;
    public static final int FLD_COMPANY_ID = 12;
    public static final int FLD_ARAP_ACCOUNT = 13;
    public static final int FLD_EXPENSE_TYPE = 14;
    public static final int FLD_EXPENSE_FIXED_VARIABLE = 15;

    public static String[] fieldNames = {
        "ID_PERKIRAAN",
        "NOMOR_PERKIRAAN",
        "LEVEL",
        "NAMA",
        "TANDA_DEBET_KREDIT",
        "POSTABLE",
        "ID_PARENT",
        "DEPARTMENT_ID",
        "ACCOUNT_NAME_ENGLISH",
        "WEIGHT",
        "GENERAL_ACCOUNT_LINK",
        "ACCOUNT_GROUP",
        "COMPANY_ID",
        "ARAP_ACCOUNT",
        "EXPENSE_TYPE",
        "EXPENSE_FIXED_VARIABLE"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    public static final int ACC_NOTPOSTED = 0;
    public static final int ACC_POSTED = 1;
    public static String[][] arrStrPostable =
            {
                {
                    "Header",
                    "Postable"
                },
                {
                    "Header",
                    "Postable"
                }
            };

    public static final int ACC_DEBETSIGN = 0;
    public static final int ACC_KREDITSIGN = 1;
    public static String[][] arrStrNormalSign =
            {
                {
                    "Debet",
                    "Kredit"
                },
                {
                    "Debet",
                    "Credit"
                }
            };

    public static final int DIRECT_EXPENSE = 0;
    public static final int INDIRECT_EXPENSE = 1;
    public static final int COMMON_EXPENSE = 2;
    public static final int OTHER_OPERATING_EXP = 3;
    public static final int NON_OPERATING_EXPENSE = 4;
    
    public static String expenseType[][] = {
        {
            "Biaya Langsung","Biaya Tidak Langsung","Biaya Bersama","Biaya Operasional Lainnya","Biaya Non Operasional"
        },
        {
            "Direct Expense","Indirect Expense","Common Expense","Other Operating Expense","Non Operating Expense"
        }
    };
    
    public static final int VARIABLE_EXPENSE = 0;
    public static final int FIXED_EXPENSE = 1;
    public static final int OTHER = 3;
    
    public static String fixedVarExpense[][] = 
    {
        {
            "Biaya Variabel","Biaya Tetap","Biaya Lainnya"
        },
        {
            "Variable Expense","Fixed Expense","Other"
        }
    };
    
    private int index;  
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static Vector getExpTypeKey(int language){
        Vector vKey = new Vector();
        try{
            for(int i = 0; i < expenseType[language].length; i++){
                vKey.add(expenseType[language][i]);
            }
        }catch(Exception e){ vKey = new Vector();}
        return vKey;
    }
    
    public static Vector getExpTypeVal(int language){
        Vector vValue = new Vector();
        try{
            for(int k = 0; k < expenseType[language].length; k++){
                vValue.add(""+k);
            }
        }catch(Exception e){vValue = new Vector();}
        return vValue;
    }
    
    public static Vector getExpFVKey(int language){
        Vector vKey = new Vector();
        try{
            for(int i = 0; i < fixedVarExpense[language].length; i++){
                vKey.add(fixedVarExpense[language][i]);
            }
        }catch(Exception e){vKey = new Vector();}
        return vKey;
    }
    
    public static Vector getExpFVVal(int language){
        Vector vVal = new Vector();
        try{
            for(int k = 0; k < fixedVarExpense[language].length; k++){
                vVal.add(""+k);
            }
        }catch(Exception e){vVal = new Vector();}
        return vVal;
    }
    
    public String getAccountGroupName(int language) {
        String accountTitle = arrAccountGroupNames[language][getIndex()];
        return accountTitle;
    }

    public Vector listAccountGroup(int language) {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < arrAccountGroupNames[0].length; i++) {
            result.add(arrAccountGroupNames[language][i]);
        }
        return result;
    }

    public PstPerkiraan() {
    }

    public PstPerkiraan(int i) throws DBException {
        super(new PstPerkiraan());
    }


    public PstPerkiraan(String sOid) throws DBException {
        super(new PstPerkiraan(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstPerkiraan(long lOid) throws DBException {
        super(new PstPerkiraan(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }

        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;

    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_PERKIRAAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPerkiraan().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Perkiraan perkiraan = PstPerkiraan.fetchExc(ent.getOID());
        ent = (Entity) perkiraan;
        return perkiraan.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstPerkiraan.insertExc((Perkiraan) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Perkiraan) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Perkiraan fetchExc(long oid) throws DBException {
        try {
            Perkiraan perkiraan = new Perkiraan();
            PstPerkiraan pPerkiraan = new PstPerkiraan(oid);

            perkiraan.setOID(oid);
            perkiraan.setIdParent(pPerkiraan.getlong(FLD_ID_PARENT));
            perkiraan.setNoPerkiraan(pPerkiraan.getString(FLD_NOPERKIRAAN));
            perkiraan.setLevel(pPerkiraan.getInt(FLD_LEVEL));
            perkiraan.setNama(pPerkiraan.getString(FLD_NAMA));
            perkiraan.setTandaDebetKredit(pPerkiraan.getInt(FLD_TANDA_DEBET_KREDIT));
            perkiraan.setPostable(pPerkiraan.getInt(FLD_POSTABLE));
            perkiraan.setDepartmentId(pPerkiraan.getlong(FLD_DEPARTMENT_ID));
            perkiraan.setAccountNameEnglish(pPerkiraan.getString(FLD_ACCOUNT_NAME_ENGLISH));
            perkiraan.setWeight(pPerkiraan.getdouble(FLD_WEIGHT));
            perkiraan.setGeneralAccountLink(pPerkiraan.getlong(FLD_GENERAL_ACCOUNT_LINK));
            perkiraan.setAccountGroup(pPerkiraan.getInt(FLD_ACCOUNT_GROUP));
            perkiraan.setCompanyId(pPerkiraan.getlong(FLD_COMPANY_ID));
            perkiraan.setArapAccount(pPerkiraan.getInt(FLD_ARAP_ACCOUNT));
            perkiraan.setExpenseType(pPerkiraan.getInt(FLD_EXPENSE_TYPE));
            perkiraan.setExpenseFixedVar(pPerkiraan.getInt(FLD_EXPENSE_FIXED_VARIABLE));

            return perkiraan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerkiraan(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Perkiraan perkiraan) throws DBException {
        try {
            PstPerkiraan pPerkiraan = new PstPerkiraan(0);

            pPerkiraan.setLong(FLD_ID_PARENT, perkiraan.getIdParent());
            pPerkiraan.setString(FLD_NOPERKIRAAN, perkiraan.getNoPerkiraan());
            pPerkiraan.setInt(FLD_LEVEL, perkiraan.getLevel());
            pPerkiraan.setString(FLD_NAMA, perkiraan.getNama());
            pPerkiraan.setInt(FLD_TANDA_DEBET_KREDIT, perkiraan.getTandaDebetKredit());
            pPerkiraan.setInt(FLD_POSTABLE, perkiraan.getPostable());
            pPerkiraan.setLong(FLD_DEPARTMENT_ID, perkiraan.getDepartmentId());
            pPerkiraan.setString(FLD_ACCOUNT_NAME_ENGLISH, perkiraan.getAccountNameEnglish());
            pPerkiraan.setDouble(FLD_WEIGHT, perkiraan.getWeight());
            pPerkiraan.setLong(FLD_GENERAL_ACCOUNT_LINK, perkiraan.getGeneralAccountLink());
            pPerkiraan.setInt(FLD_ACCOUNT_GROUP, perkiraan.getAccountGroup());
            pPerkiraan.setLong(FLD_COMPANY_ID, perkiraan.getCompanyId());
            pPerkiraan.setInt(FLD_ARAP_ACCOUNT, perkiraan.getArapAccount());
            pPerkiraan.setInt(FLD_EXPENSE_TYPE, perkiraan.getExpenseType());
            pPerkiraan.setInt(FLD_EXPENSE_FIXED_VARIABLE, perkiraan.getExpenseFixedVar());
            pPerkiraan.insert();

            perkiraan.setOID(pPerkiraan.getlong(FLD_IDPERKIRAAN));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerkiraan(0), DBException.UNKNOWN);
        }
        return perkiraan.getOID();
    }

    public static long updateExc(Perkiraan perkiraan) throws DBException {
        try {
            if (perkiraan.getOID() != 0) {
                PstPerkiraan pPerkiraan = new PstPerkiraan(perkiraan.getOID());
                pPerkiraan.setLong(FLD_ID_PARENT, perkiraan.getIdParent());
                pPerkiraan.setString(FLD_NOPERKIRAAN, perkiraan.getNoPerkiraan());
                pPerkiraan.setInt(FLD_LEVEL, perkiraan.getLevel());
                pPerkiraan.setString(FLD_NAMA, perkiraan.getNama());
                pPerkiraan.setInt(FLD_TANDA_DEBET_KREDIT, perkiraan.getTandaDebetKredit());
                pPerkiraan.setInt(FLD_POSTABLE, perkiraan.getPostable());
                pPerkiraan.setLong(FLD_DEPARTMENT_ID, perkiraan.getDepartmentId());
                pPerkiraan.setString(FLD_ACCOUNT_NAME_ENGLISH, perkiraan.getAccountNameEnglish());
                pPerkiraan.setDouble(FLD_WEIGHT, perkiraan.getWeight());
                pPerkiraan.setLong(FLD_GENERAL_ACCOUNT_LINK, perkiraan.getGeneralAccountLink());
                pPerkiraan.setInt(FLD_ACCOUNT_GROUP, perkiraan.getAccountGroup());
                pPerkiraan.setLong(FLD_COMPANY_ID, perkiraan.getCompanyId());
                pPerkiraan.setInt(FLD_ARAP_ACCOUNT, perkiraan.getArapAccount());
                pPerkiraan.setInt(FLD_EXPENSE_TYPE, perkiraan.getExpenseType());
                pPerkiraan.setInt(FLD_EXPENSE_FIXED_VARIABLE, perkiraan.getExpenseFixedVar());
                pPerkiraan.update();

                return perkiraan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerkiraan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPerkiraan pPerkiraan = new PstPerkiraan(oid);
            pPerkiraan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerkiraan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PERKIRAAN;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Perkiraan perkiraan = new Perkiraan();
                resultToObject(rs, perkiraan);
                lists.add(perkiraan);
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


    /*
     * This method used to list account number
     */
    public static Vector listNoPerkiraan(int limitStart, int recordToGet, String whereClause, String order) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_NOPERKIRAAN]
                    + " FROM " + TBL_PERKIRAAN;

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector list = new Vector(recordToGet, recordToGet);
            while (rs.next()) {
                list.add(rs.getString(fieldNames[FLD_NOPERKIRAAN]));
            }
            rs.close();
            return list;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    /*
     * This method used to find current data
     */
    public static int findLimitStart(String nomorPerkiraan, int recordToGet, String whereClause) {
        String order = PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
        int max = PstPerkiraan.getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < max) && !found; i += recordToGet) {
            Vector lst = listNoPerkiraan(i, recordToGet, whereClause, order);
            start = i;
            if (lst.size() > 0) {
                for (int nr = 0; nr < lst.size(); nr++) {
                    if (nomorPerkiraan.equals((String) lst.get(nr)))
                        found = true;
                }
            }
        }

        if ((start >= max) && (start > 0))
            start = start - recordToGet;

        return start;
    }

    private static void resultToObject(ResultSet rs, Perkiraan perkiraan) {
        try {
            perkiraan.setOID(rs.getLong(fieldNames[FLD_IDPERKIRAAN]));
            perkiraan.setIdParent(rs.getLong(fieldNames[FLD_ID_PARENT]));
            perkiraan.setNoPerkiraan(rs.getString(fieldNames[FLD_NOPERKIRAAN]));
            perkiraan.setLevel(rs.getInt(fieldNames[FLD_LEVEL]));
            perkiraan.setNama(rs.getString(fieldNames[FLD_NAMA]));
            perkiraan.setTandaDebetKredit(rs.getInt(fieldNames[FLD_TANDA_DEBET_KREDIT]));
            perkiraan.setPostable(rs.getInt(fieldNames[FLD_POSTABLE]));
            perkiraan.setDepartmentId(rs.getLong(fieldNames[FLD_DEPARTMENT_ID]));
            perkiraan.setAccountNameEnglish(rs.getString(fieldNames[FLD_ACCOUNT_NAME_ENGLISH]));
            perkiraan.setWeight(rs.getDouble(fieldNames[FLD_WEIGHT]));
            perkiraan.setGeneralAccountLink(rs.getLong(fieldNames[FLD_GENERAL_ACCOUNT_LINK]));
            perkiraan.setAccountGroup(rs.getInt(fieldNames[FLD_ACCOUNT_GROUP]));
            perkiraan.setCompanyId(rs.getLong(fieldNames[FLD_COMPANY_ID]));
            perkiraan.setArapAccount(rs.getInt(fieldNames[FLD_ARAP_ACCOUNT]));
            perkiraan.setExpenseType(rs.getInt(fieldNames[FLD_EXPENSE_TYPE]));
            perkiraan.setExpenseFixedVar(rs.getInt(fieldNames[FLD_EXPENSE_FIXED_VARIABLE]));
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_IDPERKIRAAN]
                    + ") FROM " + TBL_PERKIRAAN;

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static long getAccountIdByNo(String accCode) {
        DBResultSet dbrs = null;
        long result = 0;
        String accIncome = "42.";
        if (accIncome.equals(accCode.substring(0, 3))) {
            accCode = accCode.substring(0, 5) + ".0000";
        }

        try {
            String sql = "SELECT " + fieldNames[FLD_IDPERKIRAAN]
                    + " FROM " + TBL_PERKIRAAN
                    + " WHERE " + fieldNames[FLD_NOPERKIRAAN]
                    + " = '" + accCode
                    + "'";

//            System.out.println("PstPerkiraan.getAccountIdByNo() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = rs.getLong(1);
            }

        } catch (Exception e) {
            System.out.println("PstPerkiraan.getAccountIdByNo() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static Vector getListAccount(Vector vectGroup, Vector vectNumber, Vector vectName, int start, int recordToGet, String sortBy) {
        Vector list = getListAccount(vectGroup, vectNumber, vectName, start, recordToGet, sortBy, 0);
        return list;
    }

    public static Vector getListAccount(Vector vectGroup, Vector vectNumber, Vector vectName, int start, int recordToGet, String sortBy, long department) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT DISTINCT " + fieldNames[FLD_IDPERKIRAAN]
                    + ", " + fieldNames[FLD_NOPERKIRAAN]
                    + ", " + fieldNames[FLD_DEPARTMENT_ID]
                    + ", " + fieldNames[FLD_NAMA]
                    + ", " + fieldNames[FLD_ACCOUNT_NAME_ENGLISH]
                    + ", " + fieldNames[FLD_POSTABLE]
                    + ", " + fieldNames[FLD_LEVEL]
                    + " FROM " + TBL_PERKIRAAN;

            String strGroup = "";

            if (vectGroup != null && vectGroup.size() > 0) {
                for (int i = 0; i < vectGroup.size(); i++) {
                    if(!vectGroup.get(i).toString().equalsIgnoreCase("0")){
                        if (strGroup.length() < 1) {
                            strGroup = fieldNames[FLD_ACCOUNT_GROUP] + " = " + vectGroup.get(i);
                        } else {
                            strGroup = strGroup + " OR " + fieldNames[FLD_ACCOUNT_GROUP] + " = " + vectGroup.get(i);
                        }
                        strGroup = "(" + strGroup + ")";
                    }
                }
            }else{
                strGroup = fieldNames[FLD_ACCOUNT_GROUP] + " = " +I_ChartOfAccountGroup.ACC_GROUP_EXPENSE;
            }

            String strNumber = "";
	    String sLike = "";
	    if(DBSVR_TYPE == DBSVR_POSTGRESQL){ 
		sLike = " ILIKE ";
	    }else{
		sLike = " LIKE ";
	    }
	    
            if (vectNumber != null && vectNumber.size() > 0) {
                for (int a = 0; a < vectNumber.size(); a++) {
		    if(vectNumber.get(a).toString() != null && vectNumber.get(a).toString().length() > 0){
			if (a == 0) {
			    strNumber = fieldNames[FLD_NOPERKIRAAN] +" "+sLike+"'%" + vectNumber.get(a) + "%'";
			} else {
			    strNumber = strNumber + " OR " + fieldNames[FLD_NOPERKIRAAN] +" "+sLike+"'%" + vectNumber.get(a) + "%'";
			}
			strNumber = "(" + strNumber + ")";
		    }
                }
            }

            String strName = "";

            if (vectName != null && vectName.size() > 0) {
                for (int a = 0; a < vectName.size(); a++) {
		    if(vectName.get(a).toString() != null && vectName.get(a).toString().length() > 0){
			if (a == 0) {
			    strName = fieldNames[FLD_NAMA] + " "+sLike+"'%" + vectName.get(a) + "%'";
			} else {
			    strName = strName + " OR " + fieldNames[FLD_NAMA] + " "+sLike+"'%" + vectName.get(a) + "%'";
			}
			strName = "(" + strName + ")";
		    }
                }
            }

            String strdepartment = "";
            if(department!=0){
                strdepartment = fieldNames[FLD_DEPARTMENT_ID] + "=" +department;
            }

            String allCondition = "";
            if (strGroup != "" && strGroup.length() > 0) {
                    allCondition = strGroup;
            }

            if (strNumber != "" && strNumber.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strNumber;
                } else {
                    allCondition = strNumber;
                }
            }

            if (strName != "" && strName.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strName;
                } else {
                    allCondition = strName;
                }
            }

            if (strdepartment != "" && strdepartment.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strdepartment;
                } else {
                    allCondition = strdepartment;
                }
            }

            if (allCondition != "" && allCondition.length() > 0) {
                sql = sql + " WHERE " + allCondition;
            }

            if (sortBy != "" && sortBy.length() > 0) {
                sql = sql + " ORDER BY " + sortBy;
            }

	    System.out.println("PstPerkiraan.DBSVR_TYPE ::::::::::::::::::::;; "+PstPerkiraan.DBSVR_TYPE);
            switch (PstPerkiraan.DBSVR_TYPE) {
                case PstPerkiraan.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;

                case PstPerkiraan.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

                    break;

                case PstPerkiraan.DBSVR_SYBASE:
                    break;

                case PstPerkiraan.DBSVR_ORACLE:
                    break;

                case PstPerkiraan.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }
            
            System.out.println("sql getListAccount =====> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Perkiraan perkiraan = new Perkiraan();
                perkiraan.setOID(rs.getLong(fieldNames[FLD_IDPERKIRAAN]));
                perkiraan.setDepartmentId(rs.getLong(fieldNames[FLD_DEPARTMENT_ID]));
                perkiraan.setNoPerkiraan(rs.getString(fieldNames[FLD_NOPERKIRAAN]));
                perkiraan.setNama(rs.getString(fieldNames[FLD_NAMA]));
                perkiraan.setAccountNameEnglish(rs.getString(fieldNames[FLD_ACCOUNT_NAME_ENGLISH]));
                perkiraan.setPostable(rs.getInt(fieldNames[FLD_POSTABLE]));
                perkiraan.setLevel(rs.getInt(fieldNames[FLD_LEVEL]));
                result.add(perkiraan);
            }
            
            
        } catch (Exception e) {
            System.out.println("PstPerkiraan.getListAccount() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int getCountListAccount(Vector vectGroup, Vector vectNumber, Vector vectName) {
        return getCountListAccount(vectGroup, vectNumber, vectName,0);
    }

    public static int getCountListAccount(Vector vectGroup, Vector vectNumber, Vector vectName,long departmentOid) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_IDPERKIRAAN]
                    + ") AS ACC FROM " + TBL_PERKIRAAN;

            String strGroup = "";
            if (vectGroup != null && vectGroup.size() > 0) {
                for (int i = 0; i < vectGroup.size(); i++) {
                    if(!vectGroup.get(i).toString().equalsIgnoreCase("0")){
                        if (strGroup.length() < 1) {
                            strGroup = PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + " = '" + vectGroup.get(i) + "'";
                        } else {
                            strGroup = strGroup + " OR " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + " = '" + vectGroup.get(i) + "'";
                        }

                        strGroup = "(" + strGroup + ")";
                    }
                }
            }else{
                strGroup = PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + " = "+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE;
            }
            

            String strNumber = "";
	    String sLike = "";
	    if(PstPerkiraan.DBSVR_TYPE == DBSVR_POSTGRESQL){
		sLike = " ILIKE ";    
	    }else{
		sLike = " LIKE ";  
	    }
	    
            if (vectNumber != null && vectNumber.size() > 0) {
                for (int a = 0; a < vectNumber.size(); a++) {
		    if(vectNumber.get(a).toString() != null && vectNumber.get(a).toString().length() > 0){
			if (a == 0) {
			    strNumber = PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +" "+sLike+ "'%" + vectNumber.get(a) + "%'";
			} else {
			    strNumber = strNumber + " OR " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] + " "+sLike+"'%" + vectNumber.get(a) + "%'";
			}
			 strNumber = "(" + strNumber + ")";
		    }
                }
            }

            String strName = "";
            if (vectName != null && vectName.size() > 0) {
                for (int a = 0; a < vectName.size(); a++) {
		    if(vectName.get(a).toString() != null && vectName.get(a).toString().length() > 0){
			if (a == 0) {
			    strName = PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + " "+sLike+"'%" + vectName.get(a) + "%'";
			} else {
			    strName = strName + " OR " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + " "+sLike+"'%" + vectName.get(a) + "%'";
			}
			strName = "(" + strName + ")";
		    }
                }
            }

            String strdepartment = "";
            if(departmentOid!=0){
                strdepartment = fieldNames[FLD_DEPARTMENT_ID] + "=" +departmentOid;
            }

            String allCondition = "";
	    
            if (strGroup != "" && strGroup.length() > 0) {
                allCondition = strGroup;
            }

            if (strNumber != "" && strNumber.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strNumber;
                } else {
                    allCondition = strNumber;
                }
            }

            if (strName != "" && strName.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strName;
                } else {
                    allCondition = strName;
                }
            }

            if (strdepartment != "" && strdepartment.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strdepartment;
                } else {
                    allCondition = strdepartment;
                }
            }

            if (allCondition != "" && allCondition.length() > 0) {
                sql = sql + " WHERE " + allCondition;
            }
	    
	    System.out.println("SQL PstPerkiraan.getCountListACcount() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("ACC");
            }
        } catch (Exception e) {
            System.out.println("PstPerkiraan.getCountListACcount() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static boolean isFreeAccount(long accountOid) {
        boolean result = true;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]
                    + " FROM " + PstPerkiraan.TBL_PERKIRAAN
                    + " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]
                    + " = " + accountOid;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            if (rs.next())
                result = false;
            rs.close();

        } catch (Exception error) {
            System.out.println(".:: PstPerkiraan >> isFreeAccount() : " + error.toString());
            error.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * @author rusdianta
     * This method used to get account based on parent-child association
     * algorithm is using "stack" as temporary storage to find association and sorting the result automatically
     * @param group, this param used when the value of "parentOid" is 0, group point to the type of account that want to get
     * @param parentOid, searching process will begin with this value, if the value = 0, searching will begin from the top parent of all account
     * if group = 0 and parentOid = 0, all account will be listed
     * @return Vector of Perkiraan that have been sorting based on account group and account number
     */

    public static Vector getAllAccount(int group, long parentOid) {
        Vector result = new Vector();
        try {
            Stack stack = new Stack();
            int firstLevel = 1;
            boolean firstLoop = true;
            String sql = "";
            DBResultSet dbrs = null;

            while (firstLoop || !stack.empty()) {
                if (firstLoop) {
                    firstLoop = false;

                    sql = "SELECT * FROM " + PstPerkiraan.TBL_PERKIRAAN;
                    if (parentOid == 0) {
                        sql += " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_LEVEL]
                                + " = " + firstLevel
                                + " AND " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]
                                + " = " + group;
                    } else {
                        sql += " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]
                                + " = " + parentOid;
                    }
                } else {
                    Perkiraan account = (Perkiraan) stack.pop();
                    result.add(account);
                    long accountOid = account.getOID();

                    sql = "SELECT * FROM " + PstPerkiraan.TBL_PERKIRAAN
                            + " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]
                            + " = " + accountOid;
                }

                sql += " ORDER BY " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] + " DESC";

//                System.out.println("sql : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();

                while (rs.next()) {
                    Perkiraan account = new Perkiraan();
                    account.setOID(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                    account.setIdParent(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]));
                    account.setNoPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                    account.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                    account.setLevel(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_LEVEL]));
                    account.setPostable(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_POSTABLE]));
                    account.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));
                    account.setDepartmentId(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID]));
                    account.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                    account.setWeight(rs.getDouble(PstPerkiraan.fieldNames[PstPerkiraan.FLD_WEIGHT]));
                    account.setGeneralAccountLink(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_GENERAL_ACCOUNT_LINK]));
                    account.setAccountGroup(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]));
                    account.setCompanyId(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_COMPANY_ID]));
                    account.setArapAccount(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ARAP_ACCOUNT]));
                    account.setExpenseType(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_EXPENSE_TYPE]));
                    account.setExpenseFixedVar(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_EXPENSE_FIXED_VARIABLE]));
                    stack.push(account);
                }
                rs.close();
            }
            DBResultSet.close(dbrs);
        } catch (Exception error) {
            System.out.println(".:: SessPerkiraan >> getAllAccount() >> " + error.toString());
            error.printStackTrace();
        }
        return result;
    }
    
    public static Vector getAccountByGroup(int iAccGroup){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = "SELECT * FROM "+TBL_PERKIRAAN+
                    " WHERE "+fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+" = "+iAccGroup+
                    " AND "+fieldNames[PstPerkiraan.FLD_POSTABLE]+" = "+ACC_POSTED;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Perkiraan objPerkiraan = new Perkiraan();
                resultToObject(rs, objPerkiraan);
                vResult.add(objPerkiraan);
            }
                    
        }catch(Exception e){vResult = new Vector(1,1);}
        return vResult;
    }
    
    public static Vector findFieldPerkiraan(long oid) {
        DBResultSet dbrs = null;
        Vector lists = new Vector(1, 1);
        try {
            String sql = "SELECT " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + ", " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] + ", " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + ", " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] + ", " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_POSTABLE] + ", " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " FROM " + PstPerkiraan.TBL_PERKIRAAN +
                    " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Perkiraan perkiraan = new Perkiraan();

                perkiraan.setOID(rs.getLong(1));
                perkiraan.setNoPerkiraan(rs.getString(2));
                perkiraan.setNama(rs.getString(3));
                perkiraan.setAccountNameEnglish(rs.getString(4));
                perkiraan.setPostable(rs.getInt(5));
                perkiraan.setAccountGroup(rs.getInt(6));

                lists.add(perkiraan);
            }
        } catch (Exception e) {
            return new Vector();
        } finally {
            DBResultSet.close(dbrs);

        }
        return lists;
    }

    public static Vector findAccountLevelPostable(long oid) {
        DBResultSet dbrs = null;
        Vector lists = new Vector(1, 1);
        try {
            String sql = "SELECT " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_LEVEL] +
                    " FROM " + PstPerkiraan.TBL_PERKIRAAN +
                    " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Perkiraan perkiraan = new Perkiraan();
                perkiraan.setLevel(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_LEVEL]));
                lists.add(perkiraan);
            }
        } catch (Exception e) {
            return new Vector();
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static boolean checkAccountlevelPostable(long parentId, int level) {
        boolean result = false;
        if (parentId != 0) {
            Vector vectReference = findAccountLevelPostable(parentId);
            if (vectReference != null && vectReference.size() > 0) {
                Perkiraan refAccount = (Perkiraan) vectReference.get(0);
                if (level - refAccount.getLevel() == 1) {
                    result = true;
                }
            }
        } else {
            if (level == 1) {
                result = true;
            }
        }
        return result;
    }

    /**
     * this method used to get account name
     */
    public static String getAccountName(long accountId) {
        DBResultSet dbrs = null;
        String result = "";
        try {
            String sql = "SELECT " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    " FROM " + PstPerkiraan.TBL_PERKIRAAN +
                    " WHERE " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = " + accountId;


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]);
            }
        } catch (Exception e) {
            System.out.println("SessPerkiraan.getAccountName err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Hashtable getListCoA(int language) {
        Hashtable hListCoA = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PERKIRAAN;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Perkiraan objCoA = new Perkiraan();
                resultToObject(rs, objCoA);
                String accName = "";
                if(language == I_Language.LANGUAGE_DEFAULT){
                    accName = objCoA.getNama();
                }else{
                    accName = objCoA.getAccountNameEnglish();
                }
                hListCoA.put(accName.toUpperCase(),objCoA);
            }
            rs.close();
            return hListCoA;

        } catch (Exception e) {
            System.out.println("Exception on getListCoA() :::: "+e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }   
    
     public static Hashtable getListAccAssets(int language){
        Hashtable hListAccAssets = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PERKIRAAN +" WHERE "+FLD_ACCOUNT_GROUP+" IN("+
                        I_ChartOfAccountGroup.ACC_GROUP_FIXED_ASSETS+", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+
                        I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+", "+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+")";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Perkiraan objAccAssets = new Perkiraan();
                resultToObject(rs, objAccAssets);
                String strAccName = objAccAssets.getNama();
                if(language == I_Language.LANGUAGE_FOREIGN)
                    strAccName = objAccAssets.getAccountNameEnglish();
                hListAccAssets.put(strAccName.toUpperCase(),objAccAssets);
            }
            rs.close();
            return hListAccAssets;

        } catch (Exception e) {
            System.out.println("Exception on getListGroupAssets() :::: "+e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }
    
}
