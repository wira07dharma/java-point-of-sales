
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

package com.dimata.workflow.entity.approval; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class DocAppNote extends Entity { 

	private long docappMainOid;
	private Date noteDate;
	private String description = "";

	public long getDocappMainOid(){ 
		return docappMainOid; 
	} 

	public void setDocappMainOid(long docappMainOid){ 
		this.docappMainOid = docappMainOid; 
	} 

	public Date getNoteDate(){ 
		return noteDate; 
	} 

	public void setNoteDate(Date noteDate){ 
		this.noteDate = noteDate; 
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
