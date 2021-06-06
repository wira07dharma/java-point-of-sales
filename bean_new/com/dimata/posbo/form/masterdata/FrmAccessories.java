/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.Accessories;
import com.dimata.posbo.entity.masterdata.Accessories;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import static com.dimata.qdep.form.I_FRMType.TYPE_FLOAT;
import static com.dimata.qdep.form.I_FRMType.TYPE_LONG;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class FrmAccessories extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Accessories entAccessories;
    public static final String FRM_NAME_ACCESSORIES = "FRM_NAME_ACCESSORIES";
    public static final int FRM_FIELD_ACCESSORIES_ID = 0;
    public static final int FRM_FIELD_ACCESSORIES_NAME = 1;
    public static final int FRM_FIELD_ACCESSORIES_CODE = 2;
    public static final int FRM_FIELD_ACCESSORIES_STATUS = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_ACCESSORIES_ID",
        "FRM_FIELD_ACCESSORIES_NAME",
        "FRM_FIELD_ACCESSORIES_CODE",
        "FRM_FIELD_ACCESSORIES_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,        
        TYPE_STRING,
        TYPE_INT
    };

    public FrmAccessories() {
    }

    public FrmAccessories(Accessories entAccessories) {
        this.entAccessories = entAccessories;
    }

    public FrmAccessories(HttpServletRequest request, Accessories entAccessories) {
        super(new FrmAccessories(entAccessories), request);
        this.entAccessories = entAccessories;
    }

    public String getFormName() {
        return FRM_NAME_ACCESSORIES;
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

    public Accessories getEntityObject() {
        return entAccessories;
    }

    public void requestEntityObject(Accessories entAccessories) {
        try {
            this.requestParam();
//            entAccessories.setAccessoriesId(getLong(FRM_FIELD_ASSIGN_DISC_ID));
            entAccessories.setAccessories_code(getString(FRM_FIELD_ACCESSORIES_CODE));
            entAccessories.setAccessories_name(getString(FRM_FIELD_ACCESSORIES_NAME));
            entAccessories.setAccessories_status(getInt(FRM_FIELD_ACCESSORIES_STATUS));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}