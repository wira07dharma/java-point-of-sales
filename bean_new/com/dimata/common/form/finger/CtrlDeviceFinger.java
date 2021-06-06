
package com.dimata.common.form.finger;

/**
 *
 * @author Witar
 */
import javax.servlet.http.*; 
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.finger.DeviceFinger;
import com.dimata.common.entity.finger.PstDeviceFinger;

public class CtrlDeviceFinger 
extends Control 
implements I_Language {
 
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText ={
        {"Berhasil","Tidak Bisa Di Prosess","kode Sudah Ada","Data Belum Lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private  DeviceFinger deviceFinger;
    private PstDeviceFinger pstDeviceFinger;
    private FrmDeviceFinger frmDeviceFinger;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlDeviceFinger (HttpServletRequest request){
        msgString = "";
        deviceFinger = new DeviceFinger();
        try{
             pstDeviceFinger = new PstDeviceFinger(0);
        }catch(Exception e){
            System.out.println("Exception"+e);

        }
        frmDeviceFinger = new FrmDeviceFinger(request, deviceFinger);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
            this.frmDeviceFinger.addError(FrmDeviceFinger.FRM_FLD_DEVICE_ID,                                        
                 resultText[language][RSLT_EST_CODE_EXIST] );
                    return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                    return resultText[language][RSLT_UNKNOWN_ERROR];
        }

    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
                default:
                    return RSLT_UNKNOWN_ERROR;
            }
    }
    
    public int getLanguage(){ return language; }
    public void setLanguage(int language){ this.language = language; }
    public DeviceFinger getEmployee() { return deviceFinger; }
    public FrmDeviceFinger getForm() { return frmDeviceFinger; }
    public String getMessage(){ return msgString; }
    public int getStart() { return start; }
    
    public int action(int cmd , long oidDeviceFinger){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd){
            case Command.ADD :
                break;
            case Command.SAVE :
                if(oidDeviceFinger != 0){
                    try{
                        deviceFinger = PstDeviceFinger.fetchExc(oidDeviceFinger);
                    }catch(Exception exc){}
                }
                frmDeviceFinger.requestEntityObject(deviceFinger);
                if(frmDeviceFinger.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                if(deviceFinger.getOID()==0){
                    try{
                            long oid = pstDeviceFinger.insertExc(this.deviceFinger);
                    }catch (Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }else{
                    try {
                            long oid = pstDeviceFinger.updateExc(this.deviceFinger);
                    }catch (Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
		break;
            case Command.EDIT :

                if (oidDeviceFinger != 0) {

                    try {
                            deviceFinger = pstDeviceFinger.fetchExc(oidDeviceFinger);
                    } catch (Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

		}

		break;

            case Command.ASK :

                if (oidDeviceFinger != 0) {

                    try {

                        deviceFinger = pstDeviceFinger.fetchExc(oidDeviceFinger);

                    } catch (Exception exc){

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                    }

                }

		break;
            case Command.DELETE :
                if (oidDeviceFinger!= 0){
                    try{
                        long oid = pstDeviceFinger.deleteExc(oidDeviceFinger);
                        if(oid!=0){
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }else{
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }catch(Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            default :
		}
		return rsCode;
	}
}
