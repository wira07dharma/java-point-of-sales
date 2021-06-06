/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice.inquery;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.Material;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class InqueryMaterialItem {
     public static Vector listMaterialApi(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        int multiLanguageName = 0;
        try {
            multiLanguageName = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueByName("NAME_MATERIAL_MULTI_LANGUAGE"));
        } catch (Exception e) {
            multiLanguageName = 0;
        }
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT l.LOCATION_ID,pm.MATERIAL_ID, pm.NAME, pu.UNIT_ID, pp.PRICE, pp.PRICE_TYPE_ID, pp.STANDART_RATE_ID  FROM pos_material pm " +
                        " INNER JOIN product_location pl ON pm.MATERIAL_ID=pl.PRODUCT_ID " +
                        " INNER JOIN location l ON pl.LOCATION_ID=l.LOCATION_ID " +
                        " INNER JOIN pos_unit pu ON pm.BUY_UNIT_ID=pu.UNIT_ID " +
                        " INNER JOIN pos_price_type_mapping pp ON pm.MATERIAL_ID=pp.MATERIAL_ID ";
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
                MaterialForApi materialForApi = new MaterialForApi();
                materialForApi.setLocationId(rs.getLong("LOCATION_ID"));
                materialForApi.setMaterialId(rs.getLong("MATERIAL_ID"));
                materialForApi.setName(rs.getString("NAME"));
                if(multiLanguageName==1){
                    String[] smartPhonesSplits;
                    smartPhonesSplits = materialForApi.getName().split("\\;");
                    try{
                         materialForApi.setName(smartPhonesSplits[0]);
                    }catch(Exception ex){}
                }
                materialForApi.setUnitId(rs.getLong("UNIT_ID"));
                materialForApi.setPrice(rs.getDouble("PRICE"));
                materialForApi.setPriceTypeId(rs.getLong("PRICE_TYPE_ID"));
                materialForApi.setStandartRateId(rs.getLong("STANDART_RATE_ID"));
                lists.add(materialForApi);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            //System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
}
