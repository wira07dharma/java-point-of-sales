/*
 * SessBilling.java
 *
 * Created on October 14, 2003, 10:27 AM
 */

package com.dimata.pos.session.billing;

import java.sql.ResultSet;
import com.dimata.util.Formater;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import com.dimata.pos.entity.balance.*;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.pos.entity.search.SrcInvoice;
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.pos.entity.masterCashier.*;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;

/**
 *
 * @author  pman
 * @version
 */

/*package java*/

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;


//import entity

public class SessBilling {
    
    public static final String SRC_INVOICE_FOR_CANCEL = "SRC_INVOICE_FOR_CANCEL";
    public static final int LOG_MODE_NONE = 0;
    public static final int LOG_MODE_CONSOLE = 1;
    public static final int LOG_MODE_FILE = 2;
    
    /** Creates new SessBilling */
    public SessBilling() {
    }
    
    
    public static int sumInvoice(long cashId) {
        Vector lists = new Vector();
        int result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(CB." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + ") AS SumBill " +
            "FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CB " +
            "INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CC ON " +
            "CB." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " +
            "CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + " " +
            "WHERE CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + " = " + cashId;
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            lists = new Vector();
            while (rs.next()) {
                //Vector vector=new Vector();
                //vector.add(""+rs.getInt("SumBill"));
                //lists.add(vector);
                result = rs.getInt(1);
            }
            rs.close();
            // return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        //return new Vector();
        return result;
    }
    
    //query for sum detail product
    public static int sumProduct(long cashId) {
        Vector lists = new Vector();
        int result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SumDetail " +
            "FROM (" + PstCashCashier.TBL_CASH_CASHIER + " CC " +
            "INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CB ON " +
            "CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + " = " +
            "CB." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + ") " +
            "INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CD ON " +
            "CB." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = " +
            "CD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " " +
            "WHERE CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + " = " + cashId;
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            lists = new Vector();
            while (rs.next()) {
                /* Vector vector=new Vector();
                   vector.add(""+rs.getInt("SumDetail"));
                 lists.add(vector);*/
                result = rs.getInt(1);
            }
            rs.close();
            //return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        //return new Vector();
        return result;
    }
    
    public static Vector getBillDetail(long oid) {
        String whereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + oid;
        return PstBillDetail.list(0, 0, whereClause, "");
    }
    
    public static long getBillId(String invoice) {
        long oid = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN +
            " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + "='" + invoice + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();
            return oid;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return oid;
    }
    
    public static long deleteBillDetail(long oidMain) {
        long oid = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " DELETE FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL+
            " WHERE " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "='" + oidMain + "'";
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return oidMain;
    }
    
    public static BillMain getBillMain(long oid) {
        BillMain bill = new BillMain();
        try {
            bill = PstBillMain.fetchExc(oid);
        } catch (Exception e) {
        }
        return bill;
    }
    
    public static Vector getPayment(long billId) {
        String whereClause = PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + " = " + billId;
        return PstCashPayment.list(0, 0, whereClause, "");
    }
    
    public static Vector getReturn(long billId) {
        String whereClause = PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + " = " + billId;
        return PstCashReturn.list(0, 0, whereClause, "");
    }
    
    public Vector getListInvoice(SrcInvoice srcInvoice, int start, int recordToGet, int logMode) {
        Vector result = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " as cbm " +
            "inner join " + PstContactList.TBL_CONTACT_LIST + " as member " +
            "on member." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = cbm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + "";
            
            String invoiceNo = "";
            if (srcInvoice.getInvoiceNumber() != null && srcInvoice.getInvoiceNumber().length() > 0) {
                invoiceNo = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "=" + srcInvoice.getInvoiceNumber();
            }
            
            String memberName = "";
            if (srcInvoice.getMemberName() != null && srcInvoice.getMemberName().length() > 0) {
                memberName = " member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " like '%" + srcInvoice.getMemberName() + "%' ";
            }
            
            String invoiceStatus = "";
            if (srcInvoice.getTransStatus() != -1) {
                invoiceStatus = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + srcInvoice.getTransStatus();
            }
            
            String invoiceDate = "";
            if (srcInvoice.getInvoiceDate() != null && srcInvoice.getInvoiceDateTo() != null) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                invoiceDate = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'";
            }
            
