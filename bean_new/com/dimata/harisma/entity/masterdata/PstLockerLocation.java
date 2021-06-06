
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.masterdata; 

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

/* package  harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.locker.*;

public class PstLockerLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LOCKER_LOCATION = "HR_LOCKER_LOCATION";

	public static final  int FLD_LOCATION_ID = 0;
	public static final  int FLD_LOCATION = 1;
	public static final  int FLD_SEX = 2;

	public static final  String[] fieldNames = {
		"LOCATION_ID",
		"LOCATION",
		"SEX"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstLockerLocation(){
	}

	public PstLockerLocation(int i) throws DBException { 
		super(new PstLockerLocation()); 
	}

	public PstLockerLocation(String sOid) throws DBException { 
		super(new PstLockerLocation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLockerLocation(long lOid) throws DBException { 
		super(new PstLockerLocation(0)); 
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
		return TBL_HR_LOCKER_LOCATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLockerLocation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		LockerLocation lockerlocation = fetchExc(ent.getOID()); 
		ent = (Entity)lockerlocation; 
		return lockerlocation.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((LockerLocation) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((LockerLocation) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static LockerLocation fetchExc(long oid) throws DBException{ 
		try{ 
			LockerLocation lockerlocation = new LockerLocation();
			PstLockerLocation pstLockerLocation = new PstLockerLocation(oid); 
			lockerlocation.setOID(oid);

			lockerlocation.setLocation(pstLockerLocation.getString(FLD_LOCATION));
			lockerlocation.setSex(pstLockerLocation.getString(FLD_SEX));

			return lockerlocation; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerLocation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(LockerLocation lockerlocation) throws DBException{ 
		try{ 
			PstLockerLocation pstLockerLocation = new PstLockerLocation(0);

			pstLockerLocation.setString(FLD_LOCATION, lockerlocation.getLocation());
			pstLockerLocation.setString(FLD_SEX, lockerlocation.getSex());

			pstLockerLocation.insert(); 
			lockerlocation.setOID(pstLockerLocation.getlong(FLD_LOCATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerLocation(0),DBException.UNKNOWN); 
		}
		return lockerlocation.getOID();
	}

	public static long updateExc(LockerLocation lockerlocation) throws DBException{ 
		try{ 
			if(lockerlocation.getOID() != 0){ 
				PstLockerLocation pstLockerLocation = new PstLockerLocation(lockerlocation.getOID());

				pstLockerLocation.setString(FLD_LOCATION, lockerlocation.getLocation());
				pstLockerLocation.setString(FLD_SEX, lockerlocation.getSex());

				pstLockerLocation.update(); 
				return lockerlocation.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerLocation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLockerLocation pstLockerLocation = new PstLockerLocation(oid);
			pstLockerLocation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerLocation(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_LOCATION; 
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
				LockerLocation lockerlocation = new LockerLocation();
				resultToObject(rs, lockerlocation);
				lists.add(lockerlocation);
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

	private static void resultToObject(ResultSet rs, LockerLocation lockerlocation){
		try{
			lockerlocation.setOID(rs.getLong(PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION_ID]));
			lockerlocation.setLocation(rs.getString(PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION]));
			lockerlocation.setSex(rs.getString(PstLockerLocation.fieldNames[PstLockerLocation.FLD_SEX]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long locationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_LOCATION + " WHERE " + 
						PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION_ID] + " = " + locationId;

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
			String sql = "SELECT COUNT("+ PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION_ID] + ") FROM " + TBL_HR_LOCKER_LOCATION;
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
			  	   LockerLocation lockerlocation = (LockerLocation)list.get(ls);
				   if(oid == lockerlocation.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static boolean checkMaster(long oid){
    	if(PstLocker.checkLocation(oid))
            return true;
    	else{
            if(PstLockerTreatment.checkLocation(oid))
                return true;
            else
            	return false;
    	}
    }
}
