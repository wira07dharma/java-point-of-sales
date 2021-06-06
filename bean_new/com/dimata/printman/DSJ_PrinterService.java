/* Generated by Together */

package com.dimata.printman;

import java.io.Serializable;
import java.util.Vector;

/*  
 * @author ktanjana 
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 */



public class DSJ_PrinterService implements Runnable, Serializable  {
    
    
    static DSJ_PrinterService PrnSvc = null;      // single instance
    
    /** @link dependency */
    /*#PrinterDriverLoader lnkPrinterDriverLoader;*/
    /* vector while print  */
    static Vector  listPrnLoader = new Vector(1,1);
    
    /** @link dependency */
    /*#DSJ_PrintObj lnkDSJ_PrintObj;*/
    /* vector will print */
    static Vector  prnObjBuffer = new Vector(1,1);
    
    private static boolean  AutoRunning = false;  // true = auto , false = manual triggered
    private static boolean  CurrentlyRunningManually = false;  // true = auto , false = manual triggered
    public int threadSleepScheduler=40; // raster check for print buffer & loader
    public static boolean running=false; // running status of printer service
    
    
    public static DSJ_PrinterService getInstance(){
        if(PrnSvc==null){
            PrnSvc = new DSJ_PrinterService();
        }
        return PrnSvc;
    }
    
    boolean getAutoRunning(){
        return AutoRunning;
    }
    
    void setAutoRunning(boolean modeAuto){
        this.AutoRunning = modeAuto;
    }
    
    public void run_x() {
        System.out.println("PRINTING SERVICE => START");
        while(running){
            try{
                Thread.sleep(threadSleepScheduler);
                // cek buffer
                DSJ_PrintObj prnObj = getBuffer();
                if(prnObj!=null){   // cek buffer
                    System.out.println("PRINTING Obj => "+ prnObj.getObjDescription() );
                    removeTopBuffer();
                    if( existPrnDriverLoader(prnObj.getPrnIndex())==false){
                        PrinterDriverLoader prnLdr = new PrinterDriverLoader(prnObj);
                        Thread thr = new Thread(prnLdr);
                        thr.setDaemon(false);
                        thr.start();
                    }
     
                }
            }
            catch(Exception e){
                System.out.println("Exception "+e);
            }
        }    
        removePrnDriverLoader();
        System.out.println("PRINT SERVICE => STOP ; All printer drivers are removed ");
 
     
}
     
     
    
    synchronized public void run() {
        System.out.println("PRINTING SERVICE => START ; running = "+running);
        while(running || ((prnObjBuffer!=null) && (prnObjBuffer.size()>0)) 
              || ((listPrnLoader!=null) && (listPrnLoader.size()>0))){
            try{
                Thread.sleep(threadSleepScheduler);
                // cek buffer
                DSJ_PrintObj prnObj = getBuffer();
                if(prnObj!=null){   // cek buffer
                    System.out.println("PRINTING Obj => "+ prnObj.getObjDescription() );                    
                    if( existPrnDriverLoader(prnObj.getPrnIndex())==false){
                        PrinterDriverLoader prnLdr = new PrinterDriverLoader(prnObj);
                        addPrnDriverLoader(prnLdr);
                        removeTopBuffer(); // remove current object from buffer
                        Thread thr = new Thread(prnLdr);
                        thr.setDaemon(false);
                        thr.start();
                    }
                    
                }
                
                
                // running =false; // ONLy for TEST
                
            } catch(Exception e){
                System.out.println("Exception "+e);
                
            }
        }
            removePrnDriverLoader();
            System.out.println("PRINT SERVICE => STOP ; All printer drivers are removed ");        
        
    }
    
    public void runManually() {
        if(running && (!this.AutoRunning)){
            CurrentlyRunningManually = true;
        }
    }
    
    public static void print(DSJ_PrintObj prnObj){
        prnObjBuffer.add(prnObj);
        // add prn obj to buffer;
        
    }
    
    public DSJ_PrintObj getBuffer() {
        DSJ_PrintObj obj=null;
        if( (prnObjBuffer!=null) && (prnObjBuffer.size()>0)){
            obj = (DSJ_PrintObj) prnObjBuffer.get(0);
        }
        return obj;
        
    }
    
