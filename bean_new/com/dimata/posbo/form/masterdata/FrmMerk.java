/* 
 * Form Name  	:  FrmMerk.java 
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
package com.dimata.posbo.form.masterdata;

/* java package */
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmMerk extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Merk merk;
    public static final String FRM_NAME_MERK = "FRM_NAME_MERK";
    public static final int FRM_FIELD_MERK_ID = 0;
    public static final int FRM_FIELD_NAME = 1;
    public static final int FRM_FIELD_CODE = 2;
    //adding status ditampilkan atau tidak by mirahu 20120511
    public static final int FRM_FIELD_STATUS = 3;
    public static String[] fieldNames =
            {
        "FRM_FIELD_MERK_ID", "FRM_FIELD_NAME", "FRM_FIELD_CODE", "FRM_FIELD_STATUS"
    };
    public static int[] fieldTypes =
            {
        TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + ENTRY_REQUIRED, TYPE_INT
    };

    public FrmMerk() {
    }

    public FrmMerk(Merk merk) {
        this.merk = merk;
    }

    public FrmMerk(HttpServletRequest request, Merk merk) {
        super(new FrmMerk(merk), request);


        this.merk = merk;
    }

    public String getFormName() {
        return FRM_NAME_MERK;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public Merk getEntityObject() {
        return merk;
    }

    public void requestEntityObject(Merk merk) {
        try {
            this.requestParam();
            merk.setName(getString(FRM_FIELD_NAME));
            merk.setCode(getString(FRM_FIELD_CODE));
            //adding status ditampilkan atau tidak by mirahu 20120511
            merk.setStatus(getInt(FRM_FIELD_STATUS));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
