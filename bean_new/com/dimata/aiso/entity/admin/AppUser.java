/*
 * AppUser.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.aiso.entity.admin;

/**
 *
 * @author  ktanjana
 * @version 
 */


import java.util.*;
import com.dimata.qdep.entity.*;

public class AppUser extends Entity {
    public final static int STATUS_NEW =0;
    public final static int STATUS_LOGOUT =1;
    public final static int STATUS_LOGIN =2;
    
    public final static String[] statusTxt= {"New", "Logged out", "Logged In"};
    
    public static final int USER_GROUP_ADMIN = 0;
    public static final int USER_GROUP_HO_ACC = 1;
    public static final int USER_GROUP_BRC_ACC = 2;
    public static final int USER_GROUP_HO_STAFF = 3;
    public static final int USER_GROUP_BRC_STAFF = 4;
    public static final int USER_GROUP_HO_CASHIER = 5;
    public static final int USER_GROUP_BRC_CASHIER = 6;
    public static final int USER_GROUP_REPORTS_ONLY =7;
    
    public static final String[] strGroupUser = {
        "ADMIN","HO ACCOUNTING","BRANCH ACCOUNTING","HO STAFF",
        "BRANCH STAFF","HO CASHIER","BRANCH CASHIER", "REPORTS_ONLY"
    };
    
    private String loginId="";
    
    private String password="";
    
    private String fullName="";
    
    private String email="";
    
    private String description="";
    
    private java.util.Date regDate=new Date();
    
    private java.util.Date updateDate=new Date();
    
    private int userStatus=0;
    
    private java.util.Date lastLoginDate=new Date();
    
    private String lastLoginIp="0";
    
    /**
     * Holds value of property groupUser.
     */
    private int groupUser = 0;
    
    public AppUser() {
    }
        
    public String getLoginId() {
        return loginId;
    }
    
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public java.util.Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(java.util.Date regDate) {
        this.regDate = regDate;
    }
    
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public int getUserStatus() {
        return userStatus;
    }

    public static String getStatusTxt(int sts){
        if((sts<0) || (sts> statusTxt.length))
            return "";
        return statusTxt[sts];
    }
    
    public static Vector getStatusTxts(){
        Vector vct = new Vector(1,1);
        for(int i =0 ; i < statusTxt.length ; i++){
            vct.add(statusTxt[i]);
        }
        return vct;
    }
    
    public static Vector getStatusVals(){
        Vector vct = new Vector(1,1);
        for(int i =0 ; i < statusTxt.length ; i++){
            vct.add(Integer.toString(i));
        }
        return vct;
    }
    
     public static String getUserGroupTxt(int group){
        if((group < 0) || (group > strGroupUser.length))
            return "";
        return strGroupUser[group];
    }
    
    public static Vector getUserGroupTxts(){
        Vector vct = new Vector(1,1);
        for(int i =0 ; i < strGroupUser.length ; i++){
            vct.add(strGroupUser[i]);
        }
        return vct;
    }
    
      public static Vector getUserGroupVals(){
        Vector vct = new Vector(1,1);
        for(int i =0 ; i < strGroupUser.length ; i++){
            vct.add(Integer.toString(i));
        }
        return vct;
    }
      
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    
    public java.util.Date getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(java.util.Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
    
    /**
     * Getter for property groupUser.
     * @return Value of property groupUser.
     */
    public int getGroupUser() {
        return this.groupUser;
    }
    
    /**
     * Setter for property groupUser.
     * @param groupUser New value of property groupUser.
     */
    public void setGroupUser(int groupUser) {
        this.groupUser = groupUser;
    }
        
}

