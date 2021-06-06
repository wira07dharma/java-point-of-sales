/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetailsubject;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.marketing.entity.marketingpromotiondetailsubject.MarketingPromotionDetailSubject;
import com.dimata.marketing.entity.marketingpromotiondetailsubject.PstMarketingPromotionDetailSubject;

/*
 Description : Controll MarketingPromotionDetailSubject
 Date : Wed Nov 30 2016
 Author : Dewa
 */
public class CtrlMarketingPromotionDetailSubject extends Control implements I_Language {

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
    private MarketingPromotionDetailSubject entMarketingPromotionDetailSubject;
    private PstMarketingPromotionDetailSubject pstMarketingPromotionDetailSubject;
    private FrmMarketingPromotionDetailSubject frmMarketingPromotionDetailSubject;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingPromotionDetailSubject(HttpServletRequest request) {
        msgString = "";
        entMarketingPromotionDetailSubject = new MarketingPromotionDetailSubject();
        try {
            pstMarketingPromotionDetailSubject = new PstMarketingPromotionDetailSubject(0);
        } catch (Exception e) {;
        }
        frmMarketingPromotionDetailSubject = new FrmMarketingPromotionDetailSubject(request, entMarketingPromotionDetailSubject);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingPromotionDetailSubject.addError(frmMarketingPromotionDetailSubject.FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingPromotionDetailSubject getMarketingPromotionDetailSubject() {
        return entMarketingPromotionDetailSubject;
    }

    public FrmMarketingPromotionDetailSubject getForm() {
        return frmMarketingPromotionDetailSubject;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMarketingPromotionDetailSubject, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionDetailSubject != 0) {
                    try {
                        entMarketingPromotionDetailSubject = PstMarketingPromotionDetailSubject.fetchExc(oidMarketingPromotionDetailSubject);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionDetailSubject.requestEntityObject(entMarketingPromotionDetailSubject);

                if (frmMarketingPromotionDetailSubject.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionDetailSubject.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionDetailSubject.insertExc(this.entMarketingPromotionDetailSubject);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
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
                        long oid = pstMarketingPromotionDetailSubject.updateExc(this.entMarketingPromotionDetailSubject);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMarketingPromotionDetailSubject != 0) {
                    try {
                        entMarketingPromotionDetailSubject = PstMarketingPromotionDetailSubject.fetchExc(oidMarketingPromotionDetailSubject);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionDetailSubject != 0) {
                    try {
                        entMarketingPromotionDetailSubject = PstMarketingPromotionDetailSubject.fetchExc(oidMarketingPromotionDetailSubject);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionDetailSubject != 0) {
                    try {
                        long oid = PstMarketingPromotionDetailSubject.deleteExc(oidMarketingPromotionDetailSubject);
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
