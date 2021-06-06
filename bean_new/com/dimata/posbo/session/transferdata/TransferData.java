/*
 * TransMasterdata.java
 *
 * Created on December 21, 2004, 12:42 PM
 */

package com.dimata.posbo.session.transferdata;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.PstMemberPoin;
import com.dimata.util.Formater;
import com.dimata.util.log.TXTLogger;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.balance.PstBalance;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.Date;
import java.util.Hashtable;
import java.io.File;

public class TransferData {

    public TransferData() {
        setTypeDbField();
        setHashFieldNumeric();
    }

    public static final int TYPE_STRING = 0;
    public static final int TYPE_NUMERIC = 1;
    public static final int TYPE_DATE = 2;
    public static final int TYPE_DATETIME = 3;

    public static final int TYPE_NUMERIC_LONG = 0;
    public static final int TYPE_NUMERIC_INT = 1;
    public static final int TYPE_NUMERIC_DOUBLE = 2;

    public static Hashtable hashtable = new Hashtable();
    public static Hashtable hashFieldNumeric = new Hashtable();

    public static void setHashFieldNumeric() {
        hashFieldNumeric.put("bigint", "0");
        hashFieldNumeric.put("int", "1");
        hashFieldNumeric.put("double", "2");
        hashFieldNumeric.put("decimal", "2");
        hashFieldNumeric.put("float", "2");
        hashFieldNumeric.put("smallint", "1");
    }

    public static void setTypeDbField() {
        hashtable.put("bigint", "1");
        hashtable.put("smallint", "1");
        hashtable.put("varchar", "0");
        hashtable.put("int", "1");
        hashtable.put("decimal", "1");
        hashtable.put("char", "0");
        hashtable.put("double", "1");
        hashtable.put("float", "1");
        hashtable.put("date", "2");
        hashtable.put("datetime", "3");
        hashtable.put("timestamp", "2");
        hashtable.put("text", "0");
    }

    public int getTypeDbField(String strtype){
        String str = (String)hashtable.get(strtype);
        return Integer.parseInt(str);
    }


    public static String getString(String val) {
        return "'" + val + "'";
    }

    public static int getNumeric(int val) {
        return val;
    }

    public static long getNumeric(long val) {
        return val;
    }

    public static double getNumeric(double val) {
        return val;
    }

    public static String getStrDate(Date val) {
        if(val!=null){
            String str = "";
            str = Formater.formatDate(val,"yyyy-MM-dd");
            str = str +" "+Formater.formatDate(val,"kk:mm:ss");
            return "'" + str + "'";
        }
        return "'null'";
    }


