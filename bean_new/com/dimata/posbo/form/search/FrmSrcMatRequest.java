/*
 * Form Name  	:  FrmSrcMaterialDispatch.java 
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

package com.dimata.posbo.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.search.*;

public class FrmSrcMatRequest extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcMatRequest srcMaterialDipatch;

	public static final String FRM_NAME_SRCMATERIALDISPATCH		=  "FRM_NAME_SRCMATERIALDISPATCH" ;

	public static final int FRM_FIELD_MAT_DF_SEARCH_ID			=  0 ;
	public static final int FRM_FIELD_FROM_DATE			=  1 ;
	public static final int FRM_FIELD_END_DATE			=  2 ;
	public static final int FRM_FIELD_MATERIAL_NAME			=  3 ;
	public static final int FRM_FIELD_MATERIAL_CODE			=  4 ;
	public static final int FRM_FIELD_DF_CODE			=  5 ;
	public static final int FRM_FIELD_STATUS			=  6 ;
	public static final int FRM_FIELD_SORT_BY			=  7 ;
	public static final int FRM_FIELD_FROM_LOCATION			=  8 ;
	public static final int FRM_FIELD_TO_LOCATION			=  9 ;
    public static final int FRM_FIELD_IGNORE_DATE			=  10 ;


	public static String[] fieldNames = {
		"FRM_FIELD_MAT_DF_SEARCH_ID",  "FRM_FIELD_FROM_DATE",
		"FRM_FIELD_END_DATE",  "FRM_FIELD_MATERIAL_NAME",
		"FRM_FIELD_MATERIAL_CODE",  "FRM_FIELD_DF_CODE",
		"FRM_FIELD_STATUS",  "FRM_FIELD_SORT_BY",
		"FRM_FIELD_FROM_LOCATION",  "FRM_FIELD_TO_LOCATION" ,
        "FRM_FIELD_IGNORE_DATE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_DATE,
		TYPE_DATE,  TYPE_STRING,
		TYPE_STRING,  TYPE_STRING,
		TYPE_INT,  TYPE_INT,
		TYPE_LONG,  TYPE_LONG,
        TYPE_BOOL
	} ;

	public FrmSrcMatRequest(){
	}
	public FrmSrcMatRequest(SrcMatRequest srcMaterialDipatch){
		this.srcMaterialDipatch = srcMaterialDipatch;
	}

	public FrmSrcMatRequest(HttpServletRequest request, SrcMatRequest srcMaterialDipatch){
		super(new FrmSrcMatRequest(srcMaterialDipatch), request);
		this.srcMaterialDipatch = srcMaterialDipatch;
	}

	public String getFormName() { return FRM_NAME_SRCMATERIALDISPATCH; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcMatRequest getEntityObject(){ return srcMaterialDipatch; }

	public void requestEntityObject(SrcMatRequest srcMaterialDipatch) {
		try{
			this.requestParam();
			srcMaterialDipatch.setFromDate(getDate(FRM_FIELD_FROM_DATE));
			srcMaterialDipatch.setEndDate(getDate(FRM_FIELD_END_DATE));
			srcMaterialDipatch.setMaterialName(getString(FRM_FIELD_MATERIAL_NAME));
			srcMaterialDipatch.setMaterialCode(getString(FRM_FIELD_MATERIAL_CODE));
			srcMaterialDipatch.setDfCode(getString(FRM_FIELD_DF_CODE));
			srcMaterialDipatch.setStatus(getInt(FRM_FIELD_STATUS));
			srcMaterialDipatch.setSortBy(getInt(FRM_FIELD_SORT_BY));
			srcMaterialDipatch.setFromLocation(getLong(FRM_FIELD_FROM_LOCATION));
			srcMaterialDipatch.setToLocation(getLong(FRM_FIELD_TO_LOCATION));
            srcMaterialDipatch.setIgnoreDate(getBoolean(FRM_FIELD_IGNORE_DATE));

		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
