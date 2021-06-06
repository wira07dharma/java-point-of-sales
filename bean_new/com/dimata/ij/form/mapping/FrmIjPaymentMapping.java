/* 
 * Form Name  	:  FrmIjPaymentMapping.java 
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

package com.dimata.ij.form.mapping;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.ij.entity.mapping.*;

public class FrmIjPaymentMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private IjPaymentMapping ijPaymentMapping;

	public static final String FRM_NAME_IJPAYMENTMAPPING		=  "FRM_NAME_IJPAYMENTMAPPING" ;

	public static final int FRM_FIELD_IJ_MAP_PAYMENT_ID		=  0 ;
	public static final int FRM_FIELD_PAYMENT_SYSTEM		=  1 ;
	public static final int FRM_FIELD_CURRENCY			=  2 ;
	public static final int FRM_FIELD_ACCOUNT			=  3 ;
        public static final int FRM_FIELD_BO_SYSTEM			=  4 ;
        public static final int FRM_FIELD_LOCATION			=  5 ;

	public static String[] fieldNames = {
		"FRM_FIELD_IJ_MAP_PAYMENT_ID",  
                "FRM_FIELD_PAYMENT_SYSTEM",
		"FRM_FIELD_CURRENCY",  
                "FRM_FIELD_ACCOUNT",
                "FRM_FIELD_BO_SYSTEM",
                "FRM_FIELD_LOCATION"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  
                TYPE_LONG,
		TYPE_LONG,  
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG
	} ;

	public FrmIjPaymentMapping(){
	}
	public FrmIjPaymentMapping(IjPaymentMapping ijPaymentMapping){
		this.ijPaymentMapping = ijPaymentMapping;
	}

	public FrmIjPaymentMapping(HttpServletRequest request, IjPaymentMapping ijPaymentMapping){
		super(new FrmIjPaymentMapping(ijPaymentMapping), request);
		this.ijPaymentMapping = ijPaymentMapping;
	}

	public String getFormName() { return FRM_NAME_IJPAYMENTMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; }   

	public IjPaymentMapping getEntityObject(){ return ijPaymentMapping; }

	public void requestEntityObject(IjPaymentMapping ijPaymentMapping) {
		try{
			this.requestParam();
			ijPaymentMapping.setPaymentSystem(getLong(FRM_FIELD_PAYMENT_SYSTEM));
			ijPaymentMapping.setCurrency(getLong(FRM_FIELD_CURRENCY));
			ijPaymentMapping.setAccount(getLong(FRM_FIELD_ACCOUNT));
                        ijPaymentMapping.setBoSystem(getInt(FRM_FIELD_BO_SYSTEM));
                        ijPaymentMapping.setLocation(getLong(FRM_FIELD_LOCATION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
