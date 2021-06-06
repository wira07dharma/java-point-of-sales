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

public class FrmCashCreditPaymentInfo extends FRMHandler implements I_FRMInterface, I_FRMType   {

    	private CashCreditPaymentInfo cashCreditPaymentInfo;

	public static final String FRM_NAME_CASH_CREDIT_PAYMENT_INFO	=  "FRM_NAME_CASH_CREDIT_PAYMENT_INFO" ;

	public static final int FRM_FIELD_CREDIT_PAYMENT_INFO_ID       =  0;
        public static final int FRM_FIELD_CASH_CREDIT_PAYMENT_ID       = 1;
        public static final int FRM_FIELD_CC_NAME   = 2;
        public static final int FRM_FIELD_CC_NUMBER  =3;
        public static final int FRM_FIELD_EXPIRED_DATE  =4;
        public static final int FRM_FIELD_HOLDER_NAME    =5;
        public static final int FRM_FIELD_DEBIT_CARD_NAME  =6;
        public static final int FRM_FIELD_DEBIT_BANK_NAME    =7;
        public static final int FRM_FIELD_CHEQUE_ACCOUNT_NAME    =8;
        public static final int FRM_FIELD_CHEQUE_DUE_DATE    =9;
        public static final int FRM_FIELD_CHEQUE_BANK   =10;
        public static final int FRM_FIELD_CURRENCY_ID    =11;
        public static final int FRM_FIELD_RATE    =12;
        public static final int FRM_FIELD_AMOUNT    =13;





	public static String[] fieldNames =
        {
            "FRM_FIELD_CREDIT_PAYMENT_INFO_ID",
            "FRM_FIELD_CASH_CREDIT_PAYMENT_ID",
            "FRM_FIELD_CC_NAME ",
            "FRM_FIELD_CC_NUMBER",
            "FRM_FIELD_EXPIRED_DATE",
            "FRM_FIELD_HOLDER_NAME",
            "FRM_FIELD_DEBIT_CARD_NAME",
            "FRM_FIELD_DEBIT_BANK_NAME",
            "FRM_FIELD_CHEQUE_ACCOUNT_NAME",
            "FRM_FIELD_CHEQUE_DUE_DATE",
            "FRM_FIELD_CHEQUE_BANK",
            "FRM_FIELD_CURRENCY_ID",
            "FRM_FIELD_RATE",
            "FRM_FIELD_AMOUNT"



	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_DATE,
                TYPE_STRING,

                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_DATE,
                TYPE_STRING,

                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_FLOAT
	} ;

	public FrmCashCreditPaymentInfo ()
        {
	}

        public FrmCashCreditPaymentInfo (CashCreditPaymentInfo cashCreditPaymentInfo)
        {
		this.cashCreditPaymentInfo= cashCreditPaymentInfo;
	}

	public FrmCashCreditPaymentInfo (HttpServletRequest request, CashCreditPaymentInfo cashCreditPaymentInfo)
        {
		super(new FrmCashCreditPaymentInfo(cashCreditPaymentInfo), request);
		this.cashCreditPaymentInfo = cashCreditPaymentInfo;
	}

	public String getFormName() { return FRM_NAME_CASH_CREDIT_PAYMENT_INFO; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public CashCreditPaymentInfo getEntityObject(){ return cashCreditPaymentInfo; }

	public void requestEntityObject(CashCreditPaymentInfo cashCreditPaymentInfo)
        {
		try
                {
			this.requestParam();
                        //cashCreditPaymentInfo.setOID(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
                        cashCreditPaymentInfo.setCcName(getString(FRM_FIELD_CC_NAME));
                        cashCreditPaymentInfo.setDebitBankName(getString(FRM_FIELD_DEBIT_BANK_NAME));
                        cashCreditPaymentInfo.setPaymentId(getLong(FRM_FIELD_CASH_CREDIT_PAYMENT_ID));
                        cashCreditPaymentInfo.setCcNumber(getString(FRM_FIELD_CC_NUMBER));
                        cashCreditPaymentInfo.setExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));
                        cashCreditPaymentInfo.setDebitCardName(getString(FRM_FIELD_DEBIT_CARD_NAME));
                        cashCreditPaymentInfo.setHolderName(getString(FRM_FIELD_HOLDER_NAME));
                        cashCreditPaymentInfo.setAmount(getDouble(FRM_FIELD_AMOUNT));
                        cashCreditPaymentInfo.setChequeAccountName(getString(FRM_FIELD_CHEQUE_ACCOUNT_NAME));
                        cashCreditPaymentInfo.setRate(getDouble(FRM_FIELD_RATE));
                        cashCreditPaymentInfo.setChequeBank(getString(FRM_FIELD_CHEQUE_BANK));
                        cashCreditPaymentInfo.setChequeDueDate(getDate(FRM_FIELD_CHEQUE_DUE_DATE));
                        cashCreditPaymentInfo.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
