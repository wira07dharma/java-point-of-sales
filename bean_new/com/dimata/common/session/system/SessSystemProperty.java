/*
 * SessSystemProperty.java
 *
 * Created on April 30, 2002, 10:11 AM
 */

package com.dimata.common.session.system;

import com.dimata.common.entity.system.*;

public class SessSystemProperty {


    public static boolean loaded      = false;
    public static String[] groups = {"APPLICATION SETUP"};    
    public static String[][] subGroups = {     
        {"Application","Cashier","Outlet"}        
    };
public static String[] systemPropsProchain = {
 "SELF_SUPPLIER_ID",
"BOOK_TYPE",
"AUTO_REC_FA",
"PERIOD_INTERVAL",
"LOC_CODE",
"POS_PPN_DEFAULT",
"MAPPING_NEW_SUPPLIER_ID",
"PROP_IMGCACHE",
"LOC_TRANS_ID",
"NO_COLOR_ID",
"FINISH_PACKED_ID",
"UNFINISH_ID",
"TARITA_ID",
"COMPANY_ID",
"COST_GROUP_TARITA_ID",
"COST_GROUP_COMPANY_ID",
"PAYMENT_TYPE_CASH",
"PAYMENT_TYPE_BANK",
"ID_STANDART_RATE",
"INTERNAL_LOCATION",
"ENABLE_EXPIRED_DATE",
"SHOW_DISCOUNT_1",
"SHOW_DISCOUNT_2",
"SHOW_DISCOUNT_NOMINAL",
"SHOW_ONGKOS_KIRIM",
"SHOW_BONUS",
"SHOW_HARGA_BELI",
"SHOW_COLOR",
"SHOW_ETALASE",
"SHOW_TOTAL_BELI",
"AUTO_SAVE_RECEIVING",
"PRICE_RECEIVING_READONLY",
"SEDANA_URL",
"INTEGRATION_UNIT",
"INTEGRATION_NOMERK",
"INTEGRATION_CURR_RP",
"INTEGRATION_CURR_USD",
"INTEGRATION_PRICE_MAP",
"INTEGRATION_PRICE_MAP_CREDIT",
"PRICE_TYPE_SHOPPING_CHART",
"POS_EMAIL_NOTIFICATION",
"TYPE_OF_BUSINESS_DETAIL",
"DEFAULT_SELL_UNIT_ID",
"DEFAULT_PRICE_CURRENCY_ID",
"DEFAULT_ETALASE_NAME_REGISTER",
"DEFAULT_ETALASE_CODE_REGISTER",
"IMG_CACHE",
"DESIGN_MATERIAL_FOR",
"SKU_GENERATE_OTOMATIC",
"NAME_MATERIAL_MULTI_LANGUAGE",
"PERCENTASE_UP_PRICE_STORE_REQUEST",
"VERSI_SETTING_DISCOUNT",
"ENABLE_DUTY_FREE",
"RECEIVE_AUTO_FINAL_BY_DF",
"CALCULATE_STOCK_VALUE",
"AUTO_SAVE_TRANSFER",
"TYPE_OF_BUSINESS",
"INCLUDE_PPN_MASUKAN",
"INTEGRASI_TAX_AR",
"INTEGRASI_ID_CASHIER_AR",
"SIGN_COSTING_1",
"SIGN_COSTING_2",
"SIGN_COSTING_3",
"USE_BARCODE_OR_SKU_IN_REPORT",
"EXCHANGE_RATE",
"LOCAL_CURRENCY_DEFAULT",
"PRINT_SMALL_USE_HEADER",
"PRINT_SMALL_USE_FOOTER",
"PRINT_SMALL_BILL_HIGHT_HEADER",
"PRINT_SMALL_MAXIMUM_PER_PAGE",
"FOOTER_TEXT_GUEST_FOLIO",
"SIGN_TRANSFER_CUSTOMS_1",
"SIGN_TRANSFER_CUSTOMS_2",
"SIGN_TRANSFER_CUSTOMS_3",
"SIGN_TRANSFER_1",
"SIGN_TRANSFER_2",
"SIGN_PURCHASE_ORDER_1",
"SIGN_PURCHASE_ORDER_2",
"SIGN_PURCHASE_ORDER_3",
"SIGN_PURCHASE_ORDER_4",
"SHIPPING_INFORMATION_LEFT",
"SHIPPING_INFORMATION_RIGHT",
"SIGN_REQUEST_TRANSFER_DF_1",
"SIGN_REQUEST_TRANSFER_DF_2",
"SIGN_RECEIVE_DF_1",
"SIGN_RECEIVE_DF_2",
"SIGN_RECEIVE_DF_3",
"REKENING_ACCOUNT_NUMBER",
"SEPARATOR_SKU",
"HPP_CALCULATION",
"CHECK_STOK_DOC_STATUS_FINAL",
"SEASON_GROUP_TYPE",
"USER_EMAIL_BOOKING_ONLINE",
"PWD_EMAIL_BOOKING_ONLINE",
"HOST_EMAIL_BOOKING_ONLINE",
"PORT_EMAIL_BOOKING_ONLINE",
"EMAIL_HOTEL_BOOKING_ONLINE",
"EMAIL_BCC_BOOKING_ONLINE",
"USING_PAYMENT_DYNAMIC",
"IMG_POSTFIX",
"IMGDOC",
"MATERIAL_PERIOD",
"DATABASE_HOME",
"MASTEROUT_HOME",
"MASTERIN_HOME",
"TRANSOUT_HOME",
"TRANSIN_HOME",
"BARCODE_DIGIT_COL_1",
"BARCODE_DIGIT_COL_2",
"BARCODE_DIGIT_COL_3",
"BARCODE_DIGIT_COL_4",
"OPNAME_FILE_TYPE",
"PROCHAIN_MENU_BOOTSTRAP",
"USE_BARCODE_PATH_STATIC",
"BARCODE_PATH_STATIC",
"PERIODE_MINUTE_OPNAME",
"PRICE_TYPE_VIP",
"PRICE_TYPE_WHOLESALE",
"PRICE_TYPE_RETAIL",
"DISC_VIP",
"DISC_WHOLESALE",
"DISC_RETAIL",
"PROCHAIN_LOGIN_TYPE",
"FREE_LICENSE",
"LOGBOOK_COMPANY_ID",
"LOGBOOK_COMPANY_PWD",
"PRICE_TRANSACTION_USED_HPP",
"VALUE_DIV",
"CASHIER_USE_OPENING_BALANCE",
"SYSTEM_USE_BRAND",
"COMPANY_NAME",
"COMPANY_ADDRESS",
"COMPANY_PHONE",
"NAME_OF_MERK",
"NAME_OF_CATEGORY",
"USE_ETALASE",
"USE_CONSIGNMENT",
"SHOW_DAFTAR_PERIODE",
"SHOW_TIPE_POTONGAN",
"SHOW_TIPE_CUSTOMER",
"SHOW_POINT_MEMBER",
"SHOW_NILAI_TUKAR_HARIAN",
"SHOW_NILAI_TUKAR_STANDART",
"SHOW_AKSESORIS",
"SHOW_PRODUKSI",
"SHOW_DAILY_SELISIH_KOREKSI_STOK",
"USE_FOR_GREENBOWL",
"USE_FOR_RADITYA",
"OID_DIVE_CENTER_LOCATION",
"USE_SUB_CATEGORY",
"OID_CURR_FOR_PRICE_SALE",
"MAPPING_PRINT_PRODUKSI",
"RETAIL",
"AUTO_CALCULATING_DISCOUNT_TO_PRICE",
"PATH_IMAGE",
"USE_MASTER_GROUP",
"OUTLET_DEFAULT_LOCATION",
"USE_CASHLESS_MODULE",
"SPESIAL_REQUEST_FOOD",
"SPESIAL_REQUEST_BEVERAGE",
"DEFAULT_SUPPLIER_PO",
"POS_EMAIL_TO",
"POS_URL_ONLINE",
"CASHIER_USING_COVER_NUMBER",
"INTEGRASI_URL_SAVE_AR_API",
"INTEGRASI_DB2",
"INTEGRASI_DATABASE",
"SIGN_RECEIVE_1",
"SIGN_RECEIVE_2",
"SIGN_RECEIVE_3",
"SIGN_RECEIVE_4",
"SIGN_SALES_1",
"SIGN_SALES_2",
"SIGN_SALES_3",
"SIGN_SALES_4",
"PATH_DATA_OUT",
"PATH_DATA_IN",
"PATH_MYSQL_BIN",
"SHOW_STOCK_NOL",
"SHOW_ETALASE_TRANSFER",
"SHOW_HPP_TRANSFER",
"SHOW_TOTAL_TRANSFER",
"SIGN_TRANSFER_3",
"CASHIER_SUGGESTED_DISCOUNT_VALUE",
"PRICE_PROTECTION_1",
"PRICE_PROTECTION_2",
"PRICE_PROTECTION_3",
"SEDANA_ENABLE_TABUGAN",
"DEFAULT_RECEIVE_TYPE",
"SHOW_HARGA_JUAL",
"SHOW_HS_CODE",
"SHOW_KETERANGAN",
"SHOW_PENERIMAAN_HPP",
"SIGN_RETURN_1",
"SIGN_RETURN_2",
"SIGN_RETURN_3",
"NAME_SIGN_RETURN_1",
"NAME_SIGN_RETURN_2",
"NAME_SIGN_RETURN_3",
"CHECKING_STOCK",
"KASIR_LOGIN_TYPE",

};
  public static String[] systemPropsProchainNote = {
    "Property ini digunakan untuk insert saldo awal aktiva",
"OID of Book Type",
"Y = Auto Receive FA, N = Manual Receive FA",
"",
"String LOC_CODE is used for journal number",
"Nilai PPn secara Default",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"id standart rate yang di pergunakan",
"",
"YES= ada, NO=tidak tampil di penerimaan",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show",
"0 = hide, 1 = show, 2 = show pembelian, 3 = show penitipan",
"",
"",
"",
"",
"",
"Untuk integrasi antara currency type pos dengan hanoman ( ini harus sama dengan dengan code curency type)",
"",
"data untuk retail",
"Code untuk data credit",
"untuk harga shoping chart menggunakan price type tipe yang mana",
"",
"0=retail, 1=restaurant, 2 = jewelery",
"",
"",
"",
"",
"-",
"",
"",
"",
"",
"",
"0=Disable, 1=Enable",
"",
"",
"",
"0= retail, 2 = restaurant, 3 distributor",
"Ppn masukan di penerimaan, 0 = Biaya, 1 = Dikreditkan",
"",
"",
"",
"",
"",
"use sku or barcode in report, 0 = sku, 1 = barcode",
"CURRENCY AUD",
"untuk penamaan local currency",
"0 = use header, 1 not use header",
"0=use header, 1 = not use header",
"jika tidak menggunakan header, berapa tinggi header bill small",
"setting minimal per page",
"kalimat",
"",
"",
"",
"",
"",
"SIGN PO1",
"SIGN PO2",
"SIGN PO 3",
"SIGN PO4",
"",
"",
"",
"",
"Sign Kiri",
"Sign Tengah",
"Sign Kanan",
"",
"",
"0=average price, 1 = harga beli terakhir",
"",
"",
"",
"",
"",
"",
"",
"",
"set 0 = untuk bukan payment dinamis, 1 = untuk payment dinamis,",
"",
"RELEVANT DOC",
"0(MONTHLY), 1(THREE MONTHLY), 2(FOUR MONTHLY), 3(SIX MONTHLY), 4(ANNUAL)",
"Path of database",
"",
"",
"",
"",
"Barcode pada digit 0-3",
"barcode digit 4-21",
"digit 21 - 31",
"-",
"",
"menu penerimaan dan opname jadi bootrap style",
"1= static, 0= dinamic",
"type pencarian path folder untuk barcode, 0=dinamic, 1=static",
"",
"Price Type untuk tipe member VIP",
"Price Type untuk tipe member Wholesale",
"Price Type untuk tipe member Retail",
"Diskon untuk tipe member VIP",
"",
"Diskon untuk tipe member Retail",
"menggunakan login biasa atau login finger",
"0 = pake lisensi, 1 = lisensi seumur hidup",
"",
"",
"",
"",
"",
"setting use brand or no",
"COMPANY_NAME",
"",
"Property for company phone data",
"untuk pengganti nama merk",
"",
"",
"apakah menggunakan consigment atau tidak 0= tidak, 1 = ya",
"SHOW DAFTAR PERIODE",
"SHOW TIPE POTONGAN",
"SHOW TIPE CUSTOMER",
"SHOW POINT MEMBER",
"SHOW NILAI TUKAR",
"SHOW NILAI TUKAR",
"SHOW AKSESORIS",
"SHOW PRODUKSI",
"SHOW KOREKSI STOK",
"1=untuk menampilkan khusus fitur greenbowl 0=tidak ditampilkan",
"USE FOR RADITYA jika 1 berarti untuk raditya ",
"",
"",
"untuk setup up harga default",
"1=kalau mapping dengan table pos_sub_category, 0= kalau mapping dengan table pos_merk",
"",
"",
"",
"0=not using1 = using",
"0=default, oid default outlet",
"",
"oid dari item spesial food",
"",
"DEFAULT SUPPLIER",
"",
"",
"",
"",
"",
"0=tidak, 1=ya",
"",
"",
"",
"",
"",
"",
"",
"",
"Path data",
"Path data",
"Path data mysql/bin",
"",
"0=hide1=show",
"0=hide1=show",
"0=hide1=show",
"",
"",
"",
"",
"",
"0: DISABLE, 1: ENABLE",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"",
"0=boleh minus (minimarket), 1 = tidak boleh minus(RTC)",
"menggunakan login biasa atau login finger"
};
  public static int getSyspropPos(String sysPropName) {
    for (int i = 0; i < systemPropsProchain.length; i++) {
      if (systemPropsProchain[i].equalsIgnoreCase(sysPropName)) {
        return i;
      }
    }
    return -1;
  }

