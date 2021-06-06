
/* Created on 	:  [25-9-2002] [1.15] PM
 * 
 * @author  	: lkarunia
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class EmpLanguage extends Entity { 

	private long languageId;
	private long employeeId;
	private int oral;
	private int written;
	private String description = "";

	public long getLanguageId(){ 
		return languageId; 
	} 

	public void setLanguageId(long languageId){ 
		this.languageId = languageId; 
	} 

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public int getOral(){ 
		return oral; 
	} 

	public void setOral(int oral){ 
		this.oral = oral; 
	} 

	public int getWritten(){ 
		return written; 
	} 

	public void setWritten(int written){ 
		this.written = written; 
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
