/*
 * Ctrl Name  		:  CtrlMemberGroup.java
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

package com.dimata.posbo.form.masterdata;

/* java package */

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessMemberGroup;

public class CtrlMemberGroup extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Kode tipe member sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Group member code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private MemberGroup memberGroup;
    private PstMemberGroup pstMemberGroup;
    private FrmMemberGroup frmMemberGroup;
    int language = LANGUAGE_DEFAULT;

    public CtrlMemberGroup(HttpServletRequest request) {
        msgString = "";
        memberGroup = new MemberGroup();
        try {
            pstMemberGroup = new PstMemberGroup(0);
        } catch (Exception e) {
            ;
        }
        frmMemberGroup = new FrmMemberGroup(request, memberGroup);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMemberGroup.addError(frmMemberGroup.FRM_FIELD_MEMBER_GROUP_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MemberGroup getMemberGroup() {
        return memberGroup;
    }

    public FrmMemberGroup getForm() {
        return frmMemberGroup;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMemberGroup) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMemberGroup != 0) {
                    try {
                        memberGroup = PstMemberGroup.fetchExc(oidMemberGroup);
                    } catch (Exception exc) {
                    }
                }

                frmMemberGroup.requestEntityObject(memberGroup);

                if (frmMemberGroup.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (memberGroup.getOID() == 0) {
                    try {
                        long oid = pstMemberGroup.insertExc(this.memberGroup);
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
                        long oid = pstMemberGroup.updateExc(this.memberGroup);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMemberGroup != 0) {
                    try {
                        memberGroup = PstMemberGroup.fetchExc(oidMemberGroup);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMemberGroup != 0) {
                    try {
                        memberGroup = PstMemberGroup.fetchExc(oidMemberGroup);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMemberGroup != 0) {
                    try {
                        long oid = 0;
                        if (SessMemberGroup.readyDataToDelete(oidMemberGroup)) {
                            oid = PstMemberGroup.deleteExc(oidMemberGroup);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            memberGroup = PstMemberGroup.fetchExc(oidMemberGroup);
                            frmMemberGroup.addError(FrmMemberGroup.FRM_FIELD_CODE, "");
                            msgString = "Hapus data gagal, data digunakan oleh data lain"; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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

            default :

        }
        return excCode;
    }
}
