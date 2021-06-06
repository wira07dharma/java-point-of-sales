/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class CtrlJenisKredit extends Control implements I_Language {
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
    private JenisKredit jenisKredit;
    private PstJenisKredit pstJenisKredit;
    private FrmJenisKredit frmJenisKredit;
    private int language = LANGUAGE_DEFAULT;
    
    public CtrlJenisKredit(HttpServletRequest request) {
        msgString = "";
        jenisKredit = new JenisKredit();
        try {
            pstJenisKredit = new PstJenisKredit(0);
        } catch (Exception e) {
        }
        frmJenisKredit = new FrmJenisKredit(request, jenisKredit);
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public JenisKredit getKredit() {
        return jenisKredit;
    }

    public FrmJenisKredit getForm() {
        return frmJenisKredit;
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
                frmJenisKredit.requestEntityObject(jenisKredit);
                jenisKredit.setOID(Oid);

                if (frmJenisKredit.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (jenisKredit.getOID() == 0) {
                    try {
                        long oid = pstJenisKredit.insert(this.jenisKredit);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstJenisKredit.update(this.jenisKredit);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        jenisKredit = (JenisKredit) pstJenisKredit.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        jenisKredit = (JenisKredit) pstJenisKredit.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstJenisKredit PstKredit = new PstJenisKredit();
                    try {
                        long oid = PstKredit.deleteExc(Oid);
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
