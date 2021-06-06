
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

package com.dimata.harisma.entity.recruitment; 

/* package java */ 
import java.sql.*
;import java.util.*
;
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

public class PstRecrWorkHistory extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_WORK_HISTORY = "HR_RECR_WORK_HISTORY";

	public static final  int FLD_RECR_WORK_HISTORY_ID = 0;
	public static final  int FLD_RECR_APPLICATION_ID = 1;
	public static final  int FLD_POSITION = 2;
	public static final  int FLD_START_DATE = 3;
	public static final  int FLD_END_DATE = 4;
	public static final  int FLD_DUTIES = 5;
	public static final  int FLD_COMM_SALARY = 6;
	public static final  int FLD_LAST_SALARY = 7;
	public static final  int FLD_COMPANY_NAME = 8;
	public static final  int FLD_COMPANY_ADDRESS = 9;
	public static final  int FLD_COMPANY_PHONE = 10;
	public static final  int FLD_COMPANY_NATURE = 11;
	public static final  int FLD_COMPANY_SPV = 12;
	public static final  int FLD_LEAVE_REASON = 13;

	public static final  String[] fieldNames = {
		"RECR_WORK_HISTORY_ID",
		"RECR_APPLICATION_ID",
		"POSITION",
		"START_DATE",
		"END_DATE",
		"DUTIES",
		"COMM_SALARY",
		"LAST_SALARY",
		"COMPANY_NAME",
		"COMPANY_ADDRESS",
		"COMPANY_PHONE",
		"COMPANY_NATURE",
		"COMPANY_SPV",
		"LEAVE_REASON"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_INT,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstRecrWorkHistory(){
	}

	public PstRecrWorkHistory(int i) throws DBException { 
		super(new PstRecrWorkHistory()); 
	}

	public PstRecrWorkHistory(String sOid) throws DBException { 
		super(new PstRecrWorkHistory(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrWorkHistory(long lOid) throws DBException { 
		super(new PstRecrWorkHistory(0)); 
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
		return TBL_HR_RECR_WORK_HISTORY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrWorkHistory().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrWorkHistory recrworkhistory = fetchExc(ent.getOID()); 
		ent = (Entity)recrworkhistory; 
		return recrworkhistory.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrWorkHistory) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrWorkHistory) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrWorkHistory fetchExc(long oid) throws DBException{ 
		try{ 
			RecrWorkHistory recrworkhistory = new RecrWorkHistory();
			PstRecrWorkHistory pstRecrWorkHistory = new PstRecrWorkHistory(oid); 
			recrworkhistory.setOID(oid);

			recrworkhistory.setRecrApplicationId(pstRecrWorkHistory.getlong(FLD_RECR_APPLICATION_ID));
			recrworkhistory.setPosition(pstRecrWorkHistory.getString(FLD_POSITION));
			recrworkhistory.setStartDate(pstRecrWorkHistory.getDate(FLD_START_DATE));
			recrworkhistory.setEndDate(pstRecrWorkHistory.getDate(FLD_END_DATE));
			recrworkhistory.setDuties(pstRecrWorkHistory.getString(FLD_DUTIES));
			recrworkhistory.setCommSalary(pstRecrWorkHistory.getInt(FLD_COMM_SALARY));
			recrworkhistory.setLastSalary(pstRecrWorkHistory.getInt(FLD_LAST_SALARY));
			recrworkhistory.setCompanyName(pstRecrWorkHistory.getString(FLD_COMPANY_NAME));
			recrworkhistory.setCompanyAddress(pstRecrWorkHistory.getString(FLD_COMPANY_ADDRESS));
			recrworkhistory.setCompanyPhone(pstRecrWorkHistory.getString(FLD_COMPANY_PHONE));
			recrworkhistory.setCompanyNature(pstRecrWorkHistory.getString(FLD_COMPANY_NATURE));
			recrworkhistory.setCompanySpv(pstRecrWorkHistory.getString(FLD_COMPANY_SPV));
			recrworkhistory.setLeaveReason(pstRecrWorkHistory.getString(FLD_LEAVE_REASON));

			return recrworkhistory; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrWorkHistory(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrWorkHistory recrworkhistory) throws DBException{ 
		try{ 
			PstRecrWorkHistory pstRecrWorkHistory = new PstRecrWorkHistory(0);

			pstRecrWorkHistory.setLong(FLD_RECR_APPLICATION_ID, recrworkhistory.getRecrApplicationId());
			pstRecrWorkHistory.setString(FLD_POSITION, recrworkhistory.getPosition());
			pstRecrWorkHistory.setDate(FLD_START_DATE, recrworkhistory.getStartDate());
			pstRecrWorkHistory.setDate(FLD_END_DATE, recrworkhistory.getEndDate());
			pstRecrWorkHistory.setString(FLD_DUTIES, recrworkhistory.getDuties());
			pstRecrWorkHistory.setInt(FLD_COMM_SALARY, recrworkhistory.getCommSalary());
			pstRecrWorkHistory.setInt(FLD_LAST_SALARY, recrworkhistory.getLastSalary());
			pstRecrWorkHistory.setString(FLD_COMPANY_NAME, recrworkhistory.getCompanyName());
			pstRecrWorkHistory.setString(FLD_COMPANY_ADDRESS, recrworkhistory.getCompanyAddress());
			pstRecrWorkHistory.setString(FLD_COMPANY_PHONE, recrworkhistory.getCompanyPhone());
			pstRecrWorkHistory.setString(FLD_COMPANY_NATURE, recrworkhistory.getCompanyNature());
			pstRecrWorkHistory.setString(FLD_COMPANY_SPV, recrworkhistory.getCompanySpv());
			pstRecrWorkHistory.setString(FLD_LEAVE_REASON, recrworkhistory.getLeaveReason());

			pstRecrWorkHistory.insert(); 
			recrworkhistory.setOID(pstRecrWorkHistory.getlong(FLD_RECR_WORK_HISTORY_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrWorkHistory(0),DBException.UNKNOWN); 
		}
		return recrworkhistory.getOID();
	}

	public static long updateExc(RecrWorkHistory recrworkhistory) throws DBException{ 
		try{ 
			if(recrworkhistory.getOID() != 0){ 
				PstRecrWorkHistory pstRecrWorkHistory = new PstRecrWorkHistory(recrworkhistory.getOID());

				pstRecrWorkHistory.setLong(FLD_RECR_APPLICATION_ID, recrworkhistory.getRecrApplicationId());
				pstRecrWorkHistory.setString(FLD_POSITION, recrworkhistory.getPosition());
				pstRecrWorkHistory.setDate(FLD_START_DATE, recrworkhistory.getStartDate());
				pstRecrWorkHistory.setDate(FLD_END_DATE, recrworkhistory.getEndDate());
				pstRecrWorkHistory.setString(FLD_DUTIES, recrworkhistory.getDuties());
				pstRecrWorkHistory.setInt(FLD_COMM_SALARY, recrworkhistory.getCommSalary());
				pstRecrWorkHistory.setInt(FLD_LAST_SALARY, recrworkhistory.getLastSalary());
				pstRecrWorkHistory.setString(FLD_COMPANY_NAME, recrworkhistory.getCompanyName());
				pstRecrWorkHistory.setString(FLD_COMPANY_ADDRESS, recrworkhistory.getCompanyAddress());
				pstRecrWorkHistory.setString(FLD_COMPANY_PHONE, recrworkhistory.getCompanyPhone());
				pstRecrWorkHistory.setString(FLD_COMPANY_NATURE, recrworkhistory.getCompanyNature());
				pstRecrWorkHistory.setString(FLD_COMPANY_SPV, recrworkhistory.getCompanySpv());
				pstRecrWorkHistory.setString(FLD_LEAVE_REASON, recrworkhistory.getLeaveReason());

				pstRecrWorkHistory.update(); 
				return recrworkhistory.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrWorkHistory(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrWorkHistory pstRecrWorkHistory = new PstRecrWorkHistory(oid);
			pstRecrWorkHistory.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrWorkHistory(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_WORK_HISTORY; 
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
				RecrWorkHistory recrworkhistory = new RecrWorkHistory();
				resultToObject(rs, recrworkhistory);
				lists.add(recrworkhistory);
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

	public static void resultToObject(ResultSet rs, RecrWorkHistory recrworkhistory){
		try{
			recrworkhistory.setOID(rs.getLong(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_RECR_WORK_HISTORY_ID]));
			recrworkhistory.setRecrApplicationId(rs.getLong(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_RECR_APPLICATION_ID]));
			recrworkhistory.setPosition(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_POSITION]));
			recrworkhistory.setStartDate(rs.getDate(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_START_DATE]));
			recrworkhistory.setEndDate(rs.getDate(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_END_DATE]));
			recrworkhistory.setDuties(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_DUTIES]));
			recrworkhistory.setCommSalary(rs.getInt(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_COMM_SALARY]));
			recrworkhistory.setLastSalary(rs.getInt(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_LAST_SALARY]));
			recrworkhistory.setCompanyName(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_COMPANY_NAME]));
			recrworkhistory.setCompanyAddress(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_COMPANY_ADDRESS]));
			recrworkhistory.setCompanyPhone(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_COMPANY_PHONE]));
			recrworkhistory.setCompanyNature(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_COMPANY_NATURE]));
			recrworkhistory.setCompanySpv(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_COMPANY_SPV]));
			recrworkhistory.setLeaveReason(rs.getString(PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_LEAVE_REASON]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrWorkHistoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_WORK_HISTORY + " WHERE " + 
						PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_RECR_WORK_HISTORY_ID] + " = " + recrWorkHistoryId;

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
			String sql = "SELECT COUNT("+ PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_RECR_WORK_HISTORY_ID] + ") FROM " + TBL_HR_RECR_WORK_HISTORY;
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
			  	   RecrWorkHistory recrworkhistory = (RecrWorkHistory)list.get(ls);
				   if(oid == recrworkhistory.getOID())
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

    public static long deleteByRecrApplication(long raOID)
    {
    	try{
            String sql = " DELETE FROM "+PstRecrWorkHistory.TBL_HR_RECR_WORK_HISTORY+
                         " WHERE "+PstRecrWorkHistory.fieldNames[PstRecrWorkHistory.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
