/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.sedana.entity.masterdata.CashTellerBalance;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

public class FrmCashTellerBalance extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final int STATUS_OPEN = 0;
  public static final int STATUS_CLOSE = 1;
  
  private CashTellerBalance entCashTellerBalance;
  public static final String FRM_NAME_CASHTELLERBALANCE = "FRM_NAME_CASHTELLERBALANCE";
  public static final int FRM_FIELD_CASHBALANCEID = 0;
  public static final int FRM_FIELD_TELLERSHIFTID = 1;
  public static final int FRM_FIELD_CURRENCYID = 2;
  public static final int FRM_FIELD_TYPE = 3;
  public static final int FRM_FIELD_BALANCEDATE = 4;
  public static final int FRM_FIELD_BALANCEVALUE = 5;
  public static final int FRM_FIELD_SHOULDVALUE = 6;
  public static final int FRM_FIELD_PAYMENT_SYSTEM_ID = 7;

  public static String[] fieldNames = {
    "FRM_FIELD_CASHBALANCEID",
    "FRM_FIELD_TELLERSHIFTID",
    "FRM_FIELD_CURRENCYID",
    "FRM_FIELD_TYPE",
    "FRM_FIELD_BALANCEDATE",
    "FRM_FIELD_BALANCEVALUE",
    "FRM_FIELD_SHOULDVALUE",
    "FRM_FIELD_PAYMENT_SYSTEM_ID"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_INT,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_FLOAT,
    TYPE_LONG
  };

  public FrmCashTellerBalance() {
  }

  public FrmCashTellerBalance(CashTellerBalance entCashTellerBalance) {
    this.entCashTellerBalance = entCashTellerBalance;
  }

  public FrmCashTellerBalance(HttpServletRequest request, CashTellerBalance entCashTellerBalance) {
    super(new FrmCashTellerBalance(entCashTellerBalance), request);
    this.entCashTellerBalance = entCashTellerBalance;
  }

  public String getFormName() {
    return FRM_NAME_CASHTELLERBALANCE;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int getFieldSize() {
    return fieldNames.length;
  }

  public CashTellerBalance getEntityObject() {
    return entCashTellerBalance;
  }

  public void requestEntityObject(CashTellerBalance entCashTellerBalance) {
    try {
      this.requestParam();
      entCashTellerBalance.setCashBalanceId(getLong(FRM_FIELD_CASHBALANCEID));
      entCashTellerBalance.setTellerShiftId(getLong(FRM_FIELD_TELLERSHIFTID));
      entCashTellerBalance.setCurrencyId(getLong(FRM_FIELD_CURRENCYID));
      entCashTellerBalance.setType(getInt(FRM_FIELD_TYPE));
      entCashTellerBalance.setBalanceDate(Formater.formatDate(getString(FRM_FIELD_BALANCEDATE), "yyyy-MM-dd HH:mm:ss"));
      entCashTellerBalance.setBalanceValue(getDouble(FRM_FIELD_BALANCEVALUE));
      entCashTellerBalance.setShouldValue(getDouble(FRM_FIELD_SHOULDVALUE));
      entCashTellerBalance.setPaymentSystemId(getLong(FRM_FIELD_PAYMENT_SYSTEM_ID));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
