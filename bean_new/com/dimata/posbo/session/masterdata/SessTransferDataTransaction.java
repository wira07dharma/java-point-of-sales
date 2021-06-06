/*
 * SessTransferDataTransaction.java
 *
 * Created on April 9, 2004, 5:49 AM
 */

package com.dimata.posbo.session.masterdata;
import java.io.*;
import java.util.*;
import com.dimata.posbo.db.*;
import com.dimata.util.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.pos.entity.balance.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.pos.entity.masterCashier.*;

/**
 *
 * @author  Administrator
 * @version 
 */
public class SessTransferDataTransaction {

    public static String systemPathDelimeter = System.getProperty("file.separator");    
    
    public static final int TBL_TRANSFER_RECEIVE_MATERIAL = 0;  
    public static final int TBL_TRANSFER_RECEIVE_MATERIAL_ITEM = 1;
    public static final int TBL_TRANSFER_RETURN_MATERIAL = 2;
    public static final int TBL_TRANSFER_RETURN_MATERIAL_ITEM = 3;
    public static final int TBL_TRANSFER_DISPATCH_MATERIAL = 4;
    public static final int TBL_TRANSFER_DISPATCH_MATERIAL_ITEM = 5;
    public static final int TBL_TRANSFER_DISTRIBUTION_MAIN = 6;
    public static final int TBL_TRANSFER_DISTRIBUTION_DETAIL = 7; 
    public static final int TBL_TRANSFER_CASH_BILL_MAIN = 8;
    public static final int TBL_TRANSFER_CASH_BILL_DETAIL = 9;
    public static final int TBL_TRANSFER_CASH_BALANCE = 10;
    public static final int TBL_TRANSFER_CASH_CASHIER = 11;
    public static final int TBL_TRANSFER_CASH_PAYMENT = 12;
    public static final int TBL_TRANSFER_CASH_CREDIT_CARD = 13;
    public static final int TBL_TRANSFER_CASH_RETURN_PAYMENT = 14;    
    
