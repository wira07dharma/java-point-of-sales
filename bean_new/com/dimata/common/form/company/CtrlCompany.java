/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.company;






import com.dimata.posbo.entity.masterdata.Company;
import com.dimata.posbo.entity.masterdata.PstCompany;
import com.dimata.posbo.form.masterdata.FrmCompany;

import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Acer
 */
public class CtrlCompany extends Control implements I_Language {
    
    
     public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText ={
        {"Berhasil","Tidak Bisa Di Prosess","kode Sudah Ada","Data Belum Lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private  Company company;
    private PstCompany pstCompany;
    private FrmCompany frmCompany;
    int language = LANGUAGE_DEFAULT;

    public CtrlCompany (HttpServletRequest request){
                msgString = "";
                company = new Company();
                try{
                     pstCompany = new PstCompany(0);
                }catch(Exception e){
                    System.out.println("Exception"+e);
                   
                }
		frmCompany = new FrmCompany(request, company);
	}
    private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
                        this.frmCompany.addError(FrmCompany.FRM_FLD_COMPANY_ID,                                        
                             resultText[language][RSLT_EST_CODE_EXIST] );
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
    public Company getCompany() { return company; }
    public FrmCompany getForm() { return frmCompany; }
    public String getMessage(){ return msgString; }
    public int getStart() { return start; }
    public int action(int cmd , long oidCompany){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
                switch(cmd){
			case Command.ADD :
				break;
                        case Command.RESET :
                            company.setCompanyCode("");
                            company.setCompanyName("");
                            company.setPersonName("");
                            company.setPersonLastName("");
                            company.setBusAddress("");
                            company.setTown("");
                            company.setProvince("");
                            company.setCountry("");
                            company.setTelpNr("");
                            company.setTelpMobile("");
                            company.setFax("");
                            company.setEmailCompany("");
                            company.setPostalCode("");  
                            
				break;
                        case Command.SAVE :
				if(oidCompany != 0){
					try{
						company = PstCompany.fetchExc(oidCompany);
					}catch(Exception exc){}
				}
				frmCompany.requestEntityObject(company);
				if(frmCompany.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}
				if(company.getOID()==0){
					try{
						long oid = pstCompany.insertExc(this.company);
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
						long oid = pstCompany.updateExc(this.company);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;
			case Command.EDIT :

				if (oidCompany != 0) {

					try {

						company = PstCompany.fetchExc(oidCompany);

					} catch (DBException dbexc){ 

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidCompany != 0) {

					try {

						company = PstCompany.fetchExc(oidCompany);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;
			case Command.DELETE :
				if (oidCompany!= 0){

					try{
						long oid = PstCompany.deleteExc(oidCompany);
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
