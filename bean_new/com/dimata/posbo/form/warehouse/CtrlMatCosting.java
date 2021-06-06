package com.dimata.posbo.form.warehouse;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_IJGeneral;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.session.warehouse.SessMatCosting;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.Date;

public class CtrlMatCosting extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private MatCosting matCosting;
    private MatCosting prevMatCosting;
    private PstMatCosting pstMatCosting;
    private FrmMatCosting frmMatCosting;
    private HttpServletRequest req;
    int language = LANGUAGE_DEFAULT;
    long oid = 0;
    private Date dateLog = new Date();

    public CtrlMatCosting(HttpServletRequest request) {
        msgString = "";
        matCosting = new MatCosting();
        try {
            pstMatCosting = new PstMatCosting(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMatCosting = new FrmMatCosting(request, matCosting);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatCosting.addError(frmMatCosting.FRM_FIELD_COSTING_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatCosting getMatCosting() {
        return matCosting;
    }

    public FrmMatCosting getForm() {
        return frmMatCosting;
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

//    by dyas - 20131126
//    tambah methods action dengan 2 variabel
    public int action(int cmd, long oidMatCosting) {
        return action(cmd,oidMatCosting,"",0);
    }

//    by dyas - 20131126
//    tambah methods action dengan 4 variabel
    public int action(int cmd, long oidMatCosting, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatCosting != 0) {
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }

                // by dyas - 20131126
                // tambah try catch
                // untuk mem-fetch oidMatCosting dan menyimpannya pada variabel prevMatCosting
                try {
                    prevMatCosting = PstMatCosting.fetchExc(oidMatCosting);
                } catch (Exception exc) {
                }

                frmMatCosting.requestEntityObject(matCosting);
                Date date = ControlDate.getDateTime(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], req);
                matCosting.setCostingDate(date);

                if (oidMatCosting == 0) {
                    try {
                        SessMatCosting sessCosting = new SessMatCosting();
                        int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                        maxCounter = maxCounter + 1;
                        matCosting.setCostingCodeCounter(maxCounter);
                        matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                        matCosting.setLastUpdate(new Date());
                        this.oid = pstMatCosting.insertExc(this.matCosting);

                        // by dyas - 20131126
                        // tambah kondisi
                        // untuk mengecek jika id tidak sama dengan 0 dan id yang diinputkan belum terdaftar
                        // maka panggil methods insertHistory, COMMAND.SAVE
                        if(oid!=0)
                        {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        matCosting.setLastUpdate(new Date());
                        this.oid = pstMatCosting.updateExc(this.matCosting);

                        // by dyas - 20131126
                        // tambah kondisi
                        // untuk mengecek jika id tidak sama dengan 0 dan id yang diinputkan sudah terdaftar
                        // maka panggil methods insertHistory, COMMAND.UPDATE
                        if(oid!=0)
                        {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oid);
                        }
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidMatCosting != 0) {
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatCosting != 0) {
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatCosting != 0) {
                    try {
                        String whereClause = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + "=" + oidMatCosting;
                        Vector vect = PstMatCostingItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatCostingItem matCostingItem = (MatCostingItem) vect.get(k);
                                CtrlMatCostingItem ctrlMatDpsItm = new CtrlMatCostingItem();
                                ctrlMatDpsItm.action(Command.DELETE, matCostingItem.getOID(), oidMatCosting);
                            }
                        }
                        long oid = PstMatCosting.deleteExc(oidMatCosting);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;

                            // by dyas - 20131126
                            // tambah try catch
                            // untuk memanggil methods insertHistory, COMMAND.DELETE
                            try{
                                insertHistory(userID, nameUser, cmd, oid);
                            }catch(Exception e){
                            }
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
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
    
    public int actionHpp(int cmd, long oidMatCosting, String nameUser, long userID, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatCosting != 0) {
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }

                // by dyas - 20131126
                // tambah try catch
                // untuk mem-fetch oidMatCosting dan menyimpannya pada variabel prevMatCosting
                try {
                    prevMatCosting = PstMatCosting.fetchExc(oidMatCosting);
                } catch (Exception exc) {
                }

                frmMatCosting.requestEntityObject(matCosting);
                String date = FRMQueryString.requestString(request, "FRM_FIELD_COSTING_DATE_STRING");
                matCosting.setCostingDate(Formater.formatDate(date, "yyyy-MM-dd HH:mm:ss"));

                if (oidMatCosting == 0) {
                    try {
                        SessMatCosting sessCosting = new SessMatCosting();
                        int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                        maxCounter = maxCounter + 1;
                        matCosting.setCostingCodeCounter(maxCounter);
                        matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                        matCosting.setLastUpdate(new Date());
                        this.oid = pstMatCosting.insertExc(this.matCosting);

                        // by dyas - 20131126
                        // tambah kondisi
                        // untuk mengecek jika id tidak sama dengan 0 dan id yang diinputkan belum terdaftar
                        // maka panggil methods insertHistory, COMMAND.SAVE
                        if(oid!=0)
                        {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        matCosting.setLastUpdate(new Date());
                        this.oid = pstMatCosting.updateExc(this.matCosting);

                        // by dyas - 20131126
                        // tambah kondisi
                        // untuk mengecek jika id tidak sama dengan 0 dan id yang diinputkan sudah terdaftar
                        // maka panggil methods insertHistory, COMMAND.UPDATE
                        if(oid!=0)
                        {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oid);
                        }
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidMatCosting != 0) {
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatCosting != 0) {
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatCosting != 0) {
                    try {
                        String whereClause = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + "=" + oidMatCosting;
                        Vector vect = PstMatCostingItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatCostingItem matCostingItem = (MatCostingItem) vect.get(k);
                                CtrlMatCostingItem ctrlMatDpsItm = new CtrlMatCostingItem();
                                ctrlMatDpsItm.action(Command.DELETE, matCostingItem.getOID(), oidMatCosting);
                            }
                        }
                        long oid = PstMatCosting.deleteExc(oidMatCosting);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;

                            // by dyas - 20131126
                            // tambah try catch
                            // untuk memanggil methods insertHistory, COMMAND.DELETE
                            try{
                                insertHistory(userID, nameUser, cmd, oid);
                            }catch(Exception e){
                            }
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
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

    // by dyas - 20131126
    // tambah methods insertHistory untuk menyimpan data" yang telah dibawa ke variabel logSysHistory
    public  void insertHistory(long userID, String nameUser, int cmd, long oid)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(matCosting.getCostingCode());
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(this.matCosting.getLogDetail(prevMatCosting));

          if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE)
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
}
      }
      catch(Exception e)
      {

      }
    }
}
