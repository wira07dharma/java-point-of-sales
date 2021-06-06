
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
import com.dimata.harisma.entity.employee.*; 

public class PstCareerPath extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_WORK_HISTORY_NOW = "HR_WORK_HISTORY_NOW";

	public static final  int FLD_WORK_HISTORY_NOW_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_DEPARTMENT_ID = 2;
	public static final  int FLD_DEPARTMENT = 3;
	public static final  int FLD_POSITION_ID = 4;
	public static final  int FLD_POSITION = 5;
	public static final  int FLD_SECTION_ID = 6;
	public static final  int FLD_SECTION = 7;
	public static final  int FLD_WORK_FROM = 8;
	public static final  int FLD_WORK_TO = 9;
	public static final  int FLD_DESCRIPTION = 10;

	public static final  String[] fieldNames = {
		"WORK_HISTORY_NOW_ID",
		"EMPLOYEE_ID",
		"DEPARTMENT_ID",
		"DEPARTMENT",
		"POSITION_ID",
		"POSITION",
		"SECTION_ID",
		"SECTION",
		"WORK_FROM",
		"WORK_TO",
		"DESCRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_STRING
	 }; 

	public PstCareerPath(){
	}

	public PstCareerPath(int i) throws DBException { 
		super(new PstCareerPath()); 
	}

	public PstCareerPath(String sOid) throws DBException { 
		super(new PstCareerPath(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstCareerPath(long lOid) throws DBException { 
		super(new PstCareerPath(0)); 
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
		return TBL_HR_WORK_HISTORY_NOW;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstCareerPath().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		CareerPath careerpath = fetchExc(ent.getOID()); 
		ent = (Entity)careerpath; 
		return careerpath.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((CareerPath) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((CareerPath) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static CareerPath fetchExc(long oid) throws DBException{ 
		try{ 
			CareerPath careerpath = new CareerPath();
			PstCareerPath pstCareerPath = new PstCareerPath(oid); 
			careerpath.setOID(oid);

			careerpath.setEmployeeId(pstCareerPath.getlong(FLD_EMPLOYEE_ID));
			careerpath.setDepartmentId(pstCareerPath.getlong(FLD_DEPARTMENT_ID));
			careerpath.setDepartment(pstCareerPath.getString(FLD_DEPARTMENT));
			careerpath.setPositionId(pstCareerPath.getlong(FLD_POSITION_ID));
			careerpath.setPosition(pstCareerPath.getString(FLD_POSITION));
			careerpath.setSectionId(pstCareerPath.getlong(FLD_SECTION_ID));
			careerpath.setSection(pstCareerPath.getString(FLD_SECTION));
			careerpath.setWorkFrom(pstCareerPath.getDate(FLD_WORK_FROM));
			careerpath.setWorkTo(pstCareerPath.getDate(FLD_WORK_TO));
			careerpath.setDescription(pstCareerPath.getString(FLD_DESCRIPTION));

			return careerpath; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCareerPath(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(CareerPath careerpath) throws DBException{ 
		try{ 
			PstCareerPath pstCareerPath = new PstCareerPath(0);

			pstCareerPath.setLong(FLD_EMPLOYEE_ID, careerpath.getEmployeeId());
			pstCareerPath.setLong(FLD_DEPARTMENT_ID, careerpath.getDepartmentId());
			pstCareerPath.setString(FLD_DEPARTMENT, careerpath.getDepartment());
			pstCareerPath.setLong(FLD_POSITION_ID, careerpath.getPositionId());
			pstCareerPath.setString(FLD_POSITION, careerpath.getPosition());
			pstCareerPath.setLong(FLD_SECTION_ID, careerpath.getSectionId());
			pstCareerPath.setString(FLD_SECTION, careerpath.getSection());
			pstCareerPath.setDate(FLD_WORK_FROM, careerpath.getWorkFrom());
			pstCareerPath.setDate(FLD_WORK_TO, careerpath.getWorkTo());
			pstCareerPath.setString(FLD_DESCRIPTION, careerpath.getDescription());

			pstCareerPath.insert(); 
			careerpath.setOID(pstCareerPath.getlong(FLD_WORK_HISTORY_NOW_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCareerPath(0),DBException.UNKNOWN); 
		}
		return careerpath.getOID();
	}

	public static long updateExc(CareerPath careerpath) throws DBException{ 
		try{ 
			if(careerpath.getOID() != 0){ 
				PstCareerPath pstCareerPath = new PstCareerPath(careerpath.getOID());

				pstCareerPath.setLong(FLD_EMPLOYEE_ID, careerpath.getEmployeeId());
				pstCareerPath.setLong(FLD_DEPARTMENT_ID, careerpath.getDepartmentId());
				pstCareerPath.setString(FLD_DEPARTMENT, careerpath.getDepartment());
				pstCareerPath.setLong(FLD_POSITION_ID, careerpath.getPositionId());
				pstCareerPath.setString(FLD_POSITION, careerpath.getPosition());
				pstCareerPath.setLong(FLD_SECTION_ID, careerpath.getSectionId());
				pstCareerPath.setString(FLD_SECTION, careerpath.getSection());
				pstCareerPath.setDate(FLD_WORK_FROM, careerpath.getWorkFrom());
				pstCareerPath.setDate(FLD_WORK_TO, careerpath.getWorkTo());
				pstCareerPath.setString(FLD_DESCRIPTION, careerpath.getDescription());

				pstCareerPath.update(); 
				return careerpath.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCareerPath(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstCareerPath pstCareerPath = new PstCareerPath(oid);
			pstCareerPath.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCareerPath(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW; 
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
				CareerPath careerpath = new CareerPath();
				resultToObject(rs, careerpath);
				lists.add(careerpath);
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

	private static void resultToObject(ResultSet rs, CareerPath careerpath){
		try{
			careerpath.setOID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID]));
			careerpath.setEmployeeId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]));
			careerpath.setDepartmentId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT_ID]));
			careerpath.setDepartment(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT]));
			careerpath.setPositionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION_ID]));
			careerpath.setPosition(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION]));
			careerpath.setSectionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION_ID]));
			careerpath.setSection(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION]));
			careerpath.setWorkFrom(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]));
			careerpath.setWorkTo(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO]));
			careerpath.setDescription(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long workHistoryNowId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE " + 
						PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID] + " = " + workHistoryNowId;

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
			String sql = "SELECT COUNT("+ PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID] + ") FROM " + TBL_HR_WORK_HISTORY_NOW;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause,String orderClause){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   CareerPath careerpath = (CareerPath)list.get(ls);
				   if(oid == careerpath.getOID())
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
        vectSize = vectSize + mdl;
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
            String sql = " DELETE FROM "+PstCareerPath.TBL_HR_WORK_HISTORY_NOW +
                		 " WHERE "+PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] +
                         " = "+emplOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return emplOID;
    }

    public static boolean checkDepartment(long departmentId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE " + 
						PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT_ID] + " = " + departmentId;

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

    public static boolean checkPosition(long positionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE " + 
						PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION_ID] + " = " + positionId;

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


    public static boolean checkSection(long sectionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE " + 
						PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION] + " = " + sectionId;

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



}
