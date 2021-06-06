
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

public class PstChecklistGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_CHECKLIST_GROUP = "HR_CHECKLIST_GROUP";

	public static final  int FLD_CHECKLIST_GROUP_ID = 0;
	public static final  int FLD_CHECKLIST_GROUP = 1;

	public static final  String[] fieldNames = {
		"CHECKLIST_GROUP_ID",
		"CHECKLIST_GROUP"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstChecklistGroup(){
	}

	public PstChecklistGroup(int i) throws DBException { 
		super(new PstChecklistGroup()); 
	}

	public PstChecklistGroup(String sOid) throws DBException { 
		super(new PstChecklistGroup(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstChecklistGroup(long lOid) throws DBException { 
		super(new PstChecklistGroup(0)); 
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
		return TBL_HR_CHECKLIST_GROUP;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstChecklistGroup().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ChecklistGroup checklistgroup = fetchExc(ent.getOID()); 
		ent = (Entity)checklistgroup; 
		return checklistgroup.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ChecklistGroup) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ChecklistGroup) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static ChecklistGroup fetchExc(long oid) throws DBException{ 
		try{ 
			ChecklistGroup checklistgroup = new ChecklistGroup();
			PstChecklistGroup pstChecklistGroup = new PstChecklistGroup(oid); 
			checklistgroup.setOID(oid);

			checklistgroup.setChecklistGroup(pstChecklistGroup.getString(FLD_CHECKLIST_GROUP));

			return checklistgroup; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistGroup(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ChecklistGroup checklistgroup) throws DBException{ 
		try{ 
			PstChecklistGroup pstChecklistGroup = new PstChecklistGroup(0);

			pstChecklistGroup.setString(FLD_CHECKLIST_GROUP, checklistgroup.getChecklistGroup());

			pstChecklistGroup.insert(); 
			checklistgroup.setOID(pstChecklistGroup.getlong(FLD_CHECKLIST_GROUP_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistGroup(0),DBException.UNKNOWN); 
		}
		return checklistgroup.getOID();
	}

	public static long updateExc(ChecklistGroup checklistgroup) throws DBException{ 
		try{ 
			if(checklistgroup.getOID() != 0){ 
				PstChecklistGroup pstChecklistGroup = new PstChecklistGroup(checklistgroup.getOID());

				pstChecklistGroup.setString(FLD_CHECKLIST_GROUP, checklistgroup.getChecklistGroup());

				pstChecklistGroup.update(); 
				return checklistgroup.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistGroup(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstChecklistGroup pstChecklistGroup = new PstChecklistGroup(oid);
			pstChecklistGroup.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChecklistGroup(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_CHECKLIST_GROUP; 
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
				ChecklistGroup checklistgroup = new ChecklistGroup();
				resultToObject(rs, checklistgroup);
				lists.add(checklistgroup);
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

	public static void resultToObject(ResultSet rs, ChecklistGroup checklistgroup){
		try{
			checklistgroup.setOID(rs.getLong(PstChecklistGroup.fieldNames[PstChecklistGroup.FLD_CHECKLIST_GROUP_ID]));
			checklistgroup.setChecklistGroup(rs.getString(PstChecklistGroup.fieldNames[PstChecklistGroup.FLD_CHECKLIST_GROUP]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long checklistGroupId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CHECKLIST_GROUP + " WHERE " + 
						PstChecklistGroup.fieldNames[PstChecklistGroup.FLD_CHECKLIST_GROUP_ID] + " = " + checklistGroupId;

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
			String sql = "SELECT COUNT("+ PstChecklistGroup.fieldNames[PstChecklistGroup.FLD_CHECKLIST_GROUP_ID] + ") FROM " + TBL_HR_CHECKLIST_GROUP;
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
			  	   ChecklistGroup checklistgroup = (ChecklistGroup)list.get(ls);
				   if(oid == checklistgroup.getOID())
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
