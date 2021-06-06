/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.EmpRelevantDoc;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dedy_blinda
 */
public class FrmEmpRelevantDoc extends FRMHandler implements I_FRMInterface, I_FRMType {
    private EmpRelevantDoc empRelevantDoc;
     
     public static final String FRM_EMP_RELEVANT_DOC		=  "FRM_EMP_RELEVANT_DOC" ;
     
     public static final int FRM_FIELD_DOC_RELEVANT_ID			=  0 ;
     public static final int FRM_FIELD_ANGGOTA_ID			=  1 ;
     public static final int FRM_FIELD_DOC_TITLE			=  2 ;
     public static final int FRM_FIELD_DOC_DESCRIPTION			=  3 ;
     public static final int FRM_FIELD_FILE_NAME			=  4 ;
     public static final int FRM_FIELD_DOC_ATTACH_FILE			=  5 ;
     public static final int FRM_FIELD_RELVT_DOC_GRP_ID                 =  6 ;
     
     
      public static String[] fieldNames = {
		"FRM_FIELD_DOC_RELEVANT_ID",  
                "FRM_FIELD_ANGGOTA_ID",
		"FRM_FIELD_DOC_TITLE",
                "FRM_FIELD_DOC_DESCRIPTION",
                "FILE_NAME",
                "FRM_FIELD_DOC_ATTACH_FILE",
                "FRM_FIELD_RELVT_DOC_GRP_ID"
	} ;
        
      public static int[] fieldTypes = {
                TYPE_LONG+ ENTRY_REQUIRED,
		TYPE_LONG+ ENTRY_REQUIRED,  
		TYPE_STRING+ ENTRY_REQUIRED,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_LONG
    } ;
    
    
    
    
    /** Creates a new instance of FrmEmpRelevantDoc */
    public FrmEmpRelevantDoc() {
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
         return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getFormName() {
        return FRM_EMP_RELEVANT_DOC;
    }
    
    public FrmEmpRelevantDoc(EmpRelevantDoc empRelevantDoc){
		this.empRelevantDoc = empRelevantDoc;
	}
    
    public FrmEmpRelevantDoc(HttpServletRequest request, EmpRelevantDoc empRelevantDoc){
		super(new FrmEmpRelevantDoc(empRelevantDoc), request);
		this.empRelevantDoc = empRelevantDoc;
	}
    
    public EmpRelevantDoc getEntityObject(){ return empRelevantDoc; }
    
    public void requestEntityObject(EmpRelevantDoc empRelevantDoc) {
		try{
			this.requestParam();
			empRelevantDoc.setAnggotaId(getLong(FRM_FIELD_ANGGOTA_ID));
			empRelevantDoc.setDocTitle(getString(FRM_FIELD_DOC_TITLE));
			empRelevantDoc.setDocDescription(getString(FRM_FIELD_DOC_DESCRIPTION));
                        empRelevantDoc.setFileName(getString(FRM_FIELD_FILE_NAME));
                        empRelevantDoc.setDocAttachFile(getString(FRM_FIELD_DOC_ATTACH_FILE));
                        empRelevantDoc.setRelvtDocGrpId(getLong(FRM_FIELD_RELVT_DOC_GRP_ID));
                        
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
