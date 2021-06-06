/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author dimata005
 */
/* java package */

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessMerk;

public class CtrlMaterialUnitOrder extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material MaterialUnitOrder sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material MaterialUnitOrder Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private MaterialUnitOrder matMaterialUnitOrder;
    MaterialUnitOrder prevMaterialUnitOrder;
    private PstMaterialUnitOrder pstMatMaterialUnitOrder;
    private FrmMaterialUnitOrder frmMaterialUnitOrder;
    Date dateLog = new  Date();
    private HttpServletRequest req;
    int language = LANGUAGE_FOREIGN;

    public CtrlMaterialUnitOrder(HttpServletRequest request) {
        msgString = "";
        matMaterialUnitOrder = new MaterialUnitOrder();
        try {
            pstMatMaterialUnitOrder = new PstMaterialUnitOrder(0);
        } catch (Exception e) {
            ;
        }
        frmMaterialUnitOrder = new FrmMaterialUnitOrder(request, matMaterialUnitOrder);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMaterialUnitOrder.addError(frmMaterialUnitOrder.FRM_FIELD_MATERIAL_UNIT_BUY_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MaterialUnitOrder getMerk() {
        return matMaterialUnitOrder;
    }

    public FrmMaterialUnitOrder getForm() {
        return frmMaterialUnitOrder;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMatUnitOrder) {
             return action(cmd, oidMatUnitOrder,0,"");
        }

    public int action(int cmd, long oidMatUnitOrder,long userID, String nameUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatUnitOrder != 0) {
                    try {
                        matMaterialUnitOrder = PstMaterialUnitOrder.fetchExc(oidMatUnitOrder);
                    } catch (Exception exc) {
                    }
                    
                     // update by fitra
                    try {
                       prevMaterialUnitOrder = PstMaterialUnitOrder.fetchExc(oidMatUnitOrder);
                    } catch (Exception exc) {
                }

                    
                    
                }

                frmMaterialUnitOrder.requestEntityObject(matMaterialUnitOrder);

                if (frmMaterialUnitOrder.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

//                String whereClause = "( " + PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_NAME] +
//                        " = '" + matMaterialUnitOrder.getName() +
//                        "') AND " + PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MERK_ID] +
//                        " <> " + matMaterialUnitOrder.getOID();
                Vector isExist = new Vector();//PstMaterialUnitOrder.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }

                if (matMaterialUnitOrder.getOID() == 0) {
                    try {
                        long oid = pstMatMaterialUnitOrder.insertExc(this.matMaterialUnitOrder);
                        
                         //add by fitra
                                                        if(oid !=0)
                                                        {
                                                        
                                                        insertHistoryMatUnitOrder(userID, nameUser, cmd, oid,oidMatUnitOrder );  
                                                         
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
                        
                        
                        // add by fitra 1-05-2014
                         int cmdHistory = Command.UPDATE;
                        long oid = pstMatMaterialUnitOrder.updateExc(this.matMaterialUnitOrder);

                        
                          if (oid != 0)
                        {
                            insertHistoryMatUnitOrder(userID, nameUser, cmdHistory, oid, oidMatUnitOrder);
                        }
                        

                        
                        
                        //======== END =======

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMatUnitOrder != 0) {
                    try {
                        matMaterialUnitOrder = PstMaterialUnitOrder.fetchExc(oidMatUnitOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatUnitOrder != 0) {
                    try {
                        matMaterialUnitOrder = PstMaterialUnitOrder.fetchExc(oidMatUnitOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatUnitOrder != 0) {
                    try {
                        long oid = 0;
                        oid = PstMaterialUnitOrder.deleteExc(oidMatUnitOrder);
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
    
    
    // add by fitra 20-04-2014
    public  void insertHistoryMatUnitOrder(long userID, String nameUser, int cmd, long oid, long oidMat)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType("Pos Material Price");
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(matMaterialUnitOrder.getPstClassName());
           logSysHistory.setLogDocumentId(oidMat);
           logSysHistory.setLogDetail(matMaterialUnitOrder.getLogDetail(prevMaterialUnitOrder));                              

           if( (!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE))
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
}
      }
      catch(Exception e)
      {
        System.out.println("error "+e);
      }
   } 
    
    
}