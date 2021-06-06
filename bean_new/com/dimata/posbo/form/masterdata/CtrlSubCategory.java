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
//import com.dimata.hanoman.db.*;
import com.dimata.posbo.entity.masterdata.*;

public class CtrlSubCategory extends Control implements I_Language 
{
	public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = 
        {
		{"Berhasil", "Tidak dapat diproses", "Kode sub kategori sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Sub category code exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private SubCategory subCategory;
        private SubCategory prevSubCategory;
	private PstSubCategory pstSubCategory;
	private FrmSubCategory frmSubCategory;
	int language = LANGUAGE_DEFAULT;

	public CtrlSubCategory(HttpServletRequest request)
        {
		msgString = "";
		subCategory = new SubCategory();
		try
                {
			pstSubCategory = new PstSubCategory(0);
		}
                catch(Exception e){;}
		frmSubCategory = new FrmSubCategory(request, subCategory);
	}

	private String getSystemMessage(int msgCode)
        {
		switch (msgCode)
                {
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmSubCategory.addError(frmSubCategory.FRM_FIELD_SUB_CATEGORY_ID, resultText[language][RSLT_CODE_EXIST] );
				return resultText[language][RSLT_CODE_EXIST];
			default:
				return resultText[language][RSLT_UNKNOWN_ERROR]; 
		}
	}

	private int getControlMsgId(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				return RSLT_CODE_EXIST;
			default:
				return RSLT_UNKNOWN_ERROR;
		}
	}

	public int getLanguage(){ return language; }

	public void setLanguage(int language){ this.language = language; }

	public SubCategory getSubCategory() { return subCategory; }

	public FrmSubCategory getForm() { return frmSubCategory; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidSubCategory){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd)
                {
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidSubCategory != 0)
                                {
					try
                                        {
						subCategory = PstSubCategory.fetchExc(oidSubCategory);
                                                prevSubCategory = PstSubCategory.fetchExc(oidSubCategory);
					}
                                        catch(Exception exc)
                                        {
					}
				}

				frmSubCategory.requestEntityObject(subCategory);

				if(frmSubCategory.errorSize()>0) 
                                {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(subCategory.getOID()==0)
                                {
					try
                                        {
                                                
                                                boolean checkedCode = pstSubCategory.checkCategory(this.subCategory.getCode(),1);
                                                boolean checkedName = pstSubCategory.checkCategory(this.subCategory.getName(),0);

                                                if(checkedCode==false && checkedName==false){
                                                    long oid = pstSubCategory.insertExc(this.subCategory);
                                                }else{
                                                    msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                                    return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                                                }
                                            
						
					}
                                        catch(DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();                                                
						msgString = getSystemMessage(excCode);
                                                //System.out.println("==================> excCode : "+excCode);
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
                                                //boolean checkedCode = false;
                                                if(prevSubCategory.getCode().equals(this.subCategory.getCode()) && prevSubCategory.getName().equals(this.subCategory.getName())){
                                                    long oid = pstSubCategory.updateExc(this.subCategory);
                                                }else{
                                                    if(prevSubCategory.getCode().equals(this.subCategory.getCode())){
                                                        boolean checkedName = pstSubCategory.checkCategory(this.subCategory.getName(),0);
                                                        if(checkedName==false){
                                                            long oid = pstSubCategory.updateExc(this.subCategory);
                                                        }else{
                                                            msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                                            return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                                                        }
                                                    }else if(prevSubCategory.getName().equals(this.subCategory.getName())){
                                                        boolean checkedCode = pstSubCategory.checkCategory(this.subCategory.getCode(),1);
                                                        if(checkedCode==false){
                                                            long oid = pstSubCategory.updateExc(this.subCategory);
                                                        }else{
                                                            msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                                            return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                                                        }
                                                    }else{
                                                        long oid = pstSubCategory.updateExc(this.subCategory);
                                                    }
                                                }
					}
                                        catch (DBException dbexc)
                                        {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
                                                //System.out.println("==================> excCode : "+excCode);
					}
                                        catch (Exception exc)
                                        {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidSubCategory != 0) 
                                {
					try 
                                        {
						subCategory = PstSubCategory.fetchExc(oidSubCategory);
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
				if (oidSubCategory != 0) 
                                {
					try 
                                        {
						subCategory = PstSubCategory.fetchExc(oidSubCategory);
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
				if (oidSubCategory != 0)
                                {
					try
                                        {
						long oid = PstSubCategory.deleteExc(oidSubCategory);
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
