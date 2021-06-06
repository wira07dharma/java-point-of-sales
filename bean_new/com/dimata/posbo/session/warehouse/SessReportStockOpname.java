package com.dimata.posbo.session.warehouse;

import java.util.*;
import java.sql.*;
/* qdep package */
import com.dimata.posbo.db.*;
import com.dimata.util.*;

/* project package */
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;

public class SessReportStockOpname {
    
    public static final String SESS_SRC_REPORT_STOCK_OPNAME = "SESSION_SRC_REPORT_STOCK_OPNAME";
    
    public static Vector getReportStockOpname(SrcReportStockOpname srcReportStockOpname) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT "+
            " SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+ //  STOCK_OPNAME_ID
            ", SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]+ // STOCK_OPNAME_NUMBER
            ", SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]+ // STOCK_OPNAME_DATE
            ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ // SKU
            ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ // NAME
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]+ // COST
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]+ //QTY_OPNAME
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]+ //QTY_SOLD
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]+ //QTY_SYSTEM
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]+ //PRICE
            ", UNT."+PstUnit.fieldNames[PstUnit.FLD_CODE]+ // CODE
            " FROM (("+PstMatStockOpname.TBL_MAT_STOCK_OPNAME+" SO" + // stock_opname
            " INNER JOIN "+PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM+" SOI" + // stock_opname_item
            " ON SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+ // iSTOCK_OPNAME_ID" +
            " = SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+ // STOCK_OPNAME_ID" +
            " ) INNER JOIN "+PstMaterial.TBL_MATERIAL+" MAT" + // material
            " ON SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]+ // MATERIAL_ID" +
            " = MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ // MATERIAL_ID" +
            " ) INNER JOIN "+PstUnit.TBL_P2_UNIT+" UNT" +  // unit
            " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+ // DEFAULT_SELL_UNIT_ID
            " = UNT."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID";            
            
            String strLocationId = "";
            if(srcReportStockOpname.getLocationId() != 0) {
                strLocationId = " SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + " = " + srcReportStockOpname.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportStockOpname.getSupplierId() != 0) {
                strSupplierId = " SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] + " = " + srcReportStockOpname.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportStockOpname.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStockOpname.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportStockOpname.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStockOpname.getSubCategoryId();
            }
            
            String strDate = "";
            if((srcReportStockOpname.getDateFrom() != null) && (srcReportStockOpname.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportStockOpname.getDateFrom(),"yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportStockOpname.getDateTo(),"yyyy-MM-dd");
                strDate = " SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
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
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            srcReportStockOpname.setSortBy(4);
            
            switch(srcReportStockOpname.getSortBy()) {
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
                    sql = sql + " ORDER BY SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatStockOpname so = new MatStockOpname();
                MatStockOpnameItem soi = new MatStockOpnameItem();
                Material mat = new Material();
                Unit unt = new Unit();
                
                so.setOID(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]));
                so.setStockOpnameNumber(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]));
                so.setStockOpnameDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                temp.add(so);
                
                soi.setCost(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                soi.setPrice(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]));
                temp.add(soi);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                temp.add(mat);
                
                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
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
    
    public static Vector getReportStockOpnameTotal(SrcReportStockOpname srcReportStockOpname) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = " SELECT "+
            " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ // SKU" +
            ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ // NAME" +
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]+ // iCOST
            ", SUM(SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]+") AS QTY_OPNAME " + //  // QTY_OPNAME
            ", SUM(SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]+") AS QTY_SOLD " + // QTY_SOLD
            ", SUM(SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]+") AS QTY_SYSTEM " + // QTY_SYSTEM
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]+ // PRICE" +
            ", CAT."+PstCategory.fieldNames[PstCategory.FLD_NAME]+ // NAME" +
            " AS CAT_"+PstCategory.fieldNames[PstCategory.FLD_NAME]+ // NAME" +
            ", UNT."+PstUnit.fieldNames[PstUnit.FLD_CODE]+ // CODE" +
            " FROM ((("+PstMatStockOpname.TBL_MAT_STOCK_OPNAME+" SO" + // stock_opname
            " INNER JOIN "+PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM+" SOI" + // stock_opname_item
            " ON SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+ // STOCK_OPNAME_ID" +
            " = SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+ //  STOCK_OPNAME_ID" +
            " ) INNER JOIN "+PstMaterial.TBL_MATERIAL+" MAT" + // material
            " ON SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]+ // MATERIAL_ID" +
            " = MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ // MATERIAL_ID" +
            " ) INNER JOIN "+PstCategory.TBL_CATEGORY+" CAT" + // category
            " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+ // CATEGORY_ID" +
            " = CAT."+PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+ // CATEGORY_ID" +
            " )  INNER JOIN "+PstUnit.TBL_P2_UNIT+" UNT" +  // unit
            " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+ // DEFAULT_SELL_UNIT_ID" +
            " = UNT."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]; // UNIT_ID";
            
            String strLocationId = "";
            if(srcReportStockOpname.getLocationId() != 0) {
                strLocationId = " SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + " = " + srcReportStockOpname.getLocationId();
            }
            
            String strSupplierId = "";
            if(srcReportStockOpname.getSupplierId() != 0) {
                strSupplierId = " SO."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] + " = " + srcReportStockOpname.getSupplierId();
            }
            
            String strCategoryId = "";
            if(srcReportStockOpname.getCategoryId() != 0) {
                strCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportStockOpname.getCategoryId();
            }
            
            String strSubCategoryId = "";
            if(srcReportStockOpname.getSubCategoryId() != 0) {
                strSubCategoryId = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + srcReportStockOpname.getSubCategoryId();
            }
            
            String strDate = "";
            if((srcReportStockOpname.getDateFrom() != null) && (srcReportStockOpname.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcReportStockOpname.getDateFrom(),"yyyy-MM-dd");
                String endDate = Formater.formatDate(srcReportStockOpname.getDateTo(),"yyyy-MM-dd");
                strDate = " SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + startDate + " 00:00:01' AND '" + endDate + " 23:59:59'";
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
            
            if(whereClause.length()>0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql += " GROUP BY MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ // SKU" +
            ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ // NAME" +
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]+ // COST" +
            ", SOI."+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]+ // PRICE" +
            ", CAT."+PstCategory.fieldNames[PstCategory.FLD_NAME]+ // NAME" +
            ", UNT."+PstUnit.fieldNames[PstUnit.FLD_CODE]; // CODE";
            
            srcReportStockOpname.setSortBy(4);  
            System.out.println("sql > "+sql);
            
            switch(srcReportStockOpname.getSortBy()) {
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
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector();
                MatStockOpnameItem soi = new MatStockOpnameItem();
                Material mat = new Material();
                Category cat = new Category();
                SubCategory scat = new SubCategory();
                Unit unt = new Unit();
                
                soi.setCost(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                soi.setPrice(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]));
                temp.add(soi);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                temp.add(mat);
                
                cat.setName(rs.getString("CAT_"+PstCategory.fieldNames[PstCategory.FLD_NAME]));
                temp.add(cat);
                
                scat.setName(""); 
                temp.add(scat); 
                
                unt.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unt);
                
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
    
}

