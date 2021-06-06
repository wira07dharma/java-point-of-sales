/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;


import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.masterdata.*;

//integrasi BO dengan POS
/**
 *
 * @author user
 */
public class CtrlConnection extends Control implements I_Language {
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
    private OutletConnection conn;
    private PstConnection pstconn;
    private FrmConnection frmconn;
    int language = LANGUAGE_DEFAULT;

    public CtrlConnection(HttpServletRequest request) {
        msgString = "";
        conn = new OutletConnection();
        try {
            pstconn = new PstConnection(0);
        } catch (Exception e) {
            ;
        }
        frmconn = new FrmConnection(request, conn);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                //this.frmLocation.addError(frmLocation.FRM_FIELD_LOCATION_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

    public OutletConnection getConnection() {
        return conn;
    }

    public FrmConnection getForm() {
        return frmconn;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidconn) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String strdbdriver = "";
                String strdburl = "";
                if (oidconn != 0) {
                    try {
                        conn = PstConnection.fetchExc(oidconn);
                        strdbdriver = conn.getDbdriver();
                        strdburl = conn.getDburl();
                    } catch (Exception exc) {
                        System.out.println("Exception exc : " + exc.toString());
                    }
                }

                frmconn.requestEntityObject(conn);
                //System.out.println("location.getParentId() : " + location.getParentLocationId());

                if (frmconn.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

               // System.out.println("code : '" + strCode + "' = '" + location.getCode() + "'");
               // System.out.println("name : '" + strName + "' = '" + location.getName() + "'");

                if ((!conn.getDbdriver().equals(strdbdriver)) || (!conn.getDburl().equals(strdburl))) {
                    String whereClause = "(" + PstConnection.fieldNames[PstConnection.FLD_DBDRIVER] + " = '" + conn.getDbdriver() + "' AND " +
                            PstConnection.fieldNames[PstConnection.FLD_DBURL] + " = '" + conn.getDburl() + "') AND " +
                            PstConnection.fieldNames[PstConnection.FLD_ID_DBCONNECTION] + " <> " + conn.getOID();
                    Vector isExist = PstConnection.list(0, 1, whereClause, "");
                    if (isExist != null && isExist.size() > 0) {
                        msgString = resultText[language][RSLT_EST_CODE_EXIST];
                        System.out.println("=----------- location is already exist");
                        return RSLT_EST_CODE_EXIST;
                    }
                }

                if (conn.getOID() == 0) {
                    try {
                        long oid = pstconn.insertExc(this.conn);
                        /*if (oid != 0) {
                            //integrasi BO dengan POS
                            OutletLink ol = new OutletLink();
                            ol.setCode(location.getCode());
                            ol.setDescription(location.getDescription());
                            ol.setName(location.getName());
                            ol.setOutletId(oid);

                            I_Outlet i_outlet = (I_Outlet) Class.forName(I_Outlet.strClassNameHanoman).newInstance();
                            i_outlet.insertOutlet(ol);
                            //---- end integrasi BO vs POS
                        }*/
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstconn.updateExc(this.conn);
                        /*if (oid != 0) {
                            //integrasi BO dengan POS
                            OutletLink ol = new OutletLink();
                            ol.setCode(location.getCode());
                            ol.setDescription(location.getDescription());
                            ol.setName(location.getName());
                            ol.setOutletId(oid);

                            I_Outlet i_outlet = (I_Outlet) Class.forName(I_Outlet.strClassNameHanoman).newInstance();
                            i_outlet.updateOutlet(ol);
                            //---- end integrasi BO vs POS
                        }*/
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidconn != 0) {
                    try {
                        conn = PstConnection.fetchExc(oidconn);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidconn != 0) {
                    try {
                        conn = PstConnection.fetchExc(oidconn);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidconn != 0) {

                    try {
                        //long oid = PstLocation.deleteExc(oidLocation);
                        //PstMatMinQty.deleteByLocationId(oidLocation);
                        long oid = PstConnection.deleteExc(oidconn);
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
        return rsCode;
    }
}
