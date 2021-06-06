/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.form.email;
import com.dimata.common.entity.email.PstSettingEmail;
import com.dimata.common.entity.email.SettingEmail;
import javax.servlet.http.*;
// dimata & qdep specific package
import com.dimata.util.*;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.*;
/**
 *
 * @author dimata005
 */
public class CtrlSettingEmail {
    private String msgString;
    private int start;
    private SettingEmail settingEmail;
    private PstSettingEmail pstSettingEmail;
    private FrmSettingEmail frmSettingEmail;

    /** Creates new CtrlAppUser */
    public CtrlSettingEmail(HttpServletRequest request) {
        msgString = "";
        // errCode = Message.OK;

        settingEmail = new SettingEmail();
        try{
            pstSettingEmail = new PstSettingEmail(0);
        }catch(Exception e){}
        frmSettingEmail = new FrmSettingEmail(request);
    }

     public String getErrMessage(int errCode){
        switch (errCode){
            case FRMMessage.ERR_DELETED :
                return "Can't Delete User";
            case FRMMessage.ERR_SAVED :
                if(frmSettingEmail.getFieldSize()>0)
                    return "Can't save user, cause some required data are incomplete ";
                else
                    return "Can't save user, Duplicate login ID, please type another login ID";
            default:
                return "Can't save user";
        }
    }

     public SettingEmail getSettingEmail() {
        return settingEmail;
    }

    public FrmSettingEmail getForm() {
        return frmSettingEmail;
    }


    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

     public int actionList(int listCmd, int start, int vectSize, int recordToGet) {
        msgString = "";

        switch(listCmd){
            case Command.FIRST :
                this.start = 0;
                break;

            case Command.PREV :
                this.start = start - recordToGet;
                if(start < 0){
                    this.start = 0;
                }
                break;

            case Command.NEXT :
                this.start = start + recordToGet;
                if(start >= vectSize){
                    this.start = start - recordToGet;
                }
                break;

            case Command.LAST :
                int mdl = vectSize % recordToGet;
                if(mdl>0){
                    this.start = vectSize - mdl;
                }
                else{
                    this.start = vectSize - recordToGet;
                }

                break;

            default:
                this.start = start;
                if(vectSize<1)
                    this.start = 0;

                if(start > vectSize){
                    // set to last
                     mdl = vectSize % recordToGet;
                    if(mdl>0){
                        this.start = vectSize - mdl;
                    }
                    else{
                        this.start = vectSize - recordToGet;
                    }
                }
                break;
        } //end switch
        return this.start;
    }

       public int action(int cmd, long appSettingEmailOID) {
        long rsCode = 0;
        int errCode = -1;
        //int excCode = -1;
        int excCode = 0;

        msgString = "";
        switch(cmd){
            case Command.ADD :
                settingEmail.setEmailName("");
                break;

            case Command.SAVE :
                frmSettingEmail.requestEntityObject(settingEmail);
                settingEmail.setOID(appSettingEmailOID);

                System.out.println("*** errSize : " + frmSettingEmail.errorSize());

                if(frmSettingEmail.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return FRMMessage.MSG_INCOMPLATE;
                }

                try{
                    if (settingEmail.getOID() == 0)
                        rsCode = pstSettingEmail.insert(this.settingEmail);
                    else
                        rsCode = pstSettingEmail.update(this.settingEmail);

                } catch (Exception exc){
                    msgString = getErrMessage(excCode);
                }

                break;

            case Command.EDIT :

                if (appSettingEmailOID !=0 ){
                    settingEmail = (SettingEmail)pstSettingEmail.fetch(appSettingEmailOID);
                }
                break;

            case Command.ASK :

                if (appSettingEmailOID != 0){
                    settingEmail = (SettingEmail)pstSettingEmail.fetch(appSettingEmailOID);
                    msgString = FRMMessage.getErr(FRMMessage.MSG_ASKDEL);
                }
                break;

            //<editor-fold defaultstate="collapsed" desc="comment">
            case Command.DELETE :
                //</editor-fold>
                if (appSettingEmailOID!= 0){

                    rsCode = pstSettingEmail.delete(appSettingEmailOID);
                    if(rsCode == FRMMessage.NONE){
                        System.out.println("Error deleteByUser - " +appSettingEmailOID);
                        msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                        errCode = FRMMessage.ERR_DELETED;
                    }
                    else{

                        PstSettingEmail pstSettingEmail = new PstSettingEmail();
                        rsCode = pstSettingEmail.delete(appSettingEmailOID);

                        if(rsCode == FRMMessage.NONE){
                            msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                            errCode = FRMMessage.ERR_DELETED;
                        }
                        else
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                    }
                }
                break;

            default:

        }//end switch
        return excCode;
    }


}
