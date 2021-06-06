package com.dimata.aiso.entity.masterdata.mastertabungan;

/* package java */
import com.dimata.qdep.entity.*;

public class JenisTabungan extends Entity {

    	private String nama_jenis_tabungan = "";
        private long id_tabungan; //edit tanggal 28
               

	public String getNamaJenisTbgn(){
		return nama_jenis_tabungan;
	}

	public void setNamaJenisTbgn(String nama_jenis_tabungan){
		if ( nama_jenis_tabungan == null ) {
			nama_jenis_tabungan = "";
		}
		this.nama_jenis_tabungan = nama_jenis_tabungan;
	}

    /**
     * @return the id_tabungan
     */
    public long getId_tabungan() {
        return id_tabungan;
    }

    /**
     * @param id_tabungan the id_tabungan to set
     */
    public void setId_tabungan(long id_tabungan) {
        this.id_tabungan = id_tabungan;
    }
 
}