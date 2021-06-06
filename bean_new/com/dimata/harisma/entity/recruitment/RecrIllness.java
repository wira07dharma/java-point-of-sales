
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

package com.dimata.harisma.entity.recruitment; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class RecrIllness extends Entity { 

	private String illness = "";

	public String getIllness(){ 
		return illness; 
	} 

	public void setIllness(String illness){ 
		if ( illness == null ) {
			illness = ""; 
		} 
		this.illness = illness; 
	} 

}
