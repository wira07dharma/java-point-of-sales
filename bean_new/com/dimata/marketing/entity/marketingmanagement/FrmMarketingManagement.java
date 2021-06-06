/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingmanagement;

import com.dimata.posbo.entity.marketing.MarketingManagement;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sunima
 */
public class FrmMarketingManagement extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    private MarketingManagement entMarketingManagement;
    private static final String FRM_NAME_MARKETING_MANAGEMENT = "FRM_NAME_MARKETING_MANAGEMENT";
    public static final int FRM_FIELD_MARKETING_MANAGEMENT_ID = 0;
    public static final int FRM_FIELD_MARKETING_MANAGEMENT_TYPE = 1;
    public static final int FRM_FIELD_MARKETING_MANAGEMENT_DESCRIPTION = 2;
    public static final int FRM_FIELD_MARKETING_START_DATE = 3;
    public static final int FRM_FIELD_MARKETING_END_DATE = 4;
    public static final int FRM_FIELD_MARKETING_MANAGEMENT_TITLE = 5;
    public static final int FRM_FIELD_MARKETING_MANAGEMENT_STATUS = 6;
    public static final int FRM_FIELD_MARKETING_MANAGEMENT_NOTE = 7;
    
    public  static String[] fieldNames = {
    
        "FRM_FIELD_MARKETING_MANAGEMENT_ID", 
        "RM_FIELD_MARKETING_MANAGEMENT_TYPE",
        "FRM_FIELD_MARKETING_MANAGEMENT_DESCRIPTION",
        "FRM_FIELD_MARKETING_START_DATE",
        "FRM_FIELD_MARKETING_END_DATE",
        "FRM_FIELD_MARKETING_MANAGEMENT_TITLE",
        "FRM_FIELD_MARKETING_MANAGEMENT_STATUS",
        "FRM_FIELD_MARKETING_MANAGEMENT_NOTE"
    };
    
    public static int[] fieldTypes = {
        
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING
    };

    public FrmMarketingManagement() {
    }
    
    public FrmMarketingManagement(MarketingManagement entMarketingManagement){
        this.entMarketingManagement = entMarketingManagement;
    }
    public FrmMarketingManagement(HttpServletRequest request, MarketingManagement entMarketingManagement){
        super(new FrmMarketingManagement(entMarketingManagement), request);
        this.entMarketingManagement = entMarketingManagement;
    }
    
    public String getFormName(){
        return FRM_NAME_MARKETING_MANAGEMENT;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int getFieldSize(){
        return fieldNames.length;
    }
    
    public MarketingManagement getEntityObject(){
        return entMarketingManagement;
    }
    
    public void requestEntityObject(MarketingManagement entMarketingManagement){
    
        try{
            this.requestParam();
            //entMarketingManagement.setMarketing_type(getInt(FRM_FIELD_MARKETING_MANAGEMENT_TYPE));
            entMarketingManagement.setMarketing_description(getString(FRM_FIELD_MARKETING_MANAGEMENT_DESCRIPTION));
            entMarketingManagement.setStart_date(Formater.formatDate(getString(FRM_FIELD_MARKETING_START_DATE), "yyyy-MM-dd"));
            entMarketingManagement.setEnd_date(Formater.formatDate(getString(FRM_FIELD_MARKETING_END_DATE), "yyyy-MM-dd"));
            entMarketingManagement.setMarketing_title(getString(FRM_FIELD_MARKETING_MANAGEMENT_TITLE));
            entMarketingManagement.setMarketing_status(getInt(FRM_FIELD_MARKETING_MANAGEMENT_STATUS));
            entMarketingManagement.setMarketing_note(getString(FRM_FIELD_MARKETING_MANAGEMENT_NOTE));
        }catch(Exception e){
             System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
