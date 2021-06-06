/* Generated by Together */
package com.dimata.pos.entity.billing;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
import java.util.Hashtable;

public class Billdetail extends Entity {

    private long billMainId = 0;
    private long billDetailId = 0; //DUMMY
    private long unitId = 0;
    private long materialId = 0;
    private String itemName = "";
    private double itemPrice = 0.00;
    private int discType = 0;
    private double disc = 0.00;
    private double qty = 0;
    private double totalPrice = 0.00;
    private String sku = "";
    private int materialType = 0;
    private double cost = 0.00;
    private double discPct = 0.00;
    private Hashtable hashBilldetailCode = new Hashtable();
    private BillDetailCode billDetailCode = new BillDetailCode();
    private int updateStatus = 0;
    private double amountAvailForReturn = 0;
    private double qtyStock = 0;
    private double itemPriceStock = 0;
    private double discGlobal = 0;
    private double totalDisc = 0;
    private String note = "";
    private double disc1 = 0.00;
    private double disc2 = 0.00;
    private String noBill = "";
    private Date billMainDate = new Date();
    private long materialDetailId;
    private long supplierId;
    /**
     * UNTUK TOTAL AMOUNT ari_wiweka 20130701
     *
     * @return
     */
    private double totalAmount = 0.00;
    private double qtyIssue = 0.0;

    private double totalCost = 0.00;

    private long barcodeMat = 0;

    //add opie untuk status order dan status print bill item
    private int status = 0;
    private int statusPrint = 0;

    private Date lengthOrder = new Date();
    private Date lengthFinishOrder = new Date();

    private double qtyRequestSo = 0;

    private long tableId = 0;
    private String invoiceNumber = "";
    //ed
    private double berat = 0;
    private int material_jenis = 0; // dummy

    //added by dewok 20180329 for jewelry
    private double susutanWeight = 0;
    private double susutanPrice = 0;
    private double taxPct = 0;
    private double servicePct = 0;
    private double totalTax = 0;
    private double totalService = 0;
    //added by dewok 20180725 for jewelry
    private double additionalWeight = 0;
    private double latestItemPrice = 0;

    public double getAdditionalWeight() {
        return additionalWeight;
    }

