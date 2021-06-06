
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
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

public class Religion extends Entity { 

	private String religion = "";

	public String getReligion(){ 
		return religion; 
	} 

	public void setReligion(String religion){ 
		if ( religion == null ) {
			religion = ""; 
		} 
		this.religion = religion; 
	} 

}
