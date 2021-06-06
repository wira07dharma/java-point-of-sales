/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;


import com.dimata.aiso.entity.masterdata.mastertabungan.Afiliasi;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Dede
 */
public class FrmAfiliasi extends FRMHandler implements I_FRMInterface, I_FRMType {
  private Afiliasi afiliasi;



	public static final String FRM_AISO_AFILIASI		=  "FRM_AISO_AFILIASI" ;

	public static final int FRM_FIELD_AFILIASI_ID           =  0 ;
        
        public static final int FRM_FIELD_FEE_KOPERASI          =  1 ;
        
        public static final int FRM_FIELD_NAME_AFILIASI          =  2 ;
        
        public static final int FRM_FIELD_ALAMAT_AFILIASI          =  3 ;

	public static String[] fieldNames = {

		"FRM_FIELD_AFILIASI_ID",            
            
                "FRM_FIELD_FEE_KOPERASI",
                
                "FRM_FIELD_NAME_AFILIASI",
                
                "FRM_FIELD_ALAMAT_AFILIASI"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
                TYPE_FLOAT,
                
		TYPE_STRING + ENTRY_REQUIRED,
                
                TYPE_STRING
                        
              
	} ;
   
        
        
	public FrmAfiliasi (){

	}

	public FrmAfiliasi(Afiliasi afiliasi){

		this.afiliasi = afiliasi;

	}


	public FrmAfiliasi(HttpServletRequest request, Afiliasi afiliasi){

		super(new FrmAfiliasi(afiliasi), request);

		this.afiliasi = afiliasi;

	}


	public String getFormName() { return FRM_AISO_AFILIASI; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public Afiliasi getEntityObject(){ return afiliasi; }

	public void requestEntityObject(Afiliasi afiliasi) {
		try{
			this.requestParam();
                        
			afiliasi.setFeeKoperasi(getFloat(FRM_FIELD_FEE_KOPERASI));
                        afiliasi.setNamaAfiliasi(getString(FRM_FIELD_NAME_AFILIASI));
                        afiliasi.setAlamatAfiliasi(getString(FRM_FIELD_ALAMAT_AFILIASI));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

    void requestEntityObject(String afiliasi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
   

