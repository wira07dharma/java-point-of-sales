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
import com.dimata.posbo.entity.warehouse.CostingStockCode;

import javax.servlet.http.HttpServletRequest;

public class FrmCostingStockCode extends FRMHandler implements I_FRMInterface, I_FRMType {
    private CostingStockCode costingStockCode;

    public static final String FRM_NAME_MATERIAL_COSTING_CODE = "FRM_NAME_MATERIAL_COSTING_CODE";

    public static final int FRM_FIELD_MATERIAL_COSTING_CODE_ID = 0;
    public static final int FRM_FIELD_MATERIAL_COSTING_ITEM_ID = 1;
    public static final int FRM_FIELD_STOCK_CODE = 2;
    public static final int FRM_FIELD_STOCK_VALUE = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_COSTING_CODE_ID", "FRM_FIELD_MATERIAL_COSTING_ITEM_ID",
        "FRM_FIELD_STOCK_CODE","FRM_FIELD_STOCK_VALUE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_FLOAT
    };

    public FrmCostingStockCode() {
    }

    public FrmCostingStockCode(CostingStockCode costingStockCode) {
        this.costingStockCode = costingStockCode;
    }

    public FrmCostingStockCode(HttpServletRequest request, CostingStockCode costingStockCode) {
        super(new FrmCostingStockCode(costingStockCode), request);
        this.costingStockCode = costingStockCode;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_COSTING_CODE;
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

    public CostingStockCode getEntityObject() {
        return costingStockCode;
    }

    public void requestEntityObject(CostingStockCode costingStockCode) {
        try {
            this.requestParam();
            costingStockCode.setCostingMaterialItemId(getLong(FRM_FIELD_MATERIAL_COSTING_ITEM_ID));
            costingStockCode.setStockCode(getString(FRM_FIELD_STOCK_CODE));
            costingStockCode.setStockValue(getDouble(FRM_FIELD_STOCK_VALUE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
