
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

public class PstOriChecklist extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_ORI_CHECKLIST = "HR_ORI_CHECKLIST";

	public static final  int FLD_ORI_CHECKLIST_ID = 0;
	public static final  int FLD_RECR_APPLICATION_ID = 1;
	public static final  int FLD_INTERVIEWER_ID = 2;
	public static final  int FLD_SIGNATURE_DATE = 3;
	public static final  int FLD_INTERVIEW_DATE = 4;
	public static final  int FLD_SKILLS = 5;

	public static final  String[] fieldNames = {
		"ORI_CHECKLIST_ID",
		"RECR_APPLICATION_ID",
		"INTERVIEWER_ID",
		"SIGNATURE_DATE",
		"INTERVIEW_DATE",
                "SKILLS"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
                TYPE_STRING
	 }; 

	public PstOriChecklist(){
	}

	public PstOriChecklist(int i) throws DBException { 
		super(new PstOriChecklist()); 
	}

	public PstOriChecklist(String sOid) throws DBException { 
		super(new PstOriChecklist(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstOriChecklist(long lOid) throws DBException { 
		super(new PstOriChecklist(0)); 
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
		return TBL_HR_ORI_CHECKLIST;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstOriChecklist().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		OriChecklist orichecklist = fetchExc(ent.getOID()); 
		ent = (Entity)orichecklist; 
		return orichecklist.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((OriChecklist) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((OriChecklist) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static OriChecklist fetchExc(long oid) throws DBException{ 
		try{ 
			OriChecklist orichecklist = new OriChecklist();
			PstOriChecklist pstOriChecklist = new PstOriChecklist(oid); 
			orichecklist.setOID(oid);

			orichecklist.setRecrApplicationId(pstOriChecklist.getlong(FLD_RECR_APPLICATION_ID));
			orichecklist.setInterviewerId(pstOriChecklist.getlong(FLD_INTERVIEWER_ID));
			orichecklist.setSignatureDate(pstOriChecklist.getDate(FLD_SIGNATURE_DATE));
			orichecklist.setInterviewDate(pstOriChecklist.getDate(FLD_INTERVIEW_DATE));
                        orichecklist.setSkills(pstOriChecklist.getString(FLD_SKILLS));

			return orichecklist; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklist(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(OriChecklist orichecklist) throws DBException{ 
		try{ 
			PstOriChecklist pstOriChecklist = new PstOriChecklist(0);

			pstOriChecklist.setLong(FLD_RECR_APPLICATION_ID, orichecklist.getRecrApplicationId());
			pstOriChecklist.setLong(FLD_INTERVIEWER_ID, orichecklist.getInterviewerId());
			pstOriChecklist.setDate(FLD_SIGNATURE_DATE, orichecklist.getSignatureDate());
			pstOriChecklist.setDate(FLD_INTERVIEW_DATE, orichecklist.getInterviewDate());
                        pstOriChecklist.setString(FLD_SKILLS, orichecklist.getSkills());

			pstOriChecklist.insert(); 
			orichecklist.setOID(pstOriChecklist.getlong(FLD_ORI_CHECKLIST_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklist(0),DBException.UNKNOWN); 
		}
		return orichecklist.getOID();
	}

	public static long updateExc(OriChecklist orichecklist) throws DBException{ 
		try{ 
			if(orichecklist.getOID() != 0){ 
				PstOriChecklist pstOriChecklist = new PstOriChecklist(orichecklist.getOID());

				pstOriChecklist.setLong(FLD_RECR_APPLICATION_ID, orichecklist.getRecrApplicationId());
				pstOriChecklist.setLong(FLD_INTERVIEWER_ID, orichecklist.getInterviewerId());
				pstOriChecklist.setDate(FLD_SIGNATURE_DATE, orichecklist.getSignatureDate());
				pstOriChecklist.setDate(FLD_INTERVIEW_DATE, orichecklist.getInterviewDate());
                                pstOriChecklist.setString(FLD_SKILLS, orichecklist.getSkills());

				pstOriChecklist.update(); 
				return orichecklist.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklist(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstOriChecklist pstOriChecklist = new PstOriChecklist(oid);
			pstOriChecklist.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstOriChecklist(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_ORI_CHECKLIST; 
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
				OriChecklist orichecklist = new OriChecklist();
				resultToObject(rs, orichecklist);
				lists.add(orichecklist);
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

	public static void resultToObject(ResultSet rs, OriChecklist orichecklist){
		try{
			orichecklist.setOID(rs.getLong(PstOriChecklist.fieldNames[PstOriChecklist.FLD_ORI_CHECKLIST_ID]));
			orichecklist.setRecrApplicationId(rs.getLong(PstOriChecklist.fieldNames[PstOriChecklist.FLD_RECR_APPLICATION_ID]));
			orichecklist.setInterviewerId(rs.getLong(PstOriChecklist.fieldNames[PstOriChecklist.FLD_INTERVIEWER_ID]));
			orichecklist.setSignatureDate(rs.getDate(PstOriChecklist.fieldNames[PstOriChecklist.FLD_SIGNATURE_DATE]));
			orichecklist.setInterviewDate(rs.getDate(PstOriChecklist.fieldNames[PstOriChecklist.FLD_INTERVIEW_DATE]));
                        orichecklist.setSkills(rs.getString(PstOriChecklist.fieldNames[PstOriChecklist.FLD_SKILLS]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long oriChecklistId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_ORI_CHECKLIST + " WHERE " + 
						PstOriChecklist.fieldNames[PstOriChecklist.FLD_ORI_CHECKLIST_ID] + " = " + oriChecklistId;

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
			String sql = "SELECT COUNT("+ PstOriChecklist.fieldNames[PstOriChecklist.FLD_ORI_CHECKLIST_ID] + ") FROM " + TBL_HR_ORI_CHECKLIST;
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
			  	   OriChecklist orichecklist = (OriChecklist)list.get(ls);
				   if(oid == orichecklist.getOID())
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
        /* proses deleting data di HR_ORI_CHECKLIST_ACTIVITY */
        long ocOID = 0;
        Vector voc = new Vector(1,1);
        
        try {
            voc = PstOriChecklist.list(0, 0, PstOriChecklist.fieldNames[PstOriChecklist.FLD_RECR_APPLICATION_ID] +"="+ raOID, "");
        }
        catch (Exception e) {
        }
        System.out.println(" - voc.size() = " + voc.size());
        
        if (voc.size() > 0) {
            for (int i=0; i<voc.size(); i++) {
                OriChecklist oc = (OriChecklist) voc.get(0);
                ocOID = PstOriChecklistActivity.deleteByOriChecklist(oc.getOID());
                System.out.println(" - deleteByOriChecklist(OID) with oid = " + oc.getOID());
            }
        }
        /* end of process deleting -----------------------------*/
        
    	try{
            String sql = " DELETE FROM "+PstOriChecklist.TBL_HR_ORI_CHECKLIST+
                         " WHERE "+PstOriChecklist.fieldNames[PstOriChecklist.FLD_RECR_APPLICATION_ID] +
                         " = "+raOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete experience by employee "+exc.toString());
    	}
        
    	return raOID;
    }
}
