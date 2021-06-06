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
public class PstTicketDeposit extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc{
	
	public static final String TBL_AISO_TICKET_DEPOSIT = "aiso_ticket_deposit";
	
	public static final int FLD_TICKET_DEPOSIT_ID = 0;
	public static final int FLD_CARRIER_ID = 1;
	public static final int FLD_DEPOSIT_DATE = 2;
	public static final int FLD_DEPOSIT_AMOUNT = 3;
	public static final int FLD_DESCRIPTION = 4;
	
	public static String[] fieldNames = {
	    "TICKET_DEPOSIT_ID",
	    "CARRIER_ID",
	    "DEPOSIT_DATE",
	    "DEPOSIT_AMOUNT",
	    "DESCRIPTION"	    
	};
	
	public static int[] fieldTypes = {
	    TYPE_PK + TYPE_ID + TYPE_LONG,
	    TYPE_LONG,
	    TYPE_DATE,
	    TYPE_FLOAT,
	    TYPE_STRING
	};
	
    public PstTicketDeposit(){
    
    }
    
    public PstTicketDeposit(int i) throws DBException{
	super(new PstTicketDeposit());
    }
    
    public PstTicketDeposit(String sOid) throws DBException{
	super(new PstTicketDeposit(0));
	if(!locate(sOid))
	    throw new DBException(this, DBException.RECORD_NOT_FOUND);
	else
	    return;
    }
    
    public PstTicketDeposit(long lOid) throws DBException{
	super(new PstTicketDeposit(0));
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
        TicketDeposit objTicketDeposit = PstTicketDeposit.fetchExc(ent.getOID()); 
        ent = (Entity) objTicketDeposit;
        return objTicketDeposit.getOID();
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
        return new PstTicketDeposit().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_TICKET_DEPOSIT;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((TicketDeposit) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((TicketDeposit) ent);
    }
    
     public static TicketDeposit fetchExc(long Oid) throws DBException {
        try {
            TicketDeposit objTicketDeposit = new TicketDeposit();
            PstTicketDeposit pstTicketDeposit = new PstTicketDeposit(Oid);
            objTicketDeposit.setOID(Oid);
            
            objTicketDeposit.setCarrierId(pstTicketDeposit.getlong(FLD_CARRIER_ID));
            objTicketDeposit.setDepositDate(pstTicketDeposit.getDate(FLD_DEPOSIT_DATE));
            objTicketDeposit.setDepositAmount(pstTicketDeposit.getdouble(FLD_DEPOSIT_AMOUNT));
            objTicketDeposit.setDescription(pstTicketDeposit.getString(FLD_DESCRIPTION));

            return objTicketDeposit;  
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketDeposit(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(TicketDeposit objTicketDeposit) throws DBException {
        try {
            PstTicketDeposit pstTicketDeposit = new PstTicketDeposit(0);
            
            pstTicketDeposit.setLong(FLD_CARRIER_ID, objTicketDeposit.getCarrierId());
            pstTicketDeposit.setDate(FLD_DEPOSIT_DATE, objTicketDeposit.getDepositDate());
            pstTicketDeposit.setDouble(FLD_DEPOSIT_AMOUNT, objTicketDeposit.getDepositAmount());
            pstTicketDeposit.setString(FLD_DESCRIPTION, objTicketDeposit.getDescription());

            pstTicketDeposit.insert();
            objTicketDeposit.setOID(pstTicketDeposit.getlong(FLD_TICKET_DEPOSIT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketList(0), DBException.UNKNOWN);
        }
        return objTicketDeposit.getOID();
    }

    public static long updateExc(TicketDeposit objTicketDeposit) throws DBException {
        try {
            if (objTicketDeposit != null && objTicketDeposit.getOID() != 0) {
                PstTicketDeposit pstTicketDeposit = new PstTicketDeposit(objTicketDeposit.getOID());
               
                pstTicketDeposit.setLong(FLD_CARRIER_ID, objTicketDeposit.getCarrierId());
		pstTicketDeposit.setDate(FLD_DEPOSIT_DATE, objTicketDeposit.getDepositDate());
		pstTicketDeposit.setDouble(FLD_DEPOSIT_AMOUNT, objTicketDeposit.getDepositAmount());
		pstTicketDeposit.setString(FLD_DESCRIPTION, objTicketDeposit.getDescription());

                pstTicketDeposit.update();
                return objTicketDeposit.getOID();
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
            PstTicketDeposit pstTicketDeposit = new PstTicketDeposit(Oid);
            pstTicketDeposit.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTicketDeposit(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_TICKET_DEPOSIT + " ";
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
                TicketDeposit objTicketDeposit = new TicketDeposit();
                resultToObject(rs, objTicketDeposit);
                lists.add(objTicketDeposit);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstTicketDeposit().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, TicketDeposit objTicketDeposit) {
        try {
            objTicketDeposit.setOID(rs.getLong(fieldNames[PstTicketDeposit.FLD_TICKET_DEPOSIT_ID]));
            objTicketDeposit.setCarrierId(rs.getLong(fieldNames[PstTicketDeposit.FLD_CARRIER_ID]));
            objTicketDeposit.setDepositDate(rs.getDate(fieldNames[PstTicketDeposit.FLD_DEPOSIT_DATE]));
            objTicketDeposit.setDepositAmount(rs.getDouble(fieldNames[PstTicketDeposit.FLD_DEPOSIT_AMOUNT]));
            objTicketDeposit.setDescription(rs.getString(fieldNames[PstTicketDeposit.FLD_DESCRIPTION]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[PstTicketDeposit.FLD_TICKET_DEPOSIT_ID] + ") " +
                    " FROM " + TBL_AISO_TICKET_DEPOSIT;
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
