/* 

 * Form Name  	:  FrmContact.java 

 * Created on 	:  [date] [time] AM/PM 

 * 

 * @author  	:  [authorName] 

 * @version  	:  [version] 

 */



/*******************************************************************

 * Class Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 		: [output ...] 

 *******************************************************************/



package com.dimata.hanoman.form.masterdata;



/* java package */ 

import java.io.*; 

import java.util.*; 

import javax.servlet.*;

import javax.servlet.http.*; 

/* qdep package */ 

import com.dimata.qdep.form.*;

/* project package */

import com.dimata.hanoman.entity.masterdata.*;import com.dimata.harisma.entity.masterdata.*;



public class FrmContact extends FRMHandler implements I_FRMInterface, I_FRMType 

{

	private Contact contact;



	public static final String FRM_NAME_CONTACT		=  "FRM_NAME_CONTACT" ;



	public static final int FRM_FIELD_CONTACT_ID			=  0 ;

	public static final int FRM_FIELD_CONTACT_CODE			=  1 ;

	public static final int FRM_FIELD_CONTACT_TYPE			=  2 ;

	public static final int FRM_FIELD_REGDATE			=  3 ;

	public static final int FRM_FIELD_COMP_NAME			=  4 ;

	public static final int FRM_FIELD_PERSON_NAME			=  5 ;

	public static final int FRM_FIELD_PERSON_LASTNAME		=  6 ;

	public static final int FRM_FIELD_BUSS_ADDRESS			=  7 ;

	public static final int FRM_FIELD_TOWN			        =  8 ;

	public static final int FRM_FIELD_PROVINCE			=  9 ;

	public static final int FRM_FIELD_COUNTRY			=  10 ;

	public static final int FRM_FIELD_TELP_NR			=  11 ;

	public static final int FRM_FIELD_TELP_MOBILE			=  12 ;

	public static final int FRM_FIELD_FAX			        =  13 ;

	public static final int FRM_FIELD_HOME_ADDR			=  14 ;

	public static final int FRM_FIELD_HOME_TOWN			=  15 ;

	public static final int FRM_FIELD_HOME_PROVINCE			=  16 ;

	public static final int FRM_FIELD_HOME_COUNTRY			=  17 ;

	public static final int FRM_FIELD_HOME_TELP			=  18 ;

	public static final int FRM_FIELD_HOME_FAX			=  19 ;

	public static final int FRM_FIELD_NOTES			        =  20 ;

	public static final int FRM_FIELD_BANK_ACC			=  21 ;

	public static final int FRM_FIELD_BANK_ACC2			=  22 ;

	public static final int FRM_FIELD_EMAIL			=  23 ;

        

        public static final int FRM_FIELD_PARENT_ID		=  24 ;

        public static final int FRM_FIELD_USER_ID              = 25;
        
        public static final int FRM_FIELD_PASSWORD              = 26;
        
        public static final int FRM_FIELD_RE_PASSWORD  = 27;
        
        public static final int FRM_FIELD_MEMBER_GROUP_ID		=  28 ;

        // add by fitra 26-02-2014
        
        public static final int FRM_FIELD_COMP_COUNTRY  = 29;

        public static final int FRM_FIELD_COMP_PROVINCE  = 30;

        public static final int FRM_FIELD_COMP_REGENCY  = 31;

        public static final int FRM_FIELD_HOME_MOBILE_PHONE = 32;

        public static final int FRM_FIELD_TITLE = 33;

        public static final int FRM_FIELD_HOME_STATE=34;

        public static final int FRM_FIELD_HOME_POSTALCODE=35;

        public static final int FRM_FIELD_MEMBER_BIRTH_DATE = 36;

        public static final int FRM_FIELD_MEMBER_SEX = 37;

        public static final int FRM_FIELD_MEMBER_RELIGION_ID = 38;

        public static final int FRM_FIELD_NATIONALITY=39;

        public static final int FRM_FIELD_MEMBER_OCCUPATION=40;

        public static final int FRM_FIELD_HOME_EMAIL=41;

        public static final int FRM_FIELD_MEMBER_ID_CARD_NUMBER = 42;

        public static final int FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT = 43;

        public static final int FRM_FIELD_MEMBER_CONSIGMENT_LIMIT = 44;

        public static final int FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT = 45;

        public static final int FRM_FIELD_MEMBER_CREDIT_LIMIT = 46;

