/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.BussinessCenter;
import com.dimata.aiso.entity.masterdata.PstBussinessCenter;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlBussinessCenter extends Control implements I_Language{

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
    private BussinessCenter objBussinessCenter;
    private PstBussinessCenter pstBussinessCenter;
    private FrmBussinessCenter frmBussinessCenter;
    private int language = LANGUAGE_DEFAULT;

    public CtrlBussinessCenter(HttpServletRequest request) {
        msgString = "";
        objBussinessCenter = new BussinessCenter();
        try {
            pstBussinessCenter = new PstBussinessCenter(0);
        } catch (Exception e) {
        }
        frmBussinessCenter = new FrmBussinessCenter(request, objBussinessCenter);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public BussinessCenter getBussCenter() {
        return objBussinessCenter;
    }

    public FrmBussinessCenter getForm() {
        return frmBussinessCenter;
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
                        objBussinessCenter = PstBussinessCenter.fetchExc(Oid);
                    }catch(Exception e){}
                }
                frmBussinessCenter.requestEntityObject(objBussinessCenter);
                //aktiva.setOID();

                if (frmBussinessCenter.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objBussinessCenter.getOID() == 0) {
                    try {
                        long oid = pstBussinessCenter.insertExc(this.objBussinessCenter);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstBussinessCenter.updateExc(this.objBussinessCenter);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objBussinessCenter = (BussinessCenter) pstBussinessCenter.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objBussinessCenter = (BussinessCenter) pstBussinessCenter.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstBussinessCenter pstBussinessCenter = new PstBussinessCenter();
                    try {
                        long oid = pstBussinessCenter.deleteExc(Oid);
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
