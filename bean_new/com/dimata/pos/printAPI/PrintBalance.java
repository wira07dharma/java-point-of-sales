/*
 * TestPrn.java
 *
 * Created on August 25, 2003, 10:04 AM
 */

package com.dimata.pos.printAPI;

import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.printman.DSJ_PrintObj;
import com.dimata.pos.printman.DSJ_PrinterService;
import com.dimata.pos.session.billing.makeInvoiceNo;
import com.dimata.pos.xmlparser.DSJ_CashierConfig;
import java.util.Date;
import java.util.Vector;

public class PrintBalance {
    private Vector entObj;
    private Vector objCurr;
    private String kasir;
    private Vector vctCash;
    private static double bayar;
    private static double kembali;
    private static double total;
    private int totalInv;
    private int totalQty;
    private double totalValueCrc;
    private double totalValueCard;
    private double totalValue;
    private boolean isCard;
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
    
    public PrintBalance(Vector entObj,Vector objCurr,String kasir,Vector vctCash,int totalInv,int totalQty,DSJ_CashierConfig cashierConfig){
        this.entObj=entObj;
        this.objCurr=objCurr;
        this.kasir=kasir;
        this.vctCash=vctCash;
        this.totalInv=totalInv;
        this.totalQty=totalQty;
        this.cashierConfig=cashierConfig;
    }
    
