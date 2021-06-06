/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.report;

import com.dimata.aiso.entity.masterdata.*;
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
public class PstReportOwnerDistribution extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_REP_OWN_DISTRB = "aiso_report_owners_distrib";
   

    public static final int FLD_REP_OWN_DISTRIB_ID = 0;
    public static final int FLD_PERIOD_ID = 1;
    public static final int FLD_ADV_PAY_BALANCE = 2;
    public static final int FLD_BUSS_CENTER_ID = 3;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_LONG,       
        TYPE_FLOAT,
        TYPE_LONG
    };
    
    public static String[] fieldNames = {
        "rprt_own_ditribution_id",
        "periode_id",
        "advance_pay_balance",
        "buss_center_id"
    };
    
     public PstReportOwnerDistribution() {
    }

    public PstReportOwnerDistribution(int i) throws DBException {
        super(new PstReportOwnerDistribution());
    }


    public PstReportOwnerDistribution(String sOid) throws DBException {
        super(new PstReportOwnerDistribution(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstReportOwnerDistribution(long lOid) throws DBException {
        super(new PstReportOwnerDistribution(0));
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
        return TBL_REP_OWN_DISTRB;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstReportOwnerDistribution().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        ReportOwnerDistribution reportOwnerDistribution = PstReportOwnerDistribution.fetchExc(ent.getOID());
        ent = (Entity) reportOwnerDistribution;
        return reportOwnerDistribution.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstReportOwnerDistribution.insertExc((ReportOwnerDistribution) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((ReportOwnerDistribution) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ReportOwnerDistribution fetchExc(long oid) throws DBException {
        try {
            ReportOwnerDistribution reportOwnerDistribution = new ReportOwnerDistribution();
            PstReportOwnerDistribution pReportOwnDistr = new PstReportOwnerDistribution(oid);

            reportOwnerDistribution.setOID(oid);
            reportOwnerDistribution.setAdvance_pay_balance(pReportOwnDistr.getfloat(FLD_ADV_PAY_BALANCE));
            reportOwnerDistribution.setPeriod_id(pReportOwnDistr.getlong(FLD_PERIOD_ID));            
            reportOwnerDistribution.setBuss_center_id(pReportOwnDistr.getlong(FLD_BUSS_CENTER_ID));
            
            return reportOwnerDistribution;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportOwnerDistribution(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ReportOwnerDistribution reportOwnerDistribution) throws DBException {
        try {
            PstReportOwnerDistribution pReportOwnDistr = new PstReportOwnerDistribution(0);

            pReportOwnDistr.setFloat(FLD_ADV_PAY_BALANCE, reportOwnerDistribution.getAdvance_pay_balance());
            pReportOwnDistr.setLong(FLD_PERIOD_ID, reportOwnerDistribution.getPeriod_id());
            pReportOwnDistr.setLong(FLD_BUSS_CENTER_ID, reportOwnerDistribution.getBuss_center_id());
            pReportOwnDistr.insert();

            reportOwnerDistribution.setOID(pReportOwnDistr.getlong(FLD_REP_OWN_DISTRIB_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportOwnerDistribution(0), DBException.UNKNOWN);
        }
        return reportOwnerDistribution.getOID();
    }

    public static long updateExc(ReportOwnerDistribution reportOwnerDistribution) throws DBException {
        try {
            if (reportOwnerDistribution.getOID() != 0) {
                PstReportOwnerDistribution pReportOwnDistr = new PstReportOwnerDistribution(reportOwnerDistribution.getOID());
                
                pReportOwnDistr.setFloat(FLD_ADV_PAY_BALANCE, reportOwnerDistribution.getAdvance_pay_balance());
                pReportOwnDistr.setLong(FLD_PERIOD_ID, reportOwnerDistribution.getPeriod_id());                
                pReportOwnDistr.setLong(FLD_BUSS_CENTER_ID, reportOwnerDistribution.getBuss_center_id());
                pReportOwnDistr.update();

                return reportOwnerDistribution.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportOwnerDistribution(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstReportOwnerDistribution pBussinessCenter = new PstReportOwnerDistribution(oid);
            pBussinessCenter.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportOwnerDistribution(0), DBException.UNKNOWN);
        }
        return oid;
    }       
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_REP_OWN_DISTRB;
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
                ReportOwnerDistribution reportOwnerDistribution = new ReportOwnerDistribution();
                resultToObject(rs, reportOwnerDistribution);
                lists.add(reportOwnerDistribution);
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

    public static ReportOwnerDistribution fetchByPeriodId(long periodID, long bussCenterId) {
        int limitStart=0;
        int recordToGet=1;
        String whereClause=" "+fieldNames[FLD_PERIOD_ID]+"='"+(new Long(periodID)).toString() +"' "+
                " AND "+fieldNames[FLD_BUSS_CENTER_ID]+"='"+(new Long(bussCenterId)).toString() +"' ";
        String order="";        
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_REP_OWN_DISTRB;
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
                ReportOwnerDistribution reportOwnerDistribution = new ReportOwnerDistribution();
                resultToObject(rs, reportOwnerDistribution);
                return reportOwnerDistribution;
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }
    
    
    private static void resultToObject(ResultSet rs, ReportOwnerDistribution reportOwnerDistribution) {
        try {
            reportOwnerDistribution.setOID(rs.getLong(fieldNames[FLD_REP_OWN_DISTRIB_ID]));
            reportOwnerDistribution.setAdvance_pay_balance(rs.getDouble(fieldNames[FLD_ADV_PAY_BALANCE]));
            reportOwnerDistribution.setPeriod_id(rs.getLong(fieldNames[FLD_PERIOD_ID]));            
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }

}
