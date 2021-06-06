package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class SrcMatCosting extends Entity 
{

	private String costingCode = "";
	private Date costingDateFrom = new Date();
	private Date costingDateTo = new Date();
	private boolean ignoreDate = false;
	private long costingFrom = 0;
	private long costingTo = 0;
	private int status = -1;
	private int sortBy;

	public String getCostingCode()
        { 
            return costingCode;
	} 

	public void setCostingCode(String costingCode)
        { 
            if ( costingCode == null )
            {
		costingCode = "";
            } 
            this.costingCode = costingCode;
	} 

	public Date getCostingDateFrom()
        { 
            return costingDateFrom;
	} 

	public void setCostingDateFrom(Date costingDateFrom)
        { 
            this.costingDateFrom = costingDateFrom;
	} 

	public Date getCostingDateTo()
        { 
            return costingDateTo;
	} 

	public void setCostingDateTo(Date costingDateTo)
        { 
            this.costingDateTo = costingDateTo;
	} 

	public boolean getIgnoreDate()
        { 
            return ignoreDate; 
	} 

	public void setIgnoreDate(boolean ignoreDate)
        { 
            this.ignoreDate = ignoreDate; 
	} 

	public long getCostingFrom()
        { 
            return costingFrom;
	} 

	public void setCostingFrom(long costingFrom)
        { 
            this.costingFrom = costingFrom;
	} 

	public long getCostingTo()
        { 
            return costingTo;
	} 

	public void setCostingTo(long costingTo)
        { 
            this.costingTo = costingTo;
	} 

	public int getStatus()
        { 
            return status; 
	} 

	public void setStatus(int status)
        { 
            this.status = status; 
	} 

	public int getSortBy()
        { 
            return sortBy; 
	} 

	public void setSortBy(int sortBy)
        { 
            this.sortBy = sortBy; 
	} 

}
