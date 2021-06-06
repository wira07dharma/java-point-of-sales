/*
 * PstInvoiceMain.java
 *
 * Created on November 12, 2007, 2:24 PM
 */

package com.dimata.aiso.entity.invoice;

/**
 *
 * @author  dwi
 */
import java.sql.ResultSet;
import java.util.*;

import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.interfaces.journal.I_JournalType;

public class PstInvoiceMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType {
    
    public static final String TBL_AISO_INV_MAIN = "aiso_inv_main";
    public static final int FLD_INV_MAIN_ID = 0;
    public static final int FLD_ISSUED_DATE = 1;
    public static final int FLD_CHECKIN_DATE = 2;
    public static final int FLD_CHECKOUT_DATE = 3;
    public static final int FLD_STATUS = 4;
    public static final int FLD_TOTAL_PAX = 5;
    public static final int FLD_TOTAL_ROOM = 6;
    public static final int FLD_FIRST_CONTACT_ID = 7;
    public static final int FLD_SECOND_CONTACT_ID = 8;
    public static final int FLD_GUEST_NAME = 9;
    public static final int FLD_HOTEL_NAME = 10;
    public static final int FLD_ID_PERKIRAAN = 11;
    public static final int FLD_TERM_OF_PAYMENT = 12;
    public static final int FLD_TYPE = 13;
    public static final int FLD_TOTAL_DISCOUNT = 14;
    public static final int FLD_NUMBER_COUNTER = 15;
    public static final int FLD_INVOICE_NUMBER = 16;
    public static final int FLD_TAX = 17;
    public static final int FLD_PERKDEPOSIT = 18;
    public static final int FLD_DESCRIPTION = 19;
    public static final int FLD_DEPOSITDATE = 20;
    public static final int FLD_DEPOSIT_TIME_LIMIT = 21;
    
    public static String[] fieldNames = {
        "INVOICE_ID",
        "ISSUED_DATE",
        "CHECKIN_DATE",
        "CHECKOUT_DATE",
        "STATUS",
        "TOTAL_PAX",
        "TOTAL_ROOM",
        "FIRST_CONTACT_ID",
        "SECOND_CONTACT_ID",
        "GUEST_NAME",
        "HOTEL_NAME",
        "ID_PERKIRAAN",
        "TERM_OF_PAYMENT",
        "TYPE",
        "TOTAL_DISCOUNT",
        "NUMBER_COUNTER",
        "INVOICE_NUMBER",
        "TAX",
        "ID_PERKDEPOSIT",
        "DESCRIPTION",
        "DEPOSIT_DATE",
	"DEPOSIT_TIME_LIMIT"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE
    };
   
    
    /** Creates a new instance of PstInvoiceMain */
    public PstInvoiceMain() {
    }
    
    public PstInvoiceMain(int i) throws DBException{
        super(new PstInvoiceMain());
    }
    
