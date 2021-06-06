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
import com.dimata.sedana.entity.masterdata.MasterTabungan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMasterTabungan extends FRMHandler implements I_FRMInterface, I_FRMType {

  private MasterTabungan entMasterTabungan;
  public static final String FRM_NAME_MASTERTABUNGAN = "FRM_NAME_MASTERTABUNGAN";
  public static final int FRM_FIELD_MASTERTABUNGANID = 0;
  public static final int FRM_FIELD_KODETABUNGAN = 1;
  public static final int FRM_FIELD_NAMATABUNGAN = 2;
  public static final int FRM_FIELD_KET = 3;
  public static final int FRM_FIELD_DENDA = 4;
  public static final int FRM_FIELD_DENDA_JENIS = 5;
  public static final int FRM_FIELD_MIN_SALDO_BUNGA = 6;

  public static String[] fieldNames = {
    "FRM_FIELD_MASTERTABUNGANID",
    "FRM_FIELD_KODETABUNGAN",
    "FRM_FIELD_NAMATABUNGAN",
    "FRM_FIELD_KET",
    "FRM_FIELD_D",
    "FRM_FIELD_DENDA_JENIS",
    "FRM_MIN_SALDO_BUNGA",
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_STRING + ENTRY_REQUIRED,
    TYPE_STRING + ENTRY_REQUIRED,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_INT,
    TYPE_FLOAT
  };

  public FrmMasterTabungan() {
  }

  public FrmMasterTabungan(MasterTabungan entMasterTabungan) {
    this.entMasterTabungan = entMasterTabungan;
  }

  public FrmMasterTabungan(HttpServletRequest request, MasterTabungan entMasterTabungan) {
    super(new FrmMasterTabungan(entMasterTabungan), request);
    this.entMasterTabungan = entMasterTabungan;
  }

  public String getFormName() {
    return FRM_NAME_MASTERTABUNGAN;
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

  public MasterTabungan getEntityObject() {
    return entMasterTabungan;
  }

  public void requestEntityObject(MasterTabungan entMasterTabungan) {
    try {
      this.requestParam();
      entMasterTabungan.setMasterTabunganId(getLong(FRM_FIELD_MASTERTABUNGANID));
      entMasterTabungan.setKodeTabungan(getString(FRM_FIELD_KODETABUNGAN));
      entMasterTabungan.setNamaTabungan(getString(FRM_FIELD_NAMATABUNGAN));
      entMasterTabungan.setKet(getString(FRM_FIELD_KET));
      entMasterTabungan.setDenda(getDouble(FRM_FIELD_DENDA));
      entMasterTabungan.setJenisDenda(getInt(FRM_FIELD_DENDA_JENIS));
      entMasterTabungan.setMinSaldoBunga(getDouble(FRM_FIELD_MIN_SALDO_BUNGA));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
