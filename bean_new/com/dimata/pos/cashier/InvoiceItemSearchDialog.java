/*
 * InvoiceSearchDialog.java
 *
 * Created on December 17, 2004, 9:45 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.Material; 
import com.dimata.posbo.entity.masterdata.PstMaterial; 
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.pos.entity.billing.*;
import com.dimata.util.LogicParser;
import com.dimata.util.Validator;
import com.dimata.util.YearMonth;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  wpradnyana
 * @editor yogi and me (wpulantara)
 */



public class InvoiceItemSearchDialog extends JDialog {
    
    private static final int PRE_AND_SUF = 0;
    private static final int PRE_ONLY = 1;
    private static final int SUF_ONLY = 2;
    private static int searchMethode = CashierMainApp.getDSJ_CashierXML().getConfig(0).searchMethode;
    private static String prefix = (searchMethode==SUF_ONLY?"":"%");
    private static String sufix = (searchMethode==PRE_ONLY?"":"%");
    
    /** Creates new form InvoiceSearchDialog */
    public InvoiceItemSearchDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initAll();
        
    }
    private void cmdSetColumnTableSize(){
        int totalWidth = invoiceListTable.getWidth();
        invoiceListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        invoiceListTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth((int)(totalWidth*0.5));
        invoiceListTable.getColumnModel().getColumn(COL_CODE).setPreferredWidth((int)(totalWidth*0.2));
        invoiceListTable.getColumnModel().getColumn(COL_SERIAL).setPreferredWidth((int)(totalWidth*0.15));
        invoiceListTable.getColumnModel().getColumn(COL_QTY).setPreferredWidth((int)(totalWidth*0.1));
        
        invoiceListTable.repaint();
        invoiceListTable.revalidate();
    }
    
    public void initAll(){
        initButton();
        this.setDataVector(null);
        this.setInvoiceTableModel(null);
        this.invoiceListTable.setModel(this.getInvoiceTableModel());
        this.setSize(this.getPreferredSize());
        cmdSetColumnTableSize();
    }
    
    private void initButton(){
        nextRecordButton.setEnabled(false);
        prevRecordButton.setEnabled(false);
        firstRecordButton.setEnabled(false);
        lastRecordButton.setEnabled(false);
        selectRecordButton.setEnabled(false);
    }
    
    private DefaultTableModel invoiceTableModel= null;
    
    private static final int COL_NO = 0;
    private static final int COL_NAME = 1;
    private static final int COL_CODE = 2;
    private static final int COL_SERIAL = 3;
    private static final int COL_QTY = 4;
    
    private static String [] fieldNames={
        "No", "Item Name","Item Code","Serial Code","Qty"
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
    
    private Hashtable hashRowCodeOID = new Hashtable();
    private Vector transformResult(Vector raw){
        int rawSize = raw.size();
        Vector fix = new Vector();
        try{
            for(int i=0;i<rawSize;i++){
                //BillMain  obj = (BillMain)raw.get(i);
                Vector record = new Vector();
                Vector temp = new Vector();
                record = (Vector)raw.get(i);
                BillMain billMain = (BillMain)record.get(0);
                Billdetail billDetail = (Billdetail)record.get(1);
                BillDetailCode billDetailCode = null;
                try{
                    billDetailCode = (BillDetailCode)record.get(2);
                }catch(Exception e){
                    billDetailCode = new BillDetailCode();
                }
                
                temp.add(String.valueOf(this.startRecord+(i+1)));
                Material mat = PstMaterial.fetchExc(billDetail.getMaterialId());
                
                
                //temp.add(mat.getFullName ());
                temp.add(mat.getName());
                temp.add(mat.getSku());
                double  qtyReturned;
                if(billDetailCode!=null){
                    temp.add(billDetailCode.getStockCode());
                    qtyReturned = CashSaleController.getQtyAlreadyReturned(billMain.getOID(), billDetail.getMaterialId(), billDetailCode.getStockCode());
                }else{
                    temp.add("No Serial Code");
                    qtyReturned = CashSaleController.getQtyAlreadyReturned(billMain.getOID(), billDetail.getMaterialId(), "");
                }
                
                billDetail.setQty(billDetail.getQty()-qtyReturned);
                temp.add(String.valueOf(billDetail.getQty()));
                billDetail.setQty(billDetail.getQty()+qtyReturned);
                
                getHashRowIndexOID().put(new Integer(i),billDetail);
                getHashRowCodeOID().put(new Integer(i),billDetailCode);
                
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
        jLabel1 = new javax.swing.JLabel();
        invoiceNumberTextField = new javax.swing.JTextField();
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
        setTitle("Invoice Search");
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
                "No", "item Name", "Item Code", "Serial Code", "Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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
        jPanel2.setPreferredSize(new java.awt.Dimension(184, 100));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(new javax.swing.border.TitledBorder(null, "Parameter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 11)));
        jLabel1.setText("Invoice Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel1, gridBagConstraints);

        invoiceNumberTextField.setColumns(10);
        invoiceNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceNumberTextFieldActionPerformed(evt);
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
        jPanel7.add(invoiceNumberTextField, gridBagConstraints);

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(searchButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jPanel7, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setBorder(new javax.swing.border.TitledBorder(""));
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

    }//GEN-END:initComponents
    
    private void invoiceNumberTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_invoiceNumberTextFieldKeyPressed
    {//GEN-HEADEREND:event_invoiceNumberTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoiceNumberTextFieldKeyPressed
    
    private void closeFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeFrameButtonActionPerformed
        // Add your handling code here:
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
    
    public void cmdClose(){
        this.startRecord=0;
        this.dispose();
    }
    private void invoiceListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoiceListTableKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                cmdClose();
                break;
            case KeyEvent.VK_ENTER:
                cmdChoosen();
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
    }//GEN-LAST:event_invoiceListTableKeyPressed
    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        setStartRecord(0);
        cmdSearch();
    }//GEN-LAST:event_searchButtonActionPerformed
    
    private void invoiceNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceNumberTextFieldActionPerformed
        setStartRecord(0);
        cmdSearch();
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
        result = getInvoiceItem(this.startRecord,this.recordToGet, this.invoiceNumberTextField.getText());
        
        setSizeRecord(getInvoiceItem(0,0,this.invoiceNumberTextField.getText()).size());
        this.refreshButton(startRecord, sizeRecord, recordToGet);
        
        dataVector = this.transformResult(result);
        this.invoiceTableModel.setDataVector(dataVector,this.getColumnIdentifier());
        this.invoiceListTable.requestFocusInWindow();
        if(result.size()>0){
            this.invoiceListTable.setRowSelectionInterval(this.invoiceTableModel.getRowCount()-1,this.invoiceTableModel.getRowCount()-1);
            cmdSetColumnTableSize();
        }else if(result.size()==0){
            JOptionPane.showMessageDialog(this,"Invoice Not found","Search result",JOptionPane.OK_OPTION);
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
        Vector resultCode = new Vector(1,1);
        BillMain billmain = new BillMain();
        int indexColumn = this.invoiceListTable.getSelectedRow();
        Billdetail billDetail = new Billdetail();
        BillDetailCode billDetailCode = new BillDetailCode();
        billDetail = (Billdetail)getHashRowIndexOID().get(new Integer(indexColumn));
        billDetailCode = (BillDetailCode)getHashRowCodeOID().get(new Integer(indexColumn));
        result.add(billDetail);
        resultCode.add(billDetailCode);
        CashSaleController.setBillDetailChoosen(result);
        CashSaleController.setBillDetailCodeChoosen(resultCode);
        //CashSaleFrame cashFrame = new CashSaleFrame();
        //cashFrame.setSaleModel(SessTransactionData.getData(billmain));
        this.dispose();
    }
    
    public Vector getInvoiceItem(int start,int recordToGet, String invoiceCodeText){
        Vector result = new Vector(1,1);
        
        
        /** edited by wpulantara, adding condition */
        long locationId = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId);
        String sql = " SELECT MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
        " , DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+
        " , CODE."+PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_CASH_BILL_DETAIL_CODE_ID]+
        " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+" AS MAIN "+
        " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL+" AS DETAIL "+
        " ON MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
        " = DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
        " LEFT JOIN "+PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE+" AS CODE "+
        " ON DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+
        " = CODE."+PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+
        " WHERE MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
        " AND (MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = "+PstBillMain.TRANS_STATUS_CLOSE+
        " OR (MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = "+PstBillMain.TRANS_STATUS_OPEN+
        " AND MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = "+PstBillMain.TRANS_TYPE_CREDIT+")) "+
        " AND MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+locationId;
        
        if(invoiceCodeText.length()>0){
            sql = sql +" AND MAIN."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+
            " = "+invoiceCodeText;
        }
        
        DBResultSet dbrs = null;
        try{
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector temp = new Vector(1,1);
                long billMainId = rs.getLong(1);
                long billDetailId = rs.getLong(2);
                long billDetailCodeId = rs.getLong(3);
                BillMain billMain = PstBillMain.fetchExc(billMainId);
                Billdetail billDetail = PstBillDetail.fetchExc(billDetailId);
                BillDetailCode billDetailCode = new BillDetailCode();
                try{
                    billDetailCode = PstBillDetailCode.fetchExc(billDetailCodeId);
                }catch(Exception e){
                    
                }
                
                temp.add(billMain);
                temp.add(billDetail);
                temp.add(billDetailCode);
                result.add(temp);
            }
        }catch(Exception e){
            
            System.out.println("err di search member : "+e.toString());
            e.printStackTrace();
        }finally{
            DBResultSet.close(dbrs);
        }
        
        
        return result;
    }
    
    public Vector getInvoice(int start,int recordToGet, String invoiceCodeText){
        Vector result = new Vector(1,1);
        /*
         
         */
        
        String where = ""+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
        " = "+PstBillMain.TRANS_STATUS_CLOSE;
        
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
            if(where!=null&&where.length()>0){
                where = where + " AND ("+strCode+")";
            }else{
                where = where + "("+strCode+")";
            }
        }
        
        
        String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        result = PstBillMain.list(start,recordToGet, where,order);
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
        new InvoiceItemSearchDialog(new JFrame(), true).show();
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
     * Getter for property hashRowCodeOID.
     * @return Value of property hashRowCodeOID.
     */
    public Hashtable getHashRowCodeOID() {
        return hashRowCodeOID;
    }
    
    /**
     * Setter for property hashRowCodeOID.
     * @param hashRowCodeOID New value of property hashRowCodeOID.
     */
    public void setHashRowCodeOID(Hashtable hashRowCodeOID) {
        this.hashRowCodeOID = hashRowCodeOID;
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
        if(keyword.length()>0){
            this.invoiceNumberTextField.setText(keyword);
            this.invoiceNumberTextField.setEnabled(false);
        }
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
    private javax.swing.JTable invoiceListTable;
    private javax.swing.JTextField invoiceNumberTextField;
    private javax.swing.JLabel jLabel1;
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
    // End of variables declaration//GEN-END:variables
    
    private Vector resultCmbDate = new Vector();;
    private String keyword = "";
    
}
