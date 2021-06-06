/*
 * DSJRemotePrintTarget.java
 *
 * Created on July 18, 2003, 11:07 AM
 */

package com.dimata.printman; 

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;


/** Unicast remote object implementing java.rmi.Remote interface.
 *
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 *
 * Note :
 * - java.policy on JAVA_HOME has to be set to All Permision ( FOR NEXT FUTURE HAS TO BE DEFINED MORE SECURE )
 * - comm.jar has to be remove from <java home>/jre/lib/ext
 * - modified comm classes will be used instead of comm.jar
 */

public class RemotePrintTarget extends UnicastRemoteObject implements I_DSJ_PrintTarget {
    private static DSJ_PrinterService prnSvc = null;;
    private static Thread printThr = null;
    private static PrinterHost myHost= new PrinterHost();
    public static  PrinterHost printManSvcHost = new PrinterHost();
    static{
        printManSvcHost.setHostName("?");
        printManSvcHost.setHostIP("");
        printManSvcHost.setPort(1099);
        printManSvcHost.setRMIObjName("PrintManSvc");
        
    }
    
    /** Constructs DSJRemotePrintTarget object and exports it on default port.
     */
    public RemotePrintTarget() throws RemoteException {
        super();
        initPrintSvc();
    }
    
    /** Constructs DSJRemotePrintTarget object and exports it on specified port.
     * @param port The port for exporting
     */
    public RemotePrintTarget(int port) throws RemoteException {
        super(port);
        initPrintSvc();
    }
    
    private void initPrintSvc(){
        if(prnSvc==null){
            prnSvc = DSJ_PrinterService.getInstance();
            printThr=new Thread(prnSvc);
            prnSvc.running = true;
            printThr.setDaemon(false);
            printThr.start();
            System.out.println("Print Thread New Initialized");
        }
    }
    
    /** Register DSJRemotePrintTarget object with the RMI registry.
     * @param name - name identifying the service in the RMI registry
     * @param create - create local registry if necessary
     * @throw RemoteException if cannot be exported or bound to RMI registry
     * @throw MalformedURLException if name cannot be used to construct a valid URL
     * @throw IllegalArgumentException if null passed as name
     */
    public static void registerToRegistry(String name, Remote obj, boolean create) throws RemoteException, MalformedURLException{
        
        if (name == null) throw new IllegalArgumentException("registration name can not be null");
        
        try {
            Naming.rebind(name, obj);
        } catch (RemoteException ex){
            if (create) {
                Registry r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                r.rebind(name, obj);
            } else throw ex;
        }
    }
    
