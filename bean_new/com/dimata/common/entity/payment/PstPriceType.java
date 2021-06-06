/*
 * PstPriceType.java
 *
 * Created on July 25, 2005, 3:35 PM
 */

package com.dimata.common.entity.payment;

/* package java */


/* package qdep */
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.*;

/* package java */
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.posbo.entity.masterdata.*;

/**
 *
 * @author  gedhy
 */
public class PstPriceType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    public static final String TBL_POS_PRICE_TYPE = "price_type";

    public static final int FLD_PRICE_TYPE_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_INDEX = 3;
    public static final int FLD_PRICE_SYSTEM = 4;

    public static final String[] fieldNames = {
        "PRICE_TYPE_ID",
        "CODE",
        "NAME",
        "PINDEX",
        "PRICE_SYSTEM"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };

    public PstPriceType() {
    }

    public PstPriceType(int i) throws DBException {
        super(new PstPriceType());
    }

    public PstPriceType(String sOid) throws DBException {
        super(new PstPriceType(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPriceType(long lOid) throws DBException {
        super(new PstPriceType(0));
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
        return TBL_POS_PRICE_TYPE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPriceType().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PriceType pricetype = fetchExc(ent.getOID());
        ent = (Entity) pricetype;
        return pricetype.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PriceType) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PriceType) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PriceType fetchExc(long oid) throws DBException {
        try {
            PriceType pricetype = new PriceType();
            PstPriceType pstPriceType = new PstPriceType(oid);
            pricetype.setOID(oid);

            pricetype.setCode(pstPriceType.getString(FLD_CODE));
            pricetype.setName(pstPriceType.getString(FLD_NAME));
            pricetype.setIndex(pstPriceType.getInt(FLD_INDEX));
            pricetype.setPriceSystem(pstPriceType.getInt(FLD_PRICE_SYSTEM));


            return pricetype;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceType(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PriceType pricetype) throws DBException {
        try {
            PstPriceType pstPriceType = new PstPriceType(0);

            pstPriceType.setString(FLD_CODE, pricetype.getCode());
            pstPriceType.setString(FLD_NAME, pricetype.getName());
            pstPriceType.setInt(FLD_INDEX, pricetype.getIndex());
            pstPriceType.setInt(FLD_PRICE_SYSTEM, pricetype.getPriceSystem());

            pstPriceType.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstPriceType.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            pricetype.setOID(pstPriceType.getlong(FLD_PRICE_TYPE_ID));
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceType.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceType(0), DBException.UNKNOWN);
        }
        return pricetype.getOID();
    }

    public static long updateExc(PriceType pricetype) throws DBException {
        try {
            if (pricetype.getOID() != 0) {
                PstPriceType pstPriceType = new PstPriceType(pricetype.getOID());

                pstPriceType.setString(FLD_CODE, pricetype.getCode());
                pstPriceType.setString(FLD_NAME, pricetype.getName());
                pstPriceType.setInt(FLD_INDEX, pricetype.getIndex());
                pstPriceType.setInt(FLD_PRICE_SYSTEM, pricetype.getPriceSystem());

                pstPriceType.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstPriceType.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstPriceType.getUpdateSQL());
				
                return pricetype.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPriceType pstPriceType = new PstPriceType(oid);
            pstPriceType.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceType.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceType(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_TYPE; 
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
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PriceType pricetype = new PriceType();
                resultToObject(rs, pricetype);
                lists.add(pricetype);
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

    private static void resultToObject(ResultSet rs, PriceType pricetype) {
        try {
            pricetype.setOID(rs.getLong(PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]));
            pricetype.setCode(rs.getString(PstPriceType.fieldNames[PstPriceType.FLD_CODE]));
            pricetype.setName(rs.getString(PstPriceType.fieldNames[PstPriceType.FLD_NAME]));
            pricetype.setIndex(rs.getInt(PstPriceType.fieldNames[PstPriceType.FLD_INDEX]));
            pricetype.setPriceSystem(rs.getInt(PstPriceType.fieldNames[PstPriceType.FLD_PRICE_SYSTEM]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long priceTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_TYPE + " WHERE " +
                    PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] + " = " + priceTypeId;

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
            String sql = "SELECT COUNT(" + PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] + ") FROM " + TBL_POS_PRICE_TYPE;
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
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PriceType pricetype = (PriceType) list.get(ls);
                    if (oid == pricetype.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }
}