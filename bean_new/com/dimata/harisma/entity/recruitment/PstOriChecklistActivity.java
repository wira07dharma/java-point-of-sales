
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

package com.dimata.harisma.entity.recruitment; 

/* package java */ 
import java.sql.*
;import java.util.*
;
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

public class PstOriChecklistActivity extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_ORI_CHECKLIST_ACTIVITY = "HR_ORI_CHECKLIST_ACTIVITY";

	public static final  int FLD_ORI_CHECKLIST_ACTIVITY_ID = 0;
	public static final  int FLD_ORI_CHECKLIST_ID = 1;
	public static final  int FLD_ORI_ACTIVITY_ID = 2;
	public static final  int FLD_DONE = 3;
	public static final  int FLD_ACTIVITY_DATE = 4;

	public static final  String[] fieldNames = {
		"ORI_CHECKLIST_ACTIVITY_ID",
		"ORI_CHECKLIST_ID",
		"ORI_ACTIVITY_ID",
		"DONE",
		"ACTIVITY_DATE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT,
		TYPE_DATE
	 }; 

	public PstOriChecklistActivity(){
	}

	public PstOriChecklistActivity(int i) throws DBException { 
		super(new PstOriChecklistActivity()); 
	}

	public PstOriChecklistActivity(String sOid) throws DBException { 
		super(new PstOriChecklistActivity(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstOriChecklistActivity(long lOid) throws DBException { 
		super(new PstOriChecklistActivity(0)); 
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
		return TBL_HR_ORI_CHECKLIST_ACTIVITY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstOriChecklistActivity().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		OriChecklistActivity orichecklistactivity = fetchExc(ent.getOID()); 
		ent = (Entity)orichecklistactivity; 
		return orichecklistactivity.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((OriChecklistActivity) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((OriChecklistActivity) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static OriChecklistActivity fetchExc(long oid) throws DBException{ 
		try{ 
			OriChecklistActivity orichecklistactivity = new OriChecklistActivity();
			PstOriChecklistActivity pstOriChecklistActivity = new PstOriChecklistActivity(oid); 
			orichecklistactivity.setOID(oid);

			orichecklistactivity.setOriChecklistId(pstOriChecklistActivity.getlong(FLD_ORI_CHECKLIST_ID));
			orichecklistactivity.setOriActivityId(pstOriChecklistActivity.getlong(FLD_ORI_ACTIVITY_ID));
			orichecklistactivity.setDone(pstOriChecklistActivity.getInt(FLD_DONE));
			orichecklistactivity.setActivityDate(pstOriChecklistActivity.getDate(FLD_ACTIVITY_DATE));

			return orichecklistactivity; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklistActivity(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(OriChecklistActivity orichecklistactivity) throws DBException{ 
		try{ 
			PstOriChecklistActivity pstOriChecklistActivity = new PstOriChecklistActivity(0);

			pstOriChecklistActivity.setLong(FLD_ORI_CHECKLIST_ID, orichecklistactivity.getOriChecklistId());
			pstOriChecklistActivity.setLong(FLD_ORI_ACTIVITY_ID, orichecklistactivity.getOriActivityId());
			pstOriChecklistActivity.setInt(FLD_DONE, orichecklistactivity.getDone());
			pstOriChecklistActivity.setDate(FLD_ACTIVITY_DATE, orichecklistactivity.getActivityDate());

			pstOriChecklistActivity.insert(); 
			orichecklistactivity.setOID(pstOriChecklistActivity.getlong(FLD_ORI_CHECKLIST_ACTIVITY_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklistActivity(0),DBException.UNKNOWN); 
		}
		return orichecklistactivity.getOID();
	}

	public static long updateExc(OriChecklistActivity orichecklistactivity) throws DBException{ 
		try{ 
			if(orichecklistactivity.getOID() != 0){ 
				PstOriChecklistActivity pstOriChecklistActivity = new PstOriChecklistActivity(orichecklistactivity.getOID());

				pstOriChecklistActivity.setLong(FLD_ORI_CHECKLIST_ID, orichecklistactivity.getOriChecklistId());
				pstOriChecklistActivity.setLong(FLD_ORI_ACTIVITY_ID, orichecklistactivity.getOriActivityId());
				pstOriChecklistActivity.setInt(FLD_DONE, orichecklistactivity.getDone());
				pstOriChecklistActivity.setDate(FLD_ACTIVITY_DATE, orichecklistactivity.getActivityDate());

				pstOriChecklistActivity.update(); 
				return orichecklistactivity.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklistActivity(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstOriChecklistActivity pstOriChecklistActivity = new PstOriChecklistActivity(oid);
			pstOriChecklistActivity.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklistActivity(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_ORI_CHECKLIST_ACTIVITY; 
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
				OriChecklistActivity orichecklistactivity = new OriChecklistActivity();
				resultToObject(rs, orichecklistactivity);
				lists.add(orichecklistactivity);
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

	public static void resultToObject(ResultSet rs, OriChecklistActivity orichecklistactivity){
		try{
			orichecklistactivity.setOID(rs.getLong(PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ORI_CHECKLIST_ACTIVITY_ID]));
			orichecklistactivity.setOriChecklistId(rs.getLong(PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ORI_CHECKLIST_ID]));
			orichecklistactivity.setOriActivityId(rs.getLong(PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ORI_ACTIVITY_ID]));
			orichecklistactivity.setDone(rs.getInt(PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_DONE]));
			orichecklistactivity.setActivityDate(rs.getDate(PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ACTIVITY_DATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long oriChecklistActivityId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_ORI_CHECKLIST_ACTIVITY + " WHERE " + 
						PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ORI_CHECKLIST_ACTIVITY_ID] + " = " + oriChecklistActivityId;

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
			String sql = "SELECT COUNT("+ PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ORI_CHECKLIST_ACTIVITY_ID] + ") FROM " + TBL_HR_ORI_CHECKLIST_ACTIVITY;
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
			  	   OriChecklistActivity orichecklistactivity = (OriChecklistActivity)list.get(ls);
				   if(oid == orichecklistactivity.getOID())
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
      
        /* 27 Juni 2003 */
    public static long deleteByOriChecklist(long ocOID)
    {
    	try{
            String sql = " DELETE FROM "+PstOriChecklistActivity.TBL_HR_ORI_CHECKLIST_ACTIVITY+
                         " WHERE "+PstOriChecklistActivity.fieldNames[PstOriChecklistActivity.FLD_ORI_CHECKLIST_ID] +
                         " = "+ocOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return ocOID;
    }
}
