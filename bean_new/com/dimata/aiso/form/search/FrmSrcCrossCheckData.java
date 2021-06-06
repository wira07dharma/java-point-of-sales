/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.search;

import javax.servlet.*;
import javax.servlet.http.*;

/* package java */
import java.util.*;

/* package dimata */
import com.dimata.util.*;

/* import qdep */
import com.dimata.qdep.form.*;

/* import aiso */
import com.dimata.aiso.entity.search.*;

/**
 *
 * @author dwi
 */
public class FrmSrcCrossCheckData extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_SRC_CRS_DATA = "FRM_SRC_CRS_DATA";

  public static final int FRM_TYPE_DATA = 0;
  public static final int FRM_TYPE_TRANS = 1;
  public static final int FRM_TYPE_CRS_CHECK = 2;

  public static String[] fieldNames = {
    "TYPE_DATA",
    "TYPE_TRANSACTION",
    "TYPE_CROSS_CHECK"
  };

  public static int[] fieldTypes = {
    TYPE_INT,
    TYPE_INT,
    TYPE_INT
  };

  SrcCrossCheckData objSrcCrossCheckData;

  public FrmSrcCrossCheckData() {

  }

  public FrmSrcCrossCheckData(HttpServletRequest request) {
    super(new FrmSrcCrossCheckData(), request);
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
    return FRM_SRC_CRS_DATA;
  }

  public SrcCrossCheckData getEntityObject() {
    return objSrcCrossCheckData;
  }

  public void requestEntityObject(SrcCrossCheckData objSrcCrossCheckData) {
    try {
      this.requestParam();
      objSrcCrossCheckData.setTypeArap(this.getInt(FRM_TYPE_DATA));
      objSrcCrossCheckData.setTypeTrans(this.getInt(FRM_TYPE_TRANS));
      objSrcCrossCheckData.setTypeCrossCheck(this.getInt(FRM_TYPE_CRS_CHECK));

      this.objSrcCrossCheckData = objSrcCrossCheckData;
    } catch (Exception e) {
      objSrcCrossCheckData = new SrcCrossCheckData();
    }
  }

}
