/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form;
/* java package */

import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.harisma.entity.masterdata.*;

/**
 *
 * @author Dimata 007
 */

public class FrmPosition extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Position position;
    public static final String FRM_NAME_POSITION = "FRM_NAME_POSITION";
    public static final int FRM_FIELD_POSITION_ID = 0;
    public static final int FRM_FIELD_POSITION = 1;
    public static final int FRM_FIELD_DESCRIPTION = 2;
    public static final int FRM_FIELD_PERSENTASE_INSENTIF = 3;
    public static final int FRM_FIELD_POSITION_LEVEL = 4;

    public static String[] fieldNames = {
        "FRM_FIELD_POSITION_ID",
        "FRM_FIELD_POSITION",
        "FRM_FIELD_DESCRIPTION",
        "FRM_FIELD_PERSENTASE_INSENTIF",
        "FRM_FIELD_POSITION_LEVEL"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT
    };

    public FrmPosition() {
    }

    public FrmPosition(Position position) {
        this.position = position;
    }

    public FrmPosition(HttpServletRequest request, Position position) {
        super(new FrmPosition(position), request);
        this.position = position;
    }

    public String getFormName() {
        return FRM_NAME_POSITION;
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

    public Position getEntityObject() {
        return position;
    }

    public void requestEntityObject(Position position) {
        try {
            this.requestParam();
            position.setPosition(getString(FRM_FIELD_POSITION));
            position.setDescription(getString(FRM_FIELD_DESCRIPTION));
            position.setPersentaseInsentif(getDouble(FRM_FIELD_PERSENTASE_INSENTIF));
            position.setPositionLevel(getInt(FRM_FIELD_POSITION_LEVEL));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
