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
import com.dimata.sedana.entity.masterdata.AssignContact;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmAssignContact extends FRMHandler implements I_FRMInterface, I_FRMType {

  private AssignContact entAssignContact;
  public static final String FRM_NAME_ASSIGNCONTACT = "FRM_NAME_ASSIGNCONTACT";
  public static final int FRM_FIELD_ASSIGNTABUNGANID = 0;
  public static final int FRM_FIELD_MASTERTABUNGANID = 1;
  public static final int FRM_FIELD_CONTACTID = 2;
  public static final int FRM_FIELD_NOTABUNGAN = 3;

  public static String[] fieldNames = {
    "FRM_FIELD_ASSIGNTABUNGANID",
    "FRM_FIELD_MASTERTABUNGANID",
    "FRM_FIELD_CONTACTID",
    "FRM_FIELD_NOTABUNGAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING
  };

  public FrmAssignContact() {
  }

  public FrmAssignContact(AssignContact entAssignContact) {
    this.entAssignContact = entAssignContact;
  }

  public FrmAssignContact(HttpServletRequest request, AssignContact entAssignContact) {
    super(new FrmAssignContact(entAssignContact), request);
    this.entAssignContact = entAssignContact;
  }

  public String getFormName() {
    return FRM_NAME_ASSIGNCONTACT;
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

  public AssignContact getEntityObject() {
    return entAssignContact;
  }

  public void requestEntityObject(AssignContact entAssignContact) {
    try {
      this.requestParam();
      entAssignContact.setOID(getLong(FRM_FIELD_ASSIGNTABUNGANID));
      entAssignContact.setMasterTabunganId(getLong(FRM_FIELD_MASTERTABUNGANID));
      entAssignContact.setContactId(getLong(FRM_FIELD_CONTACTID));
      entAssignContact.setNoTabungan(getString(FRM_FIELD_NOTABUNGAN));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
