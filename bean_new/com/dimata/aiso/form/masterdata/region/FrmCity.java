/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.region;

import com.dimata.aiso.entity.masterdata.region.City;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class FrmCity extends FRMHandler implements I_FRMInterface, I_FRMType {

    private City city;

    public static final String FRM_TB_CITY		=  "FRM_TB_CITY" ;
    public static final int FRM_FIELD_CITY_ID           =  0 ;
    public static final int FRM_FIELD_CITY_NAME         =  1 ;
    
    //update tanggal 7 Maret 2013 oleh Hadi
    public static final int FRM_FIELD_PROVINCE_ID = 2;
    
    public static String[] fieldNames = {
		"FRM_FIELD_CITY_ID", 
                "FRM_FIELD_CITY_NAME",
            
            //update
            "FRM_FIELD_PROVINCE_ID"
	} ;
        
      
	public static int[] fieldTypes = {
                TYPE_LONG,
		TYPE_STRING + ENTRY_REQUIRED,
                
                //UPDATE
                TYPE_LONG
	} ;
   
        
        
	public FrmCity (){
	}

	public FrmCity(City city){
		this.city = city;
	}


	public FrmCity(HttpServletRequest request, City city){
		super(new FrmCity(city), request);
		this.city = city;
	}


	public String getFormName() { 
            return FRM_TB_CITY; 
        }

	public int[] getFieldTypes() {
            return fieldTypes; 
        }

	public String[] getFieldNames() { 
            return fieldNames; 
        }

	public int getFieldSize() { 
            return fieldNames.length; 
        }

	public City getEntityObject(){ 
            return city; 
        }

	public void requestEntityObject(City city) {
		try{
			this.requestParam();
			city.setCityName(getString(FRM_FIELD_CITY_NAME));
                        city.setIdProvince(getLong(FRM_FIELD_PROVINCE_ID));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
