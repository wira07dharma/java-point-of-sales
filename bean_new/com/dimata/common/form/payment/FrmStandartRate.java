/* 
 * Form Name  	:  FrmStandartRate.java 
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

public class FrmStandartRate extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private StandartRate standartRate;

	public static final String FRM_NAME_STANDARTRATE		=  "FRM_NAME_STANDARTRATE" ;

	public static final int FRM_FIELD_STANDART_RATE_ID			=  0 ;
	public static final int FRM_FIELD_CURRENCY_TYPE_ID			=  1 ;
	public static final int FRM_FIELD_SELLING_RATE			=  2 ;
	public static final int FRM_FIELD_START_DATE			=  3 ;
	public static final int FRM_FIELD_END_DATE			=  4 ;
	public static final int FRM_FIELD_STATUS			=  5 ;
       // public static final int FRM_FIELD_INCLUDE_IN_PROCESS			=  6 ;

	public static String[] fieldNames = {
		"FRM_FIELD_STANDART_RATE_ID",  "FRM_FIELD_CURRENCY_TYPE_ID",
		"FRM_FIELD_SELLING_RATE",  "FRM_FIELD_START_DATE",
		"FRM_FIELD_END_DATE",  "FRM_FIELD_STATUS"/*,
                "FRM_FIELD_INCLUDE_IN_PROCESS"*/
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_FLOAT + ENTRY_REQUIRED,  TYPE_DATE,
		TYPE_DATE,  TYPE_INT/*, TYPE_INT*/
	} ;

	public FrmStandartRate(){
	}
	public FrmStandartRate(StandartRate standartRate){
		this.standartRate = standartRate;
	}

	public FrmStandartRate(HttpServletRequest request, StandartRate standartRate){
		super(new FrmStandartRate(standartRate), request);
		this.standartRate = standartRate;
	}

	public String getFormName() { return FRM_NAME_STANDARTRATE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public StandartRate getEntityObject(){ return standartRate; }

	public void requestEntityObject(StandartRate standartRate) {
		try{
			this.requestParam();
			standartRate.setCurrencyTypeId(getLong(FRM_FIELD_CURRENCY_TYPE_ID));
			standartRate.setSellingRate(getDouble(FRM_FIELD_SELLING_RATE));
			standartRate.setStartDate(getDate(FRM_FIELD_START_DATE));
			standartRate.setEndDate(getDate(FRM_FIELD_END_DATE));
			standartRate.setStatus(getInt(FRM_FIELD_STATUS));
                        //standartRate.setIncludeInProcess(getInt(FRM_FIELD_INCLUDE_IN_PROCESS));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
