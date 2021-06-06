package com.dimata.posbo.session.warehouse;

/* java package */

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.util.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.location.*;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.form.search.*;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;

public class SessReportReceive {
    
    // constant for session name for warehouse report
    public static final String SESS_SRC_REPORT_RECEIVE = "SESSION_SRC_REPORT_RECEIVE";
    public static final String SESS_SRC_REPORT_RECEIVE_INVOICE = "SESSION_SRC_REPORT_RECEIVE_INVOICE";
    public static final String SESS_SRC_REPORT_RECEIVE_DISTRIBUTE = "SESSION_SRC_REPORT_RECEIVE_DISTRIBUTE";
    public static final String SESS_SRC_REPORT_RECEIVE_REKAP_TANGGAL_KATEGORI = "SESS_SRC_REPORT_RECEIVE_REKAP_TANGGAL_KATEGORI";
    public static final String SESS_SRC_REPORT_RECEIVE_REKAP_TANGGAL_KATEGORI_INTERNAL = "SESS_SRC_REPORT_RECEIVE_REKAP_TANGGAL_KATEGORI_INTERNAL";
    
    // constant for session name for store report
    public static final String SESS_SRC_REPORT_RECSUPPPERSUPP = "SESS_SRC_REPORT_RECSUPPPERSUPP";
    public static final String SESS_SRC_REPORT_RECSUPPPERINVOICE = "SESS_SRC_REPORT_RECSUPPPERINVOICE";
    public static final String SESS_SRC_REPORT_RECSUPPREKAPPERINVOICE = "SESS_SRC_REPORT_RECSUPPREKAPPERINVOICE";
    public static final String SESS_SRC_REPORT_RECWHPERINVOICE = "SESS_SRC_REPORT_RECWHPERINVOICE";
    public static final String SESS_SRC_REPORT_RECWHREKAPPERINVOICE = "SESS_SRC_REPORT_RECWHREKAPPERINVOICE";
    
