/*
 * PstAktivaLocation.java
 *
 * Created on February 25, 2008, 5:07 PM
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.Hashtable;

/**
 *
 * @author  DWI
 */
public class PstAktivaLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_AKTIVA_LOCATION = "aiso_aktiva_location";
    
    public static final int FLD_AKTIVA_LOCATION_ID = 0;
    public static final int FLD_AKTIVA_LOC_CODE = 1;
    public static final int FLD_AKTIVA_LOC_NAME = 2;
    
    public static String[] fieldNames = {
        "AKTIVA_LOC_ID","AKTIVA_LOC_CODE","AKTIVA_LOC_NAME"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING
    };
    
    /** Creates a new instance of PstAktivaLocation */
    public PstAktivaLocation() {
    }
    
    public PstAktivaLocation(int i) throws DBException {
        super(new PstAktivaLocation());
    }
    
    public PstAktivaLocation(String sOid) throws DBException {
        super(new PstAktivaLocation(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstAktivaLocation(long lOid) throws DBException {
        super(new PstAktivaLocation(0));
        String sOid = "0";
        try{
            sOid = String.valueOf(lOid);
        }catch(Exception e){
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        
        if(!locate(sOid))
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
        AktivaLocation ojbAktivaLocation = PstAktivaLocation.fetchExc(ent.getOID()); 
        ent = (Entity) ojbAktivaLocation;
        return ojbAktivaLocation.getOID();
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
        return new PstAktivaLocation().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_AKTIVA_LOCATION;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((AktivaLocation) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((AktivaLocation) ent);
    }
    
     public static AktivaLocation fetchExc(long Oid) throws DBException {
        try {
            AktivaLocation objAktivaLoc = new AktivaLocation();
            PstAktivaLocation pstAktivaLoc = new PstAktivaLocation(Oid);
            objAktivaLoc.setOID(Oid);
            
            objAktivaLoc.setAktivaLocCode(pstAktivaLoc.getString(FLD_AKTIVA_LOC_CODE));
            objAktivaLoc.setAktivaLocName(pstAktivaLoc.getString(FLD_AKTIVA_LOC_NAME));

            return objAktivaLoc;  
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAktivaLocation(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(AktivaLocation objAktivaLoc) throws DBException {
        try {
            PstAktivaLocation pstAktivaLoc = new PstAktivaLocation(0);
            
            pstAktivaLoc.setString(FLD_AKTIVA_LOC_CODE, objAktivaLoc.getAktivaLocCode());
            pstAktivaLoc.setString(FLD_AKTIVA_LOC_NAME, objAktivaLoc.getAktivaLocName());

            pstAktivaLoc.insert();
            objAktivaLoc.setOID(pstAktivaLoc.getlong(FLD_AKTIVA_LOCATION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAktivaLocation(0), DBException.UNKNOWN);
        }
        return objAktivaLoc.getOID();
    }

    public static long updateExc(AktivaLocation objAktivaLoc) throws DBException {
        try {
            if (objAktivaLoc != null && objAktivaLoc.getOID() != 0) {
                PstAktivaLocation pstAktivaLoc = new PstAktivaLocation(objAktivaLoc.getOID());
               
                pstAktivaLoc.setString(FLD_AKTIVA_LOC_CODE, objAktivaLoc.getAktivaLocCode());
                pstAktivaLoc.setString(FLD_AKTIVA_LOC_NAME, objAktivaLoc.getAktivaLocName());

                pstAktivaLoc.update();
                return objAktivaLoc.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAktivaLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstAktivaLocation pstAktivaLoc = new PstAktivaLocation(Oid);
            pstAktivaLoc.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAktivaLocation(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_AKTIVA_LOCATION + " ";
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

            while (rs.next()) {
                AktivaLocation objAktivaLoc = new AktivaLocation();
                resultToObject(rs, objAktivaLoc);
                lists.add(objAktivaLoc);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstAktivaLocation().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, AktivaLocation objAktivaLoc) {
        try {
            objAktivaLoc.setOID(rs.getLong(PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOCATION_ID]));
            objAktivaLoc.setAktivaLocCode(rs.getString(PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOC_CODE]));
            objAktivaLoc.setAktivaLocName(rs.getString(PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOC_NAME]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOCATION_ID] + ") " +
                    " FROM " + TBL_AISO_AKTIVA_LOCATION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

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
}
