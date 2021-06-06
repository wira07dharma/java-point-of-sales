
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
import java.sql.*
;import java.util.*
;
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

public class PstRecrReferences extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_REFERENCES = "HR_RECR_REFERENCES";

	public static final  int FLD_RECR_REFERENCES_ID = 0;
	public static final  int FLD_RECR_APPLICATION_ID = 1;
	public static final  int FLD_NAME = 2;
	public static final  int FLD_COMPANY = 3;
	public static final  int FLD_POSITION = 4;

	public static final  String[] fieldNames = {
		"RECR_REFERENCES_ID",
		"RECR_APPLICATION_ID",
		"NAME",
		"COMPANY",
		"POSITION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING
	 }; 

	public PstRecrReferences(){
	}

	public PstRecrReferences(int i) throws DBException { 
		super(new PstRecrReferences()); 
	}

	public PstRecrReferences(String sOid) throws DBException { 
		super(new PstRecrReferences(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrReferences(long lOid) throws DBException { 
		super(new PstRecrReferences(0)); 
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
		return TBL_HR_RECR_REFERENCES;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrReferences().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrReferences recrreferences = fetchExc(ent.getOID()); 
		ent = (Entity)recrreferences; 
		return recrreferences.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrReferences) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrReferences) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrReferences fetchExc(long oid) throws DBException{ 
		try{ 
			RecrReferences recrreferences = new RecrReferences();
			PstRecrReferences pstRecrReferences = new PstRecrReferences(oid); 
			recrreferences.setOID(oid);

			recrreferences.setRecrApplicationId(pstRecrReferences.getlong(FLD_RECR_APPLICATION_ID));
			recrreferences.setName(pstRecrReferences.getString(FLD_NAME));
			recrreferences.setCompany(pstRecrReferences.getString(FLD_COMPANY));
			recrreferences.setPosition(pstRecrReferences.getString(FLD_POSITION));

			return recrreferences; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrReferences(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrReferences recrreferences) throws DBException{ 
		try{ 
			PstRecrReferences pstRecrReferences = new PstRecrReferences(0);

			pstRecrReferences.setLong(FLD_RECR_APPLICATION_ID, recrreferences.getRecrApplicationId());
			pstRecrReferences.setString(FLD_NAME, recrreferences.getName());
			pstRecrReferences.setString(FLD_COMPANY, recrreferences.getCompany());
			pstRecrReferences.setString(FLD_POSITION, recrreferences.getPosition());

			pstRecrReferences.insert(); 
			recrreferences.setOID(pstRecrReferences.getlong(FLD_RECR_REFERENCES_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrReferences(0),DBException.UNKNOWN); 
		}
		return recrreferences.getOID();
	}

	public static long updateExc(RecrReferences recrreferences) throws DBException{ 
		try{ 
			if(recrreferences.getOID() != 0){ 
				PstRecrReferences pstRecrReferences = new PstRecrReferences(recrreferences.getOID());

				pstRecrReferences.setLong(FLD_RECR_APPLICATION_ID, recrreferences.getRecrApplicationId());
				pstRecrReferences.setString(FLD_NAME, recrreferences.getName());
				pstRecrReferences.setString(FLD_COMPANY, recrreferences.getCompany());
				pstRecrReferences.setString(FLD_POSITION, recrreferences.getPosition());

				pstRecrReferences.update(); 
				return recrreferences.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrReferences(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrReferences pstRecrReferences = new PstRecrReferences(oid);
			pstRecrReferences.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrReferences(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_REFERENCES; 
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
				RecrReferences recrreferences = new RecrReferences();
				resultToObject(rs, recrreferences);
				lists.add(recrreferences);
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

	public static void resultToObject(ResultSet rs, RecrReferences recrreferences){
		try{
			recrreferences.setOID(rs.getLong(PstRecrReferences.fieldNames[PstRecrReferences.FLD_RECR_REFERENCES_ID]));
			recrreferences.setRecrApplicationId(rs.getLong(PstRecrReferences.fieldNames[PstRecrReferences.FLD_RECR_APPLICATION_ID]));
			recrreferences.setName(rs.getString(PstRecrReferences.fieldNames[PstRecrReferences.FLD_NAME]));
			recrreferences.setCompany(rs.getString(PstRecrReferences.fieldNames[PstRecrReferences.FLD_COMPANY]));
			recrreferences.setPosition(rs.getString(PstRecrReferences.fieldNames[PstRecrReferences.FLD_POSITION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrReferencesId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_REFERENCES + " WHERE " + 
						PstRecrReferences.fieldNames[PstRecrReferences.FLD_RECR_REFERENCES_ID] + " = " + recrReferencesId;

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
			String sql = "SELECT COUNT("+ PstRecrReferences.fieldNames[PstRecrReferences.FLD_RECR_REFERENCES_ID] + ") FROM " + TBL_HR_RECR_REFERENCES;
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
			  	   RecrReferences recrreferences = (RecrReferences)list.get(ls);
				   if(oid == recrreferences.getOID())
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

    public static long deleteByRecrApplication(long raOID)
    {
    	try{
            String sql = " DELETE FROM "+PstRecrReferences.TBL_HR_RECR_REFERENCES+
                         " WHERE "+PstRecrReferences.fieldNames[PstRecrReferences.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
