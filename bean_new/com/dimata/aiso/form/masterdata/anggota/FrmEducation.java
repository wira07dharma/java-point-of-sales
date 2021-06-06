/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.Education;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa
 * Created Kamis, 21 Pebruari 2013
 */
public class FrmEducation extends FRMHandler implements I_Language, I_FRMInterface, I_FRMType{
    public static final String FRM_NAME = "EDUCATION";
    
    public static final int FRM_EDUCATION = 0;
    public static final int FRM_EDUCATION_DESC = 1;
    public static final int FRM_EDUCATION_CODE = 2;
    public static final String[] fieldNames = {
        "Education",
        "EducationDescrption",
        "EducationCode"
                
    };
    
    public static final int[] fieldTypes={
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_STRING
    };
    
    private Education education;
    
    public FrmEducation(Education education){
        this.education = education;
    }
    
    public FrmEducation(HttpServletRequest req, Education education){
        super(new FrmEducation(education),req);
        this.education = education;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public Education getEntityObject(){
        return education;
    }
    
    public void requestEntityObject(Education education){
        try{
            this.requestParam();
            
            education.setEducation(this.getString(FRM_EDUCATION));
            education.setEducationDesc(this.getString(FRM_EDUCATION_DESC));
            education.setEducationCode(this.getString(FRM_EDUCATION_CODE));
            
            this.education = education;
        }catch(Exception e){
            this.education = new Education();
        }
    }
}
