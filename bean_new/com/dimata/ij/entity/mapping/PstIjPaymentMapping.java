
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
public class PstIjPaymentMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_IJ_MAPPING_PAYMENT = "ij_mapping_payment";

	public static final  int FLD_IJ_MAP_PAYMENT_ID = 0;
	public static final  int FLD_PAYMENT_SYSTEM = 1;
	public static final  int FLD_CURRENCY = 2;
	public static final  int FLD_ACCOUNT = 3;
        public static final  int FLD_BO_SYSTEM = 4;
        public static final  int FLD_LOCATION = 5;

	public static final  String[] fieldNames = {
		"IJ_MAP_PAYMENT_ID",
		"PAYMENT_SYSTEM",
		"CURRENCY",
		"ACCOUNT",
                "BO_SYSTEM",
                "LOCATION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
                TYPE_INT,
                TYPE_LONG
	 }; 

	public PstIjPaymentMapping(){
	}

	public PstIjPaymentMapping(int i) throws DBException { 
		super(new PstIjPaymentMapping()); 
	}

	public PstIjPaymentMapping(String sOid) throws DBException { 
		super(new PstIjPaymentMapping(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstIjPaymentMapping(long lOid) throws DBException { 
		super(new PstIjPaymentMapping(0)); 
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
		return TBL_IJ_MAPPING_PAYMENT;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstIjPaymentMapping().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		IjPaymentMapping ijpaymentmapping = fetchExc(ent.getOID()); 
		ent = (Entity)ijpaymentmapping; 
		return ijpaymentmapping.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((IjPaymentMapping) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((IjPaymentMapping) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static IjPaymentMapping fetchExc(long oid) throws DBException{ 
		try{ 
			IjPaymentMapping ijpaymentmapping = new IjPaymentMapping();
			PstIjPaymentMapping pstIjPaymentMapping = new PstIjPaymentMapping(oid); 
			ijpaymentmapping.setOID(oid);

			ijpaymentmapping.setPaymentSystem(pstIjPaymentMapping.getlong(FLD_PAYMENT_SYSTEM));
			ijpaymentmapping.setCurrency(pstIjPaymentMapping.getlong(FLD_CURRENCY));
			ijpaymentmapping.setAccount(pstIjPaymentMapping.getlong(FLD_ACCOUNT));
                        ijpaymentmapping.setBoSystem(pstIjPaymentMapping.getInt(FLD_BO_SYSTEM));
                        ijpaymentmapping.setLocation(pstIjPaymentMapping.getlong(FLD_LOCATION));

			return ijpaymentmapping; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjPaymentMapping(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(IjPaymentMapping ijpaymentmapping) throws DBException{ 
		try{ 
			PstIjPaymentMapping pstIjPaymentMapping = new PstIjPaymentMapping(0);

			pstIjPaymentMapping.setLong(FLD_PAYMENT_SYSTEM, ijpaymentmapping.getPaymentSystem());
			pstIjPaymentMapping.setLong(FLD_CURRENCY, ijpaymentmapping.getCurrency());
			pstIjPaymentMapping.setLong(FLD_ACCOUNT, ijpaymentmapping.getAccount());
                        pstIjPaymentMapping.setInt(FLD_BO_SYSTEM, ijpaymentmapping.getBoSystem());
                        pstIjPaymentMapping.setLong(FLD_LOCATION, ijpaymentmapping.getLocation());

			pstIjPaymentMapping.insert(); 
			ijpaymentmapping.setOID(pstIjPaymentMapping.getlong(FLD_IJ_MAP_PAYMENT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjPaymentMapping(0),DBException.UNKNOWN); 
		}
		return ijpaymentmapping.getOID();
	}

	public static long updateExc(IjPaymentMapping ijpaymentmapping) throws DBException{ 
		try{ 
			if(ijpaymentmapping.getOID() != 0){ 
				PstIjPaymentMapping pstIjPaymentMapping = new PstIjPaymentMapping(ijpaymentmapping.getOID());

				pstIjPaymentMapping.setLong(FLD_PAYMENT_SYSTEM, ijpaymentmapping.getPaymentSystem());
				pstIjPaymentMapping.setLong(FLD_CURRENCY, ijpaymentmapping.getCurrency());
				pstIjPaymentMapping.setLong(FLD_ACCOUNT, ijpaymentmapping.getAccount());
                                pstIjPaymentMapping.setInt(FLD_BO_SYSTEM, ijpaymentmapping.getBoSystem());
                                pstIjPaymentMapping.setLong(FLD_LOCATION, ijpaymentmapping.getLocation());

				pstIjPaymentMapping.update(); 
				return ijpaymentmapping.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjPaymentMapping(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstIjPaymentMapping pstIjPaymentMapping = new PstIjPaymentMapping(oid);
			pstIjPaymentMapping.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjPaymentMapping(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_IJ_MAPPING_PAYMENT; 
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
				IjPaymentMapping ijpaymentmapping = new IjPaymentMapping();
				resultToObject(rs, ijpaymentmapping);
				lists.add(ijpaymentmapping);
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

	private static void resultToObject(ResultSet rs, IjPaymentMapping ijpaymentmapping){
		try{
			ijpaymentmapping.setOID(rs.getLong(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_IJ_MAP_PAYMENT_ID]));
			ijpaymentmapping.setPaymentSystem(rs.getLong(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_PAYMENT_SYSTEM]));
			ijpaymentmapping.setCurrency(rs.getLong(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_CURRENCY]));
			ijpaymentmapping.setAccount(rs.getLong(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_ACCOUNT]));
                        ijpaymentmapping.setBoSystem(rs.getInt(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_BO_SYSTEM]));
                        ijpaymentmapping.setLocation(rs.getLong(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_LOCATION]));
		}catch(Exception e){ }
	}

	public static boolean checkOID(long ijMapPaymentId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_IJ_MAPPING_PAYMENT + " WHERE " + 
						PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_IJ_MAP_PAYMENT_ID] + " = " + ijMapPaymentId;

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
			String sql = "SELECT COUNT("+ PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_IJ_MAP_PAYMENT_ID] + ") FROM " + TBL_IJ_MAPPING_PAYMENT;
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
			  	   IjPaymentMapping ijpaymentmapping = (IjPaymentMapping)list.get(ls);
				   if(oid == ijpaymentmapping.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
        
        
        /**
         * @param whereClause
         * @return
         */        
	public static long getAccountChart(String whereClause)
        {
            DBResultSet dbrs = null;
            long result = 0;
            try
            {
                String sql = "SELECT " + PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_ACCOUNT] +
                             " FROM " + PstIjPaymentMapping.TBL_IJ_MAPPING_PAYMENT + 
                             " WHERE " + whereClause;

                System.out.println("Sql account mapping : "  +sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next())
                { 
                    result = rs.getLong(PstIjPaymentMapping.fieldNames[PstIjPaymentMapping.FLD_ACCOUNT]); 
                    break;
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
            return result;
	}
        
}
