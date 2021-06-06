/*
 * PrintBalancePOS.java
 *
 * Created on January 5, 2005, 2:35 PM
 */

package com.dimata.pos.printAPI;

import com.dimata.pos.cashier.BalanceItem;
import com.dimata.pos.cashier.BalanceModel;
import com.dimata.pos.cashier.CashierMainApp;
import com.dimata.pos.cashier.CashSaleController;
import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.printman.DSJ_PrintObj;
import com.dimata.pos.printman.DSJ_PrinterService;
import com.dimata.pos.session.balance.SessPrintBalance;
import com.dimata.pos.xmlparser.DSJ_CashierConfig;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  yogi
 */

public class PrintBalancePOS {
    
    public static String COL_SEPARATOR = "|";
    final static int PI_COL_START  = 1 ;
    final static int PI_COL_WIDTH_NAME = 12;
    final static int PI_COL_WIDTH_QTY	  =  9;
    final static int PI_COL_WIDTH_TOTAL = 9;
    final static int PI_COL_WIDTH_DESC	  = 16;
    
    
    
    final static int PI_COL_START_BAR_CODE = PI_COL_START;
    final static int PI_COL_START_QTY	  = PI_COL_START_BAR_CODE + PI_COL_WIDTH_NAME +1;
    final static int PI_COL_START_TOTAL = PI_COL_START_QTY +  PI_COL_WIDTH_QTY +1;
    final static int PI_COL_START_SIZE	  =  PI_COL_START_TOTAL + PI_COL_WIDTH_TOTAL+7;
    static int PI_COL_START_QTY2	  =  PI_COL_START_SIZE;
    static int PI_COL_START_PRICE	  = PI_COL_START_QTY + PI_COL_WIDTH_QTY +1;
    
    private static DSJ_CashierConfig cashierConfig;
    private static int language=0;
    
