/*
 * TestPrnSvc.java
 *
 *  Created on July 25, 2003, 12:25 PM
 */

package com.dimata.pos.printman;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 *
 * @author ktanjana
 * @version 1.0
 */
public class TestPrnSvc extends Object {
    
    /** Creates new TestPrnSvc */
    public TestPrnSvc() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setSecurityManager(new RMISecurityManager());
            I_DSJ_PrintTarget prnTarget=null;
        try{
            PrinterHost rmtPrnHost = new PrinterHost();
            rmtPrnHost.setHostIP("127.0.0.1");
            rmtPrnHost.setPort(1099);
            rmtPrnHost.setRMIObjName("RemotePrintTarget");
            String nameLookUp = "//"+rmtPrnHost.getHostIP()+":"+rmtPrnHost.getPort()+"/"+rmtPrnHost.getRMIObjName();
            System.out.println("getPrinterListWithStatus >  HOST " + rmtPrnHost.getHostName()+ " = " + nameLookUp);
                        
            prnTarget = (I_DSJ_PrintTarget) Naming.lookup(nameLookUp);
            
            System.out.println(prnTarget.hello());
            
        }catch (Exception e){
            System.out.println(" EXCP : "+e);
        }
    }
}
