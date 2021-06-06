/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiontype;

import com.dimata.marketing.entity.marketingpromotiontype.MarketingPromotionType;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionType extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionType entMarketingPromotionType;
    public static final String FRM_NAME_MARKETING_PROMOTION_TYPE = "FRM_NAME_MARKETING_PROMOTION_TYPE";
    public static final int FRM_FIELD_MARKETING_PROMOTION_TYPE_ID = 0;
    public static final int FRM_FIELD_PROMOTION_TYPE = 1;
    public static final int FRM_FIELD_PROMOTION_TYPE_NAME = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_TYPE_ID",
        "FRM_FIELD_PROMOTION_TYPE",
        "FRM_FIELD_PROMOTION_TYPE_NAME"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING
    };

    public FrmMarketingPromotionType() {
    }

    public FrmMarketingPromotionType(MarketingPromotionType entMarketingPromotionType) {
        this.entMarketingPromotionType = entMarketingPromotionType;
    }

    public FrmMarketingPromotionType(HttpServletRequest request, MarketingPromotionType entMarketingPromotionType) {
        super(new FrmMarketingPromotionType(entMarketingPromotionType), request);
        this.entMarketingPromotionType = entMarketingPromotionType;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_TYPE;
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

    public MarketingPromotionType getEntityObject() {
        return entMarketingPromotionType;
    }

    public void requestEntityObject(MarketingPromotionType entMarketingPromotionType) {
        try {
            this.requestParam();
//            entMarketingPromotionType.setMarketingPromotionTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_TYPE_ID));
            entMarketingPromotionType.setPromotionType(getInt(FRM_FIELD_PROMOTION_TYPE));
            entMarketingPromotionType.setPromotionTypeName(getString(FRM_FIELD_PROMOTION_TYPE_NAME));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
