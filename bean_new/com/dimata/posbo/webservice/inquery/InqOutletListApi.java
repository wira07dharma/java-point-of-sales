/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice.inquery;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class InqOutletListApi {
     public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null; 
        try {
            String sql = "SELECT * FROM " + PstLocation.TBL_P2_LOCATION;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Location location = new Location();
                resultToObject(rs, location);
                lists.add(location);
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
     
    private static void resultToObject(ResultSet rs, Location location) {
        try {
            location.setOID(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]));
            location.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
            location.setContactId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_CONTACT_ID]));
            location.setDescription(rs.getString(PstLocation.fieldNames[PstLocation.FLD_DESCRIPTION]));
            location.setCode(rs.getString(PstLocation.fieldNames[PstLocation.FLD_CODE]));
            location.setAddress(rs.getString(PstLocation.fieldNames[PstLocation.FLD_ADDRESS]));
            location.setTelephone(rs.getString(PstLocation.fieldNames[PstLocation.FLD_TELEPHONE]));
            location.setFax(rs.getString(PstLocation.fieldNames[PstLocation.FLD_FAX]));
            location.setPerson(rs.getString(PstLocation.fieldNames[PstLocation.FLD_PERSON]));
            location.setEmail(rs.getString(PstLocation.fieldNames[PstLocation.FLD_EMAIL]));
            location.setType(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_TYPE]));
            location.setParentLocationId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_PARENT_LOCATION_ID]));
            location.setWebsite(rs.getString(PstLocation.fieldNames[PstLocation.FLD_WEBSITE]));
            location.setLocIndex(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_LOC_INDEX]));

            // ini digunakan prochain add opie 13-06-2012
            location.setServicePersen(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_PERCENT]));
            location.setTaxPersen(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_PERCENT]));
            
            location.setIcon(rs.getString("ICON"));

             // ini digunakan oleh hanoman
            location.setDepartmentId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID]));
            location.setTypeBase(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_USED_VAL]));
            location.setServiceValue(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL]));
            location.setTaxValue(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE]));
            location.setServiceValueUsd(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL_USD]));
            location.setTaxValueUsd(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE_USD]));
            location.setReportGroup(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_REPORT_GROUP]));

            //ini untuk prohchain add opie 13-06-2012
            location.setTaxSvcDefault(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_TAX_SVC_DEFAULT]));
            location.setPersentaseDistributionPurchaseOrder(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER]));
              //add fitra 29-01-2014
            location.setCompanyId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]));
            
            // added by dewok++ 2017-03-21
            location.setOpeningTime(rs.getTime(PstLocation.fieldNames[PstLocation.FLD_OPENING_TIME]));
            location.setClosingTime(rs.getTime(PstLocation.fieldNames[PstLocation.FLD_CLOSING_TIME]));
            
            location.setPriceTypeId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_PRICE_TYPE_ID]));
            location.setStandarRateId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_STANDART_RATE_ID]));
        } catch (Exception e) {
        }
    } 
     
}
