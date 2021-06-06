
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
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
import org.json.JSONObject;

public class PstBackUpService extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_BACK_UP_CONF = "BACK_UP_CONF";

	public static final  int FLD_BACK_UP_CONF_ID = 0;
	public static final  int FLD_START_TIME = 1;
	public static final  int FLD_PERIODE = 2;
	public static final  int FLD_TARGET1 = 3;
	public static final  int FLD_TARGET2 = 4;
	public static final  int FLD_TARGET3 = 5;
	public static final  int FLD_SOURCE_PATH = 6;

	public static final  String[] fieldNames = {
		"BACK_UP_CONF_ID",
		"START_TIME",
		"PERIODE",
		"TARGET1",
		"TARGET2",
		"TARGET3",
		"SOURCE_PATH"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_DATE,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstBackUpService(){
	}

	public PstBackUpService(int i) throws DBException { 
		super(new PstBackUpService()); 
	}

	public PstBackUpService(String sOid) throws DBException { 
		super(new PstBackUpService(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstBackUpService(long lOid) throws DBException { 
		super(new PstBackUpService(0)); 
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
		return TBL_BACK_UP_CONF;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstBackUpService().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		BackUpService backupservice = fetchExc(ent.getOID()); 
		ent = (Entity)backupservice; 
		return backupservice.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((BackUpService) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((BackUpService) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static BackUpService fetchExc(long oid) throws DBException{ 
		try{ 
			BackUpService backupservice = new BackUpService();
			PstBackUpService pstBackUpService = new PstBackUpService(oid); 
			backupservice.setOID(oid);

			backupservice.setStartTime(pstBackUpService.getDate(FLD_START_TIME));
			backupservice.setPeriode(pstBackUpService.getInt(FLD_PERIODE));
			backupservice.setTarget1(pstBackUpService.getString(FLD_TARGET1));
			backupservice.setTarget2(pstBackUpService.getString(FLD_TARGET2));
			backupservice.setTarget3(pstBackUpService.getString(FLD_TARGET3));
			backupservice.setSourcePath(pstBackUpService.getString(FLD_SOURCE_PATH));

			return backupservice; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstBackUpService(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(BackUpService backupservice) throws DBException{ 
		try{ 
			PstBackUpService pstBackUpService = new PstBackUpService(0);

			pstBackUpService.setDate(FLD_START_TIME, backupservice.getStartTime());
			pstBackUpService.setInt(FLD_PERIODE, backupservice.getPeriode());
			pstBackUpService.setString(FLD_TARGET1, backupservice.getTarget1());
			pstBackUpService.setString(FLD_TARGET2, backupservice.getTarget2());
			pstBackUpService.setString(FLD_TARGET3, backupservice.getTarget3());
			pstBackUpService.setString(FLD_SOURCE_PATH, backupservice.getSourcePath());

			pstBackUpService.insert(); 
			backupservice.setOID(pstBackUpService.getlong(FLD_BACK_UP_CONF_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstBackUpService(0),DBException.UNKNOWN); 
		}
		return backupservice.getOID();
	}

	public static long updateExc(BackUpService backupservice) throws DBException{ 
		try{ 
			if(backupservice.getOID() != 0){ 
				PstBackUpService pstBackUpService = new PstBackUpService(backupservice.getOID());

				pstBackUpService.setDate(FLD_START_TIME, backupservice.getStartTime());
				pstBackUpService.setInt(FLD_PERIODE, backupservice.getPeriode());
				pstBackUpService.setString(FLD_TARGET1, backupservice.getTarget1());
				pstBackUpService.setString(FLD_TARGET2, backupservice.getTarget2());
				pstBackUpService.setString(FLD_TARGET3, backupservice.getTarget3());
				pstBackUpService.setString(FLD_SOURCE_PATH, backupservice.getSourcePath());

				pstBackUpService.update(); 
				return backupservice.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstBackUpService(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstBackUpService pstBackUpService = new PstBackUpService(oid);
			pstBackUpService.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstBackUpService(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_BACK_UP_CONF; 
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
				BackUpService backupservice = new BackUpService();
				resultToObject(rs, backupservice);
				lists.add(backupservice);
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

	public static void resultToObject(ResultSet rs, BackUpService backupservice){
		try{
			backupservice.setOID(rs.getLong(PstBackUpService.fieldNames[PstBackUpService.FLD_BACK_UP_CONF_ID]));
			backupservice.setStartTime(rs.getDate(PstBackUpService.fieldNames[PstBackUpService.FLD_START_TIME]));
			backupservice.setPeriode(rs.getInt(PstBackUpService.fieldNames[PstBackUpService.FLD_PERIODE]));
			backupservice.setTarget1(rs.getString(PstBackUpService.fieldNames[PstBackUpService.FLD_TARGET1]));
			backupservice.setTarget2(rs.getString(PstBackUpService.fieldNames[PstBackUpService.FLD_TARGET2]));
			backupservice.setTarget3(rs.getString(PstBackUpService.fieldNames[PstBackUpService.FLD_TARGET3]));
			backupservice.setSourcePath(rs.getString(PstBackUpService.fieldNames[PstBackUpService.FLD_SOURCE_PATH]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long backUpConfId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_BACK_UP_CONF + " WHERE " + 
						PstBackUpService.fieldNames[PstBackUpService.FLD_BACK_UP_CONF_ID] + " = " + backUpConfId;

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
			String sql = "SELECT COUNT("+ PstBackUpService.fieldNames[PstBackUpService.FLD_BACK_UP_CONF_ID] + ") FROM " + TBL_BACK_UP_CONF;
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


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         BackUpService backUpService = PstBackUpService.fetchExc(oid);
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_BACK_UP_CONF_ID], backUpService.getOID());
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_START_TIME], backUpService.getStartTime());
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_PERIODE], backUpService.getPeriode());
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_TARGET1], backUpService.getTarget1());
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_TARGET2], backUpService.getTarget2());
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_TARGET3], backUpService.getTarget3());
         object.put(PstBackUpService.fieldNames[PstBackUpService.FLD_SOURCE_PATH], backUpService.getSourcePath());
      }catch(Exception exc){}
      return object;
   }
}
