
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

public class ScheduleCategory extends Entity { 

	private String category = "";
	private String description = "";
        private int categoryType = 0;

	public String getCategory(){ 
		return category; 
	} 

	public void setCategory(String category){ 
		if ( category == null ) {
			category = ""; 
		} 
		this.category = category; 
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 
        
	public int getCategoryType(){ 
		return categoryType; 
	} 

	public void setCategoryType(int categoryType){ 
		this.categoryType = categoryType; 
	}         

}
