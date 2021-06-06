
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

package com.dimata.posbo.entity.masterdata; 

/* package java */ 
import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
import com.dimata.posbo.entity.masterdata.*; 
import org.json.JSONObject;

public class PstMemberRegistrationHistory extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	//public static final  String TBL_MEMBER_REGISTRATION_HISTORY = "MEMBER_REGISTRATION_HISTORY";
        public static final  String TBL_MEMBER_REGISTRATION_HISTORY = "member_registration_history";

	public static final  int FLD_MEMBER_REGISTRATION_HISTORY_ID = 0;
	public static final  int FLD_MEMBER_ID = 1;
	public static final  int FLD_REGISTRATION_DATE = 2;
	public static final  int FLD_VALID_START_DATE = 3;
	public static final  int FLD_VALID_EXPIRED_DATE = 4;

	public static final  String[] fieldNames = {
		"MEMBER_REGISTRATION_HISTORY_ID",
		"MEMBER_ID",
		"REGISTRATION_DATE",
		"VALID_START_DATE",
		"VALID_EXPIRED_DATE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_DATE,
		TYPE_DATE
	 }; 

	public PstMemberRegistrationHistory(){
	}

	public PstMemberRegistrationHistory(int i) throws DBException { 
		super(new PstMemberRegistrationHistory()); 
	}

	public PstMemberRegistrationHistory(String sOid) throws DBException { 
		super(new PstMemberRegistrationHistory(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstMemberRegistrationHistory(long lOid) throws DBException { 
		super(new PstMemberRegistrationHistory(0)); 
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
		return TBL_MEMBER_REGISTRATION_HISTORY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstMemberRegistrationHistory().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		MemberRegistrationHistory memberregistrationhistory = fetchExc(ent.getOID()); 
		ent = (Entity)memberregistrationhistory; 
		return memberregistrationhistory.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((MemberRegistrationHistory) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((MemberRegistrationHistory) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static MemberRegistrationHistory fetchExc(long oid) throws DBException{ 
		try{ 
			MemberRegistrationHistory memberregistrationhistory = new MemberRegistrationHistory();
			PstMemberRegistrationHistory pstMemberRegistrationHistory = new PstMemberRegistrationHistory(oid); 
			memberregistrationhistory.setOID(oid);

			memberregistrationhistory.setMemberId(pstMemberRegistrationHistory.getlong(FLD_MEMBER_ID));
			memberregistrationhistory.setRegistrationDate(pstMemberRegistrationHistory.getDate(FLD_REGISTRATION_DATE));
			memberregistrationhistory.setValidStartDate(pstMemberRegistrationHistory.getDate(FLD_VALID_START_DATE));
			memberregistrationhistory.setValidExpiredDate(pstMemberRegistrationHistory.getDate(FLD_VALID_EXPIRED_DATE));

			return memberregistrationhistory; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegistrationHistory(0),DBException.UNKNOWN); 
		} 
	}

    public static long insertExc(MemberRegistrationHistory memberregistrationhistory) throws DBException{
		try{ 
			PstMemberRegistrationHistory pstMemberRegistrationHistory = new PstMemberRegistrationHistory(0);

			pstMemberRegistrationHistory.setLong(FLD_MEMBER_ID, memberregistrationhistory.getMemberId());
			pstMemberRegistrationHistory.setDate(FLD_REGISTRATION_DATE, memberregistrationhistory.getRegistrationDate());
			pstMemberRegistrationHistory.setDate(FLD_VALID_START_DATE, memberregistrationhistory.getValidStartDate());
			pstMemberRegistrationHistory.setDate(FLD_VALID_EXPIRED_DATE, memberregistrationhistory.getValidExpiredDate());

			pstMemberRegistrationHistory.insert();

                        long oidDataSync=PstDataSyncSql.insertExc(pstMemberRegistrationHistory.getInsertSQL());
                        PstDataSyncStatus.insertExc(oidDataSync);
			memberregistrationhistory.setOID(pstMemberRegistrationHistory.getlong(FLD_MEMBER_REGISTRATION_HISTORY_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMemberRegistrationHistory.getInsertSQL());
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegistrationHistory(0),DBException.UNKNOWN); 
		}
		return memberregistrationhistory.getOID();
	}

	public static long updateExc(MemberRegistrationHistory memberregistrationhistory) throws DBException{ 
		try{ 
			if(memberregistrationhistory.getOID() != 0){ 
				PstMemberRegistrationHistory pstMemberRegistrationHistory = new PstMemberRegistrationHistory(memberregistrationhistory.getOID());

				pstMemberRegistrationHistory.setLong(FLD_MEMBER_ID, memberregistrationhistory.getMemberId());
				pstMemberRegistrationHistory.setDate(FLD_REGISTRATION_DATE, memberregistrationhistory.getRegistrationDate());
				pstMemberRegistrationHistory.setDate(FLD_VALID_START_DATE, memberregistrationhistory.getValidStartDate());
				pstMemberRegistrationHistory.setDate(FLD_VALID_EXPIRED_DATE, memberregistrationhistory.getValidExpiredDate());

				pstMemberRegistrationHistory.update();

                                long oidDataSync=PstDataSyncSql.insertExc(pstMemberRegistrationHistory.getUpdateSQL());
                                PstDataSyncStatus.insertExc(oidDataSync);
								//kebutuhan untuk service transfer katalog
								PstDataCustom.insertDataForSyncAllLocation(pstMemberRegistrationHistory.getUpdateSQL());
				return memberregistrationhistory.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegistrationHistory(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstMemberRegistrationHistory pstMemberRegistrationHistory = new PstMemberRegistrationHistory(oid);
			pstMemberRegistrationHistory.delete();

                        long oidDataSync = PstDataSyncSql.insertExc(pstMemberRegistrationHistory.getDeleteSQL());
                        PstDataSyncStatus.insertExc(oidDataSync);
						//kebutuhan untuk service transfer katalog
						PstDataCustom.insertDataForSyncAllLocation(pstMemberRegistrationHistory.getDeleteSQL());
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegistrationHistory(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_MEMBER_REGISTRATION_HISTORY; 
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
				MemberRegistrationHistory memberregistrationhistory = new MemberRegistrationHistory();
				resultToObject(rs, memberregistrationhistory);
				lists.add(memberregistrationhistory);
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

	private static void resultToObject(ResultSet rs, MemberRegistrationHistory memberregistrationhistory){
		try{
			memberregistrationhistory.setOID(rs.getLong(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_REGISTRATION_HISTORY_ID]));
			memberregistrationhistory.setMemberId(rs.getLong(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID]));
			memberregistrationhistory.setRegistrationDate(rs.getDate(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_REGISTRATION_DATE]));
			memberregistrationhistory.setValidStartDate(rs.getDate(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_START_DATE]));
			memberregistrationhistory.setValidExpiredDate(rs.getDate(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long memberRegistrationHistoryId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_MEMBER_REGISTRATION_HISTORY + " WHERE " + 
						PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_REGISTRATION_HISTORY_ID] + " = " + memberRegistrationHistoryId;

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
			String sql = "SELECT COUNT("+ PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_REGISTRATION_HISTORY_ID] + ") FROM " + TBL_MEMBER_REGISTRATION_HISTORY;
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
			  	   MemberRegistrationHistory memberregistrationhistory = (MemberRegistrationHistory)list.get(ls);
				   if(oid == memberregistrationhistory.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
        
        public static void deleteByIdMember(long oidMember){            
            try{
                String sql = " DELETE  FROM "+PstMemberRegistrationHistory.TBL_MEMBER_REGISTRATION_HISTORY+
                             " WHERE "+PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory. FLD_MEMBER_ID]+
                             " = "+oidMember;
                
                int delete = DBHandler.execUpdate(sql);

                long oidDataSync = PstDataSyncSql.insertExc(sql);
                PstDataSyncStatus.insertExc(oidDataSync);
            }catch(Exception e){
                e.printStackTrace();                
                System.out.println("err on delete member >>> : "+e.toString());
            }
        }
     public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MemberRegistrationHistory memberRegistrationHistory = PstMemberRegistrationHistory.fetchExc(oid);
                object.put(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_REGISTRATION_HISTORY_ID], memberRegistrationHistory.getOID());
                object.put(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID], memberRegistrationHistory.getMemberId());
                object.put(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_REGISTRATION_DATE], memberRegistrationHistory.getRegistrationDate());
                object.put(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE], memberRegistrationHistory.getValidExpiredDate());
                object.put(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_START_DATE], memberRegistrationHistory.getValidStartDate());
            }catch(Exception exc){}

            return object;
        }
}
