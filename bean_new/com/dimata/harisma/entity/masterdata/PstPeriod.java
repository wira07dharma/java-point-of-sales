
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

/* package hr */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata. harisma.entity.attendance.*;

public class PstPeriod extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_PERIOD = "HR_PERIOD";

	public static final  int FLD_PERIOD_ID = 0;
	public static final  int FLD_PERIOD = 1;
	public static final  int FLD_START_DATE = 2;
	public static final  int FLD_END_DATE = 3;

	public static final  String[] fieldNames = {
		"PERIOD_ID",
		"PERIOD",
		"START_DATE",
		"END_DATE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_DATE
	 }; 

	public PstPeriod(){
	}

	public PstPeriod(int i) throws DBException { 
		super(new PstPeriod()); 
	}

	public PstPeriod(String sOid) throws DBException { 
		super(new PstPeriod(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstPeriod(long lOid) throws DBException { 
		super(new PstPeriod(0)); 
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
		return TBL_HR_PERIOD;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstPeriod().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Period period = fetchExc(ent.getOID()); 
		ent = (Entity)period; 
		return period.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Period) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Period) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Period fetchExc(long oid) throws DBException{ 
		try{ 
			Period period = new Period();
			PstPeriod pstPeriod = new PstPeriod(oid); 
			period.setOID(oid);

			period.setPeriod(pstPeriod.getString(FLD_PERIOD));
			period.setStartDate(pstPeriod.getDate(FLD_START_DATE));
			period.setEndDate(pstPeriod.getDate(FLD_END_DATE));

			return period; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPeriod(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Period period) throws DBException{ 
		try{ 
			PstPeriod pstPeriod = new PstPeriod(0);

			pstPeriod.setString(FLD_PERIOD, period.getPeriod());
			pstPeriod.setDate(FLD_START_DATE, period.getStartDate());
			pstPeriod.setDate(FLD_END_DATE, period.getEndDate());

			pstPeriod.insert(); 
			period.setOID(pstPeriod.getlong(FLD_PERIOD_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPeriod(0),DBException.UNKNOWN); 
		}
		return period.getOID();
	}

	public static long updateExc(Period period) throws DBException{ 
		try{ 
			if(period.getOID() != 0){ 
				PstPeriod pstPeriod = new PstPeriod(period.getOID());

				pstPeriod.setString(FLD_PERIOD, period.getPeriod());
				pstPeriod.setDate(FLD_START_DATE, period.getStartDate());
				pstPeriod.setDate(FLD_END_DATE, period.getEndDate());

				pstPeriod.update(); 
				return period.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPeriod(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstPeriod pstPeriod = new PstPeriod(oid);
			pstPeriod.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPeriod(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_PERIOD; 
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
				Period period = new Period();
				resultToObject(rs, period);
				lists.add(period);
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

	private static void resultToObject(ResultSet rs, Period period){
		try{
			period.setOID(rs.getLong(PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]));
			period.setPeriod(rs.getString(PstPeriod.fieldNames[PstPeriod.FLD_PERIOD]));
			period.setStartDate(rs.getDate(PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]));
			period.setEndDate(rs.getDate(PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long periodId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PERIOD + " WHERE " + 
						PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + " = " + periodId;

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
			String sql = "SELECT COUNT("+ PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + ") FROM " + TBL_HR_PERIOD;
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
			  	   Period period = (Period)list.get(ls);
				   if(oid == period.getOID())
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
    	if(PstEmpSchedule.checkPeriode(oid))
            return true;
    	else
            return false;
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

}
