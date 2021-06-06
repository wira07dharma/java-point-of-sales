
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

package com.dimata.common.entity.custom; 
 
/* package java */ 
import java.util.Date;
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class DataCustom extends Entity implements Serializable { 

	private long ownerId;
	private String dataName = "";
	private String link = "";
	private String dataValue = "";
	private int dataCount = 0;

	public long getOwnerId(){ 
		return ownerId; 
	} 

	public void setOwnerId(long ownerId){ 
		this.ownerId = ownerId; 
	} 

	public String getDataName(){ 
		return dataName; 
	} 

	public void setDataName(String dataName){ 
		if ( dataName == null ) {
			dataName = ""; 
		} 
		this.dataName = dataName; 
	} 

	public String getLink(){ 
		return link; 
	} 

	public void setLink(String link){ 
		if ( link == null ) {
			link = ""; 
		} 
		this.link = link; 
	} 

	public String getDataValue(){ 
		return dataValue; 
	} 

	public void setDataValue(String dataValue){ 
		if ( dataValue == null ) {
			dataValue = ""; 
		} 
		this.dataValue = dataValue; 
	} 

	/**
	 * @return the dataCount
	 */
	public int getDataCount() {
		return dataCount;
	}

	/**
	 * @param dataCount the dataCount to set
	 */
	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}

}