    /** Main method.
     * starting arguments :   <host name> < host IP> < rmi port > <rmi object>
     */
    public static void main(String[] args) {
        System.setSecurityManager(new RMISecurityManager());
        
        try {
            System.out.println("l="+args.length);
            if((args!=null) && (args.length>0)){
                if(args[0].equalsIgnoreCase("help")){
                    System.out.println("<host name> < host IP> < rmi port > <rmi object> <Ip of Remote Management Service> " );
                    System.out.println("OR \n <Ip of Remote Management Service> " );
                    return;
                }
                
                switch (args.length){
                    
                    case 1 :{
                        InetAddress myInet = InetAddress.getLocalHost();
                        myHost.setHostName(myInet.getHostName());
                        myHost.setHostIP(myInet.getHostAddress());
                        myHost.setPort(1099);
                        myHost.setRMIObjName("RemotePrintTarget");
                        printManSvcHost.setHostIP(new String(args[0]));
                        break;
                    }
                    
                    case 4: {
                        myHost.setHostName(new String(args[0]));
                        myHost.setHostIP(new String(args[1]));
                        myHost.setPort(Integer.parseInt(new String(args[2])));
                        myHost.setRMIObjName(new String(args[3]));
                        printManSvcHost.setHostIP("");
                        break;
                    }
                    
                    case 5: {
                        myHost.setHostName(new String(args[0]));
                        myHost.setHostIP(new String(args[1]));
                        myHost.setPort(Integer.parseInt(new String(args[2])));
                        myHost.setRMIObjName(new String(args[3]));
                        printManSvcHost.setHostIP(new String(args[4]));
                        break;
                    }
                    default:
                        System.out.println("run with parameter: RemotePrintTarget  help   to get options " );
                        return;
                }
            } else {
                InetAddress myInet = InetAddress.getLocalHost();
                myHost.setHostName(myInet.getHostName());
                myHost.setHostIP(myInet.getHostAddress());
                myHost.setPort(1099);
                myHost.setRMIObjName("RemotePrintTarget");
                printManSvcHost.setHostIP(""); // no remote management service / stand alone
            }
            
            System.out.println("Dimata(r) RemotePrintTarget Version 0.1 (C) 2003\n "
            + "Start with default parameters :" + myHost.getHostName() + " "
            + myHost.getHostIP()+ " " + myHost.getPort() + " " + myHost.getRMIObjName());
            String manHost = printManSvcHost.getHostIP(); 
            if((manHost!=null) && (manHost.length()>0)){
                System.out.println(" >>>>> HOST MAN SVC = " + printManSvcHost.getHostIP() );
            } else {
                System.out.println(" >>>>> STANDALONE <<<<< ");                
            }
            try{
                DSJ_PrinterXML dummy = new DSJ_PrinterXML();   // to initiate loadPrinters();
            } catch (Exception exc){
                System.out.println(" >> reloadLisfOfPrinter "+ exc);
            }
            
            myHost.setListOfPrinters(DSJ_PrinterXML.getListPrinter());
            if( (printManSvcHost.getHostIP()!=null) && (printManSvcHost.getHostIP().length()>0))
                regToPrinterManSvc();
            
            System.out.println("Start Remote Printer");
            RemotePrintTarget obj = new RemotePrintTarget();
            registerToRegistry("RemotePrintTarget", obj, true);
            System.out.println("RemotePrintTarget Object Registered");
            
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public int printObj(DSJ_PrintObj ObjPrint) throws RemoteException {
        //prnSvc = DSJ_PrinterService.getInstance();
        initPrintSvc();
        prnSvc.print(ObjPrint);
        //prnSvc.running = true;
        //prnSvc.run_x();
        
        //PrinterDriverLoader prLd = new PrinterDriverLoader(ObjPrint);
        //prLd.printObject(ObjPrint);
        
        return  prnSvc.getStatusPrnDriverLoader(ObjPrint.getPrnIndex());
    }
    
    /** Hello method usually returns "Hello".
     */
    public String hello() throws RemoteException {
        System.out.println("Remote client greeting me !");
        return "Hello remote Client";
    }
    
    public void stopPrintSvc() throws RemoteException{
        prnSvc.running = false;
        prnSvc = null;;
        printThr = null;
        System.out.println("Remote client Stop Print Svc !");
    }
    
    
    public Vector getLisfOfPrinter() throws RemoteException{
        return DSJ_PrinterXML.getListPrinter();
    }
    
    public Vector reloadLisfOfPrinter() throws RemoteException{
        try{ DSJ_PrinterXML.loadPrinters();
        } catch (Exception exc){
            System.out.println(" >> reloadLisfOfPrinter "+ exc);
        }
        return DSJ_PrinterXML.getListPrinter();
    }
    
    
    public Vector getStatusOfPrinter() throws RemoteException{
        Vector prn=DSJ_PrinterXML.getListPrinter();
        if( (prn!=null) && (prn.size()>0)){
            for(int i=0;i<prn.size();i++){
                PrnConfig pc = (PrnConfig) prn.get(i);
                pc.setPrnStatus(DSJ_PrinterService.getStatusPrnDriverLoader(pc.getPrnIndex()));
            }
        }
        return prn;
    }
    
    public int pausePrint(PrnConfig prn) throws RemoteException{
        return DSJ_PrinterService.pausePrint(prn);
        
    }
    
    public int resumePrint(PrnConfig prn) throws RemoteException{
        return DSJ_PrinterService.resumePrint(prn);
    }
    
    public int cancelPrint(PrnConfig prn) throws RemoteException{
        return DSJ_PrinterService.cancelPrint(prn);
    }
    
    public static void regToPrinterManSvc(){
        try{
            
            
            System.setSecurityManager(new RMISecurityManager());
            
            String nameLookUp = "//"+printManSvcHost.getHostIP()+":"+printManSvcHost.getPort()+"/"+printManSvcHost.getRMIObjName();
            I_DSJ_PrintMan prnManSvc = (I_DSJ_PrintMan) Naming.lookup(nameLookUp);
            prnManSvc.setPrinterHost(myHost);
            System.out.println("Printers are registered to :"+ nameLookUp);
        }catch (Exception e){
            System.out.println(" CANNOT CONTACT PRINT MANAGEMENT SVC \n EXCP : "+e);
        }
        
    }
     
}
