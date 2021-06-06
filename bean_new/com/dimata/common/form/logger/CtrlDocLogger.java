/*
 * Ctrl Name  		:  CtrlLocation.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  		: karya
 * @version  		: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.form.logger;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.logger.DocLogger;
import com.dimata.common.entity.logger.PstDocLogger;

public class CtrlDocLogger extends Control implements I_Language {
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
    private DocLogger DocLogger;
    private PstDocLogger PstDocLogger;
    private FrmDocLogger frmDocLogger;
    int language = LANGUAGE_DEFAULT;

    public CtrlDocLogger(HttpServletRequest request) {
        msgString = "";
        DocLogger = new DocLogger();
        try {
            PstDocLogger = new PstDocLogger(0);
        } catch (Exception e) {
            ;
        }
        frmDocLogger = new FrmDocLogger(request, DocLogger);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                //this.frmDocLogger.addError(frmDocLogger.FRM_FIELD_LOCATION_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

    public DocLogger getDocLogger() {
        return DocLogger;
    }

    public FrmDocLogger getForm() {
        return frmDocLogger;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidLocation) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String strCode = "";
                String strName = "";
                if (oidLocation != 0) {
                    try {
                        DocLogger = PstDocLogger.fetchExc(oidLocation);
                    } catch (Exception exc) {
                        System.out.println("Exception exc : " + exc.toString());
                    }
                }

                frmDocLogger.requestEntityObject(DocLogger);
                if (frmDocLogger.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (DocLogger.getOID() == 0) {
                    try {
                        long oid = PstDocLogger.insertExc(this.DocLogger);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = PstDocLogger.updateExc(this.DocLogger);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidLocation != 0) {
                    try {
                        DocLogger = PstDocLogger.fetchExc(oidLocation);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidLocation != 0) {
                    try {
                        DocLogger = PstDocLogger.fetchExc(oidLocation);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:

                if (oidLocation != 0) {
                    try {
                        long oid = PstDocLogger.deleteExc(oidLocation);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :

        }
        return rsCode;
    }
}
