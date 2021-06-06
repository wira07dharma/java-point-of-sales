
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

package com.dimata.harisma.entity.masterdata; 

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

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
import com.dimata.harisma.entity.masterdata.*; 

public class PstTrainingDept extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_TRAINING_DEPT = "HR_TRAINING_DEPT";

	public static final  int FLD_TRAINING_DEPT_ID = 0;
	public static final  int FLD_DEPARTMENT_ID = 1;
	public static final  int FLD_TRAINING_ID = 2;

	public static final  String[] fieldNames = {
		"TRAINING_DEPT_ID",
		"DEPARTMENT_ID",
		"TRAINING_ID"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG
	 }; 

	public PstTrainingDept(){
	}

	public PstTrainingDept(int i) throws DBException { 
		super(new PstTrainingDept()); 
	}

	public PstTrainingDept(String sOid) throws DBException { 
		super(new PstTrainingDept(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstTrainingDept(long lOid) throws DBException { 
		super(new PstTrainingDept(0)); 
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
		return TBL_HR_TRAINING_DEPT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstTrainingDept().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		TrainingDept trainingdept = fetchExc(ent.getOID()); 
		ent = (Entity)trainingdept; 
		return trainingdept.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((TrainingDept) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((TrainingDept) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static TrainingDept fetchExc(long oid) throws DBException{ 
		try{ 
			TrainingDept trainingdept = new TrainingDept();
			PstTrainingDept pstTrainingDept = new PstTrainingDept(oid); 
			trainingdept.setOID(oid);

			trainingdept.setDepartmentId(pstTrainingDept.getlong(FLD_DEPARTMENT_ID));
			trainingdept.setTrainingId(pstTrainingDept.getlong(FLD_TRAINING_ID));

			return trainingdept; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingDept(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(TrainingDept trainingdept) throws DBException{ 
		try{ 
			PstTrainingDept pstTrainingDept = new PstTrainingDept(0);

			pstTrainingDept.setLong(FLD_DEPARTMENT_ID, trainingdept.getDepartmentId());
			pstTrainingDept.setLong(FLD_TRAINING_ID, trainingdept.getTrainingId());

			pstTrainingDept.insert(); 
			trainingdept.setOID(pstTrainingDept.getlong(FLD_TRAINING_DEPT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingDept(0),DBException.UNKNOWN); 
		}
		return trainingdept.getOID();
	}

	public static long updateExc(TrainingDept trainingdept) throws DBException{ 
		try{ 
			if(trainingdept.getOID() != 0){ 
				PstTrainingDept pstTrainingDept = new PstTrainingDept(trainingdept.getOID());

				pstTrainingDept.setLong(FLD_DEPARTMENT_ID, trainingdept.getDepartmentId());
				pstTrainingDept.setLong(FLD_TRAINING_ID, trainingdept.getTrainingId());

				pstTrainingDept.update(); 
				return trainingdept.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingDept(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstTrainingDept pstTrainingDept = new PstTrainingDept(oid);
			pstTrainingDept.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstTrainingDept(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_DEPT; 
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
				TrainingDept trainingdept = new TrainingDept();
				resultToObject(rs, trainingdept);
				lists.add(trainingdept);
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

	private static void resultToObject(ResultSet rs, TrainingDept trainingdept){
		try{
			trainingdept.setOID(rs.getLong(PstTrainingDept.fieldNames[PstTrainingDept.FLD_TRAINING_DEPT_ID]));
			trainingdept.setDepartmentId(rs.getLong(PstTrainingDept.fieldNames[PstTrainingDept.FLD_DEPARTMENT_ID]));
			trainingdept.setTrainingId(rs.getLong(PstTrainingDept.fieldNames[PstTrainingDept.FLD_TRAINING_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long trainingDeptId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_TRAINING_DEPT + " WHERE " + 
						PstTrainingDept.fieldNames[PstTrainingDept.FLD_TRAINING_DEPT_ID] + " = " + trainingDeptId;

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
			String sql = "SELECT COUNT("+ PstTrainingDept.fieldNames[PstTrainingDept.FLD_TRAINING_DEPT_ID] + ") FROM " + TBL_HR_TRAINING_DEPT;
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
			  	   TrainingDept trainingdept = (TrainingDept)list.get(ls);
				   if(oid == trainingdept.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    public static void deleteAllByTraining(long trainingOID){
        DBResultSet dbrs = null;
        try{
            String sql = "DELETE FROM "+PstTrainingDept.TBL_HR_TRAINING_DEPT+
                " WHERE "+PstTrainingDept.fieldNames[PstTrainingDept.FLD_TRAINING_ID]+
                " = "+trainingOID;

            int status = DBHandler.execUpdate(sql);

        }
        catch(Exception e){
			System.out.println("Exception e : "+e.toString());
        }
        finally{
            DBResultSet.close(dbrs);
        }
    }

    public static Vector getTrainingByDepartment(long depOID){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try{
            String sql = "SELECT DISTINCT HT.* FROM "+PstTrainingDept.TBL_HR_TRAINING_DEPT+" TD "+
					" INNER JOIN "+PstTraining.TBL_HR_TRAINING+" HT ON HT."+PstTraining.fieldNames[PstTraining.FLD_TRAINING_ID]+
                    " = TD."+PstTrainingDept.fieldNames[PstTrainingDept.FLD_TRAINING_ID]+
					" WHERE TD."+PstTrainingDept.fieldNames[PstTrainingDept.FLD_DEPARTMENT_ID]+" = "+depOID;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()){
                 Training tr = new Training();
                 PstTraining.resultToObject(rs, tr);

                 result.add(tr);
            }

        }
        catch(Exception e){
			System.out.println("Exception e : "+e.toString());
        }
        finally{
            DBResultSet.close(dbrs);
        }

        return result;
    }

}
