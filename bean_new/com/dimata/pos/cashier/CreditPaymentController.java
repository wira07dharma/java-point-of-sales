/*
 * CreditPaymentController.java
 *
 * Created on January 15, 2005, 2:20 PM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.*;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author  wpradnyana
 */
public class CreditPaymentController {
    
    /** Creates a new instance of CreditPaymentController */
    public CreditPaymentController() {
    }
    
    /**
     *  query optimized by wpulantara
     */
    public static Vector getCreditPaymentsOf(BillMain billMain){
        Vector result= new Vector();
        if(billMain.getOID()==0){
            billMain = PstBillMain.findByInvoiceNumber(billMain.getInvoiceNumber() );
        }
        if(billMain!=null){
            String sql = "SELECT CM.*"+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CURRENCY_ID]+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_STATUS]+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_DATETIME]+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]+
            " , CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_RATE]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NAME]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NUMBER]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_ACCOUNT_NAME]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_BANK]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_DUE_DATE]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CREDIT_PAYMENT_INFO_ID]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_BANK_NAME]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_CARD_NAME]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_EXPIRED_DATE]+
            " , CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_HOLDER_NAME]+
            " FROM "+PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS CM "+
            " INNER JOIN "+PstCashCreditPayment.TBL_PAYMENT+" AS CP "+
            " ON CM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
            " = CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
            " LEFT JOIN "+PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO+" AS CI "+
            " ON CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]+
            " = CI."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]+
            " WHERE CM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]+
            " = "+billMain.getOID();
                
            DBResultSet dbrs = null;
            result = new Vector();
            System.out.println("getCreditPaymentsOf() sql: "+sql);
            try{
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    Vector temp = new Vector(1,1);
                       
                    CreditPaymentMain main = new CreditPaymentMain();
                    PstCreditPaymentMain.resultToObject(rs, main);
                    temp.add(main);
                    
                    CashCreditPayments payment = new CashCreditPayments();
                    PstCashCreditPayment.resultToObject(rs, payment);
                    System.out.println(payment.getOID()+" - "+payment.getCreditMainId()+" - "+payment.getAmount());
                    temp.add(payment);
                    
                    CashCreditPaymentInfo paymentInfo =new CashCreditPaymentInfo();
                    PstCashCreditPaymentInfo.resultToObject(rs, paymentInfo);
                    temp.add(paymentInfo);
                    
                    result.add(temp);
                }
                
                
            }
            catch(Exception e){
                
                System.out.println("err di getCreditPaymentsOf() : "+e.toString());
                e.printStackTrace();
            }finally{
                DBResultSet.close(dbrs);
            }
        }
        return result;
    }
    
    /** get amount of credit payment per invoice 
     *  by wpulantara
     */
    public static double getAmountCreditPaymentsOf(BillMain billMain){
        double result= 0;
        if(billMain!=null){
            String sql = "SELECT SUM(CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+")"+
            " FROM "+PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS CM "+
            " INNER JOIN "+PstCashCreditPayment.TBL_PAYMENT+" AS CP "+
            " ON CM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
            " = CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
            " WHERE CM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]+
            " = "+billMain.getOID();
                
            DBResultSet dbrs = null;
            System.out.println("getAmountCreditPaymentsOf() sql: "+sql);
            try{
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                if(rs.next()){
                    result = rs.getDouble(1);
                }
            }
            catch(Exception e){
                System.out.println("err di getAmountCreditPaymentsOf() : "+e.toString());
            }finally{
                DBResultSet.close(dbrs);
            }
        }
        return result;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        BillMain billMain = new BillMain();
        billMain.setOID(Long.parseLong("504404264294551872"));
        
        Vector vctres = CreditPaymentController.getCreditPaymentsOf(billMain);
        Vector vctPayment = (Vector)vctres.get(0);
        Vector vctPaymentInfo = (Vector)vctres.get(1);
        Enumeration enumPay = vctPayment.elements();
        while(enumPay.hasMoreElements()){
            CashCreditPayments payment = (CashCreditPayments)enumPay.nextElement();
            System.out.println(payment.getAmount()+" AND "+payment.getPayDateTime());
        }
        
    }
    
}
