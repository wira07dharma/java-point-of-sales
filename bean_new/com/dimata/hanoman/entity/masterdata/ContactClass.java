 

/* Created on 	:  [date] [time] AM/PM 

 * 

 * @author  	: karya

 * @version  	: 01

 */



/*******************************************************************

 * Class Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 		: [output ...] 

 *******************************************************************/



package com.dimata.hanoman.entity.masterdata; 

 

/* package java */ 

import java.util.Date;



/* package qdep */

import com.dimata.qdep.entity.*;



public class ContactClass extends Entity { 



	private String className = "";

	private String classDescription = "";

    private int classType;



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

}

