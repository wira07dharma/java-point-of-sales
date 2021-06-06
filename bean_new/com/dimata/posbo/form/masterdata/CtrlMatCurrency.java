package com.dimata.posbo.form.masterdata;

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
import com.dimata.posbo.db.*;
/* project package */

import com.dimata.posbo.entity.masterdata.*;

public class CtrlMatCurrency extends Control implements I_Language 
{
	public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_EST_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = 
        {
		{"Berhasil", "Tidak dapat diproses", "Code Material MatCurrency sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Material MatCurrency Code already exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private MatCurrency matCurrency;
	private PstMatCurrency pstMatCurrency;
	private FrmMatCurrency frmMatCurrency;
	int language = LANGUAGE_FOREIGN;

	public CtrlMatCurrency(HttpServletRequest request)
        {
		msgString = "";
		matCurrency = new MatCurrency();
		try
                {
			pstMatCurrency = new PstMatCurrency(0);
		}
                catch(Exception e){;}
		frmMatCurrency = new FrmMatCurrency(request, matCurrency);
	}

	private String getSystemMessage(int msgCode)
        {
		switch (msgCode)
                {
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmMatCurrency.addError(frmMatCurrency.FRM_FIELD_CURRENCY_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public MatCurrency getMatCurrency() { return matCurrency; } 

	public FrmMatCurrency getForm() { return frmMatCurrency; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidMatCurrency)
        {
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd)
                {
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidMatCurrency != 0)
                                {
					try
                                        {
						matCurrency = PstMatCurrency.fetchExc(oidMatCurrency);
					}
                                        catch(Exception exc)
                                        {
					}
				}

				frmMatCurrency.requestEntityObject(matCurrency);

				if(frmMatCurrency.errorSize()>0) 
                                {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

                                String whereClause = "( "+PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE]+" = '"+matCurrency.getCode()+
                                                    "') AND "+ PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID]+" <> "+matCurrency.getOID();
                                Vector isExist = PstMatCurrency.list(0,1,whereClause,"");
                                if(isExist != null && isExist.size()>0)
                                {
                                        msgString = resultText[language][RSLT_EST_CODE_EXIST];
                                                        return RSLT_EST_CODE_EXIST ;
                                }


				if(matCurrency.getOID()==0)
                                {
					try
                                        {
						long oid = pstMatCurrency.insertExc(this.matCurrency);
					}
                                        catch(DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
						return getControlMsgId(excCode);
					}
                                        catch (Exception exc)
                                        {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}
                                else
                                {
					try 
                                        {
						long oid = pstMatCurrency.updateExc(this.matCurrency);
					}
                                        catch (DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}
                                        catch (Exception exc)
                                        {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidMatCurrency != 0) 
                                {
					try 
                                        {
						matCurrency = PstMatCurrency.fetchExc(oidMatCurrency);
					} 
                                        catch (DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} 
                                        catch (Exception exc)
                                        { 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidMatCurrency != 0) 
                                {
					try 
                                        {
						matCurrency = PstMatCurrency.fetchExc(oidMatCurrency);
					} 
                                        catch (DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} 
                                        catch (Exception exc)
                                        { 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidMatCurrency != 0)
                                {
					try
                                        {
						long oid = PstMatCurrency.deleteExc(oidMatCurrency);
						if(oid!=0)
                                                {
							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
							excCode = RSLT_OK;
						}
                                                else
                                                {
							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
					}
                                        catch(DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}
                                        catch(Exception exc)
                                        {	
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			default :

		}
		return rsCode;
	}
}
