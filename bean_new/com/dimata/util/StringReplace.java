/*
 * StringReplace.java
 *
 * Created on October 4, 2003, 10:20 AM
 */

package com.dimata.util;

/**
 *
 * @author  gedhy
 */
import java.util.StringTokenizer;

public class StringReplace {
    
    /**
     * method used to replace oldString with newString
     * @param <CODE>source</CODE>source string
     * @param <CODE>oldString</CODE>string will replaced
     * @param <CODE>newString</CODE>new string that will replace old one
     * @return replaced string
     */
    public static String replaceString(String source, String oldString, String newString){
           String result = ""; 
           String tmpResult = "";
           if(source!=null && source.length()>0){               
                StringTokenizer strToken = new StringTokenizer(source,oldString);                
                while(strToken.hasMoreTokens()){
                    result = result + String.valueOf(strToken.nextToken()) + newString;
                }                                
                if(result!=null && result.length()>0){
                    result = result.substring(0,result.length()-newString.length());
                }    
           }
           return result;
    }        
    
}
