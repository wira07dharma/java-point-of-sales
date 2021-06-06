package com.dimata.posbo.session.warehouse;

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

public class SessReportReturn {
    
    // constant for return in warehouse
    public static final String SESS_SRC_REPORT_RETURN = "SESSION_SRC_REPORT_RETURN";
    public static final String SESS_SRC_REPORT_RETURN_INTERNAL = "SESSION_SRC_REPORT_RETURN_INTERNAL";
    public static final String SESS_SRC_REPORT_RETURN_INVOICE = "SESSION_SRC_REPORT_RETURN_INVOICE";
    public static final String SESS_SRC_REPORT_RETURN_INTERNAL_INVOICE = "SESSION_SRC_REPORT_RETURN_INTERNAL_INVOICE";
    public static final String SESS_SRC_REPORT_RETURN_REKAP = "SESSION_SRC_REPORT_RETURN_REKAP";
    public static final String SESS_SRC_REPORT_RETURN_REKAP_TANGGAL_KATEGORI = "SESS_SRC_REPORT_RETURN_REKAP_TANGGAL_KATEGORI";
    public static final String SESS_SRC_REPORT_RETURN_REKAP_TANGGAL_KATEGORI_INTERNAL = "SESS_SRC_REPORT_RETURN_REKAP_TANGGAL_KATEGORI_INTERNAL";
    
