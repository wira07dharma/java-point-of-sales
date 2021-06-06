
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

package com.dimata.harisma.entity.recruitment; 

/* package java */ 
import java.sql.*;
import java.util.*;

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

public class PstStaffRequisition extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_STAFF_REQUISITION = "HR_STAFF_REQUISITION";

	public static final  int FLD_STAFF_REQUISITION_ID = 0;
	public static final  int FLD_DEPARTMENT_ID = 1;
	public static final  int FLD_SECTION_ID = 2;
	public static final  int FLD_POSITION_ID = 3;
	public static final  int FLD_EMP_CATEGORY_ID = 4;
	public static final  int FLD_REQUISITION_TYPE = 5;
	public static final  int FLD_NEEDED_MALE = 6;
	public static final  int FLD_NEEDED_FEMALE = 7;
	public static final  int FLD_EXP_COMM_DATE = 8;
	public static final  int FLD_TEMP_FOR = 9;
	public static final  int FLD_APPROVED_BY = 10;
	public static final  int FLD_APPROVED_DATE = 11;
	public static final  int FLD_ACKNOWLEDGED_BY = 12;
	public static final  int FLD_ACKNOWLEDGED_DATE = 13;
	public static final  int FLD_REQUESTED_BY = 14;
	public static final  int FLD_REQUESTED_DATE = 15;

	public static final  String[] fieldNames = {
		"STAFF_REQUISITION_ID",
		"DEPARTMENT_ID",
		"SECTION_ID",
		"POSITION_ID",
		"EMP_CATEGORY_ID",
		"REQUISITION_TYPE",
		"NEEDED_MALE",
		"NEEDED_FEMALE",
		"EXP_COMM_DATE",
		"TEMP_FOR",
		"APPROVED_BY",
		"APPROVED_DATE",
		"ACKNOWLEDGED_BY",
		"ACKNOWLEDGED_DATE",
		"REQUESTED_BY",
		"REQUESTED_DATE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_DATE,
		TYPE_INT,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_LONG,
		TYPE_DATE
	 }; 

        //requisition type
        public static final int REPLACEMNT 		= 0;
        public static final int ADDITIONAL 		= 1;

        public static final String[] reqtypeKey = {"Replacement","Additional"};
        public static final int[] reqtypeValue = {0,1};
         
	public PstStaffRequisition(){
	}

	public PstStaffRequisition(int i) throws DBException { 
		super(new PstStaffRequisition()); 
	}

	public PstStaffRequisition(String sOid) throws DBException { 
		super(new PstStaffRequisition(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstStaffRequisition(long lOid) throws DBException { 
		super(new PstStaffRequisition(0)); 
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
		return TBL_HR_STAFF_REQUISITION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstStaffRequisition().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		StaffRequisition staffrequisition = fetchExc(ent.getOID()); 
		ent = (Entity)staffrequisition; 
		return staffrequisition.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((StaffRequisition) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((StaffRequisition) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static StaffRequisition fetchExc(long oid) throws DBException{ 
		try{ 
			StaffRequisition staffrequisition = new StaffRequisition();
			PstStaffRequisition pstStaffRequisition = new PstStaffRequisition(oid); 
			staffrequisition.setOID(oid);

			staffrequisition.setDepartmentId(pstStaffRequisition.getlong(FLD_DEPARTMENT_ID));
			staffrequisition.setSectionId(pstStaffRequisition.getlong(FLD_SECTION_ID));
			staffrequisition.setPositionId(pstStaffRequisition.getlong(FLD_POSITION_ID));
			staffrequisition.setEmpCategoryId(pstStaffRequisition.getlong(FLD_EMP_CATEGORY_ID));
			staffrequisition.setRequisitionType(pstStaffRequisition.getInt(FLD_REQUISITION_TYPE));
			staffrequisition.setNeededMale(pstStaffRequisition.getInt(FLD_NEEDED_MALE));
			staffrequisition.setNeededFemale(pstStaffRequisition.getInt(FLD_NEEDED_FEMALE));
			staffrequisition.setExpCommDate(pstStaffRequisition.getDate(FLD_EXP_COMM_DATE));
			staffrequisition.setTempFor(pstStaffRequisition.getInt(FLD_TEMP_FOR));
			staffrequisition.setApprovedBy(pstStaffRequisition.getlong(FLD_APPROVED_BY));
			staffrequisition.setApprovedDate(pstStaffRequisition.getDate(FLD_APPROVED_DATE));
			staffrequisition.setAcknowledgedBy(pstStaffRequisition.getlong(FLD_ACKNOWLEDGED_BY));
			staffrequisition.setAcknowledgedDate(pstStaffRequisition.getDate(FLD_ACKNOWLEDGED_DATE));
			staffrequisition.setRequestedBy(pstStaffRequisition.getlong(FLD_REQUESTED_BY));
			staffrequisition.setRequestedDate(pstStaffRequisition.getDate(FLD_REQUESTED_DATE));

			return staffrequisition; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstStaffRequisition(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(StaffRequisition staffrequisition) throws DBException{ 
		try{ 
			PstStaffRequisition pstStaffRequisition = new PstStaffRequisition(0);

			pstStaffRequisition.setLong(FLD_DEPARTMENT_ID, staffrequisition.getDepartmentId());
			pstStaffRequisition.setLong(FLD_SECTION_ID, staffrequisition.getSectionId());
			pstStaffRequisition.setLong(FLD_POSITION_ID, staffrequisition.getPositionId());
			pstStaffRequisition.setLong(FLD_EMP_CATEGORY_ID, staffrequisition.getEmpCategoryId());
			pstStaffRequisition.setInt(FLD_REQUISITION_TYPE, staffrequisition.getRequisitionType());
			pstStaffRequisition.setInt(FLD_NEEDED_MALE, staffrequisition.getNeededMale());
			pstStaffRequisition.setInt(FLD_NEEDED_FEMALE, staffrequisition.getNeededFemale());
			pstStaffRequisition.setDate(FLD_EXP_COMM_DATE, staffrequisition.getExpCommDate());
			pstStaffRequisition.setInt(FLD_TEMP_FOR, staffrequisition.getTempFor());
			pstStaffRequisition.setLong(FLD_APPROVED_BY, staffrequisition.getApprovedBy());
			pstStaffRequisition.setDate(FLD_APPROVED_DATE, staffrequisition.getApprovedDate());
			pstStaffRequisition.setLong(FLD_ACKNOWLEDGED_BY, staffrequisition.getAcknowledgedBy());
			pstStaffRequisition.setDate(FLD_ACKNOWLEDGED_DATE, staffrequisition.getAcknowledgedDate());
			pstStaffRequisition.setLong(FLD_REQUESTED_BY, staffrequisition.getRequestedBy());
			pstStaffRequisition.setDate(FLD_REQUESTED_DATE, staffrequisition.getRequestedDate());

			pstStaffRequisition.insert(); 
			staffrequisition.setOID(pstStaffRequisition.getlong(FLD_STAFF_REQUISITION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstStaffRequisition(0),DBException.UNKNOWN); 
		}
		return staffrequisition.getOID();
	}

	public static long updateExc(StaffRequisition staffrequisition) throws DBException{ 
		try{ 
			if(staffrequisition.getOID() != 0){ 
				PstStaffRequisition pstStaffRequisition = new PstStaffRequisition(staffrequisition.getOID());

				pstStaffRequisition.setLong(FLD_DEPARTMENT_ID, staffrequisition.getDepartmentId());
				pstStaffRequisition.setLong(FLD_SECTION_ID, staffrequisition.getSectionId());
				pstStaffRequisition.setLong(FLD_POSITION_ID, staffrequisition.getPositionId());
				pstStaffRequisition.setLong(FLD_EMP_CATEGORY_ID, staffrequisition.getEmpCategoryId());
				pstStaffRequisition.setInt(FLD_REQUISITION_TYPE, staffrequisition.getRequisitionType());
				pstStaffRequisition.setInt(FLD_NEEDED_MALE, staffrequisition.getNeededMale());
				pstStaffRequisition.setInt(FLD_NEEDED_FEMALE, staffrequisition.getNeededFemale());
				pstStaffRequisition.setDate(FLD_EXP_COMM_DATE, staffrequisition.getExpCommDate());
				pstStaffRequisition.setInt(FLD_TEMP_FOR, staffrequisition.getTempFor());
				pstStaffRequisition.setLong(FLD_APPROVED_BY, staffrequisition.getApprovedBy());
				pstStaffRequisition.setDate(FLD_APPROVED_DATE, staffrequisition.getApprovedDate());
				pstStaffRequisition.setLong(FLD_ACKNOWLEDGED_BY, staffrequisition.getAcknowledgedBy());
				pstStaffRequisition.setDate(FLD_ACKNOWLEDGED_DATE, staffrequisition.getAcknowledgedDate());
				pstStaffRequisition.setLong(FLD_REQUESTED_BY, staffrequisition.getRequestedBy());
				pstStaffRequisition.setDate(FLD_REQUESTED_DATE, staffrequisition.getRequestedDate());

				pstStaffRequisition.update(); 
				return staffrequisition.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstStaffRequisition(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstStaffRequisition pstStaffRequisition = new PstStaffRequisition(oid);
			pstStaffRequisition.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstStaffRequisition(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_STAFF_REQUISITION; 
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
				StaffRequisition staffrequisition = new StaffRequisition();
				resultToObject(rs, staffrequisition);
				lists.add(staffrequisition);
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

	public static void resultToObject(ResultSet rs, StaffRequisition staffrequisition){
		try{
			staffrequisition.setOID(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_STAFF_REQUISITION_ID]));
			staffrequisition.setDepartmentId(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_DEPARTMENT_ID]));
			staffrequisition.setSectionId(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_SECTION_ID]));
			staffrequisition.setPositionId(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_POSITION_ID]));
			staffrequisition.setEmpCategoryId(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_EMP_CATEGORY_ID]));
			staffrequisition.setRequisitionType(rs.getInt(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_REQUISITION_TYPE]));
			staffrequisition.setNeededMale(rs.getInt(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_NEEDED_MALE]));
			staffrequisition.setNeededFemale(rs.getInt(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_NEEDED_FEMALE]));
			staffrequisition.setExpCommDate(rs.getDate(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_EXP_COMM_DATE]));
			staffrequisition.setTempFor(rs.getInt(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_TEMP_FOR]));
			staffrequisition.setApprovedBy(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_APPROVED_BY]));
			staffrequisition.setApprovedDate(rs.getDate(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_APPROVED_DATE]));
			staffrequisition.setAcknowledgedBy(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_ACKNOWLEDGED_BY]));
			staffrequisition.setAcknowledgedDate(rs.getDate(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_ACKNOWLEDGED_DATE]));
			staffrequisition.setRequestedBy(rs.getLong(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_REQUESTED_BY]));
			staffrequisition.setRequestedDate(rs.getDate(PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_REQUESTED_DATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long staffRequisitionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_STAFF_REQUISITION + " WHERE " + 
						PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_STAFF_REQUISITION_ID] + " = " + staffRequisitionId;

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
			String sql = "SELECT COUNT("+ PstStaffRequisition.fieldNames[PstStaffRequisition.FLD_STAFF_REQUISITION_ID] + ") FROM " + TBL_HR_STAFF_REQUISITION;
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
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   StaffRequisition staffrequisition = (StaffRequisition)list.get(ls);
				   if(oid == staffrequisition.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
	/* This method used to find command where current data */
	public static int findLimitCommand(int start, int recordToGet, int vectSize){
		 int cmd = Command.LIST;
		 int mdl = vectSize % recordToGet;
		 vectSize = vectSize + (recordToGet - mdl);
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
