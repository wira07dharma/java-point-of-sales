/*
 * SerialCodeListDialog.java
 *
 * Created on January 24, 2005, 3:25 PM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  pulantara
 */
public class SerialCodeListDialog extends JDialog {
    
    // OWN MADE CONSTANTS GOES HERE
    private static final int COL_NO = 0;
    private static final int COL_NAME = 1;
    private static final int COL_CODE = 2;
    
    private static final String[] fieldNames = {
        "No",
        "Product Name",
        "Available Serial Code"
    };
    
    private static final int MAX_ROW = 8;
    // END OFF OWN MADE CONSTANTS
    
    // OWN MADE ATTRIBUTE GOES HERE
    private Vector dataVector = null;
    private Vector columnIdentifier = null;
    private Hashtable hashRowIndexOID = new Hashtable();
    private DefaultTableModel serialTableModel= null;
    private int totalRows;
    private int currentStartRow = 0;
    private long materialId = 0;
    private long locationId = 0;
    // END OFF OWN MADE ATTRIBUTE
    
    // OWN MADE METHODE GOES HERE
    private Vector transformResult(Vector raw, int start){
        int rawSize = raw.size();
        Vector fix = new Vector();
        
        for(int i=0;i<rawSize;i++){
            try{
                MaterialStockCode obj = (MaterialStockCode)raw.get(i);
                Material mat = PstMaterial.fetchExc(obj.getMaterialId());
                Vector temp = new Vector();
                hashRowIndexOID.put(new Integer(i),obj);
                temp.add(String.valueOf(i+start+1));
                //temp.add(mat.getFullName ());
                temp.add(mat.getName());
                temp.add(obj.getStockCode());
                fix.add(temp);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return fix;
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
        
        totalRows = CashSaleController.getSerialCodeCount(this.getMaterialId(),this.getLocationId());
        if(start<0 || start>=totalRows)
            start = 0;
        currentStartRow=start;
        refreshButton(start,totalRows,MAX_ROW);
        
        result = CashSaleController.getSerialCode(start,MAX_ROW,  this.getMaterialId(), this.getLocationId());
        
        dataVector = this.transformResult(result,start);
        
        serialTableModel.setDataVector(dataVector,this.getColumnIdentifier());
        
        if(result.size()>0){
            this.serialListTable.setRowSelectionInterval(serialTableModel.getRowCount()-1,serialTableModel.getRowCount()-1);
            this.serialListTable.requestFocusInWindow();
        }else if(result.size()==0){
            JOptionPane.showMessageDialog(this,"Serial Not found","Search result",JOptionPane.OK_OPTION);
        }
        this.setColumnTableSize();
    }
    private void cmdDoChoose(){
        MaterialStockCode code = new MaterialStockCode();
        int a = serialListTable.getSelectedRow();
        code = (MaterialStockCode) this.getHashRowIndexOID().get(new Integer(a));
        CashSaleController.setStockCodeChoosen(code);
        this.dispose();
    }
    
    private void setColumnTableSize(){
        
        int totalSize = this.serialListTable.getWidth();
        
        this.serialListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int) (0.1*totalSize));
        this.serialListTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth((int) (0.5*totalSize));
        this.serialListTable.getColumnModel().getColumn(COL_CODE).setPreferredWidth((int) (0.4*totalSize));
        
        this.serialListTable.validate();
        this.serialListTable.repaint();
    }
    
