/*
 * CtrlAktivaLocation.java
 *
 * Created on March 3, 2008, 9:21 AM
 */

package com.dimata.aiso.form.masterdata;

import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.qdep.form.Control;

/**
 *
 * @author  DWI
 */
public class CtrlAktivaLocation extends Control implements I_Language{
    
    public static final int RSLT_OK = 0;
    public static final int RSLT_EXIST = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_UNKNOWN = 3;
    public static String resultText[][] = {
        {"OK ...", "Kode lokasi sudah ada", "Form tidak lengkap"},
        {"OK ...", "Data already exist", "Form incomplete"}
    };


    private int start;
    private String msgString;
    private AktivaLocation objAktivaLocation;
    private PstAktivaLocation pstAktivaLocation;
    private FrmAktivaLocation frmAktivaLocation;
    private int language = LANGUAGE_DEFAULT;

    public CtrlAktivaLocation(HttpServletRequest request) {
        msgString = "";
        objAktivaLocation = new AktivaLocation();
        try {
            pstAktivaLocation = new PstAktivaLocation(0);
        } catch (Exception e) {
        }
        frmAktivaLocation = new FrmAktivaLocation(request, objAktivaLocation);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public AktivaLocation getAktivaLocation() {
        return objAktivaLocation;
    }

    public FrmAktivaLocation getForm() {
        return frmAktivaLocation;
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
                        objAktivaLocation = PstAktivaLocation.fetchExc(Oid);
                    }catch(Exception e){}
                }
                
                frmAktivaLocation.requestEntityObject(objAktivaLocation);
                //aktiva.setOID();

                if (frmAktivaLocation.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objAktivaLocation.getOID() == 0) {
                    try {
                        long oid = pstAktivaLocation.insertExc(this.objAktivaLocation);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN]; 
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstAktivaLocation.updateExc(this.objAktivaLocation);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objAktivaLocation = (AktivaLocation) pstAktivaLocation.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objAktivaLocation = (AktivaLocation) pstAktivaLocation.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstAktivaLocation pstAktivaLocation = new PstAktivaLocation();
                    try {
                        long oid = pstAktivaLocation.deleteExc(Oid);
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
