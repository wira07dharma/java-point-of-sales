
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

public class ResignedReason extends Entity { 

	private String resignedReason = "";

	public String getResignedReason(){ 
		return resignedReason; 
	} 

	public void setResignedReason(String resignedReason){ 
		if ( resignedReason == null ) {
			resignedReason = ""; 
		} 
		this.resignedReason = resignedReason; 
	} 

}
