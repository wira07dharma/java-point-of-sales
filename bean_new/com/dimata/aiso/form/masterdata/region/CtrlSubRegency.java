/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.region;

import com.dimata.aiso.entity.masterdata.region.*;;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class CtrlSubRegency extends Control implements I_Language{
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
    private SubRegency subRegency;
    private PstSubRegency pstSubRegency;
    private FrmSubRegency frmSubRegency;
    private int language = LANGUAGE_DEFAULT;
    
    public CtrlSubRegency(HttpServletRequest request) {
        msgString = "";
        subRegency = new SubRegency();
        try {
            pstSubRegency = new PstSubRegency(0);
        } catch (Exception e) {
        }
        frmSubRegency = new FrmSubRegency(request, subRegency);
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public SubRegency getSubRegency() {
        return subRegency;
    }

    public FrmSubRegency getForm() {
        return frmSubRegency;
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
                frmSubRegency.requestEntityObject(subRegency);
                subRegency.setOID(Oid);

                if (frmSubRegency.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (subRegency.getOID() == 0) {
                    try {
                        long oid = pstSubRegency.insert(this.subRegency);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstSubRegency.update(this.subRegency);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        subRegency = (SubRegency) pstSubRegency.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        subRegency = (SubRegency) pstSubRegency.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstSubRegency PstSubRegency = new PstSubRegency();
                    try {
                        long oid = PstSubRegency.deleteExc(Oid);
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
