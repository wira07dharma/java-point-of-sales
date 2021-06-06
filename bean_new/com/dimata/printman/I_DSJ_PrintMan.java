/*
 * I_DSJ_PrintMan.java
 *
 * Created on July 23, 2003, 1:36 PM
 */

package com.dimata.printman;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
 
/** Remote interface.
 *
 * @author ktanjana
 * @version 1.0
 */
public interface I_DSJ_PrintMan extends Remote {
    public Vector getHostList() throws RemoteException;    
    public Vector getHostList(boolean reload) throws RemoteException;
    public Vector getPrinterList(PrinterHost rmtPrnHost) throws RemoteException;
    public Vector reloadPrinterList(PrinterHost rmtPrnHost) throws RemoteException;
    public Vector getPrinterListWithStatus(PrinterHost rmtPrnHost) throws RemoteException;
    public void stopPrinterService(PrinterHost rmtPrnHost) throws RemoteException;
    public PrinterHost getPrinterHost(String hostIpIdx, String separ) throws RemoteException;
    public PrnConfig getPrinterConfig(String hostIpIdx, String separ) throws RemoteException;
    public int printObj(PrinterHost rmtPrnHost, DSJ_PrintObj obj) throws RemoteException;
    public int pausePrinter(PrinterHost rmtPrnHost, PrnConfig prn) throws RemoteException;
    public int resumePrinter(PrinterHost rmtPrnHost, PrnConfig prn) throws RemoteException;
    public int cancelPrinter(PrinterHost rmtPrnHost, PrnConfig prn) throws RemoteException;
    public int setPrinterStatus(PrinterHost rmtPrnHost, PrnConfig prn, int cmd) throws RemoteException;
    public void loadPrintersOnAllHost() throws RemoteException;
    public void reloadPrintersOnAllHost() throws RemoteException;
    
    public void setPrinterHost(PrinterHost rmtPrnHost) throws RemoteException;
    public PrinterHost getPrinterHostByIP(String ip) throws RemoteException;
    public Vector getPrinterHostExceptIP(String ip) throws RemoteException;
    

}
