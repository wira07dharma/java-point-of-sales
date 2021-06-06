/* 
 * Form Name  	:  FrmSrcTransferData.java 
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

package com.dimata.posbo.form.transferdata;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.transferdata.*;

public class FrmSrcTransferData extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcTransferData srcTransferData;

	public static final String FRM_NAME_SRCMEMBERREG		=  "FRM_NAME_SRC_TRANSFERDATA" ;

	public static final int FRM_FIELD_DATE_FROM			=  0 ;
	public static final int FRM_FIELD_DATE_TO			=  1 ;
	public static final int FRM_FIELD_INDEX_TABLE			=  2 ;
        public static final int FRM_FIELD_TYPE_TRANSFER			=  3 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DATE_FROM",  "FRM_FIELD_DATE_TO","FRM_FIELD_INDEX_TABLE",
                "FRM_FIELD_TYPE_TRANSFER"
	} ;

	public static int[] fieldTypes = {
		TYPE_DATE,  TYPE_DATE, TYPE_INT,
                TYPE_INT
		
	} ;

	public FrmSrcTransferData(){
	}
	public FrmSrcTransferData(SrcTransferData srcTransferData){
		this.srcTransferData = srcTransferData;
	}

	public FrmSrcTransferData(HttpServletRequest request, SrcTransferData srcTransferData){
		super(new FrmSrcTransferData(srcTransferData), request);
		this.srcTransferData = srcTransferData;
	}

	public String getFormName() { return FRM_NAME_SRCMEMBERREG; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcTransferData getEntityObject(){ return srcTransferData; }

	public void requestEntityObject(SrcTransferData srcTransferData) {
		try{
			this.requestParam();
			srcTransferData.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
			srcTransferData.setDateTo(getDate(FRM_FIELD_DATE_TO));
			srcTransferData.setIndexTable(getInt(FRM_FIELD_INDEX_TABLE));
                        srcTransferData.setTypeTransfer(getInt(FRM_FIELD_TYPE_TRANSFER));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
