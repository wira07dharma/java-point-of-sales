/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.masterdata.ChainPeriod;
import com.dimata.posbo.entity.masterdata.PstChainPeriod;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.SessProduction;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class CtrlProductionProduct extends Control implements I_Language {

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
    private ProductionProduct entProductionProduct;
    private PstProductionProduct pstProductionProduct;
    private FrmProductionProduct frmProductionProduct;
    int language = LANGUAGE_DEFAULT;

    public CtrlProductionProduct(HttpServletRequest request) {
        msgString = "";
        entProductionProduct = new ProductionProduct();
        try {
            pstProductionProduct = new PstProductionProduct(0);
        } catch (Exception e) {;
        }
        frmProductionProduct = new FrmProductionProduct(request, entProductionProduct);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmProductionProduct.addError(frmProductionProduct.FRM_FIELD_PRODUCTION_PRODUCT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ProductionProduct getProductionProduct() {
        return entProductionProduct;
    }

    public FrmProductionProduct getForm() {
        return frmProductionProduct;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidProductionProduct, long userId, String userName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidProductionProduct != 0) {
                    try {
                        entProductionProduct = PstProductionProduct.fetchExc(oidProductionProduct);
                    } catch (Exception exc) {
                    }
                }

                frmProductionProduct.requestEntityObject(entProductionProduct);

                if (frmProductionProduct.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                //CEK PERIODE DISTRIBUSI
                if (entProductionProduct.getMaterialType() == PstProductionProduct.TYPE_NEXT_COST) {
                    if (oidProductionProduct == 0) {
                        //CEK APAKAH SUDAH ADA ITEM NEXT COST / DISTRIBUSI
                        int count = PstProductionProduct.getCount(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = " + entProductionProduct.getProductionGroupId() + " AND " + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_TYPE] + " = " + PstProductionProduct.TYPE_NEXT_COST);
                        if (count > 0) {
                            msgString = "Material distribusi sudah ada untuk periode ini !";
                            return RSLT_FORM_INCOMPLETE;
                        }
                    }

                    //CEK VALIDASI
                    if (entProductionProduct.getPeriodDistribution() <= 0) {
                        msgString = "Jumlah periode untuk item distribusi tidak boleh " + entProductionProduct.getPeriodDistribution() + " !";
                        return RSLT_FORM_INCOMPLETE;
                    }

                    //CEK PERIODE SELANJUTNYA SUDAH ADA
//                    untuk saat ini, di modul produksi, item distribusi boleh dibuat walaupun belum ada group periode selanjutnya,
//                    karena cost reference akan digenerate saat group period selanjutnya dibuat
//                    int nextPeriod = SessProduction.countNextPeriodExist(entProductionProduct.getProductionGroupId());
//                    if (nextPeriod < entProductionProduct.getPeriodDistribution()) {
//                        msgString = "Jumlah periode slanjutnya tidak cukup untuk " + entProductionProduct.getPeriodDistribution() + " periode distribusi !";
//                        return RSLT_FORM_INCOMPLETE;
//                    }
                }

                //PEMBULATAN
                entProductionProduct.setCostPct(Double.valueOf(new java.text.DecimalFormat("#.##").format(entProductionProduct.getCostPct())));
                entProductionProduct.setCostValue(Double.valueOf(new java.text.DecimalFormat("#.##").format(entProductionProduct.getCostValue())));
                entProductionProduct.setSalesValue(Double.valueOf(new java.text.DecimalFormat("#.##").format(entProductionProduct.getSalesValue())));

                if (entProductionProduct.getOID() == 0) {
                    try {
                        long oid = pstProductionProduct.insertExc(this.entProductionProduct);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        SessProduction.updateItemDistribution(oid);
                        SessProduction.updatePerhitunganGroupPeriod(entProductionProduct.getProductionGroupId());
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
                        long oid = pstProductionProduct.updateExc(this.entProductionProduct);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        SessProduction.updateItemDistribution(oid);
                        SessProduction.updatePerhitunganGroupPeriod(entProductionProduct.getProductionGroupId());
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidProductionProduct != 0) {
                    try {
                        entProductionProduct = PstProductionProduct.fetchExc(oidProductionProduct);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidProductionProduct != 0) {
                    try {
                        entProductionProduct = PstProductionProduct.fetchExc(oidProductionProduct);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidProductionProduct != 0) {
                    try {
                        ProductionProduct pp = PstProductionProduct.fetchExc(oidProductionProduct);
                        long oid = PstProductionProduct.deleteExc(oidProductionProduct);
                        SessProduction.deleteCostDistribution(oid);
                        SessProduction.updatePerhitunganGroupPeriod(pp.getProductionGroupId());
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
}
