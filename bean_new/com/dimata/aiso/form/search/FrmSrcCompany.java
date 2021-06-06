/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.search;

import com.dimata.aiso.entity.search.SrcCompany;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * @author dwi
 */
public class FrmSrcCompany extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_SEARCH_COMPANY = "FRM_SEARCH_COMPANY";
  public static final int FRM_FIELD_CODE = 0;
  public static final int FRM_FIELD_NAME = 1;
  public static final int FRM_FIELD_ADDRESS = 2;
  public static final int FRM_FIELD_ORDER = 3;
  public static final int FRM_FIELD_CITY = 4;
  public static final int FRM_FIELD_PROVINCE = 5;
  public static final int FRM_FIELD_COUNTRY = 6;

  public static String[] fieldNames = {
    "FRM_SEARCH_CODE",
    "FRM_SEARCH_NAME",
    "FRM_SEARCH_ADDRESS",
    "FRM_SEARCH_ORDERBY",
    "FRM_FIELD_CITY",
    "FRM_FIELD_PROVINCE",
    "FRM_FIELD_COUNTRY"
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
  public static final int SORT_BY_PROVINCE = 4;
  public static final int SORT_BY_COUNTRY = 5;

  public static String[][] sortFieldNames = {
    {"Kode", "Nama", "Alamat", "Kota", "Propinsi", "Negara"},
    {"Code", "Name", "Address", "Town", "Province", "Country"}
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
    {"Kode", "Nama", "Alamat", "Kota", "Propinsi", "Negara"},
    {"Code", "Name", "Address", "Town", "Province", "Country"}
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

  private SrcCompany srcCompany = new SrcCompany();

  public FrmSrcCompany() {
  }

  public FrmSrcCompany(SrcCompany srcCompany) {
    this.srcCompany = srcCompany;
  }

  public FrmSrcCompany(HttpServletRequest request, SrcCompany srcCompany) {
    super(new FrmSrcCompany(srcCompany), request);
    this.srcCompany = srcCompany;
  }

  public String getFormName() {
    return this.FRM_SEARCH_COMPANY;
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

  public SrcCompany getEntityObject() {
    return srcCompany;
  }

  public void requestEntityObject(SrcCompany srcCompany) {
    try {
      this.requestParam();

      srcCompany.setCode(getString(FRM_FIELD_CODE));
      srcCompany.setName(getString(FRM_FIELD_NAME));
      srcCompany.setAddress(getString(FRM_FIELD_ADDRESS));
      srcCompany.setOrderBy(getInt(FRM_FIELD_ORDER));
      srcCompany.setTown(getString(FRM_FIELD_CITY));
      srcCompany.setProvince(getString(FRM_FIELD_PROVINCE));
      srcCompany.setCountry(getString(FRM_FIELD_COUNTRY));

      this.srcCompany = srcCompany;
    } catch (Exception e) {
      System.out.println("Exception on request entity object" + e.toString());
      srcCompany = new SrcCompany();
    }
  }
}
