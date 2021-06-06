/*
 * Ctrl Name  		:  CtrlContactList.java
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

package com.dimata.common.form.contact;

/* java package */

import java.util.*;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.session.contact.SessContactList;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;

public class CtrlContactList extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Kode supplier sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Supplier  code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private ContactList contactList;
    private PstContactList pstContactList;
    private FrmContactList frmContactList;
    int language = LANGUAGE_DEFAULT;

    public CtrlContactList(HttpServletRequest request) {
        msgString = "";
        contactList = new ContactList();
        try {
            pstContactList = new PstContactList(0);
        } catch (Exception e) {
            ;
        }
        frmContactList = new FrmContactList(request, contactList);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmContactList.addError(frmContactList.FRM_FIELD_CONTACT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ContactList getContactList() {
        return contactList;
    }

    public FrmContactList getForm() {
        return frmContactList;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    //public int action(int cmd, long oidContactList, Vector vectClassAssign)  {
      public int action(int cmd, long oidContactList, Vector vectClassAssign) throws Exception {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidContactList != 0) {
                    try {
                        contactList = PstContactList.fetchExc(oidContactList);
                    } catch (Exception exc) {
                    }
                }

                frmContactList.requestEntityObject(contactList);

                contactList.setLastUpdate(new Date());


                if (frmContactList.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (contactList.getOID() == 0) {
                    boolean bool = PstContactList.cekCodeContact(contactList.getContactCode(), oidContactList);
                    if (bool)
                        frmContactList.addError(frmContactList.FRM_FIELD_CONTACT_CODE, resultText[language][RSLT_EST_CODE_EXIST]);
                    try {
                        long oid = pstContactList.insertExc(this.contactList);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {

                    try {
                        contactList.setProcessStatus(PstContactList.UPDATE);
                        long oid = pstContactList.updateExc(this.contactList);

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                }

                PstContactClassAssign.deleteClassAssign(oidContactList);
                if ((vectClassAssign != null) && (vectClassAssign.size() > 0)) {
                    for (int i = 0; i < vectClassAssign.size(); i++) {
                        ContactClassAssign cntClsAssign = (ContactClassAssign) vectClassAssign.get(i);
                        cntClsAssign.setContactId(contactList.getOID());
                        PstContactClassAssign.insertExc(cntClsAssign);
                    }
                }

                break;

            case Command.EDIT:
                if (oidContactList != 0) {
                    try {
                        contactList = PstContactList.fetchExc(oidContactList);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidContactList != 0) {
                    try {
                        contactList = PstContactList.fetchExc(oidContactList);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidContactList != 0) {
                    try {
                        contactList = PstContactList.fetchExc(oidContactList);
                        long oid = 0;
                        if (SessContactList.readyDataToDelete(oidContactList)) {
                            contactList.setProcessStatus(PstContactList.DELETE);
                            contactList.setLastUpdate(new Date());
                            oid = PstContactList.updateExc(contactList);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmContactList.addError(FrmContactList.FRM_FIELD_CONTACT_CODE,"");
                            msgString = "Hapus data gagal, data sudah terpakai di transaksi lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
