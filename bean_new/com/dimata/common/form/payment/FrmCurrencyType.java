/* 
 * Form Name  	:  FrmCurrencyType.java 
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

package com.dimata.common.form.payment;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.common.entity.payment.*;

public class FrmCurrencyType extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private CurrencyType currencyType;

	public static final String FRM_NAME_CURRENCYTYPE		=  "FRM_NAME_CURRENCYTYPE" ;

	public static final int FRM_FIELD_CURRENCY_TYPE_ID			=  0 ;
	public static final int FRM_FIELD_CODE                                  =  1 ;
	public static final int FRM_FIELD_NAME                          =  2 ;
	public static final int FRM_FIELD_DESCRIPTION			=  3 ;
        public static final int FRM_FIELD_TAB_INDEX			=  4 ;
        public static final int FRM_FIELD_INCLUDE_IN_PROCESS			=  5 ;

	public static String[] fieldNames = {
		"FRM_FIELD_CURRENCY_TYPE_ID",  "FRM_FIELD_CODE",
		"FRM_FIELD_NAME",  "FRM_FIELD_DESCRIPTION",
                "FRM_FIELD_TAB_INDEX","FRM_FIELD_INCLUDE_IN_PROCESS"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_STRING + ENTRY_REQUIRED,
		TYPE_STRING + ENTRY_REQUIRED,  TYPE_STRING,
                TYPE_INT,TYPE_INT
	} ;

	public FrmCurrencyType(){
	}
	public FrmCurrencyType(CurrencyType currencyType){
		this.currencyType = currencyType;
	}

	public FrmCurrencyType(HttpServletRequest request, CurrencyType currencyType){
		super(new FrmCurrencyType(currencyType), request);
		this.currencyType = currencyType;
	}

	public String getFormName() { return FRM_NAME_CURRENCYTYPE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public CurrencyType getEntityObject(){ return currencyType; }

	public void requestEntityObject(CurrencyType currencyType) {
		try{
			this.requestParam();
			currencyType.setCode(getString(FRM_FIELD_CODE));
			currencyType.setName(getString(FRM_FIELD_NAME));
			currencyType.setDescription(getString(FRM_FIELD_DESCRIPTION));
                        currencyType.setTabIndex(getInt(FRM_FIELD_TAB_INDEX));
                        currencyType.setIncludeInProcess(getInt(FRM_FIELD_INCLUDE_IN_PROCESS));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
