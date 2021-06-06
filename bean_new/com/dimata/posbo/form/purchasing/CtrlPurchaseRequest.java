/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.purchasing;

/* java package */
import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequest;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.session.purchasing.SessPurchaseRequest;

/**
 *
 * @author dimata005
 */
public class CtrlPurchaseRequest extends Control implements I_Language {

    public static final String className = I_DocType.DOCTYPE_CLASSNAME;
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private long oid;
    private String msgString;
    private PurchaseRequest pr;
    PurchaseRequest prevPo = null;
    private PstPurchaseRequest pstpr;
    private FrmPurchaseRequest frmpr;
    int language = LANGUAGE_DEFAULT;
    private Date purchDate = null;
    Date dateLog = new Date();
    boolean incrementAllPrType = true;
    int counter = 0;
    int st = 0;

    public CtrlPurchaseRequest(HttpServletRequest request) {
        msgString = "";
        pr = new PurchaseRequest();
        try {
            pstpr = new PstPurchaseRequest(0);
        } catch (Exception e) {
            ;
        }
        //po=po-2;
        frmpr = new FrmPurchaseRequest(request, pr);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmpr.addError(frmpr.FRM_FIELD_PURCHASE_REQUEST_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public PurchaseRequest getPurchaseRequest() {
        return pr;
    }

    public FrmPurchaseRequest getForm() {
        return frmpr;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public long getOID() {
        return this.oid;
    }

    public int action(int cmd, long oidPurchaseRequest, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int prMaterialType = -1;
        try {
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
            prMaterialType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_POR);
        } catch (Exception e) {
            System.out.println("Error action Order Material");
        }
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPurchaseRequest != 0) {
                    try {
                        pr = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
                        purchDate = pr.getPurchRequestDate();
                        counter = pr.getPeCodeCounter();
                        st = pr.getPrStatus();
                    } catch (Exception exc) {
                    }

                    //by dyas 20131126
                    try {
                        prevPo = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
                    } catch (Exception exc) {
                    }
                }

                frmpr.requestEntityObject(pr);

                if (oidPurchaseRequest == 0) {
                    if (pr.getPurchRequestDate() == null) {
                        pr.setPurchRequestDate(new Date());
                    }
                    pr.setPeCodeCounter(SessPurchaseRequest.getIntCode(pr, purchDate, oidPurchaseRequest, counter, incrementAllPrType));
                    pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr));
                } else {
                    if (pr.getPurchRequestDate() == null) {
                        pr.setPurchRequestDate(new Date());
                    }

                    if (prevPo.getLocationId() == pr.getLocationId()) {
                        if (prevPo.getPurchRequestDate() == pr.getPurchRequestDate()) {
                            pr.setPeCodeCounter(pr.getPeCodeCounter());
                            pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr)); // pr.getPoCode()
                        } else {
                            pr.setPeCodeCounter(SessPurchaseRequest.getIntCode(pr, purchDate, oidPurchaseRequest, counter, incrementAllPrType));
                            pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr));
                        }
                    } else {
                        pr.setPeCodeCounter(SessPurchaseRequest.getIntCode(pr, purchDate, oidPurchaseRequest, counter, incrementAllPrType));
                        pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr));
                    }
                }

                if (frmpr.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (pr.getOID() == 0) {
                    try {
                        long oid = pstpr.insertExc(this.pr);
                        //PROSES UNTUK MENYIMPAN HISTORY JIKA OID DARI ORDER PO =! 0
                        if (oid != 0) {
                            this.oid = oid;
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
                        //RUBAH NILAI CMD MENJADI UPDATE
                        int cmdHistory = Command.UPDATE;
                        long oid = pstpr.updateExc(this.pr);
                        if (oid != 0) {
                            //FUNGSI SQL UNTUK MELAKUKAN INSERT KE TABEL LOG_HISTORY_NEW KETIKA SUDAH 2 JAM
                            //int getDiffHour = PstLogSysHistory.getLastUpdateTime(oid);
                            /*if(getDiffHour > 2)
                            {
                               insertHistory(userID, nameUser, cmdHistory, oid, cmd, true);
                            }
                            else
                            {*/
                            insertHistory(userID, nameUser, cmdHistory, oid);
                            //}
                        }

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidPurchaseRequest != 0) {
                    try {
                        pr = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPurchaseRequest != 0) {
                    try {
                        pr = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPurchaseRequest != 0) {
                    try {
                        pr = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
                        purchDate = pr.getPurchRequestDate();
                        counter = pr.getPeCodeCounter();
                        st = pr.getPrStatus();
                    } catch (Exception exc) {
                    }
                    try {
                        prevPo = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
                    } catch (Exception exc) {
                    }
                }

                frmpr.requestEntityObject(pr);

                if (oidPurchaseRequest == 0) {
                    pr.setPeCodeCounter(SessPurchaseRequest.getIntCode(pr, purchDate, oidPurchaseRequest, counter, incrementAllPrType));
                    pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr));
                } else {
                    if (prevPo.getLocationId() == pr.getLocationId()) {
                        pr.setPeCodeCounter(pr.getPeCodeCounter());
                        pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr)); // pr.getPoCode()
                    } else {
                        pr.setPeCodeCounter(SessPurchaseRequest.getIntCode(pr, purchDate, oidPurchaseRequest, counter, incrementAllPrType));
                        pr.setPrCode(SessPurchaseRequest.getCodeOrderMaterial(pr));
                    }
                }
                if (oidPurchaseRequest != 0) {
                    try {
                        PstPurchaseRequestItem.deleteByPurchaseRequest(oidPurchaseRequest);
                        long oid = PstPurchaseRequest.deleteExc(oidPurchaseRequest);
                        //RUBAH NILAI CMD MENJADI DELETE
                        //cmd = 6;
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                            try {
                                insertHistory(userID, nameUser, cmd, oid);
                            } catch (Exception e) {

                            }

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

            default:

        }
        return rsCode;
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
            logSysHistory.setLogOpenUrl("purchasing/material/pom/prmaterial_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(pr.getPrCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.pr.getLogDetail(prevPo));

            //jika sudah lewat 2 jam di log
            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

}




