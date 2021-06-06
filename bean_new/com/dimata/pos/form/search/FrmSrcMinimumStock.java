/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Mar 2, 2006
 * Time: 10:34:49 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.pos.form.search;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.search.SrcMinimumStock;

import javax.servlet.http.HttpServletRequest;

public class FrmSrcMinimumStock extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SrcMinimumStock srcMinimumStock;

    public static final String FRM_NAME_MINIMUM_STOCK = "FRM_NAME_MINIMUM_STOCK";

    public static final int FRM_FIELD_LOCATION_ID = 0;
    public static final int FRM_FIELD_CATEGORY_ID = 1;
    public static final int FRM_FIELD_PERIOD_ID = 2;
    public static final int FRM_FIELD_TEXT_OTHER = 3;
    public static final int FRM_FIELD_MATERIAL_ID = 4;
    public static final int FRM_FIELD_MERK_ID = 5;
    public static final int FRM_FIELD_SUPPLIER_ID = 6;

    public static String[] fieldNames = {
        "FRM_FIELD_LOCATION_ID", "FRM_FIELD_CATEGORY_ID",
        "FRM_FIELD_PERIOD_ID", "FRM_FIELD_TEXT_OTHER",
        "FRM_FIELD_MATERIAL_ID","FRM_FIELD_MERK_ID",
        "FRM_FIELD_SUPPLIER_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_STRING,
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG
    };

    public FrmSrcMinimumStock() {
    }

    public FrmSrcMinimumStock(SrcMinimumStock srcMinimumStock) {
        this.srcMinimumStock = srcMinimumStock;
    }

    public FrmSrcMinimumStock(HttpServletRequest request, SrcMinimumStock srcMinimumStock) {
        super(new FrmSrcMinimumStock(srcMinimumStock), request);
        this.srcMinimumStock = srcMinimumStock;
    }

    public String getFormName() {
        return FRM_NAME_MINIMUM_STOCK;
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

    public SrcMinimumStock getEntityObject() {
        return srcMinimumStock;
    }

    public void requestEntityObject(SrcMinimumStock srcMinimumStock) {
        try {
            this.requestParam();
            srcMinimumStock.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcMinimumStock.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            srcMinimumStock.setPeriodId(getLong(FRM_FIELD_PERIOD_ID));
            srcMinimumStock.setTextOther(getString(FRM_FIELD_TEXT_OTHER));
            srcMinimumStock.setOidMaterial(getLong(FRM_FIELD_MATERIAL_ID));
            srcMinimumStock.setOidMerk(getLong(FRM_FIELD_MERK_ID));
            srcMinimumStock.setOidSupplier(getLong(FRM_FIELD_SUPPLIER_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
