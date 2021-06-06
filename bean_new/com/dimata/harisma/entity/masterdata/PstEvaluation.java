
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

public class PstEvaluation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_EVALUATION = "HR_EVALUATION";

	public static final  int FLD_EVALUATION_ID = 0;
	public static final  int FLD_CODE = 1;
	public static final  int FLD_NAME = 2;
	public static final  int FLD_DESRIPTION = 3;

	public static final  String[] fieldNames = {
		"EVALUATION_ID",
		"CODE",
		"NAME",
		"DESRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstEvaluation(){
	}

	public PstEvaluation(int i) throws DBException { 
		super(new PstEvaluation()); 
	}

	public PstEvaluation(String sOid) throws DBException { 
		super(new PstEvaluation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstEvaluation(long lOid) throws DBException { 
		super(new PstEvaluation(0)); 
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
		return TBL_HR_EVALUATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstEvaluation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Evaluation evaluation = fetchExc(ent.getOID()); 
		ent = (Entity)evaluation; 
		return evaluation.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Evaluation) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Evaluation) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Evaluation fetchExc(long oid) throws DBException{ 
		try{ 
			Evaluation evaluation = new Evaluation();
			PstEvaluation pstEvaluation = new PstEvaluation(oid); 
			evaluation.setOID(oid);

			evaluation.setCode(pstEvaluation.getString(FLD_CODE));
			evaluation.setName(pstEvaluation.getString(FLD_NAME));
			evaluation.setDesription(pstEvaluation.getString(FLD_DESRIPTION));

			return evaluation; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEvaluation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Evaluation evaluation) throws DBException{ 
		try{ 
			PstEvaluation pstEvaluation = new PstEvaluation(0);

			pstEvaluation.setString(FLD_CODE, evaluation.getCode());
			pstEvaluation.setString(FLD_NAME, evaluation.getName());
			pstEvaluation.setString(FLD_DESRIPTION, evaluation.getDesription());

			pstEvaluation.insert(); 
			evaluation.setOID(pstEvaluation.getlong(FLD_EVALUATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEvaluation(0),DBException.UNKNOWN); 
		}
		return evaluation.getOID();
	}

	public static long updateExc(Evaluation evaluation) throws DBException{ 
		try{ 
			if(evaluation.getOID() != 0){ 
				PstEvaluation pstEvaluation = new PstEvaluation(evaluation.getOID());

				pstEvaluation.setString(FLD_CODE, evaluation.getCode());
				pstEvaluation.setString(FLD_NAME, evaluation.getName());
				pstEvaluation.setString(FLD_DESRIPTION, evaluation.getDesription());

				pstEvaluation.update(); 
				return evaluation.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEvaluation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstEvaluation pstEvaluation = new PstEvaluation(oid);
			pstEvaluation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEvaluation(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_EVALUATION; 
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
				Evaluation evaluation = new Evaluation();
				resultToObject(rs, evaluation);
				lists.add(evaluation);
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

	private static void resultToObject(ResultSet rs, Evaluation evaluation){
		try{
			evaluation.setOID(rs.getLong(PstEvaluation.fieldNames[PstEvaluation.FLD_EVALUATION_ID]));
			evaluation.setCode(rs.getString(PstEvaluation.fieldNames[PstEvaluation.FLD_CODE]));
			evaluation.setName(rs.getString(PstEvaluation.fieldNames[PstEvaluation.FLD_NAME]));
			evaluation.setDesription(rs.getString(PstEvaluation.fieldNames[PstEvaluation.FLD_DESRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long evaluationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EVALUATION + " WHERE " + 
						PstEvaluation.fieldNames[PstEvaluation.FLD_EVALUATION_ID] + " = " + evaluationId;

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
			String sql = "SELECT COUNT("+ PstEvaluation.fieldNames[PstEvaluation.FLD_EVALUATION_ID] + ") FROM " + TBL_HR_EVALUATION;
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
			  	   Evaluation evaluation = (Evaluation)list.get(ls);
				   if(oid == evaluation.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static boolean checkMaster(long oid)
    {
    	if(PstPerformanceEvaluation.checkEvaluation(oid))
            return true;
    	else
            return false;
    }
}
