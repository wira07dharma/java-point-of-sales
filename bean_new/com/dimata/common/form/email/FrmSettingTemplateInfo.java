/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.email;

import com.dimata.common.entity.email.SettingTemplateInfo;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmSettingTemplateInfo extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SettingTemplateInfo entSettingTemplateInfo;
    public static final String FRM_NAME_SETTING_TEMPLATE_INFO = "FRM_NAME_SETTING_TEMPLATE_INFO";
    public static final int FRM_FIELD_TEMPLATE_INFO_ID = 0;
    public static final int FRM_FIELD_TEMPLATE_INFO_APPLICATION = 1;
    public static final int FRM_FIELD_TEMPLATE_INFO_TYPE_MENU = 2;
    public static final int FRM_FIELD_TEMPLATE_INFO_TYPE_SEND = 3;
    public static final int FRM_FIELD_TEMPLATE_INFO_TYPE_SEND_BY = 4;
    public static final int FRM_FIELD_TEMPLATE_INFO_CONTENT_TEXT = 5;
    public static final int FRM_FIELD_TEMPLATE_INFO_CONTENT_HTML = 6;
    public static final int FRM_FIELD_TEMPLATE_INFO_SUBJECT = 7;
    public static final int FRM_FIELD_TEMPLATE_INFO_DATE_START = 8;
    public static final int FRM_FIELD_TEMPLATE_INFO_DATE_END = 9;

    public static String[] fieldNames = {
        "FRM_FIELD_TEMPLATE_INFO_ID",
        "FRM_FIELD_TEMPLATE_INFO_APPLICATION",
        "FRM_FIELD_TEMPLATE_INFO_TYPE_MENU",
        "FRM_FIELD_TEMPLATE_INFO_TYPE_SEND",
        "FRM_FIELD_TEMPLATE_INFO_TYPE_SEND_BY",
        "FRM_FIELD_TEMPLATE_INFO_CONTENT_TEXT",
        "FRM_FIELD_TEMPLATE_INFO_CONTENT_HTML",
        "FRM_FIELD_TEMPLATE_INFO_SUBJECT",
        "FRM_FIELD_TEMPLATE_INFO_DATE_START",
        "FRM_FIELD_TEMPLATE_INFO_DATE_END"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public FrmSettingTemplateInfo() {
    }

    public FrmSettingTemplateInfo(SettingTemplateInfo entSettingTemplateInfo) {
        this.entSettingTemplateInfo = entSettingTemplateInfo;
    }

    public FrmSettingTemplateInfo(HttpServletRequest request, SettingTemplateInfo entSettingTemplateInfo) {
        super(new FrmSettingTemplateInfo(entSettingTemplateInfo), request);
        this.entSettingTemplateInfo = entSettingTemplateInfo;
    }

    public String getFormName() {
        return FRM_NAME_SETTING_TEMPLATE_INFO;
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

    public SettingTemplateInfo getEntityObject() {
        return entSettingTemplateInfo;
    }

    public void requestEntityObject(SettingTemplateInfo entSettingTemplateInfo) {
        try {
            this.requestParam();            
            String sStart = getString(FRM_FIELD_TEMPLATE_INFO_DATE_START);
            String sEnd = getString(FRM_FIELD_TEMPLATE_INFO_DATE_END);
            Date dStart = null;
            Date dEnd = null;            
            if (sStart.length() > 0) {
                dStart = Formater.formatDate(sStart, "yyyy-MM-dd");
            }
            if (sEnd.length() > 0) {
                dEnd = Formater.formatDate(sEnd, "yyyy-MM-dd");
            }
            entSettingTemplateInfo.setTemplateInfoApplication(getString(FRM_FIELD_TEMPLATE_INFO_APPLICATION));
            entSettingTemplateInfo.setTemplateInfoTypeMenu(getString(FRM_FIELD_TEMPLATE_INFO_TYPE_MENU));
            entSettingTemplateInfo.setTemplateInfoTypeSend(getInt(FRM_FIELD_TEMPLATE_INFO_TYPE_SEND));
            entSettingTemplateInfo.setTemplateInfoTypeSendBy(getInt(FRM_FIELD_TEMPLATE_INFO_TYPE_SEND_BY));
            entSettingTemplateInfo.setTemplateInfoContentText(getString(FRM_FIELD_TEMPLATE_INFO_CONTENT_TEXT));
            entSettingTemplateInfo.setTemplateInfoContentHtml(getString(FRM_FIELD_TEMPLATE_INFO_CONTENT_HTML));
            entSettingTemplateInfo.setTemplateInfoSubject(getString(FRM_FIELD_TEMPLATE_INFO_SUBJECT));
            entSettingTemplateInfo.setTemplateInfoDateStart(dStart);
            entSettingTemplateInfo.setTemplateInfoDateEnd(dEnd);
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
