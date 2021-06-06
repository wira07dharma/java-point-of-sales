/*
 * OtherCostDialog.java
 *
 * Created on August 4, 2005, 1:27 PM
 */

package com.dimata.pos.cashier;

import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.pos.entity.billing.OtherCost;
import com.dimata.pos.entity.billing.PstOtherCost;
import com.dimata.util.Command;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  pulantara
 */
public class OtherCostDialog extends JDialog {
    
    public static final int COL_NO = 0;
    public static final int COL_NAME = 1;
    public static final int COL_DESCRIPTION = 2;
    public static final int COL_AMOUNT = 3;
    
    public static final String[] columnNames = {
        "No",
        "Name",
        "Description",
        "Amount in Def.Cur."
    };
    /** Holds candidate for other cost */
    private OtherCost candidateOtherCost = new OtherCost();
    
    /** Holds current command */
    private int iCommand = Command.ADD;
    /**
     *  digunakan untuk menyimpan other cost
     */
    private Hashtable hashOtherCost = new Hashtable();
    /**
     *  digunakan untuk shorting other cost
     */
    private Vector vectOtherCost = new Vector();
    /**
     *  digunakan untuk index other cost yang dipilih
     */
    private int idxOtherCost = 0;
    
    /** hold value total other cost amount */
    private double totOtherCost = 0.00;
    
    /** hold data model for otherCostTable */
    private DefaultTableModel otherCostTableModel;
    
    /** hold column identifier for otherCostTable */
    private Vector columnIdentifiers;
    
