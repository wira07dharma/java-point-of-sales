 

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

import java.io.*

;

import java.sql.*

;import java.util.*

;import java.util.Date;



/* package qdep */

import com.dimata.util.lang.I_Language;

import com.dimata.util.LogicParser;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;




import com.dimata.qdep.entity.*;



/* package hanoman */

//import com.dimata.qdep.db.DBHandler;

//import com.dimata.qdep.db.DBException;

//import com.dimata.qdep.db.DBLogger;

import com.dimata.hanoman.entity.masterdata.*;import com.dimata.harisma.entity.masterdata.*; 
import com.dimata.posbo.entity.masterdata.MemberReg;



public class PstContact extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

        public static final  String TBL_CONTACT = "contact_list";

	public static final  int FLD_CONTACT_ID = 0;

	public static final  int FLD_CONTACT_CODE = 1;

	public static final  int FLD_CONTACT_TYPE = 2;

	public static final  int FLD_REGDATE = 3;

	public static final  int FLD_COMP_NAME = 4;

	public static final  int FLD_PERSON_NAME = 5;

	public static final  int FLD_PERSON_LASTNAME = 6;

	public static final  int FLD_BUSS_ADDRESS = 7;

	public static final  int FLD_TOWN = 8;

	public static final  int FLD_PROVINCE = 9;

	public static final  int FLD_COUNTRY = 10;

	public static final  int FLD_TELP_NR = 11;

	public static final  int FLD_TELP_MOBILE = 12;

	public static final  int FLD_FAX = 13;

	public static final  int FLD_HOME_ADDR = 14;

	public static final  int FLD_HOME_TOWN = 15;

	public static final  int FLD_HOME_PROVINCE = 16;

	public static final  int FLD_HOME_COUNTRY = 17;

	public static final  int FLD_HOME_TELP = 18;

	public static final  int FLD_HOME_FAX = 19;

	public static final  int FLD_NOTES = 20;

	public static final  int FLD_BANK_ACC = 21;

	public static final  int FLD_BANK_ACC2 = 22;

	public static final  int FLD_EMAIL = 23;

        public static final  int FLD_EMPLOYEE_ID = 24;

        public static final  int FLD_PARENT_ID = 25;

        public static final  int FLD_PASSWORD = 26;

        public static final  int FLD_USER_ID = 27;

            // add by fitra 26-02-2014
        
        public static final  int FLD_COMP_COUNTRY = 28;

        public static final  int FLD_COMP_PROVINCE= 29;

        public static final  int FLD_COMP_REGENCY = 30;

        public static final  int FLD_HOME_MOBILE_PHONE = 31;

        public static final  int FLD_TITLE = 32;

        public static final  int FLD_HOME_STATE = 33;

        public static final  int FLD_HOME_POSTALCODE= 34;

        public static final  int FLD_MEMBER_BIRTH_DATE = 35;

        public static final  int FLD_MEMBER_SEX = 36;

        public static final  int FLD_MEMBER_RELIGION_ID = 37;

        public static final  int FLD_NATIONALITY = 38;

        public static final  int FLD_MEMBER_OCCUPATION = 39;

        public static final  int FLD_HOME_EMAIL= 40;

        public static final  int FLD_MEMBER_ID_CARD_NUMBER = 41;

        public static final  int FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT = 42;

        public static final  int FLD_MEMBER_CONSIGMENT_LIMIT = 43;

        public static final  int FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT = 44;

        public static final  int FLD_MEMBER_CREDIT_LIMIT = 45;

        public static final  int FLD_DAY_OF_PAYMENT = 46;

        public static final  int FLD_COMP_STATE = 47;

        public static final  int FLD_COMP_EMAIL =48;

        public static final  int FLD_POSTAL_CODE =49;
        
        public static final int FLD_MEMBER_GROUP_ID=50;

	public static final  String[] fieldNames = {

		"CONTACT_ID",

		"CONTACT_CODE",

		"CONTACT_TYPE",

		"REG_DATE",

		"COMP_NAME",

		"PERSON_NAME",

		"PERSON_LASTNAME",

		"COMP_ADDRESS",

		"COMP_CITY",

		"PROVINCE",

		"COUNTRY",//10

		"COMP_PH_NUMBER1",

		"COMP_PH_NUMBER2",

		"COMP_FAX",

		"HOME_ADDRESS",

		"HOME_CITY",

		"HOME_PROVINCE",

		"HOME_COUNTRY",

		"HOME_PH_NUMBER",

		"HOME_FAX",

		"NOTES",//20

		"BANK_ACC",

		"BANK_ACC2",

		"EMAIL_COMPANY",

                "EMPLOYEE_ID",

                "PARENT_ID",

                "MEMBER_PASSWORD_ID",
                
                "MEMBER_USER_ID",//27
                    // add by fitra 26-02-2014
                "COMP_COUNTRY",

                "COMP_PROVINCE",
                
                "COMP_REGENCY",
                
                "HOME_MOBILE_PHONE",//31
                "SALUTATION", "HOME_STATE",
                "HOME_ZIP_CODE","MEMBER_BIRTH_DATE",
                "MEMBER_SEX","MEMBER_RELIGION_ID",
                "NATIONALITY", "OCCUPATION",
                "HOME_EMAIL","MEMBER_ID_CARD_NUMBER",
                "CURRENCY_TYPE_ID_CONSIGMENT_LIMIT", "CONSIGMENT_LIMIT",
                "CURRENCY_TYPE_ID_CREDIT_LIMIT", "CREDIT_LIMIT",
                "DAYS_TERM_OF_PAYMENT", "COMP_STATE",
                "COMP_EMAIL","COMP_ZIP_CODE",// 49
                "MEMBER_GROUP_ID"

                

	 }; 



	public static final  int[] fieldTypes = {

		TYPE_LONG + TYPE_PK + TYPE_ID,

		TYPE_STRING,

		TYPE_INT,

		TYPE_DATE,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,//10

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,//20

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,

                TYPE_LONG,

                TYPE_LONG,

                TYPE_STRING,
                
                TYPE_STRING,//27
                    // add by fitra 26-02-2014

                TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING,//30
                
		TYPE_STRING,
                
                TYPE_STRING,TYPE_STRING,
                TYPE_STRING,TYPE_DATE,
                TYPE_INT,TYPE_LONG,
                TYPE_STRING,TYPE_STRING,
                TYPE_STRING,TYPE_STRING,
                TYPE_LONG,TYPE_FLOAT,
                TYPE_LONG,TYPE_FLOAT,
                TYPE_INT, TYPE_STRING,
                TYPE_STRING,TYPE_STRING,TYPE_LONG

	 };



        
    public static final int VALID = 1;
    public static final int NOT_VALID = 0;

    
     public static String sexNames[][] = {
        {"Pria", "Wanita"},
        {"Male", "Female"}
    };


    public static String statusNames[][] = {
        {"Tidak Berlaku", "Berlaku"},
        {"Not Valid", "Valid"}
    };
        

    public static final int EXT_COMPANY 	= 0;

    public static final int OWN_COMPANY 	= 1;

    public static final int EXT_PERSONEL 	= 2;



	public static final  String[] fieldNamesContactType = {

		"External Company",

		"Own Company",

		"External Personel"

    };



	//dibuka untuk akyati - eka

    public static final int CONTACT_TYPE_TRAVEL_AGENT           = 0;

    public static final int CONTACT_TYPE_SUPPLIER		= 1;

    public static final int CONTACT_TYPE_GUIDE			= 2;

    public static final int CONTACT_TYPE_COMPANY		= 3;

    public static final int CONTACT_TYPE_EMPLOYEE		= 4;

    //public static final int CONTACT_TYPE_DOT_COM_COMPANY	= 5;

    //public static final int CONTACT_TYPE_OTHER			= 6;

    public static final  int FLD_CLASS_SHIPPER                  = 5;

    public static final  int FLD_CLASS_CLIENT                   = 6;

    //baru

    public static final  int CONTACT_TYPE_DOT_COM_COMPANY       = 7;

    



    public static final String[] contactType = {"Travel Agent", "Supplier/Vendor", "Guide", "Corporate",  "Employee", "Shipper", "Client", ".com Company", "Other" };





    private double spsiDeduction;



	public PstContact(){

	}



	public PstContact(int i) throws DBException { 

		super(new PstContact()); 

	}



	public PstContact(String sOid) throws DBException { 

		super(new PstContact(0)); 

		if(!locate(sOid)) 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		else 

			return; 

	}



	public PstContact(long lOid) throws DBException { 

		super(new PstContact(0)); 

		String sOid = "0"; 

		try { 

			sOid = String.valueOf(lOid); 

		}catch(Exception e) { 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		} 

		if(!locate(sOid)) 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		else 

			return; 

	} 



	public int getFieldSize(){ 

		return fieldNames.length; 

	}



	public String getTableName(){ 

		return TBL_CONTACT;

	}



	public String[] getFieldNames(){ 

		return fieldNames; 

	}



	public int[] getFieldTypes(){ 

		return fieldTypes; 

	}



	public String getPersistentName(){ 

		return new PstContact().getClass().getName(); 

	}



	public long fetchExc(Entity ent) throws Exception{ 

		Contact contact = fetchExc(ent.getOID()); 

		ent = (Entity)contact; 

		return contact.getOID(); 

	}



	public long insertExc(Entity ent) throws Exception{ 

		return insertExc((Contact) ent); 

	}



	public long updateExc(Entity ent) throws Exception{ 

		return updateExc((Contact) ent); 

	}



	public long deleteExc(Entity ent) throws Exception{ 

		if(ent==null){ 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		} 

		return deleteExc(ent.getOID()); 

	}



	public static Contact fetchExc(long oid) throws DBException{ 

		try{ 

			Contact contact = new Contact();

			PstContact pstContact = new PstContact(oid); 

			contact.setOID(oid);



			contact.setContactCode(pstContact.getString(FLD_CONTACT_CODE));

			contact.setContactType(pstContact.getInt(FLD_CONTACT_TYPE));

			contact.setRegdate(pstContact.getDate(FLD_REGDATE));

			contact.setCompName(pstContact.getString(FLD_COMP_NAME));

			contact.setPersonName(pstContact.getString(FLD_PERSON_NAME));

			contact.setPersonLastname(pstContact.getString(FLD_PERSON_LASTNAME));

			contact.setBussAddress(pstContact.getString(FLD_BUSS_ADDRESS));

			contact.setTown(pstContact.getString(FLD_TOWN));

			contact.setProvince(pstContact.getString(FLD_PROVINCE));

			contact.setCountry(pstContact.getString(FLD_COUNTRY));

			contact.setTelpNr(pstContact.getString(FLD_TELP_NR));

			contact.setTelpMobile(pstContact.getString(FLD_TELP_MOBILE));

			contact.setFax(pstContact.getString(FLD_FAX));

			contact.setHomeAddr(pstContact.getString(FLD_HOME_ADDR));

			contact.setHomeTown(pstContact.getString(FLD_HOME_TOWN));

			contact.setHomeProvince(pstContact.getString(FLD_HOME_PROVINCE));

			contact.setHomeCountry(pstContact.getString(FLD_HOME_COUNTRY));

			contact.setHomeTelp(pstContact.getString(FLD_HOME_TELP));

			contact.setHomeFax(pstContact.getString(FLD_HOME_FAX));

			contact.setNotes(pstContact.getString(FLD_NOTES));

			contact.setBankAcc(pstContact.getString(FLD_BANK_ACC));

			contact.setBankAcc2(pstContact.getString(FLD_BANK_ACC2));

			contact.setEmail(pstContact.getString(FLD_EMAIL));

                        contact.setEmployeeID(pstContact.getlong(FLD_EMPLOYEE_ID));

                        contact.setParentId(pstContact.getlong(FLD_PARENT_ID)); 
                          // add by fitra

                        contact.setPassword(pstContact.getString(FLD_PASSWORD));
                        
                        contact.setUserId(pstContact.getString(FLD_USER_ID));

                        contact.setCompCountry(pstContact.getString(FLD_COMP_COUNTRY));
                        
                        contact.setCompProvince(pstContact.getString(FLD_COMP_PROVINCE));
                        
                        contact.setCompRegency(pstContact.getString(FLD_COMP_REGENCY));
                        
                         contact.setHomeMobilePhone(pstContact.getString(FLD_HOME_MOBILE_PHONE));

                         contact.setTitle(pstContact.getString(FLD_TITLE));
                         
                         contact.setHomeState(pstContact.getString(FLD_HOME_STATE));
                         
                         contact.setHomePostalCode(pstContact.getString(FLD_HOME_POSTALCODE));
                         
                         contact.setMemberBirthDate(pstContact.getDate(FLD_MEMBER_BIRTH_DATE));
                         
                         contact.setMemberSex(pstContact.getInt(FLD_MEMBER_SEX));
                         
                         contact.setNationality(pstContact.getString(FLD_NATIONALITY));
                         
                         contact.setMemberOccupation(pstContact.getString(FLD_MEMBER_OCCUPATION));
                         
                         contact.setHomeEmail(pstContact.getString(FLD_HOME_EMAIL));
                         
                         contact.setMemberIdCardNumber(pstContact.getString(FLD_MEMBER_ID_CARD_NUMBER));
                         
                         contact.setCurrencyTypeIdMemberConsigmentLimit(pstContact.getlong(FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT));

                         contact.setMemberConsigmentLimit(pstContact.getdouble(FLD_MEMBER_CONSIGMENT_LIMIT));

                        contact.setCurrencyTypeIdMemberCreditLimit(pstContact.getlong(FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT));

                         contact.setMemberCreditLimit(pstContact.getdouble(FLD_MEMBER_CREDIT_LIMIT));

                          contact.setDayOfPayment(pstContact.getInt(FLD_DAY_OF_PAYMENT));

                           contact.setCompState(pstContact.getString(FLD_COMP_STATE));

                            contact.setCompEmail(pstContact.getString(FLD_COMP_EMAIL));

                             contact.setPostalCode(pstContact.getString(FLD_POSTAL_CODE));
                             
                             contact.setMemberGroupId(pstContact.getlong(FLD_MEMBER_GROUP_ID));

			return contact; 

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContact(0),DBException.UNKNOWN); 

		} 

	}



	public static long insertExc(Contact contact) throws DBException{ 

		try{ 

			PstContact pstContact = new PstContact(0);



			pstContact.setString(FLD_CONTACT_CODE, contact.getContactCode());

			pstContact.setInt(FLD_CONTACT_TYPE, contact.getContactType());

			pstContact.setDate(FLD_REGDATE, contact.getRegdate());

			pstContact.setString(FLD_COMP_NAME, contact.getCompName());

			pstContact.setString(FLD_PERSON_NAME, contact.getPersonName());

			pstContact.setString(FLD_PERSON_LASTNAME, contact.getPersonLastname());

			pstContact.setString(FLD_BUSS_ADDRESS, contact.getBussAddress());

			pstContact.setString(FLD_TOWN, contact.getTown());

			pstContact.setString(FLD_PROVINCE, contact.getProvince());

			pstContact.setString(FLD_COUNTRY, contact.getCountry());

			pstContact.setString(FLD_TELP_NR, contact.getTelpNr());

			pstContact.setString(FLD_TELP_MOBILE, contact.getTelpMobile());

			pstContact.setString(FLD_FAX, contact.getFax());

			pstContact.setString(FLD_HOME_ADDR, contact.getHomeAddr());

			pstContact.setString(FLD_HOME_TOWN, contact.getHomeTown());

			pstContact.setString(FLD_HOME_PROVINCE, contact.getHomeProvince());

			pstContact.setString(FLD_HOME_COUNTRY, contact.getHomeCountry());

			pstContact.setString(FLD_HOME_TELP, contact.getHomeTelp());

			pstContact.setString(FLD_HOME_FAX, contact.getHomeFax());

			pstContact.setString(FLD_NOTES, contact.getNotes());

			pstContact.setString(FLD_BANK_ACC, contact.getBankAcc());

			pstContact.setString(FLD_BANK_ACC2, contact.getBankAcc2());

			pstContact.setString(FLD_EMAIL, contact.getEmail());

                        pstContact.setLong(FLD_EMPLOYEE_ID, contact.getEmployeeID())  ;

                        pstContact.setLong(FLD_PARENT_ID, contact.getParentId())  ;
                          // add by fitra

                        pstContact.setString(FLD_PASSWORD, contact.getPassword());

                        pstContact.setString(FLD_USER_ID, contact.getUserId());

                        pstContact.setString(FLD_COMP_COUNTRY, contact.getCompCountry());
                        
                        pstContact.setString(FLD_COMP_PROVINCE, contact.getCompProvince());
                        
                        pstContact.setString(FLD_COMP_REGENCY, contact.getCompRegency());
                        
                        pstContact.setString(FLD_HOME_MOBILE_PHONE, contact.getHomeMobilePhone());

                        pstContact.setString(FLD_TITLE, contact.getTitle());       
                        
                        pstContact.setString(FLD_HOME_STATE, contact.getHomeState());    
                        
                        pstContact.setString(FLD_HOME_POSTALCODE, contact.getHomePostalCode());
                        
                        pstContact.setDate(FLD_MEMBER_BIRTH_DATE, contact.getMemberBirthDate());
                        
                        pstContact.setInt(FLD_MEMBER_SEX, contact.getMemberSex());
                        
                        pstContact.setLong(FLD_MEMBER_RELIGION_ID, contact.getMemberReligionId());
                        
                        pstContact.setString(FLD_NATIONALITY, contact.getNationality());
                        
                        pstContact.setString(FLD_MEMBER_OCCUPATION, contact.getMemberOccupation());
                        
                        pstContact.setString(FLD_HOME_EMAIL, contact.getHomeEmail());
                        
                        pstContact.setString(FLD_MEMBER_ID_CARD_NUMBER, contact.getMemberIdCardNumber());
                        
                        pstContact.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT, contact.getCurrencyTypeIdMemberConsigmentLimit());   
                        
                        pstContact.setDouble(FLD_MEMBER_CONSIGMENT_LIMIT, contact.getMemberConsigmentLimit());
                        
                          pstContact.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT, contact.getCurrencyTypeIdMemberCreditLimit());  
                          
                            pstContact.setDouble(FLD_MEMBER_CREDIT_LIMIT, contact.getMemberCreditLimit());
                            
                              pstContact.setInt(FLD_DAY_OF_PAYMENT, contact.getDayOfPayment());
                              
                                pstContact.setString(FLD_COMP_STATE, contact.getCompState());
                                
                                  pstContact.setString(FLD_POSTAL_CODE, contact.getPostalCode());
                                  pstContact.setLong(FLD_MEMBER_GROUP_ID, contact.getMemberGroupId());

			pstContact.insert(); 

			contact.setOID(pstContact.getlong(FLD_CONTACT_ID));

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContact(0),DBException.UNKNOWN); 

		}

		return contact.getOID();

	}



	public static long updateExc(Contact contact) throws DBException{ 

		try{ 

			if(contact.getOID() != 0){ 

				PstContact pstContact = new PstContact(contact.getOID());



				pstContact.setString(FLD_CONTACT_CODE, contact.getContactCode());

				pstContact.setInt(FLD_CONTACT_TYPE, contact.getContactType());

				pstContact.setDate(FLD_REGDATE, contact.getRegdate());

				pstContact.setString(FLD_COMP_NAME, contact.getCompName());

				pstContact.setString(FLD_PERSON_NAME, contact.getPersonName());

				pstContact.setString(FLD_PERSON_LASTNAME, contact.getPersonLastname());

				pstContact.setString(FLD_BUSS_ADDRESS, contact.getBussAddress());

				pstContact.setString(FLD_TOWN, contact.getTown());

				pstContact.setString(FLD_PROVINCE, contact.getProvince());

				pstContact.setString(FLD_COUNTRY, contact.getCountry());

				pstContact.setString(FLD_TELP_NR, contact.getTelpNr());

				pstContact.setString(FLD_TELP_MOBILE, contact.getTelpMobile());

				pstContact.setString(FLD_FAX, contact.getFax());

				pstContact.setString(FLD_HOME_ADDR, contact.getHomeAddr());

				pstContact.setString(FLD_HOME_TOWN, contact.getHomeTown());

				pstContact.setString(FLD_HOME_PROVINCE, contact.getHomeProvince());

				pstContact.setString(FLD_HOME_COUNTRY, contact.getHomeCountry());

				pstContact.setString(FLD_HOME_TELP, contact.getHomeTelp());

				pstContact.setString(FLD_HOME_FAX, contact.getHomeFax());

				pstContact.setString(FLD_NOTES, contact.getNotes());

				pstContact.setString(FLD_BANK_ACC, contact.getBankAcc());

				pstContact.setString(FLD_BANK_ACC2, contact.getBankAcc2());

				pstContact.setString(FLD_EMAIL, contact.getEmail());

                                pstContact.setLong(FLD_EMPLOYEE_ID, contact.getEmployeeID()) ;

                                pstContact.setLong(FLD_PARENT_ID, contact.getParentId()) ;
                                //add by fitra

                                pstContact.setString(FLD_PASSWORD, contact.getPassword());

                                 pstContact.setString(FLD_USER_ID, contact.getUserId());

                                   pstContact.setString(FLD_COMP_COUNTRY, contact.getCompCountry());
                        
                                 pstContact.setString(FLD_COMP_PROVINCE, contact.getCompProvince());
                        
                                 pstContact.setString(FLD_COMP_REGENCY, contact.getCompRegency());
                                 
                                   pstContact.setString(FLD_HOME_MOBILE_PHONE, contact.getHomeMobilePhone());

                                    pstContact.setString(FLD_TITLE, contact.getTitle());
                        
                        pstContact.setString(FLD_HOME_STATE, contact.getHomeState());
                        
                        pstContact.setString(FLD_HOME_POSTALCODE, contact.getHomePostalCode());
                        
                        pstContact.setDate(FLD_MEMBER_BIRTH_DATE, contact.getMemberBirthDate());
                        
                        pstContact.setInt(FLD_MEMBER_SEX, contact.getMemberSex());
                        
                        pstContact.setLong(FLD_MEMBER_RELIGION_ID, contact.getMemberReligionId());
                        
                        pstContact.setString(FLD_NATIONALITY, contact.getNationality());
                        
                        pstContact.setString(FLD_MEMBER_OCCUPATION, contact.getMemberOccupation());
                        
                        pstContact.setString(FLD_HOME_EMAIL, contact.getHomeEmail());
                        
                        pstContact.setString(FLD_MEMBER_ID_CARD_NUMBER, contact.getMemberIdCardNumber());
                        
                        pstContact.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT, contact.getCurrencyTypeIdMemberConsigmentLimit());   
                        
                        pstContact.setDouble(FLD_MEMBER_CONSIGMENT_LIMIT, contact.getMemberConsigmentLimit());
                        
                          pstContact.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT, contact.getCurrencyTypeIdMemberCreditLimit());  
                          
                            pstContact.setDouble(FLD_MEMBER_CREDIT_LIMIT, contact.getMemberCreditLimit());
                            
                              pstContact.setInt(FLD_DAY_OF_PAYMENT, contact.getDayOfPayment());
                              
                                pstContact.setString(FLD_COMP_STATE, contact.getCompState());
                                
                                  pstContact.setString(FLD_POSTAL_CODE, contact.getPostalCode());
                                  
                                  pstContact.setLong(FLD_MEMBER_GROUP_ID, contact.getMemberGroupId());

				pstContact.update(); 

				return contact.getOID();



			}

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContact(0),DBException.UNKNOWN); 

		}

		return 0;

	}



	public static long deleteExc(long oid) throws DBException{ 

		try{ 

			PstContact pstContact = new PstContact(oid);

			pstContact.delete();

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContact(0),DBException.UNKNOWN); 

		}

		return oid;

	}



	public static Vector listAll(){ 

		return list(0, 500, "",""); 

	}



	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){

		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT * FROM " + TBL_CONTACT; 

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

				Contact contact = new Contact();

				resultToObject(rs, contact);

				lists.add(contact);

			}

			rs.close();

			return lists;



		}catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return new Vector();

	}



	public static Vector listContactByClassType(int limitStart, int recordToGet, String whereClause, String order){

		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT CONT.* FROM " + TBL_CONTACT + " AS CONT " +

                		 " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +

                         " ON CONT." + PstContact.fieldNames[PstContact.FLD_CONTACT_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +

                		 " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +

                         " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID];

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

            System.out.println("sql : " + sql);

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

				Contact contact = new Contact();

				resultToObject(rs, contact);

				lists.add(contact);

			}

			rs.close();

			return lists;



		}catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return new Vector();

	}



   public static Vector parse(String str) {

        Vector vector = LogicParser.textSentence(str);

        if (vector != null && vector.size() > 0) {

            if (((vector.get(vector.size() - 1)).equals(LogicParser.SIGN)) &&

                    ((vector.get(vector.size() - 1)).equals(LogicParser.ENGLISH)))

                vector.remove(vector.size() - 1);

        }

        return vector;

    }



    public static Vector listContactByType(int limitStart, int recordToGet, String whereClause, String order){

		Vector lists = new Vector();

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT CONT.* FROM " + TBL_CONTACT + " AS CONT " +

                		 " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +

                         " ON CONT." + PstContact.fieldNames[PstContact.FLD_CONTACT_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +

                		 " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +

                         " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID];


            if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

            System.out.println("sql : " + sql);

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

				Contact contact = new Contact();

				resultToObject(rs, contact);

				lists.add(contact);

			}

			rs.close();

			return lists;



		}catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return new Vector();

	}



	public static void resultToObject(ResultSet rs, Contact contact){

		try{

			contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));

			contact.setContactCode(rs.getString(PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]));

			contact.setContactType(rs.getInt(PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]));

			contact.setRegdate(rs.getDate(PstContact.fieldNames[PstContact.FLD_REGDATE]));

			contact.setCompName(rs.getString(PstContact.fieldNames[PstContact.FLD_COMP_NAME]));

			contact.setPersonName(rs.getString(PstContact.fieldNames[PstContact.FLD_PERSON_NAME]));

			contact.setPersonLastname(rs.getString(PstContact.fieldNames[PstContact.FLD_PERSON_LASTNAME]));

			contact.setBussAddress(rs.getString(PstContact.fieldNames[PstContact.FLD_BUSS_ADDRESS]));

			contact.setTown(rs.getString(PstContact.fieldNames[PstContact.FLD_TOWN]));

			contact.setProvince(rs.getString(PstContact.fieldNames[PstContact.FLD_PROVINCE]));

			contact.setCountry(rs.getString(PstContact.fieldNames[PstContact.FLD_COUNTRY]));

			contact.setTelpNr(rs.getString(PstContact.fieldNames[PstContact.FLD_TELP_NR]));

			contact.setTelpMobile(rs.getString(PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]));

			contact.setFax(rs.getString(PstContact.fieldNames[PstContact.FLD_FAX]));

			contact.setHomeAddr(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_ADDR]));

			contact.setHomeTown(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_TOWN]));

			contact.setHomeProvince(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_PROVINCE]));

			contact.setHomeCountry(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]));

			contact.setHomeTelp(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_TELP]));

			contact.setHomeFax(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_FAX]));

			contact.setNotes(rs.getString(PstContact.fieldNames[PstContact.FLD_NOTES]));

			contact.setBankAcc(rs.getString(PstContact.fieldNames[PstContact.FLD_BANK_ACC]));

			contact.setBankAcc2(rs.getString(PstContact.fieldNames[PstContact.FLD_BANK_ACC2]));

			contact.setEmail(rs.getString(PstContact.fieldNames[PstContact.FLD_EMAIL]));

                        contact.setEmployeeID(rs.getLong(PstContact.fieldNames[PstContact.FLD_EMPLOYEE_ID]));

                        contact.setParentId(rs.getLong(PstContact.fieldNames[PstContact.FLD_PARENT_ID]));

                        // Add by fitra
                        contact.setPassword(rs.getString(PstContact.fieldNames[PstContact.FLD_PASSWORD]));

                        contact.setUserId(rs.getString(PstContact.fieldNames[PstContact.FLD_USER_ID]));

                        contact.setCompCountry(rs.getString(PstContact.fieldNames[PstContact.FLD_COMP_COUNTRY]));
                        
                        contact.setCompProvince(rs.getString(PstContact.fieldNames[PstContact.FLD_COMP_PROVINCE]));
                        
                        contact.setCompRegency(rs.getString(PstContact.fieldNames[PstContact.FLD_COMP_REGENCY]));
                        
                        contact.setHomeMobilePhone(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_MOBILE_PHONE]));

                        contact.setTitle(rs.getString(PstContact.fieldNames[PstContact.FLD_TITLE]));
                        
                         contact.setHomePostalCode(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_POSTALCODE]));
                         
                          contact.setMemberBirthDate(rs.getDate(PstContact.fieldNames[PstContact.FLD_MEMBER_BIRTH_DATE]));
                          
                           contact.setMemberSex(rs.getInt(PstContact.fieldNames[PstContact.FLD_MEMBER_SEX])); 
                           
                            contact.setMemberReligionId(rs.getLong(PstContact.fieldNames[PstContact.FLD_MEMBER_RELIGION_ID]));
                            
                             contact.setNationality(rs.getString(PstContact.fieldNames[PstContact.FLD_NATIONALITY]));
                             
                              contact.setMemberOccupation(rs.getString(PstContact.fieldNames[PstContact.FLD_MEMBER_OCCUPATION]));
                              
                               contact.setHomeEmail(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_EMAIL]));
                               
                                contact.setMemberIdCardNumber(rs.getString(PstContact.fieldNames[PstContact.FLD_MEMBER_ID_CARD_NUMBER]));
                                
                                 contact.setCurrencyTypeIdMemberConsigmentLimit(rs.getLong(PstContact.fieldNames[PstContact.FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT]));
                                 
                                  contact.setMemberConsigmentLimit(rs.getFloat(PstContact.fieldNames[PstContact.FLD_MEMBER_CONSIGMENT_LIMIT]));   
                                  
                                   contact.setCurrencyTypeIdMemberCreditLimit(rs.getLong(PstContact.fieldNames[PstContact.FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT]));
                                   
                                    contact.setMemberCreditLimit(rs.getFloat(PstContact.fieldNames[PstContact.FLD_MEMBER_CREDIT_LIMIT]));
                                    
                                     contact.setDayOfPayment(rs.getInt(PstContact.fieldNames[PstContact.FLD_DAY_OF_PAYMENT]));
                                     
                                      contact.setCompState(rs.getString(PstContact.fieldNames[PstContact.FLD_COMP_STATE]));
                                      
                                       contact.setCompEmail(rs.getString(PstContact.fieldNames[PstContact.FLD_COMP_EMAIL]));
                                       
                                        contact.setPostalCode(rs.getString(PstContact.fieldNames[PstContact.FLD_POSTAL_CODE]));
                                        
                                        contact.setHomeState(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_STATE]));
                        
                                        contact.setMemberGroupId(rs.getLong(PstContact.fieldNames[PstContact.FLD_MEMBER_GROUP_ID]));
                                        

		}catch(Exception e){

        	System.out.println("exception 123 : "+e.toString());

        }

	}



	public static boolean checkOID(long contactId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_CONTACT + " WHERE " + 

						PstContact.fieldNames[PstContact.FLD_CONTACT_ID] + " = " + contactId;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			while(rs.next()) { result = true; }

			rs.close();

		}catch(Exception e){

			System.out.println("err : "+e.toString());

		}finally{

			DBResultSet.close(dbrs);

			return result;

		}

	}



         public static int countContact(String whereClause){

		//Vector lists = new Vector(); 
                
                int count = 0;     

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT COUNT(" +
                                     " MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+")" +                                
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     com.dimata.common.entity.contact.PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ com.dimata.common.entity.contact.PstContactClassAssign.fieldNames[com.dimata.common.entity.contact.PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     com.dimata.common.entity.contact.PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     com.dimata.common.entity.contact.PstContactClass.fieldNames[com.dimata.common.entity.contact.PstContactClass.FLD_CONTACT_CLASS_ID];
                                     

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			
			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while (rs.next()) {
                        count = rs.getInt(1);
                       }


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);
                         return  count;
		}

			

	}
        
        
        
	public static int getCount(String whereClause){

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT COUNT("+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID] + ") FROM " + TBL_CONTACT;

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			int count = 0;

			while(rs.next()) { count = rs.getInt(1); }



			rs.close();

			return count;

		}catch(Exception e) {

			return 0;

		}finally {

			DBResultSet.close(dbrs);

		}

	}


