/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.PstTicketDeposit;
import com.dimata.aiso.entity.masterdata.TicketDeposit;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlTicketDeposit extends Control implements I_Language{

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
    private TicketDeposit objTicketDeposit;
    private PstTicketDeposit pstTicketDeposit;
    private FrmTicketDeposit frmTicketDeposit;
    private int language = LANGUAGE_DEFAULT;

    public CtrlTicketDeposit(HttpServletRequest request) {
        msgString = "";
        objTicketDeposit = new TicketDeposit();
        try {
            pstTicketDeposit = new PstTicketDeposit(0);
        } catch (Exception e) {
        }
        frmTicketDeposit = new FrmTicketDeposit(request, objTicketDeposit);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public TicketDeposit getTicketDeposit() {
        return objTicketDeposit;
    }

    public FrmTicketDeposit getForm() {
        return frmTicketDeposit;
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
                        objTicketDeposit = PstTicketDeposit.fetchExc(Oid);
                    }catch(Exception e){}
                }
                
                frmTicketDeposit.requestEntityObject(objTicketDeposit);
                //aktiva.setOID();

                if (frmTicketDeposit.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objTicketDeposit.getOID() == 0) {
                    try {
                        long oid = pstTicketDeposit.insertExc(this.objTicketDeposit);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN]; 
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstTicketDeposit.updateExc(this.objTicketDeposit);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objTicketDeposit = (TicketDeposit) pstTicketDeposit.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objTicketDeposit = (TicketDeposit) pstTicketDeposit.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstTicketDeposit pstTicketDeposit = new PstTicketDeposit();
                    try {
                        long oid = pstTicketDeposit.deleteExc(Oid);
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
