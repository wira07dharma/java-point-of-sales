
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

public class Marital extends Entity { 

	private String maritalStatus = "";
	private String maritalCode = "";
	private int numOfChildren;

	public String getMaritalStatus(){ 
		return maritalStatus; 
	} 

	public void setMaritalStatus(String maritalStatus){ 
		if ( maritalStatus == null ) {
			maritalStatus = ""; 
		} 
		this.maritalStatus = maritalStatus; 
	} 

	public String getMaritalCode(){ 
		return maritalCode; 
	} 

	public void setMaritalCode(String maritalCode){ 
		if ( maritalCode == null ) {
			maritalCode = ""; 
		} 
		this.maritalCode = maritalCode; 
	} 

	public int getNumOfChildren(){ 
		return numOfChildren; 
	} 

	public void setNumOfChildren(int numOfChildren){ 
		this.numOfChildren = numOfChildren; 
	} 

}
