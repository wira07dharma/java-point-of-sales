
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

public class PstRecognition extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECOGNITION = "HR_RECOGNITION";

	public static final  int FLD_RECOGNITION_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_RECOG_DATE = 2;
	public static final  int FLD_POINT = 3;

	public static final  String[] fieldNames = {
		"RECOGNITION_ID",
		"EMPLOYEE_ID",
		"RECOG_DATE",
		"POINT"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_INT
	 }; 

	public PstRecognition(){
	}

	public PstRecognition(int i) throws DBException { 
		super(new PstRecognition()); 
	}

	public PstRecognition(String sOid) throws DBException { 
		super(new PstRecognition(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecognition(long lOid) throws DBException { 
		super(new PstRecognition(0)); 
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
		return TBL_HR_RECOGNITION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecognition().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Recognition recognition = fetchExc(ent.getOID()); 
		ent = (Entity)recognition; 
		return recognition.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Recognition) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Recognition) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Recognition fetchExc(long oid) throws DBException{ 
		try{ 
			Recognition recognition = new Recognition();
			PstRecognition pstRecognition = new PstRecognition(oid); 
			recognition.setOID(oid);

			recognition.setEmployeeId(pstRecognition.getlong(FLD_EMPLOYEE_ID));
			recognition.setRecogDate(pstRecognition.getDate(FLD_RECOG_DATE));
			recognition.setPoint(pstRecognition.getInt(FLD_POINT));

			return recognition; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecognition(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Recognition recognition) throws DBException{ 
		try{ 
			PstRecognition pstRecognition = new PstRecognition(0);

			pstRecognition.setLong(FLD_EMPLOYEE_ID, recognition.getEmployeeId());
			pstRecognition.setDate(FLD_RECOG_DATE, recognition.getRecogDate());
			pstRecognition.setInt(FLD_POINT, recognition.getPoint());

			pstRecognition.insert(); 
			recognition.setOID(pstRecognition.getlong(FLD_RECOGNITION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecognition(0),DBException.UNKNOWN); 
		}
		return recognition.getOID();
	}

	public static long updateExc(Recognition recognition) throws DBException{ 
		try{ 
			if(recognition.getOID() != 0){ 
				PstRecognition pstRecognition = new PstRecognition(recognition.getOID());

				pstRecognition.setLong(FLD_EMPLOYEE_ID, recognition.getEmployeeId());
				pstRecognition.setDate(FLD_RECOG_DATE, recognition.getRecogDate());
				pstRecognition.setInt(FLD_POINT, recognition.getPoint());

				pstRecognition.update(); 
				return recognition.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecognition(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecognition pstRecognition = new PstRecognition(oid);
			pstRecognition.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecognition(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECOGNITION; 
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
				Recognition recognition = new Recognition();
				resultToObject(rs, recognition);
				lists.add(recognition);
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

	public static void resultToObject(ResultSet rs, Recognition recognition){
		try{
			recognition.setOID(rs.getLong(PstRecognition.fieldNames[PstRecognition.FLD_RECOGNITION_ID]));
			recognition.setEmployeeId(rs.getLong(PstRecognition.fieldNames[PstRecognition.FLD_EMPLOYEE_ID]));
			recognition.setRecogDate(rs.getDate(PstRecognition.fieldNames[PstRecognition.FLD_RECOG_DATE]));
			recognition.setPoint(rs.getInt(PstRecognition.fieldNames[PstRecognition.FLD_POINT]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recognitionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECOGNITION + " WHERE " + 
						PstRecognition.fieldNames[PstRecognition.FLD_RECOGNITION_ID] + " = " + recognitionId;

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
			String sql = "SELECT COUNT("+ PstRecognition.fieldNames[PstRecognition.FLD_RECOGNITION_ID] + ") FROM " + TBL_HR_RECOGNITION;
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
			  	   Recognition recognition = (Recognition)list.get(ls);
				   if(oid == recognition.getOID())
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
