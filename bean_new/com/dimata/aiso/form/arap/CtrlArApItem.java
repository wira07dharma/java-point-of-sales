package com.dimata.aiso.form.arap;

import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.aiso.entity.arap.PstArApItem;
import com.dimata.aiso.entity.arap.ArApItem;
import com.dimata.qdep.form.Control;

public class CtrlArApItem extends Control implements I_Language {

    public static final int RSLT_OK = 0;
    public static final int RSLT_PAYMENT_NULL = 1;
    public static final int RSLT_DESCRIPTION = 2;
    public static final int RSLT_INCOMPLETE = 3; 
    public static final int RSLT_UNKNOWN = 4;
    public static String resultText[][] = {
        {"OK", "Angsuran belum diinput", "Keterangan belum diinput", "Data belum diinputkan","System menemukan kesalahan. Silahkan entry data kembali"},
        {"OK", "Credit term value is required", "Description is required", "Form incomplete","Unidentification error.Pleas try again"}
    };


    private int start;
    private String msgString;
    private ArApItem arap;
    private PstArApItem pstArApItem;
    private FrmArApItem frmArAp;
    private int language = LANGUAGE_DEFAULT;

    public CtrlArApItem(HttpServletRequest request) {
        msgString = "";
        arap = new ArApItem();
        try {
            pstArApItem = new PstArApItem(0);
        } catch (Exception e) {
        }
        frmArAp = new FrmArApItem(request, arap);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public ArApItem getArApItem() {
        return arap;
    }

    public FrmArApItem getForm() {
        return frmArAp;
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
                frmArAp.requestEntityObject(arap);
                arap.setOID(Oid);
                arap.setLeftToPay(arap.getAngsuran());
                
                if (frmArAp.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }
                
                if(arap.getAngsuran() == 0){
                    msgString = resultText[language][RSLT_PAYMENT_NULL];
                    return RSLT_PAYMENT_NULL;
                }
                
                if(arap.getDescription() == null && arap.getDescription().length() == 0){
                    msgString = resultText[language][RSLT_DESCRIPTION];
                    return RSLT_DESCRIPTION;
                }
                
                if (arap.getOID() == 0) {
                    try {
                        long oid = pstArApItem.insertExc(this.arap);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstArApItem.updateExc(this.arap);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        arap = (ArApItem) pstArApItem.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        arap = (ArApItem) pstArApItem.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstArApItem pstArApItem = new PstArApItem();
                    try {
                        long oid = pstArApItem.deleteExc(Oid);
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
