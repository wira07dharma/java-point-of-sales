/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.session.report;
import com.dimata.common.entity.custom.CustomeReportMaster;
import com.dimata.common.entity.payment.PstPaymentSystem;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillDetailVoid;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.PstCashCreditCard;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashPayment1;
import com.dimata.pos.entity.payment.PstCashReturn;
import java.sql.*;
import java.util.*;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
/**
 *
 * @author dimata005
 */
public class SessCustomeReport {
    public static Vector listCustomeReport(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM  custome_report_master ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                CustomeReportMaster customeReportMaster = new CustomeReportMaster();
                //resultToObject(rs, settingEmail);
                customeReportMaster.setOID(rs.getLong("CUSTOME_REPORT_MASTER_ID"));
                customeReportMaster.setCustomeReportName(rs.getString("CUSTOME_REPORT_NAME"));
                lists.add(customeReportMaster);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static String getQueryReport(long oidReport, String whereClause, String order) {
        String query = "";
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM  custome_report_master ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                query = rs.getString("CUSTOME_QUERY_REPORT");
            }
            rs.close();
            return query;
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return query;
    }
    
    public static int getIsCustome(long oidReport, String whereClause, String order) {
        int query = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM  custome_report_master ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                query = rs.getInt("IS_CUSTOME");
            }
            rs.close();
            return query;
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return query;
    }
    
    public static String getQueryReportWhere(long oidReport, String whereClause, String order) {
        String query = "";
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM  custome_report_where ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                query = rs.getString("CUSTOME_QUERY_REPORT");
            }
            rs.close();
            return query;
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return query;
    }
    
    public static double getTotalPaymentByPaymentSystem(long oidBillMain,long oidPaymentSystem){
       return getTotalPaymentByPaymentSystem(oidBillMain,oidPaymentSystem,"",""); 
    }
    
    public static double getTotalPaymentByPaymentSystem(long oidBillMain,long oidPaymentSystem, String where){
        return getTotalPaymentByPaymentSystem(oidBillMain,oidPaymentSystem,where,""); 
    }
    
    public static double getTotalPaymentByPaymentSystem(long oidBillMain,long oidPaymentSystem, String where, String whereReturn){
        double total = 0;
        double totalPayment = 0;
        double totalReturn = 0;
        String wherePaymentSystem = "";
        String whereReturnSystem = "";
        
        if(oidBillMain!=0){
            wherePaymentSystem =" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ="+oidBillMain+"";
        }
        
        if(oidPaymentSystem!=0){
            wherePaymentSystem=wherePaymentSystem+ " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+oidPaymentSystem+"";
        }
        
        if(!where.equals("")){
            if(wherePaymentSystem.equals("")){
                wherePaymentSystem=wherePaymentSystem+" "+where; 
            }else{
                wherePaymentSystem=wherePaymentSystem+" AND "+where; 
            }
        }
        
        totalPayment = PstBillMain.getSummaryByPaymentSystem(wherePaymentSystem);
        
        if(oidBillMain!=0){
            whereReturnSystem =""+ " "+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"="+oidBillMain+"";
        }
        
        if(oidBillMain!=0){
            Vector listReturnSystem = PstCashReturn.list(0, 0, whereReturnSystem, "");
            if (listReturnSystem.size()>0){
                CashReturn cashReturn = (CashReturn) listReturnSystem.get(0);
                totalReturn = cashReturn.getAmount();
            }
        }else{
            totalReturn = PstCashReturn.getReturnByBillMainDateByPaymentSystemType(whereReturn);
        }
        
        if (totalPayment>0){
            total = totalPayment - totalReturn;
        }else{
            total = totalPayment;
        }
        
        return total;
    }
    
    public static double getTotalByCategory(Long oidBillMain, int categoryFood){
           return getTotalByCategory( oidBillMain,categoryFood,""); 
    }
    
    public static double getTotalByCategory(long oidBillMain, int categoryFood, String where){
        double total = 0;
        String whereCatalog="";
        String whereClause="";
        String whereFood="";
        
        if (categoryFood!=0 && categoryFood!=1){
            whereFood = " "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>0 AND "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>1 ";  
        }else{
            whereFood = " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"="+categoryFood+"";    
        }
         
        Vector listCategory= PstCategory.list(0,0, whereFood, "");
        
        for (int i=0;i<listCategory.size();i++){
            Category category = (Category) listCategory.get(i);
            if(i==0){
                whereCatalog += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereCatalog += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        if(oidBillMain!=0){
            whereClause =" cbm."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+oidBillMain+"";
        }
        if(!where.equals("")){
            whereClause=where;
        }
        
        if(!whereCatalog.equals("")){
            whereClause += " AND ("+whereCatalog+")";
        }else{
            return 0;
        }
        
        
        total = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClause);
        
        return total;
    }
    
    public static double getTotalByCategoryVoid(long oidBillMain, int categoryFood, String where){
        double total = 0;
        String whereCatalog="";
        String whereClause="";
        String whereFood="";
        
        if (categoryFood!=0 && categoryFood!=1){
            whereFood = " "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>0 AND "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>1 ";  
        }else{
            whereFood = " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"="+categoryFood+"";    
        }
         
        Vector listCategory= PstCategory.list(0,0, whereFood, "");
        
        for (int i=0;i<listCategory.size();i++){
            Category category = (Category) listCategory.get(i);
            if(i==0){
                whereCatalog += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereCatalog += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        if(oidBillMain!=0){
            whereClause =" cbm."+PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]+"="+oidBillMain+"";
        }
        if(!where.equals("")){
            whereClause=where;
        }
        
        if(!whereCatalog.equals("")){
            whereClause += " AND ("+whereCatalog+")";
        }else{
            return 0;
        }
        
        
        total = PstBillDetail.getSumTotalPriceByCategoryAndBillMainVoid(whereClause);
        
        return total;
    }
    
    
    public static double getTotalByCategoryCompliment(long oidBillMain, int categoryFood, String where, long standardId, long priceTypeId){
        double total = 0;
        String whereCatalog="";
        String whereClause="";
        String whereFood="";
        
        if (categoryFood!=0 && categoryFood!=1){
            whereFood = " "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>0 AND "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>1 ";  
        }else{
            whereFood = " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"="+categoryFood+"";    
        }
         
        Vector listCategory= PstCategory.list(0,0, whereFood, "");
        
        for (int i=0;i<listCategory.size();i++){
            Category category = (Category) listCategory.get(i);
            if(i==0){
                whereCatalog += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereCatalog += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        if(oidBillMain!=0){
            whereClause =" pcm.COSTING_MATERIAL_ID="+oidBillMain+"";
        }
        if(!where.equals("")){
            whereClause=where;
        }
        
        if(!whereCatalog.equals("")){
            whereClause += " AND ("+whereCatalog+")";
        }else{
            return 0;
        }
        
        
        total = getPriceCompliment(standardId, priceTypeId , whereClause);
        
        return total;
    }
    
    
     public static double getPriceCompliment(long standartRateOid, long priceTypeOid, String where) {
        DBResultSet dbrs = null;
        double price = 0;
        try {
            String sql = "SELECT SUM(pptp." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] + " * pcm.QTY_COMPOSITE) AS PRICE" +
                    " FROM " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING +" AS pptp "+
                    " INNER JOIN pos_costing_material_item as pcm ON pcm.MATERIAL_ID=pptp.MATERIAL_ID "+
                    " INNER JOIN pos_material as pm ON pm.MATERIAL_ID=pcm.MATERIAL_ID "+
                    " INNER JOIN pos_category as pc ON pc.CATEGORY_ID=pm.CATEGORY_ID "+
                    " WHERE "+
                    " pptp." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + "=" + standartRateOid +
                    " AND pptp." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + "=" + priceTypeOid;
            
            sql = sql + " AND "+where;
            
            System.out.println("getSellPrice sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                price = rs.getDouble("PRICE");
            }
        } catch (Exception e) {
            System.out.println("Error get getSellPrice : " + e.toString());
        }
        return price;
    }
    
    
    public static double getTotalPaymentByCashierId(String CashierId,long oidPaymentSystem){
        double total = 0;
        double totalPayment = 0;
        double totalReturn = 0;
        String wherePaymentSystem = "";
        String whereReturnSystem = "";
        String cashCashierId= " ("+CashierId+")";
        wherePaymentSystem =""+ " ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+oidPaymentSystem+"" ;
        wherePaymentSystem=wherePaymentSystem+" AND "+cashCashierId;
        totalPayment = PstBillMain.getSummaryByPaymentSystem(wherePaymentSystem);
        totalReturn = PstCashReturn.getReturnSummary(0, cashCashierId);
        if (totalPayment>0){
            total = totalPayment - totalReturn;
        }else{
            total = totalPayment;
        }
        return total;
    }
    
    public static double getTotalPaymentReceive(long oidBillMain,long oidPaymentSystem,String where){
        double total = 0;
        double totalPayment = 0;
        double totalReturn = 0;
        String wherePaymentSystem = "";
        String whereReturnSystem = "";
        
        wherePaymentSystem =" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ="+oidBillMain+"";
        
        if(oidPaymentSystem!=0){
            wherePaymentSystem=wherePaymentSystem+ " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+oidPaymentSystem+"";
        }
        
        if(!where.equals("")){
           wherePaymentSystem=wherePaymentSystem+" AND "+where; 
        }
        
        totalPayment = PstBillMain.getSummaryByPaymentSystem(wherePaymentSystem);
        
        if (totalPayment>0){
            total = totalPayment;
        }else{
            total = totalPayment;
        }
        
        return total;
    }
    
    public static double getTotalPaymentReturn(long oidBillMain,long oidPaymentSystem,String where){
        double total = 0;
        double totalReturn = 0;
        String whereReturnSystem = "";
        
        whereReturnSystem =""
            + " "+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"="+oidBillMain+"";
        Vector listReturnSystem = PstCashReturn.list(0, 0, whereReturnSystem, "");
        if (listReturnSystem.size()>0){
            CashReturn cashReturn = (CashReturn) listReturnSystem.get(0);
            totalReturn = cashReturn.getAmount();
        }
        
        total = totalReturn;
        
        return total;
    }
    
    
     public static Vector getDetailCreditCard(long oidBillMain,long oidPaymentSystem,String where){
        String whereCreditCardInfo = "";
        
        whereCreditCardInfo =" cash_payment."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+"="+oidBillMain+""+
                             " "+where;
        Vector listReturnSystem = PstCashCreditCard.listDetailPaymentCard(whereCreditCardInfo);
        
        return listReturnSystem;
    }
    
}
