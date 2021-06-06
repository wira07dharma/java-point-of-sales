
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

public class PstLanguage extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LANGUAGE = "HR_LANGUAGE";

	public static final  int FLD_LANGUAGE_ID = 0;
	public static final  int FLD_LANGUAGE = 1;

	public static final  String[] fieldNames = {
		"LANGUAGE_ID",
		"LANGUAGE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstLanguage(){
	}

	public PstLanguage(int i) throws DBException { 
		super(new PstLanguage()); 
	}

	public PstLanguage(String sOid) throws DBException { 
		super(new PstLanguage(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLanguage(long lOid) throws DBException { 
		super(new PstLanguage(0)); 
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
		return TBL_HR_LANGUAGE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLanguage().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Language language = fetchExc(ent.getOID()); 
		ent = (Entity)language; 
		return language.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Language) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Language) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Language fetchExc(long oid) throws DBException{ 
		try{ 
			Language language = new Language();
			PstLanguage pstLanguage = new PstLanguage(oid); 
			language.setOID(oid);

			language.setLanguage(pstLanguage.getString(FLD_LANGUAGE));

			return language; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLanguage(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Language language) throws DBException{ 
		try{ 
			PstLanguage pstLanguage = new PstLanguage(0);

			pstLanguage.setString(FLD_LANGUAGE, language.getLanguage());

			pstLanguage.insert(); 
			language.setOID(pstLanguage.getlong(FLD_LANGUAGE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLanguage(0),DBException.UNKNOWN); 
		}
		return language.getOID();
	}

	public static long updateExc(Language language) throws DBException{ 
		try{ 
			if(language.getOID() != 0){ 
				PstLanguage pstLanguage = new PstLanguage(language.getOID());

				pstLanguage.setString(FLD_LANGUAGE, language.getLanguage());

				pstLanguage.update(); 
				return language.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLanguage(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLanguage pstLanguage = new PstLanguage(oid);
			pstLanguage.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLanguage(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LANGUAGE; 
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
				Language language = new Language();
				resultToObject(rs, language);
				lists.add(language);
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

	private static void resultToObject(ResultSet rs, Language language){
		try{
			language.setOID(rs.getLong(PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE_ID]));
			language.setLanguage(rs.getString(PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long languageId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LANGUAGE + " WHERE " + 
						PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE_ID] + " = " + languageId;

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
			String sql = "SELECT COUNT("+ PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE_ID] + ") FROM " + TBL_HR_LANGUAGE;
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
			  	   Language language = (Language)list.get(ls);
				   if(oid == language.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static boolean checkMaster(long oid){
    	if(PstEmpLanguage.checkLanguage(oid))
            return true;
    	else
            return false;
    }
}
