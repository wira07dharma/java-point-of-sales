package com.dimata.posbo.form.warehouse;

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
import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.*;

/* project package */
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.common.entity.logger.PstDocLogger;

public class CtrlMatConReturn extends Control implements I_Language {
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
    private MatConReturn matReturn;
    private PstMatConReturn pstMatConReturn;
    private FrmMatConReturn frmMatConReturn;
    private HttpServletRequest req;
    int language = LANGUAGE_DEFAULT;

    public CtrlMatConReturn(HttpServletRequest request) {
        msgString = "";
        matReturn = new MatConReturn();
        try {
            pstMatConReturn = new PstMatConReturn(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMatConReturn = new FrmMatConReturn(request, matReturn);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatConReturn.addError(frmMatConReturn.FRM_FIELD_RETURN_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatConReturn getMatConReturn() {
        return matReturn;
    }

    public FrmMatConReturn getForm() {
        return frmMatConReturn;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMatConReturn) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                boolean incrementAllReturnType = true;
                Date rDate = new Date();
                int recType = 0;
                int counter = 0;
                if (oidMatConReturn != 0) {
                    try {
                        matReturn = PstMatConReturn.fetchExc(oidMatConReturn);
                        rDate = matReturn.getReturnDate();
                        recType = matReturn.getReturnStatus();
                        counter = matReturn.getRetCodeCnt();
                    } catch (Exception exc) {
                    }
                }
                
                frmMatConReturn.requestEntityObject(matReturn);
                Date date = ControlDate.getDateTime(frmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE], req);
                matReturn.setReturnDate(date);
                
                if (frmMatConReturn.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                int docType = -1;
                try {
                    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                    docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_ROMR);
                } catch (Exception e) {
                }


                if (matReturn.getOID() == 0) {
                    try {
                        this.matReturn.setRetCodeCnt(SessMatConReturn.getIntCode(matReturn, rDate, oidMatConReturn, counter));
                        this.matReturn.setRetCode(SessMatConReturn.getCodeReturn(matReturn));
                        matReturn.setLastUpdate(new Date());
                        long oid = pstMatConReturn.insertExc(this.matReturn);

                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matReturn.getRetCode(), I_DocType.MAT_DOC_TYPE_ROMR, matReturn.getLastUpdate(), matReturn.getRemark());
                        //--- end

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
                        matReturn.setLastUpdate(new Date());
                        matReturn.setRetCodeCnt(counter);
                        long oid = pstMatConReturn.updateExc(this.matReturn);

                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matReturn.getRetCode(), I_DocType.MAT_DOC_TYPE_ROMR, matReturn.getLastUpdate(), matReturn.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidMatConReturn != 0) {
                    try {
                        matReturn = PstMatConReturn.fetchExc(oidMatConReturn);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatConReturn != 0) {
                    try {
                        matReturn = PstMatConReturn.fetchExc(oidMatConReturn);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatConReturn != 0) {
                    try {

                        String whereClause = PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidMatConReturn;
                        Vector vect = PstMatConReturnItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatConReturnItem matDispatchItem = (MatConReturnItem) vect.get(k);
                                CtrlMatConReturnItem ctrlMatDpsItm = new CtrlMatConReturnItem();
                                ctrlMatDpsItm.action(Command.DELETE, matDispatchItem.getOID(), oidMatConReturn);
                            }
                        }

                        /** gadnyan
                         * proses penghapusan di doc logger
                         * jika tidak di perlukan uncoment perintah ini
                         */
                        matReturn = PstMatConReturn.fetchExc(oidMatConReturn);
                        PstDocLogger.deleteDataBo_inDocLogger(matReturn.getRetCode(),I_DocType.MAT_DOC_TYPE_ROMR);
                        // -- end

                        long oid = PstMatConReturn.deleteExc(oidMatConReturn);
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

            default :

        }
        return rsCode;
    }
}