    public void printBalanceObj(){
        DSJ_PrintObj obj= new DSJ_PrintObj();//System.out.println("in printing");
        try{
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            
            //System.out.println("isi vector"+entObj.size());
            obj.setPrnIndex(1);
            obj.setPageLength(13+entObj.size());
            obj.setTopMargin(1);
            obj.setLeftMargin(1);
            obj.setRightMargin(1);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN); // 12 CPI = 96 char /line
            //header
            
            obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //obj.setLine(1,1, "PT. SUMBER MAS UTAMA (SHOP IN SHOP)");
            //obj.setLine(2,1, "NGURAH RAI AIRPORT - BALI");
            obj.setLine(1,1, ""+cashierConfig.company);
            obj.setLine(2,1, ""+cashierConfig.address);
            obj.setLine(3,1, "OPENING BALANCE");
            obj.setLine(4,1,"Tgl.  :"+getDateStr());
            obj.setLine(5,1,"Jam   :"+getTimeStr());
            obj.setLine(6,1,"Kasir :"+kasir);
            
            obj.setLineRptStr(7,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLine(8,1,"VALAS");
            obj.setLine(8,15,"KURS (Rp.)");
            obj.setLine(8,27,"SALDO KAS");
            
            obj.setLineRptStr(9,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //item data
            setItemSizeHd(obj,10);
            //System.out.println("Start Printing");
            prnSvc.print(obj);
            prnSvc.running = true;
            prnSvc.run_x();
            
            prnSvc.running=false;
            //System.out.println("Bye");
        } catch (Exception exc){
            System.out.println("Exc : "+ exc);
        }
    }
    
    public void printClosingBalanceObj(){
        DSJ_PrintObj obj= new DSJ_PrintObj();
        try{
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            //     		DSJ_PrintObj obj= new DSJ_PrintObj();
            this.totalValue=0;
            isCard=false;
            //System.out.println("isi vector"+entObj.size());
            obj.setPrnIndex(1);
            obj.setPageLength(18+entObj.size());
            obj.setTopMargin(1);
            obj.setLeftMargin(1);
            obj.setRightMargin(1);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN); // 12 CPI = 96 char /line
            //header
            obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //obj.setLine(1,1, "PT. SUMBER MAS UTAMA (SHOP IN SHOP)");
            //obj.setLine(2,1, "NGURAH RAI AIRPORT - BALI");
            obj.setLine(1,1, ""+cashierConfig.company);
            obj.setLine(2,1, ""+cashierConfig.address);
            obj.setLine(3,1, "CLOSING BALANCE");
            obj.setLine(4,1,"Tgl.  :"+getDateStr());
            obj.setLine(5,1,"Jam   :"+getTimeStr());
            obj.setLine(6,1,"Kasir :"+kasir);
            
            obj.setLineRptStr(7,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //column header
            int[] intCols = {15,15,15,70};
            obj.setLine(8,1,"VALAS");
            obj.setLine(8,13,"PENERIMAAN");
            obj.setLine(8,25,"NILAI RUPIAH");
            
            obj.setLineRptStr(9,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //item data
            int newLine=setItemSizeHdClosing(obj,10);
            if (this.isCard){
                obj.setLineRptStr(newLine,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
                obj.setLine(newLine+1,1,"CARD");
                obj.setLine(newLine+1,13,"PENERIMAAN");
                obj.setLine(newLine+1,25,"NILAI RUPIAH");
                
                obj.setLineRptStr(newLine+2,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
                setItemSizeHdClosingCard(obj,newLine+3);
            }else {
                setItemSizeHdClosingCard(obj,newLine);
            }
            
            //System.out.println("Start Printing");
            prnSvc.print(obj);
            prnSvc.running = true;
            prnSvc.run_x();
            
            prnSvc.running=false;
            //System.out.println("Bye");
        } catch (Exception exc){
            System.out.println("Exc : "+ exc);
        }
    }
    
    
    public void printClosingBalanceObj2(){
        DSJ_PrintObj obj= new DSJ_PrintObj();
        try{
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            //     		DSJ_PrintObj obj= new DSJ_PrintObj();
            this.totalValue=0;
            isCard=false;
            //System.out.println("isi vector"+entObj.size());
            obj.setPrnIndex(1);
            obj.setPageLength(18+entObj.size());
            obj.setTopMargin(1);
            obj.setLeftMargin(1);
            obj.setRightMargin(1);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(2); // 12 CPI = 96 char /line
            //header
            obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            
            //obj.setLine(1,1, "PT. SUMBER MAS UTAMA (SHOP IN SHOP)");
            //obj.setLine(2,1, "NGURAH RAI AIRPORT - BALI");
            obj.setLine(1,1, ""+cashierConfig.company);
            obj.setLine(2,1, ""+cashierConfig.address);
            obj.setLine(3,1, "SALES REPORT");
            obj.setLine(4,1,"Tgl.  :"+getDateStr());
            obj.setLine(5,1,"Jam   :"+getTimeStr());
            obj.setLine(6,1,"Kasir :"+kasir);
            
            
            obj.setLineRptStr(7,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //column header
            obj.setLine(8,1,"VALAS");
            obj.setLine(8,13,"PENERIMAAN");
            obj.setLine(8,25,"NILAI RUPIAH");
            int[] intCols = {15,15,15,70};
            
            obj.setLineRptStr(9,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //item data
            int newLine=setItemSizeHdClosing(obj,10);
            if (this.isCard){
                obj.setLineRptStr(newLine,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
                obj.setLine(newLine+1,1,"CARD");
                obj.setLine(newLine+1,13,"PENERIMAAN");
                obj.setLine(newLine+1,25,"NILAI RUPIAH");
                obj.setLineRptStr(newLine+2,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
                setItemSizeHdClosingCard(obj,newLine+3);
            }else {
                setItemSizeHdClosingCard(obj,newLine);
            }
            
            //System.out.println("Start Printing");
            obj.setLine(1,1, "\n\n\n\n\n\n\n\n");
            prnSvc.print(obj);
            prnSvc.running = true;
            prnSvc.run_x();
            
            prnSvc.running=false;
            //System.out.println("Bye");
        } catch (Exception exc){
            System.out.println("Exc : "+ exc);
        }
    }
    
    public static String getDateStr(){
        String tglNow="";
        Date tanggal=new Date();
        //tglNow=""+tanggal.getDate()+"/"+(tanggal.getMonth()+1)+"/"+(tanggal.getYear()+1900);
        makeInvoiceNo makeinv=new makeInvoiceNo();
        tglNow=makeinv.setTgl();
        return tglNow;
    }
    
    public static String getTimeStr(){
        String timeNow="";
        Date tanggal=new Date();
        //timeNow=""+tanggal.getHours()+":"+tanggal.getMinutes()+":"+tanggal.getSeconds();
        makeInvoiceNo makeinv=new makeInvoiceNo();
        timeNow=makeinv.setTime();
        return timeNow;
    }
    
    
    
    
    public static void setItemSeparate(DSJ_PrintObj obj, int line, int maxSize){
        obj.setLine(line,PI_COL_START_BAR_CODE-1,COL_SEPARATOR);
        //obj.setLine(line,PI_COL_START_DESC-1,COL_SEPARATOR);
        obj.setLine(line,PI_COL_START_QTY-1,COL_SEPARATOR);
        obj.setLine(line,PI_COL_START_TOTAL-1,COL_SEPARATOR);
        obj.setLine(line,PI_COL_START_SIZE-1,COL_SEPARATOR);
        for(int i =  0 ; i <  maxSize; i++){
            //obj.setLine(line,PI_COL_START_SIZE+i*(PI_COL_WIDTH_SIZE+1)-1,COL_SEPARATOR);
        }
        obj.setLine(line,PI_COL_START_QTY-1,COL_SEPARATOR);
        obj.setLine(line,PI_COL_START_PRICE-1,COL_SEPARATOR);
        // if(PI_COL_WIDTH_DISC_ITM!=0)
        //    obj.setLine(line,PI_COL_START_DISC_ITM-1,COL_SEPARATOR);
        // obj.setLine(line,PI_COL_START_AMOUNT-1,COL_SEPARATOR);
        //obj.setLine(line,PI_MAX_WIDTH,COL_SEPARATOR);
    }
    
    public void setVector(Vector objVct){
        this.entObj=objVct;
    }
    
    public Vector getVector(){
        return entObj;
    }
    
    
    //opening balance
    public void setItemSizeHd(DSJ_PrintObj obj, int line){
        int baris=line;
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
        this.objCurr=new Vector();
    }
    
    public int setItemSizeHdClosing(DSJ_PrintObj obj, int line){
        int baris=line;
        double total=0;
        for(int i =  0 ; i <  this.entObj.size(); i++){
            Balance blc=(Balance)entObj.get(i);
            String code="";
            String jumlah=PstBalance.formatCurrency(blc.getShouldValue());
            String val=PstBalance.formatCurrency(blc.getBalanceValue());
            
            String buyRate="";
            String sellRate="";
            double rateValue=1;
            String currencyStr=String.valueOf(blc.getCurrencyOid());
            for (int a=0;a<objCurr.size();a++){
                Vector currVec=(Vector)objCurr.get(a);
                String currencyCode=(String)currVec.get(0);
                if (currencyStr.equalsIgnoreCase(currencyCode)){
                    buyRate=(String)currVec.get(4);
                    sellRate=(String)currVec.get(3);
                    rateValue=Double.parseDouble(sellRate);
                    code=(String)currVec.get(1);
                }//endif
            }//endfor
            int type=Integer.parseInt(""+this.vctCash.get(i));
            if (type==1) {
                double rpValue=blc.getShouldValue()*rateValue;
                total+=rpValue;
                String strRpValue=PstBalance.formatCurrency(rpValue);
                jumlah=jumlah.substring(0,jumlah.length()-3);
                if (jumlah.length()==0)
                    jumlah="0";
                strRpValue=strRpValue.substring(0,strRpValue.length()-3);
                if (strRpValue.length()==0)
                    strRpValue="0";
                obj.setLine(baris,PI_COL_START_BAR_CODE,code);
                obj.setLineRightAlign(baris,PI_COL_START_QTY+1,jumlah,10);
                obj.setLineRightAlign(baris,PI_COL_START_TOTAL+1,strRpValue,12);
                baris+=1;
            }else {
                this.isCard=true;
            }
        }
        this.totalValueCrc=total;
        obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        obj.setLine(baris+1,PI_COL_START_BAR_CODE, "Sub Total Cash");
        obj.setLineRightAlign(baris+1,PI_COL_START_QTY+1,"Rp.",10);
        String totals=PstBalance.formatCurrency(total);
        totals=totals.substring(0,totals.length()-3);
        obj.setLineRightAlign(baris+1,PI_COL_START_TOTAL,totals,13);
        return baris+2;
    }
    
    public void setItemSizeHdClosingCard(DSJ_PrintObj obj, int line){
        int baris=line;
        int barisAwal=baris;
        double total=0;
        for(int i =  0 ; i <  this.entObj.size(); i++){
            Balance blc=(Balance)entObj.get(i);
            String code="";
            String jumlah=PstBalance.formatCurrency(blc.getShouldValue());
            String val=PstBalance.formatCurrency(blc.getBalanceValue());
            
            String buyRate="";
            String sellRate="";
            double rateValue=1;
            String currencyStr=String.valueOf(blc.getCurrencyOid());
            for (int a=0;a<objCurr.size();a++){
                Vector currVec=(Vector)objCurr.get(a);
                String currencyCode=(String)currVec.get(0);
                if (currencyStr.equalsIgnoreCase(currencyCode)){
                    buyRate=(String)currVec.get(4);
                    sellRate=(String)currVec.get(3);
                    rateValue=Double.parseDouble(sellRate);
                    code=(String)currVec.get(1);
                }//endif
            }//endfor
            int type=Integer.parseInt(""+this.vctCash.get(i));
            if (type==0) {
                double rpValue=blc.getShouldValue()*rateValue;
                total+=rpValue;
                String strRpValue=PstBalance.formatCurrency(rpValue);
                jumlah=jumlah.substring(0,jumlah.length()-3);
                if (jumlah.length()==0)
                    jumlah="0";
                strRpValue=strRpValue.substring(0,strRpValue.length()-3);
                if (strRpValue.length()==0)
                    strRpValue="0";
                obj.setLine(baris,PI_COL_START_BAR_CODE,code);
                obj.setLineRightAlign(baris,PI_COL_START_QTY+1,jumlah,10);
                obj.setLineRightAlign(baris,PI_COL_START_TOTAL+1,strRpValue,12);
                baris+=1;
            }
        }
        this.totalValueCard=total;
        if (this.isCard){
            obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLine(baris+1,PI_COL_START_BAR_CODE, "Sub Total Card");
            obj.setLineRightAlign(baris+1,PI_COL_START_QTY+1,"Rp.",10);
            String totals=PstBalance.formatCurrency(total);
            totals=totals.substring(0,totals.length()-3);
            obj.setLineRightAlign(baris+1,PI_COL_START_TOTAL,totals,13);
            this.totalValue=totalValueCrc+totalValueCard;
            obj.setLine(baris+2,PI_COL_START_BAR_CODE, "Total");
            obj.setLineRightAlign(baris+2,PI_COL_START_QTY+1,"Rp.",10);
            String totalstr=PstBalance.formatCurrency(totalValue);
            totalstr=totalstr.substring(0,totalstr.length()-3);
            obj.setLineRightAlign(baris+2,PI_COL_START_TOTAL,totalstr,13);
            obj.setLine(baris+4,PI_COL_START_BAR_CODE, "Total Invoice");
            String totalInvoice=""+this.totalInv;
            obj.setLineRightAlign(baris+4,PI_COL_START_QTY+1,totalInvoice,10);
            obj.setLine(baris+5,PI_COL_START_BAR_CODE, "Total Qty");
            String totalQtys=""+this.totalQty;
            obj.setLineRightAlign(baris+5,PI_COL_START_QTY+1,totalQtys,10);
            obj.setLineRptStr(baris+6,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        }else {
            this.totalValue=totalValueCrc+totalValueCard;
            obj.setLine(barisAwal,PI_COL_START_BAR_CODE, "Total");
            obj.setLineRightAlign(barisAwal,PI_COL_START_QTY+1,"Rp.",10);
            String totalstr=PstBalance.formatCurrency(totalValue);
            totalstr=totalstr.substring(0,totalstr.length()-3);
            obj.setLineRightAlign(barisAwal,PI_COL_START_TOTAL,totalstr,13);
            obj.setLine(barisAwal+1,PI_COL_START_BAR_CODE, "Total Invoice");
            String totalInvoice=""+this.totalInv;
            obj.setLineRightAlign(barisAwal+1,PI_COL_START_QTY+1,totalInvoice,10);
            obj.setLine(barisAwal+2,PI_COL_START_BAR_CODE, "Total Qty");
            String totalQtys=""+this.totalQty;
            obj.setLineRightAlign(barisAwal+2,PI_COL_START_QTY+1,totalQtys,10);
            obj.setLineRptStr(barisAwal+3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        }
        
    }
    
    
}