    /** Creates new form OtherCostDialog */
    public OtherCostDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initFields();
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
                if(editTableButton.isEnabled()){
                    this.cmdEditTable();
                }
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
                if(saveAllButton.isEnabled()){
                    cmdSaveAll();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if(iCommand!=Command.EDIT){
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
    
    private void cmdCancel(){
        this.dispose();
    }
    
    public void cmdReset(){
        
        this.setCandidateOtherCost(new OtherCost());
        
        iCommand = Command.ADD;
        
        amountTextField.setText("");
        descriptionTextArea.setText("");
        nameTextField.setText("");
        curTypeComboBox.setSelectedItem((String) CashierMainApp.getCurrencyCodeUsed());
        cmdCurrencyChange();
        
        amountTextField.setEditable(false);
        descriptionTextArea.setEnabled(false);
        curTypeComboBox.setEnabled(false);
        nameTextField.setEditable(true);
        
        nameTextField.requestFocusInWindow();
    }
    
    private void initFields(){
        
        // init currency ComboBox
        Vector hashCurr = new Vector(CashierMainApp.getHashCurrencyType().keySet());
        curTypeComboBox.setModel(new DefaultComboBoxModel(hashCurr));
        
        // init table
        this.otherCostTable.setModel(this.getOtherCostTableModel());
        this.setColumnTableSize();
        
        // reset fields
        cmdReset();
    }
    
    
    private void cmdCurrencyChange(){
        currencyRateTextField.setText(toCurrency(getCurrencyRate((String) curTypeComboBox.getSelectedItem())));
    }
    
    private double getCurrencyRate(String stCode){
        
        double result = 0;
        CurrencyType objCurrType = new CurrencyType();
        StandartRate objRate = new StandartRate();
        objCurrType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(stCode);
        objRate = CashSaleController.getLatestRate(objCurrType.getOID()+"");
        result = objRate.getSellingRate();
        return result;
    }
    /** toCurrency
     *  by wpulantara
     *  convert into selected currency format
     *  @param double
     *  @return String
     */
    private String toCurrency(double dValue){
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }
    
    /** load current candidate other cost into editor fields */
    public void loadToFields(){
        
        if(this.getCandidateOtherCost()!=null){
            nameTextField.setText(this.getCandidateOtherCost().getName());
            descriptionTextArea.setText(this.getCandidateOtherCost().getDescription());
            CurrencyType objCurrType = new CurrencyType();
            try{
                objCurrType = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(this.getCandidateOtherCost().getCurrencyId()));
            }
            catch(Exception e){
                System.out.println("err on load to fields: "+e.toString());
                objCurrType = new CurrencyType();
            }
            curTypeComboBox.setSelectedItem(objCurrType.getCode());
            currencyRateTextField.setText(toCurrency(this.getCandidateOtherCost().getRate()));
            amountTextField.setText(toCurrency(this.getCandidateOtherCost().getAmount()/this.getCandidateOtherCost().getRate()));
            defaultAmountTextField.setText(toCurrency(this.getCandidateOtherCost().getAmount()));
            
            nameTextField.setEditable(false);
            descriptionTextArea.setEnabled(false);
            curTypeComboBox.setEnabled(false);
            amountTextField.setEditable(true);
            amountTextField.requestFocusInWindow();
            this.calcDefaultAmount(); 
        }
        else{
            this.setCandidateOtherCost(new OtherCost());
            cmdReset();
        }
    }
    
    private void calcDefaultAmount(){
        try{
            double amount = CashierMainApp.getDoubleFromFormated(amountTextField.getText());
            double rate = CashierMainApp.getDoubleFromFormated(currencyRateTextField.getText());
            defaultAmountTextField.setText(toCurrency(amount*rate));
        }
        catch(Exception e){
            System.out.println("err Amount Format: "+e.toString());
            defaultAmountTextField.setText(toCurrency(0));
        }
    }
    
    private void cmdSave(){
        this.getCandidateOtherCost().setName(nameTextField.getText());
        this.getCandidateOtherCost().setDescription(descriptionTextArea.getText());
        this.getCandidateOtherCost().setRate(CashierMainApp.getDoubleFromFormated(currencyRateTextField.getText()));
        CurrencyType objCurrType = new CurrencyType();
        objCurrType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(curTypeComboBox.getSelectedItem());
        this.getCandidateOtherCost().setCurrencyId(objCurrType.getOID());
        this.getCandidateOtherCost().setAmount(CashierMainApp.getDoubleFromFormated(defaultAmountTextField.getText()));
        
        if(iCommand==Command.ADD){
            cmdAdd();
        }
        else if(iCommand==Command.EDIT){
            cmdEdit();
        }
        
    }
    
    private void cmdAdd(){
        this.insertOtherCost(this.getCandidateOtherCost());
        this.synchronizeTableAndModel();
        cmdReset();
    }
    
    private void cmdEdit(){
        this.updateOtherCost(this.getCandidateOtherCost());
        this.synchronizeTableAndModel();
        cmdReset();
    }
    
    private void cmdDelete(){
        this.removeOtherCost(this.getCandidateOtherCost());
        this.synchronizeTableAndModel();
        cmdReset();
    }
    
    private void cmdEditTable(){
        this.otherCostTable.setRowSelectionInterval(0,0);
        this.otherCostTable.requestFocusInWindow();
    }
    
    private void cmdSaveAll(){
        CashSaleController.setTotOtherCost(this.getTotOtherCost());
        CashSaleController.setOtherCostChoosen(this.getHashOtherCost());
        this.cmdCancel();
    }
    
    /** find valid other cost with key name */
    public OtherCost findValidOtherCostWith(String key){
        OtherCost result = null;
        try{
            OtherCost temp = (OtherCost) this.getHashOtherCost().get(key);
            if(temp!=null){
                result = new OtherCost();
                result.setAmount(temp.getAmount());
                result.setBillMainId(temp.getBillMainId());
                result.setCurrencyId(temp.getCurrencyId());
                result.setDescription(temp.getDescription());
                result.setName(temp.getName());
                result.setOID(temp.getOID());
                result.setRate(temp.getRate());
                result.setUpdateStatus(temp.getUpdateStatus());
            }
        }
        catch(Exception e){
            System.out.println("err on finValidOtherCostWiht: "+e.toString());
            result = null;
        }
        
        if(result!=null && result.getUpdateStatus()==PstOtherCost.UPDATE_STATUS_DELETED){
            result = null;
        }
        return result;
    }
    
    /** insert other Cost to hashOtherCost */
    public void insertOtherCost(OtherCost argOtherCost){
        this.setTotOtherCost(this.getTotOtherCost()+argOtherCost.getAmount());
        OtherCost foundOtherCost = findValidOtherCostWith(argOtherCost.getName());
        argOtherCost.setUpdateStatus(PstOtherCost.UPDATE_STATUS_INSERTED);
        if(foundOtherCost!=null){
            double tempAmount = argOtherCost.getAmount()+foundOtherCost.getAmount();
            foundOtherCost.setAmount(tempAmount);
            argOtherCost = foundOtherCost;
            argOtherCost.setUpdateStatus(PstOtherCost.UPDATE_STATUS_UPDATED);
        }
        else{
            this.getVectOtherCost().add(argOtherCost.getName());
        }
        
        this.getHashOtherCost().put(argOtherCost.getName(), argOtherCost);
    }
    
    /** update other Cost on hashOtherCost */
    public void updateOtherCost(OtherCost argOtherCost){
        this.setTotOtherCost(this.getTotOtherCost()+argOtherCost.getAmount());
        OtherCost foundOtherCost = findValidOtherCostWith(argOtherCost.getName());
        if(foundOtherCost!=null){
            this.setTotOtherCost(this.getTotOtherCost()-foundOtherCost.getAmount());
            if(argOtherCost.getUpdateStatus()==PstOtherCost.UPDATE_STATUS_NONE){
                argOtherCost.setUpdateStatus(PstOtherCost.UPDATE_STATUS_INSERTED);
            }
        }
        this.getHashOtherCost().put(argOtherCost.getName(), argOtherCost);
    }
    
    /** remove other cost on hashOtherCost */
    public void removeOtherCost(OtherCost argOtherCost){
        OtherCost foundOtherCost = findValidOtherCostWith(argOtherCost.getName());
        if(foundOtherCost!=null){
            this.setTotOtherCost(this.getTotOtherCost()-foundOtherCost.getAmount());
            foundOtherCost.setUpdateStatus(PstOtherCost.UPDATE_STATUS_DELETED);
            foundOtherCost.setAmount(0);
            
            argOtherCost = foundOtherCost;
            
            this.getHashOtherCost().put(argOtherCost.getName(), argOtherCost);
            this.getVectOtherCost().remove(this.getIdxOtherCost());
        }
    }
    
    public void synchronizeTableAndModel(){
        int size = this.getVectOtherCost().size();
        Vector dataVector = new Vector();
        for(int i = 0; i < size; i++){
            String key = (String) this.getVectOtherCost().get(i);
            OtherCost otherCost = (OtherCost) this.getHashOtherCost().get(key);
            if(otherCost.getUpdateStatus()!=PstOtherCost.UPDATE_STATUS_DELETED){
                Vector row = new Vector();
                row.add(""+(i+1));
                row.add(otherCost.getName());
                row.add(otherCost.getDescription());
                row.add(toCurrency(otherCost.getAmount()));
                dataVector.add(row);
            }
        }
        
        this.getOtherCostTableModel().setDataVector(dataVector,this.getColumnIdentifiers());
        this.setColumnTableSize();
        this.otherCostTable.revalidate();
        
        this.editTableButton.setEnabled(size>0);
        this.saveAllButton.setEnabled(size>0);
    }
    
    private void setColumnTableSize(){
        
        int totalSize = this.otherCostTable.getWidth();
        
        this.otherCostTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int) (0.05*totalSize));
        this.otherCostTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth((int) (0.25*totalSize));
        this.otherCostTable.getColumnModel().getColumn(COL_DESCRIPTION).setPreferredWidth((int) (0.50*totalSize));
        this.otherCostTable.getColumnModel().getColumn(COL_AMOUNT).setPreferredWidth((int) (0.20*totalSize));
        
