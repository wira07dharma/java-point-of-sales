/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.search;

import com.dimata.aiso.entity.search.SrcBussinessGroup;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmSrcBussGroup extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_SRC_BUSS_GROUP = "FRM_SRC_BUSS_GROUP";
  public static final int FRM_FIELD_CODE = 0;
  public static final int FRM_FIELD_NAME = 1;
  public static final int FRM_FIELD_ADDRESS = 2;
  public static final int FRM_FIELD_ORDER = 3;
  public static final int FRM_FIELD_CITY = 4;
  public static final int FRM_FIELD_PHONE = 5;
  public static final int FRM_FIELD_FAX = 6;

  public static String[] fieldNames = {
    "FRM_SEARCH_CODE",
    "FRM_SEARCH_NAME",
    "FRM_SEARCH_ADDRESS",
    "FRM_SEARCH_ORDERBY",
    "FRM_FIELD_CITY",
    "FRM_FIELD_PHONE",
    "FRM_FIELD_FAX"
  };

  public static int[] fieldTypes = {
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_INT,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING
  };

  public static final int SORT_BY_CODE = 0;
  public static final int SORT_BY_NAME = 1;
  public static final int SORT_BY_ADDRESS = 2;
  public static final int SORT_BY_CITY = 3;
  public static final int SORT_BY_PHONE = 4;
  public static final int SORT_BY_FAX = 5;

  public static String[][] sortFieldNames = {
    {"Kode", "Nama", "Alamat", "Kota", "Telp", "Fax"},
    {"Code", "Name", "Address", "Town", "Phone", "Fax"}
  };

  public static final int[] orderValue = {
    0,
    1,
    2,
    3,
    4,
    5
  };

  public static final String[][] orderKey = {
    {"Kode", "Nama", "Alamat", "Kota", "Telp", "Fax"},
    {"Code", "Name", "Address", "City", "Phone", "Fax"}
  };

  public static Vector getOrderValue() {
    Vector order = new Vector();
    for (int i = 0; i < orderValue.length; i++) {
      order.add(String.valueOf(orderValue[i]));
    }
    return order;
  }

  public static Vector getOrderKey(int language) {
    Vector order = new Vector();
    for (int i = 0; i < orderKey[language].length; i++) {
      order.add(orderKey[language][i]);
    }
    return order;
  }

  private SrcBussinessGroup srcBussinessGroup = new SrcBussinessGroup();

  public FrmSrcBussGroup() {
  }

  public FrmSrcBussGroup(SrcBussinessGroup srcBussinessGroup) {
    this.srcBussinessGroup = srcBussinessGroup;
  }

  public FrmSrcBussGroup(HttpServletRequest request, SrcBussinessGroup srcBussinessGroup) {
    super(new FrmSrcBussGroup(srcBussinessGroup), request);
    this.srcBussinessGroup = srcBussinessGroup;
  }

  public String getFormName() {
    return this.FRM_SRC_BUSS_GROUP;
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

  public static Vector getSortOrder() {
    Vector sortOrder = new Vector(1, 1);
    for (int i = 0; i < sortFieldNames.length; i++) {
      sortOrder.add(sortFieldNames[i]);
    }
    return sortOrder;
  }

  public SrcBussinessGroup getEntityObject() {
    return srcBussinessGroup;
  }

  public void requestEntityObject(SrcBussinessGroup srcBussinessGroup) {
    try {
      this.requestParam();

      srcBussinessGroup.setCode(getString(FRM_FIELD_CODE));
      srcBussinessGroup.setName(getString(FRM_FIELD_NAME));
      srcBussinessGroup.setAddress(getString(FRM_FIELD_ADDRESS));
      srcBussinessGroup.setOrderBy(getInt(FRM_FIELD_ORDER));
      srcBussinessGroup.setCity(getString(FRM_FIELD_CITY));
      srcBussinessGroup.setPhone(getString(FRM_FIELD_PHONE));
      srcBussinessGroup.setFax(getString(FRM_FIELD_FAX));

      this.srcBussinessGroup = srcBussinessGroup;
    } catch (Exception e) {
      System.out.println("Exception on request entity object" + e.toString());
      srcBussinessGroup = new SrcBussinessGroup();
    }
  }
}
