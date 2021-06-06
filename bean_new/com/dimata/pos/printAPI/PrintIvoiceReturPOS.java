/*
 * PrintIvoiceReturPOS.java
 *
 * Created on January 6, 2005, 4:52 PM
 */

package com.dimata.pos.printAPI;

import com.dimata.pos.cashier.CashierMainApp;
import com.dimata.pos.cashier.DefaultSaleModel;
import com.dimata.pos.entity.billing.BillDetailCode;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.printman.DSJ_PrintObj;
import com.dimata.pos.printman.DSJ_PrinterService;
import com.dimata.pos.session.billing.SessPrintInvoice;
import com.dimata.pos.xmlparser.DSJ_CashierConfig;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  yogi
 */

public class PrintIvoiceReturPOS {
    
    public static String COL_SEPARATOR = "|";
    final static int PI_COL_START  = 1 ;
    final static int PI_COL_WIDTH_NAME = 20;
    final static int PI_COL_WIDTH_QTY	  =  3;
    final static int PI_COL_WIDTH_TOTAL = 8;
    
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
    public final static int TXT_INV_REF=35;
    public final static int TXT_CREDIT_PAY=36;
    
    //print header
    public static final String textListHeader[][] = {
        {"TERIMAKASIH","RETURN INVOICE","No Resep","Pasien","Dokter","Biaya Resep","No","Kasir","Tgl","Jam","Nama Brg","Qty","Jumlah",
         "Total Return","Tunai","Kartu Kredit","Total Bayar", "Kembali","Nomor","Holder","Type","Total Setelah Disc","Diskon","Harga",
         "Sub Total","Pajak","Service","Nama","Barcode","Poin","Berlaku sampai","Bank","No. Account","Kartu Debet","Check","Inv Ref","Credit Payment"},
         {"THANK YOU","RETURN INVOICE","Recipe No","Patient","Doctor","Recipe Service","No","Cashier","Date","Time","Goods","Qty","Amount",
          "Total Return","Cash","CreditCard","Total Paid", "Change","Number","Holder","Type","Total After Disc","Disc","Price",
          "Sub Total","Tax","Service","Name","Barcode","Point","Exp. Date","Bank","Account No","Debet Card","Cheque","Inv Ref","Credit Payment"}
    };
    
    
    /** Creates a new instance of PrintIvoiceReturPOS */
    public PrintIvoiceReturPOS() {
    }
    
    synchronized public DSJ_PrintObj getLastFeed(DSJ_PrintObj obj, int start){
        for(int i = 0; i < cashierConfig.printingGap; i++){
            obj.setLine(start,"");
            start++;
        }
        return obj;
    }
    
    
    //by wpulantara
    
    synchronized public void printInvoiceReturObj(DefaultSaleModel saleModel) {
        DSJ_PrintObj obj= new DSJ_PrintObj();
        BillMain billMain = saleModel.getMainSale();
        PrintInvoiceObj printObj = SessPrintInvoice.getDataReturn(saleModel);
        try {
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
            CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(cashierConfig.defaultCurrencyCode);
            String currUse = currType.getCode();
            double rate = billMain.getRate();
            if(rate<1){
                rate = 1;
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
            obj.setObjDescription("Return Invoice");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN);
            
            //header
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            start++;  //1
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_TERIMAKASIH]);
            obj.setLine(start,24, ""+textListHeader[language][TXT_INVOICE]);
            start++;//2
            
            obj.setLine(start,1, ""+cashierConfig.company);
            start++;//3
            
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            start++;//4
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_NO]+": "+printObj.getNoInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_TGL]+": "+Formater.formatDate(printObj.getDateInvoice(),"dd/MM/yyyy"));
            start++;//5
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_INV_REF]+": "+printObj.getNoRefInvoice());
            obj.setLine(start,23,""+textListHeader[language][TXT_JAM]+": "+Formater.formatDate(new Date(),"kk:mm:ss"));
            start++;//6
            
            obj.setLine(start,1, ""+textListHeader[language][TXT_KASIR]+": "+printObj.getCashier());
            start++;//6
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "=",PI_COL_START_SIZE-1);
            start++;//7
            
            //column header
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
            Vector listBillDetail = printObj.getListBillDetail();
            int sizeData = listBillDetail.size();
            if(listBillDetail!=null&&listBillDetail.size()>0){
                for(int i=0;i<sizeData;i++){
                    Vector vect = (Vector)listBillDetail.get(i);
                    Billdetail billdetail = (Billdetail)vect.get(0);
                    BillDetailCode billDetailCode = (BillDetailCode)vect.get(1);
                    String nmProduct = billdetail.getItemName();
                    if(nmProduct.length()>40){
                        nmProduct = nmProduct.substring(0,39);
                    }
                    
                    String serialNo = billDetailCode.getStockCode();
                    
                    obj.setLine(start,1, nmProduct);
                    start++;//10
                    
                    if(serialNo!=null&&serialNo.length()>0){
                        obj.setLine(start,1, "SN : "+serialNo);
                        start++;//10
                    }
                    
                    obj.setColumnValue(0,start, CashierMainApp.setFormatNumber(billdetail.getItemPrice()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(1,start, CashierMainApp.setFormatNumber(billdetail.getDisc()/(rate*billdetail.getQty()),rate),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(2,start, ""+billdetail.getQty(),DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(billdetail.getTotalPrice()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                    start++;//11
                }
            }
            
            //end item
            
            //begin total
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            int[] colx = {20,1,5,14,20};
            obj.newColumn(5,"",colx);
            obj.setColumnValue(0,start, textListHeader[language][TXT_SUB_TOTAL],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getSubTotal()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_DISC],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getDiscount()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_TAX],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getTax()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_SERVICE],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(printObj.getService()/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            double creditPayment = printObj.getSubTotal() - printObj.getDiscount() + printObj.getTax() + printObj.getService();
            
            if(printObj.getTotalTransaction()<creditPayment){
                if(printObj.getTotalTransaction()>0){
                    creditPayment = creditPayment - printObj.getTotalTransaction();
                }
                obj.setColumnValue(0,start, textListHeader[language][TXT_CREDIT_PAY],DSJ_PrintObj.TEXT_LEFT);
                obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
                obj.setColumnValue(3,start, CashierMainApp.setFormatNumber(creditPayment/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
                start++;
            }
            
            obj.setColumnValue(0,start, textListHeader[language][TXT_TOTAL],DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(2,start, currUse,DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3,start, CashierMainApp.setFormatNumber((printObj.getTotalTransaction()<0?0:printObj.getTotalTransaction())/rate,rate),DSJ_PrintObj.TEXT_RIGHT);
            start++;
            
            
            obj.setLineRptStr(start,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-1);
            start++;
            
            //end total
            obj = getLastFeed(obj,start);
            
            prnSvc.print(obj);
        }
        catch (Exception exc) {
            System.out.println("Exc : "+ exc);
        }
    }
    
    
   
}
