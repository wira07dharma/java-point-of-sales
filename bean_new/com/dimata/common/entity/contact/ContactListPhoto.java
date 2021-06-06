
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

public class ContactListPhoto extends Entity implements Serializable{

	private String contactPhoto = "";

	public String getContactPhoto(){ 
		return contactPhoto; 
	} 

	public void setContactPhoto(String contactPhoto){ 
		if ( contactPhoto == null ) {
			contactPhoto = ""; 
		} 
		this.contactPhoto = contactPhoto; 
	} 

    public String getPstClassName() {
       return "com.dimata.common.entity.contact.PstContactListPhoto";
    }

}
