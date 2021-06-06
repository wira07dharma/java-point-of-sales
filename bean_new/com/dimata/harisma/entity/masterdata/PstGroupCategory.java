
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

public class PstGroupCategory extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_GROUP_CATEGORY = "HR_GROUP_CATEGORY";

	public static final  int FLD_GROUP_CATEGORY_ID = 0;
	public static final  int FLD_GROUP_RANK_ID = 1;
	public static final  int FLD_GROUP_NAME = 2;

	public static final  String[] fieldNames = {
		"GROUP_CATEGORY_ID",
		"GROUP_RANK_ID",
		"GROUP_NAME"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstGroupCategory(){
	}

	public PstGroupCategory(int i) throws DBException { 
		super(new PstGroupCategory()); 
	}

	public PstGroupCategory(String sOid) throws DBException { 
		super(new PstGroupCategory(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstGroupCategory(long lOid) throws DBException { 
		super(new PstGroupCategory(0)); 
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
		return TBL_HR_GROUP_CATEGORY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstGroupCategory().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		GroupCategory groupcategory = fetchExc(ent.getOID()); 
		ent = (Entity)groupcategory; 
		return groupcategory.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((GroupCategory) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((GroupCategory) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static GroupCategory fetchExc(long oid) throws DBException{ 
		try{ 
			GroupCategory groupcategory = new GroupCategory();
			PstGroupCategory pstGroupCategory = new PstGroupCategory(oid); 
			groupcategory.setOID(oid);

			groupcategory.setGroupRankId(pstGroupCategory.getlong(FLD_GROUP_RANK_ID));
			groupcategory.setGroupName(pstGroupCategory.getString(FLD_GROUP_NAME));

			return groupcategory; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupCategory(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(GroupCategory groupcategory) throws DBException{ 
		try{ 
			PstGroupCategory pstGroupCategory = new PstGroupCategory(0);

			pstGroupCategory.setLong(FLD_GROUP_RANK_ID, groupcategory.getGroupRankId());
			pstGroupCategory.setString(FLD_GROUP_NAME, groupcategory.getGroupName());

			pstGroupCategory.insert(); 
			groupcategory.setOID(pstGroupCategory.getlong(FLD_GROUP_CATEGORY_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupCategory(0),DBException.UNKNOWN); 
		}
		return groupcategory.getOID();
	}

	public static long updateExc(GroupCategory groupcategory) throws DBException{ 
		try{ 
			if(groupcategory.getOID() != 0){ 
				PstGroupCategory pstGroupCategory = new PstGroupCategory(groupcategory.getOID());

				pstGroupCategory.setLong(FLD_GROUP_RANK_ID, groupcategory.getGroupRankId());
				pstGroupCategory.setString(FLD_GROUP_NAME, groupcategory.getGroupName());

				pstGroupCategory.update(); 
				return groupcategory.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupCategory(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstGroupCategory pstGroupCategory = new PstGroupCategory(oid);
			pstGroupCategory.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupCategory(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_GROUP_CATEGORY; 
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
				GroupCategory groupcategory = new GroupCategory();
				resultToObject(rs, groupcategory);
				lists.add(groupcategory);
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

	private static void resultToObject(ResultSet rs, GroupCategory groupcategory){
		try{
			groupcategory.setOID(rs.getLong(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID]));
			groupcategory.setGroupRankId(rs.getLong(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_RANK_ID]));
			groupcategory.setGroupName(rs.getString(PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_NAME]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long groupCategoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_GROUP_CATEGORY + " WHERE " + 
						PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID] + " = " + groupCategoryId;

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
			String sql = "SELECT COUNT("+ PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_CATEGORY_ID] + ") FROM " + TBL_HR_GROUP_CATEGORY;
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
			  	   GroupCategory groupcategory = (GroupCategory)list.get(ls);
				   if(oid == groupcategory.getOID())
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


	public static boolean checkGroupRank(long groupRankId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_GROUP_CATEGORY + " WHERE " + 
						PstGroupCategory.fieldNames[PstGroupCategory.FLD_GROUP_RANK_ID] + " = '" + groupRankId +"'";

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
    	if(PstCategoryAppraisal.checkGroupCategory(oid))
            return true;
    	else{
        	if(PstDevImprovement.checkGroupCategory(oid))
            	return true;
            else
                return false;
    	}

    }

}
