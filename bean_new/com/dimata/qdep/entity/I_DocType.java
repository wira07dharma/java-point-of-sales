/**
 * @author	    : gedhy
 * @version	    : 01
 */

package com.dimata.qdep.entity;

public interface I_DocType {

    /**
     * declaration of identifier to hold name of class that implement this Interface
     */
    public static final String DOCTYPE_CLASSNAME = "com.dimata.workflow.entity.approval.PstDocType";

    /**
     * declaration of identifier used to filter document type
     */
    // for mysql
    public static final int FILTER_SYSTEM 	= 0xFFFF0000;
    public static final int FILTER_DOCTYPE 	= 0x0000FFFF;
    public static final String STR_FILTER_SYSTEM 	= "0xFFFF0000";
    public static final String STR_FILTER_DOCTYPE 	= "0x0000FFFF";
    /*
    // for postgres
    public static final long FILTER_SYSTEM 	= 4294901760L;
    public static final int FILTER_DOCTYPE 	= 65535;
    public static final String STR_FILTER_SYSTEM 	= "4294901760";
    public static final String STR_FILTER_DOCTYPE 	= "65535";
     */

    /**
     * declaration of identifier used to 'SHIFT' document type to get int refers 'SYSTEM NAME' and DOCTYPE
     */
    public static final int SHIFT_SYSTEM 	= 16;
    public static final int SHIFT_DOCTYPE 	= 0;

    /**
     * declaration of identifier refers to SYSTEM NAME (level 1) and DOCTYPE (level 2)
     */
    public static final int SYSTEM_GARMENT	= 0;
    public static final int FLD_DOC_TYPE_PR 	= 0; // Purchase Request
    public static final int FLD_DOC_TYPE_PO 	= 1; // Purchase Order
    public static final int FLD_DOC_TYPE_LGR 	= 2; // Purchase Lis Goods Receive
    public static final int FLD_DOC_TYPE_ROG    = 3; // Return Of Goods
    public static final int FLD_DOC_TYPE_DF 	= 4; // Dispatch Goods
    public static final int FLD_DOC_TYPE_PRO	= 5; // Propose Order
    public static final int FLD_DOC_TYPE_PI     = 6; // Proforma Invoice
    public static final int FLD_DOC_TYPE_PRD	= 7; // Propose Delivery
    public static final int FLD_DOC_TYPE_INV	= 8; // Invoice
    public static final int FLD_DOC_TYPE_POC	= 9; // Purchase Order Client
    public static final int FLD_DOC_TYPE_WLOP	= 10; // Wholesale Opname
    public static final int FLD_DOC_TYPE_COP	= 11; // Consignment Opname
    public static final int FLD_DOC_TYPE_WHOP	= 12; // Warehouse Opname


    public static final int SYSTEM_FURNITURE = 1;
    public static final int FNT_DOC_TYPE_IO 	= 0;
    public static final int FNT_DOC_TYPE_PO 	= 1;
    public static final int FNT_DOC_TYPE_LGR 	= 2;
    public static final int FNT_DOC_TYPE_ROG 	= 3;
    public static final int FNT_DOC_TYPE_DF 	= 4;
    public static final int FNT_DOC_TYPE_PI 	= 5;
    public static final int FNT_DOC_TYPE_PL 	= 6;
    public static final int FNT_DOC_TYPE_INV 	= 7;
    public static final int FNT_DOC_TYPE_SO 	= 8;
    public static final int FNT_DOC_TYPE_RFV 	= 9;
    public static final int FNT_DOC_TYPE_PDO 	= 10;

    public static final int SYSTEM_ANTIQUE = 2;
    public static final int ANT_DOC_TYPE_PR 	= 0;
    public static final int ANT_DOC_TYPE_PO 	= 1;
    public static final int ANT_DOC_TYPE_LGR 	= 2;
    public static final int ANT_DOC_TYPE_ROG 	= 3;
    public static final int ANT_DOC_TYPE_DF 	= 4;
    public static final int ANT_DOC_TYPE_PRO	= 5;
    public static final int ANT_DOC_TYPE_PI 	= 6;
    public static final int ANT_DOC_TYPE_PRD	= 7;
    public static final int ANT_DOC_TYPE_INV	= 8;

