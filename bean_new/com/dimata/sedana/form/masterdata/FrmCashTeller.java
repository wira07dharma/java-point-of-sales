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
import com.dimata.sedana.entity.masterdata.CashTeller;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

public class FrmCashTeller extends FRMHandler implements I_FRMInterface, I_FRMType {

  private CashTeller entCashTeller;
  public static final String FRM_NAME_CASHTELLER = "FRM_NAME_CASHTELLER";
  public static final int FRM_FIELD_TELLER_SHIFT_ID = 0;
  public static final int FRM_FIELD_MASTER_LOKET_ID = 1;
  public static final int FRM_FIELD_APP_USER_ID = 2;
  public static final int FRM_FIELD_OPEN_DATE = 3;
  public static final int FRM_FIELD_SPV_OPEN_ID = 4;
  public static final int FRM_FIELD_SPV_OPEN_NAME = 5;
  public static final int FRM_FIELD_SPV_CLOSE_ID = 6;
  public static final int FRM_FIELD_SPV_CLOSE_NAME = 7;
  public static final int FRM_FIELD_SHIFT_ID = 8;
  public static final int FRM_FIELD_CLOSE_DATE = 9;
  public static final int FRM_FIELD_STATUS = 10;

  public static String[] fieldNames = {
    "FRM_FIELD_TELLER_SHIFT_ID",
    "FRM_FIELD_MASTER_LOKET_ID",
    "FRM_FIELD_APP_USER_ID",
    "FRM_FIELD_OPEN_DATE",
    "FRM_FIELD_SPV_OPEN_ID",
    "FRM_FIELD_SPV_OPEN_NAME",
    "FRM_FIELD_SPV_CLOSE_ID",
    "FRM_FIELD_SPV_CLOSE_NAME",
    "FRM_FIELD_SHIFT_ID",
    "FRM_FIELD_CLOSE_DATE",
    "FRM_FIELD_STATUS"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_INT
  };

  public FrmCashTeller() {
  }

  public FrmCashTeller(CashTeller entCashTeller) {
    this.entCashTeller = entCashTeller;
  }

  public FrmCashTeller(HttpServletRequest request, CashTeller entCashTeller) {
    super(new FrmCashTeller(entCashTeller), request);
    this.entCashTeller = entCashTeller;
  }

  public String getFormName() {
    return FRM_NAME_CASHTELLER;
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

  public CashTeller getEntityObject() {
    return entCashTeller;
  }

  public void requestEntityObject(CashTeller entCashTeller) {
    try {
      this.requestParam();
      entCashTeller.setOID(getLong(FRM_FIELD_TELLER_SHIFT_ID));
      entCashTeller.setMasterLoketId(getLong(FRM_FIELD_MASTER_LOKET_ID));
      entCashTeller.setAppUserId(getLong(FRM_FIELD_APP_USER_ID));
      entCashTeller.setOpenDate(Formater.formatDate(getString(FRM_FIELD_OPEN_DATE), "yyyy-MM-dd HH:mm:ss"));
      entCashTeller.setSpvOpenId(getLong(FRM_FIELD_SPV_OPEN_ID));
      entCashTeller.setSpvOpenName(getString(FRM_FIELD_SPV_OPEN_NAME));
      entCashTeller.setSpvCloseId(getLong(FRM_FIELD_SPV_CLOSE_ID));
      entCashTeller.setSpvCloseName(getString(FRM_FIELD_SPV_CLOSE_NAME));
      entCashTeller.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
      entCashTeller.setCloseDate(Formater.formatDate(getString(FRM_FIELD_CLOSE_DATE), "yyyy-MM-dd HH:mm:ss"));
      entCashTeller.setStatus(getInt(FRM_FIELD_STATUS));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
