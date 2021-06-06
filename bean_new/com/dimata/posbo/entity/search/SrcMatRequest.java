
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

package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class SrcMatRequest extends Entity { 

	private Date fromDate;
	private Date endDate;
	private String materialName = "";
	private String materialCode = "";
	private String dfCode = "";
	private int status;
	private int sortBy;
	private long fromLocation;
	private long toLocation;
    private boolean ignoreDate;

	public Date getFromDate(){ 
		return fromDate; 
	} 

	public void setFromDate(Date fromDate){ 
		this.fromDate = fromDate; 
	} 

	public Date getEndDate(){ 
		return endDate; 
	} 

	public void setEndDate(Date endDate){ 
		this.endDate = endDate; 
	} 

	public String getMaterialName(){ 
		return materialName; 
	} 

	public void setMaterialName(String materialName){ 
		if ( materialName == null ) {
			materialName = ""; 
		} 
		this.materialName = materialName; 
	} 

	public String getMaterialCode(){ 
		return materialCode; 
	} 

	public void setMaterialCode(String materialCode){ 
		if ( materialCode == null ) {
			materialCode = ""; 
		} 
		this.materialCode = materialCode; 
	} 

	public String getDfCode(){ 
		return dfCode; 
	} 

	public void setDfCode(String dfCode){ 
		if ( dfCode == null ) {
			dfCode = ""; 
		} 
		this.dfCode = dfCode; 
	} 

	public int getStatus(){ 
		return status; 
	} 

	public void setStatus(int status){ 
		this.status = status; 
	} 

	public int getSortBy(){ 
		return sortBy; 
	} 

	public void setSortBy(int sortBy){ 
		this.sortBy = sortBy; 
	} 

	public long getFromLocation(){ 
		return fromLocation; 
	} 

	public void setFromLocation(long fromLocation){ 
		this.fromLocation = fromLocation; 
	} 

	public long getToLocation(){ 
		return toLocation; 
	} 

	public void setToLocation(long toLocation){ 
		this.toLocation = toLocation; 
	} 

    public boolean getIgnoreDate(){ return ignoreDate; }

    public void setIgnoreDate(boolean ignoreDate){ this.ignoreDate = ignoreDate; }
}
