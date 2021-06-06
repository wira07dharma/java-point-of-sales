/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.entity.contact;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;

/* package common */
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.common.entity.location.PstLocation;

public class PstContactList extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CONTACT_LIST = "contact_list";

    public static final int FLD_CONTACT_ID = 0;
    public static final int FLD_CONTACT_CODE = 1;
    public static final int FLD_EMPLOYEE_ID = 2;
    public static final int FLD_PARENT_ID = 3;
    public static final int FLD_CONTACT_TYPE = 4;
    public static final int FLD_REGDATE = 5;
    public static final int FLD_COMP_NAME = 6;
    public static final int FLD_PERSON_NAME = 7;
    public static final int FLD_PERSON_LASTNAME = 8;
    public static final int FLD_BUSS_ADDRESS = 9;
    public static final int FLD_TOWN = 10;
    public static final int FLD_PROVINCE = 11;
    public static final int FLD_COUNTRY = 12;
    public static final int FLD_TELP_NR = 13;
    public static final int FLD_TELP_MOBILE = 14;
    public static final int FLD_FAX = 15;
    public static final int FLD_HOME_ADDR = 16;
    public static final int FLD_HOME_TOWN = 17;
    public static final int FLD_HOME_PROVINCE = 18;
    public static final int FLD_HOME_COUNTRY = 19;
    public static final int FLD_HOME_TELP = 20;
    public static final int FLD_HOME_FAX = 21;
    public static final int FLD_NOTES = 22;
    public static final int FLD_BANK_ACC = 23;
    public static final int FLD_BANK_ACC2 = 24;
    public static final int FLD_EMAIL = 25;
    public static final int FLD_DIRECTIONS = 26;
    public static final int FLD_MEMBER_LAST_UPDATE = 27;
    public static final int FLD_PROCESS_STATUS = 28;

    //new
    public static final int FLD_COMPANY_BANK_ACC = 29;
    public static final int FLD_POSITION_PERSON = 30;
    public static final int FLD_POSTAL_CODE_COMPANY = 31;
    public static final int FLD_WEBSITE_COMPANY = 32;
    public static final int FLD_EMAIL_COMPANY = 33;
    public static final int FLD_POSTAL_CODE_HOME = 34;
    public static final int FLD_LOCATION_ID=35;
    
    //new
    public static final int FLD_TERM_OF_DELIVERY = 36;

    //add by fitra
    public static final int FLD_COMP_COUNTRY = 37;

    public static final int FLD_COMP_PROVINCE = 38;

    public static final int FLD_COMP_REGENCY = 39;

    public static final int FLD_HOME_MOBILE_PHONE = 40;

    public static final int FLD_TITLE = 41;

    public static final int FLD_HOME_STATE = 42;

    public static final int FLD_MEMBER_BIRTH_DATE = 43;

    public static final int FLD_MEMBER_SEX = 44;

    public static final int FLD_MEMBER_RELIGION_ID = 45;

    public static final int FLD_NATIONALITY = 46;

    public static final int FLD_MEMBER_OCCUPATION = 47;

    public static final int FLD_HOME_EMAIL = 48;

    public static final int FLD_MEMBER_ID_CARD_NUMBER = 49;

    public static final int FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT = 50;

    public static final int FLD_MEMBER_CONSIGMENT_LIMIT = 51;

    public static final int FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT = 52;

    public static final int FLD_MEMBER_CREDIT_LIMIT = 53;

    public static final int FLD_DAY_OF_PAYMENT = 54;

    public static final int FLD_COMP_STATE = 55;

    public static final int FLD_COMP_EMAIL = 56;

    public static final int FLD_POSTAL_CODE = 57;
    public static final int FLD_HOME_CITY = 58;

    //added by dewok 2019-10-07 for duty free
    public static final int FLD_PASSPORT_NO = 59;

    public static final String[] fieldNames = {
        "CONTACT_ID",
        "CONTACT_CODE",
        "EMPLOYEE_ID",
        "PARENT_ID",
        "CONTACT_TYPE",
        "REG_DATE",
        "COMP_NAME",
        "PERSON_NAME",
        "PERSON_LASTNAME",
        "BUSS_ADDRESS",
        "TOWN",//10
        "PROVINCE",
        "COUNTRY",
        "TELP_NR",
        "TELP_MOBILE",
        "FAX",
        "HOME_ADDR",
        "HOME_TOWN",
        "HOME_PROVINCE",
        "HOME_COUNTRY",
        "HOME_TELP",//20
        "HOME_FAX",
        "NOTES",
        "BANK_ACC",
        "BANK_ACC2",
        "EMAIL",
        "DIRECTIONS",
        "MEMBER_LAST_UPDATE",
        "PROCESS_STATUS",

        //new
        "COMPANY_BANK_ACC",
        "POSITION_PERSON",//30
        "POSTAL_CODE_COMPANY",
        "WEBSITE_COMPANY",
        "EMAIL_COMPANY",
        "POSTAL_CODE_HOME",
        "LOCATION_ID",
        "TERM_OF_DELIVERY",
            // by fitra
        "COMP_COUNTRY",
        "COMP_PROVINCE",
        "COMP_REGENCY",
        "HOME_MOBILE_PHONE",//40
        "SALUTATION",
        "HOME_STATE",
        "MEMBER_BIRTH_DATE",
        "MEMBER_SEX",
        "MEMBER_RELIGION_ID",
        "NATIONALITY",
        "OCCUPATION",
        "HOME_EMAIL",
        "MEMBER_ID_CARD_NUMBER",
        "CURRENCY_TYPE_ID_CONSIGMENT_LIMIT",//50
        "CONSIGMENT_LIMIT",
        "CURRENCY_TYPE_ID_CREDIT_LIMIT",  
        "CREDIT_LIMIT",
        "DAYS_TERM_OF_PAYMENT",
        "COMP_STATE",
        "COMP_EMAIL",
        "COMP_ZIP_CODE",//57
        "HOME_CITY",
        "PASSPORT_NO"
    };

    public static String sexNames[][] = {
        {"Pria", "Wanita"},
        {"Male", "Female"}
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
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
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        //new
        TYPE_STRING,
        TYPE_STRING,//30
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        //ADD BY FITRA
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,//40
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,//50
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,//57
        TYPE_STRING,
        TYPE_STRING
    };


    //status process transfer
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;

    public static final int EXT_COMPANY = 0;
    public static final int OWN_COMPANY = 1;
    public static final int EXT_PERSONEL = 2;
    public static final int EMPLOYEE = 3;

    public static final String[] fieldNamesContactType = {
        "External Company",
        "Own Company",
        "External Personel",
        "Employee"
    };

    public PstContactList() {
    }

    public PstContactList(int i) throws DBException {
        super(new PstContactList());
    }

    public PstContactList(String sOid) throws DBException {
        super(new PstContactList(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstContactList(long lOid) throws DBException {
        super(new PstContactList(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_CONTACT_LIST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstContactList().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        ContactList contactlist = fetchExc(ent.getOID());
        ent = (Entity) contactlist;
        return contactlist.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((ContactList) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((ContactList) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ContactList fetchExc(long oid) throws DBException {
        try {
            ContactList contactlist = new ContactList();
            PstContactList pstContactList = new PstContactList(oid);
            contactlist.setOID(oid);
            try {
                contactlist.setContactCode(pstContactList.getString(FLD_CONTACT_CODE));
                contactlist.setEmployeeId(pstContactList.getlong(FLD_EMPLOYEE_ID));
                contactlist.setParentId(pstContactList.getlong(FLD_PARENT_ID));
                contactlist.setContactType(pstContactList.getInt(FLD_CONTACT_TYPE));
                contactlist.setRegdate(pstContactList.getDate(FLD_REGDATE));
                contactlist.setCompName(pstContactList.getString(FLD_COMP_NAME));
                contactlist.setPersonName(pstContactList.getString(FLD_PERSON_NAME));
                contactlist.setPersonLastname(pstContactList.getString(FLD_PERSON_LASTNAME));
                contactlist.setBussAddress(pstContactList.getString(FLD_BUSS_ADDRESS));
                contactlist.setTown(pstContactList.getString(FLD_TOWN));
                contactlist.setProvince(pstContactList.getString(FLD_PROVINCE));
                contactlist.setCountry(pstContactList.getString(FLD_COUNTRY));
                contactlist.setTelpNr(pstContactList.getString(FLD_TELP_NR));
                contactlist.setTelpMobile(pstContactList.getString(FLD_TELP_MOBILE));
                contactlist.setFax(pstContactList.getString(FLD_FAX));
                contactlist.setHomeAddr(pstContactList.getString(FLD_HOME_ADDR));
                contactlist.setHomeTown(pstContactList.getString(FLD_HOME_TOWN));
                contactlist.setHomeProvince(pstContactList.getString(FLD_HOME_PROVINCE));
                contactlist.setHomeCountry(pstContactList.getString(FLD_HOME_COUNTRY));
                contactlist.setHomeTelp(pstContactList.getString(FLD_HOME_TELP));
                contactlist.setHomeFax(pstContactList.getString(FLD_HOME_FAX));
                contactlist.setNotes(pstContactList.getString(FLD_NOTES));
                contactlist.setBankAcc(pstContactList.getString(FLD_BANK_ACC));
                contactlist.setBankAcc2(pstContactList.getString(FLD_BANK_ACC2));
                contactlist.setEmail(pstContactList.getString(FLD_EMAIL));
                contactlist.setDirections(pstContactList.getString(FLD_DIRECTIONS));
                contactlist.setLastUpdate(pstContactList.getDate(FLD_MEMBER_LAST_UPDATE));
                contactlist.setProcessStatus(pstContactList.getInt(FLD_PROCESS_STATUS));

            //new
                contactlist.setCompanyBankAcc(pstContactList.getString(FLD_COMPANY_BANK_ACC));
                contactlist.setPositionPerson(pstContactList.getString(FLD_POSITION_PERSON));
                contactlist.setPostalCodeCompany(pstContactList.getString(FLD_POSTAL_CODE_COMPANY));
                contactlist.setWebsiteCompany(pstContactList.getString(FLD_WEBSITE_COMPANY));
                contactlist.setEmailCompany(pstContactList.getString(FLD_EMAIL_COMPANY));
                contactlist.setPostalCodeHome(pstContactList.getString(FLD_POSTAL_CODE_HOME));
                contactlist.setLocationId(pstContactList.getlong(FLD_LOCATION_ID));
                //new
                contactlist.setTermOfDelivery(pstContactList.getInt(FLD_TERM_OF_DELIVERY));

                // add by fitra
                contactlist.setCompCountry(pstContactList.getString(FLD_COMP_COUNTRY));
                contactlist.setCompProvince(pstContactList.getString(FLD_COMP_PROVINCE));
                contactlist.setCompRegency(pstContactList.getString(FLD_COMP_REGENCY));
                contactlist.setHomeMobilePhone(pstContactList.getString(FLD_HOME_MOBILE_PHONE));
                contactlist.setTitle(pstContactList.getString(FLD_TITLE));
                contactlist.setHomeState(pstContactList.getString(FLD_HOME_STATE));

                contactlist.setMemberBirthDate(pstContactList.getDate(FLD_MEMBER_BIRTH_DATE));
                contactlist.setMemberSex(pstContactList.getInt(FLD_MEMBER_SEX));
                contactlist.setMemberReligionId(pstContactList.getlong(FLD_MEMBER_RELIGION_ID));
                contactlist.setNationality(pstContactList.getString(FLD_NATIONALITY));
                contactlist.setMemberOccupation(pstContactList.getString(FLD_MEMBER_OCCUPATION));
                contactlist.setHomeEmail(pstContactList.getString(FLD_HOME_EMAIL));
                contactlist.setMemberIdCardNumber(pstContactList.getString(FLD_MEMBER_ID_CARD_NUMBER));
                contactlist.setCurrencyTypeIdMemberConsigmentLimit(pstContactList.getlong(FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT));
                contactlist.setMemberConsigmentLimit(pstContactList.getlong(FLD_MEMBER_CONSIGMENT_LIMIT));
                contactlist.setCurrencyTypeIdMemberCreditLimit(pstContactList.getlong(FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT));
                contactlist.setMemberCreditLimit(pstContactList.getdouble(FLD_MEMBER_CREDIT_LIMIT));
                contactlist.setDayOfPayment(pstContactList.getInt(FLD_DAY_OF_PAYMENT));
                //contactlist.setCompCountry(pstContactList.getString(FLD_HOME_EMAIL));
                contactlist.setCompState(pstContactList.getString(FLD_COMP_STATE));
                contactlist.setCompEmail(pstContactList.getString(FLD_COMP_EMAIL));
                contactlist.setPostalCode(pstContactList.getString(FLD_POSTAL_CODE));
                contactlist.setHomeCity(pstContactList.getString(FLD_HOME_CITY));
                
                contactlist.setPassportNo(pstContactList.getString(FLD_PASSPORT_NO));

            } catch (Exception e) {
                System.out.println("sdfasdfasdf" + e.toString());
            }

            return contactlist;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ContactList contactlist) throws DBException {
        try {
            PstContactList pstContactList = new PstContactList(0);

            pstContactList.setString(FLD_CONTACT_CODE, contactlist.getContactCode());
            pstContactList.setLong(FLD_EMPLOYEE_ID, contactlist.getEmployeeId());
            pstContactList.setLong(FLD_PARENT_ID, contactlist.getParentId());
            pstContactList.setInt(FLD_CONTACT_TYPE, contactlist.getContactType());
            pstContactList.setDate(FLD_REGDATE, contactlist.getRegdate());
            pstContactList.setString(FLD_COMP_NAME, contactlist.getCompName());
            pstContactList.setString(FLD_PERSON_NAME, contactlist.getPersonName());
            pstContactList.setString(FLD_PERSON_LASTNAME, contactlist.getPersonLastname());
            pstContactList.setString(FLD_BUSS_ADDRESS, contactlist.getBussAddress());
            pstContactList.setString(FLD_TOWN, contactlist.getTown());
            pstContactList.setString(FLD_PROVINCE, contactlist.getProvince());
            pstContactList.setString(FLD_COUNTRY, contactlist.getCountry());
            pstContactList.setString(FLD_TELP_NR, contactlist.getTelpNr());
            pstContactList.setString(FLD_TELP_MOBILE, contactlist.getTelpMobile());
            pstContactList.setString(FLD_FAX, contactlist.getFax());
            pstContactList.setString(FLD_HOME_ADDR, contactlist.getHomeAddr());
            pstContactList.setString(FLD_HOME_TOWN, contactlist.getHomeTown());
            pstContactList.setString(FLD_HOME_PROVINCE, contactlist.getHomeProvince());
            pstContactList.setString(FLD_HOME_COUNTRY, contactlist.getHomeCountry());
            pstContactList.setString(FLD_HOME_TELP, contactlist.getHomeTelp());
            pstContactList.setString(FLD_HOME_FAX, contactlist.getHomeFax());
            pstContactList.setString(FLD_NOTES, contactlist.getNotes());
            pstContactList.setString(FLD_BANK_ACC, contactlist.getBankAcc());
            pstContactList.setString(FLD_BANK_ACC2, contactlist.getBankAcc2());
            pstContactList.setString(FLD_EMAIL, contactlist.getEmail());
            pstContactList.setString(FLD_DIRECTIONS, contactlist.getDirections());
            pstContactList.setDate(FLD_MEMBER_LAST_UPDATE, contactlist.getLastUpdate());
            pstContactList.setInt(FLD_PROCESS_STATUS, contactlist.getProcessStatus());

            //new
            pstContactList.setString(FLD_COMPANY_BANK_ACC, contactlist.getCompanyBankAcc());
            pstContactList.setString(FLD_POSITION_PERSON, contactlist.getPositionPerson());
            pstContactList.setString(FLD_POSTAL_CODE_COMPANY, contactlist.getPostalCodeCompany());
            pstContactList.setString(FLD_WEBSITE_COMPANY, contactlist.getWebsiteCompany());
            pstContactList.setString(FLD_EMAIL_COMPANY, contactlist.getEmailCompany());
            pstContactList.setString(FLD_POSTAL_CODE_HOME, contactlist.getPostalCodeHome());
            pstContactList.setLong(FLD_LOCATION_ID, contactlist.getLocationId());

            pstContactList.setInt(FLD_TERM_OF_DELIVERY, contactlist.getTermOfDelivery());
            
            //add by fitra
            pstContactList.setString(FLD_COMP_COUNTRY, contactlist.getCompCountry());
            pstContactList.setString(FLD_COMP_PROVINCE, contactlist.getCompProvince());
            pstContactList.setString(FLD_COMP_REGENCY, contactlist.getCompRegency());
            pstContactList.setString(FLD_HOME_MOBILE_PHONE, contactlist.getHomeMobilePhone());
            pstContactList.setString(FLD_TITLE, contactlist.getTitle());
            pstContactList.setString(FLD_HOME_STATE, contactlist.getHomeState());
            pstContactList.setDate(FLD_MEMBER_BIRTH_DATE, contactlist.getMemberBirthDate());
            pstContactList.setInt(FLD_MEMBER_SEX, contactlist.getMemberSex());
            pstContactList.setLong(FLD_MEMBER_RELIGION_ID, contactlist.getMemberReligionId());
            pstContactList.setString(FLD_NATIONALITY, contactlist.getNationality());
            pstContactList.setString(FLD_MEMBER_OCCUPATION, contactlist.getMemberOccupation());
            pstContactList.setString(FLD_HOME_EMAIL, contactlist.getHomeEmail());
            pstContactList.setString(FLD_MEMBER_ID_CARD_NUMBER, contactlist.getMemberIdCardNumber());
            pstContactList.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT, contactlist.getCurrencyTypeIdMemberConsigmentLimit());
            pstContactList.setFloat(FLD_MEMBER_CONSIGMENT_LIMIT, contactlist.getMemberConsigmentLimit());
            pstContactList.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT, contactlist.getCurrencyTypeIdMemberCreditLimit());
            pstContactList.setFloat(FLD_MEMBER_CREDIT_LIMIT, contactlist.getMemberCreditLimit());
            pstContactList.setInt(FLD_DAY_OF_PAYMENT, contactlist.getDayOfPayment());  
            pstContactList.setString(FLD_COMP_STATE, contactlist.getCompState());
            pstContactList.setString(FLD_COMP_EMAIL, contactlist.getCompEmail());
            pstContactList.setString(FLD_POSTAL_CODE, contactlist.getPostalCode());
            pstContactList.setString(FLD_HOME_CITY, contactlist.getHomeCity());
            
            //added by dewok 2019-10-07 for duty free
            pstContactList.setString(FLD_PASSPORT_NO, contactlist.getPassportNo());
            
            pstContactList.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstContactList.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            contactlist.setOID(pstContactList.getlong(FLD_CONTACT_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstContactList.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
        return contactlist.getOID();
    }

    public static long updateExc(ContactList contactlist) throws DBException {
        try {
            if (contactlist.getOID() != 0) {
                PstContactList pstContactList = new PstContactList(contactlist.getOID());

                pstContactList.setString(FLD_CONTACT_CODE, contactlist.getContactCode());
                pstContactList.setLong(FLD_EMPLOYEE_ID, contactlist.getEmployeeId());
                pstContactList.setLong(FLD_PARENT_ID, contactlist.getParentId());
                pstContactList.setInt(FLD_CONTACT_TYPE, contactlist.getContactType());
                pstContactList.setDate(FLD_REGDATE, contactlist.getRegdate());
                pstContactList.setString(FLD_COMP_NAME, contactlist.getCompName());
                pstContactList.setString(FLD_PERSON_NAME, contactlist.getPersonName());
                pstContactList.setString(FLD_PERSON_LASTNAME, contactlist.getPersonLastname());
                pstContactList.setString(FLD_BUSS_ADDRESS, contactlist.getBussAddress());
                pstContactList.setString(FLD_TOWN, contactlist.getTown());
                pstContactList.setString(FLD_PROVINCE, contactlist.getProvince());
                pstContactList.setString(FLD_COUNTRY, contactlist.getCountry());
                pstContactList.setString(FLD_TELP_NR, contactlist.getTelpNr());
                pstContactList.setString(FLD_TELP_MOBILE, contactlist.getTelpMobile());
                pstContactList.setString(FLD_FAX, contactlist.getFax());
                pstContactList.setString(FLD_HOME_ADDR, contactlist.getHomeAddr());
                pstContactList.setString(FLD_HOME_TOWN, contactlist.getHomeTown());
                pstContactList.setString(FLD_HOME_PROVINCE, contactlist.getHomeProvince());
                pstContactList.setString(FLD_HOME_COUNTRY, contactlist.getHomeCountry());
                pstContactList.setString(FLD_HOME_TELP, contactlist.getHomeTelp());
                pstContactList.setString(FLD_HOME_FAX, contactlist.getHomeFax());
                pstContactList.setString(FLD_NOTES, contactlist.getNotes());
                pstContactList.setString(FLD_BANK_ACC, contactlist.getBankAcc());
                pstContactList.setString(FLD_BANK_ACC2, contactlist.getBankAcc2());
                pstContactList.setString(FLD_EMAIL, contactlist.getEmail());
                pstContactList.setString(FLD_DIRECTIONS, contactlist.getDirections());
                pstContactList.setDate(FLD_MEMBER_LAST_UPDATE, contactlist.getLastUpdate());
                pstContactList.setInt(FLD_PROCESS_STATUS, contactlist.getProcessStatus());

                //new
                pstContactList.setString(FLD_COMPANY_BANK_ACC, contactlist.getCompanyBankAcc());
                pstContactList.setString(FLD_POSITION_PERSON, contactlist.getPositionPerson());
                pstContactList.setString(FLD_POSTAL_CODE_COMPANY, contactlist.getPostalCodeCompany());
                pstContactList.setString(FLD_WEBSITE_COMPANY, contactlist.getWebsiteCompany());
                pstContactList.setString(FLD_EMAIL_COMPANY, contactlist.getEmailCompany());
                pstContactList.setString(FLD_POSTAL_CODE_HOME, contactlist.getPostalCodeHome());
                pstContactList.setLong(FLD_LOCATION_ID, contactlist.getLocationId());

                //new
                pstContactList.setInt(FLD_TERM_OF_DELIVERY, contactlist.getTermOfDelivery());
                // add by fitra
                pstContactList.setString(FLD_COMP_COUNTRY, contactlist.getCompCountry());
                pstContactList.setString(FLD_COMP_PROVINCE, contactlist.getCompProvince());
                pstContactList.setString(FLD_COMP_REGENCY, contactlist.getCompRegency());
                pstContactList.setString(FLD_HOME_MOBILE_PHONE, contactlist.getHomeMobilePhone());
                pstContactList.setString(FLD_TITLE, contactlist.getTitle());
                pstContactList.setString(FLD_HOME_STATE, contactlist.getHomeState());
                pstContactList.setDate(FLD_MEMBER_BIRTH_DATE, contactlist.getMemberBirthDate());
                pstContactList.setInt(FLD_MEMBER_SEX, contactlist.getMemberSex());
                pstContactList.setLong(FLD_MEMBER_RELIGION_ID, contactlist.getMemberReligionId());
                pstContactList.setString(FLD_NATIONALITY, contactlist.getNationality());
                pstContactList.setString(FLD_MEMBER_OCCUPATION, contactlist.getMemberOccupation());
                pstContactList.setString(FLD_HOME_EMAIL, contactlist.getHomeEmail());
                pstContactList.setString(FLD_MEMBER_ID_CARD_NUMBER, contactlist.getMemberIdCardNumber());
                pstContactList.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT, contactlist.getCurrencyTypeIdMemberConsigmentLimit());
                pstContactList.setFloat(FLD_MEMBER_CONSIGMENT_LIMIT, contactlist.getMemberConsigmentLimit());
                pstContactList.setLong(FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT, contactlist.getCurrencyTypeIdMemberCreditLimit());
                pstContactList.setFloat(FLD_MEMBER_CREDIT_LIMIT, contactlist.getMemberCreditLimit());
                pstContactList.setInt(FLD_DAY_OF_PAYMENT, contactlist.getDayOfPayment());
                pstContactList.setString(FLD_COMP_STATE, contactlist.getCompState());
                pstContactList.setString(FLD_COMP_EMAIL, contactlist.getCompEmail());
                pstContactList.setString(FLD_POSTAL_CODE, contactlist.getPostalCode());
                pstContactList.setString(FLD_HOME_CITY, contactlist.getHomeCity());
                
                pstContactList.setString(FLD_PASSPORT_NO, contactlist.getPassportNo());
                pstContactList.update();

                long oidDataSync = PstDataSyncSql.insertExc(pstContactList.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstContactList.getUpdateSQL());
                return contactlist.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstContactList pstContactList = new PstContactList(oid);
            pstContactList.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstContactList.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstContactList.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
        return oid;
    }


    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CONTACT_LIST;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ContactList contactlist = new ContactList();
                resultToObject(rs, contactlist);
                lists.add(contactlist);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static void resultToObject(ResultSet rs, ContactList contactlist) {
        try {
            contactlist.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
            contactlist.setContactCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]));
            contactlist.setEmployeeId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID]));
            contactlist.setParentId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_PARENT_ID]));
            contactlist.setContactType(rs.getInt(PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]));
            contactlist.setRegdate(rs.getDate(PstContactList.fieldNames[PstContactList.FLD_REGDATE]));
            contactlist.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
            contactlist.setPersonName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
            contactlist.setPersonLastname(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME]));
            contactlist.setBussAddress(rs.getString(PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS]));
            contactlist.setTown(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TOWN]));
            contactlist.setProvince(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PROVINCE]));
            contactlist.setCountry(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COUNTRY]));
            contactlist.setTelpNr(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TELP_NR]));
            contactlist.setTelpMobile(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TELP_MOBILE]));
            contactlist.setFax(rs.getString(PstContactList.fieldNames[PstContactList.FLD_FAX]));
            contactlist.setHomeAddr(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_ADDR]));
            contactlist.setHomeTown(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_TOWN]));
            contactlist.setHomeProvince(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_PROVINCE]));
            contactlist.setHomeCountry(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_COUNTRY]));
            contactlist.setHomeTelp(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_TELP]));
            contactlist.setHomeFax(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_FAX]));
            contactlist.setNotes(rs.getString(PstContactList.fieldNames[PstContactList.FLD_NOTES]));
            contactlist.setBankAcc(rs.getString(PstContactList.fieldNames[PstContactList.FLD_BANK_ACC]));
            contactlist.setBankAcc2(rs.getString(PstContactList.fieldNames[PstContactList.FLD_BANK_ACC2]));
            contactlist.setEmail(rs.getString(PstContactList.fieldNames[PstContactList.FLD_EMAIL]));
            contactlist.setDirections(rs.getString(PstContactList.fieldNames[PstContactList.FLD_DIRECTIONS]));
            contactlist.setLastUpdate(rs.getDate(PstContactList.fieldNames[PstContactList.FLD_MEMBER_LAST_UPDATE]));
            contactlist.setProcessStatus(rs.getInt(PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]));

            //new
            contactlist.setCompanyBankAcc(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMPANY_BANK_ACC]));
            contactlist.setPositionPerson(rs.getString(PstContactList.fieldNames[PstContactList.FLD_POSITION_PERSON]));
            contactlist.setPostalCodeCompany(rs.getString(PstContactList.fieldNames[PstContactList.FLD_POSTAL_CODE_COMPANY]));
            contactlist.setWebsiteCompany(rs.getString(PstContactList.fieldNames[PstContactList.FLD_WEBSITE_COMPANY]));
            contactlist.setEmailCompany(rs.getString(PstContactList.fieldNames[PstContactList.FLD_EMAIL_COMPANY]));
            contactlist.setPostalCodeHome(rs.getString(PstContactList.fieldNames[PstContactList.FLD_POSTAL_CODE_HOME]));
            contactlist.setLocationId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_LOCATION_ID]));

            //new
            contactlist.setTermOfDelivery(rs.getInt(PstContactList.fieldNames[PstContactList.FLD_TERM_OF_DELIVERY]));
            contactlist.setCompanyBankAcc(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMPANY_BANK_ACC]));
            //add by fitra
            contactlist.setCompCountry(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_COUNTRY]));
            contactlist.setCompProvince(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_PROVINCE]));
            contactlist.setCompRegency(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_REGENCY]));
            contactlist.setHomeMobilePhone(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_MOBILE_PHONE]));
            contactlist.setTitle(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TITLE]));
            contactlist.setHomeState(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_STATE]));
            contactlist.setMemberBirthDate(rs.getDate(PstContactList.fieldNames[PstContactList.FLD_MEMBER_BIRTH_DATE]));
            contactlist.setMemberSex(rs.getInt(PstContactList.fieldNames[PstContactList.FLD_MEMBER_SEX]));
            contactlist.setMemberReligionId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_MEMBER_RELIGION_ID]));
            contactlist.setNationality(rs.getString(PstContactList.fieldNames[PstContactList.FLD_NATIONALITY]));
            contactlist.setMemberOccupation(rs.getString(PstContactList.fieldNames[PstContactList.FLD_MEMBER_OCCUPATION]));
            contactlist.setHomeEmail(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_EMAIL]));
            contactlist.setMemberIdCardNumber(rs.getString(PstContactList.fieldNames[PstContactList.FLD_MEMBER_ID_CARD_NUMBER]));
            contactlist.setCurrencyTypeIdMemberConsigmentLimit(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT]));
            contactlist.setMemberConsigmentLimit(rs.getFloat(PstContactList.fieldNames[PstContactList.FLD_MEMBER_CONSIGMENT_LIMIT]));
            contactlist.setCurrencyTypeIdMemberCreditLimit(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT]));
            contactlist.setMemberCreditLimit(rs.getFloat(PstContactList.fieldNames[PstContactList.FLD_MEMBER_CREDIT_LIMIT]));
            contactlist.setDayOfPayment(rs.getInt(PstContactList.fieldNames[PstContactList.FLD_DAY_OF_PAYMENT]));
            contactlist.setCompState(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_STATE]));
            contactlist.setCompEmail(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_EMAIL]));
            contactlist.setPostalCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_POSTAL_CODE]));
            contactlist.setHomeCity(rs.getString(PstContactList.fieldNames[PstContactList.FLD_HOME_CITY]));
            contactlist.setPassportNo(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PASSPORT_NO]));

        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        }
    }

    public static boolean checkOID(long contactId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CONTACT_LIST + " WHERE " +
                    PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = " + contactId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ") FROM " + TBL_CONTACT_LIST;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static boolean cekCodeContact(String code, long oid) {
        DBResultSet dbrs = null;
        boolean bool = false;
        try {
            String sql = "SELECT " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    " FROM " + TBL_CONTACT_LIST +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    " = '" + code + "'" +
                    " AND " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " <> " + oid;
//            System.out.println("---------------- sql cekCodeContact : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                bool = true;
            }
            rs.close();

        } catch (Exception e) {
            return true;
        } finally {
            DBResultSet.close(dbrs);

        }
        return bool;
    }

    /**
     * mencari kode kontak apah sudah ada
     * @param code = kode kontak baru
     * @return oidContact
     */
    public static long cekCodeContact(String code) {
        DBResultSet dbrs = null;
        long oid = 0;
        try {
            String sql = "SELECT " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + TBL_CONTACT_LIST +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    " = '" + code + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();

        } catch (Exception e) {
            return oid;
        } finally {
            DBResultSet.close(dbrs);
        }
        return oid;
    }

    /* This method used to find current data */
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
                    ContactList contactlist = (ContactList) list.get(ls);
                    if (oid == contactlist.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    public static Vector getListContact(Vector vectName, Vector vectType, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT DISTINCT " +
                    " CNT." + fieldNames[FLD_CONTACT_ID] + // CONTACT_ID
                    " ,CNT." + fieldNames[FLD_CONTACT_CODE] + //CONTACT_CODE
                    " ,CNT." + fieldNames[FLD_COMP_NAME] + //COMP_NAME
                    " ,CNT." + fieldNames[FLD_PERSON_NAME] + //PERSON_NAME
                    " ,CNT." + fieldNames[FLD_BUSS_ADDRESS] + //BUSS_ADDRESS
                    " ,CNT." + fieldNames[FLD_TOWN] + //TOWN
                    " ,CNT." + fieldNames[FLD_PROVINCE] + //PROVINCE
                    " ,CNT." + fieldNames[FLD_COUNTRY] + //COUNTRY
                    " ,CNT." + fieldNames[FLD_TELP_NR] + //TELP_NR
                    " ,CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + //CLASS_TYPE
                    " FROM " + TBL_CONTACT_LIST + " AS CNT, " +
                    " " + PstContactClass.TBL_CONTACT_CLASS + " AS CLASS, " +
                    " " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASSIGN " +
                    " WHERE CNT." + fieldNames[FLD_CONTACT_ID] + " = ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " AND ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " = CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];

            String strType = "";
            if (vectType != null && vectType.size() > 0) {
                for (int i = 0; i < vectType.size(); i++) {
                    if (strType.length() < 1) {
                        strType = " (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + vectType.get(i) + ")";
                    } else {
                        strType = strType + " OR (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + vectType.get(i) + ")";
                    }
                    strType = "(" + strType + ")";
                }
            }

            String strName = "";
            if (vectName != null && vectName.size() > 0) {
                for (int a = 0; a < vectName.size(); a++) {
                    if (strName.length() < 1) {
                        strName = " (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + ((String) vectName.get(a)).trim() + "%' " +
                                " OR CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + ((String) vectName.get(a)).trim() + "%')";
                    } else {
                        strName = strName + " OR (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + vectName.get(a) + "%' " +
                                " OR CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + ((String) vectName.get(a)).trim() + "%')";
                    }
                    strName = "(" + strName + ")";
                }
            }

            if (strType.length() > 0)
                sql = sql + " AND " + strType;

            if (strName.length() > 0)
                sql = sql + " AND " + strName;

            sql = sql + " ORDER BY CNT." + fieldNames[FLD_PERSON_NAME];

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }


            System.out.println(sql);


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                ContactList cntList = new ContactList();
                ContactClass cntClass = new ContactClass();
                Vector vt = new Vector(1, 1);

                cntList.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                cntList.setContactCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]));
                cntList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                cntList.setPersonName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
                cntList.setBussAddress(rs.getString(PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS]));
                cntList.setTown(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TOWN]));
                cntList.setProvince(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PROVINCE]));
                cntList.setCountry(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COUNTRY]));
                cntList.setTelpNr(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TELP_NR]));
                vt.add(cntList);
                //---contact class
                cntClass.setClassType(rs.getInt(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]));
                vt.add(cntClass);

                result.add(vt);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public static int getCountListContact(Vector vectName, Vector vectType) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = /*"SELECT COUNT(DISTINCT "+
            " CNT."+fieldNames[FLD_CONTACT_ID]+ // CONTACT_ID
            " ,CNT."+fieldNames[FLD_CONTACT_CODE]+ //CONTACT_CODE
            " ,CNT."+fieldNames[FLD_COMP_NAME]+ //COMP_NAME
            " ,CNT."+fieldNames[FLD_BUSS_ADDRESS]+ //BUSS_ADDRESS
            " ,CNT."+fieldNames[FLD_TOWN]+ //TOWN
            " ,CNT."+fieldNames[FLD_PROVINCE]+ //PROVINCE
            " ,CNT."+fieldNames[FLD_COUNTRY]+ //COUNTRY
            " ,CNT."+fieldNames[FLD_TELP_NR]+ //TELP_NR
            " ,CLASS."+PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+ //CLASS_TYPE
            ") AS CNT
             */
                    " SELECT COUNT(CNT." + fieldNames[FLD_CONTACT_ID] + ")" +
                    " FROM " + TBL_CONTACT_LIST + " AS CNT, " +
                    " " + PstContactClass.TBL_CONTACT_CLASS + " AS CLASS, " +
                    " " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASSIGN " +
                    " WHERE CNT." + fieldNames[FLD_CONTACT_ID] + " = ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " AND ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " = CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];

            String strType = "";
            if (vectType != null && vectType.size() > 0) {
                for (int i = 0; i < vectType.size(); i++) {
                    if (strType.length() < 1) {
                        strType = " (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + vectType.get(i) + ")";
                    } else {
                        strType = strType + " OR (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + vectType.get(i) + ")";
                    }
                    strType = "(" + strType + ")";
                }
            }

            String strName = "";
            if (vectName != null && vectName.size() > 0) {
                /*for(int a=0;a<vectName.size();a++){
                    if(strName.length()<1){
                        strName = " (CNT."+fieldNames[FLD_COMP_NAME]+" LIKE '%"+((String)vectName.get(a)).trim()+"%')";
                    }else{
                        strName = strName + " OR (CNT."+fieldNames[FLD_COMP_NAME]+" LIKE '%"+((String)vectName.get(a)).trim()+"%')";
                    }
                    strName = "("+strName+")";
                }
                 */
                for (int a = 0; a < vectName.size(); a++) {
                    if (strName.length() < 1) {
                        strName = " (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + ((String) vectName.get(a)).trim() + "%' " +
                                " OR CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + ((String) vectName.get(a)).trim() + "%')";
                    } else {
                        strName = strName + " OR (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + vectName.get(a) + "%' " +
                                " OR CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + ((String) vectName.get(a)).trim() + "%')";
                    }
                    strName = "(" + strName + ")";
                }
            }

            if (strType.length() > 0)
                sql = sql + " AND " + strType;

            if (strName.length() > 0)
                sql = sql + " AND " + strName;


            System.out.println("----- " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //count = rs.getInt("CNT");
                count = rs.getInt(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return count;
    }

    /**
     * list contact depent on contactType, contactCode and contactName
     * modifield by : gedhy
     * modifield at : July 08, 2003 13:30 pm
     */
    public static Vector getListContact(Vector vectType, Vector vectCode, Vector vectName, int start, int recordToGet, String sortBy) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT DISTINCT CNT." + fieldNames[FLD_CONTACT_ID] +
                    " ,CNT." + fieldNames[FLD_CONTACT_CODE] +
                    " ,CNT." + fieldNames[FLD_COMP_NAME] +
                    " ,CNT." + fieldNames[FLD_PERSON_NAME] +
                    " ,CNT." + fieldNames[FLD_PERSON_LASTNAME] +
                    " ,CNT." + fieldNames[FLD_BUSS_ADDRESS] +
                    " ,CNT." + fieldNames[FLD_TOWN] +
                    " ,CNT." + fieldNames[FLD_PROVINCE] +
                    " ,CNT." + fieldNames[FLD_COUNTRY] +
                    " ,CNT." + fieldNames[FLD_TELP_NR] +
                    " ,CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " FROM " + TBL_CONTACT_LIST + " AS CNT, " +
                    " " + PstContactClass.TBL_CONTACT_CLASS + " AS CLASS, " +
                    " " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASSIGN " +
                    " WHERE CNT." + fieldNames[FLD_CONTACT_ID] + " = ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " AND ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " = CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];

            String strType = "";
            if (vectType != null && vectType.size() > 0) {
                for (int i = 0; i < vectType.size(); i++) {
                    if (strType.length() < 1) {
                        strType = " (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = " + vectType.get(i) + ")";
                    } else {
                        strType = strType + " OR (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = " + vectType.get(i) + ")";
                    }
                    strType = "(" + strType + ")";
                }
            }

            String strCode = "";
            if (vectCode != null && vectCode.size() > 0) {
                for (int a = 0; a < vectCode.size(); a++) {
                    if (strCode.length() < 1) {
                        strCode = " (CNT." + fieldNames[FLD_CONTACT_CODE] + " LIKE '%" + vectCode.get(a) + "%')";
                    } else {
                        strCode = strCode + " OR (CNT." + fieldNames[FLD_CONTACT_CODE] + " LIKE '%" + vectCode.get(a) + "%')";
                    }
                    strCode = "(" + strCode + ")";
                }
            }

            String strName = "";
            if (vectName != null && vectName.size() > 0) {
                for (int a = 0; a < vectName.size(); a++) {
                    if (strName.length() < 1) {
                        strName = " (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_LASTNAME] + " LIKE '%" + vectName.get(a) + "%')";
                    } else {
                        strName = strName + " OR (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_LASTNAME] + " LIKE '%" + vectName.get(a) + "%')";
                    }
                    strName = "(" + strName + ")";
                }
            }

            if (strType.length() > 0)
                sql = sql + " AND " + strType;

            if (strCode.length() > 0)
                sql = sql + " AND " + strCode;

            if (strName.length() > 0)
                sql = sql + " AND " + strName;


            if (sortBy.length() > 0) {
                sql = sql + " ORDER BY " + sortBy;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }



