
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.entity.contact; 
 
/* package java */ 
import java.util.Date;
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class ContactClassAssign extends Entity implements Serializable{

	private long contactClassId;
	private long contactId;

	public long getContactClassId(){ 
		return contactClassId; 
	} 

	public void setContactClassId(long contactClassId){ 
		this.contactClassId = contactClassId; 
	} 

	public long getContactId(){ 
		return contactId; 
	} 

	public void setContactId(long contactId){ 
		this.contactId = contactId; 
	}

    public String getPstClassName() {
       return "com.dimata.common.entity.contact.PstContactClassAssign";
    }


}
