/*
 * I_IJGeneral.java
 *
 * Created on April 17, 2005, 4:45 PM
 */
package com.dimata.ij;

/**
 *
 * @author Administrator
 * @version
 */
public interface I_IJGeneral {

  // ABSOLUT VALUE

  public static final double ABS_DOUBLE_VALUE_NOL = 0.0000000001d;

  // transaction cash direction realtively to company
  public final static int CASH_DIRECTION_IN = 0;
  public final static int CASH_DIRECTION_OUT = 1;
  // BO System
  public static final int BO_PROCHAIN_MANUFACTURE = 0;
  public static final int BO_PROCHAIN_POS = 1;
  public static final int BO_PROCHAIN_HANOMAN = 2;
  public static final int BO_SEDANA = 3;
  public static final String[] strBoSystem
          = {
            "ProChain Manufacturer",
            "ProChain POS",
            "Hanoman",
            "Dimata Sedana"
          };

  public static final int MAP_TYPE_PAYMENT = 0;
  public static final int MAP_TYPE_ACCOUNT = 1;
  public static final int MAP_TYPE_LOCATION = 2;

  public static final String[] strMapType
          = {
            "Payment",
            "Account",
            "Location"
          };

  public static final String[][] strExtraLocation = {
    {},
    {},
    {"- ROOM -",
      "-TELEPHONE -"}
  };

  public static final long EXTRA_LOCATION_OID_ROOM = Long.MAX_VALUE - 1;
  public static final long EXTRA_LOCATION_OID_TELEPHONE = EXTRA_LOCATION_OID_ROOM - 1;

  public static final int EXTRA_LOCATION_INDEX_ROOM = 0;
  public static final int EXTRA_LOCATION_INDEX_TELEPHONE = 1;

  public static final long[][] oidExtraLocation = {
    {},
    {},
    {EXTRA_LOCATION_OID_ROOM,
      EXTRA_LOCATION_OID_TELEPHONE}
  };

  // configuration index for PAYMENT
  public static final int CFG_GRP_PAY = 0;
  public static final int CFG_PAY_DP_PI = 0;
  public static final int CFG_PAY_DP_PI_A_P = 0;
  public static final int CFG_PAY_DP_PI_SALES = 1;

  // configuration index for INVENTORY
  public static final int CFG_GRP_INV = 1;
  public static final int CFG_GRP_INV_STORE = 0;
  public static final int CFG_GRP_INV_STORE_DIRECT_OUT = 0;
  public static final int CFG_GRP_INV_STORE_AFTER_REC = 1;

  // configuration index for TAX
  public static final int CFG_GRP_TAX = 2;
  public static final int CFG_GRP_TAX_SALES = 0;
  public static final int CFG_GRP_TAX_SALES_VAT_RPT = 0;
  public static final int CFG_GRP_TAX_SALES_VAT_NO_RPT = 1;
  public static final int CFG_GRP_TAX_SALES_NO_VAT = 2;

  public static final int CFG_GRP_TAX_BUY = 1;
  public static final int CFG_GRP_TAX_BUY_VAT_RPT = 0;
  public static final int CFG_GRP_TAX_BUY_NO_VAT = 1;

  // configuration index for BOOK TYPE            
  public static final int CFG_GRP_BOOK_TYPE = 3;
  public static final int CFG_GRP_BOOK_TYPE_1 = 0;
  public static final int CFG_GRP_BOOK_1_TYPE_RUPIAH = 0;
  public static final int CFG_GRP_BOOK_1_TYPE_DOLLAR = 1;

  // configuration index for SYSTEM                        
  public static final int CFG_GRP_SYS = 4;
  public static final int CFG_GRP_SYS_IJ_ENG = 0;
  public static final int CFG_GRP_SYS_IJ_ENG_INTERACTIVE = 0;
  public static final int CFG_GRP_SYS_IJ_ENG_FULL_AUTO = 1;

  // configuration index for Payment on Outlet Bill                        
  public static final int CFG_GRP_PAY_OUTLET = 5;
  public static final int CFG_GRP_PAY_OUTLET_CREDIT = 0;
  public static final int CFG_GRP_PAY_OUTLET_CREDIT_ENABLE = 0;
  public static final int CFG_GRP_PAY_OUTLET_CREDIT_DISABLE = 1;

  // Configuration Group
  public static final String[] strConfigGroup
          = {
            "Payment",
            "Inventory",
            "TAX",
            "Book Type",
            "System",
            "Payment on Outlet Bill"
          };

  // Configuration Item
  public static final String[][] strConfigItem
          = {
            {
              "DP on Proforma Invoice / Pending Order"
            },
            {
              "Inventory on Store Sales/Invoice"
            },
            {
              "TAX on Sales",
              "TAX on Buying"
            },
            {
              "Book Type"
            },
            {
              "Journal Engine"
            },
            {
              "Credit Payment"
            }
          };

