/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.form.email;
import com.dimata.common.entity.email.SettingEmail;
import com.dimata.qdep.form.*;
import javax.servlet.http.*;
/**
 *
 * @author dimata005
 */
public class FrmSettingEmail extends FRMHandler implements I_FRMInterface, I_FRMType{
    public static final String FRM_SETTING_EMAIL = "APP_SETTING_EMAIL";

    public static final int FRM_EMAIL_NAME		= 0;
    public static final int FRM_PASSWORD		= 1;
    public static final int FRM_HOST                    = 2;
    public static final int FRM_PORT                    = 3;

     public static  final String[] fieldNames = {
        "EMAIL_NAME", "PASSWORD", "HOST","PORT"
    } ;

    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED , 
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED
    };

     private SettingEmail settingEmail = new SettingEmail();
    public  FrmSettingEmail() {
    }

    public  FrmSettingEmail(HttpServletRequest request) {
        super(new  FrmSettingEmail(), request);
    }

    public String getFormName() {
        return FRM_SETTING_EMAIL;
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


    public SettingEmail getEntityObject()
    {
        return settingEmail;
    }

     public void requestEntityObject(SettingEmail entObj)
    {
        try {
            this.requestParam();
            entObj.setEmailName(this.getString(FRM_EMAIL_NAME));
            entObj.setPassword(this.getString(FRM_PASSWORD));
            entObj.setHost(this.getString(FRM_HOST));
            entObj.setPort(this.getString(FRM_PORT));

            this.settingEmail = entObj;
        }catch(Exception e) {
            System.out.println("EXC... "+e);
            entObj = new SettingEmail();
        }
    }

}
