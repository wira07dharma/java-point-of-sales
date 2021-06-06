/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;


import com.dimata.posbo.entity.masterdata.Company;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Acer
 */
public class FrmCompany extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    
     private Company company;
    public static final String FRM_COMPANY = "FRM_COMPANY";
    
 
    public static final int FRM_FLD_COMPANY_ID=0;  
    public static final int FRM_FLD_COMPANY_CODE=1; 
    public static final int FRM_FLD_COMPANY_NAME=2;
    public static final int FRM_FLD_PERSON_NAME=3;
    public static final int FRM_FLD_PERSON_LAST_NAME=4;
    public static final int FRM_FLD_BUSS_ADDRESS=5;  
    public static final int FRM_FLD_TOWN=6; 
    public static final int FRM_FLD_PROVINCE=7;
    public static final int FRM_FLD_COUNTRY=8;
    public static final int FRM_FLD_TELP_NR=9;
    public static final int FRM_FLD_TELP_MOBILE=10;  
    public static final int FRM_FLD_FAX=11; 
    public static final int FRM_FLD_EMAIL_COMPANY=12;
    public static final int FRM_FLD_POSTAL_CODE=13;
     
    public static String[] fieldNames ={
       "FRM_FLD_COMPANY_ID",
        "FRM_FLD_COMPANY_CODE",
        "FRM_FLD_COMPANY_NAME",      
        "FRM_FLD_PERSON_NAME",
        "FRM_FLD_PERSON_LAST_NAME",
        "FRM_FLD_BUS_ADDRESS",
        "FRM_FLD_TOWN",
         "FRM_FLD_PROVINCE",
        "FRM_FLD_COUNTRY",
         "FRM_FLD_TELP_NR",
        "FRM_FLD_TELP_MOBILE",
         "FRM_FLD_FAX",
        "FRM_FLD_EMAIL_COMPANY",
         "FRM_FLD_POSTAL_CODE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
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
        TYPE_STRING,
        TYPE_STRING,
    };
public FrmCompany(){
    }
    public FrmCompany(Company company){
        this.company = company;
    }
    public FrmCompany(HttpServletRequest request, Company company){
        super(new FrmCompany(company),request);// super ini 
        //berfungsi memanggil induk construktor dari FRMHandler
        this.company = company;
    }
    public String getFormName() { return FRM_COMPANY; }
    public int[] getFieldTypes() { return fieldTypes; }
    public String[] getFieldNames() { return fieldNames; }
    public int getFieldSize() { return fieldNames.length; }
    public Company getEntityObject(){ return company; }

    public void requestEntityObject(Company company) { //melakukan 
        ///pemanggilan terhadap Company
            try{
		this.requestParam();
           company.setCompanyCode(getString(FRM_FLD_COMPANY_CODE));
            company.setCompanyName(getString(FRM_FLD_COMPANY_NAME)); 
            company.setPersonName(getString(FRM_FLD_PERSON_NAME));
            company.setPersonLastName(getString(FRM_FLD_PERSON_LAST_NAME));
             company.setBusAddress(getString(FRM_FLD_BUSS_ADDRESS));
            company.setTown(getString(FRM_FLD_TOWN));
            company.setProvince(getString(FRM_FLD_PROVINCE));
            company.setCountry(getString(FRM_FLD_COUNTRY));
             company.setTelpNr(getString(FRM_FLD_TELP_NR));
            company.setTelpMobile(getString(FRM_FLD_TELP_MOBILE));
            company.setFax(getString(FRM_FLD_FAX));
            company.setEmailCompany(getString(FRM_FLD_EMAIL_COMPANY));
            company.setPostalCode(getString(FRM_FLD_POSTAL_CODE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
    
}
