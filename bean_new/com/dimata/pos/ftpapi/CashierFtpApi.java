/*
 * CashierFtpApi.java
 *
 * Created on March 9, 2006, 4:29 PM
 */

package com.dimata.pos.ftpapi;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Date;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPConnectMode;

import com.dimata.pos.cashier.CashierMainApp;
import com.dimata.common.entity.logger.*;
import com.dimata.util.Formater;

import java.security.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.MalformedURLException;
import java.io.*;
/**
 *
 * @author  pulantara
 */
public class CashierFtpApi {
    
    private Hashtable params = new Hashtable();
   
    public String LocalOutDirURL="";
    public String LocalInDirURL="";
    
    public String RemoteHost ="";
    public int RemotePort =21;
    public String RemoteUser ="";
    public String RemotePassword ="";
    public String RemoteOutDirURL="";
    public String RemoteInDirURL="";
    public String RemotePathDelim="/";
    
    public String FTPMode      = "BINARY";
    public String FTPConnMode  = "PASV";
    
    private FTPClient ftp;
    
    /** Creates a new instance of CashierFtpApi */
    public CashierFtpApi() {
        
    }
    
    /** Creates a new instance of CashierFtpApi with parameter */
    public CashierFtpApi(Hashtable params) {
        this.params = params; 
        
        this.LocalInDirURL = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.LOCAL_IN_DIR_URL]);
        this.LocalOutDirURL = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.LOCAL_OUT_DIR_URL]);
        
        this.RemotePort = Integer.parseInt((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_PORT]));
        this.RemoteHost = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_HOST]);
        this.RemoteInDirURL = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_IN_DIR_URL]);
        this.RemoteOutDirURL = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_OUT_DIR_URL]);
        this.RemotePassword = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_PASSWORD]);
        this.RemoteUser = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_USER]);
        
        this.FTPConnMode = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.CONNECTION_MODE]);
        this.FTPMode = (String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.FTP_MODE]);
    }
    
    public boolean connectFTP(){
        boolean result = false;
        try{
            // connect and test supplying port no.
            ftp = new FTPClient(this.RemoteHost, this.RemotePort);
            ftp.login(this.RemoteUser, this.RemotePassword);
            ftp.quit();
            
            // connect again
            ftp = new FTPClient(this.RemoteHost);
            
            // switch on debug of responses
            ftp.debugResponses(true);
            
            ftp.login(this.RemoteUser, this.RemotePassword);
            
            // binary or ASCII transfer
            if (CashierFtpConstant.FTP_MODE_BINARY.equalsIgnoreCase(this.FTPMode)) {
                ftp.setType(FTPTransferType.BINARY);
            }
            else if (CashierFtpConstant.FTP_MODE_ASCII.equalsIgnoreCase(this.FTPMode)) {
                ftp.setType(FTPTransferType.ASCII);
            }
            else {
                System.out.println("Unknown transfer type: " + this.FTPMode+ " set binary");
                ftp.setType(FTPTransferType.BINARY);
            }
            
            // PASV or active?
            if (CashierFtpConstant.CONN_MODE_PASIVE.equalsIgnoreCase(this.FTPConnMode)) {
                ftp.setConnectMode(FTPConnectMode.PASV);
            }
            else if (CashierFtpConstant.CONN_MODE_ACTIVE.equalsIgnoreCase(this.FTPConnMode)) {
                ftp.setConnectMode(FTPConnectMode.ACTIVE);
            }
            else {
                System.out.println("Unknown connect mode: " + this.FTPConnMode+ "  set pasiv");
                ftp.setConnectMode(FTPConnectMode.PASV);
            }
            System.out.println(" >>> connectFTP OK");
            result = true;
        } catch (Exception exc){
            System.out.println(" >>> connectFTP "+ exc);
        }
        return result;
    }
    
    private  Vector getRemoteFileNameDir(){
        Vector fNames= new Vector();
        try{
            String[] listings = ftp.dir(".", false);
            for (int i = 0; i < listings.length; i++)
                fNames.add(listings[i]);
        }catch (Exception exc){
            System.out.println(" >> getRemoteFileNameDir  "+exc);
        }
        return fNames;
    }
    
    public boolean getAllFiles(){
        boolean result = false;
        try{
            ftp.chdir(this.RemoteOutDirURL);
            Vector fileList = this.getRemoteFileNameDir();
            for(int i = 0; i < fileList.size(); i++){
                String remoteFile = (String) fileList.get(i);
                this.getFile(remoteFile, this.LocalInDirURL+CashierFtpConstant.LOCAL_PATH_DELIM+remoteFile);
            }
            result = true;
        }
        catch(Exception e){
            System.out.println("err on getAllFiles() = "+e.toString());
        }
        writeLog(result?this.getRetriveSuccessLog():this.getRetriveFailedLog());
        return result;
    }
    
    public boolean putAllFiles(){
        boolean result = false;
        try{
            ftp.chdir(this.RemoteInDirURL);
            Vector fileList = this.getFileList(this.LocalOutDirURL);
            for(int i = 0; i < fileList.size(); i++){
                java.io.File file = (java.io.File) fileList.get(i);
                putFile(file.getAbsolutePath(), CashierMainApp.getDSJ_CashierConfig().master+"_"+file.getName()); 
            }
            result = true;
        }
        catch(Exception e){
            System.out.println("err on putAllFiles() = "+e.toString());
        }
        writeLog(result?this.getUploadSuccessLog():this.getUplodaFailedLog());
        return result;
    }
    
    public boolean getFile(String remoteFile, String localPath){
        boolean result = false;
        try{
            byte[] buf = ftp.get(remoteFile);            
            FileOutputStream file = new FileOutputStream(localPath);
            file.write(buf);
            result = true;
        }catch(Exception e){
            System.out.println("err on getFile() = "+e.toString());
        }
        return result;
    }
    
    private boolean putFile(String localPath, String remoteDir, String remoteFile){
        boolean result = false;
        try{
            ftp.chdir(remoteDir);
            ftp.put(localPath, remoteFile);
            result = true;
        }catch(Exception e){
            System.out.println("err on putFile() = "+e.toString());
        }
        return result;
    }
    
    private boolean putFile(String localPath, String remoteFile){
        boolean result = false;
        try{
            ftp.put(localPath, remoteFile);            
            result = true;
        }catch(Exception e){
            System.out.println("err on putFile() = "+e.toString());
        }
        return result;
    }
    
    private Vector getFileList( String dirURL){
        try{
            java.io.File fl = new java.io.File(dirURL); // set in directory
            
            java.io.File[] fileslist = fl.listFiles();
            
            if(fileslist != null) {
                Vector vctList = new Vector(1,1);
                for(int i = 0; i < fileslist.length; i++) {
                    java.io.File f = fileslist[i];
                    vctList.add(f);
                }
                return vctList;
            }
        } catch(Exception e){
            System.out.println("err on getFileList() = "+e.toString());
        }
        return null;
    }
    
    private void writeLog(String logs){
        try{
            Date now = new Date();
            DocLogger docLog = new DocLogger();
            String docNo = CashierFtpConstant.FTP_PARAM+" "+(1900+now.getYear())+(1+now.getMonth())+now.getDate();
            docLog.setDocNumber(docNo);
            docLog.setDescription(logs);
            docLog.setDocDate(now);
            PstDocLogger.insertExc(docLog);
        }
        catch(Exception e){
            System.out.println("err on writeLog() = "+e.toString());
        }
    }
    
    private String getRetriveSuccessLog(){
        String result = "";
        Date now = new Date();
        result = "Getting all data via FTP Success, "+Formater.formatDate(now,"dd MMM yyyy  kk:mm");
        return result;
    }
    
    private String getRetriveFailedLog(){
        String result = "";
        Date now = new Date();
        result = "Getting all data via FTP Failed, "+Formater.formatDate(now,"dd MMM yyyy  kk:mm");
        return result;
    }
    
    private String getUploadSuccessLog(){
        String result = "";
        Date now = new Date();
        result = "Uploading all data via FTP Success, "+Formater.formatDate(now,"dd MMM yyyy  kk:mm");
        return result;
    }
    
    private String getUplodaFailedLog(){
        String result = "";
        Date now = new Date();
        result = "Uploading all data via FTP Failed, "+Formater.formatDate(now,"dd MMM yyyy  kk:mm");
        return result;
    }
    
    /**
     * Getter for property param.
     * @return Value of property param.
     */
    public java.util.Hashtable getParam() {
        return params;
    }
    
    /**
     * Setter for property param.
     * @param param New value of property param.
     */
    public void setParam(java.util.Hashtable params) {
        this.params = params;
    }
    
}
