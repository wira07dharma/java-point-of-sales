
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

package com.dimata.harisma.entity.attendance; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class LeaveStock extends Entity { 

	private long leavePeriodId;
	private long employeeId;
	private int alAmount;
	private int llAmount;
	private int dpAmount;
	private int addAl;
	private int addLl;
	private int addDp;

	public long getLeavePeriodId(){ 
		return leavePeriodId; 
	} 

	public void setLeavePeriodId(long leavePeriodId){ 
		this.leavePeriodId = leavePeriodId; 
	} 

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public int getAlAmount(){ 
		return alAmount; 
	} 

	public void setAlAmount(int alAmount){ 
		this.alAmount = alAmount; 
	} 

	public int getLlAmount(){ 
		return llAmount; 
	} 

	public void setLlAmount(int llAmount){ 
		this.llAmount = llAmount; 
	} 

	public int getDpAmount(){ 
		return dpAmount; 
	} 

	public void setDpAmount(int dpAmount){ 
		this.dpAmount = dpAmount; 
	} 

	public int getAddAl(){ 
		return addAl; 
	} 

	public void setAddAl(int addAl){ 
		this.addAl = addAl; 
	} 

	public int getAddLl(){ 
		return addLl; 
	} 

	public void setAddLl(int addLl){ 
		this.addLl = addLl; 
	} 

	public int getAddDp(){ 
		return addDp; 
	} 

	public void setAddDp(int addDp){ 
		this.addDp = addDp; 
	} 

}
