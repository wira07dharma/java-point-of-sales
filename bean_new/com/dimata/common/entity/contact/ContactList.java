
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.common.entity.contact;

/* package java */
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.posbo.entity.masterdata.MemberGroup;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import java.util.Date;
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;

public class ContactList extends Entity implements Serializable {

	private String contactCode = "";
	private long employeeId;
	private long parentId;
	private int contactType;
	private Date regdate;
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
	private String bankAcc = "";
	private String bankAcc2 = "";
	private String email = "";
        private String directions = "";    
        private Date lastUpdate;      
        private int processStatus = 0; 
        
        //new
        private String companyBankAcc = "";
        private String positionPerson = "";
        private String postalCodeCompany = "";
        private String websiteCompany = "";
        private String emailCompany = "";
        private String postalCodeHome = "";
        private long locationId;
        
        //term
        private int termOfDelivery=0;
        
        // add by fitra
        private String title = "";
        private String homeState = "";
        
        private Date memberBirthDate;   
        private int memberSex;
        private long memberReligionId;
        private String nationality = "";
        private String memberOccupation = "";
        private String homeEmail = "";
        private String memberIdCardNumber = "";

        private long CurrencyTypeIdMemberConsigmentLimit = 0;

        private double memberConsigmentLimit;

        private long CurrencyTypeIdMemberCreditLimit =0;

        private double memberCreditLimit;
        private int dayOfPayment;

        private String compCountry = "";
        private String compProvince = "";
        private String compRegency = "";
        private String compState="";
        private String compEmail="";
        private String homePostalCode="";
        private String homeMobilePhone="";
        private String homeCity="";     
        private String postalCode="";
		private String passportNo = "";
           
	public String getContactCode(){ 
		return contactCode; 
	} 

	public void setContactCode(String contactCode){ 
		if ( contactCode == null ) {
			contactCode = ""; 
		} 
		this.contactCode = contactCode; 
	} 

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public long getParentId(){ 
		return parentId; 
	} 

	public void setParentId(long parentId){ 
		this.parentId = parentId; 
	} 

	public int getContactType(){ 
		return contactType; 
	} 

	public void setContactType(int contactType){ 
		this.contactType = contactType; 
	} 

	public Date getRegdate(){ 
		return regdate; 
	} 

	public void setRegdate(Date regdate){ 
		this.regdate = regdate; 
	} 

	public String getCompName(){ 
		return compName; 
	} 

	public void setCompName(String compName){ 
		if ( compName == null ) {
			compName = ""; 
		} 
		this.compName = compName; 
	} 

	public String getPersonName(){ 
		return personName; 
	} 

	public void setPersonName(String personName){ 
		if ( personName == null ) {
			personName = ""; 
		} 
		this.personName = personName; 
	} 

	public String getPersonLastname(){ 
		return personLastname; 
	} 

	public void setPersonLastname(String personLastname){ 
		if ( personLastname == null ) {
			personLastname = ""; 
		} 
		this.personLastname = personLastname; 
	} 

	public String getBussAddress(){ 
		return bussAddress; 
	} 

	public void setBussAddress(String bussAddress){ 
            
		if ( bussAddress == null ) {
			bussAddress = ""; 
		}
                bussAddress = bussAddress.replace("\n", " ");
                bussAddress = bussAddress.replace("\r", " ");
		this.bussAddress = bussAddress; 
	} 

	public String getTown(){ 
		return town; 
	} 

	public void setTown(String town){ 
		if ( town == null ) {
			town = ""; 
		} 
		this.town = town; 
	} 

	public String getProvince(){ 
		return province; 
	} 

	public void setProvince(String province){ 
		if ( province == null ) {
			province = ""; 
		} 
		this.province = province; 
	} 

	public String getCountry(){ 
		return country; 
	} 

	public void setCountry(String country){ 
		if ( country == null ) {
			country = ""; 
		} 
		this.country = country; 
	} 

	public String getTelpNr(){ 
		return telpNr; 
	} 

	public void setTelpNr(String telpNr){ 
		if ( telpNr == null ) {
			telpNr = ""; 
		} 
		this.telpNr = telpNr; 
	} 

	public String getTelpMobile(){ 
		return telpMobile; 
	} 

	public void setTelpMobile(String telpMobile){ 
		if ( telpMobile == null ) {
			telpMobile = ""; 
		} 
		this.telpMobile = telpMobile; 
	} 

	public String getFax(){ 
		return fax; 
	} 

	public void setFax(String fax){ 
		if ( fax == null ) {
			fax = ""; 
		} 
		this.fax = fax; 
	} 

