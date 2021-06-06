package com.dimata.posbo.session.warehouse;

import java.util.*;
import java.util.Date;
import java.sql.*;

import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.util.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.system.AppValue;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;


public class SessReportStock {

    public static final String SESS_SRC_REPORT_STOCK = "SESSION_SRC_REPORT_STOCK";
    public static final String SESS_SRC_REPORT_STOCK_LIST = "SESSION_SRC_REPORT_STOCK_LIST";
    public static final String SESS_SRC_REPORT_STOCK_MIN = "SESSION_SRC_REPORT_STOCK_MIN";
    public static final String SESS_SRC_REPORT_STOCK_MAX = "SESSION_SRC_REPORT_STOCK_MAX";
    public static final String SESS_SRC_REPORT_STOCK_DATE = "SESSION_SRC_REPORT_STOCK_DATE";
    public static final String SESS_SRC_REPORT_STOCK_ASSET = "SESSION_SRC_REPORT_STOCK_ASSET";

    // constant for stock report on store
    public static final String SESS_SRC_REPORT_STOCK_ST_LIST_ALL = "SESS_SRC_REPORT_STOCK_ST_LIST_ALL";
    public static final String SESS_SRC_REPORT_STOCK_ST_LIST_PER_SUPP = "SESS_SRC_REPORT_STOCK_ST_LIST_PER_SUPP";
    public static final String SESS_SRC_REPORT_STOCK_ST_LIST_PER_CAT = "SESS_SRC_REPORT_STOCK_ST_LIST_PER_CAT";
    public static final String SESS_SRC_REPORT_STOCK_ST_POS_ALL = "SESS_SRC_REPORT_STOCK_ST_POS_ALL";
    public static final String SESS_SRC_REPORT_STOCK_ST_POS_PER_SUPP = "SESS_SRC_REPORT_STOCK_ST_POS_PER_SUPP";
    public static final String SESS_SRC_REPORT_STOCK_ST_POS_PER_CAT = "SESS_SRC_REPORT_STOCK_ST_POS_PER_CAT";
    public static final String SESS_SRC_REPORT_STOCK_ST_OPNAME = "SESS_SRC_REPORT_STOCK_ST_OPNAME";
    public static final String SESS_SRC_REPORT_STOCK_ST_CORRECTION = "SESS_SRC_REPORT_STOCK_ST_CORRECTION";

    // for report posisi stock
    public static final String TBL_MATERIAL_STOCK_REPORT = "pos_material_stock_report";
    public static final String TBL_MATERIAL_STOCK_REPORT_HIS = "pos_material_stock_report_temp";
    public static final int TYPE_REPORT_POSISI_ALL = 0;
    public static final int TYPE_REPORT_POSISI_CATEGORY = 1;
    public static final int TYPE_REPORT_POSISI_SUPPLIER = 2;
    
    // constant for session stock potition report
    public static final String SESS_SRC_STOCK_POTITION_REPORT = "SESS_SRC_STOCK_POTITION_REPORT";
    public static final String SESS_SRC_STOCK_POTITION_REPORT_BY_CATEGORY = "SESS_SRC_STOCK_POTITION_REPORT_BY_CATEGORY";

    // for weekly report
    public static final int TYPE_PERIODE_I = 0;
    public static final int TYPE_PERIODE_II = 1;
    public static String[] fieldPeriode = {"I", "II"};


    public static Vector getReportStock(SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + //MATERIAL_STOCK_ID
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME" +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE" +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST" +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] + // OPENING_QTY" +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + // QTY_IN" +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + // QTY_OUT" +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY" +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME" +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME" +
                    //" AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE" +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // DEFAULT_COST" +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" + // material_stock
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + //  MATERIAL_UNIT_ID+
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " INNER JOIN " + PstPeriode.TBL_STOCK_PERIODE + " PRD" + // periode
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + // PERIODE_ID+
                    " = PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + // PERIODE_ID
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID+
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID+
                    /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID*/
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_STOCK_UNIT_ID
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID; 

            if (srcReportStock.getSupplierId() != 0) {
                sql = sql + " INNER JOIN "+PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" VP "+
                    " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
                    "=VP."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            
            
            String strMerkId = "";
            if (srcReportStock.getMerkId() != 0) {
                strMerkId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID]+"="+srcReportStock.getMerkId();
                /*sql = sql + " INNER JOIN "+PstMerk.TBL_MAT_MERK+" MRK "+
                    " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID]+
                    "=MRK."+PstMerk.fieldNames[PstMerk.FLD_MERK_ID];*/
            }
            
                    
            String strLocationId = "";
            if (srcReportStock.getLocationId() != 0) {
                strLocationId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportStock.getSupplierId() != 0) {
                strSupplierId = " VP." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + srcReportStock.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportStock.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportStock.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId();
            }

            String strPeriodeId = "";
            if (srcReportStock.getPeriodeId() != 0) {
                strPeriodeId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId();
            }

            Vector vectSku = LogicParser.textSentence(srcReportStock.getSku());
            for (int i = 0; i < vectSku.size(); i++) {
                String name = (String) vectSku.get(i);
                if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                    vectSku.remove(i);
            }

            String strSku = "";
            if (vectSku != null && vectSku.size() > 0) {
                for (int a = 0; a < vectSku.size(); a++) {
                    if (strSku.length() > 0) {
                        strSku = strSku + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    } else {
                        strSku = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    }
                }
                strSku = "(" + strSku + ")";
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) { 
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if (strMerkId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strMerkId;
                } else {
                    whereClause = whereClause + strMerkId;
                }
            }
            
            
            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (strPeriodeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPeriodeId;
                } else {
                    whereClause = whereClause + strPeriodeId;
                }
            }