  // Configuration Select
  public static final String[][][] strConfigSelect
          = {
            {
              {
                "Income Received in Advance (A/P)",
                "Direct as Sales Income"
              }
            },
            {
              {
                "Inventory out after invoicing",
                "Inventory out after customer received"
              }
            },
            {
              {
                "VAT Report",
                "VAT but no report",
                "No VAT"
              },
              {
                "VAT Report",
                "VAT to Merchandise Basic Cost"
              }
            },
            {
              {
                "Book Type Rupiah",
                "Book Type Dollar"
              }
            },
            {
              {
                "Interactive",
                "Fully Automatic"
              }
            },
            {
              {
                "Enable",
                "Disable"
              }
            }
          };

  // Configuration Select
  public static final String[][][] strConfigSelectValue
          = {
            {
              {
                "0",//"Income Received in Advance (A/P)",
                "1"//Direct as Sales Income"
              }
            },
            {
              {
                "0",//Inventory out after invoicing", 
                "1"//Inventory out after customer received"           
              }
            },
            {
              {
                "0",//VAT Report", 
                "1",//VAT but no report", 
                "2"//No VAT"           
              },
              {
                "0",//VAT Report", 
                "1"//VAT to Merchandise Basic Cost"
              }
            },
            {
              {
                "1",//Book Type Rupiah", 
                "2"//Book Type Dollar"           
              }
            },
            {
              {
                "0",//"Interactive",
                "1"//Fully Automatic"           
              }
            },
            {
              {
                "0",//"Enable",
                "1"// Disable"           
              }
            }

          };

  /**
   * declaration of identifier to handle index of each payment status
   */
  public static final int PAYMENT_STATUS_NOT_POSTED = 0;
  public static final int PAYMENT_STATUS_POSTED_NOT_CLEARED = 1;
  public static final int PAYMENT_STATUS_POSTED_CLEARED = 2;
  public static final int PAYMENT_STATUS_POSTED_CLOSED = 3;

  /**
   * declaration of identifier to explain payment status above
   */
  public static final String[] fieldPaymentStatus = {
    "Not Posted",
    "Posted But Not Cleared",
    "Posted Cleared",
    "Posted Closed"
  };

  /**
   * declaration of identifier to handle index of each result code
   */
  public static final int RESULT_CODE_OK = 0;
  public static final int RESULT_CODE_ERROR = 1;

  /**
   * declaration of identifier to explain result code above
   */
  public static final String[] fieldResultCode = {
    "Ok",
    "Error"
  };

  /**
   * declaration of identifier to handle index of each transaction type
   */
  public static final int TRANS_DP_ON_SALES_ORDER = 0;
  public static final int TRANS_DP_ON_PURCHASE_ORDER = 1;
  public static final int TRANS_DP_ON_PRODUCTION_ORDER = 2;
  public static final int TRANS_GOODS_RECEIVE = 3;
  public static final int TRANS_TAX_ON_BUYING = 4;
  public static final int TRANS_SALES = 5;
  public static final int TRANS_SALES_DISCOUNT = 6;
  public static final int TRANS_COGS = 7;
  public static final int TRANS_OTHER_COST_ON_INVOICING = 8;
  public static final int TRANS_TAX_ON_SELLING = 9;
  public static final int TRANS_INVENTORY_LOCATION = 10;
  public static final int TRANS_WIP = 11;
  public static final int TRANS_PURCHASE_DISCOUNT = 12;
  public static final int TRANS_PROD_COST_DISCOUNT = 13;
  public static final int TRANS_CUSTOMER_RETURN = 14;
  public static final int TRANS_SUPPLIER_RETURN = 15;
  public static final int TRANS_DF_TO_DEPARTMENT = 16;
  public static final int TRANS_LGR_FROM_DEPARTMENT = 17;
  public static final int TRANS_EARN_CORRECTION_LAST_PER = 18;
  public static final int TRANS_CHARGE = 19;
  public static final int TRANS_COMMISION = 20;
  public static final int TRANS_COSTING = 21;
  public static final int TRANS_SERVICE_CHARGE = 22;
  public static final int TRANS_SALES_CANCEL_CHARGE = 23;
  public static final int TRANS_SALES_EXTRA_REVENUE = 24;
  public static final int TRANS_CREDIT_SALES = 25;
  public static final int TRANS_CREDIT_SALES_ADJUST_MIN = 26;
  public static final int TRANS_CREDIT_SALES_ADJUST_PLUS = 27;
  public static final int TRANS_CITY_LEDGER_TRAVEL = 28;
  public static final int TRANS_CITY_LEDGER_CORPORATE = 29;
  public static final int TRANS_CITY_LEDGER_DOT_COM = 30;
  public static final int TRANS_CITY_LEDGER_GOVERMENT = 31;
  public static final int TRANS_CITY_LEDGER_PERSONAL = 32;
  public static final int TRANS_COMMISSION_AP = 33;
  public static final int TRANS_INVENTORY_IN_TRANSIT = 34;
  public static final int TRANS_COMPLIMENTARY_COST = 35;
  public static final int TRANS_PURCHASE_FORWARDER_COST = 36;
  public static final int TRANS_CUSTOMER_SAVINGS = 37;
  public static final int TRANS_CUSTOMER_SAVINGS_REVENUE = 38;
  public static final int TRANS_CUSTOMER_SAVINGS_COST = 39;
  public static final int TRANS_CUSTOMER_SAVINGS_TAX = 40;
  public static final int TRANS_CUSTOMER_SAVINGS_AR = 41;  // account receivable 
  public static final int TRANS_CUSTOMER_SAVINGS_AP = 42;  // account payable
  public static final int TRANS_CUSTOMER_SAVINGS_OTHERS = 43;
  public static final int TRANS_LOAN_CREDIT = 44;
  public static final int TRANS_LOAN_CREDIT_REVENUE = 45;
  public static final int TRANS_LOAN_CREDIT_COST = 46;
  public static final int TRANS_LOAN_CREDIT_TAX = 47;  // account receivable 
  public static final int TRANS_LOAN_CREDIT_AR = 48;  // account payable
  public static final int TRANS_LOAN_CREDIT_AP = 49;
  public static final int TRANS_LOAN_CREDIT_OTHERS = 50;
  public static final int TRANS_CUSTOMER_DEPOSIT = 51;
  public static final int TRANS_CUSTOMER_DEPOSIT_REVENUE = 52;
  public static final int TRANS_CUSTOMER_DEPOSIT_COST = 53;
  public static final int TRANS_CUSTOMER_DEPOSIT_TAX = 54;
  public static final int TRANS_CUSTOMER_DEPOSIT_AR = 55;
  public static final int TRANS_CUSTOMER_DEPOSIT_AP = 56;
  public static final int TRANS_CUSTOMER_DEPOSIT_OTHERS = 57;
  public static final int TRANS_CUSTOMER_SAVING_INTEREST = 58;
  public static final int TRANS_CUSTOMER_SAVING_INTEREST_AP = 59;
  public static final int TRANS_CUSTOMER_SAVING_INTEREST_COST = 60;
  public static final int TRANS_LOAN_CREDIT_SEDANA_PENALTY_RECORD = 61;
  public static final int TRANS_LOAN_CREDIT_SEDANA_PAYMENT = 62;
  public static final int TRANS_SALES_REVENUE_OF_SALES_OVER_PAYMENT = 63;
  public static final int TRANS_SALES_COST_OF_SALES_UNDER_PAYMENT = 64;

