/* 
 * Form Name  	:  FrmIjConfiguration.java 
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

package com.dimata.ij.form.configuration;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.ij.entity.configuration.*;

public class FrmIjConfiguration extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private IjConfiguration ijConfiguration;

	public static final String FRM_NAME_IJCONFIGURATION		=  "FRM_NAME_IJCONFIGURATION" ;

	public static final int FRM_FIELD_IJ_CONFIGURATION_ID			=  0 ;
	public static final int FRM_FIELD_BO_SYSTEM			=  1 ;
	public static final int FRM_FIELD_CONFIG_GROUP			=  2 ;
	public static final int FRM_FIELD_CONFIG_ITEM			=  3 ;
	public static final int FRM_FIELD_CONFIG_SELECT			=  4 ;
        public static final int FRM_FIELD_IJ_IMPL_CLASS			=  5 ;

	public static String[] fieldNames = {
		"FRM_FIELD_IJ_CONFIGURATION_ID",  "FRM_FIELD_BO_SYSTEM",
		"FRM_FIELD_CONFIG_GROUP",  "FRM_FIELD_CONFIG_ITEM",
		"FRM_FIELD_CONFIG_SELECT", "FRM_FIELD_IJ_IMPL_CLASS"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_INT + ENTRY_REQUIRED,
		TYPE_INT + ENTRY_REQUIRED,  TYPE_INT + ENTRY_REQUIRED,
		TYPE_INT + ENTRY_REQUIRED, TYPE_STRING
	} ;

	public FrmIjConfiguration(){
	}
	public FrmIjConfiguration(IjConfiguration ijConfiguration){
		this.ijConfiguration = ijConfiguration;
	}

	public FrmIjConfiguration(HttpServletRequest request, IjConfiguration ijConfiguration){
		super(new FrmIjConfiguration(ijConfiguration), request);
		this.ijConfiguration = ijConfiguration;
	}

	public String getFormName() { return FRM_NAME_IJCONFIGURATION; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public IjConfiguration getEntityObject(){ return ijConfiguration; }

	public void requestEntityObject(IjConfiguration ijConfiguration) {
		try{
			this.requestParam(); 
			ijConfiguration.setBoSystem(getInt(FRM_FIELD_BO_SYSTEM));
			ijConfiguration.setConfigGroup(getInt(FRM_FIELD_CONFIG_GROUP));
			ijConfiguration.setConfigItem(getInt(FRM_FIELD_CONFIG_ITEM));
			ijConfiguration.setConfigSelect(getInt(FRM_FIELD_CONFIG_SELECT));
                        ijConfiguration.setSIjImplClass(getString(FRM_FIELD_IJ_IMPL_CLASS));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
