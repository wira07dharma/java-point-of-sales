
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author Witar
 */
import com.dimata.posbo.entity.masterdata.NotaSetting;
import com.dimata.posbo.entity.masterdata.PstNotaSetting;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import static com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT;
import javax.servlet.http.HttpServletRequest;

public class CtrlNotaSetting {
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
    private NotaSetting notaSetting;
    private PstNotaSetting pstNotaSetting;
    private FrmNotaSetting frmNotaSetting;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlNotaSetting (HttpServletRequest request){
        msgString = "";
        notaSetting = new NotaSetting();
        try{
             pstNotaSetting = new PstNotaSetting(0);
        }catch(Exception e){
            System.out.println("Exception"+e);

        }
        frmNotaSetting = new FrmNotaSetting(request, notaSetting);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
            this.frmNotaSetting.addError(frmNotaSetting.FRM_FLD_POS_NOTA_SETTING_ID,                                        
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
    public NotaSetting getEmployee() { return notaSetting; }
    public FrmNotaSetting getForm() { return frmNotaSetting; }
    public String getMessage(){ return msgString; }
    public int getStart() { return start; }
    
    public int action(int cmd , long oidNotaSetting){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd){
            case Command.ADD :
                break;
            case Command.SAVE :
                if(oidNotaSetting != 0){
                    try{
                            notaSetting = PstNotaSetting.fetchExc(oidNotaSetting);
                    }catch(Exception exc){}
                }
                frmNotaSetting.requestEntityObject(notaSetting);
                if(frmNotaSetting.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                if(notaSetting.getOID()==0){
                    try{
                            long oid = pstNotaSetting.insertExc(this.notaSetting);
                    }catch (Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }else{
                    try {
                            long oid = PstNotaSetting.updateExc(this.notaSetting);
                    }catch (Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
		break;
            case Command.EDIT :

                if (oidNotaSetting != 0) {

                    try {
                            notaSetting = PstNotaSetting.fetchExc(oidNotaSetting);
                    } catch (Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

		}

		break;

            case Command.ASK :

                if (oidNotaSetting != 0) {

                    try {

                        notaSetting = PstNotaSetting.fetchExc(oidNotaSetting);

                    } catch (Exception exc){

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                    }

                }

		break;
            case Command.DELETE :
                if (oidNotaSetting!= 0){
                    try{
                        long oid = PstNotaSetting.deleteExc(oidNotaSetting);
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
