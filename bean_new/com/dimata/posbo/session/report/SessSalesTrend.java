/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.report;

import com.dimata.harisma.entity.employee.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.db.*;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class SessSalesTrend {

    public static Vector getListBerat(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT DISTINCT(cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BERAT] + ")"
                    + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billdetail = new Billdetail();
                billdetail.setBerat(rs.getDouble(1));
                lists.add(billdetail);
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

    public static Vector getListCustomer(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT DISTINCT(mat." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + ")"
                    + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS mat "
                    + " ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Material material = new Material();
                material.setSupplierId(rs.getLong(1));
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

    public static Vector getListCategory(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT DISTINCT(mat." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + ")"
                    + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS mat "
                    + " ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Material material = new Material();
                material.setCategoryId(rs.getLong(1));
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

    public static Vector getListSales(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT DISTINCT(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + ")"
                    + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstSales.TBL_SALES + " AS sales "
                    + " ON sales." + PstSales.fieldNames[PstSales.FLD_CODE]
                    + " = cbm." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS emp "
                    + " ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = sales." + PstSales.fieldNames[PstSales.FLD_EMPLOYEE_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BillMain billMain = new BillMain();
                billMain.setSalesCode(rs.getString(1));
                lists.add(billMain);
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

    public static int getCountCustomer(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT COUNT(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + ")"
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm "
                    + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS mat "
                    + " ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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

    public static int getCountQtyItem(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT SUM(cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ")"
                    + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS mat "
                    + " ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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

    public static double getTotalSale(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT "
                    + " SUM("
                    + " IF("
                    + " cbd.total_price = (cbm.amount + cbm.disc), "
                    + " ("
                    + " (cbd.total_price - ("
                    + " ("
                    + " (cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)"
                    + " )"
                    + " )"
                    + " ) * cbm.rate"
                    + " ),"
                    + " ("
                    + " ("
                    + " cbd.total_price - ("
                    + " ("
                    + " (cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)"
                    + " )"
                    + " )"
                    + " ) * cbm.rate"
                    + " )"
                    + " )"
                    + " ) AS cbd_total "
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm"
                    + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS mat "
                    + " ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

}
