
/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 ******************************************************************
 */
package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.common.entity.payment.PstCurrencyType;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;

public class MemberReg extends Entity implements I_LogHistory {

    private String contactCode = "";
    private Date regdate;
    private long employeeId;
    private String compName = "";
    private String personName = "";
    private String personLastname = "";
    private String bussAddress = "";
    private String town = "";
    private String province = "";
    private String country = "";
    private String telpNr = "";
    private String telpMobile = "";
    private String fax = "";
    private String homeAddr = "";
    private String homeTown = "";
    private String homeProvince = "";
    private String homeCountry = "";
    private String homeTelp = "";
    private String homeFax = "";
    private String notes = "";
    private String directions = "";
    private String bankAcc = "";
    private String bankAcc2 = "";
    private int contactType;
    private String email = "";
    private long parentId;
    private long memberGroupId;
    private String memberBarcode = "";
    private String memberIdCardNumber = "";
    private int memberSex;
    private Date memberBirthDate;
    private int memberCounter;
    private long memberReligionId;
    private int memberStatus;
    private Date memberLastUpdate;
    private double memberCreditLimit;
    private double memberConsigmentLimit;
    //private String memberPaymentHistoryNote;
    private long CurrencyTypeIdMemberCreditLimit = 0;
    private long CurrencyTypeIdMemberConsigmentLimit = 0;

    /**
     * Holds value of property processStatus.
     */
    private int processStatus;

    private String postalCode = "";

    private int dayOfPayment = 0;

    private String title = "";

    private String nationality = "";
    private String homeState = "";
    private String compState = "";
    private String compEmail = "";
    private String memberOccupation = "";
    private String homeEmail = "";
    private String homePostalCode = "";

