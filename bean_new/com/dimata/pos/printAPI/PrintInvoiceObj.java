/*
 * PrintInvoiceObj.java
 *
 * Created on January 10, 2005, 9:51 AM
 */

package com.dimata.pos.printAPI;

/**
 *
 * @author  yogi
 */
import java.util.*;

public class PrintInvoiceObj {
    
    /** Holds value of property noInvoice. */
    private String noInvoice = "";
    
    /** Holds value of property noRefInvoice. */
    private String noRefInvoice;
    
    /** Holds value of property dateInvoice. */
    private Date dateInvoice;
    
    /** Holds value of property cashier. */
    private String cashier = "";
    
    /** Holds value of property listBillDetail. */
    private Vector listBillDetail = new Vector();
    
    /** Holds value of property discount. */
    private double discount = 0;
    
    /** Holds value of property tax. */
    private double tax = 0;
    
    /** Holds value of property service. */
    private double service = 0;
    
    /** Holds value of property memberBarcode. */
    private String memberBarcode = "";
    
    /** Holds value of property memberName. */
    private String memberName = "";
    
    /** Holds value of property listPayment. */
    private Vector listPayment = new Vector();
    
    /** Holds value of property listCurrencyName. */
    private Vector listCurrencyName = new Vector();
    
    /** Holds value of property totalReturn. */
    private double totalReturn = 0;
    
    /** Holds value of property listSerialNumber. */
    private Vector listSerialNumber;
    
    /** Holds value of property memberPoint. */
    private int memberPoint = 0;
    
    /** Holds value of property totalTransaction. */
    private double totalTransaction = 0;
    
    /** Holds value of property totalPaid. */
    private double totalPaid;
    
    /** Holds value of property subTotal. */
    private double subTotal = 0;
    
    /** Holds value of property prevPoint. */
    private double prevPoint = 0;
    
    /** Holds value of property totalPoint. */
    private double totalPoint = 0;
    
    /** Holds value of property balancePoint. */
    private double balancePoint = 0;
    
    /** Holds value of property totLastPayment. */
    private double totLastPayment = 0;
    
    /** Holds value of property totBalance. */
    private double totBalance = 0;
    
    /** Holds value of property totCardCost. */
    private double totCardCost = 0;
    
    /** Holds value of property totCardCostCredit. */
    private double totCardCostCredit = 0;
    
    /** Holds value of property totOtherCost. */
    private double totOtherCost =  0;
    
    /** Holds value of property listOtherCost */
    private Vector listOtherCost = new Vector();
    
    /** Creates a new instance of PrintInvoiceObj */
    public PrintInvoiceObj() {
    }
    
    /** Getter for property noInvoice.
     * @return Value of property noInvoice.
     *
     */
    public String getNoInvoice() {
        return this.noInvoice;
    }
    
