
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

import java.sql.*;
import java.util.*;


/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*;

public class PstDivision extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_DIVISION = "HR_DIVISION";

	public static final  int FLD_DIVISION_ID = 0;
	public static final  int FLD_DIVISION = 1;
	public static final  int FLD_DESCRIPTION = 2;
    public static final int FLD_LOCATION_ID = 3;

	public static final  String[] fieldNames = {
		"DIVISION_ID",
		"DIVISION",
		"DESCRIPTION",
    "LOCATION_ID"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
     TYPE_LONG
	 }; 

	public PstDivision(){
	}

	public PstDivision(int i) throws DBException { 
		super(new PstDivision());
	}

	public PstDivision(String sOid) throws DBException { 
		super(new PstDivision(0));
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDivision(long lOid) throws DBException { 
		super(new PstDivision(0));
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
		return TBL_HR_DIVISION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDivision().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Division division = fetchExc(ent.getOID());
		ent = (Entity)division;
		return division.getOID();
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Division) ent);
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Division) ent);
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Division fetchExc(long oid) throws DBException{
		try{ 
			Division division = new Division();
			PstDivision pstDivision = new PstDivision(oid);
			division.setOID(oid);

			division.setDivision(pstDivision.getString(FLD_DIVISION));
			division.setDescription(pstDivision.getString(FLD_DESCRIPTION));

			return division;
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDivision(0),DBException.UNKNOWN);
		} 
	}

	public static long insertExc(Division division) throws DBException{
		try{ 
			PstDivision pstDivision = new PstDivision(0);

			pstDivision.setString(FLD_DIVISION, division.getDivision());
			pstDivision.setString(FLD_DESCRIPTION, division.getDescription());

			pstDivision.insert();
			division.setOID(pstDivision.getlong(FLD_DIVISION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDivision(0),DBException.UNKNOWN);
		}
		return division.getOID();
	}

	public static long updateExc(Division division) throws DBException{
		try{ 
			if(division.getOID() != 0){
				PstDivision pstDivision = new PstDivision(division.getOID());

				pstDivision.setString(FLD_DIVISION, division.getDivision());
				pstDivision.setString(FLD_DESCRIPTION, division.getDescription());

				pstDivision.update();
				return division.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDivision(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDivision pstPosition = new PstDivision(oid);
			pstPosition.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDivision(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_HR_DIVISION;
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
				Division division = new Division();
				resultToObject(rs, division);
				lists.add(division);
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

	public static void resultToObject(ResultSet rs, Division division){
		try{
			division.setOID(rs.getLong(PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]));
			division.setDivision(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]));
			division.setDescription(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DESCRIPTION]));
        division.setLocationId(rs.getLong(PstDivision.fieldNames[PstDivision.FLD_LOCATION_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long positionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DIVISION+ " WHERE " +
						PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " = " + positionId;

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
			String sql = "SELECT COUNT("+ PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + ") FROM " + TBL_HR_DIVISION;
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
			  	   Division division = (Division)list.get(ls);
				   if(oid == division.getOID())
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
        if(PstEmployee.checkPosition(oid))
            return true;
        else{
            if(PstCareerPath.checkPosition(oid))
            	return true;
            else
                return false;

        }
    }
}
