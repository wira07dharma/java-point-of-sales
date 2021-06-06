/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.masterdata.BussinessCenterBudget;
import com.dimata.aiso.entity.masterdata.PstBussCenterBudget;
import com.dimata.common.entity.payment.PstStandartRate;
import java.sql.ResultSet;

/**
 *
 * @author dwi
 */
public class SessBussCenterBgt {

    public static final String SESS_BUSS_CENTER_BGT = "SESS_BUSS_CENTER_BGT";
            
    public static synchronized int getCount()
    {
        DBResultSet dbrs = null;
        int record = 0;
        try
        {
            String sql = getStringCountQuery();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next())
            {
                record = rs.getInt("COUNT");
            }
            rs.close();
        }
        catch(Exception e)
        {}
        return record;
    }
    
    
    public static synchronized String getStringCountQuery()
    {
        String countSql = "";
        try
        {
            countSql = " SELECT COUNT("+PstBussCenterBudget.fieldNames[PstBussCenterBudget.FLD_BUSS_CENTER_BUDGET_ID]+") AS COUNT "+
                        " FROM "+PstBussCenterBudget.TBL_BUSSINESS_CENTER_BUDGET;
        }
        catch(Exception e)
        {}
        return countSql;
    }

    public static synchronized double getInCurrencyAmount(long currId, double rate, double amount){
        double dResult = 0;
        try{
            long localCurrId = getIdLocalCurrency();
            if(localCurrId != 0){
                if(localCurrId == currId){
                    
                }
            }
        }catch(Exception e){}
        return dResult;
    }   
    
    public static synchronized long getIdLocalCurrency()
    {
        DBResultSet dbrs = null;
        long lResult = 0;
        try
        {
            String sql = " SELECT "+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
                         " FROM "+PstStandartRate.TBL_POS_STANDART_RATE+
                         " WHERE "+PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]+" = 1 "+
                         " AND "+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+
                         " = "+PstStandartRate.ACTIVE;
            
            System.out.println("SessBussCenterBgt.getIdLocalCurrency() :::: "+ sql);
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
    
    public static synchronized long getIdBussCenterBgtMaster(BussinessCenterBudget bCntBgtTrans){
        DBResultSet dbrs = null;
        long lResult = 0;
        try{
            String sql = " SELECT "+PstBussCenterBudget.fieldNames[PstBussCenterBudget.FLD_BUSS_CENTER_BUDGET_ID]+
                         " FROM "+PstBussCenterBudget.TBL_BUSSINESS_CENTER_BUDGET+
                         " WHERE "+PstBussCenterBudget.fieldNames[PstBussCenterBudget.FLD_BUSS_CENTER_ID]+
                         " = "+bCntBgtTrans.getBussCenterId()+
                         " AND "+PstBussCenterBudget.fieldNames[PstBussCenterBudget.FLD_ID_PERKIRAAN]+
                         " = "+bCntBgtTrans.getIdPerkiraan()+
                         " AND "+PstBussCenterBudget.fieldNames[PstBussCenterBudget.FLD_PERIODE_ID]+
                         " = "+bCntBgtTrans.getPeriodeId();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.isFirst()){
                    lResult = rs.getLong(PstBussCenterBudget.fieldNames[PstBussCenterBudget.FLD_BUSS_CENTER_BUDGET_ID]);
                }
            }
            rs.close();
        }catch(Exception e){}
        return lResult;
    }
}