    public static final int SYSTEM_MATERIAL = 3;
    public static final int MAT_DOC_TYPE_PRR 	= 0;  // Purchase Request Regular
    public static final int MAT_DOC_TYPE_PRM 	= 1;  // Purchase Request Market
    public static final int MAT_DOC_TYPE_PRA 	= 2;  // Purchase Request Asset
    public static final int MAT_DOC_TYPE_POR 	= 3;  // Purchase Order Regular
    public static final int MAT_DOC_TYPE_POM 	= 4;  // Purchase Order Market
    public static final int MAT_DOC_TYPE_POA 	= 5;  // Purchase Order Asset
    public static final int MAT_DOC_TYPE_LMRR 	= 6;  // List Material Receive Regular
    public static final int MAT_DOC_TYPE_LMRM 	= 7;  // List Material Receive Market
    public static final int MAT_DOC_TYPE_LMRA 	= 8;  // List Material Receive Asset
    public static final int MAT_DOC_TYPE_ROMR 	= 9;  // Return Of Material Regular
    public static final int MAT_DOC_TYPE_ROMM 	= 10; // Return Of Material Market
    public static final int MAT_DOC_TYPE_ROMA 	= 11; // Return Of Material Asset
    public static final int MAT_DOC_TYPE_DR 	= 12; // Dispatch Request
    public static final int MAT_DOC_TYPE_DF 	= 13; // Dispatch
    public static final int MAT_DOC_TYPE_OPN	= 14; // Opname
    public static final int MAT_DOC_TYPE_COS	= 15; // Costing
    public static final int MAT_DOC_TYPE_SALE	= 16; // penjualan
    public static final int MAT_DOC_TYPE_PROD	= 17; // Production

    //khusus untuk hanoman => PER DEPARTMENT
    //DISPATCH MATERIAL REQUEST
    public static final int MAT_DOC_TYPE_DR_KIT = 11;
    public static final int MAT_DOC_TYPE_DR_HK = 12;
    public static final int MAT_DOC_TYPE_DR_FB = 13;
    public static final int MAT_DOC_TYPE_DR_FO = 14;
    public static final int MAT_DOC_TYPE_DR_AG = 15;
    public static final int MAT_DOC_TYPE_DR_ENG = 16;
    public static final int MAT_DOC_TYPE_DR_SEC = 17;
    public static final int MAT_DOC_TYPE_DR_ACC = 18;
    public static final int MAT_DOC_TYPE_DR_HRD = 19;

    //DISPATCH MATERIAL
    public static final int MAT_DOC_TYPE_DF_KIT = 20;
    public static final int MAT_DOC_TYPE_DF_HK = 21;
    public static final int MAT_DOC_TYPE_DF_FB = 22;
    public static final int MAT_DOC_TYPE_DF_FO = 23;
    public static final int MAT_DOC_TYPE_DF_AG = 24;
    public static final int MAT_DOC_TYPE_DF_ENG = 25;
    public static final int MAT_DOC_TYPE_DF_SEC = 26;
    public static final int MAT_DOC_TYPE_DF_ACC = 27;
    public static final int MAT_DOC_TYPE_DF_HRD = 28;
    
    //source receive
    public static final int MAT_RECEIVE_SOURCE_PENERIMAAN_WITHOUT_PO = 0;
    public static final int MAT_RECEIVE_SOURCE_PENERIMAAN_WITH_PO = 1;
    public static final int MAT_RECEIVE_SOURCE_PENERIMAAN_TRANSFER_TOKO = 2;
    public static final int MAT_RECEIVE_SOURCE_PENERIMAAN_TRANSFER_UNIT= 4;
    
    
    /**
     * declaration of identifier explain SYSTEM NAME (level 1)
     */
    public static final String[] systemTypeNames = {
        "Prochain Garment",
        "Prochain Manufacturing",
        "Prochain Antique",
        "Prochain Material"
    };

    /**
     * declaration of identifier explain DOCTYPE (level 2)
     */
    public static final String[][] documentTypeNames = {
        // Prochain Garment
        {
            "Purchase Request","Purchase Order","List Goods Receive","Return of Goods","Dispatch Form","Propose Order","Proforma Invoice",
            "Propose Delivery","Invoice","Purchase Order Client", "Wholesale Opname", "Consignment Opname", "Warehouse Opname"
        },
        // Prochain Furniture
        {
            "Inventory Order","Purchase Order","List Goods Receive","Return of Goods","Dispatch Form","Proforma Invoice","Packing List",
            "Invoice","Stock Opname","RFV","Production Order"
        },
        // Prochain Antique
        {
            "Purchase Request","Purchase Order","List Goods Receive","Return of Goods","Dispatch Form","Propose Order","Proforma Invoice",
            "Propose Delivery","Invoice"
        },
        // Prochain Material
        {
            "Regular Purchase Request","Market Purchase Request","Asset Purchase Request","Regular Purchase Order","Market Purchase Order",
            "Asset Purchase Order","List Regular Material Receive","List Market Material Receive","List Asset Receive","Return of Regular Material",
            "Return of Market Material","Return of Asset","Dispatch Request","Dispatch Material", "Material Opname","Costing"
        }
    };

     /**
     * declaration of identifier explain SYSTEM NAME (level 1)
     */
    public static final String[] systemModulMenu = {
        "Member",
        "Supplier"
    };

}
