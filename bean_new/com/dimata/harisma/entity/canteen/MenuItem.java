
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

package com.dimata.harisma.entity.canteen; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MenuItem extends Entity { 

	private String itemName = "";

	public String getItemName(){ 
		return itemName; 
	} 

	public void setItemName(String itemName){ 
		if ( itemName == null ) {
			itemName = ""; 
		} 
		this.itemName = itemName; 
	} 

}
