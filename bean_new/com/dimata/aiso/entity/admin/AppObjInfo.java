/*
 * AppObjInfo.java
 *
 * Created on April 3, 2002, 4:09 PM
 */

package com.dimata.aiso.entity.admin;

import java.util.*;

/**
 *
 * @author  ktanjana
 * @version
 * @Purpose Describe application object as binary coded (integer)
 * @CodeMapping
 * bit 0 - 7   : command CMD
 * bit 8 - 15  : page , menu or other objects  OBJ
 * bit 16 - 23 :  level 2 sub-application G2
 * bit 24 - 31 :  level 1 sub-application G1
 */
public class AppObjInfo {
    
    /** Creates new AppObjInfo */
    public AppObjInfo() {
    }
    
    // filter code
    public static final int FILTER_CODE_G1  = 0xFF000000;
    public static final int FILTER_CODE_G2  = 0x00FF0000;
    public static final int FILTER_CODE_OBJ = 0x0000FF00;
    public static final int FILTER_CODE_CMD = 0x000000FF;
    
    public static final int SHIFT_CODE_G1   = 24;
    public static final int SHIFT_CODE_G2   = 16;
    public static final int SHIFT_CODE_OBJ  = 8;
    //public static final int SHIFT_CODE_CMD=0;

    
    // OBJECT COMMAND
    public static final int COMMAND_VIEW			= 0;
    public static final int COMMAND_ADD                         = 1;
    public static final int COMMAND_UPDATE			= 2;
    public static final int COMMAND_DELETE			= 3;
    public static final int COMMAND_PRINT			= 4;
    public static final int COMMAND_SUBMIT			= 5;
    public static final int COMMAND_START 			= 6;
    public static final int COMMAND_STOP 			= 7;
    public static final String[] strCommand = {"View", "Add", "Update", "Delete",
    "Print", "Submit","Start", "Stop" };
    
    // *** Application Structure ****** //
    public static final int G1_LOGIN  = 0;
    	public static final int G2_LOGIN   = 0;
            public static final int OBJ_LOGIN_LOGIN   = 0;
            public static final int OBJ_LOGIN_LOGOUT   = 1;

    public static final int G1_LEDGER  = 1;
    	public static final int G2_GNR_LEDGER   = 0;
            public static final int OBJ_GNR_LEDGER   = 0;
    
    public static final int G1_REPORT = 2;
    	public static final int G2_REPORT_GNR_LEDGER   = 0;
            public static final int OBJ_REPORT_GNR_LEDGER_PRIV   = 0;
    	public static final int G2_REPORT_QUERY_JOURNAL = 1;
            public static final int OBJ_REPORT_QUERY_JOURNAL_PRIV  = 0;
    	public static final int G2_REPORT_TRIAL_BALANCE = 2;
            public static final int OBJ_REPORT_TRIAL_BALANCE_PRIV  = 0;
    	public static final int G2_REPORT_CASH_FLOW = 3;
            public static final int OBJ_REPORT_CASH_FLOW_PRIV  = 0;
    	public static final int G2_REPORT_GROSS_PROFIT = 4;
            public static final int OBJ_REPORT_GROSS_PROFIT_PRIV  = 0;
    	public static final int G2_REPORT_BALANCE_SHEET = 5;
            public static final int OBJ_REPORT_BALANCE_SHEET_PRIV  = 0;
    	public static final int G2_REPORT_PROFIT_LOSS = 6;
            public static final int OBJ_REPORT_PROFIT_LOSS_PRIV  = 0;
    	public static final int G2_REPORT_ANALYSIS_RATIO = 7;
            public static final int OBJ_REPORT_ANALYSIS_CASH_RATIO  = 0;
            public static final int OBJ_REPORT_ANALYSIS_CURRENCT_RATIO  = 1;
    	public static final int G2_REPORT_ANALYSIS_SOLVABILITY = 8;
            public static final int OBJ_REPORT_ANALYSIS_SOLVABILITY  = 0;            
    	public static final int G2_REPORT_ANALYSIS_RENTABILITY = 9;
            public static final int OBJ_REPORT_ANALYSIS_RENTABILITY_CAPITAL  = 0;
            public static final int OBJ_REPORT_ANALYSIS_RENTABILITY_ECONOMY  = 1;

