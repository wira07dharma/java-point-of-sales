/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.entity.payment;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;


public class  PstCurrencyType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language  {

    public static final String TBL_POS_CURRENCY_TYPE = "currency_type";

    public static final int FLD_CURRENCY_TYPE_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_DESCRIPTION = 3;
    public static final int FLD_TAB_INDEX = 4;
    public static final int FLD_INCLUDE_IN_PROCESS = 5;

    public static final String[] fieldNames = {
        "CURRENCY_TYPE_ID",
        "CODE",
        "NAME",
        "DESCRIPTION",
        "TAB_INDEX",
        "INCLUDE_IN_PROCESS"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };

    public static final int NOT_INCLUDE = 0;
    public static final int INCLUDE = 1;

    public static final String includeName[][] = {
        {"Tidak", "Ya"},
        {"Not Include", "Include"}
    };

    public PstCurrencyType() {
    }

    public PstCurrencyType(int i) throws DBException {
        super(new PstCurrencyType());
    }

    public PstCurrencyType(String sOid) throws DBException {
        super(new PstCurrencyType(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstCurrencyType(long lOid) throws DBException {
        super(new PstCurrencyType(0));
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
        return TBL_POS_CURRENCY_TYPE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCurrencyType().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CurrencyType currencytype = fetchExc(ent.getOID());
        ent = (Entity) currencytype;
        return currencytype.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CurrencyType) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CurrencyType) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CurrencyType fetchExc(long oid) throws DBException {
        try {
            CurrencyType currencytype = new CurrencyType();
            PstCurrencyType pstCurrencyType = new PstCurrencyType(oid);
            currencytype.setOID(oid);

            currencytype.setCode(pstCurrencyType.getString(FLD_CODE));
            currencytype.setName(pstCurrencyType.getString(FLD_NAME));
            currencytype.setDescription(pstCurrencyType.getString(FLD_DESCRIPTION));
            currencytype.setTabIndex(pstCurrencyType.getInt(FLD_TAB_INDEX));
            currencytype.setIncludeInProcess(pstCurrencyType.getInt(FLD_INCLUDE_IN_PROCESS));

            return currencytype;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCurrencyType(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(CurrencyType currencytype) throws DBException {
        try {
            PstCurrencyType pstCurrencyType = new PstCurrencyType(0);

            pstCurrencyType.setString(FLD_CODE, currencytype.getCode());
            pstCurrencyType.setString(FLD_NAME, currencytype.getName());
            pstCurrencyType.setString(FLD_DESCRIPTION, currencytype.getDescription());
            pstCurrencyType.setInt(FLD_TAB_INDEX, currencytype.getTabIndex());
            pstCurrencyType.setInt(FLD_INCLUDE_IN_PROCESS, currencytype.getIncludeInProcess());

            pstCurrencyType.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstCurrencyType.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);

            currencytype.setOID(pstCurrencyType.getlong(FLD_CURRENCY_TYPE_ID));
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCurrencyType.getInsertSQL());
			
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCurrencyType(0), DBException.UNKNOWN);
        }
        return currencytype.getOID();
    }

    public static long updateExc(CurrencyType currencytype) throws DBException {
        try {
            if (currencytype.getOID() != 0) {
                PstCurrencyType pstCurrencyType = new PstCurrencyType(currencytype.getOID());

                pstCurrencyType.setString(FLD_CODE, currencytype.getCode());
                pstCurrencyType.setString(FLD_NAME, currencytype.getName());
                pstCurrencyType.setString(FLD_DESCRIPTION, currencytype.getDescription());
                pstCurrencyType.setInt(FLD_TAB_INDEX, currencytype.getTabIndex());
                pstCurrencyType.setInt(FLD_INCLUDE_IN_PROCESS, currencytype.getIncludeInProcess());

                pstCurrencyType.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstCurrencyType.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstCurrencyType.getUpdateSQL());
                return currencytype.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCurrencyType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCurrencyType pstCurrencyType = new PstCurrencyType(oid);
            pstCurrencyType.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstCurrencyType.getUpdateSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCurrencyType.getDeleteSQL());

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCurrencyType(0), DBException.UNKNOWN);
        }
        return oid;
    }

