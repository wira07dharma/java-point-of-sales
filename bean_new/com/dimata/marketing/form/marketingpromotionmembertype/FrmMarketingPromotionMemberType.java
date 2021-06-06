/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotionmembertype;

import com.dimata.marketing.entity.marketingpromotionmembertype.MarketingPromotionMemberType;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionMemberType extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionMemberType entMarketingPromotionMemberType;
    public static final String FRM_NAME_MARKETING_PROMOTION_MEMBER_TYPE = "FRM_NAME_MARKETING_PROMOTION_MEMBER_TYPE";
    public static final int FRM_FIELD_MARKETING_PROMOTION_MEMBER_TYPE_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_ID = 1;
    public static final int FRM_FIELD_MEMBER_TYPE_ID = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_MEMBER_TYPE_ID",
        "FRM_FIELD_MARKETING_PROMOTION_ID",
        "FRM_FIELD_MEMBER_TYPE_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public FrmMarketingPromotionMemberType() {
    }

    public FrmMarketingPromotionMemberType(MarketingPromotionMemberType entMarketingPromotionMemberType) {
        this.entMarketingPromotionMemberType = entMarketingPromotionMemberType;
    }

    public FrmMarketingPromotionMemberType(HttpServletRequest request, MarketingPromotionMemberType entMarketingPromotionMemberType) {
        super(new FrmMarketingPromotionMemberType(entMarketingPromotionMemberType), request);
        this.entMarketingPromotionMemberType = entMarketingPromotionMemberType;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_MEMBER_TYPE;
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

    public MarketingPromotionMemberType getEntityObject() {
        return entMarketingPromotionMemberType;
    }

    public void requestEntityObject(MarketingPromotionMemberType entMarketingPromotionMemberType) {
        try {
            this.requestParam();
//            entMarketingPromotionMemberType.setMarketingPromotionMemberTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_MEMBER_TYPE_ID));
            entMarketingPromotionMemberType.setMarketingPromotionId(getLong(FRM_FIELD_MARKETING_PROMOTION_ID));
            entMarketingPromotionMemberType.setMemberTypeId(getLong(FRM_FIELD_MEMBER_TYPE_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    public void requestEntityObject(MarketingPromotionMemberType entMarketingPromotionMemberType, long oidBaru) {
        try {
            this.requestParam();
//            entMarketingPromotionMemberType.setMarketingPromotionMemberTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_MEMBER_TYPE_ID));
            entMarketingPromotionMemberType.setMarketingPromotionId(oidBaru);
            entMarketingPromotionMemberType.setMemberTypeId(getLong(FRM_FIELD_MEMBER_TYPE_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    public void requestEntityObject(MarketingPromotionMemberType entMarketingPromotionMemberType, long oidBaru, long oidMulti) {
        try {
            this.requestParam();
//            entMarketingPromotionMemberType.setMarketingPromotionMemberTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_MEMBER_TYPE_ID));
            entMarketingPromotionMemberType.setMarketingPromotionId(oidBaru);
            entMarketingPromotionMemberType.setMemberTypeId(oidMulti);
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
