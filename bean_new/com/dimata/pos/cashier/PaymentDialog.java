/*
 * PaymentDialog.java
 *
 * Created on December 31, 2004, 10:57 AM
 */

package com.dimata.pos.cashier;

import com.dimata.pos.entity.payment.CashCreditCard;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.PstCashCreditCard;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.common.entity.payment.*;
import com.dimata.util.Command;
import com.dimata.util.Validator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * created based on Widi's PaymentDialog
 * hopefully can fix the bug
 * @author  pulantara
 *
 */

//import com.dimata.common.entity.currency.CurrencyRate;


public class PaymentDialog extends JDialog {
    
    // OWN MADE ATTRIBUTE GOES HERE....
    
    /** current active command */
    int iCommand = 0;
    
    /** field names */
    private static String [] fieldNames={
        "No","Method","Amount","Currency","Rate","Total","Pay Date"
    };
    
    /** net Transaction value */
    private double netTrans= 0;
    /** total payment value */
    private double allTotal = 0;
    
    /** column number constants */
    private static int COL_NO=0;
    private static int COL_PAYMENT_METHOD=1;
    private static int COL_PAYMENT_AMOUNT=2;
    private static int COL_PAYMENT_CUR_USED=3;
    private static int COL_PAYMENT_RATE_USED=4;
    private static int COL_PAYMENT_TOTAL=5;
    private static int COL_PAYMENT_DATE=6;
    
    /** currency hash table
     *  help resolving the problem in getting currency object
     *  by type or by OID
     */
    private Hashtable hashCurrencyType;
    private Hashtable hashCurrencyId;
    private Hashtable hashCurIDToCode = null;
    
    /** currently used currency rate */
    private StandartRate rateUsed ;
    
    /** dataVector :: vector that contains data for this dialog form table */
    private Vector dataVector = null;
    /** columnIdentifier :: vector that contains string name of this dialog form table */
    private Vector columnIdentifier = null;
    
    /** currency selling rate hash table */
    private Hashtable hashSellingRate = null;
    
    /** payment type hash table */
    private Hashtable hashPayType = null;
    
    /** hash table that contain list of payments */
    private Hashtable payments = new Hashtable();
    /** hash table that contains list of payment infos */
    private Hashtable paymentsInfo = new Hashtable();
    
    /** candidate for payment */
    private CashPayments candidatePayment = new CashPayments();
    /** candidate for payment info */
    private CashCreditCard candidatePaymentInfo = new CashCreditCard();
    /** card cost total value */
    private double totCardCost = 0;
    
    // END OF OWN MADE ATTRIBUTE
    
    
    // OWN MADE METHODE GOES HERE .....
    /** getter n setter for candidate payment */
    public CashPayments getCandidatePayment(){
        if(candidatePayment==null){
            candidatePayment = new CashPayments();
        }
        return candidatePayment;
    }
    public void setCandidatePayment(CashPayments candidate){
        this.candidatePayment = candidate;
    }
    /** getter and setter for candidate payment info */
    public CashCreditCard getCandidatePaymentInfo(){
        if(candidatePaymentInfo==null){
            candidatePaymentInfo = new CashCreditCard();
        }
        return candidatePaymentInfo;
    }
    public void setCandidatePaymentInfo(CashCreditCard info){
        candidatePaymentInfo = info;
    }
    
