/*
 * I_Arap.java
 *
 * Created on June 3, 2006, 11:39 PM
 */

package com.dimata.interfaces.arap;

import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public interface I_Arap {    

    // nama http session
    public static final String SESS_SEARCH_ARAP_BO_REPORT = "SESS_SEARCH_ARAP_BO_DETAIL_REPORT";
    public static final String SESS_SEARCH_ARAP_BO_DETAIL_REPORT = "SESS_SEARCH_ARAP_BO_DETAIL";
    
    // report type
    public static final int AR_REPORT_PER_DEBITOR = 0;
    public static final int AP_REPORT_PER_CREDITOR = 1;
    public static final int ARAP_REPORT_PER_DUE_DATE = 2;

    public static final String[][] stReportType = {
        {
            "Daftar Piutang per Debitur",
            "Daftar Hutang per Kreditur",
            "Hutang dan Piutang per Jatuh Tempo"
        },
        {
            "Receivable per Debitor Report",
            "Payable per Creditor Report",
            "Receivable and Payable per Due Date"
        }
    };
    
    // report type
    public static final int AR_INCREASE = 0;
    public static final int AP_INCREASE = 1;
    public static final int AR_PAYMENT = 2;
    public static final int AP_PAYMENT = 3;
    public static final int AR_DETAIL = 4;
    public static final int AP_DETAIL = 5;
    public static final int AR_TODAY_DUE_DATE = 6;
    public static final int AP_TODAY_DUE_DATE = 7;
    public static final int AR_TOMORROW_DUE_DATE = 8;
    public static final int AP_TOMORROW_DUE_DATE = 9;
    public static final int AR_AGING = 10;
    public static final int AP_AGING = 11;
    public static final int AR_DETAIL_ = 12;
    public static final int AP_DETAIL_ = 13;

    public static final int REPORT_VS_PAYMENT_MAPPING = 4;
    public static final int AR_PAYMENT_ENTRY = 0;
    public static final int AP_PAYMENT_ENTRY = 1;

    public static final String[][] stReportDetailType = {
        {
            "Daftar Penambahan Piutang",
            "Daftar Penambahan Hutang",
            "Daftar Pembayaran Piutang",
            "Daftar Pembayaran Hutang",
            "Daftar Arus Piutang",
            "Daftar Arus Hutang",
            "Daftar Piutang Jatuh Tempo Sekarang",
            "Daftar Hutang Jatuh Tempo Sekarang",
            "Daftar Piutang Jatuh Tempo Besok",
            "Daftar Hutang Jatuh Tempo Besok",
            "Daftar Umur Piutang",
            "Daftar Umur Hutang",
            "Daftar Piutang Detail",
            "Daftar Hutang Detail",
        },
        {
            "Receivable Increase Report",
            "Payable Increase Report",
            "Receivable Payment Report",
            "Payable Payment Report",
            "Receivable Movement Report",
            "Payable Movement Report",
            "Today Due Date Receivable Report",
            "Today Due Date Payable Report",
            "Tomorrow Due Date Receivable Report",
            "Tomorrow Due Date Payable Report",
            "Receivable Aging Report",
            "Payable Aging Report",
            "Receivable Detail Report",
            "Payable Detail Report",
        }
    };
    
    public static final String[][] stEntryPayment = {
        {
            "Entry Pembayaran Piutang",
            "Entry Pembayaran Hutang"
        },
        {
            "Receivable Payment Entry",
            "Payable Payment Entry"
        }

    };
    
    /**
     * this method used to get list AR or Ap Report Per Contact defend on 'SrcArapReport' object as parameter
     * @param <CODE>objSrcArapReport</CODE>object SrcArapReport
     * return 'vector of obj com.dimata.interfaces.arap.ArapPerContact'
     */
    public Vector getListArApPerContactReport(SrcArapReport objSrcArapReport);
    
    
    /**
     * this method used to get list AR or Ap Report per Due Date defend on 'SrcArapReport' object as parameter
     * @param <CODE>objSrcArapReport</CODE>object SrcArapReport
     * return 'vector of obj com.dimata.interfaces.arap.ArapPerDueDate'
     */
    public Vector getListArApPerDueDateReport(SrcArapReport objSrcArapReport);
    

    /**
     * this method used to get list AR or Ap Report defend on 'SrcArapReport' object as parameter
     * @param <CODE>objSrcArapReport</CODE>object SrcArapReport
     * return 'vector of obj com.dimata.interfaces.arap.ArapDetail'
     */
    public Vector getListArApReport(SrcArapReport objSrcArapReport);    


    /**
     * this method used to get list AR or Ap Payment Report defend on 'SrcArapReport' object as parameter
     * @param <CODE>objSrcArapReport</CODE>object SrcArapReport
     * return 'vector of obj com.dimata.interfaces.arap.ArapDetail'
     */
    public Vector getListArApPaymentReport(SrcArapReport objSrcArapReport);
    

    /**
     * this method used to get list AR or Ap Ageing Report defend on 'SrcArapReport' object as parameter
     * @param <CODE>objSrcArapReport</CODE>object SrcArapReport
     * return 'vector of obj com.dimata.interfaces.arap.ArapDetail'
     */
    public Vector getListArApAgeingReport(SrcArapReport objSrcArapReport);    
}