// add by fitra
         public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Contact contact = (Contact) list.get(ls);
                    if (oid == contact.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }
        
        
	public static int getCountContactByClassType(String whereClause){

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT COUNT(CONT."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID] + ") FROM " + TBL_CONTACT + " AS CONT " +

                		 " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +

                         " ON CONT." + PstContact.fieldNames[PstContact.FLD_CONTACT_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +

                		 " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +

                         " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID];

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			int count = 0;

			while(rs.next()) { count = rs.getInt(1); }



			rs.close();

			return count;

		}catch(Exception e) {

			return 0;

		}finally {

			DBResultSet.close(dbrs);

		}

	}

        

	public static boolean cekCodeContact(String code , long oid){//, int cntType){

        DBResultSet dbrs = null;

		boolean bool = false;

		try {

			String sql = "SELECT "+PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]+" FROM " + TBL_CONTACT+

                " WHERE "+PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]+" = '"+code+"'"+

                " AND "+PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+" <> '"+oid+"'";

                //" AND "+PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]+" = "+cntType;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			while(rs.next()) {

                bool = true;

            }

			rs.close();



		}catch(Exception e) {

			return true;

		}finally {

			DBResultSet.close(dbrs);

            return bool;

		}

	}



	public static ContactClass getContactClassType(long idContact){

		DBResultSet dbrs = null;

        ContactClass contactClass = new ContactClass();

		try {

			String sql = "SELECT CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +

                		 ", CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +

                		 ", CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME] +

                		 ", CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION] +

                		 " FROM " + PstContact.TBL_CONTACT + " AS CONT " +

                		 " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +

                         " ON CONT." + PstContact.fieldNames[PstContact.FLD_CONTACT_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +

                		 " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +

                         " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +

                		 " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +

                         " WHERE CONT." + PstContact.fieldNames[PstContact.FLD_CONTACT_ID] +

                         " = " + idContact;

            //System.out.println("..........sql : " + sql);

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

                contactClass.setOID(rs.getLong(1));

                contactClass.setClassType(rs.getInt(2));

                contactClass.setClassName(rs.getString(3));

                contactClass.setClassDescription(rs.getString(4));

            }

		}catch(Exception e) {

			return new ContactClass();

		}finally {

			DBResultSet.close(dbrs);

            return contactClass;

		}

	}



    public double getSpsiDeduction(){ return spsiDeduction; }



    public void setSpsiDeduction(double spsiDeduction){ this.spsiDeduction = spsiDeduction; }

    

    /*public static long insertContractForNewContact(long newContact, long parentContact){

        //long oid = PstContract.insertContractForNewContact(newContact, parentContact);

        //return oid;

    }*/
    
    

}

