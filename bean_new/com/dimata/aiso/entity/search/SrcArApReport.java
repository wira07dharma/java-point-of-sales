package com.dimata.aiso.entity.search;

import java.util.Date;

/**
 * User: pulantara
 * Date: Oct 21, 2005
 * Time: 1:57:05 PM
 * Description:
 */
public class SrcArApReport {

    private static final long A_DAY_MILIS = (24*60*60*1000); 

    /** Holds value of report type */
    private int reportType = 0;
    /** Holds value of contact name */
    private String contactName = "";
    /** Holds value of from date */
    private Date fromDate = new Date();
    /** Holds value of until date */
    private Date untilDate = new Date();
    /** Holds value of notaNo */
    private String notaNo = "";
    /** Holds value of periodeSpan1 */
    private int periodeSpan1 = 0;
    /** Holds value of periodeSpan2 */
    private int periodeSpan2 = 0;
    /** Holds value of periodeSpan3 */
    private int periodeSpan3 = 0;
    /** Holds value of reportLink */
    private String reportLink = "";
    /** Hodls value of language */
    private int language = 0;
    
    private int viewType = 0;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public String getNotaNo() {
        return notaNo;
    }

    public void setNotaNo(String notaNo) {
        this.notaNo = notaNo;
    }

    public int getPeriodeSpan1() {
        return periodeSpan1;
    }

    public void setPeriodeSpan1(int periodeSpan1) {
        this.periodeSpan1 = periodeSpan1;
    }

    public int getPeriodeSpan2() {
        return periodeSpan2;
    }

    public void setPeriodeSpan2(int periodeSpan2) {
        this.periodeSpan2 = periodeSpan2;
    }

    public int getPeriodeSpan3() {
        return periodeSpan3;
    }

    public void setPeriodeSpan3(int periodeSpan3) {
        this.periodeSpan3 = periodeSpan3;
    }

    public Date getFirstPeriodeDueDate(){
        Date result = new Date(fromDate.getTime()+periodeSpan1*A_DAY_MILIS);
        return result;
    }

    public Date getSecondPeriodeDueDate(){
        Date result = new Date(this.getFirstPeriodeDueDate().getTime()+periodeSpan2*A_DAY_MILIS);
        return result;
    }

    public Date getThirdPeriodeDueDate(){
        Date result = new Date(this.getSecondPeriodeDueDate().getTime()+periodeSpan3*A_DAY_MILIS);
        return result;
    }

    public Date getFirstPeriodeOverDue(){
        Date result = new Date(fromDate.getTime()-periodeSpan1*A_DAY_MILIS);
        return result;
    }

    public Date getSecondPeriodeOverDue(){
        Date result = new Date(this.getFirstPeriodeOverDue().getTime()-periodeSpan2*A_DAY_MILIS);
        return result;
    }

    public Date getThirdPeriodeOverDue(){
        Date result = new Date(this.getSecondPeriodeOverDue().getTime()-periodeSpan3*A_DAY_MILIS);
        return result;
    }

    public Date getTommorowDueDate(){
        Date result = new Date(fromDate.getTime()+A_DAY_MILIS);
        return result;
    }

    public Date getAfterTommorowDueDate(){
        Date result = new Date(fromDate.getTime()+2*A_DAY_MILIS);
        return result;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }
    
    public int getViewType(){
        return this.viewType;
    }
    
    public void setViewType(int viewType){
        this.viewType = viewType;
    }
}