    public static final int G1_PERIOD = 3;
    	public static final int G2_PERIOD_SETUP   = 0;
            public static final int OBJ_PERIOD_SETUP  = 0;
    	public static final int G2_PERIOD_CLOSE_BOOK   = 1;
            public static final int OBJ_PERIOD_CLOSE_BOOK  = 0;

    public static final int G1_MASTERDATA = 4;
    	public static final int G2_MASTERDATA_ACCOUNT_CART  = 0;
            public static final int OBJ_MASTERDATA_ACCOUNT_CART  = 0;
    	public static final int G2_MASTERDATA_ACCOUNT_LINK   = 1;
            public static final int OBJ_MASTERDATA_ACCOUNT_LINK  = 0;
    	public static final int G2_MASTERDATA_HRD   = 2;
            public static final int OBJ_MASTERDATA_HRD_DEPARTMENT  = 0;
    	public static final int G2_MASTERDATA_PAYMENT   = 3;
            public static final int OBJ_MASTERDATA_PAYMENT_CURRENCY_TYPE  = 0;
            public static final int OBJ_MASTERDATA_PAYMENT_STANDART_RATE  = 1;
            public static final int OBJ_MASTERDATA_PAYMENT_DAILY_RATE  = 2;
    	public static final int G2_MASTERDATA_IJ   = 4;
            public static final int OBJ_MASTERDATA_IJ_IJCONF  = 0;
            public static final int OBJ_MASTERDATA_IJ_CURRENCY_MAPPING  = 1;
            public static final int OBJ_MASTERDATA_IJ_PAYMENT_MAPPING  = 2;
            public static final int OBJ_MASTERDATA_IJ_ACCOUNT_MAPPING  = 3;
            public static final int OBJ_MASTERDATA_IJ_LOCATION_MAPPING  = 4;
        

    public static final int G1_SYSTEM = 5;
    	public static final int G2_SYSTEM_USER_MAN = 0;
            public static final int OBJ_SYSTEM_USER_MAN_USER 	= 0;
            public static final int OBJ_SYSTEM_USER_MAN_GROUP = 1;
            public static final int OBJ_SYSTEM_USER_MAN_PRIVILEGE = 2;
    	public static final int G2_SYSTEM_SYSTEM  = 1;
            public static final int OBJ_SYSTEM_SYSTEM_BACKUP 	= 0;
            public static final int OBJ_SYSTEM_SYSTEM_APP 	= 1;
            public static final int OBJ_SYSTEM_SYSTEM_LOCKING	= 2;
            
    public static final int G1_SEDANA = 6;        
        public static final int G2_MASTERDATA = 0;
            public static final int OBJ_NASABAH = 0;
            public static final int OBJ_BADAN_USAHA = 1;
            public static final int OBJ_PROPINSI = 2;
            public static final int OBJ_KABUPATEN = 3;
            public static final int OBJ_KECAMATAN = 4;
            public static final int OBJ_DESA = 5;
            public static final int OBJ_PENDIDIKAN = 6;
            public static final int OBJ_PEKERJAAN = 7;
            public static final int OBJ_JABATAN = 8;
            public static final int OBJ_LOKET = 9;
            public static final int OBJ_SHIFT = 10;
            public static final int OBJ_JENIS_TRANSAKSI = 11;
            public static final int OBJ_SERVICES = 12;
            
        public static final int G2_MASTER_TABUNGAN = 1;    
            public static final int OBJ_JENIS_ITEM = 0;
            public static final int OBJ_AFILIASI = 1;
            public static final int OBJ_MASTER_TABUNGAN = 2;
            
