/* 
 * Form Name  	:  FrmLocation.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: lkarunia
 * @version  	: 01 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.form.location;

/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.common.entity.location.Location;

public class FrmLocation extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Location location;

    public static final String FRM_NAME_LOCATION = "FRM_NAME_LOCATION";

    public static final int FRM_FIELD_CODE = 0;
    public static final int FRM_FIELD_NAME = 1;
    public static final int FRM_FIELD_ADDRESS = 2;
    public static final int FRM_FIELD_TELEPHONE = 3;
    public static final int FRM_FIELD_FAX = 4;
    public static final int FRM_FIELD_PERSON = 5;
    public static final int FRM_FIELD_EMAIL = 6;
    public static final int FRM_FIELD_WEBSITE = 7;
    public static final int FRM_FIELD_PARENT_LOCATION_ID = 8;
    public static final int FRM_FIELD_CONTACT_ID = 9;
    public static final int FRM_FIELD_TYPE = 10;
    public static final int FRM_FIELD_DESCRIPTION = 11;

    public static final int FRM_FIELD_SERVICE_PERCENT = 12;
    public static final int FRM_FIELD_TAX_PERCENT = 13;
    public static final int FRM_FIELD_DEPARTMENT_ID = 14;
    public static final int FRM_FIELD_USED_VAL = 15;
    public static final int FRM_FIELD_SERVICE_VAL = 16;
    public static final int FRM_FIELD_TAX_VAL = 17;
    public static final int FRM_FIELD_SERVICE_VAL_USD = 18;
    public static final int FRM_FIELD_TAX_VAL_USD = 19;
    public static final int FRM_FIELD_REPORT_GROUP = 20;

    //add opie 13-06-2012
    public static final int FRM_FIELD_TAX_SERVICE_DEFAULT =21;
    public static final int FRM_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER=22;
    //add fitra 29-01-2014
    public static final int FRM_FIELD_COMPANY_ID=23;
    public static final int FRM_FIELD_COMPANY_NAME=24;
    
    //opie-eyek 20151012
    public static final int FRM_FIELD_PRICE_TYPE_ID=25;
    public static final int FRM_FIELD_STANDARTD_ID=26;
    public static final int FRM_FIELD_USE_TABLE=27;
    
    //de Koyo 20160930
    public static final int FRM_FIELD_ACCOUNTING_EMAIL=28;
    public static final int FRM_FIELD_LOCATION_IP=29;
    public static final int FRM_FIELD_SISTEM_ADDRESS_HISTORY_OUTLET = 30;
    
    // dewok++ 2017-03-21
    public static final int FRM_FIELD_OPENING_TIME = 31;
    public static final int FRM_FIELD_CLOSING_TIME = 32;
    
    // dewok++ 2019-02-05
    public static final int FRM_FIELD_DISCOUNT_TYPE_ID = 33;
    public static final int FRM_FIELD_MEMBER_GROUP_ID = 34;
	public static final int FRM_FIELD_MAX_EXCHANGE_DAY = 35;
    
    public static String[] fieldNames = {
            "FRM_FIELD_CODE",
            "FRM_FIELD_NAME",
            "FRM_FIELD_ADDRESS",
            "FRM_FIELD_TELEPHONE",
            "FRM_FIELD_FAX",
            "FRM_FIELD_PERSON",
            "FRM_FIELD_EMAIL",
            "FRM_FIELD_WEBSITE",
            "FRM_FIELD_PARENT_LOCATION_ID",
            "FRM_FIELD_CONTACT_ID",
            "FRM_FIELD_TYPE",
            "FRM_FIELD_DESCRIPTION",

            // ini di pakai untuk hanoman
            "FRM_FIELD_SERVICE_PERCENT",
            "FRM_FIELD_TAX_PERCENT",
            "FRM_FIELD_DEPARTMENT_ID",
            "FRM_FIELD_USED_VAL",
            "FRM_FIELD_SERVICE_VAL",
            "FRM_FIELD_TAX_VAL",
            "FRM_FIELD_SERVICE_VAL_USD",
            "FRM_FIELD_TAX_VAL_USD",
            "FRM_FIELD_REPORT_GROUP",
            
            //add opie 13-06-2012
            "FRM_FIELD_TAX_SERVICE_DEFAULT",
            "FRM_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER",
              //add fitra 29-01-2014
            "COMPANY_ID",
            "COMPANY_NAME",
            "PRICE_TYPE_ID",
            "STANDART_RATE_ID",
            "FRM_FIELD_USE_TABLE",
            
            //add by De Koyo 20160930
            "FRM_FIELD_ACCOUNTING_EMAIL",
            "FRM_FIELD_LOCATION_IP",
            "FRM_FIELD_SISTEM_ADDRESS_HISTORY_OUTLET",
            
            //added by dewok++ 2017-03-21
            "OPENING_TIME",
            "CLOSING_TIME",
            
            //added by dewok++ 2019-02-05
            "FRM_FIELD_DISCOUNT_TYPE_ID",
            "FRM_FIELD_MEMBER_GROUP_ID",
			"FRM_FIELD_MAX_EXCHANGE_DAY"

    };

    public static int[] fieldTypes = {
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_INT,
            TYPE_STRING,
            // ini di pakai untuk hanoman
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_LONG,
            TYPE_INT,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_INT,
            //add opie 13-06-2012
            TYPE_INT,
            TYPE_FLOAT,
              //add fitra 29-01-2014
            TYPE_LONG,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_INT,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            //added by dewok++ 2017-03-21
            TYPE_DATE,
            TYPE_DATE,
            //added by dewok++ 2019-02-05
            TYPE_LONG,
            TYPE_LONG,
			TYPE_INT

    };

    public FrmLocation() {
    }

    public FrmLocation(Location location) {
        this.location = location;
    }

    public FrmLocation(HttpServletRequest request, Location location) {
        super(new FrmLocation(location), request);
        this.location = location;
    }

    public String getFormName() {
        return FRM_NAME_LOCATION;
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

    public Location getEntityObject() {
        return location;
    }

    public void requestEntityObject(Location location) {
        try {
            this.requestParam();
            location.setCode(getString(FRM_FIELD_CODE));
            location.setName(getString(FRM_FIELD_NAME));
            location.setAddress(getString(FRM_FIELD_ADDRESS));
            location.setTelephone(getString(FRM_FIELD_TELEPHONE));
            location.setFax(getString(FRM_FIELD_FAX));
            location.setPerson(getString(FRM_FIELD_PERSON));
            location.setEmail(getString(FRM_FIELD_EMAIL));
            location.setWebsite(getString(FRM_FIELD_WEBSITE));
            location.setParentLocationId(getLong(FRM_FIELD_PARENT_LOCATION_ID));
            location.setContactId(getLong(FRM_FIELD_CONTACT_ID));
            location.setType(getInt(FRM_FIELD_TYPE));
            location.setDescription(getString(FRM_FIELD_DESCRIPTION));

            // tambahan untuk proses di prochain opie 13-06-2012
            location.setServicePersen(getDouble(FRM_FIELD_SERVICE_PERCENT));
            location.setTaxPersen(getDouble(FRM_FIELD_TAX_PERCENT));
            
            // ini di pakai untuk hanoman
            location.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));
            location.setTypeBase(getInt(FRM_FIELD_USED_VAL));
            location.setServiceValue(getDouble(FRM_FIELD_SERVICE_VAL));
            location.setTaxValue(getDouble(FRM_FIELD_TAX_VAL));
            location.setServiceValueUsd(getDouble(FRM_FIELD_SERVICE_VAL_USD));
            location.setTaxValueUsd(getDouble(FRM_FIELD_TAX_VAL_USD));
            location.setReportGroup(getInt(FRM_FIELD_REPORT_GROUP));

            //add opie 13-06-2012
            location.setTaxSvcDefault(getInt(FRM_FIELD_TAX_SERVICE_DEFAULT));
            location.setPersentaseDistributionPurchaseOrder(getDouble(FRM_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER));
              //add fitra 29-01-2014
            location.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
            location.setCompanyName(getString(FRM_FIELD_COMPANY_NAME));
            
            location.setStandarRateId(getLong(FRM_FIELD_STANDARTD_ID));
            location.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
            location.setUseTable(getInt(FRM_FIELD_USE_TABLE));
            //System.out.println("location.getParentLocationId() : " + location.getParentLocationId());
            
            //add by de Koyo 20160930
            location.setAcountingEmail(getString(FRM_FIELD_ACCOUNTING_EMAIL));
            location.setLocationIp(getString(FRM_FIELD_LOCATION_IP));
            location.setSistemAddressHistoryOutlet(getString(FRM_FIELD_SISTEM_ADDRESS_HISTORY_OUTLET));
            
            //added by dewok++ 2017-03-21
            location.setOpeningTime(getDate(FRM_FIELD_OPENING_TIME));
            location.setClosingTime(getDate(FRM_FIELD_CLOSING_TIME));
            
            //added by dewok++ 2019-02-05
            location.setDiscountTypeId(getLong(FRM_FIELD_DISCOUNT_TYPE_ID));
            location.setMemberGroupId(getLong(FRM_FIELD_MEMBER_GROUP_ID));
			location.setMaxExchangeDay(getInt(FRM_FIELD_MAX_EXCHANGE_DAY));
            
        } catch (Exception e) {
            //System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
