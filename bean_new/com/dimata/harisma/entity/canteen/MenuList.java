
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

public class MenuList extends Entity { 

	private long menuItemId;
	private Date menuDate;
	private int menuTime;

	public long getMenuItemId(){ 
		return menuItemId; 
	} 

	public void setMenuItemId(long menuItemId){ 
		this.menuItemId = menuItemId; 
	} 

	public Date getMenuDate(){ 
		return menuDate; 
	} 

	public void setMenuDate(Date menuDate){ 
		this.menuDate = menuDate; 
	} 

	public int getMenuTime(){ 
		return menuTime; 
	} 

	public void setMenuTime(int menuTime){ 
		this.menuTime = menuTime; 
	} 

}
