
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

public class PstScheduleCategory extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_SCHEDULE_CATEGORY = "HR_SCHEDULE_CATEGORY";

	public static final  int FLD_SCHEDULE_CATEGORY_ID = 0;
	public static final  int FLD_CATEGORY = 1;
	public static final  int FLD_DESCRIPTION = 2;
        public static final  int FLD_CATEGORY_TYPE = 3;

	public static final  String[] fieldNames = {
		"SCHEDULE_CATEGORY_ID",
		"CATEGORY",
		"DESCRIPTION",
                "CATEGORY_TYPE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
                TYPE_INT
	 }; 

         public static final int CATEGORY_REGULAR 	= 0;
         public static final int CATEGORY_SPLIT_SHIFT	= 1;
         public static final int CATEGORY_NIGHT_WORKER  = 2;

         public static final String[] fieldCategoryType = {
            "Regular",
            "Split Shift",
            "Night Worker"
         };        
         
         
	public PstScheduleCategory(){
	}

	public PstScheduleCategory(int i) throws DBException { 
		super(new PstScheduleCategory()); 
	}

	public PstScheduleCategory(String sOid) throws DBException { 
		super(new PstScheduleCategory(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstScheduleCategory(long lOid) throws DBException { 
		super(new PstScheduleCategory(0)); 
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
		return TBL_HR_SCHEDULE_CATEGORY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstScheduleCategory().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ScheduleCategory schedulecategory = fetchExc(ent.getOID()); 
		ent = (Entity)schedulecategory; 
		return schedulecategory.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ScheduleCategory) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ScheduleCategory) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static ScheduleCategory fetchExc(long oid) throws DBException{ 
		try{ 
			ScheduleCategory schedulecategory = new ScheduleCategory();
			PstScheduleCategory pstScheduleCategory = new PstScheduleCategory(oid); 
			schedulecategory.setOID(oid);

			schedulecategory.setCategory(pstScheduleCategory.getString(FLD_CATEGORY));
			schedulecategory.setDescription(pstScheduleCategory.getString(FLD_DESCRIPTION));
                        schedulecategory.setCategoryType(pstScheduleCategory.getInt(FLD_CATEGORY_TYPE));

			return schedulecategory; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleCategory(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ScheduleCategory schedulecategory) throws DBException{ 
		try{ 
			PstScheduleCategory pstScheduleCategory = new PstScheduleCategory(0);

			pstScheduleCategory.setString(FLD_CATEGORY, schedulecategory.getCategory());
			pstScheduleCategory.setString(FLD_DESCRIPTION, schedulecategory.getDescription());
                        pstScheduleCategory.setInt(FLD_CATEGORY_TYPE, schedulecategory.getCategoryType());

			pstScheduleCategory.insert(); 
			schedulecategory.setOID(pstScheduleCategory.getlong(FLD_SCHEDULE_CATEGORY_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleCategory(0),DBException.UNKNOWN); 
		}
		return schedulecategory.getOID();
	}

	public static long updateExc(ScheduleCategory schedulecategory) throws DBException{ 
		try{ 
			if(schedulecategory.getOID() != 0){ 
				PstScheduleCategory pstScheduleCategory = new PstScheduleCategory(schedulecategory.getOID());

				pstScheduleCategory.setString(FLD_CATEGORY, schedulecategory.getCategory());
				pstScheduleCategory.setString(FLD_DESCRIPTION, schedulecategory.getDescription());
                                pstScheduleCategory.setInt(FLD_CATEGORY_TYPE, schedulecategory.getCategoryType());

				pstScheduleCategory.update(); 
				return schedulecategory.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleCategory(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstScheduleCategory pstScheduleCategory = new PstScheduleCategory(oid);
			pstScheduleCategory.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstScheduleCategory(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_SCHEDULE_CATEGORY; 
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
				ScheduleCategory schedulecategory = new ScheduleCategory();
				resultToObject(rs, schedulecategory);
				lists.add(schedulecategory);
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

	private static void resultToObject(ResultSet rs, ScheduleCategory schedulecategory){
		try{
			schedulecategory.setOID(rs.getLong(PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_SCHEDULE_CATEGORY_ID]));
			schedulecategory.setCategory(rs.getString(PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY]));
			schedulecategory.setDescription(rs.getString(PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_DESCRIPTION]));
                        schedulecategory.setCategoryType(rs.getInt(PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long scheduleCategoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_SCHEDULE_CATEGORY + " WHERE " + 
						PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_SCHEDULE_CATEGORY_ID] + " = " + scheduleCategoryId;

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
			String sql = "SELECT COUNT("+ PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_SCHEDULE_CATEGORY_ID] + ") FROM " + TBL_HR_SCHEDULE_CATEGORY;
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
			  	   ScheduleCategory schedulecategory = (ScheduleCategory)list.get(ls);
				   if(oid == schedulecategory.getOID())
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
    	if(PstScheduleSymbol.checkScheduleCategory(oid))
            return true;
    	else
            return false;
    }

}
