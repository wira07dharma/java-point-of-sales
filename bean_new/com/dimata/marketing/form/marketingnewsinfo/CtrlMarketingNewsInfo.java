/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.marketing.form.marketingnewsinfo;

import com.dimata.marketing.entity.marketingnewsinfo.MarketingNewsInfo;
import com.dimata.marketing.entity.marketingnewsinfo.PstMarketingNewsInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

public class CtrlMarketingNewsInfo extends Control implements I_Language {

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
    private MarketingNewsInfo entMarketingNewsInfo;
    private PstMarketingNewsInfo pstMarketingNewsInfo;
    private FrmMarketingNewsInfo frmMarketingNewsInfo;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingNewsInfo(HttpServletRequest request) {
        msgString = "";
        entMarketingNewsInfo = new MarketingNewsInfo();
        try {
            pstMarketingNewsInfo = new PstMarketingNewsInfo(0);
        } catch (Exception e) {;
        }
        frmMarketingNewsInfo = new FrmMarketingNewsInfo(request, entMarketingNewsInfo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingNewsInfo.addError(frmMarketingNewsInfo.FRM_FIELD_PROMO_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingNewsInfo getMarketingNewsInfo() {
        return entMarketingNewsInfo;
    }

    public FrmMarketingNewsInfo getForm() {
        return frmMarketingNewsInfo;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMarketingNewsInfo, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingNewsInfo != 0) {
                    try {
                        entMarketingNewsInfo = PstMarketingNewsInfo.fetchExc(oidMarketingNewsInfo);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingNewsInfo.requestEntityObject(entMarketingNewsInfo);

                if (frmMarketingNewsInfo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingNewsInfo.getOID() == 0) {
                    try {
                        long oid = pstMarketingNewsInfo.insertExc(this.entMarketingNewsInfo);
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
                        long oid = pstMarketingNewsInfo.updateExc(this.entMarketingNewsInfo);
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
                if (oidMarketingNewsInfo != 0) {
                    try {
                        entMarketingNewsInfo = PstMarketingNewsInfo.fetchExc(oidMarketingNewsInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingNewsInfo != 0) {
                    try {
                        entMarketingNewsInfo = PstMarketingNewsInfo.fetchExc(oidMarketingNewsInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingNewsInfo != 0) {
                    try {
                        long oid = PstMarketingNewsInfo.deleteExc(oidMarketingNewsInfo);
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
