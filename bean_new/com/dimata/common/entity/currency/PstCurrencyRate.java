
/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.entity.currency;

/* package java */
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package elkomjaya */
//import com.dimata.elkomjaya.db.DBHandler;
//import com.dimata.elkomjaya.db.DBException;
//import com.dimata.elkomjaya.db.DBLogger;
import com.dimata.common.entity.currency.*;

public class PstCurrencyRate extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch {
    
    //public static final  String TBL_CURRENCY_RATE = "CURRENCY_RATE";
    public static final  String TBL_CURRENCY_RATE = "currency_rate";
    
    public static final  int FLD_RATE_ID = 0;
    public static final  int FLD_DATE = 1;
    public static final  int FLD_VALUE = 2;
    
    public static final  String[] fieldNames = {
        "RATE_ID",
        "DATE",
        "VALUE"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_DATE,
        TYPE_FLOAT
    };
    
    
    public static final int RATE_CODE_RP 	= 0;
    public static final int RATE_CODE_USD 	= 1;
    public static String[] rateCodes = {
        "IDR",
        "USD"
    };
    
    public static Vector getCurrencyType(){
        Vector result = new Vector(1,1);
        for(int i=0; i<rateCodes.length; i++){
            result.add(rateCodes[i]);
        }
        return result;
    }
    
    public PstCurrencyRate(){
    }
    
    public PstCurrencyRate(int i) throws DBException {
        super(new PstCurrencyRate());
    }
    
    public PstCurrencyRate(String sOid) throws DBException {
        super(new PstCurrencyRate(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstCurrencyRate(long lOid) throws DBException {
        super(new PstCurrencyRate(0));
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
        return TBL_CURRENCY_RATE;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstCurrencyRate().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        CurrencyRate currencyrate = fetchExc(ent.getOID());
        ent = (Entity)currencyrate;
        return currencyrate.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((CurrencyRate) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((CurrencyRate) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static CurrencyRate fetchExc(long oid) throws DBException{
        try{
            CurrencyRate currencyrate = new CurrencyRate();
            PstCurrencyRate pstCurrencyRate = new PstCurrencyRate(oid);
            currencyrate.setOID(oid);
            
            currencyrate.setDate(pstCurrencyRate.getDate(FLD_DATE));
            currencyrate.setValue(pstCurrencyRate.getdouble(FLD_VALUE));
            
            return currencyrate;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCurrencyRate(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(CurrencyRate currencyrate) throws DBException{
        try{
            PstCurrencyRate pstCurrencyRate = new PstCurrencyRate(0);
            
            pstCurrencyRate.setDate(FLD_DATE, currencyrate.getDate());
            pstCurrencyRate.setDouble(FLD_VALUE, currencyrate.getValue());
            
            pstCurrencyRate.insert();
            currencyrate.setOID(pstCurrencyRate.getlong(FLD_RATE_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCurrencyRate(0),DBException.UNKNOWN);
        }
        return currencyrate.getOID();
    }
    
    public static long updateExc(CurrencyRate currencyrate) throws DBException{
        try{
            if(currencyrate.getOID() != 0){
                PstCurrencyRate pstCurrencyRate = new PstCurrencyRate(currencyrate.getOID());
                
                pstCurrencyRate.setDate(FLD_DATE, currencyrate.getDate());
                pstCurrencyRate.setDouble(FLD_VALUE, currencyrate.getValue());
                
                pstCurrencyRate.update();
                return currencyrate.getOID();
                
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCurrencyRate(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstCurrencyRate pstCurrencyRate = new PstCurrencyRate(oid);
            pstCurrencyRate.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCurrencyRate(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector listAll(){
        return list(0, 50000, "","");
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CURRENCY_RATE;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    
                    break;
                    
                case DBHandler.DBSVR_SYBASE :
                    break;
                    
                case DBHandler.DBSVR_ORACLE :
                    break;
                    
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            }
            
            System.out.println("SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                CurrencyRate currencyrate = new CurrencyRate();
                resultToObject(rs, currencyrate);
                lists.add(currencyrate);
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
    
    private static void resultToObject(ResultSet rs, CurrencyRate currencyrate){
        try{
            currencyrate.setOID(rs.getLong(PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_RATE_ID]));
            currencyrate.setDate(DBHandler.convertDate(rs.getDate(PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_DATE]),rs.getTime(PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_DATE])));
            currencyrate.setValue(rs.getDouble(PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_VALUE]));
            
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long rateId){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT * FROM " + TBL_CURRENCY_RATE + " WHERE " +
            PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_RATE_ID] + " = " + rateId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) { result = true; }
            rs.close();
        }catch(Exception e){
            System.out.println("err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_RATE_ID] + ") FROM " + TBL_CURRENCY_RATE;
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
    
    
    /* This method used to find current data */
    public static int findLimitStart( long oid, int recordToGet, String whereClause){
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, order);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    CurrencyRate currencyrate = (CurrencyRate)list.get(ls);
                    if(oid == currencyrate.getOID())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    
    public static double getLastCurrency(){
        DBResultSet dbrs = null;
        double currency = 0;
        try{
            String sql = "SELECT * FROM "+TBL_CURRENCY_RATE+" ORDER BY "+fieldNames[FLD_DATE]+" DESC";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()){
                CurrencyRate currencyRate = new CurrencyRate();
                resultToObject(rs,currencyRate);
                
                currency = currencyRate.getValue();
                break;
            }
            
        }catch(Exception e){
            System.out.println("Err get last Currency : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return currency;
        }
    }
    
    /***  function for data synchronization ***/
    public long insertExcSynch(Entity ent) throws Exception{
        return insertExcSynch((CurrencyRate) ent);
    }
    
    public static long insertExcSynch(CurrencyRate currencyRate) throws DBException{
        long newOID = 0;
        long originalOID = currencyRate.getOID();
        try{
            newOID= insertExc(currencyRate);
            if(newOID!=0){  // sukses insert ?
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCurrencyRate(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_CURRENCY_RATE + " SET " +
            PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_RATE_ID] + " = " + originalOID +
            " WHERE " + PstCurrencyRate.fieldNames[PstCurrencyRate.FLD_RATE_ID] +
            " = " + newOID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            rs.close();
            
            return originalOID;
        }catch(Exception e) {
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }
    /*** -------------------------- ***/
    
    
}
