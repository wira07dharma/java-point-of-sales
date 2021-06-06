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
/* package workflow */
import com.dimata.workflow.entity.status.*;

public class PstDocType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_DocType, I_PstDocType {

	public static final String TBL_WF_DOC_TYPE = "wf_doc_type";

	public static final int FLD_DOCTYPE_OID = 0;
	public static final int FLD_TYPE 		= 1;
	public static final int FLD_CODE 		= 2;
	public static final int FLD_NAME 		= 3;
	public static final int FLD_DESCRIPTION = 4;

	public static final String[] fieldNames = {
		"DOCTYPE_OID",
		"TYPE",
		"CODE",
		"NAME",
		"DESCRIPTION"
	 }; 

	public static final int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING
	 };

	public PstDocType(){
	}

	public PstDocType(int i) throws DBException { 
		super(new PstDocType()); 
	}

	public PstDocType(String sOid) throws DBException { 
		super(new PstDocType(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDocType(long lOid) throws DBException { 
		super(new PstDocType(0)); 
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
		return TBL_WF_DOC_TYPE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDocType().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DocType doctype = fetchExc(ent.getOID()); 
		ent = (Entity)doctype; 
		return doctype.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DocType) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DocType) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DocType fetchExc(long oid) throws DBException{ 
		try{ 
			DocType doctype = new DocType();
			PstDocType pstDocType = new PstDocType(oid); 
			doctype.setOID(oid);

			doctype.setType(pstDocType.getInt(FLD_TYPE));
			doctype.setCode(pstDocType.getString(FLD_CODE));
			doctype.setName(pstDocType.getString(FLD_NAME));
			doctype.setDescription(pstDocType.getString(FLD_DESCRIPTION));

			return doctype; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocType(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DocType doctype) throws DBException{ 
		try{ 
			PstDocType pstDocType = new PstDocType(0);

			pstDocType.setInt(FLD_TYPE, doctype.getType());
			pstDocType.setString(FLD_CODE, doctype.getCode());
			pstDocType.setString(FLD_NAME, doctype.getName());
			pstDocType.setString(FLD_DESCRIPTION, doctype.getDescription());

			pstDocType.insert(); 
			doctype.setOID(pstDocType.getlong(FLD_DOCTYPE_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocType(0),DBException.UNKNOWN); 
		}
		return doctype.getOID();
	}

	public static long updateExc(DocType doctype) throws DBException{ 
		try{ 
			if(doctype.getOID() != 0){ 
				PstDocType pstDocType = new PstDocType(doctype.getOID());

				pstDocType.setInt(FLD_TYPE, doctype.getType());
				pstDocType.setString(FLD_CODE, doctype.getCode());
				pstDocType.setString(FLD_NAME, doctype.getName());
				pstDocType.setString(FLD_DESCRIPTION, doctype.getDescription());

				pstDocType.update(); 
				return doctype.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocType(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDocType pstDocType = new PstDocType(oid);
			pstDocType.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocType(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_DOC_TYPE; 
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
				DocType doctype = new DocType();
				resultToObject(rs, doctype);
				lists.add(doctype);
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

	private static void resultToObject(ResultSet rs, DocType doctype){
		try{
			doctype.setOID(rs.getLong(PstDocType.fieldNames[PstDocType.FLD_DOCTYPE_OID]));
			doctype.setType(rs.getInt(PstDocType.fieldNames[PstDocType.FLD_TYPE]));
			doctype.setCode(rs.getString(PstDocType.fieldNames[PstDocType.FLD_CODE]));
			doctype.setName(rs.getString(PstDocType.fieldNames[PstDocType.FLD_NAME]));
			doctype.setDescription(rs.getString(PstDocType.fieldNames[PstDocType.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long doctypeOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOC_TYPE + " WHERE " + 
						PstDocType.fieldNames[PstDocType.FLD_DOCTYPE_OID] + " = " + doctypeOid;

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
			String sql = "SELECT COUNT("+ PstDocType.fieldNames[PstDocType.FLD_DOCTYPE_OID] + ") FROM " + TBL_WF_DOC_TYPE;
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
			  	   DocType doctype = (DocType)list.get(ls);
				   if(oid == doctype.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    /**
    * this method used to check availability of document type
    * return TRUE if available otherwise FALSE
    */
    public static boolean cekDocType(int docType, long oid){
        DBResultSet dbrs = null;
        boolean bool = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOC_TYPE+
                	     " WHERE " + fieldNames[FLD_TYPE] + " = " + docType +
                         " AND " + fieldNames[FLD_DOCTYPE_OID] + " <> '" + oid + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()){
				bool = true;
            }
            rs.close();

    	}catch(Exception e){
    		System.out.println("Err : "+e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return bool;
    	}
    }

    /**
    * this method used to get docType data on specify Type
    */
    public static Vector getDocTypeData(int type){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
		try{
			String sql = "SELECT " + PstDocType.fieldNames[PstDocType.FLD_CODE] +
                		 ", " + PstDocType.fieldNames[PstDocType.FLD_NAME] +
                		 " FROM " + PstDocType.TBL_WF_DOC_TYPE +
		                 " WHERE " + PstDocType.fieldNames[PstDocType.FLD_TYPE] +
                         " = " + type;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                DocType docType = new DocType();
                docType.setCode(rs.getString(1));
                docType.setName(rs.getString(2));
                result.add(docType);
            }
    	}catch(Exception e){
    		System.out.println("Err : "+e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * return false if error
    **/
    public static boolean setDocumentStatus(int docType, Vector vector){
        if(vector==null || vector.size()==0){
            return true;
        }
        
        // than insert
        for(int i=0; i<vector.size(); i++) {
            DocStatus st = (DocStatus)vector.get(i);
            long oid = 0;
            try{
	            oid = PstDocStatus.insertExc(st);
            }catch(Exception e){
                System.out.println("Error insertExc DocTypeStatus");
            }

            if(oid==0){
                return false;
            }
        }         
        return true;
    }



    /*--------------------- start implements I_PstDocType --------------------*/
	/**
    * this method used to check if object docType already exist or net
    */
    public boolean existDocType(int iSysType, int iDocType){
        if((iSysType<0) || (iSysType>systemTypeNames.length)){
            System.out.println(" ERR: existDocType iSysType out of range");
            return false;
        }
             
        if((iDocType<0) || (iDocType>(documentTypeNames[iSysType]).length))  {
            System.out.println(" ERR: existDocType iDocType out of range");
            return false;
        }

        return true;        
    }

	/**
    * this method used to compose docType consist of system name and document name
    */
    public int composeDocumentType(int iSystemType, int iDocType){
        if(!existDocType(iSystemType,iDocType))
            return -1;
        
        return (iSystemType << SHIFT_SYSTEM) + iDocType;
    }

	/**
    * this method used to get index of system name from documentType(in 32 bit format)
    */
    public long getSystemIndex(int intDocumentType){
        int iSystemType = (intDocumentType & FILTER_SYSTEM) >> SHIFT_SYSTEM;
        if(iSystemType<0)
            return -1;

        return iSystemType;
    }

	/**
    * this method used to get index of system name from documentType(in 32 bit format)
    */
    public int getDocTypeIndex(int intDocumentType){
        int iDocType = (intDocumentType & FILTER_DOCTYPE);
        if(iDocType<0)
            return -1;

        return iDocType;
    }

    /**
    * this method used to get document code
    */
    public String getDocCode(int docType){
		 String result = "";
         String whereClause = fieldNames[FLD_TYPE] + " = " + docType;
         Vector vect = list(0,0,whereClause,"");
         if(vect!=null && vect.size()>0){
			DocType documentType = (DocType)vect.get(0);
            result = documentType.getCode();
         }
         return result;
    }

    /**
    * this method used to get document code
    */
    public String getDocTitle(int docType){
		 String result = "";
         String whereClause = fieldNames[FLD_TYPE] + " = " + docType;
         Vector vect = list(0,0,whereClause,"");
         if(vect!=null && vect.size()>0){
			DocType documentType = (DocType)vect.get(0);
            result = documentType.getName();
         }
         return result;
    }
    /*--------------------- end implements I_PstDocType --------------------*/


}
