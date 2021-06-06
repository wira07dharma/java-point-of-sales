
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.pos.entity.balance;
 
import com.dimata.qdep.entity.Entity;

/* package java */ 

/* package qdep */

public class SrcMaterial2 extends Entity { 

	
	private String code = "";
	private String name = "";
	
    private double sellingPriceIDR;
   

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

	


    public double getSellingPriceIDR(){ return sellingPriceIDR; }

    public void setSellingPriceIDR(double sellingPriceIDR){ this.sellingPriceIDR = sellingPriceIDR; }

   
}
