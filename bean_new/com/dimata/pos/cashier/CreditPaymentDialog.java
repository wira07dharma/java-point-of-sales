/*
 * PaymentDialog.java
 *
 * Created on December 31, 2004, 10:57 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.*;

/**
 * @author  wpradnyana
 *
 */

import com.dimata.pos.printAPI.PrintInvoicePOS;
import com.dimata.pos.session.processdata.SessTransactionData;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import com.dimata.util.Validator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



public class CreditPaymentDialog extends JDialog {
    
    // OWN MADE ATTRIBUTE GOES HERE....
    
    /** current active command */
    int iCommand = 0;
    
    
    /** field names */
    private static String [] fieldNames={
        "No","Date","Invoice Num","Method","Amount","Currency ","Rate ","Total "
    };
    
    /** net Transaction value */
    private double netTrans= 0;
    /** total payment value */
    private double allTotal = 0;
    
    /** column number constants */
    private static int COL_NO=0;
    private static int COL_PAYMENT_DATE=1;
    private static int COL_INVOICE_NUM=2;
    private static int COL_PAYMENT_METHOD=3;
    private static int COL_PAYMENT_AMOUNT=4;
    private static int COL_PAYMENT_CUR_USED=5;
    private static int COL_PAYMENT_RATE_USED=6;
    private static int COL_PAYMENT_TOTAL=7;
    
