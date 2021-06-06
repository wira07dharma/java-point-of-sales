/*
 * DSJ_PrinterHostXML.java
 *
 * Created on July 20, 2003, 1:21 PM
 */

package com.dimata.pos.printman;
/*
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 *
 *  * List of Remote Printers will be stored into XML FILE :
 *  - Host Name, IP, Port, RMI object name
 * e.g.
 * <?xml version="1.0"?>
   <hosts>
        <host>
                <name>localhost</name>
                <ip>127.0.0.1</ip>
                <port>1099</port>
                <rmiobj>RemotePrintTarget</rmiobj>
        </host>
        <host>
                <name>ktanjana</name>
                <ip>192.168.0.123</ip>
                <port>1099</port>
                <rmiobj>RemotePrintTarget</rmiobj>
        </host>
   </hosts>
 */

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
//import com.sun.xml.tree.*;
//import com.sun.xml.parser.Resolver;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
public class DSJ_PrinterHostXML {
    
    /** Creates new DSJ_PrinterHostXML */
    public DSJ_PrinterHostXML() {
    }
    
    public static void loadHosts() throws IOException {
        System.out.println(">>> DSJ_PrinterHostXML -> loadHosts");
        InputStream inStream;
        Element currentElement;
        try {
            SAXBuilder builder = new SAXBuilder(); //"
            //get the Configuration Document, with validation
            inStream = new FileInputStream(getPathConfigFile());
            Document doc = builder.build(inStream);
            // get the root element
            Element root = doc.getRootElement();
            if (root == null) {
                System.out.println("NULL ROOT.......................");
                throw new IOException("NULL XML ROOT.......................");
            }
            // get Value Element index
            List dbElement = root.getChildren("host");
            Iterator iList = dbElement.iterator();
            PrinterHost hostHost= null;
            //if(listHost==null)
            listHost= new Vector();
            //else
            //  listHost.removeAllElements();
            
            while (iList.hasNext()) {
                Element currElement = (Element)iList.next();
                hostHost = new PrinterHost();
                hostHost.setHostName(currElement.getChild("name").getText());
                hostHost.setHostIP(currElement.getChild("ip").getText());
                hostHost.setPort(Integer.parseInt(currElement.getChild("port").getText()));
                hostHost.setRMIObjName(currElement.getChild("rmiobj").getText());
                listHost.add(hostHost);
                System.out.println("Load host : "+hostHost.getHostName()+ " "+hostHost.getHostIP()+" "+
                hostHost.getPort()+" "+hostHost.getRMIObjName() );
                
            }
            
            
        } catch (Exception exc) {
            throw new IOException(exc.getMessage());
        }
    }
    
    public static PrinterHost getPrinterHostByIP(String ip){
        if(ip==null)
            return null;
        
        if(listHost==null)
            listHost= new Vector();
        
        if(listHost.size()<0){
            try{
                loadHosts();
            } catch(Exception exc){
                System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
            }
        }
        
        String localIP = getLocahostlIP();
        if(listHost!=null && listHost.size()>0){
            for(int i = 0; i<listHost.size(); i++){
                PrinterHost host = (PrinterHost)listHost.get(i);
                if(host.getHostIP().equals(ip) ||
                (host.getHostIP().equals("127.0.0.1") && (localIP.equals(ip))) ||
                (host.getHostIP().equals("localhost") && (localIP.equals(ip)))
                )
                    return host;
            }
        }
        return null;
    }
    
    public static String getLocahostlIP(){
        try{
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            return( localHost.getHostAddress());
        } catch (Exception exc){
            System.out.println("LOCAL IP = ''");
            return "";
        }
        
    }
    
