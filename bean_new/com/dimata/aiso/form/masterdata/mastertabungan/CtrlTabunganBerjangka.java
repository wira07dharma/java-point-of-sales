/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.PstTabunganBerjangka;
import com.dimata.aiso.entity.masterdata.mastertabungan.TabunganBerjangka;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class CtrlTabunganBerjangka extends Control implements I_Language {
    public static final int RSLT_OK = 0;
    public static final int RSLT_SAME = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_EXIST = 3;
    public static final int RSLT_UNKNOWN = 4;
    
    public static String resultText[][] = {
        {"OK ...", "Data sama ...", "Form belum lengkap ...", "Account link sudah ada ...", "Kesalahan unknown ..."},
        {"OK ...", "Same Data ...", "Form incomplete ...", "Link account already exist ...", "Unknown Error ..."}
    };
    
    private int start;
    private String msgString;
    private TabunganBerjangka tabunganBerjangka;
    private PstTabunganBerjangka pstTabunganBerjangka;
    private FrmTabunganBerjangka frmTabunganBerjangka;
    private int language = LANGUAGE_DEFAULT;
    
    public CtrlTabunganBerjangka(HttpServletRequest request) {
        msgString = "";
        tabunganBerjangka = new TabunganBerjangka();
        try {
            pstTabunganBerjangka = new PstTabunganBerjangka(0);
        } catch (Exception e) {
        }
        frmTabunganBerjangka = new FrmTabunganBerjangka(request, tabunganBerjangka);
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public TabunganBerjangka getTabunganBerjangka() {
        return tabunganBerjangka;
    }

    public FrmTabunganBerjangka getForm() {
        return frmTabunganBerjangka;
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
                frmTabunganBerjangka.requestEntityObject(tabunganBerjangka);
                tabunganBerjangka.setOID(Oid);

                if (frmTabunganBerjangka.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (tabunganBerjangka.getOID() == 0) {
                    try {
                        long oid = pstTabunganBerjangka.insert(this.tabunganBerjangka);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstTabunganBerjangka.update(this.tabunganBerjangka);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        tabunganBerjangka = (TabunganBerjangka) pstTabunganBerjangka.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        tabunganBerjangka = (TabunganBerjangka) pstTabunganBerjangka.fetch(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstTabunganBerjangka PstTabunganBerjangka = new PstTabunganBerjangka();
                    try {
                        long oid = PstTabunganBerjangka.deleteExc(Oid);
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
