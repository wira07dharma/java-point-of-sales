package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.posbo.entity.masterdata.Costing;
import com.dimata.posbo.entity.masterdata.PstCosting;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;

import java.util.Date;

public class MatCosting extends Entity implements I_LogHistory{
    private long locationId = 0;
    private long costingTo = 0;
    private int locationType = 0;
    private Date costingDate = new Date();
    private String costingCode = "";
    private int costingCodeCounter = 0;
    private int costingStatus = 0;
    private String remark = "";
    private String invoiceSupplier = "";
    private int transferStatus;
    private Date lastUpdate = new Date();
    private long costingId = 0;
    private long cashCashierId = 0;
    private int enableStockFisik = 0;
    private long contactId=0;
    private long documentId=0;

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }
    
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getCostingTo() {
        return costingTo;
    }

    public void setCostingTo(long costingTo) {
        this.costingTo = costingTo;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public Date getCostingDate() {
        return costingDate;
    }

    public void setCostingDate(Date costingDate) {
        this.costingDate = costingDate;
    }

    public String getCostingCode() {
        return costingCode;
    }

    public void setCostingCode(String costingCode) {
        if (costingCode == null) {
            costingCode = "";
        }
        this.costingCode = costingCode;
    }

    public int getCostingCodeCounter() {
        return costingCodeCounter;
    }

    public void setCostingCodeCounter(int costingCodeCounter) {
        this.costingCodeCounter = costingCodeCounter;
    }

    public int getCostingStatus() {
        return costingStatus;
    }

    public void setCostingStatus(int costingStatus) {
        this.costingStatus = costingStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (remark == null) {
            remark = "";
        }
        this.remark = remark;
    }

    public String getInvoiceSupplier() {
        return invoiceSupplier;
    }

    public void setInvoiceSupplier(String invoiceSupplier) {
        if (invoiceSupplier == null) {
            invoiceSupplier = "";
        }
        this.invoiceSupplier = invoiceSupplier;
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

    /**
     * @return the costingId
     */
    public long getCostingId() {
        return costingId;
    }

    /**
     * @param costingId the costingId to set
     */
    public void setCostingId(long costingId) {
        this.costingId = costingId;
    }

    /**
     * @return the cashCashierId
     */
    public long getCashCashierId() {
        return cashCashierId;
    }

    /**
     * @param cashCashierId the cashCashierId to set
     */
    public void setCashCashierId(long cashCashierId) {
        this.cashCashierId = cashCashierId;
    }

    /**
     * @return the enableStockFisik
     */
    public int getEnableStockFisik() {
        return enableStockFisik;
    }

    /**
     * @param enableStockFisik the enableStockFisik to set
     */
    public void setEnableStockFisik(int enableStockFisik) {
        this.enableStockFisik = enableStockFisik;
    }

    // by dyas - 20131126
    // tambah methods getLogDetail untuk mengambil nilai" yang telah diinputkan oleh user yang sebelumnya telah di-set di form FrmMatCosting
    public String getLogDetail(Entity prevDoc) {
        MatCosting prevMatCosting = (MatCosting)prevDoc;
        Location locationAsl = null;
        Location locationTjn = null;
        Costing costing = null;
        String enableStockFisik = "";
        try{
          if(this!=null && getLocationId()!=0 && (prevMatCosting == null || prevMatCosting.getOID()==0 || prevMatCosting.getLocationId() != this.getLocationId() ) )
          {
                locationAsl = PstLocation.fetchExc(getLocationId());
}
            if(this!=null && getCostingTo()!=0 && (prevMatCosting == null || prevMatCosting.getOID()==0 || prevMatCosting.getCostingTo() != this.getCostingTo() ) )
            {
                locationTjn = PstLocation.fetchExc(getCostingTo());
            }
            if(this!=null && getCostingId()!=0){
                costing = PstCosting.fetchExc(getCostingId());
            }
            if(getEnableStockFisik()== 0)
            {
                enableStockFisik = "TIDAK";
            }
            else
            {
                enableStockFisik = "YA";
            }
          //=====================================================================================
        }catch(Exception exc){
  
        }

        return (prevMatCosting == null ||  prevMatCosting.getOID()==0 || prevMatCosting.getLocationId()==0 ||  prevMatCosting.getLocationId() != this.getLocationId() ?
                ("Lokasi Asal : "+ locationAsl.getName() +" ;" ) : "" ) +

                (prevMatCosting == null ||  prevMatCosting.getOID()==0 || prevMatCosting.getCostingTo()==0 ||  prevMatCosting.getCostingTo() != this.getCostingTo() ?
                ("Lokasi Tujuan : "+ locationTjn.getName() +" ;" ) : "" ) +

                (prevMatCosting == null ||  prevMatCosting.getOID()==0 || !Formater.formatDate(prevMatCosting.getCostingDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getCostingDate(), "yyyy-MM-dd"))?
                (" Date Time : " + Formater.formatDate(this.getCostingDate(), "yyyy-MM-dd") + " ;") : "") +

                (prevMatCosting == null ||  prevMatCosting.getOID()==0 || prevMatCosting.getCostingStatus()!= this.getCostingStatus() ?
                (" Status : " + I_DocStatus.fieldDocumentStatus[this.getCostingStatus()] +" ;") : "") +

                (prevMatCosting == null ||  prevMatCosting.getOID()==0 || prevMatCosting.getCostingId()==0 ||  prevMatCosting.getCostingId() != this.getCostingId() ?
                ("Tipe Biaya : "+ costing.getName() +" ;" ) : "" ) +

                ((prevMatCosting == null || prevMatCosting.getOID()==0 || prevMatCosting.getEnableStockFisik()!=this.getEnableStockFisik()) ?
                (" Enable Stock Fisik : " + enableStockFisik + " ;") : "") +

                ((prevMatCosting == null || prevMatCosting.getOID()==0 || prevMatCosting.getRemark()==null || prevMatCosting.getRemark().compareToIgnoreCase(this.getRemark())!=0) ?
                (" Remark : " + this.getRemark()+" ;") : "" );

}

    /**
     * @return the contactId
     */
    public long getContactId() {
        return contactId;
    }

    /**
     * @param contactId the contactId to set
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }



}
