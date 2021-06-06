/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.form.location;

import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.common.entity.location.*;
import com.dimata.posbo.db.DBException;

//integrasi BO dengan POS

public class CtrlInternet extends Control implements I_Language {
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
    private internetconn internet;
    private PstInternet pstInter;
    private FrmInternet frmInter;
    int language = LANGUAGE_DEFAULT;

    public CtrlInternet(HttpServletRequest request) {
        msgString = "";
        internet = new internetconn();
        try {
            pstInter = new PstInternet(0);
        } catch (Exception e) {
            ;
        }
        frmInter = new FrmInternet(request, internet);
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

    public internetconn getInternet() {
        return internet;
    }

    public FrmInternet getForm() {
        return frmInter;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidInternet) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String strIp = "";
                String strUserName = "";
                if (oidInternet != 0) {
                    try {
                        internet = PstInternet.fetchExc(oidInternet);
                        strIp = internet.getIp();
                        strUserName = internet.getUser_name();
                    } catch (Exception exc) {
                        System.out.println("Exception exc : " + exc.toString());
                    }
                }

                        frmInter.requestEntityObject(internet);
                //System.out.println("location.getParentId() : " + internet.getParentLocationId());
                System.out.println("testing");
                System.out.println(strIp);


                if (frmInter.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                System.out.println(internet.getIp().toString());
                System.out.println(strIp);

                System.out.println("ip : '" + strIp + "' = '" + internet.getIp() + "'");
                System.out.println("user name : '" + strUserName + "' = '" + internet.getUser_name() + "'");
                //System.out.println("tes");
                System.out.print(internet.getIp().toString());
                System.out.print(strIp);

                System.out.println("ip : '" + strIp + "' = '" + internet.getIp() + "'");
                if ((!internet.getIp().equals(strIp)) || (!internet.getUser_name().equals(strUserName))) {
                    String whereClause = "(" + PstInternet.fieldNames[PstInternet.FLD_IP] + " = '" + internet.getIp() + "' AND " +
                            PstInternet.fieldNames[PstInternet.FLD_USER_NAME] + " = '" + internet.getUser_name() + "') AND " +
                            PstInternet.fieldNames[PstInternet.FLD_INTERCON_ID] + " <> " + internet.getOID();
                    Vector isExist = PstInternet.list(0, 1, whereClause, "");
                    if (isExist != null && isExist.size() > 0) {
                        msgString = resultText[language][RSLT_EST_CODE_EXIST];
                        System.out.println("=----------- location is already exist");
                        return RSLT_EST_CODE_EXIST;
                    }
                }

                if (internet.getOID() == 0) {
                    try {
                        long oid = pstInter.insertExc(this.internet);
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
                        long oid = pstInter.updateExc(this.internet);
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
                if (oidInternet != 0) {
                    try {
                        internet = PstInternet.fetchExc(oidInternet);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidInternet != 0) {
                    try {
                        internet = PstInternet.fetchExc(oidInternet);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:

                if (oidInternet != 0) {

                    try {
                        //long oid = PstInternet.deleteExc(oidLocation);
                        //PstMatMinQty.deleteByLocationId(oidLocation);
                        long oid = PstInternet.deleteExc(oidInternet);
                        if (oid != 0) {

                            //integrasi BO dengan POS
                            //OutletLink ol = new OutletLink();
                            //ol.setOutletId(oidLocation);

                            //I_Outlet i_outlet = (I_Outlet) Class.forName(I_Outlet.strClassNameHanoman).newInstance();
                            //oidLocation = i_outlet.deleteOutlet(ol);
                             //---- end integrasi BO vs POS

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
