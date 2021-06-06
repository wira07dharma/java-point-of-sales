
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.entity.masterdata; 
 
/* package java */ 
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import java.util.Vector;

public class PriceTypeMapping extends Entity { 

	private double price;
        private double prevPrice;
        private long currencyTypeId;

	public long getPriceTypeId(){ 
		return this.getOID(0); 
	} 

	public void setPriceTypeId(long priceTypeId){ 
		this.setOID(0, priceTypeId); 
	} 

	public long getMaterialId(){ 
		return this.getOID(1); 
	} 

	public void setMaterialId(long materialId){ 
		this.setOID(1, materialId); 
	} 

	public long getStandartRateId(){ 
		return this.getOID(2); 
	} 

	public void setStandartRateId(long standartRateId){ 
		this.setOID(2, standartRateId); 
	} 

	public double getPrice(){ 
		return price; 
	} 

	public void setPrice(double price){ 
		this.price = price; 
	} 

    /**
     * @return the prevPrice
     */
    public double getPrevPrice() {
        return prevPrice;
}

    /**
     * @param prevPrice the prevPrice to set
     */
    public void setPrevPrice(double prevPrice) {
        this.prevPrice = prevPrice;
    }

    // add by fitra 25-04-2014
     public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        Material material = new Material();
        DiscountQtyMapping discountQtyMapping = new DiscountQtyMapping();
        //DiscountType discountType = null;
        MemberGroup memberGroup = new MemberGroup();
        DiscountType discountType = new DiscountType();
        PriceTypeMapping prevPriceTypeMapping = (PriceTypeMapping)prevDoc;
        PriceType priceType = new PriceType();
        StandartRate standartRate = new StandartRate();
         String currency = "";
        
        try {
            if (this.getPriceTypeId() != 0){
                
                priceType = PstPriceType.fetchExc(getPriceTypeId());
                //mencari curreny simbol
                currency = PstStandartRate.getCurrency(prevPriceTypeMapping.getStandartRateId());
                
}
        }catch(Exception exc){
            
            
        }
        
     
        return
        
       (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getPrice()!=this.getPrice()?
       ("Type Harga: "+ priceType.getName() +" ;" ) : "" ) +
                
       (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getPrice()!=this.getPrice() ?
       ("Harga saat ini : " + currency + this.getPrice() +" ;" ) : "" ) + 
                
                
       
                
                
                
        (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getPrice()!=this.getPrice() ?
       ("Harga sebelum di update : " + currency + prevPriceTypeMapping.getPrice() +" ;" ) : "" ) +
         
        (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getMaterialId()!=this.getMaterialId() ?
       ("Material: "+ this.getMaterialId() +" ;" ) : "" ) +
                
                
//      (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 || currency!=null?
//       ("Mata Uang  "+currency+":"+ " ;" ) : "" ) +
         
                
        (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getStandartRateId()!=this.getStandartRateId() ?
       ("Standar rate: "+ this.getStandartRateId() +" ;" ) : "" ); 
    }

    /**
     * @return the currencyTypeId
     */
    public long getCurrencyTypeId() {
        return currencyTypeId;
    }

    /**
     * @param currencyTypeId the currencyTypeId to set
     */
    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }
    
    
    // add by fitra 24-04-2014
    
    
     public String getLogDetail1(Entity prevDoc) {
        String includePpnWord = "";
        Material material = new Material();
        DiscountQtyMapping discountQtyMapping = new DiscountQtyMapping();
        //DiscountType discountType = null;
        MemberGroup memberGroup = new MemberGroup();
        DiscountType discountType = new DiscountType();
        PriceTypeMapping prevPriceTypeMapping = (PriceTypeMapping)prevDoc;
        PriceType priceType = new PriceType();
        StandartRate standartRate = new StandartRate();
         String currency = "";
        
        try {
            if (this.getPriceTypeId() != 0){
                
                priceType = PstPriceType.fetchExc(getPriceTypeId());
                //mencari curreny simbol
                currency = PstStandartRate.getCurrency(this.getStandartRateId());
                
            }
        }catch(Exception exc){
            
            
        }
        
     
        return
        
       (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getPrice()!=this.getPrice()?
       ("Type Harga: "+ priceType.getName() +" ;" ) : "" ) +
                
       (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 ||  prevPriceTypeMapping.getPrice()!=this.getPrice() ?
       ("Harga saat ini : " + currency + this.getPrice() +" ;" ) : "" );
                
                
//      (prevPriceTypeMapping == null ||  prevPriceTypeMapping.getOID()==0 || currency!=null?
//       ("Mata Uang  "+currency+":"+ " ;" ) : "" ) +
         
                
      
    }
    
    

}
