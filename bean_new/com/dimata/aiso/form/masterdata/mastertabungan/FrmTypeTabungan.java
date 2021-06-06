/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.TypeTabungan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class FrmTypeTabungan extends FRMHandler implements I_FRMInterface, I_FRMType {

    private TypeTabungan typeTabungan;



	public static final String FRM_TYPE_TABUNGAN		=  "FRM_TYPE_TABUNGAN" ;

	public static final int FRM_FIELD_ID_TIPE_TABUNGAN           =  0 ;
        
        public static final int FRM_FIELD_TIPE_TABUNGAN              =  1 ;

	public static String[] fieldNames = {

		"FRM_FIELD_ID_TIPE_TABUNGAN",            
            
                "FRM_FIELD_TIPE_TABUNGAN"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_STRING + ENTRY_REQUIRED,
                        
              
	} ;
   
        
        
	public FrmTypeTabungan (){

	}

	public FrmTypeTabungan(TypeTabungan typeTabungan){

		this.typeTabungan = typeTabungan;

	}


	public FrmTypeTabungan(HttpServletRequest request, TypeTabungan typeTabungan){

		super(new FrmTypeTabungan(typeTabungan), request);

		this.typeTabungan = typeTabungan;

	}


	public String getFormName() { return FRM_TYPE_TABUNGAN; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public TypeTabungan getEntityObject(){ return typeTabungan; }

	public void requestEntityObject(TypeTabungan typeTabungan) {

		try{

			this.requestParam();

			typeTabungan.setTypeTabungan(getString(FRM_FIELD_TIPE_TABUNGAN));
                        
		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String typeTabungan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
