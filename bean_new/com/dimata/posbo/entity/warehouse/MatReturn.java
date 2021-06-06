package com.dimata.posbo.entity.warehouse;

/* package java */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;

public class MatReturn extends Entity {
    
    private long locationId = 0;
    private int locationType = 0;
    private long returnTo = 0;
    private Date returnDate = new Date();
    private String retCode = "";
    private int retCodeCnt = 0;
    private int returnStatus = 0;
    private int returnSource = 0;
    private long supplierId = 0;
    private long purchaseOrderId = 0;
    private long receiveMaterialId = 0;
    private String remark = "";
    private int returnReason = 0;
    private Date lastUpdate = new Date();
    private long currencyId = 0;
    private double transRate = 0;
    
    public Date getLastUpdate() {
        return lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    /** Holds value of property transferStatus. */
    private int transferStatus;
    
    /** Holds value of property invoiceSupplier. */
    private String invoiceSupplier = "";
    
    public long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    public int getLocationType() {
        return locationType;
    }
    
    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }
    
    public long getReturnTo() {
        return returnTo;
    }
    
    public void setReturnTo(long returnTo) {
        this.returnTo = returnTo;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getRetCode() {
        return retCode;
    }
    
    public void setRetCode(String retCode) {
        if ( retCode == null ) {
            retCode = "";
        }
        this.retCode = retCode;
    }
    
    public int getRetCodeCnt() {
        return retCodeCnt;
    }
    
    public void setRetCodeCnt(int retCodeCnt) {
        this.retCodeCnt = retCodeCnt;
    }
    
    public int getReturnStatus() {
        return returnStatus;
    }
    
    public void setReturnStatus(int returnStatus) {
        this.returnStatus = returnStatus;
    }
    
    public int getReturnSource() {
        return returnSource;
    }
    
    public void setReturnSource(int returnSource) {
        this.returnSource = returnSource;
    }
    
    public long getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
    
    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }
    
    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    
    public long getReceiveMaterialId() {
        return receiveMaterialId;
    }
    
    public void setReceiveMaterialId(long receiveMaterialId) {
        this.receiveMaterialId = receiveMaterialId;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        if ( remark == null ) {
            remark = "";
        }
        this.remark = remark;
    }
    
    public int getReturnReason() {
        return returnReason;
    }
    
    public void setReturnReason(int returnReason) {
        this.returnReason = returnReason;
    }
    
    /** Getter for property transferStatus.
     * @return Value of property transferStatus.
     *
     */
    public int getTransferStatus() {
        return this.transferStatus;
    }
    
    /** Setter for property transferStatus.
     * @param transferStatus New value of property transferStatus.
     *
     */
    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }
    
    /** Getter for property invoiceSupplier.
     * @return Value of property invoiceSupplier.
     *
     */
    public String getInvoiceSupplier() {
        return this.invoiceSupplier;
    }
    
    /** Setter for property invoiceSupplier.
     * @param invoiceSupplier New value of property invoiceSupplier.
     *
     */
    public void setInvoiceSupplier(String invoiceSupplier) {
        if ( invoiceSupplier == null ) {
            invoiceSupplier = "";
        }
        this.invoiceSupplier = invoiceSupplier;
    }
    
    /**
     * Getter for property currencyId.
     * @return Value of property currencyId.
     */
    public long getCurrencyId() {
        return currencyId;
    }
    
    /**
     * Setter for property currencyId.
     * @param currencyId New value of property currencyId.
     */
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }
    
    /**
     * Getter for property transRate.
     * @return Value of property transRate.
     */
    public double getTransRate() {
        return transRate;
    }
    
    /**
     * Setter for property transRate.
     * @param transRate New value of property transRate.
     */
    public void setTransRate(double transRate) {
        this.transRate = transRate;
    }
    
    // by dyas - 20131125
    // tambah methods getLogDetail untuk mengambil nilai" yang telah diinputkan oleh user yang sebelumnya telah di-set di form FrmMatReturn
    public String getLogDetail(Entity prevDoc) {
        MatReturn prevMatReturn = (MatReturn)prevDoc;
        ContactList contactList = null;
        Location location = null;
        CurrencyType currencyType = null;
        MatReceive matreceive = null;
        try{
          if(this!=null && getSupplierId()!=0 && (prevMatReturn == null || prevMatReturn.getOID()==0 || prevMatReturn.getSupplierId() != this.getSupplierId() ) )
          {
              contactList = PstContactList.fetchExc(getSupplierId());
}
          //=====================================================================================
          if(this!=null && getLocationId()!=0 )
          {
            location = PstLocation.fetchExc(getLocationId());
          }
          if(this!=null && getCurrencyId()!=0 )
          {
            currencyType = PstCurrencyType.fetchExc(getCurrencyId());
          }
          if(this!=null && getReceiveMaterialId()!=0 )
          {
            matreceive = PstMatReceive.fetchExc(getReceiveMaterialId());
          }
        }catch(Exception exc){

        }
        
        String invoiceSupplier="";
        try{
            if(matreceive.getInvoiceSupplier()==null && matreceive.getInvoiceSupplier().length()>0){
                invoiceSupplier=matreceive.getInvoiceSupplier();
            }
        }catch(Exception ex){
        
        }
        

        return (prevMatReturn == null ||  prevMatReturn.getOID()==0 || prevMatReturn.getLocationId()==0 ||  prevMatReturn.getLocationId() != this.getLocationId() ?
                ("Location : "+ location.getName() +" ;" ) : "" ) +

                (prevMatReturn == null ||  prevMatReturn.getOID()==0 || !Formater.formatDate(prevMatReturn.getReturnDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getReturnDate(), "yyyy-MM-dd"))?
                (" Date Time : " + Formater.formatDate(this.getReturnDate(), "yyyy-MM-dd") + " ;") : "") +

                (contactList!=null ?
                (" Supplier : " + contactList.getCompName()+" ;"
                +" Contact : " + contactList.getPersonName()+" ;"
                +" Address : " + contactList.getBussAddress()+" ;"
                +" Telephone. : " + contactList.getTelpNr()+" ;")  :"")   +

                (!"".equals(invoiceSupplier) && (prevMatReturn == null ||  prevMatReturn.getOID()==0 || prevMatReturn.getReceiveMaterialId()!=this.getReceiveMaterialId())?
                (" Nota Supplier : " + invoiceSupplier +" ;" ) : "") +

                ((prevMatReturn == null || prevMatReturn.getOID()==0 || prevMatReturn.getReturnStatus()!=this.getReturnStatus()) ?
                (" Status : " + I_DocStatus.fieldDocumentStatus[this.getReturnStatus()]) : "" )+

                (prevMatReturn == null ||  prevMatReturn.getOID()==0 || prevMatReturn.getCurrencyId()!=this.getCurrencyId()?
                (" Currency : " + currencyType.getCode() +" ;" ) : "") +

                (prevMatReturn == null ||  prevMatReturn.getOID()==0 || prevMatReturn.getReturnReason()!= this.getReturnReason() ?
                (" Reason : " + PstMatReturn.strReturnReasonList[0][this.getReturnReason() ]+" ;") : "") +

                ((prevMatReturn == null || prevMatReturn.getOID()==0 || prevMatReturn.getRemark()==null || prevMatReturn.getRemark().compareToIgnoreCase(this.getRemark())!=0) ?
                (" Remark : " + this.getRemark()+" ;") : "" );

    }

}
