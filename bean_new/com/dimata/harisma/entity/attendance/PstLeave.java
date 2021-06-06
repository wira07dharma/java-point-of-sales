
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

public class PstLeave extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LEAVE = "HR_LEAVE";

	public static final  int FLD_LEAVE_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_SUBMIT_DATE = 2;
	public static final  int FLD_LEAVE_FROM = 3;
	public static final  int FLD_LEAVE_TO = 4;
	public static final  int FLD_DURATION = 5;
	public static final  int FLD_REASON = 6;
	public static final  int FLD_LONG_LEAVE = 7;
	public static final  int FLD_ANNUAL_LEAVE = 8;
	public static final  int FLD_LEAVE_WO_PAY = 9;
	public static final  int FLD_MATERNITY_LEAVE = 10;
	public static final  int FLD_DAY_OFF = 11;
	public static final  int FLD_PUBLIC_HOLIDAY = 12;
	public static final  int FLD_EXTRA_DAY_OFF = 13;
	public static final  int FLD_SICK_LEAVE = 14;
	public static final  int FLD_PERIOD_AL_FROM = 15;
	public static final  int FLD_PERIOD_AL_TO = 16;
	public static final  int FLD_AL_ENTITLEMENT = 17;
	public static final  int FLD_AL_TAKEN = 18;
	public static final  int FLD_AL_BALANCE = 19;
	public static final  int FLD_PERIOD_LL_FROM = 20;
	public static final  int FLD_PERIOD_LL_TO = 21;
	public static final  int FLD_LL_ENTITLEMENT = 22;
	public static final  int FLD_LL_TAKEN = 23;
	public static final  int FLD_LL_BALANCE = 24;
	public static final  int FLD_APR_SPV_DATE = 25;
	public static final  int FLD_APR_DEPTHEAD_DATE = 26;
	public static final  int FLD_APR_PMGR_DATE = 27;
    public static final  int FLD_LEAVE_TYPE = 28;

	public static final  String[] fieldNames = {
		"LEAVE_ID",
		"EMPLOYEE_ID",
		"SUBMIT_DATE",
		"LEAVE_FROM",
		"LEAVE_TO",
		"DURATION",
		"REASON",
		"LONG_LEAVE",
		"ANNUAL_LEAVE",
		"LEAVE_WO_PAY",
		"MATERNITY_LEAVE",
		"DAY_OFF",
		"PUBLIC_HOLIDAY",
		"EXTRA_DAY_OFF",
		"SICK_LEAVE",
		"PERIOD_AL_FROM",
		"PERIOD_AL_TO",
		"AL_ENTITLEMENT",
		"AL_TAKEN",
		"AL_BALANCE",
		"PERIOD_LL_FROM",
		"PERIOD_LL_TO",
		"LL_ENTITLEMENT",
		"LL_TAKEN",
		"LL_BALANCE",
		"APR_SPV_DATE",
		"APR_DEPTHEAD_DATE",
		"APR_PMGR_DATE",
        "LEAVE_TYPE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_INT,
		TYPE_STRING,
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
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_DATE,
        TYPE_INT
	 }; 


    public static final int LEAVE_TYPE_NONE = 0;
    public static final int LEAVE_TYPE_AL = 1;
    public static final int LEAVE_TYPE_LL = 2;

    public static final String[] leaveTypeStr = {"None", "Anual Leave", "long Leave"};

	public PstLeave(){
	}

	public PstLeave(int i) throws DBException { 
		super(new PstLeave()); 
	}

	public PstLeave(String sOid) throws DBException { 
		super(new PstLeave(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLeave(long lOid) throws DBException { 
		super(new PstLeave(0)); 
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
		return TBL_HR_LEAVE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLeave().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Leave leave = fetchExc(ent.getOID()); 
		ent = (Entity)leave; 
		return leave.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Leave) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Leave) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Leave fetchExc(long oid) throws DBException{ 
		try{ 
			Leave leave = new Leave();
			PstLeave pstLeave = new PstLeave(oid); 
			leave.setOID(oid);

			leave.setEmployeeId(pstLeave.getlong(FLD_EMPLOYEE_ID));
			leave.setSubmitDate(pstLeave.getDate(FLD_SUBMIT_DATE));
			leave.setLeaveFrom(pstLeave.getDate(FLD_LEAVE_FROM));
			leave.setLeaveTo(pstLeave.getDate(FLD_LEAVE_TO));
			leave.setDuration(pstLeave.getInt(FLD_DURATION));
			leave.setReason(pstLeave.getString(FLD_REASON));
			leave.setLongLeave(pstLeave.getInt(FLD_LONG_LEAVE));
			leave.setAnnualLeave(pstLeave.getInt(FLD_ANNUAL_LEAVE));
			leave.setLeaveWoPay(pstLeave.getInt(FLD_LEAVE_WO_PAY));
			leave.setMaternityLeave(pstLeave.getInt(FLD_MATERNITY_LEAVE));
			leave.setDayOff(pstLeave.getInt(FLD_DAY_OFF));
			leave.setPublicHoliday(pstLeave.getInt(FLD_PUBLIC_HOLIDAY));
			leave.setExtraDayOff(pstLeave.getInt(FLD_EXTRA_DAY_OFF));
			leave.setSickLeave(pstLeave.getInt(FLD_SICK_LEAVE));
			leave.setPeriodAlFrom(pstLeave.getInt(FLD_PERIOD_AL_FROM));
			leave.setPeriodAlTo(pstLeave.getInt(FLD_PERIOD_AL_TO));
			leave.setAlEntitlement(pstLeave.getInt(FLD_AL_ENTITLEMENT));
			leave.setAlTaken(pstLeave.getInt(FLD_AL_TAKEN));
			leave.setAlBalance(pstLeave.getInt(FLD_AL_BALANCE));
			leave.setPeriodLlFrom(pstLeave.getInt(FLD_PERIOD_LL_FROM));
			leave.setPeriodLlTo(pstLeave.getInt(FLD_PERIOD_LL_TO));
			leave.setLlEntitlement(pstLeave.getInt(FLD_LL_ENTITLEMENT));
			leave.setLlTaken(pstLeave.getInt(FLD_LL_TAKEN));
			leave.setLlBalance(pstLeave.getInt(FLD_LL_BALANCE));
			leave.setAprSpvDate(pstLeave.getDate(FLD_APR_SPV_DATE));
			leave.setAprDeptheadDate(pstLeave.getDate(FLD_APR_DEPTHEAD_DATE));
			leave.setAprPmgrDate(pstLeave.getDate(FLD_APR_PMGR_DATE));
            leave.setLeaveType(pstLeave.getInt(FLD_LEAVE_TYPE));

			return leave; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeave(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Leave leave) throws DBException{ 
		try{ 
			PstLeave pstLeave = new PstLeave(0);

			pstLeave.setLong(FLD_EMPLOYEE_ID, leave.getEmployeeId());
			pstLeave.setDate(FLD_SUBMIT_DATE, leave.getSubmitDate());
			pstLeave.setDate(FLD_LEAVE_FROM, leave.getLeaveFrom());
			pstLeave.setDate(FLD_LEAVE_TO, leave.getLeaveTo());
			pstLeave.setInt(FLD_DURATION, leave.getDuration());
			pstLeave.setString(FLD_REASON, leave.getReason());
			pstLeave.setInt(FLD_LONG_LEAVE, leave.getLongLeave());
			pstLeave.setInt(FLD_ANNUAL_LEAVE, leave.getAnnualLeave());
			pstLeave.setInt(FLD_LEAVE_WO_PAY, leave.getLeaveWoPay());
			pstLeave.setInt(FLD_MATERNITY_LEAVE, leave.getMaternityLeave());
			pstLeave.setInt(FLD_DAY_OFF, leave.getDayOff());
			pstLeave.setInt(FLD_PUBLIC_HOLIDAY, leave.getPublicHoliday());
			pstLeave.setInt(FLD_EXTRA_DAY_OFF, leave.getExtraDayOff());
			pstLeave.setInt(FLD_SICK_LEAVE, leave.getSickLeave());
			pstLeave.setInt(FLD_PERIOD_AL_FROM, leave.getPeriodAlFrom());
			pstLeave.setInt(FLD_PERIOD_AL_TO, leave.getPeriodAlTo());
			pstLeave.setInt(FLD_AL_ENTITLEMENT, leave.getAlEntitlement());
			pstLeave.setInt(FLD_AL_TAKEN, leave.getAlTaken());
			pstLeave.setInt(FLD_AL_BALANCE, leave.getAlBalance());
			pstLeave.setInt(FLD_PERIOD_LL_FROM, leave.getPeriodLlFrom());
			pstLeave.setInt(FLD_PERIOD_LL_TO, leave.getPeriodLlTo());
			pstLeave.setInt(FLD_LL_ENTITLEMENT, leave.getLlEntitlement());
			pstLeave.setInt(FLD_LL_TAKEN, leave.getLlTaken());
			pstLeave.setInt(FLD_LL_BALANCE, leave.getLlBalance());
			pstLeave.setDate(FLD_APR_SPV_DATE, leave.getAprSpvDate());
			pstLeave.setDate(FLD_APR_DEPTHEAD_DATE, leave.getAprDeptheadDate());
			pstLeave.setDate(FLD_APR_PMGR_DATE, leave.getAprPmgrDate());
            pstLeave.setInt(FLD_LEAVE_TYPE, leave.getLeaveType());

			pstLeave.insert(); 
			leave.setOID(pstLeave.getlong(FLD_LEAVE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeave(0),DBException.UNKNOWN); 
		}
		return leave.getOID();
	}

	public static long updateExc(Leave leave) throws DBException{ 
		try{ 
			if(leave.getOID() != 0){ 
				PstLeave pstLeave = new PstLeave(leave.getOID());

				pstLeave.setLong(FLD_EMPLOYEE_ID, leave.getEmployeeId());
				pstLeave.setDate(FLD_SUBMIT_DATE, leave.getSubmitDate());
				pstLeave.setDate(FLD_LEAVE_FROM, leave.getLeaveFrom());
				pstLeave.setDate(FLD_LEAVE_TO, leave.getLeaveTo());
				pstLeave.setInt(FLD_DURATION, leave.getDuration());
				pstLeave.setString(FLD_REASON, leave.getReason());
				pstLeave.setInt(FLD_LONG_LEAVE, leave.getLongLeave());
				pstLeave.setInt(FLD_ANNUAL_LEAVE, leave.getAnnualLeave());
				pstLeave.setInt(FLD_LEAVE_WO_PAY, leave.getLeaveWoPay());
				pstLeave.setInt(FLD_MATERNITY_LEAVE, leave.getMaternityLeave());
				pstLeave.setInt(FLD_DAY_OFF, leave.getDayOff());
				pstLeave.setInt(FLD_PUBLIC_HOLIDAY, leave.getPublicHoliday());
				pstLeave.setInt(FLD_EXTRA_DAY_OFF, leave.getExtraDayOff());
				pstLeave.setInt(FLD_SICK_LEAVE, leave.getSickLeave());
				pstLeave.setInt(FLD_PERIOD_AL_FROM, leave.getPeriodAlFrom());
				pstLeave.setInt(FLD_PERIOD_AL_TO, leave.getPeriodAlTo());
				pstLeave.setInt(FLD_AL_ENTITLEMENT, leave.getAlEntitlement());
				pstLeave.setInt(FLD_AL_TAKEN, leave.getAlTaken());
				pstLeave.setInt(FLD_AL_BALANCE, leave.getAlBalance());
				pstLeave.setInt(FLD_PERIOD_LL_FROM, leave.getPeriodLlFrom());
				pstLeave.setInt(FLD_PERIOD_LL_TO, leave.getPeriodLlTo());
				pstLeave.setInt(FLD_LL_ENTITLEMENT, leave.getLlEntitlement());
				pstLeave.setInt(FLD_LL_TAKEN, leave.getLlTaken());
				pstLeave.setInt(FLD_LL_BALANCE, leave.getLlBalance());
				pstLeave.setDate(FLD_APR_SPV_DATE, leave.getAprSpvDate());
				pstLeave.setDate(FLD_APR_DEPTHEAD_DATE, leave.getAprDeptheadDate());
				pstLeave.setDate(FLD_APR_PMGR_DATE, leave.getAprPmgrDate());
                pstLeave.setInt(FLD_LEAVE_TYPE, leave.getLeaveType());

				pstLeave.update(); 
				return leave.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeave(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLeave pstLeave = new PstLeave(oid);
			pstLeave.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeave(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_LEAVE; 
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
				Leave leave = new Leave();
				resultToObject(rs, leave);
				lists.add(leave);
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

	private static void resultToObject(ResultSet rs, Leave leave){
		try{
			leave.setOID(rs.getLong(PstLeave.fieldNames[PstLeave.FLD_LEAVE_ID]));
			leave.setEmployeeId(rs.getLong(PstLeave.fieldNames[PstLeave.FLD_EMPLOYEE_ID]));
			leave.setSubmitDate(rs.getDate(PstLeave.fieldNames[PstLeave.FLD_SUBMIT_DATE]));
			leave.setLeaveFrom(rs.getDate(PstLeave.fieldNames[PstLeave.FLD_LEAVE_FROM]));
			leave.setLeaveTo(rs.getDate(PstLeave.fieldNames[PstLeave.FLD_LEAVE_TO]));
			leave.setDuration(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_DURATION]));
			leave.setReason(rs.getString(PstLeave.fieldNames[PstLeave.FLD_REASON]));
			leave.setLongLeave(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_LONG_LEAVE]));
			leave.setAnnualLeave(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_ANNUAL_LEAVE]));
			leave.setLeaveWoPay(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_LEAVE_WO_PAY]));
			leave.setMaternityLeave(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_MATERNITY_LEAVE]));
			leave.setDayOff(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_DAY_OFF]));
			leave.setPublicHoliday(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_PUBLIC_HOLIDAY]));
			leave.setExtraDayOff(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_EXTRA_DAY_OFF]));
			leave.setSickLeave(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_SICK_LEAVE]));
			leave.setPeriodAlFrom(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_PERIOD_AL_FROM]));
			leave.setPeriodAlTo(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_PERIOD_AL_TO]));
			leave.setAlEntitlement(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_AL_ENTITLEMENT]));
			leave.setAlTaken(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_AL_TAKEN]));
			leave.setAlBalance(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_AL_BALANCE]));
			leave.setPeriodLlFrom(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_PERIOD_LL_FROM]));
			leave.setPeriodLlTo(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_PERIOD_LL_TO]));
			leave.setLlEntitlement(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_LL_ENTITLEMENT]));
			leave.setLlTaken(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_LL_TAKEN]));
			leave.setLlBalance(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_LL_BALANCE]));
			leave.setAprSpvDate(rs.getDate(PstLeave.fieldNames[PstLeave.FLD_APR_SPV_DATE]));
			leave.setAprDeptheadDate(rs.getDate(PstLeave.fieldNames[PstLeave.FLD_APR_DEPTHEAD_DATE]));
			leave.setAprPmgrDate(rs.getDate(PstLeave.fieldNames[PstLeave.FLD_APR_PMGR_DATE]));
            leave.setLeaveType(rs.getInt(PstLeave.fieldNames[PstLeave.FLD_LEAVE_TYPE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long leaveId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LEAVE + " WHERE " + 
						PstLeave.fieldNames[PstLeave.FLD_LEAVE_ID] + " = " + leaveId;

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
			String sql = "SELECT COUNT("+ PstLeave.fieldNames[PstLeave.FLD_LEAVE_ID] + ") FROM " + TBL_HR_LEAVE;
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
			  	   Leave leave = (Leave)list.get(ls);
				   if(oid == leave.getOID())
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
            String sql = " DELETE FROM "+PstLeave.TBL_HR_LEAVE +
                 " WHERE " + PstLeave.fieldNames[PstLeave.FLD_EMPLOYEE_ID] +
                 " = " + emplOID;
            int status = DBHandler.execUpdate(sql);
    	}
        catch(Exception exc){
        	System.out.println("error delete leave by employee "+exc.toString());
    	}
    	return emplOID;
    }

}
