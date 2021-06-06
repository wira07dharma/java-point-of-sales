/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata;

/**
 *
 * @author ktanjana
 */
public class RootClass {

    public static String getThisClassPath(){
        try{
        com.dimata.RootClass rc = new com.dimata.RootClass();
        String loadLoc = rc.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
        String locString ="/";
        int idx = loadLoc.lastIndexOf("/");
        if(idx<0){
              idx = loadLoc.lastIndexOf("\\");
              locString ="\\";
            }
        loadLoc= loadLoc.substring(1, idx);
        String sysSpt= System.getProperty("file.separator");
        String locXml = loadLoc.replace(locString, sysSpt );
        if(!locXml.substring(0,1).equals(sysSpt)){
            locXml=sysSpt+locXml;
        }
        return locXml;
        } catch(Exception exc){
            System.out.println(exc);
            return "";
        }
    }

}
