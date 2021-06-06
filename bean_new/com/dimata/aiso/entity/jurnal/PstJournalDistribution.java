/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.jurnal;

import com.dimata.aiso.db.DBException;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.I_DBInterface;
import com.dimata.aiso.db.I_DBType;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.masterdata.Perkiraan;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class PstJournalDistribution extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{
    
    public static final String TBL_JOURNAL_DISTRIBUTION = "aiso_journal_distribution";
      
    public static final int FLD_JOURNAL_DISTRIBUTION_ID = 0;
    public static final int FLD_BUSS_CENTER_ID = 1;
    public static final int FLD_JOURNAL_DETAIL_ID = 2;
    public static final int FLD_ID_PERKIRAAN = 3;
    public static final int FLD_PERIODE_ID = 4;
    public static final int FLD_DEBIT_AMOUNT = 5;
    public static final int FLD_CREDIT_AMOUNT = 6;
    public static final int FLD_NOTE = 7;
    public static final int FLD_CURRENCY_ID = 8;
    public static final int FLD_TRANS_RATE = 9;
    public static final int FLD_STANDARD_RATE = 10;
    public static final int FLD_ARAP_MAIN_ID = 11;
    public static final int FLD_ARAP_PAYMENT_ID = 12;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG
    };
    
    public static String[] fieldNames = {
        "JOURNAL_DISTRIBUTION_ID",
        "BUSS_CENTER_ID",
        "JOURNAL_DETAIL_ID",
        "ID_PERKIRAAN",
        "PERIODE_ID",
        "DEBIT_AMOUNT",
        "CREDIT_AMOUNT",
        "NOTE",
        "CURRENCY_ID",
        "TRANS_RATE",
        "STANDARD_RATE",
        "ARAP_MAIN_ID",
        "PAYMENT_ID"
    };
    
     public PstJournalDistribution() {
    }

    public PstJournalDistribution(int i) throws DBException {
        super(new PstJournalDistribution());
    }


    public PstJournalDistribution(String sOid) throws DBException {
        super(new PstJournalDistribution(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstJournalDistribution(long lOid) throws DBException {
        super(new PstJournalDistribution(0));
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
        return TBL_JOURNAL_DISTRIBUTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstJournalDistribution().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        JournalDistribution objJournalDistribution = PstJournalDistribution.fetchExc(ent.getOID());
        ent = (Entity) objJournalDistribution;
        return objJournalDistribution.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstJournalDistribution.insertExc((JournalDistribution) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((JournalDistribution) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static void deleteByJurnalDetailId(long jurnalDetailId)
            throws DBException {
        try {
            String sql="DELETE FROM "+ TBL_JOURNAL_DISTRIBUTION+ " WHERE "+fieldNames[FLD_JOURNAL_DETAIL_ID]+"="+ jurnalDetailId ;
            DBHandler.execUpdate(sql);
        } catch (Exception sqlexception) {
            sqlexception.printStackTrace(System.err);           
        } finally {
            
        }
    }
    
    
    

    public static JournalDistribution fetchExc(long oid) throws DBException {
        try {
            JournalDistribution objJournalDistribution = new JournalDistribution();
            PstJournalDistribution pJournalDistribution = new PstJournalDistribution(oid);

            objJournalDistribution.setOID(oid);
            objJournalDistribution.setBussCenterId(pJournalDistribution.getlong(FLD_BUSS_CENTER_ID));
            objJournalDistribution.setJournalDetailId(pJournalDistribution.getlong(FLD_JOURNAL_DETAIL_ID));
            objJournalDistribution.setIdPerkiraan(pJournalDistribution.getlong(FLD_ID_PERKIRAAN));
            objJournalDistribution.setPeriodeId(pJournalDistribution.getlong(FLD_PERIODE_ID));
            objJournalDistribution.setDebitAmount(pJournalDistribution.getdouble(FLD_DEBIT_AMOUNT));
            objJournalDistribution.setCreditAmount(pJournalDistribution.getdouble(FLD_CREDIT_AMOUNT));
            objJournalDistribution.setNote(pJournalDistribution.getString(FLD_NOTE));
            objJournalDistribution.setCurrencyId(pJournalDistribution.getlong(FLD_CURRENCY_ID));
            objJournalDistribution.setTransRate(pJournalDistribution.getdouble(FLD_TRANS_RATE));
            objJournalDistribution.setStandardRate(pJournalDistribution.getdouble(FLD_STANDARD_RATE));
            objJournalDistribution.setArapMainId(pJournalDistribution.getlong(FLD_ARAP_MAIN_ID));
            objJournalDistribution.setArapPaymentId(pJournalDistribution.getlong(FLD_ARAP_PAYMENT_ID));
            
            return objJournalDistribution;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJournalDistribution(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(JournalDistribution objJournalDistribution) throws DBException {
        try {
            PstJournalDistribution pJournalDistribution = new PstJournalDistribution(0);

            pJournalDistribution.setLong(FLD_BUSS_CENTER_ID, objJournalDistribution.getBussCenterId());
            pJournalDistribution.setLong(FLD_JOURNAL_DETAIL_ID, objJournalDistribution.getJournalDetailId());
            pJournalDistribution.setLong(FLD_ID_PERKIRAAN, objJournalDistribution.getIdPerkiraan());
            pJournalDistribution.setLong(FLD_PERIODE_ID, objJournalDistribution.getPeriodeId());
            pJournalDistribution.setDouble(FLD_DEBIT_AMOUNT, objJournalDistribution.getDebitAmount());
            pJournalDistribution.setDouble(FLD_CREDIT_AMOUNT, objJournalDistribution.getCreditAmount());
            pJournalDistribution.setString(FLD_NOTE, objJournalDistribution.getNote());
            pJournalDistribution.setLong(FLD_CURRENCY_ID, objJournalDistribution.getCurrencyId());
            pJournalDistribution.setDouble(FLD_TRANS_RATE, objJournalDistribution.getTransRate());
            pJournalDistribution.setDouble(FLD_STANDARD_RATE, objJournalDistribution.getStandardRate());
            pJournalDistribution.setLong(FLD_ARAP_MAIN_ID, objJournalDistribution.getArapMainId());
            pJournalDistribution.setLong(FLD_ARAP_PAYMENT_ID, objJournalDistribution.getArapPaymentId());
            pJournalDistribution.insert();

            objJournalDistribution.setOID(pJournalDistribution.getlong(FLD_JOURNAL_DISTRIBUTION_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJournalDistribution(0), DBException.UNKNOWN);
        }
        return objJournalDistribution.getOID();
    }

    public static long updateExc(JournalDistribution objJournalDistribution) throws DBException {
        try {
            if (objJournalDistribution.getOID() != 0) {
                PstJournalDistribution pJournalDistribution = new PstJournalDistribution(objJournalDistribution.getOID());
                
                pJournalDistribution.setLong(FLD_BUSS_CENTER_ID, objJournalDistribution.getBussCenterId());
                pJournalDistribution.setLong(FLD_JOURNAL_DETAIL_ID, objJournalDistribution.getJournalDetailId());
                pJournalDistribution.setLong(FLD_ID_PERKIRAAN, objJournalDistribution.getIdPerkiraan());
                pJournalDistribution.setLong(FLD_PERIODE_ID, objJournalDistribution.getPeriodeId());
                pJournalDistribution.setDouble(FLD_DEBIT_AMOUNT, objJournalDistribution.getDebitAmount());
                pJournalDistribution.setDouble(FLD_CREDIT_AMOUNT, objJournalDistribution.getCreditAmount());
                pJournalDistribution.setString(FLD_NOTE, objJournalDistribution.getNote());
                pJournalDistribution.setLong(FLD_CURRENCY_ID, objJournalDistribution.getCurrencyId());
                pJournalDistribution.setDouble(FLD_TRANS_RATE, objJournalDistribution.getTransRate());
                pJournalDistribution.setDouble(FLD_STANDARD_RATE, objJournalDistribution.getStandardRate());
                pJournalDistribution.setLong(FLD_ARAP_MAIN_ID, objJournalDistribution.getArapMainId());
                pJournalDistribution.setLong(FLD_ARAP_PAYMENT_ID, objJournalDistribution.getArapPaymentId());
                
                pJournalDistribution.update();

                return objJournalDistribution.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJournalDistribution(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstJournalDistribution pJournalDistribution = new PstJournalDistribution(oid);
            pJournalDistribution.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJournalDistribution(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_JOURNAL_DISTRIBUTION;
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
            
            System.out.println("SQL PstJournalDistribution.list(limitStart, recordToGet, whereClause, order) :::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int idx = 0;
            while (rs.next()) {
                JournalDistribution objJournalDistribution = new JournalDistribution();
                resultToObject(rs, objJournalDistribution);
                try{
                    Perkiraan acc = PstPerkiraan.fetchExc(objJournalDistribution.getIdPerkiraan());
                    objJournalDistribution.setCoaCode(acc.getNoPerkiraan());
                    objJournalDistribution.setCoaName(acc.getNama());
                    objJournalDistribution.setCoaNameEnglish(acc.getAccountNameEnglish());
                }catch(Exception exc){
                    System.out.println("EXC : PstPerkiraan.fetchExc"+exc);
                }
                objJournalDistribution.setIndex(idx);
                lists.add(objJournalDistribution);
                idx++;
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

    private static void resultToObject(ResultSet rs, JournalDistribution objJournalDistribution) {
        try {
            objJournalDistribution.setOID(rs.getLong(fieldNames[FLD_JOURNAL_DISTRIBUTION_ID]));
            objJournalDistribution.setBussCenterId(rs.getLong(fieldNames[FLD_BUSS_CENTER_ID]));
            objJournalDistribution.setJournalDetailId(rs.getLong(fieldNames[FLD_JOURNAL_DETAIL_ID]));
            objJournalDistribution.setIdPerkiraan(rs.getLong(fieldNames[FLD_ID_PERKIRAAN]));
            objJournalDistribution.setPeriodeId(rs.getLong(fieldNames[FLD_PERIODE_ID]));
            objJournalDistribution.setDebitAmount(rs.getDouble(fieldNames[FLD_DEBIT_AMOUNT]));
            objJournalDistribution.setCreditAmount(rs.getDouble(fieldNames[FLD_CREDIT_AMOUNT]));
            objJournalDistribution.setNote(rs.getString(fieldNames[FLD_NOTE]));
            objJournalDistribution.setCurrencyId(rs.getLong(fieldNames[FLD_CURRENCY_ID]));
            objJournalDistribution.setTransRate(rs.getLong(fieldNames[FLD_TRANS_RATE]));
            objJournalDistribution.setStandardRate(rs.getLong(fieldNames[FLD_STANDARD_RATE]));
            objJournalDistribution.setArapMainId(rs.getLong(fieldNames[FLD_ARAP_MAIN_ID]));
            objJournalDistribution.setArapPaymentId(rs.getLong(fieldNames[FLD_ARAP_PAYMENT_ID]));
            
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }

     public boolean isDetailItemClear(Vector vJDistribution) {
        if (vJDistribution != null && vJDistribution.size() > 0) {
            for (int i = 0; i < vJDistribution.size(); i++) {
                JournalDistribution jDistribution = (JournalDistribution) vJDistribution.get(i);
                if (jDistribution.getDataStatus() != PstJurnalDetail.DATASTATUS_CLEAN) {
                    return false;
                }
            }
        }
        return true;
    }

}
