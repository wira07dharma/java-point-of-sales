/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.balance;

/**
 *
 *  @author Ari Wiweka
 * 11/06/2013
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// qdep package
import com.dimata.qdep.form.*;

// project package
import com.dimata.pos.entity.balance.*;
public class FrmCashCashier extends FRMHandler implements I_FRMInterface, I_FRMType {

    	private CashCashier cashCashier;

	public static final String FRM_NAME_CASH_CASHIER	=  "FRM_NAME_CASH_CASHIER" ;

	public static final int FRM_FIELD_CASH_CASHIER_ID =  0 ;
	public static final int FRM_FIELD_CASH_MASTER_ID  =  1 ;
	public static final int FRM_FIELD_APP_USER_ID     =  2 ;
        public static final int FRM_FIELD_OPEN_DATE       =  3 ;
        public static final int FRM_FIELD_SPV_OID         =  4 ;
        public static final int FRM_FIELD_SPV_NAME        =  5 ;
        public static final int FRM_FIELD_SPV_CLOSE_OID   =  6 ;
        public static final int FRM_FIELD_SPV_CLOSE_NAME  =  7 ;
        public static final int FRM_FIELD_SHIFT_ID        =  8 ;

        // untuk cash_balance
        public static final int FRM_FIELD_BALANCE_VALUE = 9;
        public static final int FRM_FIELD_CURRENCY_ID = 10;
        public static final int FRM_FIELD_TYPE = 11;

        //tmp location
        public static final int FRM_FIELD_LOCATION = 12;

        //close Date
        public static final int FRM_FIELD_CLOSE_DATE = 13;

        //untuk total opening cashier
        public static final int FRM_FIELD_RUPIAH = 14;
        public static final int FRM_FIELD_USD = 15;

        //untuk amount
        public static final int FRM_FIELD_AMOUNT1 = 16;
        public static final int FRM_FIELD_AMOUNT2 = 17;
        public static final int FRM_FIELD_SUBTOTAL1 = 18;
        public static final int FRM_FIELD_SUBTOTAL2 = 19;
        public static final int FRM_FIELD_CURRENCY_ID2 = 20;

	public static String[] fieldNames =
        {
		"FRM_FIELD_CASH_CASHIER_ID",
                "FRM_FIELD_CASH_MASTER_ID",
                "FRM_FIELD_APP_USER_ID",
                "FRM_FIELD_OPEN_DATE",
                "FRM_FIELD_SPV_OID",
                "FRM_FIELD_SPV_NAME",
                "FRM_FIELD_SPV_CLOSE_OID",
                "FRM_FIELD_SPV_CLOSE_NAME",
                "FRM_FIELD_SHIFT_ID",
                
                // untuk cash balance
                "FRM_FIELD_BALANCE_VALUE",
                "FRM_FIELD_CURRENCY_ID",
                "FRM_FIELD_TYPE",
                "FRM_FIELD_LOCATION",

                //close balance
                "FRM_FIELD_CLOSE_DATE",

                //total opening cashier
                "FRM_FIELD_RUPIAH",
                "FRM_FIELD_USD",

                //untuk amount
                "FRM_FIELD_AMOUNT1",
                "FRM_FIELD_AMOUNT2",
                "FRM_FIELD_SUBTOTAL1",
                "FRM_FIELD_SUBTOTAL2",
                "FRM_FIELD_CURRENCY_ID2"

	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_DATE,
		TYPE_LONG,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_LONG,

                // untuk cash_balance
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG,

                //close date
                TYPE_DATE,

                //total opening cashier
                TYPE_FLOAT,
                TYPE_FLOAT,

                //untuk amount
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_LONG
	} ;

	public FrmCashCashier()
        {
	}

        public FrmCashCashier(CashCashier cashCashier)
        {
		this.cashCashier = cashCashier;
	}

	public FrmCashCashier(HttpServletRequest request, CashCashier cashCashier)
        {
		super(new FrmCashCashier(cashCashier), request);
		this.cashCashier = cashCashier;
	}

	public String getFormName() { return FRM_NAME_CASH_CASHIER; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public CashCashier getEntityObject(){ return cashCashier; }

	public void requestEntityObject(CashCashier cashCashier)
        {
		try
                {
			this.requestParam();
                        cashCashier.setCashMasterId(getLong(FRM_FIELD_CASH_MASTER_ID));
                        cashCashier.setAppUserId(getLong(FRM_FIELD_APP_USER_ID));
			cashCashier.setCashDate(getDate(FRM_FIELD_OPEN_DATE));
			cashCashier.setSpvOid(getLong(FRM_FIELD_SPV_OID));
			cashCashier.setSpvName(getString(FRM_FIELD_SPV_NAME));
                        cashCashier.setSpvCloseOid(getLong(FRM_FIELD_SPV_CLOSE_OID));
                        cashCashier.setSpvCloseName(getString(FRM_FIELD_SPV_CLOSE_NAME));
                        cashCashier.setShiftId(getLong(FRM_FIELD_SHIFT_ID));

                        //untuk cash_balance
                        cashCashier.setBalanceValue(getDouble(FRM_FIELD_BALANCE_VALUE));
                        cashCashier.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
                        cashCashier.setType(getInt(FRM_FIELD_TYPE));
                        cashCashier.setLocationId(getInt(FRM_FIELD_LOCATION));

                        //untuk close balance
                        cashCashier.setCloseDate(getDate(FRM_FIELD_CLOSE_DATE));

                        //untuk total opening cashier
                        cashCashier.setRupiah(getDouble(FRM_FIELD_RUPIAH));
                        cashCashier.setUsd(getDouble(FRM_FIELD_USD));

                        //UNTUK AMOUNT
                        cashCashier.setAmount1(getDouble(FRM_FIELD_AMOUNT1));
                        cashCashier.setAmount2(getDouble(FRM_FIELD_AMOUNT2));
                        cashCashier.setSubTotal1(getDouble(FRM_FIELD_SUBTOTAL1));
                        cashCashier.setSubTotal2(getDouble(FRM_FIELD_SUBTOTAL2));
                        cashCashier.setCurrencyId2(getLong(FRM_FIELD_CURRENCY_ID2));

		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
