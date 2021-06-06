
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

public class PstPresence extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_PRESENCE = "HR_PRESENCE";

	public static final  int FLD_PRESENCE_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_PRESENCE_DATETIME = 2;
	public static final  int FLD_STATUS = 3;
	public static final  int FLD_ANALYZED = 4;

	public static final  String[] fieldNames = {
		"PRESENCE_ID",
		"EMPLOYEE_ID",
		"PRESENCE_DATETIME",
		"STATUS",
                "ANALYZED"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_INT,
                TYPE_INT
	 }; 

	public PstPresence(){
	}

	public PstPresence(int i) throws DBException { 
		super(new PstPresence()); 
	}

	public PstPresence(String sOid) throws DBException { 
		super(new PstPresence(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstPresence(long lOid) throws DBException { 
		super(new PstPresence(0)); 
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
		return TBL_HR_PRESENCE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstPresence().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Presence presence = fetchExc(ent.getOID()); 
		ent = (Entity)presence; 
		return presence.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Presence) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Presence) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Presence fetchExc(long oid) throws DBException{ 
		try{ 
			Presence presence = new Presence();
			PstPresence pstPresence = new PstPresence(oid); 
			presence.setOID(oid);

			presence.setEmployeeId(pstPresence.getlong(FLD_EMPLOYEE_ID));
			presence.setPresenceDatetime(pstPresence.getDate(FLD_PRESENCE_DATETIME));
			presence.setStatus(pstPresence.getInt(FLD_STATUS));
			presence.setAnalyzed(pstPresence.getInt(FLD_ANALYZED));

			return presence; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Presence presence) throws DBException{ 
		try{ 
			PstPresence pstPresence = new PstPresence(0);

			pstPresence.setLong(FLD_EMPLOYEE_ID, presence.getEmployeeId());
			pstPresence.setDate(FLD_PRESENCE_DATETIME, presence.getPresenceDatetime());
			pstPresence.setInt(FLD_STATUS, presence.getStatus());
			pstPresence.setInt(FLD_ANALYZED, presence.getAnalyzed());

			pstPresence.insert(); 
			presence.setOID(pstPresence.getlong(FLD_PRESENCE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		}
		return presence.getOID();
	}

	public static long updateExc(Presence presence) throws DBException{ 
		try{ 
			if(presence.getOID() != 0){ 
				PstPresence pstPresence = new PstPresence(presence.getOID());

				pstPresence.setLong(FLD_EMPLOYEE_ID, presence.getEmployeeId());
				pstPresence.setDate(FLD_PRESENCE_DATETIME, presence.getPresenceDatetime());
				pstPresence.setInt(FLD_STATUS, presence.getStatus());
				pstPresence.setInt(FLD_ANALYZED, presence.getAnalyzed());

				pstPresence.update(); 
				return presence.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstPresence pstPresence = new PstPresence(oid);
			pstPresence.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_PRESENCE; 
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
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
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

	private static void resultToObject(ResultSet rs, Presence presence){
		try{
			presence.setOID(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]));
			presence.setEmployeeId(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]));
			//presence.setPresenceDatetime(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
            Date tm = DBHandler.convertDate(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]),rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));

            presence.setPresenceDatetime(tm); //rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
			presence.setStatus(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_STATUS]));
			presence.setAnalyzed(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_ANALYZED]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long presenceId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PRESENCE + " WHERE " + 
						PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] + " = " + presenceId;

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
			String sql = "SELECT COUNT("+ PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] + ") FROM " + TBL_HR_PRESENCE;
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
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   Presence presence = (Presence)list.get(ls);
				   if(oid == presence.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static long deleteByEmployee(long emplOID)
    {
    	try{
            String sql = " DELETE FROM "+PstPresence.TBL_HR_PRESENCE +
                 " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +
                 " = '" + emplOID +"'";
            int status = DBHandler.execUpdate(sql);
    	}
        catch(Exception exc){
        	System.out.println("error delete day of presence by employee "+exc.toString());
    	}
    	return emplOID;
    }
}
