
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

public class SrcMatEndStock{ 

	private long periode = 0;
	private long location = 0;
	private long materialgroup = 0;
	private long materialname = 0;
	private String materialcode = "";
	private int orderby = 0;

	public long getPeriode(){
		return periode;
	} 

	public void setPeriode(long periode){
		this.periode = periode; 
	} 

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

	public String getMaterialcode(){
		return materialcode; 
	} 

	public void setMaterialcode(String materialcode){ 
		if ( materialcode == null ) {
			materialcode = ""; 
		} 
		this.materialcode = materialcode; 
	} 

	public long getMaterialname(){
		return materialname; 
	} 

	public void setMaterialname(long materialname){ 
		this.materialname = materialname; 
	} 

	public int getOrderby(){ 
		return orderby; 
	} 

	public void setOrderby(int orderby){ 
		this.orderby = orderby; 
	} 

}
