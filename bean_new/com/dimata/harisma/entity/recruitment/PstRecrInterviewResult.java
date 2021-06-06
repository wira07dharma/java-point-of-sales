
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
import com.dimata.harisma.entity.recruitment.*; 

public class PstRecrInterviewResult extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_INTERVIEW_RESULT = "HR_RECR_INTERVIEW_RESULT";

	public static final  int FLD_RECR_INTERVIEW_RESULT_ID = 0;
	public static final  int FLD_RECR_INTERVIEW_POINT_ID = 1;
	public static final  int FLD_RECR_INTERVIEWER_FACTOR_ID = 2;
	public static final  int FLD_RECR_APPLICATION_ID = 3;

	public static final  String[] fieldNames = {
		"RECR_INTERVIEW_RESULT_ID",
		"RECR_INTERVIEW_POINT_ID",
		"RECR_INTERVIEWER_FACTOR_ID",
		"RECR_APPLICATION_ID"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG
	 }; 

	public PstRecrInterviewResult(){
	}

	public PstRecrInterviewResult(int i) throws DBException { 
		super(new PstRecrInterviewResult()); 
	}

	public PstRecrInterviewResult(String sOid) throws DBException { 
		super(new PstRecrInterviewResult(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrInterviewResult(long lOid) throws DBException { 
		super(new PstRecrInterviewResult(0)); 
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
		return TBL_HR_RECR_INTERVIEW_RESULT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrInterviewResult().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrInterviewResult recrinterviewresult = fetchExc(ent.getOID()); 
		ent = (Entity)recrinterviewresult; 
		return recrinterviewresult.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrInterviewResult) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrInterviewResult) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrInterviewResult fetchExc(long oid) throws DBException{ 
		try{ 
			RecrInterviewResult recrinterviewresult = new RecrInterviewResult();
			PstRecrInterviewResult pstRecrInterviewResult = new PstRecrInterviewResult(oid); 
			recrinterviewresult.setOID(oid);

			recrinterviewresult.setRecrInterviewPointId(pstRecrInterviewResult.getlong(FLD_RECR_INTERVIEW_POINT_ID));
			recrinterviewresult.setRecrInterviewerFactorId(pstRecrInterviewResult.getlong(FLD_RECR_INTERVIEWER_FACTOR_ID));
			recrinterviewresult.setRecrApplicationId(pstRecrInterviewResult.getlong(FLD_RECR_APPLICATION_ID));

			return recrinterviewresult; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewResult(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrInterviewResult recrinterviewresult) throws DBException{ 
		try{ 
			PstRecrInterviewResult pstRecrInterviewResult = new PstRecrInterviewResult(0);

			pstRecrInterviewResult.setLong(FLD_RECR_INTERVIEW_POINT_ID, recrinterviewresult.getRecrInterviewPointId());
			pstRecrInterviewResult.setLong(FLD_RECR_INTERVIEWER_FACTOR_ID, recrinterviewresult.getRecrInterviewerFactorId());
			pstRecrInterviewResult.setLong(FLD_RECR_APPLICATION_ID, recrinterviewresult.getRecrApplicationId());

			pstRecrInterviewResult.insert(); 
			recrinterviewresult.setOID(pstRecrInterviewResult.getlong(FLD_RECR_INTERVIEW_RESULT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewResult(0),DBException.UNKNOWN); 
		}
		return recrinterviewresult.getOID();
	}

	public static long updateExc(RecrInterviewResult recrinterviewresult) throws DBException{ 
		try{ 
			if(recrinterviewresult.getOID() != 0){ 
				PstRecrInterviewResult pstRecrInterviewResult = new PstRecrInterviewResult(recrinterviewresult.getOID());

				pstRecrInterviewResult.setLong(FLD_RECR_INTERVIEW_POINT_ID, recrinterviewresult.getRecrInterviewPointId());
				pstRecrInterviewResult.setLong(FLD_RECR_INTERVIEWER_FACTOR_ID, recrinterviewresult.getRecrInterviewerFactorId());
				pstRecrInterviewResult.setLong(FLD_RECR_APPLICATION_ID, recrinterviewresult.getRecrApplicationId());

				pstRecrInterviewResult.update(); 
				return recrinterviewresult.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewResult(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrInterviewResult pstRecrInterviewResult = new PstRecrInterviewResult(oid);
			pstRecrInterviewResult.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewResult(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_INTERVIEW_RESULT; 
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
				RecrInterviewResult recrinterviewresult = new RecrInterviewResult();
				resultToObject(rs, recrinterviewresult);
				lists.add(recrinterviewresult);
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

	public static void resultToObject(ResultSet rs, RecrInterviewResult recrinterviewresult){
		try{
			recrinterviewresult.setOID(rs.getLong(PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_INTERVIEW_RESULT_ID]));
			recrinterviewresult.setRecrInterviewPointId(rs.getLong(PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_INTERVIEW_POINT_ID]));
			recrinterviewresult.setRecrInterviewerFactorId(rs.getLong(PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_INTERVIEWER_FACTOR_ID]));
			recrinterviewresult.setRecrApplicationId(rs.getLong(PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_APPLICATION_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrInterviewResultId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_INTERVIEW_RESULT + " WHERE " + 
						PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_INTERVIEW_RESULT_ID] + " = " + recrInterviewResultId;

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
			String sql = "SELECT COUNT("+ PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_INTERVIEW_RESULT_ID] + ") FROM " + TBL_HR_RECR_INTERVIEW_RESULT;
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
			  	   RecrInterviewResult recrinterviewresult = (RecrInterviewResult)list.get(ls);
				   if(oid == recrinterviewresult.getOID())
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
            String sql = " DELETE FROM "+PstRecrInterviewResult.TBL_HR_RECR_INTERVIEW_RESULT+
                         " WHERE "+PstRecrInterviewResult.fieldNames[PstRecrInterviewResult.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
