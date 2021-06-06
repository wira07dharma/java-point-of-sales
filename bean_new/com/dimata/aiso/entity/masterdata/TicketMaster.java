/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dwi
 */
public class TicketMaster extends Entity{
    
    private String code = "";
    private String description = "";
    private int type = 0;
    private long lContactId = 0;
    private long accCogsId = 0;
    private long accApId = 0;
    
    public String getCode(){
	return this.code;
    }
    
    public void setCode(String code){
	this.code = code;
    }
    
    public String getDescription(){
	return this.description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }
    
    public int getType(){
	return this.type;
    }
    
    public void setType(int type){
	this.type = type;
    }
    
    public long getContactId(){
	return this.lContactId;
    }
    
    public void setContactId(long lContactId){
	this.lContactId = lContactId;
    }

    public long getAccApId() {
	return accApId;
    }

    public void setAccApId(long accApId) {
	this.accApId = accApId;
    }

    public long getAccCogsId() {
	return accCogsId;
    }

    public void setAccCogsId(long accCogsId) {
	this.accCogsId = accCogsId;
    }
        
}
