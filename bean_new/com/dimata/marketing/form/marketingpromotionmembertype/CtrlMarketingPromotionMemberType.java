/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotionmembertype;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.marketing.entity.marketingpromotionmembertype.MarketingPromotionMemberType;
import com.dimata.marketing.entity.marketingpromotionmembertype.PstMarketingPromotionMemberType;
import java.util.Vector;

/*
 Description : Controll MarketingPromotionMemberType
 Date : Wed Nov 30 2016
 Author : Dewa
 */
public class CtrlMarketingPromotionMemberType extends Control implements I_Language {

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
    private MarketingPromotionMemberType entMarketingPromotionMemberType;
    private PstMarketingPromotionMemberType pstMarketingPromotionMemberType;
    private FrmMarketingPromotionMemberType frmMarketingPromotionMemberType;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingPromotionMemberType(HttpServletRequest request) {
        msgString = "";
        entMarketingPromotionMemberType = new MarketingPromotionMemberType();
        try {
            pstMarketingPromotionMemberType = new PstMarketingPromotionMemberType(0);
        } catch (Exception e) {;
        }
        frmMarketingPromotionMemberType = new FrmMarketingPromotionMemberType(request, entMarketingPromotionMemberType);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingPromotionMemberType.addError(frmMarketingPromotionMemberType.FRM_FIELD_MARKETING_PROMOTION_MEMBER_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingPromotionMemberType getMarketingPromotionMemberType() {
        return entMarketingPromotionMemberType;
    }

    public FrmMarketingPromotionMemberType getForm() {
        return frmMarketingPromotionMemberType;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int actionDelete(int cmd, long oidMarketingPromotionMemberType, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionMemberType.requestEntityObject(entMarketingPromotionMemberType);

                if (frmMarketingPromotionMemberType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionMemberType.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionMemberType.insertExc(this.entMarketingPromotionMemberType);
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
                        long oid = pstMarketingPromotionMemberType.updateExc(this.entMarketingPromotionMemberType);
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
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionMemberType != 0) {
                    Vector listMultiMember = PstMarketingPromotionMemberType.list(0, 0, "" + PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MARKETING_PROMOTION_ID] + " = " + oidMarketingPromotionMemberType, "");
                    for (int i = 0; i < listMultiMember.size(); i++) {
                        MarketingPromotionMemberType member = (MarketingPromotionMemberType) listMultiMember.get(i);
                        try {
                            long oid = PstMarketingPromotionMemberType.deleteExc(member.getOID());
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
                }
                break;

            default:

        }
        return rsCode;
    }

    public int actionSave(int cmd, long oidMarketingPromotionMemberType, String oidDelete, long oidBaru, long oidMulti) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:

                frmMarketingPromotionMemberType.requestEntityObject(entMarketingPromotionMemberType, oidBaru, oidMulti);

                if (frmMarketingPromotionMemberType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                try {
                    long oid = pstMarketingPromotionMemberType.insertExc(this.entMarketingPromotionMemberType);
                    msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                    return getControlMsgId(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                }

                break;

            case Command.EDIT:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        long oid = PstMarketingPromotionMemberType.deleteExc(oidMarketingPromotionMemberType);
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

    public int action(int cmd, long oidMarketingPromotionMemberType, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionMemberType.requestEntityObject(entMarketingPromotionMemberType);

                if (frmMarketingPromotionMemberType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionMemberType.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionMemberType.insertExc(this.entMarketingPromotionMemberType);
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
                        long oid = pstMarketingPromotionMemberType.updateExc(this.entMarketingPromotionMemberType);
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
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        entMarketingPromotionMemberType = PstMarketingPromotionMemberType.fetchExc(oidMarketingPromotionMemberType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionMemberType != 0) {
                    try {
                        long oid = PstMarketingPromotionMemberType.deleteExc(oidMarketingPromotionMemberType);
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
