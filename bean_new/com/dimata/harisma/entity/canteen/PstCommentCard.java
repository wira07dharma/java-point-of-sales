
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

package com.dimata.harisma.entity.canteen; 

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

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
import com.dimata.harisma.entity.canteen.*; 

public class PstCommentCard extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_COMMENT_CARD = "HR_COMMENT_CARD";

	public static final  int FLD_COMMENT_CARD_ID = 0;
	public static final  int FLD_CHECKLIST_MARK_ID = 1;
	public static final  int FLD_COMMENT_CARD_HEADER_ID = 2;
	public static final  int FLD_CARD_QUESTION_ID = 3;
	public static final  int FLD_REMARK = 4;

	public static final  String[] fieldNames = {
		"COMMENT_CARD_ID",
		"CHECKLIST_MARK_ID",
		"COMMENT_CARD_HEADER_ID",
		"CARD_QUESTION_ID",
		"REMARK"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING
	 }; 

	public PstCommentCard(){
	}

	public PstCommentCard(int i) throws DBException { 
		super(new PstCommentCard()); 
	}

	public PstCommentCard(String sOid) throws DBException { 
		super(new PstCommentCard(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstCommentCard(long lOid) throws DBException { 
		super(new PstCommentCard(0)); 
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
		return TBL_HR_COMMENT_CARD;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstCommentCard().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		CommentCard commentcard = fetchExc(ent.getOID()); 
		ent = (Entity)commentcard; 
		return commentcard.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((CommentCard) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((CommentCard) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static CommentCard fetchExc(long oid) throws DBException{ 
		try{ 
			CommentCard commentcard = new CommentCard();
			PstCommentCard pstCommentCard = new PstCommentCard(oid); 
			commentcard.setOID(oid);

			commentcard.setChecklistMarkId(pstCommentCard.getlong(FLD_CHECKLIST_MARK_ID));
			commentcard.setCommentCardHeaderId(pstCommentCard.getlong(FLD_COMMENT_CARD_HEADER_ID));
			commentcard.setCardQuestionId(pstCommentCard.getlong(FLD_CARD_QUESTION_ID));
			commentcard.setRemark(pstCommentCard.getString(FLD_REMARK));

			return commentcard; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCommentCard(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(CommentCard commentcard) throws DBException{ 
		try{ 
			PstCommentCard pstCommentCard = new PstCommentCard(0);

			pstCommentCard.setLong(FLD_CHECKLIST_MARK_ID, commentcard.getChecklistMarkId());
			pstCommentCard.setLong(FLD_COMMENT_CARD_HEADER_ID, commentcard.getCommentCardHeaderId());
			pstCommentCard.setLong(FLD_CARD_QUESTION_ID, commentcard.getCardQuestionId());
			pstCommentCard.setString(FLD_REMARK, commentcard.getRemark());

			pstCommentCard.insert(); 
			commentcard.setOID(pstCommentCard.getlong(FLD_COMMENT_CARD_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCommentCard(0),DBException.UNKNOWN); 
		}
		return commentcard.getOID();
	}

	public static long updateExc(CommentCard commentcard) throws DBException{ 
		try{ 
			if(commentcard.getOID() != 0){ 
				PstCommentCard pstCommentCard = new PstCommentCard(commentcard.getOID());

				pstCommentCard.setLong(FLD_CHECKLIST_MARK_ID, commentcard.getChecklistMarkId());
				pstCommentCard.setLong(FLD_COMMENT_CARD_HEADER_ID, commentcard.getCommentCardHeaderId());
				pstCommentCard.setLong(FLD_CARD_QUESTION_ID, commentcard.getCardQuestionId());
				pstCommentCard.setString(FLD_REMARK, commentcard.getRemark());

				pstCommentCard.update(); 
				return commentcard.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCommentCard(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstCommentCard pstCommentCard = new PstCommentCard(oid);
			pstCommentCard.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstCommentCard(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_COMMENT_CARD; 
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
				CommentCard commentcard = new CommentCard();
				resultToObject(rs, commentcard);
				lists.add(commentcard);
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

	public static void resultToObject(ResultSet rs, CommentCard commentcard){
		try{
			commentcard.setOID(rs.getLong(PstCommentCard.fieldNames[PstCommentCard.FLD_COMMENT_CARD_ID]));
			commentcard.setChecklistMarkId(rs.getLong(PstCommentCard.fieldNames[PstCommentCard.FLD_CHECKLIST_MARK_ID]));
			commentcard.setCommentCardHeaderId(rs.getLong(PstCommentCard.fieldNames[PstCommentCard.FLD_COMMENT_CARD_HEADER_ID]));
			commentcard.setCardQuestionId(rs.getLong(PstCommentCard.fieldNames[PstCommentCard.FLD_CARD_QUESTION_ID]));
			commentcard.setRemark(rs.getString(PstCommentCard.fieldNames[PstCommentCard.FLD_REMARK]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long commentCardId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_COMMENT_CARD + " WHERE " + 
						PstCommentCard.fieldNames[PstCommentCard.FLD_COMMENT_CARD_ID] + " = " + commentCardId;

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
			String sql = "SELECT COUNT("+ PstCommentCard.fieldNames[PstCommentCard.FLD_COMMENT_CARD_ID] + ") FROM " + TBL_HR_COMMENT_CARD;
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
			  	   CommentCard commentcard = (CommentCard)list.get(ls);
				   if(oid == commentcard.getOID())
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
