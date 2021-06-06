/*
 * PstIjARSubledger.java
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
public class PstIjARSubledger extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final String TBL_IJ_AR_SUBLEDGER = "ij_ar_subledger";

	public static final int FLD_IJ_AR_SUBLEDGER_ID = 0;
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
		"IJ_AR_SUBLEDGER_ID",
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

	public PstIjARSubledger(){
	}

	public PstIjARSubledger(int i) throws DBException { 
		super(new PstIjARSubledger()); 
	}

	public PstIjARSubledger(String sOid) throws DBException { 
		super(new PstIjARSubledger()); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstIjARSubledger(long lOid) throws DBException { 
		super(new PstIjARSubledger()); 
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
		return TBL_IJ_AR_SUBLEDGER;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstIjARSubledger().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		IjARSubledger ijarsubledger = fetchExc(ent.getOID()); 
		ent = (Entity)ijarsubledger; 
		return ijarsubledger.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((IjARSubledger) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((IjARSubledger) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static IjARSubledger fetchExc(long oid) throws DBException{ 
		try{ 
			IjARSubledger ijarsubledger = new IjARSubledger();
			PstIjARSubledger pstIjARSubledger = new PstIjARSubledger(oid); 
			ijarsubledger.setOID(oid);

                        ijarsubledger.setArParent(pstIjARSubledger.getlong(FLD_PARENT_ID));
			ijarsubledger.setArDetailOid(pstIjARSubledger.getlong(FLD_JOURNAL_DETAIL_ID));
                        ijarsubledger.setArTransDate(pstIjARSubledger.getDate(FLD_TRANSACTION_DATE));
                        ijarsubledger.setArExpiredDate(pstIjARSubledger.getDate(FLD_EXPIRED_DATE));
                        ijarsubledger.setArNoBill(pstIjARSubledger.getString(FLD_BILL_NUMBER));
			ijarsubledger.setArAccChart(pstIjARSubledger.getlong(FLD_ACCOUNT_CHART_ID));
			ijarsubledger.setArContact(pstIjARSubledger.getlong(FLD_CONTACT_ID));
                        ijarsubledger.setArDebet(pstIjARSubledger.getdouble(FLD_DEBT_VALUE));
                        ijarsubledger.setArCredit(pstIjARSubledger.getdouble(FLD_CREDIT_VALUE));
                        ijarsubledger.setArPaidStatus(pstIjARSubledger.getInt(FLD_PAID_STATUS));
                        ijarsubledger.setArTransCurrency(pstIjARSubledger.getlong(FLD_TRANSACTION_CURRENCY));
                        ijarsubledger.setArTransRate(pstIjARSubledger.getdouble(FLD_TRANSACTION_RATE));

			return ijarsubledger; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjARSubledger(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(IjARSubledger ijarsubledger) throws DBException{ 
		try{ 
			PstIjARSubledger pstIjARSubledger = new PstIjARSubledger(0);

			pstIjARSubledger.setLong(FLD_PARENT_ID, ijarsubledger.getArParent());
			pstIjARSubledger.setLong(FLD_JOURNAL_DETAIL_ID, ijarsubledger.getArDetailOid());
			pstIjARSubledger.setDate(FLD_TRANSACTION_DATE, ijarsubledger.getArTransDate());
                        pstIjARSubledger.setDate(FLD_EXPIRED_DATE, ijarsubledger.getArExpiredDate());
                        pstIjARSubledger.setString(FLD_BILL_NUMBER, ijarsubledger.getArNoBill());
                        pstIjARSubledger.setLong(FLD_ACCOUNT_CHART_ID, ijarsubledger.getArAccChart());
                        pstIjARSubledger.setLong(FLD_CONTACT_ID, ijarsubledger.getArContact());
                        pstIjARSubledger.setDouble(FLD_DEBT_VALUE, ijarsubledger.getArDebet());
                        pstIjARSubledger.setDouble(FLD_CREDIT_VALUE, ijarsubledger.getArCredit());
                        pstIjARSubledger.setInt(FLD_PAID_STATUS, ijarsubledger.getArPaidStatus());                        
                        pstIjARSubledger.setLong(FLD_TRANSACTION_CURRENCY, ijarsubledger.getArTransCurrency());
                        pstIjARSubledger.setDouble(FLD_TRANSACTION_RATE, ijarsubledger.getArTransRate());                        

			pstIjARSubledger.insert(); 
			ijarsubledger.setOID(pstIjARSubledger.getlong(FLD_IJ_AR_SUBLEDGER_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjARSubledger(0),DBException.UNKNOWN); 
		}
		return ijarsubledger.getOID();
	}

	public static long updateExc(IjARSubledger ijarsubledger) throws DBException{ 
		try{ 
			if(ijarsubledger.getOID() != 0){ 
				PstIjARSubledger pstIjARSubledger = new PstIjARSubledger(ijarsubledger.getOID());

                                pstIjARSubledger.setLong(FLD_PARENT_ID, ijarsubledger.getArParent());
                                pstIjARSubledger.setLong(FLD_JOURNAL_DETAIL_ID, ijarsubledger.getArDetailOid());
                                pstIjARSubledger.setDate(FLD_TRANSACTION_DATE, ijarsubledger.getArTransDate());
                                pstIjARSubledger.setDate(FLD_EXPIRED_DATE, ijarsubledger.getArExpiredDate());
                                pstIjARSubledger.setString(FLD_BILL_NUMBER, ijarsubledger.getArNoBill());
                                pstIjARSubledger.setLong(FLD_ACCOUNT_CHART_ID, ijarsubledger.getArAccChart());
                                pstIjARSubledger.setLong(FLD_CONTACT_ID, ijarsubledger.getArContact());
                                pstIjARSubledger.setDouble(FLD_DEBT_VALUE, ijarsubledger.getArDebet());
                                pstIjARSubledger.setDouble(FLD_CREDIT_VALUE, ijarsubledger.getArCredit());
                                pstIjARSubledger.setInt(FLD_PAID_STATUS, ijarsubledger.getArPaidStatus());                        
                                pstIjARSubledger.setLong(FLD_TRANSACTION_CURRENCY, ijarsubledger.getArTransCurrency());
                                pstIjARSubledger.setDouble(FLD_TRANSACTION_RATE, ijarsubledger.getArTransRate());                        

				pstIjARSubledger.update(); 
				return ijarsubledger.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjARSubledger(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstIjARSubledger pstIjARSubledger = new PstIjARSubledger(oid);
			pstIjARSubledger.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjARSubledger(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_IJ_AR_SUBLEDGER; 
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
				IjARSubledger ijarsubledger = new IjARSubledger();
				resultToObject(rs, ijarsubledger);
				lists.add(ijarsubledger);
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

	private static void resultToObject(ResultSet rs, IjARSubledger ijarsubledger){
		try{
			ijarsubledger.setOID(rs.getLong(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_IJ_AR_SUBLEDGER_ID]));
			ijarsubledger.setArParent(rs.getLong(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_PARENT_ID]));
			ijarsubledger.setArDetailOid(rs.getLong(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_JOURNAL_DETAIL_ID]));
			ijarsubledger.setArTransDate(rs.getDate(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_TRANSACTION_DATE]));
			ijarsubledger.setArExpiredDate(rs.getDate(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_EXPIRED_DATE]));
			ijarsubledger.setArNoBill(rs.getString(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_BILL_NUMBER]));
			ijarsubledger.setArAccChart(rs.getLong(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_ACCOUNT_CHART_ID]));
			ijarsubledger.setArContact(rs.getLong(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_CONTACT_ID]));
			ijarsubledger.setArDebet(rs.getDouble(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_DEBT_VALUE]));
			ijarsubledger.setArCredit(rs.getDouble(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_CREDIT_VALUE]));
                        ijarsubledger.setArPaidStatus(rs.getInt(PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_PAID_STATUS]));
			ijarsubledger.setArTransCurrency(rs.getLong(PstIjARSubledger.fieldNames[PstIjAPSubledger.FLD_TRANSACTION_CURRENCY]));
                        ijarsubledger.setArTransRate(rs.getDouble(PstIjARSubledger.fieldNames[PstIjAPSubledger.FLD_TRANSACTION_RATE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long ijMapAccountId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_IJ_AR_SUBLEDGER + 
                                     " WHERE " + PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_IJ_AR_SUBLEDGER_ID] + 
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
			String sql = "SELECT COUNT("+ PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_IJ_AR_SUBLEDGER_ID] + ") FROM " + TBL_IJ_AR_SUBLEDGER;
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
			  	   IjARSubledger ijarsubledger = (IjARSubledger)list.get(ls);
				   if(oid == ijarsubledger.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;
  
		return start;
	}        

        
        /**
         * Get OID of IjARSubledger object for specified 'JournalDetailOID'
         * @param lIjJournalDetailOid
         * @return
         * @created by Edhy
         */
        public static long getIjARSubledgerOID(long lIjJournalDetailOid) 
        {           
            long lResult = 0;
            
            String sWhereClause = PstIjARSubledger.fieldNames[PstIjARSubledger.FLD_JOURNAL_DETAIL_ID] + 
                                  " = " + lIjJournalDetailOid;        
            Vector vListOfIjArSubledger = PstIjARSubledger.list(0, 0, sWhereClause, "");
            if(vListOfIjArSubledger!=null && vListOfIjArSubledger.size()>0)
            {
                IjARSubledger objIjARSubledger = (IjARSubledger) vListOfIjArSubledger.get(0);
                lResult = objIjARSubledger.getOID();
            }       

            return lResult;
        }
        
}
