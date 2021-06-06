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

import com.dimata.sedana.entity.masterdata.TransaksiPayment; 
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmSedanaTransaksiPayment extends FRMHandler implements I_FRMInterface, I_FRMType {

  private TransaksiPayment entSedanaTransaksiPayment;
  public static final String FRM_NAME_SEDANA_TRANSAKSI_PAYMENT = "FRM_NAME_SEDANA_TRANSAKSI_PAYMENT";
  public static final int FRM_FIELD_TRANSAKSI_PAYMENT_ID = 0;
  public static final int FRM_FIELD_PAYMENT_SYSTEM_ID = 1;
  public static final int FRM_FIELD_TRANSAKSI_ID = 2;
  public static final int FRM_FIELD_JUMLAH = 3;

  public static String[] fieldNames = {
    "FRM_FIELD_TRANSAKSI_PAYMENT_ID",
    "FRM_FIELD_PAYMENT_SYSTEM_ID",
    "FRM_FIELD_TRANSAKSI_ID",
    "FRM_FIELD_JUMLAH"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG
  };

  public FrmSedanaTransaksiPayment() {
  }

  public FrmSedanaTransaksiPayment(TransaksiPayment entSedanaTransaksiPayment) {
    this.entSedanaTransaksiPayment = entSedanaTransaksiPayment;
  }

  public FrmSedanaTransaksiPayment(HttpServletRequest request, TransaksiPayment entSedanaTransaksiPayment) {
    super(new FrmSedanaTransaksiPayment(entSedanaTransaksiPayment), request);
    this.entSedanaTransaksiPayment = entSedanaTransaksiPayment;
  }

  public String getFormName() {
    return FRM_NAME_SEDANA_TRANSAKSI_PAYMENT;
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

  public TransaksiPayment getEntityObject() {
    return entSedanaTransaksiPayment;
  }

  public void requestEntityObject(TransaksiPayment entSedanaTransaksiPayment) {
    try {
      this.requestParam();
      entSedanaTransaksiPayment.setOID(getLong(FRM_FIELD_TRANSAKSI_PAYMENT_ID));
      entSedanaTransaksiPayment.setPaymentSystemId(getLong(FRM_FIELD_PAYMENT_SYSTEM_ID));
      entSedanaTransaksiPayment.setTransaksiId(getLong(FRM_FIELD_TRANSAKSI_ID));
      entSedanaTransaksiPayment.setJumlah(getLong(FRM_FIELD_JUMLAH));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