    public void removeTopBuffer() {
        if( (prnObjBuffer!=null) && (prnObjBuffer.size()>0)){
            prnObjBuffer.remove(0);
        }
    }
    
    public static boolean existPrnDriverLoader(int index){
        if( (listPrnLoader!=null) && (listPrnLoader.size()>0)){
            for(int i=0;i<listPrnLoader.size();i++){
                PrinterDriverLoader prnLdr = (PrinterDriverLoader )listPrnLoader.get(i);
                if(index==prnLdr.getPrnIndex())
                    return true ;
            }
        }
        return false;
    }

    public static int getStatusPrnDriverLoader(int index){
        if( (listPrnLoader!=null) && (listPrnLoader.size()>0)){
            for(int i=0;i<listPrnLoader.size();i++){
                PrinterDriverLoader prnLdr = (PrinterDriverLoader )listPrnLoader.get(i);
                if(index==prnLdr.getPrnIndex())
                    return prnLdr.getErrCode();
            }
        }
        return I_DSJ_PrinterDriver.ST_IDLE;
    }

    
    public static void addPrnDriverLoader(PrinterDriverLoader prnLdr){
        if(prnLdr==null)
            listPrnLoader = new Vector(1,1);
        
        listPrnLoader.add(prnLdr);
    }
    
    public static boolean removePrnDriverLoader(int index){
        if( (listPrnLoader!=null) && (listPrnLoader.size()>0)){
            for(int i=0;i<listPrnLoader.size();i++){
                PrinterDriverLoader prnLdr = (PrinterDriverLoader )listPrnLoader.get(i);
                if(index==prnLdr.getPrnIndex()){
                    listPrnLoader.remove(i);
                    return true  ;
                }
            }
        }
        return false;
    }
    
    public static int removePrnDriverLoader(){
        int num =0;
        if( (listPrnLoader!=null) && (listPrnLoader.size()>0)){
            num = listPrnLoader.size();
            
            for(int i=0;i<listPrnLoader.size();i++){
                PrinterDriverLoader prnLdr = (PrinterDriverLoader )listPrnLoader.get(i);
                prnLdr.setContinuePrint(false);
                listPrnLoader.remove(i);
                
            }
        }
        
        return num;
    }
    
    public static int pausePrint(PrnConfig prn){        
        if(prn==null)
            return I_DSJ_PrinterDriver.ST_ERR_PARAMETER;
            
        PrinterDriverLoader prnLdr = getPrinterDrv(prn.getPrnIndex());
        if(prnLdr==null)
            return I_DSJ_PrinterDriver.ST_IDLE;
        prnLdr.pausePrint();
        
        return I_DSJ_PrinterDriver.ST_PAUSED;
    }
    
    public static int resumePrint(PrnConfig prn){        
        if(prn==null)
            return I_DSJ_PrinterDriver.ST_ERR_PARAMETER;
            
        PrinterDriverLoader prnLdr = getPrinterDrv(prn.getPrnIndex());
        if(prnLdr==null)
            return I_DSJ_PrinterDriver.ST_IDLE;
        prnLdr.resumePrint();
        
        return I_DSJ_PrinterDriver.ST_PRINTING;
    }

    public static int cancelPrint(PrnConfig prn){        
        if(prn==null)
            return I_DSJ_PrinterDriver.ST_ERR_PARAMETER;
            
        PrinterDriverLoader prnLdr = getPrinterDrv(prn.getPrnIndex());
        if(prnLdr==null)
            return I_DSJ_PrinterDriver.ST_IDLE;
        prnLdr.cancelPrint();
        
        return I_DSJ_PrinterDriver.ST_IDLE;
    }
    
    public static PrinterDriverLoader getPrinterDrv(int index ){    
        PrinterDriverLoader prnLdr = null;
        if( (listPrnLoader!=null) && (listPrnLoader.size()>0)){
            for(int i=0;i<listPrnLoader.size();i++){
                prnLdr = (PrinterDriverLoader )listPrnLoader.get(i);
                if(index==prnLdr.getPrnIndex()){
                    break;
                }
            }
        }
        
        return prnLdr;
        
    }
    
    synchronized public boolean readyToStop()
    {
        if((running || ((prnObjBuffer!=null) && (prnObjBuffer.size()>0)))
            || ((listPrnLoader!=null) && (listPrnLoader.size()>0)))
        {
            return false;
        }
        return true;
    }
}

