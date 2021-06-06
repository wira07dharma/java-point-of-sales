/* 
 * Form Name  	:  FrmIjLocationMapping.java 
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

public class FrmIjLocationMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private IjLocationMapping ijLocationMapping;

	public static final String FRM_NAME_IJLOCATIONMAPPING		=  "FRM_NAME_IJLOCATIONMAPPING" ;

	public static final int FRM_FIELD_IJ_MAP_LOCATION_ID			=  0 ;
	public static final int FRM_FIELD_TRANSACTION_TYPE			=  1 ;
	public static final int FRM_FIELD_SALES_TYPE			=  2 ;
	public static final int FRM_FIELD_CURRENCY			=  3 ;
	public static final int FRM_FIELD_LOCATION			=  4 ;
	public static final int FRM_FIELD_PROD_DEPARTMENT			=  5 ;
	public static final int FRM_FIELD_ACCOUNT			=  6 ;
        public static final int FRM_FIELD_BO_SYSTEM			=  7 ;
        public static final int FRM_FIELD_PRICE_TYPE			=  8 ;

	public static String[] fieldNames = {
		"FRM_FIELD_IJ_MAP_LOCATION_ID",  
                "FRM_FIELD_TRANSACTION_TYPE",
		"FRM_FIELD_SALES_TYPE",  
                "FRM_FIELD_CURRENCY",
		"FRM_FIELD_LOCATION",  
                "FRM_FIELD_PROD_DEPARTMENT",
		"FRM_FIELD_ACCOUNT",
                "FRM_FIELD_BO_SYSTEM",
                "FRM_FIELD_PRICE_TYPE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  
                TYPE_INT,
		TYPE_INT,  
                TYPE_LONG,
		TYPE_LONG,  
                TYPE_LONG,
		TYPE_LONG,
                TYPE_INT,
                TYPE_LONG
	} ;

	public FrmIjLocationMapping(){
	}
	public FrmIjLocationMapping(IjLocationMapping ijLocationMapping){
		this.ijLocationMapping = ijLocationMapping;
	}

	public FrmIjLocationMapping(HttpServletRequest request, IjLocationMapping ijLocationMapping){
		super(new FrmIjLocationMapping(ijLocationMapping), request);
		this.ijLocationMapping = ijLocationMapping;
	}

	public String getFormName() { return FRM_NAME_IJLOCATIONMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public IjLocationMapping getEntityObject(){ return ijLocationMapping; }

	public void requestEntityObject(IjLocationMapping ijLocationMapping) {
		try{
			this.requestParam();
			ijLocationMapping.setTransactionType(getInt(FRM_FIELD_TRANSACTION_TYPE));
			ijLocationMapping.setSalesType(getInt(FRM_FIELD_SALES_TYPE));
			ijLocationMapping.setCurrency(getLong(FRM_FIELD_CURRENCY));
			ijLocationMapping.setLocation(getLong(FRM_FIELD_LOCATION));
			ijLocationMapping.setProdDepartment(getLong(FRM_FIELD_PROD_DEPARTMENT));
			ijLocationMapping.setAccount(getLong(FRM_FIELD_ACCOUNT));
                        ijLocationMapping.setBoSystem(getInt(FRM_FIELD_BO_SYSTEM));
                        ijLocationMapping.setPriceType(getLong(FRM_FIELD_PRICE_TYPE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
