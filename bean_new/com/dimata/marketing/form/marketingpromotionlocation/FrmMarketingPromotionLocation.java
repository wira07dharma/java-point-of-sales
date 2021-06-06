/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotionlocation;

import com.dimata.marketing.entity.marketingpromotionlocation.MarketingPromotionLocation;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionLocation extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionLocation entMarketingPromotionLocation;
    public static final String FRM_NAME_MARKETING_PROMOTION_LOCATION = "FRM_NAME_MARKETING_PROMOTION_LOCATION";
    public static final int FRM_FIELD_MARKETING_PROMOTION_LOCATION_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_ID = 1;
    public static final int FRM_FIELD_LOCATION_ID = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_LOCATION_ID",
        "FRM_FIELD_MARKETING_PROMOTION_ID",
        "FRM_FIELD_LOCATION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public FrmMarketingPromotionLocation() {
    }

    public FrmMarketingPromotionLocation(MarketingPromotionLocation entMarketingPromotionLocation) {
        this.entMarketingPromotionLocation = entMarketingPromotionLocation;
    }

    public FrmMarketingPromotionLocation(HttpServletRequest request, MarketingPromotionLocation entMarketingPromotionLocation) {
        super(new FrmMarketingPromotionLocation(entMarketingPromotionLocation), request);
        this.entMarketingPromotionLocation = entMarketingPromotionLocation;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_LOCATION;
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

    public MarketingPromotionLocation getEntityObject() {
        return entMarketingPromotionLocation;
    }

    public void requestEntityObject(MarketingPromotionLocation entMarketingPromotionLocation) {
        try {
            this.requestParam();
//            entMarketingPromotionLocation.setMarketingPromotionLocationId(getLong(FRM_FIELD_MARKETING_PROMOTION_LOCATION_ID));
            entMarketingPromotionLocation.setMarketingPromotionId(getLong(FRM_FIELD_MARKETING_PROMOTION_ID));
            entMarketingPromotionLocation.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    public void requestEntityObject(MarketingPromotionLocation entMarketingPromotionLocation, long oidBaru) {
        try {
            this.requestParam();
//            entMarketingPromotionLocation.setMarketingPromotionLocationId(getLong(FRM_FIELD_MARKETING_PROMOTION_LOCATION_ID));
            entMarketingPromotionLocation.setMarketingPromotionId(oidBaru);
            entMarketingPromotionLocation.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    public void requestEntityObject(MarketingPromotionLocation entMarketingPromotionLocation, long oidBaru, long oidLong) {
        try {
            this.requestParam();
//            entMarketingPromotionLocation.setMarketingPromotionLocationId(getLong(FRM_FIELD_MARKETING_PROMOTION_LOCATION_ID));
            entMarketingPromotionLocation.setMarketingPromotionId(oidBaru);
            entMarketingPromotionLocation.setLocationId(oidLong);
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
