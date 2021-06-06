
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

public class MatCurrency extends Entity 
{ 

	private String code = "";
	private String name = "";
	private double sellingRate = 0.00;
	private double buyingRate = 0.00;

	public String getCode()
        { 
            return code; 
	} 

	public void setCode(String code)
        { 
            if ( code == null ) 
            {
                code = ""; 
            } 
            this.code = code; 
	} 

	public String getName(){ 
		return name; 
	} 

	public void setName(String name)
        { 
            if ( name == null ) 
            {
                name = ""; 
            } 
            this.name = name; 
	} 

	public double getSellingRate()
        { 
            return sellingRate; 
	} 

	public void setSellingRate(double sellingRate)
        { 
            this.sellingRate = sellingRate; 
	} 

	public double getBuyingRate()
        { 
            return buyingRate; 
	} 

	public void setBuyingRate(double buyingRate)
        { 
            this.buyingRate = buyingRate; 
	} 

}
