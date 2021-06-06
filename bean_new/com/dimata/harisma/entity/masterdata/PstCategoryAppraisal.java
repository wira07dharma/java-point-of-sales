
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
public class PstCategoryAppraisal extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_CATEGORY_APPRAISAL = "HR_CATEGORY_APPRAISAL";

	public static final  int FLD_CATEGORY_APPRAISAL_ID = 0;
	public static final  int FLD_GROUP_CATEGORY_ID = 1;
	public static final  int FLD_CATEGORY = 2;
	public static final  int FLD_DESCRIPTION = 3;

	public static final  String[] fieldNames = {
		"CATEGORY_APPRAISAL_ID",
		"GROUP_CATEGORY_ID",
		"CATEGORY",
		"DESCRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstCategoryAppraisal(){
	}

	public PstCategoryAppraisal(int i) throws DBException { 
		super(new PstCategoryAppraisal()); 
	}

	public PstCategoryAppraisal(String sOid) throws DBException { 
		super(new PstCategoryAppraisal(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstCategoryAppraisal(long lOid) throws DBException { 
		super(new PstCategoryAppraisal(0)); 
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
		return TBL_HR_CATEGORY_APPRAISAL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstCategoryAppraisal().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		CategoryAppraisal categoryappraisal = fetchExc(ent.getOID()); 
		ent = (Entity)categoryappraisal; 
		return categoryappraisal.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((CategoryAppraisal) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((CategoryAppraisal) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static CategoryAppraisal fetchExc(long oid) throws DBException{ 
		try{ 
			CategoryAppraisal categoryappraisal = new CategoryAppraisal();
			PstCategoryAppraisal pstCategoryAppraisal = new PstCategoryAppraisal(oid); 
			categoryappraisal.setOID(oid);

			categoryappraisal.setGroupCategoryId(pstCategoryAppraisal.getlong(FLD_GROUP_CATEGORY_ID));
			categoryappraisal.setCategory(pstCategoryAppraisal.getString(FLD_CATEGORY));
			categoryappraisal.setDescription(pstCategoryAppraisal.getString(FLD_DESCRIPTION));

			return categoryappraisal; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryAppraisal(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(CategoryAppraisal categoryappraisal) throws DBException{ 
		try{ 
			PstCategoryAppraisal pstCategoryAppraisal = new PstCategoryAppraisal(0);

			pstCategoryAppraisal.setLong(FLD_GROUP_CATEGORY_ID, categoryappraisal.getGroupCategoryId());
			pstCategoryAppraisal.setString(FLD_CATEGORY, categoryappraisal.getCategory());
			pstCategoryAppraisal.setString(FLD_DESCRIPTION, categoryappraisal.getDescription());

			pstCategoryAppraisal.insert(); 
			categoryappraisal.setOID(pstCategoryAppraisal.getlong(FLD_CATEGORY_APPRAISAL_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryAppraisal(0),DBException.UNKNOWN); 
		}
		return categoryappraisal.getOID();
	}

	public static long updateExc(CategoryAppraisal categoryappraisal) throws DBException{ 
		try{ 
			if(categoryappraisal.getOID() != 0){ 
				PstCategoryAppraisal pstCategoryAppraisal = new PstCategoryAppraisal(categoryappraisal.getOID());

				pstCategoryAppraisal.setLong(FLD_GROUP_CATEGORY_ID, categoryappraisal.getGroupCategoryId());
				pstCategoryAppraisal.setString(FLD_CATEGORY, categoryappraisal.getCategory());
				pstCategoryAppraisal.setString(FLD_DESCRIPTION, categoryappraisal.getDescription());

				pstCategoryAppraisal.update(); 
				return categoryappraisal.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryAppraisal(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstCategoryAppraisal pstCategoryAppraisal = new PstCategoryAppraisal(oid);
			pstCategoryAppraisal.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCategoryAppraisal(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_APPRAISAL; 
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
				CategoryAppraisal categoryappraisal = new CategoryAppraisal();
				resultToObject(rs, categoryappraisal);
				lists.add(categoryappraisal);
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

	private static void resultToObject(ResultSet rs, CategoryAppraisal categoryappraisal){
		try{
			categoryappraisal.setOID(rs.getLong(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID]));
			categoryappraisal.setGroupCategoryId(rs.getLong(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_GROUP_CATEGORY_ID]));
			categoryappraisal.setCategory(rs.getString(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY]));
			categoryappraisal.setDescription(rs.getString(PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long categoryAppraisalId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_APPRAISAL + " WHERE " + 
						PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID] + " = " + categoryAppraisalId;

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
			String sql = "SELECT COUNT("+ PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_CATEGORY_APPRAISAL_ID] + ") FROM " + TBL_HR_CATEGORY_APPRAISAL;
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
			  	   CategoryAppraisal categoryappraisal = (CategoryAppraisal)list.get(ls);
				   if(oid == categoryappraisal.getOID())
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
        vectSize = vectSize + mdl;
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

	public static boolean checkGroupCategory(long groupcategoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_APPRAISAL + " WHERE " + 
						PstCategoryAppraisal.fieldNames[PstCategoryAppraisal.FLD_GROUP_CATEGORY_ID] + " = '" + groupcategoryId+"'";

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
        if(PstCategoryCriteria.checkCategoryAppraisal(oid))
            return true;
        else{
            if(PstDevImprovementPlan.checkCategoryAppraisal(oid))
            	return true;
            else
                return false;
        }
    }



    public static Vector getPerfCategory(long groupRankId){
    	Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_CATEGORY_APPRAISAL+ " CA "+
                		 ", "+PstCategoryCriteria.TBL_HR_CATEGORY_CRITERIA + " CC "+
                         ", "+PstGroupCategory.TBL_HR_GROUP_CATEGORY + " GC "+
                         " WHERE CC."+PstCategoryCriteria.fieldNames[PstCategoryCriteria.FLD_CATEGORY_APPRAISAL_ID]+
                         " = CA."+fieldNames[FLD_CATEGORY_APPRAISAL_ID]+
                         " AND GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]+
                         " = CA."+fieldNames[FLD_GROUP_CATEGORY_ID]+
                         " AND GC."+PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_RANK_ID]+
                         " = '"+groupRankId+"'"+
                         " GROUP BY CA."+fieldNames[FLD_CATEGORY_APPRAISAL_ID];

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				CategoryAppraisal categoryappraisal = new CategoryAppraisal();
				resultToObject(rs, categoryappraisal);
				lists.add(categoryappraisal);
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
}
