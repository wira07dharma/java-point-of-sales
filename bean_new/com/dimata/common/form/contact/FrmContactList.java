/* 
 * Form Name  	:  FrmContactList.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya 
 * @version  	: 01 
 */
 
/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.form.contact;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.common.entity.contact.*;

public class FrmContactList extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private ContactList contactList;

	public static final String FRM_NAME_CONTACTLIST		=  "FRM_NAME_CONTACTLIST" ;

        public static final int FRM_FIELD_CONTACT_ID			=  0 ;
        public static final int FRM_FIELD_CONTACT_CODE			=  1 ;
        public static final int FRM_FIELD_EMPLOYEE_ID			=  2 ;
        public static final int FRM_FIELD_PARENT_ID			=  3 ;
        public static final int FRM_FIELD_CONTACT_TYPE			=  4 ;
        public static final int FRM_FIELD_REGDATE			=  5 ;
        public static final int FRM_FIELD_COMP_NAME			=  6 ;
        public static final int FRM_FIELD_PERSON_NAME			=  7 ;
        public static final int FRM_FIELD_PERSON_LASTNAME			=  8 ;
        public static final int FRM_FIELD_BUSS_ADDRESS			=  9 ;
        public static final int FRM_FIELD_TOWN			=  10 ;
        public static final int FRM_FIELD_PROVINCE			=  11 ;
        public static final int FRM_FIELD_COUNTRY			=  12 ;
        public static final int FRM_FIELD_TELP_NR			=  13 ;
        public static final int FRM_FIELD_TELP_MOBILE			=  14 ;
        public static final int FRM_FIELD_FAX			=  15 ;
        public static final int FRM_FIELD_HOME_ADDR			=  16 ;
        public static final int FRM_FIELD_HOME_TOWN			=  17 ;
        public static final int FRM_FIELD_HOME_PROVINCE			=  18 ;
        public static final int FRM_FIELD_HOME_COUNTRY			=  19 ;
        public static final int FRM_FIELD_HOME_TELP			=  20 ;
        public static final int FRM_FIELD_HOME_FAX			=  21 ;
        public static final int FRM_FIELD_NOTES			=  22 ;
        public static final int FRM_FIELD_BANK_ACC			=  23 ;
        public static final int FRM_FIELD_BANK_ACC2			=  24 ;
        public static final int FRM_FIELD_EMAIL			=  25 ;
        public static final int FRM_FIELD_WEBSITE			=  26 ;
        public static final int FRM_FIELD_LOCATION_ID			=  27 ;
        public static final int FRM_FIELD_TERM_OF_DELIVERY			=  28 ;
        
        //add opie-eyek 201407
        public static final int FRM_FIELD_TITLE			=  29 ;
        public static final int FRM_FIELD_HOME_STATE			=  30;
        public static final int FRM_FIELD_HOME_POSTALCODE			=  31;
        public static final int FRM_FIELD_MEMBER_BIRTH_DATE			=  32 ;
        public static final int FRM_FIELD_MEMBER_SEX			=  33 ;
        public static final int FRM_FIELD_MEMBER_RELIGION_ID			=  34 ;
        public static final int FRM_FIELD_NATIONALITY			=  35 ;
        public static final int FRM_FIELD_MEMBER_OCCUPATION			=  36;
        public static final int FRM_FIELD_HOME_EMAIL			=  37;
        public static final int FRM_FIELD_MEMBER_ID_CARD_NUMBER			=  38;
        public static final int FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT			=  39;
        public static final int FRM_FIELD_MEMBER_CONSIGMENT_LIMIT			=  40;
        public static final int FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT			=  41;
        public static final int FRM_FIELD_MEMBER_CREDIT_LIMIT			=42;
        public static final int FRM_FIELD_DAY_OF_PAYMENT			=43;
        public static final int FRM_FIELD_COMP_STATE			=44;
        public static final int FRM_FIELD_COMP_EMAIL			=45;
        public static final int FRM_FIELD_POSTAL_CODE			=46;

    public static String[] fieldNames = {
        "FRM_FIELD_CONTACT_ID",
        "FRM_FIELD_CONTACT_CODE",
        "FRM_FIELD_EMPLOYEE_ID",
        "FRM_FIELD_PARENT_ID",
        "FRM_FIELD_CONTACT_TYPE",
        "FRM_FIELD_REGDATE",
        "FRM_FIELD_COMP_NAME",
        "FRM_FIELD_PERSON_NAME",
        "FRM_FIELD_PERSON_LASTNAME",
        "FRM_FIELD_BUSS_ADDRESS",
        "FRM_FIELD_TOWN",
        "FRM_FIELD_PROVINCE",
        "FRM_FIELD_COUNTRY",
        "FRM_FIELD_TELP_NR",
        "FRM_FIELD_TELP_MOBILE",
        "FRM_FIELD_FAX",
        "FRM_FIELD_HOME_ADDR",
        "FRM_FIELD_HOME_TOWN",
        "FRM_FIELD_HOME_PROVINCE",
        "FRM_FIELD_HOME_COUNTRY",
        "FRM_FIELD_HOME_TELP",
        "FRM_FIELD_HOME_FAX",
        "FRM_FIELD_NOTES",
        "FRM_FIELD_BANK_ACC",
        "FRM_FIELD_BANK_ACC2",
        "FRM_FIELD_EMAIL",
        "FRM_FIELD_WEBSITE","FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_TERM_OF_DELIVERY",
            
        //update opie-eyek 20140407   
        "FRM_FIELD_TITLE",//1
        "FRM_FIELD_HOME_STATE",//2
        "FRM_FIELD_HOME_POSTALCODE",//3
        "FRM_FIELD_MEMBER_BIRTH_DATE",//4
        "FRM_FIELD_MEMBER_SEX",//5
        "FRM_FIELD_MEMBER_RELIGION_ID",//6
        "FRM_FIELD_NATIONALITY",//7
        "FRM_FIELD_MEMBER_OCCUPATION",//8
        "FRM_FIELD_HOME_EMAIL",//9
        "FRM_FIELD_MEMBER_ID_CARD_NUMBER",//10
        "FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT",//11
        "FRM_FIELD_MEMBER_CONSIGMENT_LIMIT",//12
        "FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT",//13
        "FRM_FIELD_MEMBER_CREDIT_LIMIT",//14
        "FRM_FIELD_DAY_OF_PAYMENT",//15
        "FRM_FIELD_COMP_STATE",//16
        "FRM_FIELD_COMP_EMAIL",//17
        "FRM_FIELD_POSTAL_CODE"//18
    } ;

	public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        
        //update opie-eyek 20140407
        TYPE_STRING,//1
        TYPE_STRING,//2
        TYPE_STRING,//3
        TYPE_DATE,//4
        TYPE_INT,//5
        TYPE_STRING,//6
        TYPE_STRING,//7
        TYPE_STRING,//8
        TYPE_STRING,//9
        TYPE_STRING,//10
        TYPE_LONG,//11
        TYPE_FLOAT,//12
        TYPE_LONG,//13
        TYPE_FLOAT,//14
        TYPE_STRING,//15
        TYPE_STRING,//16
        TYPE_STRING,//17
        TYPE_STRING//18
        
        
    } ;

	public FrmContactList(){
	}
	public FrmContactList(ContactList contactList){
		this.contactList = contactList;
	}

	public FrmContactList(HttpServletRequest request, ContactList contactList){
		super(new FrmContactList(contactList), request);
		this.contactList = contactList;
	}

	public String getFormName() { return FRM_NAME_CONTACTLIST; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public ContactList getEntityObject(){ return contactList; }

	public void requestEntityObject(ContactList contactList) {
		try{
			this.requestParam();
			contactList.setContactCode(getString(FRM_FIELD_CONTACT_CODE));
			contactList.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
			contactList.setParentId(getLong(FRM_FIELD_PARENT_ID));
			contactList.setContactType(getInt(FRM_FIELD_CONTACT_TYPE));
			contactList.setRegdate(getDate(FRM_FIELD_REGDATE));
			contactList.setCompName(getString(FRM_FIELD_COMP_NAME));
			contactList.setPersonName(getString(FRM_FIELD_PERSON_NAME));
			contactList.setPersonLastname(getString(FRM_FIELD_PERSON_LASTNAME));
			contactList.setBussAddress(getString(FRM_FIELD_BUSS_ADDRESS));
			contactList.setTown(getString(FRM_FIELD_TOWN));
			contactList.setProvince(getString(FRM_FIELD_PROVINCE));
			contactList.setCountry(getString(FRM_FIELD_COUNTRY));
			contactList.setTelpNr(getString(FRM_FIELD_TELP_NR));
			contactList.setTelpMobile(getString(FRM_FIELD_TELP_MOBILE));
			contactList.setFax(getString(FRM_FIELD_FAX));
			contactList.setHomeAddr(getString(FRM_FIELD_HOME_ADDR));
			contactList.setHomeTown(getString(FRM_FIELD_HOME_TOWN));
			contactList.setHomeProvince(getString(FRM_FIELD_HOME_PROVINCE));
			contactList.setHomeCountry(getString(FRM_FIELD_HOME_COUNTRY));
			contactList.setHomeTelp(getString(FRM_FIELD_HOME_TELP));
			contactList.setHomeFax(getString(FRM_FIELD_HOME_FAX));
			contactList.setNotes(getString(FRM_FIELD_NOTES));
			contactList.setBankAcc(getString(FRM_FIELD_BANK_ACC));
			contactList.setBankAcc2(getString(FRM_FIELD_BANK_ACC2));
			contactList.setEmail(getString(FRM_FIELD_EMAIL));
                        contactList.setWebsiteCompany(getString(FRM_FIELD_WEBSITE));
                        contactList.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                        contactList.setTermOfDelivery(getInt(FRM_FIELD_TERM_OF_DELIVERY));
                        
                        contactList.setTitle(getString(FRM_FIELD_TITLE));//1
                        contactList.setHomeState(getString(FRM_FIELD_HOME_STATE));//2
                        contactList.setHomePostalCode(getString(FRM_FIELD_HOME_POSTALCODE));//3
                        contactList.setMemberBirthDate(getDate(FRM_FIELD_MEMBER_BIRTH_DATE));//4
                        contactList.setMemberSex(getInt(FRM_FIELD_MEMBER_SEX));//5
                        contactList.setMemberReligionId(getLong(FRM_FIELD_MEMBER_RELIGION_ID));//6
                        contactList.setNationality(getString(FRM_FIELD_NATIONALITY));//7
                        contactList.setMemberOccupation(getString(FRM_FIELD_MEMBER_OCCUPATION));//8
                        contactList.setHomeEmail(getString(FRM_FIELD_HOME_EMAIL));//9
                        contactList.setMemberIdCardNumber(getString(FRM_FIELD_MEMBER_ID_CARD_NUMBER)); //10
                        contactList.setCurrencyTypeIdMemberConsigmentLimit(getLong(FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT)); //11
                        contactList.setMemberConsigmentLimit(getDouble(FRM_FIELD_MEMBER_CONSIGMENT_LIMIT)); //12
                        contactList.setCurrencyTypeIdMemberCreditLimit(getLong(FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT));//13
                        contactList.setMemberCreditLimit(getDouble(FRM_FIELD_MEMBER_CREDIT_LIMIT));//14
                        contactList.setDayOfPayment(getInt(FRM_FIELD_DAY_OF_PAYMENT));//15
                        contactList.setCompState(getString(FRM_FIELD_COMP_STATE));//16
                        contactList.setCompEmail(getString(FRM_FIELD_COMP_EMAIL));//17
                        contactList.setPostalCode(getString(FRM_FIELD_POSTAL_CODE));//18
                         
                        /*
                        private String homePostalCode="";//3
                        private Date memberBirthDate;//4
                        private int memberSex;//5
                        private String nationality="";//6
                        private String memberOccupation="";//7
                        private String homeEmail="";//8
                        private String memberIdCardNumber = "";//9
                        private long CurrencyTypeIdMemberConsigmentLimit = 0;//10
                        private double memberConsigmentLimit;//11
                        private long CurrencyTypeIdMemberCreditLimit = 0;//12
                        private double memberCreditLimit;//13
                        private int dayOfPayment=0;//14
                        private String compCountry = "";//15
                        private String compState="";//16
                        private String compEmail="";//17
                        private String postalCode="";//18*/
                        
                        
                        
        }catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