    public static final String[] listTableTransferNames  = {
        "POS_RECEIVE_MATERIAL_TRANSFER",
        "POS_RECEIVE_MATERIAL_ITEM_TRANSFER",
        "POS_RETURN_MATERIAL_TRANSFER",
        "POS_RETURN_MATERIAL_ITEM_TRANSFER",
        "POS_DISPATCH_MATERIAL_TRANSFER",
        "POS_DISPATCH_MATERIAL_ITEM_TRANSFER",
        "POS_DISTRIBUTION_MATERIAL_TRANSFER",
        "POS_DISTRIBUTION_DETAIL_TRANSFER",
        "CASH_BILL_MAIN_TRANSFER",
        "CASH_BILL_DETAIL_TRANSFER",
        "CASH_BALANCE_TRANSFER",
        "CASH_CASHIER_TRANSFER",
        "CASH_PAYMENT_TRANSFER",
        "CASH_CREDIT_CARD_TRANSFER",
        "CASH_RETURN_PAYMENT_TRANSFER"
    };    

    
    // -------------------------- TRANSFER TRANSACTION OUT --------------------------    
    /**
     * Transfer data transaction out from specified location
     * @param transactionDate
     * @param oidLocation
     * @return  
     * @created by Edhy Putra
     */    
    public static boolean transferTransactionOut(Date transactionDate, long oidLocation, String sourcePath, String targetPath)
    {
	boolean result = false;	                
        
        result = transferReceiveOut(transactionDate,oidLocation);
        if (result == true)
        {
            result = transferReturnOut(transactionDate,oidLocation);            
        }
        else
        {
            System.out.println("Transfer Receive Out fail!!!");
        }                
        
        if (result == true)
        {
            result = transferDistributionOut(transactionDate,oidLocation);
        }
        else
        {
            System.out.println("Transfer Return Out fail!!!");
        }

        if (result == true)
        {
            result = transferDispatchOut(transactionDate,oidLocation);
        }
        else
        {
            System.out.println("Transfer Return Out fail!!!");
        }
        
        if (result == true)
        {
            result = transferSaleOut(transactionDate,oidLocation);
        }
        else
        {
            System.out.println("Transfer Dispatch Out fail!!!");
        }
        
        if (result == true)
        {
            copyFiles(sourcePath,targetPath,listTableTransferNames);
        }
        else
        {
            System.out.println("Transfer Sale Out fail!!!");        
        }
        
        return result;
    }
    
    
    /**
     * Transfer data receive transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferReceiveOut(Date transactionDate, long oidLocation)
    {
	boolean result = false;	        
        try
        {                       
            //---- DELETE DATA ---
            // Delete receive transaction from POS_RECEIVE_MATERIAL_TRANSFER
            String sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL];
            System.out.println("RECEIVE OUT 1st : "+sql);
            int res = DBHandler.execUpdate(sql);
            
            // Delete receive transaction from POS_RECEIVE_MATERIAL_ITEM_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL_ITEM];
            //System.out.println("RECEIVE OUT 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            
            //---- INSERT DATA ---
            // Insert data receive transaction to POS_RECEIVE_MATERIAL_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL] +
                  " SELECT *" + 
                  " FROM " + PstMatReceive.TBL_MAT_RECEIVE + 
                  " WHERE " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("RECEIVE OUT 3rd : "+sql);
            res = DBHandler.execUpdate(sql);            

            // Insert data receive transaction to POS_RECEIVE_MATERIAL_ITEM_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL_ITEM] +
                  " SELECT RMI.*" + 
                  " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " RM" + 
                  " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" + 
                  " ON RM." +  PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                  " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                  " WHERE RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("RECEIVE OUT 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer receive out : " + exc);
        }
        return result;
    }
    

    /**
     * Transfer data return transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferReturnOut(Date transactionDate, long oidLocation)
    {
	boolean result = false;	        
        try
        {                       
            //---- DELETE DATA ---
            // Delete return transaction from POS_RETURN_MATERIAL_TRANSFER
            String sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL];
            //System.out.println("RETURN OUT 1st : "+sql);
            int res = DBHandler.execUpdate(sql);
            
            // Delete return transaction from POS_RETURN_MATERIAL_ITEM_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL_ITEM];
            //System.out.println("RETURN OUT 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            
            //---- INSERT DATA ---
            // Insert data return transaction to POS_RETURN_MATERIAL_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL] +
                  " SELECT *" + 
                  " FROM " + PstMatReturn.TBL_MAT_RETURN + 
                  " WHERE " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("RETURN OUT 3rd : "+sql);
            res = DBHandler.execUpdate(sql);            

            // Insert data return transaction to POS_RETURN_MATERIAL_ITEM_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL_ITEM] +
                  " SELECT RMI.*" + 
                  " FROM " + PstMatReturn.TBL_MAT_RETURN + " RM" + 
                  " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" + 
                  " ON RM." +  PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                  " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                  " WHERE RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("RETURN OUT 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer return out : " + exc);
        }
        return result;
    }
    
    /**
     * Transfer data dispatch transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferDispatchOut(Date transactionDate, long oidLocation)
    {
	boolean result = false;	        
        try
        {                       
            //---- DELETE DATA ---
            // Delete dispatch transaction from POS_DISPATCH_MATERIAL_TRANSFER
            String sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL];
            //System.out.println("DISPATCH OUT 1st : "+sql);
            int res = DBHandler.execUpdate(sql);
            
            // Delete dispatch transaction from POS_DISPATCH_MATERIAL_ITEM_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL_ITEM];
            //System.out.println("DISPATCH OUT 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            
            //---- INSERT DATA ---
            // Insert data dispatch transaction to POS_DISPATCH_MATERIAL_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL] +
                  " SELECT *" + 
                  " FROM " + PstMatDispatch.TBL_DISPATCH + 
                  " WHERE " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("DISPATCH OUT 3rd : "+sql);
            res = DBHandler.execUpdate(sql);            

            // Insert data dispatch transaction to POS_DISPATCH_MATERIAL_ITEM_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL_ITEM] +
                  " SELECT DFI.*" + 
                  " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" + 
                  " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" + 
                  " ON DF." +  PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                  " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                  " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("DISPATCH OUT 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer dispatch out : " + exc);
        }
        return result;
    }    
    
    /**
     * Transfer data distribution transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferDistributionOut(Date transactionDate, long oidLocation)
    {
	boolean result = false;	        
        try
        {                       
            //---- DELETE DATA ---
            // Delete dispatch transaction from POS_DISTRIBUTION_MATERIAL_TRANSFER
            String sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_MAIN];
            //System.out.println("DISTRIBUTION OUT 1st : "+sql);
            int res = DBHandler.execUpdate(sql);
            
            // Delete dispatch transaction from POS_DISTRIBUTION_DETAIL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_DETAIL];
            //System.out.println("DISTRIBUTION OUT 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            
            //---- INSERT DATA ---
            // Insert data dispatch transaction to POS_DISTRIBUTION_MATERIAL_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_MAIN] +
                  " SELECT *" + 
                  " FROM " + PstMatDistribution.TBL_DISTRIBUTION + 
                  " WHERE " + PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND " + PstMatDistribution.fieldNames[PstMatDistribution.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("DISTRIBUTION OUT 3rd : "+sql);
            res = DBHandler.execUpdate(sql);            

            // Insert data dispatch transaction to POS_DISTRIBUTION_DETAIL_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_DETAIL] +
                  " SELECT DSI.*" + 
                  " FROM " + PstMatDistribution.TBL_DISTRIBUTION + " DS" + 
                  " INNER JOIN " + PstMatDistributionDetail.TBL_MAT_DISTRIBUTION_DETAIL + " DSI" + 
                  " ON DS." +  PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_MATERIAL_ID] +
                  " = DSI." + PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID] +
                  " WHERE DS." + PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND DS." + PstMatDistribution.fieldNames[PstMatDistribution.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("DISTRIBUTION OUT 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer dispatch out : " + exc);
        }
        return result;
    }    

    /**
     * Transfer data sale transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferSaleOut(Date transactionDate, long oidLocation)
    {
	boolean result = false;	        
        try
        {                       
            //---- DELETE DATA ---
            // Delete return transaction from CASH_BALANCE_TRANSFER
            String sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BALANCE];
            //System.out.println("SALE OUT 1st : "+sql);
            int res = DBHandler.execUpdate(sql);
            
            // Delete return transaction from CASH_CASHIER_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_CASHIER];
            //System.out.println("SALE OUT 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete return transaction from CASH_BILL_MAIN_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_MAIN];
            //System.out.println("SALE OUT 3rd : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_BILL_DETAIL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_DETAIL];
            //System.out.println("SALE OUT 4th : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_PAYMENT_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_PAYMENT];
            //System.out.println("SALE OUT 5th : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_RETURN_PAYMENT_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_RETURN_PAYMENT];
            //System.out.println("SALE OUT 6th : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_CREDIT_CARD_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_CREDIT_CARD];
            //System.out.println("SALE OUT 7th : "+sql);
            res = DBHandler.execUpdate(sql);            
            
            
            
            //---- INSERT DATA ---
            // Insert data return transaction to CASH_BALANCE_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_BALANCE] +
                  " SELECT SLB.*" + 
                  " FROM " + PstBalance.TBL_BALANCE + " SLB" +
                  " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " SLC" +
                  " ON SLB." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] +
                  " = SLC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                  " INNER JOIN " + PstCashMaster.TBL_CASH_MASTER + " SLM" + 
                  " ON SLC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] +
                  " = SLM." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] +                
                  " WHERE SLC." + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND SLM." + PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("SALE OUT 8th : "+sql);
            res = DBHandler.execUpdate(sql);            

            // Insert data return transaction to CASH_CASHIER_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_CASHIER] +
                  " SELECT DISTINCT SLC.*" + 
                  " FROM " + PstBalance.TBL_BALANCE + " SLB" +
                  " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " SLC" +
                  " ON SLB." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] +
                  " = SLC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                  " INNER JOIN " + PstCashMaster.TBL_CASH_MASTER + " SLM" + 
                  " ON SLC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] +
                  " = SLM." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] +                
                  " WHERE SLC." + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND SLM." + PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID] +
                  " = " + oidLocation;                        
            //System.out.println("SALE OUT 9th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Insert data return transaction to CASH_BILL_MAIN_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_MAIN] +
                  " SELECT *" + 
                  " FROM " + PstBillMain.TBL_CASH_BILL_MAIN +  
                  " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("SALE OUT 10th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Insert data return transaction to CASH_BILL_DETAIL_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_DETAIL] +
                  " SELECT SLI.*" + 
                  " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " SL" + 
                  " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " SLI" + 
                  " ON SL." +  PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                  " = SLI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                  " WHERE SL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND SL." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("SALE OUT 11st : "+sql);
            res = DBHandler.execUpdate(sql);

            // Insert data return transaction to CASH_PAYMENT_TRANSFER 
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_PAYMENT] +
                  " SELECT SLP.*" + 
                  " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " SL" + 
                  " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " SLP" + 
                  " ON SL." +  PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                  " = SLP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] +
                  " WHERE SL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND SL." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("SALE OUT 12nd : "+sql);
            res = DBHandler.execUpdate(sql);

            // Insert data return transaction to CASH_RETURN_PAYMENT_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_RETURN_PAYMENT] +
                  " SELECT SLR.*" + 
                  " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " SL" + 
                  " INNER JOIN " + PstCashReturn.TBL_RETURN + " SLR" + 
                  " ON SL." +  PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                  " = SLR." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] +
                  " WHERE SL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND SL." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                  " = " + oidLocation;            
            //System.out.println("SALE OUT 13rd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Insert data return transaction to CASH_CREDIT_CARD_TRANSFER
            sql = "INSERT INTO " + listTableTransferNames[TBL_TRANSFER_CASH_CREDIT_CARD] +
                  " SELECT SLC.*" + 
                  " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " SL" + 
                  " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " SLP" + 
                  " ON SL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                  " = SLP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] +
                  " INNER JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + " SLC" + 
                  " ON SLP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID] +
                  " = SLC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] +
                  " WHERE SL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                  " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") + "'" +
                  " AND SL." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                  " = " + oidLocation;
            //System.out.println("SALE OUT 14th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer return out : " + exc);
        }
        return result;
    }

    
    // -------------------------- TRANSFER TRANSACTION IN --------------------------
    /**
     * Transfer data transaction out from specified location
     * @param transactionDate
     * @param oidLocation
     * @return  
     * @created by Edhy Putra
     */    
    public static boolean transferTransactionIn(String sourcePath, String targetPath)
    {
	boolean result = false;	                        
        
        copyFiles(sourcePath,targetPath,listTableTransferNames);
        
        result = transferReceiveIn();
        if (result == true)
        {
            result = transferReturnIn();            
        }
        else
        {
            System.out.println("Transfer Receive In fail!!!");
        }                
        
        if (result == true)
        {
            result = transferDistributionIn();
        }
        else
        {
            System.out.println("Transfer Return In fail!!!");
        }

        if (result == true)
        {
            result = transferDispatchIn();
        }
        else
        {
            System.out.println("Transfer Distibution In fail!!!");
        }
        
        if (result == true)
        {
            result = transferSaleIn();
        }
        else
        {
            System.out.println("Transfer Dispatch In fail!!!");
        }
        
        if (result == false)        
        {            
            System.out.println("Transfer Sale In fail!!!");        
        }
        
        return result;
    }
    
    
    /**
     * Transfer data receive transaction out
     * Algoritm : - FETCH data from transfered table and INSERT INTO real table 
     *            - DELETE transfered data from TRANSFERED table 
     * @param transactionDate
     * @param oidLocation
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferReceiveIn()
    {
	boolean result = false;	        
        try
        {                                              
            //---- FETCH and INSERT DATA ---
            // Insert data receive transaction to POS_RECEIVE_MATERIAL
            String sql = "INSERT INTO " + PstMatReceive.TBL_MAT_RECEIVE +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL];            
            //System.out.println("RECEIVE IN 1st : "+sql);
            int res = DBHandler.execUpdate(sql);            

            // Insert data receive transaction to POS_RECEIVE_MATERIAL_ITEM
            sql = "INSERT INTO " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL_ITEM];            
            //System.out.println("RECEIVE IN 2nd : "+sql);
            res = DBHandler.execUpdate(sql);                       
            
            
            //---- DELETE DATA ---
            // Delete receive transaction from POS_RECEIVE_MATERIAL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL];
            //System.out.println("RECEIVE IN 3rd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete receive transaction from POS_RECEIVE_MATERIAL_ITEM_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RECEIVE_MATERIAL_ITEM];
            //System.out.println("RECEIVE IN 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            result = true;
            
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer receive out : " + exc);
        }
        return result;
    }
    

    /**
     * Transfer data return transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferReturnIn()
    {
	boolean result = false;	        
        try
        {                                              
            //---- INSERT DATA ---
            // Insert data return transaction to POS_RETURN_MATERIAL
            String sql = "INSERT INTO " + PstMatReturn.TBL_MAT_RETURN +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL];            
            //System.out.println("RETURN IN 1st : "+sql);
            int res = DBHandler.execUpdate(sql);            

            // Insert data return transaction to POS_RETURN_MATERIAL_ITEM
            sql = "INSERT INTO " + PstMatReturnItem.TBL_MAT_RETURN_ITEM +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL_ITEM];             
            //System.out.println("RETURN IN 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            

            //---- DELETE DATA ---
            // Delete return transaction from POS_RETURN_MATERIAL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL];
            //System.out.println("RETURN IN 3rd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete return transaction from POS_RETURN_MATERIAL_ITEM_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_RETURN_MATERIAL_ITEM];
            //System.out.println("RETURN IN 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer return out : " + exc);
        }
        return result;
    }
    
    /**
     * Transfer data dispatch transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferDispatchIn()
    {
	boolean result = false;	        
        try
        {                       
            //---- INSERT DATA ---
            // Insert data dispatch transaction to POS_DISPATCH_MATERIAL
            String sql = "INSERT INTO " + PstMatDispatch.TBL_DISPATCH +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL];            
            //System.out.println("DISPATCH IN 1st : "+sql);
            int res = DBHandler.execUpdate(sql);            

            // Insert data dispatch transaction to POS_DISPATCH_MATERIAL_ITEM
            sql = "INSERT INTO " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL_ITEM];            
            //System.out.println("DISPATCH IN 2nd : "+sql);
            res = DBHandler.execUpdate(sql);


            //---- DELETE DATA ---
            // Delete dispatch transaction from POS_DISPATCH_MATERIAL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL];
            //System.out.println("DISPATCH IN 3rd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete dispatch transaction from POS_DISPATCH_MATERIAL_ITEM_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISPATCH_MATERIAL_ITEM];
            //System.out.println("DISPATCH IN 4th : "+sql);
            res = DBHandler.execUpdate(sql);

            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer dispatch out : " + exc);
        }
        return result;
    }    
    
    /**
     * Transfer data distribution transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferDistributionIn()
    {
	boolean result = false;	        
        try
        {                       
            //---- INSERT DATA ---
            // Insert data dispatch transaction to POS_DISTRIBUTION_MATERIAL
            String sql = "INSERT INTO " + PstMatDistribution.TBL_DISTRIBUTION +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_MAIN];            
            //System.out.println("DISTRIBUTION IN 1st : "+sql);
            int res = DBHandler.execUpdate(sql);            

            // Insert data dispatch transaction to POS_DISTRIBUTION_DETAIL
            sql = "INSERT INTO " + PstMatDistributionDetail.TBL_MAT_DISTRIBUTION_DETAIL +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_DETAIL];            
            //System.out.println("DISTRIBUTION IN 2nd : "+sql);
            res = DBHandler.execUpdate(sql);

            
            //---- DELETE DATA ---
            // Delete dispatch transaction from POS_DISTRIBUTION_MATERIAL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_MAIN];
            //System.out.println("DISTRIBUTION OUT 3rd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete dispatch transaction from POS_DISTRIBUTION_DETAIL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_DISTRIBUTION_DETAIL];
            //System.out.println("DISTRIBUTION OUT 4th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer dispatch out : " + exc);
        }
        return result;
    }    

    /**
     * Transfer data sale transaction out
     * Algoritm : DELETE from transfer table and then INSERT INTO them
     * @param transactionDate
     * @param oidLocation  
     * @return  
     * @created by Edhy
     */    
    synchronized public static boolean transferSaleIn()
    {
	boolean result = false;	        
        try
        {                       
            //---- INSERT DATA ---
            // Insert data return transaction to CASH_BALANCE
            String sql = "INSERT INTO " + PstBalance.TBL_BALANCE +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BALANCE];            
            //System.out.println("SALE IN 1st : "+sql);
            int res = DBHandler.execUpdate(sql);            

            // Insert data return transaction to CASH_CASHIER
            sql = "INSERT INTO " + PstCashCashier.TBL_CASH_CASHIER +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_CASHIER];                        
            //System.out.println("SALE IN 2nd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Insert data return transaction to CASH_BILL_MAIN
            sql = "INSERT INTO " + PstBillMain.TBL_CASH_BILL_MAIN +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_MAIN];            
            //System.out.println("SALE IN 3rd : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Insert data return transaction to CASH_BILL_DETAIL
            sql = "INSERT INTO " + PstBillDetail.TBL_CASH_BILL_DETAIL +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_DETAIL];            
            //System.out.println("SALE IN 4th : "+sql);
            res = DBHandler.execUpdate(sql);

            // Insert data return transaction to CASH_PAYMENT
            sql = "INSERT INTO " + PstCashPayment.TBL_PAYMENT +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_PAYMENT];
            //System.out.println("SALE IN 5th : "+sql);
            res = DBHandler.execUpdate(sql);

            // Insert data return transaction to CASH_RETURN
            sql = "INSERT INTO " + PstCashReturn.TBL_RETURN +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_RETURN_PAYMENT];
            //System.out.println("SALE IN 6th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Insert data return transaction to CASH_CREDIT_CARD
            sql = "INSERT INTO " + PstCashCreditCard.TBL_CREDIT_CARD +
                  " SELECT *" + 
                  " FROM " + listTableTransferNames[TBL_TRANSFER_CASH_CREDIT_CARD];
            //System.out.println("SALE IN 7th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            
            //---- DELETE DATA ---
            // Delete return transaction from CASH_BALANCE_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BALANCE];
            //System.out.println("SALE IN 8th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete return transaction from CASH_CASHIER_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_CASHIER];
            //System.out.println("SALE IN 9th : "+sql);
            res = DBHandler.execUpdate(sql);
            
            // Delete return transaction from CASH_BILL_MAIN_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_MAIN];
            //System.out.println("SALE IN 10th : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_BILL_DETAIL_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_BILL_DETAIL];
            //System.out.println("SALE IN 11st : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_PAYMENT_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_PAYMENT];
            //System.out.println("SALE IN 12nd : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_RETURN_PAYMENT_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_RETURN_PAYMENT];
            //System.out.println("SALE IN 13rd : "+sql);
            res = DBHandler.execUpdate(sql);

            // Delete return transaction from CASH_CREDIT_CARD_TRANSFER
            sql = "DELETE FROM " + listTableTransferNames[TBL_TRANSFER_CASH_CREDIT_CARD];
            //System.out.println("SALE IN 14th : "+sql);
            res = DBHandler.execUpdate(sql);            
            
            result = true;
        }
        catch(Exception exc)
        {
            System.out.println("Error transfer return out : " + exc);
        }
        return result;
    }    
  

    //----------------------------- Additional ----------------------------------
    public static void copyFiles(String sourceDir, String targetDir, String[] listFileNames){
        String errMsg = "";

        // membuat file untuk selanjutnya diproses 
        File myDir = new File(parsePath(sourceDir));
        File[] myList = myDir.listFiles();
        
        // membuat file untuk selanjutnya dilakukan pengecekan
        File checkDir = new File(parsePath(targetDir));
        File checkDirSource = new File(parsePath(sourceDir)); 

        // mengecek apakah directory source ada ???
        if (checkDirSource.exists()) {
            System.out.println("..::Source OK");
        } else {
            System.out.println(">>Unable to locate source directory, please check source path directory !!!");
            return;
        }
        
        // mengecek apakah directory target ada ???
        if (checkDir.exists()) {
            System.out.println("..::Target OK");
        } else {
            System.out.println(">>Creating target!!!");
            if (checkDir.mkdirs())
                System.out.println("..::Create success");
            else
                System.out.println(">>Create fail");
        }
        
        String fileNames = "";
        int listFileNamesCount = listFileNames.length;
        
        for (int i = 0; i < myList.length; i++)
        {

            File tmp = (File)myList[i];            
            if (tmp.isFile())
            {
                
                fileNames = tmp.getName().toUpperCase();                
                for(int it=0; it<listFileNamesCount; it++)
                {                    
                    
                    if( fileNames.equals((listFileNames[it]+".FRM")) ||
                        fileNames.equals((listFileNames[it]+".MYI")) ||
                        fileNames.equals((listFileNames[it]+".MYD")))
                    {                    
                        
                        System.out.println("copying " + tmp.getName() + "...");
                        File x_file = new File(targetDir + systemPathDelimeter + tmp.getName());
                        FileOutputStream fOut = null;
                        FileInputStream fIn = null;
                        try
                        {
                            fOut = new FileOutputStream(x_file);
                            fIn = new FileInputStream(tmp);
                            int maxbuf = 1024;
                            int rbyte = maxbuf;
                            for (int ib = 0; (ib < Integer.MAX_VALUE) && (rbyte == maxbuf); ib = ib + maxbuf)
                            {
                                byte buffer[] = new byte[maxbuf];
                                rbyte = fIn.read(buffer); 
                                if (rbyte > 0)
                                fOut.write(buffer); 
                            }
                        }
                        catch (IOException e)
                        {
                            System.out.println(e);                    
                            System.out.println(">>Cannot copy " + tmp.getName() + ". " + e.getMessage());                    
                            return;
                        }
                        catch (Exception e)
                        {
                            System.out.println(e);                    
                            System.out.println(">>Cannot copy " + tmp.getName() + ". " + e.getMessage());                    
                            return;
                        }
                        finally
                        {
                            try
                            {
                                fOut.close();
                                fIn.close();
                            }
                            catch (Exception e)
                            {
                                System.out.println(e);
                            }
                        }
                    }
                    /*
                    else
                    {
                        System.out.println(">>File tidak identik !!!");
                    } 
                    */                   
                }                    
            }                       
        }
    }
    
    
    /**
     * Parsing String path file yang ada
     * @param path
     * @return
     * @created by Edhy
     */    
    public static String parsePath(String path){
        if(path!=null && path.length()>0){
           StringTokenizer st=new StringTokenizer(path,"/");
           String newPath="";
         
           while(st.hasMoreTokens())
           {
               newPath=newPath+st.nextToken()+systemPathDelimeter;
           }
           
           newPath=newPath.substring(0,newPath.length()-systemPathDelimeter.length());           
           return newPath;
        }else{
            return "";
        }

    }    
    
    
    //---------------------------------- TESTING ------------------------------------    
    public static void main(String args[]){
        //String sourcePath = "D://mysql/data/pos_smu";                
        //String targetPath = "C://DataTransfer/transaction/transactionout";  
        
        String targetPath = "D://mysql/data/possmu";                
        String sourcePath = "C://DataTransfer/transaction/transactionout";  

        Date startDate = new Date();
        System.out.println("Mulai pada : "+startDate.getTime());
        
        Date transactionDate = new Date(104,1,28);
        long oidLocation = 504404206136894475L;
        //boolean resultOk = transferTransactionOut(transactionDate, oidLocation, sourcePath, targetPath);
        boolean resultOk = transferTransactionIn(sourcePath, targetPath);
        if(resultOk)
        {
            System.out.println("Result OK");
        }    
        else
        {
            System.out.println("Result Fail");
        }    
        
        Date endDate = new Date();
        System.out.println("Mulai pada : "+endDate.getTime());
        
        System.out.println("Lama proses : "+(endDate.getTime()-startDate.getTime()));
    }        
}