    public String[] showFieldType(String tblname) {
        DBResultSet dbrs = null;
        String[] result = {""};
        try {
            String sql = "describe " + tblname + ";";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int i = 1;
            String str = "";
            String strfield = "";
            while (rs.next()) {
                strfield = "";
                try {
                    strfield = rs.getString(2).substring(0, rs.getString(2).lastIndexOf("("));
                } catch (Exception e) {
                    strfield = String.valueOf(rs.getObject(2));
                }
                if (i == 1) {
                    str = str + strfield;
                } else {
                    str = str + ":" + strfield;
                }
                i++;
            }

            result = str.split(":");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err in describe : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        int minDate = 3;
        String path = "c:\\cashier\\data_out\\";
        TransferData transferData = new TransferData();
        transferData.writeDataTableToFile(TABLE_TRANS_MEMBER_POINT, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_PENDING_ORDER, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_PAYMENT, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_CREDIT_PAYMENT_MAIN, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_CREDIT_PAYMENT_INFO, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_CREDIT_PAYMENT, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_CREDIT_CARD, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_CASHIER, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_BILL_MAIN, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_BILL_DETAIL, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_BALANCE, minDate, path);
        transferData.writeDataTableToFile(TABLE_TRANS_CASH_RETURN_PAYMENT, minDate, path);
        System.out.println("*********** Backup data is finished...");
        System.out.println("*********** Second process, Please syncronize data from cashier");
        System.out.println("*********** Now you safe to close this window");
    }


    public static final int TABLE_TRANS_MEMBER_POINT = 0;
    public static final int TABLE_TRANS_PENDING_ORDER = 1;
    public static final int TABLE_TRANS_CASH_PAYMENT = 2;
    public static final int TABLE_TRANS_CASH_CREDIT_PAYMENT_MAIN = 3;
    public static final int TABLE_TRANS_CASH_CREDIT_PAYMENT_INFO = 4;
    public static final int TABLE_TRANS_CASH_CREDIT_PAYMENT = 5;
    public static final int TABLE_TRANS_CASH_CREDIT_CARD = 6;
    public static final int TABLE_TRANS_CASH_CASHIER = 7;
    public static final int TABLE_TRANS_CASH_BILL_MAIN = 8;
    public static final int TABLE_TRANS_CASH_BILL_DETAIL = 9;
    public static final int TABLE_TRANS_CASH_BALANCE = 10;
    public static final int TABLE_TRANS_CASH_RETURN_PAYMENT = 11;

    public static String[] dump_table_names = { "cashier_member_point_stock_dump.sql","cashier_cash_pending_order_dump.sql",
        "cashier_cash_payment_dump.sql","cashier_cash_credit_payment_main_dump.sql","cashier_cash_credit_payment_info_dump.sql",
        "cashier_cash_credit_payment_dump.sql","cashier_cash_credit_card_dump.sql","cashier_cash_cashier_dump.sql",
        "cashier_cash_bill_main_dump.sql","cashier_cash_bill_detail_dump.sql","cashier_cash_balance_dump.sql",
        "cashier_cash_return_payment_dump.sql"
    };


    /**
     * gadnyana
     * backup data to file
     * parameter by date (transaction date)
     * @param tableIndex
     * @param minDay
     * @param pathFile
     */
    public void writeDataTableToFile(int tableIndex, int minDay, String pathFile){
        DBResultSet dbrs = null;
        try{
            String table = "";
            Date dtstart = new Date();
            dtstart.setDate(dtstart.getDate() - minDay);
            Date dtend = new Date();
            String sql = "";
            pathFile = "c:\\\\cashier\\\\data_out\\\\";
            File file = new File(pathFile+"\\"+dump_table_names[tableIndex]);
            if(file.exists()){
                file.delete();
            }
            switch (tableIndex){
                case TABLE_TRANS_MEMBER_POINT:
                    table = PstMemberPoin.TBL_POS_MEMBER_POIN;
                    sql = "SELECT mp.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_MEMBER_POINT] + "' "+
                        " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " as bm " +
                        " INNER JOIN " + PstMemberPoin.TBL_POS_MEMBER_POIN + " as mp on "+
                        " mp." + PstMemberPoin.fieldNames[PstMemberPoin.FLD_CASH_BILL_MAIN_ID] +
                        " =bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                        " WHERE bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                        " BETWEEN '" + Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                        " and '" + Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                    break;
                case TABLE_TRANS_PENDING_ORDER:
                    table = PstPendingOrder.TBL_CASH_PENDING_ORDER;
                    sql = "SELECT * "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_PENDING_ORDER] + "' "+
                        " FROM " + PstPendingOrder.TBL_CASH_PENDING_ORDER+
                        " WHERE " +PstPendingOrder.fieldNames[PstPendingOrder.FLD_CREATION_DATE]+
                        " BETWEEN '" + Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                        " and '" + Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                    break;
                case TABLE_TRANS_CASH_PAYMENT:
                    table = PstCashPayment.TBL_PAYMENT;
                    sql = "SELECT bd.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_PAYMENT] + "' "+
                        " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " as bm"+
                        " INNER JOIN " + PstCashPayment.TBL_PAYMENT +" as bd on "+
                        " bd." +PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
                        " =bm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                        " WHERE bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                        " BETWEEN '" + Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                        " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                         break;
                case TABLE_TRANS_CASH_CREDIT_PAYMENT_MAIN:
                    table = PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN;
                    sql = "SELECT * "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_CREDIT_PAYMENT_MAIN] + "' "+
                        " FROM " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN +
                        " WHERE " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_DATE]+
                        " BETWEEN '" + Formater.formatDate(dtstart, "yyyy-MM-dd") +" 00:00:00' "+
                        " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                    break;
                case TABLE_TRANS_CASH_CREDIT_PAYMENT_INFO:
                    table = PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO;
                    sql = "SELECT cci.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_CREDIT_PAYMENT_INFO ] + "' "+
                        " FROM " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " as bm "+
                        " INNER JOIN " + PstCashCreditPayment.TBL_PAYMENT + " as cp on "+
                        " cp." + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
                         " =bm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
                         " INNER JOIN " + PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO+ " as cci on "+
                         " cci." + PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]+
                         " =cp." + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]+
                         " WHERE bm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_DATE]+
                         " BETWEEN '" + Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                         " and '" + Formater.formatDate(dtend, "yyyy-MM-dd") +" 23:59:59'";
                    break;
                case TABLE_TRANS_CASH_CREDIT_PAYMENT:
                    table = PstCashCreditPayment.TBL_PAYMENT;
                    sql = "SELECT cp.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_CREDIT_PAYMENT] + "' "+
                         " FROM " +PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " as bm "+
                         " INNER JOIN " + PstCashCreditPayment.TBL_PAYMENT + " as cp on "+
                         " cp." + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
                         " =bm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
                         " WHERE " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_DATE]+
                         " BETWEEN '" + Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                         " and '" + Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                    break;
                case TABLE_TRANS_CASH_CREDIT_CARD:
                    table = PstCashCreditCard.TBL_CREDIT_CARD;
                    sql = "SELECT ccc.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_CREDIT_CARD] + "' "+
                         " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " as bm"+
                         " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " as bd on "+
                         " bd." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
                         " =bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                         " INNER JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + " as ccc on "+
                         " ccc." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]+
                         " =bd." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]+
                         " WHERE bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                         " BETWEEN '" +Formater.formatDate(dtstart, "yyyy-MM-dd") +" 00:00:00' "+
                         " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") +" 23:59:59'";
                     break;
                case TABLE_TRANS_CASH_CASHIER:
                    table = PstCashCashier.TBL_CASH_CASHIER;
                    sql = "SELECT * "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_CASHIER] + "' "+
                         " FROM " + PstCashCashier.TBL_CASH_CASHIER+
                         " WHERE " + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+
                         " BETWEEN '" +Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                         " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") +" 23:59:59'";
                   break;
                case TABLE_TRANS_CASH_BILL_MAIN:
                    table = PstBillMain.TBL_CASH_BILL_MAIN;
                    sql = "SELECT * "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_BILL_MAIN] + "' "+
                         " FROM " +PstBillMain.TBL_CASH_BILL_MAIN+
                         " WHERE " +PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                         " BETWEEN '" +Formater.formatDate(dtstart, "yyyy-MM-dd") + " 00:00:00' "+
                         " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                     break;
                case TABLE_TRANS_CASH_BILL_DETAIL:
                    table = PstBillDetail.TBL_CASH_BILL_DETAIL;
                    sql = "SELECT bd.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_BILL_DETAIL] + "' "+
                         " FROM " +PstBillMain.TBL_CASH_BILL_MAIN + " as bm"+
                         " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " as bd on "+
                         " bd." +PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
                         " =bm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                         " WHERE bm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                         " BETWEEN '" +Formater.formatDate(dtstart, "yyyy-MM-dd") +" 00:00:00' "+
                         " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") +" 23:59:59'";
                    break;
                case TABLE_TRANS_CASH_BALANCE:
                    table = PstBalance.TBL_BALANCE;
                    sql = "SELECT * "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_BALANCE] + "' "+
                         " FROM " + PstBalance.TBL_BALANCE +" as cb "+
                         " INNER JOIN " +PstCashCashier.TBL_CASH_CASHIER +" as ccash on "+
                         " ccash." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
                         " =cb." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+
                         " WHERE " +PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+
                         " BETWEEN '" +Formater.formatDate(dtstart, "yyyy-MM-dd")+" 00:00:00' "+
                         " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") + " 23:59:59'";
                     break;
                case TABLE_TRANS_CASH_RETURN_PAYMENT:
                    table = PstCashReturn.TBL_RETURN;
                    sql = "SELECT cr.* "+//INTO OUTFILE '" + pathFile + dump_table_names[TABLE_TRANS_CASH_RETURN_PAYMENT] + "' "+
                         " FROM " +PstBillMain.TBL_CASH_BILL_MAIN + " as bm"+
                         " INNER JOIN " + PstCashReturn.TBL_RETURN + " as cr on "+
                         " cr." +PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
                         " =bm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                         " WHERE bm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                         " BETWEEN '" +Formater.formatDate(dtstart, "yyyy-MM-dd") +" 00:00:00' "+
                         " and '" +Formater.formatDate(dtend, "yyyy-MM-dd") +" 23:59:59'";
                    break;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            writeDataTable(pathFile,table,rs);
            System.out.println("*********** Success ...");
        }catch(Exception e){
            System.out.println(""+e.toString());
        }
    }


    public void writeDataTable(String PATH_DATA_OUT, String table, ResultSet rs){
        DBResultSet dbrs = null;
        try{
            String[] strFieldType = showFieldType(table);
            boolean dataExist = false;
            while(rs.next()){
                dataExist = true;
                String str = "INSERT INTO `"+table+"` VALUES (";
                String strfield = "";
                for(int i=0;i<strFieldType.length;i++){
                    int k = getTypeDbField(strFieldType[i]);
                    if(strfield.length()>0)
                        strfield = strfield +",";

                    try{
                        switch(k){
                            case TYPE_STRING:
                                strfield = strfield + getString(rs.getString(i+1));
                                break;
                            case TYPE_NUMERIC:
                                int nmidx = Integer.parseInt((String)hashFieldNumeric.get(strFieldType[i]));
                                switch(nmidx){
                                    case TYPE_NUMERIC_LONG:
                                        strfield = strfield + ""+getNumeric(rs.getLong(i+1));
                                        break;
                                    case TYPE_NUMERIC_INT:
                                        strfield = strfield + ""+getNumeric(rs.getInt(i+1));
                                        break;
                                    case TYPE_NUMERIC_DOUBLE:
                                        strfield = strfield + ""+getNumeric(rs.getDouble(i+1));
                                        break;
                                }
                                break;
                            case TYPE_DATE:
                                try{
                                    java.sql.Date date = rs.getDate(i+1);
                                    strfield = strfield + getStrDate(date);
                                }catch(Exception ex){
                                    Date date = rs.getDate(i+1);
                                    strfield = strfield + getStrDate(date);
                                }
                                break;
                            case TYPE_DATETIME:
                                try{
                                    java.sql.Date date = rs.getDate(i+1);
                                    Time time = rs.getTime(i+1);
                                    Date dt = DBHandler.convertDate(date,time);
                                    strfield = strfield + getStrDate(dt);
                                }catch(Exception ex){
                                    Date date = rs.getDate(i+1);
                                    strfield = strfield + getStrDate(date);
                                }
                                break;
                        }
                    }catch(Exception e){
                        System.out.println("Err For >> : "+str+" == >>> "+e.toString());
                    }
                }
                str = str +strfield+");";
                TXTLogger.logger(PATH_DATA_OUT+"cashier_" + table + "_dump.sql",str);
            }
            if(!dataExist)
                TXTLogger.logger(PATH_DATA_OUT+"cashier_" + table + "_dump.sql","");

        }catch(Exception e){
            System.out.println("Err >> writeDataTable : "+e.toString());
        }
    }


    public void writeDataTable(String PATH_DATA_OUT, String table){
        DBResultSet dbrs = null;
        try{
            String[] strFieldType = showFieldType(table);
            String sql = "select * from "+table;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                String str = "INSERT INTO `"+table+"` VALUES (";
                String strfield = "";
                for(int i=0;i<strFieldType.length;i++){
                    int k = getTypeDbField(strFieldType[i]);
                    if(strfield.length()>0)
                        strfield = strfield +",";

                    try{
                        switch(k){
                            case TYPE_STRING:
                                strfield = strfield + getString(rs.getString(i+1));
                                break;
                            case TYPE_NUMERIC:
                                int nmidx = Integer.parseInt((String)hashFieldNumeric.get(strFieldType[i]));
                                switch(nmidx){
                                    case TYPE_NUMERIC_LONG:
                                        strfield = strfield + ""+getNumeric(rs.getLong(i+1));
                                        break;
                                    case TYPE_NUMERIC_INT:
                                        strfield = strfield + ""+getNumeric(rs.getInt(i+1));
                                        break;
                                    case TYPE_NUMERIC_DOUBLE:
                                        strfield = strfield + ""+getNumeric(rs.getDouble(i+1));
                                        break;
                                }
                                break;
                            case TYPE_DATE:
                                try{
                                    java.sql.Date date = rs.getDate(i+1);
                                    strfield = strfield + getStrDate(date);
                                }catch(Exception ex){
                                    Date date = rs.getDate(i+1);
                                    strfield = strfield + getStrDate(date);
                                }
                                break;
                            case TYPE_DATETIME:
                                try{
                                    java.sql.Date date = rs.getDate(i+1);
                                    Time time = rs.getTime(i+1);
                                    Date dt = DBHandler.convertDate(date,time);
                                    strfield = strfield + getStrDate(dt);
                                }catch(Exception ex){
                                    Date date = rs.getDate(i+1);
                                    strfield = strfield + getStrDate(date);
                                }
                                break;
                        }
                    }catch(Exception e){
                        System.out.println("Err For >> : "+str+" == >>> "+e.toString());
                    }
                }
                str = str +strfield+");";
                TXTLogger.logger(PATH_DATA_OUT+"cashier_" + table + "_dump.sql",str);
            }
        }catch(Exception e){
            System.out.println("Err >> writeDataTable : "+e.toString());
        }
    }

    /**
     * TRANSFER DATA TO FILE
     * @param PATH_DATA_OUT v
     * @param table
     */ 
    public void writeOutDataFile(String PATH_DATA_OUT, String table){
        DBResultSet dbrs = null;
        try{
            String sql = "select * INTO OUTFILE '"+PATH_DATA_OUT+"cashier_" + table + "_dump.sql' from "+table;
            DBHandler.execQueryResult(sql);
	    
        }catch(Exception e){
            System.out.println("Err >> writeDataTable : "+e.toString());
        }
    }
    
}
