/**

 *  Read and write data into configuration file (xml file)

 *  edited by : gedhy

 *  This class need jdom.jar

 */



package com.dimata.qdep.db;



import java.io.*;

import java.util.*;

import org.jdom.input.SAXBuilder;

import org.jdom.Document;

import org.jdom.Element;

import org.jdom.Attribute;

import org.jdom.JDOMException;



/**

 *  Read and write data into configuration file (xml file)

 *  This class need jdom.jar

 */

public class DBConfigReader {



    /**

     * Hashtable that handle result of parsing

     */

    private Hashtable configStorage = new Hashtable();



    /**

     * InputStream that handle convert result of fileString

     */    

    private InputStream inStream;    

    

    /**

     * constant of element

     */

    public static final int APP_CONFIG   = 0;

    

    public static final int DATABASE     = 0;

        public static final int DB_DRIVER    = 0;

        public static final int DB_URL       = 1;

        public static final int DB_USER      = 2;

        public static final int DB_PASSWD    = 3;

        public static final int DB_MIN_CONN  = 4;

        public static final int DB_MAX_CONN  = 5;

    

    public static final int LOGS         = 1;

        public static final int LOG_CONN     = 0;

        public static final int LOG_APP      = 1;

        public static final int LOG_SIZE     = 2;

    

    public static final int FORMAT           = 2;

        public static final int FOR_DATE     = 0;

        public static final int FOR_DEC      = 1;

        public static final int FOR_CURR     = 2;    

        

    public static final int COMPANY         = 3;

        public static final int HEAD        = 0;

        public static final int ADDRESS     = 1;

        public static final int PHONE       = 2;

        public static final int FAX         = 3;

        public static final int HOMEPAGE    = 4;

        public static final int EMAIL       = 5;

    

    public static String xmlRoot[] = {

        "appconfig"

    };    

    

    public static String xmlSubRoot[] = {

        "database","logs","format","company"

    };    

 

    public static String xmlPcDataDb[] = {

        "dbdriver","dburl","dbuser","dbpasswd","dbminconn","dbmaxconn"

    };    



    public static String xmlPcDataLogs[] = {

        "logconn","logapp","logsize"

    };    

    

    public static String xmlPcDataFormat[] = {

        "fordate","fordecimal","forcurrency"

    };                       

    

    public static String xmlPcDataCompany[] = {

        "head","address","phone","fax","homepage","email"

    };

    

    

    /**

     * edited by gedhy

     */

    public DBConfigReader(String configFileName) throws Exception { 

        String strFile = getFileString(configFileName);

        this.inStream = new StringBufferInputStream(strFile);

        composeConfigValue();

    }



    public Hashtable getConfigValues() {

        if(configStorage.size() > 0) {

            return configStorage;

        }

        return null;

    }



    public String getConfigValue(String key) {

        if(configStorage.size() > 0) {

            return (String)configStorage.get(key);

        }

        return "";

    }    



    

    /**

     * old

     */

    private void composeConfigValue() throws IOException {

        try {            

            SAXBuilder builder = new SAXBuilder();            

            Document doc = builder.build(inStream);

            

            Element root = doc.getRootElement();

            if(root==null) throw new IOException("!!! exception : no root element available");            



            for(int i=0; i<xmlSubRoot.length; i++){                                

                Element subrootElement = root.getChild(xmlSubRoot[i]);                

                switch(i){

                    case DATABASE : 

                         for(int j=0; j<xmlPcDataDb.length; j++){                            

                             Element pcDataElement = subrootElement.getChild(xmlPcDataDb[j]);

                             this.configStorage.put(String.valueOf(xmlPcDataDb[j]), pcDataElement.getText());                             

                         }    

                         break;

                         

                    case LOGS     :

                         for(int j=0; j<xmlPcDataLogs.length; j++){                            

                             Element pcDataElement = subrootElement.getChild(xmlPcDataLogs[j]);

                             this.configStorage.put(xmlPcDataLogs[j], pcDataElement.getText());                             

                         }    

                         break;

                        

                    case FORMAT   :

                         for(int j=0; j<xmlPcDataFormat.length; j++){                            

                             Element pcDataElement = subrootElement.getChild(xmlPcDataFormat[j]);

                             this.configStorage.put(xmlPcDataFormat[j], pcDataElement.getText());                             

                         }    

                         break;                           

                         

                    case COMPANY  :

                        for(int j=0; j<xmlPcDataCompany.length; j++){                            

                            // Element pcDataElement = subrootElement.getChild(xmlPcDataCompany[j]);

                             //this.configStorage.put(xmlPcDataCompany[j], pcDataElement.getText());                             

                         }    

                         break;   

                }                                                              

            }                                 



        }catch(JDOMException exc){

            throw new IOException(exc.getMessage());

        }

    }      





    /**

     * used to get string content of a file

     */

    public String getFileString(String file){

        FileInputStream fIn = null;

        String stAll="";

        try {

            fIn = new FileInputStream(file);

            int maxbuf = 1024;

            int rbyte = maxbuf;

            

            for (int ib = 0; (ib < Integer.MAX_VALUE) && (rbyte == maxbuf); ib = ib + maxbuf) {

                byte buffer[] = new byte[maxbuf];

                rbyte = fIn.read(buffer);

                if (rbyte > 0){

                    String st = new String(buffer,0, rbyte);

                    stAll = stAll + st;

                }

            }

            

        }

        catch (Exception e)  {

            System.out.println("Exception getFileString : "+e.toString());

        }

        

        return stAll;

    }

    

    

    /**

     * testing method     

     */

    public static void main(String args[]){

        //String file = "C:\\jdk1.3.1_08\\jre\\dimata\\ziprealty.xml";

        String file = DBHandler.CONFIG_FILE;

        try{

            DBConfigReader db = new DBConfigReader(file);

            

            for(int i=0; i<xmlSubRoot.length; i++){                                                

                switch(i){

                    case DATABASE : 

                         for(int j=0; j<xmlPcDataDb.length; j++){    

                             String key = xmlPcDataDb[j];

                             System.out.println("Result : "+db.getConfigValue(key));                                                      }    

                         break;

                         

                    case LOGS     :

                         for(int j=0; j<xmlPcDataLogs.length; j++){                            

                             String key = xmlPcDataLogs[j];

                             System.out.println("Result : "+db.getConfigValue(key));                                                      }    

                         break;

                        

                    case FORMAT   :

                         for(int j=0; j<xmlPcDataFormat.length; j++){                            

                             String key = xmlPcDataFormat[j];

                             System.out.println("Result : "+db.getConfigValue(key));                             

                         }    

                         break;                        

                    case COMPANY   :

                         for(int j=0; j<xmlPcDataCompany.length; j++){                            

                             String key = xmlPcDataCompany[j];

                             System.out.println("Result : "+db.getConfigValue(key));                             

                         }    

                         break;

                }                                                              

            }                                             

        }catch(Exception e){

            System.out.println("Exc : "+e.toString());

        }            

    }

    

} 







