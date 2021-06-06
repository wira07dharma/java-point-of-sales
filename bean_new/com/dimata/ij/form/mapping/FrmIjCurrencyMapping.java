/* 
 * Form Name  	:  FrmIjCurrencyMapping.java 
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

public class FrmIjCurrencyMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private IjCurrencyMapping ijCurrencyMapping;

	public static final String FRM_NAME_IJCURRENCYMAPPING		=  "FRM_NAME_IJCURRENCYMAPPING" ;

	public static final int FRM_FIELD_IJ_MAP_CURR_ID		=  0 ;
	public static final int FRM_FIELD_BO_CURRENCY			=  1 ;
	public static final int FRM_FIELD_AISO_CURRENCY			=  2 ;
        public static final int FRM_FIELD_BO_SYSTEM			=  3 ;        

	public static String[] fieldNames = {
		"FRM_FIELD_IJ_MAP_CURR_ID",  
                "FRM_FIELD_BO_CURRENCY",
		"FRM_FIELD_AISO_CURRENCY",
                "FRM_FIELD_BO_SYSTEM"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  
                TYPE_LONG,
		TYPE_LONG,
                TYPE_INT
	} ;

	public FrmIjCurrencyMapping(){
	}
	public FrmIjCurrencyMapping(IjCurrencyMapping ijCurrencyMapping){
		this.ijCurrencyMapping = ijCurrencyMapping;
	}

	public FrmIjCurrencyMapping(HttpServletRequest request, IjCurrencyMapping ijCurrencyMapping){
		super(new FrmIjCurrencyMapping(ijCurrencyMapping), request);
		this.ijCurrencyMapping = ijCurrencyMapping;
	}

	public String getFormName() { return FRM_NAME_IJCURRENCYMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public IjCurrencyMapping getEntityObject(){ return ijCurrencyMapping; }

	public void requestEntityObject(IjCurrencyMapping ijCurrencyMapping) {
		try{
			this.requestParam();
			ijCurrencyMapping.setBoCurrency(getLong(FRM_FIELD_BO_CURRENCY));
			ijCurrencyMapping.setAisoCurrency(getLong(FRM_FIELD_AISO_CURRENCY));
                        ijCurrencyMapping.setBoSystem(getInt(FRM_FIELD_BO_SYSTEM));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
