/*
 * AppObjInfo.java
 *
 * Created on April 3, 2002, 4:09 PM
 * Modified on November 27, 2002, 10:14 AM by karya oppe
 */

package com.dimata.posbo.entity.admin;

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
    
    // filter code (contain of 32 bit)
    public static final int FILTER_CODE_G1= 0xFF000000;
    public static final int FILTER_CODE_G2= 0x00FF0000;
    public static final int FILTER_CODE_OBJ=0x0000FF00;
    public static final int FILTER_CODE_CMD=0x000000FF;
    
    public static final int SHIFT_CODE_G1= 24;   // untuk shift sampai 24 bit ke kiri dari belakang, sehingga kita mendapatkan 8 bit pertama/0xFF (G1)
    public static final int SHIFT_CODE_G2= 16;   // untuk shift sampai 16 bit ke kiri dari belakang, sehingga kita mendapatkan 8 bit pertama/0x00FF (G2)
    public static final int SHIFT_CODE_OBJ=8;    // untuk shift sampai 8 bit ke kiri dari belakang, sehingga kita mendapatkan 8 bit pertama/0x0000FF (OBJ)
    //public static final int SHIFT_CODE_CMD=0;
    
    
    // OBJECT COMMAND
    public static final int COMMAND_VIEW    = 0;
    public static final int COMMAND_ADD     = 1;
    public static final int COMMAND_UPDATE  = 2;
    public static final int COMMAND_DELETE  = 3;
    public static final int COMMAND_PRINT   = 4;
    public static final int COMMAND_SUBMIT  = 5;
    public static final int COMMAND_START   = 6;
    public static final int COMMAND_STOP    = 7;
    public static final int COMMAND_APPROVE = 8;
    public static final int COMMAND_VIEW_VALUE = 9; //update opie-eyek 20131201
    public static final int COMMAND_VIEW_STOCK = 10; //update opie-eyek 20131201
    public static final int COMMAND_FINAL = 11;
    public static final int COMMAND_CLOSED = 12;
    /** untuk membatasi informasi yang ditampilkan */
   // public static final int SHOW_ALL_INFO   = 8;
    
    public static final String[] strCommand = {
        "View", "Add New", "Update", "Delete", "Print", "Submit","Start", "Stop", "Approve","View Price","View Stock","Final", "Closed"//update opie-eyek 20131201
    };
    
    // *** Application Structure ****** //
    public static final int G1_LOGIN = 0;
    /**/public static final int G2_LOGIN = 0;
    /******/public static final int OBJ_LOGIN_LOGIN = 0;
    /******/public static final int OBJ_LOGIN_LOGOUT = 1;
    
    /**/public static final int G2_LOGIN_DEFAULT_DISPLAY = 1;
    /******/public static final int OBJ_LOGIN_DEKSTOP = 0;
    /******/public static final int OBJ_LOGIN_MOBILE = 1;
    
     public static final int G1_ADMIN = 1;
    /**/public static final int G2_ADMIN_USER = 0;
    /******/public static final int OBJ_ADMIN_USER_PRIVILEGE = 0;
    /******/public static final int OBJ_ADMIN_USER_GROUP = 1;
    /******/public static final int OBJ_ADMIN_USER_USER = 2;
    
    /**/public static final int G2_ADMIN_SYSTEM = 1;
    /******/public static final int OBJ_ADMIN_SYSTEM_BACK_UP = 0;
    /******/public static final int OBJ_ADMIN_SYSTEM_APP_SET = 1;
    /******/public static final int OBJ_ADMIN_SYSTEM_TRANSFER_DATA = 2;
    /******/public static final int OBJ_ADMIN_SYSTEM_RESTORE_DATA = 3;
    /******/public static final int OBJ_ADMIN_SYSTEM_CLOSING_PERIOD = 4;
    
    public static final int G1_MASTERDATA = 2;
    /**/public static final int G2_MASTERDATA = 0;
    /******/public static final int OBJ_MASTERDATA_CATALOG = 0;
    /******/public static final int OBJ_MASTERDATA_SUPPLIER = 1;
    /******/public static final int OBJ_MASTERDATA_MEMBER = 2;
    /******/public static final int OBJ_MASTERDATA_LOCATION = 3;
    /******/public static final int OBJ_MASTERDATA_STOCK_PERIOD = 4;
    /******/public static final int OBJ_MASTERDATA_CATEGORY = 5;
    /******/public static final int OBJ_MASTERDATA_MERK = 6;
    /******/public static final int OBJ_MASTERDATA_UNIT = 7;
    /******/public static final int OBJ_MASTERDATA_CASHIER = 8;
    /******/public static final int OBJ_MASTERDATA_SHIFT = 9;
    /******/public static final int OBJ_MASTERDATA_SALES = 10;
    /******/public static final int OBJ_MASTERDATA_CURRENCY = 11;
    /******/public static final int OBJ_MASTERDATA_DAILY_RATE = 12;
    /******/public static final int OBJ_MASTERDATA_STANDARD_RATE = 13;
    /******/public static final int OBJ_MASTERDATA_PRICE_TYPE = 14;
    /******/public static final int OBJ_MASTERDATA_DISCOUNT_TYPE = 15;
    /******/public static final int OBJ_MASTERDATA_GROUP_CODE = 16;
    /******/public static final int OBJ_MASTERDATA_CETAK_BARCODE = 17;
    //added by dewok 20180618
    //MASTER LOKASI
    /******/public static final int OBJ_MASTERDATA_COMPANY = 18;
    /******/public static final int OBJ_MASTERDATA_COUNTRY = 19;
    /******/public static final int OBJ_MASTERDATA_PROVINCE = 20;
    /******/public static final int OBJ_MASTERDATA_CITY = 21;
    /******/public static final int OBJ_MASTERDATA_AREA = 22;
    //MASTER KATEGORI
    /******/public static final int OBJ_MASTERDATA_KSG = 23;
    /******/public static final int OBJ_MASTERDATA_COLOR = 24;
    /******/public static final int OBJ_MASTERDATA_KADAR = 25;
    /******/public static final int OBJ_MASTERDATA_EMAS_LANTAKAN = 26;
    /******/public static final int OBJ_MASTERDATA_NILAI_TUKAR_EMAS = 27;
    //DAFTAR MENU
    /******/public static final int OBJ_MASTERDATA_MAPPING_ITEM_STORE_REQUEST = 28;
    /******/public static final int OBJ_MASTERDATA_HARGA_POKOK_ITEM = 29;
    /******/public static final int OBJ_MASTERDATA_PENENTUAN_HPP = 30;
    //KONTAK
    /******/public static final int OBJ_MASTERDATA_GUIDE = 31;
    //LAIN-LAIN
    /******/public static final int OBJ_MASTERDATA_MEMBER_TYPE = 32;
    /******/public static final int OBJ_MASTERDATA_MEMBER_POINT = 33;
    /******/public static final int OBJ_MASTERDATA_COST_TYPE = 34;
    /******/public static final int OBJ_MASTERDATA_SALES_TYPE = 35;
    /******/public static final int OBJ_MASTERDATA_PAYMENT_TYPE = 36;
    /******/public static final int OBJ_MASTERDATA_MARKET_RATE = 37;
    /******/public static final int OBJ_MASTERDATA_SELLING_RATE = 38;
    /******/public static final int OBJ_MASTERDATA_MEMBER_POINT_SETTING = 39;
    /******/public static final int OBJ_MASTERDATA_INCENTIVE_SETTING = 40;
    //MASTER GROUP
    /******/public static final int OBJ_MASTERDATA_MASTER_TYPE = 41;
    /******/public static final int OBJ_MASTERDATA_EMPLOYEE = 42;
    /******/public static final int OBJ_MASTERDATA_POSITION = 43;
    
    /**/public static final int G2_IMPORT_DATA = 1;
    /******/public static final int OBJ_IMPORT_DATA_CATALOG = 0;
    /******/public static final int OBJ_IMPORT_DATA_SUPPLIER = 1;
    /******/public static final int OBJ_IMPORT_DATA_MEMBER = 2;
    
    public static final int G1_SALES = 3;
    /**/public static final int G2_REPORT = 0;
    /******/public static final int OBJ_SALES_GLOBAL = 0;
    /******/public static final int OBJ_SALES_DETAIL = 1;
    /******/public static final int OBJ_GROSS_MARGIN = 2;
    /******/public static final int OBJ_DAILY_SUMMARY = 3;
    /******/public static final int OBJ_PENDING_ORDER = 4;
    /******/public static final int OBJ_BY_INVOICE = 5;
    /******/public static final int OBJ_RETURN_BY_INVOICE = 6;
    /******/public static final int OBJ_DETAIL_INVOICE = 7;
    /******/public static final int OBJ_REPORT_INCENTIVE = 8;
    
    /**/public static final int G2_AR = 1;
    /******/public static final int OBJ_PAYMENT = 0;
    /******/public static final int OBJ_SUMMARY = 1;
    
    /**
     * Ari_wiweka 20130823
     * Nambah privilege untuk kasir, sales order
     */
    /**/public static final int G2_KASIR = 2;
    /******/public static final int OBJ_RETUR = 0;
    /******/public static final int OBJ_INVOICE = 1;
    /******/public static final int OBJ_CREDIT_INVOICE = 2;
    /******/public static final int OBJ_KASIR_PAYMENT = 3;

    /**/public static final int G2_SALES_ORDER = 3;
    /******/public static final int OBJ_SALES_ORDER = 0;
    /******/public static final int OBJ_SALES_INVOICE = 1;
    /******/public static final int OBJ_SALES_RETUR = 2;

    /**/public static final int G2_PAYMENT = 4;
    /******/public static final int OBJ_PAYMENT_CASH = 0;

    public static final int G1_PURCHASING = 4;
    /**/public static final int G2_PURCHASING = 0;
    /******/public static final int OBJ_PURCHASE_ORDER = 0;
    /******/public static final int OBJ_MINIMUM_STOCK = 1;
    /******/public static final int OBJ_PURCHASE_REQUEST = 2;
    
    /**/public static final int G2_AP = 1;
    /******/public static final int OBJ_AP_SUMMARY = 0;
    /******/public static final int OBJ_AP_DETAIL = 1;
    /******/public static final int OBJ_AP_AGING = 2;
    
    public static final int G1_RECEIVING = 5;
    /**/public static final int G2_PURCHASE_RECEIVE = 0;
    /******/public static final int OBJ_PURCHASE_RECEIVE = 0;
    /******/public static final int OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE = 1; //show qty only and price too
    /******/public static final int OBJ_PURCHASE_RECEIVE_WITHOUT_PO = 2; //show qty only and price too
    /******/public static final int OBJ_PURCHASE_RECEIVE_PAYMENT= 3; //show receive payment

    /**/public static final int G2_PURCHASE_RECEIVE_REPORT = 1;
    /******/public static final int OBJ_PURCHASE_RECEIVE_REPORT = 0;
    /******/public static final int OBJ_PURCHASE_RECEIVE_REPORT_BY_RECEIPT = 1;
    /******/public static final int OBJ_PURCHASE_RECEIVE_REPORT_BY_SUPPLIER = 2;
    /******/public static final int OBJ_PURCHASE_RECEIVE_REPORT_BY_CATEGORY = 3;
    
    /**/public static final int G2_LOCATION_RECEIVE = 2;
    /******/public static final int OBJ_LOCATION_RECEIVE = 0;
    
    /**/public static final int G2_LOCATION_RECEIVE_REPORT = 3;
    /******/public static final int OBJ_LOCATION_RECEIVE_REPORT = 0;
    /******/public static final int OBJ_LOCATION_RECEIVE_REPORT_BY_RECEIPT = 1;
    /******/public static final int OBJ_LOCATION_RECEIVE_REPORT_BY_CATEGORY = 2;
    
    public static final int G1_RETURN = 6;
    /**/public static final int G2_SUPPLIER_RETURN = 0;
    /******/public static final int OBJ_SUPPLIER_RETURN = 0;
    
    /**/public static final int G2_SUPPLIER_RETURN_REPORT = 1;
    /******/public static final int OBJ_SUPPLIER_RETURN_REPORT_BY_SUPPLIER = 0;
    /******/public static final int OBJ_SUPPLIER_RETURN_REPORT_BY_INVOICE = 1;
    /******/public static final int OBJ_SUMMARY_SUPPLIER_RETURN_REPORT_BY_INVOICE = 2;
    
    public static final int G1_TRANSFER = 7;
    /**/public static final int G2_TRANSFER = 0;
    /******/public static final int OBJ_TRANSFER = 0;
    
    /**/public static final int G2_TRANSFER_REPORT = 1;
    /******/public static final int OBJ_TRANSFER_REPORT = 0;
    /******/public static final int OBJ_TRANSFER_REPORT_BY_INVOICE = 1;
    /******/public static final int OBJ_TRANSFER_REPORT_BY_SUPPLIER = 2;
    /******/public static final int OBJ_TRANSFER_REPORT_BY_CATEGORY = 3;
    
    /**/public static final int G2_TRANSFER_PRODUCTION = 2;
    /******/public static final int OBJ_TRANSFER_PRODUCTION_FOR_GOLD = 0;
    
    public static final int G1_COSTING = 8;
    /**/public static final int G2_COSTING = 0;
    /******/public static final int OBJ_COSTING = 0;
    
    /**/public static final int G2_COSTING_REPORT = 1;
    /******/public static final int OBJ_COSTING_REPORT = 0;
    /******/public static final int OBJ_COSTING_REPORT_BY_CATEGORY = 1;
    
    public static final int G1_STOCK = 9;
    /**/public static final int G2_STOCK = 0;
    /******/public static final int OBJ_STOCK_OPNAME = 0;
    /******/public static final int OBJ_STOCK_CORRECTION = 1;
    /******/public static final int OBJ_STOCK_POSTING = 2;
    
    /**/public static final int G2_STOCK_REPORT = 1;
    /******/public static final int OBJ_STOCK_REPORT = 0;
    /******/public static final int OBJ_STOCK_REPORT_BY_CATEGORY = 1;
    /******/public static final int OBJ_STOCK_REPORT_BY_SUPPLIER = 2;
    /******/public static final int OBJ_STOCK_CARD = 3;
    /******/public static final int OBJ_STOCK_POTITION = 4;
    /******/public static final int OBJ_STOCK_POTITION_BY_CATEGORY = 5;
    /******/public static final int OBJ_STOCK_SEARCH = 6;
    
    //update opie-eyek untuk membuat privilage menu
    public static final int G1_MENU = 10;
     /**/public static final int G2_MENU_HEADER = 0;
     /******/public static final int OBJ_MENU_HEADER_HOME = 0;
     /******/public static final int OBJ_MENU_HEADER_PENJUALAN = 1;
     /******/public static final int OBJ_MENU_HEADER_PEMBELIAN = 2;
     /******/public static final int OBJ_MENU_HEADER_PENERIMAAN = 3;
     /******/public static final int OBJ_MENU_HEADER_RETURN = 4;
     /******/public static final int OBJ_MENU_HEADER_TRANSFER = 5;
     /******/public static final int OBJ_MENU_HEADER_PEMBIAYAAN =6;
     /******/public static final int OBJ_MENU_HEADER_STOK = 7;
     /******/public static final int OBJ_MENU_HEADER_SISTEM = 8;
     /******/public static final int OBJ_MENU_HEADER_MASTERDATA = 9;
     

     /**/public static final int G2_MENU_SUBMENU_HOME = 1;
     /******/public static final int OBJ_MENU_HOME_PURCHASE_REQUEST_WAREHOUSE = 0;
     /******/public static final int OBJ_MENU_HOME_PURCHASE_REQUEST_OUTLET = 1;
     /******/public static final int OBJ_MENU_HOME_PURCHASE_ORDER = 2;
     /******/public static final int OBJ_MENU_HOME_RECEIVE_FROM_PO = 3;
     /******/public static final int OBJ_MENU_HOME_RECEIVE_WITHOUT_PO = 4;
     /******/public static final int OBJ_MENU_HOME_RETURN_WITH_PO = 5;
     /******/public static final int OBJ_MENU_HOME_RETURN_WITHOUT_PO = 6;
     /******/public static final int OBJ_MENU_HOME_REQUEST_TRANSFER = 7;
     /******/public static final int OBJ_MENU_HOME_TRANSFER = 8;
     /******/public static final int OBJ_MENU_HOME_RECEIVE_TRANSFER = 9;
     /******/public static final int OBJ_MENU_HOME_COSTING = 10;
     /******/public static final int OBJ_MENU_HOME_TRANSFER_UNIT = 11;
     
     /**/public static final int G2_MENU_SUBMENU_PENJUALAN = 2;
     /*report*/
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_PENJUALAN_GLOBAL = 0;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_PENJUALAN_DETAIL = 1;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_GROSS_MARGIN =2;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_REKAP_HARIAN = 3;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_PENDING_ORDER = 4;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_PER_INVOICE = 5;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_INVOICE_DETAIL = 6;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_RETUR_PER_INVOICE = 7;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_REKAPITULASI_REPORT = 8;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_REKAP_PER_KSG = 9;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_LAPORAN_PER_CASH_OPENING = 10;
     /*piutang*/
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_PIUTANG_PELUNASAN_PIUTANG = 11;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN_PIUTANG_REKAP_PIUTANG = 12;

     /**/public static final int G2_MENU_SUBMENU_PEMBELIAN = 3;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBELIAN_ORDER_PEMBELIAN_UBAH_TAMBAH = 0;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBELIAN_MINIMUM_STOK_UBAH_TAMBAH = 1;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBELIAN_HUTANG_REKAP = 2;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBELIAN_HUTANG_REKAP_DETAIL = 3;

     /**/public static final int G2_MENU_SUBMENU_PENERIMAAN = 4;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_PEMBELIAN_UBAH_TAMBAH = 0;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_PEMBELIAN_LAPORAN_GLOBAL = 1;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_PEMBELIAN_LAPORAN_PERNOTA = 2;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_PEMBELIAN_LAPORAN_PERSUPPLIER = 3;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_PEMBELIAN_LAPORAN_PERKATEGORI = 4;

     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_GUDANG_UBAH_TAMBAH = 5;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_GUDANG_LAPORAN_GLOBAL = 6;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_GUDANG_LAPORAN_PERNOTA = 7;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_GUDANG_LAPORAN_PERSUPPLIER = 8;
     /******/public static final int OBJ_MENU_SUBMENU_PENERIMAAN_DARI_GUDANG_LAPORAN_PERKATEGORI = 9;

     /**/public static final int G2_MENU_SUBMENU_RETURN = 5;
     /******/public static final int OBJ_MENU_SUBMENU_RETURN_UBAH_TAMBAH = 0;
     /******/public static final int OBJ_MENU_SUBMENU_RETURN_LAPORAN_PER_INVOICE = 1;
     /******/public static final int OBJ_MENU_SUBMENU_RETURN_LAPORAN_PER_SUPPLIER = 2;

     /**/public static final int G2_MENU_SUBMENU_TRANSFER =6;
     /******/public static final int OBJ_MENU_SUBMENU_TRANSFER_UBAH_TAMBAH = 0;
     /******/public static final int OBJ_MENU_SUBMENU_TRANSFER_LAPORAN_GLOBAL = 1;
     /******/public static final int OBJ_MENU_SUBMENU_TRANSFER_LAPORAN_PER_INVOICE = 2;
     /******/public static final int OBJ_MENU_SUBMENU_TRANSFER_LAPORAN_PER_SUPPLIER = 3;
     /******/public static final int OBJ_MENU_SUBMENU_TRANSFER_LAPORAN_PER_KATEGORI = 4;
     /******/public static final int OBJ_MENU_SUBMENU_TRANSFER_PENGIRIMAN_BARANG = 5;
        
     /**/public static final int G2_MENU_SUBMENU_PEMBIAYAAN =7;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBIAYAAN_UBAH_TAMBAH = 0;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBIAYAAN_LAPORAN_GLOBAL = 1;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBIAYAAN_LAPORAN_PER_INVOICE = 2;
     /******/public static final int OBJ_MENU_SUBMENU_PEMBIAYAAN_LAPORAN_PER_KATEGORI = 3;

     /**/public static final int G2_MENU_SUBMENU_STOK =8;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_UBAH_TAMBAH = 0;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_OPNAME_USE_FILE = 1;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_KOREKSI_STOK = 2;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_LAPORAN_GLOBAL = 3;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_LAPORAN_PER_KATEGORI = 4;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_LAPORAN_PER_SUPPLIER = 5;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_LAPORAN_KARTU_STOK = 6;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_LAPORAN_POSISI_STOK = 7;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_LAPORAN_SELISIH_KOREKSI_STOK = 8;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_POSTING_STOK = 9;
     /******/public static final int OBJ_MENU_SUBMENU_STOK_REPOSTING_STOK = 10;
     
     /**/public static final int G2_MENU_SUBMENU_MASTERDATA =9;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_KATEGORI_BARANG = 0;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_MERK = 1;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_SATUAN = 2;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_LOKASI = 3;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_BARANG = 4;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_PERIODE = 5;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_SUPPLIER = 6;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_TIPE_HARGA = 7;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_TIPE_POTONGAN = 8;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_TIPE_CUSTOMER = 9;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_MEMBER = 10;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_POINT_MEMBER = 11;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_JENIS_MATA_UANG = 12;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_NILAI_TUKAR_HARIAN =13;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_NILAI_TUKAR_STANDART = 14;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_TIPE_BIAYA = 15;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_SISTEM_PEMBAYARAN = 16;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_MASTER_KASIR = 17;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_SHIFT_KERJA = 18;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_SALES = 19;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_RUANGAN = 20;
     /******/public static final int OBJ_MENU_SUBMENU_MASTERDATA_DAFTAR_MEJA = 21;

     /**/public static final int G2_MENU_SUBMENU_SISTEM =10;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_PEMAKAI = 0;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_GROUP = 1;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_HAK_AKSES = 2;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_PENGATURAN_APLIKASI = 3;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_MODIF_TEMPLATE = 4;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_TUTUP_PERIODE = 5;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_INTERNET_CONNECTION = 6;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_TRANSFER_KE_OUTLET = 7;
     /******/public static final int OBJ_MENU_SUBMENU_SISTEM_TRANSFER_PENJUALAN = 8;
     
    public static final int G1_MARKETING = 11;
     /**/public static final int G2_MENU_SUBMENU_MARKETING = 0;
     /******/public static final int OBJ_MENU_SUBMENU_MARKETING = 0;
     
    public static final int G1_DELIVERY = 12;
     /**/public static final int G2_MENU_SUBMENU_PRODUKSI = 0;
     /******/public static final int OBJ_MENU_SUBMENU_PENJUALAN = 0;
     /******/public static final int OBJ_MENU_SUBMENU_LAPORAN = 1;
     /******/public static final int OBJ_MENU_SUBMENU_DOKUMEN = 2;

    public static final String[] titleG1 = {
        "Login Access",
        "Admin",
        "Master Data",
        "Sales/Cashier",
        "Purchasing",
        "Receiving",
        "Return",
        "Transfer",
        "Costing",
        "Stock",
        "Menu",
        "Marketing",
        "Delivery"
    };
    
    
    public static final String[][] titleG2 = {
        /* login */ {"Login Access","Login Display"},
        /* admin*/ {"User Management", "System Management"},
        /* master data */ {"Master Data", "Import Data"},
        /* sales */ {"Report", "AR", "Kasir", "Sales Order", "Payment"},
        /* purchasing */ {"Purchasing","AP"},
        /* receiving */ {"Purchase Receive", "Purchase Receive Report", "Location Receive", "Location Receive Report"},
        /* return */ {"Supplier Return", "Report"},
        /* transfer */ {"Transfer", "Report", "Transfer Production"},
        /* costing */ {"Costing", "Report"},
        /* stock */ {"Stock", "Report"},
        /*menu */{"Menu Header","Sub Menu Home","Sub Menu Penjualan","Sub Menu Pembelian","Sub Menu Penerimaan","Sub Menu Return","Sub Menu Transfer","Sub Menu Pembiayaan","Sub Menu Stok","Sub Menu Masterdata","Sub Menu Sistem"},
        /*Marketing */{"Marketing"},
        /*Delivery*/{"Produksi"}
    };
    
    
    public static final String[][][] objectTitles = {
        // Login
        {   // Login
            {"Login Page", "Logout page"},
            {"Dekstop", "Mobile"}
        },
        // Admin
        {   // User
            {"Pivilege", "Group", "User"},
            // System
            {"Data Backup", "Application Setting", "Transfer Data", "Restore Data", "Closing Period"}
        },
        // Master Data
        {   // Master Data
            {
                "Catalog", "Supplier", "Member", "Location", "Stock Period", "Category", "Merk", "Unit", "Cashier", "Shift", "Sales", "Currency", "Daily Rate", "Standard Rate", "Price Type", "Discount Type", "Group Code","Cetak Barcode",
                //added by dewok 20180618
                //MASTER LOKASI
                "Company","Country","Province","City","Area",
                //MASTER KATEGORI
                "KSG","Color","Kadar","Emas Lantakan","Nilai Tukar Emas",
                //DAFTAR MENU
                "Mapping Item Store Request","Harga Pokok Item","Penentuan HPP",
                //KONTAK
                "Guide",
                //LAIN-LAIN
                "Member Type","Member Point","Cost Type","Sales Type","Payment Type",
                "Market Rate","Selling Rate","Member Point Setting","Incentive Setting",
                //MASTER GROUP
                "Master Type","Employee","Position"
            },
            // Import Data
            {"Catalog", "Supplier", "Member"}
        },
        // Sales
        {   // Report
            {"Sales Global Report", "Sales Detail Report", "Gross Margin Report", "Daily Summary Report", "Pending Order Report", "Sales Report by Invoice", "Return Report by Invoice", "Detail Invoice Report", "Incentive Report"},
            // AR
            {"AR Payment", "AR Summary"},
            // Kasir
            {"Retur", "Invoice", "Credit Invoice", "Payment"},
            // Sales Order
            {"Sales Order", "Sales Invoice", "Sales Retur"},
            //Payment Cash
            {"Payment Cash"}
        },
        // Purchasing
        {   // Purchasing
            {"Purchase Order", "Minimum Stock", "Purchase Request"},
            // AP
            {"AP Summary", "AP Detail", "AP Aging"}
        },
        // Receiving
        {   // Purchase Receive
            {"Purchase Receive","Purchase Receive Show Qty and Price","Purchase Receive Without PO","Receive Payment"},
            // Purchase Receive Report
            {"Purchase Receive Report", "Purchase Receive Report by Receipt", "Purchase Receive Report by Supplier", "Purchase Receive Report by Category"},
            // Location Receive
            {"Location Receive"},
            // Location Receive Report
            {"Location Receive Report", "Location Receive Report by Receipt", "Location Receive Report by Category"}
        },
        // Return
        {   // Return
            {"Supplier Return"},
            // Report
            {"Supplier Return Report by Supplier", "Supplier Return Report by Invoice", "Summary Supplier Return Report by Invoice"}
        },
        // Transfer
        {   // Transfer
            {"Transfer"},
            // Report
            {"Transfer Report", "Transfer Report by Invoice", "Transfer Report by Supplier", "Transfer Report by Category"},
            // Production
            {"Production for gold"}
        },
        // Costing
        {   // Costing
            {"Costing"},
            // Report
            {"Costing Report", "Costing Report by Category "}
        },
        // Stock
        {   // Stock
            {"Stock Opname", "Stock Correction", "Stock Posting"},
            // Report
            {"Stock Report", "Stock Report by Category", "Stock Report by Supplier", "Stock Card", "Stock Potition Report", "Stock Potition Report by Category","Stock Search"}
        },
        // Menu
        {   // Menu Header
            {"Home", "Penjualan", "Pembelian","Penerimaan","Return","Transfer","Pembiayaan","Stok","Sistem","Masterdata"},
            //Sub Menu Home",
            {"Purchase Request Warehouse","Purchase Request Outlet","Purchase Order","Receive From PO","Receive Without PO","Return With PO","Return Without PO","Request Transfer","Transfer","Receive Transfer","Costing","Transfer Unit"},
            //"Sub Menu Penjualan",
            {"Sub Menu Home"},
            //"Sub Menu Pembelian",
            {"Sub Menu Home"},
            //"Sub Menu Penerimaan",
            {"Sub Menu Home"},
            //"Sub Menu Return",
            {"Sub Menu Home"},
            //"Sub Menu Transfer",
            {"Sub Menu Home"},
            //"Sub Menu Pembiayaan",
            {"Sub Menu Home"},
            //"Sub Menu Stok",
            {"Sub Menu Home"},
            //"Sub Menu Masterdata",
            {"Sub Menu Home"},
            //"Sub Menu Sistem"}
            {"Stock Report"}
        },
        {
            {"Marketing"}
        },
        {
            {"Daftar Penjualan", "Laporan Barang", "Dokumen Pengiriman"}
        }
    };
    
    
    public static final int[][][][] objectCommands = {
        // Login
        {   // Login
            {
                //Login Page
                {COMMAND_VIEW, COMMAND_SUBMIT} ,
                //"Logout page"
                {COMMAND_VIEW, COMMAND_SUBMIT},
               
            },
            {
                //display dekstop
                {COMMAND_VIEW, COMMAND_SUBMIT},
                //display mobile
                {COMMAND_VIEW, COMMAND_SUBMIT}
            }
        },
        
        // Admin
        {   // user
            {
                //"Pivilege"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                //"Group"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                //"User"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // system
            {
                //"Data Backup"
                {COMMAND_VIEW, COMMAND_START, COMMAND_STOP},
                //"Application Setting"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // Transfer Data
                {COMMAND_VIEW, COMMAND_START},
                // Restore Data
                {COMMAND_VIEW, COMMAND_START},
                // Closing Period
                {COMMAND_VIEW, COMMAND_START}
            }
        },
        // Master Data
        {   //Master Data
            {   //"Catalog"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE,COMMAND_VIEW_VALUE,COMMAND_VIEW_STOCK},
                //"Supplier"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Member"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Location"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Stock Period"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Category"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Merk"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Unit"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Cashier"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Shift"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Sales"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Currency"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Daily Rate"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Standard Rate"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Price Type"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Discount Type"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Group Code"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Group cetak barcode"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //added by dewok 20180618
                //MASTER LOKASI
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//company
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//country
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//province
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//city
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//area
                //MASTER KATEGORI
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//ksg
                {COMMAND_VIEW},//color
                {COMMAND_VIEW},//kadar
                {COMMAND_VIEW},//emas lantakan
                {COMMAND_VIEW},//nilai tukar emas
                //DAFTAR MENU
                {COMMAND_VIEW},//mapping item sr
                {COMMAND_VIEW},//harga pokok item
                {COMMAND_VIEW},//penentuan hpp
                //KONTAK
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//guide
                //LAIN-LAIN
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//member type
                {COMMAND_VIEW},//member point
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//cost type
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//sales type
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//payment type
                {COMMAND_VIEW},//market rate
                {COMMAND_VIEW},//selling rate
                {COMMAND_VIEW},//point setting
                {COMMAND_VIEW},//incentive setting
                //MASTER GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//master type
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE},//employee
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},//position
            },
            // "Import Data"
            {   //"Catalog"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Supplier"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"Member"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        // Sales
        {   // Report
            {   //"Sales Global Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Sales Detail Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Gross Margin Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Daily Summary Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Pending Order Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Sales Report by Invoice"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Return Report by Invoice"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Detail Invoice Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Incentive Report"
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // AR
            {   //"AR Payment"
                {COMMAND_VIEW},
                //"AR Summary"
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // KASIR
            {   //"Retur"
                {COMMAND_VIEW},
                //"Invoice"
                {COMMAND_VIEW,},
                //"Credit Invoice"
                {COMMAND_VIEW,},
                //"Payment"
                {COMMAND_VIEW,}
            },
            // Sales Order
            {   //"Sales Order"
                {COMMAND_VIEW},
                //"Sales Invoice"
                {COMMAND_VIEW,},
                //"Sales Retur"
                {COMMAND_VIEW,}
            },
            // Payment
            {   //"Payment Cash"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        // Purchasing
        {   // Purchasing
            {   //"Purchase Order"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE, COMMAND_CLOSED},
                //"Minimum Stock"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_START},
                        
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE}
            },
            // AP
            {   //"AP Summary"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE},
                //"AP Detail"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"AP Aging"
                {COMMAND_VIEW}
            }
        },
        // Receiving
        {   // Purchase Receive
            {   //"Purchase Receive"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE,COMMAND_FINAL},
                {COMMAND_VIEW},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE,COMMAND_FINAL},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE,COMMAND_FINAL}
            },
            // Purchase Receive Report
            {	//"Purchase Receive Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Purchase Receive Report by Receipt"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Purchase Receive Report by Supplier"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Purchase Receive Report by Category"
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // Location Receive
            {   //"Location Receive"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE,COMMAND_FINAL}
            },
            // Location Receive Report
            {	//"Location Receive Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Location Receive Report by Receipt"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Location Receive Report by Category"
                {COMMAND_VIEW, COMMAND_PRINT}
            }
        },
        // Return
        {   // Return
            {   //"Supplier Return"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE}
            },
            // Supplier Return Report
            {	//"Supplier Return Report by Supplier"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Supplier Return Report by Invoice"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Summary Supplier Return Report by Invoice"
                {COMMAND_VIEW, COMMAND_PRINT}
            }
        },
        // Transfer
        {   // Transfer
            {   //"Transfer"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT, COMMAND_APPROVE,COMMAND_VIEW_VALUE}
            },
            // Report
            {   //"Transfer Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Transfer Report by Invoice"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Transfer Report by Supplier"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Transfer Report by Category"
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            {   //"Transfer Production"
                {COMMAND_VIEW}
            }
        },
        // Costing
        {   // Costing
            {   //"Costing"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE}
            },
            // Report
            {	//"Costing Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Costing Report by Category"
                {COMMAND_VIEW, COMMAND_PRINT}
            }
        },
        // Stock
        {   // Stock
            {   //"Stock Opname"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT, COMMAND_FINAL},
                //"Stock Correction"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //"Stock Posting"
                {COMMAND_VIEW, COMMAND_START}
            },
            // Report
            {	//"Stock Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Stock Report by Category"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Stock Report by Supplier"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Stock Card"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Stock Potition Report"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Stock Potition Report by Category"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"Stock Search"
                {COMMAND_VIEW, COMMAND_PRINT}
            }
        },

        //
        // Menu
        {   // header menu
            { 
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW}
            },
            // sub menu home
            {	
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW}
            },
            // sub menu penjualan
            {
                {COMMAND_VIEW}
            },
            //sub menu pembelian
            {
                {COMMAND_VIEW}
            },
            //sub menu penerimaan
            {
                {COMMAND_VIEW}
            },
            //sub menu retun
            {
                {COMMAND_VIEW}
            },
            //sub menu transfer
            {
                {COMMAND_VIEW}
            },
            //sub menu pembiayaan
            {
                {COMMAND_VIEW}
            },
            //sub menu stok
            {
                {COMMAND_VIEW}
            },
            //sub menu masterdata
            {
                {COMMAND_VIEW}
            },
            //sub menu sistem
            {
                {COMMAND_VIEW}
            }
        },
        // Marketing
        {   // Marketing
            {   //"Marketing"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT,COMMAND_APPROVE}
            }
        },
        // Delivery
        {
            {
                //Penjualan
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //Laporan
                {COMMAND_VIEW, COMMAND_PRINT},
                //Dokumen Pengiriman
                {COMMAND_VIEW, COMMAND_PRINT}
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
        //System.out.println("objCode + command ="+objCode + command);
        return objCode + command;
    }
    
    public static int composeObjCode(int g1, int g2, int objIdx) {
        if(!existObject(g1,g2, objIdx))
            return -1;
        
        return (g1 << SHIFT_CODE_G1) + (g2 << SHIFT_CODE_G2) + (objIdx << SHIFT_CODE_OBJ);
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
