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
import com.dimata.posbo.session.masterdata.SessKsg;

public class CtrlKsg extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText =
            {
        {"Berhasil", "Tidak dapat diproses", "Code Material Ksg sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Ksg Code already exist", "Data incomplete"}};
    private int start;
    private String msgString;
    private Ksg ksg;
    private PstKsg pstMatKsg;
    private FrmKsg frmKsg;
    int language = LANGUAGE_FOREIGN;

    public CtrlKsg(HttpServletRequest request) {
        msgString = "";
        ksg = new Ksg();
        try {
            pstMatKsg = new PstKsg(0);
        } catch (Exception e) {
            ;
        }
        frmKsg = new FrmKsg(request, ksg);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) { 
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmKsg.addError(frmKsg.FRM_FIELD_KSG_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Ksg getKsg() {
        return ksg;
    }

    public FrmKsg getForm() {
        return frmKsg;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidKsg) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String ksgOldName = "";
                if (oidKsg != 0) {
                    try {
                        ksg = PstKsg.fetchExc(oidKsg);
                        ksgOldName = ksg.getName();
                    } catch (Exception exc) {
                    }
                }

                frmKsg.requestEntityObject(ksg);

                if (frmKsg.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String whereClause = "( " + PstKsg.fieldNames[PstKsg.FLD_NAME] +
                        " = '" + ksg.getName() +
                        "') AND " + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] +
                        " <> " + ksg.getOID();
                Vector isExist = PstKsg.list(0, 0, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }
 

                if (ksg.getOID() == 0) {
                    try {                                                
                        long oid = pstMatKsg.insertExc(this.ksg);
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
                        long oid = pstMatKsg.updateExc(this.ksg);

                        // ini untuk menggabungkan data material yang nama kategorinya di ubah
                        if (!ksgOldName.equals(ksg.getName())) {
                            Material material = new Material();
                            if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {

                                String where = PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + oidKsg;
                                Vector vlist = PstMaterial.list(0, 0, where, "");
                                if (vlist != null && vlist.size() > 0) {
                                    for (int k = 0; k < vlist.size(); k++) {
                                        Material mat = (Material) vlist.get(k);
                                        mat.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                                        String goodName = mat.getName();
                                        try {
                                            String category = goodName.substring(0, goodName.indexOf(mat.getSeparate()));
                                            String matName = goodName.substring(goodName.lastIndexOf(mat.getSeparate()) + 1, goodName.length());
                                            goodName = category + mat.getSeparate() + ksg.getName() + mat.getSeparate() + matName;
                                            mat.setName(goodName);
                                            PstMaterial.updateExc(mat);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                        }
                    //======== END =======

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidKsg != 0) {
                    try {
                        ksg = PstKsg.fetchExc(oidKsg);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidKsg != 0) {
                    try {
                        ksg = PstKsg.fetchExc(oidKsg);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidKsg != 0) {
                    try {
                        long oid = 0;
                        if (SessKsg.readyDataToDelete(oidKsg)) {
                            oid = PstKsg.deleteExc(oidKsg);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            ksg = PstKsg.fetchExc(oidKsg);
                            frmKsg.addError(FrmKsg.FRM_FIELD_KSG_ID, "");
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

            default:

        }
        return excCode;
    }
}