        public static final int FRM_FIELD_DAY_OF_PAYMENT = 47;

        public static final int FRM_FIELD_COMP_STATE=48;

        public static final int FRM_FIELD_COMP_EMAIL=49;

        public static final int FRM_FIELD_POSTAL_CODE = 50;

        public static final int FRM_FIELD_BIRTH_PLACE = 51;


	public static String[] fieldNames = {

		"FRM_FIELD_CONTACT_ID",  //0
                
                "FRM_FIELD_CONTACT_CODE",//1

		"FRM_FIELD_CONTACT_TYPE",  //2
                
                "FRM_FIELD_REGDATE",//3

		"FRM_FIELD_COMP_NAME", //4 
                
                "FRM_FIELD_PERSON_NAME",//5

		"FRM_FIELD_PERSON_LASTNAME",  //6
                
                "FRM_FIELD_BUSS_ADDRESS",//7

		"FRM_FIELD_TOWN",  //8
                
                "FRM_FIELD_PROVINCE",//9

		"FRM_FIELD_COUNTRY",  //10
                
                "FRM_FIELD_TELP_NR",//11

		"FRM_FIELD_TELP_MOBILE",//12  
                
                "FRM_FIELD_FAX",//13

		"FRM_FIELD_HOME_ADDR",  //14
                
                "FRM_FIELD_HOME_TOWN",//15

		"FRM_FIELD_HOME_PROVINCE",//16  
                
                "FRM_FIELD_HOME_COUNTRY", //17

		"FRM_FIELD_HOME_TELP",  //18
                
                "FRM_FIELD_HOME_FAX",//19

		"FRM_FIELD_NOTES",  //20
                
                "FRM_FIELD_BANK_ACC",//21

		"FRM_FIELD_BANK_ACC2",  //22
                
                "FRM_FIELD_EMAIL",//23

                "FRM_FIELD_PARENT_ID",//24 
                
                "FRM_FIELD_USER_ID",//25
                
                "FRM_FIELD_PASSWORD",//26
                
                "FRM_FIELD_RE_PASSWORD",//27

                "FRM_FIELD_MEMBER_GROUP_ID",//28
                
                "FRM_FIELD_COMP_COUNTRY", //29   // add by fitra 26-02-2014
                
                "FRM_FIELD_COMP_PROVINCE",//30
                
                "FRM_FIELD_COMP_REGENCY",//31

                "FRM_FIELD_HOME_MOBILE_PHONE",//32
                
                "FRM_FIELD_TITLE",//33

                "FRM_FIELD_HOME_STATE",//34
                
                "FRM_FIELD_HOME_POSTALCODE",//35
                
                "FRM_FIELD_MEMBER_BIRTH_DATE", //36
                
                "FRM_FIELD_MEMBER_SEX",//37
                
                "FRM_FIELD_MEMBER_RELIGION_ID", //38
                
                "FRM_FIELD_NATIONALITY",//39
                
                "FRM_FIELD_MEMBER_OCCUPATION", //40
                
                "FRM_FIELD_HOME_EMAIL",//41
                
                "FRM_FIELD_MEMBER_ID_CARD_NUMBER",//42
                
                "FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT",//43
                
                "FRM_FIELD_MEMBER_CONSIGMENT_LIMIT",//44
                
                "FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT",//45
                
                "FRM_FIELD_MEMBER_CREDIT_LIMIT", //46
                
                "FRM_FIELD_DAY_OF_PAYMENT",//47
                
                "FRM_FIELD_COMP_STATE", //48
                
                "FRM_FIELD_COMP_EMAIL",//49
                
                "FRM_FIELD_POSTAL_CODE",//50
                
                "FRM_FIELD_BIRTH_PLACE"//51

	} ;


//        public static String[] fieldNamesUser = {
//
//		"FRM_FIELD_CONTACT_ID",  "CONTACT_CODE",//1
//
//		"CONTACT_TYPE",  "REGDATE",//2
//
//		"NAMA_PERUSAHAAN",  "NAME",//3
//
//		"LASTNAME",  "ALAMAT",//4
//
//		"TOWN",  "PROVINCE",//5
//
//		"COUNTRY",  "TELP_NR",//6
//
//		"TELP_MOBILE",  "FAX",//7
//
//		"HOME_ADDR",  "HOME_TOWN",//8
//
//		"HOME_PROVINCE",  "HOME_COUNTRY", //9
//
//		"HOME_TELP",  "HOME_FAX",//10
//
//		"NOTES",  "BANK_ACC",//11
//
//		"BANK_ACC2",  "EMAIL",//12
//
//                "PARENT_ID", "USER_ID",//13
//                
//                "PASSWORD","RE_PASSWORD",//14
//
//                "MEMBER_GROUP_ID","COMP_COUNTRY", //15   // add by fitra 26-02-2014
//                
//                "COMP_PROVINCE","COMP_REGENCY",//16
//
//                "HOME_MOBILE_PHONE","TITLE",//17
//                
//                "HOME_STATE","HOME_POSTALCODE",//18
//                
//                "MEMBER_BIRTH_DATE", "MEMBER_SEX",//19
//                
//                "MEMBER_RELIGION_ID", "AREA",//20
//                
//                "MEMBER_OCCUPATION", "HOME_EMAIL",//21
//                
//                "MEMBER_ID_CARD_NUMBER","CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT",//22
//                
//                "MEMBER_CONSIGMENT_LIMIT","CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT",//23
//                
//                "CREDIT_LIMIT", "DAY_OF_PAYMENT",//24
//                
//                "COMP_STATE", "COMP_EMAIL",//25
//                
//                "POSTAL_CODE",
//                
//                "TEMPAT_LAHIR"//
//
//	} ;


        

