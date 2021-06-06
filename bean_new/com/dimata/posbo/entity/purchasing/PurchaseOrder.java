package com.dimata.posbo.entity.purchasing;

/* package java */

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.posbo.db.DBException;
import java.util.Date;
import java.util.Vector;

import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseOrder extends Entity  implements I_LogHistory {
    private long locationId;
    private int locationType = 0;
    private String poCode = "";
    private int poCodeCounter;
    private Date purchDate;
    private Date purchDateRequest;
    private long supplierId;
    private int poStatus;
    private String remark = "";
    // new
    private int termOfPayment = 0;
    private int creditTime = 0;
    private double ppn = 0;
    private long currencyId = 0;
    private String codeRevisi = "";
    private int includeprintprice=0;
    private long userId;
    
    //new include ppn
    private int includePpn = 0;
    
    //new exchange rate
    private double exchangeRate=0.0;
    
    private long categoryId;
    
    

    public String getCodeRevisi() {
        return codeRevisi;
    }

    public void setCodeRevisi(String codeRevisi) {
        this.codeRevisi = codeRevisi;
    }

    private Vector listItem = new Vector();
    
    public Vector getListItem() {
        return listItem;
    }

    public void setListItem(PurchaseOrderItem purhItem) {
        this.listItem.add(purhItem);
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public double getPpn() {
        return ppn;
    }

    public void setPpn(double ppn) {
        this.ppn = ppn;
    }

    public int getCreditTime() {
        return creditTime;
    }

    public void setCreditTime(int creditTime) {
        this.creditTime = creditTime;
    }

    public int getTermOfPayment() {
        return termOfPayment;
    }

    public void setTermOfPayment(int termOfPayment) {
        this.termOfPayment = termOfPayment;
    }

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

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        if (poCode == null) {
            poCode = "";
        }
        this.poCode = poCode;
    }

    public int getPoCodeCounter() {
        return poCodeCounter;
    }

    public void setPoCodeCounter(int poCodeCounter) {
        this.poCodeCounter = poCodeCounter;
    }

    public Date getPurchDate() {
        return purchDate;
    }

    public void setPurchDate(Date purchDate) {
        this.purchDate = purchDate;
    }


    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public int getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(int poStatus) {
        this.poStatus = poStatus;
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

    /**
     * @return the includePpn
     */
    public int getIncludePpn() {
        return includePpn;
    }

    /**
     * @param includePpn the includePpn to set
     */
    public void setIncludePpn(int includePpn) {
        this.includePpn = includePpn;
    }

    /**
     * @return the includeprintprice
     */
    public int getIncludeprintprice() {
        return includeprintprice;
    }

    /**
     * @param includeprintprice the includeprintprice to set
     */
    public void setIncludeprintprice(int includeprintprice) {
        this.includeprintprice = includeprintprice;
    }

    public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        PurchaseOrder prevPo = (PurchaseOrder)prevDoc;
        ContactList contactList = null;
        Location location = null;
        CurrencyType currencyType = null;
        try{
          if(this!=null && getSupplierId()!=0 && (prevPo == null || prevPo.getOID()==0 || prevPo.getSupplierId() != this.getSupplierId() ) )
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
          if(getIncludePpn() == 0)
          {
              includePpnWord = "TIDAK";
          }
          else
          {
              includePpnWord = "YA";
          }
        }catch(Exception exc){
            
        }
        
        //disini pengecekan jika exchange rate nya berubah, ubah juga semua harga detail list nya
        if( (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getExchangeRate() != this.getExchangeRate() )){
            String where = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"='"+prevPo.getOID()+"'";
            Vector listPoItem = PstPurchaseOrderItem.list(0, 0, where, "");
            double exhangeRate = this.getExchangeRate();
            if(listPoItem.size()>0){
                for(int i=0; i<listPoItem.size(); i++) {
                    PurchaseOrderItem poitem = (PurchaseOrderItem)listPoItem.get(i);
                    poitem.setDiscNominal(exhangeRate*(poitem.getDiscNominal()/prevPo.getExchangeRate()));
                    poitem.setDiscount(exhangeRate*(poitem.getDiscount()/prevPo.getExchangeRate()));
                    poitem.setDiscount1(exhangeRate*(poitem.getDiscount1()/prevPo.getExchangeRate()));
                    poitem.setDiscount2(exhangeRate*(poitem.getDiscount2()/prevPo.getExchangeRate()));
                    poitem.setOrgBuyingPrice(exhangeRate*(poitem.getOrgBuyingPrice()/prevPo.getExchangeRate()));
                    poitem.setPrice(exhangeRate*(poitem.getPrice()/prevPo.getExchangeRate()));
                    poitem.setTotal(exhangeRate*(poitem.getTotal()/prevPo.getExchangeRate()));
                    poitem.setCurBuyingPrice(exhangeRate*(poitem.getCurBuyingPrice()/prevPo.getExchangeRate()));
                    try {
                        long oid = PstPurchaseOrderItem.updateExc(poitem);
                    } catch (DBException ex) {
                    }
                }
            }
        }    
        
        return  (prevPo == null ||  prevPo.getOID()==0 || prevPo.getLocationId()==0 ||  prevPo.getLocationId() != this.getLocationId() ?
                ("Location : "+ location.getName() +" ;" ) : "" ) +

                (prevPo == null ||  prevPo.getOID()==0 || !Formater.formatDate(prevPo.getPurchDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getPurchDate(), "yyyy-MM-dd"))?
                (" Date Time : " + Formater.formatDate(this.getPurchDate(), "yyyy-MM-dd") + " ;") : "") +

                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getCurrencyId()!=this.getCurrencyId()?
                (" Currency : " + currencyType.getCode() +" ;" ) : "") +

                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getCodeRevisi()==null ||  prevPo.getCodeRevisi().compareToIgnoreCase(this.getCodeRevisi())!=0 ?
                (" Revision No. : "+ this.getCodeRevisi() +" ;" ) : "") +

                (contactList!=null ?
                (" Supplier : " + contactList.getCompName()+" ;"
                +" Contact : " + contactList.getPersonName()+" ;"
                +" Address : " + contactList.getBussAddress()+" ;"
                +" Telephone. : " + contactList.getTelpNr()+" ;")  :"")   +

                (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getTermOfPayment() != this.getTermOfPayment() ?
                (" Terms : " + PstPurchaseOrder.fieldsPaymentType[this.getTermOfPayment()] +" ;") : "") +
                
                (prevPo == null ||  prevPo.getOID()==0 ||  prevPo.getExchangeRate() != this.getExchangeRate() ?
                (" Exchange Rate : " + Formater.formatNumber(this.getExchangeRate() , "###.###")  +" ;") : "") +

                (prevPo == null ||  prevPo.getOID()==0 || prevPo.getCreditTime() != this.getCreditTime() ?
                (" Days : " + this.getCreditTime() +" ;") : "") +

                ((prevPo == null || prevPo.getOID()==0 || prevPo.getRemark()==null || prevPo.getRemark().compareToIgnoreCase(this.getRemark())!=0) ?
                (" Remark : " + this.getRemark()+" ;") : "" )+

                ((prevPo == null || prevPo.getOID()==0 || prevPo.getIncludePpn()!=this.getIncludePpn()) ?
                (" Include Ppn : " + includePpnWord + " ;") : "") +

                ((prevPo == null || prevPo.getOID()==0 || prevPo.getPpn()!=this.getPpn()) ?
                (" Ppn Sum : " + this.getPpn()+"% ;") : "") +
                /*
                ((prevPo == null || prevPo.getOID()==0 || prevPo.getIncludeprintprice()!=this.getIncludeprintprice()) ?
                (" Include print : " + this.getIncludeprintprice() + " ;") : "") +
                */
                ((prevPo == null || prevPo.getOID()==0 || prevPo.getPoStatus()!=this.getPoStatus()) ?
                (" Status : " + I_DocStatus.fieldDocumentStatus[this.getPoStatus()]) : "" ) ;
//        }
    }

    /**
     * @return the exchangeRate
     */
    public double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * @param exchangeRate the exchangeRate to set
     */
    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * @return the categoryId
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the purchDateRequest
     */
    public Date getPurchDateRequest() {
        return purchDateRequest;
    }

    /**
     * @param purchDateRequest the purchDateRequest to set
     */
    public void setPurchDateRequest(Date purchDateRequest) {
        this.purchDateRequest = purchDateRequest;
    }

    /**
     * @return the supplierName
     */
    
}
