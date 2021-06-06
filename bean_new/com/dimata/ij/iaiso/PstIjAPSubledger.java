/*
 * PstIjAPSubledger.java
 *
 * Created on January 29, 2005, 6:08 AM
 */

package com.dimata.ij.iaiso;

// package java 
import java.sql.*;
import java.util.*;

// package qdep
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;

// package ij
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;




/**
 *
 * @author  Administrator
 * @version 
 */
public class PstIjAPSubledger extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final String TBL_IJ_AP_SUBLEDGER = "ij_ap_subledger";

	public static final int FLD_IJ_AP_SUBLEDGER_ID = 0;
        public static final int FLD_PARENT_ID = 1;
	public static final int FLD_JOURNAL_DETAIL_ID = 2;
        public static final int FLD_TRANSACTION_DATE = 3;
        public static final int FLD_EXPIRED_DATE = 4;
        public static final int FLD_BILL_NUMBER = 5;
        public static final int FLD_ACCOUNT_CHART_ID = 6;
        public static final int FLD_CONTACT_ID = 7;
        public static final int FLD_DEBT_VALUE = 8;
        public static final int FLD_CREDIT_VALUE = 9;
        public static final int FLD_PAID_STATUS = 10;
        public static final int FLD_TRANSACTION_CURRENCY = 11;
        public static final int FLD_TRANSACTION_RATE = 12;

	public static final String[] fieldNames = {
		"IJ_AP_SUBLEDGER_ID",
		"PARENT_ID",
                "JOURNAL_DETAIL_ID",
                "TRANSACTION_DATE",
                "EXPIRED_DATE",
                "BILL_NUMBER",
                "ACCOUNT_CHART_ID",
                "CONTACT_ID",
                "DEBT_VALUE",
                "CREDIT_VALUE",
                "PAID_STATUS",
                "TRANSACTION_CURRENCY",
                "TRANSACTION_RATE"
	 }; 

	public static final int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_LONG,
		TYPE_LONG,
                TYPE_DATE,
                TYPE_DATE,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_LONG,
		TYPE_FLOAT,
		TYPE_FLOAT,
                TYPE_INT,
                TYPE_LONG,
                TYPE_FLOAT
	 }; 

	public PstIjAPSubledger(){
	}

	public PstIjAPSubledger(int i) throws DBException { 
		super(new PstIjAPSubledger()); 
	}

	public PstIjAPSubledger(String sOid) throws DBException { 
		super(new PstIjAPSubledger()); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstIjAPSubledger(long lOid) throws DBException { 
		super(new PstIjAPSubledger()); 
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
		return TBL_IJ_AP_SUBLEDGER;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstIjAPSubledger().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		IjAPSubledger ijapsubledger = fetchExc(ent.getOID()); 
		ent = (Entity)ijapsubledger; 
		return ijapsubledger.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((IjAPSubledger) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((IjAPSubledger) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static IjAPSubledger fetchExc(long oid) throws DBException{ 
		try{ 
			IjAPSubledger ijapsubledger = new IjAPSubledger();
			PstIjAPSubledger pstIjAPSubledger = new PstIjAPSubledger(oid); 
			ijapsubledger.setOID(oid);

                        ijapsubledger.setApParent(pstIjAPSubledger.getlong(FLD_PARENT_ID));
			ijapsubledger.setApDetailOid(pstIjAPSubledger.getlong(FLD_JOURNAL_DETAIL_ID));
                        ijapsubledger.setApTransDate(pstIjAPSubledger.getDate(FLD_TRANSACTION_DATE));
                        ijapsubledger.setApExpiredDate(pstIjAPSubledger.getDate(FLD_EXPIRED_DATE));
                        ijapsubledger.setApNoBill(pstIjAPSubledger.getString(FLD_BILL_NUMBER));
			ijapsubledger.setApAccChart(pstIjAPSubledger.getlong(FLD_ACCOUNT_CHART_ID));
			ijapsubledger.setApContact(pstIjAPSubledger.getlong(FLD_CONTACT_ID));
                        ijapsubledger.setApDebet(pstIjAPSubledger.getdouble(FLD_DEBT_VALUE));
                        ijapsubledger.setApCredit(pstIjAPSubledger.getdouble(FLD_CREDIT_VALUE));
                        ijapsubledger.setApPaidStatus(pstIjAPSubledger.getInt(FLD_PAID_STATUS));
                        ijapsubledger.setApTransCurrency(pstIjAPSubledger.getlong(FLD_TRANSACTION_CURRENCY));
                        ijapsubledger.setApTransRate(pstIjAPSubledger.getdouble(FLD_TRANSACTION_RATE));

			return ijapsubledger; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAPSubledger(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(IjAPSubledger ijapsubledger) throws DBException{ 
		try{ 
			PstIjAPSubledger pstIjAPSubledger = new PstIjAPSubledger(0);

			pstIjAPSubledger.setLong(FLD_PARENT_ID, ijapsubledger.getApParent());
			pstIjAPSubledger.setLong(FLD_JOURNAL_DETAIL_ID, ijapsubledger.getApDetailOid());
			pstIjAPSubledger.setDate(FLD_TRANSACTION_DATE, ijapsubledger.getApTransDate());
                        pstIjAPSubledger.setDate(FLD_EXPIRED_DATE, ijapsubledger.getApExpiredDate());
                        pstIjAPSubledger.setString(FLD_BILL_NUMBER, ijapsubledger.getApNoBill());
                        pstIjAPSubledger.setLong(FLD_ACCOUNT_CHART_ID, ijapsubledger.getApAccChart());
                        pstIjAPSubledger.setLong(FLD_CONTACT_ID, ijapsubledger.getApContact());
                        pstIjAPSubledger.setDouble(FLD_DEBT_VALUE, ijapsubledger.getApDebet());
                        pstIjAPSubledger.setDouble(FLD_CREDIT_VALUE, ijapsubledger.getApCredit());
                        pstIjAPSubledger.setInt(FLD_PAID_STATUS, ijapsubledger.getApPaidStatus());                        
                        pstIjAPSubledger.setLong(FLD_TRANSACTION_CURRENCY, ijapsubledger.getApTransCurrency());
                        pstIjAPSubledger.setDouble(FLD_TRANSACTION_RATE, ijapsubledger.getApTransRate());                        

			pstIjAPSubledger.insert(); 
			ijapsubledger.setOID(pstIjAPSubledger.getlong(FLD_IJ_AP_SUBLEDGER_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAPSubledger(0),DBException.UNKNOWN); 
		}
		return ijapsubledger.getOID();
	}

	public static long updateExc(IjAPSubledger ijapsubledger) throws DBException{ 
		try{ 
			if(ijapsubledger.getOID() != 0){ 
				PstIjAPSubledger pstIjAPSubledger = new PstIjAPSubledger(ijapsubledger.getOID());

                                pstIjAPSubledger.setLong(FLD_PARENT_ID, ijapsubledger.getApParent());
                                pstIjAPSubledger.setLong(FLD_JOURNAL_DETAIL_ID, ijapsubledger.getApDetailOid());
                                pstIjAPSubledger.setDate(FLD_TRANSACTION_DATE, ijapsubledger.getApTransDate());
                                pstIjAPSubledger.setDate(FLD_EXPIRED_DATE, ijapsubledger.getApExpiredDate());
                                pstIjAPSubledger.setString(FLD_BILL_NUMBER, ijapsubledger.getApNoBill());
                                pstIjAPSubledger.setLong(FLD_ACCOUNT_CHART_ID, ijapsubledger.getApAccChart());
                                pstIjAPSubledger.setLong(FLD_CONTACT_ID, ijapsubledger.getApContact());
                                pstIjAPSubledger.setDouble(FLD_DEBT_VALUE, ijapsubledger.getApDebet());
                                pstIjAPSubledger.setDouble(FLD_CREDIT_VALUE, ijapsubledger.getApCredit());
                                pstIjAPSubledger.setInt(FLD_PAID_STATUS, ijapsubledger.getApPaidStatus());                                                        
                                pstIjAPSubledger.setLong(FLD_TRANSACTION_CURRENCY, ijapsubledger.getApTransCurrency());
                                pstIjAPSubledger.setDouble(FLD_TRANSACTION_RATE, ijapsubledger.getApTransRate());                        

				pstIjAPSubledger.update(); 
				return ijapsubledger.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAPSubledger(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstIjAPSubledger pstIjAPSubledger = new PstIjAPSubledger(oid);
			pstIjAPSubledger.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAPSubledger(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_IJ_AP_SUBLEDGER; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
//                        System.out.println("sql : " + sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				IjAPSubledger ijapsubledger = new IjAPSubledger();
				resultToObject(rs, ijapsubledger);
				lists.add(ijapsubledger);
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

	private static void resultToObject(ResultSet rs, IjAPSubledger ijapsubledger){
		try{
			ijapsubledger.setOID(rs.getLong(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_IJ_AP_SUBLEDGER_ID]));
			ijapsubledger.setApParent(rs.getLong(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_PARENT_ID]));
			ijapsubledger.setApDetailOid(rs.getLong(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_JOURNAL_DETAIL_ID]));
			ijapsubledger.setApTransDate(rs.getDate(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_TRANSACTION_DATE]));
			ijapsubledger.setApExpiredDate(rs.getDate(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_EXPIRED_DATE]));
			ijapsubledger.setApNoBill(rs.getString(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_BILL_NUMBER]));
			ijapsubledger.setApAccChart(rs.getLong(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_ACCOUNT_CHART_ID]));
			ijapsubledger.setApContact(rs.getLong(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_CONTACT_ID]));
			ijapsubledger.setApDebet(rs.getDouble(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_DEBT_VALUE]));
			ijapsubledger.setApCredit(rs.getDouble(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_CREDIT_VALUE]));
                        ijapsubledger.setApPaidStatus(rs.getInt(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_PAID_STATUS]));
			ijapsubledger.setApTransCurrency(rs.getLong(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_TRANSACTION_CURRENCY]));
                        ijapsubledger.setApTransRate(rs.getDouble(PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_TRANSACTION_RATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long ijMapAccountId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_IJ_AP_SUBLEDGER + 
                                     " WHERE " + PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_IJ_AP_SUBLEDGER_ID] + 
                                     " = " + ijMapAccountId;

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
			String sql = "SELECT COUNT("+ PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_IJ_AP_SUBLEDGER_ID] + ") FROM " + TBL_IJ_AP_SUBLEDGER;
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
			  	   IjAPSubledger ijapsubledger = (IjAPSubledger)list.get(ls);
				   if(oid == ijapsubledger.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}        

        
        /**
         * Get OID of IjAPSubledger object for specified 'JournalDetailOID' already exist or not
         * @param lIjJournalDetailOid
         * @return
         * @created by Edhy
         */
        public static long getIjAPSubledgerOID(long lIjJournalDetailOid) 
        {               
            long lResult = 0;
            
            String sWhereClause = PstIjAPSubledger.fieldNames[PstIjAPSubledger.FLD_JOURNAL_DETAIL_ID] + 
                                  " = " + lIjJournalDetailOid;        
            Vector vListOfIjApSubledger = PstIjAPSubledger.list(0, 0, sWhereClause, "");
            if(vListOfIjApSubledger!=null && vListOfIjApSubledger.size()>0)
            {
                IjAPSubledger objIjAPSubledger = (IjAPSubledger) vListOfIjApSubledger.get(0);
                lResult = objIjAPSubledger.getOID();
            }       

            return lResult;
        }
        
}
