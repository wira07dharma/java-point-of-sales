
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

public class PstRecrEducation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_EDUCATION = "HR_RECR_EDUCATION";

	public static final  int FLD_RECR_EDUCATION_ID = 0;
	public static final  int FLD_RECR_APPLICATION_ID = 1;
	public static final  int FLD_EDUCATION_ID = 2;
	public static final  int FLD_START_DATE = 3;
	public static final  int FLD_END_DATE = 4;
	public static final  int FLD_STUDY = 5;
	public static final  int FLD_DEGREE = 6;

	public static final  String[] fieldNames = {
		"RECR_EDUCATION_ID",
		"RECR_APPLICATION_ID",
		"EDUCATION_ID",
		"START_DATE",
		"END_DATE",
		"STUDY",
		"DEGREE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstRecrEducation(){
	}

	public PstRecrEducation(int i) throws DBException { 
		super(new PstRecrEducation()); 
	}

	public PstRecrEducation(String sOid) throws DBException { 
		super(new PstRecrEducation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrEducation(long lOid) throws DBException { 
		super(new PstRecrEducation(0)); 
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
		return TBL_HR_RECR_EDUCATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrEducation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrEducation recreducation = fetchExc(ent.getOID()); 
		ent = (Entity)recreducation; 
		return recreducation.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrEducation) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrEducation) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrEducation fetchExc(long oid) throws DBException{ 
		try{ 
			RecrEducation recreducation = new RecrEducation();
			PstRecrEducation pstRecrEducation = new PstRecrEducation(oid); 
			recreducation.setOID(oid);

			recreducation.setRecrApplicationId(pstRecrEducation.getlong(FLD_RECR_APPLICATION_ID));
			recreducation.setEducationId(pstRecrEducation.getlong(FLD_EDUCATION_ID));
			recreducation.setStartDate(pstRecrEducation.getDate(FLD_START_DATE));
			recreducation.setEndDate(pstRecrEducation.getDate(FLD_END_DATE));
			recreducation.setStudy(pstRecrEducation.getString(FLD_STUDY));
			recreducation.setDegree(pstRecrEducation.getString(FLD_DEGREE));

			return recreducation; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrEducation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrEducation recreducation) throws DBException{ 
		try{ 
			PstRecrEducation pstRecrEducation = new PstRecrEducation(0);

			pstRecrEducation.setLong(FLD_RECR_APPLICATION_ID, recreducation.getRecrApplicationId());
			pstRecrEducation.setLong(FLD_EDUCATION_ID, recreducation.getEducationId());
			pstRecrEducation.setDate(FLD_START_DATE, recreducation.getStartDate());
			pstRecrEducation.setDate(FLD_END_DATE, recreducation.getEndDate());
			pstRecrEducation.setString(FLD_STUDY, recreducation.getStudy());
			pstRecrEducation.setString(FLD_DEGREE, recreducation.getDegree());

			pstRecrEducation.insert(); 
			recreducation.setOID(pstRecrEducation.getlong(FLD_RECR_EDUCATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrEducation(0),DBException.UNKNOWN); 
		}
		return recreducation.getOID();
	}

	public static long updateExc(RecrEducation recreducation) throws DBException{ 
		try{ 
			if(recreducation.getOID() != 0){ 
				PstRecrEducation pstRecrEducation = new PstRecrEducation(recreducation.getOID());

				pstRecrEducation.setLong(FLD_RECR_APPLICATION_ID, recreducation.getRecrApplicationId());
				pstRecrEducation.setLong(FLD_EDUCATION_ID, recreducation.getEducationId());
				pstRecrEducation.setDate(FLD_START_DATE, recreducation.getStartDate());
				pstRecrEducation.setDate(FLD_END_DATE, recreducation.getEndDate());
				pstRecrEducation.setString(FLD_STUDY, recreducation.getStudy());
				pstRecrEducation.setString(FLD_DEGREE, recreducation.getDegree());

				pstRecrEducation.update(); 
				return recreducation.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrEducation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrEducation pstRecrEducation = new PstRecrEducation(oid);
			pstRecrEducation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrEducation(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_EDUCATION; 
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
				RecrEducation recreducation = new RecrEducation();
				resultToObject(rs, recreducation);
				lists.add(recreducation);
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

	public static void resultToObject(ResultSet rs, RecrEducation recreducation){
		try{
			recreducation.setOID(rs.getLong(PstRecrEducation.fieldNames[PstRecrEducation.FLD_RECR_EDUCATION_ID]));
			recreducation.setRecrApplicationId(rs.getLong(PstRecrEducation.fieldNames[PstRecrEducation.FLD_RECR_APPLICATION_ID]));
			recreducation.setEducationId(rs.getLong(PstRecrEducation.fieldNames[PstRecrEducation.FLD_EDUCATION_ID]));
			recreducation.setStartDate(rs.getDate(PstRecrEducation.fieldNames[PstRecrEducation.FLD_START_DATE]));
			recreducation.setEndDate(rs.getDate(PstRecrEducation.fieldNames[PstRecrEducation.FLD_END_DATE]));
			recreducation.setStudy(rs.getString(PstRecrEducation.fieldNames[PstRecrEducation.FLD_STUDY]));
			recreducation.setDegree(rs.getString(PstRecrEducation.fieldNames[PstRecrEducation.FLD_DEGREE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrEducationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_EDUCATION + " WHERE " + 
						PstRecrEducation.fieldNames[PstRecrEducation.FLD_RECR_EDUCATION_ID] + " = " + recrEducationId;

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
			String sql = "SELECT COUNT("+ PstRecrEducation.fieldNames[PstRecrEducation.FLD_RECR_EDUCATION_ID] + ") FROM " + TBL_HR_RECR_EDUCATION;
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
			  	   RecrEducation recreducation = (RecrEducation)list.get(ls);
				   if(oid == recreducation.getOID())
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
            String sql = " DELETE FROM "+PstRecrEducation.TBL_HR_RECR_EDUCATION+
                		 " WHERE "+PstRecrEducation.fieldNames[PstRecrEducation.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
