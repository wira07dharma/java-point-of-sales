/*
 * InvoiceMain.java
 *
 * Created on November 12, 2007, 2:16 PM
 */

package com.dimata.aiso.entity.invoice;

/**
 *
 * @author  dwi
 */
/* import java */
import java.util.*;
import java.util.Date;

/* import qdep */
import com.dimata.qdep.entity.*;

public class InvoiceMain extends Entity{
    
    /**
     * Holds value of property status.
     */
    private int status = 0;    
   
    /**
     * Holds value of property totalPax.
     */
    private int totalPax = 0;
    
    /**
     * Holds value of property totalRoom.
     */
    private int totalRoom = 0;
    
    /**
     * Holds value of property firstContactId.
     */
    private long firstContactId = 0;
    
    /**
     * Holds value of property secondContactId.
     */
    private long secondContactId = 0;
    
    /**
     * Holds value of property guestName.
     */
    private String guestName = "";
    
    /**
     * Holds value of property hotelName.
     */
    private String hotelName = "";
    
    /**
     * Holds value of property idPerkiraan.
     */
    private long idPerkiraan = 0;
    
    /**
     * Holds value of property termOfPayment.
     */
    private int termOfPayment = 0; 
    
    /**
     * Holds value of property type.
     */
    private int type = 0;
    
    /**
     * Holds value of property totalDiscount.
     */
    private double totalDiscount = 0;
    
    /**
     * Holds value of property numberCounter.
     */
    private int numberCounter = 0;
    
    /**
     * Holds value of property invoiceNumber.
     */
    private String invoiceNumber = "";
    
    /**
     * Holds value of property tax.
     */
    private double tax = 0;
    
    /**
     * Holds value of property issuedDate.
     */    
     private Date issuedDate = new Date();
     
     /**
     * Holds value of property checkInDate.
     */    
     private Date checkInDate = new Date();
     
      /**
     * Holds value of property checkOutDate.
     */    
     private Date checkOutDate = new Date();
    
     /**
      * Holds value of property idPerkDeposit.
      */
     private long idPerkDeposit;
     
     private String description = "";
     
     private Date depositDate = new Date();
     
     private Date depositTimeLimit = new Date();
     
    /**
     * Getter for property status.
     * @return Value of property status.
     */
     
    public int getStatus() {
        return this.status;
    }    
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * Getter for property totalPax.
     * @return Value of property totalPax.
     */
    public int getTotalPax() {
        return this.totalPax;
    }
    
    /**
     * Setter for property totalPax.
     * @param totalPax New value of property totalPax.
     */
    public void setTotalPax(int totalPax) {
        this.totalPax = totalPax;
    }
    
    /**
     * Getter for property totalRoom.
     * @return Value of property totalRoom.
     */
    public int getTotalRoom() {
        return this.totalRoom;
    }
    
    /**
     * Setter for property totalRoom.
     * @param totalRoom New value of property totalRoom.
     */
    public void setTotalRoom(int totalRoom) {
        this.totalRoom = totalRoom;
    }
    
    /**
     * Getter for property firstContactId.
     * @return Value of property firstContactId.
     */
    public long getFirstContactId() {
        return this.firstContactId;
    }
    
    /**
     * Setter for property firstContactId.
     * @param firstContactId New value of property firstContactId.
     */
    public void setFirstContactId(long firstContactId) {
        this.firstContactId = firstContactId;
    }
    
    /**
     * Getter for property secondContactId.
     * @return Value of property secondContactId.
     */
    public long getSecondContactId() {
        return this.secondContactId;
    }
    
    /**
     * Setter for property secondContactId.
     * @param secondContactId New value of property secondContactId.
     */
    public void setSecondContactId(long secondContactId) {
        this.secondContactId = secondContactId;
    }
    
    /**
     * Getter for property guestName.
     * @return Value of property guestName.
     */
    public String getGuestName() {
        return this.guestName;
    }
    
