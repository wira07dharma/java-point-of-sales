
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

public class CategoryAppraisal extends Entity { 

	private long groupCategoryId;
	private String category = "";
	private String description = "";
    private boolean recommend = false;

	public long getGroupCategoryId(){ 
		return groupCategoryId; 
	} 

	public void setGroupCategoryId(long groupCategoryId){ 
		this.groupCategoryId = groupCategoryId; 
	}

	public boolean getRecommend(){
		return recommend;
	} 

	public void setRecommend(boolean recommend){
		this.recommend = recommend;
	}

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

}
