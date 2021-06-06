
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

public class RecrLanguage extends Entity { 

	private long recrApplicationId;
	private long languageId;
	private int spoken;
	private int written;
	private int reading;

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public long getLanguageId(){ 
		return languageId; 
	} 

	public void setLanguageId(long languageId){ 
		this.languageId = languageId; 
	} 

	public int getSpoken(){ 
		return spoken; 
	} 

	public void setSpoken(int spoken){ 
		this.spoken = spoken; 
	} 

	public int getWritten(){ 
		return written; 
	} 

	public void setWritten(int written){ 
		this.written = written; 
	} 

	public int getReading(){ 
		return reading; 
	} 

	public void setReading(int reading){ 
		this.reading = reading; 
	} 

}