        this.otherCostTable.validate();
        this.otherCostTable.repaint();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        listPanel = new javax.swing.JScrollPane();
        otherCostTable = new javax.swing.JTable();
        editorPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        descriptionLabel = new javax.swing.JLabel();
        currrencyLabel = new javax.swing.JLabel();
        curTypeComboBox = new javax.swing.JComboBox();
        rateLabel = new javax.swing.JLabel();
        currencyRateTextField = new javax.swing.JTextField();
        defaultAmountTextField = new javax.swing.JTextField();
        defaultAmountLabel = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        amountLabel = new javax.swing.JLabel();
        newItemButton = new javax.swing.JButton();
        editTableButton = new javax.swing.JButton();
        saveAllButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.BorderLayout(0, 2));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Other Cost Dialog");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        listPanel.setPreferredSize(new java.awt.Dimension(300, 200));
        otherCostTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Name", "Description", "Amount in Def. Cur."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        otherCostTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                otherCostTableKeyPressed(evt);
            }
        });

        listPanel.setViewportView(otherCostTable);

        getContentPane().add(listPanel, java.awt.BorderLayout.CENTER);

        editorPanel.setLayout(new java.awt.GridBagLayout());

        nameLabel.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(nameLabel, gridBagConstraints);

        nameTextField.setColumns(25);
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });
        nameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusGained(evt);
            }
        });
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(nameTextField, gridBagConstraints);

        descriptionTextArea.setColumns(35);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setRows(3);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEnabled(false);
        descriptionTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descriptionTextAreaFocusGained(evt);
            }
        });
        descriptionTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descriptionTextAreaKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(descriptionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jScrollPane1, gridBagConstraints);

        descriptionLabel.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(descriptionLabel, gridBagConstraints);

        currrencyLabel.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(currrencyLabel, gridBagConstraints);

        curTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "RP", "USD" }));
        curTypeComboBox.setEnabled(false);
        curTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curTypeComboBoxActionPerformed(evt);
            }
        });
        curTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                curTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(curTypeComboBox, gridBagConstraints);

        rateLabel.setText("Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(rateLabel, gridBagConstraints);

        currencyRateTextField.setColumns(10);
        currencyRateTextField.setEditable(false);
        currencyRateTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        currencyRateTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(currencyRateTextField, gridBagConstraints);

        defaultAmountTextField.setColumns(15);
        defaultAmountTextField.setEditable(false);
        defaultAmountTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        defaultAmountTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(defaultAmountTextField, gridBagConstraints);

        defaultAmountLabel.setText("In Def. Curr.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(defaultAmountLabel, gridBagConstraints);

        amountTextField.setColumns(10);
        amountTextField.setEditable(false);
        amountTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        amountTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountTextFieldActionPerformed(evt);
            }
        });
        amountTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountTextFieldFocusGained(evt);
            }
        });
        amountTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                amountTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                amountTextFieldKeyTyped(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(amountTextField, gridBagConstraints);

        amountLabel.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(amountLabel, gridBagConstraints);

        newItemButton.setText("New Item - F5");
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(newItemButton, gridBagConstraints);

        editTableButton.setText("Edit Table - F4");
        editTableButton.setEnabled(false);
        editTableButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editTableButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        editorPanel.add(editTableButton, gridBagConstraints);

        saveAllButton.setText("Save All - F12");
        saveAllButton.setEnabled(false);
        saveAllButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveAllButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(saveAllButton, gridBagConstraints);

        getContentPane().add(editorPanel, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed

    private void saveAllButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveAllButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_saveAllButtonKeyPressed

    private void editTableButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editTableButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_editTableButtonKeyPressed

    private void newItemButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newItemButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_newItemButtonKeyPressed

    private void amountTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountTextFieldKeyReleased
        this.calcDefaultAmount();
    }//GEN-LAST:event_amountTextFieldKeyReleased
    
    private void otherCostTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_otherCostTableKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ENTER:
                int selectedRow = this.otherCostTable.getSelectedRow();
                if(this.otherCostTable.getRowCount()==1){
                    selectedRow = 0;
                }
                
                String key = (String) this.getOtherCostTableModel().getValueAt(selectedRow, COL_NAME);
                System.out.println("ini key dari table row: "+key);
                this.setCandidateOtherCost(this.findValidOtherCostWith(key));
                this.setIdxOtherCost(selectedRow);
                this.loadToFields();
                iCommand=Command.EDIT;
                break;
            case KeyEvent.VK_DELETE:
                selectedRow = this.otherCostTable.getSelectedRow();
                if(this.otherCostTable.getRowCount()==1){
                    selectedRow = 0;
                }
                
                key = (String) this.getOtherCostTableModel().getValueAt(selectedRow, COL_NAME);
                this.setCandidateOtherCost(this.findValidOtherCostWith(key));
                this.setIdxOtherCost(selectedRow);
                int answer = JOptionPane.showConfirmDialog(this,"Are you sure to delete this item?","Delete Item",JOptionPane.OK_CANCEL_OPTION);
                if(answer==JOptionPane.OK_OPTION){
                    this.cmdDelete();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                cmdReset();
                break;
            default:
                this.getGlobalKeyListener(evt);
                break;
        }
    }//GEN-LAST:event_otherCostTableKeyPressed
    
    private void newItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemButtonActionPerformed
        cmdReset();
    }//GEN-LAST:event_newItemButtonActionPerformed
    
    private void amountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountTextFieldActionPerformed
        cmdSave();
    }//GEN-LAST:event_amountTextFieldActionPerformed
    
    private void amountTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountTextFieldKeyTyped
        calcDefaultAmount();
    }//GEN-LAST:event_amountTextFieldKeyTyped
    
    private void amountTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountTextFieldFocusGained
        amountTextField.selectAll();
    }//GEN-LAST:event_amountTextFieldFocusGained
    
    private void descriptionTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextAreaFocusGained
        descriptionTextArea.selectAll();
    }//GEN-LAST:event_descriptionTextAreaFocusGained
    
    private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusGained
        nameTextField.selectAll();
    }//GEN-LAST:event_nameTextFieldFocusGained
    
    private void amountTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountTextFieldKeyPressed
        calcDefaultAmount();
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_amountTextFieldKeyPressed
    
    private void curTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_curTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            amountTextField.setEditable(true);
            amountTextField.requestFocusInWindow();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_curTypeComboBoxKeyPressed
    
    private void descriptionTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionTextAreaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            curTypeComboBox.setEnabled(true);
            curTypeComboBox.requestFocusInWindow();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_descriptionTextAreaKeyPressed
    
    private void nameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdSaveAll();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_nameTextFieldKeyPressed
    
    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        String name = nameTextField.getText();
        if(name.length()>0){
            descriptionTextArea.setEnabled(true);
            descriptionTextArea.requestFocusInWindow();
        }
    }//GEN-LAST:event_nameTextFieldActionPerformed
    
    private void curTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curTypeComboBoxActionPerformed
        cmdCurrencyChange();
    }//GEN-LAST:event_curTypeComboBoxActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new OtherCostDialog(new JFrame(), true).show();
    }
    
    /**
     * Getter for property candidateOtherCost.
     * @return Value of property candidateOtherCost.
     */
    public OtherCost getCandidateOtherCost() {
        return candidateOtherCost;
    }
    
    /**
     * Setter for property candidateOtherCost.
     * @param candidateOtherCost New value of property candidateOtherCost.
     */
    public void setCandidateOtherCost(OtherCost candidateOtherCost) {
        this.candidateOtherCost = candidateOtherCost;
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
     * Getter for property vectOtherCost.
     * @return Value of property vectOtherCost.
     */
    public Vector getVectOtherCost() {
        if(vectOtherCost==null){
            vectOtherCost = new Vector();
        }
        return vectOtherCost;
    }
    
    /**
     * Setter for property vectOtherCost.
     * @param vectOtherCost New value of property vectOtherCost.
     */
    public void setVectOtherCost(Vector vectOtherCost) {
        this.vectOtherCost = vectOtherCost;
    }
    
    /**
     * Getter for property idxOtherCost.
     * @return Value of property idxOtherCost.
     */
    public int getIdxOtherCost() {
        return idxOtherCost;
    }
    
    /**
     * Setter for property idxOtherCost.
     * @param idxOtherCost New value of property idxOtherCost.
     */
    public void setIdxOtherCost(int idxOtherCost) {
        this.idxOtherCost = idxOtherCost;
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
     * Getter for property columnIdentifiers.
     * @return Value of property columnIdentifiers.
     */
    public Vector getColumnIdentifiers() {
        if(columnIdentifiers==null){
            columnIdentifiers = new Vector();
            columnIdentifiers.add(columnNames[COL_NO]);
            columnIdentifiers.add(columnNames[COL_NAME]);
            columnIdentifiers.add(columnNames[COL_DESCRIPTION]);
            columnIdentifiers.add(columnNames[COL_AMOUNT]);
        }
        return columnIdentifiers;
    }
    
    /**
     * Setter for property columnIdentifiers.
     * @param columnIdentifiers New value of property columnIdentifiers.
     */
    public void setColumnIdentifiers(Vector columnIdentifiers) {
        this.columnIdentifiers = columnIdentifiers;
    }
    
    /**
     * Getter for property otherCostTableModel.
     * @return Value of property otherCostTableModel.
     */
    public DefaultTableModel getOtherCostTableModel() {
        if(otherCostTableModel==null){
            otherCostTableModel = new DefaultTableModel();
            otherCostTableModel.setDataVector(new Vector(),this.getColumnIdentifiers());
        }
        return otherCostTableModel;
    }
    
    /**
     * Setter for property otherCostTableModel.
     * @param otherCostTableModel New value of property otherCostTableModel.
     */
    public void setOtherCostTableModel(DefaultTableModel otherCostTableModel) {
        this.otherCostTableModel = otherCostTableModel;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JComboBox curTypeComboBox;
    private javax.swing.JTextField currencyRateTextField;
    private javax.swing.JLabel currrencyLabel;
    private javax.swing.JLabel defaultAmountLabel;
    private javax.swing.JTextField defaultAmountTextField;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton editTableButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane listPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton newItemButton;
    private javax.swing.JTable otherCostTable;
    private javax.swing.JLabel rateLabel;
    private javax.swing.JButton saveAllButton;
    // End of variables declaration//GEN-END:variables
    
}
