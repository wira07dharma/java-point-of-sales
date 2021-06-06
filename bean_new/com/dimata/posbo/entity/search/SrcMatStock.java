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

package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.Date;

public class SrcMatStock{ 


	private long location = 0;
	private long materialgroup = 0;
	private String code = "";
	private long material = 0;
	private int orderby = 0;

	public long getLocation(){ 
		return location; 
	} 

	public void setLocation(long location){ 
		this.location = location; 
	} 

	public long getMaterialgroup(){ 
		return materialgroup; 
	} 

	public void setMaterialgroup(long materialgroup){
		this.materialgroup = materialgroup; 
	} 

	public long getMaterial(){ 
		return material; 
	} 

	public void setMaterial(long material){ 
		this.material = material; 
	} 

	public String getCode(){
		return code;
	} 

	public void setCode(String code){
		this.code = code;
	} 

	public int getOrderby(){ 
		return orderby; 
	} 

	public void setOrderby(int orderby){ 
		this.orderby = orderby; 
	} 

}
