/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.KelompokKoperasi;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dede nuharta
 */
public class FrmKelompokKoperasi extends FRMHandler implements I_FRMInterface, I_FRMType {

    private KelompokKoperasi kelompokKoperasi;



	public static final String FRM_AISO_KELOMPOK_KOPERASI		=  "FRM_AISO_KELOMPOK_KOPERASI" ;

	public static final int FRM_FIELD_KELOMPOK_ID           =  0 ;
        
        public static final int FRM_FIELD_NAMA_KELOMPOK         =  1 ;

	public static final int FRM_FIELD_DESC_KELOMPOK         =  2 ;

	public static String[] fieldNames = {

		"FRM_FIELD_KELOMPOK_ID",            
            
                "FRM_FIELD_NAMA_KELOMPOK",

		"FRM_FIELD_DESC_KELOMPOK"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_STRING + ENTRY_REQUIRED,
                
                TYPE_STRING
                        
              
	} ;
   
        
        
	public FrmKelompokKoperasi (){

	}

	public FrmKelompokKoperasi(KelompokKoperasi kelompokKoperasi){

		this.kelompokKoperasi = kelompokKoperasi;

	}


	public FrmKelompokKoperasi(HttpServletRequest request, KelompokKoperasi kelompokKoperasi){

		super(new FrmKelompokKoperasi(kelompokKoperasi), request);

		this.kelompokKoperasi = kelompokKoperasi;

	}


	public String getFormName() { return FRM_AISO_KELOMPOK_KOPERASI; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public KelompokKoperasi getEntityObject(){ return kelompokKoperasi; }

	public void requestEntityObject(KelompokKoperasi kelompokKoperasi) {

		try{

			this.requestParam();
                        //kelompokKoperasi.setOID(getLong(FRM_FIELD_KELOMPOK_ID));
			kelompokKoperasi.setNamaKelompok(this.getString(FRM_FIELD_NAMA_KELOMPOK));
                        kelompokKoperasi.setDeskripsi(this.getString(FRM_FIELD_DESC_KELOMPOK));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String kelompokKoperasi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
