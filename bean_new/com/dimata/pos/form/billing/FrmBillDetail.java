/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.billing;

/**
 *
 * @author Wiweka
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// qdep package
import com.dimata.qdep.form.*;

// project package
import com.dimata.pos.entity.masterCashier.*;
import com.dimata.pos.entity.billing.*;

public class FrmBillDetail extends FRMHandler implements I_FRMInterface, I_FRMType {

    	private Billdetail billdetail;

	public static final String FRM_NAME_BILL_DETAIL	=  "FRM_NAME_BILL_DETAIL" ;

	public static final int FRM_FIELD_CASH_BILL_DETAIL_ID       =  0;
        public static final int FRM_FIELD_CASH_BILL_MAIN_ID         =  1;
	public static final int FRM_FIELD_UNIT_ID              =  2;
	public static final int FRM_FIELD_MATERIAL_ID                 =  3;
        public static final int FRM_FIELD_QTY              =  4;
        public static final int FRM_FIELD_ITEM_PRICE         =  5;
        public static final int FRM_FIELD_DISC     =  6;
        public static final int FRM_FIELD_TOTAL_PRICE            =  7;
        public static final int FRM_FIELD_SKU              =  8;
        public static final int FRM_FIELD_ITEM_NAME                    =  9;
        public static final int FRM_FIELD_DISC_TYPE                 =  10;
        public static final int FRM_FIELD_MATERIAL_TYPE                 =  11;
        public static final int FRM_FIELD_DISC_PCT          = 12;
        public static final int FRM_FIELD_NOTE      = 13;
        public static final int FRM_FIELD_DISCON_GLOBAL           = 14;
        public static final int FRM_FIELD_STATUS              = 15;
        public static final int FRM_FIELD_DISC_1     =  16;
        public static final int FRM_FIELD_DISC_2     =  17;

        /**
         * untuk total
         * Ari_wiweka 20130701
         */
        public static final int FRM_FIELD_TOTAL_AMOUNT     =  18;
        public static final int FRM_FIELD_TOTAL_DISC = 19;
        public static final int FRM_FIELD_QTY_ISSUE = 20;
        
        //added by dewok 20180329 for jewelry
        public static final int FRM_FIELD_BERAT = 21;
        public static final int FRM_FIELD_SUSUTAN_WEIGHT = 22;
        public static final int FRM_FIELD_SUSUTAN_PRICE = 23;
        public static final int FRM_FIELD_TAX_PCT = 24;
        public static final int FRM_FIELD_SERVICE_PCT = 25;
        public static final int FRM_FIELD_COST = 26;
        public static final int FRM_FIELD_TOTAL_TAX = 27;
        public static final int FRM_FIELD_TOTAL_SERVICE = 28;
        //added by dewok 20180725 for jewelry
        public static final int FRM_FIELD_ADDITIONAL_WEIGHT = 29;
        public static final int FRM_FIELD_LATEST_ITEM_PRICE = 30;

	public static String[] fieldNames =
        {
                "FRM_FIELD_CASH_BILL_DETAIL_ID",
                "FRM_FIELD_CASH_BILL_MAIN_ID",
                "FRM_FIELD_UNIT_ID",
                "FRM_FIELD_MATERIAL_ID",
                "FRM_FIELD_QTY",
                "FRM_FIELD_ITEM_PRICE",
                "FRM_FIELD_DISC",
                "FRM_FIELD_TOTAL_PRICE",
                "FRM_FIELD_SKU",
                "FRM_FIELD_ITEM_NAME",
                "FRM_FIELD_DISC_TYPE",
                "FRM_FIELD_MATERIAL_TYPE",
                "FRM_FIELD_DISC_PCT",
                "FRM_FIELD_NOTE",
                "FRM_FIELD_DISCON_GLOBAL",
                "FRM_FIELD_STATUS",
                "FRM_FIELD_DISC_1",
                "FRM_FIELD_DISC_2",
                "FRM_FIELD_TOTAL_AMOUNT",
                "FRM_FIELD_TOTAL_DISC",
                "FRM_FIELD_QTY_ISSUE",
            //    "FRM_FIELD_SUB_TOTAL_AMOUNT"
                //added by dewok 20180329 for jewelry
                "FRM_FIELD_BERAT",
                "FRM_FIELD_SUSUTAN_WEIGHT",
                "FRM_FIELD_SUSUTAN_PRICE",
                "FRM_FIELD_TAX_PCT",
                "FRM_FIELD_SERVICE_PCT",
                "FRM_FIELD_COST",
                "FRM_FIELD_TOTAL_TAX",
                "FRM_FIELD_TOTAL_SERVICE",
                "FRM_FIELD_ADDITIONAL_WEIGHT",
                "FRM_FIELD_LATEST_ITEM_PRICE"
	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_INT,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_STRING,
                TYPE_FLOAT,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                //TYPE_FLOAT
                //added by dewok 20180329 for jewelry
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT
	} ;

	public FrmBillDetail ()
        {
	}

        public FrmBillDetail (Billdetail billdetail)
        {
		this.billdetail= billdetail;
	}

	public FrmBillDetail (HttpServletRequest request, Billdetail billdetail)
        {
		super(new FrmBillDetail(billdetail), request);
		this.billdetail = billdetail;
	}

	public String getFormName() { return FRM_NAME_BILL_DETAIL; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public Billdetail getEntityObject(){ return billdetail; }

	public void requestEntityObject(Billdetail billdetail)
        {
		try
                {
			this.requestParam();
                        
                        billdetail.setBillMainId(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
                        billdetail.setUnitId(getLong(FRM_FIELD_UNIT_ID));
                        billdetail.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
                        billdetail.setQty(getInt(FRM_FIELD_QTY));
                        billdetail.setItemPrice(getDouble(FRM_FIELD_ITEM_PRICE));
                        billdetail.setDisc(getDouble(FRM_FIELD_DISC));
                        billdetail.setTotalPrice(getDouble(FRM_FIELD_TOTAL_PRICE));
                        billdetail.setSku(getString(FRM_FIELD_SKU));
                        billdetail.setItemName(getString(FRM_FIELD_ITEM_NAME));
                        billdetail.setDiscType(getInt(FRM_FIELD_DISC_TYPE));
                        billdetail.setMaterialType(getInt(FRM_FIELD_MATERIAL_TYPE));
                        billdetail.setDiscPct(getDouble(FRM_FIELD_DISC_PCT));
                        billdetail.setNote(getString(FRM_FIELD_NOTE));
                        billdetail.setDiscGlobal(getDouble(FRM_FIELD_DISCON_GLOBAL));
                        billdetail.setUpdateStatus(getInt(FRM_FIELD_STATUS));
                        billdetail.setDisc1(getDouble(FRM_FIELD_DISC_1));
                        billdetail.setDisc2(getDouble(FRM_FIELD_DISC_2));
                        billdetail.setTotalAmount(getDouble(FRM_FIELD_TOTAL_AMOUNT));
                        billdetail.setTotalDisc(getDouble(FRM_FIELD_TOTAL_DISC));
                        billdetail.setQtyIssue(getDouble(FRM_FIELD_QTY_ISSUE));
                        //added by dewok 20180329 for jewelry
                        billdetail.setBerat(getDouble(FRM_FIELD_BERAT));
                        billdetail.setSusutanWeight(getDouble(FRM_FIELD_SUSUTAN_WEIGHT));
                        billdetail.setSusutanPrice(getDouble(FRM_FIELD_SUSUTAN_PRICE));
                        billdetail.setTaxPct(getDouble(FRM_FIELD_TAX_PCT));
                        billdetail.setServicePct(getDouble(FRM_FIELD_SERVICE_PCT));
                        billdetail.setCost(getDouble(FRM_FIELD_COST));
                        billdetail.setTotalTax(getDouble(FRM_FIELD_TOTAL_TAX));
                        billdetail.setTotalService(getDouble(FRM_FIELD_TOTAL_SERVICE));
                        //added by dewok 20180725 for jewelry
                        billdetail.setAdditionalWeight(getDouble(FRM_FIELD_ADDITIONAL_WEIGHT));
                        billdetail.setLatestItemPrice(getDouble(FRM_FIELD_LATEST_ITEM_PRICE));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
