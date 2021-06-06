/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisSimpanan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dede nuharta
 */
public class FrmJenisSimpanan extends FRMHandler implements I_FRMInterface, I_FRMType {

    private JenisSimpanan jenisSimpanan;



	public static final String FRM_AISO_JENIS_SIMPANAN		=  "FRM_AISO_JENIS_SIMPANAN" ;

	public static final int FRM_FIELD_ID_JENIS_SIMPANAN           =  0 ;
        
        public static final int FRM_FIELD_NAMA_SIMPANAN         =  1 ;

	public static final int FRM_FIELD_SETORAN_MIN           =  2 ;
        
        public static final int FRM_FIELD_BUNGA                 =  3 ;
        
        public static final int FRM_FIELD_PROVISI               =  4 ;

	public static final int FRM_FIELD_BIAYA_ADMIN           =  5 ;
        
        public static final int FRM_FIELD_DESC_JENIS_SIMPANAN   =  6 ;
        
        public static final int FRM_FIELD_FREKUENSI_SIMPANAN   =  7 ;
        
        public static final int FRM_FIELD_FREKUENSI_PENARIKAN   =  8 ;
        
        public static final int FRM_FIELD_BASIS_HARI_BUNGA   =  9 ;
        public static final int FRM_FIELD_JENIS_BUNGA   =  10 ;

	public static String[] fieldNames = {

		"FRM_FIELD_ID_SIMPANAN",            
            
                "FRM_FIELD_NAMA_SIMPANAN",

		"FRM_FIELD_SETORAN_MIN",
                
                "FRM_FIELD_BUNGA",            
            
                "FRM_FIELD_PROVISI",

		"FRM_FIELD_BIAYA_ADMIN",
                
                "FRM_FIELD_DESC_JENIS_SIMPANAN",
                
                "FRM_FIELD_FREKUENSI_SIMPANAN",
                
                "FRM_FIELD_FREKUENSI_PENARIKAN",
                
                "FRM_FIELD_BASIS_HARI_BUNGA",
                "jenis_bunga"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_STRING + ENTRY_REQUIRED,
                
                TYPE_FLOAT,
                
                TYPE_FLOAT,
                
                TYPE_FLOAT,
                
                TYPE_FLOAT,
                
                TYPE_STRING,
                
                TYPE_INT,
                
                TYPE_INT,
                
                TYPE_FLOAT,
                TYPE_INT
                        
              
	} ;
   
        
        
	public FrmJenisSimpanan (){

	}

	public FrmJenisSimpanan(JenisSimpanan jenisSimpanan){

		this.jenisSimpanan = jenisSimpanan;

	}


	public FrmJenisSimpanan(HttpServletRequest request, JenisSimpanan jenisSimpanan){

		super(new FrmJenisSimpanan(jenisSimpanan), request);

		this.jenisSimpanan = jenisSimpanan;

	}


	public String getFormName() { return FRM_AISO_JENIS_SIMPANAN; }
        

	public int[] getFieldTypes() { return fieldTypes; }
        

	public String[] getFieldNames() { return fieldNames; }
        

	public int getFieldSize() { return fieldNames.length; }
        

	public JenisSimpanan getEntityObject(){ return jenisSimpanan; }
        

	public void requestEntityObject(JenisSimpanan jenisSimpanan) {

		try{

			this.requestParam();
                        
			jenisSimpanan.setNamaSimpanan(this.getString(FRM_FIELD_NAMA_SIMPANAN));
                        jenisSimpanan.setSetoranMin(this.getFloat(FRM_FIELD_SETORAN_MIN));
                        jenisSimpanan.setBunga(this.getFloat(FRM_FIELD_BUNGA));
                        jenisSimpanan.setProvisi(this.getFloat(FRM_FIELD_PROVISI));
                        jenisSimpanan.setBiayaAdmin(this.getFloat(FRM_FIELD_BIAYA_ADMIN));
                        jenisSimpanan.setDescJenisSimpanan(this.getString(FRM_FIELD_DESC_JENIS_SIMPANAN));
                        jenisSimpanan.setFrekuensiSimpanan(this.getInt(FRM_FIELD_FREKUENSI_SIMPANAN));
                        jenisSimpanan.setFrekuensiPenarikan(this.getInt(FRM_FIELD_FREKUENSI_PENARIKAN));
                        jenisSimpanan.setBasisHariBunga(this.getDouble(FRM_FIELD_BASIS_HARI_BUNGA));
                        jenisSimpanan.setJenisBunga(this.getInt(FRM_FIELD_JENIS_BUNGA));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String jenisSimpanan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
