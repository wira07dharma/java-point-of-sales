/*
 * SpecialJournalMain.java
 *
 * Created on February 6, 2007, 9:46 AM
 */

package com.dimata.aiso.entity.specialJournal;

/* import package qdep entity */
import com.dimata.qdep.entity.Entity;

/* import package java util Date */
import java.util.*;

/**
 *
 * @author  dwi
 */
public class SpecialJournalMain extends Entity {
    
    /**
     * Holds value of property voucher_number.
     */
    private String voucher_number="";    
    
    /**
     * Getter for property voucher_number.
     * @return Value of property voucher_number.
     */
    
    private Date entry_date = new Date();
    private Date trans_date = new Date();
    
    /**
     * Holds value of property voucher_counter.
     */
    private int voucher_counter = 0;
    
    /**
     * Holds value of property journal_number.
     */
    private String journal_number="";
    
    /**
     * Holds value of property reference.
     */
    private String reference = "";
    
    /**
     * Holds value of property book_type.
     */
    private int book_type = 0;
    
    /**
     * Holds value of property amount.
     */
    private double amount = 0;
    
    /**
     * Holds value of property amount_status.
     */
    private int amount_status = 0;
    
    /**
     * Holds value of property journal_status.
     */
    private int journal_status = 0;
    
    /**
     * Holds value of property bilyet_number.
     */
    private String bilyet_number = "";
    private Date bilyet_due_date;
    
    /**
     * Holds value of property id_perkiraan.
     */
    private long id_perkiraan = 0;    
    
    /**
     * Holds value of property department_id.
     */
    private long department_id = 0;
    
    /**
     * Holds value of property periode_id.
     */
    private long periode_id = 0;
    
    /**
     * Holds value of property user_id.
     */
    private long user_id = 0;
    
    /**
     * Holds value of property description.
     */
    private String description = "";
    
    /**
     * Holds value of property contact_id.
     */
    private long contact_id = 0;
    
    /**
     * Holds value of property journal_type.
     */
    private int journal_type = 0;
    
    /**
     * Holds value of property dataStatus.
     */
    private int dataStatus;    
    private Vector jurnalDetails = new Vector(1,1);
    private Vector activity = new Vector(1,1);
    
    /**
     * Holds value of property currency_type_id.
     */
    private long currency_type_id = 0;
    
    /**
     * Holds value of property standar_rate.
     */
    private double standar_rate = 0;
    
    /**
     * Holds value of property frcontact_id.
     */
    private long frcontact_id = 0;
    
    public String getVoucherNumber() {
        return this.voucher_number;
    }    
    
    /**
     * Setter for property voucher_number.
     * @param voucher_number New value of property voucher_number.
     */
    public void setVoucherNumber(String voucher_number) {
        this.voucher_number = voucher_number;
    }
    
    public Date getEntryDate(){
        return this.entry_date;
    }
    
    public void setEntryDate(Date entry_date){
        this.entry_date = entry_date;
    }
    
     public Date getTransDate(){
        return this.trans_date;
    }
    
    public void setTransDate(Date trans_date){
        this.trans_date = trans_date;
    }
    
    /**
     * Getter for property voucher_counter.
     * @return Value of property voucher_counter.
     */
    public int getVoucherCounter() {
        return this.voucher_counter;
    }
    
    /**
     * Setter for property voucher_counter.
     * @param voucher_counter New value of property voucher_counter.
     */
    public void setVoucherCounter(int voucher_counter) {
        this.voucher_counter = voucher_counter;
    }
    
    /**
     * Getter for property journal_number.
     * @return Value of property journal_number.
     */
    public String getJournalNumber() {
        return this.journal_number;
    }
    
    /**
     * Setter for property journal_number.
     * @param journal_number New value of property journal_number.
     */
    public void setJournalNumber(String journal_number) {
        this.journal_number = journal_number;
    }
    
    /**
     * Getter for property reference.
     * @return Value of property reference.
     */
    public String getReference() {
        return this.reference;
    }
    
    /**
     * Setter for property reference.
     * @param reference New value of property reference.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    /**
     * Getter for property book_type.
     * @return Value of property book_type.
     */
    public int getBookType() {
        return this.book_type;
    }
    
