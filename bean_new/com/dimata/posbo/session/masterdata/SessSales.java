/**
 * Created by IntelliJ IDEA. User: gadnyana Date: Jun 6, 2005 Time: 10:31:05 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstPendingOrder;

import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;
import java.util.Vector;

import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.posbo.entity.masterdata.InsentifData;
import com.dimata.posbo.entity.masterdata.PstInsentifData;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstSales;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.util.Formater;
import java.util.ArrayList;

public class SessSales {

    /**
     * pengecekan data sales di bill main dan pending order
     *
     * @param salesId
     * @param salesCode
     * @return
     */
    public static boolean readyDataToDelete(long salesId, String salesCode) {
        boolean status = true;
        try {
            String where = PstPendingOrder.fieldNames[PstPendingOrder.FLD_SALES_ID] + "=" + salesId;
            Vector vlist = PstPendingOrder.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            } else {
                where = PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + "='" + salesCode + "'";
                vlist = PstBillMain.list(0, 0, where, "");
                if (vlist != null && vlist.size() > 0) {
                    status = false;
                }
            }
        } catch (Exception e) {
            System.out.println("SessSales - readyDataToDelete : " + e.toString());
        }
        return status;
    }

    //added by dewok 20180315
    public static Vector getReportInsentifOld(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();        
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + PstSales.TBL_SALES + " AS psp"
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS emp"
                    + " ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = psp." + PstSales.fieldNames[PstSales.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm"
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]
                    + " = psp." + PstSales.fieldNames[PstSales.FLD_CODE]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " AS pos"
                    + " ON pos." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + " = emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + "";
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector data = new Vector();
                
                Sales matSales = new Sales();
                matSales.setOID(rs.getLong(PstSales.fieldNames[PstSales.FLD_SALES_ID]));
                matSales.setCode(rs.getString(PstSales.fieldNames[PstSales.FLD_CODE]));
                matSales.setName(rs.getString(PstSales.fieldNames[PstSales.FLD_NAME]));
                matSales.setRemark(rs.getString(PstSales.fieldNames[PstSales.FLD_REMARK]));
                matSales.setCommision(rs.getDouble(PstSales.fieldNames[PstSales.FLD_COMMISION]));
                matSales.setLoginId(rs.getString(PstSales.fieldNames[PstSales.FLD_LOGIN_ID]));
                matSales.setPassword(rs.getString(PstSales.fieldNames[PstSales.FLD_PASSWORD]));
                matSales.setLocationId(rs.getLong(PstSales.fieldNames[PstSales.FLD_ASSIGN_LOCATION_WAREHOUSE]));
                matSales.setDefaultCurrencyId(rs.getLong(PstSales.fieldNames[PstSales.FLD_STANDARD_CURRENCY_ID]));
                matSales.setEmployeeId(rs.getLong(PstSales.fieldNames[PstSales.FLD_EMPLOYEE_ID]));
                matSales.setStatus(rs.getInt(PstSales.fieldNames[PstSales.FLD_STATUS]));
                data.add(matSales);
                
                BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
                data.add(billMain);
                
                Position position = new Position();
                PstPosition.resultToObject(rs, position);
                data.add(position);
                
                lists.add(data);
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
    
    //added by dewok 20180703
    public static Vector getReportInsentif(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " DATE(bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") AS date"
                    + ", e." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " AS position"
                    + ", e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AS employee"
                    + ", SUM(id." + PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_POINT] + ") AS point"
                    + ", SUM(id." + PstInsentifData.fieldNames[PstInsentifData.FLD_INSENTIF_VALUE] + ") AS nominal"
                    + " FROM " + PstInsentifData.TBL_INSENTIF_DATA + " AS id"
                    
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS bm"
                    + " ON bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = id." + PstInsentifData.fieldNames[PstInsentifData.FLD_CASH_BILL_MAIN_ID]
                    
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS e"
                    + " ON e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = id." + PstInsentifData.fieldNames[PstInsentifData.FLD_EMPLOYEE_ID]
                    + "";
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector data = new Vector();
                
                BillMain bm = new BillMain();
                bm.setBillDate(rs.getDate("date"));
                data.add(bm);
                
                InsentifData id = new InsentifData();
                id.setPositionId(rs.getLong("position"));
                id.setEmployeeId(rs.getLong("employee"));
                id.setInsentifPoint(rs.getDouble("point"));
                id.setInsentifValue(rs.getDouble("nominal"));
                data.add(id);
                
                lists.add(data);
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

    //added by dewok 20180611
    public static ArrayList getSalesPersonPointInsentif(long employeeId, int materialMain, Date tglAwal, Date tglAkhir) {
        ArrayList<Double> data = new ArrayList<Double>();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + " SELECT"
                    + " SUM(INSENTIF_POINT) AS poin,"
                    + " SUM(INSENTIF_VALUE) AS nominal"
                    + " FROM " + PstInsentifData.TBL_INSENTIF_DATA
                    + " WHERE " + PstInsentifData.fieldNames[PstInsentifData.FLD_EMPLOYEE_ID] + " = " + employeeId
                    + " AND " + PstInsentifData.fieldNames[PstInsentifData.FLD_CASH_BILL_DETAIL_ID] + " IN "
                    + " (SELECT cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]
                    + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS cbd "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm"
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS mat"
                    + " ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + " WHERE "
                    + " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = " + materialMain
                    + " AND DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + Formater.formatDate(tglAwal, "yyyy-MM-dd") + "'"
                    + " AND '" + Formater.formatDate(tglAkhir, "yyyy-MM-dd") + "'"
                    + " )"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {                
                data.add(rs.getDouble("poin"));
                data.add(rs.getDouble("nominal"));
            }
            rs.close();
            return data;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new ArrayList();
    }

}
