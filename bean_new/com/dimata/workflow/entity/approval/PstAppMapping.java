/**
 * Created on 	: 3:00 PM
 * @author	    : gedhy
 * @version	    : 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.workflow.entity.approval; 

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

/* package harisma */

/* package workflow */
import com.dimata.workflow.entity.status.*;

public class PstAppMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_WF_APP_MAPPING = "wf_app_mapping";

	public static final  int FLD_APP_MAPPING_OID = 0;
	public static final  int FLD_APP_TITLE = 1;
	public static final  int FLD_APPTYPE_OID = 2;
	public static final  int FLD_DOCTYPE = 3;
	public static final  int FLD_DEPARTMENT_OID = 4;
	public static final  int FLD_POSITION_OID = 5;
	public static final  int FLD_SECTION_ID = 6;
	public static final  int FLD_APP_INDEX = 7;
	public static final  int FLD_APP_CONDITION_OID = 8;
	public static final  int FLD_DOC_STATUS_OID_BEFORE = 9;
	public static final  int FLD_DOC_STATUS_OID_AFTER = 10;

	public static final  String[] fieldNames = {
		"APP_MAPPING_OID",
		"APPROVE_TITLE",
		"APPTYPE_OID",
		"DOCTYPE_TYPE",
		"DEPARTMENT_OID",
		"POSITION_OID",
		"SECTION_ID",
        "APPROVAL_INDEX",
        "APP_CONDITION_OID",
        "DOC_STATUS_OID_BEFORE",
        "DOC_STATUS_OID_AFTER",
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
		TYPE_LONG,
		TYPE_INT,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
	 }; 

	public PstAppMapping(){
	}

	public PstAppMapping(int i) throws DBException { 
		super(new PstAppMapping()); 
	}

	public PstAppMapping(String sOid) throws DBException { 
		super(new PstAppMapping(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstAppMapping(long lOid) throws DBException { 
		super(new PstAppMapping(0)); 
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
		return TBL_WF_APP_MAPPING;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstAppMapping().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		AppMapping appmapping = fetchExc(ent.getOID()); 
		ent = (Entity)appmapping; 
		return appmapping.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((AppMapping) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((AppMapping) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static AppMapping fetchExc(long oid) throws DBException{ 
		try{ 
			AppMapping appmapping = new AppMapping();
			PstAppMapping pstAppMapping = new PstAppMapping(oid); 
			appmapping.setOID(oid);

			appmapping.setAppTitle(pstAppMapping.getString(FLD_APP_TITLE));
			appmapping.setAppTypeOid(pstAppMapping.getlong(FLD_APPTYPE_OID));
			appmapping.setDocTypeType(pstAppMapping.getInt(FLD_DOCTYPE));
			appmapping.setDepartmentOid(pstAppMapping.getlong(FLD_DEPARTMENT_OID));
			appmapping.setPositionOid(pstAppMapping.getlong(FLD_POSITION_OID));
			appmapping.setSectionId(pstAppMapping.getlong(FLD_SECTION_ID));
			appmapping.setAppIndex(pstAppMapping.getInt(FLD_APP_INDEX));
			appmapping.setAppConditionOid(pstAppMapping.getlong(FLD_APP_CONDITION_OID));
			appmapping.setDocStatusOidBefore(pstAppMapping.getlong(FLD_DOC_STATUS_OID_BEFORE));
			appmapping.setDocStatusOidAfter(pstAppMapping.getlong(FLD_DOC_STATUS_OID_AFTER));

			return appmapping; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppMapping(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(AppMapping appmapping) throws DBException{ 
		try{ 
			PstAppMapping pstAppMapping = new PstAppMapping(0);

			pstAppMapping.setString(FLD_APP_TITLE, appmapping.getAppTitle());
			pstAppMapping.setLong(FLD_APPTYPE_OID, appmapping.getAppTypeOid());
			pstAppMapping.setInt(FLD_DOCTYPE, appmapping.getDocTypeType());
			pstAppMapping.setLong(FLD_DEPARTMENT_OID, appmapping.getDepartmentOid());
			pstAppMapping.setLong(FLD_POSITION_OID, appmapping.getPositionOid());
			pstAppMapping.setLong(FLD_SECTION_ID, appmapping.getSectionId());
			pstAppMapping.setInt(FLD_APP_INDEX, appmapping.getAppIndex());
			pstAppMapping.setLong(FLD_APP_CONDITION_OID, appmapping.getAppConditionOid());
			pstAppMapping.setLong(FLD_DOC_STATUS_OID_BEFORE, appmapping.getDocStatusOidBefore());
			pstAppMapping.setLong(FLD_DOC_STATUS_OID_AFTER, appmapping.getDocStatusOidAfter());

			pstAppMapping.insert(); 
			appmapping.setOID(pstAppMapping.getlong(FLD_APP_MAPPING_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppMapping(0),DBException.UNKNOWN); 
		}
		return appmapping.getOID();
	}

	public static long updateExc(AppMapping appmapping) throws DBException{ 
		try{ 
			if(appmapping.getOID() != 0){ 
				PstAppMapping pstAppMapping = new PstAppMapping(appmapping.getOID());

				pstAppMapping.setString(FLD_APP_TITLE, appmapping.getAppTitle());
				pstAppMapping.setLong(FLD_APPTYPE_OID, appmapping.getAppTypeOid());
				pstAppMapping.setInt(FLD_DOCTYPE, appmapping.getDocTypeType());
				pstAppMapping.setLong(FLD_DEPARTMENT_OID, appmapping.getDepartmentOid());
				pstAppMapping.setLong(FLD_POSITION_OID, appmapping.getPositionOid());
				pstAppMapping.setLong(FLD_SECTION_ID, appmapping.getSectionId());
				pstAppMapping.setInt(FLD_APP_INDEX, appmapping.getAppIndex());
				pstAppMapping.setLong(FLD_APP_CONDITION_OID, appmapping.getAppConditionOid());
				pstAppMapping.setLong(FLD_DOC_STATUS_OID_BEFORE, appmapping.getDocStatusOidBefore());
				pstAppMapping.setLong(FLD_DOC_STATUS_OID_AFTER, appmapping.getDocStatusOidAfter());

				pstAppMapping.update(); 
				return appmapping.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppMapping(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstAppMapping pstAppMapping = new PstAppMapping(oid);
			pstAppMapping.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppMapping(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_APP_MAPPING; 
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
				AppMapping appmapping = new AppMapping();
				resultToObject(rs, appmapping);
				lists.add(appmapping);
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

	private static void resultToObject(ResultSet rs, AppMapping appmapping){
		try{
			appmapping.setOID(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID]));
			appmapping.setAppTitle(rs.getString(PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE]));
			appmapping.setAppTypeOid(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID]));
			appmapping.setDocTypeType(rs.getInt(PstAppMapping.fieldNames[PstAppMapping.FLD_DOCTYPE]));
			appmapping.setDepartmentOid(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID]));
			appmapping.setPositionOid(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID]));
			appmapping.setSectionId(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID]));
			appmapping.setAppIndex(rs.getInt(PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID]));
			appmapping.setAppConditionOid(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID]));
			appmapping.setDocStatusOidBefore(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_DOC_STATUS_OID_BEFORE]));
			appmapping.setDocStatusOidAfter(rs.getLong(PstAppMapping.fieldNames[PstAppMapping.FLD_DOC_STATUS_OID_AFTER]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long appMappingOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_APP_MAPPING + " WHERE " + 
						PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] + " = " + appMappingOid;

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
			String sql = "SELECT COUNT("+ PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] + ") FROM " + TBL_WF_APP_MAPPING;
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
			  	   AppMapping appmapping = (AppMapping)list.get(ls);
				   if(oid == appmapping.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    /**
    * set 'action status' for specified appMapping
    * return "true" ---> no error occur when insert data
    * return "false" ---> error occur when insert data
    **/
    public static boolean setActionStatus(long appMappingId, Vector vector){
        // delete the lastest action status that record on "CHANGE_STATUS"
        if(PstChangesStatus.deleteByAppMappingOid(appMappingId)==0){
            return false;
        }
        
        if(vector==null || vector.size()==0){
            return true;
        }
        
        // insert the new action status to "CHANGE_STATUS"
        for(int i=0; i<vector.size(); i++) {
            ChangesStatus chs = (ChangesStatus)vector.get(i);
            long oid = 0;
            try{
                chs.setAppMappingOid(appMappingId);
	            oid = PstChangesStatus.insertExc(chs);
            }catch(Exception e){
                System.out.println("Error insertExc setActionStatus");
            }

            if(oid==0){
                return false;
            }
        }         
        return true;
    }

    /**
    * this method used to list 'action status' may done by specified appMapping
    */
    public static Vector listAction(long appMappingOid){
        Vector result = new Vector(1,1);
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT DS." + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                		 " FROM " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " INNER JOIN " + PstChangesStatus.TBL_WF_CHANGES_STATUS + " AS CS " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                         " = CS." + PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID] +
                         " INNER JOIN " + PstDocStatus.TBL_WF_DOC_STATUS + " AS DS " +
                         " ON CS." + PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID] +
                         " = DS." + PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] +
						 " WHERE MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
						 " = " + appMappingOid;
			//System.out.println("#####################PstAppMapping.listAction.sql : "+sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()){
				result.add(String.valueOf(rs.getInt(1)));
            }
		}catch(Exception e){
			System.out.println("#####################PstAppMapping.listAction.err : "+e.toString());
		}finally {
			DBResultSet.close(dbrs);
            return result;
		}
    }

}
