/* 
 * Ctrl Name  		:  CtrlStandartRate.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.common.form.payment;

/* java package */
import java.util.*;
import javax.servlet.http.*;

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;

/* project package */
import com.dimata.common.entity.payment.*;
import com.dimata.common.entity.system.AppValue;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;

public class CtrlStandartRate extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private StandartRate standartRate;
    private PstStandartRate pstStandartRate;
    private FrmStandartRate frmStandartRate;
    int language = LANGUAGE_DEFAULT;

    public CtrlStandartRate(HttpServletRequest request) {
        msgString = "";
        standartRate = new StandartRate();
        try {
            pstStandartRate = new PstStandartRate(0);
        } catch (Exception e) {;
        }
        frmStandartRate = new FrmStandartRate(request, standartRate);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmStandartRate.addError(frmStandartRate.FRM_FIELD_STANDART_RATE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public StandartRate getStandartRate() {
        return standartRate;
    }

    public FrmStandartRate getForm() {
        return frmStandartRate;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidStandartRate) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidStandartRate != 0) {
                    try {
                        standartRate = PstStandartRate.fetchExc(oidStandartRate);
                    } catch (Exception exc) {
                    }
                }

                frmStandartRate.requestEntityObject(standartRate);

                if (frmStandartRate.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if (standartRate.getOID() == 0) {
                    try {
                        PstStandartRate.updateStatus(standartRate.getCurrencyTypeId());
                        long oid = pstStandartRate.insertExc(this.standartRate);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstStandartRate.updateExc(this.standartRate);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidStandartRate != 0) {
                    try {
                        standartRate = PstStandartRate.fetchExc(oidStandartRate);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidStandartRate != 0) {
                    try {
                        standartRate = PstStandartRate.fetchExc(oidStandartRate);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidStandartRate != 0) {
                    try {
                        long oid = PstStandartRate.deleteExc(oidStandartRate);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:

        }
        return rsCode;
    }
    
}