    public static final String SESS_SRC_REPORT_RECEIVE_RETURN = "SESSION_SRC_REPORT_RECEIVE_RETURN";
    
    
    /**
     * @param srcReportReceive
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceive(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] + // nana kategory
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + // sebenarnya sub category
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +        
            " FROM (((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " ) " + /*INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +*/
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strCurrencyType = "";
            if(srcReportReceive.getCurrencyId() != 0) {
                strCurrencyType = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID] + "=" + srcReportReceive.getCurrencyId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strCurrencyType.length() > 0) {
                if(whereClause.length() > 0) {
                    whereClause += " AND " + strCurrencyType;
                }
                else {
                    whereClause += strCurrencyType;
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
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            sql += " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE];
            sql = sql + ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE];
            
            System.out.println("SQL SessReportReceive.getReportReceive(): " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive rec = new MatReceive();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                ContactList cnt = new ContactList();
                
                rec.setOID(rs.getLong(1));
                rec.setLocationId(rs.getLong(2));
                rec.setReceiveDate(rs.getDate(3));
                rec.setSupplierId(rs.getLong(4));
                rec.setRecCode(rs.getString(14));
                rec.setInvoiceSupplier(rs.getString(18));
                rec.setRemark(rs.getString(19));
                rec.setTransRate(rs.getDouble("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));
                temp.add(rec);
                
                recItem.setExpiredDate(rs.getDate(8));
                recItem.setCost(rs.getDouble(9));
                recItem.setQty(rs.getDouble(10));
                recItem.setTotal(rs.getDouble(11));
                temp.add(recItem);
                
                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultPrice(rs.getDouble(15));
                mat.setBarCode(rs.getString("BARCODE"));
                mat.setOID(rs.getLong("MATERIAL_ID"));
                temp.add(mat);
                
                unt.setCode(rs.getString(7));
                temp.add(unt);
                
                //curr.setCode(rs.getString(10));
                temp.add(curr);
                
                cat.setName(rs.getString(12));
                temp.add(cat);
                
                //scat.setName(rs.getString(14));
                //scat.setCode(rs.getString(18));
                temp.add(scat);
                
                cnt.setCompName(rs.getString(16));
                temp.add(cnt);
                
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
    
    public static Vector getReportReceiveSummary(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = ""
                + " SELECT "
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+", "
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]+", "
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+", "
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]+", "            
                + " SUM(RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+")"
                + " AS "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+", "
                + " SUM(RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+")"
                + " AS "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+", "
                + " SUM(MAT."+PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+" * RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+")"
                + " AS "+PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+","
                + " CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+",  "
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+","
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]+","
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID]+"," 
                + " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]+" "+
                " FROM (((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
                " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
                " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
                " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                " ) " + /*INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +*/
                " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
                " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getMultiLocation().length()>0){
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " IN (" + srcReportReceive.getMultiLocation()+")";
            }else{
                if (srcReportReceive.getLocationId() != 0) {
                    strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
                }
            }
            
            String strCurrencyType = "";
            if(srcReportReceive.getCurrencyId() != 0) {
                strCurrencyType = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID] + "=" + srcReportReceive.getCurrencyId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strCurrencyType.length() > 0) {
                if(whereClause.length() > 0) {
                    whereClause += " AND " + strCurrencyType;
                }
                else {
                    whereClause += strCurrencyType;
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
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            sql += " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            sql = sql + " GROUP BY REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+"";
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE];
            sql = sql + ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE];
            
            
            
            System.out.println("SQL SessReportReceive.getReportReceive(): " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive rec = new MatReceive();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();             
                ContactList cnt = new ContactList();
                
                rec.setOID(rs.getLong(""+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+""));
                rec.setLocationId(rs.getLong(""+PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]+""));
                rec.setReceiveDate(rs.getDate(""+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+""));
                rec.setSupplierId(rs.getLong(""+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]+""));
                rec.setRecCode(rs.getString(""+PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]+""));
                rec.setInvoiceSupplier(rs.getString(""+PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+""));
                rec.setRemark(rs.getString(""+PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]+""));  
                rec.setPurchaseOrderId(rs.getLong(""+PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID]+""));
                temp.add(rec);

                recItem.setQty(rs.getDouble(""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+""));
                recItem.setTotal(rs.getDouble(""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+""));
                temp.add(recItem);

                mat.setDefaultPrice(rs.getDouble(""+PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+""));            
                temp.add(mat);
                
                cnt.setCompName(rs.getString(""+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+""));
                temp.add(cnt);
                
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
     * Used to get data for report Receive From Supplier Per Invoice
     * @param srcReportReceive
     * @return
     * @created by Edhy
     */
    public static Vector getReportReceiveFromSuppPerInvoice(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS MATNAME" +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CATNAME" +
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " AS SCATNAME" +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            // ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK] +
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
            
                    /*" LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                    " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            //String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            //        " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO + " ) ";
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID] +
            " = 0 ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println("SessReportReceive.getReportReceiveFromSuppPerInveoice () " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive rec = new MatReceive();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                ContactList cnt = new ContactList();
                
                rec.setOID(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
                rec.setLocationId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]));
                rec.setReceiveDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
                rec.setSupplierId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
                rec.setRecCode(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                //System.out.println("CODE at rec :::: "+rec.getRecCode());
                rec.setInvoiceSupplier(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]));
                rec.setRemark(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]));
                temp.add(rec);
                
                recItem.setExpiredDate(rs.getDate(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE]));
                recItem.setCost(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
                recItem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
                recItem.setTotal(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]));
                temp.add(recItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString("MATNAME"));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(mat);
                
                //NOTE : DIGUNAKAN ANGKA KRN ADA FIELD YANG SAMA
                unt.setCode(rs.getString(7));
                //System.out.println("CODE at unit ::: "+unt.getCode());
                temp.add(unt);
                
                
                curr.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
                
                cat.setName(rs.getString("CATNAME"));
                temp.add(cat);
                
                //scat.setName(rs.getString("SCATNAME"));
                // scat.setCode(rs.getString(PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]));
                temp.add(scat);
                
                //cnt.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                temp.add(cnt);
                
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
     * @param srcReportReceive
     * @return
     * @created by Edhy
     */
    public static Vector getReportReceiveInternal(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            
/*", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
 */
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            " FROM ((((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
            " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " ) "; /*INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
" ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
" = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";//Utk Internal ambil suppliernya dari Material
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_DISPATCH +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_RETURN + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive rec = new MatReceive();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                Location loc = new Location();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                rec.setOID(rs.getLong(1));
                rec.setLocationId(rs.getLong(2));
                rec.setReceiveDate(rs.getDate(3));
                rec.setSupplierId(rs.getLong(4));
                rec.setRecCode(rs.getString(14));
                rec.setInvoiceSupplier(rs.getString(17));
                temp.add(rec);
                
                recItem.setExpiredDate(rs.getDate(8));
                recItem.setCost(rs.getDouble(9));
                recItem.setQty(rs.getDouble(11));
                recItem.setTotal(rs.getDouble(12));
                temp.add(recItem);
                
                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultPrice(rs.getDouble(15));
                temp.add(mat);
                
                unt.setCode(rs.getString(7));
                temp.add(unt);
                
                loc.setName(rs.getString(13));
                temp.add(loc);
                
                curr.setCode(rs.getString(10));
                temp.add(curr);
                
                cat.setName(rs.getString(16));
                temp.add(cat);
                
                //scat.setName(rs.getString(17));
                //scat.setCode(rs.getString(18));
                temp.add(scat);
                
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
     * @param srcReportReceive
     * @return
     * @created by Edhy
     */
    public static Vector getReportReceiveFromWhPerInv(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS MATNAME" +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " AS UNT_" + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            /*", CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] +*/
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CATNAME" +
            
                    /*", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " AS SCATNAME" +
                    ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +*/
            
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK] +
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    /*" INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " CURR" +
                    " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                    " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +*/
            " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
            
                    /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_DISPATCH + " ) ";
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println("SessReportReceive.getReportReceiveFromWhPerInv() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive rec = new MatReceive();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                Location loc = new Location();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                rec.setOID(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
                rec.setLocationId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]));
                rec.setReceiveDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
                rec.setSupplierId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
                rec.setRecCode(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                rec.setInvoiceSupplier(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]));
                rec.setRemark(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]));
                temp.add(rec);
                
                recItem.setExpiredDate(rs.getDate(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE]));
                recItem.setCost(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
                recItem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
                recItem.setTotal(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]));
                temp.add(recItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString("MATNAME"));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(mat);
                
                unt.setCode(rs.getString("UNT_" + PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
                loc.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                temp.add(loc);
                
                //curr.setCode(rs.getString(PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE]));
                temp.add(curr);
                
                cat.setName(rs.getString("CATNAME"));
                temp.add(cat);
                
                //scat.setName(rs.getString("SCATNAME"));
                //scat.setCode(rs.getString(PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]));
                temp.add(scat);
                
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
    
    
    //Untuk menampilkan laporan distribusi barang
    /**
     * @param oidREC
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceiveDistribute(long oidREC) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {
            
            String sql = "SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " FROM (" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE] +
            " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
            " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = " + oidREC;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                
                double qtyRec = rs.getDouble(3);
                //Add Item
                temp.add(rs.getString(4));
                temp.add(rs.getString(5));
                temp.add("" + qtyRec);
                temp.add("");
                temp.add("");
                temp.add("");
                temp.add("");
                temp.add("");
                result.add(temp);
                //System.out.println("TEMP 1:" + temp);
                //Add Detail for Dispatch
                temp = new Vector();
                temp = getListDistributed(rs.getLong(2), rs.getString(1));
                for (int i = 0; i < temp.size(); i++) {
                    Vector temp2 = (Vector) temp.get(i);
                    Vector temp3 = new Vector();
                    temp3.add("");
                    temp3.add("");
                    temp3.add("");
                    temp3.add((String) temp2.get(0));
                    temp3.add((String) temp2.get(1));
                    temp3.add((String) temp2.get(2));
                    temp3.add((String) temp2.get(3));
                    qtyRec = qtyRec - Double.parseDouble((String) temp2.get(3));
                    if (i == (temp.size() - 1))
                        temp3.add("" + qtyRec);
                    else
                        temp3.add("");
                    result.add(temp3);
                    //System.out.println("TEMP 2 :" + temp3);
                }
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    //Ambil detil pengiriman untuk tiap-tiap item di Dispatch Material Item
    /**
     * @param oidMaterial
     * @param invoiceSupplier
     * @return
     * @update by Edhy
     */
    private static Vector getListDistributed(long oidMaterial, String invoiceSupplier) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            
            String sql = " SELECT LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
            ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
            ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
            " FROM (" + PstMatDispatch.TBL_DISPATCH + " DF" +
            " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
            " ON DF. " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
            " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
            " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +
            " = " + PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE +
            " AND DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
            " = " + oidMaterial +
            " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_INVOICE_SUPPLIER] +
            " = '" + invoiceSupplier + "'" +
            " ORDER BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                
                temp.add(rs.getString(1));
                temp.add(Formater.formatDate(rs.getDate(2), "dd-MM-yyyy"));
                temp.add(rs.getString(3));
                temp.add("" + rs.getDouble(4));
                
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
     * @param srcReportReceive
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceiveRekapInvoice(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SLD" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS TOTAL_QTY" +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            " FROM ((((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            ") INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
            
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
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
                temp.add(rs.getString(7));
                
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
     * Used to get data for creating report penerimaan rekap per invoice
     * @param srcReportReceive
     * @return
     * @created by Edhy
     */
    public static Vector getReportReceiveFromSuppRekapPerInvoice(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SLD" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS TOTAL_QTY" +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            " FROM (((((( " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            ") INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = "(REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER + " ) ";
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
            
            sql += " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE];
            
            //System.out.println("###SessReportReceive.getReportReceiveFromSuppRekapPerInvoice() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID])));
                temp.add(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                temp.add(new Double(rs.getDouble("SumOfTOTAL")));
                temp.add(new Double(rs.getDouble("SLD")));
                temp.add(new Double(rs.getDouble("TOTAL_QTY")));
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
    
    
    /**
     * @param srcReportReceive
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceiveInternalRekapInvoice(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS TOTAL_QTY" +
            " FROM ((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI.D" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";//Utk internal ambil suppliernya dari Material
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_DISPATCH +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_RETURN + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE];
            
            sql = sql + " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            //System.out.println(sql);
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
     * @param srcReportReceive
     * @return
     * @created by Edhy
     */
    public static Vector getReportReceiveFromWhRekapInvoice(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS TOTAL_QTY" +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " FROM ((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_DISPATCH + " ) ";
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE];
            
            sql += " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            
            //System.out.println("###SessReportReceive.getReportReceiveFromWhRekapInvoice() sql " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID])));
                temp.add(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                temp.add(new Double(rs.getDouble("SumOfTOTAL")));
                temp.add(new Double(rs.getDouble("PRC")));
                temp.add(new Double(rs.getDouble("TOTAL_QTY")));
                temp.add(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                
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
     * @param srcReportReceive
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceiveTotal(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ")" +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] + // sub category
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE] +
            //adding location + supplier id 20120526
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +   
            ", CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +        
            " FROM ((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            ") INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT " +
            " ON REC." +PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +        
            " ) "; 
            
            /*LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO +" ) ";
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            sql += " WHERE (REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_POSTED;
            sql += " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_CLOSED+")";
            
            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE];
            
            
            if(srcReportReceive.getGroupBy()==0){
                sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }else if (srcReportReceive.getGroupBy()==1){
                sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+
                ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] ;
            }else{
                sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME]+
                ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }
            
            System.out.println("SessReportreceive.getreportReceiveTotal(): \n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                MatReceive matReceive = new MatReceive();
                
                recItem.setExpiredDate(rs.getDate(4));
                recItem.setCost(rs.getDouble(5));
                recItem.setQty(rs.getDouble(6));
                recItem.setTotal(rs.getDouble(7));
                temp.add(recItem);
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultPrice(rs.getDouble(10));
                mat.setBarCode(rs.getString("MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                temp.add(mat);
                
                unt.setCode(rs.getString(3));
                temp.add(unt);
                
                cat.setName(rs.getString(8));
                cat.setOID(rs.getLong("CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]));
                temp.add(cat);
                
                scat.setName(rs.getString(9));
                temp.add(scat);
                
                matReceive.setTransRate(rs.getDouble("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));
                //adding location_id + supplier_id
                matReceive.setLocationId(rs.getLong("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]));
                matReceive.setSupplierId(rs.getLong("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
                matReceive.setSupplierName(rs.getString("CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                temp.add(matReceive);
                
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
     * @param srcReportReceive
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceiveReturnTotal(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ")" +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] + // sub category
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE] +
            //adding location + supplier id 20120526
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +   
            ", CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +        
            " FROM ((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            ") INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT " +
            " ON REC." +PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +        
            " ) "; 
            
            /*LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_RETURN +" ) ";
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            sql += " WHERE (REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_POSTED;
            sql += " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_CLOSED+")";
            
            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE];
            
            
            if(srcReportReceive.getGroupBy()==0){
                sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }else if (srcReportReceive.getGroupBy()==1){
                sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+
                ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] ;
            }else{
                sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME]+
                ", CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }
            
            System.out.println("SessReportreceive.getreportReceiveTotal(): \n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                MatReceive matReceive = new MatReceive();
                
                recItem.setExpiredDate(rs.getDate(4));
                recItem.setCost(rs.getDouble(5));
                recItem.setQty(rs.getDouble(6));
                recItem.setTotal(rs.getDouble(7));
                temp.add(recItem);
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultPrice(rs.getDouble(10));
                mat.setBarCode(rs.getString("MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                temp.add(mat);
                
                unt.setCode(rs.getString(3));
                temp.add(unt);
                
                cat.setName(rs.getString(8));
                cat.setOID(rs.getLong("CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]));
                temp.add(cat);
                
                scat.setName(rs.getString(9));
                temp.add(scat);
                
                matReceive.setTransRate(rs.getDouble("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));
                //adding location_id + supplier_id
                matReceive.setLocationId(rs.getLong("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]));
                matReceive.setSupplierId(rs.getLong("REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
                matReceive.setSupplierName(rs.getString("CNT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                temp.add(matReceive);
                
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
     * This method used get list receive with bonus
     */
    public static Vector getReportReceiveBonus(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ")" +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            /*", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +*/
            /*", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +*/
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    /*" INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " CURR" +
                                    " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                                    " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +*/
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
                    /*" LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                // strSupplierId = " REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                // strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
            " = 0 " +
            " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REASON] + " = " + PstMatReceive.REASON_BONUS +
            " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID] + " = 0";
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            //", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE];
            
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                recItem.setExpiredDate(rs.getDate(4));
                recItem.setCost(rs.getDouble(5));
                recItem.setQty(rs.getDouble(6));
                recItem.setTotal(rs.getDouble(7));
                temp.add(recItem);
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                //mat.setDefaultPrice(rs.getDouble(10));
                temp.add(mat);
                
                unt.setCode(rs.getString(3));
                temp.add(unt);
                
                cat.setName(rs.getString(8));
                temp.add(cat);
                
                // scat.setName(rs.getString(9));
                temp.add(scat);
                
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
     * This method used get list receive with bonus
     */
    public static Vector getReportReceiveExchange(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ")" +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                   /* ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +*/
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    /*" INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " CURR" +
                    " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                    " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +*/
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
                    /*" LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                //strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                //strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
            " = 0 " +
            //Penerimaan barang tukar guling
            " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REASON] + " = " + PstMatReceive.REASON_TUKAR_GULING;
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    /*", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE];*/
            
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                recItem.setExpiredDate(rs.getDate(4));
                recItem.setCost(rs.getDouble(5));
                recItem.setQty(rs.getDouble(6));
                recItem.setTotal(rs.getDouble(7));
                temp.add(recItem);
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                // mat.setDefaultPrice(rs.getDouble(10));
                temp.add(mat);
                
                unt.setCode(rs.getString(3));
                temp.add(unt);
                
                cat.setName(rs.getString(8));
                temp.add(cat);
                
                // scat.setName(rs.getString(9));
                temp.add(scat);
                
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
     * this is special method using generate receive report, used on hanoman tegalsari.
     * @param srcReportReceive
     * @return
     * @created by Edhy
     */
    public static Vector getReportReceiveSubCategory(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] + ") AS TOTAL" +
            " FROM ((((" + PstMatReceive.TBL_MAT_RECEIVE + " AS REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + ")" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
            " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + ")" +
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];
            
            sql = sql + " ORDER BY SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                
                temp.add(rs.getString(1)); // Code
                temp.add(rs.getString(2)); // Name
                temp.add(new Double(rs.getDouble(3))); // Total quantity of item of its category
                temp.add(new Double(rs.getDouble(4))); // Total cost of item of its category
                
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
     * @param srcReportReceive
     * @return
     * @update by Edhy
     */
    public static Vector getReportReceiveInternalTotal(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ")" +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_POINT_PRICE] + // sub category
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
            " FROM ((((((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
            " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " ) ";/* LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";//Utk internal ambil suppliernya dari Material
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_DISPATCH +
            " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
            " = " + PstMatReceive.SOURCE_FROM_RETURN + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            sql += " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_POINT_PRICE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE];
            
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println("getReportReceiveInternalTotal(#)"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                recItem.setExpiredDate(rs.getDate(4));
                recItem.setCost(rs.getDouble(5));
                recItem.setQty(rs.getDouble(6));
                recItem.setTotal(rs.getDouble(7));
                temp.add(recItem);
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setAveragePrice(rs.getDouble(10));
                temp.add(mat);
                
                unt.setCode(rs.getString(3));
                temp.add(unt);
                
                cat.setName(rs.getString(8));
                temp.add(cat);
                
                //scat.setName(rs.getString(9));
                temp.add(scat);
                
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
     * Used to get data for generate receive from supplier report per supplier
     * @param srcReportReceive
     * @return Vector
     * @created by Edhy
     */
    public static Vector getReportReceiveFromSuppPerSupplier(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS MATNAME" +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CATNAME" +
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +  " AS SCATNAME" +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
            
            if (srcReportReceive.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS VP " +
                " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                " = VP." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            /* INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];
                " INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " CURR" +
                " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +
                " )"*/
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = ""; // Utk internal ambil suppliernya dari Material
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = "VP." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                // strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
                sql = sql + " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID] + " = 0 ";
            }else{
                sql = sql + " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID] + " = 0 ";
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            // ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE];
            
            sql += " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            //", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println("###SessReportReceive.getReportReceiveFromSupplierPerSupp : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem recItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                recItem.setExpiredDate(rs.getDate(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE]));
                recItem.setCost(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
                recItem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
                recItem.setTotal(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]));
                temp.add(recItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString("MATNAME"));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(mat);
                
                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
                cat.setName(rs.getString("CATNAME"));
                temp.add(cat);
                
                //scat.setName(rs.getString("SCATNAME"));
                temp.add(scat);
                
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
    
    
    //Laporan nilai apa yg diinginkan
    public static final int INFO_TYPE_PRICE = 0;
    public static final int INFO_TYPE_COST = 1;
    public static final int INFO_TYPE_QTY = 2;
    
    //Untuk menampilkan total penerimaan harian di lokasi tertentu per tanggal
    public static Vector getReportReceiveRekapTanggalKategori(SrcReportReceive srcReportReceive, int reportType) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //List semua kategori order by kode
            Vector listKategori = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CODE]);
            String sql = "SELECT REC.RECEIVE_DATE" +
            " FROM receive_material REC" +
            " INNER JOIN receive_material_item RMI" +
            " ON REC.RECEIVE_MATERIAL_ID" +
            " = RMI.RECEIVE_MATERIAL_ID";
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
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
            
            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY REC.RECEIVE_DATE";
            sql += " ORDER BY REC.RECEIVE_DATE";
            
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
                            temp.add(new Double(sumCategoryReceivePrice(srcReportReceive.getLocationId(), rs.getDate(1),
                            oidCategory, srcReportReceive.getSupplierId(), koneksi)));
                            break;
                        case INFO_TYPE_COST://Cost
                            temp.add(new Double(sumCategoryReceiveCost(srcReportReceive.getLocationId(), rs.getDate(1),
                            oidCategory, srcReportReceive.getSupplierId(), koneksi)));
                            break;
                        case INFO_TYPE_QTY://Qty
                            temp.add(new Double(sumCategoryReceiveQty(srcReportReceive.getLocationId(), rs.getDate(1),
                            oidCategory, srcReportReceive.getSupplierId(), koneksi)));
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
    
    
    public static final int GROUP_BY_TRANSACTION = 0;
    public static final int GROUP_BY_SUPPLIER = 1;
    public static final String[] groupByNames = {"Group Transaction","Group Supplier"};
    
    public static Vector getReportReceiveRekap(SrcReportReceive srcReportReceive, int groupBy) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1); 
        try {
            String sql = "SELECT REC.RECEIVE_CODE, cl.COMP_NAME, sum(RMI.QTY), sum(RMI.TOTAL)" +
            " FROM pos_receive_material REC " +
            " INNER JOIN pos_receive_material_item RMI ON REC.RECEIVE_MATERIAL_ID = RMI.RECEIVE_MATERIAL_ID"+
            " INNER JOIN contact_list as cl on REC.SUPPLIER_ID=cl.CONTACT_ID ";
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
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
            
            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch(groupBy){
                case GROUP_BY_TRANSACTION:
                    sql += " GROUP BY REC.MATERIAL_ID";
                    sql += " ORDER BY REC.MATERIAL_ID";
                    break;
                case GROUP_BY_SUPPLIER:
                    sql += " GROUP BY REC.SUPPLIER_ID";
                    sql += " ORDER BY REC.RECEIVE_DATE";
                    break;
            }
            
            //System.out.println(sql); 
            // Connection koneksi = PstMaterialStock.getLocalConnection();
            // Statement stmt = koneksi.createStatement();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                result.add(temp);
            }
            rs.close();
            
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
     * @param oidSupplier
     * @param koneksi
     * @return
     * @update by Edhy
     */
    
    public static double getTotalPriceReceiveByMemberAndReceiveId(long receiveMaterialId, long standartRateId , long priceTypeid){
        double totalPrice = 0;
        DBResultSet dbrs = null;
        try {
            String sql = ""
                + " SELECT SUM(ptm."+PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]+" * rmi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+") AS PRICE "
                + " FROM "+PstMatReceive.TBL_MAT_RECEIVE+" AS rm "
                + " INNER JOIN "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM+" rmi "
                + " ON rm."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" =  rmi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+" "
                + " INNER JOIN "+PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING+" ptm "
                + " ON ptm."+PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID]+" = rmi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+" "
                + " WHERE rm."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" ='"+receiveMaterialId+"' "
                + " AND ptm."+PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID]+" = '"+standartRateId+"' "
                + " AND ptm."+PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID]+" = '"+priceTypeid+"'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                totalPrice = rs.getDouble("PRICE");
            }
            rs.close();

            
        }catch (Exception e) {
            System.out.println("Err 2 : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return totalPrice;
    }
    
    private static double sumCategoryReceivePrice(long oidLocation, Date filterDate, long oidCategory
    , long oidSupplier, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SumOfTOTAL_PRICE" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strSupplierId = "";
            if (oidSupplier != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + oidSupplier;
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
            
            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
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
     * @param oidSupplier
     * @param koneksi
     * @return
     * @update by Edhy
     */
    private static double sumCategoryReceiveCost(long oidLocation, Date filterDate, long oidCategory
    , long oidSupplier, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS SumOfTOTAL_COST" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strSupplierId = "";
            if (oidSupplier != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + oidSupplier;
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
            
            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
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
     * @param oidSupplier
     * @param koneksi
     * @return
     * @update by edhy
     */
    private static double sumCategoryReceiveQty(long oidLocation, Date filterDate, long oidCategory
    , long oidSupplier, Connection koneksi) {
        double hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS SumOfTOTAL_QTY" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strSupplierId = "";
            if (oidSupplier != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + oidSupplier;
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
            
            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
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
    
    //Untuk menampilkan total pengiriman harian di lokasi tertentu per tanggal
    public static Vector getReportReceiveRekapInternalTanggalKategori(SrcReportReceive srcReportReceive, int reportType) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //List semua kategori order by kode
            Vector listKategori = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CODE]);
            String sql = "SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strSupplierId = "";
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String whereClause = "";
            if (strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if (strSupplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                } else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE];
            sql += " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE];
            
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
                            temp.add(new Double(sumCategoryReceiveInternalPrice(srcReportReceive.getLocationId(), rs.getDate(1),
                            oidCategory, srcReportReceive.getReceiveFrom(), koneksi)));
                            break;
                        case INFO_TYPE_COST://Cost
                            temp.add(new Double(sumCategoryReceiveInternalCost(srcReportReceive.getLocationId(), rs.getDate(1),
                            oidCategory, srcReportReceive.getReceiveFrom(), koneksi)));
                            break;
                        case INFO_TYPE_QTY://Qty
                            temp.add(new Double(sumCategoryReceiveInternalQty(srcReportReceive.getLocationId(), rs.getDate(1),
                            oidCategory, srcReportReceive.getReceiveFrom(), koneksi)));
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
    private static double sumCategoryReceiveInternalPrice(long oidLocation, Date filterDate, long oidCategory
    , long oidReceiveFrom, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SumOfTOTAL_PRICE" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strReceiveFrom = "";
            if (oidReceiveFrom != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + oidReceiveFrom;
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
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
    private static double sumCategoryReceiveInternalCost(long oidLocation, Date filterDate, long oidCategory
    , long oidReceiveFrom, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ") AS SumOfTOTAL_COST" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strReceiveFrom = "";
            if (oidReceiveFrom != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + oidReceiveFrom;
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
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
    private static double sumCategoryReceiveInternalQty(long oidLocation, Date filterDate, long oidCategory
    , long oidReceiveFrom, Connection koneksi) {
        double hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS SumOfTOTAL_QTY" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strLocationId = "";
            if (oidLocation != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if ((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate, "yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate, "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if (oidCategory != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strReceiveFrom = "";
            if (oidReceiveFrom != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + oidReceiveFrom;
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
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
     * Method ini digunakan untuk mendapatkan list main dokumen penerimaan internal.
     * create by: gwawan@dimata 12/10/2007
     * @param SrcReportReceive
     * @return Vector
     */
    public static Vector getReportReceiveInternalMain(SrcReportReceive srcReportReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT DISTINCT(REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + ")" +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK] +
            ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
            ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
            ", SUM(RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +") AS TOTAL_RCV" +
            " FROM (((" + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
            " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " ) ";
            
            String strLocationId = "";
            if (srcReportReceive.getLocationId() != 0) {
                strLocationId = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + srcReportReceive.getLocationId();
            }
            
            String strSupplierId = "";//Utk Internal ambil suppliernya dari Material
            if (srcReportReceive.getSupplierId() != 0) {
                strSupplierId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReceive.getSupplierId();
            }
            
            String strCategoryId = "";
            if (srcReportReceive.getCategoryId() != 0) {
                strCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReceive.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if (srcReportReceive.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReceive.getSubCategoryId();
            }
            
            String strReceiveFrom = "";
            if (srcReportReceive.getReceiveFrom() != 0) {
                strReceiveFrom = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] + " = " + srcReportReceive.getReceiveFrom();
            }
            
            String strDate = "";
            if ((srcReportReceive.getDateFrom() != null) && (srcReportReceive.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReceive.getDateFrom(), "yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcReportReceive.getDateTo(), "yyyy-MM-dd 23:23:59");
                strDate = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            if (strReceiveFrom.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strReceiveFrom;
                } else {
                    whereClause = whereClause + strReceiveFrom;
                }
            }
            
            String strReceiveSource = "( REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                                      " = " + PstMatReceive.SOURCE_FROM_DISPATCH +
                                      " OR REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                                      " = " + PstMatReceive.SOURCE_FROM_RETURN + " ) ";
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strReceiveSource;
            } else {
                whereClause = whereClause + strReceiveSource;
            }
            
            sql += " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" != "+I_DocStatus.DOCUMENT_STATUS_FINAL;
            
            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql += " GROUP BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] +
                   ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME];
            
            sql += " ORDER BY REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
                   ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                   ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println("-->>> getReportReceiveInternalMain :\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive objMatReceive = new MatReceive();
                Location objLocation = new Location();
                
                objMatReceive.setOID(rs.getLong("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
                objMatReceive.setLocationId(rs.getLong("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]));
                objMatReceive.setReceiveDate(rs.getDate("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
                objMatReceive.setSupplierId(rs.getLong("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
                objMatReceive.setRecCode(rs.getString("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                objMatReceive.setInvoiceSupplier(rs.getString("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]));
                objMatReceive.setRemark(rs.getString("REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]));
                temp.add(objMatReceive);
                
                objLocation.setName(rs.getString("LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME]));
                temp.add(objLocation);
                
                temp.add(String.valueOf(rs.getDouble("TOTAL_RCV")));
                
                result.add(temp);
                
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("exc in getReportReceiveInternalMain() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /**
     * Method ini digunakan untuk mendapatkan list item dari sebuah dokemen receive
     * create by: gwawan@dimata 12/10/2007
     * @param long
     * @return Vector
     */
    public static Vector getReportReceiveInternalItem(long oidMatReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            ", RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            " FROM (((" + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " ) " +
            " WHERE RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " +oidMatReceive;
            
            //System.out.println("-->>> getReportReceiveInternalItem():\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem objMatReceiveItem = new MatReceiveItem();
                Material objMaterial = new Material();
                Unit objUnit = new Unit();
                Category objCategory = new Category();
                
                objMatReceiveItem.setExpiredDate(rs.getDate("RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE]));
                objMatReceiveItem.setCost(rs.getDouble("RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
                objMatReceiveItem.setQty(rs.getDouble("RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
                objMatReceiveItem.setTotal(rs.getDouble("RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]));
                temp.add(objMatReceiveItem);
                
                objMaterial.setSku(rs.getString("MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                objMaterial.setName(rs.getString("MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                objMaterial.setDefaultPrice(rs.getDouble("MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(objMaterial);
                
                objUnit.setCode(rs.getString("UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(objUnit);
                
                objCategory.setName(rs.getString("CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(objCategory);
                
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
    
    public static Vector getReportReceiveSummaryJewelry(String where) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = ""
                    + " SELECT "
                    + " rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS QTY"
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_HEL]
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] + ") AS FORWARDER_COST"
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID]
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_NILAI_TUKAR]
                    + ", SUM("
                    + "(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]
                    + " + rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST]
                    + ") * rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS TOTAL"
                    + ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]
                    + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " AS rm"
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS rmi"
                    + " ON rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
                    + " = rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
                    + "";
                    
            sql += where;
            sql += " GROUP BY rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            sql += " ORDER BY rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE];
            sql += ", rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE];

            System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MatReceive rec = new MatReceive();
                MatReceiveItem recItem = new MatReceiveItem();

                rec.setOID(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
                rec.setReceiveDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
                rec.setInvoiceSupplier(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]));
                rec.setRecCode(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                rec.setSupplierId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
                rec.setBerat(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT]));
                rec.setHel(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_HEL]));
                rec.setTransRate(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));
                rec.setCurrencyId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID]));
                rec.setNilaiTukar(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_NILAI_TUKAR]));                
                rec.setRemark(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]));
                temp.add(rec);

                recItem.setQty(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ""));
                recItem.setForwarderCost(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] + ""));
                recItem.setTotal(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ""));
                temp.add(recItem);

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
	
	public static Vector getReportReceiveSummaryJewelryLebur(String where) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = ""
                    + " SELECT "
                    + " rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
					+ ", dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]
                    + ", dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                    + ", dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS QTY"
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT]+") AS BERAT"
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]+") AS COST"
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] + ") AS FORWARDER_COST"
                    + ", SUM(rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+") AS TOTAL"
                    + ", dp." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]
                    + " FROM " + PstMatDispatch.TBL_DISPATCH + " AS dp"
					+ " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " AS rm"
					+ " ON dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]
                    + " = rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS rmi"
                    + " ON rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
                    + " = rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
                    + "";
                    
            sql += where;
            sql += " GROUP BY dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID];
            sql += " ORDER BY dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE];
            sql += ", dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];

            System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                MatDispatch dis = new MatDispatch();
                MatReceiveItem recItem = new MatReceiveItem();

                dis.setOID(rs.getLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]));
                dis.setDispatchDate(rs.getDate(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]));
                dis.setDispatchCode(rs.getString(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]));
                dis.setRemark(rs.getString(PstMatDispatch.fieldNames[PstMatDispatch.FLD_REMARK]));
                temp.add(dis);

                recItem.setQty(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ""));
				recItem.setBerat(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT] + ""));
				recItem.setCost(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] + ""));
                recItem.setForwarderCost(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] + ""));
                recItem.setTotal(rs.getDouble("" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + ""));
                temp.add(recItem);

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
    
}