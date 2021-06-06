/*
 * DefaultSaleModel.java
 *
 * Created on December 21, 2004, 4:21 PM
 */

package com.dimata.pos.cashier;

import com.dimata.ObjLink.BOCashier.CustomerLink;  
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.pos.entity.billing.*;

import com.dimata.pos.entity.payment.CashCreditPaymentInfo;
import com.dimata.pos.entity.payment.CashCreditPayments;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.session.billing.makeInvoiceNo;
import com.dimata.pos.session.processdata.SessTransactionData;
import com.dimata.posbo.entity.masterdata.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;



/**
 *
 * @author  wpradnyana
 */
public class DefaultSaleModel {
    
    /** Creates a new instance of DefaultSaleModel */
    public DefaultSaleModel() {
        try {
            this.getMainSale();
            String curCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).defaultCurrencyCode;
            CurrencyType curType = (CurrencyType)CashierMainApp.getHashCurrencyType().get(curCode);
            //this.setRateUsed(CashSaleController.getLatestRate (CashierMainApp.getDSJ_CashierXML ().getConfig(0).currencyId));
            //this.setCurrencyTypeUsed (CashSaleController.getCurrencyType (CashierMainApp.getDSJ_CashierXML ().getConfig (0).currencyId));
            this.setRateUsed(CashSaleController.getLatestRate(String.valueOf(curType.getOID())));
            this.setCurrencyTypeUsed(curType);
            
            //by default , set as cash sale
            this.getMainSale().setDocType(PstBillMain.TYPE_INVOICE);
        } catch (RuntimeException e) {
            
            e.printStackTrace();
        }
    }
    
    /**
     * Getter for property customerServed.
     * @return Value of property customerServed.
     */
    public MemberReg getCustomerServed() {
        return customerServed;
    }
    
    /**
     * Setter for property customerServed.
     * @param customerServed New value of property customerServed.
     */
    public void setCustomerServed(MemberReg customerServed) {
        this.getMainSale().setCustomerId(customerServed.getOID());
        this.customerServed = customerServed;
    }
    
    /**
     * Getter for property mainSale.
     * @return Value of property mainSale.
     */
    public BillMain getMainSale() {
        
        long locationOID = CashierMainApp.getCashMaster().getLocationId();
        int cashierNo = CashierMainApp.getCashMaster().getCashierNumber();
        
        if(mainSale ==null){
            mainSale = new BillMain();
            
            try {
                mainSale.setInvoiceCounter(PstBillMain.getCounterTransaction(locationOID, cashierNo, PstBillMain.TYPE_INVOICE));
                mainSale.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(),locationOID, cashierNo, PstBillMain.TYPE_INVOICE));
                mainSale.setInvoiceNo(makeInvoiceNo.setInvoiceNumber());
                mainSale.setAppUserId(CashierMainApp.getCashCashier().getAppUserId());
                mainSale.setCashCashierId(CashierMainApp.getCashCashier().getOID());
                mainSale.setLocationId(locationOID);
                mainSale.setShiftId(CashierMainApp.getShift().getOID());
                mainSale.setBillDate(new Date());
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        
        return mainSale;
    }
    
    public BillMain getMainSale(int iSaleType) {

        long locationOID = CashierMainApp.getCashMaster().getLocationId();
        int cashierNo = CashierMainApp.getCashMaster().getCashierNumber();

        if(mainSale ==null){
            mainSale = new BillMain();

            try {
                mainSale.setInvoiceCounter(PstBillMain.getCounterTransaction(locationOID, cashierNo,iSaleType ));
                mainSale.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(),locationOID, cashierNo, iSaleType));
                mainSale.setInvoiceNo(makeInvoiceNo.setInvoiceNumber());
                mainSale.setAppUserId(CashierMainApp.getCashCashier().getAppUserId());
                mainSale.setCashCashierId(CashierMainApp.getCashCashier().getOID());
                mainSale.setLocationId(locationOID);
                mainSale.setShiftId(CashierMainApp.getShift().getOID());
                mainSale.setBillDate(new Date());
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return mainSale;
    }

    /**
     * Setter for property mainSale.
     * @param mainSale New value of property mainSale.
     */
    public void setMainSale(BillMain mainSale) {
        this.mainSale = mainSale;
    }
    
    /**
     * Getter for property hashBillDetail.
     *
     *key untuk hash bill detail code adl <b>new String (sku )  "-" new String (serialCode) </b> dari material dan berpasangan dengan bill detail;
     *Terdiri dari kelas BillDetailCode
     * @return Value of property hashBillDetail.
     */
    public Hashtable getHashBillDetail() {
        return hashBillDetail;
    }
    
    /**
     * Setter for property hashBillDetail.
     * @param hashBillDetail New value of property hashBillDetail.
     */
    public void setHashBillDetail(Hashtable hashBillDetail) {
        this.hashBillDetail = hashBillDetail;
    }
    
    /**
     * Getter for property hashPersonalDisc.
     * @return Value of property hashPersonalDisc.
     */
    public Hashtable getHashPersonalDisc() {
        return hashPersonalDisc;
    }
    
    /**
     * Setter for property hashPersonalDisc.
     * @param hashPersonalDisc New value of property hashPersonalDisc.
     */
    public void setHashPersonalDisc(Hashtable hashPersonalDisc) {
        this.hashPersonalDisc = hashPersonalDisc;
    }
    
    /**
     * Getter for property cashPayments.
     * @return Value of property cashPayments.
     */
    public Hashtable getCashPayments() {
        return cashPayments;
    }
    
    /**
     * Setter for property cashPayments.
     * @param cashPayments New value of property cashPayments.
     */
    public void setCashPayments(Hashtable cashPayments) {
        this.cashPayments = cashPayments;
    }
    
    /**
     * Getter for property creditPayment.
     * @return Value of property creditPayment.
     */
    public Hashtable getCreditPayment() {
        return creditPayment;
    }
    
    /**
     * Setter for property creditPayment.
     * @param creditPayment New value of property creditPayment.
     */
    public void setCreditPayment(Hashtable creditPayment) {
        this.creditPayment = creditPayment;
    }
    
    /**
     * Getter for property totalTrans.
     * @return Value of property totalTrans.
     */
    public double getTotalTrans() {
        if(totalTrans==0){
            //this.synchronizeAllValues ();
            
        }
        return totalTrans;
    }
    
    /**
     * Setter for property totalTrans.
     * @param totalTrans New value of property totalTrans.
     */
    public void setTotalTrans(double totalTrans) {
        this.totalTrans = totalTrans;
    }
    
    private MemberReg customerServed = new MemberReg();
    private BillMain mainSale = new BillMain();
    /**
     *key untuk hash bill detail adl <b>new String (sku )  "-" new String (serialCode) </b> dari material;
     *Terdiri dari kelas Billdetail
     *
     */
    private Hashtable hashBillDetail = new Hashtable();
    /**
     *key untuk hash bill detail code adl <b>new String (sku )  "-" new String (serialCode) </b> dari material dan berpasangan dengan bill detail;
     *Terdiri dari kelas BillDetailCode
     */
    private Hashtable hashBillDetailCode = new Hashtable();
    /**
     *Vector yang digunakan untuk shorting item bill detail pada hashBillDetailCode
     */
    private Vector vectBillDetail = new Vector();
    /**
     * indeks digunakan untuk mengetahui item mana pada vectBillDetail yang sedang
     * atau terakhir diedit/delete
     */
    private int idxBillDetail = 0;
    /**
     *TIDAK DIMASUKKAN DALAM PROSES TRANSAKSI KE DATABASE, HANYA SEBAGAI ACUAN
     *key untuk hash personal discount adalah <b> new Long(material OID )</b>
     *Terdiri dari kelas PersonalDisc
     *
     */
    private Hashtable hashPersonalDisc = new Hashtable();
    /**
     *key untuk cash payments adalah <b> Date time()<b>, ambil dari pstCashPayments
     *Terdiri dari kelas CashPayments
     */
    private Hashtable cashPayments = new Hashtable();
    /**
     *key untuk cash payments Info adalah <b> Datetime  ()<b>, ambil dari pstCashPayments dan berpasangan dengan cash payments
     *Terdiri dari kelas CashCreditCard (menyimpan info tambahab untuk payments)
     */
    private Hashtable cashPaymentInfo = new Hashtable();
    /**
     *key untuk credit payments adalah <b> Date time payment </b>
     *Terdiri dari kelas CashCreditPayment
     */
    private Hashtable creditPayment = new Hashtable();
    /**
     *key untuk credit payments adalah <b> Date time payment </b>payment dan berpasangan dengan credit payment
     *Terdiri dari kelas CashCreditPaymentInfo
     */
    private Hashtable creditPaymentInfo = new Hashtable();
    /**
     *key untuk category untuk penghitungan point per category
     */
    private Hashtable categoryAmount = new Hashtable();
    
    /**
     *key untuk category untuk penghitungan qty per category
     */
    private Hashtable categoryQty = new Hashtable();
    
    /**
     * holds value of property cashReturn
     */
    private Hashtable cashReturn = new Hashtable();
    
    /**
     * digunakan pada transaksi retur,
     * berisi weight (bobot faktor ) untuk masing-masing item yang telah dijual
     *
     */
    private Hashtable weightPerSaleDetail = new Hashtable();
    
    /**
     *  digunakan untuk menyimpan other cost
     */
    private Hashtable hashOtherCost = new Hashtable();
    
    /** digunakan untuk mempercepat synchronized dengan menyimpan category yang sudah diperoleh */
    private Hashtable hashCategoryByMaterialId = new Hashtable();
        
    //private CashReturn cashReturn = new CashReturn();
    private Sales salesPerson = new Sales();
    private MemberPoin memberPoin = new MemberPoin();
    private StandartRate rateUsed = new StandartRate();
    private CurrencyType currencyTypeUsed = new CurrencyType();
    
    
    //private Member
    private double netTrans = 0.00;
    private double netCreditPay = 0.00;
    private double totalTrans = 0.00;
    private double lastPayment = 0.00;
    private double totOtherCost = 0.00;
    private double totCardCost = 0.00;
    
    private boolean isOpenBillPayment = false;
    
    public int transType=0;
    
    private CustomerLink customerLink = new CustomerLink();
    public void updateTotalTrans(){
        
    }
    
    /**
     * Getter for property memberPoin.
     * @return Value of property memberPoin.
     */
    public MemberPoin getMemberPoin() {
        return memberPoin;
    }
    
    /**
     * Setter for property memberPoin.
     * @param memberPoin New value of property memberPoin.
     */
    public void setMemberPoin(MemberPoin memberPoin) {
        this.memberPoin = memberPoin;
        //memberPoin.
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
        StandartRate curRate = CashSaleController.getLatestRate(String.valueOf(currencyTypeUsed.getOID()));
        this.setRateUsed(curRate);
    }
    
    /**
     * (hardly) fixed by wpulantara
     * comment :: its still not effisient coding but better
     */
    public void synchronizeAllValues(){
        
        Enumeration enumBillDetail = this.getHashBillDetail().elements();
        Enumeration enumBillDetailCode = this.getHashBillDetailCode().elements();
        Enumeration enumOtherCost = this.getHashOtherCost().elements();
        CurrencyType curType = this.getCurrencyTypeUsed();
        StandartRate curRate = this.getRateUsed();
        int size  = this.getHashBillDetail().size();
        double tempTotalTrans = 0;
        int discType = 0;
        double discVal = 0;
        double totalTrans = 0;
        double taxPct = 0;
        double taxVal = 0;
        double svcPct = 0;
        double svcVal = 0;
        
        MemberPoin newMemberPoin = null;
        int iPoin =0;
        this.getCategoryAmount().clear();
        this.getCategoryQty().clear();
        while(enumBillDetail.hasMoreElements()){
            
            Billdetail tempBillDetail = null;
            try {
                tempBillDetail = (Billdetail) enumBillDetail.nextElement();
            }catch(Exception e) {
                System.out.println("err onm synchronizeAllValue: "+e.toString());
            }
            
            BillDetailCode tempBillDetailCode = new BillDetailCode();
            
            try {
                tempBillDetailCode = (BillDetailCode) enumBillDetailCode.nextElement();
            }catch(Exception e) {
                System.out.println("err Sync: "+e.toString());
            }
            
            if(tempBillDetail!=null&&tempBillDetail.getUpdateStatus()!=PstBillDetail.UPDATE_STATUS_DELETED){
                long materialId = tempBillDetail.getMaterialId();
                Category category = new Category();
                try{
                    category = (Category) this.getHashCategoryByMaterialId().get(tempBillDetail.getMaterialId()+"");
                    if(category==null){
                        category = CashSaleController.getCategoryByMaterialId(tempBillDetail.getMaterialId());
                        this.getHashCategoryByMaterialId().put(tempBillDetail.getMaterialId()+"", category);
                    }
                }catch(Exception dbe){
                    System.out.println("err Sync: "+dbe.toString());
                    category = new Category();
                }
                
                double dPrice = tempBillDetail.getItemPrice();
                double dQty = tempBillDetail.getQty();
                double dDiscPct = tempBillDetail.getDiscPct();
                double dDiscValue = tempBillDetail.getDisc();
                double dTotal =0;
                double dDisc = 0;
                try {
                    
                    if(tempBillDetail.getDiscType()==PstBillDetail.DISC_TYPE_PERCENT){
                        dTotal = (dPrice*dQty);
                        System.out.println("discValue "+dTotal+" "+dPrice+" "+dQty);
                        dDiscValue = dTotal * dDiscPct/100;
                        dTotal = (dTotal-dDiscValue);
                        System.out.println("discValue "+dDiscValue+" "+dPrice+" "+dQty+" "+dTotal+" "+dDiscPct);
                        tempBillDetail.setDiscPct(0);
                    }else{
                        dTotal = (dPrice*dQty)-(dDiscValue);
                        System.out.println("discValue "+dDiscValue+" "+dPrice+" "+dQty+" "+dTotal+" "+dDiscPct);
                    }
                    
                    tempBillDetail.setDisc(dDiscValue);
                    tempBillDetail.setDiscType(PstBillDetail.DISC_TYPE_VALUE);
                }catch(Exception e) {
                    System.out.println("err onm synchronizeAllValue: "+e.toString());
                }
                
                //point calculation
                double poin = 0;
                tempBillDetail.setTotalPrice(dTotal) ;
                
                double dPrev = 0;
                double dQtyPrev = 0;
                double dPrcPrev = 0;
                String stUnit = getUnitName(tempBillDetail.getSku());
                try{
                    
                    String stPrevAmount = (String) this.getCategoryAmount().get(category.getOID()+"");
                    String stPrevQty = (String) this.getCategoryQty().get(stUnit+" "+category.getName()); 
                    if(stPrevAmount!=null){
                        dPrev = Double.parseDouble(stPrevAmount);
                    }
                    if(stPrevQty!=null){
                        StringTokenizer st = new StringTokenizer(stPrevQty,"/");
                        if(st.hasMoreTokens()){
                            dQtyPrev = Double.parseDouble(st.nextToken());
                        }
                        if(st.hasMoreTokens()){
                            dPrcPrev = Double.parseDouble(st.nextToken());
                        }
                    }
                }
                catch(Exception e){
                    System.out.println("err on point calc: "+e.toString());
                }
                
                double dCatTotal = dTotal + dPrev%category.getPointPrice();
                double dCatQtyTotal = dQty + dQtyPrev;
                double dCatPrcTotal = dTotal/this.getRateUsed().getSellingRate() + dPrcPrev;
                if(dCatTotal>=category.getPointPrice()&&category.getPointPrice()>1){
                    poin = dCatTotal/category.getPointPrice();
                }
                
                // case for return invoice
                if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR){
                    double amountAlreadyRetur = CashSaleController.getAmountCategoryAlreadyReturned(this.lastInvoice.getMainSale().getOID(), category.getOID());
                    String stOldAmount = (String) this.getLastInvoice().getCategoryAmount().get(category.getOID()+"");
                    double oldAmount = 0;
                    double prevAmount = 0;
                    double newAmount = 0;
                    if(stOldAmount!=null)
                        oldAmount = Double.parseDouble(stOldAmount);
                    prevAmount = oldAmount - amountAlreadyRetur - dPrev;
                    newAmount = prevAmount - dTotal;
                    System.out.println("old="+oldAmount+" prev="+prevAmount+" new="+newAmount);
                    if(category.getPointPrice()>0){
                        int prevPoint = (int) (prevAmount/category.getPointPrice());
                        int newPoint = (int) (newAmount/category.getPointPrice());
                        poin = newPoint-prevPoint;
                    }
                    else{
                        poin = 0;
                    }
                }
                
                // update category amount
                this.getCategoryAmount().put(category.getOID()+"", String.valueOf(dTotal+dPrev));
                this.getCategoryQty().put(stUnit+" "+category.getName(), dCatQtyTotal+"/"+dCatPrcTotal);
                
                iPoin = iPoin + (int) poin;
                memberPoin = new MemberPoin();
                
                try {
                    memberPoin.setCredit(iPoin);
                    this.setMemberPoin(memberPoin);
                }catch(Exception e) {
                    System.out.println("err on set point: "+e.toString());
                }
                
                
                Billdetail newBillDetail = new Billdetail();
                newBillDetail = tempBillDetail;
                
                /* untuk return sale*/
                if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR){
                    Double dblTemp = (Double)this.getWeightPerSaleDetail().get(newBillDetail.getSku()+"-"+tempBillDetailCode.getStockCode());
                    double saleDetailWeight = dblTemp.doubleValue();
                    double tmpDiscVal = 0;
                    try {
                        tmpDiscVal = newBillDetail.getQty() * saleDetailWeight * this.getLastDiscountVal();
                        discVal = discVal + tmpDiscVal;
                        discType = PstBillMain.DISC_TYPE_VALUE;
                        taxPct = 0;
                    }
                    catch (RuntimeException e1) {
                        e1.printStackTrace();
                    }
                    
                    double tmpTaxVal = 0;
                    try {
                        tmpTaxVal = newBillDetail.getQty() * saleDetailWeight * this.getLastTaxVal();
                        taxVal = taxVal +  tmpTaxVal;
                        svcPct = 0;
                    }
                    catch (RuntimeException e2) {
                        
                        e2.printStackTrace();
                    }
                    
                    double tmpSvcVal = 0;
                    try {
                        tmpSvcVal = newBillDetail.getQty() * saleDetailWeight * this.getLastSvcVal();
                        svcVal = svcVal + tmpSvcVal;
                    }
                    catch (RuntimeException e3) {
                        System.out.println("err onm synchronizeAllValue: "+e3.toString());
                    }
                    
                    
                }
                
                tempTotalTrans = tempTotalTrans + newBillDetail.getTotalPrice();
                try {
                    this.getHashBillDetail().put(newBillDetail.getSku()+"-"+tempBillDetailCode.getStockCode(), newBillDetail);
                }
                catch (RuntimeException e1) {
                    
                    e1.printStackTrace();
                }
            }
        }
        
        tempTotalTrans = tempTotalTrans + this.getTotOtherCost();
        double dblRateUsed = getRateUsed().getSellingRate();
        //added by wpulantara for rounded things
        //--------------------------------
        
        if(dblRateUsed==1){
            double dRound = tempTotalTrans%dblRateUsed;
            if(dRound>0){
                if(dRound<tempTotalTrans)
                    tempTotalTrans = tempTotalTrans-dRound;
            }
        }
        //---------------------------------
        
        /*untuk cash sale*/
        if(this.getMainSale().getDocType()==PstBillMain.TYPE_INVOICE){
            discType = this.getMainSale().getDiscType();
            discVal = this.getMainSale().getDiscount();
            totalTrans = this.getTotalTrans();
            if(tempTotalTrans==totalTrans || totalTrans==0 || this.getTotOtherCost()>0){
                taxPct = this.getMainSale().getTaxPercentage();
                taxVal = this.getMainSale().getTaxValue();
                svcPct = this.getMainSale().getServicePct();
                svcVal = this.getMainSale().getServiceValue();
            }
            else{
                taxPct = this.getMainSale().getTaxPercentage();
                taxVal = (tempTotalTrans/totalTrans)*this.getMainSale().getTaxValue();
                svcPct = this.getMainSale().getServicePct();
                svcVal = (tempTotalTrans/totalTrans)*this.getMainSale().getServiceValue();
            }
        }
        
        this.getMainSale().setDiscType(discType);
        this.getMainSale().setDiscount(discVal);
        this.getMainSale().setTaxValue(taxVal);
        this.getMainSale().setServiceValue(svcVal);
        
        
        
        
        this.setTotalTrans(tempTotalTrans);
        calculateNetTransWith();
        //if(this.getLastPayment()>0){
        //this.setNetTrans (this.getNetTrans ()-this.getLastPayment ());
        //}
        this.setErrorMsg("Success");
        this.setErrNo(0);
        
    }
    
    private double lastDiscountVal=0;
    private double lastTaxVal=0;
    private double lastSvcVal=0;
    private double lastChange=0;
    
    public void countLastPayment(){
        
        Enumeration enumBillDetail = this.getHashBillDetail().elements();
        Enumeration enumBillDetailCode = this.getHashBillDetailCode().elements();
        CurrencyType curType = this.getCurrencyTypeUsed();
        StandartRate curRate = this.getRateUsed();
        int size = this.getHashBillDetail().size();
        double tempTotalTrans = 0;
        MemberPoin newMemberPoin = null;
        int iPoin =0;
        while(enumBillDetail.hasMoreElements()){
            
            Billdetail tempBillDetail = null;
            tempBillDetail = (Billdetail) enumBillDetail.nextElement();
            BillDetailCode tempBillDetailCode = null;
            tempBillDetailCode = (BillDetailCode) enumBillDetailCode.nextElement();
            if(tempBillDetail!=null&&tempBillDetail.getUpdateStatus()!=PstBillDetail.UPDATE_STATUS_DELETED){
                long materialId = tempBillDetail.getMaterialId();
                Material material = new Material();
                Category category = new Category();
                try{
                    material = PstMaterial.fetchExc(materialId);
                    category = PstCategory.fetchExc(material.getCategoryId());
                }catch(Exception dbe){
                    dbe.printStackTrace();
                }
                
                double dPrice = tempBillDetail.getItemPrice();
                double dQty = tempBillDetail.getQty();
                
                double dDiscPct = tempBillDetail.getDiscPct();
                double dDiscValue = tempBillDetail.getDisc();
                double dTotal =0;
                try {
                    if(tempBillDetail.getDiscType()==PstBillDetail.DISC_TYPE_PERCENT){
                        
                        dTotal = (dPrice*dQty)*((100-dDiscPct)/100);
                    }else{
                        dTotal = (dPrice*dQty)-dDiscValue;
                    }
                }catch(Exception e) {
                    System.out.println("err on countLastPayment: "+e.toString());
                }
                double poin = 0;
                tempBillDetail.setTotalPrice(dTotal) ;
                if(dTotal>category.getPointPrice()&&category.getPointPrice()>1){
                    
                    try{
                        poin = dTotal/category.getPointPrice();
                        
                        memberPoin = new MemberPoin();
                        iPoin = iPoin + (int) poin;
                        memberPoin.setCredit(iPoin);
                        this.setMemberPoin(memberPoin);
                    }catch(Exception e) {
                        System.out.println("err on countLastPayment: "+e.toString());
                    }
                    
                }
                Billdetail newBillDetail = new Billdetail();
                newBillDetail = tempBillDetail;
                tempTotalTrans = tempTotalTrans + newBillDetail.getTotalPrice();
                try {
                    this.getHashBillDetail().put(newBillDetail.getSku()+"-"+tempBillDetailCode.getStockCode(), newBillDetail);
                }catch(Exception e) {
                    System.out.println("err on countLastPayment: "+e.toString());
                }
            }
        }
        
        this.setLastPayment(tempTotalTrans);
        
        this.setErrorMsg("Success");
        this.setErrNo(0);
        
    }
    
    public double getPayDiff(){
        
        double returndouble = (getLastPayment() - getNetTrans());
        
        return returndouble;
        
    }
    public double getCandReturn(){
        
        double returndouble = (getTotalPayments() - getNetTrans());
        
        return returndouble;
    }
    public boolean insertPaymentWith(double amount,String currencyCode){
        return false;
    }
    public double getTotalPayments(){
        
        Vector values = new Vector( this.getCashPayments().values());
        int size = values.size();
        Vector table = new Vector();
        double allTotal = 0;
        for(int i=0;i<size;i++){
            try {
                double tempTotal = 0;
                CashPayments temp = (CashPayments)values.get(i);
                tempTotal = temp.getAmount() ;
                allTotal = allTotal + tempTotal;
            }catch(Exception e) {
                System.out.println("Err on totalPayment: "+e.toString());
            }
        }
        allTotal = allTotal + this.getLastPayment();
        
        return allTotal;
    }
    private int errNo=0;
    /**
     * Getter for property errorMsg.
     * @return Value of property errorMsg.
     */
    public java.lang.String getErrorMsg() {
        return errorMsg;
    }
    
    /**
     * Setter for property errorMsg.
     * @param errorMsg New value of property errorMsg.
     */
    public void setErrorMsg(java.lang.String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    /**
     * Getter for property errNo.
     * @return Value of property errNo.
     */
    public int getErrNo() {
        return errNo;
    }
    
    /**
     * Setter for property errNo.
     * @param errNo New value of property errNo.
     */
    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }
    
    /**
     * Getter for property cashPaymentInfo.
     * @return Value of property cashPaymentInfo.
     */
    public Hashtable getCashPaymentInfo() {
        return cashPaymentInfo;
    }
    
    /**
     * Setter for property cashPaymentInfo.
     * @param cashPaymentInfo New value of property cashPaymentInfo.
     */
    public void setCashPaymentInfo(Hashtable cashPaymentInfo) {
        this.cashPaymentInfo = cashPaymentInfo;
    }
    
    /**
     * Getter for property creditPaymentInfo.
     * @return Value of property creditPaymentInfo.
     */
    public Hashtable getCreditPaymentInfo() {
        return creditPaymentInfo;
    }
    
    /**
     * Setter for property creditPaymentInfo.
     * @param creditPaymentInfo New value of property creditPaymentInfo.
     */
    public void setCreditPaymentInfo(Hashtable creditPaymentInfo) {
        this.creditPaymentInfo = creditPaymentInfo;
    }
    
    /**
     * Getter for property hashBillDetailCode.
     * @return Value of property hashBillDetailCode.
     */
    public Hashtable getHashBillDetailCode() {
        return hashBillDetailCode;
    }
    
    /**
     * Setter for property hashBillDetailCode.
     * @param hashBillDetailCode New value of property hashBillDetailCode.
     */
    public void setHashBillDetailCode(Hashtable hashBillDetailCode) {
        this.hashBillDetailCode = hashBillDetailCode;
    }
    
    /**
     * Getter for property lastPayment.
     * @return Value of property lastPayment.
     */
    public double getLastPayment() {
        return lastPayment;
    }
    
    /**
     * Setter for property lastPayment.
     * @param lastPayment New value of property lastPayment.
     */
    public void setLastPayment(double lastPayment) {
        this.lastPayment = lastPayment;
    }
    
    /**
     * Getter for property cashReturn.
     * @return Value of property cashReturn.
     */
    public Hashtable getCashReturn() {
        if(cashReturn==null){
            cashReturn = new Hashtable();
        }
        return cashReturn;
    }
    
    /**
     * Setter for property cashReturn.
     * @param cashReturn New value of property cashReturn.
     */
    public void setCashReturn(Hashtable cashReturn) {
        this.cashReturn = cashReturn;
    }
    
    /**
     * Getter for property salesPerson.
     * @return Value of property salesPerson.
     */
    public Sales getSalesPerson() {
        
        return salesPerson;
    }
    
    /**
     * Setter for property salesPerson.
     * @param salesPerson New value of property salesPerson.
     */
    public void setSalesPerson(Sales salesPerson) {
        
        this.salesPerson = salesPerson;
        this.getMainSale().setSalesCode(salesPerson.getCode());
        
    }
    
    private String errorMsg="";
    
    
    
    /**
     * edited by wpulantara
     */
    public Billdetail findValidSaleDetailWith(String key){
        
        Billdetail saleDetail = null;
        try {
            Billdetail tempDetail = new Billdetail();
            tempDetail = (Billdetail)this.getHashBillDetail().get(key);
            saleDetail = new Billdetail();
            saleDetail.setAmountAvailForReturn(tempDetail.getAmountAvailForReturn());
            saleDetail.setBillDetailCode(tempDetail.getBillDetailCode());
            saleDetail.setBillMainId(tempDetail.getBillMainId());
            saleDetail.setCost(tempDetail.getCost());
            saleDetail.setDisc(tempDetail.getDisc());
            saleDetail.setDiscPct(tempDetail.getDiscPct());
            saleDetail.setDiscType(tempDetail.getDiscType());
            saleDetail.setHashBilldetailCode(tempDetail.getHashBilldetailCode());
            saleDetail.setItemName(tempDetail.getItemName());
            saleDetail.setItemPrice(tempDetail.getItemPrice());
            saleDetail.setMaterialId(tempDetail.getMaterialId());
            saleDetail.setMaterialType(tempDetail.getMaterialType());
            saleDetail.setOID(tempDetail.getOID());
            saleDetail.setQty(tempDetail.getQty());
            saleDetail.setSku(tempDetail.getSku());
            saleDetail.setTotalPrice(tempDetail.getTotalPrice());
            saleDetail.setUnitId(tempDetail.getUnitId());
            saleDetail.setUpdateStatus(tempDetail.getUpdateStatus());
        }catch(Exception e) {
            System.out.println("err on findValidSaleDetailWith: "+e.toString());
            saleDetail = null;
        }
        /*
        if(saleDetail!=null && saleDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_DELETED){
            saleDetail = null;
        }
         
         */
        return saleDetail;
    }
    
    
    
    public Billdetail updateSaleDetail(Billdetail argSaleDetail,BillDetailCode argSaleDetailCode){
        
        Billdetail saleDetail = null;
        Billdetail foundSaleDetail = new Billdetail();
        foundSaleDetail = this.findValidSaleDetailWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
        
        double oldQty = 0;
        if(foundSaleDetail!=null){
            oldQty = foundSaleDetail.getQty();
            if(argSaleDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_NONE){
                argSaleDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_INSERTED);
            }
            
        }
        boolean ok=false;
        if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR){
            System.out.println("MASUK KE RETUR UPDATE: ");
            Billdetail lastDetail = this.getLastInvoice().findValidSaleDetailWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
            double diff = oldQty + lastDetail.getAmountAvailForReturn()-argSaleDetail.getQty();
            System.out.println("nilai old="+oldQty+" lastQty="+lastDetail.getAmountAvailForReturn()+" argQty="+argSaleDetail.getQty());
            if(diff>=0){
                try{
                    lastDetail.setAmountAvailForReturn(diff);
                    this.getLastInvoice().updateSaleDetail(lastDetail,argSaleDetailCode);
                    ok=true;
                }catch(Exception e) {
                    
                }
            }else{
                JOptionPane.showMessageDialog(null,"This return is not sufficient","Item Returns",JOptionPane.ERROR_MESSAGE);
                ok=false;
            }
        }else{
            ok=true;
        }
        if(ok){
            try {
                this.insertSaleDetailCode(argSaleDetail, argSaleDetailCode);
                this.getHashBillDetail().put(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode(), argSaleDetail);
            }catch(Exception e) {
                
            }
        }
        if(this.getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN)
            this.setIsOpenBillPayment(false);
        return saleDetail;
    }
    
    public Billdetail insertSaleDetail(Billdetail argSaleDetail,BillDetailCode argSaleDetailCode){
        
        Billdetail saleDetail = null;
        Billdetail foundSaleDetail = this.findValidSaleDetailWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
        argSaleDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_INSERTED);
        
        boolean ok=false;
        if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR){
            Billdetail lastDetail = this.getLastInvoice().findValidSaleDetailWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
            double diff = lastDetail.getAmountAvailForReturn()-argSaleDetail.getQty();
            if(diff>=0){
                lastDetail.setAmountAvailForReturn(diff);
                this.getLastInvoice().updateSaleDetail(lastDetail,argSaleDetailCode);
                ok=true;
            }else{
                JOptionPane.showMessageDialog(null,"This return is not sufficient","Item Returns",JOptionPane.ERROR_MESSAGE);
                ok=false;
            }
        }else{
            ok=true;
        }
        
        if(ok){
            if(foundSaleDetail!=null){
                double tempTotal = foundSaleDetail.getQty()+argSaleDetail.getQty();
                if(argSaleDetailCode.getStockCode().length()>0 && !CashierMainApp.isUsingProductColor()){
                    tempTotal = 1;
                }
                foundSaleDetail.setQty(tempTotal);
                foundSaleDetail.setDisc(argSaleDetail.getDisc());
                foundSaleDetail.setItemPrice(argSaleDetail.getItemPrice());
                argSaleDetail= foundSaleDetail ;
                
                if(argSaleDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_NONE ){
                    argSaleDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_UPDATED);
                }
                else if(argSaleDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_DELETED){
                    if(argSaleDetail.getOID()>0)
                        argSaleDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_UPDATED);
                    else
                        argSaleDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_INSERTED);
                    this.vectBillDetail.add(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
                }
                
            }
            else{
                this.vectBillDetail.add(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
            }
            
            this.insertSaleDetailCode(argSaleDetail, argSaleDetailCode);
            this.getHashBillDetail().put(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode(), argSaleDetail);
        }
        if(this.getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN)
            this.setIsOpenBillPayment(false);
        return saleDetail;
    }
    
    public Billdetail removeSaleDetail(Billdetail argSaleDetail,BillDetailCode argSaleDetailCode){
        
        Billdetail saleDetail = null;
        Billdetail foundSaleDetail = this.findValidSaleDetailWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
        if(foundSaleDetail!=null){
            
            argSaleDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_DELETED);
            argSaleDetail.setQty(0);
            
            if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR){
                Billdetail lastDetail = this.getLastInvoice().findValidSaleDetailWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
                lastDetail.setAmountAvailForReturn(lastDetail.getQty());
                this.getLastInvoice().updateSaleDetail(lastDetail,argSaleDetailCode);
                
            }
            this.getHashBillDetail().put(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode(), argSaleDetail);
            this.removeSaleDetailCode(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
            this.getVectBillDetail().removeElementAt(this.getIdxBillDetail());
            System.out.println("INI SIZE "+this.getVectBillDetail().size());
        }
        if(this.getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN)
            this.setIsOpenBillPayment(false);
        return saleDetail;
    }
    
    public BillDetailCode findValidSaleDetailCodeWith(String key){
        
        BillDetailCode saleDetailCode = null;
        try {
            saleDetailCode = new BillDetailCode();
            saleDetailCode = (BillDetailCode)this.getHashBillDetailCode().get(key);
        }catch(Exception e) {
            System.out.println("err on findValidSaleDetailCodeWith: "+e.toString());
            saleDetailCode = null;
        }
        if(saleDetailCode!=null && saleDetailCode.getUpdateStatus()==PstBillDetailCode.UPDATE_STATUS_DELETED){
            saleDetailCode = null;
        }
        
        return saleDetailCode;
    }
    
    public BillDetailCode insertSaleDetailCode(Billdetail argSaleDetail, BillDetailCode argSaleDetailCode){
        
        BillDetailCode saleDetailCode = null;
        BillDetailCode foundSaleDetailCode = this.findValidSaleDetailCodeWith(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode());
        if(argSaleDetailCode!=null){
            argSaleDetailCode.setUpdateStatus(PstBillDetailCode.UPDATE_STATUS_INSERTED);
            if(foundSaleDetailCode!=null){
                if(argSaleDetailCode.getUpdateStatus()==PstBillDetailCode.UPDATE_STATUS_NONE){
                    argSaleDetailCode.setUpdateStatus(PstBillDetailCode.UPDATE_STATUS_UPDATED);
                }
            }
        }
        try {
            this.getHashBillDetailCode().put(argSaleDetail.getSku()+"-"+argSaleDetailCode.getStockCode(), argSaleDetailCode);
        }catch(Exception e) {
            System.out.println("err on insertSaleDetailCode: "+e.toString());
        }
        
        return saleDetailCode;
    }
    
    public BillDetailCode removeSaleDetailCode(String sku){
        
        BillDetailCode saleDetailCode = null;
        BillDetailCode foundSaleDetailCode = this.findValidSaleDetailCodeWith(sku);
        if(foundSaleDetailCode!=null){
            
            foundSaleDetailCode.setUpdateStatus(PstBillDetailCode.UPDATE_STATUS_DELETED);
            try {
                //saleDetailCode = (BillDetailCode) this.getHashBillDetailCode().remove(sku);
                this.getHashBillDetailCode().put(sku, foundSaleDetailCode);
            }catch(Exception e) {
                System.out.println("err on removeSaleDetailCode: "+e.toString());
            }
        }
        
        return saleDetailCode;
    }
    
    
    public static final int TAX_AND_SERVICE=0;
    public static final int TAX_FIRST=1;
    public static final int SERVICE_FIRST=2;
    private double taxAmount=0;
    private double svcAmount=0;
    private double discAmount=0;
    
    public double calculateNetTransWith(){
        
        double temp =0;
        double argNetTrans = this.getTotalTrans() - this.getTotOtherCost();
        int discType=this.getMainSale().getDiscType();
        double discValue=this.getMainSale().getDiscount();
        double taxPct=this.getMainSale().getTaxPercentage();
        double taxValue=this.getMainSale().getTaxValue();
        double svcPct=this.getMainSale().getServicePct();
        double svcValue=this.getMainSale().getServiceValue();
        try {
            if(discType==PstBillMain.DISC_TYPE_PCT){
                double adiscValue = argNetTrans*(discValue/100);
                argNetTrans = argNetTrans*((100-discValue)/100);
                this.setDiscAmount(adiscValue);
                this.getMainSale().setDiscType(PstBillMain.DISC_TYPE_VALUE);
                this.getMainSale().setDiscount(this.getDiscAmount());
            }else{
                this.setDiscAmount(discValue);
                argNetTrans = argNetTrans - discValue;
            }
        }catch(Exception e) {
            System.out.println("err on calculateNetTransWith: "+e.toString());
        }
        
        int netTransMethod = CashierMainApp.getDSJ_CashierXML().getConfig(0).netTransPriority;
        double temp1 =0;
        double temp2 =0;
        switch(netTransMethod){
            case TAX_AND_SERVICE:
                try {
                    taxPct = argNetTrans*taxPct/100;
                    taxAmount = taxValue + taxPct;
                    temp1 = argNetTrans + taxAmount;
                    this.setTaxAmount(taxAmount);
                    
                    
                    svcPct = argNetTrans*svcPct/100;
                    svcAmount = svcValue + svcPct;
                    temp2 = temp1 + svcAmount;
                    this.setSvcAmount(svcAmount);
                }catch(Exception e) {
                    System.out.println("err on calculateNetTransWith: "+e.toString());
                }
                break;
            case TAX_FIRST:
                try {
                    taxPct = argNetTrans*taxPct/100;
                    taxAmount = taxValue + taxPct;
                    temp1 = argNetTrans + taxAmount;
                    this.setTaxAmount(taxAmount);
                    svcPct = temp1 *svcPct/100;
                    svcAmount = svcValue + svcPct;
                    temp2 = temp1 + svcAmount;
                    this.setSvcAmount(svcAmount);
                }catch(Exception e) {
                    System.out.println("err on calculateNetTransWith: "+e.toString());
                }
                break;
            case SERVICE_FIRST:
                try {
                    svcPct = argNetTrans*svcPct/100;
                    svcAmount = svcValue + svcPct;
                    temp1 = argNetTrans + svcAmount;
                    this.setSvcAmount(svcAmount);
                    taxPct = temp1*taxPct/100;
                    taxAmount = taxValue + taxPct;
                    temp2 = temp1 +taxAmount;
                    this.setTaxAmount(taxAmount);
                }catch(Exception e) {
                    System.out.println("err on calculateNetTransWith: "+e.toString());
                }
                break;
            default :
                
                break;
        }
        
        // special case for credit invoice return
        if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR && this.getLastInvoice().getMainSale().getTransctionType()==PstBillMain.TRANS_TYPE_CREDIT && this.getLastInvoice().getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
            double creditPayment = 0.00;
            creditPayment = this.getLastInvoice().getNetTrans() - CreditPaymentController.getAmountCreditPaymentsOf(this.getLastInvoice().getMainSale());
            if(temp2>creditPayment)
                this.setNetCreditPay(creditPayment);
            else
                this.setNetCreditPay(temp2);
            
            temp2 = temp2 - creditPayment;
            
            if(creditPayment>0){
                CreditPaymentModel creditPaymentModel = new CreditPaymentModel();
                creditPaymentModel.transferFromSale(this.getLastInvoice());
                
                CashCreditPayments cp = new CashCreditPayments();
                cp.setAmount(this.getNetCreditPay());
                cp.setCurrencyId(this.getCurrencyTypeUsed().getOID());
                cp.setPayDateTime(new Date());
                cp.setPaymentType(PstCashPayment.RETURN); // pay by return
                cp.setRate(this.getRateUsed().getSellingRate());
                
                CashCreditPaymentInfo cpi = new CashCreditPaymentInfo();
                
                creditPaymentModel.insertPaymentWith(cp,cpi);
                creditPaymentModel.synchronizeAllValues();
                
                this.setCreditPay(creditPaymentModel);
            }
        }
        
        this.setNetTrans(temp2 + this.getTotOtherCost());
        this.getMainSale().setTaxValue(taxAmount);
        this.getMainSale().setTaxPercentage(0);
        this.getMainSale().setServiceValue(svcAmount);
        this.getMainSale().setServicePct(0);
        
        return temp2;
    }
    
    public void transferDpToCreditPayment(){
        if(this.getLastPayment()>0){
            CreditPaymentModel creditPaymentModel = new CreditPaymentModel();
            creditPaymentModel.transferFromSale(this);
            
            CashCreditPayments cp = new CashCreditPayments();
            cp.setAmount(this.getLastPayment());
            cp.setCurrencyId(this.getCurrencyTypeUsed().getOID());
            cp.setPayDateTime(new Date());
            cp.setPaymentType(PstCashPayment.DP); // pay by down payment
            cp.setRate(this.getRateUsed().getSellingRate());
            
            CashCreditPaymentInfo cpi = new CashCreditPaymentInfo();
            
            creditPaymentModel.insertPaymentWith(cp,cpi);
            creditPaymentModel.synchronizeAllValues();
            
            this.setCreditPay(creditPaymentModel);
            
            this.setNetCreditPay(this.getLastPayment());
        }
    }
    
    /**
     * Getter for property taxAmount.
     * @return Value of property taxAmount.
     */
    public double getTaxAmount() {
        return taxAmount;
    }
    
    /**
     * Setter for property taxAmount.
     * @param taxAmount New value of property taxAmount.
     */
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    /**
     * Getter for property svcAmount.
     * @return Value of property svcAmount.
     */
    public double getSvcAmount() {
        return svcAmount;
    }
    
    /**
     * Setter for property svcAmount.
     * @param svcAmount New value of property svcAmount.
     */
    public void setSvcAmount(double svcAmount) {
        this.svcAmount = svcAmount;
    }
    
    /**
     * Getter for property discAmount.
     * @return Value of property discAmount.
     */
    public double getDiscAmount() {
        return discAmount;
    }
    
    /**
     * Setter for property discAmount.
     * @param discAmount New value of property discAmount.
     */
    public void setDiscAmount(double discAmount) {
        this.discAmount = discAmount;
    }
    
    /**
     * Getter for property netTrans.
     * @return Value of property netTrans.
     */
    public double getNetTrans() {
        return netTrans;
    }
    
    /**
     * Setter for property netTrans.
     * @param netTrans New value of property netTrans.
     */
    public void setNetTrans(double netTrans) {
        this.netTrans = netTrans;
    }
    
    public void transferFromPendingOrder(PendingOrder pendingOrder){
        if(pendingOrder.getMemberId()>1){
            try{
                MemberReg member = PstMemberReg.fetchExc(pendingOrder.getMemberId());
                this.getMainSale().setCustomerId(member.getOID());
                this.setCustomerServed(member);
            }catch(Exception e){
                System.out.println("Err transferFromPendingOrder: "+e.toString());
            }
        }else{
            MemberReg member = CashSaleController.getCustomerNonMember();
            this.setCustomerServed(member);
        }
        // fixed by wpulantara
        try{
            Sales salesPerson = PstSales.fetchExc(pendingOrder.getSalesId());
            this.setSalesPerson(salesPerson);
            this.getMainSale().setSalesCode(salesPerson.getCode());
        }
        catch(Exception e){
            System.out.println("Err transferFromPendingOrder: "+e.toString());
        }
        
        this.setLastPayment(pendingOrder.getDownPayment());
        this.getMainSale().setCashPendingOrderId(pendingOrder.getOID());
        this.setRefBoxOrdNumber(pendingOrder.getOrderNumber());
        this.setRefPendOrdNumber(pendingOrder.getPoNumber());
        
    }
    
    
    Hashtable lastDetails = new Hashtable();
    Hashtable lastDetailCodes = new Hashtable();
    DefaultSaleModel lastInvoice = null;
    CreditPaymentModel creditPay = null;
    /*
     * PLEASE FIX THIS, ITS REALYY BAADD!!!!!
     * I DONT KNOW WHAT SHOULD DO!!
     *
     */
    
    public void transferForReturn(DefaultSaleModel lastSale){
        
        Hashtable localWeightPerSaleDetail = new Hashtable();
        this.setLastInvoice(lastSale);
        try{
            lastSale.synchronizeAllValues();
        }catch(Exception e) {
            System.out.println("err on transferForReturn: "+e.toString());
        }
        lastDetails = lastSale.getHashBillDetail();
        lastDetailCodes = lastSale.getHashBillDetailCode();
        //Vector returned = CashSaleController.getReturnedItemList(lastSale.getMainSale ());
        //Hashtable det = (Hashtable)returned.get(0);
        //Hashtable detCode = (Hashtable)returned.get(1);
        //this.setReturnedItemList(det);CashSaleController.getReturnedItemList(lastSale.getMainSale ());
        //this.setReturnedItemCodeList(detCode);  CashSaleController.getReturnedItemCodeList(lastSale.getMainSale ());
        double totalTrans = lastSale.getTotalTrans();
        double netTrans = lastSale.getNetTrans();
        this.setCustomerServed(lastSale.getCustomerServed());
        double lastDiscVal = 0;
        double taxVal = 0;
        double svcVal = 0;
        System.out.println("totalTrans ="+totalTrans);
        double disc = lastSale.getMainSale().getDiscount();
        try {
            if(lastSale.getMainSale().getDiscType()==PstBillMain.DISC_TYPE_PCT){
                lastDiscVal = disc/100*totalTrans;
            }else{
                lastDiscVal = disc;
            }
            
            if(lastSale.getMainSale().getTaxPercentage()>0){
                taxVal = lastSale.getMainSale().getTaxPercentage()/100*totalTrans;
                
            }else{
                taxVal = lastSale.getMainSale().getTaxValue();
                
            }
            if(lastSale.getMainSale().getServicePct() >0){
                svcVal = lastSale.getMainSale().getServicePct()/100*totalTrans;
                
            }else{
                svcVal = lastSale.getMainSale().getServiceValue();
                
            }
        }catch(Exception e) {
            System.out.println("err on transferForReturn: "+e.toString());
        }
        
        
        Enumeration e = lastDetails.elements();
        Enumeration e2 = lastDetailCodes.elements();
        while(e.hasMoreElements()){
            Billdetail tmpDetail = (Billdetail)e.nextElement();
            BillDetailCode tmpDetailCode = null;
            try{
                tmpDetailCode = (BillDetailCode)e2.nextElement();
            }catch(Exception ex){
                System.out.println("err on transferForReturn: "+e.toString());
                tmpDetailCode = new BillDetailCode();
                tmpDetailCode.setStockCode("");
            }
            String key = tmpDetail.getSku()+"-"+tmpDetailCode.getStockCode();
            double weight = (tmpDetail.getTotalPrice()/tmpDetail.getQty())/totalTrans;
            
            localWeightPerSaleDetail.put(key, new Double(weight));
        }
        
        this.setWeightPerSaleDetail(localWeightPerSaleDetail);
        this.setLastDiscountVal(lastDiscVal);
        this.setLastTaxVal(taxVal);
        this.setLastSvcVal(svcVal);
        
    }
    /**
     * Getter for property refPendOrdNumber.
     * @return Value of property refPendOrdNumber.
     */
    public java.lang.String getRefPendOrdNumber() {
        return refPendOrdNumber;
    }
    
    /**
     * Setter for property refPendOrdNumber.
     * @param refPendOrdNumber New value of property refPendOrdNumber.
     */
    public void setRefPendOrdNumber(java.lang.String refPendOrdNumber) {
        this.refPendOrdNumber = refPendOrdNumber;
    }
    
    /**
     * Getter for property refBoxOrdNumber.
     * @return Value of property refBoxOrdNumber.
     */
    public java.lang.String getRefBoxOrdNumber() {
        return refBoxOrdNumber;
    }
    
    /**
     * Setter for property refBoxOrdNumber.
     * @param refBoxOrdNumber New value of property refBoxOrdNumber.
     */
    public void setRefBoxOrdNumber(java.lang.String refBoxOrdNumber) {
        this.refBoxOrdNumber = refBoxOrdNumber;
    }
    
    /**
     * Getter for property weightPerSaleDetail.
     * @return Value of property weightPerSaleDetail.
     */
    public Hashtable getWeightPerSaleDetail() {
        if(weightPerSaleDetail==null){
            weightPerSaleDetail = countWeightForAllItems();
        }
        return weightPerSaleDetail;
    }
    
    /**
     * Setter for property weightPerSaleDetail.
     * @param weightPerSaleDetail New value of property weightPerSaleDetail.
     */
    public void setWeightPerSaleDetail(Hashtable weightPerSaleDetail) {
        this.weightPerSaleDetail = weightPerSaleDetail;
    }
    
    public Hashtable countWeightForAllItems(){
        
        Hashtable localWeight = new Hashtable();
        double localTotalTrans = this.getTotalTrans();
        double localTotalDisc = this.getMainSale().getDiscount();
        double localTotalTax = this.getMainSale().getTaxValue();
        double localTotalSvc = this.getMainSale().getServiceValue();
        
        return localWeight;
    }
    
    private String refPendOrdNumber="";
    private String refBoxOrdNumber="";
    
    public static void main(String [] args){
        DefaultSaleModel newSale = new DefaultSaleModel();
        
        BillMain billMain = new BillMain();
        String oif = "504404263867935726";
        billMain.setOID(Long.parseLong(oif));
        DefaultSaleModel lastInvoice = SessTransactionData.getData(billMain);
        newSale.transferForReturn(lastInvoice);
        newSale.synchronizeAllValues();
        System.out.println(newSale);
        
        
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
        
        mainSale.setDocType(transType);
        mainSale.setInvoiceCounter(PstBillMain.getCounterTransaction(locationOID, cashierNo, transType));
        mainSale.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(),locationOID, cashierNo,transType));
        mainSale.setInvoiceNo(makeInvoiceNo.setInvoiceNumber());
        mainSale.setAppUserId(CashierMainApp.getCashCashier().getAppUserId());
        mainSale.setCashCashierId(CashierMainApp.getCashCashier().getOID());
        mainSale.setLocationId(locationOID);
        mainSale.setShiftId(CashierMainApp.getShift().getOID());
        mainSale.setBillDate(new Date());
        
        
    }
    /**
     * Getter for property lastDiscountVal.
     * @return Value of property lastDiscountVal.
     */
    public double getLastDiscountVal() {
        return lastDiscountVal;
    }
    
    /**
     * Setter for property lastDiscountVal.
     * @param lastDiscountVal New value of property lastDiscountVal.
     */
    public void setLastDiscountVal(double lastDiscountVal) {
        this.lastDiscountVal = lastDiscountVal;
    }
    
    /**
     * Getter for property lastSvcVal.
     * @return Value of property lastSvcVal.
     */
    public double getLastSvcVal() {
        return lastSvcVal;
    }
    
    /**
     * Setter for property lastSvcVal.
     * @param lastSvcVal New value of property lastSvcVal.
     */
    public void setLastSvcVal(double lastSvcVal) {
        this.lastSvcVal = lastSvcVal;
    }
    
    /**
     * Getter for property lastTaxVal.
     * @return Value of property lastTaxVal.
     */
    public double getLastTaxVal() {
        return lastTaxVal;
    }
    
    /**
     * Setter for property lastTaxVal.
     * @param lastTaxVal New value of property lastTaxVal.
     */
    public void setLastTaxVal(double lastTaxVal) {
        this.lastTaxVal = lastTaxVal;
    }
    
    /**
     * Getter for property lastDetails.
     * @return Value of property lastDetails.
     */
    public Hashtable getLastDetails() {
        return lastDetails;
    }
    
    /**
     * Setter for property lastDetails.
     * @param lastDetails New value of property lastDetails.
     */
    public void setLastDetails(Hashtable lastDetails) {
        this.lastDetails = lastDetails;
    }
    
    /**
     * Getter for property lastDetailCodes.
     * @return Value of property lastDetailCodes.
     */
    public Hashtable getLastDetailCodes() {
        return lastDetailCodes;
    }
    
    /**
     * Setter for property lastDetailCodes.
     * @param lastDetailCodes New value of property lastDetailCodes.
     */
    public void setLastDetailCodes(Hashtable lastDetailCodes) {
        this.lastDetailCodes = lastDetailCodes;
    }
    
    public String isSuffctOkForReturn(Billdetail detail,BillDetailCode detailCode){
        
        String message="";
        String key = detail.getSku()+"-"+detailCode.getStockCode();
        Billdetail tempDeta = this.getLastInvoice().findValidSaleDetailWith(key);
        BillDetailCode tempDetaCode = this.getLastInvoice().findValidSaleDetailCodeWith(key);
        
        return message;
    }
    
    /**
     * Getter for property lastInvoice.
     * @return Value of property lastInvoice.
     */
    public com.dimata.pos.cashier.DefaultSaleModel getLastInvoice() {
        if(lastInvoice==null){
            lastInvoice = new DefaultSaleModel();
        }
        return lastInvoice;
    }
    
    /**
     * Setter for property lastInvoice.
     * @param lastInvoice New value of property lastInvoice.
     */
    public void setLastInvoice(com.dimata.pos.cashier.DefaultSaleModel lastInvoice) {
        this.lastInvoice = lastInvoice;
        this.getMainSale().setParentId(lastInvoice.getMainSale().getOID());
    }
    
    /**
     * Getter for property returnedItemList.
     * @return Value of property returnedItemList.
     */
    public Hashtable getReturnedItemList() {
        if(returnedItemList==null){
            returnedItemList = new Hashtable();
        }
        return returnedItemList;
    }
    
    /**
     * Setter for property returnedItemList.
     * @param returnedItemList New value of property returnedItemList.
     */
    public void setReturnedItemList(Hashtable returnedItemList) {
        this.returnedItemList = returnedItemList;
    }
    
    /**
     * Getter for property returnedItemCodeList.
     * @return Value of property returnedItemCodeList.
     */
    public Hashtable getReturnedItemCodeList() {
        if(returnedItemCodeList==null){
            returnedItemCodeList = new Hashtable();
        }
        return returnedItemCodeList;
    }
    
    /**
     * Setter for property returnedItemCodeList.
     * @param returnedItemCodeList New value of property returnedItemCodeList.
     */
    public void setReturnedItemCodeList(Hashtable returnedItemCodeList) {
        this.returnedItemCodeList = returnedItemCodeList;
    }
    
    private Hashtable returnedItemList = null;//new Hashtable();
    private Hashtable returnedItemCodeList = null;//new Hashtable();
    
    public boolean isAllValuesCompleted(){
        
        if(!isAnySales()){
            System.out.println("No Sales");
            return false;
        }
        if(!isAnyInvoice()){
            System.out.println("No Invoice");
            return false;
        }
        if(!isAnyCustomer()){
            System.out.println("No Customer");
            return false;
        }
        if(!isAnySaleDetail()){
            System.out.println("No Sale Detail");
            return false;
        }
        if(!isAnyIncludedCost()){
            System.out.println("No Included Cost");
            return false;
        }
        if(!isAnyPayments()){
            System.out.println("No Payment");
            return false;
        }
        if(!isAnyReturns()){
            System.out.println("No Change");
            return false;
        }
        return true;
    }
    
    public boolean isAnySales(){
        if(this.getSalesPerson().getOID()>0 || !CashierMainApp.isEnableSaleEntry()){
            return true;
        }else{
            return false;
        }
        
    }
    public boolean isAnyInvoice(){
        if(this.getMainSale().getInvoiceNumber().length()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyInvoiceRefNumber(){
        if(this.getMainSale().getParentId()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyCustomer(){
        if(this.getMainSale().getCustomerId()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnySaleDetail(){
        if(this.getHashBillDetail().size()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyIncludedCost(){
        if(this.getMainSale().getDiscount()>=0&&this.getMainSale().getTaxValue()>=0&&this.getMainSale().getServiceValue()>=0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyPayments(){
        if(this.getMainSale().getDocType()==PstBillMain.TYPE_RETUR||this.getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
            return true;
        }
        else if(this.getCashPayments().size()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isAnyReturns(){
        if(this.getCashReturn().size()>=0){
            return true;
        }else{
            return false;
        }
        
    }
    
    /**
     * Getter for property lastChange.
     * @return Value of property lastChange.
     */
    public double getLastChange() {
        
        return lastChange;
    }
    
    /**
     * Setter for property lastChange.
     * @param lastChange New value of property lastChange.
     */
    public void setLastChange(double lastChange) {
        this.lastChange = lastChange;
    }
    
    /**
     * Getter for property customerLink.
     * @return Value of property customerLink.
     */
    public CustomerLink getCustomerLink() {
        return customerLink;
    }
    
    /**
     * Setter for property customerLink.
     * @param customerLink New value of property customerLink.
     */
    public void setCustomerLink(CustomerLink customerLink) {
        if(customerLink!=null){
            this.customerLink = customerLink;
            int curIndex = customerLink.getCurrency();
            String curCode = CashierMainApp.getCurrencyCodeUsed(curIndex);
            CurrencyType curType = (CurrencyType)CashierMainApp.getHashCurrencyType().get(curCode);
            this.setCustomerServed(CashSaleController.getCustomerMember());
            StandartRate curRate = CashSaleController.getStandartRate(String.valueOf(curType.getOID()));
            this.setCurrencyTypeUsed(curType);
            this.setRateUsed(curRate);
            this.getMainSale().setCurrencyId(curType.getOID()); 
            this.getMainSale().setRate(curRate.getSellingRate());
            System.out.println("Index user is"+curIndex);
            System.out.println("curCode user is"+curCode);
            System.out.println("curCode user type is "+curType.getOID());
            System.out.println("curCode user type is "+curType.getCode());
            System.out.println("curRate user type is "+curRate.getOID()+" and "+curRate.getSellingRate());
        }
        else{
            this.customerLink = new CustomerLink();
            String curCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).defaultCurrencyCode;
            CurrencyType curType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(curCode);
            this.setCustomerServed(CashSaleController.getCustomerNonMember());
            StandartRate curRate = CashSaleController.getStandartRate(String.valueOf(curType.getOID()));
            this.setCurrencyTypeUsed(curType);
            this.setRateUsed(curRate);
            this.getMainSale().setCurrencyId(curType.getOID()); 
            this.getMainSale().setRate(curRate.getSellingRate());
            System.out.println("curCode user is"+curCode);
            System.out.println("curCode user type is "+curType.getOID());
            System.out.println("curCode user type is "+curType.getCode());
            System.out.println("curRate user type is "+curRate.getOID()+" and "+curRate.getSellingRate());
        }
    }
    
    private String getUnitName(String sku){
        if(sku!=null&&sku.length()>3){
            String cand = sku.substring(sku.length()-3);
            if(CashierMainApp.getVectFixUnit().contains(cand)){
                return cand.toUpperCase();
            }
            else if(CashierMainApp.getVectFloatingUnit().contains(cand)){ 
                return cand.toUpperCase();
            } 
            else{
                return "PCS";
            }
        } 
        else{
            return "PCS";
        }
    }
    
    /**
     * Getter for property shouldChange.
     * @return Value of property shouldChange.
     */
    public double getShouldChange() {
        return shouldChange;
    }
    
    /**
     * Setter for property shouldChange.
     * @param shouldChange New value of property shouldChange.
     */
    public void setShouldChange(double shouldChange) {
        this.shouldChange = shouldChange;
    }
    
    /**
     * Getter for property categoryAmount.
     * @return Value of property categoryAmount.
     */
    public Hashtable getCategoryAmount() {
        return categoryAmount;
    }
    
    /**
     * Setter for property categoryAmount.
     * @param categoryAmount New value of property categoryAmount.
     */
    public void setCategoryAmount(Hashtable categoryAmount) {
        this.categoryAmount = categoryAmount;
    }
    
    /**
     * Getter for property netCreditPay.
     * @return Value of property netCreditPay.
     */
    public double getNetCreditPay() {
        return netCreditPay;
    }
    
    /**
     * Setter for property netCreditPay.
     * @param netCreditPay New value of property netCreditPay.
     */
    public void setNetCreditPay(double netCreditPay) {
        this.netCreditPay = netCreditPay;
    }
    
    /**
     * Getter for property creditPay.
     * @return Value of property creditPay.
     */
    public com.dimata.pos.cashier.CreditPaymentModel getCreditPay() {
        return creditPay;
    }
    
    /**
     * Setter for property creditPay.
     * @param creditPay New value of property creditPay.
     */
    public void setCreditPay(com.dimata.pos.cashier.CreditPaymentModel creditPay) {
        this.creditPay = creditPay;
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
        this.totOtherCost = this.totOtherCost - this.totCardCost + totCardCost;
        this.totCardCost = totCardCost;
    }
    
    /**
     * Getter for property vectBillDetail.
     * @return Value of property vectBillDetail.
     */
    public Vector getVectBillDetail() {
        return vectBillDetail;
    }
    
    /**
     * Setter for property vectBillDetail.
     * @param vectBillDetail New value of property vectBillDetail.
     */
    public void setVectBillDetail(Vector vectBillDetail) {
        this.vectBillDetail = vectBillDetail;
    }
    
    /**
     * Getter for property idxBillDetail.
     * @return Value of property idxBillDetail.
     */
    public int getIdxBillDetail() {
        return idxBillDetail;
    }
    
    /**
     * Setter for property idxBillDetail.
     * @param idxBillDetail New value of property idxBillDetail.
     */
    public void setIdxBillDetail(int idxBillDetail) {
        this.idxBillDetail = idxBillDetail;
    }
    
    /**
     * Getter for property hashOtherCost.
     * @return Value of property hashOtherCost.
     */
    public Hashtable getHashOtherCost() {
        return hashOtherCost;
    }
    
    /**
     * Setter for property hashOtherCost.
     * @param hashOtherCost New value of property hashOtherCost.
     */
    public void setHashOtherCost(Hashtable hashOtherCost) {
        this.hashOtherCost = hashOtherCost;
    }
    
    /**
     * Getter for property isOpenBillPayment.
     * @return Value of property isOpenBillPayment.
     */
    public boolean isIsOpenBillPayment() {
        return isOpenBillPayment;
    }
    
    /**
     * Setter for property isOpenBillPayment.
     * @param isOpenBillPayment New value of property isOpenBillPayment.
     */
    public void setIsOpenBillPayment(boolean isOpenBillPayment) {
        this.isOpenBillPayment = isOpenBillPayment;
    }
    
    /**
     * Getter for property categoryQty.
     * @return Value of property categoryQty.
     */
    public java.util.Hashtable getCategoryQty() {
        return categoryQty;
    }
    
    /**
     * Setter for property categoryQty.
     * @param categoryQty New value of property categoryQty.
     */
    public void setCategoryQty(java.util.Hashtable categoryQty) {
        this.categoryQty = categoryQty;
    }
    
    /**
     * Getter for property hashCategoryByMaterialId.
     * @return Value of property hashCategoryByMaterialId.
     */
    public java.util.Hashtable getHashCategoryByMaterialId() {
        return hashCategoryByMaterialId;
    }
    
    /**
     * Setter for property hashCategoryByMaterialId.
     * @param hashCategoryByMaterialId New value of property hashCategoryByMaterialId.
     */
    public void setHashCategoryByMaterialId(java.util.Hashtable hashCategoryByMaterialId) {
        this.hashCategoryByMaterialId = hashCategoryByMaterialId;
    }
    
    private double shouldChange = 0;
    
    
}
