
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

public class LockerLocation extends Entity { 

	private String location = "";
	private String sex = "";

	public String getLocation(){ 
		return location; 
	} 

	public void setLocation(String location){ 
		if ( location == null ) {
			location = ""; 
		} 
		this.location = location; 
	} 

	public String getSex(){ 
		return sex; 
	} 

	public void setSex(String sex){ 
		if ( sex == null ) {
			sex = ""; 
		} 
		this.sex = sex; 
	} 

}
