package com.dimata.posbo.session.warehouse;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*; 
import javax.servlet.*;
import javax.servlet.http.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.location.*;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.admin.*;  
import com.dimata.posbo.entity.masterdata.*;   
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;

//adding import for reposting stok
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.PstMatReceive;

public class SessReportSale {

    public static final String SESS_SRC_REPORT_SALE = "SESSION_SRC_REPORT_SALE";
    public static final String SESS_SRC_REPORT_SALE_REKAP = "SESSION_SRC_REPORT_SALE_REKAP";
    public static final String SESS_SRC_REPORT_SALE_REKAP_KATEGORI = "SESSION_SRC_REPORT_SALE_KATEGORI";
    public static final String SESS_SRC_REPORT_SALE_REKAP_SUPPLIER = "SESSION_SRC_REPORT_SALE_SUPPLIER";
    public static final String SESS_SRC_REPORT_SALE_CASH = "SESSION_SRC_REPORT_SALE_CASH";
    public static final String SESS_SRC_REPORT_SALE_CC = "SESSION_SRC_REPORT_SALE_CC";
    public static final String SESS_SRC_REPORT_SALE_GRAFIK_BARANG = "SESSION_SRC_REPORT_SALE_GRAFIK_BARANG";
    public static final String SESS_SRC_REPORT_SALE_GRAFIK_KATEGORI = "SESSION_SRC_REPORT_SALE_GRAFIK_KATEGORI";

    /** ary, gadnyana update
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSale(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC_TYPE] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " AS MAT_" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " AS LOC_" + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] +
                    " AS USR_" + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] +
                    ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " AS CNT_" + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] +
                    ", SLS." + PstSales.fieldNames[PstSales.FLD_NAME] + // sales_name" +
                    " AS SLS_" + PstSales.fieldNames[PstSales.FLD_NAME] + // sales_name" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " FROM ((((((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //CASH_BILL_MAIN_ID" +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + //  MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + // APP_USER_ID" +
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID
                    " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID" +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + //UNIT_ID" +
                    " ) LEFT JOIN " + PstSales.TBL_SALES + " SLS" + // sales_person
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + // SALES_CODE" +
                    " = SLS." + PstSales.fieldNames[PstSales.FLD_CODE]; // SALES_CODE";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String strDocType = "";
            if (srcReportSale.getDocType() >= 0) {
                int[] transMode = getTransMode(srcReportSale);
                strDocType = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] + " " +
                        " AND " +
                        " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] + " " +
                        " AND " +
                        " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] + " ";

            }
            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (strDocType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDocType;
                } else {
                    whereClause = whereClause + strDocType;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] +
                            " ,USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] +
                            " ,CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //CASH_BILL_MAIN_ID"+
                            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            //System.out.println("SALE SQl : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                BillMain cbm = new BillMain();
                Billdetail cbd = new Billdetail();
                Material mat = new Material();
                Unit unt = new Unit();
                Location loc = new Location();
                AppUser usr = new AppUser();
                ContactList cnt = new ContactList();
                Sales sls = new Sales();

                cbm.setOID(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                cbm.setLocationId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]));
                cbm.setAppUserId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]));
                cbm.setShiftId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]));
                cbm.setBillDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                cbm.setInvoiceNo(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                cbm.setDiscType(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]));
                cbm.setDiscount(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT]));

                cbm.setServiceValue(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE]));
                cbm.setTaxValue(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE]));
                cbm.setInvoiceNumber(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                temp.add(cbm);

                cbd.setItemPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
                cbd.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
                cbd.setDisc(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]));
                cbd.setDiscType(rs.getInt(PstBillDetail.fieldNames[PstBillDetail.FLD_DISC_TYPE]));
                cbd.setTotalPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]));
                cbd.setCost(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_COST]));
                temp.add(cbd);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString("MAT_" + PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                temp.add(mat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                loc.setName(rs.getString("LOC_" + PstLocation.fieldNames[PstLocation.FLD_NAME]));
                temp.add(loc);

                usr.setFullName(rs.getString("USR_" + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
                temp.add(usr);

                cnt.setCompName(rs.getString("CNT_" + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                temp.add(cnt);

                sls.setName(rs.getString("SLS_" + PstSales.fieldNames[PstSales.FLD_NAME]));
                temp.add(sls);


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

    /** ary
     * untuk memncari jenis pembayaran per invoice
     * @param oidBillMain
     * @return
     */
    public static Vector getPayments(long oidBillMain) {
        String whereClause = PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + "=" + oidBillMain;
        Vector vtBillMain = PstCashPayment.list(0, 0, whereClause, "");
        return vtBillMain;
    }

