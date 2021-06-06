/*
 * Form Name  	:  FrmReceiveMaterialCode.java
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

package com.dimata.posbo.form.warehouse;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.warehouse.ReturnStockCode;

import javax.servlet.http.HttpServletRequest;

public class FrmReturnStockCode extends FRMHandler implements I_FRMInterface, I_FRMType {
    private ReturnStockCode returnStockCode;

    public static final String FRM_NAME_MATERIAL_RETURN_CODE = "FRM_NAME_MATERIAL_RETURN_CODE";

    public static final int FRM_FIELD_MATERIAL_RETURN_CODE_ID = 0;
    public static final int FRM_FIELD_MATERIAL_RETURN_ITEM_ID = 1;
    public static final int FRM_FIELD_STOCK_CODE = 2;
     public static final int FRM_FIELD_STOCK_VALUE = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_RETURN_CODE_ID", "FRM_FIELD_MATERIAL_RETURN_ITEM_ID",
        "FRM_FIELD_STOCK_CODE","FRM_FIELD_STOCK_VALUE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_FLOAT + ENTRY_REQUIRED
    };

    public FrmReturnStockCode() {
    }

    public FrmReturnStockCode(ReturnStockCode returnStockCode) {
        this.returnStockCode = returnStockCode;
    }

    public FrmReturnStockCode(HttpServletRequest request, ReturnStockCode returnStockCode) {
        super(new FrmReturnStockCode(returnStockCode), request);
        this.returnStockCode = returnStockCode;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_RETURN_CODE;
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

    public ReturnStockCode getEntityObject() {
        return returnStockCode;
    }

    public void requestEntityObject(ReturnStockCode returnStockCode) {
        try {
            this.requestParam();
            returnStockCode.setReturnMaterialItemId(getLong(FRM_FIELD_MATERIAL_RETURN_ITEM_ID));
            returnStockCode.setStockCode(getString(FRM_FIELD_STOCK_CODE));
            returnStockCode.setStockValue(getDouble(FRM_FIELD_STOCK_VALUE));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
