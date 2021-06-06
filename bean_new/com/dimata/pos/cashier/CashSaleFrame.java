/*
 * CashSaleFrame.java
 *
 * Created on December 4, 2004, 7:11 AM
 */

package com.dimata.pos.cashier;

import com.dimata.ObjLink.BOCashier.CustomerLink;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.printAPI.PrintInvoicePOS;
import com.dimata.pos.printAPI.PrintIvoiceReturPOS;
import com.dimata.pos.session.processdata.SessTransactionData;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import com.dimata.util.Validator;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author  Widi Pradnyana
 */
//public class CashSaleFrame extends javax.swing.JInternalFrame {

public class CashSaleFrame extends JDialog {

    public static final int TRANS_INVOICE = 0;
    public static final int TRANS_RETURN = 1;
    public static final int TRANS_PENDING_ORDER = 2;
    public static final int TRANS_PAY_PENDING_ORDER = 3;
    public static final int TRANS_GIFT = 4;
    public static final int TRANS_CREDIT_PAYMENT = 5;
    public static final int TRANS_OPEN_BILL = 6;
    public static final int TRANS_COST = 7;
    public static final int TRANS_COMPLIMENT = 8;
    public static final int TRANS_HALF_INVOICE = 9;
    public static final int TRANS_IMVOICE_CLAIM = 10;

    public static final String[] transType = {
        "Invoice",
        "Return Invoice",
        "Pending Order",
        "Pay Pending Order",
        "Gift Trans",
        "Credit Payment",
        "Open Bill",
        "Costing",
        "Compliment",
        "Half Invoice",
        "Internal Invoice"
    };

    private static final int MEMBER_TYPE = 1;
    private static final int NON_MEMBER_TYPE = 0;
    private MemberReg nonCustomerMember = new MemberReg();
    private int transTypeChoosen = -1;
    private int processType = -1;
    public static final int TRANSACTION_PROCESSING = 1;
    public static final int TRANSACTION_REPRINTING = 2;
    public static final int TRANSACTION_HALF_INVOICE = 3;
    public static final int TRANSACTION_INVOICE_CLAIM = 4;
    CashierMainFrame parent = null;

    private int netTransPriority = CashierMainApp.getDSJ_CashierXML().getConfig(0).netTransPriority;
    private static final int PARALEL = 0;
    private static final int TAX_FIRST = 1;
    private static final int SERVICE_FIRST = 2;

    private Billdetail candidateBilldetail = null;
    private BillDetailCode candidateBilldetailCode = null;
    long totalTrans = 0;
    private Vector columnIdentifiers = null;
    private Vector vctBillDetail = new Vector();
    private BillMain billMain = null;
    private CashCashier cashCashier = new CashCashier();
    private DefaultTableModel saleTableModel = null;
    private boolean memberFound = false;
    private boolean okOtherCost = false;
    private double pctHalfInvoice = 100;
    private DefaultSaleModel saleModel = new DefaultSaleModel();
    private static String[] columnNames = {
        "No", "Item Code", "Item Name", "Serial Number", "Price", "Disc", "Qty", "Total"
    };
    //private static ArrayList columnIdentifier = new ArrayList();
    private static int COL_DETAIL_NO = 0;
    private static int COL_DETAIL_ITEM_CODE = 1;
    private static int COL_DETAIL_ITEM_NAME = 2;
    private static int COL_DETAIL_STOCK_CODE = 3;
    private static int COL_DETAIL_ITEM_PRICE = 4;
    private static int COL_DETAIL_ITEM_DISC = 5;
    private static int COL_DETAIL_ITEM_QTY = 6;
    private static int COL_DETAIL_ITEM_TOTAL = 7;


    public static final int DISC_PCT = 0;
    public static final int DISC_VALUE = 1;

    private Hashtable hashLocation = new Hashtable();

    /** Creates new form CashSaleFrame */
    public CashSaleFrame(Frame parent, boolean modal) {
        super(parent, true);
        netTransPriority = CashierMainApp.getDSJ_CashierXML().getConfig(0).netTransPriority;
        initComponents();
        //this.setSaleModel (new DefaultSaleModel());
        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                guestSearchPanel.setVisible(false);
                memberShipPanel.setVisible(true);
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                guestSearchPanel.setVisible(true);
                memberShipPanel.setVisible(false);
                break;
            default :
                guestSearchPanel.setVisible(false);
                memberShipPanel.setVisible(true);
                break;
        }

        openBillPanel.setVisible(CashierMainApp.isEnableOpenBill());
        saveAsOpenBillButton.setVisible(CashierMainApp.isEnableOpenBill());
        saveAsCreditButton.setVisible(!CashierMainApp.isEnableOpenBill());
        salesNameTextField.setVisible(CashierMainApp.isEnableSaleEntry());
        salesNameLabel.setVisible(CashierMainApp.isEnableSaleEntry());
        otherCostLabel.setVisible(CashierMainApp.isEnableOtherCost());
        contactInputLabel.setVisible(CashierMainApp.isEnableContactInput());

        memberShipPanel.setVisible(CashierMainApp.isEnableMembership());
        memberGuestPanel.setVisible(memberShipPanel.isVisible() || guestSearchPanel.isVisible());
        memberOpenPanel.setVisible(memberGuestPanel.isVisible() || openBillPanel.isVisible());

        priceCurrComboBox.setVisible(CashierMainApp.isEnablePriceCurrSelect());
        priceCurrLabel.setVisible(CashierMainApp.isEnablePriceCurrSelect());

        locationComboBox.setVisible(CashierMainApp.isEnableLocationSelect());
        locationLabel.setVisible(CashierMainApp.isEnableLocationSelect());

        // transType ComboBox Model
        Vector vTransType = new Vector();
        vTransType.add(transType[TRANS_INVOICE]);
        vTransType.add(transType[TRANS_RETURN]);
        if (CashierMainApp.isEnablePendingOrder()) {
            vTransType.add(transType[TRANS_PENDING_ORDER]);
            vTransType.add(transType[TRANS_PAY_PENDING_ORDER]);
        }
        if (CashierMainApp.isEnableGiftTrans()) {
            vTransType.add(transType[TRANS_GIFT]);
        }
        if (CashierMainApp.isEnableCreditPayment()) {
            vTransType.add(transType[TRANS_CREDIT_PAYMENT]);
        }
        if (CashierMainApp.isEnableOpenBill()) {
            vTransType.add(transType[TRANS_OPEN_BILL]);
        }
        if (CashierMainApp.isEnableHalfInvoice()) {
            vTransType.add(transType[TRANS_HALF_INVOICE]);
        }

        saleTypeComboBox.setModel(new DefaultComboBoxModel(vTransType));

