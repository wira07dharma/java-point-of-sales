/* 
 * Form Name  	:  FrmInventoryGroup.java 
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

public class FrmSubCategory extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SubCategory subCategory;

	public static final String FRM_NAME_SUB_CATEGORY    =  "FRM_NAME_SUB_CATEGORY" ;

	public static final int FRM_FIELD_SUB_CATEGORY_ID   =  0 ;
	public static final int FRM_FIELD_CATEGORY_ID       =  1 ;
	public static final int FRM_FIELD_CODE              =  2 ;
	public static final int FRM_FIELD_NAME              =  3 ;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_SUB_CATEGORY_ID",
            "FRM_FIELD_CATEGORY_ID",
            "FRM_FIELD_CODE",
            "FRM_FIELD_NAME"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_LONG,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING + ENTRY_REQUIRED
	} ;

	public FrmSubCategory(){
	}
	public FrmSubCategory(SubCategory subCategory){
		this.subCategory = subCategory;
	}

	public FrmSubCategory(HttpServletRequest request, SubCategory subCategory){
		super(new FrmSubCategory(subCategory), request);
		this.subCategory = subCategory;
	}

	public String getFormName() { return FRM_NAME_SUB_CATEGORY; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SubCategory getEntityObject(){ return subCategory; }

	public void requestEntityObject(SubCategory subCategory) 
        {
		try
                {
			this.requestParam();
			subCategory.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
			subCategory.setCode(getString(FRM_FIELD_CODE));
			subCategory.setName(getString(FRM_FIELD_NAME));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
