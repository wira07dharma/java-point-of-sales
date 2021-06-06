
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

package com.dimata.ij.entity.mapping; 

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




public class PstIjAccountMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_IJ_MAPPING_ACCOUNT = "ij_mapping_account";

	public static final  int FLD_IJ_MAP_ACCOUNT_ID = 0;
	public static final  int FLD_JOURNAL_TYPE = 1;
	public static final  int FLD_CURRENCY = 2;
	public static final  int FLD_ACCOUNT = 3;
        public static final  int FLD_BO_SYSTEM = 4;
        public static final  int FLD_LOCATION = 5;

	public static final  String[] fieldNames = {
		"IJ_MAP_ACCOUNT_ID",
		"JOURNAL_TYPE",
		"CURRENCY",
		"ACCOUNT",
                "BO_SYSTEM",
                "LOCATION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_INT,
		TYPE_LONG,
		TYPE_LONG,
                TYPE_INT,
                TYPE_LONG
	 }; 

	public PstIjAccountMapping(){
	}

	public PstIjAccountMapping(int i) throws DBException { 
		super(new PstIjAccountMapping()); 
	}

	public PstIjAccountMapping(String sOid) throws DBException { 
		super(new PstIjAccountMapping(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstIjAccountMapping(long lOid) throws DBException { 
		super(new PstIjAccountMapping(0)); 
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
		return TBL_IJ_MAPPING_ACCOUNT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstIjAccountMapping().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		IjAccountMapping ijaccountmapping = fetchExc(ent.getOID()); 
		ent = (Entity)ijaccountmapping; 
		return ijaccountmapping.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((IjAccountMapping) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((IjAccountMapping) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static IjAccountMapping fetchExc(long oid) throws DBException{ 
		try{ 
			IjAccountMapping ijaccountmapping = new IjAccountMapping();
			PstIjAccountMapping pstIjAccountMapping = new PstIjAccountMapping(oid); 
			ijaccountmapping.setOID(oid);

			ijaccountmapping.setJournalType(pstIjAccountMapping.getInt(FLD_JOURNAL_TYPE));
			ijaccountmapping.setCurrency(pstIjAccountMapping.getlong(FLD_CURRENCY));
			ijaccountmapping.setAccount(pstIjAccountMapping.getlong(FLD_ACCOUNT));
                        ijaccountmapping.setBoSystem(pstIjAccountMapping.getInt(FLD_BO_SYSTEM));
                        ijaccountmapping.setLocation(pstIjAccountMapping.getlong(FLD_LOCATION));

			return ijaccountmapping; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAccountMapping(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(IjAccountMapping ijaccountmapping) throws DBException{ 
		try{ 
			PstIjAccountMapping pstIjAccountMapping = new PstIjAccountMapping(0);

			pstIjAccountMapping.setInt(FLD_JOURNAL_TYPE, ijaccountmapping.getJournalType());
			pstIjAccountMapping.setLong(FLD_CURRENCY, ijaccountmapping.getCurrency());
			pstIjAccountMapping.setLong(FLD_ACCOUNT, ijaccountmapping.getAccount());
                        pstIjAccountMapping.setInt(FLD_BO_SYSTEM, ijaccountmapping.getBoSystem());
                        pstIjAccountMapping.setLong(FLD_LOCATION, ijaccountmapping.getLocation());                        

			pstIjAccountMapping.insert(); 
			ijaccountmapping.setOID(pstIjAccountMapping.getlong(FLD_IJ_MAP_ACCOUNT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAccountMapping(0),DBException.UNKNOWN); 
		}
		return ijaccountmapping.getOID();
	}

	public static long updateExc(IjAccountMapping ijaccountmapping) throws DBException{ 
		try{ 
			if(ijaccountmapping.getOID() != 0){ 
				PstIjAccountMapping pstIjAccountMapping = new PstIjAccountMapping(ijaccountmapping.getOID());

				pstIjAccountMapping.setInt(FLD_JOURNAL_TYPE, ijaccountmapping.getJournalType());
				pstIjAccountMapping.setLong(FLD_CURRENCY, ijaccountmapping.getCurrency());
				pstIjAccountMapping.setLong(FLD_ACCOUNT, ijaccountmapping.getAccount());
                                pstIjAccountMapping.setInt(FLD_BO_SYSTEM, ijaccountmapping.getBoSystem());                                
                                pstIjAccountMapping.setLong(FLD_LOCATION, ijaccountmapping.getLocation());                                

				pstIjAccountMapping.update(); 
				return ijaccountmapping.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAccountMapping(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstIjAccountMapping pstIjAccountMapping = new PstIjAccountMapping(oid);
			pstIjAccountMapping.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjAccountMapping(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_IJ_MAPPING_ACCOUNT; 
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
                                break;
                        }            			
                        
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				IjAccountMapping ijaccountmapping = new IjAccountMapping();
				resultToObject(rs, ijaccountmapping);
				lists.add(ijaccountmapping);
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

	private static void resultToObject(ResultSet rs, IjAccountMapping ijaccountmapping){
		try{
			ijaccountmapping.setOID(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID]));
			ijaccountmapping.setJournalType(rs.getInt(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE]));
			ijaccountmapping.setCurrency(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY]));
			ijaccountmapping.setAccount(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_ACCOUNT]));
                        ijaccountmapping.setBoSystem(rs.getInt(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_BO_SYSTEM]));
                        ijaccountmapping.setLocation(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_LOCATION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long ijMapAccountId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_IJ_MAPPING_ACCOUNT + " WHERE " + 
						PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID] + " = " + ijMapAccountId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			
		}
                return result;
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID] + ") FROM " + TBL_IJ_MAPPING_ACCOUNT;
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
			  	   IjAccountMapping ijaccountmapping = (IjAccountMapping)list.get(ls);
				   if(oid == ijaccountmapping.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
        
        
        /**
         * @param lLocationOid
         * @param transactionType
         * @param transactionCurrency
         * @return
         * @created by Edhy
         */        
	public static IjAccountMapping getObjIjAccountMapping(long lLocationOid, int transactionType, long transactionCurrency)
        {
            DBResultSet dbrs = null;
            IjAccountMapping objIjAccountMapping = new IjAccountMapping();
            try
            {
                String sql = "SELECT " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_ACCOUNT] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_BO_SYSTEM] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_LOCATION] + 
                             " FROM " + TBL_IJ_MAPPING_ACCOUNT + 
                             " WHERE " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_LOCATION] + 
                             " = " + lLocationOid + 
                             " AND " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE] + 
                             " = " + transactionType +
                             " AND " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY] + 
                             " = " + transactionCurrency;                  

                System.out.println("---------------------------> sql getObjIjAccountMapping : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()) 
                { 
                    objIjAccountMapping.setOID(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID]));
                    objIjAccountMapping.setJournalType(rs.getInt(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE]));
                    objIjAccountMapping.setCurrency(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY]));
                    objIjAccountMapping.setAccount(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_ACCOUNT]));                    
                    objIjAccountMapping.setBoSystem(rs.getInt(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_BO_SYSTEM]));                    
                    objIjAccountMapping.setLocation(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_LOCATION]));                                        
                }
                rs.close();
            }
            catch(Exception e)
            {
                System.out.println("err : "+e.toString());
            }
            finally
            {
                DBResultSet.close(dbrs);
                
            }
            return objIjAccountMapping;
	}
        
        
        /**
         * @param transactionType
         * @param transactionCurrency
         * @return
         * @created by Edhy
         */        
	public static IjAccountMapping getObjIjAccountMapping(int transactionType, long transactionCurrency)
        {
            DBResultSet dbrs = null;
            IjAccountMapping objIjAccountMapping = new IjAccountMapping();
            try
            {
                String sql = "SELECT " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_ACCOUNT] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_BO_SYSTEM] + 
                             ", " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_LOCATION] + 
                             " FROM " + TBL_IJ_MAPPING_ACCOUNT + 
                             " WHERE " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE] + 
                             " = " + transactionType + 
                             " AND " + PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY] + 
                             " = " + transactionCurrency;

                System.out.println("---------------------------> sql getObjIjAccountMapping : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()) 
                { 
                    objIjAccountMapping.setOID(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_IJ_MAP_ACCOUNT_ID]));
                    objIjAccountMapping.setJournalType(rs.getInt(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_JOURNAL_TYPE]));
                    objIjAccountMapping.setCurrency(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_CURRENCY]));
                    objIjAccountMapping.setAccount(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_ACCOUNT]));                    
                    objIjAccountMapping.setBoSystem(rs.getInt(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_BO_SYSTEM]));                    
                    objIjAccountMapping.setLocation(rs.getLong(PstIjAccountMapping.fieldNames[PstIjAccountMapping.FLD_LOCATION]));                                        
                }
                rs.close();
            }
            catch(Exception e)
            {
                System.out.println("err : "+e.toString());
            }
            finally
            {
                DBResultSet.close(dbrs);
                
            }
            return objIjAccountMapping;
	}        
}
