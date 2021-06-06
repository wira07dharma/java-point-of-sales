
/* Created on 	:  feb 8,2003 10.17 AM
 * 
 * @author  	: lkarunia
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.entity.contact; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import java.io.*;

public class ContactClass extends Entity implements Serializable { 

	private String className = "";
	private String classDescription = "";
    private int classType;
    private String seriesNumber = "";

	public String getClassName(){ 
		return className; 
	} 

	public void setClassName(String className){ 
		if ( className == null ) {
			className = ""; 
		} 
		this.className = className; 
	} 

	public String getClassDescription(){ 
		return classDescription; 
	} 

	public void setClassDescription(String classDescription){ 
		if ( classDescription == null ) {
			classDescription = ""; 
		} 
		this.classDescription = classDescription; 
	} 

    public int getClassType(){ return classType; }

    public void setClassType(int classType){ this.classType = classType; }

    public String getSeriesNumber(){ return seriesNumber; }

    public void setSeriesNumber(String seriesNumber){ this.seriesNumber = seriesNumber; }
    
    public  String getPstClassName() {
       return "com.dimata.common.entity.contact.PstContactClass";
    }
}
