/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetail;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.marketing.entity.marketingpromotiondetail.MarketingPromotionDetail;
import com.dimata.marketing.entity.marketingpromotiondetail.PstMarketingPromotionDetail;

/*
 Description : Controll MarketingPromotionDetail
 Date : Sat Dec 03 2016
 Author : Dewa
 */
public class CtrlMarketingPromotionDetail extends Control implements I_Language {

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
    private MarketingPromotionDetail entMarketingPromotionDetail;
    private PstMarketingPromotionDetail pstMarketingPromotionDetail;
    private FrmMarketingPromotionDetail frmMarketingPromotionDetail;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingPromotionDetail(HttpServletRequest request) {
        msgString = "";
        entMarketingPromotionDetail = new MarketingPromotionDetail();
        try {
            pstMarketingPromotionDetail = new PstMarketingPromotionDetail(0);
        } catch (Exception e) {;
        }
        frmMarketingPromotionDetail = new FrmMarketingPromotionDetail(request, entMarketingPromotionDetail);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingPromotionDetail.addError(frmMarketingPromotionDetail.FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingPromotionDetail getMarketingPromotionDetail() {
        return entMarketingPromotionDetail;
    }

    public FrmMarketingPromotionDetail getForm() {
        return frmMarketingPromotionDetail;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMarketingPromotionDetail, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        entMarketingPromotionDetail = PstMarketingPromotionDetail.fetchExc(oidMarketingPromotionDetail);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionDetail.requestEntityObject(entMarketingPromotionDetail);

                if (frmMarketingPromotionDetail.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionDetail.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionDetail.insertExc(this.entMarketingPromotionDetail);
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
                        long oid = pstMarketingPromotionDetail.updateExc(this.entMarketingPromotionDetail);
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
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        entMarketingPromotionDetail = PstMarketingPromotionDetail.fetchExc(oidMarketingPromotionDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        entMarketingPromotionDetail = PstMarketingPromotionDetail.fetchExc(oidMarketingPromotionDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        long oid = PstMarketingPromotionDetail.deleteExc(oidMarketingPromotionDetail);
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

    public int actionApprove(int cmd, long oidMarketingPromotionDetail, String status) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        entMarketingPromotionDetail = PstMarketingPromotionDetail.fetchExc(oidMarketingPromotionDetail);
                    } catch (Exception exc) {
                    }
                }

                entMarketingPromotionDetail.setStatusApprove(status);

                //frmMarketingPromotionDetail.requestEntityObject(entMarketingPromotionDetail);
                if (frmMarketingPromotionDetail.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionDetail.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionDetail.insertExc(this.entMarketingPromotionDetail);
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
                        long oid = pstMarketingPromotionDetail.updateExc(this.entMarketingPromotionDetail);
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
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        entMarketingPromotionDetail = PstMarketingPromotionDetail.fetchExc(oidMarketingPromotionDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        entMarketingPromotionDetail = PstMarketingPromotionDetail.fetchExc(oidMarketingPromotionDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionDetail != 0) {
                    try {
                        long oid = PstMarketingPromotionDetail.deleteExc(oidMarketingPromotionDetail);
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