    public PstInvoiceMain(String sOid) throws DBException{
        super(new PstInvoiceMain(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstInvoiceMain(long lOid) throws DBException{
        super(new PstInvoiceMain(0));
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
        return deleteExc((InvoiceMain)ent);  
    }
    
    public long fetchExc(Entity ent) throws Exception {
        InvoiceMain objInvoiceMain = PstInvoiceMain.fetchExc(ent.getOID());
        ent = (Entity)objInvoiceMain;
        return objInvoiceMain.getOID();
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
        return new PstInvoiceMain().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_INV_MAIN;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return PstInvoiceMain.insertExc((InvoiceMain)ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return PstInvoiceMain.updateExc((InvoiceMain)ent);
    }
    
    public static InvoiceMain fetchExc(long lOid) throws DBException{
        try{
            InvoiceMain objInvoiceMain = new InvoiceMain();
            PstInvoiceMain pstInvoiceMain = new PstInvoiceMain(lOid);
            objInvoiceMain.setOID(lOid);
            
            objInvoiceMain.setCheckInDate(pstInvoiceMain.getDate(FLD_CHECKIN_DATE));
            objInvoiceMain.setCheckOutDate(pstInvoiceMain.getDate(FLD_CHECKOUT_DATE));
            objInvoiceMain.setIssuedDate(pstInvoiceMain.getDate(FLD_ISSUED_DATE));
            objInvoiceMain.setFirstContactId(pstInvoiceMain.getlong(FLD_FIRST_CONTACT_ID));
            objInvoiceMain.setGuestName(pstInvoiceMain.getString(FLD_GUEST_NAME));
            objInvoiceMain.setHotelName(pstInvoiceMain.getString(FLD_HOTEL_NAME));
            objInvoiceMain.setIdPerkiraan(pstInvoiceMain.getlong(FLD_ID_PERKIRAAN));
            objInvoiceMain.setInvoiceNumber(pstInvoiceMain.getString(FLD_INVOICE_NUMBER));
            objInvoiceMain.setNumberCounter(pstInvoiceMain.getInt(FLD_NUMBER_COUNTER));
            objInvoiceMain.setSecondContactId(pstInvoiceMain.getlong(FLD_SECOND_CONTACT_ID));
            objInvoiceMain.setStatus(pstInvoiceMain.getInt(FLD_STATUS));
            objInvoiceMain.setTax(pstInvoiceMain.getdouble(FLD_TAX));
            objInvoiceMain.setTermOfPayment(pstInvoiceMain.getInt(FLD_TERM_OF_PAYMENT));
            objInvoiceMain.setTotalDiscount(pstInvoiceMain.getdouble(FLD_TOTAL_DISCOUNT));
            objInvoiceMain.setTotalPax(pstInvoiceMain.getInt(FLD_TOTAL_PAX));
            objInvoiceMain.setTotalRoom(pstInvoiceMain.getInt(FLD_TOTAL_ROOM));
            objInvoiceMain.setType(pstInvoiceMain.getInt(FLD_TYPE));
            objInvoiceMain.setIdPerkDeposit(pstInvoiceMain.getlong(FLD_PERKDEPOSIT));
            objInvoiceMain.setDescription(pstInvoiceMain.getString(FLD_DESCRIPTION));
            objInvoiceMain.setDepositDate(pstInvoiceMain.getDate(FLD_DEPOSITDATE));
            objInvoiceMain.setDepositTimeLimit(pstInvoiceMain.getDate(FLD_DEPOSIT_TIME_LIMIT));
            
            return objInvoiceMain;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceMain(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(InvoiceMain objInvoiceMain) throws DBException{
        try{
            PstInvoiceMain pstInvoiceMain = new PstInvoiceMain(0);
            
            pstInvoiceMain.setDate(FLD_CHECKIN_DATE, objInvoiceMain.getCheckInDate());
            pstInvoiceMain.setDate(FLD_CHECKOUT_DATE, objInvoiceMain.getCheckOutDate());
            pstInvoiceMain.setDate(FLD_ISSUED_DATE, objInvoiceMain.getIssuedDate());
            pstInvoiceMain.setLong(FLD_FIRST_CONTACT_ID, objInvoiceMain.getFirstContactId());
            pstInvoiceMain.setString(FLD_GUEST_NAME, objInvoiceMain.getGuestName());
            pstInvoiceMain.setString(FLD_HOTEL_NAME, objInvoiceMain.getHotelName());
            pstInvoiceMain.setLong(FLD_ID_PERKIRAAN, objInvoiceMain.getIdPerkiraan());
            pstInvoiceMain.setString(FLD_INVOICE_NUMBER, objInvoiceMain.getInvoiceNumber());
            pstInvoiceMain.setInt(FLD_NUMBER_COUNTER, objInvoiceMain.getNumberCounter());
            pstInvoiceMain.setLong(FLD_SECOND_CONTACT_ID, objInvoiceMain.getSecondContactId());
            pstInvoiceMain.setInt(FLD_STATUS, objInvoiceMain.getStatus());
            pstInvoiceMain.setDouble(FLD_TAX, objInvoiceMain.getTax());
            pstInvoiceMain.setInt(FLD_TERM_OF_PAYMENT, objInvoiceMain.getTermOfPayment()); 
            pstInvoiceMain.setDouble(FLD_TOTAL_DISCOUNT, objInvoiceMain.getTotalDiscount());
            pstInvoiceMain.setInt(FLD_TOTAL_PAX, objInvoiceMain.getTotalPax());
            pstInvoiceMain.setInt(FLD_TOTAL_ROOM, objInvoiceMain.getTotalRoom());
            pstInvoiceMain.setInt(FLD_TYPE, objInvoiceMain.getType());
            pstInvoiceMain.setLong(FLD_PERKDEPOSIT, objInvoiceMain.getIdPerkDeposit());
	    pstInvoiceMain.setString(FLD_DESCRIPTION, objInvoiceMain.getDescription());
	    pstInvoiceMain.setDate(FLD_DEPOSITDATE, objInvoiceMain.getDepositDate());
	    pstInvoiceMain.setDate(FLD_DEPOSIT_TIME_LIMIT, objInvoiceMain.getDepositTimeLimit());
           
            pstInvoiceMain.insert();
            objInvoiceMain.setOID(pstInvoiceMain.getlong(FLD_INV_MAIN_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceMain(0), DBException.UNKNOWN);
        }
        return objInvoiceMain.getOID();
    }
    
    public static long updateExc(InvoiceMain objInvoiceMain) throws DBException{
        try{
            if(objInvoiceMain.getOID() != 0){
                PstInvoiceMain pstInvoiceMain = new PstInvoiceMain(objInvoiceMain.getOID());
            
                pstInvoiceMain.setDate(FLD_CHECKIN_DATE, objInvoiceMain.getCheckInDate());
                pstInvoiceMain.setDate(FLD_CHECKOUT_DATE, objInvoiceMain.getCheckOutDate());
                pstInvoiceMain.setDate(FLD_ISSUED_DATE, objInvoiceMain.getIssuedDate());
                pstInvoiceMain.setLong(FLD_FIRST_CONTACT_ID, objInvoiceMain.getFirstContactId());
                pstInvoiceMain.setString(FLD_GUEST_NAME, objInvoiceMain.getGuestName());
                pstInvoiceMain.setString(FLD_HOTEL_NAME, objInvoiceMain.getHotelName());
                pstInvoiceMain.setLong(FLD_ID_PERKIRAAN, objInvoiceMain.getIdPerkiraan());
                pstInvoiceMain.setString(FLD_INVOICE_NUMBER, objInvoiceMain.getInvoiceNumber());
                pstInvoiceMain.setInt(FLD_NUMBER_COUNTER, objInvoiceMain.getNumberCounter());
                pstInvoiceMain.setLong(FLD_SECOND_CONTACT_ID, objInvoiceMain.getSecondContactId());
                pstInvoiceMain.setInt(FLD_STATUS, objInvoiceMain.getStatus());
                pstInvoiceMain.setDouble(FLD_TAX, objInvoiceMain.getTax());
                pstInvoiceMain.setInt(FLD_TERM_OF_PAYMENT, objInvoiceMain.getTermOfPayment());
                pstInvoiceMain.setDouble(FLD_TOTAL_DISCOUNT, objInvoiceMain.getTotalDiscount());
                pstInvoiceMain.setInt(FLD_TOTAL_PAX, objInvoiceMain.getTotalPax());
                pstInvoiceMain.setInt(FLD_TOTAL_ROOM, objInvoiceMain.getTotalRoom());
                pstInvoiceMain.setInt(FLD_TYPE, objInvoiceMain.getType());
                pstInvoiceMain.setLong(FLD_PERKDEPOSIT, objInvoiceMain.getIdPerkDeposit());
		pstInvoiceMain.setString(FLD_DESCRIPTION, objInvoiceMain.getDescription());
		pstInvoiceMain.setDate(FLD_DEPOSITDATE, objInvoiceMain.getDepositDate());
		pstInvoiceMain.setDate(FLD_DEPOSIT_TIME_LIMIT, objInvoiceMain.getDepositTimeLimit());
                
                pstInvoiceMain.update();
                return objInvoiceMain.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceMain(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstInvoiceMain pstInvoiceMain = new PstInvoiceMain(lOid);
            pstInvoiceMain.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceMain(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhereClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT * FROM "+TBL_AISO_INV_MAIN;
            if(strWhereClause != null && strWhereClause.length() > 0)
                sql += " WHERE "+strWhereClause;
            if(strOrderBy != null && strOrderBy.length() > 0)
                sql += " ORDER BY "+strOrderBy;
           switch(DBSVR_TYPE){
               case DBSVR_MYSQL:
                if(iStart == 0 && iRecordToGet == 0)
                    sql += "";
                else
                    sql += " LIMIT "+iStart+", "+iRecordToGet;
               break;
               case DBSVR_POSTGRESQL:
                 if(iStart == 0 && iRecordToGet == 0)
                     sql += "";
                 else
                     sql += " LIMIT "+iRecordToGet+" OFFSET "+iStart;
               break;
               case DBSVR_SYBASE:
               break;
               case DBSVR_ORACLE:
               break;
               case DBSVR_MSSQL:
               break; 
               default:
               break;
            }
           
           dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();
           while(rs.next()){
                InvoiceMain objInvoiceMain = new InvoiceMain();
                objInvoiceMain = resultToObject(rs, objInvoiceMain);
                vResult.add(objInvoiceMain);
           }
           rs.close();
           return vResult;
        }catch(Exception e){
            System.out.println(new PstInvoiceMain().getClass().getName()+".list exception : "+e.toString());
        }
        return new Vector();
    }
    
    public static InvoiceMain resultToObject(ResultSet rs, InvoiceMain objInvoiceMain){
        try{
            objInvoiceMain.setOID(rs.getLong(fieldNames[FLD_INV_MAIN_ID]));
            objInvoiceMain.setCheckInDate(rs.getDate(fieldNames[FLD_CHECKIN_DATE]));
            objInvoiceMain.setCheckOutDate(rs.getDate(fieldNames[FLD_CHECKOUT_DATE]));
            objInvoiceMain.setIssuedDate(rs.getDate(fieldNames[FLD_ISSUED_DATE]));
            objInvoiceMain.setGuestName(rs.getString(fieldNames[FLD_GUEST_NAME]));
            objInvoiceMain.setHotelName(rs.getString(fieldNames[FLD_HOTEL_NAME]));
            objInvoiceMain.setIdPerkiraan(rs.getLong(fieldNames[FLD_ID_PERKIRAAN]));
            objInvoiceMain.setInvoiceNumber(rs.getString(fieldNames[FLD_INVOICE_NUMBER]));
            objInvoiceMain.setNumberCounter(rs.getInt(fieldNames[FLD_NUMBER_COUNTER]));
            objInvoiceMain.setSecondContactId(rs.getLong(fieldNames[FLD_SECOND_CONTACT_ID]));
            objInvoiceMain.setStatus(rs.getInt(fieldNames[FLD_STATUS]));
            objInvoiceMain.setTax(rs.getDouble(fieldNames[FLD_TAX]));
            objInvoiceMain.setTermOfPayment(rs.getInt(fieldNames[FLD_TERM_OF_PAYMENT]));
            objInvoiceMain.setTotalDiscount(rs.getDouble(fieldNames[FLD_TOTAL_DISCOUNT]));
            objInvoiceMain.setTotalPax(rs.getInt(fieldNames[FLD_TOTAL_PAX]));
            objInvoiceMain.setTotalRoom(rs.getInt(fieldNames[FLD_TOTAL_ROOM]));
            objInvoiceMain.setType(rs.getInt(fieldNames[FLD_TYPE]));
            objInvoiceMain.setFirstContactId(rs.getLong(fieldNames[FLD_FIRST_CONTACT_ID]));
            objInvoiceMain.setIdPerkDeposit(rs.getLong(fieldNames[FLD_PERKDEPOSIT]));
	    objInvoiceMain.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
	    objInvoiceMain.setDepositDate(rs.getDate(fieldNames[FLD_DEPOSITDATE]));
	    objInvoiceMain.setDepositTimeLimit(rs.getDate(fieldNames[FLD_DEPOSIT_TIME_LIMIT]));
        }catch(Exception e){
            System.out.println("Exception on resultToObject() ::: "+e.toString());
        }
        return objInvoiceMain;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT COUNT("+fieldNames[FLD_INV_MAIN_ID]+") FROM "+TBL_AISO_INV_MAIN;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    iResult = rs.getInt(1);
                }
                rs.close();
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on "+new PstInvoiceMain().getClass().getName()+".getCount() ::: "+e.toString());
                e.printStackTrace();
            }
        return iResult;
    }
    
    public static void main(String[] arg){
        PstInvoiceMain pstInvoiceMain = new PstInvoiceMain();
        InvoiceMain objInvoiceMain = new InvoiceMain();
        
        /*try{
            objInvoiceMain = pstInvoiceMain.fetchExc(504404353118274052L);
        }catch(Exception e){}*/
        
        Date cOutDate = new Date();
        Date cInDate = new Date();
        cInDate.setMonth(0);
        /*objInvoiceMain.setCheckInDate(cInDate);
        objInvoiceMain.setCheckOutDate(cOutDate);
        objInvoiceMain.setFirstContactId(504404335831980598L);
        objInvoiceMain.setGuestName("UNGU BAND");
        objInvoiceMain.setHotelName("INNA BALI");*/
        objInvoiceMain.setIdPerkiraan(504404326767341020L);
        /*objInvoiceMain.setInvoiceNumber("0708-051");
        objInvoiceMain.setIssuedDate(cInDate);
        objInvoiceMain.setNumberCounter(1);
        objInvoiceMain.setSecondContactId(504404346555320239L);
        objInvoiceMain.setStatus(0);
        objInvoiceMain.setTax(15.5);
        objInvoiceMain.setTotalRoom(30);
        objInvoiceMain.setTotalPax(10);
        objInvoiceMain.setTermOfPayment(7);
        objInvoiceMain.setType(1);
        objInvoiceMain.setTotalDiscount(35000.35);*/
        
        try{
            objInvoiceMain = pstInvoiceMain.fetchExc(504404353726563864L);            
            System.out.println("SUKSES :::: "+objInvoiceMain.getInvoiceNumber());
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("GAGAL");
        }
        
    }
}
