/* 
 * Form Name  	:  FrmReligion.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya 
 * @version  	: 01 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.form;

/* java package */ 
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.harisma.entity.masterdata.*;

public class FrmReligion extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private Religion religion;

	public static final String FRM_NAME_RELIGION		=  "FRM_NAME_RELIGION" ;

	public static final int FRM_FIELD_RELIGION_ID			=  0 ;
	public static final int FRM_FIELD_RELIGION			=  1 ;

	public static String[] fieldNames = {
		"FRM_FIELD_RELIGION_ID",  "FRM_FIELD_RELIGION"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_STRING + ENTRY_REQUIRED
	} ;

	public FrmReligion(){
	}
	public FrmReligion(Religion religion){
		this.religion = religion;
	}

	public FrmReligion(HttpServletRequest request, Religion religion){
		super(new FrmReligion(religion), request);
		this.religion = religion;
	}

	public String getFormName() { return FRM_NAME_RELIGION; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public Religion getEntityObject(){ return religion; }

	public void requestEntityObject(Religion religion) {
		try{
			this.requestParam();
			religion.setReligion(getString(FRM_FIELD_RELIGION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
