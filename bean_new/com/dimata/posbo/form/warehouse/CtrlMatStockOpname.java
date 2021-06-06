package com.dimata.posbo.form.warehouse;

/* java package */
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.posbo.db.*;
/* project package */

import com.dimata.posbo.entity.warehouse.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import java.util.*;
import javax.servlet.http.*;

public class CtrlMatStockOpname extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText
            = {
                {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}};
    private int start;
    private long newOid = 0;
    private String msgString;
    private MatStockOpname matStockOpname;
    MatStockOpname prevMatStockOpname = null;
    private PstMatStockOpname pstMatStockOpname;
    private FrmMatStockOpname frmMatStockOpname;
    Date dateLog = new Date();
    private HttpServletRequest req;
    int language = LANGUAGE_DEFAULT;

    public CtrlMatStockOpname(HttpServletRequest request) {
        msgString = "";
        matStockOpname = new MatStockOpname();
        try {
            pstMatStockOpname = new PstMatStockOpname(0);
        } catch (Exception e) {
        }
        req = request;
        frmMatStockOpname = new FrmMatStockOpname(request, matStockOpname);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatStockOpname.addError(frmMatStockOpname.FRM_FIELD_STOCK_OPNAME_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatStockOpname getMatStockOpname() {
        return matStockOpname;
    }

    public FrmMatStockOpname getForm() {
        return frmMatStockOpname;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public long getNewOid() {
        return newOid;
    }

    public void setNewOid(long newOid) {
        this.newOid = newOid;
    }

    public int action(int cmd, long oidMatStockOpname) {
        return action(cmd, oidMatStockOpname, "", 0);
    }

    public int action(int cmd, long oidMatStockOpname, String nameUser, long userID) {
        return action(cmd, oidMatStockOpname, nameUser, userID, 0);
    }

    public int action(int cmd, long oidMatStockOpname, String nameUser, long userID, int typeHarian) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int counter = 0;
        boolean stockOpname = true;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatStockOpname != 0) {
                    try {
                        matStockOpname = PstMatStockOpname.fetchExc(oidMatStockOpname);
                    } catch (Exception exc) {
                    }

//                    by dyas - 20131127
//                    tambah try catch untuk simpen nilai di variabel prevMatStockOpname
                    try {
                        prevMatStockOpname = PstMatStockOpname.fetchExc(oidMatStockOpname);
                    } catch (Exception exc) {
                    }
                }

                frmMatStockOpname.requestEntityObject(matStockOpname);
                Date date = ControlDate.getDateTime(frmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], req);
                if (date == null) {
                    date = FRMQueryString.requestDate(req, "" + frmMatStockOpname.fieldNames[frmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE] + "");
                }
                matStockOpname.setStockOpnameDate(date);

                if (frmMatStockOpname.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                //added by dewok 2018-12-22 cek tgl opname tidak boleh lewat dari hari / jam saat ini
                if (matStockOpname.getStockOpnameDate().after(new Date())) {
                    msgString = "Tidak dapat membuat dokumen opname lewat dari jam / tanggal hari ini.";
                    return 1;
                }

                if (matStockOpname.getOID() == 0) {
                    try {
                        if (typeHarian == 1) {
                            this.matStockOpname.setStockOpnameNumber(PstMatStockOpname.generateStockOpnameNumberHarian(this.matStockOpname));
                        } else {
                            this.matStockOpname.setStockOpnameNumber(PstMatStockOpname.generateStockOpnameNumber(this.matStockOpname));
                        }
                        long oid = pstMatStockOpname.insertExc(this.matStockOpname);
                        //added by wira untuk update code counter
                        MatStockOpname mop = PstMatStockOpname.fetchExc(oid);
                        int codeCounter = PstMatStockOpname.getIntCode(mop, mop.getStockOpnameDate(), oid, counter, stockOpname);
                        mop.setCodeCounter(codeCounter);
                        oid = pstMatStockOpname.updateExc(mop);
                        
                        //dyas 20131127
                        //tambah kondisi untuk manggil methods insertHistory
                        if (oid != 0) {
                            this.newOid = oid;
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
                        //kondisi untuk mengecek sejauh mana object baru berbeda dengan objek baru
                        String oldDate = "";
                        String newDate = "";

                        oldDate = Formater.formatDate(prevMatStockOpname.getStockOpnameDate(), "MM/DD/YYYY");
                        newDate = Formater.formatDate(this.matStockOpname.getStockOpnameDate(), "MM/DD/YYYY");
                        Long oldLocations = prevMatStockOpname.getLocationId();
                        Long newLocations = this.matStockOpname.getLocationId();

                        if (!newDate.equals(oldDate) && oldLocations != newLocations) {
                            // jika tanggal dan lokasi berbeda, maka generate ulang
                            this.matStockOpname.setStockOpnameNumber(PstMatStockOpname.generateStockOpnameNumber(this.matStockOpname));
                        } else if (prevMatStockOpname.getLocationId() != this.matStockOpname.getLocationId()) {
                            //jika hanya lokasi yang berbeda, maka ganti kode di depan saja
                            Location oldLocation = PstLocation.fetchExc(prevMatStockOpname.getLocationId());
                            Location newLocation = PstLocation.fetchExc(this.matStockOpname.getLocationId());

                            String code = prevMatStockOpname.getStockOpnameNumber();
                            String newCode = code.replaceAll(oldLocation.getCode(), newLocation.getCode());
                            this.matStockOpname.setStockOpnameNumber(newCode);
                        } else if (!newDate.equals(oldDate)) {
                            //jika hanya tanggal yang berbeda
                            String oldCodeDate = Formater.formatDate(prevMatStockOpname.getStockOpnameDate(), "YYMMDD");
                            String newCodeDate = Formater.formatDate(this.matStockOpname.getStockOpnameDate(), "YYMMDD");
                            String code = prevMatStockOpname.getStockOpnameNumber();
                            String newCode = code.replaceAll(oldCodeDate, newCodeDate);
                            this.matStockOpname.setStockOpnameNumber(newCode);
                        }

                        //added by dewok 2018-12-22
                        if (matStockOpname.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                            boolean notOK = cekApakahBolehDiFinal(matStockOpname.getOID());
                            if (notOK) {
                                msgString = "Final dokumen gagal. Terdapat item yg sudah di opname sampai final di jam setelah dokumen opname ini.";
                                return 1;
                            }
                        }

                        long oid = pstMatStockOpname.updateExc(this.matStockOpname);
                        int cmdHistory = Command.UPDATE;

                        //dyas 20131127
                        //tambah kondisi untuk manggil methods insertHistory
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                            this.newOid = oid;
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
                if (oidMatStockOpname != 0) {
                    try {
                        matStockOpname = PstMatStockOpname.fetchExc(oidMatStockOpname);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatStockOpname != 0) {
                    try {
                        matStockOpname = PstMatStockOpname.fetchExc(oidMatStockOpname);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatStockOpname != 0) {
                    try {
                        String whereClause = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + "=" + oidMatStockOpname;
                        Vector vect = PstMatStockOpnameItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatStockOpnameItem matDispatchItem = (MatStockOpnameItem) vect.get(k);
                                CtrlMatStockOpnameItem ctrlMatDpsItm = new CtrlMatStockOpnameItem();
                                ctrlMatDpsItm.action(Command.DELETE, matDispatchItem.getOID(), oidMatStockOpname);
                            }
                        }

                        //dyas 20131127
                        //tambah try catch untuk ngambil data berdasarkan id yang dibawa
                        try {
                            prevMatStockOpname = PstMatStockOpname.fetchExc(oidMatStockOpname);
                        } catch (Exception e) {
                        };

                        long oid = PstMatStockOpname.deleteExc(oidMatStockOpname);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;

                            if (oid != 0) {
                                insertHistory(userID, nameUser, cmd, oid);
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

    public void insertHistory(long userID, String nameUser, int cmd, long oid) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/stock/mat_opname_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(matStockOpname.getStockOpnameNumber());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.matStockOpname.getLogDetail(prevMatStockOpname));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    public boolean cekApakahBolehDiFinal(long oidOpname) {
        MatStockOpname opname = new MatStockOpname();
        try {
            opname = PstMatStockOpname.fetchExc(oidOpname);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        //cek apakah ada data opname final di jam setelah opname ini
        String opnameDate = Formater.formatDate(opname.getStockOpnameDate(), "yyyy-MM-dd HH:mm:ss");
        String opnameStatus = "" + I_DocStatus.DOCUMENT_STATUS_CLOSED + "," + I_DocStatus.DOCUMENT_STATUS_POSTED + "," + I_DocStatus.PAYMENT_STATUS_POSTED_CLOSED;
        String whereOp = PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " > '" + opnameDate + "'"
                + " AND " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " IN (" + opnameStatus + ")";
        Vector<MatStockOpname> listOpname = PstMatStockOpname.list(0, 0, whereOp, null);
        if (listOpname.isEmpty()) {
            //tidak ada opname final setelah jam ini
            return false;
        }

        //get semua item opname
        Vector<MatStockOpnameItem> listItemOpn = PstMatStockOpnameItem.list(0, 0, PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidOpname, null);
        String items = "";
        for (MatStockOpnameItem item : listItemOpn) {
            //shortcut cek item sudah diopname
            if (sudahDiOpname(item.getMaterialId(), opname.getStockOpnameDate())) {
                return true;
            }
            items += (items.length() == 0) ? "" + item.getMaterialId() : "," + item.getMaterialId();
        }

        //cek apakah ada item yg sudah di opname sampai final di data opname setelah tgl opname ini
        for (MatStockOpname opnameAfter : listOpname) {
            String whereItem = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + opnameAfter.getOID()
                    + " AND " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + " IN (" + items + ")";
            Vector listItemExist = PstMatStockOpnameItem.list(0, 0, whereItem, null);
            if (listItemExist.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean sudahDiOpname(long oidMaterial, Date opnameDate) {
        String dateChek = Formater.formatDate(opnameDate, "yyyy-MM-dd HH:mm:ss");
        String opnameStatus = "" + I_DocStatus.DOCUMENT_STATUS_CLOSED + "," + I_DocStatus.DOCUMENT_STATUS_POSTED + "," + I_DocStatus.PAYMENT_STATUS_POSTED_CLOSED;
        String opnameId = "SELECT " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]
                + " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME
                + " WHERE " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " > '" + dateChek + "'"
                + " AND " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " IN (" + opnameStatus + ")"
                + "";
        String whereOpname = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " IN (" + opnameId + ")"
                + " AND " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + " = '" + oidMaterial + "'"
                + "";
        Vector<MatStockOpnameItem> listOpnameItem = PstMatStockOpnameItem.list(0, 0, whereOpname, null);
        if (listOpnameItem.size() > 0) {
            return true;
        }
        return false;
    }

}
