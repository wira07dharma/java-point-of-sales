package com.dimata.aiso.entity.masterdata.mastertabungan;

/* package java */
import com.dimata.qdep.entity.*;
/**
 * @author Dede Nuharta
 */
public class MasterTabungan extends Entity {

    	private String savings_name = "";
  
	public String getSavingName(){
		return savings_name;
	}
        
 
	public void setSavingName(String savings_name){
		if ( savings_name == null ) {
			savings_name = "";
		}
		this.savings_name = savings_name;
	}
 
}