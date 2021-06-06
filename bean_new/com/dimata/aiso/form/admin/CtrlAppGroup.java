/*
 * CtrlAppGroup.java
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
import com.dimata.util.*;
import java.util.*;
import com.dimata.aiso.entity.admin.*;
import com.dimata.aiso.form.admin.*;
import com.dimata.aiso.session.admin.*;
import com.dimata.qdep.form.*;

public class CtrlAppGroup {
    
    private String msgString;
    private int start;
    private AppGroup appGroup;
    private PstAppGroup pstAppGroup;
    private FrmAppGroup frmAppGroup;
    
    /** Creates new CtrlAppGroup */
    public CtrlAppGroup(HttpServletRequest request) {
        msgString = "";
        // errCode = Message.OK;
        
        appGroup = new AppGroup();
        try{
            pstAppGroup = new PstAppGroup(0);
        }catch(Exception e){}
        frmAppGroup = new FrmAppGroup(request);
    }
    
    
    public AppGroup getAppGroup() {
        return appGroup;
    }
    
    public FrmAppGroup getForm() {
        return frmAppGroup;
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
    
    public int action(int cmd, long appGroupOID) {
        msgString = "";
        int excCode = 0;
        switch(cmd){
            case Command.ADD :
                appGroup.setGroupName("");
                appGroup.setRegDate(new Date());
                break;
                
            case Command.SAVE :
                frmAppGroup.requestEntityObject(appGroup);
                appGroup.setOID(appGroupOID);
                
                System.out.println("*** errSize : " + frmAppGroup.errorSize());
                
                if(frmAppGroup.errorSize() > 0) {
                    excCode = FRMMessage.MSG_INCOMPLATE;
                    msgString = FRMMessage.getMsg(excCode);
                    return excCode;
                }
                
                long rsCode = 0;
                if (appGroup.getOID() == 0)
                    rsCode = PstAppGroup.insert(this.appGroup);
                else
                    rsCode = PstAppGroup.update(this.appGroup);
                
                if(rsCode == FRMMessage.NONE){
                    	excCode= FRMMessage.ERR_SAVED;
                        msgString = FRMMessage.getErr(FRMMessage.ERR_SAVED);
                    }
                else{                    
                    Vector groupPriv = this.frmAppGroup.getGroupPriv(this.appGroup.getOID());
                    
                    if(SessAppGroup.setGroupPriv(this.appGroup.getOID(), groupPriv))                    
                        msgString = FRMMessage.getMsg(FRMMessage.ERR_SAVED);
                    else {
                        msgString = FRMMessage.getErr(FRMMessage.ERR_UNKNOWN);
                    	excCode=0;
                    }
                }
                
                break;
                
            case Command.EDIT :
                
                if (appGroupOID !=0 ){
                    appGroup = (AppGroup)pstAppGroup.fetch(appGroupOID);
                }
                break;
                
            case Command.ASK :
                
                if (appGroupOID != 0){
                    appGroup = (AppGroup)pstAppGroup.fetch(appGroupOID);
                    excCode = FRMMessage.MSG_ASKDEL;
                    msgString = FRMMessage.getErr(FRMMessage.MSG_ASKDEL);
                }
                break;
                
            case Command.DELETE :
                
                if (appGroupOID != 0){
                    
                    rsCode = PstGroupPriv.deleteByGroup(appGroupOID);
                    if(rsCode == FRMMessage.NONE){
                        excCode = FRMMessage.ERR_DELETED;
                        msgString = FRMMessage.getErr(excCode);
                    }
                    else{

                        PstAppGroup pstAppGroup = new PstAppGroup();
                        rsCode = pstAppGroup.delete(appGroupOID);

                        if(rsCode == FRMMessage.NONE){
                            excCode = FRMMessage.ERR_DELETED;
                            msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                        }else
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                    }
                }
                break;
                                
            default:
                
        }//end switch
        return excCode;
    }
    
}
