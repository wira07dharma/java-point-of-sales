
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

package com.dimata.harisma.entity.canteen; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MealTime extends Entity { 

	private String mealTime = "";

	public String getMealTime(){ 
		return mealTime; 
	} 

	public void setMealTime(String mealTime){ 
		if ( mealTime == null ) {
			mealTime = ""; 
		} 
		this.mealTime = mealTime; 
	} 

}
