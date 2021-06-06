/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.logger;

import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;
import java.util.Date;

/** 
 *
 * @author AGUS
 * Objectives;
 *  - Menyiapkan fasilitas untuk tracking dan control aktifitas user terhadap data-data dan dokumen 
 *   yang dibuat maupun di update user di system
 * Use Cases :
 *  - Admin :
 *       1. Mencari dan list aktifitas user berdasarkan : user name /login ID, documen number, date/time of update, detail data, action
 *       2. Drill down membuka document yang diubah dengan click : di list log
 *       3. Drill down membuka user yang dilog
 *       4. Ide : roll back document / data ..ini agak rumit deh ..
 *  - User : 
 *       1. user bisa melihat list history pada saat buka document bila diperlukan dengan click ShoW History
 * 
 */
public class LogSysHistory extends Entity{
    private long logUserId;
    private String logLoginName;
    private String logDocumentNumber;
    private String logDocumentType;
    private int logDocumentStatus;
    private long logDocumentId;
    private String logUserAction;
    private String logOpenUrl;
    private Date logUpdateDate;
    private String logDetail;
    private String logApplication;

    /**
     * @return the logUserId
     */
    public long getLogUserId() {
        return logUserId;
    }

    /**
     * @param logUserId the logUserId to set
     */
    public void setLogUserId(long logUserId) {
        this.logUserId = logUserId;
    }

    /**
     * @return the logLoginName
     */
    public String getLogLoginName() {
        return logLoginName;
    }

    /**
     * @param logLoginName the logLoginName to set
     */
    public void setLogLoginName(String logLoginName) {
        this.logLoginName = logLoginName;
    }

    /**
     * @return the logDocumentNumber
     */
    public String getLogDocumentNumber() {
        return logDocumentNumber;
    }

    /**
     * @param logDocumentNumber the logDocumentNumber to set
     */
    public void setLogDocumentNumber(String logDocumentNumber) {
        this.logDocumentNumber = logDocumentNumber;
    }

    /**
     * @return the logDocumentType
     */
    public String getLogDocumentType() {
        return logDocumentType;
    }

    /**
     * @param logDocumentType the logDocumentType to set
     */
    public void setLogDocumentType(String logDocumentType) {
        this.logDocumentType = logDocumentType;
    }

    /**
     * @return the logDocumentId
     */
    public long getLogDocumentId() {
        return logDocumentId;
    }

    /**
     * @param logDocumentId the logDocumentId to set
     */
    public void setLogDocumentId(long logDocumentId) {
        this.logDocumentId = logDocumentId;
    }

    /**
     * @return the logUserAction
     */
    public String getLogUserAction() {
        return logUserAction;
    }

    /**
     * @param logUserAction the logUserAction to set
     */
    public void setLogUserAction(String logUserAction) {
        this.logUserAction = logUserAction;
    }

    /**
     * @return the logOpenUrl
     */
    public String getLogOpenUrl() {
        return logOpenUrl;
    }

    /**
     * @param logOpenUrl the logOpenUrl to set
     */
    public void setLogOpenUrl(String logOpenUrl) {
        this.logOpenUrl = logOpenUrl;
    }

    /**
     * @return the logUpdateDate
     */
    public Date getLogUpdateDate() {
        return logUpdateDate;
    }

    /**
     * @param logUpdateDate the logUpdateDate to set
     */
    public void setLogUpdateDate(Date logUpdateDate) {
        this.logUpdateDate = logUpdateDate;
    }

    /**
     * @return the logDetail
     */
    public String getLogDetail() {
        return logDetail;
    }

    /**
     * @param logDetail the logDetail to set
     */
    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    /**
     * @return the logApplication
     */
    public String getLogApplication() {
        return logApplication;
    }

    /**
     * @param logApplication the logApplication to set
     */
    public void setLogApplication(String logApplication) {
        this.logApplication = logApplication;
    }

    /**
     * @return the logDocumentStatus
     */
    public int getLogDocumentStatus() {
        return logDocumentStatus;
    }

    /**
     * @param logDocumentStatus the logDocumentStatus to set
     */
    public void setLogDocumentStatus(int logDocumentStatus) {
        this.logDocumentStatus = logDocumentStatus;
    }
   
}
