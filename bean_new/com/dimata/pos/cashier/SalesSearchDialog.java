/*
 * SalesSearchDialog.java
 *
 * Created on January 24, 2005, 1:04 PM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.Sales;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  pulantara
 */
public class SalesSearchDialog extends JDialog {
    
    private Vector dataVector = null;
    private Vector columnIdentifier = null;
    private Hashtable hashRowIndexOID = new Hashtable();
    private DefaultTableModel salesTableModel= null;
    private static final int COL_NO=0;
    private static final int COL_NAME=1;
    private static final int COL_CODE=2;
    private static final String[] fieldNames = {
        "No",
        "Name",
        "Code"
    };
    
    private static final int MAX_ROW = 8;
    
    private int totalRows;
    
    private int currentStartRow = 0;
    
    private Vector transformResult(Vector raw, int start){
        int rawSize = raw.size();
        Vector fix = new Vector();
        
        for(int i=0;i<rawSize;i++){
            Sales obj = (Sales)raw.get(i);
            
            Vector temp = new Vector();
            hashRowIndexOID.put(new Integer(i),obj);
            temp.add(String.valueOf(i+start+1));
            temp.add(obj.getName());
            temp.add(obj.getCode());
            fix.add(temp);
        }
        return fix;
    }
    
    public void setKeyword(String code, String name){
        salesCodeTextField.setText(code);
        salesNameTextField.setText(name);
    }
    