  /**
   * declaration of identifier to explain result transaction type above
   */
  public static final String[] strTransactionType = {
    "DP on Sales Order",
    "DP on Purchase Order",
    "DP on Production Order",
    "Goods Receive (LGR)",
    "TAX on Buying",
    "Sales (Invoicing)/Sales Return",
    "Sales Discount",
    "Cost of Goods Sold (Non Composit)",
    "Other Cost of Sales",
    "TAX on Selling", //9
    "Inventory Location",
    "Work In Process",
    "Purchasing Discount",
    "Production Cost Discount",
    "Sales Return (Special CoA)",
    "Purchase/Supplier Return",
    "Internal Inventory Transfer",
    "Internal Inventory Receive",
    "Revenue Correction",
    "Transaction Charge   (Cost)",//19
    "Transaction Commision(Cost)", // 20
    "Company Cost/Expense/ CoGS of Composit",
    "Service Charge of Sales",
    "Sales Cancellation Charge (Revenue)", // room cancellation
    "Sales Extra Revenue", // e.g : extra bed on Hanoman
    "Credit Sales",
    "Credit Sales Adjustment(-)",
    "Credit Sales Adjustment(+)",
    "City Ledger - Travel",
    "City Ledger - Corporate",
    "City Ledger - .Com",
    "City Ledger - Goverment",
    "City Ledger - Personal Guest",
    "Commision Account Payable(A/P)",
    "Inventory in Transit",
    "Complimentary Cost",
    "Purchase Forwarder Cost",
    "Customer Saving",
    "Customer Saving Revenue",
    "Customer Saving Cost",
    "Customer Saving Tax",
    "Customer Saving AR",
    "Customer Saving AP",
    "Customer Saving Others",
    "Loan and Credit", // 44
    "Loan and Credit Revenue",
    "Loan and Credit Cost",
    "Loan and Credit Tax",
    "Loan and Credit AR",
    "Loan and Credit AP",
    "Loan and Credit Others",
    "Customer Deposit",
    "Customer Deposit Revenue",
    "Customer Deposit Cost",
    "Customer Deposit AR",
    "Customer Deposit AP",
    "Customer Deposit Others",
    "Customer Deposit Others",
    "Customer Saving Interest",
    "Customer Saving Interest AP",
    "Customer Saving Interest Cost",
    "Trans Loan Credit Sedana Penalty Record",
    "Trans Loan Credit Sedana Payment",
    "Revenue of Sales Over Payment",
    "Revenue of Sales Under Payment"
  };

