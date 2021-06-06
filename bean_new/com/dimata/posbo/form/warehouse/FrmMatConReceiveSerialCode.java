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
import com.dimata.posbo.entity.warehouse.MatConReceiveSerialCode;

import javax.servlet.http.HttpServletRequest;

public class FrmMatConReceiveSerialCode extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatConReceiveSerialCode receiveStockCode;

    public static final String FRM_NAME_MATERISL_CODE = "FRM_NAME_MATERIAL_RECEIVE_CODE";

    public static final int FRM_FIELD_MATERIAL_RECEIVE_CODE_ID = 0;
    public static final int FRM_FIELD_MATERIAL_RECEIVE_ITEM_ID = 1;
    public static final int FRM_FIELD_STOCK_CODE = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_RECEIVE_CODE_ID", "FRM_FIELD_MATERIAL_RECEIVE_ITEM_ID",
        "FRM_FIELD_STOCK_CODE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED
    };

    public FrmMatConReceiveSerialCode() {
    }

    public FrmMatConReceiveSerialCode(MatConReceiveSerialCode receiveStockCode) {
        this.receiveStockCode = receiveStockCode;
    }

    public FrmMatConReceiveSerialCode(HttpServletRequest request, MatConReceiveSerialCode receiveStockCode) {
        super(new FrmMatConReceiveSerialCode(receiveStockCode), request);
        this.receiveStockCode = receiveStockCode;
    }

    public String getFormName() {
        return FRM_NAME_MATERISL_CODE;
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

    public MatConReceiveSerialCode getEntityObject() {
        return receiveStockCode;
    }

    public void requestEntityObject(MatConReceiveSerialCode receiveStockCode) {
        try {
            this.requestParam();
            receiveStockCode.setReceiveMaterialItemId(getLong(FRM_FIELD_MATERIAL_RECEIVE_ITEM_ID));
            receiveStockCode.setStockCode(getString(FRM_FIELD_STOCK_CODE));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
