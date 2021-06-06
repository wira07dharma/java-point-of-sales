/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata;

import com.dimata.aiso.db.DBException;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.I_DBInterface;
import com.dimata.aiso.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class PstBussCenterBudget extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_BUSSINESS_CENTER_BUDGET = "aiso_buss_center_budget";
    public static final int FLD_BUSS_CENTER_BUDGET_ID = 0;
    public static final int FLD_ID_PERKIRAAN = 1;
    public static final int FLD_PERIODE_ID = 2;
    public static final int FLD_BUDGET = 3;
    public static final int FLD_DEBIT_FOREIGN_STD_RATE = 4;
    public static final int FLD_DEBIT_FOREIGN_TRANS_RATE = 5;
    public static final int FLD_DEBIT_LOCAL_STD_RATE = 6;
    public static final int FLD_DEBIT_LOCAL_TRANS_RATE = 7;
    public static final int FLD_CREDIT_FOREIGN_STD_RATE = 8;
    public static final int FLD_CREDIT_FOREIGN_TRANS_RATE = 9;
    public static final int FLD_CREDIT_LOCAL_STD_RATE = 10;
    public static final int FLD_CREDIT_LOCAL_TRANS_RATE = 11;
    public static final int FLD_BUSS_CENTER_ID = 12;
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG
    };
    public static String[] fieldNames = {
        "BUSS_CENTER_BUDGET_ID",
        "ID_PERKIRAAN",
        "PERIODE_ID",
        "BUDGET",
        "DEBIT_FOREIGN_STD_RATE",
        "DEBIT_FOREIGN_TRANS_RATE",
        "DEBIT_LOCAL_STD_RATE",
        "DEBIT_LOCAL_TRANS_RATE",
        "CREDIT_FOREIGN_STD_RATE",
        "CREDIT_FOREIGN_TRANS_RATE",
        "CREDIT_LOCAL_STD_RATE",
        "CREDIT_LOCAL_TRANS_RATE",
        "BUSS_CENTER_ID"
    };

    public PstBussCenterBudget() {
    }

    public PstBussCenterBudget(int i) throws DBException {
        super(new PstBussCenterBudget());
    }

    public PstBussCenterBudget(String sOid) throws DBException {
        super(new PstBussCenterBudget(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstBussCenterBudget(long lOid) throws DBException {
        super(new PstBussCenterBudget(0));
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
        return TBL_BUSSINESS_CENTER_BUDGET;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstBussCenterBudget().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        BussinessCenterBudget objBussinessCenterBudget = PstBussCenterBudget.fetchExc(ent.getOID());
        ent = (Entity) objBussinessCenterBudget;
        return objBussinessCenterBudget.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstBussCenterBudget.insertExc((BussinessCenterBudget) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((BussinessCenter) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static BussinessCenterBudget fetchExc(long oid) throws DBException {
        try {
            BussinessCenterBudget objBussinessCenterBudget = new BussinessCenterBudget();
            PstBussCenterBudget pBussCenterBudget = new PstBussCenterBudget(oid);

            objBussinessCenterBudget.setOID(oid);
            objBussinessCenterBudget.setIdPerkiraan(pBussCenterBudget.getlong(FLD_ID_PERKIRAAN));
            objBussinessCenterBudget.setPeriodeId(pBussCenterBudget.getlong(FLD_PERIODE_ID));
            objBussinessCenterBudget.setBudget(pBussCenterBudget.getdouble(FLD_BUDGET));
            objBussinessCenterBudget.setDebitForeignStdRate(pBussCenterBudget.getdouble(FLD_DEBIT_FOREIGN_STD_RATE));
            objBussinessCenterBudget.setDebitForeignTransRate(pBussCenterBudget.getdouble(FLD_DEBIT_FOREIGN_TRANS_RATE));
            objBussinessCenterBudget.setDebitLocalStdRate(pBussCenterBudget.getdouble(FLD_DEBIT_LOCAL_STD_RATE));
            objBussinessCenterBudget.setDebitLocalTransRate(pBussCenterBudget.getdouble(FLD_DEBIT_LOCAL_TRANS_RATE));
            objBussinessCenterBudget.setCreditForeignStdRate(pBussCenterBudget.getdouble(FLD_CREDIT_FOREIGN_STD_RATE));
            objBussinessCenterBudget.setCreditForeignTransRate(pBussCenterBudget.getdouble(FLD_CREDIT_FOREIGN_TRANS_RATE));
            objBussinessCenterBudget.setCreditLocalStdRate(pBussCenterBudget.getdouble(FLD_CREDIT_LOCAL_STD_RATE));
            objBussinessCenterBudget.setCreditLocalTransRate(pBussCenterBudget.getdouble(FLD_CREDIT_LOCAL_TRANS_RATE));
            objBussinessCenterBudget.setBussCenterId(pBussCenterBudget.getlong(FLD_BUSS_CENTER_ID));

            return objBussinessCenterBudget;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussCenterBudget(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(BussinessCenterBudget objBussinessCenterBudget) throws DBException {
        try {
            PstBussCenterBudget pBussCenterBudget = new PstBussCenterBudget(0);

            pBussCenterBudget.setLong(FLD_ID_PERKIRAAN, objBussinessCenterBudget.getIdPerkiraan());
            pBussCenterBudget.setLong(FLD_PERIODE_ID, objBussinessCenterBudget.getPeriodeId());
            pBussCenterBudget.setDouble(FLD_BUDGET, objBussinessCenterBudget.getBudget());
            pBussCenterBudget.setDouble(FLD_DEBIT_FOREIGN_STD_RATE, objBussinessCenterBudget.getDebitForeignStdRate());
            pBussCenterBudget.setDouble(FLD_DEBIT_FOREIGN_TRANS_RATE, objBussinessCenterBudget.getDebitForeignTransRate());
            pBussCenterBudget.setDouble(FLD_DEBIT_LOCAL_STD_RATE, objBussinessCenterBudget.getDebitLocalStdRate());
            pBussCenterBudget.setDouble(FLD_DEBIT_LOCAL_TRANS_RATE, objBussinessCenterBudget.getDebitLocalTransRate());
            pBussCenterBudget.setDouble(FLD_CREDIT_FOREIGN_STD_RATE, objBussinessCenterBudget.getCreditForeignStdRate());
            pBussCenterBudget.setDouble(FLD_CREDIT_FOREIGN_TRANS_RATE, objBussinessCenterBudget.getCreditForeignTransRate());
            pBussCenterBudget.setDouble(FLD_CREDIT_LOCAL_STD_RATE, objBussinessCenterBudget.getCreditLocalStdRate());
            pBussCenterBudget.setDouble(FLD_CREDIT_LOCAL_TRANS_RATE, objBussinessCenterBudget.getCreditLocalTransRate());
            pBussCenterBudget.setLong(FLD_BUSS_CENTER_ID, objBussinessCenterBudget.getBussCenterId());

            pBussCenterBudget.insert();

            objBussinessCenterBudget.setOID(pBussCenterBudget.getlong(FLD_BUSS_CENTER_BUDGET_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussCenterBudget(0), DBException.UNKNOWN);
        }
        return objBussinessCenterBudget.getOID();
    }

    public static long updateExc(BussinessCenterBudget objBussinessCenterBudget) throws DBException {
        try {
            if (objBussinessCenterBudget.getOID() != 0) {
                PstBussCenterBudget pBussCenterBudget = new PstBussCenterBudget(objBussinessCenterBudget.getOID());

                pBussCenterBudget.setLong(FLD_ID_PERKIRAAN, objBussinessCenterBudget.getIdPerkiraan());
                pBussCenterBudget.setLong(FLD_PERIODE_ID, objBussinessCenterBudget.getPeriodeId());
                pBussCenterBudget.setDouble(FLD_BUDGET, objBussinessCenterBudget.getBudget());
                pBussCenterBudget.setDouble(FLD_DEBIT_FOREIGN_STD_RATE, objBussinessCenterBudget.getDebitForeignStdRate());
                pBussCenterBudget.setDouble(FLD_DEBIT_FOREIGN_TRANS_RATE, objBussinessCenterBudget.getDebitForeignTransRate());
                pBussCenterBudget.setDouble(FLD_DEBIT_LOCAL_STD_RATE, objBussinessCenterBudget.getDebitLocalStdRate());
                pBussCenterBudget.setDouble(FLD_DEBIT_LOCAL_TRANS_RATE, objBussinessCenterBudget.getDebitLocalTransRate());
                pBussCenterBudget.setDouble(FLD_CREDIT_FOREIGN_STD_RATE, objBussinessCenterBudget.getCreditForeignStdRate());
                pBussCenterBudget.setDouble(FLD_CREDIT_FOREIGN_TRANS_RATE, objBussinessCenterBudget.getCreditForeignTransRate());
                pBussCenterBudget.setDouble(FLD_CREDIT_LOCAL_STD_RATE, objBussinessCenterBudget.getCreditLocalStdRate());
                pBussCenterBudget.setDouble(FLD_CREDIT_LOCAL_TRANS_RATE, objBussinessCenterBudget.getCreditLocalTransRate());
                pBussCenterBudget.setLong(FLD_BUSS_CENTER_ID, objBussinessCenterBudget.getBussCenterId());

                pBussCenterBudget.update();

                return objBussinessCenterBudget.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussCenterBudget(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstBussCenterBudget pBussinessCenter = new PstBussCenterBudget(oid);
            pBussinessCenter.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussCenterBudget(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_BUSSINESS_CENTER_BUDGET;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }

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
                BussinessCenterBudget objBussinessCenterBudget = new BussinessCenterBudget();
                resultToObject(rs, objBussinessCenterBudget);
                lists.add(objBussinessCenterBudget);
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

    public static Vector listByAccountNr(long bussCenterId, long periodId, String accNr[]) {
        Vector lists = new Vector(1, 1);
        if (accNr != null && accNr.length > 0) {
            String whr = "";
            for (int i = 0; i < accNr.length; i++) {
                whr = whr + "'" + accNr[i] + "',";
            }
            whr = whr.substring(0, whr.length() - 1);

            String sql = "select a.id_perkiraan, a.nomor_perkiraan, a.nama, b.* from aiso_perkiraan a " +
                    " inner join aiso_buss_center_budget b on a.id_perkiraan=b.id_perkiraan " +
                    "where b.periode_id='" + periodId + "' and b.buss_center_id='" + bussCenterId + "' and  a.nomor_perkiraan in" +
                    " (" + whr + ")" + " ORDER BY " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] + " DESC";
            DBResultSet dbrs = null;
            try {
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();

                while (rs.next()) {
                    BussinessCenterBudget objBussinessCenterBudget = new BussinessCenterBudget();
                    resultToObject(rs, objBussinessCenterBudget);
                    lists.add(objBussinessCenterBudget);
                }
                rs.close();
                return lists;
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                DBResultSet.close(dbrs);
            }
        }
        return lists;
    }

    public static Vector listByAccountGroup(long bussCenterId, long periodId, int iAccountGroup) {
        Vector lists = new Vector(1, 1);
        if (bussCenterId !=0 && periodId!=0) {
            String sql = "select a.id_perkiraan, a.LEVEL, a.postable, a.nomor_perkiraan, a.nama, a.ACCOUNT_NAME_ENGLISH, b.* from aiso_perkiraan a " +
                    " left join aiso_buss_center_budget b on a.id_perkiraan=b.id_perkiraan and " +
                    " b.periode_id='" + periodId + "' and b.buss_center_id='" + bussCenterId + "' " +
                    "where " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "="+iAccountGroup +
                     " ORDER BY " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            DBResultSet dbrs = null;
            try {
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();

                while (rs.next()) {
                    BussinessCenterBudget objBussinessCenterBudget = new BussinessCenterBudget();
                    resultToObject(rs, objBussinessCenterBudget);
                    Perkiraan per = objBussinessCenterBudget.getPerkiraan();
                    try{
                        per.setOID(rs.getLong("id_perkiraan"));
                        per.setNoPerkiraan(rs.getString("nomor_perkiraan"));
                        per.setNama(rs.getString("nama"));
                        per.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                        per.setLevel(rs.getInt("LEVEL"));
                        per.setPostable(rs.getInt("postable"));
                    } catch(Exception exc){                        
                    }
                    lists.add(objBussinessCenterBudget);
                }
                rs.close();
                return lists;
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                DBResultSet.close(dbrs);
            }
        }
        return lists;
    }
    
    public static double sumBussCenterBudget(long bussCenterId, long periodId, String[] accNr) {
        double totalBudget=0;

        if (accNr != null && accNr.length > 0) {
            String whr = "";
            for (int i = 0; i < accNr.length; i++) {
                whr = whr + "'" + accNr[i] + "',";
            }
            whr = whr.substring(0, whr.length() - 1);

            String sql = "select sum(b."+fieldNames[FLD_BUDGET]+") as total_budget from aiso_perkiraan a " +
                    " inner join aiso_buss_center_budget b on a.id_perkiraan=b.id_perkiraan " +
                    "where b.periode_id='" + periodId + "' and b.buss_center_id='" + bussCenterId + "' and  a.nomor_perkiraan in" +
                    " (" + whr + ")";
            DBResultSet dbrs = null;
            try {
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    totalBudget = rs.getDouble("total_budget");
                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                DBResultSet.close(dbrs);
            }
        }
        return totalBudget;
    }
    
    
    private static void resultToObject(ResultSet rs, BussinessCenterBudget objBussinessCenterBudget) {
        try {
            objBussinessCenterBudget.setOID(rs.getLong(fieldNames[FLD_BUSS_CENTER_BUDGET_ID]));
            objBussinessCenterBudget.setIdPerkiraan(rs.getLong(fieldNames[FLD_ID_PERKIRAAN]));
            objBussinessCenterBudget.setPeriodeId(rs.getLong(fieldNames[FLD_PERIODE_ID]));
            objBussinessCenterBudget.setBudget(rs.getDouble(fieldNames[FLD_BUDGET]));
            objBussinessCenterBudget.setDebitForeignStdRate(rs.getDouble(fieldNames[FLD_DEBIT_FOREIGN_STD_RATE]));
            objBussinessCenterBudget.setDebitForeignTransRate(rs.getDouble(fieldNames[FLD_DEBIT_FOREIGN_TRANS_RATE]));
            objBussinessCenterBudget.setDebitLocalStdRate(rs.getDouble(fieldNames[FLD_DEBIT_LOCAL_STD_RATE]));
            objBussinessCenterBudget.setDebitLocalTransRate(rs.getDouble(fieldNames[FLD_DEBIT_LOCAL_TRANS_RATE]));
            objBussinessCenterBudget.setCreditForeignStdRate(rs.getDouble(fieldNames[FLD_CREDIT_FOREIGN_STD_RATE]));
            objBussinessCenterBudget.setCreditForeignTransRate(rs.getDouble(fieldNames[FLD_CREDIT_FOREIGN_TRANS_RATE]));
            objBussinessCenterBudget.setCreditLocalStdRate(rs.getDouble(fieldNames[FLD_CREDIT_LOCAL_STD_RATE]));
            objBussinessCenterBudget.setCreditLocalTransRate(rs.getDouble(fieldNames[FLD_CREDIT_LOCAL_TRANS_RATE]));
            objBussinessCenterBudget.setBussCenterId(rs.getLong(fieldNames[FLD_BUSS_CENTER_ID]));

        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }
}
