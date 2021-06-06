/*
 * BalanceModel.java
 *
 * Created on January 5, 2005, 3:10 PM
 */

package com.dimata.pos.cashier;

import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstPendingOrder;
import com.dimata.pos.entity.payment.PstCashCreditPayment;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashCreditCard;
import com.dimata.pos.entity.payment.PstCashCreditPaymentInfo;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.pos.entity.payment.PstCreditPaymentMain;
import com.dimata.pos.entity.billing.PstOtherCost;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 * COMMENT: its gonna be big fat model for balance
 *          (hiks)
 *          its still mix model n controller (sorry)
 * @author  pulantara
 */
public class BalanceModel {
    
    
    // PRINTING SUPPORT ATRIBUTE
    
    /** title for the header (printing purpose) */
    private String title = "";
    /** date for printing purpose, set current date */
    private Date date = new Date();
    /** cashier name */
    private String cashierName = "";
    /** amount of opening balance */
    private double openingBalance = 0;
    /** transaction list */
    private Vector balanceDetail = new Vector();
    /** subtotal for each payment type */
    private Vector subTotal = new Vector();
    /** sales bruto */
    private double salesBruto = 0;
    /** change total */
    private double changeTotal = 0;
    /** return payment total */
    private double returnTotal = 0;
    /** sales Netto */
    private double salesNetto = 0;
    /** down payment */
    private double downPayment = 0;
    /** down payment used */
    private double dpUsed = 0;
    /** credit payment */
    private double creditPayment = 0;
    /** other cost */
    private double otherCost = 0;
    /** credit card cost */
    private double cardCost = 0;
    /** closing balance amount  */
    private double closingBalance = 0;
    
    /** total invoice */
    private int totalInvoice = 0;
    /** Qty sales */
    private double qtySales = 0;
    /** total return */
    private int totalReturn = 0;
    /** Qty return */
    private double qtyReturn = 0;
    
    // END OF PRINTING SUPPORT ATTRIBUTE
    
    // PROCESS SUPPORT ATTRIBUTE
    
    /** cash cashier ID :: very important key */
    private long cashCashierId;
    
    /** contains all opening balance items */
    private Vector openingDetail = new Vector();
    
    /** contains all retur type sale for current cash cashier ID */
    private Vector returnVector = new Vector();
    
    /** contains all down payments for current cash cashier ID */
    private Vector downPaymentVector = new Vector();
    
    /** contains all card cost for current cash cashier ID */
    private Vector cardCostVector = new Vector();
    
    /** contains all cradit payment for current cash cashier ID */
    private Vector creditPaymentVector = new Vector();
    
    /** contains balance items for closing */
    private Vector closingDetail = new Vector();
    
    // END OF PROCESS SUPPORT ATTRIBUTE
    
    
    /** Creates a new instance of BalanceModel */
    public BalanceModel() {
    }
    
    /** Create a new instance with param
     *
     * this constructor will set the CashCashierId with param value
     * and will calculate/init all field base on CashCashierId
     * @param long CashCashierId
     */
    public BalanceModel(long lCashCashierId){
        this.setCashCashierId(lCashCashierId);
        this.initAll();
    }
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /**
     * Getter for property date.
     * @return Value of property date.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Setter for property date.
     * @param date New value of property date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Getter for property cashierName.
     * @return Value of property cashierName.
     */
    public java.lang.String getCashierName() {
        return cashierName;
    }
    
    /**
     * Setter for property cashierName.
     * @param cashierName New value of property cashierName.
     */
    public void setCashierName(java.lang.String cashierName) {
        this.cashierName = cashierName;
    }
    
    /**
     * Getter for property openingBalance.
     * @return Value of property openingBalance.
     */
    public double getOpeningBalance() {
        return openingBalance;
    }
    
