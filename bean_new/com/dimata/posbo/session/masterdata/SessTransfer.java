package com.dimata.posbo.session.masterdata;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.pos.entity.balance.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.pos.entity.masterCashier.*;

public class SessTransfer {
    
    public static final String TBL_TRANSFER_RECEIVE_MATERIAL = "TRANSFER_RECEIVE_MATERIAL";
    public static final String TBL_TRANSFER_RECEIVE_MATERIAL_ITEM = "TRANSFER_RECEIVE_MATERIAL_ITEM";
    public static final String TBL_TRANSFER_RETURN_MATERIAL = "TRANSFER_RETURN_MATERIAL";
    public static final String TBL_TRANSFER_RETURN_MATERIAL_ITEM = "TRANSFER_RETURN_MATERIAL_ITEM";
    public static final String TBL_TRANSFER_DISPATCH_MATERIAL = "TRANSFER_DISPATCH_MATERIAL";
    public static final String TBL_TRANSFER_DISPATCH_MATERIAL_ITEM = "TRANSFER_DISPATCH_MATERIAL_ITEM";
    public static final String TBL_TRANSFER_CASH_BILL_MAIN = "TRANSFER_CASH_BILL_MAIN";
    public static final String TBL_TRANSFER_CASH_BILL_DETAIL = "TRANSFER_CASH_BILL_DETAIL";
    public static final String TBL_TRANSFER_CASH_BALANCE = "TRANSFER_CASH_BALANCE";
    public static final String TBL_TRANSFER_CASH_CASHIER = "TRANSFER_CASH_CASHIER";
    public static final String TBL_TRANSFER_CASH_PAYMENT = "TRANSFER_CASH_PAYMENT";
    public static final String TBL_TRANSFER_CASH_CREDIT_CARD = "TRANSFER_CASH_CREDIT_CARD";
    public static final String TBL_TRANSFER_CASH_RETURN_PAYMENT = "TRANSFER_CASH_RETURN_PAYMENT";
    
    //1. Delete old Receive Transfer Data, Transfer Receive
    //2. Delete old Return Transfer Data, Transfer Return
    //3. Delete old Dispatch Transfer Data, Transfer Dispatch
    //4. Delete old Sale Transfer Data, Transfer Sale
    /**
     * @param transactionDate
     * @param oidLocation
     * @return
     * @update by Edhy
     */
    public static boolean TransferOut(Date transactionDate, long oidLocation) {
        boolean result = false;
        Connection koneksi = null;
        try {
            koneksi = openTransferOut();
        }
        catch(Exception exc) {
            System.out.println("Koneksi Error : " + exc);
        }
        
        result = ReceiveOut(transactionDate,oidLocation,koneksi);
        if (result == true) {
            result = ReturnOut(transactionDate,oidLocation,koneksi);
        }
        else {
            System.out.println("Transfer Receive Out fail!!!");
        }
        
        if (result == true) {
            result = DispatchOut(transactionDate,oidLocation,koneksi);
        }
        else {
            System.out.println("Transfer Return Out fail!!!");
        }
        
        if (result == true) {
            result = SaleOut(transactionDate,oidLocation,koneksi);
        }
        else {
            System.out.println("Transfer Dispatch Out fail!!!");
        }
        
        if (result == false)
            System.out.println("Transfer Sale Out fail!!!");
        
        closeTransferOut(koneksi);
        
        return result;
    }
    
    
    /**
     * @param transactionDate
     * @param oidLocation
     * @param koneksi
     * @return
     * @update by Edhy
     */
    public static boolean ReceiveOut(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Receive Out
            Statement stmt = koneksi.createStatement();
            
            //Delete Receive Material
            String sql = "DELETE FROM " + TBL_TRANSFER_RECEIVE_MATERIAL;
            int res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_RECEIVE_MATERIAL_ITEM;
            res = stmt.executeUpdate(sql);
            
            //Fetch receive by date
            sql = "SELECT RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", RM." +  PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE_CNT] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RETURN_MATERIAL_ID] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " RM" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON RM." +  PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " WHERE RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            " = " + oidLocation +
            " ORDER BY RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
            
            //System.out.println("RECEIVE OUT SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            long oidREC = 0;
            boolean isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                if (oidREC != rs.getLong(1)) {
                    oidREC = rs.getLong(1);
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO " + TBL_TRANSFER_RECEIVE_MATERIAL +
                    " (RECEIVE_MATERIAL_ID"+
                    ", LOCATION_ID" +
                    ", LOCATION_TYPE" +
                    ", RECEIVE_DATE" +
                    ", RECEIVE_STATUS" +
                    ", RECEIVE_SOURCE" +
                    ", SUPPLIER_ID" +
                    ", PURCHASE_ORDER_ID" +
                    ", REMARK" +
                    ", RECEIVE_FROM" +
                    ", REC_CODE" +
                    ", REC_CODE_CNT" +
                    ", DISPATCH_MATERIAL_ID" +
                    ", RETURN_MATERIAL_ID" +
                    ", INVOICE_SUPPLIER" +
                    ", TOTAL_PPN" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    "," + rs.getInt(3) +
                    ",'" + simpledateformat.format((rs.getDate(4)!=null ? rs.getDate(4) : new Date())) +
                    "'," + I_DocStatus.DOCUMENT_STATUS_DRAFT +//rs.getInt(5) +
                    "," + rs.getInt(6) +
                    "," + rs.getLong(7) +
                    "," + rs.getLong(8) +
                    ",'" + (rs.getString(9)!=null ? rs.getString(9) : "") +
                    "'," + rs.getLong(10) +
                    ",'" + (rs.getString(11)!=null ? rs.getString(11) : "") +
                    "'," + rs.getInt(12) +
                    "," + rs.getLong(13) +
                    "," + rs.getLong(14) +
                    ",'" + (rs.getString(15)!=null ? rs.getString(15) : "") +
                    "'," + rs.getDouble(16) +
                    ")";
                    //System.out.println("INSERT RECEIVE OUT SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
                
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_RECEIVE_MATERIAL_ITEM +
                " (RECEIVE_MATERIAL_ITEM_ID" +
                ", RECEIVE_MATERIAL_ID" +
                ", MATERIAL_ID" +
                ", UNIT_ID" +
                ", EXPIRED_DATE" +
                ", COST" +
                ", CURRENCY_ID" +
                ", QTY" +
                ", DISCOUNT" +
                ", TOTAL" +
                ") VALUES " +
                "(" + rs.getLong(17) +
                "," + rs.getLong(18) +
                "," + rs.getLong(19) +
                "," + rs.getLong(20) +
                ",'" + simpledateformat.format((rs.getDate(21)!=null ? rs.getDate(21) : new Date())) +
                "'," + rs.getDouble(22) +
                "," + rs.getLong(23) +
                "," + rs.getDouble(24) +
                "," + rs.getDouble(25) +
                "," + rs.getDouble(26) +
                ")";
                //System.out.println("INSERT RECEIVE ITEM OUT SQL : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer receive out : " + exc);
        }
        return result;
    }
    
    
    /**
     * @param transactionDate
     * @param oidLocation
     * @param koneksi
     * @return
     * @update by Edhy
     */
    public static boolean ReturnOut(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Return Out
            Statement stmt = koneksi.createStatement();
            
            //Delete Return Material
            String sql = "DELETE FROM " + TBL_TRANSFER_RETURN_MATERIAL;
            int res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_RETURN_MATERIAL_ITEM;
            res = stmt.executeUpdate(sql);
            
            //Fetch return by date
            sql = "SELECT RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_PURCHASE_ORDER_ID] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE_CNT] +
            ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] +
            " FROM " + PstMatReturn.TBL_MAT_RETURN + " RM" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " WHERE RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
            " = " + oidLocation +
            " ORDER BY RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID];
            
