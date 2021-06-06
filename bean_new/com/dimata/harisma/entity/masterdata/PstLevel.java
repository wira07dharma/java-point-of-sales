
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.masterdata;

/* package java */ 
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

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

public class PstLevel extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LEVEL = "hr_level";

	public static final  int FLD_LEVEL_ID = 0;
    public static final  int FLD_GROUP_RANK_ID = 1;
	public static final  int FLD_LEVEL = 2;
	public static final  int FLD_DESCRIPTION = 3;

	public static final  String[] fieldNames = {
		"LEVEL_ID",
        "GROUP_RANK_ID",
		"LEVEL",
		"DESCRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstLevel(){
	}

	public PstLevel(int i) throws DBException { 
		super(new PstLevel()); 
	}

	public PstLevel(String sOid) throws DBException { 
		super(new PstLevel(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLevel(long lOid) throws DBException { 
		super(new PstLevel(0)); 
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
		return TBL_HR_LEVEL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLevel().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Level level = fetchExc(ent.getOID()); 
		ent = (Entity)level; 
		return level.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Level) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Level) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Level fetchExc(long oid) throws DBException{ 
		try{ 
			Level level = new Level();
			PstLevel pstLevel = new PstLevel(oid); 
			level.setOID(oid);

            level.setGroupRankId(pstLevel.getlong(FLD_GROUP_RANK_ID));
			level.setLevel(pstLevel.getString(FLD_LEVEL));
			level.setDescription(pstLevel.getString(FLD_DESCRIPTION));

			return level; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLevel(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Level level) throws DBException{ 
		try{ 
			PstLevel pstLevel = new PstLevel(0);

            pstLevel.setLong(FLD_GROUP_RANK_ID, level.getGroupRankId());
			pstLevel.setString(FLD_LEVEL, level.getLevel());
			pstLevel.setString(FLD_DESCRIPTION, level.getDescription());

			pstLevel.insert(); 
			level.setOID(pstLevel.getlong(FLD_LEVEL_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLevel(0),DBException.UNKNOWN); 
		}
		return level.getOID();
	}

	public static long updateExc(Level level) throws DBException{ 
		try{ 
			if(level.getOID() != 0){ 
				PstLevel pstLevel = new PstLevel(level.getOID());
                System.out.println(level.getGroupRankId());
                pstLevel.setLong(FLD_GROUP_RANK_ID, level.getGroupRankId());
				pstLevel.setString(FLD_LEVEL, level.getLevel());
				pstLevel.setString(FLD_DESCRIPTION, level.getDescription());

				pstLevel.update(); 
				return level.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLevel(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLevel pstLevel = new PstLevel(oid);
			pstLevel.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLevel(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LEVEL; 
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
				Level level = new Level();
				resultToObject(rs, level);
				lists.add(level);
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

	public static void resultToObject(ResultSet rs, Level level){
		try{
			level.setOID(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]));
            level.setGroupRankId(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_GROUP_RANK_ID]));
			level.setLevel(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]));
			level.setDescription(rs.getString(PstLevel.fieldNames[PstLevel.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long levelId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LEVEL + " WHERE " + 
						PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " = '" + levelId +"'";

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
			String sql = "SELECT COUNT("+ PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ") FROM " + TBL_HR_LEVEL;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause,String order){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   Level level = (Level)list.get(ls);
				   if(oid == level.getOID())
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
			String sql = "SELECT * FROM " + TBL_HR_LEVEL + " WHERE " + 
						PstLevel.fieldNames[PstLevel.FLD_GROUP_RANK_ID] + " = '" + groupRankId +"'";

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


    public static boolean checkMaster(long oid){
    	if(PstEmployee.checkLevel(oid))
            return true;
    	else
            return false;
    }
}
