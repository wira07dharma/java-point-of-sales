/*
 * PrintInvoicePOS.java
 *
 * Created on January 6, 2005, 10:39 AM
 */

package com.dimata.pos.printAPI;

import com.dimata.pos.cashier.*;
import com.dimata.pos.entity.billing.BillDetailCode;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.OtherCost;
import com.dimata.pos.entity.payment.CashCreditCard;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.printman.DSJ_PrintObj;
import com.dimata.pos.printman.DSJ_PrinterService;
import com.dimata.pos.session.billing.SessPrintInvoice;
import com.dimata.pos.xmlparser.DSJ_CashierConfig;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
/**
 *
 * @author  yogi
 */
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.util.Formater;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Enumeration;

public class PrintInvoicePOS {
    
    final int[] cols = {11,10,5,14,20};
    final int[] colp = {21,5,14,20};
    final int cola[] = {11,10,19,20};
    final int colb[] = {21,19,20};
    public static String COL_SEPARATOR = "|";
    final static int PI_COL_START  = 1 ;
    final static int PI_COL_WIDTH_NAME = 20;
    final static int PI_COL_WIDTH_QTY	  =  3;
    final static int PI_COL_WIDTH_TOTAL = 8;
    final static int MAX_LENGTH = 40+1;
    
    final static int PI_COL_START_BAR_CODE = PI_COL_START;
    final static int PI_COL_START_QTY	  = PI_COL_START_BAR_CODE + PI_COL_WIDTH_NAME +1;
    final static int PI_COL_START_TOTAL   = PI_COL_START_QTY +  PI_COL_WIDTH_QTY +1;
    final static int PI_COL_START_SIZE	  =  PI_COL_START_TOTAL + PI_COL_WIDTH_TOTAL+7;
    
    private static DSJ_CashierConfig cashierConfig;
    private static int language=0;
    private static Vector ccObj=null;
    
    
    public final static int TXT_TERIMAKASIH=0;
    public final static int TXT_INVOICE=1;
    public final static int TXT_NORESEP=2;
    public final static int TXT_PASIEN=3;
    public final static int TXT_DOKTER=4;
    public final static int TXT_BIAYARESEP=5;
    public final static int TXT_NO=6;
    public final static int TXT_KASIR=7;
    public final static int TXT_TGL=8;
    public final static int TXT_JAM=9;
    public final static int TXT_NAMA=10;
    public final static int TXT_QTY=11;
    public final static int TXT_JUMLAH=12;
    public final static int TXT_TOTAL=13;
    public final static int TXT_TUNAI=14;
    public final static int TXT_KARTU=15;
    public final static int TXT_TOTAL_BAYAR=16;
    public final static int TXT_KEMBALI=17;
    public final static int TXT_NO_KARTU=18;
    public final static int TXT_HOLDER=19;
    public final static int TXT_TIPE_KARTU=20;
    public final static int TXT_TOTAL_AFTER_DISC=21;
    public final static int TXT_DISC=22;
    public final static int TXT_PRC=23;
    public final static int TXT_SUB_TOTAL=24;
    public final static int TXT_TAX=25;
    public final static int TXT_SERVICE=26;
    public final static int TXT_MEMBER_NAME=27;
    public final static int TXT_MEMBER_BARCODE=28;
    public final static int TXT_MEMBER_POINT=29;
    public final static int TXT_TGL_BERLAKU=30;
    public final static int TXT_NAMA_BANK=31;
    public final static int TXT_NO_ACCOUNT=32;
    public final static int TXT_DEBET=33;
    public final static int TXT_CHECK=34;
    public final static int TXT_INVOICE_CREDIT=35;
    public final static int TXT_GIFT_TAKING=36;
    public final static int TXT_PREV_POINT=37;
    public final static int TXT_ITEM_POINT=38;
    public final static int TXT_POINT=39;
    public final static int TXT_TOTAL_POINT=40;
    public final static int TXT_CURR_BAL_POINT=41;
    public final static int TXT_CREDIT_PAYMENT=42;
    public final static int TXT_NO_REF=43;
    public final static int TXT_JML_TRANS_TERAKHIR=44;
    public final static int TXT_BALANCE=45;
    public final static int TXT_JML_TRANS_AWAL=46;
    public final static int TXT_DP=47;
    public final static int TXT_PAY_BY_RETURN=48;
    public final static int TXT_PAY_BY_DP=49;
    public final static int TXT_OPEN_BILL=50;
    public final static int TXT_GUEST_NAME=51;
    public final static int TXT_COVER=52;
    public final static int TXT_CARD_COST=53;
    public final static int TXT_OTHER_COST=54;
    public final static int TXT_OTHER_COST_NOTE=55;
    public final static int TXT_RATE = 56;
    public final static int TXT_TOTAL_QTY = 57;
    public final static int TXT_SWIFT_CODE = 58;
    public final static int TXT_COMPANY = 59;
    public final static int TXT_CONTACT = 60;
    public final static int TXT_ADDRESS = 61;
    public final static int TXT_COUNTRY = 62;
    public final static int TXT_TLP = 63;
    public final static int TXT_EMAIL = 64;
    public final static int TXT_CURRENCY_IN = 65;
    public final static int TXT_SKU = 66;
    public final static int TXT_BARCODE = 67;
    public final static int TXT_STN = 68;
    public final static int TXT_DESCRIPTION = 69;
    public final static int TXT_INV_NAME = 70;
    public final static int TXT_PREPARED_BY = 71;
    public final static int TXT_CHECKED_BY = 72;
    public final static int TXT_APPROVED_BY = 73;
    public final static int TXT_CUSTOMER = 74;
    public final static int TXT_SUBTOTAL_PER_PAGE = 75;
    
    //print header
    public static final String textListHeader[][] = {
        {"TERIMAKASIH","   INVOICE","No Resep","Pasien","Dokter","Biaya Resep","No","Kasir","Tgl","Jam","Nama Brg","Qty","Jumlah",
         "Total","Tunai","Kartu Kredit","Total Bayar", "Kembalian","Nomor","Holder","Type","Total Setelah Disc","Diskon","Harga",
         "Sub Total","Pajak","Service","Nama","Barcode","Poin","Tgl Akhir","Bank","No. Account","Kartu Debet","Cek","Credit Invoice",
         "     Ambil Hadiah","Jml Poin Sebelumnya","Item-Poin","Poin","Total Poin Terpakai","Total Poin Sisa",
         "Pembayaran Kredit","Ref No Inv","Tot Trans Terakhir","Sisa","Nilai Trans Kredit","Uang Muka","Bayar dgn Retur","Bayar dengan DP",
         " OPEN BILL","Nama Tamu","Meja","Biaya Kartu","Biaya Lain","Catatan Biaya Lain:","*) Nilai Tukar ", "Total Qty", "Swift Code",
         "Perusahaan","Kontak","Alamat","Negara Asal","Telp", "Email", "Dalam Mata Uang","SKU","Barcode","Stn", "Deskripsi","Nama",
         "Dibuat oleh", "Mengetahui", "Menyetujui","Konsumen","Subtotal Per Halaman"},
         {"THANK YOU","   INVOICE","Recipe No","Patient","Doctor","Recipe Service","No","Cashier","Date","Time","Goods","Qty","Amount",
          "Total","Cash","CreditCard","Total Paid", "Change","Number","Holder","Type","Total After Disc","Disc","Price",
          "Sub Total","Tax","Service","Name","Barcode","Point","Exp. Date","Bank","Account No","Debet Card","Cheque","Credit Invoice",
          "      Gift Taking","Prev Point","Item-Point","Point","Total Used Point","Total Point Left",
          "   Credit Payment","Ref Inv","Tot Last Trans","Balance","Credit Invoice Amt","Down Payment","Pay by Return","Bayar dengan DP",
          " OPEN BILL","Guest Name","Cover","CC Cost","Other Cost","Other Cost Note:","*) Rate ", "Total Qty", "Swift Code",
          "Company", "Contact", "Address", "Country of Origin", "Phone", "Email", "Currency In", "SKU", "Barcode","Unit","Description","Name",
          "Prepared By","Checked By","Approved By","Customer","Subtotal Per Page"}
    };
    
    /** Holds value of property startLine. */
    private int startLine = 0;
    
