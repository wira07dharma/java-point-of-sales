/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.analysis;

/**
 *
 * @author dwi
 */
public class PieDataSet {
    private String sLabel = "";
    private double dAmount = 0.0;
    
    public String getLabel(){
	return this.sLabel;
    }
    
    public void setLabel(String sLabel){
	this.sLabel = sLabel;
    }
    
    public double getAmount(){
	return this.dAmount;
    }
    
    public void setAmount(double dAmount){
	this.dAmount = dAmount;
    }
}