        public static final int G2_MASTER_KREDIT = 2;
            public static final int OBJ_JENIS_KREDIT = 0;
            public static final int OBJ_SUMBER_DANA = 1;
            public static final int OBJ_KOLEKTIBILITAS_PEMBAYARAN = 2;
            public static final int OBJ_PENJAMIN_KREDIT = 3;
        
        public static final int G2_TRANSACTION = 3;
            public static final int OBJ_PENAMBAHAN_TABUNGAN = 0;
            public static final int OBJ_PENARIKAN_TABUNGAN = 1;
            public static final int OBJ_MUTASI = 2;
            public static final int OBJ_PENGAJUAN_KREDIT = 3;
            public static final int OBJ_PENILAIAN_KREDIT = 4;
            public static final int OBJ_PENCAIRAN_KREDIT = 5;
            public static final int OBJ_PEMBAYARAN_KREDIT = 6;
            public static final int OBJ_DAFTAR_TRANSAKSI_KREDIT = 7;
            public static final int OBJ_SISTEM_KREDIT = 8;
            
        public static final int G2_REPORT = 4;
            public static final int OBJ_REPORT_PER_PINJAMAN = 0;
            public static final int OBJ_TABUNGAN_WAJIB = 1;
            public static final int OBJ_SISA_PINJAMAN = 2;
            public static final int OBJ_KOLEKTIBILITAS = 3;
            public static final int OBJ_RANGKUMAN_KOLEKTIBILITAS = 4;
        
        public static final int G2_TELLER_SHIFT = 5;
            public static final int OBJ_OPENING = 0;
            public static final int OBJ_CLOSING = 1;
            public static final int OBJ_PRINT_OPENING = 2;
            public static final int OBJ_PRINT_CLOSING = 3;
            public static final int OBJ_TELLER_SHIFT_MANAGEMENT = 4;
        
        public static final int G2_SYSTEM_ADMIN = 6;
            public static final int OBJ_USER = 0;
            public static final int OBJ_GROUP_PRIVILAGE = 1;
            public static final int OBJ_PRIVILAGE = 2;
            public static final int OBJ_SYSTEM_PROPERTY = 3;
            
        public static final int G2_MASTER_DOCUMENT = 7;
            public static final int OBJ_DOCUMENT_TYPE = 0;
            public static final int OBJ_DOCUMENT_TEMPLATE = 1;
        
