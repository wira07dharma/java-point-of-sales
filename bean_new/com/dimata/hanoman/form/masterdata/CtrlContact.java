/*

 * Ctrl Name  		:  CtrlContact.java 

 * Created on 	:  [date] [time] AM/PM 

 * 

 * @author  		:  [authorName] 

 * @version  		:  [version] 

 */



/*******************************************************************

 * Class Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 		: [output ...] 

 *******************************************************************/



package com.dimata.hanoman.form.masterdata;



/* java package */ 

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.*; 


import javax.servlet.http.*; 

/* dimata package */

import com.dimata.util.*;

import com.dimata.util.lang.*;

/* qdep package */

import com.dimata.qdep.system.*;

import com.dimata.qdep.form.*;

import com.dimata.posbo.db.DBException;

/* project package */

//import com.dimata.qdep.db.*;

import com.dimata.hanoman.entity.masterdata.*;




public class CtrlContact extends Control implements I_Language 

{

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

	
	private Contact contact;

           Contact prevPo = null;

        private ContactClassAssign contactClassAssign;
        
         private Date purchDate = null;
            Date dateLog = new  Date();
       

	private PstContact pstContact;

        private PstContactClassAssign pstContactClassAssign;

	private FrmContact frmContact;

	int language = LANGUAGE_DEFAULT;



	public CtrlContact(HttpServletRequest request){

		msgString = "";

		contact = new Contact();
                contactClassAssign = new ContactClassAssign(); 

		try{

			pstContact = new PstContact(0);

                       
		}catch(Exception e){;}

		frmContact = new FrmContact(request, contact);

                
                
                try{

			
                        
                        pstContactClassAssign = new PstContactClassAssign(0);

		}catch(Exception e){;}

		
	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmContact.addError(frmContact.FRM_FIELD_CONTACT_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public Contact getContact() { return contact; } 



	public FrmContact getForm() { return frmContact; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }

        

	public int action(int cmd , long oidContact, int contactClassType){
             return action(cmd, oidContact, contactClassType, 0,0,"");
        }
        
        public int action(int cmd , long oidContact,long oidContactClassAssign, int contactClassType, long userID, String nameUser){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

                long oid = 0;

		switch(cmd){

			case Command.ADD :

				break;

			case Command.SAVE :

				if(oidContact != 0){

					try{

						contact = PstContact.fetchExc(oidContact);

					}catch(Exception exc){

					}

                                        //update  by fitra untuk membandingkan entity yg dulu dengan yang sekarang.
                                        try{
                                            
                                            prevPo =  PstContact.fetchExc(oidContact);
                                        }catch (Exception exc){
                                            
				}

				}



				frmContact.requestEntityObject(contact);



                                if(!isOutOfChar(contact.getCompName(), "/"))

                                    frmContact.addError(frmContact.FRM_FIELD_COMP_NAME,"Illegal character '/'");



                                if(!isOutOfChar(contact.getPersonName(), "/"))

                                    frmContact.addError(frmContact.FRM_FIELD_PERSON_NAME,"Illegal character '/'");



                                boolean bool = PstContact.cekCodeContact(contact.getContactCode(),oidContact);//, contact.getContactType() );

                                if(bool){

					frmContact.addError(frmContact.FRM_FIELD_CONTACT_CODE,"duplicat entry");

                                        msgString = "Can't save data, duplicat entry for company code";

					return RSLT_FORM_INCOMPLETE ;

                                }






				if(frmContact.errorSize()>0) {

                                    if(contact.getContactType()==PstContact.CONTACT_TYPE_GUIDE && contact.getCompName().length()<1){

                                        //remove error comp name

                                        frmContact.removeError(frmContact.FRM_FIELD_COMP_NAME);

                                    }

                                    

                                    if(frmContact.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

                                    }                                    

				}



				if(contact.getOID()==0){

					try{

						oid = pstContact.insertExc(this.contact);
                                                
					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

					}



				}else{

					try {

						oid = pstContact.updateExc(this.contact);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 

					}



				}

                                // start setting contact class

                                PstContactClassAssign.deleteClassAssign(oid);

                                String whClause = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + contactClassType;

                                Vector vectContactClass = PstContactClass.list(0,0,whClause,"");

                                long contactClassId = 0;

                                if(vectContactClass!=null && vectContactClass.size()>0){

                                    ContactClass contactClass = (ContactClass)vectContactClass.get(0);

                                    contactClassId = contactClass.getOID();

                                }

                                ContactClassAssign contactClassAssign = new ContactClassAssign();

                                contactClassAssign.setContactId(oid);

                                contactClassAssign.setContactClassId(contactClassId);

                                PstContactClassAssign.insertExc(contactClassAssign);

                                // end setting contact class

				break;

			case Command.EDIT :

				if (oidContact != 0) {

					try {

						contact = PstContact.fetchExc(oidContact);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidContact != 0) {

					try {

						contact = PstContact.fetchExc(oidContact);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidContact != 0){

					try{

                                                long oidChild = PstContactClassAssign.deleteClassAssign(oidContact);

						oid = PstContact.deleteExc(oidContact);

						if(oid!=0){

							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);

							excCode = RSLT_OK;
                                                        // update by fitra
                                                        try
                                                       {   
                                                         insertHistory(userID, nameUser, cmd, oid);
                                                       }
                                                        catch(Exception e)
                                                         {   

                                                         }
                                                        try{
                                                        prevPo = PstContact.fetchExc(oidContact);
                                                        } catch(Exception exc){
                                                            
                                                        }

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



    public static boolean isOutOfChar(String str, String separator){

        if(str!=null && str.length()>0){

			int idx = str.indexOf(separator);

            if(idx>-1){

                return false;

            }

            return true;

        }

        return true;

    }
// update by fitra
public  void insertHistory(long userID, String nameUser, int cmd, long oid)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("masterdata/travel.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType("Masterdata Contact");
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(contact.getContactCode());
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(this.contact.getLogDetail(prevPo));
           //jika sudah lewat 2 jam di log
           if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE)
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
           }

           
      }
      catch(Exception e)
      {
System.out.println("error "+e);
      }
    }


   
    

}

