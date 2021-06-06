/*
 * TestRemPrnMan.java
 *
 * Created on July 27, 2003, 8:33 PM
 */

package com.dimata.printman;

import java.util.Vector;

/**
 *
 * @author  ktanjana
 * @version 
 */ 
public class TestRemPrnMan {

    /** Creates new TestRemPrnMan */
    public TestRemPrnMan() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        //TestHostIP();
        RemotePrintMan.reloadPrintersOnAllHost();
    }


    public static void testList(){
        String rmtHost = "127.0.0.1";
        System.out.println("Request from :  "+rmtHost);
        System.out.println("All Printers on Local : ");
        Vector prnLst = null;
        PrinterHost host = null;
        try{
            host = DSJ_PrinterHostXML.getPrinterHostByIP(rmtHost);
            if(host!=null)
                prnLst = host.getListOfPrinters(true, rmtHost);//getPrinterListWithStatus(host);
            
            if(prnLst!=null){
                for(int i = 0; i< prnLst.size();i++){
                    try{
                        PrnConfig prnConf= (PrnConfig) prnLst.get(i);
                        System.out.print(" value="+ host.getHostIP()+";"+prnConf.getPrnIndex()+" => ");
                        System.out.println(host.getHostName()+ " / " + prnConf.getPrnIndex()+ " "+prnConf.getPrnName()+" "+prnConf.getPrnPort());
                        
                    } catch (Exception exc){System.out.println("ERROR "+ exc);}
                }
            }
        } catch (Exception exc1){System.out.println("ERROR" + exc1);}
        
        
        System.out.println("All Shared Printers : ");
        Vector hostLst = RemotePrintMan.getHostList();
        prnLst = null;
        host = null;
        if(hostLst!=null){
            for(int h = 0; h< hostLst.size();h++){
                try{
                    host = (PrinterHost )hostLst.get(h);
                    if(host!=null)
                        prnLst = host.getListOfPrinters(true);//getPrinterListWithStatus(host);
                    if(prnLst!=null){
                        for(int i = 0; i< prnLst.size();i++){
                            try{
                                PrnConfig prnConf= (PrnConfig) prnLst.get(i);
                                System.out.print(" value="+host.getHostIP()+";"+prnConf.getPrnIndex()+ " " );
                                System.out.println(host.getHostName()+ " / " + prnConf.getPrnIndex()+ " "+prnConf.getPrnName()+" "+prnConf.getPrnPort());
                            } catch (Exception exc){System.out.println("ERROR "+ exc);}
                        }
                    }
                } catch (Exception exc1){System.out.println("ERROR" + exc1);}
            }
        }
        
        System.out.println("All Printers :");
        hostLst = RemotePrintMan.getHostList();
        prnLst = null;
        host = null;
        if(hostLst!=null){
            for(int h = 0; h< hostLst.size();h++){
                try{
                    host = (PrinterHost )hostLst.get(h);
                    if(host!=null)
                        prnLst = host.getListOfPrinters(false);//getPrinterListWithStatus(host);
                    if(prnLst!=null){
                        for(int i = 0; i< prnLst.size();i++){
                            try{
                                PrnConfig prnConf= (PrnConfig) prnLst.get(i);
                                System.out.print(" value ="+host.getHostIP()+";"+prnConf.getPrnIndex()+ " ");
                                System.out.println(host.getHostName()+ " / " + prnConf.getPrnIndex()+ " "+prnConf.getPrnName()+" "+prnConf.getPrnPort());
                                
                            } catch (Exception exc){System.out.println("ERROR "+ exc);}
                        }
                    }
                } catch (Exception exc1){System.out.println("ERROR" + exc1);}
            }
        }
        
    }
    
    public static void TestHostIP(){
        Vector prnLst = null;
        System.out.println(" JSP 2 1");
       
        System.out.println(" JSP 2 2");
        PrinterHost pHost = new PrinterHost();
        pHost.setHostIP("127.0.0.1");
        pHost.setPort(1099);
        pHost.setHostName("");
        pHost.setRMIObjName("RemotePrintTarget");
        try{
            System.out.println(" JSP 2 a");
            prnLst = RemotePrintMan.getPrinterList(pHost);
            System.out.println(" JSP 2 b");
            /*if(host!=null){
                prnLst = host.getListOfPrinters(true, rmtHost);//getPrinterListWithStatus(host);
                                System.out.println(" JSP 2 c");
                                }
             */
            if(prnLst!=null){
                for(int i = 0; i< prnLst.size();i++){
                    try{
                        PrnConfig prnConf= (PrnConfig) prnLst.get(i);
                        System.out.print(" <option value='"+ pHost.getHostIP()+";"+prnConf.getPrnIndex()+"'> ");
                        System.out.println(pHost.getHostName()+ " / " + prnConf.getPrnIndex()+ " "+prnConf.getPrnName()+" "+prnConf.getPrnPort());
                        System.out.print(" </option>");
                        
                    } catch (Exception exc){System.out.println("ERROR "+ exc);}
                }
            }
        } catch (Exception exc1){System.out.println("ERROR" + exc1);}
        
    }    
}
