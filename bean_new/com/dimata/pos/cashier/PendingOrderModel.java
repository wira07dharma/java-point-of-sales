/*
 * PendingOrderModel.java
 *
 * Created on December 28, 2004, 6:59 PM
 */

package com.dimata.pos.cashier;

import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.billing.PendingOrder;
import com.dimata.pos.entity.billing.PstPendingOrder;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.common.entity.payment.StandartRate;

/**
 *
 * @author  wpradnyana
 */
public class PendingOrderModel
{
    
    /** Creates a new instance of PendingOrderModel */
    
    private PendingOrder pendingOrder;
    private MemberReg memberServed;
    private CashCashier cashCashier; 
    private CurrencyType currencyUsed ;
    private StandartRate rateUsed; 
    private String customerName="";
    private String customerAddress="";
    private String customerPhone="";
    public PendingOrderModel ()
    {
        this.getCashCashier ();
        this.getCurrencyUsed (); 
        this.getPendingOrder ().setCashierId (this.getCashCashier ().getOID());
        this.getPendingOrder().setPendingOrderStatus(PstPendingOrder.STATUS_OPEN);
        long locationId = CashierMainApp.getCashMaster ().getLocationId (); 
        int cashierNumber = CashierMainApp.getCashMaster ().getCashierNumber ();
        //this.getPendingOrder ().setPoNumber (PstPendingOrder.generateNumberInvoice (new Date(),locationId,cashierNumber,PstBillMain.TYPE_INVOICE));      
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args)
    {
        // TODO code application logic here
    }
    
    /**
     * Getter for property pendingOrder.
     * @return Value of property pendingOrder.
     */
    public PendingOrder getPendingOrder()
    {
        if(pendingOrder==null){
            pendingOrder = new PendingOrder(); 
        }
        return pendingOrder;
    }
    
    /**
     * Setter for property pendingOrder.
     * @param pendingOrder New value of property pendingOrder.
     */
    public void setPendingOrder(PendingOrder pendingOrder)
    {
        this.pendingOrder = pendingOrder;
    }
    
    /**
     * Getter for property memberServed.
     * @return Value of property memberServed.
     */
    public MemberReg getMemberServed()
    {
        if(memberServed==null){
            memberServed = new MemberReg(); 
        }
        return memberServed;
    }
    
    /**
     * Setter for property memberServed.
     * @param memberServed New value of property memberServed.
     */
    public void setMemberServed(MemberReg memberServed)
    {
        this.memberServed = memberServed;
        this.getPendingOrder ().setMemberId (memberServed.getOID ());
    }
    
    /**
     * Getter for property customerName.
     * @return Value of property customerName.
     */
    public java.lang.String getCustomerName ()
    {
        return customerName;
    }
    
    /**
     * Setter for property customerName.
     * @param customerName New value of property customerName.
     */
    public void setCustomerName (java.lang.String customerName)
    {
        this.customerName = customerName;
    }
    
    /**
     * Getter for property customerAddress.
     * @return Value of property customerAddress.
     */
    public java.lang.String getCustomerAddress ()
    {
        return customerAddress;
    }
    
    /**
     * Setter for property customerAddress.
     * @param customerAddress New value of property customerAddress.
     */
    public void setCustomerAddress (java.lang.String customerAddress)
    {
        this.customerAddress = customerAddress;
    }
    
    /**
     * Getter for property customerPhone.
     * @return Value of property customerPhone.
     */
    public java.lang.String getCustomerPhone ()
    {
        return customerPhone;
    }
    
    private String errorMsg = "";
    private int errNo=0; 
    /**
     * Setter for property customerPhone.
     * @param customerPhone New value of property customerPhone.
     */
    public void setCustomerPhone (java.lang.String customerPhone)
    {
        this.customerPhone = customerPhone;
    }
    public void synchronizeAllValues(){
        long membOId = 0;
        try{
            membOId = this.getMemberServed ().getOID ();  
        }catch(Exception e){
            
        }
        this.getPendingOrder ().setMemberId (membOId);
        
    }
    
    /**
     * Getter for property errorMsg.
     * @return Value of property errorMsg.
     */
    public java.lang.String getErrorMsg ()
    {
        return errorMsg;
    }
    
    /**
     * Setter for property errorMsg.
     * @param errorMsg New value of property errorMsg.
     */
    public void setErrorMsg (java.lang.String errorMsg)
    {
        this.errorMsg = errorMsg;
    }
    
