/*
 * FrmSrcActvity.java
 *
 * Created on February 23, 2007, 9:03 AM
 */
package com.dimata.aiso.form.search;

import com.dimata.aiso.entity.search.SrcActivity;
/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.search.*;

/**
 *
 * @author dwi
 */
public class FrmSrcActivity extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_SRC_ACTIVITY = "FRM_SRC_ACTIVITY";

  /**
   * Search parameter
   */
  public static final int FRM_CODE = 0;
  public static final int FRM_DESCRIPTION = 1;
  public static final int FRM_LEVEL = 2;
  public static final int FRM_POSTED = 3;
  public static final int FRM_TYPE = 4;
  public static final int FRM_ORDERBY = 5;

  public static String[] fieldNames = {
    "CODE",
    "DESCRIPTION",
    "LEVEL",
    "POSTED",
    "TYPE",
    "ORDER_BY"
  };

  public static int[] fieldTypes = {
    TYPE_STRING,
    TYPE_STRING,
    TYPE_INT,
    TYPE_INT,
    TYPE_INT,
    TYPE_INT
  };

  /**
   * All Parameter
   */
  public static final int ALL_LEVEL = -1;
  public static String[] fieldAllLevel = {
    "Semua", "All"
  };

  public static final int ALL_POSTED = -1;
  public static String[] fieldAllPosted = {
    "Semua", "All"
  };

  public static final int ALL_TYPE = -1;
  public static String[] fieldAllType = {
    "Semua", "All"
  };

  /**
   * Short parameter
   */
  public static final int SHORTBY_CODE = 0;
  public static final int SHORTBY_LEVEL = 1;
  public static final int SHORTBY_POSTED = 2;
  public static final int SHORTBY_TYPE = 3;

  public static String[][] shortFieldNames = {
    {"Kode", "Tingkat", "Status", "Tipe"},
    {"Code", "Level", "Posted", "Type"}
  };

  public static String[] shortFieldKey = {
    "CODE",
    "ACT_LEVEL",
    "POSTED",
    "ACT_TYPE"
  };

  private SrcActivity srcActivity = new SrcActivity();

  /**
   * Creates a new instance of FrmSrcActvity
   */
  public FrmSrcActivity() {
  }

  public FrmSrcActivity(HttpServletRequest request) {
    super(new FrmSrcActivity(), request);
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int getFieldSize() {
    return fieldNames.length;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getFormName() {
    return this.FRM_SRC_ACTIVITY;
  }

  public static Vector getShortOrder(int language) {
    Vector vShortOrder = new Vector(1, 1);
    for (int i = 0; i < shortFieldNames[language].length; i++) {
      vShortOrder.add(shortFieldNames[language][i]);
    }
    return vShortOrder;
  }

  public SrcActivity getEntityObject() {
    return srcActivity;
  }

  public void requestEntityObject(SrcActivity srcActivity) {
    try {
      this.requestParam();

      srcActivity.setCode(this.getString(FRM_CODE));
      srcActivity.setDescription(this.getString(FRM_DESCRIPTION));
      srcActivity.setActLevel(this.getInt(FRM_LEVEL));
      srcActivity.setActType(this.getInt(FRM_TYPE));
      srcActivity.setPosted(this.getInt(FRM_POSTED));
      srcActivity.setOrderBy(this.getInt(FRM_ORDERBY));

      this.srcActivity = srcActivity;
    } catch (Exception e) {
      System.out.println("Exception on request Entity Object FrmSrcActivity ===> " + e.toString());
      srcActivity = new SrcActivity();
    }
  }

  public static void main(String[] arg) {
    FrmSrcActivity frmSrcActivity = new FrmSrcActivity();
    System.out.println(frmSrcActivity.shortFieldNames.length);
    for (int i = 0; i < frmSrcActivity.shortFieldNames.length; i++) {

      System.out.println(frmSrcActivity.shortFieldNames[i][0]);
    }
  }
}
