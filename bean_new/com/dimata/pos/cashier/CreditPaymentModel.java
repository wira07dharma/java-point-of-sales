/*
 * CreditPaymentModel.java
 *
 * Created on January 14, 2005, 3:56 PM
 */

package com.dimata.pos.cashier;

import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.*;

import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  wpradnyana
 * edited by wpulantara
 */
public class CreditPaymentModel {
    
    
    private BillMain saleRef;
    private CreditPaymentMain mainPayment;
    
    Hashtable hashCurTypeKeyCode;
    Hashtable hashCurTypeKeyId;
    /*private Hashtable listLastPayment=new Hashtable();
    private Hashtable listLastPaymentInfo=new Hashtable();
    private Hashtable listNewPayment = new Hashtable();
    private Hashtable listNewPaymentInfo = new Hashtable();
     */
    private Vector listLastPayment=new Vector();
    private Vector listLastPaymentInfo=new Vector();
    private Vector listNewPayment = new Vector();
    private Vector listNewPaymentInfo = new Vector();
    
    private double lastTransTotal=0;
    
    private double currentPaymentTotal=0;
    private double lastPaymentTotal=0;
    private double paymentBalanceTotal=0;
    
    // change things, added by wpulantara
    private double change=0;
    private CashCreditPayments creditPaymentChange = null;
    
    // currency things, added by wpulantara
    CurrencyType currencyTypeUsed = null;
    StandartRate rateUsed = null;
    
    /** Creates a new instance of CreditPaymentModel */
    public CreditPaymentModel() {
        String curCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).defaultCurrencyCode;
        CurrencyType curType = (CurrencyType)CashierMainApp.getHashCurrencyType().get(curCode);
        this.setRateUsed(CashSaleController.getLatestRate(String.valueOf(curType.getOID())));
        this.setCurrencyTypeUsed(curType);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    /**
     * Getter for property saleRef.
     * @return Value of property saleRef.
     */
    public BillMain getSaleRef() {
        if(saleRef==null){
            saleRef = new BillMain();
        }
        return saleRef;
    }
    
    /**
     * Setter for property saleRef.
     * @param saleRef New value of property saleRef.
     */
    public void setSaleRef(BillMain saleRef) {
        
        this.saleRef = saleRef;
        this.getMainPayment().setBillMainId(saleRef.getOID());
        
    }
    
    /**
     * Getter for property lastTransTotal.
     * @return Value of property lastTransTotal.
     */
    public double getLastTransTotal() {
        return lastTransTotal;
    }
    
    /**
     * Setter for property lastTransTotal.
     * @param lastTransTotal New value of property lastTransTotal.
     */
    public void setLastTransTotal(double lastTransTotal) {
        this.lastTransTotal = lastTransTotal;
    }
    
    /**
     * Getter for property currentPaymentTotal.
     * @return Value of property currentPaymentTotal.
     */
    public double getCurrentPaymentTotal() {
        return currentPaymentTotal;
    }
    
    /**
     * Setter for property currentPaymentTotal.
     * @param currentPaymentTotal New value of property currentPaymentTotal.
     */
    public void setCurrentPaymentTotal(double currentPaymentTotal) {
        this.currentPaymentTotal = currentPaymentTotal;
    }
    
    /**
     * Getter for property paymentBalanceTotal.
     * @return Value of property paymentBalanceTotal.
     */
    public double getPaymentBalanceTotal() {
        return paymentBalanceTotal;
    }
    
    /**
     * Setter for property paymentBalanceTotal.
     * @param paymentBalanceTotal New value of property paymentBalanceTotal.
     */
    public void setPaymentBalanceTotal(double paymentBalanceTotal) {
        this.paymentBalanceTotal = paymentBalanceTotal;
    }
    
