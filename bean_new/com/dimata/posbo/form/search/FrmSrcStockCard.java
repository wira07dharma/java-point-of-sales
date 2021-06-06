package com.dimata.posbo.form.search;

import javax.servlet.http.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.search.*;

public class FrmSrcStockCard extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcStockCard srcStockCard;

    public static final String FRM_NAME_SRCSTOCKCARD = "FRM_NAME_SRCSTOCKCARD";

    public static final int FRM_FIELD_LOCATION_ID = 0;
    public static final int FRM_FIELD_MATERIAL_ID = 1;
    public static final int FRM_FIELD_START_DATE = 2;
    public static final int FRM_FIELD_END_DATE = 3;
    public static final int FRM_FIELD_DOC_STATUS = 4;
    public static final int FRM_FIELD_STATUSDATE = 5;

    public static String[] fieldNames =
            {
                "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_MATERIAL_ID",
                "FRM_FIELD_START_DATE",
                "FRM_FIELD_END_DATE",
                "FRM_FIELD_DOC_STATUS",
                "FRM_FIELD_STATUSDATE"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG,
                TYPE_LONG,
                TYPE_DATE,
                TYPE_DATE,
                TYPE_INT,
                TYPE_INT
            };

    public FrmSrcStockCard() {
    }

    public FrmSrcStockCard(SrcStockCard srcStockCard) {
        this.srcStockCard = srcStockCard;
    }

    public FrmSrcStockCard(HttpServletRequest request, SrcStockCard srcStockCard) {
        super(new FrmSrcStockCard(srcStockCard), request);
        this.srcStockCard = srcStockCard;
    }

    public String getFormName() {
        return FRM_NAME_SRCSTOCKCARD;
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

    public SrcStockCard getEntityObject() {
        return srcStockCard;
    }

    public void requestEntityObject(SrcStockCard srcStockCard) {
        try {
            this.requestParam();
            srcStockCard.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcStockCard.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            srcStockCard.setStardDate(getDate(FRM_FIELD_START_DATE));
            srcStockCard.setEndDate(getDate(FRM_FIELD_END_DATE));
            srcStockCard.setStatusDate(getInt(FRM_FIELD_STATUSDATE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
