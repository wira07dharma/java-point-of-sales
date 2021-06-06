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
import com.dimata.posbo.session.masterdata.SessCategory;

//integrasi dengan BO lain
import com.dimata.ObjLink.BOPos.CategoryLink;
import com.dimata.interfaces.BOPos.I_Category;
import com.dimata.system.entity.PstSystemProperty;

public class CtrlCategory extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_DELETE_RESTRICT = 4;

    // ini untuk membedakan material
    // untuk hanoman,material sendiri, atau yang lain
    public int DESIGN_MATERIAL_FOR = DESIGN_MATERIAL;
    public static int DESIGN_MATERIAL = 0;
    public static int DESIGN_HANOMAN = 1;


    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Kategori sudah ada", "Data tidak lengkap", "Kategori tidak bisa dihapus, masih dipakai modul lain ..."},
        {"Succes", "Can not process", "Category code already exist", "Data incomplete", "Cannot delete, category still used by another module"}
    };

    private int start;
    private String msgString;
    private Category category;
    private Category prevCategory;
    private PstCategory pstCategory;
    private FrmCategory frmCategory;
    int language = LANGUAGE_FOREIGN;

    public CtrlCategory(HttpServletRequest request) {
        msgString = "";
        category = new Category();
        try {
            pstCategory = new PstCategory(0);
        } catch (Exception e) {
            ;
        }
        frmCategory = new FrmCategory(request, category);
    }

    private String getSystemMessage(int msgCode) {
        // System.out.println("=========>msgCode : " + msgCode);
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
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

    public Category getCategory() {
        return category;
    }

    public FrmCategory getForm() {
        return frmCategory;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCategory) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        // proses men set program yang untk hanoman / material sendiri.
        try {
            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
        } catch (Exception e) {
        }
        // - selesai

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                String categOldName = "";
                if (oidCategory != 0) {
                    try {
                        category = PstCategory.fetchExc(oidCategory);
                        prevCategory = PstCategory.fetchExc(oidCategory);
                        categOldName = category.getName();
                    } catch (Exception exc) {
                    }
                }

                frmCategory.requestEntityObject(category);

                if (frmCategory.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (category.getOID() == 0) {
                    try {
                        
                        //cek nama dan kode apakah ada yg sama
                        boolean checkedCode = pstCategory.checkCategory(category.getCode(),1);
                        boolean checkedName = pstCategory.checkCategory(category.getName(),0);
                        
                        if(checkedCode==false && checkedName==false){
                            long oid = pstCategory.insertExc(this.category);
                        }else{
                            msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                            return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                        }
                       
                       /* if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            //INtegrasi POS
                            if (oid != 0) {
                                CategoryLink clink = new CategoryLink();
                                clink.setName(category.getName());
                                clink.setCategoryId(oid);

                                I_Category i_cat = (I_Category) Class.forName(I_Category.strClassNameHanoman).newInstance();
                                i_cat.insertCategory(clink);
                            }
                            // ----------
                        }*/

                    } catch (DBException dbexc) {
                        System.out.println("Masuk ke DBException");
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println("Masuk ke Exception");
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    
                    try {
                        
                        //boolean checkedCode = pstCategory.checkCategory(category.getCode(),1);
                        //boolean checkedName = pstCategory.checkCategory(category.getName(),0);
                        
                        if(prevCategory.getCode().equals(this.category.getCode())){
                            boolean checkedName = pstCategory.checkCategory(category.getName(),0);
                            int countMaterial = pstCategory.checkCategoryCount(category.getName(),0);
                            if(checkedName==false){
                                
                                long oid = pstCategory.updateExc(this.category);
                                
                            }else if(prevCategory.getCategoryId()!=this.category.getCategoryId()){
                                
                                long oid = pstCategory.updateExc(this.category);
                                
                            }else if(prevCategory.getCatParentId()!=this.category.getCatParentId()){
                                
                                long oid = pstCategory.updateExc(this.category);
                            }else if (checkedName==true && countMaterial<=1 ){
                                long oid = pstCategory.updateExc(this.category);
                            }else{    
                                
                                msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                            }
                        }else if(prevCategory.getName().equals(this.category.getName())){
                            
                            boolean checkedCode = pstCategory.checkCategory(category.getCode(),1);
                            
                            if(checkedCode==false){
                                
                                long oid = pstCategory.updateExc(this.category);
                                
                            }else if(prevCategory.getCategoryId()!=this.category.getCategoryId()){
                                
                                long oid = pstCategory.updateExc(this.category);
                                
                            }else if(prevCategory.getCatParentId()!=this.category.getCatParentId()){
                                
                                long oid = pstCategory.updateExc(this.category);
                                
                            }else{ 
                                
                                msgString = getSystemMessage(I_DBExceptionInfo.MULTIPLE_ID);
                                return getControlMsgId(I_DBExceptionInfo.MULTIPLE_ID);
                                
                            }
                        }else{
                            long oid = pstCategory.updateExc(this.category);
                        }
                        
                        //long oid = pstCategory.updateExc(this.category);

                        // ini untuk menggabungkan data material yang nama kategorinya di ubah
                        if (!categOldName.equals(category.getName())) {
                            Material material = new Material();
                            if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {

                                String where = PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + oidCategory;
                                Vector vlist = PstMaterial.list(0, 0, where, "");
                                if (vlist != null && vlist.size() > 0) {
                                    for (int k = 0; k < vlist.size(); k++) {
                                        Material mat = (Material) vlist.get(k);
                                        mat.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                                        String goodName = mat.getName();
                                        try {
                                            goodName = goodName.substring(goodName.indexOf(mat.getSeparate()) + 1, goodName.length());
                                            goodName = category.getName() + mat.getSeparate() + goodName;
                                            mat.setName(goodName);
                                            PstMaterial.updateExc(mat);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                        }
                        //======== END =======

                        /*if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            //INtegrasi POS
                            if (oid != 0) {
                                CategoryLink clink = new CategoryLink();
                                clink.setName(category.getName());
                                clink.setCategoryId(category.getOID());

                                I_Category i_cat = (I_Category) Class.forName(I_Category.strClassNameHanoman).newInstance();
                                i_cat.updateCategory(clink);
                            }
                            // ----------
                        }*/

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                break;

            case Command.EDIT:
                if (oidCategory != 0) {
                    try {
                        category = PstCategory.fetchExc(oidCategory);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCategory != 0) {
                    try {
                        category = PstCategory.fetchExc(oidCategory);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCategory != 0) {
                    try {
                        long oid = 0;
                        if(SessCategory.readyDataToDelete(oidCategory)){ //cek apakaha ada material lain yang menggunakan category tsb
                            if(SessCategory.readyDataToDeleteCheckInduk(oidCategory)){ //cek apakah material tsb adalah induk dari category lain
                                oid = PstCategory.deleteExc(oidCategory);
                            }
                        }
                        if (oid != 0) {
                            if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                                //INtegrasi POS
                                if (oidCategory != 0) {
                                    try {
                                        CategoryLink clink = new CategoryLink();
                                        clink.setCategoryId(oidCategory);

                                        I_Category i_cat = (I_Category) Class.forName(I_Category.strClassNameHanoman).newInstance();
                                        oidCategory = i_cat.deleteCategory(clink);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            category = PstCategory.fetchExc(oidCategory);
                            frmCategory.addError(FrmCategory.FRM_FIELD_CATEGORY_ID,"");
                            msgString = "Hapus data gagal,data masih digunakan oleh data lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
                }
                break;

            default :

        }
        return excCode;
    }
}