    public void synchronizeAllValues(){
        
        
        Enumeration enumNewPayments = this.getListNewPayment().elements();
        Enumeration enumNewPaymentInfo = this.getListNewPaymentInfo().elements();
        Enumeration enumLastPayments = this.getListLastPayment().elements();
        Enumeration enumLastPaymentInfo = this.getListLastPaymentInfo().elements();
        double totalLastPayment=0;
        double totalNewPayment=0;
        while(enumLastPayments.hasMoreElements()){
            CashCreditPayments payments = null;
            try {
                payments = (CashCreditPayments)enumLastPayments.nextElement();
            } catch (RuntimeException e) {
                
                e.printStackTrace();
            }
            
            double tempTotal = payments.getAmount();
            totalLastPayment = totalLastPayment + tempTotal;
            
        }
        this.setLastPaymentTotal(totalLastPayment);
        while(enumNewPayments.hasMoreElements()){
            CashCreditPayments payments = null;
            try {
                payments = (CashCreditPayments)enumNewPayments.nextElement();
            } catch (RuntimeException e) {
                
                e.printStackTrace();
            }
            
            double tempTotal = payments.getAmount();
            totalNewPayment = totalNewPayment+ tempTotal;
            
        }
        
        // edited by wpulantara for handling change
        this.setCurrentPaymentTotal(totalNewPayment);
        double newBalance = this.getLastTransTotal()-(totalLastPayment + totalNewPayment) ;
        double change = 0;
        if(newBalance<=0){
            // handled on Controller
            // this.getSaleRef().setTransactionStatus(PstBillMain.TRANS_STATUS_CLOSE);
            if(newBalance<0)
                change = - newBalance;
            newBalance = 0;
        }
        
        this.setChange(change);
        // add new negatif payment as cash credit payment change
        if(change>0){
            CashCreditPayments payChange = new CashCreditPayments();
            payChange.setCurrencyId(this.getCurrencyTypeUsed().getOID());
            payChange.setRate(this.getRateUsed().getSellingRate());
            payChange.setPaymentType(PstCashCreditPayment.CASH);
            payChange.setPayDateTime(new Date());
            payChange.setAmount(-change);
            this.setCreditPaymentChange(payChange);
        }
        else{
            this.setCreditPaymentChange(null);
        }
        
        this.setPaymentBalanceTotal(newBalance);
        
    }
    
    public void transferFromSale(DefaultSaleModel model){
        
        BillMain main = model.getMainSale();
        model.synchronizeAllValues();
        double lastNetTrans = model.getNetTrans();
        //double lastRetur = CashSaleController.getAmountAlreadyReturned(model.getMainSale().getOID());
        this.setLastTransTotal(lastNetTrans); 
        this.setSaleRef(main);
        Vector vctLastPayments = CreditPaymentController.getCreditPaymentsOf(main);
        Enumeration enumPay = vctLastPayments.elements();
        Vector vctP = new Vector();
        Vector vctInfo = new Vector();
        while(enumPay.hasMoreElements()){
            Vector  row = (Vector)enumPay.nextElement();
            CashCreditPayments payment = null;
            CashCreditPaymentInfo paymentInfo= null;
            try {
                payment = (CashCreditPayments )row.get(1);
                paymentInfo= (CashCreditPaymentInfo )row.get(2);
                vctP.add(payment);
                vctInfo.add(paymentInfo);
            } catch (RuntimeException e) {
                
                e.printStackTrace();
            }
        }
        this.setListLastPayment(vctP);
        this.setListLastPaymentInfo(vctInfo);
        synchronizeAllValues();
        
    }
    
    private int transType=0;
    
    //EROOOORRR, BELUM BERESSS!! CROTT
    //public CashCreditPayments findPaymentWith(CashCreditPayments creditPayment,CashCreditPaymentInfo creditPaymentInfo){
    public int findIndexPaymentWith(CashCreditPayments creditPayment,CashCreditPaymentInfo creditPaymentInfo){
        
        
        Enumeration enumPay = this.getListNewPayment().elements();
        Enumeration enumInfo = this.getListNewPaymentInfo().elements();
        int index=0;
        boolean found = false;
        while(enumPay.hasMoreElements()&&!found){
            CashCreditPayments temp = (CashCreditPayments)enumPay.nextElement();
            index++;
            if(creditPayment.getPayDateTime().compareTo(temp.getPayDateTime())==0){
                found = true;
            }
            
        }
        int returnint = index - 1;
        
        return returnint;
        
    }
    
