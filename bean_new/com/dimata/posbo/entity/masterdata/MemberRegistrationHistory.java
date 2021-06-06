
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

package com.dimata.posbo.entity.masterdata; 
 
/* package java */ 
import com.dimata.common.entity.logger.I_LogHistory;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;

public class MemberRegistrationHistory extends Entity implements I_LogHistory{

	private long memberId;
	private Date registrationDate;
	private Date validStartDate;
	private Date validExpiredDate;

	public long getMemberId(){ 
		return memberId; 
	} 

	public void setMemberId(long memberId){ 
		this.memberId = memberId; 
	} 

	public Date getRegistrationDate(){ 
		return registrationDate; 
	} 

	public void setRegistrationDate(Date registrationDate){ 
		this.registrationDate = registrationDate; 
	} 

	public Date getValidStartDate(){ 
		return validStartDate; 
	} 

	public void setValidStartDate(Date validStartDate){ 
		this.validStartDate = validStartDate; 
	} 

	public Date getValidExpiredDate(){ 
		return validExpiredDate; 
	} 

	public void setValidExpiredDate(Date validExpiredDate){ 
		this.validExpiredDate = validExpiredDate; 
	}

    public String getLogDetail(Entity prevDoc) {
        MemberRegistrationHistory prevPo = (MemberRegistrationHistory)prevDoc;
        
        return  ((prevPo == null ||  prevPo.getOID()==0 || !Formater.formatDate(prevPo.getRegistrationDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getRegistrationDate(), "yyyy-MM-dd")))?
                (" Registration Date : " + Formater.formatDate(this.getRegistrationDate(), "yyyy-MM-dd") + " ;") : "")+
                ((prevPo == null ||  prevPo.getOID()==0 || !Formater.formatDate(prevPo.getValidStartDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getValidStartDate(), "yyyy-MM-dd")))?
                (" Registration Date : " + Formater.formatDate(this.getValidStartDate(), "yyyy-MM-dd") + " ;") : "")+
                 ((prevPo == null ||  prevPo.getOID()==0 || !Formater.formatDate(prevPo.getValidExpiredDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getValidExpiredDate(), "yyyy-MM-dd")))?
                (" Registration Date : " + Formater.formatDate(this.getValidExpiredDate(), "yyyy-MM-dd") + " ;") : "");
    }

}
