/*
 * CashClosingBalanceDialog.java
 *
 * Created on January 6, 2005, 10:25 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.admin.AppUser; 
import com.dimata.posbo.entity.admin.PstAppUser; 
import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.printAPI.PrintBalancePOS;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.util.Formater;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import java.util.Hashtable;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;


/**
 *
 * @author  pulantara
 */

public class CashClosingBalanceDialog extends JDialog {
    
    /** Creates new form CashClosingBalanceDialog */
    public CashClosingBalanceDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initField();
    }
    public CashClosingBalanceDialog(Frame parent, boolean modal, long lCashCashierId) {
        super(parent, modal);
        initComponents();
        initField();
        setCashCashierId(lCashCashierId);
    }
    
    // OWN MADE CONSTANTS GOES HERE
    
    // constants for column number
    private static final int COL_NO = 0;
    private static final int COL_PAYMENT_TYPE = 1;
    private static final int COL_CURRENCY_TYPE = 2;
    private static final int COL_AMOUNT = 3;
    private static final int COL_TOTAL_IDR = 4;
    
    // constants for column names
    private static final String[] columnNames = {
        "No",
        "Payment Type",
        "Currency Type",
        "Amount",
        "Total(IDR)"
    };
    
    // constants for currency
    private static final int INTERNATIONAL = 0;
    private static final int LOCAL = 1;
    
    private static final String[] currFormat = {
        "###,###.##",
        "###,###.00"
    };
    
    // this can be change
    private static final int CURR_FORMAT_USED = LOCAL;
    
    // type of display request constans
    public static final int DISPLAY_TOTAL_SALE = 0;
    public static final int DISPLAY_CLOSING = 1;
    
    // string for each type
    public static final String[] displayNames = {
        "Total Sale"  ,
        "Closing Balance"
    };
    
    // END OF OWN MADE CONSTANTS
    
    // OWN MADE ATTRIBUTE GOES HERE
    
    // type of request
    private int displayRequest = DISPLAY_TOTAL_SALE;
    // data container
    private Vector dataVector = new Vector();
    // column names container
    private Vector columnVector = new Vector();
    // contain object currencyType accesed by currency's code
    private Hashtable currencyCode = new Hashtable();
    // contain object currencyOID accesed by currency's OID
    private Hashtable currencyOID = new Hashtable();
    // an object that contains lots off information required in this dialog
    private BalanceModel objBalance;
    // cash cashier id, mostly required for initiate objBalance
    private long cashCashierId = 0;
    // Holds BalanceDetailDialog
    private BalanceDetailDialog detailDialog;
    
    // END OF OWN MADE ATTRIBUTE
    
    // OWN MADE METHODE GOES HERE
    
    /** init Field
     *  initiate all field in this dialog form at the begining of its construction
     *  its gonna be a fat methode for this class
     */
    private void initField(){
        
        // init for currency hashtables
        currencyCode = CashierMainApp.getHashCurrencyType();
        currencyOID = CashierMainApp.getHashCurrencyId();
        
        // init columnVector
        this.initColumnVector();
        
        // SETUP FOR SYSTEM INFO
        // set cashier name
        cashierNameTextField.setText(CashierMainApp.getCashUser().getUserName());
        
        // set today date
        dateTextField.setText(Formater.formatDate(getTodayDate(),"EEEE, dd MMM yyyy"));
        
        //  added for refreshing the time
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timeTextField.setText(Formater.formatDate(getTodayDate(),"hh:mm:ss a"));
            }
        };
        new Timer(1000,taskPerformer).start();
        // END SETUP FOR SYSTEM INFO
        
        // set dialog form size
        Dimension dim = new Dimension();
        dim = this.getContentPane().getToolkit().getScreenSize();
        if(dim.width>800){
            int w = dim.width*3/4;      // 3/4 screenSize
            int h = dim.height*3/4;     // 3/4 screenSize
            this.setBounds(w/6,h/6, w,h);
        }
        else{
            this.setBounds(dim.width/20, dim.height/20, dim.width*9/10,dim.height*9/10);
        }
        
        // deactivate loginPanel
        this.deactivateLoginPanel();
        
        // draw current Balance Model if the model exist
        if(this.getObjBalance()!=null){
            this.drawModel();
        }
        
        // focus on okButton
        okButton.requestFocusInWindow();
        
        
    }
    
    /** getTodayDate
     *  return currently updated date n time
     *  @return Date
     */
    private Date getTodayDate(){
        Date today = new Date();
        return today;
    }
    
    /** activate editorPanel */
    private void activateEditorPanel(){
        mainPanel.setVisible(true);
        okButton.requestFocusInWindow();
    }
    
    /** deactivate editorPanel */
    private void deactivateEditorPanel(){
        mainPanel.setVisible(false);
        
    }
    
    /** activate loginPanel */
    private void activateLoginPanel(){
        loginPanel.setVisible(true);
        userNameTextField.setText("");
        uPasswordField.setText("");
        userNameTextField.requestFocusInWindow();
    }
    
    /** deactivate loginPanel */
    private void deactivateLoginPanel(){
        loginPanel.setVisible(false);
    }
    
    public void cmdSetTableColumnSize(){
        int totalWidth = balanceDetailTable.getWidth();
        balanceDetailTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        balanceDetailTable.getColumnModel().getColumn(COL_AMOUNT).setPreferredWidth((int)(totalWidth*0.3));
        balanceDetailTable.getColumnModel().getColumn(COL_CURRENCY_TYPE).setPreferredWidth((int)(totalWidth*0.1));
        balanceDetailTable.getColumnModel().getColumn(COL_PAYMENT_TYPE).setPreferredWidth((int)(totalWidth*0.25));
        balanceDetailTable.getColumnModel().getColumn(COL_TOTAL_IDR).setPreferredWidth((int)(totalWidth*0.3));
        
        balanceDetailTable.revalidate();
        balanceDetailTable.repaint();
    }
    /** draw table */
    private void drawTable(){
        
        balanceDetailTable.setModel(new DefaultTableModel(createDataModel(dataVector),columnVector){
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        balanceDetailTable.validate();
        cmdSetTableColumnSize();
    }
    
    /** createDataModel
     *  basicly convert the logic data vector into table data vector
     */
    private Vector createDataModel(Vector vData){
        
        Vector vDataModel = new Vector();
        
        for(int i = 0; i < vData.size(); i++){
            Vector vRow = new Vector();
            BalanceItem objItem = new BalanceItem();
            CurrencyType objCurrType = new CurrencyType();
            
            objItem = (BalanceItem) vData.get(i);
            objCurrType = (CurrencyType) currencyOID.get(Long.valueOf(objItem.getCurrencyID()+""));
            
            vRow.add(i+1+"");
            //vRow.add(BalanceItem.typeNames[objItem.getPaymentType()]);
            vRow.add(PstCashPayment.paymentType[objItem.getPaymentType()]);
            // Please update the currency type data (in the data base)
            if(objCurrType!=null){
                vRow.add(objCurrType.getCode());
            }
            else{
                vRow.add("Rp");
            }
            vRow.add(toCurrency(objItem.getAmount()));
            vRow.add(toCurrency(objItem.getTotalIDR()));
            vDataModel.add(vRow);
            
        }
        
        return vDataModel;
    }
    
    /** init columnVector
     *  basicly its parse each element of String array columnNames
     *  into vector columnVector
     */
    private void initColumnVector(){
        this.getColumnVector().removeAllElements();
        for(int i = 0; i < columnNames.length; i++){
            this.getColumnVector().add(columnNames[i]);
        }
    }
    
    /** init BalanceModel
     *  init a BalanceModel object that used in this diolog
     *  prereq : cashCashierId allready setted
     */
    private void initBalanceModel(){
        this.setObjBalance(new BalanceModel(this.getCashCashierId()));
        this.drawModel();
    }
    
    /** drawModel
     *  use for draw all information in Balance Model
     */
    private void drawModel(){
        // set opening balance
        openingTextField.setText(toCurrency(this.getObjBalance().getOpeningBalance()));
        // set data vector
        this.setDataVector(this.getObjBalance().getBalanceDetail());
        // draw balance detail table
        this.drawTable();
        // set sales bruto
        salesBrutoTextField.setText(toCurrency(this.getObjBalance().getSalesBruto()));
        // set change total
        changeTextField.setText(toCurrency(this.getObjBalance().getChangeTotal()));
        // set return
        returnTextField.setText(toCurrency(getObjBalance().getReturnTotal()));
        // set sales netto
        salesNettoTextField.setText(toCurrency(this.getObjBalance().getSalesNetto()));
        // set credit sales netto
        creditTextField.setText(toCurrency(this.getObjBalance().getCreditPayment()));
        // set down payment
        downPaymentTextField.setText(toCurrency(this.getObjBalance().getDownPayment()));
        // set other cost
        otherCostTextField.setText(toCurrency(this.getObjBalance().getOtherCost()));
        // set closing balance
        closingTextField.setText(toCurrency(this.getObjBalance().getClosingBalance()));
        // set total invoice
        totalInvoiceTextField.setText(this.getObjBalance().getTotalInvoice()+"");
        // set total return
        totalReturnTextField.setText(this.getObjBalance().getTotalReturn()+"");
        // set Qty sales
        qtySalesTextField.setText(this.getObjBalance().getQtySales()+"");
        // set Qty return
        qtyReturnTextField.setText(this.getObjBalance().getQtyReturn()+"");
        
    }
    
    /** cmdOK */
    private void cmdOK(){
        if(this.getDisplayRequest()==DISPLAY_CLOSING){
            this.deactivateEditorPanel();
            this.activateLoginPanel();
        }
        else {
            int answer = JOptionPane.showConfirmDialog(this,"Print the List?","Print Dialog",JOptionPane.YES_NO_OPTION);
            if(answer==JOptionPane.YES_OPTION){
                cmdPrintTotalSale();
            }
            this.dispose();
        }
    }
    
    private AppUser appUser;
    private void cmdLogin(){
        AppUser objUser = new AppUser();
        objUser = PstAppUser.getByLoginIDAndPassword(userNameTextField.getText(), String.copyValueOf(uPasswordField.getPassword()));
        if (objUser == null || objUser.getLoginId().length()==0 || objUser.getLoginId()==null){
            JOptionPane.showMessageDialog(this,"Can't Login, check your User ID and Password","Supervisor Login",JOptionPane.OK_OPTION);
            userNameTextField.requestFocusInWindow();
        }
        else{
            if(objUser.getUserGroupNew()==PstAppUser.GROUP_SUPERVISOR) {
                this.setAppUser(objUser);
                cmdSaveAll();
                int answer = JOptionPane.showConfirmDialog(this,"Print the List?","Print Dialog",JOptionPane.YES_NO_OPTION);
                if(answer==JOptionPane.YES_OPTION){
                    cmdPrint();
                }
                CashierMainApp.setCashCashier(null);
                CashierMainApp.runCashier();
            }
            else{
                JOptionPane.showMessageDialog(this,"Unauthorized user(supervisor only)","Supervisor Login",JOptionPane.OK_CANCEL_OPTION);
                userNameTextField.requestFocusInWindow();
            }
        }
    }
    
    /** cmdSaveAll */
    private void cmdSaveAll(){
        Balance objBalance;
        CashCashier objCashCashier ;
        try{
            objCashCashier = PstCashCashier.fetchExc(this.getCashCashierId());
            objCashCashier.setSpvCloseOid(this.getAppUser().getOID());
            objCashCashier.setSpvCloseName(this.getAppUser().getFullName());
            PstCashCashier.updateExc(objCashCashier);
            CashierMainApp.setCashCashier(objCashCashier);
                        
            Vector balanceSet = this.getObjBalance().getClosingDetail();
            int size = balanceSet.size();
            BalanceItem item;
            for(int i = 0; i < size; i++){
                item = new BalanceItem();
                item = (BalanceItem) balanceSet.get(i);
                
                objBalance = new Balance();
                objBalance.setCashCashierId(objCashCashier.getOID());
                objBalance.setBalanceDate(this.getTodayDate());
                objBalance.setBalanceType(1); // closing
                objBalance.setCurrencyOid(item.getCurrencyID());
                objBalance.setBalanceValue(0);
                objBalance.setShouldValue(item.getAmount());
                
                PstBalance.insertExc(objBalance);
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("........... Error on cmdSaveAll() :: " + e);
        }
        
        dataVector = new Vector();
        cmdCancelLogin();
        deactivateLoginPanel();
        activateEditorPanel();
        this.dispose();
        
        
    }
    
    /** cmdPrint */
    private void cmdPrint(){
        // please input the code here (help me)
        PrintBalancePOS print = new PrintBalancePOS();
        if(CashierMainApp.isUsingBigInvoice()){
            print.printBigCloseBalanceObj(this.getObjBalance());
        }
        else{
            print.printCloseBalanceObj(this.getObjBalance());
        }
        
    }
    
    /**
     * cmdPrintTotalSale()
     */
    private void cmdPrintTotalSale(){
        PrintBalancePOS print = new PrintBalancePOS();
        if(CashierMainApp.isUsingBigInvoice()){
            print.printBigTotalSaleObj(this.getObjBalance());
        }
        else{
            print.printTotalSaleObj(this.getObjBalance());
        }
    }
    
    /** cmdCancelLogin */
    private void cmdCancelLogin(){
        deactivateLoginPanel();
        activateEditorPanel();
    }
    
    /** toCurrency
     *  convert into selected currency format
     *  @param double
     *  @return String
     */
    private String toCurrency(double dValue){
        //return Formater.formatNumber(dValue, currFormat[CURR_FORMAT_USED]);
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }
    // END OF OWN MADE METHODE
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        shopNameTextField = new javax.swing.JTextField();
        dateTextField = new javax.swing.JTextField();
        cashierNameTextField = new javax.swing.JTextField();
        timeTextField = new javax.swing.JTextField();
        mainPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        totalReturnTextField = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        qtyReturnTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        qtySalesTextField = new javax.swing.JTextField();
        totalInvoiceTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        editorPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        balanceDetailTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        openingTextField = new javax.swing.JTextField();
        openingDetailButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        closingLabel = new javax.swing.JLabel();
        closingTextField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        returnTextField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        salesBrutoTextField = new javax.swing.JTextField();
        salesNettoTextField = new javax.swing.JTextField();
        creditTextField = new javax.swing.JTextField();
        downPaymentTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        returnDetailButton = new javax.swing.JButton();
        creditDetailButton = new javax.swing.JButton();
        dpDetailButton = new javax.swing.JButton();
        closingDetailButton = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        changeTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        otherCostTextField = new javax.swing.JTextField();
        loginPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        uPasswordField = new javax.swing.JPasswordField();
        cancelLoginButton = new javax.swing.JButton();
        okLoginButton = new javax.swing.JButton();

        topPanel.setLayout(new java.awt.GridBagLayout());

        topPanel.setBorder(new javax.swing.border.TitledBorder(""));
        topPanel.setName("Cash Balance Info");
        jLabel1.setText("Shop");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        topPanel.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Cashier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        topPanel.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 3);
        topPanel.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 3);
        topPanel.add(jLabel4, gridBagConstraints);

        shopNameTextField.setColumns(15);
        shopNameTextField.setEditable(false);
        shopNameTextField.setText("ANUGERAH");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        topPanel.add(shopNameTextField, gridBagConstraints);

        dateTextField.setColumns(15);
        dateTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        topPanel.add(dateTextField, gridBagConstraints);

        cashierNameTextField.setColumns(15);
        cashierNameTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        topPanel.add(cashierNameTextField, gridBagConstraints);

        timeTextField.setColumns(15);
        timeTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        topPanel.add(timeTextField, gridBagConstraints);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Closing Balance");
        mainPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(new javax.swing.border.TitledBorder("Sales Statistic"));
        jLabel23.setText("Total Sales");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jLabel23, gridBagConstraints);

        jLabel24.setText("Total Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jLabel24, gridBagConstraints);

        totalReturnTextField.setColumns(5);
        totalReturnTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(totalReturnTextField, gridBagConstraints);

        jLabel25.setText("Qty Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jLabel25, gridBagConstraints);

        qtyReturnTextField.setColumns(5);
        qtyReturnTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(qtyReturnTextField, gridBagConstraints);

        okButton.setMnemonic('O');
        okButton.setText("OK - F12");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        okButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        jPanel7.add(okButton, gridBagConstraints);

        jLabel26.setText("Qty Sales");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jLabel26, gridBagConstraints);

        qtySalesTextField.setColumns(5);
        qtySalesTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(qtySalesTextField, gridBagConstraints);

        totalInvoiceTextField.setColumns(5);
        totalInvoiceTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(totalInvoiceTextField, gridBagConstraints);

        cancelButton.setMnemonic('O');
        cancelButton.setText("Cancel - ESC");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        cancelButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cancelButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        jPanel7.add(cancelButton, gridBagConstraints);

        mainPanel.add(jPanel7, java.awt.BorderLayout.SOUTH);

        editorPanel.setLayout(new java.awt.BorderLayout());

        editorPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 150));
        balanceDetailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Payment Type", "Currency Type", "Amount", "Total IDR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(balanceDetailTable);

        editorPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel13.setText("Opening Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel13, gridBagConstraints);

        openingTextField.setColumns(15);
        openingTextField.setEditable(false);
        openingTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(openingTextField, gridBagConstraints);

        openingDetailButton.setMnemonic('1');
        openingDetailButton.setText("Detail (Alt 1)");
        openingDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openingDetailButtonActionPerformed(evt);
            }
        });
        openingDetailButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                openingDetailButtonKeyPressed(evt);
            }
        });

        jPanel1.add(openingDetailButton, new java.awt.GridBagConstraints());

        editorPanel.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        closingLabel.setText("Closing Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(closingLabel, gridBagConstraints);

        closingTextField.setColumns(15);
        closingTextField.setEditable(false);
        closingTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(closingTextField, gridBagConstraints);

        jLabel16.setText("Credit Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel16, gridBagConstraints);

        returnTextField.setColumns(15);
        returnTextField.setEditable(false);
        returnTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(returnTextField, gridBagConstraints);

        jLabel17.setText("Cash Sales Bruto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel17, gridBagConstraints);

        salesBrutoTextField.setColumns(15);
        salesBrutoTextField.setEditable(false);
        salesBrutoTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(salesBrutoTextField, gridBagConstraints);

        salesNettoTextField.setColumns(15);
        salesNettoTextField.setEditable(false);
        salesNettoTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(salesNettoTextField, gridBagConstraints);

        creditTextField.setColumns(15);
        creditTextField.setEditable(false);
        creditTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(creditTextField, gridBagConstraints);

        downPaymentTextField.setColumns(15);
        downPaymentTextField.setEditable(false);
        downPaymentTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(downPaymentTextField, gridBagConstraints);

        jLabel18.setText("Cash Sales Netto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel18, gridBagConstraints);

        jLabel19.setText("Invoice Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel19, gridBagConstraints);

        returnDetailButton.setMnemonic('2');
        returnDetailButton.setText("Detail (Alt 2)");
        returnDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnDetailButtonActionPerformed(evt);
            }
        });
        returnDetailButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                returnDetailButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel2.add(returnDetailButton, gridBagConstraints);

        creditDetailButton.setMnemonic('3');
        creditDetailButton.setText("Detail (Alt 3)");
        creditDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditDetailButtonActionPerformed(evt);
            }
        });
        creditDetailButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                creditDetailButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel2.add(creditDetailButton, gridBagConstraints);

        dpDetailButton.setMnemonic('4');
        dpDetailButton.setText("Detail (Alt 4)");
        dpDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpDetailButtonActionPerformed(evt);
            }
        });
        dpDetailButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dpDetailButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        jPanel2.add(dpDetailButton, gridBagConstraints);

        closingDetailButton.setMnemonic('5');
        closingDetailButton.setText("Detail (Alt 5)");
        closingDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closingDetailButtonActionPerformed(evt);
            }
        });
        closingDetailButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closingDetailButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        jPanel2.add(closingDetailButton, gridBagConstraints);

        jLabel20.setText("Total Change");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel20, gridBagConstraints);

        changeTextField.setColumns(15);
        changeTextField.setEditable(false);
        changeTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(changeTextField, gridBagConstraints);

        jLabel15.setText("Down Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel15, gridBagConstraints);

        jLabel21.setText("Other Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel21, gridBagConstraints);

        otherCostTextField.setColumns(15);
        otherCostTextField.setEditable(false);
        otherCostTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(otherCostTextField, gridBagConstraints);

        editorPanel.add(jPanel2, java.awt.BorderLayout.SOUTH);

        mainPanel.add(editorPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        loginPanel.setLayout(new java.awt.GridBagLayout());

        loginPanel.setBorder(new javax.swing.border.TitledBorder("Supervisor Login"));
        jLabel7.setText("Supervisor Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        loginPanel.add(jLabel7, gridBagConstraints);

        userNameTextField.setColumns(10);
        userNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userNameTextFieldActionPerformed(evt);
            }
        });
        userNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userNameTextFieldFocusGained(evt);
            }
        });
        userNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                userNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 5, 5);
        loginPanel.add(userNameTextField, gridBagConstraints);

        jLabel8.setText("Supervisor Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        loginPanel.add(jLabel8, gridBagConstraints);

        uPasswordField.setColumns(10);
        uPasswordField.setPreferredSize(new java.awt.Dimension(81, 20));
        uPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uPasswordFieldActionPerformed(evt);
            }
        });
        uPasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                uPasswordFieldFocusGained(evt);
            }
        });
        uPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                uPasswordFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 5, 5);
        loginPanel.add(uPasswordField, gridBagConstraints);

        cancelLoginButton.setMnemonic('C');
        cancelLoginButton.setText("Cancel ");
        cancelLoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelLoginButtonActionPerformed(evt);
            }
        });
        cancelLoginButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cancelLoginButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        loginPanel.add(cancelLoginButton, gridBagConstraints);

        okLoginButton.setIcon(new javax.swing.ImageIcon(""));
        okLoginButton.setMnemonic('L');
        okLoginButton.setText("Approve");
        okLoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okLoginButtonActionPerformed(evt);
            }
        });
        okLoginButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okLoginButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        loginPanel.add(okLoginButton, gridBagConstraints);

        getContentPane().add(loginPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void cancelButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_cancelButtonKeyPressed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if(this.getDisplayRequest()==this.DISPLAY_CLOSING)
            cmdCancelClosing();
        else
            this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void uPasswordFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_uPasswordFieldKeyPressed
    {//GEN-HEADEREND:event_uPasswordFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_uPasswordFieldKeyPressed
    
    private void userNameTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_userNameTextFieldKeyPressed
    {//GEN-HEADEREND:event_userNameTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_userNameTextFieldKeyPressed
    
    private void closingDetailButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_closingDetailButtonKeyPressed
    {//GEN-HEADEREND:event_closingDetailButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_closingDetailButtonKeyPressed
    
    private void dpDetailButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_dpDetailButtonKeyPressed
    {//GEN-HEADEREND:event_dpDetailButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_dpDetailButtonKeyPressed
    
    private void creditDetailButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_creditDetailButtonKeyPressed
    {//GEN-HEADEREND:event_creditDetailButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_creditDetailButtonKeyPressed
    
    private void returnDetailButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_returnDetailButtonKeyPressed
    {//GEN-HEADEREND:event_returnDetailButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_returnDetailButtonKeyPressed
    
    private void openingDetailButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_openingDetailButtonKeyPressed
    {//GEN-HEADEREND:event_openingDetailButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_openingDetailButtonKeyPressed
    
    private void closingDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closingDetailButtonActionPerformed
        this.setDetailDialog(new BalanceDetailDialog(null,true,this.getObjBalance().getClosingDetail(),"Closing Balance Detail"));
        this.getDetailDialog().show();
    }//GEN-LAST:event_closingDetailButtonActionPerformed
    
    private void dpDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpDetailButtonActionPerformed
        this.setDetailDialog(new BalanceDetailDialog(null,true,this.getObjBalance().getDownPaymentVector(),"Down Payment Detail"));
        this.getDetailDialog().show();
    }//GEN-LAST:event_dpDetailButtonActionPerformed
    
    private void creditDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditDetailButtonActionPerformed
        this.setDetailDialog(new BalanceDetailDialog(null,true,this.getObjBalance().getCreditPaymentVector(),"Credit Payment Detail"));
        this.getDetailDialog().show();
    }//GEN-LAST:event_creditDetailButtonActionPerformed
    
    private void returnDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnDetailButtonActionPerformed
        this.setDetailDialog(new BalanceDetailDialog(null,true,this.getObjBalance().getReturnVector(),"Return Detail"));
        this.getDetailDialog().show();
    }//GEN-LAST:event_returnDetailButtonActionPerformed
    
    private void openingDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openingDetailButtonActionPerformed
        this.setDetailDialog(new BalanceDetailDialog(null,true,this.getObjBalance().getOpeningDetail(),"Opening Balance Detail"));
        this.getDetailDialog().show();
    }//GEN-LAST:event_openingDetailButtonActionPerformed
    
    private void cancelLoginButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelLoginButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdCancelLogin();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cancelLoginButtonKeyPressed
    
    private void okLoginButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okLoginButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdLogin();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_okLoginButtonKeyPressed
    
    private void okButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdOK();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_okButtonKeyPressed
    
    private void uPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uPasswordFieldActionPerformed
        cmdLogin();
        //okLoginButton.requestFocusInWindow();
    }//GEN-LAST:event_uPasswordFieldActionPerformed
    
    private void userNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameTextFieldActionPerformed
        uPasswordField.requestFocusInWindow();
    }//GEN-LAST:event_userNameTextFieldActionPerformed
    
    private void uPasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_uPasswordFieldFocusGained
        uPasswordField.selectAll();
    }//GEN-LAST:event_uPasswordFieldFocusGained
    
    private void userNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userNameTextFieldFocusGained
        userNameTextField.selectAll();
    }//GEN-LAST:event_userNameTextFieldFocusGained
    
    private void cancelLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelLoginButtonActionPerformed
        cmdCancelLogin();
    }//GEN-LAST:event_cancelLoginButtonActionPerformed
    
    private void okLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okLoginButtonActionPerformed
        cmdLogin();
    }//GEN-LAST:event_okLoginButtonActionPerformed
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        cmdOK();
    }//GEN-LAST:event_okButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new CashClosingBalanceDialog(new JFrame(), true, Long.parseLong("504404252344989983"/*504404252316523634*/)).show();
    }
    
    /**
     * Getter for property dataVector.
     * @return Value of property dataVector.
     */
    public Vector getDataVector() {
        return dataVector;
    }
    
    /**
     * Setter for property dataVector.
     * @param dataVector New value of property dataVector.
     */
    public void setDataVector(Vector dataVector) {
        this.dataVector = dataVector;
    }
    
    /**
     * Getter for property columnVector.
     * @return Value of property columnVector.
     */
    public Vector getColumnVector() {
        return columnVector;
    }
    
    /**
     * Setter for property columnVector.
     * @param columnVector New value of property columnVector.
     */
    public void setColumnVector(Vector columnVector) {
        this.columnVector = columnVector;
    }
    
    /**
     * Getter for property currencyCode.
     * @return Value of property currencyCode.
     */
    public Hashtable getCurrencyCode() {
        return currencyCode;
    }
    
    /**
     * Setter for property currencyCode.
     * @param currencyCode New value of property currencyCode.
     */
    public void setCurrencyCode(Hashtable currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    /**
     * Getter for property currencyOID.
     * @return Value of property currencyOID.
     */
    public Hashtable getCurrencyOID() {
        return currencyOID;
    }
    
    /**
     * Setter for property currencyOID.
     * @param currencyOID New value of property currencyOID.
     */
    public void setCurrencyOID(Hashtable currencyOID) {
        this.currencyOID = currencyOID;
    }
    
    /**
     * Getter for property objBalance.
     * @return Value of property objBalance.
     */
    public BalanceModel getObjBalance() {
        return objBalance;
    }
    
    /**
     * Setter for property objBalance.
     * @param objBalance New value of property objBalance.
     */
    public void setObjBalance( BalanceModel objBalance) {
        this.objBalance = objBalance;
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
        this.initBalanceModel();
    }
    
    /**
     * Getter for property appUser.
     * @return Value of property appUser.
     */
    public AppUser getAppUser() {
        return appUser;
    }
    
    /**
     * Setter for property appUser.
     * @param appUser New value of property appUser.
     */
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    
    /**
     * Getter for property detailDialog.
     * @return Value of property detailDialog.
     */
    public com.dimata.pos.cashier.BalanceDetailDialog getDetailDialog() {
        return detailDialog;
    }
    
    public void cmdCancelClosing(){
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure want to cancel closing?","Cancel closing",JOptionPane.YES_NO_OPTION);
        if(answer==JOptionPane.YES_OPTION){
            this.dispose();
        }else{
            
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
                
                break;
            case KeyEvent.VK_F5:
                
                break;
            case KeyEvent.VK_F6:
                
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
                cmdOK();
                break;
            case KeyEvent.VK_ESCAPE:
                if(evt.getSource().equals(userNameTextField)||evt.getSource().equals(uPasswordField)){
                    cmdCancelLogin();
                }else{
                    if(this.getDisplayRequest()==this.DISPLAY_CLOSING)
                        cmdCancelClosing();
                    else
                        this.dispose();
                }
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
     * Setter for property detailDialog.
     * @param detailDialog New value of property detailDialog.
     */
    public void setDetailDialog(com.dimata.pos.cashier.BalanceDetailDialog detailDialog) {
        this.detailDialog = detailDialog;
    }
    
    /**
     * Getter for property displayRequest.
     * @return Value of property displayRequest.
     */
    public int getDisplayRequest() {
        return displayRequest;
    }
    
    /**
     * Setter for property displayRequest.
     * @param displayRequest New value of property displayRequest.
     */
    public void setDisplayRequest(int displayRequest) {
        this.displayRequest = displayRequest;
        closingLabel.setText(displayNames[displayRequest]);
        this.setTitle(displayNames[displayRequest]);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable balanceDetailTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cancelLoginButton;
    private javax.swing.JTextField cashierNameTextField;
    private javax.swing.JTextField changeTextField;
    private javax.swing.JButton closingDetailButton;
    private javax.swing.JLabel closingLabel;
    private javax.swing.JTextField closingTextField;
    private javax.swing.JButton creditDetailButton;
    private javax.swing.JTextField creditTextField;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JTextField downPaymentTextField;
    private javax.swing.JButton dpDetailButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton okLoginButton;
    private javax.swing.JButton openingDetailButton;
    private javax.swing.JTextField openingTextField;
    private javax.swing.JTextField otherCostTextField;
    private javax.swing.JTextField qtyReturnTextField;
    private javax.swing.JTextField qtySalesTextField;
    private javax.swing.JButton returnDetailButton;
    private javax.swing.JTextField returnTextField;
    private javax.swing.JTextField salesBrutoTextField;
    private javax.swing.JTextField salesNettoTextField;
    private javax.swing.JTextField shopNameTextField;
    private javax.swing.JTextField timeTextField;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField totalInvoiceTextField;
    private javax.swing.JTextField totalReturnTextField;
    private javax.swing.JPasswordField uPasswordField;
    private javax.swing.JTextField userNameTextField;
    // End of variables declaration//GEN-END:variables
    
}
