/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.masterdata;

import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sangtel6
 */
public class FrmMasterType extends FRMHandler implements I_FRMInterface, I_FRMType 
 {
   
    
    private MasterType masterType;



	public static final String FRM_NAME_MASTERTYPE		=  "FRM_NAME_MASTERTYPE" ;



	public static final int FRM_FIELD_MASTER_TYPE_ID		=  0 ;
        
        public static final int FRM_FIELD_TYPE_GROUP			=  1 ;

	public static final int FRM_FIELD_MASTER_CODE			=  2 ;

	public static final int FRM_FIELD_MASTER_NAME			=  3 ;

	public static final int FRM_FIELD_DESCRIPTION			=  4 ;



	public static String[] fieldNames = {

		"FRM_FIELD_MASTER_TYPE_ID",
                
                "FRM_FIELD_TYPE_GROUP",
                
                "FRM_FIELD_MASTER_CODE",

		"FRM_FIELD_MASTER_NAME",  
                
                "FRM_FIELD_DESCRIPTION"

	} ;



	public static int[] fieldTypes = {

		TYPE_LONG,  
                
                TYPE_INT,
                
                TYPE_STRING + ENTRY_REQUIRED,

		TYPE_STRING + ENTRY_REQUIRED,  
                
                TYPE_STRING

	} ;



	public FrmMasterType(){

	}

	public FrmMasterType(MasterType masterType){

		this.masterType = masterType;

	}



	public FrmMasterType(HttpServletRequest request, MasterType masterType){

		super(new FrmMasterType(masterType), request);

		this.masterType = masterType;

	}



	public String getFormName() { return FRM_NAME_MASTERTYPE; } 



	public int[] getFieldTypes() { return fieldTypes; }



	public String[] getFieldNames() { return fieldNames; } 



	public int getFieldSize() { return fieldNames.length; } 



	public MasterType getEntityObject(){ return masterType; }



	public void requestEntityObject(MasterType masterType) {

		try{

			this.requestParam();

			

			masterType.setTypeGroup(getInt(FRM_FIELD_TYPE_GROUP));
                        
                        masterType.setMasterCode(getString(FRM_FIELD_MASTER_CODE));
                        
                        masterType.setMasterName(getString(FRM_FIELD_MASTER_NAME));

			masterType.setDescription(getString(FRM_FIELD_DESCRIPTION));

		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

}
