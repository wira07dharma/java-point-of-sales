
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
import com.dimata.harisma.entity.recruitment.*; 

public class PstRecrAnswerIllness extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_ANSWER_ILLNESS = "HR_RECR_ANSWER_ILLNESS";

	public static final  int FLD_RECR_ANSWER_ILLNESS_ID = 0;
	public static final  int FLD_RECR_ILLNESS_ID = 1;
	public static final  int FLD_RECR_APPLICATION_ID = 2;
	public static final  int FLD_ANSWER = 3;

	public static final  String[] fieldNames = {
		"RECR_ANSWER_ILLNESS_ID",
		"RECR_ILLNESS_ID",
		"RECR_APPLICATION_ID",
		"ANSWER"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT
	 }; 

        //gender----
        public static final int YES 	= 1;
        public static final int NO 	= 2;
        public static final String[] illnessKey = {"Yes","No"};
        public static final int[] illnessValue = {1,2};

	public PstRecrAnswerIllness(){
	}

	public PstRecrAnswerIllness(int i) throws DBException { 
		super(new PstRecrAnswerIllness()); 
	}

	public PstRecrAnswerIllness(String sOid) throws DBException { 
		super(new PstRecrAnswerIllness(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrAnswerIllness(long lOid) throws DBException { 
		super(new PstRecrAnswerIllness(0)); 
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
		return TBL_HR_RECR_ANSWER_ILLNESS;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrAnswerIllness().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrAnswerIllness recranswerillness = fetchExc(ent.getOID()); 
		ent = (Entity)recranswerillness; 
		return recranswerillness.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrAnswerIllness) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrAnswerIllness) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrAnswerIllness fetchExc(long oid) throws DBException{ 
		try{ 
			RecrAnswerIllness recranswerillness = new RecrAnswerIllness();
			PstRecrAnswerIllness pstRecrAnswerIllness = new PstRecrAnswerIllness(oid); 
			recranswerillness.setOID(oid);

			recranswerillness.setRecrIllnessId(pstRecrAnswerIllness.getlong(FLD_RECR_ILLNESS_ID));
			recranswerillness.setRecrApplicationId(pstRecrAnswerIllness.getlong(FLD_RECR_APPLICATION_ID));
			recranswerillness.setAnswer(pstRecrAnswerIllness.getInt(FLD_ANSWER));

			return recranswerillness; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrAnswerIllness(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrAnswerIllness recranswerillness) throws DBException{ 
		try{ 
			PstRecrAnswerIllness pstRecrAnswerIllness = new PstRecrAnswerIllness(0);

			pstRecrAnswerIllness.setLong(FLD_RECR_ILLNESS_ID, recranswerillness.getRecrIllnessId());
			pstRecrAnswerIllness.setLong(FLD_RECR_APPLICATION_ID, recranswerillness.getRecrApplicationId());
			pstRecrAnswerIllness.setInt(FLD_ANSWER, recranswerillness.getAnswer());

			pstRecrAnswerIllness.insert(); 
			recranswerillness.setOID(pstRecrAnswerIllness.getlong(FLD_RECR_ANSWER_ILLNESS_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrAnswerIllness(0),DBException.UNKNOWN); 
		}
		return recranswerillness.getOID();
	}

	public static long updateExc(RecrAnswerIllness recranswerillness) throws DBException{ 
		try{ 
			if(recranswerillness.getOID() != 0){ 
				PstRecrAnswerIllness pstRecrAnswerIllness = new PstRecrAnswerIllness(recranswerillness.getOID());

				pstRecrAnswerIllness.setLong(FLD_RECR_ILLNESS_ID, recranswerillness.getRecrIllnessId());
				pstRecrAnswerIllness.setLong(FLD_RECR_APPLICATION_ID, recranswerillness.getRecrApplicationId());
				pstRecrAnswerIllness.setInt(FLD_ANSWER, recranswerillness.getAnswer());

				pstRecrAnswerIllness.update(); 
				return recranswerillness.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrAnswerIllness(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrAnswerIllness pstRecrAnswerIllness = new PstRecrAnswerIllness(oid);
			pstRecrAnswerIllness.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrAnswerIllness(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_RECR_ANSWER_ILLNESS; 
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
				RecrAnswerIllness recranswerillness = new RecrAnswerIllness();
				resultToObject(rs, recranswerillness);
				lists.add(recranswerillness);
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

	public static void resultToObject(ResultSet rs, RecrAnswerIllness recranswerillness){
		try{
			recranswerillness.setOID(rs.getLong(PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_RECR_ANSWER_ILLNESS_ID]));
			recranswerillness.setRecrIllnessId(rs.getLong(PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_RECR_ILLNESS_ID]));
			recranswerillness.setRecrApplicationId(rs.getLong(PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_RECR_APPLICATION_ID]));
			recranswerillness.setAnswer(rs.getInt(PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_ANSWER]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrAnswerIllnessId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_ANSWER_ILLNESS + " WHERE " + 
						PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_RECR_ANSWER_ILLNESS_ID] + " = " + recrAnswerIllnessId;

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
			String sql = "SELECT COUNT("+ PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_RECR_ANSWER_ILLNESS_ID] + ") FROM " + TBL_HR_RECR_ANSWER_ILLNESS;
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
			  	   RecrAnswerIllness recranswerillness = (RecrAnswerIllness)list.get(ls);
				   if(oid == recranswerillness.getOID())
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
            String sql = " DELETE FROM "+PstRecrAnswerIllness.TBL_HR_RECR_ANSWER_ILLNESS+
                         " WHERE "+PstRecrAnswerIllness.fieldNames[PstRecrAnswerIllness.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}

    	return raOID;
    }
}
