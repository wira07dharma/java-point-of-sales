/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: May 4, 2004
 * Time: 8:55:30 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.common.form.search;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.common.entity.search.SrcLogger;

import javax.servlet.http.HttpServletRequest;

public class FrmSrcLogger extends FRMHandler implements I_FRMInterface, I_FRMType{
	private SrcLogger srcLogger;

	public static final String FRM_NAME_LOGGER		=  "FRM_NAME_LOGGER";

	public static final int FRM_FIELD_START_DATE			=  0 ;
	public static final int FRM_FIELD_END_DATE			    =  1 ;
    public static final int FRM_FIELD_EMPLOYEE_ID		    =  2 ;

	public static String[] fieldNames = {
		"FRM_FIELD_START_DATE",  "FRM_FIELD_END_DATE",
        "FRM_FIELD_EMPLOYEE_ID"
	} ;

	public static int[] fieldTypes = {
		TYPE_DATE, TYPE_DATE, TYPE_LONG
	} ;

	public FrmSrcLogger(){
	}
	public FrmSrcLogger(SrcLogger srcLogger){
		this.srcLogger = srcLogger;
	}

	public FrmSrcLogger(HttpServletRequest request, SrcLogger srcLogger){
		super(new FrmSrcLogger(srcLogger), request);
		this.srcLogger = srcLogger;
	}

	public String getFormName() { return FRM_NAME_LOGGER; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public SrcLogger getEntityObject(){ return srcLogger; }

	public void requestEntityObject(SrcLogger srcLogger) {
		try{
			this.requestParam();
			srcLogger.setStartDate(getDate(FRM_FIELD_START_DATE));
			srcLogger.setEndDate(getDate(FRM_FIELD_END_DATE));
            srcLogger.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
