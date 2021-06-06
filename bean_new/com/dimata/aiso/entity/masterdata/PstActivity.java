/*
 * PstActivity.java
 *
 * Created on January 12, 2007, 9:08 AM
 */

package com.dimata.aiso.entity.masterdata;

/* import package aiso*/
import com.dimata.aiso.db.*; 

/* import package qdep*/
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package java util*/
import java.util.Vector;

/* import package java sql*/
import java.sql.ResultSet;
/**
 *
 * @author  dwi
 */
public class PstActivity extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    public static final String TBL_AISO_ACTIVITY = "aiso_activity";
    public static final int FLD_ACTIVITY_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_DESCRIPTION = 2;
    public static final int FLD_PARENT_ID = 3;
    public static final int FLD_POSTED = 4;
    public static final int FLD_ACT_TYPE = 5;
    public static final int FLD_ASSUMP_AND_RISK = 6;
    public static final int FLD_OUTPUT_AND_DELV = 7;
    public static final int FLD_PERFM_INDICT = 8;
    public static final int FLD_COST_IMPL = 9;
    public static final int FLD_ACT_LEVEL = 10;
       
    
    public static String [] fieldNames = {
        "ACTIVITY_ID",
        "CODE",
        "DESCRIPTION",        
        "PARENT_ID",
        "POSTED",
        "ACT_TYPE",        
        "ASSUMP_AND_RISK",
        "OUTPUT_AND_DELV",
        "PERFM_INDICT",
        "COST_IMPL",
        "ACT_LEVEL"
    };
    
    public static int [] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,     
        TYPE_FK + TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT
    };
    
    public static final int ACT_NOTPOSTED = 0;
    public static final int ACT_POSTED = 1;
    
    public static String[] actPosted = {
        "HEADER",
        "POSTABLE"
    };
    
    public static final int ACT_PROGRAMATIC = 0;
    public static final int ACT_SUPPORT = 1;
    
    public static String[] actAssign = {
        "PROGRAMATIC",
        "SUPPORT"
    };
    
   
    public static final int LEVEL_MODULE = 0;
    public static final int LEVEL_SUB_MODULE = 1;
    public static final int LEVEL_HEADER = 2;
    public static final int LEVEL_ACTIVITY = 3;
    
    public static String[] actLevel = {
        "M","S","H","A"
    };
    
    /** Creates a new instance of PstActivity */
    public PstActivity() {
    }
    
     public PstActivity(int i) throws DBException {
        super(new PstActivity());
    }
    
    public PstActivity(String sOid) throws DBException {
        super(new PstActivity(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstActivity(long lOid) throws DBException {
        super(new PstActivity(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }

        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public long fetchExc(Entity ent) throws Exception {
        Activity ojbActivity = PstActivity.fetchExc(ent.getOID());
        ent = (Entity) ojbActivity;
        return ojbActivity.getOID();
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstActivity().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_ACTIVITY;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((Activity) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((Activity) ent);
    }
    
     public static Activity fetchExc(long Oid) throws DBException {
        try {
            Activity ojbActivity = new Activity();
            PstActivity pstActivity = new PstActivity(Oid);
            ojbActivity.setOID(Oid);
            ojbActivity.setCode(pstActivity.getString(FLD_CODE));            
            ojbActivity.setDescription(pstActivity.getString(FLD_DESCRIPTION));
            ojbActivity.setIdParent(pstActivity.getlong(FLD_PARENT_ID));  
            ojbActivity.setPosted(pstActivity.getInt(FLD_POSTED));
            ojbActivity.setType(pstActivity.getInt(FLD_ACT_TYPE));
            ojbActivity.setOutPutandDelv(pstActivity.getString(FLD_OUTPUT_AND_DELV));
            ojbActivity.setPerfmIndict(pstActivity.getString(FLD_PERFM_INDICT));
            ojbActivity.setAssumpAndRisk(pstActivity.getString(FLD_ASSUMP_AND_RISK));
            ojbActivity.setCostImpl(pstActivity.getString(FLD_COST_IMPL));
            ojbActivity.setActLevel(pstActivity.getInt(FLD_ACT_LEVEL));
                     
            return ojbActivity;
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivity(0), DBException.UNKNOWN);
        }
    }
     
      public static long insertExc(Activity ojbActivity) throws DBException {
        try {
            PstActivity pstActivity = new PstActivity(0);

            pstActivity.setString(FLD_CODE, ojbActivity.getCode());            
            pstActivity.setString(FLD_DESCRIPTION, ojbActivity.getDescription());
            pstActivity.setLong(FLD_PARENT_ID, ojbActivity.getIdParent());
            pstActivity.setInt(FLD_POSTED, ojbActivity.getPosted());
            pstActivity.setInt(FLD_ACT_TYPE, ojbActivity.getType());
            pstActivity.setString(FLD_OUTPUT_AND_DELV, ojbActivity.getOutPutandDelv());
            pstActivity.setString(FLD_PERFM_INDICT, ojbActivity.getPerfmIndict());
            pstActivity.setString(FLD_ASSUMP_AND_RISK, ojbActivity.getAssumpAndRisk());
            pstActivity.setString(FLD_COST_IMPL, ojbActivity.getCostImpl());
            pstActivity.setInt(FLD_ACT_LEVEL, ojbActivity.getActLevel());
            
            pstActivity.insert();
            ojbActivity.setOID(pstActivity.getlong(FLD_ACTIVITY_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivity(0), DBException.UNKNOWN);
        }
        return ojbActivity.getOID();
    }
    
      public static long updateExc(Activity ojbActivity) throws DBException {
        try {
            if (ojbActivity != null && ojbActivity.getOID() != 0) {
                PstActivity pstActivity = new PstActivity(ojbActivity.getOID());

                pstActivity.setString(FLD_CODE, ojbActivity.getCode());                
                pstActivity.setString(FLD_DESCRIPTION, ojbActivity.getDescription());
                pstActivity.setLong(FLD_PARENT_ID, ojbActivity.getIdParent());
                pstActivity.setInt(FLD_POSTED, ojbActivity.getPosted());
                pstActivity.setInt(FLD_ACT_TYPE, ojbActivity.getType());
                pstActivity.setString(FLD_OUTPUT_AND_DELV, ojbActivity.getOutPutandDelv());
                pstActivity.setString(FLD_PERFM_INDICT, ojbActivity.getPerfmIndict());
                pstActivity.setString(FLD_ASSUMP_AND_RISK, ojbActivity.getAssumpAndRisk());
                pstActivity.setString(FLD_COST_IMPL, ojbActivity.getCostImpl());
                pstActivity.setInt(FLD_ACT_LEVEL, ojbActivity.getActLevel());

                pstActivity.update();
                return ojbActivity.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivity(0), DBException.UNKNOWN);
        }
        return 0;
    }
      
      public static long deleteExc(long Oid) throws DBException {
        try {
            PstActivity pstActivity = new PstActivity(Oid);
            pstActivity.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivity(0), DBException.UNKNOWN);
        }
        return Oid;
    }
      
       public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_ACTIVITY + " ";
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("List sql PstActivity ===> "+sql);
            
            while (rs.next()) {
                Activity ojbActivity = new Activity();
                resultToObject(rs, ojbActivity);
                lists.add(ojbActivity);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new Activity().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
       
      
       
       private static void resultToObject(ResultSet rs, Activity ojbActivity) {
        try {

            ojbActivity.setOID(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]));
            ojbActivity.setCode(rs.getString(PstActivity.fieldNames[PstActivity.FLD_CODE]));            
            ojbActivity.setDescription(rs.getString(PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]));
            ojbActivity.setIdParent(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]));
            ojbActivity.setPosted(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_POSTED]));
            ojbActivity.setType(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]));
            ojbActivity.setOutPutandDelv(rs.getString(PstActivity.fieldNames[PstActivity.FLD_OUTPUT_AND_DELV]));
            ojbActivity.setPerfmIndict(rs.getString(PstActivity.fieldNames[PstActivity.FLD_PERFM_INDICT]));
            ojbActivity.setAssumpAndRisk(rs.getString(PstActivity.fieldNames[PstActivity.FLD_ASSUMP_AND_RISK]));
            ojbActivity.setCostImpl(rs.getString(PstActivity.fieldNames[PstActivity.FLD_COST_IMPL]));
            ojbActivity.setActLevel(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }
       
       public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID] + ") " +
                    " FROM " + TBL_AISO_ACTIVITY;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            System.out.println("sql get count ===> "+sql);
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
       
       public static void main(String[] arg){
            PstActivity pstActivity = new PstActivity();
            
            for(int i=0; i < pstActivity.actLevel.length; i++){
                System.out.println("pstActivity.actLevel indek ke "+i+" : "+pstActivity.actLevel[i]);
            }
       }

}
