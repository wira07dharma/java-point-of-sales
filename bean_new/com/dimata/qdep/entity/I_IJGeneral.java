/*
 * I_IJGeneral.java
 *
 * Created on April 17, 2005, 4:45 PM
 */

package com.dimata.qdep.entity;

/**
 *
 * @author  Administrator
 * @version
 */
public interface I_IJGeneral {
    
    // BO System
    public static final int BO_PROCHAIN_MANUFACTURE = 0;
    public static final int BO_PROCHAIN_POS = 1;
    public static final int BO_PROCHAIN_HANOMAN = 2;
    public static final String[] strBoSystem =
    {
        "ProChain Manufacturer",
                "ProChain POS",
                "Hanoman"
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
    public static final int  CFG_GRP_INV_STORE_AFTER_REC = 1;
    
    // configuration index for TAX
    public static final int CFG_GRP_TAX = 2;
    public static final int CFG_GRP_TAX_SALES = 0;
    public static final int CFG_GRP_TAX_SALES_VAT_RPT = 0;
    public static final int CFG_GRP_TAX_SALES_VAT_NO_RPT = 1;
    public static final int CFG_GRP_TAX_SALES_NO_VAT = 2;
    
    public static final int CFG_GRP_TAX_BUY = 1;
    public static final int CFG_GRP_TAX_BUY_NO_VAT = 0;
    public static final int CFG_GRP_TAX_BUY_VAT_RPT = 1;
    
    // configuration index for SYSTEM
    public static final int CFG_GRP_SYS = 3;
    public static final int CFG_GRP_SYS_IJ_ENG = 0;
    public static final int CFG_GRP_SYS_IJ_ENG_INTERACTIVE= 0;
    public static final int CFG_GRP_SYS_IJ_ENG_FULL_AUTO= 1;
    
    
    // Configuration Group
    public static final String[] strConfigGroup =
    {
        "Payment",
                "Inventory",
                "TAX",
                "System"
    };
    
    
    // Configuration Item
    public static final String[][] strConfigItem =
    {
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
            "Journal Engine"
        }
    };
    
    
    // Configuration Select
    public static final String[][][] strConfigSelect =
    {
        {
            {
                //"Income Received in Advance (A/P)",
                //"Direct as Sales Income"  => ini di by pas dulu, karena sulit menentukan pendatapan prod dept mana ???
                "Income Received in Advance (A/P)"
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
                "Interactive",
                        "Fully Automatic"
            }
        }
    };
    
    /**
     * declaration of identifier to handle index of each payment status
     */
    public static final int PAYMENT_STATUS_NOT_POSTED           = 0;
    public static final int PAYMENT_STATUS_POSTED_NOT_CLEARED   = 1;
    public static final int PAYMENT_STATUS_POSTED_CLEARED       = 2;
    public static final int PAYMENT_STATUS_POSTED_CLOSED        = 3;
    
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
    public static final int RESULT_CODE_OK           = 0;
    public static final int RESULT_CODE_ERROR        = 1;
    
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
    public static final int TRANS_DP_ON_SALES_ORDER        = 0;
    public static final int TRANS_DP_ON_PURCHASE_ORDER     = 1;
    public static final int TRANS_DP_ON_PRODUCTION_ORDER   = 2;
    public static final int TRANS_GOODS_RECEIVE            = 3;
    public static final int TRANS_TAX_ON_BUYING            = 4;
    public static final int TRANS_SALES                    = 5;
    public static final int TRANS_SALES_DISCOUNT           = 6;
    public static final int TRANS_COGS                     = 7;
    public static final int TRANS_OTHER_COST_ON_INVOICING  = 8;
    public static final int TRANS_TAX_ON_SELLING           = 9;
    public static final int TRANS_INVENTORY_LOCATION       = 10;
    public static final int TRANS_WIP                      = 11;
    public static final int TRANS_PURCHASE_DISCOUNT        = 12;
    public static final int TRANS_PROD_COST_DISCOUNT       = 13;
    public static final int TRANS_CUSTOMER_RETURN          = 14;
    public static final int TRANS_SUPPLIER_RETURN          = 15;
    public static final int TRANS_DF_TO_DEPARTMENT         = 16;
    public static final int TRANS_LGR_FROM_DEPARTMENT      = 17;
    public static final int TRANS_EARN_CORRECTION_LAST_PER = 18;
    public static final int TRANS_CHARGE                   = 19;
    public static final int TRANS_COMMISION                = 20;
    public static final int TRANS_COSTING                  = 21;
    
