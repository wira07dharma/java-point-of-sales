
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

package com.dimata.workflow.entity.status; 

/* package java */ 
import java.sql.*
;import java.util.*
;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

/* package workflow */

public class PstChangesStatus extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_WF_CHANGES_STATUS = "wf_changes_status";

	public static final  int FLD_APP_MAPPING_OID = 0;
	public static final  int FLD_DOC_STATUS_OID = 1;

	public static final  String[] fieldNames = {
		"APP_MAPPING_OID",
		"DOC_STATUS_OID"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_PK + TYPE_FK + TYPE_LONG,
		TYPE_PK + TYPE_FK + TYPE_LONG
	 }; 

	public PstChangesStatus(){
	}

	public PstChangesStatus(int i) throws DBException { 
		super(new PstChangesStatus()); 
	}

	public PstChangesStatus(String sOid) throws DBException { 
		super(new PstChangesStatus(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstChangesStatus(long appMappingOid, long docTypeStatusOid) throws DBException { 
		super(new PstChangesStatus(0)); 
		if(!locate(appMappingOid, docTypeStatusOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else 
			return; 
	} 

	public int getFieldSize(){ 
		return fieldNames.length; 
	}

	public String getTableName(){ 
		return TBL_WF_CHANGES_STATUS;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstChangesStatus().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ChangesStatus changesstatus = fetchExc(ent.getOID(0), ent.getOID(1) ); 
		ent = (Entity)changesstatus; 
		return changesstatus.getOID(0); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ChangesStatus) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ChangesStatus) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent); 
	}

	public static ChangesStatus fetchExc(long appMappingOid, long docStatusOid) throws DBException{
		try{ 
			ChangesStatus changesstatus = new ChangesStatus();
			PstChangesStatus pstChangesStatus = new PstChangesStatus(appMappingOid, docStatusOid);
			changesstatus.setAppMappingOid(appMappingOid);
			changesstatus.setDocStatusOid(docStatusOid);


			return changesstatus; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChangesStatus(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ChangesStatus changesstatus) throws DBException{ 
		try{ 
			PstChangesStatus pstChangesStatus = new PstChangesStatus(0);

			pstChangesStatus.setLong(FLD_APP_MAPPING_OID, changesstatus.getAppMappingOid());
			pstChangesStatus.setLong(FLD_DOC_STATUS_OID, changesstatus.getDocStatusOid());

			pstChangesStatus.insert(); 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChangesStatus(0),DBException.UNKNOWN); 
		}
		return changesstatus.getOID();
	}

	public static long updateExc(ChangesStatus changesstatus) throws DBException{ 
		try{ 
			if(changesstatus.getOID() != 0){ 
				PstChangesStatus pstChangesStatus = new PstChangesStatus(changesstatus.getAppMappingOid(), changesstatus.getDocStatusOid());


				pstChangesStatus.update(); 
				return changesstatus.getAppMappingOid();
			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChangesStatus(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long appMappingOid, long docStatusOid) throws DBException{
		try{ 
			PstChangesStatus pstChangesStatus = new PstChangesStatus(appMappingOid, docStatusOid);
			pstChangesStatus.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstChangesStatus(0),DBException.UNKNOWN); 
		}
		return appMappingOid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_WF_CHANGES_STATUS; 
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
				ChangesStatus changesstatus = new ChangesStatus();
				resultToObject(rs, changesstatus);
				lists.add(changesstatus);
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

	private static void resultToObject(ResultSet rs, ChangesStatus changesstatus){
		try{
			changesstatus.setAppMappingOid(rs.getLong(PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID]));
			changesstatus.setDocStatusOid(rs.getLong(PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long appMappingOid, long docStatusOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_CHANGES_STATUS + " WHERE " + 
			PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID] + " = " + appMappingOid + " AND " + 
			PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID] + " = " + docStatusOid;

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
			String sql = "SELECT COUNT("+ PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID] + ") FROM " + TBL_WF_CHANGES_STATUS;
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


	public static long deleteByAppMappingOid (long appMappingOid) {
		 try {
			 String sql = "DELETE FROM " + TBL_WF_CHANGES_STATUS+
				 " WHERE "+PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID] +
				 " = " + appMappingOid ;

			 int k = DBHandler.execUpdate(sql);
			 return appMappingOid;
		 }catch(Exception exc) { 
		    System.out.println(" exception delete by Owner ID "+exc.toString()); 
		 }
		 return 0;  
	}

	public static long deleteByDocStatusOid (long docStatusOid) {
		 try {
			 String sql = "DELETE FROM " + TBL_WF_CHANGES_STATUS+
				 " WHERE "+PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID] +
				 " = " + docStatusOid ;

			  int k = DBHandler.execUpdate(sql);
			 return docStatusOid;
		 }catch(Exception exc) { 
		    System.out.println(" exception delete by Owner ID "+exc.toString()); 
		 }
		 return 0;  
	}

	/* This method used to find current data */
	public static int findLimitStart(long appMappingOid, long docStatusOid, int recordToGet, String whereClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   ChangesStatus changesstatus = (ChangesStatus)list.get(ls);
				   if(appMappingOid==changesstatus.getAppMappingOid() && docStatusOid==changesstatus.getDocStatusOid())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    /**
    * this method used to list 'action status' for specify document type
    */
    public static Vector listProcessStatus(long appMappingId){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " + PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID] +
                		 ", " + PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID] +
                		 " FROM " + PstChangesStatus.TBL_WF_CHANGES_STATUS +
                	     " WHERE " + PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID] + " = " + appMappingId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                ChangesStatus chs = new ChangesStatus();
                chs.setDocStatusOid(rs.getLong(1));
                chs.setAppMappingOid(rs.getLong(2));
                result.add(chs);
            }
    	}catch(Exception e){
    		System.out.println("Err : "+e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

}
