/*
 * OpenBillSearchDialog.java
 *
 * Created on December 17, 2004, 9:44 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.PstSales;
import com.dimata.posbo.entity.masterdata.Sales; 
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.util.Formater;
import com.dimata.util.LogicParser;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
/**
 *
 * @author  wpradnyana
 * @editor yogi
 * @recreate wpulantara:p
 */

import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OpenBillSearchDialog extends JDialog {
    
    /** Creates new form OpenBillSearchDialog */
    public OpenBillSearchDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initAllFields();
    }
    
    public void initAllFields(){
        initKeyword();
        initButton();
        this.setDataVector(null);
        this.setMemberTableModel(null);
        this.openBillListTable.setModel(this.getMemberTableModel());
        cmdSetColumnTableSize();
    }
    
    private void initButton(){
        nextRecordButton.setEnabled(false);
        prevRecordButton.setEnabled(false);
        firstRecordButton.setEnabled(false);
        lastRecordButton.setEnabled(false);
        selectRecordButton.setEnabled(false);
    }
    
    private void initKeyword(){
        openBillCodeTextField.setText("");
        openBillInfoTextField.setText("");
        guestNameTextField.setText("");
    }
    
    public void cmdSetColumnTableSize(){
        int totalWidth = openBillListTable.getWidth();
        openBillListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        openBillListTable.getColumnModel().getColumn(COL_CVR_NO).setPreferredWidth((int)(totalWidth*0.2));
        openBillListTable.getColumnModel().getColumn(COL_CUST).setPreferredWidth((int)(totalWidth*0.45));
        openBillListTable.getColumnModel().getColumn(COL_DATE).setPreferredWidth((int)(totalWidth*0.15));
        openBillListTable.getColumnModel().getColumn(COL_INVOICE_NO).setPreferredWidth((int)(totalWidth*0.15));
        
        openBillListTable.repaint();
        openBillListTable.revalidate();
        
    }
    private DefaultTableModel memberTableModel= null;
    
    private static int COL_NO=0;
    private static int COL_INVOICE_NO=1;
    private static int COL_CVR_NO=2;
    private static int COL_DATE=3;
    private static int COL_CUST=4;
    
    private static String [] fieldNames={
        "No", "Invoice Number", "Cover Number", "Date", "Cust Name"
    };
    
    private Vector dataVector = null;
    private Vector columnIdentifier = null;
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
     * Getter for property memberTableModel.
     * @return Value of property memberTableModel.
     */
    public DefaultTableModel getMemberTableModel() {
        if(memberTableModel==null){
            memberTableModel = new DefaultTableModel();
            memberTableModel.setDataVector(this.getDataVector(),this.getColumnIdentifier());
        }
        return memberTableModel;
    }
    
    /**
     * Setter for property memberTableModel.
     * @param memberTableModel New value of property memberTableModel.
     */
    public void setMemberTableModel(DefaultTableModel memberTableModel) {
        this.memberTableModel = memberTableModel;
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
                BillMain  obj = (BillMain)raw.get(i);
                Vector temp = new Vector();
                hashRowIndexOID.put(new Integer(i),obj);
                
                temp.add(String.valueOf(getStartRecord()+i+1));
                temp.add(obj.getInvoiceNumber());
                temp.add(obj.getCoverNumber());
                temp.add(Formater.formatDate(obj.getBillDate(),"dd-MM-yyyy"));
                temp.add(obj.getGuestName());
                
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
        openBillListTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        openBillCodeTextField = new javax.swing.JTextField();
        openBillInfoTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        guestNameTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        firstRecordButton = new javax.swing.JButton();
        prevRecordButton = new javax.swing.JButton();
        nextRecordButton = new javax.swing.JButton();
        lastRecordButton = new javax.swing.JButton();
        selectRecordButton = new javax.swing.JButton();
        closeFrameButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Open Bill Search");
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 150));
        openBillListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Open Bill Code", "Open Bill Info", "Date", "Cust Name", "Sales Name"
            }
        ));
        openBillListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                openBillListTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(openBillListTable);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(new javax.swing.border.TitledBorder("Keyword"));
        jLabel1.setText("Invoice Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(jLabel2, gridBagConstraints);

        openBillCodeTextField.setColumns(10);
        openBillCodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBillCodeTextFieldActionPerformed(evt);
            }
        });
        openBillCodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                openBillCodeTextFieldFocusGained(evt);
            }
        });
        openBillCodeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                openBillCodeTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(openBillCodeTextField, gridBagConstraints);

        openBillInfoTextField.setColumns(15);
        openBillInfoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBillInfoTextFieldActionPerformed(evt);
            }
        });
        openBillInfoTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                openBillInfoTextFieldFocusGained(evt);
            }
        });
        openBillInfoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                openBillInfoTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(openBillInfoTextField, gridBagConstraints);

        searchButton.setMnemonic('F');
        searchButton.setText("Search (Alt F)");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(searchButton, gridBagConstraints);

        jLabel3.setText("Cover Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(jLabel3, gridBagConstraints);

        guestNameTextField.setColumns(10);
        guestNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestNameTextFieldActionPerformed(evt);
            }
        });
        guestNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                guestNameTextFieldFocusGained(evt);
            }
        });
        guestNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                guestNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(guestNameTextField, gridBagConstraints);

        jPanel2.add(jPanel4, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

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

        jPanel3.add(jPanel6);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents
            
    private void guestNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_guestNameTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdClose();
        }
    }//GEN-LAST:event_guestNameTextFieldKeyPressed
    
    private void openBillInfoTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_openBillInfoTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdClose();
        }
    }//GEN-LAST:event_openBillInfoTextFieldKeyPressed
    
    private void openBillCodeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_openBillCodeTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdClose();
        }
    }//GEN-LAST:event_openBillCodeTextFieldKeyPressed
            
    private void guestNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guestNameTextFieldFocusGained
        guestNameTextField.selectAll();
    }//GEN-LAST:event_guestNameTextFieldFocusGained
    
    private void openBillInfoTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_openBillInfoTextFieldFocusGained
        openBillInfoTextField.selectAll();
    }//GEN-LAST:event_openBillInfoTextFieldFocusGained
    
    private void openBillCodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_openBillCodeTextFieldFocusGained
        openBillCodeTextField.selectAll();
    }//GEN-LAST:event_openBillCodeTextFieldFocusGained
            
    private void guestNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guestNameTextFieldActionPerformed
            cmdSearch();
        
    }//GEN-LAST:event_guestNameTextFieldActionPerformed
    
    private void openBillInfoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBillInfoTextFieldActionPerformed
        if(openBillInfoTextField.getText().length()>0){
            cmdSearch();
        }
        else {
            guestNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_openBillInfoTextFieldActionPerformed
    
    private void openBillListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_openBillListTableKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ENTER:
                cmdChoosen();
                break;
            case KeyEvent.VK_ESCAPE:
                openBillCodeTextField.requestFocusInWindow();
                break;
            case KeyEvent.VK_HOME:
                if(firstRecordButton.isEnabled()){
                    cmdFirst();
                }
                break;
            case KeyEvent.VK_END:
                if(lastRecordButton.isEnabled()){
                    cmdLast();
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if(prevRecordButton.isEnabled()){
                    cmdPrev();
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if(nextRecordButton.isEnabled()){
                    cmdNext();
                }
                break;
            case KeyEvent.VK_UP:
                if(prevRecordButton.isEnabled() && openBillListTable.getSelectedRow()==0){
                    cmdPrev();
                }
                break;
            case KeyEvent.VK_DOWN:
                if(nextRecordButton.isEnabled() && openBillListTable.getSelectedRow()==recordToGet-1){
                    cmdNext();
                }
                break;
        }
    }//GEN-LAST:event_openBillListTableKeyPressed
    
    private void selectRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRecordButtonActionPerformed
        cmdChoosen();
    }//GEN-LAST:event_selectRecordButtonActionPerformed
    
    private void lastRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastRecordButtonActionPerformed
        cmdLast();
    }//GEN-LAST:event_lastRecordButtonActionPerformed
    
    private void nextRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextRecordButtonActionPerformed
        cmdNext();
    }//GEN-LAST:event_nextRecordButtonActionPerformed
    
    private void prevRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevRecordButtonActionPerformed
        cmdPrev();
    }//GEN-LAST:event_prevRecordButtonActionPerformed
    
    private void firstRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstRecordButtonActionPerformed
        cmdFirst();
    }//GEN-LAST:event_firstRecordButtonActionPerformed
    
    private void closeFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeFrameButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeFrameButtonActionPerformed
    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        cmdSearch();
    }//GEN-LAST:event_searchButtonActionPerformed
    
    private void openBillCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBillCodeTextFieldActionPerformed
        if(openBillCodeTextField.getText().length()>0){
            cmdSearch();
        }
        else {
            openBillInfoTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_openBillCodeTextFieldActionPerformed
    
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
        
        result = getOpenBill(this.startRecord,this.recordToGet, openBillCodeTextField.getText(),openBillInfoTextField.getText(),guestNameTextField.getText(),"");
        setSizeRecord(getOpenBillCount(openBillCodeTextField.getText(),openBillInfoTextField.getText(),guestNameTextField.getText(),""));
        
        // added by wpulantara for navigation button state controll =p
        refreshButton(startRecord,sizeRecord,recordToGet);
        
        dataVector = this.transformResult(result);
        memberTableModel.setDataVector(dataVector,this.getColumnIdentifier());
        openBillListTable.requestFocusInWindow();
        if(result.size()>0){
            cmdSetColumnTableSize();
            this.openBillListTable.setRowSelectionInterval(memberTableModel.getRowCount()-1,memberTableModel.getRowCount()-1);
        }else if(result.size()==0){
            JOptionPane.showMessageDialog(this,"Bill Not found","Search result",JOptionPane.OK_OPTION);
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
    public void cmdClose(){
        this.startRecord = 0;
        this.dispose();
    }
    private void cmdChoosen(){
        Vector result = new Vector(1,1);
        BillMain billmain = new BillMain();
        int indexColumn = openBillListTable.getSelectedRow();
        billmain = (BillMain)getHashRowIndexOID().get(new Integer(indexColumn));
        result.add(billmain);
        CashSaleController.setOpenBillChoosen(result);
        
        this.startRecord = 0;
        this.dispose();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        OpenBillSearchDialog frame = new OpenBillSearchDialog(new JFrame(), true);
        frame.setSize(480,360);
        frame.show();
    }
    
    public static Vector getOpenBill(int start,int recordToGet, String billCodeText, String billInfoText, String guestName, String salesCode){
        Vector result = new Vector(1,1);
        String where = ""+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
        " = "+PstBillMain.TRANS_STATUS_OPEN +
        " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
        " = "+PstBillMain.TRANS_TYPE_CASH;
        
        if(billCodeText!=null&&billCodeText.length()>0){
            Vector vect = parseTxt(billCodeText);
            String strCode = "";
            for(int i=0;i<vect.size();i++){
                if(strCode.length()==0){
                    strCode = strCode + " ("+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '%"+vect.get(i)+"%') ";
                }else{
                    strCode = strCode + " OR ("+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '%"+vect.get(i)+"%') ";
                }
                
            }
            if(where!=null&&where.length()>0){
                where = where + " AND ("+strCode+")";
            }else{
                where = where + "("+strCode+")";
            }
        }
        
        if(billInfoText!=null&&billInfoText.length()>0){
            if(where!=null&&where.length()>0){
                where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER]+
                " LIKE '%"+billInfoText+"%' ";
            }else{
                where = where + " "+PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER]+
                " LIKE '%"+billInfoText+"%' ";
            }
        }
        
        if(guestName!=null&&guestName.length()>0){
            if(where!=null&&where.length()>0){
                where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_GUEST_NAME]+
                " LIKE '%"+guestName+"%' ";
            }
            else{
                where = where + " "+PstBillMain.fieldNames[PstBillMain.FLD_GUEST_NAME]+
                " LIKE '%"+guestName+"%' ";
            }
        }
        
        if(salesCode!=null&&salesCode.length()>0){
            if(where!=null&&where.length()>0){
                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]+
                " LIKE '%"+salesCode+"%' ";
            }
            else {
                where = where + " " + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]+
                " LIKE '%"+salesCode+"%' ";
            }
        }
        
        System.out.println("sql search:: "+where);
        String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        result = PstBillMain.list(start,recordToGet, where,order);
        return result;
    }
    
    public static int getOpenBillCount(String billCodeText, String billInfoText, String guestName, String salesCode){
        int result = 0;
        String where = ""+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
        " = "+PstBillMain.TRANS_STATUS_OPEN +
        " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
        " = "+PstBillMain.TRANS_TYPE_CASH;
        
        if(billCodeText!=null&&billCodeText.length()>0){
            Vector vect = parseTxt(billCodeText);
            String strCode = "";
            for(int i=0;i<vect.size();i++){
                if(strCode.length()==0){
                    strCode = strCode + " ("+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '%"+vect.get(i)+"%') ";
                }else{
                    strCode = strCode + " OR ("+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
                    " LIKE '%"+vect.get(i)+"%') ";
                }
                
            }
            if(where!=null&&where.length()>0){
                where = where + " AND ("+strCode+")";
            }else{
                where = where + "("+strCode+")";
            }
        }
        
        if(billInfoText!=null&&billInfoText.length()>0){
            if(where!=null&&where.length()>0){
                where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER]+
                " LIKE '%"+billInfoText+"%' ";
            }else{
                where = where + " "+PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER]+
                " LIKE '%"+billInfoText+"%' ";
            }
        }
        
        if(guestName!=null&&guestName.length()>0){
            if(where!=null&&where.length()>0){
                where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_GUEST_NAME]+
                " LIKE '%"+guestName+"%' ";
            }
            else{
                where = where + " "+PstBillMain.fieldNames[PstBillMain.FLD_GUEST_NAME]+
                " LIKE '%"+guestName+"%' ";
            }
        }
        
        if(salesCode!=null&&salesCode.length()>0){
            if(where!=null&&where.length()>0){
                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]+
                " LIKE '%"+salesCode+"%' ";
            }
            else {
                where = where + " " + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]+
                " LIKE '%"+salesCode+"%' ";
            }
        }
        
        System.out.println("sql search:: "+where);
        result = PstBillMain.getCount(where);
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
    
    /**
     * Getter for property recordToGet.
     * @return Value of property recordToGet.
     */
    public int getRecordToGet() {
        return recordToGet;
    }
    
    /**
     * Setter for property recordToGet.
     * @param recordToGet New value of property recordToGet.
     */
    public void setRecordToGet(int recordToGet) {
        this.recordToGet = recordToGet;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeFrameButton;
    private javax.swing.JButton firstRecordButton;
    private javax.swing.JTextField guestNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lastRecordButton;
    private javax.swing.JButton nextRecordButton;
    private javax.swing.JTextField openBillCodeTextField;
    private javax.swing.JTextField openBillInfoTextField;
    private javax.swing.JTable openBillListTable;
    private javax.swing.JButton prevRecordButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton selectRecordButton;
    // End of variables declaration//GEN-END:variables
    
    /** Holds value of property startRecord. */
    private int startRecord = 0;
    
    /** Holds value of property sizeRecord. */
    private int sizeRecord = 0;
    
    /** Holds value of property recordToGet. */
    private int recordToGet = 8;
    
}
