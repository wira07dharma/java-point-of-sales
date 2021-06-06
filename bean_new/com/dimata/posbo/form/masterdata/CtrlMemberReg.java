/*
 * Ctrl Name  		:  CtrlMemberReg.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  		:  [authorName]
 * @version  		:  [version]
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 ******************************************************************
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessMemberReg;
import com.dimata.common.entity.contact.PstContactClassAssign;
import com.dimata.common.entity.contact.ContactClass;
import com.dimata.common.entity.contact.ContactClassAssign;
import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.db.OIDFactory;
import com.dimata.qdep.entity.I_DocType;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Vector;

public class CtrlMemberReg extends Control implements I_Language {

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
    private MemberReg memberReg;
    private MemberReg prevMemberReg;
    private PstMemberReg pstMemberReg;
    private FrmMemberReg frmMemberReg;
    int language = LANGUAGE_DEFAULT;
    Date dateLog = new Date();

    public CtrlMemberReg(HttpServletRequest request) {
        msgString = "";
        memberReg = new MemberReg();
        try {
            pstMemberReg = new PstMemberReg(0);
        } catch (Exception e) {
            ;
        }
        frmMemberReg = new FrmMemberReg(request, memberReg);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMemberReg.addError(frmMemberReg.FRM_FIELD_CONTACT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MemberReg getMemberReg() {
        return memberReg;
    }

    public FrmMemberReg getForm() {
        return frmMemberReg;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMemberReg) {
        return action(cmd, oidMemberReg, 0, "");
    }

    public int action(int cmd, long oidMemberReg, long userID, String nameUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                MemberReg memReg = new MemberReg();
                if (oidMemberReg != 0) {
                    try {
                        memberReg = PstMemberReg.fetchExc(oidMemberReg);
                        memReg = PstMemberReg.fetchExc(oidMemberReg);
                    } catch (Exception exc) {
                    }

                    try {
                        prevMemberReg = PstMemberReg.fetchExc(oidMemberReg);
                    } catch (Exception exc) {
                    }
                }

                frmMemberReg.requestEntityObject(memberReg);

                if (frmMemberReg.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                memberReg.setMemberLastUpdate(new Date());
                if (memberReg.getOID() == 0) {
                    memberReg.setRegdate(new Date());
                    if (memberReg.getContactCode() == null || memberReg.getContactCode().length() == 0) {
                        String barcode = "";//PstMemberReg.genBarcode(memberReg);
                        memberReg.setMemberBarcode(barcode);
                        memberReg.setContactCode(barcode);
                        memberReg.setMemberCounter(PstMemberReg.setCounter(memberReg));
                    }

                    try {
                        long oid = pstMemberReg.insertExc(this.memberReg);

                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
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
                        memberReg.setRegdate(memReg.getRegdate());
                        memberReg.setMemberCounter(memReg.getMemberCounter());
                        memberReg.setMemberBarcode(memberReg.getContactCode());
                        memberReg.setProcessStatus(PstMemberReg.UPDATE);

                        //check validate member
                        String whHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID]
                                + " = " + memberReg.getOID();
                        String orHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]
                                + " DESC ";
                        Vector listHistory = PstMemberRegistrationHistory.list(0, 1, whHistory, orHistory);
                        if (listHistory != null && listHistory.size() > 0) {
                            MemberRegistrationHistory memberHis = (MemberRegistrationHistory) listHistory.get(0);
                            if (memberHis.getValidExpiredDate().before(new Date())) {
                                memberReg.setMemberStatus(PstMemberReg.NOT_VALID);
                            }
                        }
                        long oid = pstMemberReg.updateExc(this.memberReg);

                        if (oid != 0) {
                            int cmdUpdate = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdUpdate, oid);
                        }

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                /* contact assign */
                PstContactClassAssign.deleteClassAssign(oidMemberReg);
                ContactClass cClass = PstMemberReg.getContactClass();
                ContactClassAssign cntClsAssign = new ContactClassAssign();
                cntClsAssign.setContactId(memberReg.getOID());
                cntClsAssign.setContactClassId(cClass.getOID());
                try {
                    PstContactClassAssign.insertExc(cntClsAssign);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("err at contactclass assign : " + e.toString());
                }

                break;
            case Command.EDIT:
                if (oidMemberReg != 0) {
                    try {
                        memberReg = PstMemberReg.fetchExc(oidMemberReg);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.UPDATE: // untuk update member point
                if (oidMemberReg != 0) {
                    try {
                        MemberPoin memberPoint = frmMemberReg.requestEntityObjectForUpdate(oidMemberReg);
						MemberPoin memberPoin = PstMemberPoin.getTotalPoint(oidMemberReg);
						int point = memberPoin.getCredit() - (memberPoin.getDebet() + memberPoint.getDebet());
						memberPoint.setCurrentPoint(point);
                        PstMemberPoin.insertExc(memberPoint);
                        insertHistoryPenukaranPoin(userID, nameUser, cmd, oidMemberReg, memberPoint, null);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMemberReg != 0) {
                    try {
                        memberReg = PstMemberReg.fetchExc(oidMemberReg);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMemberReg != 0) {
                    try {
                        if (oidMemberReg != 0) {
                            insertHistory(userID, nameUser, cmd, oidMemberReg);
                        }

                        memberReg = PstMemberReg.fetchExc(oidMemberReg);
                        long oid = 0;
                        if (SessMemberReg.readyDataToDelete(oidMemberReg)) {
                            memberReg.setContactCode("" + memberReg.getOID());
                            memberReg.setMemberBarcode("" + memberReg.getOID());
                            memberReg.setProcessStatus(PstMemberReg.DELETE);
                            memberReg.setMemberLastUpdate(new Date());
                            oid = PstMemberReg.updateExc(memberReg);
                        }
                        if (oid != 0) {
                            PstMemberRegistrationHistory.deleteByIdMember(oid);
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmMemberReg.addError(FrmMemberReg.FRM_FIELD_MEMBER_GROUP_ID, "");
                            msgString = "Hapus data gagal, data masih digunakan oleh data lain.";
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

    /**
     * Insert Data ke log History
     */
    public void insertHistory(long userID, String nameUser, int cmd, long oid) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("masterdata/memberreg_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.systemModulMenu[0]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(memberReg.getContactCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.memberReg.getLogDetail(prevMemberReg));

            //if(!"".equals(logSysHistory.getLogDetail()))
            if (!"".equals(logSysHistory.getLogDetail()) || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    //added by dewok 20180328
    public void insertHistoryPenukaranPoin(long userID, String nameUser, int cmd, long oid, MemberPoin memberPoin, MemberPoin prevMemberPoin) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("masterdata/memberreg_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.systemModulMenu[0]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(memberReg.getContactCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(memberPoin.getLogDetail(prevMemberPoin));

            //if(!"".equals(logSysHistory.getLogDetail()))
            if (!"".equals(logSysHistory.getLogDetail()) || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
    /*
     * Description : insertHistoryInvoice
     * Author : Hendra McHen
     * Date : 2014-12-22 (Hari Ibu)
     */

    public void insertHistoryInvoice(long userID, String nameUser, int cmd, long oid, String itemName) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("masterdata/memberreg_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.systemModulMenu[0]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(memberReg.getContactCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail("Invoicing melebihi credit limit; Item name: " + itemName);

            //if(!"".equals(logSysHistory.getLogDetail()))
            if (!"".equals(logSysHistory.getLogDetail()) || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    public long actionSaveFromBill(int cmd, long oidContact, int contactClassType, String Name, String phoneNumber, String Address) {

        msgString = "";

        int excCode = I_DBExceptionInfo.NO_EXCEPTION;

        int rsCode = RSLT_OK;

        long oid = 0;

        switch (cmd) {

            case Command.ADD:
                break;

            case Command.SAVE:

                memberReg.setPersonName(Name);
                memberReg.setTelpMobile(phoneNumber);
                memberReg.setHomeAddr(phoneNumber);
                memberReg.setHomeTelp(phoneNumber);
                memberReg.setHomeAddr(Address);

                memberReg.setContactCode("" + OIDFactory.generateOID());
                memberReg.setContactType(contactClassType);
                Date newDate = new Date();
                memberReg.setRegdate(newDate);

                if (memberReg.getOID() == 0) {

                    //insert contact
                    try {
                        oid = pstMemberReg.insertExc(this.memberReg);
                    } catch (DBException dbexc) {

                        excCode = dbexc.getErrorCode();

                        msgString = getSystemMessage(excCode);

                        return getControlMsgId(excCode);

                    } catch (Exception exc) {

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

                    }

                    //insert contact_class_assignt
                    PstContactClassAssign.deleteClassAssign(oid);
                    String whClause = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + contactClassType;
                    Vector vectContactClass = PstContactClass.list(0, 0, whClause, "");
                    long contactClassId = 0;
                    if (vectContactClass != null && vectContactClass.size() > 0) {
                        ContactClass contactClass = (ContactClass) vectContactClass.get(0);
                        contactClassId = contactClass.getOID();
                    }

                    ContactClassAssign contactClassAssign = new ContactClassAssign();

                    contactClassAssign.setContactId(oid);

                    contactClassAssign.setContactClassId(contactClassId);

                    try {
                        PstContactClassAssign.insertExc(contactClassAssign);
                    } catch (DBException dbexc) {

                        excCode = dbexc.getErrorCode();

                        msgString = getSystemMessage(excCode);

                        return getControlMsgId(excCode);

                    } catch (Exception exc) {

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

                    }

                } else {

                }
                break;

            default:

        }

        return oid;
    }

}
