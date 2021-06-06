/*
 * I_IJGeneral.java
 *
 * Created on December 25, 2004, 3:37 PM
 */

package com.dimata.ij.entity.configuration; 

// package java 
import java.sql.*;
import java.util.*;

// package qdep
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;

// interfaces package
import com.dimata.qdep.entity.I_IJGeneral;

// package ij
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

public class PstIjConfiguration extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_IJGeneral 
{ 
	public static final  String TBL_IJ_CONFIGURATION = "ij_configuration";

	public static final  int FLD_IJ_CONFIGURATION_ID = 0;
	public static final  int FLD_BO_SYSTEM = 1;
	public static final  int FLD_CONFIG_GROUP = 2;
	public static final  int FLD_CONFIG_ITEM = 3;
	public static final  int FLD_CONFIG_SELECT = 4;
        public static final  int FLD_IJ_IMPL_CLASS = 5;

	public static final  String[] fieldNames = {
		"IJ_CONFIGURATION_ID",
		"BO_SYSTEM",
		"CONFIG_GROUP",
		"CONFIG_ITEM",
		"CONFIG_SELECT",
                "IJ_IMPL_CLASS"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
                TYPE_STRING
	 }; 

	public PstIjConfiguration(){
	}

	public PstIjConfiguration(int i) throws DBException { 
		super(new PstIjConfiguration()); 
	}

