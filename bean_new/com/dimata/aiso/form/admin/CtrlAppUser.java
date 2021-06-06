/*
 * CtrlAppUser.java
 *
 * Created on April 3, 2002, 10:48 AM
 */

package com.dimata.aiso.form.admin;

/**
 *
 * @author  ktanjana
 * @version
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
// dimata & qdep specific package
import com.dimata.util.*;
import com.dimata.aiso.db.*;
import com.dimata.qdep.form.*;
// aiso specific package
import com.dimata.aiso.entity.admin.*;
import com.dimata.aiso.form.admin.*;
import com.dimata.aiso.session.admin.*;
import com.dimata.common.entity.custom.PstDataCustom;


public class CtrlAppUser {

    private String msgString;
    private int start;
    private AppUser appUser;
    private PstAppUser pstAppUser;
    private FrmAppUser frmAppUser;

    /** Creates new CtrlAppUser */
    public CtrlAppUser(HttpServletRequest request) {
        msgString = "";
        // errCode = Message.OK;

        appUser = new AppUser();
        try {
            pstAppUser = new PstAppUser(0);
        } catch (Exception e) {
        }
        frmAppUser = new FrmAppUser(request);
    }


    public String getErrMessage(int errCode) {
        switch (errCode) {
            case FRMMessage.ERR_DELETED:
                return "Can't Delete User";
            case FRMMessage.ERR_SAVED:
                if (frmAppUser.getFieldSize() > 0)
                    return "Can't save user, cause some required data are incomplete ";
                else
                    return "Can't save user, Duplicate login ID, please type another login ID";
            default:
                return "Can't save user";
        }
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public FrmAppUser getForm() {
        return frmAppUser;
    }


    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    /*
     * return this.start
     **/
    public int actionList(int listCmd, int start, int vectSize, int recordToGet) {
        msgString = "";

        switch (listCmd) {
            case Command.FIRST:
                this.start = 0;
                break;

            case Command.PREV:
                this.start = start - recordToGet;
                if (start < 0) {
                    this.start = 0;
                }
                break;

            case Command.NEXT:
                this.start = start + recordToGet;
                if (start >= vectSize) {
                    this.start = start - recordToGet;
                }
                break;

            case Command.LAST:
                int mdl = vectSize % recordToGet;
                if (mdl > 0) {
                    this.start = vectSize - mdl;
                } else {
                    this.start = vectSize - recordToGet;
                }

                break;

            default:
                this.start = start;
                if (vectSize < 1)
                    this.start = 0;

                if (start > vectSize) {
                    // set to last
                    mdl = vectSize % recordToGet;
                    if (mdl > 0) {
                        this.start = vectSize - mdl;
                    } else {
                        this.start = vectSize - recordToGet;
                    }
                }
                break;
        } //end switch
        return this.start;
    }

    public int action(int cmd, long appUserOID) {
        long rsCode = 0;
        int errCode = -1;
        int excCode = 0;

        msgString = "";
        switch (cmd) {
            case Command.ADD:
                appUser.setLoginId("");
                appUser.setRegDate(new Date());
                break;

            case Command.SAVE:                
                frmAppUser.requestEntityObject(appUser);
                appUser.setOID(appUserOID);              
                
                if (frmAppUser.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return FRMMessage.MSG_INCOMPLATE;
                }
                
                 if (appUser.getOID() != 0) {                   
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);                   
                    //return FRMMessage.MSG_SAVED;
                }
                
                
                try {
                    if (appUser.getOID() == 0){
                      this.appUser.setLastLoginDate(new Date());
                      this.appUser.setLastLoginIp("0");
                        rsCode = PstAppUser.insert(this.appUser);
                    }else{
                        rsCode = PstAppUser.update(this.appUser);                       
                    }
                    if (rsCode == FRMMessage.NONE) {
                        excCode = FRMMessage.ERR_SAVED;
                        msgString = getErrMessage(excCode);

                    } else {
                        // for user group
                        Vector userGroup = this.frmAppUser.getUserGroup(this.appUser.getOID());
                        if (SessAppUser.setUserGroup(this.appUser.getOID(), userGroup)) {
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                        } else {
                            msgString = FRMMessage.getErr(FRMMessage.ERR_UNKNOWN);
                            excCode = 0;
                        }

                        /** gadnyana
                         * for data custom
                         * jika tidak di perlukan
                         * uncomment perintah ini
                        */
                        Vector userLocs = this.frmAppUser.getUserAssignLocation();
                        if (SessAppUser.setUserAssignLocation(this.appUser.getOID(), userLocs)) {
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                        } else {
                            msgString = FRMMessage.getErr(FRMMessage.ERR_UNKNOWN);
                            excCode = 0;
                        }
                        
                        
                        /** edhy
                         * for data custom department
                         * jika tidak di perlukan
                         * uncomment perintah ini
                        */
                        Vector userDepts = this.frmAppUser.getUserAssignDepartment();
                        if (SessAppUser.setUserAssignDepartment(this.appUser.getOID(), userDepts)) 
                        {
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                        }
                        else 
                        {
                            msgString = FRMMessage.getErr(FRMMessage.ERR_UNKNOWN);
                            excCode = 0;
                        }
                    }
                } catch (DBException exc) {
                    excCode = exc.getErrorCode();
                    msgString = getErrMessage(excCode);
                }

                break;

            case Command.EDIT:

                if (appUserOID != 0) {
                    appUser = (AppUser) pstAppUser.fetch(appUserOID);
                }
                break;

            case Command.ASK:

                if (appUserOID != 0) {
                    appUser = (AppUser) pstAppUser.fetch(appUserOID);
                    msgString = FRMMessage.getErr(FRMMessage.MSG_ASKDEL);
                }
                break;

            case Command.DELETE:

                if (appUserOID != 0) {

                    rsCode = PstUserGroup.deleteByUser(appUserOID);

                    /** gadnyana
                     * untuk hapus data custom, jika tidak di perlukan
                     * uncomment perintah ini
                     */
                    rsCode = PstDataCustom.deleteCustomDataExc(PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+"="+appUserOID);
                    // -- end

                    if (rsCode != FRMMessage.NONE) 
                    {
                        System.out.println("Error deleteByUser - " + appUserOID);
                        msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                        errCode = FRMMessage.ERR_DELETED;
                    }
                    else 
                    {
                        PstAppUser pstAppUser = new PstAppUser();
                        rsCode = pstAppUser.delete(appUserOID);

                        if (rsCode == FRMMessage.NONE) {
                            msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED); 
                            errCode = FRMMessage.ERR_DELETED;
                        } else
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                    }
                }
                break;

            default:

        }//end switch
        return excCode;
    }

}
