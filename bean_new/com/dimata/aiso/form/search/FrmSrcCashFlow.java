/*
 * FrmSrcCashFlow.java
 *
 * Created on December 17, 2007, 2:37 PM
 */
package com.dimata.aiso.form.search;

/**
 *
 * @author dwi
 */

/* package javax */
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

public class FrmSrcCashFlow extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_CASHFLOW = "FRM_CASHFLOW";

  public static final int FRM_START_DATE = 0;
  public static final int FRM_END_DATE = 1;

  public static String[] fieldNames = {
    "START_DATE", "END_DATE"
  };

  public static int[] fieldTypes = {
    TYPE_DATE, TYPE_DATE
  };

  SrcCashFlow srcCashFlow;

  /**
   * Creates a new instance of FrmSrcInvoice
   */
  public FrmSrcCashFlow() {
  }

  public FrmSrcCashFlow(HttpServletRequest request) {
    super(new FrmSrcCashFlow(), request);
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
    return FRM_CASHFLOW;
  }

  public SrcCashFlow getEntityObject() {
    return srcCashFlow;
  }

  public void requestEntityObject(SrcCashFlow srcCashFlow) {
    try {
      this.requestParam();
      srcCashFlow.setDateFrom(this.getDate(FRM_START_DATE));
      srcCashFlow.setDateTo(this.getDate(FRM_END_DATE));

      this.srcCashFlow = srcCashFlow;
    } catch (Exception e) {
      srcCashFlow = new SrcCashFlow();
    }
  }

  public static void main(String[] args) {
    FrmSrcCashFlow frmSrcCashFlow = new FrmSrcCashFlow();
    int iLength = frmSrcCashFlow.getFieldSize();
    frmSrcCashFlow.requestParam();
  }
}
