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

public class FrmCashCreditCard extends FRMHandler implements I_FRMInterface, I_FRMType   {

    	private CashCreditCard cashCreditCard;

	public static final String FRM_NAME_CASH_CREDIT_CARD	=  "FRM_NAME_CASH_CREDIT_CARD" ;

	public static final int FRM_FIELD_CC_ID       =  0;
        public static final int FRM_FIELD_PAYMENT_ID      = 1;
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
            "FRM_FIELD_CC_ID",
            "FRM_FIELD_PAYMENT_ID",
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

	public FrmCashCreditCard ()
        {
	}

        public FrmCashCreditCard (CashCreditCard cashCreditCard)
        {
		this.cashCreditCard= cashCreditCard;
	}

	public FrmCashCreditCard (HttpServletRequest request, CashCreditCard cashCreditCard)
        {
		super(new FrmCashCreditCard(cashCreditCard), request);
		this.cashCreditCard = cashCreditCard;
	}

	public String getFormName() { return FRM_NAME_CASH_CREDIT_CARD; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public CashCreditCard getEntityObject(){ return cashCreditCard; }

	public void requestEntityObject(CashCreditCard cashCreditCard)
        {
		try
                {
			this.requestParam();
                        //cashCreditCard.setOID(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
                        cashCreditCard.setCcName(getString(FRM_FIELD_CC_NAME));
                        cashCreditCard.setDebitBankName(getString(FRM_FIELD_DEBIT_BANK_NAME));
                        cashCreditCard.setPaymentId(getLong(FRM_FIELD_PAYMENT_ID));
                        cashCreditCard.setCcNumber(getString(FRM_FIELD_CC_NUMBER));
                        cashCreditCard.setExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));
                        cashCreditCard.setDebitCardName(getString(FRM_FIELD_DEBIT_CARD_NAME));
                        cashCreditCard.setHolderName(getString(FRM_FIELD_HOLDER_NAME));
                        cashCreditCard.setAmount(getDouble(FRM_FIELD_AMOUNT));
                        cashCreditCard.setChequeAccountName(getString(FRM_FIELD_CHEQUE_ACCOUNT_NAME));
                        cashCreditCard.setRate(getDouble(FRM_FIELD_RATE));
                        cashCreditCard.setChequeBank(getString(FRM_FIELD_CHEQUE_BANK));
                        cashCreditCard.setChequeDueDate(getDate(FRM_FIELD_CHEQUE_DUE_DATE));
                        cashCreditCard.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
