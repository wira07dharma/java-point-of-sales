package com.dimata.posbo.session.warehouse;

import java.util.*;
import java.util.Date;
import java.sql.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.location.*;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
//import costing
import com.dimata.posbo.entity.masterdata.PstCosting;

public class SessReportCosting {

    public static final String SESS_SRC_REPORT_DISPATCH = "SESSION_SRC_REPORT_DISPATCH";
    public static final String SESS_SRC_REPORT_DISPATCH_INVOICE = "SESSION_SRC_REPORT_DISPATCH_INVOICE";
    public static final String SESS_SRC_REPORT_DISPATCH_REKAP = "SESSION_SRC_REPORT_DISPATCH_REKAP";

    /**
     * @param srcReportDispatch
     * @return
     * @update by
     */
    public static Vector getReportDispatch(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_INVOICE_SUPPLIER] +
                    " FROM (((" + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    ") INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportDispatch.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportDispatch.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportDispatch.getCategoryId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportDispatch.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportDispatch.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportDispatch.getSubCategoryId();
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " ORDER BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MatDispatch df = new MatDispatch();
                MatDispatchItem dfItem = new MatDispatchItem();
                Material mat = new Material();
                Unit unt = new Unit();
                MatCurrency curr = new MatCurrency();
                Location loc = new Location();

                df.setOID(rs.getLong(1));
                df.setLocationId(rs.getLong(2));
                df.setDispatchDate(rs.getDate(3));
                df.setDispatchTo(rs.getLong(4));
                df.setDispatchCode(rs.getString(5));
                df.setInvoiceSupplier(rs.getString(13));
                temp.add(df);

                dfItem.setQty(rs.getDouble(8));
                temp.add(dfItem);

                mat.setSku(rs.getString(6));
                mat.setName(rs.getString(7));
                mat.setDefaultCost(rs.getDouble(10));
                mat.setDefaultPrice(rs.getDouble(11));
                mat.setSubCategoryId(rs.getLong(12));
                temp.add(mat);

                unt.setCode(rs.getString(9));
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


    /**
     * @param srcReportDispatch
     * @return
     * @update by Edhy
     */
    public static Vector getReportDispatchRekap(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT Sum(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SumOfQTY" +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + ") AS CST" +
                    ", SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
                    " FROM (((" + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportDispatch.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportDispatch.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportDispatch.getCategoryId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportDispatch.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportDispatch.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportDispatch.getSubCategoryId();
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
            sql += " ORDER BY CAT.CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add("" + rs.getDouble(1));
                temp.add(rs.getString(2));
                temp.add(rs.getString(3));
                temp.add(new Double(rs.getDouble(4)));
                temp.add(new Double(rs.getDouble(5)));

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


    /**
     * @param srcReportDispatch
     * @return
     * @update by Edhy
     */
    public static Vector getReportDispatchRekapInvoice(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    ", SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + ") AS CST" +
                    ", SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
                    ", SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS TOT" +
                    " FROM (((" + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    ") INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportDispatch.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportDispatch.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportDispatch.getCategoryId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportDispatch.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportDispatch.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportDispatch.getSubCategoryId();
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];

            sql = sql + " ORDER BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(2)));
                temp.add(rs.getString(3));
                temp.add(new Double(rs.getDouble(4)));
                temp.add(new Double(rs.getDouble(5)));
                temp.add(new Double(rs.getDouble(6)));

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


