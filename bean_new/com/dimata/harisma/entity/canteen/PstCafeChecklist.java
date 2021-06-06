
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

public class PstCafeChecklist extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_CAFE_CHECKLIST = "HR_CAFE_CHECKLIST";

	public static final  int FLD_CAFE_CHECKLIST_ID = 0;
	public static final  int FLD_MEAL_TIME_ID = 1;
	public static final  int FLD_CHECK_DATE = 2;
	public static final  int FLD_CHECKED_BY = 3;
	public static final  int FLD_APPROVED_BY = 4;

	public static final  String[] fieldNames = {
		"CAFE_CHECKLIST_ID",
		"MEAL_TIME_ID",
		"CHECK_DATE",
		"CHECKED_BY",
		"APPROVED_BY"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_LONG,
		TYPE_LONG
	 }; 

	public PstCafeChecklist(){
	}

	public PstCafeChecklist(int i) throws DBException { 
		super(new PstCafeChecklist()); 
	}

	public PstCafeChecklist(String sOid) throws DBException { 
		super(new PstCafeChecklist(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstCafeChecklist(long lOid) throws DBException { 
		super(new PstCafeChecklist(0)); 
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
		return TBL_HR_CAFE_CHECKLIST;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstCafeChecklist().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		CafeChecklist cafechecklist = fetchExc(ent.getOID()); 
		ent = (Entity)cafechecklist; 
		return cafechecklist.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((CafeChecklist) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((CafeChecklist) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static CafeChecklist fetchExc(long oid) throws DBException{ 
		try{ 
			CafeChecklist cafechecklist = new CafeChecklist();
			PstCafeChecklist pstCafeChecklist = new PstCafeChecklist(oid); 
			cafechecklist.setOID(oid);

			cafechecklist.setMealTimeId(pstCafeChecklist.getlong(FLD_MEAL_TIME_ID));
			cafechecklist.setCheckDate(pstCafeChecklist.getDate(FLD_CHECK_DATE));
			cafechecklist.setCheckedBy(pstCafeChecklist.getlong(FLD_CHECKED_BY));
			cafechecklist.setApprovedBy(pstCafeChecklist.getlong(FLD_APPROVED_BY));

			return cafechecklist; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeChecklist(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(CafeChecklist cafechecklist) throws DBException{ 
		try{ 
			PstCafeChecklist pstCafeChecklist = new PstCafeChecklist(0);

			pstCafeChecklist.setLong(FLD_MEAL_TIME_ID, cafechecklist.getMealTimeId());
			pstCafeChecklist.setDate(FLD_CHECK_DATE, cafechecklist.getCheckDate());
			pstCafeChecklist.setLong(FLD_CHECKED_BY, cafechecklist.getCheckedBy());
			pstCafeChecklist.setLong(FLD_APPROVED_BY, cafechecklist.getApprovedBy());

			pstCafeChecklist.insert(); 
			cafechecklist.setOID(pstCafeChecklist.getlong(FLD_CAFE_CHECKLIST_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeChecklist(0),DBException.UNKNOWN); 
		}
		return cafechecklist.getOID();
	}

	public static long updateExc(CafeChecklist cafechecklist) throws DBException{ 
		try{ 
			if(cafechecklist.getOID() != 0){ 
				PstCafeChecklist pstCafeChecklist = new PstCafeChecklist(cafechecklist.getOID());

				pstCafeChecklist.setLong(FLD_MEAL_TIME_ID, cafechecklist.getMealTimeId());
				pstCafeChecklist.setDate(FLD_CHECK_DATE, cafechecklist.getCheckDate());
				pstCafeChecklist.setLong(FLD_CHECKED_BY, cafechecklist.getCheckedBy());
				pstCafeChecklist.setLong(FLD_APPROVED_BY, cafechecklist.getApprovedBy());

				pstCafeChecklist.update(); 
				return cafechecklist.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeChecklist(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstCafeChecklist pstCafeChecklist = new PstCafeChecklist(oid);
			pstCafeChecklist.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCafeChecklist(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_CAFE_CHECKLIST; 
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
				CafeChecklist cafechecklist = new CafeChecklist();
				resultToObject(rs, cafechecklist);
				lists.add(cafechecklist);
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

	public static void resultToObject(ResultSet rs, CafeChecklist cafechecklist){
		try{
			cafechecklist.setOID(rs.getLong(PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_CAFE_CHECKLIST_ID]));
			cafechecklist.setMealTimeId(rs.getLong(PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_MEAL_TIME_ID]));
			cafechecklist.setCheckDate(rs.getDate(PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_CHECK_DATE]));
			cafechecklist.setCheckedBy(rs.getLong(PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_CHECKED_BY]));
			cafechecklist.setApprovedBy(rs.getLong(PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_APPROVED_BY]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long cafeChecklistId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CAFE_CHECKLIST + " WHERE " + 
						PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_CAFE_CHECKLIST_ID] + " = " + cafeChecklistId;

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
			String sql = "SELECT COUNT("+ PstCafeChecklist.fieldNames[PstCafeChecklist.FLD_CAFE_CHECKLIST_ID] + ") FROM " + TBL_HR_CAFE_CHECKLIST;
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
			  	   CafeChecklist cafechecklist = (CafeChecklist)list.get(ls);
				   if(oid == cafechecklist.getOID())
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
