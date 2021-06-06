
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
import java.sql.*
;import java.util.*
;
/* package qdep */
import com.dimata.util.lang.I_Language;
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

public class PstEmpAppraisal extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_EMPLOYEE_APPRAISAL = "HR_EMPLOYEE_APPRAISAL";

	public static final  int FLD_EMPLOYEE_APPRAISAL_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_APPRAISOR_ID = 2;
	public static final  int FLD_DATE_OF_APPRAISAL = 3;
	public static final  int FLD_LAST_APPRAISAL = 4;
	public static final  int FLD_TOTAL_SCORE = 5;
	public static final  int FLD_TOTAL_CRITERIA = 6;
	public static final  int FLD_SCORE_AVERAGE = 7;
	public static final  int FLD_DATE_PERFORMANCE = 8;
	public static final  int FLD_TIME_PERFORMANCE = 9;

	public static final  String[] fieldNames = {
		"EMPLOYEE_APPRAISAL_ID",
		"EMPLOYEE_ID",
		"APPRAISOR_ID",
		"DATE_OF_APPRAISAL",
		"LAST_APPRAISAL",
		"TOTAL_SCORE",
		"TOTAL_CRITERIA",
		"SCORE_AVERAGE",
		"DATE_PERFORMANCE",
		"TIME_PERFORMANCE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_INT,
		TYPE_INT,
		TYPE_FLOAT,
		TYPE_DATE,
		TYPE_DATE
	 }; 

	public PstEmpAppraisal(){
	}

	public PstEmpAppraisal(int i) throws DBException { 
		super(new PstEmpAppraisal()); 
	}

	public PstEmpAppraisal(String sOid) throws DBException { 
		super(new PstEmpAppraisal(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstEmpAppraisal(long lOid) throws DBException { 
		super(new PstEmpAppraisal(0)); 
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
		return TBL_HR_EMPLOYEE_APPRAISAL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstEmpAppraisal().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		EmpAppraisal empappraisal = fetchExc(ent.getOID()); 
		ent = (Entity)empappraisal; 
		return empappraisal.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((EmpAppraisal) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((EmpAppraisal) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static EmpAppraisal fetchExc(long oid) throws DBException{ 
		try{ 
			EmpAppraisal empappraisal = new EmpAppraisal();
			PstEmpAppraisal pstEmpAppraisal = new PstEmpAppraisal(oid);

			empappraisal.setOID(oid);
			empappraisal.setEmployeeId(pstEmpAppraisal.getlong(FLD_EMPLOYEE_ID));
			empappraisal.setAppraisorId(pstEmpAppraisal.getlong(FLD_APPRAISOR_ID));
			empappraisal.setDateOfAppraisal(pstEmpAppraisal.getDate(FLD_DATE_OF_APPRAISAL));
			empappraisal.setLastAppraisal(pstEmpAppraisal.getDate(FLD_LAST_APPRAISAL));
			empappraisal.setTotalScore(pstEmpAppraisal.getInt(FLD_TOTAL_SCORE));
			empappraisal.setTotalCriteria(pstEmpAppraisal.getInt(FLD_TOTAL_CRITERIA));
			empappraisal.setScoreAverage(pstEmpAppraisal.getdouble(FLD_SCORE_AVERAGE));
			empappraisal.setDatePerformance(pstEmpAppraisal.getDate(FLD_DATE_PERFORMANCE));
			empappraisal.setTimePerformance(pstEmpAppraisal.getDate(FLD_TIME_PERFORMANCE));

			return empappraisal; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){
            System.out.println("\tException on : PstEmpAppraisal.java > EmpAppraisal fetchExc(long oid) "+e);
			throw new DBException(new PstEmpAppraisal(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(EmpAppraisal empappraisal) throws DBException{ 
		try{ 
			PstEmpAppraisal pstEmpAppraisal = new PstEmpAppraisal(0);

			pstEmpAppraisal.setLong(FLD_EMPLOYEE_ID, empappraisal.getEmployeeId());
			pstEmpAppraisal.setLong(FLD_APPRAISOR_ID, empappraisal.getAppraisorId());
			pstEmpAppraisal.setDate(FLD_DATE_OF_APPRAISAL, empappraisal.getDateOfAppraisal());
			pstEmpAppraisal.setDate(FLD_LAST_APPRAISAL, empappraisal.getLastAppraisal());
			pstEmpAppraisal.setInt(FLD_TOTAL_SCORE, empappraisal.getTotalScore());
			pstEmpAppraisal.setInt(FLD_TOTAL_CRITERIA, empappraisal.getTotalCriteria());
			pstEmpAppraisal.setDouble(FLD_SCORE_AVERAGE, empappraisal.getScoreAverage());
			pstEmpAppraisal.setDate(FLD_DATE_PERFORMANCE, empappraisal.getDatePerformance());
			pstEmpAppraisal.setDate(FLD_TIME_PERFORMANCE, empappraisal.getTimePerformance());

			pstEmpAppraisal.insert(); 
			empappraisal.setOID(pstEmpAppraisal.getlong(FLD_EMPLOYEE_APPRAISAL_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpAppraisal(0),DBException.UNKNOWN); 
		}
		return empappraisal.getOID();
	}

	public static long updateExc(EmpAppraisal empappraisal) throws DBException{ 
		try{ 
			if(empappraisal.getOID() != 0){ 
				PstEmpAppraisal pstEmpAppraisal = new PstEmpAppraisal(empappraisal.getOID());

				pstEmpAppraisal.setLong(FLD_EMPLOYEE_ID, empappraisal.getEmployeeId());
				pstEmpAppraisal.setLong(FLD_APPRAISOR_ID, empappraisal.getAppraisorId());
				pstEmpAppraisal.setDate(FLD_DATE_OF_APPRAISAL, empappraisal.getDateOfAppraisal());
				pstEmpAppraisal.setDate(FLD_LAST_APPRAISAL, empappraisal.getLastAppraisal());
				pstEmpAppraisal.setInt(FLD_TOTAL_SCORE, empappraisal.getTotalScore());
				pstEmpAppraisal.setInt(FLD_TOTAL_CRITERIA, empappraisal.getTotalCriteria());
				pstEmpAppraisal.setDouble(FLD_SCORE_AVERAGE, empappraisal.getScoreAverage());
				pstEmpAppraisal.setDate(FLD_DATE_PERFORMANCE, empappraisal.getDatePerformance());
				pstEmpAppraisal.setDate(FLD_TIME_PERFORMANCE, empappraisal.getTimePerformance());

				pstEmpAppraisal.update(); 
				return empappraisal.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpAppraisal(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstEmpAppraisal pstEmpAppraisal = new PstEmpAppraisal(oid);
			pstEmpAppraisal.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpAppraisal(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE_APPRAISAL; 
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
				EmpAppraisal empappraisal = new EmpAppraisal();
				resultToObject(rs, empappraisal);
				lists.add(empappraisal);
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

	private static void resultToObject(ResultSet rs, EmpAppraisal empappraisal){
		try{
			empappraisal.setOID(rs.getLong(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_EMPLOYEE_APPRAISAL_ID]));
			empappraisal.setEmployeeId(rs.getLong(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_EMPLOYEE_ID]));
			empappraisal.setAppraisorId(rs.getLong(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_APPRAISOR_ID]));
			empappraisal.setDateOfAppraisal(rs.getDate(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_DATE_OF_APPRAISAL]));
			empappraisal.setLastAppraisal(rs.getDate(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_LAST_APPRAISAL]));
			empappraisal.setTotalScore(rs.getInt(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_TOTAL_SCORE]));
			empappraisal.setTotalCriteria(rs.getInt(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_TOTAL_CRITERIA]));
			empappraisal.setScoreAverage(rs.getDouble(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_SCORE_AVERAGE]));
			empappraisal.setDatePerformance(rs.getDate(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_DATE_PERFORMANCE]));
			empappraisal.setTimePerformance(rs.getDate(PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_TIME_PERFORMANCE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long employeeAppraisalId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE_APPRAISAL + " WHERE " + 
						PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_EMPLOYEE_APPRAISAL_ID] + " = " + employeeAppraisalId;

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
			String sql = "SELECT COUNT("+ PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_EMPLOYEE_APPRAISAL_ID] + ") FROM " + TBL_HR_EMPLOYEE_APPRAISAL;
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
			  	   EmpAppraisal empappraisal = (EmpAppraisal)list.get(ls);
				   if(oid == empappraisal.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}



    public static void updateScore(long empAppraisalOID){
    	try{
        	Vector evaluate = PstPerformanceEvaluation.list(0,0,PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL]+" = "+empAppraisalOID,"");
            int totScore = PstPerformanceEvaluation.countScore(empAppraisalOID);
            double average = (new Integer(totScore)).doubleValue()/(new Integer(evaluate.size())).doubleValue();

        	String sql = " UPDATE "+PstEmpAppraisal.TBL_HR_EMPLOYEE_APPRAISAL+
                		 " SET "+PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_TOTAL_SCORE]+" = "+totScore+
                         ", "+PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_TOTAL_CRITERIA]+" = "+evaluate.size()+
                         ", "+PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_SCORE_AVERAGE]+" = "+average+
                         " WHERE "+PstEmpAppraisal.fieldNames[PstEmpAppraisal.FLD_EMPLOYEE_APPRAISAL_ID]+" = "+empAppraisalOID;

        	//System.out.println("sql "+sql);
        	
                int a = DBHandler.execUpdate(sql);

    	}catch(Exception exc){
        	System.out.println("exc....update score "+exc.toString());
    	}
    }

}
