/*
 * FtpConfigDialog.java
 *
 * Created on March 9, 2006, 9:41 AM
 */

package com.dimata.pos.cashier;

import com.dimata.pos.ftpapi.*;
import java.util.*;
import javax.swing.JOptionPane;
import java.awt.event.*;

/**
 *
 * @author  pulantara
 */
public class FtpConfigDialog extends javax.swing.JDialog {
    
    private Hashtable params = new Hashtable();
        
    /** Creates new form FtpConfigDialog */
    public FtpConfigDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initFields();
    }
    
    private void initFields(){
        try{
            params = CashierFtpParamHandler.getAllConfig();
            loadToFields();
        }
        catch(Exception e){
            System.out.println("err on initFields() = "+e.toString());
        }
        
        remoteInDirUrlTextField.requestFocusInWindow();
    }
    
    private void loadToFields(){
        try{
            
            remoteInDirUrlTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_IN_DIR_URL]));
            remoteOutDirUrlTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_OUT_DIR_URL]));
            
            localInDirUrlTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.LOCAL_IN_DIR_URL]));
            localOutDirUrlTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.LOCAL_OUT_DIR_URL]));
            
            remoteHostNameTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_HOST]));
            remotePortTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_PORT]));
            userNameTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_USER]));
            userPasswordField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_PASSWORD]));
            
            ftpModeComboBox.setSelectedItem((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.FTP_MODE]));
            connModeComboBox.setSelectedItem((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.CONNECTION_MODE]));
            
            intervalTextField.setText((String) params.get(CashierFtpConstant.fieldNames[CashierFtpConstant.SERVICE_INTERVAL])); 
        }
        catch(Exception e){
            System.out.println("err on loadToFields() = "+e.toString());
        }
    }
    
    private void getFromFields(){
        try{
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_IN_DIR_URL], remoteInDirUrlTextField.getText());
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_OUT_DIR_URL], remoteOutDirUrlTextField.getText());
            
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.LOCAL_IN_DIR_URL], localInDirUrlTextField.getText());
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.LOCAL_OUT_DIR_URL], localOutDirUrlTextField.getText());
            
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_HOST], remoteHostNameTextField.getText());
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_PORT], remotePortTextField.getText());
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_USER], userNameTextField.getText());
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_PASSWORD], userPasswordField.getText());
            
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.FTP_MODE], (String) ftpModeComboBox.getSelectedItem());
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.CONNECTION_MODE], (String) connModeComboBox.getSelectedItem());
            
            params.put(CashierFtpConstant.fieldNames[CashierFtpConstant.SERVICE_INTERVAL],  intervalTextField.getText());
        }
        catch(Exception e){
            System.out.println("err on getFromFields() = "+e.toString());
        }
    }
    
    private synchronized void cmdSave(){
        getFromFields();
        if(CashierFtpParamHandler.saveParam(CashierFtpConstant.FTP_PARAM, params)){
            initFields();
        }
        else{
            JOptionPane.showMessageDialog(this,"Can't save/update FTP parameters","FTP Configuration Error",JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private synchronized void cmdConnect(){
        CashierFtpApi ftpApi = new CashierFtpApi(params);
        if(ftpApi.connectFTP()){
            JOptionPane.showMessageDialog(this,"FTP Connection OK","FTP Connection Information",JOptionPane.INFORMATION_MESSAGE); 
        }
        else{
            JOptionPane.showMessageDialog(this,"FTP Connection FAILED","FTP Connection Information",JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private void cmdClose(){
        this.dispose();
    }
    
    private synchronized void cmdGetData(){
        CashierFtpApi ftpApi = new CashierFtpApi(params);
        if(!ftpApi.connectFTP()){
            JOptionPane.showMessageDialog(this,"FTP Connection FAILED","FTP Connection Information",JOptionPane.WARNING_MESSAGE); 
        }
        if(ftpApi.getAllFiles()){
            JOptionPane.showMessageDialog(this,"Retriving Data SUCCESS","Data Retrive Information",JOptionPane.INFORMATION_MESSAGE); 
        }
        else{
            JOptionPane.showMessageDialog(this,"Retriving Data FAILED","Data Retrive Information",JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private synchronized void cmdPutData(){
        CashierFtpApi ftpApi = new CashierFtpApi(params);
        if(!ftpApi.connectFTP()){
            JOptionPane.showMessageDialog(this,"FTP Connection FAILED","FTP Connection Information",JOptionPane.WARNING_MESSAGE); 
        }
        if(ftpApi.putAllFiles()){
            JOptionPane.showMessageDialog(this,"Uploading Data SUCCESS","Data Upload Information",JOptionPane.INFORMATION_MESSAGE); 
        }
        else{
            JOptionPane.showMessageDialog(this,"Uploading Data FAILED","Data Upload Information",JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    public synchronized void getGlobalKeyListener(KeyEvent evt){
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
                synchButton.setEnabled(false);
                cmdGetData();
                cmdPutData();
                synchButton.setEnabled(true);
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
                saveSettingButton.setEnabled(false);
                cmdSave();
                cmdConnect();
                saveSettingButton.setEnabled(true); 
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        inputPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        remoteInDirUrlTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        remoteOutDirUrlTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        intervalTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        localOutDirUrlTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        remoteHostNameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        remotePortTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ftpModeComboBox = new javax.swing.JComboBox();
        connModeComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        localInDirUrlTextField = new javax.swing.JTextField();
        userPasswordField = new javax.swing.JPasswordField();
        commandPanel = new javax.swing.JPanel();
        saveSettingButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        runAsServiceButton = new javax.swing.JButton();
        synchButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FTP Connection Dialog");
        inputPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Remote Input Directory URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel1, gridBagConstraints);

        remoteInDirUrlTextField.setColumns(15);
        remoteInDirUrlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remoteInDirUrlTextFieldActionPerformed(evt);
            }
        });
        remoteInDirUrlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remoteInDirUrlTextFieldFocusGained(evt);
            }
        });
        remoteInDirUrlTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remoteInDirUrlTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(remoteInDirUrlTextField, gridBagConstraints);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Remote Output Directory URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel2, gridBagConstraints);

        remoteOutDirUrlTextField.setColumns(15);
        remoteOutDirUrlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remoteOutDirUrlTextFieldActionPerformed(evt);
            }
        });
        remoteOutDirUrlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remoteOutDirUrlTextFieldFocusGained(evt);
            }
        });
        remoteOutDirUrlTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remoteOutDirUrlTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(remoteOutDirUrlTextField, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Synch Interval (minutes)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel3, gridBagConstraints);

        intervalTextField.setColumns(5);
        intervalTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                intervalTextFieldFocusGained(evt);
            }
        });
        intervalTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                intervalTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(intervalTextField, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Local Out Directory URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel4, gridBagConstraints);

        localOutDirUrlTextField.setColumns(20);
        localOutDirUrlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localOutDirUrlTextFieldActionPerformed(evt);
            }
        });
        localOutDirUrlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                localOutDirUrlTextFieldFocusGained(evt);
            }
        });
        localOutDirUrlTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                localOutDirUrlTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(localOutDirUrlTextField, gridBagConstraints);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Remote Host Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel5, gridBagConstraints);

        remoteHostNameTextField.setColumns(20);
        remoteHostNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remoteHostNameTextFieldActionPerformed(evt);
            }
        });
        remoteHostNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remoteHostNameTextFieldFocusGained(evt);
            }
        });
        remoteHostNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remoteHostNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(remoteHostNameTextField, gridBagConstraints);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Remote Port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel6, gridBagConstraints);

        remotePortTextField.setColumns(4);
        remotePortTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remotePortTextFieldActionPerformed(evt);
            }
        });
        remotePortTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remotePortTextFieldFocusGained(evt);
            }
        });
        remotePortTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remotePortTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(remotePortTextField, gridBagConstraints);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("User Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel7, gridBagConstraints);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("FTP Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel8, gridBagConstraints);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Connection Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel9, gridBagConstraints);

        ftpModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BINARY", "ASCII" }));
        ftpModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftpModeComboBoxActionPerformed(evt);
            }
        });
        ftpModeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ftpModeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(ftpModeComboBox, gridBagConstraints);

        connModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ACTIVE", "PASIVE" }));
        connModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connModeComboBoxActionPerformed(evt);
            }
        });
        connModeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                connModeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(connModeComboBox, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("MS Sans Serif", 1, 11));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Mode Setting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("MS Sans Serif", 1, 11));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Remote Host Setting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel11, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("MS Sans Serif", 1, 11));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Service Setting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("MS Sans Serif", 1, 11));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Remote Directory Setting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(jLabel13, gridBagConstraints);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("User Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel14, gridBagConstraints);

        userNameTextField.setColumns(15);
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(userNameTextField, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("MS Sans Serif", 1, 11));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Local Directory Setting (use '\\\\' instead '\\')");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        inputPanel.add(jLabel15, gridBagConstraints);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Local Input Directory URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(jLabel16, gridBagConstraints);

        localInDirUrlTextField.setColumns(20);
        localInDirUrlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localInDirUrlTextFieldActionPerformed(evt);
            }
        });
        localInDirUrlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                localInDirUrlTextFieldFocusGained(evt);
            }
        });
        localInDirUrlTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                localInDirUrlTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(localInDirUrlTextField, gridBagConstraints);

        userPasswordField.setColumns(18);
        userPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userPasswordFieldActionPerformed(evt);
            }
        });
        userPasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userPasswordFieldFocusGained(evt);
            }
        });
        userPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                userPasswordFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        inputPanel.add(userPasswordField, gridBagConstraints);

        getContentPane().add(inputPanel, java.awt.BorderLayout.CENTER);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        saveSettingButton.setText("Save/Test Setting (F12)");
        saveSettingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSettingButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        commandPanel.add(saveSettingButton, gridBagConstraints);

        editButton.setText("Cancel (ESC)");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        commandPanel.add(editButton, gridBagConstraints);

        runAsServiceButton.setText("Run As Service (F2)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        commandPanel.add(runAsServiceButton, gridBagConstraints);

        synchButton.setText("Sync Data (F6)");
        synchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                synchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        commandPanel.add(synchButton, gridBagConstraints);

        getContentPane().add(commandPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void intervalTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_intervalTextFieldFocusGained
        intervalTextField.selectAll();
    }//GEN-LAST:event_intervalTextFieldFocusGained

    private void userPasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userPasswordFieldFocusGained
        userPasswordField.selectAll();
    }//GEN-LAST:event_userPasswordFieldFocusGained

    private void userNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userNameTextFieldFocusGained
        userNameTextField.selectAll();
    }//GEN-LAST:event_userNameTextFieldFocusGained

    private void remotePortTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remotePortTextFieldFocusGained
        remotePortTextField.selectAll();
    }//GEN-LAST:event_remotePortTextFieldFocusGained

    private void remoteHostNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remoteHostNameTextFieldFocusGained
        remoteHostNameTextField.selectAll();
    }//GEN-LAST:event_remoteHostNameTextFieldFocusGained

    private void localOutDirUrlTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_localOutDirUrlTextFieldFocusGained
        localOutDirUrlTextField.selectAll();
    }//GEN-LAST:event_localOutDirUrlTextFieldFocusGained

    private void localInDirUrlTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_localInDirUrlTextFieldFocusGained
        localInDirUrlTextField.selectAll();
    }//GEN-LAST:event_localInDirUrlTextFieldFocusGained

    private void remoteOutDirUrlTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remoteOutDirUrlTextFieldFocusGained
        remoteOutDirUrlTextField.selectAll();
    }//GEN-LAST:event_remoteOutDirUrlTextFieldFocusGained

    private void remoteInDirUrlTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remoteInDirUrlTextFieldFocusGained
        remoteInDirUrlTextField.selectAll();
    }//GEN-LAST:event_remoteInDirUrlTextFieldFocusGained

    private void connModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connModeComboBoxActionPerformed
        intervalTextField.requestFocusInWindow();
    }//GEN-LAST:event_connModeComboBoxActionPerformed

    private void ftpModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftpModeComboBoxActionPerformed
        connModeComboBox.requestFocusInWindow();
    }//GEN-LAST:event_ftpModeComboBoxActionPerformed

    private void userPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userPasswordFieldActionPerformed
        if(userPasswordField.getText().length()>0){
            ftpModeComboBox.requestFocusInWindow();
        }
    }//GEN-LAST:event_userPasswordFieldActionPerformed

    private void userNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameTextFieldActionPerformed
        if(userNameTextField.getText().length()>0){
            userPasswordField.requestFocusInWindow();
        }
    }//GEN-LAST:event_userNameTextFieldActionPerformed

    private void remotePortTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remotePortTextFieldActionPerformed
        if(remotePortTextField.getText().length()>0){
            userNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_remotePortTextFieldActionPerformed

    private void remoteHostNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remoteHostNameTextFieldActionPerformed
        if(remoteHostNameTextField.getText().length()>0){
            remotePortTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_remoteHostNameTextFieldActionPerformed

    private void localOutDirUrlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_localOutDirUrlTextFieldActionPerformed
        if(localOutDirUrlTextField.getText().length()>0){
            remoteHostNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_localOutDirUrlTextFieldActionPerformed

    private void localInDirUrlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_localInDirUrlTextFieldActionPerformed
        if(localInDirUrlTextField.getText().length()>0){
            localOutDirUrlTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_localInDirUrlTextFieldActionPerformed

    private void remoteOutDirUrlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remoteOutDirUrlTextFieldActionPerformed
        if(remoteOutDirUrlTextField.getText().length()>0){
            localInDirUrlTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_remoteOutDirUrlTextFieldActionPerformed

    private void remoteInDirUrlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remoteInDirUrlTextFieldActionPerformed
        if(remoteInDirUrlTextField.getText().length()>0){
            remoteOutDirUrlTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_remoteInDirUrlTextFieldActionPerformed

    private void intervalTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_intervalTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_intervalTextFieldKeyPressed

    private void connModeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_connModeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            saveSettingButton.requestFocusInWindow();
        }
        else{
            this.getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_connModeComboBoxKeyPressed

    private void ftpModeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftpModeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            connModeComboBox.requestFocusInWindow();
        }
        else{
            this.getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_ftpModeComboBoxKeyPressed

    private void userPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userPasswordFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_userPasswordFieldKeyPressed

    private void userNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userNameTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_userNameTextFieldKeyPressed

    private void remotePortTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remotePortTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_remotePortTextFieldKeyPressed

    private void remoteHostNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remoteHostNameTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_remoteHostNameTextFieldKeyPressed

    private void localOutDirUrlTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_localOutDirUrlTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_localOutDirUrlTextFieldKeyPressed

    private void localInDirUrlTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_localInDirUrlTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_localInDirUrlTextFieldKeyPressed

    private void remoteOutDirUrlTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remoteOutDirUrlTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_remoteOutDirUrlTextFieldKeyPressed

    private void remoteInDirUrlTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remoteInDirUrlTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_remoteInDirUrlTextFieldKeyPressed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        cmdClose();
    }//GEN-LAST:event_editButtonActionPerformed

    private void synchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_synchButtonActionPerformed
        synchButton.setEnabled(false);
        cmdGetData();
        cmdPutData();
        synchButton.setEnabled(true);
    }//GEN-LAST:event_synchButtonActionPerformed

    private void saveSettingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSettingButtonActionPerformed
        saveSettingButton.setEnabled(false);
        cmdSave();
        cmdConnect();
        saveSettingButton.setEnabled(true);
    }//GEN-LAST:event_saveSettingButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new FtpConfigDialog(new javax.swing.JFrame(), true).show();
    }
    
    /**
     * Getter for property params.
     * @return Value of property params.
     */
    public java.util.Hashtable getParams() {
        return params;
    }
    
    /**
     * Setter for property params.
     * @param params New value of property params.
     */
    public void setParams(java.util.Hashtable params) {
        this.params = params;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel commandPanel;
    private javax.swing.JComboBox connModeComboBox;
    private javax.swing.JButton editButton;
    private javax.swing.JComboBox ftpModeComboBox;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField intervalTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField localInDirUrlTextField;
    private javax.swing.JTextField localOutDirUrlTextField;
    private javax.swing.JTextField remoteHostNameTextField;
    private javax.swing.JTextField remoteInDirUrlTextField;
    private javax.swing.JTextField remoteOutDirUrlTextField;
    private javax.swing.JTextField remotePortTextField;
    private javax.swing.JButton runAsServiceButton;
    private javax.swing.JButton saveSettingButton;
    private javax.swing.JButton synchButton;
    private javax.swing.JTextField userNameTextField;
    private javax.swing.JPasswordField userPasswordField;
    // End of variables declaration//GEN-END:variables
    
}
