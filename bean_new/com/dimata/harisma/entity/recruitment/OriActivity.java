
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

public class OriActivity extends Entity { 

	private long oriGroupId;
	private String activity = "";

	public long getOriGroupId(){ 
		return oriGroupId; 
	} 

	public void setOriGroupId(long oriGroupId){ 
		this.oriGroupId = oriGroupId; 
	} 

	public String getActivity(){ 
		return activity; 
	} 

	public void setActivity(String activity){ 
		if ( activity == null ) {
			activity = ""; 
		} 
		this.activity = activity; 
	} 

}
