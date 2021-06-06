/*
 * RemotePrinterHost.java
 *
 * Created on July 19, 2003, 8:29 PM
 */

package com.dimata.printman;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author  ktanjana
 * @version 
 *
 * 
 Printer of Index = 1 will be set as default local printer
 */
public class PrinterHost implements Serializable{

    private String hostName="localhost";
    private String hostIP="localhost";
    private int    port=1099;
    private String RMIObjName="RemotePrintTarget";
    private Vector listPrinter = new Vector(1,1);
       
    /** Creates new RemotePrinterHost */
    public PrinterHost() {
    }
    
    public void setHostName(String hostName){this.hostName=hostName;}
    public void setHostIP(String hostIP){this.hostIP=hostIP;}
    public void setPort(int port){this.port=port;}
    public void setRMIObjName(String RMIObjName){this.RMIObjName=RMIObjName;}    
    public String getHostName(){return this.hostName;}
    public String getHostIP(){return this.hostIP;}
    public int getPort(){return this.port;}
    public String getRMIObjName(){return this.RMIObjName;}
    
    /**
     * get list of printer of a host
     * parameters :
     *  - sharedOnly: set false for getting printers shared only, 
     *  - workstHostIP of the workstation IP. If workstHostIP equals to Printer Host , then sharedOnly will be ignored; 
     * return a copy of the vector                   
     */
    public Vector getListOfPrinters(boolean sharedOnly, String workstHostIP){
        if(workstHostIP== null)
            return new Vector();
        if(workstHostIP.equals(this.hostIP))
            sharedOnly=false;
             
        Vector lst = new Vector();
        if(listPrinter!=null){
            for(int i=0;i<listPrinter.size();i++){
                PrnConfig prn = (PrnConfig)listPrinter.get(i);
                if( (prn!=null) && ((sharedOnly==false) || (prn.getShared()))) {
                    lst.add(prn);
                }
            }
        }
        
        return lst;
    }

     /* get list of printer of a host
     * parameters :
    *  - sharedOnly: set false for getting printers shared only, 
    */
    public Vector getListOfPrinters(boolean sharedOnly){

        Vector lst = new Vector();
        if(listPrinter!=null){
            for(int i=0;i<listPrinter.size();i++){
                PrnConfig prn = (PrnConfig)listPrinter.get(i);
                if( (prn!=null) && ((sharedOnly==false) || (prn.getShared()))) {
                    lst.add(prn);
                }
            }
        }
        return lst;
    }

    public Vector getListOfPrinters(){
        return getListOfPrinters(false);
    }
    
    
    public void setListOfPrinters(Vector listPrinter){this.listPrinter=listPrinter;}

     /* get printer config by printer Index
     * parameters :
    *  
    */
    public PrnConfig getPrnConfig(int idx){
        PrnConfig prn=null;
        if(listPrinter!=null){
            for(int i=0;i<listPrinter.size();i++){
                 prn = (PrnConfig)listPrinter.get(i);                
                if((prn!=null) && (prn.getPrnIndex()==idx)) {
                    return prn;
                }
            }
        }
        return prn;
    }
    
}
