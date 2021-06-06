package com.dimata.posbo.form.warehouse;

/* java package */

import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_IJGeneral;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.common.entity.logger.PstDocLogger;

public class CtrlMatConDispatch extends Control implements I_Language {
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
    private MatConDispatch matDispatch;
    private PstMatConDispatch pstMatConDispatch;
    private FrmMatConDispatch frmMatConDispatch;
    private HttpServletRequest req;
    int language = LANGUAGE_DEFAULT;
    long oid = 0;
    
    public CtrlMatConDispatch(HttpServletRequest request) {
        msgString = "";
        matDispatch = new MatConDispatch();
        try {
            pstMatConDispatch = new PstMatConDispatch(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMatConDispatch = new FrmMatConDispatch(request, matDispatch);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatConDispatch.addError(frmMatConDispatch.FRM_FIELD_DISPATCH_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
    
    public MatConDispatch getMatConDispatch() {
        return matDispatch;
    }
    
    public FrmMatConDispatch getForm() {
        return frmMatConDispatch;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public long getOidTransfer() {
        return oid;
    }
    
    public int action(int cmd, long oidMatConDispatch) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
                
            case Command.SAVE:
                if (oidMatConDispatch != 0) {
                    try {
                        matDispatch = PstMatConDispatch.fetchExc(oidMatConDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }
                
                frmMatConDispatch.requestEntityObject(matDispatch);
                Date date = ControlDate.getDateTime(FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_DATE], req);
                matDispatch.setDispatchDate(date);
                
                if (oidMatConDispatch == 0) {
                    try {
                        SessMatConDispatch sessDispatch = new SessMatConDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatConDispatch.insertExc(this.matDispatch);
                        
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end
                        
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatConDispatch.updateExc(this.matDispatch);
                        
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
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
                if (oidMatConDispatch != 0) {
                    try {
                        matDispatch = PstMatConDispatch.fetchExc(oidMatConDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK:
                if (oidMatConDispatch != 0) {
                    try {
                        matDispatch = PstMatConDispatch.fetchExc(oidMatConDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE:
                if (oidMatConDispatch != 0) {
                    try {
                        String whereClause = PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatConDispatch;
                        Vector vect = PstMatConDispatchItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatConDispatchItem matDispatchItem = (MatConDispatchItem) vect.get(k);
                                CtrlMatConDispatchItem ctrlMatDpsItm = new CtrlMatConDispatchItem();
                                ctrlMatDpsItm.action(Command.DELETE, matDispatchItem.getOID(), oidMatConDispatch);
                            }
                        }
                        
                        /** gadnyan
                         * proses penghapusan di doc logger
                         * jika tidak di perlukan uncoment perintah ini
                         */
                        matDispatch = PstMatConDispatch.fetchExc(oidMatConDispatch);
                        PstDocLogger.deleteDataBo_inDocLogger(matDispatch.getDispatchCode(), I_DocType.MAT_DOC_TYPE_DF);
                        // -- end
                        
                        long oid = PstMatConDispatch.deleteExc(oidMatConDispatch);
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
                        System.out.println("exception dbexc : " + dbexc.toString());
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        System.out.println("exception exc : " + exc.toString());
                    }
                }
                break;
                
            default :
                break;
        }
        return rsCode;
    }
}
