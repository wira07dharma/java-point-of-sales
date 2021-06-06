/*
 * SessUserSession.java
 *
 * Created on April 11, 2002, 6:54 AM
 */

package com.dimata.aiso.session.admin;

import com.dimata.aiso.entity.admin.*;
import java.util.*;


//import com.dimata.aiso.db.DBHandler;
//import com.dimata.aiso.db.DBException;


/**
 *
 * @author  ktanjana
 * @version 
 * @objective represent access session of a user loggin into he system
 */
public class SessUserSession {
    public final static String HTTP_SESSION_NAME = "USER_SESSION";
    public final static int DO_LOGIN_OK = 0;
    public final static int DO_LOGIN_ALREADY_LOGIN =1;
    public final static int DO_LOGIN_NOT_VALID =2;
    public final static int DO_LOGIN_SYSTEM_FAIL =3;
    public final static int DO_LOGIN_GET_PRIV_ERROR =4;
    public final static int DO_LOGIN_NO_PRIV_ASIGNED =5;
    
    public final static String[] soLoginTxt={"Login succeed","User is already logged in",
    "Login ID or Password are invalid","System cannot login you", "Can't get privilege",
    "No access asigned, please contact your system administrator"};
    
    private Vector  privObj= new Vector();
    private AppUser appUser = new AppUser();
    
    /** Creates new SessUserSession */
    public SessUserSession() {
        appUser.setUserStatus(AppUser.STATUS_LOGOUT);
    }

    public SessUserSession(String hostIP) {
        appUser.setUserStatus(AppUser.STATUS_LOGOUT);
        appUser.setLastLoginIp(hostIP);
    }

    public String getUserName(){
        return appUser.getFullName();
    }

    public long getUserOID(){
        return appUser.getOID();
    }
        
    public boolean isLoggedIn(){        
        if( /*(this.appUser!=null) && (this.appUser.getOID()!=0) && */
             (this.appUser.getUserStatus()==AppUser.STATUS_LOGIN))
            return true;
        else 
            return false;
    }
    
    
   public boolean checkPrivilege(int objCode){
       if(!isLoggedIn())
           return false;
       return SessAppPrivilegeObj.existCode(this.privObj, objCode);
   }
    
    public int doLogin(String loginID, String password)
    {                
        AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, password);
        appUser = user;

        if(user==null)
        {
            return DO_LOGIN_SYSTEM_FAIL;
        }
        
        if(user.getOID()==0)
        {
            return DO_LOGIN_NOT_VALID;
        }
        
        user.setLastLoginIp(this.appUser.getLastLoginIp());
        user.setUserStatus(AppUser.STATUS_LOGIN);
        user.setLastLoginDate(new Date());
        
//        if(PstAppUser.updateUserByLoggedIn(user)==0)  // ---> ini proses yang update data app_user
//        {            
//            return DO_LOGIN_SYSTEM_FAIL;
//        }
        
        this.appUser = user;        
        
        privObj = PstAppUser.getUserPrivObj(this.appUser.getOID());

        if(privObj==null)
        {
            privObj= new Vector(1,1);
            return DO_LOGIN_GET_PRIV_ERROR;
        }
        return DO_LOGIN_OK;
    }

    public void doLogout(){
        if( (appUser!=null) || (appUser.getUserStatus() != AppUser.STATUS_LOGIN))
            return;
        
        PstAppUser.updateUserStatus(appUser.getOID(), AppUser.STATUS_LOGOUT);
            
    }
    
    public AppUser getAppUser (){
    	return this.appUser;
    }

    public void printAppUser(){
        //System.out.println("AppUser : ");
        //System.out.println("user ID = "+ this.appUser.getOID());
        //System.out.println("login ID = "+ this.appUser.getLoginId());
       // System.out.println("status = "+ this.appUser.getUserStatus());
    }
}
