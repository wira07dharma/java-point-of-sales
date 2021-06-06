/*
 * Form Name  	:  FrmDiscountType.java
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
import com.dimata.posbo.entity.warehouse.SourceStockCode;

import javax.servlet.http.HttpServletRequest;

public class FrmSourceStockCode extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SourceStockCode sourceStockCode;

    public static final String FRM_NAME_SOURCE_STOCK_CODE = "FRM_NAME_SOURCE_STOCK_CODE";

    public static final int FRM_FIELD_SOURCE_STOCK_CODE_ID = 0;
    public static final int FRM_FIELD_SOURCE_ID = 1;
    public static final int FRM_FIELD_STOCK_CODE = 2;
    public static final int FRM_FIELD_STOCK_VALUE = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_SOURCE_STOCK_CODE_ID", "FRM_FIELD_SOURCE_ID",
        "FRM_FIELD_STOCK_CODE","FRM_FIELD_STOCK_VALUE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,TYPE_FLOAT
    };

    public FrmSourceStockCode() {
    }

    public FrmSourceStockCode(SourceStockCode sourceStockCode) {
        this.sourceStockCode = sourceStockCode;
    }

    public FrmSourceStockCode(HttpServletRequest request, SourceStockCode sourceStockCode) {
        super(new FrmSourceStockCode(sourceStockCode), request);
        this.sourceStockCode = sourceStockCode;
    }

    public String getFormName() {
        return FRM_NAME_SOURCE_STOCK_CODE;
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

    public SourceStockCode getEntityObject() {
        return sourceStockCode;
    }

    public void requestEntityObject(SourceStockCode sourceStockCode) {
        try {
            this.requestParam();
            sourceStockCode.setSourceId(getLong(FRM_FIELD_SOURCE_ID));
            sourceStockCode.setStockCode(getString(FRM_FIELD_STOCK_CODE));
            sourceStockCode.setStockValue(getDouble(FRM_FIELD_STOCK_VALUE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
