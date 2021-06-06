/*
 * TestPrn.java
 *
 * Created on August 25, 2003, 10:04 AM
 */

package com.dimata.pos.printAPI;

import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.printman.DSJ_PrintObj;
import com.dimata.pos.printman.DSJ_PrinterService;
import com.dimata.pos.session.billing.makeInvoiceNo;
import com.dimata.pos.xmlparser.DSJ_CashierConfig;
import java.util.Date;
import java.util.Vector;

//new 17 - 05 - 04

public class PrintInvoice {
    private Vector entObj;
    private static double bayar;
    private static double kembali;
    private static double total;
    private String invNo;
    private double tax;
    private double svc;
    private Vector curObj;
    private Vector crc;
    private double totalVal;
    private String strDisc;
    boolean status_cash;
    boolean status_card;
    private String kasir;
    private String lokasi;
    private int line;
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
      
    public PrintInvoice(Vector entObj,String lokasi,double total,double bayar,double kembali,String invNo,double tax,double svc,Vector curObj,Vector crc,String strDisc,String kasir,int line){
        this.entObj=entObj;
        this.total=total;
        this.bayar=bayar;
        this.kembali=kembali;
        this.invNo=invNo;
        this.curObj=curObj;
        this.crc=crc;
        this.tax=tax;
        this.svc=svc;
        this.strDisc=strDisc;
        this.kasir=kasir;
        this.lokasi=lokasi;
        this.line=line;
    }
    
