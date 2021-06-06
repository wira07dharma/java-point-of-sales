/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.region;

import com.dimata.aiso.entity.masterdata.region.*;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class CtrlWard extends Control implements I_Language {
    public static final int RSLT_OK = 0;
    public static final int RSLT_SAME = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_EXIST = 3;
    public static final int RSLT_UNKNOWN = 4;
    
    public static String resultText[][] = {
        {"OK ...", "Data sama ...", "Form belum lengkap ...", "Account link sudah ada ...", "Kesalahan tidak diketahui ..."},
        {"OK ...", "Same Data ...", "Form incomplete ...", "Link account already exist ...", "Unknown Error ..."}
    };
    
    private int start;
    private String msgString;
    private Ward ward;
    private PstWard pstWard;
    private FrmWard frmWard;
    private int language = LANGUAGE_DEFAULT;
    
    public CtrlWard(HttpServletRequest request) {
        msgString = "";
        ward = new Ward();
        try {
            pstWard = new PstWard(0);
        } catch (Exception e) {
        }
        frmWard = new FrmWard(request, ward);
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public Ward getWard() {
        return ward;
    }

    public FrmWard getForm() {
        return frmWard;
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
                frmWard.requestEntityObject(ward);
                ward.setOID(Oid);

                if (frmWard.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (ward.getOID() == 0) {
                    try {
                        long oid = pstWard.insert(this.ward);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstWard.update(this.ward);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        ward = (Ward) pstWard.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        ward = (Ward) pstWard.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstWard PstWard = new PstWard();
                    try {
                        long oid = PstWard.deleteExc(Oid);
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
