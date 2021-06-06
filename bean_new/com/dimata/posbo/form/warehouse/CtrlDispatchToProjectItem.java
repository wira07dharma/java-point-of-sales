/**
 * User: wardana
 * Date: Mar 24, 2004
 * Time: 8:31:00 AM
 * Version: 1.0
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;

import javax.servlet.http.HttpServletRequest;

public class CtrlDispatchToProjectItem extends Control implements I_Language {
    public static final int RSLT_OK = 0;
    public static final int RSLT_UNKNOWN_ERROR = 1;
    public static final int RSLT_MATERIAL_EXIST = 2;
    public static final int RSLT_FORM_INCOMPLETE = 3;
    public static final int RSLT_QTY_NULL = 4;
    public static final int RSLT_QTY_MAX = 5;

    public static String[][] stResultText = {
        {"Berhasil", "Tidak dapat diproses", "Barang sudah ada", "Data tidak lengkap", "Jumlah barang tidak boleh nol ...", "Jumlah barang Maksimum adalah : "},
        {"Succes", "Can not process", "Material already exist ...", "Data incomplete", "Quantity may not zero ...", "Maximum quantity is : "}
    };

    private int iStart;
    private String stMsgString;
    private DispatchToProjectItem objDspToProjectItem;
    private PstDispatchToProjectItem objPstDspToProjectItem;
    private FrmDispatchToProjectItem objFrmDspToProjectItem;
    int language = LANGUAGE_DEFAULT;
    long lOid = 0;

    public CtrlDispatchToProjectItem() {
    }

    public CtrlDispatchToProjectItem(HttpServletRequest request) {
        stMsgString = "";
        objDspToProjectItem = new DispatchToProjectItem();
        try {
            objPstDspToProjectItem = new PstDispatchToProjectItem(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        objFrmDspToProjectItem = new FrmDispatchToProjectItem(request, objDspToProjectItem);
    }

    private String getSystemMessage(int iMsgCode) {
        switch (iMsgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.objFrmDspToProjectItem.addError(FrmDispatchToProject.FRM_FLD_DISPATCH_MATERIAL_ID, stResultText[language][RSLT_MATERIAL_EXIST]);
                return stResultText[language][RSLT_MATERIAL_EXIST];
            default:
                return stResultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int iMsgCode) {
        switch (iMsgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_MATERIAL_EXIST;
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

    public DispatchToProjectItem getDispatchToProjectItem() {
        return objDspToProjectItem;
    }

    public FrmDispatchToProjectItem getForm() {
        return objFrmDspToProjectItem;
    }

    public String getMessage() {
        return stMsgString;
    }

    public int getStart() {
        return iStart;
    }

    public long getOidTransfer() {
        return lOid;
    }

    public int action(int iCmd, long lOidDspToPrjItem, long lOidDspToPjr) {
        int iRsCode = RSLT_OK;
        switch (iCmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                iRsCode = actionSave(lOidDspToPrjItem, lOidDspToPjr);
                break;
            case Command.EDIT:
                iRsCode = actionEditOrAsk(lOidDspToPrjItem);
                break;
            case Command.ASK:
                iRsCode = actionEditOrAsk(lOidDspToPrjItem);
                break;
            case Command.DELETE:
                iRsCode = actionDelete(lOidDspToPrjItem, lOidDspToPjr);
                break;
            default:
                break;
        }

        return iRsCode;
    }

    private int actionSave(long lOidDspToPrjItem, long lOidDspToPjr) {
        stMsgString = "";
        int iExcCode = I_DBExceptionInfo.NO_EXCEPTION;
        double iMaxQty = 0;
        if (lOidDspToPrjItem != 0) {
            try {
                objDspToProjectItem = PstDispatchToProjectItem.fetchExc(lOidDspToPrjItem);

                // Add max quantity from current quantity on database (if Item exist)
                iMaxQty += objDspToProjectItem.getiQty();
            } catch (Exception e) {
                iExcCode = I_DBExceptionInfo.UNKNOWN;
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
            }
        }

        objFrmDspToProjectItem.requestEntityObject(objDspToProjectItem);
        objDspToProjectItem.setlDispatchMaterialId(lOidDspToPjr);

        if (objFrmDspToProjectItem.errorSize() > 0) {
            stMsgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
            return RSLT_FORM_INCOMPLETE;
        }

        // cheking if Quantity value is zero
        if (objDspToProjectItem.getiQty() == 0) {
            stMsgString = getSystemMessage(RSLT_QTY_NULL);
            return getControlMsgId(RSLT_QTY_NULL);
        }

        // Get residue quantity from recieved item
        // Add residue quantity to max quantity
        // checking item quantity is not bigger then max quantity.
        DispatchToProject objDspToProject = new DispatchToProject();
        try {
            objDspToProject = PstDispatchToProject.fetchExc(lOidDspToPjr);
        } catch (DBException dbe) {
            iExcCode = dbe.getErrorCode();
            stMsgString = getSystemMessage(iExcCode);
        }
        MatReceiveItem objMatReceiveItem = PstMatReceiveItem.getObjectReceiveItem(objDspToProject.getStInvoiceSupplier(), 0, objDspToProjectItem.getlMaterialId());
        iMaxQty += objMatReceiveItem.getResidueQty();
        if (objDspToProjectItem.getiQty() > iMaxQty) {
            iExcCode = RSLT_QTY_MAX;
            stMsgString = getSystemMessage(RSLT_QTY_MAX);
            return getControlMsgId(RSLT_QTY_MAX);
        }

        // process save or update item
        if (objDspToProjectItem.getOID() == 0) {
            try {
                this.lOid = PstDispatchToProjectItem.insertExc(objDspToProjectItem);
            } catch (DBException dbexc) {
                iExcCode = dbexc.getErrorCode();
                stMsgString = getSystemMessage(iExcCode);
            } catch (Exception e) {
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
            }
        } else {
            try {
                this.lOid = PstDispatchToProjectItem.updateExc(objDspToProjectItem);
            } catch (DBException dbexc) {
                iExcCode = dbexc.getErrorCode();
                stMsgString = getSystemMessage(iExcCode);
            } catch (Exception e) {
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
            }
        }

        // process update residue quantity on Received Item
        objMatReceiveItem.setResidueQty(iMaxQty - objDspToProjectItem.getiQty());
        try {
            PstMatReceiveItem.updateExc(objMatReceiveItem);
        } catch (DBException dbexc) {
            iExcCode = dbexc.getErrorCode();
            stMsgString = getSystemMessage(iExcCode);
        } catch (Exception e) {
            stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
        }
        PstMatReceive.processUpdate(objMatReceiveItem.getReceiveMaterialId());

        return iExcCode;
    }

    private int actionEditOrAsk(long lOidDspToPrjItem) {
        stMsgString = "";
        int iExcCode = I_DBExceptionInfo.NO_EXCEPTION;
        if (lOidDspToPrjItem != 0) {
            try {
                objDspToProjectItem = PstDispatchToProjectItem.fetchExc(lOidDspToPrjItem);
                iExcCode = RSLT_OK;
            } catch (DBException dbe) {
                iExcCode = dbe.getErrorCode();
                stMsgString = getSystemMessage(iExcCode);
            } catch (Exception e) {
                iExcCode = I_DBExceptionInfo.UNKNOWN;
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
            }

        }
        return iExcCode;
    }

    private int actionDelete(long lOidDspToPrjItem, long lOidDspToPjr) {
        stMsgString = "";
        int iExcCode = I_DBExceptionInfo.NO_EXCEPTION;
        double iMaxQty = 0;

        if (lOidDspToPrjItem != 0) {
            try {
                objDspToProjectItem = PstDispatchToProjectItem.fetchExc(lOidDspToPrjItem);
                iMaxQty += objDspToProjectItem.getiQty();

                DispatchToProject objDspToProject = new DispatchToProject();
                objDspToProject = PstDispatchToProject.fetchExc(lOidDspToPjr);

                MatReceiveItem objMatReceiveItem = PstMatReceiveItem.
                        getObjectReceiveItem(objDspToProject.getStInvoiceSupplier(),0, objDspToProjectItem.getlMaterialId());
                iMaxQty += objMatReceiveItem.getResidueQty();

                lOidDspToPrjItem = PstDispatchToProjectItem.deleteExc(lOidDspToPrjItem);

                objMatReceiveItem.setResidueQty(iMaxQty);
                PstMatReceiveItem.updateExc(objMatReceiveItem);
                PstMatReceive.processUpdate(objMatReceiveItem.getReceiveMaterialId());

                iExcCode = RSLT_OK;
            } catch (DBException dbe) {
                iExcCode = dbe.getErrorCode();
                stMsgString = getSystemMessage(iExcCode);
            } catch (Exception e) {
                iExcCode = I_DBExceptionInfo.UNKNOWN;
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
            }
        }
        return iExcCode;
    }
}
