/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.balance;

/**
 *
 * @author Ari Wiweka
 * 11/06/2013
 */
import java.util.*;
import javax.servlet.http.*;

// dimata package
import com.dimata.util.*;
import com.dimata.util.lang.*;

// qdep package
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
//import com.dimata.qdep.db.*;

// project package
import com.dimata.pos.entity.balance.*;
import com.dimata.posbo.db.DBException;
import com.dimata.pos.session.masterCashier.SessMasterCashier;
import com.dimata.posbo.entity.admin.*;

public class CtrlCashCashier extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material Sales sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material Sales Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private CashCashier cashCashier;
    private OpeningCashCashier openingCashCashier;
    private PstCashCashier pstCashCashier;
    private FrmCashCashier frmCashCashier;

    //untuk cash_balance
    private Balance balance;
    private PstBalance pstBalance;
    int language = LANGUAGE_FOREIGN;

    public CtrlCashCashier(HttpServletRequest request) {
        msgString = "";
        cashCashier = new CashCashier();
        try {
            pstCashCashier = new PstCashCashier(0);
        } catch (Exception e) {
            ;
        }
        frmCashCashier = new FrmCashCashier(request, cashCashier);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCashCashier.addError(frmCashCashier.FRM_FIELD_CASH_CASHIER_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CashCashier getCashCashier() {
        return cashCashier;
    }

    public OpeningCashCashier getOpeningCashCashier() {
        return openingCashCashier;
    }

    public FrmCashCashier getForm() {
        return frmCashCashier;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }



    public int action(int cmd, long oidCashCashier, HttpServletRequest request, long oidSupervisor) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCashCashier != 0) {
                    try {
                        cashCashier = PstCashCashier.fetchExc(oidCashCashier);
                    } catch (Exception exc) {
                    }
                }

                
                
                frmCashCashier.requestEntityObject(cashCashier);
                //set data supervisor
                cashCashier.setSpvOid(oidSupervisor);
                cashCashier.setCashDate(new Date());

                String supervisorName = "";
                PstAppUser pstAppUser = new PstAppUser();
                AppUser appUser = pstAppUser.fetch(oidSupervisor);
                supervisorName = appUser.getFullName();
                cashCashier.setSpvName(supervisorName);

                if (frmCashCashier.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (cashCashier.getOID() == 0) {
                    try {
                        long oid = pstCashCashier.insertExc(this.cashCashier);
                        if(oid!=0){
                            //proses insert cash_balnce
                            Balance balance = new Balance();
                            balance.setCashCashierId(cashCashier.getOID());
                            balance.setBalanceValue(cashCashier.getSubTotal1());
                            balance.setCurrencyOid(cashCashier.getCurrencyId());
                            balance.setBalanceDate(new Date());
                            balance.setBalanceType(cashCashier.getType());

                            oid = pstBalance.insertExc(balance);

                            if(cashCashier.getCurrencyId2()!= 0){
                            Balance balanceOther = new Balance();
                            balanceOther.setCashCashierId(cashCashier.getOID());
                            balanceOther.setBalanceValue(cashCashier.getSubTotal2());
                            balanceOther.setCurrencyOid(cashCashier.getCurrencyId2());
                            balanceOther.setBalanceDate(new Date());
                            balanceOther.setBalanceType(cashCashier.getType());

                            oid = pstBalance.insertExc(balanceOther);
                            }
                        }else{
                            msgString = getSystemMessage(1);
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
                        long oid = pstCashCashier.updateExc(this.cashCashier);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidCashCashier != 0) {
                    try {
                        cashCashier = PstCashCashier.fetchExc(oidCashCashier);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCashCashier != 0) {
                    try {
                        cashCashier = PstCashCashier.fetchExc(oidCashCashier);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCashCashier != 0) {
                    try {
                        long oid = 0;
                        if(SessMasterCashier.readyDataToDelete(oidCashCashier)){
                            oid = PstCashCashier.deleteExc(oidCashCashier);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            cashCashier = PstCashCashier.fetchExc(oidCashCashier);
                            frmCashCashier.addError(FrmCashCashier.FRM_FIELD_CASH_CASHIER_ID, "");
                            msgString = "Hapus data gagal, data sudah terpakai di modul lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :

        }
        return excCode;
    }

}
