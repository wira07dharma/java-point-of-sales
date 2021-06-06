
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.locker; 

/* package java */ 
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

/* package harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.locker.*; 

public class PstLocker extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LOCKER = "HR_LOCKER";

	public static final  int FLD_LOCKER_ID = 0;
	public static final  int FLD_LOCATION_ID = 1;
	public static final  int FLD_LOCKER_NUMBER = 2;
	public static final  int FLD_KEY_NUMBER = 3;
	public static final  int FLD_SPARE_KEY = 4;
        public static final  int FLD_CONDITION_ID = 5;

	public static final  String[] fieldNames = {
		"LOCKER_ID",
		"LOCATION_ID",
		"LOCKER_NUMBER",
		"KEY_NUMBER",
		"SPARE_KEY",
		"CONDITION_ID"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_LONG
	 }; 

	public PstLocker(){
	}

	public PstLocker(int i) throws DBException { 
		super(new PstLocker()); 
	}

	public PstLocker(String sOid) throws DBException { 
		super(new PstLocker(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLocker(long lOid) throws DBException { 
		super(new PstLocker(0)); 
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
		return TBL_HR_LOCKER;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLocker().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Locker locker = fetchExc(ent.getOID()); 
		ent = (Entity)locker; 
		return locker.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Locker) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Locker) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Locker fetchExc(long oid) throws DBException{ 
		try{ 
			Locker locker = new Locker();
			PstLocker pstLocker = new PstLocker(oid); 
			locker.setOID(oid);

			locker.setLocationId(pstLocker.getlong(FLD_LOCATION_ID));
			locker.setLockerNumber(pstLocker.getString(FLD_LOCKER_NUMBER));
			locker.setKeyNumber(pstLocker.getString(FLD_KEY_NUMBER));
			locker.setSpareKey(pstLocker.getString(FLD_SPARE_KEY));
			locker.setConditionId(pstLocker.getlong(FLD_CONDITION_ID));

			return locker; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLocker(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Locker locker) throws DBException{ 
		try{ 
			PstLocker pstLocker = new PstLocker(0);

			pstLocker.setLong(FLD_LOCATION_ID, locker.getLocationId());
			pstLocker.setString(FLD_LOCKER_NUMBER, locker.getLockerNumber());
			pstLocker.setString(FLD_KEY_NUMBER, locker.getKeyNumber());
			pstLocker.setString(FLD_SPARE_KEY, locker.getSpareKey());
			pstLocker.setLong(FLD_CONDITION_ID, locker.getConditionId());

			pstLocker.insert(); 
			locker.setOID(pstLocker.getlong(FLD_LOCKER_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLocker(0),DBException.UNKNOWN); 
		}
		return locker.getOID();
	}

	public static long updateExc(Locker locker) throws DBException{ 
		try{ 
			if(locker.getOID() != 0){ 
				PstLocker pstLocker = new PstLocker(locker.getOID());

				pstLocker.setLong(FLD_LOCATION_ID, locker.getLocationId());
				pstLocker.setString(FLD_LOCKER_NUMBER, locker.getLockerNumber());
				pstLocker.setString(FLD_KEY_NUMBER, locker.getKeyNumber());
				pstLocker.setString(FLD_SPARE_KEY, locker.getSpareKey());
				pstLocker.setLong(FLD_CONDITION_ID, locker.getConditionId());

				pstLocker.update(); 
				return locker.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLocker(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLocker pstLocker = new PstLocker(oid);
			pstLocker.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLocker(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_LOCKER; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Locker locker = new Locker();
				resultToObject(rs, locker);
				lists.add(locker);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}

	private static void resultToObject(ResultSet rs, Locker locker){
		try{
			locker.setOID(rs.getLong(PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]));
			locker.setLocationId(rs.getLong(PstLocker.fieldNames[PstLocker.FLD_LOCATION_ID]));
			locker.setLockerNumber(rs.getString(PstLocker.fieldNames[PstLocker.FLD_LOCKER_NUMBER]));
			locker.setKeyNumber(rs.getString(PstLocker.fieldNames[PstLocker.FLD_KEY_NUMBER]));
			locker.setSpareKey(rs.getString(PstLocker.fieldNames[PstLocker.FLD_SPARE_KEY]));
			locker.setConditionId(rs.getLong(PstLocker.fieldNames[PstLocker.FLD_CONDITION_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long lockerId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LOCKER + " WHERE " + 
						PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID] + " = '" + lockerId + "'";

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID] + ") FROM " + TBL_HR_LOCKER;
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
			  	   Locker locker = (Locker)list.get(ls);
				   if(oid == locker.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static boolean checkLocation(long locationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LOCKER + " WHERE " + 
						PstLocker.fieldNames[PstLocker.FLD_LOCATION_ID] + " = '" + locationId + "'";

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}


    public static long updateLocker(Locker locker){
		try{
			String sql = " UPDATE "+PstLocker.TBL_HR_LOCKER+
                         " SET "+PstLocker.fieldNames[PstLocker.FLD_LOCATION_ID]+
                         " = "+locker.getLocationId()+
                         " , "+PstLocker.fieldNames[PstLocker.FLD_LOCKER_NUMBER]+
                         " = "+locker.getLockerNumber()+
                         " WHERE "+PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]+
                         " = "+locker.getOID();

			DBHandler.execUpdate(sql);
            return locker.getOID();

		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}
        return 0;
    }

}
