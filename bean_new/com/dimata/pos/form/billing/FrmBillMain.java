/*
 * FrmCashMaster.java
 *
 * Created on January 8, 2004, 9:53 AM
 */

package com.dimata.pos.form.billing;

/**
 *
 * @author  gedhy
 */
// java package
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// qdep package
import com.dimata.qdep.form.*;

// project package
import com.dimata.pos.entity.masterCashier.*;
import com.dimata.pos.entity.billing.*;

public class FrmBillMain extends FRMHandler implements I_FRMInterface, I_FRMType {

    	private BillMain billMain;

	public static final String FRM_NAME_BILL_MAIN	=  "FRM_NAME_BILL_MAIN" ;

	public static final int FRM_FIELD_CASH_BILL_MAIN_ID =  0 ;
	public static final int FRM_FIELD_APP_USER_ID    =  1 ;
	public static final int FRM_FIELD_BILL_DATE =  2 ;
        public static final int FRM_FIELD_BILL_NUMBER            =  3 ;
        public static final int FRM_FIELD_CASH_CASHIER_ID       =  4 ;
        public static final int FRM_FIELD_CASH_PENDING_ORDER_ID     =  5 ;
        public static final int FRM_FIELD_COVER_NUMBER=6;
        public static final int FRM_FIELD_CUSTOMER_ID=7;
        public static final int FRM_FIELD_DISC_GLOBAL=8;
        public static final int FRM_FIELD_DISC_TYPE=9;
        public static final int FRM_FIELD_DOC_TYPE=10;
        public static final int FRM_FIELD_GUEST_NAME=11;
        public static final int FRM_FIELD_INVOICE_COUNTER=12;
        public static final int FRM_FIELD_INVOICE_NUMBER=13;
        public static final int FRM_FIELD_LOCATION_ID=14;
        public static final int FRM_FIELD_NOTES=15;
        public static final int FRM_FIELD_PARENT_ID=16;
        public static final int FRM_FIELD_SALES_CODE=17;
        public static final int FRM_FIELD_SERVICE_PCT=18;
        public static final int FRM_FIELD_SERVICE_VALUE=19;
        public static final int FRM_FIELD_SHIFT_ID=20;
        public static final int FRM_FIELD_SPECIAL_FLAG=21;
        public static final int FRM_FIELD_SPECIAL_ID=22;
        public static final int FRM_FIELD_TAX_PCT=23;
        public static final int FRM_FIELD_TAX_VALUE=24;
        public static final int FRM_FIELD_TRANSACTION_STATUS=25;
        public static final int FRM_FIELD_TRANS_TYPE=26;

        //untuk Delivery Address
        public static final int FRM_FIELD_SHIPPING_ADDRESS=27;
        public static final int FRM_FIELD_SHIPPING_CITY=28;
        public static final int FRM_FIELD_SHIPPING_PROVINCE=29;
        public static final int FRM_FIELD_SHIPPING_COUNTRY=30;
        public static final int FRM_FIELD_SHIPPING_ZIP=31;
        public static final int FRM_FIELD_SHIPPING_PH_NUMBER=32;
        public static final int FRM_FIELD_SHIPPING_MOBILE_NUMBER=33;
        public static final int FRM_FIELD_SHIPPING_FAX=34;
        public static final int FRM_FIELD_CURRENCY_ID = 35;

        //untuk total Penjualan
        /**
         * Ari_wiweka20130701
         */
        public static final int FRM_FIELD_AMOUNT = 36;
        public static final int FRM_FIELD_PAID_AMOUNT = 37;

        public static final int FRM_FIELD_DISC_PCT = 38;
        public static final int FRM_FIELD_STATUS_DATE = 39;
        public static final int FRM_FIELD_DATE_FROM = 40;
        public static final int FRM_FIELD_DATE_TO = 41;

        public static final int FRM_FIELD_RATE = 42;

        public static final int FRM_FIELD_INVOICING_ID = 43;
        public static final int FRM_FIELD_STATUS_INVOICING = 44;
        public static final int FRM_FIELD_DO_PERSON_ID = 45;

        public static final int FRM_FIELD_TYPE_SALES_ORDER = 46;
        public static final int FRM_FIELD_PARENT_SALES_ID = 47;
        public static final int FRM_FIELD_DUE_DATE_PAYMENT = 48;
        
        public static final int FRM_FIELD_ROOM_ID = 49;
        public static final int FRM_FIELD_TABLE_ID = 50;
        
        public static final int FRM_FIELD_PAX_NUMBER = 51;
        
        public static final int FRM_FIELD_SPLIT=52;
        public static final int FRM_FIELD_RESERVATION_ID=53;
        public static final int FRM_FIELD_ID_NEGARA = 54;
        public static final int FRM_FIELD_GENDER = 55;
        //added by wira 20191115
        public static final int FRM_FIELD_STATUS = 56;
        public static final int FRM_FIELD_APP_USER_SALES_ID = 57;