    /** Setter for property noInvoice.
     * @param noInvoice New value of property noInvoice.
     *
     */
    public void setNoInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
    }
    
    /** Getter for property noRefInvoice.
     * @return Value of property noRefInvoice.
     *
     */
    public String getNoRefInvoice() {
        return this.noRefInvoice;
    }
    
    /** Setter for property noRefInvoice.
     * @param noRefInvoice New value of property noRefInvoice.
     *
     */
    public void setNoRefInvoice(String noRefInvoice) {
        this.noRefInvoice = noRefInvoice;
    }
    
    /** Getter for property dateInvoice.
     * @return Value of property dateInvoice.
     *
     */
    public Date getDateInvoice() {
        return this.dateInvoice;
    }
    
    /** Setter for property dateInvoice.
     * @param dateInvoice New value of property dateInvoice.
     *
     */
    public void setDateInvoice(Date dateInvoice) {
        this.dateInvoice = dateInvoice;
    }
    
    /** Getter for property cashier.
     * @return Value of property cashier.
     *
     */
    public String getCashier() {
        return this.cashier;
    }
    
    /** Setter for property cashier.
     * @param cashier New value of property cashier.
     *
     */
    public void setCashier(String cashier) {
        this.cashier = cashier;
    }
    
    /** Getter for property listBillDetail.
     * @return Value of property listBillDetail.
     *
     */
    public Vector getListBillDetail() {
        return this.listBillDetail;
    }
    
    /** Setter for property listBillDetail.
     * @param listBillDetail New value of property listBillDetail.
     *
     */
    public void setListBillDetail(Vector listBillDetail) {
        this.listBillDetail = listBillDetail;
    }
    
    /** Getter for property discount.
     * @return Value of property discount.
     *
     */
    public double getDiscount() {
        return this.discount;
    }
    
    /** Setter for property discount.
     * @param discount New value of property discount.
     *
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    /** Getter for property tax.
     * @return Value of property tax.
     *
     */
    public double getTax() {
        return this.tax;
    }
    
    /** Setter for property tax.
     * @param tax New value of property tax.
     *
     */
    public void setTax(double tax) {
        this.tax = tax;
    }
    
    /** Getter for property service.
     * @return Value of property service.
     *
     */
    public double getService() {
        return this.service;
    }
    
    /** Setter for property service.
     * @param service New value of property service.
     *
     */
    public void setService(double service) {
        this.service = service;
    }
    
    /** Getter for property memberBarcode.
     * @return Value of property memberBarcode.
     *
     */
    public String getMemberBarcode() {
        return this.memberBarcode;
    }
    
    /** Setter for property memberBarcode.
     * @param memberBarcode New value of property memberBarcode.
     *
     */
    public void setMemberBarcode(String memberBarcode) {
        this.memberBarcode = memberBarcode;
    }
    
    /** Getter for property memberName.
     * @return Value of property memberName.
     *
     */
    public String getMemberName() {
        return this.memberName;
    }
    
    /** Setter for property memberName.
     * @param memberName New value of property memberName.
     *
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    /** Getter for property listPayment.
     * @return Value of property listPayment.
     *
     */
    public Vector getListPayment() {
        return this.listPayment;
    }
    
    /** Setter for property listPayment.
     * @param listPayment New value of property listPayment.
     *
     */
    public void setListPayment(Vector listPayment) {
        this.listPayment = listPayment;
    }
    
    /** Getter for property listCurrencyName.
     * @return Value of property listCurrencyName.
     *
     */
    public Vector getListCurrencyName() {
        return this.listCurrencyName;
    }
    
    /** Setter for property listCurrencyName.
     * @param listCurrencyName New value of property listCurrencyName.
     *
     */
    public void setListCurrencyName(Vector listCurrencyName) {
        this.listCurrencyName = listCurrencyName;
    }
    
    /** Getter for property totalReturn.
     * @return Value of property totalReturn.
     *
     */
    public double getTotalReturn() {
        return this.totalReturn;
    }
    
    /** Setter for property totalReturn.
     * @param totalReturn New value of property totalReturn.
     *
     */
    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }
    
    /** Getter for property listSerialNumber.
     * @return Value of property listSerialNumber.
     *
     */
    public Vector getListSerialNumber() {
        return this.listSerialNumber;
    }
    
    /** Setter for property listSerialNumber.
     * @param listSerialNumber New value of property listSerialNumber.
     *
     */
    public void setListSerialNumber(Vector listSerialNumber) {
        this.listSerialNumber = listSerialNumber;
    }
    
    /** Getter for property memberPoint.
     * @return Value of property memberPoint.
     *
     */
    public int getMemberPoint() {
        return this.memberPoint;
    }
    
    /** Setter for property memberPoint.
     * @param memberPoint New value of property memberPoint.
     *
     */
    public void setMemberPoint(int memberPoint) {
        this.memberPoint = memberPoint;
    }
    
    /** Getter for property totalTransaction.
     * @return Value of property totalTransaction.
     *
     */
    public double getTotalTransaction() {
        return this.totalTransaction;
    }
    
    /** Setter for property totalTransaction.
     * @param totalTransaction New value of property totalTransaction.
     *
     */
    public void setTotalTransaction(double totalTransaction) {
        this.totalTransaction = totalTransaction;
    }
    
    /** Getter for property totalPaid.
     * @return Value of property totalPaid.
     *
     */
    public double getTotalPaid() {
        return this.totalPaid;
    }
    
    /** Setter for property totalPaid.
     * @param totalPaid New value of property totalPaid.
     *
     */
    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }
    
    /** Getter for property subTotal.
     * @return Value of property subTotal.
     *
     */
    public double getSubTotal() {
        return this.subTotal;
    }
    
    /** Setter for property subTotal.
     * @param subTotal New value of property subTotal.
     *
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
    
    /** Getter for property prevPoint.
     * @return Value of property prevPoint.
     *
     */
    public double getPrevPoint() {
        return this.prevPoint;
    }
    
    /** Setter for property prevPoint.
     * @param prevPoint New value of property prevPoint.
     *
     */
    public void setPrevPoint(double prevPoint) {
        this.prevPoint = prevPoint;
    }
    
    /** Getter for property totalPoint.
     * @return Value of property totalPoint.
     *
     */
    public double getTotalPoint() {
        return this.totalPoint;
    }
    
    /** Setter for property totalPoint.
     * @param totalPoint New value of property totalPoint.
     *
     */
    public void setTotalPoint(double totalPoint) {
        this.totalPoint = totalPoint;
    }
    
    /** Getter for property balancePoint.
     * @return Value of property balancePoint.
     *
     */
    public double getBalancePoint() {
        return this.balancePoint;
    }
    
    /** Setter for property balancePoint.
     * @param balancePoint New value of property balancePoint.
     *
     */
    public void setBalancePoint(double balancePoint) {
        this.balancePoint = balancePoint;
    }
    
    /** Getter for property totLastPayment.
     * @return Value of property totLastPayment.
     *
     */
    public double getTotLastPayment() {
        return this.totLastPayment;
    }
    
    /** Setter for property totLastPayment.
     * @param totLastPayment New value of property totLastPayment.
     *
     */
    public void setTotLastPayment(double totLastPayment) {
        this.totLastPayment = totLastPayment;
    }
    
    /** Getter for property totBalance.
     * @return Value of property totBalance.
     *
     */
    public double getTotBalance() {
        return this.totBalance;
    }
    
    /** Setter for property totBalance.
     * @param totBalance New value of property totBalance.
     *
     */
    public void setTotBalance(double totBalance) {
        this.totBalance = totBalance;
    }
    
    /**
     * Getter for property totCardCost.
     * @return Value of property totCardCost.
     */
    public double getTotCardCost() {
        return totCardCost;
    }
    
    /**
     * Setter for property totCardCost.
     * @param totCardCost New value of property totCardCost.
     */
    public void setTotCardCost(double totCardCost) {
        this.totCardCost = totCardCost;
    }
    
    /**
     * Getter for property totOtherCost.
     * @return Value of property totOtherCost.
     */
    public double getTotOtherCost() {
        return totOtherCost;
    }
    
    /**
     * Setter for property totOtherCost.
     * @param totOtherCost New value of property totOtherCost.
     */
    public void setTotOtherCost(double totOtherCost) {
        this.totOtherCost = totOtherCost;
    }
    
    /**
     * Getter for property listOtherCost.
     * @return Value of property listOtherCost.
     */
    public java.util.Vector getListOtherCost() {
        return listOtherCost;
    }
    
    /**
     * Setter for property listOtherCost.
     * @param listOtherCost New value of property listOtherCost.
     */
    public void setListOtherCost(java.util.Vector listOtherCost) {
        this.listOtherCost = listOtherCost;
    }
    
    /**
     * Getter for property totCardCostCredit.
     * @return Value of property totCardCostCredit.
     */
    public double getTotCardCostCredit() {
        return totCardCostCredit;
    }
    
    /**
     * Setter for property totCardCostCredit.
     * @param totCardCostCredit New value of property totCardCostCredit.
     */
    public void setTotCardCostCredit(double totCardCostCredit) {
        this.totCardCostCredit = totCardCostCredit;
    }
    
}
