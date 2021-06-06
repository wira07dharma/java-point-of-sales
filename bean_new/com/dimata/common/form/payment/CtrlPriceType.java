/*
 * Ctrl Name  		:  CtrlPriceType.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  		:  [authorName]
 * @version  		:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.form.payment;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.posbo.db.DBException;
import com.dimata.common.session.currency.SessCurrencyType;

public class CtrlPriceType extends Control implements I_Language {
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
    private PriceType priceType;
    private PstPriceType pstPriceType;
    private FrmPriceType frmPriceType;
    int language = LANGUAGE_DEFAULT;

    public CtrlPriceType(HttpServletRequest request) {
        msgString = "";
        priceType = new PriceType();
        try {
            pstPriceType = new PstPriceType(0);
        } catch (Exception e) {
            ;
        }
        frmPriceType = new FrmPriceType(request, priceType);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPriceType.addError(frmPriceType.FRM_FIELD_PRICE_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public PriceType getPriceType() {
        return priceType;
    }

    public FrmPriceType getForm() {
        return frmPriceType;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPriceType) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPriceType != 0) {
                    try {
                        priceType = PstPriceType.fetchExc(oidPriceType);
                    } catch (Exception exc) {
                    }
                }

                frmPriceType.requestEntityObject(priceType);

                if (frmPriceType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (priceType.getOID() == 0) {
                    try {
                        long oid = pstPriceType.insertExc(this.priceType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstPriceType.updateExc(this.priceType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidPriceType != 0) {
                    try {
                        priceType = PstPriceType.fetchExc(oidPriceType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPriceType != 0) {
                    try {
                        priceType = PstPriceType.fetchExc(oidPriceType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPriceType != 0) {
                    try {
                        long oid = 0;
                        if(SessCurrencyType.readyPriceTypeDataToDelete(oidPriceType)){
                            oid = PstPriceType.deleteExc(oidPriceType);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmPriceType.addError(FrmPriceType.FRM_FIELD_PRICE_TYPE_ID,"");
                            msgString = "Hapus data gagal, data masih digunakan oleh modul lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