  public static final String[] strTransactionTypeBahasa = {
    "DP Order Penjualan", //0
    "DP Order Pembelian", //1
    "DP Order Produksi", //2
    "Goods Receive (LGR)", //3
    "TAX on Buying", //4
    "Sales (Invoicing)/Sales Return", //5
    "Sales Discount", //6
    "Cost of Goods Sold (Non Composit)", //7
    "Other Cost of Sales", //8
    "TAX on Selling", //9
    "Inventory Location", //10
    "Work In Process", //11
    "Purchasing Discount", //12
    "Production Cost Discount", //13
    "Sales Return (Special CoA)", //14
    "Purchase/Supplier Return", //15
    "Internal Inventory Transfer", //16
    "Internal Inventory Receive", //17
    "Revenue Correction", //18
    "Transaction Charge   (Cost)",//19
    "Transaction Commision(Cost)", // 20
    "Company Cost/Expense/ CoGS of Composit", //21
    "Service Charge of Sales", //22
    "Sales Cancellation Charge (Revenue)", // 23 room cancellation
    "Sales Extra Revenue", // 24 e.g : extra bed on Hanoman
    "Credit Sales", //25
    "Credit Sales Adjustment(-)", //26
    "Credit Sales Adjustment(+)", //27
    "City Ledger - Travel", //28
    "City Ledger - Corporate", //29
    "City Ledger - .Com", //30
    "City Ledger - Goverment", //31        
    "City Ledger - Personal Guest", //32
    "Commision Account Payable(A/P)", //33
    "Inventory in Transit", //34
    "Complimentary Cost", //35
    "Purchase Forwarder Cost", //36
    "Tabungan Nasabah", //37
    "Tabungan Nasabah - Pendapatan", //38
    "Tabungan Nasabah - Biaya", //39
    "Tabungan Nasabah - Pajak", //40
    "Tabungan Nasabah - Piutang", //41
    "Tabungan Nasabah - Hutang", //42
    "Tabungan Nasabah - Lainnya", //43
    "Kredit & Pinjaman", //44
    "Kredit & Pinjaman - Pendapatan", //45
    "Kredit & Pinjaman - Biaya", //46
    "Kredit & Pinjaman - Pajak", //47
    "Kredit & Pinjaman - Piutang", //48
    "Kredit & Pinjaman - Hutang", //49
    "Kredit & Pinjaman - Lainnya", //50
    "Deposito Nasabah", //51
    "Deposito Nasabah - Pendapatan", //52
    "Deposito Nasabah - Biaya", //53
    "Deposito Nasabah - Pajak", //54
    "Deposito Nasabah - Piutang", //55
    "Deposito Nasabah - Hutang", //56
    "Deposito Nasabah - Lainnya", //57
    "Tabungan Nasabah - Bunga",
    "Tabungan Nasabah - Hutang Bunga",
    "Tabungan Nasabah - Biaya Bunga",
    "Kredit - Sedana Pencatatan Denda",
    "Kredit - Sedana Pembayaran",
    "Pendapat Penjualan Pembayaran Lebih",
    "Beban Penjualan Pembayaran Kurang"
  };

  public static final String strTransactionTypeLang[][] = {
    strTransactionTypeBahasa,
    strTransactionType
  };

