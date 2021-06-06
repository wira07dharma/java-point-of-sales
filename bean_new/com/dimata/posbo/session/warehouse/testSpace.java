/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.warehouse;

/**
 *
 * @author gadnyana
 */
public class testSpace {
    
    public static void main(String[] args){
        String str = "26 POT   10:38 001        00:00:00 200        238222                 0";
        str = str.replaceAll(" ", "");
        //for(int i=1;i<str.length();i++){
        //    String charx = str.substring(i-1 ,i);
            
        //    System.out.println("char >> : "+charx);
        //}
        System.out.println("str >> : "+str);
    }

}
