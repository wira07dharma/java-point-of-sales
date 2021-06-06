/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotionpricetype;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.marketing.entity.marketingpromotionpricetype.MarketingPromotionPriceType;
import com.dimata.marketing.entity.marketingpromotionpricetype.PstMarketingPromotionPriceType;
import java.util.Vector;

/*
 Description : Controll MarketingPromotionPriceType
 Date : Wed Nov 30 2016
 Author : Dewa
 */
public class CtrlMarketingPromotionPriceType extends Control implements I_Language {

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
    private MarketingPromotionPriceType entMarketingPromotionPriceType;
    private PstMarketingPromotionPriceType pstMarketingPromotionPriceType;
    private FrmMarketingPromotionPriceType frmMarketingPromotionPriceType;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingPromotionPriceType(HttpServletRequest request) {
        msgString = "";
        entMarketingPromotionPriceType = new MarketingPromotionPriceType();
        try {
            pstMarketingPromotionPriceType = new PstMarketingPromotionPriceType(0);
        } catch (Exception e) {;
        }
        frmMarketingPromotionPriceType = new FrmMarketingPromotionPriceType(request, entMarketingPromotionPriceType);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingPromotionPriceType.addError(frmMarketingPromotionPriceType.FRM_FIELD_MARKETING_PROMOTION_PRICE_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingPromotionPriceType getMarketingPromotionPriceType() {
        return entMarketingPromotionPriceType;
    }

    public FrmMarketingPromotionPriceType getForm() {
        return frmMarketingPromotionPriceType;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int actionDelete(int cmd, long oidMarketingPromotionPriceType, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionPriceType.requestEntityObject(entMarketingPromotionPriceType);

                if (frmMarketingPromotionPriceType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionPriceType.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionPriceType.insertExc(this.entMarketingPromotionPriceType);
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
                        long oid = pstMarketingPromotionPriceType.updateExc(this.entMarketingPromotionPriceType);
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
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionPriceType != 0) {
                    Vector listMultiPrice = PstMarketingPromotionPriceType.list(0, 0, "" + PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_MARKETING_PROMOTION_ID] + " = " + oidMarketingPromotionPriceType, "");
                    for (int i = 0; i < listMultiPrice.size(); i++) {
                        MarketingPromotionPriceType price = (MarketingPromotionPriceType) listMultiPrice.get(i);
                        try {
                            long oid = PstMarketingPromotionPriceType.deleteExc(price.getOID());
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

    public int actionSave(int cmd, long oidMarketingPromotionPriceType, String oidDelete, long oidBaru, long oidMulti) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:

                frmMarketingPromotionPriceType.requestEntityObject(entMarketingPromotionPriceType, oidBaru, oidMulti);

                if (frmMarketingPromotionPriceType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                try {
                    long oid = pstMarketingPromotionPriceType.insertExc(this.entMarketingPromotionPriceType);
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
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        long oid = PstMarketingPromotionPriceType.deleteExc(oidMarketingPromotionPriceType);
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

    public int action(int cmd, long oidMarketingPromotionPriceType, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionPriceType.requestEntityObject(entMarketingPromotionPriceType);

                if (frmMarketingPromotionPriceType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionPriceType.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionPriceType.insertExc(this.entMarketingPromotionPriceType);
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
                        long oid = pstMarketingPromotionPriceType.updateExc(this.entMarketingPromotionPriceType);
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
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        entMarketingPromotionPriceType = PstMarketingPromotionPriceType.fetchExc(oidMarketingPromotionPriceType);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionPriceType != 0) {
                    try {
                        long oid = PstMarketingPromotionPriceType.deleteExc(oidMarketingPromotionPriceType);
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