            String whereClause = "";
            if (invoiceNo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceNo;
                }
                else {
                    whereClause = whereClause + invoiceNo;
                }
            }
            
            if (memberName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberName;
                }
                else {
                    whereClause = whereClause + memberName;
                }
            }
            
            if (invoiceDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceDate;
                }
                else {
                    whereClause = whereClause + invoiceDate;
                }
            }
            
            if (invoiceStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceStatus;
                }
                else {
                    whereClause = whereClause + invoiceStatus;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause;
            }
            
            if (recordToGet > 0) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }
            
            
            if (logMode == LOG_MODE_CONSOLE) {
                System.out.println("Search Invoice: " + sql);
            }
            else if (logMode == LOG_MODE_FILE) {
                
            }
            else if (logMode == LOG_MODE_NONE) {
                
            }
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SrcInvoice invoice = new SrcInvoice();
                invoice.setInvoiceId(rs.getLong("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ""));
                Date tDate = DBHandler.convertDate(rs.getDate("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ""), rs.getTime("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ""));
                invoice.setInvoiceDate(tDate);
                invoice.setMemberId(rs.getLong("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + ""));
                invoice.setMemberName(rs.getString("member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + ""));
                invoice.setCustomerName(rs.getString("member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + ""));
                invoice.setInvoiceNumber(rs.getString("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + ""));
                invoice.setTransStatus(rs.getInt("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + ""));
                
                if (logMode == LOG_MODE_CONSOLE) {
                    invoice.printValues();
                }
                else if (logMode == LOG_MODE_FILE) {
                    
                }
                else if (logMode == LOG_MODE_NONE) {
                    
                }
                
                result.add(invoice);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param iDocType
     * @param srcInvoice
     * @param start
     * @param recordToGet
     * @param logMode
     * @return
     * @created by Edhy
     */
    public Vector getListInvoice(SrcInvoice srcInvoice, int start, int recordToGet, int iDocType, int logMode) {
        Vector result = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " as cbm " +
            "inner join " + PstContactList.TBL_CONTACT_LIST + " as member " +
            "on member." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = cbm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + "";
            
            String invoiceNo = "";
            if (srcInvoice.getInvoiceNumber() != null && srcInvoice.getInvoiceNumber().length() > 0) {
                invoiceNo = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "=" + srcInvoice.getInvoiceNumber();
            }
            
            String memberName = "";
            if (srcInvoice.getMemberName() != null && srcInvoice.getMemberName().length() > 0) {
                memberName = " member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " like '%" + srcInvoice.getMemberName() + "%' ";
            }
            
            String invoiceStatus = "";
            if (srcInvoice.getTransStatus() != -1) {
                invoiceStatus = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + srcInvoice.getTransStatus();
            }
            
            String invoiceDate = "";
            if (srcInvoice.getInvoiceDate() != null && srcInvoice.getInvoiceDateTo() != null) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                invoiceDate = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'";
            }
            
            String strDocType = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] +
            " = " + iDocType;
            
            String whereClause = "";
            if (invoiceNo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceNo;
                }
                else {
                    whereClause = whereClause + invoiceNo;
                }
            }
            
            if (memberName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberName;
                }
                else {
                    whereClause = whereClause + memberName;
                }
            }
            
            if (invoiceDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceDate;
                }
                else {
                    whereClause = whereClause + invoiceDate;
                }
            }
            
            if (invoiceStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceStatus;
                }
                else {
                    whereClause = whereClause + invoiceStatus;
                }
            }
            
            if (strDocType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDocType;
                }
                else {
                    whereClause = whereClause + strDocType;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause;
            }
            
            if (recordToGet > 0) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }
            
            
            if (logMode == LOG_MODE_CONSOLE) {
                System.out.println("Search Invoice: " + sql);
            }
            else if (logMode == LOG_MODE_FILE) {
                
            }
            else if (logMode == LOG_MODE_NONE) {
                
            }
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SrcInvoice invoice = new SrcInvoice();
                invoice.setInvoiceId(rs.getLong("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ""));
                Date tDate = DBHandler.convertDate(rs.getDate("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ""), rs.getTime("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ""));
                invoice.setInvoiceDate(tDate);
                invoice.setMemberId(rs.getLong("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + ""));
                invoice.setMemberName(rs.getString("member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + ""));
                invoice.setCustomerName(rs.getString("member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + ""));
                invoice.setInvoiceNumber(rs.getString("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + ""));
                invoice.setTransStatus(rs.getInt("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + ""));
                
                if (logMode == LOG_MODE_CONSOLE) {
                    invoice.printValues();
                }
                else if (logMode == LOG_MODE_FILE) {
                    
                }
                else if (logMode == LOG_MODE_NONE) {
                    
                }
                
                result.add(invoice);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcInvoice
     * @param iDocType
     * @return
     * @created by Edhy
     */
    public int getCountListInvoice(SrcInvoice srcInvoice, int iDocType) {
        int result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "select count(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ")" +
            " from " + PstBillMain.TBL_CASH_BILL_MAIN + " as cbm " +
            " inner join " + PstContactList.TBL_CONTACT_LIST + " as member " +
            " on member." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = cbm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + "";
            
            String invoiceNo = "";
            if (srcInvoice.getInvoiceNumber() != null && srcInvoice.getInvoiceNumber().length() > 0) {
                invoiceNo = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "=" + srcInvoice.getInvoiceNumber();
            }
            
            String memberName = "";
            if (srcInvoice.getMemberName() != null && srcInvoice.getMemberName().length() > 0) {
                memberName = " member." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " like '%" + srcInvoice.getMemberName() + "%' ";
            }
            
            String invoiceStatus = "";
            if (srcInvoice.getTransStatus() != -1) {
                invoiceStatus = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + srcInvoice.getTransStatus();
            }
            
            String invoiceDate = "";
            if (srcInvoice.getInvoiceDate() != null && srcInvoice.getInvoiceDateTo() != null) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                invoiceDate = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'";
            }
            
            String strDocType = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] +
            " = " + iDocType;
            
            String whereClause = "";
            if (invoiceNo.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceNo;
                }
                else {
                    whereClause = whereClause + invoiceNo;
                }
            }
            
            if (memberName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberName;
                }
                else {
                    whereClause = whereClause + memberName;
                }
            }
            
            if (invoiceDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceDate;
                }
                else {
                    whereClause = whereClause + invoiceDate;
                }
            }
            
            if (invoiceStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceStatus;
                }
                else {
                    whereClause = whereClause + invoiceStatus;
                }
            }
            
            if (strDocType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDocType;
                }
                else {
                    whereClause = whereClause + strDocType;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause;
            }
            
            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /**
     * untuk mencari data lokasi yang link dengan cash master
     * @return
     */
    public static Vector getCashMaster(){
        try{
            
        }catch(Exception e){}
        return new Vector();
    }
    
    /**
     * Fungsi ini digunakan untuk mendapatkan detail Invoice
     */
    public static Vector listInvoiceDetail(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT";
            sql+= " BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID];
            sql+= ", BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_SKU];
            sql+= ", BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME];
            sql+= ", BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE];
            sql+= ", BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC];
            sql+= ", BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY];
            sql+= ", BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE];
            sql+= ", UNIT."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];
            sql+= ", UNIT."+PstUnit.fieldNames[PstUnit.FLD_CODE];
            sql+= ", PM."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE];
            sql+= ", PM."+PstMaterial.fieldNames[PstMaterial.FLD_NAME];
            sql+= " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL+" AS BILL_DETAIL";
            sql+= " LEFT JOIN "+PstUnit.TBL_P2_UNIT+" AS UNIT";
            sql+= " ON BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]+"=UNIT."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];
            sql+= " LEFT JOIN "+PstMaterial.TBL_MATERIAL+" AS PM";
            sql+= " ON BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"=PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;
                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    break;
                case DBHandler.DBSVR_SYBASE :
                    break;
                case DBHandler.DBSVR_ORACLE :
                    break;
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            }
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                Billdetail billDetail = new Billdetail();
                Unit unit = new Unit();
                Material material = new Material();
                
                billDetail.setOID(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]));
                billDetail.setSku(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]));
                billDetail.setItemName(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]));
                billDetail.setItemPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
                billDetail.setDisc(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]));
                billDetail.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
                billDetail.setTotalPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]));
                temp.add(billDetail);
                
                unit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unit);
                
                material.setBarCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                temp.add(material);
                
                lists.add(temp);
            }
            rs.close();
            return lists;
            
        }catch(Exception e) {
            System.out.println(e.toString());
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    /**
     * Fungsi ini digunakan untuk mendapatkan nilai Grand Total dari list invoice
     * create by: gwawan@dimata 15 Juni 2007
     * @param srcSaleReport SrcSaleReport
     * @return result Vector
     */
    public static Vector getGrandTotal(SrcSaleReport srcSaleReport) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        String sql = "";
        String whereClause = "";
        try {
            if(srcSaleReport.getCurrencyOid()!=0) {
                sql+= " select bm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" as id";
                sql+= ", sum(bd."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+") as bruto";
                sql+= ", (bm."+PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT]+") as diskon";
                sql+= ", (bm."+PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE]+") as pajak";
                sql+= ", (bm."+PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE]+") as service";
                sql+= " from "+PstBillMain.TBL_CASH_BILL_MAIN+" bm inner join "+PstBillDetail.TBL_CASH_BILL_DETAIL+" bd";
                sql+= " on bm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = bd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID];
            }
            else {
                sql+= " select bm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" as id";
                sql+= ", sum(bd."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+"*bm."+PstBillMain.fieldNames[PstBillMain.FLD_RATE]+") as bruto";
                sql+= ", (bm."+PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT]+"*bm."+PstBillMain.fieldNames[PstBillMain.FLD_RATE]+") as diskon";
                sql+= ", (bm."+PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE]+"*bm."+PstBillMain.fieldNames[PstBillMain.FLD_RATE]+") as pajak";
                sql+= ", (bm."+PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE]+"*bm."+PstBillMain.fieldNames[PstBillMain.FLD_RATE]+") as service";
                sql+= " from "+PstBillMain.TBL_CASH_BILL_MAIN+" bm inner join "+PstBillDetail.TBL_CASH_BILL_DETAIL+" bd";
                sql+= " on bm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = bd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID];
            }
            
            /** tanggal */
            String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
            String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");
            
            whereClause = "bm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'";
            
            /** tipe penjualan */
            if(srcSaleReport.getTransType() == -1){
                whereClause = whereClause + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " AND (bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " OR bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " AND (bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " OR bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
            }else if(srcSaleReport.getTransType() == PstCashPayment.CASH) {
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+srcSaleReport.getTransType();
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
            }else {
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+srcSaleReport.getTransType();
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            }
            
            /** shift */
            if(srcSaleReport.getShiftId()!=0) {
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
            }
            
            /** lokasi */
            if(srcSaleReport.getLocationId()!=0) {
                whereClause += " AND bm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
            }
            
            /** currency */
            if(srcSaleReport.getCurrencyOid()!=0) {
                whereClause += " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
            }
            
            if (whereClause != null && whereClause.length() > 0)
                sql += " WHERE " + whereClause;
            
            sql += " group by id";
            
            System.out.println(">>>"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double bruto = 0, diskon = 0, pajak = 0, service = 0;
            while(rs.next()) {
                bruto += rs.getDouble("bruto");
                diskon += rs.getDouble("diskon");
                pajak += rs.getDouble("pajak");
                service += rs.getDouble("service");
            }
            
            result.add(String.valueOf(bruto));
            result.add(String.valueOf(diskon));
            result.add(String.valueOf(pajak));
            result.add(String.valueOf(service));
            return result;
        }
        catch(Exception e) {
            System.out.println("Exc in SessBilling.getGrandTotal(#) : "+e.toString());
        }
        return result;
    }
    
    /**
     * Ari Wiweka 20130625
     * Fungsi untuk generate Invoice Number
     * @param args
     */
    public static String getCodeOrderMaterial(BillMain billMain) {
        String strCode = "";
        try {
            AppUser ap = new AppUser();
            ap = PstAppUser.fetch(billMain.getAppUserSalesId());
            /** get location code; gwawan@21juni2007 */
            long cashmasterid = 0;
            Vector vctCasMasterId = PstCashCashier.list(0, 0, PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+"="+billMain.getCashCashierId(), "");
            CashCashier cashCashier = new CashCashier();
            if(vctCasMasterId.size()>0){
                cashCashier = (CashCashier)vctCasMasterId.get(0);
                cashmasterid = cashCashier.getCashMasterId();
            }

            Vector vctCabang = PstCashMaster.list(0, 0, PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+cashmasterid, "");
            CashMaster cashMaster = new CashMaster();
            if(vctCabang.size()>0){
                cashMaster = (CashMaster)vctCabang.get(0);
            }

            if(billMain.getTransactionStatus()==2 && billMain.getTransctionType()==0){
               //Transaksi Cancel dilambangkan dengan "C"
               strCode = cashMaster.getCabang() + "." +
                    getYearMonth(billMain.getBillDate(), TYPE_GET_DATE) +
                    "" + getYearMonth(billMain.getBillDate(), TYPE_GET_MONTH) +
                    "" + getYearMonth(billMain.getBillDate(), TYPE_GET_YEAR) +
                    "." + getCashierNumber(cashMaster.getCashierNumber()) +
                    "" + getCounter(billMain.getInvoiceCounter())+ "C";
            }else if(billMain.getTransactionStatus()==1 && billMain.getTransctionType()==0){
               //Transaksi Open Bill dilambangkan dengan "X"
                if(billMain.getAppUserSalesId()==0){
                  strCode = cashMaster.getCabang() + "." +
                           getYearMonth(billMain.getBillDate(), TYPE_GET_DATE) +
                            "" + getYearMonth(billMain.getBillDate(), TYPE_GET_MONTH) +
                            "" + getYearMonth(billMain.getBillDate(), TYPE_GET_YEAR) +
                            "."+ Formater.formatDate(billMain.getBillDate(),"hhmmss")+
                            "" + getCounter(billMain.getInvoiceCounter())+ "X";
                }else{
                 strCode =  ap.getFullName()+ "." +
                            getYearMonth(billMain.getBillDate(), TYPE_GET_DATE) +
                            "" + getYearMonth(billMain.getBillDate(), TYPE_GET_MONTH) +
                            "" + getYearMonth(billMain.getBillDate(), TYPE_GET_YEAR) +
                            "."+ Formater.formatDate(billMain.getBillDate(),"hhmmss")+
                            "" + getCounter(billMain.getInvoiceCounter())+ "X";
                }
                
            }else{
            strCode = cashMaster.getCabang() + "." +
                    getYearMonth(billMain.getBillDate(), TYPE_GET_DATE) +
                    "" + getYearMonth(billMain.getBillDate(), TYPE_GET_MONTH) +
                    "" + getYearMonth(billMain.getBillDate(), TYPE_GET_YEAR) +
                    "." + getCashierNumber(cashMaster.getCashierNumber()) +
                    "" + getCounter(billMain.getInvoiceCounter());
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        }
        return strCode;
    }

    public static int getIntCode(BillMain billMain, Date pDate, long oid, int counter) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_COUNTER] +
                    ") AS PMAX FROM " + PstBillMain.TBL_CASH_BILL_MAIN +
                    " WHERE YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = "+ (billMain.getBillDate().getYear() + 1900) + "" +
                    " AND MONTH(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = " + (billMain.getBillDate().getMonth() + 1) + "" +
                    " AND DATE(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = " + (billMain.getBillDate().getDate()) +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = '" + billMain.getLocationId() +
                    "' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '"+ billMain.getTransactionStatus() +
                    "' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = '"+ billMain.getTransctionType() +"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                max = rs.getInt("PMAX");
            }
            rs.close();

            if (oid == 0) {
                max = max + 1;
            } else {
                if (billMain.getBillDate() != pDate)
                    max = max + 1;
                else
                    max = counter;
            }

        } catch (Exception e) {
            System.out.println("Err at counter : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }

    /**
     * this method used to getNextIndex of maximum number of PR number
     * return : int new number
     *
     * modified on : June 27, 2003 10:53 PM
     * modified by : gedhy
     */
    public static int getIntCode(BillMain billMain, Date pDate, long oid, int counter, boolean isIncrement) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_COUNTER] +
                    ") AS PMAX FROM " + PstBillMain.TBL_CASH_BILL_MAIN +
                    " WHERE YEAR(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = "+ (billMain.getBillDate().getYear() + 1900) + "" +
                    " AND MONTH(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") = " + (billMain.getBillDate().getMonth() + 1) +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = '" + billMain.getLocationId() +
                    "' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '"+ billMain.getTransactionStatus() +
                    "' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = '"+ billMain.getTransctionType() +"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                max = rs.getInt("PMAX");
            }

            if (oid == 0) {
                max = max + 1;
            } else {
                if (billMain.getBillDate() != pDate)
                    max = max + 1;
                else
                    max = counter;
            }
        } catch (Exception e) {
            System.out.println("!!!!!SessOrderMaterial.getIntCode() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }

    public static String getCounter(int counter) {
        String strCounter = "";
        String str = String.valueOf(counter);
        switch (str.length()) {
            case 1:
                strCounter = "00000" + counter;
                break;
            case 2:
                strCounter = "0000" + counter;
                break;
            case 3:
                strCounter = "000" + counter;
                break;
            case 4:
                strCounter = "00" + counter;
                break;
            case 5:
                strCounter = "0" + counter;
                break;
            case 6:
                strCounter = "" + counter;
                break;
            default:
                strCounter = "" + counter;
        }
        return strCounter;
    }

    public static String getCashierNumber(int cashierNumber) {
        String strCashNumber = "";
        String str = String.valueOf(cashierNumber);
        switch (str.length()) {
            case 1:
                strCashNumber = "0" + cashierNumber;
                break;
            case 2:
                strCashNumber = "" + cashierNumber;
                break;
            default:
                strCashNumber = "" + cashierNumber;
        }
        return strCashNumber;
    }

    public static final int TYPE_GET_YEAR = 0;
    public static final int TYPE_GET_MONTH = 1;
    public static final int TYPE_GET_DATE = 2;

    public static String getYearMonth(Date date, int getType) {
        String str = "";
        try {
            switch (getType) {
                case TYPE_GET_YEAR:
                    str = "" + date.getYear();
                    str = str.substring(str.length() - 2, str.length());
                    break;

                case TYPE_GET_MONTH:
                    str = "" + (date.getMonth() + 1);
                    if (str.length() != 2)
                        str = "0" + str;
                    break;

                case TYPE_GET_DATE:
                    str = "" + (date.getDate());
                    break;

                default:
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        }
        return str;
    }


    public static void main(String args[]) {
        //long cahId=504404224380385384;
        SessBilling sessBilling = new SessBilling();
        SrcInvoice invoice = new SrcInvoice();
        invoice.setMemberName("Mem");
        Vector result = sessBilling.getListInvoice(invoice, 1, 199, 0);
        Enumeration en = result.elements();
        while (en.hasMoreElements()) {
            SrcInvoice src = (SrcInvoice) en.nextElement();
            src.printValues();
        }
        // int totalInv = sessBilling.sumInvoice(1);
        // System.out.println("jumlah inv ="+totalInv);
        // int totalPrd = sessBilling.sumProduct(1);
        // System.out.println("jumlah product ="+totalPrd);
        //SessBilling.listInvoiceDetail(0, 0,  "","");
    }
    
}
