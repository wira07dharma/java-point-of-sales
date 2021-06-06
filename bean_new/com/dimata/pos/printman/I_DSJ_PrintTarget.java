/*
 * I_DSJ_PrintTarget.java
 *
 * Created on July 18, 2003, 11:07 AM
 * by I Ketut Kartika T.
 * copyright : PT. Dimata Sora Jayate
 */

package com.dimata.pos.printman;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/** Remote interface.
 *
 * @author ktanjana
 * @version 1.0
 */
public interface I_DSJ_PrintTarget extends Remote {
    /** Hello method usually returns "Hello".
     */
    public String hello() throws RemoteException;
    
    public int printObj(DSJ_PrintObj ObjPrint) throws RemoteException;
    
    public void stopPrintSvc() throws RemoteException;
    
    public Vector getLisfOfPrinter() throws RemoteException;

    public Vector reloadLisfOfPrinter() throws RemoteException;
        
    public Vector getStatusOfPrinter() throws RemoteException;
    
    public int pausePrint(PrnConfig prn) throws RemoteException;
    
    public int resumePrint(PrnConfig prn) throws RemoteException;
    
    public int cancelPrint(PrnConfig printer) throws RemoteException;
    
}
