/*
 * Ctrl Name  		:  CtrlMcdPeriode.java
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

package com.dimata.common.form.periode;

/* java package */

import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



/* project package */
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;

public class CtrlPeriode extends Control implements I_Language {
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
    private Periode periode;
    private PstPeriode pstPeriode;
    private FrmPeriode frmPeriode;
    int language = LANGUAGE_DEFAULT;

    public CtrlPeriode(HttpServletRequest request) {
        msgString = "";
        periode = new Periode();
        try {
            pstPeriode = new PstPeriode(0);
        } catch (Exception e) {
            ;
        }
        frmPeriode = new FrmPeriode(request, periode);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPeriode.addError(frmPeriode.FRM_FIELD_END_DATE, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Periode getPeriode() {
        return periode;
    }

    public FrmPeriode getForm() {
        return frmPeriode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPeriode) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                Periode mPeriode = new Periode();
                if (oidPeriode != 0) {
                    try {
                        mPeriode = PstPeriode.fetchExc(oidPeriode);
                    } catch (Exception exc) {
                    }
                }

                frmPeriode.requestEntityObject(periode);
                periode.setOID(mPeriode.getOID());
                periode.setPeriodeType(mPeriode.getPeriodeType());
                periode.setStartDate(mPeriode.getStartDate());
                periode.setEndDate(mPeriode.getEndDate());
                periode.setStatus(mPeriode.getStatus());

                if (frmPeriode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (periode.getOID() == 0) {
                    try {
                        long oid = pstPeriode.insertExc(this.periode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstPeriode.updateExc(this.periode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidPeriode != 0) {
                    try {
                        periode = PstPeriode.fetchExc(oidPeriode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPeriode != 0) {
                    try {
                        periode = PstPeriode.fetchExc(oidPeriode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPeriode != 0) {
                    try {
                        long oid = PstPeriode.deleteExc(oidPeriode);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
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