    final static int TXT_TGL=0;
    final static int TXT_JAM=1;
    final static int TXT_KASIR=2;
    final static int TXT_TUNAI=3;
    final static int TXT_JUMLAH=4;
    final static int TXT_TOTAL=5;
    final static int TXT_SUB_TOTAL_TUNAI=6;
    final static int TXT_KARTU=7;
    final static int TXT_SUB_TOTAL_KARTU=8;
    final static int TXT_PENJUALAN_KOTOR=9;
    final static int TXT_TOTAL_RESEP=10;
    final static int TXT_TOTAL_RETUR=11;
    final static int TXT_TOTAL_PENJUALAN=12;
    final static int TXT_TOTAL_INVOICE=13;
    final static int TXT_TOTAL_QTY=14;
    final static int TXT_TOTAL_RETUR_INVOICE=15;
    final static int TXT_TOTAL_QTY_RETUR=16;
    final static int TXT_DITERIMA_OLEH=17;
    final static int TXT_RATE=18;
    final static int TXT_TOTAL_BALANCE=19;
    final static int TXT_SALES_BRUTO=20;
    final static int TXT_SALES_NETTO=21;
    final static int TXT_TOTAL_CLOSING=22;
    final static int TXT_DEBET=23;
    final static int TXT_TOTAL_DEBET=24;
    final static int TXT_CHEQUE=25;
    final static int TXT_TOTAL_CHEQUE=26;
    final static int TXT_TOTAL_CURR=27;
    final static int TXT_OPENING_BALANCE=28;
    final static int TXT_TOTAL_DP=29;
    final static int TXT_TOTAL_CREDIT_SALE=30;
    final static int TXT_TOTAL_CHANGE=31;
    final static int TXT_TOTAL_SALE=32;
    final static int TXT_CASH_IN_DRAWER=33;
    final static int TXT_CLOSING_DETAIL=34;
    final static int TXT_TOTAL_SALE_DETAIL=35;
    final static int TXT_OTHER_COST=36;
    final static int TXT_CARD_COST=37;
    final static int TXT_DESCRIPTION=38;
    
    
    //print tittle
    public static final String titleHeader[][]={
        {"OPENING BALANCE","CLOSING BALANCE","     TOTAL SALE","TOTAL SALE"},
        {"OPENING BALANCE","CLOSING BALANCE","     TOTAL SALE","TOTAL SALE"}
    };
    //print header
    public static final String textListHeader[][] = {
        {"Tgl","Jam","Kasir","Tunai","Jumlah",
         //"Total(USD)","Sub Total Tunai","Kartu Kredit","Sub Total Kartu Kredit","Penjualan Kotor",
         "Jumlah","Subtot Tunai","Kartu Kredit","Subtot Kartu Kredit","Penjualan Kotor",
         "Servis Resep","Retur","Total Penjualan","Total Transaksi","Jml Brg Terjual",
         "Total Retur","Tot Brg Retur", "Diterima,", "Kurs (IDR)","Total Opening",
         "Penjualan bruto","Penjualan netto","Total Closing",
         "Kartu Debet","Subtot Kartu Debet","Cheque","Subtotal Cheque","Total","Opening Balance",
         "Pending Order(DP)","Pelunasan Piutang","Total Kembalian","Total Sale","Cash di Drawer","Closing Detail","Total Sale Detail",
         "Biaya Lain", "Biaya Kartu", "Keterangan"},
         {"Date","Time","Cashier","Cash","Amount",
          //"Total(USD)","SubTotal Cash","Credit Card","SubTotal Card","Sales Bruto",
          "Amount","Subtotal Cash","Credit Card","Subtotal Card","Sales Bruto",
          "Recipe Svc","Retur","Sales Total","Total Invoice","Qty Sales",
          "Total Retur","Qty Retur", "Receive By,", "Rate (IDR)","Total Opening Balance",
          "Sales brutto","Sales netto","Total Closing Balance",
          "Debet Card","Subtotal Debet Card ","Cheque","Subtotal Cheque","Total","Opening Balance",
          "Pending Order(DP)","Total Credit Payment","Total Change","Total Sale","Cash in Drawer","Closing Detail","Total Sale Detail",
          "Other Cost","Credit Card Cost", "Description"
         }
    };
    
    /** Holds value of property startLine. */
    private int startLine = 0;
    
    /** Creates a new instance of PrintBalancePOS */
    public PrintBalancePOS() {
    }
    
    
    
    //this use to print balance
    synchronized public void printBalanceObj(CashCashier cash) {
        
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintBalanceObj objToPrint = SessPrintBalance.getData(cash);
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            language=cashierConfig.language;
            obj.setPrnIndex(1);
            obj.setPageLength(20);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            int start = PI_COL_START - 1;
            //header
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setLine(start,1, ""+cashierConfig.company);
            obj.setLine(start,24, titleHeader[language][0]);
            start++;
            
            obj.setLine(start,1, ""+cashierConfig.address);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",cashierConfig.address.length()+1);
            start++;
            
            obj.setLine(start,1,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(objToPrint.getDateBalance(),"dd/MM/yyyy"));
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(new Date(),"kk:mm:ss"));
            start++;
            
            obj.setLine(start,1,""+textListHeader[language][TXT_KASIR]+": "+objToPrint.getCashier());
            start++;
            
            //item data
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;
            
            
            int[] cols = {7,13,20};
            obj.newColumn(3,"",cols);
            
