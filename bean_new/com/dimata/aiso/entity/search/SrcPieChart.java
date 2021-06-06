/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.search;


import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author dwi
 */
public class SrcPieChart {
    private JspWriter writer = null;
    private String sChartRoot = "";
    private HttpSession session = null;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private double dInAmount = 0.0;
    private boolean isLoadNewData = false;
    private int iShortOrientation = 0;
    private int iRecordToGet = 0;
    private String sChartTitle = "";
    private String sOtherLabel = "";
    
    public SrcPieChart(){
    
    }
    
    public SrcPieChart(JspWriter writer, String sChartRoot, HttpSession session, Date startDate, Date endDate,
	    double dInAmount, boolean isLoadNewData, int iShortOrientation, int iRecordToGet, String sChartTitle,
	    String sOtherLabel){
	    
	this.writer = writer;
	this.sChartRoot = sChartRoot;
	this.session = session;
	this.startDate = startDate;
	this.endDate = endDate;
	this.dInAmount = dInAmount;
	this.isLoadNewData = isLoadNewData;
	this.iShortOrientation = iShortOrientation;
	this.iRecordToGet = iRecordToGet;
	this.sChartTitle = sChartTitle;
	this.sOtherLabel = sOtherLabel;
    }
    
    public JspWriter getJspWriter(){
	return this.writer;
    }
    
    public void setJspWriter(JspWriter writer){
	this.writer = writer;
    }
    
    public String getChartRoot(){
	return this.sChartRoot;
    }
    
    public void setChartRoot(String sChartRoot){
	this.sChartRoot = sChartRoot;    
    }
    
    public HttpSession getHttpSession(){
	return this.session;
    }
    
    public void setHttpSession(HttpSession session){
	this.session = session;
    }
    
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
    
    public double getInAmount(){
	return this.dInAmount;
    }
    
    public void setInAmount(double dInAmount){
	this.dInAmount = dInAmount;
    }
    
    public boolean getIsLoadNewData(){
	return this.isLoadNewData;
    }
    
    public void setIsLoadNewData(boolean isLoadNewData){
	this.isLoadNewData = isLoadNewData;
    }
    
    public int getShortOrientation(){
	return this.iShortOrientation;
    }
    
    public void setShortOrientation(int iShortOrientation){
	this.iShortOrientation = iShortOrientation;
    }
    
    public int getRecordToGet(){
	return this.iRecordToGet;
    }
    
    public void setRecordToGet(int iRecordToGet){
	this.iRecordToGet = iRecordToGet;
    }
    
    public String getChartTitle(){
	return this.sChartTitle;
    }
    
    public void setChartTitle(String sChartTitle){
	this.sChartTitle = sChartTitle;
    }
    
    public String getOtherLabel(){
	return this.sOtherLabel;
    }
    
    public void setOtherLabel(String sOtherLabel){
	this.sOtherLabel = sOtherLabel;
    }
}