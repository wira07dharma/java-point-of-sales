
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

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class FamilyMember extends Entity { 

	private long employeeId;
	private String fullName = "";
	private String relationship = "";
	private Date birthDate;
	private String job = "";
	private String address = "";
    private boolean guaranteed = false;
    private boolean ignoreBirth;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public String getFullName(){ 
		return fullName; 
	} 

	public void setFullName(String fullName){ 
		if ( fullName == null ) {
			fullName = ""; 
		} 
		this.fullName = fullName; 
	} 

	public String getRelationship(){ 
		return relationship; 
	} 

	public void setRelationship(String relationship){ 
		if ( relationship == null ) {
			relationship = ""; 
		} 
		this.relationship = relationship; 
	} 

	public Date getBirthDate(){ 
		return birthDate; 
	} 

	public void setBirthDate(Date birthDate){ 
		this.birthDate = birthDate; 
	} 

	public String getJob(){ 
		return job; 
	} 

	public void setJob(String job){ 
		if ( job == null ) {
			job = ""; 
		} 
		this.job = job; 
	} 

	public String getAddress(){ 
		return address; 
	} 

	public void setAddress(String address){ 
		if ( address == null ) {
			address = ""; 
		} 
		this.address = address; 
	} 

    public boolean getGuaranteed(){ return guaranteed; }

    public void setGuaranteed(boolean guaranteed){ this.guaranteed = guaranteed; }

    public boolean getIgnoreBirth(){
            return ignoreBirth;
        }

    public void setIgnoreBirth(boolean ignoreBirth){
            this.ignoreBirth = ignoreBirth;
        }
}