    /**
     * Getter for property errNo.
     * @return Value of property errNo.
     */
    public int getErrNo ()
    {
        return errNo;
    }
    
    /**
     * Setter for property errNo.
     * @param errNo New value of property errNo.
     */
    public void setErrNo (int errNo)
    {
        this.errNo = errNo;
    }
    
    /**
     * Getter for property currencyUsed.
     * @return Value of property currencyUsed.
     */
    public CurrencyType getCurrencyUsed()
    {
        if(currencyUsed==null){
            currencyUsed = (CurrencyType) CashierMainApp.getHashCurrencyType().get(CashierMainApp.getDSJ_CashierXML().getConfig(0).defaultCurrencyCode);
            this.getPendingOrder ().setCurrencyId (currencyUsed.getOID ()); 
            this.getRateUsed (); 
        }
        return currencyUsed;
    }
    
    /**
     * Setter for property currencyUsed.
     * @param currencyUsed New value of property currencyUsed.
     */
    public void setCurrencyUsed(CurrencyType currencyUsed)
    {
        
        this.currencyUsed = currencyUsed;
        this.getPendingOrder ().setCurrencyId (currencyUsed.getOID ()); 
        this.getRateUsed (); 
    }
    
    /**
     * Getter for property rateUsed.
     * @return Value of property rateUsed.
     */
    public StandartRate getRateUsed()
    {
        if(rateUsed==null){
            rateUsed = new StandartRate();
            rateUsed = CashSaleController.getLatestRate (String.valueOf (this.getCurrencyUsed ().getOID()));
            this.getPendingOrder ().setRate (rateUsed.getSellingRate ()); 
        }
        return rateUsed;
    }
    
    /**
     * Setter for property rateUsed.
     * @param rateUsed New value of property rateUsed.
     */
    public void setRateUsed(StandartRate rateUsed)
    {
        this.rateUsed = rateUsed;
        this.getPendingOrder ().setRate (rateUsed.getSellingRate ()); 
    }
    
    /**
     * Getter for property cashCashier.
     * @return Value of property cashCashier.
     */
    public CashCashier getCashCashier()
    {
        if(cashCashier==null){
            cashCashier = CashierMainApp.getCashCashier ();
            this.getPendingOrder ().setCashierId (cashCashier.getOID ()); 
        }
        return cashCashier;
    }
    
    /**
     * Setter for property cashCashier.
     * @param cashCashier New value of property cashCashier.
     */
    public void setCashCashier(CashCashier cashCashier)
    {
        this.cashCashier = cashCashier;
    }
    
    public boolean isAllValuesCompleted(){
        if(!isAnySales ()){
            return false;
        }
        if(!isAnyCustomers ()){
            return false;
        }
        if(!isAnyTransaction ()){
            return false;
        }
        if(!isOrderDataCompleted ()){
            return false;
        }
        return true;
    }
    
    public boolean isAnySales(){
        if(this.getPendingOrder ().getSalesId ()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyTransaction(){
        if(this.getPendingOrder ().getCashierId ()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyCustomers(){
        if(this.getMemberServed ().getOID ()>0||(this.getCustomerName ()!=""&&this.getCustomerAddress ()!=""&&this.getCustomerPhone ()!="")){
            return true;
        }else{
            return false;
        }
        
    }
    public boolean isOrderDataCompleted(){
        boolean isBoxOrder= false;
        boolean isFinishDate = false;
        boolean isExpDate = false;
        boolean isPayment = false;
        if(this.getPendingOrder ().getOrderNumber ()!=""){
            isBoxOrder=true; 
        }
        //if(this.getPendingOrder ().getPlanTakenDate ().after (new Date())||this.getPendingOrder ().getPlanTakenDate ().equals (new Date())){
        if(this.getPendingOrder ().getPlanTakenDate ()!=null){
            isFinishDate = true;
        }
        //if(this.getPendingOrder ().getExpiredDate ().equals (new Date())||this.getPendingOrder ().getExpiredDate ().after (new Date())){ 
        if(this.getPendingOrder ().getExpiredDate ()!=null){ 
            isExpDate = true;
        }
        if(this.getPendingOrder ().getDownPayment ()>=0){
            isPayment = true; 
        }
        
        if(isBoxOrder&&isFinishDate&&isExpDate&&isPayment){
            return true; 
        }else{
            return false;
        }
            
        
    }
}
