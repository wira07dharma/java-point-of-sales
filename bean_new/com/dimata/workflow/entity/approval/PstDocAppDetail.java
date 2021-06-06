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

public class PstDocAppDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	public static final  String TBL_WF_DOCAPP_DETAIL = "wf_docapp_detail";

	public static final  int FLD_DOCAPP_DETAIL_OID = 0;
	public static final  int FLD_EMPLOYEE_OID = 1;
	public static final  int FLD_DOCAPP_MAIN_OID = 2;
	public static final  int FLD_APP_MAPPING_OID = 3;
	public static final  int FLD_APP_STATUS = 4;

	public static final  String[] fieldNames = {
		"DOCAPP_DETAIL_OID",
		"EMPLOYEE_OID",
		"DOCAPP_MAIN_OID",
		"APP_MAPPING_OID",
		"APP_STATUS"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT
	 };

	public PstDocAppDetail(){
	}

	public PstDocAppDetail(int i) throws DBException { 
		super(new PstDocAppDetail()); 
	}

	public PstDocAppDetail(String sOid) throws DBException { 
		super(new PstDocAppDetail(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDocAppDetail(long lOid) throws DBException { 
		super(new PstDocAppDetail(0)); 
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
		return TBL_WF_DOCAPP_DETAIL;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDocAppDetail().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DocAppDetail docappdetail = fetchExc(ent.getOID()); 
		ent = (Entity)docappdetail; 
		return docappdetail.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DocAppDetail) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DocAppDetail) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DocAppDetail fetchExc(long oid) throws DBException{ 
		try{ 
			DocAppDetail docappdetail = new DocAppDetail();
			PstDocAppDetail pstDocAppDetail = new PstDocAppDetail(oid); 
			docappdetail.setOID(oid);

			docappdetail.setEmployeeOid(pstDocAppDetail.getlong(FLD_EMPLOYEE_OID));
			docappdetail.setDocappMainOid(pstDocAppDetail.getlong(FLD_DOCAPP_MAIN_OID));
			docappdetail.setAppMappingOid(pstDocAppDetail.getlong(FLD_APP_MAPPING_OID));
			docappdetail.setAppStatus(pstDocAppDetail.getInt(FLD_APP_STATUS));

			return docappdetail; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppDetail(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DocAppDetail docappdetail) throws DBException{ 
		try{ 
			PstDocAppDetail pstDocAppDetail = new PstDocAppDetail(0);

			pstDocAppDetail.setLong(FLD_EMPLOYEE_OID, docappdetail.getEmployeeOid());
			pstDocAppDetail.setLong(FLD_DOCAPP_MAIN_OID, docappdetail.getDocappMainOid());
			pstDocAppDetail.setLong(FLD_APP_MAPPING_OID, docappdetail.getAppMappingOid());
			pstDocAppDetail.setInt(FLD_APP_STATUS, docappdetail.getAppStatus());

			pstDocAppDetail.insert(); 
			docappdetail.setOID(pstDocAppDetail.getlong(FLD_DOCAPP_DETAIL_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppDetail(0),DBException.UNKNOWN); 
		}
		return docappdetail.getOID();
	}

	public static long updateExc(DocAppDetail docappdetail) throws DBException{ 
		try{ 
			if(docappdetail.getOID() != 0){ 
				PstDocAppDetail pstDocAppDetail = new PstDocAppDetail(docappdetail.getOID());

				pstDocAppDetail.setLong(FLD_EMPLOYEE_OID, docappdetail.getEmployeeOid());
				pstDocAppDetail.setLong(FLD_DOCAPP_MAIN_OID, docappdetail.getDocappMainOid());
				pstDocAppDetail.setLong(FLD_APP_MAPPING_OID, docappdetail.getAppMappingOid());
				pstDocAppDetail.setInt(FLD_APP_STATUS, docappdetail.getAppStatus());

				pstDocAppDetail.update(); 
				return docappdetail.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppDetail(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDocAppDetail pstDocAppDetail = new PstDocAppDetail(oid);
			pstDocAppDetail.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppDetail(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_DOCAPP_DETAIL; 
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
				DocAppDetail docappdetail = new DocAppDetail();
				resultToObject(rs, docappdetail);
				lists.add(docappdetail);
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

	private static void resultToObject(ResultSet rs, DocAppDetail docappdetail){
		try{
			docappdetail.setOID(rs.getLong(PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_DETAIL_OID]));
			docappdetail.setEmployeeOid(rs.getLong(PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_EMPLOYEE_OID]));
			docappdetail.setDocappMainOid(rs.getLong(PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_MAIN_OID]));
			docappdetail.setAppMappingOid(rs.getLong(PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_APP_MAPPING_OID]));
			docappdetail.setAppStatus(rs.getInt(PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_APP_STATUS]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long docappDetailOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOCAPP_DETAIL + " WHERE " + 
						PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_DETAIL_OID] + " = " + docappDetailOid;

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
			String sql = "SELECT COUNT("+ PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_DETAIL_OID] + ") FROM " + TBL_WF_DOCAPP_DETAIL;
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
			  	   DocAppDetail docappdetail = (DocAppDetail)list.get(ls);
				   if(oid == docappdetail.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

	public static void deleteByDocAppMain(long docAppMainId){
		DBResultSet dbrs = null;
		try{
			String sql = "DELETE FROM " + TBL_WF_DOCAPP_DETAIL +
                		 " WHERE " + PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_MAIN_OID] +
                         " = " + docAppMainId;
			dbrs = DBHandler.execQueryResult(sql);
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
		}
	}


    /**
    * this method used to check if current document already approve with appropriate approval
    * @return "true" ---> already approve
    * @return "false" ---> not yet
    */
    public static boolean isDocumentApproved(long docId, long appMappingId){
		DBResultSet dbrs = null;
		try{
			String sql = "SELECT DET." + PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_DETAIL_OID] +
                		 " FROM " + PstDocAppDetail.TBL_WF_DOCAPP_DETAIL + " AS DET " +
                         " INNER JOIN " + PstDocAppMain.TBL_WF_DOC_APP_MAIN + " AS MAN " +
                         " ON DET." + PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_MAIN_OID] +
						 " = MAN." + PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCAPP_MAIN_OID] +
                		 " WHERE MAN." + PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCUMENT_OID] + " = " + docId +
						 " AND DET." + PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_APP_MAPPING_OID] + " = " + appMappingId;
			dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                return true;
            }
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
		}
        return false;
    }

}
