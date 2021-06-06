/* 
 * Form Name  	:  FrmDataCustom.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.form.custom;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.common.entity.custom.*;

public class FrmDataCustom extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DataCustom dataCustom;

	public static final String FRM_NAME_DATACUSTOM		=  "FRM_NAME_DATACUSTOM" ;

	public static final int FRM_FIELD_DATA_CUSTOM_ID			=  0 ;
	public static final int FRM_FIELD_OWNER_ID			=  1 ;
	public static final int FRM_FIELD_DATA_NAME			=  2 ;
	public static final int FRM_FIELD_LINK			=  3 ;
	public static final int FRM_FIELD_DATA_VALUE			=  4 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DATA_CUSTOM_ID",  "FRM_FIELD_OWNER_ID",
		"FRM_FIELD_DATA_NAME",  "FRM_FIELD_LINK",
		"FRM_FIELD_DATA_VALUE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_STRING,  TYPE_STRING,
		TYPE_STRING
	} ;

	public FrmDataCustom(){
	}
	public FrmDataCustom(DataCustom dataCustom){
		this.dataCustom = dataCustom;
	}

	public FrmDataCustom(HttpServletRequest request, DataCustom dataCustom){
		super(new FrmDataCustom(dataCustom), request);
		this.dataCustom = dataCustom;
	}

	public String getFormName() { return FRM_NAME_DATACUSTOM; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DataCustom getEntityObject(){ return dataCustom; }

	public void requestEntityObject(DataCustom dataCustom) {
		try{
			this.requestParam();
			dataCustom.setOwnerId(getLong(FRM_FIELD_OWNER_ID));
			dataCustom.setDataName(getString(FRM_FIELD_DATA_NAME));
			dataCustom.setLink(getString(FRM_FIELD_LINK));
			dataCustom.setDataValue(getString(FRM_FIELD_DATA_VALUE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
