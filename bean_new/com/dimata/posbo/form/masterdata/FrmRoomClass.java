/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.RoomClass;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmRoomClass extends FRMHandler implements I_FRMInterface, I_FRMType {

    private RoomClass entRoomClass;
    public static final String FRM_NAME_ROOM_CLASS = "FRM_NAME_ROOM_CLASS";
    public static final int FRM_FIELD_POS_ROOM_CLASS_ID = 0;
    public static final int FRM_FIELD_CLASS_NAME = 1;
    public static final int FRM_FIELD_CLASS_DESC = 2;
    public static final int FRM_FIELD_SHOW_INDEX = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_POS_ROOM_CLASS_ID",
        "FRM_FIELD_CLASS_NAME",
        "FRM_FIELD_CLASS_DESC",
        "FRM_FIELD_SHOW_INDEX"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT
    };

    public FrmRoomClass() {
    }

    public FrmRoomClass(RoomClass entRoomClass) {
        this.entRoomClass = entRoomClass;
    }

    public FrmRoomClass(HttpServletRequest request, RoomClass entRoomClass) {
        super(new FrmRoomClass(entRoomClass), request);
        this.entRoomClass = entRoomClass;
    }

    public String getFormName() {
        return FRM_NAME_ROOM_CLASS;
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

    public RoomClass getEntityObject() {
        return entRoomClass;
    }

    public void requestEntityObject(RoomClass entRoomClass) {
        try {
            this.requestParam();
//            entRoomClass.setPosRoomClassId(getLong(FRM_FIELD_POS_ROOM_CLASS_ID));
            entRoomClass.setClassName(getString(FRM_FIELD_CLASS_NAME));
            entRoomClass.setClassDesc(getString(FRM_FIELD_CLASS_DESC));
            entRoomClass.setShowIndex(getInt(FRM_FIELD_SHOW_INDEX));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
