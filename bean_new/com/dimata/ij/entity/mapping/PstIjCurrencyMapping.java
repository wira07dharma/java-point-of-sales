
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




public class PstIjCurrencyMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_IJ_MAPPING_CURRENCY = "ij_mapping_currency";

	public static final  int FLD_IJ_MAP_CURR_ID = 0;
	public static final  int FLD_BO_CURRENCY = 1;
	public static final  int FLD_AISO_CURRENCY = 2;
        public static final  int FLD_BO_SYSTEM = 3;

	public static final  String[] fieldNames = {
		"IJ_MAP_CURR_ID",
		"BO_CURRENCY",
		"AISO_CURRENCY",
                "BO_SYSTEM"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
                TYPE_INT
	 }; 

	public PstIjCurrencyMapping(){
	}

	public PstIjCurrencyMapping(int i) throws DBException { 
		super(new PstIjCurrencyMapping()); 
	}

	public PstIjCurrencyMapping(String sOid) throws DBException { 
		super(new PstIjCurrencyMapping(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstIjCurrencyMapping(long lOid) throws DBException { 
		super(new PstIjCurrencyMapping(0)); 
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
		return TBL_IJ_MAPPING_CURRENCY;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstIjCurrencyMapping().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		IjCurrencyMapping ijcurrencymapping = fetchExc(ent.getOID()); 
		ent = (Entity)ijcurrencymapping; 
		return ijcurrencymapping.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((IjCurrencyMapping) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((IjCurrencyMapping) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static IjCurrencyMapping fetchExc(long oid) throws DBException{ 
		try{ 
			IjCurrencyMapping ijcurrencymapping = new IjCurrencyMapping();
			PstIjCurrencyMapping pstIjCurrencyMapping = new PstIjCurrencyMapping(oid); 
			ijcurrencymapping.setOID(oid);

			ijcurrencymapping.setBoCurrency(pstIjCurrencyMapping.getlong(FLD_BO_CURRENCY));
			ijcurrencymapping.setAisoCurrency(pstIjCurrencyMapping.getlong(FLD_AISO_CURRENCY));
                        ijcurrencymapping.setBoSystem(pstIjCurrencyMapping.getInt(FLD_BO_SYSTEM));

			return ijcurrencymapping; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjCurrencyMapping(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(IjCurrencyMapping ijcurrencymapping) throws DBException{ 
		try{ 
			PstIjCurrencyMapping pstIjCurrencyMapping = new PstIjCurrencyMapping(0);

			pstIjCurrencyMapping.setLong(FLD_BO_CURRENCY, ijcurrencymapping.getBoCurrency());
			pstIjCurrencyMapping.setLong(FLD_AISO_CURRENCY, ijcurrencymapping.getAisoCurrency());
                        pstIjCurrencyMapping.setInt(FLD_BO_SYSTEM, ijcurrencymapping.getBoSystem());

			pstIjCurrencyMapping.insert(); 
			ijcurrencymapping.setOID(pstIjCurrencyMapping.getlong(FLD_IJ_MAP_CURR_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjCurrencyMapping(0),DBException.UNKNOWN); 
		}
		return ijcurrencymapping.getOID();
	}

	public static long updateExc(IjCurrencyMapping ijcurrencymapping) throws DBException{ 
		try{ 
			if(ijcurrencymapping.getOID() != 0){ 
				PstIjCurrencyMapping pstIjCurrencyMapping = new PstIjCurrencyMapping(ijcurrencymapping.getOID());

				pstIjCurrencyMapping.setLong(FLD_BO_CURRENCY, ijcurrencymapping.getBoCurrency());
				pstIjCurrencyMapping.setLong(FLD_AISO_CURRENCY, ijcurrencymapping.getAisoCurrency());
                                pstIjCurrencyMapping.setInt(FLD_BO_SYSTEM, ijcurrencymapping.getBoSystem());
                                
				pstIjCurrencyMapping.update(); 
				return ijcurrencymapping.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjCurrencyMapping(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstIjCurrencyMapping pstIjCurrencyMapping = new PstIjCurrencyMapping(oid);
			pstIjCurrencyMapping.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjCurrencyMapping(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_IJ_MAPPING_CURRENCY; 
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
				IjCurrencyMapping ijcurrencymapping = new IjCurrencyMapping();
				resultToObject(rs, ijcurrencymapping);
				lists.add(ijcurrencymapping);
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

	private static void resultToObject(ResultSet rs, IjCurrencyMapping ijcurrencymapping){
		try{
			ijcurrencymapping.setOID(rs.getLong(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_IJ_MAP_CURR_ID]));
			ijcurrencymapping.setBoCurrency(rs.getLong(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_CURRENCY]));
			ijcurrencymapping.setAisoCurrency(rs.getLong(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_AISO_CURRENCY]));
                        ijcurrencymapping.setBoSystem(rs.getInt(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_SYSTEM]));
		}catch(Exception e){ }
	}

	public static boolean checkOID(long ijMapCurrId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_IJ_MAPPING_CURRENCY + " WHERE " + 
						PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_IJ_MAP_CURR_ID] + " = " + ijMapCurrId;

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
			String sql = "SELECT COUNT("+ PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_IJ_MAP_CURR_ID] + ") FROM " + TBL_IJ_MAPPING_CURRENCY;
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
			  	   IjCurrencyMapping ijcurrencymapping = (IjCurrencyMapping)list.get(ls);
				   if(oid == ijcurrencymapping.getOID())
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
	public static IjCurrencyMapping getObjIjCurrencyMapping(int boSystem, long lBoCurrency)
        {
            DBResultSet dbrs = null;
            IjCurrencyMapping objIjCurrencyMapping = new IjCurrencyMapping();
            try
            {
                String sql = "SELECT " + PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_IJ_MAP_CURR_ID] + 
                             ", " + PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_SYSTEM] + 
                             ", " + PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_CURRENCY] + 
                             ", " + PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_AISO_CURRENCY] + 
                             " FROM " + TBL_IJ_MAPPING_CURRENCY + 
                             " WHERE " + PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_SYSTEM] + 
                             " = " + boSystem + 
                             " AND " + PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_CURRENCY] + 
                             " = " + lBoCurrency;                  

                System.out.println("curr map : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()) 
                { 
                    objIjCurrencyMapping.setOID(rs.getLong(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_IJ_MAP_CURR_ID]));
                    objIjCurrencyMapping.setBoSystem(rs.getInt(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_SYSTEM]));
                    objIjCurrencyMapping.setBoCurrency(rs.getLong(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_BO_CURRENCY]));
                    objIjCurrencyMapping.setAisoCurrency(rs.getLong(PstIjCurrencyMapping.fieldNames[PstIjCurrencyMapping.FLD_AISO_CURRENCY]));                    
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
            return objIjCurrencyMapping;
	}
        
}
