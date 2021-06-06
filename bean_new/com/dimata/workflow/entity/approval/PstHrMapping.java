
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

package com.dimata.workflow.entity.approval; 

/* package java */ 
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import java.sql.*
;import java.util.*
;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;

/* package workflow */

public class PstHrMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_WF_HR_MAPPING = "wf_hr_mapping";

	public static final  int FLD_HR_MAPPING_OID = 0;
	public static final  int FLD_APP_MAPPING_OID = 1;
	public static final  int FLD_DEPARTMENT_ID = 2;
	public static final  int FLD_SECTION_ID = 3;
	public static final  int FLD_POSITION_ID = 4;

	public static final  String[] fieldNames = {
		"HR_MAPPING_OID",
		"APP_MAPPING_OID",
		"DEPARTMENT_ID",
		"SECTION_ID",
		"POSITION_ID"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG
	 }; 

	public PstHrMapping(){
	}

	public PstHrMapping(int i) throws DBException { 
		super(new PstHrMapping()); 
	}

	public PstHrMapping(String sOid) throws DBException { 
		super(new PstHrMapping(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstHrMapping(long lOid) throws DBException { 
		super(new PstHrMapping(0)); 
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
		return TBL_WF_HR_MAPPING;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstHrMapping().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		HrMapping hrmapping = fetchExc(ent.getOID()); 
		ent = (Entity)hrmapping; 
		return hrmapping.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((HrMapping) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((HrMapping) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static HrMapping fetchExc(long oid) throws DBException{ 
		try{ 
			HrMapping hrmapping = new HrMapping();
			PstHrMapping pstHrMapping = new PstHrMapping(oid); 
			hrmapping.setOID(oid);

			hrmapping.setAppMappingOid(pstHrMapping.getlong(FLD_APP_MAPPING_OID));
			hrmapping.setDepartmentId(pstHrMapping.getlong(FLD_DEPARTMENT_ID));
			hrmapping.setSectionId(pstHrMapping.getlong(FLD_SECTION_ID));
			hrmapping.setPositionId(pstHrMapping.getlong(FLD_POSITION_ID));

			return hrmapping; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstHrMapping(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(HrMapping hrmapping) throws DBException{ 
		try{ 
			PstHrMapping pstHrMapping = new PstHrMapping(0);

			pstHrMapping.setLong(FLD_APP_MAPPING_OID, hrmapping.getAppMappingOid());
			pstHrMapping.setLong(FLD_DEPARTMENT_ID, hrmapping.getDepartmentId());
			pstHrMapping.setLong(FLD_SECTION_ID, hrmapping.getSectionId());
			pstHrMapping.setLong(FLD_POSITION_ID, hrmapping.getPositionId());

			pstHrMapping.insert(); 
			hrmapping.setOID(pstHrMapping.getlong(FLD_HR_MAPPING_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstHrMapping(0),DBException.UNKNOWN); 
		}
		return hrmapping.getOID();
	}

	public static long updateExc(HrMapping hrmapping) throws DBException{ 
		try{ 
			if(hrmapping.getOID() != 0){ 
				PstHrMapping pstHrMapping = new PstHrMapping(hrmapping.getOID());

				pstHrMapping.setLong(FLD_APP_MAPPING_OID, hrmapping.getAppMappingOid());
				pstHrMapping.setLong(FLD_DEPARTMENT_ID, hrmapping.getDepartmentId());
				pstHrMapping.setLong(FLD_SECTION_ID, hrmapping.getSectionId());
				pstHrMapping.setLong(FLD_POSITION_ID, hrmapping.getPositionId());

				pstHrMapping.update(); 
				return hrmapping.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstHrMapping(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstHrMapping pstHrMapping = new PstHrMapping(oid);
			pstHrMapping.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstHrMapping(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_HR_MAPPING; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
						if(limitStart == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

                        break;

                 case DBHandler.DBSVR_POSTGRESQL :
						if(limitStart == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;

                        break;

                 case DBHandler.DBSVR_SYBASE :
                    	break;

                 case DBHandler.DBSVR_ORACLE :
                    	break;

                 case DBHandler.DBSVR_MSSQL :
                    	break;

                default:
                    ;
            }

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				HrMapping hrmapping = new HrMapping();
				resultToObject(rs, hrmapping);
				lists.add(hrmapping);
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

	private static void resultToObject(ResultSet rs, HrMapping hrmapping){
		try{
			hrmapping.setOID(rs.getLong(PstHrMapping.fieldNames[PstHrMapping.FLD_HR_MAPPING_OID]));
			hrmapping.setAppMappingOid(rs.getLong(PstHrMapping.fieldNames[PstHrMapping.FLD_APP_MAPPING_OID]));
			hrmapping.setDepartmentId(rs.getLong(PstHrMapping.fieldNames[PstHrMapping.FLD_DEPARTMENT_ID]));
			hrmapping.setSectionId(rs.getLong(PstHrMapping.fieldNames[PstHrMapping.FLD_SECTION_ID]));
			hrmapping.setPositionId(rs.getLong(PstHrMapping.fieldNames[PstHrMapping.FLD_POSITION_ID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long hrMappingOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_HR_MAPPING + " WHERE " + 
						PstHrMapping.fieldNames[PstHrMapping.FLD_HR_MAPPING_OID] + " = " + hrMappingOid;

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
			String sql = "SELECT COUNT("+ PstHrMapping.fieldNames[PstHrMapping.FLD_HR_MAPPING_OID] + ") FROM " + TBL_WF_HR_MAPPING;
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
			  	   HrMapping hrmapping = (HrMapping)list.get(ls);
				   if(oid == hrmapping.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
}
