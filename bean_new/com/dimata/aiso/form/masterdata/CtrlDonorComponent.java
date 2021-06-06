/*
 * CtrlDonorComponent.java
 *
 * Created on January 9, 2007, 3:10 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package aiso */
import com.dimata.aiso.entity.masterdata.*;

/* import package qdep */
import com.dimata.qdep.form.Control;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package servlet http */
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author  dwi
 */
public class CtrlDonorComponent extends Control implements I_Language {
    public static final int RSLT_OK = 0;    
    public static final int RSLT_INCOMPLETE = 1;
    public static final int RSLT_EXIST = 2;
    public static final int RSLT_UNKNOWN = 3;
    public static String resultText[][] = {
        {"OK", "Form belum lengkap", "Donor component sudah ada", "Kesalahan unknown"},
        {"OK", "Form incomplete", "Donor component already exist", "Unknown Error"}
    };


    private int start;
    private String msgString;
    private DonorComponent objDonorComponent;
    private PstDonorComponent pstDonorComponent;
    private FrmDonorComponent frmDonorComponent;
    private int language = LANGUAGE_DEFAULT;
    
    /** Creates a new instance of CtrlDonorComponent */
    public CtrlDonorComponent(HttpServletRequest request) {
        msgString = "";
        objDonorComponent = new DonorComponent();
        try {
            pstDonorComponent = new PstDonorComponent(0);
        } catch (Exception e) {
        }
        frmDonorComponent = new FrmDonorComponent(request, objDonorComponent);
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public DonorComponent getDonorComponent() {
        return objDonorComponent;
    }

    public FrmDonorComponent getForm() {
        return frmDonorComponent;
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
                        objDonorComponent = PstDonorComponent.fetchExc(Oid);
                    }catch(Exception e){}
                }
                frmDonorComponent.requestEntityObject(objDonorComponent);
                //objDonorComponent.setOID();

                System.out.println("objDonorComponent.getCode() =====> "+ objDonorComponent.getCode());
                if (frmDonorComponent.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }
                
                boolean checkDonorComp = false;
                if(objDonorComponent.getCode() != null && objDonorComponent.getCode().length() > 0){
                    String strWhClause = PstDonorComponent.fieldNames[PstDonorComponent.FLD_CODE]+" = '"+objDonorComponent.getCode()+"'";
                    checkDonorComp = PstDonorComponent.cekDonorComp(strWhClause); 
                }
                
                
                if(checkDonorComp && objDonorComponent.getOID() == 0){
                   msgString = resultText[language][RSLT_EXIST];
                   return RSLT_EXIST;
                }
                
                if (objDonorComponent.getOID() == 0) {
                    try {
                        long oid = pstDonorComponent.insertExc(this.objDonorComponent);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstDonorComponent.updateExc(this.objDonorComponent);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objDonorComponent = (DonorComponent) pstDonorComponent.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objDonorComponent = (DonorComponent) pstDonorComponent.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstDonorComponent pstDonorComponent = new PstDonorComponent();
                    try {
                        long oid = pstDonorComponent.deleteExc(Oid);
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