    /** ary, gadnyana
     * Untuk menampilkan total penjualan harian di lokasi tertentu per tanggal
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapTanggal(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SUM_PRICE" + // TOTAL_PRICE
                    ",0 AS SUM_DISC " +
                    ",0 AS SUM_SERVICE " +
                    ",0 AS SUM_TAX " +
                    ",SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS HPP" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID+
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" PM"
                    + " ON CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+" = "
                    + " PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+""; // CASH_BILL_MAIN_ID;

            String strLocationId = "";
            if (srcReportSale.getLocationMultiple().length()>0){
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN (" + srcReportSale.getLocationMultiple()+")";
            }else{
                if (srcReportSale.getLocationId() != 0) {
                    strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
                }
            }
            

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd 23:59:59");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereCategoryId = "";
            String whereCategoryTemp="";
            //if (srcReportSale.getCategoryId()!=0){
            //    whereCategoryId = " PM."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+srcReportSale.getCategoryId()+"'";
            //}
            if (srcReportSale.getCategoryMultiple().length()>0){
                String tempText[] = srcReportSale.getCategoryMultiple().split(",");
                for (int i=0; i <tempText.length;i++){
                    if (whereCategoryTemp.length()>0){
                        whereCategoryTemp = whereCategoryTemp + " OR " + " PM."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+tempText[i]+"'";
                    }else{
                        whereCategoryTemp = " PM."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+tempText[i]+"'";
                    }
                }
                
                if (whereCategoryTemp.length()>0){
                    whereCategoryId = "( " + whereCategoryTemp + " )";
                }
            }

            String whereClause = "";
            
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
                //added transaction type and transaction status
                whereClause = whereClause +  " AND(( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH+
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE + ")";
                whereClause = whereClause +  " OR( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT +
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_TYPE_CREDIT + "))";

            } else {
                whereClause = whereClause + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
                 //added transaction type and transaction status
                whereClause = whereClause +  " AND(( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH+
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE + ")";
                whereClause = whereClause +  " OR( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT +
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_TYPE_CREDIT + "))";
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause; 
            }
            
            if (whereCategoryId.length()>0){
                if (whereClause.length() > 0) {
                    sql = sql + " AND " + whereCategoryId;
                } else {
                    sql = sql + "WHERE " + whereCategoryId;
                }
            }

            //sql += " GROUP BY  CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";

             //add Group by for search all location
            if (strLocationId.length() > 0) {
                sql += " GROUP BY  CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";
            } else {
                sql += " GROUP BY LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";
            }
            //update opie 25-06-2012 biar nilai tax tidak double
            String sql2 = " union SELECT 0 AS SUM_PRICE" +
                    ",SUM(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] + ") AS SUM_DISC " +
                    ",SUM(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] + ") AS SUM_SERVICE " +
                    ",SUM(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] + ") AS SUM_TAX " +
                    ", 0 AS HPP" +
                    ", TAX." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    " FROM (SELECT DISTINCT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID+
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstMaterial.TBL_MATERIAL+" PM"
                    + " ON CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+" = "
                    + " PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+""; // CASH_BILL_MAIN_ID;
            
            if (whereClause.length() > 0) {  
                sql2 = sql2 + " WHERE " + whereClause;
            }
            
            if (whereCategoryId.length()>0){
                if (whereClause.length() > 0) {
                    sql2 = sql2 + " AND " + whereCategoryId;
                } else {
                    sql2 = sql2 + "WHERE " + whereCategoryId;
                }
            }
            
            //sql2 += " GROUP BY  CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";

            //add Group by for search all location
            //update opie 25-06-2012 biar nilai tax tidak double
            if (strLocationId.length() > 0) {
                sql2 += " ) AS TAX GROUP BY  TAX." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";
            } else {
                sql2 += " ) AS TAX GROUP BY LEFT(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";
            }

            sql = "select sum(SUM_PRICE) as SUM_PRICE,sum(SUM_DISC) as SUM_DISC,sum(SUM_SERVICE) as SUM_SERVICE,sum(SUM_TAX) as SUM_TAX, sum(HPP) as HPP," + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " from (" + sql + " " + sql2 + ") as tbl group by LEFT (" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ ",10)";

            System.out.println("SALE SQL ############: " + sql);
            dbrs = DBHandler.execQueryResult(sql); 
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {  
                Vector temp = new Vector();

                temp.add(Formater.formatDate(rs.getDate(6), "dd-MM-yyyy"));
                //temp.add(new Double((((rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")) + rs.getDouble("SUM_SERVICE")) + rs.getDouble("SUM_TAX"))));
                //temp.add(new Double(rs.getDouble("HPP")));
                //temp.add(new Double((((rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")) + rs.getDouble("SUM_SERVICE")) + rs.getDouble("SUM_TAX")) - rs.getDouble("HPP")));
                temp.add(new Double(rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")));
                temp.add(new Double(rs.getDouble("HPP")));
                temp.add(new Double((rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")) - rs.getDouble("HPP")));
                
                // tax
                temp.add(new Double(rs.getDouble("SUM_TAX")));
                // service
                temp.add(new Double(rs.getDouble("SUM_SERVICE")));
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
     * add opie-eyek agar bisa di group by location
     * @param srcReportSale
     * @return 
     */
    public static Hashtable getReportSaleRekapTanggalPerLocation(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        //Vector result = new Vector(1, 1);
        Hashtable hashRekapPerLocation= new Hashtable();
        try {
            String sql = "SELECT SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SUM_PRICE" + // TOTAL_PRICE
                    ",0 AS SUM_DISC " +
                    ",0 AS SUM_SERVICE " +
                    ",0 AS SUM_TAX " +
                    ",SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS HPP" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // BILL_DATE+
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID+
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" PM"
                    + " ON CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+" = "
                    + " PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+""; // CASH_BILL_MAIN_ID;

            String whereLokasiTemp = "";
            String strLocationId="";
//            if (srcReportSale.getLocationId() != 0) {
//                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
//            }

            if (srcReportSale.getLocationMultiple().length()>0){
                String tempText[] = srcReportSale.getLocationMultiple().split(",");
                for (int i=0; i <tempText.length;i++){
                    if (whereLokasiTemp.length()>0){
                        whereLokasiTemp = whereLokasiTemp + " OR " + "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +"='"+tempText[i]+"'";
                    }else{
                        whereLokasiTemp = "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +"='"+tempText[i]+"'";
                    }
                }
                
                if (whereLokasiTemp.length()>0){
                    strLocationId = " ( " + whereLokasiTemp + " ) ";
                }
            }


            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd 23:59:59");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereCategoryId = "";
            String whereCategoryTemp="";
            //if (srcReportSale.getCategoryId()!=0){
            //    whereCategoryId = " PM."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+srcReportSale.getCategoryId()+"'";
            //}
            if (srcReportSale.getCategoryMultiple().length()>0){
                String tempText[] = srcReportSale.getCategoryMultiple().split(",");
                for (int i=0; i <tempText.length;i++){
                    if (whereCategoryTemp.length()>0){
                        whereCategoryTemp = whereCategoryTemp + " OR " + " PM."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+tempText[i]+"'";
                    }else{
                        whereCategoryTemp = " PM."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+tempText[i]+"'";
                    }
                }
                
                if (whereCategoryTemp.length()>0){
                    whereCategoryId = "( " + whereCategoryTemp + " )";
                }
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
                //added transaction type and transaction status
                whereClause = whereClause +  " AND(( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH+
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE + ")";
                whereClause = whereClause +  " OR( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT +
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_TYPE_CREDIT + "))";

            } else {
                whereClause = whereClause + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
                 //added transaction type and transaction status
                whereClause = whereClause +  " AND(( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH+
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE + ")";
                whereClause = whereClause +  " OR( " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT +
                              " AND " + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_TYPE_CREDIT + "))";
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause; 
            }
            
            if (whereCategoryId.length()>0){
                if (whereClause.length() > 0) {
                    sql = sql + " AND " + whereCategoryId;
                } else {
                    sql = sql + "WHERE " + whereCategoryId;
                }
            }

            //sql += " GROUP BY  CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";

             //add Group by for search all location
            if (strLocationId.length() > 0) {
                sql += " GROUP BY  CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";
            } else {
                sql += " GROUP BY LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10), CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
            }
            //update opie 25-06-2012 biar nilai tax tidak double
            String sql2 = " union SELECT 0 AS SUM_PRICE" +
                    ",SUM(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] + ") AS SUM_DISC " +
                    ",SUM(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] + ") AS SUM_SERVICE " +
                    ",SUM(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] + ") AS SUM_TAX " +
                    ", 0 AS HPP" +
                    ", TAX." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    ", TAX." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // BILL_DATE+
                    " FROM (SELECT DISTINCT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID+
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstMaterial.TBL_MATERIAL+" PM"
                    + " ON CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+" = "
                    + " PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+""; // CASH_BILL_MAIN_ID;
            
            if (whereClause.length() > 0) {  
                sql2 = sql2 + " WHERE " + whereClause;
            }
            
            if (whereCategoryId.length()>0){
                if (whereClause.length() > 0) {
                    sql2 = sql2 + " AND " + whereCategoryId;
                } else {
                    sql2 = sql2 + "WHERE " + whereCategoryId;
                }
            }
            
            //sql2 += " GROUP BY  CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";

            //add Group by for search all location
            //update opie 25-06-2012 biar nilai tax tidak double
            if (strLocationId.length() > 0) {
                sql2 += " ) AS TAX GROUP BY  TAX." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+", LEFT(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10)";
            } else {
                sql2 += " ) AS TAX GROUP BY LEFT(TAX." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+",10), TAX." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
            }

            sql = "select sum(SUM_PRICE) as SUM_PRICE,sum(SUM_DISC) as SUM_DISC,sum(SUM_SERVICE) as SUM_SERVICE,sum(SUM_TAX) as SUM_TAX, sum(HPP) as HPP," +
                    PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +","+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " from (" + sql + " " + sql2 + ") as tbl group by LEFT (" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ ",10), LOCATION_ID";

            System.out.println("SALE SQL ############: " + sql);
            dbrs = DBHandler.execQueryResult(sql); 
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {  
                Vector temp = new Vector();

                temp.add(Formater.formatDate(rs.getDate(6), "dd-MM-yyyy"));
                //temp.add(new Double((((rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")) + rs.getDouble("SUM_SERVICE")) + rs.getDouble("SUM_TAX"))));
                //temp.add(new Double(rs.getDouble("HPP")));
                //temp.add(new Double((((rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")) + rs.getDouble("SUM_SERVICE")) + rs.getDouble("SUM_TAX")) - rs.getDouble("HPP")));
                temp.add(new Double(rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")));
                temp.add(new Double(rs.getDouble("HPP")));
                temp.add(new Double((rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")) - rs.getDouble("HPP")));
                
                // tax
                temp.add(new Double(rs.getDouble("SUM_TAX")));
                // service
                temp.add(new Double(rs.getDouble("SUM_SERVICE")));
                //esult.add(temp); 
                hashRekapPerLocation.put(""+rs.getLong("LOCATION_ID")+"-"+Formater.formatDate(rs.getDate(6), "dd-MM-yyyy"), temp);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashRekapPerLocation;
    }

    /** ary, gadnyana
     * Untuk menampilkan total penjualan harian di lokasi tertentu per tanggal
     * sesuai kategori terpilih dan digroup by sub kategorinya
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapKategori(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            /*String sql = "SELECT "+
            " SUM(CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS SumOfQTY" + // QTY
            ", SUM(CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]+") AS SumOfTOTAL_PRICE" + // TOTAL_PRICE
            ", SUM(CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+ // QTY
            " * CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+") AS HPP" + // COST
            ", SUM((CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+ // QTY+
            " * CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]+ // ITEM_PRICE+
            ") - CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+") AS DISC" + // TOTAL_PRICE
            ", CAT."+PstCategory.fieldNames[PstCategory.FLD_NAME]+" AS NM_CATEG "+ // NAME" +
            ", SCAT."+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]+" AS NM_SUB_CATEG "+ // name
            ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+ // SUB_CATEGORY_ID" +
            " FROM ((("+PstCategory.TBL_CATEGORY+" CAT" + // category
            " INNER JOIN "+PstSubCategory.TBL_SUB_CATEGORY+" SCAT" + // sub_category
            " ON CAT."+PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+ // CATEGORY_ID
            " = SCAT."+PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID]+ // CATEGORY_ID+
            " ) INNER JOIN "+PstMaterial.TBL_MATERIAL+" MAT" + // material
            " ON SCAT."+PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID]+ //SUB_CATEGORY_ID+
            " = MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+ // SUB_CATEGORY_ID+
            " ) LEFT JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL+" CBD" + // cash_bill_detail
            " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ // MATERIAL_ID+
            " = CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+ // MATERIAL_ID+
            " ) LEFT JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" CBM" + //  cash_bill_main
            " ON CBD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+ // CASH_BILL_MAIN_ID+
            " = CBM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID;*/

            String sql = "SELECT " +
                    " SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SumOfQTY" + // QTY
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SumOfTOTAL_PRICE" + // TOTAL_PRICE
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS HPP" + // COST
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY+
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE+
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS NM_CATEG " + // NAME" +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " AS NM_SUB_CATEG " + // name
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    " FROM " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + //SUB_CATEGORY_ID+
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID+
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + //CATEGORY_ID+
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID+
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + //  cash_bill_main
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID+
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID;

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

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

            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME+
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME+
                    " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] + // CODE+
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]; // CODE";

            //System.out.println("=>> SQL :" + sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(new Double(rs.getDouble("SumOfQTY")));
                temp.add(new Double(rs.getDouble("SumOfTOTAL_PRICE")));
                temp.add(new Double(rs.getDouble("HPP")));
                temp.add(new Double(rs.getDouble("DISC")));
                temp.add(rs.getString("NM_CATEG"));
                temp.add(rs.getString("NM_SUB_CATEG"));
                temp.add(new Integer(countItemSubCategory(koneksi, rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]))));

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

    /**
     * untuk mencari jenis pembayaran dari invoice yang terselect dan
     * yang di cari berdasarkan nobill.
     */
    public static Vector getReportPayments(Vector vtnobill) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " CUR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] +
                    " ,SUM(PAY." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] + ") AS SUM_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] +
                    " FROM " + PstCashPayment.TBL_PAYMENT + " AS PAY " +
                    " INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " AS CUR ON " +
                    " PAY." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID] + " = CUR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID];

            String whereClause = "";
            if (vtnobill != null && vtnobill.size() > 0) {
                for (int k = 0; k < vtnobill.size(); k++) {
                    if (whereClause.length() == 0) {
                        whereClause = "PAY." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + "=" + vtnobill.get(k);
                    } else {
                        whereClause = whereClause + " OR " + "PAY." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + "=" + vtnobill.get(k);
                    }
                }
                whereClause = "(" + whereClause + ")";
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " GROUP BY PAY." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MatCurrency matCurrency = new MatCurrency();
                CashPayments cashPayments = new CashPayments();

                matCurrency.setCode(rs.getString(PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE]));
                temp.add(matCurrency);

                cashPayments.setAmount(rs.getDouble("SUM_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                temp.add(cashPayments);

                result.add(temp);

            }
            rs.close();
        } catch (Exception e) {
            System.out.println("ERROR QUERY >>> " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //Untuk menampilkan total pembayaran dengan valas
    public static Vector getReportSaleRekapCash(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE] + // RATE+
                    ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] + // AMOUNT+
                    ", (CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE] + " * CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] + ") AS TOTAL_PAY" + // RATE * AMOUNT
                    ", (CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE] + " * CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ") AS TOTAL_RET" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + // BILL_NUMBER+
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME+
                    " FROM ((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " CP" + // cash_payment
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID+
                    " = CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID+
                    " ) LEFT JOIN " + PstCashReturn.TBL_RETURN + " CR" + // cash_return_payment
                    " ON CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID+
                    " = CR." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + // CASH_BILL_MAIN_ID+
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + // APP_USER_ID+
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]; // USER_ID;

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCurrencyId = "";
            if (srcReportSale.getCurrencyId() != 0) {
                strCurrencyId = " CP.CURRENCY_ID = " + srcReportSale.getCurrencyId();
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }
            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            String strPaymentType = " CP.PAY_TYPE = 0";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strPaymentType;
            } else {
                whereClause = whereClause + strPaymentType;
            }

            if (strCurrencyId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCurrencyId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " ORDER BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE"+
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]; // BILL_NUMBER";

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(new Double(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE])));
                temp.add(new Double(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT])));
                temp.add(new Double(rs.getDouble("TOTAL_PAY")));
                temp.add(new Double(rs.getDouble("TOTAL_RET")));
                temp.add(Formater.formatDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), "dd-MM-yyyy"));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                temp.add(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));

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

    /**ary
     * Untuk menampilkan total pembayaran dengan credit card
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapCreditCard(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE] + // RATE
                    ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] + // AMOUNT
                    ", (CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE] + // RATE
                    " * CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] + ") AS TOTAL_PAY" + // AMOUNT
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + // BILL_NUMBER" +
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME" +
                    ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER] + // CC_NUMBER" +
                    ", CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME] + // HOLDER_NAME" +
                    " FROM ((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " CP" + // cash_payment
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID
                    " = CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " ) INNER JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + " CC" + //  cash_credit_card
                    " ON CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID] + // CASH_PAYMENT_ID
                    " = CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + // CASH_PAYMENT_ID
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + // APP_USER_ID
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]; // USER_ID;

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCurrencyId = "";
            if (srcReportSale.getCurrencyId() != 0) {
                strCurrencyId = " CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID] + " = " + srcReportSale.getCurrencyId(); // CURRENCY_ID
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }
            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            String strPaymentType = " CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] + " = 1"; // PAY_TYPE
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strPaymentType;
            } else {
                whereClause = whereClause + strPaymentType;
            }

            if (strCurrencyId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCurrencyId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " ORDER BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]; // BILL_NUMBER";

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(new Double(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE])));
                temp.add(new Double(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT])));
                temp.add(new Double(rs.getDouble("TOTAL_PAY")));
                temp.add(Formater.formatDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), "dd-MM-yyyy"));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                temp.add(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
                temp.add(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER]));
                temp.add(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME]));

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

    /** ary
     * Tampilkan grafik penjualan per barang (secara bulanan dalam setahun)
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleGrafikBarang(SrcReportSaleGrafikBarang srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " MONTH(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") AS BULAN" + // BILL_DATE
                    " , SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SumOfQTY" + // QTY
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strMaterialId = "";
            if (srcReportSale.getMaterialId() != 0) {
                strMaterialId = " CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + " = " + srcReportSale.getMaterialId();
            }

            String strSku = "";
            if ((srcReportSale.getSku() != null) && ((srcReportSale.getSku()).length() > 0)) {
                strMaterialId = " CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] + " = " + srcReportSale.getSku();
            }

            String strTahun = "";
            if (srcReportSale.getTahun() != 0) {
                strTahun = " Year(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = " + srcReportSale.getTahun();
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strMaterialId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strMaterialId;
                } else {
                    whereClause = whereClause + strMaterialId;
                }
            }

            if (strSku.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSku;
                } else {
                    whereClause = whereClause + strSku;
                }
            }

            if (strTahun.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strTahun;
                } else {
                    whereClause = whereClause + strTahun;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql += " GROUP BY MONTH(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")"; // BILL_DATE
            sql += " ORDER BY MONTH(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")"; // BILL_DATE

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add("" + rs.getInt("BULAN"));
                temp.add("" + rs.getDouble("SumOfQTY"));

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

    /**ary
     * Menampilkan grafik penjualan barang per kategori dalam 1 bulan
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleGrafikKategori(SrcReportSaleGrafikKategori srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    ", Sum(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SumOfQTY" + //  QTY
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID+
                    " FROM ((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]; // CATEGORY_ID";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strBulan = "";
            if (srcReportSale.getBulan() != 0) {
                strBulan = " MONTH(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = " + srcReportSale.getBulan();
            }

            String strTahun = "";
            if (srcReportSale.getTahun() != 0) {
                strTahun = " Year(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = " + srcReportSale.getTahun();
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strBulan.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strBulan;
                } else {
                    whereClause = whereClause + strBulan;
                }
            }

            if (strTahun.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strTahun;
                } else {
                    whereClause = whereClause + strTahun;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }
            sql += " GROUP BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]; // CATEGORY_ID";
            sql += " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE]; // CODE";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(rs.getString(PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add("" + rs.getDouble("SumOfQTY"));
                temp.add("" + rs.getLong(PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]));
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

    /**ary
     * Untuk menampilkan total penjualan harian di lokasi tertentu per tanggal
     * sesuai kategori terpilih dan digroup by sub kategorinya
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapSupplier(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // NAME
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // NAME
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SumOfQTY" + // QTY
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SumOfTOTAL_PRICE" + // TOTAL_PRICE
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS HPP" + // QTY * COST
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE+
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + // COMP_NAME+
                    " FROM ((((" + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + //  cash_bill_main
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " =" + // SUPPLIER_ID
                    " CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]; // CONTACT_ID";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

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

            if (strSubCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                } else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(new Double(rs.getDouble("SumOfQTY")));
                temp.add(new Double(rs.getDouble("SumOfTOTAL_PRICE")));
                temp.add(new Double(rs.getDouble("HPP")));
                temp.add(new Double(rs.getDouble("DISC")));
                temp.add(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));

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
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapInvoice(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + // BILL_NUMBER" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SUM_PRICE" + //  TOTAL_PRICE
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS HPP" + // COST
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY+
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME" +
                    ", SLS." + PstSales.fieldNames[PstSales.FLD_NAME] + // SALES_NAME" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + // INVOICE_NUMBER
                    " FROM ((((((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID+
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID+
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + // APP_USER_ID
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID" +
                    " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID" +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID" +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID" +
                    " ) LEFT JOIN " + PstSales.TBL_SALES + " SLS" + // sales_person
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + // SALES_CODE
                    " = SLS." + PstSales.fieldNames[PstSales.FLD_CODE]; // SALES_CODE";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //Add Group By
            sql += " GROUP BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + // BILL_NUMBER
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME
                    ", SLS." + PstSales.fieldNames[PstSales.FLD_NAME]; // SALES_NAME;

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " ,USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME" +
                            " ,CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] +
                            " ,CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID";
            }

            //System.out.println("SALE SQl : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(Formater.formatDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID])));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                temp.add(new Double(rs.getDouble("SUM_PRICE")));
                temp.add(new Double(rs.getDouble("HPP")));
                temp.add(new Double(rs.getDouble("DISC")));
                temp.add(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
                temp.add(rs.getString(PstSales.fieldNames[PstSales.FLD_NAME]));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));

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
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapInvoiceByDoc(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + // BILL_NUMBER" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SUM_PRICE" + //  TOTAL_PRICE
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS HPP" + // COST
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY+
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME" +
                    ", SLS." + PstSales.fieldNames[PstSales.FLD_NAME] + // SALES_NAME" +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + // INVOICE_NUMBER
                    " FROM ((((((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID+
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID+
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID+
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + // APP_USER_ID
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID" +
                    " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID" +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID" +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID" +
                    " ) LEFT JOIN " + PstSales.TBL_SALES + " SLS" + // sales_person
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + // SALES_CODE
                    " = SLS." + PstSales.fieldNames[PstSales.FLD_CODE]; // SALES_CODE";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String strDocType = "";
            if ((srcReportSale.getDocType() >= 0)) {

                int[] transMode = getTransMode(srcReportSale);
                strDocType = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] + " " +
                        " AND " +
                        " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] + " " +
                        " AND " +
                        " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] + " ";

            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (strDocType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDocType;
                } else {
                    whereClause = whereClause + strDocType;
                }
            }
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //Add Group By
            sql += " GROUP BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE+
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + // BILL_NUMBER
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME
                    ", SLS." + PstSales.fieldNames[PstSales.FLD_NAME]; // SALES_NAME;

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " ,USR." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + // FULL_NAME" +
                            " ,CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] +
                            " ,CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID";
            }

            //System.out.println("SALE SQl : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();

                temp.add(Formater.formatDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID])));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                temp.add(new Double(rs.getDouble("SUM_PRICE")));
                temp.add(new Double(rs.getDouble("HPP")));
                temp.add(new Double(rs.getDouble("DISC")));
                temp.add(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
                temp.add(rs.getString(PstSales.fieldNames[PstSales.FLD_NAME]));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));

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
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleTotal(SrcReportSale srcReportSale) {
        //DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTALQTY" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTALHPP" +
                    //", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + ") AS ITEMPRC" +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + " AS ITEMPRC" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS TOTALPRC" +
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // category
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // sub category
                    " FROM (((((((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID" +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + //APP_USER_ID" +
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID" +

                    " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list

                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID" +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]; // SUB_CATEGORY_ID";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            //System.out.println("SALE SQL : " + sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                Billdetail cbd = new Billdetail();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();

                cbd.setQty(rs.getDouble("TOTALQTY"));
                cbd.setCost(rs.getDouble("TOTALHPP"));
                cbd.setItemPrice(rs.getDouble("ITEMPRC"));
                cbd.setTotalPrice(rs.getDouble("TOTALPRC"));
                cbd.setDisc(rs.getDouble("DISC"));
                temp.add(cbd);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                temp.add(mat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

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
     * @param subCategoryId
     * @return 081
     */
    /**wsutaya
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleAll(SrcReportSale srcReportSale) {
        //DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTALQTY" +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + " AS BUYPRC " + //HARGA BELI

                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + " AS ITEMPRC" + //HARGA JUAL
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS TOTALPRC" + //TOTAL JUAL
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // category
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // sub category
                    " FROM ((((((((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID" +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + //APP_USER_ID" +
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID" +

                    /* " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID*/
                    /*
                    INNER JOIN POS_VENDOR_PRICE VPC ON VPC.MATERIAL_ID = MAT.MATERIAL_ID
                    )
                    INNER JOIN CONTACT_LIST CNT ON VPC.VENDOR_ID = CNT.CONTACT_ID
                     */
                    " ) INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VPC " +
                    " ON VPC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT " +
                    " ON VPC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID" +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]; // SUB_CATEGORY_ID";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " VPC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }
            //PROSES PENCARIAN MELIBATKAN WAKTU
            String strDate = "";
            Date tempDate = srcReportSale.getDateTo();
            Date searchEndDate = new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate() + 1);
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(searchEndDate, "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + " 07:00:00' AND '" + endDate + " 06:59:00'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            //System.out.println("SALE SQL : " + sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                Billdetail cbd = new Billdetail();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();

                cbd.setQty(rs.getDouble("TOTALQTY"));
                cbd.setCost(rs.getDouble("BUYPRC"));

                cbd.setItemPrice(rs.getDouble("ITEMPRC"));
                cbd.setTotalPrice(rs.getDouble("TOTALPRC"));
                cbd.setDisc(rs.getDouble("DISC"));
                temp.add(cbd);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                temp.add(mat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

                result.add(temp);
                String xx = "sasasa";
                xx = xx.toUpperCase();

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

    /**wsutaya
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleAllNew(SrcReportSale srcReportSale) {
        //DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTALQTY" +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + " AS BUYPRC " + //HARGA BELI

                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + " AS ITEMPRC" + //HARGA JUAL
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS TOTALPRC" + //TOTAL JUAL
                    ", SUM((CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + // QTY
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + // ITEM_PRICE
                    ") - CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS DISC" + // TOTAL_PRICE
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] + // category
                    //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    //" AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + // sub category
                    " FROM (" +
                    //"(" +
                    "(" +
                    //"((" +
                    "(((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID" +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID" +
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + //APP_USER_ID" +
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID" +

                    /* " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID*/
                    /*
                    INNER JOIN POS_VENDOR_PRICE VPC ON VPC.MATERIAL_ID = MAT.MATERIAL_ID
                    )
                    INNER JOIN CONTACT_LIST CNT ON VPC.VENDOR_ID = CNT.CONTACT_ID
                     */
                    //" ) INNER JOIN "+PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" VPC "+
                    //" ON VPC."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+
                    //" = MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
                    //" ) INNER JOIN "+PstContactList.TBL_CONTACT_LIST+ " CNT "+
                    //" ON VPC."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+
                    //" = CNT."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+

                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID" +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID" +
                    //" ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    //" ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    //" = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]; // SUB_CATEGORY_ID
                    "";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
            //strSupplierId = " VPC." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
            //strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }
            //PROSES PENCARIAN MELIBATKAN WAKTU
            String strDate = "";
            Date tempDate = srcReportSale.getDateTo();
            Date searchEndDate = new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate() + 1);
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(searchEndDate, "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + " 07:00:00' AND '" + endDate + " 06:59:00'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String strDocType = "";
            if ((srcReportSale.getDocType() >= 0)) {

                int[] transMode = getTransMode(srcReportSale);
                strDocType = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] + " " +
                        " AND " +
                        " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] + " " +
                        " AND " +
                        " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] + " ";

            }


            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (strDocType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDocType;
                } else {
                    whereClause = whereClause + strDocType;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
                    "";

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            //System.out.println("SALE SQL : " + sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                Billdetail cbd = new Billdetail();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();

                cbd.setQty(rs.getDouble("TOTALQTY"));
                cbd.setCost(rs.getDouble("BUYPRC"));

                cbd.setItemPrice(rs.getDouble("ITEMPRC"));
                cbd.setTotalPrice(rs.getDouble("TOTALPRC"));
                cbd.setDisc(rs.getDouble("DISC"));
                temp.add(cbd);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                temp.add(mat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                //scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                scat.setName("");
                temp.add(scat);

                result.add(temp);
                String xx = "sasasa";
                xx = xx.toUpperCase();

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

    public static int countItemSubCategory(Connection koneksi, long subCategoryId) {
        int result = 0;
        try {
            String sql = "SELECT COUNT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") AS CNT_MAT" + // MATERIAL_ID
                    " FROM " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " WHERE MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + subCategoryId; // SUB_CATEGORY_ID

            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                result = rs.getInt(1);
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

    /** ary ,gadnyana
     * Untuk menampilkan total penjualan harian di lokasi tertentu per tanggal
     * @param srcReportSale
     * @return
     */
    public static Vector getReportSaleRekapTanggalKategori(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //List semua kategori order by kode
            Vector listKategori = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CODE]);
            String sql = "SELECT CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + // BILL_DATE
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID";

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]; // BILL_DATE";
            sql += " ORDER BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]; // BILL_DATE";

            //System.out.println("==> SQL :" + sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                for (int i = 0; i < listKategori.size(); i++) {
                    //Count total Sale for each category
                    Category cat = (Category) listKategori.get(i);
                    long oidCategory = cat.getOID();
                    temp.add(new Double(sumCategorySale(srcReportSale.getLocationId(), rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), oidCategory, koneksi)));
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

    /** ary , gadnyana
     * Menghitung total sale per kategori dlm suatu range tertentu
     * @param oidLocation
     * @param filterDate
     * @param oidCategory
     * @param koneksi
     * @return
     */
    private static double sumCategorySale(long oidLocation, Date filterDate, long oidCategory, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " +
                    " SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS SumOfTOTAL_PRICE" + // TOTAL_PRICE
                    " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + //  sub_category
                    " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID+
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + //  material
                    " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    " ) INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + //MATERIAL_ID
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID" +
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]; // CASH_BILL_MAIN_ID";

            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + oidLocation;
            }

            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }

            String whereClause = "";
            if (strLocationId.length() > 0) {
                whereClause = strLocationId;
            }

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

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                hasil = rs.getDouble("SumOfTOTAL_PRICE");
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

    /** edhy, gadnyana
     * This method used to search data for Daily Sale Report per Supplier (Daftar Stok Penjualan Barang Harian)
     * @param <CODE>SrcReportSale</CODE>object used in parameter for searching data
     * @return <CODE>Vector</CODE>vector of result object
     */
    public static Vector getReportSaleTotalPerSupplier(SrcReportSale srcReportSale) {
        //DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTALQTY" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTALHPP" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + ") AS ITEMPRC" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS TOTALPRC" +
                    ", SUM((CBD.QTY * CBD.ITEM_PRICE) - CBD.TOTAL_PRICE) AS DISC" +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " FROM (((((((" + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" + // cash_bill_main
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" + // cash_bill_detail
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + //  CASH_BILL_MAIN_ID
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + // CASH_BILL_MAIN_ID
                    " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" + // location
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + // LOCATION_ID
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + // LOCATION_ID
                    " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" + // material
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + // MATERIAL_ID" +
                    " ) INNER JOIN " + PstAppUser.TBL_APP_USER + " USR" + // app_user
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + // APP_USER_ID" +
                    " = USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + // USER_ID" +
                    " ) LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" + // contact_list
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + // SUPPLIER_ID
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + // CONTACT_ID" +
                    " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" + // unit
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + // UNIT_ID" +
                    " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + // UNIT_ID" +
                    " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" + // category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // CATEGORY_ID
                    " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" + // sub_category
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + // SUB_CATEGORY_ID" +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]; // SUB_CATEGORY_ID";

            String strSupplierType = "";
            if (srcReportSale.getSupplierType() != -1) {
                strSupplierType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] + " = " + srcReportSale.getSupplierType();
            }

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strShiftId = "";
            if (srcReportSale.getShiftId() != 0) {
                strShiftId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + srcReportSale.getShiftId();
            }

            String strOperatorId = "";
            if (srcReportSale.getOperatorId() != 0) {
                strOperatorId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + " = " + srcReportSale.getOperatorId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strSubCategoryId = "";
            if (srcReportSale.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportSale.getSubCategoryId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strSalesCode = "";
            if (((srcReportSale.getSalesCode()).length() > 0) && (srcReportSale.getSalesCode() != null)) {
                strSalesCode = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + srcReportSale.getSalesCode() + "'";
            }

            String whereClause = "";

            if (strSupplierType.length() > 0) {
                whereClause = strSupplierType;
            }

            if (strLocationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strLocationId;
                } else {
                    whereClause = whereClause + strLocationId;
                }
            }

            if (strShiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strShiftId;
                } else {
                    whereClause = whereClause + strShiftId;
                }
            }

            if (strOperatorId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strOperatorId;
                } else {
                    whereClause = whereClause + strOperatorId;
                }
            }

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

            if (strSalesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSalesCode;
                } else {
                    whereClause = whereClause + strSalesCode;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];

            srcReportSale.setSortBy(4);

            switch (srcReportSale.getSortBy()) {
                case 0:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 1:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    break;
                case 2:
                    //sql = sql + " ORDER BY SL."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
                    break;
                case 3:
                    //sql = sql + " ORDER BY CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIMIT "+start+","+recordToGet;
                    break;
                default:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                            " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector temp = new Vector();
                Billdetail cbd = new Billdetail();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();

                cbd.setQty(rs.getDouble("TOTALQTY"));
                cbd.setCost(rs.getDouble("TOTALHPP"));
                cbd.setItemPrice(rs.getDouble("ITEMPRC"));
                cbd.setTotalPrice(rs.getDouble("TOTALPRC"));
                cbd.setDisc(rs.getDouble("DISC"));
                temp.add(cbd);

                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                temp.add(mat);

                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);

                cat.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);

                scat.setName(rs.getString("SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);

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

    /**
     * used to list all supplier which goods is sold in specify period
     * @param <CODE>srcReportSale</CODE>for searching parameter
     * @created by edhy
     */
    public static Vector getListSupplierWhichGoodSold(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + ") AS RESULTSUM" +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS CBD" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT" +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM" +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strSupplierId = "";
            if (srcReportSale.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportSale.getSupplierId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String whereClause = "";

            if (strLocationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strLocationId;
                } else {
                    whereClause = whereClause + strLocationId;
                }
            }

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    " ORDER BY RESULTSUM DESC";

            //System.out.println("SQL-------------> " + sql);

            //Connection koneksi = PstMaterialStock.getLocalConnection();
            //Statement stmt = koneksi.createStatement();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);

                temp.add(String.valueOf(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID])));
                temp.add(String.valueOf(rs.getDouble("RESULTSUM")));

                result.add(temp);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * used to list all sold's goods by specify supplier
     * @param <CODE>srcReportSale</CODE>for searching parameter
     * @param <CODE>supplierId</CODE>supplier OID which goods' sold
     * @created by edhy
     */
    public static Vector getListGoodSoldOfSupplier(SrcReportSale srcReportSale, long supplierId) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " AS SUB_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS TOTQTY" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS TOTSOLD" +
                    ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ") AS TOTCOGS" +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS CBD" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT" +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM" +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SUB " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];


            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strSupplierId = "";
            if (supplierId != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + supplierId;
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String whereClause = "";

            if (strLocationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strLocationId;
                } else {
                    whereClause = whereClause + strLocationId;
                }
            }

            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql += " GROUP BY SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];

            //System.out.println("SQL ----------------> " + sql);
            //Connection koneksi = PstMaterialStock.getLocalConnection();
            //Statement stmt = koneksi.createStatement();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                Billdetail billDetail = new Billdetail();

                category.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                subCategory.setName(rs.getString("SUB_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                billDetail.setQty(rs.getDouble("TOTQTY"));
                billDetail.setTotalPrice(rs.getDouble("TOTSOLD"));
                billDetail.setCost(rs.getDouble("TOTCOGS"));

                temp.add(category);
                temp.add(subCategory);
                temp.add(billDetail);

                result.add(temp);
            }
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * used to list all material/goods which not move (tidak bergerak) in specify period
     * @param <CODE>SrcReportSales</CODE>object used tas parameter
     *
     */
    public static Vector getListStaticGoods(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String outerSql = " SELECT " +
                    " CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " AS SUB_" + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " AS SUB_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " FROM " + PstMaterial.TBL_MATERIAL + " AS MAT" +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SUB" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];

            String innerSql = " SELECT DISTINCT CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS CBD" +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM" +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];

            String strLocationId = "";
            if (srcReportSale.getLocationId() != 0) {
                strLocationId = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcReportSale.getLocationId();
            }

            String strCategoryId = "";
            if (srcReportSale.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcReportSale.getCategoryId();
            }

            String strDate = "";
            if ((srcReportSale.getDateFrom() != null) && (srcReportSale.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd");
                strDate = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String whereClause = "";

            if (strLocationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strLocationId;
                } else {
                    whereClause = whereClause + strLocationId;
                }
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (whereClause.length() > 0) {
                innerSql = innerSql + " WHERE " + whereClause;
            }


            String whereClauseOuter = "";
            if (strCategoryId.length() > 0) {
                whereClauseOuter = strCategoryId;
            }

            String strMaterialNotIn = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " NOT IN (" + innerSql + ")";
            //if(whereClauseOuter.length()>0) {
            if (whereClauseOuter.length() > 0) {
                whereClauseOuter = " WHERE " + whereClauseOuter + " AND " + strMaterialNotIn;
            } else {
                whereClauseOuter = " WHERE " + strMaterialNotIn;
            }
            //}

            String sql = outerSql + whereClauseOuter + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", SUB." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            //System.out.println("SQL ----------------> " + sql);
            //Connection koneksi = PstMaterialStock.getLocalConnection();
            //Statement stmt = koneksi.createStatement();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                Material material = new Material();

                category.setCode(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_CODE]));
                category.setName(rs.getString("CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME]));

                subCategory.setCode(rs.getString("SUB_" + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]));
                subCategory.setName(rs.getString("SUB_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));

                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));

                temp.add(category);
                temp.add(subCategory);
                temp.add(material);

                result.add(temp);
            }
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ini untuk mencari jumlah qty per bulan selama 1 tahun
     * @param oidCategory
     * @param tahun
     * @return vector of vector
     */
    public static Vector getReportGrafikPerjualanPertahun(long oidCategory, long oidLocation, int tahun) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SUM(DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS QTYTOTAL " +
                    " , MONTH(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") AS BULAN " +
                    " FROM " + PstCategory.TBL_CATEGORY + " AS CATEG " +
                    " INNER JOIN ((" + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS DT " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS MN ON " +
                    " DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = MN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT ON DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + " = " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") " +
                    " ON CATEG." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " WHERE (((YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "))=" + tahun + ") " +
                    " AND ((" + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + ")=" + oidLocation + ") " +
                    " AND ((CATEG." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")=" + oidCategory + ")) " +
                    " GROUP BY MONTH(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                temp.add("" + rs.getDouble("QTYTOTAL"));
                temp.add("" + rs.getInt("BULAN"));

                result.add(temp);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERR getReportGrafikPerjualanPertahun : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ini untuk mencari jumlah qty per bulan selama 1 tahun
     * @param oidCategory
     * @param tahun
     * @return vector of vector
     */
    public static Vector getReportGrafikPerjualanPertahun(long oidCategory, long oidLocation, int tahun, int totahun) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SUM(DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS QTYTOTAL " +
                    " , YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") AS TAHUN " +
                    " FROM " + PstCategory.TBL_CATEGORY + " AS CATEG " +
                    " INNER JOIN ((" + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS DT " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS MN ON " +
                    " DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = MN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT ON DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + " = " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") " +
                    " ON CATEG." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " WHERE (((YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")) >= " + tahun + ") " +
                    " AND ((YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")) <= " + totahun + ") " +
                    " AND ((" + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + ") = " + oidLocation + ") " +
                    " AND ((CATEG." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")=" + oidCategory + ")) " +
                    " GROUP BY YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                temp.add("" + rs.getDouble("QTYTOTAL"));
                temp.add("" + rs.getInt("TAHUN"));

                result.add(temp);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERR getReportGrafikPerjualanPertahun : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ini untuk mencari jumlah qty selama 1 tahun yang mereturn per sub category
     * @param oidCategory
     * @param tahun
     * @return vector of vector
     */
    public static Vector getReportGrafikPerjualanSubCategory(long oidCategory, long oidLocation, int tahun) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SUM(DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS QTYTOTAL " +
                    " ,CATEG." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " ,CATEG." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,CATEG." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    " FROM " + PstSubCategory.TBL_SUB_CATEGORY + " AS CATEG " +
                    " INNER JOIN ((" + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS DT " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS MN ON " +
                    " DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = MN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT " +
                    " ON DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + " = " +
                    " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") " +
                    " ON CATEG." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " WHERE (((YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")) = " + tahun + ") " +
                    " AND ((" + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + ") = " + oidLocation + ") " +
                    " AND ((CATEG." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] + ")=" + oidCategory + ")) " +
                    " GROUP BY CATEG." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                SubCategory subCategory = new SubCategory();

                temp.add("" + rs.getDouble("QTYTOTAL"));

                subCategory.setOID(rs.getLong(PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]));
                subCategory.setCode(rs.getString(PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]));
                subCategory.setName(rs.getString(PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(subCategory);

                result.add(temp);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERR getReportGrafikPerjualanSubCategory : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ini untuk mencari jumlah laporan per sales
     * @param SrcReportSale
     * @return vector of vector
     */
    public static Vector getReportPerSales(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        long locationId = srcReportSale.getLocationId();
        Date dtFrom = srcReportSale.getDateFrom();
        Date dtTo = srcReportSale.getDateTo();
        String codeSales = srcReportSale.getSalesCode();

        try {
            String sql = "SELECT " +
                    " COUNT(DETAIL." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + ") AS TOTAL_TRANS " +
                    " ,SUM(DETAIL." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") AS TOT_PRICE " +
                    " ,SALES." + PstSales.fieldNames[PstSales.FLD_NAME] +
                    " AS SALES_" + PstSales.fieldNames[PstSales.FLD_NAME] +
                    " ,LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " AS LOC_" + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ,MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " FROM (((" + PstBillMain.TBL_CASH_BILL_MAIN + " AS MAIN " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS DETAIL " +
                    " ON DETAIL." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    ") LEFT JOIN " + PstSales.TBL_SALES + " AS SALES " +
                    " ON SALES." + PstSales.fieldNames[PstSales.FLD_CODE] +
                    " = MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] +
                    ") INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS LOC " +
                    " ON LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " +
                    " MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + ") ";

            String where = " WHERE MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] +
                    " = '" + codeSales + "'";

            if (locationId != 0) {
                if (where != null && where.length() > 0) {
                    where = where + " AND MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + locationId;
                } else {
                    where = where + " MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + locationId;
                }
            }

            if (dtFrom != null && dtTo != null) {
                if (where != null && where.length() > 0) {
                    where = where + " AND (MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' AND " +
                            "'" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "') ";
                } else {
                    where = where + " (MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' AND " +
                            "'" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "') ";
                }
            }

            if (where != null && where.length() > 0) {
                sql = sql + where;
            }

            sql = sql + " GROUP BY MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ORDER BY MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            //System.out.println("sql = " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);

                temp.add("" + rs.getInt("TOTAL_TRANS"));
                temp.add("" + rs.getDouble("TOT_PRICE"));
                temp.add(rs.getString("SALES_" + PstSales.fieldNames[PstSales.FLD_NAME]));
                temp.add(rs.getString("LOC_" + PstLocation.fieldNames[PstLocation.FLD_NAME]));
                temp.add(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));

                result.add(temp);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERR getReportPerSales : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ini untuk mencari jumlah laporan invoice external
     * @param SrcReportSale
     * @param persen
     * @return vector of vector
     */

    /*
     *SELECT SUBSTRING(A.BILL_NUMBER,9,14) AS STR,  A.INVOICE_NUMBER,
    SUM(B.TOTAL_PRICE) AS TOT_PRICE
    FROM CASH_BILL_MAIN AS A
    INNER JOIN CASH_BILL_DETAIL AS B
    ON B.CASH_BILL_MAIN_ID = A.CASH_BILL_MAIN_ID
    GROUP BY A.BILL_NUMBER
    ORDER BY A.BILL_NUMBER
     */
    public static Vector getReportPerInvoiceEx(SrcReportSale srcReportSale, double persen) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        long locationId = srcReportSale.getLocationId();
        Date dtFrom = srcReportSale.getDateFrom();
        Date dtTo = srcReportSale.getDateTo();
        long operatorId = srcReportSale.getOperatorId();

        try {
            String sql = "SELECT " +
                    " SUBSTRING(MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + ",9,14) AS STR_TIME " +
                    " ,MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,SUM(DETAIL." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + "*" + persen + ") AS TOT_PRICE " +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS MAIN " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS DETAIL " +
                    " ON DETAIL." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];

            String where = " WHERE MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] +
                    " = " + PstBillMain.TYPE_EXTERNAL;

            if (locationId != 0) {
                if (where != null && where.length() > 0) {
                    where = where + " AND MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + locationId;
                } else {
                    where = where + " MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + locationId;
                }
            }

            if (operatorId != 0) {
                if (where != null && where.length() > 0) {
                    where = where + " AND MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] +
                            " = " + operatorId;
                } else {
                    where = where + " MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] +
                            " = " + operatorId;
                }
            }

            if (dtFrom != null && dtTo != null) {
                if (where != null && where.length() > 0) {
                    where = where + " AND (MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' AND " +
                            "'" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "') ";
                } else {
                    where = where + " (MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' AND " +
                            "'" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "') ";
                }
            }

            if (where != null && where.length() > 0) {
                sql = sql + where;
            }

            sql = sql + " GROUP BY MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] +
                    " ORDER BY MAIN." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO];
            //System.out.println("sql = " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector(1, 1);

                temp.add(rs.getString("STR_TIME"));
                temp.add(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                temp.add("" + rs.getDouble("TOT_PRICE"));

                result.add(temp);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERR getReportInvoice ex : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //method ini fungsi untuk mengeset time untuk inputan string
    public static String setLikeTime(String text) {
        String str = "";
        if (text != null && text.length() > 0) {
            int count_end = 2;
            int count_begin = 0;
            for (int i = 0; i < 3; i++) {
                String getText = text.substring(count_begin, count_end);
                if (i != 2) {
                    str = str + getText + ":";
                } else {
                    str = str + getText;
                }
                count_end += 2;
                count_begin += 2;
            }
        }
        return str;
    }

    /** gadnyana
     * di pakai untuk mencari data penjualan yang statusnya masih draft
     * @param materialId
     * @param status
     * @return
     */
    public static Vector getQtyBillDetail(long materialId, long oidLocation, int status) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " ,SUM(BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS " + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    " ,BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM " +
                    " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                    " = " + status +
                    " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " = " + oidLocation;

            if (materialId != 0) {
                sql = sql + " AND BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                        " = " + materialId;
            }
            sql = sql + " GROUP BY BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID];

            //System.out.println("=>> SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billdetail = new Billdetail();

                billdetail.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
                billdetail.setMaterialId(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]));
                billdetail.setItemName(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]));

                list.add(billdetail);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("==>> ERR getQtyBillDetail : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /** gadnyana
     * untuk mencari qty penjualan satu item
     * @param materialId
     * @param status
     * @return
     */
    public static double getBillDetail(long materialId, long oidLocation, int status) {
        double qtysale = 0;
        try {
            Vector vtsale = getQtyBillDetail(materialId, oidLocation, status);
            if (vtsale != null && vtsale.size() > 0) {
                Billdetail billDetail = (Billdetail) vtsale.get(0);
                qtysale = Double.parseDouble(String.valueOf(billDetail.getQty()));
            }
        } catch (Exception e) {
        }
        return qtysale;
    }

    /**gadnyana
     * ini adalah untuk mencari data laporan penjualan
     * Rekap Penjualan per shift / per hari dan per lokasi
     * @param srcReportSale
     * @return Vector
     */
    public static Vector getDataRekapPenjualan(SrcReportSale srcReportSale) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            Shift shift = PstShift.fetchExc(srcReportSale.getShiftId());
            String stTime = Formater.formatDate(shift.getStartTime(), "HH:mm:00");
            String edTime = Formater.formatDate(shift.getEndTime(), "HH:mm:00");
            Date dtTo = new Date(srcReportSale.getDateFrom().getTime());
            if (shift.getStartTime().getHours() > shift.getEndTime().getHours()) {
                dtTo.setDate(dtTo.getDate() + 1);
                srcReportSale.setDateTo(dtTo);
            } else {
                srcReportSale.setDateTo(dtTo);
            }

            String sQL = "SELECT " +
                    "CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] +
                    ",CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] +
                    ",CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] +
                    ",CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] +
                    ",SUM(" + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS " + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    ",U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ",SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ")" +
                    ",SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ")" + // TOTAL_PRICE
                    ",(((SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") - " +
                    "SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ")) " +
                    "/ SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ")) * 100) AS MARGIN " +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " U ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " BETWEEN '" + Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd") + " " + stTime + "' AND '" + Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd") + " " + edTime + "'";

            if (srcReportSale.getShiftId() != 0) {
            //sQL = sQL + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + "=" + srcReportSale.getShiftId();
            }

            if (srcReportSale.getLocationId() != 0) {
                sQL = sQL + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcReportSale.getLocationId();
            }

            sQL = sQL + " GROUP BY CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID];

            //System.out.println("sSQL >"+sQL);
            dbrs = DBHandler.execQueryResult(sQL);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                Billdetail billDetail = new Billdetail();
                Unit unit = new Unit();
                billDetail.setSku(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]));
                billDetail.setItemName(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]));
                billDetail.setCost(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_COST]));
                billDetail.setItemPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
                billDetail.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
                vt.add(billDetail);

                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vt.add(unit);

                vt.add(String.valueOf(rs.getDouble(7))); // total harga beli
                vt.add(String.valueOf(rs.getDouble(8))); // total harga jual
                vt.add(String.valueOf(rs.getDouble(9))); // total margin ((harga jual - harga beli) / harga jual) * 100%

                list.add(vt);
            }

        } catch (Exception e) {
            System.out.println("==>> ERROR getDataRekapPenjualan " + e.toString());
            return new Vector(1, 1);
        }
        return list;
    }
    private static final int DOC_TYPE = 0;
    private static final int TRANSACTION_TYPE = 1;
    private static final int TRANSACTION_STATUS = 2;

    public static int[] getTransMode(SrcReportSale srcReportSale) {
        int[] transType = {0, 0, 0};
        switch (srcReportSale.getDocType()) {
            case SrcReportSale.TYPE_CASH:
                transType[DOC_TYPE] = PstBillMain.TYPE_INVOICE;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_CLOSE;
                break;
            case SrcReportSale.TYPE_RETUR:
                transType[DOC_TYPE] = PstBillMain.TYPE_RETUR;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_CLOSE;
                break;
            case SrcReportSale.TYPE_CREDIT:
                transType[DOC_TYPE] = PstBillMain.TYPE_INVOICE;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CREDIT;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_OPEN;
                break;
            case SrcReportSale.TYPE_OPEN_BILL:
                transType[DOC_TYPE] = PstBillMain.TYPE_INVOICE;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_OPEN;
                break;
        }
        return transType;
    }

    /**wpradnyana
     * ini adalah untuk mencari data laporan penjualan berdasar tipe transaksi penjualan
     * Rekap Penjualan per shift / per hari dan per lokasi
     * @param srcReportSale
     * @return Vector
     */
    public static Vector getDataRekapPenjualanByDoc(SrcReportSale srcReportSale) {

        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            Shift shift = PstShift.fetchExc(srcReportSale.getShiftId());
            String stTime = Formater.formatDate(shift.getStartTime(), "HH:mm:00");
            String edTime = Formater.formatDate(shift.getEndTime(), "HH:mm:00");
            Date dtTo = new Date(srcReportSale.getDateFrom().getTime());
            if (shift.getStartTime().getHours() > shift.getEndTime().getHours()) {
                dtTo.setDate(dtTo.getDate() + 1);
                srcReportSale.setDateTo(dtTo);
            } else {
                srcReportSale.setDateTo(dtTo);
            }
            int[] transMode = getTransMode(srcReportSale);

            String sQL = "SELECT " +
                    "CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] +
                    ",CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] +
                    ",CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] +
                    ",CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] +
                    ",SUM(" + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS " + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    ",U." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ",SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ")" +
                    ",SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ")" + // TOTAL_PRICE
                    ",(((SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") - " +
                    "SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + " * CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + ")) " +
                    "/ SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ")) * 100) AS MARGIN " +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                    " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstUnit.TBL_P2_UNIT + " U ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "" +
                    "AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] + " " +
                    " AND " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] + " " +
                    " AND " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] + " ";


            if (srcReportSale.getShiftId() != 0) {
            //sQL = sQL + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + "=" + srcReportPendingOrder.getShiftId();
            }

            if (srcReportSale.getLocationId() != 0) {
                sQL = sQL + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcReportSale.getLocationId();
            }

            sQL = sQL + " GROUP BY CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID];

            //System.out.println("sSQL >"+sQL);
            dbrs = DBHandler.execQueryResult(sQL);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                Billdetail billDetail = new Billdetail();
                Unit unit = new Unit();
                billDetail.setSku(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]));
                billDetail.setItemName(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]));
                billDetail.setCost(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_COST]));
                billDetail.setItemPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
                billDetail.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
                vt.add(billDetail);

                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vt.add(unit);

                vt.add(String.valueOf(rs.getDouble(7))); // total harga beli
                vt.add(String.valueOf(rs.getDouble(8))); // total harga jual
                vt.add(String.valueOf(rs.getDouble(9))); // total margin ((harga jual - harga beli) / harga jual) * 100%
                //vt.add(PstBillMain.transType[1][srcReportSale.getDocType()]);
                list.add(vt);
            }

        } catch (Exception e) {
            System.out.println("==>> ERROR getDataRekapPenjualan " + e.toString());
            return new Vector(1, 1);
        }
        return list;

    }

    /**
     * ini proses pencarian penjualan,return barang
     * proses nya ada di kasir
     * @param srcStockCard
     * @return
     */
    public static Vector getDataMaterial(SrcStockCard srcStockCard) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        String useForRaditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA"); 
        try {
            String sql = "SELECT";
                    if (useForRaditya.equals("1")){
                        sql += " R." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE]
                            + ",IF(P.`NO_KREDIT` IS NOT NULL, P.`NO_KREDIT`, R.INVOICE_NUMBER) AS INVOICE_NUMBER";
                    } else {
                        sql += " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                            + ",R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];
                    }
                    sql+= ",R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " ,SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BERAT] + ") AS SUM_BERAT " +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS R " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS RI " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
			if (PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL").equals("2")) {
				sql += " LEFT JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " AS REC " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_ID_BILL_MAIN_SALES_ORDER];
			}
                        if (useForRaditya.equals("1")){
                            sql += " LEFT JOIN `sedana_raditya`.`aiso_pinjaman` P ON R.`CASH_BILL_MAIN_ID` = P.`CASH_BILL_MAIN_ID`";
                        }
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                        " OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            } else {
                whereClause = " (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                        " OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND ";
                }
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
                    whereClause += " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + srcStockCard.getLocationId();
                }
            }

            if (useForRaditya.equals("1")){
                if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                    if (whereClause.length() > 0) {
                        whereClause += " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                    } else {
                        whereClause += " R." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                    }
                } else {
                    //update opie-eyek 20161021
                    if (whereClause.length() > 0) {
                        whereClause += " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                    } else {
                        whereClause += " R." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                    }
                }
            } else {
                if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                    if (whereClause.length() > 0) {
                        whereClause += " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                    } else {
                        whereClause += " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                    }
                } else {
                    //update opie-eyek 20161021
                    if (whereClause.length() > 0) {
                        whereClause += " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                    } else {
                        whereClause += " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                    }
                }
            }

            String strStatus = "";
            if (useForRaditya.equals("1")){
                strStatus = "((R." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DITERIMA + ")"
                        + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DIAMBIL_LANGSUNG + "))";
            } else {
                if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                    for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                        } else {
                            strStatus = "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                        }
                    }
                    strStatus = "(" + strStatus + ")";
                }
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            // for check invoice cancel or not
            if (whereClause.length() > 0) {
                whereClause += " AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED+
								" AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "!=" + PstBillMain.INVOICING_FINISH;
                             //penambahan open biil tidak ditampilkan
                             //by mirahu 5-12-2011
                                if (useForRaditya.equals("0")){
                               sql+=" AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                               " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";
                                }
            } else {
                whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
                              //penambahan open biil tidak ditampilkan
                              //by mirahu 5-12-2011
                              if (useForRaditya.equals("0")){
                             sql+=" AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                               " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";
                              }
            }
			
			if (PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL").equals("2")) {
				if (whereClause.length() > 0) {
					whereClause += " AND (REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " != " + PstMatReceive.SOURCE_FROM_BUYBACK+
							" OR REC."+ PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " IS NULL) ";
				} else {
					whereClause += " (REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " != " + PstMatReceive.SOURCE_FROM_BUYBACK+
							" OR REC."+ PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " IS NULL )";
				}
			}

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

			

            if (useForRaditya.equals("1")){
            sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE];
            } else {
                sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE];
            }
            sql = sql + " ORDER BY R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];

            //System.out.println("sql sale : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = new Date();
                if (useForRaditya.equals("1")){
                    date = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE]), rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE]));
                } else {
                    date = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                }
                stockCardReport.setDate(date);

                stockCardReport.setTransaction_type(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]));
                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_SALE);
                stockCardReport.setDocCode(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setBerat(rs.getDouble("SUM_BERAT"));
                stockCardReport.setLocationId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]));
                if (srcStockCard.getLanguage() == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
                    switch (stockCardReport.getTransaction_type()) {
                        case PstBillMain.TYPE_INVOICE:
                            stockCardReport.setKeterangan("Penjualan");
                            break;
                        case PstBillMain.TYPE_RETUR:
                            stockCardReport.setKeterangan("Pengembalian penjualan");
                            break;
                        case PstBillMain.TYPE_GIFT:
                            stockCardReport.setKeterangan("Hadiah");
                            break;
                        case PstBillMain.TYPE_COST:
                            stockCardReport.setKeterangan("Pembiayaan");
                            break;
                        case PstBillMain.TYPE_COMPLIMENT:
                            stockCardReport.setKeterangan("Compliment");
                            break;
                        case PstBillMain.TYPE_IMVOICE_CLAIM:
                            stockCardReport.setKeterangan("Invoice calm");
                            break;
                        default:
                            stockCardReport.setKeterangan("Penjualan");
                    }
                } else {
                    switch (stockCardReport.getTransaction_type()) {
                        case PstBillMain.TYPE_INVOICE:
                            stockCardReport.setKeterangan("Sales");
                            break;
                        case PstBillMain.TYPE_RETUR:
                            stockCardReport.setKeterangan("Return sales");
                            break;
                        case PstBillMain.TYPE_GIFT:
                            stockCardReport.setKeterangan("Gift");
                            break;
                        case PstBillMain.TYPE_COST:
                            stockCardReport.setKeterangan("Cost");
                            break;
                        case PstBillMain.TYPE_COMPLIMENT:
                            stockCardReport.setKeterangan("Compliment");
                            break;
                        case PstBillMain.TYPE_IMVOICE_CLAIM:
                            stockCardReport.setKeterangan("Invoice calm");
                            break;
                        default:
                            stockCardReport.setKeterangan("Sales");
                    }
                }

                PstStockCardReport.insertExc(stockCardReport);
            }
        } catch (Exception e) {
            System.out.println("Err getDataMaterial : " + e.toString());
        }
        return list;
    }

     /**
     * fungsi ini di gunakan untuk mencari daftar sale
     * di pakai untuk perhitungan reposting stock berdasarkan kartu stok.
     * by Mirahu
     * 20120803
     * @return
     */
    public static void getQtyStockMaterialReposting(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +                    
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BERAT] + ") AS SUM_BERAT " +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS R " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS RI " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = " RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE + ")";
                       // " OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            } else {
                whereClause = " (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +  ")";
                        //" OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if (srcMaterialRepostingStock.getDateFrom() != null && srcMaterialRepostingStock.getDateTo() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            // for check invoice cancel or not
            if (whereClause.length() > 0) {
                whereClause += " AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED+
                               " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                               " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";

            } else {
                whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED +
                              " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                              " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }


            sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];

            System.out.println("sql sale : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double qtyMaterial = 0;
            double qtyAll = 0;
            //-- added by dewok 2018-05-03 for jewelry
            double beratAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();
                
               // stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_SALE);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;                
                srcMaterialRepostingStock.setQty(qtyAll);
                //-- added by dewok 2018 for jewelry
                beratAll += rs.getDouble("SUM_BERAT");
                srcMaterialRepostingStock.setBerat(beratAll);
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                
                //PstStockCardReport.insertExc(stockCardReport);
            }
             if (qtyAll== 0){
                srcMaterialRepostingStock.setQty(0);
            }
            
        } catch (Exception e) {
            System.out.println("Err getDataMaterial : " + e.toString());
        }
        
    }
    
      /**
     * fungsi ini di gunakan untuk mencari daftar sale return
     * di pakai untuk perhitungan reposting stock berdasarkan kartu stok.
     * by Mirahu
     * 20120803
     * @return
     */
    
    public static void getQtyStockMaterialRepostingReturn(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +                    
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BERAT] + ") AS SUM_BERAT " +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS R " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS RI " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = " RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
                        //" OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            } else {
                whereClause = " (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
                        //" OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if (srcMaterialRepostingStock.getDateFrom() != null && srcMaterialRepostingStock.getDateTo() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            // for check invoice cancel or not
            if (whereClause.length() > 0) {
                whereClause += " AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            } else {
                whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }


            sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];

            System.out.println("sql sale return : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double qtyMaterial = 0;
            double qtyAll = 0;
            //-- added by dewok 2018-05-03 for jewelry
            double beratAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

               // stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_SALE);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcMaterialRepostingStock.setQty(qtyAll);
                //-- added by dewok 2018-05-03 for jewelry
                beratAll += rs.getDouble("SUM_BERAT");
                srcMaterialRepostingStock.setBerat(beratAll);
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);

                //PstStockCardReport.insertExc(stockCardReport);
            }
             if (qtyAll== 0){
                srcMaterialRepostingStock.setQty(0);
            }

        } catch (Exception e) {
            System.out.println("Err getDataMaterial : " + e.toString());
        }
    }

    
    /**
     * ini proses pencarian penjualan,untuk qty opname
     * proses nya ada di kasir
     * @param srcStockCard
     * @return
     */
    public static Vector getDataMaterialTime(SrcStockCard srcStockCard) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] +
                    " ,SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS R " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS RI " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                        " OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            } else {
                whereClause = " (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                        " OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            // for check invoice cancel or not
            if (whereClause.length() > 0) {
                whereClause += " AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            } else {
                whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }


           // sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                   // " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    //" ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE];
            sql = sql + " GROUP BY R." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];

            //System.out.println("sql sale : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                stockCardReport.setDate(date);

                stockCardReport.setTransaction_type(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]));
                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_SALE);
                stockCardReport.setDocCode(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setKeterangan("Penjualan Barang");

                PstStockCardReport.insertExc(stockCardReport);
            }
        } catch (Exception e) {
            System.out.println("Err getDataMaterial : " + e.toString());
        }
        return list;
    }

    /**
     * ini proses pencarian qty penjualan,untuk qty opname
     * proses nya ada di kasir
     * @param srcStockCard
     * @return
     */
    public static void getQtyStockMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS R " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS RI " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE + ")";
                       // " OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            } else {
                whereClause = " (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +  ")";
                        //" OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            // for check invoice cancel or not
            if (whereClause.length() > 0) {
                whereClause += " AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED+
                               " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                               " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";

            } else {
                whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED +
                              " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                              " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }


            sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];

            //System.out.println("sql sale : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double qtyMaterial = 0;
            double qtyAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();
                
               // stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_SALE);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcStockCard.setQty(qtyAll);
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                
                //PstStockCardReport.insertExc(stockCardReport);
            }
             if (qtyAll== 0){
                srcStockCard.setQty(0);
            }
            
        } catch (Exception e) {
            System.out.println("Err getDataMaterial : " + e.toString());
        }
    }


     /**
     * ini proses pencarian qty penjualan,untuk qty opname return
     * proses nya ada di kasir
     * @param srcStockCard
     * @return
     */
    public static void getQtyStockMaterialReturn(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS R " +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS RI " +
                    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                    " ON RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
                        //" OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            } else {
                whereClause = " (R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
                        //" OR R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR + ")";
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            // for check invoice cancel or not
            if (whereClause.length() > 0) {
                whereClause += " AND " + " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            } else {
                whereClause = " R." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }


            sql = sql + " GROUP BY R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
                    " ,RI." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER];

            //System.out.println("sql sale : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double qtyMaterial = 0;
            double qtyAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

               // stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_SALE);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcStockCard.setQty(qtyAll);
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);

                //PstStockCardReport.insertExc(stockCardReport);
            }
             if (qtyAll== 0){
                srcStockCard.setQty(0);
            }

        } catch (Exception e) {
            System.out.println("Err getDataMaterial : " + e.toString());
        }
    }

    /** adnyana
     * untuk pengecekan jumlah qty barang yang terjual
     * @param oidMaterial
     * @param locationOid
     * @return
     */
    public static double getQtySale(long oidMaterial, long locationOid) {
        DBResultSet dbrs = null;
        double totQty = 0;
        try {
            String sql = "SELECT SUM(DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS DT " +
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM " +
                    " ON DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "= BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " WHERE DT." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + oidMaterial +
                    " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + locationOid +
                    " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TYPE_INVOICE;

            System.out.println("com.dimata.posbo.session.warehouse.SessReportSale.getQtySale: \n" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                totQty = rs.getDouble("SUM_QTY");
            }
        } catch (Exception e) {
            System.out.println("err get penjualan: " + e.toString());
        }
        return totQty;
    }

    /** wpradnyana
     * ini adalah untuk mencari data laporan penjualan berdasar tipe transaksi penjualan
     * Rekap Penjualan per shift / per hari dan per lokasi
     * @param srcReportSale
     * @return Vector
     */
    /* public static Vector getDataRekapPendingOrder(SrcPendingOrder srcReportPendingOrder) {
    DBResultSet dbrs = null;
    Vector list = new Vector(1, 1);  */

    /*
    SELECT
    cash_pending_order.order_number as FRM_FIELD_BOX_ORDER_CODE
    , cash_pending_order.name as FRM_FIELD_CUSTOMER_NAME
    , cash_pending_order.member_id as FRM_FIELD_MEMBER_ID
    , cash_pending_order.creation_date as FRM_FIELD_DATE_CREATED_FROM
    , cash_pending_order.expired_date as FRM_FIELD_DATE_EXPIRED_FROM
    ,cash_pending_order.plan_taken_date as FRM_FIELD_DATE_FINISHED_FROM
    ,cash_pending_order.down_payment as FRM_FIELD_DOWN_PAYMENT_VALUE
    ,location.location_id as FRM_FIELD_LOCATION_ID
    ,app_user.user_id as FRM_FIELD_OPERATOR_ID
    ,app_user.full_name as FRM_FIELD_OPERATOR_NAME
    ,pos_sales_person.sales_name as FRM_FIELD_SALES_NAME
    ,location.name as FRM_FIELD_LOCATION_NAME
    ,pos_sales_person.sales_id as FRM_FIELD_SALES_ID
    FROM
    cash_pending_order
    ,cash_cashier
    ,cash_master
    ,pos_sales_person
    ,location
    ,app_user
    where
    location.location_id = cash_master.location_id
    and
    cash_pending_order.cash_cashier_id=cash_cashier.cash_cashier_id
    and
    cash_master.cash_master_id = cash_cashier.cash_master_id
    and
    pos_sales_person.sales_id = cash_pending_order.sales_id
    and
    app_user.user_id =  cash_cashier.app_user_id
    and
    order_number like '%0%'
    and
    cash_pending_order.name like '%%'
    and
    creation_date  between  '1904-02-01 ' and   '3908-02-01'
    and
    expired_date  between  '1904-02-01 ' and  '3908-02-01'
    and
    plan_taken_date  between  '1904-02-01 ' and  '3908-02-01'
    and
    down_payment > 0.0
    and
    location.location_id =  0
    and
    app_user.user_id =  0
    and
    pos_sales_person.sales_name like '%%'
    order by  cash_pending_order.EXPIRED_DATE
     */
    /*  try {
    String sQL = "" +
    "\n SELECT " +
    "\n cash_pending_order.order_number as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_BOX_ORDER_CODE]+", " +
    "\n cash_pending_order.name as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_CUSTOMER_NAME]+", " +
    "\n cash_pending_order.member_id as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_MEMBER_ID]+", " +
    "\n cash_pending_order.creation_date as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_CREATED_FROM]+", " +
    "\n cash_pending_order.expired_date as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_EXPIRED_FROM]+"," +
    "\n cash_pending_order.plan_taken_date as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_FINISHED_FROM]+"," +
    "\n cash_pending_order.down_payment as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DOWN_PAYMENT_VALUE]+"," +
    "\n location.location_id as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_LOCATION_ID]+"," +
    "\n app_user.user_id as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_OPERATOR_ID]+"," +
    "\n app_user.full_name as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_OPERATOR_NAME]+"," +
    "\n pos_sales_person.sales_name as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SALES_NAME]+"," +
    "\n location.name as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_LOCATION_NAME]+"," +
    "\n pos_sales_person.sales_id as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SALES_ID]+", " +
    "\n cash_bill_main.cash_bill_main_id as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_PAID_INV_ID]+", " +
    "\n cash_bill_main.invoice_number as "+FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_PAID_INV_NUMBER]+"" +
    "\n FROM " +
    "\n `cash_pending_order` " +
    "\n ,cash_cashier" +
    "\n ,cash_master" +
    "\n ,pos_sales_person" +
    "\n ,location" +
    "\n ,app_user" +
    "\n left join " +
    "\n cash_bill_main on cash_bill_main.cash_pending_order_id = cash_pending_order.cash_pending_order_id" +
    "\n" +
    "\n  where " +
    "\n  location.location_id = cash_master.location_id" +
    "\n  and " +
    "\n  cash_pending_order.cash_cashier_id=cash_cashier.cash_cashier_id" +
    "\n  and " +
    "\n  cash_master.cash_master_id = cash_cashier.cash_master_id " +
    "\n  and " +
    "\n  pos_sales_person.sales_id = cash_pending_order.sales_id" +
    "\n  and" +
    "\n  app_user.user_id =  cash_cashier.app_user_id " ;
    String whereClause = "";
    String useBoxOrder = "";
    if(srcReportPendingOrder.getBoxOrderCode ().length ()>0){
    useBoxOrder = "\n  order_number like '%"+srcReportPendingOrder.getBoxOrderCode ()+"%' " ;
    }
    String useCustomerName = "";
    if(srcReportPendingOrder.getCustomerName ().length ()>0){
    useCustomerName = " \n cash_pending_order.name like '%"+srcReportPendingOrder.getCustomerName ()+"%'" ;
    }
    String useCreationDate = "";
    //if(srcReportPendingOrder.getBoxOrderCode ().length ()>0){
    useCreationDate = "\n  creation_date " +
    " between " +
    " '"+ Formater.formatDate(srcReportPendingOrder.getDateCreatedFrom (), "yyyy-MM-dd")
    +" ' and " +
    "  '"+Formater.formatDate(srcReportPendingOrder.getDateCreatedTo (), "yyyy-MM-dd")+"'"  ;
    String useExpiredDate = "";
    useExpiredDate = "\n  expired_date " +
    " between " +
    " '"+ Formater.formatDate(srcReportPendingOrder.getDateExpiredFrom (), "yyyy-MM-dd")
    +" ' and " +
    " '"+Formater.formatDate(srcReportPendingOrder.getDateExpiredTo(), "yyyy-MM-dd")+ "'" ;
    String useFinishDate =  "" ;
    useFinishDate  = "\n plan_taken_date " +
    " between " +
    " '"+ Formater.formatDate(srcReportPendingOrder.getDateFinishedFrom (), "yyyy-MM-dd")
    +" ' and " +
    " '"+Formater.formatDate(srcReportPendingOrder.getDateFinishedTo (), "yyyy-MM-dd")+  "'";
    String useDownPayment = "" ;
    if(srcReportPendingOrder.getDownaPaymentValue ()>0){
    useDownPayment = "\n  down_payment > "+srcReportPendingOrder.getDownaPaymentValue ()+" ";
    }
    String useLocation = "";
    if(srcReportPendingOrder.getLocationId ()>0){
    useLocation = "\n  location.location_id = "+srcReportPendingOrder.getLocationId()+"";
    }
    String useKasir = "";
    if(srcReportPendingOrder.getOperatorId ()>0){
    useKasir = "\n  app_user.user_id = "+srcReportPendingOrder.getOperatorId ()+"" ;
    }
    String useSales =  "" ;
    if(srcReportPendingOrder.getSalesId ()>0){
    useSales = "\n  pos_sales_person.sales_name like '%"+srcReportPendingOrder.getSalesName ()+"%' " ;
    }
    if (useBoxOrder.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useBoxOrder;
    } else {
    whereClause = " and " + whereClause + useBoxOrder;
    }
    }
    if (useCustomerName.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useCustomerName;
    } else {
    whereClause = " and  " + whereClause + useCustomerName;
    }
    }
    if (useDownPayment.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useDownPayment;
    } else {
    whereClause = " and " + whereClause + useDownPayment;
    }
    }
    if (useCreationDate.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useCreationDate;
    } else {
    whereClause = " and " + whereClause + useCreationDate;
    }
    }
    if (useFinishDate.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useFinishDate;
    } else {
    whereClause = " and " + whereClause + useFinishDate;
    }
    }
    if (useExpiredDate.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useExpiredDate;
    } else {
    whereClause = " and " + whereClause + useExpiredDate;
    }
    }
    if (useLocation.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useLocation;
    } else {
    whereClause = " and " + whereClause + useLocation;
    }
    }
    if (useKasir.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useKasir;
    } else {
    whereClause = " and " + whereClause + useKasir;
    }
    }
    if (useSales.length() > 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " and " + useSales;
    } else {
    whereClause = " and " + whereClause + useSales;
    }
    }
    String orderClause = "" ;
    orderClause = "\n  order by cash_pending_order."+FrmSrcPendingOrder.sortMap[srcReportPendingOrder.getSortMethod ()]+"";
    sQL = sQL +whereClause +orderClause;
    System.out.println("sSQL >"+sQL);
    dbrs = DBHandler.execQueryResult(sQL);
    ResultSet rs = dbrs.getResultSet();
    while(rs.next()){
    SrcPendingOrder srcResult = new SrcPendingOrder();
    srcResult.setLocationId(rs.getLong(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_LOCATION_ID]));
    //srcResult.setShiftId(rs.getLong(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SHIFT_ID]));
    srcResult.setOperatorId(rs.getLong(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_OPERATOR_ID]));
    srcResult.setBoxOrderCode (rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_BOX_ORDER_CODE]));
    srcResult.setCustomerName(rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_CUSTOMER_NAME]));
    srcResult.setDateCreatedFrom (rs.getDate(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_CREATED_FROM]));
    //srcResult.setDateCreatedTo(rs.getDate(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_CREATED_TO]));
    srcResult.setDateExpiredFrom (rs.getDate(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_EXPIRED_FROM]));
    //srcResult.setDateExpiredTo (rs.getDate(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_EXPIRED_TO]));
    srcResult.setDateFinishedFrom (rs.getDate(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_FINISHED_FROM]));
    //srcResult.setDateFinishedTo(rs.getDate(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_FINISHED_TO]));
    srcResult.setDownaPaymentValue (rs.getDouble(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DOWN_PAYMENT_VALUE]));
    srcResult.setSalesName(rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SALES_NAME]));
    //srcResult.setSortMethod (rs.getInt(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SORT_METHOD]));
    srcResult.setOperatorName (rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_OPERATOR_NAME]));
    srcResult.setLocationName (rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_LOCATION_NAME]));
    srcResult.setSalesId (rs.getLong(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SALES_ID]));
    //srcResult.setShiftName (rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SHIFT_NAME]));
    srcResult.setMemberId (rs.getLong(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_MEMBER_ID]));
    //srcResult.setMemberName (rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_MEMBER_NAME]));
    srcResult.setPaidInvoiceId (rs.getLong(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_PAID_INV_ID]));
    srcResult.setPaidInvoiceNumber (rs.getString(FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_PAID_INV_NUMBER]));
    list.add(srcResult);
    }
    } catch (Exception e) {
    System.out.println("==>> ERROR getDataRekapPenjualan "+e.toString());
    return new Vector(1,1);
    }
    return list;
    }*/

    /*public static void main(String args[]) {*/
    /*Vector list = PstBillDetail.list(0,0,"","");
    if(list!=null && list.size()>0){
    System.out.println("==>> Start Update: "+list.size()+" Item");
    Material mat = new Material();
    for(int k=0;k<list.size();k++){
    Billdetail billDetail = (Billdetail)list.get(k);
    String where = PstMaterial.fieldNames[PstMaterial.FLD_SKU]+"='"+billDetail.getSku()+"'";
    Vector vtmat = PstMaterial.list(0,0,where,"");
    if(vtmat!=null && vtmat.size()>0){
    mat = (Material)vtmat.get(0);
    }
    if(mat.getOID()!=billDetail.getMaterialId()){
    System.out.println("==>> Update Detail mat new :"+mat.getOID()+"<>old:"+billDetail.getMaterialId());
    billDetail.setMaterialId(mat.getOID());
    try{
    PstBillDetail.updateExc(billDetail);
    }catch(Exception e){}
    }
    }
    System.out.println("==>> End Update: "+list.size()+" Item");
    }*/

    /*SrcPendingOrder srcReportPendingOrder = new SrcPendingOrder();
    srcReportPendingOrder.setLocationId(0);
    srcReportPendingOrder.setShiftId(0);
    srcReportPendingOrder.setOperatorId(0);
    srcReportPendingOrder.setBoxOrderCode ("2");
    srcReportPendingOrder.setCustomerName("cah");
    srcReportPendingOrder.setDateCreatedFrom (new Date(4,01,01));
    srcReportPendingOrder.setDateCreatedTo(new Date(2008,01,01));
    srcReportPendingOrder.setDateExpiredFrom (new Date(4,01,01));
    srcReportPendingOrder.setDateExpiredTo (new Date(2008,01,01));
    srcReportPendingOrder.setDateFinishedFrom (new Date(4,01,01));
    srcReportPendingOrder.setDateFinishedTo(new Date(2008,01,01));
    srcReportPendingOrder.setDownaPaymentValue (0);
    srcReportPendingOrder.setSalesName("ida");
    srcReportPendingOrder.setSortMethod (1);
    srcReportPendingOrder.setOperatorName ("");
    srcReportPendingOrder.setOperatorId(0);
    srcReportPendingOrder.setLocationName ("");
    srcReportPendingOrder.setLocationId(0);
    srcReportPendingOrder.setSalesId (0);
    srcReportPendingOrder.setShiftName ("");
    srcReportPendingOrder.setMemberId (0);
    srcReportPendingOrder.setMemberName ("cah");
    Vector result = new Vector();
    try{
    result = SessReportSale.getDataRekapPendingOrder (srcReportPendingOrder);
    Enumeration enResult = result.elements ();
    while(enResult.hasMoreElements ()){
    SrcPendingOrder srcResult = (SrcPendingOrder )enResult.nextElement ();
    System.out.println (FrmSrcPendingOrder.toString (srcResult));
    }
    }catch(Exception e){
    }
    System.exit (0);
    }*/

    /*public static void getStockCardReport(SrcStockCard srcStockCard) {
    DBResultSet dbrs = null;
    try {
    String sql = "SELECT" +
    " R." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
    " ,R." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
    " ,SUM(RI." + PstBilldetail.fieldNames[PstBilldetail.FLD_QTY_OPNAME] + ") AS SUM_QTY " +
    " FROM " + PstBillMain.TBL_MAT_STOCK_OPNAME+ " AS R " +
    " INNER JOIN " + PstBilldetail.TBL_STOCK_OPNAME_ITEM+ " AS RI " +
    " ON R." + PstBillMain.fieldNames[PstBillMain.FLD_STOCK_OPNAME_ID] +
    " = RI." + PstBilldetail.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID];
    String whereClause = "";
    if (srcStockCard.getMaterialId() != 0) {
    whereClause = "RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
    }
    if (srcStockCard.getLocationId() != 0) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
    } else {
    whereClause = "R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
    }
    }
    if ((srcStockCard.getStardDate() != null) && (srcStockCard.getStardDate() != null)) {
    if (whereClause.length() > 0) {
    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
    } else {
    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
    }
    }
    if (whereClause.length() > 0) {
    sql = sql + " WHERE " + whereClause;
    }
    sql = sql + " GROUP BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
    sql = sql + " ORDER BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
    //System.out.println("sql receive : "+sql);
    dbrs = DBHandler.execQueryResult(sql);
    ResultSet rs = dbrs.getResultSet();
    while (rs.next()) {
    StockCardReport stockCardReport = new StockCardReport();
    stockCardReport.setDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
    stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_OPN);
    stockCardReport.setDocCode(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]));
    stockCardReport.setQty(rs.getInt("SUM_QTY"));
    stockCardReport.setKeterangan("Opname Barang");
    PstStockCardReport.insertExc(stockCardReport);
    }
    } catch (Exception e) {
    System.out.println("err getDataMaterial : "+e.toString());
    }
    }*/
    public static void clearTransaksi(Date manualDate, int type) {
        DBResultSet dbrs = null;
        String strtype = ">";
        if (type == 1) {
            strtype = "<";
        }
        try {
            String sql = "SELECT * FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " WHERE BILL_DATE " + strtype + " '" + Formater.formatDate(manualDate, "yyyy-MM-dd 00:00:01") + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long oidMain = rs.getLong("CASH_BILL_MAIN_ID");
                System.out.println("oid bill main : " + oidMain);

                // ------------------------------------------- pendeletetan item
                sql = "SELECT * FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " WHERE " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidMain;
                // DBResultSet.close(dbrs);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rsi = dbrs.getResultSet();
                while (rsi.next()) { // pendeleten serial code item
                    sql = "DELETE FROM " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE + " WHERE " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] + "=" + rsi.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]);
                    DBHandler.execUpdate(sql);

                    sql = "DELETE FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " WHERE " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] + "=" + rsi.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]);
                    ;
                    DBHandler.execUpdate(sql);
                }
                // rsi.close();
                System.out.println("PstBillDetail.TBL_CASH_BILL_DETAIL");
                // pendeletetan item main
                sql = "DELETE FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " WHERE " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidMain;
                DBHandler.execUpdate(sql);

                System.out.println("PstCashPayment.TBL_PAYMENT");
                // -------------------------- pendeletetan payment
                sql = "SELECT * FROM " + PstCashPayment.TBL_PAYMENT + " WHERE " + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + "=" + oidMain;
                //DBResultSet.close(dbrs);
                dbrs = DBHandler.execQueryResult(sql);
                rsi = dbrs.getResultSet();
                while (rsi.next()) {
                    System.out.println("PstCashCreditCard.TBL_CREDIT_CARD");
                    sql = "DELETE FROM " + PstCashCreditCard.TBL_CREDIT_CARD + " WHERE " + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + "=" + rsi.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]);
                    DBHandler.execUpdate(sql);

                    // delete
                    System.out.println("PstCashPayment.TBL_PAYMENT");
                    sql = "DELETE FROM " + PstCashPayment.TBL_PAYMENT + " WHERE " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID] + "=" + rsi.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]);
                    DBHandler.execUpdate(sql);
                }


                System.out.println("PstCashReturn.TBL_RETURN");
                // -------------------------- pendeletetan return payment
                sql = "DELETE FROM " + PstCashReturn.TBL_RETURN + " WHERE " + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + "=" + oidMain;
                DBHandler.execUpdate(sql);

                System.out.println("PstRecipe.TBL_CASH_RECIPE");
                // -------------------------- pendeletetan cash recipe
                sql = "DELETE FROM " + PstRecipe.TBL_CASH_RECIPE + " WHERE " + PstRecipe.fieldNames[PstRecipe.FLD_CASH_BILL_MAIN_ID] + "=" + oidMain;
                DBHandler.execUpdate(sql);

                System.out.println("PstOtherCost.TBL_CASH_OTHER_COST");
                // -------------------------- pendeletetan cash other cost
                sql = "DELETE FROM " + PstOtherCost.TBL_CASH_OTHER_COST + " WHERE " + PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID] + "=" + oidMain;
                DBHandler.execUpdate(sql);

                System.out.println("PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN");
                // ------------------------------------------- pendeletetan kredit item
                sql = "SELECT * FROM " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " WHERE " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + "=" + oidMain;


                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rsx = dbrs.getResultSet();
                while (rsx.next()) { // pendeleten serial code item
                    sql = "SELECT * FROM " + PstCashCreditPayment.TBL_PAYMENT + " WHERE " + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID] + "=" + rsx.getLong(PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]);
                    //DBResultSet.close(dbrs);
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rsp = dbrs.getResultSet();
                    while (rsp.next()) { // pendeleten serial code item

                        System.out.println("PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO");
                        sql = "DELETE FROM " + PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO + " WHERE " + PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID] + "=" + rsp.getLong(PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]);
                        DBHandler.execUpdate(sql);

                        System.out.println("PstCashCreditPayment.TBL_PAYMENT");
                        sql = "DELETE FROM " + PstCashCreditPayment.TBL_PAYMENT + " WHERE " + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID] + "=" + rsp.getLong(PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]);
                        DBHandler.execUpdate(sql);
                    }

                    System.out.println("PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN");
                    // pendeletetan item main
                    sql = "DELETE FROM " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " WHERE " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID] + "=" + rsx.getLong(PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]);
                    DBHandler.execUpdate(sql);
                }

                sql = "DELETE FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + "=" + oidMain;
                DBHandler.execUpdate(sql);
            }

            // ------------------------------------------- pendeletetan kredit item
            sql = "SELECT * FROM " + PstCashCashier.TBL_CASH_CASHIER + " WHERE " + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " " + strtype + " '" + Formater.formatDate(manualDate, "yyyy-MM-dd") + "'";
            //DBResultSet.close(dbrs);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rsi = dbrs.getResultSet();
            while (rsi.next()) {
                System.out.println("PstBalance.TBL_BALANCE");
                sql = "DELETE FROM " + PstBalance.TBL_BALANCE + " WHERE " + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "=" + rsi.getLong(PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]);
                DBHandler.execUpdate(sql);

                System.out.println("PstCashCashier.TBL_CASH_CASHIER");
                sql = "DELETE FROM " + PstCashCashier.TBL_CASH_CASHIER + " WHERE " + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + "=" + rsi.getLong(PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]);
                DBHandler.execUpdate(sql);
            }

        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        }
    }

    public static void main(String[] args) {
        try {
            Date date = new Date(2005 - 1900, 11, 31, 23, 59, 59);

            // 0 = >
            // 1 = <
            System.out.println("Tanggal select : " + date);
            clearTransaksi(date, 0);

        } catch (Exception e) {
        }
    }

    public static Vector ReportBrand(SrcReportSale srcReportSale) {
        Vector list = new Vector();
        DBResultSet dbrs = null;   
        try {    
            String sql = "select pmr.code, sum(bd.qty) as total_qty ,pmr.name from cash_bill_detail as bd " +
                    " inner join pos_material as pm on bd.material_id=pm.material_id " +
                    " inner join pos_ksg pmr on pm.gondola_code=pmr.ksg_id " +
                    " inner join cash_bill_main as  bm on bd.cash_bill_main_id=bm.cash_bill_main_id ";
            sql = sql + " where bm.bill_date between '" + Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' " +
                    " and '" + Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd 23:59:59") + "' ";
  
            if (srcReportSale.getLocationId() != 0) { 
                sql = sql + " and bm.location_id=" + srcReportSale.getLocationId();
            }
            if (srcReportSale.getShiftId() != 0) {
                sql = sql + " and bm.shift_id=" + srcReportSale.getShiftId();
            }
            if (srcReportSale.getCurrencyId() != 0) {
                sql = sql + " and bm.currency_id=" + srcReportSale.getCurrencyId();
            }
            sql = sql + " group by pmr.code";

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector litem = new Vector(1, 1);
            int i = 0;
            while (rs.next()) {
                i++;
                litem = new Vector(1, 1);
                litem.add(String.valueOf(i));
                litem.add(String.valueOf(rs.getString("code")));
                litem.add(String.valueOf(rs.getInt("total_qty")));
                litem.add(String.valueOf(rs.getString("name")));
                list.add(litem);
            }
        } catch (Exception e) {
            return new Vector();
        }
        return list;
    }

    public static Vector ReportRekapKategori(SrcReportSale srcReportSale) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "select pm.sku , pm.name as prod_name ,pmr.name, " +
                    " sum(bd.qty) as total_qty ,pmr.name, sum(bd.total_price) as total_price, " +
                    
                    //" bd.cost from cash_bill_detail as bd " +
                    " sum(bd.cost*bd.qty) as cost, sum(bd.total_tax) as total_tax, sum(bd.total_service) as total_service, sum(bd.disc_global) as disc  from cash_bill_detail as bd " +
                    " inner join pos_material as pm on bd.material_id=pm.material_id " +
                    " inner join pos_merk pmr on pm.merk_id=pmr.merk_id " +
                    " inner join cash_bill_main as  bm on bd.cash_bill_main_id=bm.cash_bill_main_id ";

            if (srcReportSale.getSupplierId() != 0) {
                sql = sql + " inner join pos_vendor_price as vp on pm.material_id=vp.material_id";
            }
  
            sql = sql + " where bm.bill_date between '" + Formater.formatDate(srcReportSale.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' " +
                        " and '" + Formater.formatDate(srcReportSale.getDateTo(), "yyyy-MM-dd 23:59:59") + "' "+
                        " and bm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                                  " and (bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                                  " or bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                                  " and (bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                                  " or bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
            
            if (srcReportSale.getSupplierId() != 0) {
                sql = sql + " and vp.vendor_id=" + srcReportSale.getSupplierId();
            }

            if (srcReportSale.getLocationId() != 0) {
                sql = sql + " and bm.location_id=" + srcReportSale.getLocationId();
            } 

            if (srcReportSale.getShiftId() != 0) {
                sql = sql + " and bm.shift_id=" + srcReportSale.getShiftId();
            }

            if (srcReportSale.getCurrencyId() != 0) {
                sql = sql + " and bm.currency_id=" + srcReportSale.getCurrencyId();
            }

            if (srcReportSale.getCategoryId() != 0) {
                sql = sql + " and pm.category_id=" + srcReportSale.getCategoryId();
            }
            sql = sql + " group by pm.material_id order by pm.merk_id ";

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector litem = new Vector(1, 1);
            int i = 0;
            while (rs.next()) {
                i++;
                litem = new Vector(1, 1);
                litem.add(String.valueOf(i));//0
                litem.add(String.valueOf(rs.getString("sku")));//1
                litem.add(String.valueOf(rs.getString("prod_name")));//2
                litem.add(String.valueOf(rs.getInt("total_qty")));//3
                litem.add(String.valueOf(rs.getDouble("total_price")));//4
                litem.add(String.valueOf(rs.getString("name")));//5
                litem.add(String.valueOf(rs.getDouble("cost")));//6
                //adding tax & service
                // by mirahu 20120316
                litem.add(String.valueOf(rs.getDouble("total_tax")));//7
                litem.add(String.valueOf(rs.getDouble("total_service")));//8
                
                //add opie-eyek 20160809
                //add discount
                litem.add(String.valueOf(rs.getDouble("disc")));//9
                
                list.add(litem);
            }
        } catch (Exception e) {
            return new Vector();
        }
        return list;
    }
}
