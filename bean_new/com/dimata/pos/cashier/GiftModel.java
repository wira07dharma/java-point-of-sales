/*
 * GiftModel.java
 *
 * Created on January 10, 2005, 10:11 AM
 */

package com.dimata.pos.cashier;
 
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.pos.entity.billing.BillDetailCode;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.session.billing.makeInvoiceNo;
import com.dimata.posbo.entity.masterdata.MemberReg;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  pulantara
 */
public class GiftModel
{
    
    // FORM AND PRINTING SUPPORT ATTRIBUTE
    
    /** current sales */
    private Sales sales = null;
    
    /*** current costumer served */
    private MemberReg customerServed = null; 
    
    /** available point */
    private double availablePoint = 0;
    /** requested point / point used */
    private double requestedPoint = 0;
    
    /** list of choosen gift */
    private Vector listGift = new Vector();
    /** current gift item add/edit */
    private GiftItem currentGiftItem = null;
    
    // BACKGROUND PROCESS SUPPORT ATTRIBUTE
    
    /** current BillDetail for current gift item */
    private Billdetail currentBillDetail = null;
    /** candidate for BillDetailCode */
    private BillDetailCode currentBillDetailCode = null;
    /** current gift item position on the list 
     *  value -1 mean there is no current gift item 
     */
    private int itemIndex = -1;
       
    /** bill main for this gift transaction */
    private BillMain giftTrans = new BillMain();
    
    /** vector contains all bill detail for this gift transaction */
    private Vector giftTransDetail = new Vector();
    
    /** vector contains all bill detail code for this gift transaction */
    private Hashtable gifTransDetailCode = new Hashtable();
    
    /** Creates a new instance of GiftModel */
    public GiftModel ()
    {
    }
    /** Creates a new instance of GiftModel with param */
    public GiftModel(long locationOID, int cashierNo){
        initGiftTrans(locationOID, cashierNo);
    }
    
    /** calculate all requested point from list 
     *  this will update requestedPoint 
     */
    private void calculateReqPoint(){
        int size = this.getListGift().size();
        double result = 0;
        GiftItem item;
        for(int i = 0; i < size; i++){
            item = new GiftItem();
            item = (GiftItem) this.getListGift().get(i);
            result = result + item.getPoint()*item.getAmount();
        }
        this.setRequestedPoint(result);
    }
    
    /** add a value (calculate form currentGiftItem) into requested point */
    private void addReqPoint(){
        double result = this.getRequestedPoint();
        result = result + currentGiftItem.getPoint()*currentGiftItem.getAmount();
        this.setRequestedPoint(result);
    }
    
    /** substract requested point with currentGitftItem value */
    private void subReqPoint(){
        double result = this.getRequestedPoint();
        result = result - currentGiftItem.getPoint()*currentGiftItem.getAmount();
        this.setRequestedPoint(result);
    }
    
    /** add a currentGiftItem into list
     *  currentGiftItem will setted null
     *  currentBillDetail will setted null
     *  currentBillDetailCode will setted null if any
     */
    public void addItemOnList(){
        try{
            this.getListGift().add(this.getCurrentGiftItem());
            this.getGiftTransDetail().add(this.getCurrentBillDetail());
            if(this.getCurrentBillDetailCode()!=null){
                this.getGifTransDetailCode().put(this.getCurrentBillDetail(),this.getCurrentBillDetailCode());
            }
            this.addReqPoint();
            this.setCurrentGiftItem(null);
            this.setCurrentBillDetail(null);
            this.setCurrentBillDetailCode(null);
            this.setItemIndex(-1);
        }
        catch(Exception e){
            System.out.println("Error on GiftModel.addItemOnList: "+e);
        }
    }
    
