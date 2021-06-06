
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.entity.currency; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class CurrencyType extends Entity { 

	private String code = "";
	private String name = "";
	private String description = "";

        /** Holds value of property tabIndex. */
        private int tabIndex;
        
        /** Holds value of property includeInProcess. */
        private int includeInProcess;
        
	public String getCode(){ 
		return code; 
	} 

	public void setCode(String code){ 
		if ( code == null ) {
			code = ""; 
		} 
		this.code = code; 
	} 

	public String getName(){ 
		return name; 
	} 

	public void setName(String name){ 
		if ( name == null ) {
			name = ""; 
		} 
		this.name = name; 
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 

        /** Getter for property tabIndex.
         * @return Value of property tabIndex.
         *
         */
        public int getTabIndex() {
            return this.tabIndex;
        }
        
        /** Setter for property tabIndex.
         * @param tabIndex New value of property tabIndex.
         *
         */
        public void setTabIndex(int tabIndex) {
            this.tabIndex = tabIndex;
        }
        
        /** Getter for property includeInProcess.
         * @return Value of property includeInProcess.
         *
         */
        public int getIncludeInProcess() {
            return this.includeInProcess;
        }
        
        /** Setter for property includeInProcess.
         * @param includeInProcess New value of property includeInProcess.
         *
         */
        public void setIncludeInProcess(int includeInProcess) {
            this.includeInProcess = includeInProcess;
        }
        
}
