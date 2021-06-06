/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.payment;

/**
 *
 * @author Wiweka
 */
import com.dimata.pos.form.billing.*;
import com.dimata.pos.form.payment.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// qdep package
import com.dimata.qdep.form.*;

// project package
import com.dimata.pos.entity.masterCashier.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;

public class   FrmCashPayment extends FRMHandler implements I_FRMInterface, I_FRMType   {

    	private CashPayments1 cashPayment;

	public static final String FRM_NAME_CASH_PAYMENT	=  "FRM_NAME_CASH_PAYMENT" ;

	public static final int FRM_FIELD_CASH_PAYMENT_ID       =  0;
        public static final int FRM_FIELD_CASH_BILL_MAIN_ID     =  1;
	public static final int FRM_FIELD_PAYMENT_STATUS        =  2;
        public static final int FRM_FIELD_CURR_ID           =  3;
        public static final int FRM_FIELD_PAY_TYPE              =  4;
        public static final int FRM_FIELD_RATE                  =  5;
        public static final int FRM_FIELD_PAY_DATETIME          =  6;
        public static final int FRM_FIELD_PAY_AMOUNT                =  7;
        public static final int FRM_FIELD_AMOUNT_RETURN                =  8;
        public static final int FRM_FIELD_CASHCASHIER_ID                =  9;


	public static String[] fieldNames =
        {
            "FRM_FIELD_CASH_BILL_DETAIL_CODE_ID",
            "FRM_FIELD_CASH_BILL_MAIN_ID",
            "FRM_FIELD_PAYMENT_STATUS ",
            "FRM_FIELD_CURR_ID",
            "FRM_FIELD_PAY_TYPE",
            "FRM_FIELD_RATE",
            "FRM_FIELD_PAY_DATETIME",
            "FRM_FIELD_PAY_AMOUNT",

            "FRM_FIELD_AMOUNT_RETURN",
            "FRM_FIELD_CASHCASHIER_ID"



	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_DATE,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_LONG
	} ;

	public FrmCashPayment ()
        {
	}

        public FrmCashPayment (CashPayments1 cashPayment)
        {
		this.cashPayment= cashPayment;
	}

	public FrmCashPayment (HttpServletRequest request, CashPayments1 cashPayment)
        {
		super(new FrmCashPayment(cashPayment), request);
		this.cashPayment = cashPayment;
	}

	public String getFormName() { return FRM_NAME_CASH_PAYMENT; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public CashPayments1 getEntityObject(){ return cashPayment; }

	public void requestEntityObject(CashPayments1 cashPayment)
        {
		try
                {
			this.requestParam();
                        //cashPayment.setOID(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
                        cashPayment.setBillMainId(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
                        //cashPayment.setPaymentStatus(getInt(FRM_FIELD_PAYMENT_STATUS));
                        cashPayment.setCurrencyId(getLong(FRM_FIELD_CURR_ID));
                        cashPayment.setPaymentType(getLong(FRM_FIELD_PAY_TYPE));
                        cashPayment.setRate(getDouble(FRM_FIELD_RATE));
                        //cashPayment.setPayDateTime(getDate(FRM_FIELD_PAY_DATETIME));
                        cashPayment.setAmount(getDouble(FRM_FIELD_PAY_AMOUNT));
                        cashPayment.setAmountReturn(getDouble(FRM_FIELD_AMOUNT_RETURN));
                        cashPayment.setCashCashierId(getLong(FRM_FIELD_CASHCASHIER_ID)); 
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
