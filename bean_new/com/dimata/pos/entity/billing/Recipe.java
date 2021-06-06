/*
 * Recipe2.java
 *
 * Created on May 18, 2004, 2:01 PM
 */

package com.dimata.pos.entity.billing;

import com.dimata.qdep.entity.Entity;

/* package qdep */

public class Recipe extends Entity { 

	private long cashBillMainId;
	private String recipeNumber = "";
	private String doctorName = "";
	private String patientName = "";
	private double recipeService;

	public long getCashBillMainId(){ 
		return cashBillMainId; 
	} 

	public void setCashBillMainId(long cashBillMainId){ 
		this.cashBillMainId = cashBillMainId; 
	} 

	public String getRecipeNumber(){ 
		return recipeNumber; 
	} 

	public void setRecipeNumber(String recipeNumber){ 
		if ( recipeNumber == null ) {
			recipeNumber = ""; 
		} 
		this.recipeNumber = recipeNumber; 
	} 

	public String getDoctorName(){ 
		return doctorName; 
	} 

	public void setDoctorName(String doctorName){ 
		if ( doctorName == null ) {
			doctorName = ""; 
		} 
		this.doctorName = doctorName; 
	} 

	public String getPatientName(){ 
		return patientName; 
	} 

	public void setPatientName(String patientName){ 
		if ( patientName == null ) {
			patientName = ""; 
		} 
		this.patientName = patientName; 
	} 

	public double getRecipeService(){ 
		return recipeService; 
	} 

	public void setRecipeService(double recipeService){ 
		this.recipeService = recipeService; 
	} 

    
}