    public void setAdditionalWeight(double additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    public double getLatestItemPrice() {
        return latestItemPrice;
    }

    public void setLatestItemPrice(double latestItemPrice) {
        this.latestItemPrice = latestItemPrice;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getSusutanWeight() {
        return susutanWeight;
    }

    public void setSusutanWeight(double susutanWeight) {
        this.susutanWeight = susutanWeight;
    }

    public double getSusutanPrice() {
        return susutanPrice;
    }

    public void setSusutanPrice(double susutanPrice) {
        this.susutanPrice = susutanPrice;
    }

    public double getTaxPct() {
        return taxPct;
    }

    public void setTaxPct(double taxPct) {
        this.taxPct = taxPct;
    }

    public double getServicePct() {
        return servicePct;
    }

    public void setServicePct(double servicePct) {
        this.servicePct = servicePct;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getTotalService() {
        return totalService;
    }

    public void setTotalService(double totalService) {
        this.totalService = totalService;
    }

    public long getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(long billDetailId) {
        this.billDetailId = billDetailId;
    }

    public int getMaterial_jenis() {
        return material_jenis;
    }

    public void setMaterial_jenis(int material_jenis) {
        this.material_jenis = material_jenis;
    }

    public long getBillMainId() {
        return billMainId;
    }

    public void setBillMainId(long billMainId) {
        this.billMainId = billMainId;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getDiscType() {
        return discType;
    }

    public void setDiscType(int discType) {
        this.discType = discType;
    }

    public double getDisc() {
        return disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Getter for property discPct.
     *
     * @return Value of property discPct.
     */
    public double getDiscPct() {
        return discPct;
    }

    /**
     * Setter for property discPct.
     *
     * @param discPct New value of property discPct.
     */
    public void setDiscPct(double discPct) {
        this.discPct = discPct;
    }

    /**
     * Getter for property billDetailCode.
     *
     * @return Value of property billDetailCode.
     */
    public com.dimata.pos.entity.billing.BillDetailCode getBillDetailCode() {
        return billDetailCode;
    }

    /**
     * Setter for property billDetailCode.
     *
     * @param billDetailCode New value of property billDetailCode.
     */
    public void setBillDetailCode(com.dimata.pos.entity.billing.BillDetailCode billDetailCode) {
        this.billDetailCode = billDetailCode;
    }

    /**
     * Getter for property hashBilldetailCode.
     *
     * @return Value of property hashBilldetailCode.
     */
    public Hashtable getHashBilldetailCode() {
        return hashBilldetailCode;
    }

    /**
     * Setter for property hashBilldetailCode.
     *
     * @param hashBilldetailCode New value of property hashBilldetailCode.
     */
    public void setHashBilldetailCode(Hashtable hashBilldetailCode) {
        this.hashBilldetailCode = hashBilldetailCode;
    }

    /**
     * Getter for property updateStatus.
     *
     * @return Value of property updateStatus.
     */
    public int getUpdateStatus() {
        return updateStatus;
    }

    /**
     * Setter for property updateStatus.
     *
     * @param updateStatus New value of property updateStatus.
     */
    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    /**
     * Getter for property amountAvailForReturn.
     *
     * @return Value of property amountAvailForReturn.
     */
    public double getAmountAvailForReturn() {
        return amountAvailForReturn;
    }

    /**
     * Setter for property amountAvailForReturn.
     *
     * @param amountAvailForReturn New value of property amountAvailForReturn.
     */
    public void setAmountAvailForReturn(double amountAvailForReturn) {
        this.amountAvailForReturn = amountAvailForReturn;
    }

    /**
     * Getter for property qtyStock.
     *
     * @return Value of property qtyStock.
     */
    public double getQtyStock() {
        return qtyStock;
    }

    /**
     * Setter for property qtyStock.
     *
     * @param qtyStock New value of property qtyStock.
     */
    public void setQtyStock(double qtyStock) {
        this.qtyStock = qtyStock;
    }

    public double getItemPriceStock() {
        return itemPriceStock;
    }

    public void setItemPriceStock(double itemPriceStock) {
        this.itemPriceStock = itemPriceStock;
    }

    /**
     * Getter for property discGlobal.
     *
     * @return Value of property discGlobal.
     */
    public double getDiscGlobal() {
        return discGlobal;
    }

    /**
     * Setter for property discGlobal.
     *
     * @param discGlobal New value of property discGlobal.
     */
    public void setDiscGlobal(double discGlobal) {
        this.discGlobal = discGlobal;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the disc1
     */
    public double getDisc1() {
        return disc1;
    }

    /**
     * @param disc1 the disc1 to set
     */
    public void setDisc1(double disc1) {
        this.disc1 = disc1;
    }

    /**
     * @return the disc2
     */
    public double getDisc2() {
        return disc2;
    }

    /**
     * @param disc2 the disc2 to set
     */
    public void setDisc2(double disc2) {
        this.disc2 = disc2;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the totalDisc
     */
    public double getTotalDisc() {
        return totalDisc;
    }

    /**
     * @param totalDisc the totalDisc to set
     */
    public void setTotalDisc(double totalDisc) {
        this.totalDisc = totalDisc;
    }

    /**
     * @return the qtyIssue
     */
    public double getQtyIssue() {
        return qtyIssue;
    }

    /**
     * @param qtyIssue the qtyIssue to set
     */
    public void setQtyIssue(double qtyIssue) {
        this.qtyIssue = qtyIssue;
    }

    /**
     * @return the totalCost
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the barcodeMat
     */
    public long getBarcodeMat() {
        return barcodeMat;
    }

    /**
     * @param barcodeMat the barcodeMat to set
     */
    public void setBarcodeMat(long barcodeMat) {
        this.barcodeMat = barcodeMat;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the statusPrint
     */
    public int getStatusPrint() {
        return statusPrint;
    }

    /**
     * @param statusPrint the statusPrint to set
     */
    public void setStatusPrint(int statusPrint) {
        this.statusPrint = statusPrint;
    }

    /**
     * @return the lengthOrder
     */
    public Date getLengthOrder() {
        return lengthOrder;
    }

    /**
     * @param lengthOrder the lengthOrder to set
     */
    public void setLengthOrder(Date lengthOrder) {
        this.lengthOrder = lengthOrder;
    }

    /**
     * @return the lengthFinishOrder
     */
    public Date getLengthFinishOrder() {
        return lengthFinishOrder;
    }

    /**
     * @param lengthFinishOrder the lengthFinishOrder to set
     */
    public void setLengthFinishOrder(Date lengthFinishOrder) {
        this.lengthFinishOrder = lengthFinishOrder;
    }

    /**
     * @return the noBill
     */
    public String getNoBill() {
        return noBill;
    }

    /**
     * @param noBill the noBill to set
     */
    public void setNoBill(String noBill) {
        this.noBill = noBill;
    }

    /**
     * @return the qtyRequestSo
     */
    public double getQtyRequestSo() {
        return qtyRequestSo;
    }

    /**
     * @param qtyRequestSo the qtyRequestSo to set
     */
    public void setQtyRequestSo(double qtyRequestSo) {
        this.qtyRequestSo = qtyRequestSo;
    }

    /**
     * @return the billMainDate
     */
    public Date getBillMainDate() {
        return billMainDate;
    }

    /**
     * @param billMainDate the billMainDate to set
     */
    public void setBillMainDate(Date billMainDate) {
        this.billMainDate = billMainDate;
    }

    public long getMaterialDetailId() {
        return materialDetailId;
    }

    public void setMaterialDetailId(long materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getTableId() {
        return tableId;
    }

    /**
     * @param tableId the tableId to set
     */
    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    /**
     * @return the invoiceNumber
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

}