    // public static void main(String[] argv){
    public void printInvoiceObj(){
        DSJ_PrintObj obj= new DSJ_PrintObj();
        try{
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            String str="0123456789";
            //System.out.println(str.substring(3,5));
            //System.out.println("isi vector "+entObj.size()+" line = "+this.line);
            obj.setPrnIndex(1);
            obj.setPageLength(20+this.curObj.size()+(entObj.size())+line);
            //System.out.println("xxxxxxxxxxx ");
            obj.setTopMargin(1);
            obj.setLeftMargin(0);
            obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_40_COLUMN); // 40 char /line
            //header
            obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            obj.setLine(1,1, "TERIMA KASIH");
            obj.setLine(1,30, "INVOICE");
            obj.setLine(2,1, ""+this.lokasi);
            //obj.setLine(2,1, "RTA Shop");
            obj.setLine(4,1, "Nomor:"+this.invNo);
            obj.setLine(4,23,"Tgl:"+getDateStr());
            obj.setLine(5,1, "Kasir:"+this.kasir);
            obj.setLine(5,23,"Jam:"+getTimeStr());
            obj.setLineRptStr(6,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //column header
            obj.setLine(7,1,"Nama Brg");
            obj.setLine(7,22,"Qty");
            obj.setLine(7,31,"Jumlah");
            
            obj.setLineRptStr(8,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            
            //item data
            int mulai=setItemSizeHd(obj,9);
            setBotom(obj,mulai);
            if (this.strDisc.length()!=0)
                mulai=mulai+6;
            else
                mulai=mulai+4;
            int start=setPayment(obj,mulai);
            start=start+1;
            int start2=setPaymentCard(obj,start);
            start2=start2-1;
            setBotom2(obj,start2);
            
            //System.out.println("Start Printing");
            prnSvc.print(obj);
            prnSvc.running = true;
            prnSvc.run_x();
            prnSvc.running=false;
            //System.out.println("");
            //System.out.println("Press any key to end");
            //System.out.println("Bye");
        } catch (Exception exc){
            System.out.println("Exc : "+ exc);
        }
    }
    
    public void printInvoiceObj2(){
        DSJ_PrintObj obj= new DSJ_PrintObj();
        try{
            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            //     		DSJ_PrintObj obj= new DSJ_PrintObj();
            String str="0123456789";
            //System.out.println(str.substring(3,5));
            //System.out.println("isi vector"+entObj.size()+this.curObj.size());
            obj.setPrnIndex(1);
            obj.setPageLength(20+this.curObj.size()+entObj.size()+line);
            obj.setTopMargin(1);
            obj.setLeftMargin(0);
            obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(2); // 12 CPI = 96 char /line
            //header
            obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLineRptStr(3,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_QTY);
            obj.setLine(1,1, "TERIMA KASIH");
            obj.setLine(1,20, "INVOICE");
            obj.setLine(2,1, ""+this.lokasi);
            //obj.setLine(2,1, "RTA Shop");
            obj.setLine(4,1, "Nomor :"+this.invNo);
            obj.setLine(4,20,"Tgl.  :"+getDateStr());
            obj.setLine(5,1, "Kasir :"+this.kasir);
            obj.setLine(5,20,"Jam   :"+getTimeStr());
            obj.setLineRptStr(6,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //column header
            int[] intCols = {20,10,15,1};
            obj.newColumn(4,"|",intCols);
            obj.setColumnValue(0,7,"Name",obj.TEXT_CENTER);
            obj.setColumnValue(1,7,"Qty",obj.TEXT_CENTER);
            obj.setColumnValue(2,7,"Jumlah",obj.TEXT_CENTER);
            obj.setLineRptStr(8,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            //item data
            int mulai=setItemSizeHd(obj,9);
            setBotom(obj,mulai);
            if (this.strDisc.length()!=0)
                mulai=mulai+8;
            else
                mulai=mulai+7;
            int start=setPayment(obj,mulai);
            start=start+1;
            int start2=setPaymentCard(obj,start);
            start2=start2-1;
            setBotom2(obj,start2);
            
            //System.out.println("Start Printing");
            prnSvc.print(obj);
            prnSvc.running = true;
            prnSvc.run_x();
            
            
            //System.out.println("");
            //System.out.println("Press any key to end");
            
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
        
    }
    
    public void setVector(Vector objVct){
        this.entObj=objVct;
    }
    
    public Vector getVector(){
        return entObj;
    }
    
    public static Vector getSizeTypes(){
        Vector sizeTypes = new Vector(1,1);
        Vector aType = new Vector(1,1);
        aType.add("XS");aType.add("S");aType.add("M");aType.add("L");aType.add("XL");aType.add("XXL");
        sizeTypes.add(aType);
        aType = new Vector(1,1);
        aType.add("26");aType.add("28");aType.add("30");aType.add("32");aType.add("34");aType.add("36");;aType.add("38");;aType.add("40");
        sizeTypes.add(aType);
        
        return sizeTypes;
    }
    
    public static int getMaxSize(Vector sizeTypes){
        int maxSize =0;
        for(int i =  0 ; i <  sizeTypes.size(); i++){
            Vector ast = (Vector) sizeTypes.get(i);
            int lng = ast.size();
            if(lng>maxSize)
                maxSize= lng;
        }
        return maxSize;
    }
    
    
    
    public int setItemSizeHd(DSJ_PrintObj obj, int line){
        int baris=line;
        totalVal=0;
        Vector vector=getVector();
        if ((vector!=null)&&(vector.size()>0)) {
            for(int i =  0 ; i <  vector.size(); i++){
                Billdetail detail = (Billdetail) vector.get(i);
                String nama=detail.getItemName();
                String name2="";
                int panjang=nama.length();
                if (panjang>20) {
                    name2=nama.substring(0,20);
                    nama=name2;
                }
                String qty=String.valueOf(detail.getQty());
                totalVal+=detail.getTotalPrice();
                String amt=PstBillDetail.formatCurrency(detail.getTotalPrice());
                amt=amt.substring(0,amt.length()-3);
                double disc=detail.getDisc();
                if (disc<=0){
                    obj.setLine(baris,PI_COL_START_BAR_CODE,nama);
                    obj.setLine(baris,PI_COL_START_QTY+2,qty);
                    obj.setLineRightAlign(baris,PI_COL_START_TOTAL+1,amt,10);
                    baris+=1;
                }
                else {
                    String disStr="Disc ";
                    int disconType=detail.getDiscType();
                    if (disconType==0){
                        disStr=disStr+disc+"%";
                    }else{
                        disStr=disStr+"Rp."+disc;
                        disStr=disStr.substring(0,disStr.length());
                    }
                    obj.setLine(baris,PI_COL_START_BAR_CODE,nama);
                    obj.setLine(baris,PI_COL_START_QTY+1,disStr);
                    obj.setLine(baris+1,PI_COL_START_QTY+1,qty);
                    obj.setLineRightAlign(baris+1,PI_COL_START_TOTAL+1,amt,10);
                    baris+=2;
                }
            }
        }
        return baris;
        
    }
    
    private void setBotom(DSJ_PrintObj obj, int line) {
        int baris=line;
        //obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        String tot=PstBillDetail.formatCurrency(this.totalVal);
        tot=tot.substring(0,tot.length()-3);
        String taxs=PstBillDetail.formatCurrency(this.tax);
        taxs=taxs.substring(0,taxs.length()-3);
        String svcs=PstBillDetail.formatCurrency(this.svc);
        svcs=svcs.substring(0,svcs.length()-3);
        String totals=PstBillDetail.formatCurrency(this.total);
        totals=totals.substring(0,totals.length()-3);
        
      
        if (this.strDisc.length()!=0) {
            obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLine(baris+1,PI_COL_START_BAR_CODE, "Sub Total");
            obj.setLine(baris+1,PI_COL_START_QTY+1,"Rp.");
            obj.setLineRightAlign(baris+1,PI_COL_START_TOTAL+1,tot,10);
            obj.setLine(baris+2,PI_COL_START_BAR_CODE, "Disc");
            obj.setLine(baris+2,PI_COL_START_QTY+1,strDisc);
            obj.setLineRightAlign(baris+2,PI_COL_START_TOTAL+1,"",10);
            
            obj.setLine(baris+3,PI_COL_START_BAR_CODE, "Total");
            obj.setLine(baris+3,PI_COL_START_QTY+1,"Rp.");
            obj.setLineRightAlign(baris+3,PI_COL_START_TOTAL+1,totals,10);
            obj.setLineRptStr(baris+4,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        }else {
            obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLine(baris+1,PI_COL_START_BAR_CODE, "Total");
            obj.setLine(baris+1,PI_COL_START_QTY+1,"Rp.");
            obj.setLineRightAlign(baris+1,PI_COL_START_TOTAL+1,totals,10);
            obj.setLineRptStr(baris+2,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        }
        
    }
    
    private void setBotom21(DSJ_PrintObj obj, int line) {
        int baris=line;
        obj.setLineRptStr(baris,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        String tot=PstBillDetail.formatCurrency(this.totalVal);
        tot=tot.substring(0,tot.length()-3);
        String taxs=PstBillDetail.formatCurrency(this.tax);
        taxs=taxs.substring(0,taxs.length()-3);
        String svcs=PstBillDetail.formatCurrency(this.svc);
        svcs=svcs.substring(0,svcs.length()-3);
        String totals=PstBillDetail.formatCurrency(this.total);
        totals=totals.substring(0,totals.length()-3);
        
        obj.setLine(baris+1,PI_COL_START_BAR_CODE, "Sub Total");
        obj.setLine(baris+1,PI_COL_START_QTY+1,"Rp.");
        obj.setLineRightAlign(baris+1,PI_COL_START_TOTAL+1,tot,10);
        obj.setLine(baris+2,PI_COL_START_BAR_CODE, "Tax");
        obj.setLine(baris+2,PI_COL_START_QTY+1,"Rp.");
        obj.setLineRightAlign(baris+2,PI_COL_START_TOTAL+1,taxs,10);
        obj.setLine(baris+3,PI_COL_START_BAR_CODE, "Service");
        obj.setLine(baris+3,PI_COL_START_QTY+1,"Rp.");
        obj.setLineRightAlign(baris+3,PI_COL_START_TOTAL+1,svcs,10);
        if (this.strDisc.length()!=0) {
            obj.setLine(baris+4,PI_COL_START_BAR_CODE, "Disc");
            obj.setLine(baris+4,PI_COL_START_QTY+1,strDisc);
            obj.setLineRightAlign(baris+4,PI_COL_START_TOTAL+1,"",10);
            obj.setLineRptStr(baris+5,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLine(baris+6,PI_COL_START_BAR_CODE, "Total");
            obj.setLine(baris+6,PI_COL_START_QTY+1,"Rp.");
            obj.setLineRightAlign(baris+6,PI_COL_START_TOTAL+1,totals,10);
        }else {
            obj.setLineRptStr(baris+4,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
            obj.setLine(baris+5,PI_COL_START_BAR_CODE, "Total");
            obj.setLine(baris+5,PI_COL_START_QTY+1,"Rp.");
            obj.setLineRightAlign(baris+5,PI_COL_START_TOTAL+1,totals,10);
        }
        
    }
    
    private int setPayment(DSJ_PrintObj obj, int line) {
        int baris=line;
        int barisAwal=baris;
        status_cash=false;
        for (int j=0;j<curObj.size();j++) {
            CashPayments pay=(CashPayments)curObj.get(j);
            String payType="";
            String curencyStr="";
            String rates="";
            
            int payTipe=pay.getPaymentType();
            String currencyIdStr=String.valueOf(pay.getCurrencyId());
            for (int i=0;i<crc.size();i++){
                Vector currVec=(Vector)crc.get(i);
                String currencyIds=(String)currVec.get(0);
                if (currencyIdStr.equalsIgnoreCase(currencyIds)){
                    curencyStr=(String)currVec.get(1);
                    rates=(String)currVec.get(3);
                }
            }//endfor in
            if (payTipe==0){
                status_cash=true;
                double amount=pay.getAmount()*Double.parseDouble(rates);
                //amount=amount*Double.parseDouble(rates);
                String amounts=PstBillDetail.formatCurrency(amount);
                amounts=amounts.substring(0,amounts.length()-3);
                obj.setLine(baris,PI_COL_START_BAR_CODE+4,curencyStr);
                obj.setLine(baris,PI_COL_START_QTY+1,"Rp.");
                obj.setLineRightAlign(baris,PI_COL_START_TOTAL+1,amounts,10);
                baris+=1;
            }
        }
        if (status_cash) {
            obj.setLine(barisAwal-1,PI_COL_START_BAR_CODE, "Cash");
        }
        return baris;
        
    }
    
    private int setPaymentCard(DSJ_PrintObj obj, int line) {
        int baris=line;
        if (!status_cash)
            baris=baris-1;
        int barisAwal=baris;
        status_card=false;
        for (int j=0;j<curObj.size();j++) {
            CashPayments pay=(CashPayments)curObj.get(j);
            String payType="";
            String curencyStr="";
            String rates="";
            
            int payTipe=pay.getPaymentType();
            String currencyIdStr=String.valueOf(pay.getCurrencyId());
            for (int i=0;i<crc.size();i++){
                Vector currVec=(Vector)crc.get(i);
                String currencyIds=(String)currVec.get(0);
                if (currencyIdStr.equalsIgnoreCase(currencyIds)){
                    curencyStr=(String)currVec.get(1);
                    rates=(String)currVec.get(3);
                }
            }//endfor in
            if (payTipe==1){
                status_card=true;
                double amount=pay.getAmount()*Double.parseDouble(rates);
                //amount=amount*Double.parseDouble(rates);
                String amounts=PstBillDetail.formatCurrency(amount);
                amounts=amounts.substring(0,amounts.length()-3);
                obj.setLine(baris,PI_COL_START_BAR_CODE+4,curencyStr);
                obj.setLine(baris,PI_COL_START_QTY+1,"Rp.");
                obj.setLineRightAlign(baris,PI_COL_START_TOTAL+1,amounts,10);
                baris+=1;
            }
        }
        if (status_card)
            obj.setLine(barisAwal-1,PI_COL_START_BAR_CODE, "Card");
        return baris;
        
    }
    
    private void setBotom2(DSJ_PrintObj obj, int line) {
        int baris=line;
        if (!status_card)
            baris=baris-1;
        obj.setLineRptStr(baris+1,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
        obj.setLine(baris+2,PI_COL_START_BAR_CODE, "Total Bayar");
        obj.setLine(baris+2,PI_COL_START_QTY+1,"Rp.");
        String pay=PstBillDetail.formatCurrency(bayar);
        pay=pay.substring(0,pay.length()-3);
        obj.setLineRightAlign(baris+2,PI_COL_START_TOTAL+1,pay,10);
        obj.setLine(baris+3,PI_COL_START_BAR_CODE, "Kembali");
        obj.setLine(baris+3,PI_COL_START_QTY+1,"Rp.");
        String ret=PstBillDetail.formatCurrency(kembali);
        ret=ret.substring(0,ret.length()-3);
        if (ret.length()==0)
                    ret="0";
        obj.setLineRightAlign(baris+3,PI_COL_START_TOTAL+1,ret,10);
        obj.setLineRptStr(baris+4,PI_COL_START_BAR_CODE-1, "-",PI_COL_START_SIZE-3);
    }
}
