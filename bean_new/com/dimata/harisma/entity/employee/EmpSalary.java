
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

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class EmpSalary extends Entity { 

	private long employeeId;
	private int los1;
	private int los2;
	private double currBasic;
	private double currTransport;
	private double currTotal;
	private double newBasic;
	private double newTransport;
	private double newTotal;
	private double incSalary;
	private double incTransport;
	private double additional;
	private double incTotal;
	private double percentageBasic;
	private double percentTransport;
	private double percentageTotal;
    private Date currDate;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public int getLos1(){ 
		return los1; 
	} 

	public void setLos1(int los1){ 
		this.los1 = los1; 
	} 

	public int getLos2(){ 
		return los2; 
	} 

	public void setLos2(int los2){ 
		this.los2 = los2; 
	} 

	public double getCurrBasic(){ 
		return currBasic; 
	} 

	public void setCurrBasic(double currBasic){ 
		this.currBasic = currBasic; 
	} 

	public double getCurrTransport(){ 
		return currTransport; 
	} 

	public void setCurrTransport(double currTransport){ 
		this.currTransport = currTransport; 
	} 

	public double getCurrTotal(){ 
		return currTotal; 
	} 

	public void setCurrTotal(double currTotal){ 
		this.currTotal = currTotal; 
	} 

	public double getNewBasic(){ 
		return newBasic; 
	} 

	public void setNewBasic(double newBasic){ 
		this.newBasic = newBasic; 
	} 

	public double getNewTransport(){ 
		return newTransport; 
	} 

	public void setNewTransport(double newTransport){ 
		this.newTransport = newTransport; 
	} 

	public double getNewTotal(){ 
		return newTotal; 
	} 

	public void setNewTotal(double newTotal){ 
		this.newTotal = newTotal; 
	} 

	public double getIncSalary(){ 
		return incSalary; 
	} 

	public void setIncSalary(double incSalary){ 
		this.incSalary = incSalary; 
	} 

	public double getIncTransport(){ 
		return incTransport; 
	} 

	public void setIncTransport(double incTransport){ 
		this.incTransport = incTransport; 
	} 

	public double getAdditional(){ 
		return additional; 
	} 

	public void setAdditional(double additional){ 
		this.additional = additional; 
	} 

	public double getIncTotal(){ 
		return incTotal; 
	} 

	public void setIncTotal(double incTotal){ 
		this.incTotal = incTotal; 
	} 

	public double getPercentageBasic(){ 
		return percentageBasic; 
	} 

	public void setPercentageBasic(double percentageBasic){ 
		this.percentageBasic = percentageBasic; 
	} 

	public double getPercentTransport(){ 
		return percentTransport; 
	} 

	public void setPercentTransport(double percentTransport){ 
		this.percentTransport = percentTransport; 
	} 

	public double getPercentageTotal(){ 
		return percentageTotal; 
	} 

	public void setPercentageTotal(double percentageTotal){ 
		this.percentageTotal = percentageTotal; 
	} 

    public Date getCurrDate(){ return currDate; }

    public void setCurrDate(Date currDate){ this.currDate = currDate; }
}
