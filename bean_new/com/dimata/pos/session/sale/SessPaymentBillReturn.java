/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.session.sale;

import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.CashPayments1;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashPayment1;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SessPaymentBillReturn {
    public static double getPaymentDinamisWithReturnTransaction(int limitStart, int recordToGet, String whereClause) {
        Vector lists = new Vector();
        double total =0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                    + " , CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + " , SUM(CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT] + ")" + "AS SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]
                    + " , SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")" + "AS SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]
                    + " FROM " + PstCashPayment1.TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " + PstCashReturn.TBL_RETURN + " CRP"
                    + " ON CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + whereClause
                    + //" AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
                    //" AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=1"
                    + //Add Transaction type and transaction status
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                    " GROUP BY " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE];

            //if(whereClause != null && whereClause.length() > 0)
            //sql = sql + " WHERE " + whereClause;

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
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                double totPayment = 0;
                double totPaymentReturn=0;
                CashPayments1 objCashPaymentsDinamis = new CashPayments1();
                CurrencyType currencyType = new CurrencyType();
                CashReturn cashReturn = new CashReturn();
                objCashPaymentsDinamis.setPaymentType(rs.getLong(1));
                objCashPaymentsDinamis.setRate(rs.getInt(4));
                objCashPaymentsDinamis.setAmount(rs.getDouble("SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                cashReturn.setAmount(rs.getDouble("SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]));
                
                totPayment=objCashPaymentsDinamis.getAmount();
                totPaymentReturn=cashReturn.getAmount();
                total=total+(totPayment-totPaymentReturn);
            }
            rs.close();
            return total;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }
}
