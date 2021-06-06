/*
 * AppUser.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.posbo.entity.admin;

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
    public final static int STATUS_NON_AKTIF =3;
    
    public final static String[] statusTxt= {"New", "Logged out", "Logged In", "Non Aktif"};
    
    
    private int userGroupNew = 0;
   
    private String loginId="";
    private String password="";
    private String fullName="";
    private String email="";
    private String description="";
    private java.util.Date regDate=new Date();
    private java.util.Date updateDate=new Date();
    private int userStatus=0;
    private java.util.Date lastLoginDate=new Date();
    private String lastLoginIp="";
    private long employeeId;
    //update opie-eyek 20130903
    private Date startTime = new Date();
    private Date endTime = new Date();
    //update by koyo 20151028
    private String fingerData="";
    private long companyId;

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
        
    public long getEmployeeId(){ return employeeId; }        

    public void setEmployeeId(long employeeId){ this.employeeId = employeeId; }

    public int getUserGroupNew()
    { 
        return userGroupNew; 
    }        

    public void setUserGroupNew(int userGroupNew)
    { 
        this.userGroupNew = userGroupNew; 
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    
    public String getFingerData() {
        return fingerData;
    }

    public void setFingerData(String fingerData) {
        this.fingerData = fingerData;
    }

	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
    
}



