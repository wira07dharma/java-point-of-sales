/*
 * SessSystemProperty.java
 *
 * Created on April 30, 2002, 10:11 AM
 */

package com.dimata.system.session;
import com.dimata.system.entity.*;

public class SessSystemProperty {


    public static boolean loaded  = false;
    public static String[] groups = {"APPLICATION SETUP"};    
    public static String[][] subGroups = {     
        {"Application","Cashier","Outlet"}    
    };

    
    /**
    *  loadable properties are loaded here
    */
    public static String MATERIAL_PERIOD       = "0";
    public static String DATABASE_HOME         = "1";    
    public static String MASTERIN_HOME         = "2";    
    public static String MASTEROUT_HOME        = "3";    
    public static String TRANSOUT_HOME         = "4";    
    public static String TRANSIN_HOME          = "5";    

    /**
    * creates new SessSystemProperty
    */
    public SessSystemProperty() {
        if(!loaded) {            
            boolean ok = loadFromDB();
            String okStr = "OK";
            if(!ok) okStr = "FAILED";
            loaded = true;
        }
    }
    
    public boolean loadFromDB() {
        try {
            MATERIAL_PERIOD  = PstSystemProperty.getValueByName("MATERIAL_PERIOD");
            DATABASE_HOME  = PstSystemProperty.getValueByName("DATABASE_HOME");
            MASTEROUT_HOME  = PstSystemProperty.getValueByName("MASTEROUT_HOME");
            MASTERIN_HOME  = PstSystemProperty.getValueByName("MASTERIN_HOME");
            TRANSOUT_HOME  = PstSystemProperty.getValueByName("TRANSOUT_HOME");
            TRANSIN_HOME  = PstSystemProperty.getValueByName("TRANSIN_HOME");
            return true;
        }catch(Exception e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        SessSystemProperty prop = new SessSystemProperty();
    }
}
