
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
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MemberGroup extends Entity { 

	private long discountTypeId;
	private long priceTypeId;
	private String code = "";
	private String name = "";
	private String description = "";

        /** Holds value of property groupType. */
        private int groupType = 1;
        
        //Update By Koyo Royo
        private int typePoint = 0;
        private double pointInCalculate = 0;
        private int viewCustomerType=0;
        
        
	public long getDiscountTypeId(){ 
		return discountTypeId; 
	} 

	public void setDiscountTypeId(long discountTypeId){ 
		this.discountTypeId = discountTypeId; 
	} 

	public long getPriceTypeId(){ 
		return priceTypeId; 
	} 

	public void setPriceTypeId(long priceTypeId){ 
		this.priceTypeId = priceTypeId; 
	} 

	public String getCode(){ 
		return code; 
	} 

	public void setCode(String code){ 
		if ( code == null ) {
			code = ""; 
		} 
		this.code = code; 
	} 

	public String getName(){ 
		return name; 
	} 

	public void setName(String name){ 
		if ( name == null ) {
			name = ""; 
		} 
		this.name = name; 
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 

        /** Getter for property groupType.
         * @return Value of property groupType.
         *
         */
        public int getGroupType() {
            return this.groupType;
        }
        
        /** Setter for property groupType.
         * @param groupType New value of property groupType.
         *
         */
        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

    /**
     * @return the typePoint
     */
    public int getTypePoint() {
        return typePoint;
    }

    /**
     * @param typePoint the typePoint to set
     */
    public void setTypePoint(int typePoint) {
        this.typePoint = typePoint;
    }

    /**
     * @return the pointInCalculate
     */
    public double getPointInCalculate() {
        return pointInCalculate;
    }

    /**
     * @param pointInCalculate the pointInCalculate to set
     */
    public void setPointInCalculate(double pointInCalculate) {
        this.pointInCalculate = pointInCalculate;
    }

    /**
     * @return the viewCustomerType
     */
    public int getViewCustomerType() {
        return viewCustomerType;
    }

    /**
     * @param viewCustomerType the viewCustomerType to set
     */
    public void setViewCustomerType(int viewCustomerType) {
        this.viewCustomerType = viewCustomerType;
    }
        
}
