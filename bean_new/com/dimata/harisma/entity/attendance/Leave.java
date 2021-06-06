
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

package com.dimata.harisma.entity.attendance; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Leave extends Entity { 

	private long employeeId;
	private Date submitDate;
	private Date leaveFrom;
	private Date leaveTo;
	private int duration;
	private String reason = "";
	private int longLeave;
	private int annualLeave;
	private int leaveWoPay;
	private int maternityLeave;
	private int dayOff;
	private int publicHoliday;
	private int extraDayOff;
	private int sickLeave;
	private int periodAlFrom;
	private int periodAlTo;
	private int alEntitlement;
	private int alTaken;
	private int alBalance;
	private int periodLlFrom;
	private int periodLlTo;
	private int llEntitlement;
	private int llTaken;
	private int llBalance;
	private Date aprSpvDate;
	private Date aprDeptheadDate;
	private Date aprPmgrDate;
    private int leaveType;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public Date getSubmitDate(){ 
		return submitDate; 
	} 

	public void setSubmitDate(Date submitDate){ 
		this.submitDate = submitDate; 
	} 

	public Date getLeaveFrom(){ 
		return leaveFrom; 
	} 

	public void setLeaveFrom(Date leaveFrom){ 
		this.leaveFrom = leaveFrom; 
	} 

	public Date getLeaveTo(){ 
		return leaveTo; 
	} 

	public void setLeaveTo(Date leaveTo){ 
		this.leaveTo = leaveTo; 
	} 

	public int getDuration(){ 
		return duration; 
	} 

	public void setDuration(int duration){ 
		this.duration = duration; 
	} 

	public String getReason(){ 
		return reason; 
	} 

	public void setReason(String reason){ 
		if ( reason == null ) {
			reason = ""; 
		} 
		this.reason = reason; 
	} 

	public int getLongLeave(){ 
		return longLeave; 
	} 

	public void setLongLeave(int longLeave){ 
		this.longLeave = longLeave; 
	} 

	public int getAnnualLeave(){ 
		return annualLeave; 
	} 

	public void setAnnualLeave(int annualLeave){ 
		this.annualLeave = annualLeave; 
	} 

	public int getLeaveWoPay(){ 
		return leaveWoPay; 
	} 

	public void setLeaveWoPay(int leaveWoPay){ 
		this.leaveWoPay = leaveWoPay; 
	} 

	public int getMaternityLeave(){ 
		return maternityLeave; 
	} 

	public void setMaternityLeave(int maternityLeave){ 
		this.maternityLeave = maternityLeave; 
	} 

	public int getDayOff(){ 
		return dayOff; 
	} 

	public void setDayOff(int dayOff){ 
		this.dayOff = dayOff; 
	} 

	public int getPublicHoliday(){ 
		return publicHoliday; 
	} 

	public void setPublicHoliday(int publicHoliday){ 
		this.publicHoliday = publicHoliday; 
	} 

	public int getExtraDayOff(){ 
		return extraDayOff; 
	} 

	public void setExtraDayOff(int extraDayOff){ 
		this.extraDayOff = extraDayOff; 
	} 

	public int getSickLeave(){ 
		return sickLeave; 
	} 

	public void setSickLeave(int sickLeave){ 
		this.sickLeave = sickLeave; 
	} 

	public int getPeriodAlFrom(){ 
		return periodAlFrom; 
	} 

	public void setPeriodAlFrom(int periodAlFrom){ 
		this.periodAlFrom = periodAlFrom; 
	} 

	public int getPeriodAlTo(){ 
		return periodAlTo; 
	} 

	public void setPeriodAlTo(int periodAlTo){ 
		this.periodAlTo = periodAlTo; 
	} 

	public int getAlEntitlement(){ 
		return alEntitlement; 
	} 

	public void setAlEntitlement(int alEntitlement){ 
		this.alEntitlement = alEntitlement; 
	} 

	public int getAlTaken(){ 
		return alTaken; 
	} 

	public void setAlTaken(int alTaken){ 
		this.alTaken = alTaken; 
	} 

	public int getAlBalance(){ 
		return alBalance; 
	} 

	public void setAlBalance(int alBalance){ 
		this.alBalance = alBalance; 
	} 

	public int getPeriodLlFrom(){ 
		return periodLlFrom; 
	} 

	public void setPeriodLlFrom(int periodLlFrom){ 
		this.periodLlFrom = periodLlFrom; 
	} 

	public int getPeriodLlTo(){ 
		return periodLlTo; 
	} 

	public void setPeriodLlTo(int periodLlTo){ 
		this.periodLlTo = periodLlTo; 
	} 

	public int getLlEntitlement(){ 
		return llEntitlement; 
	} 

	public void setLlEntitlement(int llEntitlement){ 
		this.llEntitlement = llEntitlement; 
	} 

	public int getLlTaken(){ 
		return llTaken; 
	} 

	public void setLlTaken(int llTaken){ 
		this.llTaken = llTaken; 
	} 

	public int getLlBalance(){ 
		return llBalance; 
	} 

	public void setLlBalance(int llBalance){ 
		this.llBalance = llBalance; 
	} 

	public Date getAprSpvDate(){ 
		return aprSpvDate; 
	} 

	public void setAprSpvDate(Date aprSpvDate){ 
		this.aprSpvDate = aprSpvDate; 
	} 

	public Date getAprDeptheadDate(){ 
		return aprDeptheadDate; 
	} 

	public void setAprDeptheadDate(Date aprDeptheadDate){ 
		this.aprDeptheadDate = aprDeptheadDate; 
	} 

	public Date getAprPmgrDate(){ 
		return aprPmgrDate; 
	} 

	public void setAprPmgrDate(Date aprPmgrDate){ 
		this.aprPmgrDate = aprPmgrDate; 
	} 

    public int getLeaveType(){ return leaveType; }

    public void setLeaveType(int leaveType){ this.leaveType = leaveType; }
}