    /**
     * declaration of identifier to explain result transaction type above
     */
    public static final String[] strTransactionType = {
        "DP on Sales Order",
                "DP on Purchase Order",
                "DP on Production Order",
                "Goods Receive (LGR)",
                "TAX on Buying",
                "Sale (Invoicing)",
                "Sales Discount",
                "Cost of Goods Sold",
                "Other Cost on Invoicing",
                "TAX on Selling",
                "Inventory Location",
                "Work In Process",
                "Purchasing Discount",
                "Production Cost Discount",
                "Customer Return",
                "Supplier Return",
                "DF to Department",
                "LGR from Department",
                "Earning Correction on last period",
                "Charge",
                "Commision",
                "Costing"
    };
    
    
    /**
     * declaration of identifier to handle sale type on back office system
     */
    public static final int SALE_TYPE_REGULAR         = 0;
    public static final int SALE_TYPE_CONSIGNMENT     = 1;
    public static final int SALE_TYPE_PERSONAL        = 2;
    
    
    /**
     * declaration of identifier to explain sale type above
     */
    public static final String[] fieldSaleType = {
        "Regular",
                "Consignment",
                "Personal"
    };
    
    
    /**
     * declaration of identifier to handle DF type on back office system
     */
    public static final int DF_TYPE_WAREHOUSE         = 0;
    public static final int DF_TYPE_PRODUCTION      = 1;
    
    /**
     * declaration of identifier to explain DF type above
     */
    public static final String[] fieldDfType = {
        "DF To Warehouse",
                "DF To Production"
    };
    
    
    /**
     * declaration of identifier to handle transaction type on back office system
     */
    public static final int TRANSACTION_TYPE_CASH   = 0;
    public static final int TRANSACTION_TYPE_CREDIT = 1;
    
    /**
     * declaration of identifier to explain transaction type above
     */
    public static final String[] fieldTransactionType = {
        "Cash",
                "Credit"
    };
    
    
    /**
     * declaration of identifier to handle document type on back office system that referred by generated journal
     */
    public static final int DOC_TYPE_DP_ON_SALES_ORDER   = 0;
    public static final int DOC_TYPE_DP_ON_PURCHASE_ORDER = 1;
    public static final int DOC_TYPE_DP_ON_PRODUCTION_ORDER = 2;
    public static final int DOC_TYPE_PURCHASE_ON_LGR = 3;
    public static final int DOC_TYPE_PROD_COST_ON_LGR = 4;
    public static final int DOC_TYPE_INVENTORY_ON_DF = 5;
    public static final int DOC_TYPE_SALES_ON_INV = 6;
    public static final int DOC_TYPE_PAYMENT_ON_LGR = 7;
    public static final int DOC_TYPE_PAYMENT_ON_INV = 8;
    public static final int DOC_TYPE_PAYMENT_TYPE_POSTED_CLEARED = 9;
    public static final int DOC_TYPE_LGR_FROM_DEPT = 10;
    public static final int DOC_TYPE_DF_TO_DEPT = 11;
    public static final int DOC_TYPE_DF_FOR_COSTING = 12;
    public static final int DOC_TYPE_CANCELLATION = 13;
    public static final int DOC_TYPE_ADJUSTMENT = 14;
    public static final int DOC_TYPE_ACQ_OF_COMMISION = 15;
    public static final int DOC_TYPE_PAYMENT_ON_COMMISION = 16;
    public static final int DOC_TYPE_CUST_RETURN = 17;
    public static final int DOC_TYPE_CUST_RETURN_DEDUCTION = 18;
    public static final int DOC_TYPE_SUPP_RETURN = 19;
    public static final int DOC_TYPE_SUPP_RETURN_DEDUCTION = 20;
    
    /**
     * declaration of identifier to explain transaction type above
     */
    public static final String[] fieldDocumentType = {
        "DP On Sales Order Document",
                "DP On Purchase Order Document",
                "DP On Production Order Document",
                "Purchase On LGR Document",
                "Production Cost On LGR Document",
                "Inventory On DF Document",
                "Sales On Invoice Document",
                "Payment On LGR Document",
                "Payment On Invoice Document",
                "Payment Type Document Posted Cleared",
                "Receive From Department",
                "Dispatch To Department",
                "Dispatch For Costing",
                "Cancellation",
                "Adjustment",
                "Acquisition Of Commision",
                "Payment On Commision",
                "Customer Return",
                "Customer Return Deduction",
                "Supplier Return",
                "Supplier Return Deduction"
    };
    
    
    /**
     * declaration of identifier to handle adjustment type on back office system
     */
    public static final int ADJUSTMENT_PLUS   = 0;
    public static final int ADJUSTMENT_MINUS  = 1;
    
    /**
     * declaration of identifier to explain transaction type above
     */
    public static final String[] fieldAdjustmentType = {
        "Plus",
                "Minus"
    };
    
    /**
     * declaration of identifier to handle return payment type on back office system
     */
    public static final int TYPE_RET_PAYMENT_REFUND     = 0;
    public static final int TYPE_RET_PAYMENT_AR_DEDUCT  = 1;
    
    /**
     * declaration of identifier to explain transaction type above
     */
    public static final String[] fieldReturnPaymentType = {
        "Refund / Cash",
                "A/R Deduction"
    };
    
    /**
     * this method used to get selected configuration for specified BO, Group and Config Item
     */
    public int getIJConfiguration(int intBoSystem, int intConfigGroup, int intConfigItem);
    
    /*
Refund / Cash : jika retur penjualan dibayar dengan tunai atau balik barang balik uang.
A/R Deductio
     **/
    
}
