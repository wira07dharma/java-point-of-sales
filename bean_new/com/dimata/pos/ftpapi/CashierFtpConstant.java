/*
 * CashierFtpConstant.java
 *
 * Created on March 8, 2006, 4:20 PM
 */

package com.dimata.pos.ftpapi;

/**
 *
 * @author  pulantara
 */
public class CashierFtpConstant {
    
    public static final String FTP_PARAM = "FTP_PARAM";
    
    public static final String FTP_MODE_BINARY = "BINARY";
    public static final String FTP_MODE_ASCII = "ASCII";
    
    public static final String CONN_MODE_ACTIVE = "ACTIVE";
    public static final String CONN_MODE_PASIVE = "PASIVE";
    
    public static final String LOCAL_PATH_DELIM = System.getProperty("file.separator");
    
    public static final int LOCAL_IN_DIR_URL = 0;
    public static final int LOCAL_OUT_DIR_URL = 1;
    
    public static final int REMOTE_IN_DIR_URL = 2;
    public static final int REMOTE_OUT_DIR_URL = 3;
    
    public static final int REMOTE_HOST = 4;
    public static final int REMOTE_PORT = 5;
    public static final int REMOTE_USER = 6;
    public static final int REMOTE_PASSWORD = 7;
    public static final int FTP_MODE = 8;
    public static final int CONNECTION_MODE = 9;
    
    public static final int SERVICE_INTERVAL = 10;
    
    public static final String[] fieldNames = {
        "LOCAL_IN_DIR_URL",
        "LOCAL_OUT_DIR_URL",

        "REMOTE_IN_DIR_URL",
        "REMOTE_OUT_DIR_URL",

        "REMOTE_HOST",
        "REMOTE_PORT",
        "REMOTE_USER",
        "REMOTE_PASSWORD",
        "FTP_MODE",
        "CONNECTION_MODE",
        
        "SERVICE_INTERVAL"
    };
    
    /** Creates a new instance of CashierFtpConstant */
    public CashierFtpConstant() {
    }
    
}
