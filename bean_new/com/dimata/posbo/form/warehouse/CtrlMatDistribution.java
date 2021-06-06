package com.dimata.posbo.form.warehouse;

/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.purchasing.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.posbo.session.purchasing.*;

public class CtrlMatDistribution extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_TRANS_ERROR = 4;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap","Transfer data gagal"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete","Transfer is failed"}
    };
    
    private int start;
    private String msgString;
    private MatDistribution matDistribution;
    private PstMatDistribution pstMatDistribution;
    private FrmMatDistribution frmMatDistribution;
    int language = LANGUAGE_DEFAULT;
    long oid = 0;
    
    public CtrlMatDistribution(HttpServletRequest request) {
        msgString = "";
        matDistribution = new MatDistribution();
        try {
            pstMatDistribution = new PstMatDistribution(0);
        }
        catch(Exception e){;}
        frmMatDistribution = new FrmMatDistribution(request, matDistribution);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmMatDistribution.addError(frmMatDistribution.FRM_FIELD_DISTRIBUTION_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public MatDistribution getMatDistribution() { return matDistribution; }
    
    public FrmMatDistribution getForm() { return frmMatDistribution; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public long getOidTransfer(){ return oid;}
    
    public int action(int cmd , long oidMatDistribution) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidMatDistribution != 0) {
                    try {
                        matDistribution = PstMatDistribution.fetchExc(oidMatDistribution);
                    }
                    catch(Exception exc) {
                        System.out.println("Exception : "+exc.toString());
                    }
                }
                
                frmMatDistribution.requestEntityObject(matDistribution);
                
                if (oidMatDistribution == 0) {
                    try {
                        SessMatDistribution sessDispatch = new SessMatDistribution();
                        int maxCounter = sessDispatch.getMaxDistributionCounter(matDistribution.getDistributionDate(),matDistribution);
                        maxCounter = maxCounter + 1;
                        matDistribution.setDistributionCodeCounter(maxCounter);
                        matDistribution.setDistributionCode(sessDispatch.generateDistributionCode(matDistribution));
                        this.oid = pstMatDistribution.insertExc(matDistribution);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                else {
                    try {
                        this.oid = pstMatDistribution.updateExc(this.matDistribution);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.EDIT :
                if (oidMatDistribution != 0) {
                    try {
                        matDistribution = PstMatDistribution.fetchExc(oidMatDistribution);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidMatDistribution != 0) {
                    try {
                        matDistribution = PstMatDistribution.fetchExc(oidMatDistribution);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidMatDistribution != 0) {
                    try {
                        String whereClause = PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID]+"="+oidMatDistribution;
                        Vector vect = PstMatDistributionDetail.list(0,0,whereClause,"");
                        if(vect!=null && vect.size()>0){
                            for(int k=0;k<vect.size();k++){
                                MatDistributionDetail matDispatchItem = (MatDistributionDetail)vect.get(k);
                                CtrlMatDistributionDetail ctrlMatDpsItm = new CtrlMatDistributionDetail();
                                ctrlMatDpsItm.action(Command.DELETE,matDispatchItem.getOID(),oidMatDistribution);
                            }
                        }


                        long oid = PstMatDistribution.deleteExc(oidMatDistribution);
                        if(oid!=0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }
                        else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch(DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        System.out.println("exception dbexc : "+dbexc.toString());
                    }
                    catch(Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        System.out.println("exception exc : "+exc.toString());
                    }
                }
                break;
                
            case Command.SUBMIT : // proses create df witch automatic.
                if(oidMatDistribution != 0) {
                    try{
                        matDistribution = PstMatDistribution.fetchExc(oidMatDistribution);
                    }catch(Exception e){}
                    
                    int idx = PstMatDistribution.generateDispatch(oidMatDistribution);
                    if(idx!=0){ // error
                        rsCode = RSLT_TRANS_ERROR;
                    }else{ // ok
                        rsCode = RSLT_OK;
                    }
                }
                break;
                
            default :
                break;
        }
        return rsCode;
    }
}
