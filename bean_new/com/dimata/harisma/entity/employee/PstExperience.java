
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
import com.dimata. harisma.entity.employee.*; 

public class PstExperience extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_WORK_HISTORY_PAST = "HR_WORK_HISTORY_PAST";

	public static final  int FLD_WORK_HISTORY_PAST_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_COMPANY_NAME = 2;
	public static final  int FLD_START_DATE = 3;
	public static final  int FLD_END_DATE = 4;
	public static final  int FLD_POSITION = 5;
	public static final  int FLD_MOVE_REASON = 6;

	public static final  String[] fieldNames = {
		"WORK_HISTORY_PAST_ID",
		"EMPLOYEE_ID",
		"COMPANY_NAME",
		"START_DATE",
		"END_DATE",
		"POSITION",
		"MOVE_REASON"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_INT,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstExperience(){
	}

	public PstExperience(int i) throws DBException { 
		super(new PstExperience()); 
	}

	public PstExperience(String sOid) throws DBException { 
		super(new PstExperience(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstExperience(long lOid) throws DBException { 
		super(new PstExperience(0)); 
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
		return TBL_HR_WORK_HISTORY_PAST;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstExperience().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Experience experience = fetchExc(ent.getOID()); 
		ent = (Entity)experience; 
		return experience.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Experience) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Experience) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Experience fetchExc(long oid) throws DBException{ 
		try{ 
			Experience experience = new Experience();
			PstExperience pstExperience = new PstExperience(oid); 
			experience.setOID(oid);

			experience.setEmployeeId(pstExperience.getlong(FLD_EMPLOYEE_ID));
			experience.setCompanyName(pstExperience.getString(FLD_COMPANY_NAME));
			experience.setStartDate(pstExperience.getInt(FLD_START_DATE));
			experience.setEndDate(pstExperience.getInt(FLD_END_DATE));
			experience.setPosition(pstExperience.getString(FLD_POSITION));
			experience.setMoveReason(pstExperience.getString(FLD_MOVE_REASON));

			return experience; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExperience(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Experience experience) throws DBException{ 
		try{ 
			PstExperience pstExperience = new PstExperience(0);

			pstExperience.setLong(FLD_EMPLOYEE_ID, experience.getEmployeeId());
			pstExperience.setString(FLD_COMPANY_NAME, experience.getCompanyName());
			pstExperience.setInt(FLD_START_DATE, experience.getStartDate());
			pstExperience.setInt(FLD_END_DATE, experience.getEndDate());
			pstExperience.setString(FLD_POSITION, experience.getPosition());
			pstExperience.setString(FLD_MOVE_REASON, experience.getMoveReason());

			pstExperience.insert(); 
			experience.setOID(pstExperience.getlong(FLD_WORK_HISTORY_PAST_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExperience(0),DBException.UNKNOWN); 
		}
		return experience.getOID();
	}

	public static long updateExc(Experience experience) throws DBException{ 
		try{ 
			if(experience.getOID() != 0){ 
				PstExperience pstExperience = new PstExperience(experience.getOID());

				pstExperience.setLong(FLD_EMPLOYEE_ID, experience.getEmployeeId());
				pstExperience.setString(FLD_COMPANY_NAME, experience.getCompanyName());
				pstExperience.setInt(FLD_START_DATE, experience.getStartDate());
				pstExperience.setInt(FLD_END_DATE, experience.getEndDate());
				pstExperience.setString(FLD_POSITION, experience.getPosition());
				pstExperience.setString(FLD_MOVE_REASON, experience.getMoveReason());

				pstExperience.update(); 
				return experience.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExperience(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstExperience pstExperience = new PstExperience(oid);
			pstExperience.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExperience(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_PAST; 
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
				Experience experience = new Experience();
				resultToObject(rs, experience);
				lists.add(experience);
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

	private static void resultToObject(ResultSet rs, Experience experience){
		try{
			experience.setOID(rs.getLong(PstExperience.fieldNames[PstExperience.FLD_WORK_HISTORY_PAST_ID]));
			experience.setEmployeeId(rs.getLong(PstExperience.fieldNames[PstExperience.FLD_EMPLOYEE_ID]));
			experience.setCompanyName(rs.getString(PstExperience.fieldNames[PstExperience.FLD_COMPANY_NAME]));
			experience.setStartDate(rs.getInt(PstExperience.fieldNames[PstExperience.FLD_START_DATE]));
			experience.setEndDate(rs.getInt(PstExperience.fieldNames[PstExperience.FLD_END_DATE]));
			experience.setPosition(rs.getString(PstExperience.fieldNames[PstExperience.FLD_POSITION]));
			experience.setMoveReason(rs.getString(PstExperience.fieldNames[PstExperience.FLD_MOVE_REASON]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long workHistoryPastId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_PAST + " WHERE " + 
						PstExperience.fieldNames[PstExperience.FLD_WORK_HISTORY_PAST_ID] + " = " + workHistoryPastId;

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
			String sql = "SELECT COUNT("+ PstExperience.fieldNames[PstExperience.FLD_WORK_HISTORY_PAST_ID] + ") FROM " + TBL_HR_WORK_HISTORY_PAST;
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
			  	   Experience experience = (Experience)list.get(ls);
				   if(oid == experience.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

     public static int findLimitCommand(int start, int recordToGet, int vectSize)
    {
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


    public static long deleteByEmployee(long emplOID)
    {
    	try{
            String sql = " DELETE FROM "+PstExperience.TBL_HR_WORK_HISTORY_PAST+
                		 " WHERE "+PstExperience.fieldNames[PstExperience.FLD_EMPLOYEE_ID] +
                         " = "+emplOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return emplOID;
    }




}
