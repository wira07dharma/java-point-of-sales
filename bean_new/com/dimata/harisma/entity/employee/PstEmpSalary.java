
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

public class PstEmpSalary extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_EMP_SALARY = "HR_EMP_SALARY";

	public static final  int FLD_EMP_SALARY_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_LOS1 = 2;
	public static final  int FLD_LOS2 = 3;
	public static final  int FLD_CURR_BASIC = 4;
	public static final  int FLD_CURR_TRANSPORT = 5;
	public static final  int FLD_CURR_TOTAL = 6;
	public static final  int FLD_NEW_BASIC = 7;
	public static final  int FLD_NEW_TRANSPORT = 8;
	public static final  int FLD_NEW_TOTAL = 9;
	public static final  int FLD_INC_SALARY = 10;
	public static final  int FLD_INC_TRANSPORT = 11;
	public static final  int FLD_ADDITIONAL = 12;
	public static final  int FLD_INC_TOTAL = 13;
	public static final  int FLD_PERCENTAGE_BASIC = 14;
	public static final  int FLD_PERCENT_TRANSPORT = 15;
	public static final  int FLD_PERCENTAGE_TOTAL = 16;
    public static final  int FLD_CURR_DATE = 17;

	public static final  String[] fieldNames = {
		"EMP_SALARY_ID",
		"EMPLOYEE_ID",
		"LOS1",
		"LOS2",
		"CURR_BASIC",
		"CURR_TRANSPORT",
		"CURR_TOTAL",
		"NEW_BASIC",
		"NEW_TRANSPORT",
		"NEW_TOTAL",
		"INC_SALARY",
		"INC_TRANSPORT",
		"ADDITIONAL",
		"INC_TOTAL",
		"PERCENTAGE_BASIC",
		"PERCENT_TRANSPORT",
		"PERCENTAGE_TOTAL",
        "CURR_DATE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_INT,
		TYPE_INT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_FLOAT,
        TYPE_DATE
	 }; 

	public PstEmpSalary(){
	}

	public PstEmpSalary(int i) throws DBException { 
		super(new PstEmpSalary()); 
	}

	public PstEmpSalary(String sOid) throws DBException { 
		super(new PstEmpSalary(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstEmpSalary(long lOid) throws DBException { 
		super(new PstEmpSalary(0)); 
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
		return TBL_HR_EMP_SALARY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstEmpSalary().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		EmpSalary empsalary = fetchExc(ent.getOID()); 
		ent = (Entity)empsalary; 
		return empsalary.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((EmpSalary) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((EmpSalary) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static EmpSalary fetchExc(long oid) throws DBException{ 
		try{ 
			EmpSalary empsalary = new EmpSalary();
			PstEmpSalary pstEmpSalary = new PstEmpSalary(oid); 
			empsalary.setOID(oid);

			empsalary.setEmployeeId(pstEmpSalary.getlong(FLD_EMPLOYEE_ID));
			empsalary.setLos1(pstEmpSalary.getInt(FLD_LOS1));
			empsalary.setLos2(pstEmpSalary.getInt(FLD_LOS2));
			empsalary.setCurrBasic(pstEmpSalary.getdouble(FLD_CURR_BASIC));
			empsalary.setCurrTransport(pstEmpSalary.getdouble(FLD_CURR_TRANSPORT));
			empsalary.setCurrTotal(pstEmpSalary.getdouble(FLD_CURR_TOTAL));
			empsalary.setNewBasic(pstEmpSalary.getdouble(FLD_NEW_BASIC));
			empsalary.setNewTransport(pstEmpSalary.getdouble(FLD_NEW_TRANSPORT));
			empsalary.setNewTotal(pstEmpSalary.getdouble(FLD_NEW_TOTAL));
			empsalary.setIncSalary(pstEmpSalary.getdouble(FLD_INC_SALARY));
			empsalary.setIncTransport(pstEmpSalary.getdouble(FLD_INC_TRANSPORT));
			empsalary.setAdditional(pstEmpSalary.getdouble(FLD_ADDITIONAL));
			empsalary.setIncTotal(pstEmpSalary.getdouble(FLD_INC_TOTAL));
			empsalary.setPercentageBasic(pstEmpSalary.getdouble(FLD_PERCENTAGE_BASIC));
			empsalary.setPercentTransport(pstEmpSalary.getdouble(FLD_PERCENT_TRANSPORT));
			empsalary.setPercentageTotal(pstEmpSalary.getdouble(FLD_PERCENTAGE_TOTAL));
            empsalary.setCurrDate(pstEmpSalary.getDate(FLD_CURR_DATE));

			return empsalary; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSalary(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(EmpSalary empsalary) throws DBException{ 
		try{ 
			PstEmpSalary pstEmpSalary = new PstEmpSalary(0);

			pstEmpSalary.setLong(FLD_EMPLOYEE_ID, empsalary.getEmployeeId());
			pstEmpSalary.setInt(FLD_LOS1, empsalary.getLos1());
			pstEmpSalary.setInt(FLD_LOS2, empsalary.getLos2());
			pstEmpSalary.setDouble(FLD_CURR_BASIC, empsalary.getCurrBasic());
			pstEmpSalary.setDouble(FLD_CURR_TRANSPORT, empsalary.getCurrTransport());
			pstEmpSalary.setDouble(FLD_CURR_TOTAL, empsalary.getCurrTotal());
			pstEmpSalary.setDouble(FLD_NEW_BASIC, empsalary.getNewBasic());
			pstEmpSalary.setDouble(FLD_NEW_TRANSPORT, empsalary.getNewTransport());
			pstEmpSalary.setDouble(FLD_NEW_TOTAL, empsalary.getNewTotal());
			pstEmpSalary.setDouble(FLD_INC_SALARY, empsalary.getIncSalary());
			pstEmpSalary.setDouble(FLD_INC_TRANSPORT, empsalary.getIncTransport());
			pstEmpSalary.setDouble(FLD_ADDITIONAL, empsalary.getAdditional());
			pstEmpSalary.setDouble(FLD_INC_TOTAL, empsalary.getIncTotal());
			pstEmpSalary.setDouble(FLD_PERCENTAGE_BASIC, empsalary.getPercentageBasic());
			pstEmpSalary.setDouble(FLD_PERCENT_TRANSPORT, empsalary.getPercentTransport());
			pstEmpSalary.setDouble(FLD_PERCENTAGE_TOTAL, empsalary.getPercentageTotal());
            pstEmpSalary.setDate(FLD_CURR_DATE, empsalary.getCurrDate());

			pstEmpSalary.insert(); 
			empsalary.setOID(pstEmpSalary.getlong(FLD_EMP_SALARY_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSalary(0),DBException.UNKNOWN); 
		}
		return empsalary.getOID();
	}

	public static long updateExc(EmpSalary empsalary) throws DBException{ 
		try{ 
			if(empsalary.getOID() != 0){ 
				PstEmpSalary pstEmpSalary = new PstEmpSalary(empsalary.getOID());

				pstEmpSalary.setLong(FLD_EMPLOYEE_ID, empsalary.getEmployeeId());
				pstEmpSalary.setInt(FLD_LOS1, empsalary.getLos1());
				pstEmpSalary.setInt(FLD_LOS2, empsalary.getLos2());
				pstEmpSalary.setDouble(FLD_CURR_BASIC, empsalary.getCurrBasic());
				pstEmpSalary.setDouble(FLD_CURR_TRANSPORT, empsalary.getCurrTransport());
				pstEmpSalary.setDouble(FLD_CURR_TOTAL, empsalary.getCurrTotal());
				pstEmpSalary.setDouble(FLD_NEW_BASIC, empsalary.getNewBasic());
				pstEmpSalary.setDouble(FLD_NEW_TRANSPORT, empsalary.getNewTransport());
				pstEmpSalary.setDouble(FLD_NEW_TOTAL, empsalary.getNewTotal());
				pstEmpSalary.setDouble(FLD_INC_SALARY, empsalary.getIncSalary());
				pstEmpSalary.setDouble(FLD_INC_TRANSPORT, empsalary.getIncTransport());
				pstEmpSalary.setDouble(FLD_ADDITIONAL, empsalary.getAdditional());
				pstEmpSalary.setDouble(FLD_INC_TOTAL, empsalary.getIncTotal());
				pstEmpSalary.setDouble(FLD_PERCENTAGE_BASIC, empsalary.getPercentageBasic());
				pstEmpSalary.setDouble(FLD_PERCENT_TRANSPORT, empsalary.getPercentTransport());
				pstEmpSalary.setDouble(FLD_PERCENTAGE_TOTAL, empsalary.getPercentageTotal());
                pstEmpSalary.setDate(FLD_CURR_DATE, empsalary.getCurrDate());

				pstEmpSalary.update(); 
				return empsalary.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSalary(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstEmpSalary pstEmpSalary = new PstEmpSalary(oid);
			pstEmpSalary.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSalary(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_EMP_SALARY; 
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
				EmpSalary empsalary = new EmpSalary();
				resultToObject(rs, empsalary);
				lists.add(empsalary);
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

	private static void resultToObject(ResultSet rs, EmpSalary empsalary){
		try{
			empsalary.setOID(rs.getLong(PstEmpSalary.fieldNames[PstEmpSalary.FLD_EMP_SALARY_ID]));
			empsalary.setEmployeeId(rs.getLong(PstEmpSalary.fieldNames[PstEmpSalary.FLD_EMPLOYEE_ID]));
			empsalary.setLos1(rs.getInt(PstEmpSalary.fieldNames[PstEmpSalary.FLD_LOS1]));
			empsalary.setLos2(rs.getInt(PstEmpSalary.fieldNames[PstEmpSalary.FLD_LOS2]));
			empsalary.setCurrBasic(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_CURR_BASIC]));
			empsalary.setCurrTransport(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_CURR_TRANSPORT]));
			empsalary.setCurrTotal(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_CURR_TOTAL]));
			empsalary.setNewBasic(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_NEW_BASIC]));
			empsalary.setNewTransport(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_NEW_TRANSPORT]));
			empsalary.setNewTotal(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_NEW_TOTAL]));
			empsalary.setIncSalary(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_INC_SALARY]));
			empsalary.setIncTransport(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_INC_TRANSPORT]));
			empsalary.setAdditional(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_ADDITIONAL]));
			empsalary.setIncTotal(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_INC_TOTAL]));
			empsalary.setPercentageBasic(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_PERCENTAGE_BASIC]));
			empsalary.setPercentTransport(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_PERCENT_TRANSPORT]));
			empsalary.setPercentageTotal(rs.getDouble(PstEmpSalary.fieldNames[PstEmpSalary.FLD_PERCENTAGE_TOTAL]));
            empsalary.setCurrDate(rs.getDate(PstEmpSalary.fieldNames[PstEmpSalary.FLD_CURR_DATE]));
		}catch(Exception e){ }
	}

	public static boolean checkOID(long empSalaryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EMP_SALARY + " WHERE " + 
						PstEmpSalary.fieldNames[PstEmpSalary.FLD_EMP_SALARY_ID] + " = " + empSalaryId;

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
			String sql = "SELECT COUNT("+ PstEmpSalary.fieldNames[PstEmpSalary.FLD_EMP_SALARY_ID] + ") FROM " + TBL_HR_EMP_SALARY;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   EmpSalary empsalary = (EmpSalary)list.get(ls);
				   if(oid == empsalary.getOID())
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



    public static long deleteByEmployee(long empOid){
    	try{
        	String sql = " DELETE FROM "+PstEmpSalary.TBL_HR_EMP_SALARY+
                		 " WHERE "+PstEmpSalary.fieldNames[PstEmpSalary.FLD_EMPLOYEE_ID]+
                         " = "+empOid;

        	int status = DBHandler.execUpdate(sql);

    	}catch(Exception exc){
        	System.out.println("error delete Emp Salary "+exc.toString());
    	}
    	return empOid;
    }
}
