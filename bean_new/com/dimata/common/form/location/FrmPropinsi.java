/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.location;

import com.dimata.common.entity.location.Provinsi;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Acer
 */
public class FrmPropinsi extends FRMHandler implements I_FRMInterface, I_FRMType  {
    
    private Provinsi provinsi;

	public static final String FRM_NAME_PROPINSI		=  "FRM_NAME_PROPINSI" ;

	public static final int FRM_FIELD_ID_PROPINSI			=  0 ;
	public static final int FRM_FIELD_KD_PROPINSI			=  1 ;
	public static final int FRM_FIELD_NM_PROPINSI			=  2 ;
         public static final int FRM_FIELD_ID_NEGARA			=  3 ;

	public static String[] fieldNames = {
		"FRM_FIELD_ID_PROPINSI",  "FRM_FIELD_KD_PROPINSI",
		"FRM_FIELD_NM_PROPINSI",  "FRM_FIELD_ID_NEGARA"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG ,  TYPE_STRING,
		TYPE_STRING + ENTRY_REQUIRED, TYPE_LONG + ENTRY_REQUIRED
	} ;

	public FrmPropinsi(){
	}
	public FrmPropinsi(Provinsi provinsi){
		this.provinsi = provinsi;
	}

	public FrmPropinsi(HttpServletRequest request, Provinsi provinsi){
		super(new FrmPropinsi(provinsi), request);
		this.provinsi = provinsi;
	}

	public String getFormName() { return FRM_NAME_PROPINSI; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public Provinsi getEntityObject(){ return provinsi; }

	public void requestEntityObject(Provinsi provinsi) {
		try{
			this.requestParam();
			provinsi.setKdProvinsi(getString(FRM_FIELD_KD_PROPINSI));
			provinsi.setNmProvinsi(getString(FRM_FIELD_NM_PROPINSI));
                        provinsi.setIdNegara(getLong(FRM_FIELD_ID_NEGARA));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
    
}
