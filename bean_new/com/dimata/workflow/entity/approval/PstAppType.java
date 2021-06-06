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

import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

public class PstAppType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	public static final  String TBL_WF_APP_TYPE = "wf_app_type";

	public static final  int FLD_APPTYPE_OID = 0;
	public static final  int FLD_NAME = 1;
	public static final  int FLD_DESCRIPTION = 2;

	public static final  String[] fieldNames = {
		"APPTYPE_OID",
		"NAME",
		"DESCRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstAppType(){
	}

	public PstAppType(int i) throws DBException { 
		super(new PstAppType()); 
	}

	public PstAppType(String sOid) throws DBException { 
		super(new PstAppType(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstAppType(long lOid) throws DBException { 
		super(new PstAppType(0)); 
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
		return TBL_WF_APP_TYPE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstAppType().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		AppType apptype = fetchExc(ent.getOID()); 
		ent = (Entity)apptype; 
		return apptype.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((AppType) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((AppType) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static AppType fetchExc(long oid) throws DBException{ 
		try{ 
			AppType apptype = new AppType();
			PstAppType pstAppType = new PstAppType(oid); 
			apptype.setOID(oid);

			apptype.setName(pstAppType.getString(FLD_NAME));
			apptype.setDescription(pstAppType.getString(FLD_DESCRIPTION));

			return apptype; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppType(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(AppType apptype) throws DBException{ 
		try{ 
			PstAppType pstAppType = new PstAppType(0);

			pstAppType.setString(FLD_NAME, apptype.getName());
			pstAppType.setString(FLD_DESCRIPTION, apptype.getDescription());

			pstAppType.insert(); 
			apptype.setOID(pstAppType.getlong(FLD_APPTYPE_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppType(0),DBException.UNKNOWN); 
		}
		return apptype.getOID();
	}

	public static long updateExc(AppType apptype) throws DBException{ 
		try{ 
			if(apptype.getOID() != 0){ 
				PstAppType pstAppType = new PstAppType(apptype.getOID());

				pstAppType.setString(FLD_NAME, apptype.getName());
				pstAppType.setString(FLD_DESCRIPTION, apptype.getDescription());

				pstAppType.update(); 
				return apptype.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppType(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstAppType pstAppType = new PstAppType(oid);
			pstAppType.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstAppType(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_APP_TYPE; 
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
				AppType apptype = new AppType();
				resultToObject(rs, apptype);
				lists.add(apptype);
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

	private static void resultToObject(ResultSet rs, AppType apptype){
		try{
			apptype.setOID(rs.getLong(PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID]));
			apptype.setName(rs.getString(PstAppType.fieldNames[PstAppType.FLD_NAME]));
			apptype.setDescription(rs.getString(PstAppType.fieldNames[PstAppType.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long apptypeOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_APP_TYPE + " WHERE " + 
						PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID] + " = " + apptypeOid;

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
			String sql = "SELECT COUNT("+ PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID] + ") FROM " + TBL_WF_APP_TYPE;
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
			  	   AppType apptype = (AppType)list.get(ls);
				   if(oid == apptype.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    /**
    * this method used to get name of approval type
    */
    public static String getApprovalTypeName(long appTypeOid){
        String result = "";
        try{
	        PstAppType pstAppType = new PstAppType();
	        AppType appType = pstAppType.fetchExc(appTypeOid);
	        return appType.getName();
        }catch(Exception e){
            System.out.println("Error");
        }
        return result;
	}

    public static void main(String args[]){
        Vector vectAppType = PstAppType.list(0,0,"","");
        if(vectAppType!=null && vectAppType.size()>0){
            System.out.println("AppType : " + vectAppType.size());
        }else{
            System.out.println("AppType empty");
        }
    }

}