  public static final boolean[][] enableTransactionTypeByBo = { /*[Bo System Index][] */
    // BO_PROCHAIN_MANUFACTURE = 0;
    {true,//"DP on Sales Order",
      true,//"DP on Purchase Order",
      true,//"DP on Production Order",
      true,//"Goods Receive (LGR)",
      true,//"TAX on Buying",
      true,//"Sales (Invoicing)",
      true,//"Sales Discount",
      true,//"Cost of Goods Sold (Non Composit)",
      true,//"Other Cost of Sales",
      true,//"TAX on Selling", //9
      true,//"Inventory Location",
      true,//"Work In Process",
      true,//"Purchasing Discount",
      true,//"Production Cost Discount",
      true,//"Sales/Customer Return",
      true,//"Purchase/Supplier Return",
      true,//"Internal Inventory Transfer",
      true,//"Internal Inventory Receive",
      true,//"Revenue Correction",
      true,//"Transaction Charge   (Cost)",//19
      true,//"Transaction Commision(Cost)", // 20
      true,//"Company Cost/Expense/ CoGS of Composit",
      false,//"Service Charge of Sales"
      true,//"Sales Cancellation Charge (Revenue)", // room cancellation
      true,//"Sales Extra Revenue", // e.g : extra bed on Hanoman
      true,//"Credit Sales",
      false,//"Credit Sales Adjustment(-)",
      false,//"Credit Sales Adjustment(+)",
      false,//"City Ledger - Travel",
      false,//"City Ledger - Corporate",
      false,//"City Ledger - .Com",
      false,//"City Ledger - Goverment",        
      false,//"City Ledger - Personal Guest",
      false,//"Commision Account Payable(A/P)",
      true,//"Inventory in Transit",
      true, //"Complimentary Cost",
      true,//"Purchase Forwarder Cost",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //61
      false, //62
      true,
      true,
      true,
      true,
  },
    //BO_PROCHAIN_POS = 1;
    {true,//"DP on Sales Order",
      true,//"DP on Purchase Order",
      true,//"DP on Production Order",
      true,//"Goods Receive (LGR)",
      true,//"TAX on Buying",
      true,//"Sales (Invoicing)",
      true,//"Sales Discount",
      true,//"Cost of Goods Sold (Non Composit)",
      true,//"Other Cost of Sales",
      true,//"TAX on Selling", //9
      true,//"Inventory Location",
      true,//"Work In Process",
      true,//"Purchasing Discount",
      true,//"Production Cost Discount",
      true,//"Sales/Customer Return",
      true,//"Purchase/Supplier Return",
      true,//"Internal Inventory Transfer",
      true,//"Internal Inventory Receive",
      true,//"Revenue Correction",
      true,//"Transaction Charge   (Cost)",//19
      true,//"Transaction Commision(Cost)", // 20
      true,//"Company Cost/Expense/ CoGS of Composit",
      false,//"Service Charge of Sales",
      true,//"Sales Cancellation Charge (Revenue)", // room cancellation
      true,//"Sales Extra Revenue", // e.g : extra bed on Hanoman
      true,//"Credit Sales",
      false,//"Credit Sales Adjustment(-)",
      false,//"Credit Sales Adjustment(+)",
      false,//"City Ledger - Travel",
      false,//"City Ledger - Corporate",
      false,//"City Ledger - .Com",
      false,//"City Ledger - Goverment",        
      false,//"City Ledger - Personal Guest",
      false,//"Commision Account Payable(A/P)",
      true,//"Inventory in Transit",
      true, //"Complimentary Cost",
      true,//"Purchase Forwarder Cost",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"  
      false, //61
      false, //62
      true,
      true,
      true,
      true,
  },
    //BO_PROCHAIN_HANOMAN = 2;
    {true,//"DP on Sales Order",
      false,//"DP on Purchase Order",
      false,//"DP on Production Order",
      false,//"Goods Receive (LGR)",
      false,//"TAX on Buying",
      true,//"Sales (Invoicing)",
      true,//"Sales Discount",
      false,//"Cost of Goods Sold (Non Composit)",
      true,//"Other Cost of Sales",
      true,//"TAX on Selling", //9
      false,//"Inventory Location",
      false,//"Work In Process",
      false,//"Purchasing Discount",
      false,//"Production Cost Discount",
      false,//"Sales/Customer Return",
      false,//"Purchase/Supplier Return",
      false,//"Internal Inventory Transfer",
      false,//"Internal Inventory Receive",
      true,//"Revenue Correction",
      true,//"Transaction Charge   (Cost)",//19
      true,//"Transaction Commision(Cost)", // 20
      false,//"Company Cost/Expense/ CoGS of Composit",
      true,//"Service Charge of Sales", , -opie-eyek 20160203
      true,//"Sales Cancellation Charge (Revenue)", // room cancellation
      true,//"Sales Extra Revenue", // e.g : extra bed on Hanoman
      true,//"Credit Sales",
      true,//"Credit Sales Adjustment(-)",
      true,//"Credit Sales Adjustment(+)",
      true,//"City Ledger - Travel",
      true,//"City Ledger - Corporate",
      true,//"City Ledger - .Com",
      true,//"City Ledger - Goverment",        
      true,//"City Ledger - Personal Guest",
      true,//"Commision Account Payable(A/P)",
      false,//"Inventory in Transit",
      false, //"Complimentary Cost",
      false,//"Purchase Forwarder Cost",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false, //"Customer Saving",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false,//"Revenue of Saving and Loan",
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //"Cost of Saving and Loan"
      false, //61
      false, //62
      true,
      true,
      true,
      true,
  },
    //BO_SEDANA = 2;
    {false,//"DP on Sales Order",
      false,//"DP on Purchase Order",
      false,//"DP on Production Order",
      false,//"Goods Receive (LGR)",
      false,//"TAX on Buying",
      false,//"Sales (Invoicing)",
      false,//"Sales Discount",
      false,//"Cost of Goods Sold (Non Composit)",
      false,//"Other Cost of Sales",
      false,//"TAX on Selling", //9
      false,//"Inventory Location",
      false,//"Work In Process",
      false,//"Purchasing Discount",
      false,//"Production Cost Discount",
      false,//"Sales/Customer Return",
      false,//"Purchase/Supplier Return",
      false,//"Internal Inventory Transfer",
      false,//"Internal Inventory Receive",
      false,//"Revenue Correction",
      false,//"Transaction Charge   (Cost)",//19
      false,//"Transaction Commision(Cost)", // 20
      false,//"Company Cost/Expense/ CoGS of Composit",
      false,//"Service Charge of Sales", , -opie-eyek 20160203
      false,//"Sales Cancellation Charge (Revenue)", // room cancellation
      false,//"Sales Extra Revenue", // e.g : extra bed on Hanoman
      false,//"Credit Sales",
      false,//"Credit Sales Adjustment(-)",
      false,//"Credit Sales Adjustment(+)",
      false,//"City Ledger - Travel",
      false,//"City Ledger - Corporate",
      false,//"City Ledger - .Com",
      false,//"City Ledger - Goverment",        
      false,//"City Ledger - Personal Guest",
      false,//"Commision Account Payable(A/P)",
      false,//"Inventory in Transit",
      false, //"Complimentary Cost",
      false,//"Purchase Forwarder Cost",
      true, //"Customer Saving",
      true, //"Customer Saving",
      true, //"Customer Saving",
      true, //"Customer Saving",
      true, //"Customer Saving",
      true, //"Customer Saving",
      true, //"Customer Saving",
      true, //"Revenue of Saving and Loan",
      true, //"Revenue of Saving and Loan",
      true, //"Revenue of Saving and Loan",
      true, //"Revenue of Saving and Loan",
      true, //"Revenue of Saving and Loan",
      true, //"Revenue of Saving and Loan",
      true, //"Revenue of Saving and Loan",
      true, //"Cost of Saving and Loan"
      true, //"Cost of Saving and Loan"
      true, //"Cost of Saving and Loan"
      true, //"Cost of Saving and Loan"
      true, //"Cost of Saving and Loan"
      true, //"Cost of Saving and Loan"
      true, //"Cost of Saving and Loan"
      false, //61
      false, //62
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      true,
    }

  };

