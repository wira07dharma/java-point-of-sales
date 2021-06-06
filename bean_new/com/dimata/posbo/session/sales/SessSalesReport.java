/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.sales;

import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.qdep.db.*;
import java.sql.*;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class SessSalesReport {

    private String dateStart = "";
    private String dateEnd = "";
    private String customerName = "";
    private String itemName = "";
    private String itemSku = "";
    private String itemCategoryCode = "";

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public static ArrayList listSales(SessSalesReport salesReport) {
        ArrayList arrayList = new ArrayList();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + "  DATE(cbm.BILL_DATE) AS 'DATE', "
                    + "  TIME(cbm.BILL_DATE) AS 'TIME', "
                    + "  cbm.BILL_NUMBER AS 'INVOICE', "
                    + "  cl.PERSON_NAME AS 'NAME', "
                    + "  dm.JENIS_DOKUMEN AS 'JENIS_DOKUMEN', "
                    + "  dm.NOMOR_BC AS 'NOMOR_DOKUMEN', "
                    + "  cl.PASSPORT_NO AS 'NOMOR_PASSPORT', "
                    + "  cat.CODE AS 'KODE_BARANG', "
                    + "  m.SKU AS 'SERI_BARANG', "
                    + "  m.NAME AS 'NAMA_BARANG', "
                    + "  cbd.QTY AS 'JUMLAH', "
                    + "  u.CODE AS 'SATUAN' "
                    + " FROM "
                    + "  cash_bill_main AS cbm "
                    + "  INNER JOIN contact_list AS cl "
                    + "    ON cl.CONTACT_ID = cbm.CUSTOMER_ID "
                    + "  INNER JOIN pos_dispatch_material_bill AS dmb "
                    + "    ON dmb.CASH_BILL_MAIN_ID = cbm.CASH_BILL_MAIN_ID "
                    + "  INNER JOIN pos_dispatch_material AS dm "
                    + "    ON dm.DISPATCH_MATERIAL_ID = dmb.DISPATCH_MATERIAL_ITEM_ID "
                    + "  INNER JOIN cash_bill_detail AS cbd "
                    + "    ON cbd.CASH_BILL_MAIN_ID = cbm.CASH_BILL_MAIN_ID "
                    + "  INNER JOIN pos_material AS m "
                    + "    ON m.MATERIAL_ID = cbd.MATERIAL_ID "
                    + "  INNER JOIN pos_category AS cat "
                    + "    ON cat.CATEGORY_ID = m.CATEGORY_ID "
                    + "  INNER JOIN pos_unit AS u "
                    + "    ON u.UNIT_ID = m.BUY_UNIT_ID ";

            String where = " WHERE ("
                    + " ("
                    + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                    + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH
                    + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                    + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                    + ") OR ("
                    + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                    + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
                    + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                    + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                    + " )"
                    + ") ";

            if (salesReport.getDateStart() != null && !salesReport.getDateStart().isEmpty()) {
                where += where.isEmpty() ? "" : " AND ";
                where += " cbm.BILL_DATE >= '" + salesReport.getDateStart() + " 00:00:00'";
            }

            if (salesReport.getDateEnd() != null && !salesReport.getDateEnd().isEmpty()) {
                where += where.isEmpty() ? "" : " AND ";
                where += " cbm.BILL_DATE <= '" + salesReport.getDateEnd() + " 23:59:59'";
            }

            if (salesReport.getItemCategoryCode() != null && !salesReport.getItemCategoryCode().isEmpty()) {
                where += where.isEmpty() ? "" : " AND ";
                where += " cat.CODE = '" + salesReport.getItemCategoryCode() + "'";
            }

            if (salesReport.getItemSku() != null && !salesReport.getItemSku().isEmpty()) {
                where += where.isEmpty() ? "" : " AND ";
                where += " m.SKU = '" + salesReport.getItemSku() + "'";
            }

            if (salesReport.getCustomerName() != null && !salesReport.getCustomerName().isEmpty()) {
                where += where.isEmpty() ? "" : " AND ";
                where += " cl.PERSON_NAME = '" + salesReport.getCustomerName() + "'";
            }

            sql += where;

            sql += ""
                    + " ORDER BY cbm.BILL_DATE DESC, "
                    + "  cbm.BILL_NUMBER DESC"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JSONObject object = new JSONObject();
                object.put("DATE", rs.getString("DATE"));
                object.put("TIME", rs.getString("TIME"));
                object.put("INVOICE", rs.getString("INVOICE"));
                object.put("NAME", rs.getString("NAME"));
                object.put("JENIS_DOKUMEN", rs.getString("JENIS_DOKUMEN"));
                object.put("NOMOR_DOKUMEN", rs.getString("NOMOR_DOKUMEN"));
                object.put("NOMOR_PASSPORT", rs.getString("NOMOR_PASSPORT"));
                object.put("KODE_BARANG", rs.getString("KODE_BARANG"));
                object.put("SERI_BARANG", rs.getString("SERI_BARANG"));
                object.put("NAMA_BARANG", rs.getString("NAMA_BARANG"));
                object.put("JUMLAH", rs.getDouble("JUMLAH"));
                object.put("SATUAN", rs.getString("SATUAN"));

                arrayList.add(object);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return arrayList;
    }
}