    private long locationId = 0;//ini untuk mapping member yang merupakan intern lokasi

    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        if (contactCode == null) {
            contactCode = "";
        }
        this.contactCode = contactCode;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        if (compName == null) {
            compName = "";
        }
        this.compName = compName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        if (personName == null) {
            personName = "";
        }
        this.personName = personName;
    }

    public String getPersonLastname() {
        return personLastname;
    }

    public void setPersonLastname(String personLastname) {
        if (personLastname == null) {
            personLastname = "";
        }
        this.personLastname = personLastname;
    }

    public String getBussAddress() {
        return bussAddress;
    }

    public void setBussAddress(String bussAddress) {
        if (bussAddress == null) {
            bussAddress = "";
        }
        this.bussAddress = bussAddress;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        if (town == null) {
            town = "";
        }
        this.town = town;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        if (province == null) {
            province = "";
        }
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country == null) {
            country = "";
        }
        this.country = country;
    }

    public String getTelpNr() {
        return telpNr;
    }

    public void setTelpNr(String telpNr) {
        if (telpNr == null) {
            telpNr = "";
        }
        this.telpNr = telpNr;
    }

    public String getTelpMobile() {
        return telpMobile;
    }

    public void setTelpMobile(String telpMobile) {
        if (telpMobile == null) {
            telpMobile = "";
        }
        this.telpMobile = telpMobile;
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

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        if (homeAddr == null) {
            homeAddr = "";
        }
        this.homeAddr = homeAddr;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        if (homeTown == null) {
            homeTown = "";
        }
        this.homeTown = homeTown;
    }

    public String getHomeProvince() {
        return homeProvince;
    }

    public void setHomeProvince(String homeProvince) {
        if (homeProvince == null) {
            homeProvince = "";
        }
        this.homeProvince = homeProvince;
    }

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        if (homeCountry == null) {
            homeCountry = "";
        }
        this.homeCountry = homeCountry;
    }

    public String getHomeTelp() {
        return homeTelp;
    }

    public void setHomeTelp(String homeTelp) {
        if (homeTelp == null) {
            homeTelp = "";
        }
        this.homeTelp = homeTelp;
    }

    public String getHomeFax() {
        return homeFax;
    }

    public void setHomeFax(String homeFax) {
        if (homeFax == null) {
            homeFax = "";
        }
        this.homeFax = homeFax;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (notes == null) {
            notes = "";
        }
        this.notes = notes;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        if (directions == null) {
            directions = "";
        }
        this.directions = directions;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        if (bankAcc == null) {
            bankAcc = "";
        }
        this.bankAcc = bankAcc;
    }

    public String getBankAcc2() {
        return bankAcc2;
    }

    public void setBankAcc2(String bankAcc2) {
        if (bankAcc2 == null) {
            bankAcc2 = "";
        }
        this.bankAcc2 = bankAcc2;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getMemberGroupId() {
        return memberGroupId;
    }

    public void setMemberGroupId(long memberGroupId) {
        this.memberGroupId = memberGroupId;
    }

    public String getMemberBarcode() {
        return memberBarcode;
    }

    public void setMemberBarcode(String memberBarcode) {
        if (memberBarcode == null) {
            memberBarcode = "";
        }
        this.memberBarcode = memberBarcode;
    }

    public String getMemberIdCardNumber() {
        return memberIdCardNumber;
    }

    public void setMemberIdCardNumber(String memberIdCardNumber) {
        if (memberIdCardNumber == null) {
            memberIdCardNumber = "";
        }
        this.memberIdCardNumber = memberIdCardNumber;
    }

    public int getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(int memberSex) {
        this.memberSex = memberSex;
    }

    public Date getMemberBirthDate() {
        return memberBirthDate;
    }

    public void setMemberBirthDate(Date memberBirthDate) {
        this.memberBirthDate = memberBirthDate;
    }

    public int getMemberCounter() {
        return memberCounter;
    }

    public void setMemberCounter(int memberCounter) {
        this.memberCounter = memberCounter;
    }

    public long getMemberReligionId() {
        return memberReligionId;
    }

    public void setMemberReligionId(long memberReligionId) {
        this.memberReligionId = memberReligionId;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Date getMemberLastUpdate() {
        return memberLastUpdate;
    }

    public void setMemberLastUpdate(Date memberLastUpdate) {
        this.memberLastUpdate = memberLastUpdate;
    }

    /**
     * Getter for property processStatus.
     *
     * @return Value of property processStatus.
     *
     */
    public int getProcessStatus() {
        return this.processStatus;
    }

    /**
     * Setter for property processStatus.
     *
     * @param processStatus New value of property processStatus.
     *
     */
    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    /**
     * @return the memberCreditLimit
     */
    public double getMemberCreditLimit() {
        return memberCreditLimit;
    }

    /**
     * @param memberCreditLimit the memberCreditLimit to set
     */
    public void setMemberCreditLimit(double memberCreditLimit) {
        this.memberCreditLimit = memberCreditLimit;
    }

    /**
     * @return the memberConsigmentLimit
     */
    public double getMemberConsigmentLimit() {
        return memberConsigmentLimit;
    }

    /**
     * @param memberConsigmentLimit the memberConsigmentLimit to set
     */
    public void setMemberConsigmentLimit(double memberConsigmentLimit) {
        this.memberConsigmentLimit = memberConsigmentLimit;
    }

    /**
     * @return the memberPaymentHistoryNote
     */
    /*public String getMemberPaymentHistoryNote() {
     return memberPaymentHistoryNote;
     }*/
    /**
     * @param memberPaymentHistoryNote the memberPaymentHistoryNote to set
     */
    /* public void setMemberPaymentHistoryNote(String memberPaymentHistoryNote) {
     this.memberPaymentHistoryNote = memberPaymentHistoryNote;
     }*/
    /**
     * @return the CurrencyTypeIdMemberCreditLimit
     */
    public long getCurrencyTypeIdMemberCreditLimit() {
        return CurrencyTypeIdMemberCreditLimit;
    }

    /**
     * @param CurrencyTypeIdMemberCreditLimit the
     * CurrencyTypeIdMemberCreditLimit to set
     */
    public void setCurrencyTypeIdMemberCreditLimit(long CurrencyTypeIdMemberCreditLimit) {
        this.CurrencyTypeIdMemberCreditLimit = CurrencyTypeIdMemberCreditLimit;
    }

    /**
     * @return the CurrencyTypeIdMemberConsigmentLimit
     */
    public long getCurrencyTypeIdMemberConsigmentLimit() {
        return CurrencyTypeIdMemberConsigmentLimit;
    }

    /**
     * @param CurrencyTypeIdMemberConsigmentLimit the
     * CurrencyTypeIdMemberConsigmentLimit to set
     */
    public void setCurrencyTypeIdMemberConsigmentLimit(long CurrencyTypeIdMemberConsigmentLimit) {
        this.CurrencyTypeIdMemberConsigmentLimit = CurrencyTypeIdMemberConsigmentLimit;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLogDetail(Entity prevDoc) {
        MemberReg prevPo = (MemberReg) prevDoc;

        String memberType = "";
        try {
            if (prevPo == null || prevPo.getOID() == 0 || prevPo.getMemberGroupId() != 0 || prevPo.getMemberGroupId() != this.getMemberGroupId()) {
                memberType = PstMemberGroup.getCodeMemberWithMemberID(this.getMemberGroupId());
            }
        } catch (Exception ex) {
            memberType = "";
        }

        String memberSex = "";
        try {
            if (prevPo == null || prevPo.getOID() == 0 || prevPo.getMemberSex() != this.getMemberSex()) {
                memberSex = PstMemberReg.sexNames[0][this.getMemberSex()];
            }
        } catch (Exception ex) {
            memberSex = "";
        }

        String consigmentLimit = "";
        try {
            if (prevPo == null || prevPo.getOID() == 0 || prevPo.getCurrencyTypeIdMemberConsigmentLimit() != this.getCurrencyTypeIdMemberConsigmentLimit() || prevPo.getMemberConsigmentLimit() != this.getMemberConsigmentLimit()) {
                String priceCode = PstCurrencyType.getPriceCode(this.getCurrencyTypeIdMemberConsigmentLimit());
                consigmentLimit = consigmentLimit + priceCode + " " + this.getMemberConsigmentLimit();
            }
        } catch (Exception ex) {
            consigmentLimit = "";
        }

        String creditLimit = "";
        try {
            if (prevPo == null || prevPo.getOID() == 0 || prevPo.getCurrencyTypeIdMemberCreditLimit() != this.getCurrencyTypeIdMemberCreditLimit() || prevPo.getMemberCreditLimit() != this.getMemberCreditLimit()) {
                String priceCode = PstCurrencyType.getPriceCode(this.getCurrencyTypeIdMemberCreditLimit());
                creditLimit = creditLimit + priceCode + " " + this.getMemberCreditLimit();
            }
        } catch (Exception ex) {
            creditLimit = "";
        }

        return ((prevPo == null || prevPo.getOID() == 0 || prevPo.getContactCode() == null || prevPo.getPersonName().compareToIgnoreCase(this.getPersonName()) != 0)
                ? ("Code Member : " + this.getContactCode() + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getMemberGroupId() != this.getMemberGroupId())
                        ? (" Member Type : " + memberType + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getPersonName() == null || prevPo.getPersonName().compareToIgnoreCase(this.getPersonName()) != 0)
                        ? (" Contact Person : " + this.getPersonName() + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getHomeAddr() == null || prevPo.getHomeAddr().compareToIgnoreCase(this.getHomeAddr()) != 0)
                        ? (" Address CP: " + this.getHomeAddr() + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getHomeTelp() == null || prevPo.getHomeTelp().compareToIgnoreCase(this.getHomeTelp()) != 0)
                        ? (" No Tlp CP : " + this.getHomeTelp() + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getTelpMobile() == null || prevPo.getTelpMobile().compareToIgnoreCase(this.getTelpMobile()) != 0)
                        ? (" Days : " + this.getTelpMobile() + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getHomeTown() == null || prevPo.getHomeTown().compareToIgnoreCase(this.getHomeTown()) != 0)
                        ? (" Place : " + this.getHomeTown() + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || !Formater.formatDate(prevPo.getMemberBirthDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getMemberBirthDate(), "yyyy-MM-dd")))
                        ? (" and Date Of Birth : " + Formater.formatDate(this.getMemberBirthDate(), "yyyy-MM-dd") + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getMemberSex() != this.getMemberSex())
                        ? (" Gender : " + memberSex + " ;") : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getMemberIdCardNumber() == null || prevPo.getMemberIdCardNumber().compareToIgnoreCase(this.getMemberIdCardNumber()) != 0)
                        ? (" No ID : " + this.getMemberIdCardNumber()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getCurrencyTypeIdMemberConsigmentLimit() != this.getCurrencyTypeIdMemberConsigmentLimit() || prevPo.getMemberConsigmentLimit() != this.getMemberConsigmentLimit())
                        ? (" Consigment Limit : " + consigmentLimit) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getMemberIdCardNumber() == null || prevPo.getMemberIdCardNumber().compareToIgnoreCase(this.getMemberIdCardNumber()) != 0)
                        ? (" Credit Limit : " + creditLimit) : "")
                + //data perusahaan
                ((prevPo == null || prevPo.getOID() == 0 || prevPo.getCompName().compareToIgnoreCase(this.getCompName()) != 0)
                        ? (" Company Name : " + this.getCompName()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getBussAddress().compareToIgnoreCase(this.getBussAddress()) != 0)
                        ? (" Company Address : " + this.getBussAddress()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getTelpNr().compareToIgnoreCase(this.getTelpNr()) != 0)
                        ? (" Company Tlp : " + this.getBussAddress()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getFax().compareToIgnoreCase(this.getFax()) != 0)
                        ? (" Company Fax : " + this.getFax()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getTown().compareToIgnoreCase(this.getTown()) != 0)
                        ? (" Company City : " + this.getTown()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getProvince().compareToIgnoreCase(this.getProvince()) != 0)
                        ? (" Company Province : " + this.getProvince()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getCountry().compareToIgnoreCase(this.getCountry()) != 0)
                        ? (" Company Country : " + this.getCountry()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getPostalCode().compareToIgnoreCase(this.getPostalCode()) != 0)
                        ? (" Company Zip : " + this.getPostalCode()) : "")
                + ((prevPo == null || prevPo.getOID() == 0 || prevPo.getPostalCode().compareToIgnoreCase(this.getPostalCode()) != 0)
                        ? (" Company Zip : " + this.getPostalCode()) : "");

    }

    /**
     * @return the dayOfPayment
     */
    public int getDayOfPayment() {
        return dayOfPayment;
    }

    /**
     * @param dayOfPayment the dayOfPayment to set
     */
    public void setDayOfPayment(int dayOfPayment) {
        this.dayOfPayment = dayOfPayment;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the homeState
     */
    public String getHomeState() {
        return homeState;
    }

    /**
     * @param homeState the homeState to set
     */
    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }

    /**
     * @return the compState
     */
    public String getCompState() {
        return compState;
    }

    /**
     * @param compState the compState to set
     */
    public void setCompState(String compState) {
        this.compState = compState;
    }

    /**
     * @return the compEmail
     */
    public String getCompEmail() {
        return compEmail;
    }

    /**
     * @param compEmail the compEmail to set
     */
    public void setCompEmail(String compEmail) {
        this.compEmail = compEmail;
    }

    /**
     * @return the memberOccupation
     */
    public String getMemberOccupation() {
        return memberOccupation;
    }

    /**
     * @param memberOccupation the memberOccupation to set
     */
    public void setMemberOccupation(String memberOccupation) {
        this.memberOccupation = memberOccupation;
    }

    /**
     * @return the homeEmail
     */
    public String getHomeEmail() {
        return homeEmail;
    }

    /**
     * @param homeEmail the homeEmail to set
     */
    public void setHomeEmail(String homeEmail) {
        this.homeEmail = homeEmail;
    }

    /**
     * @return the homePostalCode
     */
    public String getHomePostalCode() {
        return homePostalCode;
    }

    /**
     * @param homePostalCode the homePostalCode to set
     */
    public void setHomePostalCode(String homePostalCode) {
        this.homePostalCode = homePostalCode;
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

}
