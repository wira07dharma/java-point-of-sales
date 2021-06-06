/*
 * FrmContactEmployee.java
 *
 * Created on March 26, 2002, 10:00 AM
 */

/**
 *
 * @author  edarmasusila
 * @version 
 */


package com.dimata.common.form.contact;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
//import com.dimata.qdep.db.*;
import com.dimata.qdep.form.*;
import com.dimata.common.entity.contact.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FrmContactPersonell extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_CONTACT = "CONTACT_PERSONELL";

    public static final int FRM_CONTACT_TYPE				= 0;
    public static final int FRM_CODE 						= 1;
    public static final int FRM_PERSON_NAME 				= 2;
    public static final int FRM_PERSON_LAST_NAME 			= 3;
    public static final int FRM_TELP_MOBILE 				= 4;
    public static final int FRM_HOME_ADDRESS 				= 5;
    public static final int FRM_HOME_TOWN 					= 6;
    public static final int FRM_HOME_PROVINCE 				= 7;
    public static final int FRM_HOME_COUNTRY 				= 8;
    public static final int FRM_HOME_TELP 					= 9;
    public static final int FRM_HOME_FAX 					= 10;
    public static final int FRM_DIRECTION 					= 11;
    public static final int FRM_BANK_ACC1 					= 12;
    public static final int FRM_BANK_ACC2 					= 13;
    public static final int FRM_NOTES 						= 14;
    public static final int FRM_EMAIL						= 15;
    // DATE BELUM JADI
    public static final int FRM_REG_DATE 					= 16;
    public static final int FRM_DIRECTIONS 					= 17;

    public static String[] fieldNames = {
             "PER_CONTACT_TYPE",
             "PER_CODE",
             "PER_PERSON_NAME",
             "PER_PERSON_LASTNAME",
             "PER_TELP_MOBILE",
             "PER_HOME_ADDRESS",
             "PER_HOME_TOWN",
             "PER_HOME_PROVINCE",
             "PER_HOME_COUNTRY",
             "PER_HOME_TELP",
             "PER_HOME_FAX",
             "PER_DIRECTION",
             "PER_BANK_ACC1",
             "PER_BANK_ACC2",
             "PERM_NOTES",
             "PER_EMAIL",
             "PER_REG_DATE",
             "FRM_DIRECTIONS"
    } ;

    public static int[] fieldTypes = {
        	TYPE_INT + ENTRY_REQUIRED,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING + ENTRY_REQUIRED,
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
            TYPE_DATE,
            TYPE_STRING
    };


    private ContactList contactList = new ContactList();

    public FrmContactPersonell()
    { 
    }  
        
    
    public FrmContactPersonell(HttpServletRequest request) {
        super(new FrmContactPersonell(), request);
    }

    public String getFormName() {
        return FRM_CONTACT;
    }    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }    
    
    public String[] getFieldNames() {
        return fieldNames;
    }
        
    
    public int getFieldSize() {
        return fieldNames.length;
    }
       
    public ContactList getEntityObject()
    {
        return contactList;
    }

    public void requestEntityObject(ContactList contactList)
    {        
        try {
            this.requestParam();                    
            contactList.setContactCode(this.getString(FRM_CODE));
            contactList.setContactType(this.getInt(FRM_CONTACT_TYPE));
            /* company data */
            //contact.setCompName(this.getString(FRM_COMPANY_NAME));
            contactList.setPersonName(this.getString(FRM_PERSON_NAME));
            contactList.setPersonLastname(this.getString(FRM_PERSON_LAST_NAME));
            //contact.setBussAddress(this.getString(FRM_BUS_ADDRESS));
            //contact.setTown(this.getString(FRM_TOWN));
            //contact.setProvince(this.getString(FRM_PROVINCE));
            //contact.setCountry(this.getString(FRM_COUNTRY));
            //contact.setTelpNumber(this.getString(FRM_TELP_NUMBER));
            contactList.setTelpMobile(this.getString(FRM_TELP_MOBILE));
            //contact.setFax(this.getString(FRM_TELP_PAX));
            /* home data */
            contactList.setHomeAddr(this.getString(FRM_HOME_ADDRESS));
            contactList.setHomeTown(this.getString(FRM_HOME_TOWN));
            contactList.setHomeProvince(this.getString(FRM_HOME_PROVINCE));
            contactList.setHomeCountry(this.getString(FRM_HOME_COUNTRY));
            contactList.setHomeTelp(this.getString(FRM_HOME_TELP));
            contactList.setHomeFax(this.getString(FRM_HOME_FAX));

            //contactList.setDirections(this.getString(FRM_DIRECTION));
            contactList.setBankAcc(this.getString(FRM_BANK_ACC1));
            contactList.setBankAcc2(this.getString(FRM_BANK_ACC2));
            contactList.setNotes(this.getString(FRM_NOTES));
            contactList.setEmail(this.getString(FRM_EMAIL));
            contactList.setDirections(this.getString(FRM_DIRECTION));

			contactList.setRegdate(new Date());   //==> under construction
            this.contactList = contactList;
        }catch(Exception e) {
            System.out.println("EXC...");
            contactList = new ContactList();
        }       
    }

}
