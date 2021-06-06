
package com.dimata.common.form.contact;

/**
 *
 * @author Witar
 */
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.contact.ContactListPath;
import com.dimata.common.entity.contact.PstContactListPath;

public class CtrlContactListPath extends Control implements I_Language {
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
    private ContactListPath entContactListPath;
    private PstContactListPath pstContactListPath;
    private FrmContactListPath frmContactListPath;
    int language = LANGUAGE_DEFAULT;

    public CtrlContactListPath(HttpServletRequest request) {
        msgString = "";
        entContactListPath = new ContactListPath();
    try {
        pstContactListPath = new PstContactListPath(0);
    } catch (Exception e) {;
    }
        frmContactListPath = new FrmContactListPath(request, entContactListPath);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmContactListPath.addError(frmContactListPath.FRM_FIELD_CONTACT_LIST_PATH, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ContactListPath getContactListPath() {
        return entContactListPath;
    }

    public FrmContactListPath getForm() {
        return frmContactListPath;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidContactListPath) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
            break;
            case Command.SAVE:
                if (oidContactListPath != 0) {
                    try {
                        entContactListPath = PstContactListPath.fetchExc(oidContactListPath);
                    } catch (Exception exc) {
                    }
                }

                frmContactListPath.requestEntityObject(entContactListPath);

                if (frmContactListPath.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entContactListPath.getOID() == 0) {
                    try {
                        long oid = pstContactListPath.insertExc(this.entContactListPath);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstContactListPath.updateExc(this.entContactListPath);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;
            case Command.EDIT:
                if (oidContactListPath != 0) {
                    try {
                        entContactListPath = PstContactListPath.fetchExc(oidContactListPath);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;

            case Command.ASK:
                if (oidContactListPath != 0) {
                    try {
                        entContactListPath = PstContactListPath.fetchExc(oidContactListPath);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;

            case Command.DELETE:
                if (oidContactListPath != 0) {
                    try {
                        long oid = PstContactListPath.deleteExc(oidContactListPath);
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

            default:

        }
        return rsCode;
    }
}
