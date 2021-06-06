/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class PriceProtection extends Entity{
   
    //private long supplierId;
    private String numberPP="";
    private int ppCounter=0;
    private long locationId;
    private Date dateCreated = new Date();
    private Date dateApproved = new Date();
    private double totalAmount=0.0;
    private double exchangeRate=0.0;
    private int status;
    private String remark="";
    private String locationName="";
    
    private Vector listProtectionItem = new Vector();
    
    public Vector getListProtectionItem() {
        return listProtectionItem;
    }

    public void setListProtectionItem(PriceProtectionItem purhItem) {
        this.listProtectionItem.add(purhItem);
    }
    
    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the exchangeRate
     */
    public double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * @param exchangeRate the exchangeRate to set
     */
    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the dateApproved
     */
    public Date getDateApproved() {
        return dateApproved;
    }

    /**
     * @param dateApproved the dateApproved to set
     */
    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the numberPP
     */
    public String getNumberPP() {
        return numberPP;
    }

    /**
     * @param numberPP the numberPP to set
     */
    public void setNumberPP(String numberPP) {
        this.numberPP = numberPP;
    }

    /**
     * @return the ppCounter
     */
    public int getPpCounter() {
        return ppCounter;
    }

    /**
     * @param ppCounter the ppCounter to set
     */
    public void setPpCounter(int ppCounter) {
        this.ppCounter = ppCounter;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         PriceProtection priceProtection = PstPriceProtection.fetchExc(oid);
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID], priceProtection.getOID());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION], priceProtection.getNumberPP());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_COUNTER], priceProtection.getPpCounter());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID], priceProtection.getLocationId());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE], priceProtection.getDateCreated());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_APPROVAL_DATE], priceProtection.getDateApproved());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_TOTAL_AMOUNT], priceProtection.getTotalAmount());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_EXCHANGE_RATE], priceProtection.getExchangeRate());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS], priceProtection.getStatus());
         object.put(PstPriceProtection.fieldNames[PstPriceProtection.FLD_REMARK], priceProtection.getRemark());
      }catch(Exception exc){}
      return object;
   }
    
}