	public String getHomeAddr(){ 
		return homeAddr; 
	} 

	public void setHomeAddr(String homeAddr){ 
		if ( homeAddr == null ) {
			homeAddr = ""; 
		} 
                homeAddr = homeAddr.replace("\n", " ");
                homeAddr = homeAddr.replace("\r", " ");
		this.homeAddr = homeAddr; 
	} 

	public String getHomeTown(){ 
		return homeTown; 
	} 

	public void setHomeTown(String homeTown){ 
		if ( homeTown == null ) {
			homeTown = ""; 
		} 
		this.homeTown = homeTown; 
	} 

	public String getHomeProvince(){ 
		return homeProvince; 
	} 

	public void setHomeProvince(String homeProvince){ 
		if ( homeProvince == null ) {
			homeProvince = ""; 
		} 
		this.homeProvince = homeProvince; 
	} 

	public String getHomeCountry(){ 
		return homeCountry; 
	} 

	public void setHomeCountry(String homeCountry){ 
		if ( homeCountry == null ) {
			homeCountry = ""; 
		} 
		this.homeCountry = homeCountry; 
	} 

	public String getHomeTelp(){ 
		return homeTelp; 
	} 

	public void setHomeTelp(String homeTelp){ 
		if ( homeTelp == null ) {
			homeTelp = ""; 
		} 
		this.homeTelp = homeTelp; 
	} 

	public String getHomeFax(){ 
		return homeFax; 
	} 

	public void setHomeFax(String homeFax){ 
		if ( homeFax == null ) {
			homeFax = ""; 
		} 
		this.homeFax = homeFax; 
	} 

	public String getNotes(){ 
		return notes; 
	} 

	public void setNotes(String notes){ 
		if ( notes == null ) {
			notes = ""; 
		} 
		this.notes = notes; 
	} 

	public String getBankAcc(){ 
		return bankAcc; 
	} 

	public void setBankAcc(String bankAcc){ 
		if ( bankAcc == null ) {
			bankAcc = ""; 
		} 
		this.bankAcc = bankAcc; 
	} 

	public String getBankAcc2(){ 
		return bankAcc2; 
	} 

	public void setBankAcc2(String bankAcc2){ 
		if ( bankAcc2 == null ) {
			bankAcc2 = ""; 
		} 
		this.bankAcc2 = bankAcc2; 
	} 

	public String getEmail(){ 
		return email; 
	} 

	public void setEmail(String email){ 
		if ( email == null ) {
			email = ""; 
		} 
		this.email = email; 
	} 

    public String getDirections(){ return directions; }

    public void setDirections(String directions){
		if ( directions == null ) {
			directions = "";
		} 
        this.directions = directions;
    }

    /**
     * Getter for property lastUpdate.
     *
     * @return Value of property lastUpdate.
     *
     */
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Setter for property lastUpdate.
     *
     * @param lastUpdate New value of property lastUpdate.
     *
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    public String getPstClassName() {
        return "com.dimata.common.entity.contact.PstContactList";
    }

    /**
     * Getter for property companyBankAcc.
     *
     * @return Value of property companyBankAcc.
     *
     */
    public String getCompanyBankAcc() {
        return companyBankAcc;
    }

    /**
     * Setter for property companyBankAcc.
     *
     * @param companyBankAcc New value of property companyBankAcc.
     *
     */
    public void setCompanyBankAcc(String companyBankAcc) {
        this.companyBankAcc = companyBankAcc;
    }

    /**
     * Getter for property positionPerson.
     *
     * @return Value of property positionPerson.
     *
     */
    public String getPositionPerson() {
        return positionPerson;
    }

    /**
     * Setter for property positionPerson.
     *
     * @param positionPerson New value of property positionPerson.
     *
     */
    public void setPositionPerson(String positionPerson) {
        this.positionPerson = positionPerson;
    }

    /**
     * Getter for property postalCodeCompany.
     *
     * @return Value of property postalCodeCompany.
     *
     */
    public String getPostalCodeCompany() {
        return postalCodeCompany;
    }

    /**
     * Setter for property postalCodeCompany.
     *
     * @param postalCodeCompany New value of property postalCodeCompany.
     *
     */
    public void setPostalCodeCompany(String postalCodeCompany) {
        this.postalCodeCompany = postalCodeCompany;
    }

    /**
     * Getter for property websiteCompany.
     *
     * @return Value of property websiteCompany.
     *
     */
    public String getWebsiteCompany() {
        return websiteCompany;
    }

    /**
     * Setter for property websiteCompany.
     *
     * @param websiteCompany New value of property websiteCompany.
     *
     */
    public void setWebsiteCompany(String websiteCompany) {
        this.websiteCompany = websiteCompany;
    }

