
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

package com.dimata.common.entity.currency;
 
/* package java */ 
import java.util.Date;
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class CurrencyRate extends Entity implements Serializable{

	private Date date;
	private double value;

	public Date getDate(){ 
		return date; 
	} 

	public void setDate(Date date){ 
		this.date = date; 
	} 

	public double getValue(){ 
		return value; 
	} 

	public void setValue(double value){ 
		this.value = value; 
	} 

    public String getPstClassName() {
       return "com.dimata.common.entity.currency.PstCurrencyRate";
    }
}
