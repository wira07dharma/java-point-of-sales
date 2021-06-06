/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.transaksi;


import com.dimata.aiso.entity.masterdata.transaksi.DataTransaksi;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Dede
 */
public class FrmDataTransaksi extends FRMHandler implements I_FRMInterface, I_FRMType {
  private DataTransaksi dataTransaksi;



	public static final String FRM_TRANSAKSI_NAME		=  "DATA_TRANSAKSI" ;

	public static final int FRM_FIELD_ID_TRANSAKSI             =  0 ;
        
        public static final int FRM_FIELD_ID_ANGGOTA              =  1 ;
        
        public static final int FRM_FIELD_CODE_TRANSAKSI            = 2 ;
        
        public static final int FRM_FIELD_TANGGAL_TRANSAKSI       =  3 ;
         
        public static final int FRM_FIELD_JENIS_TRANSAKSI       =  4 ;
        
        public static final int FRM_FIELD_BUNGA                 =  5 ;
        
        public static final int FRM_FIELD_POTONGAN                 =  6 ;
        
         public static final int FRM_FIELD_JUMLAH_TRANSAKSI     =  7 ;
         
        public static final int FRM_FIELD_TOTAL_SALDO                 =  8 ;

	public static String[] fieldNames = {

		"FRM_FIELD_ID_TRANSAKSI",            
            
                "FRM_FIELD_ID_ANGGOTA",
                
                "FRM_FIELD_CODE_TRANSAKSI",
                
                "FRM_FIELD_TANGGAL_TRANSAKSI",
                
                "FRM_FIELD_JENIS_TRANSAKSI",
                
                "FRM_FIELD_BUNGA",
                
                "FRM_FIELD_POTONGAN",
                
                "FRM_FIELD_JUMLAH_TRANSAKSI",
                
                "FRM_FIELD_TOTAL_SALDO"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
                TYPE_LONG,
                
                TYPE_STRING,
                
                TYPE_STRING,
                
                TYPE_LONG,
                
                TYPE_FLOAT,
                
		TYPE_FLOAT,
                
                TYPE_FLOAT,
                
                TYPE_FLOAT
                        
              
	} ;
   
        
        
	public FrmDataTransaksi (){

	}

	public FrmDataTransaksi(DataTransaksi dataTransaksi){

		this.dataTransaksi = dataTransaksi;

	}


	public FrmDataTransaksi(HttpServletRequest request, DataTransaksi dataTransaksi){

		super(new FrmDataTransaksi(dataTransaksi), request);

		this.dataTransaksi = dataTransaksi;

	}


	public String getFormName() { return FRM_TRANSAKSI_NAME; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public DataTransaksi getEntityObject(){ return dataTransaksi; }

	public void requestEntityObject(DataTransaksi dataTransaksi) {
		try{
			this.requestParam();
                        String tanggal = this.getString(FRM_FIELD_TANGGAL_TRANSAKSI);
                        Date tgl = Formater.formatDate(tanggal, "yyyy-MM-dd");
                        dataTransaksi.setIdAnggota(getLong(FRM_FIELD_ID_ANGGOTA));
                        dataTransaksi.setCodeTransaksi(getString(FRM_FIELD_CODE_TRANSAKSI));
                        dataTransaksi.setTanggal(tgl);
                        dataTransaksi.setJenisTransaksi(getLong(FRM_FIELD_JENIS_TRANSAKSI));
			dataTransaksi.setBunga(getFloat(FRM_FIELD_BUNGA));
                        dataTransaksi.setPotongan(getFloat(FRM_FIELD_POTONGAN));
                        dataTransaksi.setJumlahTransaksi(getFloat(FRM_FIELD_JUMLAH_TRANSAKSI));
                        dataTransaksi.setSaldo(getFloat(FRM_FIELD_TOTAL_SALDO));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

    void requestEntityObject(String dataTransaksi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
   

