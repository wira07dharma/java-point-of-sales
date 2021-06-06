
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
import java.sql.*;
import java.util.*;
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

public class PstRecrLanguage extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_LANGUAGE = "HR_RECR_LANGUAGE";

	public static final  int FLD_RECR_LANGUAGE_ID = 0;
	public static final  int FLD_RECR_APPLICATION_ID = 1;
	public static final  int FLD_LANGUAGE_ID = 2;
	public static final  int FLD_SPOKEN = 3;
	public static final  int FLD_WRITTEN = 4;
	public static final  int FLD_READING = 5;

	public static final  String[] fieldNames = {
		"RECR_LANGUAGE_ID",
		"RECR_APPLICATION_ID",
		"LANGUAGE_ID",
		"SPOKEN",
		"WRITTEN",
		"READING"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT
	 }; 

    public static final int GRADE_FLUENT = 0;
    public static final int GRADE_FAIR	 = 1;
    public static final int GRADE_POOR	 = 2;

    public static final int[] gradeValue = {GRADE_FLUENT, GRADE_FAIR, GRADE_POOR};
    public static final String[] gradeKey = {"Fluent","Fair","Poor"};

    public static Vector getGradeKey(){
    	Vector result = new Vector(1,1);
        for(int i= 0;i<gradeKey.length;i++){
        	result.add(gradeKey[i]);
        }
        return result;
    }

    public static Vector getGradeValue(){
    	Vector result = new Vector(1,1);
        for(int i= 0;i<gradeValue.length;i++){
        	result.add(String.valueOf(gradeValue[i]));
        }
        return result;
    }

	public PstRecrLanguage(){
	}

	public PstRecrLanguage(int i) throws DBException { 
		super(new PstRecrLanguage()); 
	}

	public PstRecrLanguage(String sOid) throws DBException { 
		super(new PstRecrLanguage(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrLanguage(long lOid) throws DBException { 
		super(new PstRecrLanguage(0)); 
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
		return TBL_HR_RECR_LANGUAGE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrLanguage().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrLanguage recrlanguage = fetchExc(ent.getOID()); 
		ent = (Entity)recrlanguage; 
		return recrlanguage.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrLanguage) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrLanguage) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrLanguage fetchExc(long oid) throws DBException{ 
		try{ 
			RecrLanguage recrlanguage = new RecrLanguage();
			PstRecrLanguage pstRecrLanguage = new PstRecrLanguage(oid); 
			recrlanguage.setOID(oid);

			recrlanguage.setRecrApplicationId(pstRecrLanguage.getlong(FLD_RECR_APPLICATION_ID));
			recrlanguage.setLanguageId(pstRecrLanguage.getlong(FLD_LANGUAGE_ID));
			recrlanguage.setSpoken(pstRecrLanguage.getInt(FLD_SPOKEN));
			recrlanguage.setWritten(pstRecrLanguage.getInt(FLD_WRITTEN));
			recrlanguage.setReading(pstRecrLanguage.getInt(FLD_READING));

			return recrlanguage; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrLanguage(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrLanguage recrlanguage) throws DBException{ 
		try{ 
			PstRecrLanguage pstRecrLanguage = new PstRecrLanguage(0);

			pstRecrLanguage.setLong(FLD_RECR_APPLICATION_ID, recrlanguage.getRecrApplicationId());
			pstRecrLanguage.setLong(FLD_LANGUAGE_ID, recrlanguage.getLanguageId());
			pstRecrLanguage.setInt(FLD_SPOKEN, recrlanguage.getSpoken());
			pstRecrLanguage.setInt(FLD_WRITTEN, recrlanguage.getWritten());
			pstRecrLanguage.setInt(FLD_READING, recrlanguage.getReading());

			pstRecrLanguage.insert(); 
			recrlanguage.setOID(pstRecrLanguage.getlong(FLD_RECR_LANGUAGE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrLanguage(0),DBException.UNKNOWN); 
		}
		return recrlanguage.getOID();
	}

	public static long updateExc(RecrLanguage recrlanguage) throws DBException{ 
		try{ 
			if(recrlanguage.getOID() != 0){ 
				PstRecrLanguage pstRecrLanguage = new PstRecrLanguage(recrlanguage.getOID());

				pstRecrLanguage.setLong(FLD_RECR_APPLICATION_ID, recrlanguage.getRecrApplicationId());
				pstRecrLanguage.setLong(FLD_LANGUAGE_ID, recrlanguage.getLanguageId());
				pstRecrLanguage.setInt(FLD_SPOKEN, recrlanguage.getSpoken());
				pstRecrLanguage.setInt(FLD_WRITTEN, recrlanguage.getWritten());
				pstRecrLanguage.setInt(FLD_READING, recrlanguage.getReading());

				pstRecrLanguage.update(); 
				return recrlanguage.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrLanguage(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrLanguage pstRecrLanguage = new PstRecrLanguage(oid);
			pstRecrLanguage.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrLanguage(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_LANGUAGE; 
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
				RecrLanguage recrlanguage = new RecrLanguage();
				resultToObject(rs, recrlanguage);
				lists.add(recrlanguage);
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

	public static void resultToObject(ResultSet rs, RecrLanguage recrlanguage){
		try{
			recrlanguage.setOID(rs.getLong(PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_RECR_LANGUAGE_ID]));
			recrlanguage.setRecrApplicationId(rs.getLong(PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_RECR_APPLICATION_ID]));
			recrlanguage.setLanguageId(rs.getLong(PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_LANGUAGE_ID]));
			recrlanguage.setSpoken(rs.getInt(PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_SPOKEN]));
			recrlanguage.setWritten(rs.getInt(PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_WRITTEN]));
			recrlanguage.setReading(rs.getInt(PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_READING]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrLanguageId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_LANGUAGE + " WHERE " + 
						PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_RECR_LANGUAGE_ID] + " = " + recrLanguageId;

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
			String sql = "SELECT COUNT("+ PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_RECR_LANGUAGE_ID] + ") FROM " + TBL_HR_RECR_LANGUAGE;
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
			  	   RecrLanguage recrlanguage = (RecrLanguage)list.get(ls);
				   if(oid == recrlanguage.getOID())
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
            String sql = " DELETE FROM "+PstRecrLanguage.TBL_HR_RECR_LANGUAGE+
                         " WHERE "+PstRecrLanguage.fieldNames[PstRecrLanguage.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
