 

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



package com.dimata.hanoman.entity.masterdata;

 

/* package java */ 

import java.util.Date;



/* package qdep */

import com.dimata.qdep.entity.*;



public class ContactClassAssign extends Entity { 



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



}