//            System.out.println("PstContactList.getListContact() sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ContactList cntList = new ContactList();
                ContactClass cntClass = new ContactClass();
                Vector vt = new Vector(1, 1);

                cntList.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                cntList.setContactCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]));
                cntList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                cntList.setPersonName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
                cntList.setPersonLastname(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME]));
                cntList.setBussAddress(rs.getString(PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS]));
                cntList.setTown(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TOWN]));
                cntList.setProvince(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PROVINCE]));
                cntList.setCountry(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COUNTRY]));
                cntList.setTelpNr(rs.getString(PstContactList.fieldNames[PstContactList.FLD_TELP_NR]));
                vt.add(cntList);

                //---contact class
                cntClass.setClassType(rs.getInt(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]));
                vt.add(cntClass);

                result.add(vt);
            }
        } catch (Exception e) {
            System.out.println("PstContactList.getListContact() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public static int getCountListContact(Vector vectType, Vector vectCode, Vector vectName) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(CNT." + fieldNames[FLD_CONTACT_ID] +
                    ") AS CNT FROM " + TBL_CONTACT_LIST + " AS CNT, " +
                    " " + PstContactClass.TBL_CONTACT_CLASS + " AS CLASS, " +
                    " " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASSIGN " +
                    " WHERE CNT." + fieldNames[FLD_CONTACT_ID] + " = ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " AND ASSIGN." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " = CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];

            String strType = "";
            if (vectType != null && vectType.size() > 0) {
                for (int i = 0; i < vectType.size(); i++) {
                    if (strType.length() < 1) {
                        strType = " (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = " + vectType.get(i) + ")";
                    } else {
                        strType = strType + " OR (CLASS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = " + vectType.get(i) + ")";
                    }
                    strType = "(" + strType + ")";
                }
            }

            String strCode = "";
            if (vectCode != null && vectCode.size() > 0) {
                for (int a = 0; a < vectCode.size(); a++) {
                    if (strCode.length() < 1) {
                        strCode = " (CNT." + fieldNames[FLD_CONTACT_CODE] + " LIKE '%" + vectCode.get(a) + "%')";
                    } else {
                        strCode = strCode + " OR (CNT." + fieldNames[FLD_CONTACT_CODE] + " LIKE '%" + vectCode.get(a) + "%')";
                    }
                    strCode = "(" + strCode + ")";
                }
            }

            String strName = "";
            if (vectName != null && vectName.size() > 0) {
                for (int a = 0; a < vectName.size(); a++) {
                    if (strName.length() < 1) {
                        strName = " (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_LASTNAME] + " LIKE '%" + vectName.get(a) + "%')";
                    } else {
                        strName = strName + " OR (CNT." + fieldNames[FLD_COMP_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_NAME] + " LIKE '%" + vectName.get(a) + "%')" +
                                " OR (CNT." + fieldNames[FLD_PERSON_LASTNAME] + " LIKE '%" + vectName.get(a) + "%')";
                    }
                    strName = "(" + strName + ")";
                }
            }

            if (strType.length() > 0)
                sql = sql + " AND " + strType;

            if (strCode.length() > 0)
                sql = sql + " AND " + strCode;

            if (strName.length() > 0)
                sql = sql + " AND " + strName;

