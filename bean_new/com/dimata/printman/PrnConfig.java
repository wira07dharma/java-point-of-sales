
package com.dimata.printman;

import java.io.Serializable;

/*
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 */

public class PrnConfig implements Serializable{
    
    public PrnConfig() {
    }
    
    
/*    public PrnConfig(int prnIndex) {
        try{
            // get other data from xml file
            this.prnIndex = prnIndex;
            this.prnName = "LX300 at LPT1";
            this.prnPort = "LPT1";
            this.prnDrvClassName = "com.dimata.printman.DSJ_PrnDrv_LX300";
            
        }catch(Exception e){
            System.out.println("Err PrnConfig : "+e.toString());
        }
    } 
*/    
    
    public int getPrnIndex(){ return prnIndex; }
    
    public void setPrnIndex(int prnIndex){ this.prnIndex = prnIndex; }
    
    public String getPrnName(){ return prnName; }
    
    public void setPrnName(String prnName){ this.prnName = prnName; }
    
    public String getPrnDrvClassName(){ return prnDrvClassName; }
    
    public void setPrnDrvClassName(String prnDrvClassName){ this.prnDrvClassName = prnDrvClassName; }
    
    public String getPrnPort(){ return prnPort; }
    
    public void setPrnPort(String prnPort){ this.prnPort = prnPort; }

    public int getPrnStatus(){ return prnStatus; }
    
    public void setPrnStatus(int prnStatus){ this.prnStatus = prnStatus; }
    
    public boolean getShared(){return this.shared;}
    public void setShared(boolean shared){this.shared=shared;}
    
    private int prnIndex;
    private String prnName;
    private String prnDrvClassName;
    private String prnPort;
    private int prnStatus=I_DSJ_PrinterDriver.ST_IDLE;
    private boolean shared=false;
        
}
