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
import com.dimata.aiso.entity.masterdata.TicketRate;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class PstTicketRate extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc{
    
    public static final String TBL_AISO_TICKET_PRICE = "aiso_ticket_price";
    
    public static final int FLD_TICKET_PRICE_ID = 0;
    public static final int FLD_CARRIER_ID = 1;
    public static final int FLD_ROUTE_ID = 2;
    public static final int FLD_CLASS_ID = 3;
    public static final int FLD_RATE = 4;
    public static final int FLD_NET_RATE_TO_AIRLINE = 5;
    
    public static String[] fieldNames = {
	"TICKET_PRICE_ID",
	"CARRIER_ID",
	"ROUTE_ID",
	"CLASS_ID",
	"RATE",
	"NET_RATE_TO_AIRLINE"
    };
    
    public static int[] fieldTypes = {
	TYPE_PK + TYPE_ID + TYPE_LONG,
	TYPE_LONG,
	TYPE_LONG,
	TYPE_LONG,
	TYPE_FLOAT,
	TYPE_FLOAT
    };
    
    public PstTicketRate(){
    
    }
    
    public PstTicketRate(int i) throws DBException{
	super(new PstTicketRate());
    }
    
    public PstTicketRate(String sOid) throws DBException{
	super(new PstTicketRate(0));
	if(!locate(sOid))
	    throw new DBException(this, DBException.RECORD_NOT_FOUND);
	else
	    return;
    }
    
    public PstTicketRate(long lOid) throws DBException{
	super(new PstTicketRate(0));
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
        TicketRate objTicketRate = PstTicketRate.fetchExc(ent.getOID()); 
        ent = (Entity) objTicketRate;
        return objTicketRate.getOID();
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
        return new PstTicketRate().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_TICKET_PRICE;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((TicketRate) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((TicketRate) ent);
    }
    
     public static TicketRate fetchExc(long Oid) throws DBException {
        try {
            TicketRate objTicketRate = new TicketRate();
            PstTicketRate pstTicketRate = new PstTicketRate(Oid);
            objTicketRate.setOID(Oid);
            
            objTicketRate.setCarrierId(pstTicketRate.getlong(FLD_CARRIER_ID));
            objTicketRate.setRouteId(pstTicketRate.getlong(FLD_ROUTE_ID));
            objTicketRate.setClassId(pstTicketRate.getlong(FLD_CLASS_ID));
            objTicketRate.setRate(pstTicketRate.getdouble(FLD_RATE));
            objTicketRate.setNetRateToAirLine(pstTicketRate.getdouble(FLD_NET_RATE_TO_AIRLINE));

            return objTicketRate;  
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketRate(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(TicketRate objTicketRate) throws DBException {
        try {
            PstTicketRate pstTicketRate = new PstTicketRate(0);
            
            pstTicketRate.setLong(FLD_CARRIER_ID, objTicketRate.getCarrierId());
            pstTicketRate.setLong(FLD_ROUTE_ID, objTicketRate.getRouteId());
            pstTicketRate.setLong(FLD_CLASS_ID, objTicketRate.getClassId());
            pstTicketRate.setDouble(FLD_RATE, objTicketRate.getRate());
            pstTicketRate.setDouble(FLD_NET_RATE_TO_AIRLINE, objTicketRate.getNetRateToAirLine());

            pstTicketRate.insert();
            objTicketRate.setOID(pstTicketRate.getlong(FLD_TICKET_PRICE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketRate(0), DBException.UNKNOWN);
        }
        return objTicketRate.getOID();
    }

    public static long updateExc(TicketRate objTicketRate) throws DBException {
        try {
            if (objTicketRate != null && objTicketRate.getOID() != 0) {
                PstTicketRate pstTicketRate = new PstTicketRate(objTicketRate.getOID());
               
                pstTicketRate.setLong(FLD_CARRIER_ID, objTicketRate.getCarrierId());
		pstTicketRate.setLong(FLD_ROUTE_ID, objTicketRate.getRouteId());
		pstTicketRate.setLong(FLD_CLASS_ID, objTicketRate.getClassId());
		pstTicketRate.setDouble(FLD_RATE, objTicketRate.getRate());
		pstTicketRate.setDouble(FLD_NET_RATE_TO_AIRLINE, objTicketRate.getNetRateToAirLine());

                pstTicketRate.update();
                return objTicketRate.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketRate(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstTicketRate pstTicketRate = new PstTicketRate(Oid);
            pstTicketRate.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketRate(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_TICKET_PRICE + " ";
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
	    
	    System.out.println("SQL ticket rate :::::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet(); 

            while (rs.next()) {
                TicketRate objTicketRate = new TicketRate();
                resultToObject(rs, objTicketRate);
                lists.add(objTicketRate);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstTicketMaster().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, TicketRate objTicketRate) {
        try {
            objTicketRate.setOID(rs.getLong(fieldNames[PstTicketRate.FLD_TICKET_PRICE_ID]));
            objTicketRate.setCarrierId(rs.getLong(fieldNames[PstTicketRate.FLD_CARRIER_ID]));
            objTicketRate.setRouteId(rs.getLong(fieldNames[PstTicketRate.FLD_ROUTE_ID]));
            objTicketRate.setClassId(rs.getLong(fieldNames[PstTicketRate.FLD_CLASS_ID]));
            objTicketRate.setRate(rs.getDouble(fieldNames[PstTicketRate.FLD_RATE]));
            objTicketRate.setNetRateToAirLine(rs.getDouble(fieldNames[PstTicketRate.FLD_NET_RATE_TO_AIRLINE]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[PstTicketRate.FLD_TICKET_PRICE_ID] + ") " +
                    " FROM " + TBL_AISO_TICKET_PRICE;
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
