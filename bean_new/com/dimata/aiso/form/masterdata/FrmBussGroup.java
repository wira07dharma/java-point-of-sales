/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.BussinessGroup;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmBussGroup extends FRMHandler implements I_FRMInterface, I_FRMType{

     public static final String FRM_BUSS_GROUP  = "FRM_BUSS_GROUP";
    public static final int FRM_FIELD_CODE		= 0;
    public static final int FRM_FIELD_NAME		= 1;
    public static final int FRM_FIELD_ADDRESS		= 2;
    public static final int FRM_FIELD_CITY		= 3;
    public static final int FRM_FIELD_PHONE		= 4;
    public static final int FRM_FIELD_FAX		= 5;

    public static String[] fieldNames = {
       "FRM_SEARCH_CODE",
       "FRM_SEARCH_NAME",
       "FRM_SEARCH_ADDRESS",
       "FRM_FIELD_CITY",
       "FRM_FIELD_PHONE",
       "FRM_FIELD_FAX"
    } ;

    public static int[] fieldTypes = {       
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING,
       TYPE_STRING,
       TYPE_STRING,
       TYPE_STRING
    };
   
    private BussinessGroup objBussinessGroup;

    public FrmBussGroup(){ 
    }
    
    public FrmBussGroup(BussinessGroup objBussinessGroup){
            this.objBussinessGroup = objBussinessGroup;
    }   
    
    public FrmBussGroup(HttpServletRequest request, BussinessGroup objBussinessGroup){
            super(new FrmBussGroup(objBussinessGroup), request);
            this.objBussinessGroup = objBussinessGroup;
    }    

    public String getFormName() {
        return this.FRM_BUSS_GROUP;
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

    public BussinessGroup getEntityObject()
    {
        return objBussinessGroup;
    }

    public void requestEntityObject(BussinessGroup objBussinessGroup)
    {        
        try {
            this.requestParam();

            objBussinessGroup.setBussGroupCode(this.getString(FRM_FIELD_CODE));
            objBussinessGroup.setBussGroupName(this.getString(FRM_FIELD_NAME));
            objBussinessGroup.setBussGroupAddress(this.getString(FRM_FIELD_ADDRESS));
            objBussinessGroup.setBussGroupCity(this.getString(FRM_FIELD_CITY));
            objBussinessGroup.setBussGroupPhone(this.getString(FRM_FIELD_PHONE));
            objBussinessGroup.setBussGroupFax(this.getString(FRM_FIELD_FAX));

            this.objBussinessGroup = objBussinessGroup;
        }catch(Exception e) {
            System.out.println("Exception on request entity object"+e.toString());
            objBussinessGroup = new BussinessGroup();
        }       
    }
}
