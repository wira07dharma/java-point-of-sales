/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.posbo.entity.masterdata.MasterGroupMapping;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMasterGroupMapping extends FRMHandler implements I_FRMInterface, I_FRMType {

  private MasterGroupMapping entMasterGroupMapping;
  public static final String FRM_NAME_MASTER_GROUP_MAPPING = "FRM_NAME_MASTER_GROUP_MAPPING";
  public static final int FRM_FIELD_MASTER_GROUP_MAPPING_ID = 0;
  public static final int FRM_FIELD_GROUP_ID = 1;
  public static final int FRM_FIELD_MODUL = 2;

  public static String[] fieldNames = {
    "FRM_FIELD_MASTER_GROUP_MAPPING_ID",
    "FRM_FIELD_GROUP_ID",
    "FRM_FIELD_MODUL"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_INT
  };

  public FrmMasterGroupMapping() {
  }

  public FrmMasterGroupMapping(MasterGroupMapping entMasterGroupMapping) {
    this.entMasterGroupMapping = entMasterGroupMapping;
  }

  public FrmMasterGroupMapping(HttpServletRequest request, MasterGroupMapping entMasterGroupMapping) {
    super(new FrmMasterGroupMapping(entMasterGroupMapping), request);
    this.entMasterGroupMapping = entMasterGroupMapping;
  }

  public String getFormName() {
    return FRM_NAME_MASTER_GROUP_MAPPING;
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

  public MasterGroupMapping getEntityObject() {
    return entMasterGroupMapping;
  }

  public void requestEntityObject(MasterGroupMapping entMasterGroupMapping) {
    try {
      this.requestParam();
      entMasterGroupMapping.setGroupId(getLong(FRM_FIELD_GROUP_ID));
      entMasterGroupMapping.setModul(getInt(FRM_FIELD_MODUL));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
