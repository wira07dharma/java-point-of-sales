package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstMatCurrency extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	//public static final  String TBL_CURRENCY = "POS_CURRENCY";
        public static final  String TBL_CURRENCY = "pos_currency";

	public static final  int FLD_CURRENCY_ID	= 0;
	public static final  int FLD_CODE 		= 1;
	public static final  int FLD_NAME 		= 2;
	public static final  int FLD_SELLING_RATE 	= 3;
	public static final  int FLD_BUYING_RATE 	= 4;

	public static final  String[] fieldNames =
        {
		"CURRENCY_ID",
		"CODE",
		"NAME",
		"SELLING_RATE",
		"BUYING_RATE"
	};

	public static final  int[] fieldTypes =
        {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_FLOAT,
		TYPE_FLOAT
	};

	public PstMatCurrency(){
	}

	public PstMatCurrency(int i) throws DBException {
		super(new PstMatCurrency());
	}

	public PstMatCurrency(String sOid) throws DBException {
		super(new PstMatCurrency(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstMatCurrency(long lOid) throws DBException {
		super(new PstMatCurrency(0));
		String sOid = "0";
		try {
			sOid = String.valueOf(lOid);
		}catch(Exception e) {
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public int getFieldSize(){
		return fieldNames.length;
	}

	public String getTableName(){
		return TBL_CURRENCY;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstMatCurrency().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		MatCurrency matCurrency = fetchExc(ent.getOID());
		ent = (Entity)matCurrency;
		return matCurrency.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((MatCurrency) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((MatCurrency) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID());
	}

	public static MatCurrency fetchExc(long oid) throws DBException
        {
		try
                {
			MatCurrency matCurrency = new MatCurrency();
			PstMatCurrency pstCurrency = new PstMatCurrency(oid);
			matCurrency.setOID(oid);

			matCurrency.setCode(pstCurrency.getString(FLD_CODE));
			matCurrency.setName(pstCurrency.getString(FLD_NAME));
			matCurrency.setSellingRate(pstCurrency.getdouble(FLD_SELLING_RATE));
			matCurrency.setBuyingRate(pstCurrency.getdouble(FLD_BUYING_RATE));

			return matCurrency;
		}
                catch(DBException dbe)
                {
			throw dbe;
		}
                catch(Exception e)
                {
			throw new DBException(new PstMatCurrency(0),DBException.UNKNOWN);
		}
	}

	public static long insertExc(MatCurrency matCurrency) throws DBException
        {
		try
                {
			PstMatCurrency pstCurrency = new PstMatCurrency(0);

			pstCurrency.setString(FLD_CODE, matCurrency.getCode());
			pstCurrency.setString(FLD_NAME, matCurrency.getName());
			pstCurrency.setDouble(FLD_SELLING_RATE, matCurrency.getSellingRate());
			pstCurrency.setDouble(FLD_BUYING_RATE, matCurrency.getBuyingRate());

			pstCurrency.insert();
			matCurrency.setOID(pstCurrency.getlong(FLD_CURRENCY_ID));
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCurrency.getInsertSQL());
			
		}
                catch(DBException dbe)
                {
			throw dbe;
		}
                catch(Exception e)
                {
			throw new DBException(new PstMatCurrency(0),DBException.UNKNOWN);
		}
		return matCurrency.getOID();
	}

	public static long updateExc(MatCurrency matCurrency) throws DBException
        {
		try
                {
			if(matCurrency.getOID() != 0)
                        {
				PstMatCurrency pstCurrency = new PstMatCurrency(matCurrency.getOID());

				pstCurrency.setString(FLD_CODE, matCurrency.getCode());
				pstCurrency.setString(FLD_NAME, matCurrency.getName());
				pstCurrency.setDouble(FLD_SELLING_RATE, matCurrency.getSellingRate());
				pstCurrency.setDouble(FLD_BUYING_RATE, matCurrency.getBuyingRate());

				pstCurrency.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstCurrency.getUpdateSQL());
				return matCurrency.getOID();

			}
		}
                catch(DBException dbe)
                {
			throw dbe;
		}
                catch(Exception e)
                {
			throw new DBException(new PstMatCurrency(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException
        {
		try
                {
			PstMatCurrency pstCurrency = new PstMatCurrency(oid);
			pstCurrency.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCurrency.getDeleteSQL());
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstMatCurrency(0),DBException.UNKNOWN);
		}
		return oid;
	}

	public static Vector listAll(){
		return list(0, 500, "","");
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order)
        {
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try
                {
			String sql = "SELECT * FROM " + TBL_CURRENCY;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
                        switch (DBHandler.DBSVR_TYPE) {
                        case DBHandler.DBSVR_MYSQL :
						if(limitStart == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

                                                break;

                        case DBHandler.DBSVR_POSTGRESQL :
						if(limitStart == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;

                                                break;

                         case DBHandler.DBSVR_SYBASE :
                                break;

                         case DBHandler.DBSVR_ORACLE :
                                break;

                         case DBHandler.DBSVR_MSSQL :
                                break;

                        default:
                            ;
            }
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next())
                        {
				MatCurrency matCurrency = new MatCurrency();
				resultToObject(rs, matCurrency);
				lists.add(matCurrency);
			}
			rs.close();
			return lists;

		}
                catch(Exception e)
                {
			System.out.println(e);
		}
                finally
                {
			DBResultSet.close(dbrs);
		}
		return new Vector();
	}

	private static void resultToObject(ResultSet rs, MatCurrency matCurrency)
        {
		try
                {
			matCurrency.setOID(rs.getLong(PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID]));
			matCurrency.setCode(rs.getString(PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE]));
			matCurrency.setName(rs.getString(PstMatCurrency.fieldNames[PstMatCurrency.FLD_NAME]));
			matCurrency.setSellingRate(rs.getDouble(PstMatCurrency.fieldNames[PstMatCurrency.FLD_SELLING_RATE]));
			matCurrency.setBuyingRate(rs.getDouble(PstMatCurrency.fieldNames[PstMatCurrency.FLD_BUYING_RATE]));
		}
                catch(Exception e)
                { }
	}

	public static boolean checkOID(long currencyId)
        {
		DBResultSet dbrs = null;
		boolean result = false;
		try
                {
			String sql = "SELECT * FROM " + TBL_CURRENCY + " WHERE " +
						fieldNames[PstMatCurrency.FLD_CURRENCY_ID] + " = " + currencyId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
		}
		return result;
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ fieldNames[PstMatCurrency.FLD_CURRENCY_ID] + ") FROM " + TBL_CURRENCY;
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


	/* This method used to find current data */
	public static int findLimitStart( long oid, int recordToGet, String whereClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order);
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){
			  	   MatCurrency matCurrency = (MatCurrency)list.get(ls);
				   if(oid == matCurrency.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MatCurrency matCurrency = PstMatCurrency.fetchExc(oid);
                object.put(PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID], matCurrency.getOID());
                object.put(PstMatCurrency.fieldNames[PstMatCurrency.FLD_BUYING_RATE], matCurrency.getBuyingRate());
                object.put(PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE], matCurrency.getCode());
                object.put(PstMatCurrency.fieldNames[PstMatCurrency.FLD_NAME], matCurrency.getName());
                object.put(PstMatCurrency.fieldNames[PstMatCurrency.FLD_SELLING_RATE], matCurrency.getSellingRate());
            }catch(Exception exc){}

            return object;
        }
}