            if (strSku.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSku;
                } else {
                    whereClause = whereClause + strSku;
                } 
            }

            String strKlausa = "(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0" + // QTY
                    " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + " <> 0" + // QTY_IN
                    " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + " <> 0)"; // QTY_OUT

            /*if (whereClause.length() > 0) { 
                whereClause += " AND " + strKlausa +
                    " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+"="+srcReportStock.getTypeConsig();
            } else {
                whereClause += strKlausa +
                    " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+"="+srcReportStock.getTypeConsig();
            }*/

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }


            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                MaterialStock ms = new MaterialStock();
                Material mat = new Material();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                Unit unt = new Unit();

                ms.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                ms.setOpeningQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
                ms.setQtyIn(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN]));
                ms.setQtyOut(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT]));
                ms.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                temp.add(ms);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                mat.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                temp.add(mat);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName("");//rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                result.add(temp);

            }
            rs.close();
            stmt.close();
            PstMaterialStock.closeLocalConnection(koneksi);

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static Vector getReportStockAsset(SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] + // CODE
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    ", COUNT(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + ") AS CountOfMATERIAL_UNIT_ID" + // MATERIAL_UNIT_ID
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS SumOfQTY" + // QTY
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + ") AS SumOfQTY_IN" + // QTY_IN
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + ") AS SumOfQTY_OUT" + // QTY_OUT
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + ") AS TotalBeli" + // QTY * DEFAULT_COST
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS TotalJual" + // QTY * DEFAULT_PRICE
                    " FROM ((" + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + //  SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + //  SUB_CATEGORY_ID
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID+
                    " ) INNER JOIN " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" + // material_stock
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]; // MATERIAL_UNIT_ID;

            String strLocationId = "";
            if (srcReportStock.getLocationId() != 0) {
                strLocationId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportStock.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportStock.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportStock.getCategoryId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            String strPeriodeId = "";
            if (srcReportStock.getPeriodeId() != 0) {
                strPeriodeId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId();
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strPeriodeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPeriodeId;
                } else {
                    whereClause = whereClause + strPeriodeId;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] + // code
                    " , CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME"+
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME" +
                    " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] + // CODE
                    " , SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]; // CODE";

            //System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(rs.getString(PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]));
                temp.add(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(new Double(rs.getDouble("CountOfMATERIAL_UNIT_ID")));
                temp.add(new Double(rs.getDouble("SumOfQTY")));
                temp.add(new Double(rs.getDouble("SumOfQTY_IN")));
                temp.add(new Double(rs.getDouble("SumOfQTY_OUT")));
                temp.add(new Double(rs.getDouble("TotalBeli")));
                temp.add(new Double(rs.getDouble("TotalJual")));

                result.add(temp);

            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /** ary, gadnyana
     * Tampilkan barang yg qtynya dibawah qty min tapi <> 0
     * kalau nol ditampilin terlalu banyak
     * @param srcReportStock
     * @return
     */
    public static Vector getReportStockMin(SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + // MATERIAL_STOCK_ID+
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] + // OPENING_QTY
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + // QTY_IN
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + // QTY_OUT
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] + // QTY_MIN
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE
                    " FROM (((" + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" + // material_stock
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_UNIT_ID+
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " )INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_STOCK_UNIT_ID
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID;

            String strLocationId = "";
            if (srcReportStock.getLocationId() != 0) {
                strLocationId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportStock.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportStock.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportStock.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportStock.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId();
            }

            String strPeriodeId = "";
            if (srcReportStock.getPeriodeId() != 0) {
                strPeriodeId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId();
            }

            Vector vectSku = LogicParser.textSentence(srcReportStock.getSku());
            for (int i = 0; i < vectSku.size(); i++) {
                String name = (String) vectSku.get(i);
                if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                    vectSku.remove(i);
            }

            String strSku = "";
            if (vectSku != null && vectSku.size() > 0) {
                for (int a = 0; a < vectSku.size(); a++) {
                    if (strSku.length() > 0) {
                        strSku = strSku + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    } else {
                        strSku = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    }
                }
                strSku = "(" + strSku + ")";
            }

            String strKondisi = " (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " <= MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ") AND (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " > 0)"; // QTY // QTY_MIN // QTY

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (strPeriodeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPeriodeId;
                } else {
                    whereClause = whereClause + strPeriodeId;
                }
            }

            if (strSku.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSku;
                } else {
                    whereClause = whereClause + strSku;
                }
            }

            if (strKondisi.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strKondisi;
                } else {
                    whereClause = whereClause + strKondisi;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MaterialStock ms = new MaterialStock();
                Material mat = new Material();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                Unit unt = new Unit();

                ms.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                ms.setOpeningQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
                ms.setQtyIn(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN]));
                ms.setQtyOut(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT]));
                ms.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                ms.setQtyMin(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN]));
                temp.add(ms);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                temp.add(mat);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                result.add(temp);

            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /** ary, gadnyana
     * Tampilkan barang yg qtynya dibatas qty max
     * @param srcReportStock
     * @return
     */
    public static Vector getReportStockMax(SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + //  MATERIAL_STOCK_ID
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + //  SKU
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] + // OPENING_QTY
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + // QTY_IN
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + // QTY_OUT
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] + // QTY_MAX
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE
                    " FROM (((" + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" + // material_stock
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_UNIT_ID
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " )INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + //  CATEGORY_ID
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_SELL_UNIT_ID
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID;

            String strLocationId = "";
            if (srcReportStock.getLocationId() != 0) {
                strLocationId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportStock.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportStock.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportStock.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportStock.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId();
            }

            String strPeriodeId = "";
            if (srcReportStock.getPeriodeId() != 0) {
                strPeriodeId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId();
            }

            Vector vectSku = LogicParser.textSentence(srcReportStock.getSku());
            for (int i = 0; i < vectSku.size(); i++) {
                String name = (String) vectSku.get(i);
                if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                    vectSku.remove(i);
            }

            String strSku = "";
            if (vectSku != null && vectSku.size() > 0) {
                for (int a = 0; a < vectSku.size(); a++) {
                    if (strSku.length() > 0) {
                        strSku = strSku + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    } else {
                        strSku = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    }
                }
                strSku = "(" + strSku + ")";
            }

            String strKondisi = " (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " >= MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] + ")"; // QTY , QTY_MAX

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (strPeriodeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPeriodeId;
                } else {
                    whereClause = whereClause + strPeriodeId;
                }
            }

            if (strSku.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSku;
                } else {
                    whereClause = whereClause + strSku;
                }
            }

            if (strKondisi.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strKondisi;
                } else {
                    whereClause = whereClause + strKondisi;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MaterialStock ms = new MaterialStock();
                Material mat = new Material();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                Unit unt = new Unit();

                ms.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                ms.setOpeningQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
                ms.setQtyIn(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN]));
                ms.setQtyOut(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT]));
                ms.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                ms.setQtyMax(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX]));
                temp.add(ms);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                temp.add(mat);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                result.add(temp);

            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**ary, gadnyana
     *
     * @param srcReportStock
     * @return
     */
    public static Vector getReportStockDate(SrcReportStock srcReportStock) {
        //DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //Cari periode aktif sesuai tanggal mulai
            long oidPeriode = PstPeriode.getByMonthYear(srcReportStock.getDateFrom());
            String sql = " SELECT " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + //  MATERIAL_UNIT_ID
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + //DEFAULT_PRICE
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + //DEFAULT_COST
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] + // OPENING_QTY
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    ", AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    ", AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE
                    " FROM ((((" + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" + // material_stock
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_UNIT_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstPeriode.TBL_STOCK_PERIODE + " PRD" + // periode
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + // PERIODE_ID
                    " = PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + // PERIODE_ID" +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_SELL_UNIT_ID
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID";

            String strLocationId = "";
            if (srcReportStock.getLocationId() != 0) {
                strLocationId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportStock.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportStock.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportStock.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportStock.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId();
            }

            String strPeriodeId = "";
            if (srcReportStock.getPeriodeId() != 0) {
                strPeriodeId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId();
            }

            Vector vectSku = LogicParser.textSentence(srcReportStock.getSku());
            for (int i = 0; i < vectSku.size(); i++) {
                String name = (String) vectSku.get(i);
                if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                    vectSku.remove(i);
            }

            String strSku = "";
            if (vectSku != null && vectSku.size() > 0) {
                for (int a = 0; a < vectSku.size(); a++) {
                    if (strSku.length() > 0) {
                        strSku = strSku + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    } else {
                        strSku = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    }
                }
                strSku = "(" + strSku + ")";
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (strPeriodeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPeriodeId;
                } else {
                    whereClause = whereClause + strPeriodeId;
                }
            }

            if (strSku.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSku;
                } else {
                    whereClause = whereClause + strSku;
                }
            }

            String strKlausa = "(PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + " = " + oidPeriode + // PERIODE_ID
                    " ) AND (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0" + // QTY
                    " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + " <> 0" + // QTY_IN
                    " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + " <> 0)"; // QTY_OUT
            if (whereClause.length() > 0) {
                whereClause += " AND " + strKlausa;
            } else {
                whereClause += strKlausa;
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                Material mat = new Material();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                Unit unt = new Unit();

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                temp.add(mat);

                Date asu = srcReportStock.getDateFrom();
                if (asu.getDate() != 1) {
                    temp.add("" + (rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]) + getOpeningQty(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom())));
                } else {
                    temp.add("" + rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
                }

                temp.add("" + getCountSale(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));
                temp.add("" + getCountReturn(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));
                temp.add("" + getCountDispatch(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));
                temp.add("" + getCountReceive(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                result.add(temp);

            }
            rs.close();
            stmt.close();
            PstMaterialStock.closeLocalConnection(koneksi);

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            //DBResultSet.close(dbrs);
        }
        return result;
    }

    /** ary, gadnyana
     * @param koneksi
     * @param oidMaterial
     * @param oidLocation
     * @param startDateX
     * @param endDateX
     * @return
     */
    public static double getCountSale(Connection koneksi, long oidMaterial, long oidLocation,
                                   Date startDateX, Date endDateX) {
        double result = 0;
        //DBResultSet dbrs = null;
        try {
            //Cari periode aktif sesuai tanggal mulai
            String startDate = Formater.formatDate(startDateX, "yyyy-MM-dd");
            String endDate = Formater.formatDate(endDateX, "yyyy-MM-dd");
            String sql = " SELECT SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SumOfQTY" + // QTY
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " = " + oidLocation +
                    " AND CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = " + oidMaterial +
                    " AND (CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE" +
                    " BETWEEN '" + startDate + "' AND '" + endDate + "')";
            
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                result = rs.getDouble("SumOfQTY");
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            //DBResultSet.close(dbrs);
        }
        return result;
    }

    /** ary, gadnyana
     * @param koneksi
     * @param oidMaterial
     * @param oidLocation
     * @param startDateX
     * @param endDateX
     * @return
     */
    public static double getCountReturn(Connection koneksi, long oidMaterial,
                                     long oidLocation, Date startDateX, Date endDateX) {
        double result = 0;
        //DBResultSet dbrs = null;
        try {
            //Cari periode aktif sesuai tanggal mulai
            String startDate = Formater.formatDate(startDateX, "yyyy-MM-dd");
            String endDate = Formater.formatDate(endDateX, "yyyy-MM-dd");
            String sql = " SELECT " +
                    " SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfQTY" + // QTY
                    " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" + //  return_material
                    " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" + // return_material_item
                    " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + // RETURN_MATERIAL_ID+
                    " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + // RETURN_MATERIAL_ID
                    " WHERE RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " = " + oidLocation +
                    " AND RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = " + oidMaterial +
                    " AND (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + // RETURN_DATE
                    " BETWEEN '" + startDate + "' AND '" + endDate + "')";
            
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                result = rs.getDouble("SumOfQTY");
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            //DBResultSet.close(dbrs);
        }
        return result;
    }

    /** ary, gadnyana
     * @param koneksi
     * @param oidMaterial
     * @param oidLocation
     * @param startDateX
     * @param endDateX
     * @return
     */
    public static double getCountDispatch(Connection koneksi, long oidMaterial,
                                       long oidLocation, Date startDateX, Date endDateX) {
        double result = 0;
        //DBResultSet dbrs = null;
        try {
            //Cari periode aktif sesuai tanggal mulai
            String startDate = Formater.formatDate(startDateX, "yyyy-MM-dd");
            String endDate = Formater.formatDate(endDateX, "yyyy-MM-dd");

            String sql = " SELECT " +
                    " SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SumOfQTY" + // QTY
                    " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" + // dispatch_material
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI " + // dispatch_material_item
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] + // DISPATCH_MATERIAL_ID+
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + // DISPATCH_MATERIAL_ID
                    " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + // LOCATION_ID
                    " = " + oidLocation +
                    " AND DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = " + oidMaterial +
                    " AND (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + // DISPATCH_DATE
                    " BETWEEN '" + startDate + "' AND '" + endDate + "')";
            
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                result = rs.getDouble("SumOfQTY");
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            //DBResultSet.close(dbrs);
        }
        return result;
    }

    public static double getCountReceive(Connection koneksi, long oidMaterial,
                                      long oidLocation, Date startDateX, Date endDateX) {
        double result = 0;
        //DBResultSet dbrs = null;
        try {
            //Cari periode aktif sesuai tanggal mulai
            String startDate = Formater.formatDate(startDateX, "yyyy-MM-dd");
            String endDate = Formater.formatDate(endDateX, "yyyy-MM-dd");
            String sql = " SELECT " +
                    " SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS CountOfQTY" + // QTY
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" + // receive_material
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" + // receive_material_item
                    " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + // RECEIVE_MATERIAL_ID" +
                    " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + // RECEIVE_MATERIAL_ID" +
                    " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + // LOCATION_ID
                    " = " + oidLocation +
                    " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " = " + oidMaterial +
                    " AND (REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + //RECEIVE_DATE
                    " BETWEEN '" + startDate + "' AND '" + endDate + "')";
            
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                result = rs.getDouble("CountOfQTY");
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            //DBResultSet.close(dbrs);
        }
        return result;
    }

    public static double getOpeningQty(Connection koneksi, long oidMaterial, long oidLocation, Date startDateX) {
        double result = 0;
        try {
            String sql = "";
            //Set tanggal satu
            Date tanggalSatu = new Date();
            tanggalSatu.setYear(startDateX.getYear());
            tanggalSatu.setMonth(startDateX.getMonth());
            tanggalSatu.setDate(1);
            //Set satu hari sebelum start
            Date tanggalSebelum = new Date();
            tanggalSebelum.setYear(startDateX.getYear());
            tanggalSebelum.setMonth(startDateX.getMonth());
            tanggalSebelum.setDate(startDateX.getDate() - 1);
            //Get Receive
            result += getCountReceive(koneksi, oidMaterial, oidLocation, tanggalSatu, tanggalSebelum);
            //Get Sold
            result = result - getCountSale(koneksi, oidMaterial, oidLocation, tanggalSatu, tanggalSebelum);
            //Get Return
            result = result - getCountReturn(koneksi, oidMaterial, oidLocation, tanggalSatu, tanggalSebelum);
            //Get Dispatch
            result = result - getCountDispatch(koneksi, oidMaterial, oidLocation, tanggalSatu, tanggalSebelum);

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        }
        return result;
    }

    public static Vector getReportStockDateSupplier(SrcReportStock srcReportStock) {
        //DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //Cari periode aktif sesuai tanggal mulai
            long oidPeriode = PstPeriode.getByMonthYear(srcReportStock.getDateFrom());
            String sql = " SELECT " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_UNIT_ID
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] + // OPENING_QTY
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME category
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME sub category
                    ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + // COMP_NAME
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE
                    " FROM (((((" + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" + // material_stock
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_UNIT_ID
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " ) INNER JOIN " + PstPeriode.TBL_STOCK_PERIODE + " PRD" + // periode
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + // PERIODE_ID
                    " = PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + // PERIODE_ID
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + //  SUB_CATEGORY_ID
                    " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + //  CONTACT_ID
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_SELL_UNIT_ID
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID;

            String strLocationId = "";
            if (srcReportStock.getLocationId() != 0) {
                strLocationId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportStock.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportStock.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportStock.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportStock.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId();
            }

            String strPeriodeId = "";
            if (srcReportStock.getPeriodeId() != 0) {
                strPeriodeId = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId();
            }

            Vector vectSku = LogicParser.textSentence(srcReportStock.getSku());
            for (int i = 0; i < vectSku.size(); i++) {
                String name = (String) vectSku.get(i);
                if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                    vectSku.remove(i);
            }

            String strSku = "";
            if (vectSku != null && vectSku.size() > 0) {
                for (int a = 0; a < vectSku.size(); a++) {
                    if (strSku.length() > 0) {
                        strSku = strSku + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    } else {
                        strSku = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vectSku.get(a) + "%')";
                    }
                }
                strSku = "(" + strSku + ")";
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (strPeriodeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPeriodeId;
                } else {
                    whereClause = whereClause + strPeriodeId;
                }
            }

            if (strSku.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSku;
                } else {
                    whereClause = whereClause + strSku;
                }
            }

            String strKlausa = "(PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + " = " + oidPeriode + // PERIODE_ID+
                    " ) AND (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0" + // QTY
                    " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] + " <> 0" + // QTY_IN
                    " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] + " <> 0)"; // QTY_OUT
            if (whereClause.length() > 0) {
                whereClause += " AND " + strKlausa;
            } else {
                whereClause += strKlausa;
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                Vector temp = new Vector();
                Material mat = new Material();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                ContactList cnt = new ContactList();
                Unit unt = new Unit();

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                temp.add(mat);

                Date asu = srcReportStock.getDateFrom();
                if (asu.getDate() != 1) {
                    temp.add("" + (rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]) + getOpeningQty(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom())));
                } else {
                    temp.add("" + rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
                }
                temp.add("" + getCountSale(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));
                temp.add("" + getCountReturn(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));
                temp.add("" + getCountDispatch(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));
                temp.add("" + getCountReceive(koneksi, rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]), srcReportStock.getLocationId(), srcReportStock.getDateFrom(), srcReportStock.getDateTo()));

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

                cnt.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                temp.add(cnt);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                result.add(temp);

            }
            rs.close();
            stmt.close();
            PstMaterialStock.closeLocalConnection(koneksi);

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            //DBResultSet.close(dbrs);
        }
        return result;
    }

    /** gadnyana
     * fungsi untuk mencari data stock - per kategori
     */
    public static Vector getReportStockPerKategori(SrcReportStock srcReportStock, int language,
                                                   boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    " ,U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB CATEGORY
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " AS SC_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // SUB CATEGORY NAME
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " =MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID

                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " =C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " =SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY " +
                    " C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            boolean first = false;
            long oidcategory = 0;

            // variable for total all item
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            // variable for total sub kategori
            double subTotalCost = 0;
            double subTotalPrice = 0;
            double subTotalQty = 0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontal());
                    list.add(header(language));
                    list.add(drawLineHorizontal());
                    baris = 3;
                    firstt = true;
                }
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                SubCategory subCategory = new SubCategory();
                Unit unit = new Unit();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                //material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                //Note : Karena ada dua field yg sama : NAME dr Material dan NAME dr Unit
                material.setName(rs.getString(2));

                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                subCategory.setOID(rs.getLong(PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]));
                subCategory.setName(rs.getString("SC_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));

                unit.setName(rs.getString(PstUnit.fieldNames[PstUnit.FLD_NAME]));

                // cek and create list
                if (!first) {

                    //*******PERINTAH INI AKAN DIGUNAKAN JIKA MEMAKAI SUB KATAGORY********
                    //list.add(headerSubTotal(subCategory.getName())); // for header sub category
                    //baris++;
                    //list.add(drawLineHorizontal()); // for last line page
                    ///baris++;*/

                    oidcategory = subCategory.getOID();
                    first = true;
                } else {
                    if (subCategory.getOID() != oidcategory) {
                        list.add(drawLineHorizontal()); // for last line page
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;
                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }
                        //******PERINTAH INI DIGINAKAN JIKA MEMAKAI SUB KATAGORY*******
                        //list.add(headerSubTotal(subTotalQty,subTotalCost,subTotalPrice)); // for total sub category
                        //baris++;

                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }

                        //list.add(drawLineTotal());
                        //baris++;

                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }

                        //list.add(headerSubTotal(subCategory.getName())); // for header sub category
                        //baris++;

                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }

                        //list.add(drawLineHorizontal()); // for last line page
                        //baris++;

                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }

                        oidcategory = subCategory.getOID();
                        subTotalQty = 0;
                        subTotalCost = 0;
                        subTotalPrice = 0;
                    }
                }

                list.add(drawItem(line, material, matStock, unit));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 4;
                }

                // total sub category
                subTotalQty = subTotalQty + matStock.getQty();
                subTotalCost = subTotalCost + (matStock.getQty() * material.getDefaultCost());
                subTotalPrice = subTotalPrice + (matStock.getQty() * material.getDefaultPrice());
                // total all item
                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {

                list.add(drawLineHorizontal()); // for last line page
                //PERINTAH INI DIGUNAKAN JIKA MEMAKAI SUB KATAGORY
                //list.add(headerSubTotal(subTotalQty,subTotalCost,subTotalPrice)); // for total sub category
                //list.add(drawLineTotal());
                list.add(headerTotal(totalQty, totalCost, totalPrice));
                list.add(drawLineTotal());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * fungsi untuk mencari data stock - per kategori
     * @created by Edhy
     * @update by gwawan@dimata 14/09/2007
     */
    public static Vector getReportStockPerKategori(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    //", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    //adding sum(QTY) pergroup material id by mirahu 20120314
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS SUM_QTY"+// QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT/
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    /*
                    ", SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+ // SUB CATEGORY
                    ", SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]+" AS SC_"+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]+ // SUB CATEGORY NAME
                    */
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // SELLING_PRICE
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    
                    ", C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] +" AS CAT_NAME "+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + // NAME
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT] + ") AS SUM_BERAT"+// QTY
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +

                    /*        " INNER JOIN "+PstSubCategory.TBL_SUB_CATEGORY+" AS SC "+
                    " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+
                    " = SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+*/
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " <> " + PstMaterial.DELETE+
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
                    //" AND MAT." + fieldNames[FLD_EDIT_MATERIAL] + "!='" + EDIT_NON_AKTIVE+"'"; add opie-eyek 20160428
            
            //add opie-eyek 20160811
            if(srcReportStock.getCategoryId()!=0){
                sql=sql+ " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId() ;
            }        
                                       
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }

            // new gadnyana
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportStock.getMerkId();
            }


            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            //sql = sql + " ORDER BY " +
              //adding group by 20120314 by mirahu
              sql = sql + " GROUP BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
                          
            //added by dewok 2018
            int cekTypeOfBusinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
            
            if(srcReportStock.getCategoryId()==0){
                if (cekTypeOfBusinessDetail == 2) {
                    sql += " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                                ", RIGHT(M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";                    
                } else {
                 sql=sql+" ORDER BY " +
                      " C." + PstCategory.fieldNames[PstCategory.FLD_NAME]+" ASC ";
                }
            }else{                
                if (cekTypeOfBusinessDetail == 2) {
                    sql += " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                                ", RIGHT(M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";  
                } else {
                    sql=sql+" ORDER BY " +
                          " C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                          ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                }
            }
                            
            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();
            
            System.out.println("SessReportStock.getReportStockKategori sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karen HPP sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));
                
                Material material = new Material();
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                // check data average * standard rate
                //StandartRate standartRate = PstStandartRate.getActiveStandardRate(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])); /** method ini tidak menghemat resource */
                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));// * standarRate);
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setBarCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                vectTemp.add(material);

                MaterialStock matStock = new MaterialStock();
                //matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                matStock.setQty(rs.getDouble("SUM_QTY"));
                matStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                matStock.setBerat(rs.getDouble("SUM_BERAT"));
                vectTemp.add(matStock);

                Category category = new Category();
                category.setOID(rs.getLong(PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]));
                category.setName(rs.getString("CAT_NAME"));
                vectTemp.add(category);

                Unit unit = new Unit();
                unit.setName(rs.getString(PstUnit.fieldNames[PstUnit.FLD_NAME]));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vectTemp.add(unit);

                result.add(vectTemp);
            }
        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockKategori : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
     * fungsi untuk mencari data stock - per kategori dengan nilai cost saja
     * dipakai di Tegalsari
     * @param srcReportStock
     * @param language
     * @param isZero
     * @param pgStart
     * @param pgNext
     * @return
     */
    public static Vector getReportStockPerKategoriWithCost(SrcReportStock srcReportStock, int language,
                                                           boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB CATEGORY
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " AS SC_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // SUB CATEGORY NAME
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " =MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " =C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " =SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY " +
                    " C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            boolean first = false;
            long oidcategory = 0;

            // variable for total all item
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            // variable for total sub kategori
            double subTotalCost = 0;
            double subTotalPrice = 0;
            double subTotalQty = 0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontalWithCost());
                    list.add(headerCost(language));
                    list.add(drawLineHorizontalWithCost());
                    baris = 3;
                    firstt = true;
                }
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                SubCategory subCategory = new SubCategory();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                subCategory.setOID(rs.getLong(PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]));
                subCategory.setName(rs.getString("SC_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));

                // cek and create list
                if (!first) {
                    list.add(headerSubTotalCost(subCategory.getName())); // for header sub category
                    baris++;
                    list.add(drawLineHorizontalWithCost()); // for last line page
                    baris++;
                    oidcategory = subCategory.getOID();
                    first = true;
                } else {
                    if (subCategory.getOID() != oidcategory) {
                        list.add(drawLineHorizontalWithCost()); // for last line page
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;
                            list.add(drawLineHorizontalWithCost()); // for last line page
                            list.add(drawLineHorizontalWithCost()); // for first line page
                            list.add(headerCost(language)); // for header list
                            list.add(drawLineHorizontalWithCost()); // for line limit item
                            baris = 3;
                        }
                        list.add(headerSubTotalCost(subTotalQty, subTotalCost, subTotalPrice)); // for total sub category
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontalWithCost()); // for last line page
                            list.add(drawLineHorizontalWithCost()); // for first line page
                            list.add(headerCost(language)); // for header list
                            list.add(drawLineHorizontalWithCost()); // for line limit item
                            baris = 3;
                        }
                        list.add(drawLineTotal());
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontalWithCost()); // for last line page
                            list.add(drawLineHorizontalWithCost()); // for first line page
                            list.add(headerCost(language)); // for header list
                            list.add(drawLineHorizontalWithCost()); // for line limit item
                            baris = 3;
                        }
                        list.add(headerSubTotalCost(subCategory.getName())); // for header sub category
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontalWithCost()); // for last line page
                            list.add(drawLineHorizontalWithCost()); // for first line page
                            list.add(headerCost(language)); // for header list
                            list.add(drawLineHorizontalWithCost()); // for line limit item
                            baris = 3;
                        }
                        list.add(drawLineHorizontalWithCost()); // for last line page
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontalWithCost()); // for last line page
                            list.add(drawLineHorizontalWithCost()); // for first line page
                            list.add(headerCost(language)); // for header list
                            list.add(drawLineHorizontalWithCost()); // for line limit item
                            baris = 3;
                        }

                        oidcategory = subCategory.getOID();
                        subTotalQty = 0;
                        subTotalCost = 0;
                        subTotalPrice = 0;
                    }
                }

                list.add(drawItemCost(line, material, matStock));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontalWithCost()); // for last line page
                    list.add(drawLineHorizontalWithCost()); // for first line page
                    list.add(headerCost(language)); // for header list
                    list.add(drawLineHorizontalWithCost()); // for line limit item
                    baris = 4;
                }

                // total sub category
                subTotalQty = subTotalQty + matStock.getQty();
                subTotalCost = subTotalCost + (matStock.getQty() * material.getDefaultCost());
                subTotalPrice = subTotalPrice + (matStock.getQty() * material.getDefaultPrice());
                // total all item
                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontalWithCost()); // for last line page
                list.add(headerSubTotalLastCost(subTotalQty, subTotalCost)); // for total sub category
                list.add(drawLineTotalCost());
                list.add(headerTotalCost(totalQty, totalCost));
                list.add(drawLineTotalCost());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    /** gadnyana
     * fungsi untuk mencari daftar data stock - per supplier
     */
    public static Vector getReportStockPerSupplier(SrcReportStock srcReportStock, int language,
                                                   boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    " ,U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " ,C." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " =MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID

                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //Unit
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +

                    " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS VC " + // MATERIAL-SUPLIER
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +


                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS C " + //SUPLIER
                    " ON VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = C." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +


                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CT " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " =CT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " =SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId() +
                    " AND VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + srcReportStock.getSupplierId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY " +
                    " CT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontal());
                    list.add(header(language));
                    list.add(drawLineHorizontal());
                    baris = 3;
                    firstt = true;
                }

                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                //material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                //Note : Digunakan angka karena ada dua field yang sama-> NAME dr material dan NAME dr Unit
                material.setName(rs.getString(2));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                unit.setName(rs.getString(PstUnit.fieldNames[PstUnit.FLD_NAME]));

                list.add(drawItem(line, material, matStock, unit));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 3;
                }

                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontal()); // for last line page
                list.add(headerTotal(totalQty, totalCost, totalPrice));
                list.add(drawLineTotal());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * fungsi untuk mencari daftar data stock - per supplier
     * @created by Edhy
     * @update by gwawan@dimata 14/09/2007
     */
    public static Vector getReportStockPerSupplier(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", C." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // SELLING_PRICE
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //Unit
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS VC " + // MATERIAL-SUPLIER
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS C " + //SUPLIER
                    " ON VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = C." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CT " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    /*
                    " INNER JOIN "+PstSubCategory.TBL_SUB_CATEGORY+" AS SC "+
                    " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+
                    " = SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+*/
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + srcReportStock.getSupplierId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " <> " + PstMaterial.DELETE;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            sql = sql + " ORDER BY " +
                    " CT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            /** get list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();
            
            //System.out.println("SessReportStock.getReportStockPerSupplier sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]);
                long buyUnitId = rs.getLong(8);
                long stockUnitId = rs.getLong(9);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karena hpp sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));// * standarRate);
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                vectTemp.add(material);

                MaterialStock matStock = new MaterialStock();
                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                vectTemp.add(matStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

        } catch (Exception e) {
            System.out.println("Exc SessReportStock.getReportStockPerSupplier : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

     /**
     * fungsi untuk mencari daftar data stock - per supplier - distinct
     * @update by Mirahu@dimata 13/04/2011
     */

    public static Vector getReportStockPerSupplierDistinct(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT DISTINCT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    //addd opie-eyek 28012013
                    //", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS SUM_QTY"+// QTY

                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", C." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // SELLING_PRICE
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + // NAME
                    
                    ", C." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //Unit
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS VC " + // MATERIAL-SUPLIER
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS C " + //SUPLIER
                    " ON VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = C." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CT " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    /*
                    " INNER JOIN "+PstSubCategory.TBL_SUB_CATEGORY+" AS SC "+
                    " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+
                    " = SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+*/
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId();
            
            if(srcReportStock.getSupplierId()!=0){
                sql = sql+" AND VC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + srcReportStock.getSupplierId();
            }        
            
            sql=sql+" AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " <> " + PstMaterial.DELETE;

            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

           /* sql = sql + " ORDER BY " +
                    " CT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];*/
            //add opie-eyek 28012013
            sql = sql + " GROUP BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            
            if(srcReportStock.getSupplierId()!=0){
                    sql = sql + " ORDER BY " +
                       " CT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                       ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }else{
                sql = sql + " ORDER BY " +
                       " C." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" ASC ";
            }
           

            /** get list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();

            //System.out.println("SessReportStock.getReportStockPerSupplier sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]);
                long buyUnitId = rs.getLong(8);
                long stockUnitId = rs.getLong(9);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karena hpp sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));// * standarRate);
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setBarCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                vectTemp.add(material);

                MaterialStock matStock = new MaterialStock();
                matStock.setQty(rs.getDouble("SUM_QTY"));
                vectTemp.add(matStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vectTemp.add(unit);
                
                
                ContactList contact = new ContactList();
                contact.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contact.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                vectTemp.add(contact);
                
                
                result.add(vectTemp);
            }

        } catch (Exception e) {
            System.out.println("Exc SessReportStock.getReportStockPerSupplier : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }



    /**
     * fungsi untuk mencari daftar data stock - per supplier dengan nilai cost saja
     * digunakan di Tegalsari
     * @param srcReportStock
     * @param language
     * @param isZero
     * @param pgStart
     * @param pgNext
     * @return
     */
    public static Vector getReportStockPerSupplierWithCost(SrcReportStock srcReportStock, int language,
                                                           boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " ,C." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " =MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    " = C." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CT " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " =CT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " =SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY " +
                    " CT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontalWithCost());
                    list.add(headerCost(language));
                    list.add(drawLineHorizontalWithCost());
                    baris = 3;
                    firstt = true;
                }

                Material material = new Material();
                MaterialStock matStock = new MaterialStock();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                list.add(drawItemCost(line, material, matStock));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 3;
                }

                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontalWithCost()); // for last line page
                list.add(headerTotalCost(totalQty, totalCost));
                list.add(drawLineHorizontalWithCost());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /** gadnyana
     * fungsi untuk mencari data stock - all
     */
    public static Vector getReportStockAll(SrcReportStock srcReportStock, int language,
                                           boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    " ,U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " =MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " =C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " =SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY " +
                    " C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            double qtyPerStockUnit = 0;

            boolean firstt = false;
            boolean bool = true;

            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontal());
                    list.add(header(language));
                    list.add(drawLineHorizontal());
                    baris = 3;
                    firstt = true;
                }
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                //material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                // Note : digunakan angka karena ada 2 field yang sama : NAME dr material dan NAME dr Unit
                material.setName(rs.getString(2));

                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));

                //System.out.println("qtyPerStockUnit on All");
                qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(material.getBuyUnitId(), material.getDefaultStockUnitId());

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                unit.setName(rs.getString(PstUnit.fieldNames[PstUnit.FLD_NAME]));

                list.add(drawItem(line, material, matStock, unit));
                baris++;
                line++;


                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 3;
                }

                totalQty = totalQty + matStock.getQty();
                //totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalCost = totalCost + (matStock.getQty() * (material.getDefaultCost() / qtyPerStockUnit));
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontal()); // for last line page
                list.add(headerTotal(totalQty, totalCost, totalPrice));
                list.add(drawLineTotal());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    /**
     * fungsi untuk mencari data stock - all
     * note : material cost = cost per unit stock
     * @created by Edhy
     * @updated by gwawan@dimata 14/9/2007
     */
    public static Vector getReportStockAll(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +

                    /*
                    " INNER JOIN "+PstSubCategory.TBL_SUB_CATEGORY+" AS SC "+
                    " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+
                    " = SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+
                    */

                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                " = " + srcReportStock.getMerkId();
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            sql = sql + " ORDER BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            /*
            " C."+PstCategory.fieldNames[PstCategory.FLD_CODE]+
            ", SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]+
            ", M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
             */
            
            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(5);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karena hpp sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(1));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(6));
                material.setAveragePrice(rs.getDouble(9));// * standarRate);
                material.setOID(rs.getLong(10));
                vectTemp.add(material);

                MaterialStock materialStock = new MaterialStock();
                materialStock.setQty(rs.getDouble(3));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                vectTemp.add(materialStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(11));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockAll(#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    /**
     * Fungsi ini digunakan untuk mendapatkan list stok
     * @param SrcreportStock Objek untuk melakukan pencarian
     * @param boolean Kondisi untuk menentukkan apakah proses kalkulasi melibatkan stok bernilai nol
     * @param int Start list
     * @param int Banyaknya list yang harus ditampilkan
     * @return Vector yang menampung instance dari class Material, MaterialStock dan Unit
     * @create by gwawan@dimata 3 Jan 2008
     * @updated by gwawan@dimata 17 Jan 2008
     */
    public static Vector getReportStockAll(SrcReportStock srcReportStock, boolean isZero, int limitStart, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    (isZero ? ", 0 AS " : ", MS.") + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY

                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    (isZero ? (", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ " AS " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]) :
                        (", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID])) + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    (isZero ?  (",0 AS "+ PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] ): ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]) + // LOCATION
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + // SELLING_PRICE
                    " FROM " + (isZero ? (PstMaterial.TBL_MATERIAL + " AS M " ) : ( PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + //  MATERIAL_STOCK
                    (!isZero ? (" INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] ) : " " ) + // MATERIAL_STOCK_ID
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    
                    " LEFT JOIN " + PstKsg.TBL_MAT_KSG + " AS G " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                    " = G." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] +
                    
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if( !isZero &&  (srcReportStock.getLocationId() != 0)) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }
            
            if(!isZero && srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }
            
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }
            
            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }
            
            if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }
//
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }
            
            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            sql = sql + " ORDER BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
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
                    ;
            }
            
            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();

            System.out.println("SessReportStock.getReportStockAll(#,#,#,#) : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
             
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(5);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karen HPP sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(1));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(6));
                material.setBuyUnitId(rs.getLong(7));
                material.setDefaultStockUnitId(rs.getLong(8));
                material.setAveragePrice(rs.getDouble(9));// * standarRate);
                material.setOID(rs.getLong(10));
                material.setGondolaName(rs.getString(14));
                vectTemp.add(material); 

                MaterialStock materialStock = new MaterialStock();
                materialStock.setQty(rs.getDouble(3));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                vectTemp.add(materialStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(11));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    public static Vector getReportStockAll2(SrcReportStock srcReportStock, boolean isZero, int limitStart, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    (isZero ? ", 0 AS " : ", MS.") + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY

                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    (isZero ? (", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ " AS " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]) :
                        (", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID])) + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    (isZero ?  (",0 AS "+ PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] ): ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]) + // LOCATION
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + // SELLING_PRICE
                    ", M."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+"" +
                    " FROM " + (isZero ? (PstMaterial.TBL_MATERIAL + " AS M " ) : ( PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + //  MATERIAL_STOCK
                    (!isZero ? (" INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] ) : " " ) + // MATERIAL_STOCK_ID
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    
                    " LEFT JOIN " + PstKsg.TBL_MAT_KSG + " AS G " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                    " = G." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] +
                    
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if( !isZero &&  (srcReportStock.getLocationId() != 0)) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }
            
            if(!isZero && srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }
            
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }
            
            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }
            
            if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }
//
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }
            
            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            sql = sql + " ORDER BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
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
                    ;
            }
            
            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();

            System.out.println("SessReportStock.getReportStockAll(#,#,#,#) : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
             
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(5);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karen HPP sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(1));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(6));
                material.setBuyUnitId(rs.getLong(7));
                material.setDefaultStockUnitId(rs.getLong(8));
                material.setAveragePrice(rs.getDouble(9));// * standarRate);
                material.setOID(rs.getLong(10));
                material.setGondolaName(rs.getString(14));
                material.setBarcode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                vectTemp.add(material); 

                MaterialStock materialStock = new MaterialStock();
                materialStock.setQty(rs.getDouble(3));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                vectTemp.add(materialStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(11));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    //Search Katalog di transfer berdasarkan barcode
    public static Vector getReportStockAllBarcode(SrcReportStock srcReportStock, boolean isZero, int limitStart, int recordToGet, String whereClause) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    (isZero ? ", 0 AS " : ", MS.") + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY

                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    (isZero ? (", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ " AS " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]) :
                        (", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID])) + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    (isZero ?  (",0 AS "+ PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] ): ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]) + // LOCATION
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // category
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_COLOR_ID] + // color
                    " FROM " + (isZero ? (PstMaterial.TBL_MATERIAL + " AS M " ) : ( PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + //  MATERIAL_STOCK
                    (!isZero ? (" INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] ) : " " ) + // MATERIAL_STOCK_ID
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +

                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE+
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != 4"
                    + whereClause
                    + "";

            if( !isZero &&  (srcReportStock.getLocationId() != 0)) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            if(!isZero && srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }

            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }

//            if(srcReportStock.getCategoryId()!=0){
//                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
//            }
            //add opie-eyek 20140416
             if (srcReportStock.getCategoryId() > 0) {
                  String strGroup = " AND ( M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcReportStock.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,srcReportStock.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                sql = sql + strGroup;
            } 

            if(srcReportStock.getSku().length() != 0) {
                sql += " AND (M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'" +
                     "OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " like '%" + srcReportStock.getSku() + "%')";
            }
//
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }

            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            sql = sql + " ORDER BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

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
                    ;
            }

            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();

            System.out.println("SessReportStock.getReportStockAll(#,#,#,#) : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(5);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karen HPP sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(1));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(6));
                material.setBuyUnitId(rs.getLong(7));
                material.setDefaultStockUnitId(rs.getLong(8));
                material.setAveragePrice(rs.getDouble(9));// * standarRate);
                material.setOID(rs.getLong(10));
                material.setGondolaCode(rs.getLong(14));
                material.setCategoryId(rs.getLong(15));
                material.setPosColor(rs.getLong(16));
                vectTemp.add(material);

                MaterialStock materialStock = new MaterialStock();
                materialStock.setQty(rs.getDouble(3));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                vectTemp.add(materialStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(11));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    
    public static Vector getReportStockAllSummary(SrcReportStock srcReportStock, boolean isZero, int limitStart, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") "+ // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + // LOCATION
                    ", G." + PstKsg.fieldNames[PstKsg.FLD_NAME]+" AS GONDOLA_NAME "+ // SELLING_PRICE
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    
                    " LEFT JOIN " + PstKsg.TBL_MAT_KSG + " AS G " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                    " = G." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] +
                    
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }
            
            if(srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }
            
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }
            
            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }
            
            if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }
            
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }
            
            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            //yg length name < 2 tidak ditampilkan
            // by mirahu
            //20110822
             sql = sql + " AND LENGTH(M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " )>2";
           
            sql = sql + " GROUP BY "+" M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" ORDER BY ";

            //update opie-eyek 25012013
            if(srcReportStock.getSortBy() == 0) { //SKU
                sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }else if(srcReportStock.getSortBy() == 1){ //NAMa
                sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
            }else if(srcReportStock.getSortBy() == 2){ //QTY
                sql=sql+" MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +" DESC";
            }else if(srcReportStock.getSortBy() == 3) { //SATUAN
                sql=sql+" U." + PstUnit.fieldNames[PstUnit.FLD_NAME];
            }else if(srcReportStock.getSortBy() == 4){ //NILAI STOCK
                 sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +" DESC";
            }else if(srcReportStock.getSortBy() == 5){ //CATEGORY
                 sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +" ASC";
            }else{
               sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            
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
                    ;
            }
            
            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();

            System.out.println("SessReportStock.getReportStockAll(#,#,#,#) : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(5);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karen HPP sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(1));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(6));
                material.setBuyUnitId(rs.getLong(7));
                material.setDefaultStockUnitId(rs.getLong(8));
                material.setAveragePrice(rs.getDouble(9));// * standarRate);
                material.setOID(rs.getLong(10));
                material.setGondolaName(rs.getString(14));
                vectTemp.add(material); 

                MaterialStock materialStock = new MaterialStock();
                materialStock.setQty(rs.getDouble(3));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                vectTemp.add(materialStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(11));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    
    public static Vector getReportStockAllSummary2(SrcReportStock srcReportStock, boolean isZero, int limitStart, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") "+ // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] + // BUYING_UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // SELLING_PRICE
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // SELLING_PRICE
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // QTY
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //UNIT
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // DEFAULT_COST_CURRENCY_ID
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + // LOCATION
                    ", G." + PstKsg.fieldNames[PstKsg.FLD_NAME]+" AS GONDOLA_NAME "+ // SELLING_PRICE
                    ", M."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+" " + //BARCODE
                    ", SUM(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT] + ") AS "+ PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT] +// BERAT
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    
                    " LEFT JOIN " + PstKsg.TBL_MAT_KSG + " AS G " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                    " = G." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] +
                    
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            //added by dewok 2018-12-31 item non aktif tidak di tampilkan
            sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }
            
            if(srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }
            
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }
            
            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }
            
            if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }
            
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }
            
            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND G."  + PstKsg.fieldNames[PstKsg.FLD_CODE] + " = '" + srcReportStock.getKsg() + "'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            
            //yg length name < 2 tidak ditampilkan
            // by mirahu
            //20110822
            //tapi untuk litama nama boleh kosong, jadi coding ini saya non aktifkan ya...
            //edited by dewok 2018
             //sql = sql + " AND LENGTH(M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " )>2";
           
            sql = sql + " GROUP BY "+" M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" ORDER BY ";

            //update opie-eyek 25012013
            if(srcReportStock.getSortBy() == 0) { //SKU
                //added BY dewok 2018 FOR litama
                int cekTypeOfBusinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
                if (cekTypeOfBusinessDetail == 2) {
                    sql+=" RIGHT(M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
                } else {
                    sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                }
            }else if(srcReportStock.getSortBy() == 1){ //NAMa
                sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
            }else if(srcReportStock.getSortBy() == 2){ //QTY
                sql=sql+" MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +" DESC";
            }else if(srcReportStock.getSortBy() == 3) { //SATUAN
                sql=sql+" U." + PstUnit.fieldNames[PstUnit.FLD_NAME];
            }else if(srcReportStock.getSortBy() == 4){ //NILAI STOCK
                 sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +" DESC";
            }else if(srcReportStock.getSortBy() == 5){ //CATEGORY
                 sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +" ASC";
            }else{
               sql=sql+" M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            
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
                    ;
            }
            
            /** mendapatkan list standar rate */
            //PstStandartRate pstStandartRate = new PstStandartRate();
            //Hashtable hashStandarRate = pstStandartRate.getStandartRate();

            System.out.println("SessReportStock.getReportStockAll(#,#,#,#) : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);

                // calculate cost per stock unit id
                double costPerBuyUnit = rs.getDouble(5);
                long buyUnitId = rs.getLong(7);
                long stockUnitId = rs.getLong(8);
                double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
                double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;
                //[karen HPP sudah dalam mata uang default] double standarRate = Double.parseDouble((String)hashStandarRate.get(""+rs.getLong("M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID])));

                Material material = new Material();
                material.setSku(rs.getString(1));
                material.setName(rs.getString(2));
                material.setDefaultCost(costPerStockUnit);
                material.setDefaultPrice(rs.getDouble(6));
                material.setBuyUnitId(rs.getLong(7));
                material.setDefaultStockUnitId(rs.getLong(8));
                material.setAveragePrice(rs.getDouble(9));// * standarRate);
                material.setOID(rs.getLong(10));
                material.setGondolaName(rs.getString(14));
                material.setBarcode(rs.getString(15));
                vectTemp.add(material); 

                MaterialStock materialStock = new MaterialStock();
                materialStock.setQty(rs.getDouble(3));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                materialStock.setMaterialUnitId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]));
                materialStock.setBerat(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT]));
                vectTemp.add(materialStock);

                Unit unit = new Unit();
                unit.setName(rs.getString(4));
                unit.setCode(rs.getString(11));
                vectTemp.add(unit);

                result.add(vectTemp);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
     * generate stockall only cost for Tegalsari
     */
    public static Vector getReportStockAllWithCost(SrcReportStock srcReportStock, int language,
                                                   boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();


            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            double qtyPerBaseUnit = 0;

            boolean firstt = false;
            boolean bool = true;

            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontalWithCost());
                    list.add(headerCost(language));
                    list.add(drawLineHorizontalWithCost());
                    baris = 3;
                    firstt = true;
                }

                Material material = new Material();
                MaterialStock matStock = new MaterialStock();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                list.add(drawItemCost(line, material, matStock));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontalWithCost()); // for last line page
                    list.add(drawLineHorizontalWithCost()); // for first line page
                    list.add(headerCost(language)); // for header list
                    list.add(drawLineHorizontalWithCost()); // for line limit item
                    baris = 3;
                }

                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">No data available</div></TD></TR>");
            } else {
                list.add(drawLineHorizontalWithCost()); // for last line page
                list.add(headerTotalCost(totalQty, totalCost));
                list.add(drawLineTotalCost());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * used to get stock report on store all
     * @param <CODE>srcReportStock</CODE>object that store searching parameter
     * @param <CODE>language</CODE>language that use for report
     * @param <CODE>isZero</CODE>flag that indicate zero value will display or not (TRUE if displayed otherwise FALSE)
     * @param <CODE>pgStart</CODE>maximum number of line that can write with data on the first page
     * @param <CODE>pgNext</CODE>maximum number of line that can write with data on the next of first page
     * @return
     * @created by Edhy
     */
    public static Vector getReportStockStoreAll(SrcReportStock srcReportStock, int language, boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CAT " + // CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SCAT " + // SUB CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + srcReportStock.getLocationId();


            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontal());
                    list.add(header(language));
                    list.add(drawLineHorizontal());
                    baris = 3;
                    firstt = true;
                }
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                list.add(drawItem(line, material, matStock, unit));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 3;
                }

                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontal()); // for last line page
                list.add(headerTotal(totalQty, totalCost, totalPrice));
                list.add(drawLineTotal());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * used to get data for report stock on store per supplier
     * @param <CODE>srcReportStock</CODE>object used to store search parameter
     * @param <CODE>language</CODE>report's language choosed
     * @param <CODE>isZero</CODE>flag indicate if zero value displayed or not (TRUE if displayed otherwise FALSE)
     * @param <CODE>pgStart</CODE>maximum line can write data on the first page
     * @param <CODE>pgNext</CODE>maximum line can write data on the next first page
     * @return
     * @created by Edhy
     */
    public static Vector getReportStockStorePerSupplier(SrcReportStock srcReportStock, int language, boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + // QTY
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ", C." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + // MATERIAL_STOCK_ID
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CAT " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SCAT " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    " = C." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontal());
                    list.add(header(language));
                    list.add(drawLineHorizontal());
                    baris = 3;
                    firstt = true;
                }

                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                list.add(drawItem(line, material, matStock, unit));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 3;
                }

                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }
            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontal()); // for last line page
                list.add(headerTotal(totalQty, totalCost, totalPrice));
                list.add(drawLineTotal());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    /**
     * used to get data for report stock on store per category
     * @param <CODE>srcReportStock</CODE>object that store search parameter
     * @param <CODE>language</CODE>report's language choosed
     * @param <CODE>isZero</CODE>flag indicate if zero value displayed or not (TRUE if displayed otherwise FALSE)
     * @param <CODE>pgStart</CODE>maximum line can write data on the first page of report
     * @param <CODE>pgNext</CODE>maximum line can write data on the next of first page of report
     * @return
     * @created by Edhy
     */
    public static Vector getReportStockStorePerCategory(SrcReportStock srcReportStock, int language, boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    ", SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    ", SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " AS SC_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + srcReportStock.getLocationId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = " + srcReportStock.getCategoryId();

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            sql = sql + " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;
            boolean first = false;
            long oidcategory = 0;

            // variable for total all item
            double totalCost = 0;
            double totalPrice = 0;
            double totalQty = 0;

            // variable for total sub kategori
            double subTotalCost = 0;
            double subTotalPrice = 0;
            double subTotalQty = 0;

            // create list first
            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            while (rs.next()) {
                bool = false;
                if (!firstt) {
                    list.add(drawLineHorizontal());
                    list.add(header(language));
                    list.add(drawLineHorizontal());
                    baris = 3;
                    firstt = true;
                }
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                SubCategory subCategory = new SubCategory();
                Unit unit = new Unit();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));

                subCategory.setOID(rs.getLong(PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]));
                subCategory.setName(rs.getString("SC_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));

                // cek and create list
                if (!first) {
                    list.add(headerSubTotal(subCategory.getName())); // for header sub category
                    baris++;
                    list.add(drawLineHorizontal()); // for last line page
                    baris++;
                    oidcategory = subCategory.getOID();
                    first = true;
                } else {
                    if (subCategory.getOID() != oidcategory) {
                        list.add(drawLineHorizontal()); // for last line page
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;
                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }
                        list.add(headerSubTotal(subTotalQty, subTotalCost, subTotalPrice)); // for total sub category
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }
                        list.add(drawLineTotal());
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }
                        list.add(headerSubTotal(subCategory.getName())); // for header sub category
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }
                        list.add(drawLineHorizontal()); // for last line page
                        baris++;
                        if (baris == maxlines) { // cek fix page is true , create new page and new header
                            if (boolmaxlines) {
                                maxlines = pgNext;
                            }
                            boolmaxlines = false;

                            list.add(drawLineHorizontal()); // for last line page
                            list.add(drawLineHorizontal()); // for first line page
                            list.add(header(language)); // for header list
                            list.add(drawLineHorizontal()); // for line limit item
                            baris = 3;
                        }

                        oidcategory = subCategory.getOID();
                        subTotalQty = 0;
                        subTotalCost = 0;
                        subTotalPrice = 0;
                    }
                }

                list.add(drawItem(line, material, matStock, unit));
                baris++;
                line++;

                if (baris == maxlines) {
                    if (boolmaxlines) {
                        maxlines = pgNext;
                    }
                    boolmaxlines = false;

                    list.add(drawLineHorizontal()); // for last line page
                    list.add(drawLineHorizontal()); // for first line page
                    list.add(header(language)); // for header list
                    list.add(drawLineHorizontal()); // for line limit item
                    baris = 4;
                }

                // total sub category
                subTotalQty = subTotalQty + matStock.getQty();
                subTotalCost = subTotalCost + (matStock.getQty() * material.getDefaultCost());
                subTotalPrice = subTotalPrice + (matStock.getQty() * material.getDefaultPrice());
                // total all item
                totalQty = totalQty + matStock.getQty();
                totalCost = totalCost + (matStock.getQty() * material.getDefaultCost());
                totalPrice = totalPrice + (matStock.getQty() * material.getDefaultPrice());
            }

            if (bool) {
                list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
            } else {
                list.add(drawLineHorizontal()); // for last line page
                list.add(headerSubTotal(subTotalQty, subTotalCost, subTotalPrice)); // for total sub category
                list.add(drawLineTotal());
                list.add(headerTotal(totalQty, totalCost, totalPrice));
                list.add(drawLineTotal());
            }
            list.add("</table>");
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static String headerSubTotal(String subName) {
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"4\">SUB KATEGORI</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"13\">: " + subName + "</TD>";
        str = str + "<TD class=\"listgensell\" ><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    public static String headerSubTotalCost(String subName) {
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"4\">SUB KATEGORI</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"7\">: " + subName + "</TD>";
        str = str + "<TD class=\"listgensell\" ><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    public static String headerSubTotal(double totalQty, double totalCost, double totalPrice) {
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        //NO
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        //SKU
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        //NAME
        str = str + "<TD class=\"listgensell\"><div align=\"right\">SUB TOTAL</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //QTY
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalQty) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        //SATUAN
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        //HARGA BELI
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //TOTAL BELI
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //HARGA JUAL
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //TOTAL JUAL
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalPrice) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    public static String headerSubTotalCost(double totalQty, double totalCost, double totalPrice) {
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">SUB TOTAL</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalQty) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        return str;
    }

    public static String headerSubTotalLastCost(double totalQty, double totalCost) {
        String str = "";
        str = "<TR><TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">SUB TOTAL</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalQty) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        return str;
    }

    public static String header(int language) {
        String str = "";
        str = "<TR><TD class=\"listgensell\" width=\"1%\">|</TD>";
        // No
        str = str + "<TD class=\"listgensell\" width=\"3%\"><div align=\"center\">" + textListMaterialHeader[language][0] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        //SKU
        str = str + "<TD class=\"listgensell\" width=\"11%\"><div align=\"center\">" + textListMaterialHeader[language][1] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        //NAMA BARANG
        str = str + "<TD class=\"listgensell\" width=\"23%\"><div align=\"center\">" + textListMaterialHeader[language][2] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        //QTY
        str = str + "<TD class=\"listgensell\" width=\"5%\"><div align=\"center\">" + textListMaterialHeader[language][3] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";

        //SATUAN
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListMaterialHeader[language][4] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";

        //HRG BELI
        str = str + "<TD class=\"listgensell\" width=\"8%\"><div align=\"center\">" + textListMaterialHeader[language][5] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        //TOTAL BELI
        str = str + "<TD class=\"listgensell\" width=\"10%\"><div align=\"center\">" + textListMaterialHeader[language][6] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        //HRG JUAL
        str = str + "<TD class=\"listgensell\" width=\"8%\"><div align=\"center\">" + textListMaterialHeader[language][7] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        //TOTAL JUAL
        str = str + "<TD class=\"listgensell\" width=\"10%\"><div align=\"center\">" + textListMaterialHeader[language][8] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    /**
     * for stock only with Cost for Tgalsari
     */
    public static String headerCost(int language) {
        String str = "";
        str = "<TR><TD class=\"listgensell\" width=\"1%\">|</TD>";
        str = str + "<TD class=\"listgensell\" width=\"3%\"><div align=\"center\">" + textListMaterialHeader[language][0] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"11%\"><div align=\"center\">" + textListMaterialHeader[language][1] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"30%\"><div align=\"center\">" + textListMaterialHeader[language][2] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"5%\"><div align=\"center\">" + textListMaterialHeader[language][3] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"8%\"><div align=\"center\">" + textListMaterialHeader[language][4] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"10%\"><div align=\"center\">" + textListMaterialHeader[language][5] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        return str;
    }

    public static String posisiStockHeader(int language) {
        String str = "";

        str = "<TR><TD class=\"listgensell\" width=\"1%\">|</TD>";
        str = str + "<TD class=\"listgensell\" width=\"3%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"11%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"25%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"8%\"></div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"5%\"></div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"45%\" colspan=\"13\"><div align=\"center\">" + textListPosisiStockHeader[language][5] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"5%\"></div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD></TR>";

        str = str + "<TR><TD class=\"listgensell\" width=\"1%\">|</TD>";
        str = str + "<TD class=\"listgensell\" width=\"3%\"><div align=\"center\">" + textListPosisiStockHeader[language][0] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"11%\"><div align=\"center\">" + textListPosisiStockHeader[language][1] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"25%\"><div align=\"center\">" + textListPosisiStockHeader[language][2] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"8%\"><div align=\"center\">" + textListPosisiStockHeader[language][3] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"5%\"><div align=\"center\">" + textListPosisiStockHeader[language][4] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"45%\" colspan=\"13\" >------------------------------------------------------------------------------------</TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"8%\"><div align=\"center\">" + textListPosisiStockHeader[language][12] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD></TR>";

        str = str + "<TR><TD class=\"listgensell\" width=\"1%\">|</TD>";
        str = str + "<TD class=\"listgensell\" width=\"3%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"11%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"25%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"8%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"5%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][6] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][11] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][7] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][8] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][9] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][13] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"6%\"><div align=\"center\">" + textListPosisiStockHeader[language][10] + "</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\" width=\"8%\"></TD>";
        str = str + "<TD class=\"listgensell\" width=\"1%\"><div align=\"center\">|</div></TD></TR>";

        return str;
    }

    public static String headerTotal(double totalQty, double totalCost, double totalPrice) {
        String str = "";
        str = "<TR><TD class=\"listgensell\"></TD>";
        //kolom NO
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        //kolom SKU
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        //Kolom NAMA BARANG
        str = str + "<TD class=\"listgensell\"><div align=\"right\">TOTAL </div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //Kolom QTY
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalQty) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        //Kolom SATUAN
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        //Kolom HRG BELI
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //Kolom TOTAL BELI
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //Kolom HARGA JUAL
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //Kolom TOTAL JUAL
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalPrice) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    /**
     * header Total for tegalsari
     */
    public static String headerTotalCost(double totalQty, double totalCost) {
        String str = "";
        str = "<TR><TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">TOTAL </div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalQty) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        return str;
    }

    public static final String textListMaterialHeader[][] = {
        {"NO", "SKU", "NAMA BARANG", "QTY", "STN", "HRG BELI", "TOTAL BELI", "HRG JUAL", "TOTAL JUAL"},
        {"NO", "CODE", "NAME", "QTY", "UNIT", "COST", "TOTAL COST", "SELL", "TOTAL SELL"}
    };

    public static final String textListPosisiStockHeader[][] = {
        {"NO", "SKU", "NAMA BARANG", "HPP", "UNIT", "QUANTITY", "AWAL", "TERIMA", "TRANSFER", "RETURN", "SALDO", "OPNAME", "NILAI STOK","JUAL"},
        {"NO", "CODE", "NAME", "AVERAGE PRICE", "UNIT", "QUANTITY", "BEGINNING", "RECEIVE", "DISPATCH", "RET.& SALE", "ENDING", "OPNAME", "VALUE STOCK","SALE"}
    };

    public static String drawLineHorizontal() {
        String str = "";
        str = "<TR class=\"listgensell\"><TD>-</TD>";
        //Untuk kolom No
        str = str + "<TD>-----</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Untuk kolom SKU
        str = str + "<TD>---------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Untuk kolom nama barang
        str = str + "<TD>----------------------------------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Untuk kolom Qty
        str = str + "<TD>--------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";

        //Kolom SATUAN
        str = str + "<TD>----------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";

        //Untuk kolom HRG Beli
        str = str + "<TD>----------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Untuk kolom Total beli
        str = str + "<TD>--------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Untuk kolom HRG Jual
        str = str + "<TD>----------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Untuk kolom Total jual
        str = str + "<TD>--------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD></TR>";
        return str;
    }

    /**
     * drawHorizontal for taglsari
     */
    public static String drawLineHorizontalWithCost() {
        String str = "";
        str = "<TR class=\"listgensell\"><TD>-</TD>";
        str = str + "<TD>-------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>---------------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------------------------------------------------------------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        return str;
    }

    public static String drawPosisiStockLineHorizontal() {
        String str = "";
        str = "<TR class=\"listgensell\"><TD>-</TD>";
        str = str + "<TD>---</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>----------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>---------------------------------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-----</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>---------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD></TR>";
        return str;
    }

    public static String drawPosisiStockLineTotalHorizontal() {
        String str = "";
        str = "<TR class=\"listgensell\"><TD>|</TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD></TR>";
        return str;
    }

    public static String drawPosisiStockLineTotalHorizontal(String strX) {
        String str = "";
        str = "<TR class=\"listgensell\"><TD>" + strX + "</TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\"></div></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>---------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>---------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-----------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD></TR>";
        return str;
    }

    public static String drawPosisiStockHeaderTotal(String hdNm, String nm) {
        String str = "";
        str = "<TR class=\"listgensell\"><TD>|</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"3\">" + hdNm.toUpperCase() + "</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"16\">: " + nm + "</TD>";
        str = str + "<TD><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    public static String drawPosisiStockTotal(String hdNm, double qtyAwal, double qtyOpname, double qtyReceive, double qtyKeluar, double qtyRetur, double qtyAkhir) {
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"9\"><div align=\"right\">" + hdNm.toUpperCase() + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyAwal) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyOpname) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyReceive) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyKeluar) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyRetur) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyAkhir) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    public static String drawPosisiStockTotal(String awl, String hdNm, double qtyAwal, double qtyOpname,
                                              double qtyReceive, double qtyKeluar, double qtyRetur, double qtyAkhir,
                                              double totalStock, double qtySale) {
        String str = "";
        str = "<TR><TD class=\"listgensell\">" + awl + "</TD>";
        str = str + "<TD class=\"listgensell\" colspan=\"9\"><div align=\"right\">" + hdNm.toUpperCase() + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyAwal) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyOpname) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyReceive) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyKeluar) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyRetur) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtySale) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyAkhir) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalStock) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    public static String drawLineTotal() {
        Vector rowx = new Vector();
        String str = "";
        str = "<TR class=\"listgensell\"><TD></TD>";
        //Kolom No
        str = str + "<TD></TD>";
        str = str + "<TD></TD>";
        //Kolom SKU
        str = str + "<TD></TD>";
        str = str + "<TD></TD>";
        //Kolom NAMA
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Kolom QTY
        str = str + "<TD>-------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";

        //Kolom SATUAN
        str = str + "<TD>----------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";

        //Kolom HARGA BELI
        str = str + "<TD>--------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Kolom TOTAL BELI
        str = str + "<TD>------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Kolom HARGA JUAL
        str = str + "<TD>--------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        //Kolom TOTAL JUAL
        str = str + "<TD>------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD></TR>";
        return str;
    }

    /**
     * For Tegalsari
     */
    public static String drawLineTotalCost() {
        Vector rowx = new Vector();
        String str = "";
        str = "<TR class=\"listgensell\"><TD></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD></TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>-------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        str = str + "<TD>--------------------------</TD>";
        str = str + "<TD><div align=\"center\">-</div></TD>";
        return str;
    }

    public static String drawItem(int number, Material mat, MaterialStock ms, Unit unt) {

        //System.out.println("---> start method drawItem");
        //System.out.println("qtyPerStockUnit on drawItem");
        double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());

        String nama_barang = mat.getName();
        //try{
        if (nama_barang.length() >= 34) {
            nama_barang = nama_barang.substring(0, 34);
        }
        /*
        }catch(Exception e)
        {
            System.out.println("Exce nama_barang : "+e.toString());
        }
         */
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        //NO
        str = str + "<TD class=\"listgensell\">" + number + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //SKU
        str = str + "<TD class=\"listgensell\"><div align=\"center\">" + mat.getSku() + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //NAMA
        str = str + "<TD class=\"listgensell\">" + nama_barang + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //QTY
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQty()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        //SATUAN
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + unt.getName() + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        //HARGA BELI
        //double buyingPrice = mat.getDefaultCost();
        double buyingPricePerStockUnit = mat.getDefaultCost() / qtyPerBaseUnit;
        double buyingPrice = mat.getDefaultCost() / qtyPerBaseUnit;
        //System.out.println("buyingPrice : " + buyingPrice);
        //System.out.println("buyingPricePerStockUnit : " + buyingPricePerStockUnit);
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getDefaultCost()) + "</div></TD>";
        //str = str + "<TD class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(buyingPrice)+"</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //TOTAL BELI
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQty() * buyingPrice) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //HARGA JUAL
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getDefaultPrice()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //TOTAL JUAL
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQty() * mat.getDefaultPrice()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        //System.out.println("---> end method drawItem");
        return str;
    }

    /**
     * DrawItem Stock for Tegalsari
     */
    public static String drawItemCost(int number, Material mat, MaterialStock ms) {

        String nama_barang = mat.getName();
        if (nama_barang.length() >= 34) {
            nama_barang = nama_barang.substring(0, 34);
        }
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        str = str + "<TD class=\"listgensell\">" + number + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">" + mat.getSku() + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\">" + nama_barang + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQty()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getDefaultCost()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQty() * mat.getDefaultCost()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        return str;
    }

    public static String drawPosisiStockItem(int number, Material mat, MaterialStock ms, Unit unit) {
        String nama_barang = mat.getName();
        if (nama_barang.length() >= 34) {
            nama_barang = nama_barang.substring(0, 34);
        }
        String str = "";
        str = "<TR><TD class=\"listgensell\">|</TD>";
        str = str + "<TD class=\"listgensell\">" + number + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">" + mat.getSku() + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\">" + nama_barang + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getAveragePrice()) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\">" + unit.getCode() + "</TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getQty() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQty()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getOpnameQty() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getOpnameQty()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getQtyIn() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQtyIn()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getQtyOut() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQtyOut()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getQtyMin() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getQtyMin()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getSaleQty() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getSaleQty()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";

        if (ms.getClosingQty() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getClosingQty()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        if (ms.getClosingQty() == 0) {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">&nbsp;</div></TD>";
        } else {
            str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(ms.getClosingQty() * mat.getAveragePrice()) + "</div></TD>";
        }
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    /** gadnyana
     * untuk pembuatan total dari posisi stock all, yang store dan warehouse
     * @param totQty
     * @param totQtyIn
     * @param totQtyOut
     * @param totQtyMin
     * @param totQtyLast
     * @return
     */
    public static String drawPosisiStockTotalItem(double totQty, double totQtyIn, double totQtyOut, double totQtyMin, double totQtyLast, double totQtySale) {
        String str = "";
        str = "<TR><TD class=\"listgensell\"></TD>";
        str = str + "<TD colspan=\"9\" class=\"listgensell\"><div align=\"right\">TOTAL</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totQty) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totQtyIn) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totQtyOut) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totQtyMin) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totQtySale) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"right\">" + FRMHandler.userFormatStringDecimal(totQtyLast) + "</div></TD>";
        str = str + "<TD class=\"listgensell\"><div align=\"center\">|</div></TD></TR>";
        return str;
    }

    /** gadnyana
     * get qty in / receive next insert ke TBL_NAME
     * @param TBL_NAME
     * @param srcReportStock
     */
    public static void insertSelectReceive(String TBL_NAME, SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL," +
                    " SELL_PRICE, UNIT, QTY_RECEIVE, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
                    ", MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
                    ", MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                    ", MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql += ", (MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE];
                sql += " * (MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST];
                sql += " + MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] + "))";
                
            }
            else {
                sql += ", 0";
            }
            sql += ", " + srcReportStock.getUserId();
            sql +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS MRI " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " AS MR " +
                    " ON MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " = MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " WHERE MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }

            if (srcReportStock.getSupplierId() != 0) {
                // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                //sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportStock.getSubCategoryId();
            }

            //sql = sql + " AND (MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
            //        " OR MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
            //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
            
            sql += " AND MR."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND MR."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            //System.out.println("SQL RECEIVE : " + sql);
            int i = DBHandler.execSqlInsert(sql);
            // untuk mengalikan qty sesuai dengan base unit
            //System.out.println("====>>>> START TRANSFER DATA RECEIVE");
            sql = "SELECT * FROM " + TBL_NAME;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long unitId = rs.getLong("UNIT_ID");
                long baseUnitId = rs.getLong("BASE_UNIT_ID");

                double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
                sql = "UPDATE " + TBL_NAME + " SET QTY_RECEIVE = " + (rs.getDouble("QTY_RECEIVE") * qtyBase);
                sql+= " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID")+";";
                DBHandler.execUpdate(sql);
            }
            //System.out.println("====>>>> END TRANSFER DATA RECEIVE");
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectReceive(#,#) >> " + e.toString());
        }
    }

    /** gadnyana
     * get qty dispatch and insert ke TBL_NAME
     * @param TBL_NAME
     * @param srcReportStock
     */
    public static void insertSelectDispatch(String TBL_NAME, SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL, SELL_PRICE," +
                    " UNIT, QTY_DISPATCH, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    ", MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
                    ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    ", MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql += ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP];
            }
            else {
                sql += ", 0";
            }
            sql += ", " + srcReportStock.getUserId();
            sql +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS MDI " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " AS MD " +
                    " ON MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " WHERE MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }

            if (srcReportStock.getSupplierId() != 0) {
                //sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportStock.getSubCategoryId();
            }
            
            //sql = sql + " AND (MR." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
             //       " OR MR." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";
            //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
            
            sql += " AND MD."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND MD."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            //System.out.println("SQL DISPATCH : " + sql);
            int i = DBHandler.execSqlInsert(sql);
            // untuk mengalikan qty sesuai dengan base unit
            //System.out.println("====>>>> START TRANSFER DATA DISPATCH");
            sql = "SELECT * FROM " + TBL_NAME;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long unitId = rs.getLong("UNIT_ID");
                long baseUnitId = rs.getLong("BASE_UNIT_ID");

                double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
                sql = "UPDATE " + TBL_NAME + " SET QTY_DISPATCH = " + (rs.getDouble("QTY_DISPATCH") * qtyBase);
                sql+= " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
                DBHandler.execUpdate(sql);
            }
            //System.out.println("====>>>> END TRANSFER DATA DISPATCH");

        } catch (Exception e) {
        }
    }

    public static void insertSelectCosting(String TBL_NAME, SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL," +
                    " SELL_PRICE, UNIT, QTY_DISPATCH, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] +
                    ", MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
                    ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID] +
                    ", MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql += ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP];
            }
            else {
                sql += ", 0";
            }
            sql += ", " + srcReportStock.getUserId();
            sql +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " AS MCI " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstMatCosting.TBL_COSTING + " AS MC " +
                    " ON MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " = MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " WHERE MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }
            
            if (srcReportStock.getSupplierId() != 0) {
                //sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportStock.getSubCategoryId();
            }

            //sql = sql + " AND (MR." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
             //       " OR MR." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";
            //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
            
            sql += " AND MC."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND MC."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            //System.out.println("SQL COSTING : " + sql);
            int i = DBHandler.execSqlInsert(sql);
            // untuk mengalikan qty sesuai dengan base unit
            //System.out.println("====>>>> START TRANSFER DATA COSTING");
            sql = "SELECT * FROM " + TBL_NAME;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long unitId = rs.getLong("UNIT_ID");
                long baseUnitId = rs.getLong("BASE_UNIT_ID");

                double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
                sql = "UPDATE " + TBL_NAME + " SET QTY_DISPATCH = " + (rs.getDouble("QTY_DISPATCH") * qtyBase);
                sql+= " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
                DBHandler.execUpdate(sql);
            }
            //System.out.println("====>>>> END TRANSFER DATA DISPATCH");

        } catch (Exception e) {
        }
    }


    public static void insertSelectOpname(String TBL_NAME, SrcReportStock srcReportStock) {
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, OPNAME_ITEM_ID, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID," +
                    " BARCODE,MATERIAL,SELL_PRICE,UNIT,QTY_OPNAME, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID] +
                    ", SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
                    ", SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql += ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST];
            }
            else {
                sql += ", 0";
            }
            sql += ", " + srcReportStock.getUserId();
            sql +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS SOI " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS SO " +
                    " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " WHERE SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }
            if (srcReportStock.getSupplierId() != 0) {
                //sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportStock.getSubCategoryId();
            }

            //sql = sql + " AND MR." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED;
            //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
            
            sql += " AND SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            //System.out.println("SQL OPNAME : " + sql);
            int i = DBHandler.execSqlInsert(sql);

        } catch (Exception e) {
        }
    }

    public static void insertSelectSale(String TBL_NAME, SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL, SELL_PRICE," +
                    " UNIT, QTY_SALE, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK] + //FLD_QUANTITY] + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] +
                    ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql += ", (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE];
                sql += " * BM." + PstBillMain.fieldNames[PstBillMain.FLD_RATE] + ")";
            }
            else {
                sql += ", 0";
            }
            sql += ", " + srcReportStock.getUserId();
            sql +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM " +
                    " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR +
                    " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                    " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            
            String sql2 = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL," +
                    " SELL_PRICE, UNIT, QTY_RETURN_CUST, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK] + //FLD_QUANTITY] + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] +
                    ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql2 += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql2 += ", (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE];
                sql2 += " * BM." + PstBillMain.fieldNames[PstBillMain.FLD_RATE] + ")";
            }
            else {
                sql2 += ", 0";
            }
            sql2 += ", " + srcReportStock.getUserId();
            sql2 +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM " +
                    " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR +
                    " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR +
                    " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
                sql2 += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }
            
            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
                sql2 = sql2 + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSupplierId() != 0) {
                //sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportStock.getSubCategoryId();
            }
            
            //sql = sql + " AND (MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
            //        " OR MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
            //sql2 = sql2 + " AND (MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
            //        " OR MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
            //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
            
            
            System.out.println("SQL PENJUALAN : " + sql+"\n");
            int i = DBHandler.execSqlInsert(sql);
            /** Proses ini tidak diperlukan lagi, karena field qty_stock merupakan qty berdasarkan unit dari stock
            // untuk mengalikan qty sesuai dengan base unit
            //System.out.println("====>>>> START TRANSFER DATA INVOICE");
            sql = "SELECT * FROM " + TBL_NAME;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long unitId = rs.getLong("UNIT_ID");
                long baseUnitId = rs.getLong("BASE_UNIT_ID");
                
                double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
                sql = "UPDATE " + TBL_NAME + " SET QTY_SALE = " + (rs.getDouble("QTY_SALE") * qtyBase);
                sql+= " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
                DBHandler.execUpdate(sql);
            }
             */
            //System.out.println("====>>>> END TRANSFER DATA INVOICE");
            
            //System.out.println("SQL RETUR PENJUALAN : " + sql2);
            int a = DBHandler.execSqlInsert(sql2);
            /** Proses ini tidak diperlukan lagi, karena field qty_stock merupakan qty berdasarkan unit dari stock
            // untuk mengalikan qty sesuai dengan base unit
            //System.out.println("====>>>> START TRANSFER DATA CUSTOMER RETURN");
            sql2 = "SELECT * FROM " + TBL_NAME;
            dbrs = DBHandler.execQueryResult(sql2);
            ResultSet rs2 = dbrs.getResultSet();
            while (rs2.next()) {
                long unitId = rs2.getLong("UNIT_ID");
                long baseUnitId = rs2.getLong("BASE_UNIT_ID");
                
                double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
                sql = "UPDATE " + TBL_NAME + " SET QTY_RETURN_CUST = " + (rs2.getDouble("QTY_RETURN_CUST") * qtyBase);
                sql+= " WHERE ITEM_ID = " + rs2.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs2.getLong("MATERIAL_ID");
                DBHandler.execUpdate(sql);
            }
             */
            //System.out.println("====>>>> END TRANSFER DATA CUSTOMER RETURN");

        } catch (Exception e) {
            System.out.println("Error insert penjualan " + e.toString());
        }
    }

    /** gadnyana
     * get qty retur and insert ke TBL_NAME
     * @param TBL_NAME
     * @param srcReportStock
     */
    public static void insertSelectReturn(String TBL_NAME, SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID,"+
                    " BARCODE,MATERIAL, SELL_PRICE, UNIT, QTY_RETURN, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) " +
                    " SELECT DISTINCT " +
                    " MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                    ", MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + // " ,SUM(RMI."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]+") "+
                    ", MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID] +
                    ", MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID];
            if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_MASTER) {
                sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
            }
            else if(srcReportStock.getStockValueBy() == SrcReportStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
                sql += ", (MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST];
                sql += " * MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_TRANS_RATE] + ")";
            }
            else {
                sql += ", 0";
            }
            sql += ", " + srcReportStock.getUserId();
            sql +=  " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS MRI " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " AS MR " +
                    " ON MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                    " = MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    " WHERE MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcReportStock.getDateTo(), "yyyy-MM-dd 23:23:59") + "'" +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcReportStock.getLocationId();
            }

            if (srcReportStock.getSupplierId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportStock.getSubCategoryId();
            }

            //sql = sql + " AND (MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
            //        " OR MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
            //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            sql += " AND MR."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND MR."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            // System.out.println("SQL RETUR : " + sql);
            int i = DBHandler.execSqlInsert(sql);

            // untuk mengalikan qty sesuai dengan base unit
            //System.out.println("====>>>> START TRANSFER DATA RETURN");
            sql = "SELECT * FROM " + TBL_NAME;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long unitId = rs.getLong("UNIT_ID");
                long baseUnitId = rs.getLong("BASE_UNIT_ID");

                double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
                sql = "UPDATE " + TBL_NAME + " SET QTY_RETURN = " + (rs.getDouble("QTY_RETURN") * qtyBase);
                sql+= " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
                DBHandler.execUpdate(sql);
            }
            //System.out.println("====>>>> END TRANSFER DATA RETURN");

        } catch (Exception e) {
        }
    }


    /** gadnyana
     * insert ke sesuai TBL_NAME untuk qty awal
     * @param TBL_NAME
     * @param periode
     */
    public static void insertSelectPreviousPeriode(String TBL_NAME, SrcReportStock srcReportStock) { // long periode, long oidLocation
        try {
            /*String sql = "INSERT INTO " + TBL_NAME +
                    " (MATERIAL_ID,BARCODE,MATERIAL,SELL_PRICE,UNIT,QTY_AWAL,SUB_CATEGORY_ID,SUPPLIER_ID)" +
                    " SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    " ,U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportStock.getPeriodeId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();

            if (srcReportStock.getSupplierId() != 0) {
                //sql = sql + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportStock.getSupplierId();
            }

            if (srcReportStock.getCategoryId() != 0) {
                sql = sql + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

            if (srcReportStock.getSubCategoryId() != 0) {
                //sql = sql + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId();
            }

            //System.out.println("SQL PREVIOUS : " + sql);
            int i = DBHandler.execSqlInsert(sql);*/

        } catch (Exception e) {
        }
    }

    /**
     * data history dari receive, df,return
     * @param TBL_NAME
     * @param TBL_NAME_HIS
     */
    public static void insertSelectHistory(String TBL_NAME, String TBL_NAME_HIS) {
        try {
            String sql = "INSERT INTO " + TBL_NAME +
                    " (MATERIAL_ID,SUB_CATEGORY_ID,SUPPLIER_ID,BARCODE,MATERIAL,SELL_PRICE,UNIT,QTY_AWAL)" +
                    " SELECT MATERIAL_ID,SUB_CATEGORY_ID,SUPPLIER_ID,BARCODE,MATERIAL,SELL_PRICE,UNIT,SUM((QTY_RECEIVE  - QTY_RETURN) - QTY_DISPATCH)" +
                    " FROM " + TBL_NAME_HIS +
                    " GROUP BY BARCODE,MATERIAL,UNIT";

            //System.out.println("SQL : " + sql);
            int i = DBHandler.execSqlInsert(sql);

        } catch (Exception e) {
        }
    }

    /** gadnyana
     * delete data history
     * @param TBL_NAME
     * @param TBL_NAME_HIS
     */
    public static void deleteSelect(String TBL_NAME, String TBL_NAME_HIS) {
        try {
            System.out.println("== >> DELETE HISTORY");
            String sql = "DELETE FROM " + TBL_NAME;
            DBHandler.execUpdate(sql);
            
            sql = "DELETE FROM " + TBL_NAME_HIS;
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
        }
    }


    /** gadnyana
     * fungsi ini di gunakan oleh 2 modul yaitu
     * report posisi stok dan weekly report yang periode II, param weekReport adalah yang membedakan dalam pencarian
     * bila true berarti memilih report weekly report
     * dan sebaliknya. jika false berarti memilih
     * @param type
     * @param srcReportStock
     * @return
     */
    synchronized public static Vector reportPosisiStock(boolean weekReport, int type, SrcReportStock srcReportStock,
                                                        int language, boolean isZero, int pgStart, int pgNext) {
        
        /** hapus isi tabel temporary dalam keadaan kosong */
        deleteSelect(TBL_MATERIAL_STOCK_REPORT, TBL_MATERIAL_STOCK_REPORT_HIS);
        
        Vector list = new Vector(1, 1);
        try {System.out.println("1. masuk ke fungsi reportPosisiStock(#,#,#,#,#,#,#) >> rekap transaksi");
            //Periode matPeriode = PstPeriode.getPeriode(srcReportStock.getDateFrom());
            // menghambil data trnasaksi sesuai dengan tanggal yang di select.
            insertSelectReceive(TBL_MATERIAL_STOCK_REPORT, srcReportStock);
            insertSelectReturn(TBL_MATERIAL_STOCK_REPORT, srcReportStock);
            insertSelectDispatch(TBL_MATERIAL_STOCK_REPORT, srcReportStock);
            insertSelectCosting(TBL_MATERIAL_STOCK_REPORT, srcReportStock);
            insertSelectSale(TBL_MATERIAL_STOCK_REPORT, srcReportStock);
            insertSelectOpname(TBL_MATERIAL_STOCK_REPORT, srcReportStock);
            //insertSelectPreviousPeriode(TBL_MATERIAL_STOCK_REPORT, srcReportStock);

            // cek jika tanggal yang di pilih > dari tanggal awal periode
            // System.out.println("=== >>> END CREATE HISTORY");
            // if (srcReportStock.getDateFrom().getDate() > matPeriode.getStartDate().getDate()) {
            // set date last find data
            Date dateTo = srcReportStock.getDateFrom();
            dateTo.setDate(dateTo.getDate() - 1);
            srcReportStock.setDateTo(dateTo);
            
            Vector vect = PstPeriode.list(0, 0, "", PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]);
            if (vect != null && vect.size() > 0) {
                Periode periode = (Periode) vect.get(0);
                srcReportStock.setDateFrom(periode.getStartDate());
                srcReportStock.setPeriodeId(periode.getOID());
            }
            
            // set date start find data
            insertSelectReceive(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportStock);
            insertSelectReturn(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportStock);
            insertSelectDispatch(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportStock);
            insertSelectCosting(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportStock);
            insertSelectSale(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportStock);
            insertSelectOpname(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportStock);
            //insertSelectHistory(TBL_MATERIAL_STOCK_REPORT, TBL_MATERIAL_STOCK_REPORT_HIS);
            // }
            //System.out.println("=== >>> END LOAD DATA");
            if (weekReport) {
                System.out.println("=== >>> WEEKLY REPORT");
                list = weeklyStockReportPeriodeII();
            } else {
                System.out.println("=== >>> POSISI STOCK");
                String sql = "SELECT ";
                switch (type) {
                    case TYPE_REPORT_POSISI_SUPPLIER:
                        sql = sql + " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ", ";
                }

                sql = sql + " MS.BARCODE, MS.MATERIAL,MS.SELL_PRICE,MS.UNIT," +
                        " SUM(MS.QTY_AWAL) AS QTY_AWAL, " +
                        " SUM(MS.QTY_RECEIVE) AS QTY_RECEIVE, " +
                        " SUM(MS.QTY_DISPATCH) AS QTY_DISPATCH," +
                        " SUM(MS.QTY_RETURN) AS QTY_RETURN , " +
                        " SUM(MS.QTY_SALE) AS QTY_SALE , " +
                        " SUM((((MS.QTY_AWAL + MS.QTY_RECEIVE) - MS.QTY_DISPATCH)- MS.QTY_RETURN)-QTY_SALE) AS QTY_AKHIR " +
                        " FROM " + TBL_MATERIAL_STOCK_REPORT + " AS MS ";

                /*
                " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                " ON MS.SUB_CATEGORY_ID = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                " ON SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                ",SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] + ",*/
                
                switch (type) {
                    case TYPE_REPORT_POSISI_ALL:
                        /** Seluruh proses telah dipindahkan ke JSP. Praktis fungsi ini hanya melakukan rekap data */
                        //sql = sql + " GROUP BY " + " MS.BARCODE,MS.MATERIAL " + " ORDER BY MS.BARCODE";
                        
                        /** method ini langsung menghasilkan output dalam format HTML */
                        System.out.println("=== >> REPORT POSISI ALL WITH HTML!");
                        list = reportPosisiStockAll(srcReportStock, sql, language, isZero, pgStart, pgNext);
                        break;
                    case TYPE_REPORT_POSISI_CATEGORY:
                        sql = sql +
                                " GROUP BY " +
                                " MS.BARCODE,MS.MATERIAL " +
                                " ORDER BY MS.BARCODE";
                        System.out.println("=== >> REPORT POSISI KATEGORI");
                        /** method ini langsung menghasilkan output dalam format HTML */
                        //System.out.println("=== >> REPORT POSISI ALL WITH HTML!");
                        list = reportPosisiStockKategori(srcReportStock, sql, language, isZero, pgStart, pgNext);
                        break;
                    case TYPE_REPORT_POSISI_SUPPLIER:
                        sql = sql +
                                " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS VP " +
                                " ON MS.MATERIAL_ID = VP." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                                " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                                " ON VP." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                                " WHERE VP." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + srcReportStock.getSupplierId() +
                                " GROUP BY " +
                                " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ", " +
                                " MS.BARCODE,MS.MATERIAL " +
                                " ORDER BY MS.BARCODE";
                        System.out.println("=== >> REPORT POSISI SUPPLIER");
                        list = reportPosisiStockSupplier(srcReportStock, sql, language, isZero, pgStart, pgNext);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exc. reportPosisiStock(#,#,#,#,#,#,#) >> " + e.toString());
        }
        
        return list;
    }

    /**gadnyana
     * for repost posisi stock at gudang
     * @param sql
     * @param language
     * @param isZero
     * @param pgStart
     * @param pgNext
     * @return
     */
    public static Vector reportPosisiStockAll(SrcReportStock srcReportStock, String sql, int language, boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            Vector vect = getReportStockAll(srcReportStock, false);
            // dbrs = DBHandler.execQueryResult(sql);
            // ResultSet rs = dbrs.getResultSet();
            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;

            double totQty = 0;
            double totQtyIn = 0;
            double totQtyOut = 0;
            double totQtyMin = 0;
            double totQtyLast = 0;
            double qtyOpname = 0;
            double qtySale = 0;
            double totalStock = 0.0;

            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            
            /**
             * untuk mencari posisi stock barang
             */
            if (vect != null && vect.size() > 0) {
                for (int k = 0; k < vect.size(); k++) {
                    Vector vt = (Vector) vect.get(k);
                    Material material = (Material) vt.get(0);
                    MaterialStock materialStock = (MaterialStock) vt.get(1);
                    Unit unit = (Unit) vt.get(2);
                    MaterialStock matStock = new MaterialStock();

                    sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE FROM " + TBL_MATERIAL_STOCK_REPORT;
                    sql = sql + " WHERE MATERIAL_ID=" + material.getOID() + " AND OPNAME_ITEM_ID!=0 ORDER BY TRS_DATE DESC";

                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();

                    Date date = new Date();
                    boolean withOpBool = false;
                    while (rs.next()) {
                        withOpBool = true;
                        matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
                        date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
                        break;
                    }

                    // get qty receive
                    sql = "SELECT SUM(QTY_RECEIVE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();

                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyIn(rs.getDouble("TOT"));
                    }

                    // get qty DISPATCH
                    sql = "SELECT SUM(QTY_DISPATCH) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyOut(rs.getDouble("TOT"));
                    }

                    // get qty RETURN
                    sql = "SELECT SUM(QTY_RETURN) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyMin(rs.getDouble("TOT"));
                    }

                    // get qty RETURN
                    sql = "SELECT SUM(QTY_SALE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setSaleQty(rs.getDouble("TOT"));
                    }

                    // stock awal
                    matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
                    if (withOpBool) {
                        // STOCK AKHIR
                        matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn()) - (matStock.getQtyOut() + matStock.getQtyMin())-matStock.getSaleQty());
                    } else {
                        // STOCK AKHIR
                        matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty()) - (matStock.getQtyOut() + matStock.getQtyMin())-matStock.getSaleQty());
                    }

                    bool = false;
                    if (!firstt) {
                        list.add(drawPosisiStockLineHorizontal());
                        list.add(posisiStockHeader(language));
                        list.add(drawPosisiStockLineHorizontal());
                        baris = 3;
                        firstt = true;
                    }

                    // ini di pakai untuk update stok barang
                   /* try{
                        String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+material.getOID()+
                                " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+504404252370511713L;
                        Vector vlist = PstMaterialStock.list(0,0,where,"");
                        if(vlist!=null && vlist.size()>0){
                            //MaterialStock matStock = (MaterialStock)vlist.get(1);
                        }
                    }catch(Exception e){}*/

                    list.add(drawPosisiStockItem(line, material, matStock, unit));
                    baris++;
                    line++;

                    if (baris == maxlines) {
                        if (boolmaxlines) {
                            maxlines = pgNext;
                        }
                        boolmaxlines = false;

                        list.add(drawPosisiStockLineHorizontal());
                        list.add(drawPosisiStockLineHorizontal());
                        list.add(posisiStockHeader(language));
                        list.add(drawPosisiStockLineHorizontal());
                        baris = 3;
                    }

                    totQty += matStock.getQty();
                    totQtyIn += matStock.getQtyIn();
                    totQtyOut += matStock.getQtyOut();
                    totQtyMin += matStock.getQtyMin();
                    totQtyLast += matStock.getClosingQty();
                    qtyOpname += matStock.getOpnameQty();
                    qtySale += matStock.getSaleQty();
                    totalStock = totalStock + (matStock.getClosingQty() * material.getAveragePrice());
                }
                if (bool) {
                    list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
                } else {
                    list.add(drawPosisiStockLineHorizontal());
                    list.add(drawPosisiStockTotal("", "TOTAL", totQty, qtyOpname, totQtyIn, totQtyOut, totQtyMin, totQtyLast, totalStock,qtySale));
                    list.add(drawPosisiStockLineTotalHorizontal(""));
                }
                list.add("</table>");

                deleteSelect(TBL_MATERIAL_STOCK_REPORT, TBL_MATERIAL_STOCK_REPORT_HIS);
            }
        } catch (Exception e) {
            System.out.println("ERR >> : " + e.toString());
        }
        return list;
    }


    /**
     *
     * @param srcReportStock
     * @param sql
     * @param language
     * @param isZero
     * @param pgStart
     * @param pgNext
     * @return
     */
    public static double reportQtyAwalPosisiStock(long oidMaterial, long oidLocation) {
        DBResultSet dbrs = null;
        MaterialStock matStock = new MaterialStock();
        try {
            /** get begin qty */
            String sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE FROM " + TBL_MATERIAL_STOCK_REPORT_HIS;
            sql = sql + " WHERE MATERIAL_ID=" + oidMaterial + " AND LOCATION_ID="+ oidLocation +" AND OPNAME_ITEM_ID !=0 ORDER BY TRS_DATE DESC";
            
            Date date = new Date();
            boolean withOpBool = false;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                withOpBool = true;
                matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
                date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
                break;
            }
            
            
            /** get receive qty */
            sql = "SELECT SUM(QTY_RECEIVE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT_HIS +
                    " WHERE MATERIAL_ID=" + oidMaterial + " AND LOCATION_ID="+ oidLocation;
            if (withOpBool) {
                sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
            }
            sql = sql + " GROUP BY MATERIAL_ID";
            
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            while (rs.next()) {
                matStock.setQtyIn(rs.getDouble("TOT"));
            }
            
            
            /** get dispatch qty */
            sql = "SELECT SUM(QTY_DISPATCH) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT_HIS +
                    " WHERE MATERIAL_ID=" + oidMaterial + " AND LOCATION_ID="+ oidLocation;
            if (withOpBool) {
                sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
            }
            sql = sql + " GROUP BY MATERIAL_ID";
            
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            while (rs.next()) {
                matStock.setQtyOut(rs.getDouble("TOT"));
            }
            
            
            /** get return qty */
            sql = "SELECT SUM(QTY_RETURN) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT_HIS +
                    " WHERE MATERIAL_ID=" + oidMaterial + " AND LOCATION_ID="+ oidLocation;
            if (withOpBool) {
                sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
            }
            sql = sql + " GROUP BY MATERIAL_ID";
            
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            while (rs.next()) {
                matStock.setQtyMin(rs.getDouble("TOT"));
            }
            
            /** get qty sale */
            sql = "SELECT SUM(QTY_SALE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT_HIS;
            sql += " WHERE MATERIAL_ID=" + oidMaterial + " AND LOCATION_ID="+ oidLocation;
            if (withOpBool) {
                sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
            }
            sql = sql + " GROUP BY MATERIAL_ID";
            
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            while (rs.next()) {
                matStock.setSaleQty(rs.getDouble("TOT"));
            }
            
            /** get end qty */
            if (withOpBool) {
                matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            }else{
                matStock.setClosingQty(matStock.getQtyIn() - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            }
            
        }
        catch (Exception e) {
            System.out.println("Exc. in reportQtyAwalPosisiStock(#,#) : " + e.toString());
        }
        return matStock.getClosingQty();
    }


    public static Vector reportPosisiStockKategori(SrcReportStock srcReportStock, String sql, int language, boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            Vector vect = getReportStockPerKategori(srcReportStock, false);
            //dbrs = DBHandler.execQueryResult(sql);
            //ResultSet rs = dbrs.getResultSet();

            // variable for get value name subCategory
            // if diferent create new header sub category
            String nmCategory = "";

            //for fisrt loop
            boolean first = true;

            // for cek max line in page
            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;

            // total qty per sub categoryt
            double subQtyAwal = 0;
            double subQtyReceive = 0;
            double subQtyKeluar = 0;
            double subQtyRetur = 0;
            double subQtyAkhir = 0;
            double subQtySale = 0;
            // total all qty
            double qtyAwal = 0;
            double qtyReceive = 0;
            double qtyKeluar = 0;
            double qtyRetur = 0;
            double qtyAkhir = 0;
            double qtySale = 0;
            double qtyOpname = 0;
            double totalStock = 0.0;
            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            if (vect != null && vect.size() > 0) {
                for (int k = 0; k < vect.size(); k++) {
                    Vector vt = (Vector) vect.get(k);
                    Material material = (Material) vt.get(0);
                    MaterialStock materialStock = (MaterialStock) vt.get(1);
                    Unit unit = (Unit) vt.get(3);

                    MaterialStock matStock = new MaterialStock();

                    sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE FROM " + TBL_MATERIAL_STOCK_REPORT;
                    sql = sql + " WHERE MATERIAL_ID=" + material.getOID() + " AND OPNAME_ITEM_ID !=0 ORDER BY TRS_DATE DESC";

                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();

                    Date date = new Date();
                    boolean withOpBool = false;
                    while (rs.next()) {
                        withOpBool = true;
                        matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
                        date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
                        break;
                    }
                    //System.out.println("ini tanggal yang di dapat dari opname : "+date);

                    // get qty receive
                    sql = "SELECT SUM(QTY_RECEIVE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();

                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyIn(rs.getDouble("TOT"));
                    }

                    // get qty DISPATCH
                    sql = "SELECT SUM(QTY_DISPATCH) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyOut(rs.getDouble("TOT"));
                    }

                    // get qty RETURN
                    sql = "SELECT SUM(QTY_RETURN) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyMin(rs.getDouble("TOT"));
                    }

                    // get qty sale
                    sql = "SELECT SUM(QTY_SALE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setSaleQty(rs.getDouble("TOT"));
                    }

                    // stock awal
                    matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
                    if (withOpBool) {
                        // STOCK AKHIR
                        matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn()) - (matStock.getQtyOut() + matStock.getQtyMin())-matStock.getSaleQty());
                    } else {
                        // STOCK AKHIR
                        matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
                    }

                    bool = false;
                    if (!firstt) {
                        list.add(drawPosisiStockLineHorizontal());
                        list.add(posisiStockHeader(language));
                        list.add(drawPosisiStockLineHorizontal());
                        baris = 3;
                        firstt = true;
                    }

                    /*Material material = new Material();
                    MaterialStock matStock = new MaterialStock();
                    SubCategory subCategory = new SubCategory();
                    Unit unit = new Unit();

                    //subCategory.setName(rs.getString(PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                    material.setSku(rs.getString("BARCODE"));
                    material.setName(rs.getString("MATERIAL"));
                    material.setDefaultPrice(rs.getDouble("SELL_PRICE"));
                    unit.setCode(rs.getString("UNIT"));
                    matStock.setQty(rs.getInt("QTY_AWAL"));
                    matStock.setQtyIn(rs.getInt("QTY_RECEIVE"));
                    matStock.setQtyOut(rs.getInt("QTY_DISPATCH"));
                    matStock.setQtyMin(rs.getInt("QTY_RETURN"));
                    matStock.setClosingQty(rs.getInt("QTY_AKHIR"));*/

                    list.add(drawPosisiStockItem(line, material, matStock, unit));
                    baris++;
                    line++;
                    if (baris == maxlines) {
                        if (boolmaxlines) {
                            maxlines = pgNext;
                        }
                        boolmaxlines = false;

                        list.add(drawPosisiStockLineHorizontal());
                        list.add(drawPosisiStockLineHorizontal());
                        list.add(posisiStockHeader(language));
                        list.add(drawPosisiStockLineHorizontal());
                        baris = 3;
                    }
                    // total sub qty
                    subQtyAwal = subQtyAwal + matStock.getQty();
                    subQtyReceive = subQtyReceive + matStock.getQtyIn();
                    subQtyKeluar = subQtyKeluar + matStock.getQtyOut();
                    subQtyRetur = subQtyRetur + matStock.getQtyMin();
                    subQtyAkhir = subQtyAkhir + matStock.getClosingQty();
                    subQtySale = subQtySale + matStock.getSaleQty();

                    // total qty
                    qtyAwal = qtyAwal + matStock.getQty();
                    qtyReceive = qtyReceive + matStock.getQtyIn();
                    qtyKeluar = qtyKeluar + matStock.getQtyOut();
                    qtyRetur = qtyRetur + matStock.getQtyMin();
                    qtyAkhir = qtyAkhir + matStock.getClosingQty();
                    qtySale = qtySale + matStock.getSaleQty();
                    qtyOpname += matStock.getOpnameQty();
                    totalStock = totalStock + (matStock.getClosingQty() * material.getAveragePrice());
                }
                if (bool) {
                    list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
                } else {
                    list.add(drawPosisiStockLineHorizontal()); // for last line page
                    //list.add(drawPosisiStockTotal("", "SUB TOTAL", subQtyAwal, subQtyReceive, subQtyKeluar, subQtyRetur, subQtyAkhir));
                    //list.add(drawPosisiStockLineTotalHorizontal("")); // for last line page
                    list.add(drawPosisiStockTotal("", "TOTAL", qtyAwal, qtyOpname, qtyReceive, qtyKeluar, qtyRetur, qtyAkhir, totalStock, qtySale));
                    list.add(drawPosisiStockLineTotalHorizontal("")); // for last line page
                    baris++;
                }
            }
            list.add("</table>");
            deleteSelect(TBL_MATERIAL_STOCK_REPORT, TBL_MATERIAL_STOCK_REPORT_HIS);
        } catch (Exception e) {
            System.out.println("ERR POSISI KATEGORI >> : " + e.toString());
        }
        return list;
    }

    public static Vector reportPosisiStockSupplier(SrcReportStock srcReportStock, String sql, int language, boolean isZero, int pgStart, int pgNext) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            /*dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();*/
            Vector vect = getReportStockPerSupplier(srcReportStock, false);
            int line = 1;
            int baris = 0;
            int maxlines = pgStart;
            boolean boolmaxlines = true;

            double totQty = 0;
            double totQtyIn = 0;
            double totQtyOut = 0;
            double totQtyMin = 0;
            double totQtyLast = 0;
            double totQtyOpname = 0;
            double qtySale =0;
            double totalStock = 0.0;
            boolean firstt = false;
            boolean bool = true;
            // create table HTML for list
            list.add("<table width=\"100%\" border=\"0\" class=\"listgen\" cellspacing=\"0\">");
            if (vect != null && vect.size() > 0) {
                for (int k = 0; k < vect.size(); k++) {
                    Vector vt = (Vector) vect.get(k);
                    Material material = (Material) vt.get(0);
                    MaterialStock materialStock = (MaterialStock) vt.get(1);
                    Unit unit = (Unit) vt.get(2);

                    MaterialStock matStock = new MaterialStock();

                    sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE FROM " + TBL_MATERIAL_STOCK_REPORT;
                    sql = sql + " WHERE MATERIAL_ID=" + material.getOID() + " AND OPNAME_ITEM_ID!=0 ORDER BY TRS_DATE DESC";

                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();

                    Date date = new Date();
                    boolean withOpBool = false;
                    while (rs.next()) {
                        withOpBool = true;
                        matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
                        date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
                        break;
                    }

                    // get qty receive
                    sql = "SELECT SUM(QTY_RECEIVE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();

                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyIn(rs.getDouble("TOT"));
                    }

                    // get qty DISPATCH
                    sql = "SELECT SUM(QTY_DISPATCH) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyOut(rs.getDouble("TOT"));
                    }

                    // get qty RETURN
                    sql = "SELECT SUM(QTY_RETURN) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setQtyMin(rs.getDouble("TOT"));
                    }

                    // get qty RETURN
                    sql = "SELECT SUM(QTY_SALE) AS TOT FROM " + TBL_MATERIAL_STOCK_REPORT +
                            " WHERE MATERIAL_ID=" + material.getOID();
                    if (withOpBool) {
                        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date,"yyyy-MM-dd")+" "+Formater.formatTimeLocale(date,"kk:mm:ss") + "'";
                    }
                    sql = sql + " GROUP BY MATERIAL_ID";
                    dbrs = DBHandler.execQueryResult(sql);
                    rs = dbrs.getResultSet();
                    while (rs.next()) {
                        matStock.setSaleQty(rs.getDouble("TOT"));
                    }

                    // stock awal
                    matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
                    if (withOpBool) {
                        // STOCK AKHIR
                        matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn()) - (matStock.getQtyOut() + matStock.getQtyMin())- matStock.getSaleQty());
                    } else {
                        // STOCK AKHIR
                        matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty()) - (matStock.getQtyOut() + matStock.getQtyMin())- matStock.getSaleQty());
                    }

                    bool = false;
                    if (!firstt) {
                        list.add(drawPosisiStockLineHorizontal());
                        list.add(posisiStockHeader(language));
                        list.add(drawPosisiStockLineHorizontal());
                        baris = 3;
                        firstt = true;
                    }

                    /*Material material = new Material();
                    MaterialStock matStock = new MaterialStock();
                    Unit unit = new Unit();

                    material.setSku(rs.getString("BARCODE"));
                    material.setName(rs.getString("MATERIAL"));
                    material.setDefaultPrice(rs.getDouble("SELL_PRICE"));

                    unit.setCode(rs.getString("UNIT"));

                    matStock.setQty(rs.getInt("QTY_AWAL"));
                    matStock.setQtyIn(rs.getInt("QTY_RECEIVE"));
                    matStock.setQtyOut(rs.getInt("QTY_DISPATCH"));
                    matStock.setQtyMin(rs.getInt("QTY_RETURN"));
                    matStock.setClosingQty(rs.getInt("QTY_AKHIR")); */

                    list.add(drawPosisiStockItem(line, material, matStock, unit));
                    baris++;
                    line++;

                    if (baris == maxlines) {
                        if (boolmaxlines) {
                            maxlines = pgNext;
                        }
                        boolmaxlines = false;

                        list.add(drawPosisiStockLineHorizontal());
                        list.add(drawPosisiStockLineHorizontal());
                        list.add(posisiStockHeader(language));
                        list.add(drawPosisiStockLineHorizontal());
                        baris = 3;
                    }
                    totQty += matStock.getQty();
                    totQtyIn += matStock.getQtyIn();
                    totQtyOut += matStock.getQtyOut();
                    totQtyMin += matStock.getQtyMin();
                    totQtyLast += matStock.getClosingQty();
                    totQtyOpname += matStock.getOpnameQty();
                    qtySale += matStock.getSaleQty();
                    totalStock = totalStock + (matStock.getClosingQty() * material.getAveragePrice());
                }

                if (bool) {
                    list.add("<TR><TD><div class=\"msginfo\">Tidak ada data</div></TD></TR>");
                } else {
                    list.add(drawPosisiStockLineHorizontal());
                    list.add(drawPosisiStockTotal("", "TOTAL", totQty, totQtyOpname, totQtyIn, totQtyOut, totQtyMin, totQtyLast, totalStock,qtySale));
                    list.add(drawPosisiStockLineTotalHorizontal(""));
                }
            }
            list.add("</table>");
            deleteSelect(TBL_MATERIAL_STOCK_REPORT, TBL_MATERIAL_STOCK_REPORT_HIS);

        } catch (Exception e) {
            System.out.println("ERR >> : " + e.toString());
        }
        return list;
    }

    /** gadnyana
     * untuk mencari report stock periode I
     * @param srcReportStock
     * @return
     */
    public static Vector weeklyStockReportPeriodeI(SrcReportStock srcReportStock) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            Periode periode = PstPeriode.getPeriode(srcReportStock.getDateFrom());
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // sku
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME+
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE+
                    " ,U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE+
                    " ,MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] + // OPENING_QTY+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + // MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + //  MATERIAL
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_SELL_UNIT_ID
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID+
                    " WHERE " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + periode.getOID() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId() +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStock.getSubCategoryId() +
                    " ORDER BY " +
                    " C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector rowx = new Vector(1, 1);
                Material material = new Material();
                Unit unit = new Unit();
                MaterialStock materialStock = new MaterialStock();

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                rowx.add(material);

                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                rowx.add(unit);

                materialStock.setOpeningQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
                rowx.add(materialStock);

                list.add(rowx);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERR >> " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static Vector weeklyStockReportPeriodeII() {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " MS.BARCODE,MS.MATERIAL,MS.SELL_PRICE,MS.UNIT," +
                    " SUM(((MS.QTY_AWAL + MS.QTY_RECEIVE) - MS.QTY_DISPATCH)-MS.QTY_RETURN) AS QTY_AKHIR " +
                    " FROM " + TBL_MATERIAL_STOCK_REPORT + " AS MS " +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON MS.SUB_CATEGORY_ID = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " GROUP BY " +
                    " BARCODE,MATERIAL" +
                    " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,MS.BARCODE";

            //System.out.println("=>> SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector rowx = new Vector(1, 1);
                Material material = new Material();
                Unit unit = new Unit();
                MaterialStock materialStock = new MaterialStock();

                material.setSku(rs.getString("BARCODE"));
                material.setName(rs.getString("MATERIAL"));
                material.setDefaultPrice(rs.getDouble("SELL_PRICE"));
                rowx.add(material);

                unit.setCode(rs.getString("UNIT"));
                rowx.add(unit);

                materialStock.setOpeningQty(rs.getDouble("QTY_AKHIR"));
                rowx.add(materialStock);

                list.add(rowx);
            }
            rs.close();
            deleteSelect(TBL_MATERIAL_STOCK_REPORT, TBL_MATERIAL_STOCK_REPORT_HIS);

        } catch (Exception e) {
            System.out.println("==>> ERR PERIODE II :" + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    /**
     * ini untuk mencari stok yang ada di toko/gudang (location)
     * @param locationId
     * @param supplierId
     * @return
     */
    public static Vector getListStock(long locationId, long supplierId) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // Code
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // Code
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID] + // price id
                    ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CATEG_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // id+
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //CODE+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // cost+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + locationId;

            if (supplierId != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + supplierId;
            }

            sql = sql + " GROUP BY M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            //System.out.println("=>> SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector rowx = new Vector(1, 1);
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();
                Category category = new Category();

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPriceCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID]));

                rowx.add(material);

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                rowx.add(matStock);

                category.setName(rs.getString("CATEG_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                rowx.add(category);

                unit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                rowx.add(unit);

                list.add(rowx);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("=>> ERR : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    /** gadnyana
     * untuk mengambil qty stock berdasarkan lokasi dan supplier
     * @param locationId
     * @param supplierId
     * @return
     */
    public static Vector getItemStock(long locationId, long supplierId) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // Code
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // Code
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID] + // price id
                    ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CATEG_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // id+
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //CODE+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // cost+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + locationId;

            if (supplierId != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + supplierId;
            }

            sql = sql + " GROUP BY M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            //System.out.println("=>> SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector rowx = new Vector(1, 1);
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();
                Category category = new Category();

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPriceCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID]));

                rowx.add(material);

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                rowx.add(matStock);

                category.setName(rs.getString("CATEG_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                rowx.add(category);

                unit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                rowx.add(unit);

                list.add(rowx);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("=>> ERR : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /** gadnyana
     * untuk mengambil qty stock berdasarkan lokasi dan supplier
     * @param locationId
     * @param supplierId
     * @return
     */
    public static Vector getItemStock(long locationId, long categoryId, String code, String name) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // Code
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // Code
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID] + // price id
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + // currency id cost
                    ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CATEG_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // id+
                    ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //CODE+
                    ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + // cost+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +

                    /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +*/
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + locationId +
                    " AND M."+PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS]+" != "+PstMaterial.DELETE;

            if (categoryId != 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + categoryId;
            }

            if (code.length() > 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
            }

            if (name.length() > 0) {
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + name + "%'";
            }

            //if(supplierId!=0){
            //    sql = sql + " AND M."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+" = "+supplierId;
            //}


            sql = sql + " GROUP BY M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ORDER BY C." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            System.out.println("=>> SQL get stock toko : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector rowx = new Vector(1, 1);
                Material material = new Material();
                MaterialStock matStock = new MaterialStock();
                Unit unit = new Unit();
                Category category = new Category();

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                material.setDefaultPriceCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID]));
                material.setDefaultCostCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]));

                rowx.add(material);

                matStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                rowx.add(matStock);

                category.setName(rs.getString("CATEG_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                rowx.add(category);

                unit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                rowx.add(unit);

                list.add(rowx);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("=>> ERR : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    private static String createFieldSum(Vector vectLoc) {
        String str = "";
        if (vectLoc != null && vectLoc.size() > 0) {
            for (int k = 0; k < vectLoc.size(); k++) {
                Location loc = (Location) vectLoc.get(k);
                if (str.length() == 0) {
                    str = "sum(q" + k + "),sum(qm" + k + ")";
                } else {
                    str = str + ",sum(q" + k + "),sum(qm" + k + ")";
                }
            }
        }
        return str;
    }


    private static String createField(Vector vectLoc, long oidLocation, int idx) {
        String str = "";
        if (vectLoc != null && vectLoc.size() > 0) {
            for (int k = 0; k < vectLoc.size(); k++) {
                Location loc = (Location) vectLoc.get(k);
                if (idx == 0) {
                    if (loc.getOID() == oidLocation) {
                        if (str.length() == 0) {
                            str = "qty as q" + k + ",qty_min as qm" + k;
                        } else {
                            str = str + ",qty as q" + k + ",qty_min as qm" + k;
                        }
                    } else {
                        if (str.length() == 0) {
                            str = "0 as q" + k + ",0 as qm" + k;
                        } else {
                            str = str + ",0 as q" + k + ",0 as qm" + k;
                        }
                    }
                } else {
                    if (loc.getOID() == oidLocation) {
                        if (str.length() == 0) {
                            str = "qty as q" + k + ",qty_min as qm" + k;
                        } else {
                            str = str + ",qty as q" + k + ",qty_min as qm" + k;
                        }
                    } else {
                        if (str.length() == 0) {
                            str = "0 as q" + k + ",0 as qm" + k;
                        } else {
                            str = str + ",0 as q" + k + ",0 as qm" + k;
                        }
                    }
                }
            }
        }
        return str;
    }

    private static String getFieldSum(int type, int idx, Vector vectLoc) {
        String str = "";
        if (vectLoc != null && vectLoc.size() > 0) {
            for (int k = 0; k < vectLoc.size(); k++) {
                // Location loc = (Location) vectLoc.get(k);
                if (idx == k) {
                    if (type == 0) {
                        str = "sum(q" + k + ")";
                    } else {
                        str = "sum(qm" + k + ")";
                    }
                    break;
                }
            }
        }
        return str;
    }

    public static Vector getReportStockLocation(SrcMinimumStock srcMinimumStock, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sqlUnion = "";
            String sql = "SELECT " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    "," + createFieldSum(srcMinimumStock.getvLocation()) + " FROM (";

            for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                Location location = (Location) srcMinimumStock.getvLocation().get(k);
                sqlUnion = sqlUnion + "SELECT location_id, material_unit_id, " +
                        createField(srcMinimumStock.getvLocation(), location.getOID(), k) +
                        " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS";

                String where = "";
                if ((srcMinimumStock.getCategoryId() != 0) || srcMinimumStock.getTextOther().length()>0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on " +
                            " ms.material_unit_id = pm.material_id ";
                    if(srcMinimumStock.getCategoryId() != 0)
                        where = " pm.category_id="+srcMinimumStock.getCategoryId();
                }

                if(srcMinimumStock.getTextOther().length()>0){
                    if(where.length()>0){
                        where = where +" and (pm.SKU like '" + srcMinimumStock.getTextOther() +"'"+
                                " or pm.NAME like '" + srcMinimumStock.getTextOther() +"')";
                    }else{
                        where = " (pm.SKU like '" + srcMinimumStock.getTextOther() +"'"+
                                " or pm.NAME like '" + srcMinimumStock.getTextOther() +"')";
                    }
                }

                if(location.getOID()!=0){
                    if(where.length()>0){
                        where = where +" and ms.location_id="+location.getOID();
                    }else{
                        where = " ms.location_id="+location.getOID();
                    }
                }

                if(srcMinimumStock.getPeriodId()!=0){
                    if(where.length()>0){
                        where = where +" and ms.periode_id = " + srcMinimumStock.getPeriodId();
                    }else{
                        where = " ms.periode_id = " + srcMinimumStock.getPeriodId();
                    }
                }

                if(where.length()>0)
                    sqlUnion = sqlUnion + " where " + where;

                if (k < (srcMinimumStock.getvLocation().size() - 1)) {
                    sqlUnion = sqlUnion + " union ";
                }
            }

            sql = sql + sqlUnion + " ) as tbl " +
                    " inner join pos_material as m on tbl.material_unit_id = m.material_id " +
                    " group by material_unit_id ";

            if (recordToGet != 0) {
                sql = sql + " limit " + start + "," + recordToGet;
            }

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vitem = new Vector();
                Vector vMin = new Vector();
                Vector vStock = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                vitem.add(material);

                // ini untuk object minimum stock
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vMin.add(String.valueOf(rs.getInt(getFieldSum(1, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vMin);

                // ini untuk object stock on hand
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vStock.add(String.valueOf(rs.getInt(getFieldSum(0, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vStock);

                list.add(vitem);
            }

        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return list;
    }


    public static int getCountReportStockLocation(SrcMinimumStock srcMinimumStock) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sqlUnion = "";
            String sql = "SELECT COUNT(" + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + ") AS CNT " +
                    " FROM (";
            for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                Location location = (Location) srcMinimumStock.getvLocation().get(k);
                sqlUnion = sqlUnion + "SELECT periode_id, material_unit_id " +
                        " FROM pos_material_stock where qty <= qty_min " +
                        " and periode_id = '" + srcMinimumStock.getPeriodId() + "'" +
                        " and location_id = '" + location.getOID() + "'";
                if (k < (srcMinimumStock.getvLocation().size() - 1)) {
                    sqlUnion = sqlUnion + " union ";
                }
            }
            sql = sql + sqlUnion + " ) as tbl " +
                    " inner join pos_material as m on tbl.material_unit_id = m.material_id " +
                    " group by periode_id ";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("CNT");
            }
        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return count;
    }
    
    /**
     * Fungsi ini digunakan untuk mendapatkan nilai stok
     * @param SrcReportStock Merupakan objek yang menampung kata kunci dalam pencarian nilai stok
     * @return Vector Merupakan objek yang menampung jumlah dari list stok dan nilai stok
     * @created by gwawan@20080117
     */
    public static Vector getStockValue(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {

            String sql = "SELECT COUNT(M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") AS COUNT_LIST" +
                    ", SUM(M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " * " +
                    "MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS STOCK_VALUE" +
                    " FROM " + (isZero ? (PstMaterial.TBL_MATERIAL + " AS M " ) : ( PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + //  MATERIAL_STOCK
                    (!isZero ? (" INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " ) : ( " INNER JOIN " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " + //CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +                    
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;

            if( srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }
            
            if(srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }
            
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }
            
            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }
            
           if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }
            
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }
            
            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            } 

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            //System.out.println("SessReportStock.getStockValue(#,#) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                result.add(String.valueOf((int)rs.getInt("COUNT_LIST")));
                result.add(String.valueOf((double)rs.getDouble("STOCK_VALUE")));
            }
            
            rs.close();
            if(isZero){
                result.setElementAt(String.valueOf(getItemCountMaterial(srcReportStock,"")), 0);
            }
        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getStockValue(#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

     /**
     * Fungsi ini digunakan untuk mendapatkan nilai stok
     * @param SrcReportStock Merupakan objek yang menampung kata kunci dalam pencarian nilai stok
     * @return Vector Merupakan objek yang menampung jumlah dari list stok dan nilai stok
     * @created by mirah@20110823
     */
    public static Vector getStockValueDistinct(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {

            String sql = "SELECT COUNT(DISTINCT M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") AS COUNT_LIST" +
                    ", SUM(M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " * " +
                    "MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS STOCK_VALUE" +
                    " FROM " + (isZero ? (PstMaterial.TBL_MATERIAL + " AS M " ) : ( PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + //  MATERIAL_STOCK
                    (!isZero ? (" INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " ) : ( " INNER JOIN " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " + //CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;

            if( srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            if(srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }

            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }

            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }

           if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }

            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }

            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            //yg length name < 2 tidak ditampilkan
            // by mirahu
            //20110822
             sql = sql + " AND LENGTH(M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " )>2";

            //System.out.println("SessReportStock.getStockValue(#,#) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result.add(String.valueOf((int)rs.getInt("COUNT_LIST")));
                result.add(String.valueOf((double)rs.getDouble("STOCK_VALUE")));
            }

            rs.close();
            if(isZero){
                result.setElementAt(String.valueOf(getItemCountMaterial(srcReportStock, "")), 0);
            }
        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getStockValue(#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static Vector getStockValueBarcode(SrcReportStock srcReportStock, boolean isZero, String whereClause) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {

            String sql = "SELECT COUNT(M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") AS COUNT_LIST" +
                    ", SUM(M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " * " +
                    "MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS STOCK_VALUE" +
                    " FROM " + (isZero ? (PstMaterial.TBL_MATERIAL + " AS M " ) : ( PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + //  MATERIAL_STOCK
                    (!isZero ? (" INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " ) : ( " INNER JOIN " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " )) + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " + //CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE+
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != 4"
                    + whereClause
                    + "";

            if( srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }

            if(srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }

            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }

//            if(srcReportStock.getCategoryId()!=0){
//                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
//            }
           //update opie-eyek 20140416
            if (srcReportStock.getCategoryId() > 0) {
                  String strGroup = " AND ( M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcReportStock.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,srcReportStock.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                sql = sql + strGroup;
            } 
            
            
           if(srcReportStock.getSku().length() != 0) {
                sql += " AND (M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'" +
                     "OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " like '%" + srcReportStock.getSku() + "%')";
            }

            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }

            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }

            //System.out.println("SessReportStock.getStockValue(#,#) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result.add(String.valueOf((int)rs.getInt("COUNT_LIST")));
                result.add(String.valueOf((double)rs.getDouble("STOCK_VALUE")));
            }

            rs.close();
            if(isZero){
                result.setElementAt(String.valueOf(getItemCountMaterial(srcReportStock, whereClause)), 0);
            }
        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getStockValue(#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }


    public static int getItemCountMaterial(SrcReportStock srcReportStock, String whereClause) {
        DBResultSet dbrs = null;
        int count=0;
        try {
            String sql = "SELECT COUNT(M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") AS COUNT_LIST" +
                    " FROM " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " + //CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;

            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }

//            if(srcReportStock.getCategoryId()!=0){
//                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
//            }
            //update opie-eyek 20140416
            if (srcReportStock.getCategoryId() > 0) {
                //buatkan seperti
                sql += " AND ( M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcReportStock.getCategoryId();
                
                String where = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]+"='"+srcReportStock.getCategoryId()+"' OR "+
                               PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"='"+srcReportStock.getCategoryId()+"'";
                
                Vector masterCatAcak = PstCategory.list(0,0,where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    Vector<Long> levelParent = new Vector<Long>();
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         sql +=" OR M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                sql +=")";
            } 
            
            if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }

            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }

            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            }
            
            //updated by dewok 2018-02-06
            if (whereClause.length() > 0) {
                sql += whereClause;
            }
 
            //System.out.println("SessReportStock.getStockValue(#,#) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                count = (int)rs.getInt("COUNT_LIST");
            }

            rs.close();
            //return count;
        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getStockValue(#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return count;
        }
    }



    public static Vector getStockValueSummary(SrcReportStock srcReportStock, boolean isZero) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT COUNT(M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") AS COUNT_LIST" +
                    ", SUM(M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " * " +
                    "MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS STOCK_VALUE" +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS " + //  MATERIAL_STOCK
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " + // MATERIAL
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " + //CATEGORY
                    " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    
                    " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if(srcReportStock.getLocationId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportStock.getLocationId();
            }
            
            if(srcReportStock.getPeriodeId() != 0) {
                sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " +srcReportStock.getPeriodeId();
            }
            
            if(srcReportStock.getMerkId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportStock.getMerkId();
            }
            
            if(srcReportStock.getCategoryId()!=0){
                sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStock.getCategoryId();
            }
            
            if(srcReportStock.getSku().length() != 0) {
                sql += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + srcReportStock.getSku() + "%'";
            }
            
            if(srcReportStock.getMaterialName().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + srcReportStock.getMaterialName() + "%'";
            }
            
            if(srcReportStock.getKsg().length() != 0) {
                sql += " AND M."  + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " like '%" + srcReportStock.getKsg() + "%'";
            } 

            if (!isZero) {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
            }
            
            sql = sql +" GROUP BY M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            
            //System.out.println("SessReportStock.getStockValue(#,#) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                result.add(String.valueOf((int)rs.getInt("COUNT_LIST")));
                result.add(String.valueOf((double)rs.getDouble("STOCK_VALUE")));
            }
            
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println("Exc on SessReportStock.getStockValue(#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
}
