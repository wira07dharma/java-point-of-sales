/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.rsvcustpackbilling;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dewa
 */
public class ResvCustomPackBilling extends Entity {

    private long billingTypeItemId = 0;
    private long billingTypeId = 0;
    private long travelPackTypeId = 0;
    private double quantity = 0;
    private int quantityType = 0;
    private double priceRp = 0;
    private double priceUsd = 0;
    private double totalPriceRp = 0;
    private double totalPriceUsd = 0;
    private int discountType = 0;
    private double discountPercentage = 0;
    private double discountAmountRp = 0;
    private double discountAmountUsd = 0;
    private double serviceRp = 0;
    private double serviceUsd = 0;
    private double taxRp = 0;
    private double taxUsd = 0;
    private long contractId = 0;
    private long roomClassId = 0;
    private int useDefault = 0;
    private int type = 0;
    private Date dateTaken = null;
    private long typeSales = 0;
    private long typeCompliment = 0;
    private long reservationId = 0;
    private double rate = 0;
    private int consume = 0;
    private int duration = 0;
    private int paxMale = 0;
    private int paxFemale = 0;
    private String note = "";
    private long roomId = 0;
    private long tableId = 0;
    private long customerIdToCharge = 0;
    private long customerIdConsume = 0;
    private int fixRoom = 0;
    private int fixPic = 0;
    private long currencyType = 0;
    private int serviceInclude = 0;
    private int taxInclude = 0;
    private Date startDate = null;
    private Date endDate = null;
    private String roomName = "";

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    private String locationName = "";
    private String itemName = "";

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getServiceInclude() {
        return serviceInclude;
    }

    public void setServiceInclude(int serviceInclude) {
        this.serviceInclude = serviceInclude;
    }

    public int getTaxInclude() {
        return taxInclude;
    }

    public void setTaxInclude(int taxInclude) {
        this.taxInclude = taxInclude;
    }

    public long getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(long currencyType) {
        this.currencyType = currencyType;
    }

    public long getBillingTypeItemId() {
        return billingTypeItemId;
    }

    public void setBillingTypeItemId(long billingTypeItemId) {
        this.billingTypeItemId = billingTypeItemId;
    }

    public long getBillingTypeId() {
        return billingTypeId;
    }

    public void setBillingTypeId(long billingTypeId) {
        this.billingTypeId = billingTypeId;
    }

    public long getTravelPackTypeId() {
        return travelPackTypeId;
    }

    public void setTravelPackTypeId(long travelPackTypeId) {
        this.travelPackTypeId = travelPackTypeId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(int quantityType) {
        this.quantityType = quantityType;
    }

    public double getPriceRp() {
        return priceRp;
    }

    public void setPriceRp(double priceRp) {
        this.priceRp = priceRp;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(double priceUsd) {
        this.priceUsd = priceUsd;
    }

    public double getTotalPriceRp() {
        return totalPriceRp;
    }

    public void setTotalPriceRp(double totalPriceRp) {
        this.totalPriceRp = totalPriceRp;
    }

    public double getTotalPriceUsd() {
        return totalPriceUsd;
    }

    public void setTotalPriceUsd(double totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountAmountRp() {
        return discountAmountRp;
    }

    public void setDiscountAmountRp(double discountAmountRp) {
        this.discountAmountRp = discountAmountRp;
    }

    public double getDiscountAmountUsd() {
        return discountAmountUsd;
    }

    public void setDiscountAmountUsd(double discountAmountUsd) {
        this.discountAmountUsd = discountAmountUsd;
    }

    public double getServiceRp() {
        return serviceRp;
    }

    public void setServiceRp(double serviceRp) {
        this.serviceRp = serviceRp;
    }

    public double getServiceUsd() {
        return serviceUsd;
    }

    public void setServiceUsd(double serviceUsd) {
        this.serviceUsd = serviceUsd;
    }

    public double getTaxRp() {
        return taxRp;
    }

    public void setTaxRp(double taxRp) {
        this.taxRp = taxRp;
    }

    public double getTaxUsd() {
        return taxUsd;
    }

    public void setTaxUsd(double taxUsd) {
        this.taxUsd = taxUsd;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public long getRoomClassId() {
        return roomClassId;
    }

    public void setRoomClassId(long roomClassId) {
        this.roomClassId = roomClassId;
    }

    public int getUseDefault() {
        return useDefault;
    }

    public void setUseDefault(int useDefault) {
        this.useDefault = useDefault;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public long getTypeSales() {
        return typeSales;
    }

    public void setTypeSales(long typeSales) {
        this.typeSales = typeSales;
    }

    public long getTypeCompliment() {
        return typeCompliment;
    }

    public void setTypeCompliment(long typeCompliment) {
        this.typeCompliment = typeCompliment;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getConsume() {
        return consume;
    }

    public void setConsume(int consume) {
        this.consume = consume;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPaxMale() {
        return paxMale;
    }

    public void setPaxMale(int paxMale) {
        this.paxMale = paxMale;
    }

    public int getPaxFemale() {
        return paxFemale;
    }

    public void setPaxFemale(int paxFemale) {
        this.paxFemale = paxFemale;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getCustomerIdToCharge() {
        return customerIdToCharge;
    }

    public void setCustomerIdToCharge(long customerIdToCharge) {
        this.customerIdToCharge = customerIdToCharge;
    }

    public long getCustomerIdConsume() {
        return customerIdConsume;
    }

    public void setCustomerIdConsume(long customerIdConsume) {
        this.customerIdConsume = customerIdConsume;
    }

    public int getFixRoom() {
        return fixRoom;
    }

    public void setFixRoom(int fixRoom) {
        this.fixRoom = fixRoom;
    }

    public int getFixPic() {
        return fixPic;
    }

    public void setFixPic(int fixPic) {
        this.fixPic = fixPic;
    }

}
