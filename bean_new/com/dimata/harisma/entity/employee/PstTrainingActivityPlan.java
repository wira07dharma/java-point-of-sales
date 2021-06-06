
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

public class PstTrainingActivityPlan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_TRAINING_ACTIVITY_PLAN = "HR_TRAINING_ACTIVITY_PLAN";

	public static final  int FLD_TRAINING_ACTIVITY_PLAN_ID = 0;
	public static final  int FLD_DEPARTMENT_ID = 1;
	public static final  int FLD_DATE = 2;
	public static final  int FLD_PROGRAM = 3;
	public static final  int FLD_TRAINER = 4;
	public static final  int FLD_PROGRAMS_PLAN = 5;
	public static final  int FLD_TOT_HOURS_PLAN = 6;
	public static final  int FLD_TRAINEES_PLAN = 7;
	public static final  int FLD_REMARK = 8;
    public static final  int FLD_TRAINING_ID = 9;

	public static final  String[] fieldNames = {
		"TRAINING_ACTIVITY_PLAN_ID",
		"DEPARTMENT_ID",
		"DATE",
		"PROGRAM",
		"TRAINER",
		"PROGRAMS_PLAN",
		"TOT_HOURS_PLAN",
		"TRAINEES_PLAN",
		"REMARK",
        "TRAINING_ID"
	 }; 

	public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG
    };

	public PstTrainingActivityPlan(){
	}

	public PstTrainingActivityPlan(int i) throws DBException { 
		super(new PstTrainingActivityPlan()); 
	}

	public PstTrainingActivityPlan(String sOid) throws DBException { 
		super(new PstTrainingActivityPlan(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstTrainingActivityPlan(long lOid) throws DBException { 
		super(new PstTrainingActivityPlan(0)); 
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
		return TBL_HR_TRAINING_ACTIVITY_PLAN;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstTrainingActivityPlan().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		TrainingActivityPlan trainingactivityplan = fetchExc(ent.getOID()); 
		ent = (Entity)trainingactivityplan; 
		return trainingactivityplan.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((TrainingActivityPlan) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((TrainingActivityPlan) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static TrainingActivityPlan fetchExc(long oid) throws DBException{ 
		try{ 
			TrainingActivityPlan trainingactivityplan = new TrainingActivityPlan();
			PstTrainingActivityPlan pstTrainingActivityPlan = new PstTrainingActivityPlan(oid); 
			trainingactivityplan.setOID(oid);

			trainingactivityplan.setDepartmentId(pstTrainingActivityPlan.getlong(FLD_DEPARTMENT_ID));
			trainingactivityplan.setDate(pstTrainingActivityPlan.getDate(FLD_DATE));
			trainingactivityplan.setProgram(pstTrainingActivityPlan.getString(FLD_PROGRAM));
			trainingactivityplan.setTrainer(pstTrainingActivityPlan.getString(FLD_TRAINER));
			trainingactivityplan.setProgramsPlan(pstTrainingActivityPlan.getInt(FLD_PROGRAMS_PLAN));
			trainingactivityplan.setTotHoursPlan(pstTrainingActivityPlan.getInt(FLD_TOT_HOURS_PLAN));
			trainingactivityplan.setTraineesPlan(pstTrainingActivityPlan.getInt(FLD_TRAINEES_PLAN));
			trainingactivityplan.setRemark(pstTrainingActivityPlan.getString(FLD_REMARK));
			trainingactivityplan.setTrainingId(pstTrainingActivityPlan.getlong(FLD_TRAINING_ID));

			return trainingactivityplan; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityPlan(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(TrainingActivityPlan trainingactivityplan) throws DBException{ 
		try{ 
			PstTrainingActivityPlan pstTrainingActivityPlan = new PstTrainingActivityPlan(0);

			pstTrainingActivityPlan.setLong(FLD_DEPARTMENT_ID, trainingactivityplan.getDepartmentId());
			pstTrainingActivityPlan.setDate(FLD_DATE, trainingactivityplan.getDate());
			pstTrainingActivityPlan.setString(FLD_PROGRAM, trainingactivityplan.getProgram());
			pstTrainingActivityPlan.setString(FLD_TRAINER, trainingactivityplan.getTrainer());
			pstTrainingActivityPlan.setInt(FLD_PROGRAMS_PLAN, trainingactivityplan.getProgramsPlan());
			pstTrainingActivityPlan.setInt(FLD_TOT_HOURS_PLAN, trainingactivityplan.getTotHoursPlan());
			pstTrainingActivityPlan.setInt(FLD_TRAINEES_PLAN, trainingactivityplan.getTraineesPlan());
			pstTrainingActivityPlan.setString(FLD_REMARK, trainingactivityplan.getRemark());
            pstTrainingActivityPlan.setLong(FLD_TRAINING_ID, trainingactivityplan.getTrainingId());

			pstTrainingActivityPlan.insert(); 
			trainingactivityplan.setOID(pstTrainingActivityPlan.getlong(FLD_TRAINING_ACTIVITY_PLAN_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityPlan(0),DBException.UNKNOWN); 
		}
		return trainingactivityplan.getOID();
	}

	public static long updateExc(TrainingActivityPlan trainingactivityplan) throws DBException{ 
		try{ 
			if(trainingactivityplan.getOID() != 0){ 
				PstTrainingActivityPlan pstTrainingActivityPlan = new PstTrainingActivityPlan(trainingactivityplan.getOID());

				pstTrainingActivityPlan.setLong(FLD_DEPARTMENT_ID, trainingactivityplan.getDepartmentId());
				pstTrainingActivityPlan.setDate(FLD_DATE, trainingactivityplan.getDate());
				pstTrainingActivityPlan.setString(FLD_PROGRAM, trainingactivityplan.getProgram());
				pstTrainingActivityPlan.setString(FLD_TRAINER, trainingactivityplan.getTrainer());
				pstTrainingActivityPlan.setInt(FLD_PROGRAMS_PLAN, trainingactivityplan.getProgramsPlan());
				pstTrainingActivityPlan.setInt(FLD_TOT_HOURS_PLAN, trainingactivityplan.getTotHoursPlan());
				pstTrainingActivityPlan.setInt(FLD_TRAINEES_PLAN, trainingactivityplan.getTraineesPlan());
				pstTrainingActivityPlan.setString(FLD_REMARK, trainingactivityplan.getRemark());
	            pstTrainingActivityPlan.setLong(FLD_TRAINING_ID, trainingactivityplan.getTrainingId());
				pstTrainingActivityPlan.update(); 
				return trainingactivityplan.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityPlan(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstTrainingActivityPlan pstTrainingActivityPlan = new PstTrainingActivityPlan(oid);
			pstTrainingActivityPlan.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingActivityPlan(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_ACTIVITY_PLAN; 
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

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>"+sql);
            dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				TrainingActivityPlan trainingactivityplan = new TrainingActivityPlan();
				resultToObject(rs, trainingactivityplan);
				lists.add(trainingactivityplan);
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

	public static void resultToObject(ResultSet rs, TrainingActivityPlan trainingactivityplan){
		try{
			trainingactivityplan.setOID(rs.getLong(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ACTIVITY_PLAN_ID]));
			trainingactivityplan.setDepartmentId(rs.getLong(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_DEPARTMENT_ID]));
			trainingactivityplan.setDate(rs.getDate(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_DATE]));
			trainingactivityplan.setProgram(rs.getString(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_PROGRAM]));
			trainingactivityplan.setTrainer(rs.getString(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINER]));
			trainingactivityplan.setProgramsPlan(rs.getInt(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_PROGRAMS_PLAN]));
			trainingactivityplan.setTotHoursPlan(rs.getInt(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TOT_HOURS_PLAN]));
			trainingactivityplan.setTraineesPlan(rs.getInt(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINEES_PLAN]));
			trainingactivityplan.setRemark(rs.getString(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_REMARK]));
            trainingactivityplan.setTrainingId(rs.getLong(PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long trainingActivityPlanId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_ACTIVITY_PLAN + " WHERE " + 
						PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ACTIVITY_PLAN_ID] + " = " + trainingActivityPlanId;

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
			String sql = "SELECT COUNT("+ PstTrainingActivityPlan.fieldNames[PstTrainingActivityPlan.FLD_TRAINING_ACTIVITY_PLAN_ID] + ") FROM " + TBL_HR_TRAINING_ACTIVITY_PLAN;
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
			  	   TrainingActivityPlan trainingactivityplan = (TrainingActivityPlan)list.get(ls);
				   if(oid == trainingactivityplan.getOID())
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


    public static Vector getActual(long oid)
    {
    	DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
		try {
			String sql = "  SELECT COUNT(DISTINCT "+ PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_ACTUAL_ID] + ") AS TOT_PRG "+
                		 ", SUM("+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_ATENDEES]+") AS TOT_TRAINEES"+
                         ", (SUM(TIME_TO_SEC("+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_END_TIME]+")) - "+
                         "  SUM(TIME_TO_SEC("+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_START_TIME ]+")))/3600 AS TOT_HOURS " +
                		 "  FROM " + PstTrainingActivityActual.TBL_HR_TRAINING_ACTIVITY_ACTUAL+
                         "  WHERE "+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_PLAN_ID]+
                         " = "+oid +
                         " GROUP BY "+PstTrainingActivityActual.fieldNames[PstTrainingActivityActual.FLD_TRAINING_ACTIVITY_PLAN_ID];

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {
                result.add(""+rs.getInt(1));
                result.add(""+rs.getInt(2));
                double hours = rs.getDouble(3);
                long iHours = Math.round(hours);
                System.out.println("iHours)))))))))))))))))"+iHours);
                result.add(""+iHours);
                
            }

			rs.close();
			return result;
		}catch(Exception e) {
            System.out.println(e);
			return new Vector(1,1);
		}finally {
			DBResultSet.close(dbrs);
		}
    }
}
