
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
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

/* package  harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.recruitment.*;

public class PstMarital extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_MARITAL = "HR_MARITAL";

	public static final  int FLD_MARITAL_ID = 0;
	public static final  int FLD_MARITAL_STATUS = 1;
	public static final  int FLD_MARITAL_CODE = 2;
	public static final  int FLD_NUM_OF_CHILDREN = 3;

	public static final  String[] fieldNames = {
		"MARITAL_ID",
		"MARITAL_STATUS",
		"MARITAL_CODE",
		"NUM_OF_CHILDREN"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT
	 }; 

	public PstMarital(){
	}

	public PstMarital(int i) throws DBException { 
		super(new PstMarital()); 
	}

	public PstMarital(String sOid) throws DBException { 
		super(new PstMarital(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstMarital(long lOid) throws DBException { 
		super(new PstMarital(0)); 
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
		return TBL_HR_MARITAL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstMarital().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Marital marital = fetchExc(ent.getOID()); 
		ent = (Entity)marital; 
		return marital.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Marital) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Marital) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Marital fetchExc(long oid) throws DBException{ 
		try{ 
			Marital marital = new Marital();
			PstMarital pstMarital = new PstMarital(oid); 
			marital.setOID(oid);

			marital.setMaritalStatus(pstMarital.getString(FLD_MARITAL_STATUS));
			marital.setMaritalCode(pstMarital.getString(FLD_MARITAL_CODE));
			marital.setNumOfChildren(pstMarital.getInt(FLD_NUM_OF_CHILDREN));

			return marital; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMarital(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Marital marital) throws DBException{ 
		try{ 
			PstMarital pstMarital = new PstMarital(0);

			pstMarital.setString(FLD_MARITAL_STATUS, marital.getMaritalStatus());
			pstMarital.setString(FLD_MARITAL_CODE, marital.getMaritalCode());
			pstMarital.setInt(FLD_NUM_OF_CHILDREN, marital.getNumOfChildren());

			pstMarital.insert(); 
			marital.setOID(pstMarital.getlong(FLD_MARITAL_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMarital(0),DBException.UNKNOWN); 
		}
		return marital.getOID();
	}

	public static long updateExc(Marital marital) throws DBException{ 
		try{ 
			if(marital.getOID() != 0){ 
				PstMarital pstMarital = new PstMarital(marital.getOID());

				pstMarital.setString(FLD_MARITAL_STATUS, marital.getMaritalStatus());
				pstMarital.setString(FLD_MARITAL_CODE, marital.getMaritalCode());
				pstMarital.setInt(FLD_NUM_OF_CHILDREN, marital.getNumOfChildren());

				pstMarital.update(); 
				return marital.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMarital(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstMarital pstMarital = new PstMarital(oid);
			pstMarital.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMarital(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_MARITAL; 
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
				Marital marital = new Marital();
				resultToObject(rs, marital);
				lists.add(marital);
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

	private static void resultToObject(ResultSet rs, Marital marital){
		try{
			marital.setOID(rs.getLong(PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID]));
			marital.setMaritalStatus(rs.getString(PstMarital.fieldNames[PstMarital.FLD_MARITAL_STATUS]));
			marital.setMaritalCode(rs.getString(PstMarital.fieldNames[PstMarital.FLD_MARITAL_CODE]));
			marital.setNumOfChildren(rs.getInt(PstMarital.fieldNames[PstMarital.FLD_NUM_OF_CHILDREN]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long maritalId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_MARITAL + " WHERE " + 
						PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID] + " = " + maritalId;

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
			String sql = "SELECT COUNT("+ PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID] + ") FROM " + TBL_HR_MARITAL;
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
			  	   Marital marital = (Marital)list.get(ls);
				   if(oid == marital.getOID())
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
        if(PstEmployee.checkMarital(oid) || PstRecrApplication.checkMarital(oid))
            return true;
        else
            return false;
    }
}
