
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

package com.dimata.harisma.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class LockerCondition extends Entity { 

	private String condition = "";

	public String getCondition(){ 
		return condition; 
	} 

	public void setCondition(String condition){ 
		if ( condition == null ) {
			condition = ""; 
		} 
		this.condition = condition; 
	} 

}
