/* 
 * Form Name  	:  FrmIjAccountMapping.java 
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

public class FrmIjAccountMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private IjAccountMapping ijAccountMapping;

	public static final String FRM_NAME_IJACCOUNTMAPPING		=  "FRM_NAME_IJACCOUNTMAPPING" ;

	public static final int FRM_FIELD_IJ_MAP_ACCOUNT_ID		=  0 ;
	public static final int FRM_FIELD_JOURNAL_TYPE			=  1 ;
	public static final int FRM_FIELD_CURRENCY			=  2 ;
	public static final int FRM_FIELD_ACCOUNT			=  3 ;
        public static final int FRM_FIELD_BO_SYSTEM			=  4 ;
        public static final int FRM_FIELD_LOCATION			=  5 ;

	public static String[] fieldNames = {
		"FRM_FIELD_IJ_MAP_ACCOUNT_ID",  
                "FRM_FIELD_JOURNAL_TYPE",
		"FRM_FIELD_CURRENCY",  
                "FRM_FIELD_ACCOUNT",
                "FRM_FIELD_BO_SYSTEM",
                "FRM_FIELD_LOCATION"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  
                TYPE_INT,
		TYPE_LONG,  
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG
	} ;

	public FrmIjAccountMapping(){
	}
	public FrmIjAccountMapping(IjAccountMapping ijAccountMapping){
		this.ijAccountMapping = ijAccountMapping;
	}

	public FrmIjAccountMapping(HttpServletRequest request, IjAccountMapping ijAccountMapping){
		super(new FrmIjAccountMapping(ijAccountMapping), request);
		this.ijAccountMapping = ijAccountMapping;
	}

	public String getFormName() { return FRM_NAME_IJACCOUNTMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public IjAccountMapping getEntityObject(){ return ijAccountMapping; }

	public void requestEntityObject(IjAccountMapping ijAccountMapping) {
		try{
			this.requestParam();
			ijAccountMapping.setJournalType(getInt(FRM_FIELD_JOURNAL_TYPE));
			ijAccountMapping.setCurrency(getLong(FRM_FIELD_CURRENCY));
			ijAccountMapping.setAccount(getLong(FRM_FIELD_ACCOUNT));
                        ijAccountMapping.setBoSystem(getInt(FRM_FIELD_BO_SYSTEM));
                        ijAccountMapping.setLocation(getLong(FRM_FIELD_LOCATION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
