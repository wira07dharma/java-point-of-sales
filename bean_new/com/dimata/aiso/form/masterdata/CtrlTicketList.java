/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.PstTicketList;
import com.dimata.aiso.entity.masterdata.TicketList;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlTicketList extends Control implements I_Language{

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
    private TicketList objTicketList;
    private PstTicketList pstTicketList;
    private FrmTicketList frmTicketList;
    private int language = LANGUAGE_DEFAULT;

    public CtrlTicketList(HttpServletRequest request) {
        msgString = "";
        objTicketList = new TicketList();
        try {
            pstTicketList = new PstTicketList(0);
        } catch (Exception e) {
        }
        frmTicketList = new FrmTicketList(request, objTicketList);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public TicketList getTicketList() {
        return objTicketList;
    }

    public FrmTicketList getForm() {
        return frmTicketList;
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
                        objTicketList = PstTicketList.fetchExc(Oid);
                    }catch(Exception e){}
                }
                
                frmTicketList.requestEntityObject(objTicketList);
                //aktiva.setOID();

                if (frmTicketList.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objTicketList.getOID() == 0) {
                    try {
                        long oid = pstTicketList.insertExc(this.objTicketList);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN]; 
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstTicketList.updateExc(this.objTicketList);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objTicketList = (TicketList) pstTicketList.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objTicketList = (TicketList) pstTicketList.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstTicketList pstTicketRate = new PstTicketList();
                    try {
                        long oid = pstTicketList.deleteExc(Oid);
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
