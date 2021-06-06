/*
 * PstForwarderInfo.java
 *
 * Created on May 30, 2007, 6:16 PM
 */

package com.dimata.posbo.entity.warehouse;

/* package java */
import java.sql.ResultSet;
import java.util.Vector;

/* package qdep */
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

/* package common */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.custom.PstDataCustom;
import org.json.JSONObject;

/**
 *
 * @author  gwawan
 */
public class PstForwarderInfo extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    public static final String TBL_FORWARDER_INFO = "pos_forwarder_info";
    public static final int FLD_FORWARDER_ID = 0;
    public static final int FLD_RECEIVE_ID = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final int FLD_DOC_NUMBER = 3;
    public static final int FLD_DOC_DATE = 4;
    public static final int FLD_CONTACT_ID = 5;
    public static final int FLD_CURRENCY_ID = 6;
    public static final int FLD_TRANS_RATE = 7;
    public static final int FLD_TOTAL_COST = 8;
    public static final int FLD_NOTES = 9;
    public static final int FLD_STATUS = 10;
    public static final int FLD_COUNTER = 11;
    
    public static final String fieldNames[] = {
        "FORWARDER_ID",
        "RECEIVE_ID",
        "LOCATION_ID",
        "DOC_NUMBER",
        "DOC_DATE",
        "CONTACT_ID",
        "CURRENCY_ID",
        "TRANS_RATE",
        "TOTAL_COST",
        "NOTES",
        "STATUS",
        "COUNTER"
    };
    
    public static final int fieldTypes[] = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };
    
    /** Creates a new instance of PstForwarderInfo */
    public PstForwarderInfo() {
    }
    
    public PstForwarderInfo(int i) throws DBException {
        super(new PstForwarderInfo());
    }
    
    public PstForwarderInfo(String sOid) throws DBException {
        super(new PstForwarderInfo(0));
        if(!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        else
            return;
    }
    
    public PstForwarderInfo(long lOid) throws DBException {
        super(new PstForwarderInfo(0));
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
    
    public String[] getFieldNames() {
        return this.fieldNames;
    }
    
    public int getFieldSize() {
        return this.fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return this.fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstForwarderInfo().getClass().getName();
    }
    
    public String getTableName() {
        return this.TBL_FORWARDER_INFO;
    }
    
    public long deleteExc(com.dimata.qdep.entity.Entity ent) throws Exception {
        if(ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public long fetchExc(com.dimata.qdep.entity.Entity ent) throws Exception {
        ForwarderInfo forwarderInfo = fetchExc(ent.getOID());
        ent = (com.dimata.qdep.entity.Entity)forwarderInfo;
        return forwarderInfo.getOID();
    }
    
    public long insertExc(com.dimata.qdep.entity.Entity ent) throws Exception {
        return insertExc((ForwarderInfo)ent);
    }
    
    public long updateExc(com.dimata.qdep.entity.Entity ent) throws Exception {
        return deleteExc((ForwarderInfo)ent);
    }
    
    public static ForwarderInfo fetchExc(long oid) throws DBException {
        try {
            ForwarderInfo forwarderInfo = new ForwarderInfo();
            PstForwarderInfo pstForwarderInfo = new PstForwarderInfo(oid);
            forwarderInfo.setOID(oid);
            try {
                forwarderInfo.setReceiveId(pstForwarderInfo.getlong(FLD_RECEIVE_ID));
                forwarderInfo.setLocationId(pstForwarderInfo.getlong(FLD_LOCATION_ID));
                forwarderInfo.setDocNumber(pstForwarderInfo.getString(FLD_DOC_NUMBER));
                forwarderInfo.setDocDate(pstForwarderInfo.getDate(FLD_DOC_DATE));
                forwarderInfo.setContactId(pstForwarderInfo.getlong(FLD_CONTACT_ID));
                forwarderInfo.setCurrencyId(pstForwarderInfo.getlong(FLD_CURRENCY_ID));
                forwarderInfo.setTransRate(pstForwarderInfo.getdouble(FLD_TRANS_RATE));
                forwarderInfo.setTotalCost(pstForwarderInfo.getdouble(FLD_TOTAL_COST));
                forwarderInfo.setNotes(pstForwarderInfo.getString(FLD_NOTES));
                forwarderInfo.setStatus(pstForwarderInfo.getInt(FLD_STATUS));
                forwarderInfo.setCounter(pstForwarderInfo.getInt(FLD_COUNTER));
            }
            catch (Exception e) {
                System.out.println("ERROR in fetchExc >>> "+e.toString());
            }
            return forwarderInfo;
        }
        catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstForwarderInfo(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(ForwarderInfo forwarderInfo) throws DBException {
        try {
            PstForwarderInfo pstForwarderInfo = new PstForwarderInfo(0);
            
            pstForwarderInfo.setLong(FLD_RECEIVE_ID, forwarderInfo.getReceiveId());
            pstForwarderInfo.setLong(FLD_LOCATION_ID, forwarderInfo.getLocationId());
            pstForwarderInfo.setString(FLD_DOC_NUMBER, forwarderInfo.getDocNumber());
            pstForwarderInfo.setDate(FLD_DOC_DATE, forwarderInfo.getDocDate());
            pstForwarderInfo.setLong(FLD_CONTACT_ID, forwarderInfo.getContactId());
            pstForwarderInfo.setLong(FLD_CURRENCY_ID, forwarderInfo.getCurrencyId());
            pstForwarderInfo.setDouble(FLD_TRANS_RATE, forwarderInfo.getTransRate());
            pstForwarderInfo.setDouble(FLD_TOTAL_COST, forwarderInfo.getTotalCost());
            pstForwarderInfo.setString(FLD_NOTES, forwarderInfo.getNotes());
            pstForwarderInfo.setInt(FLD_STATUS, forwarderInfo.getStatus());
            pstForwarderInfo.setInt(FLD_COUNTER, forwarderInfo.getCounter());
            
            pstForwarderInfo.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstForwarderInfo.getInsertSQL());
            forwarderInfo.setOID(pstForwarderInfo.getlong(FLD_FORWARDER_ID));
        }
        catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstForwarderInfo(0), DBException.UNKNOWN);
        }
        return forwarderInfo.getOID();
    }
    
    public static long updateExc(ForwarderInfo forwarderInfo) throws DBException {
        try {
            if(forwarderInfo.getOID() != 0) {
                PstForwarderInfo pstForwarderInfo = new PstForwarderInfo(forwarderInfo.getOID());
                
                pstForwarderInfo.setLong(FLD_RECEIVE_ID, forwarderInfo.getReceiveId());
                pstForwarderInfo.setLong(FLD_LOCATION_ID, forwarderInfo.getLocationId());
                pstForwarderInfo.setString(FLD_DOC_NUMBER, forwarderInfo.getDocNumber());
                pstForwarderInfo.setDate(FLD_DOC_DATE, forwarderInfo.getDocDate());
                pstForwarderInfo.setLong(FLD_CONTACT_ID, forwarderInfo.getContactId());
                pstForwarderInfo.setLong(FLD_CURRENCY_ID, forwarderInfo.getCurrencyId());
                pstForwarderInfo.setDouble(FLD_TRANS_RATE, forwarderInfo.getTransRate());
                pstForwarderInfo.setDouble(FLD_TOTAL_COST, forwarderInfo.getTotalCost());
                pstForwarderInfo.setString(FLD_NOTES, forwarderInfo.getNotes());
                pstForwarderInfo.setInt(FLD_STATUS, forwarderInfo.getStatus());
                pstForwarderInfo.setInt(FLD_COUNTER, forwarderInfo.getCounter());
                
                pstForwarderInfo.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstForwarderInfo.getUpdateSQL());
                return forwarderInfo.getOID();
            }
        }
        catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstForwarderInfo(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstForwarderInfo pstForwarderInfo = new PstForwarderInfo(oid);
            pstForwarderInfo.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstForwarderInfo.getDeleteSQL());
        }
        catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstForwarderInfo(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_FORWARDER_INFO;
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
                    ;
            }


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ForwarderInfo forwarderInfo = new ForwarderInfo();
                resultToObject(rs, forwarderInfo);
                lists.add(forwarderInfo);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static void resultToObject(ResultSet rs, ForwarderInfo forwarderInfo) {
        try {
            forwarderInfo.setOID(rs.getLong(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_FORWARDER_ID]));
            forwarderInfo.setReceiveId(rs.getLong(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID]));
            forwarderInfo.setLocationId(rs.getLong(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_LOCATION_ID]));
            forwarderInfo.setDocNumber(rs.getString(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_NUMBER]));
            forwarderInfo.setDocDate(rs.getDate(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_DATE]));
            forwarderInfo.setContactId(rs.getLong(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CONTACT_ID]));
            forwarderInfo.setCurrencyId(rs.getLong(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CURRENCY_ID]));
            forwarderInfo.setTransRate(rs.getDouble(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_TRANS_RATE]));
            forwarderInfo.setTotalCost(rs.getDouble(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_TOTAL_COST]));
            forwarderInfo.setNotes(rs.getString(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_NOTES]));
            forwarderInfo.setStatus(rs.getInt(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_STATUS]));
            forwarderInfo.setCounter(rs.getInt(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_COUNTER]));
        } catch (Exception e) {
            System.out.println("Exc in resultToObject >>> " + e.toString());
        }
    }
    public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         ForwarderInfo forwarderInfo = PstForwarderInfo.fetchExc(oid);
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_FORWARDER_ID], forwarderInfo.getOID());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID], forwarderInfo.getReceiveId());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_LOCATION_ID], forwarderInfo.getLocationId());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_NUMBER], forwarderInfo.getDocNumber());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_DATE], forwarderInfo.getDocDate());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CONTACT_ID], forwarderInfo.getContactId());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CURRENCY_ID], forwarderInfo.getCurrencyId());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_TRANS_RATE], forwarderInfo.getTransRate());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_TOTAL_COST], forwarderInfo.getTotalCost());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_NOTES], forwarderInfo.getNotes());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_STATUS], forwarderInfo.getStatus());
         object.put(PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_COUNTER], forwarderInfo.getCounter());
      }catch(Exception exc){}
      return object;
   }   
}
