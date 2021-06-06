/*
 * GiftFrame.java
 *
 * Created on January 10, 2005, 11:41 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.pos.entity.billing.BillDetailCode;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.printAPI.PrintInvoicePOS;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.util.Command;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.dimata.util.Validator;

/**
 *
 * @author  pulantara
 */
public class GiftFrame extends JDialog {
    
    /** Creates new form GiftFrame */
    public GiftFrame(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initField();
        initState();
        initSize();
        
        // sales
        salesLabel.setVisible(CashierMainApp.isEnableSaleEntry());
        salesTextField.setVisible(CashierMainApp.isEnableSaleEntry());
    }
    
    // OWN MADE CONSTANTS
    
    /** form name */
    public static final String FORM_GIFT = "Gift Form";
    
    /** columnIdentifier */
    public static final int COL_NO = 0;
    public static final int COL_ITEM_CODE = 1;
    public static final int COL_ITEM_NAME = 2;
    public static final int COL_SERIAL_CODE = 3;
    public static final int COL_ITEM_POINT = 4;
    public static final int COL_AMOUNT = 5;
    
    public static final String[] columnNames = {
        "No",
        "Item Code",
        "Item Name",
        "Serial Code",
        "Item Point",
        "Amount"
    };
    
    // END OF OWN MADE CONSTANTS
    
    // OWN MADE ATTRIBUTE GOES HERE
    
    /** error Message */
    private String errMsg = GiftController.errMsg[GiftController.NONE];
    
    /** gift model for this form */
    private GiftModel model;
    
    /** Hold OID of candidate gift item */
    private long materialId;
    
    // END OF OWN MADE ATTRIBUTE
    
    
    // OWN MADE METHODE GOES HERE
    
