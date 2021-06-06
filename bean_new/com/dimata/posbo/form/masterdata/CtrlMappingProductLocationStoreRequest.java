
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author Witar
 */

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CtrlMappingProductLocationStoreRequest extends Control implements I_Language{
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText ={
        {"Berhasil", "Tidak dapat diproses", "Kode Mapping Product sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Mapping Product Code already exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private MappingProductLocationStoreRequest mappingProductLocationStoreRequest;
    private PstMappingProductLocationStoreRequest  pstMappingProductLocationStoreRequest;
    private FrmMappingProductLocationStoreRequest frmMappingProductLocationStoreRequest;
    int language = LANGUAGE_FOREIGN;
    
    public CtrlMappingProductLocationStoreRequest(HttpServletRequest request) {
        msgString = "";
        mappingProductLocationStoreRequest = new MappingProductLocationStoreRequest();
        try {
            pstMappingProductLocationStoreRequest = new PstMappingProductLocationStoreRequest(0);
        } catch (Exception e) {
            ;
        }
        frmMappingProductLocationStoreRequest = new FrmMappingProductLocationStoreRequest(request, mappingProductLocationStoreRequest);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) { 
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMappingProductLocationStoreRequest.addError(frmMappingProductLocationStoreRequest.FRM_FIELD_POS_LOCATION_MAPPING_STORE_REQUEST_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MappingProductLocationStoreRequest getMappingProductLocationStoreRequest() {
        return mappingProductLocationStoreRequest;
    }

    public FrmMappingProductLocationStoreRequest getForm() {
        return frmMappingProductLocationStoreRequest;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidMapping) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMapping != 0) {
                    try {
                        mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);                      
                    } catch (Exception exc) {
                    }
                }

                frmMappingProductLocationStoreRequest.requestEntityObject(mappingProductLocationStoreRequest);

                if (frmMappingProductLocationStoreRequest.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (mappingProductLocationStoreRequest.getOID() == 0) {
                    try {
                        long oid = pstMappingProductLocationStoreRequest.insertExc(this.mappingProductLocationStoreRequest);
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
                        long oid = pstMappingProductLocationStoreRequest.updateExc(this.mappingProductLocationStoreRequest);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMapping != 0) {
                    try {
                        mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMapping != 0) {
                    try {
                        mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMapping != 0) {
                    try {
                        long oid = 0;  
                        oid =  PstMappingProductLocationStoreRequest.deleteExc(oid);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);
                            frmMappingProductLocationStoreRequest.addError(FrmMappingProductLocationStoreRequest.FRM_FIELD_POS_LOCATION_MAPPING_STORE_REQUEST_ID, "");
                            msgString = "Hapus data gagal,data masih digunakan oleh data lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
                
            case Command.SAVEALL :
                
            break;

            default:

        }
        return excCode;
    }
    
    public int action2(int cmd, long oidMapping,String values, long oidMainLocation) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMapping != 0) {
                    try {
                        mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);                      
                    } catch (Exception exc) {
                    }
                }

                frmMappingProductLocationStoreRequest.requestEntityObject(mappingProductLocationStoreRequest);

                if (frmMappingProductLocationStoreRequest.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (mappingProductLocationStoreRequest.getOID() == 0) {
                    try {
                        long oid = pstMappingProductLocationStoreRequest.insertExc(this.mappingProductLocationStoreRequest);
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
                        long oid = pstMappingProductLocationStoreRequest.updateExc(this.mappingProductLocationStoreRequest);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMapping != 0) {
                    try {
                        mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMapping != 0) {
                    try {
                        mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMapping != 0) {
                    try {
                        long oid = 0;  
                        oid =  PstMappingProductLocationStoreRequest.deleteExc(oid);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oidMapping);
                            frmMappingProductLocationStoreRequest.addError(FrmMappingProductLocationStoreRequest.FRM_FIELD_POS_LOCATION_MAPPING_STORE_REQUEST_ID, "");
                            msgString = "Hapus data gagal,data masih digunakan oleh data lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
                
            case Command.SAVEALL :
                Location entMainLocation = new Location();
                try {
                    entMainLocation = PstLocation.fetchExc(oidMainLocation);
                } catch (Exception e) {
                }
                
                //delete data sebelumnya, dimana lokasi memiliki company yang sama dengan main location
                int iError = PstMappingProductLocationStoreRequest.deleteMappingByCompany(entMainLocation.getCompanyId());
                
                if (iError==0){
                    //jika delete sudah berhasil maka dilanjutkan dengan proses insert data
                    //value yang ada parameter akan dipecah terlebih dahulu
                    String locationMaterial [] = values.split(",");
                    for (int i = 0; i<locationMaterial.length;i++){
                        String temp = locationMaterial[i];
                        //nilai temp akan dipecah lagi, sehingga di dapat id location dan id material
                        String tempValues[] = temp.split("-");
                        MappingProductLocationStoreRequest mappingProductLocationStoreRequest = new MappingProductLocationStoreRequest();
                        mappingProductLocationStoreRequest.setLocationId(Long.parseLong(tempValues[1]));
                        mappingProductLocationStoreRequest.setMaterialId(Long.parseLong(tempValues[0]));
                        try {
                            long oid = PstMappingProductLocationStoreRequest.insertExc(mappingProductLocationStoreRequest);
                            if (oid != 0) {
                                msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                excCode = RSLT_OK;
                            } else {
                                msgString = "Proses simpan gagal..."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                excCode = RSLT_FORM_INCOMPLETE;
                            }
                        } catch (DBException dbexc) {
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                        } catch (Exception exc) {
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                        
                    }
                }
            break;

            default:

        }
        return excCode;
    }
    
}