	public static String[] fieldNames =
        {
		"FRM_FIELD_CASH_BILL_MAIN_ID",
                "FRM_FIELD_APP_USER_ID",
                "FRM_FIELD_BILL_DATE",
                "FRM_FIELD_BILL_NUMBER",
                "FRM_FIELD_CASH_CASHIER_ID",
                "FRM_FIELD_CASH_PENDING_ORDER_ID",
                "FRM_FIELD_COVER_NUMBER",
                "FRM_FIELD_CUSTOMER_ID",
                "FRM_FIELD_DISC_GLOBAL",
                "FRM_FIELD_DISC_TYPE",
                "FRM_FIELD_DOC_TYPE",
                "FRM_FIELD_GUEST_NAME",
                "FRM_FIELD_INVOICE_COUNTER",
                "FRM_FIELD_INVOICE_NUMBER",
                "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_NOTES",
                "FRM_FIELD_PARENT_ID",
                "FRM_FIELD_SALES_CODE",
                "FRM_FIELD_SERVICE_PCT",
                "FRM_FIELD_SERVICE_VALUE",
                "FRM_FIELD_SHIFT_ID",
                "FRM_FIELD_SPECIAL_FLAG",
                "FRM_FIELD_SPECIAL_ID",
                "FRM_FIELD_TAX_PCT",
                "FRM_FIELD_TAX_VALUE",
                "FRM_FIELD_TRANSACTION_STATUS",
                "FRM_FIELD_TRANS_TYPE",
                "FRM_FIELD_SHIPPING_ADDRESS",
                "FRM_FIELD_SHIPPING_CITY",
                "FRM_FIELD_SHIPPING_PROVINCE",
                "FRM_FIELD_SHIPPING_COUNTRY",
                "FRM_FIELD_SHIPPING_ZIP",
                "FRM_FIELD_SHIPPING_PH_NUMBER",
                "FRM_FIELD_SHIPPING_MOBILE_NUMBER",
                "FRM_FIELD_SHIPPING_FAX",
                "FRM_FIELD_CURRENCY_ID",
                "FRM_FIELD_AMOUNT",
                "FRM_FIELD_PAID_AMOUNT",
                "FRM_FIELD_DISC_PCT",

                "FRM_FIELD_STATUS_DATE",
                "FRM_FIELD_DATE_FROM",
                "FRM_FIELD_DATE_TO",
                "FRM_FIELD_RATE",

                "FRM_FIELD_INVOICING_ID",
                "FRM_FIELD_STATUS_INVOICING",
                "FRM_FIELD_DO_PERSON_ID",
                "FRM_FIELD_TYPE_SALES_ORDER",
                "FRM_FIELD_PARENT_SALES_ID",
                "FRM_FIELD_DUE_DATE_PAYMENT",
                "FRM_FIELD_ROOM_ID",
                "FRM_FIELD_TABLE_ID",
                "PAX_NUMBER",
                "FRM_FIELD_SPLIT",
                "FRM_FIELD_RESERVATION_ID",
                "FRM_FIELD_ID_NEGARA",
                "FRM_FIELD_GENDER",
                "FRM_FIELD_STATUS",
                "FRM_FIELD_APP_USER_SALES_ID"


	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,
                TYPE_DATE,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_INT,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_INT,
                TYPE_INT,

                //Delivery Address
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_LONG,

                TYPE_FLOAT,
                TYPE_FLOAT,

                TYPE_FLOAT,

                TYPE_INT,
                TYPE_DATE,
                TYPE_DATE,
                TYPE_FLOAT,

                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG,
                TYPE_INT,
                
                TYPE_LONG,
                TYPE_LONG,
                
                TYPE_INT,
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT,
                TYPE_LONG

	} ;

	public FrmBillMain ()
        {
	}

        public FrmBillMain (BillMain billMain)
        {
		this.billMain= billMain;
	}

	public FrmBillMain (HttpServletRequest request, BillMain billMain)
        {
		super(new FrmBillMain(billMain), request);
		this.billMain = billMain;
	}