            obj.setColumnValue(0,start,""+textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,""+textListHeader[language][TXT_RATE],DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
            System.out.println("Line length "+((String)obj.getLines().get(start)).length());
            System.out.println("String "+((String)obj.getLines().get(start)));
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            int sizeData = objToPrint.getListDataBalance().size();
            double total = 0;
            for(int i=0;i<sizeData;i++){
                Vector vect = (Vector)objToPrint.getListDataBalance().get(i);
                Balance balance = (Balance)vect.get(0);
                CurrencyType curr = (CurrencyType)vect.get(1);
                StandartRate daily = (StandartRate)vect.get(2);
                
                obj.setColumnValue(0, start,curr.getCode(),DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(1, start,FRMHandler.userFormatStringDecimal(daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balance.getBalanceValue()*daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balance.getBalanceValue()*daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(2, start,FRMHandler.userFormatStringDecimal(balance.getBalanceValue()*daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                total = total + (balance.getBalanceValue()*daily.getSellingRate());
                start++;
            }
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(total),DSJ_PrintObj.TEXT_RIGHT);
            //obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(total),DSJ_PrintObj.TEXT_RIGHT);
            //obj.setColumnValue(2,start,FRMHandler.userFormatStringDecimal(total),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start++,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            
            //obj.setLine(start,1, "\n\n\n\n\n\n\n\n");
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            //prnSvc.running = true;
            //prnSvc.run_x();
            System.out.println("Print balance finish ...");
            //prnSvc.running=false;
        }
        catch (Exception exc) {
            System.out.println("Exc di printing balance : "+ exc);
            exc.printStackTrace();
        }
    }
    
    //this use to print balance
    synchronized public void printBigBalanceObj(CashCashier cash) {
        
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintBalanceObj objToPrint = SessPrintBalance.getData(cash);
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            language=cashierConfig.language;
            obj.setPrnIndex(1);
            obj.setObjDescription("Print big balance invoice");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE);
            
            int start = PI_COL_START - 1;
            //header
            
           
            //header
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            obj.setLine(start,1, ""+cashierConfig.address);
            start++;//3
            obj.setLine(start,1,""+titleHeader[language][0]);
            start++;
            
            obj.setLine(start,1, "");
            start++;//3
                        
            obj.setLine(start,1, "");
            start++;//3
            
            int[] intCols1 = new int[4];
            intCols1[0] = 15;
            intCols1[1] = 25;
            intCols1[2] = 15;
            intCols1[3] = 25;
            
            obj.newColumn(4,"", intCols1);
                        
            obj.setColumnValue(0, start, textListHeader[language][TXT_TGL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, ": " +Formater.formatDate(objToPrint.getDateBalance()), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, start, ": " +Formater.formatDate(new Date(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, textListHeader[language][TXT_KASIR], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, ": " +objToPrint.getCashier(), DSJ_PrintObj.TEXT_LEFT);
            start++;
                       
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            
            int[] cols = {30,25,25};
            obj.newColumn(3,"",cols);
            
            obj.setColumnValue(0,start,""+textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,""+textListHeader[language][TXT_RATE],DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            int sizeData = objToPrint.getListDataBalance().size();
            double total = 0;
            for(int i=0;i<sizeData;i++){
                Vector vect = (Vector)objToPrint.getListDataBalance().get(i);
                Balance balance = (Balance)vect.get(0);
                CurrencyType curr = (CurrencyType)vect.get(1);
                StandartRate daily = (StandartRate)vect.get(2);
                
                obj.setColumnValue(0, start,curr.getCode(),DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balance.getBalanceValue()*daily.getSellingRate()),DSJ_PrintObj.TEXT_RIGHT);
                total = total + (balance.getBalanceValue()*daily.getSellingRate());
                start++;
            }
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(total),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print big balance finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di printing balance : "+ exc);
            exc.printStackTrace();
        }
    }
    
            
    // this is currently used
    // by wpulantara
    synchronized public void printCloseBalanceObj(BalanceModel model) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BalanceModel printObj = model;
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            language=cashierConfig.language;
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = CashSaleController.getLatestRate(String.valueOf(currType.getOID())).getSellingRate();
            if(rate<1){
                rate = 1;
            }
            
            obj.setPrnIndex(1);
            obj.setPageLength(60);
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            int start = PI_COL_START - 1;
            //header
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setLine(start,1, ""+cashierConfig.company);
            obj.setLine(start,24, titleHeader[language][1]);
            start++;
            
            obj.setLine(start,1, ""+cashierConfig.address);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",cashierConfig.address.length()+1);
            start++;
            
            obj.setLine(start,1,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDate(),"dd/MM/yyyy"));
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(new Date(),"kk:mm:ss"));
            start++;
            
            obj.setLine(start,1,""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashierName());
            start++;
            start++;
            
            int[] colx = {20,6,12,20};
            obj.newColumn(4,"",colx);
            
            obj.setColumnValue(0,start,textListHeader[language][TXT_OPENING_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getOpeningBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //total
            obj.newColumn(4,"",colx);
            obj = setDetailTotal(obj, start,currUse,printObj, rate);
            start = getStartLine();
            
            
            //obj.setLine (start,1, "\n\n\n\n\n\n\n\n");
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print balance finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc : "+ exc);
        }
    }
    
    

    // this is currently used
    // by wpulantara
    synchronized public void printBigCloseBalanceObj(BalanceModel model) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BalanceModel printObj = model;
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            language=cashierConfig.language;
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = CashSaleController.getLatestRate(String.valueOf(currType.getOID())).getSellingRate();
            if(rate<1){
                rate = 1;
            }
            
            obj.setPrnIndex(1);
            obj.setPageLength(100);
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE);
            
            int start = PI_COL_START - 1;
            //header
            
            //header
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            obj.setLine(start,1, ""+cashierConfig.address);
            start++;//3
            obj.setLine(start,1,""+titleHeader[language][1]);
            start++;
            
            obj.setLine(start,1, "");
            start++;//3
                        
            obj.setLine(start,1, "");
            start++;//3
            
            int[] intCols1 = new int[4];
            intCols1[0] = 15;
            intCols1[1] = 25;
            intCols1[2] = 15;
            intCols1[3] = 20;
            
            obj.newColumn(4,"", intCols1);
                        
            obj.setColumnValue(0, start, textListHeader[language][TXT_TGL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, ": " +Formater.formatDate(printObj.getDate(),"dd/MM/yyyy"), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, start, ": " +Formater.formatDate(new Date(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, textListHeader[language][TXT_KASIR], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, ": " +printObj.getCashierName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
                       
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            int[] colx = {45,10,25,25};
            obj.newColumn(4,"",colx);
            
            obj.setColumnValue(0,start,textListHeader[language][TXT_OPENING_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getOpeningBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //total
            obj = setBigDetailTotal(obj, start,currUse,printObj, rate);
            start = getStartLine();
            
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print balance finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc : "+ exc);
        }
    }
    
    // this is currently used
    // by wpulantara
    synchronized public void printBigTotalSaleObj(BalanceModel model) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BalanceModel printObj = model;
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            language=cashierConfig.language;
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = CashSaleController.getLatestRate(String.valueOf(currType.getOID())).getSellingRate();
            if(rate<1){
                rate = 1;
            }
            
            obj.setPrnIndex(1);
            obj.setPageLength(100);
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE);
            
            int start = PI_COL_START - 1;
            //header
            
            //header
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            obj.setLine(start,1, ""+cashierConfig.address);
            start++;//3
            obj.setLine(start,1, ""+titleHeader[language][3]);
            start++;//3
            obj.setLine(start,1, "");
            start++;//3
                        
            obj.setLine(start,1, "");
            start++;//3
            
            int[] intCols1 = new int[4];
            intCols1[0] = 15;
            intCols1[1] = 25;
            intCols1[2] = 15;
            intCols1[3] = 20;
            
            obj.newColumn(4,"", intCols1);
                        
            obj.setColumnValue(0, start, textListHeader[language][TXT_TGL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, ": " +Formater.formatDate(printObj.getDate(),"dd/MM/yyyy"), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, start, ": " +Formater.formatDate(new Date(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, textListHeader[language][TXT_KASIR], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, ": " +printObj.getCashierName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
                       
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            int[] colx = {45,10,25,25};
            obj.newColumn(4,"",colx);
            
            obj.setColumnValue(0,start,textListHeader[language][TXT_OPENING_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getOpeningBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //total
            obj = setBigDetailTotalSale(obj, start,currUse,printObj, rate);
            start = getStartLine();
            
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print balance finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc : "+ exc);
        }
    }
    
    
    //for print total sale (currently in use)
    synchronized public void printTotalSaleObj(BalanceModel model) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BalanceModel printObj = model;
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            language=cashierConfig.language;
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = CashSaleController.getLatestRate(String.valueOf(currType.getOID())).getSellingRate();
            if(rate<1){
                rate = 1;
            }
            
            obj.setPrnIndex(1);
            obj.setPageLength(60);
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            int start = PI_COL_START - 1;
            //header
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setLine(start,1, ""+cashierConfig.company);
            obj.setLine(start,24, titleHeader[language][2]);
            start++;
            
            obj.setLine(start,1, ""+cashierConfig.address);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",cashierConfig.address.length()+1);
            start++;
            
            obj.setLine(start,1,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDate(),"dd/MM/yyyy"));
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(new Date(),"kk:mm:ss"));
            start++;
            
            obj.setLine(start,1,""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashierName());
            start++;
            start++;
            
            int[] colx = {20,6,12,20};
            obj.newColumn(4,"",colx);
            
            obj.setColumnValue(0,start,textListHeader[language][TXT_OPENING_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getOpeningBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            
            //total
            obj.newColumn(4,"",colx);
            obj = setDetailTotalSale(obj, start,currUse,printObj,rate);
            start = getStartLine();
            obj.setLine(start,1, "\n\n\n\n\n\n\n\n");
            prnSvc.print(obj);
            //prnSvc.running = true;
            //prnSvc.run_x();
            System.out.println("Print balance finish ...");
            //prnSvc.running=false;
        }
        catch (Exception exc) {
            System.out.println("Exc : "+ exc);
        }
    }
    /*
    //opening balance
    public void setItemSizeHd(DSJ_PrintObj obj, int start){
        int baris=start;
        if ((this.entObj!=null)&&(entObj.size()>0)) {
            for(int i =  0 ; i <  this.entObj.size(); i++){
                Balance blc=(Balance)entObj.get(i);
                String jumlah=PstBalance.formatCurrency(blc.getBalanceValue());
                String code="";
                String rates="";
                double rateValue=1;
                String currencyStr=String.valueOf(blc.getCurrencyOid());
                for (int a=0;a<objCurr.size();a++){
                    Vector currVec=(Vector)objCurr.get(a);
                    String currencyCode=(String)currVec.get(0);
                    if (currencyStr.equalsIgnoreCase(currencyCode)){
                        rates=(String)currVec.get(3);
                        rateValue=Double.parseDouble(rates);
                        rates=PstBalance.formatCurrency(rateValue);
                        code=(String)currVec.get(1);
                    }//endif
                }//endfor
                //System.out.println("Printing >>>>>>>>>>>>>>>>>>>>");
                rates=rates.substring(0,rates.length()-3);
                jumlah=jumlah.substring(0,jumlah.length()-3);
                if (jumlah.length()==0)
                    jumlah="0";
                obj.setLine(baris,PI_COL_START_BAR_CODE,code);
                obj.setLineRightAlign(baris,PI_COL_START_QTY+1,rates,10);
                obj.setLineRightAlign(baris,PI_COL_START_TOTAL+1,jumlah,12);
                baris+=1;
            }
        }
        obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        int awal=1;
        int start=baris+1;
        obj.setLine(start+1,0,"\n\n\n\n\n\n\n\n");
        /*for (int i=start;i<(start + 8);i++){
                obj.setLine(start+awal,PI_COL_START_QTY+1,"");
                awal++;
        }*/
    //}
    
    //set balance detail
    public DSJ_PrintObj setDetailBalance(DSJ_PrintObj obj, int start,Vector listDetail,String currUse, double rate){
        if(listDetail!=null&&listDetail.size()>0){
            Vector tempCash = (Vector)listDetail.get(0);
            Vector tempCredit = (Vector)listDetail.get(1);
            Vector tempCheq = (Vector)listDetail.get(2);
            Vector tempDebit = (Vector)listDetail.get(3);
            
            if(tempCash!=null&&tempCash.size()>0){
                int[] cols = {14,12,12,20};
                obj.newColumn(4,"",cols);
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int sizeData = tempCash.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCash.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int[] colx = {20,6,12,20};
                obj.newColumn(4,"",colx);
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_SUB_TOTAL_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                start++;
            }
            
            //credit card
            if(tempCredit!=null&&tempCredit.size()>0){
                int[] cols = {14,12,12,20};
                obj.newColumn(4,"",cols);
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_KARTU],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int sizeData = tempCredit.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCredit.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int[] colx = {20,6,12,20};
                obj.newColumn(4,"",colx);
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_SUB_TOTAL_KARTU],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(Double.parseDouble((String)printObj.getSubTotal().get(i))),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                start++;
            }
            
            //cheque
            if(tempCheq!=null&&tempCheq.size()>0){
                int[] cols = {14,12,12,20};
                obj.newColumn(4,"",cols);
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_CHEQUE],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int sizeData = tempCheq.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCheq.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int[] colx = {20,6,12,20};
                obj.newColumn(4,"",colx);
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CHEQUE],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(Double.parseDouble((String)printObj.getSubTotal().get(i))),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                start++;
            }
            
            //debet
            if(tempDebit!=null&&tempDebit.size()>0){
                int[] cols = {14,12,12,20};
                obj.newColumn(4,"",cols);
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_DEBET],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int sizeData = tempDebit.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempDebit.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int[] colx = {20,6,12,20};
                obj.newColumn(4,"",colx);
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_DEBET],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(Double.parseDouble((String)printObj.getSubTotal().get(i))),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                start++;
            }
            //}
            setStartLine(start);
        }
        return obj;
    }
    
