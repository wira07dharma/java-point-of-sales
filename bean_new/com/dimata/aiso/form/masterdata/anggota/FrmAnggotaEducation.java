/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.AnggotaEducation;
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
public class FrmAnggotaEducation extends FRMHandler implements I_Language, I_FRMInterface, I_FRMType{
    public static final String FRM_NAME_ANGGOTA_EDUCATION = "ANGGOTA_EDUCATION";
    
    public static final int FRM_ANGGOTA_ID = 0;
    public static final int FRM_EDUCATION_ID = 1;
    public static final int FRM_EDUCATION_DETAIL = 2;
    
    public static final String[] fieldNames = {
        "Anggota_id",
        "Education_Id",
        "Education_Detail"
    };
    
    public static final int[] fieldTypes={
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING
    };
    
    private AnggotaEducation anggotaEducation;
    
    public FrmAnggotaEducation(AnggotaEducation anggotaEducation){
        this.anggotaEducation = anggotaEducation;
    }
    
    public FrmAnggotaEducation(HttpServletRequest req, AnggotaEducation anggotaEducation){
        super(new FrmAnggotaEducation(anggotaEducation),req);
        this.anggotaEducation = anggotaEducation;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_ANGGOTA_EDUCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public AnggotaEducation getEntityObject(){
        return anggotaEducation;
    }
    
    public void requestEntityObject(AnggotaEducation anggotaEducation){
        try{
            this.requestParam();
            
            anggotaEducation.setAnggotaId(this.getLong(FRM_ANGGOTA_ID));
            anggotaEducation.setEducationId(this.getLong(FRM_EDUCATION_ID));
            anggotaEducation.setEducationDetail(this.getString(FRM_EDUCATION_DETAIL));
            
            this.anggotaEducation = anggotaEducation;
        }catch(Exception e){
            this.anggotaEducation = new AnggotaEducation();
        }
    }
}
