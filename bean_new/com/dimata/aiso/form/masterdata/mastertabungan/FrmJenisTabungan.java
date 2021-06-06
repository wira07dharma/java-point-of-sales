/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisTabungan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import static com.dimata.qdep.form.I_FRMType.TYPE_STRING;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class FrmJenisTabungan extends FRMHandler implements I_FRMInterface, I_FRMType {

    private JenisTabungan jenisTabungan;



	public static final String FRM_AISO_JENIS_TABUNGAN                 =  "FRM_AISO_JENIS_TABUNGAN" ;

	public static final int FRM_FIELD_JENIS_TABUNGAN_ID              =  0 ;
        
        public static final int FRM_FIELD_ID_TABUNGAN                    =  1 ;

        public static final int FRM_FIELD_NAMA_JENIS_TABUNGAN            =  2 ;
            
	public static String[] fieldNames = {

		"FRM_FIELD_JENIS_TABUNGAN_ID",            
            
                "FRM_FIELD_ID_TABUNGAN",
                
                "FRM_FIELD_NAMA_JENIS_TABUNGAN"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_LONG + ENTRY_REQUIRED,
                
                TYPE_STRING,
                        
              
	} ;
   
        
        
	public FrmJenisTabungan (){

	}

	public FrmJenisTabungan(JenisTabungan jenisTabungan){

		this.jenisTabungan = jenisTabungan;

	}


	public FrmJenisTabungan(HttpServletRequest request, JenisTabungan jenisTabungan){

		super(new FrmJenisTabungan(jenisTabungan), request);

		this.jenisTabungan = jenisTabungan;

	}


	public String getFormName() { return FRM_AISO_JENIS_TABUNGAN; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public JenisTabungan getEntityObject(){ return jenisTabungan; }

	public void requestEntityObject(JenisTabungan jenisTabungan) {

		try{

			this.requestParam();

			jenisTabungan.setNamaJenisTbgn(getString(FRM_FIELD_NAMA_JENIS_TABUNGAN));
                        jenisTabungan.setId_tabungan(getLong(FRM_FIELD_ID_TABUNGAN));
                        
		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String jenisTabungan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
