/*
 * CashBalanceDialog.java
 *
 * Created on December 27, 2004, 1:41 PM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.printAPI.PrintBalancePOS;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.util.Formater;
/**
 *
 * @author  pulantara
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.Hashtable;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;




public class CashBalanceDialog extends JDialog implements KeyListener {
    
    private static final int CMD_NEW = 1;
    private static final int CMD_EDIT = 2;
    
    private static final String[] cmdString = {
        "",
        "NEW",
        "EDIT"
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
    
    private DefaultTableModel balanceTableModel = null;
    private int currentCmd;
    
    private int currentSelectedRow = -1 ;
    
    private static int COL_NO=0;
    private static int COL_Currency=1;
    private static int COL_Currency_Rate=2;
    private static int COL_Amount=3;
    private static int COL_Value=4;
    
    /** Creates new form CashBalanceDialog */
    public CashBalanceDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initField();
        initState();
    }
    
    private void setDialogTitle(){
        String title = "Opening Balance >> Shop :"+CashierMainApp.getDSJ_CashierXML().getConfig(0).company+" >> Cashier : "+cashierNameTextField.getText()+" >> at :"+dateTextField.getText();
        this.setTitle(title);
    }
    
    private void setSizeTableColumn(){
        
        
        int totalWidth = cashBalanceListTable.getWidth();
        cashBalanceListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.1));
        cashBalanceListTable.getColumnModel().getColumn(COL_Currency).setPreferredWidth((int)(totalWidth*0.15));
        cashBalanceListTable.getColumnModel().getColumn(COL_Currency_Rate).setPreferredWidth((int)(totalWidth*0.25));
        cashBalanceListTable.getColumnModel().getColumn(COL_Amount).setPreferredWidth((int)(totalWidth*0.25));
        cashBalanceListTable.getColumnModel().getColumn(COL_Value).setPreferredWidth((int)(totalWidth*0.25));
        
        cashBalanceListTable.revalidate();
        cashBalanceListTable.repaint();
        
    }
    private void initField() {
        
        // init command
        currentCmd = CMD_NEW;
        
        // init currency
        currencyCode = CashierMainApp.getHashCurrencyType();
        currencyOID = CashierMainApp.getHashCurrencyId();
        currentCurrCode = CashierMainApp.getCurrencyCodeUsed();
        
        
        // set currency combo box
        currencyComboBox.setModel(new DefaultComboBoxModel(currencyCode.keySet().toArray()));
        currencyComboBox.setSelectedItem(currentCurrCode);
        
        // init status label
        cmdReset();
        
        // set column vector
        columnVector.add("No");
        columnVector.add("Currency");
        columnVector.add("Currency Rate(IDR)");
        columnVector.add("Amount");
        columnVector.add("Value (IDR)");
        
        
        // set init currency rate
        currencyRateTextField.setText(toCurrency(getCurrencyRate((String) currencyComboBox.getSelectedItem())));
        
        // set cashier name
        cashierNameTextField.setText(CashierMainApp.getCashUser().getUserName());
        
        // set today date
        dateTextField.setText(Formater.formatDate(getTodayDate(),"EEEE, dd MMM yyyy"));
        setDialogTitle();
        
        
        /**
         *  added for refreshing the time
         */
        
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timeTextField.setText(Formater.formatDate(getTodayDate(),"hh:mm:ss a"));
            }
        };
        
        new Timer(1000,taskPerformer).start();
        
        // set dialog form size
        Dimension dim = new Dimension();
        dim = this.getContentPane().getToolkit().getScreenSize();
        if(dim.width>800){
            int w = dim.width*3/4;      // 3/4 screenSize
            int h = dim.height*3/4;     // 3/4 screenSize
            this.setBounds(w/6,h/6, w,h);
        }
        else{
            this.setBounds(dim.width/20,dim.height/20, dim.width*9/10, dim.height*9/10 /*9/10 screen size (avoid window taskbar)*/ );
        }
        // deactivate login panel before all balance item inserted
        deactivateLoginPanel();
        
        
        cashBalanceListTable.setModel(new DefaultTableModel(new Vector(),columnVector){
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        //cashBalanceListTable.revalidate ();
        setSizeTableColumn();
        // set focus window on currencyComboBox
        currencyComboBox.requestFocusInWindow();
    }
    
    /** init the default state of this dialog form */
    private void initState(){
        valueTextField.setEnabled(false);
        currencyComboBox.requestFocusInWindow();
    }
    
    /** methode use for validate number input from textfield
     *  @parameter String stInput
     *  @return boolean
     */
    private boolean validateInput(String stInput){
        try{
            double temp = Double.parseDouble(CashierMainApp.getFrameHandler().deFormatStringDecimal(stInput));
            //Double.parseDouble(stInput);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    
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
        jLabel9 = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        uPasswordField = new javax.swing.JPasswordField();
        cancelLoginButton = new javax.swing.JButton();
        okLoginButton = new javax.swing.JButton();
        editorPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cashBalanceListTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        valueTextField = new javax.swing.JTextField();
        currencyComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        currencyRateTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        newItemButton = new javax.swing.JButton();
        editItemButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        saveBalanceButton = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Opening Balance");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        jLabel9.setText("Product Code");
        mainPanel.add(jLabel9, java.awt.BorderLayout.CENTER);

        loginPanel.setLayout(new java.awt.GridBagLayout());

        loginPanel.setBorder(new javax.swing.border.TitledBorder(""));
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

        mainPanel.add(loginPanel, java.awt.BorderLayout.SOUTH);

        editorPanel.setLayout(new java.awt.BorderLayout());

        editorPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 150));
        cashBalanceListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Currency", "Currency Rate(IDR)", "Amount", "Value (IDR)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cashBalanceListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cashBalanceListTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(cashBalanceListTable);

        editorPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel10.setText("Currrency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel11, gridBagConstraints);

        valueTextField.setColumns(10);
        valueTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valueTextField.setEnabled(false);
        valueTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valueTextFieldActionPerformed(evt);
            }
        });
        valueTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                valueTextFieldFocusGained(evt);
            }
        });
        valueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                valueTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(valueTextField, gridBagConstraints);

        currencyComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "IDR", "USD" }));
        currencyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currencyComboBoxActionPerformed(evt);
            }
        });
        currencyComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                currencyComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(currencyComboBox, gridBagConstraints);

        jLabel13.setText("Currency Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel13, gridBagConstraints);

        currencyRateTextField.setColumns(10);
        currencyRateTextField.setEditable(false);
        currencyRateTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(currencyRateTextField, gridBagConstraints);

        editorPanel.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        newItemButton.setMnemonic('N');
        newItemButton.setText("New (F5)");
        newItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemButtonActionPerformed(evt);
            }
        });
        newItemButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newItemButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(newItemButton, gridBagConstraints);

        editItemButton.setMnemonic('E');
        editItemButton.setText("Edit (F4)");
        editItemButton.setEnabled(false);
        editItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemButtonActionPerformed(evt);
            }
        });
        editItemButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editItemButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(editItemButton, gridBagConstraints);

        deleteItemButton.setMnemonic('D');
        deleteItemButton.setText("Delete (Alt+D)");
        deleteItemButton.setEnabled(false);
        deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemButtonActionPerformed(evt);
            }
        });
        deleteItemButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deleteItemButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(deleteItemButton, gridBagConstraints);

        saveBalanceButton.setMnemonic('S');
        saveBalanceButton.setText("Save All (F12)");
        saveBalanceButton.setEnabled(false);
        saveBalanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBalanceButtonActionPerformed(evt);
            }
        });
        saveBalanceButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveBalanceButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(saveBalanceButton, gridBagConstraints);

        jLabel14.setText("Total Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel14, gridBagConstraints);

        totalTextField.setColumns(15);
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(totalTextField, gridBagConstraints);

        editorPanel.add(jPanel2, java.awt.BorderLayout.SOUTH);

        mainPanel.add(editorPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private void uPasswordFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_uPasswordFieldKeyPressed
    {//GEN-HEADEREND:event_uPasswordFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_uPasswordFieldKeyPressed
    
    private void userNameTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_userNameTextFieldKeyPressed
    {//GEN-HEADEREND:event_userNameTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_userNameTextFieldKeyPressed
    
    private void valueTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_valueTextFieldKeyPressed
    {//GEN-HEADEREND:event_valueTextFieldKeyPressed
        // TODO add your handling code here:
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_valueTextFieldKeyPressed
    
    private void formKeyTyped (java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyTyped
    {//GEN-HEADEREND:event_formKeyTyped
        // TODO add your handling code here:
        System.out.println("typed");
        if(evt.getKeyCode()==KeyEvent.VK_F12){
            if(evt.getSource() instanceof CashBalanceDialog ){
                okLoginButton.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_formKeyTyped
    
    private void formKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyPressed
    {//GEN-HEADEREND:event_formKeyPressed
        // TODO add your handling code here:
        
        
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed
    
    private void userNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userNameTextFieldFocusGained
        userNameTextField.selectAll();
    }//GEN-LAST:event_userNameTextFieldFocusGained
    
    private void valueTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueTextFieldFocusGained
        valueTextField.selectAll();
    }//GEN-LAST:event_valueTextFieldFocusGained
    
    private void uPasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_uPasswordFieldFocusGained
        uPasswordField.selectAll();
    }//GEN-LAST:event_uPasswordFieldFocusGained
    
    private void okLoginButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okLoginButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdLogin();
        }
    }//GEN-LAST:event_okLoginButtonKeyPressed
    
    private void okLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okLoginButtonActionPerformed
        if(isAnyBalanceDetail()){
            cmdLogin();
        }else{
            JOptionPane.showMessageDialog(this,"Please complete balance data","Incomplete data",JOptionPane.ERROR_MESSAGE);
            cmdCancelLogin();
        }
    }//GEN-LAST:event_okLoginButtonActionPerformed
    
    private void uPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uPasswordFieldActionPerformed
        okLoginButton.requestFocusInWindow();
        cmdLogin();
    }//GEN-LAST:event_uPasswordFieldActionPerformed
    
    private void userNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameTextFieldActionPerformed
        uPasswordField.requestFocusInWindow();
    }//GEN-LAST:event_userNameTextFieldActionPerformed
    
    private void valueTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valueTextFieldActionPerformed
        if(validateInput(valueTextField.getText())){
            double parsed = 0;
            try{
                parsed = Double.parseDouble(valueTextField.getText());
                
            }catch(Exception e){
                parsed = CashierMainApp.getDoubleFromFormated(valueTextField.getText());
            }
            valueTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(parsed));
            cmdSave();
        }
        else{
            JOptionPane.showMessageDialog(this,"Input must be decimal number","CashBalance Dialog",JOptionPane.OK_CANCEL_OPTION);
            valueTextField.setText("");
        }
    }//GEN-LAST:event_valueTextFieldActionPerformed
    
    private void currencyComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_currencyComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            valueTextField.setEnabled(true);
            valueTextField.requestFocusInWindow();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            cashBalanceListTable.requestFocusInWindow();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_currencyComboBoxKeyPressed
    
    private void saveBalanceButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveBalanceButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            activateLoginPanel();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_saveBalanceButtonKeyPressed
    
    private void deleteItemButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deleteItemButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdDelete();
            currentCmd = CMD_NEW;
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_deleteItemButtonKeyPressed
    
    private void editItemButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editItemButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(currentSelectedRow>=0){
                //cmdTableClicked();
                
            }
            else {
                JOptionPane.showMessageDialog(this,"No Selected Row","Edit Record",JOptionPane.OK_OPTION);
            }
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_editItemButtonKeyPressed
    
    private void newItemButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newItemButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            currentCmd = CMD_NEW;
            cmdReset();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_newItemButtonKeyPressed
    
    private void cancelLoginButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelLoginButtonKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdCancelLogin();
        }
    }//GEN-LAST:event_cancelLoginButtonKeyPressed
    
    private void editItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemButtonActionPerformed
        if(currentSelectedRow>=0){
            //cmdTableClicked();
            cmdEditTables();
        }
        else {
            JOptionPane.showMessageDialog(this,"No Selected Row","Edit Record",JOptionPane.OK_OPTION);
        }
        
    }//GEN-LAST:event_editItemButtonActionPerformed
    
    private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed
        cmdDelete();
        currentCmd = CMD_NEW;
    }//GEN-LAST:event_deleteItemButtonActionPerformed
    
    private void newItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemButtonActionPerformed
        currentCmd = CMD_NEW;
        cmdReset();
    }//GEN-LAST:event_newItemButtonActionPerformed
    
    private void cashBalanceListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashBalanceListTableKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdTableClicked();
        }
        else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
            cmdTableClicked();
            cmdDelete();
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_cashBalanceListTableKeyPressed
    
    private void cmdSave(){
        
        switch(currentCmd){
            case CMD_NEW:
                cmdAdd();
                break;
            case CMD_EDIT:
                cmdEdit();
                break;
        };
        
    }
    
    private void currencyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currencyComboBoxActionPerformed
        cmdCurrencyChange();
    }//GEN-LAST:event_currencyComboBoxActionPerformed
    
    private void cancelLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelLoginButtonActionPerformed
        cmdCancelLogin();
        
    }//GEN-LAST:event_cancelLoginButtonActionPerformed
    
    private void saveBalanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBalanceButtonActionPerformed
        if(isAnyBalanceDetail()){
            activateLoginPanel();
        }else{
            JOptionPane.showMessageDialog(this,"Please complete balances","Incomplete data",JOptionPane.ERROR_MESSAGE);
            cmdReset();
        }
    }//GEN-LAST:event_saveBalanceButtonActionPerformed
    private AppUser appUser;
    private CashCashier cashCashier;
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
                dataVector.removeAllElements();
                
            }
            else{
                JOptionPane.showMessageDialog(this,"Unauthorized user(supervisor only)","Supervisor Login",JOptionPane.OK_CANCEL_OPTION);
                userNameTextField.requestFocusInWindow();
            }
        }
    }
    
    private void cmdPrint(){
        //this for print
        PrintBalancePOS print = new PrintBalancePOS();
        if(CashierMainApp.isUsingBigInvoice()){
            print.printBigBalanceObj(CashierMainApp.getCashCashier());
        }
        else{
            print.printBalanceObj(CashierMainApp.getCashCashier());
        }
        
    }
    
    private void cmdReset(){
        currencyComboBox.setSelectedItem(currentCurrCode);
        
        cmdCurrencyChange();
        valueTextField.setText(toCurrency(0));
        initState();
        currentCmd = CMD_NEW;
        deleteItemButton.setEnabled(false);
        //editItemButton.setEnabled(false);
    }
    
    private void cmdCurrencyChange(){
        currencyRateTextField.setText(toCurrency(getCurrencyRate((String) currencyComboBox.getSelectedItem())));
    }
    
    public void getGlobalKeyListener(KeyEvent evt){
        switch(evt.getKeyCode()){
            case KeyEvent.VK_F1 :
                break;
            case KeyEvent.VK_F2:
                cmdSave();
                break;
            case KeyEvent.VK_F3:
                //cmdTableClicked();
                break;
            case KeyEvent.VK_F4:
                cmdEditTables();
                break;
            case KeyEvent.VK_F5:
                cmdReset();
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
                activateLoginPanel();
                break;
            case KeyEvent.VK_ESCAPE:
                if(evt.getSource().equals(userNameTextField)||evt.getSource().equals(uPasswordField)){
                    cmdCancelLogin();
                }else{
                    cmdReset();
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
    
    public boolean isAllValuesCompleted(){
        if(!isAnyBalanceDetail()){
            return false;
        }
        if(!isApproved()){
            return false;
        }
        return true;
        
    }
    
    public boolean isAnyBalanceDetail(){
        if(this.dataVector.size()>0){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isApproved(){
        if(this.getCashCashier().getOID()>0){
            return true;
        }else{
            return false;
        }
    }
    
    private double getCurrencyRate(String stCode){
        
        double result = 0;
        CurrencyType objCurrType = new CurrencyType();
        StandartRate objRate = new StandartRate();
        objCurrType = (CurrencyType) currencyCode.get(stCode);
        //by widi
        //using standart rate, not daily rate
        objRate = CashSaleController.getStandartRate(objCurrType.getOID()+"");
        result = objRate.getSellingRate();
        return result;
    }
    
    
    private void cmdSaveAll(){
        
        cashCashier = this.getCashCashier();
        cashCashier.setAppUserId(CashierMainApp.getCashUser().getUserId());
        cashCashier.setCashDate(new Date());
        cashCashier.setCashMasterId(CashierMainApp.getCashMaster().getOID());
        cashCashier.setSpvName(this.getAppUser().getFullName());
        cashCashier.setSpvOid(this.getAppUser().getOID());
        cashCashier.setSpvCloseOid(1);
        cashCashier.setSpvCloseName("");
        
        try{
            
            long lCashierOid = PstCashCashier.insertExc(cashCashier);
            cashCashier.setOID(lCashierOid);
            
        }catch(Exception e){
            System.out.println("........... Error on cmdSaveAll() :: " + e);
        }
        Balance objBalance;
        int size = dataVector.size();
        for(int i = 0; i < size; i++){
            objBalance = new Balance();
            objBalance = (Balance) dataVector.get(i);
            objBalance.setCashCashierId(cashCashier.getOID());
            
            try{
                PstBalance.insertExc(objBalance);
            }
            catch(Exception e) {
                System.out.println(".....Error on CashBalanceDialog:cmdSaveAll():"+e);
            }
        }
        
        //dataVector.removeAllElements();
        cmdCancelLogin();
        deactivateLoginPanel();
        activateEditorPanel();
        this.dispose();
        CashierMainApp.setCashCashier(cashCashier);
        //}
    }
    
    private void cmdClose(){
        dataVector.removeAllElements();
        cmdCancelLogin();
        activateLoginPanel();
        deactivateEditorPanel();
    }
    
    private void cmdCancelLogin(){
        userNameTextField.setText("");
        uPasswordField.setText("");
        deactivateLoginPanel();
        activateEditorPanel();
    }
    
    private void cmdAdd(){
        
        Balance objBalance = new Balance();
        CurrencyType objCurrType = new CurrencyType();
        objCurrType = (CurrencyType) currencyCode.get((String) currencyComboBox.getSelectedItem());
        objBalance.setBalanceDate(getTodayDate());
        
        objBalance.setBalanceValue(CashierMainApp.getDoubleFromFormated(valueTextField.getText()));
        objBalance.setCurrencyOid(objCurrType.getOID());
        objBalance.setBalanceType(0); // 0 : opening
        
        int position = getAddIndex(objBalance.getCurrencyOid());
        if(position<dataVector.size()){
            Balance objOld = new Balance();
            objOld = (Balance) dataVector.get(position);
            objBalance.setBalanceValue(objOld.getBalanceValue() + objBalance.getBalanceValue());
            dataVector.set(position,objBalance);
        }
        else {
            dataVector.add(objBalance);
        }
        
        drawTable();
        
        cmdReset();
        
        saveBalanceButton.setEnabled(true);
        editItemButton.setEnabled(true);
    }
    
    private int getAddIndex(long lOid){
        
        boolean found = false;
        
        int i = 0;
        Balance objBalance = new Balance();
        
        while((i < dataVector.size()) && !found){
            objBalance = (Balance) dataVector.get(i);
            if(objBalance.getCurrencyOid()==lOid){
                found = true;
            }
            else{
                i++;
            }
        }
        
        return i;
    }
    
    private void cmdTableClicked(){
        int idx = cashBalanceListTable.getSelectedRow();
        redrawEditor(idx);
        currentCmd = CMD_EDIT;
        currentSelectedRow = idx;
        
        deleteItemButton.setEnabled(true);
        editItemButton.setEnabled(true);
    }
    private void cmdEditTables(){
        if(this.cashBalanceListTable.getModel().getRowCount()>0){
            this.cashBalanceListTable.requestFocusInWindow();
            this.cashBalanceListTable.setRowSelectionInterval(0,0);
        }else{
            JOptionPane.showMessageDialog(this,"Please insert any item","Incomplete Data",JOptionPane.WARNING_MESSAGE);
            cmdReset();
        }
    }
    
    private void redrawEditor(int idx){
        Balance objBalance = new Balance();
        CurrencyType objCurrType = new CurrencyType();
        StandartRate objStdRate = new StandartRate();
        
        objBalance = (Balance) dataVector.get(idx);
        objCurrType = (CurrencyType) currencyOID.get(Long.valueOf(objBalance.getCurrencyOid()+""));
        objStdRate = CashSaleController.getStandartRate(objBalance.getCurrencyOid()+"");
        //objStdRate = CashSaleController.getLatestRate(objBalance.getCurrencyOid()+"");
        
        currencyComboBox.setSelectedItem(objCurrType.getCode());
        currencyRateTextField.setText(toCurrency(objStdRate.getSellingRate()));
        valueTextField.setText(toCurrency(objBalance.getBalanceValue()));
        
        initState();
    }
    
    
    private void cmdEdit(){
        Balance objBalance = new Balance();
        CurrencyType objCurrType = new CurrencyType();
        objCurrType = (CurrencyType) currencyCode.get((String) currencyComboBox.getSelectedItem());
        objBalance.setBalanceDate(getTodayDate());
        objBalance.setBalanceType(0); // 0: for opening
        
        objBalance.setBalanceValue(CashierMainApp.getDoubleFromFormated(valueTextField.getText()));
        objBalance.setCurrencyOid(objCurrType.getOID());
        // not sure???!! this need to be sychronized with WIDI
        //objBalance.setCashCashierId(CashierMainApp.getCashCashier().getOID());
        // this line added only for testing purpose
        //objBalance.setCashCashierId(30122004);
        
        int position = getAddIndex(objBalance.getCurrencyOid());
        
        if(position<dataVector.size() && position!=currentSelectedRow){
            Balance objOld = new Balance();
            objOld = (Balance) dataVector.get(position);
            objBalance.setBalanceValue(objOld.getBalanceValue() + objBalance.getBalanceValue());
            dataVector.set(currentSelectedRow,objBalance);
            dataVector.removeElementAt(position);
        }
        else{
            dataVector.set(currentSelectedRow,objBalance);
        }
        
        currentSelectedRow = -1;
        currentCmd = CMD_NEW;
        drawTable();
        
        cmdReset();
    }
    
    
    private void cmdDelete(){
        if(currentSelectedRow>=0){
            int answer = JOptionPane.showConfirmDialog(this,"Are you sure to delete this item?","Deleting items",JOptionPane.YES_NO_OPTION);
            if(answer==JOptionPane.YES_OPTION){
                dataVector.removeElementAt(currentSelectedRow);
                currentSelectedRow = -1;
                if(dataVector.size()==0){
                    saveBalanceButton.setEnabled(false);
                    editItemButton.setEnabled(false);
                }
                drawTable();
                cmdReset();
            }
        }
        else {
            JOptionPane.showMessageDialog(this,"No Row Selected","Delete Record",JOptionPane.OK_OPTION);
        }
        
    }
    
    private void drawTable(){
        cashBalanceListTable.setModel(new DefaultTableModel(createDataModel(dataVector),columnVector){
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        setSizeTableColumn();
        totalTextField.setText(toCurrency(this.getTotalIDR(dataVector)));
    }
    
    private Vector createDataModel(Vector vData){
        
        Vector vDataModel = new Vector();
        
        for(int i = 0; i < vData.size(); i++){
            Vector vRow = new Vector();
            Balance objBalance = new Balance();
            CurrencyType objCurrType = new CurrencyType();
            StandartRate objStdRate = new StandartRate();
            
            objBalance = (Balance) vData.get(i);
            
            objCurrType = (CurrencyType) currencyOID.get(Long.valueOf(objBalance.getCurrencyOid()+""));
            objStdRate = CashSaleController.getStandartRate(objCurrType.getOID()+"");
            //objStdRate = CashSaleController.getLatestRate(objCurrType.getOID()+"");
            // add Row No
            if(objCurrType!=null){
                vRow.add(i+1+"");
                vRow.add(objCurrType.getCode());
                vRow.add(toCurrency(objStdRate.getSellingRate()));
                vRow.add(toCurrency(objBalance.getBalanceValue()));
                vRow.add(toCurrency(objBalance.getBalanceValue()*objStdRate.getSellingRate()));
                vDataModel.add(vRow);
            }
        }
        
        return vDataModel;
    }
    
    private double getTotalIDR(Vector vData){
        
        double result = 0;
        
        for(int i = 0; i < vData.size(); i++){
            Vector vRow = new Vector();
            Balance objBalance = new Balance();
            CurrencyType objCurrType = new CurrencyType();
            StandartRate objStdRate = new StandartRate();
            
            objBalance = (Balance) vData.get(i);
            
            objCurrType = (CurrencyType) currencyOID.get(Long.valueOf(objBalance.getCurrencyOid()+""));
            objStdRate = CashSaleController.getStandartRate(objCurrType.getOID()+"");
            //objStdRate = CashSaleController.getLatestRate(objCurrType.getOID()+"");
            
            if(objCurrType!=null){
                result = result +
                objStdRate.getSellingRate() *
                objBalance.getBalanceValue();
            }
        }
        
        return result;
    }
    
    /** toCurrency
     *  convert into selected currency format
     *  @param double
     *  @return String
     */
    private String toCurrency(double dValue){
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }
    
    private Date getTodayDate(){
        Date today = new Date();
        return today;
    }
    
    // activate editorPanel
    private void activateEditorPanel(){
        editorPanel.setVisible(true);
        drawTable();
    }
    
    // deactivate editorPanel
    private void deactivateEditorPanel(){
        editorPanel.setVisible(false);
        
    }
    
    private void activateLoginPanel(){
        loginPanel.setVisible(true);
        userNameTextField.requestFocusInWindow();
    }
    private void deactivateLoginPanel(){
        loginPanel.setVisible(false);
    }
    
    private void resetTable(){
        dataVector.removeAllElements();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new CashBalanceDialog(new JFrame(), true).show();
    }
    
    /**
     * Getter for property cashCashier.
     * @return Value of property cashCashier.
     */
    public CashCashier getCashCashier() {
        if(cashCashier==null){
            cashCashier = new CashCashier();
            //cashCashier = CashierMainApp.getCashCashier ();
        }
        //cashCashier = CashierMainApp.getCashCashier ();
        return cashCashier;
    }
    
    /**
     * Setter for property cashCashier.
     * @param cashCashier New value of property cashCashier.
     */
    public void setCashCashier(CashCashier cashCashier) {
        this.cashCashier = cashCashier;
    }
    
    /**
     * Getter for property appUser.
     * @return Value of property appUser.
     */
    public AppUser getAppUser() {
        if(appUser==null){
            appUser = new AppUser();
        }
        return appUser;
    }
    
    /**
     * Setter for property appUser.
     * @param appUser New value of property appUser.
     */
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed");
        if(e.getKeyCode()==KeyEvent.VK_F12){
            if(e.getSource() instanceof CashBalanceDialog ){
                okLoginButton.requestFocusInWindow();
            }
        }
        //throw new UnsupportedOperationException ();
    }
    
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException();
    }
    
    public void keyTyped(KeyEvent e) {
        System.out.println("typed");
        if(e.getKeyCode()==KeyEvent.VK_F12){
            if(e.getSource() instanceof CashBalanceDialog ){
                okLoginButton.requestFocusInWindow();
            }
        }
        //throw new UnsupportedOperationException ();
    }
    
    /**
     * Getter for property balanceTableModel.
     * @return Value of property balanceTableModel.
     */
    public DefaultTableModel getBalanceTableModel() {
        if(balanceTableModel==null){
            balanceTableModel = new DefaultTableModel();
        }
        return balanceTableModel;
    }
    
    /**
     * Setter for property balanceTableModel.
     * @param balanceTableModel New value of property balanceTableModel.
     */
    public void setBalanceTableModel(DefaultTableModel balanceTableModel) {
        this.balanceTableModel = balanceTableModel;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelLoginButton;
    private javax.swing.JTable cashBalanceListTable;
    private javax.swing.JTextField cashierNameTextField;
    private javax.swing.JComboBox currencyComboBox;
    private javax.swing.JTextField currencyRateTextField;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JButton deleteItemButton;
    private javax.swing.JButton editItemButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newItemButton;
    private javax.swing.JButton okLoginButton;
    private javax.swing.JButton saveBalanceButton;
    private javax.swing.JTextField shopNameTextField;
    private javax.swing.JTextField timeTextField;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField totalTextField;
    private javax.swing.JPasswordField uPasswordField;
    private javax.swing.JTextField userNameTextField;
    private javax.swing.JTextField valueTextField;
    // End of variables declaration//GEN-END:variables
    private Vector dataVector = new Vector();
    private Vector columnVector = new Vector();
    private Hashtable currencyCode = new Hashtable();
    private Hashtable currencyOID = new Hashtable();
    private String currentCurrCode;
}
