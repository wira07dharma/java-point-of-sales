/* 
 * Form Name  	:  FrmNegara.java
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

package com.dimata.common.form.location;

/* java package */ 
import com.dimata.common.entity.location.Negara;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;


import javax.servlet.http.HttpServletRequest;

public class FrmNegara extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private Negara negara;

	public static final String FRM_NAME_NEGARA		=  "FRM_NAME_NEGARA" ;

	public static final int FRM_FIELD_ID_NEGARA		=  0 ;
	public static final int FRM_FIELD_BENUA			=  1 ;
	public static final int FRM_FIELD_NM_NEGARA		=  2 ;

	public static String[] fieldNames = {
		"FRM_FIELD_ID_NEGARA",  "FRM_FIELD_BENUA",
		"FRM_FIELD_NM_NEGARA"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_STRING,
		TYPE_STRING + ENTRY_REQUIRED
	} ;

	public FrmNegara(){
	}
	public FrmNegara(Negara negara){
		this.negara = negara;
	}

	public FrmNegara(HttpServletRequest request, Negara negara){
		super(new FrmNegara(negara), request);
		this.negara = negara;
	}

	public String getFormName() { return FRM_NAME_NEGARA; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public Negara getEntityObject(){ return negara; }

	public void requestEntityObject(Negara negara) {
		try{
			this.requestParam();
			negara.setBenua(getString(FRM_FIELD_BENUA));
			negara.setNmNegara(getString(FRM_FIELD_NM_NEGARA));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