	public static int[] fieldTypes = {

		TYPE_LONG,  //0
                
                TYPE_STRING + ENTRY_REQUIRED,//1

		TYPE_INT,  //2
                
                TYPE_DATE,//3

		TYPE_STRING, //4 
                
                TYPE_STRING + ENTRY_REQUIRED,//5

		TYPE_STRING,//6  
                
                TYPE_STRING,//7

		TYPE_STRING,//8
                
                TYPE_STRING,//9

		TYPE_STRING,//10  
                
                TYPE_STRING,//11

		TYPE_STRING,//12  
                
                TYPE_STRING,//13

		TYPE_STRING,//14
                
                TYPE_STRING + ENTRY_REQUIRED,//15

		TYPE_STRING,//16
                
                TYPE_STRING,//17

		TYPE_STRING,//18  
                
                TYPE_STRING,//19

		TYPE_STRING,//20  
                
                TYPE_STRING,//21

		TYPE_STRING,  //22
                
                TYPE_STRING,//23

                TYPE_LONG,//24
                
                TYPE_STRING,//25
                
                TYPE_STRING,//26
                
                TYPE_STRING,//27

                TYPE_STRING,//28    
                
                TYPE_STRING,//29     // add by fitra 26-02-2014
                
                TYPE_STRING,//30    
                
                TYPE_STRING,//31
                
                TYPE_STRING, //32
                
                TYPE_STRING,// 33
                
                TYPE_STRING,   //34
                
                TYPE_STRING,//35
                
                TYPE_DATE, //36
                
                TYPE_INT,//37
                
                TYPE_LONG,//38
                
                TYPE_STRING,//39
                
                TYPE_STRING, //40
                
                TYPE_STRING,//41
                
                TYPE_STRING, //42
                
                TYPE_LONG,//43
                
                TYPE_FLOAT, //43
                
                TYPE_LONG,//44
                
                TYPE_FLOAT, //45
                
                TYPE_INT,//46
                
                TYPE_STRING, //47
                
                TYPE_STRING,//48
                
                TYPE_STRING,//49
                
                TYPE_STRING,//50
                
                TYPE_DATE//51
	} ;



	public FrmContact(){

	}

	public FrmContact(Contact contact){

		this.contact = contact;

	}



	public FrmContact(HttpServletRequest request, Contact contact){

		super(new FrmContact(contact), request);

		this.contact = contact;

	}



	public String getFormName() { return FRM_NAME_CONTACT; } 



	public int[] getFieldTypes() { return fieldTypes; }



	public String[] getFieldNames() { return fieldNames; } 



	public int getFieldSize() { return fieldNames.length; } 



	public Contact getEntityObject(){ return contact; }