    public static  void setPrinterHostByIP(PrinterHost phost){
        if(phost==null)
            return ;
        
        if(listHost==null)
            listHost= new Vector();
        
        if(listHost.size()<0){
            try{
                System.out.println("\n Load Hosts" );
                loadHosts();
            } catch(Exception exc){
                System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
            }
        }
        boolean okset=false;
        if(listHost!=null && listHost.size()>0){
            
            String localIP = getLocahostlIP();
            
            for(int i = 0; i<listHost.size(); i++){
                PrinterHost host = (PrinterHost)listHost.get(i);
                System.out.println(""+i+") List : "+ host.getHostIP()+" Coming :"+phost.getHostIP());
                
                if((host.getHostIP().equals(phost.getHostIP())) ||
                (host.getHostIP().equals("127.0.0.1") && (localIP.equals(phost.getHostIP()))) ||
                (host.getHostIP().equals("localhost") && (localIP.equals(phost.getHostIP())))){
                    listHost.set(i, phost);
                    okset=true;
                    System.out.println(">>> UPDATE PRINTER HOST : "+ phost.getHostName()+ " " +
                    phost.getHostIP());
                    break;
                }
            }
            if(okset==false){
                listHost.add(phost);
                System.out.println(">>> ADD PRINTER HOST : "+ phost.getHostName()+ " " +
                phost.getHostIP());
            }
            
        }else {
            listHost.add(phost);
            System.out.println(">>> ADD PRINTER HOST : "+ phost.getHostName()+ " " +
            phost.getHostIP());
        }
        
    }
    
    
    public static Vector getPrinterHostExceptIP(String ip){
        if(ip==null)
            return new Vector();;
            
            if(listHost==null)
                listHost= new Vector();
            
            if(listHost.size()<0){
                try{
                    loadHosts();
                } catch(Exception exc){
                    System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
                }
            }
            
            String localIP = getLocahostlIP();
            
            
            Vector hosts = new Vector();
            
            if(listHost!=null && listHost.size()>0){
                for(int i = 0; i<listHost.size(); i++){
                    PrinterHost host = (PrinterHost)listHost.get(i);
                    if( ( !host.getHostIP().equals(ip)) &&
                    !(host.getHostIP().equals("127.0.0.1") && (localIP.equals(ip))) ||
                    !(host.getHostIP().equals("localhost") && (localIP.equals(ip)))) {
                        hosts.add(host);
                    }
                    
                }
            }
            return hosts;
    }
    
    
    public static PrinterHost getPrinterHostByName(String name){
        if (name==null)
            return null;
        
        if(listHost==null)
            listHost = new Vector();
        
        if(listHost.size()<0){
            try{
                loadHosts();
            } catch(Exception exc){
                System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
            }
        }
        if(listHost!=null && listHost.size()>0){
            for(int i = 0; i<listHost.size(); i++){
                PrinterHost host = (PrinterHost)listHost.get(i);
                if(host.getHostName().equals(name))
                    return host;
            }
        }
        return null;
    }
    
    
    public static String getPathConfigFile(){
        return pathConfigFile;
    }
    
    public static void setPathConfigFile(String pathConfigFl){
        pathConfigFile = pathConfigFl;
    }
    
    public static Vector getListHost(){
        if(listHost==null)
            listHost= new Vector();
        if(listHost.size()<0){
            try{
                loadHosts();
            } catch(Exception exc){
                System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
            }
        }
        return listHost;
    }
    
    public static Vector getListHost(boolean reload){
        if(listHost==null)
            listHost= new Vector();
        
        if((listHost.size()<0)  || reload){
            try{
                loadHosts();
            } catch(Exception exc){
                System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
            }
        }
        
        return listHost;
    }
    
    private static String pathConfigFile= System.getProperty("java.home") + System.getProperty("file.separator") + "dimata" +
    System.getProperty("file.separator") + "printerhosts.xml";
    private static Vector listHost = new Vector();
    
    static {
        try{
            if(listHost==null)
                listHost= new Vector();
            
            if(listHost.size()<0){
                
                loadHosts();
            }
        } catch(Exception exc){
            System.out.println("EXC : getPrinterHost-> loadHosts\n " + exc);
        }
    }
    
}
