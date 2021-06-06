/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.masterdata;

import com.dimata.hanoman.entity.masterdata.MasterGroup;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sangtel6
 */
public class FrmMasterGroup extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    
     private MasterGroup masterGroup;



	public static final String FRM_NAME_MASTER_GROUP	=  "FRM_NAME_MASTER_GROUP" ;



	public static final int FRM_FIELD_MASTER_GROUP_ID		=  0 ;
        
        public static final int FRM_FIELD_TYPE_GROUP			=  1 ;

	

	public static final int FRM_FIELD_NAME_GROUP			=  2 ;

	



	public static String[] fieldNames = {

		"FRM_FIELD_MASTER_GROUP_ID",
                
                "FRM_FIELD_TYPE_GROUP",
                
                "FRM_FIELD_NAME_GROUP"

		

	} ;



	public static int[] fieldTypes = {

		TYPE_LONG,  
                
                TYPE_INT,
                
                TYPE_STRING

		

	} ;



	public FrmMasterGroup(){

	}

	public FrmMasterGroup(MasterGroup masterGroup){

		this.masterGroup = masterGroup;

	}



	public FrmMasterGroup(HttpServletRequest request, MasterGroup masterGroup){

		super(new FrmMasterGroup(masterGroup), request);

		this.masterGroup = masterGroup;

	}



	public String getFormName() { return FRM_NAME_MASTER_GROUP; } 



	public int[] getFieldTypes() { return fieldTypes; }



	public String[] getFieldNames() { return fieldNames; } 



	public int getFieldSize() { return fieldNames.length; } 



	public MasterGroup getEntityObject(){ return masterGroup; }



	public void requestEntityObject(MasterGroup masterGroup) {

		try{

			this.requestParam();

			

			masterGroup.setTypeGroup(getInt(FRM_FIELD_TYPE_GROUP));
                        
                       
                        
                        masterGroup.setNamaGroup(getString(FRM_FIELD_NAME_GROUP));

			

		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    
}
