/*
 * DSJ_CashierConfig.java
 *
 */

package com.dimata.pos.xmlparser;


import java.util.Hashtable; 
/**
 *
 * @author  wpulantara
 * @version 
 */
public class DSJ_CashierConfig {

    public DSJ_CashierConfig() {
    }

    public String master="";
    public String company="";
    public String address="";
    public int salesentry = 0;
    public String currencyId = "0";  
    
    public int enableMembership = 0;
    public int enableOpenBill = 0;
    public int enablePendingOrder = 0;
    public int enableCreditSales = 0;
    public int enableCreditPayment = 0;
    public int enableGiftTrans=0; 
    public int enableService=0;
    public int enableTax=0;
    
    public String nonMemberId = "0";
    public String fordate = "MMM-dd-yyyy";
    public String fordecimal = "###,###.##";
    public String forcurrency = "###,###.00";
    public int statusMsg =0;
    public String msg1=""; 
    public int useMsg1 =0;
    public String msg2=""; 
    public int useMsg2 =0;
    public String msg3=""; 
    public int useMsg3 =0;  
    public String msg4=""; 
    public int useMsg4 =0;

    public String cashierNumber = "0"; 
    public String cashierService = "0"; 
    public String cashierTax = "0"; 
    public String locationId = "0"; 
    public String giftLocationId = "0";
    
    public int netTransPriority = 0; 
    public int language = 0;
    
    public String defaultCurrencyCode = "";
    public String nonMemberCode = ""; 
    public String memberCode = ""; // hanoman only
    
    public int integrationType = 0;
    public int dataPublishMethod = 0; 
    
    public Hashtable currencyCodeMap = new Hashtable(); 
    
    public int searchMethode = 0;
    
    public int printingGap = 0;
    
    public int autoNewSale = 0;
    
    /**================
     * CREDIT CARD COST
       ================*/
    public int enableCardCost = 0;
    public String cardCost = "0";
    public String paymentTypeCardCost = "0";
    
    /** ================
     *  OTHER COST
     *  ================
     */
    public int enableOtherCost = 0;
    
    /** ===============
     *  CONTACT INPUT
     *  ===============
     */
    public int enableContactInput = 0;
    public int enablePriceMapSelect = 0;
    public String priceMapId = "0";
    
    /** =======================
     *  PRICE CURRENCY SELECT 
     *  =======================
     */
    public int enablePriceCurrSelect = 0;
    
    /** ======================= 
     *  ENABLE LOCATION SELECT
     *  =======================
     */
    public int enableLocationSelect = 0;
    
    /** ========================
     *  EXCHANGE RATE UPDATE 
     *  ========================
     */
    public int enableRateUpdate = 0;
    
    /** ========================
     *  USING BIG INVOICE
     *  ========================
     */
    public int usingBigInvoice = 0;
    
    /** =====================================
     *  ADDITIONAL ATTRIBUTE FOR BIG INVOICE  
     *  =====================================
     */
    public String ext1 = "";
    public String ext2 = "";
    public String ext3 = "";
    public String ext4 = "";
    public String ext5 = "";
    public String ext6 = "";
    public String ext7 = "";
    public int itemPerPage = 25;
    
    /** ======================
     *  PRODUCT IMAGE
     *  ======================
     */
    public int enableProductImage = 0;
    
    /** ====================================
     *  FILE EXTENSION USE IN PRODUCT IMAGE
     *  eg: jpg, gif, JPG, GIF, bmp, BMP
     *  ====================================
     */
    
    public String imageEx = "";
    
    /** ========================
     *  USING PRODUCT COLOR
     *  ========================
     */
    public int usingProductColor = 0;
 
    /** ===================
     *  ENABLE PRICE EDIT
     *  ===================
     */
    
    public int enablePriceEdit = 0;
    
    /** ================
     *  FLOATING UNIT
     *  ================
     */
    
    public String floatingUnit = "";  
    
    /** ================
     *  FIX UNIT
     *  ================
     */
    
    public String fixUnit = "";  
    
    /** 
     * ENABLE HALF INVOICE
     */
    public int enableHalfInvoice = 0;
    
}
