/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.warehouse;


import java.sql.*;
import java.util.*;
/* package qdep */
import com.dimata.posbo.db.*;

import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.periode.Periode;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.search.SrcMaterial;
/**
 *
 * @author dimata005
 */
public class SessDoSearch {
    
    public static int countMaterialStockCurrPeriodAll(SrcMaterial objSrcMaterial) {
        return countMaterialStockCurrPeriodAll(objSrcMaterial, "");
    }
    
    public static int countMaterialStockCurrPeriodAll(SrcMaterial objSrcMaterial, String whereAdd) {
        int result = 0;
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT COUNT(DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + ")" +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!=4"+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "="+PstMaterial.MAT_TYPE_REGULAR;
            
            if(objSrcMaterial.getCategoryId() > 0) {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
                //buatkan seperti
                String strGroup = " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,objSrcMaterial.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                
                sql += strGroup;
            }
            //if(objSrcMaterial.getMatcode() != "") {
               // sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
            //}
            
            
            if(objSrcMaterial.getMatcode() != "") {
               sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'" +
                      " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
           }
            if(objSrcMaterial.getMatname() != "") {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
            
            //updated by dewok for litama 2018-02-19
            if (!whereAdd.equals("")) {
                sql += whereAdd;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector listMaterialStockCurrPeriodAll(SrcMaterial objSrcMaterial, int start, int recordToGet, String orderBy) {
        return listMaterialStockCurrPeriodAll(objSrcMaterial, start, recordToGet, orderBy, "");
    }
    
    public static Vector listMaterialStockCurrPeriodAll(SrcMaterial objSrcMaterial, int start, int recordToGet, String orderBy, String whereAdd) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    ", MT.*"+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE+
                    " AND MT."+PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL]+"!='"+PstMaterial.EDIT_NON_AKTIVE+"'"+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "="+PstMaterial.MAT_TYPE_REGULAR;
            if(objSrcMaterial.getMatcode() != "") {
               sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'" +
                      " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
           }
            if(objSrcMaterial.getMatname() != "") {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
            
             if(objSrcMaterial.getCategoryId() > 0) {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
                //buatkan seperti
                String strGroup = " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,objSrcMaterial.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                
                sql += strGroup;
            }
             //if(objSrcMaterial.getBarCode() != "") {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getBarCode() + "%'";
            //}
             
            //updated by dewok for litama 2018-02-19
            if (!whereAdd.equals("")) {
                sql += whereAdd;
            }
             
            sql = sql + " ORDER BY MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

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

            System.out.println("listMaterialStockCurrPeriod():"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector addVec = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCostCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                addVec.add(material);
                
                MaterialStock materialStock = new MaterialStock();
                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));
                addVec.add(materialStock);
                
                result.add(addVec);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector listMaterialStockCurrPeriodAll2(SrcMaterial objSrcMaterial, int start, int recordToGet, String orderBy) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    ", MT.*"+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;
            
            if(objSrcMaterial.getMatcode() != "") {
               sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'" +
                      " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
           }
            if(objSrcMaterial.getMatname() != "") {
                sql += " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
            
            if (objSrcMaterial.getMatcode() != ""){
                sql += ")";
            }
            
             if(objSrcMaterial.getCategoryId() > 0) {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
            }
             //if(objSrcMaterial.getBarCode() != "") {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getBarCode() + "%'";
            //}
            sql = sql + " ORDER BY MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

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

            System.out.println("listMaterialStockCurrPeriod():"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector addVec = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCostCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                addVec.add(material);
                
                MaterialStock materialStock = new MaterialStock();
                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));
                addVec.add(materialStock);
                
                result.add(addVec);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
}
