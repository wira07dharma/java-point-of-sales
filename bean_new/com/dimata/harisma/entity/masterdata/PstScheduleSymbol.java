
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

package com.dimata.harisma.entity.masterdata; 

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
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.attendance.*;

public class PstScheduleSymbol extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_SCHEDULE_SYMBOL = "HR_SCHEDULE_SYMBOL";

	public static final  int FLD_SCHEDULE_ID = 0;
	public static final  int FLD_SCHEDULE_CATEGORY_ID = 1;
	public static final  int FLD_SCHEDULE = 2;
	public static final  int FLD_SYMBOL = 3;
	public static final  int FLD_TIME_IN = 4;
	public static final  int FLD_TIME_OUT = 5;

	public static final  String[] fieldNames = {
		"SCHEDULE_ID",
		"SCHEDULE_CATEGORY_ID",
		"SCHEDULE",
		"SYMBOL",
		"TIME_IN",
		"TIME_OUT"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_DATE
	 }; 

	public PstScheduleSymbol(){
	}

	public PstScheduleSymbol(int i) throws DBException { 
		super(new PstScheduleSymbol()); 
	}

	public PstScheduleSymbol(String sOid) throws DBException { 
		super(new PstScheduleSymbol(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstScheduleSymbol(long lOid) throws DBException { 
		super(new PstScheduleSymbol(0)); 
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
		return TBL_HR_SCHEDULE_SYMBOL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstScheduleSymbol().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ScheduleSymbol schedulesymbol = fetchExc(ent.getOID()); 
		ent = (Entity)schedulesymbol; 
		return schedulesymbol.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ScheduleSymbol) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ScheduleSymbol) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static ScheduleSymbol fetchExc(long oid) throws DBException{ 
		try{ 
			ScheduleSymbol schedulesymbol = new ScheduleSymbol();
			PstScheduleSymbol pstScheduleSymbol = new PstScheduleSymbol(oid); 
			schedulesymbol.setOID(oid);

			schedulesymbol.setScheduleCategoryId(pstScheduleSymbol.getlong(FLD_SCHEDULE_CATEGORY_ID));
			schedulesymbol.setSchedule(pstScheduleSymbol.getString(FLD_SCHEDULE));
			schedulesymbol.setSymbol(pstScheduleSymbol.getString(FLD_SYMBOL));
			schedulesymbol.setTimeIn(pstScheduleSymbol.getDate(FLD_TIME_IN));
			schedulesymbol.setTimeOut(pstScheduleSymbol.getDate(FLD_TIME_OUT));

			return schedulesymbol; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleSymbol(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ScheduleSymbol schedulesymbol) throws DBException{ 
		try{ 
			PstScheduleSymbol pstScheduleSymbol = new PstScheduleSymbol(0);

			pstScheduleSymbol.setLong(FLD_SCHEDULE_CATEGORY_ID, schedulesymbol.getScheduleCategoryId());
			pstScheduleSymbol.setString(FLD_SCHEDULE, schedulesymbol.getSchedule());
			pstScheduleSymbol.setString(FLD_SYMBOL, schedulesymbol.getSymbol());
			pstScheduleSymbol.setDate(FLD_TIME_IN, schedulesymbol.getTimeIn());
			pstScheduleSymbol.setDate(FLD_TIME_OUT, schedulesymbol.getTimeOut());

			pstScheduleSymbol.insert(); 
			schedulesymbol.setOID(pstScheduleSymbol.getlong(FLD_SCHEDULE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleSymbol(0),DBException.UNKNOWN); 
		}
		return schedulesymbol.getOID();
	}

	public static long updateExc(ScheduleSymbol schedulesymbol) throws DBException{ 
		try{ 
			if(schedulesymbol.getOID() != 0){ 
				PstScheduleSymbol pstScheduleSymbol = new PstScheduleSymbol(schedulesymbol.getOID());

				pstScheduleSymbol.setLong(FLD_SCHEDULE_CATEGORY_ID, schedulesymbol.getScheduleCategoryId());
				pstScheduleSymbol.setString(FLD_SCHEDULE, schedulesymbol.getSchedule());
				pstScheduleSymbol.setString(FLD_SYMBOL, schedulesymbol.getSymbol());
				pstScheduleSymbol.setDate(FLD_TIME_IN, schedulesymbol.getTimeIn());
				pstScheduleSymbol.setDate(FLD_TIME_OUT, schedulesymbol.getTimeOut());

				pstScheduleSymbol.update(); 
				return schedulesymbol.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleSymbol(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstScheduleSymbol pstScheduleSymbol = new PstScheduleSymbol(oid);
			pstScheduleSymbol.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleSymbol(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_SCHEDULE_SYMBOL; 
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
				ScheduleSymbol schedulesymbol = new ScheduleSymbol();
				resultToObject(rs, schedulesymbol);
				lists.add(schedulesymbol);
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

	private static void resultToObject(ResultSet rs, ScheduleSymbol schedulesymbol){
		try{
			schedulesymbol.setOID(rs.getLong(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]));
			schedulesymbol.setScheduleCategoryId(rs.getLong(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID]));
			schedulesymbol.setSchedule(rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE]));
			schedulesymbol.setSymbol(rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL]));
			//schedulesymbol.setTimeIn(rs.getDate(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN]));
			//schedulesymbol.setTimeOut(rs.getDate(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT]));
			schedulesymbol.setTimeIn(rs.getTime(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN]));
			schedulesymbol.setTimeOut(rs.getTime(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long scheduleId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_SCHEDULE_SYMBOL + " WHERE " + 
						PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " = " + scheduleId;

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
			String sql = "SELECT COUNT("+ PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + ") FROM " + TBL_HR_SCHEDULE_SYMBOL;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause,String order){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   ScheduleSymbol schedulesymbol = (ScheduleSymbol)list.get(ls);
				   if(oid == schedulesymbol.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


	public static boolean checkScheduleCategory(long scheduleCategoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_SCHEDULE_SYMBOL + " WHERE " + 
						PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + " = '" + scheduleCategoryId + "'";

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


    public static boolean checkMaster(long oid)
    {
    	if(PstEmpSchedule.checkScheduleSymbol(oid))
            return true;
    	else
            return false;
    }


        public static int findLimitCommand(int start, int recordToGet, int vectSize)
    {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
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
