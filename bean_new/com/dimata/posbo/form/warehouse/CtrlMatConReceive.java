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
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

/* project package */
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.form.warehouse.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.payment.DailyRate;
import com.dimata.common.entity.payment.PstDailyRate;

public class CtrlMatConReceive extends Control implements I_Language {
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
    private MatConReceive matReceive;
    private PstMatConReceive pstMatConReceive;
    private FrmMatConReceive frmMatConReceive;
    private HttpServletRequest req;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMatConReceive(HttpServletRequest request) {
        msgString = "";
        matReceive = new MatConReceive();
        try {
            pstMatConReceive = new PstMatConReceive(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMatConReceive = new FrmMatConReceive(request, matReceive);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatConReceive.addError(frmMatConReceive.FRM_FIELD_RECEIVE_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
    
    public MatConReceive getMatConReceive() {
        return matReceive;
    }
    
    public FrmMatConReceive getForm() {
        return frmMatConReceive;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidMatConReceive) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                boolean incrementAllReceiveType = true;
                Date rDate = new Date();
                int recType = 0;
                int counter = 0;
                double transRate = 0;
                if (oidMatConReceive != 0) {
                    try {
                        matReceive = PstMatConReceive.fetchExc(oidMatConReceive);
                        rDate = matReceive.getReceiveDate();
                        recType = matReceive.getReceiveStatus();
                        counter = matReceive.getRecCodeCnt();
                        transRate = matReceive.getTransRate();
                    } catch (Exception exc) {
                    }
                }
                
                frmMatConReceive.requestEntityObject(matReceive);
                Date date = ControlDate.getDateTime(FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_DATE],req);
                matReceive.setReceiveDate(date);
                matReceive.setTransRate(transRate);
                
                int docType = -1;
                try {
                    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                    docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                } catch (Exception e) {
                }
                
                
                if (frmMatConReceive.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if (matReceive.getOID() == 0) {
                    try {
                        System.out.println("masuk action receive, create new document!");
                        /** Untuk mendapatkan besarnya daily rate per satuan default (:currency rate = 1)
                         * yang digunakan untuk nilai pada transaksi rate
                         * create by gwawan@dimata 16 Agutus 2007
                         */
                        if(this.matReceive.getCurrencyId() != 0) {
                            String whereClause = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+this.matReceive.getCurrencyId();
                            String orderClause = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
                            Vector listDailyRate = PstDailyRate.list(0, 1, whereClause, orderClause);
                            DailyRate objDailyRate = (DailyRate)listDailyRate.get(0);
                            this.matReceive.setTransRate(objDailyRate.getSellingRate());
                        }
                        else {
                            this.matReceive.setTransRate(0);
                        }
                        
                        this.matReceive.setRecCodeCnt(SessMatConReceive.getIntCode(matReceive, rDate, oidMatConReceive, docType, counter, incrementAllReceiveType));
                        this.matReceive.setRecCode(SessMatConReceive.getCodeReceive(matReceive));
                        matReceive.setLastUpdate(new Date());
                        long oid = pstMatConReceive.insertExc(this.matReceive);
                        
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        //PstDocLogger.insertDataBo_toDocLogger(matReceive.getRecCode(), I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR, matReceive.getLastUpdate(), matReceive.getRemark());
                        //--- end
                        System.out.println("action comlpete....");
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println("exception: "+exc.toString());
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                } else {
                    try {
                        matReceive.setLastUpdate(new Date());
                        long oid = pstMatConReceive.updateExc(this.matReceive);
                        
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matReceive.getRecCode(), I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR, matReceive.getLastUpdate(), matReceive.getRemark());
                        //--- end
                        
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                
                /** set status pada forwarder info dengan value terkini! */
                try {
                    String whereClause = ""+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID]+"="+this.matReceive.getOID();
                    Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                    ForwarderInfo forwarderInfo = new ForwarderInfo();
                    for(int j=0; j<vctListFi.size(); j++) {
                        forwarderInfo = (ForwarderInfo)vctListFi.get(j);
                        forwarderInfo.setStatus(this.matReceive.getReceiveStatus());
                        long oid = PstForwarderInfo.updateExc(forwarderInfo);
                    }
                }
                catch(Exception e) {
                    System.out.println("Exc in update status, forwarder_info >>> "+e.toString());
                }
                
                break;
                
            case Command.EDIT:
                if (oidMatConReceive != 0) {
                    try {
                        matReceive = PstMatConReceive.fetchExc(oidMatConReceive);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK:
                if (oidMatConReceive != 0) {
                    try {
                        matReceive = PstMatConReceive.fetchExc(oidMatConReceive);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE:
                if (oidMatConReceive != 0) {
                    try {
                        // memproses item penerimaan barang
                        CtrlMatConReceiveItem objCtlItem = new CtrlMatConReceiveItem();
                        MatConReceiveItem objItem = new MatConReceiveItem();
                        String stWhereClose = PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidMatConReceive;
                        Vector vListItem = PstMatConReceiveItem.list(0, 0, stWhereClose, "");
                        if (vListItem != null && vListItem.size() > 0) {
                            for (int i = 0; i < vListItem.size(); i++) {
                                objItem = (MatConReceiveItem) vListItem.get(i);
                                objCtlItem.action(Command.DELETE, objItem.getOID(), oidMatConReceive);
                            }
                        }
                        
                        /** gadnyan
                         * proses penghapusan di doc logger
                         * jika tidak di perlukan uncoment perintah ini
                         */
                        matReceive = PstMatConReceive.fetchExc(oidMatConReceive);
                        PstDocLogger.deleteDataBo_inDocLogger(matReceive.getRecCode(),I_DocType.MAT_DOC_TYPE_LMRR);
                        // -- end
                        
                        
                        /** delete forwarder information berdasarkan dok. receive */
                        String whereClause = ""+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID]+"="+matReceive.getOID();
                        Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                        ForwarderInfo forwarderInfo = new ForwarderInfo();
                        CtrlForwarderInfo ctrlForwarderInfo = new CtrlForwarderInfo();
                        for(int j=0; j<vctListFi.size(); j++) {
                            forwarderInfo = (ForwarderInfo)vctListFi.get(j);
                            ctrlForwarderInfo.action(Command.DELETE, forwarderInfo.getOID());
                        }
                        
                        /** delete receive */
                        long oid = PstMatConReceive.deleteExc(oidMatConReceive);
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