//            System.out.println("PstContactList.getCountListContact() sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("CNT");
            }
        } catch (Exception e) {
            System.out.println("PstContactList.getCountListContact() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return count;
    }


    /**
     * used to get contactId specify by parameter below
     * @param <CODE>contactCode</CODE>selected contactCode
     * @param <CODE>contactName</CODE>selected contactName
     * @return <CODE>OID</CODE>of contact
     */
    public static long getContactIdByCodeName(String contactCode, String contactName) {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_CONTACT_ID] +
                    " FROM " + TBL_CONTACT_LIST +
                    " WHERE " + fieldNames[FLD_CONTACT_CODE] + "='" + contactCode + "'" +
                    " AND " + fieldNames[FLD_PERSON_NAME] + "='" + contactName + "'";

//            System.out.println("PstContactList.getContactIdByCodeName() sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (Exception e) {
            System.out.println("PstContactList.getContactIdByCodeName() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }


    public static ContactList getContactList(String where) {
        Vector v = PstContactList.list(0, 0, where, null);
        ContactList cl = new ContactList();
        if (v != null && v.size() > 0) {
            cl = (ContactList) v.get(0);
        }

        return cl;
    }


    /**
     * @param idContact
     * @return
     * @created by Edhy
     */
    public static ContactClass getContactClassType(long idContact) {
        DBResultSet dbrs = null;
        ContactClass contactClass = new ContactClass();
        try {
            String sql = "SELECT CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    ", CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    ", CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME] +
                    ", CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION] +
                    " FROM " + PstContactList.TBL_CONTACT_LIST + " AS CONT " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +
                    " ON CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " WHERE CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = " + idContact;

//            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                contactClass.setOID(rs.getLong(1));
                contactClass.setClassType(rs.getInt(2));
                contactClass.setClassName(rs.getString(3));
                contactClass.setClassDescription(rs.getString(4));
            }
        } catch (Exception e) {
            return new ContactClass();
        } finally {
            DBResultSet.close(dbrs);
            return contactClass;
        }
    }


    /**
     * @param whereClause
     * @return
     * @created by Edhy
     */
    public static int getCountContactByClassType(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ") FROM " + TBL_CONTACT_LIST + " AS CONT " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +
                    " ON CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID];

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

//            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /**
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     * @created by Edhy
     */
    public static Vector listContactByClassType(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT CONT.* FROM " + TBL_CONTACT_LIST + " AS CONT " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +
                    " ON CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID];

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            /*if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;*/

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

//            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ContactList contact = new ContactList();
                resultToObject(rs, contact);
                lists.add(contact);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    /**
     * supplier internal
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return 
     */
      public static Vector listContactByClassTypeWarehouse(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT CONT.* FROM " + TBL_CONTACT_LIST + " AS CONT " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASS " +
                    " ON CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " = ASS." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+
                    " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS LC " +
                    " ON CONT." + PstContactList.fieldNames[PstContactList.FLD_LOCATION_ID] +
                    " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];;

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            /*if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;*/

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

//            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ContactList contact = new ContactList();
                resultToObject(rs, contact);
                lists.add(contact);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    /**
     * update oid lama dengan oid baru
     * @param newOID
     * @param originalOID
     * @throws DBException
     * @return
     */
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_CONTACT_LIST +
                    " SET " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = " + originalOID +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " = " + newOID;

            DBHandler.execUpdate(sql);
            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }




    /*-------------------------------- EMPLOYEE -------------------------------*/
    /**
     * This method used to fetch employee from contact
     */
    public static boolean fetchEmployeeInContact(long employeeId) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT  * FROM " + TBL_CONTACT_LIST +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + "=" + employeeId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                return true;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
        return false;
    }


    /**
     * This method used to fetch employee from contact
     */
    public static long getContactIdOfEmployee(long employeeId) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + TBL_CONTACT_LIST +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + "=" + employeeId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                return rs.getLong(1);
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
        return 0;
    }

    
     public static int getDayOfPaymentOfSupplier(long supplierId) throws DBException {
        DBResultSet dbrs = null;
        int dayOfPayment=0;
        try {
            String sql = "SELECT " + PstContactList.fieldNames[PstContactList.FLD_DAY_OF_PAYMENT] +
                    " FROM " + TBL_CONTACT_LIST +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + supplierId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                dayOfPayment=rs.getInt(1);
                return dayOfPayment;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
        return dayOfPayment;
    }
    
    /**
     * This method used to invoke employee to contact
     */
    /*
    public static long insertEmployeeToContact(Employee employee) throws DBException{
        ContactList contactlist = new ContactList();
        try{
            PstContactList pstContactList = new PstContactList(0);

            pstContactList.setString(FLD_CONTACT_CODE, employee.getEmployeeNum());
            pstContactList.setLong(FLD_EMPLOYEE_ID, employee.getOID());
            pstContactList.setInt(FLD_CONTACT_TYPE, EMPLOYEE);
            pstContactList.setDate(FLD_REGDATE, employee.getCommencingDate());
            pstContactList.setString(FLD_PERSON_NAME, employee.getFullName());
            pstContactList.setString(FLD_BUSS_ADDRESS, employee.getAddress());
            pstContactList.setString(FLD_TELP_NR, employee.getPhone());
            pstContactList.setString(FLD_TELP_MOBILE, employee.getHandphone());

            pstContactList.insert();
            contactlist.setOID(pstContactList.getlong(FLD_CONTACT_ID));

            if(contactlist.getOID()!=0){
                long classId = 0;
                String whClause = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+"="+PstContactClass.CONTACT_TYPE_EMPLOYEE;
                Vector vectClass = PstContactClass.list(0,0,whClause,"");
                if(vectClass!=null && vectClass.size()>0){
                    ContactClass cntCls = (ContactClass) vectClass.get(0);
                    classId = cntCls.getOID();
                }

                PstContactClassAssign pstClassAssign = new PstContactClassAssign();
                ContactClassAssign classAssign = new ContactClassAssign();
                classAssign.setContactId(contactlist.getOID());
                classAssign.setContactClassId(classId);
                pstClassAssign.insertExc(classAssign);
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstContactList(0),DBException.UNKNOWN);
        }
        return contactlist.getOID();
    }
     **/

    /**
     * This method used to update employee to contact
     */
    /*
    public static long updateEmployeeInContact(Employee employee) throws DBException{
        DBResultSet dbrs = null;
        try{
            String strCommDate = "\"" + Formater.formatDate(employee.getCommencingDate(), "yyyy-MM-dd") + "\"";
            String sql = "UPDATE " + TBL_CONTACT_LIST +
            " SET " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] + "='" + employee.getEmployeeNum() + "'," +
            PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + "=" + employee.getOID() + ", " +
            PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE] + "=" + EMPLOYEE + ", " +
            PstContactList.fieldNames[PstContactList.FLD_REGDATE] + "=" + strCommDate + ", " +
            PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + "='" + employee.getFullName() + "', " +
            PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS] + "='" + employee.getAddress() + "', " +
            PstContactList.fieldNames[PstContactList.FLD_TELP_NR] + "='" + employee.getPhone() + "', " +
            PstContactList.fieldNames[PstContactList.FLD_TELP_MOBILE] + "='" + employee.getPhone() + "'" +
            " WHERE " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + "=" + employee.getOID();
            dbrs = DBHandler.execQueryResult(sql);
            return employee.getOID();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstContactList(0),DBException.UNKNOWN);
        }
    }
    */

    /**
     * This method used to delete employee from contact
     */
    public static long deleteEmployeeFromContact(long employeeId) throws DBException {
        DBResultSet dbrs = null;
        try {
            long contactId = 0;
            String whereClause = PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + "=" + employeeId;
            Vector vectEmp = PstContactList.list(0, 0, whereClause, "");
            if (vectEmp != null && vectEmp.size() > 0) {
                ContactList contactList = (ContactList) vectEmp.get(0);
                contactId = contactList.getOID();
            }

            PstContactClassAssign.deleteClassAssign(contactId);

            String sql = "DELETE FROM " + TBL_CONTACT_LIST +
                    " WHERE " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + "=" + employeeId;
            dbrs = DBHandler.execQueryResult(sql);
            return employeeId;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactList(0), DBException.UNKNOWN);
        }
    }


    /**
     * used to update objects' contact with specifid one
     * @param <CODE>vectUsed</CODE>vector of object that will be update
     * @param <CODE>vectSelected</CODE>vector of object usd to update
     * @return vector of object contact exist after update process
     */
    public static Vector deduplicateContact(Vector vectUsed, Vector vectSelected) {
        Vector result = new Vector(1, 1);
        /*if(vectUsed!=null && vectUsed.size()>0){
            String strIn = "";
            String strDel = "";
            long idUsed = ((Long)vectUsed.get(0)).longValue();
            if(vectSelected!=null && vectSelected.size()>0){
                int maxSelected = vectSelected.size();
                for(int i=0; i<maxSelected; i++){
                    String strIterate = String.valueOf(vectSelected.get(i));
                    strIn = strIn + strIterate + ",";

                    if(!String.valueOf(idUsed).equals(strIterate)){
                        strDel = strDel + strIterate + ",";
                    }
                }

                if(strIn.length()>0){
                    strIn = strIn.substring(0,strIn.length()-1);
                }

                if(strDel.length()>0){
                    strDel = strDel.substring(0,strDel.length()-1);
                }

                System.out.println("strIn : "+strIn);
                System.out.println("strDel : "+strDel);
                PstJurnalUmum.DeduplicateJuContact(idUsed,strIn);
                PstBpPiutang.DeduplicateBpContact(idUsed,strIn);
                PstContactClassAssign.DeduplicateContact(strDel);

                try{
                    ContactList cont = fetchExc(idUsed);
                    result.add(cont);
                }catch(Exception e){
                    System.out.println("Exc on deduplicate : "+e.toString());
                }
            }
        } */
        return result;
    }
    
    public static ContactList fetchByCode(String code) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        ContactList contact = new ContactList();
        try {
            String sql = "SELECT * FROM " + TBL_CONTACT_LIST +
                    " WHERE " + fieldNames[FLD_CONTACT_CODE] +
                    " = '" + code + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, contact);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return contact;
    }
    /*-------------------------------- EMPLOYEE -------------------------------*/
}
