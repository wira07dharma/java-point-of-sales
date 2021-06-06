/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.DailyRate;
import com.dimata.aiso.entity.masterdata.PstDailyRate;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlDailyRate extends Control implements I_Language{

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
    private DailyRate objDailyRate;
    private PstDailyRate pstDailyRate;
    private FrmDailyRate frmDailyRate;
    private int language = LANGUAGE_DEFAULT;

    public CtrlDailyRate(HttpServletRequest request) {
        msgString = "";
        objDailyRate = new DailyRate();
        try {
            pstDailyRate = new PstDailyRate(0);
        } catch (Exception e) {
        }
        frmDailyRate = new FrmDailyRate(request, objDailyRate);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public DailyRate getDailyRate() {
        return objDailyRate;
    }

    public FrmDailyRate getForm() {
        return frmDailyRate;
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
                        objDailyRate = PstDailyRate.fetchExc(Oid);
                    }catch(Exception e){}
                }
                frmDailyRate.requestEntityObject(objDailyRate);
                //aktiva.setOID();

                if (frmDailyRate.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objDailyRate.getOID() == 0) {
                    try {
                        pstDailyRate.updateStatus(objDailyRate.getCurrencyId());
                        long oid = pstDailyRate.insertExc(this.objDailyRate);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstDailyRate.updateExc(this.objDailyRate);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objDailyRate = (DailyRate) pstDailyRate.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objDailyRate = (DailyRate) pstDailyRate.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstDailyRate pstDailyRate = new PstDailyRate();
                    try {
                        long oid = pstDailyRate.deleteExc(Oid);
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
