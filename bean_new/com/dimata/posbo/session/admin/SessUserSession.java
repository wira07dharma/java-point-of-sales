/*
 * SessUserSession.java
 *
 * Created on April 11, 2002, 6:54 AM
 */

package com.dimata.posbo.session.admin;

import com.dimata.posbo.entity.admin.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.posbo.entity.masterdata.*;
import java.util.*;

/**
 *
 * @author  ktanjana
 * @version
 * @objective represent access session of a user loggin into he system
 */
public class SessUserSession  {
    public final static String HTTP_SESSION_NAME = "USER_SESSION";
    public final static String HTTP_SESSION_SALES = "SALES_SESSION";
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
    private Sales sales = new Sales();
    private Employee employee = new Employee();
    private Department department = new Department();
    private Section section = new Section();
    private Position position = new Position();

    /** Creates new SessUserSession */
    public SessUserSession() {
        appUser.setUserStatus(AppUser.STATUS_LOGOUT);
    }

    public SessUserSession(String hostIP) {
        appUser.setUserStatus(AppUser.STATUS_LOGOUT);
        appUser.setLastLoginIp(hostIP);
    }

    public AppUser getAppUser(){
    	return this.appUser;
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


    public int doLogin(String loginID, String password){
        AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, password);

        if(user==null)
            return DO_LOGIN_SYSTEM_FAIL;

        if(user.getOID()==0)
            return DO_LOGIN_NOT_VALID;

        user.setLastLoginIp(this.appUser.getLastLoginIp());
        user.setUserStatus(AppUser.STATUS_LOGIN);
        user.setLastLoginDate(new Date());

        /*if(PstAppUser.updateByLoggedIn(user)==0)
            return DO_LOGIN_SYSTEM_FAIL;*/

        this.appUser = user;

        privObj = PstAppUser.getUserPrivObj(this.appUser.getOID());

        if(privObj==null){
            privObj= new Vector(1,1);
            return DO_LOGIN_GET_PRIV_ERROR;
        }

        System.out.println(" User login OK status ->" + appUser.getUserStatus());
        return DO_LOGIN_OK;
    }

    /**
     * Ari_wiweka 20130827
     * untuk login sales
     */
    public int doLoginSales(String loginID, String password){
        Sales salesUser = PstSales.getByLoginIDAndPassword(loginID, password);

        if(salesUser==null)
            return DO_LOGIN_SYSTEM_FAIL;

        if(salesUser.getOID()==0)
            return DO_LOGIN_NOT_VALID;

       // this.sales = salesUser;

       // privObj = PstSales.getObjectSales(this.sales.getCode());

       /* if(privObj==null){
            privObj= new Vector(1,1);
            return DO_LOGIN_GET_PRIV_ERROR;
        }*/

       // System.out.println(" User login OK status ->" + sales.getUserStatus());
        return DO_LOGIN_OK;
    }

    /**
     * doApprovalSupervisor
     * add Ari Wiweka 11/06/2013
     */
    public int doApproval(String loginID, String password){
        AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, password);

        if(user==null)
            return DO_LOGIN_SYSTEM_FAIL;

        if(user.getOID()==0)
            return DO_LOGIN_NOT_VALID;

        if(user.getUserGroupNew()==4)
            return DO_LOGIN_OK;

        return DO_LOGIN_NOT_VALID;
    }

    public void doLogout(){
        if((this.appUser!=null) && (this.appUser.getUserStatus() == AppUser.STATUS_LOGIN)){
        	PstAppUser.updateUserStatus(appUser.getOID(), AppUser.STATUS_LOGOUT);
        }
    }


    public void printAppUser(){
        System.out.println(" ==== AppUser ====");
        System.out.println(" user ID = "+ this.appUser.getOID());
        System.out.println(" login ID = "+ this.appUser.getLoginId());
        System.out.println(" employee = "+ this.appUser.getEmployeeId());
        System.out.println(" status = "+ this.appUser.getUserStatus());
    }


    public Employee getEmployee(){ return employee; }

    public void setEmployee(){
		try{
			if(this.appUser.getEmployeeId()!=0){
				this.employee = PstEmployee.fetchExc(this.appUser.getEmployeeId());
			}
		}catch(Exception e){
            System.out.println("err setDepartment : "+e.toString());
        }
    }

    public Department getDepartment(){ return department; }

    public void setDepartment(){
		try{
			if(this.employee.getOID()!=0){
				this.department = PstDepartment.fetchExc(this.employee.getDepartmentId());
			}
        }catch(Exception e){
            System.out.println("err setDepartment : "+e.toString());
        }
    }

    public Section getSection(){ return section; }

    public void setSection(){
		try{
			if(this.employee.getOID()!=0){
				this.section = PstSection.fetchExc(this.employee.getSectionId());
			}
        }catch(Exception e){
            System.out.println("err setSection : "+e.toString());
        }
    }

    public Position getPosition(){ return position; }

    public void setPosition(){
		try{
			if(this.employee.getOID()!=0){
				this.position = PstPosition.fetchExc(this.employee.getPositionId());
			}
        }catch(Exception e){
            System.out.println("err setPosition : "+e.toString());
        }
    }

    public void setUserHrdData(boolean usingSection){
        try{
		   this.setEmployee();
	       this.setDepartment();
           if(usingSection){
	       this.setSection();
           }
	       this.setPosition();
        }catch(Exception e){
            System.out.println("err setUserHrdData : "+e.toString());
        }
    }

}

