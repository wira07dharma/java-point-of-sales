/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.Vocation;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class FrmVocation extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Vocation vocation;



	public static final String FRM_TB_VOCATION		=  "FRM_TB_VOCATION" ;

	public static final int FRM_FIELD_VOCATION_ID           =  0 ;
        
        public static final int FRM_FIELD_VOCATION_NAME         =  1 ;

	public static final int FRM_FIELD_DESC_VOCATION         =  2 ;

	public static String[] fieldNames = {

		"FRM_FIELD_VOCATION_ID",            
            
                "FRM_FIELD_VOCATION_NAME",

		"FRM_FIELD_DESC_VOCATION"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_STRING + ENTRY_REQUIRED,
                
                TYPE_STRING,
                        
              
	} ;
   
        
        
	public FrmVocation (){

	}

	public FrmVocation(Vocation vocation){

		this.vocation = vocation;

	}


	public FrmVocation(HttpServletRequest request, Vocation vocation){

		super(new FrmVocation(vocation), request);

		this.vocation = vocation;

	}


	public String getFormName() { return FRM_TB_VOCATION; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public Vocation getEntityObject(){ return vocation; }

	public void requestEntityObject(Vocation vocation) {

		try{

			this.requestParam();

			vocation.setVocationName(getString(FRM_FIELD_VOCATION_NAME));
                        
                        vocation.setDescription(getString(FRM_FIELD_DESC_VOCATION));
                        
		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String vocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
