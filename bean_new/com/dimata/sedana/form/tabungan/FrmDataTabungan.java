/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.tabungan;


import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.tabungan.DataTabungan;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Dede
 */
public class FrmDataTabungan extends FRMHandler implements I_FRMInterface, I_FRMType {
  private DataTabungan dataTabungan;



	public static final String FRM_TABUNGAN_NAME		=  "DATA_TABUNGAN" ;

	public static final int FRM_FIELD_ID_SIMPANAN             =  0 ;
        
        public static final int FRM_FIELD_ID_ANGGOTA              =  1 ;
        
        public static final int FRM_FIELD_ID_JENIS_SIMPANAN       =  2 ;
         
        public static final int FRM_FIELD_JENIS_TABUNGAN_ID       =  3 ;
        
        public static final int FRM_FIELD_TANGGAL                 =  4 ;
        
        public static final int FRM_FIELD_JUMLAH                 =  5 ;
        
        public static final int FRM_FIELD_STATUS                    = 6 ;

	public static String[] fieldNames = {

		"FRM_FIELD_ID_SIMPANAN",            
            
                "FRM_FIELD_ID_ANGGOTA",
                
                "FRM_FIELD_ID_JENIS_SIMPANAN",
                
                "FRM_FIELD_JENIS_TABUNGAN_ID",
                
                "FRM_FIELD_TANGGAL",
                
                "FRM_FIELD_JUMLAH",
                
                "FRM_FIELD_STATUS"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
                TYPE_LONG,
                
                TYPE_LONG,
                
                TYPE_LONG,
                
                TYPE_STRING,
                
		TYPE_FLOAT,
                
                TYPE_INT
                        
              
	} ;
   
        
        
	public FrmDataTabungan (){

	}

	public FrmDataTabungan(DataTabungan dataTabungan){

		this.dataTabungan = dataTabungan;

	}


	public FrmDataTabungan(HttpServletRequest request, DataTabungan dataTabungan){

		super(new FrmDataTabungan(dataTabungan), request);

		this.dataTabungan = dataTabungan;

	}


	public String getFormName() { return FRM_TABUNGAN_NAME; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public DataTabungan getEntityObject(){ return dataTabungan; }

	public void requestEntityObject(DataTabungan dataTabungan) {
		try{
			this.requestParam();
                        String tanggal = this.getString(FRM_FIELD_TANGGAL);
                        Date tgl = Formater.formatDate(tanggal, "yyyy-MM-dd");
                        dataTabungan.setIdAnggota(getLong(FRM_FIELD_ID_ANGGOTA));
                        dataTabungan.setIdJenisSimpanan(getLong(FRM_FIELD_ID_JENIS_SIMPANAN));
                        dataTabungan.setIdJenisTabungan(getLong(FRM_FIELD_JENIS_TABUNGAN_ID));
			dataTabungan.setTanggal(tgl);
                        dataTabungan.setJumlah(getFloat(FRM_FIELD_JUMLAH));
                        dataTabungan.setStatus(getInt(FRM_FIELD_STATUS));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

    void requestEntityObject(String dataTabungan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
   

