/*
 * CtrlAppPriv.java
 *
 * Created on April 3, 2002, 10:48 AM
 */

package com.dimata.posbo.form.admin;

/**
 *
 * @author  ktanjana
 * @version
 */

import javax.servlet.*;
import javax.servlet.http.*;
import com.dimata.util.*;
import java.util.*;
import com.dimata.posbo.entity.admin.*;
import com.dimata.posbo.form.admin.*;
import com.dimata.qdep.form.*;

public class CtrlAppPriv {
    
    private String msgString;
    private int start;
    private AppPriv appPriv;
    private PstAppPriv pstAppPriv;
    private FrmAppPriv frmAppPriv;
    
    /** Creates new CtrlAppPriv */
    public CtrlAppPriv(HttpServletRequest request) {
        msgString = "";
        // errCode = Message.OK;
        
        appPriv = new AppPriv();
        try{
            pstAppPriv = new PstAppPriv(0);
        }catch(Exception e){}
        frmAppPriv = new FrmAppPriv(request);
    }
    
    
    public AppPriv getAppPriv() {
        return appPriv;
    }
    
    public FrmAppPriv getForm() {
        return frmAppPriv;
    }
    
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long appPrivOID, int start, int vectSize, int recordToGet) {
        msgString = "";
        int excCode = 0;
        switch(cmd){
            case Command.ADD :
                appPriv.setPrivName("");
                appPriv.setRegDate(new Date());
                break;
                
            case Command.SAVE :
                frmAppPriv.requestEntityObject(appPriv);
                appPriv.setOID(appPrivOID);
                
                System.out.println("*** errSize : " + frmAppPriv.errorSize());
                
                if(frmAppPriv.errorSize() > 0) {
                    excCode = FRMMessage.MSG_INCOMPLATE;
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return excCode;
                }
                
                long rsCode = 0;
                if (appPriv.getOID() == 0){
                    rsCode = PstAppPriv.insert(this.appPriv);
                }
                else
                    rsCode = PstAppPriv.update(this.appPriv);
                    this.start = start;
                if(rsCode == FRMMessage.NONE){
                    excCode = FRMMessage.ERR_SAVED;
                    msgString = FRMMessage.getErr(excCode);
                }else
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                
                break;
                
            case Command.EDIT :
                if (appPrivOID != 0){
                    appPriv = (AppPriv)pstAppPriv.fetch(appPrivOID);
                }
                break;
                
            case Command.ASK :
                if (appPrivOID !=0 ){
                    appPriv = (AppPriv)pstAppPriv.fetch(appPrivOID);
                    excCode = FRMMessage.MSG_ASKDEL;
                    msgString = FRMMessage.getErr(FRMMessage.MSG_ASKDEL);
                }
                break;
                
            case Command.DELETE :
                if (appPrivOID != 0){
                    rsCode = PstAppPrivilegeObj.deleteByPrivOID(appPrivOID);
                    if(rsCode == FRMMessage.NONE)
                        msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                    else{
                        rsCode = PstGroupPriv.deleteByPriv(appPrivOID);
                        if(rsCode == FRMMessage.NONE)
                            msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                        else{                                            
                            PstAppPriv pstAppPriv = new PstAppPriv();
                            rsCode = pstAppPriv.delete(appPrivOID);

                            if(rsCode == FRMMessage.NONE)
                                msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                            else
                                msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                        }
                    }
                    
                }
                break;
                
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
                
        }//end switch
        return excCode;
    }
    
}
