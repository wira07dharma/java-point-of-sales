/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.assigntabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.assigntabungan.AssignTabungan;
import javax.servlet.http.HttpServletRequest;

public class FrmAssignTabungan extends FRMHandler implements I_FRMInterface, I_FRMType {

  private AssignTabungan entAssignTabungan;
  public static final String FRM_NAME_ASSIGNTABUNGAN = "FRM_NAME_ASSIGNTABUNGAN";
  public static final int FRM_FIELD_ASSIGNMASTERTABUNGANID = 0;
  public static final int FRM_FIELD_MASTERTABUNGAN = 1;
  public static final int FRM_FIELD_IDJENISSIMPANAN = 2;

  public static String[] fieldNames = {
    "FRM_FIELD_ASSIGNMASTERTABUNGANID",
    "FRM_FIELD_MASTERTABUNGAN",
    "FRM_FIELD_IDJENISSIMPANAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG
  };

  public FrmAssignTabungan() {
  }

  public FrmAssignTabungan(AssignTabungan entAssignTabungan) {
    this.entAssignTabungan = entAssignTabungan;
  }

  public FrmAssignTabungan(HttpServletRequest request, AssignTabungan entAssignTabungan) {
    super(new FrmAssignTabungan(entAssignTabungan), request);
    this.entAssignTabungan = entAssignTabungan;
  }

  public String getFormName() {
    return FRM_NAME_ASSIGNTABUNGAN;
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

  public AssignTabungan getEntityObject() {
    return entAssignTabungan;
  }

  public void requestEntityObject(AssignTabungan entAssignTabungan) {
    try {
      this.requestParam();
      entAssignTabungan.setAssignMasterTabunganId(getLong(FRM_FIELD_ASSIGNMASTERTABUNGANID));
      entAssignTabungan.setMasterTabungan(getLong(FRM_FIELD_MASTERTABUNGAN));
      entAssignTabungan.setIdJenisSimpanan(getLong(FRM_FIELD_IDJENISSIMPANAN));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