    /** Creates a new instance of PrintInvoicePOS */
    public PrintInvoicePOS() {
    }
    
    
    synchronized public void printInvoiceObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            long oidMemberPublic = CashSaleController.getCustomerNonMember().getOID();
            boolean checkMember = false;
            if(oidMemberPublic==defaultSaleModel.getMainSale().getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            //obj.setTopMargin(1);
            //obj.setLeftMargin(0);
            //obj.setRightMargin(0);
            obj.setObjDescription("Print Invoice Object");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,30, ""+textListHeader[language][TXT_INVOICE]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //column header
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            obj = getItem(obj, start, printObj, rate);
            start = getStartLine();
            //end item
            
            
            //begin total
            obj = getTotal(obj, currUse, start, printObj, rate);
            start = getStartLine();
            //end total
            
            //start member
            if(!checkMember){
                obj = getMember(obj, start, printObj);
                start = getStartLine();
            }
            //end member
            
            if(CashierMainApp.isEnableOpenBill()){
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                obj.newColumn(4,"",cola);
                
                // cover guest data
                String membName = "";
                if(defaultSaleModel.getMainSale().getGuestName().length()>15){
                    membName = defaultSaleModel.getMainSale().getGuestName().substring(0,15);
                }else{
                    membName = defaultSaleModel.getMainSale().getGuestName();
                }
                obj.setColumnValue(1,start, textListHeader[language][TXT_GUEST_NAME],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, ": "+membName,DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(1,start, textListHeader[language][TXT_COVER],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, ": "+defaultSaleModel.getMainSale().getCoverNumber(),DSJ_PrintObj.TEXT_LEFT);
                start++;
                // ---
            }
            
            //start payment
            obj = getPayment(obj, start, printObj, currUse, rate);
            start = getStartLine();
            //end payment
            
            //start total paid
            obj = getTotalPaid(obj, start, printObj, currUse, rate);
            start = getStartLine();
            //end total kembali
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCost(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
            }
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            //obj.setLine(start,1, "\n\n\n\n\n\n\n\n");
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            //prnSvc.running = true;
            //prnSvc.run_x();
            System.out.println("Print invoice finish ...");
            //prnSvc.running=false;
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printHalfInvoiceObj(DefaultSaleModel defaultSaleModel, double pct) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            long oidMemberPublic = CashSaleController.getCustomerNonMember().getOID();
            boolean checkMember = false;
            if(oidMemberPublic==defaultSaleModel.getMainSale().getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            obj.setObjDescription("Print Invoice Object");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,30, ""+textListHeader[language][TXT_INVOICE]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //column header
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            obj = getItemPct(obj, start, printObj, rate,pct);
            start = getStartLine();
            //end item
            
            
            //begin total
            obj = getTotalPct(obj, currUse, start, printObj, rate,pct);
            start = getStartLine();
            //end total
            
            //start member
            if(!checkMember){
                obj = getMember(obj, start, printObj);
                start = getStartLine();
            }
            //end member
            
            if(CashierMainApp.isEnableOpenBill()){
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;
                
                obj.newColumn(4,"",cola);
                
                // cover guest data
                String membName = "";
                if(defaultSaleModel.getMainSale().getGuestName().length()>15){
                    membName = defaultSaleModel.getMainSale().getGuestName().substring(0,15);
                }else{
                    membName = defaultSaleModel.getMainSale().getGuestName();
                }
                obj.setColumnValue(1,start, textListHeader[language][TXT_GUEST_NAME],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, ": "+membName,DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(1,start, textListHeader[language][TXT_COVER],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, ": "+defaultSaleModel.getMainSale().getCoverNumber(),DSJ_PrintObj.TEXT_LEFT);
                start++;
                // ---
            }
            
            //start payment
            obj = getPaymentPct(obj, start, printObj, currUse, rate, pct);
            start = getStartLine();
            //end payment
            
            //start total paid
            obj = getTotalPaidPct(obj, start, printObj, currUse, rate, pct);
            start = getStartLine();
            //end total kembali
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostPct(obj,start,printObj,currUse,1,pct);
                start = getStartLine();
            }
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
            }
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            //obj.setLine(start,1, "\n\n\n\n\n\n\n\n");
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            //prnSvc.running = true;
            //prnSvc.run_x();
            System.out.println("Print invoice finish ...");
            //prnSvc.running=false;
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigInvoiceObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            
            obj.setObjDescription("Print Big Invoice Object "+printObj.getNoInvoice());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            int headLength = 8;
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }                   
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(defaultSaleModel.getMainSale().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_INVOICE].trim(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
                
                headLength++;
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_EMAIL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getEmail(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            //column header
            obj = getItemBig(obj, start, printObj, rate, currUse); 
            start = getStartLine();
            //end item
            
            obj.setHeader(headLength,headLength+2);
            int barisPage = (cashierConfig.itemPerPage*2)+5;
            obj.setPageLength(barisPage);
            obj.setSkipLineIsPaperFix(cashierConfig.printingGap);
            
            
            //qty per kategory
            obj = setCategoryQty(obj, defaultSaleModel.getCategoryQty());
            start = getStartLine();
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostBig(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            // rate
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
            
            
            obj = setLastHeader(obj, printObj);
            start = getStartLine();
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigHalfInvoiceObj(DefaultSaleModel defaultSaleModel, double pct) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            
            obj.setObjDescription("Print Big Invoice Object "+printObj.getNoInvoice());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            int headLength = 8;
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }                   
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(defaultSaleModel.getMainSale().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_INVOICE].trim(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
                
                headLength++;
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_EMAIL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getEmail(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            //column header
            obj = getItemBigPct(obj, start, printObj, rate, currUse, pct); 
            start = getStartLine();
            //end item
            
            obj.setHeader(headLength,headLength+2);
            int barisPage = (cashierConfig.itemPerPage*2)+5;
            obj.setPageLength(barisPage);
            obj.setSkipLineIsPaperFix(cashierConfig.printingGap);
            
            
            //qty per kategory
            obj = setCategoryQty(obj, defaultSaleModel.getCategoryQty());
            start = getStartLine();
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostBig(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            // rate
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
            
            
            obj = setLastHeader(obj, printObj);
            start = getStartLine();
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigReturnInvoiceObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getDataReturn(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int headLength = 10;
            obj.setObjDescription("Print Big Invoice Object "+printObj.getNoInvoice());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(defaultSaleModel.getMainSale().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, "RETURN INVOICE ", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_NO_REF], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +printObj.getNoRefInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;  
            
            //column header
            obj = getItemBig(obj, start, printObj, rate, currUse);
            start = getStartLine();
            //end item
            
            obj.setHeader(headLength,headLength+2); 
            int barisPage = (cashierConfig.itemPerPage*2)+5; 
            obj.setPageLength(barisPage);
            obj.setSkipLineIsPaperFix(cashierConfig.printingGap);
            
             //qty per kategory
            obj = setCategoryQty(obj, defaultSaleModel.getCategoryQty());
            start = getStartLine();
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostBig(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
            
            obj = setLastHeader(obj, printObj);
            start = getStartLine();
                        
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigOpenBillObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int headLength = 10;
            obj.setObjDescription("Print Big Open Bill Object "+printObj.getNoInvoice());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(defaultSaleModel.getMainSale().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_INVOICE].trim(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_EMAIL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getEmail(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
                        
            //column header
            obj = getItemBig(obj, start, printObj, rate, currUse);
            start = getStartLine();
            //end item
            
            obj.setHeader(headLength,headLength+2);
            int barisPage = (cashierConfig.itemPerPage*2)+5; 
            obj.setPageLength(barisPage);
            obj.setSkipLineIsPaperFix(cashierConfig.printingGap);
            
             //qty per kategory
            obj = setCategoryQty(obj, defaultSaleModel.getCategoryQty());
            start = getStartLine();
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostBig(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
                        
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigCreditInvoiceObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int headLength = 10;
            obj.setObjDescription("Print Big Credit Invoice Object "+printObj.getNoInvoice());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(defaultSaleModel.getMainSale().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_INVOICE_CREDIT].trim(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_EMAIL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getEmail(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            //column header
            obj = getItemBig(obj, start, printObj, rate, currUse);
            start = getStartLine();
            //end item
            
            obj.setHeader(headLength,headLength+2);
            int barisPage = (cashierConfig.itemPerPage*2)+5;
            obj.setPageLength(barisPage);
            obj.setSkipLineIsPaperFix(cashierConfig.printingGap);
            
             //qty per kategory
            obj = setCategoryQty(obj, defaultSaleModel.getCategoryQty());
            start = getStartLine();
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostBig(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
            
            obj = setLastHeader(obj, printObj);
            start = getStartLine();
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
                        
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigHalfCreditInvoiceObj(DefaultSaleModel defaultSaleModel, double pct) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int headLength = 10;
            obj.setObjDescription("Print Big Credit Invoice Object "+printObj.getNoInvoice());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(defaultSaleModel.getMainSale().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_INVOICE_CREDIT].trim(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_EMAIL], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getEmail(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            //column header
            obj = getItemBigPct(obj, start, printObj, rate, currUse,pct);
            start = getStartLine();
            //end item
            
            obj.setHeader(headLength,headLength+2);
            int barisPage = (cashierConfig.itemPerPage*2)+5;
            obj.setPageLength(barisPage);
            obj.setSkipLineIsPaperFix(cashierConfig.printingGap);
            
             //qty per kategory
            obj = setCategoryQty(obj, defaultSaleModel.getCategoryQty());
            start = getStartLine();
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCostBigPct(obj,start,printObj,currUse,1,pct);
                start = getStartLine();
            }
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
            
            obj = setLastHeader(obj, printObj);
            start = getStartLine();
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
                        
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printOpenBillPaymentObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 200;
            obj.setPageLength(barisPage);
            //obj.setTopMargin(1);
            //obj.setLeftMargin(0);
            //obj.setRightMargin(0);
            obj.setObjDescription("Print Open Bill Payment");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            
            //begin total
            obj = getTotal(obj, currUse, start, printObj,rate);
            start = getStartLine();
            //end total
            
            //start payment
            obj = getPayment(obj, start, printObj, currUse,rate);
            start = getStartLine();
            //end payment
            
            //start total paid
            obj = getTotalPaid(obj, start, printObj, currUse,rate);
            start = getStartLine();
            //end total kembali
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
            }
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print open bill payment finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printOpenBillObj(DefaultSaleModel defaultSaleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getData(defaultSaleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(defaultSaleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(defaultSaleModel.getMainSale().getCurrencyId()));
            }
            String currUse = currType.getCode();
            double rate = defaultSaleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            long oidMemberPublic = CashSaleController.getCustomerNonMember().getOID();
            boolean checkMember = false;
            if(oidMemberPublic==defaultSaleModel.getMainSale().getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            obj.setObjDescription("Print Open Bill Object");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,30, ""+textListHeader[language][TXT_OPEN_BILL]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //column header
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            obj = getItem(obj, start, printObj,rate);
            start = getStartLine();
            //end item
            
            
            //begin total
            obj = getTotal(obj, currUse, start, printObj,rate);
            start = getStartLine();
            //end total
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.newColumn(4,"",cola);
            
            // cover guest data
            String membName = "";
            if(defaultSaleModel.getMainSale().getGuestName().length()>15){
                membName = defaultSaleModel.getMainSale().getGuestName().substring(0,15);
            }else{
                membName = defaultSaleModel.getMainSale().getGuestName();
            }
            obj.setColumnValue(1,start, textListHeader[language][TXT_GUEST_NAME],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, ": "+membName,DSJ_PrintObj.TEXT_LEFT);
            start++;
            obj.setColumnValue(1,start, textListHeader[language][TXT_COVER],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, ": "+defaultSaleModel.getMainSale().getCoverNumber(),DSJ_PrintObj.TEXT_LEFT);
            start++;
            // ---
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            if((printObj.getTotOtherCost()-printObj.getTotCardCost())>0){
                obj = getOtherCost(obj,start,printObj,currUse,1);
                start = getStartLine();
            }
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print open bill finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    
    //this use to print credit invoice
    synchronized public void printCreditInvoiceObj(DefaultSaleModel saleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BillMain billmain = saleModel.getMainSale();
        PrintInvoiceObj printObj = SessPrintInvoice.getDataCreditInvoice(saleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(saleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(saleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = saleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            long oidMemberPublic = Long.parseLong(cashierConfig.nonMemberId);
            boolean checkMember = false;
            if(oidMemberPublic==billmain.getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            //obj.setTopMargin(1);
            //obj.setLeftMargin(0);
            //obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,23, ""+textListHeader[language][TXT_INVOICE_CREDIT]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //column header
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            obj = getItem(obj, start, printObj,rate);
            start = getStartLine();
            //end item
            
            
            //begin total
            obj = getTotal(obj, currUse, start, printObj,rate);
            start = getStartLine();
            //end total
            
            //start member
            if(!checkMember){
                obj = getMember(obj, start, printObj);
                start = getStartLine();
            }
            
            //end member
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            if(printObj.getTotOtherCost()>0){
                obj = getOtherCost(obj,start,printObj,currUse,rate);
                start = getStartLine();
            }
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
            }
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            // last feed
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    //this use to print credit invoice
    synchronized public void printHalfCreditInvoiceObj(DefaultSaleModel saleModel,double pct) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BillMain billmain = saleModel.getMainSale();
        PrintInvoiceObj printObj = SessPrintInvoice.getDataCreditInvoice(saleModel);
        
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            if(saleModel.getMainSale().getCurrencyId()>0){
                currType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(saleModel.getMainSale().getCurrencyId()));
            }
            
            String currUse = currType.getCode();
            double rate = saleModel.getMainSale().getRate();
            if(rate<1){
                rate = 1;
            }
            
            long oidMemberPublic = Long.parseLong(cashierConfig.nonMemberId);
            boolean checkMember = false;
            if(oidMemberPublic==billmain.getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            //obj.setTopMargin(1);
            //obj.setLeftMargin(0);
            //obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,23, ""+textListHeader[language][TXT_INVOICE_CREDIT]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //column header
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            obj = getItemPct(obj, start, printObj,rate, pct);
            start = getStartLine();
            //end item
            
            
            //begin total
            obj = getTotalPct(obj, currUse, start, printObj,rate, pct);
            start = getStartLine();
            //end total
            
            //start member
            if(!checkMember){
                obj = getMember(obj, start, printObj);
                start = getStartLine();
            }
            
            //end member
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            if(printObj.getTotOtherCost()>0){
                obj = getOtherCostPct(obj,start,printObj,currUse,rate, pct);
                start = getStartLine();
            }
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
            }
            
            // bottom msg
            obj = getBottomMsg(obj,start);
            start = getStartLine();
            
            // last feed
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            System.out.println("Print invoice finish ...");
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    //this use to print gift taking
    //synchronized public void printGiftTakingObj(BillMain billmain) {
    synchronized public void printGiftTakingObj(GiftModel giftModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        
        PrintInvoiceObj printObj = SessPrintInvoice.getDataGiftTaking(giftModel);
        
        BillMain billmain = giftModel.getGiftTrans();
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            
            
            long oidMemberPublic = Long.parseLong(cashierConfig.nonMemberId);
            boolean checkMember = false;
            if(oidMemberPublic==billmain.getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            //obj.setTopMargin(1);
            //obj.setLeftMargin(0);
            //obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,23, ""+textListHeader[language][TXT_GIFT_TAKING]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //start member
            int cola[] = {11,10,19,20};
            obj.newColumn(4,"",cola);
            if(!checkMember){
                obj = getMemberGift(obj, start, printObj);
                start = getStartLine();
            }
            //end member
            
            //begin last prev poin
            
            obj.newColumn(3,"",colb);
            obj.setColumnValue(0,start, textListHeader[language][TXT_PREV_POINT],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start, ""+printObj.getPrevPoint(),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //end lasst prev point
            
            //column header
            int[] cols = {11,10,5,14,20};
            obj.newColumn(5,"",cols);
            obj = getItemPoint(obj, start, printObj);
            start = getStartLine();
            //end item
            
            
            //begin total point
            
            obj.newColumn(3,"",colb);
            obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_POINT],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start, ""+printObj.getTotalPoint(),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //end total point
            
            //begin curr balance poin
            obj.setColumnValue(0,start, textListHeader[language][TXT_CURR_BAL_POINT],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start, ""+printObj.getBalancePoint(),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            //obj.setLine(start,1, "\n\n\n\n\n\n\n\n");
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
            //prnSvc.running = true;
            //prnSvc.run_x();
            System.out.println("Print invoice finish ...");
            //prnSvc.running=false;
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    //this use to print credit payment
    synchronized public void printCreditPaymentObj(CreditPaymentModel creditPaymentModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        //PrintInvoiceObj printObj = SessPrintInvoice.getDataPaymentCreditInvoice(billmain);
        PrintInvoiceObj printObj = SessPrintInvoice.getDataPaymentCreditInvoice(creditPaymentModel);
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = creditPaymentModel.getRateUsed().getSellingRate();
            if(rate<1){
                rate = 1;
            }
            
            long oidMemberPublic = Long.parseLong(cashierConfig.nonMemberId);
            boolean checkMember = false;
            if(oidMemberPublic==creditPaymentModel.getSaleRef().getCustomerId()){
                checkMember = true;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            
            int barisPage = 40;
            obj.setPageLength(barisPage);
            //obj.setTopMargin(1);
            //obj.setLeftMargin(0);
            //obj.setRightMargin(0);
            obj.setObjDescription("Credit Payment Obj");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,23, ""+textListHeader[language][TXT_CREDIT_PAYMENT]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO_REF]+": "+printObj.getNoRefInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"));
            start++;//6
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //begin last trans
            obj.newColumn(3,"",colb);
            obj.setColumnValue(0,start, textListHeader[language][TXT_JML_TRANS_AWAL],DSJ_PrintObj.TEXT_LEFT);
            //obj.setColumnValue(1,start, ""+FRMHandler.userFormatStringDecimalWithoutPoint(printObj.getTotalTransaction ()),DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(printObj.getTotalTransaction()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            //end last trans
            
            if(printObj.getTotCardCostCredit()>0){
                start--;
                obj.newColumn(3,"",colb);
                obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(printObj.getTotCardCostCredit()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;  //1
                
                obj.newColumn(3,"",colb);
                obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start, CashierMainApp.setFormatNumber((printObj.getTotCardCostCredit()+printObj.getTotalTransaction())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                start++;  //1
            }
            
            //begin last trans
            obj.newColumn(3,"",colb);
            obj.setColumnValue(0,start, textListHeader[language][TXT_JML_TRANS_TERAKHIR],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            
            //end last trans
            
            
            //start member
            int cola[] = {11,10,19,20};
            obj.newColumn(4,"",cola);
            if(!checkMember){
                obj = getMemberCredit(obj, start, printObj);
                start = getStartLine();
            }
            //end member
            
            //start payment
            obj = getPaymentCredit(obj, start, printObj, currUse, rate);
            start = getStartLine();
            //end payment
            
            
            //begin total paid
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.newColumn(3,"",colb);
            obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCostCredit())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //begin change
            obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            //end change
            
            //begin balance
            obj.setColumnValue(0,start, textListHeader[language][TXT_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            //obj.setColumnValue(1,start, ""+FRMHandler.userFormatStringDecimalWithoutPoint(printObj.getTotBalance()),DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(printObj.getTotBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
            }
            
            // last feeds
            obj = getLastFeed(obj,start);
            start = getStartLine();
            
            prnSvc.print(obj);
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    synchronized public void printBigCreditPaymentObj(CreditPaymentModel creditPaymentModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        PrintInvoiceObj printObj = SessPrintInvoice.getDataPaymentCreditInvoice(creditPaymentModel);
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = creditPaymentModel.getRateUsed().getSellingRate();
            if(rate<1){
                rate = 1;
            }
            
            int start = PI_COL_START-1;
            //get language
            language=cashierConfig.language;
            
            obj.setPrnIndex(1);
            obj.setObjDescription("Credit Big Payment Obj");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE); // 12 CPI = 96 char /start
            
            //header
            if(cashierConfig.company.length()>0){
                obj.setLine(start,1, ""+cashierConfig.company);
                start++;//3
            }
            
            int[] intCols2 = {65,15,50};
            
            MemberReg member = new MemberReg();
            try{
                member = PstMemberReg.fetchExc(creditPaymentModel.getSaleRef().getCustomerId());
            }
            catch(Exception e){
                System.out.println("err on get contact = "+e.toString());
            }
            
            obj.newColumn(3,"", intCols2);
            
            obj.setColumnValue(0, start,""+cashierConfig.ext1, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_INVOICE].trim(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start,": " +printObj.getNoInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start,""+cashierConfig.ext2, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_TGL]+" "+textListHeader[language][TXT_JAM], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +Formater.formatDate(printObj.getDateInvoice())+" "+Formater.formatDate(printObj.getDateInvoice(),"kk:mm:ss"), DSJ_PrintObj.TEXT_LEFT);
            start++;
                        
            obj.setColumnValue(0, start, "" + cashierConfig.ext3, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_COMPANY], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getCompName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext4, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CONTACT], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getPersonName(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, "" + cashierConfig.ext5, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_ADDRESS], DSJ_PrintObj.TEXT_LEFT);
            if(member.getBussAddress().length()>50){
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(0,50)+"-", DSJ_PrintObj.TEXT_LEFT);
                start++;
                obj.setColumnValue(2, start, ": " +member.getBussAddress().substring(50), DSJ_PrintObj.TEXT_LEFT);
            }
            else{
                obj.setColumnValue(2, start, ": " +member.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);
            }
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext6, DSJ_PrintObj.TEXT_LEFT); 
            obj.setColumnValue(1, start, textListHeader[language][TXT_TLP], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +member.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);
            start++;
            
            obj.setColumnValue(0, start, ""+cashierConfig.ext7, DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, start, textListHeader[language][TXT_NO_REF], DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2, start, ": " +printObj.getNoRefInvoice(), DSJ_PrintObj.TEXT_LEFT);
            start++;            
            
            int[] col5 = {80, 10, 7, 15,18};
            obj.newColumn(5,"",col5);
            
            start++;//2
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;  //1
            
            //begin last trans
            obj.setColumnValue(0,start, textListHeader[language][TXT_JML_TRANS_AWAL],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotalTransaction()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++; //1
            //end last trans
            
            if(printObj.getTotCardCostCredit()>0){
                start--;
                obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotCardCostCredit()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;     //1
                
                obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotCardCostCredit()+printObj.getTotalTransaction())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;  //1
            }
            
            //begin last trans
            obj.setColumnValue(0,start, textListHeader[language][TXT_JML_TRANS_TERAKHIR],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            Vector listPayment = printObj.getListPayment();
            int sizePayment = listPayment.size();
            
            
            if(listPayment!=null&&listPayment.size()>0){
                for(int i=0;i<sizePayment;i++){
                    Vector temp = (Vector)listPayment.get(i);
                    CashPayments cashPayment = (CashPayments)temp.get(0);
                    CurrencyType crType = (CurrencyType)temp.get(1);
                    CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                    
                    if(cashPayment.getAmount()>0){
                        
                        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                        start++; //1
                        
                        int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                        double dAmount = 0;
                        switch(typePayment){
                            case PstCashPayment.CARD :
                                
                                //kartu kredit
                                obj.setColumnValue(0,start, textListHeader[language][TXT_KARTU],DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_KARTU]+" : "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TIPE_KARTU]+" : "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                                
                                break;
                            case PstCashPayment.CHEQUE :
                                
                                //check
                                obj.setColumnValue(0,start,textListHeader[language][TXT_CHECK],DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_ACCOUNT]+ " : "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+" : "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                                
                                break;
                            case PstCashPayment.DEBIT :
                                
                                //debet
                                obj.setColumnValue(0,start,textListHeader[language][TXT_DEBET],DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start,"     "+  textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                obj.setColumnValue(0,start, "     "+textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                                
                                break;
                            default :
                                
                                obj.setColumnValue(0,start,textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                                start++;
                                
                                dAmount = cashPayment.getAmount()/rate;
                                break;
                        }
                        
                        if(!crType.getCode().equals(currUse)){
                            String sign = "x";
                            if(cashPayment.getRate()<1){
                                cashPayment.setRate(1);
                            }
                            String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(), rate);
                            if(cashPayment.getRate()<rate){
                                sign = "/";
                                out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                            }
                            obj.setColumnValue(0,start,"     "+ out,DSJ_PrintObj.TEXT_RIGHT);
                        }
                        else{
                            start--;
                        }
                        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                        start++;
                    }
                }
            }
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++; //1
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCostCredit())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            //begin change
            obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            //end change
            
            //begin balance
            obj.setColumnValue(0,start, textListHeader[language][TXT_BALANCE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotBalance()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            if(rate>1){
                obj.setLine(start,1, ""+textListHeader[language][TXT_RATE]+currUse+" "+CashierMainApp.setFormatNumber(rate,rate));
                start++;
                obj.setLine(start,"");
                start++;
            }
                                    
            prnSvc.print(obj);
        }
        catch (Exception exc) {
            System.out.println("Exc di print obj : "+ exc);
            exc.printStackTrace();
        }
    }
    
    //to print item
    synchronized public DSJ_PrintObj getItem(DSJ_PrintObj obj,int start,PrintInvoiceObj printObj, double rate){
        int[] cols = {11,10,5,14,20};
        obj.newColumn(5,"",cols);
        obj.setColumnValue(0,start, textListHeader[language][TXT_PRC],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start, textListHeader[language][TXT_QTY],DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, textListHeader[language][TXT_JUMLAH],DSJ_PrintObj.TEXT_RIGHT);
        start++;//8
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;//9
        
        //begin item
        Vector listDataDetail = printObj.getListBillDetail();
        int sizeData = listDataDetail.size();
        double totalTransaction = 0;
        double totalQty = 0;
        if(listDataDetail!=null&&listDataDetail.size()>0){
            for(int i=0;i<sizeData;i++){
                Vector vect = (Vector)listDataDetail.get(i);
                Billdetail billdetail = (Billdetail)vect.get(0);
                BillDetailCode billDetailCode = (BillDetailCode)vect.get(1);
                
                String nmProduct = billdetail.getItemName();
                
                nmProduct = billdetail.getSku() + " / " + nmProduct;
                
                Vector vectView = new Vector();
                if(nmProduct.length()>40){
                    StringTokenizer st = new StringTokenizer(nmProduct," ");
                    String stView = "";
                    int rowLength = 0;
                    while(st.hasMoreTokens()){
                        String token = st.nextToken();
                        rowLength = rowLength + token.length()+1;
                        if(rowLength<MAX_LENGTH){
                            stView = stView + token + " ";
                        }
                        else{
                            vectView.add(stView);
                            stView = token + " ";
                            rowLength = token.length() + 1;
                        }
                    }
                    vectView.add(stView);
                }
                else{
                    vectView.add(nmProduct);
                }
                
                for(int j=0; j< vectView.size(); j++){
                    nmProduct = (String) vectView.get(j);
                    obj.setLine(start,1, nmProduct);
                    start++;
                }
                
                String serialNo = billDetailCode.getStockCode();
                
                if(serialNo!=null&&serialNo.length()>0){
                    obj.setLine(start,1, "SN : "+serialNo);
                    start++;//10
                }
                String sQty = String.valueOf(billdetail.getQty());
                if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                    sQty = sQty.replace('.',',');
                obj.setColumnValue(0,start, CashierMainApp.setFormatNumber(billdetail.getItemPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(billdetail.getDisc()/rate*(billdetail.getQty()>0?billdetail.getQty():1), rate),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(billdetail.getTotalPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                
                start++;//11
                totalTransaction = totalTransaction + billdetail.getTotalPrice()/rate;
                totalQty = totalQty + billdetail.getQty();
            }
        }
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;//9
        
        String sQty = String.valueOf(totalQty);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            sQty = sQty.replace('.',',');
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_QTY],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print item
    synchronized public DSJ_PrintObj getItemPct(DSJ_PrintObj obj,int start,PrintInvoiceObj printObj, double rate, double pct){
        int[] cols = {11,10,5,14,20};
        obj.newColumn(5,"",cols);
        obj.setColumnValue(0,start, textListHeader[language][TXT_PRC],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start, textListHeader[language][TXT_QTY],DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, textListHeader[language][TXT_JUMLAH],DSJ_PrintObj.TEXT_RIGHT);
        start++;//8
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;//9
        
        //begin item
        Vector listDataDetail = printObj.getListBillDetail();
        int sizeData = listDataDetail.size();
        double totalTransaction = 0;
        double totalQty = 0;
        if(listDataDetail!=null&&listDataDetail.size()>0){
            for(int i=0;i<sizeData;i++){
                Vector vect = (Vector)listDataDetail.get(i);
                Billdetail billdetail = (Billdetail)vect.get(0);
                BillDetailCode billDetailCode = (BillDetailCode)vect.get(1);
                
                String nmProduct = billdetail.getItemName();
                
                nmProduct = billdetail.getSku() + " / " + nmProduct;
                
                Vector vectView = new Vector();
                if(nmProduct.length()>40){
                    StringTokenizer st = new StringTokenizer(nmProduct," ");
                    String stView = "";
                    int rowLength = 0;
                    while(st.hasMoreTokens()){
                        String token = st.nextToken();
                        rowLength = rowLength + token.length()+1;
                        if(rowLength<MAX_LENGTH){
                            stView = stView + token + " ";
                        }
                        else{
                            vectView.add(stView);
                            stView = token + " ";
                            rowLength = token.length() + 1;
                        }
                    }
                    vectView.add(stView);
                }
                else{
                    vectView.add(nmProduct);
                }
                
                for(int j=0; j< vectView.size(); j++){
                    nmProduct = (String) vectView.get(j);
                    obj.setLine(start,1, nmProduct);
                    start++;
                }
                
                String serialNo = billDetailCode.getStockCode();
                
                if(serialNo!=null&&serialNo.length()>0){
                    obj.setLine(start,1, "SN : "+serialNo);
                    start++;//10
                }
                String sQty = String.valueOf(billdetail.getQty());
                if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                    sQty = sQty.replace('.',',');
                billdetail.setItemPrice(billdetail.getItemPrice()*pct/100);
                billdetail.setDisc(billdetail.getDisc()*pct/100);
                billdetail.setTotalPrice(billdetail.getTotalPrice()*pct/100);
                obj.setColumnValue(0,start, CashierMainApp.setFormatNumber(billdetail.getItemPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(billdetail.getDisc()/rate*(billdetail.getQty()>0?billdetail.getQty():1), rate),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(billdetail.getTotalPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                
                start++;//11
                totalTransaction = totalTransaction + billdetail.getTotalPrice()/rate;
                totalQty = totalQty + billdetail.getQty();
            }
        }
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;//9
        
        String sQty = String.valueOf(totalQty);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            sQty = sQty.replace('.',',');
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_QTY],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print item
    synchronized public DSJ_PrintObj getItemBig(DSJ_PrintObj obj,int start,PrintInvoiceObj printObj, double rate, String currUse){
        
        obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
        start++;
        
        int[] intCols = {5, 25, 25,25, 10, 7, 15,18};
        int[] col5 = {80, 10, 7, 15,18};
        obj.newColumn(8,"|",intCols);
        obj.setColumnValue(0,start, textListHeader[language][TXT_NO].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(1,start, textListHeader[language][TXT_SKU].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(2,start, textListHeader[language][TXT_NAMA].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(3,start, textListHeader[language][TXT_DESCRIPTION].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(4,start, textListHeader[language][TXT_QTY].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(5,start, textListHeader[language][TXT_STN].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(6,start, textListHeader[language][TXT_PRC].toUpperCase()+" ("+currUse+")",DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(7,start, textListHeader[language][TXT_JUMLAH].toUpperCase()+" ("+currUse+")",DSJ_PrintObj.TEXT_CENTER);
        start++;//8
        
        obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
        start++;
        
        //begin item
        Vector listDataDetail = printObj.getListBillDetail();
        int sizeData = listDataDetail.size();
        double totalTransaction = 0;
        double totalQty = 0;
        if(listDataDetail!=null&&listDataDetail.size()>0){
            double totalPerPage = 0;
            double qtyPerPage = 0;
            for(int i=0;i<sizeData;i++){
                Vector vect = (Vector)listDataDetail.get(i);
                Billdetail billdetail = (Billdetail)vect.get(0);
                BillDetailCode billDetailCode = (BillDetailCode)vect.get(1);
                
                String nmProduct = billdetail.getItemName();
                String serialNo = billDetailCode.getStockCode();
                String nmUnit = billdetail.getSku().substring(billdetail.getSku().length()-3,billdetail.getSku().length());
                String sQty = String.valueOf(billdetail.getQty());
                if(sQty.substring(sQty.length()-2).equals(".0")){
                    if(CashierMainApp.getVectFixUnit()!=null && CashierMainApp.getVectFixUnit().size()>0){
                        if(!CashierMainApp.getVectFixUnit().contains(nmUnit)){
                            nmUnit = (String) CashierMainApp.getVectFixUnit().get(0); 
                        }
                    }
                    else{
                        nmUnit = "PCS";
                    }
                }
                else{
                    if(CashierMainApp.getVectFloatingUnit()!=null && CashierMainApp.getVectFloatingUnit().size()>0){
                        if(!CashierMainApp.getVectFloatingUnit().contains(nmUnit)){
                            nmUnit = (String) CashierMainApp.getVectFloatingUnit().get(0); 
                        }
                    }
                    else{
                        nmUnit = "PCS";
                    }
                }
                if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                    sQty = sQty.replace('.',',');
                
                obj.setColumnValue(0,start, ""+(i+1),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(1,start, billdetail.getSku(),DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, nmProduct,DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(3,start, serialNo,DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(5,start, nmUnit,DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(6,start, CashierMainApp.setFormatNumber(billdetail.getItemPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(7,start, CashierMainApp.setFormatNumber(billdetail.getTotalPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;//11
                
                totalPerPage = totalPerPage + billdetail.getTotalPrice()/rate;
                qtyPerPage = qtyPerPage + billdetail.getQty();
                int marker = cashierConfig.itemPerPage-4;
                if((i+1-marker)%cashierConfig.itemPerPage==0){ 
                    obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                    start++;
                    String sQtyPerPage = String.valueOf(qtyPerPage);
                    if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                        sQtyPerPage = sQtyPerPage.replace('.', ',');
                    obj.newColumn(5,"|",col5);
                    obj.setColumnValue(0,start, textListHeader[language][TXT_SUBTOTAL_PER_PAGE],DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1,start, sQtyPerPage,DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(totalPerPage, rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                    start++;
                    
                    totalPerPage = 0; 
                    qtyPerPage = 0;
                    obj.newColumn(8,"|",intCols);
                }
                else{
                    obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                    start++;
                }
                
                totalTransaction = totalTransaction + billdetail.getTotalPrice()/rate;
                totalQty = totalQty + billdetail.getQty();
            }
            start--;
            obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
            start++;
            if(listDataDetail.size()>cashierConfig.itemPerPage-5){
                String sQtyPerPage = String.valueOf(qtyPerPage);
                if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                    sQtyPerPage = sQtyPerPage.replace('.', ',');
                obj.newColumn(5,"|",col5);
                obj.setColumnValue(0,start, textListHeader[language][TXT_SUBTOTAL_PER_PAGE],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start, sQtyPerPage,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(totalPerPage, rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
            }
        }
        
        obj.endHeader(start);        
        
        String sQty = String.valueOf(totalQty);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            sQty = sQty.replace('.',',');
        
        
        obj.newColumn(5,"|",col5);
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getSubTotal()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        if(printObj.getDiscount()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getDiscount()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        
        if(printObj.getService()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_SERVICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getService()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getTax()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_TAX],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTax()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        if(printObj.getTotOtherCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotOtherCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        if(printObj.getTotCardCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotCardCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        start--;
        obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
        start++;
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotalTransaction())/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        
        if(printObj.getTotLastPayment()>0){
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        Vector listPayment = printObj.getListPayment();
        int sizePayment = listPayment.size();
        
        
        if(listPayment!=null&&listPayment.size()>0){
            for(int i=0;i<sizePayment;i++){
                Vector temp = (Vector)listPayment.get(i);
                CashPayments cashPayment = (CashPayments)temp.get(0);
                CurrencyType crType = (CurrencyType)temp.get(1);
                CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                double dAmount = 0;
                switch(typePayment){
                    case PstCashPayment.CARD :
                        
                        //kartu kredit
                        obj.setColumnValue(0,start, textListHeader[language][TXT_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_KARTU]+" : "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TIPE_KARTU]+" : "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.CHEQUE :
                        
                        //check
                        obj.setColumnValue(0,start,textListHeader[language][TXT_CHECK],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_ACCOUNT]+ " : "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+" : "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.DEBIT :
                        
                        //debet
                        obj.setColumnValue(0,start,textListHeader[language][TXT_DEBET],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+  textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start, "     "+textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    default :
                        
                        obj.setColumnValue(0,start,textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = cashPayment.getAmount()/rate;
                        break;
                }
                
                if(!crType.getCode().equals(currUse)){
                    String sign = "x";
                    if(cashPayment.getRate()<1){
                        cashPayment.setRate(1);
                    }
                    String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),cashPayment.getRate())+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(), rate);
                    if(cashPayment.getRate()<rate){
                        sign = "/";
                        out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                    }
                    obj.setColumnValue(0,start,"     "+ out,DSJ_PrintObj.TEXT_RIGHT);
                }
                else{
                    start--;
                }
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
            }
            
            obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
            start++;
            
            if(printObj.getTotalPaid()>0){
                obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                //end total paid
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                //start total kembali
                obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
            }
        }
        else{
            obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
            start++;
        }
        
        setStartLine(start);
        return obj;
    }
    
    //to print item
    synchronized public DSJ_PrintObj getItemBigPct(DSJ_PrintObj obj,int start,PrintInvoiceObj printObj, double rate, String currUse, double pct){
        
        obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
        start++;
        
        int[] intCols = {5, 25, 25,25, 10, 7, 15,18};
        int[] col5 = {80, 10, 7, 15,18};
        obj.newColumn(8,"|",intCols);
        obj.setColumnValue(0,start, textListHeader[language][TXT_NO].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(1,start, textListHeader[language][TXT_SKU].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(2,start, textListHeader[language][TXT_NAMA].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(3,start, textListHeader[language][TXT_DESCRIPTION].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(4,start, textListHeader[language][TXT_QTY].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(5,start, textListHeader[language][TXT_STN].toUpperCase(),DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(6,start, textListHeader[language][TXT_PRC].toUpperCase()+" ("+currUse+")",DSJ_PrintObj.TEXT_CENTER);
        obj.setColumnValue(7,start, textListHeader[language][TXT_JUMLAH].toUpperCase()+" ("+currUse+")",DSJ_PrintObj.TEXT_CENTER);
        start++;//8
        
        obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
        start++;
        
        //begin item
        Vector listDataDetail = printObj.getListBillDetail();
        int sizeData = listDataDetail.size();
        double totalTransaction = 0;
        double totalQty = 0;
        if(listDataDetail!=null&&listDataDetail.size()>0){
            double totalPerPage = 0;
            double qtyPerPage = 0;
            for(int i=0;i<sizeData;i++){
                Vector vect = (Vector)listDataDetail.get(i);
                Billdetail billdetail = (Billdetail)vect.get(0);
                BillDetailCode billDetailCode = (BillDetailCode)vect.get(1);
                
                billdetail.setItemPrice(billdetail.getItemPrice()*pct/100);
                billdetail.setDisc(billdetail.getDisc()*pct/100);
                billdetail.setTotalPrice(billdetail.getTotalPrice()*pct/100);
                String nmProduct = billdetail.getItemName();
                String serialNo = billDetailCode.getStockCode();
                String nmUnit = billdetail.getSku().substring(billdetail.getSku().length()-3,billdetail.getSku().length());
                String sQty = String.valueOf(billdetail.getQty());
                if(sQty.substring(sQty.length()-2).equals(".0")){
                    if(CashierMainApp.getVectFixUnit()!=null && CashierMainApp.getVectFixUnit().size()>0){
                        if(!CashierMainApp.getVectFixUnit().contains(nmUnit)){
                            nmUnit = (String) CashierMainApp.getVectFixUnit().get(0); 
                        }
                    }
                    else{
                        nmUnit = "PCS";
                    }
                }
                else{
                    if(CashierMainApp.getVectFloatingUnit()!=null && CashierMainApp.getVectFloatingUnit().size()>0){
                        if(!CashierMainApp.getVectFloatingUnit().contains(nmUnit)){
                            nmUnit = (String) CashierMainApp.getVectFloatingUnit().get(0); 
                        }
                    }
                    else{
                        nmUnit = "PCS";
                    }
                }
                if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                    sQty = sQty.replace('.',',');
                
                obj.setColumnValue(0,start, ""+(i+1),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(1,start, billdetail.getSku(),DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, nmProduct,DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(3,start, serialNo,DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(5,start, nmUnit,DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(6,start, CashierMainApp.setFormatNumber(billdetail.getItemPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(7,start, CashierMainApp.setFormatNumber(billdetail.getTotalPrice()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;//11
                
                totalPerPage = totalPerPage + billdetail.getTotalPrice()/rate;
                qtyPerPage = qtyPerPage + billdetail.getQty();
                int marker = cashierConfig.itemPerPage-4;
                if((i+1-marker)%cashierConfig.itemPerPage==0){ 
                    obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                    start++;
                    String sQtyPerPage = String.valueOf(qtyPerPage);
                    if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                        sQtyPerPage = sQtyPerPage.replace('.', ',');
                    obj.newColumn(5,"|",col5);
                    obj.setColumnValue(0,start, textListHeader[language][TXT_SUBTOTAL_PER_PAGE],DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1,start, sQtyPerPage,DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(totalPerPage, rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                    obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                    start++;
                    
                    totalPerPage = 0; 
                    qtyPerPage = 0;
                    obj.newColumn(8,"|",intCols);
                }
                else{
                    obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                    start++;
                }
                
                totalTransaction = totalTransaction + billdetail.getTotalPrice()/rate;
                totalQty = totalQty + billdetail.getQty();
            }
            start--;
            obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
            start++;
            if(listDataDetail.size()>cashierConfig.itemPerPage-5){
                String sQtyPerPage = String.valueOf(qtyPerPage);
                if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                    sQtyPerPage = sQtyPerPage.replace('.', ',');
                obj.newColumn(5,"|",col5);
                obj.setColumnValue(0,start, textListHeader[language][TXT_SUBTOTAL_PER_PAGE],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(1,start, sQtyPerPage,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(totalPerPage, rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
            }
        }
        
        obj.endHeader(start);        
        
        String sQty = String.valueOf(totalQty);
        if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
            sQty = sQty.replace('.',',');
        
        
        obj.newColumn(5,"|",col5);
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(1,start, sQty,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getSubTotal()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        if(printObj.getDiscount()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getDiscount()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        
        if(printObj.getService()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_SERVICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getService()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getTax()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_TAX],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTax()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        printObj.setTotOtherCost(printObj.getTotOtherCost()*pct/100);
        if(printObj.getTotOtherCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotOtherCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        printObj.setTotCardCost(printObj.getTotCardCost()*pct/100);
        if(printObj.getTotCardCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotCardCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
        }
        
        start--;
        obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
        start++;
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotalTransaction())/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        
        if(printObj.getTotLastPayment()>0){
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        Vector listPayment = printObj.getListPayment();
        int sizePayment = listPayment.size();
        
        
        if(listPayment!=null&&listPayment.size()>0){
            for(int i=0;i<sizePayment;i++){
                Vector temp = (Vector)listPayment.get(i);
                CashPayments cashPayment = (CashPayments)temp.get(0);
                CurrencyType crType = (CurrencyType)temp.get(1);
                CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                cashPayment.setAmount(cashPayment.getAmount()*pct/100);
                int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                double dAmount = 0;
                switch(typePayment){
                    case PstCashPayment.CARD :
                        
                        //kartu kredit
                        obj.setColumnValue(0,start, textListHeader[language][TXT_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_KARTU]+" : "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TIPE_KARTU]+" : "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.CHEQUE :
                        
                        //check
                        obj.setColumnValue(0,start,textListHeader[language][TXT_CHECK],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_ACCOUNT]+ " : "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+" : "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.DEBIT :
                        
                        //debet
                        obj.setColumnValue(0,start,textListHeader[language][TXT_DEBET],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+  textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start, "     "+textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    default :
                        
                        obj.setColumnValue(0,start,textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = cashPayment.getAmount()/rate;
                        break;
                }
                
                if(!crType.getCode().equals(currUse)){
                    String sign = "x";
                    if(cashPayment.getRate()<1){
                        cashPayment.setRate(1);
                    }
                    String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),cashPayment.getRate())+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(), rate);
                    if(cashPayment.getRate()<rate){
                        sign = "/";
                        out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                    }
                    obj.setColumnValue(0,start,"     "+ out,DSJ_PrintObj.TEXT_RIGHT);
                }
                else{
                    start--;
                }
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
            }
            
            obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
            start++;
            
            if(printObj.getTotalPaid()>0){
                obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                //end total paid
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
                
                //start total kembali
                obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
            }
        }
        else{
            obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
            start++;
        }
        
        setStartLine(start);
        return obj;
    }
    
    synchronized public DSJ_PrintObj getCreditPaymentBig(DSJ_PrintObj obj,int start,PrintInvoiceObj printObj, double rate, String currUse){
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        int[] col5 = {80, 10, 7, 15,18};
        obj.newColumn(5,"",col5);
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_JML_TRANS_AWAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotalTransaction()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getTotCardCostCredit()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotCardCostCredit()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotCardCostCredit()+printObj.getTotalTransaction())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;  //1
        }
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_JML_TRANS_TERAKHIR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        if(printObj.getTotLastPayment()>0){
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        Vector listPayment = printObj.getListPayment();
        int sizePayment = listPayment.size();
        
        
        if(listPayment!=null&&listPayment.size()>0){
            for(int i=0;i<sizePayment;i++){
                Vector temp = (Vector)listPayment.get(i);
                CashPayments cashPayment = (CashPayments)temp.get(0);
                CurrencyType crType = (CurrencyType)temp.get(1);
                CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                
                int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                double dAmount = 0;
                switch(typePayment){
                    case PstCashPayment.CARD :
                        
                        //kartu kredit
                        obj.setColumnValue(0,start, textListHeader[language][TXT_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_KARTU]+" : "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TIPE_KARTU]+" : "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.CHEQUE :
                        
                        //check
                        obj.setColumnValue(0,start,textListHeader[language][TXT_CHECK],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NO_ACCOUNT]+ " : "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_TGL_BERLAKU]+" : "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+ textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.DEBIT :
                        
                        //debet
                        obj.setColumnValue(0,start,textListHeader[language][TXT_DEBET],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start,"     "+  textListHeader[language][TXT_NAMA_BANK]+" : "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(0,start, "     "+textListHeader[language][TXT_HOLDER]+" : "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    default :
                        
                        obj.setColumnValue(0,start,textListHeader[language][TXT_TUNAI],DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = cashPayment.getAmount()/rate;
                        break;
                }
                
                if(!crType.getCode().equals(currUse)){
                    String sign = "x";
                    if(cashPayment.getRate()<1){
                        cashPayment.setRate(1);
                    }
                    String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(), rate);
                    if(cashPayment.getRate()<rate){
                        sign = "/";
                        out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                    }
                    obj.setColumnValue(0,start,"     "+ out,DSJ_PrintObj.TEXT_RIGHT);
                }
                else{
                    start--;
                }
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
            }
            
            obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
            start++;
            
            if(printObj.getTotalPaid()>0){
                obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                //end total paid
                
                //start total kembali
                obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(4,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
                
                obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
                start++;
            }
        }
        
        setStartLine(start);
        return obj;
    }
    
    //to print item point
    public DSJ_PrintObj getItemPoint(DSJ_PrintObj obj,int start,PrintInvoiceObj printObj){
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
        start++;//7
        
        int[] cols = {11,10,5,14,20};
        obj.newColumn(5,"",cols);
        obj.setColumnValue(1,start, textListHeader[language][TXT_ITEM_POINT],DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(2,start, textListHeader[language][TXT_QTY],DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, textListHeader[language][TXT_POINT],DSJ_PrintObj.TEXT_RIGHT);
        start++;//8
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;//9
        
        //begin item
        Vector listDataDetail = printObj.getListBillDetail();
        int sizeData = listDataDetail.size();
        double totalTransaction = 0;
        
        if(listDataDetail!=null&&listDataDetail.size()>0){
            for(int i=0;i<sizeData;i++){
                GiftItem giftItem = (GiftItem)listDataDetail.get(i);
                String nmProduct = giftItem.getName();
                if(nmProduct.length()>40){
                    nmProduct = nmProduct.substring(0,39);
                }
                
                String serialNo = giftItem.getSerialCode();
                obj.setLine(start,1, nmProduct);
                start++;//10
                
                if(serialNo!=null&&serialNo.length()>0){
                    obj.setLine(start,1, "SN : "+serialNo);
                    start++;//10
                }
                
                obj.setColumnValue(1,start, ""+giftItem.getPoint(),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start, ""+giftItem.getAmount(),DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(3,start, ""+(giftItem.getPoint()*giftItem.getAmount()),DSJ_PrintObj.TEXT_RIGHT);
                start++;//11
            }
        }
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;//9
        
        setStartLine(start);
        return obj;
    }
    
    //to print total
    synchronized public DSJ_PrintObj getTotal(DSJ_PrintObj obj, String currUse,int start, PrintInvoiceObj printObj, double rate ){
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getSubTotal()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getDiscount()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getDiscount()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        
        if(printObj.getService()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_SERVICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getService()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getTax()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_TAX],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTax()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getTotOtherCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotOtherCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getTotCardCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotCardCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber((printObj.getTotalTransaction())/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print total
    synchronized public DSJ_PrintObj getTotalPct(DSJ_PrintObj obj, String currUse,int start, PrintInvoiceObj printObj, double rate, double pct ){
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getSubTotal()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        if(printObj.getDiscount()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getDiscount()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        
        if(printObj.getService()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_SERVICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getService()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        if(printObj.getTax()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_TAX],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTax()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        printObj.setTotOtherCost(printObj.getTotOtherCost()*pct/100);
        if(printObj.getTotOtherCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_OTHER_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotOtherCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        printObj.setTotCardCost(printObj.getTotCardCost()*pct/100);
        if(printObj.getTotCardCost()>0){
            obj.setColumnValue(0,start, textListHeader[language][TXT_CARD_COST],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotCardCost()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber((printObj.getTotalTransaction())/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        setStartLine(start);
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
    
    //to print member
    public DSJ_PrintObj getMember(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj){
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        obj.setLine(start,1, "Member");
        start++;
        
        obj.newColumn(4,"",cola);
        
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_BARCODE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+printObj.getMemberBarcode(),DSJ_PrintObj.TEXT_LEFT);
        start++;
        String membName = "";
        if(printObj.getMemberName().length()>15){
            membName = printObj.getMemberName().substring(0,15);
        }else{
            membName = printObj.getMemberName();
        }
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_NAME],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+membName,DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_POINT],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+printObj.getMemberPoint(),DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print member
    public DSJ_PrintObj getMemberGift(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj){
        
        
        obj.setLine(start,1, "Member");
        start++;
        
        obj.newColumn(4,"",cola);
        
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_BARCODE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+printObj.getMemberBarcode(),DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_NAME],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+printObj.getMemberName(),DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    public DSJ_PrintObj getMemberCredit(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj){
        
        
        obj.setLine(start,1, "Member");
        start++;
        
        obj.newColumn(4,"",cola);
        
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_BARCODE],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+printObj.getMemberBarcode(),DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        obj.setColumnValue(1,start, textListHeader[language][TXT_MEMBER_NAME],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, ": "+printObj.getMemberName(),DSJ_PrintObj.TEXT_LEFT);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print member
    public DSJ_PrintObj getOtherCost(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj, String currUse, double rate){
        
        Vector listObj = printObj.getListOtherCost();
        int sizeObj = listObj.size();
        String stListObj = textListHeader[language][TXT_OTHER_COST_NOTE];
        for(int i = 0; i < sizeObj; i++){
            OtherCost otherCost = (OtherCost) listObj.get(i);
            if(i==0){
                stListObj = stListObj+" "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
            else{
                stListObj = stListObj+" + "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
        }
        StringTokenizer st = new StringTokenizer(stListObj," ");
        String stView = "";
        Vector vectView = new Vector();
        int rowLength = 0;
        while(st.hasMoreTokens()){
            String token = st.nextToken();
            rowLength = rowLength + token.length()+1;
            if(rowLength<MAX_LENGTH){
                stView = stView + token + " ";
            }
            else{
                System.out.println(".......ini stView: "+stView);
                vectView.add(stView);
                stView = token + " ";
                rowLength = token.length() + 1;
            }
            
        }
        System.out.println(".......ini stView: "+stView);
        vectView.add(stView);
        
        for(int i = 0; i <vectView.size();i++){
            String outView = (String) vectView.get(i);
            obj.setLine(start,1, outView);
            start++;
        }
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print member
    public DSJ_PrintObj getOtherCostPct(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj, String currUse, double rate, double pct){
        
        Vector listObj = printObj.getListOtherCost();
        int sizeObj = listObj.size();
        String stListObj = textListHeader[language][TXT_OTHER_COST_NOTE];
        for(int i = 0; i < sizeObj; i++){
            OtherCost otherCost = (OtherCost) listObj.get(i);
            otherCost.setAmount(otherCost.getAmount()*pct/100);
            if(i==0){
                stListObj = stListObj+" "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
            else{
                stListObj = stListObj+" + "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
        }
        StringTokenizer st = new StringTokenizer(stListObj," ");
        String stView = "";
        Vector vectView = new Vector();
        int rowLength = 0;
        while(st.hasMoreTokens()){
            String token = st.nextToken();
            rowLength = rowLength + token.length()+1;
            if(rowLength<MAX_LENGTH){
                stView = stView + token + " ";
            }
            else{
                System.out.println(".......ini stView: "+stView);
                vectView.add(stView);
                stView = token + " ";
                rowLength = token.length() + 1;
            }
            
        }
        System.out.println(".......ini stView: "+stView);
        vectView.add(stView);
        
        for(int i = 0; i <vectView.size();i++){
            String outView = (String) vectView.get(i);
            obj.setLine(start,1, outView);
            start++;
        }
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    public DSJ_PrintObj getOtherCostBig(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj, String currUse, double rate){
        
        Vector listObj = printObj.getListOtherCost();
        int sizeObj = listObj.size();
        String stListObj = textListHeader[language][TXT_OTHER_COST_NOTE];
        for(int i = 0; i < sizeObj; i++){
            OtherCost otherCost = (OtherCost) listObj.get(i);
            if(i==0){
                stListObj = stListObj+" "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
            else{
                stListObj = stListObj+" + "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
        }
        
        obj.setLine(start,1, stListObj);
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    public DSJ_PrintObj getOtherCostBigPct(DSJ_PrintObj obj, int start, PrintInvoiceObj printObj, String currUse, double rate, double pct){
        
        Vector listObj = printObj.getListOtherCost();
        int sizeObj = listObj.size();
        String stListObj = textListHeader[language][TXT_OTHER_COST_NOTE];
        for(int i = 0; i < sizeObj; i++){
            OtherCost otherCost = (OtherCost) listObj.get(i);
            otherCost.setAmount(otherCost.getAmount()*pct/100);
            if(i==0){
                stListObj = stListObj+" "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
            else{
                stListObj = stListObj+" + "+otherCost.getName()+" "+
                currUse+"."+CashierMainApp.setFormatNumber(otherCost.getAmount()/rate, rate)+" ";
            }
        }
        
        obj.setLine(start,1, stListObj);
        start++;
        
        obj.setLineRptStr(start, 0, "-", obj.getCharacterSelected());
        start++;
        
        setStartLine(start);
        return obj;
    }
    
    //to print payment
    public DSJ_PrintObj getPayment(DSJ_PrintObj obj,int start, PrintInvoiceObj printObj,String currUse, double rate){
        
        if(printObj.getTotLastPayment()>0){
            obj.newColumn(5,"",cols);
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        Vector listPayment = printObj.getListPayment();
        int sizePayment = listPayment.size();
        
        
        if(listPayment!=null&&listPayment.size()>0){
            for(int i=0;i<sizePayment;i++){
                Vector temp = (Vector)listPayment.get(i);
                CashPayments cashPayment = (CashPayments)temp.get(0);
                CurrencyType crType = (CurrencyType)temp.get(1);
                CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                
                int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                double dAmount = 0;
                switch(typePayment){
                    case PstCashPayment.CARD :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.newColumn(4,"",cola);
                        
                        //kartu kredit
                        obj.setLine(start,1, textListHeader[language][TXT_KARTU]);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NO_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_TIPE_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_HOLDER],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_TGL_BERLAKU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.CHEQUE :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.newColumn(4,"",cola);
                        
                        //check
                        obj.setLine(start,1, textListHeader[language][TXT_CHECK]);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NO_ACCOUNT],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_TGL_BERLAKU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NAMA_BANK],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.DEBIT :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.newColumn(4,"",cola);
                        
                        //debet
                        obj.setLine(start,1, textListHeader[language][TXT_DEBET]);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NAMA_BANK],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_HOLDER],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    default :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.setLine(start,1, textListHeader[language][TXT_TUNAI]);
                        start++;
                        
                        dAmount = cashPayment.getAmount()/rate;
                        break;
                }
                
                
                obj.newColumn(4,"",colp);
                if(!crType.getCode().equals(currUse)){
                    String sign = "x";
                    if(cashPayment.getRate()<1){
                        cashPayment.setRate(1);
                    }
                    String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),cashPayment.getRate())+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(), rate);
                    if(cashPayment.getRate()<rate){
                        sign = "/";
                        out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                    }
                    
                    if(out.length()>20){ // lebih dari panjang column
                        obj.setLine(start,1,out);
                        start++;
                        obj.newColumn(4,"",colp);
                    }
                    else{
                        obj.setColumnValue(0,start,out,DSJ_PrintObj.TEXT_RIGHT);
                    }
                }
                obj.setColumnValue(1,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
            }
        }
        setStartLine(start);
        return obj;
    }
    
    //to print payment
    public DSJ_PrintObj getPaymentPct(DSJ_PrintObj obj,int start, PrintInvoiceObj printObj,String currUse, double rate, double pct){
        
        if(printObj.getTotLastPayment()>0){
            obj.newColumn(5,"",cols);
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_DP],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotLastPayment()/rate, rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
        }
        
        Vector listPayment = printObj.getListPayment();
        int sizePayment = listPayment.size();
        
        
        if(listPayment!=null&&listPayment.size()>0){
            for(int i=0;i<sizePayment;i++){
                Vector temp = (Vector)listPayment.get(i);
                CashPayments cashPayment = (CashPayments)temp.get(0);
                CurrencyType crType = (CurrencyType)temp.get(1);
                CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                
                cashPayment.setAmount(cashPayment.getAmount()*pct/100);
                int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                double dAmount = 0;
                switch(typePayment){
                    case PstCashPayment.CARD :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.newColumn(4,"",cola);
                        
                        //kartu kredit
                        obj.setLine(start,1, textListHeader[language][TXT_KARTU]);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NO_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_TIPE_KARTU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_HOLDER],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_TGL_BERLAKU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.CHEQUE :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.newColumn(4,"",cola);
                        
                        //check
                        obj.setLine(start,1, textListHeader[language][TXT_CHECK]);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NO_ACCOUNT],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_TGL_BERLAKU],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NAMA_BANK],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    case PstCashPayment.DEBIT :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.newColumn(4,"",cola);
                        
                        //debet
                        obj.setLine(start,1, textListHeader[language][TXT_DEBET]);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_NAMA_BANK],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        obj.setColumnValue(1,start, textListHeader[language][TXT_HOLDER],DSJ_PrintObj.TEXT_LEFT);
                        obj.setColumnValue(2,start, ": "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                        start++;
                        
                        dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                        
                        break;
                    default :
                        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                        start++;
                        
                        obj.setLine(start,1, textListHeader[language][TXT_TUNAI]);
                        start++;
                        
                        dAmount = cashPayment.getAmount()/rate;
                        break;
                }
                
                
                obj.newColumn(4,"",colp);
                if(!crType.getCode().equals(currUse)){
                    String sign = "x";
                    if(cashPayment.getRate()<1){
                        cashPayment.setRate(1);
                    }
                    String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),cashPayment.getRate())+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(), rate);
                    if(cashPayment.getRate()<rate){
                        sign = "/";
                        out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                    }
                    
                    if(out.length()>20){ // lebih dari panjang column
                        obj.setLine(start,1,out);
                        start++;
                        obj.newColumn(4,"",colp);
                    }
                    else{
                        obj.setColumnValue(0,start,out,DSJ_PrintObj.TEXT_RIGHT);
                    }
                }
                obj.setColumnValue(1,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(2,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
            }
        }
        setStartLine(start);
        return obj;
    }
    
    public DSJ_PrintObj getPaymentCredit(DSJ_PrintObj obj,int start, PrintInvoiceObj printObj,String currUse, double rate){
        Vector listPayment = printObj.getListPayment();
        int sizePayment = listPayment.size();
        
        if(listPayment!=null&&listPayment.size()>0){
            for(int i=0;i<sizePayment;i++){
                Vector temp = (Vector)listPayment.get(i);
                CashPayments cashPayment = (CashPayments)temp.get(0);
                CurrencyType crType = (CurrencyType)temp.get(1);
                CashCreditCard cashInfo = (CashCreditCard)temp.get(2);
                if(cashPayment.getAmount()>0){
                    int typePayment = cashPayment.getPaymentType();//PstCashPayment.CASH;
                    double dAmount = 0;
                    switch(typePayment){
                        case PstCashPayment.CARD :
                            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                            start++;
                            
                            obj.newColumn(4,"",cola);
                            
                            //kartu kredit
                            obj.setLine(start,1, textListHeader[language][TXT_KARTU]);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_NO_KARTU],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getCcNumber(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_TIPE_KARTU],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getCcName(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_HOLDER],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getHolderName(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_TGL_BERLAKU],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+Formater.formatDate(cashInfo.getExpiredDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                            
                            break;
                        case PstCashPayment.CHEQUE :
                            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                            start++;
                            
                            obj.newColumn(4,"",cola);
                            
                            //check
                            obj.setLine(start,1, textListHeader[language][TXT_CHECK]);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_NO_ACCOUNT],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getChequeAccountName(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_TGL_BERLAKU],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+Formater.formatDate(cashInfo.getChequeDueDate(),"dd-MM-yyyy"),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_NAMA_BANK],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getChequeBank(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                            
                            break;
                        case PstCashPayment.DEBIT :
                            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                            start++;
                            
                            obj.newColumn(4,"",cola);
                            
                            //debet
                            obj.setLine(start,1, textListHeader[language][TXT_DEBET]);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_NAMA_BANK],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getDebitBankName(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            obj.setColumnValue(1,start, textListHeader[language][TXT_HOLDER],DSJ_PrintObj.TEXT_LEFT);
                            obj.setColumnValue(2,start, ": "+cashInfo.getDebitCardName(),DSJ_PrintObj.TEXT_LEFT);
                            start++;
                            
                            dAmount = (cashPayment.getAmount()+cashInfo.getAmount())/rate;
                            
                            break;
                        default :
                            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
                            start++;
                            
                            obj.setLine(start,1, textListHeader[language][TXT_TUNAI]);
                            start++;
                            
                            dAmount = cashPayment.getAmount()/rate;
                            break;
                    }
                    
                    
                    obj.newColumn(4,"",colp);
                    if(!crType.getCode().equals(currUse)){
                        String sign = "x";
                        if(cashPayment.getRate()<1){
                            cashPayment.setRate(1);
                        }
                        String out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),cashPayment.getRate())+sign+CashierMainApp.setFormatNumber(cashPayment.getRate(),rate);
                        if(cashPayment.getRate()<rate){
                            sign = "/";
                            out = crType.getCode()+" "+CashierMainApp.setFormatNumber(cashPayment.getAmount()/cashPayment.getRate(),rate)+sign+CashierMainApp.setFormatNumber(rate,rate);
                        }
                        if(out.length()>20){ // lebih dari panjang column
                            obj.setLine(start,1,out);
                            start++;
                            obj.newColumn(4,"",colp);
                        }
                        else{
                            obj.setColumnValue(0,start,out,DSJ_PrintObj.TEXT_RIGHT);
                        }
                    }
                    obj.setColumnValue(1,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2,start, CashierMainApp.setFormatNumber(dAmount,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;
                }
            }
        }
        
        setStartLine(start);
        return obj;
    }
    
    
    //to total paid
    public DSJ_PrintObj getTotalPaid(DSJ_PrintObj obj,int start, PrintInvoiceObj printObj, String currUse, double rate){
        obj.newColumn(5,"",cols);
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        //end total paid
        
        //start total kembali
        obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        setStartLine(start);
        return obj;
    }
    
    //to total paid
    public DSJ_PrintObj getTotalPaidPct(DSJ_PrintObj obj,int start, PrintInvoiceObj printObj, String currUse, double rate, double pct){
        obj.newColumn(5,"",cols);
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        
        printObj.setTotCardCost(printObj.getTotCardCost()*pct/100); 
        obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL_BAYAR],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber((printObj.getTotalPaid()+printObj.getTotCardCost())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        //end total paid
        
        //start total kembali
        obj.setColumnValue(0,start, textListHeader[language][TXT_KEMBALI],DSJ_PrintObj.TEXT_LEFT);
        obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
        obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTotalReturn()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
        start++;
        
        obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
        start++;
        setStartLine(start);
        return obj;
    }
    
    
    //pesan dibawah invoice
    synchronized public DSJ_PrintObj getBottomMsg(DSJ_PrintObj obj, int start){
        start++;
        obj.newColumn(1,"");
        if(cashierConfig.statusMsg==CashierMainApp.ENABLED){
            if (cashierConfig.useMsg1==CashierMainApp.ENABLED){
                obj.setLineCenterAlign(start,0,obj.getCharacterSelected(),cashierConfig.msg1);
                start++;
            }
            if (cashierConfig.useMsg2==CashierMainApp.ENABLED){
                obj.setLineCenterAlign(start,0,obj.getCharacterSelected(),cashierConfig.msg2);
                start++;
            }
            if (cashierConfig.useMsg3==CashierMainApp.ENABLED){
                obj.setLineCenterAlign(start,0,obj.getCharacterSelected(),cashierConfig.msg3);
                start++;
            }
            if (cashierConfig.useMsg4==CashierMainApp.ENABLED){
                obj.setLineCenterAlign(start,0,obj.getCharacterSelected(),cashierConfig.msg4);
                start++;
            }
        }
        start++;
        this.setStartLine(start);
        return obj;
    }
    
    /**
     * print terakhir header description dan approval
     * @param obj
     * @return
     */
    public DSJ_PrintObj setLastHeader(DSJ_PrintObj obj, PrintInvoiceObj printObj){
        try {
            int start = getStartLine();
            start++;
            obj.newColumn(4, "");
            obj.setColumnValue(0, start, textListHeader[language][TXT_PREPARED_BY], DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(1, start, textListHeader[language][TXT_CHECKED_BY], DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(2, start, CashierMainApp.getDSJ_CashierXML().getConfig(0).company, DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(3, start, textListHeader[language][TXT_CUSTOMER], DSJ_PrintObj.TEXT_CENTER);
            
            start++;
            obj.setColumnValue(0, start, "", DSJ_PrintObj.TEXT_CENTER);
            start++;
            obj.setColumnValue(0, start, "", DSJ_PrintObj.TEXT_CENTER);
            start++;
            obj.setColumnValue(0, start, "", DSJ_PrintObj.TEXT_CENTER);
            
            start++;
            obj.setColumnValue(0, start, printObj.getCashier(), DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(1, start, "--------------------", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(2, start, "--------------------", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(3, start, "--------------------", DSJ_PrintObj.TEXT_CENTER);
            
            start = start + 2;
            this.setStartLine(start);
        } catch (Exception e) {
        }
        return obj;
    }
    
    /**
     * print terakhir header description dan approval
     * @param obj
     * @return
     */
    public DSJ_PrintObj setCategoryQty(DSJ_PrintObj obj, Hashtable categoryList){ 
        try {
            if(categoryList!=null&&categoryList.size()>0){
                
                int start = getStartLine();
                int iCol = 0;
                        
                obj.newColumn(6, "");
                Enumeration enumeration = categoryList.keys();
                while(enumeration.hasMoreElements()){
                    String key = (String) enumeration.nextElement();
                    String val = (String) categoryList.get(key);
                    String qty = "0";
                    String prc = "0";
                    StringTokenizer st = new StringTokenizer(val,"/");
                        if(st.hasMoreTokens()){
                            if(cashierConfig.language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT)
                                qty = st.nextToken().replace('.',',');
                            else
                                qty = st.nextToken();
                        }
                        if(st.hasMoreTokens()){
                            prc = st.nextToken();
                        }
                    obj.setColumnValue(iCol, start,qty + " "+ key, DSJ_PrintObj.TEXT_LEFT);
                    iCol++;
                    double dPrc = 0;
                    try{
                        dPrc = Double.parseDouble(prc);
                        prc = CashierMainApp.setFormatNumber(dPrc);
                    }
                    catch(Exception e){
                        System.out.println("err on setCategoryQty() : "+e.toString());
                        prc = "0";
                    }
                    obj.setColumnValue(iCol, start,""/*": " +cashierConfig.defaultCurrencyCode+" "+ prc*/, DSJ_PrintObj.TEXT_LEFT);
                    iCol++;
                    if(iCol>=6){
                        start++;
                        iCol = 0;
                    }
                }
                start++;
                obj.setLineRptStr(start, 0, "=", obj.getCharacterSelected());
                start++;
                this.setStartLine(start);
            }
        } catch (Exception e) {
            System.out.println("err on setCategoryQty() = "+e.toString());
        }
        return obj;
    }
    
    /** Getter for property start.
     * @return Value of property start.
     *
     */
    public int getStartLine() {
        return this.startLine;
    }
    
    /** Setter for property start.
     * @param start New value of property start.
     *
     */
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }
    
}