  public static String getSyspropPosNote(String sysPropName) {
    for (int i = 0; i < systemPropsProchain.length; i++) {
      if (systemPropsProchain[i].equalsIgnoreCase(sysPropName)) {
        return systemPropsProchainNote[i];
      }
    }
    return "";
  }
    /**
     *  static and permanent system property should be hard coded here
     */
    public static final String PROP_APPURL      =  "http://192.168.0.10:8080"; //own
	//public static final String PROP_APPURL      =  "http://192.168.0.16:8080";//server

    /**
     *  loadable properties are loaded here
     */    
  /*  public static String ADMIN_EMAIL        = "Not initialized";*/
 
    public static String PROP_IMGCACHE         = "";//"C:\\tomcat\\webapps\\prochain\\imgchace\\";
    public static String MATERIAL_PERIOD       = "0";//MATERIAL_PERIOD";
    public static String LOC_TRANS_ID          = "0";
    public static String NO_COLOR_ID           = "0";
    public static String FINISH_ID             = "0";
    public static String UNFINISH_ID           = "0";
    public static String TARITA_ID             = "0";
    public static String COST_GROUP_TARITA_ID  = "0";
    

    /** Creates new SessSystemProperty */
    public SessSystemProperty() {
        if(!loaded) {            
            boolean ok = loadFromDB();
            String okStr = "OK";
            if(!ok) okStr = "FAILED";
            System.out.println("Loading system proerties ............................. ["+ okStr +"]");
            loaded = true;
        }
    } 
    
    
    public boolean loadFromDB() {
        try {

            PstSystemProperty.loadFromDbToHashNew(systemPropsProchain);
            PROP_IMGCACHE    = PstSystemProperty.getValueByName("PROP_IMGCACHE");
            LOC_TRANS_ID	 = PstSystemProperty.getValueByName("LOC_TRANS_ID");
            NO_COLOR_ID      = PstSystemProperty.getValueByName("NO_COLOR_ID");
            FINISH_ID      = PstSystemProperty.getValueByName("FINISH_PACKED_ID");
            UNFINISH_ID     = PstSystemProperty.getValueByName("UNFINISH_ID");
            //TARITA_ID     = PstSystemProperty.getValueByName("TARITA_ID");
            TARITA_ID     = PstSystemProperty.getValueByName("COMPANY_ID");
            //COST_GROUP_TARITA_ID = PstSystemProperty.getValueByName("COST_GROUP_TARITA_ID");
            COST_GROUP_TARITA_ID = PstSystemProperty.getValueByName("COST_GROUP_COMPANY_ID");
            
            return true;
        }catch(Exception e) {
            return false;
        }
    }
    
    
    
    public static void main(String[] args) {
        SessSystemProperty prop = new SessSystemProperty();
    }


}
