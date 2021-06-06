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
import com.dimata.sedana.entity.masterdata.JangkaWaktu;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmJangkaWaktu extends FRMHandler implements I_FRMInterface, I_FRMType {

  private JangkaWaktu entJangkaWaktu;
  public static final String FRM_NAME_JANGKA_WAKTU = "FRM_NAME_JANGKA_WAKTU";
  public static final int FRM_FIELD_JANGKA_WAKTU_ID = 0;
  public static final int FRM_FIELD_JANGKA_WAKTU = 1;

  public static String[] fieldNames = {
    "FRM_FIELD_JANGKA_WAKTU_ID",
    "FRM_FIELD_JANGKA_WAKTU"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_INT
  };

  public FrmJangkaWaktu() {
  }

  public FrmJangkaWaktu(JangkaWaktu entJangkaWaktu) {
    this.entJangkaWaktu = entJangkaWaktu;
  }

  public FrmJangkaWaktu(HttpServletRequest request, JangkaWaktu entJangkaWaktu) {
    super(new FrmJangkaWaktu(entJangkaWaktu), request);
    this.entJangkaWaktu = entJangkaWaktu;
  }

  public String getFormName() {
    return FRM_NAME_JANGKA_WAKTU;
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

  public JangkaWaktu getEntityObject() {
    return entJangkaWaktu;
  }

  public void requestEntityObject(JangkaWaktu entJangkaWaktu) {
    try {
      this.requestParam();
      entJangkaWaktu.setJangkaWaktuId(getLong(FRM_FIELD_JANGKA_WAKTU_ID));
      entJangkaWaktu.setJangkaWaktu(getInt(FRM_FIELD_JANGKA_WAKTU));
    } catch (Exception e) { 
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
