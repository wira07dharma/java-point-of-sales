/*
 * CtrlContactEmployee.java
 *
 * Created on March 26, 2002, 10:00 AM
 */

/**
 *
 * @author  edarmasusila
 * @version 
 */


package com.dimata.common.form.contact;

import javax.servlet.*;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*; 
import java.util.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.form.contact.*;
import com.dimata.qdep.form.*;

public class CtrlContactPersonell extends Control implements I_Language {

    private String msgString;
    private int start;
    private int iErrCode = FRMMessage.ERR_NONE;
    private ContactList contactList;
    private PstContactList pstContact;
    private FrmContactPersonell frmContactPsl;

    public CtrlContactPersonell(HttpServletRequest request) {
        msgString = "";
        contactList = new ContactList();
        try{
            pstContact = new PstContactList(0);
        }catch(Exception e){}
        	frmContactPsl = new FrmContactPersonell(request);
    }
    

    public ContactList getContactPersonell()
    {
        return contactList;
    }
    

    public FrmContactPersonell getForm()
    {
        return frmContactPsl;
    }
    
    
    public String getMessage()
    {
        return msgString;
    }

     public int getErrCode()
    {
        return iErrCode;
    }
        
    public int getStart()
    {
        return start;
    }
    
    //public void action(int cmd, long contactPslOID, int start, int vectSize, int recordToGet, Vector vectClassAssign, boolean sameCode) 
      public void action(int cmd, long contactPslOID, int start, int vectSize, int recordToGet, Vector vectClassAssign, boolean sameCode) throws Exception
    {
        msgString = "";
        long rsCode = 0;
        switch(cmd){
	        case Command.ADD :
	            break;

            case Command.SAVE :
				if(contactPslOID != 0){
					try{
						contactList = PstContactList.fetchExc(contactPslOID);
					}catch(Exception exc){
					}
				}

                frmContactPsl.requestEntityObject(contactList);

                if(!sameCode){
	                boolean bool = PstContactList.cekCodeContact(contactList.getContactCode(),contactPslOID);
	                if(bool)
						frmContactPsl.addError(frmContactPsl.FRM_CODE,"Personnel code is alredy defined.");
                }

                if(frmContactPsl.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    iErrCode = FRMMessage.ERR_SAVED;
                    return;
                }


                if (contactList.getOID() == 0)
                    try{
	                rsCode = PstContactList.insertExc(this.contactList);
                	}catch(Exception e){}
                else
                    try{
                    rsCode = PstContactList.updateExc(this.contactList);
                    }catch(Exception e){}
                if(rsCode == FRMMessage.NONE)       {  // error condition
                    msgString = FRMMessage.getErr(FRMMessage.ERR_SAVED);
                	iErrCode = FRMMessage.ERR_SAVED;
                }
                else{
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                    iErrCode = FRMMessage.ERR_NONE;
                }

                PstContactClassAssign.deleteClassAssign(contactPslOID);
                System.out.println("=========================================vectClassAssign.size() : "+vectClassAssign.size());
                if((vectClassAssign!=null)&&(vectClassAssign.size()>0)){
					for(int i=0;i<vectClassAssign.size();i++){
                    	ContactClassAssign cntClsAssign = (ContactClassAssign)vectClassAssign.get(i);
                        cntClsAssign.setContactId(contactList.getOID());                                                                
                        PstContactClassAssign.insertExc(cntClsAssign);
                    }
                }

            break;
            
            case Command.EDIT :
				try{
				if (contactPslOID > 0){
					contactList = (ContactList)pstContact.fetchExc(contactPslOID);
				}
            	}catch(Exception e){}
            break;

        	case Command.ASK :
                try{
                if (contactPslOID > 0){
					contactList = (ContactList)pstContact.fetchExc(contactPslOID);
				}
                }catch(Exception e){}
            break;
            
            case Command.DELETE :

                if (contactPslOID > 0){
					PstContactList pstContactPersonell = new PstContactList();
                        try{
        				rsCode = pstContactPersonell.deleteExc(contactPslOID);
                        }catch(Exception e){}
                    if(rsCode == FRMMessage.NONE) {
	                    msgString = FRMMessage.getErr(FRMMessage.ERR_DELETED);
                        iErrCode = FRMMessage.ERR_DELETED;
                    }
	                else{
	                    msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                        iErrCode = FRMMessage.ERR_NONE;
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
    }

}
