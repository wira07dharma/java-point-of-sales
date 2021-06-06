/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.Position;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class FrmPosition extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Position position;



	public static final String FRM_TB_POSITION		=  "FRM_TB_POSITION" ;

	public static final int FRM_FIELD_POSITION_ID           =  0 ;
        
        public static final int FRM_FIELD_POSITION_NAME         =  1 ;

	public static String[] fieldNames = {

		"FRM_FIELD_POSITION_ID",            
            
                "FRM_FIELD_POSITION_NAME"

	} ;
        
      
	public static int[] fieldTypes = {

                TYPE_LONG,
                
		TYPE_STRING + ENTRY_REQUIRED,
                        
              
	} ;
   
        
        
	public FrmPosition (){

	}

	public FrmPosition(Position position){

		this.position = position;

	}


	public FrmPosition(HttpServletRequest request, Position position){

		super(new FrmPosition(position), request);

		this.position = position;

	}


	public String getFormName() { return FRM_TB_POSITION; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public Position getEntityObject(){ return position; }

	public void requestEntityObject(Position position) {

		try{

			this.requestParam();

			position.setPositionName(getString(FRM_FIELD_POSITION_NAME));
                        
		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

    void requestEntityObject(String position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
