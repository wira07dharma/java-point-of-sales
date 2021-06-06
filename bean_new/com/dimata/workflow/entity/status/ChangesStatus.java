
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

public class ChangesStatus extends Entity { 


	public long getAppMappingOid(){ 
		return this.getOID(0); 
	} 

	public void setAppMappingOid(long appMappingOid){ 
		this.setOID(0, appMappingOid); 
	} 

	public long getDocStatusOid(){
		return this.getOID(1); 
	} 

	public void setDocStatusOid(long docStatusOid){
		this.setOID(1, docStatusOid);
	} 

}