    /**
     * Getter for property emailCompany.
     *
     * @return Value of property emailCompany.
     *
     */
    public String getEmailCompany() {
        return emailCompany;
    }

    /**
     * Setter for property emailCompany.
     *
     * @param emailCompany New value of property emailCompany.
     *
     */
    public void setEmailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
    }

    /**
     * Getter for property postalCodeHome.
     *
     * @return Value of property postalCodeHome.
     *
     */
    public String getPostalCodeHome() {
        return postalCodeHome;
    }

    /**
     * Setter for property postalCodeHome.
     *
     * @param postalCodeHome New value of property postalCodeHome.
     *
     */
    public void setPostalCodeHome(String postalCodeHome) {
        this.postalCodeHome = postalCodeHome;
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
     * @return the termOfDelivery
     */
    public int getTermOfDelivery() {
        return termOfDelivery;
    }

    /**
     * @param termOfDelivery the termOfDelivery to set
     */
    public void setTermOfDelivery(int termOfDelivery) {
        this.termOfDelivery = termOfDelivery;
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
     * @return the memberBirthDate
     */
    public Date getMemberBirthDate() {
        return memberBirthDate;
    }

    /**
     * @param memberBirthDate the memberBirthDate to set
     */
    public void setMemberBirthDate(Date memberBirthDate) {
        this.memberBirthDate = memberBirthDate;
    }

    /**
     * @return the memberSex
     */
    public int getMemberSex() {
        return memberSex;
    }

    /**
     * @param memberSex the memberSex to set
     */
    public void setMemberSex(int memberSex) {
        this.memberSex = memberSex;
    }

    /**
     * @return the memberReligionId
     */
    public long getMemberReligionId() {
        return memberReligionId;
    }

    /**
     * @param memberReligionId the memberReligionId to set
     */
    public void setMemberReligionId(long memberReligionId) {
        this.memberReligionId = memberReligionId;
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
     * @return the memberIdCardNumber
     */
    public String getMemberIdCardNumber() {
        return memberIdCardNumber;
    }

    /**
     * @param memberIdCardNumber the memberIdCardNumber to set
     */
    public void setMemberIdCardNumber(String memberIdCardNumber) {
        this.memberIdCardNumber = memberIdCardNumber;
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
     * @return the compCountry
     */
    public String getCompCountry() {
        return compCountry;
    }

    /**
     * @param compCountry the compCountry to set
     */
    public void setCompCountry(String compCountry) {
        this.compCountry = compCountry;
    }

    /**
     * @return the compProvince
     */
    public String getCompProvince() {
        return compProvince;
    }

    /**
     * @param compProvince the compProvince to set
     */
    public void setCompProvince(String compProvince) {
        this.compProvince = compProvince;
    }

    /**
     * @return the compRegency
     */
    public String getCompRegency() {
        return compRegency;
    }

    /**
     * @param compRegency the compRegency to set
     */
    public void setCompRegency(String compRegency) {
        this.compRegency = compRegency;
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
     * @return the homeMobilePhone
     */
    public String getHomeMobilePhone() {
        return homeMobilePhone;
    }

    /**
     * @param homeMobilePhone the homeMobilePhone to set
     */
    public void setHomeMobilePhone(String homeMobilePhone) {
        this.homeMobilePhone = homeMobilePhone;
    }

    /**
     * @return the homeCity
     */
    public String getHomeCity() {
        return homeCity;
    }

    /**
     * @param homeCity the homeCity to set
     */
    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        ContactList prevSup = (ContactList) prevDoc;
        //DiscountType discountType = null;
        Location location = new Location();
        try {

            if (this.getLocationId() != 0) {
                location = PstLocation.fetchExc(getLocationId());
            }

        } catch (Exception exc) {

        }

        return (prevSup == null || prevSup.getOID() == 0 || !Formater.formatDate(prevSup.getRegdate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getRegdate(), "yyyy-MM-dd"))
                ? (" Date Time : " + Formater.formatDate(this.getRegdate(), "yyyy-MM-dd") + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || !prevSup.getContactCode().equals(this.getContactCode())
                        ? ("Code : " + this.getContactCode() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getTitle().compareToIgnoreCase(this.getTitle()) != 0
                        ? (" Tittle : " + this.getTitle() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getPersonName().compareToIgnoreCase(this.getPersonName()) != 0
                        ? (" Person Name : " + this.getPersonName() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getPersonLastname().compareToIgnoreCase(this.getPersonLastname()) != 0
                        ? (" Last Name : " + this.getPersonLastname() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeCountry().compareToIgnoreCase(this.getHomeCountry()) != 0
                        ? (" Country : " + this.getHomeCountry() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeProvince().compareToIgnoreCase(this.getHomeProvince()) != 0
                        ? (" Province : " + this.getHomeProvince() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeCity().compareToIgnoreCase(this.getHomeCity()) != 0
                        ? (" City : " + this.getHomeCity() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeState().compareToIgnoreCase(this.getHomeState()) != 0
                        ? (" Area : " + this.getHomeState() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeAddr().compareToIgnoreCase(this.getHomeAddr()) != 0
                        ? (" Address : " + this.getHomeAddr() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getPostalCodeHome().compareToIgnoreCase(this.getPostalCodeHome()) != 0
                        ? (" Postal Code : " + this.getPostalCodeHome() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeTelp().compareToIgnoreCase(this.getHomeTelp()) != 0
                        ? (" No Telephone: " + this.getHomeTelp() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeMobilePhone().compareToIgnoreCase(this.getHomeMobilePhone()) != 0
                        ? (" No Handphone: " + this.getHomeMobilePhone() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeTown().compareToIgnoreCase(this.getHomeTown()) != 0
                        ? (" Birth Place : " + this.getHomeTown() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || !Formater.formatDate(prevSup.getMemberBirthDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getMemberBirthDate(), "yyyy-MM-dd"))
                        ? (" Birth Date : " + Formater.formatDate(this.getMemberBirthDate(), "yyyy-MM-dd") + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getMemberSex() != this.getMemberSex()
                        ? (" Type Member : " + this.getMemberSex() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getNationality().compareToIgnoreCase(this.getNationality()) != 0
                        ? ("Nationality: " + this.getNationality() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getMemberOccupation().compareToIgnoreCase(this.getMemberOccupation()) != 0
                        ? ("Occupation: " + this.getMemberOccupation() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getEmail().compareToIgnoreCase(this.getEmail()) != 0
                        ? ("Email 1: " + this.getEmail() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getHomeEmail().compareToIgnoreCase(this.getHomeEmail()) != 0
                        ? ("Email 2: " + this.getHomeEmail() + " ;") : "")
                + " No ID : " + this.getMemberIdCardNumber() + " ;"
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getMemberConsigmentLimit() != this.getMemberConsigmentLimit()
                        ? (" Consigmint Limit : " + this.getMemberConsigmentLimit() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getMemberCreditLimit() != this.getMemberCreditLimit()
                        ? (" Credit Limit : " + this.getMemberCreditLimit() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getTermOfDelivery() != this.getTermOfDelivery()
                        ? (" Waktu Kirim : " + this.getTermOfDelivery() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getDayOfPayment() != this.getDayOfPayment()
                        ? (" Waktu jatuh tempo : " + this.getDayOfPayment() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getCompName().compareToIgnoreCase(this.getCompName()) != 0
                        ? (" Company Name : " + this.getCompName() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getCompCountry().compareToIgnoreCase(this.getCompCountry()) != 0
                        ? (" Country : " + this.getCompCountry() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getCompProvince().compareToIgnoreCase(this.getCompProvince()) != 0
                        ? (" Province : " + this.getCompProvince() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getCompRegency().compareToIgnoreCase(this.getCompRegency()) != 0
                        ? (" Regency : " + this.getCompRegency() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getCompState().compareToIgnoreCase(this.getCompState()) != 0
                        ? (" Area : " + this.getCompState() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getBussAddress().compareToIgnoreCase(this.getBussAddress()) != 0
                        ? (" Company Address : " + this.getBussAddress() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getLocationId() != this.getLocationId()
                        ? (" Location : " + this.getLocationId() + " ;") : "")
                // problem email null
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getCompEmail() == null || prevSup.getCompEmail().compareToIgnoreCase(this.getCompEmail()) != 0
                        ? (" Company Email : " + this.getCompEmail() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getTelpNr().compareToIgnoreCase(this.getTelpNr()) != 0
                        ? (" Company Telp : " + this.getTelpNr() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getFax().compareToIgnoreCase(this.getFax()) != 0
                        ? (" Company Fax : " + this.getFax() + " ;") : "")
                + (prevSup == null || prevSup.getOID() == 0 || prevSup.getPostalCode().compareToIgnoreCase(this.getPostalCode()) != 0
                        ? (" Postal Code : " + this.getPostalCode() + " ;") : "");

//        }
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

	/**
	 * @return the passportNo
	 */
	public String getPassportNo() {
		return passportNo;
	}

	/**
	 * @param passportNo the passportNo to set
	 */
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
    
}
