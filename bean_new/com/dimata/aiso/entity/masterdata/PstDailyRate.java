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
public class PstDailyRate extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{

    public static final String TBL_DAILY_RATE = "aiso_daily_rate";
    
    public static final int FLD_DAILY_RATE_ID = 0;
    public static final int FLD_CURRENCY_ID = 1;
    public static final int FLD_BUYING_AMOUNT = 2;
    public static final int FLD_SELLING_AMOUNT = 3;
    public static final int FLD_STATUS = 4;
    public static final int FLD_START_DATE = 5;
    public static final int FLD_END_DATE = 6;
    public static final int FLD_LOCAL_FOREIGN = 7;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT
    };
    
    public static String[] fieldNames = {
        "DAILY_RATE_ID",
        "CURRENCY_ID",
        "BUYING_AMOUNT",
        "SELLING_AMOUNT",
        "STATUS",
        "START_DATE",
        "END_DATE",
        "LOCAL_FOREIGN"
    };
    
    public static final  int NOT_ACTIVE = 0;
    public static final  int ACTIVE = 1;

    public static final String statusName[][] = {
        {"History","Aktif"},
        {"History","Active"}
    };
    
    public static final int LOCAL = 0;
    public static final int DEFAULT_FOREIGN = 1;
    public static final int FOREIGN = 2;
    
    public static final String localForeignNames[][] = {
        {"Lokal","Asing Acuan","Asing"},
        {"Local","Default Foreign","Foreign"}
    };
    
     public PstDailyRate() {
    }

    public PstDailyRate(int i) throws DBException {
        super(new PstDailyRate());
    }


    public PstDailyRate(String sOid) throws DBException {
        super(new PstDailyRate(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstDailyRate(long lOid) throws DBException {
        super(new PstDailyRate(0));
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

    public static Vector getLocalForeignKey(int language){
        Vector vResult = new Vector();
        for(int i = 0; i < localForeignNames[language].length; i++){
            vResult.add(localForeignNames[language][i]);
        }
        return vResult;
    }
    
    public static Vector getLocalForeigVal(int language){
        Vector vResult = new Vector();
        for(int i = 0; i < localForeignNames[language].length; i++){
            vResult.add(""+i);
        }
        return vResult;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_DAILY_RATE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDailyRate().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DailyRate objDailyRate = PstDailyRate.fetchExc(ent.getOID());
        ent = (Entity) objDailyRate;
        return objDailyRate.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstDailyRate.insertExc((DailyRate) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((BussinessCenter) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static DailyRate fetchExc(long oid) throws DBException {
        try {
            DailyRate objDailyRate = new DailyRate();
            PstDailyRate pDailyRate = new PstDailyRate(oid);

            objDailyRate.setOID(oid);
            objDailyRate.setCurrencyId(pDailyRate.getlong(FLD_CURRENCY_ID));
            objDailyRate.setBuyingAmount(pDailyRate.getdouble(FLD_BUYING_AMOUNT));
            objDailyRate.setSellingAmount(pDailyRate.getdouble(FLD_SELLING_AMOUNT));
            objDailyRate.setStatus(pDailyRate.getInt(FLD_STATUS));
            objDailyRate.setStartDate(pDailyRate.getDate(FLD_START_DATE));
            objDailyRate.setEndDate(pDailyRate.getDate(FLD_END_DATE));
            objDailyRate.setLocalForeign(pDailyRate.getInt(FLD_LOCAL_FOREIGN));
            
            return objDailyRate;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(DailyRate objDailyRate) throws DBException {
        try {
            PstDailyRate pDailyRate = new PstDailyRate(0);

            pDailyRate.setLong(FLD_CURRENCY_ID, objDailyRate.getCurrencyId());
            pDailyRate.setDouble(FLD_BUYING_AMOUNT, objDailyRate.getBuyingAmount());
            pDailyRate.setDouble(FLD_SELLING_AMOUNT, objDailyRate.getSellingAmount());
            pDailyRate.setInt(FLD_STATUS, objDailyRate.getStatus());
            pDailyRate.setDate(FLD_START_DATE, objDailyRate.getStartDate());
            pDailyRate.setDate(FLD_END_DATE, objDailyRate.getEndDate());
            pDailyRate.setInt(FLD_LOCAL_FOREIGN, objDailyRate.getLocalForeign());
            pDailyRate.insert();

            objDailyRate.setOID(pDailyRate.getlong(FLD_DAILY_RATE_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
        return objDailyRate.getOID();
    }

    public static long updateExc(DailyRate objDailyRate) throws DBException {
        try {
            if (objDailyRate.getOID() != 0) {
                PstDailyRate pDailyRate = new PstDailyRate(objDailyRate.getOID());
                
                pDailyRate.setLong(FLD_CURRENCY_ID, objDailyRate.getCurrencyId());
                pDailyRate.setDouble(FLD_BUYING_AMOUNT, objDailyRate.getBuyingAmount());
                pDailyRate.setDouble(FLD_SELLING_AMOUNT, objDailyRate.getSellingAmount());
                pDailyRate.setInt(FLD_STATUS, objDailyRate.getStatus());
                pDailyRate.setDate(FLD_START_DATE, objDailyRate.getStartDate());
                pDailyRate.setDate(FLD_END_DATE, objDailyRate.getEndDate());
                pDailyRate.setInt(FLD_LOCAL_FOREIGN, objDailyRate.getLocalForeign());
                
                pDailyRate.update();

                return objDailyRate.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDailyRate pDailyRate = new PstDailyRate(oid);
            pDailyRate.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_DAILY_RATE;
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
                DailyRate objDailyRate = new DailyRate();
                resultToObject(rs, objDailyRate);
                lists.add(objDailyRate);
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

    private static void resultToObject(ResultSet rs, DailyRate objDailyRate) {
        try {
            objDailyRate.setOID(rs.getLong(fieldNames[FLD_DAILY_RATE_ID]));
            objDailyRate.setCurrencyId(rs.getLong(fieldNames[FLD_CURRENCY_ID]));
            objDailyRate.setBuyingAmount(rs.getDouble(fieldNames[FLD_BUYING_AMOUNT]));
            objDailyRate.setSellingAmount(rs.getDouble(fieldNames[FLD_SELLING_AMOUNT]));
            objDailyRate.setStatus(rs.getInt(fieldNames[FLD_STATUS]));
            objDailyRate.setStartDate(rs.getDate(fieldNames[FLD_START_DATE]));
            objDailyRate.setEndDate(rs.getDate(fieldNames[FLD_END_DATE]));
            objDailyRate.setLocalForeign(rs.getInt(fieldNames[FLD_LOCAL_FOREIGN]));
            
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ fieldNames[FLD_DAILY_RATE_ID] + ") FROM " + TBL_DAILY_RATE;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while(rs.next()) { count = rs.getInt(1); }

            rs.close();
            return count;
        }catch(Exception e) {
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }
    
     public static void updateStatus(long currencyTypeId){
        String whereClause = fieldNames[FLD_CURRENCY_ID] +" = "+currencyTypeId+
        " AND "+fieldNames[FLD_STATUS] +" = "+ACTIVE;
        String order = fieldNames[FLD_STATUS]+" DESC ";
        Vector result = list(0,0, whereClause, order);
        if(result!=null&&result.size()>0){
            for(int i=0;i<result.size();i++){
                DailyRate sRate = (DailyRate)result.get(i);
                sRate.setStatus(NOT_ACTIVE);
                try{
                    updateExc(sRate);
                    System.out.println("Update OK");
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("err di update status : "+e.toString());
                }
            }
        }
    }

}
