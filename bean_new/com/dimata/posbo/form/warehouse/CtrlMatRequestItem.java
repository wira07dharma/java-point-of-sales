/*
 * Ctrl Name  		:  CtrlMatRequestItem.java
 * Created on           :  [date] [time] AM/PM
 *
 * @author  		:  [authorName]
 * @version  		:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

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
//import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.*;

public class CtrlMatRequestItem extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private MatDispatchItem materialDispatchItem;
    private PstMatDispatchItem pstMatDispatchItem;
    private FrmMatRequestItem frmMaterialRequestItem;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMatRequestItem(HttpServletRequest request){
        msgString = "";
        materialDispatchItem = new MatDispatchItem();
        try{
            pstMatDispatchItem = new PstMatDispatchItem(0);
        }catch(Exception e){;}
        frmMaterialRequestItem = new FrmMatRequestItem(request, materialDispatchItem);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmMaterialRequestItem.addError(frmMaterialRequestItem.FRM_FIELD_MATERIAL_DISPATCH_ITEM_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public MatDispatchItem getMatDispatchItem() { return materialDispatchItem; }
    
    public FrmMatRequestItem getForm() { return frmMaterialRequestItem; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public int action(int cmd , long oidMatDispatchItem, long oidMatDispatch, long materialId, long materialGroup){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd){
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidMatDispatchItem != 0){
                    try{
                        materialDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    }catch(Exception exc){
                    }
                }
                
                frmMaterialRequestItem.requestEntityObject(materialDispatchItem);
                
                if(frmMaterialRequestItem.errorSize()>0 || oidMatDispatch==0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                /*if command==add, check for request item, if already exis=> return error*/
                /*
                if(oidMatDispatchItem==0){
                        if(SessMatDispatchItem.checkMaterialItem(oidMatDispatch, materialGroup, materialId)){
                            msgString = "can't complete ... item already add to the request... ";
                                                return RSLT_FORM_INCOMPLETE ;
                        }
                }
                 */
                
                //materialDispatchItem.setMatDispatchId(oidMatDispatch);
                
                //-------------- get material stock ID -------
                /*
                String where  = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_GROUP_ID]+"="+materialGroup+" AND "+
                    PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID]+"="+materialId;
                 */
                
                String where  = "";
                MatDispatch matDispatch = new MatDispatch();
                try{
                    matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                }
                catch(Exception e){
                    System.out.println("Exceptio  e: "+e.toString());
                }
                
                Vector temp = PstMaterialStock.list(0,0,where, null);
                long stockOID = 0;
                if(temp!=null && temp.size()>0){
                    for(int i=0; i<temp.size(); i++){
                        MaterialStock matStock = (MaterialStock)temp.get(i);
                        System.out.println("----------------------------> index : "+i);
                        System.out.println("----------------------------> matStock.getLocationId() : "+matStock.getLocationId());
                        //System.out.println("----------------------------> matDispatch.getDispatchFrom() : "+matDispatch.getDispatchFrom());
                        //if(matStock.getLocationId()==matDispatch.getDispatchFrom()){
                        //stockOID = matStock.getOID();
                        //break;
                        //}
                    }
                }
                
                System.out.println("----------------------------> stockOID : "+stockOID);
                
                //materialDispatchItem.setMatStockId(stockOID);
                
                //----------------------------------------------
                
                if(materialDispatchItem.getOID()==0){
                    try{
                        long oid = pstMatDispatchItem.insertExc(this.materialDispatchItem);
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }else{
                    try {
                        long oid = pstMatDispatchItem.updateExc(this.materialDispatchItem);
                    }catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                break;
                
            case Command.EDIT :
                if (oidMatDispatchItem != 0) {
                    try {
                        materialDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidMatDispatchItem != 0) {
                    try {
                        materialDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidMatDispatchItem != 0){
                    try{
                        long oid = PstMatDispatchItem.deleteExc(oidMatDispatchItem);
                        if(oid!=0){
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }else{
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
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
