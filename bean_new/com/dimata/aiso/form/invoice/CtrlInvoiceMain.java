/*
 * CtrlInvoiceMain.java
 *
 * Created on November 12, 2007, 2:31 PM
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
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.Control;

// import aiso
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.invoice.*;
import com.dimata.aiso.entity.costing.*;
import com.dimata.aiso.entity.jurnal.*;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.session.invoice.*;
import com.dimata.aiso.session.costing.*;
import com.dimata.aiso.session.jurnal.*;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.PstDepartment;

public class CtrlInvoiceMain extends Control implements I_Language{
    
    public static int RSLT_OK = 0;
    public static int RSLT_UPDATE = 1;
    public static int RSLT_UNKNOWN_ERROR = 2;
    public static int RSLT_INVALID_TRANS_DATE = 3;
    public static int RSLT_VOUCHER_NUMBER_EXIST = 4;
    public static int RSLT_FORM_INCOMPLETE = 5;
    public static int RSLT_FIRST_CONTACT_ID = 6;
    
    public static String[][] resultText = {
        {"Data Tersimpan","Data Terupdate","Proses Gagal","Tanggal transaksi tidak sesuai","Nomor sudah ada","Form tidak lengkap","Customer belum dientry."},
        {"Data is saved","Data is updated","Process Failed","Transaction Date Invalid","Invoice Number is Exist","Form Incomplete","Contact is required"}
    };
    
    private int iErrorCode = 0;
    private String msgString;
    private InvoiceMain objInvoiceMain;
    private PstInvoiceMain pstInvoiceMain;
    private FrmInvoiceMain frmInvoiceMain;
    private int iLanguage = LANGUAGE_DEFAULT;
    
    /** Creates a new instance of CtrlInvoiceMain */
    public CtrlInvoiceMain() {
    }
    
    public CtrlInvoiceMain(HttpServletRequest request) {
        msgString = "";
        objInvoiceMain = new InvoiceMain();
        try{
            pstInvoiceMain = new PstInvoiceMain(0);
        }catch(Exception e){
            System.out.println("Exception on CtrlInvoiceMain() ::: "+e.toString());
        }
        frmInvoiceMain = new FrmInvoiceMain(request, objInvoiceMain);
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
    
    public InvoiceMain getInvoiceMain(){
        return objInvoiceMain;
    }
    
    public FrmInvoiceMain getForm(){ 
        return frmInvoiceMain; 
    }
    
    public String getMassage(){
        return msgString;
    }
    
    public int getErrorCode(){
        return iErrorCode;
    }
    
    public int action(int iCommand, long lInvoiceMainId, Vector vCosting, long userOID){
        int iResult = RSLT_OK;
            switch(iCommand){
                case Command.ADD:
                break;
                case Command.SAVE:
                    frmInvoiceMain.requestEntityObject(objInvoiceMain);
                    objInvoiceMain.setOID(lInvoiceMainId);                    
                    
                    //System.out.println("objInvoiceMain.getTermOfPayment() ::::::::: "+objInvoiceMain.getTermOfPayment());
                    if(frmInvoiceMain.errorSize() > 0){
                        msgString = resultText[iLanguage][RSLT_FORM_INCOMPLETE];
                        return RSLT_FORM_INCOMPLETE;
                    }
                    
                    //Check validated of transaction date
                    Date transDate = new Date();
                    if(objInvoiceMain.getIssuedDate() != null){
                        transDate = objInvoiceMain.getIssuedDate();
                    }
                    
                    Date entryDate = new Date();
                    Date startPeriodDate = new Date();
                    Date entPeriodDate = new Date();
                    Vector vPeriod = PstPeriode.getCurrPeriod();
                    long lPeriodId = 0;
                    if(vPeriod != null && vPeriod.size() > 0){
                        Periode objPeriod = (Periode)vPeriod.get(0);
                        lPeriodId = objPeriod.getOID();
                        startPeriodDate = objPeriod.getTglAwal();
                        entPeriodDate = objPeriod.getTglAkhir();
                    }
                                        
                    Date currDate = new Date();
                    if(currDate.after(entPeriodDate)){
                        if(SessJurnal.checkTransactionDate(transDate)){
                            msgString = resultText[iLanguage][RSLT_INVALID_TRANS_DATE];
                            return RSLT_INVALID_TRANS_DATE;
                        }
                    }else{
                         if(!(objInvoiceMain != null && SessJurnal.isValidTransactionDate(transDate,entryDate,startPeriodDate,entPeriodDate))){                       
                            msgString = resultText[iLanguage][RSLT_INVALID_TRANS_DATE];
                            return RSLT_INVALID_TRANS_DATE;                       
                        }
                    }    
                    
                    if(objInvoiceMain.getFirstContactId() == 0){
                         msgString = resultText[iLanguage][RSLT_FIRST_CONTACT_ID];
                        return RSLT_FIRST_CONTACT_ID;
                    }
                    
                     String strVoucherNumber = SessInvoice.genInvoiceNumber(transDate); 
                        String strPrefik = SessJurnal.generatePrefiksVoucher(transDate);
                        int iNumberCounter = 0;
                        if(transDate.after(entPeriodDate)){  
                            String strMonth = strPrefik.substring(2,4);
                            int iMonth = Integer.parseInt(strMonth);
                            int iNewMonth = transDate.getMonth() + 1;
                            if(iNewMonth == iMonth){                               
                                 iNumberCounter = SessInvoice.getLastCounter(strPrefik) + 1;                                 
                            }else{  
                                iNumberCounter = 1;                                
                            }                            
                        }else{
                                iNumberCounter = SessInvoice.getLastCounter(strPrefik) + 1;                                
                        }
                        
                        
                        
                    if(objInvoiceMain.getOID() == 0){                       
                        try{
			    objInvoiceMain.setInvoiceNumber(strVoucherNumber);
			    objInvoiceMain.setNumberCounter(iNumberCounter);
                            long lOid = pstInvoiceMain.insertExc(this.objInvoiceMain);
                        }catch(Exception e){                            
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }else{
                        try{
			    setInvoiceNumber(objInvoiceMain.getOID(), objInvoiceMain);			   
                            long lOid = pstInvoiceMain.updateExc(this.objInvoiceMain);
                        }catch(Exception e){                            
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.EDIT:
                    if(lInvoiceMainId != 0){
                        try{                            
                            objInvoiceMain = pstInvoiceMain.fetchExc(lInvoiceMainId);
                        }catch(Exception e){
                            System.out.println("Exception on Edit CtrlInvoiceMain.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.POST:
                    if(lInvoiceMainId != 0){
                        try{                            
                            objInvoiceMain = pstInvoiceMain.fetchExc(lInvoiceMainId);
                            long lCurrType = 0;
                            long bookType = 0;
                            long periodeOID = 0;
                            long lDepartmentId = 0;
                            bookType = Long.parseLong(PstSystemProperty.getValueByName("BOOK_TYPE"));
                            if(bookType != 0){
                                lCurrType = bookType;
                            }
                            periodeOID = PstPeriode.getCurrPeriodId();
                            Vector vDept = PstDepartment.list(0, 0, "", "");
                            
                            if(vDept != null && vDept.size() > 0){
                                for(int i = 0; i < vDept.size(); i++){
                                    Department objDepartment = new Department();
                                    objDepartment = (Department)vDept.get(i);
                                    if(objDepartment != null){
                                        lDepartmentId = objDepartment.getOID();
                                    }
                                }
                            }
                            
                            int iPostJournal = SessInvoice.postingJournalInvoice(bookType, userOID, periodeOID, objInvoiceMain, lCurrType, lDepartmentId); 
                            int iPostAr = 0;
                            if(iPostJournal == 1 && objInvoiceMain.getTermOfPayment() > 0){
                                iPostAr = SessInvoice.postingAccReceivable(objInvoiceMain, lCurrType); 
                            }else{
                                try{
                                    objInvoiceMain.setStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                    pstInvoiceMain.updateExc(objInvoiceMain);
                                }catch(Exception e){}
                            }
                            
                            int iPostJournalCosting = 0;
                            
                            if(iPostAr == 1){
                                 iPostJournalCosting = SessCosting.postJournalCosting(bookType, userOID, periodeOID, objInvoiceMain, lCurrType, lDepartmentId);
                                
                            }else{
                                if(iPostJournal == 1 && objInvoiceMain.getTermOfPayment() == 0){
                                    iPostJournalCosting = SessCosting.postJournalCosting(bookType, userOID, periodeOID, objInvoiceMain, lCurrType, lDepartmentId); 
                                }
                            }
                            
                            int iPostPayable = 0;
                            if(iPostJournalCosting == 1){
                                iPostPayable = SessCosting.postAccPayable(objInvoiceMain, lCurrType);
                            }
                        }catch(Exception e){
                            System.out.println("Exception on Edit CtrlInvoiceMain.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                 case Command.SUBMIT: 
                    if(lInvoiceMainId != 0){
                        try{
                            objInvoiceMain = pstInvoiceMain.fetchExc(lInvoiceMainId);
                            if(objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                objInvoiceMain.setStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
                                long lUpdate = pstInvoiceMain.updateExc(objInvoiceMain);
                            }
                            if(vCosting != null && vCosting.size() > 0){
                                Costing objCosting = new Costing();
                                for(int i = 0; i < vCosting.size(); i++){
                                    objCosting = (Costing)vCosting.get(i);
                                    if(objCosting.getStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                        objCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
                                        long lUpdateStsCosting = PstCosting.updateExc(objCosting);
                                    }
                                }
                            }
                        }catch(Exception e){
                            System.out.println("Exception on Edit CtrlInvoiceMain.UpdateStatus() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                 case Command.UPDATE: 
                    if(lInvoiceMainId != 0){
                        try{
                            objInvoiceMain = pstInvoiceMain.fetchExc(lInvoiceMainId);
                            if(objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                                objInvoiceMain.setStatus(I_DocStatus.DOCUMENT_STATUS_CANCELLED);
                                long lUpdate = pstInvoiceMain.updateExc(objInvoiceMain);
                            }
                            if(vCosting != null && vCosting.size() > 0){
                                Costing objCosting = new Costing();
                                for(int i = 0; i < vCosting.size(); i++){
                                    objCosting = (Costing)vCosting.get(i);
                                    if(objCosting.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                                        objCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_CANCELLED);
                                        long lUpdateStsCosting = PstCosting.updateExc(objCosting);
                                    }
                                }
                            }
                        }catch(Exception e){
                            System.out.println("Exception on Void CtrlInvoiceMain.UpdateStatus() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.ASK:
                    if(lInvoiceMainId != 0){
                        try{
                            objInvoiceMain = pstInvoiceMain.fetchExc(lInvoiceMainId);
                        }catch(Exception e){
                            System.out.println("Exception on Ask CtrlInvoiceMain.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.DELETE:
                    if(lInvoiceMainId != 0){
                        PstInvoiceMain pstInvMain = new PstInvoiceMain();
                        try{
                            long lOid = pstInvMain.deleteExc(lInvoiceMainId);
                            System.out.println("CtrlInvoiceMain.AFTER DELETE MAIN");
                            if(objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                System.out.println("CtrlInvoiceMain.BEFORE DELETE DETAIL");
                                SessInvoice.deleteAll(lInvoiceMainId);
                                System.out.println("CtrlInvoiceMain.AFTER DELETE DETAIL");
                            }
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
    
    public static synchronized void setInvoiceNumber(long lIdInvoiceMain, InvoiceMain objInvoiceMain){
	InvoiceMain invMain = new InvoiceMain();
	if(lIdInvoiceMain != 0){
	    try{
		invMain = PstInvoiceMain.fetchExc(lIdInvoiceMain);
	    }catch(Exception e){}
	}
	
	if(invMain != null){
	    objInvoiceMain.setInvoiceNumber(invMain.getInvoiceNumber());
	    objInvoiceMain.setNumberCounter(invMain.getNumberCounter());
	}
    }
    
    public static void main(String[] arg){
        Date transDate = new Date();
        String strPrefik = SessJurnal.generatePrefiksVoucher(transDate);
        System.out.println("strPrefik : "+strPrefik);
    }
    
    
}
