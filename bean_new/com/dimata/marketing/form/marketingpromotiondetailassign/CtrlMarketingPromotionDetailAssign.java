/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetailassign;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.marketing.entity.marketingpromotiondetailassign.MarketingPromotionDetailAssign;
import com.dimata.marketing.entity.marketingpromotiondetailassign.PstMarketingPromotionDetailAssign;
import com.dimata.posbo.db.*;

/*
 Description : Controll MarketingPromotionDetailAssign
 Date : Wed Nov 30 2016
 Author : Dewa
 */
public class CtrlMarketingPromotionDetailAssign extends Control implements I_Language {

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
    private MarketingPromotionDetailAssign entMarketingPromotionDetailAssign;
    private PstMarketingPromotionDetailAssign pstMarketingPromotionDetailAssign;
    private FrmMarketingPromotionDetailAssign frmMarketingPromotionDetailAssign;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingPromotionDetailAssign(HttpServletRequest request) {
        msgString = "";
        entMarketingPromotionDetailAssign = new MarketingPromotionDetailAssign();
        try {
            pstMarketingPromotionDetailAssign = new PstMarketingPromotionDetailAssign(0);
        } catch (Exception e) {;
        }
        frmMarketingPromotionDetailAssign = new FrmMarketingPromotionDetailAssign(request, entMarketingPromotionDetailAssign);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingPromotionDetailAssign.addError(frmMarketingPromotionDetailAssign.FRM_FIELD_MARKETING_PROMOTION_DETAIL_ASSIGN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingPromotionDetailAssign getMarketingPromotionDetailAssign() {
        return entMarketingPromotionDetailAssign;
    }

    public FrmMarketingPromotionDetailAssign getForm() {
        return frmMarketingPromotionDetailAssign;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMarketingPromotionDetailAssign) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionDetailAssign != 0) {
                    try {
                        entMarketingPromotionDetailAssign = PstMarketingPromotionDetailAssign.fetchExc(oidMarketingPromotionDetailAssign);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionDetailAssign.requestEntityObject(entMarketingPromotionDetailAssign);

                if (frmMarketingPromotionDetailAssign.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionDetailAssign.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionDetailAssign.insertExc(this.entMarketingPromotionDetailAssign);
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
                        long oid = pstMarketingPromotionDetailAssign.updateExc(this.entMarketingPromotionDetailAssign);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMarketingPromotionDetailAssign != 0) {
                    try {
                        entMarketingPromotionDetailAssign = PstMarketingPromotionDetailAssign.fetchExc(oidMarketingPromotionDetailAssign);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionDetailAssign != 0) {
                    try {
                        entMarketingPromotionDetailAssign = PstMarketingPromotionDetailAssign.fetchExc(oidMarketingPromotionDetailAssign);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionDetailAssign != 0) {
                    try {
                        long oid = PstMarketingPromotionDetailAssign.deleteExc(oidMarketingPromotionDetailAssign);
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
