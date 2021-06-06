/*
 * CtrlSystemProperty.java
 *
 * Created on March 18, 2002, 4:27 PM
 */


/**
 *
 * @author  gmudiasa/echa
 * @version 
 */

package com.dimata.system.form;

import com.dimata.common.entity.system.AppValue;
import javax.servlet.*;
import javax.servlet.http.*;

import com.dimata.util.*;

import com.dimata.qdep.entity.*;
import com.dimata.system.entity.*;

import com.dimata.qdep.form.*;
import com.dimata.system.form.*;
import com.dimata.system.session.*;

public class CtrlSystemProperty {

    private int msgCode = 0;
    private long rsCode = 0;
    private String msgString;
        
    private SystemProperty sysProp;
    private PstSystemProperty pstSystemProperty;
    private FrmSystemProperty frmSystemProperty;
    
    
    public CtrlSystemProperty(HttpServletRequest request) {
        msgString = "";        
        sysProp = new SystemProperty(); 
        try{
            pstSystemProperty = new PstSystemProperty(0);
        }catch(Exception e){}
        frmSystemProperty = new FrmSystemProperty(request, sysProp);
    }
    

    public SystemProperty getSystemProperty() {
        return sysProp;
    }
    

    public FrmSystemProperty getForm() {
        return frmSystemProperty;
    }
    
    
    public String getMessage(){
        return FRMMessage.getMessage(msgCode);
    }

        
    public int getMsgCode(){
        return msgCode;
    }

   
    public void action(int cmd, long oid) {
        action(cmd, oid, null);
    }
    
    public void action(int cmd, long oid, HttpServletRequest req)
    {
        rsCode = 0;
        msgString = "";        
        switch(cmd){ 
            case Command.SAVE :
                frmSystemProperty.requestEntityObject(sysProp);
                
                System.out.println("*** errSize : " + frmSystemProperty.errorSize());
                if(frmSystemProperty.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return;
                }                
                
                if(oid == 0) {
                    rsCode = pstSystemProperty.insert(sysProp);
                } else {
                    sysProp.setOID(oid);
                    rsCode = pstSystemProperty.update(sysProp);
                }


                if(rsCode == FRMMessage.NONE){
                    if (oid == 0)
                    	msgCode = FRMMessage.ERR_SAVED;
                	else
                        msgCode = FRMMessage.ERR_UPDATED;
                }else{
                    if (oid == 0)
                    	msgCode = FRMMessage.MSG_SAVED;
                	else
                        msgCode = FRMMessage.MSG_UPDATED;
                }

                break;
            
            case Command.EDIT :
                sysProp = PstSystemProperty.fetch(oid);
                frmSystemProperty.setEntityObject(sysProp);
                break;
            

            case Command.UPDATE : // update value only                                
                String newValue = FRMQueryString.requestString(req, FrmSystemProperty.fieldNames[FrmSystemProperty.FRM_VALUE]);
                
                if(newValue.trim().length() == 0) {
                    frmSystemProperty.addError(FrmSystemProperty.FRM_VALUE, " <<");
                }else{
                    SystemProperty sp = PstSystemProperty.fetch(oid);
                    sp.setValue(newValue);
                    rsCode = PstSystemProperty.update(sp);                    
                }
                               
                if(rsCode == FRMMessage.NONE) {
                    msgString = FRMMessage.getErr(FRMMessage.ERR_UPDATED);
                }else {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_UPDATED);
                }                
                break;

            
            case Command.ASK:                
                msgCode = FRMMessage.MSG_ASKDEL;
                msgString = FRMMessage.getMsg(FRMMessage.MSG_ASKDEL);
                break;

                
            case Command.DELETE :    
                rsCode = PstSystemProperty.delete(oid);
                break;

            case Command.LOAD :    
                SessSystemProperty sysProp = new SessSystemProperty();
                AppValue.loadSystemProperty();
                break;
            
            default:
                
        }//end switch
    }
    
 
}