  public static final int SYS_PROP_CITY_LEDGER_TRAVEL = 0;
  public static final int SYS_PROP_CITY_LEDGER_CORPORATE = 1;
  public static final int SYS_PROP_CITY_LEDGER_DOT_COM = 2;
  public static final int SYS_PROP_CITY_LEDGER_GOVERMENT = 3;
  public static final int SYS_PROP_CITY_LEDGER_PERSONAL_GUEST = 4;

  public static final String SYS_PROP_CITY_LEDGER[] = {
    "TRAVEL_AGENT", //  TRAVEL AGENT OID ON MASTER TYPE
    "CORPORATE", //  CORPORATE OID ON MASTER TYPE,
    "DOT_COM", //  master type id of .com company
    "GOVERMENT", //  GOVERMENT OID ON MASTER TYPE,
    "WALK_IN", // PERSONAL OID MASTER
  };

  /**
   * declaration of identifier to handle sale type on back office system
   */
  public static final int SALE_TYPE_REGULAR = 0;
  public static final int SALE_TYPE_CONSIGNMENT = 1;
  public static final int SALE_TYPE_PERSONAL = 2;
  public static final int SALE_ROOM_REVENUE = 3;
  public static final int SALE_ROOM_REV_NO_SHOW = 4;
  public static final int SALE_ROOM_REV_EXTRA_BED = 5;
  public static final int SALE_ROOM_REV_ADD_RATE = 6;
  public static final int SALE_ROOM_REV_SURCH_COMP = 7;
  public static final int SALE_ROOM_DISCOUNT = 8;
  public static final int SALE_ROOM_SPEC_DISCOUNT = 9;
  public static final int SALE_ROOM_ADJUSTMENT_PLUS = 10;
  public static final int SALE_ROOM_ADJUSTMENT_MIN = 11;
  public static final int SALE_ROOM_GUIDE_COMMISION = 12;
  public static final int SALE_ROOM_MARKETING_FEE = 13;
  public static final int SALE_ROOM_MANAGEMENT_FEE = 14;
  public static final int SALE_OUTLET_INCLD_BREAKFST = 15;
  public static final int SALE_SERVICE_CHRG = 16;
  public static final int SALE_CANCEL_FEE = 17;
  public static final int SALE_TELEPHONE_LOCAL = 18;
  public static final int SALE_TELEPHONE_INTERLOCAL = 20;
  public static final int SALE_TELEPHONE_INTERNATIONAL = 19;
  public static final int SALE_TELEPHONE_HP_NATIONAL = 20;
  public static final int SALE_TELEPHONE_HP_INTERNATIONAL = 21;
  public static final int SALE_TELEPHONE_OTHER = 22;

  public static final int[] idxSaleType = {
    SALE_TYPE_REGULAR,
    SALE_TYPE_CONSIGNMENT,
    SALE_TYPE_PERSONAL,
    SALE_ROOM_REVENUE,
    SALE_ROOM_REV_NO_SHOW,
    SALE_ROOM_REV_EXTRA_BED,
    SALE_ROOM_REV_ADD_RATE,
    SALE_ROOM_REV_SURCH_COMP,
    SALE_ROOM_DISCOUNT,
    SALE_ROOM_SPEC_DISCOUNT,
    SALE_ROOM_ADJUSTMENT_PLUS,
    SALE_ROOM_ADJUSTMENT_MIN,
    SALE_ROOM_GUIDE_COMMISION,
    SALE_ROOM_MARKETING_FEE,
    SALE_ROOM_MANAGEMENT_FEE,
    SALE_OUTLET_INCLD_BREAKFST,
    SALE_SERVICE_CHRG,
    SALE_CANCEL_FEE,
    SALE_TELEPHONE_LOCAL,
    SALE_TELEPHONE_INTERLOCAL,
    SALE_TELEPHONE_INTERNATIONAL,
    SALE_TELEPHONE_HP_NATIONAL,
    SALE_TELEPHONE_HP_INTERNATIONAL,
    SALE_TELEPHONE_OTHER
  };

  /**
   * declaration of identifier to explain sale type above
   */
  public static final String[] fieldSaleType = {
    "Outlet Regular",
    "Outlet Consignment",
    "Outlet Personal",
    "Room Revenue",
    "Room Revenue No Show",
    "Room Revenue Extra Bed",
    "Room Revenue day use/add. night",
    "Room Revenue Surcharge&Compulsary",
    "Room Discount (Prior to C-In)",
    "Room Special Discount",
    "Room Adjustment (plus)",
    "Room Adjustment (minus)",
    "Room Guide Commission",
    "Room Marketing Fee",
    "Room Management Fee",
    "Outlet Included Breakfast",
    "Service Charge",
    "Cancelation Fee",
    "Telephone Rev. Local",
    "Telephone Rev. Interlocal",
    "Telephone Rev. International",
    "Telephone Rev. Local HP",
    "Telephone Rev. International HP",
    "Telephone Rev. Other"
  };