	public String getFormName() { return FRM_NAME_BILL_MAIN; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public BillMain getEntityObject(){ return billMain; }

	public void requestEntityObject(BillMain billMain)
        {
		try
                {
			this.requestParam();
                        
                        billMain.setCashCashierId(getLong(FRM_FIELD_CASH_CASHIER_ID));
                        billMain.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                        billMain.setBillDate(getDate(FRM_FIELD_BILL_DATE));
                        billMain.setInvoiceNo(getString(FRM_FIELD_BILL_NUMBER));
                        billMain.setAppUserId(getLong(FRM_FIELD_APP_USER_ID));
                        billMain.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
                        billMain.setDiscType(getInt(FRM_FIELD_DISC_TYPE));
                        billMain.setDiscount(getDouble(FRM_FIELD_DISC_GLOBAL));
                        billMain.setTaxPercentage(getDouble(FRM_FIELD_TAX_PCT));
                        billMain.setTaxValue(getDouble(FRM_FIELD_TAX_VALUE));
                        billMain.setServicePct(getDouble(FRM_FIELD_SERVICE_PCT));
                        billMain.setServiceValue(getDouble(FRM_FIELD_SERVICE_VALUE));
                        billMain.setBillStatus(getInt(FRM_FIELD_TRANSACTION_STATUS));
                        billMain.setSalesCode(getString(FRM_FIELD_SALES_CODE));

                        billMain.setInvoiceNumber(getString(FRM_FIELD_INVOICE_NUMBER));
                        billMain.setInvoiceCounter(getInt(FRM_FIELD_INVOICE_COUNTER));
                        billMain.setTransType(getInt(FRM_FIELD_TRANS_TYPE));
                        billMain.setDocType(getInt(FRM_FIELD_DOC_TYPE));


                        billMain.setCashPendingOrderId (getLong (FRM_FIELD_CASH_PENDING_ORDER_ID));
                        billMain.setCustomerId (getLong(FRM_FIELD_CUSTOMER_ID));
                        billMain.setTransctionType (getInt(FRM_FIELD_TRANS_TYPE));
                        billMain.setTransactionStatus (getInt(FRM_FIELD_TRANSACTION_STATUS));
                        billMain.setCoverNumber (getString(FRM_FIELD_COVER_NUMBER));
                        billMain.setSpecialId (getLong(FRM_FIELD_SPECIAL_ID));
                        billMain.setSpecialFlag (getInt(FRM_FIELD_SPECIAL_FLAG));

                        billMain.setParentId (getLong(FRM_FIELD_PARENT_ID));

                        billMain.setGuestName(getString(FRM_FIELD_GUEST_NAME));
                        billMain.setNotes(getString(FRM_FIELD_NOTES));

                        //Delivery Address
                        billMain.setShippingAddress(getString(FRM_FIELD_SHIPPING_ADDRESS));
                        billMain.setShippingCity(getString(FRM_FIELD_SHIPPING_CITY));
                        billMain.setShippingProvince(getString(FRM_FIELD_SHIPPING_PROVINCE));
                        billMain.setShippingCountry(getString(FRM_FIELD_SHIPPING_COUNTRY));
                        billMain.setShippingZipCode(getString(FRM_FIELD_SHIPPING_ZIP));
                        billMain.setShippingPhoneNumber(getString(FRM_FIELD_SHIPPING_PH_NUMBER));
                        billMain.setShippingMobilePhone(getString(FRM_FIELD_SHIPPING_MOBILE_NUMBER));
                        billMain.setShippingFax(getString(FRM_FIELD_SHIPPING_FAX));

                        //untuk total penjualan
                        billMain.setAmount(getDouble(FRM_FIELD_AMOUNT));
                        billMain.setPaidAmount(getDouble(FRM_FIELD_PAID_AMOUNT));

                        billMain.setDiscPct(getDouble(FRM_FIELD_DISC_PCT));

                        billMain.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));

                        billMain.setStatusDate(getInt(FRM_FIELD_STATUS_DATE));
                        billMain.setDatefrom(getDate(FRM_FIELD_DATE_FROM));
                        billMain.setDateto(getDate(FRM_FIELD_DATE_TO));

                        billMain.setRate(getDouble(FRM_FIELD_RATE));

                        billMain.setInvoicingId(getLong(FRM_FIELD_INVOICING_ID));
                        billMain.setStatusInv(getInt(FRM_FIELD_STATUS_INVOICING)); 
                        billMain.setDoPersonId(getLong(FRM_FIELD_DO_PERSON_ID));

                        billMain.setTypeSalesOrder(getInt(FRM_FIELD_TYPE_SALES_ORDER));
                        billMain.setParentSalesOrderId(getLong(FRM_FIELD_PARENT_SALES_ID));
                        billMain.setDueDatePayment(getInt(FRM_FIELD_DUE_DATE_PAYMENT));
                        
                        billMain.setRoomID(getLong(FRM_FIELD_ROOM_ID));
                        billMain.setTableId(getLong(FRM_FIELD_TABLE_ID));
                        
                        billMain.setPaxNumber(getInt(FRM_FIELD_PAX_NUMBER));
                        billMain.setSplitBill(getInt(FRM_FIELD_SPLIT));
                        
						billMain.setNegaraId(getLong(FRM_FIELD_ID_NEGARA));
                        billMain.setGender(getInt(FRM_FIELD_GENDER));
                        billMain.setStatus(getInt(FRM_FIELD_STATUS));
                        billMain.setAppUserSalesId(getLong(FRM_FIELD_APP_USER_SALES_ID));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
        
        
        
        public void xrequestEntityObject(BillMain billMain)
        {
		try
                {
			this.requestParam();
                        
                        billMain.setCashCashierId(getLong(FRM_FIELD_CASH_CASHIER_ID));
                        
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
