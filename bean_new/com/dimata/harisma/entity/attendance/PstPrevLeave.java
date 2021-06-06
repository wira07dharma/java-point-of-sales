
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

package com.dimata.harisma.entity.attendance; 

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
import com.dimata.harisma.entity.attendance.*; 

public class PstPrevLeave extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_PREV_LEAVE = "HR_PREV_LEAVE";

	public static final  int FLD_PREV_LEAVE_ID = 0;
	public static final  int FLD_DP_LM = 1;
	public static final  int FLD_DP_ADD = 2;
	public static final  int FLD_DP_TAKEN = 3;
	public static final  int FLD_DP_BAL = 4;
	public static final  int FLD_AL_LM = 5;
	public static final  int FLD_AL_ADD = 6;
	public static final  int FLD_AL_TAKEN = 7;
	public static final  int FLD_AL_BAL = 8;
	public static final  int FLD_LL_LM = 9;
	public static final  int FLD_LL_ADD = 10;
	public static final  int FLD_LL_TAKEN = 11;
	public static final  int FLD_LL_BAL = 12;
    public static final  int FLD_MONTH = 13;


	public static final  String[] fieldNames = {
		"PREV_LEAVE_ID",
		"DP_LM",
		"DP_ADD",
		"DP_TAKEN",
		"DP_BAL",
		"AL_LM",
		"AL_ADD",
		"AL_TAKEN",
		"AL_BAL",
		"LL_LM",
		"LL_ADD",
		"LL_TAKEN",
		"LL_BAL",
        "MONTH"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
        TYPE_INT
	 }; 

	public PstPrevLeave(){
	}

	public PstPrevLeave(int i) throws DBException { 
		super(new PstPrevLeave()); 
	}

	public PstPrevLeave(String sOid) throws DBException { 
		super(new PstPrevLeave(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstPrevLeave(long lOid) throws DBException { 
		super(new PstPrevLeave(0)); 
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
		return TBL_HR_PREV_LEAVE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstPrevLeave().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		PrevLeave prevleave = fetchExc(ent.getOID()); 
		ent = (Entity)prevleave; 
		return prevleave.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((PrevLeave) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((PrevLeave) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static PrevLeave fetchExc(long oid) throws DBException{ 
		try{ 
			PrevLeave prevleave = new PrevLeave();
			PstPrevLeave pstPrevLeave = new PstPrevLeave(oid); 
			prevleave.setOID(oid);

			prevleave.setDpLm(pstPrevLeave.getInt(FLD_DP_LM));
			prevleave.setDpAdd(pstPrevLeave.getInt(FLD_DP_ADD));
			prevleave.setDpTaken(pstPrevLeave.getInt(FLD_DP_TAKEN));
			prevleave.setDpBal(pstPrevLeave.getInt(FLD_DP_BAL));
			prevleave.setAlLm(pstPrevLeave.getInt(FLD_AL_LM));
			prevleave.setAlAdd(pstPrevLeave.getInt(FLD_AL_ADD));
			prevleave.setAlTaken(pstPrevLeave.getInt(FLD_AL_TAKEN));
			prevleave.setAlBal(pstPrevLeave.getInt(FLD_AL_BAL));
			prevleave.setLlLm(pstPrevLeave.getInt(FLD_LL_LM));
			prevleave.setLlAdd(pstPrevLeave.getInt(FLD_LL_ADD));
			prevleave.setLlTaken(pstPrevLeave.getInt(FLD_LL_TAKEN));
			prevleave.setLlBal(pstPrevLeave.getInt(FLD_LL_BAL));
            prevleave.setMonth(pstPrevLeave.getInt(FLD_MONTH));


			return prevleave; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPrevLeave(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(PrevLeave prevleave) throws DBException{ 
		try{ 
			PstPrevLeave pstPrevLeave = new PstPrevLeave(0);

			pstPrevLeave.setInt(FLD_DP_LM, prevleave.getDpLm());
			pstPrevLeave.setInt(FLD_DP_ADD, prevleave.getDpAdd());
			pstPrevLeave.setInt(FLD_DP_TAKEN, prevleave.getDpTaken());
			pstPrevLeave.setInt(FLD_DP_BAL, prevleave.getDpBal());
			pstPrevLeave.setInt(FLD_AL_LM, prevleave.getAlLm());
			pstPrevLeave.setInt(FLD_AL_ADD, prevleave.getAlAdd());
			pstPrevLeave.setInt(FLD_AL_TAKEN, prevleave.getAlTaken());
			pstPrevLeave.setInt(FLD_AL_BAL, prevleave.getAlBal());
			pstPrevLeave.setInt(FLD_LL_LM, prevleave.getLlLm());
			pstPrevLeave.setInt(FLD_LL_ADD, prevleave.getLlAdd());
			pstPrevLeave.setInt(FLD_LL_TAKEN, prevleave.getLlTaken());
			pstPrevLeave.setInt(FLD_LL_BAL, prevleave.getLlBal());
            pstPrevLeave.setInt(FLD_MONTH, prevleave.getMonth());

			pstPrevLeave.insert(); 
			prevleave.setOID(pstPrevLeave.getlong(FLD_PREV_LEAVE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPrevLeave(0),DBException.UNKNOWN); 
		}
		return prevleave.getOID();
	}

	public static long updateExc(PrevLeave prevleave) throws DBException{ 
		try{ 
			if(prevleave.getOID() != 0){ 
				PstPrevLeave pstPrevLeave = new PstPrevLeave(prevleave.getOID());

				pstPrevLeave.setInt(FLD_DP_LM, prevleave.getDpLm());
				pstPrevLeave.setInt(FLD_DP_ADD, prevleave.getDpAdd());
				pstPrevLeave.setInt(FLD_DP_TAKEN, prevleave.getDpTaken());
				pstPrevLeave.setInt(FLD_DP_BAL, prevleave.getDpBal());
				pstPrevLeave.setInt(FLD_AL_LM, prevleave.getAlLm());
				pstPrevLeave.setInt(FLD_AL_ADD, prevleave.getAlAdd());
				pstPrevLeave.setInt(FLD_AL_TAKEN, prevleave.getAlTaken());
				pstPrevLeave.setInt(FLD_AL_BAL, prevleave.getAlBal());
				pstPrevLeave.setInt(FLD_LL_LM, prevleave.getLlLm());
				pstPrevLeave.setInt(FLD_LL_ADD, prevleave.getLlAdd());
				pstPrevLeave.setInt(FLD_LL_TAKEN, prevleave.getLlTaken());
				pstPrevLeave.setInt(FLD_LL_BAL, prevleave.getLlBal());
                pstPrevLeave.setInt(FLD_MONTH, prevleave.getMonth());

				pstPrevLeave.update(); 
				return prevleave.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPrevLeave(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstPrevLeave pstPrevLeave = new PstPrevLeave(oid);
			pstPrevLeave.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPrevLeave(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_PREV_LEAVE; 
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
				PrevLeave prevleave = new PrevLeave();
				resultToObject(rs, prevleave);
				lists.add(prevleave);
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

	private static void resultToObject(ResultSet rs, PrevLeave prevleave){
		try{
			prevleave.setOID(rs.getLong(PstPrevLeave.fieldNames[PstPrevLeave.FLD_PREV_LEAVE_ID]));
			prevleave.setDpLm(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_DP_LM]));
			prevleave.setDpAdd(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_DP_ADD]));
			prevleave.setDpTaken(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_DP_TAKEN]));
			prevleave.setDpBal(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_DP_BAL]));
			prevleave.setAlLm(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_AL_LM]));
			prevleave.setAlAdd(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_AL_ADD]));
			prevleave.setAlTaken(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_AL_TAKEN]));
			prevleave.setAlBal(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_AL_BAL]));
			prevleave.setLlLm(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_LL_LM]));
			prevleave.setLlAdd(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_LL_ADD]));
			prevleave.setLlTaken(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_LL_TAKEN]));
			prevleave.setLlBal(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_LL_BAL]));
            prevleave.setMonth(rs.getInt(PstPrevLeave.fieldNames[PstPrevLeave.FLD_MONTH]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long prevLeaveId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PREV_LEAVE + " WHERE " + 
						PstPrevLeave.fieldNames[PstPrevLeave.FLD_PREV_LEAVE_ID] + " = " + prevLeaveId;

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
			String sql = "SELECT COUNT("+ PstPrevLeave.fieldNames[PstPrevLeave.FLD_PREV_LEAVE_ID] + ") FROM " + TBL_HR_PREV_LEAVE;
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
			  	   PrevLeave prevleave = (PrevLeave)list.get(ls);
				   if(oid == prevleave.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
}