    public DSJ_PrintObj setBigDetailBalance(DSJ_PrintObj obj, int start,Vector listDetail,String currUse, double rate){
        if(listDetail!=null&&listDetail.size()>0){
            Vector tempCash = (Vector)listDetail.get(0);
            Vector tempCredit = (Vector)listDetail.get(1);
            Vector tempCheq = (Vector)listDetail.get(2);
            Vector tempDebit = (Vector)listDetail.get(3);
            int[] cols = {30,25,25,25};
            obj.newColumn(4,"|",cols);
            if(tempCash!=null&&tempCash.size()>0){
                
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(3,start,""+textListHeader[language][TXT_DESCRIPTION],DSJ_PrintObj.TEXT_CENTER);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                int sizeData = tempCash.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCash.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                                
                obj.setColumnValue(0,start,textListHeader[language][TXT_SUB_TOTAL_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                start++;
            }
            
            //credit card
            if(tempCredit!=null&&tempCredit.size()>0){
                
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_KARTU],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(3,start,""+textListHeader[language][TXT_DESCRIPTION],DSJ_PrintObj.TEXT_CENTER);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                int sizeData = tempCredit.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCredit.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_SUB_TOTAL_KARTU],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                //obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(Double.parseDouble((String)printObj.getSubTotal().get(i))),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                start++;
            }
            
            //cheque
            if(tempCheq!=null&&tempCheq.size()>0){
                
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_CHEQUE],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(3,start,""+textListHeader[language][TXT_DESCRIPTION],DSJ_PrintObj.TEXT_CENTER);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                int sizeData = tempCheq.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCheq.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CHEQUE],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                start++;
            }
            
