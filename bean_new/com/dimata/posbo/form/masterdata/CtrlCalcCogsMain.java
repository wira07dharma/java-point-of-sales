/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.masterdata.*;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class CtrlCalcCogsMain extends Control implements I_Language {

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
    private CalcCogsMain entCalcCogsMain;
    private PstCalcCogsMain pstCalcCogsMain;
    private FrmCalcCogsMain frmCalcCogsMain;
    int language = LANGUAGE_DEFAULT;

    public CtrlCalcCogsMain(HttpServletRequest request) {
        msgString = "";
        entCalcCogsMain = new CalcCogsMain();
        try {
            pstCalcCogsMain = new PstCalcCogsMain(0);
        } catch (Exception e) {;
        }
        frmCalcCogsMain = new FrmCalcCogsMain(request, entCalcCogsMain);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCalcCogsMain.addError(frmCalcCogsMain.FRM_FIELD_CALC_COGS_MAIN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CalcCogsMain getCalcCogsMain() {
        return entCalcCogsMain;
    }

    public FrmCalcCogsMain getForm() {
        return frmCalcCogsMain;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCalcCogsMain, long userId, String userName, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCalcCogsMain != 0) {
                    try {
                        entCalcCogsMain = PstCalcCogsMain.fetchExc(oidCalcCogsMain);
                    } catch (Exception exc) {
                    }
                }

                frmCalcCogsMain.requestEntityObject(entCalcCogsMain);

                if (frmCalcCogsMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entCalcCogsMain.getOID() == 0) {
                    try {
                        long oid = pstCalcCogsMain.insertExc(this.entCalcCogsMain);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        
                        //save multiple location cost
                        String multiLocationCost[] = FRMQueryString.requestStringValues(request, "MULTIPLE_LOCATION_COST");
                        if (multiLocationCost != null) {
                            for (int i = 0; i < multiLocationCost.length; i++) {
                                CalcCogsLocation ccl = new CalcCogsLocation();
                                ccl.setCalcCogsMainId(oid);
                                ccl.setLocationId(Long.valueOf(multiLocationCost[i]));
                                ccl.setLocationCalcCogsType(PstCalcCogsLocation.CALC_COGS_LOC_TYPE_COST);
                                PstCalcCogsLocation.insertExc(ccl);
                            }
                        }
                        
                        //save multiple location sales
                        String multiLocationSales[] = FRMQueryString.requestStringValues(request, "MULTIPLE_LOCATION_SALES");
                        if (multiLocationSales != null) {
                            for (int i = 0; i < multiLocationSales.length; i++) {
                                CalcCogsLocation ccl = new CalcCogsLocation();
                                ccl.setCalcCogsMainId(oid);
                                ccl.setLocationId(Long.valueOf(multiLocationSales[i]));
                                ccl.setLocationCalcCogsType(PstCalcCogsLocation.CALC_COGS_LOC_TYPE_SALES);
                                PstCalcCogsLocation.insertExc(ccl);
                            }
                        }
                        
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
                        long oid = pstCalcCogsMain.updateExc(this.entCalcCogsMain);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        
                        //delete last location cost
                        String whereLocCost = "" + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID] + " = " + oidCalcCogsMain
                                + " AND " + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_COST;
                        Vector<CalcCogsLocation> listLocationCost = PstCalcCogsLocation.list(0, 0, whereLocCost, "");
                        for (int i = 0; i < listLocationCost.size(); i++) {
                            long locationCostId = listLocationCost.get(i).getOID();
                            PstCalcCogsLocation.deleteExc(locationCostId);
                        }                            
                        
                        //save multiple location cost
                        String multiLocationCost[] = FRMQueryString.requestStringValues(request, "MULTIPLE_LOCATION_COST");
                        if (multiLocationCost != null) {
                            for (int i = 0; i < multiLocationCost.length; i++) {
                                CalcCogsLocation ccl = new CalcCogsLocation();
                                ccl.setCalcCogsMainId(oidCalcCogsMain);
                                ccl.setLocationId(Long.valueOf(multiLocationCost[i]));
                                ccl.setLocationCalcCogsType(PstCalcCogsLocation.CALC_COGS_LOC_TYPE_COST);
                                PstCalcCogsLocation.insertExc(ccl);
                            }
                        }
                                                
                        //delete last location sales
                        String whereLocSales = "" + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID] + " = " + oidCalcCogsMain
                                + " AND " + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_SALES;
                        Vector<CalcCogsLocation> listLocationMain = PstCalcCogsLocation.list(0, 0, whereLocSales, "");
                        for (int i = 0; i < listLocationMain.size(); i++) {
                            long locationSalesId = listLocationMain.get(i).getOID();
                            PstCalcCogsLocation.deleteExc(locationSalesId);
                        }                            
                        
                        //save multiple location sales
                        String multiLocationSales[] = FRMQueryString.requestStringValues(request, "MULTIPLE_LOCATION_SALES");
                        if (multiLocationSales != null) {
                            for (int i = 0; i < multiLocationSales.length; i++) {
                                CalcCogsLocation ccl = new CalcCogsLocation();
                                ccl.setCalcCogsMainId(oidCalcCogsMain);
                                ccl.setLocationId(Long.valueOf(multiLocationSales[i]));
                                ccl.setLocationCalcCogsType(PstCalcCogsLocation.CALC_COGS_LOC_TYPE_SALES);
                                PstCalcCogsLocation.insertExc(ccl);
                            }
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
                if (oidCalcCogsMain != 0) {
                    try {
                        entCalcCogsMain = PstCalcCogsMain.fetchExc(oidCalcCogsMain);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCalcCogsMain != 0) {
                    try {
                        entCalcCogsMain = PstCalcCogsMain.fetchExc(oidCalcCogsMain);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCalcCogsMain != 0) {
                    try {
                        long oid = PstCalcCogsMain.deleteExc(oidCalcCogsMain);
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
