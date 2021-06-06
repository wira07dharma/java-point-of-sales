/*
 * I_ChartOfAccountGroup.java
 *
 * Created on August 2, 2005, 10:05 AM
 */

package com.dimata.interfaces.chartofaccount;

/**
 *
 * @author  gedhy
 */
public interface I_ChartOfAccountGroup {

    public static final int ACC_GROUP_ALL = 0;
    public static final int ACC_GROUP_CASH = 1;
    public static final int ACC_GROUP_BANK = 2;
    public static final int ACC_GROUP_LIQUID_ASSETS = 3;
    public static final int ACC_GROUP_FIXED_ASSETS = 4;
    public static final int ACC_GROUP_OTHER_ASSETS = 5;
    public static final int ACC_GROUP_CURRENCT_LIABILITIES = 6;
    public static final int ACC_GROUP_LONG_TERM_LIABILITIES = 7;
    public static final int ACC_GROUP_EQUITY = 8;
    public static final int ACC_GROUP_REVENUE = 9;
    public static final int ACC_GROUP_COST_OF_SALES = 10;
    public static final int ACC_GROUP_EXPENSE = 11;
    public static final int ACC_GROUP_OTHER_REVENUE = 12;
    public static final int ACC_GROUP_OTHER_EXPENSE = 13;
    public static final int ACC_GROUP_GROSS_PROFIT = 14;
    public static final int ACC_GROUP_INCOME_TAXES = 15;
    public static final int ACC_GROUP_PERIOD_EARNING = 16;
    public static final int ACC_GROUP_YEAR_EARNING = 17;
    public static final int ACC_GROUP_RETAINED_EARNING = 18;
    public static final int ACC_GROUP_AKU_PENYUSUTAN = 19;
    public static final int ACC_GROUP_BIAYA_PENYUSUTAN = 20;
    public static final int ACC_GROUP_PIUTANG = 21;
    public static final int ACC_GROUP_AKU_AMORTISASI = 22;
    public static final int ACC_GROUP_BIAYA_AMORTISASI = 23;  
    public static final int ACC_GROUP_PETTY_CASH = 24;
    public static final int ACC_GROUP_FUND = 25;    
    public static final int ACC_GROUP_INVENTORY = 26; 
    public static final int ACC_GROUP_INTEREST_EXPENSES = 27; 
    public static final int ACC_GROUP_TAXES = 28;         
    
    public static String[][] arrAccountGroupNames =
            {
                {
                    "Semua",
                    "Kas",
                    "Bank",
                    "Aktiva Lancar",
                    "Aktiva Tetap",
                    "Aktiva Lain-Lain",
                    "Hutang Jangka Pendek",
                    "Hutang Jangka Panjang",
                    "Modal",
                    "Pendapatan Operasional",
                    "Harga Pokok Penjualan",
                    "Biaya Operasional",
                    "Pendapatan Non Operasional",
                    "Biaya Non Operasional",
                    "Laba Kotor",
                    "Pph Badan",
                    "Laba Rugi Periode Berjalan",
                    "Laba Rugi Tahun Berjalan",
                    "Laba Ditahan",
                    "Akumulasi Penyusutan",
                    "Biaya penyusutan",
                    "Piutang",
                    "Akumulasi Amortisasi",
                    "Biaya Amortisasi",
                    "Kas Kecil",
                    "Dana",
                    "Persediaan",
                    "Biaya Bunga",
                    "Pajak-Pajak"
                },
                {
                    "All",
                    "Cash",
                    "Bank",
                    "Liquid Assets",
                    "Fixed Assets",
                    "Other Assets",
                    "Current Liabilities",
                    "Long Term Liabilities",
                    "Equity",
                    "Revenue",
                    "Cost Of Sales",
                    "Expense",
                    "Other Revenue",
                    "Other Expense",
                    "Gross Margin",
                    "Income Taxes",
                    "Period Earning",
                    "Yearly Earning",
                    "Retained Earning",
                    "Depreciation Accumulation",
                    "Depreciation Cost",
                    "Account Receivable",
                    "Amortization Accumulation",
                    "Amortization Cost",
                    "Petty Cash",
                    "Fund",
                    "Inventory",
                    "Interest Expenses",
                    "Taxes"
                }
            };

}
