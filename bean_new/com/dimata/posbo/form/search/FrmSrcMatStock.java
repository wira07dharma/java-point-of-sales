/* 
 * Form Name  	:  FrmSrcMaterialStock.java 
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

public class FrmSrcMatStock extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcMatStock srcMaterialStock;

	public static final String FRM_NAME_SRCMATERIALSTOCK		=  "FRM_NAME_SRCMATERIALSTOCK" ;

	public static final int FRM_FIELD_LOCATION			=  0 ;
	public static final int FRM_FIELD_MATERIALGROUP		=  1 ;
	public static final int FRM_FIELD_CODE				=  2 ;
	public static final int FRM_FIELD_MATERIAL			=  3 ;
	public static final int FRM_FIELD_ORDERBY			=  4 ;

	public static String[] fieldNames = {
		"FRM_FIELD_LOCATION",
        "FRM_FIELD_MATERIALGROUP",
        "FRM_FIELD_CODE",
		"FRM_FIELD_MATERIAL",
        "FRM_FIELD_ORDERBY"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
		TYPE_LONG,
        TYPE_INT
	} ;

    public static final int SORT_BY_LOCATION	= 0;
    public static final int SORT_BY_GROUP		= 1;
    public static final int SORT_BY_CODE		= 2;
    public static final int SORT_BY_MATERIAL	= 3;

    public static String[] sortFieldNames = {
			"Location", // location name
            "Group", // material group
            "Code",  // material code
            "Name"  // material name
    };
    public static String[] sortFieldKey = {
        	"NAME", // location name
            "NAME", // group name
            "CODE", // materialcode
            "NAME" // material name
    };

    public static Vector getSortFieldNames(){
        Vector result = new Vector(1,1);
        for(int i=0; i<sortFieldNames.length; i++){
            result.add(String.valueOf(sortFieldNames[i]));
        }
        return result;
    }

    public static Vector getSortFieldKey(){
        Vector result = new Vector(1,1);
        for(int i=0; i<sortFieldKey.length; i++){
            result.add(String.valueOf(sortFieldKey[i]));
        }
        return result;
    }

	public FrmSrcMatStock(){
	}
	public FrmSrcMatStock(SrcMatStock srcMaterialStock){
		this.srcMaterialStock = srcMaterialStock;
	}

	public FrmSrcMatStock(HttpServletRequest request, SrcMatStock srcMaterialStock){
		super(new FrmSrcMatStock(srcMaterialStock), request);
		this.srcMaterialStock = srcMaterialStock;
	}

	public String getFormName() { return FRM_NAME_SRCMATERIALSTOCK; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcMatStock getEntityObject(){ return srcMaterialStock; }

	public void requestEntityObject(SrcMatStock srcMaterialStock) {
		try{
			this.requestParam();
			srcMaterialStock.setLocation(getLong(FRM_FIELD_LOCATION));
			srcMaterialStock.setMaterialgroup(getLong(FRM_FIELD_MATERIALGROUP));
			srcMaterialStock.setCode(getString(FRM_FIELD_CODE));
			srcMaterialStock.setMaterial(getLong(FRM_FIELD_MATERIAL));
			srcMaterialStock.setOrderby(getInt(FRM_FIELD_ORDERBY));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
