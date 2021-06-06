/*
 * SessPrintBalance.java
 *
 * Created on January 7, 2005, 12:04 PM
 */

package com.dimata.pos.session.balance;

import com.dimata.pos.cashier.CashSaleController;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.printAPI.PrintBalanceObj;
/**
 *
 * @author  yogi
 */
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

public class SessPrintBalance {
    
    /** Creates a new instance of SessPrintBalance */
    public SessPrintBalance() {
    }
    
    public static PrintBalanceObj getData(CashCashier cashCashier){
        PrintBalanceObj obj = new PrintBalanceObj();
        if(cashCashier!=null){
            obj.setDateBalance(cashCashier.getCashDate());            
            try{                
                if(cashCashier.getAppUserId()!=0){
                    AppUser appUser = PstAppUser.fetch(cashCashier.getAppUserId());
                    obj.setCashier(appUser.getFullName());
                }
            }catch(Exception e){
                System.out.println("err fetch nama kasir :  "+e.toString());
                e.printStackTrace();
            }
            obj.setListDataBalance(listDataBalance(cashCashier.getOID()));
        }
        return obj;
    }
    
    public static PrintBalanceObj getData(CashCashier cashCashier,Vector listObjBalance,Hashtable hashCurrency){
        PrintBalanceObj obj = new PrintBalanceObj();
        if(cashCashier!=null){
            obj.setDateBalance(cashCashier.getCashDate());            
            try{                
                if(cashCashier.getAppUserId()!=0){
                    AppUser appUser = PstAppUser.fetch(cashCashier.getAppUserId());
                    obj.setCashier(appUser.getFullName());
                }
            }catch(Exception e){
                System.out.println("err fetch nama kasir :  "+e.toString());
                e.printStackTrace();
            }
            obj.setListDataBalance(listDataBalance(listObjBalance,hashCurrency));
        }
        return obj;
    }
    
    //convert data from obj Vector && Hashtable
    // vector content objbalance
    //hashtable hashcurrency content objcurency
    public static Vector listDataBalance(Vector listDataObjBalance,Hashtable hashCurrency){        
        Vector result = new Vector(1,1);
        if(listDataObjBalance!=null&&listDataObjBalance.size()>0){
            for(int i=0;i<listDataObjBalance.size();i++){
                Vector temp = new Vector(1,1);
                Balance balance = (Balance)listDataObjBalance.get(i);
                temp.add(balance);
                CurrencyType curr = (CurrencyType)hashCurrency.get(new Long(balance.getCurrencyOid()));
                temp.add(curr);
                //widi
                //using sandart rate, not daily rate
                StandartRate daily = CashSaleController.getStandartRate(""+balance.getCurrencyOid());                
                //StandartRate daily = CashSaleController.getLatestRate(""+balance.getCurrencyOid());                
                temp.add(daily);
                result.add(temp);
            }
        }
        return result;
    }    
        
    public static Vector listDataBalance(long oidCashCashier){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);        
        try{
            String sql = " SELECT BLC."+PstBalance.fieldNames[PstBalance.FLD_BALANCE_VALUE]+
                         ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
                         ", RT."+PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]+
                         " FROM "+PstBalance.TBL_BALANCE+" AS BLC "+
                         " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CURR "+
                         " ON BLC."+PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID]+
                         " = CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                         " INNER JOIN "+PstStandartRate.TBL_POS_STANDART_RATE+" AS RT "+
                         " ON CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                         " = RT."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
                         " WHERE "+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+
                         " = "+oidCashCashier+
                         " AND "+PstBalance.fieldNames[PstBalance.FLD_BALANCE_TYPE]+
                         " = 0 "+
                         " AND RT."+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+
                         " = "+PstStandartRate.ACTIVE;
            
            //System.out.println("SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector temp = new Vector(1,1);
                Balance balance = new Balance();
                balance.setBalanceValue(rs.getDouble(1));
                temp.add(balance);
                
                CurrencyType curr = new CurrencyType();
                curr.setCode(rs.getString(2));
                temp.add(curr);
                
                StandartRate rate = new StandartRate();
                rate.setSellingRate(rs.getDouble(3));
                temp.add(rate);
                result.add(temp);
                
            }
        }catch(Exception e){
            System.out.println("err list data balance : "+e.toString());
            e.printStackTrace();
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long oidCashCashier = 504404263258483611l;
        CashCashier cash = null;
        try{
            cash = PstCashCashier.fetchExc(oidCashCashier);            
        }catch(Exception e){
        }
        PrintBalanceObj obj = getData(cash);
        System.out.println("listBalance >>>>> : "+obj.getListDataBalance().size());
    }
    
}
