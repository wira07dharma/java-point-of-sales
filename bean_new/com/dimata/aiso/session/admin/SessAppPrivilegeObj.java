/*
 * SessAppPrivilegeObj.java
 *
 * Created on April 11, 2002, 1:15 PM
 */

package com.dimata.aiso.session.admin;

import java.util.*;

/**
 *
 * @author  ktanjana
 * @version 
 */
public class SessAppPrivilegeObj {

    /** Creates new SessAppPrivilegeObj */
    public SessAppPrivilegeObj() {
    }

    public static boolean existCode(Vector codes, int code){
//        System.out.println("codes..= "+codes.size());
        if((codes==null) || (codes.size()<1))
            return false;

        for(int i=0; i<codes.size();i++){
            if(code== ( (Integer) codes.get(i)).intValue() )
                return true;
        }
        
        return false;
        
    }    
}
