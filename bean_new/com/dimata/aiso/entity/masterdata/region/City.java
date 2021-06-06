package com.dimata.aiso.entity.masterdata.region;

/* package java */
import com.dimata.qdep.entity.*;

/**
 * metod tersebut dipanggil bisa untuk semua class
 * @author Dede Nuharta
 */

public class City extends Entity {
/**
 * Mendefinisikan data
 */
    	private String city_name = "";
        
        //update tanggal 7 Maret 2013 oleh Hadi
        private long idProvince;
               

        /**
         * mengambil data dan memberikan nilai, contohnya getname, kemudian meberi return nama
         * @return 
         */
	public String getCityName(){
		return city_name;
	}
        
       
       /**
        * konstruktor / sebuah fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
        * @param nama 
        */ 
	public void setCityName(String city_name){
		if ( city_name == null ) {
			city_name = "";
		}
		this.city_name = city_name;
	}

    /**
     * @return the idProvince
     */
    public long getIdProvince() {
        return idProvince;
    }

    /**
     * @param idProvince the idProvince to set
     */
    public void setIdProvince(long idProvince) {
        this.idProvince = idProvince;
    }
 
}