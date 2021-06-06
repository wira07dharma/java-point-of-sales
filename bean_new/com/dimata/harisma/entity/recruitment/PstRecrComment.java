
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

public class PstRecrComment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_COMMENT = "HR_RECR_COMMENT";

	public static final  int FLD_RECR_COMMENT_ID = 0;
	public static final  int FLD_RECR_INTERVIEWER_ID = 1;
	public static final  int FLD_RECR_APPLICATION_ID = 2;
	public static final  int FLD_COMMENT = 3;

	public static final  String[] fieldNames = {
		"RECR_COMMENT_ID",
		"RECR_INTERVIEWER_ID",
		"RECR_APPLICATION_ID",
		"COMMENT"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstRecrComment(){
	}

	public PstRecrComment(int i) throws DBException { 
		super(new PstRecrComment()); 
	}

	public PstRecrComment(String sOid) throws DBException { 
		super(new PstRecrComment(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrComment(long lOid) throws DBException { 
		super(new PstRecrComment(0)); 
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
		return TBL_HR_RECR_COMMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrComment().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrComment recrcomment = fetchExc(ent.getOID()); 
		ent = (Entity)recrcomment; 
		return recrcomment.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrComment) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrComment) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrComment fetchExc(long oid) throws DBException{ 
		try{ 
			RecrComment recrcomment = new RecrComment();
			PstRecrComment pstRecrComment = new PstRecrComment(oid); 
			recrcomment.setOID(oid);

			recrcomment.setRecrInterviewerId(pstRecrComment.getlong(FLD_RECR_INTERVIEWER_ID));
			recrcomment.setRecrApplicationId(pstRecrComment.getlong(FLD_RECR_APPLICATION_ID));
			recrcomment.setComment(pstRecrComment.getString(FLD_COMMENT));

			return recrcomment; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrComment(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrComment recrcomment) throws DBException{ 
		try{ 
			PstRecrComment pstRecrComment = new PstRecrComment(0);

			pstRecrComment.setLong(FLD_RECR_INTERVIEWER_ID, recrcomment.getRecrInterviewerId());
			pstRecrComment.setLong(FLD_RECR_APPLICATION_ID, recrcomment.getRecrApplicationId());
			pstRecrComment.setString(FLD_COMMENT, recrcomment.getComment());

			pstRecrComment.insert(); 
			recrcomment.setOID(pstRecrComment.getlong(FLD_RECR_COMMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrComment(0),DBException.UNKNOWN); 
		}
		return recrcomment.getOID();
	}

	public static long updateExc(RecrComment recrcomment) throws DBException{ 
		try{ 
			if(recrcomment.getOID() != 0){ 
				PstRecrComment pstRecrComment = new PstRecrComment(recrcomment.getOID());

				pstRecrComment.setLong(FLD_RECR_INTERVIEWER_ID, recrcomment.getRecrInterviewerId());
				pstRecrComment.setLong(FLD_RECR_APPLICATION_ID, recrcomment.getRecrApplicationId());
				pstRecrComment.setString(FLD_COMMENT, recrcomment.getComment());

				pstRecrComment.update(); 
				return recrcomment.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrComment(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrComment pstRecrComment = new PstRecrComment(oid);
			pstRecrComment.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrComment(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_COMMENT; 
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
				RecrComment recrcomment = new RecrComment();
				resultToObject(rs, recrcomment);
				lists.add(recrcomment);
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

	public static void resultToObject(ResultSet rs, RecrComment recrcomment){
		try{
			recrcomment.setOID(rs.getLong(PstRecrComment.fieldNames[PstRecrComment.FLD_RECR_COMMENT_ID]));
			recrcomment.setRecrInterviewerId(rs.getLong(PstRecrComment.fieldNames[PstRecrComment.FLD_RECR_INTERVIEWER_ID]));
			recrcomment.setRecrApplicationId(rs.getLong(PstRecrComment.fieldNames[PstRecrComment.FLD_RECR_APPLICATION_ID]));
			recrcomment.setComment(rs.getString(PstRecrComment.fieldNames[PstRecrComment.FLD_COMMENT]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrCommentId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_COMMENT + " WHERE " + 
						PstRecrComment.fieldNames[PstRecrComment.FLD_RECR_COMMENT_ID] + " = " + recrCommentId;

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
			String sql = "SELECT COUNT("+ PstRecrComment.fieldNames[PstRecrComment.FLD_RECR_COMMENT_ID] + ") FROM " + TBL_HR_RECR_COMMENT;
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
			  	   RecrComment recrcomment = (RecrComment)list.get(ls);
				   if(oid == recrcomment.getOID())
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
            String sql = " DELETE FROM "+PstRecrComment.TBL_HR_RECR_COMMENT+
                         " WHERE "+PstRecrComment.fieldNames[PstRecrComment.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