	public void requestEntityObject(Contact contact) {

		try{

			this.requestParam();

			contact.setContactCode(getString(FRM_FIELD_CONTACT_CODE));

                        //System.out.println("........Contact_Code....."+contact.getContactCode());

			contact.setContactType(getInt(FRM_FIELD_CONTACT_TYPE));

			contact.setRegdate(getDate(FRM_FIELD_REGDATE));

			contact.setCompName(getString(FRM_FIELD_COMP_NAME));

			contact.setPersonName(getString(FRM_FIELD_PERSON_NAME));

			contact.setPersonLastname(getString(FRM_FIELD_PERSON_LASTNAME));

			contact.setBussAddress(getString(FRM_FIELD_BUSS_ADDRESS));

			contact.setTown(getString(FRM_FIELD_TOWN));

			contact.setProvince(getString(FRM_FIELD_PROVINCE));

			contact.setCountry(getString(FRM_FIELD_COUNTRY));

			contact.setTelpNr(getString(FRM_FIELD_TELP_NR));

			contact.setTelpMobile(getString(FRM_FIELD_TELP_MOBILE));

			contact.setFax(getString(FRM_FIELD_FAX));

			contact.setHomeAddr(getString(FRM_FIELD_HOME_ADDR));

			contact.setHomeTown(getString(FRM_FIELD_HOME_TOWN));

			contact.setHomeProvince(getString(FRM_FIELD_HOME_PROVINCE));

			contact.setHomeCountry(getString(FRM_FIELD_HOME_COUNTRY));

			contact.setHomeTelp(getString(FRM_FIELD_HOME_TELP));

			contact.setHomeFax(getString(FRM_FIELD_HOME_FAX));

			contact.setNotes(getString(FRM_FIELD_NOTES));

			contact.setBankAcc(getString(FRM_FIELD_BANK_ACC));

			contact.setBankAcc2(getString(FRM_FIELD_BANK_ACC2));

			contact.setEmail(getString(FRM_FIELD_EMAIL));

                        contact.setParentId(getLong(FRM_FIELD_PARENT_ID));
                        
                        contact.setUserId(getString(FRM_FIELD_USER_ID));
                        
                        contact.setPassword(getString(FRM_FIELD_PASSWORD));
                        
                        contact.setMemberGroupId(getLong(FRM_FIELD_MEMBER_GROUP_ID));
                        
                            // add by fitra 26-02-2014

                        contact.setCompCountry(getString(FRM_FIELD_COMP_COUNTRY));
                        
                        contact.setCompProvince(getString(FRM_FIELD_COMP_PROVINCE));
                        
                        contact.setCompRegency(getString(FRM_FIELD_COMP_REGENCY));
     
                        contact.setHomeMobilePhone(getString(FRM_FIELD_HOME_MOBILE_PHONE));

                         contact.setTitle(getString(FRM_FIELD_TITLE));

                          contact.setHomeState(getString(FRM_FIELD_HOME_STATE));
                          
                          contact.setHomePostalCode(getString(FRM_FIELD_HOME_POSTALCODE));
                          
                         contact.setMemberBirthDate(getDate(FRM_FIELD_MEMBER_BIRTH_DATE));
                         
                         contact.setMemberSex(getInt(FRM_FIELD_MEMBER_SEX));
                         
                         contact.setMemberReligionId(getLong(FRM_FIELD_MEMBER_RELIGION_ID));
                         
                         contact.setNationality(getString(FRM_FIELD_NATIONALITY));
                         
                         contact.setMemberOccupation(getString(FRM_FIELD_MEMBER_OCCUPATION));
                         
                         contact.setHomeEmail(getString(FRM_FIELD_HOME_EMAIL));
                         
                         contact.setMemberIdCardNumber(getString(FRM_FIELD_MEMBER_ID_CARD_NUMBER));
                         
                         contact.setCurrencyTypeIdMemberConsigmentLimit(getLong(FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT));
                         
                         contact.setMemberConsigmentLimit(getFloat(FRM_FIELD_MEMBER_CONSIGMENT_LIMIT));
                         
                         contact.setCurrencyTypeIdMemberCreditLimit(getLong(FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT));
                         
                          contact.setMemberCreditLimit(getFloat(FRM_FIELD_MEMBER_CREDIT_LIMIT));
                          
                          contact.setDayOfPayment(getInt(FRM_FIELD_DAY_OF_PAYMENT));
                          
                          contact.setCompState(getString(FRM_FIELD_COMP_STATE));
                          
                          contact.setCompEmail(getString(FRM_FIELD_COMP_EMAIL));
                          
                          contact.setPostalCode(getString(FRM_FIELD_POSTAL_CODE));
                          
                          contact.setBirthPlace(getString(FRM_FIELD_BIRTH_PLACE));
                          
                        


		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

}

