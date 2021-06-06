/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.masterdata.DailyRate;
import com.dimata.aiso.entity.masterdata.PstDailyRate;
import com.dimata.common.entity.currency.PstCurrencyType;
import com.dimata.common.entity.payment.PstStandartRate;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class SessDailyRate {

    public static final String SESS_DAILY_RATE = "SESS_DAILY_RATE";
    
    public static synchronized Vector getDataCurrency()
    {
        DBResultSet dbrs = null;
        Vector vResult = new Vector();
        try
        {
            String sql = getStringQuery();
            //System.out.println(" SQL SessDailyRate.getDataCurrency() :::::::::::: " +sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next())
            {
                DailyRate objDailyRate = new DailyRate();
                resultToObject(rs, objDailyRate);
                vResult.add(objDailyRate);
            }
            rs.close();
        }
        catch(Exception e){}
        return vResult;
    }
    
    public static synchronized void resultToObject(ResultSet rs, DailyRate objDailyRate)
    {
        try{
            objDailyRate.setCurrencyId(rs.getLong(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]));
            objDailyRate.setBuyingAmount(rs.getDouble(PstDailyRate.fieldNames[PstDailyRate.FLD_SELLING_AMOUNT]));
            objDailyRate.setSellingAmount(rs.getDouble(PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]));
        }catch(Exception e){}
    }
    
    public static synchronized String getStringQuery()
    {
        String sql = "";
        try
        {
            sql = " SELECT C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                    ", D."+PstDailyRate.fieldNames[PstDailyRate.FLD_SELLING_AMOUNT]+
                    ", S."+PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]+
                    " FROM "+PstCurrencyType.TBL_CURRENCY_TYPE+" AS C "+
                    " INNER JOIN "+PstDailyRate.TBL_DAILY_RATE+" AS D "+
                    " ON C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                    " = D."+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                    " INNER JOIN "+PstStandartRate.TBL_POS_STANDART_RATE+" AS S "+
                    " ON C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                    " = S."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
                    " WHERE D."+PstDailyRate.fieldNames[PstDailyRate.FLD_STATUS]+
                    " = "+PstStandartRate.ACTIVE+
                    " AND S."+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+
                    " = "+PstStandartRate.ACTIVE;
        }   
        catch(Exception e){}
        return sql;
    }
    
    public static synchronized long getIdLocalCurrency(){
        DBResultSet dbrs = null;
        long lResult = 0;
        try{
            String sql = " SELECT "+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                        " FROM "+PstDailyRate.TBL_DAILY_RATE+
                        " WHERE "+PstDailyRate.fieldNames[PstDailyRate.FLD_LOCAL_FOREIGN]+
                        " = "+PstDailyRate.LOCAL;
            //System.out.println("getIdLocalCurrency ::::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.isFirst()){
                    lResult = rs.getLong(PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]);
                }
            }
            rs.close();
        }catch(Exception e){}
        return lResult;
    }
    
    public static synchronized long getIdDefaultTrnsFrgCurrency(){
        DBResultSet dbrs = null;
        long lResult = 0;
        try{
            String sql = " SELECT "+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                        " FROM "+PstDailyRate.TBL_DAILY_RATE+
                        " WHERE "+PstDailyRate.fieldNames[PstDailyRate.FLD_LOCAL_FOREIGN]+
                        " = "+PstDailyRate.DEFAULT_FOREIGN;
            //System.out.println("getIdDefaultTrnsFrgCurrency ::::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.isFirst()){
                    lResult = rs.getLong(PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]);
                }
            }
            rs.close();
        }catch(Exception e){}
        return lResult;
    }

    public static synchronized long getIdDefaultStdFrgCurrency(){
        DBResultSet dbrs = null;
        long lResult = 0;
        try{
           String sql = " SELECT S."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
                        " FROM "+PstStandartRate.TBL_POS_STANDART_RATE+" AS S "+
                        " INNER JOIN "+PstDailyRate.TBL_DAILY_RATE+" AS D "+
                        " ON S."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
                        " = D."+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                        " WHERE D."+PstDailyRate.fieldNames[PstDailyRate.FLD_LOCAL_FOREIGN]+
                        " = "+PstDailyRate.DEFAULT_FOREIGN;
           //System.out.println("getIdDefaultStdFrgCurrency ::::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.isFirst()){
                    lResult = rs.getLong(PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]);
                }
            }
            rs.close();
        }catch(Exception e){}
        return lResult;
    }
    
    public static synchronized double getDefaultCurrRate(){
        DBResultSet dbrs = null;
        double dResult = 0.0;
        try{
            String sql = " SELECT "+PstDailyRate.fieldNames[PstDailyRate.FLD_SELLING_AMOUNT]+
                        " FROM "+PstDailyRate.TBL_DAILY_RATE+
                        " WHERE "+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                        " = "+getIdDefaultTrnsFrgCurrency();
            
            //System.out.println("getDefaultCurrRate ::::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.isFirst()){
                    dResult = rs.getDouble(PstDailyRate.fieldNames[PstDailyRate.FLD_SELLING_AMOUNT]);
                }
            }
            rs.close();
        }catch(Exception e){}
        return dResult;
    }
    
    public static synchronized double getDefaultFrgStdRate(){
        DBResultSet dbrs = null;
        double dResult = 0.0;
        try{
            String sql = " SELECT S."+PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]+
                        " FROM "+PstStandartRate.TBL_POS_STANDART_RATE+" AS S "+
                        " INNER JOIN "+PstDailyRate.TBL_DAILY_RATE+" AS D "+
                         " ON S."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
                        " = D."+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                        " WHERE D."+PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_ID]+
                        " = "+getIdDefaultStdFrgCurrency();
            
            //System.out.println("getDefaultFrgStdRate ::::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.isFirst()){
                    dResult = rs.getDouble(PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]);
                }
            }
            rs.close();
        }catch(Exception e){}
        return dResult;
    }
}
