/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmKitchenProduksiMapping extends FRMHandler implements I_FRMInterface, I_FRMType {

    private KitchenProduksiMapping entKitchenProduksiMapping;
    public static final String FRM_NAME_KITCHENPRODUKSIMAPPING = "FRM_NAME_KITCHENPRODUKSIMAPPING";
    public static final int FRM_FIELD_POSMAPPINGPRODUKSIID = 0;
    public static final int FRM_FIELD_SUBCATEGORYID = 1;
    public static final int FRM_FIELD_LOCATIONORDERID = 2;
    public static final int FRM_FIELD_LOCATIONPRODUKSIID = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_POSMAPPINGPRODUKSIID",
        "FRM_FIELD_SUBCATEGORYID",
        "FRM_FIELD_LOCATIONORDERID",
        "FRM_FIELD_LOCATIONPRODUKSIID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public FrmKitchenProduksiMapping() {
    }

    public FrmKitchenProduksiMapping(KitchenProduksiMapping entKitchenProduksiMapping) {
        this.entKitchenProduksiMapping = entKitchenProduksiMapping;
    }

    public FrmKitchenProduksiMapping(HttpServletRequest request, KitchenProduksiMapping entKitchenProduksiMapping) {
        super(new FrmKitchenProduksiMapping(entKitchenProduksiMapping), request);
        this.entKitchenProduksiMapping = entKitchenProduksiMapping;
    }

    public String getFormName() {
        return FRM_NAME_KITCHENPRODUKSIMAPPING;
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

    public KitchenProduksiMapping getEntityObject() {
        return entKitchenProduksiMapping;
    }

    public void requestEntityObject(KitchenProduksiMapping entKitchenProduksiMapping) {
        try {
            this.requestParam();
            entKitchenProduksiMapping.setSubCategoryId(getLong(FRM_FIELD_SUBCATEGORYID));
            entKitchenProduksiMapping.setLocationOrderId(getLong(FRM_FIELD_LOCATIONORDERID));
            entKitchenProduksiMapping.setLocationProduksiId(getLong(FRM_FIELD_LOCATIONPRODUKSIID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
