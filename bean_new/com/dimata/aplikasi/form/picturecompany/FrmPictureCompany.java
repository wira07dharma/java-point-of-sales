/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.form.picturecompany;

import com.dimata.aplikasi.entity.mastertemplate.TempDinamis;
import com.dimata.aplikasi.entity.picturecompany.PictureCompany;
import com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis;
import static com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis.FRM_FLD_TEMP_COLOR;
import static com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis.FRM_FLD_TEMP_NAVIGATION;
import static com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis.FRM_FLD_TEMP_VERSION_NO;
import static com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis.FRM_TEMP_DINAMIS;
import static com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis.fieldNames;
import static com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis.fieldTypes;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author user
 */
public class FrmPictureCompany extends FRMHandler implements I_FRMInterface, I_FRMType {
    public PictureCompany pictureCompany;
    
    public static final String FRM_PICTURE_COMPANY="FRM_PICTURE_COMPANY";
    
    public static final int FRM_FLD_PICTURE_COMPANY_ID = 0;
    public static final int FRM_FLD_NAMA_PICTURE=1;
    
    public static String[]fieldNames={
        "FRM_FLD_PICTURE_COMPANY_ID",
        "FRM_FLD_NAMA_PICTURE"
    };
    
    public static int[]fiedTypes={
        TYPE_LONG,
        TYPE_STRING
    };
    
    public FrmPictureCompany(){
    }
    public FrmPictureCompany(PictureCompany pictureCompany){
            this.pictureCompany=pictureCompany;
    }
    public  FrmPictureCompany(HttpServletRequest request, PictureCompany pictureCompany){
        super(new FrmPictureCompany(pictureCompany),request);
        this.pictureCompany=pictureCompany;
    }
    public String getFormName(){ return FRM_PICTURE_COMPANY; }
    
    
        public int[] getFieldTypes(){ return  fieldTypes; }
     
        public String[] getFieldNames(){ return fieldNames;}
        
        public int getFieldSize (){return  fieldNames.length; }
        
        public PictureCompany getEntityObject(){return  pictureCompany; }
        
        
        public void requetEntityObject(PictureCompany pictureCompany){
        
            try{
            
                this.requestParam();
                    //pictureCompany.setPictureCompanyId(getString(FRM_FLD_PICTURE_COMPANY_ID).toUpperCase());
                    pictureCompany.setNamaPicture(getString(FRM_FLD_NAMA_PICTURE)); 

            }catch (Exception e){
            
                    System.out.println("Error on requestEntityObject : " + e.toString());
              }
      }
}