            //System.out.println("RETURN OUT SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            long oidREC = 0;
            boolean isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                if (oidREC != rs.getLong(1)) {
                    oidREC = rs.getLong(1);
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    
                    /*
                "SELECT RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_PURCHASE_ORDER_ID] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID] +
                ", RM." + PstMatReturn.fieldNames[PstMatReceive.FLD_REMARK] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                ", RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE_CNT] +
                ", RM." + PstMatReceive.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
                     */
                    
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_RETURN_MATERIAL +
                    " (RETURN_MATERIAL_ID"+
                    ", LOCATION_ID" +
                    ", LOCATION_TYPE" +
                    ", RETURN_DATE" +
                    ", RETURN_TO" +
                    ", RETURN_STATUS" +
                    ", RETURN_SOURCE" +
                    ", SUPPLIER_ID" +
                    ", PURCHASE_ORDER_ID" +
                    ", RECEIVE_MATERIAL_ID" +
                    ", REMARK" +
                    ", RETURN_CODE" +
                    ", RETURN_CODE_COUNTER" +
                    ", RETURN_REASON" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    "," + rs.getInt(3) +
                    ",'" + simpledateformat.format((rs.getDate(4)!=null ? rs.getDate(4) : new Date())) +
                    "'," + rs.getLong(5) +
                    "," + I_DocStatus.DOCUMENT_STATUS_DRAFT +//rs.getInt(6) +
                    "," + rs.getInt(7) +
                    "," + rs.getLong(8) +
                    ",'" + rs.getLong(9) +
                    "'," + rs.getLong(10) +
                    ",'" + (rs.getString(11)!=null ? rs.getString(11) : "") +
                    "','" + (rs.getString(12)!=null ? rs.getString(12) : "") +
                    "'," + rs.getInt(13) +
                    ",'" + rs.getInt(14) +
                    "')";
                    //System.out.println("INSERT RECEIVE OUT SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
                
                //Insert item
                sql_x = "INSERT INTO "+ TBL_TRANSFER_RETURN_MATERIAL_ITEM +
                " (RETURN_MATERIAL_ITEM_ID" +
                ", RETURN_MATERIAL_ID" +
                ", MATERIAL_ID" +
                ", UNIT_ID" +
                ", COST" +
                ", CURRENCY_ID" +
                ", QTY" +
                ", TOTAL" +
                ") VALUES " +
                "(" + rs.getLong(15) +
                "," + rs.getLong(16) +
                "," + rs.getLong(17) +
                "," + rs.getLong(18) +
                "," + rs.getDouble(19) +
                "," + rs.getLong(20) +
                "," + rs.getDouble(21) +
                "," + rs.getDouble(22) +
                ")";
                //System.out.println("INSERT RECEIVE ITEM OUT SQL : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer return : " + exc);
        }
        return result;
    }
    
    
    /**
     * @param transactionDate
     * @param oidLocation
     * @param koneksi
     * @return
     * @update by Edhy
     */
    public static boolean DispatchOut(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Dispatch Out
            Statement stmt = koneksi.createStatement();
            
            //Delete Dispatch Material
            String sql = "DELETE FROM " + TBL_TRANSFER_DISPATCH_MATERIAL;
            int res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_DISPATCH_MATERIAL_ITEM;
            res = stmt.executeUpdate(sql);
            
            //Fetch dispatch by date
            sql = "SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_REMARK] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE_COUNTER] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_INVOICE_SUPPLIER] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
            " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
            " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
            " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
            " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
            " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
            " = " + oidLocation +
            " ORDER BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
            
            //System.out.println("DISPATCH OUT SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            long oidREC = 0;
            boolean isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                if (oidREC != rs.getLong(1)) {
                    oidREC = rs.getLong(1);
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_DISPATCH_MATERIAL +
                    " (DISPATCH_MATERIAL_ID"+
                    ", LOCATION_ID" +
                    ", LOCATION_TYPE" +
                    ", DISPATCH_DATE" +
                    ", DISPATCH_STATUS" +
                    ", REMARK" +
                    ", DISPATCH_CODE" +
                    ", DISPATCH_CODE_COUNTER" +
                    ", DISPATCH_TO" +
                    ", INVOICE_SUPPLIER" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    "," + rs.getInt(3) +
                    ",'" + simpledateformat.format((rs.getDate(4)!=null ? rs.getDate(4) : new Date())) +
                    "'," + I_DocStatus.DOCUMENT_STATUS_DRAFT +//rs.getInt(5) +
                    ",'" + (rs.getString(6)!=null ? rs.getString(6) : "") +
                    "','" + (rs.getString(7)!=null ? rs.getString(7) : "") +
                    "'," + rs.getInt(8) +
                    "," + rs.getLong(9) +
                    ",'" + (rs.getString(10)!=null ? rs.getString(10) : "") +
                    "')";
                    //System.out.println("INSERT DISPATCH OUT SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
                
                //Insert item
                sql_x = "INSERT INTO "+ TBL_TRANSFER_DISPATCH_MATERIAL_ITEM +
                " (DISPATCH_MATERIAL_ITEM_ID" +
                ", DISPATCH_MATERIAL_ID" +
                ", MATERIAL_ID" +
                ", UNIT_ID" +
                ", QTY" +
                ") VALUES " +
                "(" + rs.getLong(11) +
                "," + rs.getLong(12) +
                "," + rs.getLong(13) +
                "," + rs.getLong(14) +
                "," + rs.getDouble(15) +
                ")";
                //System.out.println("INSERT DISPATCH OUT ITEM SQL : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer dispatch : " + exc);
        }
        return result;
    }
    
    /**
     * @param transactionDate
     * @param oidLocation
     * @param koneksi
     * @return
     * @update by Edhy
     */
    public static boolean SaleOut(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Sale
            Statement stmt = koneksi.createStatement();
            
            //Delete Sale (Cash_xxx)
            String sql = "DELETE FROM " + TBL_TRANSFER_CASH_BALANCE;
            int res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_CASH_CASHIER;
            res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_CASH_BILL_MAIN;
            res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_CASH_BILL_DETAIL;
            res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_CASH_PAYMENT;
            res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_CASH_RETURN_PAYMENT;
            res = stmt.executeUpdate(sql);
            
            sql = "DELETE FROM " + TBL_TRANSFER_CASH_CREDIT_CARD;
            res = stmt.executeUpdate(sql);
            
            //Fetch sale by date
            sql = "SELECT CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPV_OID] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPV_NAME] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
            ", CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_NAME] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_BALANCE_ID] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_BALANCE_TYPE] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_BALANCE_DATE] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_BALANCE_VALUE] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_SHOULD_VALUE] +
            " FROM (" + PstBalance.TBL_BALANCE + " CB" +
            " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CC" +
            " ON CB." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] +
            " = CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
            ") INNER JOIN " + PstCashMaster.TBL_CASH_MASTER + " CM" +
            " ON CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] +
            " = CM." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] +
            " WHERE CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND CM." + PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID] +
            " = " + oidLocation +
            " ORDER BY CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
            ", CB." + PstBalance.fieldNames[PstBalance.FLD_BALANCE_ID];
            
            //System.out.println("SALE OUT SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            long oidREC = 0;
            boolean isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                if (oidREC != rs.getLong(1)) {
                    oidREC = rs.getLong(1);
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_CASHIER +
                    " (CASH_CASHIER_ID"+
                    ", CASH_MASTER_ID" +
                    ", APP_USER_ID" +
                    ", OPEN_DATE" +
                    ", SPV_OPEN_ID" +
                    ", SPV_OPEN_NAME" +
                    ", SPV_CLOSE_ID" +
                    ", SPV_CLOSE_NAME" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    "," + rs.getLong(3) +
                    ",'" + simpledateformat.format((rs.getDate(4)!=null ? rs.getDate(4) : new Date())) +
                    "'," + rs.getLong(5) +
                    ",'" + (rs.getString(6)!=null ? rs.getString(6) : "") +
                    "'," + rs.getLong(7) +
                    ",'" + (rs.getString(8)!=null ? rs.getString(8) : "") +
                    "')";
                    // System.out.println("INSERT SALE OUT SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
                
                //Insert item
                sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_BALANCE +
                " (CASH_BALANCE_ID" +
                ", CASH_CASHIER_ID" +
                ", CURRENCY_ID" +
                ", TYPE" +
                ", BALANCE_DATE" +
                ", BALANCE_VALUE" +
                ", SHOULD_VALUE" +
                ") VALUES " +
                "(" + rs.getLong(9) +
                "," + rs.getLong(10) +
                "," + rs.getLong(11) +
                "," + rs.getInt(12) +
                ",'" + simpledateformat.format((rs.getDate(13)!=null ? rs.getDate(13) : new Date())) +
                "'," + rs.getDouble(14) +
                "," + rs.getDouble(15) +
                ")";
                System.out.println("INSERT SALE OUT ITEM SQL : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            
            //**************************//
            //    Now it's bill turn   //
            //*************************//
            sql = "SELECT CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_PERCENTAGE] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_PCT] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC_TYPE] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] +
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" +
            " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
            " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
            " = " + oidLocation +
            " ORDER BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID];
            
            //System.out.println("SALE BILL OUT SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            
            oidREC = 0;
            isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                if (oidREC != rs.getLong(1)) {
                    oidREC = rs.getLong(1);
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_BILL_MAIN +
                    " (CASH_BILL_MAIN_ID"+
                    ", CASH_CASHIER_ID" +
                    ", LOCATION_ID" +
                    ", BILL_DATE" +
                    ", BILL_NUMBER" +
                    ", APP_USER_ID" +
                    ", SHIFT_ID" +
                    ", DISC" +
                    ", TAX_PCT" +
                    ", TAX_VALUE" +
                    ", SERVICE_PCT" +
                    ", SERVICE_VALUE" +
                    ", DISC_TYPE" +
                    ", BILL_STATUS" +
                    ", SALES_CODE" +
                    ") VALUES " +
                    "(" + oidREC +
                    "," + rs.getLong(2) +
                    "," + rs.getLong(3) +
                    ",'" + simpledateformat.format((rs.getDate(4)!=null ? rs.getDate(4) : new Date())) +
                    "','" + (rs.getString(5)!=null ? rs.getString(5) : "") +
                    "'," + rs.getLong(6) +
                    "," + rs.getLong(7) +
                    "," + rs.getDouble(8) +
                    "," + rs.getDouble(9) +
                    "," + rs.getDouble(10) +
                    "," + rs.getDouble(11) +
                    "," + rs.getDouble(12) +
                    "," + rs.getInt(13) +
                    "," + I_DocStatus.DOCUMENT_STATUS_DRAFT +//rs.getInt(14) +
                    ",'" + (rs.getString(15)!=null ? rs.getString(15) : "") +
                    "')";
                    //System.out.println("INSERT SALE BILL SQl : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
                
                //Insert item
                sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_BILL_DETAIL +
                " (SALE_ITEM_ID" +
                ", CASH_BILL_MAIN_ID" +
                ", UNIT_ID" +
                ", MATERIAL_ID" +
                ", QTY" +
                ", ITEM_PRICE" +
                ", DISC" +
                ", TOTAL_PRICE" +
                ", SKU" +
                ", ITEM_NAME" +
                ", DISC_TYPE" +
                ", MATERIAL_TYPE" +
                ", COST" +
                ") VALUES " +
                "(" + rs.getLong(16) +
                "," + rs.getLong(17) +
                "," + rs.getLong(18) +
                "," + rs.getLong(19) +
                "," + rs.getDouble(20) +
                "," + rs.getDouble(21) +
                "," + rs.getDouble(22) +
                "," + rs.getDouble(23) +
                ",'" + (rs.getString(24)!=null ? rs.getString(24) : "") +
                "','" + (rs.getString(25)!=null ? rs.getString(25) : "") +
                "'," + rs.getInt(26) +
                "," + rs.getInt(27) +
                "," + rs.getDouble(28) +
                ")";
                //System.out.println("INSERT SALE BILL ITEM SQl : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            
            
            //****************************//
            //    Now it's payment turn   //
            //***************************//
            sql = "SELECT CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] +
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " CP" +
            " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " = CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] +
            " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
            " = " + oidLocation;
            
            //System.out.println("SALE PAYMENT SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            
            oidREC = 0;
            isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                isInsert = true;
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_PAYMENT +
                    " (CASH_PAYMENT_ID"+
                    ", CURRENCY_ID" +
                    ", CASH_BILL_MAIN_ID" +
                    ", PAY_TYPE" +
                    ", RATE" +
                    ", AMOUNT" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    "," + rs.getLong(3) +
                    "," + rs.getInt(4) +
                    "," + rs.getDouble(5) +
                    "," + rs.getDouble(6) +
                    ")";
                    //System.out.println("INSERT SALE PAYMENT SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
            }
            
            //****************************//
            //    Now it's return turn   //
            //***************************//
            sql = "SELECT CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_RETURN_ID] +
            ", CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID] +
            ", CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] +
            ", CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE] +
            ", CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] +
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + PstCashReturn.TBL_RETURN + " CR" +
            " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " = CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] +
            " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
            " = " + oidLocation;
            
            //System.out.println("SALE RETURN SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            
            oidREC = 0;
            isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                isInsert = true;
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_RETURN_PAYMENT +
                    " (RETURN_ID"+
                    ", CURRENCY_ID" +
                    ", CASH_BILL_MAIN_ID" +
                    ", RATE" +
                    ", AMOUNT" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    "," + rs.getLong(3) +
                    "," + rs.getDouble(4) +
                    "," + rs.getDouble(5) +
                    ")";
                    //System.out.println("INSERT SALE RETURN SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
            }
            
            //*******************************//
            //    Now it's credit card turn //
            //******************************//
            sql = "SELECT CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID] +
            ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] +
            ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME] +
            ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER] +
            ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_EXPIRED_DATE] +
            ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME] +
            " FROM (" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " CP" +
            " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " = CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] +
            " ) INNER JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + " CC" +
            " ON CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID] +
            " = CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] +
            " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
            " = '" + Formater.formatDate(transactionDate, "yyyy-MM-dd") +
            "' AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
            " = " + oidLocation;
            
            //System.out.println("SALE CREDIT CARD SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            
            oidREC = 0;
            isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                isInsert = true;
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ TBL_TRANSFER_CASH_CREDIT_CARD +
                    " (CC_ID"+
                    ", CASH_PAYMENT_ID" +
                    ", CC_NAME" +
                    ", CC_NUMBER" +
                    ", EXPIRED_DATE" +
                    ", HOLDER_NAME" +
                    ") VALUES " +
                    "(" + rs.getLong(1) +
                    "," + rs.getLong(2) +
                    ",'" + (rs.getString(3)!=null ? rs.getString(3) : "") +
                    "','" + (rs.getString(4)!=null ? rs.getString(4) : "") +
                    "','" + simpledateformat.format((rs.getDate(5)!=null ? rs.getDate(5) : new Date())) +
                    "','" + (rs.getString(6)!=null ? rs.getString(6) : "") +
                    "')";
                    //System.out.println("INSERT SALE CC SQL : "+sql_x);
                    res = stmt.executeUpdate(sql_x);
                }
            }
            rs.close();
            result = true;
        }
        catch(Exception exc) {
            //System.out.println("Error transfer sale : " + exc);
        }
        return result;
    }
    
    
    //Transfer In Section
    public static boolean TransferIn(Date transactionDate, long oidLocation) {
        boolean result = false;
        Connection koneksi = null;
        try {
            koneksi = openTransferIn();
        }
        catch(Exception exc) {
            System.out.println("Error : " + exc);
        }
        
        result = ReceiveIn(transactionDate,oidLocation,koneksi);
        if (result == true) {
            result = ReturnIn(transactionDate,oidLocation,koneksi);
        }
        else {
            System.out.println("Transfer Receive In fail!!!");
        }
        
        if (result == true) {
            result = DispatchIn(transactionDate,oidLocation,koneksi);
        }
        else {
            System.out.println("Transfer Return In fail!!!");
        }
        
        if (result == true) {
            result = SaleIn(transactionDate,oidLocation,koneksi);
        }
        else {
            System.out.println("Transfer Dispatch In fail!!!");
        }
        
        
        if (result == false)
            System.out.println("Transfer Sale In fail!!!");
        
        closeTransferIn(koneksi);
        return result;
    }
    
    
    public static boolean ReceiveIn(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        try {
            int res = 0;
            //Fetch receive by date
            String sql = "SELECT RM.RECEIVE_MATERIAL_ID" +
            ", RM.LOCATION_ID" +
            ", RM.LOCATION_TYPE" +
            ", RM.RECEIVE_DATE" +
            ", RM.RECEIVE_STATUS" +
            ", RM.RECEIVE_SOURCE" +
            ", RM.SUPPLIER_ID" +
            ", RM.PURCHASE_ORDER_ID" +
            ", RM.REMARK" +
            ", RM.RECEIVE_FROM" +
            ", RM.REC_CODE" +
            ", RM.REC_CODE_CNT" +
            ", RM.DISPATCH_MATERIAL_ID" +
            ", RM.RETURN_MATERIAL_ID" +
            ", RM.INVOICE_SUPPLIER" +
            ", RM.TOTAL_PPN" +
            ", RMI.RECEIVE_MATERIAL_ITEM_ID" +
            ", RMI.RECEIVE_MATERIAL_ID" +
            ", RMI.MATERIAL_ID" +
            ", RMI.UNIT_ID" +
            ", RMI.EXPIRED_DATE" +
            ", RMI.COST" +
            ", RMI.CURRENCY_ID" +
            ", RMI.QTY" +
            ", RMI.DISCOUNT" +
            ", RMI.TOTAL" +
            " FROM " + TBL_TRANSFER_RECEIVE_MATERIAL + " RM" +
            " INNER JOIN " + TBL_TRANSFER_RECEIVE_MATERIAL_ITEM + " RMI" +
            " ON RM.RECEIVE_MATERIAL_ID" +
            " = RMI.RECEIVE_MATERIAL_ID" +
            " WHERE RM.LOCATION_ID" +
            " = '" + oidLocation + "'";
            
            //System.out.println("SQL RECEIVE IN : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            String oidREC = "";
            String oidRECOld = "";
            boolean isInsert = true;
            while(rst.next()) {
                boolean exist = false;
                //Fetch all we needed
                oidRECOld = rst.getString(1);
                if (oidREC.equals(oidRECOld) == false) {
                    oidREC = oidRECOld;
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                //Check whether main is needed to insert or not
                String sql_x = "";
                if (isInsert == true) {
                    exist = PstMatReceive.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstMatReceive.TBL_MAT_RECEIVE +
                        " (RECEIVE_MATERIAL_ID"+
                        ", LOCATION_ID" +
                        ", LOCATION_TYPE" +
                        ", RECEIVE_DATE" +
                        ", RECEIVE_STATUS" +
                        ", RECEIVE_SOURCE" +
                        ", SUPPLIER_ID" +
                        ", PURCHASE_ORDER_ID" +
                        ", REMARK" +
                        ", RECEIVE_FROM" +
                        ", REC_CODE" +
                        ", REC_CODE_CNT" +
                        ", DISPATCH_MATERIAL_ID" +
                        ", RETURN_MATERIAL_ID" +
                        ", INVOICE_SUPPLIER" +
                        ", TOTAL_PPN" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rst.getString(2) +
                        "," + rst.getInt(3) +
                        ",'" + rst.getString(4) +
                        "'," + rst.getInt(5) +
                        "," + rst.getInt(6) +
                        "," + rst.getString(7) +
                        "," + rst.getLong(8) +
                        ",'" + rst.getString(9) +
                        "'," + rst.getString(10) +
                        ",'" + rst.getString(11) +
                        "'," + rst.getInt(12) +
                        "," + rst.getString(13) +
                        "," + rst.getString(14) +
                        ",'" + rst.getString(15) +
                        "'," + rst.getDouble(16) +
                        ")";
                        //System.out.println("INSERT SQL RECEIVE IN : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
                //Insert item
                String oidItem = rst.getString(17);
                exist = PstMatReceiveItem.checkOID(Long.parseLong(oidItem));
                if (exist == false) {
                    sql_x = "INSERT INTO "+ PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM +
                    " (RECEIVE_MATERIAL_ITEM_ID" +
                    ", RECEIVE_MATERIAL_ID" +
                    ", MATERIAL_ID" +
                    ", UNIT_ID" +
                    ", EXPIRED_DATE" +
                    ", COST" +
                    ", CURRENCY_ID" +
                    ", QTY" +
                    ", DISCOUNT" +
                    ", TOTAL" +
                    ") VALUES " +
                    "(" + oidItem +
                    "," + rst.getString(18) +
                    "," + rst.getString(19) +
                    "," + rst.getString(20) +
                    ",'" + rst.getString(21) +
                    "'," + rst.getDouble(22) +
                    "," + rst.getString(23) +
                    "," + rst.getDouble(24) +
                    "," + rst.getDouble(25) +
                    "," + rst.getDouble(26) +
                    ")";
                    //System.out.println("INSERT SQL RECEIVE ITEM IN : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer receive in : " + exc);
        }
        return result;
    }
    
    
    public static boolean ReturnIn(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        try {
            int res = 0;
            String sql = "SELECT RM.RETURN_MATERIAL_ID" +
            ", RM.LOCATION_ID" +
            ", RM.LOCATION_TYPE" +
            ", RM.RETURN_DATE" +
            ", RM.RETURN_TO" +
            ", RM.RETURN_STATUS" +
            ", RM.RETURN_SOURCE" +
            ", RM.SUPPLIER_ID" +
            ", RM.PURCHASE_ORDER_ID" +
            ", RM.RECEIVE_MATERIAL_ID" +
            ", RM.REMARK" +
            ", RM.RETURN_CODE" +
            ", RM.RETURN_CODE_COUNTER" +
            ", RM.RETURN_REASON" +
            ", RMI.RETURN_MATERIAL_ITEM_ID" +
            ", RMI.RETURN_MATERIAL_ID" +
            ", RMI.MATERIAL_ID" +
            ", RMI.UNIT_ID" +
            ", RMI.COST" +
            ", RMI.CURRENCY_ID" +
            ", RMI.QTY" +
            ", RMI.TOTAL" +
            " FROM " + TBL_TRANSFER_RETURN_MATERIAL + " RM" +
            " INNER JOIN " + TBL_TRANSFER_RETURN_MATERIAL_ITEM + " RMI" +
            " ON RM.RETURN_MATERIAL_ID" +
            " = RMI.RETURN_MATERIAL_ID" +
            " WHERE RM.LOCATION_ID" +
            " = '" + oidLocation + "'" ;
            
            //System.out.println("RETURN IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String oidREC = "";
            String oidRECOld = "";
            boolean isInsert = true;
            while(rs.next()) {
                boolean exist = false;
                //Fetch all we needed
                oidRECOld = rs.getString(1);
                if (oidREC.equals(oidRECOld) == false) {
                    oidREC = oidRECOld;
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                //Check whether main is needed to insert or not
                String sql_x = "";
                if (isInsert == true) {
                    exist = PstMatReturn.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstMatReturn.TBL_MAT_RETURN +
                        " (RETURN_MATERIAL_ID"+
                        ", LOCATION_ID" +
                        ", LOCATION_TYPE" +
                        ", RETURN_DATE" +
                        ", RETURN_TO" +
                        ", RETURN_STATUS" +
                        ", RETURN_SOURCE" +
                        ", SUPPLIER_ID" +
                        ", PURCHASE_ORDER_ID" +
                        ", RECEIVE_MATERIAL_ID" +
                        ", REMARK" +
                        ", RETURN_CODE" +
                        ", RETURN_CODE_COUNTER" +
                        ", RETURN_REASON" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rs.getString(2) +
                        "," + rs.getInt(3) +
                        ",'" + rs.getString(4) +
                        "'," + rs.getString(5) +
                        "," + rs.getInt(6) +
                        "," + rs.getInt(7) +
                        "," + rs.getString(8) +
                        ",'" + rs.getString(9) +
                        "'," + rs.getString(10) +
                        ",'" + rs.getString(11) +
                        "','" + rs.getString(12) +
                        "'," + rs.getInt(13) +
                        "," + rs.getInt(14) +
                        ")";
                        //System.out.println("INSERT RETURN OUT SQL : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
                
                //Insert item
                String oidItem = rs.getString(15);
                exist = PstMatReturnItem.checkOID(Long.parseLong(oidItem));
                if (exist == false) {
                    sql_x = "INSERT INTO "+ PstMatReturnItem.TBL_MAT_RETURN_ITEM +
                    " (RETURN_MATERIAL_ITEM_ID" +
                    ", RETURN_MATERIAL_ID" +
                    ", MATERIAL_ID" +
                    ", UNIT_ID" +
                    ", COST" +
                    ", CURRENCY_ID" +
                    ", QTY" +
                    ", TOTAL" +
                    ") VALUES " +
                    "(" + oidItem +
                    "," + rs.getString(16) +
                    "," + rs.getString(17) +
                    "," + rs.getString(18) +
                    "," + rs.getDouble(19) +
                    "," + rs.getString(20) +
                    "," + rs.getDouble(21) +
                    "," + rs.getDouble(22) +
                    ")";
                    //System.out.println("INSERT RETURN ITEM OUT SQL : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer return in : " + exc);
        }
        return result;
    }
    
    
    public static boolean DispatchIn(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        DBResultSet dbrs = null;
        try {
            int res = 0;
            //Fetch dispatch by date
            String sql = "SELECT DF.DISPATCH_MATERIAL_ID" +
            ", DF.LOCATION_ID" +
            ", DF.LOCATION_TYPE" +
            ", DF.DISPATCH_DATE" +
            ", DF.DISPATCH_STATUS" +
            ", DF.REMARK" +
            ", DF.DISPATCH_CODE" +
            ", DF.DISPATCH_CODE_COUNTER" +
            ", DF.DISPATCH_TO" +
            ", DF.INVOICE_SUPPLIER" +
            ", DFI.DISPATCH_MATERIAL_ITEM_ID" +
            ", DFI.DISPATCH_MATERIAL_ID" +
            ", DFI.MATERIAL_ID" +
            ", DFI.UNIT_ID" +
            ", DFI.QTY" +
            " FROM " + TBL_TRANSFER_DISPATCH_MATERIAL + " DF" +
            " INNER JOIN " + TBL_TRANSFER_DISPATCH_MATERIAL_ITEM + " DFI" +
            " ON DF.DISPATCH_MATERIAL_ID" +
            " = DFI.DISPATCH_MATERIAL_ID" +
            " WHERE DF.LOCATION_ID" +
            " = '" + oidLocation + "'";
            
            //System.out.println("DISPATCH IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String oidREC = "";
            String oidRECOld = "";
            
            boolean isInsert = true;
            while(rs.next()) {
                boolean exist = false;
                //Fetch all we needed
                oidRECOld = rs.getString(1);
                if (oidREC.equals(oidRECOld) == false) {
                    oidREC = oidRECOld;
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                //Check whether main is needed to insert or not
                String sql_x = "";
                if (isInsert == true) {
                    exist = PstMatDispatch.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstMatDispatch.TBL_DISPATCH +
                        " (DISPATCH_MATERIAL_ID"+
                        ", LOCATION_ID" +
                        ", LOCATION_TYPE" +
                        ", DISPATCH_DATE" +
                        ", DISPATCH_STATUS" +
                        ", REMARK" +
                        ", DISPATCH_CODE" +
                        ", DISPATCH_CODE_COUNTER" +
                        ", DISPATCH_TO" +
                        ", INVOICE_SUPPLIER" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rs.getString(2) +
                        "," + rs.getInt(3) +
                        ",'" + rs.getString(4) +
                        "'," + rs.getInt(5) +
                        ",'" + rs.getString(6) +
                        "','" + rs.getString(7) +
                        "'," + rs.getInt(8) +
                        "," + rs.getString(9) +
                        ",'" + rs.getString(10) +
                        "')";
                        //System.out.println("INSERT DISPATCH IN SQL : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
                
                //Insert item
                String oidItem = rs.getString(11);
                exist = PstMatDispatchItem.checkOID(Long.parseLong(oidItem));
                if (exist == false) {
                    sql_x = "INSERT INTO "+ PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM +
                    " (DISPATCH_MATERIAL_ITEM_ID" +
                    ", DISPATCH_MATERIAL_ID" +
                    ", MATERIAL_ID" +
                    ", UNIT_ID" +
                    ", QTY" +
                    ") VALUES " +
                    "(" + oidItem +
                    "," + rs.getString(12) +
                    "," + rs.getString(13) +
                    "," + rs.getString(14) +
                    "," + rs.getDouble(15) +
                    ")";
                    //System.out.println("INSERT DISPATCH ITEM IN SQL : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer dispatch in : " + exc);
        }
        return result;
    }
    
    public static boolean SaleIn(Date transactionDate, long oidLocation, Connection koneksi) {
        boolean result = false;
        DBResultSet dbrs = null;
        try {
            int res = 0;
            String sql = "SELECT CC.CASH_CASHIER_ID" +
            ", CC.CASH_MASTER_ID" +
            ", CC.APP_USER_ID" +
            ", CC.OPEN_DATE" +
            ", CC.SPV_OPEN_ID" +
            ", CC.SPV_OPEN_NAME" +
            ", CC.SPV_CLOSE_ID" +
            ", CC.SPV_CLOSE_NAME" +
            ", CB.CASH_BALANCE_ID" +
            ", CB.CASH_CASHIER_ID" +
            ", CB.CURRENCY_ID" +
            ", CB.TYPE" +
            ", CB.BALANCE_DATE" +
            ", CB.BALANCE_VALUE" +
            ", CB.SHOULD_VALUE" +
            " FROM " + TBL_TRANSFER_CASH_BALANCE + " CB" +
            " INNER JOIN " + TBL_TRANSFER_CASH_CASHIER + " CC" +
            " ON CB.CASH_CASHIER_ID" +
            " = CC.CASH_CASHIER_ID";
            
            //System.out.println("CASH MASTER IN : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String oidREC = "";
            String oidRECOld = "";
            
            boolean isInsert = true;
            while(rs.next()) {
                boolean exist = false;
                //Fetch all we needed
                oidRECOld = rs.getString(1);
                if (oidREC.equals(oidRECOld) == false) {
                    oidREC = oidRECOld;
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                if (isInsert == true) {
                    exist = PstCashCashier.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstCashCashier.TBL_CASH_CASHIER +
                        " (CASH_CASHIER_ID"+
                        ", CASH_MASTER_ID" +
                        ", APP_USER_ID" +
                        ", OPEN_DATE" +
                        ", SPV_OPEN_ID" +
                        ", SPV_OPEN_NAME" +
                        ", SPV_CLOSE_ID" +
                        ", SPV_CLOSE_NAME" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rs.getString(2) +
                        "," + rs.getString(3) +
                        ",'" + rs.getString(4) +
                        "'," + rs.getString(5) +
                        ",'" + rs.getString(6) +
                        "'," + rs.getString(7) +
                        ",'" + rs.getString(8) +
                        "')";
                        //System.out.println("INSERT CASH MASTER IN : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
                
                //Insert item
                String oidItem = rs.getString(9);
                exist = PstBalance.checkOID(Long.parseLong(oidItem));
                if (exist == false) {
                    sql_x = "INSERT INTO "+ PstBalance.TBL_BALANCE +
                    " (CASH_BALANCE_ID" +
                    ", CASH_CASHIER_ID" +
                    ", CURRENCY_ID" +
                    ", TYPE" +
                    ", BALANCE_DATE" +
                    ", BALANCE_VALUE" +
                    ", SHOULD_VALUE" +
                    ") VALUES " +
                    "(" + oidItem +
                    "," + rs.getString(10) +
                    "," + rs.getString(11) +
                    "," + rs.getInt(12) +
                    ",'" + rs.getString(13) +
                    "'," + rs.getDouble(14) +
                    "," + rs.getDouble(15) +
                    ")";
                    //System.out.println("INSERT CASH MASTER ITEM IN : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            
            //**************************//
            //    Now it's bill turn   //
            //*************************//
            sql = "SELECT CBM.CASH_BILL_MAIN_ID" +
            ", CBM.CASH_CASHIER_ID" +
            ", CBM.LOCATION_ID" +
            ", CBM.BILL_DATE" +
            ", CBM.BILL_NUMBER" +
            ", CBM.APP_USER_ID" +
            ", CBM.SHIFT_ID" +
            ", CBM.DISC" +
            ", CBM.TAX_PCT" +
            ", CBM.TAX_VALUE" +
            ", CBM.SERVICE_PCT" +
            ", CBM.SERVICE_VALUE" +
            ", CBM.DISC_TYPE" +
            ", CBM.BILL_STATUS" +
            ", CBM.SALES_CODE" +
            ", CBD.SALE_ITEM_ID" +
            ", CBD.CASH_BILL_MAIN_ID" +
            ", CBD.UNIT_ID" +
            ", CBD.MATERIAL_ID" +
            ", CBD.QTY" +
            ", CBD.ITEM_PRICE" +
            ", CBD.DISC" +
            ", CBD.TOTAL_PRICE" +
            ", CBD.SKU" +
            ", CBD.ITEM_NAME" +
            ", CBD.DISC_TYPE" +
            ", CBD.MATERIAL_TYPE" +
            ", CBD.COST" +
            " FROM " + TBL_TRANSFER_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + TBL_TRANSFER_CASH_BILL_DETAIL + " CBD" +
            " ON CBM.CASH_BILL_MAIN_ID" +
            " = CBD.CASH_BILL_MAIN_ID" +
            " WHERE CBM.LOCATION_ID" +
            " = '" + oidLocation + "'";
            
            //System.out.println("CASH BILL IN : "+sql);
            stmt = koneksi.createStatement();
            rs = stmt.executeQuery(sql);
            oidREC = "";
            oidRECOld = "";
            
            isInsert = true;
            while(rs.next()) {
                boolean exist = false;
                //Fetch all we needed
                oidRECOld = rs.getString(1);
                if (oidREC.equals(oidRECOld) == false) {
                    oidREC = oidRECOld;
                    isInsert = true;
                }
                else {
                    isInsert = false;
                }
                
                //Check whether main is needed to insert or not
                String sql_x = "";
                if (isInsert == true) {
                    exist = PstBillMain.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstBillMain.TBL_CASH_BILL_MAIN +
                        " (CASH_BILL_MAIN_ID"+
                        ", CASH_CASHIER_ID" +
                        ", LOCATION_ID" +
                        ", BILL_DATE" +
                        ", BILL_NUMBER" +
                        ", APP_USER_ID" +
                        ", SHIFT_ID" +
                        ", DISC" +
                        ", TAX_PCT" +
                        ", TAX_VALUE" +
                        ", SERVICE_PCT" +
                        ", SERVICE_VALUE" +
                        ", DISC_TYPE" +
                        ", BILL_STATUS" +
                        ", SALES_CODE" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rs.getString(2) +
                        "," + rs.getString(3) +
                        ",'" + rs.getString(4) +
                        "','" + rs.getString(5) +
                        "'," + rs.getString(6) +
                        "," + rs.getString(7) +
                        "," + rs.getDouble(8) +
                        "," + rs.getDouble(9) +
                        "," + rs.getDouble(10) +
                        "," + rs.getDouble(11) +
                        "," + rs.getDouble(12) +
                        "," + rs.getInt(13) +
                        "," + rs.getInt(14) +
                        ",'" + rs.getString(15) +
                        "')";
                        //System.out.println("INSERT CASH BILL IN : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
                
                //Insert item
                String oidItem = rs.getString(16);
                exist = PstBillDetail.checkOID(Long.parseLong(oidItem));
                if (exist == false) {
                    sql_x = "INSERT INTO "+ PstBillDetail.TBL_CASH_BILL_DETAIL +
                    " (SALE_ITEM_ID" +
                    ", CASH_BILL_MAIN_ID" +
                    ", UNIT_ID" +
                    ", MATERIAL_ID" +
                    ", QTY" +
                    ", ITEM_PRICE" +
                    ", DISC" +
                    ", TOTAL_PRICE" +
                    ", SKU" +
                    ", ITEM_NAME" +
                    ", DISC_TYPE" +
                    ", MATERIAL_TYPE" +
                    ", COST" +
                    ") VALUES " +
                    "(" + oidItem +
                    "," + rs.getString(17) +
                    "," + rs.getString(18) +
                    "," + rs.getString(19) +
                    "," + rs.getDouble(20) +
                    "," + rs.getDouble(21) +
                    "," + rs.getDouble(22) +
                    "," + rs.getDouble(23) +
                    ",'" + rs.getString(24) +
                    "','" + rs.getString(25) +
                    "'," + rs.getInt(26) +
                    "," + rs.getInt(27) +
                    "," + rs.getDouble(28) +
                    ")";
                    //System.out.println("INSERT CASH BILL ITEM IN : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            
            //****************************//
            //    Now it's payment turn   //
            //***************************//
            sql = "SELECT CP.CASH_PAYMENT_ID" +
            ", CP.CURRENCY_ID" +
            ", CP.CASH_BILL_MAIN_ID" +
            ", CP.PAY_TYPE" +
            ", CP.RATE" +
            ", CP.AMOUNT" +
            " FROM " + TBL_TRANSFER_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + TBL_TRANSFER_CASH_PAYMENT + " CP" +
            " ON CBM.CASH_BILL_MAIN_ID" +
            " = CP.CASH_BILL_MAIN_ID" +
            " WHERE CBM.LOCATION_ID" +
            " = '" + oidLocation + "'";
            
            //System.out.println("CASH PAYMENT IN : "+sql);
            stmt = koneksi.createStatement();
            rs = stmt.executeQuery(sql);
            
            isInsert = true;
            while(rs.next()) {
                boolean exist = false;
                //Fetch all we needed
                isInsert = true;
                String sql_x = "";
                if (isInsert == true) {
                    oidREC = rs.getString(1);
                    exist = PstCashPayment.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstCashPayment.TBL_PAYMENT +
                        " (CASH_PAYMENT_ID"+
                        ", CURRENCY_ID" +
                        ", CASH_BILL_MAIN_ID" +
                        ", PAY_TYPE" +
                        ", RATE" +
                        ", AMOUNT" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rs.getString(2) +
                        "," + rs.getString(3) +
                        "," + rs.getInt(4) +
                        "," + rs.getDouble(5) +
                        "," + rs.getDouble(6) +
                        ")";
                        //System.out.println("INSERT CASH BILL IN : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
            }
            
            //****************************//
            //    Now it's return turn   //
            //***************************//
            sql = "SELECT CR.RETURN_ID" +
            ", CR.CURRENCY_ID" +
            ", CR.CASH_BILL_MAIN_ID" +
            ", CR.RATE" +
            ", CR.AMOUNT" +
            " FROM " + TBL_TRANSFER_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + TBL_TRANSFER_CASH_RETURN_PAYMENT + " CR" +
            " ON CBM.CASH_BILL_MAIN_ID" +
            " = CR.CASH_BILL_MAIN_ID" +
            " WHERE CBM.LOCATION_ID" +
            " = '" + oidLocation + "'";
            
            //System.out.println("CASH RETURN IN : "+sql);
            stmt = koneksi.createStatement();
            rs = stmt.executeQuery(sql);
            
            isInsert = true;
            while(rs.next()) {
                boolean exist = false;
                //Fetch all we needed
                isInsert = true;
                String sql_x = "";
                if (isInsert == true) {
                    oidREC = rs.getString(1);
                    exist = PstCashReturn.checkOID(Long.parseLong(oidREC));
                    if (exist == false) {
                        sql_x = "INSERT INTO "+ PstCashReturn.TBL_RETURN +
                        " (RETURN_ID"+
                        ", CURRENCY_ID" +
                        ", CASH_BILL_MAIN_ID" +
                        ", RATE" +
                        ", AMOUNT" +
                        ") VALUES " +
                        "(" + oidREC +
                        "," + rs.getString(2) +
                        "," + rs.getString(3) +
                        "," + rs.getDouble(4) +
                        "," + rs.getDouble(5) +
                        ")";
                        //System.out.println("INSERT CASH RETURN IN : "+sql_x);
                        res = DBHandler.execUpdate(sql_x);
                    }
                }
            }
            
            //*******************************//
            //    Now it's credit card turn //
            //******************************//
            sql = "SELECT CC.CC_ID" +
            ", CC.CASH_PAYMENT_ID" +
            ", CC.CC_NAME" +
            ", CC.CC_NUMBER" +
            ", CC.EXPIRED_DATE" +
            ", CC.HOLDER_NAME" +
            " FROM (" + TBL_TRANSFER_CASH_BILL_MAIN + " CBM" +
            " INNER JOIN " + TBL_TRANSFER_CASH_PAYMENT + " CP" +
            " ON CBM.CASH_BILL_MAIN_ID" +
            " = CP.CASH_BILL_MAIN_ID" +
            " ) INNER JOIN " + TBL_TRANSFER_CASH_CREDIT_CARD + " CC" +
            " ON CP.CASH_PAYMENT_ID" +
            " = CC.CASH_PAYMENT_ID" +
            " WHERE CBM.LOCATION_ID" +
            " = '" + oidLocation + "'";
            
            //System.out.println("CASH CC IN : "+sql);
            stmt = koneksi.createStatement();
            rs = stmt.executeQuery(sql);
            
            isInsert = true;
            while(rs.next()) {
                //Fetch all we needed
                isInsert = true;
                String sql_x = "";
                if (isInsert == true) {
                    sql_x = "INSERT INTO "+ PstCashCreditCard.TBL_CREDIT_CARD +
                    " (CC_ID"+
                    ", CASH_PAYMENT_ID" +
                    ", CC_NAME" +
                    ", CC_NUMBER" +
                    ", EXPIRED_DATE" +
                    ", HOLDER_NAME" +
                    ") VALUES " +
                    "(" + rs.getString(1) +
                    "," + rs.getString(2) +
                    ",'" + rs.getString(3) +
                    "','" + rs.getString(4) +
                    "','" + rs.getString(5) +
                    "','" + rs.getString(6) +
                    "')";
                    //System.out.println("INSERT CASH CC IN : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            rs.close();
            result = true;
        }
        catch(Exception exc) {
            System.out.println("Error transfer sale in : " + exc);
        }
        return result;
    }
    
    //Open local connection untuk transfer data transaksi keluar
    public static Connection openTransferOut() {
        Connection koneksi = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            koneksi = DriverManager.getConnection("jdbc:odbc:DataOUT");
        }
        catch(Exception exc) {
            System.out.println("Error open connection : " + exc);
        }
        return koneksi;
    }
    
    //Tutup local connection untuk transfer data transaksi keluar
    public static void closeTransferOut(Connection koneksi) {
        try {
            koneksi.close();
        }
        catch(Exception exc) {
            System.out.println("Error close connection : " + exc);
        }
    }
    
    //Open local connection untuk transfer data transaksi masuk
    public static Connection openTransferIn() {
        Connection koneksi = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            koneksi = DriverManager.getConnection("jdbc:odbc:DataIN");
        }
        catch(Exception exc) {
            System.out.println("Error open connection : " + exc);
        }
        return koneksi;
    }
    
    //Tutup local connection untuk transfer data transaksi masuk
    public static void closeTransferIn(Connection koneksi) {
        try {
            koneksi.close();
        }
        catch(Exception exc) {
            System.out.println("Error close connection : " + exc);
        }
    }
    
    //From formatted string we compose into Date MySQL
    public static Date composeDate(String myDate) {
        Date hasil = new Date();
        
        return hasil;
    }
    
    
    public static void main(String args[]){
        Date today = new Date(104,1,28);
        //boolean result = TransferOut(today, 504404206136894475L);
        boolean result = TransferIn(today, 504404206136894475L);
        //System.out.println("Transfer Data Out Finish :)");
    }
    
}
