/**
 * User: gwawan
 * Date: Mei 9, 2007
 * Time: 4:46:00 PM
 * Version: 1.0
 */
package com.dimata.common.form.payment;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.common.entity.payment.PaymentInfo;

import javax.servlet.http.HttpServletRequest;

public class FrmPaymentInfo extends FRMHandler implements I_FRMInterface, I_FRMType {
    private PaymentInfo paymentInfo;
    public static final String FRM_NAME_PAYMENT_INFO = "FRM_NAME_PAYMENT_INFO";
    
    public static final int FRM_FIELD_PAYMENT_INFO_ID           =  0 ;
    public static final int FRM_FIELD_BANK_NAME                 =  1 ;
    public static final int FRM_FIELD_BANK_ADDRESS              =  2 ;
    public static final int FRM_FIELD_SWIFT_CODE                =  3 ;
    public static final int FRM_FIELD_ACCOUNT_NAME              =  4 ;
    public static final int FRM_FIELD_ACCOUNT_NUMBER            =  5 ;
    public static final int FRM_FIELD_NAME_ON_CARD              =  6 ;
    public static final int FRM_FIELD_CARD_NUMBER               =  7 ;
    public static final int FRM_FIELD_CARD_ID                   =  8 ;
    public static final int FRM_FIELD_EXPIRED_DATE              =  9 ;
    public static final int FRM_FIELD_CHECK_BG_NUMBER           =  10 ;
    public static final int FRM_FIELD_BANK_COST                 =  11 ;
    public static final int FRM_FIELD_PURCH_PAYMENT_ID          =  12 ;
    public static final int FRM_FIELD_PAY_ADDRESS               =  13 ;
    public static final int FRM_FIELD_DUE_DATE                  =  14;
    public static final int FRM_FIELD_TANGGAL_PENCAIRAN         =  15;
    
    public static String[] fieldNames = {
        "FRM_FIELD_PAYMENT_INFO_ID",
        "FRM_FIELD_BANK_NAME",
        "FRM_FIELD_BANK_ADDRESS",
        "FRM_FIELD_SWIFT_CODE",
        "FRM_FIELD_ACCOUNT_NAME",
        "FRM_FIELD_ACCOUNT_NUMBER",
        "FRM_FIELD_NAME_ON_CARD",
        "FRM_FIELD_CARD_NUMBER",
        "FRM_FIELD_CARD_ID",
        "FRM_FIELD_EXPIRED_DATE",
        "FRM_FIELD_CHECK_BG_NUMBER",
        "FRM_FIELD_BANK_COST",
        "FRM_FIELD_PURCH_PAYMENT_ID",
        "FRM_FIELD_PAY_ADDRESS",
        "FRM_FIELD_DUE_DATE",
        "FRM_FIELD_TANGGAL_PENCAIRAN"
    };
    
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE
    };
    
    public FrmPaymentInfo(){
    }
    public FrmPaymentInfo(PaymentInfo paymentInfo){
        this.paymentInfo = paymentInfo;
    }
    
    public FrmPaymentInfo(HttpServletRequest request, PaymentInfo paymentInfo){
        super(new FrmPaymentInfo(paymentInfo), request);
        this.paymentInfo = paymentInfo;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getFormName() {
        return FRM_NAME_PAYMENT_INFO;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public void requestEntityObject(PaymentInfo paymentInfo) {
        try{
            this.requestParam();
            paymentInfo.setOID(getLong(FRM_FIELD_PAYMENT_INFO_ID));
            paymentInfo.setlPurchPaymentId(getLong(FRM_FIELD_PURCH_PAYMENT_ID));
            paymentInfo.setStBankName(getString(FRM_FIELD_BANK_NAME));
            paymentInfo.setStBankAddress(getString(FRM_FIELD_BANK_ADDRESS));
            paymentInfo.setStSwiftCade(getString(FRM_FIELD_SWIFT_CODE));
            paymentInfo.setStAccountName(getString(FRM_FIELD_ACCOUNT_NAME));
            paymentInfo.setStAccountNumber(getString(FRM_FIELD_ACCOUNT_NUMBER));
            paymentInfo.setStNameOnCard(getString(FRM_FIELD_NAME_ON_CARD));
            paymentInfo.setStCardNumber(getString(FRM_FIELD_CARD_NUMBER));
            paymentInfo.setStCardId(getString(FRM_FIELD_CARD_ID));
            paymentInfo.setDtExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));
            paymentInfo.setStCheckBGNumber(getString(FRM_FIELD_CHECK_BG_NUMBER));
            paymentInfo.setdBankCost(getDouble(FRM_FIELD_BANK_COST));
            paymentInfo.setStPaymentAddress(getString(FRM_FIELD_PAY_ADDRESS));
            paymentInfo.setDueDate(getDate(FRM_FIELD_DUE_DATE));
            paymentInfo.setTanggalPencairan(getDate(FRM_FIELD_TANGGAL_PENCAIRAN));
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

