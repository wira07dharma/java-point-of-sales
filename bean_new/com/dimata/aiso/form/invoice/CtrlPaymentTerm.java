/*
 * CtrlPaymentTerm.java
 *
 * Created on November 13, 2007, 5:11 PM
 */

package com.dimata.aiso.form.invoice;

/**
 *
 * @author  dwi
 */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// import dimata
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

// import qdep
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.Control;

// import aiso
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.invoice.*;
import com.dimata.aiso.entity.jurnal.*;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.session.jurnal.*;

public class CtrlPaymentTerm extends Control implements I_Language{
    
    public static int RSLT_OK = 0;
    public static int RSLT_UPDATE = 1;
    public static int RSLT_UNKNOWN_ERROR = 2;
    public static int RSLT_INVALID_TRANS_DATE = 3;
    public static int RSLT_VOUCHER_NUMBER_EXIST = 4;
    public static int RSLT_FORM_INCOMPLETE = 5;
    
    public static String[][] resultText = {
        {"Data Tersimpan","Data Terupdate","Proses Gagal","Tanggal transaksi tidak sesuai","Nomor sudah ada","Form tidak lengkap"},
        {"Data is saved","Data is updated","Process Failed","Transaction Data Invalid","Invoice Number is Exist","Form Incomplete"}
    };
    
    private int iErrorCode = 0;
    private String msgString;
    private PaymentTerm objPaymentTerm;
    private PstPaymentTerm pstPaymentTerm;
    private FrmPaymentTerm frmPaymentTerm;
    private int iLanguage = LANGUAGE_DEFAULT;
    
    /** Creates a new instance of CtrlPaymentTerm */
    public CtrlPaymentTerm() {
    }
    
    public CtrlPaymentTerm(HttpServletRequest request) {
        msgString = "";
        objPaymentTerm = new PaymentTerm();
        try{
            pstPaymentTerm = new PstPaymentTerm(0);
        }catch(Exception e){
            System.out.println("Exception on CtrlInvoiceMain() ::: "+e.toString());
        }
        frmPaymentTerm = new FrmPaymentTerm(request, objPaymentTerm);
    }
    
    public int getLanguage(){
        return iLanguage;
    }
    
    public void setLanguage(int iLanguage){
        this.iLanguage = iLanguage;
    }
    
    private String getSystemMassage(int msgCode){
        switch(msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID:
                return resultText[iLanguage][RSLT_VOUCHER_NUMBER_EXIST];
        }
        return resultText[iLanguage][RSLT_OK];
    }
    
    private int getControlMassageId(int msgCode){
        switch(msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_VOUCHER_NUMBER_EXIST;
        }
        return RSLT_OK;
    }
    
    public PaymentTerm getPaymentTerm(){
        return objPaymentTerm;
    }
    
    public FrmPaymentTerm getForm(){ 
        return frmPaymentTerm; 
    }
    
    public String getMassage(){
        return msgString;
    }
    
    public int getErrorCode(){
        return iErrorCode;
    }
    
    public int action(int iCommand, long lPaymentTermId){
        int iResult = RSLT_OK;
            switch(iCommand){
                case Command.ADD:
                break;
                case Command.SAVE:
                    frmPaymentTerm.requestEntityObject(objPaymentTerm);
                    objPaymentTerm.setOID(lPaymentTermId);
                    
                    if(frmPaymentTerm.errorSize() > 0){
                        msgString = resultText[iLanguage][RSLT_FORM_INCOMPLETE];
                        return RSLT_FORM_INCOMPLETE;
                    }
                    
                    //Check validated of transaction date
                    if(objPaymentTerm.getOID() == 0){
                        try{
                            long lOid = pstPaymentTerm.insertExc(this.objPaymentTerm);
                        }catch(Exception e){
                            System.out.println("Exception on CtrlInvoiceMain.insertExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }else{
                        try{
                            long lOid = pstPaymentTerm.updateExc(this.objPaymentTerm);
                        }catch(Exception e){
                            System.out.println("Exception on CtrlInvoiceMain.updateExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.EDIT:
                    if(lPaymentTermId != 0){
                        try{
                            objPaymentTerm = pstPaymentTerm.fetchExc(lPaymentTermId);
                        }catch(Exception e){
                            System.out.println("Exception on Edit CtrlInvoiceMain.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.ASK:
                    if(lPaymentTermId != 0){
                        try{
                            objPaymentTerm = pstPaymentTerm.fetchExc(lPaymentTermId);
                        }catch(Exception e){
                            System.out.println("Exception on Ask CtrlInvoiceMain.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.DELETE:
                    if(lPaymentTermId != 0){
                        PstInvoiceAdjMain pstInvoiceAdjMain = new PstInvoiceAdjMain();
                        try{
                            long lOid = pstPaymentTerm.deleteExc(lPaymentTermId);
                        }catch(Exception e){
                            System.out.println("Exception on CtrlInvoiceMain.deleteExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                default:
                break;    
            }
        return iResult;
    }
}