    //public CashCreditPaymentInfo findPaymentInfoWith(CashCreditPayments creditPayment,CashCreditPaymentInfo creditPaymentInfo){
    public int findPaymentInfoWith(CashCreditPayments creditPayment,CashCreditPaymentInfo creditPaymentInfo){
        
        CashCreditPaymentInfo creditInfo = null;
        
        Enumeration enumPay = this.getListNewPayment().elements();
        Enumeration enumInfo = this.getListNewPaymentInfo().elements();
        int index = 0;
        boolean found = false;
        while(enumPay.hasMoreElements()&&!found){
            CashCreditPayments temp = null;
            CashCreditPaymentInfo tempInfo = null;
            try {
                temp = (CashCreditPayments)enumPay.nextElement();
                
                tempInfo = (CashCreditPaymentInfo)enumInfo.nextElement();
            } catch (RuntimeException e) {
                
                e.printStackTrace();
            }
            index++;
            if(creditPayment.getPayDateTime().compareTo(temp.getPayDateTime())==0){
                creditInfo = tempInfo;
                found = true;
            }
           
        }
        
        int returnint = index - 1;
        
        return returnint;
    }
    public CashCreditPayments insertPaymentWith(CashCreditPayments creditPayment,CashCreditPaymentInfo creditPaymentInfo){
        
        CashCreditPayments inserted = null;
        
        try {
            this.getListNewPayment().add(creditPayment);
            this.getListNewPaymentInfo().add(creditPaymentInfo);
        } catch (RuntimeException e) {
            
            e.printStackTrace();
        }
        inserted = creditPayment;
        
        return inserted ;
        
    }
    
    public CashCreditPayments deletePaymentWith(CashCreditPayments creditPayment,CashCreditPaymentInfo creditPaymentInfo){
        
        CashCreditPayments creditPayments = null;
        int indexPayment = findIndexPaymentWith(creditPayment, creditPaymentInfo);
        if(indexPayment>-1){
            try {
                this.getListNewPayment().remove(creditPayment);
                this.getListNewPaymentInfo().remove(creditPaymentInfo);
            } catch (RuntimeException e) {
                
                e.printStackTrace();
            }
        }
        return creditPayment;
        
    }
    
