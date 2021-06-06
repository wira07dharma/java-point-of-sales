/*
 * I_JournalType.java
 *
 * Created on August 25, 2005, 10:30 AM
 */

package com.dimata.interfaces.journal;

/**
 *
 * @author  gedhy
 */
public interface I_JournalType 
{    
 
    public static final int TIPE_JURNAL_UMUM                   = 0; 
    public static final int TIPE_JURNAL_PENUTUP_1              = 1; 
    public static final int TIPE_JURNAL_PENUTUP_2              = 2; 
    public static final int TIPE_JURNAL_PENUTUP_3              = 3;
    public static final int TIPE_SPECIAL_JOURNAL_CASH          = 10; /* set special journal starting from 10 , cause 0 to 9 reserve for regular journal for source of journal */
    public static final int TIPE_SPECIAL_JOURNAL_BANK          = 11;
    public static final int TIPE_SPECIAL_JOURNAL_PETTY_CASH    = 12;
    public static final int TIPE_SPECIAL_JOURNAL_REPLACEMENT   = 13;
    public static final int TIPE_SPECIAL_JOURNAL_BANK_TRANSFER = 14;
    public static final int TIPE_SPECIAL_JOURNAL_NON_CASH      = 15;
    public static final int TIPE_SPECIAL_JOURNAL_FUND          = 16;
    public static final int TIPE_SPECIAL_JOURNAL_PAYMENT       = 17;
    
    public static String[][] arrJournalTypeNames = 
    {
    	{
            "Jurnal Umum",
            "Jurnal Penutup 1", // meng-nol-khan "revenue" dan "expense" => "Laba(Rugi) Per Berjalan"
            "Jurnal Penutup 2", // meng-nol-khan "Laba(Rugi) Per Berjalan" => "Laba(Rugi) Thn Berjalan"
            "Jurnal Penutup 3",  // meng-nol-khan "Laba(Rugi) Thn Berjalan" => "Laba Ditahan"
            "Jurnal Khusus Kas", //Add by DWI 2007 02 06
            "Jurnal Khusus Bank", //Add by DWI 2007 02 06
            "Jurnal Khusus Kas Kecil", //Add by DWI 2007 02 06
            "Jurnal Khusus Pengisian Kas Kecil", //Add by DWI 2007 02 26
            "Jurnal Khusus Transfer Bank", //Add by DWI 2007 02 26
            "Jurnal Khusus Non Kas", //Add by DWI 2007 02 28
            "Jurnal Khusus Pendanaan", //Add by DWI 2007 03 06
            "Jurnal Khusus Pembayaran", //Add by DWI 2007 04 03
        },    	
        {
            "General Journal",
            "Closing Journal 1", // meng-nol-khan "revenue" dan "expense" => "Laba(Rugi) Per Berjalan"
            "Closing Journal 2", // meng-nol-khan "Laba(Rugi) Per Berjalan" => "Laba(Rugi) Thn Berjalan"
            "Closing Journal 3", // meng-nol-khan "Laba(Rugi) Thn Berjalan" => "Laba Ditahan"
            "Special Journal Cash", //Add by DWI 2007 02 06
            "Special Journal Bank", //Add by DWI 2007 02 06
            "Special Journal Petty Cash", //Add by DWI 2007 02 06
            "Special Journal Petty Cash Replacement", //Add by DWI 2007 02 26
            "Special Journal Petty Bank Transfer", //Add by DWI 2007 02 26
            "Special Journal Petty Non Cash", //Add by DWI 2007 02 26
            "Special Journal Funding", //Add by DWI 2007 03 06
            "Special Journal Payment", //Add by DWI 2007 04 03
        }
        
    }; 
    
}
