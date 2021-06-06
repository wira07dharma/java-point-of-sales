package com.dimata.posbo.form.warehouse;

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
import com.dimata.posbo.entity.warehouse.*;

public class CtrlMatDistributionDetail extends Control implements I_Language {
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
    private MatDistributionDetail matDistributionDetail;
    private PstMatDistributionDetail pstMatDistributionDetail;
    private FrmMatDistributionDetail frmMatDistributionDetail;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMatDistributionDetail() {
    }
    
    public CtrlMatDistributionDetail(HttpServletRequest request) {
        msgString = "";
        matDistributionDetail = new MatDistributionDetail();
        try {
            pstMatDistributionDetail = new PstMatDistributionDetail(0);
        }
        catch(Exception e){}
        frmMatDistributionDetail = new FrmMatDistributionDetail(request, matDistributionDetail);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmMatDistributionDetail.addError(frmMatDistributionDetail.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public MatDistributionDetail getMatDistributionDetail() { return matDistributionDetail; }
    
    public FrmMatDistributionDetail getForm() { return frmMatDistributionDetail; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    
    /**
     *
     * @param cmd
     * @param <CODE>oidMatDistributionDetail</CODE>
     * @param oidMatDistribution
     * @return
     */
    synchronized public int action(int cmd , long oidMatDistributionDetail, long oidMatDistribution) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                double qtymax = 0;
                if(oidMatDistributionDetail != 0) {
                    try {
                        matDistributionDetail = PstMatDistributionDetail.fetchExc(oidMatDistributionDetail);
                        qtymax = matDistributionDetail.getQty();
                    }
                    catch(Exception exc) {
                    }
                }
                
                frmMatDistributionDetail.requestEntityObject(matDistributionDetail);
                matDistributionDetail.setDispatchMaterialId(oidMatDistribution);
                
                if(frmMatDistributionDetail.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                /**
                 * ini di pakai untuk mencari objeck dispatch
                 * dan di pakai untuk mencari object receive item
                 * untuk mengetahui nilai sisa yang sebenarnya.
                 */
                MatDistribution matDistribution = new MatDistribution();
                try{
                    matDistribution = PstMatDistribution.fetchExc(oidMatDistribution);
                }catch(Exception e){}
                MatReceiveItem matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matDistribution.getInvoiceSupplier(), 0,matDistributionDetail.getMaterialId());
                
                qtymax = qtymax + matReceiveItem.getResidueQty();
                System.out.println("===>>> SISA QTY : "+qtymax);
                
                // jika quantity yg akan didispatch melebihi maximum yg ada, maka return error
                if(matDistributionDetail.getQty() > qtymax){
                    frmMatDistributionDetail.addError(0,"");
                    msgString = "maksimal qty adalah ="+qtymax;
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                /**
                 * jika quantity yg akan didispctch memenuhi syarat, maka proses seperti biasa
                 * INSERT atau UPDATE sesuai dengan nilai OID dispstch
                 */
                if(matDistributionDetail.getOID()==0) {
                    try {
                        long oid = pstMatDistributionDetail.insertExc(this.matDistributionDetail);
                    }
                    catch(DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                else {
                    try {
                        long oid = pstMatDistributionDetail.updateExc(this.matDistributionDetail);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                
                /**
                 * Jika proses INSERT atau UPDATE berhasil (no exception), maka lakukan proses
                 * peng-update-an quantity sisa di receive berdasarkan NO INVOICE yg ada
                 */
                matReceiveItem.setResidueQty(qtymax - matDistributionDetail.getQty());
                try{
                    PstMatReceiveItem.updateExc(matReceiveItem);
                }catch(Exception e){}
                
                // update transfer status
                PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId());
                
                break;
                
            case Command.EDIT :
                if (oidMatDistributionDetail != 0) {
                    try {
                        matDistributionDetail = PstMatDistributionDetail.fetchExc(oidMatDistributionDetail);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidMatDistributionDetail != 0) {
                    try {
                        matDistributionDetail = PstMatDistributionDetail.fetchExc(oidMatDistributionDetail);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidMatDistributionDetail != 0) {
                    /**
                     * Proses peng-update-an quantity sisa di dokumen receive kalau proses
                     * delete pada dokumen dispatch ini berhasil
                     */
                    MatDistributionDetail matDistributionDetail = new MatDistributionDetail();
                    try{
                        matDistributionDetail = PstMatDistributionDetail.fetchExc(oidMatDistributionDetail);
                    }catch(Exception e){
                        System.out.println("Err when fetch matDistributionDetail " + e.toString());
                    }
                    
                    qtymax = matDistributionDetail.getQty();
                    
                    matDistribution = new MatDistribution();
                    try{
                        matDistribution = PstMatDistribution.fetchExc(oidMatDistribution);
                    }catch(Exception e){}
                    matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matDistribution.getInvoiceSupplier(), 0, matDistributionDetail.getMaterialId());
                    qtymax = qtymax + matReceiveItem.getResidueQty();
                    
                    try {
                        long oid = PstMatDistributionDetail.deleteExc(oidMatDistributionDetail);
                        if(oid!=0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }
                        else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch(DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch(Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                    // update receive item
                    matReceiveItem.setResidueQty(qtymax);
                    try{
                        PstMatReceiveItem.updateExc(matReceiveItem);
                    }catch(Exception e){}
                    
                    // update transfer status
                    PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId());
                }
                break;
                
            default :
                break;
        }
        return rsCode;
    }
}