    private void initTodays(){
        Date now = new Date();
        int date = now.getDate();
        int mon = now.getMonth();
        int year = now.getYear()+1900;
        
        cheqDateComboBox.setSelectedIndex(date-1);
        cheqMonthComboBox.setSelectedItem(stMonthModel[mon]);
        cheqYearComboBox.setSelectedItem(String.valueOf(year));
        
        cardDateComboBox.setSelectedIndex(date-1);
        cardMonthComboBox.setSelectedItem(stMonthModel[mon]);
        cardYearComboBox.setSelectedItem(String.valueOf(year));
        
        
    }
    String[] stMonthModel = {
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };
    /** This method is called from within the constructor to
     * initialize the form
     */
    private void initField(){
        
        /**
         *  init for date select Combo Box Model
         */
        
        // date (java's date has values from 1 to 31)
        Vector vDateModel = new Vector();
        for(int i = 1; i < 32 ; i++){
            vDateModel.add(i+"");
        }
        cheqDateComboBox.setModel(new DefaultComboBoxModel(vDateModel));
        cardDateComboBox.setModel(new DefaultComboBoxModel(vDateModel));
        
        // month (java's month has values from 0 to 11)
        
        cheqMonthComboBox.setModel(new DefaultComboBoxModel(stMonthModel));
        cardMonthComboBox.setModel(new DefaultComboBoxModel(stMonthModel));
        
        // year (java's year = calendar year - 1900)
        Vector vYearModel = new Vector();
        int thisYear =  new Date().getYear()+1900;
        for(int i = 0; i < 5; i++){
            vYearModel.add(thisYear+i+"");
        }
        cheqYearComboBox.setModel(new DefaultComboBoxModel(vYearModel));
        cardYearComboBox.setModel(new DefaultComboBoxModel(vYearModel));
        initTodays();
        /** init payment type combo box */
        Vector comboModel = new Vector();
        comboModel.add(PstCashPayment.paymentType[PstCashPayment.CASH]);
        comboModel.add(PstCashPayment.paymentType[PstCashPayment.CARD]);
        comboModel.add(PstCashPayment.paymentType[PstCashPayment.CHEQUE]);
        comboModel.add(PstCashPayment.paymentType[PstCashPayment.DEBIT]);
        payTypeComboBox.setModel(new DefaultComboBoxModel(comboModel));
        
        /** init currency combo box */
        Vector hashCurr = new Vector(CashierMainApp.getHashCurrencyType().keySet());
        paymentCurTypeComboBox.setModel(new DefaultComboBoxModel(hashCurr));
        paymentCurTypeComboBox.setSelectedItem((String)CashierMainApp.getCurrencyCodeUsed());
        curTypeUsedLabel.setText((String)CashierMainApp.getCurrencyCodeUsed());
        paidCurTypeUsedLabel.setText((String)CashierMainApp.getCurrencyCodeUsed());
        
        /** init netTransLabel */
        //netTransLabel.setText (Formater.formatNumber (this.getNetTrans (), CashierMainApp.getDSJ_CashierXML ().getConfig (0).forcurrency));
        netTransLabel.setText(toCurrency(this.getNetTrans()));
        
        paymentTable.setModel(new DefaultTableModel(new Vector(),this.getColumnIdentifier()){
            // add can't edit property
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        /** set visibility of card cost field */
        cardCostLabel.setVisible(CashierMainApp.isEnableCardCost());
        cardCostTextField.setVisible(CashierMainApp.isEnableCardCost());
        cardCostPctTextField.setVisible(CashierMainApp.isEnableCardCost());
        
        /** set the mnemonic for button n command */
        setMnenomic();
        setTableColumnSize();
    }
    
    private void setTableColumnSize(){
        int totalWidth = this.paymentTable.getWidth();
        
        this.paymentTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_METHOD).setPreferredWidth((int)(totalWidth*0.15));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_AMOUNT).setPreferredWidth((int)(totalWidth*0.20));
        
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_CUR_USED).setPreferredWidth((int)(totalWidth*0.1));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_RATE_USED).setPreferredWidth((int)(totalWidth*0.1));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_TOTAL).setPreferredWidth((int)(totalWidth*0.30));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_DATE).setPreferredWidth((int)(totalWidth*0.20));
        
        this.paymentTable.revalidate();
        this.paymentTable.repaint();
        
    }
    /** this methode init the initial state for each field in this form */
    private void initState(){
        
        setICommand(Command.ADD);
        
        cardDateComboBox.setEnabled(false);
        cardMonthComboBox.setEnabled(false);
        cardYearComboBox.setEnabled(false);
        
        cardHolderTextField.setText("");
        cardNumberTextField.setText("");
        cheqBankTextField.setText("");
        
        
        cheqDateComboBox.setEnabled(false);
        cheqMonthComboBox.setEnabled(false);
        cheqYearComboBox.setEnabled(false);
        
        cheqNameTextField.setText("");
        dbtBankTextField.setText("");
        dbtNumTextField.setText("");
        payAmountTextField.setText("");
        cardCostTextField.setText("");
        cardCostPctTextField.setText("");
        
        cardHolderTextField.setEditable(false);
        cardNumberTextField.setEditable(false);
        cheqBankTextField.setEditable(false);
        
        cheqNameTextField.setEditable(false);
        dbtBankTextField.setEditable(false);
        dbtNumTextField.setEditable(false);
        payAmountTextField.setEditable(false);
        cardCostTextField.setEditable(false);
        cardCostPctTextField.setEditable(false);
        
        cardTypeComboBox.setEnabled(false);
        
        payTypeComboBox.requestFocusInWindow();
    }
    
    /** set mnemonic for buttons in this dialog form */
    private void setMnenomic(){
        saveAllPaymentButton.setMnemonic(KeyEvent.VK_S);
        itemNewButton.setMnemonic(KeyEvent.VK_N);
    }
    
    /** Creates new form PaymentDialog */
    public PaymentDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initField();
    }
    
    /** getter and setter for netTrans
     */
    public double getNetTrans() {
        return netTrans;
    }
    public void setNetTrans(double dValue){
        netTrans = dValue;
        //netTransLabel.setText (Formater.formatNumber (netTrans, CashierMainApp.getDSJ_CashierXML ().getConfig (0).forcurrency));
        netTransLabel.setText(toCurrency(netTrans/(this.getRateUsed().getSellingRate()>0?this.getRateUsed().getSellingRate():1)));
    }
    /** getter n setter for allTotal */
    public double getAllTotal(){
        return allTotal;
    }
    public void setAllTotal(double dValue){
        allTotal = dValue;
    }
    
    /**
     * Getter for property hashCurrencyType.
     * @return Value of property hashCurrencyType.
     */
    public Hashtable getHashCurrencyType() {
        if(hashCurrencyType==null){
            hashCurrencyType = CashierMainApp.getHashCurrencyType();
        }
        return hashCurrencyType;
    }
    
    /**
     * Setter for property hashCurrencyType.
     * @param hashCurrencyType New value of property hashCurrencyType.
     */
    public void setHashCurrencyType(Hashtable hashCurrencyType) {
        this.hashCurrencyType = hashCurrencyType;
    }
    
    /**
     * Getter for property hashCurIDToCode.
     * @return Value of property hashCurIDToCode.
     */
    public Hashtable getHashCurIDToCode() {
        if(hashCurIDToCode==null){
            hashCurIDToCode = new Hashtable();
            
        }
        return hashCurIDToCode;
    }
    
    /**
     * Setter for property hashCurIDToCode.
     * @param hashCurIDToCode New value of property hashCurIDToCode.
     */
    public void setHashCurIDToCode(Hashtable hashCurIDToCode) {
        this.hashCurIDToCode = hashCurIDToCode;
    }
    
    /**
     * Getter for property hashCurrencyId.
     * @return Value of property hashCurrencyId.
     */
    public Hashtable getHashCurrencyId() {
        if(hashCurrencyId==null){
            hashCurrencyId = CashierMainApp.getHashCurrencyId();
        }
        return hashCurrencyId;
    }
    
    /**
     * Setter for property hashCurrencyId.
     * @param hashCurrencyId New value of property hashCurrencyId.
     */
    public void setHashCurrencyId(Hashtable hashCurrencyId) {
        this.hashCurrencyId = hashCurrencyId;
    }
    
    /**
     * Getter for property hashPayType.
     * @return Value of property hashPayType.
     */
    public Hashtable getHashPayType() {
        if(hashPayType==null){
            hashPayType = new Hashtable();
            int size = PstCashPayment.paymentType.length;
            //Enumeration e = PstCashPayment.paymentType.
            for(int i=0;i<size;i++){
                hashPayType.put(PstCashPayment.paymentType[i],new Integer(i));
            }
        }
        return hashPayType;
    }
    
    /**
     * Setter for property hashPayType.
     * @param hashPayType New value of property hashPayType.
     */
    public void setHashPayType(Hashtable hashPayType) {
        this.hashPayType = hashPayType;
    }
    
    /**
     * Getter for property hashSellingRate.
     * @return Value of property hashSellingRate.
     */
    public Hashtable getHashSellingRate() {
        if(hashSellingRate==null){
            hashSellingRate = CashSaleController.getHashSellingRatePerCurrency();
        }
        return hashSellingRate;
    }
    
    /**
     * Setter for property hashSellingRate.
     * @param hashSellingRate New value of property hashSellingRate.
     */
    public void setHashSellingRate(Hashtable hashSellingRate) {
        this.hashSellingRate = hashSellingRate;
    }
    
    /**
     * Getter for property payments.
     * @return Value of property payments.
     */
    public Hashtable getPayments() {
        if(payments==null){
            payments = new Hashtable();
        }
        return payments;
    }
    
    /**
     * Setter for property payments.
     * @param payments New value of property payments.
     */
    public void setPayments(Hashtable payments) {
        this.payments = payments;
        synchronizeTableAndModel();
    }
    
    /**
     * Getter for property paymentsInfo.
     * @return Value of property paymentsInfo.
     */
    public Hashtable getPaymentsInfo() {
        return paymentsInfo;
    }
    
    /**
     * Setter for property paymentsInfo.
     * @param paymentsInfo New value of property paymentsInfo.
     */
    public void setPaymentsInfo(Hashtable paymentsInfo) {
        this.paymentsInfo = paymentsInfo;
        synchronizeTableAndModel();
    }
    
    /**
     * Getter for property columnIdentifier.
     * @return Value of property columnIdentifier.
     */
    public Vector getColumnIdentifier() {
        if(columnIdentifier==null){
            columnIdentifier = new Vector();
            for(int i = 0;i<fieldNames.length;i++){
                columnIdentifier.add(fieldNames[i]);
            }
            
        }
        return columnIdentifier;
    }
    
    /**
     * Setter for property columnIdentifier.
     * @param columnIdentifier New value of property columnIdentifier.
     */
    public void setColumnIdentifier(Vector columnIdentifier) {
        this.columnIdentifier = columnIdentifier;
    }
    
    /**
     * Getter for property iCommand.
     * @return Value of property iCommand.
     */
    public int getICommand() {
        return iCommand;
    }
    
    /**
     * Setter for property iCommand.
     * @param iCommand New value of property iCommand.
     */
    public void setICommand(int iCommand) {
        this.iCommand = iCommand;
    }
    /**
     * methode that handle what action should be performed when
     * a change of payment type accured
     */
    private void cmdChangePaymentType(){
        
        int selectedType = payTypeComboBox.getSelectedIndex();
        initState();
        
        switch(selectedType){
            case PstCashPayment.CASH:
                //payAmountTextField.setEditable (true);
                this.getCandidatePayment().setPaymentType(PstCashPayment.CASH);
                paymentCurTypeComboBox.setEnabled(true);
                paymentCurTypeComboBox.requestFocusInWindow();
                break;
            case PstCashPayment.CARD:
                this.getCandidatePayment().setPaymentType(PstCashPayment.CARD);
                cardTypeComboBox.setEnabled(true);
                cardTypeComboBox.requestFocusInWindow();
                break;
            case PstCashPayment.CHEQUE:
                cheqNameTextField.setEditable(true);
                this.getCandidatePayment().setPaymentType(PstCashPayment.CHEQUE);
                cheqNameTextField.requestFocusInWindow();
                break;
            case PstCashPayment.DEBIT:
                dbtBankTextField.setEditable(true);
                this.getCandidatePayment().setPaymentType(PstCashPayment.DEBIT);
                dbtBankTextField.requestFocusInWindow();
                break;
            default:
                break;
        }
    }
    
    /** methode use for validate number input from textfield
     *  @parameter String stInput
     *  @return boolean
     */
    private boolean validateInput(String stInput){
        try{
            double temp = CashierMainApp.getDoubleFromFormated(stInput);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    
    /** methode that used for synchronized table and its model
     *  also draw/redraw the table and control the navigation button
     */
    public void synchronizeTableAndModel(){
        Vector values = new Vector( this.getPayments().values());
        int size = values.size();
        Vector table = new Vector();
        
        // synchronize for each item in payments
        allTotal = 0;
        for(int i=0;i<size;i++){
            double tempTotal = 0;
            double cost = 0;
            double totalCost = 0;
            CashPayments temp = (CashPayments)values.get(i);
            CashCreditCard info = new CashCreditCard();
            if(CashierMainApp.isNeedCardCost(temp.getPaymentType())){
                info = (CashCreditCard) this.getPaymentsInfo().get(temp.getPayDateTime());
            }
            
            Vector tempVector = new Vector();
            tempVector.add(String.valueOf(i+1));
            String sPaymentMethod = PstCashPayment.paymentType[temp.getPaymentType()];
            tempVector.add(sPaymentMethod);
            CurrencyType curType = (CurrencyType) this.getHashCurrencyId().get(new Long(temp.getCurrencyId()));
            StandartRate rate = (StandartRate) this.getHashSellingRate().get(new Long(temp.getCurrencyId()));
            
            String sPaymentAmount = toCurrency(temp.getAmount()/ rate.getSellingRate());
            
            tempTotal = temp.getAmount() / rate.getSellingRate();
            cost = info.getAmount()/info.getRate();
            String stCost = "";
            if(cost>0){
                stCost = " + "+toCurrency(cost);
            }
            tempVector.add(toCurrency(tempTotal)+stCost);
            //tempVector.add (Formater.formatNumber (temp.getAmount (),CashierMainApp.getDSJ_CashierXML ().getConfig (0).forcurrency));
            
            
            tempVector.add(curType.getCode());
            //tempVector.add (Formater.formatNumber (temp.getRate (),CashierMainApp.getDSJ_CashierXML ().getConfig (0).forcurrency));
            tempVector.add(toCurrency(temp.getRate()));
            totalCost = info.getAmount();
            stCost = "";
            if(totalCost>0){
                stCost = " + "+toCurrency(totalCost);
            }
            tempVector.add(toCurrency(temp.getAmount())+stCost);
            
            tempVector.add(String.valueOf(temp.getPayDateTime()));
            table.add(tempVector);
            allTotal = allTotal + temp.getAmount();
        }
        
        // set the table model
        this.paymentTable.setModel(new DefaultTableModel(table,this.getColumnIdentifier()){
            // add can't edit property
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        setTableColumnSize();
        this.paymentTable.validate();
        
        // update total payment
        this.setAllTotal(allTotal);
        //this.paidTransLabel.setText (Formater.formatNumber (allTotal,CashierMainApp.getDSJ_CashierXML ().getConfig (0).forcurrency));
        this.paidTransLabel.setText(toCurrency(allTotal/(this.getRateUsed().getSellingRate()>0?this.getRateUsed().getSellingRate():1)));
        // if allTotal >= netTrans enable saveAllItemButton
        if(allTotal>=netTrans){
            cmdNewPayment();
            saveAllPaymentButton.setEnabled(true);
            saveAllPaymentButton.requestFocusInWindow();
        }
        else{
            saveAllPaymentButton.setEnabled(false);
            cmdNewPayment();
        }
    }
    
    /** finding the right CashPayments object that satisfied the current parameter
     * @return CashPayments
     * @parameter String currUsed, String payType
     */
    private CashPayments findPaymentsWith(String curUsed,String payType){
        
        CashPayments temp = null;
        CashPayments foundPayment = null;
        double amount = 0;
        Enumeration e = this.getPayments().elements();
        boolean found  = false;
        long lCurUsed = 0;
        CurrencyType tempCur = (CurrencyType) this.getHashCurrencyType().get(curUsed);
        lCurUsed = tempCur.getOID();
        Integer IPayType = (Integer)this.getHashPayType().get(payType);
        int iPayType = IPayType.intValue();
        
        while(e.hasMoreElements()&&found==false){
            temp = (CashPayments) e.nextElement();
            if(temp.getCurrencyId()==lCurUsed&&temp.getPaymentType()==iPayType){
                foundPayment = temp;
                found = true;
            } // end if
        } // end while
        
        return foundPayment;
    }
    
    /** finding the right CashPayments object that satisfied the current parameter
     * @return CashPayments
     * @parameter int payType, long currId
     */
    private CashPayments findPaymentsWith(int payType,long currId){
        
        CashPayments temp = null;
        CashPayments foundPayment = null;
        boolean found = false;
        Enumeration e = this.getPayments().elements();
        
        while(e.hasMoreElements()&&found==false){
            temp = (CashPayments) e.nextElement();
            if(temp.getCurrencyId()==currId&&temp.getPaymentType()==payType){
                foundPayment = temp;
                found = true;
            }
        }
        return foundPayment;
    }
    
    private void cmdItemAdd() throws NumberFormatException{
        
        int candidateType = this.getCandidatePayment().getPaymentType();
        String curType = (String)paymentCurTypeComboBox.getSelectedItem();
        CurrencyType currId = (CurrencyType)this.getHashCurrencyType().get(curType);
        long longCurrId = currId.getOID();
        candidatePayment.setCurrencyId(longCurrId);
        
        StandartRate srate = CashSaleController.getLatestRate(String.valueOf(currId.getOID()));
        candidatePayment.setRate(srate.getSellingRate());
        candidatePayment.setPaymentType(payTypeComboBox.getSelectedIndex());
        double amount = CashierMainApp.getDoubleFromFormated(payAmountTextField.getText());
        candidatePayment.setAmount(amount*srate.getSellingRate());
        
        candidatePayment.setPayDateTime(new Date());
        CashCreditCard paymentInfo = new CashCreditCard();
        if(candidateType!=PstCashPayment.CASH){
            switch(candidateType){
                case PstCashPayment.CHEQUE:
                    paymentInfo.setChequeAccountName(cheqNameTextField.getText());
                    paymentInfo.setChequeBank(cheqBankTextField.getText());
                    paymentInfo.setChequeDueDate(new Date(
                    Integer.parseInt((String) cheqYearComboBox.getSelectedItem())-1900,
                    cheqMonthComboBox.getSelectedIndex(),
                    cheqDateComboBox.getSelectedIndex()+1));
                    break;
                case PstCashPayment.CARD:
                    paymentInfo.setCcName((String)cardTypeComboBox.getSelectedItem());
                    paymentInfo.setCcNumber(cardNumberTextField.getText());
                    paymentInfo.setHolderName(cardHolderTextField.getText());
                    paymentInfo.setExpiredDate(new Date(
                    Integer.parseInt((String) cardYearComboBox.getSelectedItem())-1900,
                    cardMonthComboBox.getSelectedIndex(),
                    cardDateComboBox.getSelectedIndex()+1));
                    break;
                case PstCashPayment.DEBIT:
                    paymentInfo.setDebitBankName(dbtBankTextField.getText());
                    paymentInfo.setDebitCardName(dbtNumTextField.getText());
                    break;
            }
            if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(candidateType)){
                paymentInfo.setCurrencyId(longCurrId);
                paymentInfo.setRate(srate.getSellingRate());
                amount = CashierMainApp.getDoubleFromFormated(cardCostTextField.getText());
                paymentInfo.setAmount(amount*srate.getSellingRate());
                System.out.println("INI NILAI CARD COST PAS ADD1: "+paymentInfo.getAmount());
                this.setTotCardCost(this.getTotCardCost()+paymentInfo.getAmount()); 
                System.out.println("INI NILAI CARD COST PAS ADD2: "+this.getTotCardCost());
            }
        }
        
        boolean itemAlreadyInserted = false;
        
        CashPayments tempPayment = findPaymentsWith(candidateType,longCurrId);
        if(tempPayment!=null){
            itemAlreadyInserted = true;
        }
        if(itemAlreadyInserted){
            tempPayment.setAmount(tempPayment.getAmount()+candidatePayment.getAmount());
            candidatePayment = tempPayment;
        }
        paymentInfo.setUpdateStatus(PstCashCreditCard.UPDATE_STATUS_INSERTED);
        candidatePayment.setUpdateStatus(PstCashPayment.UPDATE_STATUS_INSERTED);
        this.getPaymentsInfo().put(candidatePayment.getPayDateTime(), paymentInfo);
        this.getPayments().put(candidatePayment.getPayDateTime(), candidatePayment);
        synchronizeTableAndModel();
    }
    
    public boolean isAllValuesCompleted(){
        if(!isAnyPayments()){
            return false;
        }
        return true;
    }
    public boolean isAnyPayments(){
        if(this.getPayments().size()>0){
            return true;
        }else{
            return false;
        }
    }
    public void getGlobalKeyListener(KeyEvent evt){
        switch(evt.getKeyCode()){
            case KeyEvent.VK_F1 :
                break;
            case KeyEvent.VK_F2:
                break;
            case KeyEvent.VK_F3:
                break;
            case KeyEvent.VK_F4:
                cmdEditTable();
                break;
            case KeyEvent.VK_F5:
                cmdNewPayment();
                break;
            case KeyEvent.VK_F6:
                break;
            case KeyEvent.VK_F7:
                break;
            case KeyEvent.VK_F8:
                break;
            case KeyEvent.VK_F9:
                break;
            case KeyEvent.VK_F10:
                break;
            case KeyEvent.VK_F11:
                break;
            case KeyEvent.VK_F12:
                cmdSaveAll();
                break;
            case KeyEvent.VK_ESCAPE:
                break;
            case KeyEvent.VK_ENTER:
                break;
            case KeyEvent.VK_DOWN:
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_BACK_SPACE:
                break;
            case KeyEvent.VK_TAB:
                break;
                
            default:
                break;
        }
    }
    /** Init for new payment */
    private void cmdNewPayment(){
        initState();
        this.setCandidatePayment(null);
        this.setCandidatePaymentInfo(null);
        this.getCandidatePayment();
        this.getCandidatePaymentInfo();
        this.setICommand(Command.ADD);
    }
    
    /** Load selected row to editor */
    private void cmdLoadPaymentInfo(){
        
        this.setICommand(Command.EDIT);
        int selectedRow = this.paymentTable.getSelectedRow();
        
        String curType = (String)this.paymentTable.getValueAt(selectedRow, COL_PAYMENT_CUR_USED);
        String payType = (String)this.paymentTable.getValueAt(selectedRow, COL_PAYMENT_METHOD);
        
        CashPayments temp = findPaymentsWith(curType,payType);
        boolean itemFound = false;
        StandartRate srate = CashSaleController.getLatestRate(String.valueOf(temp.getCurrencyId()));
        if(temp!=null)
            itemFound = true;
        if(itemFound) {
            payTypeComboBox.setSelectedIndex(temp.getPaymentType());
            paymentCurTypeComboBox.setSelectedItem(curType);
            payAmountTextField.setText(toCurrency(temp.getAmount()/srate.getSellingRate()));
            payAmountTextField.setEditable(true);
            payAmountTextField.requestFocusInWindow();
            if(temp.getPaymentType()!=PstCashPayment.CASH){
                CashCreditCard card = (CashCreditCard) this.getPaymentsInfo().get(temp.getPayDateTime());
                switch(temp.getPaymentType()){
                    case PstCashPayment.CARD :
                        cardDateComboBox.setSelectedIndex(card.getExpiredDate().getDate()-1);
                        cardMonthComboBox.setSelectedIndex(card.getExpiredDate().getMonth());
                        cardYearComboBox.setSelectedItem(card.getExpiredDate().getYear()+1900+"");
                        cardHolderTextField.setText(card.getCcName());
                        cardNumberTextField.setText(card.getCcNumber());
                        break;
                    case PstCashPayment.CHEQUE :
                        cheqDateComboBox.setSelectedIndex(card.getChequeDueDate().getDate()-1);
                        cheqMonthComboBox.setSelectedIndex(card.getChequeDueDate().getMonth());
                        cheqYearComboBox.setSelectedItem(card.getChequeDueDate().getYear()+1900+"");
                        cheqNameTextField.setText(card.getChequeAccountName());
                        cheqBankTextField.setText(card.getChequeBank());
                        break;
                    case PstCashPayment.DEBIT :
                        dbtBankTextField.setText(card.getDebitBankName());
                        dbtNumTextField.setText(card.getDebitCardName());
                        break;
                }
                this.setCandidatePaymentInfo(card);
            }
            
            this.setCandidatePayment(temp);
        }
    }
    
    /** delete selected item */
    private void cmdItemDelete(){
        cmdLoadPaymentInfo();
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure to delete this item?","Deleting items",JOptionPane.YES_NO_OPTION);
        
        if(answer==JOptionPane.YES_OPTION){
            this.getPayments().remove(this.getCandidatePayment().getPayDateTime());
            CashCreditCard temp = (CashCreditCard) this.getPaymentsInfo().remove(this.getCandidatePayment().getPayDateTime());
            this.setTotCardCost(this.getTotCardCost()-temp.getAmount());
            synchronizeTableAndModel();
            
        }
        
    }
    
    /** update current selected row */
    private void cmdItemUpdate() throws NumberFormatException{
        
        this.getPayments().remove(this.getCandidatePayment().getPayDateTime());
        CashCreditCard temp = (CashCreditCard) this.getPaymentsInfo().remove(this.getCandidatePayment().getPayDateTime());
        this.setTotCardCost(this.getTotCardCost()-temp.getAmount());
        
        int candidateType = this.getCandidatePayment().getPaymentType();
        
        String curType = (String)paymentCurTypeComboBox.getSelectedItem();
        CurrencyType currId = (CurrencyType)this.getHashCurrencyType().get(curType);
        long longCurrId = currId.getOID();
        candidatePayment.setCurrencyId(longCurrId);
        
        StandartRate srate = CashSaleController.getLatestRate(String.valueOf(currId.getOID()));
        candidatePayment.setRate(srate.getSellingRate());
        candidatePayment.setPaymentType(payTypeComboBox.getSelectedIndex());
        double amount = CashierMainApp.getDoubleFromFormated(payAmountTextField.getText());
        candidatePayment.setAmount(amount*srate.getSellingRate());
        
        candidatePayment.setPayDateTime(new Date());
        CashCreditCard paymentInfo = new CashCreditCard();
        if(candidateType!=PstCashPayment.CASH){
            switch(candidateType){
                case PstCashPayment.CHEQUE:
                    paymentInfo.setChequeAccountName(cheqNameTextField.getText());
                    paymentInfo.setChequeBank(cheqBankTextField.getText());
                    paymentInfo.setChequeDueDate(new Date(
                    Integer.parseInt((String) cheqYearComboBox.getSelectedItem())-1900,
                    cheqMonthComboBox.getSelectedIndex(),
                    cheqDateComboBox.getSelectedIndex()+1));
                    
                    break;
                case PstCashPayment.CARD:
                    paymentInfo.setCcName(cardHolderTextField.getText());
                    paymentInfo.setCcNumber(cardNumberTextField.getText());
                    paymentInfo.setExpiredDate(new Date(
                    Integer.parseInt((String) cardYearComboBox.getSelectedItem()),
                    cardMonthComboBox.getSelectedIndex(),
                    cardDateComboBox.getSelectedIndex()+1));
                    
                    break;
                case PstCashPayment.DEBIT:
                    paymentInfo.setDebitBankName(dbtBankTextField.getText());
                    paymentInfo.setDebitCardName(dbtNumTextField.getText());
                    
                    break;
            }
            if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(candidateType)){
                paymentInfo.setCurrencyId(longCurrId);
                paymentInfo.setRate(srate.getSellingRate());
                amount = CashierMainApp.getDoubleFromFormated(cardCostTextField.getText());
                paymentInfo.setAmount(amount*srate.getSellingRate());
                this.setTotCardCost(this.getTotCardCost()+paymentInfo.getAmount()); 
            }
        }
        
        
        this.getPaymentsInfo().put(candidatePayment.getPayDateTime(), paymentInfo);
        this.getPayments().put(candidatePayment.getPayDateTime(), candidatePayment);
        
        
        synchronizeTableAndModel();
        
    }
    
    /** save all item in the table
     *  prereq : at least one item in the table
     */
    private void cmdSaveAll(){
        CashSaleController.setPaymentSet(this.getPayments());
        CashSaleController.setPaymentInfoSet(this.getPaymentsInfo());
        System.out.println("ini besar card cost di payment : "+this.getTotCardCost());
        CashSaleController.setCardCost(this.getTotCardCost());
        this.dispose();
    }
    
    /** toCurrency
     *  convert into selected currency format
     *  @param double
     *  @return String
     */
    private String toCurrency(double dValue){
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }
    
    //  END OF OWN MADE METHODE
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        payTypePanel = new javax.swing.JPanel();
        payTypeLabel = new javax.swing.JLabel();
        payTypeComboBox = new javax.swing.JComboBox();
        cardCostLabel = new javax.swing.JLabel();
        payAmountTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        paymentCurTypeComboBox = new javax.swing.JComboBox();
        payAmountLabel1 = new javax.swing.JLabel();
        cardCostTextField = new javax.swing.JTextField();
        cardCostPctTextField = new javax.swing.JTextField();
        creditCardPanel = new javax.swing.JPanel();
        cardTypeComboBox = new javax.swing.JComboBox();
        cardTypeLabel = new javax.swing.JLabel();
        cardNumLabel = new javax.swing.JLabel();
        cardNumberTextField = new javax.swing.JTextField();
        cardHolderTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cardDateComboBox = new javax.swing.JComboBox();
        cardMonthComboBox = new javax.swing.JComboBox();
        cardYearComboBox = new javax.swing.JComboBox();
        chequePanel = new javax.swing.JPanel();
        cheqBankLabel11 = new javax.swing.JLabel();
        cheqHolderNameLabel = new javax.swing.JLabel();
        cheqExpDateLabel = new javax.swing.JLabel();
        cheqNameTextField = new javax.swing.JTextField();
        cheqBankTextField = new javax.swing.JTextField();
        cheqDateComboBox = new javax.swing.JComboBox();
        cheqMonthComboBox = new javax.swing.JComboBox();
        cheqYearComboBox = new javax.swing.JComboBox();
        debitCardPanel = new javax.swing.JPanel();
        dbtNumTextField = new javax.swing.JTextField();
        dbtBankLabel = new javax.swing.JLabel();
        dbtCardNameLabel = new javax.swing.JLabel();
        dbtBankTextField = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        saveAllPaymentButton = new javax.swing.JButton();
        itemNewButton = new javax.swing.JButton();
        editTableButton = new javax.swing.JButton();
        closeDialogButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        curTypeUsedLabel = new javax.swing.JLabel();
        netTransLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        paidCurTypeUsedLabel = new javax.swing.JLabel();
        paidTransLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        paymentTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment Dialog");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder(""));
        payTypePanel.setLayout(new java.awt.GridBagLayout());

        payTypePanel.setBorder(new javax.swing.border.TitledBorder("Payment Type"));
        payTypeLabel.setText("Payment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(payTypeLabel, gridBagConstraints);

        payTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                payTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(payTypeComboBox, gridBagConstraints);

        cardCostLabel.setText("Card Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(cardCostLabel, gridBagConstraints);

        payAmountTextField.setColumns(10);
        payAmountTextField.setEditable(false);
        payAmountTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payAmountTextFieldActionPerformed(evt);
            }
        });
        payAmountTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                payAmountTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(payAmountTextField, gridBagConstraints);

        jLabel1.setText("Currency Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(jLabel1, gridBagConstraints);

        paymentCurTypeComboBox.setEnabled(false);
        paymentCurTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paymentCurTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(paymentCurTypeComboBox, gridBagConstraints);

        payAmountLabel1.setText("Payment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(payAmountLabel1, gridBagConstraints);

        cardCostTextField.setColumns(6);
        cardCostTextField.setEditable(false);
        cardCostTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardCostTextFieldActionPerformed(evt);
            }
        });
        cardCostTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardCostTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(cardCostTextField, gridBagConstraints);

        cardCostPctTextField.setColumns(3);
        cardCostPctTextField.setEditable(false);
        cardCostPctTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardCostPctTextFieldActionPerformed(evt);
            }
        });
        cardCostPctTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cardCostPctTextFieldFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(cardCostPctTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        jPanel1.add(payTypePanel, gridBagConstraints);

        creditCardPanel.setLayout(new java.awt.GridBagLayout());

        creditCardPanel.setBorder(new javax.swing.border.TitledBorder("Credit Card"));
        cardTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Visa", "Master Card", "BCA Card", "AMEX" }));
        cardTypeComboBox.setEnabled(false);
        cardTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardTypeComboBox, gridBagConstraints);

        cardTypeLabel.setText("Card Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardTypeLabel, gridBagConstraints);

        cardNumLabel.setText("Card Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardNumLabel, gridBagConstraints);

        cardNumberTextField.setColumns(10);
        cardNumberTextField.setEditable(false);
        cardNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardNumberTextFieldActionPerformed(evt);
            }
        });
        cardNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardNumberTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardNumberTextField, gridBagConstraints);

        cardHolderTextField.setColumns(10);
        cardHolderTextField.setEditable(false);
        cardHolderTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardHolderTextFieldActionPerformed(evt);
            }
        });
        cardHolderTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardHolderTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardHolderTextField, gridBagConstraints);

        jLabel4.setText("Card Holder Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(jLabel4, gridBagConstraints);

        jLabel6.setText("Card Exp Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(jLabel6, gridBagConstraints);

        cardDateComboBox.setEnabled(false);
        cardDateComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardDateComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardDateComboBox, gridBagConstraints);

        cardMonthComboBox.setEnabled(false);
        cardMonthComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardMonthComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardMonthComboBox, gridBagConstraints);

        cardYearComboBox.setEnabled(false);
        cardYearComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardYearComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        creditCardPanel.add(cardYearComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(creditCardPanel, gridBagConstraints);

        chequePanel.setLayout(new java.awt.GridBagLayout());

        chequePanel.setBorder(new javax.swing.border.TitledBorder("Cheque"));
        cheqBankLabel11.setText("Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqBankLabel11, gridBagConstraints);

        cheqHolderNameLabel.setText("Account Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqHolderNameLabel, gridBagConstraints);

        cheqExpDateLabel.setText("Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqExpDateLabel, gridBagConstraints);

        cheqNameTextField.setColumns(10);
        cheqNameTextField.setEditable(false);
        cheqNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cheqNameTextFieldActionPerformed(evt);
            }
        });
        cheqNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cheqNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqNameTextField, gridBagConstraints);

        cheqBankTextField.setColumns(10);
        cheqBankTextField.setEditable(false);
        cheqBankTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cheqBankTextFieldActionPerformed(evt);
            }
        });
        cheqBankTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cheqBankTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqBankTextField, gridBagConstraints);

        cheqDateComboBox.setEnabled(false);
        cheqDateComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cheqDateComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqDateComboBox, gridBagConstraints);

        cheqMonthComboBox.setEnabled(false);
        cheqMonthComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cheqMonthComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqMonthComboBox, gridBagConstraints);

        cheqYearComboBox.setEnabled(false);
        cheqYearComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cheqYearComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        chequePanel.add(cheqYearComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(chequePanel, gridBagConstraints);

        debitCardPanel.setLayout(new java.awt.GridBagLayout());

        debitCardPanel.setBorder(new javax.swing.border.TitledBorder("Debit Card"));
        dbtNumTextField.setColumns(10);
        dbtNumTextField.setEditable(false);
        dbtNumTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dbtNumTextFieldActionPerformed(evt);
            }
        });
        dbtNumTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dbtNumTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        debitCardPanel.add(dbtNumTextField, gridBagConstraints);

        dbtBankLabel.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        debitCardPanel.add(dbtBankLabel, gridBagConstraints);

        dbtCardNameLabel.setText("Debit Card Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        debitCardPanel.add(dbtCardNameLabel, gridBagConstraints);

        dbtBankTextField.setColumns(10);
        dbtBankTextField.setEditable(false);
        dbtBankTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dbtBankTextFieldActionPerformed(evt);
            }
        });
        dbtBankTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dbtBankTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        debitCardPanel.add(dbtBankTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(debitCardPanel, gridBagConstraints);

        jPanel13.setLayout(new java.awt.GridBagLayout());

        jPanel13.setBorder(new javax.swing.border.TitledBorder("Command"));
        saveAllPaymentButton.setText("Save Payments - F12");
        saveAllPaymentButton.setEnabled(false);
        saveAllPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllPaymentButtonActionPerformed(evt);
            }
        });
        saveAllPaymentButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveAllPaymentButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(saveAllPaymentButton, gridBagConstraints);

        itemNewButton.setText("New - F5");
        itemNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNewButtonActionPerformed(evt);
            }
        });
        itemNewButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemNewButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(itemNewButton, gridBagConstraints);

        editTableButton.setText("Edit Table-F4");
        editTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTableButtonActionPerformed(evt);
            }
        });
        editTableButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editTableButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(editTableButton, gridBagConstraints);

        closeDialogButton.setMnemonic('X');
        closeDialogButton.setText("Cancel Payments - Alt+X");
        closeDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeDialogButtonActionPerformed(evt);
            }
        });
        closeDialogButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closeDialogButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(closeDialogButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel13, gridBagConstraints);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel2.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        curTypeUsedLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        curTypeUsedLabel.setText("Rp.");
        jPanel2.add(curTypeUsedLabel);

        netTransLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        netTransLabel.setText("0.00");
        jPanel2.add(netTransLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel4.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        paidCurTypeUsedLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        paidCurTypeUsedLabel.setText("Rp.");
        jPanel4.add(paidCurTypeUsedLabel);

        paidTransLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        paidTransLabel.setText("0.00");
        jPanel4.add(paidTransLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel4, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel3.setPreferredSize(new java.awt.Dimension(771, 247));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(new javax.swing.border.TitledBorder("Payment Detail"));
        paymentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Payment Method", "Payment Amount", "Currency Type Used", "Rate Used", "Total", "Payment Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paymentTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(paymentTable);

        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel8, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void cardCostPctTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cardCostPctTextFieldFocusGained
        cardCostPctTextField.selectAll();
    }//GEN-LAST:event_cardCostPctTextFieldFocusGained

    private void cardCostPctTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardCostPctTextFieldActionPerformed
        String amount = cardCostPctTextField.getText();
        if(Validator.isFloat(amount))//amount.length()>0)
        {
            double payAmount = CashierMainApp.getDoubleFromFormated(payAmountTextField.getText());
            double pctCost = CashierMainApp.getDoubleFromFormated(cardCostPctTextField.getText());
            double costAmount = pctCost * payAmount / 100;
            cardCostTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(costAmount));
            cardCostTextField.setEditable(true);
            cardCostTextField.requestFocusInWindow();
        }
        else {
            JOptionPane.showMessageDialog(this,"Pct Amount not right","Payment Dialog",JOptionPane.OK_CANCEL_OPTION);
            payAmountTextField.setText("");
        }
    }//GEN-LAST:event_cardCostPctTextFieldActionPerformed
    
    private void cardCostTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardCostTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cardCostTextFieldKeyPressed
    
    private void cardCostTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardCostTextFieldActionPerformed
        String amount = cardCostTextField.getText();
        if(Validator.isFloat(amount))//amount.length()>0)
        {
            if(iCommand==Command.ADD){
                cmdItemAdd();
            }
            else {
                cmdItemUpdate();
            }
        }
        else {
            JOptionPane.showMessageDialog(this,"Card Cost Amount not right","Payment Dialog",JOptionPane.OK_CANCEL_OPTION);
            payAmountTextField.setText("");
        }
    }//GEN-LAST:event_cardCostTextFieldActionPerformed
    
    private void cheqBankTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cheqBankTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cheqBankTextFieldKeyPressed
    
    private void cheqNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cheqNameTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cheqNameTextFieldKeyPressed
    
    private void dbtNumTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dbtNumTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_dbtNumTextFieldKeyPressed
    
    private void dbtBankTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dbtBankTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_dbtBankTextFieldKeyPressed
    
    private void editTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editTableButtonActionPerformed
        this.cmdEditTable();
    }//GEN-LAST:event_editTableButtonActionPerformed
    
    private void closeDialogButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_closeDialogButtonKeyPressed
    {//GEN-HEADEREND:event_closeDialogButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_closeDialogButtonKeyPressed
    
    private void closeDialogButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeDialogButtonActionPerformed
    {//GEN-HEADEREND:event_closeDialogButtonActionPerformed
        cmdCancel();
    }//GEN-LAST:event_closeDialogButtonActionPerformed
    
    public void cmdCancel(){
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure to cancel this payments?","Canceling payments",JOptionPane.YES_NO_OPTION);
        switch(answer){
            case JOptionPane.YES_OPTION:
                this.setPayments(null);
                this.setPaymentsInfo(null);
                CashSaleController.setPaymentSet(null);
                CashSaleController.setPaymentInfoSet(null);
                
                this.dispose();
                break;
            case JOptionPane.NO_OPTION:
                cmdNewPayment();
                break;
        }
    }
    private void cardHolderTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_cardHolderTextFieldKeyPressed
    {//GEN-HEADEREND:event_cardHolderTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cardHolderTextFieldKeyPressed
    
    private void cardNumberTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_cardNumberTextFieldKeyPressed
    {//GEN-HEADEREND:event_cardNumberTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cardNumberTextFieldKeyPressed
    
    private void payAmountTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_payAmountTextFieldKeyPressed
    {//GEN-HEADEREND:event_payAmountTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_payAmountTextFieldKeyPressed
    
    private void editTableButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_editTableButtonKeyPressed
    {//GEN-HEADEREND:event_editTableButtonKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
        
    }//GEN-LAST:event_editTableButtonKeyPressed
    
    public void cmdEditTable(){
        if(isAnyPayments()){
            paymentTable.requestFocusInWindow();
            paymentTable.setRowSelectionInterval(0,0);
        }else{
            JOptionPane.showMessageDialog(this,"Plase complete payments","Incomplete data",JOptionPane.WARNING_MESSAGE);
            cmdNewPayment();
        }
    }
    private void formKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyPressed
    {//GEN-HEADEREND:event_formKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed
    
    private void saveAllPaymentButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveAllPaymentButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdSaveAll();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_saveAllPaymentButtonKeyPressed
    
    private void saveAllPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllPaymentButtonActionPerformed
        if(isAnyPayments()){
            cmdSaveAll();
        }else{
            JOptionPane.showMessageDialog(this,"Please add any payments","Incomplete data",JOptionPane.WARNING_MESSAGE);
            cmdNewPayment();
        }
    }//GEN-LAST:event_saveAllPaymentButtonActionPerformed
    
    private void paymentTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentTableKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdLoadPaymentInfo();
            setICommand(Command.EDIT);
        }else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
            cmdItemDelete();
            setICommand(Command.ADD);
        }
    }//GEN-LAST:event_paymentTableKeyPressed
    
    private void itemNewButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemNewButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdNewPayment();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_itemNewButtonKeyPressed
    
    private void itemNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNewButtonActionPerformed
        this.cmdNewPayment();
    }//GEN-LAST:event_itemNewButtonActionPerformed
    
    private void payAmountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payAmountTextFieldActionPerformed
        String amount = payAmountTextField.getText();
        if(Validator.isFloat(amount))//amount.length()>0)
        {
            if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(this.getCandidatePayment().getPaymentType())){
                double payAmount = CashierMainApp.getDoubleFromFormated(payAmountTextField.getText());
                double pctCost = 0;
                try{
                    pctCost = Double.parseDouble(CashierMainApp.getDSJ_CashierXML().getConfig(0).cardCost);
                }
                catch(Exception e){
                    System.out.println("err cardCost: "+e.toString());
                    pctCost = 0;
                }
                double costAmount = pctCost * payAmount / 100;
                cardCostPctTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(pctCost));
                cardCostTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(costAmount));
                cardCostPctTextField.setEditable(true);
                cardCostPctTextField.requestFocusInWindow();
            }
            else{
                if(iCommand==Command.ADD){
                    cmdItemAdd();
                }
                else {
                    cmdItemUpdate();
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(this,"Pay Amount not right","Payment Dialog",JOptionPane.OK_CANCEL_OPTION);
            payAmountTextField.setText("");
        }
    }//GEN-LAST:event_payAmountTextFieldActionPerformed
    
    private void cheqBankTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cheqBankTextFieldActionPerformed
        if(cheqBankTextField.getText().length()>0){
            paymentCurTypeComboBox.setEnabled(true);
            paymentCurTypeComboBox.requestFocusInWindow();
        }else{
            //getGlobalKeyListener (evt);
        }
    }//GEN-LAST:event_cheqBankTextFieldActionPerformed
    
    private void cheqYearComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cheqYearComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cheqBankTextField.setEditable(true);
            cheqBankTextField.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cheqYearComboBoxKeyPressed
    
    private void cheqMonthComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cheqMonthComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cheqYearComboBox.setEnabled(true);
            cheqYearComboBox.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cheqMonthComboBoxKeyPressed
    
    private void cheqDateComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cheqDateComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cheqMonthComboBox.setEnabled(true);
            cheqMonthComboBox.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cheqDateComboBoxKeyPressed
    
    private void cheqNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cheqNameTextFieldActionPerformed
        if(cheqNameTextField.getText().length()>0){
            cheqDateComboBox.setEnabled(true);
            cheqDateComboBox.requestFocusInWindow();
        }else{
            //getGlobalKeyListener (evt);
        }
    }//GEN-LAST:event_cheqNameTextFieldActionPerformed
    
    private void dbtNumTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbtNumTextFieldActionPerformed
        if(dbtNumTextField.getText().length()>0){
            paymentCurTypeComboBox.setEnabled(true);
            paymentCurTypeComboBox.requestFocusInWindow();
        }else{
            //getGlobalKeyListener (evt);
        }
    }//GEN-LAST:event_dbtNumTextFieldActionPerformed
    
    private void dbtBankTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbtBankTextFieldActionPerformed
        if(dbtBankTextField.getText().length()>0){
            dbtNumTextField.setEditable(true);
            dbtNumTextField.requestFocusInWindow();
        }else{
            //getGlobalKeyListener (evt);
        }
    }//GEN-LAST:event_dbtBankTextFieldActionPerformed
    
    private void cardYearComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardYearComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            paymentCurTypeComboBox.setEnabled(true);
            paymentCurTypeComboBox.requestFocusInWindow();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cardYearComboBoxKeyPressed
    
    private void cardMonthComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardMonthComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cardYearComboBox.setEnabled(true);
            cardYearComboBox.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cardMonthComboBoxKeyPressed
    
    private void cardDateComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardDateComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cardMonthComboBox.setEnabled(true);
            cardMonthComboBox.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cardDateComboBoxKeyPressed
    
    private void cardTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cardNumberTextField.setEditable(true);
            cardNumberTextField.requestFocusInWindow();
            
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cardTypeComboBoxKeyPressed
    
    private void cardHolderTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardHolderTextFieldActionPerformed
        if(cardHolderTextField.getText().length()>0){
            cardDateComboBox.setEnabled(true);
            cardDateComboBox.requestFocusInWindow();
            
        }else{
            //getGlobalKeyListener (evt);
        }
    }//GEN-LAST:event_cardHolderTextFieldActionPerformed
    
    private void cardNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardNumberTextFieldActionPerformed
        if(cardNumberTextField.getText().length()>0){
            cardHolderTextField.setEditable(true);
            cardHolderTextField.requestFocusInWindow();
        }else{
            //getGlobalKeyListener (evt);
        }
    }//GEN-LAST:event_cardNumberTextFieldActionPerformed
    
    private void paymentCurTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentCurTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            double netT = this.getNetTrans()-this.getAllTotal();
            String selectedCur = (String)paymentCurTypeComboBox.getSelectedItem();
            CurrencyType type = (CurrencyType)CashierMainApp.getHashCurrencyType().get(selectedCur);
            StandartRate rate = (StandartRate)CashSaleController.getLatestRate(String.valueOf(type.getOID()));
            StandartRate defaultRate = this.getRateUsed();
            double iShouldPay = netT/rate.getSellingRate();
            double resShould = netT%rate.getSellingRate();
            double left = (resShould * 1000)%1000;
            if(defaultRate.getCurrencyTypeId()==rate.getCurrencyTypeId()){
                iShouldPay = netT/defaultRate.getSellingRate();
            }
            else{
                if(resShould>0){
                    iShouldPay=iShouldPay+0.01 ;
                }
            }
            
            payAmountTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(iShouldPay));
            
            payAmountTextField.setEditable(true);
            payAmountTextField.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
            
        }
    }//GEN-LAST:event_paymentCurTypeComboBoxKeyPressed
    
    private void payTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_payTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdChangePaymentType();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            //paymentTable.requestFocusInWindow ();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_payTypeComboBoxKeyPressed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        PaymentDialog dialog = new PaymentDialog(new JFrame(), true);
        double val = 2430000;
        
        
        //System.out.println("parsed= "+DecimalFormat.
        
        dialog.setNetTrans(2430000);
        dialog.show();
    }
    
    
    public StandartRate getRateUsed() {
        if(rateUsed==null){
            rateUsed = new StandartRate();
        }
        return rateUsed;
    }
    /**
     * Setter for property rateUsed.
     * @param rateUsed New value of property rateUsed.
     */
    public void setRateUsed(StandartRate rateUsed) {
        this.rateUsed = rateUsed;
        try{
            CurrencyType currType = PstCurrencyType.fetchExc(rateUsed.getCurrencyTypeId());
            paymentCurTypeComboBox.setSelectedItem(currType.getCode());
            curTypeUsedLabel.setText(currType.getCode());
            paidCurTypeUsedLabel.setText(currType.getCode());
        }
        catch(Exception e){
            System.out.println("err on setRateUsed : "+e.toString());
        }
        
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cardCostLabel;
    private javax.swing.JTextField cardCostPctTextField;
    private javax.swing.JTextField cardCostTextField;
    private javax.swing.JComboBox cardDateComboBox;
    private javax.swing.JTextField cardHolderTextField;
    private javax.swing.JComboBox cardMonthComboBox;
    private javax.swing.JLabel cardNumLabel;
    private javax.swing.JTextField cardNumberTextField;
    private javax.swing.JComboBox cardTypeComboBox;
    private javax.swing.JLabel cardTypeLabel;
    private javax.swing.JComboBox cardYearComboBox;
    private javax.swing.JLabel cheqBankLabel11;
    private javax.swing.JTextField cheqBankTextField;
    private javax.swing.JComboBox cheqDateComboBox;
    private javax.swing.JLabel cheqExpDateLabel;
    private javax.swing.JLabel cheqHolderNameLabel;
    private javax.swing.JComboBox cheqMonthComboBox;
    private javax.swing.JTextField cheqNameTextField;
    private javax.swing.JComboBox cheqYearComboBox;
    private javax.swing.JPanel chequePanel;
    private javax.swing.JButton closeDialogButton;
    private javax.swing.JPanel creditCardPanel;
    private javax.swing.JLabel curTypeUsedLabel;
    private javax.swing.JLabel dbtBankLabel;
    private javax.swing.JTextField dbtBankTextField;
    private javax.swing.JLabel dbtCardNameLabel;
    private javax.swing.JTextField dbtNumTextField;
    private javax.swing.JPanel debitCardPanel;
    private javax.swing.JButton editTableButton;
    private javax.swing.JButton itemNewButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel netTransLabel;
    private javax.swing.JLabel paidCurTypeUsedLabel;
    private javax.swing.JLabel paidTransLabel;
    private javax.swing.JLabel payAmountLabel1;
    private javax.swing.JTextField payAmountTextField;
    private javax.swing.JComboBox payTypeComboBox;
    private javax.swing.JLabel payTypeLabel;
    private javax.swing.JPanel payTypePanel;
    private javax.swing.JComboBox paymentCurTypeComboBox;
    private javax.swing.JTable paymentTable;
    private javax.swing.JButton saveAllPaymentButton;
    // End of variables declaration//GEN-END:variables
    
}
