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

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessMerk;

public class CtrlMerk extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material Merk sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material Merk Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private Merk merk;
    private Merk prevmerk;
    private PstMerk pstMatMerk;
    private FrmMerk frmMerk;
    int language = LANGUAGE_FOREIGN;

    public CtrlMerk(HttpServletRequest request) {
        msgString = "";
        merk = new Merk();
        try {
            pstMatMerk = new PstMerk(0);
        } catch (Exception e) {
            ;
        }
        frmMerk = new FrmMerk(request, merk);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMerk.addError(frmMerk.FRM_FIELD_MERK_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Merk getMerk() {
        return merk;
    }

    public FrmMerk getForm() {
        return frmMerk;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMerk) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String merkOldName = "";
                if (oidMerk != 0) {
                    try {
                        merk = PstMerk.fetchExc(oidMerk);
                        prevmerk = PstMerk.fetchExc(oidMerk);
                        merkOldName = merk.getName();
                    } catch (Exception exc) {
                    }
                }

                frmMerk.requestEntityObject(merk);

                if (frmMerk.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String whereClause = "( " + PstMerk.fieldNames[PstMerk.FLD_NAME] +
                        " = '" + merk.getName() +
                        "') AND " + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] +
                        " <> " + merk.getOID();
                Vector isExist = PstMerk.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }


                if (merk.getOID() == 0) {
                    try {
                        //cek nama dan kode apakah ada yg sama
                        boolean checkedCode = PstMerk.checkMerk(merk.getCode(),1);
                        boolean checkedName = PstMerk.checkMerk(merk.getName(),0);
                        
                        if(checkedCode==false && checkedName==false){
                            long oid = pstMatMerk.insertExc(this.merk);
                        }else{
                            msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                            return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
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
                        
                        if(prevmerk.getCode().equals(this.merk.getCode())){
                            boolean checkedName = pstMatMerk.checkMerk(merk.getName(),0);
                            if(checkedName==false){
                               long oid = pstMatMerk.updateExc(this.merk);
                            }else{
                                msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                            }
                        }else if(prevmerk.getName().equals(this.merk.getName())){
                            boolean checkedCode = pstMatMerk.checkMerk(merk.getCode(),1);
                            if(checkedCode==false){
                                long oid = pstMatMerk.updateExc(this.merk);
                            }else{
                                msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                            }
                        }else{
                            long oid = pstMatMerk.updateExc(this.merk);
                        }
                        
                        // ini untuk menggabungkan data material yang nama kategorinya di ubah
                        if (!merkOldName.equals(merk.getName())) {
                            Material material = new Material();
                              //hide opie-eyek 20160812
//                            if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
//
//                                String where = PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + oidMerk;
//                                Vector vlist = PstMaterial.list(0, 0, where, "");
//                                if (vlist != null && vlist.size() > 0) {
//                                    for (int k = 0; k < vlist.size(); k++) {
//                                        Material mat = (Material) vlist.get(k);
//                                        mat.setProses(Material.IS_PROCESS_INSERT_UPDATE);
//                                        String goodName = mat.getName();
//                                        try {
//                                            String category = goodName.substring(0, goodName.indexOf(mat.getSeparate()));
//                                            String matName = goodName.substring(goodName.lastIndexOf(mat.getSeparate()) + 1, goodName.length());
//                                            goodName = category + mat.getSeparate() + merk.getName() + mat.getSeparate() + matName;
//                                            mat.setName(goodName);
//                                            PstMaterial.updateExc(mat);
//                                        } catch (Exception e) {
//                                        }
//                                    }
//                                }
//                            }
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
                if (oidMerk != 0) {
                    try {
                        merk = PstMerk.fetchExc(oidMerk);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMerk != 0) {
                    try {
                        merk = PstMerk.fetchExc(oidMerk);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMerk != 0) {
                    try {
                        long oid = 0;
                        if(SessMerk.readyDataToDelete(oidMerk)){
                            oid = PstMerk.deleteExc(oidMerk);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            merk = PstMerk.fetchExc(oidMerk);
                            frmMerk.addError(FrmMerk.FRM_FIELD_MERK_ID,"");
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

            default :

        }
        return excCode;
    }
}
