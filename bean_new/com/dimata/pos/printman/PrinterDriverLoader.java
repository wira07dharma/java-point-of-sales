
package com.dimata.pos.printman;

import java.io.Serializable;
import java.util.Vector;

/* 
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 */


public class PrinterDriverLoader  implements Runnable, Serializable {
    
    public PrinterDriverLoader(DSJ_PrintObj prnOb) {
        try{
            this.prnObj = prnOb;
            PrnConfig prConf = DSJ_PrinterXML.getPrnConfig(prnObj.getPrnIndex());
            String prnDriverClass =  prConf.getPrnDrvClassName();
            i_PrnDriver = (I_DSJ_PrinterDriver) Class.forName(prnDriverClass).newInstance();
            i_PrnDriver.initPrinter(prConf);
            i_PrnDriver.setCPI(this.prnObj.getCpiIndex()); 
        }catch(Exception e){
            System.out.println(" ! EXC : PrinterDriverLoader =  "+e.toString());
            
        }
    }
    
    public DSJ_PrintObj getPrnObj(){ return prnObj; }
    
    public void setPrnObj(DSJ_PrintObj prnObj){ this.prnObj = prnObj; }
    
    public int getPrnIndex() {
        
        return     prnObj.getPrnIndex();
    }
    
    private void setPage(){
        i_PrnDriver.setPageWidth(prnObj.getPageWidth()) ;
        i_PrnDriver.setPageLength(prnObj.getPageLength());
       // i_PrnDriver.setLeftMargin(prnObj.getLeftMargin());
        i_PrnDriver.setRightMargin(prnObj.getRightMargin());
        i_PrnDriver.setTopMargin(prnObj.getTopMargin());
        i_PrnDriver.setButtomMargin(prnObj.getBottomMargin());
        
       // if(prnObj.getSpacing()!=0)
       //    i_PrnDriver.setVtSpacing(prnObj.getVerticalSpacing(),prnObj.getSpacing());
       // else
       //    i_PrnDriver.setVtSpacing(prnObj.getVerticalSpacing());
        
        //i_PrnDriver.setFont(prnObj.getFont());
    }
    
    public void run() {
        try{
            // printing process
            // set Page
            setPage();
            Thread.sleep(threadSleep);
            //check open cashdrawer
            if(cashDrawer){
                i_PrnDriver.setOpenCash();
            }
            // print lines
            printObject(prnObj);
            Thread.sleep(threadSleep);
            i_PrnDriver.formFeed();
            i_PrnDriver.endPrinting();
            DSJ_PrinterService.removePrnDriverLoader(getPrnIndex());
        }catch(Exception e){
            System.out.println(" ! EXC : PrinterDriverLoader > run =  "+e.toString());
            
        }
    }
    
    public I_DSJ_PrinterDriver getPrnDriver(){
        return i_PrnDriver;
    }
    
    public void setPrnDriver(I_DSJ_PrinterDriver i_PrnDriver){
        this.i_PrnDriver = i_PrnDriver;
    }
    
    public int getThreadSleep(){
        return threadSleep;
    }
    
    public void setThreadSleep(int threadSleep){
        this.threadSleep = threadSleep;
    }
    
    public boolean printObject(DSJ_PrintObj prnObj){
        Vector lines = prnObj.getLines();
        System.out.println(" Printer " +  prnObj.getPrnIndex() + " ------------------------------");
        //int pages = lines.size()/prnObj.getPageLength();
        int lnSkip = 0;
        int number = 1;
        if(lines!=null && lines.size()>0){
            for(int i=0;(i<lines.size()) && continuePrint ;i++){
                String str = (String)lines.get(i);
                try{
                    while (pausePrinter && continuePrint){
                        errCode = i_PrnDriver.ST_PAUSED;
                        Thread.sleep(threadSleep*20);                        
                    }
                    
                    boolean bool = i_PrnDriver.isPrinterTimedOut();
                    while(bool && continuePrint){ // && continuePrint
                        errCode = i_PrnDriver.ST_ERR_PAPER_OUT;
                        Thread.sleep(threadSleep*10);
                        System.out.println(" ! ERR : > printObject =  at line "+ i + " => "+i_PrnDriver.errMessage[errCode]);
                        bool = i_PrnDriver.isPrinterTimedOut();
                        if (bool) continuePrint = false; // for test only
                    }
                    if( continuePrint==false) {
                        errCode= i_PrnDriver.ST_ERR_PRINTING_CANCELED;
                        return false;
                    }
                    errCode= i_PrnDriver.ST_PRINTING; 
                    
                    if(prnObj.getPageLength()==lnSkip){
                        //String s = "page"+number+" - "+pages;
                        //i_PrnDriver.println(s);
                        
                        for(int f=0;f<prnObj.getSkipLineIsPaperFix();f++){
                            System.out.println(" ");
                            i_PrnDriver.println(" ");
                        } 
                        lnSkip = -1;
                        number++;
                        if(i<=prnObj.getIndexEndHeader()){
                            lnSkip = printHeader(prnObj);
                        }
                    }
                    System.out.println(str);
                    i_PrnDriver.println(str);
                    
                    Thread.sleep(threadSleep);
                }catch(Exception e){
                    System.out.println(" ! EXC : PrinterDriverLoader > printObject =  "+e.toString());
                    
                }
                lnSkip++;
            }
            return true;
        }
        return false;
    }
    
    public int printHeader(DSJ_PrintObj prnObj){
        int line = 0;
        Vector header = prnObj.getHeader();
        if(header!=null && header.size()>0){
            for(int i=0;i<header.size();i++){
                String str = (String)header.get(i);
                System.out.println(str);
                i_PrnDriver.println(str);
                line++;
            }
        }
        return line;
    }
    
    public boolean getContinuePrint(){ return continuePrint; }
    
    public void setContinuePrint(boolean continuePrint){ this.continuePrint = continuePrint; }
    
    public int getErrCode(){ return errCode; }
    
    public void setErrCode(int errCode){ this.errCode = errCode; }
    
    public void pausePrint(){pausePrinter=true;}
    public void resumePrint(){pausePrinter=false;}
    public void cancelPrint(){continuePrint=false;}
    
    /** @link dependency */
    /*#I_DSJ_PrinterDriver lnkI_DSJ_PrinterDriver;*/
    private DSJ_PrintObj prnObj = new DSJ_PrintObj();
    private I_DSJ_PrinterDriver i_PrnDriver;
    private int threadSleep=40;
    private boolean continuePrint=true;
    private int errCode=0;
    private boolean pausePrinter=false;
    private boolean cashDrawer = true;
        
}
