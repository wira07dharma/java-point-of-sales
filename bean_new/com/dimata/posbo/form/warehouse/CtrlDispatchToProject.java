/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 4:25:56 PM
 * Version: 1.0
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.qdep.form.Control;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.util.DocCodeGenerator;
import com.dimata.posbo.entity.warehouse.DispatchToProject;
import com.dimata.posbo.entity.warehouse.PstDispatchToProject;
import com.dimata.posbo.entity.warehouse.PstDispatchToProjectItem;
import com.dimata.posbo.entity.warehouse.DispatchToProjectItem;
import com.dimata.posbo.session.warehouse.SessDispatchToProject;

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

public class CtrlDispatchToProject extends Control implements I_Language {

    public static final String className = I_DocType.DOCTYPE_CLASSNAME;

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] stResultText =
            {
                {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
            };

    private int iStart;
    private String stMsgString;
    private DispatchToProject objDspToProject;
    private PstDispatchToProject objPstDspToProject;
    private FrmDispatchToProject objFrmDspToProject;
    int language = LANGUAGE_DEFAULT;
    long lOid = 0;

    public CtrlDispatchToProject(HttpServletRequest request) {
        stMsgString = "";
        objDspToProject = new DispatchToProject();
        try {
            objPstDspToProject = new PstDispatchToProject(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        objFrmDspToProject = new FrmDispatchToProject(request, objDspToProject);
    }

    private String getSystemMessage(int iMsgCode) {
        switch (iMsgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.objFrmDspToProject.addError(FrmDispatchToProject.FRM_FLD_DISPATCH_MATERIAL_ID, stResultText[language][RSLT_EST_CODE_EXIST]);
                return stResultText[language][RSLT_EST_CODE_EXIST];
            default:
                return stResultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int iMsgCode) {
        switch (iMsgCode) {
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

    public DispatchToProject getDispatchToProject() {
        return objDspToProject;
    }

    public FrmDispatchToProject getForm() {
        return objFrmDspToProject;
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

    public int action(int iCmd, long lOidDspToPrj) {
        int iRsCode = RSLT_OK;
        switch (iCmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                iRsCode = actionSave(lOidDspToPrj);
                break;
            case Command.EDIT:
                iRsCode = actionEditOrAsk(lOidDspToPrj);
                break;
            case Command.ASK:
                iRsCode = actionEditOrAsk(lOidDspToPrj);
                break;
            case Command.DELETE:
                iRsCode = actionDelete(lOidDspToPrj);
                break;
            default:
                break;
        }

        return iRsCode;
    }

    private int actionSave(long lOidDspToPrj) {
        System.out.println(".........masuk save...........");
        stMsgString = "";
        int iExcCode = I_DBExceptionInfo.NO_EXCEPTION;
        if (lOidDspToPrj != 0) {
            try {
                objDspToProject = PstDispatchToProject.fetchExc(lOidDspToPrj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        objFrmDspToProject.requestEntityObject(objDspToProject);

        if (lOidDspToPrj == 0) {
            try {
                int iMaxConter = SessDispatchToProject.getMaxDispatchCounter(objDspToProject.getDtDispatchDate(), objDspToProject);
                int iDocType = -1;
                String stDocCode = "";
                I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
                iDocType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);
                stDocCode = DocCodeGenerator.getDocumentCode(iDocType, objDspToProject.getDtDispatchDate(), iMaxConter+1);
                objDspToProject.setiDispatchCodeCounter(iMaxConter+1);
                objDspToProject.setStDispatchCode(stDocCode);
                this.lOid = objPstDspToProject.insertExc(this.objDspToProject);
            } catch (DBException dbexc) {
                dbexc.printStackTrace();
                iExcCode = dbexc.getErrorCode();
                stMsgString = getSystemMessage(iExcCode);
            } catch (Exception e) {
                e.printStackTrace();
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
            }
        }
        else{
            try{
                this.lOid = objPstDspToProject.updateExc(this.objDspToProject);
            } catch (DBException dbexc) {
                dbexc.printStackTrace();
                iExcCode = dbexc.getErrorCode();
                stMsgString = getSystemMessage(iExcCode);
            } catch (Exception e) {
                e.printStackTrace();
                stMsgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
            }
        }
        return iExcCode;
    }

    private int actionEditOrAsk(long lOidDspToPrj) {
        stMsgString = "";
        int iExcCode = I_DBExceptionInfo.NO_EXCEPTION;
        if (lOidDspToPrj != 0) {
            try {
                objDspToProject = PstDispatchToProject.fetchExc(lOidDspToPrj);
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

    private int actionDelete(long lOidDspToPrj) {
        stMsgString = "";
        CtrlDispatchToProjectItem objCtlItem = new CtrlDispatchToProjectItem();
        Vector vListItem = new Vector();
        String stWhereClose = PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_DISPATCH_MATERIAL_ID]+" = "+lOidDspToPrj;
        DispatchToProjectItem objItem = new DispatchToProjectItem();
        int iExcCode = I_DBExceptionInfo.NO_EXCEPTION;
        if (lOidDspToPrj != 0) {
            try {
                vListItem = PstDispatchToProjectItem.list(0,0,stWhereClose,"");
                if(vListItem != null && vListItem.size() > 0){
                    for(int i=0; i < vListItem.size(); i++){
                        objItem = (DispatchToProjectItem) vListItem.get(i);
                        objCtlItem.action(Command.DELETE, objItem.getOID(), lOidDspToPrj);
                    }
                }
                lOidDspToPrj = PstDispatchToProject.deleteExt(lOidDspToPrj);
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
