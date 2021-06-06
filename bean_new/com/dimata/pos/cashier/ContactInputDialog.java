/*
 * ContactInputDialog.java
 *
 * Created on August 12, 2002, 2:24 PM
 */

package com.dimata.pos.cashier;

import com.dimata.common.entity.contact.ContactClass;
import com.dimata.common.entity.contact.ContactClassAssign;
import com.dimata.common.entity.contact.PstContactClassAssign;
import com.dimata.posbo.entity.masterdata.*;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author  pulantara
 */
public class ContactInputDialog extends JDialog {
    
    // ATRIBUTE
    
    /** Holds candidate for MemberReg */
    private MemberReg candidateMemberReg = new MemberReg();
    
    /** Holds hashtable for member type with member type name as key */
    private Hashtable hashMemberTypeByName = new Hashtable();
    
    /** Holds hashtable for member type with member type oid as key */
    private Hashtable hashMemberTypeById = new Hashtable();
    
    // CONSTRUCTOR
    
    /** Creates new form ContactInputDialog */
    public ContactInputDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initFields();
    }
    
    // METHODE
    
    /** init fields */
    public void initFields(){
        
        // init member type / price type combo box
        
        Vector listMemberType = PstMemberGroup.list(0,0,"","");
        Vector modelData = new Vector();
        for(int i = 0; i < listMemberType.size(); i++){
            MemberGroup group = (MemberGroup) listMemberType.get(i);
            // add to model
            modelData.add(group.getName());
            // add hashtable
            this.getHashMemberTypeByName().put(group.getName(), group.getOID()+"");
            this.getHashMemberTypeById().put(group.getOID()+"", group.getName());
        }
        // set model
        priceTypeComboBox.setModel(new DefaultComboBoxModel(modelData));
        // get default oid from cash config
        long oid = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).priceMapId);
        // set default selected item on price type combo box
        priceTypeComboBox.setSelectedItem((String) this.getHashMemberTypeById().get(oid+""));
        // init fiels state
        initState();
    }
    
    /** init fields's state */
    private void initState(){
        
        // editor state
        codeTextField.setEditable(true);
        nameTextField.setEditable(false);
        priceTypeComboBox.setEnabled(false);
        addressTextArea.setEnabled(false);
        cityTextField.setEditable(false);
        countryTextField.setEditable(false);
        emailTextField.setEditable(false);
        phoneTextField.setEditable(false);
        faxTextField.setEditable(false);
        
        // command button state
        checkButton.setEnabled(false);
        resetButton.setEnabled(true);
        saveButton.setEnabled(false);
        closeButton.setEnabled(true);
        
        // set focus
        codeTextField.requestFocusInWindow();
    }
    
    
    
    /** global key listener for this dialog */
    public void getGlobalKeyListener(KeyEvent evt){
        switch(evt.getKeyCode()){
            case KeyEvent.VK_F1 :
                if(checkButton.isEnabled()){
                    cmdCheck();
                }
                break;
            case KeyEvent.VK_F2:
                break;
            case KeyEvent.VK_F3:
                break;
            case KeyEvent.VK_F4:
                break;
            case KeyEvent.VK_F5:
                if(resetButton.isEnabled()){
                    cmdReset();
                }
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
                if(saveButton.isEnabled()){
                    cmdSave();
                }
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
    
    /** action taken when checkButton pressed */
    private void cmdCheck(){
        CashSaleController.setMemberChoosen(null);
        CashSaleController.showMemberSearch(null, nameTextField.getText(), codeTextField.getText());
        Vector memberList = CashSaleController.getMemberChoosen();
        if(memberList.size()==1){
            this.setCandidateMemberReg((MemberReg) memberList.get(0));
            try{
                this.setCandidateMemberReg(PstMemberReg.fetchExc(this.getCandidateMemberReg().getOID()));
            }
            catch(Exception e){
                System.out.println("err on fetching Member = "+e.toString());
            }
            this.loadToFields();
        }
        else{
            JOptionPane.showMessageDialog(this,"Member Not Found","Member Not Found",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** action taken when resetButton pressed */
    public void cmdReset(){
        this.initState();
    }
    
    /** action taken when saveButton pressed */
    private void cmdSave(){
        this.getFromFields();
        if(this.getCandidateMemberReg().getOID()>0){
            System.out.println("oid yg disearch:"+this.getCandidateMemberReg().getOID());
            try{
                PstMemberReg.updateExc(this.getCandidateMemberReg());
            }
            catch(Exception e){
                System.out.println("err on cmdSave(): "+e.toString());
            }
        }
        else{
            try{
                long oid = PstMemberReg.insertExc(this.getCandidateMemberReg());
                
                ContactClass cc = PstMemberReg.getContactClass();
                ContactClassAssign cca = new ContactClassAssign();
                cca.setContactClassId(cc.getOID());
                cca.setContactId(oid);
                PstContactClassAssign.insertExc(cca);
                
                Date today = new Date();
                Date expDate = new Date(today.getYear()+10,today.getMonth(),today.getDay());
                MemberRegistrationHistory memberHistory = new MemberRegistrationHistory();
                memberHistory.setMemberId(oid);
                memberHistory.setRegistrationDate(today);
                memberHistory.setValidStartDate(today);
                memberHistory.setValidExpiredDate(expDate);
                PstMemberRegistrationHistory.insertExc(memberHistory);
                
            }
            catch(Exception e){
                System.out.println("err on cmdSave(): "+e.toString());
            }
        }
        cmdClose();
    }
    
    /* action taken when closeButton pressed */
    private void cmdClose(){
        this.setCandidateMemberReg(new MemberReg());
        this.dispose();
    }
    
    /** return true if is available to run check action */
    private boolean isAvailToCheck(){
        return codeTextField.getText().length()>0;
    }
    
    /** load current candidate to fields */
    public void loadToFields(){
        if(this.getCandidateMemberReg()!=null){
            
            // load each property into corresponding fields
            if(this.getCandidateMemberReg().getMemberBarcode().length()>0){
                codeTextField.setText(this.getCandidateMemberReg().getMemberBarcode()); 
            }
            else{
                codeTextField.setText(this.getCandidateMemberReg().getContactCode());
            }
            if(this.getCandidateMemberReg().getPersonName().length()>0){
                nameTextField.setText(this.getCandidateMemberReg().getPersonName());
            }
            else{
                nameTextField.setText(this.getCandidateMemberReg().getCompName());
            }
            if(this.getCandidateMemberReg().getHomeAddr().length()>0){
                addressTextArea.setText(this.getCandidateMemberReg().getHomeAddr());
            }
            else{
                addressTextArea.setText(this.getCandidateMemberReg().getBussAddress());
            }
            if(this.getCandidateMemberReg().getHomeTown().length()>0){
                cityTextField.setText(this.getCandidateMemberReg().getHomeTown());
            }
            else{
                cityTextField.setText(this.getCandidateMemberReg().getTown());
            }
            if(this.getCandidateMemberReg().getHomeCountry().length()>0){
                countryTextField.setText(this.getCandidateMemberReg().getHomeCountry());
            }
            else{
                countryTextField.setText(this.getCandidateMemberReg().getCountry());
            }
            emailTextField.setText(this.getCandidateMemberReg().getEmail());
            if(this.getCandidateMemberReg().getHomeTelp().length()>0){
                phoneTextField.setText(this.getCandidateMemberReg().getHomeTelp());
            }
            else{
                phoneTextField.setText(this.getCandidateMemberReg().getTelpNr());
            }
            if(this.getCandidateMemberReg().getHomeFax().length()>0){
                faxTextField.setText(this.getCandidateMemberReg().getHomeFax());
            }
            else{
                faxTextField.setText(this.getCandidateMemberReg().getFax());
            }
            // set focus for code text field
            codeTextField.requestFocusInWindow();
        }
    }
    
    /** get current candidate from fields */
    private void getFromFields(){
        
        if(this.getCandidateMemberReg()==null){
            this.setCandidateMemberReg(new MemberReg());
        }
        this.getCandidateMemberReg().setContactCode(codeTextField.getText());
        this.getCandidateMemberReg().setMemberBarcode(codeTextField.getText()); 
        this.getCandidateMemberReg().setPersonName(nameTextField.getText());
        this.getCandidateMemberReg().setHomeAddr(addressTextArea.getText());
        this.getCandidateMemberReg().setBussAddress(addressTextArea.getText());
        this.getCandidateMemberReg().setHomeTown(cityTextField.getText());
        this.getCandidateMemberReg().setTown(cityTextField.getText());
        this.getCandidateMemberReg().setHomeCountry(countryTextField.getText());
        this.getCandidateMemberReg().setCountry(countryTextField.getText());
        this.getCandidateMemberReg().setEmail(emailTextField.getText());
        this.getCandidateMemberReg().setHomeTelp(phoneTextField.getText());
        this.getCandidateMemberReg().setTelpNr(phoneTextField.getText());
        this.getCandidateMemberReg().setHomeFax(faxTextField.getText());
        this.getCandidateMemberReg().setFax(faxTextField.getText());
        this.getCandidateMemberReg().setMemberGroupId(Long.parseLong((String) this.getHashMemberTypeByName().get(priceTypeComboBox.getSelectedItem())));
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        editorPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codeTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        countryTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        addressTextArea = new javax.swing.JTextArea();
        cityTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        faxTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        priceTypeComboBox = new javax.swing.JComboBox();
        commandPanel = new javax.swing.JPanel();
        checkButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Contact Input Dialog");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        editorPanel.setLayout(new java.awt.GridBagLayout());

        editorPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jLabel1.setText("Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel1, gridBagConstraints);

        codeTextField.setColumns(15);
        codeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeTextFieldActionPerformed(evt);
            }
        });
        codeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codeTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codeTextFieldKeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(codeTextField, gridBagConstraints);

        jLabel2.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel2, gridBagConstraints);

        nameTextField.setColumns(25);
        nameTextField.setEditable(false);
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(nameTextField, gridBagConstraints);

        jLabel3.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel3, gridBagConstraints);

        countryTextField.setColumns(12);
        countryTextField.setEditable(false);
        countryTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryTextFieldActionPerformed(evt);
            }
        });
        countryTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                countryTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(countryTextField, gridBagConstraints);

        jLabel4.setText("City / Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel4, gridBagConstraints);

        emailTextField.setColumns(25);
        emailTextField.setEditable(false);
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        emailTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                emailTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(emailTextField, gridBagConstraints);

        addressTextArea.setColumns(30);
        addressTextArea.setLineWrap(true);
        addressTextArea.setRows(3);
        addressTextArea.setWrapStyleWord(true);
        addressTextArea.setEnabled(false);
        addressTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addressTextAreaKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(addressTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jScrollPane1, gridBagConstraints);

        cityTextField.setColumns(12);
        cityTextField.setEditable(false);
        cityTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityTextFieldActionPerformed(evt);
            }
        });
        cityTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cityTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(cityTextField, gridBagConstraints);

        jLabel5.setText("Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Phone / Fax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel6, gridBagConstraints);

        phoneTextField.setColumns(12);
        phoneTextField.setEditable(false);
        phoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneTextFieldActionPerformed(evt);
            }
        });
        phoneTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                phoneTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(phoneTextField, gridBagConstraints);

        faxTextField.setColumns(12);
        faxTextField.setEditable(false);
        faxTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                faxTextFieldActionPerformed(evt);
            }
        });
        faxTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                faxTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(faxTextField, gridBagConstraints);

        jLabel7.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        editorPanel.add(jLabel7, gridBagConstraints);

        jLabel8.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        editorPanel.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Price Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(jLabel9, gridBagConstraints);

        priceTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PUBLIC", "MEMBER", "AGENT" }));
        priceTypeComboBox.setEnabled(false);
        priceTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                priceTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        editorPanel.add(priceTypeComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(editorPanel, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        commandPanel.setBorder(new javax.swing.border.TitledBorder(""));
        checkButton.setText("Check  -  F1");
        checkButton.setEnabled(false);
        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkButtonActionPerformed(evt);
            }
        });
        checkButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        commandPanel.add(checkButton, gridBagConstraints);

        resetButton.setText("Reset  -  F5");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        resetButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resetButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        commandPanel.add(resetButton, gridBagConstraints);

        saveButton.setText("Save  -  F12");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        saveButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        commandPanel.add(saveButton, gridBagConstraints);

        closeButton.setMnemonic('X');
        closeButton.setText("Close  -  Alt+X");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        closeButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closeButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        commandPanel.add(closeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(commandPanel, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    private void priceTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            addressTextArea.setEnabled(true);
            addressTextArea.requestFocusInWindow();
        }
        else{
            this.getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_priceTypeComboBoxKeyPressed
    
    private void faxTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_faxTextFieldActionPerformed
        saveButton.requestFocusInWindow();
    }//GEN-LAST:event_faxTextFieldActionPerformed
    
    private void phoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneTextFieldActionPerformed
        faxTextField.setEditable(true);
        faxTextField.requestFocusInWindow();
    }//GEN-LAST:event_phoneTextFieldActionPerformed
    
    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        phoneTextField.setEditable(true);
        phoneTextField.requestFocusInWindow();
    }//GEN-LAST:event_emailTextFieldActionPerformed
    
    private void countryTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryTextFieldActionPerformed
        emailTextField.setEditable(true);
        emailTextField.requestFocusInWindow();
        saveButton.setEnabled(true);
    }//GEN-LAST:event_countryTextFieldActionPerformed
    
    private void cityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityTextFieldActionPerformed
        countryTextField.setEditable(true);
        countryTextField.requestFocusInWindow();
    }//GEN-LAST:event_cityTextFieldActionPerformed
    
    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        if(nameTextField.getText().length()>0){
            if(CashierMainApp.isEnablePriceMapSelect()){
                priceTypeComboBox.setEnabled(true);
                priceTypeComboBox.requestFocusInWindow();
            }
            else{
                addressTextArea.setEnabled(true);
                addressTextArea.requestFocusInWindow();
            }
            
           // saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_nameTextFieldActionPerformed
    
    private void codeTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codeTextFieldKeyReleased
        checkButton.setEnabled(this.isAvailToCheck());
    }//GEN-LAST:event_codeTextFieldKeyReleased
    
    private void codeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeTextFieldActionPerformed
        if(codeTextField.getText().length()>0){
            nameTextField.setEditable(true);
            nameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_codeTextFieldActionPerformed
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed
    
    private void closeButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_closeButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_closeButtonKeyPressed
    
    private void saveButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_saveButtonKeyPressed
    
    private void resetButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resetButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_resetButtonKeyPressed
    
    private void checkButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkButtonKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_checkButtonKeyPressed
    
    private void faxTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_faxTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_faxTextFieldKeyPressed
    
    private void phoneTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_phoneTextFieldKeyPressed
    
    private void emailTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_emailTextFieldKeyPressed
    
    private void countryTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_countryTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_countryTextFieldKeyPressed
    
    private void cityTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_cityTextFieldKeyPressed
    
    private void addressTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addressTextAreaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            cityTextField.setEditable(true);
            cityTextField.requestFocusInWindow();
        }
        else{
            this.getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_addressTextAreaKeyPressed
    
    private void nameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyPressed
        this.getGlobalKeyListener(evt);
    }//GEN-LAST:event_nameTextFieldKeyPressed
    
    private void codeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codeTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            if(codeTextField.getText().length()>0){
                this.cmdClose();
            }
        }
        else{
            this.getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_codeTextFieldKeyPressed
    
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        cmdClose();
    }//GEN-LAST:event_closeButtonActionPerformed
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        cmdSave();
    }//GEN-LAST:event_saveButtonActionPerformed
    
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        cmdReset();
    }//GEN-LAST:event_resetButtonActionPerformed
    
    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkButtonActionPerformed
        cmdCheck();
    }//GEN-LAST:event_checkButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ContactInputDialog(new JFrame(), true).show();
    }
    
    /**
     * Getter for property candidateMemberReg.
     * @return Value of property candidateMemberReg.
     */
    public MemberReg getCandidateMemberReg() {
        return candidateMemberReg;
    }
    
    /**
     * Setter for property candidateMemberReg.
     * @param candidateMemberReg New value of property candidateMemberReg.
     */
    public void setCandidateMemberReg(MemberReg candidateMemberReg) {
        this.candidateMemberReg = candidateMemberReg;
    }
    
    /**
     * Getter for property hashMemberTypeByName.
     * @return Value of property hashMemberTypeByName.
     */
    public Hashtable getHashMemberTypeByName() {
        return hashMemberTypeByName;
    }
    
    /**
     * Setter for property hashMemberTypeByName.
     * @param hashMemberTypeByName New value of property hashMemberTypeByName.
     */
    public void setHashMemberTypeByName(Hashtable hashMemberTypeByName) {
        this.hashMemberTypeByName = hashMemberTypeByName;
    }
    
    /**
     * Getter for property hashMemberTypeById.
     * @return Value of property hashMemberTypeById.
     */
    public Hashtable getHashMemberTypeById() {
        return hashMemberTypeById;
    }
    
    /**
     * Setter for property hashMemberTypeById.
     * @param hashMemberTypeById New value of property hashMemberTypeById.
     */
    public void setHashMemberTypeById(Hashtable hashMemberTypeById) {
        this.hashMemberTypeById = hashMemberTypeById;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JButton checkButton;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JButton closeButton;
    private javax.swing.JTextField codeTextField;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JTextField countryTextField;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField faxTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JComboBox priceTypeComboBox;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
    
}
