/*
 * PayGeneral.java
 *
 * Created on March 29, 2007, 2:44 PM
 */
package com.dimata.posbo.entity.masterdata;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

/**
 *
 * @author yunny
 */
public class PayGeneral extends Entity {

    // private long genId;

    private String companyName = "";
    private String compAddress = "";
    private String city = "";
    private String zipCode = "";
    private String bussinessType = "";
    private String taxOffice = "";
    private String regTaxNr = "";
    private String regTaxBusNr = "";
    private Date regTaxDate;
    private String tel = "";
    private String fax = "";
    private String leaderName = "";
    private String leaderPosition = "";
    private String taxRepLocation = "";
    private int taxYear;
    private int taxMonth;
    private int taxRepDate;
    private double taxPaidPct;
    private double taxPosCostPct;
    private double taxPosCostMax;
    private int taxRound1000;
    private int taxCalcMtd;
    private double nonTaxIncome;
    private int nonTaxWife;
    private int nonTaxDepnt;
    private int taxFormSignBy;
    private String localCurCode = "";
    private int workDays;

    /**
     * Getter for property companyName.
     *
     * @return Value of property companyName.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter for property companyName.
     *
     * @param companyName New value of property companyName.
     */
    public void setCompanyName(String companyName) {
        if (companyName == null) {
            companyName = "";
        }
        this.companyName = companyName;
    }

    /**
     * Getter for property compAddress.
     *
     * @return Value of property compAddress.
     */
    public String getCompAddress() {
        return compAddress;
    }

    /**
     * Setter for property compAddress.
     *
     * @param compAddress New value of property compAddress.
     */
    public void setCompAddress(String compAddress) {
        if (compAddress == null) {
            compAddress = "";
        }
        this.compAddress = compAddress;
    }

    /**
     * Getter for property city.
     *
     * @return Value of property city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter for property city.
     *
     * @param city New value of property city.
     */
    public void setCity(String city) {
        if (city == null) {
            city = "";
        }
        this.city = city;
    }

    /**
     * Getter for property zipCode.
     *
     * @return Value of property zipCode.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Setter for property zipCode.
     *
     * @param zipCode New value of property zipCode.
     */
    public void setZipCode(String zipCode) {
        if (zipCode == null) {
            zipCode = "";
        }
        this.zipCode = zipCode;
    }

    /**
     * Getter for property bussinesType.
     *
     * @return Value of property bussinesType.
     */
    public String getBussinessType() {
        return bussinessType;
    }

    /**
     * Setter for property bussinesType.
     *
     * @param bussinesType New value of property bussinesType.
     */
    public void setBussinessType(String bussinessType) {
        if (bussinessType == null) {
            bussinessType = "";
        }
        this.bussinessType = bussinessType;
    }

    /**
     * Getter for property taxOffice.
     *
     * @return Value of property taxOffice.
     */
    public String getTaxOffice() {
        return taxOffice;
    }

    /**
     * Setter for property taxOffice.
     *
     * @param taxOffice New value of property taxOffice.
     */
    public void setTaxOffice(String taxOffice) {
        if (taxOffice == null) {
            taxOffice = "";
        }
        this.taxOffice = taxOffice;
    }

    /**
     * Getter for property regTaxNr.
     *
     * @return Value of property regTaxNr.
     */
    public String getRegTaxNr() {
        return regTaxNr;
    }

    /**
     * Setter for property regTaxNr.
     *
     * @param regTaxNr New value of property regTaxNr.
     */
    public void setRegTaxNr(String regTaxNr) {
        if (regTaxNr == null) {
            regTaxNr = "";
        }
        this.regTaxNr = regTaxNr;
    }

    /**
     * Getter for property regTaxDate.
     *
     * @return Value of property regTaxDate.
     */
    public Date getRegTaxDate() {
        return regTaxDate;
    }

    /**
     * Setter for property regTaxDate.
     *
     * @param regTaxDate New value of property regTaxDate.
     */
    public void setRegTaxDate(Date regTaxDate) {
        this.regTaxDate = regTaxDate;
    }

