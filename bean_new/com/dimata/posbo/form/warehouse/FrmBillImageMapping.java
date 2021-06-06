/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

/**
 *
 * @author Regen
 */
import com.dimata.posbo.entity.warehouse.BillImageMapping;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmBillImageMapping extends FRMHandler implements I_FRMInterface, I_FRMType {

  private BillImageMapping entBillImageMapping;
  public static final String FRM_NAME_BILLIMAGEMAPPING = "FRM_NAME_BILLIMAGEMAPPING";
  public static final int FRM_FIELD_IMAGE_ID = 0;
  public static final int FRM_FIELD_BILL_MAIN_ID = 1;
  public static final int FRM_FIELD_FILE_NAME = 2;
  public static final int FRM_FIELD_DOCUMENT_NAME = 3;
  public static final int FRM_FIELD_KETERANGAN = 4;

  public static String[] fieldNames = {
    "FRM_FIELD_IMAGE_ID",
    "FRM_FIELD_BILL_MAIN_ID",
    "FRM_FIELD_FILE_NAME",
    "FRM_FIELD_DOCUMENT_NAME",
    "FRM_FIELD_KETERANGAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING
  };

  public FrmBillImageMapping() {
  }

  public FrmBillImageMapping(BillImageMapping entBillImageMapping) {
    this.entBillImageMapping = entBillImageMapping;
  }

  public FrmBillImageMapping(HttpServletRequest request, BillImageMapping entBillImageMapping) {
    super(new FrmBillImageMapping(entBillImageMapping), request);
    this.entBillImageMapping = entBillImageMapping;
  }

  public String getFormName() {
    return FRM_NAME_BILLIMAGEMAPPING;
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

  public BillImageMapping getEntityObject() {
    return entBillImageMapping;
  }

  public void requestEntityObject(BillImageMapping entBillImageMapping) {
    try {
      this.requestParam();
      entBillImageMapping.setOID(getLong(FRM_FIELD_IMAGE_ID));
      entBillImageMapping.setBillMainId(getLong(FRM_FIELD_BILL_MAIN_ID));
      entBillImageMapping.setFileName(getString(FRM_FIELD_FILE_NAME));
      entBillImageMapping.setDocumentName(getString(FRM_FIELD_DOCUMENT_NAME));
      entBillImageMapping.setKeterangan(getString(FRM_FIELD_KETERANGAN));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
