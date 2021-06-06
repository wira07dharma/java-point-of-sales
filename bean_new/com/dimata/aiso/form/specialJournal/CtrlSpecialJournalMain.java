/*
 * CtrlSpecialJournalMain.java
 *
 * Created on February 8, 2007, 4:15 PM
 */

package com.dimata.aiso.form.specialJournal;

/* import package java util */

import java.util.*;

/* import package javax servlet http */
import javax.servlet.http.HttpServletRequest;

/* import package dimata util lang */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package dimata qdep form */
import com.dimata.qdep.form.*;
import com.dimata.qdep.form.Control;

/* import package dimata qdep system */
import com.dimata.qdep.system.*;
import com.dimata.qdep.entity.I_DocStatus;

/* import package dimata aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.specialJournal.*;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.session.specialJournal.*;

/**
 * @author dwi
 */
public class CtrlSpecialJournalMain extends Control implements I_Language {

    public static int POSTED_JU = 0;
    public static int POSTED_JD = 1;

    public static int LENGTH_PREV = 4;
    public static int LENGTH_VOUCHER = 8;
    public static String STR_BLANK = "0";

    public static int RSLT_OK = 0;
    public static int RSLT_UPDATE = 1;
    public static int RSLT_UNKNOWN_ERROR = 2;
    public static int RSLT_ERR_TRANS_DATE = 3;
    public static int RSLT_ERR_VOUCHER = 4;
    public static int RSLT_ERR_VOUCHER_EXIST = 5;
    public static int RSLT_FORM_INCOMPLETE = 6;
    public static int RSLT_ERR_ENTRY_DATE = 7;
    public static int RSLT_ERR_CONTACT = 8;
    public static int RSLT_ERR_REFERENCE = 9;
    public static int RSLT_ERR_AMOUNT = 10;
    public static int RSLT_ERR_MEMO = 11;

    public static String[][] resultText =
            {
                    {"Jurnal tersimpan ...", "Jurnal terupdate ...", "Tidak dapat diproses ...", 
                     "Tanggal transaksi tidak sesuai ...", "No voucher tidak sesuai ...", "No voucher sudah ada ...", 
                     "Data tidak lengkap ...","Tanggal entry tidak sesuai",
                     "Error : Data Kontak Belum Diisi !","Error : No Dokumen Referensi Belum Diisi !",
                     "Error : Nilai Transaksi Belum Diisi !","Error : Memo Belum Diisi !"
                    },
                    {"Journal saved ...", "Journal updated ...", "Can not process ...", 
                     "Error : Transaction date invalid", "Error : Voucher no invalid ...", "Voucher no exist ...", 
                     "Data incomplete ...","Entry date invalid",
                     "Error : Contact is Required","Error : Doc Ref is Required",
                     "Error : Amount is Required","Error : Memo is Required"
                    }
            };

    private int iErrCode = 0;
    private String msgString;
    private SpecialJournalMain objSpecialJournalMain;
    private PstSpecialJournalMain pstSpecialJournalMain;
    private PstSpecialJournalDetail pstSpecialJournalDetail;
    private FrmSpecialJournalMain frmSpecialJournalMain;
    private CtrlSpecialJournalDetail ctrlSpecialJournalDetail;
    private int language = LANGUAGE_DEFAULT;
    private String msgBalance;

    public CtrlSpecialJournalMain() {
    }

