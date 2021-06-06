
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

package com.dimata.harisma.entity.employee; 

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
import com.dimata.harisma.entity.employee.*; 

public class PstTrainingHistory extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_TRAINING_HISTORY = "HR_TRAINING_HISTORY";

	public static final  int FLD_TRAINING_HISTORY_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_TRAINING_ID = 2;
	public static final  int FLD_START_DATE = 3;
	public static final  int FLD_END_DATE = 4;
	public static final  int FLD_TRAINER = 5;
	public static final  int FLD_REMARK = 6;
    public static final  int FLD_DURATION = 7;
    public static final  int FLD_PRESENCE = 8;
    public static final  int FLD_START_TIME = 9;


	public static final  String[] fieldNames = {
		"TRAINING_HISTORY_ID",
		"EMPLOYEE_ID",
		"TRAINING_ID",
		"START_DATE",
		"END_DATE",
		"TRAINER",
		"REMARK",
        "DURATION",
        "PRESENCE",
        "START_TIME"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE
	 }; 

	public PstTrainingHistory(){
	}

	public PstTrainingHistory(int i) throws DBException { 
		super(new PstTrainingHistory()); 
	}

	public PstTrainingHistory(String sOid) throws DBException { 
		super(new PstTrainingHistory(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstTrainingHistory(long lOid) throws DBException { 
		super(new PstTrainingHistory(0)); 
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
		return TBL_HR_TRAINING_HISTORY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstTrainingHistory().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		TrainingHistory traininghistory = fetchExc(ent.getOID()); 
		ent = (Entity)traininghistory; 
		return traininghistory.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((TrainingHistory) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((TrainingHistory) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static TrainingHistory fetchExc(long oid) throws DBException{ 
		try{ 
			TrainingHistory traininghistory = new TrainingHistory();
			PstTrainingHistory pstTrainingHistory = new PstTrainingHistory(oid); 
			traininghistory.setOID(oid);

			traininghistory.setEmployeeId(pstTrainingHistory.getlong(FLD_EMPLOYEE_ID));
			traininghistory.setTrainingId(pstTrainingHistory.getlong(FLD_TRAINING_ID));
			traininghistory.setStartDate(pstTrainingHistory.getDate(FLD_START_DATE));
			traininghistory.setEndDate(pstTrainingHistory.getDate(FLD_END_DATE));
			traininghistory.setTrainer(pstTrainingHistory.getString(FLD_TRAINER));
			traininghistory.setRemark(pstTrainingHistory.getString(FLD_REMARK));
            traininghistory.setDuration(pstTrainingHistory.getInt(FLD_DURATION));
            traininghistory.setPresence(pstTrainingHistory.getInt(FLD_PRESENCE));
            traininghistory.setStartTime(pstTrainingHistory.getDate(FLD_START_TIME));

			return traininghistory; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingHistory(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(TrainingHistory traininghistory) throws DBException{ 
		try{ 
			PstTrainingHistory pstTrainingHistory = new PstTrainingHistory(0);

			pstTrainingHistory.setLong(FLD_EMPLOYEE_ID, traininghistory.getEmployeeId());
			pstTrainingHistory.setLong(FLD_TRAINING_ID, traininghistory.getTrainingId());
			pstTrainingHistory.setDate(FLD_START_DATE, traininghistory.getStartDate());
			pstTrainingHistory.setDate(FLD_END_DATE, traininghistory.getEndDate());
			pstTrainingHistory.setString(FLD_TRAINER, traininghistory.getTrainer());
			pstTrainingHistory.setString(FLD_REMARK, traininghistory.getRemark());
            pstTrainingHistory.setInt(FLD_DURATION, traininghistory.getDuration());
            pstTrainingHistory.setInt(FLD_PRESENCE, traininghistory.getPresence());
            pstTrainingHistory.setDate(FLD_START_TIME, traininghistory.getStartTime());

			pstTrainingHistory.insert(); 
			traininghistory.setOID(pstTrainingHistory.getlong(FLD_TRAINING_HISTORY_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingHistory(0),DBException.UNKNOWN); 
		}
		return traininghistory.getOID();
	}

	public static long updateExc(TrainingHistory traininghistory) throws DBException{ 
		try{ 
			if(traininghistory.getOID() != 0){ 
				PstTrainingHistory pstTrainingHistory = new PstTrainingHistory(traininghistory.getOID());

				pstTrainingHistory.setLong(FLD_EMPLOYEE_ID, traininghistory.getEmployeeId());
				pstTrainingHistory.setLong(FLD_TRAINING_ID, traininghistory.getTrainingId());
				pstTrainingHistory.setDate(FLD_START_DATE, traininghistory.getStartDate());
				pstTrainingHistory.setDate(FLD_END_DATE, traininghistory.getEndDate());
				pstTrainingHistory.setString(FLD_TRAINER, traininghistory.getTrainer());
				pstTrainingHistory.setString(FLD_REMARK, traininghistory.getRemark());
	            pstTrainingHistory.setInt(FLD_DURATION, traininghistory.getDuration());
	            pstTrainingHistory.setInt(FLD_PRESENCE, traininghistory.getPresence());
	            pstTrainingHistory.setDate(FLD_START_TIME, traininghistory.getStartTime());

				pstTrainingHistory.update(); 
				return traininghistory.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingHistory(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstTrainingHistory pstTrainingHistory = new PstTrainingHistory(oid);
			pstTrainingHistory.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingHistory(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_HISTORY; 
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
                	if(limitStart == 0 && recordToGet == 0)
						sql = sql + ""; 
					else 
						sql = sql + " LIMIT " + limitStart + ","+ recordToGet ; 
			}			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				TrainingHistory traininghistory = new TrainingHistory();
				resultToObject(rs, traininghistory);
				lists.add(traininghistory);
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

	public static void resultToObject(ResultSet rs, TrainingHistory traininghistory){
		try{
			traininghistory.setOID(rs.getLong(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_TRAINING_HISTORY_ID]));
			traininghistory.setEmployeeId(rs.getLong(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_EMPLOYEE_ID]));
			traininghistory.setTrainingId(rs.getLong(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_TRAINING_ID]));
			traininghistory.setStartDate(rs.getDate(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_START_DATE]));
			traininghistory.setEndDate(rs.getDate(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_END_DATE]));
			traininghistory.setTrainer(rs.getString(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_TRAINER]));
			traininghistory.setRemark(rs.getString(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_REMARK]));
            traininghistory.setDuration(rs.getInt(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_DURATION]));
            traininghistory.setPresence(rs.getInt(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_PRESENCE]));

            Date dt = rs.getDate(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_START_TIME]);
            Date tm = rs.getTime(PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_START_TIME]);
            Date start = new Date(dt.getYear(),dt.getMonth(),dt.getDate(),tm.getHours(),tm.getMinutes());
            traininghistory.setStartTime(start);

		}catch(Exception e){ }
	}

	public static boolean checkOID(long trainingHistoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_HISTORY + " WHERE " + 
						PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_TRAINING_HISTORY_ID] + " = " + trainingHistoryId;

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
			String sql = "SELECT COUNT("+ PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_TRAINING_HISTORY_ID] + ") FROM " + TBL_HR_TRAINING_HISTORY;
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
			  	   TrainingHistory traininghistory = (TrainingHistory)list.get(ls);
				   if(oid == traininghistory.getOID())
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