    /**
     * Setter for property book_type.
     * @param book_type New value of property book_type.
     */
    public void setBookType(int book_type) {
        this.book_type = book_type;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return this.amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property amount_status.
     * @return Value of property amount_status.
     */
    public int getAmountStatus() {
        return this.amount_status;
    }
    
    /**
     * Setter for property amount_status.
     * @param amount_status New value of property amount_status.
     */
    public void setAmountStatus(int amount_status) {
        this.amount_status = amount_status;
    }
    
    /**
     * Getter for property journal_status.
     * @return Value of property journal_status.
     */
    public int getJournalStatus() {
        return this.journal_status;
    }
    
    /**
     * Setter for property journal_status.
     * @param journal_status New value of property journal_status.
     */
    public void setJournalStatus(int journal_status) {
        this.journal_status = journal_status;
    }
    
    /**
     * Getter for property bilyet_number.
     * @return Value of property bilyet_number.
     */
    public String getBilyetNumber() {
        return this.bilyet_number;
    }
    
    /**
     * Setter for property bilyet_number.
     * @param bilyet_number New value of property bilyet_number.
     */
    public void setBilyetNumber(String bilyet_number) {
        this.bilyet_number = bilyet_number;
    }
    
    public Date getBilyetDueDate(){
        return this.bilyet_due_date;
    }
    
    public void setBilyetDueDate(Date bilyet_due_date){
        this.bilyet_due_date = bilyet_due_date;
    }
    
    /**
     * Getter for property id_perkiraan.
     * @return Value of property id_perkiraan.
     */
    public long getIdPerkiraan() {
        return this.id_perkiraan;
    }
    
    /**
     * Setter for property id_perkiraan.
     * @param id_perkiraan New value of property id_perkiraan.
     */
    public void setIdPerkiraan(long id_perkiraan) {
        this.id_perkiraan = id_perkiraan;
    }
    
    /**
     * Getter for property department_id.
     * @return Value of property department_id.
     */
    public long getDepartmentId() {
        return this.department_id;
    }
    
    /**
     * Setter for property department_id.
     * @param department_id New value of property department_id.
     */
    public void setDepartmentId(long department_id) {
        this.department_id = department_id;
    }
    
    /**
     * Getter for property periode_id.
     * @return Value of property periode_id.
     */
    public long getPeriodeId() {
        return this.periode_id;
    }
    
    /**
     * Setter for property periode_id.
     * @param periode_id New value of property periode_id.
     */
    public void setPeriodeId(long periode_id) {
        this.periode_id = periode_id;
    }
    
    /**
     * Getter for property user_id.
     * @return Value of property user_id.
     */
    public long getUserId() {
        return this.user_id;
    }
    
    /**
     * Setter for property user_id.
     * @param user_id New value of property user_id.
     */
    public void setUserId(long user_id) {
        this.user_id = user_id;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for property contact_id.
     * @return Value of property contact_id.
     */
    public long getContactId() {
        return this.contact_id;
    }
    
    /**
     * Setter for property contact_id.
     * @param contact_id New value of property contact_id.
     */
    public void setContactId(long contact_id) {
        this.contact_id = contact_id;
    }
    
    /**
     * Getter for property journal_type.
     * @return Value of property journal_type.
     */
    public int getJournalType() {
        return this.journal_type;
    }
    
    /**
     * Setter for property journal_type.
     * @param journal_type New value of property journal_type.
     */
    public void setJournalType(int journal_type) {
        this.journal_type = journal_type;
    }
    
    /**
     * Getter for property dataStatus.
     * @return Value of property dataStatus.
     */
    public int getDataStatus() {
        return this.dataStatus;
    }
    
    /**
     * Setter for property dataStatus.
     * @param dataStatus New value of property dataStatus.
     */
    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }
    
    public Vector getJurnalDetails(){ return jurnalDetails; }

    public void setJurnalDetails(Vector jurnalDetails){ this.jurnalDetails = jurnalDetails; }
    
    public Vector getActivity(){ return activity; }

    public void setActivity(Vector activity){ this.activity = activity; }
    
    public void addDetails(int i, SpecialJournalDetail entSpecialJurnalDetail){
        if(entSpecialJurnalDetail!=null){
           this.jurnalDetails.add(i, entSpecialJurnalDetail);
        }
    }
    
    public void addActivity(Vector tempDetail){
        if(tempDetail!=null){
           this.activity.add(tempDetail);
        }
    }

    public void addActivity(int i, SpecialJournalDetailAssignt entSpecialJournalDetailAssignt){
        if(entSpecialJournalDetailAssignt!=null){
           this.activity.add(i, entSpecialJournalDetailAssignt);
        }
    }
    
    public void updateDetails(int i, SpecialJournalDetail entSpecialJurnalDetail){
        if((entSpecialJurnalDetail!=null)&&(i<jurnalDetails.size())){
            this.jurnalDetails.set(i,(Object)entSpecialJurnalDetail);
        }
    }
    
    public void updateActivity(int i, SpecialJournalDetailAssignt entSpecialJournalDetailAssignt){
        if((entSpecialJournalDetailAssignt!=null)&&(i<activity.size())){
            this.activity.set(i,(Object)entSpecialJournalDetailAssignt);
        }
    }

    public void deleteDetails(int i){
        if((i<jurnalDetails.size())&&(i>=0)){
            this.jurnalDetails.remove(i);
        }
    }
    
    public void deleteActivity(int i){
        if((i<activity.size())&&(i>=0)){
            this.activity.remove(i);
        }
    }

    public SpecialJournalDetail getJurnalDetail(int i){
         if ( (i < jurnalDetails.size()) && (i >= 0))
          {
             SpecialJournalDetail entSpecialJournalDetail = (SpecialJournalDetail)jurnalDetails.get(i);
             return entSpecialJournalDetail ;
          }
         else
            return new SpecialJournalDetail();
    }
    
    public SpecialJournalDetailAssignt getActivity(int i){
         if ( (i < jurnalDetails.size()) && (i >= 0))
          {
             SpecialJournalDetailAssignt entSpecialJournalDetailAssignt = (SpecialJournalDetailAssignt)jurnalDetails.get(i);
             return entSpecialJournalDetailAssignt ;
          }
         else
            return new SpecialJournalDetailAssignt();
    }

    public void indexSyncronize(Vector jurnalDetails){
        if((jurnalDetails!=null)&&(jurnalDetails.size()>0)){
            for(int i=0; i<jurnalDetails.size(); i++){
                SpecialJournalDetail entSpecialJournalDetail = (SpecialJournalDetail)jurnalDetails.get(i);
                entSpecialJournalDetail.setIndex(i);
                updateDetails(i,entSpecialJournalDetail);
            }
        }
    }
    
     public void indexSyncronizeActivity(Vector activity){
        if((activity!=null)&&(activity.size()>0)){
            for(int i=0; i<activity.size(); i++){
                SpecialJournalDetailAssignt entSpecialJournalDetailAssignt = (SpecialJournalDetailAssignt)activity.get(i);
                entSpecialJournalDetailAssignt.setIndex(i); 
                updateActivity(i,entSpecialJournalDetailAssignt);
            }
        }
    }

    /**
     * ini di gunakan untuk pengecekan department
     * apakah combo nya di tampilkan atau tidak
      * @return
     */
    public boolean getDeptView(){
        boolean bool = true;
        if((jurnalDetails!=null)&&(jurnalDetails.size()>0)){
            for(int i=0; i<jurnalDetails.size(); i++){
                SpecialJournalDetail entSpecialJournalDetail = (SpecialJournalDetail)jurnalDetails.get(i);
                if(entSpecialJournalDetail.getDataStatus()!=PstSpecialJournalDetail.DATASTATUS_DELETE){
                    bool = false;
                    break;
                }
            }
        }
        return bool;
    }
    
    public boolean getDeptViewActivity(){
        boolean bool = true;
        if((activity!=null)&&(activity.size()>0)){
            for(int i=0; i<activity.size(); i++){
                SpecialJournalDetailAssignt entSpecialJournalDetailAssignt = (SpecialJournalDetailAssignt)activity.get(i);
                if(entSpecialJournalDetailAssignt.getDataStatus()!=PstSpecialJournalDetailAssignt.DATASTATUS_DELETE){
                    bool = false;
                    break;
                }
            }
        }
        return bool;
    }
    
    /**
     * Getter for property currency_type_id.
     * @return Value of property currency_type_id.
     */
    public long getCurrencyTypeId() {
        return this.currency_type_id;
    }
    
    /**
     * Setter for property currency_type_id.
     * @param currency_type_id New value of property currency_type_id.
     */
    public void setCurrencyTypeId(long currency_type_id) {
        this.currency_type_id = currency_type_id;
    }
    
    /**
     * Getter for property standar_rate.
     * @return Value of property standar_rate.
     */
    public double getStandarRate() {
        return this.standar_rate;
    }
    
    /**
     * Setter for property standar_rate.
     * @param standar_rate New value of property standar_rate.
     */
    public void setStandarRate(double standarrate) {
        this.standar_rate = standarrate;
    }
    
    /**
     * Getter for property frcontact_id.
     * @return Value of property frcontact_id.
     */
    public long getFrcontactId() {
        return this.frcontact_id;
    }
    
    /**
     * Setter for property frcontact_id.
     * @param frcontact_id New value of property frcontact_id.
     */
    public void setFrcontactId(long frcontact_id) {
        this.frcontact_id = frcontact_id;
    }
    
}