    /**
     * Getter for property tel.
     *
     * @return Value of property tel.
     */
    public String getTel() {
        return tel;
    }

    /**
     * Setter for property tel.
     *
     * @param tel New value of property tel.
     */
    public void setTel(String tel) {
        if (tel == null) {
            tel = "";
        }
        this.tel = tel;
    }

    /**
     * Getter for property fax.
     *
     * @return Value of property fax.
     */
    public String getFax() {
        return fax;
    }

    /**
     * Setter for property fax.
     *
     * @param fax New value of property fax.
     */
    public void setFax(String fax) {
        if (fax == null) {
            fax = "";
        }
        this.fax = fax;
    }

    /**
     * Getter for property leaderName.
     *
     * @return Value of property leaderName.
     */
    public String getLeaderName() {
        return leaderName;
    }

    /**
     * Setter for property leaderName.
     *
     * @param leaderName New value of property leaderName.
     */
    public void setLeaderName(String leaderName) {
        if (leaderName == null) {
            leaderName = "";
        }
        this.leaderName = leaderName;
    }

    /**
     * Getter for property leaderPosition.
     *
     * @return Value of property leaderPosition.
     */
    public String getLeaderPosition() {
        return leaderPosition;
    }

    /**
     * Setter for property leaderPosition.
     *
     * @param leaderPosition New value of property leaderPosition.
     */
    public void setLeaderPosition(String leaderPosition) {
        if (leaderPosition == null) {
            leaderPosition = "";
        }
        this.leaderPosition = leaderPosition;
    }

    /**
     * Getter for property taxRepLocation.
     *
     * @return Value of property taxRepLocation.
     */
    public String getTaxRepLocation() {
        return taxRepLocation;
    }

    /**
     * Setter for property taxRepLocation.
     *
     * @param taxRepLocation New value of property taxRepLocation.
     */
    public void setTaxRepLocation(String taxRepLocation) {
        if (taxRepLocation == null) {
            taxRepLocation = "";
        }
        this.taxRepLocation = taxRepLocation;
    }

    /**
     * Getter for property taxYear.
     *
     * @return Value of property taxYear.
     */
    public int getTaxYear() {
        return taxYear;
    }

    /**
     * Setter for property taxYear.
     *
     * @param taxYear New value of property taxYear.
     */
    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    /**
     * Getter for property taxMonth.
     *
     * @return Value of property taxMonth.
     */
    public int getTaxMonth() {
        return taxMonth;
    }

    /**
     * Setter for property taxMonth.
     *
     * @param taxMonth New value of property taxMonth.
     */
    public void setTaxMonth(int taxMonth) {
        this.taxMonth = taxMonth;
    }

    /**
     * Getter for property taxRepDate.
     *
     * @return Value of property taxRepDate.
     */
    public int getTaxRepDate() {
        return taxRepDate;
    }

    /**
     * Setter for property taxRepDate.
     *
     * @param taxRepDate New value of property taxRepDate.
     */
    public void setTaxRepDate(int taxRepDate) {
        this.taxRepDate = taxRepDate;
    }

    /**
     * Getter for property taxPaidPct.
     *
     * @return Value of property taxPaidPct.
     */
    public double getTaxPaidPct() {
        return taxPaidPct;
    }

    /**
     * Setter for property taxPaidPct.
     *
     * @param taxPaidPct New value of property taxPaidPct.
     */
    public void setTaxPaidPct(double taxPaidPct) {
        this.taxPaidPct = taxPaidPct;
    }

    /**
     * Getter for property taxPosCostPct.
     *
     * @return Value of property taxPosCostPct.
     */
    public double getTaxPosCostPct() {
        return taxPosCostPct;
    }

    /**
     * Setter for property taxPosCostPct.
     *
     * @param taxPosCostPct New value of property taxPosCostPct.
     */
    public void setTaxPosCostPct(double taxPosCostPct) {
        this.taxPosCostPct = taxPosCostPct;
    }

    /**
     * Getter for property taxPosCostMax.
     *
     * @return Value of property taxPosCostMax.
     */
    public double getTaxPosCostMax() {
        return taxPosCostMax;
    }

