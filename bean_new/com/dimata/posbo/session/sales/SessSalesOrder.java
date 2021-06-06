/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.sales;

import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.location.*;
import com.dimata.common.entity.logger.*;
import com.dimata.common.entity.payment.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.util.Command;
import com.dimata.util.Round;
import com.dimata.util.Formater;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class SessSalesOrder {
    
    public static Vector listJoinBillMainDetail(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT * FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD "
                    + " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
                lists.add(billMain);
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

    public static Vector listJoinBillDetailMaterial(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT * FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD "
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS PM "
                    + " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + " = PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billdetail = new Billdetail();
                PstBillDetail.resultToObject(rs, billdetail);
                lists.add(billdetail);
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

    public static Vector listJoinBillMainDetailMaterial(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT * FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD "
                    + " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS PM "
                    + " ON PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector list = new Vector();

                BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
                billMain.setParentSalesOrderId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID]));
                list.add(billMain);

                Billdetail billdetail = new Billdetail();
                PstBillDetail.resultToObject(rs, billdetail);
                list.add(billdetail);

                Material material = new Material();
                PstMaterial.resultToObject(rs, material);
                list.add(material);

                lists.add(list);
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

    public static int getCashierNumber(long idBillMainOrder) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT"
                    + " cash_master.CASHIER_NUMBER "
                    + " FROM cash_master "
                    + " INNER JOIN cash_cashier "
                    + " ON cash_master.CASH_MASTER_ID = cash_cashier.CASH_MASTER_ID "
                    + " INNER JOIN cash_bill_main "
                    + " ON cash_cashier.CASH_CASHIER_ID = cash_bill_main.CASH_CASHIER_ID "
                    + " WHERE cash_bill_main.CASH_BILL_MAIN_ID = '" + idBillMainOrder + "'"
                    + "";
            //if (whereClause != null && whereClause.length() > 0) {
            //    sql = sql + " WHERE " + whereClause;
            //}

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static int listTaxService(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " location." + PstLocation.fieldNames[PstLocation.FLD_TAX_SVC_DEFAULT]
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS billMain "
                    + " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS location "
                    + " ON billMain." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                    + " = location." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static double getCreditPaymentAmount(long billMainId) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ccp." + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]
                    + " FROM " + PstCashCreditPayment.TBL_PAYMENT + " AS ccp "
                    + " INNER JOIN " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " AS cpm "
                    + " ON cpm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]
                    + " = ccp." + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]
                    + " WHERE cpm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + billMainId + "'"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static String getCreditPaymentType(long billMainId) {
        String type = "";
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM]
                    + " FROM " + PstPaymentSystem.TBL_P2_PAYMENT_SYSTEM + " AS ps"
                    + " INNER JOIN " + PstCashCreditPaymentDinamis.TBL_PAYMENT + " AS ccp"
                    + " ON ccp." + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAY_TYPE]
                    + " = ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]
                    + " INNER JOIN " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " AS cpm"
                    + " ON cpm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]
                    + " = ccp." + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_CREDIT_MAIN_ID]
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm"
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cpm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]
                    + " WHERE cpm." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + billMainId + "'"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                type = rs.getString(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return type;
    }

    public static String getPaymentType(long billMainId) {
        String type = "";
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM]
                    + " FROM " + PstPaymentSystem.TBL_P2_PAYMENT_SYSTEM + " AS ps"
                    + " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " AS cp"
                    + " ON cp." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]
                    + " = ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cbm"
                    + " ON cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = cp." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " WHERE cp." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + " = '" + billMainId + "'"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                type = rs.getString(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return type;
    }

    public static double getAddCharge(long billMainId) {
        double amount = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT cpi.BANK_COST "
                    + " FROM cash_credit_payment_info cpi"
                    + " INNER JOIN cash_credit_payment ccp"
                    + " ON ccp.CASH_CREDIT_PAYMENT_ID = cpi.CASH_CREDIT_PAYMENT_ID"
                    + " INNER JOIN cash_credit_payment_main cpm"
                    + " ON cpm.CREDIT_PAYMENT_MAIN_ID = ccp.CREDIT_PAYMENT_MAIN_ID"
                    + " INNER JOIN cash_bill_main cbm"
                    + " ON cbm.CASH_BILL_MAIN_ID = cpm.CASH_BILL_MAIN_ID"
                    + " WHERE cpm.CASH_BILL_MAIN_ID "
                    + " = '" + billMainId + "'"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                amount = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return amount;
    }
	
	public static double getDownPayment(long billMainId) {
        double dp = 0;
        Vector<CreditPaymentMain> listDp = PstCreditPaymentMain.list(0, 0, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = " + billMainId, "");
        for (CreditPaymentMain cpm : listDp) {
            dp += cpm.getPayAmountCredit();
        }
        return dp;
    }

    public static double getFirstDownPayment(long billMainId) {
        double dp = 0;
        Vector<CreditPaymentMain> listDp = PstCreditPaymentMain.list(0, 0, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = " + billMainId,
				PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_PAYMENT_DATE]);
		int no = 0;
        for (CreditPaymentMain cpm : listDp) {
            if (cpm.getDocType() == 6){
				dp += cpm.getPayAmountCredit();
			} else if (no == 0){
				dp = cpm.getPayAmountCredit();
			}
			no++;
        }
        return dp;
    }
    
    public static double getPayment(long billMainId) {
        double payment = 0;
        Vector<CashPayments1> listPayment = PstCashPayment1.list(0, 0, PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID] + " = " + billMainId, "");
        for (CashPayments1 cp : listPayment) {
            payment += cp.getAmount();
        }
        return payment;
    }

    public static double getTotalBillOrder(long billMainId) {
        double total = 0;
        Vector<Billdetail> dataDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + billMainId, "");
        for (Billdetail bd : dataDetail) {
            Material material;
            try {
                material = PstMaterial.fetchExc(bd.getMaterialId());
            } catch (Exception ex) {
                material = new Material();
            }
            double totalPrice = 0;
            if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                totalPrice = bd.getItemPrice() + bd.getCost() + bd.getSusutanPrice();
            } else {
                totalPrice = (bd.getBerat() * bd.getItemPrice()) + bd.getCost() + (bd.getSusutanWeight() * bd.getItemPrice()) + (bd.getAdditionalWeight() * bd.getLatestItemPrice());
            }
            double afterDisc = totalPrice - ((bd.getDiscPct() / 100) * totalPrice);
            double afterTax = afterDisc + ((bd.getTaxPct() / 100) * afterDisc);
            total += afterTax;
        }
        return total;
    }

    public static double getTotalBillFinishOrder(long billMainOrder, long billMainId) {
        double total = 0;
        
        double nilaiOrder = getTotalBillOrder(billMainOrder);
        double dpOrder = getDownPayment(billMainOrder);
        boolean dpLebihBesar = dpOrder > (nilaiOrder / 2);
        
        Vector<Billdetail> dataDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + billMainId, "");
        for (Billdetail bd : dataDetail) {
            Material material;
            try {
                material = PstMaterial.fetchExc(bd.getMaterialId());
            } catch (Exception ex) {
                material = new Material();
            }
            double totalPrice = 0;            
            if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                totalPrice = bd.getItemPrice() + bd.getCost() + bd.getSusutanPrice();
            } else {
                double itemPriceBerlaku = dpLebihBesar ? bd.getItemPrice() : bd.getLatestItemPrice();
                totalPrice = (bd.getBerat() * itemPriceBerlaku) + bd.getCost() + (bd.getSusutanWeight() * bd.getItemPrice()) + (bd.getAdditionalWeight() * bd.getLatestItemPrice());
            }
            double afterDisc = totalPrice - ((bd.getDiscPct() / 100) * totalPrice);
            double afterTax = afterDisc + ((bd.getTaxPct() / 100) * afterDisc);
            total += Round.round(afterTax,2);
        }
        return total;
    }

    public static Vector listJoinBillMainPayment(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT * FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD "
                    + " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS PM "
                    + " ON PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + " INNER JOIN " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " AS CCPM "
                    + " ON CCPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector list = new Vector();

                BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
                billMain.setParentSalesOrderId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID]));
                list.add(billMain);

                Billdetail billdetail = new Billdetail();
                PstBillDetail.resultToObject(rs, billdetail);
                list.add(billdetail);

                Material material = new Material();
                PstMaterial.resultToObject(rs, material);
                list.add(material);

                CreditPaymentMain creditPaymentMain = new CreditPaymentMain();
                PstCreditPaymentMain.resultToObject(rs, creditPaymentMain);
                list.add(creditPaymentMain);

                lists.add(list);
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

    //==========================================================================
    public static int createInvoice(long idBillOrder) {
        return duplicateBillOrder(idBillOrder, new Date());
    }

    public static String getInvoiceNumber(long idBillMainOrder) {
        String invoiceNumb = "";
        int cashierNumber = getCashierNumber(idBillMainOrder);
        String cashierNumberFormat = "00" + cashierNumber + "." + Formater.formatDate(new Date(), "yyyyMMdd");
        Vector listLastInvcNumber = PstBillMain.list(0, 1, PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + cashierNumberFormat + "%' "
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " NOT LIKE '%C'", PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " DESC");
        if (!listLastInvcNumber.isEmpty()) {
            BillMain billMainInv = (BillMain) listLastInvcNumber.get(0);
            String[] number = billMainInv.getInvoiceNo().split("\\.");
            int newNumber = Integer.parseInt(number[2]) + 1;
            String lastNumber = "000" + newNumber;
            lastNumber = lastNumber.substring(lastNumber.length() - 3);
            invoiceNumb = cashierNumberFormat + "." + lastNumber;
        } else {
            invoiceNumb = cashierNumberFormat + ".001";
        }
        return invoiceNumb;
    }

    public static int duplicateBillOrder(long idBillMainOrder, Date tglProduksi) {
        int error = 0;
        BillMain bm = new BillMain();
        if (idBillMainOrder > 0 && PstBillMain.checkOID(idBillMainOrder)) {
            try {
                bm = PstBillMain.fetchExc(idBillMainOrder);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return 1;
            }
        }

        //duplicate data bill main dan bill detail
        Vector listBillDetail = PstBillDetail.list(0, 0, "" + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = '" + idBillMainOrder + "'", "");
        long oidNewBillMain = 0;
        for (int i = 0; i < listBillDetail.size(); i++) {
            try {
                Billdetail newBilldetail = (Billdetail) listBillDetail.get(i);
                //cek tipe item emas lantakan
                Material material = PstMaterial.fetchExc(newBilldetail.getMaterialId());
                if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                    //bill detail dgn item emas lantakan tidak dibuatkan bill detail baru
                    continue;
                }

                //create new bill main
                BillMain newBillMain = PstBillMain.fetchExc(idBillMainOrder);
                newBillMain.setOID(0);
                newBillMain.setParentSalesOrderId(idBillMainOrder);
                newBillMain.setDocType(PstBillMain.TYPE_INVOICE);
                newBillMain.setTransctionType(PstBillMain.TRANS_TYPE_CREDIT);
                newBillMain.setTransactionStatus(PstBillMain.TRANS_STATUS_DELETED);
                newBillMain.setStatusInv(PstBillMain.INVOICING_ON_PROSES);
                newBillMain.setNotes(bm.getNotes() + " - [Duplicate bill main from sales order : " + bm.getInvoiceNo() + "]");
                newBillMain.setBillDate(tglProduksi);
                String invoiceNumb = getInvoiceNumber(idBillMainOrder);
                newBillMain.setInvoiceNumber(invoiceNumb);
                newBillMain.setInvoiceNo(invoiceNumb);
                newBillMain.setIsService(newBilldetail.getMaterialType());
                oidNewBillMain = PstBillMain.insertExc(newBillMain);
				
				BillMain billMainSO = PstBillMain.fetchExc(idBillMainOrder);
				try {
					billMainSO.setBillStatus(0);
					billMainSO.setTransactionStatus(PstBillMain.TRANS_STATUS_CLOSE);
					billMainSO.setStatusInv(PstBillMain.INVOICING_FINISH);
					PstBillMain.updateExc(billMainSO);
				} catch (Exception exc){
				}

                //create new bill detail
                newBilldetail.setOID(0);
                newBilldetail.setBillMainId(oidNewBillMain);
                newBilldetail.setNote(newBilldetail.getNote() + " - [Duplicate bill detail from sales order : " + bm.getInvoiceNo() + "]");
                long oidNewBillDetail = PstBillDetail.insertExc(newBilldetail);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                error = 1;
            }
        }

        //cek jika item servis maka item emas lantakan ditambahkan ke salah satu bill main
        if (bm.getIsService() == 1) {
            //get item emas lantakan
            String where = ""
                    + " BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = '" + idBillMainOrder + "'"
                    + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = " + Material.MATERIAL_TYPE_EMAS_LANTAKAN;

            Vector listEmasLantakan = listJoinBillDetailMaterial(0, 0, "" + where, "");
            for (int i = 0; i < listEmasLantakan.size(); i++) {
                try {
                    Billdetail billdetail = (Billdetail) listEmasLantakan.get(i);
                    //add new bill detail
                    billdetail.setOID(0);
                    billdetail.setBillMainId(oidNewBillMain);
                    billdetail.setNote("[Insert emas lantakan for sales order : " + bm.getInvoiceNo() + "] " + billdetail.getNote());
                    long oidNewBillDetail = PstBillDetail.insertExc(billdetail);
                } catch (com.dimata.posbo.db.DBException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return error;
    }
    //==========================================================================
    public static int updateDetailOrder(Billdetail billDetailOrder, Material matBillDetail, long userId, String userName) {
        int error = 0;
        //update sku material
        try {
            String codeSku = "";
            if (matBillDetail.getKepemilikanId() != 0 && PstContactList.checkOID(matBillDetail.getKepemilikanId())) {
                ContactList contactList = PstContactList.fetchExc(matBillDetail.getKepemilikanId());
                codeSku += contactList.getContactCode();
            }
            if (matBillDetail.getCategoryId() != 0 && PstCategory.checkOID(matBillDetail.getCategoryId())) {
                Category category = PstCategory.fetchExc(matBillDetail.getCategoryId());
                codeSku += category.getCode();
            }
            if (matBillDetail.getMerkId() != 0 && PstMerk.checkOID(matBillDetail.getMerkId())) {
                Merk merk = PstMerk.fetchExc(matBillDetail.getMerkId());
                codeSku += merk.getCode();
            }
            if (matBillDetail.getPosKadar() != 0 && PstKadar.checkOID(matBillDetail.getPosKadar())) {
                Kadar kadar = PstKadar.fetchExc(matBillDetail.getPosKadar());
                codeSku += kadar.getKodeKadar();
            }
            if (matBillDetail.getSupplierId() != 0 && PstContactList.checkOID(matBillDetail.getSupplierId())) {
                ContactList contactList = PstContactList.fetchExc(matBillDetail.getSupplierId());
                codeSku += contactList.getContactCode();
            }
            if (matBillDetail.getPosColor() != 0 && PstColor.checkOID(matBillDetail.getPosColor())) {
                Color color = PstColor.fetchExc(matBillDetail.getPosColor());
                codeSku += color.getColorCode();
            }

            //update data material
            if (matBillDetail.getMaterialJenisType() != Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                String sku = matBillDetail.getSku();
				if (sku.length() == 14){
					String lastDigitSku = matBillDetail.getSku().substring(sku.length() - 7);
                    matBillDetail.setSku(codeSku + lastDigitSku);
                    matBillDetail.setBarCode(matBillDetail.getSku());
				} else if (!sku.equals(codeSku)){
                    String year = Formater.formatDate(new Date(), "yyyy");
                    year = year.substring(year.length() - 2, year.length());
                    int lastCounter = PstMaterial.getLastCounterLitamaByYear(year);
                    String lastCode = PstMaterial.getCounterLitama(lastCounter);
                    String lastDigitSku = matBillDetail.getSku().substring(sku.length() - 7);
                    matBillDetail.setSku(codeSku + year + lastCode);
                    matBillDetail.setBarCode(matBillDetail.getSku());
                }  

                Material prevMaterial = PstMaterial.fetchExc(matBillDetail.getOID());
                PstMaterial.updateExc(matBillDetail);
                insertHistoryMaterial(userId, userName, Command.UPDATE, matBillDetail.getOID(), matBillDetail, prevMaterial);
            }
            
            //update data bill detail
            billDetailOrder.setUnitId(matBillDetail.getDefaultStockUnitId());
            billDetailOrder.setSku(matBillDetail.getSku());
            billDetailOrder.setItemName(matBillDetail.getName());
            PstBillDetail.updateExc(billDetailOrder);

        } catch (Exception e) {
            error += 1;
            System.out.println(e.getMessage());
        }

        return error;
    }
    //==========================================================================
	
	public static int prosesProduksi(long idBillMainOrder, long idBillMainInvoice, MatDispatch matDispatch, MatDispatchItem matDispatchItem, Date tglProduksi) {
		return prosesProduksi(idBillMainOrder, idBillMainInvoice, matDispatch, matDispatchItem, tglProduksi,0,"");
	}
    public static int prosesProduksi(long idBillMainOrder, long idBillMainInvoice, MatDispatch matDispatch, MatDispatchItem matDispatchItem, Date tglProduksi, long userID, String namaUser) {
        int error = 0;
        try {
            //cek perubahan berat
            //error = updateHargaEmas(idBillMainOrder);
            
            //get data invoice
            String whereBillOrder = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = '" + idBillMainInvoice + "'";
            if (idBillMainInvoice == 0) {
                whereBillOrder = "" + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " IN ("
                    + " SELECT " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN
                    + " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = '" + idBillMainOrder + "'"
                    + ")";
            }
            whereBillOrder += ""
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED //2
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES //1
                + "";
            Vector<BillMain> listBillInvoice = PstBillMain.list(0, 0, whereBillOrder, null);
            for (BillMain bill : listBillInvoice) {
                //cek ada emas lantakan atau tidak
                String where = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = " + bill.getOID()
                        + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = " + Material.MATERIAL_TYPE_EMAS_LANTAKAN;
                Vector listEmasLantakan = listJoinBillMainDetailMaterial(0, 0, where, "");
                //if (true) {continue;}
                //cek tipe order
                if (bill.getIsService() == 0) { //item baru
                    if (listEmasLantakan.isEmpty()) {
                        error = transferLokasi(bill.getOID(), matDispatch, matDispatchItem, tglProduksi);
                    } else {
                        error = transferProduksi(bill.getOID(), matDispatch, matDispatchItem, tglProduksi);
                    }
                } else if (bill.getIsService() == 1) { //item servis
                    error = transferLokasi(bill.getOID(), matDispatch, matDispatchItem, tglProduksi);
                } else if (bill.getIsService() == 2) { //item modif
                    if (listEmasLantakan.isEmpty()) {
                        error = transferLokasi(bill.getOID(), matDispatch, matDispatchItem, tglProduksi);
                    } else {
                        error = transferProduksi(bill.getOID(), matDispatch, matDispatchItem, tglProduksi);
                    }
                }                                
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            error = 1;
        }
        return error;
    }

    public static double getHargaEmasBaru(long idMaterial, double beratItem) {
        double hargaBaru = 0;
        try {
            //------------------------------------------------------------------
            //cek nilai tukar emas
            Material material = new Material();
            if (PstMaterial.checkOID(idMaterial)) {
                material = PstMaterial.fetchExc(idMaterial);
            }
            long kadarId = material.getPosKadar();
            long warnaId = material.getPosColor();
            double nilaiTukarJual = 0;
            Vector<MaterialNilaiTukarEmas> nilaiTukarEmas = PstMaterialNilaiTukarEmas.list(0, 0, ""
                    + "" + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " = " + kadarId
                    + " AND " + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_COLOR_ID] + " = " + warnaId, "");
            if (!nilaiTukarEmas.isEmpty()) {
                nilaiTukarJual = nilaiTukarEmas.get(0).getLokal();
            }
            //------------------------------------------------------------------
            //cek harga jual emas
            double hargaJualEmasLantakan = getHargaJualEmasLantakanTerbaru();
            //------------------------------------------------------------------
            hargaBaru = (nilaiTukarJual / 100) * hargaJualEmasLantakan * beratItem;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return hargaBaru;
    }

    public static double getHargaJualEmasLantakanTerbaru() {        
        EmasLantakan emasLantakan = new EmasLantakan();
        try {
            emasLantakan = PstEmasLantakan.getEmasLantakan();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return emasLantakan.getHargaJual();
    }

    public static int transferLokasi(long idBillMain, MatDispatch matDispatch, MatDispatchItem dispatchItem, Date tglProduksi) {
        int status = 0;
        try {
            BillMain bm = PstBillMain.fetchExc(idBillMain);
            //set data dispatch
            Calendar c = Calendar.getInstance();
            c.setTime(tglProduksi);
            c.add(Calendar.MINUTE, -1);
            matDispatch.setDispatchDate(c.getTime());
            matDispatch.setLocationType(PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE);
            matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            matDispatch.setRemark("*Automatic transfer process for production. Invoice number : " + bm.getInvoiceNo());
            SessMatDispatch sessDispatch = new SessMatDispatch();
            int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
            matDispatch.setDispatchCodeCounter(maxCounter + 1);
            matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));
            matDispatch.setLast_update(new Date());
            matDispatch.setIdBillMainSalesOrder(idBillMain);
            long oidMatDispatch = PstMatDispatch.insertExc(matDispatch);
            
            if (oidMatDispatch == 0) {
                return 1;
            }
            
            //set data dispatch item
            Vector listBillDetailItem = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'", "");
            for (int i = 0; i < listBillDetailItem.size(); i++) {
                Billdetail bd = (Billdetail) listBillDetailItem.get(i);
                Material m = PstMaterial.fetchExc(bd.getMaterialId());
                MaterialDetail md = new MaterialDetail();
                long idMatDetail = PstMaterialDetail.checkOIDMaterialDetailId(m.getOID());
                if (idMatDetail > 0 && PstMaterialDetail.checkOID(idMatDetail)) {
                    md = PstMaterialDetail.fetchExc(idMatDetail);
                }
                //simpan dispatch item
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                matDispatchItem.setDispatchMaterialId(oidMatDispatch);
                matDispatchItem.setMaterialId(bd.getMaterialId());
                matDispatchItem.setUnitId(m.getDefaultStockUnitId());
                matDispatchItem.setGondolaId(dispatchItem.getGondolaId());
                matDispatchItem.setGondolaToId(dispatchItem.getGondolaToId());
                matDispatchItem.setQty(1);
                matDispatchItem.setResidueQty(matDispatchItem.getQty());
                //berat
                matDispatchItem.setBeratLast(bd.getBerat() + bd.getAdditionalWeight());
                matDispatchItem.setBeratCurrent(bd.getBerat() + bd.getAdditionalWeight());
                matDispatchItem.setBeratSelisih(0);
                //harga
                double hppAwal = 0;
                if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    hppAwal = m.getAveragePrice();
                } else if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    hppAwal = bd.getItemPrice() + bd.getLatestItemPrice();
                }
                matDispatchItem.setHppAwal(hppAwal);
                matDispatchItem.setHpp(hppAwal);
                //ongkos
                matDispatchItem.setOngkosAwal(md.getOngkos());
                matDispatchItem.setOngkos(md.getOngkos());
                //total
                matDispatchItem.setHppTotalAwal(matDispatchItem.getHppAwal() + matDispatchItem.getOngkosAwal());
                matDispatchItem.setHppTotal(matDispatchItem.getHpp() + matDispatchItem.getOngkos());
                long oidDispatchItem = PstMatDispatchItem.insertExc(matDispatchItem);
                
                if (oidDispatchItem == 0) {
                    return 1;
                }
                
                //update dispatch item type
                matDispatch.setDispatchItemType(m.getMaterialJenisType());
                PstMatDispatch.updateExc(matDispatch);
            }

            //jika status transfer langsung FINAL maka dibuatkan data penerimaan secara otomatis dan otomatis di posting
            //jika status transfer DRAFT maka untuk TRANSFER LOKASI, data penerimaan dibuat saat transfer di final secara manual dari menu transfer
            if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                //posting transfer lokasi
                SessPosting sessPosting = new SessPosting();
                sessPosting.postedDispatchDoc(oidMatDispatch);

                //pembuatan penerimaan otomatis
                MatReceive matReceive = new MatReceive();
                matReceive.setReceiveDate(matDispatch.getDispatchDate());
                matReceive.setReceiveFrom(matDispatch.getLocationId());
                matReceive.setLocationId(matDispatch.getDispatchTo());
                matReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH);
                matReceive.setDispatchMaterialId(matDispatch.getOID());
                matReceive.setRemark("*Automatic receive process for production. Transfer number : " + matDispatch.getDispatchCode());
                int docType = -1;
                try {
                    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                    docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, matReceive.getReceiveDate(), 0, docType, 0, true));
                matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                matReceive.setCurrencyId(1);
                matReceive.setIdBillMainSalesOrder(idBillMain);
                matReceive.setReceiveItemType(matDispatch.getDispatchItemType());
                long oidMatReceive = PstMatReceive.insertExc(matReceive);
                
                if (oidMatReceive == 0) {
                    return 1;
                }
                
                //pembuatan penerimaan item otomatis
                Vector listDispatchItem = PstMatDispatchItem.list(0, 0, matDispatch.getOID());
                if (listDispatchItem != null && listDispatchItem.size() > 0) {
                    for (int x = 0; x < listDispatchItem.size(); x++) {
                        Vector temp = (Vector) listDispatchItem.get(x);
                        MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
                        if (dfItem.getMaterialId() > 0 && PstMaterial.checkOID(dfItem.getMaterialId())) {
                            Material m = PstMaterial.fetchExc(dfItem.getMaterialId());
                            if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                                continue;
                            }
                        }
                        MatReceiveItem matReceiveItem = new MatReceiveItem();
                        matReceiveItem.setUnitId(dfItem.getUnitId());
                        matReceiveItem.setQty(dfItem.getQty());
                        matReceiveItem.setResidueQty(dfItem.getQty());
                        matReceiveItem.setBerat(dfItem.getBeratCurrent());
                        matReceiveItem.setMaterialId(dfItem.getMaterialId());
                        matReceiveItem.setReceiveMaterialId(oidMatReceive);
                        matReceiveItem.setCost(dfItem.getHpp());
                        matReceiveItem.setForwarderCost(dfItem.getOngkos());
                        // matReceiveItem.setCurrBuyingPrice(dfItem.getHpp());
                        matReceiveItem.setTotal(matReceiveItem.getQty() * matReceiveItem.getCost());
                        matReceiveItem.setCurrencyId(matReceive.getCurrencyId());
                        long oidReceiveItem = PstMatReceiveItem.insertExc(matReceiveItem);

                        if (oidReceiveItem == 0) {
                            return 1;
                        }

                        //untuk insert otomatis serial code dari transfer
                        if (oidReceiveItem != 0) {
                            Vector listSerialCode = new Vector();
                            listSerialCode = PstDispatchStockCode.checkListStockCodeDispatch(dfItem.getOID());
                            for (int sc = 0; sc < listSerialCode.size(); sc++) {
                                DispatchStockCode dispatchStockCode = (DispatchStockCode) listSerialCode.get(sc);
                                ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                                receiveStockCode.setReceiveMaterialItemId(oidReceiveItem);
                                receiveStockCode.setStockCode(dispatchStockCode.getStockCode());
                                receiveStockCode.setStockValue(dispatchStockCode.getStockValue());
                                receiveStockCode.setReceiveMaterialId(oidMatReceive);
                                try {
                                    PstReceiveStockCode.insertExc(receiveStockCode);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                }
                //posting penerimaan
                if (matReceive.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                    sessPosting = new SessPosting();
                    sessPosting.postedReceiveDoc(oidMatReceive, "", 0);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 1;
        }

        return status;
    }

	public static int transferProduksi(long idBillMain, MatDispatch matDispatch, MatDispatchItem dispatchItem, Date tglProduksi) {
		return transferProduksi(idBillMain, matDispatch, dispatchItem, tglProduksi, 0, "");
	}
	
    public static int transferProduksi(long idBillMain, MatDispatch matDispatch, MatDispatchItem dispatchItem, Date tglProduksi, long userId, String nameUser) {
        int status = 0;
        try {
            BillMain bm = PstBillMain.fetchExc(idBillMain);
            //insert data dispatch material
            //set new date -1 minute
            Calendar c = Calendar.getInstance();
            c.setTime(tglProduksi);
            c.add(Calendar.MINUTE, -1);
            matDispatch.setDispatchDate(c.getTime());
            matDispatch.setLocationType(PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR);
            matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            matDispatch.setRemark("*Automatic transfer lebur process for production. Invoice number : " + bm.getInvoiceNo());
            SessMatDispatch sessDispatch = new SessMatDispatch();
            int maxCounter = sessDispatch.getMaxDispatchUnitCounter(matDispatch.getDispatchDate(), matDispatch);
            matDispatch.setDispatchCodeCounter(maxCounter + 1);
            matDispatch.setDispatchCode(sessDispatch.generateDispatchUnitCode(matDispatch));
            matDispatch.setIdBillMainSalesOrder(idBillMain);
            long oidDispatchMaterial = PstMatDispatch.insertExc(matDispatch);
            
			Location locFrom = PstLocation.fetchExc(matDispatch.getLocationId());
			Location locTo = PstLocation.fetchExc(matDispatch.getDispatchTo());
			Ksg ksgFrom = PstKsg.fetchExc(dispatchItem.getGondolaId());
			Ksg ksgTo = PstKsg.fetchExc(dispatchItem.getGondolaToId());
			
			String detail = "Transfer dari ";
			
            if (oidDispatchMaterial == 0) {
                return 1;
            }

            //insert data receive material
            MatReceive matReceive = new MatReceive();
            matReceive.setOID(OIDFactory.generateOID());
            matReceive.setLocationId(matDispatch.getDispatchTo());
            matReceive.setLocationType(matDispatch.getLocationType());
            matReceive.setReceiveDate(matDispatch.getDispatchDate());
            matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH_UNIT);
            matReceive.setRemark("*Automatic receive process for production. Transfer lebur number : " + matDispatch.getDispatchCode());
            matReceive.setReceiveFrom(matDispatch.getLocationId());
            matReceive.setRecCode(matDispatch.getDispatchCode());
            matReceive.setRecCodeCnt(matDispatch.getDispatchCodeCounter());
            matReceive.setDispatchMaterialId(matDispatch.getOID());
            matReceive.setReceiveItemType(matDispatch.getDispatchItemType());
            matReceive.setIdBillMainSalesOrder(idBillMain);
            long oidReceiveMaterial = PstMatReceive.insertExc(matReceive);

            if (oidReceiveMaterial == 0) {
                return 1;
            }

            //looping for saving item target n source            
            Vector listBillDetailItem = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'", "");
            long groupId = OIDFactory.generateOID();
            //insert data dispatch receive item
            for (int i = 0; i < listBillDetailItem.size(); i++) {
                Billdetail bd = (Billdetail) listBillDetailItem.get(i);
                Material m = PstMaterial.fetchExc(bd.getMaterialId());
				MaterialDetail mDet = PstMaterialDetail.fetchExcMaterialDetailId(m.getOID());
                //insert data dispatch receive item
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                matDispatchReceiveItem.setDfRecGroupId(groupId);
                matDispatchReceiveItem.setDispatchMaterialId(oidDispatchMaterial);
                MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
                MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();

                //insert data dispacth item
                matDispatchItem.setDispatchMaterialId(oidDispatchMaterial);
                //insert data receive item
                matReceiveItem.setReceiveMaterialId(oidReceiveMaterial);

                if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                    matDispatchItem.setMaterialId(bd.getMaterialId());
                    matDispatchItem.setUnitId(m.getDefaultStockUnitId());
                    matDispatchItem.setQty(1);
                    matDispatchItem.setBeratCurrent(bd.getBerat());
                    matDispatchItem.setOngkos(bd.getCost());
                    //matDispatchItem.setHpp(bd.getItemPrice());
                    //matDispatchItem.setHppTotal(bd.getItemPrice());
                    matDispatchItem.setGondolaId(dispatchItem.getGondolaId());
                    matDispatchItem.setGondolaToId(dispatchItem.getGondolaToId());
                } else {
                    matReceiveItem.setMaterialId(bd.getMaterialId());
                    matReceiveItem.setUnitId(m.getDefaultStockUnitId());
                    matReceiveItem.setQty(1);
                    matReceiveItem.setBerat(bd.getBerat() + bd.getAdditionalWeight());
                    matReceiveItem.setForwarderCost(mDet.getOngkos());
                    double hpp = hpp = m.getAveragePrice();
                    /* update 21 November 2019 -> dimatikan karena request */
//                    if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
//                        hpp = m.getAveragePrice();
//                    } else if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
//                        hpp = bd.getItemPrice() + bd.getLatestItemPrice();
//                    }
                    /* end */
                    matReceiveItem.setCost(hpp);
                    matReceiveItem.setTotal(hpp + mDet.getOngkos());
					matReceiveItem.setRemark(m.getMaterialDescription());
                    
                    //update dispatch/receive item type
                    matDispatch.setDispatchItemType(m.getMaterialJenisType());
                    PstMatDispatch.updateExc(matDispatch);
                    matReceive.setReceiveItemType(m.getMaterialJenisType());
                    PstMatReceive.updateExc(matReceive);
                }
                long oidDispatchReceive = PstMatDispatchReceiveItem.insertExc(matDispatchReceiveItem);

                if (oidDispatchReceive == 0) {
                    return 1;
                }
            }

            //posting stok
            SessPosting sessPosting = new SessPosting();
            if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {                
                sessPosting.postedDispatchDoc(oidDispatchMaterial);
            }
            if (matReceive.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                sessPosting.postedReceiveDoc(oidReceiveMaterial, "", 0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return status;
    }
    //==========================================================================
    public static int updateStatusInvoicingBillMain(long idBillMainOrder, long idBillMainInvoice) {
        int error = 0;
        //get data invoice
        String whereBillOrder = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = '" + idBillMainInvoice + "'";
        if (idBillMainInvoice == 0) {
            whereBillOrder = "" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " IN ("
                    + " SELECT " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN
                    + " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = '" + idBillMainOrder + "'"
                    + ")";
        }
        whereBillOrder += ""
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED //2
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES //1
                + "";
        Vector<BillMain> listBillInvoice = PstBillMain.list(0, 0, whereBillOrder, null);
        //update status invoice
        for (BillMain bm : listBillInvoice) {
            try {
                bm.setStatusInv(PstBillMain.INVOICING_FINISH);
                bm.setBillDate(new Date());
                PstBillMain.updateExc(bm);
            } catch (com.dimata.posbo.db.DBException ex) {
                System.out.println(ex.getMessage());
                error = 1;
            }
        }

        //pecah DP
        double dp = getDownPayment(idBillMainOrder);
        if (dp > 0) {
            //get data invoice yg sudah selesai produksi
            whereBillOrder = ""
                    + "((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED //2
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_FINISH //2
                    + ") OR ("
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE //0
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES //1
                    + ")) AND " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = " + idBillMainOrder
                    + "";
            Vector<BillMain> listBillFinish = PstBillMain.list(0, 0, whereBillOrder, null);
            //get data dp yg sudah di bagi
            double dpDibagi = 0;
            for (BillMain bm : listBillFinish) {
                dpDibagi += getDownPayment(bm.getOID());
            }
            //cek sisa DP
            double sisaDp = dp - dpDibagi;
            if (sisaDp > 0) {
                for (BillMain bm : listBillInvoice) {
                    //get nilai bill
                    double nilaiBill = getTotalBillFinishOrder(idBillMainOrder, bm.getOID());
                    //createcredit payment
                    if (nilaiBill >= sisaDp) {
                        createCreditPayment(idBillMainOrder, idBillMainInvoice, sisaDp);
                    } else {
                        createCreditPayment(idBillMainOrder, idBillMainInvoice, nilaiBill);
                    }
                    sisaDp -= nilaiBill;
                    if (sisaDp <= 0) {
                        break;
                    }
                }
            }
        }
        return error;
    }

    public static int createCreditPayment(long idBillMainOrder, long idBillMainInvoice, double nilaiDp) {
        int error = 0;
        try {
            //duplicate data cash credit payment main
            long oidNewCreditPaymentMain = 0;
            Vector listCreditPaymentMain = PstCreditPaymentMain.list(0, 0, "" + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + idBillMainOrder + "'", "");
            long oidLastCreditPaymentMain = 0;
            if (!listCreditPaymentMain.isEmpty()) {
                CreditPaymentMain newCreditPaymentMain = (CreditPaymentMain) listCreditPaymentMain.get(0);
                oidLastCreditPaymentMain = newCreditPaymentMain.getOID();
                newCreditPaymentMain.setOID(0);
                newCreditPaymentMain.setBillMainId(idBillMainInvoice);
                newCreditPaymentMain.setPayAmountCredit(nilaiDp);
                oidNewCreditPaymentMain = PstCreditPaymentMain.insertExc(newCreditPaymentMain);
            }

            //duplicate data cash credit payment
            long oidNewCreditPayment = 0;
            Vector listCreditPayment = PstCashCreditPaymentDinamis.list(0, 0, "" + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_CREDIT_MAIN_ID] + " = '" + oidLastCreditPaymentMain + "'", "");
            long oidLastCreditPayment = 0;
            if (!listCreditPayment.isEmpty()) {
                CashCreditPaymentsDinamis newCashCreditPayment = (CashCreditPaymentsDinamis) listCreditPayment.get(0);
                oidLastCreditPayment = newCashCreditPayment.getOID();
                newCashCreditPayment.setOID(0);
                newCashCreditPayment.setCreditMainId(oidNewCreditPaymentMain);
                newCashCreditPayment.setAmount(nilaiDp);
                oidNewCreditPayment = PstCashCreditPaymentDinamis.insertExc(newCashCreditPayment);
            }

            //duplicate data cash credit payment info                
            Vector listCreditPaymentInfo = PstCashCreditPaymentInfo.list(0, 0, "" + PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID] + " = '" + oidLastCreditPayment + "'", "");
            if (!listCreditPaymentInfo.isEmpty()) {
                CashCreditPaymentInfo newCashCreditPaymentInfo = (CashCreditPaymentInfo) listCreditPaymentInfo.get(0);
                newCashCreditPaymentInfo.setOID(0);
                newCashCreditPaymentInfo.setPaymentId(oidNewCreditPayment);
                newCashCreditPaymentInfo.setAmount(nilaiDp);
                long oidNewCreditPaymentInfo = PstCashCreditPaymentInfo.insertExc(newCashCreditPaymentInfo);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            error += 1;
        }
        return error;
    }
	
	public static boolean isCanceled(long oidSalesOrder){
		boolean status = false;
		
		String where = PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID]+"="+oidSalesOrder;
		Vector listBillFinished = PstBillMain.list(0, 0, where, "");
		if (listBillFinished.size()>0){
			for (int i=0; i < listBillFinished.size(); i++){
				BillMain billMain = (BillMain) listBillFinished.get(i);
				if (billMain.getDocType() == PstBillMain.TYPE_INVOICE && billMain.getTransctionType() == PstBillMain.TRANS_TYPE_CASH
						&& billMain.getTransactionStatus() == PstBillMain.TRANS_STATUS_DELETED 
						&& billMain.getStatusInv() == PstBillMain.INVOICING_ON_PROSES){
					status = true;
				}
			}
		}
		
		return status;
	}

    //==========================================================================
    public static void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid, Material material, Material prevMaterial) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("warehouse/material/dispatch/src_produksi_emas.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(material.getLogDetail(prevMaterial));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
	
	public static void insertHistoryMaterialWithDetailAction(long userID, String nameUser, int cmd, long oid, Material material, String detail, String action) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("warehouse/material/dispatch/src_produksi_emas.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(action);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(detail);

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    public void insertHistoryTransfer(long userID, String nameUser, int cmd, long oid, MatDispatch matDispatch, MatDispatch prevMatDispatch) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("warehouse/material/dispatch/src_produksi_emas.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(matDispatch.getDispatchCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(matDispatch.getLogDetail(prevMatDispatch));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    public void insertHistoryPenerimaan(long userID, String nameUser, int cmd, long oid, MatReceive matReceive, MatReceive prevMatReceive) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("warehouse/material/dispatch/src_produksi_emas.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(matReceive.getRecCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(matReceive.getLogDetail(prevMatReceive));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
    //==========================================================================
    public static Vector listBuyBack(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + " SELECT "
                    + " bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + ", bm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
                    + ", bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + ", rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]
                    + ", IFNULL(mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+",newmat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+") AS " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + ", IFNULL(mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",newmat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+") AS "+ PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                    + ", rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]
                    + ", IFNULL(mat." + PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID]+",newmat." + PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID]+") AS "+ PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID]
                    + ", item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT_AWAL]
                    + ", item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT]
                    + ", rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_HEL]
                    + ", item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]
                    + ", item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]
                    + ", newmat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE]
                    + ", rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]
                    + ", IF(mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" IS NULL, '-', newmat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+" ) AS "+ PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]
                    + ", rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]
                    + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " AS rec "
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS item "
                    + " ON rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
                    + " = item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS bm "
                    + " ON rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_ID_BILL_MAIN_SALES_ORDER]
                    + " = bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " AS mat "
                    + " ON item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PREV_MATERIAL_ID]
                    + " = mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS newmat "
                    + " ON item." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = newmat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector list = new Vector();

                BillMain billMain = new BillMain();
                billMain.setInvoiceNumber(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                billMain.setBillDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                list.add(billMain);

                MatReceive matReceive = new MatReceive();
                matReceive.setReceiveDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
                matReceive.setRecCode(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                matReceive.setHel(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_HEL]));
                matReceive.setTransRate(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));
                list.add(matReceive);

                MatReceiveItem recItem = new MatReceiveItem();
                recItem.setBeratAwal(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT_AWAL]));
                recItem.setBerat(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT]));
                recItem.setDiscount(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]));
                recItem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
                recItem.setRemark(rs.getString(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_REMARK]));
                list.add(recItem);

                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setPosKadar(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID]));
                material.setMaterialJenisType(rs.getInt(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE]));
                list.add(material);

                Material newMaterial = new Material();
                newMaterial.setBarCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]));
                list.add(newMaterial);

                lists.add(list);
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
}
