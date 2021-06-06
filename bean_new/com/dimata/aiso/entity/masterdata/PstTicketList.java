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
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class PstTicketList extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc{
    
    public static final String TBL_AISO_TICKET_LIST = "aiso_ticket_list";

    public static final int FLD_TICKET_LIST_ID = 0;
    public static final int FLD_CARRIER_ID = 1;
    public static final int FLD_TICKET_NUMBER = 2;
    public static final int FLD_TICKET_DEPOSIT_ID = 3;
    public static final int FLD_INVOICE_DETAIL_ID = 4;
    public static final int FLD_TICKET_PRICE = 5;

    public static String[] fieldNames = {
	"TICKET_LIST_ID",
	"CARRIER_ID",
	"TICKET_NUMBER",
	"TICKET_DEPOSIT_ID",
	"INVOICE_DETAIL_ID",
	"TICKET_PRICE"
    };

    public static int[] fieldTypes = {
	TYPE_PK + TYPE_ID + TYPE_LONG,
	TYPE_LONG,
	TYPE_STRING,
	TYPE_LONG,
	TYPE_LONG,
	TYPE_FLOAT
    };

	
    public PstTicketList(){
    
    }
    
    public PstTicketList(int i) throws DBException{
	super(new PstTicketList());
    }
    
    public PstTicketList(String sOid) throws DBException{
	super(new PstTicketList(0));
	if(!locate(sOid))
	    throw new DBException(this, DBException.RECORD_NOT_FOUND);
	else
	    return;
    }
    
    public PstTicketList(long lOid) throws DBException{
	super(new PstTicketList(0));
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
        TicketList objTicketList = PstTicketList.fetchExc(ent.getOID()); 
        ent = (Entity) objTicketList;
        return objTicketList.getOID();
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
        return new PstTicketList().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_TICKET_LIST;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((TicketList) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((TicketList) ent);
    }
    
     public static TicketList fetchExc(long Oid) throws DBException {
        try {
            TicketList objTicketList = new TicketList();
            PstTicketList pstTicketList = new PstTicketList(Oid);
            objTicketList.setOID(Oid);
            
            objTicketList.setCarrierId(pstTicketList.getlong(FLD_CARRIER_ID));
            objTicketList.setTicketNumber(pstTicketList.getString(FLD_TICKET_NUMBER));
            objTicketList.setTicketDepositId(pstTicketList.getlong(FLD_TICKET_DEPOSIT_ID));
            objTicketList.setInvoiceDetailId(pstTicketList.getlong(FLD_INVOICE_DETAIL_ID));
            objTicketList.setTicketPrice(pstTicketList.getdouble(FLD_TICKET_PRICE));

            return objTicketList;  
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketList(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(TicketList objTicketList) throws DBException {
        try {
            PstTicketList pstTicketList = new PstTicketList(0);
            
            pstTicketList.setLong(FLD_CARRIER_ID, objTicketList.getCarrierId());
            pstTicketList.setString(FLD_TICKET_NUMBER, objTicketList.getTicketNumber());
            pstTicketList.setLong(FLD_TICKET_DEPOSIT_ID, objTicketList.getTicketDepositId());
            pstTicketList.setLong(FLD_INVOICE_DETAIL_ID, objTicketList.getInvoiceDetailId());
            pstTicketList.setDouble(FLD_TICKET_PRICE, objTicketList.getTicketPrice());

            pstTicketList.insert();
            objTicketList.setOID(pstTicketList.getlong(FLD_TICKET_LIST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketList(0), DBException.UNKNOWN);
        }
        return objTicketList.getOID();
    }

    public static long updateExc(TicketList objTicketList) throws DBException {
        try {
            if (objTicketList != null && objTicketList.getOID() != 0) {
                PstTicketList pstTicketList = new PstTicketList(objTicketList.getOID());
               
                pstTicketList.setLong(FLD_CARRIER_ID, objTicketList.getCarrierId());
		pstTicketList.setString(FLD_TICKET_NUMBER, objTicketList.getTicketNumber());
		pstTicketList.setLong(FLD_TICKET_DEPOSIT_ID, objTicketList.getTicketDepositId());
		pstTicketList.setLong(FLD_INVOICE_DETAIL_ID, objTicketList.getInvoiceDetailId());
		pstTicketList.setDouble(FLD_TICKET_PRICE, objTicketList.getTicketPrice());

                pstTicketList.update();
                return objTicketList.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketList(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstTicketList pstTicketList = new PstTicketList(Oid);
            pstTicketList.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketList(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_TICKET_LIST + " ";
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
                TicketList objTicketList = new TicketList();
                resultToObject(rs, objTicketList);
                lists.add(objTicketList);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstTicketList().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, TicketList objTicketList) {
        try {
            objTicketList.setOID(rs.getLong(fieldNames[PstTicketList.FLD_TICKET_LIST_ID]));
            objTicketList.setCarrierId(rs.getLong(fieldNames[PstTicketList.FLD_CARRIER_ID]));
            objTicketList.setTicketNumber(rs.getString(fieldNames[PstTicketList.FLD_TICKET_NUMBER]));
            objTicketList.setTicketDepositId(rs.getLong(fieldNames[PstTicketList.FLD_TICKET_DEPOSIT_ID]));
            objTicketList.setInvoiceDetailId(rs.getLong(fieldNames[PstTicketList.FLD_INVOICE_DETAIL_ID]));
            objTicketList.setTicketPrice(rs.getDouble(fieldNames[PstTicketList.FLD_TICKET_PRICE]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[PstTicketList.FLD_TICKET_LIST_ID] + ") " +
                    " FROM " + TBL_AISO_TICKET_LIST;
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
    
    public static synchronized long getTicketListIdByInvDetailId(long invDetailId){
	DBResultSet dbrs = null;
	long lResult = 0;
	try{
	    String sql = " SELECT "+fieldNames[PstTicketList.FLD_TICKET_LIST_ID]+
			 " FROM "+TBL_AISO_TICKET_LIST+
			 " WHERE "+fieldNames[PstTicketList.FLD_INVOICE_DETAIL_ID]+" = "+invDetailId;
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		lResult = rs.getLong(fieldNames[PstTicketList.FLD_TICKET_LIST_ID]);
	    }
	}catch(Exception e){}
	return lResult;
    }
}
