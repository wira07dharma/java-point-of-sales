/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.jurnal;

import com.dimata.aiso.db.DBException;
import com.dimata.aiso.entity.jurnal.JournalDistribution;
import com.dimata.aiso.entity.jurnal.PstJournalDistribution;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.qdep.form.Control;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class CtrlJournalDistribution extends Control implements I_Language{

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
    private JournalDistribution objJournalDistribution;
    private PstJournalDistribution pstJournalDistribution;
    private FrmJournalDistribution frmJournalDistribution;
    private int language = LANGUAGE_DEFAULT;

    public CtrlJournalDistribution(HttpServletRequest request) {
        msgString = "";
        objJournalDistribution = new JournalDistribution();
        try {
            pstJournalDistribution = new PstJournalDistribution(0);
        } catch (Exception e) {
        }
        frmJournalDistribution = new FrmJournalDistribution(request, objJournalDistribution);
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public JournalDistribution getJournalDistribution() {
        return objJournalDistribution;
    }

    public FrmJournalDistribution getForm() {
        return frmJournalDistribution;
    }

    public String getMessage() {
        return msgString;
    }

    public int action(int cmd) {
        if(cmd==Command.SAVE || cmd==Command.ADD){
            frmJournalDistribution.requestEntityObject(objJournalDistribution);
            double dDebitAmount = objJournalDistribution.getDebitAmount() * objJournalDistribution.getTransRate();
            double dCreditAmount = objJournalDistribution.getCreditAmount() * objJournalDistribution.getTransRate();
            objJournalDistribution.setDebitAmount(dDebitAmount);
            objJournalDistribution.setCreditAmount(dCreditAmount);
        }
        return RSLT_OK;
    }
    
    public static int postedJournalDistribution(JournalDistribution jDistribution)
    {
        long oid = 0;
        switch(jDistribution.getDataStatus())
        {
            case PstJurnalDetail.DATASTATUS_CLEAN :                
                break;
                
            case PstJurnalDetail.DATASTATUS_ADD :                
                try
                {   
			oid = PstJournalDistribution.insertExc(jDistribution);
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJournalDistribution.postedJournalDistribution(jDistribution).DATASTATUS_ADD : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJournalDistribution.postedJournalDistribution(jDistribution).DATASTATUS_ADD : " + exc.toString());
                }
                break;
                
            case PstJurnalDetail.DATASTATUS_UPDATE :                
                try
                {   
			oid = PstJournalDistribution.updateExc(jDistribution);
		    
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJournalDistribution.postedJournalDistribution(jDistribution).DATASTATUS_UPDATE : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJournalDistribution.postedJournalDistribution(jDistribution).DATASTATUS_UPDATE : " + exc.toString());
                }
                break;
                
            case PstJurnalDetail.DATASTATUS_DELETE :                
                try
                {
			oid = PstJournalDistribution.deleteExc(jDistribution.getOID());
                }
                catch(DBException dbexc)
                {
                    
                    System.out.println("DBException on CtrlJournalDistribution.postedJournalDistribution(jDistribution).DATASTATUS_DELETE : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJournalDistribution.postedJournalDistribution(jDistribution).DATASTATUS_DELETE : " + exc.toString());
                }
                break;
        }
        return 0;
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
                        objJournalDistribution = PstJournalDistribution.fetchExc(Oid);
                    }catch(Exception e){}
                }
                frmJournalDistribution.requestEntityObject(objJournalDistribution);
                //aktiva.setOID();

                if (frmJournalDistribution.errorSize() > 0) {
                    msgString = resultText[language][RSLT_INCOMPLETE];
                    return RSLT_INCOMPLETE;
                }

                if (objJournalDistribution.getOID() == 0) {
                    try {
                        long oid = PstJournalDistribution.insertExc(this.objJournalDistribution);
                        long lPosting = CtrlJurnalUmum.postingBussCenterBgt(this.objJournalDistribution.getArapPaymentId(),this.objJournalDistribution.getBussCenterId());
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                } else {
                    try {
                        long oid = PstJournalDistribution.updateExc(this.objJournalDistribution);
                        long lPosting = CtrlJurnalUmum.postingBussCenterBgt(this.objJournalDistribution.getArapPaymentId(),this.objJournalDistribution.getBussCenterId());
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objJournalDistribution = (JournalDistribution) PstJournalDistribution.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objJournalDistribution = (JournalDistribution) PstJournalDistribution.fetchExc(Oid);
                    } catch (Exception exc) {
                        msgString = resultText[language][RSLT_UNKNOWN];
                        return RSLT_UNKNOWN;
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    PstJournalDistribution PstJournalDistribution = new PstJournalDistribution();
                    try {
                        long oid = PstJournalDistribution.deleteExc(Oid);
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
}
