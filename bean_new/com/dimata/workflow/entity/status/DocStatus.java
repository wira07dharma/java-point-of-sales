
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

package com.dimata.workflow.entity.status; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class DocStatus extends Entity { 

	private int type;
	private String name = "";
	private String description = "";
    private int docType;

	public int getType(){ 
		return type; 
	} 

	public void setType(int type){ 
		this.type = type; 
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

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 

    public int getDocType(){ return docType; }

    public void setDocType(int docType){ this.docType = docType; }
}