	public PstIjConfiguration(String sOid) throws DBException { 
		super(new PstIjConfiguration(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstIjConfiguration(long lOid) throws DBException { 
		super(new PstIjConfiguration(0)); 
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
		return TBL_IJ_CONFIGURATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstIjConfiguration().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		IjConfiguration ijconfiguration = fetchExc(ent.getOID()); 
		ent = (Entity)ijconfiguration; 
		return ijconfiguration.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((IjConfiguration) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((IjConfiguration) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static IjConfiguration fetchExc(long oid) throws DBException{ 
		try{ 
			IjConfiguration ijconfiguration = new IjConfiguration();
			PstIjConfiguration pstIjConfiguration = new PstIjConfiguration(oid); 
			ijconfiguration.setOID(oid);

			ijconfiguration.setBoSystem(pstIjConfiguration.getInt(FLD_BO_SYSTEM));
			ijconfiguration.setConfigGroup(pstIjConfiguration.getInt(FLD_CONFIG_GROUP));
			ijconfiguration.setConfigItem(pstIjConfiguration.getInt(FLD_CONFIG_ITEM));
			ijconfiguration.setConfigSelect(pstIjConfiguration.getInt(FLD_CONFIG_SELECT));
                        ijconfiguration.setSIjImplClass(pstIjConfiguration.getString(FLD_IJ_IMPL_CLASS));

			return ijconfiguration; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjConfiguration(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(IjConfiguration ijconfiguration) throws DBException{ 
		try{ 
			PstIjConfiguration pstIjConfiguration = new PstIjConfiguration(0);

			pstIjConfiguration.setInt(FLD_BO_SYSTEM, ijconfiguration.getBoSystem());
			pstIjConfiguration.setInt(FLD_CONFIG_GROUP, ijconfiguration.getConfigGroup());
			pstIjConfiguration.setInt(FLD_CONFIG_ITEM, ijconfiguration.getConfigItem());
			pstIjConfiguration.setInt(FLD_CONFIG_SELECT, ijconfiguration.getConfigSelect());
                        pstIjConfiguration.setString(FLD_IJ_IMPL_CLASS, ijconfiguration.getSIjImplClass());

			pstIjConfiguration.insert(); 
			ijconfiguration.setOID(pstIjConfiguration.getlong(FLD_IJ_CONFIGURATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjConfiguration(0),DBException.UNKNOWN); 
		}
		return ijconfiguration.getOID();
	}

	public static long updateExc(IjConfiguration ijconfiguration) throws DBException{ 
		try{ 
			if(ijconfiguration.getOID() != 0){ 
				PstIjConfiguration pstIjConfiguration = new PstIjConfiguration(ijconfiguration.getOID());

				pstIjConfiguration.setInt(FLD_BO_SYSTEM, ijconfiguration.getBoSystem());
				pstIjConfiguration.setInt(FLD_CONFIG_GROUP, ijconfiguration.getConfigGroup());
				pstIjConfiguration.setInt(FLD_CONFIG_ITEM, ijconfiguration.getConfigItem());
				pstIjConfiguration.setInt(FLD_CONFIG_SELECT, ijconfiguration.getConfigSelect());
                                pstIjConfiguration.setString(FLD_IJ_IMPL_CLASS, ijconfiguration.getSIjImplClass());

				pstIjConfiguration.update(); 
				return ijconfiguration.getOID();

			}
		}catch(DBException dbe){  
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjConfiguration(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstIjConfiguration pstIjConfiguration = new PstIjConfiguration(oid);
			pstIjConfiguration.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstIjConfiguration(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_IJ_CONFIGURATION; 
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
				IjConfiguration ijconfiguration = new IjConfiguration();
				resultToObject(rs, ijconfiguration);
				lists.add(ijconfiguration);
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

	private static void resultToObject(ResultSet rs, IjConfiguration ijconfiguration){
		try{
			ijconfiguration.setOID(rs.getLong(PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_IJ_CONFIGURATION_ID]));
			ijconfiguration.setBoSystem(rs.getInt(PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_BO_SYSTEM]));
			ijconfiguration.setConfigGroup(rs.getInt(PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_GROUP]));
			ijconfiguration.setConfigItem(rs.getInt(PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_ITEM]));
			ijconfiguration.setConfigSelect(rs.getInt(PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_SELECT]));
                        ijconfiguration.setSIjImplClass(rs.getString(PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_IJ_IMPL_CLASS]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long ijConfigurationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_IJ_CONFIGURATION + " WHERE " + 
						PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_IJ_CONFIGURATION_ID] + " = " + ijConfigurationId;

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
			String sql = "SELECT COUNT("+ PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_IJ_CONFIGURATION_ID] + ") FROM " + TBL_IJ_CONFIGURATION;
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
			  	   IjConfiguration ijconfiguration = (IjConfiguration)list.get(ls);
				   if(oid == ijconfiguration.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
        
        /**
         * this method used to get selected configuration for specified BO, Group and Config Item
         * @param intBoSystem
         * @param intConfigGroup
         * @param intConfigItem
         * @return -1 if no record exist, otherwise >= 0 defend on config index
         * @created by Edhy
         */
        public int getIJConfiguration(int intBoSystem, int intConfigGroup, int intConfigItem)
        {
            int result = -1;
            DBResultSet dbrs = null;            
            try
            {
                String sql = "SELECT " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_SELECT] + 
                             " FROM " + PstIjConfiguration.TBL_IJ_CONFIGURATION + 
                             " WHERE " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_BO_SYSTEM] + 
                             " = " + intBoSystem + 
                             " AND " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_GROUP] + 
                             " = " + intConfigGroup + 
                             " AND " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_ITEM] + 
                             " = " + intConfigItem;
                
                //System.out.println("getIJConfiguration sql : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()) 
                { 
                    result = rs.getInt(1); 
                }
                rs.close();
            }
            catch(Exception e)
            {
                System.out.println("exp when getIJConfiguration : " + e.toString());
            }
            finally
            {
                DBResultSet.close(dbrs);
                
            }   
            return result;
        };
        

        /**
         * this method used to get selected configuration for specified BO, Group and Config Item
         * @param intBoSystem
         * @param intConfigGroup
         * @param intConfigItem
         * @return true if record exist, otherwise false defend on config index
         * @created by Edhy
         */
        public IjConfiguration getObjIJConfiguration(int intBoSystem, int intConfigGroup, int intConfigItem)
        {
            IjConfiguration objIjConfiguration = new IjConfiguration();
            DBResultSet dbrs = null;            
            try
            {
                String sql = "SELECT " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_IJ_CONFIGURATION_ID] + 
                             ", " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_BO_SYSTEM] + 
                             ", " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_GROUP] + 
                             ", " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_ITEM] + 
                             ", " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_SELECT] + 
                             ", " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_IJ_IMPL_CLASS] + 
                             " FROM " + PstIjConfiguration.TBL_IJ_CONFIGURATION + 
                             " WHERE " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_BO_SYSTEM] + 
                             " = " + intBoSystem + 
                             " AND " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_GROUP] + 
                             " = " + intConfigGroup + 
                             " AND " + PstIjConfiguration.fieldNames[PstIjConfiguration.FLD_CONFIG_ITEM] + 
                             " = " + intConfigItem;
                
                //System.out.println("getIJConfiguration sql : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()) 
                {                     
                    objIjConfiguration.setOID(rs.getLong(1));
                    objIjConfiguration.setBoSystem(rs.getInt(2));
                    objIjConfiguration.setConfigGroup(rs.getInt(3));
                    objIjConfiguration.setConfigItem(rs.getInt(4));
                    objIjConfiguration.setConfigSelect(rs.getInt(5));                    
                    objIjConfiguration.setSIjImplClass(rs.getString(6));
                }
                rs.close();
            }
            catch(Exception e)
            {
                System.out.println("exp when getIJConfiguration : " + e.toString());
            }
            finally
            {
                DBResultSet.close(dbrs);
                
            }   
            return objIjConfiguration;
        };
        
        /**
         * @param intBoSystem
         * @param strConfigGroup
         * @param strConfigItem
         * @param strConfigSelect
         * @return  
         */        
        public int storeIjConfPerBoSystem(int intBoSystem, String[] strConfigGroup, String[] strConfigItem, String[] strConfigSelect, String sIjImplClass)
        {
            int result = 0;
            
            if( (strConfigGroup!=null) && (strConfigItem!=null) && (strConfigSelect!=null) )
            {
                int arrLength = strConfigGroup.length;
                if( (strConfigGroup.length == arrLength) && (strConfigItem.length == arrLength) && (strConfigSelect.length == arrLength) )
                {
                    for(int i=0; i<arrLength; i++)
                    {
                        try
                        {
                            int intConfigGroup = Integer.parseInt(strConfigGroup[i]);
                            int intConfigItem = Integer.parseInt(strConfigItem[i]);
                            int intConfigSelect = Integer.parseInt(strConfigSelect[i]);
                            IjConfiguration objIjConfiguration = getObjIJConfiguration(intBoSystem, intConfigGroup, intConfigItem);
                            
                            // insert objIjConfiguration
                            if(objIjConfiguration.getOID() == 0)
                            {
                                objIjConfiguration.setBoSystem(intBoSystem);
                                objIjConfiguration.setConfigGroup(intConfigGroup);
                                objIjConfiguration.setConfigItem(intConfigItem);
                                objIjConfiguration.setConfigSelect(intConfigSelect);
                                objIjConfiguration.setSIjImplClass(sIjImplClass);                                
                                
                                insertExc(objIjConfiguration);                                
                            }
                            
                            // update objIjConfiguration
                            else
                            {
                                objIjConfiguration.setBoSystem(intBoSystem);
                                objIjConfiguration.setConfigGroup(intConfigGroup);
                                objIjConfiguration.setConfigItem(intConfigItem);
                                objIjConfiguration.setConfigSelect(intConfigSelect);
                                objIjConfiguration.setSIjImplClass(sIjImplClass);
                                
                                updateExc(objIjConfiguration);                                               
                            }
                            result++;
                        }
                        catch(Exception e)
                        {
                            System.out.println("Exc when storeIjConfPerBoSystem : " + e.toString());
                        }
                    }
                }
            }
            
            return result;
        }
}
