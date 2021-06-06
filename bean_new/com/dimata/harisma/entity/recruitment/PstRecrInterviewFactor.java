
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

public class PstRecrInterviewFactor extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_INTERVIEW_FACTOR = "HR_RECR_INTERVIEW_FACTOR";

	public static final  int FLD_RECR_INTERVIEW_FACTOR_ID = 0;
	public static final  int FLD_INTERVIEW_FACTOR = 1;

	public static final  String[] fieldNames = {
		"RECR_INTERVIEW_FACTOR_ID",
		"INTERVIEW_FACTOR"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstRecrInterviewFactor(){
	}

	public PstRecrInterviewFactor(int i) throws DBException { 
		super(new PstRecrInterviewFactor()); 
	}

	public PstRecrInterviewFactor(String sOid) throws DBException { 
		super(new PstRecrInterviewFactor(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrInterviewFactor(long lOid) throws DBException { 
		super(new PstRecrInterviewFactor(0)); 
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
		return TBL_HR_RECR_INTERVIEW_FACTOR;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrInterviewFactor().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrInterviewFactor recrinterviewfactor = fetchExc(ent.getOID()); 
		ent = (Entity)recrinterviewfactor; 
		return recrinterviewfactor.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrInterviewFactor) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrInterviewFactor) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrInterviewFactor fetchExc(long oid) throws DBException{ 
		try{ 
			RecrInterviewFactor recrinterviewfactor = new RecrInterviewFactor();
			PstRecrInterviewFactor pstRecrInterviewFactor = new PstRecrInterviewFactor(oid); 
			recrinterviewfactor.setOID(oid);

			recrinterviewfactor.setInterviewFactor(pstRecrInterviewFactor.getString(FLD_INTERVIEW_FACTOR));

			return recrinterviewfactor; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewFactor(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrInterviewFactor recrinterviewfactor) throws DBException{ 
		try{ 
			PstRecrInterviewFactor pstRecrInterviewFactor = new PstRecrInterviewFactor(0);

			pstRecrInterviewFactor.setString(FLD_INTERVIEW_FACTOR, recrinterviewfactor.getInterviewFactor());

			pstRecrInterviewFactor.insert(); 
			recrinterviewfactor.setOID(pstRecrInterviewFactor.getlong(FLD_RECR_INTERVIEW_FACTOR_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewFactor(0),DBException.UNKNOWN); 
		}
		return recrinterviewfactor.getOID();
	}

	public static long updateExc(RecrInterviewFactor recrinterviewfactor) throws DBException{ 
		try{ 
			if(recrinterviewfactor.getOID() != 0){ 
				PstRecrInterviewFactor pstRecrInterviewFactor = new PstRecrInterviewFactor(recrinterviewfactor.getOID());

				pstRecrInterviewFactor.setString(FLD_INTERVIEW_FACTOR, recrinterviewfactor.getInterviewFactor());

				pstRecrInterviewFactor.update(); 
				return recrinterviewfactor.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewFactor(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrInterviewFactor pstRecrInterviewFactor = new PstRecrInterviewFactor(oid);
			pstRecrInterviewFactor.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewFactor(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_INTERVIEW_FACTOR; 
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
				RecrInterviewFactor recrinterviewfactor = new RecrInterviewFactor();
				resultToObject(rs, recrinterviewfactor);
				lists.add(recrinterviewfactor);
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

	public static void resultToObject(ResultSet rs, RecrInterviewFactor recrinterviewfactor){
		try{
			recrinterviewfactor.setOID(rs.getLong(PstRecrInterviewFactor.fieldNames[PstRecrInterviewFactor.FLD_RECR_INTERVIEW_FACTOR_ID]));
			recrinterviewfactor.setInterviewFactor(rs.getString(PstRecrInterviewFactor.fieldNames[PstRecrInterviewFactor.FLD_INTERVIEW_FACTOR]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrInterviewFactorId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_INTERVIEW_FACTOR + " WHERE " + 
						PstRecrInterviewFactor.fieldNames[PstRecrInterviewFactor.FLD_RECR_INTERVIEW_FACTOR_ID] + " = " + recrInterviewFactorId;

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
			String sql = "SELECT COUNT("+ PstRecrInterviewFactor.fieldNames[PstRecrInterviewFactor.FLD_RECR_INTERVIEW_FACTOR_ID] + ") FROM " + TBL_HR_RECR_INTERVIEW_FACTOR;
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
			  	   RecrInterviewFactor recrinterviewfactor = (RecrInterviewFactor)list.get(ls);
				   if(oid == recrinterviewfactor.getOID())
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