    /** init fields at construction */
    private void initField(){
        this.setModel(new GiftModel(Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).giftLocationId),CashierMainApp.getCashMaster().getCashierNumber()));
        // set init service type = GIFT_INFO
        GiftController.setCurrentServiceType(GiftController.GIFT_INFO);
        
        drawSalesInfo();
        drawCustomerInfo();
        drawDetailInfo();
        drawItemInfo();
        drawList();
        
    }
    
    /** init form size at construction */
    private void initSize(){
        Dimension dim = new Dimension();
        dim = this.getContentPane().getToolkit().getScreenSize();
        int w = dim.width;      // full screenSize
        int h = dim.height;     // full screenSize
        this.setBounds(0,0, w,h);
    }
    
    /** isError
     *  return true when error accured
     *  @return boolean
     */
    private boolean isError(){
        return this.getErrMsg().length()>0;
    }
    
    /** isNoError
     *  return true when no error accured
     *  @return boolean
     */
    private boolean isNoError(){
        return this.getErrMsg().length()==0;
    }
    
    /** cmdShowErrMsg
     *  show error message with massage dialog box
     *  and reset errMsg to no errror
     */
    private void cmdShowErrMsg(){
        JOptionPane.showMessageDialog(this,errMsg,FORM_GIFT,JOptionPane.OK_CANCEL_OPTION);
        this.setErrMsg(GiftController.errMsg[GiftController.NONE]);
    }
    
    /** change current service as requested
     * @return String errMsg
     */
    private String cmdChangeServiceType(){
        
        GiftController.setCurrentServiceType(servTypeComboBox.getSelectedIndex());
        this.setModel(new GiftModel(CashierMainApp.getCashMaster().getLocationId(),CashierMainApp.getCashMaster().getCashierNumber()));
        drawSalesInfo();
        drawCustomerInfo();
        drawDetailInfo();
        drawItemInfo();
        drawList();
        return GiftController.getErr();
    }
    
    /** search sales
     * @return String errMsg
     */
    private String cmdSearchSales(String name){
        String errMsg = "";
        Vector temp = CashSaleController.getSales(0,0, name, "");
        Sales sales;
        if(temp.size()==1){
            sales = (Sales) temp.get(0);
        }
        else{
            CashSaleController.showSalesSearch(null,name,"");
            sales = CashSaleController.getSalesChoosen();
        }
        
        if(sales!=null && sales.getName()!=null && sales.getName().length()>0){
            this.getModel().setSales(sales);
            this.getModel().getGiftTrans().setSalesCode(sales.getCode());
        }
        else{
            errMsg = GiftController.errMsg[GiftController.RECORD_NOT_FOUND];
        }
        
        return errMsg;
    }
    
    /** change current costumer type as requested
     * @return String errMsg
     */
    private String cmdChangeCostumerType(){
        String errMsg = "";
        //TODO add code here if a change of customer type folowed  by some actions
        return errMsg;
    }
    
    /** search member by name or code
     *  @return String errMsg
     */
    private String cmdSearchMember(String code, String name){
        String errMsg = "";
        MemberReg member;
        Vector vMember = new Vector();
        if(code.length()+name.length()>0) // optimezed search
            vMember = CashSaleController.getMember(0,0,code,name);
        if(vMember.size()==1){
            member = (MemberReg) vMember.get(0);
            this.getModel().setCustomerServed(member);
            this.getModel().getGiftTrans().setCustomerId(member.getOID());
        }
        else{
            CashSaleController.showMemberSearch(null,name,code);
            Vector vChoosen = CashSaleController.getMemberChoosen();
            if(vChoosen.size()==1){
                member = (MemberReg) vChoosen.get(0);
                vMember = CashSaleController.getMember(0,0,member.getMemberBarcode(),member.getPersonName());
                member = (MemberReg) vMember.get(0);
                this.getModel().setCustomerServed(member);
                this.getModel().getGiftTrans().setCustomerId(member.getOID());
            }
            else{
                errMsg = GiftController.errMsg[GiftController.RECORD_NOT_FOUND];
            }
        }
        
        return errMsg;
    }
    
    /** search gift item by name or code or point
     *  this methode will also set the current GiftItem, BillDetail,
     *  and BillDetail code (if any) in the model
     *  @return String errMsg
     */
    private String cmdSearchGift(String code, String name, int point){
        String errMsg = "";
        this.getModel().setCurrentGiftItem(null);
        this.getModel().setCurrentBillDetail(null);
        this.getModel().setCurrentBillDetailCode(null);
        Material material = null;
        if((code.length()+name.length()) > 0){
            Vector temp = GiftController.getGiftWith(0,0,code,name,0);
            if(temp.size()==1){
                material = (Material) temp.get(0);
            }
            else {
                GiftController.showGiftSearchDialog(this,code,name,point);
                material = GiftController.getGiftItemChoosen();
            }
        }
        else {
            GiftController.showGiftSearchDialog(this,"","",0);
            material = GiftController.getGiftItemChoosen();
        }
        
        if(material!=null){
            GiftItem gift = new GiftItem();
            gift.setCode(material.getSku());
            gift.setName(material.getName());
            gift.setPoint(material.getMinimumPoint());
            
            Billdetail detail = new Billdetail();
            detail.setMaterialId(material.getOID());
            detail.setUnitId(material.getDefaultStockUnitId());
            detail.setItemName(material.getName());
            detail.setCost(material.getAveragePrice());
            
            detail.setItemPrice(material.getAveragePrice());
            detail.setSku(material.getSku());
            
            
            if(material.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String stCode = "";
                BillDetailCode detailCode = new BillDetailCode();
                long giftLoc = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).giftLocationId);
                int codeSize = CashSaleController.getSerialCodeCount(material.getOID(),giftLoc);
                
                if(codeSize>0){
                    CashSaleController.showSerialCodeListDialog(this, material.getOID(), giftLoc);
                    MaterialStockCode objCode = CashSaleController.getStockCodeChoosen();
                    stCode = objCode.getStockCode();
                }
                else{
                    JOptionPane.showMessageDialog(this,"Serial code not found, manual entry",FORM_GIFT,JOptionPane.INFORMATION_MESSAGE);
                }
                while(stCode.length()==0){
                    stCode = JOptionPane.showInputDialog(this,"Input serial code ",FORM_GIFT, JOptionPane.WARNING_MESSAGE);
                    if(stCode==null){
                        stCode = "";
                    }
                }
                
                detailCode.setStockCode(stCode);
                gift.setSerialCode(stCode);
                
                this.getModel().setCurrentBillDetailCode(detailCode);
            }
            this.getModel().setCurrentGiftItem(gift);
            this.setMaterialId(material.getOID());
            this.getModel().setCurrentBillDetail(detail);
            
        }
        else{
            errMsg = GiftController.errMsg[GiftController.RECORD_NOT_FOUND];
        }
        
        return errMsg;
    }
    
    /** windows close methode */
    private String cmdCloseWindow(){
        String errMsg = "";
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure to close this gift transaction?",FORM_GIFT, JOptionPane.YES_NO_OPTION);
        if(answer==JOptionPane.YES_OPTION){
            this.getModel().setGiftTransDetail(new Vector()); // ignore confirm
            this.cmdNewGiftTrans();
            this.dispose();
        }
        return errMsg;
    }
    
    /** draw sales info panel */
    private void drawSalesInfo(){
        servTypeComboBox.setModel(new DefaultComboBoxModel(GiftController.serviceType));
        servTypeComboBox.setSelectedIndex(GiftController.getCurrentServiceType());
        if(this.getModel().getSales()!=null){
            salesTextField.setText(this.getModel().getSales().getName());
        }
        else{
            salesTextField.setText("");
        }
    }
    
    /** draw customer info panel */
    private void drawCustomerInfo(){
        if(this.getModel().getCustomerServed()!=null){
            memberCodeTextField.setText(this.getModel().getCustomerServed().getMemberBarcode());
            memberNameTextField.setText(this.getModel().getCustomerServed().getPersonName());
        }
        else{
            memberCodeTextField.setText("");
            memberNameTextField.setText("");
        }
    }
    
    /** draw candidate item info panel */
    private void drawItemInfo(){
        if(this.getModel().getCurrentGiftItem()!=null){
            itemCodeTextField.setText(this.getModel().getCurrentGiftItem().getCode());
            itemNameTextField.setText(this.getModel().getCurrentGiftItem().getName());
            itemPointTextField.setText(this.getModel().getCurrentGiftItem().getPoint()+"");
            amountTextField.setText(this.getModel().getCurrentGiftItem().getAmount()+"");
        }
        else{
            itemCodeTextField.setText("");
            itemNameTextField.setText("");
            itemPointTextField.setText("");
            amountTextField.setText("");
        }
    }
    
    /** draw detail (point) info */
    private void drawDetailInfo(){
        curPointTextField.setText(this.getModel().getAvailablePoint()+"");
        usedPointTextField.setText(this.getModel().getRequestedPoint()+"");
        freePointTextField.setText(this.getModel().getRestPoint()+"");
    }
    
    /** draw gift item list */
    private void drawList(){
        giftListTable.setModel(new DefaultTableModel(createDataModel(this.getModel().getListGift()),
        this.getColumnIdentifier()) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        }
        );
        if(this.getModel().getListGift().size()>0){
            editTableButton.setEnabled(true);
        }else{
            editTableButton.setEnabled(false);
        }
        cmdSetColumnTableSize();
        giftListTable.validate();
    }
    
    /** changeCommandPanelState
     *  @param int Command
     */
    private void changeCommandPanelState(int cmd){
        switch(cmd){
            case Command.NONE :
                itemSearchButton.setEnabled(false);
                itemUpdateButton.setEnabled(false);
                itemRemoveButton.setEnabled(false);
                
                break;
            case Command.ADD :
                itemSearchButton.setEnabled(true);
                itemUpdateButton.setEnabled(false);
                itemRemoveButton.setEnabled(false);
                break;
            case Command.EDIT:
                itemSearchButton.setEnabled(false);
                itemUpdateButton.setEnabled(true);
                itemRemoveButton.setEnabled(true);
                break;
        }
    }
    
    public void cmdSetColumnTableSize(){
        
        int totalWidth = giftListTable.getWidth();
        
        giftListTable.revalidate();
        giftListTable.getColumnModel().getColumn(COL_NO).setPreferredWidth((int)(totalWidth*0.05));
        giftListTable.getColumnModel().getColumn(COL_ITEM_CODE).setPreferredWidth((int)(totalWidth*0.20));
        giftListTable.getColumnModel().getColumn(COL_ITEM_NAME).setPreferredWidth((int)(totalWidth*0.40));
        giftListTable.getColumnModel().getColumn(COL_SERIAL_CODE).setPreferredWidth((int)(totalWidth*0.15));
        giftListTable.getColumnModel().getColumn(COL_ITEM_POINT).setPreferredWidth((int)(totalWidth*0.10));
        giftListTable.getColumnModel().getColumn(COL_AMOUNT).setPreferredWidth((int)(totalWidth*0.10));
        
        //giftListTable.revalidate ();
        giftListTable.repaint();
    }
    
    /** createDataModel(Vector data)
     *  return Vector that can be use as data model for
     *  gift item list table
     *  @param Vector data
     *  @return Vector
     */
    private Vector createDataModel(Vector data){
        Vector result = new Vector();
        Vector row;
        int size = data.size();
        GiftItem item;
        for(int i = 0; i < size; i++){
            item = new GiftItem();
            item = (GiftItem) data.get(i);
            row = new Vector();
            
            row.add(i+1+"");
            row.add(item.getCode());
            row.add(item.getName());
            if(item.getSerialCode().length()>0){
                row.add(item.getSerialCode());
            }
            else{
                row.add("N/A");
            }
            row.add(item.getPoint()+"");
            row.add(item.getAmount()+"");
            
            result.add(row);
        }
        
        return result;
    }
    
    /** getColumnIdentifier
     *  return a Vector contains column name
     *  translated from constant columnNames
     *  @return Vector
     */
    private Vector getColumnIdentifier(){
        Vector result = new Vector();
        int size = columnNames.length;
        
        for(int i = 0; i < size; i++){
            result.add(columnNames[i]);
        }
        
        return result;
    }
    
    /** init state of this form */
    public void initState(){
        
        
        
        custTypeComboBox.setEnabled(false);
        memberCodeTextField.setEnabled(false);
        memberNameTextField.setEnabled(false);
        
        itemCodeTextField.setEnabled(false);
        itemNameTextField.setEnabled(false);
        itemPointTextField.setEnabled(false);
        amountTextField.setEnabled(false);
        
        itemSearchButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);
        itemRemoveButton.setEnabled(false);
        
        saveGiftTransButton.setEnabled(false);
        //salesTextField.requestFocusInWindow();
        servTypeComboBox.setEnabled(true);
        servTypeComboBox.requestFocusInWindow();
    }
    
    /** cmdNewGiftTrans
     *  reset form for new gift transaction
     *  @return String err
     */
    public String cmdNewGiftTrans(){
        String errMsg = "";
        boolean isOk = true;
        if(this.getModel().getGiftTransDetail().size()>0)
            isOk = (JOptionPane.showConfirmDialog(this,"Are you sure make a New Gift Transaction?","New Gift Trans Confirmation",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION);
        if(isOk){
            try{
                initField();
                changeCommandPanelState(Command.NONE);
                initState();
            }
            catch(Exception e){
                errMsg = GiftController.errMsg[GiftController.UNKNOWN_ERR];
            }
        }
        return errMsg;
    }
    
    /**
     * isValidInt(String)
     * return true if input String can be converted into int
     * @param String
     * @return boolean
     */
    private boolean isValidInt(String st){
        try{
            Integer.parseInt(st);
        }
        catch(NumberFormatException nfe){
            this.setErrMsg(GiftController.errMsg[GiftController.NUMBER_FORMAT_ERR]);
            return false;
        }
        return true;
    }
    
    /**
     * isValidAmount(String)
     * return true if input String is a valid amount
     * implies isValidInt(String)
     * @param String
     * @return boolean
     * @see isValidInt(String)
     */
    private boolean isValidAmount(String st){
        boolean result = false;
        if(Validator.isFloat(st)){
            if(CashierMainApp.getDoubleFromFormated(st)>0){
                result = true;
                if(this.getModel().getCurrentBillDetailCode()!=null){
                    if(Integer.parseInt(st)!=1){
                        this.setErrMsg(GiftController.errMsg[GiftController.CODE_REQUIRED_ITEM_AMOUNT_ERR]);
                        result = false;
                    }
                }
            }
            else{
                this.setErrMsg(GiftController.errMsg[GiftController.AMOUNT_UNDERFLOW_ERR]);
            }
        }
        return result;
    }
    
    /**
     * cmdAdd()
     * add current item into list
     * @return String error
     */
    private String cmdAdd(){
        String errMsg = "";
        if((this.getModel().getRestPoint()-(this.getModel().getCurrentGiftItem().getPoint()*this.getModel().getCurrentGiftItem().getAmount()))>=0){
            try{
                if(this.getModel().getCurrentBillDetailCode()!=null){
                    int size = this.getModel().getListGiftSize();
                    int idx = this.getModel().getIndexOfItemWithCode(this.getModel().getCurrentGiftItem());
                    
                    if(size>0 && idx<size){
                        GiftItem item = (GiftItem) this.getModel().getListGift().get(idx);
                        String stCode = this.getModel().getCurrentGiftItem().getSerialCode();
                        while(idx<size && item.getSerialCode().equals(stCode) || stCode.length()==0){
                            // if not unique request for another serial code
                            JOptionPane.showMessageDialog(this,"Serial code must unique",FORM_GIFT,JOptionPane.INFORMATION_MESSAGE);
                            long giftLoc = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).giftLocationId);
                            int codeSize = CashSaleController.getSerialCodeCount(this.getMaterialId(), giftLoc);
                            
                            if(codeSize>0){
                                CashSaleController.showSerialCodeListDialog(this,getMaterialId(), giftLoc);
                                MaterialStockCode objCode = CashSaleController.getStockCodeChoosen();
                                stCode = objCode.getStockCode();
                            }
                            else{
                                JOptionPane.showMessageDialog(this,"Serial code not found, manual entry",FORM_GIFT,JOptionPane.INFORMATION_MESSAGE);
                                stCode = JOptionPane.showInputDialog(this,"Input another serial code (unique)",FORM_GIFT, JOptionPane.WARNING_MESSAGE);
                            }
                            
                            
                            if(stCode==null){
                                stCode = "";
                            }
                            this.getModel().getCurrentGiftItem().setSerialCode(stCode);
                            idx = this.getModel().getIndexOfItemWithCode(this.getModel().getCurrentGiftItem());
                            if(idx<size){
                                item = (GiftItem) this.getModel().getListGift().get(idx);
                                stCode = this.getModel().getCurrentGiftItem().getSerialCode();
                            }
                        }
                    }
                    this.getModel().addItemOnList();
                }
                else{
                    //System.out.println(".....MASUK KE KEMUNGKINAN APPEND");
                    if(this.getModel().getIndexOfItem(this.getModel().getCurrentGiftItem())>=this.getModel().getListGiftSize()){
                        this.getModel().addItemOnList();
                    }
                    else{
                        //System.out.println(".....MASUK KE APPEND");
                        this.getModel().setItemIndex(this.getModel().getIndexOfItem(this.getModel().getCurrentGiftItem()));
                        this.getModel().appendItemOnList();
                    }
                }
            }
            catch(Exception e){
                System.out.println("Error on GiftFrame.cmdAdd() : "+e);
                errMsg = GiftController.errMsg[GiftController.CANT_ADD_ITEM_ERR];
            }
        }
        else{
            errMsg = GiftController.errMsg[GiftController.INSUFFICIENT_AVAILABLE_POINT_ERR];
        }
        return errMsg;
    }
    
    /**
     * cmdEdit(int)
     * edit current selected item amount
     * @param int index of current selected item
     * @return String error
     */
    private String cmdEdit(int index){
        String errMsg = "";
        try{
            GiftController.setICommand(Command.EDIT);
            this.changeCommandPanelState(GiftController.getICommand());
            this.getModel().setItemIndex(index);
            this.getModel().getItemOnList();
            /**
             *edited by widi
             */
            this.getModel().setRequestedPoint(this.getModel().getRequestedPoint()-(this.getModel().getCurrentGiftItem().getPoint()*this.getModel().getCurrentGiftItem().getAmount()));
            this.drawItemInfo();
        }
        catch(Exception e){
            errMsg = GiftController.errMsg[GiftController.CANT_UPDATE_ITEM_ERR];
        }
        return errMsg;
    }
    
    /**
     * cmdDelete(int)
     * remove current selected item amount
     * @param int index of current selected item
     * @return String error
     */
    private String cmdDelete(int index){
        String errMsg = "";
        int answer = JOptionPane.showConfirmDialog(this,"Are you sure to delete this item?",FORM_GIFT,JOptionPane.YES_NO_OPTION);
        if(answer==JOptionPane.YES_OPTION){
            try{
                /** neuteralize cmdEdit effect */
                //this.getModel().setRequestedPoint(this.getModel().getRequestedPoint()+(this.getModel().getCurrentGiftItem().getPoint()*this.getModel().getCurrentGiftItem().getAmount()));
                this.getModel().setItemIndex(index);
                this.getModel().getItemOnList();
                this.getModel().removeItemOnList();
                GiftController.setICommand(Command.ADD);
                this.drawItemInfo();
                this.drawList();
                this.drawDetailInfo();
                this.changeCommandPanelState(GiftController.getICommand());
                if(this.getModel().getListGiftSize()==0){
                    saveGiftTransButton.setEnabled(false);
                }
            }
            catch(Exception e){
                errMsg = GiftController.errMsg[GiftController.CANT_REMOVE_ITEM_ERR];
            }
        }
        return errMsg;
    }
    
    /**
     * cmdUpdate()
     * remove current selected item amount
     * @return String error
     */
    private String cmdUpdate(){
        String errMsg = "";
        //if(GiftController.getICommand()==Command.ADD){
        
        //if(((this.getModel().getRestPoint()-(this.getModel().getCurrentGiftItem().getPoint()*this.getModel().getCurrentGiftItem().getAmount()))>=0)&&(GiftController.getICommand()==Command.ADD)){
        if(((this.getModel().getRestPoint()-(this.getModel().getCurrentGiftItem().getPoint()*this.getModel().getCurrentGiftItem().getAmount()))>=0)){
            try{
                this.getModel().setItemOnList();
                this.drawList();
                this.drawDetailInfo();
                this.drawItemInfo();
                GiftController.setICommand(Command.ADD);
                this.changeCommandPanelState(GiftController.getICommand());
            }
            catch(Exception e){
                errMsg = GiftController.errMsg[GiftController.CANT_UPDATE_ITEM_ERR];
            }
        }
        /*else if(((this.getModel().getRestPoint()-(this.getModel().getCurrentGiftItem().getPoint()*this.getModel().getCurrentGiftItem().getAmount()))>=0)&&(GiftController.getICommand()==Command.EDIT)){
             try{
                this.getModel().setItemOnList();
                this.drawList();
                this.drawDetailInfo();
                this.drawItemInfo();
                GiftController.setICommand(Command.ADD);
                this.changeCommandPanelState(GiftController.getICommand());
            }
            catch(Exception e){
                errMsg = GiftController.errMsg[GiftController.CANT_UPDATE_ITEM_ERR];
            }
        } */
        else{
            errMsg = GiftController.errMsg[GiftController.INSUFFICIENT_AVAILABLE_POINT_ERR];
        }
        return errMsg;
    }
    
    /**
     * cmdSaveAll()
     * update member point persistent
     * with current model
     * @return String err
     */
    private String cmdSaveAll(){
        String errMsg ="";
        if(this.getModel().isAllValueCompleted()){
            if(this.getModel().getRestPoint()>=0){
                try{
                    // handle for form printing
                    int answer = JOptionPane.showConfirmDialog(this,"Print this form?","Printing Dialog",JOptionPane.YES_NO_OPTION);
                    if(answer==JOptionPane.YES_OPTION){
                        
                        this.setErrMsg(cmdPrint());
                        
                        if(isError()){
                            this.cmdShowErrMsg();
                        }
                    }
                    // update persistent from model
                    GiftController.updatePstFromModel(this.getModel());
                    this.getModel().setGiftTransDetail(new Vector()); // ignore confirm
                    cmdNewGiftTrans();
                }
                catch(Exception e){
                    errMsg = GiftController.errMsg[GiftController.CANT_UPDATE_PST_ERR];
                }
            }
            else {
                errMsg = GiftController.errMsg[GiftController.INSUFFICIENT_AVAILABLE_POINT_ERR];
                giftListTable.requestFocusInWindow();
            }
        }else{
            JOptionPane.showMessageDialog(this,"Please complete all values","Incomplete Data",JOptionPane.ERROR_MESSAGE);
            cmdNewGift();
        }
        return errMsg;
    }
    
    /**
     *  cmdPrint()
     *  print gift-taking invoice
     *  attached at save-and-close methode
     *  @return String err
     *  @see cmdSaveAll()
     */
    private String cmdPrint(){
        String errMsg = "";
        
        // TODO please help me write lines for printing process.Thanks
        
        PrintInvoicePOS print = new PrintInvoicePOS();
        print.printGiftTakingObj(this.getModel());
        
        return errMsg;
    }
    
    // END OF OWN MADE METHODE
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel10 = new javax.swing.JPanel();
        itemSearchButton = new javax.swing.JButton();
        itemUpdateButton = new javax.swing.JButton();
        itemRemoveButton = new javax.swing.JButton();
        transInfoPanel = new javax.swing.JPanel();
        salesInfoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        servTypeComboBox = new javax.swing.JComboBox();
        salesLabel = new javax.swing.JLabel();
        salesTextField = new javax.swing.JTextField();
        customerInfoPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        custTypeComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        memberNameTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        memberCodeTextField = new javax.swing.JTextField();
        detailPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        curPointTextField = new javax.swing.JTextField();
        usedPointTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        freePointTextField = new javax.swing.JTextField();
        invoiceEditorPanel = new javax.swing.JPanel();
        itemEditorPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        itemCodeTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        itemNameTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        itemPointTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        actionPanel = new javax.swing.JPanel();
        saveGiftTransButton = new javax.swing.JButton();
        newGiftTransButton = new javax.swing.JButton();
        closeGiftTransButton = new javax.swing.JButton();
        editTableButton = new javax.swing.JButton();
        itemListPanel = new javax.swing.JScrollPane();
        giftListTable = new javax.swing.JTable();

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel10.setBorder(new javax.swing.border.TitledBorder(""));
        itemSearchButton.setMnemonic('F');
        itemSearchButton.setText("Find Item (Alt F)");
        itemSearchButton.setEnabled(false);
        itemSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSearchButtonActionPerformed(evt);
            }
        });
        itemSearchButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemSearchButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(itemSearchButton, gridBagConstraints);

        itemUpdateButton.setMnemonic('U');
        itemUpdateButton.setText("Update (Alt U)");
        itemUpdateButton.setEnabled(false);
        itemUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemUpdateButtonActionPerformed(evt);
            }
        });
        itemUpdateButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemUpdateButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(itemUpdateButton, gridBagConstraints);

        itemRemoveButton.setMnemonic('R');
        itemRemoveButton.setText("Remove (Alt R)");
        itemRemoveButton.setEnabled(false);
        itemRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRemoveButtonActionPerformed(evt);
            }
        });
        itemRemoveButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemRemoveButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(itemRemoveButton, gridBagConstraints);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gift");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        transInfoPanel.setLayout(new java.awt.GridBagLayout());

        transInfoPanel.setBorder(new javax.swing.border.TitledBorder(""));
        salesInfoPanel.setLayout(new java.awt.GridBagLayout());

        salesInfoPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jLabel1.setText("Service Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        salesInfoPanel.add(jLabel1, gridBagConstraints);

        servTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Point Info", "Gift Taking" }));
        servTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                servTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        salesInfoPanel.add(servTypeComboBox, gridBagConstraints);

        salesLabel.setText("Sales Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        salesInfoPanel.add(salesLabel, gridBagConstraints);

        salesTextField.setColumns(10);
        salesTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesTextFieldActionPerformed(evt);
            }
        });
        salesTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salesTextFieldFocusGained(evt);
            }
        });
        salesTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salesTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        salesInfoPanel.add(salesTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        transInfoPanel.add(salesInfoPanel, gridBagConstraints);

        customerInfoPanel.setLayout(new java.awt.GridBagLayout());

        customerInfoPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jLabel3.setText("Customer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        customerInfoPanel.add(jLabel3, gridBagConstraints);

        custTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Member" }));
        custTypeComboBox.setEnabled(false);
        custTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                custTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        customerInfoPanel.add(custTypeComboBox, gridBagConstraints);

        jLabel4.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        customerInfoPanel.add(jLabel4, gridBagConstraints);

        memberNameTextField.setColumns(20);
        memberNameTextField.setEnabled(false);
        memberNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberNameTextFieldActionPerformed(evt);
            }
        });
        memberNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                memberNameTextFieldFocusGained(evt);
            }
        });
        memberNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                memberNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        customerInfoPanel.add(memberNameTextField, gridBagConstraints);

        jLabel5.setText("Member Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        customerInfoPanel.add(jLabel5, gridBagConstraints);

        memberCodeTextField.setColumns(15);
        memberCodeTextField.setEnabled(false);
        memberCodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberCodeTextFieldActionPerformed(evt);
            }
        });
        memberCodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                memberCodeTextFieldFocusGained(evt);
            }
        });
        memberCodeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                memberCodeTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        customerInfoPanel.add(memberCodeTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        transInfoPanel.add(customerInfoPanel, gridBagConstraints);

        detailPanel.setLayout(new java.awt.GridBagLayout());

        detailPanel.setBorder(new javax.swing.border.TitledBorder("Member Point Details"));
        jLabel13.setText("Current Point");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        detailPanel.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Point Used");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        detailPanel.add(jLabel14, gridBagConstraints);

        curPointTextField.setColumns(5);
        curPointTextField.setEditable(false);
        curPointTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        detailPanel.add(curPointTextField, gridBagConstraints);

        usedPointTextField.setColumns(5);
        usedPointTextField.setEditable(false);
        usedPointTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        detailPanel.add(usedPointTextField, gridBagConstraints);

        jLabel15.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        detailPanel.add(jLabel15, gridBagConstraints);

        freePointTextField.setColumns(5);
        freePointTextField.setEditable(false);
        freePointTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        detailPanel.add(freePointTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        transInfoPanel.add(detailPanel, gridBagConstraints);

        getContentPane().add(transInfoPanel, java.awt.BorderLayout.NORTH);

        invoiceEditorPanel.setLayout(new java.awt.GridBagLayout());

        invoiceEditorPanel.setBorder(new javax.swing.border.TitledBorder(""));
        invoiceEditorPanel.setPreferredSize(new java.awt.Dimension(771, 247));
        itemEditorPanel.setLayout(new java.awt.GridBagLayout());

        itemEditorPanel.setBorder(new javax.swing.border.TitledBorder(""));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 5));

        jPanel9.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jLabel9.setText("Item Code");
        jPanel9.add(jLabel9);

        itemCodeTextField.setColumns(10);
        itemCodeTextField.setEnabled(false);
        itemCodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCodeTextFieldActionPerformed(evt);
            }
        });
        itemCodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemCodeTextFieldFocusGained(evt);
            }
        });
        itemCodeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemCodeTextFieldKeyPressed(evt);
            }
        });

        jPanel9.add(itemCodeTextField);

        jLabel7.setText("Item Name");
        jPanel9.add(jLabel7);

        itemNameTextField.setColumns(25);
        itemNameTextField.setEnabled(false);
        itemNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNameTextFieldActionPerformed(evt);
            }
        });
        itemNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemNameTextFieldFocusGained(evt);
            }
        });
        itemNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemNameTextFieldKeyPressed(evt);
            }
        });

        jPanel9.add(itemNameTextField);

        jLabel10.setText("Item Point");
        jPanel9.add(jLabel10);

        itemPointTextField.setColumns(5);
        itemPointTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemPointTextField.setEnabled(false);
        itemPointTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemPointTextFieldFocusGained(evt);
            }
        });
        itemPointTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemPointTextFieldKeyPressed(evt);
            }
        });

        jPanel9.add(itemPointTextField);

        jLabel11.setText("Amount");
        jPanel9.add(jLabel11);

        amountTextField.setColumns(5);
        amountTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        amountTextField.setEnabled(false);
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
        });

        jPanel9.add(amountTextField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        itemEditorPanel.add(jPanel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        invoiceEditorPanel.add(itemEditorPanel, gridBagConstraints);

        actionPanel.setLayout(new java.awt.GridBagLayout());

        actionPanel.setBorder(new javax.swing.border.TitledBorder(""));
        saveGiftTransButton.setMnemonic('S');
        saveGiftTransButton.setText("Save Gift Transaction -F12");
        saveGiftTransButton.setEnabled(false);
        saveGiftTransButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGiftTransButtonActionPerformed(evt);
            }
        });
        saveGiftTransButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveGiftTransButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        actionPanel.add(saveGiftTransButton, gridBagConstraints);

        newGiftTransButton.setMnemonic('N');
        newGiftTransButton.setText("New Gift Trans -F5");
        newGiftTransButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGiftTransButtonActionPerformed(evt);
            }
        });
        newGiftTransButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newGiftTransButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        actionPanel.add(newGiftTransButton, gridBagConstraints);

        closeGiftTransButton.setMnemonic('X');
        closeGiftTransButton.setText("Close Gift Trans -Alt X");
        closeGiftTransButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeGiftTransButtonActionPerformed(evt);
            }
        });
        closeGiftTransButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closeGiftTransButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        actionPanel.add(closeGiftTransButton, gridBagConstraints);

        editTableButton.setMnemonic('A');
        editTableButton.setText("Edit Table -F4");
        editTableButton.setEnabled(false);
        editTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTableButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        actionPanel.add(editTableButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        invoiceEditorPanel.add(actionPanel, gridBagConstraints);

        itemListPanel.setBorder(new javax.swing.border.TitledBorder(""));
        giftListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Item Code", "Item Name", "Serial Code", "Item Point", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        giftListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                giftListTableKeyPressed(evt);
            }
        });

        itemListPanel.setViewportView(giftListTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        invoiceEditorPanel.add(itemListPanel, gridBagConstraints);

        getContentPane().add(invoiceEditorPanel, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private void editTableButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_editTableButtonActionPerformed
    {//GEN-HEADEREND:event_editTableButtonActionPerformed
        cmdEditTable();
    }//GEN-LAST:event_editTableButtonActionPerformed
    
    public void cmdEditTable(){
        if(giftListTable.getModel().getRowCount()>0){
            giftListTable.requestFocusInWindow();
            giftListTable.setRowSelectionInterval(0,0);
        }else{
            cmdNewGift();
        }
    }
    private void closeGiftTransButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_closeGiftTransButtonKeyPressed
    {//GEN-HEADEREND:event_closeGiftTransButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_closeGiftTransButtonKeyPressed
    
    private void newGiftTransButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_newGiftTransButtonKeyPressed
    {//GEN-HEADEREND:event_newGiftTransButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_newGiftTransButtonKeyPressed
    
    private void saveGiftTransButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_saveGiftTransButtonKeyPressed
    {//GEN-HEADEREND:event_saveGiftTransButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_saveGiftTransButtonKeyPressed
    
    private void itemRemoveButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemRemoveButtonKeyPressed
    {//GEN-HEADEREND:event_itemRemoveButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemRemoveButtonKeyPressed
    
    private void itemUpdateButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemUpdateButtonKeyPressed
    {//GEN-HEADEREND:event_itemUpdateButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemUpdateButtonKeyPressed
    
    private void itemSearchButtonKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemSearchButtonKeyPressed
    {//GEN-HEADEREND:event_itemSearchButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemSearchButtonKeyPressed
    
    private void itemPointTextFieldKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemPointTextFieldKeyPressed
    {//GEN-HEADEREND:event_itemPointTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemPointTextFieldKeyPressed
    
    private void formKeyPressed (java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyPressed
    {//GEN-HEADEREND:event_formKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed
    
    private void formWindowOpened (java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowOpened
    {//GEN-HEADEREND:event_formWindowOpened
        servTypeComboBox.requestFocusInWindow();
        cmdNewGiftTrans();
    }//GEN-LAST:event_formWindowOpened
    
    private void salesTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salesTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_F1){
            this.setErrMsg(this.cmdSearchSales(""));
            if(isNoError()){
                cmdNewCustomer();
            }
            else{
                this.cmdShowErrMsg();
                salesTextField.requestFocusInWindow();
            }
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_salesTextFieldKeyPressed
    
    private void memberNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_memberNameTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_F1){
            this.setErrMsg(this.cmdSearchMember("",""));
            if(isNoError()){
                drawCustomerInfo();
                drawDetailInfo();
                if(GiftController.getCurrentServiceType()==GiftController.GIFT_TAKING){
                    itemCodeTextField.setEnabled(true);
                    itemCodeTextField.requestFocusInWindow();
                    changeCommandPanelState(GiftController.getICommand());
                }
                else{
                    servTypeComboBox.requestFocusInWindow();
                }
            }
            else{
                this.cmdShowErrMsg();
            }
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_memberNameTextFieldKeyPressed
    
    public void getGlobalKeyListener(KeyEvent evt){
        switch(evt.getKeyCode()){
            case KeyEvent.VK_F1 :
                break;
            case KeyEvent.VK_F2:
                cmdNewGift();
                break;
            case KeyEvent.VK_F3:
                break;
            case KeyEvent.VK_F4:
                if(editTableButton.isEnabled())
                    cmdEditTable();
                break;
            case KeyEvent.VK_F5:
                cmdNewGiftTrans();
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
                if(saveGiftTransButton.isEnabled())
                    cmdSaveAll();
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
    private void memberCodeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_memberCodeTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_F1){
            this.setErrMsg(this.cmdSearchMember("",""));
            if(isNoError()){
                drawCustomerInfo();
                drawDetailInfo();
                if(GiftController.getCurrentServiceType()==GiftController.GIFT_TAKING){
                    itemCodeTextField.setEnabled(true);
                    itemCodeTextField.requestFocusInWindow();
                    changeCommandPanelState(GiftController.getICommand());
                }
                else{
                    servTypeComboBox.requestFocusInWindow();
                }
            }
            else{
                this.cmdShowErrMsg();
            }
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_memberCodeTextFieldKeyPressed
    
    private void amountTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountTextFieldKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                this.getModel().setCurrentGiftItem(null);
                this.getModel().setCurrentBillDetail(null);
                this.getModel().setCurrentBillDetailCode(null);
                drawItemInfo();
                itemCodeTextField.requestFocusInWindow();
                break;
            case KeyEvent.VK_PAGE_DOWN:
                giftListTable.requestFocusInWindow();
                break;
            default:
                getGlobalKeyListener(evt);
                break;
        }
        
    }//GEN-LAST:event_amountTextFieldKeyPressed
    
    private void itemNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemNameTextFieldKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                this.getModel().setCurrentGiftItem(null);
                this.getModel().setCurrentBillDetail(null);
                this.getModel().setCurrentBillDetailCode(null);
                drawItemInfo();
                itemCodeTextField.requestFocusInWindow();
                break;
            case KeyEvent.VK_F1:
                this.setErrMsg(cmdSearchGift(itemCodeTextField.getText(),
                itemNameTextField.getText(), 0));
                if(this.isNoError()){
                    drawItemInfo();
                    amountTextField.setText("1");
                    amountTextField.setEnabled(true);
                    amountTextField.requestFocusInWindow();
                }
                else{
                    cmdShowErrMsg();
                    itemCodeTextField.requestFocusInWindow();
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                giftListTable.requestFocusInWindow();
                break;
            default:
                getGlobalKeyListener(evt);
                break;
        }
    }//GEN-LAST:event_itemNameTextFieldKeyPressed
    
    private void itemCodeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemCodeTextFieldKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                this.getModel().setCurrentGiftItem(null);
                this.getModel().setCurrentBillDetail(null);
                this.getModel().setCurrentBillDetailCode(null);
                drawItemInfo();
                itemCodeTextField.requestFocusInWindow();
                break;
            case KeyEvent.VK_F1:
                this.setErrMsg(cmdSearchGift(itemCodeTextField.getText(),
                itemNameTextField.getText(), 0));
                if(this.isNoError()){
                    drawItemInfo();
                    amountTextField.setText("1");
                    amountTextField.setEnabled(true);
                    amountTextField.requestFocusInWindow();
                }
                else{
                    cmdShowErrMsg();
                    itemCodeTextField.requestFocusInWindow();
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                giftListTable.requestFocusInWindow();
                break;
            default:
                getGlobalKeyListener(evt);
                break;
        }
    }//GEN-LAST:event_itemCodeTextFieldKeyPressed
    
    private void saveGiftTransButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGiftTransButtonActionPerformed
        
        this.setErrMsg(cmdSaveAll());
        if(isError()){
            this.cmdShowErrMsg();
        }
        else{
            cmdNewGiftTrans();
        }
    }//GEN-LAST:event_saveGiftTransButtonActionPerformed
    
    private void itemRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRemoveButtonActionPerformed
        this.setErrMsg(cmdDelete(this.getModel().getItemIndex()));
        if(isError()){
            this.cmdShowErrMsg();
        }
        itemCodeTextField.requestFocusInWindow();
    }//GEN-LAST:event_itemRemoveButtonActionPerformed
    
    private void itemUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemUpdateButtonActionPerformed
        String amount = amountTextField.getText();
        if(isValidAmount(amount)){
            this.getModel().getCurrentGiftItem().setAmount(Integer.parseInt(amount));
            this.getModel().getCurrentBillDetail().setQty(Integer.parseInt(amount));
            this.setErrMsg(cmdUpdate());
        }
        if(isError()){
            this.cmdShowErrMsg();
        }
        itemCodeTextField.requestFocusInWindow();
    }//GEN-LAST:event_itemUpdateButtonActionPerformed
    
    private void giftListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_giftListTableKeyPressed
        int index = giftListTable.getSelectedRow();
        
        switch(evt.getKeyCode()){
            case KeyEvent.VK_ENTER:
                if(index<0){
                    errMsg = GiftController.errMsg[GiftController.NO_ITEM_SELECTED];
                    this.cmdShowErrMsg();
                    giftListTable.requestFocusInWindow();
                }
                else{
                    this.setErrMsg(cmdEdit(index));
                    if(isError()){
                        this.cmdShowErrMsg();
                    }
                    amountTextField.requestFocusInWindow();
                }
                break;
            case KeyEvent.VK_DELETE:
                if(index<0){
                    errMsg = GiftController.errMsg[GiftController.NO_ITEM_SELECTED];
                    this.cmdShowErrMsg();
                    giftListTable.requestFocusInWindow();
                }
                else{
                    //this.setErrMsg(cmdEdit(index));
                    this.setErrMsg(cmdDelete(index));
                    if(isError()){
                        this.cmdShowErrMsg();
                    }
                    itemCodeTextField.requestFocusInWindow();
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                saveGiftTransButton.requestFocusInWindow();
                break;
            case KeyEvent.VK_ESCAPE:
                itemCodeTextField.requestFocusInWindow();
                break;
            default:
                getGlobalKeyListener(evt);
                break;
        }
    }//GEN-LAST:event_giftListTableKeyPressed
    
    private void itemSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSearchButtonActionPerformed
        String code = itemCodeTextField.getText();
        String name = itemNameTextField.getText();
        this.setErrMsg(cmdSearchGift(code,name,0));
        if(this.isNoError()){
            drawItemInfo();
            amountTextField.setText("1");
            amountTextField.setEnabled(true);
            amountTextField.requestFocusInWindow();
        }
        else{
            cmdShowErrMsg();
            itemCodeTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_itemSearchButtonActionPerformed
    
    private void amountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountTextFieldActionPerformed
        String amount = amountTextField.getText();
        if(isValidAmount(amount)){
            this.getModel().getCurrentGiftItem().setAmount(CashierMainApp.getDoubleFromFormated(amount));
            this.getModel().getCurrentBillDetail().setQty(CashierMainApp.getDoubleFromFormated(amount));
            this.getModel().getCurrentBillDetail().setTotalPrice(this.getModel().getCurrentBillDetail().getQty()*this.getModel().getCurrentBillDetail().getItemPrice());
            switch(GiftController.getICommand()){
                case Command.ADD:
                    //itemAddButton.requestFocusInWindow();
                    this.setErrMsg(cmdAdd());
                    if(isNoError()){
                        drawItemInfo();
                        drawList();
                        drawDetailInfo();
                        saveGiftTransButton.setEnabled(true);
                        itemCodeTextField.requestFocusInWindow();
                    }
                    else{
                        this.cmdShowErrMsg();
                        amountTextField.requestFocusInWindow();
                    }
                    break;
                case Command.EDIT:
                    //itemUpdateButton.requestFocusInWindow();
                    this.setErrMsg(cmdUpdate());
                    if(isError()){
                        this.cmdShowErrMsg();
                        amountTextField.requestFocusInWindow();
                    }
                    else{
                        itemCodeTextField.requestFocusInWindow();
                    }
                    break;
            }
        }
        else{
            cmdShowErrMsg();
            amountTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_amountTextFieldActionPerformed
    
    private void itemNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNameTextFieldActionPerformed
        String name = itemNameTextField.getText();
        this.setErrMsg(cmdSearchGift("",name,0));
        if(this.isNoError()){
            drawItemInfo();
            amountTextField.setText("1");
            amountTextField.setEnabled(true);
            amountTextField.requestFocusInWindow();
        }
        else{
            GiftController.setErr(GiftController.errMsg[GiftController.NONE]);
            itemCodeTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_itemNameTextFieldActionPerformed
    
    private void amountTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountTextFieldFocusGained
        amountTextField.selectAll();
    }//GEN-LAST:event_amountTextFieldFocusGained
    
    private void itemPointTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemPointTextFieldFocusGained
        itemPointTextField.selectAll();
    }//GEN-LAST:event_itemPointTextFieldFocusGained
    
    private void itemNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemNameTextFieldFocusGained
        itemNameTextField.selectAll();
    }//GEN-LAST:event_itemNameTextFieldFocusGained
    
    private void itemCodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemCodeTextFieldFocusGained
        itemCodeTextField.selectAll();
    }//GEN-LAST:event_itemCodeTextFieldFocusGained
    
    private void memberNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_memberNameTextFieldFocusGained
        memberNameTextField.selectAll();
    }//GEN-LAST:event_memberNameTextFieldFocusGained
    
    private void memberCodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_memberCodeTextFieldFocusGained
        memberCodeTextField.selectAll();
    }//GEN-LAST:event_memberCodeTextFieldFocusGained
    
    private void salesTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesTextFieldFocusGained
        salesTextField.selectAll();
    }//GEN-LAST:event_salesTextFieldFocusGained
    
    private void newGiftTransButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGiftTransButtonActionPerformed
        this.setErrMsg(cmdNewGiftTrans());
        if(this.isError()){
            cmdShowErrMsg();
        }
    }//GEN-LAST:event_newGiftTransButtonActionPerformed
    
    private void closeGiftTransButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeGiftTransButtonActionPerformed
        this.setErrMsg(cmdCloseWindow());
        if(this.isError()){
            cmdShowErrMsg();
        }
    }//GEN-LAST:event_closeGiftTransButtonActionPerformed
    
    private void itemCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCodeTextFieldActionPerformed
        String code = itemCodeTextField.getText();
        if(code.length()>0){
            this.setErrMsg(cmdSearchGift(code,"",0));
            if(this.isNoError()){
                drawItemInfo();
                amountTextField.setText("1");
                amountTextField.setEnabled(true);
                amountTextField.requestFocusInWindow();
            }
            else{
                //cmdShowErrMsg();
                GiftController.setErr(GiftController.errMsg[GiftController.NONE]);
                itemCodeTextField.requestFocusInWindow();
            }
        }
        else{
            itemNameTextField.setEnabled(true);
            itemNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_itemCodeTextFieldActionPerformed
    
    public void cmdNewGift(){
        if(this.getModel().isAnyCustomer()){
            itemCodeTextField.setEnabled(true);
            itemCodeTextField.requestFocusInWindow();
        }else{
            JOptionPane.showMessageDialog(this,"Please set any customer","Incomplete Data",JOptionPane.ERROR_MESSAGE);
            cmdNewCustomer();
        }
    }
    
    public void cmdNewCustomer(){
        custTypeComboBox.setEnabled(true);
        custTypeComboBox.requestFocusInWindow();
    }
    private void memberNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberNameTextFieldActionPerformed
        String name = memberNameTextField.getText();
        this.setErrMsg(cmdSearchMember("",name));
        if(this.isNoError()){
            drawCustomerInfo();
            drawDetailInfo();
            if(GiftController.getCurrentServiceType()==GiftController.GIFT_TAKING){
                cmdNewGift();
                changeCommandPanelState(GiftController.getICommand());
            }
            else{
                servTypeComboBox.requestFocusInWindow();
            }
        }
        else{
            //cmdShowErrMsg();
            GiftController.setErr(GiftController.errMsg[GiftController.NONE]);
            memberNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_memberNameTextFieldActionPerformed
    
    private void memberCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberCodeTextFieldActionPerformed
        String code = memberCodeTextField.getText();
        if(code.length()>0){
            this.setErrMsg(cmdSearchMember(code,""));
            if(this.isNoError()){
                drawCustomerInfo();
                drawDetailInfo();
                if(GiftController.getCurrentServiceType()==GiftController.GIFT_TAKING){
                    itemCodeTextField.setEnabled(true);
                    itemCodeTextField.requestFocusInWindow();
                    changeCommandPanelState(GiftController.getICommand());
                }
                else{
                    servTypeComboBox.requestFocusInWindow();
                }
            }
            else{
                //cmdShowErrMsg();
                GiftController.setErr(GiftController.errMsg[GiftController.NONE]);
                memberCodeTextField.requestFocusInWindow();
            }
        }
        else{
            memberNameTextField.setEnabled(true);
            memberNameTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_memberCodeTextFieldActionPerformed
    
    private void custTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_custTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            this.setErrMsg(cmdChangeCostumerType());
            if(this.isNoError()){ // no error
                memberCodeTextField.setEnabled(true);
                memberCodeTextField.requestFocusInWindow();
            }
            else{
                cmdShowErrMsg();
                custTypeComboBox.requestFocusInWindow();
            }
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_custTypeComboBoxKeyPressed
    
    private void salesTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesTextFieldActionPerformed
        //this.setModel(new GiftModel(CashierMainApp.getCashMaster().getLocationId(),CashierMainApp.getCashMaster().getCashierNumber()));
        this.setErrMsg(cmdSearchSales(salesTextField.getText()));
        if(this.isNoError()){ // no error
            drawSalesInfo();
            cmdNewCustomer();
        }
        else{
            //cmdShowErrMsg();
            GiftController.setErr(GiftController.errMsg[GiftController.NONE]);
            salesTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_salesTextFieldActionPerformed
    
    public void cmdSelectSales(){
        if(CashierMainApp.isEnableSaleEntry()){
            salesTextField.setEnabled(true);
            salesTextField.setEditable(true);
            salesTextField.requestFocusInWindow();
        }
        else{
            Sales sales = new Sales();
            this.getModel().setSales(sales);
            this.getModel().getGiftTrans().setSalesCode(sales.getCode());
            cmdNewCustomer();
        }
    }
    private void servTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_servTypeComboBoxKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            this.setErrMsg(cmdChangeServiceType());
            if(this.isNoError()){ // no error
                cmdSelectSales();
            }
            else {
                cmdShowErrMsg();
                servTypeComboBox.requestFocusInWindow();
            }
        }else{
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_servTypeComboBoxKeyPressed
    
    /**
     * Getter for property errMsg.
     * @return Value of property errMsg.
     */
    public java.lang.String getErrMsg() {
        return errMsg;
    }
    
    /**
     * Setter for property errMsg.
     * @param errMsg New value of property errMsg.
     */
    public void setErrMsg(java.lang.String errMsg) {
        this.errMsg = errMsg;
    }
    
    /**
     * Getter for property model.
     * @return Value of property model.
     */
    public com.dimata.pos.cashier.GiftModel getModel() {
        return model;
    }
    
    /**
     * Setter for property model.
     * @param model New value of property model.
     */
    public void setModel(com.dimata.pos.cashier.GiftModel model) {
        this.model = model;
    }
    
    public static void main(String[] argv){
        GiftFrame test = new GiftFrame(new JFrame(), true);
        test.show();
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JButton closeGiftTransButton;
    private javax.swing.JTextField curPointTextField;
    private javax.swing.JComboBox custTypeComboBox;
    private javax.swing.JPanel customerInfoPanel;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JButton editTableButton;
    private javax.swing.JTextField freePointTextField;
    private javax.swing.JTable giftListTable;
    private javax.swing.JPanel invoiceEditorPanel;
    private javax.swing.JTextField itemCodeTextField;
    private javax.swing.JPanel itemEditorPanel;
    private javax.swing.JScrollPane itemListPanel;
    private javax.swing.JTextField itemNameTextField;
    private javax.swing.JTextField itemPointTextField;
    private javax.swing.JButton itemRemoveButton;
    private javax.swing.JButton itemSearchButton;
    private javax.swing.JButton itemUpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField memberCodeTextField;
    private javax.swing.JTextField memberNameTextField;
    private javax.swing.JButton newGiftTransButton;
    private javax.swing.JPanel salesInfoPanel;
    private javax.swing.JLabel salesLabel;
    private javax.swing.JTextField salesTextField;
    private javax.swing.JButton saveGiftTransButton;
    private javax.swing.JComboBox servTypeComboBox;
    private javax.swing.JPanel transInfoPanel;
    private javax.swing.JTextField usedPointTextField;
    // End of variables declaration//GEN-END:variables
    
}
