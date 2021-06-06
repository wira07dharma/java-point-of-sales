
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

public class PstDevImprovement extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_DEV_IMPROVEMENT = "HR_DEV_IMPROVEMENT";

	public static final  int FLD_DEV_IMPROVEMENT_ID = 0;
	public static final  int FLD_EMPLOYEE_APPRAISAL = 1;
	public static final  int FLD_GROUP_CATEGORY_ID = 2;
	public static final  int FLD_IMPROVEMENT = 3;

	public static final  String[] fieldNames = {
		"DEV_IMPROVEMENT_ID",
		"EMPLOYEE_APPRAISAL",
		"GROUP_CATEGORY_ID",
		"IMPROVEMENT"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstDevImprovement(){
	}

	public PstDevImprovement(int i) throws DBException { 
		super(new PstDevImprovement()); 
	}

	public PstDevImprovement(String sOid) throws DBException { 
		super(new PstDevImprovement(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDevImprovement(long lOid) throws DBException { 
		super(new PstDevImprovement(0)); 
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
		return TBL_HR_DEV_IMPROVEMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDevImprovement().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DevImprovement devimprovement = fetchExc(ent.getOID()); 
		ent = (Entity)devimprovement; 
		return devimprovement.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DevImprovement) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DevImprovement) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DevImprovement fetchExc(long oid) throws DBException{ 
		try{ 
			DevImprovement devimprovement = new DevImprovement();
			PstDevImprovement pstDevImprovement = new PstDevImprovement(oid); 
			devimprovement.setOID(oid);

			devimprovement.setEmployeeAppraisal(pstDevImprovement.getlong(FLD_EMPLOYEE_APPRAISAL));
			devimprovement.setGroupCategoryId(pstDevImprovement.getlong(FLD_GROUP_CATEGORY_ID));
			devimprovement.setImprovement(pstDevImprovement.getString(FLD_IMPROVEMENT));

			return devimprovement; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovement(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DevImprovement devimprovement) throws DBException{ 
		try{ 
			PstDevImprovement pstDevImprovement = new PstDevImprovement(0);

			pstDevImprovement.setLong(FLD_EMPLOYEE_APPRAISAL, devimprovement.getEmployeeAppraisal());
			pstDevImprovement.setLong(FLD_GROUP_CATEGORY_ID, devimprovement.getGroupCategoryId());
			pstDevImprovement.setString(FLD_IMPROVEMENT, devimprovement.getImprovement());

			pstDevImprovement.insert(); 
			devimprovement.setOID(pstDevImprovement.getlong(FLD_DEV_IMPROVEMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovement(0),DBException.UNKNOWN); 
		}
		return devimprovement.getOID();
	}

	public static long updateExc(DevImprovement devimprovement) throws DBException{ 
		try{ 
			if(devimprovement.getOID() != 0){ 
				PstDevImprovement pstDevImprovement = new PstDevImprovement(devimprovement.getOID());

				pstDevImprovement.setLong(FLD_EMPLOYEE_APPRAISAL, devimprovement.getEmployeeAppraisal());
				pstDevImprovement.setLong(FLD_GROUP_CATEGORY_ID, devimprovement.getGroupCategoryId());
				pstDevImprovement.setString(FLD_IMPROVEMENT, devimprovement.getImprovement());

				pstDevImprovement.update(); 
				return devimprovement.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovement(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDevImprovement pstDevImprovement = new PstDevImprovement(oid);
			pstDevImprovement.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDevImprovement(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_DEV_IMPROVEMENT; 
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
				DevImprovement devimprovement = new DevImprovement();
				resultToObject(rs, devimprovement);
				lists.add(devimprovement);
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

	private static void resultToObject(ResultSet rs, DevImprovement devimprovement){
		try{
			devimprovement.setOID(rs.getLong(PstDevImprovement.fieldNames[PstDevImprovement.FLD_DEV_IMPROVEMENT_ID]));
			devimprovement.setEmployeeAppraisal(rs.getLong(PstDevImprovement.fieldNames[PstDevImprovement.FLD_EMPLOYEE_APPRAISAL]));
			devimprovement.setGroupCategoryId(rs.getLong(PstDevImprovement.fieldNames[PstDevImprovement.FLD_GROUP_CATEGORY_ID]));
			devimprovement.setImprovement(rs.getString(PstDevImprovement.fieldNames[PstDevImprovement.FLD_IMPROVEMENT]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long devImprovementId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DEV_IMPROVEMENT + " WHERE " + 
						PstDevImprovement.fieldNames[PstDevImprovement.FLD_DEV_IMPROVEMENT_ID] + " = '" + devImprovementId + "'";

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
			String sql = "SELECT COUNT("+ PstDevImprovement.fieldNames[PstDevImprovement.FLD_DEV_IMPROVEMENT_ID] + ") FROM " + TBL_HR_DEV_IMPROVEMENT;
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
			  	   DevImprovement devimprovement = (DevImprovement)list.get(ls);
				   if(oid == devimprovement.getOID())
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


    public static Vector listCategImprovement(long grRankOID)
    {
    	DBResultSet dbrs = null;
		try {
			String sql = " SELECT GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]+
                		 " , GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_NAME]+
                         " FROM "+PstGroupCategory.TBL_HR_GROUP_CATEGORY+" GC "+
                         " LEFT JOIN "+PstCategoryAppraisal.TBL_HR_CATEGORY_APPRAISAL+ " CA "+
                         " ON CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_GROUP_CATEGORY_ID]+
                         " = GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]+
                         " WHERE GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_RANK_ID]+" = "+grRankOID +
                         " AND CA."+PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID] + " IS NULL";

            System.out.println("sql "+sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();


			Vector result = new Vector(1,1);
			while(rs.next()) {
                GroupCategory grCateg = new  GroupCategory();

                grCateg.setOID(rs.getLong(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]));
                grCateg.setGroupName(rs.getString(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_NAME]));

                result.add(grCateg);
            }

			rs.close();
			return result;
		}catch(Exception e) {
			return new Vector(1,1);
		}finally {
			DBResultSet.close(dbrs);
		}
    }



    public static Vector listDevImprovement(long empAppraisalOID)
    {
    	DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
		try {
			String sql = " SELECT DI."+PstDevImprovement.fieldNames[PstDevImprovement.FLD_DEV_IMPROVEMENT_ID]+
                		 " ,DI."+PstDevImprovement.fieldNames[PstDevImprovement.FLD_IMPROVEMENT]+
                         " ,GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]+
                         " ,GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_NAME]+
                         " FROM "+PstDevImprovement.TBL_HR_DEV_IMPROVEMENT+ " DI "+
                         " , "+PstGroupCategory.TBL_HR_GROUP_CATEGORY+" GC "+
                         " WHERE GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]+
                         " = DI."+PstDevImprovement.fieldNames[PstDevImprovement.FLD_GROUP_CATEGORY_ID]+
                         " AND DI."+PstDevImprovement.fieldNames[PstDevImprovement.FLD_EMPLOYEE_APPRAISAL]+
                         " =  "+empAppraisalOID+
                         " ORDER BY GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID];

            System.out.println("sql "+sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

            long groupOID = 0;
            int num = 0;
			while(rs.next()) {
                Vector temp = new Vector(1,1);
                DevImprovement devImprov = new DevImprovement();
                GroupCategory grpCateg = new  GroupCategory();
				Vector numbers = new Vector(1,1);

                long tempGrpOID = rs.getLong(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]);


                if(groupOID!= tempGrpOID){
                    num = 0;
                    groupOID = tempGrpOID;
                }


                numbers.add(""+num);
                temp.add(numbers);

                grpCateg.setGroupName(rs.getString(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_NAME]));
                temp.add(grpCateg);

                devImprov.setOID(rs.getLong(PstDevImprovement.fieldNames[PstDevImprovement.FLD_DEV_IMPROVEMENT_ID]));
                devImprov.setImprovement(rs.getString(PstDevImprovement.fieldNames[PstDevImprovement.FLD_IMPROVEMENT]));
                temp.add(devImprov);

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
            String sql = " DELETE FROM "+PstDevImprovement.TBL_HR_DEV_IMPROVEMENT +
                		 " WHERE "+PstDevImprovement.fieldNames[PstDevImprovement.FLD_DEV_IMPROVEMENT_ID] +
                         " = "+emplAppOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete improvemnt by appraisal "+exc.toString());
    	}

    	return emplAppOID;
    }


    public static boolean checkGroupCategory(long groupCategoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DEV_IMPROVEMENT + " WHERE " + 
						PstDevImprovement.fieldNames[PstDevImprovement.FLD_GROUP_CATEGORY_ID] + " = '" + groupCategoryId + "'";

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
