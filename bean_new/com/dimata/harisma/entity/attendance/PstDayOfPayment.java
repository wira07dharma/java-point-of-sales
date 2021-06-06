
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

public class PstDayOfPayment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_DAY_OF_PAYMENT = "HR_DAY_OF_PAYMENT";

	public static final  int FLD_DAY_OF_PAYMENT_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_DURATION = 2;
	public static final  int FLD_DP_FROM = 3;
	public static final  int FLD_DP_TO = 4;
	public static final  int FLD_APR_DEPTHEAD_DATE = 5;
	public static final  int FLD_CONTACT_ADDRESS = 6;
	public static final  int FLD_REMARKS = 7;

	public static final  String[] fieldNames = {
		"DAY_OF_PAYMENT_ID",
		"EMPLOYEE_ID",
		"DURATION",
		"DP_FROM",
		"DP_TO",
		"APR_DEPTHEAD_DATE",
		"CONTACT_ADDRESS",
		"REMARKS"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_INT,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstDayOfPayment(){
	}

	public PstDayOfPayment(int i) throws DBException { 
		super(new PstDayOfPayment()); 
	}

	public PstDayOfPayment(String sOid) throws DBException { 
		super(new PstDayOfPayment(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDayOfPayment(long lOid) throws DBException { 
		super(new PstDayOfPayment(0)); 
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
		return TBL_HR_DAY_OF_PAYMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDayOfPayment().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DayOfPayment dayofpayment = fetchExc(ent.getOID()); 
		ent = (Entity)dayofpayment; 
		return dayofpayment.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DayOfPayment) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DayOfPayment) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DayOfPayment fetchExc(long oid) throws DBException{ 
		try{ 
			DayOfPayment dayofpayment = new DayOfPayment();
			PstDayOfPayment pstDayOfPayment = new PstDayOfPayment(oid); 
			dayofpayment.setOID(oid);

			dayofpayment.setEmployeeId(pstDayOfPayment.getlong(FLD_EMPLOYEE_ID));
			dayofpayment.setDuration(pstDayOfPayment.getInt(FLD_DURATION));
			dayofpayment.setDpFrom(pstDayOfPayment.getDate(FLD_DP_FROM));
			dayofpayment.setDpTo(pstDayOfPayment.getDate(FLD_DP_TO));
			dayofpayment.setAprDeptheadDate(pstDayOfPayment.getDate(FLD_APR_DEPTHEAD_DATE));
			dayofpayment.setContactAddress(pstDayOfPayment.getString(FLD_CONTACT_ADDRESS));
			dayofpayment.setRemarks(pstDayOfPayment.getString(FLD_REMARKS));

			return dayofpayment; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDayOfPayment(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DayOfPayment dayofpayment) throws DBException{ 
		try{ 
			PstDayOfPayment pstDayOfPayment = new PstDayOfPayment(0);

			pstDayOfPayment.setLong(FLD_EMPLOYEE_ID, dayofpayment.getEmployeeId());
			pstDayOfPayment.setInt(FLD_DURATION, dayofpayment.getDuration());
			pstDayOfPayment.setDate(FLD_DP_FROM, dayofpayment.getDpFrom());
			pstDayOfPayment.setDate(FLD_DP_TO, dayofpayment.getDpTo());
			pstDayOfPayment.setDate(FLD_APR_DEPTHEAD_DATE, dayofpayment.getAprDeptheadDate());
			pstDayOfPayment.setString(FLD_CONTACT_ADDRESS, dayofpayment.getContactAddress());
			pstDayOfPayment.setString(FLD_REMARKS, dayofpayment.getRemarks());

			pstDayOfPayment.insert(); 
			dayofpayment.setOID(pstDayOfPayment.getlong(FLD_DAY_OF_PAYMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDayOfPayment(0),DBException.UNKNOWN); 
		}
		return dayofpayment.getOID();
	}

	public static long updateExc(DayOfPayment dayofpayment) throws DBException{ 
		try{ 
			if(dayofpayment.getOID() != 0){ 
				PstDayOfPayment pstDayOfPayment = new PstDayOfPayment(dayofpayment.getOID());

				pstDayOfPayment.setLong(FLD_EMPLOYEE_ID, dayofpayment.getEmployeeId());
				pstDayOfPayment.setInt(FLD_DURATION, dayofpayment.getDuration());
				pstDayOfPayment.setDate(FLD_DP_FROM, dayofpayment.getDpFrom());
				pstDayOfPayment.setDate(FLD_DP_TO, dayofpayment.getDpTo());
				pstDayOfPayment.setDate(FLD_APR_DEPTHEAD_DATE, dayofpayment.getAprDeptheadDate());
				pstDayOfPayment.setString(FLD_CONTACT_ADDRESS, dayofpayment.getContactAddress());
				pstDayOfPayment.setString(FLD_REMARKS, dayofpayment.getRemarks());

				pstDayOfPayment.update(); 
				return dayofpayment.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDayOfPayment(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDayOfPayment pstDayOfPayment = new PstDayOfPayment(oid);
			pstDayOfPayment.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDayOfPayment(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_DAY_OF_PAYMENT; 
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
				DayOfPayment dayofpayment = new DayOfPayment();
				resultToObject(rs, dayofpayment);
				lists.add(dayofpayment);
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

	private static void resultToObject(ResultSet rs, DayOfPayment dayofpayment){
		try{
			dayofpayment.setOID(rs.getLong(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_DAY_OF_PAYMENT_ID]));
			dayofpayment.setEmployeeId(rs.getLong(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_EMPLOYEE_ID]));
			dayofpayment.setDuration(rs.getInt(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_DURATION]));
			dayofpayment.setDpFrom(rs.getDate(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_DP_FROM]));
			dayofpayment.setDpTo(rs.getDate(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_DP_TO]));
			dayofpayment.setAprDeptheadDate(rs.getDate(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_APR_DEPTHEAD_DATE]));
			dayofpayment.setContactAddress(rs.getString(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_CONTACT_ADDRESS]));
			dayofpayment.setRemarks(rs.getString(PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_REMARKS]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long dayOfPaymentId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DAY_OF_PAYMENT + " WHERE " + 
						PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_DAY_OF_PAYMENT_ID] + " = " + dayOfPaymentId;

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
			String sql = "SELECT COUNT("+ PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_DAY_OF_PAYMENT_ID] + ") FROM " + TBL_HR_DAY_OF_PAYMENT;
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
			  	   DayOfPayment dayofpayment = (DayOfPayment)list.get(ls);
				   if(oid == dayofpayment.getOID())
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
            String sql = " DELETE FROM "+PstDayOfPayment.TBL_HR_DAY_OF_PAYMENT +
                 " WHERE " + PstDayOfPayment.fieldNames[PstDayOfPayment.FLD_EMPLOYEE_ID] +
                 " = " + emplOID;
            int status = DBHandler.execUpdate(sql);
    	}
        catch(Exception exc){
        	System.out.println("error delete day of payment by employee "+exc.toString());
    	}
    	return emplOID;
    }

}
