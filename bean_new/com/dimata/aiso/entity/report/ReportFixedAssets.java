/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.report;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author dwi
 */
public class ReportFixedAssets extends Entity{
    
    private String code = "";
    private String name = "";
    private String location = "";
    private Date aqcDate = new Date();
    private int life = 0;
    private int qty = 0;
    private double aqcLastMonth = 0.0;
    private double aqcIncrement = 0.0;
    private double aqcDecrement = 0.0;
    private double aqcThisMonth = 0.0;
    private double depLastMonth = 0.0;
    private double depIncrement = 0.0;
    private double depDecrement = 0.0;
    private double depThisMonth = 0.0;
    private double bookValue = 0.0;
    private long locationId = 0;
    private long periodId = 0;
    private long accountId = 0;
    private long groupId = 0;
    private long depMethodId = 0;
    private long depTypeId = 0;
    private long fixedAssetsId = 0;
    
    
    public String getCode(){
	return this.code;
    }
    
    public void setCode(String code){
	this.code = code;
    }
    
    public String getName(){
	return this.name;
    }
    
    public void setName(String name){
	this.name = name;
    }
    
    public String getLocation(){
	return this.location;
    }
    
    public void setLocation(String location){
	this.location = location;
    }
    
    public Date getAqcDate(){
	return this.aqcDate;
    }
    
    public void setAqcDate(Date aqcDate){
	this.aqcDate = aqcDate;
    }
    
    public int getLife(){
	return this.life;
    }
    
    public void setLife(int life){
	this.life = life;
    }
    
    public int getQty(){
	return this.qty;
    }
    
    public void setQty(int qty){
	this.qty = qty;
    }
    
    public double getAqcLastMonth(){
	return this.aqcLastMonth;
    }
    
    public void setAqcLastMonth(double aqcLastMonth){
	this.aqcLastMonth = aqcLastMonth;
    }
    
    public double getAqcIncrement(){
	return this.aqcIncrement;
    }
    
    public void setAqcIncrement(double aqcIncrement){
	this.aqcIncrement = aqcIncrement;
    }
    
    public double getAqcDecrement(){
	return this.aqcDecrement;
    }
    
    public void setAqcDecrement(double aqcDecrement){
	this.aqcDecrement = aqcDecrement;
    }
    
    public double getAqcThisMonth(){
	return this.aqcThisMonth;
    }
    
    public void setAqcThisMonth(double aqcThisMonth){
	this.aqcThisMonth = aqcThisMonth;
    }
    
    public double getDepLastMonth(){
	return this.depLastMonth;
    }
    
    public void setDepLastMonth(double depLastMonth){
	this.depLastMonth = depLastMonth;
    }
    
    public double getDepIncrement(){
	return this.depIncrement;
    }
    
    public void setDepIncrement(double depIncrement){
	this.depIncrement = depIncrement;
    }
    
    public double getDepDecrement(){
	return this.depDecrement;
    }
    
    public void setDepDecrement(double depDecrement){
	this.depDecrement = depDecrement;
    }
    
    public double getDepThisMonth(){
	return this.depThisMonth;
    }
    
    public void setDepThisMonth(double depThisMonth){
	this.depThisMonth = depThisMonth;
    }
    
    public double getBookValue(){
	return this.bookValue;
    }
    
    public void setBookValue(double bookValue){
	this.bookValue = bookValue;
    }
    
    public long getLocationId(){
	return this.locationId;
    }
    
    public void setLocationId(long locationId){
	this.locationId = locationId;
    }
    
    public long getPeriodId(){
	return this.periodId;
    }
    
    public void setPeriodId(long periodId){
	this.periodId = periodId;
    }
    
    public long getDepMethodId(){
	return this.depMethodId;
    }
    
    public void setDepMethodId(long depMethodId){
	this.depMethodId = depMethodId;
    }
    
    public long getDepTypeId(){
	return this.depTypeId;
    }
    
    public void setDepTypeId(long depTypeId){
	this.depTypeId = depTypeId;
    }
    
    public long getGroupId(){
	return this.groupId;
    }
    
    public void setGroupId(long groupId){
	this.groupId = groupId;
    }
    
    public long getAccountId(){
	return this.accountId;
    }
    
    public void setAccountId(long accountId){
	this.accountId = accountId;
    }
    
    public long getFixedAssetsId(){
	return this.fixedAssetsId;
    }
    
    public void setFixedAssetsId(long fixedAssetsId){
	this.fixedAssetsId = fixedAssetsId;
    }
}