    /**
     * Getter for property mainSale.
     * @return Value of property mainSale.
     */
    public CreditPaymentMain getMainPayment() {
        long locationOID = CashierMainApp.getCashMaster().getLocationId();
        int cashierNo = CashierMainApp.getCashMaster().getCashierNumber();
        
        if(mainPayment==null){
            mainPayment = new CreditPaymentMain();
            
            try {
                mainPayment.setInvoiceCounter(PstCreditPaymentMain.getCounterTransaction(locationOID, cashierNo, PstCreditPaymentMain.TYPE_CREDIT_PAYMENT));
                mainPayment.setInvoiceNumber(PstCreditPaymentMain.generateNumberInvoice(new Date(),locationOID, cashierNo, PstCreditPaymentMain.TYPE_CREDIT_PAYMENT));
                
                mainPayment.setAppUserId(CashierMainApp.getCashCashier().getAppUserId());
                mainPayment.setCashCashierId(CashierMainApp.getCashCashier().getOID());
                mainPayment.setLocationId(locationOID);
                mainPayment.setShiftId(CashierMainApp.getShift().getOID());
                mainPayment.setBillDate(new Date());
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return mainPayment;
    }
    
    /**
     * Setter for property mainSale.
     * @param mainSale New value of property mainSale.
     */
    public void setMainPayment(CreditPaymentMain mainPayment) {
        this.mainPayment = mainPayment;
    }
    
    /**
     * Getter for property listLastPayment.
     * @return Value of property listLastPayment.
     */
    public Vector getListLastPayment() {
        if(listLastPayment==null){
            listLastPayment = new Vector();
        }
        return listLastPayment;
    }
    
    /**
     * Setter for property listLastPayment.
     * @param listLastPayment New value of property listLastPayment.
     */
    public void setListLastPayment(Vector listLastPayment) {
        this.listLastPayment = listLastPayment;
    }
    
    /**
     * Getter for property listLastPaymentInfo.
     * @return Value of property listLastPaymentInfo.
     */
    public Vector getListLastPaymentInfo() {
        if(listLastPaymentInfo==null){
            listLastPaymentInfo = new Vector();
        }
        return listLastPaymentInfo;
    }
    
    /**
     * Setter for property listLastPaymentInfo.
     * @param listLastPaymentInfo New value of property listLastPaymentInfo.
     */
    public void setListLastPaymentInfo(Vector listLastPaymentInfo) {
        this.listLastPaymentInfo = listLastPaymentInfo;
    }
    
    /**
     * Getter for property listNewPayment.
     * @return Value of property listNewPayment.
     */
    public Vector getListNewPayment() {
        if(listNewPayment==null){
            listNewPayment = new Vector();
        }
        return listNewPayment;
    }
    
    /**
     * Setter for property listNewPayment.
     * @param listNewPayment New value of property listNewPayment.
     */
    public void setListNewPayment(Vector listNewPayment) {
        this.listNewPayment = listNewPayment;
    }
    
    /**
     * Getter for property listNewPaymentInfo.
     * @return Value of property listNewPaymentInfo.
     */
    public Vector getListNewPaymentInfo() {
        if(listNewPaymentInfo==null){
            listNewPaymentInfo = new Vector();
        }
        return listNewPaymentInfo;
    }
    
    /**
     * Setter for property listNewPaymentInfo.
     * @param listNewPaymentInfo New value of property listNewPaymentInfo.
     */
    public void setListNewPaymentInfo(Vector listNewPaymentInfo) {
        this.listNewPaymentInfo = listNewPaymentInfo;
    }
    
    /**
     * Getter for property lastPaymentTotal.
     * @return Value of property lastPaymentTotal.
     */
    public double getLastPaymentTotal() {
        return lastPaymentTotal;
    }
    
    /**
     * Setter for property lastPaymentTotal.
     * @param lastPaymentTotal New value of property lastPaymentTotal.
     */
    public void setLastPaymentTotal(double lastPaymentTotal) {
        this.lastPaymentTotal = lastPaymentTotal;
    }
    
    /**
     * Getter for property transType.
     * @return Value of property transType.
     */
    public int getTransType() {
        return transType;
    }
    
    /**
     * Setter for property transType.
     * @param transType New value of property transType.
     */
    public void setTransType(int transType) {
        this.transType = transType;
        long locationOID = CashierMainApp.getCashMaster().getLocationId();
        int cashierNo = CashierMainApp.getCashMaster().getCashierNumber();
        this.getMainPayment();
        mainPayment.setDocType(transType);
        mainPayment.setInvoiceCounter(PstCreditPaymentMain.getCounterTransaction(locationOID, cashierNo, transType));
        mainPayment.setInvoiceNumber(PstCreditPaymentMain.generateNumberInvoice(new Date(),locationOID, cashierNo,transType));
        //mainPayment.setInvoiceNo(com.dimata.pos.session.billing.makeInvoiceNo.setInvoiceNumber());
        mainPayment.setAppUserId(CashierMainApp.getCashCashier().getAppUserId());
        mainPayment.setCashCashierId(CashierMainApp.getCashCashier().getOID());
        mainPayment.setLocationId(locationOID);
        mainPayment.setShiftId(CashierMainApp.getShift().getOID());
        mainPayment.setBillDate(new Date());
    }
    
    /**
     * Getter for property change.
     * @return Value of property change.
     */
    public double getChange() {
        return change;
    }
    
    /**
     * Setter for property change.
     * @param change New value of property change.
     */
    public void setChange(double change) {
        this.change = change;
    }
    
    /**
     * Getter for property currencyTypeUsed.
     * @return Value of property currencyTypeUsed.
     */
    public CurrencyType getCurrencyTypeUsed() {
        return currencyTypeUsed;
    }
    
    /**
     * Setter for property currencyTypeUsed.
     * @param currencyTypeUsed New value of property currencyTypeUsed.
     */
    public void setCurrencyTypeUsed(CurrencyType currencyTypeUsed) {
        this.currencyTypeUsed = currencyTypeUsed;
    }
    
    /**
     * Getter for property rateUsed.
     * @return Value of property rateUsed.
     */
    public StandartRate getRateUsed() {
        return rateUsed;
    }
    
    /**
     * Setter for property rateUsed.
     * @param rateUsed New value of property rateUsed.
     */
    public void setRateUsed(StandartRate rateUsed) {
        this.rateUsed = rateUsed;
    }
    
    /**
     * Getter for property creditPaymentChange.
     * @return Value of property creditPaymentChange.
     */
    public CashCreditPayments getCreditPaymentChange() {
        return creditPaymentChange;
    }
    
    /**
     * Setter for property creditPaymentChange.
     * @param creditPaymentChange New value of property creditPaymentChange.
     */
    public void setCreditPaymentChange(CashCreditPayments creditPaymentChange) {
        this.creditPaymentChange = creditPaymentChange;
    }
    
}
