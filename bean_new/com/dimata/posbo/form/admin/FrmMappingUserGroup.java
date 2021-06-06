/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.admin;

import com.dimata.posbo.entity.admin.MappingUserGroup;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMappingUserGroup extends FRMHandler implements I_FRMInterface, I_FRMType {

  private MappingUserGroup entMappingUserGroup;
  public static final String FRM_NAME_MAPPING_USER_GROUP = "FRM_NAME_MAPPING_USER_GROUP";
  public static final int FRM_FIELD_MAPPING_USER_GROUP_ID = 0;
  public static final int FRM_FIELD_USER_ID = 1;
  public static final int FRM_FIELD_GROUP_USER_ID = 2;
  public static final int FRM_FIELD_COMPANY_ID = 3;

  public static String[] fieldNames = {
    "FRM_FIELD_MAPPING_USER_GROUP_ID",
    "FRM_FIELD_USER_ID",
    "FRM_FIELD_GROUP_USER_ID",
    "FRM_FIELD_COMPANY_ID"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG
  };

  public FrmMappingUserGroup() {
  }

  public FrmMappingUserGroup(MappingUserGroup entMappingUserGroup) {
    this.entMappingUserGroup = entMappingUserGroup;
  }

  public FrmMappingUserGroup(HttpServletRequest request, MappingUserGroup entMappingUserGroup) {
    super(new FrmMappingUserGroup(entMappingUserGroup), request);
    this.entMappingUserGroup = entMappingUserGroup;
  }

  public String getFormName() {
    return FRM_NAME_MAPPING_USER_GROUP;
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

  public MappingUserGroup getEntityObject() {
    return entMappingUserGroup;
  }

  public void requestEntityObject(MappingUserGroup entMappingUserGroup) {
    try {
      this.requestParam();
      entMappingUserGroup.setUserId(getLong(FRM_FIELD_USER_ID));
      entMappingUserGroup.setGroupUserId(getLong(FRM_FIELD_GROUP_USER_ID));
      entMappingUserGroup.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
