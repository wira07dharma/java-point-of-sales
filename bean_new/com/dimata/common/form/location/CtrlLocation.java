/*
 * Ctrl Name  		:  CtrlLocation.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  		: karya
 * @version  		: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.form.location;

import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.common.entity.location.*;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.posbo.db.DBException;

//integrasi BO dengan POS

public class CtrlLocation extends Control implements I_Language {
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
    private Location location;
    private PstLocation pstLocation;
    private FrmLocation frmLocation;
    int language = LANGUAGE_DEFAULT;

    public CtrlLocation(HttpServletRequest request) {
        msgString = "";
        location = new Location();
        try {
            pstLocation = new PstLocation(0);
        } catch (Exception e) {
            ;
        }
        frmLocation = new FrmLocation(request, location);
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

    public Location getLocation() {
        return location;
    }

    public FrmLocation getForm() {
        return frmLocation;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidLocation, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String strCode = "";
                String strName = "";
                if (oidLocation != 0) {
                    try {
                        location = PstLocation.fetchExc(oidLocation);
                        strCode = location.getCode();
                        strName = location.getName();
                    } catch (Exception exc) {
                        System.out.println("Exception exc : " + exc.toString());
                    }
                }
                
                Date openTime = ControlDate.getTime(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_OPENING_TIME], request);
                Date closeTime = ControlDate.getTime(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_CLOSING_TIME], request);

                frmLocation.requestEntityObject(location);
                System.out.println("location.getParentId() : " + location.getParentLocationId());

                if (frmLocation.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                System.out.println("code : '" + strCode + "' = '" + location.getCode() + "'");
                System.out.println("name : '" + strName + "' = '" + location.getName() + "'");

                if ((!location.getCode().equals(strCode)) || (!location.getName().equals(strName))) {
                    String whereClause = "(" + PstLocation.fieldNames[PstLocation.FLD_CODE] + " = '" + location.getCode() + "' AND " +
                            PstLocation.fieldNames[PstLocation.FLD_NAME] + " = '" + location.getName() + "') AND " +
                            PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " <> " + location.getOID();
                    Vector isExist = PstLocation.list(0, 1, whereClause, "");
                    if (isExist != null && isExist.size() > 0) {
                        msgString = resultText[language][RSLT_EST_CODE_EXIST];
                        System.out.println("=----------- location is already exist");
                        return RSLT_EST_CODE_EXIST;
                    }
                }
                
                location.setOpeningTime(openTime);
                location.setClosingTime(closeTime);

                if (location.getOID() == 0) {
                    try {
                        long oid = pstLocation.insertExc(this.location);
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
                        long oid = pstLocation.updateExc(this.location);
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
                if (oidLocation != 0) {
                    try {
                        location = PstLocation.fetchExc(oidLocation);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidLocation != 0) {
                    try {
                        location = PstLocation.fetchExc(oidLocation);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:

                if (oidLocation != 0) {

                    try {
                        //long oid = PstLocation.deleteExc(oidLocation);
                        //PstMatMinQty.deleteByLocationId(oidLocation);
                        long oid = PstLocation.deleteExc(oidLocation);
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
