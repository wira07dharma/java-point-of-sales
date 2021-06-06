/*
 * I_DocStatus.java
 *
 * Created on November 12, 2007, 2:49 PM
 */

package com.dimata.interfaces.docstatus;

/**
 *
 * @author  dwi
 */
public interface I_DocStatus {
    
    public static final int STS_DRAFT = 0;
    public static final int STS_FINAL = 1;
    public static final int STS_POSTED = 2;
    
    public static String[] arrStatusNames = {
        "DRAFT","FINAL","POSTED"
    };
}