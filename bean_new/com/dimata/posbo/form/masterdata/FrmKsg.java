/* 
 * Form Name  	:  FrmKsg.java 
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
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmKsg extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Ksg ksg;
    public static final String FRM_NAME_KSG = "FRM_NAME_KSG";
    public static final int FRM_FIELD_KSG_ID = 0;
    public static final int FRM_FIELD_NAME = 1;
    public static final int FRM_FIELD_CODE = 2;
    public static final int FRM_FIELD_LOCATION_ID = 3;
    
    public static String[] fieldNames =
            {
        "FRM_FIELD_KSG_ID", "FRM_FIELD_NAME", "FRM_FIELD_CODE", "FRM_FIELD_LOCATION_ID"
    };
    public static int[] fieldTypes =
            {
        TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + ENTRY_REQUIRED, TYPE_LONG
    };

    public FrmKsg() {
    }

    public FrmKsg(Ksg ksg) {
        this.ksg = ksg;
    }

    public FrmKsg(HttpServletRequest request, Ksg ksg) {
        super(new FrmKsg(ksg), request);


        this.ksg = ksg;
    }

    public String getFormName() {
        return FRM_NAME_KSG;
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

    public Ksg getEntityObject() {
        return ksg;
    } 

    public void requestEntityObject(Ksg ksg) {
        try {
            this.requestParam();
            ksg.setName(getString(FRM_FIELD_NAME));
            ksg.setCode(getString(FRM_FIELD_CODE));
            ksg.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
