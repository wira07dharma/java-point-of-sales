/* 
 * Form Name  	:  FrmPersonalDiscount.java 
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

package com.dimata.posbo.form.masterdata;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.gui.jsp.*;

public class FrmPersonalDiscount extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private PersonalDiscount personalDiscount;

	public static final String FRM_PERSONAL_DISCOUNT		=  "FRM_PERSONAL_DISCOUNT" ;

	public static final int FRM_PERSONAL_DISCOUNT_ID			=  0 ;
	public static final int FRM_MATERIAL_ID			=  1 ;
	public static final int FRM_PERSONAL_DISC_VAL			=  2 ;
	public static final int FRM_PERSONAL_DISC_PCT			=  3 ;
        public static final int FRM_CONTACT_ID			=  4 ;

	public static String[] fieldNames = {
		"FRM_PERSONAL_DISCOUNT_ID",  "FRM_MATERIAL_ID",
		"FRM_PERSONAL_DISC_VAL",  "FRM_PERSONAL_DISC_PCT",
                "FRM_CONTACT_ID"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_FLOAT,  TYPE_FLOAT,
                TYPE_LONG
	} ;

	public FrmPersonalDiscount(){
	}
	public FrmPersonalDiscount(PersonalDiscount personalDiscount){
		this.personalDiscount = personalDiscount;
	}

	public FrmPersonalDiscount(HttpServletRequest request, PersonalDiscount personalDiscount){
		super(new FrmPersonalDiscount(personalDiscount), request);                                                
		this.personalDiscount = personalDiscount;
	}

	public String getFormName() { return FRM_PERSONAL_DISCOUNT; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public PersonalDiscount getEntityObject(){ return personalDiscount; }

	public void requestEntityObject(PersonalDiscount personalDiscount) {
		try{
			this.requestParam();
			personalDiscount.setMaterialId(getLong(FRM_MATERIAL_ID));
			personalDiscount.setPersDiscVal(getDouble(FRM_PERSONAL_DISC_VAL));                        
                        personalDiscount.setPersDiscPct(getDouble(FRM_PERSONAL_DISC_PCT));                        
			personalDiscount.setContactId(getLong(FRM_CONTACT_ID));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
