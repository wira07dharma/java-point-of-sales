/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.MasterTabungan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class FrmMasterTabungan extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MasterTabungan masterTabungan;



	public static final String FRM_AISO_MASTER_TABUNGAN		=  "FRM_AISO_MASTER_TABUNGAN" ;

	public static final int FRM_FIELD_ID_TABUNGAN           =  0 ;
        
        public static final int FRM_FIELD_NAMA_TABUNGAN         =  1 ;

	public static String[] fieldNames = {

		"FRM_FIELD_ID_TABUNGAN",            
            
                "FRM_FIELD_NAMA_TABUNGAN"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_STRING + ENTRY_REQUIRED,
                        
              
	} ;
   
        
        
	public FrmMasterTabungan (){

	}

	public FrmMasterTabungan(MasterTabungan masterTabungan){

		this.masterTabungan = masterTabungan;

	}


	public FrmMasterTabungan(HttpServletRequest request, MasterTabungan masterTabungan){

		super(new FrmMasterTabungan(masterTabungan), request);

		this.masterTabungan = masterTabungan;

	}


	public String getFormName() { return FRM_AISO_MASTER_TABUNGAN; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public MasterTabungan getEntityObject(){ return masterTabungan; }

	public void requestEntityObject(MasterTabungan masterTabungan) {

		try{

			this.requestParam();

			masterTabungan.setSavingName(getString(FRM_FIELD_NAMA_TABUNGAN));
                        
		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String masterTabungan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
