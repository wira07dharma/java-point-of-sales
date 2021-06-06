
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
import com.dimata.harisma.entity.employee.*;

public class PstCategoryCriteria extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_CATEGORY_CRITERIA = "HR_CATEGORY_CRITERIA";

	public static final  int FLD_CATEGORY_CRITERIA_ID = 0;
	public static final  int FLD_CATEGORY_APPRAISAL_ID = 1;
	public static final  int FLD_CRITERIA = 2;

	public static final  String[] fieldNames = {
		"CATEGORY_CRITERIA_ID",
		"CATEGORY_APPRAISAL_ID",
		"CRITERIA"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstCategoryCriteria(){
	}

	public PstCategoryCriteria(int i) throws DBException { 
		super(new PstCategoryCriteria()); 
	}

	public PstCategoryCriteria(String sOid) throws DBException { 
		super(new PstCategoryCriteria(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstCategoryCriteria(long lOid) throws DBException { 
		super(new PstCategoryCriteria(0)); 
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
		return TBL_HR_CATEGORY_CRITERIA;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstCategoryCriteria().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		CategoryCriteria categorycriteria = fetchExc(ent.getOID()); 
		ent = (Entity)categorycriteria; 
		return categorycriteria.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((CategoryCriteria) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((CategoryCriteria) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static CategoryCriteria fetchExc(long oid) throws DBException{ 
		try{ 
			CategoryCriteria categorycriteria = new CategoryCriteria();
			PstCategoryCriteria pstCategoryCriteria = new PstCategoryCriteria(oid); 
			categorycriteria.setOID(oid);

			categorycriteria.setCategoryAppraisalId(pstCategoryCriteria.getlong(FLD_CATEGORY_APPRAISAL_ID));
			categorycriteria.setCriteria(pstCategoryCriteria.getString(FLD_CRITERIA));

			return categorycriteria; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryCriteria(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(CategoryCriteria categorycriteria) throws DBException{ 
		try{ 
			PstCategoryCriteria pstCategoryCriteria = new PstCategoryCriteria(0);

			pstCategoryCriteria.setLong(FLD_CATEGORY_APPRAISAL_ID, categorycriteria.getCategoryAppraisalId());
			pstCategoryCriteria.setString(FLD_CRITERIA, categorycriteria.getCriteria());

			pstCategoryCriteria.insert(); 
			categorycriteria.setOID(pstCategoryCriteria.getlong(FLD_CATEGORY_CRITERIA_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryCriteria(0),DBException.UNKNOWN); 
		}
		return categorycriteria.getOID();
	}

	public static long updateExc(CategoryCriteria categorycriteria) throws DBException{ 
		try{ 
			if(categorycriteria.getOID() != 0){ 
				PstCategoryCriteria pstCategoryCriteria = new PstCategoryCriteria(categorycriteria.getOID());

				pstCategoryCriteria.setLong(FLD_CATEGORY_APPRAISAL_ID, categorycriteria.getCategoryAppraisalId());
				pstCategoryCriteria.setString(FLD_CRITERIA, categorycriteria.getCriteria());

				pstCategoryCriteria.update(); 
				return categorycriteria.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryCriteria(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstCategoryCriteria pstCategoryCriteria = new PstCategoryCriteria(oid);
			pstCategoryCriteria.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryCriteria(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_CRITERIA; 
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
				CategoryCriteria categorycriteria = new CategoryCriteria();
				resultToObject(rs, categorycriteria);
				lists.add(categorycriteria);
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

	private static void resultToObject(ResultSet rs, CategoryCriteria categorycriteria){
		try{
			categorycriteria.setOID(rs.getLong(PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID]));
			categorycriteria.setCategoryAppraisalId(rs.getLong(PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_APPRAISAL_ID]));
			categorycriteria.setCriteria(rs.getString(PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CRITERIA]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long categoryCriteriaId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_CRITERIA + " WHERE " + 
						PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID] + " = " + categoryCriteriaId;

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
			String sql = "SELECT COUNT("+ PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_CRITERIA_ID] + ") FROM " + TBL_HR_CATEGORY_CRITERIA;
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
			  	   CategoryCriteria categorycriteria = (CategoryCriteria)list.get(ls);
				   if(oid == categorycriteria.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static boolean checkCategoryAppraisal(long categoryAppraisalId){
    	DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_CRITERIA + " WHERE " + 
						PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_APPRAISAL_ID] + " = '" + categoryAppraisalId +"'";

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


    public static boolean checkMaster(long oid)
    {
        if(PstPerformanceEvaluation.checkCategoryCriteria(oid))
            return true;
        else
            return false;
    }

}