    // END OFF OWN MADE METHODE
    
    
    /** Creates new form SerialCodeListDialog */
    public SerialCodeListDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.serialListTable.setModel(this.getSerialTableModel());
    }
    
    /** Creates new form SerialCodeListDialog with param*/
    public SerialCodeListDialog(Frame parent, boolean modal, long matId, long locId) {
        super(parent, modal);
        initComponents();
        this.setMaterialId(matId);
        this.setLocationId(locId);
        this.serialListTable.setModel(this.getSerialTableModel());
        currentStartRow = 0;
        this.cmdSearch(currentStartRow);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        serialListTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        firstRecordButton = new javax.swing.JButton();
        prevRecordButton = new javax.swing.JButton();
        nextRecordButton = new javax.swing.JButton();
        lastRecordButton = new javax.swing.JButton();
        selectRecordButton = new javax.swing.JButton();
        closeFrameButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Serial Code List");
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(new javax.swing.border.TitledBorder(""));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 150));
        serialListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Product Name", "Available Serial Number"
            }
        ));
        serialListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                serialListTableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(serialListTable);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        firstRecordButton.setText("First");
        firstRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstRecordButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(firstRecordButton, gridBagConstraints);

        prevRecordButton.setText("Prev");
        prevRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevRecordButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(prevRecordButton, gridBagConstraints);

        nextRecordButton.setText("Next");
        nextRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextRecordButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(nextRecordButton, gridBagConstraints);

        lastRecordButton.setText("Last");
        lastRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastRecordButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(lastRecordButton, gridBagConstraints);

        selectRecordButton.setMnemonic('C');
        selectRecordButton.setText("Choose (Alt C)");
        selectRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRecordButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(selectRecordButton, gridBagConstraints);

        closeFrameButton.setMnemonic('X');
        closeFrameButton.setText("Close (Alt X)");
        closeFrameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFrameButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(closeFrameButton, gridBagConstraints);

        jPanel3.add(jPanel6);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents
    
    private void serialListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_serialListTableKeyPressed
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
        }
    }//GEN-LAST:event_serialListTableKeyPressed
    
    private void closeFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeFrameButtonActionPerformed
        CashSaleController.setStockCodeChoosen(new MaterialStockCode());
        this.dispose();
    }//GEN-LAST:event_closeFrameButtonActionPerformed
    
    private void selectRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRecordButtonActionPerformed
        cmdDoChoose();
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //new SerialCodeListDialog(new javax.swing.JFrame(), true,Long.parseLong("504404263433707910"),0).show();
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
     * Getter for property totalRows.
     * @return Value of property totalRows.
     */
    public int getTotalRows() {
        return totalRows;
    }
    
    /**
     * Setter for property totalRows.
     * @param totalRows New value of property totalRows.
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }
    
    /**
     * Getter for property currentStartRow.
     * @return Value of property currentStartRow.
     */
    public int getCurrentStartRow() {
        return currentStartRow;
    }
    
    /**
     * Setter for property currentStartRow.
     * @param currentStartRow New value of property currentStartRow.
     */
    public void setCurrentStartRow(int currentStartRow) {
        this.currentStartRow = currentStartRow;
    }
    
    /**
     * Getter for property materialId.
     * @return Value of property materialId.
     */
    public long getMaterialId() {
        return materialId;
    }
    
    /**
     * Setter for property materialId.
     * @param materialId New value of property materialId.
     */
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }
    
    /**
     * Getter for property serialTableModel.
     * @return Value of property serialTableModel.
     */
    public DefaultTableModel getSerialTableModel() {
        if(serialTableModel==null){
            serialTableModel = new DefaultTableModel();
            serialTableModel.setDataVector(this.getDataVector(),this.getColumnIdentifier());
        }
        return serialTableModel;
    }
    
    /**
     * Setter for property serialTableModel.
     * @param serialTableModel New value of property serialTableModel.
     */
    public void setSerialTableModel(DefaultTableModel serialTableModel) {
        this.serialTableModel = serialTableModel;
    }
    
    /**
     * Getter for property locationId.
     * @return Value of property locationId.
     */
    public long getLocationId() {
        return locationId;
    }
    
    /**
     * Setter for property locationId.
     * @param locationId New value of property locationId.
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeFrameButton;
    private javax.swing.JButton firstRecordButton;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lastRecordButton;
    private javax.swing.JButton nextRecordButton;
    private javax.swing.JButton prevRecordButton;
    private javax.swing.JButton selectRecordButton;
    private javax.swing.JTable serialListTable;
    // End of variables declaration//GEN-END:variables
    
}
