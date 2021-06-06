 

/* Created on 	:  [date] [time] AM/PM 

 * 

 * @author  	:  [authorName] 

 * @version  	:  [version] 

 */



/*******************************************************************

 * Class Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 		: [output ...] 

 *******************************************************************/



package com.dimata.hanoman.entity.masterdata; 

 

/* package java */ 

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.DiscountType;
import com.dimata.common.entity.payment.PstDiscountType;
import com.dimata.posbo.entity.masterdata.MemberGroup;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import java.util.Date;



/* package qdep */

import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;



public class Contact extends Entity { 



	private String contactCode = "";

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

        private long employeeID;

        //ad by fitra
        private long memberGroupId;
    
        private String title = "";

          private String homeState = "";
          
          private String homePostalCode="";
          
          private Date memberBirthDate;
          
          private int memberSex;
          
          private String compEmail="";
          
          private String postalCode="";
          
          private String homeEmail="";
          
          private String memberIdCardNumber = "";
          
          private long CurrencyTypeIdMemberConsigmentLimit = 0;
          
          private double memberConsigmentLimit;
          
           private long CurrencyTypeIdMemberCreditLimit = 0;
           
           private double memberCreditLimit;
           
              private int dayOfPayment=0;
              
               private String compState="";

           private String nationality="";
           
           private String memberOccupation="";
          
          private long memberReligionId;
    /** Holds value of property parentId. */

    private long parentId;

        //add by fitra
    private String userId = "";
    private String password = "";
    
     private String compCountry = "";
    private String compProvince = "";
     private String compRegency = "";

     private String homeMobilePhone = "";
      private String birthPlace = "";
    

	public String getContactCode(){ 

		return contactCode; 

	} 



