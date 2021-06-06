/*
 * CashRateDialog.java
 *
 * Created on January 18, 2006, 10:25 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.printAPI.PrintBalancePOS;
import com.dimata.common.entity.payment.*;
import com.dimata.util.Formater;
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
/**
 *
 * @author  pulantara
 */
public class CashRateDialog extends javax.swing.JDialog {
    
    /** Creates new form CashRateDialog */
    public CashRateDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initFields();
        initState();
    }
    
    
    private void initFields(){
        
        // init currency
        currencyCode = CashierMainApp.getHashCurrencyType();
        currencyOID = CashierMainApp.getHashCurrencyId();
        currentCurrCode = CashierMainApp.getCurrencyCodeUsed();
        this.setStandartRateCandidate(null);
        this.setDailyRateCandidate(null);
        
        // set currency combo box
        currencyComboBox.setModel(new DefaultComboBoxModel(currencyCode.keySet().toArray()));
        currencyComboBox.setSelectedItem(currentCurrCode);
        cmdCurrencyChange();
        
        // deactivate login panel before all balance item inserted
        deactivateLoginPanel();
        
    }
    
    /** init the default state of this dialog form */
    private void initState(){
        valueTextField.setEnabled(false);
        currencyComboBox.requestFocusInWindow();
    }
    
    private void cmdCurrencyChange(){
        currencyRateTextField.setText(toCurrency(getCurrencyRate((String) currencyComboBox.getSelectedItem())));
    }
    
    private String toCurrency(double dValue){
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }
    
    private double getCurrencyRate(String stCode){
        
        double result = 0;
        CurrencyType objCurrType = new CurrencyType();
        StandartRate objRate = new StandartRate();
        DailyRate objDaily = new DailyRate();
        objCurrType = (CurrencyType) currencyCode.get(stCode);
        objRate = CashSaleController.getStandartRate(objCurrType.getOID()+"");
        objDaily = CashSaleController.getLatestDailyRate(objCurrType.getOID());
        this.setStandartRateCandidate(objRate);
        this.setDailyRateCandidate(objDaily);
        result = objRate.getSellingRate();
        return result;
    }
    
    private void activateLoginPanel(){
        loginPanel.setVisible(true);
        userNameTextField.requestFocusInWindow();
    }
    private void deactivateLoginPanel(){
        loginPanel.setVisible(false);
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
                break;
            case KeyEvent.VK_F10:
                break;
            case KeyEvent.VK_F11:
                break;
            case KeyEvent.VK_F12:
                break;
            case KeyEvent.VK_ESCAPE:
                cmdClose();
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
    
    private void cmdLogin(){
        AppUser objUser = new AppUser();
        objUser = PstAppUser.getByLoginIDAndPassword(userNameTextField.getText(), String.copyValueOf(uPasswordField.getPassword()));
        if (objUser == null || objUser.getLoginId().length()==0 || objUser.getLoginId()==null){
            JOptionPane.showMessageDialog(this,"Can't Login, check your User ID and Password","Supervisor Login",JOptionPane.OK_OPTION);
            userNameTextField.requestFocusInWindow();
        }
        else{
            if(objUser.getUserGroupNew()==PstAppUser.GROUP_SUPERVISOR) {
                cmdSave();
                cmdClose();
            }
            else{
                JOptionPane.showMessageDialog(this,"Unauthorized user(supervisor only)","Supervisor Login",JOptionPane.OK_CANCEL_OPTION);
                userNameTextField.requestFocusInWindow();
            }
        }
    }
    
    // activate editorPanel
    private void activateEditorPanel(){
        editorPanel.setVisible(true);
        initState();
    }
    
    // deactivate editorPanel
    private void deactivateEditorPanel(){
        editorPanel.setVisible(false);
    }
    
    private void cmdCancelLogin(){
        userNameTextField.setText("");
        uPasswordField.setText("");
        deactivateLoginPanel();
        activateEditorPanel();
    }
    
    private boolean cmdSave(){
        boolean result = false;
        if(this.getStandartRateCandidate()!=null && this.getDailyRateCandidate()!=null){
            try{
                PstStandartRate.updateExc(this.getStandartRateCandidate());
                PstDailyRate.updateExc(this.getDailyRateCandidate());
                result = true;
            }
            catch(Exception e){
                System.out.println("err on cmdSave(): "+e.toString());
                result = false;
            }
        }
        return result;
    }
    
    private void cmdClose(){
        this.dispose();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        editorPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        valueTextField = new javax.swing.JTextField();
        currencyComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        currencyRateTextField = new javax.swing.JTextField();
        loginPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        uPasswordField = new javax.swing.JPasswordField();
        cancelLoginButton = new javax.swing.JButton();
        okLoginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Exchange Rate Dialog");
        editorPanel.setLayout(new java.awt.GridBagLayout());

        editorPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jLabel10.setText("Currrency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel10, gridBagConstraints);

        jLabel11.setText("New Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel11, gridBagConstraints);

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
        editorPanel.add(valueTextField, gridBagConstraints);

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
        editorPanel.add(currencyComboBox, gridBagConstraints);

        jLabel13.setText("Current Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel13, gridBagConstraints);

        currencyRateTextField.setColumns(10);
        currencyRateTextField.setEditable(false);
        currencyRateTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(currencyRateTextField, gridBagConstraints);

        getContentPane().add(editorPanel, java.awt.BorderLayout.CENTER);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        loginPanel.add(okLoginButton, gridBagConstraints);

        getContentPane().add(loginPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void valueTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valueTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            currencyComboBox.requestFocusInWindow();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_valueTextFieldKeyPressed
    
    private void uPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_uPasswordFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            userNameTextField.requestFocusInWindow();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_uPasswordFieldKeyPressed
    
    private void uPasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_uPasswordFieldFocusGained
        uPasswordField.selectAll();
    }//GEN-LAST:event_uPasswordFieldFocusGained
    
    private void userNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userNameTextFieldFocusGained
        userNameTextField.selectAll();
    }//GEN-LAST:event_userNameTextFieldFocusGained
    
    private void userNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userNameTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdCancelLogin();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_userNameTextFieldKeyPressed
    
    private void valueTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueTextFieldFocusGained
        valueTextField.selectAll();
    }//GEN-LAST:event_valueTextFieldFocusGained
    
    private void okLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okLoginButtonActionPerformed
        cmdLogin();
    }//GEN-LAST:event_okLoginButtonActionPerformed
    
    private void cancelLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelLoginButtonActionPerformed
        cmdCancelLogin();
    }//GEN-LAST:event_cancelLoginButtonActionPerformed
    
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
            this.getStandartRateCandidate().setSellingRate(parsed);
            this.getDailyRateCandidate().setSellingRate(parsed);
            activateLoginPanel();
        }
        else{
            JOptionPane.showMessageDialog(this,"Input must be decimal number","CashBalance Dialog",JOptionPane.OK_CANCEL_OPTION);
            valueTextField.setText("");
        }
    }//GEN-LAST:event_valueTextFieldActionPerformed
    
    private void currencyComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_currencyComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            valueTextField.setEnabled(true);
            valueTextField.setText(currencyRateTextField.getText());
            valueTextField.requestFocusInWindow();
        }
        else if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdClose();
        }
        else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_currencyComboBoxKeyPressed
    
    private void currencyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currencyComboBoxActionPerformed
        cmdCurrencyChange();
    }//GEN-LAST:event_currencyComboBoxActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new CashRateDialog(new javax.swing.JFrame(), true).show();
    }
    
    /**
     * Getter for property currencyCode.
     * @return Value of property currencyCode.
     */
    public java.util.Hashtable getCurrencyCode() {
        return currencyCode;
    }
    
    /**
     * Setter for property currencyCode.
     * @param currencyCode New value of property currencyCode.
     */
    public void setCurrencyCode(java.util.Hashtable currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    /**
     * Getter for property currencyOID.
     * @return Value of property currencyOID.
     */
    public java.util.Hashtable getCurrencyOID() {
        return currencyOID;
    }
    
    /**
     * Setter for property currencyOID.
     * @param currencyOID New value of property currencyOID.
     */
    public void setCurrencyOID(java.util.Hashtable currencyOID) {
        this.currencyOID = currencyOID;
    }
    
    /**
     * Getter for property currentCurrCode.
     * @return Value of property currentCurrCode.
     */
    public java.lang.String getCurrentCurrCode() {
        return currentCurrCode;
    }
    
    /**
     * Setter for property currentCurrCode.
     * @param currentCurrCode New value of property currentCurrCode.
     */
    public void setCurrentCurrCode(java.lang.String currentCurrCode) {
        this.currentCurrCode = currentCurrCode;
    }
    
    /**
     * Getter for property standartRateCandidate.
     * @return Value of property standartRateCandidate.
     */
    public com.dimata.common.entity.payment.StandartRate getStandartRateCandidate() {
        return standartRateCandidate;
    }
    
    /**
     * Setter for property standartRateCandidate.
     * @param standartRateCandidate New value of property standartRateCandidate.
     */
    public void setStandartRateCandidate(com.dimata.common.entity.payment.StandartRate standartRateCandidate) {
        this.standartRateCandidate = standartRateCandidate;
    }
    
    /**
     * Getter for property dailyRateCandidate.
     * @return Value of property dailyRateCandidate.
     */
    public com.dimata.common.entity.payment.DailyRate getDailyRateCandidate() {
        return dailyRateCandidate;
    }
    
    /**
     * Setter for property dailyRateCandidate.
     * @param dailyRateCandidate New value of property dailyRateCandidate.
     */
    public void setDailyRateCandidate(com.dimata.common.entity.payment.DailyRate dailyRateCandidate) {
        this.dailyRateCandidate = dailyRateCandidate;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelLoginButton;
    private javax.swing.JComboBox currencyComboBox;
    private javax.swing.JTextField currencyRateTextField;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JButton okLoginButton;
    private javax.swing.JPasswordField uPasswordField;
    private javax.swing.JTextField userNameTextField;
    private javax.swing.JTextField valueTextField;
    // End of variables declaration//GEN-END:variables
    private Hashtable currencyCode = new Hashtable();
    private Hashtable currencyOID = new Hashtable();
    private String currentCurrCode;
    private StandartRate standartRateCandidate = null;
    private DailyRate dailyRateCandidate = null;
}