    /**
     * Setter for property taxPosCostMax.
     *
     * @param taxPosCostMax New value of property taxPosCostMax.
     */
    public void setTaxPosCostMax(double taxPosCostMax) {
        this.taxPosCostMax = taxPosCostMax;
    }

    /**
     * Getter for property taxRound1000.
     *
     * @return Value of property taxRound1000.
     */
    public int getTaxRound1000() {
        return taxRound1000;
    }

    /**
     * Setter for property taxRound1000.
     *
     * @param taxRound1000 New value of property taxRound1000.
     */
    public void setTaxRound1000(int taxRound1000) {
        this.taxRound1000 = taxRound1000;
    }

    /**
     * Getter for property taxCalcMtd.
     *
     * @return Value of property taxCalcMtd.
     */
    public int getTaxCalcMtd() {
        return taxCalcMtd;
    }

    /**
     * Setter for property taxCalcMtd.
     *
     * @param taxCalcMtd New value of property taxCalcMtd.
     */
    public void setTaxCalcMtd(int taxCalcMtd) {
        this.taxCalcMtd = taxCalcMtd;
    }

    /**
     * Getter for property nonTaxWife.
     *
     * @return Value of property nonTaxWife.
     */
    public int getNonTaxWife() {
        return nonTaxWife;
    }

    /**
     * Setter for property nonTaxWife.
     *
     * @param nonTaxWife New value of property nonTaxWife.
     */
    public void setNonTaxWife(int nonTaxWife) {
        this.nonTaxWife = nonTaxWife;
    }

    /**
     * Getter for property nonTaxDepnt.
     *
     * @return Value of property nonTaxDepnt.
     */
    public int getNonTaxDepnt() {
        return nonTaxDepnt;
    }

    /**
     * Setter for property nonTaxDepnt.
     *
     * @param nonTaxDepnt New value of property nonTaxDepnt.
     */
    public void setNonTaxDepnt(int nonTaxDepnt) {
        this.nonTaxDepnt = nonTaxDepnt;
    }

    /**
     * Getter for property taxFormSignBy.
     *
     * @return Value of property taxFormSignBy.
     */
    public int getTaxFormSignBy() {
        return taxFormSignBy;
    }

    /**
     * Setter for property taxFormSignBy.
     *
     * @param taxFormSignBy New value of property taxFormSignBy.
     */
    public void setTaxFormSignBy(int taxFormSignBy) {
        this.taxFormSignBy = taxFormSignBy;
    }

    /**
     * Getter for property localCurCode.
     *
     * @return Value of property localCurCode.
     */
    public String getLocalCurCode() {
        return localCurCode;
    }

    /**
     * Setter for property localCurCode.
     *
     * @param localCurCode New value of property localCurCode.
     */
    public void setLocalCurCode(String localCurCode) {
        if (taxRepLocation == null) {
            taxRepLocation = "";
        }
        this.localCurCode = localCurCode;
    }

    /**
     * Getter for property regTaxBusNr.
     *
     * @return Value of property regTaxBusNr.
     */
    public String getRegTaxBusNr() {
        return regTaxBusNr;
    }

    /**
     * Setter for property regTaxBusNr.
     *
     * @param regTaxBusNr New value of property regTaxBusNr.
     */
    public void setRegTaxBusNr(String regTaxBusNr) {
        if (regTaxBusNr == null) {
            regTaxBusNr = "";
        }
        this.regTaxBusNr = regTaxBusNr;
    }

    /**
     * Getter for property nonTaxIncome.
     *
     * @return Value of property nonTaxIncome.
     */
    public double getNonTaxIncome() {
        return nonTaxIncome;
    }

    /**
     * Setter for property nonTaxIncome.
     *
     * @param nonTaxIncome New value of property nonTaxIncome.
     */
    public void setNonTaxIncome(double nonTaxIncome) {
        this.nonTaxIncome = nonTaxIncome;
    }

    /**
     * Getter for property workDays.
     *
     * @return Value of property workDays.
     */
    public int getWorkDays() {
        return workDays;
    }

    /**
     * Setter for property workDays.
     *
     * @param workDays New value of property workDays.
     */
    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

}
