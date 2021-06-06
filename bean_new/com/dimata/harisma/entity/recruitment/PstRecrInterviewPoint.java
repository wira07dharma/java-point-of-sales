
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

public class PstRecrInterviewPoint extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_INTERVIEW_POINT = "HR_RECR_INTERVIEW_POINT";

	public static final  int FLD_RECR_INTERVIEW_POINT_ID = 0;
	public static final  int FLD_INTERVIEW_POINT = 1;
	public static final  int FLD_INTERVIEW_MARK = 2;

	public static final  String[] fieldNames = {
		"RECR_INTERVIEW_POINT_ID",
		"INTERVIEW_POINT",
		"INTERVIEW_MARK"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_INT,
		TYPE_STRING
	 }; 

	public PstRecrInterviewPoint(){
	}

	public PstRecrInterviewPoint(int i) throws DBException { 
		super(new PstRecrInterviewPoint()); 
	}

	public PstRecrInterviewPoint(String sOid) throws DBException { 
		super(new PstRecrInterviewPoint(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrInterviewPoint(long lOid) throws DBException { 
		super(new PstRecrInterviewPoint(0)); 
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
		return TBL_HR_RECR_INTERVIEW_POINT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrInterviewPoint().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrInterviewPoint recrinterviewpoint = fetchExc(ent.getOID()); 
		ent = (Entity)recrinterviewpoint; 
		return recrinterviewpoint.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrInterviewPoint) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrInterviewPoint) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrInterviewPoint fetchExc(long oid) throws DBException{ 
		try{ 
			RecrInterviewPoint recrinterviewpoint = new RecrInterviewPoint();
			PstRecrInterviewPoint pstRecrInterviewPoint = new PstRecrInterviewPoint(oid); 
			recrinterviewpoint.setOID(oid);

			recrinterviewpoint.setInterviewPoint(pstRecrInterviewPoint.getInt(FLD_INTERVIEW_POINT));
			recrinterviewpoint.setInterviewMark(pstRecrInterviewPoint.getString(FLD_INTERVIEW_MARK));

			return recrinterviewpoint; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewPoint(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrInterviewPoint recrinterviewpoint) throws DBException{ 
		try{ 
			PstRecrInterviewPoint pstRecrInterviewPoint = new PstRecrInterviewPoint(0);

			pstRecrInterviewPoint.setInt(FLD_INTERVIEW_POINT, recrinterviewpoint.getInterviewPoint());
			pstRecrInterviewPoint.setString(FLD_INTERVIEW_MARK, recrinterviewpoint.getInterviewMark());

			pstRecrInterviewPoint.insert(); 
			recrinterviewpoint.setOID(pstRecrInterviewPoint.getlong(FLD_RECR_INTERVIEW_POINT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewPoint(0),DBException.UNKNOWN); 
		}
		return recrinterviewpoint.getOID();
	}

	public static long updateExc(RecrInterviewPoint recrinterviewpoint) throws DBException{ 
		try{ 
			if(recrinterviewpoint.getOID() != 0){ 
				PstRecrInterviewPoint pstRecrInterviewPoint = new PstRecrInterviewPoint(recrinterviewpoint.getOID());

				pstRecrInterviewPoint.setInt(FLD_INTERVIEW_POINT, recrinterviewpoint.getInterviewPoint());
				pstRecrInterviewPoint.setString(FLD_INTERVIEW_MARK, recrinterviewpoint.getInterviewMark());

				pstRecrInterviewPoint.update(); 
				return recrinterviewpoint.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewPoint(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrInterviewPoint pstRecrInterviewPoint = new PstRecrInterviewPoint(oid);
			pstRecrInterviewPoint.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrInterviewPoint(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_INTERVIEW_POINT; 
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
				RecrInterviewPoint recrinterviewpoint = new RecrInterviewPoint();
				resultToObject(rs, recrinterviewpoint);
				lists.add(recrinterviewpoint);
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

	public static void resultToObject(ResultSet rs, RecrInterviewPoint recrinterviewpoint){
		try{
			recrinterviewpoint.setOID(rs.getLong(PstRecrInterviewPoint.fieldNames[PstRecrInterviewPoint.FLD_RECR_INTERVIEW_POINT_ID]));
			recrinterviewpoint.setInterviewPoint(rs.getInt(PstRecrInterviewPoint.fieldNames[PstRecrInterviewPoint.FLD_INTERVIEW_POINT]));
			recrinterviewpoint.setInterviewMark(rs.getString(PstRecrInterviewPoint.fieldNames[PstRecrInterviewPoint.FLD_INTERVIEW_MARK]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrInterviewPointId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_INTERVIEW_POINT + " WHERE " + 
						PstRecrInterviewPoint.fieldNames[PstRecrInterviewPoint.FLD_RECR_INTERVIEW_POINT_ID] + " = " + recrInterviewPointId;

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
			String sql = "SELECT COUNT("+ PstRecrInterviewPoint.fieldNames[PstRecrInterviewPoint.FLD_RECR_INTERVIEW_POINT_ID] + ") FROM " + TBL_HR_RECR_INTERVIEW_POINT;
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
			  	   RecrInterviewPoint recrinterviewpoint = (RecrInterviewPoint)list.get(ls);
				   if(oid == recrinterviewpoint.getOID())
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
