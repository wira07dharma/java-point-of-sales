
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

public class PstLockerCondition extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LOCKER_CONDITION = "HR_LOCKER_CONDITION";

	public static final  int FLD_CONDITION_ID = 0;
	public static final  int FLD_CONDITION = 1;

	public static final  String[] fieldNames = {
		"CONDITION_ID",
		"CONDITION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstLockerCondition(){
	}

	public PstLockerCondition(int i) throws DBException { 
		super(new PstLockerCondition()); 
	}

	public PstLockerCondition(String sOid) throws DBException { 
		super(new PstLockerCondition(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLockerCondition(long lOid) throws DBException { 
		super(new PstLockerCondition(0)); 
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
		return TBL_HR_LOCKER_CONDITION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLockerCondition().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		LockerCondition lockercondition = fetchExc(ent.getOID()); 
		ent = (Entity)lockercondition; 
		return lockercondition.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((LockerCondition) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((LockerCondition) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static LockerCondition fetchExc(long oid) throws DBException{ 
		try{ 
			LockerCondition lockercondition = new LockerCondition();
			PstLockerCondition pstLockerCondition = new PstLockerCondition(oid); 
			lockercondition.setOID(oid);

			lockercondition.setCondition(pstLockerCondition.getString(FLD_CONDITION));

			return lockercondition; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerCondition(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(LockerCondition lockercondition) throws DBException{ 
		try{ 
			PstLockerCondition pstLockerCondition = new PstLockerCondition(0);

			pstLockerCondition.setString(FLD_CONDITION, lockercondition.getCondition());

			pstLockerCondition.insert(); 
			lockercondition.setOID(pstLockerCondition.getlong(FLD_CONDITION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerCondition(0),DBException.UNKNOWN); 
		}
		return lockercondition.getOID();
	}

	public static long updateExc(LockerCondition lockercondition) throws DBException{ 
		try{ 
			if(lockercondition.getOID() != 0){ 
				PstLockerCondition pstLockerCondition = new PstLockerCondition(lockercondition.getOID());

				pstLockerCondition.setString(FLD_CONDITION, lockercondition.getCondition());

				pstLockerCondition.update(); 
				return lockercondition.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerCondition(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLockerCondition pstLockerCondition = new PstLockerCondition(oid);
			pstLockerCondition.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLockerCondition(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_CONDITION; 
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
				LockerCondition lockercondition = new LockerCondition();
				resultToObject(rs, lockercondition);
				lists.add(lockercondition);
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

	public static void resultToObject(ResultSet rs, LockerCondition lockercondition){
		try{
			lockercondition.setOID(rs.getLong(PstLockerCondition.fieldNames[PstLockerCondition.FLD_CONDITION_ID]));
			lockercondition.setCondition(rs.getString(PstLockerCondition.fieldNames[PstLockerCondition.FLD_CONDITION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long conditionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LOCKER_CONDITION + " WHERE " + 
						PstLockerCondition.fieldNames[PstLockerCondition.FLD_CONDITION_ID] + " = " + conditionId;

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
			String sql = "SELECT COUNT("+ PstLockerCondition.fieldNames[PstLockerCondition.FLD_CONDITION_ID] + ") FROM " + TBL_HR_LOCKER_CONDITION;
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
			  	   LockerCondition lockercondition = (LockerCondition)list.get(ls);
				   if(oid == lockercondition.getOID())
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
