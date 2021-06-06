/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.payment;
/**
 *
 * @author Wiweka
 * 20130711
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

public class FrmCashCreditPayment extends FRMHandler implements I_FRMInterface, I_FRMType   {

    	private CashCreditPaymentsDinamis cashCreditPaymentsDinamis;

	public static final String FRM_NAME_CASH_CREDIT_PAYMENT	=  "FRM_NAME_CASH_CREDIT_PAYMENT" ;

	public static final int FRM_FIELD_CASH_CREDIT_PAYMENT_ID       =  0;
        public static final int FRM_FIELD_CURRENCY_ID       = 1;
        public static final int FRM_FIELD_CREDIT_MAIN_ID   = 2;
        public static final int FRM_FIELD_PAY_TYPE  =3;
        public static final int FRM_FIELD_RATE  =4;
        public static final int FRM_FIELD_PAY_AMOUNT    =5;
        public static final int FRM_FIELD_PAY_DATETIME  =6;
        public static final int FRM_FIELD_PAYMENT_STATUS    =7;


	public static String[] fieldNames =
        {
            "FRM_FIELD_CASH_CREDIT_PAYMENT_ID",
            "FRM_FIELD_CURRENCY_ID",
            "FRM_FIELD_CREDIT_MAIN_ID ",
            "FRM_FIELD_PAY_TYPE",
            "FRM_FIELD_RATE",
            "FRM_FIELD_PAY_AMOUNT",
            "FRM_FIELD_PAY_DATETIME",
            "FRM_FIELD_PAYMENT_STATUS"



	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,                
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT,                
                TYPE_FLOAT,
                TYPE_DATE,
                TYPE_INT
	} ;

	public FrmCashCreditPayment ()
        {
	}

        public FrmCashCreditPayment (CashCreditPaymentsDinamis cashCreditPaymentsDinamis)
        {
		this.cashCreditPaymentsDinamis= cashCreditPaymentsDinamis;
	}

	public FrmCashCreditPayment (HttpServletRequest request, CashCreditPaymentsDinamis cashCreditPaymentsDinamis)
        {
		super(new FrmCashCreditPayment(cashCreditPaymentsDinamis), request);
		this.cashCreditPaymentsDinamis = cashCreditPaymentsDinamis;
	}

	public String getFormName() { return FRM_NAME_CASH_CREDIT_PAYMENT; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public CashCreditPaymentsDinamis getEntityObject(){ return cashCreditPaymentsDinamis; }

	public void requestEntityObject(CashCreditPaymentsDinamis cashCreditPaymentsDinamis)
        {
		try
                {
			this.requestParam();
                        //cashCreditPaymentsDinamis.setOID(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
                        //cashCreditPaymentsDinamis.setCreditMainId(getLong(FRM_FIELD_CREDIT_MAIN_ID));
                        //cashCreditPaymentsDinamis.setPaymentStatus(getInt(FRM_FIELD_PAYMENT_STATUS));
                        cashCreditPaymentsDinamis.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
                        cashCreditPaymentsDinamis.setPaymentType(getLong(FRM_FIELD_PAY_TYPE));
                        cashCreditPaymentsDinamis.setRate(getDouble(FRM_FIELD_RATE));
                        //cashCreditPaymentsDinamis.setPayDateTime(getDate(FRM_FIELD_PAY_DATETIME));
                        cashCreditPaymentsDinamis.setAmount(getDouble(FRM_FIELD_PAY_AMOUNT));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
