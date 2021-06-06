package com.dimata.posbo.entity.warehouse;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatDistribution extends Entity 
{ 
	private long locationId = 0;
	private int locationType = 0;
	private Date distributionDate = new Date();
	private String distributionCode = "";
	private int distributionCodeCounter = 0;
	private int distributionStatus = 0;
	private String remark = "";
	private String invoiceSupplier = "";

	public long getLocationId()
        { 
            return locationId; 
	} 

	public void setLocationId(long locationId)
        { 
            this.locationId = locationId; 
	} 

	public int getLocationType()
        { 
            return locationType; 
	} 

	public void setLocationType(int locationType)
        { 
            this.locationType = locationType; 
	} 

	public Date getDistributionDate()
        {
            return distributionDate;
        } 

	public void setDistributionDate(Date distributionDate)
        {
            this.distributionDate = distributionDate;
        } 

	public String getDistributionCode()
        {
            return distributionCode;
        } 

	public void setDistributionCode(String distributionCode)
        { 
            if ( distributionCode == null ) 
            {
		distributionCode = ""; 
            } 
            this.distributionCode = distributionCode; 
	} 

	public int getDistributionCodeCounter()
        {
            return distributionCodeCounter;
        } 

	public void setDistributionCodeCounter(int distributionCodeCounter)
        {
            this.distributionCodeCounter = distributionCodeCounter;
        } 

	public int getDistributionStatus()
        {
            return distributionStatus;
        } 

	public void setDistributionStatus(int distributionStatus)
        {
            this.distributionStatus = distributionStatus;
        } 

	public String getRemark()
        { 
            return remark; 
	} 

	public void setRemark(String remark)
        { 
            if ( remark == null ) 
            {
		remark = ""; 
            } 
            this.remark = remark; 
	} 

	public String getInvoiceSupplier()
        { 
            return invoiceSupplier; 
	} 

	public void setInvoiceSupplier(String invoiceSupplier)
        { 
            if ( invoiceSupplier == null ) 
            {
		invoiceSupplier = ""; 
            } 
            this.invoiceSupplier = invoiceSupplier; 
	} 

}
