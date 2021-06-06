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

package com.dimata.common.entity.periode; 
 
/* package java */ 
import java.util.Date;
import java.io.*;
/* package qdep */
import com.dimata.qdep.entity.*;

public class Periode extends Entity implements Serializable  {

	private int periodeType;
	private String periodeName = "";
	private Date startDate;
	private Date endDate;
	private int status;

    public Date getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(Date lastEntry) {
        this.lastEntry = lastEntry;
    }

    private Date lastEntry;

	public int getPeriodeType(){
		return periodeType; 
	} 

	public void setPeriodeType(int periodeType){ 
		this.periodeType = periodeType; 
	} 

	public String getPeriodeName(){ 
		return periodeName; 
	} 

	public void setPeriodeName(String periodeName){ 
		if ( periodeName == null ) {
			periodeName = ""; 
		} 
		this.periodeName = periodeName;
	} 

	public Date getStartDate(){ 
		return startDate; 
	} 

	public void setStartDate(Date startDate){ 
		this.startDate = startDate; 
	} 

	public Date getEndDate(){ 
		return endDate; 
	} 

	public void setEndDate(Date endDate){ 
		this.endDate = endDate; 
	} 

	public int getStatus(){ 
		return status; 
	} 

	public void setStatus(int status){ 
		this.status = status; 
	}

	public String getPstClassName() {
       return "com.dimata.common.entity.periode.PstPeriode" ;
    }

}
