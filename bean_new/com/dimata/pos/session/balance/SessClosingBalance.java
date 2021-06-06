/*
 * SessClosingBalance.java
 *
 * Created on June 2, 2003, 2:19 PM
 */

package com.dimata.pos.session.balance;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashReturn;
/**
 *
 * @author  Administrator
 * @version 
 */
/*package java*/
import java.sql.ResultSet;
import java.util.Vector;

//import entity

public class SessClosingBalance {
    private static String TBL_MATERIAL="material"; 
    /** Creates new SessClosingBalance */
    public SessClosingBalance() {
    }
    
    
  public Vector getMaster(long cashMasterId){  
  Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            String sql=" SELECT cash_master.CASH_MASTER_ID, cash_master.LOCATION_ID, cash_master.CASHIER_NUMBER, cash_master.TAX, cash_master.SERVICE, cash_master.PRICE_TYPE, location.NAME"+
                       " FROM cash_master INNER JOIN location ON cash_master.LOCATION_ID = location.LOCATION_ID"+
                       " WHERE (((cash_master.CASH_MASTER_ID)="+cashMasterId+"))"+
                       " GROUP BY cash_master.CASH_MASTER_ID, cash_master.LOCATION_ID, cash_master.TAX, cash_master.SERVICE, cash_master.PRICE_TYPE, location.NAME";
            
            
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector jumlah=new Vector(); 
               jumlah.add(""+rs.getLong(1));
               jumlah.add(""+rs.getLong(2));
               jumlah.add(""+rs.getInt(3));
               jumlah.add(""+rs.getDouble(4));
               jumlah.add(""+rs.getDouble(5));
               jumlah.add(""+rs.getString(6));
               jumlah.add(""+rs.getString(7));
               lists.add(jumlah);
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
   
  public Vector getCashierShift(){  
  Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            //String sql = "select cash_master_id,location,cashier_name  from CASH_MASTER  where cash_master_id="+cashMasterId;
            String sql=" SELECT * FROM SHIFT ";
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector shift=new Vector(); 
               shift.add(""+rs.getLong(1));
               shift.add(""+rs.getString(2));
               shift.add(""+rs.getString(3));
               lists.add(shift);
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
   
 /* public Vector getMasterCash(long cashMasterId){  
  Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            String sql = "SELECT CASH_MASTER.location, CASH_MASTER.cashier_name, CASH_TYPE.service, CASH_TYPE.tax "+
                         "FROM CASH_MASTER INNER JOIN CASH_TYPE ON CASH_MASTER.cash_type_id = CASH_TYPE.cash_type_id "+
                         "WHERE (((CASH_MASTER.cash_master_id)="+cashMasterId+")) "+
                         "GROUP BY CASH_MASTER.location, CASH_MASTER.cashier_name, CASH_TYPE.service, CASH_TYPE.tax";

            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector jumlah=new Vector(); 
               jumlah.add(""+rs.getString(1));
               jumlah.add(""+rs.getString(2));
               jumlah.add(""+rs.getDouble(3));
               jumlah.add(""+rs.getDouble(4));
               lists.add(jumlah);
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
   */
    public static Vector sumPaymentPerShift(long cashId){
     
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
             String sql = " SELECT CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+", "+
                                  "CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+", "+
                                  "SUM(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+") AS SumOfamount_rp "+
                          "FROM ("+PstCashPayment.TBL_PAYMENT+" CP "+
                          "INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" CBM ON "+
                          "CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+" = "+
                          "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+") "+
                          "INNER JOIN "+PstCashCashier.TBL_CASH_CASHIER+" CC ON "+
                          "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+" = "+
                          "CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" "+
                          "WHERE CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" = "+cashId+" "+
                          "AND CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+" = 0 "+
                          "GROUP BY CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+", "+
                          "CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID];
        
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector vector=new Vector(); 
               vector.add(""+rs.getLong(1));
               vector.add(""+rs.getLong(2));
               vector.add(""+rs.getDouble("SumOfamount_rp"));
               lists.add(vector);
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

public static Vector sumReturnPerShift(long cashId){
     
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
           String sql = " SELECT CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+", "+
                                  "CR."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+", "+
                                  "SUM(CR."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+") AS SumOfamount_rt "+
                          "FROM ("+PstCashReturn.TBL_RETURN+" CR "+
                          "INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" CBM ON "+
                          "CR."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+" = "+
                          "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+") "+
                          "INNER JOIN "+PstCashCashier.TBL_CASH_CASHIER+" CC ON "+
                          "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+" = "+
                          "CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" "+
                          "WHERE CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" = "+cashId+" "+
                          "GROUP BY CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+", "+
                          "CR."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID];
         //504404213837800286
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector vector=new Vector(); 
               vector.add(""+rs.getLong(1));
               vector.add(""+rs.getLong(2));
               vector.add(""+rs.getDouble("SumOfamount_rt"));
               lists.add(vector);
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
/*
public static Vector getAllCurrency(){
     
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
          
           String sql = "SELECT CASH_CURRENCY.currency_id, CASH_CURRENCY.code, CASH_EXCHANGE_RATE.buying_rate, CASH_EXCHANGE_RATE.selling_rate "+
                        "FROM CASH_CURRENCY INNER JOIN CASH_EXCHANGE_RATE ON CASH_CURRENCY.currency_id = CASH_EXCHANGE_RATE.currency_id "+
                        "WHERE (((CASH_EXCHANGE_RATE.used_as_default)=1)) "+
                        "GROUP BY CASH_CURRENCY.currency_id, CASH_CURRENCY.code,CASH_EXCHANGE_RATE.buying_rate, CASH_EXCHANGE_RATE.selling_rate";


            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector vector=new Vector(); 
               vector.add(""+rs.getLong(1));
               vector.add(""+rs.getString(2));
               vector.add(""+rs.getDouble(3));
               vector.add(""+rs.getDouble(4));
               lists.add(vector);
            }
            rs.close();
            return lists;
	}catch(Exception e) {
		System.out.println(e);
	}finally {
		DBResultSet.close(dbrs);
	}
  return new Vector();
}*/


public static Vector getPayByCreditCard(long cashId){
     
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
          
           /*String sql ="SELECT CASH_PAYMENT.currency_id, Sum(CASH_PAYMENT.amount) AS SumOfamount_rp "+
                       "FROM (CASH_PAYMENT INNER JOIN CASH_BILL_MAIN 
                        ON CASH_PAYMENT.cash_bill_main_id = CASH_BILL_MAIN.cash_bill_main_id) 
                        INNER JOIN CASH_CASHIER ON CASH_BILL_MAIN.cash_cashier_id = CASH_CASHIER.cash_cashier_id "+
                       "WHERE (((CASH_CASHIER.cash_cashier_id)= "+cashId+") AND ((CASH_PAYMENT.pay_type)=1)) "+
                       "GROUP BY CASH_PAYMENT.currency_id";*/

            String sql=" SELECT CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+","+
                       " SUM("+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+") AS SumOfamount_rp"+
                       " FROM ("+PstCashPayment.TBL_PAYMENT+" CP"+
                       " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" CBM ON"+
                       " CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+" = "+
                       " CBM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+")"+
                       " INNER JOIN "+PstCashCashier.TBL_CASH_CASHIER+" CC ON"+
                       " CBM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+" = "+
                       " CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
                       " WHERE (((CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+")="+cashId+")"+
                       " AND ((CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+")="+PstCashPayment.CARD+"))"+
                       " GROUP BY CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID];
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector vector=new Vector(); 
               vector.add(""+rs.getLong(1));
               vector.add(""+rs.getDouble(2));
               lists.add(vector);
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

