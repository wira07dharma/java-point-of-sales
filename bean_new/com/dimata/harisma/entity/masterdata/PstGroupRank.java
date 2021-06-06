
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

public class PstGroupRank extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_GROUP_RANK = "HR_GROUP_RANK";

	public static final  int FLD_GROUP_RANK_ID = 0;
	public static final  int FLD_GROUP_NAME = 1;
	public static final  int FLD_DESCRIPTION = 2;

	public static final  String[] fieldNames = {
		"GROUP_RANK_ID",
		"GROUP_NAME",
		"DESCRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstGroupRank(){
	}

	public PstGroupRank(int i) throws DBException { 
		super(new PstGroupRank()); 
	}

	public PstGroupRank(String sOid) throws DBException { 
		super(new PstGroupRank(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstGroupRank(long lOid) throws DBException { 
		super(new PstGroupRank(0)); 
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
		return TBL_HR_GROUP_RANK;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstGroupRank().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		GroupRank grouprank = fetchExc(ent.getOID()); 
		ent = (Entity)grouprank; 
		return grouprank.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((GroupRank) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((GroupRank) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static GroupRank fetchExc(long oid) throws DBException{ 
		try{ 
			GroupRank grouprank = new GroupRank();
			PstGroupRank pstGroupRank = new PstGroupRank(oid); 
			grouprank.setOID(oid);

			grouprank.setGroupName(pstGroupRank.getString(FLD_GROUP_NAME));
			grouprank.setDescription(pstGroupRank.getString(FLD_DESCRIPTION));

			return grouprank; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupRank(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(GroupRank grouprank) throws DBException{ 
		try{ 
			PstGroupRank pstGroupRank = new PstGroupRank(0);

			pstGroupRank.setString(FLD_GROUP_NAME, grouprank.getGroupName());
			pstGroupRank.setString(FLD_DESCRIPTION, grouprank.getDescription());

			pstGroupRank.insert(); 
			grouprank.setOID(pstGroupRank.getlong(FLD_GROUP_RANK_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupRank(0),DBException.UNKNOWN); 
		}
		return grouprank.getOID();
	}

	public static long updateExc(GroupRank grouprank) throws DBException{ 
		try{ 
			if(grouprank.getOID() != 0){ 
				PstGroupRank pstGroupRank = new PstGroupRank(grouprank.getOID());

				pstGroupRank.setString(FLD_GROUP_NAME, grouprank.getGroupName());
				pstGroupRank.setString(FLD_DESCRIPTION, grouprank.getDescription());

				pstGroupRank.update(); 
				return grouprank.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupRank(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstGroupRank pstGroupRank = new PstGroupRank(oid);
			pstGroupRank.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstGroupRank(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_GROUP_RANK; 
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
				GroupRank grouprank = new GroupRank();
				resultToObject(rs, grouprank);
				lists.add(grouprank);
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

	private static void resultToObject(ResultSet rs, GroupRank grouprank){
		try{
			grouprank.setOID(rs.getLong(PstGroupRank.fieldNames[PstGroupRank.FLD_GROUP_RANK_ID]));
			grouprank.setGroupName(rs.getString(PstGroupRank.fieldNames[PstGroupRank.FLD_GROUP_NAME]));
			grouprank.setDescription(rs.getString(PstGroupRank.fieldNames[PstGroupRank.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long groupRankId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_GROUP_RANK + " WHERE " + 
						PstGroupRank.fieldNames[PstGroupRank.FLD_GROUP_RANK_ID] + " = " + groupRankId;

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
			String sql = "SELECT COUNT("+ PstGroupRank.fieldNames[PstGroupRank.FLD_GROUP_RANK_ID] + ") FROM " + TBL_HR_GROUP_RANK;
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
			  	   GroupRank grouprank = (GroupRank)list.get(ls);
				   if(oid == grouprank.getOID())
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
    	if(PstGroupCategory.checkGroupRank(oid))
            return true;
    	else{
        	if(PstExpCoverage.checkGroupRank(oid))
                return true;
        	else{
            	if(PstLevel.checkGroupRank(oid))
                    return true;
            	else
                    return false;

        	}
    	}
    }
}
