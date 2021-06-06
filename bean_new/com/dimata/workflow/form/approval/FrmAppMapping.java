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

public class FrmAppMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private AppMapping appMapping;

	public static final String FRM_NAME_APPMAPPING		=  "FRM_NAME_APPMAPPING" ;

	public static final int FRM_FIELD_APP_MAPPING_OID		=  0 ;
	public static final int FRM_FIELD_APP_TITLE				=  1 ;
	public static final int FRM_FIELD_APPTYPE_OID			=  2 ;
	public static final int FRM_FIELD_DOCTYPE				=  3 ;
	public static final int FRM_FIELD_DEPARTMENT_OID		=  4 ;
	public static final int FRM_FIELD_POSITION_OID			=  5 ;
	public static final int FRM_FIELD_SECTION_ID			=  6 ;
	public static final int FRM_FIELD_APP_INDEX				=  7 ;
	public static final int FRM_FIELD_CONDITION_ID 			=  8 ;
	public static final int FRM_FIELD_DOC_STATUS_ID_BEFORE	=  9 ;
	public static final int FRM_FIELD_DOC_STATUS_ID_AFTER	=  10 ;
	public static final int FRM_FIELD_PROCESS_ASSIGNED		=  11 ;

	public static String[] fieldNames = {
		"FRM_FIELD_APP_MAPPING_OID",
        "FRM_FIELD_APPT_TITLE",
        "FRM_FIELD_APPTYPE_OID",
        "FRM_FIELD_DOCTYPE",
		"FRM_FIELD_DEPARTMENT_OID",
        "FRM_FIELD_POSITION_OID",
		"FRM_FIELD_SECTION_ID",
		"FRM_FIELD_APP_INDEX",
		"FRM_FIELD_CONDITION_ID",
		"FRM_FIELD_DOC_STATUS_ID_BEFORE",
		"FRM_FIELD_DOC_STATUS_ID_AFTER",
        "FRM_FIELD_PROCESS_ASSIGNED"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_INT,
		TYPE_LONG,
        TYPE_LONG,
		TYPE_LONG,
        TYPE_INT,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
        TYPE_COLLECTION
	} ;

	public FrmAppMapping(){
	}

	public FrmAppMapping(AppMapping appMapping){
		this.appMapping = appMapping;
	}

	public FrmAppMapping(HttpServletRequest request, AppMapping appMapping){
		super(new FrmAppMapping(appMapping), request);
		this.appMapping = appMapping;
	}

	public String getFormName() { return FRM_NAME_APPMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; }

	public AppMapping getEntityObject(){ return appMapping; }

	public void requestEntityObject(AppMapping appMapping) {
		try{
			this.requestParam();
			appMapping.setAppTitle(getString(FRM_FIELD_APP_TITLE));
			appMapping.setAppTypeOid(getLong(FRM_FIELD_APPTYPE_OID));
			appMapping.setDocTypeType(getInt(FRM_FIELD_DOCTYPE));
			appMapping.setDepartmentOid(getLong(FRM_FIELD_DEPARTMENT_OID));
			appMapping.setPositionOid(getLong(FRM_FIELD_POSITION_OID));
			appMapping.setSectionId(getLong(FRM_FIELD_SECTION_ID));
			appMapping.setAppIndex(getInt(FRM_FIELD_APP_INDEX));
			appMapping.setAppConditionOid(getLong(FRM_FIELD_CONDITION_ID));
			appMapping.setDocStatusOidBefore(getLong(FRM_FIELD_DOC_STATUS_ID_BEFORE));
			appMapping.setDocStatusOidAfter(getLong(FRM_FIELD_DOC_STATUS_ID_AFTER));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

    /**
     * has to be call after requestEntityObject
     * return Vector of PROCESS STATUS objects
     **/
    public Vector getProcessStatus(){
        Vector result = new Vector(1,1);
        Vector vectProcStatus = this.getVectorLong(this.fieldNames[FRM_FIELD_PROCESS_ASSIGNED]);
        
        if(vectProcStatus==null){
            return result;
        }

        int max = vectProcStatus.size();
		//System.out.println("-------------------------vectProcStatus.size() : "+vectProcStatus.size());
        for(int i=0; i<max; i++){
            long procStatusOid = ((Long)vectProcStatus.get(i)).longValue();
            ChangesStatus chs = new ChangesStatus();
            chs.setDocStatusOid(procStatusOid);
            result.add(chs);
        }
        return result;
    }
}
