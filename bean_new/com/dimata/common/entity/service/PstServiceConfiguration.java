/*
 * PstServiceConfiguration.java
 *
 * Created on September 27, 2004, 9:18 PM
 */

package com.dimata.common.entity.service;

/* package java */ 
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

/**
 *
 * @author  gedhy
 */
public class PstServiceConfiguration  extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final  String TBL_SERVICE_CONF = "service_conf";

    public static final  int FLD_SERVICE_ID = 0;
    public static final  int FLD_SERVICE_TYPE = 1;
    public static final  int FLD_START_TIME = 2;
    public static final  int FLD_PERIODE = 3;

    public static final  String[] fieldNames = {
            "SERVICE_ID",
            "SERVICE_TYPE",
            "START_TIME",
            "PERIODE"
     }; 

    public static final  int[] fieldTypes = {
            TYPE_LONG + TYPE_PK + TYPE_ID,
            TYPE_INT,
            TYPE_DATE,
            TYPE_INT
     }; 

    public static int SERVICE_TYPE_PRESENCE = 0;  
    public static int SERVICE_TYPE_ABSENCE = 1;
    public static int SERVICE_TYPE_LATENESS = 2;
    public static int SERVICE_TYPE_LEAVE = 3;        
    public static int SERVICE_TYPE_BACKUPDB = 4;  
    
    public static String[][] resultText = {
        {"Kehadiran", "Absen", "Terlambat", "Cuti", "Backup Database"},
        {"Presence", "Absence", "Lateness", "Leave", "Backup Database"}
    };
     
    public PstServiceConfiguration(){
    }

    public PstServiceConfiguration(int i) throws DBException { 
            super(new PstServiceConfiguration()); 
    }

    public PstServiceConfiguration(String sOid) throws DBException { 
            super(new PstServiceConfiguration(0)); 
            if(!locate(sOid)) 
                    throw new DBException(this,DBException.RECORD_NOT_FOUND); 
            else 
                    return; 
    }

    public PstServiceConfiguration(long lOid) throws DBException { 
            super(new PstServiceConfiguration(0)); 
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
            return TBL_SERVICE_CONF;
    }

    public String[] getFieldNames(){ 
            return fieldNames; 
    }

    public int[] getFieldTypes(){ 
            return fieldTypes; 
    }

    public String getPersistentName(){ 
            return new PstServiceConfiguration().getClass().getName(); 
    }

    public long fetchExc(Entity ent) throws Exception{ 
            ServiceConfiguration serviceConfiguration = fetchExc(ent.getOID()); 
            ent = (Entity)serviceConfiguration; 
            return serviceConfiguration.getOID(); 
    }

    public long insertExc(Entity ent) throws Exception{ 
            return insertExc((ServiceConfiguration) ent); 
    }

    public long updateExc(Entity ent) throws Exception{ 
            return updateExc((ServiceConfiguration) ent); 
    }

    public long deleteExc(Entity ent) throws Exception{ 
            if(ent==null){ 
                    throw new DBException(this,DBException.RECORD_NOT_FOUND); 
            } 
            return deleteExc(ent.getOID()); 
    }

    public static ServiceConfiguration fetchExc(long oid) throws DBException{ 
            try{ 
                    ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
                    PstServiceConfiguration pstServiceConfiguration = new PstServiceConfiguration(oid); 
                    serviceConfiguration.setOID(oid);

                    serviceConfiguration.setServiceType(pstServiceConfiguration.getInt(FLD_SERVICE_TYPE));
                    serviceConfiguration.setStartTime(pstServiceConfiguration.getDate(FLD_START_TIME));
                    serviceConfiguration.setPeriode(pstServiceConfiguration.getInt(FLD_PERIODE));
                    

                    return serviceConfiguration; 
            }catch(DBException dbe){ 
                    throw dbe; 
            }catch(Exception e){ 
                    throw new DBException(new PstServiceConfiguration(0),DBException.UNKNOWN); 
            } 
    }

    public static long insertExc(ServiceConfiguration serviceConfiguration) throws DBException{ 
            try{ 
                    PstServiceConfiguration pstServiceConfiguration = new PstServiceConfiguration(0);

                    pstServiceConfiguration.setInt(FLD_SERVICE_TYPE, serviceConfiguration.getServiceType());
                    pstServiceConfiguration.setDate(FLD_START_TIME, serviceConfiguration.getStartTime());
                    pstServiceConfiguration.setInt(FLD_PERIODE, serviceConfiguration.getPeriode());                    

                    pstServiceConfiguration.insert(); 
                    serviceConfiguration.setOID(pstServiceConfiguration.getlong(FLD_SERVICE_ID));
            }catch(DBException dbe){ 
                    throw dbe; 
            }catch(Exception e){ 
                    throw new DBException(new PstServiceConfiguration(0),DBException.UNKNOWN); 
            }
            return serviceConfiguration.getOID();
    }

    public static long updateExc(ServiceConfiguration serviceConfiguration) throws DBException{ 
            try{ 
                    if(serviceConfiguration.getOID() != 0){ 
                            PstServiceConfiguration pstServiceConfiguration = new PstServiceConfiguration(serviceConfiguration.getOID());

                            pstServiceConfiguration.setInt(FLD_SERVICE_TYPE, serviceConfiguration.getServiceType());
                            pstServiceConfiguration.setDate(FLD_START_TIME, serviceConfiguration.getStartTime());
                            pstServiceConfiguration.setInt(FLD_PERIODE, serviceConfiguration.getPeriode());                            

                            pstServiceConfiguration.update(); 
                            return serviceConfiguration.getOID();

                    }
            }catch(DBException dbe){ 
                    throw dbe; 
            }catch(Exception e){ 
                    throw new DBException(new PstServiceConfiguration(0),DBException.UNKNOWN); 
            }
            return 0;
    }

    public static long deleteExc(long oid) throws DBException{ 
            try{ 
                    PstServiceConfiguration pstServiceConfiguration = new PstServiceConfiguration(oid);
                    pstServiceConfiguration.delete();
            }catch(DBException dbe){ 
                    throw dbe; 
            }catch(Exception e){ 
                    throw new DBException(new PstServiceConfiguration(0),DBException.UNKNOWN); 
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
                    String sql = "SELECT * FROM " + TBL_SERVICE_CONF; 
                    if(whereClause != null && whereClause.length() > 0)
                            sql = sql + " WHERE " + whereClause;
                    if(order != null && order.length() > 0)
                            sql = sql + " ORDER BY " + order;
                    if(limitStart == 0 && recordToGet == 0)
                            sql = sql + "";
                    else
                            sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
//                    System.out.println("SQL Service conf : " + sql);
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();
                    while(rs.next()) {
                            ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
                            resultToObject(rs, serviceConfiguration);
                            lists.add(serviceConfiguration);
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

    public static void resultToObject(ResultSet rs, ServiceConfiguration serviceConfiguration){
            try{
                    serviceConfiguration.setOID(rs.getLong(PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_SERVICE_ID]));
                    serviceConfiguration.setServiceType(rs.getInt(PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_SERVICE_TYPE]));
                    Date dtStartTime = DBHandler.convertDate(rs.getDate(PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_START_TIME]), rs.getTime(PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_START_TIME]));
                    //System.out.println("dtStartTime : " + dtStartTime);
                    serviceConfiguration.setStartTime(dtStartTime);
                    serviceConfiguration.setPeriode(rs.getInt(PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_PERIODE]));                    

            }catch(Exception e){
                System.out.println("resultToObject exc : " + e.toString());
            }
    }

    public static int getCount(String whereClause){
            DBResultSet dbrs = null;
            try {
                    String sql = "SELECT COUNT("+ PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_SERVICE_ID] + ") FROM " + TBL_SERVICE_CONF;
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
    
    
    public static ServiceConfiguration getSvcConfigurationByType(int svcType)
    {
            ServiceConfiguration result = new ServiceConfiguration();             
            try 
            {
                    String whereClause = PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_SERVICE_TYPE] + "=" + svcType;
                    Vector vectResult = list(0, 0, whereClause, "");
                    if(vectResult!=null && vectResult.size()>0)
                    {
                        result = (ServiceConfiguration)vectResult.get(0);
                    }
                    return result;   

            }
            catch(Exception e) 
            {
                    System.out.println(e);
            }
            return result;
    }
    
}
