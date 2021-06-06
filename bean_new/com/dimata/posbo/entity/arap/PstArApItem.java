// Generated by Together

package com.dimata.posbo.entity.arap;

import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstArApItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {

    public static final String TBL_ARAP_ITEM = "aiso_arap_item";
    public static final int FLD_ARAP_ITEM_ID = 0;
    public static final int FLD_ARAP_MAIN_ID = 1;
    public static final int FLD_ANGSURAN = 2;
    public static final int FLD_DUE_DATE = 3;
    public static final int FLD_DESCRIPTION = 4;
    public static final int FLD_ARAP_ITEM_STATUS = 5;
    public static final int FLD_LEFT_TO_PAY = 6;
    public static final int FLD_CURRENCY_ID = 7;
    public static final int FLD_RATE = 8;
    public static final int FLD_RECEIVE_AKTIVA_ID = 9;
    public static final int FLD_SELLING_AKTIVA_ID = 10;

    public static String[] fieldNames = {
        "ARAP_ITEM_ID",
        "ARAP_MAIN_ID",
        "ANGSURAN",
        "DUE_DATE",
        "DESCRIPTION",
        "ARAP_ITEM_STATUS",
        "LEFT_TO_PAY",
        "CURRENCY_ID",
        "RATE",
        "RECEIVE_AKTIVA_ID",
        "SELLING_AKTIVA_ID"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstArApItem() {
    }

    public PstArApItem(int i) throws DBException {
        super(new PstArApItem());
    }

    public PstArApItem(String sOid) throws DBException {
        super(new PstArApItem(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstArApItem(long lOid) throws DBException {
        super(new PstArApItem(0));
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
        return TBL_ARAP_ITEM;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstArApItem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws DBException {
        ArApItem aktiva = fetchExc(ent.getOID());
        ent = (Entity) aktiva;
        return aktiva.getOID();
    }

    public long insertExc(Entity ent) throws DBException {
        return insertExc((ArApItem) ent);
    }

    public long updateExc(Entity ent) throws DBException {
        return updateExc((ArApItem) ent);
    }

    public long deleteExc(Entity ent) throws DBException {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ArApItem fetchExc(long Oid) throws DBException {
        try {
            ArApItem aktiva = new ArApItem();
            PstArApItem pstAktiva = new PstArApItem(Oid);
            aktiva.setOID(Oid);

            aktiva.setArApMainId(pstAktiva.getlong(FLD_ARAP_MAIN_ID));
            aktiva.setAngsuran(pstAktiva.getdouble(FLD_ANGSURAN));
            aktiva.setDueDate(pstAktiva.getDate(FLD_DUE_DATE));
            aktiva.setDescription(pstAktiva.getString(FLD_DESCRIPTION));
            aktiva.setArApItemStatus(pstAktiva.getInt(FLD_ARAP_ITEM_STATUS));
            aktiva.setLeftToPay(pstAktiva.getdouble(FLD_LEFT_TO_PAY));
            aktiva.setCurrencyId(pstAktiva.getlong(FLD_CURRENCY_ID));
            aktiva.setRate(pstAktiva.getdouble(FLD_RATE));
            aktiva.setSellingAktivaId(pstAktiva.getlong(FLD_SELLING_AKTIVA_ID));
            aktiva.setReceiveAktivaId(pstAktiva.getlong(FLD_RECEIVE_AKTIVA_ID));

            return aktiva;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApItem(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ArApItem aktiva) throws DBException {
        try {
            PstArApItem pstAktiva = new PstArApItem(0);

            pstAktiva.setLong(FLD_ARAP_MAIN_ID, aktiva.getArApMainId());
            pstAktiva.setDouble(FLD_ANGSURAN, aktiva.getAngsuran());
            pstAktiva.setDate(FLD_DUE_DATE, aktiva.getDueDate());
            pstAktiva.setString(FLD_DESCRIPTION, aktiva.getDescription());
            pstAktiva.setInt(FLD_ARAP_ITEM_STATUS, aktiva.getArApItemStatus());
            pstAktiva.setDouble(FLD_LEFT_TO_PAY, aktiva.getLeftToPay());
            pstAktiva.setLong(FLD_CURRENCY_ID, aktiva.getCurrencyId());
            pstAktiva.setDouble(FLD_RATE, aktiva.getRate());
            pstAktiva.setLong(FLD_SELLING_AKTIVA_ID, aktiva.getSellingAktivaId());
            pstAktiva.setLong(FLD_RECEIVE_AKTIVA_ID, aktiva.getReceiveAktivaId());

            pstAktiva.insert();
            aktiva.setOID(pstAktiva.getlong(FLD_ANGSURAN));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApItem(0), DBException.UNKNOWN);
        }
        return aktiva.getOID();
    }

    public static long updateExc(ArApItem aktiva) throws DBException {
        try {
            if (aktiva != null && aktiva.getOID() != 0) {
                PstArApItem pstAktiva = new PstArApItem(aktiva.getOID());

                pstAktiva.setLong(FLD_ARAP_MAIN_ID, aktiva.getArApMainId());
                pstAktiva.setDouble(FLD_ANGSURAN, aktiva.getAngsuran());
                pstAktiva.setDate(FLD_DUE_DATE, aktiva.getDueDate());
                pstAktiva.setString(FLD_DESCRIPTION, aktiva.getDescription());
                pstAktiva.setInt(FLD_ARAP_ITEM_STATUS, aktiva.getArApItemStatus());
                pstAktiva.setDouble(FLD_LEFT_TO_PAY, aktiva.getLeftToPay());
                pstAktiva.setLong(FLD_CURRENCY_ID, aktiva.getCurrencyId());
                pstAktiva.setDouble(FLD_RATE, aktiva.getRate());
                pstAktiva.setLong(FLD_SELLING_AKTIVA_ID, aktiva.getSellingAktivaId());
                pstAktiva.setLong(FLD_RECEIVE_AKTIVA_ID, aktiva.getReceiveAktivaId());

                pstAktiva.update();
                return aktiva.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApItem(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstArApItem pstAktiva = new PstArApItem(Oid);
            pstAktiva.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApItem(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ARAP_ITEM + " ";
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
                ArApItem aktiva = new ArApItem();
                resultToObject(rs, aktiva);
                lists.add(aktiva);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstArApItem().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, ArApItem aktiva) {
        try {
            aktiva.setOID(rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]));
            aktiva.setAngsuran(rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_ANGSURAN]));
            aktiva.setArApMainId(rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]));
            aktiva.setDueDate(rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]));
            aktiva.setDescription(rs.getString(PstArApItem.fieldNames[PstArApItem.FLD_DESCRIPTION]));
            aktiva.setArApItemStatus(rs.getInt(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_STATUS]));
            aktiva.setLeftToPay(rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]));
            aktiva.setCurrencyId(rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_CURRENCY_ID]));
            aktiva.setRate(rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_RATE]));
            aktiva.setSellingAktivaId(rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID]));
            aktiva.setReceiveAktivaId(rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstArApItem.fieldNames[PstArApItem.FLD_ANGSURAN] + ") " +
                    " FROM " + TBL_ARAP_ITEM;
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

    public static double getTotalAngsuran(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstArApItem.fieldNames[PstArApItem.FLD_ANGSURAN] + ") " +
                    " FROM " + TBL_ARAP_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double result = 0;
            if (rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static void deleteByMainId(long oid) {
        DBResultSet dbrs = null;
        String whereClause = PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID] + "=" + oid;
        try {
            String sql = " DELETE FROM " + TBL_ARAP_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

     public static void deleteBySellingAktivaId(long oid) {
        DBResultSet dbrs = null;
        String whereClause = PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID] + "=" + oid;
        try {
            String sql = " DELETE FROM " + TBL_ARAP_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

     public static void deleteByReceiveAktivaId(long oid) {
        DBResultSet dbrs = null;
        String whereClause = PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID] + "=" + oid;
        try {
            String sql = " DELETE FROM " + TBL_ARAP_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         ArApItem arApItem = PstArApItem.fetchExc(oid);
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID], arApItem.getOID());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID], arApItem.getArApMainId());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_ANGSURAN], arApItem.getCurrencyId());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE], arApItem.getRate());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_DESCRIPTION], arApItem.getAngsuran());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_STATUS], arApItem.getDueDate());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY], arApItem.getDescription());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_CURRENCY_ID], arApItem.getArApItemStatus());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_RATE], arApItem.getLeftToPay());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID], arApItem.getSellingAktivaId());
         object.put(PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID], arApItem.getReceiveAktivaId());
      }catch(Exception exc){}
      return object;
   }
}
