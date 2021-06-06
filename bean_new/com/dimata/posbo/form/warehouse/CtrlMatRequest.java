/* 
 * Ctrl Name  		:  CtrlMaterialRequest.java 
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
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.*;

public class CtrlMatRequest extends Control implements I_Language 
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
	private MatDispatch materialDispatch;
	private PstMatDispatch pstMatDispatch;
	private FrmMatRequest frmMaterialRequest;
	int language = LANGUAGE_DEFAULT;

	public CtrlMatRequest(HttpServletRequest request){
		msgString = "";
		materialDispatch = new MatDispatch();
		try{
			pstMatDispatch = new PstMatDispatch(0);
		}catch(Exception e){;}
		frmMaterialRequest = new FrmMatRequest(request, materialDispatch);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmMaterialRequest.addError(frmMaterialRequest.FRM_FIELD_MAT_DISPATCH_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public MatDispatch getMatDispatch() { return materialDispatch; } 

	public FrmMatRequest getForm() { return frmMaterialRequest; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidMatDispatch){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidMatDispatch != 0){
					try{
						materialDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
					}catch(Exception exc){
					}
				}

            	//Date oldDate = materialDispatch.getDfmDate();

				frmMaterialRequest.requestEntityObject(materialDispatch);


                //int dfType = SessMatDispatch.getDispatchRequestType(materialDispatch.getDispatchFrom(), materialDispatch.getDispatchTo());

                //materialDispatch.setDispatchType(dfType);


                try{
	                I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	                //materialDispatch.setDocType(i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR));

                    System.out.println("----------------> i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) : "+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR));

                }
                catch(Exception e){
                    System.out.println("Exception e - interface : "+e.toString());
                }

                SessMatDispatch sessDispatch = new SessMatDispatch();

				if(frmMaterialRequest.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

                /*
                if(materialDispatch.getDispatchFrom()==materialDispatch.getDispatchTo()){
                    msgString = "target is the same as material location ....";
					return RSLT_FORM_INCOMPLETE ;
                }
                 */

				if(materialDispatch.getOID()==0){
					try{

                        //int maxCounter = sessDispatch.getMaxReqCounter(materialDispatch.getDfmDate());
		                //maxCounter = maxCounter + 1;
		                //materialDispatch.setDispatchCodeCounter(maxCounter);

                        //System.out.println("----maxCounter2 : "+maxCounter);
		                //System.out.println("----materialDispatch : "+materialDispatch.getOID());
		                //System.out.println("----maxCounter1 : "+maxCounter);
		                //.out.println("after + 1 ctrl : -->"+maxCounter);
		
		                //String code = DocCodeGenerator.getDocumentCode(materialDispatch.getDocType(), materialDispatch.getDfmDate(), materialDispatch.getDispatchCodeCounter());//sessDispatch.generateRequestCode(materialDispatch);
		
		                //materialDispatch.setDispatchCode(code);

						long oid = pstMatDispatch.insertExc(this.materialDispatch);
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
                        //String oldDateStr = Formater.formatDate(oldDate, "dd-MM-yyyy");
                        //String newDate = Formater.formatDate(materialDispatch.getDfmDate(), "dd-MM-yyyy");

                        /*                    
                        if(!oldDateStr.equals(newDate)){
                            //update on code
                            //int maxCounter = sessDispatch.getMaxReqCounter(materialDispatch.getDfmDate());
			                maxCounter = maxCounter + 1;
			                materialDispatch.setDispatchCodeCounter(maxCounter);
	
	                        //System.out.println("----maxCounter2 : "+maxCounter);
			                //System.out.println("----materialDispatch : "+materialDispatch.getOID());
			               // System.out.println("----maxCounter1 : "+maxCounter);
			               // System.out.println("after + 1 ctrl : -->"+maxCounter);
			
			                String code = DocCodeGenerator.getDocumentCode(materialDispatch.getDocType(), materialDispatch.getDfmDate(), materialDispatch.getDispatchCodeCounter());//sessDispatch.generateRequestCode(materialDispatch);
			
			                materialDispatch.setDispatchCode(code);

                        }
                         */

						long oid = pstMatDispatch.updateExc(this.materialDispatch);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidMatDispatch != 0) {
					try {
						materialDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidMatDispatch != 0) {
					try {
						materialDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidMatDispatch != 0){
					try{
						long oid = PstMatDispatch.deleteExc(oidMatDispatch);
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