    /** append a currentGiftItem into list
     *  currentGiftItem will setted null
     *  currentBillDetail will setted null
     *  currentBillDetailCode will setted null if any
     */
    public void appendItemOnList(){
        try{
            GiftItem item =(GiftItem) this.getListGift().get(this.getItemIndex());
            Billdetail detail = (Billdetail) this.getGiftTransDetail().get(this.getItemIndex());
            item.setAmount(item.getAmount()+this.getCurrentGiftItem().getAmount());
            detail.setQty(detail.getQty()+this.getCurrentBillDetail().getQty());
            this.getListGift().set(this.getItemIndex(),item);
            this.getGiftTransDetail().set(this.getItemIndex(), detail);
            
            this.addReqPoint();
            this.setCurrentGiftItem(null);
            this.setCurrentBillDetail(null);
            this.setCurrentBillDetailCode(null);
            this.setItemIndex(-1);
        }
        catch(Exception e){
            System.out.println("Error on GiftModel.addItemOnList: "+e);
        }
    }
    
    /** get a giftItem at itemIndex from list into currentGiftItem
     */
    public void getItemOnList(){
        try{
            this.setCurrentGiftItem((GiftItem) this.getListGift().get(this.getItemIndex()));
            this.setCurrentBillDetail((Billdetail) this.getGiftTransDetail().get(this.getItemIndex()));
            this.setCurrentBillDetailCode((BillDetailCode) this.getGifTransDetailCode().get((Billdetail) this.getCurrentBillDetail()));
        }
        catch(Exception e){
            System.out.println("Error on GiftModel.getItemOnList: "+e);
            this.setCurrentGiftItem(null);
        }
    }
    
    /** set a giftItem at itemIndex on list 
     *  currentGiftItem will setted null
     */
    public void setItemOnList(){
        try{
            this.getListGift().set(this.getItemIndex(), this.getCurrentGiftItem());
            this.getGiftTransDetail().set(this.getItemIndex(), this.getCurrentBillDetail());
            if(this.getCurrentBillDetailCode()!=null){
                this.getGifTransDetailCode().put((Billdetail) this.getCurrentBillDetail(),this.getCurrentBillDetailCode());
            }
            this.calculateReqPoint();
            this.setCurrentGiftItem(null);
            this.setCurrentBillDetail(null);
            this.setCurrentBillDetailCode(null);
            this.setItemIndex(-1);
        }
        catch(Exception e)
        {
            System.out.println("Error on GiftModel.setItemOnList: "+e);
        }
    }
    
    /** remove a giftItem at itemIndex on list
     *  currentGiftItem will setted null
     */
    public void removeItemOnList(){
        try{
            this.subReqPoint();
            this.getListGift().removeElementAt(this.getItemIndex());
            this.getGifTransDetailCode().remove((Billdetail) this.getGiftTransDetail().remove(this.getItemIndex()));
            this.setCurrentGiftItem(null);
            this.setCurrentBillDetail(null);
            this.setCurrentBillDetailCode(null);
            this.setItemIndex(-1);
        }
        catch(Exception e){
            System.out.println("Error on GiftModel.removeItemOnList: "+e);
        }
    }
    
    /** getListGiftSize */
    public int getListGiftSize(){
        return this.getListGift().size();
    }
    
    /** update listMemberPoint 
     *  update each item on listMemberPoint so
     *  its satisfied the current state on
     *  the model
     *  NOTE: availablePoint must greater or equal
     *        to requestedPoint
     *  UNUSED REASON :: BUSINESS PROCESS CHANGED
     */
    /*
    public void updateMemberPoint(){
        int reqPoint = this.getRequestedPoint();
        MemberPoin poin;
        int i = 0;
        int size = this.getListMemberPoint().size();
        Vector list = this.getListMemberPoint();
        while((i < size) && (reqPoint>0)){
            poin = (MemberPoin) list.get(i);
            if((poin.getDebet()-poin.getCredit())>=reqPoint){
                poin.setCredit(poin.getCredit()+reqPoint);
                reqPoint = 0;
            }
            else
            {
                reqPoint = reqPoint - (poin.getDebet()-poin.getCredit());
                poin.setCredit(poin.getDebet());
                //System.out.println("ini nilai reqPoin ..."+reqPoint);
            }
            list.set(i, poin);
            i++;
        }
        this.setListMemberPoint(list);
    }
     */
    