    /**
     * Setter for property openingBalance.
     * @param openingBalance New value of property openingBalance.
     */
    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }
    
    /**
     * Getter for property balanceDetail.
     * @return Value of property balanceDetail.
     */
    public Vector getBalanceDetail() {
        return balanceDetail;
    }
    
    /**
     * Setter for property balanceDetail.
     * @param balanceDetail New value of property balanceDetail.
     */
    public void setBalanceDetail(Vector balanceDetail) {
        this.balanceDetail = balanceDetail;
    }
    
    /**
     * Getter for property subTotal.
     * @return Value of property subTotal.
     */
    public Vector getSubTotal() {
        return subTotal;
    }
    
    /**
     * Setter for property subTotal.
     * @param subTotal New value of property subTotal.
     */
    public void setSubTotal(Vector subTotal) {
        this.subTotal = subTotal;
    }
    
    /**
     * Getter for property salesBruto.
     * @return Value of property salesBruto.
     */
    public double getSalesBruto() {
        return salesBruto;
    }
    
    /**
     * Setter for property salesBruto.
     * @param salesBruto New value of property salesBruto.
     */
    public void setSalesBruto(double salesBruto) {
        this.salesBruto = salesBruto;
    }
    
    /**
     * Getter for property returnTotal.
     * @return Value of property returnTotal.
     */
    public double getReturnTotal() {
        return returnTotal;
    }
    
    /**
     * Setter for property returnTotal.
     * @param returnTotal New value of property returnTotal.
     */
    public void setReturnTotal(double returnTotal) {
        this.returnTotal = returnTotal;
    }
    
    /**
     * Getter for property salesNetto.
     * @return Value of property salesNetto.
     */
    public double getSalesNetto() {
        return salesNetto;
    }
    
    /**
     * Setter for property salesNetto.
     * @param salesNetto New value of property salesNetto.
     */
    public void setSalesNetto(double salesNetto) {
        this.salesNetto = salesNetto;
    }
    
    /**
     * Getter for property closingBalance.
     * @return Value of property closingBalance.
     */
    public double getClosingBalance() {
        return closingBalance;
    }
    
    /**
     * Setter for property closingBalance.
     * @param closingBalance New value of property closingBalance.
     */
    public void setClosingBalance(double closingBalance) {
        this.closingBalance = closingBalance;
    }
    
    /**
     * Getter for property totalInvoice.
     * @return Value of property totalInvoice.
     */
    public int getTotalInvoice() {
        return totalInvoice;
    }
    
    /**
     * Setter for property totalInvoice.
     * @param totalInvoice New value of property totalInvoice.
     */
    public void setTotalInvoice(int totalInvoice) {
        this.totalInvoice = totalInvoice;
    }
    
    /**
     * Getter for property qtySales.
     * @return Value of property qtySales.
     */
    public double getQtySales() {
        return qtySales;
    }
    
    /**
     * Setter for property qtySales.
     * @param qtySales New value of property qtySales.
     */
    public void setQtySales(double qtySales) {
        this.qtySales = qtySales;
    }
    
    /**
     * Getter for property totalReturn.
     * @return Value of property totalReturn.
     */
    public int getTotalReturn() {
        return totalReturn;
    }
    
    /**
     * Setter for property totalReturn.
     * @param totalReturn New value of property totalReturn.
     */
    public void setTotalReturn(int totalReturn) {
        this.totalReturn = totalReturn;
    }
    
    /**
     * Getter for property qtyReturn.
     * @return Value of property qtyReturn.
     */
    public double getQtyReturn() {
        return qtyReturn;
    }
    
    /**
     * Setter for property qtyReturn.
     * @param qtyReturn New value of property qtyReturn.
     */
    public void setQtyReturn(double qtyReturn) {
        this.qtyReturn = qtyReturn;
    }
    
    /**
     * Getter for property cashCashierId.
     * @return Value of property cashCashierId.
     */
    public long getCashCashierId() {
        return cashCashierId;
    }
    
    /**
     * Setter for property cashCashierId.
     * @param cashCashierId New value of property cashCashierId.
     */
    public void setCashCashierId(long cashCashierId) {
        this.cashCashierId = cashCashierId;
    }
    
    /**
     * Getter for property returnVector.
     * @return Value of property returnVector.
     */
    public Vector getReturnVector() {
        return returnVector;
    }
    
    /**
     * Getter for property openingDetail.
     * @return Value of property openingDetail.
     */
    public Vector getOpeningDetail() {
        return openingDetail;
    }
    
    /**
     * Setter for property openingDetail.
     * @param openingDetail New value of property openingDetail.
     */
    public void setOpeningDetail(Vector openingDetail) {
        this.openingDetail = openingDetail;
    }
    
    /**
     * Setter for property returnVector.
     * @param returnVector New value of property returnVector.
     */
    public void setReturnVector(Vector returnVector) {
        this.returnVector = returnVector;
    }
    
    /**
     * Getter for property closingDetail.
     * @return Value of property closingDetail.
     */
    public Vector getClosingDetail() {
        return closingDetail;
    }
    
    /**
     * Setter for property closingDetail.
     * @param closingDetail New value of property closingDetail.
     */
    public void setClosingDetail(Vector closingDetail) {
        this.closingDetail = closingDetail;
    }
    
    /**
     * init cashierName attribute
     * I.S. : CashCashierId allready setted
     * F.S. : CashierName setted with value calculate from CashCashierId
     */
    private void initCashierName(){
        String stResult = "";
        DBResultSet dbrs = null;
        try{
            // sorry for hard coded (time is running fast)
            String sql = " SELECT USR."+PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]+
                         " FROM "+PstAppUser.TBL_APP_USER+" AS USR "+
                         " INNER JOIN "+PstCashCashier.TBL_CASH_CASHIER+" AS SES "+
                         " ON USR."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]+
                         " = SES."+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+  
                         " WHERE SES."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+ 
                         " = "+this.getCashCashierId();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if(rs.next()){
                stResult = rs.getString(1);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initCashierName: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        this.setCashierName(stResult);
    }
    
    /** init opening balance ]
     *  I.S. : CashCashierId allready setted
     *  F.S. : OpeningBalance setted with value calculate from CashCashierId
     *  this methode will also init vector openingDetail property
     *  and set closingDetail = openingDetail
     */
    private void initOpeningBalance(){
        double result = 0;
        Vector opening = new Vector();
        Vector closing = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT B."+PstBalance.fieldNames[PstBalance.FLD_BALANCE_VALUE]+
            " * C."+PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]+
            " , B."+PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID]+
            " , B."+PstBalance.fieldNames[PstBalance.FLD_BALANCE_VALUE]+
            " , CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstBalance.TBL_BALANCE+" AS B "+
            " INNER JOIN "+PstStandartRate.TBL_POS_STANDART_RATE+" AS C "+
            " ON B."+PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID]+
            " = C."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CT "+
            " ON C."+PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+
            " = CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " WHERE B."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND C."+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+
            " = "+PstStandartRate.ACTIVE;
            
            
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem item;
            BalanceItem item2;
            while(rs.next()){
                //
                item = new BalanceItem();
                item2 = new BalanceItem();
                item.setPaymentType(PstCashPayment.CASH);
                item.setTotalIDR(rs.getDouble(1));
                item.setCurrencyID(rs.getLong(2));
                item.setAmount(rs.getDouble(3));
                item.setCurrencyCode(rs.getString(4));
                item2.setPaymentType(PstCashPayment.CASH);
                item2.setTotalIDR(rs.getDouble(1));
                item2.setCurrencyID(rs.getLong(2));
                item2.setAmount(rs.getDouble(3));
                item2.setCurrencyCode(rs.getString(4));
                
                result = result + rs.getDouble(1);
                opening.add(item);
                closing.add(item2);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initBalanceDetail: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        this.setOpeningDetail(opening);
        this.setClosingDetail(closing);
        this.setOpeningBalance(result);
    }
    
    /** init vector balanceDetail
     *  I.S. : CashCashierId allready setted
     *  F.S. : balanceDetail setted with value calculate from CashCashierId
     *   its also update closingDetail
     */
    private void initBalanceDetail(){
        Vector result = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+
            " / P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+") "+
            ", C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS BM "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS C "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            " = C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " , "+PstCashCashier.TBL_CASH_CASHIER + " AS S "+
            " WHERE ((BM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = S."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+ ") " +
            " OR (BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " < S."+PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+
            " AND P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+
            " > S."+PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+
            " AND S."+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+")) "+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+PstBillMain.TYPE_INVOICE+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " GROUP BY P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            
            System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem objBalanceItem;
            while(rs.next()){
                objBalanceItem = new BalanceItem();
                
                objBalanceItem.setPaymentType(rs.getInt(1));
                objBalanceItem.setCurrencyID(rs.getLong(2));
                objBalanceItem.setAmount(rs.getDouble(3));
                objBalanceItem.setTotalIDR(rs.getDouble(4));
                objBalanceItem.setCurrencyCode(rs.getString(5));
                result.add(objBalanceItem);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initBalanceDetail: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        // update closingDetail
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(result);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.addClosingItemBy(group);
        }
        
        this.setBalanceDetail(result);
    }
    
    /** init attribute creditPayment and creditPaymentVector
     *  I.S. : CashCashierId allready setted
     *  F.S. : creditPayment setted with value calculate from CashCashierId
     *   its also update closingDetail
     */
    private void initCreditPayment(){
        double result = 0;
        Vector detail = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]+
            ", P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CURRENCY_ID]+
            ", SUM(P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+
            " / P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_RATE]+") "+
            ", SUM(P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+") "+
            ", C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstCashCreditPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS BM "+
            " ON P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
            " = BM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS C "+
            " ON P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CURRENCY_ID]+
            " = C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " WHERE BM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]+
            " < "+PstCashPayment.RETURN+
            " GROUP BY P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]+
            ", P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CURRENCY_ID]+
            ", C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            
            //System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem objBalanceItem;
            while(rs.next()){
                objBalanceItem = new BalanceItem();
                
                objBalanceItem.setPaymentType(rs.getInt(1));
                objBalanceItem.setCurrencyID(rs.getLong(2));
                objBalanceItem.setAmount(rs.getDouble(3));
                objBalanceItem.setTotalIDR(rs.getDouble(4));
                objBalanceItem.setCurrencyCode(rs.getString(5));
                detail.add(objBalanceItem);
                
                result = result + rs.getDouble(4);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initBalanceDetail: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        // update closingDetail
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(detail);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.addClosingItemBy(group);
        }
        
        this.setCreditPaymentVector(detail);
        this.setCreditPayment(result);
    }
    
    /** init attribute changeTotal
     *  I.S. : CashCashierId allready setted
     *  F.S. : changeTotal setted with value calculate from CashCashierId
     */
    private void initChangeTotal(){
        double result = 0;
        Vector detail = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT C." +PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+"/"+
            " C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]+")"+
            ", SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+") "+
            ", CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstCashReturn.TBL_RETURN+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = C."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CT "+
            " ON C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            " = CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " GROUP BY C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            
            System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem objBalanceItem;
            while(rs.next()){
                objBalanceItem = new BalanceItem();
                
                result = result + rs.getDouble(3);
                
                objBalanceItem.setPaymentType(PstCashPayment.CASH);
                objBalanceItem.setCurrencyID(rs.getLong(1));
                objBalanceItem.setAmount(rs.getDouble(2));
                objBalanceItem.setTotalIDR(rs.getDouble(3));
                objBalanceItem.setCurrencyCode(rs.getString(4));
                detail.add(objBalanceItem);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initChangeTotal: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        // update closingDetail
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(detail);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.subClosingItemBy(group);
        }
        
        this.setChangeTotal(result);
        
    }
    
    /** init attribute dpUsed
     *  I.S. : CashCashierId allready setted
     *  F.S. : dpUsed setted with value calculate from CashCashierId
     */
    private void initDpUsed(){
        double result = 0;
        Vector detail = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT C." +PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]+
            ", SUM(C."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_DOWN_PAYMENT]+"/"+
            " C."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_RATE]+")"+
            ", SUM(C."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_DOWN_PAYMENT]+") "+
            ", CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstPendingOrder.TBL_CASH_PENDING_ORDER+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_PENDING_ORDER_ID]+
            " = C."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_PENDING_ORDER_ID]+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CT "+
            " ON C."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]+
            " = CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " GROUP BY C."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]+
            ", CT."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            
            System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem objBalanceItem;
            while(rs.next()){
                objBalanceItem = new BalanceItem();
                
                result = result + rs.getDouble(3);
                
                objBalanceItem.setCurrencyID(rs.getLong(1));
                objBalanceItem.setAmount(rs.getDouble(2));
                objBalanceItem.setTotalIDR(rs.getDouble(3));
                objBalanceItem.setCurrencyCode(rs.getString(4));
                detail.add(objBalanceItem);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initDpUsed: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        // update closingDetail
        /*
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(detail);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.subClosingItemBy(group);
        }
         */
        this.setDpUsed(result);
        
    }
    
    /** init attribute downPayment and downPaymentVector
     *  I.S. : CashCashierId allready setted
     *  F.S. : downPayment setted with value calculate from CashCashierId
     *   its also update closingDetail
     */
    private void initDownPayment(){
        double result = 0;
        Vector detail = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]+
            " , SUM(DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_DOWN_PAYMENT]+
            " / DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_RATE]+") "+
            " , SUM(DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_DOWN_PAYMENT]+")"+
            " , C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstPendingOrder.TBL_CASH_PENDING_ORDER+" AS DP "+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS C "+
            " ON DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]+
            " = C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " WHERE DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " GROUP BY DP."+PstPendingOrder.fieldNames[PstPendingOrder.FLD_CURRENCY_ID]+
            " , C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            
            //System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem objBalanceItem;
            while(rs.next()){
                objBalanceItem = new BalanceItem();
                
                objBalanceItem.setPaymentType(PstCashPayment.CASH); // down payment must in cash
                objBalanceItem.setCurrencyID(rs.getLong(1));
                objBalanceItem.setAmount(rs.getDouble(2));
                objBalanceItem.setTotalIDR(rs.getDouble(3));
                objBalanceItem.setCurrencyCode(rs.getString(4));
                detail.add(objBalanceItem);
                
                result = result + rs.getDouble(3);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initBalanceDetail: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        // update closingDetail
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(detail);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.addClosingItemBy(group);
        }
        this.setDownPayment(result);
        this.setDownPaymentVector(detail);
    }
    
    
    /** init attribute downPayment and downPaymentVector
     *  I.S. : CashCashierId allready setted
     *  F.S. : downPayment setted with value calculate from CashCashierId
     *   its also update closingDetail
     */
    public void initOtherCost() {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = " SELECT SUM(OC."+PstOtherCost.fieldNames[PstOtherCost.FLD_AMOUNT]+")"+
                         " FROM "+PstOtherCost.TBL_CASH_OTHER_COST+ " AS OC "+
                         " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS MAIN "+
                         " ON OC."+PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]+
                         " = MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                         " WHERE MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
                         " = "+this.getCashCashierId();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of card cost: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        this.setOtherCost(result);
    }
    
    /** init attribute cardCost and cardCostVector
     *  I.S. : CashCashierId allready setted
     *  F.S. : cardCost setted with value calculate from CashCashierId
     *   its also update closingDetail
     */
    private void initCardCost(){
        double result = 0;
        Vector detail = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
                         ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CURRENCY_ID]+
                         ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_RATE]+
                         ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_AMOUNT]+
                         ", CUR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
                         " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+" AS MAIN "+
                         " INNER JOIN "+PstCashPayment.TBL_PAYMENT+" AS PAY "+
                         " ON MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
                         " = PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
                         " INNER JOIN "+PstCashCreditCard.TBL_CREDIT_CARD+" AS DTL "+
                         " ON PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]+
                         " = DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]+
                         " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CUR "+
                         " ON DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CURRENCY_ID]+
                         " = CUR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                         " WHERE MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
                         " = "+this.getCashCashierId()+
                         " UNION "+
                         " SELECT PAY."+PstCashPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]+
                         ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CURRENCY_ID]+
                         ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_RATE]+
                         ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_AMOUNT]+
                         ", CUR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
                         " FROM "+PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS MAIN "+
                         " INNER JOIN "+PstCashCreditPayment.TBL_PAYMENT+" AS PAY "+
                         " ON MAIN."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
                         " = PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
                         " INNER JOIN "+PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO+" AS DTL "+ 
                         " ON PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]+
                         " = DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]+
                         " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CUR "+
                         " ON DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CURRENCY_ID]+
                         " = CUR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
                         " WHERE MAIN."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID]+
                         " = "+this.getCashCashierId();  
            
            //System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem objBalanceItem;
            while(rs.next()){
                objBalanceItem = new BalanceItem();
                
                objBalanceItem.setPaymentType(rs.getInt(1)); // down payment must in cash
                objBalanceItem.setCurrencyID(rs.getLong(2));
                objBalanceItem.setAmount(rs.getDouble(4)/rs.getDouble(3));
                objBalanceItem.setTotalIDR(rs.getDouble(4));
                objBalanceItem.setCurrencyCode(rs.getString(5));
                detail.add(objBalanceItem);
                
                result = result + rs.getDouble(4);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initBalanceDetail: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        // update closingDetail
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(detail);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.addClosingItemBy(group);
        }
        this.setCardCost(result);
        this.setCardCostVector(detail);
    }
    
    
    /** init subTotal
     *  I.S. : vector balanceDetail allready setted/init
     *  F.S. : vector subTotal setted with value of each payment type
     *  @see BalanceModel.initBalanceDetail()
     */
    private void initSubTotal(){
        int size = this.getBalanceDetail().size();
        int currType = PstCashPayment.CASH;
        
        BalanceItem obj;
        double subTotal = 0;
        for(int i = 0; i < size; i++){
            obj = new BalanceItem();
            obj = (BalanceItem) this.getBalanceDetail().get(i);
            if(obj.getPaymentType()==currType){
                subTotal = subTotal + obj.getTotalIDR();
            }
            else{
                this.getSubTotal().add(currType, subTotal+"");
                subTotal = obj.getTotalIDR();
                currType++;
            }
        }
        this.getSubTotal().add(currType,subTotal+"");
    }
    
    /** init Sales Bruto
     *  I.S. = vector SubTotal allready setted/init
     *  F.S. = salesBruto setted with value calculated from sum of
     *         all values ini vector SubTotal
     *  @see BalanceModel.initSubTotal()
     */
    private void initSalesBruto(){
        double result = 0;
        int size = this.getSubTotal().size();
        for(int i = 0; i<size; i++){
            result = result + Double.parseDouble((String) this.getSubTotal().get(i));
        }
        this.setSalesBruto(result);
    }
    
    /** init Return
     *  I.S. : CashCashierId allready setted
     *  F.S. : ReturnTotal setted with value calculate from CashCashierId
     *  this methode will also init returnVector property and update closingDetail
     */
    private void initReturn(){
        double result = 0;
        Vector detail = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+
            " / P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+") "+
            ", C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS BM "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS C "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            " = C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+PstBillMain.TYPE_RETUR+
            " GROUP BY P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", C."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            BalanceItem item;
            while(rs.next()){
                item = new BalanceItem();
                item.setPaymentType(rs.getInt(1));
                item.setCurrencyID(rs.getLong(2));
                item.setAmount(rs.getDouble(3));
                item.setTotalIDR(rs.getDouble(4));
                item.setCurrencyCode(rs.getString(5));
                
                result = result + rs.getDouble(4);
                detail.add(item);
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initReturnTotal: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        // update closingDetail
        Vector temp = new Vector();
        Vector group = new Vector();
        temp = switchByTypePayment(detail);
        int size = temp.size();
        for(int i = 0; i < size; i++){
            group = (Vector) temp.get(i);
            this.subClosingItemBy(group);
        }
        
        this.setReturnVector(detail);
        this.setReturnTotal(result);
    }
    
    /** initSalesNetto
     *  I.S. : salesBruto and returnTotal already setted
     *  F.S. : salesNetto setted with salesBruto - returnTotal
     *  @see initSalesNetto, initReturnTotal
     */
    private void initSalesNetto(){
        this.setSalesNetto(this.getSalesBruto()-this.getChangeTotal()-this.getReturnTotal()-this.getOtherCost()+this.getCardCost());
    }
    
    /** initClosingBalance
     *  I.S. : salesNetto already setted
     *  F.S. : closingBalance = openingBalance + salesNetto
     */
    private void initClosingBalance(){
        this.setClosingBalance(this.getOpeningBalance()+
        this.getSalesNetto()+
        this.getDownPayment()+
        this.getCreditPayment()+
        this.getOtherCost());
        
    }
    
    /** initTotalInvoice
     *  I.S. : cashCashierId already setted
     *  F.S. : totalInvoice setted with total count of bill main record
     *         for current cashCashierId
     */
    private void initTotalInvoice(){
        int result = 0;
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT COUNT("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+") TOTAL" +
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+
            " WHERE "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+this.getCashCashierId()+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+" = "+PstBillMain.TYPE_INVOICE;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if(rs.next()){
                result = rs.getInt("TOTAL");
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initTotalInvoice: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        this.setTotalInvoice(result);
    }
    
    /** initQtySales
     *  I.S. : cashCashierId must already setted
     *  F.S. : QtySales setted with sum of qty of each transaction
     *          in bill main for current cashCashierId
     */
    private void initQtySales(){
        double result = 0;
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") QTY "+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+" BM "+
            " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL+" BD "+
            " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+PstBillMain.TYPE_INVOICE ;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if(rs.next()){
                result = rs.getDouble("QTY");
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initQtySales: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        this.setQtySales(result); 
    }
    
    /** initTotalReturn
     *  I.S. : cashCashierId must already setted
     *  F.S. : totalReturn setted with count of return for current cashCashierId
     */
    private void initTotalReturn(){
        int result = 0;
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT COUNT("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+") TOTAL" +
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+
            " WHERE "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+this.getCashCashierId()+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+" = "+PstBillMain.TYPE_RETUR;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if(rs.next()){
                result = rs.getInt("TOTAL");
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initTotalReturn: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        this.setTotalReturn(result);
    }
    
    /** initQtyReturn
     *  I.S. : cashCashierId must already setted
     *  F.S. : qtyReturn setted with sum of qty for each return for
     *          current cashCashierId
     */
    private void initQtyReturn(){
        double result = 0;
        DBResultSet dbrs = null;
        try{
            
            String sql = "SELECT SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") QTY "+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+" BM "+
            " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL+" BD "+
            " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
            " = "+this.getCashCashierId()+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " <> "+PstBillMain.TRANS_STATUS_DELETED+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+PstBillMain.TYPE_RETUR ;
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if(rs.next()){
                result = rs.getDouble("QTY");
            }
            
        }
        catch(Exception e){
            System.out.println("Error on BalanceModel::initQtyReturn: "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        this.setQtyReturn(result);
    }
    
    /** addClosingItemBy(Vector)
     *  add amount on items in closingDetail by items in param Vector
     *  by currency_id
     *  @param Vector
     */
    private void addClosingItemBy(Vector sales){
        int insize = sales.size();
        int size = closingDetail.size();
        boolean isUpdated;
        BalanceItem salesItem, closingItem;
        for(int i = 0; i < insize; i++){
            isUpdated = false;
            salesItem = new BalanceItem();
            salesItem = (BalanceItem) sales.get(i);
            for(int j = 0; j < size; j++){
                closingItem = new BalanceItem();
                closingItem = (BalanceItem) closingDetail.get(j);
                if(closingItem.getCurrencyID()==salesItem.getCurrencyID() && closingItem.getPaymentType()==salesItem.getPaymentType()){
                    closingItem.setAmount(closingItem.getAmount()+salesItem.getAmount());
                    closingItem.setTotalIDR(closingItem.getTotalIDR()+salesItem.getTotalIDR());
                    closingDetail.setElementAt(closingItem, j);
                    isUpdated = true;
                }
            }
            if(!isUpdated){
                closingDetail.add(salesItem);
            }
        }
    }
    
    /** addClosingItemBy(Vector)
     *  add amount on items in closingDetail by items in param Vector
     *  by currency_id
     *  @param Vector
     */
    private void subClosingItemBy(Vector sales){
        int insize = sales.size();
        int size = closingDetail.size();
        boolean isUpdated;
        BalanceItem salesItem, closingItem;
        for(int i = 0; i < insize; i++){
            isUpdated = false;
            salesItem = new BalanceItem();
            salesItem = (BalanceItem) sales.get(i);
            for(int j = 0; j < size; j++){
                closingItem = new BalanceItem();
                closingItem = (BalanceItem) closingDetail.get(j);
                if(closingItem.getCurrencyID()==salesItem.getCurrencyID() && closingItem.getPaymentType()==salesItem.getPaymentType()){
                    closingItem.setAmount(closingItem.getAmount()-salesItem.getAmount());
                    closingItem.setTotalIDR(closingItem.getTotalIDR()-salesItem.getTotalIDR());
                    closingDetail.setElementAt(closingItem, j);
                    isUpdated = true;
                }
            }
            if(!isUpdated){
                salesItem.setAmount(-salesItem.getAmount());
                salesItem.setTotalIDR(-salesItem.getTotalIDR());
                closingDetail.add(salesItem);
            }
        }
    }
    
    /** iniAll
     *  this method call all init methode
     *  DO NOT CHANGE THE ORDER
     */
    private void initAll(){
        this.initCashierName();
        this.initOpeningBalance();
        this.initBalanceDetail();
        this.initSubTotal();
        this.initSalesBruto();
        this.initChangeTotal();
        this.initReturn();
        this.initOtherCost();
        this.initSalesNetto();
        this.initCreditPayment();
        this.initDownPayment();
        //this.initDpUsed(); -not required
        this.initCardCost();
        this.initClosingBalance();
        this.initTotalInvoice();
        this.initQtySales();
        this.initTotalReturn();
        this.initQtyReturn();
    }
    
    /** testing purpose only methode
     *  remove it when not required...
     */
    private double testDetail(){
        BalanceItem obj = new BalanceItem();
        int size = this.getBalanceDetail().size();
        //System.out.println("size : "+size);
        //System.out.println("this.getBalanceDetail()>>>> : "+this.getBalanceDetail());
        obj = (BalanceItem) this.getBalanceDetail().get(0);
        return obj.getTotalIDR();
    }
    private double testClosingDetail(){
        BalanceItem obj = new BalanceItem();
        int size = this.getClosingDetail().size();
        //System.out.println("size : "+size);
        //System.out.println("this.getBalanceDetail()>>>> : "+this.getBalanceDetail());
        obj = (BalanceItem) this.getClosingDetail().get(0);
        return obj.getTotalIDR();
    }
    
    /** testing purpose only methode
     *  remove it when not required...
     */
    private double testSubTotal(){
        String st = (String) this.getSubTotal().get(0);
        return Double.parseDouble(st);
    }
    
    /** switch balance detail by type payment */
    public static Vector switchByTypePayment(Vector listBalanceDetail){
        Vector result = new Vector(1,1);
        if(listBalanceDetail!=null&&listBalanceDetail.size()>0){
            boolean check = false;
            Vector tempCard = new Vector();
            Vector tempCash = new Vector();
            Vector tempDebit = new Vector();
            Vector tempCheq = new Vector();
            for(int i=0;i<listBalanceDetail.size();i++){
                BalanceItem balanceItem = (BalanceItem)listBalanceDetail.get(i);
                switch(balanceItem.getPaymentType()){
                    case PstCashPayment.CARD :
                        tempCard.add(balanceItem);
                        break;
                    case PstCashPayment.CHEQUE :
                        tempCheq.add(balanceItem);
                        break;
                    case PstCashPayment.DEBIT :
                        tempDebit.add(balanceItem);
                        break;
                    default :
                        tempCash.add(balanceItem);
                        break;
                }
            }
            result.add(tempCash);
            result.add(tempCard);
            result.add(tempCheq);
            result.add(tempDebit);
        }
        return result;
    }
    
    
    
    /** testing purpose only methode
     *  remove it when not required...
     */
    public static void main(String[] argv){
        
        //BalanceModel test = new BalanceModel(Long.parseLong("504404244022073572"));
        
        BalanceModel test = new BalanceModel(Long.parseLong("504404263784641973"));
        Vector list = switchByTypePayment(test.getBalanceDetail());
        System.out.println(".........list : "+ list);
        
        
        System.out.println(".........ini nama kasir : "+ test.getCashierName());
        System.out.println(".........ini opening balance : "+ test.getOpeningBalance());
        //        System.out.println(".........ini Detail IDR : "+ test.testDetail());
        //        System.out.println(".........ini Subtotla : "+ test.testSubTotal());
        System.out.println(".........ini Sales Bruto : "+ test.getSalesBruto());
        System.out.println(".........ini Return : "+ test.getReturnTotal());
        System.out.println(".........ini Sales Netto : "+ test.getSalesNetto());
        System.out.println(".........ini Credit Payment : "+test.getCreditPayment());
        System.out.println(".........ini Down Payment : "+ test.getDownPayment());
        System.out.println(".........ini Closing Balance : "+ test.getClosingBalance());
        System.out.println(".........ini Total Invoice : "+ test.getTotalInvoice());
        System.out.println(".........ini Qty Sales : "+ test.getQtySales());
        System.out.println(".........ini Total Return : "+ test.getTotalReturn());
        System.out.println(".........ini Qty Return : "+ test.getQtyReturn());
        System.out.println(".........ini jumlah closingDetail: "+test.getClosingDetail().size());
        //        System.out.println(".........ini Closing IDR : "+ test.testClosingDetail());
        
    }
    
    /**
     * Getter for property downPayment.
     * @return Value of property downPayment.
     */
    public double getDownPayment() {
        return downPayment;
    }
    
    /**
     * Setter for property downPayment.
     * @param downPayment New value of property downPayment.
     */
    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }
    
    /**
     * Getter for property downPaymentVector.
     * @return Value of property downPaymentVector.
     */
    public Vector getDownPaymentVector() {
        return downPaymentVector;
    }
    
    /**
     * Setter for property downPaymentVector.
     * @param downPaymentVector New value of property downPaymentVector.
     */
    public void setDownPaymentVector(Vector downPaymentVector) {
        this.downPaymentVector = downPaymentVector;
    }
    
    /**
     * Getter for property creditPayment.
     * @return Value of property creditPayment.
     */
    public double getCreditPayment() {
        return creditPayment;
    }
    
    /**
     * Setter for property creditPayment.
     * @param creditPayment New value of property creditPayment.
     */
    public void setCreditPayment(double creditPayment) {
        this.creditPayment = creditPayment;
    }
    
    /**
     * Getter for property creditPaymentVector.
     * @return Value of property creditPaymentVector.
     */
    public Vector getCreditPaymentVector() {
        return creditPaymentVector;
    }
    
    /**
     * Setter for property creditPaymentVector.
     * @param creditPaymentVector New value of property creditPaymentVector.
     */
    public void setCreditPaymentVector(Vector creditPaymentVector) {
        this.creditPaymentVector = creditPaymentVector;
    }
    
    /**
     * Getter for property changeTotal.
     * @return Value of property changeTotal.
     */
    public double getChangeTotal() {
        return changeTotal;
    }
    
    /**
     * Setter for property changeTotal.
     * @param changeTotal New value of property changeTotal.
     */
    public void setChangeTotal(double changeTotal) {
        this.changeTotal = changeTotal;
    }
    
    /**
     * Getter for property dpUsed.
     * @return Value of property dpUsed.
     */
    public double getDpUsed() {
        return dpUsed;
    }
    
    /**
     * Setter for property dpUsed.
     * @param dpUsed New value of property dpUsed.
     */
    public void setDpUsed(double dpUsed) {
        this.dpUsed = dpUsed;
    }
    
    /**
     * Getter for property otherCost.
     * @return Value of property otherCost.
     */
    public double getOtherCost() {
        return otherCost;
    }
    
    /**
     * Setter for property otherCost.
     * @param otherCost New value of property otherCost.
     */
    public void setOtherCost(double otherCost) {
        this.otherCost = otherCost;
    }
    
    /**
     * Getter for property cardCost.
     * @return Value of property cardCost.
     */
    public double getCardCost() {
        return cardCost;
    }
    
    /**
     * Setter for property cardCost.
     * @param cardCost New value of property cardCost.
     */
    public void setCardCost(double cardCost) {
        this.otherCost = this.otherCost - this.cardCost + cardCost;
        this.cardCost = cardCost;
    }
    
    /**
     * Getter for property cardCostVector.
     * @return Value of property cardCostVector.
     */
    public java.util.Vector getCardCostVector() {
        return cardCostVector;
    }
    
    /**
     * Setter for property cardCostVector.
     * @param cardCostVector New value of property cardCostVector.
     */
    public void setCardCostVector(java.util.Vector cardCostVector) {
        this.cardCostVector = cardCostVector;
    }
    
}
