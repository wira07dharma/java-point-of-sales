

package com.dimata.printman;
/*
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 *
 *  * List of Remote Printers will be stored into XML FILE :
 *  - Host Name, IP, Port, RMI object name, Shared status ( 0= not shared, 1=shared)
 * e.g. 
 * <?xml version="1.0"?>
   <printers>
	<printer>
		<idx>1</idx>
		<name>LX300</name>
		<port>LPT1</port>
		<driver>com.dimata.printman.DSJ_PrnDrv_LX300</driver>	
                <shared>1</shared>
    	</printer>
 	<printer>
		<idx>2</idx>
		<name>Lq100</name>
		<port>LPT1</port>
		<driver>com.dimata.printman.DSJ_PrnDrv_LQ100</driver>	
                <shared>0</shared>
 	</printer>
  </printers>
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

public class DSJ_PrinterXML {
    
    public DSJ_PrinterXML(){
    }
    
    public static void loadPrinters() throws IOException {
        System.out.println(">>> loadPrinters");
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
            List dbElement = root.getChildren("printer");
            Iterator iList = dbElement.iterator();
            PrnConfig prnConf= null;
            //if(listPrinter==null)
                listPrinter= new Vector();
            //else
            //    listPrinter.removeAllElements();
                
            while (iList.hasNext()) {               
                Element currElement = (Element)iList.next();
                prnConf = new PrnConfig();
                prnConf.setPrnIndex(Integer.parseInt(currElement.getChild("idx").getText()));
                prnConf.setPrnName(currElement.getChild("name").getText());
                prnConf.setPrnPort(currElement.getChild("port").getText());
                prnConf.setPrnDrvClassName(currElement.getChild("driver").getText());
                int shared = Integer.parseInt(currElement.getChild("shared").getText());
                prnConf.setShared((shared ==0 ? false : true));                
                
                listPrinter.add(prnConf);
                System.out.println("Load printer : "+prnConf.getPrnIndex()+ " "+prnConf.getPrnName()+" "+
                                    prnConf.getPrnPort()+" "+prnConf.getPrnDrvClassName()+" " +
                                    prnConf.getShared());
                
            }
            
            
        } catch (Exception exc) {
            throw new IOException(exc.getMessage());
        }
    }
    
    public static PrnConfig getPrnConfig(int index){
        if(listPrinter==null || listPrinter.size()<0){
            try{
                loadPrinters();
            } catch(Exception exc){
                System.out.println("EXC : getPrnConfig-> loadPrinters\n " + exc);
            }
        }
        if(listPrinter!=null && listPrinter.size()>0){
            for(int i = 0; i<listPrinter.size(); i++){
                PrnConfig prn = (PrnConfig)listPrinter.get(i);
                if(prn.getPrnIndex()==index)
                    return prn;
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
    
    public static Vector getListPrinter(){
        if((listPrinter==null) || listPrinter.size()<0){
            try{
                loadPrinters();
            } catch(Exception exc){
                System.out.println("EXC : getPrnConfig-> loadPrinters\n " + exc);
            }
        }
        
        return listPrinter;
    }
    
    private static String pathConfigFile= System.getProperty("java.home") + System.getProperty("file.separator") + "dimata" +
    System.getProperty("file.separator") + "printers.xml";
    private static Vector listPrinter = null;
    
    static {
        try{
            if((listPrinter==null) || (listPrinter.size()<0)){
                
                loadPrinters();
            }
        } catch(Exception exc){
            System.out.println("EXC : getPrnConfig-> loadPrinters\n " + exc);
        }
    }

}
