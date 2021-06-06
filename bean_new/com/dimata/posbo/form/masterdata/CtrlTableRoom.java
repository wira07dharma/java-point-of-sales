/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.db.DBException;

/**
 *
 * @author Dimata 007
 */
public class CtrlTableRoom extends Control implements I_Language {
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Table Room sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Room Table Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private TableRoom tableRoom;
    private PstTableRoom pstTableRoom;
    private FrmTableRoom frmTableRoom;
    int language = LANGUAGE_FOREIGN;

    public CtrlTableRoom(HttpServletRequest request) {
        msgString = "";
        tableRoom = new TableRoom();
        try {
            pstTableRoom = new PstTableRoom(0);
        } catch (Exception e) {
            ;
        }
        frmTableRoom = new FrmTableRoom(request, tableRoom);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmTableRoom.addError(frmTableRoom.FRM_FIELD_TABLE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public TableRoom getTableRoom() {
        return tableRoom;
    }

    public FrmTableRoom getForm() {
        return frmTableRoom;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidTableRoom, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidTableRoom != 0) {
                    try {
                        tableRoom = PstTableRoom.fetchExc(oidTableRoom);
                    } catch (Exception exc) {
                    }
                }

                frmTableRoom.requestEntityObject(tableRoom);
                
                if (frmTableRoom.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

       

                if (tableRoom.getOID() == 0) {
                    try {
                        long oid = pstTableRoom.insertExc(this.tableRoom);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstTableRoom.updateExc(this.tableRoom);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidTableRoom != 0) {
                    try {
                        tableRoom = PstTableRoom.fetchExc(oidTableRoom);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidTableRoom != 0) {
                    try {
                        tableRoom = PstTableRoom.fetchExc(oidTableRoom);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidTableRoom != 0) {

                    try {
                        long oid = PstTableRoom.deleteExc(oidTableRoom);
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
        return excCode;
    }
    
    
}
