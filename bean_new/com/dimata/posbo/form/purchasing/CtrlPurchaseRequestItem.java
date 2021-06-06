/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.purchasing;

/* dimata package */
/* qdep package */
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.posbo.entity.purchasing.PstPurchaseRequest;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.entity.purchasing.PurchaseRequestItem;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import java.util.Date;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author dimata005
 */
public class CtrlPurchaseRequestItem extends Control implements I_Language {
    
    public static final int RSLT_OK = 0;
    public static final int RSLT_UNKNOWN_ERROR = 1;
    public static final int RSLT_MATERIAL_EXIST = 2;
    public static final int RSLT_FORM_INCOMPLETE = 3;
    public static final int RSLT_QTY_NULL = 4;
    public static final int RSLT_INPUT_AWAL = 5;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Material sudah ada", "Data tidak lengkap", "Jumlah material tidak boleh nol ...",""},
        {"Succes", "Can not process", "Material already exist ...", "Data incomplete", "Quantity may not zero ...",""}
    };

    private int start;
    private String msgString;
    private PurchaseRequestItem prItem;
    private PstPurchaseRequestItem pstPrItem;
    private FrmPurchaseRequestItem frmPrItem;
    private Date dateLog = new Date();
    PurchaseRequestItem prevPoItem = null;
    PurchaseRequest purchaseOrder = null;
    int language = LANGUAGE_DEFAULT;

    public CtrlPurchaseRequestItem() {
    }

    public CtrlPurchaseRequestItem(HttpServletRequest request) {
        msgString = "";
        prItem = new PurchaseRequestItem();
        try {
            pstPrItem = new PstPurchaseRequestItem(0);
        } catch (Exception e) {
            ;
        }
        frmPrItem = new FrmPurchaseRequestItem(request, prItem);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmPrItem.addError(frmPrItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmPrItem.addError(frmPrItem.FRM_FIELD_QUANTITY, resultText[language][RSLT_QTY_NULL]);
                return resultText[language][RSLT_QTY_NULL];
            default :
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                return RSLT_MATERIAL_EXIST;
            case RSLT_QTY_NULL:
                return RSLT_QTY_NULL;
            default :
                return RSLT_UNKNOWN_ERROR;
        }
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public PurchaseRequestItem getPurchaseRequestItem() {
        return prItem;
    }

    public FrmPurchaseRequestItem getForm() {
        return frmPrItem;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

     public int action(int cmd, long oidPoItem, long oidPurchaseOrder, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPoItem != 0) {
                    try {
                        prItem = PstPurchaseRequestItem.fetchExc(oidPoItem);
                    } catch (Exception exc) {
                    }

                    try {
                        prevPoItem = PstPurchaseRequestItem.fetchExc(oidPoItem);
                    } catch (Exception exc) {
                    }
                }

                frmPrItem.requestEntityObject(prItem);
                prItem.setPurchaseOrderId(oidPurchaseOrder);
                
                
                if (frmPrItem.errorSize() > 0) {
                    if(frmPrItem.errorSize()==3){
                        msgString = resultText[language][RSLT_INPUT_AWAL];
                        return RSLT_INPUT_AWAL;
                    }else{
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                        return RSLT_FORM_INCOMPLETE;
                    }
                   
                }
                
                // check if current material already exist in orderMaterial
                if (prItem.getQuantity() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }

                if (prItem.getOID() == 0) {
                    try {
                        long oid = pstPrItem.insertExc(this.prItem);

                    if(this!=null && oidPurchaseOrder!=0 )
                    {
                        purchaseOrder = PstPurchaseRequest.fetchExc(oidPurchaseOrder);
                    }
                        if(oid!=0)
                        {
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                            if(oidUserLast != 0 && oidUserLast != userID )
                            {
                                insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
                            }
                        }


                    } catch (DBException dbexc) {
                        System.out.println(dbexc);
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println(exc);
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstPrItem.updateExc(this.prItem);
                        if(oid!=0)
                        {
                            //SET NILAI CMD AGAR MENJADI UPDATE
                            int cmdHistory = Command.UPDATE;
                            try {
                                if(oidPurchaseOrder!=0 ) {
                                    purchaseOrder = PstPurchaseRequest.fetchExc(oidPurchaseOrder);
                                }
                            } catch (Exception exc) {
                            }
                            insertHistory(userID, nameUser, cmdHistory, oidPurchaseOrder);
                        }
                    } catch (DBException dbexc) {
                        System.out.println(dbexc);
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        System.out.println(exc);
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidPoItem != 0) {
                    try {
                        prItem = PstPurchaseRequestItem.fetchExc(oidPoItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPoItem != 0) {
                    try {
                        prItem = PstPurchaseRequestItem.fetchExc(oidPoItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
               //=================================================================
                try {
                        purchaseOrder = PstPurchaseRequest.fetchExc(oidPurchaseOrder);
                        prItem = PstPurchaseRequestItem.fetchExc(oidPoItem);
                        prevPoItem = null;
                    } catch (Exception exc) {
                    }
                //================================================================
                if (oidPoItem != 0) {
                    try {
                        //long oid = PstPurchaseRequestItem.deleteExc(oidPoItem);
                        long oid = pstPrItem.deleteExc(oidPoItem);
                         //long oid = pstPrItem.updateExc(this.prItem);
                        if (oid != 0) {
                            //SET NILAI CMD AGAR MENJADI UPDATE
                            //cmd = 6;
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                            //cek siapa yang update? di log jika yang update  berbeda dengan user yang mengcreate pertama
//                            if(oidUserLast != 0 && oidUserLast != userID )
//                            {
//                                insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
//                            }
                            insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
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

    public  void insertHistory(long userID, String nameUser, int getCmd, long oidPurchaseOrder)
    {
       try
       {
            LogSysHistory logSysHistory = new LogSysHistory();
            //logSysHistory.setLogUserId(prItem.getMaterialId());
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("purchasing/material/pom/pomaterialitem.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogUserAction(Command.commandString[getCmd]);
            logSysHistory.setLogDocumentNumber(purchaseOrder.getPrCode());
            logSysHistory.setLogDocumentId(oidPurchaseOrder);

            logSysHistory.setLogDetail(this.prItem.getLogDetail(prevPoItem));

            if(!logSysHistory.getLogDetail().equals("") || getCmd==Command.DELETE)
            {
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
            }
       catch(Exception e)
       {
       }
    }

}
