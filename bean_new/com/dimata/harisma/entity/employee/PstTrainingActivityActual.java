
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
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*; 

public class PstTrainingActivityActual extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_TRAINING_ACTIVITY_ACTUAL = "HR_TRAINING_ACTIVITY_ACTUAL";

	public static final  int FLD_TRAINING_ACTIVITY_ACTUAL_ID = 0;
	public static final  int FLD_TRAINING_ACTIVITY_PLAN_ID = 1;
	public static final  int FLD_DATE = 2;
	public static final  int FLD_START_TIME = 3;
	public static final  int FLD_END_TIME = 4;
	public static final  int FLD_ATENDEES = 5;
	public static final  int FLD_VENUE = 6;
	public static final  int FLD_REMARK = 7;

	public static final  String[] fieldNames = {
		"TRAINING_ACTIVITY_ACTUAL_ID",
		"TRAINING_ACTIVITY_PLAN_ID",
		"DATE",
		"START_TIME",
		"END_TIME",
		"ATENDEES",
		"VENUE",
		"REMARK"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstTrainingActivityActual(){
	}

	public PstTrainingActivityActual(int i) throws DBException { 
		super(new PstTrainingActivityActual()); 
	}

	public PstTrainingActivityActual(String sOid) throws DBException { 
		super(new PstTrainingActivityActual(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstTrainingActivityActual(long lOid) throws DBException { 
		super(new PstTrainingActivityActual(0)); 
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
		return TBL_HR_TRAINING_ACTIVITY_ACTUAL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstTrainingActivityActual().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		TrainingActivityActual trainingactivityactual = fetchExc(ent.getOID()); 
		ent = (Entity)trainingactivityactual; 
		return trainingactivityactual.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((TrainingActivityActual) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((TrainingActivityActual) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static TrainingActivityActual fetchExc(long oid) throws DBException{ 
		try{ 
			TrainingActivityActual trainingactivityactual = new TrainingActivityActual();
			PstTrainingActivityActual pstTrainingActivityActual = new PstTrainingActivityActual(oid); 
			trainingactivityactual.setOID(oid);

			trainingactivityactual.setTrainingActivityPlanId(pstTrainingActivityActual.getlong(FLD_TRAINING_ACTIVITY_PLAN_ID));
			trainingactivityactual.setDate(pstTrainingActivityActual.getDate(FLD_DATE));
			trainingactivityactual.setStartTime(pstTrainingActivityActual.getDate(FLD_START_TIME));
			trainingactivityactual.setEndTime(pstTrainingActivityActual.getDate(FLD_END_TIME));
			trainingactivityactual.setAtendees(pstTrainingActivityActual.getInt(FLD_ATENDEES));
			trainingactivityactual.setVenue(pstTrainingActivityActual.getString(FLD_VENUE));
			trainingactivityactual.setRemark(pstTrainingActivityActual.getString(FLD_REMARK));

			return trainingactivityactual; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityActual(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(TrainingActivityActual trainingactivityactual) throws DBException{ 
		try{ 
			PstTrainingActivityActual pstTrainingActivityActual = new PstTrainingActivityActual(0);

			pstTrainingActivityActual.setLong(FLD_TRAINING_ACTIVITY_PLAN_ID, trainingactivityactual.getTrainingActivityPlanId());
			pstTrainingActivityActual.setDate(FLD_DATE, trainingactivityactual.getDate());
			pstTrainingActivityActual.setDate(FLD_START_TIME, trainingactivityactual.getStartTime());
			pstTrainingActivityActual.setDate(FLD_END_TIME, trainingactivityactual.getEndTime());
			pstTrainingActivityActual.setInt(FLD_ATENDEES, trainingactivityactual.getAtendees());
			pstTrainingActivityActual.setString(FLD_VENUE, trainingactivityactual.getVenue());
			pstTrainingActivityActual.setString(FLD_REMARK, trainingactivityactual.getRemark());

			pstTrainingActivityActual.insert(); 
			trainingactivityactual.setOID(pstTrainingActivityActual.getlong(FLD_TRAINING_ACTIVITY_ACTUAL_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityActual(0),DBException.UNKNOWN); 
		}
		return trainingactivityactual.getOID();
	}

	public static long updateExc(TrainingActivityActual trainingactivityactual) throws DBException{ 
		try{ 
			if(trainingactivityactual.getOID() != 0){ 
				PstTrainingActivityActual pstTrainingActivityActual = new PstTrainingActivityActual(trainingactivityactual.getOID());

				pstTrainingActivityActual.setLong(FLD_TRAINING_ACTIVITY_PLAN_ID, trainingactivityactual.getTrainingActivityPlanId());
				pstTrainingActivityActual.setDate(FLD_DATE, trainingactivityactual.getDate());
				pstTrainingActivityActual.setDate(FLD_START_TIME, trainingactivityactual.getStartTime());
				pstTrainingActivityActual.setDate(FLD_END_TIME, trainingactivityactual.getEndTime());
				pstTrainingActivityActual.setInt(FLD_ATENDEES, trainingactivityactual.getAtendees());
				pstTrainingActivityActual.setString(FLD_VENUE, trainingactivityactual.getVenue());
				pstTrainingActivityActual.setString(FLD_REMARK, trainingactivityactual.getRemark());

				pstTrainingActivityActual.update(); 
				return trainingactivityactual.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityActual(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstTrainingActivityActual pstTrainingActivityActual = new PstTrainingActivityActual(oid);
			pstTrainingActivityActual.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityActual(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_ACTIVITY_ACTUAL; 
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
				TrainingActivityActual trainingactivityactual = new TrainingActivityActual();
				resultToObject(rs, trainingactivityactual);
				lists.add(trainingactivityactual);
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

	public static void resultToObject(ResultSet rs, TrainingActivityActual trainingactivityactual){
		try{
			trainingactivityactual.setOID(rs.getLong(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_ACTUAL_ID]));
			trainingactivityactual.setTrainingActivityPlanId(rs.getLong(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_PLAN_ID]));
			trainingactivityactual.setDate(rs.getDate(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE]));
			trainingactivityactual.setStartTime(rs.getTime(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_START_TIME]));
			trainingactivityactual.setEndTime(rs.getTime(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_END_TIME]));
			trainingactivityactual.setAtendees(rs.getInt(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_ATENDEES]));
			trainingactivityactual.setVenue(rs.getString(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_VENUE]));
			trainingactivityactual.setRemark(rs.getString(PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_REMARK]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long trainingActivityActualId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_ACTIVITY_ACTUAL + " WHERE " + 
						PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_ACTUAL_ID] + " = " + trainingActivityActualId;

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
			String sql = "SELECT COUNT("+ PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_ACTUAL_ID] + ") FROM " + TBL_HR_TRAINING_ACTIVITY_ACTUAL;
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
			  	   TrainingActivityActual trainingactivityactual = (TrainingActivityActual)list.get(ls);
				   if(oid == trainingactivityactual.getOID())
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



    public static Vector listActivity(Date date, int limitStart, int recordToGet)
    {
    	if(date == null)
            return new Vector(1,1);

        Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = " SELECT TAA.*,TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_PROGRAM]+
                		 " , TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINER]+
                         " , TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ID]+
                		 " , DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+
                		 " , DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]+
                		 " FROM " + TBL_HR_TRAINING_ACTIVITY_ACTUAL + " TAA "+
                		 " INNER JOIN "+PstTrainingActivityPlan.TBL_HR_TRAINING_ACTIVITY_PLAN + " TAP "+
                         " ON TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ACTIVITY_PLAN_ID]+
                         " = TAA."+fieldNames[FLD_TRAINING_ACTIVITY_PLAN_ID]+
                         " LEFT JOIN "+PstDepartment.TBL_HR_DEPARTMENT + " DEP "+
                         " ON DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+
                         " = TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_DEPARTMENT_ID]+
                         " WHERE MONTH(TAA."+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE]+")"+
                         " = "+(date.getMonth()+1)+
					  	 " AND "+
					  	 " YEAR(TAA."+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE]+")"+
                         " = "+(date.getYear()+1900)+
                         " ORDER BY TAA."+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE];

            System.out.println("DBHandler.DBSVR_TYPE ==== "+DBHandler.DBSVR_TYPE);

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

            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {
                Vector temp = new Vector(1,1);
				TrainingActivityActual trainingactivityactual = new TrainingActivityActual();
				TrainingActivityPlan trainingActivityPlan = new TrainingActivityPlan();
                Department department = new Department();

                resultToObject(rs, trainingactivityactual);
				temp.add(trainingactivityactual);

                trainingActivityPlan.setProgram(rs.getString(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_PROGRAM]));
                trainingActivityPlan.setTrainer(rs.getString(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINER]));
                trainingActivityPlan.setTrainingId(rs.getLong(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ID]));
				temp.add(trainingActivityPlan);

                long deptId = rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]);
                if(deptId == 0){
                    department.setDepartment("Generic Training");
                }else{
                	department.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                }
                temp.add(department);

                lists.add(temp);
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
    
	public static int getCount(Date date, int limitStart, int recordToGet){
		DBResultSet dbrs = null;
		try {
			//String sql = "SELECT COUNT("+ PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_ACTUAL_ID] + ") FROM " + TBL_HR_TRAINING_ACTIVITY_ACTUAL;
			String sql = " SELECT COUNT(TAA." + PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_ACTUAL_ID] + ") "+
                		 " FROM " + TBL_HR_TRAINING_ACTIVITY_ACTUAL + " TAA "+
                		 " INNER JOIN "+PstTrainingActivityPlan.TBL_HR_TRAINING_ACTIVITY_PLAN + " TAP "+
                         " ON TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ACTIVITY_PLAN_ID]+
                         " = TAA."+fieldNames[FLD_TRAINING_ACTIVITY_PLAN_ID]+
                         " LEFT JOIN "+PstDepartment.TBL_HR_DEPARTMENT + " DEP "+
                         " ON DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+
                         " = TAP."+PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_DEPARTMENT_ID]+
                         " WHERE MONTH(TAA."+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE]+")"+
                         " = "+(date.getMonth()+1)+
					  	 " AND "+
					  	 " YEAR(TAA."+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE]+")"+
                         " = "+(date.getYear()+1900)+
                         " ORDER BY TAA."+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_DATE];

            System.out.println("DBHandler.DBSVR_TYPE ==== "+DBHandler.DBSVR_TYPE);

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
}
