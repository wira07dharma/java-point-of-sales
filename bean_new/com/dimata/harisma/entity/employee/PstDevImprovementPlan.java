
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
import com.dimata.harisma.entity.masterdata.*;

public class PstDevImprovementPlan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_DEV_IMPROVEMENT_PLAN = "HR_DEV_IMPROVEMENT_PLAN";

	public static final  int FLD_DEV_IMPROVEMENT_PLAN_ID = 0;
	public static final  int FLD_CATEGORY_APPRAISAL_ID = 1;
	public static final  int FLD_EMPLOYEE_APPRAISAL = 2;
	public static final  int FLD_IMPROV_PLAN = 3;
    public static final  int FLD_RECOMMEND = 4;

	public static final  String[] fieldNames = {
		"DEV_IMPROVEMENT_PLAN_ID",
		"CATEGORY_APPRAISAL_ID",
		"EMPLOYEE_APPRAISAL",
		"IMPROV_PLAN",
        "RECOMMEND"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING,
        TYPE_BOOL
	 }; 

	public PstDevImprovementPlan(){
	}

	public PstDevImprovementPlan(int i) throws DBException { 
		super(new PstDevImprovementPlan()); 
	}

	public PstDevImprovementPlan(String sOid) throws DBException { 
		super(new PstDevImprovementPlan(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDevImprovementPlan(long lOid) throws DBException { 
		super(new PstDevImprovementPlan(0)); 
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
		return TBL_HR_DEV_IMPROVEMENT_PLAN;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDevImprovementPlan().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DevImprovementPlan devimprovementplan = fetchExc(ent.getOID()); 
		ent = (Entity)devimprovementplan; 
		return devimprovementplan.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DevImprovementPlan) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DevImprovementPlan) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DevImprovementPlan fetchExc(long oid) throws DBException{ 
		try{ 
			DevImprovementPlan devimprovementplan = new DevImprovementPlan();
			PstDevImprovementPlan pstDevImprovementPlan = new PstDevImprovementPlan(oid); 
			devimprovementplan.setOID(oid);

			devimprovementplan.setCategoryAppraisalId(pstDevImprovementPlan.getlong(FLD_CATEGORY_APPRAISAL_ID));
			devimprovementplan.setEmployeeAppraisal(pstDevImprovementPlan.getlong(FLD_EMPLOYEE_APPRAISAL));
			devimprovementplan.setImprovPlan(pstDevImprovementPlan.getString(FLD_IMPROV_PLAN));
            devimprovementplan.setRecommend(pstDevImprovementPlan.getboolean(FLD_RECOMMEND));

			return devimprovementplan; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovementPlan(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DevImprovementPlan devimprovementplan) throws DBException{ 
		try{ 
			PstDevImprovementPlan pstDevImprovementPlan = new PstDevImprovementPlan(0);

			pstDevImprovementPlan.setLong(FLD_CATEGORY_APPRAISAL_ID, devimprovementplan.getCategoryAppraisalId());
			pstDevImprovementPlan.setLong(FLD_EMPLOYEE_APPRAISAL, devimprovementplan.getEmployeeAppraisal());
			pstDevImprovementPlan.setString(FLD_IMPROV_PLAN, devimprovementplan.getImprovPlan());
            pstDevImprovementPlan.setboolean(FLD_RECOMMEND, devimprovementplan.getRecommend());

			pstDevImprovementPlan.insert(); 
			devimprovementplan.setOID(pstDevImprovementPlan.getlong(FLD_DEV_IMPROVEMENT_PLAN_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovementPlan(0),DBException.UNKNOWN); 
		}
		return devimprovementplan.getOID();
	}

	public static long updateExc(DevImprovementPlan devimprovementplan) throws DBException{ 
		try{ 
			if(devimprovementplan.getOID() != 0){ 
				PstDevImprovementPlan pstDevImprovementPlan = new PstDevImprovementPlan(devimprovementplan.getOID());

				pstDevImprovementPlan.setLong(FLD_CATEGORY_APPRAISAL_ID, devimprovementplan.getCategoryAppraisalId());
				pstDevImprovementPlan.setLong(FLD_EMPLOYEE_APPRAISAL, devimprovementplan.getEmployeeAppraisal());
				pstDevImprovementPlan.setString(FLD_IMPROV_PLAN, devimprovementplan.getImprovPlan());
                pstDevImprovementPlan.setboolean(FLD_RECOMMEND, devimprovementplan.getRecommend());

				pstDevImprovementPlan.update(); 
				return devimprovementplan.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovementPlan(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDevImprovementPlan pstDevImprovementPlan = new PstDevImprovementPlan(oid);
			pstDevImprovementPlan.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovementPlan(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_DEV_IMPROVEMENT_PLAN; 
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
				DevImprovementPlan devimprovementplan = new DevImprovementPlan();
				resultToObject(rs, devimprovementplan);
				lists.add(devimprovementplan);
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

	private static void resultToObject(ResultSet rs, DevImprovementPlan devimprovementplan){
		try{
			devimprovementplan.setOID(rs.getLong(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_DEV_IMPROVEMENT_PLAN_ID]));
			devimprovementplan.setCategoryAppraisalId(rs.getLong(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_CATEGORY_APPRAISAL_ID]));
			devimprovementplan.setEmployeeAppraisal(rs.getLong(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_EMPLOYEE_APPRAISAL]));
			devimprovementplan.setImprovPlan(rs.getString(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_IMPROV_PLAN]));
            devimprovementplan.setRecommend(rs.getBoolean(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_RECOMMEND]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long devImprovementPlanId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DEV_IMPROVEMENT_PLAN + " WHERE " + 
						PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_DEV_IMPROVEMENT_PLAN_ID] + " = " + devImprovementPlanId;

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
			String sql = "SELECT COUNT("+ PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_DEV_IMPROVEMENT_PLAN_ID] + ") FROM " + TBL_HR_DEV_IMPROVEMENT_PLAN;
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
			  	   DevImprovementPlan devimprovementplan = (DevImprovementPlan)list.get(ls);
				   if(oid == devimprovementplan.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}



     public static Vector listCategImprovement(long grRankOID)
    {
    	DBResultSet dbrs = null;
		try {
			String sql = " SELECT CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]+
                		 " , CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]+
                         " FROM "+PstCategoryAppraisal.TBL_HR_CATEGORY_APPRAISAL+" CA "+
                         " LEFT JOIN "+PstCategoryCriteria.TBL_HR_CATEGORY_CRITERIA + " CC "+
                         " ON CC."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_APPRAISAL_ID]+
                         " = CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]+
                         " INNER JOIN "+PstGroupCategory.TBL_HR_GROUP_CATEGORY+ " GC "+
                         " ON GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]+
                         " = CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_GROUP_CATEGORY_ID]+
                         " WHERE GC."+ PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_RANK_ID]+
                         " = "+grRankOID+
                         " AND CC."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID] + " IS NULL";

            System.out.println("sql "+sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();


			Vector result = new Vector(1,1);
			while(rs.next()) {
                CategoryAppraisal categAppraisal = new  CategoryAppraisal();

                categAppraisal.setOID(rs.getLong(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]));
                categAppraisal.setCategory(rs.getString(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]));

                result.add(categAppraisal);
            }

			rs.close();
			return result;
		}catch(Exception e) {
			return new Vector(1,1);
		}finally {
			DBResultSet.close(dbrs);
		}
    }



    public static Vector listDevImprovementPlan(long empAppraisalOID)
    {
    	DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
		try {
			String sql = " SELECT DIP."+PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_DEV_IMPROVEMENT_PLAN_ID]+
                		 " ,DIP."+PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_RECOMMEND]+
                		 " ,DIP."+PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_IMPROV_PLAN]+
                         " ,CC."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]+
                         " ,CC."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]+
                         " FROM "+PstDevImprovementPlan.TBL_HR_DEV_IMPROVEMENT_PLAN+ " DIP "+
                         " , "+PstCategoryAppraisal.TBL_HR_CATEGORY_APPRAISAL+" CC "+
                         " WHERE CC."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]+
                         " = DIP."+PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_CATEGORY_APPRAISAL_ID]+
                         " AND DIP."+PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_EMPLOYEE_APPRAISAL]+
                         " = "+empAppraisalOID+
                         " ORDER BY CC."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID];

            System.out.println("sql "+sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

            long groupOID = 0;
            int num = 0;
			while(rs.next()) {
                Vector temp = new Vector(1,1);
                DevImprovementPlan devImprovPlan = new DevImprovementPlan();
                CategoryAppraisal categApp = new  CategoryAppraisal();
				Vector numbers = new Vector(1,1);

                long tempGrpOID = rs.getLong(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]);


                if(groupOID!= tempGrpOID){
                    num = 0;
                    groupOID = tempGrpOID;
                }


                numbers.add(""+num);
                temp.add(numbers);

                categApp.setCategory(rs.getString(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]));
                temp.add(categApp);

                devImprovPlan.setOID(rs.getLong(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_DEV_IMPROVEMENT_PLAN_ID]));
                devImprovPlan.setRecommend(rs.getBoolean(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_RECOMMEND]));
                devImprovPlan.setImprovPlan(rs.getString(PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_IMPROV_PLAN]));
                temp.add(devImprovPlan);

                result.add(temp);

                num++;
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
            String sql = " DELETE FROM "+PstDevImprovementPlan.TBL_HR_DEV_IMPROVEMENT_PLAN +
                		 " WHERE "+PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_DEV_IMPROVEMENT_PLAN_ID] +
                         " = "+emplAppOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete evaluation by appraisal "+exc.toString());
    	}

    	return emplAppOID;
    }

	public static boolean checkCategoryAppraisal(long categoryAppraisalId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DEV_IMPROVEMENT_PLAN + " WHERE " + 
						PstDevImprovementPlan.fieldNames[PstDevImprovementPlan.FLD_CATEGORY_APPRAISAL_ID] + " = " + categoryAppraisalId;

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