    public CtrlSpecialJournalMain(HttpServletRequest request) {
        msgString = "";
        objSpecialJournalMain = new SpecialJournalMain();
        try {
            pstSpecialJournalMain = new PstSpecialJournalMain(0);
        } catch (Exception e) {
        }
        frmSpecialJournalMain = new FrmSpecialJournalMain(request, objSpecialJournalMain);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return resultText[language][RSLT_ERR_VOUCHER_EXIST];
        }
        return resultText[language][RSLT_OK];
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_ERR_VOUCHER_EXIST;
        }
        return RSLT_OK;
    }

    public SpecialJournalMain getSpecialJournalMain() {
        return objSpecialJournalMain;
    }

    public FrmSpecialJournalMain getForm() {
        return frmSpecialJournalMain;
    }

    public String getMessage() {
        return msgString;
    }

    public int getErrCode() {
        return iErrCode;
    }

    public String getMsgBalance() {
        return msgBalance;
    }

    public int action(int cmd, long specialJournalMainId, long userId) {
        long periodeId = 0;
        long oid = 0;
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        switch (cmd) {
            case Command.ADD:
                objSpecialJournalMain = new SpecialJournalMain();
                objSpecialJournalMain.setVoucherNumber("");
                objSpecialJournalMain.setDescription("");
                break;

            case Command.SAVE:
                if (specialJournalMainId != 0) {
                    
                    try {                        
                        objSpecialJournalMain = PstSpecialJournalMain.fetchExc(specialJournalMainId);                        
                        frmSpecialJournalMain.requestEntityObject(objSpecialJournalMain); 
                        
                        System.out.println("objSpecialJournalMain.getStandarRate() SAVE ===> "+objSpecialJournalMain.getStandarRate());
                        objSpecialJournalMain.setAmount(objSpecialJournalMain.getAmount() * objSpecialJournalMain.getStandarRate());
                        
                        PstSpecialJournalMain.updateExc(objSpecialJournalMain);
                        objSpecialJournalMain = PstSpecialJournalMain.fetchExc(specialJournalMainId);
                    } catch (Exception e) {
                        System.out.println("specialJournalMain >>>>>>>>>>>> : "+e.toString());
                    }
                }
                break;
                
            case Command.POST:
                frmSpecialJournalMain.requestEntityObject(objSpecialJournalMain);
                 System.out.println("objSpecialJournalMain.getStandarRate() POST ===> "+objSpecialJournalMain.getStandarRate());
                if (objSpecialJournalMain.getEntryDate() == null) {
                    objSpecialJournalMain.setEntryDate(new Date());
                }

               /*if (frmSpecialJournalMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if(objSpecialJournalMain.getDescription().length()==0){
                    msgString = resultText[language][RSLT_ERR_CONTACT];
                    return RSLT_ERR_CONTACT;
                }
                
                if(objSpecialJournalMain.getReference().length()==0){
                    msgString = resultText[language][RSLT_ERR_REFERENCE];
                    return RSLT_ERR_REFERENCE;
                }
                
                if(objSpecialJournalMain.getContactId() == 0){
                    msgString = resultText[language][RSLT_ERR_AMOUNT];
                    return RSLT_ERR_AMOUNT;
                }
                
                 if(objSpecialJournalMain.getAmount() == 0){
                   msgString = resultText[language][RSLT_ERR_MEMO];
                    return RSLT_ERR_MEMO;
                }*/

                if (objSpecialJournalMain.getDescription().length()==0 &&
                    objSpecialJournalMain.getReference().length()==0 &&
                    objSpecialJournalMain.getContactId() == 0 &&
                    objSpecialJournalMain.getAmount() == 0
                ) {                    
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }             
                
                if(objSpecialJournalMain.getDescription().length()==0){
                    frmSpecialJournalMain.removeError(FrmSpecialJournalMain.FRM_DESCRIPTION);
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_DESCRIPTION,resultText[language][RSLT_ERR_MEMO]);
                    //return RSLT_ERR_MEMO;
                }
                
                if(objSpecialJournalMain.getReference().length()==0){
                    frmSpecialJournalMain.removeError(FrmSpecialJournalMain.FRM_REFERENCE);
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_REFERENCE,resultText[language][RSLT_ERR_REFERENCE]);                    
                    //return RSLT_ERR_REFERENCE;
                }
                
                if(objSpecialJournalMain.getContactId() ==  0 ){
                    frmSpecialJournalMain.removeError(FrmSpecialJournalMain.FRM_CONTACT_ID);
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_CONTACT_ID,resultText[language][RSLT_ERR_CONTACT]);
                    //return RSLT_ERR_CONTACT;
                }
                
                if(objSpecialJournalMain.getFrcontactId() == 0 ){
                    frmSpecialJournalMain.removeError(FrmSpecialJournalMain.FRM_FR_CONTACT_ID);
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_FR_CONTACT_ID,resultText[language][RSLT_ERR_CONTACT]);
                    //return RSLT_ERR_CONTACT;
                }
                
                 if(objSpecialJournalMain.getAmount() == 0 ){
                     msgString = resultText[language][RSLT_ERR_AMOUNT];
                    /*frmSpecialJournalMain.removeError(FrmSpecialJournalMain.FRM_AMOUNT);
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_AMOUNT,resultText[language][RSLT_ERR_AMOUNT]);*/
                    return RSLT_ERR_AMOUNT;
                }


                // get current period
                String wClause = PstPeriode.fieldNames[PstPeriode.FLD_POSTED] + " = " + PstPeriode.PERIOD_OPEN;
                Vector sessperiode = PstPeriode.list(0, 0, wClause, "");
                System.out.println("==>>>>>> : " + sessperiode);
                Periode periode = new Periode();
                if (sessperiode != null && sessperiode.size() > 0) {
                    periode = (Periode) sessperiode.get(0);
                }
                
                 if((objSpecialJournalMain.getTransDate().after(periode.getTglAkhir())) || (objSpecialJournalMain.getTransDate().before(periode.getTglAwal()))){
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_TRANS_DATE,resultText[language][RSLT_ERR_TRANS_DATE]);
                    return RSLT_ERR_TRANS_DATE;
                }

                if(objSpecialJournalMain.getEntryDate().after(periode.getTglAkhirEntry())){
                    frmSpecialJournalMain.addError(FrmSpecialJournalMain.FRM_ENTRY_DATE,resultText[language][RSLT_ERR_ENTRY_DATE]);
                    return RSLT_ERR_ENTRY_DATE;
                }
                
                
                
                String strVoucher = SessSpecialJurnal.generateVoucherNumber(periode.getOID(), objSpecialJournalMain.getEntryDate());
                int iConter = SessSpecialJurnal.getLastCounter(periode.getOID());
                objSpecialJournalMain.setOID(specialJournalMainId);
                objSpecialJournalMain.setUserId(userId);
                objSpecialJournalMain.setPeriodeId(periodeId);
                objSpecialJournalMain.setEntryDate(new Date());
                objSpecialJournalMain.setVoucherNumber(strVoucher);
                objSpecialJournalMain.setVoucherCounter(iConter + 1);

                break;
               
            case Command.EDIT:
                
                if (specialJournalMainId != 0) {
                    try {
                        
                        objSpecialJournalMain = (SpecialJournalMain) pstSpecialJournalMain.fetchExc(specialJournalMainId);
                       
                        long jurDetailId = 0;
                        try {                           
                            // get detail data from database depend on jurnalumumId and set it to object jurnalumum
                            String whereClauseJurDetail = PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] + " = " + specialJournalMainId;
                            String orderBy = PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + " DESC";
                            
                           
                            Vector vectjurnaldetail = PstSpecialJournalDetail.list(0, 0, whereClauseJurDetail, orderBy);
                            int size = vectjurnaldetail.size();
                            for (int i = 0; i < size; i++) {
                                Vector temp = new Vector();
                                SpecialJournalDetail objSpecialJournalDetail = (SpecialJournalDetail) vectjurnaldetail.get(i);
                                jurDetailId = objSpecialJournalDetail.getOID();
                                temp.add(objSpecialJournalDetail);
                                try {
                                    String whereClauseActivity = PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID] + " = " + jurDetailId;
                                    long jDetailAssingtId = 0;
                                    Vector vectJDetailAssignt = PstSpecialJournalDetailAssignt.list(0, 0, whereClauseActivity, orderBy);
                                    for (int k = 0; k < vectJDetailAssignt.size(); k++) {                                       
                                        SpecialJournalDetailAssignt objSpecialJournalDetailAssignt = (SpecialJournalDetailAssignt) vectJDetailAssignt.get(k);
                                        jDetailAssingtId = objSpecialJournalDetailAssignt.getOID();
                                        temp.add(objSpecialJournalDetailAssignt);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Exception on edit activity ===> " + e.toString());
                                }
                                objSpecialJournalMain.addActivity(temp);
                            }
                        } catch (Exception e) {
                            System.out.println("Exc when edit journal : " + e.toString());
                        }
                        // objSpecialJournalMain.indexSyncronize(objSpecialJournalMain.getJurnalDetails());
                        
                    } catch (DBException dbexc) {
                        System.out.println("Exception on edit =============================> "+dbexc.toString());
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {                        
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.UPDATE:
                if (specialJournalMainId != 0) {
                    try {
                        objSpecialJournalMain = PstSpecialJournalMain.fetchExc(specialJournalMainId);
                        objSpecialJournalMain.setJournalStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        PstSpecialJournalMain.updateExc(objSpecialJournalMain);
                    } catch (Exception e) {
                    }
                }
                break;
            case Command.DELETE:
                if (specialJournalMainId != 0) {
                    try {
                        objSpecialJournalMain = PstSpecialJournalMain.fetchExc(specialJournalMainId);
                    } catch (Exception e) {
                    }
                }

            default:

        }
        return rsCode;
    }

    public static void journalDelete(long specialJournalMainId) {
        long oid = 0;
        if (specialJournalMainId != 0) {
            try {
                oid = PstSpecialJournalDetail.deleteByJurnalIDExc(specialJournalMainId);
                if (oid != 0) {
                    PstSpecialJournalMain pstSpecialJournalMain = new PstSpecialJournalMain();
                    oid = pstSpecialJournalMain.deleteExc(specialJournalMainId);
                }
            } catch (Exception e) {
                System.out.println("CtrlJurnalUmum.journalDelete.deleteByJurnalIDExc() err : " + e.toString());
            }
        }
    }

    public void JournalSave(SpecialJournalMain objSpecialJournalMain, Vector listDetail) {
        try {
            if(objSpecialJournalMain.getPeriodeId()==0){
                String wClause = PstPeriode.fieldNames[PstPeriode.FLD_POSTED] + " = " + PstPeriode.PERIOD_OPEN;
                Vector sessperiode = PstPeriode.list(0, 0, wClause, "");
                System.out.println("==>>>>>> : " + sessperiode);
                Periode periode = new Periode();
                if (sessperiode != null && sessperiode.size() > 0) {
                    periode = (Periode) sessperiode.get(0);
                }
                objSpecialJournalMain.setPeriodeId(periode.getOID());
            }
            
            long oidMain = 0;
            if(objSpecialJournalMain.getOID()==0)
                oidMain = PstSpecialJournalMain.insertExc(objSpecialJournalMain);
            else{
                oidMain = PstSpecialJournalMain.updateExc(objSpecialJournalMain);
                oidMain = objSpecialJournalMain.getOID();
            }
            
            PstSpecialJournalDetail.deleteByJurnalIDExc(oidMain);
            
            for (int k = 0; k < listDetail.size(); k++) {
                Vector temp = (Vector) listDetail.get(k);

                // journal detail
                SpecialJournalDetail specJournalDetail = (SpecialJournalDetail) temp.get(0); // if ada
                specJournalDetail.setJournalMainId(oidMain);
                
                long oidItem = 0;
                oidItem = PstSpecialJournalDetail.insertExc(specJournalDetail);
                
                // aktivity
                SpecialJournalDetailAssignt specialJournalDetailAssignt = (SpecialJournalDetailAssignt) temp.get(1);
                if (specialJournalDetailAssignt.getActivityId() != 0) {
                    specialJournalDetailAssignt.setJournalDetailId(oidItem);
                    PstSpecialJournalDetailAssignt.insertExc(specialJournalDetailAssignt);
                }
            }
        } catch (Exception e) {
        }
    }

}
