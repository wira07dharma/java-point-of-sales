/*
 * CtrlAppPriv.java
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
import com.dimata.qdep.form.*;

public class CtrlAppPrivilegeObj {
    
    private String msgString;
    private int start;
    private AppPrivilegeObj appPrivObj;
    private PstAppPrivilegeObj pstAppPrivObj;
    private FrmAppPrivilegeObj frmAppPrivObj;
    
    /** Creates new CtrlAppPriv */
    public CtrlAppPrivilegeObj(HttpServletRequest request) {
        msgString = "";
        // errCode = Message.OK;
        
        appPrivObj = new AppPrivilegeObj();
        try{
            pstAppPrivObj = new PstAppPrivilegeObj(0);
        }catch(Exception e){}
        frmAppPrivObj = new FrmAppPrivilegeObj(request);
    }
    
    
    public AppPrivilegeObj getAppPrivObj() {
        return appPrivObj;
    }
    
    public FrmAppPrivilegeObj getForm() {
        return frmAppPrivObj;
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
    
    
    
    public int action(int cmd, long appPrivObjOID) {
        msgString = "";
        int excCode = 0;
        switch(cmd){
            case Command.ADD :
                appPrivObj.setPrivId(0);
                appPrivObj.setCode(0);
                appPrivObj.setPrivObjId(0);
                appPrivObj.setCode(0);
                appPrivObj.setCommands(new Vector(1,1));
                break;
                
            case Command.SAVE :
                frmAppPrivObj.requestEntityObject(appPrivObj);
                appPrivObj.setOID(appPrivObjOID);
                
                System.out.println("*** errSize : " + frmAppPrivObj.errorSize());
                
                if( (frmAppPrivObj.errorSize() > 0) || (appPrivObj.getCommandsSize()<1)) {
                    excCode = FRMMessage.MSG_INCOMPLATE;
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return excCode;
                }
                
                long rsCode = 0;
                if (appPrivObj.getOID() ==0 ){
                    
                    rsCode = PstAppPrivilegeObj.insert(this.appPrivObj);
                    
                    /* to get the start number of list last */
                    /*
                    int mdl = (vectSize+1)  % recordToGet;
                    if(mdl>0){
                        this.start = (vectSize+1)  - mdl;
                    }
                    else{
                        this.start = (vectSize+1) - recordToGet;
                    }
                     **/
                }
                else
                    rsCode = PstAppPrivilegeObj.update(this.appPrivObj);
                
                if(rsCode == FRMMessage.NONE){
                    msgString = FRMMessage.getErr(FRMMessage.ERR_SAVED);
               		excCode = FRMMessage.ERR_SAVED;
                }else
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                
                break;
                
            case Command.EDIT :
                
                if (appPrivObjOID != 0){
                    appPrivObj = (AppPrivilegeObj)pstAppPrivObj.fetch(appPrivObjOID);
                }
                break;
                
            case Command.ASK :
                
                if (appPrivObjOID != 0){
                    appPrivObj = (AppPrivilegeObj)pstAppPrivObj.fetch(appPrivObjOID);
                    msgString = FRMMessage.getErr(FRMMessage.MSG_ASKDEL);
                }
                break;
                
            case Command.DELETE :
                
                if (appPrivObjOID != 0){

                    PstAppPrivilegeObj pstAppPrivObj = new PstAppPrivilegeObj();

                    appPrivObj = (AppPrivilegeObj)pstAppPrivObj.fetch(appPrivObjOID);

                    rsCode = pstAppPrivObj.deleteByPrivOIDObjCode(appPrivObj.getPrivId() , appPrivObj.getCode() );
                    
                    if(rsCode == FRMMessage.NONE)
                        msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                    else
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                }
                break;
                
                
            default:
                
        }//end switch
        return excCode;
    }
    
}
