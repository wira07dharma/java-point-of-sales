/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.BussinessGroup;
import com.dimata.aiso.entity.masterdata.PstBussGroup;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlBussGroup extends Control implements I_Language{

    public static final int RSLT_OK = 0;
    public static final int RSLT_INCOMPLETE = 1;
    public static final int RSLT_EXIST = 2;
    public static final int RSLT_UNKNOWN = 3;
    public static String resultText[][] = {
        {"OK", "Form belum lengkap", "Data sudah ada", "Kesalahan unknown"},
        {"OK", "Form incomplete", "Data already exist", "Unknown Error"}
    };


    private int start;
    private String msgString;
    private BussinessGroup objBussinessGroup;
    private PstBussGroup pstBussGroup;
    private FrmBussGroup frmBussinessGroup;
    private int language = LANGUAGE_DEFAULT;

    public CtrlBussGroup(HttpServletRequest request) {
        msgString = "";
        objBussinessGroup = new BussinessGroup();
        try {
            pstBussGroup = new PstBussGroup(0);
        } catch (Exception e) {
        }
        frmBussinessGroup = new FrmBussGroup(request, objBussinessGroup);
    }

    public int getLanguage() {
        return language;
    }

    public BussinessGroup getBussGroup() {
        return objBussinessGroup;
    }
    public void setLanguage(int language) {
        this.language = language;
    }

    public BussinessGroup getBussinessGroup() {
        return objBussinessGroup;
    }

    public FrmBussGroup getForm() {
        return frmBussinessGroup;
    }

    public String getMessage() {
        return msgString;
    }

    public int action(int cmd, long Oid) {
        this.start = start;
        int result = RSLT_OK;
        msgString = "";
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if(Oid!=0){
                    try{
                        objBussinessGroup = PstBussGroup.fetchExc(Oid);
                    }catch(Exception e){}
                }
                frmBussinessGroup.requestEntityObject(objBussinessGroup);
                //aktiva.setOID();

                if (frmBussinessGroup.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objBussinessGroup.getOID() == 0) {
                    try {
                        long oid = PstBussGroup.insertExc(this.objBussinessGroup);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = PstBussGroup.updateExc(this.objBussinessGroup);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objBussinessGroup = (BussinessGroup) PstBussGroup.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objBussinessGroup = (BussinessGroup) PstBussGroup.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstBussGroup PstBussGroup = new PstBussGroup();
                    try {
                        long oid = PstBussGroup.deleteExc(Oid);
                        this.start = 0;
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            default:

        }
        return result;
    }
}