    /** initGiftTrans(long, int)
     *  set init value for giftTrans
     *  @param long locationOID, int cashierNo
     */
    private void initGiftTrans(long locationOID, int cashierNo){
        
            BillMain mainSale = new BillMain();
            mainSale.setInvoiceCounter(PstBillMain.getCounterTransaction(locationOID, cashierNo, PstBillMain.TYPE_GIFT));
            mainSale.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(),locationOID, cashierNo, PstBillMain.TYPE_GIFT));
            mainSale.setInvoiceNo(makeInvoiceNo.setInvoiceNumber());
            mainSale.setAppUserId (CashierMainApp.getCashCashier ().getAppUserId ());
            mainSale.setCashCashierId (CashierMainApp.getCashCashier ().getOID ());
            mainSale.setLocationId (locationOID);
            mainSale.setShiftId (CashierMainApp.getShift ().getOID ()); 
            mainSale.setDocType(PstBillMain.TYPE_GIFT);
            //mainSale.setTransType(PstBillMain.TYPE_GIFT);
            mainSale.setBillDate (new Date());     
            
            this.setGiftTrans(mainSale);
    }
    
    /** getIndexOfItem(GiftItem)
     *  return index of given item if any or
     *  return listGiftItem's size if not found
     */
    public int getIndexOfItem(GiftItem item){
        int i = 0;
        boolean found = false;
        GiftItem obj;
        int size = this.getListGiftSize();
        Vector list = this.getListGift();
        while(i < size && !found){
            obj = (GiftItem) list.get(i);
            //System.out.println(obj.getCode()+" = "+item.getCode());
            if(obj.getCode().equals(item.getCode())){
                found = true;
            }
            else{
                i++;
            }
        }
        return i;
    }
    
    /** getIndexOfItemWithCode(GiftItem)
     *  return index of given item if there's any or
     *  return listGiftItem's size if not found
     *  serial code compared too
     */
    public int getIndexOfItemWithCode(GiftItem item){
        int i = 0;
        boolean found = false;
        GiftItem obj;
        int size = this.getListGiftSize();
        Vector list = this.getListGift();
        while(i < size && !found){
            obj = (GiftItem) list.get(i);
            if(obj.getCode().equals(item.getCode()) && obj.getSerialCode().equals(item.getSerialCode())){
                found = true;
            }
            else{
                i++;
            }
        }
        return i;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args)
    {
        // TODO code application logic here
    }
    
        
    /**
     * Getter for property customerServed.
     * @return Value of property customerServed.
     */
    public MemberReg getCustomerServed()
    {
        if(customerServed==null){
            customerServed = new MemberReg();
        }
        return customerServed;
    }
    
    /**
     * Setter for property customerServed.
     * @param customerServed New value of property customerServed.
     */
    public void setCustomerServed(MemberReg customerServed)
    {
        this.customerServed = customerServed;
        this.setAvailablePoint (GiftController.countAvailablePointOf (customerServed.getOID()));  
    }      
       
    /**
     * Getter for property availablePoint.
     * @return Value of property availablePoint.
     */
    public double getAvailablePoint ()
    {
        return availablePoint; 
    }
    
    /**
     * Setter for property availablePoint.
     * @param availablePoint New value of property availablePoint.
     */
    public void setAvailablePoint (double availablePoint)
    {
        this.availablePoint = availablePoint;
    }
    
    /**
     * Getter for property requestedPoint.
     * @return Value of property requestedPoint.
     */
    public double getRequestedPoint ()
    {
        return requestedPoint; 
    }
    
    /**
     * Setter for property requestedPoint.
     * @param requestedPoint New value of property requestedPoint.
     */
    public void setRequestedPoint (double requestedPoint)
    {
        this.requestedPoint = requestedPoint;
    }
    
    /**
     * Getter for restPoint.
     * @return available point - requested point.
     */
    public double getRestPoint ()
    {
        return this.getAvailablePoint()-this.getRequestedPoint(); 
    }
    
     
    /**
     * Getter for property listGift.
     * @return Value of property listGift.
     */
    public Vector getListGift() {
        return listGift;
    }
    
    /**
     * Setter for property listGift.
     * @param listGift New value of property listGift.
     */
    public void setListGift(Vector listGift) {
        this.listGift = listGift;
    }
    
    /**
     * Getter for property currentGiftItem.
     * @return Value of property currentGiftItem.
     */
    public com.dimata.pos.cashier.GiftItem getCurrentGiftItem() {
        return currentGiftItem;
    }
    
    /**
     * Setter for property currentGiftItem.
     * @param currentGiftItem New value of property currentGiftItem.
     */
    public void setCurrentGiftItem(com.dimata.pos.cashier.GiftItem currentGiftItem) {
        this.currentGiftItem = currentGiftItem;
    }
    
    /**
     * Getter for property itemIndex.
     * @return Value of property itemIndex.
     */
    public int getItemIndex() {
        return itemIndex;
    }
    
    /**
     * Setter for property itemIndex.
     * @param itemIndex New value of property itemIndex.
     */
    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }
    
    /**
     * Getter for property sales.
     * @return Value of property sales.
     */
    public Sales getSales() {
        return sales;
    }
    
    /**
     * Setter for property sales.
     * @param sales New value of property sales.
     */
    public void setSales(Sales sales) {
        this.sales = sales;
    }
   
    /**
     * Getter for property giftTrans.
     * @return Value of property giftTrans.
     */
    public BillMain getGiftTrans() {
        return giftTrans;
    }
    
    /**
     * Setter for property giftTrans.
     * @param giftTrans New value of property giftTrans.
     */
    public void setGiftTrans(BillMain giftTrans) {
        this.giftTrans = giftTrans;
    }
    
    /**
     * Getter for property giftTransDetail.
     * @return Value of property giftTransDetail.
     */
    public Vector getGiftTransDetail() {
        return giftTransDetail;
    }
    
    /**
     * Setter for property giftTransDetail.
     * @param giftTransDetail New value of property giftTransDetail.
     */
    public void setGiftTransDetail(Vector giftTransDetail) {
        this.giftTransDetail = giftTransDetail;
    }
    
    /**
     * Getter for property gifTransDetailCode.
     * @return Value of property gifTransDetailCode.
     */
    public Hashtable getGifTransDetailCode() {
        return gifTransDetailCode;
    }
    
    /**
     * Setter for property gifTransDetailCode.
     * @param gifTransDetailCode New value of property gifTransDetailCode.
     */
    public void setGifTransDetailCode(Hashtable gifTransDetailCode) {
        this.gifTransDetailCode = gifTransDetailCode;
    }
    
   
    /**
     * Getter for property currentBillDetail.
     * @return Value of property currentBillDetail.
     */
    public Billdetail getCurrentBillDetail() {
        return currentBillDetail;
    }
    
    /**
     * Setter for property currentBillDetail.
     * @param currentBillDetail New value of property currentBillDetail.
     */
    public void setCurrentBillDetail(Billdetail currentBillDetail) {
        this.currentBillDetail = currentBillDetail;
    }
    
    /**
     * Getter for property currentBillDetailCode.
     * @return Value of property currentBillDetailCode.
     */
    public BillDetailCode getCurrentBillDetailCode() {
        return currentBillDetailCode;
    }
    
    /**
     * Setter for property currentBillDetailCode.
     * @param currentBillDetailCode New value of property currentBillDetailCode.
     */
    public void setCurrentBillDetailCode(BillDetailCode currentBillDetailCode) {
        this.currentBillDetailCode = currentBillDetailCode;
    }
    
    public boolean isAllValueCompleted(){
        if(!isAnyCustomer ()){
            return false;
        }
        if(!isAnyGiftItems ()){
            return false; 
        }
        return true;
    }
    public boolean isAnyCustomer(){
        if(this.getCustomerServed ().getOID ()>0){
            return true;
        }else{
            return false;
        }
        
    }
    public boolean isAnyGiftItems(){
        if(this.getListGift ().size ()>0){
            return true; 
        }else{
            return false; 
        }
    }
    
}
