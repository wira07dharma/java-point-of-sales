/* 
 * Ctrl Name  		:  CtrlPriceTypeMapping.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.posbo.form.masterdata;

/* java package */ 
import java.util.*; 
import javax.servlet.http.*; 
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */
//import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;

public class CtrlPriceTypeMapping extends Control implements I_Language {

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
	private PriceTypeMapping priceTypeMapping;
	private PstPriceTypeMapping pstPriceTypeMapping;
	private FrmPriceTypeMapping frmPriceTypeMapping;
	int language = LANGUAGE_DEFAULT;

    public CtrlPriceTypeMapping(HttpServletRequest request) {
		msgString = "";
		priceTypeMapping = new PriceTypeMapping();
        try {
			pstPriceTypeMapping = new PstPriceTypeMapping(0);
        } catch (Exception e) {;
        }
		frmPriceTypeMapping = new FrmPriceTypeMapping(request, priceTypeMapping);
	}

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPriceTypeMapping.addError(frmPriceTypeMapping.FRM_FIELD_PRICE_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
				return resultText[language][RSLT_EST_CODE_EXIST];
			default:
				return resultText[language][RSLT_UNKNOWN_ERROR]; 
		}
	}

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
				return RSLT_EST_CODE_EXIST;
			default:
				return RSLT_UNKNOWN_ERROR;
		}
	}

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public PriceTypeMapping getPriceTypeMapping() {
        return priceTypeMapping;
    }

    public FrmPriceTypeMapping getForm() {
        return frmPriceTypeMapping;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    
    
    

    public int action(int cmd, long oidPriceTypeId, long oidMaterialId, long oidStandartRateId) {
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
				break;

            case Command.SAVE:
				
                                
				
                boolean checkOIDs = PstPriceTypeMapping.checkOID(oidPriceTypeId, oidMaterialId, oidStandartRateId);
                if (checkOIDs) {
                    try {
                        priceTypeMapping = PstPriceTypeMapping.fetchExc(oidPriceTypeId, oidMaterialId, oidStandartRateId);
                    } catch (Exception exc) {
					 }
				}                                        
                                
                                frmPriceTypeMapping.requestEntityObject(priceTypeMapping);
                if (oidPriceTypeId != 0 && oidMaterialId != 0 && oidStandartRateId != 0) {
                                    priceTypeMapping.setPriceTypeId(oidPriceTypeId);
                                    priceTypeMapping.setMaterialId(oidMaterialId);
                                    priceTypeMapping.setStandartRateId(oidStandartRateId);
                                }

                if (frmPriceTypeMapping.errorSize() > 0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
				}

                if (checkOIDs == false) {
                    try {
						long oid = pstPriceTypeMapping.insertExc(this.priceTypeMapping);
                        if (oid != 0) {
							msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
							excCode = RSLT_OK;
                        } else {
							msgString = FRMMessage.getMessage(FRMMessage.ERR_SAVED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
                    } catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
						return getControlMsgId(excCode);
                    } catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

                } else {
					try {
						long oid = pstPriceTypeMapping.updateExc(this.priceTypeMapping);
                        if (oid != 0) {
							msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
							excCode = RSLT_OK;
                        } else {
							msgString = FRMMessage.getMessage(FRMMessage.ERR_SAVED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
                    } catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

            case Command.EDIT:
				try {
                    priceTypeMapping = PstPriceTypeMapping.fetchExc(oidPriceTypeId, oidMaterialId, oidStandartRateId);
                } catch (DBException dbexc) {
					excCode = dbexc.getErrorCode();
					msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
					msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
				}
				break;

            case Command.ASK:
				try {
                    priceTypeMapping = PstPriceTypeMapping.fetchExc(oidPriceTypeId, oidMaterialId, oidStandartRateId);
                } catch (DBException dbexc) {
					excCode = dbexc.getErrorCode();
					msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
					msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
				}
				break;

            case Command.DELETE:
                try {
                    long oid = PstPriceTypeMapping.deleteExc(oidPriceTypeId, oidMaterialId, oidStandartRateId);
                } catch (DBException dbexc) {
					excCode = dbexc.getErrorCode();
					msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
					msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
				}
				break;

            default:

		}
		return rsCode;
	}

    public int action(int cmd,HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                //update price
                frmPriceTypeMapping.requestEntityObjectMultiple();
                Vector listHarga = frmPriceTypeMapping.getGetListHarga();
                if (listHarga != null && listHarga.size() > 0) {
                    for (int i = 0; i < listHarga.size(); i++) {
                        priceTypeMapping = (PriceTypeMapping) listHarga.get(i);
                            try {
                                if (priceTypeMapping.getPrevPrice() != priceTypeMapping.getPrice()) {
                                    
                                    boolean checkOID = PstPriceTypeMapping.checkOID(priceTypeMapping.getPriceTypeId(), priceTypeMapping.getMaterialId(), priceTypeMapping.getStandartRateId());
                                           
                                    if(checkOID){
                                        long oid = pstPriceTypeMapping.updateExc(this.priceTypeMapping);
                                    }else{
                                         PstPriceTypeMapping.insertExc(this.priceTypeMapping);
                                    }
                                }
                            } catch (Exception exc) {
                                System.out.println("erorr update prive mapping"+exc);
                            }
                    }
                }    
        }
        return rsCode;
    }
    
    
    
    
   
    
    
}
