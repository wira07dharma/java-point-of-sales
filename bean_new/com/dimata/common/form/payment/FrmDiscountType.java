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

package com.dimata.common.form.payment;

import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.common.entity.payment.DiscountType;

public class FrmDiscountType extends FRMHandler implements I_FRMInterface, I_FRMType {
    private DiscountType discountType;

    public static final String FRM_NAME_DISCOUNTTYPE = "FRM_NAME_DISCOUNTTYPE";

    public static final int FRM_FIELD_DISCOUNT_TYPE_ID = 0;
    public static final int FRM_FIELD_CODE = 1;
    public static final int FRM_FIELD_NAME = 2;
    public static final int FRM_FIELD_INDEX = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_DISCOUNT_TYPE_ID", "FRM_FIELD_CODE",
        "FRM_FIELD_NAME","FRM_FIELD_INDEX"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT
    };

    public FrmDiscountType() {
    }

    public FrmDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public FrmDiscountType(HttpServletRequest request, DiscountType discountType) {
        super(new FrmDiscountType(discountType), request);
        this.discountType = discountType;
    }

    public String getFormName() {
        return FRM_NAME_DISCOUNTTYPE;
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

    public DiscountType getEntityObject() {
        return discountType;
    }

    public void requestEntityObject(DiscountType discountType) {
        try {
            this.requestParam();
            discountType.setCode(getString(FRM_FIELD_CODE));
            discountType.setName(getString(FRM_FIELD_NAME));
            discountType.setPindex(getInt(FRM_FIELD_INDEX));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
