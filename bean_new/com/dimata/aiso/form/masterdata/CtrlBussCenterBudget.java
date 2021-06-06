/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.jurnal.JournalDistribution;
import com.dimata.aiso.entity.jurnal.PstJournalDistribution;
import com.dimata.aiso.entity.masterdata.BussinessCenterBudget;
import com.dimata.aiso.entity.masterdata.PstBussCenterBudget;
import com.dimata.aiso.session.masterdata.SessDailyRate;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlBussCenterBudget extends Control implements I_Language{

    public static final int RSLT_OK = 0;
    public static final int RSLT_INCOMPLETE = 1;
    public static final int RSLT_EXIST = 2;
    public static final int RSLT_UNKNOWN = 3;
    public static String resultText[][] = {
        {"OK", "Form belum lengkap", "Data sudah ada", "Kesalahan unknown"},
        {"OK", "Form incomplete", "Data already exist", "Unknown Error"}
    };


    private int start;
    private String msgString;
    private BussinessCenterBudget objBussinessCenterBudget;
    private PstBussCenterBudget pstBussCenterBudget;
    private FrmBussCenterBudget frmBussCenterBudget;
    private int language = LANGUAGE_DEFAULT;

    public CtrlBussCenterBudget(HttpServletRequest request) {
        msgString = "";
        objBussinessCenterBudget = new BussinessCenterBudget();
        try {
            pstBussCenterBudget = new PstBussCenterBudget(0);
        } catch (Exception e) {
        }
        frmBussCenterBudget = new FrmBussCenterBudget(request, objBussinessCenterBudget);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public BussinessCenterBudget getBussCenterBgt() {
        return objBussinessCenterBudget;
    }

    public FrmBussCenterBudget getForm() {
        return frmBussCenterBudget;
    }

    public String getMessage() {
        return msgString;
    }

    public int action(int cmd, long Oid) {
        this.start = start;
        int result = RSLT_OK;
        msgString = "";
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if(Oid!=0){
                    try{
                        objBussinessCenterBudget = PstBussCenterBudget.fetchExc(Oid);
                    }catch(Exception e){}
                }
                frmBussCenterBudget.requestEntityObject(objBussinessCenterBudget);
                //aktiva.setOID();

                if (frmBussCenterBudget.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objBussinessCenterBudget.getOID() == 0) {
                    try {
                        long oid = PstBussCenterBudget.insertExc(this.objBussinessCenterBudget);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = pstBussCenterBudget.updateExc(this.objBussinessCenterBudget);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objBussinessCenterBudget = (BussinessCenterBudget) pstBussCenterBudget.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objBussinessCenterBudget = (BussinessCenterBudget) pstBussCenterBudget.fetchExc(Oid);                        
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstBussCenterBudget pstBussCenterBudget = new PstBussCenterBudget();
                    try {
                        long oid = pstBussCenterBudget.deleteExc(Oid);
                        this.start = 0;
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            default:

        }
        return result;
    }
    
    public static synchronized Vector listTransBCBgt(long arapPaymentId, long bussCntId){
        Vector vResult = new Vector();
        if(arapPaymentId != 0){
            try{
                Vector vListJDistribution = (Vector)listJDistribution(arapPaymentId,bussCntId);
                double dDebet = 0.0;
                double dKredit = 0.0;
                if(vListJDistribution.size() > 0){
                    long localCurrId = SessDailyRate.getIdLocalCurrency();
                    double frgTransRate = SessDailyRate.getDefaultCurrRate();
                    double frgStdRate = SessDailyRate.getDefaultFrgStdRate();
                    for(int i = 0; i < vListJDistribution.size(); i++){
                        JournalDistribution objJDist = (JournalDistribution)vListJDistribution.get(i);
                        BussinessCenterBudget objBCBgt = new BussinessCenterBudget();
                       
                        dDebet = objJDist.getDebitAmount();
                        dKredit = objJDist.getCreditAmount();
                        
                        objBCBgt.setIdPerkiraan(objJDist.getIdPerkiraan());
                        objBCBgt.setPeriodeId(objJDist.getPeriodeId());
                        objBCBgt.setBussCenterId(objJDist.getBussCenterId());
                        if(objJDist.getCurrencyId() == localCurrId){
                            objBCBgt.setCreditForeignStdRate(dKredit/frgStdRate);
                            objBCBgt.setCreditForeignTransRate(dKredit/frgTransRate);
                            objBCBgt.setCreditLocalStdRate(dKredit);
                            objBCBgt.setCreditLocalTransRate(dKredit);
                            objBCBgt.setDebitForeignStdRate(dDebet/frgStdRate);
                            objBCBgt.setDebitForeignTransRate(dDebet/frgTransRate);
                            objBCBgt.setDebitLocalStdRate(dDebet);
                            objBCBgt.setDebitLocalTransRate(dDebet);
                        }else{
                            objBCBgt.setCreditForeignStdRate(dKredit/objJDist.getStandardRate());
                            objBCBgt.setCreditForeignTransRate(dKredit/objJDist.getTransRate());
                            objBCBgt.setCreditLocalStdRate(dKredit);
                            objBCBgt.setCreditLocalTransRate(dKredit);
                            objBCBgt.setDebitForeignStdRate(dDebet/objJDist.getStandardRate());
                            objBCBgt.setDebitForeignTransRate(dDebet/objJDist.getTransRate());
                            objBCBgt.setDebitLocalStdRate(dDebet);
                            objBCBgt.setDebitLocalTransRate(dDebet);
                        }
                        vResult.add(objBCBgt);
                    }
                    
                    
                }
                
               
            }catch(Exception e){}
        }
        return vResult;
    }
    
    public static synchronized Vector listJDistribution(long arapPaymentId, long bussCntId){
        Vector vResult = new Vector();
        if(arapPaymentId != 0){
            try{
                String whClause = PstJournalDistribution.fieldNames[PstJournalDistribution.FLD_ARAP_PAYMENT_ID]+" = "+arapPaymentId+
                                  " AND "+PstJournalDistribution.fieldNames[PstJournalDistribution.FLD_BUSS_CENTER_ID]+" = "+bussCntId;
                        vResult = arapPaymentId==0 ? new Vector() : (Vector)PstJournalDistribution.list(0, 0, whClause, "");
            }catch(Exception e){}
        }
        return vResult;
    }
}
