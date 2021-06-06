/**
 * Created on 	: 3:00 PM
 * @author	    : gedhy
 * @version	    : 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.workflow.entity.approval; 

/* package qdep */
import com.dimata.qdep.entity.*;

public class DocAppDetail extends Entity { 

	private long employeeOid;
	private long docappMainOid;
	private long appMappingOid;
	private int appStatus;

	public long getEmployeeOid(){ 
		return employeeOid; 
	} 

	public void setEmployeeOid(long employeeOid){ 
		this.employeeOid = employeeOid; 
	} 

	public long getDocappMainOid(){ 
		return docappMainOid; 
	} 

	public void setDocappMainOid(long docappMainOid){ 
		this.docappMainOid = docappMainOid; 
	} 

	public long getAppMappingOid(){ 
		return appMappingOid; 
	} 

	public void setAppMappingOid(long appMappingOid){ 
		this.appMappingOid = appMappingOid; 
	} 

	public int getAppStatus(){ 
		return appStatus; 
	} 

	public void setAppStatus(int appStatus){ 
		this.appStatus = appStatus; 
	} 

}
