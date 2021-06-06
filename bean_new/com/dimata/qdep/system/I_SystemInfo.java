/*
 * I_SystemInfo.java
 *
 * Created on April 11, 2002, 7:19 PM
 */

package com.dimata.qdep.system;

/**
 *
 * @author  ktanjana
 * @version 
 */
public interface I_SystemInfo {
    int NO_INFO = 0;
    int DATA_LOCKED = 1;
    int HAVE_NOPRIV =2;
    int NOT_LOGIN =0;
    
    String textInfo[]={"","Data is locked","Forbidden .... You have no privilege to enter that page","You are not logged in"};

}

