package com.dimata.posbo.entity.warehouse;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatConStockOpname extends Entity 
{ 

	private long locationId = 0;
	private Date stockOpnameDate = new Date();
        private String stockOpnameTime = "";
        private String stockOpnameNumber = "";
        private int stockOpnameStatus = 0;
        private String remark = "";
	private long supplierId = 0;
	private long categoryId = 0;
	private long subCategoryId = 0;
       
	public long getLocationId()
        { 
            return locationId; 
	} 

	public void setLocationId(long locationId)
        { 
            this.locationId = locationId; 
	} 

	public Date getStockOpnameDate()
        { 
            return stockOpnameDate; 
	} 

	public void setStockOpnameDate(Date stockOpnameDate)
        { 
            this.stockOpnameDate = stockOpnameDate; 
	} 

        public String getStockOpnameTime()
        { 
            return stockOpnameTime; 
        }

        public void setStockOpnameTime(String stockOpnameTime)
        { 
            this.stockOpnameTime = stockOpnameTime; 
        }

        public String getStockOpnameNumber()
        { 
            return stockOpnameNumber; 
        }

        public void setStockOpnameNumber(String stockOpnameNumber)
        { 
            this.stockOpnameNumber = stockOpnameNumber; 
        }

	public long getSupplierId()
        { 
            return supplierId; 
	} 

	public void setSupplierId(long supplierId)
        { 
            this.supplierId = supplierId; 
	} 

	public long getCategoryId()
        { 
            return categoryId; 
	} 

	public void setCategoryId(long categoryId)
        { 
            this.categoryId = categoryId; 
	} 

	public long getSubCategoryId()
        { 
            return subCategoryId; 
	} 

	public void setSubCategoryId(long subCategoryId)
        { 
            this.subCategoryId = subCategoryId; 
	} 

        public int getStockOpnameStatus(){ return stockOpnameStatus; }

        public void setStockOpnameStatus(int stockOpnameStatus){ this.stockOpnameStatus = stockOpnameStatus; }

        public String getRemark(){ return remark; }

        public void setRemark(String remark){ this.remark = remark; }

}
