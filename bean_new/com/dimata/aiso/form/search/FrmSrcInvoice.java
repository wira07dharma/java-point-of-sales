/*
 * FrmSrcInvoice.java
 *
 * Created on November 20, 2007, 5:56 PM
 */
package com.dimata.aiso.form.search;

/**
 *
 * @author dwi
 */
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

public class FrmSrcInvoice extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_INVOICE = "FRM_INVOICE";

  public static final int FRM_INV_NUMBER = 0;
  public static final int FRM_CONTACT_ID = 1;
  public static final int FRM_START_DATE = 2;
  public static final int FRM_END_DATE = 3;
  public static final int FRM_DATE_TYPE = 4;
  public static final int FRM_INVOICE_TYPE = 5;
  public static final int FRM_INVOICE_STATUS = 6;

  public static String[] fieldNames = {
    "INVOICE_NUMBER", "CONTACT_ID", "START_DATE", "END_DATE", "DATE_TYPE", "INVOICE_TYPE", "INVOICE_STATUS"
  };

  public static int[] fieldTypes = {
    TYPE_STRING, TYPE_LONG, TYPE_DATE, TYPE_DATE, TYPE_INT, TYPE_INT, TYPE_INT
  };

  public static final int ALL_DATE = 0;
  public static final int SELECTED_DATE = 1;

  SrcInvoice srcInvoice;

  /**
   * Creates a new instance of FrmSrcInvoice
   */
  public FrmSrcInvoice() {
  }

  public FrmSrcInvoice(HttpServletRequest request) {
    super(new FrmSrcInvoice(), request);
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
    return FRM_INVOICE;
  }

  public SrcInvoice getEntityObject() {
    return srcInvoice;
  }

  public void requestEntityObject(SrcInvoice srcInvoice) {
    try {
      this.requestParam();
      srcInvoice.setInvoice_number(this.getString(FRM_INV_NUMBER));
      srcInvoice.setContactId(this.getLong(FRM_CONTACT_ID));
      srcInvoice.setStartDate(this.getDate(FRM_START_DATE));
      srcInvoice.setEndDate(this.getDate(FRM_END_DATE));
      srcInvoice.setDateType(this.getInt(FRM_DATE_TYPE));
      srcInvoice.setInvoiceType(this.getInt(FRM_INVOICE_TYPE));
      srcInvoice.setInvoiceStatus(this.getInt(FRM_INVOICE_STATUS));

      this.srcInvoice = srcInvoice;
    } catch (Exception e) {
      srcInvoice = new SrcInvoice();
    }
  }

  public static void main(String[] arg) {
    FrmSrcInvoice frmInvoice = new FrmSrcInvoice();
    frmInvoice.requestParam();
  }
}
