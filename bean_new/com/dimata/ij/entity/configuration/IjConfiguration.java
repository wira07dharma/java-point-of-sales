/* 
 * Created on 	 : Dec 25, 2004  
 * @author	 : Edhy Putra
 * @version	 : 01
 */

package com.dimata.ij.entity.configuration; 
 
// package java
import java.util.Date;

// package qdep
import com.dimata.qdep.entity.*;

public class IjConfiguration extends Entity 
{ 
	private int boSystem = -1;
	private int configGroup = -1;
	private int configItem = -1;
	private int configSelect = -1;                
        private String sIjImplClass = "";
        
	public int getBoSystem(){ 
		return boSystem; 
	} 

	public void setBoSystem(int boSystem){ 
		this.boSystem = boSystem; 
	} 

	public int getConfigGroup(){ 
		return configGroup; 
	} 

	public void setConfigGroup(int configGroup){ 
		this.configGroup = configGroup; 
	} 

	public int getConfigItem(){ 
		return configItem; 
	} 

	public void setConfigItem(int configItem){ 
		this.configItem = configItem; 
	} 

	public int getConfigSelect(){ 
		return configSelect; 
	} 

	public void setConfigSelect(int configSelect){ 
		this.configSelect = configSelect; 
	} 
        
        public String getSIjImplClass() {
            return this.sIjImplClass;
        }
        
        public void setSIjImplClass(String sIjImplClass) {
            this.sIjImplClass = sIjImplClass;
        }
        
}