  /**
   * declaration of identifier to handle DF type on back office system
   */
  public static final int DF_TYPE_WAREHOUSE = 0;
  public static final int DF_TYPE_PRODUCTION = 1;

  /**
   * declaration of identifier to explain DF type above
   */
  public static final String[] fieldDfType = {
    "DF To Warehouse",
    "DF To Production"
  };
  
  /**
   * declaration of identifier to handle Prod type on back office system
   */
  public static final int PROD_TYPE_COST = 0;
  public static final int PROD_TYPE_PRODUCT = 1;

  /**
   * declaration of identifier to explain DF type above
   */
  public static final String[] fieldProdType = {
    "Cost",
    "Product"
  };

  /**
   * declaration of identifier to handle transaction type on back office system
   */
  public static final int TRANSACTION_TYPE_CASH = 0;
  public static final int TRANSACTION_TYPE_CREDIT = 1;
  public static final int TRANSACTION_TYPE_COMLIMENT = 2;

  /**
   * declaration of identifier to explain transaction type above
   */
  public static final String[] fieldTransactionType = {
    "Cash",
    "Credit",
    "Compliment"
  };

  /**
   * declaration of identifier to handle document type on back office system
   * that referred by generated journal
   */
  public static final int DOC_TYPE_DP_ON_SALES_ORDER = 0; //
  public static final int DOC_TYPE_DP_ON_PURCHASE_ORDER = 1; //
  public static final int DOC_TYPE_DP_ON_PRODUCTION_ORDER = 2; //
  public static final int DOC_TYPE_PURCHASE_ON_LGR = 3; //
  public static final int DOC_TYPE_PROD_COST_ON_LGR = 4;
  public static final int DOC_TYPE_INVENTORY_ON_DF = 5;
  public static final int DOC_TYPE_SALES_ON_INV = 6;
  public static final int DOC_TYPE_PAYMENT_ON_LGR = 7;
  public static final int DOC_TYPE_PAYMENT_ON_INV = 8;
  public static final int DOC_TYPE_PAYMENT_TYPE = 9;
  public static final int DOC_TYPE_PAYMENT_TYPE_POSTED_CLEARED = 10;
  public static final int DOC_TYPE_CLOSING_CREDIT_SALES = 11;
  public static final int DOC_TYPE_SALES_COMMISSION = 12;
  public static final int DOC_TYPE_SUPLIER_RETURN = 13;
  public static final int DOC_TYPE_INVENTORY_ON_LGR = 14;
  public static final int DOC_TYPE_INVENTORY_ON_COST = 15;
  public static final int DOC_TYPE_COMPLIMENT_COST = 16;
  public static final int DOC_TYPE_SAVING_DEPOSIT = 17;
  public static final int DOC_TYPE_SAVING_CASH_DRAWER = 18;
  public static final int DOC_TYPE_CREDIT_REALISATION = 19;
  public static final int DOC_TYPE_CREDIT_PAYMENT = 20;
  public static final int DOC_TYPE_CREDIT_PENALTY_GENERATE = 21;
  public static final int DOC_TYPE_PURCHASE_ON_LGR_PAYMENT = 22;
  public static final int DOC_TYPE_PURCHASE_ON_LGR_AP = 23;
  public static final int DOC_TYPE_CREDIT_INTEREST_GENERATE = 24;
  public static final int DOC_TYPE_INVENTORY_ON_PROD_COST = 25;
  public static final int DOC_TYPE_INVENTORY_ON_PROD_PRODUCT = 26;

  public static final int[] DOC_TYPE_PAYMENT = {DOC_TYPE_DP_ON_SALES_ORDER, DOC_TYPE_DP_ON_PURCHASE_ORDER, DOC_TYPE_DP_ON_PRODUCTION_ORDER, DOC_TYPE_PURCHASE_ON_LGR, DOC_TYPE_CREDIT_PAYMENT};

  /**
   * declaration of identifier to explain transaction type above
   */
  public static final String[] fieldDocumentType = {
    "DP On Sales Order Document",
    "DP On Purchase Order Document",
    "DP On Production Order Document",
    "Purchase Receive ( LGR ) Document ",
    "Production Cost On LGR Document",
    "Inventory On DF(Transfer) Document",
    "Sales On Invoice Document",
    "Payment On LGR Document",
    "Payment On Invoice Document",
    "Payment Type Document",
    "Payment Type Document Posted Cleared",
    "Closing Credit Sales",
    "Sales Commision",
    "Supplier Return",
    "Inventory On Receive by DF(Transfer)",
    "Inventory for Cost",
    "Compliment Cost",
    "Saving Deposit",
    "Saving Cash Drawer",
    "Credit Realization",
    "Credit Payment",
    "",
    "",
    "",
    "Interest Generate",
	"Inventory On Production Cost Document",
	"Inventory On Production Product Document"
  };

