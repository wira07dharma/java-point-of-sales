
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

package com.dimata.harisma.entity.employee; 

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
import com.dimata.harisma.entity.employee.*; 

public class PstSpecialAchievement extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_SPECIAL_ACHIEVEMENT = "HR_SPECIAL_ACHIEVEMENT";

	public static final  int FLD_SPECIAL_ACHIEVEMENT_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_TYPE_OF_AWARD = 2;
	public static final  int FLD_PRESENTED_BY = 3;
	public static final  int FLD_DATE = 4;

	public static final  String[] fieldNames = {
		"SPECIAL_ACHIEVEMENT_ID",
		"EMPLOYEE_ID",
		"TYPE_OF_AWARD",
		"PRESENTED_BY",
		"DATE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE
	 }; 

	public PstSpecialAchievement(){
	}

	public PstSpecialAchievement(int i) throws DBException { 
		super(new PstSpecialAchievement()); 
	}

	public PstSpecialAchievement(String sOid) throws DBException { 
		super(new PstSpecialAchievement(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstSpecialAchievement(long lOid) throws DBException { 
		super(new PstSpecialAchievement(0)); 
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
		return TBL_HR_SPECIAL_ACHIEVEMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstSpecialAchievement().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		SpecialAchievement specialachievement = fetchExc(ent.getOID()); 
		ent = (Entity)specialachievement; 
		return specialachievement.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((SpecialAchievement) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((SpecialAchievement) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static SpecialAchievement fetchExc(long oid) throws DBException{ 
		try{ 
			SpecialAchievement specialachievement = new SpecialAchievement();
			PstSpecialAchievement pstSpecialAchievement = new PstSpecialAchievement(oid); 
			specialachievement.setOID(oid);

			specialachievement.setEmployeeId(pstSpecialAchievement.getlong(FLD_EMPLOYEE_ID));
			specialachievement.setTypeOfAward(pstSpecialAchievement.getString(FLD_TYPE_OF_AWARD));
			specialachievement.setPresentedBy(pstSpecialAchievement.getString(FLD_PRESENTED_BY));
			specialachievement.setDate(pstSpecialAchievement.getDate(FLD_DATE));

			return specialachievement; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){
            System.out.println(e);
			throw new DBException(new PstSpecialAchievement(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(SpecialAchievement specialachievement) throws DBException{ 
		try{ 
			PstSpecialAchievement pstSpecialAchievement = new PstSpecialAchievement(0);

			pstSpecialAchievement.setLong(FLD_EMPLOYEE_ID, specialachievement.getEmployeeId());
			pstSpecialAchievement.setString(FLD_TYPE_OF_AWARD, specialachievement.getTypeOfAward());
			pstSpecialAchievement.setString(FLD_PRESENTED_BY, specialachievement.getPresentedBy());
			pstSpecialAchievement.setDate(FLD_DATE, specialachievement.getDate());

			pstSpecialAchievement.insert(); 
			specialachievement.setOID(pstSpecialAchievement.getlong(FLD_SPECIAL_ACHIEVEMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstSpecialAchievement(0),DBException.UNKNOWN); 
		}
		return specialachievement.getOID();
	}

	public static long updateExc(SpecialAchievement specialachievement) throws DBException{ 
		try{ 
			if(specialachievement.getOID() != 0){ 
				PstSpecialAchievement pstSpecialAchievement = new PstSpecialAchievement(specialachievement.getOID());

				pstSpecialAchievement.setLong(FLD_EMPLOYEE_ID, specialachievement.getEmployeeId());
				pstSpecialAchievement.setString(FLD_TYPE_OF_AWARD, specialachievement.getTypeOfAward());
				pstSpecialAchievement.setString(FLD_PRESENTED_BY, specialachievement.getPresentedBy());
				pstSpecialAchievement.setDate(FLD_DATE, specialachievement.getDate());

				pstSpecialAchievement.update(); 
				return specialachievement.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstSpecialAchievement(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstSpecialAchievement pstSpecialAchievement = new PstSpecialAchievement(oid);
			pstSpecialAchievement.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstSpecialAchievement(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_SPECIAL_ACHIEVEMENT; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;

			switch (DBHandler.DBSVR_TYPE) { 
			case DBHandler.DBSVR_MYSQL : 
					if(limitStart == 0 && recordToGet == 0)
						sql = sql + ""; 
					else 
						sql = sql + " LIMIT " + limitStart + ","+ recordToGet ; 
				 break;
			case DBHandler.DBSVR_POSTGRESQL : 
 					if(limitStart == 0 && recordToGet == 0) 
						sql = sql + ""; 
					else 
						sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
				 break;
			case DBHandler.DBSVR_SYBASE :
				 break;
			case DBHandler.DBSVR_ORACLE :
				 break;
			case DBHandler.DBSVR_MSSQL :
				 break;

			default:
               	if(limitStart == 0 && recordToGet == 0)
					sql = sql + ""; 
				else 
					sql = sql + " LIMIT " + limitStart + ","+ recordToGet ; 
			}
            dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				SpecialAchievement specialachievement = new SpecialAchievement();
				resultToObject(rs, specialachievement);
				lists.add(specialachievement);
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

	public static void resultToObject(ResultSet rs, SpecialAchievement specialachievement){
		try{
			specialachievement.setOID(rs.getLong(PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_SPECIAL_ACHIEVEMENT_ID]));
			specialachievement.setEmployeeId(rs.getLong(PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_EMPLOYEE_ID]));
			specialachievement.setTypeOfAward(rs.getString(PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_TYPE_OF_AWARD]));
			specialachievement.setPresentedBy(rs.getString(PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_PRESENTED_BY]));
			specialachievement.setDate(rs.getDate(PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_DATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long specialAchievementId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_SPECIAL_ACHIEVEMENT + " WHERE " + 
						PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_SPECIAL_ACHIEVEMENT_ID] + " = " + specialAchievementId;

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
			String sql = "SELECT COUNT("+ PstSpecialAchievement.fieldNames[PstSpecialAchievement.FLD_SPECIAL_ACHIEVEMENT_ID] + ") FROM " + TBL_HR_SPECIAL_ACHIEVEMENT;
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
			  	   SpecialAchievement specialachievement = (SpecialAchievement)list.get(ls);
				   if(oid == specialachievement.getOID())
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
