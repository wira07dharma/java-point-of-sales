/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.PstTicketRate;
import com.dimata.aiso.entity.masterdata.TicketRate;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlTicketRate extends Control implements I_Language{
    
    public static final int RSLT_OK = 0;
    public static final int RSLT_EXIST = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_UNKNOWN = 3;
    public static String resultText[][] = {
        {"OK ...", "Kode sudah ada", "Form tidak lengkap","Unknown"},
        {"OK ...", "Data already exist", "Form incomplete","Unknown"}
    };


    private int start;
    private String msgString;
    private TicketRate objTicketRate;
    private PstTicketRate pstTicketRate;
    private FrmTicketRate frmTicketRate;
    private int language = LANGUAGE_DEFAULT;

    public CtrlTicketRate(HttpServletRequest request) {
        msgString = "";
        objTicketRate = new TicketRate();
        try {
            pstTicketRate = new PstTicketRate(0);
        } catch (Exception e) {
        }
        frmTicketRate = new FrmTicketRate(request, objTicketRate);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public TicketRate getTicketRate() {
        return objTicketRate;
    }

    public FrmTicketRate getForm() {
        return frmTicketRate;
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
                        objTicketRate = PstTicketRate.fetchExc(Oid);
                    }catch(Exception e){}
                }
                
                frmTicketRate.requestEntityObject(objTicketRate);
                //aktiva.setOID();

                if (frmTicketRate.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objTicketRate.getOID() == 0) {
                    try {
                        long oid = pstTicketRate.insertExc(this.objTicketRate);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN]; 
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstTicketRate.updateExc(this.objTicketRate);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objTicketRate = (TicketRate) pstTicketRate.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objTicketRate = (TicketRate) pstTicketRate.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstTicketRate pstTicketRate = new PstTicketRate();
                    try {
                        long oid = pstTicketRate.deleteExc(Oid);
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
