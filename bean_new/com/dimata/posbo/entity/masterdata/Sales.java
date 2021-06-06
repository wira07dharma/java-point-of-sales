package com.dimata.posbo.entity.masterdata;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Sales extends Entity 
{ 



    private String name = "";
    private String code = "";
    private String remark = "";
    private double commision = 0.0;
    //adding login id & password by mirahu 20120514
    private String loginId = "";
    private String password = "";
    

    /**
     * add opie-eyek 20131128
     */
    private long locationId; //location dimana sales bisa melakukan order
    private long defaultCurrencyId=0;//standart currency yang bisa di pergunakan oleh sales
    //added by dewok
    private long employeeId;
    private int status;
    private long positionId = 0;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public String getName(){
		return name; 
	} 

	public void setName(String name)
        { 
            if ( name == null ) 
            {
                name = ""; 
            } 
            this.name = name; 
	} 

	public String getCode(){ 
		return code; 
	} 

	public void setCode(String code)
        { 
            if ( code == null ) 
            {
                code = ""; 
            } 
            this.code = code; 
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

    /**
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the defaultCurrencyId
     */
    public long getDefaultCurrencyId() {
        return defaultCurrencyId;
    }

    /**
     * @param defaultCurrencyId the defaultCurrencyId to set
     */
    public void setDefaultCurrencyId(long defaultCurrencyId) {
        this.defaultCurrencyId = defaultCurrencyId;
    }
      /**
       * @return the positionId
       */
      public long getPositionId() {
        return positionId;
      }

      /**
       * @param positionId the positionId to set
       */
      public void setPositionId(long positionId) {
        this.positionId = positionId;
      }

}
