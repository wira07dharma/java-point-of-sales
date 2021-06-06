
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
//import com.dimata. harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.locker.*; 

public class PstLockerTreatment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LOCKER_TREATMENT = "HR_LOCKER_TREATMENT";

	public static final  int FLD_TREATMENT_ID = 0;
	public static final  int FLD_LOCATION_ID = 1;
	public static final  int FLD_TREATMENT_DATE = 2;
	public static final  int FLD_TREATMENT = 3;

	public static final  String[] fieldNames = {
		"TREATMENT_ID",
		"LOCATION_ID",
		"TREATMENT_DATE",
		"TREATMENT"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_STRING
	 }; 

	public PstLockerTreatment(){
	}

	public PstLockerTreatment(int i) throws DBException { 
		super(new PstLockerTreatment()); 
	}

	public PstLockerTreatment(String sOid) throws DBException { 
		super(new PstLockerTreatment(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLockerTreatment(long lOid) throws DBException { 
		super(new PstLockerTreatment(0)); 
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
		return TBL_HR_LOCKER_TREATMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLockerTreatment().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		LockerTreatment lockertreatment = fetchExc(ent.getOID()); 
		ent = (Entity)lockertreatment; 
		return lockertreatment.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((LockerTreatment) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((LockerTreatment) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static LockerTreatment fetchExc(long oid) throws DBException{ 
		try{ 
			LockerTreatment lockertreatment = new LockerTreatment();
			PstLockerTreatment pstLockerTreatment = new PstLockerTreatment(oid); 
			lockertreatment.setOID(oid);

			lockertreatment.setLocationId(pstLockerTreatment.getlong(FLD_LOCATION_ID));
			lockertreatment.setTreatmentDate(pstLockerTreatment.getDate(FLD_TREATMENT_DATE));
			lockertreatment.setTreatment(pstLockerTreatment.getString(FLD_TREATMENT));

			return lockertreatment; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerTreatment(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(LockerTreatment lockertreatment) throws DBException{ 
		try{ 
			PstLockerTreatment pstLockerTreatment = new PstLockerTreatment(0);

			pstLockerTreatment.setLong(FLD_LOCATION_ID, lockertreatment.getLocationId());
			pstLockerTreatment.setDate(FLD_TREATMENT_DATE, lockertreatment.getTreatmentDate());
			pstLockerTreatment.setString(FLD_TREATMENT, lockertreatment.getTreatment());

			pstLockerTreatment.insert(); 
			lockertreatment.setOID(pstLockerTreatment.getlong(FLD_TREATMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerTreatment(0),DBException.UNKNOWN); 
		}
		return lockertreatment.getOID();
	}

	public static long updateExc(LockerTreatment lockertreatment) throws DBException{ 
		try{ 
			if(lockertreatment.getOID() != 0){ 
				PstLockerTreatment pstLockerTreatment = new PstLockerTreatment(lockertreatment.getOID());

				pstLockerTreatment.setLong(FLD_LOCATION_ID, lockertreatment.getLocationId());
				pstLockerTreatment.setDate(FLD_TREATMENT_DATE, lockertreatment.getTreatmentDate());
				pstLockerTreatment.setString(FLD_TREATMENT, lockertreatment.getTreatment());

				pstLockerTreatment.update(); 
				return lockertreatment.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerTreatment(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLockerTreatment pstLockerTreatment = new PstLockerTreatment(oid);
			pstLockerTreatment.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerTreatment(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_TREATMENT; 
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
				LockerTreatment lockertreatment = new LockerTreatment();
				resultToObject(rs, lockertreatment);
				lists.add(lockertreatment);
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

	private static void resultToObject(ResultSet rs, LockerTreatment lockertreatment){
		try{
			lockertreatment.setOID(rs.getLong(PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_TREATMENT_ID]));
			lockertreatment.setLocationId(rs.getLong(PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_LOCATION_ID]));
			lockertreatment.setTreatmentDate(rs.getDate(PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_TREATMENT_DATE]));
			lockertreatment.setTreatment(rs.getString(PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_TREATMENT]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long treatmentId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_TREATMENT + " WHERE " + 
						PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_TREATMENT_ID] + " = '" + treatmentId + "'";

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
			String sql = "SELECT COUNT("+ PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_TREATMENT_ID] + ") FROM " + TBL_HR_LOCKER_TREATMENT;
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
			  	   LockerTreatment lockertreatment = (LockerTreatment)list.get(ls);
				   if(oid == lockertreatment.getOID())
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
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_TREATMENT + " WHERE " + 
						PstLockerTreatment.fieldNames[PstLockerTreatment.FLD_LOCATION_ID] + " = '" + locationId + "'";

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
}
