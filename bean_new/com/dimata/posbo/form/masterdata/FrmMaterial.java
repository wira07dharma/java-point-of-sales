package com.dimata.posbo.form.masterdata;

/* java package */

import com.dimata.common.entity.system.PstSystemProperty;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmMaterial extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Material material;

    public static final String FRM_NAME_MATERIAL = "FRM_NAME_MATERIAL";

    public static final int FRM_FIELD_MATERIAL_ID = 0;
    public static final int FRM_FIELD_SKU = 1;
    public static final int FRM_FIELD_BARCODE = 2;
    public static final int FRM_FIELD_NAME = 3;
    public static final int FRM_FIELD_MERK_ID = 4;
    public static final int FRM_FIELD_CATEGORY_ID = 5;
    public static final int FRM_FIELD_SUB_CATEGORY_ID = 6;
    public static final int FRM_FIELD_DEFAULT_STOCK_UNIT_ID = 7;
    public static final int FRM_FIELD_DEFAULT_PRICE = 8;
    public static final int FRM_FIELD_DEFAULT_PRICE_CURRENCY_ID = 9;
    public static final int FRM_FIELD_DEFAULT_COST = 10;
    public static final int FRM_FIELD_DEFAULT_COST_CURRENCY_ID = 11;
    public static final int FRM_FIELD_DEFAULT_SUPPLIER_TYPE = 12;
    public static final int FRM_FIELD_SUPPLIER_ID = 13;
    public static final int FRM_FIELD_PRICE_TYPE_01 = 14;
    public static final int FRM_FIELD_PRICE_TYPE_02 = 15;
    public static final int FRM_FIELD_PRICE_TYPE_03 = 16;
    public static final int FRM_FIELD_MATERIAL_TYPE = 17;

    public static final int FRM_FIELD_LAST_DISCOUNT = 18;
    public static final int FRM_FIELD_LAST_VAT = 19;
    public static final int FRM_FIELD_CURR_BUY_PRICE = 20;
    public static final int FRM_FIELD_BUY_UNIT_ID = 21;
    public static final int FRM_FIELD_EXPIRED_DATE = 22;
    public static final int FRM_FIELD_PROFIT = 23;
    public static final int FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION = 24;

    public static final int FRM_FIELD_AVERAGE_PRICE = 25;
    public static final int FRM_FIELD_MINIMUM_POINT = 26;
    public static final int FRM_FIELD_REQUIRED_SERIAL_NUMBER = 27;
    public static final int FRM_FIELD_LAST_UPDATE = 28;
    public static final int FRM_FIELD_PROCESS_STATUS = 29;
    public static final int FRM_FIELD_CONSIGMENT_TYPE = 30;
    public static final int FRM_FIELD_GONDOLA_CODE = 31;

    //for last cost cargo
    public static final int FRM_FIELD_LAST_COST_CARGO = 32;
    
    //for edit material -opie 12-06-2012
    public static final int FRM_FIELD_EDIT_MATERIAL =33;

    //for point sales 20130903
    public static final int FRM_FIELD_POINT_SALES =34;
    
    public static final int FRM_FIELD_NAME_1 = 35;
    public static final int FRM_FIELD_NAME_2 = 36;
    public static final int FRM_FIELD_DESCRIPTION = 37;
    
    //addd untuk litama jewelery
    //oleh opie-eyek 20171009
    public static final int FRM_FIELD_KEPEMILIKAN = 38;
    public static final int FRM_FIELD_KADAR = 39;
    public static final int FRM_FIELD_WARNA = 40;
    public static final int FRM_FIELD_MATERIAL_JENIS_TYPE = 41;
	public static final int FRM_FIELD_SALES_RULE = 42;
	public static final int FRM_FIELD_RETURN_RULE = 43;
        public static final int FRM_FIELD_VIEW_IN_SHOPPING_CHART = 44;
    
    public static String[] fieldNames =
            {
                "FRM_FIELD_MATERIAL_ID",
                "FRM_FIELD_SKU",
                "FRM_FIELD_BARCODE",
                "FRM_FIELD_NAME",
                "FRM_FIELD_MERK_ID",
                "FRM_FIELD_CATEGORY_ID",
                "FRM_FIELD_SUB_CATEGORY_ID",
                "FRM_FIELD_DEFAULT_STOCK_UNIT_ID",
                "FRM_FIELD_DEFAULT_PRICE",
                "FRM_FIELD_DEFAULT_PRICE_CURRENCY_ID",
                "FRM_FIELD_DEFAULT_COST",
                "FRM_FIELD_DEFAULT_COST_CURRENCY_ID",
                "FRM_FIELD_DEFAULT_SUPPLIER_TYPE",
                "FRM_FIELD_SUPPLIER_ID",
                "FRM_FIELD_PRICE_TYPE_01",
                "FRM_FIELD_PRICE_TYPE_02",
                "FRM_FIELD_PRICE_TYPE_03",
                "FRM_FIELD_MATERIAL_TYPE",
                "FRM_FIELD_LAST_DISCOUNT",
                "FRM_FIELD_LAST_VAT",
                "FRM_FIELD_CURR_BUY_PRICE",
                "FRM_FIELD_BUY_UNIT_ID",
                "FRM_FIELD_EXPIR ED_DATE",
                "FRM_FIELD_PROFIT",
                "FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION",
                "FRM_FIELD_AVERAGE_PRICE",
                "FRM_FIELD_MINIMUM_POINT",
                "FRM_FIELD_REQUIRED_SERIAL_NUMBER",
                "FRM_FIELD_LAST_UPDATE",
                "FRM_FIELD_PROCESS_STATUS",
                "FRM_FIELD_CONSIGMENT_TYPE",
                "FRM_FIELD_GONDOLA_CODE",
                //last cost cargo
                "FRM_FIELD_LAST_COST_CARGO",

               //for edit material -opie 12-06-2012
                "FRM_EDIT_MATERIAL",
                
                "FRM_FIELD_POINT_SALES",
                "FRM_FIELD_NAME_1",
                "FRM_FIELD_NAME_2",
                "FRM_FIELD_DESCRIPTION",
                
                "FRM_FIELD_KEPEMILIKAN",
                "FRM_FIELD_KADAR",
                "FRM_FIELD_WARNA",
                "FRM_FIELD_MATERIAL_JENIS_TYPE",
                "FRM_FIELD_SALES_RULE",
                "FRM_FIELD_RETURN_RULE",
                "FRM_FIELD_VIEW_IN_SHOPPING_CHART"
                                

            };

    public static int[] fieldTypes =
            {
                TYPE_LONG,
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_DATE,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,//*
                TYPE_INT,
                TYPE_INT,
                TYPE_DATE,
                TYPE_INT,
                TYPE_INT,
                TYPE_LONG,//gondola
                //LAST COST CARGO
                TYPE_FLOAT,
                //for edit material -opie 12-06-2012
                TYPE_INT,
                TYPE_INT,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT
            };

    public FrmMaterial() {
    }

    public FrmMaterial(Material material) {
        this.material = material;
    }

    public FrmMaterial(HttpServletRequest request, Material material) {
        super(new FrmMaterial(material), request);
        this.material = material;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL;
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

    public Material getEntityObject() {
        return material;
    }

    public void requestEntityObject(Material material) {
        try {
            this.requestParam();
            //disni pengecekan apakah penomberan sku otomatis atau
            material.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
            
//            int otomaticSku =Integer.parseInt(PstSystemProperty.getValueByName("SKU_GENERATE_OTOMATIC"));
//            if(otomaticSku==1){
//                String kodeSub="";
//                try {
//                        SubCategory subCategory = PstSubCategory.fetchExc(material.getSubCategoryId());
//                        kodeSub = subCategory.getCode();
//                    } catch (Exception e) {}
//                material.setSku(PstMaterial.getCodeOtomaticGenerateSku(kodeSub));
//                //material.setSku(getString(FRM_FIELD_SKU));
//            }else{
//                material.setSku(getString(FRM_FIELD_SKU));
//            }
            material.setSku(getString(FRM_FIELD_SKU));
            
            material.setBarCode(getString(FRM_FIELD_BARCODE));
            
            int multiLanguageName=0;
            try {
                multiLanguageName = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueByName("NAME_MATERIAL_MULTI_LANGUAGE"));
            } catch (Exception e) {
                multiLanguageName = 0;
            }
            
            if(multiLanguageName==1){
                String name1 = getString(FRM_FIELD_NAME);
                String name2 = getString(FRM_FIELD_NAME_1);
                String name3= getString(FRM_FIELD_NAME_2);
                
                String nameAll = name1+Material.getSeparateLanguage()+name2+Material.getSeparateLanguage()+name3;
                
                material.setName(nameAll); 
            }else{
                 material.setName(getString(FRM_FIELD_NAME)); 
            }
            material.setMerkId(getLong(FRM_FIELD_MERK_ID));
            material.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            material.setDefaultStockUnitId(getLong(FRM_FIELD_DEFAULT_STOCK_UNIT_ID));
            material.setDefaultPrice(getDouble(FRM_FIELD_DEFAULT_PRICE));
            material.setDefaultPriceCurrencyId(getLong(FRM_FIELD_DEFAULT_PRICE_CURRENCY_ID));
            material.setDefaultCost(getDouble(FRM_FIELD_DEFAULT_COST));
            material.setDefaultCostCurrencyId(getLong(FRM_FIELD_DEFAULT_COST_CURRENCY_ID));
            material.setDefaultSupplierType(getInt(FRM_FIELD_DEFAULT_SUPPLIER_TYPE));
            material.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            material.setPriceType01(getDouble(FRM_FIELD_PRICE_TYPE_01));
            material.setPriceType02(getDouble(FRM_FIELD_PRICE_TYPE_02));
            material.setPriceType03(getDouble(FRM_FIELD_PRICE_TYPE_03));
            material.setMaterialType(getInt(FRM_FIELD_MATERIAL_TYPE));

            // NEW
            material.setLastDiscount(getDouble(FRM_FIELD_LAST_DISCOUNT));
            material.setLastVat(getDouble(FRM_FIELD_LAST_VAT));
            material.setCurrBuyPrice(getDouble(FRM_FIELD_CURR_BUY_PRICE));
            material.setBuyUnitId(getLong(FRM_FIELD_BUY_UNIT_ID));
            material.setExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));

            material.setProfit(getDouble(FRM_FIELD_PROFIT));
            material.setCurrSellPriceRecomentation(getDouble(FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION));
            
            material.setAveragePrice(getDouble(FRM_FIELD_AVERAGE_PRICE));
            material.setMinimumPoint(getInt(FRM_FIELD_MINIMUM_POINT));
            material.setRequiredSerialNumber(getInt(FRM_FIELD_REQUIRED_SERIAL_NUMBER));
            material.setLastUpdate(getDate(FRM_FIELD_LAST_UPDATE));
            material.setProcessStatus(getInt(FRM_FIELD_PROCESS_STATUS));
            material.setMatTypeConsig(getInt(FRM_FIELD_CONSIGMENT_TYPE));
            
            material.setGondolaCode(getLong(FRM_FIELD_GONDOLA_CODE));

            //last cost cargo
            material.setLastCostCargo(getDouble(FRM_FIELD_LAST_COST_CARGO));

            //for edit material -opie 12-06-2012
            material.setEditMaterial(getInt(FRM_FIELD_EDIT_MATERIAL));

            //add opie-eyek 20130903 untuk point sales
            material.setPointSales(getInt(FRM_FIELD_POINT_SALES));
            material.setMaterialDescription(getString(FRM_FIELD_DESCRIPTION));
            
            material.setKepemilikanId(getLong(FRM_FIELD_KEPEMILIKAN));
            material.setPosColor(getLong(FRM_FIELD_WARNA));
            material.setPosKadar(getLong(FRM_FIELD_KADAR));
            material.setMaterialJenisType(getInt(FRM_FIELD_MATERIAL_JENIS_TYPE));
            material.setSalesRule(getInt(FRM_FIELD_SALES_RULE));
            material.setReturnRule(getInt(FRM_FIELD_RETURN_RULE));
            material.setViewInChart(getInt(FRM_FIELD_VIEW_IN_SHOPPING_CHART));
            
        } catch (Exception e) { 
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
