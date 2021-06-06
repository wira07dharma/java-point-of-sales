
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

package com.dimata.posbo.entity.admin.service;
 
/* package java */ 
import java.util.*;
import java.sql.Time;

/* package qdep */
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class Logger extends Entity { 

	private Date dateCreated;
	private Time timeCreated;
	private String target1Note = "";
	private String target2Note = "";
	private String target3Note = "";

	public Date getDateCreated(){ 
		return dateCreated; 
	} 

	public void setDateCreated(Date dateCreated){ 
		this.dateCreated = dateCreated; 
	} 

	public Time getTimeCreated(){ 
		return timeCreated; 
	} 

	public void setTimeCreated(Time timeCreated){ 
		this.timeCreated = timeCreated; 
	} 

	public String getTarget1Note(){ 
		return target1Note; 
	} 

	public void setTarget1Note(String target1Note){ 
		if ( target1Note == null ) {
			target1Note = ""; 
		} 
		this.target1Note = target1Note; 
	} 

	public String getTarget2Note(){ 
		return target2Note; 
	} 

	public void setTarget2Note(String target2Note){ 
		if ( target2Note == null ) {
			target2Note = ""; 
		} 
		this.target2Note = target2Note; 
	} 

	public String getTarget3Note(){ 
		return target3Note; 
	} 

	public void setTarget3Note(String target3Note){ 
		if ( target3Note == null ) {
			target3Note = ""; 
		} 
		this.target3Note = target3Note; 
	} 
        
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         Logger logger = PstLogger.fetchExc(oid);
         object.put(PstLogger.fieldNames[PstLogger.FLD_DATE_CREATED], logger.getDateCreated());
         object.put(PstLogger.fieldNames[PstLogger.FLD_TIME_CREATED], logger.getTimeCreated());
         object.put(PstLogger.fieldNames[PstLogger.FLD_TARGET1_NOTE], logger.getTarget1Note());
         object.put(PstLogger.fieldNames[PstLogger.FLD_TARGET2_NOTE], logger.getTarget2Note());
         object.put(PstLogger.fieldNames[PstLogger.FLD_TARGET3_NOTE], logger.getTarget3Note());
      }catch(Exception exc){}
      return object;
   }
}
