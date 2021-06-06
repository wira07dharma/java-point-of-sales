
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

public class PstChecklistMark extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_CHECKLIST_MARK = "HR_CHECKLIST_MARK";

	public static final  int FLD_CHECKLIST_MARK_ID = 0;
	public static final  int FLD_CHECKLIST_MARK = 1;

	public static final  String[] fieldNames = {
		"CHECKLIST_MARK_ID",
		"CHECKLIST_MARK"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstChecklistMark(){
	}

	public PstChecklistMark(int i) throws DBException { 
		super(new PstChecklistMark()); 
	}

	public PstChecklistMark(String sOid) throws DBException { 
		super(new PstChecklistMark(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstChecklistMark(long lOid) throws DBException { 
		super(new PstChecklistMark(0)); 
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
		return TBL_HR_CHECKLIST_MARK;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstChecklistMark().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ChecklistMark checklistmark = fetchExc(ent.getOID()); 
		ent = (Entity)checklistmark; 
		return checklistmark.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ChecklistMark) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ChecklistMark) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static ChecklistMark fetchExc(long oid) throws DBException{ 
		try{ 
			ChecklistMark checklistmark = new ChecklistMark();
			PstChecklistMark pstChecklistMark = new PstChecklistMark(oid); 
			checklistmark.setOID(oid);

			checklistmark.setChecklistMark(pstChecklistMark.getString(FLD_CHECKLIST_MARK));

			return checklistmark; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistMark(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ChecklistMark checklistmark) throws DBException{ 
		try{ 
			PstChecklistMark pstChecklistMark = new PstChecklistMark(0);

			pstChecklistMark.setString(FLD_CHECKLIST_MARK, checklistmark.getChecklistMark());

			pstChecklistMark.insert(); 
			checklistmark.setOID(pstChecklistMark.getlong(FLD_CHECKLIST_MARK_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistMark(0),DBException.UNKNOWN); 
		}
		return checklistmark.getOID();
	}

	public static long updateExc(ChecklistMark checklistmark) throws DBException{ 
		try{ 
			if(checklistmark.getOID() != 0){ 
				PstChecklistMark pstChecklistMark = new PstChecklistMark(checklistmark.getOID());

				pstChecklistMark.setString(FLD_CHECKLIST_MARK, checklistmark.getChecklistMark());

				pstChecklistMark.update(); 
				return checklistmark.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistMark(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstChecklistMark pstChecklistMark = new PstChecklistMark(oid);
			pstChecklistMark.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistMark(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_CHECKLIST_MARK; 
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
				ChecklistMark checklistmark = new ChecklistMark();
				resultToObject(rs, checklistmark);
				lists.add(checklistmark);
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

	public static void resultToObject(ResultSet rs, ChecklistMark checklistmark){
		try{
			checklistmark.setOID(rs.getLong(PstChecklistMark.fieldNames[PstChecklistMark.FLD_CHECKLIST_MARK_ID]));
			checklistmark.setChecklistMark(rs.getString(PstChecklistMark.fieldNames[PstChecklistMark.FLD_CHECKLIST_MARK]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long checklistMarkId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CHECKLIST_MARK + " WHERE " + 
						PstChecklistMark.fieldNames[PstChecklistMark.FLD_CHECKLIST_MARK_ID] + " = " + checklistMarkId;

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
			String sql = "SELECT COUNT("+ PstChecklistMark.fieldNames[PstChecklistMark.FLD_CHECKLIST_MARK_ID] + ") FROM " + TBL_HR_CHECKLIST_MARK;
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
			  	   ChecklistMark checklistmark = (ChecklistMark)list.get(ls);
				   if(oid == checklistmark.getOID())
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
