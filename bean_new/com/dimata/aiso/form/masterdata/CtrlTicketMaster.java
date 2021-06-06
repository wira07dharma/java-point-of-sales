/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.PstTicketMaster;
import com.dimata.aiso.entity.masterdata.TicketMaster;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlTicketMaster extends Control implements I_Language{

    public static final int RSLT_OK = 0;
    public static final int RSLT_EXIST = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_UNKNOWN = 3;
    public static String resultText[][] = {
        {"OK ...", "Kode sudah ada", "Form tidak lengkap","Unknown Error"},
        {"OK ...", "Data already exist", "Form incomplete","Unknown Error"}
    };


    private int start;
    private String msgString;
    private TicketMaster objTicketMaster;
    private PstTicketMaster pstTicketMaster;
    private FrmTicketMaster frmTicketMaster;
    private int language = LANGUAGE_DEFAULT;

    public CtrlTicketMaster(HttpServletRequest request) {
        msgString = "";
        objTicketMaster = new TicketMaster();
        try {
            pstTicketMaster = new PstTicketMaster(0);
        } catch (Exception e) {
        }
        frmTicketMaster = new FrmTicketMaster(request, objTicketMaster);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public TicketMaster getTicketMaster() {
        return objTicketMaster;
    }

    public FrmTicketMaster getForm() {
        return frmTicketMaster;
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
                        objTicketMaster = PstTicketMaster.fetchExc(Oid);
                    }catch(Exception e){}
                }
                
                frmTicketMaster.requestEntityObject(objTicketMaster);
                //aktiva.setOID();

                if (frmTicketMaster.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objTicketMaster.getOID() == 0) {
                    try {
                        long oid = pstTicketMaster.insertExc(this.objTicketMaster);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN]; 
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstTicketMaster.updateExc(this.objTicketMaster);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
		System.out.println("Oid bean :::::::::: "+Oid);
                if (Oid != 0) {
                    try {
                        objTicketMaster = (TicketMaster) pstTicketMaster.fetchExc(Oid);
                    } catch (Exception exc) {
			System.out.println("Exception on Edit :::::::::: "+exc.toString());
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objTicketMaster = (TicketMaster) pstTicketMaster.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstTicketMaster pstTicketMaster = new PstTicketMaster();
                    try {
                        long oid = pstTicketMaster.deleteExc(Oid);
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
