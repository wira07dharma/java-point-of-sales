
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

public class PrevLeave extends Entity { 

	private int dpLm;
	private int dpAdd;
	private int dpTaken;
	private int dpBal;
	private int alLm;
	private int alAdd;
	private int alTaken;
	private int alBal;
	private int llLm;
	private int llAdd;
	private int llTaken;
	private int llBal;
    private int month;

	public int getDpLm(){ 
		return dpLm; 
	} 

	public void setDpLm(int dpLm){ 
		this.dpLm = dpLm; 
	} 

	public int getDpAdd(){ 
		return dpAdd; 
	} 

	public void setDpAdd(int dpAdd){ 
		this.dpAdd = dpAdd; 
	} 

	public int getDpTaken(){ 
		return dpTaken; 
	} 

	public void setDpTaken(int dpTaken){ 
		this.dpTaken = dpTaken; 
	} 

	public int getDpBal(){ 
		return dpBal; 
	} 

	public void setDpBal(int dpBal){ 
		this.dpBal = dpBal; 
	} 

	public int getAlLm(){ 
		return alLm; 
	} 

	public void setAlLm(int alLm){ 
		this.alLm = alLm; 
	} 

	public int getAlAdd(){ 
		return alAdd; 
	} 

	public void setAlAdd(int alAdd){ 
		this.alAdd = alAdd; 
	} 

	public int getAlTaken(){ 
		return alTaken; 
	} 

	public void setAlTaken(int alTaken){ 
		this.alTaken = alTaken; 
	} 

	public int getAlBal(){ 
		return alBal; 
	} 

	public void setAlBal(int alBal){ 
		this.alBal = alBal; 
	} 

	public int getLlLm(){ 
		return llLm; 
	} 

	public void setLlLm(int llLm){ 
		this.llLm = llLm; 
	} 

	public int getLlAdd(){ 
		return llAdd; 
	} 

	public void setLlAdd(int llAdd){ 
		this.llAdd = llAdd; 
	} 

	public int getLlTaken(){ 
		return llTaken; 
	} 

	public void setLlTaken(int llTaken){ 
		this.llTaken = llTaken; 
	} 

	public int getLlBal(){ 
		return llBal; 
	} 

	public void setLlBal(int llBal){ 
		this.llBal = llBal; 
	} 

    public int getMonth(){ return month; }

    public void setMonth(int month){ this.month = month; }
}
