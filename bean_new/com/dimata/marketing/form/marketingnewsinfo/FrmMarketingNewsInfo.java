/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingnewsinfo;

import com.dimata.marketing.entity.marketingnewsinfo.MarketingNewsInfo;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

public class FrmMarketingNewsInfo extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingNewsInfo entMarketingNewsInfo;
    public static final String FRM_NAME_MARKETINGNEWSINFO = "FRM_NAME_MARKETINGNEWSINFO";
    public static final int FRM_FIELD_PROMO_ID = 0;
    public static final int FRM_FIELD_TITLE = 1;
    public static final int FRM_FIELD_VALID_START = 2;
    public static final int FRM_FIELD_VALID_END = 3;
    public static final int FRM_FIELD_DESCRIPTION = 4;
    public static final int FRM_FIELD_LOCATION_ID = 5;

    public static String[] fieldNames = {
        "FRM_FIELD_PROMO_ID",
        "FRM_FIELD_TITLE",
        "FRM_FIELD_VALID_START",
        "FRM_FIELD_VALID_END",
        "FRM_FIELD_DESCRIPTION",
        "FRM_FIELD_LOCATION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG
    };

    public FrmMarketingNewsInfo() {
    }

    public FrmMarketingNewsInfo(MarketingNewsInfo entMarketingNewsInfo) {
        this.entMarketingNewsInfo = entMarketingNewsInfo;
    }

    public FrmMarketingNewsInfo(HttpServletRequest request, MarketingNewsInfo entMarketingNewsInfo) {
        super(new FrmMarketingNewsInfo(entMarketingNewsInfo), request);
        this.entMarketingNewsInfo = entMarketingNewsInfo;
    }

    public String getFormName() {
        return FRM_NAME_MARKETINGNEWSINFO;
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

    public MarketingNewsInfo getEntityObject() {
        return entMarketingNewsInfo;
    }

    public void requestEntityObject(MarketingNewsInfo entMarketingNewsInfo) {
        try {
            this.requestParam();
            entMarketingNewsInfo.setOID(getLong(FRM_FIELD_PROMO_ID));
            entMarketingNewsInfo.setTitle(getString(FRM_FIELD_TITLE));
            String tempDateStart = getString(FRM_FIELD_VALID_START);
            String tempDateEnd = getString(FRM_FIELD_VALID_END);
            entMarketingNewsInfo.setValidStart(Formater.formatDate(tempDateStart, "dd/MM/yyyy"));
            entMarketingNewsInfo.setValidEnd(Formater.formatDate(tempDateEnd, "dd/MM/yyyy"));
            entMarketingNewsInfo.setDescription(getString(FRM_FIELD_DESCRIPTION));
            entMarketingNewsInfo.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
