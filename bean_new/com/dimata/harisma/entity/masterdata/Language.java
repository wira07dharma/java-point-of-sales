
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

public class Language extends Entity { 

	private String language = "";

	public String getLanguage(){ 
		return language; 
	} 

	public void setLanguage(String language){ 
		if ( language == null ) {
			language = ""; 
		} 
		this.language = language; 
	} 

}
