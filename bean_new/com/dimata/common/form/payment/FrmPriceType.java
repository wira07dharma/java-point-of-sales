/*
 * Form Name  	:  FrmPriceType.java
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
import com.dimata.common.entity.payment.PriceType;

public class FrmPriceType extends FRMHandler implements I_FRMInterface, I_FRMType {
    private PriceType priceType;

    public static final String FRM_NAME_PRICETYPE = "FRM_NAME_PRICETYPE";

    public static final int FRM_FIELD_PRICE_TYPE_ID = 0;
    public static final int FRM_FIELD_CODE = 1;
    public static final int FRM_FIELD_NAME = 2;
    public static final int FRM_FIELD_INDEX = 3;
    public static final int FRM_FIELD_PRICE = 4;

    public static String[] fieldNames = {
        "FRM_FIELD_PRICE_TYPE_ID", "FRM_FIELD_CODE",
        "FRM_FIELD_NAME","FRM_FIELD_INDEX", "FRM_FIELD_PRICE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,TYPE_INT,TYPE_INT
    };

    public FrmPriceType() {
    }

    public FrmPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public FrmPriceType(HttpServletRequest request, PriceType priceType) {
        super(new FrmPriceType(priceType), request);
        this.priceType = priceType;
    }

    public String getFormName() {
        return FRM_NAME_PRICETYPE;
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

    public PriceType getEntityObject() {
        return priceType;
    }

    public void requestEntityObject(PriceType priceType) {
        try {
            this.requestParam();
            priceType.setCode(getString(FRM_FIELD_CODE));
            priceType.setName(getString(FRM_FIELD_NAME));
            priceType.setIndex(getInt(FRM_FIELD_INDEX));
            priceType.setPriceSystem(getInt(FRM_FIELD_PRICE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
