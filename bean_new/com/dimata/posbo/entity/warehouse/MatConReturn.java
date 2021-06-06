package com.dimata.posbo.entity.warehouse;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatConReturn extends Entity 
{ 

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
        private int transferStatus;
        private String invoiceSupplier = "";
        private Date lastUpdate = new Date();
       
        
    
        
        public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


	public long getLocationId()
        { 
            return locationId; 
	} 

	public void setLocationId(long locationId)
        { 
            this.locationId = locationId; 
	} 

	public int getLocationType()
        { 
            return locationType; 
	} 

	public void setLocationType(int locationType)
        { 
            this.locationType = locationType; 
	} 

	public long getReturnTo()
        { 
            return returnTo; 
	} 

	public void setReturnTo(long returnTo)
        { 
            this.returnTo = returnTo; 
	} 

	public Date getReturnDate()
        { 
            return returnDate; 
	} 

	public void setReturnDate(Date returnDate)
        { 
            this.returnDate = returnDate; 
	} 

	public String getRetCode()
        { 
            return retCode; 
	} 

	public void setRetCode(String retCode)
        { 
            if ( retCode == null ) 
            {
                retCode = ""; 
            } 
            this.retCode = retCode; 
	} 

	public int getRetCodeCnt()
        { 
            return retCodeCnt; 
	} 

	public void setRetCodeCnt(int retCodeCnt)
        { 
            this.retCodeCnt = retCodeCnt; 
	} 

	public int getReturnStatus()
        { 
            return returnStatus; 
	} 

	public void setReturnStatus(int returnStatus)
        { 
            this.returnStatus = returnStatus; 
	} 

	public int getReturnSource()
        { 
            return returnSource; 
	} 

	public void setReturnSource(int returnSource)
        { 
            this.returnSource = returnSource; 
	} 

	public long getSupplierId()
        { 
            return supplierId; 
	} 

	public void setSupplierId(long supplierId)
        { 
            this.supplierId = supplierId; 
	} 

	public long getPurchaseOrderId()
        { 
            return purchaseOrderId; 
	} 

	public void setPurchaseOrderId(long purchaseOrderId)
        { 
            this.purchaseOrderId = purchaseOrderId; 
	} 

	public long getReceiveMaterialId()
        { 
            return receiveMaterialId; 
	} 

	public void setReceiveMaterialId(long receiveMaterialId)
        { 
            this.receiveMaterialId = receiveMaterialId; 
	} 

	public String getRemark()
        { 
            return remark; 
	} 

	public void setRemark(String remark)
        { 
            if ( remark == null ) 
            {
            	remark = ""; 
            } 
            this.remark = remark; 
	} 

	public int getReturnReason()
        { 
            return returnReason; 
	} 

	public void setReturnReason(int returnReason)
        { 
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
            if ( invoiceSupplier == null ) 
            {
            	invoiceSupplier = ""; 
            } 
            this.invoiceSupplier = invoiceSupplier;           
        }
        
}
