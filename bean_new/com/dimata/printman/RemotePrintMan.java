/*
 * RemotePrintMan.java
 *
 *  Created on July 19, 2003, 8:16 PM
 */

package com.dimata.printman;
 
import java.rmi.*;
import java.util.*;

/**
 *
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 *
 * Remote Printer Target Manager will be used to :
 *  - registered  remote printer targets : IP and RMI Object Server Name
 *  - list, control, get status, object que of remote printers
 *
 * List of Remote Printers will be stored into XML FILE :
 *  - Host Name, IP, Port, RMI object name
 * e.g.
 * <?xml version="1.0"?>
 * <hosts>
 * <host>
 * <name>localhost</name>
 * <ip>127.0.0.1</ip>
 * <port>1099</port>
 * <rmiobj>RemotePrintTarget</rmiobj>
 * </host>
 * <host>
 * <name>ktanjana</name>
 * <ip>192.168.0.123</ip>
 * <port>1099</port>
 * <rmiobj>RemotePrintTarget</rmiobj>
 * </host>
 * </hosts>
 */
public class RemotePrintMan extends Object {
    
    private static PrinterHost printManSvcHost = new PrinterHost();
    private static I_DSJ_PrintMan prnManSvc=null;
    static{
        try{
            printManSvcHost.setHostName(java.net.InetAddress.getLocalHost().getHostName());
            printManSvcHost.setHostIP(java.net.InetAddress.getLocalHost().getHostAddress());
            printManSvcHost.setPort(1099);
            printManSvcHost.setRMIObjName("PrintManSvc");
            
            System.setSecurityManager(new RMISecurityManager());
            
            String nameLookUp = getNameLookUp();
            prnManSvc = (I_DSJ_PrintMan) Naming.lookup(getNameLookUp());
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
    }
    
    
    /** Creates new RemotePrintMan */
    public RemotePrintMan() {
    }
    
    public static void main(String[] args) {
        try{
            PrintManSvc.stReloadPrintersOnAllHost();
        } catch(Exception exc){
            System.out.println(exc);
        }
        
        
    }
    
    public static String getNameLookUp(){
        return "//"+printManSvcHost.getHostIP()+":"+printManSvcHost.getPort()+"/"+printManSvcHost.getRMIObjName();
    }
    
    /* Get list of printer hosts
     * return as Vector
     */
    public static Vector getHostList(){
        try{
            return prnManSvc.getHostList();
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return new Vector();
    }
    
    public static Vector getHostList(boolean reload){
        try{
            return prnManSvc.getHostList(reload);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return new Vector();
    }
    
    /* get printer list of a host
     * return as
     */
    public static Vector getPrinterList(PrinterHost printManSvcHost){
        try{
            return prnManSvc.getPrinterList(printManSvcHost);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return new Vector();
    }
    
    public static Vector reloadPrinterList(PrinterHost printManSvcHost){
        try{
            return prnManSvc.reloadPrinterList(printManSvcHost);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return new Vector();
    }
    
    
    /* get printer list of a host with status of each printer
     * return as
     */
    public static Vector getPrinterListWithStatus(PrinterHost printManSvcHost){
        try{
            return prnManSvc.getPrinterListWithStatus(printManSvcHost);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return new Vector();
    }
    
    /* stop printer status !!! ALL OBJECT WILL BE REMOVED
     */
    public static void stopPrinterService(PrinterHost printManSvcHost){
        try{
            prnManSvc.getPrinterListWithStatus(printManSvcHost);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
    }
    
    /* get printer host from String :  <host IP><separ><printer index>
     */
    public static PrinterHost getPrinterHost(String hostIpIdx, String separ){
        try{
            return prnManSvc.getPrinterHost(hostIpIdx, separ);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return null;
    }
    
    public static PrnConfig getPrinterConfig(String hostIpIdx, String separ){
        try{
            return prnManSvc.getPrinterConfig(hostIpIdx, separ);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return null;
    }
    
    /* print object into a host
     * return int as error message
     */
    public static int printObj(PrinterHost printManSvcHost, DSJ_PrintObj obj){
        try{
            return prnManSvc.printObj(printManSvcHost, obj);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return I_DSJ_PrinterDriver.ST_ERR_NO_HOST;
    }
    
    public static  int pausePrinter(PrinterHost printManSvcHost, PrnConfig prn){
        return setPrinterStatus(printManSvcHost,  prn, I_DSJ_PrinterDriver.ST_PAUSED);
    }
    
    public static  int resumePrinter(PrinterHost printManSvcHost, PrnConfig prn){
        return setPrinterStatus(printManSvcHost,  prn, I_DSJ_PrinterDriver.ST_PRINTING);
    }
    
    public static  int cancelPrinter(PrinterHost printManSvcHost, PrnConfig prn){
        return setPrinterStatus(printManSvcHost,  prn, I_DSJ_PrinterDriver.ST_IDLE);
    }
    /* set printer into a host
     * return int as error message
     */
    private static int setPrinterStatus(PrinterHost printManSvcHost, PrnConfig prn, int cmd){
        try{
            return  prnManSvc.setPrinterStatus(printManSvcHost, prn, cmd);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        return I_DSJ_PrinterDriver.ST_ERR_NO_HOST;
    }
    
    public static void reloadPrintersOnAllHost(){
        try{
             prnManSvc.reloadPrintersOnAllHost();
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        
    }
    
    public static PrinterHost getPrinterHostByIP(String ip) throws java.rmi.RemoteException{
        try{
            return prnManSvc.getPrinterHostByIP(ip);
        }catch (Exception e){
            System.out.println("  EXCP : "+e);
        }
        
        return null;
    }
    
    public static Vector getPrinterHostExceptIP(String ip) throws java.rmi.RemoteException{
        try{
            return DSJ_PrinterHostXML.getPrinterHostExceptIP(ip);
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
        
        return null;
        
        
    }
    
    
}
