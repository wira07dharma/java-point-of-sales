/* 
 * Form Name  	:  FrmBackUpService.java 
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

public class FrmBackUpService extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private BackUpService backUpService;

	public static final String FRM_NAME_BACKUPSERVICE		=  "FRM_NAME_BACKUPSERVICE" ;

	public static final int FRM_FIELD_BACK_UP_CONF_ID			=  0 ;
	public static final int FRM_FIELD_START_TIME			=  1 ;
	public static final int FRM_FIELD_PERIODE			=  2 ;
	public static final int FRM_FIELD_TARGET1			=  3 ;
	public static final int FRM_FIELD_TARGET2			=  4 ;
	public static final int FRM_FIELD_TARGET3			=  5 ;
	public static final int FRM_FIELD_SOURCE_PATH			=  6 ;

	public static String[] fieldNames = {
		"FRM_FIELD_BACK_UP_CONF_ID",  "FRM_FIELD_START_TIME",
		"FRM_FIELD_PERIODE",  "FRM_FIELD_TARGET1",
		"FRM_FIELD_TARGET2",  "FRM_FIELD_TARGET3",
		"FRM_FIELD_SOURCE_PATH"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_DATE,
		TYPE_INT + ENTRY_REQUIRED,  TYPE_STRING + ENTRY_REQUIRED,
		TYPE_STRING,  TYPE_STRING,
		TYPE_STRING + ENTRY_REQUIRED
	} ;

	public FrmBackUpService(){
	}
	public FrmBackUpService(BackUpService backUpService){
		this.backUpService = backUpService;
	}

	public FrmBackUpService(HttpServletRequest request, BackUpService backUpService){
		super(new FrmBackUpService(backUpService), request);
		this.backUpService = backUpService;
	}

	public String getFormName() { return FRM_NAME_BACKUPSERVICE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public BackUpService getEntityObject(){ return backUpService; }

	public void requestEntityObject(BackUpService backUpService) {
		try{
			this.requestParam();
			backUpService.setStartTime(getDate(FRM_FIELD_START_TIME));
			backUpService.setPeriode(getInt(FRM_FIELD_PERIODE));
			backUpService.setTarget1(getString(FRM_FIELD_TARGET1));
			backUpService.setTarget2(getString(FRM_FIELD_TARGET2));
			backUpService.setTarget3(getString(FRM_FIELD_TARGET3));
			backUpService.setSourcePath(getString(FRM_FIELD_SOURCE_PATH));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
