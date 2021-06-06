/*
 * FrmAppUser.java
 *
 * Created on April 3, 2002, 10:38 AM
 */

package com.dimata.posbo.form.admin;

import java.util.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.admin.*;
import javax.servlet.http.*;

/**
 *
 * @author  ktanjana
 * @version 
 */
public class FrmAppUser extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_APP_USER = "APP_USER";
    
    public static final int FRM_LOGIN_ID		= 0;
    public static final int FRM_PASSWORD		= 1;
    public static final int FRM_CFRM_PASSWORD		= 2;
    public static final int FRM_FULL_NAME		= 3;
    public static final int FRM_EMAIL			= 4;
    public static final int FRM_DESCRIPTION		= 5;
    public static final int FRM_REG_DATE		= 6;
    public static final int FRM_UPDATE_DATE		= 7;
    public static final int FRM_USER_STATUS		= 8;
    public static final int FRM_LAST_LOGIN_DATE         = 9;
    public static final int FRM_LAST_LOGIN_IP		= 10;
    public static final int FRM_USER_GROUP		= 11;
    public static final int FRM_EMPLOYEE_ID		= 12;
    public static final int FRM_USER_GROUP_NEW		= 13;
    //update opie-eyek 20130903
    public static final int FRM_FIELD_START_TIME = 14;
    public static final int FRM_FIELD_END_TIME = 15;
	public static final int FRM_FIELD_COMPANY_ID = 16;

    public static  final String[] fieldNames = {
        "LOGIN_ID", "PASSWORD", "CFRM_PASSWORD","FULL_NAME", "EMAIL", "DESCRIPTION"
        ,"REG_DATE", "UPDATE_DATE", "USER_STATUS", "LAST_LOGIN_DATE", "LAST_LOGIN_IP"
        , "USER_GROUP","EMPLOYEE_ID","USER_GROUP_NEW","FRM_FIELD_START_TIME","FRM_FIELD_END_TIME", "COMPANY_ID"
    } ;

    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED , TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + ENTRY_REQUIRED,  
        TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + FORMAT_EMAIL,  TYPE_STRING,
        TYPE_DATE, TYPE_DATE, TYPE_INT, TYPE_DATE, TYPE_STRING,  TYPE_COLLECTION,TYPE_LONG,
        TYPE_INT,TYPE_DATE,TYPE_DATE, TYPE_LONG
    };


    public static final int DAY_SENIN =0;
    public static final int DAY_SELASA =1;
    public static final int DAY_RABU =2;
    public static final int DAY_KAMIS =3;
    public static final int DAY_JUMAT =4;
    public static final int DAY_SABTU =5;
    public static final int DAY_MINGGU =6;

    public static final String [] searchKey = {

        "Monday", "Tuesday", "Wednesday", "Thursday","Friday", "Saturday", "Sunday"

    };

    public static final String[] searchValue = {

        "0","1","2","3","4","5","6"

    };

    public static String getSerchValue(String nameDays){
        String valueDay="-1";
        if(searchValue!=null && searchValue.length>0){
           for(int x=0;x<searchKey.length;x++){
               if(searchKey[x].equals(nameDays)){
                  valueDay = searchValue[x];
               }
           }
        }
        return  valueDay;
    }
     public static Vector searchKey()

    {

    	Vector result = new Vector(1,1);

        for(int r=0;r < searchKey.length;r++){

        	result.add(searchKey[r]);

        }

        return result;

    }



    public static Vector searchValue()

    {

    	Vector result = new Vector(1,1);

        for(int r=0;r < searchValue.length;r++){

        	result.add(searchValue[r]);

        }

        return result;

    }


    private AppUser appUser = new AppUser();
    
    
    
    /** Creates new FrmAppUser */
    public FrmAppUser() {
    }

    public FrmAppUser(HttpServletRequest request) {
        super(new FrmAppUser(), request);
    }
    
    public String getFormName() {
        return FRM_APP_USER;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    
    public AppUser getEntityObject()
    {
        return appUser;
    }
    
        
    public void requestEntityObject(AppUser entObj)
    {        
        try {
            this.requestParam();                    
            entObj.setLoginId(this.getString(FRM_LOGIN_ID));
            entObj.setPassword(this.getString(FRM_PASSWORD));
            entObj.setFullName(this.getString(FRM_FULL_NAME));
            entObj.setEmail(this.getString(FRM_EMAIL));
            entObj.setDescription(this.getString(FRM_DESCRIPTION));
            entObj.setRegDate(this.getDate(FRM_REG_DATE));
            entObj.setUpdateDate(this.getDate(FRM_UPDATE_DATE));
            entObj.setUserStatus(this.getInt(FRM_USER_STATUS));
            entObj.setLastLoginDate(this.getDate(FRM_LAST_LOGIN_DATE));
            entObj.setLastLoginIp(this.getString(FRM_LAST_LOGIN_IP));
            entObj.setEmployeeId(this.getLong(FRM_EMPLOYEE_ID));
            entObj.setUserGroupNew(this.getInt(FRM_USER_GROUP_NEW));
            //update opie-eyek 20130903
            entObj.setStartTime(getDate(FRM_FIELD_START_TIME));
            entObj.setEndTime(getDate(FRM_FIELD_END_TIME));
			entObj.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
            this.appUser = entObj;
        }catch(Exception e) {
            System.out.println("EXC... "+e);
            entObj = new AppUser();
        }       
    }
    /**
     * has to be call after requestEntityObject
     * return Vector of UserGroup objects
     **/ 
    public Vector getUserGroup(long userOID){
        Vector userGroups = new Vector(1,1);
        
        Vector groupOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP]);        
        
        if (groupOIDs==null)
            return userGroups;
        int max = groupOIDs.size();
        
        for(int i=0; i< max; i++){
            long groupOID = ( (Long)groupOIDs.get(i)).longValue();
            UserGroup ug = new UserGroup();
            ug.setUserID(userOID);
            ug.setGroupID(groupOID);
            userGroups.add(ug);
        }
        return userGroups;
    }

    /**add opie-eyek 19022013
     * untuk mencari assign lokasi user
     * maksudnya, user ini bisa approve document menjadi final
     *
     * @param userOID
     * @return
     */
    public Vector getUserAssignLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_DC");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }

    /*
     * add opie-eyek 20130819
     */
    public Vector getUserAssignLocationTransfer() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_TF");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }


     /*
     * add opie-eyek 20140110
     */
    public Vector getUserAssignViewSaleStockReportLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_SR");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }


     /*
     * add opie-eyek 20140212
     */
    public Vector getUserAssignCreateDocumentLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_DT");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }

     public Vector getUserAssignDay() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_DY");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }
    public Vector getUserAssignManagingProduction() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_CV");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }
    public Vector getUserAssignSalesLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_SA");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }
    public Vector getUserAssignDeliveryLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_DA");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }
    public Vector getUserAssignMainLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_AS");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }

    
}

