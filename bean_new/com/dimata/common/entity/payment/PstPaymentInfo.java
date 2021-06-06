/**
 * User: gwawan
 * Date: Mei 9, 2007
 * Time: 4:46:00 PM
 * Version: 1.0
 */
package com.dimata.common.entity.payment;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PstPaymentInfo extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_FNT_PAYMENT_INFO = "payment_info";
    
    public static final int FLD_PAYMENT_INFO_ID     = 0;
    public static final int FLD_BANK_NAME           = 1;
    public static final int FLD_BANK_ADDRESS        = 2;
    public static final int FLD_SWIFT_CODE          = 3;
    public static final int FLD_ACCOUNT_NAME        = 4;
    public static final int FLD_ACCOUNT_NUMBER      = 5;
    public static final int FLD_NAME_ON_CARD        = 6;
    public static final int FLD_CARD_NUMBER         = 7;
    public static final int FLD_CARD_ID             = 8;
    public static final int FLD_EXPIRED_DATE        = 9;
    public static final int FLD_CHECK_BG_NUMBER     = 10;
    public static final int FLD_BANK_COST           = 11;
    public static final int FLD_PURC_PAYMENT_ID     = 12;
    public static final int FLD_PAYMENT_ADDRESS     = 13;
    public static final int FLD_DUE_DATE            = 14;
    
    public static final String[] fieldNames = {
        "PAYMENT_INFO_ID",  //0
        "BANK_NAME",        //1
        "BANK_ADDRESS",     //2
        "SWIFT_CODE",       //3
        "ACCOUNT_NAME",     //4
        "ACCOUNT_NUMBER",   //5
        "NAME_ON_CARD",     //6
        "CARD_NUMBER",      //7
        "CARD_ID",          //8
        "EXPIRED_DATE",     //9
        "CHECK_BG_NUMBER",  //10
        "BANK_COST",        //11
        "PURCH_PAYMENT_ID", //12
        "PAY_ADDRESS",      //13
        "DUE_DATE"          //14
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,  //0
        TYPE_STRING, //1
        TYPE_STRING, //2
        TYPE_STRING, //3
        TYPE_STRING, //4
        TYPE_STRING, //5
        TYPE_STRING, //6
        TYPE_STRING, //7
        TYPE_STRING, //8
        TYPE_DATE,   //9
        TYPE_STRING, //10
        TYPE_FLOAT,  //11
        TYPE_LONG,   //12
        TYPE_STRING, //13
        TYPE_DATE    //14
    };
    
    public PstPaymentInfo() {
    }
    
    public PstPaymentInfo(int i) throws DBException {
        super(new PstPaymentInfo());
    }
    
    public PstPaymentInfo(String sOid) throws DBException {
        super(new PstPaymentInfo(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstPaymentInfo(long lOid) throws DBException {
        super(new PstPaymentInfo(0));
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
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_FNT_PAYMENT_INFO;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstPaymentInfo().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception {
        PaymentInfo paymentInfo = (PaymentInfo) ent;
        long oid = ent.getOID();
        PstPaymentInfo pstPaymentInfo = new PstPaymentInfo(oid);
        paymentInfo.setOID(oid);
        
        paymentInfo.setStBankName(pstPaymentInfo.getString(FLD_BANK_NAME));
        paymentInfo.setStBankAddress(pstPaymentInfo.getString(FLD_BANK_ADDRESS));
        paymentInfo.setStSwiftCade(pstPaymentInfo.getString(FLD_SWIFT_CODE));
        paymentInfo.setStAccountName(pstPaymentInfo.getString(FLD_ACCOUNT_NAME));
        paymentInfo.setStAccountNumber(pstPaymentInfo.getString(FLD_ACCOUNT_NUMBER));
        paymentInfo.setStNameOnCard(pstPaymentInfo.getString(FLD_NAME_ON_CARD));
        paymentInfo.setStCardNumber(pstPaymentInfo.getString(FLD_CARD_NUMBER));
        paymentInfo.setStCardId(pstPaymentInfo.getString(FLD_CARD_ID));
        paymentInfo.setDtExpiredDate(pstPaymentInfo.getDate(FLD_EXPIRED_DATE));
        paymentInfo.setStCheckBGNumber(pstPaymentInfo.getString(FLD_CHECK_BG_NUMBER));
        paymentInfo.setdBankCost(pstPaymentInfo.getdouble(FLD_BANK_COST));
        paymentInfo.setlPurchPaymentId(pstPaymentInfo.getlong(FLD_PURC_PAYMENT_ID));
        paymentInfo.setStPaymentAddress(pstPaymentInfo.getString(FLD_PAYMENT_ADDRESS));
        paymentInfo.setDueDate(pstPaymentInfo.getDate(FLD_DUE_DATE));
        
        return ent.getOID();
    }
    
    public static PaymentInfo fetchExc(long oid) throws DBException {
        try {
            PaymentInfo paymentInfo = new PaymentInfo();
            PstPaymentInfo pstPaymentInfo = new PstPaymentInfo(oid);
            paymentInfo.setOID(oid);
            
            paymentInfo.setStBankName(pstPaymentInfo.getString(FLD_BANK_NAME));
            paymentInfo.setStBankAddress(pstPaymentInfo.getString(FLD_BANK_ADDRESS));
            paymentInfo.setStSwiftCade(pstPaymentInfo.getString(FLD_SWIFT_CODE));
            paymentInfo.setStAccountName(pstPaymentInfo.getString(FLD_ACCOUNT_NAME));
            paymentInfo.setStAccountNumber(pstPaymentInfo.getString(FLD_ACCOUNT_NUMBER));
            paymentInfo.setStNameOnCard(pstPaymentInfo.getString(FLD_NAME_ON_CARD));
            paymentInfo.setStCardNumber(pstPaymentInfo.getString(FLD_CARD_NUMBER));
            paymentInfo.setStCardId(pstPaymentInfo.getString(FLD_CARD_ID));
            paymentInfo.setDtExpiredDate(pstPaymentInfo.getDate(FLD_EXPIRED_DATE));
            paymentInfo.setStCheckBGNumber(pstPaymentInfo.getString(FLD_CHECK_BG_NUMBER));
            paymentInfo.setdBankCost(pstPaymentInfo.getdouble(FLD_BANK_COST));
            paymentInfo.setlPurchPaymentId(pstPaymentInfo.getlong(FLD_PURC_PAYMENT_ID));
            paymentInfo.setStPaymentAddress(pstPaymentInfo.getString(FLD_PAYMENT_ADDRESS));
            paymentInfo.setDueDate(pstPaymentInfo.getDate(FLD_DUE_DATE));
            
            return paymentInfo;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentInfo(0), DBException.UNKNOWN);
        }
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((PaymentInfo) ent);
    }
    
    public static long updateExc(PaymentInfo paymentInfo) throws DBException {
        try {
            if (paymentInfo.getOID() != 0) {
                PstPaymentInfo pstPaymentInfo = new PstPaymentInfo(paymentInfo.getOID());
                
                pstPaymentInfo.setString(FLD_BANK_NAME, paymentInfo.getStBankName());
                pstPaymentInfo.setString(FLD_BANK_ADDRESS, paymentInfo.getStBankAddress());
                pstPaymentInfo.setString(FLD_SWIFT_CODE, paymentInfo.getStSwiftCade());
                pstPaymentInfo.setString(FLD_ACCOUNT_NAME, paymentInfo.getStAccountName());
                pstPaymentInfo.setString(FLD_ACCOUNT_NUMBER, paymentInfo.getStAccountNumber());
                pstPaymentInfo.setString(FLD_BANK_NAME, paymentInfo.getStBankName());
                pstPaymentInfo.setString(FLD_NAME_ON_CARD, paymentInfo.getStNameOnCard());
                pstPaymentInfo.setString(FLD_CARD_NUMBER, paymentInfo.getStCardNumber());
                pstPaymentInfo.setString(FLD_CARD_ID, paymentInfo.getStCardId());
                pstPaymentInfo.setDate(FLD_EXPIRED_DATE, paymentInfo.getDtExpiredDate());
                pstPaymentInfo.setString(FLD_CHECK_BG_NUMBER, paymentInfo.getStCheckBGNumber());
                pstPaymentInfo.setDouble(FLD_BANK_COST, paymentInfo.getdBankCost());
                pstPaymentInfo.setLong(FLD_PURC_PAYMENT_ID, paymentInfo.getlPurchPaymentId());
                pstPaymentInfo.setString(FLD_PAYMENT_ADDRESS, paymentInfo.getStPaymentAddress());
                paymentInfo.setDueDate(pstPaymentInfo.getDate(FLD_DUE_DATE));
                
                pstPaymentInfo.update();
                return paymentInfo.getOID();
                
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentInfo(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public long deleteExc(Entity ent) throws Exception {
        return deleteExc(ent.getOID());
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstPaymentInfo pstPaymentInfo = new PstPaymentInfo(oid);
            pstPaymentInfo.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentInfo(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((PaymentInfo) ent);
    }
    
    public static long insertExc(PaymentInfo paymentInfo) throws DBException {
        try {
            PstPaymentInfo pstPaymentInfo = new PstPaymentInfo(0);
            
            pstPaymentInfo.setString(FLD_BANK_NAME, paymentInfo.getStBankName());
            pstPaymentInfo.setString(FLD_BANK_ADDRESS, paymentInfo.getStBankAddress());
            pstPaymentInfo.setString(FLD_SWIFT_CODE, paymentInfo.getStSwiftCade());
            pstPaymentInfo.setString(FLD_ACCOUNT_NAME, paymentInfo.getStAccountName());
            pstPaymentInfo.setString(FLD_ACCOUNT_NUMBER, paymentInfo.getStAccountNumber());
            pstPaymentInfo.setString(FLD_BANK_NAME, paymentInfo.getStBankName());
            pstPaymentInfo.setString(FLD_NAME_ON_CARD, paymentInfo.getStNameOnCard());
            pstPaymentInfo.setString(FLD_CARD_NUMBER, paymentInfo.getStCardNumber());
            pstPaymentInfo.setString(FLD_CARD_ID, paymentInfo.getStCardId());
            pstPaymentInfo.setDate(FLD_EXPIRED_DATE, paymentInfo.getDtExpiredDate());
            pstPaymentInfo.setString(FLD_CHECK_BG_NUMBER, paymentInfo.getStCheckBGNumber());
            pstPaymentInfo.setDouble(FLD_BANK_COST, paymentInfo.getdBankCost());
            pstPaymentInfo.setLong(FLD_PURC_PAYMENT_ID, paymentInfo.getlPurchPaymentId());
            pstPaymentInfo.setString(FLD_PAYMENT_ADDRESS, paymentInfo.getStPaymentAddress());
            pstPaymentInfo.setDate(FLD_DUE_DATE, paymentInfo.getDueDate());
            
            pstPaymentInfo.insert();
            paymentInfo.setOID(pstPaymentInfo.getlong(FLD_PAYMENT_INFO_ID));
        } catch (DBException dbe) {
            dbe.printStackTrace();
            throw dbe;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(new PstPaymentInfo(0), DBException.UNKNOWN);
        }
        return paymentInfo.getOID();
    }
    
    public static void resultToObject(ResultSet rs, PaymentInfo paymentInfo) {
        try {
            paymentInfo.setOID(rs.getLong(fieldNames[FLD_PAYMENT_INFO_ID]));
            paymentInfo.setStBankName(rs.getString(fieldNames[FLD_BANK_NAME]));
            paymentInfo.setStBankAddress(rs.getString(fieldNames[FLD_BANK_ADDRESS]));
            paymentInfo.setStSwiftCade(rs.getString(fieldNames[FLD_SWIFT_CODE]));
            paymentInfo.setStAccountName(rs.getString(fieldNames[FLD_ACCOUNT_NAME]));
            paymentInfo.setStAccountNumber(rs.getString(fieldNames[FLD_ACCOUNT_NUMBER]));
            paymentInfo.setStNameOnCard(rs.getString(fieldNames[FLD_NAME_ON_CARD]));
            paymentInfo.setStCardNumber(rs.getString(fieldNames[FLD_CARD_NUMBER]));
            paymentInfo.setStCardId(rs.getString(fieldNames[FLD_CARD_ID]));
            paymentInfo.setDtExpiredDate(rs.getDate(fieldNames[FLD_EXPIRED_DATE]));
            paymentInfo.setStCheckBGNumber(rs.getString(fieldNames[FLD_CHECK_BG_NUMBER]));
            paymentInfo.setdBankCost(rs.getDouble(fieldNames[FLD_BANK_COST]));
            paymentInfo.setlPurchPaymentId(rs.getLong(fieldNames[FLD_PURC_PAYMENT_ID]));
            paymentInfo.setStPaymentAddress(rs.getString(fieldNames[FLD_PAYMENT_ADDRESS]));
            paymentInfo.setDueDate(rs.getDate(fieldNames[FLD_DUE_DATE]));
            
        } catch (Exception e) {
        }
    }
    
    public static int deletePaymentInfo(long lPaymentId){
        int iResult = 0;
        DBResultSet dbrs = null;
        String stSql = "DELETE FROM "+ TBL_FNT_PAYMENT_INFO +
        " WHERE "+fieldNames[FLD_PURC_PAYMENT_ID] +" = "+lPaymentId;
        try {
            iResult = DBHandler.execUpdate(stSql);
        } catch (DBException e) {
            e.printStackTrace();
        }finally{
            DBResultSet.close(dbrs);
        }
        return iResult;
    }
    
    public static PaymentInfo getPaymentInfo(long lPaymentId){
        Vector vResult = getListPaymentInfo(lPaymentId);
        PaymentInfo paymentInfo = new PaymentInfo();
        if(vResult != null && vResult.size() > 0){
            for(int i=0; i < vResult.size(); i++){
                paymentInfo = (PaymentInfo) vResult.get(i);
                break;
            }
        }
        return paymentInfo;
    }
    
    public static Vector getListPaymentInfo(long lPaymentId){
        Vector vResult = new Vector();
        DBResultSet dbrs = null;
        String stSql = "SELECT * FROM  "+TBL_FNT_PAYMENT_INFO+
        " WHERE "+fieldNames[FLD_PURC_PAYMENT_ID] +" = "+ lPaymentId;
        
        try {
            dbrs = DBHandler.execQueryResult(stSql);
            ResultSet rs = dbrs.getResultSet();
            PaymentInfo paymentInfo = null;
            if(rs != null){
                while(rs.next()){
                    paymentInfo = new PaymentInfo();
                    resultToObject(rs, paymentInfo);
                    
                    vResult.add(paymentInfo);
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DBResultSet.close(dbrs);
        }
        return vResult;
    }
}
