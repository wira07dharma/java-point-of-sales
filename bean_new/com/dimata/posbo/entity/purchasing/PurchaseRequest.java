/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.purchasing;

import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dimata005
 * update 20140303 untuk term of payment
 */
public class PurchaseRequest extends Entity  implements I_LogHistory{

    /**
     * @return the requestSource
     */
    public int getRequestSource() {
        return requestSource;
    }

    /**
     * @param requestSource the requestSource to set
     */
    public void setRequestSource(int requestSource) {
        this.requestSource = requestSource;
    }
    private long locationId;
    private String prCode = "";
    private int peCodeCounter;
    private Date purchRequestDate = null;
    private int prStatus;
    private String remark = "";
    private String locationName="";
    private int termOfPayment=0;
    private long warehouseSupplierId=0;
    private Vector listItem = new Vector();
    private long currencyId=0;
    private double exhangeRate=0.0;
    private String nomorBc = "";
    private String jenisDocument = "";
    private int requestSource = 0;
    
    
    
    public static final int TYPE_SOURCE_PURCHASE = 0;
    public static final int TYPE_SOURCE_TRANSFER = 1;
	
    
    public Vector getListItem() {
        return listItem;
    }

    public void setListItem(PurchaseRequestItem purhItem) {
        this.listItem.add(purhItem);
    }
    
    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the prCode
     */
    public String getPrCode() {
        return prCode;
    }

    /**
     * @param prCode the prCode to set
     */
    public void setPrCode(String prCode) {
        this.prCode = prCode;
    }

    /**
     * @return the peCodeCounter
     */
    public int getPeCodeCounter() {
        return peCodeCounter;
    }

    /**
     * @param peCodeCounter the peCodeCounter to set
     */
    public void setPeCodeCounter(int peCodeCounter) {
        this.peCodeCounter = peCodeCounter;
    }

    /**
     * @return the purchRequestDate
     */
    public Date getPurchRequestDate() {
        return purchRequestDate;
    }

    /**
     * @param purchRequestDate the purchRequestDate to set
     */
    public void setPurchRequestDate(Date purchRequestDate) {
        this.purchRequestDate = purchRequestDate;
    }

    /**
     * @return the prStatus
     */
    public int getPrStatus() {
        return prStatus;
    }

    /**
     * @param prStatus the prStatus to set
     */
    public void setPrStatus(int prStatus) {
        this.prStatus = prStatus;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getLogDetail(Entity prevDoc) {
        PurchaseRequest prevPr = (PurchaseRequest)prevDoc;
        ContactList contactList = null;
        Location location = null;
        
          //=====================================================================================
          if(this!=null && getLocationId()!=0 )
          {
            try {
                location = PstLocation.fetchExc(getLocationId());
            } catch (DBException ex) {
                Logger.getLogger(PurchaseRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
          }

         return  (prevPr == null ||  prevPr.getOID()==0 || prevPr.getLocationId()==0 ||  prevPr.getLocationId() != this.getLocationId() ?
                ("Location : "+ location.getName() +" ;" ) : "" ) +

                (prevPr == null ||  prevPr.getOID()==0 || !Formater.formatDate(prevPr.getPurchRequestDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getPurchRequestDate(), "yyyy-MM-dd"))?
                (" Date Time : " + Formater.formatDate(this.getPurchRequestDate(), "yyyy-MM-dd") + " ;") : "") +

                ((prevPr == null || prevPr.getOID()==0 || prevPr.getRemark()==null || prevPr.getRemark().compareToIgnoreCase(this.getRemark())!=0) ?
                (" Remark : " + this.getRemark()+" ;") : "" )+

                ((prevPr == null || prevPr.getOID()==0 || prevPr.getPrStatus()!=this.getPrStatus()) ?
                (" Status : " + I_DocStatus.fieldDocumentStatus[this.getPrStatus()]) : "" ) ;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the termOfPayment
     */
    public int getTermOfPayment() {
        return termOfPayment;
    }

    /**
     * @param termOfPayment the termOfPayment to set
     */
    public void setTermOfPayment(int termOfPayment) {
        this.termOfPayment = termOfPayment;
    }

    /**
     * @return the warehouseSupplierId
     */
    public long getWarehouseSupplierId() {
        return warehouseSupplierId;
    }

    /**
     * @param warehouseSupplierId the warehouseSupplierId to set
     */
    public void setWarehouseSupplierId(long warehouseSupplierId) {
        this.warehouseSupplierId = warehouseSupplierId;
    }

    /**
     * @return the currencyId
     */
    public long getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the exhangeRate
     */
    public double getExhangeRate() {
        return exhangeRate;
    }

    /**
     * @param exhangeRate the exhangeRate to set
     */
    public void setExhangeRate(double exhangeRate) {
        this.exhangeRate = exhangeRate;
    }
 /**
   * @return the nomorBc
   */
  public String getNomorBc() {
    return nomorBc;
  }

  /**
   * @param nomorBc the nomorBc to set
   */
  public void setNomorBc(String nomorBc) {
    this.nomorBc = nomorBc;
  }

  /**
   * @return the jenisDocument
   */
  public String getJenisDocument() {
    return jenisDocument;
  }

  /**
   * @param jenisDocument the jenisDocument to set
   */
  public void setJenisDocument(String jenisDocument) {
    this.jenisDocument = jenisDocument;
  }

    
}