    /**
     * Ari Wiwela 20130810
     * untuk multiple select currency di cahier
     * @param docType
     * @return
     */
    public static Vector getCurr(){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " +fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+ ", "+fieldNames[PstCurrencyType.FLD_CODE]
                                + " FROM " +TBL_POS_CURRENCY_TYPE
                                + " WHERE " + PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector vectTemp = new Vector(1,1);
		vectTemp.add(String.valueOf(rs.getInt(1)));
                vectTemp.add(String.valueOf(rs.getString(2)));
                result.add(vectTemp);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!data.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_CURRENCY_TYPE;
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
                CurrencyType currencytype = new CurrencyType();
                resultToObject(rs, currencytype);
                lists.add(currencytype);
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

    /**
     *  adnyana
     *  untuk mencari list data merk
     * yang returnnya hashtable
     * @return
     */
    public static Hashtable getListMerkHashtable() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_CURRENCY_TYPE;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CurrencyType currencytype = new CurrencyType();
                resultToObject(rs, currencytype);
                lists.put(currencytype.getCode().toUpperCase(),currencytype);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }
    
    /*
     * Fungsi ini digunakan untuk mendapatkan object dari curency type 
     * yang digunakan secara default oleh sistem.
     * creted by: gwawan@dimata 25 Juli 2007
     * @param
     * @return Object dari default currency type yang digunakan oleh sistem
     */
    public static CurrencyType getDefaultCurrencyType() {
        DBResultSet dbrs = null;
        CurrencyType currencyType = new CurrencyType();
        String sql = " select ct.* from "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" ct"+
                     " inner join "+PstDailyRate.TBL_POS_DAILY_RATE+" dr"+
                     " on ct."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+" = "+
                     " dr."+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+
                     " where dr."+PstDailyRate.fieldNames[PstDailyRate.FLD_SELLING_RATE]+" = 1"+
                     //update opie-eyek, tampilkan yg hanya include di proses
                     " and ct."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"=1";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, currencyType);
            }
        }
        catch(Exception e) {
            System.out.println("Exc in get default currency type: "+e.toString());
        }
        return currencyType;
    }

    public static void resultToObject(ResultSet rs, CurrencyType currencytype) {
        try {
            currencytype.setOID(rs.getLong(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]));
            currencytype.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
            currencytype.setName(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]));
            currencytype.setDescription(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_DESCRIPTION]));
            currencytype.setTabIndex(rs.getInt(PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]));
            currencytype.setIncludeInProcess(rs.getInt(PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long currencyTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_CURRENCY_TYPE + " WHERE " +
                    PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + " = " + currencyTypeId;

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

        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + ") FROM " + TBL_POS_CURRENCY_TYPE;
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
                    CurrencyType currencytype = (CurrencyType) list.get(ls);
                    if (oid == currencytype.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    /**
     * update oid lama dengan oid baru
     * @param newOID
     * @param originalOID
     * @return
     * @throws DBException
     */
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_POS_CURRENCY_TYPE +
                    " SET " + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                    " = " + originalOID +
                    " WHERE " + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                    " = " + newOID;
//            System.out.println(new PstCurrencyType().getClass().getName() + ".updateSynchOID() sql : " + sql);
            DBHandler.execUpdate(sql);
            return originalOID;
        } catch (Exception e) {
            System.out.println(new PstCurrencyType().getClass().getName() + ".updateSynchOID() exp : " + e.toString());
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }


    public static String getPriceCode(long oidCurrency) {
        DBResultSet dbrs = null;
        String getPriceCode = "";
        try {
            String sql = "SELECT " + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] + " FROM " + TBL_POS_CURRENCY_TYPE +
                         " WHERE " + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"='"+oidCurrency+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                getPriceCode = rs.getString(1);
            }

            rs.close();
            return getPriceCode;
        } catch (Exception e) {
            return getPriceCode;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static CurrencyType fetchByCode(String code) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        CurrencyType currencyType = new CurrencyType();
        try {
            String sql = "SELECT * FROM " + TBL_POS_CURRENCY_TYPE +
                    " WHERE " + fieldNames[FLD_CODE] +
                    " = '" + code + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, currencyType);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return currencyType;
    }
    
}
