/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Apr 30, 2004
 * Time: 4:28:04 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.common.entity.search;

import java.util.Date;

public class SrcLogger {
    private Date startDate = new Date();
    private Date endDate = new Date();
    private long employeeId = 0;

    public Date getStartDate(){
        return this.startDate;
    }

    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }

    public Date getEndDate(){
        return this.endDate;
    }

    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

   public long getEmployeeId(){
        return this.employeeId;
    }

    public void setEmployeeId(long employeeId){
        this.employeeId = employeeId;
    }

}
