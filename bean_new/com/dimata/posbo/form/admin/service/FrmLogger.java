/*
 * Form Name  	:  FrmLogger.java 
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

package com.dimata.posbo.form.admin.service;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.posbo.entity.admin.service.*;

public class FrmLogger extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private Logger logger;

	public static final String FRM_NAME_LOGGER		=  "FRM_NAME_LOGGER" ;

	public static final int FRM_FIELD_LOGGER_ID			=  0 ;
	public static final int FRM_FIELD_DATE_CREATED			=  1 ;
	public static final int FRM_FIELD_TIME_CREATED			=  2 ;
	public static final int FRM_FIELD_TARGET1_NOTE			=  3 ;
	public static final int FRM_FIELD_TARGET2_NOTE			=  4 ;
	public static final int FRM_FIELD_TARGET3_NOTE			=  5 ;

	public static String[] fieldNames = {
		"FRM_FIELD_LOGGER_ID",  "FRM_FIELD_DATE_CREATED",
		"FRM_FIELD_TIME_CREATED",  "FRM_FIELD_TARGET1_NOTE",
		"FRM_FIELD_TARGET2_NOTE",  "FRM_FIELD_TARGET3_NOTE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_DATE,
		TYPE_DATE,  TYPE_STRING,
		TYPE_STRING,  TYPE_STRING
	} ;

	public FrmLogger(){
	}
	public FrmLogger(Logger logger){
		this.logger = logger;
	}

	public FrmLogger(HttpServletRequest request, Logger logger){
		super(new FrmLogger(logger), request);
		this.logger = logger;
	}

	public String getFormName() { return FRM_NAME_LOGGER; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public Logger getEntityObject(){ return logger; }

	public void requestEntityObject(Logger logger) {
		try{
			this.requestParam();
			logger.setDateCreated(getDate(FRM_FIELD_DATE_CREATED));
			//logger.setTimeCreated(getDate(FRM_FIELD_TIME_CREATED));
			logger.setTarget1Note(getString(FRM_FIELD_TARGET1_NOTE));
			logger.setTarget2Note(getString(FRM_FIELD_TARGET2_NOTE));
			logger.setTarget3Note(getString(FRM_FIELD_TARGET3_NOTE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
