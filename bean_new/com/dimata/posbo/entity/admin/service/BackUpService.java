
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

package com.dimata.posbo.entity.admin.service;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class BackUpService extends Entity { 

	private Date startTime;
	private int periode;
	private String target1 = "";
	private String target2 = "";
	private String target3 = "";
	private String sourcePath = "";

	public Date getStartTime(){ 
		return startTime; 
	} 

	public void setStartTime(Date startTime){ 
		this.startTime = startTime; 
	} 

	public int getPeriode(){ 
		return periode; 
	} 

	public void setPeriode(int periode){ 
		this.periode = periode; 
	} 

	public String getTarget1(){ 
		return target1; 
	} 

	public void setTarget1(String target1){ 
		if ( target1 == null ) {
			target1 = ""; 
		} 
		this.target1 = target1; 
	} 

	public String getTarget2(){ 
		return target2; 
	} 

	public void setTarget2(String target2){ 
		if ( target2 == null ) {
			target2 = ""; 
		} 
		this.target2 = target2; 
	} 

	public String getTarget3(){ 
		return target3; 
	} 

	public void setTarget3(String target3){ 
		if ( target3 == null ) {
			target3 = ""; 
		} 
		this.target3 = target3; 
	} 

	public String getSourcePath(){ 
		return sourcePath; 
	} 

	public void setSourcePath(String sourcePath){ 
		if ( sourcePath == null ) {
			sourcePath = ""; 
		} 
		this.sourcePath = sourcePath; 
	} 

}
