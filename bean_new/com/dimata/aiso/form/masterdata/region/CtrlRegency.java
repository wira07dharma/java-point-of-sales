/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.region;

import com.dimata.qdep.form.Control;
import com.dimata.util.lang.I_Language;

import com.dimata.aiso.entity.masterdata.region.*;
import com.dimata.util.Command;
import javax.servlet.http.*;
/**
 *
 * @author dw1p4
 */
public class CtrlRegency extends Control implements I_Language{
    public static final int RSLT_OK = 0;
    public static final int RSLT_SAME = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_EXIST = 3;
    public static final int RSLT_UNKNOWN = 4;
    
    public static String resultText[][] = {
        {"OK ...", "Data sama ...", "Form belum lengkap ...", "Account link sudah ada ...", "Kesalahan unknown ..."},
        {"OK ...", "Same Data...", "Form incomplete ...", "Link account already exist ...", "Unknown Error ..."}
    };
    
    private int start;
    private String msgString;
    private Regency regency;
    private PstRegency pstRegency;
    private FrmRegency frmRegency;
    private int language = LANGUAGE_DEFAULT;
    
    public CtrlRegency(HttpServletRequest request) {
        msgString = "";
        regency = new Regency();
        try {
            pstRegency = new PstRegency(0);
        } catch (Exception e) {
        }
        frmRegency = new FrmRegency(request, regency);
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public Regency getRegency() {
        return regency;
    }

    public FrmRegency getForm() {
        return frmRegency;
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
                frmRegency.requestEntityObject(regency);
                regency.setOID(Oid);

                if (frmRegency.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (regency.getOID() == 0) {
                    try {
                        long oid = pstRegency.insert(this.regency);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstRegency.update(this.regency);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        regency = (Regency) pstRegency.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        regency = (Regency) pstRegency.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstRegency PstRegency = new PstRegency();
                    try {
                        long oid = PstRegency.deleteExc(Oid);
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
