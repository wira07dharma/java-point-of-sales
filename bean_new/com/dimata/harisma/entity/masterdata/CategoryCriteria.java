
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

public class CategoryCriteria extends Entity { 

	private long categoryAppraisalId;
	private String criteria = "";

	public long getCategoryAppraisalId(){ 
		return categoryAppraisalId; 
	} 

	public void setCategoryAppraisalId(long categoryAppraisalId){ 
		this.categoryAppraisalId = categoryAppraisalId; 
	} 

	public String getCriteria(){ 
		return criteria; 
	} 

	public void setCriteria(String criteria){ 
		if ( criteria == null ) {
			criteria = ""; 
		} 
		this.criteria = criteria; 
	} 

}