            //debet
            if(tempDebit!=null&&tempDebit.size()>0){
                
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_DEBET],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_CENTER);
                obj.setColumnValue(3,start,""+textListHeader[language][TXT_DESCRIPTION],DSJ_PrintObj.TEXT_CENTER);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                int sizeData = tempDebit.size();
                double subtotal = 0;
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempDebit.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount(),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    subtotal = subtotal + balanceItem.getTotalIDR();
                }
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_DEBET],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(subtotal/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                start++;
            }
            setStartLine(start);
        }
        return obj;
    }
    
    //set cash in drawer
    synchronized public DSJ_PrintObj setCashInDrawer(DSJ_PrintObj obj, int start,Vector list,String currUse){
        if(list!=null&&list.size()>0){
            Vector tempCash = (Vector)list.get(0);
            
            if(tempCash!=null&&tempCash.size()>0){
                int[] cols = {14,12,12,20};
                obj.newColumn(4,"",cols);
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
                start++;
                
                //start amount payment
                obj.setColumnValue(0,start,""+textListHeader[language][TXT_CASH_IN_DRAWER],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start,textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start,""+textListHeader[language][TXT_TOTAL_CURR],DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                int sizeData = tempCash.size();
                for(int j=0;j<sizeData;j++){
                    BalanceItem balanceItem = (BalanceItem)tempCash.get(j);
                    obj.setColumnValue(0, start,balanceItem.getCurrencyCode(),DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, start,CashierMainApp.setFormatNumber(balanceItem.getAmount()),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2, start,CashierMainApp.setFormatNumber(balanceItem.getTotalIDR()),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                }
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                start++;
                start++;
            }
            
            setStartLine(start);
        }
        return obj;
    }
    
    //set total
    synchronized public DSJ_PrintObj setDetailTotal(DSJ_PrintObj obj, int start,String currUse,BalanceModel printObj, double rate){
        /*
        obj.setColumnValue(0,start,textListHeader[language][TXT_PENJUALAN_KOTOR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getSalesBruto()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
         
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CHANGE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getChangeTotal()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
         */
        if(printObj.getDownPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getDownPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_PENJUALAN],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getSalesBruto()-printObj.getChangeTotal()-printObj.getOtherCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getReturnTotal()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR_INVOICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getReturnTotal()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCreditPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CREDIT_SALE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCreditPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if((printObj.getOtherCost()-printObj.getCardCost())>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getOtherCost()-printObj.getCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCardCost()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCardCost()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CLOSING],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getClosingBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_CLOSING_DETAIL],DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        Vector list = printObj.getClosingDetail();
        list = printObj.switchByTypePayment(list);
        obj = this.setDetailBalance(obj,start,list,currUse,rate);
        start = getStartLine();
        
        int[] colx = {20,12,12,14};
        obj.newColumn(4,"",colx);
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_INVOICE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalInvoice(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalReturn(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY_RETUR],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        start++;
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_KASIR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,textListHeader[language][TXT_DITERIMA_OLEH],DSJ_PrintObj.TEXT_LEFT);
        start++;
        start++;
        start++;
        
        obj.setColumnValue(0,start,printObj.getCashierName(),DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,"______________",DSJ_PrintObj.TEXT_LEFT);
        start++;
        this.setStartLine(start);
        return obj;
    }
    
    synchronized public DSJ_PrintObj setBigDetailTotal(DSJ_PrintObj obj, int start,String currUse,BalanceModel printObj, double rate){
       
        if(printObj.getDownPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getDownPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_PENJUALAN],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getSalesBruto()-printObj.getChangeTotal()-printObj.getOtherCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getReturnTotal()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR_INVOICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getReturnTotal()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCreditPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CREDIT_SALE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCreditPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if((printObj.getOtherCost()-printObj.getCardCost())>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getOtherCost()-printObj.getCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCardCost()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCardCost()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CLOSING],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getClosingBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        start++;
              
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_CLOSING_DETAIL],DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        Vector list = printObj.getClosingDetail();
        list = printObj.switchByTypePayment(list);
        obj = this.setBigDetailBalance(obj,start,list,currUse,rate);
        start = getStartLine();
        
        int[] colx = {35,20,15,15};
        obj.newColumn(4,"",colx);
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_INVOICE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalInvoice(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalReturn(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY_RETUR],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()),DSJ_PrintObj.TEXT_RIGHT); 
        start++;
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_KASIR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,textListHeader[language][TXT_DITERIMA_OLEH],DSJ_PrintObj.TEXT_LEFT);
        start++;
        start++;
        start++;
        
        obj.setColumnValue(0,start,printObj.getCashierName(),DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,"______________",DSJ_PrintObj.TEXT_LEFT);
        start++;
        this.setStartLine(start);
        return obj;
    }
    
    
    synchronized public DSJ_PrintObj getLastFeed(DSJ_PrintObj obj, int start){
        for(int i = 0; i < cashierConfig.printingGap; i++){
            obj.setLine(start,"");
            start++;
        }
        setStartLine(start);
        return obj;
    }
    
    synchronized public DSJ_PrintObj setDetailTotalSale(DSJ_PrintObj obj, int start,String currUse,BalanceModel printObj, double rate){
        /*
        obj.setColumnValue(0,start,textListHeader[language][TXT_PENJUALAN_KOTOR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getSalesBruto()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
         
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CHANGE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getChangeTotal()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
         */
        if(printObj.getDownPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getDownPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_PENJUALAN],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getSalesBruto()-printObj.getChangeTotal()-printObj.getOtherCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getReturnTotal()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR_INVOICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getReturnTotal()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCreditPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CREDIT_SALE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCreditPayment(),rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if((printObj.getOtherCost()-printObj.getCardCost())>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getOtherCost()-printObj.getCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCardCost()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCardCost()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_SALE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getClosingBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_SALE_DETAIL],DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        Vector list = printObj.getClosingDetail();
        list = printObj.switchByTypePayment(list);
        obj = this.setDetailBalance(obj,start,list,currUse,rate);
        start = getStartLine();
        
        int[] colx = {20,12,12,14};
        obj.newColumn(4,"",colx);
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_INVOICE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalInvoice(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalReturn(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY_RETUR],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        start++;
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        this.setStartLine(start);
        return obj;
    }
    
    synchronized public DSJ_PrintObj setBigDetailTotalSale(DSJ_PrintObj obj, int start,String currUse,BalanceModel printObj, double rate){
        
        if(printObj.getDownPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getDownPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_PENJUALAN],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getSalesBruto()-printObj.getChangeTotal()-printObj.getOtherCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getReturnTotal()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR_INVOICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getReturnTotal()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCreditPayment()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_CREDIT_SALE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCreditPayment(),rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if((printObj.getOtherCost()-printObj.getCardCost())>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber((printObj.getOtherCost()-printObj.getCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getCardCost()>0){
            obj.setColumnValue(0,start,textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getCardCost()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_SALE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start,CashierMainApp.setFormatNumber(printObj.getClosingBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_SALE_DETAIL],DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        Vector list = printObj.getClosingDetail();
        list = printObj.switchByTypePayment(list);
        obj = this.setBigDetailBalance(obj,start,list,currUse,rate);
        start = getStartLine();
        
        int[] colx = {25,15,15,15};
        obj.newColumn(4,"",colx);
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_INVOICE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalInvoice(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtySales()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_RETUR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start,""+printObj.getTotalReturn(),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setColumnValue(0,start,textListHeader[language][TXT_TOTAL_QTY_RETUR],DSJ_PrintObj.TEXT_LEFT);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()).replace('.',','),DSJ_PrintObj.TEXT_RIGHT);
        else
            obj.setColumnValue(1,start,String.valueOf(printObj.getQtyReturn()),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        this.setStartLine(start);
        return obj;
    }
    
    /** Getter for property startLine.
     * @return Value of property startLine.
     *
     */
    public int getStartLine() {
        return this.startLine;
    }
    
    /** Setter for property startLine.
     * @param startLine New value of property startLine.
     *
     */
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }
    
}
