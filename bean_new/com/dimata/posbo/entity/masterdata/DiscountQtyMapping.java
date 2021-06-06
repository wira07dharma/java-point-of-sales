/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author PT. Dimata
 */

/* package qdep */
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.qdep.entity.*;

public class DiscountQtyMapping extends Entity {
        private double startQty;
        private double toQty;
        private double discountValue;
        private int discountType;
         // for updated catalog
        private java.util.Date updateDate;
        // add by fitra 25-04-2014
        private long discountTypeId;
        private long currencyTypeId;
        private long locationId;
        private long materialId;

	

	public long getDiscountTypeId(){
		return this.getOID(0);
	}

	public void setDiscountTypeId(long discountTypeId){
		this.setOID(0, discountTypeId);
	}

	public long getCurrencyTypeId(){
		return this.getOID(1);
	}


	public void setCurrencyTypeId(long currencyTypeId){
		this.setOID(1, currencyTypeId);
	}
        public long getLocationId(){
		return this.getOID(2);
	}

	public void setLocationId(long LocationId){
		this.setOID(2, LocationId);
	}

        public long getMaterialId(){
		return this.getOID(3);
	}

	public void setMaterialId(long materialId){
		this.setOID(3, materialId);
	}

        
	public int getDiscountType(){
		return discountType;
	}

	public void setDiscountType(int discountType){
		this.discountType = discountType;
	}

	

    /**
     * @return the startQty
     */
    public double getStartQty() {
        return startQty;
    }

    /**
     * @param startQty the startQty to set
     */
    public void setStartQty(double startQty) {
        this.startQty = startQty;
    }

    /**
     * @return the toQty
     */
    public double getToQty() {
        return toQty;
    }

    /**
     * @param toQty the toQty to set
     */
    public void setToQty(double toQty) {
        this.toQty = toQty;
    }

    /**
     * @return the discountValue
     */
    public double getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the discountTypeId
     */
   // public long getDiscountTypeId() {
       // return discountTypeId;
   // }

    /**
     * @param discountTypeId the discountTypeId to set
     */
    //public void setDiscountTypeId(long discountTypeId) {
       // this.discountTypeId = discountTypeId;
   // }

    /**
     * @return the currencyTypeId
     */
    //public long getCurrencyTypeId() {
        //return currencyTypeId;
   // }

    /**
     * @param currencyTypeId the currencyTypeId to set
     */
   // public void setCurrencyTypeId(long currencyTypeId) {
       // this.currencyTypeId = currencyTypeId;
    //}

    /**
     * @return the locationId
     */
    //public long getLocationId() {
        //return locationId;
   // }

    /**
     * @param locationId the locationId to set
     */
   // public void setLocationId(long locationId) {
        //this.locationId = locationId;
    //}

    /**
     * @return the materialId
     */
    //public long getMaterialId() {
        //return materialId;
   // }

    /**
     * @param materialId the materialId to set
     */
    //public void setMaterialId(long materialId) {
        //this.materialId = materialId;
    //}

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    // add by fitra 01-05-2014
     public String getLogDetail(Entity prevDoc, long location, long typeMemberDiscId) {
        String includePpnWord = "";
        Material material = new Material();
        DiscountQtyMapping prevDiscountQtyMapping = (DiscountQtyMapping)prevDoc;
        //DiscountType discountType = null;
        MemberGroup memberGroup = new MemberGroup();
        DiscountType discountType = new DiscountType();
        Location locationx = new Location();
        
        
        try{
            if (location != 0){
                
              locationx = PstLocation.fetchExc(location); 
}
            
        }catch (Exception exc){
            
        }
        
        
         
        try{
            if (typeMemberDiscId!= 0){
                
              discountType = PstDiscountType.fetchExc(typeMemberDiscId); 
            }
            
        }catch (Exception exc){
            
        }
        
        
     
        return
        
       (prevDiscountQtyMapping == null ||  prevDiscountQtyMapping.getOID()==0 ||  prevDiscountQtyMapping.getStartQty()!=this.getStartQty() ?
       ("Start: "+ this.getStartQty() +" ;" ) : "" ) +
                
       (prevDiscountQtyMapping == null ||  prevDiscountQtyMapping.getOID()==0 ||  prevDiscountQtyMapping.getToQty()!=this.getToQty() ?
       ("To Qty : "+ this.getToQty() +" ;" ) : "" ) + 
                
       (prevDiscountQtyMapping == null ||  prevDiscountQtyMapping.getOID()==0 ||  prevDiscountQtyMapping.getDiscountValue()!=this.getDiscountValue() ?
       ("Discount: "+ prevDiscountQtyMapping.getDiscountValue() +" ;" ) : "" ) +
         
       (prevDiscountQtyMapping == null ||  prevDiscountQtyMapping.getOID()==0 ||  prevDiscountQtyMapping.getLocationId()!=this.getLocationId()  ?
       ("lokasi: "+ locationx.getName() +" ;" ) : "" ) +
                
                
       (prevDiscountQtyMapping == null ||  prevDiscountQtyMapping.getOID()==0 ||  prevDiscountQtyMapping.getDiscountTypeId()!=this.getDiscountTypeId() ?
       ("Type Member: "+ discountType.getName() +" ;" ) : "" ) +
         
        
       (prevDiscountQtyMapping == null ||  prevDiscountQtyMapping.getOID()==0 ||  prevDiscountQtyMapping.getDiscountType()!=this.getDiscountType() ?
       ("Discount Type: "+ this.getDiscountType() +" ;" ) : "" ); 
        
    }

    
    

}