    public static final String[] titleG1 = {
        "Login","Journal", "Report", "Period", "Masterdata", "System","Sedana"
    };
    
    
    public static final String[][] titleG2 = 
    {
        // Login  
        {
            "Login Access"
        },
        // Ledger 
        {
            "Journal"
        },
        // Report 
        {
            "General Ledger", 
            "Query Journal", 
            "Trial Balance", 
            "Cash Flow", 
            "Gross Profit of Goods Sold", 
            "Balance Sheet", 
            "Profit and Loss", 
            "Ratio Analysis", 
            "Solvalibity Analysis", 
            "Rentability Analysis"
        },
        // Period 
        {
            "Setup Period", 
            "Close Book"
        },
        // Master Data 
        {
            "Account Chart", 
            "Account Link", 
            "HRD", 
            "Payment",
            "IJ Configuration"
        },
        // System 
        {
            "User Management", 
            "System Management"
        },
         {//g2 sedana
            "Masterdata", 
            "Master Tabungan",
            "Master Kredit",
            "Transaction",
            "Laporan",
            "Teller Shift",
            "Sistem Admin",
            "Master Dokumen"
        }
    };

    
    public static final String[][][] objectTitles = {
        // Login
        {   // Login
            { "Login Page", "Logout page"}
        },
        // Ledger
        {   // General Ledger
            { "Journal"}
        },
        // Report
        {   // General Ledger
            { "General Ledger"},
            // Query Journal
            { "Query Journal"},
            // Trial Balance
            { "Trial Balance"},
            // Cash Flow
            { "Cash Flow"},
            // Gross Profit
            { "Gross Profit of Goods Sold"},
            // Balance Sheet
            { "Balance Sheet"},
            // Profit Loss
            { "Profit Loss"},
            // Ratio Analysis
            { "Cash Ratio", "Current Ratio"},
            // Solvability Analysis
            { "Solvability"},
            // Rentability Analysis
            { "Equity Rentability", "Economy Rentability"}
        },
        // Period
        {   // Setup Period
            { "Setup Period"},
            // Close Book
            { "Close Book"}
        },
        // Masterdata
        {   // Account Cart
            {"Account Chart"},
            // Account Link
            {"Account Link"},
            // HRD
            {"Department"},
            // Payment
            {"Currency Type","Standart Rate","Daily Rate"},
            // IJ
            {"IJ Configuration","IJ Currency Mapping","IJ Payment Mapping","IJ Account Mapping","IJ Location Mapping"}
        },
        // System
        { 	// User Management
        	{"User", "Group", "Privilege"},
            // System
        	{"Back Up Service","Application Setting","Object Locking"}
        },
        { //g3 sedana
            //masterdata
            {"Nasabah Individu","Badan Usaha","Propinsi", "Kabupaten", "Kecamatan", "Desa", "Pendidikan", "Pekerjaan",
                "Jabatan", "Loket", "Shift", "Jenis Transaksi", "Service"},//0
            //masterdata tabungan
            {"Jenis Item", "Afiliasi", "Master Tabungan"},//1
            //masterdata kredit
            {"Jenis Kredit", "Sumber Dana", "Kolektibilitas Pembayaran", "Penjamin Kredit"},
            //transaction
            {"Penambahan Tabungan", "Penarikan Tabungan", "Mutasi", "Pengajuan Kredit", "Penilaian Kredit",
                "Pencairan Kredit", "Pembayaran Kredit", "Daftar Transaksi", "Sistem Kredit"},
            //laporan
            {"Report Per Pinjaman", "Tabungan Wajib", "Sisa Pinjaman", "Kolektibilitas", "Rangkuman Kolektibilitas"},
            //teller shift
            {"Opening", "Closing", "Print Opening", "Print Closing", "Teller Shift Management"},
            //system admin
            {"User", "Group Privilage", "Privilage", "System Property"},
            //masterdata dokumen
            {"Jenis Dokumen", "Template Dokumen"}
        }
    };
    
    
    public static final int[][][][] objectCommands = {
        // Login
        {   // Login
            {
                //Login Page
                {COMMAND_VIEW, COMMAND_SUBMIT} ,
                //"Logout page"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            }
        },
        // Journal
        {   // Journal
            {
                // Journal
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            }
        },
        
        // Report
        {   // General Ledger
            {
                //"General Ledger"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Query Journal
            {
                //"Query Journal"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Trial Balance
            {
                //"Trial Balance"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Cash Flow
            {
                //"Cash Flow"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Gross Profit
            {
                //"Gross Profit of goods sold"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Balance Sheet
            {
                //"Balance Sheet"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Profit Loss
            {
                //"Profit Loss"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Ratio Analysis            
            { 
                //"Cash Ratio",                 
                {COMMAND_VIEW, COMMAND_SUBMIT},                
                //"Current Ratio"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Solvability Analysis
            { 
                //"Solvability"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            },
            // Rentability Analysis
            { 
                //"Equity Rentability", 
                {COMMAND_VIEW, COMMAND_SUBMIT},
                //"Economy Rentability"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            }
        },

        // Period
        {   // Setup Period
            { //"Setup Period"
                {COMMAND_VIEW, COMMAND_UPDATE}
            },
            // Close Book
            {   //"Close Book"
                {COMMAND_VIEW, COMMAND_SUBMIT}
            }
        },

        // Masterdata
        {   // Account Cart
            {   //"Account Cart"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // Account Link
            {   //"Account Link"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // HRD
            {
                //"Department"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // Payment
            {
                //"Currency Type",
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Standart Rate",
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Daily Rate"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // IJ
            {
                //"IJ Configuration",
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"IJ Currency Mapping",
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"IJ Payment Mapping",
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"IJ Account Mapping",
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"IJ Location Mapping"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },

        // System
        {   // User Management
            {   //"User"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Group"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Privilege"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // System Management
            {   //"Back Up Service"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Application Setting"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Objet Locking"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        {//sedana
            {//masterdata
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//nasabah
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // badan usaha
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // propinsi   
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // kabupaten
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // kecamatan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // desa
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // pendidikan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // pekerjaan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // jabatan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // loket
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // shift
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // jenis transaksi
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE} // service
            },
            {//masterdata tabungan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // jenis item
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // afiliasi
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // master tabungan                
            },
            {//master kredit
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, //jenis kredit
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // sumber dana
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // kolektibilitas pembayaran
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // penjamin kredit
            },
            {//transaksi
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // penambahan tabungan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // penarikan tabungan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // mutasi
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // pengajuan kredit
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // penilaian kredit
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // pencairan kredit
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // pembayaran kredit
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // daftar transaksi
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // sistem kredit
            },
            {// laporan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // report per pinjaman
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // tabungan wajib
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // sisa pinjaman
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // kolektibilitas
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // rangkuman kolektibilitas
            },
            {// teller shift
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // opening
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // closing
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // print opening
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // print closing
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // teller shift management                
            },
            {// sistem admin
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // user
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // group privilage
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // privilage
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // sistem properti
            },
            {//master dokumen
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, //jenis dokumen
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}, // template dokumen
            }
        }
    };
    

    public static String getStrCommand(int command){
        if((command<0) || (command > strCommand.length) ){
            System.out.println(" ERR: getStrCommand - commmand out of range");
            return "";
        }
        return strCommand[command];
        
    }
    
    public static boolean existObject(int g1, int g2, int objIdx){
        if( (g1<0) || (g1> titleG1.length)){
            System.out.println(" ERR: composeCode g1 out of range");
            return false;
        }
             
        if((g2<0) || (g2 > (titleG2[g1]).length))  {
            System.out.println(" ERR: composeCode g2 out of range");
            return false;
        }
        
        if((objIdx<0) || (objIdx> (objectTitles[g1][g2]).length)){
            System.out.println(" ERR: composeCode objIdx out of range");
            return false;
        }
        
        return true;        
    }
    
    public static int composeCode(int g1, int g2, int objIdx, int command) {        
        if(!existObject(g1,g2, objIdx))
            return -1;
        
        if((command<0) || (command > strCommand.length)){ 
            System.out.println(" ERR: composeCode commmand out of range");
            return -1;
        }
        
        if(!privExistCommand(g1,g2, objIdx, command)){
            System.out.println(" ERR: composeCode commmand out not exist on object "+ 
            getTitleGroup1(g1)+"-"+getTitleGroup2(g2)+"-"+getTitleObject(objIdx));
            return -1;
        }
        
        return (g1 << SHIFT_CODE_G1) + (g2 << SHIFT_CODE_G2) + (objIdx << SHIFT_CODE_OBJ ) + command;
    }

    public static int composeCode(int objCode, int command) {        
        if((command<0) || (command > strCommand.length) ){
            System.out.println(" ERR: composeCode commmand out of range");
            return -1;
        }
//        System.out.println("objCode + command"+objCode + command);
        return objCode + command;
    }
    
    public static int composeObjCode(int g1, int g2, int objIdx) {        
        if(!existObject(g1,g2, objIdx))
            return -1;
                
        return (g1 << SHIFT_CODE_G1) + (g2 << SHIFT_CODE_G2) + (objIdx << SHIFT_CODE_OBJ );
    }
    
    private static boolean privExistCommand(int g1, int g2, int objIdx, int command){
        for(int i=0; i< objectCommands[g1][g2][objIdx].length;i++){
            if(objectCommands[g1][g2][objIdx][i]==command)
                return true;            
        }
        return false;
    }

    public static boolean existCommand(int g1, int g2, int objIdx, int command){
        if(!existObject(g1,g2, objIdx))
            return false;
        
        return privExistCommand(g1,g2, objIdx, command);
    }
    
    public static int getG1G2ObjIdx(int code){
        return (code & (FILTER_CODE_G1 + FILTER_CODE_G2 + FILTER_CODE_OBJ));
    }

    public static int getCommand(int code){
        return (code & FILTER_CODE_CMD);
    }
    
    public static int getIdxGroup1(int code){
        int g1 = (code & FILTER_CODE_G1) >> SHIFT_CODE_G1;
        if( (g1<0) || (g1> titleG1.length)){
            System.out.println(" ERR: getIdxGroup1 g1 on code out of range");
            return -1;
        }
        return g1;        
    }

    public static String getTitleGroup1(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return "";

        return titleG1[g1];
    }

    public static int getIdxGroup2(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return -1;
        
        int g2 = (code & FILTER_CODE_G2) >> SHIFT_CODE_G2;
        if( (g2<0) || (g2> titleG2[g1].length)){
            System.out.println(" ERR: getIdxGroup2 g2 on code out of range");
            return -1;
        }
        return g2;        
    }

    public static String getTitleGroup2(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return "";
        
        int g2 = getIdxGroup2(code);
        if(g2<0)
            return "";
        System.out.println("g1 : "+g1);
        System.out.println("g2 : "+g2);
        return titleG2[g1][g2];
    }

    public static int getIdxObject(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return -1;
        
        int g2 = getIdxGroup2(code);
        if(g2<0)
            return -1;
        
        int oidx = (code & FILTER_CODE_OBJ) >> SHIFT_CODE_OBJ;
        if( (oidx<0) || (oidx> objectTitles[g1][g2].length)){
            System.out.println(" ERR: getIdxObject, oidx on code out of range");
            return -1;
        }
        return oidx;        
    }

    public static String getTitleObject(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return "";
        
        int g2 = getIdxGroup2(code);
        if(g2<0)
            return "";
        
        int oidx = getIdxObject(code);
        if(oidx<0)
            return "";
        
        return objectTitles[g1][g2][oidx];
    }
    
    /*
     * parse privobj code into title/string of g1, g2, objidx and command
     * return Vector of String: 0=g1, 1=g, 2=objIdx, 3=command, 4=Integer error code (0=false, -1=falses), 
     * 
     */
    public static Vector parseStringCode(int code){
        Vector titleCodes= new Vector(4,1);
        titleCodes.add(new String(""));
        titleCodes.add(new String(""));
        titleCodes.add(new String(""));
        titleCodes.add(new String(""));
        titleCodes.add(new Integer(0));
        
        int g1 = getIdxGroup1(code);
        if(g1<0){
            titleCodes.set(0, "Invalid G1 Idx");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(0, titleG1[g1]);

        int g2 = getIdxGroup2(code);
        if(g2<0){
            titleCodes.set(1, "Invalid G2 Idx");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(1, titleG2[g1][g2]);
        
        int oidx = getIdxObject(code);
        if(oidx<0){
            titleCodes.set(2, "Invalid Obj. Idx");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(2, objectTitles[g1][g2][oidx]);
        
        int cmd = getCommand(code);
        if(cmd<0){
            titleCodes.set(3, "Invalid Command");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(3, strCommand[cmd]);
                
        return titleCodes;
    }

    
}
