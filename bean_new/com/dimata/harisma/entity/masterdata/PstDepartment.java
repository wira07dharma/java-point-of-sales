
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.masterdata;

/* package java */ 
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;
import com.dimata.util.*;

/* package HRIS */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.masterdata.*; 
import com.dimata.harisma.entity.employee.*;

public class PstDepartment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_DEPARTMENT = "hr_department";

	public static final  int FLD_DEPARTMENT_ID = 0;
	public static final  int FLD_DEPARTMENT = 1;
	public static final  int FLD_DESCRIPTION = 2;
    public static final  int FLD_DIVISION_ID = 3;

	public static final  String[] fieldNames = {
		"DEPARTMENT_ID",
		"DEPARTMENT",
		"DESCRIPTION",
                "DIVISION_ID"
	 };

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
                TYPE_LONG
	 };

	public PstDepartment(){
	}

	public PstDepartment(int i) throws DBException { 
		super(new PstDepartment()); 
	}

	public PstDepartment(String sOid) throws DBException { 
		super(new PstDepartment(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDepartment(long lOid) throws DBException { 
		super(new PstDepartment(0)); 
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
		return TBL_HR_DEPARTMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDepartment().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Department department = fetchExc(ent.getOID()); 
		ent = (Entity)department; 
		return department.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Department) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Department) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Department fetchExc(long oid) throws DBException{ 
		try{ 
			Department department = new Department();
			PstDepartment pstDepartment = new PstDepartment(oid); 
			department.setOID(oid);

			department.setDepartment(pstDepartment.getString(FLD_DEPARTMENT));
			department.setDescription(pstDepartment.getString(FLD_DESCRIPTION));
            department.setDivisionId(pstDepartment.getlong(FLD_DIVISION_ID));

			return department;
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDepartment(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Department department) throws DBException{ 
		try{ 
			PstDepartment pstDepartment = new PstDepartment(0);

			pstDepartment.setString(FLD_DEPARTMENT, department.getDepartment());
			pstDepartment.setString(FLD_DESCRIPTION, department.getDescription());
            pstDepartment.setLong(FLD_DIVISION_ID, department.getDivisionId());

			pstDepartment.insert();
			department.setOID(pstDepartment.getlong(FLD_DEPARTMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDepartment(0),DBException.UNKNOWN); 
		}
		return department.getOID();
	}

	public static long updateExc(Department department) throws DBException{ 
		try{ 
			if(department.getOID() != 0){ 
				PstDepartment pstDepartment = new PstDepartment(department.getOID());

				pstDepartment.setString(FLD_DEPARTMENT, department.getDepartment());
				pstDepartment.setString(FLD_DESCRIPTION, department.getDescription());
                pstDepartment.setLong(FLD_DIVISION_ID, department.getDivisionId());

				pstDepartment.update();
				return department.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDepartment(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDepartment pstDepartment = new PstDepartment(oid);
			pstDepartment.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDepartment(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_DEPARTMENT; 
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
				Department department = new Department();
				resultToObject(rs, department);
				lists.add(department);
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

	public static void resultToObject(ResultSet rs, Department department){
		try{
			department.setOID(rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]));
			department.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
			department.setDescription(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DESCRIPTION]));
            department.setDivisionId(rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long departmentId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DEPARTMENT + " WHERE " + 
						PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentId;

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
			String sql = "SELECT COUNT("+ PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ") FROM " + TBL_HR_DEPARTMENT;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String order){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   Department department = (Department)list.get(ls);
				   if(oid == department.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

   public static boolean checkMaster(long oid)
    {
        if(PstEmployee.checkDepartment(oid))
            return true;
        else{
            if(PstCareerPath.checkDepartment(oid))
            	return true;
            else{
                 if(PstSection.checkDepartment(oid))
                  	return true;
                 else
               		 return false;
            }
        }
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

}
