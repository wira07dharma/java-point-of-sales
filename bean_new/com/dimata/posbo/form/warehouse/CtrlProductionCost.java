/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.SessProduction;

/**
 *
 * @author Dimata 007
 */
public class CtrlProductionCost extends Control implements I_Language {

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
    private ProductionCost entProductionCost;
    private PstProductionCost pstProductionCost;
    private FrmProductionCost frmProductionCost;
    int language = LANGUAGE_DEFAULT;

    public CtrlProductionCost(HttpServletRequest request) {
        msgString = "";
        entProductionCost = new ProductionCost();
        try {
            pstProductionCost = new PstProductionCost(0);
        } catch (Exception e) {;
        }
        frmProductionCost = new FrmProductionCost(request, entProductionCost);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmProductionCost.addError(frmProductionCost.FRM_FIELD_PRODUCTION_COST_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ProductionCost getProductionCost() {
        return entProductionCost;
    }

    public FrmProductionCost getForm() {
        return frmProductionCost;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidProductionCost, long userId, String userName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidProductionCost != 0) {
                    try {
                        entProductionCost = PstProductionCost.fetchExc(oidProductionCost);
                    } catch (Exception exc) {
                    }
                }

                frmProductionCost.requestEntityObject(entProductionCost);

                if (frmProductionCost.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                //PEMBULATAN
                entProductionCost.setStockValue(Double.valueOf(new java.text.DecimalFormat("#.##").format(entProductionCost.getStockValue())));

                if (entProductionCost.getOID() == 0) {
                    try {
                        long oid = pstProductionCost.insertExc(this.entProductionCost);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        SessProduction.updatePerhitunganGroupPeriod(entProductionCost.getProductionGroupId());
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
                        long oid = pstProductionCost.updateExc(this.entProductionCost);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        SessProduction.updatePerhitunganGroupPeriod(entProductionCost.getProductionGroupId());
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidProductionCost != 0) {
                    try {
                        entProductionCost = PstProductionCost.fetchExc(oidProductionCost);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidProductionCost != 0) {
                    try {
                        entProductionCost = PstProductionCost.fetchExc(oidProductionCost);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidProductionCost != 0) {
                    try {
                        ProductionCost pc = PstProductionCost.fetchExc(oidProductionCost);
                        long oid = PstProductionCost.deleteExc(oidProductionCost);
                        SessProduction.updatePerhitunganGroupPeriod(pc.getProductionGroupId());
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