    private void cmdFirst(){
        currentStartRow = 0;
        cmdSearch(currentStartRow);
    }
    private void cmdPrev(){
        currentStartRow = currentStartRow - MAX_ROW;
        cmdSearch(currentStartRow);
    }
    private void cmdNext(){
        currentStartRow = currentStartRow + MAX_ROW;
        cmdSearch(currentStartRow);
    }
    private void cmdLast(){
        currentStartRow = totalRows - ((totalRows % MAX_ROW)==0?MAX_ROW:(totalRows % MAX_ROW));
        cmdSearch(currentStartRow);
    }
    public void cmdSetTableColumnSize(){
        int totalWidth = salesListTable.getWidth();
        salesListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.1));
        salesListTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth((int)(totalWidth*0.4));
        salesListTable.getColumnModel().getColumn(COL_CODE).setPreferredWidth((int)(totalWidth*0.5));
        salesListTable.repaint();
        salesListTable.revalidate();
    }
    
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
    
    public void cmdSearch(int start){
        
        Vector result = new Vector();
        // count total rows
        
        totalRows = CashSaleController.getSalesCount( salesNameTextField.getText(),salesCodeTextField.getText());
        if(start<0 || start>=totalRows)
            start = 0;
        currentStartRow=start;
        
        refreshButton(start,totalRows,MAX_ROW);
        
        
        result = CashSaleController.getSales(start,MAX_ROW,  salesNameTextField.getText(),salesCodeTextField.getText());
        
        dataVector = this.transformResult(result,start);
        
        salesTableModel.setDataVector(dataVector,this.getColumnIdentifier());
        
        if(result.size()>0){
            cmdSetTableColumnSize();
            this.salesListTable.setRowSelectionInterval(salesTableModel.getRowCount()-1,salesTableModel.getRowCount()-1);
            this.salesListTable.requestFocusInWindow();
        }else if(result.size()==0){
            JOptionPane.showMessageDialog(this,"Sales Not found","Search result",JOptionPane.OK_OPTION);
            salesNameTextField.requestFocusInWindow();
            
        }
    }
    private void cmdDoChoose(){
        Sales sales = new Sales();
        int a = salesListTable.getSelectedRow();
        sales = (Sales) this.getHashRowIndexOID().get(new Integer(a));
        CashSaleController.setSalesChoosen(sales);
        currentStartRow = 0;
        this.dispose();
    }
    
    
    /** Creates new form SalesSearchDialog */
    public SalesSearchDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initAllFields();
    }
    
    public void initAllFields(){
        initButton();
        this.setDataVector(null);
        this.setSalesTableModel(null);
        this.salesListTable.setModel(this.getSalesTableModel());
        cmdSetTableColumnSize();
    }
    
    private void initButton(){
        nextRecordButton.setEnabled(false);
        prevRecordButton.setEnabled(false);
        firstRecordButton.setEnabled(false);
        lastRecordButton.setEnabled(false);
        selectRecordButton.setEnabled(false);
    }
    
    /** Creates new form SalesSearchDialog with param*/
    public SalesSearchDialog(Frame parent, boolean modal, String name, String code) {
        super(parent, modal);
        initComponents();
        initAllFields();
        salesNameTextField.setText(name);
        salesCodeTextField.setText(code);
        this.salesListTable.setModel(this.getSalesTableModel());
        currentStartRow = 0;
        cmdSearch(currentStartRow);
    }
    
    public void refreshSearch(String name, String code){
        if((name.length()+code.length())>0){
            initAllFields();
            salesNameTextField.setText(name);
            salesCodeTextField.setText(code);
            currentStartRow = 0;
            cmdSearch(currentStartRow);
        }
        else{
            salesNameTextField.setText("");
            salesCodeTextField.setText("");
            currentStartRow = 0;
            initAllFields();
        }
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
        salesListTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        salesCodeTextField = new javax.swing.JTextField();
        salesNameTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        firstRecordButton = new javax.swing.JButton();
        prevRecordButton = new javax.swing.JButton();
        nextRecordButton = new javax.swing.JButton();
        lastRecordButton = new javax.swing.JButton();
        selectRecordButton = new javax.swing.JButton();
        closeFrameButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sales Search Dialog");
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(new javax.swing.border.TitledBorder(""));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 150));
        salesListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Name", "Code"
            }
        ));
        salesListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salesListTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(salesListTable);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(new javax.swing.border.TitledBorder("Keyword"));
        jLabel1.setText("Sales Name");
        jPanel4.add(jLabel1, new java.awt.GridBagConstraints());

        jLabel2.setText("Sales Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel4.add(jLabel2, gridBagConstraints);

        salesCodeTextField.setColumns(10);
        salesCodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesCodeTextFieldActionPerformed(evt);
            }
        });
        salesCodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salesCodeTextFieldFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel4.add(salesCodeTextField, gridBagConstraints);

        salesNameTextField.setColumns(10);
        salesNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesNameTextFieldActionPerformed(evt);
            }
        });
        salesNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salesNameTextFieldFocusGained(evt);
            }
        });
        salesNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salesNameTextFieldKeyPressed(evt);
            }
        });

        jPanel4.add(salesNameTextField, new java.awt.GridBagConstraints());

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jPanel4.add(searchButton, new java.awt.GridBagConstraints());

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

    private void selectRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRecordButtonActionPerformed
        cmdDoChoose();
    }//GEN-LAST:event_selectRecordButtonActionPerformed
    
    private void salesNameTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_salesNameTextFieldKeyPressed
    {//GEN-HEADEREND:event_salesNameTextFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            cmdClose();
        }
    }//GEN-LAST:event_salesNameTextFieldKeyPressed
    
    private void salesCodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesCodeTextFieldFocusGained
        salesCodeTextField.selectAll();
    }//GEN-LAST:event_salesCodeTextFieldFocusGained
    
    private void salesNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesNameTextFieldFocusGained
        salesNameTextField.selectAll();
    }//GEN-LAST:event_salesNameTextFieldFocusGained
    
    public void cmdClose(){
        CashSaleController.setSalesChoosen(new Sales());
        currentStartRow = 0;
        this.dispose();
    }
    private void closeFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeFrameButtonActionPerformed
        cmdClose();
    }//GEN-LAST:event_closeFrameButtonActionPerformed
    
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
    
    private void salesCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesCodeTextFieldActionPerformed
        currentStartRow = 0;
        cmdSearch(currentStartRow);
    }//GEN-LAST:event_salesCodeTextFieldActionPerformed
    
    private void salesNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesNameTextFieldActionPerformed
        String name = salesNameTextField.getText();
        currentStartRow = 0;
        if(name.length()>0){
            cmdSearch(currentStartRow);
        }
        else{
            salesCodeTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_salesNameTextFieldActionPerformed
    
    private void salesListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salesListTableKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ENTER:
                cmdDoChoose();
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
            case KeyEvent.VK_ESCAPE:
                salesNameTextField.requestFocusInWindow();
                break;
            case KeyEvent.VK_UP:
                if(salesListTable.getSelectedRow()==0)
                    cmdPrev();
                break;
            case KeyEvent.VK_DOWN:
                if(salesListTable.getSelectedRow()==MAX_ROW-1)
                    cmdNext();
                break;
        }
    }//GEN-LAST:event_salesListTableKeyPressed
    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        currentStartRow = 0;
        cmdSearch(currentStartRow);
    }//GEN-LAST:event_searchButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new SalesSearchDialog(new JFrame(), true,"i","").show();
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
    
    /**
     * Getter for property salesTableModel.
     * @return Value of property salesTableModel.
     */
    public DefaultTableModel getSalesTableModel() {
        if(salesTableModel==null){
            salesTableModel = new DefaultTableModel();
            salesTableModel.setDataVector(this.getDataVector(),this.getColumnIdentifier());
        }
        return salesTableModel;
    }
    
    /**
     * Setter for property salesTableModel.
     * @param salesTableModel New value of property salesTableModel.
     */
    public void setSalesTableModel(DefaultTableModel salesTableModel) {
        this.salesTableModel = salesTableModel;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeFrameButton;
    private javax.swing.JButton firstRecordButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lastRecordButton;
    private javax.swing.JButton nextRecordButton;
    private javax.swing.JButton prevRecordButton;
    private javax.swing.JTextField salesCodeTextField;
    private javax.swing.JTable salesListTable;
    private javax.swing.JTextField salesNameTextField;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton selectRecordButton;
    // End of variables declaration//GEN-END:variables
    
}
