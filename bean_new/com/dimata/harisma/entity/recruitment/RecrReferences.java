
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

public class RecrReferences extends Entity { 

	private long recrApplicationId;
	private String name = "";
	private String company = "";
	private String position = "";

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public String getName(){ 
		return name; 
	} 

	public void setName(String name){ 
		if ( name == null ) {
			name = ""; 
		} 
		this.name = name; 
	} 

	public String getCompany(){ 
		return company; 
	} 

	public void setCompany(String company){ 
		if ( company == null ) {
			company = ""; 
		} 
		this.company = company; 
	} 

	public String getPosition(){ 
		return position; 
	} 

	public void setPosition(String position){ 
		if ( position == null ) {
			position = ""; 
		} 
		this.position = position; 
	} 

}
