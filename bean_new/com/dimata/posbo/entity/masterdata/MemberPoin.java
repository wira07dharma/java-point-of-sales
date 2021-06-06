
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

package com.dimata.posbo.entity.masterdata; 
 
/* package java */ 
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;

public class MemberPoin extends Entity implements Serializable {

            
    private long cashBillMainId = 0;
    private long memberId = 0;
    private Date transactionDate = new Date();
    private int debet = 0;
    private int credit = 0;
	private int currentPoint = 0;
    
    public String getPstClassName() {
       return "com.dimata.pos.entity.member.MemberPoin" ;
    }

    /**
     * Getter for property cashBillMainId.
     * @return Value of property cashBillMainId.
     */
    public long getCashBillMainId ()
    {
        return cashBillMainId;
    }
    
    /**
     * Setter for property cashBillMainId.
     * @param cashBillMainId New value of property cashBillMainId.
     */
    public void setCashBillMainId (long cashBillMainId)
    {
        this.cashBillMainId = cashBillMainId;
    }
    
    /**
     * Getter for property memberId.
     * @return Value of property memberId.
     */
    public long getMemberId ()
    {
        return memberId;
    }
    
    /**
     * Setter for property memberId.
     * @param memberId New value of property memberId.
     */
    public void setMemberId (long memberId)
    {
        this.memberId = memberId;
    }
    
    /**
     * Getter for property debet.
     * @return Value of property debet.
     */
    public int getDebet ()
    {
        return debet;
    }
    
    /**
     * Setter for property debet.
     * @param debet New value of property debet.
     */
    public void setDebet (int debet)
    {
        this.debet = debet;
    }
    
    /**
     * Getter for property credit.
     * @return Value of property credit.
     */
    public int getCredit ()
    {
        return credit;
    }
    
    /**
     * Setter for property credit.
     * @param credit New value of property credit.
     */
    public void setCredit (int credit)
    {
        this.credit = credit;
    }
    
    /**
     * Getter for property transactionDate.
     * @return Value of property transactionDate.
     */
    public java.util.Date getTransactionDate() {
        return transactionDate;
    }
    
    /**
     * Setter for property transactionDate.
     * @param transactionDate New value of property transactionDate.
     */
    public void setTransactionDate(java.util.Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getLogDetail(Entity prevDoc) {
        String history = "";
        
        MemberPoin prevMemberPoin = new MemberPoin();
        BillMain billMain = new BillMain();
        BillMain prevBillMain = new BillMain();
        MemberReg memberReg = new MemberReg();        
        MemberReg prevMemberReg = new MemberReg();        
        
        if (prevDoc != null) {
            prevMemberPoin = (MemberPoin) prevDoc;
            try {
                if (prevMemberPoin.getCashBillMainId() != 0) {
                    prevBillMain = PstBillMain.fetchExc(prevMemberPoin.getCashBillMainId());
                }
                if (prevMemberPoin.getMemberId() != 0) {
                    prevMemberReg = PstMemberReg.fetchExc(prevMemberPoin.getMemberId());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            if (this.getMemberId() != 0) {
                memberReg = PstMemberReg.fetchExc(this.getMemberId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            if (this.getCashBillMainId() != 0) {
                billMain = PstBillMain.fetchExc(this.getCashBillMainId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (prevDoc == null) {
			MemberPoin memberPoin = PstMemberPoin.getTotalPoint(memberReg.getOID());
            int point = memberPoin.getCredit() - memberPoin.getDebet();
            history += ""
                    + "Member : " + memberReg.getPersonName() + "; "
                    + "Poin diterima : " + this.getCredit() + "; "
                    + "Poin ditukar : " + this.getDebet() + "; "
                    + "Tanggal transaksi : " + Formater.formatDate(this.getTransactionDate(), "yyyy-MM-dd hh:mm:ss") + "; "
                    + "Nomor bill : " + billMain.getInvoiceNo() + "; "
					+ "Total poin : " + point + "; "
                    + "";
        } else {
            if (prevMemberPoin.getMemberId() != this.getMemberId()) {
                history += "Member changed from " + prevMemberReg.getPersonName() + " to " + memberReg.getPersonName();
            }
            if (prevMemberPoin.getCredit()!= this.getCredit()) {
                history += "Poin diterima changed from " + prevMemberPoin.getCredit() + " to " + this.getCredit();
            }
            if (prevMemberPoin.getDebet()!= this.getDebet()) {
                history += "Poin ditukar changed from " + prevMemberPoin.getDebet() + " to " + this.getDebet();
            }
            int check = prevMemberPoin.getTransactionDate().compareTo(this.getTransactionDate());
            if (check != 0) {
                history += "Tanggal transaksi changed from " + Formater.formatDate(prevMemberPoin.getTransactionDate(), "yyyy-MM-dd hh:mm:ss") 
                        + " to " + Formater.formatDate(this.getTransactionDate(), "yyyy-MM-dd hh:mm:ss");
            }
            if (prevMemberPoin.getCashBillMainId()!= this.getCashBillMainId()) {
                history += "Nomor bill changed from " + prevBillMain.getInvoiceNo() + " to " + billMain.getInvoiceNo();
            }
        }
        return history;
    }
    
	/**
	 * @return the currentPoint
	 */
	public int getCurrentPoint() {
		return currentPoint;
	}

	/**
	 * @param currentPoint the currentPoint to set
	 */
	public void setCurrentPoint(int currentPoint) {
		this.currentPoint = currentPoint;
	}
    
}
