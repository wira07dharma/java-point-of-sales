/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.email;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class SettingTemplateInfo extends Entity {

    private String templateInfoApplication = "";
    private String templateInfoTypeMenu = "";
    private int templateInfoTypeSend = 0;
    private int templateInfoTypeSendBy = 0;
    private String templateInfoContentText = "";
    private String templateInfoContentHtml = "";
    private String templateInfoSubject = "";
    private Date templateInfoDateStart = null;
    private Date templateInfoDateEnd = null;

    public String getTemplateInfoApplication() {
        return templateInfoApplication;
    }

    public void setTemplateInfoApplication(String templateInfoApplication) {
        this.templateInfoApplication = templateInfoApplication;
    }

    public String getTemplateInfoTypeMenu() {
        return templateInfoTypeMenu;
    }

    public void setTemplateInfoTypeMenu(String templateInfoTypeMenu) {
        this.templateInfoTypeMenu = templateInfoTypeMenu;
    }

    public int getTemplateInfoTypeSend() {
        return templateInfoTypeSend;
    }

    public void setTemplateInfoTypeSend(int templateInfoTypeSend) {
        this.templateInfoTypeSend = templateInfoTypeSend;
    }

    public int getTemplateInfoTypeSendBy() {
        return templateInfoTypeSendBy;
    }

    public void setTemplateInfoTypeSendBy(int templateInfoTypeSendBy) {
        this.templateInfoTypeSendBy = templateInfoTypeSendBy;
    }

    public String getTemplateInfoContentText() {
        return templateInfoContentText;
    }

    public void setTemplateInfoContentText(String templateInfoContentText) {
        this.templateInfoContentText = templateInfoContentText;
    }

    public String getTemplateInfoContentHtml() {
        return templateInfoContentHtml;
    }

    public void setTemplateInfoContentHtml(String templateInfoContentHtml) {
        this.templateInfoContentHtml = templateInfoContentHtml;
    }

    public String getTemplateInfoSubject() {
        return templateInfoSubject;
    }

    public void setTemplateInfoSubject(String templateInfoSubject) {
        this.templateInfoSubject = templateInfoSubject;
    }

    public Date getTemplateInfoDateStart() {
        return templateInfoDateStart;
    }

    public void setTemplateInfoDateStart(Date templateInfoDateStart) {
        this.templateInfoDateStart = templateInfoDateStart;
    }

    public Date getTemplateInfoDateEnd() {
        return templateInfoDateEnd;
    }

    public void setTemplateInfoDateEnd(Date templateInfoDateEnd) {
        this.templateInfoDateEnd = templateInfoDateEnd;
    }

}
