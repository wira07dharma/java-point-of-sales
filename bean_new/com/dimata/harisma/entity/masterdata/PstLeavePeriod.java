
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

package com.dimata.harisma.entity.masterdata; 

/* package java */ 
import java.sql.*
;import java.util.*
;
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
import com.dimata.harisma.entity.masterdata.*; 

public class PstLeavePeriod extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LEAVE_PERIOD = "HR_LEAVE_PERIOD";

	public static final  int FLD_LEAVE_PERIOD_ID = 0;
	public static final  int FLD_START_DATE = 1;
	public static final  int FLD_END_DATE = 2;
	public static final  int FLD_STATUS = 3;

	public static final  String[] fieldNames = {
		"LEAVE_PERIOD_ID",
		"START_DATE",
		"END_DATE",
		"STATUS"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_BOOL
	 };


    public static String[] statusStr   = {"History", "Valid"};

	public PstLeavePeriod(){
	}

	public PstLeavePeriod(int i) throws DBException { 
		super(new PstLeavePeriod()); 
	}

	public PstLeavePeriod(String sOid) throws DBException { 
		super(new PstLeavePeriod(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLeavePeriod(long lOid) throws DBException { 
		super(new PstLeavePeriod(0)); 
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
		return TBL_HR_LEAVE_PERIOD;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLeavePeriod().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		LeavePeriod leaveperiod = fetchExc(ent.getOID()); 
		ent = (Entity)leaveperiod; 
		return leaveperiod.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((LeavePeriod) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((LeavePeriod) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static LeavePeriod fetchExc(long oid) throws DBException{ 
		try{ 
			LeavePeriod leaveperiod = new LeavePeriod();
			PstLeavePeriod pstLeavePeriod = new PstLeavePeriod(oid); 
			leaveperiod.setOID(oid);

			leaveperiod.setStartDate(pstLeavePeriod.getDate(FLD_START_DATE));
			leaveperiod.setEndDate(pstLeavePeriod.getDate(FLD_END_DATE));
			leaveperiod.setStatus(pstLeavePeriod.getboolean(FLD_STATUS));

			return leaveperiod; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeavePeriod(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(LeavePeriod leaveperiod) throws DBException{ 
		try{ 
			PstLeavePeriod pstLeavePeriod = new PstLeavePeriod(0);

			pstLeavePeriod.setDate(FLD_START_DATE, leaveperiod.getStartDate());
			pstLeavePeriod.setDate(FLD_END_DATE, leaveperiod.getEndDate());
			pstLeavePeriod.setboolean(FLD_STATUS, leaveperiod.getStatus());

			pstLeavePeriod.insert(); 
			leaveperiod.setOID(pstLeavePeriod.getlong(FLD_LEAVE_PERIOD_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeavePeriod(0),DBException.UNKNOWN); 
		}
		return leaveperiod.getOID();
	}

	public static long updateExc(LeavePeriod leaveperiod) throws DBException{ 
		try{ 
			if(leaveperiod.getOID() != 0){ 
				PstLeavePeriod pstLeavePeriod = new PstLeavePeriod(leaveperiod.getOID());

				pstLeavePeriod.setDate(FLD_START_DATE, leaveperiod.getStartDate());
				pstLeavePeriod.setDate(FLD_END_DATE, leaveperiod.getEndDate());
				pstLeavePeriod.setboolean(FLD_STATUS, leaveperiod.getStatus());

				pstLeavePeriod.update(); 
				return leaveperiod.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeavePeriod(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLeavePeriod pstLeavePeriod = new PstLeavePeriod(oid);
			pstLeavePeriod.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeavePeriod(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LEAVE_PERIOD; 
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
				LeavePeriod leaveperiod = new LeavePeriod();
				resultToObject(rs, leaveperiod);
				lists.add(leaveperiod);
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

	private static void resultToObject(ResultSet rs, LeavePeriod leaveperiod){
		try{
			leaveperiod.setOID(rs.getLong(PstLeavePeriod.fieldNames[PstLeavePeriod.FLD_LEAVE_PERIOD_ID]));
			leaveperiod.setStartDate(rs.getDate(PstLeavePeriod.fieldNames[PstLeavePeriod.FLD_START_DATE]));
			leaveperiod.setEndDate(rs.getDate(PstLeavePeriod.fieldNames[PstLeavePeriod.FLD_END_DATE]));
			leaveperiod.setStatus(rs.getBoolean(PstLeavePeriod.fieldNames[PstLeavePeriod.FLD_STATUS]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long leavePeriodId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LEAVE_PERIOD + " WHERE " + 
						PstLeavePeriod.fieldNames[PstLeavePeriod.FLD_LEAVE_PERIOD_ID] + " = " + leavePeriodId;

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
			String sql = "SELECT COUNT("+ PstLeavePeriod.fieldNames[PstLeavePeriod.FLD_LEAVE_PERIOD_ID] + ") FROM " + TBL_HR_LEAVE_PERIOD;
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
			  	   LeavePeriod leaveperiod = (LeavePeriod)list.get(ls);
				   if(oid == leaveperiod.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
}
