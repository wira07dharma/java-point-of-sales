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
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.posbo.session.warehouse.SessProduction;
import com.dimata.qdep.entity.I_DocStatus;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class CtrlProduction extends Control implements I_Language {

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
    private Production entProduction;
    private PstProduction pstProduction;
    private FrmProduction frmProduction;
    int language = LANGUAGE_DEFAULT;

    public CtrlProduction(HttpServletRequest request) {
        msgString = "";
        entProduction = new Production();
        try {
            pstProduction = new PstProduction(0);
        } catch (Exception e) {;
        }
        frmProduction = new FrmProduction(request, entProduction);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmProduction.addError(frmProduction.FRM_FIELD_PRODUCTION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Production getProduction() {
        return entProduction;
    }

    public FrmProduction getForm() {
        return frmProduction;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidProduction) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidProduction != 0) {
                    try {
                        entProduction = PstProduction.fetchExc(oidProduction);
                    } catch (Exception exc) {
                    }
                }

                frmProduction.requestEntityObject(entProduction);

                if (frmProduction.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                entProduction.setOID(oidProduction);

                if (entProduction.getProductionDate() == null) {
                    msgString = "Tanggal produksi harus diisi dengan benar !";
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entProduction.getLocationToId() == 0) {
                    entProduction.setLocationToId(entProduction.getLocationFromId());
                }

                if (entProduction.getOID() == 0) {
                    //GENERATE KODE
                    entProduction.setProductionNumber(SessProduction.generateProductionCode(entProduction));
                }

                if (entProduction.getOID() == 0) {
                    try {
                        long oid = pstProduction.insertExc(this.entProduction);
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
                        long oid = pstProduction.updateExc(this.entProduction);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        if (entProduction.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                            SessPosting sessPosting = new SessPosting();
                            sessPosting.postedProductionDoc(oidProduction);
                            entProduction = PstProduction.fetchExc(oidProduction);
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
                if (oidProduction != 0) {
                    try {
                        entProduction = PstProduction.fetchExc(oidProduction);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidProduction != 0) {
                    try {
                        entProduction = PstProduction.fetchExc(oidProduction);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidProduction != 0) {
                    try {
                        Production p = PstProduction.fetchExc(oidProduction);
                        if (p.getProductionStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                            msgString = "Tidak dapat menghapus produksi dengan status " + I_DocStatus.fieldDocumentStatus[p.getProductionStatus()] + " !";
                            return RSLT_UNKNOWN_ERROR;
                        }
                        deleteAllGroupPeriod(oidProduction);
                        long oid = PstProduction.deleteExc(oidProduction);
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
        return excCode;
    }

    public void deleteAllGroupPeriod(long productionId) throws DBException {
        Vector<ProductionGroup> listAllGroup = PstProductionGroup.list(0, 0, PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID] + " = " + productionId, "");
        for (ProductionGroup group : listAllGroup) {
            deleteAllCost(group.getOID());
            deleteAllProduct(group.getOID());
            PstProductionGroup.deleteExc(group.getOID());
        }
    }

    public void deleteAllCost(long groupId) throws DBException {
        Vector<ProductionCost> listAllCost = PstProductionCost.list(0, 0, PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID] + " = " + groupId, "");
        for (ProductionCost cost : listAllCost) {
            PstProductionCost.deleteExc(cost.getOID());
        }
    }

    public void deleteAllProduct(long groupId) throws DBException {
        Vector<ProductionProduct> listAllProduct = PstProductionProduct.list(0, 0, PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = " + groupId, "");
        for (ProductionProduct product : listAllProduct) {
            PstProductionProduct.deleteExc(product.getOID());
        }
    }
}
