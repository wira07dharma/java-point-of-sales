
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

import com.dimata.harisma.entity.employee.*;

public class PstEducation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_EDUCATION = "HR_EDUCATION";

	public static final  int FLD_EDUCATION_ID = 0;
	public static final  int FLD_EDUCATION = 1;
	public static final  int FLD_EDUCATION_DESC = 2;

	public static final  String[] fieldNames = {
		"EDUCATION_ID",
		"EDUCATION",
		"EDUCATION_DESC"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstEducation(){
	}

	public PstEducation(int i) throws DBException { 
		super(new PstEducation()); 
	}

	public PstEducation(String sOid) throws DBException { 
		super(new PstEducation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstEducation(long lOid) throws DBException { 
		super(new PstEducation(0)); 
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
		return TBL_HR_EDUCATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstEducation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Education education = fetchExc(ent.getOID()); 
		ent = (Entity)education; 
		return education.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Education) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Education) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Education fetchExc(long oid) throws DBException{ 
		try{ 
			Education education = new Education();
			PstEducation pstEducation = new PstEducation(oid); 
			education.setOID(oid);

			education.setEducation(pstEducation.getString(FLD_EDUCATION));
			education.setEducationDesc(pstEducation.getString(FLD_EDUCATION_DESC));

			return education; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Education education) throws DBException{ 
		try{ 
			PstEducation pstEducation = new PstEducation(0);

			pstEducation.setString(FLD_EDUCATION, education.getEducation());
			pstEducation.setString(FLD_EDUCATION_DESC, education.getEducationDesc());

			pstEducation.insert(); 
			education.setOID(pstEducation.getlong(FLD_EDUCATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
		}
		return education.getOID();
	}

	public static long updateExc(Education education) throws DBException{ 
		try{ 
			if(education.getOID() != 0){ 
				PstEducation pstEducation = new PstEducation(education.getOID());

				pstEducation.setString(FLD_EDUCATION, education.getEducation());
				pstEducation.setString(FLD_EDUCATION_DESC, education.getEducationDesc());

				pstEducation.update(); 
				return education.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstEducation pstEducation = new PstEducation(oid);
			pstEducation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_EDUCATION; 
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
				Education education = new Education();
				resultToObject(rs, education);
				lists.add(education);
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

	private static void resultToObject(ResultSet rs, Education education){
		try{
			education.setOID(rs.getLong(PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID]));
			education.setEducation(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION]));
			education.setEducationDesc(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION_DESC]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long educationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EDUCATION + " WHERE " + 
						PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID] + " = " + educationId;

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
			String sql = "SELECT COUNT("+ PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID] + ") FROM " + TBL_HR_EDUCATION;
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
			  	   Education education = (Education)list.get(ls);
				   if(oid == education.getOID())
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
        
    public static boolean checkMaster(long oid){
    	if(PstEmpEducation.checkEducation(oid))
            return true;
    	else
            return false;
    }
}