    public void cmdSetTableColumnSize(){
        int totalWidth = this.paymentTable.getWidth();
        
        this.paymentTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        this.paymentTable.getColumnModel().getColumn(COL_INVOICE_NUM).setPreferredWidth((int)(totalWidth*0.1));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_METHOD).setPreferredWidth((int)(totalWidth*0.1));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_AMOUNT).setPreferredWidth((int)(totalWidth*0.15));
        
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_CUR_USED).setPreferredWidth((int)(totalWidth*0.1));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_RATE_USED).setPreferredWidth((int)(totalWidth*0.1));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_TOTAL).setPreferredWidth((int)(totalWidth*0.25));
        this.paymentTable.getColumnModel().getColumn(COL_PAYMENT_DATE).setPreferredWidth((int)(totalWidth*0.25));
        
        this.paymentTable.revalidate();
        this.paymentTable.repaint();
    }
    
    /** currency hash table
     *  help resolving the problem in getting currency object
     *  by type or by OID
     */
    private Hashtable hashCurrencyType;
    private Hashtable hashCurrencyId;
    private Hashtable hashCurIDToCode = null;
    
    private Sales currentSales=null;
    private AppUser currentUserSales=null;
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
    
    private Hashtable lastPayments = new Hashtable();
    /** hash table that contains list of payment infos */
    private Hashtable lastPaymentsInfo = new Hashtable();
    
    private CreditPaymentModel saleModel = new CreditPaymentModel();
    
    private CashCreditPayments candidatePayment = new CashCreditPayments();
    private CashCreditPaymentInfo candidatePaymentInfo = new CashCreditPaymentInfo();
    // END OF OWN MADE ATTRIBUTE
    
    
    // OWN MADE METHODE GOES HERE .....
    public void initAllFields(){
        
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
    
    // month (java's month has values from 0 to 11)
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
    public void initField(){
        
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
        payTypeComboBox.setEnabled(false);
        /** init currency combo box */
        Vector hashCurr = new Vector(CashierMainApp.getHashCurrencyType().keySet());
        paymentCurTypeComboBox.setModel(new DefaultComboBoxModel(hashCurr));
        paymentCurTypeComboBox.setSelectedItem((String)CashierMainApp.getCurrencyCodeUsed());
        /** init netTransLabel */
        netTransLabel.setText(toCurrency(this.getNetTrans()));
        
        /** set the mnemonic for button n command */
        setMnenomic();
        salesNameTextField.setText("");
        invoiceNumberTextField.setText("");
        returInvTextField.setText("");
        custNameTextField.setText("");
        initState();
        returInvTextField.setEditable(false);
        custNameTextField.setEditable(false);
        showLastPaymentButton.setEnabled(false);
        editTableButton.setEnabled(false);
        saleTypeComboBox.setEnabled(true);
        
        /** set visibility of card cost field */
        cardCostLabel.setVisible(CashierMainApp.isEnableCardCost());
        cardCostTextField.setVisible(CashierMainApp.isEnableCardCost());
        cardCostPctTextField.setVisible(CashierMainApp.isEnableCardCost());
        
        saleTypeComboBox.requestFocusInWindow();
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
        itemAddButton.setEnabled(false);
        itemRemoveButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);
        
        
        payTypeComboBox.requestFocusInWindow();
    }
    
    /** set mnemonic for buttons in this dialog form */
    private void setMnenomic(){
        
        itemAddButton.setMnemonic('A');
        
        itemRemoveButton.setMnemonic('R');
        itemUpdateButton.setMnemonic('U');
        closeDialogButton.setMnemonic('X');
        itemNewButton.setMnemonic('N');
        showLastPaymentButton.setMnemonic('L');
        editTableButton.setMnemonic('E');
        saveAllPaymentButton.setMnemonic('S');
        lastTransactionItemsButton.setMnemonic('I');
        
    }
    
    /** Creates new form PaymentDialog */
    public CreditPaymentDialog(Frame parent, boolean modal) {
        super(parent, modal);
        setModal(true);
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
        netTransLabel.setText(toCurrency(netTrans));
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
                payAmountTextField.setEditable(true);
                payAmountTextField.setEditable(true);
                this.getCandidatePayment().setPaymentType(PstCashPayment.CASH);
                paymentCurTypeComboBox.setEnabled(true);
                paymentCurTypeComboBox.requestFocusInWindow();
                break;
            case PstCashPayment.CARD:
                cardTypeComboBox.setEnabled(true);
                this.getCandidatePayment().setPaymentType(PstCashPayment.CARD);
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
            double temp = Double.parseDouble(stInput);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    
    public double countLastPaymentTotal(){
        
        Vector lastValues = this.getSaleModel().getListLastPayment();
        int lastsize = lastValues.size();
        double lastTransTotal = 0;
        Vector tableLast = new Vector();
        for(int i=0;i<lastsize;i++){
            
            double tempTotal = 0;
            CashCreditPayments temp = (CashCreditPayments)lastValues.get(i);
            Vector tempVector = new Vector();
            tempVector.add(String.valueOf(i+1));
            String sPaymentMethod = PstCashCreditPayment.paymentType[temp.getPaymentType()];
            tempVector.add(sPaymentMethod);
            String sPaymentAmount = toCurrency(temp.getAmount());
            
            tempVector.add(toCurrency(temp.getAmount()));
            CurrencyType curType = (CurrencyType) this.getHashCurrencyId().get(new Long(temp.getCurrencyId()));
            tempVector.add(curType.getCode());
            tempVector.add(toCurrency(temp.getRate()));
            StandartRate rate = (StandartRate) this.getHashSellingRate().get(new Long(temp.getCurrencyId()));
            tempTotal = temp.getAmount() * rate.getSellingRate();
            tempVector.add(toCurrency(tempTotal));
            tempVector.add(String.valueOf(temp.getPayDateTime()));
            tableLast.add(tempVector);
            lastTransTotal = lastTransTotal + tempTotal;
        }
        return lastTransTotal ;
    }
    /** methode that used for synchronized table and its model
     *  also draw/redraw the table and control the navigation button
     */
    public void synchronizeTableAndModel(){
        this.getSaleModel().synchronizeAllValues();
        Vector values = this.getSaleModel().getListNewPayment();
        MemberReg member = new MemberReg();
        try{
            member = PstMemberReg.fetchExc(this.getSaleModel().getSaleRef().getCustomerId());
        }catch(Exception e){
            member.setPersonName("");
            member.setMemberBarcode("");
        }
        custNameTextField.setText(member.getPersonName());
        memberNameLabel.setText(member.getPersonName());
        memberCodeLabel.setText(member.getMemberBarcode());
        int size = values.size();
        double newTransTotal = 0;
        Vector tableNew = new Vector();
        // synchronize for each item in payments
        allTotal = 0;
        //lastTransTotal = alltotal;
        for(int i=0;i<size;i++){
            double tempTotal = 0;
            double costTotal = 0;
            CashCreditPayments temp = (CashCreditPayments)values.get(i);
            CashCreditPaymentInfo info = new CashCreditPaymentInfo();
            if(CashierMainApp.isNeedCardCost(temp.getPaymentType())){
                info = (CashCreditPaymentInfo) this.getPaymentsInfo().get(temp.getPayDateTime());
            }
            Vector tempVector = new Vector();
            
            tempVector.add(String.valueOf(i+1));
            String sPaymentMethod = PstCashCreditPayment.paymentType[temp.getPaymentType()];
            
            tempVector.add(Formater.formatDate(temp.getPayDateTime(),CashierMainApp.getDSJ_CashierXML().getConfig(0).fordate));
            tempVector.add(this.getSaleModel().getSaleRef().getInvoiceNumber());
            StandartRate rate = (StandartRate) this.getHashSellingRate().get(new Long(temp.getCurrencyId()));
            tempVector.add(sPaymentMethod);
            String sPaymentAmount = toCurrency(temp.getAmount()/rate.getSellingRate());
            String sCost = "";
            if(costTotal>0){
                sCost = " + "+toCurrency(info.getAmount()/info.getRate());
            }
            tempVector.add(sPaymentAmount+sCost);
            CurrencyType curType = (CurrencyType) this.getHashCurrencyId().get(new Long(temp.getCurrencyId()));
            tempVector.add(curType.getCode());
            tempVector.add(toCurrency(temp.getRate()));
            
            tempTotal = temp.getAmount()/ rate.getSellingRate();
            tempVector.add(toCurrency(temp.getAmount()));
            
            tableNew.add(tempVector);
            
        }
        
        //update button
        editTableButton.setEnabled(size>0);
        saveAllPaymentButton.setEnabled(size>0);
        
        // set the table model
        this.paymentTable.setModel(new DefaultTableModel(tableNew,this.getColumnIdentifier()){
            // add can't edit property
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        this.paymentTable.validate();
        cmdSetTableColumnSize();
        
        // update total payment
        this.setAllTotal(newTransTotal);
        lastPaymentTransLabel.setText(toCurrency(this.getSaleModel().getLastPaymentTotal()));
        paidTransLabel.setText(toCurrency(this.getSaleModel().getCurrentPaymentTotal()));
        netTransLabel.setText(toCurrency(this.getSaleModel().getLastTransTotal()));
        changeLabel.setText(toCurrency(this.getSaleModel().getChange()));
        newBalanceTransLabel.setText(toCurrency(this.getSaleModel().getPaymentBalanceTotal()));
        // if allTotal >= netTrans enable saveAllItemButton
        if(size>0){
            cmdNewPayment();
        }
        
        if(this.getSaleModel().getPaymentBalanceTotal()<=0){
            itemAddButton.setEnabled(false);
            saveAllPaymentButton.requestFocusInWindow();
            saveAllPaymentButton.setEnabled(true);
            
        }
        // else{
        //   saveAllPaymentButton.setEnabled(false);
        //   cmdNewPayment();
        // }
    }
    
    private String toCurrency(double dValue){
        //return Formater.formatNumber(dValue, currFormat[CURR_FORMAT_USED]);
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }
    /** finding the right CashPayments object that satisfied the current parameter
     * @return CashPayments
     * @parameter String currUsed, String payType
     */
    private CashCreditPayments findPaymentsWith(String curUsed,String payType){
        
        CashCreditPayments temp = null;
        CashCreditPayments foundPayment = null;
        double amount = 0;
        Enumeration e = this.getPayments().elements();
        boolean found  = false;
        long lCurUsed = 0;
        CurrencyType tempCur = (CurrencyType) this.getHashCurrencyType().get(curUsed);
        lCurUsed = tempCur.getOID();
        Integer IPayType = (Integer)this.getHashPayType().get(payType);
        int iPayType = IPayType.intValue();
        
        while(e.hasMoreElements()&&found==false){
            temp = (CashCreditPayments) e.nextElement();
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
    private CashCreditPayments findPaymentsWith(int payType,long currId){
        
        CashCreditPayments temp = null;
        CashCreditPayments foundPayment = null;
        boolean found = false;
        Enumeration e = this.getPayments().elements();
        
        while(e.hasMoreElements()&&found==false){
            temp = (CashCreditPayments) e.nextElement();
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
        CashCreditPaymentInfo paymentInfo = new CashCreditPaymentInfo();
        if(candidateType!=PstCashCreditPayment.CASH){
            switch(candidateType){
                case PstCashCreditPayment.CHEQUE:
                    paymentInfo.setChequeAccountName(cheqNameTextField.getText());
                    paymentInfo.setChequeBank(cheqBankTextField.getText());
                    paymentInfo.setChequeDueDate(new Date(
                    Integer.parseInt((String) cheqYearComboBox.getSelectedItem())-1900,
                    cheqMonthComboBox.getSelectedIndex(),
                    cheqDateComboBox.getSelectedIndex()+1));
                    break;
                case PstCashCreditPayment.CARD:
                    paymentInfo.setCcName((String)cardTypeComboBox.getSelectedItem());
                    paymentInfo.setCcNumber(cardNumberTextField.getText());
                    paymentInfo.setHolderName(cardHolderTextField.getText());
                    paymentInfo.setExpiredDate(new Date(
                    Integer.parseInt((String) cardYearComboBox.getSelectedItem())-1900,
                    cardMonthComboBox.getSelectedIndex(),
                    cardDateComboBox.getSelectedIndex()+1));
                    break;
                case PstCashCreditPayment.DEBIT:
                    paymentInfo.setDebitBankName(dbtBankTextField.getText());
                    paymentInfo.setDebitCardName(dbtNumTextField.getText());
                    break;
                    
            }
            if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(candidateType)){
                paymentInfo.setCurrencyId(longCurrId);
                paymentInfo.setRate(srate.getSellingRate());
                amount = CashierMainApp.getDoubleFromFormated(cardCostTextField.getText());
                paymentInfo.setAmount(amount*srate.getSellingRate());
            }
        }
        
        boolean itemAlreadyInserted = false;
        
        /*CashCreditPayments tempPayment = findPaymentsWith (candidateType,longCurrId);
        if(tempPayment!=null){
            itemAlreadyInserted = true;
        }
        if(itemAlreadyInserted){
            tempPayment.setAmount (tempPayment.getAmount ()+candidatePayment.getAmount ());
            candidatePayment = tempPayment;
        }*/
        paymentInfo.setUpdateStatus(PstCashCreditPayment.UPDATE_STATUS_INSERTED);
        candidatePayment.setUpdateStatus(PstCashCreditPayment.UPDATE_STATUS_INSERTED);
        //this.getSaleModel ().getListNewPaymentInfo ().put (candidatePayment.getPayDateTime (), paymentInfo);
        this.getSaleModel().insertPaymentWith(candidatePayment, paymentInfo);
        //this.getSaleModel ().getListNewPaymentInfo ().put (candidatePayment.getPayDateTime (), candidatePayment);
        cmdNewPayment();
        synchronizeTableAndModel();
    }
    
    /** Init for new payment */
    private void cmdNewPayment(){
        
        this.setCandidatePayment(null);
        this.getCandidatePayment();
        this.getCandidatePaymentInfo();
        this.setICommand(Command.ADD);
        initState();
    }
    
    /** Load selected row to editor */
    private void cmdLoadPaymentInfo(){
        
        this.setICommand(Command.EDIT);
        int selectedRow = this.paymentTable.getSelectedRow();
        
        String curType = (String)this.paymentTable.getValueAt(selectedRow, COL_PAYMENT_CUR_USED);
        String payType = (String)this.paymentTable.getValueAt(selectedRow, COL_PAYMENT_METHOD);
        //int index = this.getSaleModel ().fi
        CashCreditPayments temp = (CashCreditPayments)this.getSaleModel().getListNewPayment().get(selectedRow);
        boolean itemFound = false;
        CashCreditPaymentInfo card = (CashCreditPaymentInfo) this.getSaleModel().getListNewPaymentInfo().get(selectedRow);
        if(temp!=null)
            itemFound = true;
        StandartRate srate = CashSaleController.getLatestRate(String.valueOf(temp.getCurrencyId()));
        if(itemFound) {
            payTypeComboBox.setSelectedIndex(temp.getPaymentType());
            paymentCurTypeComboBox.setSelectedItem(curType);
            double amount = temp.getAmount()/srate.getSellingRate();
            payAmountTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(amount));
            payAmountTextField.setEditable(true);
            payAmountTextField.requestFocusInWindow();
            //if(temp.getPaymentType ()!=PstCashPayment.CASH){
            
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
            //}
            
            this.setCandidatePayment(temp);
            
            itemRemoveButton.setEnabled(true);
            itemUpdateButton.setEnabled(true);
        }
    }
    
    /** delete selected item */
    private void cmdItemDelete(){
        cmdLoadPaymentInfo();
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure to delete this item?","Deleting items",JOptionPane.YES_NO_OPTION);
        
        if(answer==JOptionPane.YES_OPTION){
            this.getSaleModel().deletePaymentWith(this.getCandidatePayment(),this.getCandidatePaymentInfo());
            //this.getSaleModel ().getListLastPayment().remove (this.getCandidatePayment ());
            //this.getSaleModel ().getListLastPaymentInfo ().remove (this.getCandidatePayment ());
            synchronizeTableAndModel();
            
        }else{
            cmdNewPayment();
        }
        
    }
    
    /** update current selected row */
    private void cmdItemUpdate() throws NumberFormatException{
        
        //this.getSaleModel ().getListLastPayment().remove (this.getCandidatePayment ());
        //this.getSaleModel ().getListLastPaymentInfo ().remove (this.getCandidatePayment ());
        this.getSaleModel().deletePaymentWith(this.getCandidatePayment(),this.getCandidatePaymentInfo());
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
        CashCreditPaymentInfo paymentInfo = new CashCreditPaymentInfo();
        //if(candidateType!=PstCashPayment.CASH){
        switch(candidateType){
            case PstCashPayment.CHEQUE:
                paymentInfo.setChequeAccountName(cheqNameTextField.getText());
                paymentInfo.setChequeBank(cheqBankTextField.getText());
                paymentInfo.setChequeDueDate(new Date(
                Integer.parseInt((String) cheqYearComboBox.getSelectedItem())-1900,
                cheqMonthComboBox.getSelectedIndex(),
                cheqDateComboBox.getSelectedIndex()+1));
                if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(candidateType)){
                    paymentInfo.setCurrencyId(longCurrId);
                    paymentInfo.setRate(srate.getSellingRate());
                    amount = CashierMainApp.getDoubleFromFormated(cardCostTextField.getText());
                    paymentInfo.setAmount(amount*srate.getSellingRate());
                }
                break;
            case PstCashPayment.CARD:
                paymentInfo.setCcName(cardHolderTextField.getText());
                paymentInfo.setCcNumber(cardNumberTextField.getText());
                paymentInfo.setExpiredDate(new Date(
                Integer.parseInt((String) cardYearComboBox.getSelectedItem()),
                cardMonthComboBox.getSelectedIndex(),
                cardDateComboBox.getSelectedIndex()+1));
                if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(candidateType)){
                    paymentInfo.setCurrencyId(longCurrId);
                    paymentInfo.setRate(srate.getSellingRate());
                    amount = CashierMainApp.getDoubleFromFormated(cardCostTextField.getText());
                    paymentInfo.setAmount(amount*srate.getSellingRate());
                }
                break;
            case PstCashPayment.DEBIT:
                paymentInfo.setDebitBankName(dbtBankTextField.getText());
                paymentInfo.setDebitCardName(dbtNumTextField.getText());
                if(CashierMainApp.isEnableCardCost() && CashierMainApp.isNeedCardCost(candidateType)){
                    paymentInfo.setCurrencyId(longCurrId);
                    paymentInfo.setRate(srate.getSellingRate());
                    amount = CashierMainApp.getDoubleFromFormated(cardCostTextField.getText());
                    paymentInfo.setAmount(amount*srate.getSellingRate());
                }
                break;
        }
        //}
        
        this.getSaleModel().insertPaymentWith(candidatePayment, paymentInfo);
        //this.getSaleModel ().getListLastPaymentInfo ().put(candidatePayment.getPayDateTime(), paymentInfo);
        //this.getSaleModel ().getListLastPayment().put (candidatePayment.getPayDateTime(), candidatePayment);
        
        
        synchronizeTableAndModel();
        
    }
    
    /** save all item in the table
     *  prereq : at least one item in the table
     */
    private void cmdSaveAll(){
        try {
            SessTransactionData.putCreditPaymentData(this.getSaleModel());
        } catch (DBException ex) {
            Logger.getLogger(CreditPaymentDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        int answer = JOptionPane.showConfirmDialog(this,"Print Transaction?","Printing",JOptionPane.YES_NO_OPTION);
        if(answer==JOptionPane.YES_OPTION){
            cmdPrint();
        }
        //CashSaleController.setPaymentSet (this.getPayments ());
        //CashSaleController.setPaymentInfoSet (this.getPaymentsInfo ());
        //this.dispose ();
        cmdNewTrans();
        
    }
    
    public void cmdNewTrans(){
        
        this.setSaleModel(null);
        this.setCurrentSales(null);
        this.setCurrentSales(this.getCurrentSales());
        this.setSaleModel(this.getSaleModel());
        this.setCandidatePayment(null);
        this.setCandidatePayment(this.getCandidatePayment());
        this.setCandidatePaymentInfo(null);
        this.setCandidatePaymentInfo(this.getCandidatePaymentInfo());
        synchronizeTableAndModel();
        this.initField();
        
        
        
    }
    private void cmdPrint(){
        PrintInvoicePOS print = new PrintInvoicePOS();
        if(CashierMainApp.isUsingBigInvoice()){
            print.printBigCreditPaymentObj(this.getSaleModel());
        }
        else{
            print.printCreditPaymentObj(this.getSaleModel());
        }
    }
    //  END OF OWN MADE METHODE
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lastPaymentLabel = new javax.swing.JLabel();
        lastTransactionItemsButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        salesNameTextField = new javax.swing.JTextField();
        itemUpdateButton = new javax.swing.JButton();
        itemRemoveButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
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
        payTypePanel = new javax.swing.JPanel();
        payTypeLabel = new javax.swing.JLabel();
        payTypeComboBox = new javax.swing.JComboBox();
        payAmountLabel = new javax.swing.JLabel();
        payAmountTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        paymentCurTypeComboBox = new javax.swing.JComboBox();
        cardCostTextField = new javax.swing.JTextField();
        cardCostLabel = new javax.swing.JLabel();
        cardCostPctTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        curTypeUsedLabel = new javax.swing.JLabel();
        netTransLabel = new javax.swing.JLabel();
        curTypeUsedLabel2 = new javax.swing.JLabel();
        paidTransLabel = new javax.swing.JLabel();
        newBalanceTransLabel = new javax.swing.JLabel();
        paidCurTypeUsedLabel = new javax.swing.JLabel();
        lastPaymentTransLabel = new javax.swing.JLabel();
        curTypeUsedLabel1 = new javax.swing.JLabel();
        lastTrans = new javax.swing.JLabel();
        lastPaid = new javax.swing.JLabel();
        newPayment = new javax.swing.JLabel();
        newBalance = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        memberNameLabel = new javax.swing.JLabel();
        memberCodeLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        changeTextLabel = new javax.swing.JLabel();
        curTypeUsedLabel3 = new javax.swing.JLabel();
        changeLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        paymentTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        itemAddButton = new javax.swing.JButton();
        saveAllPaymentButton = new javax.swing.JButton();
        itemNewButton = new javax.swing.JButton();
        closeDialogButton = new javax.swing.JButton();
        showLastPaymentButton = new javax.swing.JButton();
        editTableButton = new javax.swing.JButton();
        saleTypePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        saleTypeComboBox = new javax.swing.JComboBox();
        returInvTextField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        invoiceNumberTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        custNameTextField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();

        jLabel5.setText("Last Paid");
        jPanel5.add(jLabel5);

        jLabel7.setText("Rp.");
        jPanel5.add(jLabel7);

        lastPaymentLabel.setText("0.00");
        jPanel5.add(lastPaymentLabel);

        lastTransactionItemsButton.setText("Invoice Items -Alt I");
        lastTransactionItemsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastTransactionItemsButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Sales Name");
        salesNameTextField.setColumns(5);
        salesNameTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        salesNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesNameTextFieldActionPerformed(evt);
            }
        });

        itemUpdateButton.setText("Update");
        itemUpdateButton.setEnabled(false);
        itemUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemUpdateButtonActionPerformed(evt);
            }
        });
        itemUpdateButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemUpdateButtonKeyPressed(evt);
            }
        });

        itemRemoveButton.setText("Remove");
        itemRemoveButton.setEnabled(false);
        itemRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRemoveButtonActionPerformed(evt);
            }
        });
        itemRemoveButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemRemoveButtonKeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Credit Payment");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentShown(evt);
            }
        });

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel10.setBorder(new javax.swing.border.TitledBorder(""));
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel10.add(creditCardPanel, gridBagConstraints);

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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(chequePanel, gridBagConstraints);

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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel10.add(debitCardPanel, gridBagConstraints);

        payTypePanel.setLayout(new java.awt.GridBagLayout());

        payTypePanel.setBorder(new javax.swing.border.TitledBorder("Payment Type"));
        payTypeLabel.setText("Payment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(payTypeLabel, gridBagConstraints);

        payTypeComboBox.setEnabled(false);
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

        payAmountLabel.setText("Payment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(payAmountLabel, gridBagConstraints);

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

        cardCostLabel.setText("Card Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        payTypePanel.add(cardCostLabel, gridBagConstraints);

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        jPanel10.add(payTypePanel, gridBagConstraints);

        jPanel1.add(jPanel10);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel2.setFont(new java.awt.Font("MS Sans Serif", 0, 24));
        curTypeUsedLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        curTypeUsedLabel.setText("Rp.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(curTypeUsedLabel, gridBagConstraints);

        netTransLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        netTransLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTransLabel.setText("0.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(netTransLabel, gridBagConstraints);

        curTypeUsedLabel2.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        curTypeUsedLabel2.setText("Rp.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(curTypeUsedLabel2, gridBagConstraints);

        paidTransLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        paidTransLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paidTransLabel.setText("0.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(paidTransLabel, gridBagConstraints);

        newBalanceTransLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        newBalanceTransLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        newBalanceTransLabel.setText("0.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(newBalanceTransLabel, gridBagConstraints);

        paidCurTypeUsedLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        paidCurTypeUsedLabel.setText("Rp.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(paidCurTypeUsedLabel, gridBagConstraints);

        lastPaymentTransLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        lastPaymentTransLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lastPaymentTransLabel.setText("0.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(lastPaymentTransLabel, gridBagConstraints);

        curTypeUsedLabel1.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        curTypeUsedLabel1.setText("Rp.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(curTypeUsedLabel1, gridBagConstraints);

        lastTrans.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        lastTrans.setText("Total Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(lastTrans, gridBagConstraints);

        lastPaid.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        lastPaid.setText("Total Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(lastPaid, gridBagConstraints);

        newPayment.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        newPayment.setText("Total Payments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(newPayment, gridBagConstraints);

        newBalance.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        newBalance.setText("New Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(newBalance, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        jLabel9.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel9, gridBagConstraints);

        memberNameLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        memberNameLabel.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(memberNameLabel, gridBagConstraints);

        memberCodeLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        memberCodeLabel.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(memberCodeLabel, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        jLabel12.setText("Member Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel12, gridBagConstraints);

        changeTextLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        changeTextLabel.setText("Change");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(changeTextLabel, gridBagConstraints);

        curTypeUsedLabel3.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        curTypeUsedLabel3.setText("Rp.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(curTypeUsedLabel3, gridBagConstraints);

        changeLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        changeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        changeLabel.setText("0.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(changeLabel, gridBagConstraints);

        jPanel1.add(jPanel2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(771, 247));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(new javax.swing.border.TitledBorder(""));
        paymentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Payment Date", "Invoice Number", "Payment Method", "Payment Amount", "Currency Type Used", "Rate Used"
            }
        ));
        paymentTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paymentTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(paymentTable);

        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel13.setLayout(new java.awt.GridBagLayout());

        jPanel13.setBorder(new javax.swing.border.TitledBorder(""));
        itemAddButton.setText("Add Payment - F2");
        itemAddButton.setEnabled(false);
        itemAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAddButtonActionPerformed(evt);
            }
        });
        itemAddButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemAddButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(itemAddButton, gridBagConstraints);

        saveAllPaymentButton.setText("Save All - F12");
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(saveAllPaymentButton, gridBagConstraints);

        itemNewButton.setText("New Transaction - F5");
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(itemNewButton, gridBagConstraints);

        closeDialogButton.setText("Close Frame - Alt+X");
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(closeDialogButton, gridBagConstraints);

        showLastPaymentButton.setText(" Payment History - F6");
        showLastPaymentButton.setEnabled(false);
        showLastPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showLastPaymentButtonActionPerformed(evt);
            }
        });
        showLastPaymentButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                showLastPaymentButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(showLastPaymentButton, gridBagConstraints);

        editTableButton.setText("Edit Table - F4");
        editTableButton.setEnabled(false);
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel13.add(editTableButton, gridBagConstraints);

        jPanel3.add(jPanel13, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        saleTypePanel.setLayout(new java.awt.GridBagLayout());

        saleTypePanel.setBorder(new javax.swing.border.TitledBorder(""));
        saleTypePanel.setAutoscrolls(true);
        jLabel2.setText("Trans Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(jLabel2, gridBagConstraints);

        saleTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Credit Payment" }));
        saleTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleTypeComboBoxActionPerformed(evt);
            }
        });
        saleTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saleTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(saleTypeComboBox, gridBagConstraints);

        returInvTextField.setColumns(10);
        returInvTextField.setEditable(false);
        returInvTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returInvTextFieldActionPerformed(evt);
            }
        });
        returInvTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                returInvTextFieldFocusGained(evt);
            }
        });
        returInvTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                returInvTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(returInvTextField, gridBagConstraints);

        jLabel22.setText("Ref Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(jLabel22, gridBagConstraints);

        invoiceNumberTextField.setColumns(10);
        invoiceNumberTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        saleTypePanel.add(invoiceNumberTextField, gridBagConstraints);

        jLabel8.setText("Invoice Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        saleTypePanel.add(jLabel8, gridBagConstraints);

        custNameTextField.setColumns(15);
        custNameTextField.setEditable(false);
        custNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custNameTextFieldActionPerformed(evt);
            }
        });
        custNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                custNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(custNameTextField, gridBagConstraints);

        jLabel23.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(jLabel23, gridBagConstraints);

        getContentPane().add(saleTypePanel, java.awt.BorderLayout.NORTH);

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
    
    private void custNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custNameTextFieldActionPerformed
        cmdSearchInvoice();
    }//GEN-LAST:event_custNameTextFieldActionPerformed
    
    private void returInvTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_returInvTextFieldFocusGained
        returInvTextField.selectAll();
    }//GEN-LAST:event_returInvTextFieldFocusGained
    
    private void custNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_custNameTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_custNameTextFieldKeyPressed
    
    private void showLastPaymentButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_showLastPaymentButtonKeyPressed
    {//GEN-HEADEREND:event_showLastPaymentButtonKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_showLastPaymentButtonKeyPressed
    
    private void closeDialogButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_closeDialogButtonKeyPressed
    {//GEN-HEADEREND:event_closeDialogButtonKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_closeDialogButtonKeyPressed
    
    private void editTableButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_editTableButtonKeyPressed
    {//GEN-HEADEREND:event_editTableButtonKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_editTableButtonKeyPressed
    
    private void cheqBankTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_cheqBankTextFieldKeyPressed
    {//GEN-HEADEREND:event_cheqBankTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cheqBankTextFieldKeyPressed
    
    private void cheqNameTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_cheqNameTextFieldKeyPressed
    {//GEN-HEADEREND:event_cheqNameTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cheqNameTextFieldKeyPressed
    
    private void dbtNumTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_dbtNumTextFieldKeyPressed
    {//GEN-HEADEREND:event_dbtNumTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_dbtNumTextFieldKeyPressed
    
    private void dbtBankTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_dbtBankTextFieldKeyPressed
    {//GEN-HEADEREND:event_dbtBankTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_dbtBankTextFieldKeyPressed
    
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
    
    private void formKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyPressed
    {//GEN-HEADEREND:event_formKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed
    
    private void formWindowOpened (java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowOpened
    {//GEN-HEADEREND:event_formWindowOpened
        // TODO add your handling code here:
        saleTypeComboBox.requestFocusInWindow();
        //cmdNewTrans ();
    }//GEN-LAST:event_formWindowOpened
    
    private void lastTransactionItemsButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_lastTransactionItemsButtonActionPerformed
    {//GEN-HEADEREND:event_lastTransactionItemsButtonActionPerformed
        // TODO add your handling code here:
        cmdLastInvoiceItems();
    }//GEN-LAST:event_lastTransactionItemsButtonActionPerformed
    
    private void cmdLastInvoiceItems(){
        //CashSaleController.set
        CashSaleController.showInvoiceItemSearch(this, this.getSaleModel().getSaleRef().getInvoiceNumber());
        
    }
    private void editTableButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_editTableButtonActionPerformed
    {//GEN-HEADEREND:event_editTableButtonActionPerformed
        // TODO add your handling code here:
        paymentTable.requestFocusInWindow();
        if(paymentTable.getModel().getRowCount()>0) {
            paymentTable.setRowSelectionInterval(0,0);
        }
    }//GEN-LAST:event_editTableButtonActionPerformed
    
    private void jPanel1ComponentShown (java.awt.event.ComponentEvent evt)//GEN-FIRST:event_jPanel1ComponentShown
    {//GEN-HEADEREND:event_jPanel1ComponentShown
        // TODO add your handling code here:
        this.cmdNewTrans();
    }//GEN-LAST:event_jPanel1ComponentShown
    
    private void showLastPaymentButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_showLastPaymentButtonActionPerformed
    {//GEN-HEADEREND:event_showLastPaymentButtonActionPerformed
        cmdShowLastPayment();
        
    }//GEN-LAST:event_showLastPaymentButtonActionPerformed
    
    public void cmdShowLastPayment(){
        CashSaleController.showCreditPaymentHistoryDialog(this, this.getSaleModel().getSaleRef().getInvoiceNumber());
    }
    private void closeDialogButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeDialogButtonActionPerformed
    {//GEN-HEADEREND:event_closeDialogButtonActionPerformed
        // TODO add your handling code here:
        cmdClose();
    }//GEN-LAST:event_closeDialogButtonActionPerformed
    
    private void cmdClose(){
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure want to close this transaction?","Closing transaction",JOptionPane.OK_CANCEL_OPTION);
        if(answer==JOptionPane.OK_OPTION){
            this.setSaleModel(null);
            this.dispose();
        }
    }
    private void saleTypeComboBoxKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_saleTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_saleTypeComboBoxKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdChangeTrans();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_saleTypeComboBoxKeyPressed
    
    private void cmdChangeTrans(){
        int transTypeChoosen = this.saleTypeComboBox.getSelectedIndex();
        String selectedObject= (String)this.saleTypeComboBox.getSelectedItem();
        //Invoice", "Invoice Ret", "Open Bill", "Credit Sales", "Cost", "Compliment"
        //if(selectedObject.equals("Invoice")){
        transTypeChoosen = PstCreditPaymentMain.TYPE_CREDIT_PAYMENT;
        //}else if(selectedObject.equals("Invoice Ret")){
        //   transTypeChoosen = PstBillMain.TYPE_RETUR;
        //}else if(selectedObject.equals("Cost")){
        //    transTypeChoosen = PstBillMain.TYPE_COST;
        //}else if(selectedObject.equals("Compliment")){
        //    transTypeChoosen = PstBillMain.TYPE_COMPLIMENT;
        //}
        
        
        switch(transTypeChoosen){
            case PstCreditPaymentMain.TYPE_CREDIT_PAYMENT:
                this.getSaleModel().getMainPayment().setDocType(PstCreditPaymentMain.TYPE_CREDIT_PAYMENT);
                returInvTextField.setEditable(true);
                returInvTextField.requestFocusInWindow();
                break;
                
            default:
                break;
        }
        this.initAllFields();
        this.createNewTrans(transTypeChoosen);
        saleTypeComboBox.setSelectedIndex(transTypeChoosen);
        invoiceNumberTextField.setText(this.getSaleModel().getMainPayment().getInvoiceNumber());
        //saleDateTextField.setText(Formater.formatDate(this.getSaleModel().getMainSale().getBillDate(),CashierMainApp.getDSJ_CashierXML().getConfig(0).fordate));
        //cashierNumberTextField.setText(""+CashierMainApp.getCashMaster().getCashierNumber());
        salesNameTextField.setEditable(false);
        //salesNameTextField.requestFocusInWindow();
        //
        
        returInvTextField.setEditable(true);
        returInvTextField.requestFocusInWindow();
        
    }
    private void createNewTrans(int transTypeChoosen){
        this.setSaleModel(null);
        this.setSaleModel(this.getSaleModel());
        this.getSaleModel().getMainPayment().setAppUserSalesId(this.getCurrentUserSales().getOID());
//        this.getSaleModel().getMainPayment().setSalesCode(this.getCurrentSales().getCode());
        this.getSaleModel().setTransType(transTypeChoosen);
        
    }
    private void salesNameTextFieldActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_salesNameTextFieldActionPerformed
    {//GEN-HEADEREND:event_salesNameTextFieldActionPerformed
        // TODO add your handling code here:
        cmdSalesSearch();
    }//GEN-LAST:event_salesNameTextFieldActionPerformed
    
    private void cmdSalesSearch(){
        /** edited by wpulantara for sales searching */
        Sales sales;
        Vector rs = CashSaleController.getSales(0,0,salesNameTextField.getText(),"");
        if(rs.size()>0){
            if(rs.size()==1){
                sales = (Sales) rs.get(0);
            }
            else{
                CashSaleController.showSalesSearch(this,salesNameTextField.getText(), "");
                sales = CashSaleController.getSalesChoosen();
            }
            this.setCurrentSales(sales);
            salesNameTextField.setText(sales.getName());
            
            saleTypeComboBox.setEnabled(true);
            saleTypeComboBox.requestFocusInWindow();
        }
        else{
            JOptionPane.showMessageDialog(this,"Sales person not found","Sales person",JOptionPane.ERROR_MESSAGE);
            salesNameTextField.requestFocusInWindow();
        }
        
        // OLD CODE GOES HERE
        
        /*
        Sales sales = CashSaleController.getSalesByCode(salesNameTextField.getText());
        if(sales==null){
            JOptionPane.showMessageDialog(this,"Sales person not found","Sales person",JOptionPane.ERROR_MESSAGE);
            salesNameTextField.requestFocusInWindow();
        }else{
            this.setCurrentSales (sales);
            salesNameTextField.setText(sales.getName());
         
            saleTypeComboBox.setEnabled (true);
            saleTypeComboBox.requestFocusInWindow ();
        }
        saleTypeComboBox.requestFocusInWindow ();
         */
    }
    private void returInvTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_returInvTextFieldKeyPressed
    {//GEN-HEADEREND:event_returInvTextFieldKeyPressed
        
        getGlobalKeyListener(evt);
        
    }//GEN-LAST:event_returInvTextFieldKeyPressed
    
    private void returInvTextFieldActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_returInvTextFieldActionPerformed
    {//GEN-HEADEREND:event_returInvTextFieldActionPerformed
        if(returInvTextField.getText().length()>0)
            cmdSearchInvoice();
        else{
            custNameTextField.setEditable(true);
            custNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_returInvTextFieldActionPerformed
    
    private void cmdSearchInvoice(){
        CashSaleController.setCreditSaleChoosen(null);
        CashSaleController.showCreditSalesSearch(this, returInvTextField.getText(), this.custNameTextField.getText());
        Vector vctInvChoos = CashSaleController.getCreditSaleChoosen();
        if(vctInvChoos.size()==1){
            BillMain billMain = (BillMain)vctInvChoos.get(0);
            if(billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
                DefaultSaleModel model = SessTransactionData.getData(billMain);
                this.getSaleModel().transferFromSale(model);
                //this.getSaleModel ().synchronizeAllValues ();
                if(this.getSaleModel().getPaymentBalanceTotal()<=0){
                    JOptionPane.showMessageDialog(this,"This invoice has fullpaid.","Fullpaid Invoice",JOptionPane.WARNING_MESSAGE);
                    cmdNewTrans();
                }else{
                    //this.setSaleModel (creditModel);
                    returInvTextField.setText(this.getSaleModel().getSaleRef().getInvoiceNumber());
                    synchronizeTableAndModel();
                    showLastPaymentButton.setEnabled(true);
                    payTypeComboBox.setEnabled(true);
                    payTypeComboBox.requestFocusInWindow();
                }
            }else{
                JOptionPane.showMessageDialog(this,"Sorry,choosen invoice has been closed!","Closed Invoice ",JOptionPane.ERROR_MESSAGE);
                returInvTextField.requestFocusInWindow();
            }
        }
        else{
            // no luck, no result =D
            returInvTextField.requestFocusInWindow();
        }
    }
    private void saleTypeComboBoxActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_saleTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_saleTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saleTypeComboBoxActionPerformed
    
    private void saveAllPaymentButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveAllPaymentButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdSaveAll();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_saveAllPaymentButtonKeyPressed
    
    private void saveAllPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllPaymentButtonActionPerformed
        cmdSaveAll();
    }//GEN-LAST:event_saveAllPaymentButtonActionPerformed
    
    private void itemUpdateButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemUpdateButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdItemUpdate();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_itemUpdateButtonKeyPressed
    
    private void itemUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemUpdateButtonActionPerformed
        cmdItemUpdate();
    }//GEN-LAST:event_itemUpdateButtonActionPerformed
    
    private void itemRemoveButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemRemoveButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdItemDelete();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_itemRemoveButtonKeyPressed
    
    private void itemRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRemoveButtonActionPerformed
        cmdItemDelete();
    }//GEN-LAST:event_itemRemoveButtonActionPerformed
    
    private void paymentTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentTableKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdLoadPaymentInfo();
            setICommand(Command.EDIT);
        }else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
            cmdItemDelete();
            setICommand(Command.ADD);
        }else{
            getGlobalKeyListener(evt);
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
        //this.cmdNewPayment();
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure wanto close this transaction?","Closing transaction",JOptionPane.YES_NO_OPTION);
        if(answer==JOptionPane.YES_OPTION){
            this.cmdNewTrans();
        }else if(answer==JOptionPane.NO_OPTION){
            
        }
    }//GEN-LAST:event_itemNewButtonActionPerformed
    
    private void itemAddButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemAddButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            //cmdItemAdd();
            cmdNewPayment();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_itemAddButtonKeyPressed
    
    private void itemAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAddButtonActionPerformed
        //cmdItemAdd();
    }//GEN-LAST:event_itemAddButtonActionPerformed
    
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
        }
    }//GEN-LAST:event_cheqNameTextFieldActionPerformed
    
    private void dbtNumTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbtNumTextFieldActionPerformed
        if(dbtNumTextField.getText().length()>0){
            paymentCurTypeComboBox.setEnabled(true);
            paymentCurTypeComboBox.requestFocusInWindow();
        }
    }//GEN-LAST:event_dbtNumTextFieldActionPerformed
    
    private void dbtBankTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbtBankTextFieldActionPerformed
        if(dbtBankTextField.getText().length()>0){
            dbtNumTextField.setEditable(true);
            dbtNumTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_dbtBankTextFieldActionPerformed
    
    private void cardYearComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardYearComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            paymentCurTypeComboBox.setEnabled(true);
            paymentCurTypeComboBox.requestFocusInWindow();
        }else{
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
        }
    }//GEN-LAST:event_cardHolderTextFieldActionPerformed
    
    private void cardNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardNumberTextFieldActionPerformed
        if(cardNumberTextField.getText().length()>0){
            cardHolderTextField.setEditable(true);
            cardHolderTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_cardNumberTextFieldActionPerformed
    
    private void paymentCurTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentCurTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            double netT = this.getSaleModel().getPaymentBalanceTotal();
            String selectedCur = (String)paymentCurTypeComboBox.getSelectedItem();
            CurrencyType type = (CurrencyType)CashierMainApp.getHashCurrencyType().get(selectedCur);
            StandartRate rate = (StandartRate)CashSaleController.getLatestRate(String.valueOf(type.getOID()));
            //StandartRate defaultRate = this.getSaleModel().getRateUsed();
            double iShouldPay = netT/rate.getSellingRate();
            double resShould = netT%rate.getSellingRate();
            double left = (resShould * 1000)%1000;
            if(resShould>0){
                //iShouldPay=iShouldPay+1;//(netT+rate.getSellingRate ())/rate.getSellingRate () ;
                //iShouldPay=(iShouldPay*100+1)/100; //(netT+rate.getSellingRate ())/rate.getSellingRate () ;
                iShouldPay=iShouldPay+0.01;
            }
            
            //double shouldPay = netT/rate.getSellingRate ()+1;
            //payAmountTextField.setText(Formater.formatNumber (iShouldPay,"###.###"));
            payAmountTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(iShouldPay));
            payAmountTextField.setEditable(true);
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
            paymentTable.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_payTypeComboBoxKeyPressed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        CreditPaymentDialog dialog = new CreditPaymentDialog(new JFrame(), true);
        //dialog.setNetTrans(10000);
        dialog.show();
    }
    
    /**
     * Getter for property saleModel.
     * @return Value of property saleModel.
     */
    public com.dimata.pos.cashier.CreditPaymentModel getSaleModel() {
        if(saleModel==null){
            saleModel = new CreditPaymentModel();
        }
        return saleModel;
    }
    
    /**
     * Setter for property saleModel.
     * @param saleModel New value of property saleModel.
     */
    public void setSaleModel(com.dimata.pos.cashier.CreditPaymentModel saleModel) {
        this.saleModel = saleModel;
    }
    
    /**
     * Getter for property candidateCreditPayment.
     * @return Value of property candidateCreditPayment.
     */
    public CashCreditPayments getCandidatePayment() {
        if(candidatePayment==null){
            candidatePayment = new CashCreditPayments();
        }
        return candidatePayment;
    }
    
    /**
     * Setter for property candidateCreditPayment.
     * @param candidateCreditPayment New value of property candidateCreditPayment.
     */
    public void setCandidatePayment(CashCreditPayments candidatePayment) {
        this.candidatePayment = candidatePayment;
    }
    
    /**
     * Getter for property candidatePaymentInfo.
     * @return Value of property candidatePaymentInfo.
     */
    public CashCreditPaymentInfo getCandidatePaymentInfo() {
        if(candidatePaymentInfo==null){
            candidatePaymentInfo = new CashCreditPaymentInfo();
        }
        return candidatePaymentInfo;
    }
    
    /**
     * Setter for property candidatePaymentInfo.
     * @param candidatePaymentInfo New value of property candidatePaymentInfo.
     */
    public void setCandidatePaymentInfo(CashCreditPaymentInfo candidatePaymentInfo) {
        this.candidatePaymentInfo = candidatePaymentInfo;
    }
    
    /**
     * Getter for property lastPayments.
     * @return Value of property lastPayments.
     */
    public Hashtable getLastPayments() {
        if(lastPayments==null){
            lastPayments = new Hashtable();
        }
        return lastPayments;
    }
    
    /**
     * Setter for property lastPayments.
     * @param lastPayments New value of property lastPayments.
     */
    public void setLastPayments(Hashtable lastPayments) {
        this.lastPayments = lastPayments;
    }
    
    /**
     * Getter for property lastPaymentsInfo.
     * @return Value of property lastPaymentsInfo.
     */
    public Hashtable getLastPaymentsInfo() {
        if(lastPaymentsInfo==null){
            lastPaymentsInfo = new Hashtable();
        }
        return lastPaymentsInfo;
    }
    
    /**
     * Setter for property lastPaymentsInfo.
     * @param lastPaymentsInfo New value of property lastPaymentsInfo.
     */
    public void setLastPaymentsInfo(Hashtable lastPaymentsInfo) {
        this.lastPaymentsInfo = lastPaymentsInfo;
    }
    
    /**
     * Getter for property currentSales.
     * @return Value of property currentSales.
     */
    public Sales getCurrentSales() {
        if(currentSales==null){
            currentSales = new Sales();
        }
        return currentSales;
    }
    
    /**
     * Setter for property currentSales.
     * @param currentSales New value of property currentSales.
     */
    public void setCurrentSales(Sales currentSales) {
        this.currentSales = currentSales;
    }
    public AppUser getCurrentUserSales() {
        if(currentUserSales==null){
            currentUserSales = new AppUser();
        }
        return currentUserSales;
    }
    
    /**
     * Setter for property currentSales.
     * @param currentSales New value of property currentSales.
     */
    public void setCurrentUserSales(AppUser currentUserSales) {
        this.currentUserSales = currentUserSales;
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
    private javax.swing.JLabel changeLabel;
    private javax.swing.JLabel changeTextLabel;
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
    private javax.swing.JLabel curTypeUsedLabel1;
    private javax.swing.JLabel curTypeUsedLabel2;
    private javax.swing.JLabel curTypeUsedLabel3;
    private javax.swing.JTextField custNameTextField;
    private javax.swing.JLabel dbtBankLabel;
    private javax.swing.JTextField dbtBankTextField;
    private javax.swing.JLabel dbtCardNameLabel;
    private javax.swing.JTextField dbtNumTextField;
    private javax.swing.JPanel debitCardPanel;
    private javax.swing.JButton editTableButton;
    private javax.swing.JTextField invoiceNumberTextField;
    private javax.swing.JButton itemAddButton;
    private javax.swing.JButton itemNewButton;
    private javax.swing.JButton itemRemoveButton;
    private javax.swing.JButton itemUpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lastPaid;
    private javax.swing.JLabel lastPaymentLabel;
    private javax.swing.JLabel lastPaymentTransLabel;
    private javax.swing.JLabel lastTrans;
    private javax.swing.JButton lastTransactionItemsButton;
    private javax.swing.JLabel memberCodeLabel;
    private javax.swing.JLabel memberNameLabel;
    private javax.swing.JLabel netTransLabel;
    private javax.swing.JLabel newBalance;
    private javax.swing.JLabel newBalanceTransLabel;
    private javax.swing.JLabel newPayment;
    private javax.swing.JLabel paidCurTypeUsedLabel;
    private javax.swing.JLabel paidTransLabel;
    private javax.swing.JLabel payAmountLabel;
    private javax.swing.JTextField payAmountTextField;
    private javax.swing.JComboBox payTypeComboBox;
    private javax.swing.JLabel payTypeLabel;
    private javax.swing.JPanel payTypePanel;
    private javax.swing.JComboBox paymentCurTypeComboBox;
    private javax.swing.JTable paymentTable;
    private javax.swing.JTextField returInvTextField;
    private javax.swing.JComboBox saleTypeComboBox;
    private javax.swing.JPanel saleTypePanel;
    private javax.swing.JTextField salesNameTextField;
    private javax.swing.JButton saveAllPaymentButton;
    private javax.swing.JButton showLastPaymentButton;
    // End of variables declaration//GEN-END:variables
    
    public void cmdEditTable(){
        if(paymentTable.getModel().getRowCount()>0) {
            paymentTable.requestFocusInWindow();
            paymentTable.setRowSelectionInterval(0,0);
        }
    }
    public void getGlobalKeyListener(KeyEvent evt){
        switch(evt.getKeyCode()){
            case KeyEvent.VK_F1 :
                break;
            case KeyEvent.VK_F2:
                cmdNewPayment();
                break;
            case KeyEvent.VK_F3:
                
                break;
            case KeyEvent.VK_F4:
                cmdEditTable();
                break;
            case KeyEvent.VK_F5:
                cmdNewTrans();
                break;
            case KeyEvent.VK_F6:
                cmdShowLastPayment();
                break;
            case KeyEvent.VK_F7:
                break;
            case KeyEvent.VK_F8:
                
                break;
            case KeyEvent.VK_F9:
                //cmdCancel ();
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
    
}