    // constant for return in store
    public static final String SESS_SRC_REPRETSTORESUPPPERSUPP = "SESS_SRC_REPRETSTORESUPPPERSUPP";
    public static final String SESS_SRC_REPRETSTORESUPPPERINV = "SESS_SRC_REPRETSTORESUPPPERINV";
    public static final String SESS_SRC_REPRETSTORESUPPREKAPINV = "SESS_SRC_REPRETSTORESUPPREKAPINV";
    public static final String SESS_SRC_REPRETSTOREWHPERINV = "SESS_SRC_REPRETSTOREWHPERINV";
    public static final String SESS_SRC_REPRETSTOREWHREKAPINV = "SESS_SRC_REPRETSTOREWHREKAPINV";
    
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturn(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_TRANS_RATE] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RET." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportReturn.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReturn.getSubCategoryId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strFilterSupplier = "";
            strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " <> 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSubCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                }
                else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            sql += " WHERE (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL + ")";
            
            if(whereClause.length()>0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            System.out.println("sql getReportReturn(#): "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturn ret = new MatReturn();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                ContactList cnt = new ContactList();
                
                ret.setOID(rs.getLong(1));
                ret.setLocationId(rs.getLong(2));
                ret.setReturnDate(rs.getDate(3));
                ret.setSupplierId(rs.getLong(4));
                ret.setReturnReason(rs.getInt(12));
                ret.setRetCode(rs.getString(13));
                ret.setCurrencyId(rs.getLong(16));
                ret.setTransRate(rs.getDouble(17));
                temp.add(ret);
                
                retItem.setCost(rs.getDouble(8));
                retItem.setQty(rs.getDouble(10));
                retItem.setTotal(rs.getDouble(11));
                temp.add(retItem);
                
                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultPrice(rs.getDouble(14));
                temp.add(mat);
                
                unt.setCode(rs.getString(7));
                temp.add(unt);
                
                curr.setCode(rs.getString(9));
                temp.add(curr);
                
                cnt.setCompName(rs.getString(15));
                temp.add(cnt);
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /**
     * used to get data for generate report return to supplier per invoice
     * @param srcReportReturn
     * @return
     * @created by Edhy
     */
    public static Vector getReportReturnStoreToSupplierInvoice(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturn ret = new MatReturn();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                ContactList cnt = new ContactList();
                
                ret.setOID(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]));
                ret.setLocationId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID]));
                ret.setReturnDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                ret.setSupplierId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID]));
                ret.setReturnReason(rs.getInt(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON]));
                ret.setRetCode(rs.getString(PstMatReturn.fieldNames[PstMatReceive.FLD_REC_CODE]));
                temp.add(ret);
                
                retItem.setCost(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST]));
                retItem.setQty(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]));
                retItem.setTotal(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL]));
                temp.add(retItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(mat);
                
                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
                curr.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
                
                cnt.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                temp.add(cnt);
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnInternal(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] +
            ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportReturn.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReturn.getSubCategoryId();
            }
            
            String strReturnTo = "";
            if(srcReportReturn.getReturnTo() != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + srcReportReturn.getReturnTo();
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strFilterSupplier = "";
            strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSubCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                }
                else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturn ret = new MatReturn();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                Location loc = new Location();
                
                ret.setOID(rs.getLong(1));
                ret.setLocationId(rs.getLong(2));
                ret.setReturnDate(rs.getDate(3));
                ret.setSupplierId(rs.getLong(4));
                ret.setReturnReason(rs.getInt(13));
                ret.setRetCode(rs.getString(14));
                temp.add(ret);
                
                retItem.setCost(rs.getDouble(8));
                retItem.setQty(rs.getDouble(10));
                retItem.setTotal(rs.getDouble(11));
                temp.add(retItem);
                
                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultPrice(rs.getDouble(15));
                temp.add(mat);
                
                unt.setCode(rs.getString(7));
                temp.add(unt);
                
                loc.setName(rs.getString(12));
                temp.add(loc);
                
                curr.setCode(rs.getString(9));
                temp.add(curr);
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcReportReturn
     * @return
     * @created by Edhy
     */
    public static Vector getReportReturnStoreToWhPerInv(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " AS UNT_" + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] +
            ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
            " AS LOC_"+PstLocation.fieldNames[PstLocation.FLD_NAME] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strReturnTo = "";
            if(srcReportReturn.getReturnTo() != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + srcReportReturn.getReturnTo();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            System.out.println("getReportReturnStoreToWhPerInv(#)\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturn ret = new MatReturn();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                CurrencyType curr = new CurrencyType();
                Location loc = new Location();
                
                ret.setOID(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]));
                ret.setLocationId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID]));
                ret.setReturnDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                ret.setSupplierId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID]));
                ret.setReturnReason(rs.getInt(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON]));
                ret.setRetCode(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]));
                ret.setRemark(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK]));
                temp.add(ret);
                
                retItem.setCost(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST]));
                retItem.setQty(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]));
                retItem.setTotal(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL]));
                temp.add(retItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(mat);
                
                unt.setCode(rs.getString("UNT_"+PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
                loc.setName(rs.getString("LOC_"+PstLocation.fieldNames[PstLocation.FLD_NAME]));
                temp.add(loc);
                
                curr.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    //Rekap Return
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnRekap(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]+ ") AS SumOfQTY" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SumOfTOTAL" +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            " FROM (" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." +  PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportReturn.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReturn.getSubCategoryId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strFilterSupplier = "";
            strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " <> 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSubCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                }
                else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
            
            srcReportReturn.setSortBy(4);
            
            switch(srcReportReturn.getSortBy()) {
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
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON];
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                temp.add(new Integer(rs.getInt(1)));
                temp.add(new Double(rs.getDouble(2)));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(new Integer(rs.getInt(4)));
                temp.add(Formater.formatDate(rs.getDate(5),"dd-MM-yyyy"));
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnRekapInvoice(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfQTY" +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportReturn.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReturn.getSubCategoryId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strFilterSupplier = "";
            strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " <> 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSubCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                }
                else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
            
            sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(2)));
                temp.add(rs.getString(3));
                temp.add(new Double(rs.getDouble(4)));
                temp.add(new Double(rs.getDouble(5)));
                temp.add(PstMatReturn.strReturnReasonList[0][rs.getInt(6)]);
                temp.add(new Double(rs.getDouble(7)));
                temp.add(rs.getString(8));
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcReportReturn
     * @return
     * @created by Edhy
     */
    public static Vector getReportReturnStoreToSupplierRekapInvoice(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfQTY" +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
            
            sql += " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID])));
                temp.add(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]));
                temp.add(new Double(rs.getDouble("SumOfTOTAL")));
                temp.add(new Double(rs.getDouble("PRC")));
                temp.add(PstMatReturn.strReturnReasonList[0][rs.getInt(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON])]);
                temp.add(new Double(rs.getDouble("SumOfQTY")));
                temp.add(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnInternalRekapInvoice(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfQTY" +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE+ " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION +" LOC" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportReturn.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReturn.getSubCategoryId();
            }
            
            String strReturnTo = "";
            if(srcReportReturn.getReturnTo() != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + srcReportReturn.getReturnTo();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strFilterSupplier = "";
            strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSubCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                }
                else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON];
            
            sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(2)));
                temp.add(rs.getString(3));
                temp.add(new Double(rs.getDouble(4)));
                temp.add(new Double(rs.getDouble(5)));
                temp.add(PstMatReturn.strReturnReasonList[0][rs.getInt(6)]);
                temp.add(new Double(rs.getDouble(7)));
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcReportReturn
     * @return
     * @created by Edhy
     */
    public static Vector getReportRekapReturnStoreToWhPerInv(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS PRC" +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfQTY" +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strReturnTo = "";
            if(srcReportReturn.getReturnTo() != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + srcReportReturn.getReturnTo();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON];
            
            sql += " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                temp.add(Formater.formatDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]), "dd-MM-yyyy"));
                temp.add(new Long(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID])));
                temp.add(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER]));
                temp.add(new Double(rs.getDouble("SumOfTOTAL")));
                temp.add(new Double(rs.getDouble("PRC")));
                temp.add(PstMatReturn.strReturnReasonList[0][rs.getInt(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON])]);
                temp.add(new Double(rs.getDouble("SumOfQTY")));
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnTotal(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ")" +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                /*
                ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                " AS SUB_"+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +*/
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_TRANS_RATE] +
            " FROM ((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " )"+/*INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +)*/
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strFilterSupplier = "";
            //strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " <> 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            sql += " WHERE (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT;
            sql += " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL + ")";
            
            if(whereClause.length()>0) {
                sql = sql + " AND " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_TRANS_RATE] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE];
            
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            System.out.println("getReportReturnTotal sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                MatReturn matReturn = new MatReturn();
                CurrencyType objCurrencyType = new CurrencyType();
                
                retItem.setCost(rs.getDouble(4));
                retItem.setQty(rs.getDouble(5));
                retItem.setTotal(rs.getDouble(6));
                temp.add(retItem); //0
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultPrice(rs.getDouble(7));
                temp.add(mat); //1
                
                unt.setCode(rs.getString(3));
                temp.add(unt); //2
                
                cat.setName(rs.getString(8));
                temp.add(cat); //3
                
                //scat.setName(rs.getString(9));
                temp.add(scat); //4
                
                matReturn.setSupplierId(rs.getLong(9));
                matReturn.setCurrencyId(rs.getLong(10));
                matReturn.setTransRate(rs.getDouble(12));
                temp.add(matReturn); //5
                
                objCurrencyType.setCode(rs.getString(11));
                temp.add(objCurrencyType); //6
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * Used to get data return from location (store) into Supplier
     * @param srcReportReturn
     * @return
     * @created by Edhy
     * @updated by gwawan@dimata 5 Dec 2007
     */
    public static Vector getReportReturnStoreToSupplierTotal(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") QTY" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") TOTAL" +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            " AS CAT_" + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                /*", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                " AS SCAT_" + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +*/
            ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
            " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                /*" INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " CURR" +
                " ON RMI." +  PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
                " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +
                " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +*/
            " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
            
            if(srcReportReturn.getSupplierId() != 0){
                sql = sql + " INNER JOIN "+ PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" AS VP "+
                " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
                " = VP."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " VP."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql +=  " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
            
            sql +=  " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println("SQL : "+sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                MatReturn matReturn = new MatReturn();
                
                retItem.setCost(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST]));
                retItem.setQty(rs.getDouble("QTY"));
                retItem.setTotal(rs.getDouble("TOTAL"));
                temp.add(retItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                temp.add(mat);
                
                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
                cat.setName(rs.getString("CAT_"+PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);
                
                // scat.setName(rs.getString("SCAT_"+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]));
                temp.add(scat);
                
                matReturn.setSupplierId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID]));
                temp.add(matReturn);
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    
    /**
     * @param srcReportReturn
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnInternalTotal(SrcReportReturn srcReportReturn) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ")" +
            ", SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ")" +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            " FROM ((((((" + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
            " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " ) INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
            " ) INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
            " ON MAT." + PstMaterial.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportReturn.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportReturn.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportReturn.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportReturn.getSubCategoryId();
            }
            
            String strReturnTo = "";
            if(srcReportReturn.getReturnTo() != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + srcReportReturn.getReturnTo();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strReturnReason = "";
            if(srcReportReturn.getReturnReason() != -1) {
                strReturnReason = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON] + " = " + srcReportReturn.getReturnReason();
            }
            
            String strFilterSupplier = "";
            strFilterSupplier = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = 0";
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSubCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSubCategoryId;
                }
                else {
                    whereClause = whereClause + strSubCategoryId;
                }
            }
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strReturnReason.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnReason;
                }
                else {
                    whereClause = whereClause + strReturnReason;
                }
            }
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(strFilterSupplier.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strFilterSupplier;
                }
                else {
                    whereClause = whereClause + strFilterSupplier;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
            
            sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
            ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatReturnItem retItem = new MatReturnItem();
                Material mat = new Material();
                Unit unt = new Unit();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                
                retItem.setCost(rs.getDouble(4));
                retItem.setQty(rs.getDouble(5));
                retItem.setTotal(rs.getDouble(6));
                temp.add(retItem);
                
                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultPrice(rs.getDouble(7));
                temp.add(mat);
                
                unt.setCode(rs.getString(3));
                temp.add(unt);
                
                cat.setName(rs.getString(8));
                temp.add(cat);
                
                scat.setName(rs.getString(9));
                temp.add(scat);
                
                result.add(temp);
                
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    //Laporan nilai apa yg diinginkan
    public static final int INFO_TYPE_PRICE   = 0;
    public static final int INFO_TYPE_COST    = 1;
    public static final int INFO_TYPE_QTY     = 2;
    
    //Untuk menampilkan total pengiriman harian di lokasi tertentu per tanggal
    /**
     * @param srcReportReturn
     * @param reportType
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnRekapTanggalKategori(SrcReportReturn srcReportReturn, int reportType) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            //List semua kategori order by kode
            Vector listKategori = PstCategory.list(0,0, "", PstCategory.fieldNames[PstCategory.FLD_CODE]);
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
            sql += " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
            
            //System.out.println(sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                for (int i=0;i<listKategori.size();i++) {
                    //Count total Dispatch for each category
                    Category cat = (Category)listKategori.get(i);
                    long oidCategory = cat.getOID();
                    switch (reportType) {
                        case INFO_TYPE_PRICE://Price
                            temp.add(new Double(sumCategoryReturnPrice(srcReportReturn.getLocationId(),rs.getDate(1),
                            oidCategory,srcReportReturn.getSupplierId(),koneksi)));
                            break;
                        case INFO_TYPE_COST://Cost
                            temp.add(new Double(sumCategoryReturnCost(srcReportReturn.getLocationId(),rs.getDate(1),
                            oidCategory,srcReportReturn.getSupplierId(),koneksi)));
                            break;
                        case INFO_TYPE_QTY://Qty
                            temp.add(new Double(sumCategoryReturnQty(srcReportReturn.getLocationId(),rs.getDate(1),
                            oidCategory,srcReportReturn.getSupplierId(),koneksi)));
                            break;
                    }
                }
                //System.out.println(temp);
                result.add(temp);
            }
            rs.close();
            stmt.close();
            PstMaterialStock.closeLocalConnection(koneksi);
            
        }
        catch(Exception e) {
            System.out.println("Err 1 : "+e.toString());
        }
        finally {
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
    private static double sumCategoryReturnPrice(long oidLocation, Date filterDate, long oidCategory
    , long oidSupplier, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI."+ PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]+
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SumOfTOTAL_PRICE" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(oidLocation != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate,"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate,"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if(oidCategory != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strSupplierId = "";
            if(oidSupplier != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + oidSupplier;
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            
        }
        catch(Exception e) {
            System.out.println("Err 2 : "+e.toString());
        }
        finally {
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
    private static double sumCategoryReturnCost(long oidLocation, Date filterDate, long oidCategory
    , long oidSupplier, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL_COST" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(oidLocation != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate,"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate,"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if(oidCategory != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strSupplierId = "";
            if(oidSupplier != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + oidSupplier;
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            
        }
        catch(Exception e) {
            System.out.println("Err 2 : "+e.toString());
        }
        finally {
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
     * @update by Edhy
     */
    private static double sumCategoryReturnQty(long oidLocation, Date filterDate, long oidCategory
    , long oidSupplier, Connection koneksi) {
        double hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfTOTAL_QTY" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(oidLocation != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate,"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate,"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if(oidCategory != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strSupplierId = "";
            if(oidSupplier != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + oidSupplier;
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            
        }
        catch(Exception e) {
            System.out.println("Err 2 : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }
    
    
    //Untuk menampilkan total pengiriman harian di lokasi tertentu per tanggal
    /**
     * @param srcReportReturn
     * @param reportType
     * @return
     * @update by Edhy
     */
    public static Vector getReportReturnRekapInternalTanggalKategori(SrcReportReturn srcReportReturn, int reportType) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            //List semua kategori order by kode
            Vector listKategori = PstCategory.list(0,0, "", PstCategory.fieldNames[PstCategory.FLD_CODE]);
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
            " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON RET." +  PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(srcReportReturn.getLocationId() != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcReportReturn.getLocationId();
            }
            
            String strDate = "";
            if((srcReportReturn.getDateFrom() != null) && (srcReportReturn.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportReturn.getDateFrom(),"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(srcReportReturn.getDateTo(),"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strSupplierId = "";
            if(srcReportReturn.getSupplierId() != 0) {
                strSupplierId = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + " = " + srcReportReturn.getSupplierId();
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strSupplierId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strSupplierId;
                }
                else {
                    whereClause = whereClause + strSupplierId;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
            sql += " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
            
            //System.out.println(sql);
            Connection koneksi = PstMaterialStock.getLocalConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                Vector temp = new Vector();
                
                temp.add(Formater.formatDate(rs.getDate(1), "dd-MM-yyyy"));
                for (int i=0;i<listKategori.size();i++) {
                    //Count total Dispatch for each category
                    Category cat = (Category)listKategori.get(i);
                    long oidCategory = cat.getOID();
                    switch (reportType) {
                        case INFO_TYPE_PRICE://Price
                            temp.add(new Double(sumCategoryReturnInternalPrice(srcReportReturn.getLocationId(),rs.getDate(1),
                            oidCategory,srcReportReturn.getReturnTo(),koneksi)));
                            break;
                        case INFO_TYPE_COST://Cost
                            temp.add(new Double(sumCategoryReturnInternalCost(srcReportReturn.getLocationId(),rs.getDate(1),
                            oidCategory,srcReportReturn.getReturnTo(),koneksi)));
                            break;
                        case INFO_TYPE_QTY://Qty
                            temp.add(new Double(sumCategoryReturnInternalQty(srcReportReturn.getLocationId(),rs.getDate(1),
                            oidCategory,srcReportReturn.getReturnTo(),koneksi)));
                            break;
                    }
                }
                //System.out.println(temp);
                result.add(temp);
            }
            rs.close();
            stmt.close();
            PstMaterialStock.closeLocalConnection(koneksi);
            
        }
        catch(Exception e) {
            System.out.println("Err 1 : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    
    //Menghitung total dispatch per kategori dlm suatu range tertentu by price
    private static double sumCategoryReturnInternalPrice(long oidLocation, Date filterDate, long oidCategory
    , long oidReturnTo, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + ") AS SumOfTOTAL_PRICE" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(oidLocation != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate,"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate,"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if(oidCategory != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strReturnTo = "";
            if(oidReturnTo != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + oidReturnTo;
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            
        }
        catch(Exception e) {
            System.out.println("Err 2 : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }
    
    
    
    //Menghitung total dispatch per kategori dlm suatu range tertentu by cost
    /**
     * @param oidLocation
     * @param filterDate
     * @param oidCategory
     * @param oidReturnTo
     * @param koneksi
     * @return
     * @update by Edhy
     */
    private static double sumCategoryReturnInternalCost(long oidLocation, Date filterDate, long oidCategory
    , long oidReturnTo, Connection koneksi) {
        double hasil = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL] + ") AS SumOfTOTAL_COST" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
            " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(oidLocation != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate,"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate,"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if(oidCategory != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strReturnTo = "";
            if(oidReturnTo != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + oidReturnTo;
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            
        }
        catch(Exception e) {
            System.out.println("Err 2 : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }
    
    
    //Menghitung total dispatch per kategori dlm suatu range tertentu by qty
    /**
     * @param oidLocation
     * @param filterDate
     * @param oidCategory
     * @param oidReturnTo
     * @param koneksi
     * @return
     * @update by Edhy
     */
    private static double sumCategoryReturnInternalQty(long oidLocation, Date filterDate, long oidCategory
    , long oidReturnTo, Connection koneksi) {
        double hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SumOfTOTAL_QTY" +
            " FROM (((" + PstCategory.TBL_CATEGORY + " CAT" +
            " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
            " ON CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " ) INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RMI" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " = RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " RET" +
            " ON RMI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
            " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID];
            
            String strLocationId = "";
            if(oidLocation != 0) {
                strLocationId = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + oidLocation;
            }
            
            String strDate = "";
            if((filterDate != null) && (filterDate != null)) {
                String startDate = Formater.formatDate(filterDate,"yyyy-MM-dd 00:00:01");
                String endDate = Formater.formatDate(filterDate,"yyyy-MM-dd 23:23:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strCategoryId = "";
            if(oidCategory != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
            }
            
            String strReturnTo = "";
            if(oidReturnTo != 0) {
                strReturnTo = " RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] + " = " + oidReturnTo;
            }
            
            String whereClause = "";
            if(strLocationId.length() > 0)
                whereClause = strLocationId;
            
            if(strDate.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strDate;
                }
                else {
                    whereClause = whereClause + strDate;
                }
            }
            
            if(strCategoryId.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strCategoryId;
                }
                else {
                    whereClause = whereClause + strCategoryId;
                }
            }
            
            if(strReturnTo.length()>0) {
                if(whereClause.length()>0) {
                    whereClause = whereClause + " AND " + strReturnTo;
                }
                else {
                    whereClause = whereClause + strReturnTo;
                }
            }
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            //System.out.println(sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                hasil = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            
        }
        catch(Exception e) {
            System.out.println("Err 2 : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }
    
}
