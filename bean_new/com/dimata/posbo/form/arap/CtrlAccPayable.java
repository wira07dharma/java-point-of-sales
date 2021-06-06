/*
 * CtrlAccPayable.java
 *
 * Created on May 5, 2007, 12:56 PM
 */

package com.dimata.posbo.form.arap;

/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.posbo.entity.arap.AccPayable;
import com.dimata.posbo.entity.arap.PstAccPayable;
import com.dimata.posbo.entity.arap.AccPayableDetail;
import com.dimata.posbo.entity.arap.PstAccPayableDetail;
import com.dimata.common.entity.payment.PaymentInfo;
import com.dimata.common.entity.payment.PstPaymentInfo;
import com.dimata.common.form.payment.FrmPaymentInfo;
import com.dimata.posbo.entity.warehouse.PriceProtection;
import com.dimata.posbo.entity.warehouse.PstPriceProtection;
import com.dimata.posbo.entity.warehouse.PstPriceProtectionDistribution;
import com.dimata.posbo.session.arap.SessAccPayablePaymentNumber;
/**
 *
 * @author  gwawan
 */
public class CtrlAccPayable extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Unit sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Unit Code already exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private AccPayable accPayable;
    private AccPayable prevAccPayable;
    private PstAccPayable pstAccPayable;
    private FrmAccPayable frmAccPayable;
    int language = LANGUAGE_FOREIGN;
    
    private long oidAccPayable;
    private long oidAccPayableDetail;
    private long oidPP;
    private AccPayableDetail accPayableDetail;
    private PstAccPayableDetail pstAccPayableDetail;
    private FrmAccPayableDetail frmAccPayableDetail;
    private PaymentInfo paymentInfo;
    private PstPaymentInfo pstPaymentInfo;
    private FrmPaymentInfo frmPaymentInfo;
    
    /** Creates a new instance of CtrlAccPayable */
    public CtrlAccPayable(HttpServletRequest request) {
        msgString = "";
        accPayable = new AccPayable();
        accPayableDetail = new AccPayableDetail();
        paymentInfo = new PaymentInfo();
        try{
            pstAccPayable = new PstAccPayable(0);
            pstAccPayableDetail = new PstAccPayableDetail(0);
            pstPaymentInfo = new PstPaymentInfo(0);
        } catch(Exception e) {
            ;
        }
        frmAccPayable = new FrmAccPayable(request, accPayable);
        frmAccPayableDetail = new FrmAccPayableDetail(request, accPayableDetail);
        frmPaymentInfo = new FrmPaymentInfo(request, paymentInfo);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAccPayable.addError(frmAccPayable.FRM_ACC_PAYABLE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage() {
        return this.language;
    }
    
    public void setLanguage(int language) {
        this.language = language;
    }
    
    public FrmAccPayable getForm() {
        return this.frmAccPayable;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public AccPayable getAccPayable() {
        return this.accPayable;
    }
    
    public AccPayableDetail getAccPayableDetail() {
        return this.accPayableDetail;
    }
    
    public PaymentInfo getPaymentInfo() {
        return this.paymentInfo;
    }
    
    public int action(int cmd, long oidAccPayable, long oidAccPayableDetail, long ppOid) {
        this.oidAccPayableDetail = oidAccPayableDetail;
        this.oidPP = ppOid;
        return this.action(cmd, oidAccPayable);
    }
    
    public int action(int cmd, long oidAccPayable, long oidAccPayableDetail) {
        this.oidAccPayableDetail = oidAccPayableDetail;
        return this.action(cmd, oidAccPayable);
    }
    
    public int action(int cmd, long oidObj) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                if(oidObj != 0) {
                    try {
                        this.accPayable = PstAccPayable.fetchExc(oidObj);
                        this.prevAccPayable = PstAccPayable.fetchExc(oidObj);
                    } catch(Exception e) {
                    }
                }
                
                frmAccPayable.requestEntityObject(accPayable);
                if(frmAccPayable.errorSize() > 0) {
                    msgString = FRMMessage.getMessage(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                //ambil object AccPayableDetail dan PaymentInfo
                frmAccPayableDetail.requestEntityObject(accPayableDetail);
                frmPaymentInfo.requestEntityObject(paymentInfo);
                
                if(accPayable.getOID() == 0) {
                    try {
                        accPayable.setStatus(5);
                        
                        //proses pembuatan nomor payment
                        accPayable.setNoUrut(SessAccPayablePaymentNumber.getIntCode(this.accPayable.getPaymentDate()));
                        accPayable.setPaymentNumber(SessAccPayablePaymentNumber.getCodePaymentNumber(this.accPayable));
                        long accPayableId = pstAccPayable.insertExc(this.accPayable);
                        
                        accPayableDetail.setAccPayableId(accPayableId);
                        long accPayableDetailId = pstAccPayableDetail.insertExc(this.accPayableDetail);
                        
                        paymentInfo.setlPurchPaymentId(accPayableDetailId);
                        PriceProtection priceProtection = new PriceProtection();
                        if(this.oidPP!=0){
                            try{
                                //priceProtection = PstPriceProtection.fetchExc(this.oidPP);
                                this.paymentInfo.setStCardNumber(""+this.oidPP);
                                this.paymentInfo.setStCardId(""+this.oidPP);
                                
                                //update 
                                PstPriceProtectionDistribution.updateStatusPaymentPP(this.oidPP, 1);
                                
                            }catch(Exception ex){
                            
                            }
                            //PriceProtection 
                        }
                        
                        long paymentInfoId = pstPaymentInfo.insertExc(this.paymentInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        accPayable.setStatus(5);
                        if(!prevAccPayable.getPaymentDate().equals(this.accPayable.getPaymentDate())){
                            accPayable.setNoUrut(SessAccPayablePaymentNumber.getIntCode(this.accPayable.getPaymentDate()));
                            accPayable.setPaymentNumber(SessAccPayablePaymentNumber.getCodePaymentNumber(this.accPayable));
                        }
                        long accPayableId = pstAccPayable.updateExc(this.accPayable);
                        long accPayableDetailId = pstAccPayableDetail.updateExc(this.accPayableDetail);
                        if(paymentInfo.getlPurchPaymentId() == accPayableDetail.getOID()) {
                            long paymentInfoId = pstPaymentInfo.updateExc(this.paymentInfo);
                        }
                        else {
                            paymentInfo.setlPurchPaymentId(accPayableDetailId);
                            long paymentInfoId = pstPaymentInfo.insertExc(this.paymentInfo);
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.EDIT:
                if (oidObj != 0) {
                    try {
                        accPayable = PstAccPayable.fetchExc(oidObj);System.out.println("accPayable ID: "+accPayable.getOID());
                        accPayableDetail = PstAccPayableDetail.fetchExc(oidAccPayableDetail);System.out.println("accPayableDetail ID: "+accPayableDetail.getOID());
                        paymentInfo = PstPaymentInfo.getPaymentInfo(accPayableDetail.getOID());System.out.println("paymentInfo ID: "+paymentInfo.getOID());
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.ASK:
                if (oidObj != 0) {
                    try {
                        accPayable = PstAccPayable.fetchExc(oidObj);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.DELETE:
                if (oidObj != 0) {
                    try {
                        String whereClause = PstAccPayable.fieldNames[PstAccPayable.FLD_ACC_PAYABLE_ID] + "=" + oidObj;
                        Vector vect = PstAccPayableDetail.list(0, 0, whereClause, "");
                        
                        if (vect != null && vect.size() > 0) {
                            for (int i = 0; i < vect.size(); i++) {
                                AccPayableDetail accPayableDetail = (AccPayableDetail) vect.get(i);
                                CtrlAccPayableDetail ctrlAccPayableDetail = new CtrlAccPayableDetail();
                                ctrlAccPayableDetail.action(Command.DELETE, accPayableDetail.getOID());
                                PstPaymentInfo.deletePaymentInfo(accPayableDetail.getOID());
                            }
                        }
                        
                        long oid = PstAccPayable.deleteExc(oidObj);
                        
                         PstPriceProtectionDistribution.updateStatusPaymentPP(this.oidPP, 0);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        System.out.println(dbexc.toString());
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        System.out.println(exc.toString());
                    }
                }
                break;
        }
        return rsCode;
    }
    
}
