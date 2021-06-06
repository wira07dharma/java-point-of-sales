
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

package com.dimata.posbo.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class DiscountType extends Entity { 

	private String code = "";
	private String name = "";

	public String getCode(){ 
		return code; 
	} 

	public void setCode(String code){ 
		if ( code == null ) {
			code = ""; 
		} 
		this.code = code; 
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

}