  public static final String[] fieldDocumentTypeBahasa = {
    "DP On Sales Order Document",
    "DP On Purchase Order Document",
    "DP On Production Order Document",
    "Purchase Receive ( LGR ) Document ",
    "Production Cost On LGR Document",
    "Inventory On DF(Transfer) Document",
    "Sales On Invoice Document",
    "Payment On LGR Document",
    "Payment On Invoice Document",
    "Payment Type Document",
    "Payment Type Document Posted Cleared",
    "Closing Credit Sales",
    "Sales Commision",
    "Supplier Return",
    "Inventory On Receive by DF(Transfer)",
    "Inventory for Cost",
    "Compliment Cost",
    "Setoran Tabungan",
    "Penarikan Tabungan",
    "Pencairan Kredit",
    "Pembayaran Angsuran Kredit",
    "",
    "",
    "",
    "Pencatatan Bunga",
	"Inventory On Production Cost Document",
	"Inventory On Production Product Document"
  };

  public static final String[] fieldDocumentTypeLanguage[][] = {
    {fieldDocumentTypeBahasa},
    {fieldDocumentType}
  };

  /**
   * declaration of identifier to handle return payment type on back office
   * system
   */
  public static final int TYPE_RET_PAYMENT_REFUND = 0;
  public static final int TYPE_RET_PAYMENT_AR_DEDUCT = 1;

  /**
   * declaration of payment detail type
   */
  public static final int PAID_FOR_INVOICE = 0;
  public static final int PAID_FOR_DEPOSIT = 1;
  public static final int PAID_FOR_DEPOSIT_PAYMENT = 2;
  public static final int PAID_FOR_DEPOSIT_REFUND = 3;
  public static final int PAID_FOR_OTHER_INCOME = 4;
  public static final int PAID_FOR_PAYMENT_REFUND = 5;
  public static final int PAID_FOR_EXPENSE = 6;
  public static final String[] strPaidFor = {"Payment", "Deposit", "Deposit Payment", "Deposit Refund", "Other Income", "Payment Refund", "Expense"};//, "Loss/Expense"};

  /**
   * this method used to get selected configuration for specified BO, Group and
   * Config Item
   */
  public int getIJConfiguration(int intBoSystem, int intConfigGroup, int intConfigItem);

  public static final int MESSAGE_ACC_MAP_NOT_FOUND = 0;
  public static final int MESSAGE_ACC_MAP_TO_SAME_COA = 1;
  public static final int MESSAGE_ACC_MAP_LOSS_COA = 2;
  public static final String strMessages[][] = {
    {"Nomor Perkiraan map tidak ditemukan", "Mapping Debet & Kredit ke Nomor Perkiraan yang sama", "Nomor perkiraan tidak ada di table perkiraan, sudah terhapus"},
    {"CoA Setting not found", "Account map for debt and credit to one same account", "CoA not found on CoA table, CoA is deleted"}
  };

  public static final int MAT_TYPE_REGULAR = 0;
  public static final int MAT_TYPE_COMPOSITE = 1;
  public static final int MAT_TYPE_SERVICE = 2;
  public static final String strMaterialType[][] = {
    {"Barang", "Composite", "Jasa"},
    {"Regular", "Composite", "Service"}
  };

    // group of Dimata Sedana special data
  //"Customer Saving",
  public static final int SEDANA_SAVING_IDX = 0;
  public static final String SEDANA_SAVING[][] = {{"Transaksi Tabungan", "Customer Saving"}};

  //"Revenue of Saving and Loan",
  public static final int SEDANA_REVENUE_SAVING_ADMIN = 0;
  public static final int SEDANA_REVENUE_CREDIT_INTEREST = 1;
  public static final int SEDANA_REVENUE_CREDIT_ADMIN = 2;
  public static final int SEDANA_REVENUE_CREDIT_PROVISION = 3;
  public static final int SEDANA_REVENUE_CREDIT_PENALTY = 4;

  public static final String SEDANA_REVENUE[][] = {
    {"Pendapatan biaya tabungan", "Revenue Saving Acc Admin"},
    {"Pendapatan Bunga Pinjaman", "Revenue Credit Interest"},
    {"Pendapatan Admin Pinjaman", "Revenue Credit Admin"},
    {" Pendapatan Provisi Kredit", "Revenue Credit Provision "},
    {"Pendapatan Denda Kredit ", "Revenue Credit Penalty"}
  };

  //"Cost of Saving and Loan"   
  public static final int SEDANA_COST_SAVING_INTEREST = 0;
  public static final String SEDANA_COST[][] = {
    {"Biaya bunga tabungan", "Cost Savings Interest"}
  };

  public static final int ERR_MAPPING_PAYMENT = 0;
  public static final int ERR_MAPPING_LOCATION = 1;
  public static final int ERR_MAPPING_ACCOUNT = 2;

  public static final int ENGINE_MSG_ERROR = 0;
  public static final int ENGINE_MSG_WARNING = 1;
  public static final int ENGINE_MSG_INFO = 2;
  public static final int ENGINE_MSG_DEFAULT = 3;

}
