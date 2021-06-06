/*
 * SessCashier.java
 *
 * Created on October 8, 2003, 8:58 AM
 */

package com.dimata.pos.session.balance;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.PstBillMain;
/*package java*/
import java.sql.ResultSet;
import java.util.Vector;

//import entity

/**
 *
 * @author  pman
 * @version 
 */
public class SessCashier {
    private static String TBL_CASH_CASHIER="CASH_CASHIER";
    private static String TBL_BILL_MAIN="CASH_BILL_MAIN";
    /** Creates new SessCashier */
    public SessCashier() {
    }
  
    public static Vector checkCashier(long userId){
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
           
            String sql="SELECT "+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+","
                                +PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+
                       " FROM "+TBL_CASH_CASHIER+
                       " WHERE (("+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+"="+userId+") AND "+
                               "("+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"=1))";
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector cashier=new Vector(); 
               cashier.add(""+rs.getString(1));
               cashier.add(""+rs.getString(2));
               lists.add(cashier);
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


  public static Vector getCashSetting(long cashId){
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            //String sql = "select sku,name,default_price,default_sell_unit_id,default_price_currency_id,material_id,PRICE_TYPE_01,PRICE_TYPE_02,PRICE_TYPE_03 from MATERIAL where name like '"+key+"%'";
            String sql="SELECT CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID]+","+
                             " CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+","+
                             " CB."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+","+
                             " CB."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+","+
                             " CB."+PstBillMain.fieldNames[PstBillMain.FLD_TAX_PERCENTAGE]+","+
                             " CB."+PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_PCT]+
                       " FROM "+TBL_CASH_CASHIER+" CC "+
                       " INNER JOIN "+TBL_BILL_MAIN+" CB "+
                       " ON CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" = "+
                       " CB."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
                       " WHERE CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+"="+cashId+
                       " GROUP BY CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID]+","+
                                " CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+","+
                                " CB."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+","+
                                " CB."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+","+
                                " CB."+PstBillMain.fieldNames[PstBillMain.FLD_TAX_PERCENTAGE]+","+
                                " CB."+PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_PCT];
                               
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector cashier=new Vector(); 
               cashier.add(""+rs.getString(1));
               cashier.add(""+rs.getString(2));
               cashier.add(""+rs.getString(3));
               cashier.add(""+rs.getString(4));
               cashier.add(""+rs.getString(5));
               cashier.add(""+rs.getString(6));
               lists.add(cashier);
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

//query to get kode toko
 public static String getKodeToko(long locationId){
        String kodeToko="";
        DBResultSet dbrs = null;
        try{
           String sql = "SELECT CODE FROM LOCATION"+
                        " WHERE LOCATION_ID ="+locationId;
            //System.out.println(sql);            
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
               kodeToko=rs.getString(1);
            }
            rs.close();
            //return lists;
	}catch(Exception e) {
		System.out.println(e);
	}finally {
		DBResultSet.close(dbrs);
	}
        return kodeToko;
}



public static void main(String args[]){
  
   System.out.println("kode toko ="+ SessCashier.getKodeToko(504404223189962406L));  
}
}