        this.initAllFields();
    }

    public CashSaleFrame() {
        initComponents();
        //this.setSaleModel (new DefaultSaleModel());
        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                guestSearchPanel.setVisible(false);
                memberShipPanel.setVisible(true);
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                guestSearchPanel.setVisible(true);
                memberShipPanel.setVisible(false);
                break;
            default :
                guestSearchPanel.setVisible(false);
                memberShipPanel.setVisible(true);
                break;
        }
        //this.setM
        openBillPanel.setVisible(false);
        this.initAllFields();
        //this.getSaleTableModel ().setColumnIdentifiers (this.getColumnIdentifiers ());
        //this.curLabel(CashierMainApp.getDSJ_CashierXML ().getConfig (0).)
    }

    double netTrans = 0;

    private void synchronizeModelAndTable() {
        Vector dataVector = new Vector();
        this.getSaleModel().synchronizeAllValues();
        this.salesNameTextField.setText(this.getSaleModel().getSalesPerson().getName());
        this.invoiceNumberTextField.setText(this.getSaleModel().getMainSale().getInvoiceNumber());
        int size = this.getSaleModel().getVectBillDetail().size();

        MemberReg tmpMember = this.getSaleModel().getCustomerServed();
        if (tmpMember.getOID() == CashSaleController.getCustomerNonMember().getOID()) {
            custTypeComboBox.setSelectedIndex(NON_MEMBER_TYPE);
            memberNameTextField.setText("");
            memberCodeTextField.setText("");
        } else {
            custTypeComboBox.setSelectedIndex(MEMBER_TYPE);
            memberNameTextField.setText(tmpMember.getPersonName());
            memberCodeTextField.setText(tmpMember.getMemberBarcode());
        }

        if (CashierMainApp.isEnableOpenBill()) {
            coverGuestNameTextField.setText(this.getSaleModel().getMainSale().getGuestName());
            coverTextField.setText(this.getSaleModel().getMainSale().getCoverNumber());
        }

        if (CashierMainApp.getIntegrationWith() == CashierMainApp.INTEGRATE_WITH_HANOMAN && this.getSaleModel().getMainSale().getSpecialId() > 0) {
            guestTypeComboBox.setSelectedIndex(0);
            guestTextField.setText(this.getSaleModel().getCustomerLink().getCustomerName());
            roomNumberTextField.setText(this.getSaleModel().getCustomerLink().getRoomNumber());
            resNumberTextField.setText(this.getSaleModel().getCustomerLink().getResNumber());
        } else {
            guestTypeComboBox.setSelectedIndex(1);
            guestTextField.setText("");
            roomNumberTextField.setText("");
            resNumberTextField.setText("");
        }

        double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
        String strRateType = this.getSaleModel().getCurrencyTypeUsed().getCode();
        long currTypeUsedId = this.getSaleModel().getRateUsed().getCurrencyTypeId();
        String fordate = CashierMainApp.getDSJ_CashierXML().getConfig(0).fordate;
        String fordecimal = CashierMainApp.getDSJ_CashierXML().getConfig(0).fordecimal;
        String forcurrency = CashierMainApp.getDSJ_CashierXML().getConfig(0).forcurrency;
        double tempTotalTrans = 0;

        this.totalValueCurTypeLabel.setText(strRateType);
        this.priceCurrComboBox.setSelectedItem(strRateType);

        for (int i = 0; i < size; i++) {
            String key = (String) this.getSaleModel().getVectBillDetail().get(i);
            Billdetail tempBillDetail = (Billdetail) this.getSaleModel().getHashBillDetail().get(key);
            BillDetailCode tempDetailCode = (BillDetailCode) this.getSaleModel().getHashBillDetailCode().get(key);
            if (tempBillDetail.getUpdateStatus() != PstBillDetail.UPDATE_STATUS_DELETED) {
                Vector vctTempBillDetail = new Vector();

                vctTempBillDetail.add("" + (i + 1));
                vctTempBillDetail.add(tempBillDetail.getSku());
                vctTempBillDetail.add(tempBillDetail.getItemName());

                if (tempDetailCode != null) {
                    vctTempBillDetail.add(tempDetailCode.getStockCode());
                } else {
                    vctTempBillDetail.add("");
                }


                vctTempBillDetail.add(strRateType + " " + toCurrency(tempBillDetail.getItemPrice() / dblRateUsed));
                if (tempBillDetail.getDiscType() == PstBillDetail.DISC_TYPE_PERCENT) {
                    vctTempBillDetail.add(toCurrency(tempBillDetail.getDiscPct()) + "%");
                } else if (tempBillDetail.getDiscType() == PstBillDetail.DISC_TYPE_VALUE) {
                    vctTempBillDetail.add(strRateType + " " + toCurrency(tempBillDetail.getDisc() / (dblRateUsed * (tempBillDetail.getQty() > 0 ? tempBillDetail.getQty() : 1))));
                } else {
                    vctTempBillDetail.add(strRateType + " " + toCurrency(0));
                }
                vctTempBillDetail.add(toCurrency(tempBillDetail.getQty()));

                vctTempBillDetail.add(strRateType + " " + toCurrency(tempBillDetail.getTotalPrice() / dblRateUsed));
                tempTotalTrans = tempTotalTrans + tempBillDetail.getTotalPrice();
                dataVector.add(vctTempBillDetail);
            }
        }

        //added by wpulantara for rounded things
        //--------------------------------
        if (dblRateUsed == 1) {
            double dRound = tempTotalTrans % dblRateUsed;
            if (dRound > 0) {
                if (dRound < tempTotalTrans)
                    tempTotalTrans = tempTotalTrans - dRound;
            }
        }
        //---------------------------------

        this.getSaleModel().setTotalTrans(tempTotalTrans);
        this.getSaleTableModel().setDataVector(dataVector, this.getColumnIdentifiers());


        this.saleDetailTable.validate();

        double tempDiscount = this.getSaleModel().getMainSale().getDiscount() / dblRateUsed;
        int discType = this.getSaleModel().getMainSale().getDiscType();

        discTransTextField2.setText(toCurrency(tempDiscount));

        lastDiscTypeComboBox.setSelectedIndex(DISC_VALUE);
        double taxValue = this.getSaleModel().getMainSale().getTaxValue() / dblRateUsed;
        double taxPct = this.getSaleModel().getMainSale().getTaxPercentage();
        if (taxValue > 0) {
            taxTypeComboBox.setSelectedIndex(TAX_TYPE_VALUE);
            incTaxlTransTextField.setText(toCurrency(taxValue));
        } else {
            taxTypeComboBox.setSelectedIndex(TAX_TYPE_PCT);
            incTaxlTransTextField.setText(toCurrency(taxPct));

        }
        double svcValue = this.getSaleModel().getMainSale().getServiceValue() / dblRateUsed;
        double svcPct = this.getSaleModel().getMainSale().getServicePct();
        if (svcValue > 0) {
            servTypeComboBox.setSelectedIndex(SERV_STATUS_VALUE);
            incServlTransTextField.setText(toCurrency(svcValue));
        } else {
            servTypeComboBox.setSelectedIndex(SERV_STATUS_PCT);
            incServlTransTextField.setText(toCurrency(svcPct));
        }
        netTrans = this.getSaleModel().getNetTrans();

        int poin = 0;
        poin = this.getSaleModel().getMemberPoin().getCredit();

        this.setNetTrans(netTrans);
        if (this.getSaleModel().getLastPayment() > 0) {
            CashReturn cashReturn = new CashReturn();
        }
        if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR) {
            lastDiscTypeComboBox.setSelectedIndex(DISC_VALUE);
            taxTypeComboBox.setSelectedIndex(TAX_TYPE_VALUE);
            servTypeComboBox.setSelectedIndex(SERV_STATUS_VALUE);
            lastDiscTypeComboBox.setEnabled(false);
            taxTypeComboBox.setEnabled(false);
            servTypeComboBox.setEnabled(false);

            discTransTextField2.setText(toCurrency(this.getSaleModel().getDiscAmount() / dblRateUsed));
            incTaxlTransTextField.setText(toCurrency(this.getSaleModel().getTaxAmount() / dblRateUsed));
            incServlTransTextField.setText(toCurrency(this.getSaleModel().getSvcAmount() / dblRateUsed));
            this.getSaleModel().getCashPayments().clear();

            if (this.getSaleModel().getNetTrans() > 0) {
                CashPayments newCashPayment = new CashPayments();
                newCashPayment.setAmount(this.getSaleModel().getNetTrans());
                newCashPayment.setRate(dblRateUsed);
                newCashPayment.setCurrencyId(currTypeUsedId);
                newCashPayment.setPayDateTime(new Date());
                newCashPayment.setUpdateStatus(PstCashPayment.UPDATE_STATUS_INSERTED);
                newCashPayment.setPaymentType(PstCashPayment.CASH);
                this.getSaleModel().getCashPayments().clear();
                this.getSaleModel().getCashPayments().put(newCashPayment.getPayDateTime(), newCashPayment);
            }
        }
        cmdUpdateButton();

        poinTextField.setText(String.valueOf(poin));

        lastPaymentTransTextField.setText(toCurrency(this.getSaleModel().getLastPayment()));

        otherCostTextField.setText(toCurrency(this.getSaleModel().getTotOtherCost()));

        totalTransTextField.setText(toCurrency((tempTotalTrans / dblRateUsed)));
        totalTransTextField.setSelectionEnd(lastPaymentTransTextField.getText().length());

        totalValueLabel.setText(toCurrency((this.getSaleModel().getNetTrans() < 0 ? 0 : this.getSaleModel().getNetTrans()) / dblRateUsed));
        totalValueLabel.setSelectionEnd(totalValueLabel.getText().length());
        cmdSetColumnTableSize();
    }

    private void cmdUpdateButton() {

        if (this.getSaleModel().isAnySaleDetail() || this.getSaleModel().isAnyPayments()) {
            editTableButton.setEnabled(true);
            if (saveAsCreditSalesButton.isVisible())
                saveAsCreditSalesButton.setEnabled(true);
            if (this.getSaleModel().getMainSale().getTransactionStatus() == PstBillMain.TRANS_STATUS_CLOSE)
                saveAsOpenBillButton.setEnabled(true);
            if (this.getSaleModel().getMainSale().getDocType() != PstBillMain.TYPE_RETUR) {
                doPaymentButton.setEnabled(true);
            } else {
                saveAsCreditSalesButton.setEnabled(false);
                saveAsOpenBillButton.setEnabled(false);
            }

        }
        if (this.getSaleModel().isAllValuesCompleted()) {
            printCashButton.setEnabled(true);
        } else {
            doPaymentButton.setEnabled(false);
            editTableButton.setEnabled(false);
            setPaymentButton.setEnabled(false);
            saveAsCreditSalesButton.setEnabled(false);

        }

        if (this.getSaleModel().getHashBillDetail().elements().hasMoreElements()) {
            editItemButton.setEnabled(true);
            if (this.getSaleModel().getMainSale().getDocType() != PstBillMain.TYPE_RETUR) {
                if (saveAsCreditSalesButton.isVisible())
                    saveAsCreditButton.setEnabled(true);
            } else {
                printCashButton.setEnabled(true);
            }
        } else {
            if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR)
                printCashButton.setEnabled(false);
            editItemButton.setEnabled(false);
            saveAsCreditButton.setEnabled(false);
        }

    }

    private void setMnemonics() {
        itemAddButton.setMnemonic('A');
        itemUpdateButton.setMnemonic('U');
        itemRemoveButton.setMnemonic('Z');
        cancelEditTableButton.setMnemonic('C');
        doPaymentButton.setMnemonic('D');
        srcOpenBillButton.setMnemonic(KeyEvent.VK_F5);
        saveAsCreditSalesButton.setMnemonic(KeyEvent.VK_F6);
        pendingOrderSearchButton.setMnemonic(KeyEvent.VK_F7);
        invoiceSearchButton.setMnemonic(KeyEvent.VK_F8);
        setPaymentButton.setMnemonic(KeyEvent.VK_F11);
        printCashButton.setMnemonic(KeyEvent.VK_F12);
        saveAsOpenBillButton.setMnemonic(KeyEvent.VK_F9);
        closeSaleButton.setMnemonic('X');
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        otherInfoPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        topPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        shopNameTextField = new javax.swing.JTextField();
        timeTextField = new javax.swing.JTextField();
        saleDateTextField = new javax.swing.JTextField();
        cashierNumberTextField = new javax.swing.JTextField();
        cvrLabel6 = new javax.swing.JLabel();
        persDiscLabel23 = new javax.swing.JLabel();
        personalDiscTextField = new javax.swing.JTextField();
        coverNoTextField = new javax.swing.JTextField();
        hiddenCommandPanel = new javax.swing.JPanel();
        invoiceSearchButton = new javax.swing.JButton();
        pendingOrderSearchButton = new javax.swing.JButton();
        setPaymentButton = new javax.swing.JButton();
        doPaymentButton = new javax.swing.JButton();
        saveAsCreditSalesButton = new javax.swing.JButton();
        srcOpenBillButton = new javax.swing.JButton();
        editTableButton = new javax.swing.JButton();
        itemUpdateButton = new javax.swing.JButton();
        itemRemoveButton = new javax.swing.JButton();
        cancelEditTableButton = new javax.swing.JButton();
        notesLabel19 = new javax.swing.JLabel();
        notesTextField = new javax.swing.JTextField();
        saleCommandPanel = new javax.swing.JPanel();
        itemAddButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        saleTypePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        saleTypeComboBox = new javax.swing.JComboBox();
        salesNameLabel = new javax.swing.JLabel();
        salesNameTextField = new javax.swing.JTextField();
        returInvTextField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        invoiceNumberTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        priceCurrLabel = new javax.swing.JLabel();
        priceCurrComboBox = new javax.swing.JComboBox();
        locationComboBox = new javax.swing.JComboBox();
        locationLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        totalValueCurTypeLabel = new javax.swing.JLabel();
        totalValueLabel = new javax.swing.JTextField();
        itemListPanel = new javax.swing.JPanel();
        itemInfoPanel = new javax.swing.JPanel();
        memberOpenPanel = new javax.swing.JPanel();
        memberGuestPanel = new javax.swing.JPanel();
        memberShipPanel = new javax.swing.JPanel();
        custTypeLabel3 = new javax.swing.JLabel();
        custTypeComboBox = new javax.swing.JComboBox();
        membCodeLabel5 = new javax.swing.JLabel();
        memberCodeTextField = new javax.swing.JTextField();
        membNameLabel4 = new javax.swing.JLabel();
        memberNameTextField = new javax.swing.JTextField();
        guestSearchPanel = new javax.swing.JPanel();
        guestTypeLabel = new javax.swing.JLabel();
        guestTextField = new javax.swing.JTextField();
        roomNumberLabel = new javax.swing.JLabel();
        roomNumberTextField = new javax.swing.JTextField();
        resNumberLabel = new javax.swing.JLabel();
        resNumberTextField = new javax.swing.JTextField();
        guestTypeComboBox = new javax.swing.JComboBox();
        guestNameLabel = new javax.swing.JLabel();
        openBillPanel = new javax.swing.JPanel();
        coverLabel = new javax.swing.JLabel();
        coverTextField = new javax.swing.JTextField();
        coverGuestNameLabel = new javax.swing.JLabel();
        coverGuestNameTextField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        itemNameTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        itemCodeTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        itemDiscTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        itemQtyTextField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        itemPriceTextField = new javax.swing.JTextField();
        discTypeComboBox = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        serialNumTextField = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        discTypeLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        saleDetailTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        paymentDetailPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        totalTransTextField = new javax.swing.JTextField();
        otherCostTextField = new javax.swing.JTextField();
        lastPaymentTransTextField = new javax.swing.JTextField();
        lastDiscTypeComboBox = new javax.swing.JComboBox();
        discTransTextField2 = new javax.swing.JTextField();
        taxTypeComboBox = new javax.swing.JComboBox();
        incTaxlTransTextField = new javax.swing.JTextField();
        servTypeComboBox = new javax.swing.JComboBox();
        incServlTransTextField = new javax.swing.JTextField();
        curTypeComboBox = new javax.swing.JComboBox();
        paymentTransTextField = new javax.swing.JTextField();
        changeTransTextField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        poinTextField = new javax.swing.JTextField();
        currencyRateTextField = new javax.swing.JTextField();
        saleShortCutPanel = new javax.swing.JPanel();
        labelCommandPanel = new javax.swing.JPanel();
        newSaleLabel = new javax.swing.JLabel();
        totalSaleLabel = new javax.swing.JLabel();
        otherCostLabel = new javax.swing.JLabel();
        contactInputLabel = new javax.swing.JLabel();
        endSaleLabel = new javax.swing.JLabel();
        reprintLabel = new javax.swing.JLabel();
        paddingLabel1 = new javax.swing.JLabel();
        paddingLabel2 = new javax.swing.JLabel();
        buttonCommandPanel = new javax.swing.JPanel();
        editItemButton = new javax.swing.JButton();
        multiPaymentButton = new javax.swing.JButton();
        saveAsCreditButton = new javax.swing.JButton();
        printCashButton = new javax.swing.JButton();
        resetPaymentButton = new javax.swing.JButton();
        closeSaleButton = new javax.swing.JButton();
        saveAsOpenBillButton = new javax.swing.JButton();

        otherInfoPanel.setLayout(new java.awt.GridBagLayout());

        otherInfoPanel.setAutoscrolls(true);
        otherInfoPanel.setEnabled(false);
        jLabel18.setText("Copyright. PT. Dimata Sora Jayate");
        otherInfoPanel.add(jLabel18, new java.awt.GridBagConstraints());

        jLabel19.setText("www.dimata-solutions.com");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        otherInfoPanel.add(jLabel19, gridBagConstraints);

        topPanel.setLayout(new java.awt.GridBagLayout());

        topPanel.setBorder(new javax.swing.border.TitledBorder(""));
        topPanel.setName("Cash Balance Info");
        jLabel3.setText("Shop");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Cashier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(jLabel6, gridBagConstraints);

        shopNameTextField.setColumns(15);
        shopNameTextField.setEditable(false);
        shopNameTextField.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(shopNameTextField, gridBagConstraints);

        timeTextField.setColumns(15);
        timeTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(timeTextField, gridBagConstraints);

        saleDateTextField.setColumns(10);
        saleDateTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(saleDateTextField, gridBagConstraints);

        cashierNumberTextField.setColumns(5);
        cashierNumberTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        topPanel.add(cashierNumberTextField, gridBagConstraints);

        cvrLabel6.setText("Cvr. No");
        persDiscLabel23.setText("Personal Disc");
        personalDiscTextField.setColumns(8);
        personalDiscTextField.setEditable(false);
        personalDiscTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                personalDiscTextFieldFocusGained(evt);
            }
        });

        coverNoTextField.setColumns(5);
        coverNoTextField.setEditable(false);
        coverNoTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coverNoTextFieldFocusGained(evt);
            }
        });

        hiddenCommandPanel.setLayout(new java.awt.GridBagLayout());

        invoiceSearchButton.setText("Src Invoice-F8");
        invoiceSearchButton.setEnabled(false);
        invoiceSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceSearchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        hiddenCommandPanel.add(invoiceSearchButton, gridBagConstraints);

        pendingOrderSearchButton.setText("Src Pending Order-F7");
        pendingOrderSearchButton.setEnabled(false);
        pendingOrderSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingOrderSearchButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        hiddenCommandPanel.add(pendingOrderSearchButton, gridBagConstraints);

        setPaymentButton.setText("Multi Payment-F11");
        setPaymentButton.setEnabled(false);
        setPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPaymentButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        hiddenCommandPanel.add(setPaymentButton, gridBagConstraints);

        doPaymentButton.setText("Do Payments -F9");
        doPaymentButton.setEnabled(false);
        doPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doPaymentButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        hiddenCommandPanel.add(doPaymentButton, gridBagConstraints);

        saveAsCreditSalesButton.setText("Save As Credit -F6");
        saveAsCreditSalesButton.setEnabled(false);
        saveAsCreditSalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsCreditSalesButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        hiddenCommandPanel.add(saveAsCreditSalesButton, gridBagConstraints);

        srcOpenBillButton.setText("Src Open Bill-F5");
        srcOpenBillButton.setEnabled(false);
        srcOpenBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srcOpenBillButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        hiddenCommandPanel.add(srcOpenBillButton, gridBagConstraints);

        editTableButton.setText("Edit Table -F3");
        editTableButton.setEnabled(false);
        editTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTableButtonActionPerformed(evt);
            }
        });

        itemUpdateButton.setText("Update ");
        itemUpdateButton.setNextFocusableComponent(itemRemoveButton);
        itemUpdateButton.setEnabled(false);
        itemUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemUpdateButtonActionPerformed(evt);
            }
        });

        itemRemoveButton.setText("Remove ");
        itemRemoveButton.setNextFocusableComponent(saleDetailTable);
        itemRemoveButton.setEnabled(false);
        itemRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRemoveButtonActionPerformed(evt);
            }
        });

        cancelEditTableButton.setText("Cancel");
        cancelEditTableButton.setNextFocusableComponent(itemRemoveButton);
        cancelEditTableButton.setEnabled(false);
        cancelEditTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelEditTableButtonActionPerformed(evt);
            }
        });

        notesLabel19.setText("Notes");
        notesTextField.setColumns(5);
        notesTextField.setEditable(false);
        notesTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notesTextFieldFocusGained(evt);
            }
        });

        saleCommandPanel.setLayout(new java.awt.GridBagLayout());

        saleCommandPanel.setAutoscrolls(true);
        itemAddButton.setText("New Item-F2");
        itemAddButton.setBorderPainted(false);
        itemAddButton.setNextFocusableComponent(discTransTextField2);
        itemAddButton.setEnabled(false);
        itemAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAddButtonActionPerformed(evt);
            }
        });
        itemAddButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemAddButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        saleCommandPanel.add(itemAddButton, gridBagConstraints);

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Invoicing");
        setModal(true);
        setName("cashSaleDialog");
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

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(967, 130));
        saleTypePanel.setLayout(new java.awt.GridBagLayout());

        saleTypePanel.setBorder(new javax.swing.border.TitledBorder(""));
        saleTypePanel.setAutoscrolls(true);
        jLabel1.setText("Sale Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(jLabel1, gridBagConstraints);

        saleTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Invoice", "Invoice Return", "Pending Order", "Pay Pending Order", "Gift", "Credit Payment"}));
        saleTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saleTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(saleTypeComboBox, gridBagConstraints);

        salesNameLabel.setText("Sales Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(salesNameLabel, gridBagConstraints);

        salesNameTextField.setColumns(5);
        salesNameTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        salesNameTextField.setEnabled(false);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(salesNameTextField, gridBagConstraints);

        returInvTextField.setColumns(10);
        returInvTextField.setEditable(false);
        returInvTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returInvTextFieldActionPerformed(evt);
            }
        });
        returInvTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                returInvTextFieldFocusGained(evt);
            }
        });
        returInvTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                returInvTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(returInvTextField, gridBagConstraints);

        jLabel22.setText("Reference");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(jLabel22, gridBagConstraints);

        invoiceNumberTextField.setColumns(10);
        invoiceNumberTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        saleTypePanel.add(invoiceNumberTextField, gridBagConstraints);

        jLabel8.setText("Invoice Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        saleTypePanel.add(jLabel8, gridBagConstraints);

        priceCurrLabel.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(priceCurrLabel, gridBagConstraints);

        priceCurrComboBox.setNextFocusableComponent(salesNameTextField);
        priceCurrComboBox.setEnabled(false);
        priceCurrComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                priceCurrComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        saleTypePanel.add(priceCurrComboBox, gridBagConstraints);

        locationComboBox.setNextFocusableComponent(salesNameTextField);
        locationComboBox.setEnabled(false);
        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });
        locationComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                locationComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        saleTypePanel.add(locationComboBox, gridBagConstraints);

        locationLabel.setText("Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        saleTypePanel.add(locationLabel, gridBagConstraints);

        jPanel1.add(saleTypePanel, java.awt.BorderLayout.WEST);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(180, 211, 222));
        jPanel2.setBorder(new javax.swing.border.TitledBorder(""));
        totalValueCurTypeLabel.setFont(new java.awt.Font("MS Sans Serif", 1, 36));
        totalValueCurTypeLabel.setText("Rp.");
        jPanel2.add(totalValueCurTypeLabel, java.awt.BorderLayout.WEST);

        totalValueLabel.setBackground(new java.awt.Color(180, 211, 222));
        totalValueLabel.setEditable(false);
        totalValueLabel.setFont(new java.awt.Font("MS Sans Serif", 1, 36));
        totalValueLabel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalValueLabel.setText("000.00");
        jPanel2.add(totalValueLabel, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jPanel1, gridBagConstraints);

        itemListPanel.setLayout(new java.awt.BorderLayout());

        itemListPanel.setAutoscrolls(true);
        itemInfoPanel.setLayout(new java.awt.BorderLayout());

        itemInfoPanel.setAutoscrolls(true);
        memberOpenPanel.setLayout(new java.awt.BorderLayout());

        memberGuestPanel.setLayout(new java.awt.GridBagLayout());

        memberShipPanel.setLayout(new java.awt.GridBagLayout());

        memberShipPanel.setBorder(new javax.swing.border.TitledBorder(""));
        custTypeLabel3.setText("Customer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        memberShipPanel.add(custTypeLabel3, gridBagConstraints);

        custTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Non Member", "Member"}));
        custTypeComboBox.setEnabled(false);
        custTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                custTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        memberShipPanel.add(custTypeComboBox, gridBagConstraints);

        membCodeLabel5.setText("Member Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        memberShipPanel.add(membCodeLabel5, gridBagConstraints);

        memberCodeTextField.setColumns(10);
        memberCodeTextField.setEditable(false);
        memberCodeTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        memberShipPanel.add(memberCodeTextField, gridBagConstraints);

        membNameLabel4.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        memberShipPanel.add(membNameLabel4, gridBagConstraints);

        memberNameTextField.setColumns(22);
        memberNameTextField.setEditable(false);
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        memberShipPanel.add(memberNameTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        memberGuestPanel.add(memberShipPanel, gridBagConstraints);

        guestSearchPanel.setLayout(new java.awt.GridBagLayout());

        guestSearchPanel.setBorder(new javax.swing.border.TitledBorder(""));
        guestTypeLabel.setText("Guest Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(guestTypeLabel, gridBagConstraints);

        guestTextField.setColumns(12);
        guestTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestTextFieldActionPerformed(evt);
            }
        });
        guestTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                guestTextFieldFocusGained(evt);
            }
        });
        guestTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                guestTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(guestTextField, gridBagConstraints);

        roomNumberLabel.setText("Room Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(roomNumberLabel, gridBagConstraints);

        roomNumberTextField.setColumns(5);
        roomNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomNumberTextFieldActionPerformed(evt);
            }
        });
        roomNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                roomNumberTextFieldFocusGained(evt);
            }
        });
        roomNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roomNumberTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(roomNumberTextField, gridBagConstraints);

        resNumberLabel.setText("Resv Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(resNumberLabel, gridBagConstraints);

        resNumberTextField.setColumns(9);
        resNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resNumberTextFieldActionPerformed(evt);
            }
        });
        resNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                resNumberTextFieldFocusGained(evt);
            }
        });
        resNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resNumberTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(resNumberTextField, gridBagConstraints);

        guestTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Inside Guest", "Outside Guest"}));
        guestTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                guestTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(guestTypeComboBox, gridBagConstraints);

        guestNameLabel.setText("Guest Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        guestSearchPanel.add(guestNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        memberGuestPanel.add(guestSearchPanel, gridBagConstraints);

        memberOpenPanel.add(memberGuestPanel, java.awt.BorderLayout.WEST);

        openBillPanel.setLayout(new java.awt.GridBagLayout());

        openBillPanel.setBorder(new javax.swing.border.TitledBorder(""));
        coverLabel.setText("Cover");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        openBillPanel.add(coverLabel, gridBagConstraints);

        coverTextField.setColumns(15);
        coverTextField.setEditable(false);
        coverTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        coverTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverTextFieldActionPerformed(evt);
            }
        });
        coverTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coverTextFieldFocusGained(evt);
            }
        });
        coverTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                coverTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        openBillPanel.add(coverTextField, gridBagConstraints);

        coverGuestNameLabel.setText("Cover Guest Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        openBillPanel.add(coverGuestNameLabel, gridBagConstraints);

        coverGuestNameTextField.setColumns(25);
        coverGuestNameTextField.setEditable(false);
        coverGuestNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverGuestNameTextFieldActionPerformed(evt);
            }
        });
        coverGuestNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coverGuestNameTextFieldFocusGained(evt);
            }
        });
        coverGuestNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                coverGuestNameTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        openBillPanel.add(coverGuestNameTextField, gridBagConstraints);

        memberOpenPanel.add(openBillPanel, java.awt.BorderLayout.CENTER);

        itemInfoPanel.add(memberOpenPanel, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(new javax.swing.border.TitledBorder(""));
        jLabel7.setText("Item Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel7, gridBagConstraints);

        itemNameTextField.setColumns(30);
        itemNameTextField.setEditable(false);
        itemNameTextField.setNextFocusableComponent(itemPriceTextField);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(itemNameTextField, gridBagConstraints);

        jLabel9.setText("Serial Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel9, gridBagConstraints);

        itemCodeTextField.setColumns(12);
        itemCodeTextField.setEditable(false);
        itemCodeTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemCodeTextField.setNextFocusableComponent(itemNameTextField);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(itemCodeTextField, gridBagConstraints);

        jLabel10.setText("Disc Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel10, gridBagConstraints);

        itemDiscTextField.setColumns(5);
        itemDiscTextField.setEditable(false);
        itemDiscTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemDiscTextField.setNextFocusableComponent(itemQtyTextField);
        itemDiscTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDiscTextFieldActionPerformed(evt);
            }
        });
        itemDiscTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemDiscTextFieldFocusGained(evt);
            }
        });
        itemDiscTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemDiscTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(itemDiscTextField, gridBagConstraints);

        jLabel11.setText("Quantity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel11, gridBagConstraints);

        itemQtyTextField.setColumns(5);
        itemQtyTextField.setEditable(false);
        itemQtyTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemQtyTextField.setNextFocusableComponent(itemCodeTextField);
        itemQtyTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemQtyTextFieldActionPerformed(evt);
            }
        });
        itemQtyTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemQtyTextFieldFocusGained(evt);
            }
        });
        itemQtyTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemQtyTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(itemQtyTextField, gridBagConstraints);

        jLabel21.setText("Price");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel21, gridBagConstraints);

        itemPriceTextField.setColumns(10);
        itemPriceTextField.setEditable(false);
        itemPriceTextField.setNextFocusableComponent(discTypeComboBox);
        itemPriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemPriceTextFieldActionPerformed(evt);
            }
        });
        itemPriceTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemPriceTextFieldFocusGained(evt);
            }
        });
        itemPriceTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemPriceTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(itemPriceTextField, gridBagConstraints);

        discTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"%", "Val"}));
        discTypeComboBox.setNextFocusableComponent(itemDiscTextField);
        discTypeComboBox.setEnabled(false);
        discTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discTypeComboBoxKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(discTypeComboBox, gridBagConstraints);

        jLabel15.setText("Disc Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel15, gridBagConstraints);

        serialNumTextField.setColumns(10);
        serialNumTextField.setEditable(false);
        serialNumTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialNumTextFieldActionPerformed(evt);
            }
        });
        serialNumTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                serialNumTextFieldFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                serialNumTextFieldFocusLost(evt);
            }
        });
        serialNumTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                serialNumTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(serialNumTextField, gridBagConstraints);

        jLabel26.setText("Item Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(jLabel26, gridBagConstraints);

        discTypeLabel.setText("(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel4.add(discTypeLabel, gridBagConstraints);

        itemInfoPanel.add(jPanel4, java.awt.BorderLayout.WEST);

        itemListPanel.add(itemInfoPanel, java.awt.BorderLayout.NORTH);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 200));
        jScrollPane2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane2KeyPressed(evt);
            }
        });

        saleDetailTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                    "No", "Item Code", "Item Name", "Serial Number", "Price", "Disc", "Qty", "Total"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        saleDetailTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saleDetailTableKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                saleDetailTableKeyTyped(evt);
            }
        });

        jScrollPane2.setViewportView(saleDetailTable);

        jPanel7.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        itemListPanel.add(jPanel7, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.ipady = 180;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(itemListPanel, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        paymentDetailPanel.setLayout(new java.awt.GridBagLayout());

        paymentDetailPanel.setBorder(new javax.swing.border.TitledBorder(""));
        paymentDetailPanel.setAutoscrolls(true);
        jLabel12.setText("Total Trans");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel12, gridBagConstraints);

        jLabel13.setText("Cur Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Other Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel14, gridBagConstraints);

        jLabel16.setText("DP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel16, gridBagConstraints);

        jLabel17.setText("Change");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel17, gridBagConstraints);

        totalTransTextField.setColumns(15);
        totalTransTextField.setEditable(false);
        totalTransTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(totalTransTextField, gridBagConstraints);

        otherCostTextField.setColumns(15);
        otherCostTextField.setEditable(false);
        otherCostTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(otherCostTextField, gridBagConstraints);

        lastPaymentTransTextField.setColumns(15);
        lastPaymentTransTextField.setEditable(false);
        lastPaymentTransTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(lastPaymentTransTextField, gridBagConstraints);

        lastDiscTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Disc(%)", "Disc(value)"}));
        lastDiscTypeComboBox.setNextFocusableComponent(discTransTextField2);
        lastDiscTypeComboBox.setEnabled(false);
        lastDiscTypeComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lastDiscTypeComboBoxFocusGained(evt);
            }
        });
        lastDiscTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lastDiscTypeComboBoxKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                lastDiscTypeComboBoxKeyTyped(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(lastDiscTypeComboBox, gridBagConstraints);

        discTransTextField2.setColumns(15);
        discTransTextField2.setEditable(false);
        discTransTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discTransTextField2.setNextFocusableComponent(taxTypeComboBox);
        discTransTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discTransTextField2ActionPerformed(evt);
            }
        });
        discTransTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                discTransTextField2FocusGained(evt);
            }
        });
        discTransTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discTransTextField2KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(discTransTextField2, gridBagConstraints);

        taxTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Tax (%)", "Tax (Value)"}));
        taxTypeComboBox.setEnabled(false);
        taxTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                taxTypeComboBoxKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                taxTypeComboBoxKeyTyped(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(taxTypeComboBox, gridBagConstraints);

        incTaxlTransTextField.setColumns(15);
        incTaxlTransTextField.setEditable(false);
        incTaxlTransTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        incTaxlTransTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incTaxlTransTextFieldActionPerformed(evt);
            }
        });
        incTaxlTransTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                incTaxlTransTextFieldFocusGained(evt);
            }
        });
        incTaxlTransTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                incTaxlTransTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(incTaxlTransTextField, gridBagConstraints);

        servTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Svc (%)", "Svc (Value)"}));
        servTypeComboBox.setEnabled(false);
        servTypeComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                servTypeComboBoxKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                servTypeComboBoxKeyTyped(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(servTypeComboBox, gridBagConstraints);

        incServlTransTextField.setColumns(15);
        incServlTransTextField.setEditable(false);
        incServlTransTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        incServlTransTextField.setNextFocusableComponent(curTypeComboBox);
        incServlTransTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incServlTransTextFieldActionPerformed(evt);
            }
        });
        incServlTransTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                incServlTransTextFieldFocusGained(evt);
            }
        });
        incServlTransTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                incServlTransTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(incServlTransTextField, gridBagConstraints);

        curTypeComboBox.setNextFocusableComponent(paymentTransTextField);
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        paymentDetailPanel.add(curTypeComboBox, gridBagConstraints);

        paymentTransTextField.setColumns(15);
        paymentTransTextField.setEditable(false);
        paymentTransTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paymentTransTextField.setNextFocusableComponent(changeTransTextField);
        paymentTransTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentTransTextFieldActionPerformed(evt);
            }
        });
        paymentTransTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                paymentTransTextFieldFocusGained(evt);
            }
        });
        paymentTransTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paymentTransTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(paymentTransTextField, gridBagConstraints);

        changeTransTextField.setColumns(15);
        changeTransTextField.setEditable(false);
        changeTransTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        changeTransTextField.setNextFocusableComponent(printCashButton);
        changeTransTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTransTextFieldActionPerformed(evt);
            }
        });
        changeTransTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                changeTransTextFieldFocusGained(evt);
            }
        });
        changeTransTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                changeTransTextFieldKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(changeTransTextField, gridBagConstraints);

        jLabel20.setText("Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel20, gridBagConstraints);

        jLabel28.setText("Poin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(jLabel28, gridBagConstraints);

        poinTextField.setColumns(15);
        poinTextField.setEditable(false);
        poinTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        poinTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                poinTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        paymentDetailPanel.add(poinTextField, gridBagConstraints);

        currencyRateTextField.setEditable(false);
        currencyRateTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 70;
        paymentDetailPanel.add(currencyRateTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel6.add(paymentDetailPanel, gridBagConstraints);

        saleShortCutPanel.setLayout(new java.awt.GridBagLayout());

        saleShortCutPanel.setBorder(new javax.swing.border.TitledBorder(""));
        labelCommandPanel.setLayout(new java.awt.GridBagLayout());

        newSaleLabel.setText("F5  -  New Sale       ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(newSaleLabel, gridBagConstraints);

        totalSaleLabel.setText("F1  -  Total Sales        ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(totalSaleLabel, gridBagConstraints);

        otherCostLabel.setText("F7  -  Other Cost ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(otherCostLabel, gridBagConstraints);

        contactInputLabel.setText("F2  -  Contact Edit    ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(contactInputLabel, gridBagConstraints);

        endSaleLabel.setText("F9  -  Do Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(endSaleLabel, gridBagConstraints);

        reprintLabel.setText("F3  -  Reprint Invoice");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(reprintLabel, gridBagConstraints);

        paddingLabel1.setForeground(java.awt.SystemColor.activeCaptionBorder);
        paddingLabel1.setText("F7  -  Other Cost       ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(paddingLabel1, gridBagConstraints);

        paddingLabel2.setForeground(new java.awt.Color(212, 208, 200));
        paddingLabel2.setText("F2  -  Contact Edit       ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        labelCommandPanel.add(paddingLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        saleShortCutPanel.add(labelCommandPanel, gridBagConstraints);

        buttonCommandPanel.setLayout(new java.awt.GridBagLayout());

        editItemButton.setText("Edit Invoice Item - F4");
        editItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemButtonActionPerformed(evt);
            }
        });
        editItemButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editItemButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        buttonCommandPanel.add(editItemButton, gridBagConstraints);

        multiPaymentButton.setText("Multi Payments - F11");
        multiPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiPaymentButtonActionPerformed(evt);
            }
        });
        multiPaymentButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                multiPaymentButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        buttonCommandPanel.add(multiPaymentButton, gridBagConstraints);

        saveAsCreditButton.setText("Save As Credit - F6");
        saveAsCreditButton.setPreferredSize(new java.awt.Dimension(130, 23));
        saveAsCreditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsCreditButtonActionPerformed(evt);
            }
        });
        saveAsCreditButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveAsCreditButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        buttonCommandPanel.add(saveAsCreditButton, gridBagConstraints);

        printCashButton.setText("Print - F12");
        printCashButton.setPreferredSize(new java.awt.Dimension(130, 23));
        printCashButton.setEnabled(false);
        printCashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printCashButtonActionPerformed(evt);
            }
        });
        printCashButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                printCashButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        buttonCommandPanel.add(printCashButton, gridBagConstraints);

        resetPaymentButton.setText("Reset Payments - F8");
        resetPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPaymentButtonActionPerformed(evt);
            }
        });
        resetPaymentButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resetPaymentButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        buttonCommandPanel.add(resetPaymentButton, gridBagConstraints);

        closeSaleButton.setMnemonic('X');
        closeSaleButton.setText("Close Sales - Altl+X");
        closeSaleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeSaleButton.setPreferredSize(new java.awt.Dimension(130, 23));
        closeSaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeSaleButtonActionPerformed(evt);
            }
        });
        closeSaleButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closeSaleButtonKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        buttonCommandPanel.add(closeSaleButton, gridBagConstraints);

        saveAsOpenBillButton.setText("Save Open Bill-F6");
        saveAsOpenBillButton.setPreferredSize(new java.awt.Dimension(130, 23));
        saveAsOpenBillButton.setEnabled(false);
        saveAsOpenBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsOpenBillButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        buttonCommandPanel.add(saveAsOpenBillButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        saleShortCutPanel.add(buttonCommandPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel6.add(saleShortCutPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jPanel6, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void locationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationComboBoxActionPerformed
        try {
            String code = (String) locationComboBox.getSelectedItem();
            this.returInvTextField.setText((String) CashSaleController.getHashLocationName().get(code));
        } catch (Exception e) {
            System.out.println("err on selectLocation = " + e.toString());
        }
    }//GEN-LAST:event_locationComboBoxActionPerformed

    private void locationComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_locationComboBoxKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                try {
                    String code = (String) locationComboBox.getSelectedItem();
                    int cashierNo = CashierMainApp.getCashMaster().getCashierNumber();
                    long locationId = Long.parseLong((String) CashSaleController.getHashLocationByCode().get(code));
                    this.getSaleModel().getMainSale().setLocationId(locationId);
                    CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId = locationId + "";
                    this.getSaleModel().getMainSale().setInvoiceCounter(PstBillMain.getCounterTransaction(locationId, cashierNo, PstBillMain.TYPE_INVOICE));
                    this.getSaleModel().getMainSale().setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(), locationId, cashierNo, PstBillMain.TYPE_INVOICE));
                    this.invoiceNumberTextField.setText(this.getSaleModel().getMainSale().getInvoiceNumber());
                    this.returInvTextField.setText((String) CashSaleController.getHashLocationName().get(code));
                } catch (Exception e) {
                    System.out.println("err on selectLocation = " + e.toString());
                }
                this.cmdSelectPriceCurr();
                break;
            default:
                this.getGlobalKeyListener(evt);
                break;
        }
    }//GEN-LAST:event_locationComboBoxKeyPressed

    private void changeTransTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_changeTransTextFieldFocusGained
        changeTransTextField.selectAll();
    }//GEN-LAST:event_changeTransTextFieldFocusGained

    private void priceCurrComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceCurrComboBoxKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                try {
                    String code = (String) priceCurrComboBox.getSelectedItem();
                    CurrencyType currType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(code);
                    this.getSaleModel().setRateUsed(CashSaleController.getLatestRate(String.valueOf(currType.getOID())));
                    this.getSaleModel().setCurrencyTypeUsed(currType);
                    this.getSaleModel().getMainSale().setCurrencyId(currType.getOID());
                    this.getSaleModel().getMainSale().setRate(this.getSaleModel().getRateUsed().getSellingRate());
                    this.totalValueCurTypeLabel.setText(code);
                } catch (Exception e) {
                    System.out.println("err on selectPriceCurr = " + e.toString());
                }
                this.cmdSelectSales();
                break;
            default:
                this.getGlobalKeyListener(evt);
                break;
        }
    }//GEN-LAST:event_priceCurrComboBoxKeyPressed

    private void itemPriceTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemPriceTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemPriceTextFieldKeyPressed

    private void itemPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPriceTextFieldActionPerformed
        if (Validator.isFloat(itemPriceTextField.getText())) {
            double newPrice = CashierMainApp.getDoubleFromFormated(itemPriceTextField.getText());
            if (newPrice > 0 && newPrice != (this.getCandidateBilldetail().getItemPrice() / this.getSaleModel().getRateUsed().getSellingRate())) {
                CashSaleController.setSupervisorApprove(false);
                CashSaleController.showSupervisorLoginDialog();
                if (CashSaleController.isSupervisorApprove()) {
                    if (newPrice < (this.getCandidateBilldetail().getItemPrice() / this.getSaleModel().getRateUsed().getSellingRate())) {
                        JOptionPane.showMessageDialog(this, "Price lower than default", "Input Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    this.getCandidateBilldetail().setItemPrice(newPrice * this.getSaleModel().getRateUsed().getSellingRate());
                    discTypeComboBox.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(this, "Must Approve By Supervisor", "Input Warning", JOptionPane.WARNING_MESSAGE);
                    itemPriceTextField.requestFocusInWindow();
                }
            } else {
                discTypeComboBox.requestFocusInWindow();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Price must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            itemPriceTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_itemPriceTextFieldActionPerformed

    private void coverTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_coverTextFieldFocusGained
        coverTextField.selectAll();
    }//GEN-LAST:event_coverTextFieldFocusGained

    private void coverGuestNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_coverGuestNameTextFieldFocusGained
        coverGuestNameTextField.selectAll();
    }//GEN-LAST:event_coverGuestNameTextFieldFocusGained

    private void coverTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coverTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_coverTextFieldKeyPressed

    private void coverGuestNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coverGuestNameTextFieldKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_coverGuestNameTextFieldKeyPressed

    private void coverTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverTextFieldActionPerformed
        String stTemp = coverTextField.getText();
        if (stTemp.length() > 0) {
            this.getSaleModel().getMainSale().setCoverNumber(stTemp);
            this.setOkOtherCost(true);
            itemCodeTextField.setEditable(true);
            itemCodeTextField.requestFocusInWindow();
        } else {
            JOptionPane.showMessageDialog(this, "Cover number can't be empty", "Input Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_coverTextFieldActionPerformed

    private void coverGuestNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverGuestNameTextFieldActionPerformed
        String stTemp = coverGuestNameTextField.getText();
        if (stTemp.length() > 0) {
            this.getSaleModel().getMainSale().setGuestName(stTemp);
            coverTextField.setEditable(true);
            coverTextField.requestFocusInWindow();
        } else {
            JOptionPane.showMessageDialog(this, "Guest name can't be empty", "Input Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_coverGuestNameTextFieldActionPerformed

    private void lastDiscTypeComboBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lastDiscTypeComboBoxFocusGained
        multiPaymentButton.setEnabled(true);
    }//GEN-LAST:event_lastDiscTypeComboBoxFocusGained

    private void closeSaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeSaleButtonActionPerformed
        this.cmdCloseWindows();
    }//GEN-LAST:event_closeSaleButtonActionPerformed

    private void closeSaleButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_closeSaleButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_closeSaleButtonKeyPressed

    private void multiPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiPaymentButtonActionPerformed
        cmdSetPayment();
    }//GEN-LAST:event_multiPaymentButtonActionPerformed

    private void resetPaymentButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resetPaymentButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_resetPaymentButtonKeyPressed

    private void resetPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPaymentButtonActionPerformed
        cmdResetPayments();
    }//GEN-LAST:event_resetPaymentButtonActionPerformed

    private void editItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemButtonActionPerformed
        cmdEditSaleTable();
    }//GEN-LAST:event_editItemButtonActionPerformed

    private void guestTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_guestTypeComboBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmdChooseGuestType();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_guestTypeComboBoxKeyPressed

    private void poinTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_poinTextFieldActionPerformed

    }//GEN-LAST:event_poinTextFieldActionPerformed

    private void resNumberTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_resNumberTextFieldKeyPressed
    {//GEN-HEADEREND:event_resNumberTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdGuestSearch();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            guestTextField.setText("");
            roomNumberTextField.setText("");
            resNumberTextField.setText("");
            guestTextField.setEditable(false);
            roomNumberTextField.setEditable(false);
            resNumberTextField.setEditable(false);
            guestTypeComboBox.requestFocusInWindow();
        } else {
            getGlobalKeyListener(evt);
        }

    }//GEN-LAST:event_resNumberTextFieldKeyPressed

    private void resNumberTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_resNumberTextFieldFocusGained
    {//GEN-HEADEREND:event_resNumberTextFieldFocusGained
        resNumberTextField.selectAll();
    }//GEN-LAST:event_resNumberTextFieldFocusGained

    private void resNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resNumberTextFieldActionPerformed
    {//GEN-HEADEREND:event_resNumberTextFieldActionPerformed
        cmdGuestSearch();
    }//GEN-LAST:event_resNumberTextFieldActionPerformed

    private void roomNumberTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_roomNumberTextFieldKeyPressed
    {//GEN-HEADEREND:event_roomNumberTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdGuestSearch();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            guestTextField.setText("");
            roomNumberTextField.setText("");
            resNumberTextField.setText("");
            guestTextField.setEditable(false);
            roomNumberTextField.setEditable(false);
            resNumberTextField.setEditable(false);
            guestTypeComboBox.requestFocusInWindow();
        } else {
            getGlobalKeyListener(evt);
        }

    }//GEN-LAST:event_roomNumberTextFieldKeyPressed

    private void roomNumberTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_roomNumberTextFieldFocusGained
    {//GEN-HEADEREND:event_roomNumberTextFieldFocusGained
        roomNumberTextField.selectAll();
    }//GEN-LAST:event_roomNumberTextFieldFocusGained

    private void roomNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_roomNumberTextFieldActionPerformed
    {//GEN-HEADEREND:event_roomNumberTextFieldActionPerformed
        if (roomNumberTextField.getText().length() == 0) {
            resNumberTextField.setEditable(true);
            resNumberTextField.requestFocusInWindow();
        } else {
            cmdGuestSearch();
        }
    }//GEN-LAST:event_roomNumberTextFieldActionPerformed

    private void guestTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_guestTextFieldFocusGained
    {//GEN-HEADEREND:event_guestTextFieldFocusGained

        guestTextField.selectAll();
    }//GEN-LAST:event_guestTextFieldFocusGained

    private void guestTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_guestTextFieldKeyPressed
    {//GEN-HEADEREND:event_guestTextFieldKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdGuestSearch();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            guestTextField.setText("");
            roomNumberTextField.setText("");
            resNumberTextField.setText("");
            guestTextField.setEditable(false);
            roomNumberTextField.setEditable(false);
            resNumberTextField.setEditable(false);
            guestTypeComboBox.requestFocusInWindow();

        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_guestTextFieldKeyPressed

    private void guestTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_guestTextFieldActionPerformed
    {//GEN-HEADEREND:event_guestTextFieldActionPerformed
        if (guestTextField.getText().length() == 0) {
            roomNumberTextField.setEditable(true);
            roomNumberTextField.requestFocusInWindow();
        } else {
            cmdGuestSearch();
        }
    }//GEN-LAST:event_guestTextFieldActionPerformed

    private void printCashButtonKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_printCashButtonKeyPressed
    {//GEN-HEADEREND:event_printCashButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_printCashButtonKeyPressed

    private void multiPaymentButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_multiPaymentButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_multiPaymentButtonKeyPressed

    private void saveAsCreditButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveAsCreditButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_saveAsCreditButtonKeyPressed

    private void editItemButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editItemButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_editItemButtonKeyPressed

    private void itemAddButtonKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemAddButtonKeyPressed
    {//GEN-HEADEREND:event_itemAddButtonKeyPressed
        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemAddButtonKeyPressed

    private void saveAsCreditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsCreditButtonActionPerformed
        cmdSaveCreditSales();
    }//GEN-LAST:event_saveAsCreditButtonActionPerformed

    public void cmdReprintInvoice() {
        if (this.cmdNewSales()) {
            CashSaleController.setInvoiceChoosen(null);
            CashSaleController.showInvoiceSearch(this, "");
            Vector invoiceChoosen = CashSaleController.getInvoiceChoosen();
            if (invoiceChoosen != null || invoiceChoosen.size() > 0) {
                BillMain billmain = (BillMain) invoiceChoosen.get(0);
                DefaultSaleModel saleModel = SessTransactionData.getData(billmain);
                this.setSaleModel(saleModel);
                //this.getSaleModel().countLastPayment();
                synchronizeModelAndTable();

                this.setProcessType(TRANSACTION_REPRINTING);
                editItemButton.setEnabled(false);
                saveAsCreditButton.setEnabled(false);
                printCashButton.setEnabled(true);
                printCashButton.requestFocusInWindow();
            }
        }

    }

    public void cmdHalfInvoice() {
        if (this.cmdNewSales()) {
            CashSaleController.setInvoiceChoosen(null);
            CashSaleController.showInvoiceSearch(this, "");
            Vector invoiceChoosen = CashSaleController.getInvoiceChoosen();
            if (invoiceChoosen != null || invoiceChoosen.size() > 0) {
                String sInput = null;
                while (sInput == null || !CashierMainApp.validateInput(sInput)) {
                    sInput = JOptionPane.showInputDialog(this, "Please Input Half Percentage", CashierMainApp.setFormatNumber(50));
                }

                double pct = CashierMainApp.getDoubleFromFormated(sInput);
                this.setPctHalfInvoice(pct);

                BillMain billmain = (BillMain) invoiceChoosen.get(0);
                DefaultSaleModel saleModel = SessTransactionData.getDataHalfInvoice(billmain, pct);
                this.setSaleModel(saleModel);
                synchronizeModelAndTable();

                this.setProcessType(TRANSACTION_HALF_INVOICE);
                editItemButton.setEnabled(false);
                saveAsCreditButton.setEnabled(false);
                printCashButton.setEnabled(true);
                printCashButton.requestFocusInWindow();
            }
        }

    }


    private void formKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_formKeyPressed
    {//GEN-HEADEREND:event_formKeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_formKeyPressed

    private void changeTransTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_changeTransTextFieldKeyPressed
    {//GEN-HEADEREND:event_changeTransTextFieldKeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_changeTransTextFieldKeyPressed

    private void incServlTransTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_incServlTransTextFieldKeyPressed
    {//GEN-HEADEREND:event_incServlTransTextFieldKeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_incServlTransTextFieldKeyPressed

    private void incTaxlTransTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_incTaxlTransTextFieldKeyPressed
    {//GEN-HEADEREND:event_incTaxlTransTextFieldKeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_incTaxlTransTextFieldKeyPressed

    private void discTransTextField2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_discTransTextField2KeyPressed
    {//GEN-HEADEREND:event_discTransTextField2KeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_discTransTextField2KeyPressed

    private void itemDiscTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemDiscTextFieldKeyPressed
    {//GEN-HEADEREND:event_itemDiscTextFieldKeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_itemDiscTextFieldKeyPressed

    private void salesNameTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_salesNameTextFieldKeyPressed
    {//GEN-HEADEREND:event_salesNameTextFieldKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdSearchSalesPerson();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_salesNameTextFieldKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowOpened
    {//GEN-HEADEREND:event_formWindowOpened


        cmdChooseTransType();
    }//GEN-LAST:event_formWindowOpened

    public void cmdChooseTransType() {
        saleTypeComboBox.setEnabled(true);
        saleTypeComboBox.requestFocusInWindow();
    }

    private void curTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_curTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_curTypeComboBoxActionPerformed
        cmdCurrencyChange();
    }//GEN-LAST:event_curTypeComboBoxActionPerformed

    private double getCurrencyRate(String stCode) {

        double result = 0;
        CurrencyType objCurrType = new CurrencyType();
        StandartRate objRate = new StandartRate();
        objCurrType = (CurrencyType) CashierMainApp.getHashCurrencyType().get(stCode);
        //by widi
        //using standart rate, not daily rate
        //objRate = CashSaleController.getStandartRate(objCurrType.getOID()+"");
        objRate = CashSaleController.getLatestRate(objCurrType.getOID() + "");
        result = objRate.getSellingRate();
        return result;
    }

    /** toCurrency
     *  by wpulantara
     *  convert into selected currency format
     *  @return String
     */
    private String toCurrency(double dValue) {
        return CashierMainApp.getFrameHandler().userFormatStringDecimal(dValue);
    }

    private void cmdCurrencyChange() {
        currencyRateTextField.setText(toCurrency(getCurrencyRate((String) curTypeComboBox.getSelectedItem())));
    }

    private void changeTransTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_changeTransTextFieldActionPerformed
    {//GEN-HEADEREND:event_changeTransTextFieldActionPerformed
        if (Validator.isFloat(changeTransTextField.getText())) {
            double dChange = CashierMainApp.getDoubleFromFormated(changeTransTextField.getText());
            if (dChange > this.getSaleModel().getShouldChange()) {
                JOptionPane.showMessageDialog(this, "Change amount larger than it should be", "Input Warning", JOptionPane.WARNING_MESSAGE);
                changeTransTextField.requestFocusInWindow();
            } else {
                cmdCountReturn();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pay amount must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            changeTransTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_changeTransTextFieldActionPerformed

    private void editTableButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editTableButtonActionPerformed
    {//GEN-HEADEREND:event_editTableButtonActionPerformed

        saleDetailTable.requestFocusInWindow();
        if (saleDetailTable.getRowCount() > 0) {
            saleDetailTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_editTableButtonActionPerformed

    private void doPaymentButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doPaymentButtonActionPerformed
    {//GEN-HEADEREND:event_doPaymentButtonActionPerformed
        cmdDoPayment();
    }//GEN-LAST:event_doPaymentButtonActionPerformed

    private void cmdDoPayment() {
        if (this.getSaleModel().isAnySaleDetail()) {
            lastDiscTypeComboBox.setEnabled(true);
            taxTypeComboBox.setEnabled(true);
            servTypeComboBox.setEnabled(true);
            lastDiscTypeComboBox.requestFocusInWindow();
            setPaymentButton.setEnabled(true);
            printCashButton.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Can't Do Payment, Incomplete Data", "Incomplete Data", JOptionPane.ERROR_MESSAGE);
            //cmdDoAddNewItems();
        }
    }

    private void srcOpenBillButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_srcOpenBillButtonActionPerformed
    {//GEN-HEADEREND:event_srcOpenBillButtonActionPerformed
        cmdSearchOpenBill();


    }//GEN-LAST:event_srcOpenBillButtonActionPerformed

    private void cmdSearchOpenBill() {

        CashSaleController.setOpenBillChoosen(null);
        CashSaleController.showOpenBillSearch(this, invoiceNumberTextField.getText());
        Vector vctOpenBill = CashSaleController.getOpenBillChoosen();
        if (vctOpenBill.size() == 1) {
            BillMain found = (BillMain) vctOpenBill.get(0);
            if (found.getTransactionStatus() == PstBillMain.TRANS_STATUS_CLOSE) {
                JOptionPane.showMessageDialog(this, "This transaction had been closed", "Closed Transaction", JOptionPane.ERROR_MESSAGE);
                saleTypeComboBox.requestFocusInWindow();
            } else {
                DefaultSaleModel sale = SessTransactionData.getData(found);
                this.setSaleModel(sale);
                this.setOkOtherCost(true);
                custTypeComboBox.setEnabled(false);
                itemCodeTextField.setEnabled(true);
                itemCodeTextField.setEditable(true);
                itemCodeTextField.requestFocusInWindow();
                saveAsOpenBillButton.setEnabled(false);
                if (CashierMainApp.isEnableCreditPayment()) {
                    saveAsOpenBillButton.setVisible(false);
                    saveAsCreditButton.setVisible(true);
                    saveAsCreditButton.setEnabled(true);
                }
                synchronizeModelAndTable();
            }

        }

    }

    private void saveAsOpenBillButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveAsOpenBillButtonActionPerformed
    {//GEN-HEADEREND:event_saveAsOpenBillButtonActionPerformed
        cmdSaveOpenBill();
    }//GEN-LAST:event_saveAsOpenBillButtonActionPerformed

    private void returInvTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_returInvTextFieldKeyPressed
    {//GEN-HEADEREND:event_returInvTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdInvoiceSearch();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_returInvTextFieldKeyPressed

    private void returInvTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_returInvTextFieldActionPerformed
    {//GEN-HEADEREND:event_returInvTextFieldActionPerformed
        cmdInvoiceSearch();

    }//GEN-LAST:event_returInvTextFieldActionPerformed

    private PendingOrder refPendingOrder;
    private int transferInvFrom;

    public static int TRANS_FROM_INVOICE = 0;
    public static int TRANS_FROM_PEND_ORDER = 1;
    public static int TRANS_FROM_CREDIT_SALES = 2;

    private void cmdSearchPendingOrder() {
        if (this.getSaleModel().isAnyInvoice()) {
            CashSaleController.setPendingOrderChoosen(null);
            CashSaleController.showPendingOrderSearch(null, null);
            Vector vctPendingOrder = CashSaleController.getPendingOrderChoosen();
            if (vctPendingOrder.size() == 1) {
                PendingOrder temp = (PendingOrder) vctPendingOrder.get(0);
                returInvTextField.setText(temp.getPoNumber());
                this.getSaleModel().transferFromPendingOrder(temp);
                synchronizeModelAndTable();
                this.setOkOtherCost(true);
                itemCodeTextField.setEditable(true);
                itemNameTextField.setEditable(true);
                itemCodeTextField.requestFocusInWindow();

            } else {
                //JOptionPane.showMessageDialog(this,"Pending order not found","Search item",JOptionPane.ERROR_MESSAGE);
                saleTypeComboBox.setSelectedItem("Pay Pending Order");
                saleTypeComboBox.requestFocusInWindow();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sales person has not been set", "Sales Person", JOptionPane.ERROR_MESSAGE);
            salesNameTextField.requestFocusInWindow();
        }
    }

    private void cmdSearchInvoiceItem() {
        double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
        String keyword = returInvTextField.getText();
        CashSaleController.setBillDetailChoosen(null);
        CashSaleController.setBillDetailCodeChoosen(null);
        CashSaleController.showInvoiceItemSearch(this, keyword);
        Vector detailChoosen = CashSaleController.getBillDetailChoosen();
        Vector detailCodeChoosen = CashSaleController.getBillDetailCodeChoosen();
        if (detailChoosen.size() >= 1) {
            Billdetail detail = (Billdetail) detailChoosen.get(0);
            Material mat = new Material();
            try {
                mat = PstMaterial.fetchExc(detail.getMaterialId());
            } catch (Exception dbe) {

            }
            itemCodeTextField.setText(mat.getSku());
            itemNameTextField.setText(mat.getName());
            if (detailCodeChoosen.size() >= 1) {
                BillDetailCode code = (BillDetailCode) detailCodeChoosen.get(0);
                serialNumTextField.setText(code.getStockCode());
                this.setCandidateBilldetailCode(code);
            }
            double disc = 0;
            if (detail.getDiscType() == PstBillDetail.TYPE_DISC_PCT) {
                disc = detail.getDiscPct();
                discTypeComboBox.setSelectedIndex(PstBillDetail.TYPE_DISC_PCT);
            } else {
                detail.setDisc(detail.getDisc() / (detail.getQty() > 0 ? detail.getQty() : 1));
                disc = detail.getDisc() / dblRateUsed;
                discTypeComboBox.setSelectedIndex(PstBillDetail.TYPE_DISC_VAL);
            }
            itemDiscTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(disc));
            itemQtyTextField.setText("1");
            this.setCandidateBilldetail(detail);
            //double rateUsed = detail.get
            //itemPriceTextField.setText(Formater.formatNumber(detail.getItemPrice(),CashierMainApp.getDSJ_CashierXML().getConfig(0).forcurrency));
            itemPriceTextField.setText(toCurrency(detail.getItemPrice() / dblRateUsed));
            itemQtyTextField.setEditable(true);
            itemQtyTextField.requestFocusInWindow();
            this.setICommand(Command.ADD);
        } else {
            JOptionPane.showMessageDialog(this, "Return invoice item not found", "Search item", JOptionPane.ERROR_MESSAGE);
            itemCodeTextField.requestFocusInWindow();
        }

    }

    private void paymentTransTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_paymentTransTextFieldFocusGained
    {//GEN-HEADEREND:event_paymentTransTextFieldFocusGained

        paymentTransTextField.selectAll();
    }//GEN-LAST:event_paymentTransTextFieldFocusGained

    private void itemQtyTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_itemQtyTextFieldFocusGained
    {//GEN-HEADEREND:event_itemQtyTextFieldFocusGained

        itemQtyTextField.selectAll();
    }//GEN-LAST:event_itemQtyTextFieldFocusGained

    private void itemPriceTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_itemPriceTextFieldFocusGained
    {//GEN-HEADEREND:event_itemPriceTextFieldFocusGained
        itemPriceTextField.selectAll();
    }//GEN-LAST:event_itemPriceTextFieldFocusGained

    private void itemDiscTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_itemDiscTextFieldFocusGained
    {//GEN-HEADEREND:event_itemDiscTextFieldFocusGained

        itemDiscTextField.selectAll();
    }//GEN-LAST:event_itemDiscTextFieldFocusGained

    private void serialNumTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_serialNumTextFieldFocusGained
    {//GEN-HEADEREND:event_serialNumTextFieldFocusGained

        serialNumTextField.selectAll();
    }//GEN-LAST:event_serialNumTextFieldFocusGained

    private void itemNameTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_itemNameTextFieldFocusGained
    {//GEN-HEADEREND:event_itemNameTextFieldFocusGained

        itemNameTextField.selectAll();
    }//GEN-LAST:event_itemNameTextFieldFocusGained

    private void itemCodeTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_itemCodeTextFieldFocusGained
    {//GEN-HEADEREND:event_itemCodeTextFieldFocusGained

        itemCodeTextField.selectAll();

    }//GEN-LAST:event_itemCodeTextFieldFocusGained

    private void notesTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_notesTextFieldFocusGained
    {//GEN-HEADEREND:event_notesTextFieldFocusGained

        notesTextField.selectAll();
    }//GEN-LAST:event_notesTextFieldFocusGained

    private void coverNoTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_coverNoTextFieldFocusGained
    {//GEN-HEADEREND:event_coverNoTextFieldFocusGained

        coverNoTextField.selectAll();
    }//GEN-LAST:event_coverNoTextFieldFocusGained

    private void personalDiscTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_personalDiscTextFieldFocusGained
    {//GEN-HEADEREND:event_personalDiscTextFieldFocusGained

        personalDiscTextField.selectAll();

    }//GEN-LAST:event_personalDiscTextFieldFocusGained

    private void memberCodeTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_memberCodeTextFieldFocusGained
    {//GEN-HEADEREND:event_memberCodeTextFieldFocusGained

        memberCodeTextField.selectAll();

    }//GEN-LAST:event_memberCodeTextFieldFocusGained

    private void memberNameTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_memberNameTextFieldFocusGained
    {//GEN-HEADEREND:event_memberNameTextFieldFocusGained

        memberNameTextField.selectAll();
    }//GEN-LAST:event_memberNameTextFieldFocusGained

    private void returInvTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_returInvTextFieldFocusGained
    {//GEN-HEADEREND:event_returInvTextFieldFocusGained

        returInvTextField.selectAll();
    }//GEN-LAST:event_returInvTextFieldFocusGained

    private void salesNameTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_salesNameTextFieldFocusGained
    {//GEN-HEADEREND:event_salesNameTextFieldFocusGained

        salesNameTextField.selectAll();
    }//GEN-LAST:event_salesNameTextFieldFocusGained


    private void itemQtyTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_itemQtyTextFieldKeyPressed
    {//GEN-HEADEREND:event_itemQtyTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cmdCancel();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_itemQtyTextFieldKeyPressed

    private void incServlTransTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_incServlTransTextFieldFocusGained
    {//GEN-HEADEREND:event_incServlTransTextFieldFocusGained

        incServlTransTextField.selectAll();
    }//GEN-LAST:event_incServlTransTextFieldFocusGained

    private void incTaxlTransTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_incTaxlTransTextFieldFocusGained
    {//GEN-HEADEREND:event_incTaxlTransTextFieldFocusGained

        incTaxlTransTextField.selectAll();
    }//GEN-LAST:event_incTaxlTransTextFieldFocusGained

    private void discTransTextField2FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_discTransTextField2FocusGained
    {//GEN-HEADEREND:event_discTransTextField2FocusGained

        discTransTextField2.selectAll();
    }//GEN-LAST:event_discTransTextField2FocusGained

    private void itemDiscTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_itemDiscTextFieldActionPerformed
    {//GEN-HEADEREND:event_itemDiscTextFieldActionPerformed

        if (Validator.isFloat(itemDiscTextField.getText())) {
            double dVal = CashierMainApp.getDoubleFromFormated(itemDiscTextField.getText());
            if (this.getCandidateBilldetail().getDiscType() == PstBillDetail.DISC_TYPE_VALUE) {
                double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
                this.getCandidateBilldetail().setDisc(dVal * dblRateUsed);
            } else {
                this.getCandidateBilldetail().setDiscPct(dVal);
            }
            itemQtyTextField.setEditable(true);
            itemQtyTextField.requestFocusInWindow();
        } else {
            JOptionPane.showMessageDialog(this, "Item Discount must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            itemDiscTextField.requestFocusInWindow();

        }
    }//GEN-LAST:event_itemDiscTextFieldActionPerformed

    public void cmdCountPayment(KeyEvent evt) {

        String selectedCur = (String) curTypeComboBox.getSelectedItem();
        double netT = this.getSaleModel().getNetTrans() - this.getSaleModel().getLastPayment();
        CurrencyType type = (CurrencyType) CashierMainApp.getHashCurrencyType().get(selectedCur);
        StandartRate rate = (StandartRate) CashSaleController.getLatestRate(String.valueOf(type.getOID()));
        StandartRate defaultRate = this.getSaleModel().getRateUsed();
        double iShouldPay = netT;
        //double resShould = 0;
        if (rate.getCurrencyTypeId() != defaultRate.getCurrencyTypeId()) {
            iShouldPay = netT / rate.getSellingRate();
            double resShould = netT % (rate.getSellingRate() * 100);
            if (resShould > 0 && rate.getSellingRate() > defaultRate.getSellingRate())
                iShouldPay = iShouldPay + 0.01;  //menghindari kerugian nilai tukar
        } else {
            iShouldPay = netT / defaultRate.getSellingRate();
        }
        if (netT < 0) {
            iShouldPay = 0;
            cmdCountReturn();
        }
        paymentTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(iShouldPay));
        paymentTransTextField.setEditable(true);
        paymentTransTextField.requestFocusInWindow();
        changeTransTextField.setEditable(true);
    }

    private void curTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_curTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_curTypeComboBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String selectedCur = (String) curTypeComboBox.getSelectedItem();
            cmdCountPayment(evt);
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_curTypeComboBoxKeyPressed

    private void paymentTransTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_paymentTransTextFieldKeyPressed
    {//GEN-HEADEREND:event_paymentTransTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdSetPayment();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_paymentTransTextFieldKeyPressed

    private void servTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_servTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_servTypeComboBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            servTypeComboBox.setEnabled(false);
            incServlTransTextField.setEditable(true);
            if (this.getSaleModel().getSvcAmount() > 0) {
                incServlTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(this.getSaleModel().getSvcAmount() / this.getSaleModel().getRateUsed().getSellingRate()));
            } else {
                incServlTransTextField.setText(CashierMainApp.getDSJ_CashierXML().getConfig(0).cashierService);
            }
            incServlTransTextField.requestFocusInWindow();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_servTypeComboBoxKeyPressed

    private void taxTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_taxTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_taxTypeComboBoxKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            taxTypeComboBox.setEnabled(false);
            incTaxlTransTextField.setEditable(true);
            if (this.getSaleModel().getTaxAmount() > 0) {
                incTaxlTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(this.getSaleModel().getTaxAmount() / this.getSaleModel().getRateUsed().getSellingRate()));
            } else {
                incTaxlTransTextField.setText(CashierMainApp.getDSJ_CashierXML().getConfig(0).cashierTax);
            }
            incTaxlTransTextField.requestFocusInWindow();

        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_taxTypeComboBoxKeyPressed

    private void lastDiscTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_lastDiscTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_lastDiscTypeComboBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.getSaleModel().getDiscAmount() > 0) {
                discTransTextField2.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(this.getSaleModel().getDiscAmount() / this.getSaleModel().getRateUsed().getSellingRate()));
            } else {
                discTransTextField2.setText("0");
            }
            lastDiscTypeComboBox.setEnabled(false);
            discTransTextField2.setEditable(true);
            discTransTextField2.requestFocusInWindow();
            resetPaymentButton.setEnabled(true);
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_lastDiscTypeComboBoxKeyPressed

    private void lastDiscTypeComboBoxKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_lastDiscTypeComboBoxKeyTyped
    {//GEN-HEADEREND:event_lastDiscTypeComboBoxKeyTyped

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            discTransTextField2.requestFocusInWindow();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_lastDiscTypeComboBoxKeyTyped

    private void cmdCountServTrans() {
        double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
        double val = CashierMainApp.getDoubleFromFormated(incServlTransTextField.getText());
        if (servTypeComboBox.getSelectedIndex() == SERV_STATUS_VALUE) {
            this.getSaleModel().getMainSale().setServiceValue(val * dblRateUsed);
            this.getSaleModel().getMainSale().setServicePct(0);
        } else if (servTypeComboBox.getSelectedIndex() == SERV_STATUS_PCT) {
            this.getSaleModel().getMainSale().setServicePct(val);
            this.getSaleModel().getMainSale().setServiceValue(0);
        }
        synchronizeModelAndTable();
    }

    private void incServlTransTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_incServlTransTextFieldActionPerformed
    {//GEN-HEADEREND:event_incServlTransTextFieldActionPerformed

        //this.
        if (Validator.isFloat(incServlTransTextField.getText())) {
            cmdCountServTrans();
            incServlTransTextField.setEditable(false);
            paymentTransTextField.setEditable(true);
            if (netTransPriority == SERVICE_FIRST && CashierMainApp.isEnableTax()) {
                taxTypeComboBox.setEnabled(true);
                taxTypeComboBox.requestFocusInWindow();
            } else {
                curTypeComboBox.setEditable(false);
                curTypeComboBox.setEnabled(true);
                curTypeComboBox.requestFocusInWindow();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Service amount must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            incServlTransTextField.requestFocusInWindow();
        }
        //paymentTransTextField.requestFocusInWindow ();
    }//GEN-LAST:event_incServlTransTextFieldActionPerformed

    public static final int TAX_TYPE_PCT = 0;
    public static final int TAX_TYPE_VALUE = 1;
    public static final int SERV_STATUS_PCT = 0;
    public static final int SERV_STATUS_VALUE = 1;

    private void cmdCountTaxTrans() {
        double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
        double val = CashierMainApp.getDoubleFromFormated(incTaxlTransTextField.getText());
        if (taxTypeComboBox.getSelectedIndex() == TAX_TYPE_VALUE) {
            this.getSaleModel().getMainSale().setTaxValue(val * dblRateUsed);
            this.getSaleModel().getMainSale().setTaxPercentage(0);

        } else if (taxTypeComboBox.getSelectedIndex() == TAX_TYPE_PCT) {
            this.getSaleModel().getMainSale().setTaxPercentage(val);
            this.getSaleModel().getMainSale().setTaxValue(0);
        }
        synchronizeModelAndTable();
    }

    private void incTaxlTransTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_incTaxlTransTextFieldActionPerformed
    {//GEN-HEADEREND:event_incTaxlTransTextFieldActionPerformed

        if (Validator.isFloat(incTaxlTransTextField.getText())) {
            cmdCountTaxTrans();
            incTaxlTransTextField.setEditable(false);
            if (netTransPriority == SERVICE_FIRST) {
                curTypeComboBox.setEditable(false);
                curTypeComboBox.setEnabled(true);
                curTypeComboBox.requestFocusInWindow();
            } else if (CashierMainApp.isEnableService()) {
                servTypeComboBox.setEnabled(true);
                servTypeComboBox.requestFocusInWindow();
            } else {
                curTypeComboBox.setEditable(false);
                curTypeComboBox.setEnabled(true);
                curTypeComboBox.requestFocusInWindow();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tax amount must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            incTaxlTransTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_incTaxlTransTextFieldActionPerformed

    private void servTypeComboBoxKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_servTypeComboBoxKeyTyped
    {//GEN-HEADEREND:event_servTypeComboBoxKeyTyped

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            incServlTransTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_servTypeComboBoxKeyTyped

    private void taxTypeComboBoxKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_taxTypeComboBoxKeyTyped
    {//GEN-HEADEREND:event_taxTypeComboBoxKeyTyped

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            incTaxlTransTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_taxTypeComboBoxKeyTyped

    private void serialNumTextFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_serialNumTextFieldFocusLost
    {//GEN-HEADEREND:event_serialNumTextFieldFocusLost

        if (serialNumTextField.getText() == null && this.getCandidateBilldetail().getBillDetailCode() == null) {
            JOptionPane.showMessageDialog(this, "Serial code for this item is required", "Require Serial Code", JOptionPane.OK_OPTION);
            serialNumTextField.requestFocusInWindow();
        }
    }//GEN-LAST:event_serialNumTextFieldFocusLost

    private void serialNumTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_serialNumTextFieldKeyPressed
    {//GEN-HEADEREND:event_serialNumTextFieldKeyPressed

        getGlobalKeyListener(evt);
    }//GEN-LAST:event_serialNumTextFieldKeyPressed

    private void serialNumTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_serialNumTextFieldActionPerformed
    {//GEN-HEADEREND:event_serialNumTextFieldActionPerformed

        this.getCandidateBilldetailCode().setStockCode(serialNumTextField.getText());
    }//GEN-LAST:event_serialNumTextFieldActionPerformed

    private void cancelEditTableButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelEditTableButtonActionPerformed
    {//GEN-HEADEREND:event_cancelEditTableButtonActionPerformed

        //this.saleDetailTable
        //discTransTextField2.requestFocusInWindow ();
        cmdCancel();
    }//GEN-LAST:event_cancelEditTableButtonActionPerformed

    private void cmdCancel() {
        this.initSaleItemFields();
    }

    public void cmdShowCreatePendingOrder() {
        CashierMainFrame main = null;
        try {
            main = (CashierMainFrame) this.getParent();
            main.cmdNewPendingOrder();
            cmdNewSales();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this.getParent().getName() + " " + this.getParent().toString());
        }


    }

    public void cmdShowCreateGift() {
        CashierMainFrame main = null;
        try {
            main = (CashierMainFrame) this.getParent();
            main.cmdNewGift();
            cmdNewSales();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this.getParent().getName() + " " + this.getParent().toString());
        }


    }

    public void cmdShowCreateCreditPayment() {
        CashierMainFrame main = null;
        try {
            main = (CashierMainFrame) this.getParent();
            main.cmdNewCreditPayment();
            cmdNewSales();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this.getParent().getName() + " " + this.getParent().toString());
        }


    }

    public void cmdResetPayments() {
        this.getSaleModel().getCashPayments().clear();
        this.getSaleModel().getCashPaymentInfo().clear();
        this.getSaleModel().getCashReturn().clear();
        this.getSaleModel().setTotCardCost(0);

        this.synchronizeModelAndTable();

        otherCostTextField.setText(this.toCurrency(this.getSaleModel().getTotOtherCost()));
        changeTransTextField.setText(this.toCurrency(0));
        paymentTransTextField.setText(this.toCurrency(0));
        lastDiscTypeComboBox.setEnabled(true);
        resetPaymentButton.setEnabled(false);
        lastDiscTypeComboBox.setEnabled(true);
        lastDiscTypeComboBox.requestFocusInWindow();

    }

    public void cmdEditSaleTable() {
        if (this.getSaleModel().isAnySaleDetail()) {
            this.multiPaymentButton.setEnabled(false);
            this.resetPaymentButton.setEnabled(false);
            this.saleDetailTable.setEnabled(true);
            this.saleDetailTable.requestFocusInWindow();
            this.saleDetailTable.setRowSelectionInterval(0, 0);
        } else {
            JOptionPane.showMessageDialog(this, "Please add any item first", "Empty item", JOptionPane.ERROR_MESSAGE);
            cmdDoAddNewItems();

        }

    }

    private void itemCodeTextFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_itemCodeTextFieldFocusLost
    {//GEN-HEADEREND:event_itemCodeTextFieldFocusLost

        if (serialNumTextField.getText() == null && this.getCandidateBilldetail().getBillDetailCode() == null) {
            //JOptionPane.showMessageDialog (this,"Serial code for this item is required","Require Serial Code",JOptionPane.OK_OPTION);
            //serialNumTextField.requestFocusInWindow ();
        }
    }//GEN-LAST:event_itemCodeTextFieldFocusLost

    private void salesNameTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_salesNameTextFieldActionPerformed
    {//GEN-HEADEREND:event_salesNameTextFieldActionPerformed

        cmdSearchSalesPerson();

    }//GEN-LAST:event_salesNameTextFieldActionPerformed

    private void discTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_discTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_discTypeComboBoxKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.getCandidateBilldetail().setDiscType(discTypeComboBox.getSelectedIndex());
//itemDiscTextField.setText (
            int discType = discTypeComboBox.getSelectedIndex();
            if (discType == PstBillDetail.DISC_TYPE_PERCENT) {
                itemDiscTextField.setText(toCurrency(this.getCandidateBilldetail().getDiscPct()));
                discTypeLabel.setText("(%)");
            } else {
                itemDiscTextField.setText(toCurrency(this.getCandidateBilldetail().getDisc()));
                discTypeLabel.setText("(" + CashierMainApp.getCurrencyCodeUsed() + ")");
            }
            itemDiscTextField.setEditable(true);
            itemDiscTextField.requestFocusInWindow();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_discTypeComboBoxKeyPressed

    private void custTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_custTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_custTypeComboBoxKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmdChangeCustType();
        } else {
            getGlobalKeyListener(evt);
        }
    }//GEN-LAST:event_custTypeComboBoxKeyPressed

    private void saleTypeComboBoxKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_saleTypeComboBoxKeyPressed
    {//GEN-HEADEREND:event_saleTypeComboBoxKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String selectedObject = (String) this.saleTypeComboBox.getSelectedItem();
            if (selectedObject.equals(transType[TRANS_PENDING_ORDER])) {
                cmdShowCreatePendingOrder();
            } else if (selectedObject.equals(transType[TRANS_GIFT])) {
                cmdShowCreateGift();
            } else if (selectedObject.equals(transType[TRANS_CREDIT_PAYMENT])) {
                cmdShowCreateCreditPayment();
            } else if (selectedObject.equals(transType[TRANS_OPEN_BILL])) {
                cmdSearchOpenBill();
            } else if (selectedObject.equals(transType[TRANS_HALF_INVOICE])) {
                cmdHalfInvoice();
            } else {
                cmdChangeTransType();
            }

        } else {
            getGlobalKeyListener(evt);
        }
    }

    private void saleDetailTableKeyTyped(java.awt.event.KeyEvent evt)//GEN-LAST:event_saleTypeComboBoxKeyPressed
    {//GEN-FIRST:event_saleDetailTableKeyTyped

        if (evt.getKeyCode() == (KeyEvent.VK_PAGE_DOWN + KeyEvent.VK_ALT)) {
            JOptionPane.showConfirmDialog(this, "Item Choosen", "Choose", JOptionPane.OK_OPTION);
//discTransTextField2.requestFocusInWindow ();
            lastDiscTypeComboBox.requestFocusInWindow();
        }
    }

    private void jScrollPane2KeyPressed(java.awt.event.KeyEvent evt)//GEN-LAST:event_saleDetailTableKeyTyped
    {//GEN-FIRST:event_jScrollPane2KeyPressed

    }

    private void invoiceSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_jScrollPane2KeyPressed
        // TODO add your handling code here://GEN-FIRST:event_invoiceSearchButtonActionPerformed
        cmdInvoiceSearch();
    }

    private void pendingOrderSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_invoiceSearchButtonActionPerformed
        // TODO add your handling code here://GEN-FIRST:event_pendingOrderSearchButtonActionPerformed
        cmdSearchPendingOrder();
    }//GEN-LAST:event_pendingOrderSearchButtonActionPerformed

    private void saveAsCreditSalesButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveAsCreditSalesButtonActionPerformed
    {//GEN-HEADEREND:event_saveAsCreditSalesButtonActionPerformed

        cmdSaveCreditSales();
    }                        //GEN-LAST:event_saveAsCreditSalesButtonActionPerformed


    private void cmdPendingOrderDelete() {

    }

    private void cmdInvoiceSearch() {
        CashSaleController.setInvoiceChoosen(null);
        CashSaleController.showInvoiceSearch(this, returInvTextField.getText());
        Vector invoiceChoosen = CashSaleController.getInvoiceChoosen();
        if (invoiceChoosen.size() == 1) {
            BillMain billMain = (BillMain) invoiceChoosen.get(0);
            try {
                DefaultSaleModel tmpSaleModel = SessTransactionData.getData(billMain);
                this.getSaleModel().getMainSale().setDocType(PstBillMain.TYPE_RETUR);
                this.getSaleModel().transferForReturn(tmpSaleModel);
                //billMain.setDocType(PstBillMain.TYPE_RETUR);
                this.getSaleModel().getMainSale().setRefInvoiceNum(billMain.getInvoiceNumber());
                this.synchronizeModelAndTable();
                returInvTextField.setText(billMain.getInvoiceNumber());

                this.setOkOtherCost(true);
                itemNameTextField.setEditable(true);
                itemCodeTextField.setEditable(true);
                itemCodeTextField.requestFocusInWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            returInvTextField.requestFocusInWindow();
        }
    }

    private void closeWindowButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_closeWindowButtonActionPerformed
        cmdCloseWindows();

    }//GEN-LAST:event_closeWindowButtonActionPerformed

    private void newSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_newSalesButtonActionPerformed
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure to create new transaction?", "Create new transaction", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            this.cmdNewSales();
        }

    }//GEN-LAST:event_newSalesButtonActionPerformed


    private void itemCodeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {

//GEN-FIRST:event_itemCodeTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            cmdShowItemSearch();

        } else {
            getGlobalKeyListener(evt);
        }

    }//GEN-LAST:event_itemCodeTextFieldKeyPressed

    private Sales salesPerson = new Sales();

    public void cmdDoChooseSaleType() {

        switch (transTypeChoosen) {
            case PstBillMain.TYPE_INVOICE:
                cmdDoTransactionInvoice();
                break;
            case PstBillMain.TYPE_RETUR:
                cmdDoTransactionReturn();
                break;
            case PstBillMain.TYPE_COST:
                cmdDoTransactionCost();
                break;
            case PstBillMain.TYPE_COMPLIMENT:
                cmdDoTransactionCompliment();
                break;
            default:
                break;
        }

    }

    /** edited by wpulantara at Jan 25th 2005 */
    private void cmdSearchSalesPerson() {
        Sales sales = null;
        Vector rs = CashSaleController.getSales(0, 0, salesNameTextField.getText(), "");
        if (rs.size() > 0) {
            if (rs.size() == 1) {
                sales = (Sales) rs.get(0);
            } else {
                CashSaleController.setSalesChoosen(null);
                CashSaleController.showSalesSearch(this, salesNameTextField.getText(), "");
                sales = CashSaleController.getSalesChoosen();

            }
            if (sales != null && sales.getName() != null && sales.getName().length() > 0) {
                this.setSalesPerson(sales);
                this.getSaleModel().setSalesPerson(sales);
                this.getSaleModel().getMainSale().setSalesCode(sales.getCode());
                this.getBillMain().setSalesCode(sales.getCode());
                salesNameTextField.setText(sales.getName());
                salesNameTextField.setEditable(false);
                this.getSaleModel().setSalesPerson(this.getSalesPerson());
                cmdDoChooseSaleType();
            } else {
                salesNameTextField.requestFocusInWindow();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sales person not found", "Sales person", JOptionPane.ERROR_MESSAGE);
            salesNameTextField.requestFocusInWindow();
        }
/*if(sales==null){
                JOptionPane.showMessageDialog(this,"Sales person not found","Sales person",JOptionPane.ERROR_MESSAGE);
                salesNameTextField.requestFocusInWindow();
            }else{

                this.setSalesPerson (sales);
                this.getSaleModel().setSalesPerson(sales);
                this.getSaleModel().getMainSale().setSalesCode(sales.getCode());
                this.getBillMain().setSalesCode(sales.getCode());
                salesNameTextField.setText(sales.getName());

                                /*if(this.getSaleModel().getMainSale().getDocType()!=PstBillMain.TYPE_RETUR){
                                    custTypeComboBox.requestFocusInWindow();
                                    custTypeComboBox.setEditable(false);
                                }else{
                                    returInvTextField.setEditable(true);
                                    returInvTextField.requestFocusInWindow();
                                }*/
/* saleTypeComboBox.setEditable(false);
                saleTypeComboBox.requestFocusInWindow();*/
        //newSalesButton.setEditable(true);
    }

    private void itemNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {

//GEN-FIRST:event_itemNameTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {

            CashSaleController.showItemSearch(null, itemNameTextField.getText(), itemCodeTextField.getText());
            CashSaleController.getProductChoosen();

            Vector product = CashSaleController.getProductChoosen();

            if (product.size() == 1) {
                Material material = (Material) product.get(0);
                itemNameTextField.setText(material.getName());
                itemCodeTextField.setText(material.getBarCode());
                itemNameTextField.requestFocusInWindow();
            }
        } else {
            getGlobalKeyListener(evt);
        }


    }//GEN-LAST:event_itemNameTextFieldKeyPressed

    private void memberNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {

//GEN-FIRST:event_memberNameTextFieldKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            CashSaleController.showMemberSearch(null, memberNameTextField.getText(), memberCodeTextField.getText());
            Vector member = CashSaleController.getMemberChoosen();

            if (member.size() == 1) {
                MemberReg memberReg = (MemberReg) member.get(0);
                memberCodeTextField.setText(memberReg.getMemberBarcode());
                memberNameTextField.setText(memberReg.getPersonName());
            }
        } else {
            getGlobalKeyListener(evt);
        }

    }//GEN-LAST:event_memberNameTextFieldKeyPressed

    private void memberCodeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {

//GEN-FIRST:event_memberCodeTextFieldKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            CashSaleController.showMemberSearch(null, memberNameTextField.getText(), memberCodeTextField.getText());
            Vector member = CashSaleController.getMemberChoosen();

            if (member.size() == 1) {
                MemberReg memberReg = (MemberReg) member.get(0);
                memberCodeTextField.setText(memberReg.getMemberBarcode());
                memberNameTextField.setText(memberReg.getPersonName());

            }
        } else {
            getGlobalKeyListener(evt);
        }


    }//GEN-LAST:event_memberCodeTextFieldKeyPressed


    private boolean isTypedIsNumber(KeyEvent evt) {
        boolean temp = false;
        if (evt.getKeyCode() >= KeyEvent.VK_0 && evt.getKeyCode() <= KeyEvent.VK_9) {
            return true;
        } else {
            return false;
        }
    }


    private void loadSaleDetailToFields(Billdetail billDetail, BillDetailCode billDetailCode) {
        StandartRate rateUsed = this.getSaleModel().getRateUsed();
        itemNameTextField.setText(billDetail.getItemName());
        itemNameTextField.setEditable(false);
        itemCodeTextField.setText(billDetail.getSku());
        itemCodeTextField.setEditable(false);
        itemRemoveButton.setEnabled(true);
        itemUpdateButton.setEnabled(true);
        cancelEditTableButton.setEnabled(true);
        if (billDetail.getDiscType() == PstBillDetail.DISC_TYPE_PERCENT) {
            discTypeComboBox.setSelectedIndex(PstBillDetail.DISC_TYPE_PERCENT);
            itemDiscTextField.setText(toCurrency(billDetail.getDiscPct()));
            discTypeLabel.setText("(%)");
        } else {
            discTypeComboBox.setSelectedIndex(PstBillDetail.DISC_TYPE_VALUE);
            itemDiscTextField.setText(toCurrency(billDetail.getDisc() / (rateUsed.getSellingRate() * (billDetail.getQty() > 0 ? billDetail.getQty() : 1))));
            discTypeLabel.setText("(" + CashierMainApp.getCurrencyCodeUsed() + ")");
        }
        discTypeComboBox.setEditable(false);

        itemDiscTextField.setEditable(true);
        itemQtyTextField.setText(toCurrency(billDetail.getQty()));
        itemQtyTextField.setEditable(true);
        //itemPriceTextField.setText(String.valueOf(billDetail.getItemPrice()));
        itemPriceTextField.setText(toCurrency(billDetail.getItemPrice() / rateUsed.getSellingRate()));
        itemAddButton.setEnabled(false);
        itemRemoveButton.setEnabled(true);
        itemUpdateButton.setEnabled(true);
        itemQtyTextField.requestFocusInWindow();
        if (billDetailCode != null) {
            serialNumTextField.setText(billDetailCode.getStockCode());
        }
    }

    private void saleDetailTableKeyPressed(java.awt.event.KeyEvent evt) {

//GEN-FIRST:event_saleDetailTableKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            int selectedRow = saleDetailTable.getSelectedRow();
            if (saleDetailTable.getRowCount() == 1) {
                selectedRow = 0;
            }
            String sku = (String) this.getSaleTableModel().getValueAt(selectedRow, COL_DETAIL_ITEM_CODE);
            String serialCode = (String) this.getSaleTableModel().getValueAt(selectedRow, COL_DETAIL_STOCK_CODE);

            Billdetail billDetail = this.getSaleModel().findValidSaleDetailWith(sku + "-" + serialCode);
            BillDetailCode billDetailCode = this.getSaleModel().findValidSaleDetailCodeWith(sku + "-" + serialCode); //getHashBillDetailCode ().get (sku);

            this.getSaleModel().setIdxBillDetail(selectedRow);
            this.loadSaleDetailToFields(billDetail, billDetailCode);
            this.setCandidateBilldetail(billDetail);
            this.setCandidateBilldetailCode(billDetailCode);

            this.setICommand(Command.EDIT);
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cmdCancel();
        } else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {

            int selectedRow = saleDetailTable.getSelectedRow();
            if (saleDetailTable.getRowCount() == 1) {
                selectedRow = 0;
            }
            String sku = (String) this.getSaleTableModel().getValueAt(selectedRow, COL_DETAIL_ITEM_CODE);
            String serialCode = (String) this.getSaleTableModel().getValueAt(selectedRow, COL_DETAIL_STOCK_CODE);
            Billdetail billDetail = (Billdetail) this.getSaleModel().findValidSaleDetailWith(sku + "-" + serialCode);
            BillDetailCode billDetailCode = (BillDetailCode) this.getSaleModel().findValidSaleDetailCodeWith(sku + "-" + serialCode); //getHashBillDetailCode ().get (sku);
            this.getSaleModel().setIdxBillDetail(selectedRow);
            this.loadSaleDetailToFields(billDetail, billDetailCode);
            this.setCandidateBilldetail(billDetail);
            this.setCandidateBilldetailCode(billDetailCode);
            this.setICommand(Command.CONFIRM);
            cmdItemDelete();

        } else {
            getGlobalKeyListener(evt);
        }


    }//GEN-LAST:event_saleDetailTableKeyPressed

    private void cmdCountLastDiscTrans() {
        int discType = lastDiscTypeComboBox.getSelectedIndex();
        double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
        double inputed = 0;
        if (Validator.isFloat(discTransTextField2.getText())) {
            try {
                inputed = CashierMainApp.getDoubleFromFormated(discTransTextField2.getText());
            } catch (Exception e) {
                //   inputed = CashierMainApp.getDoubleFromFormated (discTransTextField2.getText());
            }
            double value = 0;
            if (discType == PstBillMain.DISC_TYPE_PCT) {
                this.getSaleModel().getMainSale().setDiscType(PstBillMain.DISC_TYPE_PCT);
                value = inputed;
            } else if (discType == PstBillMain.DISC_TYPE_VALUE) {
                this.getSaleModel().getMainSale().setDiscType(PstBillMain.DISC_TYPE_VALUE);
                value = inputed * dblRateUsed;
            }

            this.getSaleModel().getMainSale().setDiscount(value);
            this.synchronizeModelAndTable();
        } else {
            JOptionPane.showMessageDialog(this, "Discount value must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            discTransTextField2.requestFocusInWindow();
        }
    }

    private void discTransTextField2ActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_discTransTextField2ActionPerformed

        if (Validator.isFloat(discTransTextField2.getText())) {
            cmdCountLastDiscTrans();
            discTransTextField2.setEditable(false);
            paymentTransTextField.setEditable(true);

            if (netTransPriority == SERVICE_FIRST && CashierMainApp.isEnableService()) {
                servTypeComboBox.setEnabled(true);
                servTypeComboBox.requestFocusInWindow();
            } else if (CashierMainApp.isEnableTax()) {
                taxTypeComboBox.setEnabled(true);
                taxTypeComboBox.requestFocusInWindow();
            } else {
                curTypeComboBox.setEnabled(true);
                curTypeComboBox.requestFocusInWindow();
            }
            setPaymentButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Discount amount must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            discTransTextField2.requestFocusInWindow();
        }

    }//GEN-LAST:event_discTransTextField2ActionPerformed


    synchronized private void printCashButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_printCashButtonActionPerformed
        cmdPrintCash();

    }//GEN-LAST:event_printCashButtonActionPerformed

    private void setPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_setPaymentButtonActionPerformed

        cmdSetPayment();


    }//GEN-LAST:event_setPaymentButtonActionPerformed

    private boolean isTotalAndPaymentDiffOk() {
        boolean temp = false;
        if (CashierMainApp.getDoubleFromFormated(paymentTransTextField.getText()) < CashierMainApp.getDoubleFromFormated(totalValueLabel.getText())) {
            return false;
        } else {
            return true;
        }

    }

    private void paymentTransTextFieldActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_paymentTransTextFieldActionPerformed
        if (Validator.isFloat(paymentTransTextField.getText())) {
            cmdAddPayment();
        } else {
            JOptionPane.showMessageDialog(this, "Pay amount must be number", "Input Warning", JOptionPane.WARNING_MESSAGE);
            paymentTransTextField.requestFocusInWindow();
        }


    }//GEN-LAST:event_paymentTransTextFieldActionPerformed


    private void itemRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_itemRemoveButtonActionPerformed

        cmdItemDelete();


    }//GEN-LAST:event_itemRemoveButtonActionPerformed

    private void itemUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_itemUpdateButtonActionPerformed

        cmdItemUpdate();

    }//GEN-LAST:event_itemUpdateButtonActionPerformed

    private void itemAddButtonActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_itemAddButtonActionPerformed

        //cmdItemAdd();
        //cmdCancel ();
        cmdCancel();

    }//GEN-LAST:event_itemAddButtonActionPerformed

    private void itemQtyTextFieldActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_itemQtyTextFieldActionPerformed
        if (Validator.isFloat(itemQtyTextField.getText())) {
            cmdItemAdd();
        } else {
            itemQtyTextField.requestFocusInWindow();

        }

    }//GEN-LAST:event_itemQtyTextFieldActionPerformed

    private void itemCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_itemCodeTextFieldActionPerformed
        if (itemCodeTextField.getText().length() == 0) {
            itemNameTextField.setEditable(true);
            itemNameTextField.requestFocusInWindow();

        } else {

            cmdItemSearch();


        }

    }//GEN-LAST:event_itemCodeTextFieldActionPerformed

    private void itemNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_itemNameTextFieldActionPerformed


        if (itemNameTextField.getText().length() == 0 && itemCodeTextField.getText().length() == 0) {
            cmdShowItemSearch();
        } else {
            cmdItemSearch();
        }


    }//GEN-LAST:event_itemNameTextFieldActionPerformed

    private void saleTypeComboBoxActionPerformed(ActionEvent evt) {

    }

    public BillMain getBillMain() {
        if (billMain == null) {
            billMain = new BillMain();

            // billMain = new BillMain();
/*
             billMain.setCashCashierId (cashCashier.getOID());
            billMain.setAppUserId(cashCashier.getAppUserId ());
            billMain.setLocationId (CashierMainApp.getCashMaster ().getLocationId ());
            billMain.setShiftId (CashierMainApp.getShift ().getOID ());
            billMain.setBillDate (new Date());
             */
            //saleModel.getMainSale ().setAppUserId (CashierMainApp.getCashCashier ().getAppUserId ());
            //saleModel.getMainSale ().setCashCashierId (CashierMainApp.getCashCashier ().getOID ());

            //billMain.setInvoiceCounter (PstBillMain.getCounterTransaction (CashierMainApp.getCashMaster ().getLocationId (), cashierNumber, docType
            //billMain.set

        }
        return billMain;
    }

    public void setBillMain(BillMain billMain) {
        this.billMain = billMain;
    }

    public CashCashier getCashCashier() {
        return cashCashier;
    }

    public void setCashCashier(CashCashier cashCashier) {
        this.cashCashier = cashCashier;
    }

    public DefaultSaleModel getSaleModel() {
        if (saleModel == null) {
            saleModel = new DefaultSaleModel();

/*saleModel.setRateUsed (CashSaleController.getLatestRate (CashierMainApp.getDSJ_CashierXML ().getConfig (0).currencyId));
            saleModel.setCurrencyTypeUsed (CashSaleController.getCurrencyType (CashierMainApp.getDSJ_CashierXML ().getConfig (0).currencyId));
            saleModel.getMainSale ();
             */
        }
        //saleModel.getMainSale ().setI
        //saleModel.getMainSale ().setBillStatus (PstBillMain.
        return saleModel;
    }

    private Sales currentSales = null;

    public DefaultSaleModel getSaleModel(int transType) {

        //if(saleModel==null){
        saleModel = new DefaultSaleModel();
        saleModel.setTransType(transType);
        saleModel.setSalesPerson(this.getCurrentSales());
        //}


        return saleModel;
    }

    public void setSaleModel(DefaultSaleModel saleModel) {
        this.saleModel = saleModel;
    }

    private void createNewSale(int transTypeChoosen) {

        this.getSaleModel(transTypeChoosen);
        //this.getSaleModel().setSalesPerson (this.getSalesPerson ());
        //saleModel.getMainSale ().setSalesCode (

    }

    private void closeSale() {
        if (billMain == null) {
            JOptionPane.showConfirmDialog(this, "Are you sure to close this sale?", "Sale Closing", JOptionPane.YES_NO_CANCEL_OPTION);

        } else {
            billMain = null;
        }
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_jTextField1ActionPerformed

        //createNewSale();
        custTypeComboBox.setEditable(false);
        custTypeComboBox.requestFocusInWindow();
        //memberCodeTextField.requestFocusInWindow();


    }//GEN-LAST:event_jTextField1ActionPerformed

    private void custTypeComboBoxActionPerformed(ActionEvent evt) {

    }

    private void memberCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_memberCodeTextFieldActionPerformed


        if (memberCodeTextField.getText().length() == 0) {
            memberNameTextField.requestFocusInWindow();
        } else {
            cmdMemberSearch();
        }
        //if(memberFound==true){
        //itemNameTextField.requestFocusInWindow();
        //}


    }//GEN-LAST:event_memberCodeTextFieldActionPerformed

    private void memberNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {

//GEN-FIRST:event_memberNameTextFieldActionPerformed

        if (memberNameTextField.getText().length() == 0) {
            CashSaleController.showMemberSearch(null, memberNameTextField.getText(), memberCodeTextField.getText());
            Vector member = CashSaleController.getMemberChoosen();

            if (member.size() == 1) {
                MemberReg memberReg = (MemberReg) member.get(0);
                memberCodeTextField.setText(memberReg.getMemberBarcode());
                memberNameTextField.setText(memberReg.getPersonName());
            }
        } else {
            cmdMemberSearch();
        }

    }//GEN-LAST:event_memberNameTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonCommandPanel;
    private javax.swing.JButton cancelEditTableButton;
    private javax.swing.JTextField cashierNumberTextField;
    private javax.swing.JTextField changeTransTextField;
    private javax.swing.JButton closeSaleButton;
    private javax.swing.JLabel contactInputLabel;
    private javax.swing.JLabel coverGuestNameLabel;
    private javax.swing.JTextField coverGuestNameTextField;
    private javax.swing.JLabel coverLabel;
    private javax.swing.JTextField coverNoTextField;
    private javax.swing.JTextField coverTextField;
    private javax.swing.JComboBox curTypeComboBox;
    private javax.swing.JTextField currencyRateTextField;
    private javax.swing.JComboBox custTypeComboBox;
    private javax.swing.JLabel custTypeLabel3;
    private javax.swing.JLabel cvrLabel6;
    private javax.swing.JTextField discTransTextField2;
    private javax.swing.JComboBox discTypeComboBox;
    private javax.swing.JLabel discTypeLabel;
    private javax.swing.JButton doPaymentButton;
    private javax.swing.JButton editItemButton;
    private javax.swing.JButton editTableButton;
    private javax.swing.JLabel endSaleLabel;
    private javax.swing.JLabel guestNameLabel;
    private javax.swing.JPanel guestSearchPanel;
    private javax.swing.JTextField guestTextField;
    private javax.swing.JComboBox guestTypeComboBox;
    private javax.swing.JLabel guestTypeLabel;
    private javax.swing.JPanel hiddenCommandPanel;
    private javax.swing.JTextField incServlTransTextField;
    private javax.swing.JTextField incTaxlTransTextField;
    private javax.swing.JTextField invoiceNumberTextField;
    private javax.swing.JButton invoiceSearchButton;
    private javax.swing.JButton itemAddButton;
    private javax.swing.JTextField itemCodeTextField;
    private javax.swing.JTextField itemDiscTextField;
    private javax.swing.JPanel itemInfoPanel;
    private javax.swing.JPanel itemListPanel;
    private javax.swing.JTextField itemNameTextField;
    private javax.swing.JTextField itemPriceTextField;
    private javax.swing.JTextField itemQtyTextField;
    private javax.swing.JButton itemRemoveButton;
    private javax.swing.JButton itemUpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel labelCommandPanel;
    private javax.swing.JComboBox lastDiscTypeComboBox;
    private javax.swing.JTextField lastPaymentTransTextField;
    private javax.swing.JComboBox locationComboBox;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel membCodeLabel5;
    private javax.swing.JLabel membNameLabel4;
    private javax.swing.JTextField memberCodeTextField;
    private javax.swing.JPanel memberGuestPanel;
    private javax.swing.JTextField memberNameTextField;
    private javax.swing.JPanel memberOpenPanel;
    private javax.swing.JPanel memberShipPanel;
    private javax.swing.JButton multiPaymentButton;
    private javax.swing.JLabel newSaleLabel;
    private javax.swing.JLabel notesLabel19;
    private javax.swing.JTextField notesTextField;
    private javax.swing.JPanel openBillPanel;
    private javax.swing.JLabel otherCostLabel;
    private javax.swing.JTextField otherCostTextField;
    private javax.swing.JPanel otherInfoPanel;
    private javax.swing.JLabel paddingLabel1;
    private javax.swing.JLabel paddingLabel2;
    private javax.swing.JPanel paymentDetailPanel;
    private javax.swing.JTextField paymentTransTextField;
    private javax.swing.JButton pendingOrderSearchButton;
    private javax.swing.JLabel persDiscLabel23;
    private javax.swing.JTextField personalDiscTextField;
    private javax.swing.JTextField poinTextField;
    private javax.swing.JComboBox priceCurrComboBox;
    private javax.swing.JLabel priceCurrLabel;
    private javax.swing.JButton printCashButton;
    private javax.swing.JLabel reprintLabel;
    private javax.swing.JLabel resNumberLabel;
    private javax.swing.JTextField resNumberTextField;
    private javax.swing.JButton resetPaymentButton;
    private javax.swing.JTextField returInvTextField;
    private javax.swing.JLabel roomNumberLabel;
    private javax.swing.JTextField roomNumberTextField;
    private javax.swing.JPanel saleCommandPanel;
    private javax.swing.JTextField saleDateTextField;
    private javax.swing.JTable saleDetailTable;
    private javax.swing.JPanel saleShortCutPanel;
    private javax.swing.JComboBox saleTypeComboBox;
    private javax.swing.JPanel saleTypePanel;
    private javax.swing.JLabel salesNameLabel;
    private javax.swing.JTextField salesNameTextField;
    private javax.swing.JButton saveAsCreditButton;
    private javax.swing.JButton saveAsCreditSalesButton;
    private javax.swing.JButton saveAsOpenBillButton;
    private javax.swing.JTextField serialNumTextField;
    private javax.swing.JComboBox servTypeComboBox;
    private javax.swing.JButton setPaymentButton;
    private javax.swing.JTextField shopNameTextField;
    private javax.swing.JButton srcOpenBillButton;
    private javax.swing.JComboBox taxTypeComboBox;
    private javax.swing.JTextField timeTextField;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel totalSaleLabel;
    private javax.swing.JTextField totalTransTextField;
    private javax.swing.JLabel totalValueCurTypeLabel;
    private javax.swing.JTextField totalValueLabel;
    // End of variables declaration//GEN-END:variables



    public void cmdSetColumnTableSize() {

        int totalWidth = saleDetailTable.getWidth();

        saleDetailTable.revalidate();
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_NO).setPreferredWidth((int) (totalWidth * 0.05));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_ITEM_CODE).setPreferredWidth((int) (totalWidth * 0.10));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_ITEM_NAME).setPreferredWidth((int) (totalWidth * 0.30));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_STOCK_CODE).setPreferredWidth((int) (totalWidth * 0.10));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_ITEM_PRICE).setPreferredWidth((int) (totalWidth * 0.10));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_ITEM_DISC).setPreferredWidth((int) (totalWidth * 0.10));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_ITEM_QTY).setPreferredWidth((int) (totalWidth * 0.10));
        saleDetailTable.getColumnModel().getColumn(COL_DETAIL_ITEM_TOTAL).setPreferredWidth((int) (totalWidth * 0.15));

        //saleDetailTable.revalidate ();
        saleDetailTable.repaint();
    }

    public void initAllFields() {

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timeTextField.setText(Formater.formatDate(new Date(), "hh:mm:ss a"));
            }
        };
        this.setMnemonics();

        new Timer(1000, taskPerformer).start();

        resetPaymentButton.setEnabled(false);
        multiPaymentButton.setEnabled(false);
        editItemButton.setEnabled(false);
        saveAsCreditButton.setEnabled(false);

        priceCurrComboBox.setEditable(false);
        priceCurrComboBox.setEnabled(false);
        salesNameTextField.setEditable(false);
        saleShortCutPanel.setVisible(true);
        saleTypeComboBox.setEnabled(true);

        saleTypeComboBox.setSelectedIndex(0);
        invoiceNumberTextField.setText("");
        salesNameTextField.setText("");
        returInvTextField.setEditable(false);
        returInvTextField.setText("");
        custTypeComboBox.setEnabled(false);
        custTypeComboBox.setSelectedIndex(0);
        custTypeComboBox.setEditable(false);
        memberCodeTextField.setEditable(false);
        memberCodeTextField.setText("");
        memberNameTextField.setEditable(false);
        memberNameTextField.setText("");

        guestTypeComboBox.setEnabled(false);
        guestTextField.setEditable(false);
        roomNumberTextField.setEditable(false);
        resNumberTextField.setEditable(false);
        guestTextField.setText("");
        roomNumberTextField.setText("");
        resNumberTextField.setText("");

        coverGuestNameTextField.setEditable(false);
        coverTextField.setEditable(false);
        coverGuestNameTextField.setText("");
        coverTextField.setText("");

        this.setOkOtherCost(false);

        discTypeComboBox.setEnabled(false);
        personalDiscTextField.setEditable(false);
        personalDiscTextField.setText("");
        itemNameTextField.setEditable(false);
        itemNameTextField.setText("");
        itemCodeTextField.setEditable(false);
        itemCodeTextField.setText("");
        itemDiscTextField.setEditable(false);
        itemDiscTextField.setText("");
        itemPriceTextField.setEditable(false);
        itemPriceTextField.setText("");
        itemQtyTextField.setEditable(false);
        itemQtyTextField.setText("");
        serialNumTextField.setText("");
        serialNumTextField.setEditable(false);


        itemAddButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);
        itemRemoveButton.setEnabled(false);
        editTableButton.setEnabled(false);
        totalTransTextField.setText("0");
        otherCostTextField.setText("0");
        lastPaymentTransTextField.setText("0");
        changeTransTextField.setText("0");
        discTransTextField2.setEditable(false);
        discTransTextField2.setText("0");

        lastDiscTypeComboBox.setEnabled(false);
        taxTypeComboBox.setEnabled(false);
        servTypeComboBox.setEnabled(false);
        incTaxlTransTextField.setEditable(false);
        incTaxlTransTextField.setText("0");
        incServlTransTextField.setEditable(false);
        incServlTransTextField.setText("0");
        paymentTransTextField.setEditable(false);
        paymentTransTextField.setText("0");
        curTypeComboBox.setEnabled(false);
        changeTransTextField.setText("0");
        changeTransTextField.setEditable(false);
        poinTextField.setText("0");
        poinTextField.setEditable(false);

        setPaymentButton.setEnabled(false);
        printCashButton.setEnabled(false);
        saveAsOpenBillButton.setEnabled(false);
        saveAsOpenBillButton.setVisible(CashierMainApp.isEnableOpenBill());
        doPaymentButton.setEnabled(false);
        saveAsCreditSalesButton.setEnabled(false);
        saveAsCreditSalesButton.setVisible(CashierMainApp.isEnableCreditPayment() && !CashierMainApp.isEnableOpenBill());
        srcOpenBillButton.setEnabled(false);
        invoiceSearchButton.setEnabled(false);
        pendingOrderSearchButton.setEnabled(false);
        Vector temp = new Vector(CashierMainApp.getHashCurrencyType().keySet());

        curTypeComboBox.setModel(new DefaultComboBoxModel(temp));
        curTypeComboBox.setSelectedItem((String) CashierMainApp.getCurrencyCodeUsed());
        curTypeComboBox.setEditable(false);

        if (CashierMainApp.isEnableLocationSelect()) {
            locationComboBox.setModel(new DefaultComboBoxModel(new Vector(CashSaleController.getHashLocationById().values())));
            locationComboBox.setSelectedItem((String) CashSaleController.getHashLocationById().get(CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId + ""));
            locationComboBox.setEditable(false);
            returInvTextField.setText((String) CashSaleController.getHashLocationName().get(locationComboBox.getSelectedItem()));
        }

        priceCurrComboBox.setModel(new DefaultComboBoxModel(temp));
        priceCurrComboBox.setSelectedItem((String) CashierMainApp.getCurrencyCodeUsed());

        poinTextField.setText("0");
        lastPaymentTransTextField.setText("0");


        saleDateTextField.setText(Formater.formatDate(new Date(), CashierMainApp.getDSJ_CashierXML().getConfig(0).fordate));
        cashierNumberTextField.setText("" + CashierMainApp.getCashUser().getUserName());
        String frameTitleText = "Billing >> Shop:" + CashierMainApp.getDSJ_CashierXML().getConfig(0).company + ">> Cashier :" + cashierNumberTextField.getText() + ">> At : " + saleDateTextField.getText() + "";
        this.setTitle(frameTitleText);

        String forcur = CashierMainApp.getDSJ_CashierXML().getConfig(0).forcurrency;
        totalValueCurTypeLabel.setText(CashierMainApp.getCurrencyCodeUsed());

        totalValueLabel.setText(toCurrency(0));

        this.getSaleTableModel();
        saleTableModel.setDataVector(new Vector(), this.getColumnIdentifiers());

        saleDetailTable.setModel(saleTableModel);
        saleDetailTable.revalidate();
        cmdSetColumnTableSize();
        cmdChooseTransType();
        //salesNameTextField.requestFocusInWindow ();

    }

    public void cmdDoTransactionInvoice() {
        this.getSaleModel().getMainSale().setDocType(PstBillMain.TYPE_INVOICE);

        srcOpenBillButton.setEnabled(true);
        saveAsOpenBillButton.setEnabled(true);
        pendingOrderSearchButton.setEnabled(true);
        cmdChooseCustType();

    }

    public void cmdDoTransactionReturn() {
        this.getSaleModel().getMainSale().setDocType(PstBillMain.TYPE_RETUR);
        invoiceSearchButton.setEnabled(true);
        returInvTextField.setEditable(true);
        returInvTextField.setText("");
        returInvTextField.requestFocusInWindow();
        saveAsCreditSalesButton.setEnabled(false);
    }

    public void cmdDoTransactionCost() {
        this.getSaleModel().getMainSale().setDocType(PstBillMain.TYPE_COST);
    }

    public void cmdDoTransactionCompliment() {
        this.getSaleModel().getMainSale().setDocType(PstBillMain.TYPE_COMPLIMENT);
    }

    public void cmdSelectSales() {
        if (CashierMainApp.isEnableSaleEntry()) {
            salesNameTextField.setEditable(true);
            salesNameTextField.setEnabled(true);
            salesNameTextField.requestFocusInWindow();
        } else {
            this.getSaleModel().getMainSale().setSalesCode("");
            this.getSaleModel().setSalesPerson(new Sales());
            this.cmdDoChooseSaleType();
        }
    }

    public void cmdSelectPriceCurr() {
        if (CashierMainApp.isEnablePriceCurrSelect()) {
            priceCurrComboBox.setEnabled(true);
            priceCurrComboBox.requestFocusInWindow();
        } else {
            this.cmdSelectSales();
        }
    }

    public void cmdSelectLocation() {
        if (CashierMainApp.isEnableLocationSelect()) {
            locationComboBox.setEnabled(true);
            locationComboBox.requestFocusInWindow();
        } else {
            this.cmdSelectPriceCurr();
        }
    }

    public void cmdChangeTransType() {

        transTypeChoosen = this.saleTypeComboBox.getSelectedIndex();
        String selectedObject = (String) this.saleTypeComboBox.getSelectedItem();
        if (selectedObject.equals(transType[TRANS_INVOICE])) {
            transTypeChoosen = PstBillMain.TYPE_INVOICE;
        } else if (selectedObject.equals(transType[TRANS_RETURN])) {
            transTypeChoosen = PstBillMain.TYPE_RETUR;
        } else if (selectedObject.equals(transType[TRANS_COST])) {
            transTypeChoosen = PstBillMain.TYPE_COST;
        } else if (selectedObject.equals(transType[TRANS_COMPLIMENT])) {
            transTypeChoosen = PstBillMain.TYPE_COMPLIMENT;
        } else if (selectedObject.equals(transType[TRANS_PAY_PENDING_ORDER])) {
            transTypeChoosen = PstBillMain.TYPE_INVOICE;
        }

        this.createNewSale(transTypeChoosen);

        saleTypeComboBox.setSelectedIndex(transTypeChoosen);
        saleTypeComboBox.setEnabled(true);
        invoiceNumberTextField.setText(this.getSaleModel().getMainSale().getInvoiceNumber());
        saleDateTextField.setText(Formater.formatDate(this.getSaleModel().getMainSale().getBillDate(), CashierMainApp.getDSJ_CashierXML().getConfig(0).fordate));
        if (selectedObject.equals(transType[TRANS_PAY_PENDING_ORDER])) {
            cmdSearchPendingOrder();
        } else {
            cmdSelectLocation();
        }

    };

    public void cmdChangeCustType() {

        int custType = custTypeComboBox.getSelectedIndex();
        switch (custType) {
            case MEMBER_TYPE:
                memberCodeTextField.setEditable(true);
                memberNameTextField.setEditable(true);
                memberCodeTextField.requestFocusInWindow();
                break;
            case NON_MEMBER_TYPE:
                nonCustomerMember = CashSaleController.getCustomerNonMember();
                if (nonCustomerMember == null) {
                    JOptionPane.showMessageDialog(this, "Please set data for non member", "Not Found", JOptionPane.ERROR_MESSAGE);
                    custTypeComboBox.requestFocusInWindow();
                } else {
                    this.getSaleModel().setCustomerServed(nonCustomerMember);
                    memberCodeTextField.setText("");
                    memberNameTextField.setText("");
                    memberCodeTextField.setEditable(false);
                    memberNameTextField.setEditable(false);
                    custTypeComboBox.setEnabled(false);
                    if (CashierMainApp.isEnableOpenBill()) {
                        coverGuestNameTextField.setEditable(true);
                        coverGuestNameTextField.requestFocusInWindow();
                    } else {
                        this.setOkOtherCost(true);
                        itemCodeTextField.setEditable(true);
                        itemCodeTextField.requestFocusInWindow();
                        itemNameTextField.setEditable(true);
                    }
                }
                break;
            default:
                break;
        }

    }

    public void cmdGuestSearch() {
        boolean guestFound = false;

        Vector vctGuestFound = new Vector();
        CustomerLink customerLink = new CustomerLink();
        vctGuestFound = CashSaleController.getGuestList(guestTextField.getText(), roomNumberTextField.getText(), resNumberTextField.getText(), 0, 0);
        if (vctGuestFound != null && vctGuestFound.size() == 1) {
            customerLink = (CustomerLink) vctGuestFound.get(0);
            this.getSaleModel().setCustomerLink(customerLink);
            this.getSaleModel().getMainSale().setSpecialId(customerLink.getReservationId());
            guestTextField.setText(customerLink.getCustomerName());
            roomNumberTextField.setText(customerLink.getRoomNumber());
            resNumberTextField.setText(customerLink.getResNumber());
            totalValueCurTypeLabel.setText(this.getSaleModel().getCurrencyTypeUsed().getCode());
            guestFound = true;
        } else if (vctGuestFound.size() > 1) {
            CashSaleController.setGuestChoosen(null);
            CashSaleController.showGuestSearch(this, "");
            Vector vctGuest = CashSaleController.getGuestChoosen();
            if (vctGuest.size() == 1) {
                customerLink = (CustomerLink) vctGuest.get(0);
                this.getSaleModel().setCustomerLink(customerLink);
                this.getSaleModel().getMainSale().setSpecialId(customerLink.getReservationId());
                guestTextField.setText(customerLink.getCustomerName());
                roomNumberTextField.setText(customerLink.getRoomNumber());
                resNumberTextField.setText(customerLink.getResNumber());
                totalValueCurTypeLabel.setText(this.getSaleModel().getCurrencyTypeUsed().getCode());
                guestFound = true;
            }
        } else {
            guestFound = false;
        }
        if (!guestFound) {
            JOptionPane.showMessageDialog(this, "Guest Not Found", "Guest Not Found", JOptionPane.ERROR_MESSAGE);
            guestTextField.requestFocusInWindow();

        } else {
            if (CashierMainApp.isEnableOpenBill()) {
                coverGuestNameTextField.setEditable(true);
                coverGuestNameTextField.requestFocusInWindow();
            } else {
                cmdCancel();
            }
        }

    }

    public void cmdMemberSearch() {

        boolean memberFound = false;
        Vector membFound = CashSaleController.getMember(0, 0, memberCodeTextField.getText(), memberNameTextField.getText());
        if (membFound.size() == 1) {
            memberFound = true;
        } else {
            CashSaleController.setMemberChoosen(null);
            CashSaleController.showMemberSearch(null, memberNameTextField.getText(), memberCodeTextField.getText());
            Vector member = CashSaleController.getMemberChoosen();

            if (member.size() == 1) {
                MemberReg memberReg = (MemberReg) member.get(0);
                memberCodeTextField.setText(memberReg.getMemberBarcode());
                memberNameTextField.setText(memberReg.getPersonName());
                MemberGroup group = new MemberGroup();
                group = CashSaleController.getGroupOfMember(memberReg.getMemberGroupId());
                if (group.getGroupType() == PstMemberGroup.AGEN) {
                    saveAsCreditSalesButton.setEnabled(true);
                }
                memberFound = true;
            } else
                memberFound = false;
        }
        if (memberFound == false) {
            JOptionPane.showMessageDialog(this, "Member Not Found", "Member Not Found", JOptionPane.ERROR_MESSAGE);
            memberCodeTextField.requestFocusInWindow();

        } else {
            if (membFound.size() > 0) {
                MemberReg temp = (MemberReg) membFound.get(0);
                this.getSaleModel().setCustomerServed(temp);
                this.getSaleModel().getMainSale().setGuestName(temp.getPersonName());
                Hashtable hashPersDisc = new Hashtable();
                hashPersDisc = CashSaleController.getMemberPersonalDiscount(temp);
                this.getSaleModel().setHashPersonalDisc(hashPersDisc);
                memberNameTextField.setText(temp.getPersonName() + " " + temp.getPersonLastname());
                memberCodeTextField.setText(temp.getMemberBarcode());
                memberNameTextField.setEditable(false);
                memberCodeTextField.setEditable(false);
                custTypeComboBox.setEnabled(false);
                personalDiscTextField.setText("-set per item-");
                personalDiscTextField.setEditable(false);
                if (CashierMainApp.isEnableOpenBill()) {
                    coverGuestNameTextField.setEditable(true);
                    coverGuestNameTextField.setText(temp.getPersonName() + " " + temp.getPersonLastname());
                    coverGuestNameTextField.requestFocusInWindow();
                } else {
                    this.setOkOtherCost(true);
                    itemNameTextField.setEditable(true);
                    itemCodeTextField.setEditable(true);
                    itemCodeTextField.requestFocusInWindow();
                    this.setICommand(Command.ADD);
                }
            }
        }

    }

    public void cmdKreditSearch() {

        boolean creditSaleFound = false;
        CashSaleController.showCreditSalesSearch(null, "", "");
        Vector vctCreditSaleChoosen = CashSaleController.getCreditSaleChoosen();
        if (vctCreditSaleChoosen.size() == 1) {
            BillMain temp = (BillMain) vctCreditSaleChoosen.get(0);
            this.setSaleModel(SessTransactionData.getData(temp));
            this.getSaleModel().countLastPayment();
            this.synchronizeModelAndTable();
            this.saleDetailTable.setEnabled(false);

        }
        if (creditSaleFound == true) {

        } else {
            JOptionPane.showConfirmDialog(this, "Credit Search", "Credit Sale Search Not Found", JOptionPane.ERROR_MESSAGE);
        }

    }

    private Billdetail getCandidateBilldetail() {
        if (candidateBilldetail == null) {
            candidateBilldetail = new Billdetail();
        }
        return candidateBilldetail;
    }

    private void setCandidateBilldetail(Billdetail billDetail) {

        this.candidateBilldetail = billDetail;
        if (billDetail != null && billDetail.getMaterialId() > 0) {
            Material mat = new Material();
            try {
                mat = PstMaterial.fetchExc(billDetail.getMaterialId());
            } catch (Exception dbe) {
                System.out.println("err on setCandidateBilldetail: " + dbe.toString());
            }
            this.setMaterialOfSaleDetail(mat);
        }


    }

    private int iCommand = 0;

    public void cmdShowItemSearch() {

        if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR) {
            cmdSearchInvoiceItem();
        } else {
            MemberGroup memberGroup = new MemberGroup();
            try {
                memberGroup = PstMemberGroup.fetchExc(this.getSaleModel().getCustomerServed().getMemberGroupId());
            } catch (Exception dbe) {
                dbe.printStackTrace();
            }
            CashSaleController.setProductChoosen(null);
            CashSaleController.showItemSearch(this, itemNameTextField.getText(), itemCodeTextField.getText(), this.getSaleModel().getRateUsed().getOID(), memberGroup.getPriceTypeId());
            Vector product = CashSaleController.getProductChoosen();
            if (product.size() == 1) {
                Material material = (Material) product.get(0);
                itemNameTextField.setText(material.getName());
                //itemNameTextField.setText(material.getFullName());
                itemCodeTextField.setText(material.getSku());
                itemCodeTextField.requestFocusInWindow();
            }
        }
    }

    public void cmdShowItemSearch(long currId, long priceTypeId) {

        if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR) {
            cmdSearchInvoiceItem();
        } else {
            CashSaleController.setProductChoosen(null);
            CashSaleController.showItemSearch(this, itemNameTextField.getText(), itemCodeTextField.getText(), currId, priceTypeId);
            Vector product = CashSaleController.getProductChoosen();
            if (product.size() == 1) {
                Material material = (Material) product.get(0);
                itemNameTextField.setText(material.getName());
                //itemNameTextField.setText(material.getFullName());
                if (material.getBarCode().length() > 0) {
                    itemCodeTextField.setText(material.getBarCode());
                } else {
                    itemCodeTextField.setText(material.getSku());
                }
                itemCodeTextField.requestFocusInWindow();
            }
        }
    }

    private Material materialOfSaleDetail;

    public void cmdDoAddNewItems() {
        cmdCancel();
    }

    /** optimized by wpulantara */
    public void cmdItemSearch() {

        StandartRate rateUsed = this.getSaleModel().getRateUsed();
        if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR) {
            cmdSearchInvoiceItem();
        } else {
            boolean itemFound = false;
            MemberGroup memberGroup = new MemberGroup();
            try {
                memberGroup = PstMemberGroup.fetchExc(this.getSaleModel().getCustomerServed().getMemberGroupId());
            } catch (Exception dbe) {
                dbe.printStackTrace();
            }
            Vector vctItemSearch = new Vector(); //CashSaleController.getItem(0, 0, itemNameTextField.getText(), itemCodeTextField.getText(), this.getSaleModel().getRateUsed().getOID(), memberGroup.getPriceTypeId());
            if (vctItemSearch.size() == 1) {
                itemFound = true;
            } else {
                cmdShowItemSearch(this.getSaleModel().getRateUsed().getOID(), memberGroup.getPriceTypeId());
            }

            if (itemFound) {
                Material temp = (Material) vctItemSearch.get(0);
                this.setMaterialOfSaleDetail(temp);
                if (candidateBilldetail != null) {
                    candidateBilldetail = new Billdetail();
                } else {
                    candidateBilldetail = this.getCandidateBilldetail();
                }
                candidateBilldetail = CashSaleController.createBillDetailFrom(temp, this.getSaleModel().getCustomerServed(), this.getSaleModel().getHashPersonalDisc(), this.getSaleModel().getRateUsed(), this.getSaleModel().getCurrencyTypeUsed());
                if (candidateBilldetail.getItemPrice() > 0) {

                    discTypeComboBox.setEditable(false);
                    String stCode = "";
                    if (temp.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                        long saleLoc = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId);
                        int codeSize = CashSaleController.getSerialCodeCount(temp.getOID(), saleLoc);

                        if (codeSize > 0) {
                            CashSaleController.showSerialCodeListDialog(this, temp.getOID(), saleLoc);
                            MaterialStockCode objCode = CashSaleController.getStockCodeChoosen();
                            stCode = objCode.getStockCode();
                        } else {
                            JOptionPane.showMessageDialog(this, "Serial code not found, manual entry", "Serial Code", JOptionPane.INFORMATION_MESSAGE);
                        }
                        while (stCode.length() == 0) {
                            stCode = JOptionPane.showInputDialog(this, "Input serial code ", "Serial Code Entry", JOptionPane.WARNING_MESSAGE);
                            if (stCode == null) {
                                stCode = "";
                            }
                        }
                    } else if (CashierMainApp.isUsingProductColor()) {
                        System.out.println("masuk ke using prod color");
                        int codeSize = CashSaleController.getSerialCodeCount(temp.getOID(), 0);
                        if (codeSize > 0) {
                            System.out.println("masuk ke if di using prod color");
                            CashSaleController.showSerialCodeListDialog(this, temp.getOID(), 0);
                            MaterialStockCode objCode = CashSaleController.getStockCodeChoosen();
                            stCode = objCode.getStockCode();
                        }
                    }
                    if (stCode.length() == 0 && temp.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                        cmdCancel();
                    } else {
                        itemDiscTextField.setEditable(true);
                        itemAddButton.setEnabled(true);
                        itemCodeTextField.setText(candidateBilldetail.getSku());
                        itemNameTextField.setText(candidateBilldetail.getItemName());
                        itemDiscTextField.setText(toCurrency(candidateBilldetail.getDiscPct()));
                        itemPriceTextField.setText(toCurrency(candidateBilldetail.getItemPrice() / rateUsed.getSellingRate()));

                        serialNumTextField.setText(stCode);
                        if (temp.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                            itemQtyTextField.setText("1");
                        }
                        BillDetailCode detailCode = new BillDetailCode();
                        detailCode.setStockCode(stCode);

                        this.setCandidateBilldetailCode(detailCode);

                        if (CashierMainApp.isEnableProductImage()) {
                            CashSaleController.showProductImageDialog(this, candidateBilldetail.getSku() + "." + (CashierMainApp.getDSJ_CashierConfig().imageEx.length() > 0 ? CashierMainApp.getDSJ_CashierConfig().imageEx : "jpg"));
                        }

                        itemQtyTextField.setEditable(true);
                        itemQtyTextField.setText("1");
                        discTypeComboBox.setEnabled(true);
                        discTypeComboBox.setSelectedIndex(candidateBilldetail.getDiscType());
                        if (CashierMainApp.isEnablePriceEdit()) {
                            itemPriceTextField.setEditable(true);
                            itemPriceTextField.requestFocusInWindow();
                        } else {
                            discTypeComboBox.requestFocusInWindow();
                        }

                        this.setICommand(Command.ADD);
                        this.setCandidateBilldetail(candidateBilldetail);

                    }
                } else if (candidateBilldetail.getItemPrice() <= 0) {
                    JOptionPane.showMessageDialog(this, "Price has not been set for this item.\n Cannot used for transaction");
                    cmdCancel();
                }
            }
        }

    }

    public Billdetail validateTotalCandidateBill(Billdetail candidate) {

        int discCombo = discTypeComboBox.getSelectedIndex();
        double dTotal = 0;
        double dDisc = 0;
        double dPrice = candidate.getItemPrice();
        double dQty = candidate.getQty();
        if (discCombo == DISC_PCT) {
            candidate.setDiscType(PstBillDetail.TYPE_DISC_PCT);
            candidate.setDiscPct(CashierMainApp.getDoubleFromFormated(itemDiscTextField.getText()));
            dDisc = candidateBilldetail.getDiscPct();
            dTotal = (dPrice * dQty) * ((100 - dDisc) % 100);
        } else {
            candidate.setDiscType(PstBillDetail.TYPE_DISC_VAL);
            candidate.setDisc(CashierMainApp.getDoubleFromFormated(itemDiscTextField.getText()));
            dDisc = candidate.getDisc();
            dTotal = (dPrice * dQty) - dDisc;
        }
        candidate.setTotalPrice(dTotal);


        return candidate;
    }

    public void cmdItemAdd() {

        Billdetail candidate = this.getCandidateBilldetail();
        candidate.setQty(CashierMainApp.getDoubleFromFormated(itemQtyTextField.getText()));
        int discCombo = discTypeComboBox.getSelectedIndex();
        candidate.setDiscType(discCombo);
        double dTotal = 0;

        double dDisc = CashierMainApp.getDoubleFromFormated(itemDiscTextField.getText());
        double dPrice = candidate.getItemPrice();

        if (discCombo == PstBillDetail.DISC_TYPE_PERCENT) {
            candidate.setDiscType(PstBillDetail.DISC_TYPE_PERCENT);
            candidate.setDiscPct(CashierMainApp.getDoubleFromFormated(itemDiscTextField.getText()));
            candidate.setDisc(0);

        } else {
            double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
            candidate.setDiscType(PstBillDetail.DISC_TYPE_VALUE);
            candidate.setDisc(CashierMainApp.getDoubleFromFormated(itemDiscTextField.getText()) * dblRateUsed * candidate.getQty());
            candidate.setDiscPct(0);
        }
        itemRemoveButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);

        BillDetailCode saleDetailCode = this.getCandidateBilldetailCode();
        if (this.getMaterialOfSaleDetail().getRequiredSerialNumber() == 1 && candidate.getQty() > 1) {
            JOptionPane.showMessageDialog(this, "This item has serial code, qty must be 1", "Serial Coded Item Qty", JOptionPane.ERROR_MESSAGE);
            itemQtyTextField.requestFocusInWindow();
        } else {
            if (this.getICommand() == Command.ADD) {
                this.getSaleModel().insertSaleDetail(candidate, saleDetailCode);
            } else if (this.getICommand() == Command.EDIT) {
                this.getSaleModel().updateSaleDetail(candidate, saleDetailCode);
            }

            this.synchronizeModelAndTable();
            initSaleItemFields();

            this.setCandidateBilldetail(new Billdetail());

        }


    }

    public void initSaleItemFields() {

        this.setOkOtherCost(true);
        itemNameTextField.setEditable(true);
        itemCodeTextField.setEditable(true);
        itemCodeTextField.requestFocusInWindow();
        itemNameTextField.setText("");
        itemCodeTextField.setText("");
        discTypeComboBox.setEditable(false);
        serialNumTextField.setText("");
        serialNumTextField.setEditable(false);
        itemQtyTextField.setEditable(true);
        discTypeComboBox.setEnabled(false);
        itemDiscTextField.setText("");
        itemDiscTextField.setEditable(false);
        itemPriceTextField.setText("");
        itemPriceTextField.setEditable(false);
        itemQtyTextField.setText("");
        itemQtyTextField.setEditable(false);
        itemCodeTextField.requestFocusInWindow();
        //itemNameTextField.requestFocusInWindow();
        itemRemoveButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);
        cancelEditTableButton.setEnabled(false);

    }

    public void cmdItemDelete() {

        int answer = JOptionPane.showConfirmDialog(this, "Are you sure to delete this item?", "Delete Item", JOptionPane.OK_CANCEL_OPTION);

        if (answer == JOptionPane.OK_OPTION) {
            this.getSaleModel().removeSaleDetail(this.getCandidateBilldetail(), this.getCandidateBilldetailCode());
            this.synchronizeModelAndTable();
        }

        this.setICommand(Command.ADD);
        this.initSaleItemFields();
        itemRemoveButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);

    }

    public void cmdItemUpdate() {

        String sku = this.getCandidateBilldetail().getSku();
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure to update this item?", "Update Item", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            this.synchronizeModelAndTable();
        } else if (answer == JOptionPane.CANCEL_OPTION) {

        }
        this.initSaleItemFields();

    }

    public void cmdDetailChoose() {

    }

    public void cmdCountReturn() {
        double tempTotal = this.getSaleModel().getTotalPayments();
        double ntt = this.getSaleModel().getNetTrans();

        StandartRate defaultRate = this.getSaleModel().getRateUsed();
        CashReturn cashReturn = new CashReturn();
        CurrencyType type = (CurrencyType) this.getSaleModel().getCurrencyTypeUsed();
        double dCashReturn = CashierMainApp.getDoubleFromFormated(changeTransTextField.getText());
        cashReturn.setAmount(dCashReturn * defaultRate.getSellingRate());
        cashReturn.setCurrencyId(type.getOID());
        cashReturn.setRate(defaultRate.getSellingRate());
        this.getSaleModel().getCashReturn().clear();
        this.getSaleModel().getCashReturn().put(new Long(cashReturn.getCurrencyId()), cashReturn);
        changeTransTextField.setEditable(true);
        changeTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(dCashReturn));
        this.getSaleModel().getMainSale().setTransactionStatus(PstBillMain.TRANS_STATUS_CLOSE);
        printCashButton.setEnabled(true);
        printCashButton.requestFocusInWindow();
    }

    public void cmdAddPayment() {

        String selectedCur = (String) curTypeComboBox.getSelectedItem();
        CurrencyType type = (CurrencyType) CashierMainApp.getHashCurrencyType().get(selectedCur);
        StandartRate rate = (StandartRate) CashSaleController.getLatestRate(String.valueOf(type.getOID()));

        StandartRate defaultRate = this.getSaleModel().getRateUsed();
        CashPayments newCashPayment = new CashPayments();
        double inputedAmount = 0;
        inputedAmount = CashierMainApp.getDoubleFromFormated(paymentTransTextField.getText());

        double realAmount;
        if (rate.getCurrencyTypeId() != defaultRate.getCurrencyTypeId())
            realAmount = inputedAmount * rate.getSellingRate();
        else
            realAmount = inputedAmount * defaultRate.getSellingRate();


        double payDiff = this.getSaleModel().getPayDiff(); //this.getSaleModel().getLastPayment()-this.getSaleModel().getNetTrans()*defaultRate.getSellingRate();
        double dCashReturn = realAmount + this.getSaleModel().getLastPayment()/*defaultRate.getSellingRate ()*/ - this.getSaleModel().getNetTrans();//*rate.getSellingRate();// - this.getSaleModel().getLastPayment();
        if (payDiff > 0) {
            realAmount = 0;
            dCashReturn = payDiff;
        }

        if (dCashReturn < 0) {
            JOptionPane.showMessageDialog(this, "Payment is not sufficient for this transaction", "Payment", JOptionPane.ERROR_MESSAGE);
            curTypeComboBox.requestFocusInWindow();
            printCashButton.setEnabled(false);
        } else {
            if (this.getSaleModel().getCashPayments().size() <= 1) {
                newCashPayment.setAmount(realAmount);
                newCashPayment.setRate(rate.getSellingRate());
                newCashPayment.setCurrencyId(type.getOID());
                newCashPayment.setPayDateTime(new Date());
                newCashPayment.setUpdateStatus(PstCashPayment.UPDATE_STATUS_INSERTED);
                newCashPayment.setPaymentType(PstCashPayment.CASH);
                this.getSaleModel().getCashPayments().clear();
                this.getSaleModel().getCashPayments().put(newCashPayment.getPayDateTime(), newCashPayment);


                this.getSaleModel().setShouldChange(dCashReturn);
                changeTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(dCashReturn / defaultRate.getSellingRate()));
                changeTransTextField.setEditable(true);
                synchronizeModelAndTable();
                changeTransTextField.requestFocusInWindow();
                paymentTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(inputedAmount));

            }


        }

    }

    public void cmdSetPayment() {

        if (this.getSaleModel().isAnyIncludedCost()) {
            try {
                Hashtable payments = this.getSaleModel().getCashPayments();
                Hashtable paymentInfo = this.getSaleModel().getCreditPayment();
                double netTrans = this.getSaleModel().getNetTrans() - this.getSaleModel().getLastPayment();
                CashSaleController.showPaymentDialog(null, payments, paymentInfo, netTrans, this.getSaleModel().getRateUsed());
                payments = CashSaleController.getPaymentSet();
                paymentInfo = CashSaleController.getPaymentInfoSet();
                double dCc = CashSaleController.getCardCost();
                double dblRateUsed = this.getSaleModel().getRateUsed().getSellingRate();
                if (dblRateUsed == 0) {
                    dblRateUsed = 1;
                }

                if (payments.size() > 0 && paymentInfo.size() > 0) {

                    this.getSaleModel().setCashPayments(payments);
                    this.getSaleModel().setCashPaymentInfo(paymentInfo);
                    this.getSaleModel().setTotCardCost(dCc);
                    double totalP = this.getSaleModel().getTotalPayments();
                    double dOtherCost = this.getSaleModel().getTotOtherCost();
                    double dCashReturn = this.getSaleModel().getCandReturn();
                    this.getSaleModel().setShouldChange(dCashReturn);
                    paymentTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal((totalP + dCc) / dblRateUsed));
                    otherCostTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(dOtherCost / dblRateUsed));
                    changeTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(dCashReturn / dblRateUsed));
                    if (dCc > 0) {
                        totalValueLabel.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal((this.getSaleModel().getNetTrans() + dCc) / dblRateUsed));
                    }
                    paymentTransTextField.setEditable(false);

                    setPaymentButton.setEnabled(true);
                    changeTransTextField.setEditable(true);
                    changeTransTextField.setEnabled(true);
                    boolean bool = this.changeTransTextField.requestFocusInWindow();
                    resetPaymentButton.setEnabled(true);


                    System.out.println("change text field requesting focus result = " + bool);

                } else {
                    paymentTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(0));

                    changeTransTextField.setText(CashierMainApp.getFrameHandler().userFormatStringDecimal(0));

                }
            } catch (Exception e) {
                System.out.println("err on setPayment(): " + e.toString());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please set discounts", "Set value", JOptionPane.WARNING_MESSAGE);
            cmdDoPayment();
        }

    }

    public synchronized void cmdPrintCash() {

        if (this.getSaleModel().isAllValuesCompleted()) {
            if (this.getProcessType() == TRANSACTION_PROCESSING) {
                printCashButton.setEnabled(false); // avoid twice execution
                SessTransactionData.putData(this.getSaleModel());
            } else if (this.getProcessType() == TRANSACTION_REPRINTING || this.getProcessType() == TRANSACTION_HALF_INVOICE) {

            }
            int printAnswer = JOptionPane.showConfirmDialog(this, "Print Invoice?", "Printed", JOptionPane.YES_NO_OPTION);
            switch (printAnswer) {
                case JOptionPane.YES_OPTION:
                    if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_INVOICE
                            && this.getSaleModel().getMainSale().getTransctionType() == PstBillMain.TRANS_TYPE_CASH) {
                        PrintInvoicePOS print = new PrintInvoicePOS();
                        if (this.getSaleModel().getMainSale().getTransactionStatus() == PstBillMain.TRANS_STATUS_CLOSE)
                            if (this.getSaleModel().isIsOpenBillPayment()) {
                                if (CashierMainApp.isUsingBigInvoice()) {
                                    print.printBigInvoiceObj(this.getSaleModel());
                                } else {
                                    print.printOpenBillPaymentObj(this.getSaleModel());
                                }
                            } else {
                                if (CashierMainApp.isUsingBigInvoice()) {
                                    if (this.getProcessType() == TRANSACTION_HALF_INVOICE) {
                                        print.printBigHalfInvoiceObj(this.getSaleModel(), this.getPctHalfInvoice());
                                    } else {
                                        print.printBigInvoiceObj(this.getSaleModel());
                                    }
                                } else {
                                    if (this.getProcessType() == TRANSACTION_HALF_INVOICE) {
                                        print.printHalfInvoiceObj(this.getSaleModel(), this.getPctHalfInvoice());
                                    } else {
                                        print.printInvoiceObj(this.getSaleModel());
                                    }
                                }
                            }
                        else {
                            if (CashierMainApp.isUsingBigInvoice()) {
                                print.printBigOpenBillObj(this.getSaleModel());
                            } else {
                                print.printOpenBillObj(this.getSaleModel());
                            }
                        }
                    } else if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR
                            && this.getSaleModel().getMainSale().getTransctionType() == PstBillMain.TRANS_TYPE_CASH) {
                        if (CashierMainApp.isUsingBigInvoice()) {
                            PrintInvoicePOS print = new PrintInvoicePOS();
                            print.printBigReturnInvoiceObj(this.getSaleModel());
                        } else {
                            PrintIvoiceReturPOS printRetur = new PrintIvoiceReturPOS();
                            printRetur.printInvoiceReturObj(this.getSaleModel());
                        }
                    } else if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_INVOICE
                            && this.getSaleModel().getMainSale().getTransctionType() == PstBillMain.TRANS_TYPE_CREDIT) {
                        PrintInvoicePOS print = new PrintInvoicePOS();
                        if (CashierMainApp.isUsingBigInvoice()) {
                            if (this.getProcessType() == TRANSACTION_HALF_INVOICE) {
                                print.printBigHalfCreditInvoiceObj(this.getSaleModel(), this.getPctHalfInvoice());
                            } else {
                                print.printBigCreditInvoiceObj(this.getSaleModel());
                            }
                        } else {
                            if (this.getProcessType() == TRANSACTION_HALF_INVOICE) {
                                print.printHalfCreditInvoiceObj(this.getSaleModel(), this.getPctHalfInvoice());
                            } else {
                                print.printCreditInvoiceObj(this.getSaleModel());
                            }
                        }
                    }

                    // print credit payment if exists by wpulantara
                    if (this.getSaleModel().getCreditPay() != null) {
                        int printCredit = JOptionPane.showConfirmDialog(this, "Print Credit Payment?", "Printed", JOptionPane.YES_NO_OPTION);
                        if (printCredit == JOptionPane.YES_OPTION) {
                            try {
                                Thread.sleep(1500);
                            } catch (Exception e) {
                                System.out.println("err on Printing Delay: " + e.toString());
                            }
                            PrintInvoicePOS print = new PrintInvoicePOS();
                            if (CashierMainApp.isUsingBigInvoice()) {
                                print.printBigCreditPaymentObj(this.getSaleModel().getCreditPay());
                            } else {
                                print.printCreditPaymentObj(this.getSaleModel().getCreditPay());
                            }
                        }
                    }
                    break;

                case JOptionPane.NO_OPTION:
                    break;
                default:
                    break;
            }
            this.getSaleModel().setHashBillDetail(new Hashtable()); // ignore New Sale confirmation
            if (!CashierMainApp.isAutoNewSale()) {
                JOptionPane.showMessageDialog(this, "Total Transaksi\t: " + CashierMainApp.getCurrencyCodeUsed() + ". " + CashierMainApp.getFrameHandler().userFormatStringDecimal(this.getSaleModel().getNetTrans()) +
                        "\nTotal Paid\t\t: " + CashierMainApp.getCurrencyCodeUsed() + ". " + CashierMainApp.getFrameHandler().userFormatStringDecimal(this.getSaleModel().getTotalPayments()) +
                        "\nTotal Change\t\t: " + CashierMainApp.getCurrencyCodeUsed() + ". " + CashierMainApp.getFrameHandler().userFormatStringDecimal(this.getSaleModel().getShouldChange()) +
                        "\nContinue to New Sale ..... ",
                        "Sale Info", JOptionPane.INFORMATION_MESSAGE);
            }
            this.cmdNewSales();
            this.initAllFields();
        } else {
            JOptionPane.showMessageDialog(this, "Data is incomplete!!", "Incomplete data", JOptionPane.ERROR_MESSAGE);
            salesNameTextField.requestFocusInWindow();
        }

    }

    public void cmdSaveOpenBill() {

        int answer = JOptionPane.showConfirmDialog(this, "Are you sure to save this transaction as open bill?", "Saved As Open Bill", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            this.getSaleModel().getMainSale().setTransactionStatus(PstBillMain.TRANS_STATUS_OPEN);
            this.getSaleModel().getMainSale().setTransctionType(PstBillMain.TRANS_TYPE_CASH);

            // clear all payments
            this.getSaleModel().getCashPayments().clear();
            this.getSaleModel().getCashPaymentInfo().clear();
            this.getSaleModel().getCashReturn().clear();
            // ------------------
            JOptionPane.showMessageDialog(this, "This transaction has converted to open bill", "Open Bill", JOptionPane.INFORMATION_MESSAGE);

            printCashButton.setEnabled(true);
            printCashButton.requestFocusInWindow();
        }

    }

    public void cmdSaveCreditSales() {

        if (this.getSaleModel().isAnyIncludedCost()) {
            MemberReg member = this.getSaleModel().getCustomerServed();
            MemberGroup group = CashSaleController.getGroupOfMember(member.getMemberGroupId());
            if (group.getGroupType() == PstMemberGroup.AGEN) {
                this.getSaleModel().getMainSale().setTransactionStatus(PstBillMain.TRANS_STATUS_OPEN);
                this.getSaleModel().getMainSale().setTransctionType(PstBillMain.TRANS_TYPE_CREDIT);

                // transfer dp to credit payment if exist
                this.getSaleModel().transferDpToCreditPayment();
                // clear all payments
                this.getSaleModel().getCashPayments().clear();
                this.getSaleModel().getCashPaymentInfo().clear();
                this.getSaleModel().getCashReturn().clear();
                // ------------------
                JOptionPane.showMessageDialog(this, "This transaction has converted to credit sale", "Credit sale", JOptionPane.INFORMATION_MESSAGE);

                printCashButton.setEnabled(true);
                printCashButton.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "This customer doesn't allowed to do credit transaction", " Customer Type", JOptionPane.OK_OPTION);
                saveAsCreditSalesButton.setEnabled(false);
                cmdCancel();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please set discounts", "Set values", JOptionPane.WARNING_MESSAGE);
            cmdDoPayment();
        }

    }

    public void cmdSetCreditPayment() {

        CashSaleController.showPaymentDialog(null, null, null, 0, null);

    }

    public boolean cmdCloseWindows() {

        int windowClosingAnswer = JOptionPane.showConfirmDialog(this, "Are you sure to close window and end transaction", "Closing window", JOptionPane.OK_CANCEL_OPTION);
        if (windowClosingAnswer == JOptionPane.OK_OPTION) {
            this.cmdNewSales();
            //this.closeSale();
            this.dispose();
            return true;
        } else
            return false;
    }

    public boolean cmdNewSales() {

        boolean isOk = true;
        if (!this.getSaleModel().getHashBillDetail().isEmpty()) {
            int newSaleConfirm = JOptionPane.showConfirmDialog(this, "Are you sure make a New Sale?", "New Sale Confirmation", JOptionPane.YES_NO_OPTION);
            if (newSaleConfirm == JOptionPane.NO_OPTION)
                isOk = false;
        }
        if (isOk) {
            this.setProcessType(TRANSACTION_PROCESSING);
            this.setSaleModel(null);
            this.setCandidateBilldetail(null);
            this.setCandidateBilldetail(this.getCandidateBilldetail());
            this.setCandidateBilldetailCode(null);
            this.setCandidateBilldetailCode(this.getCandidateBilldetailCode());
            this.setSaleModel(this.getSaleModel());
            this.setMnemonics();
            this.getSaleTableModel().setColumnIdentifiers(this.getColumnIdentifiers());
            this.initAllFields();
        }

        return isOk;
    }

    /**
     * Getter for property transTypeChoosen.
     * @return Value of property transTypeChoosen.
     */
    public int getTransTypeChoosen() {

        return transTypeChoosen;
    }

    /**
     * Setter for property transTypeChoosen.
     * @param transTypeChoosen New value of property transTypeChoosen.
     */
    public void setTransTypeChoosen(int transTypeChoosen) {

        //this.transTypeChoosen = transTypeChoosen;
        //PstBillMain.

    }

    public Vector getColumnIdentifiers() {

        if (columnIdentifiers == null) {
            columnIdentifiers = new Vector();
            columnIdentifiers.add(columnNames[COL_DETAIL_NO]);
            columnIdentifiers.add(columnNames[COL_DETAIL_ITEM_CODE]);
            columnIdentifiers.add(columnNames[COL_DETAIL_ITEM_NAME]);
            columnIdentifiers.add(columnNames[COL_DETAIL_STOCK_CODE]);
            columnIdentifiers.add(columnNames[COL_DETAIL_ITEM_PRICE]);
            columnIdentifiers.add(columnNames[COL_DETAIL_ITEM_DISC]);
            columnIdentifiers.add(columnNames[COL_DETAIL_ITEM_QTY]);
            columnIdentifiers.add(columnNames[COL_DETAIL_ITEM_TOTAL]);

        }

        return columnIdentifiers;
    }

    /**
     * Getter for property saleTableModel.
     * @return Value of property saleTableModel.
     */
    public DefaultTableModel getSaleTableModel() {

        if (saleTableModel == null) {
            saleTableModel = new DefaultTableModel();

            saleTableModel.setDataVector(this.getVctBillDetail(), this.getColumnIdentifiers());
        }

        return saleTableModel;
    }

    /**
     * Setter for property saleTableModel.
     * @param saleTableModel New value of property saleTableModel.
     */
    public void setSaleTableModel(DefaultTableModel saleTableModel) {

        this.saleTableModel = saleTableModel;

    }

    private void setTableModelforTable(DefaultTableModel tableModel) {

        //this.getSaleTableModel().set

    }

    /**
     * Getter for property vctBillDetail.
     * @return Value of property vctBillDetail.
     */
    public Vector getVctBillDetail() {
        if (vctBillDetail == null) {
            vctBillDetail = new Vector();
        }
        return vctBillDetail;
    }

    /**
     * Setter for property vctBillDetail.
     * @param vctBillDetail New value of property vctBillDetail.
     */
    public void setVctBillDetail(Vector vctBillDetail) {
        this.vctBillDetail = vctBillDetail;

    }

    public void cmdChooseCustType() {

        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                if (CashierMainApp.isEnableMembership()) {
                    custTypeComboBox.setEnabled(true);
                    custTypeComboBox.requestFocusInWindow();
                } else {
                    cmdChangeCustType();
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                guestTypeComboBox.setEnabled(true);
                guestTypeComboBox.requestFocusInWindow();
                break;
            default :
                if (CashierMainApp.isEnableMembership()) {
                    custTypeComboBox.setEnabled(true);
                    custTypeComboBox.requestFocusInWindow();
                } else {
                    cmdChangeCustType();
                }
                break;
        }

    }

    public void cmdChooseGuestType() {
        if (guestTypeComboBox.getSelectedIndex() == 0) {
            guestTextField.setEnabled(true);
            guestTextField.setEditable(true);
            guestTextField.requestFocusInWindow();
        } else {
            guestTextField.setEnabled(false);
            guestTextField.setEditable(false);
            this.getSaleModel().setCustomerLink(null);
            if (CashierMainApp.isEnableOpenBill()) {
                coverGuestNameTextField.setEditable(true);
                coverGuestNameTextField.requestFocusInWindow();
            } else {
                cmdCancel();
            }
        }
    }

    public static void showTotalSale() {

        CashClosingBalanceDialog closingBalanceDialog = new CashClosingBalanceDialog(null, true);
        double x = closingBalanceDialog.getToolkit().getScreenSize().getWidth() / 2;
        double y = closingBalanceDialog.getToolkit().getScreenSize().getHeight() / 2;
        double xlength = (double) closingBalanceDialog.getWidth() / 2;
        double ylength = (double) closingBalanceDialog.getHeight() / 2;
        double locx = x - xlength;
        double locy = y - ylength;
        closingBalanceDialog.setLocation((int) locx, (int) locy);

        closingBalanceDialog.setCashCashierId(CashierMainApp.getCashCashier().getOID());
        closingBalanceDialog.setDisplayRequest(CashClosingBalanceDialog.DISPLAY_TOTAL_SALE);
        closingBalanceDialog.show();

    }

    public void cmdSetOtherCost() {
        Hashtable hashOtherCost = this.getSaleModel().getHashOtherCost();
        double dOtherCost = this.getSaleModel().getTotOtherCost();

        CashSaleController.showOtherCostDialog(hashOtherCost, dOtherCost);

        if (this.getSaleModel().getMainSale().getTransactionStatus() == PstBillMain.TRANS_STATUS_OPEN && dOtherCost != CashSaleController.getTotOtherCost()) {
            this.getSaleModel().setIsOpenBillPayment(false);
        }

        hashOtherCost = CashSaleController.getOtherCostChoosen();
        dOtherCost = CashSaleController.getTotOtherCost();

        this.getSaleModel().setHashOtherCost(hashOtherCost);
        this.getSaleModel().setTotOtherCost(dOtherCost);

        this.synchronizeModelAndTable();
    }

    public void cmdContactInput() {
        CashSaleController.showContactInputDialog();
    }

    public void getGlobalKeyListener(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_F1:
                showTotalSale();
                break;
            case KeyEvent.VK_F2:
                if (CashierMainApp.isEnableContactInput()) {
                    cmdContactInput();
                }
                break;
            case KeyEvent.VK_F3:
                cmdReprintInvoice();
                break;
            case KeyEvent.VK_F4:
                if (evt.getSource().equals(saleDetailTable)) {
                    cmdCancel();
                } else {
                    if (editItemButton.isEnabled())
                        cmdEditSaleTable();
                }
                break;
            case KeyEvent.VK_F5:
                cmdNewSales();
                break;
            case KeyEvent.VK_F6:
                if (saveAsCreditButton.isEnabled() && saveAsCreditButton.isVisible())
                    cmdSaveCreditSales();
                else if (saveAsOpenBillButton.isEnabled() && saveAsOpenBillButton.isVisible())
                    cmdSaveOpenBill();
                break;
            case KeyEvent.VK_F7:
                if (CashierMainApp.isEnableOtherCost() && this.isOkOtherCost()) {
                    cmdSetOtherCost();
                }
                break;
            case KeyEvent.VK_F8:
                if (resetPaymentButton.isEnabled())
                    cmdResetPayments();
                break;
            case KeyEvent.VK_F9:
                if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR) {
                    int answer = JOptionPane.showConfirmDialog(this, "Discounts cannot be set for this transaction! \r\n Continue printing?", "Return Error", JOptionPane.OK_CANCEL_OPTION);
                    if (answer == JOptionPane.OK_OPTION) {
                        printCashButton.requestFocusInWindow();
                    } else {
                        cmdCancel();
                    }
                } else {
                    if (this.getProcessType() == TRANSACTION_PROCESSING) {
                        cmdDoPayment();
                    } else {
                        JOptionPane.showMessageDialog(this, "Discount cannot be set for reprinting purpose", "Do Payment Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                break;
            case KeyEvent.VK_F10:

                break;
            case KeyEvent.VK_F11:
                if (this.getSaleModel().getMainSale().getDocType() == PstBillMain.TYPE_RETUR) {
                    int answer = JOptionPane.showConfirmDialog(this, "Discounts cannot be set for this transaction! \r\n Continue printing?", " ", JOptionPane.OK_CANCEL_OPTION);
                    if (answer == JOptionPane.OK_OPTION) {
                        printCashButton.requestFocusInWindow();
                    } else {
                        cmdCancel();
                    }
                } else {
                    if (multiPaymentButton.isEnabled())
                        cmdSetPayment();
                }
                break;
            case KeyEvent.VK_F12:
                if (printCashButton.isEnabled())
                    cmdPrintCash();
                break;
            case KeyEvent.VK_ESCAPE:
                if (evt.getSource().equals(itemNameTextField) || evt.getSource().equals(itemCodeTextField) || evt.getSource().equals(serialNumTextField) || evt.getSource().equals(itemPriceTextField) || evt.getSource().equals(itemDiscTextField) || evt.getSource().equals(discTypeComboBox) || evt.getSource().equals(itemQtyTextField)) {
                    cmdCancel();
                } else if (evt.getSource().equals(salesNameTextField) || evt.getSource().equals(returInvTextField)) {
                    cmdChooseTransType();
                } else if (evt.getSource().equals(memberCodeTextField) || evt.getSource().equals(memberNameTextField)) {
                    cmdChooseCustType();
                } else if (evt.getSource().equals(saleDetailTable)) {
                    cmdCancel();
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

    /**
     * Getter for property candidateBilldetailCode.
     * @return Value of property candidateBilldetailCode.
     */
    public BillDetailCode getCandidateBilldetailCode() {
        if (candidateBilldetailCode == null) {
            candidateBilldetailCode = new BillDetailCode();
        }
        return candidateBilldetailCode;
    }

    /**
     * Setter for property candidateBilldetailCode.
     * @param candidateBilldetailCode New value of property candidateBilldetailCode.
     */
    public void setCandidateBilldetailCode(BillDetailCode candidateBilldetailCode) {
        this.candidateBilldetailCode = candidateBilldetailCode;
    }

    /**
     * Getter for property netTrans.
     * @return Value of property netTrans.
     */
    public double getNetTrans() {
        return netTrans;
    }

    /**
     * Setter for property netTrans.
     * @param netTrans New value of property netTrans.
     */
    public void setNetTrans(double netTrans) {
        this.netTrans = netTrans;
    }

    /**
     * Getter for property iCommand.
     * @return Value of property iCommand.
     */
    public int getICommand() {
        return iCommand;
    }

    /**
     * Setter for property iCommand.
     * @param iCommand New value of property iCommand.
     */
    public void setICommand(int iCommand) {
        this.iCommand = iCommand;
    }

    /**
     * Getter for property materialOfSaleDetail.
     * @return Value of property materialOfSaleDetail.
     */
    public Material getMaterialOfSaleDetail() {

        if (materialOfSaleDetail == null) {
            Material mat = new Material();
            try {
                mat = PstMaterial.fetchExc(this.getCandidateBilldetail().getMaterialId());
            } catch (Exception dbe) {

            }
            materialOfSaleDetail = mat;
        }
        return materialOfSaleDetail;
    }

    /**
     * Setter for property materialOfSaleDetail.
     * @param materialOfSaleDetail New value of property materialOfSaleDetail.
     */
    public void setMaterialOfSaleDetail(Material materialOfSaleDetail) {
        this.materialOfSaleDetail = materialOfSaleDetail;
    }

    /**
     * Getter for property transferInvFrom.
     * @return Value of property transferInvFrom.
     */
    public int getTransferInvFrom() {
        return transferInvFrom;
    }

    /**
     * Setter for property transferInvFrom.
     * @param transferInvFrom New value of property transferInvFrom.
     */
    public void setTransferInvFrom(int transferInvFrom) {
        this.transferInvFrom = transferInvFrom;
    }

    /**
     * Getter for property refPendingOrder.
     * @return Value of property refPendingOrder.
     */
    public PendingOrder getRefPendingOrder() {
        return refPendingOrder;
    }

    /**
     * Setter for property refPendingOrder.
     * @param refPendingOrder New value of property refPendingOrder.
     */
    public void setRefPendingOrder(PendingOrder refPendingOrder) {
        this.refPendingOrder = refPendingOrder;
    }

    /**
     * Getter for property currentSales.
     * @return Value of property currentSales.
     */
    public Sales getCurrentSales() {
        if (currentSales == null) {
            currentSales = new Sales();
        }
        return currentSales;
    }

    /**
     * Setter for property currentSales.
     * @param currentSales New value of property currentSales.
     */
    public void setCurrentSales(Sales currentSales) {
        this.currentSales = currentSales;
    }

    /**
     * Getter for property salesPerson.
     * @return Value of property salesPerson.
     */
    public Sales getSalesPerson() {
        return salesPerson;
    }

    /**
     * Setter for property salesPerson.
     * @param salesPerson New value of property salesPerson.
     */
    public void setSalesPerson(Sales salesPerson) {
        this.salesPerson = salesPerson;
    }

    /**
     * Getter for property processType.
     * @return Value of property processType.
     */
    public int getProcessType() {
        return processType;
    }

    /**
     * Setter for property processType.
     * @param processType New value of property processType.
     */
    public void setProcessType(int processType) {
        this.processType = processType;
    }

    public static void main(String[] args) {
        CashSaleFrame saleFrame = new CashSaleFrame(null, true);
        saleFrame.show();
    }

    /**
     * Getter for property okOtherCost.
     * @return Value of property okOtherCost.
     */
    public boolean isOkOtherCost() {
        return okOtherCost;
    }

    /**
     * Setter for property okOtherCost.
     * @param okOtherCost New value of property okOtherCost.
     */
    public void setOkOtherCost(boolean okOtherCost) {
        this.okOtherCost = okOtherCost;
    }

    /**
     * Getter for property pctHalfInvoice.
     * @return Value of property pctHalfInvoice.
     */
    public double getPctHalfInvoice() {
        return pctHalfInvoice;
    }

    /**
     * Setter for property pctHalfInvoice.
     * @param pctHalfInvoice New value of property pctHalfInvoice.
     */
    public void setPctHalfInvoice(double pctHalfInvoice) {
        this.pctHalfInvoice = pctHalfInvoice;
    }

}
