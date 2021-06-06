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
import com.dimata.sedana.entity.masterdata.KolektibilitasPembayaran;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmKolektibilitasPembayaran extends FRMHandler implements I_FRMInterface, I_FRMType {

  private KolektibilitasPembayaran entKolektibilitasPembayaran;
  public static final String FRM_NAME_KOLEKTIBILITASPEMBAYARAN = "FRM_NAME_KOLEKTIBILITASPEMBAYARAN";
  public static final int FRM_FIELD_KOLEKTIBILITASID = 0;
  public static final int FRM_FIELD_KODEKOLEKTIBILITAS = 1;
  public static final int FRM_FIELD_TINGKATKOLEKTIBILITAS = 2;
  public static final int FRM_FIELD_JUDULKOLEKTIBILITAS = 3;
  public static final int FRM_FIELD_MAXHARITUNGGAKANPOKOK = 4;
  public static final int FRM_FIELD_MAXHARIJUMLAHTUNGGAKANBUNGA = 5;
  public static final int FRM_FIELD_TINGKATRESIKO = 6;

  public static String[] fieldNames = {
    "FRM_FIELD_KOLEKTIBILITASID",
    "FRM_FIELD_KODEKOLEKTIBILITAS",
    "FRM_FIELD_TINGKATKOLEKTIBILITAS",
    "FRM_FIELD_JUDULKOLEKTIBILITAS",
    "FRM_FIELD_MAXHARITUNGGAKANPOKOK",
    "FRM_FIELD_MAXHARIJUMLAHTUNGGAKANBUNGA",
    "FRM_FIELD_TINGKATRESIKO"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_STRING,
    TYPE_INT,
    TYPE_STRING,
    TYPE_INT,
    TYPE_INT,
    TYPE_FLOAT
  };

  public FrmKolektibilitasPembayaran() {
  }

  public FrmKolektibilitasPembayaran(KolektibilitasPembayaran entKolektibilitasPembayaran) {
    this.entKolektibilitasPembayaran = entKolektibilitasPembayaran;
  }

  public FrmKolektibilitasPembayaran(HttpServletRequest request, KolektibilitasPembayaran entKolektibilitasPembayaran) {
    super(new FrmKolektibilitasPembayaran(entKolektibilitasPembayaran), request);
    this.entKolektibilitasPembayaran = entKolektibilitasPembayaran;
  }

  public String getFormName() {
    return FRM_NAME_KOLEKTIBILITASPEMBAYARAN;
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

  public KolektibilitasPembayaran getEntityObject() {
    return entKolektibilitasPembayaran;
  }

  public void requestEntityObject(KolektibilitasPembayaran entKolektibilitasPembayaran) {
    try {
      this.requestParam();
      entKolektibilitasPembayaran.setOID(getLong(FRM_FIELD_KOLEKTIBILITASID));
      entKolektibilitasPembayaran.setKodeKolektibilitas(getString(FRM_FIELD_KODEKOLEKTIBILITAS));
      entKolektibilitasPembayaran.setTingkatKolektibilitas(getInt(FRM_FIELD_TINGKATKOLEKTIBILITAS));
      entKolektibilitasPembayaran.setJudulKolektibilitas(getString(FRM_FIELD_JUDULKOLEKTIBILITAS));
      entKolektibilitasPembayaran.setTingkatResiko(getFloat(FRM_FIELD_TINGKATRESIKO));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