public static Vector sumOpeningBalance(long cashId){
     
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
             String sql = "SELECT "+
                          "CB."+PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID]+", "+
                          "SUM(CB."+PstBalance.fieldNames[PstBalance.FLD_BALANCE_VALUE]+") AS SumBalance "+
                          "FROM "+PstBalance.TBL_BALANCE+" CB "+
                          "WHERE CB."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+" =  "+cashId+" "+ //504404214598756723
                          "GROUP BY CB."+PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID];
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector jumlah=new Vector(); 
               jumlah.add(""+rs.getLong(1));
               jumlah.add(""+rs.getDouble("SumBalance"));
               lists.add(jumlah);
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

  /* 
   public static Vector searching(String key){
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            //String sql = "select sku,name,default_price,default_sell_unit_id,default_price_currency_id,material_id,PRICE_TYPE_01,PRICE_TYPE_02,PRICE_TYPE_03 from MATERIAL where name like '"+key+"%'";
            String sql="SELECT "+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_NAME]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_01]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_02]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_03]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]+
                       " FROM "+TBL_MATERIAL+
                       " WHERE "+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE '"+key+"%'";
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector product=new Vector(); 
               product.add(""+rs.getString(1));
               product.add(""+rs.getString(2));
               product.add(""+rs.getString(3));
               product.add(""+rs.getString(4));
               product.add(""+rs.getString(5));
               product.add(""+rs.getString(6));
               product.add(""+rs.getString(7));
               product.add(""+rs.getString(8));
               product.add(""+rs.getString(9));
               product.add(""+rs.getString(10));
               product.add(""+rs.getString(11));
               lists.add(product);
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


 public static Vector searching2(String key){
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            // String sql = "select sku,name,default_price,default_sell_unit_id,default_price_currency_id,material_id,PRICE_TYPE_01,PRICE_TYPE_02,PRICE_TYPE_03 from MATERIAL where sku like '"+key+"%'";
            String sql="SELECT "+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_NAME]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_01]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_02]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_03]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+","
                                +PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]+
                       " FROM "+TBL_MATERIAL+
                       " WHERE "+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" LIKE '"+key+"%'";
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector product=new Vector(); 
               product.add(""+rs.getString(1));
               product.add(""+rs.getString(2));
               product.add(""+rs.getString(3));
               product.add(""+rs.getString(4));
               product.add(""+rs.getString(5));
               product.add(""+rs.getString(6));
               product.add(""+rs.getString(7));
               product.add(""+rs.getString(8));
               product.add(""+rs.getString(9));
               product.add(""+rs.getString(10));
               product.add(""+rs.getString(11));
               lists.add(product);
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

  public static Vector getUnit(){
        Vector lists = new Vector(); 
	DBResultSet dbrs = null;
        try{
            String sql = "select * from UNIT ";
            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();           
            while(rs.next()) {
               Vector unit=new Vector(); 
               unit.add(""+rs.getString(1));
               unit.add(""+rs.getString(2));
               unit.add(""+rs.getString(3));
               unit.add(""+rs.getString(4));
               lists.add(unit);
            }
            rs.close();
            return lists;
	}catch(Exception e) {
		System.out.println(e);
	}finally {
		DBResultSet.close(dbrs);
	}
  return new Vector();
}*/
  
public static void main(String args[]){
     SessClosingBalance sess=new SessClosingBalance();
        Vector vct=sess.getMaster(1);   
        System.out.println(" "+vct);
}
}
