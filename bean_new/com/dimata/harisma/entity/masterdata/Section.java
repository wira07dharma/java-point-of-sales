
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
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

public class Section extends Entity { 

    private long departmentId;
	private String section = "";
	private String description = "";

	public String getSection(){ 
		return section; 
	} 

	public void setSection(String section){ 
		if ( section == null ) {
			section = ""; 
		} 
		this.section = section; 
	}

    public long getDepartmentId(){
		return departmentId;
	} 

	public void setDepartmentId(long departmentId){
		this.departmentId = departmentId;
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