    /**
     * @param srcReportDispatch
     * @return
     * @update by Edhy
     */
    public static Vector getReportCosting(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", SUM(DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS TOT_QTY" +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    //", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " FROM " + PstMatCosting.TBL_COSTING + " DF" +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " DFI" +
                    " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                    " ON DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " LEFT JOIN " + PstCosting.TBL_COSTING + " COST" +
                    " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] +
                    " = COST." + PstCosting.fieldNames[PstCosting.FLD_COSTING_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strCategoryId = "";
            if (srcReportDispatch.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportDispatch.getCategoryId();
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            //Add search by type Costing check Box
            //String strCostingId = "";
            //if (srcReportDispatch.getCostingId() != 0) {
                //strCostingId = " COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + " = " + srcReportDispatch.getCostingId();
            //}moment when someone says i don't think so..
            //update opie-eyek 20150130
            String strCostingId = "";
            System.err.print( "eyek "+" yul" );
                if (srcReportDispatch.getCostingId() != null && srcReportDispatch.getCostingId().size() > 0) {
                    strCostingId=strCostingId+"(";
                    int a=0;
                    for (int b = 0; b < srcReportDispatch.getCostingId().size(); b++) {
                        a=a+1;
                        if(a==1){
                            strCostingId = strCostingId + "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + " =" + srcReportDispatch.getCostingId().get(b) + ")";
                        }else{
                            strCostingId = strCostingId + " OR " + "(DF ." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + " =" + srcReportDispatch.getCostingId().get(b) + ")";
                        }
//                        if (strCostingId.length() != 0) {
//                            
//                        } else {
//                            
//                        }
		}
                    strCostingId=strCostingId+")";
               }
               //end of search by costing Type

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + startDate + " 00:00:01' AND '" + endDate + " 23:59:59'";
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            //where strCostingId
            if (strCostingId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCostingId;
                } else {
                    whereClause = whereClause + strCostingId;
                }
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }
            
            sql += " WHERE DF."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND "+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;

            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];

            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            System.out.println("SQL Lap.Pembiayaan perglobal" +sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MatDispatchItem dfItem = new MatDispatchItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();

                dfItem.setQty(rs.getDouble(3));
                temp.add(dfItem);

                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultCost(rs.getDouble(5));
                mat.setAveragePrice(rs.getDouble(6));
                mat.setCategoryId(rs.getLong(8));
                temp.add(mat);

                unt.setCode(rs.getString(4));
                temp.add(unt);

                cat.setName(rs.getString(7));
                temp.add(cat);

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

      /**
     * @param srcReportDispatch
     * @return
     * @update by Edhy
     */
    public static Vector getReportCostingTypeCosting(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", SUM(DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS TOT_QTY" +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    ", DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]+
                    ", DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]+
                    ", DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+
                    " FROM " + PstMatCosting.TBL_COSTING + " DF" +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " DFI" +
                    " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                    " ON DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " LEFT JOIN " + PstCosting.TBL_COSTING + " COST" +
                    " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] +
                    " = COST." + PstCosting.fieldNames[PstCosting.FLD_COSTING_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strCategoryId = "";
            if (srcReportDispatch.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportDispatch.getCategoryId();
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            //Add search by type Costing
            String strCostingId = "";
            //if (srcReportDispatch.getCostingId() != 0) {
                //strCostingId = " COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + " = " + srcReportDispatch.getCostingId();
            //}

             //String strCostingId = "";
              if (srcReportDispatch.getCostingId() != null && srcReportDispatch.getCostingId().size() > 0) {
                    int a=0;
                    strCostingId=strCostingId+" ( ";
                    for (int b = 0; b < srcReportDispatch.getCostingId().size(); b++) {
                        a=a+1;
                        if(a==1){
                            strCostingId =strCostingId + " (DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + " =" + srcReportDispatch.getCostingId().get(b) + ") ";
                        }else{
                            strCostingId = strCostingId + " OR " + "(DF ." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + " =" + srcReportDispatch.getCostingId().get(b) + ") ";
                        }
		}
                 strCostingId=strCostingId+" ) ";   
               }

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + startDate + " 00:00:01' AND '" + endDate + " 23:59:59'";
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            //where strCostingId
            if (strCostingId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCostingId;
                } else {
                    whereClause = whereClause + strCostingId;
                }
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            sql += " WHERE DF."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND "+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;

            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];

            sql = sql + " ORDER BY DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    ", DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];

            System.out.println("SQL Lap.Pembiayaan pernota" +sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MatCostingItem costItem = new MatCostingItem();
                MatCosting matCost = new MatCosting();
                //MatDispatchItem dfItem = new MatDispatchItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();

                costItem.setQty(rs.getDouble(3));
                temp.add(costItem);

                matCost.setOID(rs.getLong(9));
                matCost.setCostingCode(rs.getString(10));
                matCost.setCostingDate(rs.getDate(11));
                temp.add(matCost);

                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultCost(rs.getDouble(5));
                mat.setAveragePrice(rs.getDouble(6));
                mat.setCategoryId(rs.getLong(8));
                temp.add(mat);

                unt.setCode(rs.getString(4));
                temp.add(unt);

                cat.setName(rs.getString(7));
                temp.add(cat);

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



    /**
     * get data for report dispatch per category -- used in Tegalsari
     * @param srcReportDispatch
     * @return
     * @created by Edhy
     */
    public static Vector getReportDispatchPerCategory(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS TOT_QTY" +
                    ", SUM(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " * DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ")" +
                    " FROM ((((" + PstMatDispatch.TBL_DISPATCH + " AS DF" +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS DFI" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + ")" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
                    " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS LOC" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ")" +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")" +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SCAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportDispatch.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportDispatch.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportDispatch.getCategoryId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportDispatch.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportDispatch.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportDispatch.getSubCategoryId();
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];
            sql = sql + " ORDER BY SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];

            //System.out.println("getReportDispatchPerCategory sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(new Long(rs.getLong(1)));
                temp.add(rs.getString(2));
                temp.add(rs.getString(3));
                temp.add(new Double(rs.getDouble(4)));
                temp.add(new Double(rs.getDouble(5)));

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

/*
SELECT SCAT.SUB_CATEGORY_ID, SCAT.CODE, SCAT.NAME, SUM(DFI.QTY) AS TOT_QTY, SUM(MAT.DEFAULT_COST)
FROM ((((POS_DISPATCH_MATERIAL DF
INNER JOIN POS_DISPATCH_MATERIAL_ITEM DFI ON DF.DISPATCH_MATERIAL_ID = DFI.DISPATCH_MATERIAL_ID )
INNER JOIN POS_MATERIAL MAT ON DFI.MATERIAL_ID = MAT.MATERIAL_ID )
INNER JOIN LOCATION LOC ON DF.DISPATCH_TO = LOC.LOCATION_ID )
INNER JOIN POS_CATEGORY CAT ON MAT.CATEGORY_ID = CAT.CATEGORY_ID )
INNER JOIN POS_SUB_CATEGORY SCAT ON MAT.SUB_CATEGORY_ID = SCAT.SUB_CATEGORY_ID
WHERE  DF.LOCATION_ID = 1 AND  MAT.CATEGORY_ID = 2 AND  DF.DISPATCH_DATE BETWEEN '2004-03-01' AND '2004-03-24'
AND  DF.DISPATCH_TO = 504404206834238270
GROUP BY SCAT.SUB_CATEGORY_ID
ORDER BY SCAT.CODE
*/


    //Laporan nilai apa yg diinginkan
    public static final int INFO_TYPE_PRICE = 0;
    public static final int INFO_TYPE_COST = 1;
    public static final int INFO_TYPE_QTY = 2;

    //Untuk menampilkan total pengiriman harian di lokasi tertentu per tanggal
    /**
     * @param srcReportDispatch
     * @param reportType
     * @return
     * @update by Edhy
     */
    public static Vector getReportDispatchRekapTanggalKategori(SrcReportDispatch srcReportDispatch, int reportType) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //List semua kategori order by kode
            Vector listKategori = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CODE]);
            String sql = "SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID];

            String strLocationId = "";
            if (srcReportDispatch.getLocationId() != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId();
            }

            String strDate = "";
            if ((srcReportDispatch.getDateFrom() != null) && (srcReportDispatch.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportDispatch.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportDispatch.getDateTo(), "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strDispatchTo = "";
            if (srcReportDispatch.getDispatchTo() != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + srcReportDispatch.getDispatchTo();
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE];
            sql += " ORDER BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE];

            //System.out.println(sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                for (int i = 0; i < listKategori.size(); i++) {
                    //Count total Dispatch for each category
                    Category cat = (Category) listKategori.get(i);
                    long oidCategory = cat.getOID();
                    switch (reportType) {
                        case INFO_TYPE_PRICE://Price
                            temp.add(new Double(sumCategoryDispatchPrice(srcReportDispatch.getLocationId(), rs.getDate(1),
                                    oidCategory, srcReportDispatch.getDispatchTo(), koneksi)));
                            break;
                        case INFO_TYPE_COST://Cost
                            temp.add(new Double(sumCategoryDispatchCost(srcReportDispatch.getLocationId(), rs.getDate(1),
                                    oidCategory, srcReportDispatch.getDispatchTo(), koneksi)));
                            break;
                        case INFO_TYPE_QTY://Qty
                            temp.add(new Double(sumCategoryDispatchQty(srcReportDispatch.getLocationId(), rs.getDate(1),
                                    oidCategory, srcReportDispatch.getDispatchTo(), koneksi)));
                            break;
                    }
                }
                //System.out.println(temp);
                result.add(temp);
            }
            rs.close();
            stmt.close();
            PstMaterialStock.closeLocalConnection(koneksi);

        } catch (Exception e) {
            System.out.println("Err 1 : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    //Menghitung total dispatch per kategori dlm suatu range tertentu by price
    /**
     * @param oidLocation
     * @param filterDate
     * @param oidCategory
     * @param oidDispatchTo
     * @param koneksi
     * @return
     * @update by Edhy
     */
    private static double sumCategoryDispatchPrice(long oidLocation, Date filterDate, long oidCategory
                                                   , long oidDispatchTo, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SumOfTOTAL_PRICE" +
                    " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " ) INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID];

            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocation;
            }

            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }

            String strDispatchTo = "";
            if (oidDispatchTo != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidDispatchTo;
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err 2 : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }


    //Menghitung total dispatch per kategori dlm suatu range tertentu by cost
    /**
     * @param oidLocation
     * @param filterDate
     * @param oidCategory
     * @param oidDispatchTo
     * @param koneksi
     * @return
     * @update by Edhy
     */
    private static double sumCategoryDispatchCost(long oidLocation, Date filterDate, long oidCategory
                                                  , long oidDispatchTo, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + ") AS SumOfTOTAL_COST" +
                    " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " ) INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID];

            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocation;
            }

            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }

            String strDispatchTo = "";
            if (oidDispatchTo != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidDispatchTo;
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err 2 : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }


    //Menghitung total dispatch per kategori dlm suatu range tertentu by qty
    /**
     * @param oidLocation
     * @param filterDate
     * @param oidCategory
     * @param oidDispatchTo
     * @param koneksi
     * @return
     * @update by Edhy
     */
    private static double sumCategoryDispatchQty(long oidLocation, Date filterDate, long oidCategory
                                              , long oidDispatchTo, Connection koneksi) {
        double hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SumOfTOTAL_QTY" +
                    " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " ) INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID];

            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocation;
            }

            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                strDate = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }

            String strDispatchTo = "";
            if (oidDispatchTo != 0) {
                strDispatchTo = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidDispatchTo;
            }

            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                } else {
                    whereClause = whereClause + strCategoryId;
                }
            }

            if (strDispatchTo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDispatchTo;
                } else {
                    whereClause = whereClause + strDispatchTo;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Err 2 : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }


    /**
     * Pencarian rekap pengiriman barang
     */
    public static Vector getReportRekapDispatch(SrcReportDispatch srcReportDispatch) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ",M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ",SUM(DI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") SUM_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + // QTY
                    ",U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + // CODE
                    ",M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ",M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ",CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    ",CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ",L." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LOC_" + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " FROM " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS DI " + // DISPATCH_MATERIAL_ITEM
                    " INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " AS DM ON " + // DISPATCH_MATERIAL
                    " DI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " =DM." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] + // DISPATCH_MATERIAL_ID
                    " LEFT JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " AS RM ON " + // RECEIVE_MATERIAL
                    " DM." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_INVOICE_SUPPLIER] +
                    " =RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + // INVOICE_SUPPLIER
                    " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS L ON " + // LOCATION
                    " DM." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
                    " =L." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION ID
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL ON " + // CONTACT_LIST
                    " RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
                    " =CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + // MATERIAL
                    " AS M ON DI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " =M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT +
                    " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + // DEFAULT_STOCK_UNIT_ID
                    " =U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID
                    " WHERE DM." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcReportDispatch.getLocationId(); // LOCATION_ID

            if (srcReportDispatch.getDispatchTo() != 0) {
                sql = sql + " AND DM." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + srcReportDispatch.getDispatchTo(); // DISPATCH_TO
            }

            if (srcReportDispatch.getSupplierId() != 0) {
                sql = sql + " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + "=" + srcReportDispatch.getSupplierId(); // SUPPLIER_ID
            }

            sql = sql + " GROUP BY " +
                    " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + // SKU
                    ",M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + // NAME
                    ",U." + PstUnit.fieldNames[PstUnit.FLD_CODE] + //CODE
                    ",M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + // DEFAULT_COST
                    ",M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + // DEFAULT_PRICE
                    ",CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    ",CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ",L." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ORDER BY RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector rx = new Vector();
                Material mat = new Material();
                Unit unit = new Unit();
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                ContactList contactList = new ContactList();
                Location location = new Location();

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                rx.add(mat);

                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                rx.add(unit);

                matDispatchItem.setQty(rs.getDouble("SUM_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]));
                rx.add(matDispatchItem);

                contactList.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                rx.add(contactList);

                location.setName(rs.getString("LOC_" + PstLocation.fieldNames[PstLocation.FLD_NAME]));
                rx.add(location);

                list.add(rx);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("ERR GET REKAP =>> " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }
}
