/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.form.uploadpicture;

import com.dimata.aplikasi.entity.mastertemplate.TempDinamis;
import com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground;
import com.dimata.aplikasi.entity.uploadpicture.PictureBackground;
import com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author arin
 */
public class FrmPictureBackground extends FRMHandler implements I_FRMInterface, I_FRMType{
 private PictureBackground pictureBackground;
    
    public static final String FRM_PICTURE_BACKGROUND = "FRM_PICTURE_BACKGROUND";
    
    public static final int FRM_FLD_NAMA_PICTURE = 0;
    public static final int FRM_FLD_KETERANGAN=1;
    public static final int FRM_FLD_UPLOAD_PICTURE=2;
    public static final int FRM_FLD_CHANGE_PICTURE=3;
    
     public static String[] fieldNames={
    
        "FRM_FLD_NAMA_PICTURE",
        "FRM_FLD_KETERANGAN",
        "FRM_FLD_UPLOAD_PICTURE",
        "FRM_FLD_CHANGE_PICTURE",
        
   };
    public static int[]fieldTypes={
    
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING, 
        TYPE_BOOL,
    };
public FrmPictureBackground(){
    }
    public FrmPictureBackground(PictureBackground pictureBackground){
            this.pictureBackground=pictureBackground;
    }
    public  FrmPictureBackground(HttpServletRequest request, PictureBackground pictureBackground){
        super(new FrmPictureBackground(pictureBackground),request);
        this.pictureBackground=pictureBackground;
    }
    public String getFormName(){ return FRM_PICTURE_BACKGROUND; }
    
    
        public int[] getFieldTypes(){ return  fieldTypes; }
     
        public String[] getFieldNames(){ return fieldNames;}
        
        public int getFieldSize (){return  fieldNames.length; }
        
        public PictureBackground getEntityObject(){return  pictureBackground; }
        
        
        public void requetEntityObject(PictureBackground pictureBackground){
        
            try{
            
                this.requestParam();
                    pictureBackground.setNamaPicture(getString(FRM_FLD_NAMA_PICTURE).toUpperCase());
                    pictureBackground.setKeterangan(getString(FRM_FLD_KETERANGAN));           
                    pictureBackground.setUploadPicture(getString(FRM_FLD_UPLOAD_PICTURE));
                    pictureBackground.setChangePicture(getBoolean(FRM_FLD_CHANGE_PICTURE));
                    
            }catch (Exception e){
            
                    System.out.println("Error on requestEntityObject : " + e.toString());
              }
      }
   
}