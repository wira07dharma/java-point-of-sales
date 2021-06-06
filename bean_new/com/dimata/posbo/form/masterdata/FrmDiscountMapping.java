/* 
 * Form Name  	:  FrmDiscountMapping.java 
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

public class FrmDiscountMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DiscountMapping discountMapping;

	public static final String FRM_NAME_DISCOUNTMAPPING		=  "FRM_NAME_DISCOUNTMAPPING" ;

	public static final int FRM_FIELD_DISCOUNT_TYPE_ID			=  0 ;
	public static final int FRM_FIELD_MATERIAL_ID			=  1 ;
	public static final int FRM_FIELD_CURRENCY_TYPE_ID			=  2 ;
	//public static final int FRM_FIELD_DISCOUNT_TYPE			=  3 ;
	//public static final int FRM_FIELD_DISCOUNT			=  4 ;
        public static final int FRM_FIELD_DISCOUNT_PCT			=  3 ;
        public static final int FRM_FIELD_DISCOUNT_VALUE		=  4 ;


	public static String[] fieldNames = {
		"FRM_FIELD_DISCOUNT_TYPE_ID",  "FRM_FIELD_MATERIAL_ID",
		"FRM_FIELD_CURRENCY_TYPE_ID",  
                "FRM_FIELD_DISCOUNT_PCT",
                "FRM_FIELD_DISCOUNT_VALUE"
                /*"FRM_FIELD_DISCOUNT_TYPE",
		"FRM_FIELD_DISCOUNT"*/
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG + ENTRY_REQUIRED,  TYPE_LONG + ENTRY_REQUIRED,
		TYPE_LONG + ENTRY_REQUIRED,  /*TYPE_INT,
		TYPE_FLOAT + ENTRY_REQUIRED*/
                TYPE_FLOAT, TYPE_FLOAT
	} ;

	public FrmDiscountMapping(){
	}
	public FrmDiscountMapping(DiscountMapping discountMapping){
		this.discountMapping = discountMapping;
	}

	public FrmDiscountMapping(HttpServletRequest request, DiscountMapping discountMapping){
		super(new FrmDiscountMapping(discountMapping), request);
		this.discountMapping = discountMapping;
	}

	public String getFormName() { return FRM_NAME_DISCOUNTMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DiscountMapping getEntityObject(){ return discountMapping; }

	public void requestEntityObject(DiscountMapping discountMapping) {
		try{
			this.requestParam();
			//discountMapping.setDiscountType(getInt(FRM_FIELD_DISCOUNT_TYPE));
			//discountMapping.setDiscount(getDouble(FRM_FIELD_DISCOUNT));
                        discountMapping.setDiscountPct(getDouble(FRM_FIELD_DISCOUNT_PCT));
                        discountMapping.setDiscountValue(getDouble(FRM_FIELD_DISCOUNT_VALUE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
