
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

public class PstExpCoverage extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_EXPLANATION_COVERAGE = "HR_EXPLANATION_COVERAGE";

	public static final  int FLD_EXPLANATION_COVERAGE_ID = 0;
	public static final  int FLD_GROUP_RANK_ID = 1;
	public static final  int FLD_DESCRIPTIONS = 2;
	public static final  int FLD_DEF_COVERAGE = 3;

	public static final  String[] fieldNames = {
		"EXPLANATION_COVERAGE_ID",
		"GROUP_RANK_ID",
		"DESCRIPTION",
		"DEFINITION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstExpCoverage(){
	}

	public PstExpCoverage(int i) throws DBException { 
		super(new PstExpCoverage()); 
	}

	public PstExpCoverage(String sOid) throws DBException { 
		super(new PstExpCoverage(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstExpCoverage(long lOid) throws DBException { 
		super(new PstExpCoverage(0)); 
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
		return TBL_HR_EXPLANATION_COVERAGE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstExpCoverage().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ExpCoverage expcoverage = fetchExc(ent.getOID()); 
		ent = (Entity)expcoverage; 
		return expcoverage.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ExpCoverage) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ExpCoverage) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static ExpCoverage fetchExc(long oid) throws DBException{ 
		try{ 
			ExpCoverage expcoverage = new ExpCoverage();
			PstExpCoverage pstExpCoverage = new PstExpCoverage(oid); 
			expcoverage.setOID(oid);

			expcoverage.setGroupRankId(pstExpCoverage.getlong(FLD_GROUP_RANK_ID));
			expcoverage.setDescriptions(pstExpCoverage.getString(FLD_DESCRIPTIONS));
			expcoverage.setDefCoverage(pstExpCoverage.getString(FLD_DEF_COVERAGE));

			return expcoverage; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExpCoverage(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ExpCoverage expcoverage) throws DBException{ 
		try{ 
			PstExpCoverage pstExpCoverage = new PstExpCoverage(0);

			pstExpCoverage.setLong(FLD_GROUP_RANK_ID, expcoverage.getGroupRankId());
			pstExpCoverage.setString(FLD_DESCRIPTIONS, expcoverage.getDescriptions());
			pstExpCoverage.setString(FLD_DEF_COVERAGE, expcoverage.getDefCoverage());

			pstExpCoverage.insert(); 
			expcoverage.setOID(pstExpCoverage.getlong(FLD_EXPLANATION_COVERAGE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExpCoverage(0),DBException.UNKNOWN); 
		}
		return expcoverage.getOID();
	}

	public static long updateExc(ExpCoverage expcoverage) throws DBException{ 
		try{ 
			if(expcoverage.getOID() != 0){ 
				PstExpCoverage pstExpCoverage = new PstExpCoverage(expcoverage.getOID());

				pstExpCoverage.setLong(FLD_GROUP_RANK_ID, expcoverage.getGroupRankId());
				pstExpCoverage.setString(FLD_DESCRIPTIONS, expcoverage.getDescriptions());
				pstExpCoverage.setString(FLD_DEF_COVERAGE, expcoverage.getDefCoverage());

				pstExpCoverage.update(); 
				return expcoverage.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExpCoverage(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstExpCoverage pstExpCoverage = new PstExpCoverage(oid);
			pstExpCoverage.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstExpCoverage(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_EXPLANATION_COVERAGE; 
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
				ExpCoverage expcoverage = new ExpCoverage();
				resultToObject(rs, expcoverage);
				lists.add(expcoverage);
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

	private static void resultToObject(ResultSet rs, ExpCoverage expcoverage){
		try{
			expcoverage.setOID(rs.getLong(PstExpCoverage.fieldNames[PstExpCoverage.FLD_EXPLANATION_COVERAGE_ID]));
			expcoverage.setGroupRankId(rs.getLong(PstExpCoverage.fieldNames[PstExpCoverage.FLD_GROUP_RANK_ID]));
			expcoverage.setDescriptions(rs.getString(PstExpCoverage.fieldNames[PstExpCoverage.FLD_DESCRIPTIONS]));
			expcoverage.setDefCoverage(rs.getString(PstExpCoverage.fieldNames[PstExpCoverage.FLD_DEF_COVERAGE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long explanationCoverageId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EXPLANATION_COVERAGE + " WHERE " + 
						PstExpCoverage.fieldNames[PstExpCoverage.FLD_EXPLANATION_COVERAGE_ID] + " = " + explanationCoverageId;

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
			String sql = "SELECT COUNT("+ PstExpCoverage.fieldNames[PstExpCoverage.FLD_EXPLANATION_COVERAGE_ID] + ") FROM " + TBL_HR_EXPLANATION_COVERAGE;
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
			  	   ExpCoverage expcoverage = (ExpCoverage)list.get(ls);
				   if(oid == expcoverage.getOID())
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
			String sql = "SELECT * FROM " + TBL_HR_EXPLANATION_COVERAGE + " WHERE " + 
						PstExpCoverage.fieldNames[PstExpCoverage.FLD_GROUP_RANK_ID] + " = '" + groupRankId + "'";

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
