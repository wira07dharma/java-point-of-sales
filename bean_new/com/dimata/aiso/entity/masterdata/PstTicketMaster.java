/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.aiso.db.DBException;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.I_DBInterface;
import com.dimata.aiso.db.I_DBType;
import com.dimata.aiso.entity.masterdata.TicketMaster;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class PstTicketMaster extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc{
    
    public static final String TBL_AISO_TICKET_MASTER = "aiso_ticket_master";
    
    public static final int FLD_TICKET_MASTER_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_DESCRIPTION = 2;
    public static final int FLD_TYPE = 3;
    public static final int FLD_CONTACT_ID = 4;
    public static final int FLD_ACC_COGS_ID = 5;
    public static final int FLD_ACC_AP_ID = 6;
    
    public static String[] fieldNames = {
	"TICKET_MASTER_ID",
	"CODE",
	"DESCRIPTION",
	"TICKET_MASTER_TYPE",
	"CONTACT_ID",
	"ACC_COGS_ID",
	"ACC_AP_ID"
    };
    
    public static int[] fieldTypes = {
	TYPE_PK + TYPE_ID + TYPE_LONG,
	TYPE_STRING,
	TYPE_STRING,
	TYPE_INT,
	TYPE_LONG,
	TYPE_LONG,
	TYPE_LONG
    };
    
    public static final int MASTER_CARRIER = 0;
    public static final int MASTER_ROUTE = 1;
    public static final int MASTER_CLASS = 2;
    
    public PstTicketMaster(){
    
    }
    
    public PstTicketMaster(int i) throws DBException{
	super(new PstTicketMaster());
    }
    
    public PstTicketMaster(String sOid) throws DBException{
	super(new PstTicketMaster(0));
	if(!locate(sOid))
	    throw new DBException(this, DBException.RECORD_NOT_FOUND);
	else
	    return;
    }
    
    public PstTicketMaster(long lOid) throws DBException{
	super(new PstTicketMaster(0));
	String sOid = "0";
	try{
	    sOid = String.valueOf(lOid);
	}catch(Exception e){
	    throw new DBException(this,DBException.RECORD_NOT_FOUND);
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
        TicketMaster objTicketMaster = PstTicketMaster.fetchExc(ent.getOID()); 
        ent = (Entity) objTicketMaster;
        return objTicketMaster.getOID();
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
        return new PstTicketMaster().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_TICKET_MASTER;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((TicketMaster) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((TicketMaster) ent);
    }
    
     public static TicketMaster fetchExc(long Oid) throws DBException {
        try {
            TicketMaster objTicketMaster = new TicketMaster();
            PstTicketMaster pstTicketMaster = new PstTicketMaster(Oid);
            objTicketMaster.setOID(Oid);
            
            objTicketMaster.setCode(pstTicketMaster.getString(FLD_CODE));
            objTicketMaster.setDescription(pstTicketMaster.getString(FLD_DESCRIPTION));
            objTicketMaster.setType(pstTicketMaster.getInt(FLD_TYPE));
            objTicketMaster.setContactId(pstTicketMaster.getlong(FLD_CONTACT_ID));
            objTicketMaster.setAccCogsId(pstTicketMaster.getlong(FLD_ACC_COGS_ID));
            objTicketMaster.setAccApId(pstTicketMaster.getlong(FLD_ACC_AP_ID));

            return objTicketMaster;  
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketMaster(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(TicketMaster objTicketMaster) throws DBException {
        try {
            PstTicketMaster pstTicketMaster = new PstTicketMaster(0);
            
            pstTicketMaster.setString(FLD_CODE, objTicketMaster.getCode());
            pstTicketMaster.setString(FLD_DESCRIPTION, objTicketMaster.getDescription());
            pstTicketMaster.setInt(FLD_TYPE, objTicketMaster.getType());
            pstTicketMaster.setLong(FLD_CONTACT_ID, objTicketMaster.getContactId());
            pstTicketMaster.setLong(FLD_ACC_COGS_ID, objTicketMaster.getAccCogsId());
            pstTicketMaster.setLong(FLD_ACC_AP_ID, objTicketMaster.getAccApId());

            pstTicketMaster.insert();
            objTicketMaster.setOID(pstTicketMaster.getlong(FLD_TICKET_MASTER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketMaster(0), DBException.UNKNOWN);
        }
        return objTicketMaster.getOID();
    }

    public static long updateExc(TicketMaster objTicketMaster) throws DBException {
        try {
            if (objTicketMaster != null && objTicketMaster.getOID() != 0) {
                PstTicketMaster pstTicketMaster = new PstTicketMaster(objTicketMaster.getOID());
               
                pstTicketMaster.setString(FLD_CODE, objTicketMaster.getCode());
		pstTicketMaster.setString(FLD_DESCRIPTION, objTicketMaster.getDescription());
		pstTicketMaster.setInt(FLD_TYPE, objTicketMaster.getType());
		pstTicketMaster.setLong(FLD_CONTACT_ID, objTicketMaster.getContactId());
		pstTicketMaster.setLong(FLD_ACC_COGS_ID, objTicketMaster.getAccCogsId());
		pstTicketMaster.setLong(FLD_ACC_AP_ID, objTicketMaster.getAccApId());

                pstTicketMaster.update();
                return objTicketMaster.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketMaster(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstTicketMaster pstTicketMaster = new PstTicketMaster(Oid);
            pstTicketMaster.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketMaster(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_TICKET_MASTER + " ";
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
                TicketMaster objTicketMaster = new TicketMaster();
                resultToObject(rs, objTicketMaster);
                lists.add(objTicketMaster);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstTicketMaster().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, TicketMaster objTicketMaster) {
        try {
            objTicketMaster.setOID(rs.getLong(fieldNames[PstTicketMaster.FLD_TICKET_MASTER_ID]));
            objTicketMaster.setCode(rs.getString(fieldNames[PstTicketMaster.FLD_CODE]));
            objTicketMaster.setDescription(rs.getString(fieldNames[PstTicketMaster.FLD_DESCRIPTION]));
            objTicketMaster.setType(rs.getInt(fieldNames[PstTicketMaster.FLD_TYPE]));
            objTicketMaster.setContactId(rs.getLong(fieldNames[PstTicketMaster.FLD_CONTACT_ID]));
            objTicketMaster.setAccCogsId(rs.getLong(fieldNames[PstTicketMaster.FLD_ACC_COGS_ID]));
            objTicketMaster.setAccApId(rs.getLong(fieldNames[PstTicketMaster.FLD_ACC_AP_ID]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[PstTicketMaster.FLD_TICKET_MASTER_ID] + ") " +
                    " FROM " + TBL_AISO_TICKET_MASTER;
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
