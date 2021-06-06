/*
 * FrmServiceConfiguration.java
 *
 * Created on September 27, 2004, 9:35 PM
 */

package com.dimata.common.form.service;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.common.entity.service.*;
import com.dimata.common.session.service.*;

/**
 *
 * @author  gedhy  
 */
public class FrmServiceConfiguration extends FRMHandler implements I_FRMInterface, I_FRMType 
{
    
    private ServiceConfiguration serviceConfiguration;

    public static final String FRM_SERVICE_CONF	=  "FRM_SERVICE_CONF" ;

    public static final int FRM_FIELD_SERVICE_ID		=  0 ;
    public static final int FRM_FIELD_SERVICE_TYPE		=  1 ;
    public static final int FRM_FIELD_START_TIME		=  2 ;
    public static final int FRM_FIELD_PERIODE			=  3 ;    

    public static String[] fieldNames = {
            "FRM_FIELD_SERVICE_ID",  
            "FRM_FIELD_SERVICE_TYPE",
            "FRM_FIELD_START_TIME",
            "FRM_FIELD_PERIODE"              
    } ;

    public static int[] fieldTypes = {
            TYPE_LONG,  
            TYPE_INT,
            TYPE_DATE,
            TYPE_INT
    } ;

    public FrmServiceConfiguration(){
    }
    
    public FrmServiceConfiguration(ServiceConfiguration serviceConfiguration){
            this.serviceConfiguration = serviceConfiguration;
    }

    public FrmServiceConfiguration(HttpServletRequest request, ServiceConfiguration serviceConfiguration){
            super(new FrmServiceConfiguration(serviceConfiguration), request);
            this.serviceConfiguration = serviceConfiguration;
    }

    public String getFormName() { return FRM_SERVICE_CONF; } 

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; } 

    public int getFieldSize() { return fieldNames.length; } 

    public ServiceConfiguration getEntityObject(){ return serviceConfiguration; }

    public void requestEntityObject(ServiceConfiguration serviceConfiguration) {
            try{
                    this.requestParam();
                    serviceConfiguration.setServiceType(getInt(FRM_FIELD_SERVICE_TYPE));
                    serviceConfiguration.setStartTime(getDate(FRM_FIELD_START_TIME));
                    serviceConfiguration.setPeriode(getInt(FRM_FIELD_PERIODE));                    
            }catch(Exception e){
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
    }
    
}