    /**
     * Setter for property guestName.
     * @param guestName New value of property guestName.
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    /**
     * Getter for property hotelName.
     * @return Value of property hotelName.
     */
    public String getHotelName() {
        return this.hotelName;
    }
    
    /**
     * Setter for property hotelName.
     * @param hotelName New value of property hotelName.
     */
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    
    /**
     * Getter for property idPerkiraan.
     * @return Value of property idPerkiraan.
     */
    public long getIdPerkiraan() {
        return this.idPerkiraan;
    }
    
    /**
     * Setter for property idPerkiraan.
     * @param idPerkiraan New value of property idPerkiraan.
     */
    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
    }
    
    /**
     * Getter for property termOfPayment.
     * @return Value of property termOfPayment.
     */
    public int getTermOfPayment() {
        return this.termOfPayment;
    }
    
    /**
     * Setter for property termOfPayment.
     * @param termOfPayment New value of property termOfPayment.
     */
    public void setTermOfPayment(int termOfPayment) {
        this.termOfPayment = termOfPayment;
    }
    
    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }
    
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * Getter for property totalDiscount.
     * @return Value of property totalDiscount.
     */
    public double getTotalDiscount() {
        return this.totalDiscount;
    }
    
    /**
     * Setter for property totalDiscount.
     * @param totalDiscount New value of property totalDiscount.
     */
    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
    
    /**
     * Getter for property numberCounter.
     * @return Value of property numberCounter.
     */
    public int getNumberCounter() {
        return this.numberCounter;
    }
    
    /**
     * Setter for property numberCounter.
     * @param numberCounter New value of property numberCounter.
     */
    public void setNumberCounter(int numberCounter) {
        this.numberCounter = numberCounter;
    }
    
    /**
     * Getter for property invoiceNumber.
     * @return Value of property invoiceNumber.
     */
    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }
    
    /**
     * Setter for property invoiceNumber.
     * @param invoiceNumber New value of property invoiceNumber.
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    /**
     * Getter for property tax.
     * @return Value of property tax.
     */
    public double getTax() {
        return this.tax;
    }
    
    /**
     * Setter for property tax.
     * @param tax New value of property tax.
     */
    public void setTax(double tax) {
        this.tax = tax;
    }
    
    /**
     * Getter for property issuedDate.
     * @return Value of property issuedDate.
     */
    public Date getIssuedDate() {
        return this.issuedDate;
    }
    
    /**
     * Setter for property issuedDate.
     * @param tax New value of property issuedDate.
     */
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
    
     /**
     * Getter for property checkInDate.
     * @return Value of property checkInDate.
     */
    public Date getCheckInDate() {
        return this.checkInDate;
    }
    
    /**
     * Setter for property checkInDate.
     * @param tax New value of property checkInDate.
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    /**
     * Getter for property checkOutDate.
     * @return Value of property checkOutDate.
     */
    public Date getCheckOutDate() {
        return this.checkOutDate;
    }
    
    /**
     * Setter for property checkOutDate.
     * @param tax New value of property checkOutDate.
     */
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    /**
     * Getter for property idPerkDeposit.
     * @return Value of property idPerkDeposit.
     */
    public long getIdPerkDeposit() {
        return this.idPerkDeposit;
    }
    
    /**
     * Setter for property idPerkDeposit.
     * @param idPerkDeposit New value of property idPerkDeposit.
     */
    public void setIdPerkDeposit(long idPerkDeposit) {
        this.idPerkDeposit = idPerkDeposit;
    }
    
    public String getDescription(){
	return this.description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }
    
    public Date getDepositDate(){
	return this.depositDate;
    }
    
    public void setDepositDate(Date depositDate){
	this.depositDate = depositDate;
    }
    
    public Date getDepositTimeLimit(){
	return this.depositTimeLimit;
    }
    
    public void setDepositTimeLimit(Date depositTimeLimit){
	this.depositTimeLimit = depositTimeLimit;
    }
}
