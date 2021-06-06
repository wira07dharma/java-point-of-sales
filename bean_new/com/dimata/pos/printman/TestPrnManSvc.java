/*
 * TestPrnManSvc.java
 *
 *  Created on July 25, 2003, 4:10 PM
 */

package com.dimata.pos.printman;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 *
 * @author ktanjana
 * @version 1.0
 */
public class TestPrnManSvc extends Object {
    public static int  manRMIPort = 1099; 

    /** Creates new TestPrnManSvc */
    public TestPrnManSvc() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        System.setSecurityManager(new RMISecurityManager());
            I_DSJ_PrintMan prnManSvc=null;
        try{
         if((args!=null) && (args.length>0)){
             manRMIPort = Integer.parseInt(args[0]);
             System.out.println("Port "+manRMIPort);
         }
        } catch (Exception e){
            System.out.println("Exc " + e + "\n" + " PARAMETER : [port]");
        }
            
        try{
            PrinterHost rmtPrnHost = new PrinterHost();
            rmtPrnHost.setHostIP("127.0.0.1");
            rmtPrnHost.setPort(1099);
            rmtPrnHost.setRMIObjName("PrintManSvc");
            String nameLookUp = "//"+rmtPrnHost.getHostIP()+":"+rmtPrnHost.getPort()+"/"+rmtPrnHost.getRMIObjName();
            System.out.println("getPrinterListWithStatus >  HOST " + rmtPrnHost.getHostName()+ " = " + nameLookUp);
                        
            prnManSvc = (I_DSJ_PrintMan) Naming.lookup(nameLookUp);
            
            prnManSvc.reloadPrintersOnAllHost();
            System.out.println("TEST END");
            
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }        
    }
}
