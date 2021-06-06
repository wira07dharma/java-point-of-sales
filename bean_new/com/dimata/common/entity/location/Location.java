
/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 * *****************************************************************
 */
package com.dimata.common.entity.location;

/* package java */
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;
import java.util.Date;

public class Location extends Entity implements Serializable {

    private String name = "";
    private long contactId = 0;
    private String description = "";
    private String code = "";
    private String address = "";
    private String telephone = "";
    private String fax = "";
    private String person = "";
    private String email = "";
    private int type = 0;
    private long parentLocationId = 0;
    private String website = "";
    private long vendorId;

    //add prochain 13-06-2012
    private double servicePersen = 0.0;
    private double taxPersen = 0.0;

    private long departmentId = 0;
    private double serviceValue = 0.0;
    private double taxValue = 0.0;
    private double serviceValueUsd = 0.0;
    private double taxValueUsd = 0.0;
    private int reportGroup = 0;
    private int typeBase = 0;
    private int locIndex = 0;
    private long regencyId = 0;
    private int taxSvcDefault = 0;
    private double persentaseDistributionPurchaseOrder = 0.0;
    //add fitra 29-01-2014
    private long companyId = 0;

    //update by fitra
    private String companyName;
    private String departmentName;

    //update opie-eyek 20151012
    private long priceTypeId = 0;
    private String priceTypeName = "";
    private long standarRateId = 0;
    private String symbolCurr = "";
    private long useTable = 0;

    //update by de koyo 20160930
    private String acountingEmail = "";
    private String locationIp = "";
    private String sistemAddressHistoryOutlet = "";

    // updated by dewok++ 2017-03-21    
    private Date openingTime = null;
    private Date closingTime = null;

    // updated by dewok++ 2019-02-05
    private long discountTypeId = 0;
    private long memberGroupId = 0;

    private String icon = "";
    private int maxExchangeDay = 0;

    public int getLocIndex() {
        return locIndex;
    }

    public void setLocIndex(int locIndex) {
        this.locIndex = locIndex;
    }

    public int getTypeBase() {
        return typeBase;
    }

    public void setTypeBase(int typeBase) {
        this.typeBase = typeBase;
    }

    public double getServicePersen() {
        return servicePersen;
    }

    public void setServicePersen(double servicePersen) {
        this.servicePersen = servicePersen;
    }

    public double getTaxPersen() {
        return taxPersen;
    }

    public void setTaxPersen(double taxPersen) {
        this.taxPersen = taxPersen;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public double getServiceValue() {
        return serviceValue;
    }

    public void setServiceValue(double serviceValue) {
        this.serviceValue = serviceValue;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public double getServiceValueUsd() {
        return serviceValueUsd;
    }

    public void setServiceValueUsd(double serviceValueUsd) {
        this.serviceValueUsd = serviceValueUsd;
    }

    public double getTaxValueUsd() {
        return taxValueUsd;
    }

    public void setTaxValueUsd(double taxValueUsd) {
        this.taxValueUsd = taxValueUsd;
    }

    public int getReportGroup() {
        return reportGroup;
    }

    public void setReportGroup(int reportGroup) {
        this.reportGroup = reportGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null) {
            code = "";
        }
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null) {
            address = "";
        }
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null) {
            telephone = "";
        }
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        if (fax == null) {
            fax = "";
        }
        this.fax = fax;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        if (person == null) {
            person = "";
        }
        this.person = person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            email = "";
        }
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        if (website == null) {
            website = "";
        }
        this.website = website;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public String getPstClassName() {
        return "com.dimata.common.entity.location.PstLocation";
    }

    /**
     * @return the taxSvcDefault
     */
    public int getTaxSvcDefault() {
        return taxSvcDefault;
    }

    /**
     * @param taxSvcDefault the taxSvcDefault to set
     */
    public void setTaxSvcDefault(int taxSvcDefault) {
        this.taxSvcDefault = taxSvcDefault;
    }

    /**
     * @return the persentaseDistributionPurchaseOrder
     */
    public double getPersentaseDistributionPurchaseOrder() {
        return persentaseDistributionPurchaseOrder;
    }

    /**
     * @param persentaseDistributionPurchaseOrder the
     * persentaseDistributionPurchaseOrder to set
     */
    public void setPersentaseDistributionPurchaseOrder(double persentaseDistributionPurchaseOrder) {
        this.persentaseDistributionPurchaseOrder = persentaseDistributionPurchaseOrder;
    }

    /**
     * @return the companyName
     */
    /**
     * @return the companyId
     */
    public long getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the priceTypeId
     */
    public long getPriceTypeId() {
        return priceTypeId;
    }

    /**
     * @param priceTypeId the priceTypeId to set
     */
    public void setPriceTypeId(long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    /**
     * @return the standarRateId
     */
    public long getStandarRateId() {
        return standarRateId;
    }

    /**
     * @param standarRateId the standarRateId to set
     */
    public void setStandarRateId(long standarRateId) {
        this.standarRateId = standarRateId;
    }

    /**
     * @return the priceTypeName
     */
    public String getPriceTypeName() {
        return priceTypeName;
    }

    /**
     * @param priceTypeName the priceTypeName to set
     */
    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    /**
     * @return the symbolCurr
     */
    public String getSymbolCurr() {
        return symbolCurr;
    }

    /**
     * @param symbolCurr the symbolCurr to set
     */
    public void setSymbolCurr(String symbolCurr) {
        this.symbolCurr = symbolCurr;
    }

    /**
     * @return the useTable
     */
    public long getUseTable() {
        return useTable;
    }

    /**
     * @param useTable the useTable to set
     */
    public void setUseTable(long useTable) {
        this.useTable = useTable;
    }

    /**
     * @return the acountingEmail
     */
    public String getAcountingEmail() {
        if (acountingEmail == null) {
            acountingEmail = "";
        }
        return acountingEmail;
    }

    /**
     * @param acountingEmail the acountingEmail to set
     */
    public void setAcountingEmail(String acountingEmail) {
        this.acountingEmail = acountingEmail;
    }

    /**
     * @return the locationIp
     */
    public String getLocationIp() {
        if (locationIp == null) {
            locationIp = "";
        }
        return locationIp;
    }

    /**
     * @param locationIp the locationIp to set
     */
    public void setLocationIp(String locationIp) {
        this.locationIp = locationIp;
    }

    /**
     * @return the sistemAddressHistoryOutlet
     */
    public String getSistemAddressHistoryOutlet() {
        return sistemAddressHistoryOutlet;
    }

    /**
     * @param sistemAddressHistoryOutlet the sistemAddressHistoryOutlet to set
     */
    public void setSistemAddressHistoryOutlet(String sistemAddressHistoryOutlet) {
        this.sistemAddressHistoryOutlet = sistemAddressHistoryOutlet;
    }

    public Date getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Date openingTime) {
        this.openingTime = openingTime;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(long discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public long getMemberGroupId() {
        return memberGroupId;
    }

    public void setMemberGroupId(long memberGroupId) {
        this.memberGroupId = memberGroupId;
    }

    /**
     * @return the maxExchangeDay
     */
    public int getMaxExchangeDay() {
        return maxExchangeDay;
    }

    /**
     * @param maxExchangeDay the maxExchangeDay to set
     */
    public void setMaxExchangeDay(int maxExchangeDay) {
        this.maxExchangeDay = maxExchangeDay;
    }

    /**
     * @return the regencyId
     */
    public long getRegencyId() {
        return regencyId;
    }

    /**
     * @param regencyId the regencyId to set
     */
    public void setRegencyId(long regencyId) {
        this.regencyId = regencyId;
    }

}
