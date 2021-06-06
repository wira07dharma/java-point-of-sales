/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotionlocation;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.marketing.entity.marketingpromotionlocation.MarketingPromotionLocation;
import com.dimata.marketing.entity.marketingpromotionlocation.PstMarketingPromotionLocation;
import java.util.Vector;

/*
 Description : Controll MarketingPromotionLocation
 Date : Wed Nov 30 2016
 Author : Dewa
 */
public class CtrlMarketingPromotionLocation extends Control implements I_Language {

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
    private MarketingPromotionLocation entMarketingPromotionLocation;
    private PstMarketingPromotionLocation pstMarketingPromotionLocation;
    private FrmMarketingPromotionLocation frmMarketingPromotionLocation;
    int language = LANGUAGE_DEFAULT;

    public CtrlMarketingPromotionLocation(HttpServletRequest request) {
        msgString = "";
        entMarketingPromotionLocation = new MarketingPromotionLocation();
        try {
            pstMarketingPromotionLocation = new PstMarketingPromotionLocation(0);
        } catch (Exception e) {;
        }
        frmMarketingPromotionLocation = new FrmMarketingPromotionLocation(request, entMarketingPromotionLocation);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingPromotionLocation.addError(frmMarketingPromotionLocation.FRM_FIELD_MARKETING_PROMOTION_LOCATION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MarketingPromotionLocation getMarketingPromotionLocation() {
        return entMarketingPromotionLocation;
    }

    public FrmMarketingPromotionLocation getForm() {
        return frmMarketingPromotionLocation;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMarketingPromotionLocation, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionLocation.requestEntityObject(entMarketingPromotionLocation);

                if (frmMarketingPromotionLocation.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionLocation.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionLocation.insertExc(this.entMarketingPromotionLocation);
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
                        long oid = pstMarketingPromotionLocation.updateExc(this.entMarketingPromotionLocation);
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
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        long oid = PstMarketingPromotionLocation.deleteExc(oidMarketingPromotionLocation);
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

    public int actionDelete(int cmd, long oidMarketingPromotionLocation, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingPromotionLocation.requestEntityObject(entMarketingPromotionLocation);

                if (frmMarketingPromotionLocation.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingPromotionLocation.getOID() == 0) {
                    try {
                        long oid = pstMarketingPromotionLocation.insertExc(this.entMarketingPromotionLocation);
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
                        long oid = pstMarketingPromotionLocation.updateExc(this.entMarketingPromotionLocation);
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
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionLocation != 0) {
                    Vector listMultiLocation = PstMarketingPromotionLocation.list(0, 0, "" + PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_MARKETING_PROMOTION_ID] + " = " + oidMarketingPromotionLocation, "");
                    for (int i = 0; i < listMultiLocation.size(); i++) {
                        MarketingPromotionLocation location = (MarketingPromotionLocation) listMultiLocation.get(i);
                        try {
                            long oid = PstMarketingPromotionLocation.deleteExc(location.getOID());
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

    public int actionSave(int cmd, long oidMarketingPromotionLocation, String oidDelete, long oidBaru, long oidMulti) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:

                frmMarketingPromotionLocation.requestEntityObject(entMarketingPromotionLocation, oidBaru, oidMulti);

                if (frmMarketingPromotionLocation.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                try {
                    long oid = pstMarketingPromotionLocation.insertExc(this.entMarketingPromotionLocation);
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
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        entMarketingPromotionLocation = PstMarketingPromotionLocation.fetchExc(oidMarketingPromotionLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingPromotionLocation != 0) {
                    try {
                        long oid = PstMarketingPromotionLocation.deleteExc(oidMarketingPromotionLocation);
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
