/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.search;

/**
 *
 * @author dwi
 */
public class SrcCrossCheckData {
    
    private int typeArap        = 0;
    private int typeTransaction = 0;
    private int typeCrossCheck	= 0;
    
    public int getTypeArap(){
	return this.typeArap;
    }
    
    public void setTypeArap(int typeArap){
	this.typeArap = typeArap;
    }
    
    public int getTypeTrans(){
	return this.typeTransaction;
    }
    
    public void setTypeTrans(int typeTransaction){
	this.typeTransaction = typeTransaction;
    }
    
    public int getTypeCrossCheck(){
	return this.typeCrossCheck;
    }
    
    public void setTypeCrossCheck(int typeCrossCheck){
	this.typeCrossCheck = typeCrossCheck;
    }
}
