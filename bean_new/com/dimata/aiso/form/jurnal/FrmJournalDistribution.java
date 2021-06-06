/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.jurnal;

import com.dimata.aiso.entity.jurnal.JournalDistribution;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmJournalDistribution extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_JOURNAL_DISTRIBUTION = "FRM_JOURNAL_DISTRIBUTION";      
   
    public static final int FRM_BUSS_CENTER_ID = 0;
    public static final int FRM_DEBIT_AMOUNT = 1;
    public static final int FRM_CREDIT_AMOUNT = 2;
    public static final int FRM_NOTE = 3;
    public static final int FRM_CURRENCY_ID = 4;
    public static final int FRM_TRANS_RATE = 5;
    public static final int FRM_ID_PERKIRAAN = 6;
    public static final int FRM_PERIODE_ID = 7;
    public static final int FRM_STANDARD_RATE = 8;
    public static final int FRM_ARAP_MAIN_ID = 9;
    public static final int FRM_ARAP_PAYMENT_ID = 10;
    public static final int FRM_JOURNAL_DETAIL_ID = 11;   
    public static final int FRM_JDIS_INDEX=12;
    public static final int FRM_JDIS_OID=13;
    public static final int FRM_COA_CODE=14;
    public static final int FRM_COA_NAME=15;
    

    public static String[] fieldNames =
            {
                "FRM_BUSS_CENTER_ID",
                "FRM_DEBIT_AMOUNT",
                "FRM_CREDIT_AMOUNT",
                "FRM_NOTE",
                "FRM_CURRENCY_ID",
                "FRM_TRANS_RATE",
                "FRM_ID_PERKIRAAN",
                "FRM_PERIODE_ID",
                "FRM_STANDARD_RATE",
                "FRM_ARAP_MAIN_ID",
                "FRM_ARAP_PAYMENT_ID",
                "FRM_JOURNAL_DETAIL_ID",
                "FRM_JDIS_INDEX",
                "FRM_JDIS_OID",
                "FRM_COA_CODE",
                "FRM_COA_NAME"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG + ENTRY_REQUIRED,  //0
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_STRING,
                TYPE_LONG, //4
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_LONG, //9
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_STRING
            };

    private JournalDistribution objJournalDistribution;

    public FrmJournalDistribution(JournalDistribution objJournalDistribution) {
        this.objJournalDistribution = objJournalDistribution;
    }

    public FrmJournalDistribution(HttpServletRequest request, JournalDistribution objJournalDistribution) {
        super(new FrmJournalDistribution(objJournalDistribution), request);
        this.objJournalDistribution = objJournalDistribution;
    }

    public String getFormName() {
        return FRM_JOURNAL_DISTRIBUTION;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public JournalDistribution getEntityObject() {
        return objJournalDistribution;
    }

    public void requestEntityObject(JournalDistribution objJournalDistribution) {
        try {
            this.requestParam();
            objJournalDistribution.setBussCenterId(this.getLong(FRM_BUSS_CENTER_ID));
            objJournalDistribution.setDebitAmount(this.getDouble(FRM_DEBIT_AMOUNT));
            objJournalDistribution.setCreditAmount(this.getDouble(FRM_CREDIT_AMOUNT));
            objJournalDistribution.setNote(this.getString(FRM_NOTE));
            objJournalDistribution.setCurrencyId(this.getLong(FRM_CURRENCY_ID));
            objJournalDistribution.setTransRate(this.getDouble(FRM_TRANS_RATE));
            objJournalDistribution.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objJournalDistribution.setPeriodeId(this.getLong(FRM_PERIODE_ID));
            objJournalDistribution.setStandardRate(this.getDouble(FRM_STANDARD_RATE));
            objJournalDistribution.setArapMainId(this.getLong(FRM_ARAP_MAIN_ID));
            objJournalDistribution.setArapPaymentId(this.getLong(FRM_ARAP_PAYMENT_ID));
            objJournalDistribution.setJournalDetailId(this.getLong(FRM_JOURNAL_DETAIL_ID));
            
            this.objJournalDistribution = objJournalDistribution;
        } catch (Exception e) {
            objJournalDistribution = new JournalDistribution();
        }
    }
}
