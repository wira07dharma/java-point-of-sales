/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.sedana.entity.masterdata.MasterLoket;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMasterLoket extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MasterLoket entMasterLoket;
    public static final String FRM_NAME_MASTERLOKET = "FRM_NAME_MASTERLOKET";
    public static final int FRM_FIELD_MASTER_LOKET_ID = 0;
    public static final int FRM_FIELD_LOKET_NUMBER = 1;
    public static final int FRM_FIELD_LOCATOIN_ID = 2;
    public static final int FRM_FIELD_LOKET_NAME = 3;
    public static final int FRM_FIELD_LOKET_TYPE = 4;

    public static String[] fieldNames = {
        "FRM_FIELD_MASTER_LOKET_ID",
        "FRM_FIELD_LOKET_NUMBER",
        "FRM_FIELD_LOCATOIN_ID",
        "FRM_FIELD_LOKET_NAME",
        "FRM_FIELD_LOKET_TYPE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT
    };

    public FrmMasterLoket() {
    }

    public FrmMasterLoket(MasterLoket entMasterLoket) {
        this.entMasterLoket = entMasterLoket;
    }

    public FrmMasterLoket(HttpServletRequest request, MasterLoket entMasterLoket) {
        super(new FrmMasterLoket(entMasterLoket), request);
        this.entMasterLoket = entMasterLoket;
    }

    public String getFormName() {
        return FRM_NAME_MASTERLOKET;
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

    public MasterLoket getEntityObject() {
        return entMasterLoket;
    }

    public void requestEntityObject(MasterLoket entMasterLoket) {
        try {
            this.requestParam();
            entMasterLoket.setMasterLoketId(getLong(FRM_FIELD_MASTER_LOKET_ID));
            entMasterLoket.setLoketNumber(getInt(FRM_FIELD_LOKET_NUMBER));
            entMasterLoket.setLocationId(getLong(FRM_FIELD_LOCATOIN_ID));
            entMasterLoket.setLoketName(getString(FRM_FIELD_LOKET_NAME));
            entMasterLoket.setLoketType(getInt(FRM_FIELD_LOKET_TYPE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
