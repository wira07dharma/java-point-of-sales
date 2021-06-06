/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.Company;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmCompany extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_COMPANY = "FRM_COMPANY";   
   
    public static final int FRM_COMPANY_CODE = 0;
    public static final int FRM_COMPANY_NAME = 1;
    public static final int FRM_PERSON_NAME = 2;
    public static final int FRM_PERSON_LAST_NAME = 3;
    public static final int FRM_BUSS_ADDRESS = 4;
    public static final int FRM_TOWN = 5;
    public static final int FRM_PROVINCE = 6;
    public static final int FRM_COUNTRY = 7;
    public static final int FRM_TELP_NR = 8;
    public static final int FRM_TELP_MOBILE = 9;
    public static final int FRM_FAX = 10;
    public static final int FRM_EMAIL_COMPANY = 11;
    public static final int FRM_POSTAL_CODE = 12;
    public static final int FRM_COMPANY_IMAGE = 13;

    public static String[] fieldNames =
            {
                "FRM_COMPANY_CODE",
                "FRM_COMPANY_NAME",
                "FRM_PERSON_NAME",
                "FRM_PERSON_LAST_NAME",
                "FRM_BUSS_ADDRESS",
                "FRM_TOWN",
                "FRM_PROVINCE",
                "FRM_COUNTRY",
                "FRM_TELP_NR",
                "FRM_TELP_MOBILE",
                "FRM_FAX",
                "FRM_EMAIL_COMPANY",
                "FRM_POSTAL_CODE",
                "FRM_COMPANY_IMAGE"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING + ENTRY_REQUIRED,//0
                TYPE_STRING + ENTRY_REQUIRED,//1
                TYPE_STRING,//2
                TYPE_STRING,//3
                TYPE_STRING,//4
                TYPE_STRING,//5
                TYPE_STRING,//6
                TYPE_STRING,//7
                TYPE_STRING,//8
                TYPE_STRING,//9
                TYPE_STRING,//10
                TYPE_STRING,//11
                TYPE_STRING,//12
                TYPE_STRING
            };

    private Company objCompany;

    public FrmCompany(Company objCompany) {
        this.objCompany = objCompany;
    }

    public FrmCompany(HttpServletRequest request, Company objCompany) {
        super(new FrmCompany(objCompany), request);
        this.objCompany = objCompany;
    }

    public String getFormName() {
        return FRM_COMPANY;
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

    public Company getEntityObject() {
        return objCompany;
    }

    public void requestEntityObject(Company objCompany) {
        try {
            this.requestParam();
                        
            objCompany.setCompanyCode(this.getString(FRM_COMPANY_CODE));
            objCompany.setCompanyName(this.getString(FRM_COMPANY_NAME));
            objCompany.setPersonName(this.getString(FRM_PERSON_NAME));
            objCompany.setPersonLastName(this.getString(FRM_PERSON_LAST_NAME));
            objCompany.setTown(this.getString(FRM_TOWN));
            objCompany.setProvince(this.getString(FRM_PROVINCE));
            objCompany.setCountry(this.getString(FRM_COUNTRY));
            objCompany.setPhoneNr(this.getString(FRM_TELP_NR));
            objCompany.setMobilePh(this.getString(FRM_TELP_MOBILE));
            objCompany.setBussAddress(this.getString(FRM_BUSS_ADDRESS));
            objCompany.setFax(this.getString(FRM_FAX));
            objCompany.setCompEmail(this.getString(FRM_EMAIL_COMPANY));
            objCompany.setPostalCode(this.getString(FRM_POSTAL_CODE));
            //objCompany.setCompImage(this.getString(FRM_COMPANY_IMAGE));
            
            this.objCompany = objCompany;
        } catch (Exception e) {
            objCompany = new Company();
        }
    }
}
