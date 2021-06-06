/*
 * Ctrl Name  		:  CtrlVdrMaterialPrice.java
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

package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlMatVendorPrice extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_DELETE_RESTRICT = 4;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Vendor sudah ada", "Data tidak lengkap", "Price tidak bisa dihapus, masih dipakai modul lain ..."},
        {"Succes", "Can not process", "Vendor exist", "Data incomplete", "Cannot delete, vendor price still used by another module"}
    };

    private int start;
    private String msgString;
    private Material material;
    private MatVendorPrice matVendorPrice;
    private PstMatVendorPrice pstMatVendorPrice;
    private FrmMatVendorPrice frmMatVendorPrice;
    int language = LANGUAGE_DEFAULT;

    public CtrlMatVendorPrice(HttpServletRequest request) {
        msgString = "";
        matVendorPrice = new MatVendorPrice();
        try {
            pstMatVendorPrice = new PstMatVendorPrice(0);
        } catch (Exception e) {
            ;
        }
        frmMatVendorPrice = new FrmMatVendorPrice(request, matVendorPrice);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatVendorPrice.addError(FrmMatVendorPrice.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            case I_DBExceptionInfo.DEL_RESTRICTED:
                return resultText[language][RSLT_DELETE_RESTRICT];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            case I_DBExceptionInfo.DEL_RESTRICTED:
                return RSLT_DELETE_RESTRICT;
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

    public MatVendorPrice getMatVendorPrice() {
        return matVendorPrice;
    }

    public Material getMaterial() {
        return material;
    }

    public FrmMatVendorPrice getForm() {
        return frmMatVendorPrice;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidVendorPrice) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                try {
                    material = PstMaterial.fetchExc(oidVendorPrice);
                } catch (Exception e) {
                }
                break;

            case Command.SAVE:
                boolean checkOIDs = pstMatVendorPrice.checkOID(oidVendorPrice);
                if (checkOIDs) {
                    try {
                        matVendorPrice = pstMatVendorPrice.fetchExc(oidVendorPrice);
                    } catch (Exception exc) {
                        System.out.println(exc.toString());
                    }
                }

                frmMatVendorPrice.requestEntityObject(matVendorPrice);

                System.out.println("error size() : "+frmMatVendorPrice.errorSize());

                if (frmMatVendorPrice.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (pstMatVendorPrice.checkOIDs(matVendorPrice.getMaterialId(), matVendorPrice.getVendorId(), matVendorPrice.getPriceCurrency(), matVendorPrice.getBuyingUnitId(), oidVendorPrice)) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }

                if (oidVendorPrice == 0) {
                    try {
                        long oid = pstMatVendorPrice.insertExc(this.matVendorPrice);
                        if (oid != 0) {
                            //update masterdata agar menjadi default supplier
                            try{
                                Material material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
                                material.setSupplierId(matVendorPrice.getVendorId());
                                long oidMat = PstMaterial.updateExc(material);
                            }catch(Exception ex){
                            }
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
                        long oid = pstMatVendorPrice.updateExc(this.matVendorPrice);
                        if (oid != 0) {
                            try{
                                Material material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
                                material.setSupplierId(matVendorPrice.getVendorId());
                                long oidMat = PstMaterial.updateExc(material);
                            }catch(Exception ex){
                            }
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
                    matVendorPrice = PstMatVendorPrice.fetchExc(oidVendorPrice);
                    //material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            case Command.ASK:
                try {
                    matVendorPrice = PstMatVendorPrice.fetchExc(oidVendorPrice);
                    //material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            case Command.DELETE:
                try {
                    long oid = PstMatVendorPrice.deleteExc(oidVendorPrice);
                    //material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            default :

        }
        return rsCode;
    }
    
    public int actionInsertDefaultSupplier(int cmd, long oidVendorPrice, Material mat) {
        try {
            //cek
            if(oidVendorPrice!=0){
                if (pstMatVendorPrice.checkOIDsVendor(mat.getOID(),oidVendorPrice)) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return 0;
                }
            }
            
            MatVendorPrice matVendorPrice = new MatVendorPrice();
            matVendorPrice.setOID(0);
            matVendorPrice.setMaterialId(mat.getOID());
            matVendorPrice.setVendorId(oidVendorPrice);
            matVendorPrice.setBuyingUnitId(mat.getDefaultStockUnitId());
            matVendorPrice.setVendorPriceBarcode("");
            matVendorPrice.setVendorPriceCode("");
            matVendorPrice.setPriceCurrency(0);
            matVendorPrice.setLastDiscount(0);
            matVendorPrice.setLastVat(0);
            matVendorPrice.setCurrBuyingPrice(0);
            matVendorPrice.setOrgBuyingPrice(0);
            matVendorPrice.setDescription("");
            matVendorPrice.setLastCostCargo(0);
            matVendorPrice.setLastDiscount1(0);
            matVendorPrice.setLastDiscount2(0);
            long oid = pstMatVendorPrice.insertExc(matVendorPrice);
            
            if (oid != 0) {
                try{
                    Material material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
                    material.setSupplierId(matVendorPrice.getVendorId());
                    long oidMat = PstMaterial.updateExc(material);
                }catch(Exception ex){
                }
                msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
            } 
            
            
        } catch (DBException ex) {
            
        }
        return 0;
    }
}