	public void setContactCode(String contactCode){ 

		if ( contactCode == null ) {

			contactCode = ""; 

		} 

		this.contactCode = contactCode; 

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



    public long getEmployeeID(){ return employeeID; }



    public void setEmployeeID(long employeeID){ this.employeeID = employeeID; }

    

    /** Getter for property parentId.

     * @return Value of property parentId.

     *

     */

    public long getParentId() {

        return this.parentId;

    }

    

    /** Setter for property parentId.

     * @param parentId New value of property parentId.

     *

     */

    public void setParentId(long parentId) {

        this.parentId = parentId;

    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the memberGroupId
     */
    public long getMemberGroupId() {
        return memberGroupId;
    }

    /**
     * @param memberGroupId the memberGroupId to set
     */
    public void setMemberGroupId(long memberGroupId) {
        this.memberGroupId = memberGroupId;
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
     * @param CurrencyTypeIdMemberConsigmentLimit the CurrencyTypeIdMemberConsigmentLimit to set
     */
    public void setCurrencyTypeIdMemberConsigmentLimit(long CurrencyTypeIdMemberConsigmentLimit) {
        this.CurrencyTypeIdMemberConsigmentLimit = CurrencyTypeIdMemberConsigmentLimit;
    }

    /**
     * @return the memberConsigmentLimit
     */


    /**
     * @return the CurrencyTypeIdMemberCreditLimit
     */
    public long getCurrencyTypeIdMemberCreditLimit() {
        return CurrencyTypeIdMemberCreditLimit;
    }

    /**
     * @param CurrencyTypeIdMemberCreditLimit the CurrencyTypeIdMemberCreditLimit to set
     */
    public void setCurrencyTypeIdMemberCreditLimit(long CurrencyTypeIdMemberCreditLimit) {
        this.CurrencyTypeIdMemberCreditLimit = CurrencyTypeIdMemberCreditLimit;
    }

    /**
     * @return the memberCreditLimit
     */
   

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
     * @return the postalCode
     */
    

    /**
     * @param postalCode the postalCode to set
     */
   

    /**
     * @param memberConsigmentLimit the memberConsigmentLimit to set
     */
   

    /**
     * @return the memberConsigmentLimit
     */
    

    /**
     * @return the memberCreditLimit
     */
   

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
    

    /**
     * @return the memberCreditLimit
     */
    

    /**
     * @return the tittle
     */
    
    // update by Fitra Anggara
    
  public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        Contact prevPo = (Contact)prevDoc;
        //DiscountType discountType = null;
        MemberGroup memberGroup = new MemberGroup();
        try{


          if(this.getMemberGroupId()!=0 )
          {
            memberGroup = PstMemberGroup.fetchExc(getMemberGroupId());
}

        }catch(Exception exc){
            
        }

        return  
                 (prevPo == null ||  prevPo.getOID()==0 || !Formater.formatDate(prevPo.getRegdate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getRegdate(), "yyyy-MM-dd"))?
                (" Date Time : " + Formater.formatDate(this.getRegdate(), "yyyy-MM-dd") + " ;") : "") +

                
                
                (prevPo == null ||  prevPo.getOID()==0 ||  !prevPo.getContactCode().equals(this.getContactCode()) ?
                ("Code : "+ this.getContactCode() +" ;" ) : "" ) +

                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getMemberGroupId()!=this.getMemberGroupId()?
                (" Type Member : " + memberGroup.getName() +" ;" ) : "") +
                

                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getTitle().compareToIgnoreCase(this.getTitle())!=0 ?
                (" Tittle : "+ this.getTitle() +" ;" ) : "") +
                
                
                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getPersonName().compareToIgnoreCase(this.getPersonName())!=0 ?
                (" Person Name : " + this.getPersonName() +" ;") : "")+
                
                 (prevPo == null ||  prevPo.getOID()==0 || prevPo.getPersonLastname().compareToIgnoreCase(this.getPersonLastname())!=0 ?
                (" Last Name : " + this.getPersonLastname()+" ;") : "")+
                
               
                
                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getUserId().compareToIgnoreCase(this.getUserId())!=0 ?
                (" Username : " + this.getUserId()+" ;") : "")+

                 (prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeCountry().compareToIgnoreCase(this.getHomeCountry())!=0 ?
                (" Country : " + this.getHomeCountry()+" ;") : "")+
                
                 (prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeProvince().compareToIgnoreCase(this.getHomeProvince())!=0 ?
                (" Province : " + this.getHomeProvince()+" ;") : "")+
                
                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeTown().compareToIgnoreCase(this.getHomeTown())!=0 ?
                (" City : " + this.getHomeTown()+" ;") : "")+ 
                
                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeState().compareToIgnoreCase(this.getHomeState())!=0 ?
                (" Area : " + this.getHomeState()+" ;") : "")
              
           
              
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeAddr().compareToIgnoreCase(this.getHomeAddr())!=0 ?
                (" Address : " + this.getHomeAddr()+" ;") : "")
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomePostalCode().compareToIgnoreCase(this.getHomePostalCode())!=0 ?
                (" Postal Code : " + this.getHomePostalCode()+" ;") : "")
                
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeTelp().compareToIgnoreCase(this.getHomeTelp())!=0 ?
                (" No Telephone: " + this.getHomeTelp()+" ;") : "")
                
                
                 +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeMobilePhone().compareToIgnoreCase(this.getHomeMobilePhone())!=0 ?
                (" No Handphone: " + this.getHomeMobilePhone()+" ;") : "")
                
                
                + (prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeTown().compareToIgnoreCase(this.getHomeTown())!=0 ?
                (" Birth Place : " + this.getHomeTown()+" ;") : "")
                
                + (prevPo == null ||  prevPo.getOID()==0 || !Formater.formatDate(prevPo.getMemberBirthDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getMemberBirthDate(), "yyyy-MM-dd"))?
                (" Birth Date : " + Formater.formatDate(this.getMemberBirthDate(), "yyyy-MM-dd") + " ;") : "") 
                
                 + (prevPo == null ||  prevPo.getOID()==0 || prevPo.getMemberSex()!=this.getMemberSex()?
                (" Type Member : " + this.getMemberSex() +" ;" ) : "") 
                
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getNationality().compareToIgnoreCase(this.getNationality())!=0 ?
                ("Nationality: " + this.getNationality()+" ;") : "")
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getMemberOccupation().compareToIgnoreCase(this.getMemberOccupation())!=0 ?
                ("Occupation: " + this.getMemberOccupation()+" ;") : "")
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getEmail().compareToIgnoreCase(this.getEmail())!=0 ?
                ("Email 1: " + this.getEmail()+" ;") : "")
                
                +(prevPo == null ||  prevPo.getOID()==0 || prevPo.getHomeEmail().compareToIgnoreCase(this.getHomeEmail())!=0 ?
                ("Email 2: " + this.getHomeEmail()+" ;") : "")
          
                 +" No ID : " + this.getMemberIdCardNumber()+" ;"
                 + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getMemberConsigmentLimit() != this.getMemberConsigmentLimit() ?
                (" Consigmint Limit : " + this.getMemberConsigmentLimit() +" ;") : "")  
                
                 + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getMemberCreditLimit() != this.getMemberCreditLimit() ?
                (" Credit Limit : " + this.getMemberCreditLimit() +" ;") : "")  
                
                   + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getDayOfPayment() != this.getDayOfPayment() ?
                (" Waktu jatuh tempo : " + this.getDayOfPayment() +" ;") : "")  
                
                + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getCompName().compareToIgnoreCase(this.getCompName()) != 0 ?
                (" Company Name : " + this.getCompName() +" ;") : "")  
                
                + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getCompCountry().compareToIgnoreCase(this.getCompCountry()) !=0 ?
                (" Country : " + this.getCompCountry() +" ;") : "")
                
                    + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getCompProvince().compareToIgnoreCase(this.getCompProvince()) !=0 ?
                (" Province : " + this.getCompProvince() +" ;") : "")
                
                + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getCompRegency().compareToIgnoreCase(this.getCompRegency()) !=0 ?
                (" Regency : " + this.getCompRegency() +" ;") : "")
                
                 + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getCompState().compareToIgnoreCase(this.getCompState()) !=0 ?
                (" Area : " + this.getCompState() +" ;") : "")
                
                + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getBussAddress().compareToIgnoreCase(this.getBussAddress()) !=0 ?
                (" Company Address : " + this.getBussAddress() +" ;") : "")
                // problem email null
                + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getCompEmail()==null || prevPo.getCompEmail().compareToIgnoreCase(this.getCompEmail()) !=0 ?
                (" Company Email : " + this.getCompEmail() +" ;") : "")
                
                 + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getTelpNr().compareToIgnoreCase(this.getTelpNr()) !=0 ?
                (" Company Telp : " + this.getTelpNr() +" ;") : "")
                
                 + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getFax().compareToIgnoreCase(this.getFax()) !=0 ?
                (" Company Fax : " + this.getFax() +" ;") : "")
                
                
                 + (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getPostalCode().compareToIgnoreCase(this.getPostalCode()) !=0 ?
                (" Postal Code : " + this.getPostalCode() +" ;") : "");

               
//        }
    }    

    /**
     * @return the birthPlace
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * @param birthPlace the birthPlace to set
     */
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }


}

