/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetailassign;

/**
 *
 * @author Dewa
 */
import com.dimata.marketing.entity.marketingpromotiondetailassign.MarketingPromotionDetailAssign;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMarketingPromotionDetailAssign extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionDetailAssign entMarketingPromotionDetailAssign;
    public static final String FRM_NAME_MARKETING_PROMOTION_DETAIL_ASSIGN = "FRM_NAME_MARKETING_PROMOTION_DETAIL_ASSIGN";
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID = 1;
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_OBJECT_ID = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID",
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID",
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_OBJECT_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public FrmMarketingPromotionDetailAssign() {
    }

    public FrmMarketingPromotionDetailAssign(MarketingPromotionDetailAssign entMarketingPromotionDetailAssign) {
        this.entMarketingPromotionDetailAssign = entMarketingPromotionDetailAssign;
    }

    public FrmMarketingPromotionDetailAssign(HttpServletRequest request, MarketingPromotionDetailAssign entMarketingPromotionDetailAssign) {
        super(new FrmMarketingPromotionDetailAssign(entMarketingPromotionDetailAssign), request);
        this.entMarketingPromotionDetailAssign = entMarketingPromotionDetailAssign;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_DETAIL_ASSIGN;
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

    public MarketingPromotionDetailAssign getEntityObject() {
        return entMarketingPromotionDetailAssign;
    }

    public void requestEntityObject(MarketingPromotionDetailAssign entMarketingPromotionDetailAssign) {
        try {
            this.requestParam();
//            entMarketingPromotionDetailAssign.setMarketingPromotionDetailAssignId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID));
            entMarketingPromotionDetailAssign.setMarketingPromotionDetailSubjectId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID));
            entMarketingPromotionDetailAssign.setMarketingPromotionDetailObjectId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_OBJECT_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
