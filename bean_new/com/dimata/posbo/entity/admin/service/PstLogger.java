
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

package com.dimata.posbo.entity.admin.service;

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language; 
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

import com.dimata.posbo.entity.admin.*;

public class PstLogger extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_LOGGER = "LOGGER";

	//public static final  int FLD_LOGGER_ID = 0;
	public static final  int FLD_DATE_CREATED = 0;
	public static final  int FLD_TIME_CREATED = 1;
	public static final  int FLD_TARGET1_NOTE = 2;
	public static final  int FLD_TARGET2_NOTE = 3;
	public static final  int FLD_TARGET3_NOTE = 4;

	public static final  String[] fieldNames = {
		//"LOGGER_ID",
		"DATE_CREATED",
		"TIME_CREATED",
		"TARGET1_NOTE",
		"TARGET2_NOTE",
		"TARGET3_NOTE"
	 }; 

	public static final  int[] fieldTypes = {
		//TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstLogger(){
	}

	public PstLogger(int i) throws DBException { 
		super(new PstLogger()); 
	}

	public PstLogger(String sOid) throws DBException { 
		super(new PstLogger(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLogger(long lOid) throws DBException { 
		super(new PstLogger(0)); 
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
		return TBL_LOGGER;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLogger().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Logger logger = fetchExc(ent.getOID()); 
		ent = (Entity)logger; 
		return logger.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Logger) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Logger) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Logger fetchExc(long oid) throws DBException{ 
		try{ 
			Logger logger = new Logger();
			PstLogger pstLogger = new PstLogger(oid); 
			logger.setOID(oid);

			logger.setDateCreated(pstLogger.getDate(FLD_DATE_CREATED));
			//logger.setTimeCreated(pstLogger.getDate(FLD_TIME_CREATED));
			logger.setTarget1Note(pstLogger.getString(FLD_TARGET1_NOTE));
			logger.setTarget2Note(pstLogger.getString(FLD_TARGET2_NOTE));
			logger.setTarget3Note(pstLogger.getString(FLD_TARGET3_NOTE));

			return logger; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLogger(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Logger logger) throws DBException{ 
		try{ 
			PstLogger pstLogger = new PstLogger(0);

			pstLogger.setDate(FLD_DATE_CREATED, logger.getDateCreated());
			pstLogger.setDate(FLD_TIME_CREATED, logger.getTimeCreated());
			pstLogger.setString(FLD_TARGET1_NOTE, logger.getTarget1Note());
			pstLogger.setString(FLD_TARGET2_NOTE, logger.getTarget2Note());
			pstLogger.setString(FLD_TARGET3_NOTE, logger.getTarget3Note());

			pstLogger.insert(); 
			//logger.setOID(pstLogger.getlong(FLD_LOGGER_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLogger(0),DBException.UNKNOWN); 
		}
		return logger.getOID();
	}

	public static long updateExc(Logger logger) throws DBException{ 
		try{ 
			if(logger.getOID() != 0){ 
				PstLogger pstLogger = new PstLogger(logger.getOID());

				pstLogger.setDate(FLD_DATE_CREATED, logger.getDateCreated());
				pstLogger.setDate(FLD_TIME_CREATED, logger.getTimeCreated());
				pstLogger.setString(FLD_TARGET1_NOTE, logger.getTarget1Note());
				pstLogger.setString(FLD_TARGET2_NOTE, logger.getTarget2Note());
				pstLogger.setString(FLD_TARGET3_NOTE, logger.getTarget3Note());

				pstLogger.update(); 
				return logger.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLogger(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLogger pstLogger = new PstLogger(oid);
			pstLogger.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLogger(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_LOGGER; 
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
				Logger logger = new Logger();
				resultToObject(rs, logger);
				lists.add(logger);
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

	public static void resultToObject(ResultSet rs, Logger logger){
		try{
			//logger.setOID(rs.getLong(PstLogger.fieldNames[PstLogger.FLD_LOGGER_ID]));
			logger.setDateCreated(rs.getDate(PstLogger.fieldNames[PstLogger.FLD_DATE_CREATED]));
            System.out.println("time ::::: "+rs.getTime(PstLogger.fieldNames[PstLogger.FLD_DATE_CREATED]));
			logger.setTimeCreated(rs.getTime(PstLogger.fieldNames[PstLogger.FLD_DATE_CREATED]));
			logger.setTarget1Note(rs.getString(PstLogger.fieldNames[PstLogger.FLD_TARGET1_NOTE]));
			logger.setTarget2Note(rs.getString(PstLogger.fieldNames[PstLogger.FLD_TARGET2_NOTE]));
			logger.setTarget3Note(rs.getString(PstLogger.fieldNames[PstLogger.FLD_TARGET3_NOTE]));

		}catch(Exception e){ }
	}

/*	public static boolean checkOID(long loggerId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_LOGGER + " WHERE " + 
						PstLogger.fieldNames[PstLogger.FLD_LOGGER_ID] + " = " + loggerId;

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
       */
/*	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstLogger.fieldNames[PstLogger.FLD_LOGGER_ID] + ") FROM " + TBL_LOGGER;
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
    */

	/* This method used to find current data */
/*	public static int findLimitStart( long oid, int recordToGet, String whereClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   Logger logger = (Logger)list.get(ls);
				   if(oid == logger.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	} */
}
