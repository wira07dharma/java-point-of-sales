
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
import com.dimata.harisma.entity.masterdata.*;

public class PstPerformanceEvaluation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_PERFORMANCE_EVALUATION = "HR_PERFORMANCE_EVALUATION";

	public static final  int FLD_PERFORMANCE_APPRAISAL_ID = 0;
	public static final  int FLD_EMPLOYEE_APPRAISAL = 1;
	public static final  int FLD_EVALUATION_ID = 2;
	public static final  int FLD_CATEGORY_CRITERIA_ID = 3;
	public static final  int FLD_JUSTIFICATION = 4;

	public static final  String[] fieldNames = {
		"PERFORMANCE_APPRAISAL_ID",
		"EMPLOYEE_APPRAISAL",
		"EVALUATION_ID",
		"CATEGORY_CRITERIA_ID",
		"JUSTIFICATION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstPerformanceEvaluation(){
	}

	public PstPerformanceEvaluation(int i) throws DBException { 
		super(new PstPerformanceEvaluation()); 
	}

	public PstPerformanceEvaluation(String sOid) throws DBException { 
		super(new PstPerformanceEvaluation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstPerformanceEvaluation(long lOid) throws DBException { 
		super(new PstPerformanceEvaluation(0)); 
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
		return TBL_HR_PERFORMANCE_EVALUATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstPerformanceEvaluation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		PerformanceEvaluation performanceevaluation = fetchExc(ent.getOID()); 
		ent = (Entity)performanceevaluation; 
		return performanceevaluation.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((PerformanceEvaluation) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((PerformanceEvaluation) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static PerformanceEvaluation fetchExc(long oid) throws DBException{ 
		try{ 
			PerformanceEvaluation performanceevaluation = new PerformanceEvaluation();
			PstPerformanceEvaluation pstPerformanceEvaluation = new PstPerformanceEvaluation(oid); 
			performanceevaluation.setOID(oid);

			performanceevaluation.setEmployeeAppraisal(pstPerformanceEvaluation.getlong(FLD_EMPLOYEE_APPRAISAL));
			performanceevaluation.setEvaluationId(pstPerformanceEvaluation.getlong(FLD_EVALUATION_ID));
			performanceevaluation.setCategoryCriteriaId(pstPerformanceEvaluation.getlong(FLD_CATEGORY_CRITERIA_ID));
			performanceevaluation.setJustification(pstPerformanceEvaluation.getString(FLD_JUSTIFICATION));

			return performanceevaluation; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPerformanceEvaluation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(PerformanceEvaluation performanceevaluation) throws DBException{ 
		try{ 
			PstPerformanceEvaluation pstPerformanceEvaluation = new PstPerformanceEvaluation(0);

			pstPerformanceEvaluation.setLong(FLD_EMPLOYEE_APPRAISAL, performanceevaluation.getEmployeeAppraisal());
			pstPerformanceEvaluation.setLong(FLD_EVALUATION_ID, performanceevaluation.getEvaluationId());
			pstPerformanceEvaluation.setLong(FLD_CATEGORY_CRITERIA_ID, performanceevaluation.getCategoryCriteriaId());
			pstPerformanceEvaluation.setString(FLD_JUSTIFICATION, performanceevaluation.getJustification());

			pstPerformanceEvaluation.insert(); 
			performanceevaluation.setOID(pstPerformanceEvaluation.getlong(FLD_PERFORMANCE_APPRAISAL_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPerformanceEvaluation(0),DBException.UNKNOWN); 
		}
		return performanceevaluation.getOID();
	}

	public static long updateExc(PerformanceEvaluation performanceevaluation) throws DBException{ 
		try{ 
			if(performanceevaluation.getOID() != 0){ 
				PstPerformanceEvaluation pstPerformanceEvaluation = new PstPerformanceEvaluation(performanceevaluation.getOID());

				pstPerformanceEvaluation.setLong(FLD_EMPLOYEE_APPRAISAL, performanceevaluation.getEmployeeAppraisal());
				pstPerformanceEvaluation.setLong(FLD_EVALUATION_ID, performanceevaluation.getEvaluationId());
				pstPerformanceEvaluation.setLong(FLD_CATEGORY_CRITERIA_ID, performanceevaluation.getCategoryCriteriaId());
				pstPerformanceEvaluation.setString(FLD_JUSTIFICATION, performanceevaluation.getJustification());

				pstPerformanceEvaluation.update(); 
				return performanceevaluation.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPerformanceEvaluation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstPerformanceEvaluation pstPerformanceEvaluation = new PstPerformanceEvaluation(oid);
			pstPerformanceEvaluation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPerformanceEvaluation(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_PERFORMANCE_EVALUATION; 
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
				PerformanceEvaluation performanceevaluation = new PerformanceEvaluation();
				resultToObject(rs, performanceevaluation);
				lists.add(performanceevaluation);
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


    public static Vector listPerfEvaluation(long empAppOID){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = " SELECT PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_PERFORMANCE_APPRAISAL_ID]+
               			 " , PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL]+
                		 " , PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_CATEGORY_CRITERIA_ID]+
                         " , PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID]+
                         " , CC."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CRITERIA]+
                         " , PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_JUSTIFICATION]+
                         " , CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]+
                         " , CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]+
                         " , EVA."+PstEvaluation.fieldNames[PstEvaluation.FLD_CODE]+
                         " FROM " + TBL_HR_PERFORMANCE_EVALUATION + " PER "+                              
               			 " , "+PstCategoryCriteria.TBL_HR_CATEGORY_CRITERIA + " CC "+
                         " , "+PstCategoryAppraisal.TBL_HR_CATEGORY_APPRAISAL + " CA "+
                         " , "+PstEvaluation.TBL_HR_EVALUATION + " EVA "+
                         " WHERE PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_CATEGORY_CRITERIA_ID]+
                         " = CC."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID]+
                         " AND CC."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_APPRAISAL_ID]+
                         " = CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]+
                         " AND PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID]+
                         " = EVA."+PstEvaluation.fieldNames[PstEvaluation.FLD_EVALUATION_ID]+
                         " AND PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL]+
                         " = "+empAppOID +
                         " ORDER BY CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID];


			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
            long criteriaId = 0 ;
            int num = 0;
			while(rs.next()) {
                Vector temp = new Vector(1,1);
				PerformanceEvaluation performanceevaluation = new PerformanceEvaluation();
                CategoryAppraisal categAppraisal = new CategoryAppraisal();
                CategoryCriteria categCriteria = new CategoryCriteria();
                Evaluation evaluation = new Evaluation();
                Vector numbers = new Vector(1,1);

				resultToObject(rs, performanceevaluation);
                temp.add(performanceevaluation);

                categAppraisal.setOID(rs.getLong(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]));
                categAppraisal.setCategory(rs.getString(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]));
                temp.add(categAppraisal);

                categCriteria.setCriteria(rs.getString(PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CRITERIA]));
                temp.add(categCriteria);

                evaluation.setCode(rs.getString(PstEvaluation.fieldNames[PstEvaluation.FLD_CODE]));
                temp.add(evaluation);

                long capp = rs.getLong(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]);
                if(capp!= criteriaId){
                    num = 0;
                    criteriaId = capp;
                }

                numbers.add(""+num);
                temp.add(numbers);

                lists.add(temp);

                num++;
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


	private static void resultToObject(ResultSet rs, PerformanceEvaluation performanceevaluation){
		try{
			performanceevaluation.setOID(rs.getLong(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_PERFORMANCE_APPRAISAL_ID]));
			performanceevaluation.setEmployeeAppraisal(rs.getLong(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL]));
			performanceevaluation.setEvaluationId(rs.getLong(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID]));
			performanceevaluation.setCategoryCriteriaId(rs.getLong(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_CATEGORY_CRITERIA_ID]));
			performanceevaluation.setJustification(rs.getString(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_JUSTIFICATION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long performanceAppraisalId ){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PERFORMANCE_EVALUATION + " WHERE " + 
						PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_PERFORMANCE_APPRAISAL_ID] + " = " + performanceAppraisalId;

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
			String sql = "SELECT COUNT("+ PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_PERFORMANCE_APPRAISAL_ID] + ") FROM " + TBL_HR_PERFORMANCE_EVALUATION;
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
			  	   PerformanceEvaluation performanceevaluation = (PerformanceEvaluation)list.get(ls);
				   if(oid == performanceevaluation.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}



    public static int countScore(long oidEmpAppraisal)
    {
        DBResultSet dbrs = null;
    	try{
        	String sql = " SELECT EVA."+PstEvaluation.fieldNames[PstEvaluation.FLD_CODE]+
                         " FROM "+PstEvaluation.TBL_HR_EVALUATION+" EVA "+
                         " , "+PstPerformanceEvaluation.TBL_HR_PERFORMANCE_EVALUATION+ " PER "+
                         " WHERE PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID]+
                         " = EVA."+PstEvaluation.fieldNames[PstEvaluation.FLD_EVALUATION_ID]+
                         " AND PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL]+
                         " = "+oidEmpAppraisal ;

        	dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) {
                String str = rs.getString(PstEvaluation.fieldNames[PstEvaluation.FLD_CODE]);
                if((str.trim()).length()>1)
                    str = str.substring(0,1);

                count = count + Integer.parseInt(str);
            }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}


    public static Vector listCriteria(long oidCategApp, long empAppOID)
    {
    	DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
    	try{
        	String sql = " SELECT CRI."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID]+
                		 ", CRI."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CRITERIA]+
                         ", PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_PERFORMANCE_APPRAISAL_ID]+
                         ", PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID]+
                         ", PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_JUSTIFICATION]+
                         " FROM "+PstCategoryCriteria.TBL_HR_CATEGORY_CRITERIA + " CRI "+
                         " LEFT JOIN "+PstPerformanceEvaluation.TBL_HR_PERFORMANCE_EVALUATION+ " PER "+
                         " ON PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_CATEGORY_CRITERIA_ID]+
                         " = CRI."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID]+
                         " AND PER."+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL]+
                         " = "+empAppOID+
                         " WHERE "+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_APPRAISAL_ID]+
                         " = "+oidCategApp ;

            System.out.println("sql === "+sql);
        	dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {
                Vector temp = new Vector(1,1);
                CategoryCriteria categCriteria = new CategoryCriteria();
                PerformanceEvaluation performanceEvaluation = new PerformanceEvaluation();

                categCriteria.setOID(rs.getLong(PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID]));
                categCriteria.setCriteria(rs.getString(PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CRITERIA]));
                performanceEvaluation.setOID(rs.getLong(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_PERFORMANCE_APPRAISAL_ID]));
                performanceEvaluation.setEvaluationId(rs.getLong(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID]));
                performanceEvaluation.setJustification(rs.getString(PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_JUSTIFICATION]));

                temp.add(categCriteria);
                temp.add(performanceEvaluation);

                result.add(temp);
            }

			rs.close();
			return result;
		}catch(Exception e) {
			return new Vector(1,1);
		}finally {
			DBResultSet.close(dbrs);
		}
	}


    public static long deleteByAppraisal(long emplAppOID)
    {
    	try{
            String sql = " DELETE FROM "+PstPerformanceEvaluation.TBL_HR_PERFORMANCE_EVALUATION +
                		 " WHERE "+PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EMPLOYEE_APPRAISAL] +
                         " = "+emplAppOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete evaluation by appraisal "+exc.toString());
    	}

    	return emplAppOID;
    }


    public static boolean checkCategoryCriteria(long categoryAppraisalId ){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PERFORMANCE_EVALUATION + " WHERE " + 
						PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_CATEGORY_CRITERIA_ID] + " = " + categoryAppraisalId;

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

    public static boolean checkEvaluation(long evaluationId ){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PERFORMANCE_EVALUATION + " WHERE " + 
						PstPerformanceEvaluation.fieldNames[PstPerformanceEvaluation.FLD_EVALUATION_ID] + " = " + evaluationId;

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
