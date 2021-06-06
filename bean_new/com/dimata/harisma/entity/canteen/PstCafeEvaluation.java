
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

package com.dimata.harisma.entity.canteen; 

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
import com.dimata.harisma.entity.canteen.*; 

public class PstCafeEvaluation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_CAFE_EVALUATION = "HR_CAFE_EVALUATION";

	public static final  int FLD_CAFE_EVALUATION_ID = 0;
	public static final  int FLD_CHECKLIST_MARK_ID = 1;
	public static final  int FLD_CAFE_CHECKLIST_ID = 2;
	public static final  int FLD_CHECKLIST_ITEM_ID = 3;
	public static final  int FLD_REMARK = 4;

	public static final  String[] fieldNames = {
		"CAFE_EVALUATION_ID",
		"CHECKLIST_MARK_ID",
		"CAFE_CHECKLIST_ID",
		"CHECKLIST_ITEM_ID",
		"REMARK"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstCafeEvaluation(){
	}

	public PstCafeEvaluation(int i) throws DBException { 
		super(new PstCafeEvaluation()); 
	}

	public PstCafeEvaluation(String sOid) throws DBException { 
		super(new PstCafeEvaluation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstCafeEvaluation(long lOid) throws DBException { 
		super(new PstCafeEvaluation(0)); 
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
		return TBL_HR_CAFE_EVALUATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstCafeEvaluation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		CafeEvaluation cafeevaluation = fetchExc(ent.getOID()); 
		ent = (Entity)cafeevaluation; 
		return cafeevaluation.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((CafeEvaluation) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((CafeEvaluation) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static CafeEvaluation fetchExc(long oid) throws DBException{ 
		try{ 
			CafeEvaluation cafeevaluation = new CafeEvaluation();
			PstCafeEvaluation pstCafeEvaluation = new PstCafeEvaluation(oid); 
			cafeevaluation.setOID(oid);

			cafeevaluation.setChecklistMarkId(pstCafeEvaluation.getlong(FLD_CHECKLIST_MARK_ID));
			cafeevaluation.setCafeChecklistId(pstCafeEvaluation.getlong(FLD_CAFE_CHECKLIST_ID));
			cafeevaluation.setChecklistItemId(pstCafeEvaluation.getlong(FLD_CHECKLIST_ITEM_ID));
			cafeevaluation.setRemark(pstCafeEvaluation.getString(FLD_REMARK));

			return cafeevaluation; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeEvaluation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(CafeEvaluation cafeevaluation) throws DBException{ 
		try{ 
			PstCafeEvaluation pstCafeEvaluation = new PstCafeEvaluation(0);

			pstCafeEvaluation.setLong(FLD_CHECKLIST_MARK_ID, cafeevaluation.getChecklistMarkId());
			pstCafeEvaluation.setLong(FLD_CAFE_CHECKLIST_ID, cafeevaluation.getCafeChecklistId());
			pstCafeEvaluation.setLong(FLD_CHECKLIST_ITEM_ID, cafeevaluation.getChecklistItemId());
			pstCafeEvaluation.setString(FLD_REMARK, cafeevaluation.getRemark());

			pstCafeEvaluation.insert(); 
			cafeevaluation.setOID(pstCafeEvaluation.getlong(FLD_CAFE_EVALUATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeEvaluation(0),DBException.UNKNOWN); 
		}
		return cafeevaluation.getOID();
	}

	public static long updateExc(CafeEvaluation cafeevaluation) throws DBException{ 
		try{ 
			if(cafeevaluation.getOID() != 0){ 
				PstCafeEvaluation pstCafeEvaluation = new PstCafeEvaluation(cafeevaluation.getOID());

				pstCafeEvaluation.setLong(FLD_CHECKLIST_MARK_ID, cafeevaluation.getChecklistMarkId());
				pstCafeEvaluation.setLong(FLD_CAFE_CHECKLIST_ID, cafeevaluation.getCafeChecklistId());
				pstCafeEvaluation.setLong(FLD_CHECKLIST_ITEM_ID, cafeevaluation.getChecklistItemId());
				pstCafeEvaluation.setString(FLD_REMARK, cafeevaluation.getRemark());

				pstCafeEvaluation.update(); 
				return cafeevaluation.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeEvaluation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstCafeEvaluation pstCafeEvaluation = new PstCafeEvaluation(oid);
			pstCafeEvaluation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeEvaluation(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_CAFE_EVALUATION; 
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
				CafeEvaluation cafeevaluation = new CafeEvaluation();
				resultToObject(rs, cafeevaluation);
				lists.add(cafeevaluation);
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

	public static void resultToObject(ResultSet rs, CafeEvaluation cafeevaluation){
		try{
			cafeevaluation.setOID(rs.getLong(PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_CAFE_EVALUATION_ID]));
			cafeevaluation.setChecklistMarkId(rs.getLong(PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_CHECKLIST_MARK_ID]));
			cafeevaluation.setCafeChecklistId(rs.getLong(PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_CAFE_CHECKLIST_ID]));
			cafeevaluation.setChecklistItemId(rs.getLong(PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_CHECKLIST_ITEM_ID]));
			cafeevaluation.setRemark(rs.getString(PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_REMARK]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long cafeEvaluationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CAFE_EVALUATION + " WHERE " + 
						PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_CAFE_EVALUATION_ID] + " = " + cafeEvaluationId;

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
			String sql = "SELECT COUNT("+ PstCafeEvaluation.fieldNames[PstCafeEvaluation.FLD_CAFE_EVALUATION_ID] + ") FROM " + TBL_HR_CAFE_EVALUATION;
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
			  	   CafeEvaluation cafeevaluation = (CafeEvaluation)list.get(ls);
				   if(oid == cafeevaluation.getOID())
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
