
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

package com.dimata.harisma.entity.canteen; 

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
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
import com.dimata.harisma.entity.canteen.*; 

public class PstMealTime extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_MEAL_TIME = "HR_MEAL_TIME";

	public static final  int FLD_MEAL_TIME_ID = 0;
	public static final  int FLD_MEAL_TIME = 1;

	public static final  String[] fieldNames = {
		"MEAL_TIME_ID",
		"MEAL_TIME"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstMealTime(){
	}

	public PstMealTime(int i) throws DBException { 
		super(new PstMealTime()); 
	}

	public PstMealTime(String sOid) throws DBException { 
		super(new PstMealTime(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstMealTime(long lOid) throws DBException { 
		super(new PstMealTime(0)); 
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
		return TBL_HR_MEAL_TIME;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstMealTime().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		MealTime mealtime = fetchExc(ent.getOID()); 
		ent = (Entity)mealtime; 
		return mealtime.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((MealTime) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((MealTime) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static MealTime fetchExc(long oid) throws DBException{ 
		try{ 
			MealTime mealtime = new MealTime();
			PstMealTime pstMealTime = new PstMealTime(oid); 
			mealtime.setOID(oid);

			mealtime.setMealTime(pstMealTime.getString(FLD_MEAL_TIME));

			return mealtime; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMealTime(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(MealTime mealtime) throws DBException{ 
		try{ 
			PstMealTime pstMealTime = new PstMealTime(0);

			pstMealTime.setString(FLD_MEAL_TIME, mealtime.getMealTime());

			pstMealTime.insert(); 
			mealtime.setOID(pstMealTime.getlong(FLD_MEAL_TIME_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMealTime(0),DBException.UNKNOWN); 
		}
		return mealtime.getOID();
	}

	public static long updateExc(MealTime mealtime) throws DBException{ 
		try{ 
			if(mealtime.getOID() != 0){ 
				PstMealTime pstMealTime = new PstMealTime(mealtime.getOID());

				pstMealTime.setString(FLD_MEAL_TIME, mealtime.getMealTime());

				pstMealTime.update(); 
				return mealtime.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMealTime(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstMealTime pstMealTime = new PstMealTime(oid);
			pstMealTime.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMealTime(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_MEAL_TIME; 
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
				MealTime mealtime = new MealTime();
				resultToObject(rs, mealtime);
				lists.add(mealtime);
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

	public static void resultToObject(ResultSet rs, MealTime mealtime){
		try{
			mealtime.setOID(rs.getLong(PstMealTime.fieldNames[PstMealTime.FLD_MEAL_TIME_ID]));
			mealtime.setMealTime(rs.getString(PstMealTime.fieldNames[PstMealTime.FLD_MEAL_TIME]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long mealTimeId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_MEAL_TIME + " WHERE " + 
						PstMealTime.fieldNames[PstMealTime.FLD_MEAL_TIME_ID] + " = " + mealTimeId;

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
			String sql = "SELECT COUNT("+ PstMealTime.fieldNames[PstMealTime.FLD_MEAL_TIME_ID] + ") FROM " + TBL_HR_MEAL_TIME;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   MealTime mealtime = (MealTime)list.get(ls);
				   if(oid == mealtime.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
	/* This method used to find command where current data */
	public static int findLimitCommand(int start, int recordToGet, int vectSize){
		 int cmd = Command.LIST;
		 int mdl = vectSize % recordToGet;
		 vectSize = vectSize + (recordToGet - mdl);
		 if(start == 0)
			 cmd =  Command.FIRST;
		 else{
			 if(start == (vectSize-recordToGet))
				 cmd = Command.LAST;
			 else{
				 start = start + recordToGet;
				 if(start <= (vectSize - recordToGet)){
					 cmd = Command.NEXT;
					 System.out.println("next.......................");
				 }else{
					 start = start - recordToGet;
					 if(start > 0){
						 cmd = Command.PREV; 
						 System.out.println("prev.......................");
					 } 
				 }
			 } 
		 }

		 return cmd;
	}
}
