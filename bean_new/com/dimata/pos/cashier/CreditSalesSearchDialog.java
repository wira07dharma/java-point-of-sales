/*
 * InvoiceSearchDialog.java
 *
 * Created on December 17, 2004, 9:45 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.payment.PstCreditPaymentMain;
import com.dimata.pos.entity.payment.PstCashCreditPayment;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.util.Formater;
import com.dimata.util.LogicParser;
import com.dimata.util.Validator;
import com.dimata.util.YearMonth;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.Date;
/**
 *
 * @author  wpradnyana
 * @editor yogi
 * @editor juga wpulantara
 */

import javax.swing.table.DefaultTableModel;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;

public class CreditSalesSearchDialog extends JDialog {
    
    private static final int PRE_AND_SUF = 0;
    private static final int PRE_ONLY = 1;
    private static final int SUF_ONLY = 2;
    private static int searchMethode = CashierMainApp.getDSJ_CashierXML().getConfig(0).searchMethode;
    private static String prefix = (searchMethode==SUF_ONLY?"":"%");
    private static String sufix = (searchMethode==PRE_ONLY?"":"%");
    
    /** Creates new form InvoiceSearchDialog */
    public CreditSalesSearchDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initAll();
    }
    
    public void initAll(){
        initButton();
        this.setDataVector(null);
        this.setInvoiceTableModel(null);
        this.invoiceListTable.setModel(this.getInvoiceTableModel());
        this.jComboBox1.setModel(new DefaultComboBoxModel(cmbDate()));
        this.jComboBox2.setModel(new DefaultComboBoxModel(cmbMonth()));
        this.jComboBox4.setModel(new DefaultComboBoxModel(cmbDate()));
        this.jComboBox5.setModel(new DefaultComboBoxModel(cmbMonth()));
        
        Date dateNow = new Date();
        if(this.jComboBox1.getSelectedIndex()==0){
            jComboBox1.setSelectedIndex((dateNow.getDate()-1));
        }
        
        if(this.jComboBox4.getSelectedIndex()==0){
            jComboBox4.setSelectedIndex((dateNow.getDate()-1));
        }
        
        if(this.jComboBox2.getSelectedIndex()==0){
            jComboBox2.setSelectedIndex((Validator.getIntMonth(dateNow))-1);
        }
        if(this.jComboBox5.getSelectedIndex()==0){
            jComboBox5.setSelectedIndex((Validator.getIntMonth(dateNow))-1);
        }
        Vector vectYear = cmbYear(dateNow,5,-3);
        
        this.jComboBox3.setModel(new DefaultComboBoxModel(vectYear));
        if(this.jComboBox3.getSelectedIndex()==0){
            jComboBox3.setSelectedIndex(indexCmbYear(dateNow,vectYear));
        }
        this.jComboBox6.setModel(new DefaultComboBoxModel(vectYear));
        if(this.jComboBox6.getSelectedIndex()==0){
            jComboBox6.setSelectedIndex(indexCmbYear(dateNow,vectYear));
        }
        
        if(this.allDtChoice.isSelected()){
            this.fromDtChoice.setSelected(false);
        }else{
            this.allDtChoice.setSelected(false);
        }
    }
    
    private void initButton(){
        nextRecordButton.setEnabled(false);
        prevRecordButton.setEnabled(false);
        firstRecordButton.setEnabled(false);
        lastRecordButton.setEnabled(false);
        selectRecordButton.setEnabled(false);
    }
    
    private DefaultTableModel invoiceTableModel= null;
    
    private static final int COL_NO=0;
    private static final int COL_NUM=1;
    private static final int COL_DATE=2;
    private static final int COL_NAME=3;
    private static final int COL_BALANCE=4;
    
    private static String [] fieldNames={
        "No", "Invoice Number", "Invoice Date", "Cust. Name","Balance"
    };
    
    public void cmdSetColumnTableSize(){
        int totalWidth = invoiceListTable.getWidth();
        invoiceListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        invoiceListTable.getColumnModel().getColumn(COL_NUM).setPreferredWidth((int)(totalWidth*0.15));
        invoiceListTable.getColumnModel().getColumn(COL_DATE).setPreferredWidth((int)(totalWidth*0.15));
        invoiceListTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth((int)(totalWidth*0.5));
        invoiceListTable.getColumnModel().getColumn(COL_BALANCE).setPreferredWidth((int)(totalWidth*0.15));
        invoiceListTable.repaint();
        invoiceListTable.revalidate();
    }
    
    private Vector dataVector = null;
    private Vector columnIdentifier = null;
    private String keyword = "";
    
    public Vector getColumnIdentifier(){
        if(columnIdentifier==null){
            columnIdentifier = new Vector();
            for(int i = 0;i<fieldNames.length;i++){
                columnIdentifier.add(fieldNames[i]);
            }
            
        }
        return columnIdentifier;
    }
    public void setColumnIdentifier(Vector columnIdentifier){
        this.columnIdentifier = columnIdentifier;
    }
    
    private static Vector parseTxt(String txt){
        Vector vect = LogicParser.textSentence(txt);
        for(int i =0;i < vect.size();i++) {
            String name =(String)vect.get(i);
            if((name.equals(LogicParser.SIGN))||(name.equals(LogicParser.ENGLISH[0])))
                vect.remove(i);
        }
        return vect;
    }
    
    /**
     * Getter for property invoiceTableModel.
     * @return Value of property invoiceTableModel.
     */
    public DefaultTableModel getInvoiceTableModel() {
        if(invoiceTableModel==null){
            invoiceTableModel = new DefaultTableModel();
            invoiceTableModel.setDataVector(this.getDataVector(),this.getColumnIdentifier());
        }
        return invoiceTableModel;
    }
    
    /**
     * Setter for property invoiceTableModel.
     * @param invoiceTableModel New value of property invoiceTableModel.
     */
    public void setInvoiceTableModel(DefaultTableModel invoiceTableModel) {
        this.invoiceTableModel = invoiceTableModel;
    }
    /**
     * Getter for property dataVector.
     * @return Value of property dataVector.
     */
    public Vector getDataVector() {
        if(dataVector==null){
            dataVector = new Vector();
        }
        return dataVector;
    }
    
    /**
     * Setter for property dataVector.
     * @param dataVector New value of property dataVector.
     */
    public void setDataVector(Vector dataVector) {
        this.dataVector = dataVector;
    }
    
    private Hashtable hashRowIndexOID = new Hashtable();
    
    private Vector transformResult(Vector raw){
        int rawSize = raw.size();
        Vector fix = new Vector();
        try{
            for(int i=0;i<rawSize;i++){
                Vector  obj = (Vector)raw.get(i);
                
                Vector temp = new Vector();
                hashRowIndexOID.put(new Integer(i),(String) obj.get(0));
                
                temp.add(String.valueOf(this.startRecord+(i+1)));
                temp.add((String) obj.get(1));
                temp.add(Formater.formatDate((Date) obj.get(2),"dd-MM-yyyy")); 
                temp.add((String) obj.get(3));
                String balance = CashierMainApp.getFrameHandler().userFormatStringDecimal((Double.parseDouble((String) obj.get(4) )));
                temp.add(balance);
                fix.add(temp);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("err at transfor result : "+e.toString());
        }
        return fix;
    }
    
    /**
     * Getter for property hashRowIndexOID.
     * @return Value of property hashRowIndexOID.
     */
    public Hashtable getHashRowIndexOID() {
        return hashRowIndexOID;
    }
    
    /**
     * Setter for property hashRowIndexOID.
     * @param hashRowIndexOID New value of property hashRowIndexOID.
     */
    public void setHashRowIndexOID(Hashtable hashRowIndexOID) {
        this.hashRowIndexOID = hashRowIndexOID;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceListTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        allDtChoice = new javax.swing.JRadioButton();
        fromDtChoice = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        invoiceNumberTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        custNameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        firstRecordButton = new javax.swing.JButton();
        prevRecordButton = new javax.swing.JButton();
        nextRecordButton = new javax.swing.JButton();
        lastRecordButton = new javax.swing.JButton();
        selectRecordButton = new javax.swing.JButton();
        closeFrameButton = new javax.swing.JButton();
        totalCreditTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search Credit Sales");
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 150));
        invoiceListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Invoice Number", "Invoice Date", "Status", "Balance"
            }
        ));
        invoiceListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoiceListTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(invoiceListTable);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(new javax.swing.border.TitledBorder(null, "Parameter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 11)));
        allDtChoice.setSelected(true);
        allDtChoice.setText("All Date");
        allDtChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allDtChoiceActionPerformed(evt);
            }
        });
        allDtChoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                allDtChoiceKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(allDtChoice, gridBagConstraints);

        fromDtChoice.setText("From");
        fromDtChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromDtChoiceActionPerformed(evt);
            }
        });
        fromDtChoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fromDtChoiceKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(fromDtChoice, gridBagConstraints);

        jLabel2.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        jPanel7.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Invoice Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel7.add(jLabel3, gridBagConstraints);

        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox1KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jComboBox1, gridBagConstraints);

        jComboBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox2KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jComboBox2, gridBagConstraints);

        jComboBox3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox3KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jComboBox3, gridBagConstraints);

        jComboBox4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox4KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jComboBox4, gridBagConstraints);

        jComboBox5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox5KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jComboBox5, gridBagConstraints);

        jComboBox6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox6KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jComboBox6, gridBagConstraints);

        jLabel1.setText("Cust.Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel7.add(jLabel1, gridBagConstraints);

        invoiceNumberTextField.setColumns(10);
        invoiceNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceNumberTextFieldActionPerformed(evt);
            }
        });
        invoiceNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoiceNumberTextFieldFocusGained(evt);
            }
        });
        invoiceNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoiceNumberTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(invoiceNumberTextField, gridBagConstraints);

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(searchButton, gridBagConstraints);

        custNameTextField.setColumns(15);
        custNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custNameTextFieldActionPerformed(evt);
            }
        });
        custNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                custNameTextFieldFocusGained(evt);
            }
        });
        custNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                custNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(custNameTextField, gridBagConstraints);

        jLabel4.setText("Inv.Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel7.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel7, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        firstRecordButton.setText("First");
        firstRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstRecordButtonActionPerformed(evt);
            }
        });

        jPanel6.add(firstRecordButton, new java.awt.GridBagConstraints());

        prevRecordButton.setText("Prev");
        prevRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevRecordButtonActionPerformed(evt);
            }
        });

        jPanel6.add(prevRecordButton, new java.awt.GridBagConstraints());

        nextRecordButton.setText("Next");
        nextRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextRecordButtonActionPerformed(evt);
            }
        });

        jPanel6.add(nextRecordButton, new java.awt.GridBagConstraints());

        lastRecordButton.setText("Last");
        lastRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastRecordButtonActionPerformed(evt);
            }
        });

        jPanel6.add(lastRecordButton, new java.awt.GridBagConstraints());

        selectRecordButton.setText("Choose");
        selectRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRecordButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel6.add(selectRecordButton, gridBagConstraints);

        closeFrameButton.setText("Close");
        closeFrameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFrameButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel6.add(closeFrameButton, gridBagConstraints);

        jPanel3.add(jPanel6, new java.awt.GridBagConstraints());

        totalCreditTextField.setColumns(15);
        totalCreditTextField.setEditable(false);
        totalCreditTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(totalCreditTextField, gridBagConstraints);

        jLabel5.setText("Total ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(jLabel5, gridBagConstraints);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void fromDtChoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fromDtChoiceKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_fromDtChoiceKeyPressed

    private void allDtChoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_allDtChoiceKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_allDtChoiceKeyPressed

    private void jComboBox6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox6KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox6KeyPressed

    private void jComboBox5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox5KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox5KeyPressed

    private void jComboBox4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox4KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox4KeyPressed

    private void jComboBox3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox3KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox3KeyPressed

    private void jComboBox2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox2KeyPressed

    private void jComboBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox1KeyPressed

    private void custNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_custNameTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_custNameTextFieldKeyPressed

    private void invoiceNumberTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoiceNumberTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            this.dispose();
        }
    }//GEN-LAST:event_invoiceNumberTextFieldKeyPressed
    
    private void custNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_custNameTextFieldFocusGained
        custNameTextField.selectAll();
    }//GEN-LAST:event_custNameTextFieldFocusGained
    
    private void custNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custNameTextFieldActionPerformed
        this.startRecord=0;
        cmdSearch();
    }//GEN-LAST:event_custNameTextFieldActionPerformed
    
    private void invoiceNumberTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoiceNumberTextFieldFocusGained
        invoiceNumberTextField.selectAll();
    }//GEN-LAST:event_invoiceNumberTextFieldFocusGained
    
    private void fromDtChoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromDtChoiceActionPerformed
        // Add your handling code here:
        this.allDtChoice.setSelected(false);
    }//GEN-LAST:event_fromDtChoiceActionPerformed
    
    private void allDtChoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allDtChoiceActionPerformed
        // Add your handling code here:
        this.fromDtChoice.setSelected(false);
    }//GEN-LAST:event_allDtChoiceActionPerformed
    
    private void closeFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeFrameButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeFrameButtonActionPerformed
    
    private void selectRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRecordButtonActionPerformed
        // Add your handling code here:
        cmdChoosen();
    }//GEN-LAST:event_selectRecordButtonActionPerformed
    
    private void lastRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastRecordButtonActionPerformed
        // Add your handling code here:
        cmdLast();
    }//GEN-LAST:event_lastRecordButtonActionPerformed
    
    private void nextRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextRecordButtonActionPerformed
        // Add your handling code here:
        cmdNext();
    }//GEN-LAST:event_nextRecordButtonActionPerformed
    
    private void prevRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevRecordButtonActionPerformed
        // Add your handling code here:
        cmdPrev();
    }//GEN-LAST:event_prevRecordButtonActionPerformed
    
    private void firstRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstRecordButtonActionPerformed
        // Add your handling code here:
        cmdFirst();
    }//GEN-LAST:event_firstRecordButtonActionPerformed
    
    private void invoiceListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoiceListTableKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cmdChoosen();
        }else if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            invoiceNumberTextField.requestFocusInWindow();
        }else if(evt.getKeyCode()==KeyEvent.VK_HOME && firstRecordButton.isEnabled()){
            cmdFirst();
        }else if(evt.getKeyCode()==KeyEvent.VK_END && lastRecordButton.isEnabled()){
            cmdLast();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP && prevRecordButton.isEnabled()){
            cmdPrev();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN && nextRecordButton.isEnabled()){
            cmdNext();
        }else if((evt.getKeyCode()==KeyEvent.VK_UP) && (invoiceListTable.getSelectedRow() == 0) && prevRecordButton.isEnabled()){
            cmdPrev();
        }else if((evt.getKeyCode()==KeyEvent.VK_DOWN)&& (invoiceListTable.getSelectedRow() == recordToGet-1) && nextRecordButton.isEnabled()){ 
            cmdNext();
        }
    }//GEN-LAST:event_invoiceListTableKeyPressed
    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        this.startRecord=0;
        cmdSearch();
    }//GEN-LAST:event_searchButtonActionPerformed
    
    private void invoiceNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceNumberTextFieldActionPerformed
        if(invoiceNumberTextField.getText().length()>0){
            this.startRecord=0;
            cmdSearch();
        }
        else{
            custNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_invoiceNumberTextFieldActionPerformed
    
    private void refreshButton(int start, int totalRows, int recordToGet){
        
        if(totalRows>0)
            selectRecordButton.setEnabled(true);
        else
            selectRecordButton.setEnabled(false);
        
        if((totalRows - start) <= recordToGet){
            lastRecordButton.setEnabled(false);
            nextRecordButton.setEnabled(false);
        }
        else{
            lastRecordButton.setEnabled(true);
            nextRecordButton.setEnabled(true);
        }
        
        if(start == 0){
            firstRecordButton.setEnabled(false);
            prevRecordButton.setEnabled(false);
        }
        else{
            firstRecordButton.setEnabled(true);
            prevRecordButton.setEnabled(true);
        }
        
    }
    
    public void cmdSearch(){
        Vector result = new Vector();
        result = getInvoice(this.startRecord,this.recordToGet, this.invoiceNumberTextField.getText(),this.custNameTextField.getText());
        setSizeRecord(getInvoiceCount(this.invoiceNumberTextField.getText(),this.custNameTextField.getText()));
        refreshButton(startRecord, sizeRecord, recordToGet);
        dataVector = this.transformResult(result);
        this.invoiceTableModel.setDataVector(dataVector,this.getColumnIdentifier());
        this.invoiceListTable.requestFocusInWindow();
        if(result.size()>0){
            cmdSetColumnTableSize();
            this.invoiceListTable.setRowSelectionInterval(this.invoiceTableModel.getRowCount()-1,this.invoiceTableModel.getRowCount()-1);
            totalCreditTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(getBalance(this.invoiceNumberTextField.getText(),this.custNameTextField.getText())));  
        }
        else{
            totalCreditTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(0)); 
            JOptionPane.showMessageDialog(this,"Invoice Not found","Search result",JOptionPane.OK_OPTION);
            invoiceNumberTextField.requestFocusInWindow();
        }
        
    }
    
    private void cmdFirst(){
        this.startRecord = 0;
        cmdSearch();
    }
    
    private void cmdPrev(){
        this.startRecord = this.startRecord - this.recordToGet;
        if(this.startRecord<1){
            this.startRecord = 0;
        }
        cmdSearch();
    }
    
    private void cmdNext(){
        this.startRecord = this.startRecord + this.recordToGet;
        if(this.startRecord >= this.sizeRecord){
            //this.startRecord = this.startRecord - this.sizeRecord;
            cmdFirst();
        }
        cmdSearch();
    }
    
    private void cmdLast(){
        int mdl = this.sizeRecord % this.recordToGet;
        if(mdl>0){
            this.startRecord = this.sizeRecord - mdl;
        }
        else{
            this.startRecord = this.sizeRecord - this.recordToGet;
        }
        cmdSearch();
    }
    
    private void cmdChoosen(){
        Vector result = new Vector(1,1);
        BillMain billmain = new BillMain();
        int indexColumn = this.invoiceListTable.getSelectedRow();
        long billmainId = Long.parseLong((String)getHashRowIndexOID().get(new Integer(indexColumn)));
        try{
            billmain = PstBillMain.fetchExc(billmainId);
        }
        catch(Exception e){
            System.out.println("Err on choose item : "+e.toString());
        }
        result.add(billmain);
        CashSaleController.setCreditSaleChoosen(result);
        this.dispose();
    }
    
    public Vector getInvoice(int start,int recordToGet, String invoiceCodeText, String custNameText){
        Vector result = new Vector(1,1);
        long locationId = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId);
        String where = "BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = "+PstBillMain.TRANS_TYPE_CREDIT+" AND BM."+
        PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = "+PstBillMain.TRANS_STATUS_OPEN+" AND BM."+
        PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+" = "+locationId;
        
        if(invoiceCodeText!=null&&invoiceCodeText.length()>0){
            Vector vect = parseTxt(invoiceCodeText);
            String strCode = "";
            for(int i=0;i<vect.size();i++){
                if(strCode.length()==0){
                    strCode = strCode + " (BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '"+prefix+vect.get(i)+sufix+"') ";
                }else{
                    strCode = strCode + " OR (BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '"+prefix+vect.get(i)+sufix+"') ";
                }
                
            }
            
            where = where + " AND ("+strCode+")";
            
        }
        
        if(custNameText!=null&&custNameText.length()>0){
            String stCustName = " (CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+
            " LIKE '"+prefix+custNameText+sufix+"' OR CL."+
            PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME]+
            " LIKE '"+prefix+custNameText+sufix+"') ";
            where = where + " AND " + stCustName;
        }
        
        if(this.fromDtChoice.isSelected()){
            Date dtFrom = composeDate(this.jComboBox1,this.jComboBox2,this.jComboBox3);
            Date dtTo = composeDate(this.jComboBox4,this.jComboBox5,this.jComboBox6);
            if(dtFrom!=null&&dtTo!=null){
                if(where!=null&&where.length()>0){
                    where = where + " AND (BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " BETWEEN '"+Formater.formatDate(dtFrom,"yyyy-MM-dd")+" 00:00:01'  "+
                    " AND '"+Formater.formatDate(dtTo,"yyyy-MM-dd")+" 23:59:00') ";
                }else{
                    where = where + " (BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " BETWEEN '"+Formater.formatDate(dtFrom,"yyyy-MM-dd")+" 00:00:01' "+
                    " AND '"+Formater.formatDate(dtTo,"yyyy-MM-dd")+" 23:59:00') ";
                }
            }
        }
        String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "+
            " BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            ", BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+ 
            ", BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ 
            ", CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+ 
            ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+")"+
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN+ " AS BM "+ 
            " INNER JOIN "+PstMemberReg.TBL_CONTACT_LIST+ " AS CL "+
            " ON BM."+PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+
            " = CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]+
            " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL+" AS BD "+ 
            " ON BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]; 
                
            sql = sql + " WHERE " + where;
            
             sql = sql + " GROUP BY "+
            " BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            ", BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+ 
            ", BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ 
            ", CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            
           
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + ","+ recordToGet ;
                    break;
                case DBHandler.DBSVR_POSTGRESQL :
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ start ;
                    break;
                default:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + ","+ recordToGet ;
            }
            //System.out.println("sql get Invoice : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector temp = new Vector();
                long billMainId = rs.getLong(1);
                temp.add(billMainId+"");
                temp.add(rs.getString(2));
                temp.add(rs.getDate(3));
                temp.add(rs.getString(4));
                double netTrans = rs.getDouble(5);
                double lastPay = getLastPayment(billMainId);
                double balance = netTrans - lastPay;
                temp.add(balance+"");
                result.add(temp);
            }
            rs.close();
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        //result = PstBillMain.list(start,recordToGet, where,order);
        return result;
    }
     
    public int getInvoiceCount(String invoiceCodeText, String custNameText){
        int result = 0;
        long locationId = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId);
        String where = ""+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = "+PstBillMain.TRANS_TYPE_CREDIT+" AND "+
        PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = "+PstBillMain.TRANS_STATUS_OPEN+" AND "+
        PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+" = "+locationId;
        
        if(invoiceCodeText!=null&&invoiceCodeText.length()>0){
            Vector vect = parseTxt(invoiceCodeText);
            String strCode = "";
            for(int i=0;i<vect.size();i++){
                if(strCode.length()==0){
                    strCode = strCode + " ("+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '"+prefix+vect.get(i)+sufix+"') ";
                }else{
                    strCode = strCode + " OR ("+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '"+prefix+vect.get(i)+sufix+"') ";
                }
                
            }
            
            where = where + " AND ("+strCode+")";
            
        }
        
        if(custNameText!=null&&custNameText.length()>0){
            String stCustName = " ("+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+
            " LIKE '"+prefix+custNameText+sufix+"' OR "+
            PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME]+
            " LIKE '"+prefix+custNameText+sufix+"') ";
            where = where + " AND " + stCustName;
        }
        
        if(this.fromDtChoice.isSelected()){
            Date dtFrom = composeDate(this.jComboBox1,this.jComboBox2,this.jComboBox3);
            Date dtTo = composeDate(this.jComboBox4,this.jComboBox5,this.jComboBox6);
            if(dtFrom!=null&&dtTo!=null){
                if(where!=null&&where.length()>0){
                    where = where + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " BETWEEN '"+Formater.formatDate(dtFrom,"yyyy-MM-dd")+" 00:00:01'  "+
                    " AND '"+Formater.formatDate(dtTo,"yyyy-MM-dd")+" 23:59:00') ";
                }else{
                    where = where + " ("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " BETWEEN '"+Formater.formatDate(dtFrom,"yyyy-MM-dd")+" 00:00:01' "+
                    " AND '"+Formater.formatDate(dtTo,"yyyy-MM-dd")+" 23:59:00') ";
                }
            }
        }
        
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(*) FROM " + PstBillMain.TBL_CASH_BILL_MAIN+
            " INNER JOIN "+PstMemberReg.TBL_CONTACT_LIST+
            " ON "+PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+
            " = "+PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID];
            if(where != null && where.length() > 0)
                sql = sql + " WHERE " + where;
           
            //System.out.println("sql get InvoiceCount : "+sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if(rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        //result = PstBillMain.list(start,recordToGet, where,order);
        return result; 
    }
    
    public double getLastPayment(long billMainId){
        double result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+") "+      
            " FROM " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS M "+
            " INNER JOIN "+ PstCashCreditPayment.TBL_PAYMENT+" AS P "+
            " ON M."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
            " = P."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
            " WHERE M."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]+
            " = "+billMainId;
            
            //System.out.println("sql get Last Payment : "+sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
       return result; 
    }
    
    public double getBalance(String invoiceCodeText, String custNameText){ 
        double result = 0;
        long locationId = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId);
        String where = "BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = "+PstBillMain.TRANS_TYPE_CREDIT+" AND BM."+
        PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = "+PstBillMain.TRANS_STATUS_OPEN+" AND BM."+
        PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+" = "+locationId;
        
        if(invoiceCodeText!=null&&invoiceCodeText.length()>0){
            Vector vect = parseTxt(invoiceCodeText);
            String strCode = "";
            for(int i=0;i<vect.size();i++){
                if(strCode.length()==0){
                    strCode = strCode + " (BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '"+prefix+vect.get(i)+sufix+"') ";
                }else{
                    strCode = strCode + " OR (BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '"+prefix+vect.get(i)+sufix+"') ";
                }
                
            }
            
            where = where + " AND ("+strCode+")";
            
        }
        
        if(custNameText!=null&&custNameText.length()>0){
            String stCustName = " (CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+
            " LIKE '"+prefix+custNameText+sufix+"' OR CL."+
            PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME]+
            " LIKE '"+prefix+custNameText+sufix+"') ";
            where = where + " AND " + stCustName;
        }
        
        if(this.fromDtChoice.isSelected()){
            Date dtFrom = composeDate(this.jComboBox1,this.jComboBox2,this.jComboBox3);
            Date dtTo = composeDate(this.jComboBox4,this.jComboBox5,this.jComboBox6);
            if(dtFrom!=null&&dtTo!=null){
                if(where!=null&&where.length()>0){
                    where = where + " AND (BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " BETWEEN '"+Formater.formatDate(dtFrom,"yyyy-MM-dd")+" 00:00:01'  "+
                    " AND '"+Formater.formatDate(dtTo,"yyyy-MM-dd")+" 23:59:00') ";
                }else{
                    where = where + " (BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " BETWEEN '"+Formater.formatDate(dtFrom,"yyyy-MM-dd")+" 00:00:01' "+
                    " AND '"+Formater.formatDate(dtTo,"yyyy-MM-dd")+" 23:59:00') ";
                }
            }
        }
        
        DBResultSet dbrs = null;
        double netTrans = 0;
        double lastPay = 0;
        try {
            String sql = "SELECT "+
            " SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+")"+
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN+ " AS BM "+ 
            " INNER JOIN "+PstMemberReg.TBL_CONTACT_LIST+ " AS CL "+
            " ON BM."+PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+
            " = CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]+
            " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL+" AS BD "+ 
            " ON BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID];  
                
            sql = sql + " WHERE " + where;
            
            //System.out.println("sql get netTrans : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if(rs.next()) {
                netTrans = rs.getDouble(1);
            }
            rs.close();
            
            sql = "SELECT "+
            " SUM(CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+")"+
            " FROM " + PstBillMain.TBL_CASH_BILL_MAIN+ " AS BM "+ 
            " INNER JOIN "+PstMemberReg.TBL_CONTACT_LIST+ " AS CL "+
            " ON BM."+PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+
            " = CL."+PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]+
            " INNER JOIN "+PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS CM "+ 
            " ON BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = CM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]+
            " INNER JOIN "+PstCashCreditPayment.TBL_PAYMENT+ " AS CP "+
            " ON CM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
            " = CP."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID];  
                
            sql = sql + " WHERE " + where;
            
            //System.out.println("sql get lastPay : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            if(rs.next()) {
                lastPay = rs.getDouble(1);
            }
            rs.close();
            
            result = netTrans - lastPay;
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /** Getter for property startRecord.
     * @return Value of property startRecord.
     *
     */
    public int getStartRecord() {
        return this.startRecord;
    }
    
    /** Setter for property startRecord.
     * @param startRecord New value of property startRecord.
     *
     */
    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }
    
    /** Getter for property sizeRecord.
     * @return Value of property sizeRecord.
     *
     */
    public int getSizeRecord() {
        return this.sizeRecord;
    }
    
    /** Setter for property sizeRecord.
     * @param sizeRecord New value of property sizeRecord.
     *
     */
    public void setSizeRecord(int sizeRecord) {
        this.sizeRecord = sizeRecord;
    }
    
    /** Holds value of property startRecord. */
    private int startRecord = 0;
    
    /** Holds value of property sizeRecord. */
    private int sizeRecord = 0;
    
    /** Holds value of property recordToGet. */
    private int recordToGet = 8;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new InvoiceSearchDialog(new JFrame(), true).show();
    }
    
    public Vector cmbDate() {
        Vector result = new Vector();
        for(int i=1;i<=31;i++){
            result.add(""+i);
        }
        
        return result;
    }
    
    public Vector cmbMonth() {
        Vector result = new Vector();
        for(int i=1;i<=12;i++){
            result.add(YearMonth.getShortInaMonthName(i));
        }
        return result;
    }
    
    public Vector cmbYear(Date dt,int interval,int milestone) {
        Vector result = new Vector();
        int yr = Validator.getIntYear(dt);
        int loop = 0;
        boolean check = false;
        for(int i=(yr+milestone);i<=(yr+(interval+milestone));i++){
            result.add(""+i);
        }
        System.out.println(">>>>>>>> result : "+result);
        return result;
    }
    
    public int indexCmbYear(Date dt, Vector cmbYear) {
        int yr = Validator.getIntYear(dt);
        int loop = 0;
        boolean check = false;
        for(int i=0;i<cmbYear.size();i++){
            String str = (String)cmbYear.get(i);
            if(str.equals(String.valueOf(yr))){
                check = true;
                return loop;
            }
            if(!check){
                loop++;
            }
        }
        return 0;
    }
    
    public Date composeDate(JComboBox jCombo1,JComboBox jCombo2,JComboBox jCombo3){
        Date dtNew = new Date();
        int date = Integer.parseInt((String)jCombo1.getSelectedItem());
        int month = jCombo2.getSelectedIndex()+1;
        int year = Integer.parseInt((String)jCombo3.getSelectedItem());
        
        dtNew = new Date((year-1900),(month-1),date);
        return dtNew;
    }
    
    public int getInt(){
        return 0;
    }
    
    /**
     * Getter for property keyword.
     * @return Value of property keyword.
     */
    public java.lang.String getKeyword() {
        return keyword;
    }
    
    /**
     * Setter for property keyword.
     * @param keyword New value of property keyword.
     */
    public void setKeyword(java.lang.String keyword) {
        this.keyword = keyword;
        this.invoiceNumberTextField.setText(keyword);
        //this.invoiceNumberTextField.setEditable (false);
    }
    
    public void setKeyword(java.lang.String keyword, java.lang.String name) {
        this.keyword = keyword;
        this.invoiceNumberTextField.setText(keyword);
        this.custNameTextField.setText(name);
        //this.invoiceNumberTextField.setEditable (false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton allDtChoice;
    private javax.swing.JButton closeFrameButton;
    private javax.swing.JTextField custNameTextField;
    private javax.swing.JButton firstRecordButton;
    private javax.swing.JRadioButton fromDtChoice;
    private javax.swing.JTable invoiceListTable;
    private javax.swing.JTextField invoiceNumberTextField;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lastRecordButton;
    private javax.swing.JButton nextRecordButton;
    private javax.swing.JButton prevRecordButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton selectRecordButton;
    private javax.swing.JTextField totalCreditTextField;
    // End of variables declaration//GEN-END:variables
    
    private Vector resultCmbDate = new Vector();;
    
}
