/**
 * Created on 	: 3:00 PM
 * @author	    : gedhy
 * @version	    : 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.workflow.form.approval;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*;

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.workflow.entity.approval.*;
import com.dimata.workflow.entity.status.*;

public class FrmDocType extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DocType docType;

	public static final String FRM_NAME_DOCTYPE		=  "FRM_NAME_DOCTYPE" ;

	public static final int FRM_FIELD_DOCTYPE_OID	=  0 ;
	public static final int FRM_FIELD_SYSTEM_TYPE	=  1 ;
	public static final int FRM_FIELD_DOCUMENT_TYPE	=  2 ;
	public static final int FRM_FIELD_CODE			=  3 ;
	public static final int FRM_FIELD_NAME			=  4 ;
	public static final int FRM_FIELD_DESCRIPTION	=  5 ;
	public static final int FRM_FIELD_DOCSTATUS		=  6 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DOCTYPE_OID",
        "FRM_FIELD_SYSTEM_TYPE",
        "FRM_FIELD_DOCUMENT_TYPE",
		"FRM_FIELD_CODE",
        "FRM_FIELD_NAME",
		"FRM_FIELD_DESCRIPTION",
        "FRM_FIELD_DOC_STATUS"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
		TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
		TYPE_STRING,
        TYPE_COLLECTION
	} ;

	public FrmDocType(){
	}

	public FrmDocType(DocType docType){
		this.docType = docType;
	}

	public FrmDocType(HttpServletRequest request, DocType docType){
		super(new FrmDocType(docType), request);
		this.docType = docType;
	}

	public String getFormName() { return FRM_NAME_DOCTYPE; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DocType getEntityObject(){ return docType; }

	public void requestEntityObject(DocType docType) {
		try{
			this.requestParam();
            PstDocType pstDocType = new PstDocType();
            int iDocType = pstDocType.composeDocumentType(getInt(FRM_FIELD_SYSTEM_TYPE),getInt(FRM_FIELD_DOCUMENT_TYPE));
			docType.setType(iDocType);
			docType.setCode(getString(FRM_FIELD_CODE));
			docType.setName(getString(FRM_FIELD_NAME));
			docType.setDescription(getString(FRM_FIELD_DESCRIPTION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

    /**
     * has to be call after requestEntityObject
     * return Vector of PROCESS STATUS objects
     **/
    /*public Vector getDocumentStatus(int docType){
        Vector result = new Vector(1,1);
        Vector vectDocStatus = this.getVectorLong(this.fieldNames[FRM_FIELD_DOCSTATUS]);
        
        if(vectDocStatus==null){
            return result;
        }

        int max = vectDocStatus.size();
		System.out.println("-------------------------vectDocStatus.size() : "+vectDocStatus.size());
        for(int i=0; i<max; i++){
            long docStatusOid = ((Long)vectDocStatus.get(i)).longValue();
            DocTypeStatus dts = new DocTypeStatus();
            dts.setDocStatusOid(docStatusOid);
            dts.setDoctypeType(docType);
            result.add(dts);
        }
        return result;
    }*/

}
