/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotionpricetype;

import com.dimata.marketing.entity.marketingpromotionpricetype.MarketingPromotionPriceType;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionPriceType extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionPriceType entMarketingPromotionPriceType;
    public static final String FRM_NAME_MARKETING_PROMOTION_PRICE_TYPE = "FRM_NAME_MARKETING_PROMOTION_PRICE_TYPE";
    public static final int FRM_FIELD_MARKETING_PROMOTION_PRICE_TYPE_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_ID = 1;
    public static final int FRM_FIELD_PRICE_TYPE_ID = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_PRICE_TYPE_ID",
        "FRM_FIELD_MARKETING_PROMOTION_ID",
        "FRM_FIELD_PRICE_TYPE_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public FrmMarketingPromotionPriceType() {
    }

    public FrmMarketingPromotionPriceType(MarketingPromotionPriceType entMarketingPromotionPriceType) {
        this.entMarketingPromotionPriceType = entMarketingPromotionPriceType;
    }

    public FrmMarketingPromotionPriceType(HttpServletRequest request, MarketingPromotionPriceType entMarketingPromotionPriceType) {
        super(new FrmMarketingPromotionPriceType(entMarketingPromotionPriceType), request);
        this.entMarketingPromotionPriceType = entMarketingPromotionPriceType;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_PRICE_TYPE;
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

    public MarketingPromotionPriceType getEntityObject() {
        return entMarketingPromotionPriceType;
    }
    
    public void requestEntityObject(MarketingPromotionPriceType entMarketingPromotionPriceType) {
        try {
            this.requestParam();
//            entMarketingPromotionPriceType.setMarketingPromotionPriceTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_PRICE_TYPE_ID));
            entMarketingPromotionPriceType.setMarketingPromotionId(getLong(FRM_FIELD_MARKETING_PROMOTION_ID));
            entMarketingPromotionPriceType.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

    public void requestEntityObject(MarketingPromotionPriceType entMarketingPromotionPriceType, long oidBaru) {
        try {
            this.requestParam();
//            entMarketingPromotionPriceType.setMarketingPromotionPriceTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_PRICE_TYPE_ID));
            entMarketingPromotionPriceType.setMarketingPromotionId(oidBaru);
            entMarketingPromotionPriceType.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    public void requestEntityObject(MarketingPromotionPriceType entMarketingPromotionPriceType, long oidBaru, long oidMulti) {
        try {
            this.requestParam();
//            entMarketingPromotionPriceType.setMarketingPromotionPriceTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_PRICE_TYPE_ID));
            entMarketingPromotionPriceType.setMarketingPromotionId(oidBaru);
            entMarketingPromotionPriceType.setPriceTypeId(oidMulti);
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
