/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.form.mastertemplate;

import com.dimata.aplikasi.entity.mastertemplate.TempDinamis;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import static com.dimata.qdep.form.I_FRMType.TYPE_STRING;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author arin
 */
public class FrmTempDinamis extends FRMHandler implements I_FRMInterface, I_FRMType{
 private TempDinamis tempDinamis;
    
    public static final String FRM_TEMP_DINAMIS = "FRM_TEMP_DINAMIS";
    
    //public static final int FRM_FLD_TEMP_DINAMIS_ID = 0;
    public static final int FRM_FLD_TEMP_VERSION_NO=0;
    public static final int FRM_FLD_TEMP_COLOR=1;
    public static final int FRM_FLD_HEAD_COLOR=2;
    public static final int FRM_FLD_CONT_COLOR=3;
    public static final int FRM_FLD_BG_MENU=4;
    public static final int FRM_FLD_FONT_MENU=5;
    public static final int FRM_FLD_HOVER_MENU=6;
    public static final int FRM_FLD_TEMP_NAVIGATION=7;
    public static final int FRM_FLD_LANGUAGE=8;
    public static final int FRM_FLD_TEMP_COLOR_HEADER2=9;
    public static final int FRM_FLD_GARIS_HAEADER1=10;
    public static final int FRM_FLD_GARIS_HEADER2=11;
    public static final int FRM_FLD_GARIS_CONTENT=12;
    public static final int FRM_FLD_FOOTER_GARIS=13;
    public static final int FRM_FLD_FOOTER_BACKGROUND=14;
    public static final int FRM_FLD_TABLE_HEADER=15;
    public static final int FRM_FLD_TABLE_CELL=16;
   
    public static String[] fieldNames={
    
        //"FRM_FLD_TEMP_DINAMIS_ID",
        "FRM_FLD_TEMP_VERSION_NO",
        "FRM_FLD_TEMP_COLOR",
        "FRM_FLD_HEAD_COLOR",
        "FRM_FLD_CONT_COLOR",
        "FRM_FLD_BG_MENU",
        "FRM_FLD_FONT_MENU",
        "FRM_FLD_HOVER_MENU",
        "FRM_FLD_TEMP_NAVIGATION",
        "FRM_FLD_LANGUAGE",
        "FRM_FLD_TEMP_COLOR_HEADER2",
        "FRM_FLD_GARIS_HAEADER1",
        "FRM_FLD_GARIS_HEADER2",
        "FRM_FLD_GARIS_CONTENT",
        "FRM_FLD_FOOTER_GARIS",
        "FRM_FLD_FOOTER_BACKGROUND",
        "FRM_FLD_TABLE_HEADER",
        "FRM_FLD_TABLE_CELL"
   };
    public static int[]fieldTypes={
    
        //TYPE_LONG,
        TYPE_INT,
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
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
            
    };
    
    public static final int TEMPLATE_I = 0;
    public static final int TEMPLATE_II = 1;
    public static int[] JENIS_TEMPLATE_VALUE = {0,1};
    public static final String[] JENIS_TEMPLATE_KEY = {"Template 1","Template 2"};
    public static Vector getJenisTemplateValue(){
    Vector jenis=new Vector();
    for(int indx=0;indx<JENIS_TEMPLATE_VALUE.length;indx++){
        jenis.add(""+JENIS_TEMPLATE_VALUE[indx]); 
    }
    return jenis;
    }
    public static Vector getJenisTemplateKey(){
      Vector jenis=new Vector();
    for(int indx=0;indx<JENIS_TEMPLATE_KEY.length;indx++){
        jenis.add(""+JENIS_TEMPLATE_KEY[indx]); 
    } 
    return jenis;
    }
public FrmTempDinamis(){
    }
    public FrmTempDinamis(TempDinamis tempDinamis){
            this.tempDinamis=tempDinamis;
    }
    public  FrmTempDinamis(HttpServletRequest request, TempDinamis tempDinamis){
        super(new FrmTempDinamis(tempDinamis),request);
        this.tempDinamis=tempDinamis;
    }
    public String getFormName(){ return FRM_TEMP_DINAMIS; }
    
    
        public int[] getFieldTypes(){ return  fieldTypes; }
     
        public String[] getFieldNames(){ return fieldNames;}
        
        public int getFieldSize (){return  fieldNames.length; }
        
        public TempDinamis getEntityObject(){return  tempDinamis; }
        
        
        public void requetEntityObject(TempDinamis tempDinamis){
        
            try{
            
                this.requestParam();
                    //tempDinamis.setTempDinamisId(getString(FRM_FLD_TEMP_DINAMIS_ID).toUpperCase());
                    tempDinamis.setTempVersionNo(getString(FRM_FLD_TEMP_VERSION_NO));           
                    tempDinamis.setTempColor(getString(FRM_FLD_TEMP_COLOR));           
                    tempDinamis.setHeaderColor(getString(FRM_FLD_HEAD_COLOR));//header 1
                    //update by devin 2013-12-18
                    tempDinamis.setTempColorHeader(getString(FRM_FLD_TEMP_COLOR_HEADER2));
                    tempDinamis.setGarisHeader1(getString(FRM_FLD_GARIS_HAEADER1));
                    tempDinamis.setGarisHeader2(getString(FRM_FLD_GARIS_HEADER2));
                    tempDinamis.setContentColor(getString(FRM_FLD_CONT_COLOR)); //background content         
                    tempDinamis.setBgMenu(getString(FRM_FLD_BG_MENU)); //background menu         
                    tempDinamis.setFontMenu(getString(FRM_FLD_FONT_MENU));//font menu          
                    tempDinamis.setHoverMenu(getString(FRM_FLD_HOVER_MENU));//hover menu
                    tempDinamis.setNavigation(getString(FRM_FLD_TEMP_NAVIGATION));
                    //tempDinamis.setLanguage(getString(FRM_FLD_LANGUAGE));
                    //update by devin 2013-12-18
                    tempDinamis.setGarisContent(getString(FRM_FLD_GARIS_CONTENT));
                    tempDinamis.setFooterGaris(getString(FRM_FLD_FOOTER_GARIS));
                    tempDinamis.setFooterBackground(getString(FRM_FLD_FOOTER_BACKGROUND));
                    //update opie-eyek 20140109
                    tempDinamis.setTableHeader(getString(FRM_FLD_TABLE_HEADER));
                    tempDinamis.setTableCell(getString(FRM_FLD_TABLE_CELL));
                    

            }catch (Exception e){
            
                    System.out.println("Error on requestEntityObject : " + e.toString());
              }
      }
   
}