package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.Date;
import java.util.Vector;

public class SrcMatReturn
{ 

    private Vector returnstatus = new Vector();
    private String returnnumber = "";
    private String vendorname = "";
    private Date returnfromdate = new Date();
    private Date returntodate = new Date();
    private int returndatestatus = 0;
    private int returnsortby = 0;
    private int returnSortByType = 0;
    private int locationType = -1;
    private long locationFrom = 0;
    private int returnReason = -1;
    private long purchaseOrderId=0;
    
    public static String strSortType[][] = 
    {
	   {"Menaik","Menurun"},
	   {"Ascending","Descending"}
    };

    /** Holds value of property typeInvoice. 
     * pencarian return yang invoice tidak blank = true
     * pencarian return yang invoice blank = false
     */
    private boolean typeInvoice = true;
    
    public static Vector getStrSortType(int language)
    {
        Vector result = new Vector(1,1);
        for(int i=0; i<strSortType.length; i++)
        {
            result.add(String.valueOf(strSortType[language][i]));
	}
        return result;
    }

    public Vector getReturnstatus()
    {
	return returnstatus; 
    } 

    public void setReturnstatus(Vector returnstatus)
    {
	this.returnstatus = returnstatus; 
    } 

    public String getReturnnumber()
    { 
	return returnnumber; 
    } 

    public void setReturnnumber(String returnnumber)
    { 
	if ( returnnumber == null ) 
        {
            returnnumber = ""; 
	} 
	this.returnnumber = returnnumber; 
    } 

    public String getVendorname()
    { 
	return vendorname; 
    } 

    public void setVendorname(String vendorname)
    { 
	if ( vendorname == null ) 
        {
            vendorname = ""; 
	} 
	this.vendorname = vendorname; 
    } 

    public Date getReturnfromdate()
    { 
	return returnfromdate; 
    } 

    public void setReturnfromdate(Date returnfromdate)
    { 
	this.returnfromdate = returnfromdate; 
    } 

    public Date getReturntodate()
    { 
	return returntodate; 
    } 

    public void setReturntodate(Date returntodate)
    { 
	this.returntodate = returntodate; 
    } 

    public int getReturndatestatus()
    { 
	return returndatestatus; 
    } 

    public void setReturndatestatus(int returndatestatus)
    { 
	this.returndatestatus = returndatestatus; 
    } 

    public int getReturnsortby()
    { 
	return returnsortby; 
    } 

    public void setReturnsortby(int returnsortby)
    { 
	this.returnsortby = returnsortby; 
    } 

    public int getReturnSortByType()
    { 
        return returnSortByType; 
    }

    public void setReturnSortByType(int returnSortByType)
    { 
        this.returnSortByType = returnSortByType; 
    }
    
    public int getLocationType()
    { 
        return locationType; 
    }

    public void setLocationType(int locationType)
    { 
        this.locationType = locationType; 
    }
    
    public long getLocationFrom()
    { 
        return locationFrom; 
    }

    public void setLocationFrom(long locationFrom)
    { 
        this.locationFrom = locationFrom; 
    }
    
    public int getReturnReason()
    { 
        return returnReason; 
    }
 
    public void setReturnReason(int returnReason)
    { 
        this.returnReason = returnReason; 
    }
    
    /** Getter for property typeInvoice.
     * @return Value of property typeInvoice.
     *
     */
    public boolean getTypeInvoice() {
        return this.typeInvoice;
    }
    
    /** Setter for property typeInvoice.
     * @param typeInvoice New value of property typeInvoice.
     *
     */
    public void setTypeInvoice(boolean typeInvoice) {
        this.typeInvoice = typeInvoice;
    }

    /**
     * @return the purchaseOrderId
     */
    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    /**
     * @param purchaseOrderId the purchaseOrderId to set
     */
    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    
}
