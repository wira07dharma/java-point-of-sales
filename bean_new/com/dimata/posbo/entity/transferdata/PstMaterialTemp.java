package com.dimata.posbo.entity.transferdata;

/* package java */

import java.io.*;
import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;

/* package posbo */
//import com.dimata.posbo.entity.purchasing.*; 
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.common.entity.contact.*;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;

// temporary
//import com.dimata.cashier.entity.billing.PstBillDetail;

public class PstMaterialTemp extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_MATERIAL = "POS_MATERIAL_TEMP";
    public static final String TBL_MATERIAL = "pos_material_temp";

    public static final int FLD_MATERIAL_ID = 0;
    public static final int FLD_SKU = 1;
    public static final int FLD_BARCODE = 2;
    public static final int FLD_NAME = 3;
    public static final int FLD_MERK_ID = 4;
    public static final int FLD_CATEGORY_ID = 5;
    public static final int FLD_SUB_CATEGORY_ID = 6;
    public static final int FLD_DEFAULT_STOCK_UNIT_ID= 7;
    public static final int FLD_DEFAULT_PRICE = 8;
    public static final int FLD_DEFAULT_PRICE_CURRENCY_ID = 9;
    public static final int FLD_DEFAULT_COST = 10;
    public static final int FLD_DEFAULT_COST_CURRENCY_ID = 11;
    public static final int FLD_DEFAULT_SUPPLIER_TYPE = 12;
    public static final int FLD_SUPPLIER_ID = 13;
    public static final int FLD_PRICE_TYPE_01 = 14;
    public static final int FLD_PRICE_TYPE_02 = 15;
    public static final int FLD_PRICE_TYPE_03 = 16;
    public static final int FLD_MATERIAL_TYPE = 17;
    public static final int FLD_LAST_DISCOUNT = 18;
    public static final int FLD_LAST_VAT = 19;
    public static final int FLD_CURR_BUY_PRICE = 20;
    public static final int FLD_EXPIRED_DATE = 21;
    public static final int FLD_BUY_UNIT_ID = 22;
    public static final int FLD_PROFIT = 23;
    public static final int FLD_CURR_SELL_PRICE_RECOMENTATION = 24;
    public static final int FLD_AVERAGE_PRICE = 25;
    public static final int FLD_MINIMUM_POINT = 26;
    public static final int FLD_LAST_UPDATE = 27;
    public static final int FLD_REQUIRED_SERIAL_NUMBER = 28;
    public static final int FLD_PROCESS_STATUS = 29;

    public static final String[] fieldNames = {
        "MATERIAL_ID",
        "SKU",
        "BARCODE",
        "NAME",
        "MERK_ID",
        "CATEGORY_ID",
        "SUB_CATEGORY_ID",
        "DEFAULT_SELL_UNIT_ID",
        "DEFAULT_PRICE",
        "DEFAULT_PRICE_CURRENCY_ID",
        "DEFAULT_COST",
        "DEFAULT_COST_CURRENCY_ID",
        "DEFAULT_SUPPLIER_TYPE",
        "SUPPLIER_ID",
        "PRICE_TYPE_01",
        "PRICE_TYPE_02",
        "PRICE_TYPE_03",
        "MATERIAL_TYPE",
        "LAST_DISCOUNT",
        "LAST_VAT",
        "CURR_BUY_PRICE",
        "EXPIRED_DATE",
        "BUY_UNIT_ID",
        "PROFIT",
        "CURR_SELL_PRICE_RECOMENTATION",
        "AVERAGE_PRICE",
        "MINIMUM_POINT",
        "LAST_UPDATE",
        "REQUIRED_SERIAL_NUMBER",
        "PROCESS_STATUS"        
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
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
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,//*
        TYPE_INT,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT
    };

    public static final  int NOT_REQUIRED = 0;
    public static final  int REQUIRED = 1;
    
    public static final  String requiredNames[][] ={
        {"Tidak Diperlukan","Diperlukan"},
        {"Not Required","Required"}
    };
    
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
    
    
    public PstMaterialTemp() {
    }

    public PstMaterialTemp(int i) throws DBException {
        super(new PstMaterialTemp());
    }

    public PstMaterialTemp(String sOid) throws DBException {
        super(new PstMaterialTemp(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMaterialTemp(long lOid) throws DBException {
        super(new PstMaterialTemp(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_MATERIAL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialTemp().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Material material = fetchExc(ent.getOID());
        ent = (Entity) material;
        return material.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Material) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Material) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Material fetchExc(long oid) throws DBException {
        try {
            Material material = new Material();
            PstMaterialTemp pstMaterial = new PstMaterialTemp(oid);
            material.setOID(oid);

            material.setSku(pstMaterial.getString(FLD_SKU));
            material.setBarCode(pstMaterial.getString(FLD_BARCODE));
            material.setName(pstMaterial.getString(FLD_NAME));
            material.setMerkId(pstMaterial.getlong(FLD_MERK_ID));
            material.setCategoryId(pstMaterial.getlong(FLD_CATEGORY_ID));

            material.setSubCategoryId(pstMaterial.getlong(FLD_SUB_CATEGORY_ID));
            material.setDefaultStockUnitId(pstMaterial.getlong(FLD_DEFAULT_STOCK_UNIT_ID));
            material.setDefaultPrice(pstMaterial.getdouble(FLD_DEFAULT_PRICE));

            material.setDefaultPriceCurrencyId(pstMaterial.getlong(FLD_DEFAULT_PRICE_CURRENCY_ID));
            material.setDefaultCost(pstMaterial.getdouble(FLD_DEFAULT_COST));
            material.setDefaultCostCurrencyId(pstMaterial.getlong(FLD_DEFAULT_COST_CURRENCY_ID));
            material.setDefaultSupplierType(pstMaterial.getInt(FLD_DEFAULT_SUPPLIER_TYPE));
            material.setSupplierId(pstMaterial.getlong(FLD_SUPPLIER_ID));
            material.setPriceType01(pstMaterial.getdouble(FLD_PRICE_TYPE_01));
            material.setPriceType02(pstMaterial.getdouble(FLD_PRICE_TYPE_02));
            material.setPriceType03(pstMaterial.getdouble(FLD_PRICE_TYPE_03));
            material.setMaterialType(pstMaterial.getInt(FLD_MATERIAL_TYPE));

            // NEW
            material.setLastDiscount(pstMaterial.getdouble(FLD_LAST_DISCOUNT));
            material.setLastVat(pstMaterial.getdouble(FLD_LAST_VAT));
            material.setCurrBuyPrice(pstMaterial.getdouble(FLD_CURR_BUY_PRICE));
            material.setExpiredDate(pstMaterial.getDate(FLD_EXPIRED_DATE));
            material.setBuyUnitId(pstMaterial.getlong(FLD_BUY_UNIT_ID));
            material.setProfit(pstMaterial.getdouble(FLD_PROFIT));
            material.setCurrSellPriceRecomentation(pstMaterial.getdouble(FLD_CURR_SELL_PRICE_RECOMENTATION));

            material.setAveragePrice(pstMaterial.getdouble(FLD_AVERAGE_PRICE));
            material.setMinimumPoint(pstMaterial.getInt(FLD_MINIMUM_POINT));
            material.setRequiredSerialNumber(pstMaterial.getInt(FLD_REQUIRED_SERIAL_NUMBER));
            material.setLastUpdate(pstMaterial.getDate(FLD_LAST_UPDATE));
            material.setProcessStatus(pstMaterial.getInt(FLD_PROCESS_STATUS));
            
            return material;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            e.printStackTrace();            
            System.out.println("err : "+e.toString());            
            throw new DBException(new PstMaterialTemp(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Material material) throws DBException {
        try {
            PstMaterialTemp pstMaterial = new PstMaterialTemp(0);

            pstMaterial.setString(FLD_SKU, material.getSku());
            pstMaterial.setString(FLD_BARCODE, material.getBarCode());
            pstMaterial.setString(FLD_NAME, material.getName());
            pstMaterial.setLong(FLD_MERK_ID, material.getMerkId());
            pstMaterial.setLong(FLD_CATEGORY_ID, material.getCategoryId());

            pstMaterial.setLong(FLD_SUB_CATEGORY_ID, material.getSubCategoryId());
            pstMaterial.setLong(FLD_DEFAULT_STOCK_UNIT_ID, material.getDefaultStockUnitId());
            pstMaterial.setDouble(FLD_DEFAULT_PRICE, material.getDefaultPrice());

            pstMaterial.setLong(FLD_DEFAULT_PRICE_CURRENCY_ID, material.getDefaultPriceCurrencyId());
            pstMaterial.setDouble(FLD_DEFAULT_COST, material.getDefaultCost());
            pstMaterial.setLong(FLD_DEFAULT_COST_CURRENCY_ID, material.getDefaultCostCurrencyId());
            pstMaterial.setInt(FLD_DEFAULT_SUPPLIER_TYPE, material.getDefaultSupplierType());
            pstMaterial.setLong(FLD_SUPPLIER_ID, material.getSupplierId());
            pstMaterial.setDouble(FLD_PRICE_TYPE_01, material.getPriceType01());
            pstMaterial.setDouble(FLD_PRICE_TYPE_02, material.getPriceType02());
            pstMaterial.setDouble(FLD_PRICE_TYPE_03, material.getPriceType03());
            pstMaterial.setInt(FLD_MATERIAL_TYPE, material.getMaterialType());

            // NEW
            pstMaterial.setDouble(FLD_LAST_DISCOUNT, material.getLastDiscount());
            pstMaterial.setDouble(FLD_LAST_VAT, material.getLastVat());
            pstMaterial.setDouble(FLD_CURR_BUY_PRICE, material.getCurrBuyPrice());
            pstMaterial.setDate(FLD_EXPIRED_DATE, material.getExpiredDate());
            pstMaterial.setLong(FLD_BUY_UNIT_ID, material.getBuyUnitId());
            pstMaterial.setDouble(FLD_PROFIT, material.getProfit());
            pstMaterial.setDouble(FLD_CURR_SELL_PRICE_RECOMENTATION, material.getCurrSellPriceRecomentation());

            pstMaterial.setDouble(FLD_AVERAGE_PRICE,material.getAveragePrice());
            pstMaterial.setInt(FLD_MINIMUM_POINT,material.getMinimumPoint());
            pstMaterial.setInt(FLD_REQUIRED_SERIAL_NUMBER,material.getRequiredSerialNumber());
            pstMaterial.setDate(FLD_LAST_UPDATE,material.getLastUpdate());
            pstMaterial.setInt(FLD_PROCESS_STATUS,material.getProcessStatus());
            
            pstMaterial.insert();
            material.setOID(pstMaterial.getlong(FLD_MATERIAL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialTemp(0), DBException.UNKNOWN);
        }
        return material.getOID();
    }

    public static long updateExc(Material material) throws DBException {
        try {
            if (material.getOID() != 0) {
                PstMaterialTemp pstMaterial = new PstMaterialTemp(material.getOID());

                pstMaterial.setString(FLD_SKU, material.getSku());
                pstMaterial.setString(FLD_BARCODE, material.getBarCode());
                pstMaterial.setString(FLD_NAME, material.getName());
                pstMaterial.setLong(FLD_MERK_ID, material.getMerkId());
                pstMaterial.setLong(FLD_CATEGORY_ID, material.getCategoryId());

                pstMaterial.setLong(FLD_SUB_CATEGORY_ID, material.getSubCategoryId());
                pstMaterial.setLong(FLD_DEFAULT_STOCK_UNIT_ID, material.getDefaultStockUnitId());
                pstMaterial.setDouble(FLD_DEFAULT_PRICE, material.getDefaultPrice());

                pstMaterial.setLong(FLD_DEFAULT_PRICE_CURRENCY_ID, material.getDefaultPriceCurrencyId());
                pstMaterial.setDouble(FLD_DEFAULT_COST, material.getDefaultCost());
                pstMaterial.setLong(FLD_DEFAULT_COST_CURRENCY_ID, material.getDefaultCostCurrencyId());
                pstMaterial.setInt(FLD_DEFAULT_SUPPLIER_TYPE, material.getDefaultSupplierType());
                pstMaterial.setLong(FLD_SUPPLIER_ID, material.getSupplierId());
                pstMaterial.setDouble(FLD_PRICE_TYPE_01, material.getPriceType01());
                pstMaterial.setDouble(FLD_PRICE_TYPE_02, material.getPriceType02());
                pstMaterial.setDouble(FLD_PRICE_TYPE_03, material.getPriceType03());
                pstMaterial.setInt(FLD_MATERIAL_TYPE, material.getMaterialType());

                // NEW
                pstMaterial.setDouble(FLD_LAST_DISCOUNT, material.getLastDiscount());
                pstMaterial.setDouble(FLD_LAST_VAT, material.getLastVat());
                pstMaterial.setDouble(FLD_CURR_BUY_PRICE, material.getCurrBuyPrice());
                pstMaterial.setDate(FLD_EXPIRED_DATE, material.getExpiredDate());
                pstMaterial.setLong(FLD_BUY_UNIT_ID, material.getBuyUnitId());
                pstMaterial.setDouble(FLD_PROFIT, material.getProfit());
                pstMaterial.setDouble(FLD_CURR_SELL_PRICE_RECOMENTATION, material.getCurrSellPriceRecomentation());
                
                pstMaterial.setDouble(FLD_AVERAGE_PRICE,material.getAveragePrice());
                pstMaterial.setInt(FLD_MINIMUM_POINT,material.getMinimumPoint());
                pstMaterial.setInt(FLD_REQUIRED_SERIAL_NUMBER,material.getRequiredSerialNumber());
                pstMaterial.setDate(FLD_LAST_UPDATE,material.getLastUpdate());
                pstMaterial.setInt(FLD_PROCESS_STATUS,material.getProcessStatus());
            
                pstMaterial.update();
                return material.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialTemp(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMinMaxStock.deleteByMaterial(oid);

            PstMaterialComposit.deleteByMaterial(oid);

            PstMaterialTemp pstMaterial = new PstMaterialTemp(oid);
            pstMaterial.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialTemp(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Material material = new Material();
                resultToObject(rs, material);
                lists.add(material);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //Overload this function to make it work!!
    public static Vector list(int limitStart, int recordToGet, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MAT." + fieldNames[FLD_MATERIAL_ID] +
                    " ,MAT." + fieldNames[FLD_SKU] +
                    " ,MAT." + fieldNames[FLD_BARCODE] +
                    " ,MAT." + fieldNames[FLD_NAME] +
                    " ,MAT." + fieldNames[FLD_DEFAULT_SUPPLIER_TYPE] +
                    " ,MAT." + fieldNames[FLD_DEFAULT_PRICE] +
                    " ,MAT." + fieldNames[FLD_DEFAULT_COST] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    " FROM ((" + TBL_MATERIAL +
                    " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT.CATEGORY_ID" +
                    " = CAT.CATEGORY_ID)" +
                    " LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
                    " SCAT ON MAT.SUB_CATEGORY_ID" +
                    " = SCAT.SUB_CATEGORY_ID" +
                    " ) LEFT JOIN contact_list CNT" +
                    " ON MAT.SUPPLIER_ID = CNT.CONTACT_ID";

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                Material material = new Material();
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                ContactList cnt = new ContactList();

                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setBarCode(rs.getString(3));
                material.setName(rs.getString(4));
                material.setDefaultSupplierType(rs.getInt(5));
                material.setDefaultPrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                temp.add(material);

                category.setName(rs.getString(8));
                category.setCode(rs.getString(9));
                temp.add(category);

                subCategory.setName(rs.getString(10));
                subCategory.setCode(rs.getString(11));
                temp.add(subCategory);

                cnt.setCompName(rs.getString(12));
                cnt.setContactCode(rs.getString(13));
                temp.add(cnt);

                lists.add(temp);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static void resultToObject(ResultSet rs, Material material) {
        try {
            material.setOID(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MATERIAL_ID]));
            material.setSku(rs.getString(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU]));
            material.setBarCode(rs.getString(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_BARCODE]));
            material.setName(rs.getString(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_NAME]));
            material.setMerkId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MERK_ID]));
            material.setCategoryId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CATEGORY_ID]));

            material.setSubCategoryId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SUB_CATEGORY_ID]));
            material.setDefaultStockUnitId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_STOCK_UNIT_ID]));
            material.setDefaultPrice(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_PRICE]));

            material.setDefaultPriceCurrencyId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_PRICE_CURRENCY_ID]));
            material.setDefaultCost(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_COST]));
            material.setDefaultCostCurrencyId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_COST_CURRENCY_ID]));
            material.setDefaultSupplierType(rs.getInt(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_SUPPLIER_TYPE]));
            material.setSupplierId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SUPPLIER_ID]));
            material.setPriceType01(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PRICE_TYPE_01]));
            material.setPriceType02(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PRICE_TYPE_02]));
            material.setPriceType03(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PRICE_TYPE_03]));
            material.setMaterialType(rs.getInt(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MATERIAL_TYPE]));

            // NEW
            material.setLastDiscount(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_DISCOUNT]));
            material.setLastVat(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_VAT]));
            material.setCurrBuyPrice(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CURR_BUY_PRICE]));
            material.setExpiredDate(rs.getDate(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_EXPIRED_DATE]));
            material.setBuyUnitId(rs.getLong(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_BUY_UNIT_ID]));
            material.setProfit(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PROFIT]));
            material.setCurrSellPriceRecomentation(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CURR_SELL_PRICE_RECOMENTATION]));

            material.setAveragePrice(rs.getDouble(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_AVERAGE_PRICE]));
            material.setMinimumPoint(rs.getInt(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MINIMUM_POINT]));
            material.setRequiredSerialNumber(rs.getInt(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_REQUIRED_SERIAL_NUMBER]));
            material.setLastUpdate(rs.getDate(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_UPDATE]));
            material.setProcessStatus(rs.getInt(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PROCESS_STATUS]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long catalogId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL +
                    " WHERE " + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MATERIAL_ID] +
                    " = " + catalogId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MATERIAL_ID] + ") FROM " + TBL_MATERIAL;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    //Supplier Type Combo
    public static final int SUPPLIER_CONSIGNMENT = 0;
    public static final int SUPPLIER_CASH = 1;
    public static final int SUPPLIER_ELSE = 2;

    public static final String SpTypeSourceKey[] = {"Consignment", "Cash", "Else"};
    public static final int SpTypeSourceValue[] = {0, 1, 2};

    public static Vector listSpTypeSourceTypeKey() {
        Vector result = new Vector(1, 1);
        for (int s = 0; s < SpTypeSourceKey.length; s++) {
            result.add(SpTypeSourceKey[s]);
        }
        return result;
    }

    public static Vector listSpTypeSourceTypeValue() {
        Vector result = new Vector(1, 1);
        for (int s = 0; s < SpTypeSourceValue.length; s++) {
            result.add("" + SpTypeSourceValue[s]);
        }
        return result;
    }

    //Material Type Combo
    public static final int MATERIAL_TYPE_REGULAR = 0;
    public static final int MATERIAL_TYPE_COMPOSITE = 1;

    public static final String MatTypeSourceKey[] = {"Regular", "Composite"};
    public static final int MatTypeSourceValue[] = {0, 1};

    public static Vector listMatTypeKey() {
        Vector result = new Vector(1, 1);
        for (int s = 0; s < MatTypeSourceKey.length; s++) {
            result.add(MatTypeSourceKey[s]);
        }
        return result;
    }

    public static Vector listMatTypeValue() {
        Vector result = new Vector(1, 1);
        for (int s = 0; s < MatTypeSourceValue.length; s++) {
            result.add("" + MatTypeSourceValue[s]);
        }
        return result;
    }

    /**
     * this method used to list material data
     * @param objMaterial
     * @param limitStart --> starting index of displaying result
     * @param recordToGet --> amount of displaying result
     * @return Vector of material
     * @update by Edhy
     */
    public static Vector getListMaterialItem(long oidVendor, Material objMaterial,
                                             int limitStart, int recordToGet, String orderBy) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT "+
                    " MAT." + fieldNames[FLD_MATERIAL_ID] +
                    " , MAT." + fieldNames[FLD_SKU] +
                    " , MAT." + fieldNames[FLD_NAME] +
                    " , MAT." + fieldNames[FLD_CATEGORY_ID] +
                    " , CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " , MAT." + fieldNames[FLD_DEFAULT_STOCK_UNIT_ID] +
                    " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    " , MAT." + fieldNames[FLD_DEFAULT_COST_CURRENCY_ID] +
                    " , CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] +
                    " , MAT." + fieldNames[FLD_DEFAULT_COST] +
                    " , MAT." + fieldNames[FLD_SUB_CATEGORY_ID] +
                    " , MAT." + fieldNames[FLD_DEFAULT_PRICE] +
                    " , VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE]+
                    " , VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT]+
                    " , VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE]+
                    " , UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " FROM " + TBL_MATERIAL +
                    " MAT "+
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + fieldNames[FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " LEFT JOIN " + PstMatCurrency.TBL_CURRENCY +
                    " CURR ON MAT." + fieldNames[FLD_DEFAULT_COST_CURRENCY_ID] +
                    " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID]+
                    " INNER JOIN "+PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" AS VDR ON " +
                    " MAT."+fieldNames[FLD_MATERIAL_ID]+
                    " = VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT +
                    " UNT ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_BUYING_UNIT_ID] +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            String strGroup = "";
            if (objMaterial.getCategoryId() > 0) {
                strGroup = " MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CATEGORY_ID] +
                        " = " + objMaterial.getCategoryId();
            }

            String strSubCategory = "";
            if (objMaterial.getSubCategoryId() > 0) {
                strSubCategory = " MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SUB_CATEGORY_ID] +
                        " = " + objMaterial.getSubCategoryId();
            }

            String strCode = "";
            if (objMaterial.getSku() != "" && objMaterial.getSku().length() > 0) {
                strCode = " (MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU] +
                        " LIKE '%" + objMaterial.getSku() + "%')";
            }

            String strName = "";
            if (objMaterial.getName() != "" && objMaterial.getName().length() > 0) {
                strName = " (MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_NAME] +
                        " LIKE '%" + objMaterial.getName() + "%')";
            }


            String whereClause = "";
            if (oidVendor != 0) {
                whereClause = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + oidVendor;
            }

            if (strGroup.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strGroup;
                } else {
                    whereClause = strGroup;
                }
            }

            if (strSubCategory.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategory;
                } else {
                    whereClause = whereClause + strSubCategory;
                }
            }

            if (strCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCode;
                } else {
                    whereClause = whereClause + strCode;
                }
            }

            if (strName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strName;
                } else {
                    whereClause = whereClause + strName;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " ORDER BY " + orderBy;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;
                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                Material material = new Material();
                Category cat = new Category();
                Unit matUnit = new Unit();
                MatCurrency matCurr = new MatCurrency();
                MatVendorPrice matVendorPrice = new MatVendorPrice();

                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setName(rs.getString(3));
                material.setCategoryId(rs.getLong(4));
                material.setDefaultStockUnitId(rs.getLong(6));
                material.setDefaultCostCurrencyId(rs.getLong(8));
                material.setDefaultCost(rs.getDouble(10));
                material.setSubCategoryId(rs.getLong(11));
                material.setDefaultPrice(rs.getDouble(12));
                vt.add(material);

                cat.setName(rs.getString(5));
                vt.add(cat);

                matUnit.setOID(rs.getLong(16));
                matUnit.setCode(rs.getString(7));
                vt.add(matUnit);

                matCurr.setCode(rs.getString(9));
                vt.add(matCurr);

                matVendorPrice.setOrgBuyingPrice(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE]));
                matVendorPrice.setLastDiscount(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT]));
                matVendorPrice.setCurrBuyingPrice(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE]));
                vt.add(matVendorPrice);

                result.add(vt);
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * this method used to list material data
     * @param objMaterial
     * @return amount of Vector of material
     */
    public static int getCountMaterialItem(long oidVendor, Material objMaterial) {
        int result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT( DISTINCT MAT." + fieldNames[FLD_MATERIAL_ID] + ") AS CNT" +
                    " FROM " + TBL_MATERIAL +
                    " MAT "+
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + fieldNames[FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " LEFT JOIN " + PstMatCurrency.TBL_CURRENCY +
                    " CURR ON MAT." + fieldNames[FLD_DEFAULT_COST_CURRENCY_ID] +
                    " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID]+
                    " INNER JOIN "+PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" AS VDR ON " +
                    " MAT."+fieldNames[FLD_MATERIAL_ID]+
                    " = VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT +
                    " UNT ON MAT." + fieldNames[FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            String strGroup = "";
            if (objMaterial.getCategoryId() > 0) {
                strGroup = " MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CATEGORY_ID] +
                        " = " + objMaterial.getCategoryId();
            }

            String strSubCategory = "";
            if (objMaterial.getSubCategoryId() > 0) {
                strSubCategory = " MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SUB_CATEGORY_ID] +
                        " = " + objMaterial.getSubCategoryId();
            }

            String strCode = "";
            if (objMaterial.getSku() != "" && objMaterial.getSku().length() > 0) {
                strCode = " (MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU] +
                        " LIKE '%" + objMaterial.getSku() + "%')";
            }

            String strName = "";
            if (objMaterial.getName() != "" && objMaterial.getName().length() > 0) {
                strName = " (MAT." + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_NAME] +
                        " LIKE '%" + objMaterial.getName() + "%')";
            }

            String whereClause = "";
            if (oidVendor != 0) {
                whereClause = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + oidVendor;
            }

            if (strGroup.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strGroup;
                } else {
                    whereClause = strGroup;
                }
            }

            if (strSubCategory.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategory;
                } else {
                    whereClause = whereClause + strSubCategory;
                }
            }

            if (strCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCode;
                } else {
                    whereClause = whereClause + strCode;
                }
            }

            if (strName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strName;
                } else {
                    whereClause = whereClause + strName;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //Untuk create new PO
    public static String getNameBySku(String sku) {
        String hasil = "";
        DBResultSet dbrs = null;
        try {
            String sql_str = " SELECT " + fieldNames[FLD_NAME] +
                    " FROM " + TBL_MATERIAL +
                    " WHERE " + fieldNames[FLD_SKU] +
                    " = '" + sku + "'";
            dbrs = DBHandler.execQueryResult(sql_str);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                hasil = rs.getString(1);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println(exc);
        }
        return hasil;
    }

    //Untuk create new PO
    public static Material fetchBySku(String sku) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        Material mat = new Material();
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL +
                    " WHERE " + fieldNames[FLD_SKU] +
                    " = '" + sku + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, mat);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return mat;
    }


    /*
    public static void checkSales(long oid){
        String whereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"="+oid;
        Vector listMaterial = PstBillDetail.list(0,0,whereClause,"");
        if(listMaterial==null || listMaterial.size()==0){
            long result = 0;
            try{
               PstMaterialTemp pstMaterial = new PstMaterialTemp();
               result = pstMaterial.deleteExc(oid);
               System.out.println("Deleted material oid = "+result);
            }catch(Exception e){
                System.out.println("Error when delete material");
            }
        }else{
            System.out.println("No material oid "+oid+" in sales process");
        }
    }

    public static void checkSku(String Sku){
        String whereClause = PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU]+"='"+Sku+"'";
        String orderBy = PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU];
        Vector listMaterial = PstMaterialTemp.list(0,0,whereClause,orderBy);
        if(listMaterial!=null && listMaterial.size()>1){
            int maxMaterial = listMaterial.size();
            for(int i=0; i<maxMaterial; i++){
                Material material = (Material)listMaterial.get(i);
                try{
                    checkSales(material.getOID());
                }catch(Exception e){
                    System.out.println("Error when check data to sales process");
                }
            }
        }else{
            System.out.println("Material with sku = "+Sku+" only one available");
        }
    }

    public static void main(String args[]){
        String whereClause = PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU] + " LIKE '0%'";
        String orderBy = PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU];

        Vector listMaterial = PstMaterialTemp.list(0,0,whereClause,orderBy);
        if(listMaterial!=null && listMaterial.size()>0){
            int maxMaterial = listMaterial.size();
            System.out.println("max SKU : "+maxMaterial);
            for(int i=0; i<maxMaterial; i++){
                Material material = (Material)listMaterial.get(i);
                try{
                    System.out.println("iterate ke : "+(i+1));
                    checkSku(material.getSku());
                }catch(Exception e){
                    System.out.println("Err when check Sku : "+e.toString());
                }
            }
        }
    }
     */

   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         Material material = PstMaterialTemp.fetchExc(oid);
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MATERIAL_ID], material.getOID());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SKU], material.getSku());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_BARCODE], material.getBarCode());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_NAME], material.getName());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MERK_ID], material.getMerkId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CATEGORY_ID], material.getCategoryId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SUB_CATEGORY_ID], material.getSubCategoryId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_STOCK_UNIT_ID ], material.getDefaultStockUnitId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_PRICE], material.getDefaultPrice());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_PRICE_CURRENCY_ID], material.getDefaultPriceCurrencyId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_COST], material.getDefaultCost());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_COST_CURRENCY_ID], material.getDefaultCostCurrencyId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_DEFAULT_SUPPLIER_TYPE], material.getDefaultSupplierType());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_SUPPLIER_ID], material.getSupplierId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PRICE_TYPE_01], material.getPriceType01());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PRICE_TYPE_02], material.getPriceType02());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PRICE_TYPE_03], material.getPriceType03());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MATERIAL_TYPE], material.getMaterialType());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_DISCOUNT], material.getLastDiscount());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_VAT], material.getLastVat());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CURR_BUY_PRICE], material.getCurrBuyPrice());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_EXPIRED_DATE], material.getExpiredDate());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_BUY_UNIT_ID], material.getBuyUnitId());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PROFIT], material.getProfit());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_CURR_SELL_PRICE_RECOMENTATION], material.getCurrSellPriceRecomentation());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_AVERAGE_PRICE], material.getAveragePrice());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_MINIMUM_POINT], material.getMinimumPoint());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_UPDATE], material.getLastUpdate());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_REQUIRED_SERIAL_NUMBER], material.getRequiredSerialNumber());
         object.put(PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_PROCESS_STATUS], material.getProcessStatus());
      }catch(Exception exc){}
      return object;
   }

}
