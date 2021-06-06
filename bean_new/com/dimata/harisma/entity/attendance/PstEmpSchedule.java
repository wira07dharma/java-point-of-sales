
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

/* package harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.attendance.*; 

public class PstEmpSchedule extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_EMP_SCHEDULE = "HR_EMP_SCHEDULE";

	public static final  int FLD_EMP_SCHEDULE_ID = 0;
	public static final  int FLD_PERIOD_ID = 1;
	public static final  int FLD_EMPLOYEE_ID = 2;
	public static final  int FLD_D1 = 3;
	public static final  int FLD_D2 = 4;
	public static final  int FLD_D3 = 5;
	public static final  int FLD_D4 = 6;
	public static final  int FLD_D5 = 7;
	public static final  int FLD_D6 = 8;
	public static final  int FLD_D7 = 9;
	public static final  int FLD_D8 = 10;
	public static final  int FLD_D9 = 11;
	public static final  int FLD_D10 = 12;
	public static final  int FLD_D11 = 13;
	public static final  int FLD_D12 = 14;
	public static final  int FLD_D13 = 15;
	public static final  int FLD_D14 = 16;
	public static final  int FLD_D15 = 17;
	public static final  int FLD_D16 = 18;
	public static final  int FLD_D17 = 19;
	public static final  int FLD_D18 = 20;
	public static final  int FLD_D19 = 21;
	public static final  int FLD_D20 = 22;
	public static final  int FLD_D21 = 23;
	public static final  int FLD_D22 = 24;
	public static final  int FLD_D23 = 25;
	public static final  int FLD_D24 = 26;
	public static final  int FLD_D25 = 27;
	public static final  int FLD_D26 = 28;
	public static final  int FLD_D27 = 29;
	public static final  int FLD_D28 = 30;
	public static final  int FLD_D29 = 31;
	public static final  int FLD_D30 = 32;
	public static final  int FLD_D31 = 33;
	public static final  int FLD_D2ND1 = 34;
	public static final  int FLD_D2ND2 = 35;
	public static final  int FLD_D2ND3 = 36;
	public static final  int FLD_D2ND4 = 37;
	public static final  int FLD_D2ND5 = 38;
	public static final  int FLD_D2ND6 = 39;
	public static final  int FLD_D2ND7 = 40;
	public static final  int FLD_D2ND8 = 41;
	public static final  int FLD_D2ND9 = 42;
	public static final  int FLD_D2ND10 = 43;
	public static final  int FLD_D2ND11 = 44;
	public static final  int FLD_D2ND12 = 45;
	public static final  int FLD_D2ND13 = 46;
	public static final  int FLD_D2ND14 = 47;
	public static final  int FLD_D2ND15 = 48;
	public static final  int FLD_D2ND16 = 49;
	public static final  int FLD_D2ND17 = 50;
	public static final  int FLD_D2ND18 = 51;
	public static final  int FLD_D2ND19 = 52;
	public static final  int FLD_D2ND20 = 53;
	public static final  int FLD_D2ND21 = 54;
	public static final  int FLD_D2ND22 = 55;
	public static final  int FLD_D2ND23 = 56;
	public static final  int FLD_D2ND24 = 57;
	public static final  int FLD_D2ND25 = 58;
	public static final  int FLD_D2ND26 = 59;
	public static final  int FLD_D2ND27 = 60;
	public static final  int FLD_D2ND28 = 61;
	public static final  int FLD_D2ND29 = 62;
	public static final  int FLD_D2ND30 = 63;
	public static final  int FLD_D2ND31 = 64;

	public static final  String[] fieldNames = {
		"EMP_SCHEDULE_ID","PERIOD_ID","EMPLOYEE_ID",
		"D1","D2","D3","D4","D5","D6","D7","D8","D9","D10",
		"D11","D12","D13","D14","D15","D16","D17","D18","D19","D20",
		"D21","D22","D23","D24","D25","D26","D27","D28","D29","D30","D31",
		"D2ND1","D2ND2","D2ND3","D2ND4","D2ND5","D2ND6","D2ND7","D2ND8","D2ND9","D2ND10",
		"D2ND11","D2ND12","D2ND13","D2ND14","D2ND15","D2ND16","D2ND17","D2ND18","D2ND19","D2ND20",
		"D2ND21","D2ND22","D2ND23","D2ND24","D2ND25","D2ND26","D2ND27","D2ND28","D2ND29","D2ND30","D2ND31"                
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
		TYPE_LONG                
	 };

 	public static final String FLD_SCHEDULE = "D";

	public PstEmpSchedule(){
	}

	public PstEmpSchedule(int i) throws DBException { 
		super(new PstEmpSchedule()); 
	}

	public PstEmpSchedule(String sOid) throws DBException { 
		super(new PstEmpSchedule(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstEmpSchedule(long lOid) throws DBException { 
		super(new PstEmpSchedule(0)); 
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
		return TBL_HR_EMP_SCHEDULE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstEmpSchedule().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		EmpSchedule empschedule = fetchExc(ent.getOID()); 
		ent = (Entity)empschedule; 
		return empschedule.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((EmpSchedule) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((EmpSchedule) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static EmpSchedule fetchExc(long oid) throws DBException{ 
		try{ 
			EmpSchedule empschedule = new EmpSchedule();
			PstEmpSchedule pstEmpSchedule = new PstEmpSchedule(oid); 
			empschedule.setOID(oid);

			empschedule.setPeriodId(pstEmpSchedule.getlong(FLD_PERIOD_ID));
			empschedule.setEmployeeId(pstEmpSchedule.getlong(FLD_EMPLOYEE_ID));
			empschedule.setD1(pstEmpSchedule.getlong(FLD_D1));
			empschedule.setD2(pstEmpSchedule.getlong(FLD_D2));
			empschedule.setD3(pstEmpSchedule.getlong(FLD_D3));
			empschedule.setD4(pstEmpSchedule.getlong(FLD_D4));
			empschedule.setD5(pstEmpSchedule.getlong(FLD_D5));
			empschedule.setD6(pstEmpSchedule.getlong(FLD_D6));
			empschedule.setD7(pstEmpSchedule.getlong(FLD_D7));
			empschedule.setD8(pstEmpSchedule.getlong(FLD_D8));
			empschedule.setD9(pstEmpSchedule.getlong(FLD_D9));
			empschedule.setD10(pstEmpSchedule.getlong(FLD_D10));
			empschedule.setD11(pstEmpSchedule.getlong(FLD_D11));
			empschedule.setD12(pstEmpSchedule.getlong(FLD_D12));
			empschedule.setD13(pstEmpSchedule.getlong(FLD_D13));
			empschedule.setD14(pstEmpSchedule.getlong(FLD_D14));
			empschedule.setD15(pstEmpSchedule.getlong(FLD_D15));
			empschedule.setD16(pstEmpSchedule.getlong(FLD_D16));
			empschedule.setD17(pstEmpSchedule.getlong(FLD_D17));
			empschedule.setD18(pstEmpSchedule.getlong(FLD_D18));
			empschedule.setD19(pstEmpSchedule.getlong(FLD_D19));
			empschedule.setD20(pstEmpSchedule.getlong(FLD_D20));
			empschedule.setD21(pstEmpSchedule.getlong(FLD_D21));
			empschedule.setD22(pstEmpSchedule.getlong(FLD_D22));
			empschedule.setD23(pstEmpSchedule.getlong(FLD_D23));
			empschedule.setD24(pstEmpSchedule.getlong(FLD_D24));
			empschedule.setD25(pstEmpSchedule.getlong(FLD_D25));
			empschedule.setD26(pstEmpSchedule.getlong(FLD_D26));
			empschedule.setD27(pstEmpSchedule.getlong(FLD_D27));
			empschedule.setD28(pstEmpSchedule.getlong(FLD_D28));
			empschedule.setD29(pstEmpSchedule.getlong(FLD_D29));
			empschedule.setD30(pstEmpSchedule.getlong(FLD_D30));
			empschedule.setD31(pstEmpSchedule.getlong(FLD_D31));

			empschedule.setD2nd1(pstEmpSchedule.getlong(FLD_D2ND1));
			empschedule.setD2nd2(pstEmpSchedule.getlong(FLD_D2ND2));
			empschedule.setD2nd3(pstEmpSchedule.getlong(FLD_D2ND3));
			empschedule.setD2nd4(pstEmpSchedule.getlong(FLD_D2ND4));
			empschedule.setD2nd5(pstEmpSchedule.getlong(FLD_D2ND5));
			empschedule.setD2nd6(pstEmpSchedule.getlong(FLD_D2ND6));
			empschedule.setD2nd7(pstEmpSchedule.getlong(FLD_D2ND7));
			empschedule.setD2nd8(pstEmpSchedule.getlong(FLD_D2ND8));
			empschedule.setD2nd9(pstEmpSchedule.getlong(FLD_D2ND9));
			empschedule.setD2nd10(pstEmpSchedule.getlong(FLD_D2ND10));
			empschedule.setD2nd11(pstEmpSchedule.getlong(FLD_D2ND11));
			empschedule.setD2nd12(pstEmpSchedule.getlong(FLD_D2ND12));
			empschedule.setD2nd13(pstEmpSchedule.getlong(FLD_D2ND13));
			empschedule.setD2nd14(pstEmpSchedule.getlong(FLD_D2ND14));
			empschedule.setD2nd15(pstEmpSchedule.getlong(FLD_D2ND15));
			empschedule.setD2nd16(pstEmpSchedule.getlong(FLD_D2ND16));
			empschedule.setD2nd17(pstEmpSchedule.getlong(FLD_D2ND17));
			empschedule.setD2nd18(pstEmpSchedule.getlong(FLD_D2ND18));
			empschedule.setD2nd19(pstEmpSchedule.getlong(FLD_D2ND19));
			empschedule.setD2nd20(pstEmpSchedule.getlong(FLD_D2ND20));
			empschedule.setD2nd21(pstEmpSchedule.getlong(FLD_D2ND21));
			empschedule.setD2nd22(pstEmpSchedule.getlong(FLD_D2ND22));
			empschedule.setD2nd23(pstEmpSchedule.getlong(FLD_D2ND23));
			empschedule.setD2nd24(pstEmpSchedule.getlong(FLD_D2ND24));
			empschedule.setD2nd25(pstEmpSchedule.getlong(FLD_D2ND25));
			empschedule.setD2nd26(pstEmpSchedule.getlong(FLD_D2ND26));
			empschedule.setD2nd27(pstEmpSchedule.getlong(FLD_D2ND27));
			empschedule.setD2nd28(pstEmpSchedule.getlong(FLD_D2ND28));
			empschedule.setD2nd29(pstEmpSchedule.getlong(FLD_D2ND29));
			empschedule.setD2nd30(pstEmpSchedule.getlong(FLD_D2ND30));
			empschedule.setD2nd31(pstEmpSchedule.getlong(FLD_D2ND31));
                        
			return empschedule; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSchedule(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(EmpSchedule empschedule) throws DBException{ 
		try{ 
			PstEmpSchedule pstEmpSchedule = new PstEmpSchedule(0);

			pstEmpSchedule.setLong(FLD_PERIOD_ID, empschedule.getPeriodId());
			pstEmpSchedule.setLong(FLD_EMPLOYEE_ID, empschedule.getEmployeeId());
			pstEmpSchedule.setLong(FLD_D1, empschedule.getD1());
			pstEmpSchedule.setLong(FLD_D2, empschedule.getD2());
			pstEmpSchedule.setLong(FLD_D3, empschedule.getD3());
			pstEmpSchedule.setLong(FLD_D4, empschedule.getD4());
			pstEmpSchedule.setLong(FLD_D5, empschedule.getD5());
			pstEmpSchedule.setLong(FLD_D6, empschedule.getD6());
			pstEmpSchedule.setLong(FLD_D7, empschedule.getD7());
			pstEmpSchedule.setLong(FLD_D8, empschedule.getD8());
			pstEmpSchedule.setLong(FLD_D9, empschedule.getD9());
			pstEmpSchedule.setLong(FLD_D10, empschedule.getD10());
			pstEmpSchedule.setLong(FLD_D11, empschedule.getD11());
			pstEmpSchedule.setLong(FLD_D12, empschedule.getD12());
			pstEmpSchedule.setLong(FLD_D13, empschedule.getD13());
			pstEmpSchedule.setLong(FLD_D14, empschedule.getD14());
			pstEmpSchedule.setLong(FLD_D15, empschedule.getD15());
			pstEmpSchedule.setLong(FLD_D16, empschedule.getD16());
			pstEmpSchedule.setLong(FLD_D17, empschedule.getD17());
			pstEmpSchedule.setLong(FLD_D18, empschedule.getD18());
			pstEmpSchedule.setLong(FLD_D19, empschedule.getD19());
			pstEmpSchedule.setLong(FLD_D20, empschedule.getD20());
			pstEmpSchedule.setLong(FLD_D21, empschedule.getD21());
			pstEmpSchedule.setLong(FLD_D22, empschedule.getD22());
			pstEmpSchedule.setLong(FLD_D23, empschedule.getD23());
			pstEmpSchedule.setLong(FLD_D24, empschedule.getD24());
			pstEmpSchedule.setLong(FLD_D25, empschedule.getD25());
			pstEmpSchedule.setLong(FLD_D26, empschedule.getD26());
			pstEmpSchedule.setLong(FLD_D27, empschedule.getD27());
			pstEmpSchedule.setLong(FLD_D28, empschedule.getD28());
			pstEmpSchedule.setLong(FLD_D29, empschedule.getD29());
			pstEmpSchedule.setLong(FLD_D30, empschedule.getD30());
			pstEmpSchedule.setLong(FLD_D31, empschedule.getD31());

			pstEmpSchedule.setLong(FLD_D2ND1, empschedule.getD2nd1());
			pstEmpSchedule.setLong(FLD_D2ND2, empschedule.getD2nd2());
			pstEmpSchedule.setLong(FLD_D2ND3, empschedule.getD2nd3());
			pstEmpSchedule.setLong(FLD_D2ND4, empschedule.getD2nd4());
			pstEmpSchedule.setLong(FLD_D2ND5, empschedule.getD2nd5());
			pstEmpSchedule.setLong(FLD_D2ND6, empschedule.getD2nd6());
			pstEmpSchedule.setLong(FLD_D2ND7, empschedule.getD2nd7());
			pstEmpSchedule.setLong(FLD_D2ND8, empschedule.getD2nd8());
			pstEmpSchedule.setLong(FLD_D2ND9, empschedule.getD2nd9());
			pstEmpSchedule.setLong(FLD_D2ND10, empschedule.getD2nd10());
			pstEmpSchedule.setLong(FLD_D2ND11, empschedule.getD2nd11());
			pstEmpSchedule.setLong(FLD_D2ND12, empschedule.getD2nd12());
			pstEmpSchedule.setLong(FLD_D2ND13, empschedule.getD2nd13());
			pstEmpSchedule.setLong(FLD_D2ND14, empschedule.getD2nd14());
			pstEmpSchedule.setLong(FLD_D2ND15, empschedule.getD2nd15());
			pstEmpSchedule.setLong(FLD_D2ND16, empschedule.getD2nd16());
			pstEmpSchedule.setLong(FLD_D2ND17, empschedule.getD2nd17());
			pstEmpSchedule.setLong(FLD_D2ND18, empschedule.getD2nd18());
			pstEmpSchedule.setLong(FLD_D2ND19, empschedule.getD2nd19());
			pstEmpSchedule.setLong(FLD_D2ND20, empschedule.getD2nd20());
			pstEmpSchedule.setLong(FLD_D2ND21, empschedule.getD2nd21());
			pstEmpSchedule.setLong(FLD_D2ND22, empschedule.getD2nd22());
			pstEmpSchedule.setLong(FLD_D2ND23, empschedule.getD2nd23());
			pstEmpSchedule.setLong(FLD_D2ND24, empschedule.getD2nd24());
			pstEmpSchedule.setLong(FLD_D2ND25, empschedule.getD2nd25());
			pstEmpSchedule.setLong(FLD_D2ND26, empschedule.getD2nd26());
			pstEmpSchedule.setLong(FLD_D2ND27, empschedule.getD2nd27());
			pstEmpSchedule.setLong(FLD_D2ND28, empschedule.getD2nd28());
			pstEmpSchedule.setLong(FLD_D2ND29, empschedule.getD2nd29());
			pstEmpSchedule.setLong(FLD_D2ND30, empschedule.getD2nd30());
			pstEmpSchedule.setLong(FLD_D2ND31, empschedule.getD2nd31());

                        pstEmpSchedule.insert(); 
			empschedule.setOID(pstEmpSchedule.getlong(FLD_EMP_SCHEDULE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSchedule(0),DBException.UNKNOWN); 
		}
		return empschedule.getOID();
	}

	public static long updateExc(EmpSchedule empschedule) throws DBException{ 
		try{ 
			if(empschedule.getOID() != 0){ 
				PstEmpSchedule pstEmpSchedule = new PstEmpSchedule(empschedule.getOID());

				pstEmpSchedule.setLong(FLD_PERIOD_ID, empschedule.getPeriodId());
				pstEmpSchedule.setLong(FLD_EMPLOYEE_ID, empschedule.getEmployeeId());
				pstEmpSchedule.setLong(FLD_D1, empschedule.getD1());
				pstEmpSchedule.setLong(FLD_D2, empschedule.getD2());
				pstEmpSchedule.setLong(FLD_D3, empschedule.getD3());
				pstEmpSchedule.setLong(FLD_D4, empschedule.getD4());
				pstEmpSchedule.setLong(FLD_D5, empschedule.getD5());
				pstEmpSchedule.setLong(FLD_D6, empschedule.getD6());
				pstEmpSchedule.setLong(FLD_D7, empschedule.getD7());
				pstEmpSchedule.setLong(FLD_D8, empschedule.getD8());
				pstEmpSchedule.setLong(FLD_D9, empschedule.getD9());
				pstEmpSchedule.setLong(FLD_D10, empschedule.getD10());
				pstEmpSchedule.setLong(FLD_D11, empschedule.getD11());
				pstEmpSchedule.setLong(FLD_D12, empschedule.getD12());
				pstEmpSchedule.setLong(FLD_D13, empschedule.getD13());
				pstEmpSchedule.setLong(FLD_D14, empschedule.getD14());
				pstEmpSchedule.setLong(FLD_D15, empschedule.getD15());
				pstEmpSchedule.setLong(FLD_D16, empschedule.getD16());
				pstEmpSchedule.setLong(FLD_D17, empschedule.getD17());
				pstEmpSchedule.setLong(FLD_D18, empschedule.getD18());
				pstEmpSchedule.setLong(FLD_D19, empschedule.getD19());
				pstEmpSchedule.setLong(FLD_D20, empschedule.getD20());
				pstEmpSchedule.setLong(FLD_D21, empschedule.getD21());
				pstEmpSchedule.setLong(FLD_D22, empschedule.getD22());
				pstEmpSchedule.setLong(FLD_D23, empschedule.getD23());
				pstEmpSchedule.setLong(FLD_D24, empschedule.getD24());
				pstEmpSchedule.setLong(FLD_D25, empschedule.getD25());
				pstEmpSchedule.setLong(FLD_D26, empschedule.getD26());
				pstEmpSchedule.setLong(FLD_D27, empschedule.getD27());
				pstEmpSchedule.setLong(FLD_D28, empschedule.getD28());
				pstEmpSchedule.setLong(FLD_D29, empschedule.getD29());
				pstEmpSchedule.setLong(FLD_D30, empschedule.getD30());
				pstEmpSchedule.setLong(FLD_D31, empschedule.getD31());

                                pstEmpSchedule.setLong(FLD_D2ND1, empschedule.getD2nd1());
                                pstEmpSchedule.setLong(FLD_D2ND2, empschedule.getD2nd2());
                                pstEmpSchedule.setLong(FLD_D2ND3, empschedule.getD2nd3());
                                pstEmpSchedule.setLong(FLD_D2ND4, empschedule.getD2nd4());
                                pstEmpSchedule.setLong(FLD_D2ND5, empschedule.getD2nd5());
                                pstEmpSchedule.setLong(FLD_D2ND6, empschedule.getD2nd6());
                                pstEmpSchedule.setLong(FLD_D2ND7, empschedule.getD2nd7());
                                pstEmpSchedule.setLong(FLD_D2ND8, empschedule.getD2nd8());
                                pstEmpSchedule.setLong(FLD_D2ND9, empschedule.getD2nd9());
                                pstEmpSchedule.setLong(FLD_D2ND10, empschedule.getD2nd10());
                                pstEmpSchedule.setLong(FLD_D2ND11, empschedule.getD2nd11());
                                pstEmpSchedule.setLong(FLD_D2ND12, empschedule.getD2nd12());
                                pstEmpSchedule.setLong(FLD_D2ND13, empschedule.getD2nd13());
                                pstEmpSchedule.setLong(FLD_D2ND14, empschedule.getD2nd14());
                                pstEmpSchedule.setLong(FLD_D2ND15, empschedule.getD2nd15());
                                pstEmpSchedule.setLong(FLD_D2ND16, empschedule.getD2nd16());
                                pstEmpSchedule.setLong(FLD_D2ND17, empschedule.getD2nd17());
                                pstEmpSchedule.setLong(FLD_D2ND18, empschedule.getD2nd18());
                                pstEmpSchedule.setLong(FLD_D2ND19, empschedule.getD2nd19());
                                pstEmpSchedule.setLong(FLD_D2ND20, empschedule.getD2nd20());
                                pstEmpSchedule.setLong(FLD_D2ND21, empschedule.getD2nd21());
                                pstEmpSchedule.setLong(FLD_D2ND22, empschedule.getD2nd22());
                                pstEmpSchedule.setLong(FLD_D2ND23, empschedule.getD2nd23());
                                pstEmpSchedule.setLong(FLD_D2ND24, empschedule.getD2nd24());
                                pstEmpSchedule.setLong(FLD_D2ND25, empschedule.getD2nd25());
                                pstEmpSchedule.setLong(FLD_D2ND26, empschedule.getD2nd26());
                                pstEmpSchedule.setLong(FLD_D2ND27, empschedule.getD2nd27());
                                pstEmpSchedule.setLong(FLD_D2ND28, empschedule.getD2nd28());
                                pstEmpSchedule.setLong(FLD_D2ND29, empschedule.getD2nd29());
                                pstEmpSchedule.setLong(FLD_D2ND30, empschedule.getD2nd30());
                                pstEmpSchedule.setLong(FLD_D2ND31, empschedule.getD2nd31());

                                pstEmpSchedule.update(); 
				return empschedule.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSchedule(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstEmpSchedule pstEmpSchedule = new PstEmpSchedule(oid);
			pstEmpSchedule.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpSchedule(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 0, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_EMP_SCHEDULE; 
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
				EmpSchedule empschedule = new EmpSchedule();
				resultToObject(rs, empschedule);
				lists.add(empschedule);
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

	private static void resultToObject(ResultSet rs, EmpSchedule empschedule){
		try{
			empschedule.setOID(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID]));
			empschedule.setPeriodId(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]));
			empschedule.setEmployeeId(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]));
			empschedule.setD1(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D1]));
			empschedule.setD2(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2]));
			empschedule.setD3(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D3]));
			empschedule.setD4(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D4]));
			empschedule.setD5(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D5]));
			empschedule.setD6(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D6]));
			empschedule.setD7(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D7]));
			empschedule.setD8(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D8]));
			empschedule.setD9(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D9]));
			empschedule.setD10(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D10]));
			empschedule.setD11(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D11]));
			empschedule.setD12(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D12]));
			empschedule.setD13(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D13]));
			empschedule.setD14(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D14]));
			empschedule.setD15(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D15]));
			empschedule.setD16(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D16]));
			empschedule.setD17(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D17]));
			empschedule.setD18(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D18]));
			empschedule.setD19(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D19]));
			empschedule.setD20(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D20]));
			empschedule.setD21(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D21]));
			empschedule.setD22(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D22]));
			empschedule.setD23(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D23]));
			empschedule.setD24(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D24]));
			empschedule.setD25(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D25]));
			empschedule.setD26(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D26]));
			empschedule.setD27(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D27]));
			empschedule.setD28(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D28]));
			empschedule.setD29(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D29]));
			empschedule.setD30(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D30]));
			empschedule.setD31(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D31]));

			empschedule.setD2nd1(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND1]));
			empschedule.setD2nd2(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND2]));
			empschedule.setD2nd3(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND3]));
			empschedule.setD2nd4(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND4]));
			empschedule.setD2nd5(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND5]));
			empschedule.setD2nd6(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND6]));
			empschedule.setD2nd7(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND7]));
			empschedule.setD2nd8(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND8]));
			empschedule.setD2nd9(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND9]));
			empschedule.setD2nd10(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND10]));
			empschedule.setD2nd11(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND11]));
			empschedule.setD2nd12(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND12]));
			empschedule.setD2nd13(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND13]));
			empschedule.setD2nd14(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND14]));
			empschedule.setD2nd15(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND15]));
			empschedule.setD2nd16(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND16]));
			empschedule.setD2nd17(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND17]));
			empschedule.setD2nd18(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND18]));
			empschedule.setD2nd19(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND19]));
			empschedule.setD2nd20(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND20]));
			empschedule.setD2nd21(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND21]));
			empschedule.setD2nd22(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND22]));
			empschedule.setD2nd23(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND23]));
			empschedule.setD2nd24(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND24]));
			empschedule.setD2nd25(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND25]));
			empschedule.setD2nd26(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND26]));
			empschedule.setD2nd27(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND27]));
			empschedule.setD2nd28(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND28]));
			empschedule.setD2nd29(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND29]));
			empschedule.setD2nd30(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND30]));
			empschedule.setD2nd31(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2ND31]));

                }catch(Exception e){ }
	}

	public static boolean checkOID(long empScheduleId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EMP_SCHEDULE + " WHERE " + 
						PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID] + " = '" + empScheduleId +"'";

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
			String sql = "SELECT COUNT("+ PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID] + ") FROM " + TBL_HR_EMP_SCHEDULE;
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
			  	   EmpSchedule empschedule = (EmpSchedule)list.get(ls);
				   if(oid == empschedule.getOID())
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
            String sql = " DELETE FROM "+PstEmpSchedule.TBL_HR_EMP_SCHEDULE +
                 " WHERE " + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] +
                 " = " + emplOID;
            int status = DBHandler.execUpdate(sql);
    	}
        catch(Exception exc){
        	System.out.println("error delete empschedule by employee "+exc.toString());
    	}
    	return emplOID;
    }

    public static boolean checkPeriode(long periodeId){
            DBResultSet dbrs = null;
            boolean result = false;
            try{
                    String sql = "SELECT * FROM " + TBL_HR_EMP_SCHEDULE + " WHERE " + 
                                            PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] + " = '" + periodeId +"'";

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

    public static boolean checkScheduleSymbol(long scheduleSymbolId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_EMP_SCHEDULE + " WHERE " + 
                               PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D1] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D2] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D3] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D4] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D5] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D6] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D7] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D8] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D9] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D10] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D11] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D12] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D13] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D14] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D15] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D16] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D17] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D18] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D19] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D20] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D21] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D22] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D23] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D24] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D25] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D26] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D27] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D28] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D29] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D30] + " = '" + scheduleSymbolId +"'"+
                        " OR "+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_D31] + " = '" + scheduleSymbolId +"'";

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
    /*
    public static long process(EmpSchedule empschedule) throws DBException{ 
        String where = "";
            where += FLD_PERIOD_ID + "=" + empschedule.getPeriodId();
            where += " AND " + FLD_EMPLOYEE_ID;
            where += "=" + empschedule.getEmployeeId();
        Vector vcheck = PstEmpSchedule.list(0, 0, where, "");
        if (vchecklist.size() > 0) {
            // update
            
        }
        else {
            // insert
            PstEmpSchedule.insertExc(empSchedule);
        }
    }
     */
}
