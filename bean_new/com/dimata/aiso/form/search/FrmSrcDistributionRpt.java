/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.search;

import com.dimata.aiso.entity.search.SrcDistributionRpt;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmSrcDistributionRpt extends FRMHandler implements I_FRMInterface, I_FRMType {

  public static final String FRM_SRC_DISTRIBUTION_RPT = "FRM_SRC_DISTRIBUTION_RPT";

  public static final int FRM_BISNIS_CENTER_ID = 0;
  public static final int FRM_PERIOD_ID = 0;
  public static final int FRM_CURRENCY_ID = 0;

  public static String fieldNames[] = {
    "FRM_BISNIS_CENTER_ID",
    "FRM_PERIOD_ID",
    "FRM_CURRENCY_ID"
  };

  public static int fieldTypes[] = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG
  };

  private SrcDistributionRpt srcDistributionRpt;

  public FrmSrcDistributionRpt(SrcDistributionRpt srcDistributionRpt) {
    this.srcDistributionRpt = srcDistributionRpt;
  }

  public FrmSrcDistributionRpt(HttpServletRequest request, SrcDistributionRpt srcDistributionRpt) {
    super(new FrmSrcDistributionRpt(srcDistributionRpt), request);
    this.srcDistributionRpt = srcDistributionRpt;
  }

  public String getFormName() {
    return FRM_SRC_DISTRIBUTION_RPT;
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

  public SrcDistributionRpt getEntityObject() {
    return srcDistributionRpt;
  }

  public void requestEntityObject(SrcDistributionRpt srcDistributionRpt) {
    try {
      this.requestParam();
      srcDistributionRpt.setBinisCenterId(this.getLong(FRM_BISNIS_CENTER_ID));
      srcDistributionRpt.setPeriodId(this.getLong(FRM_PERIOD_ID));
      srcDistributionRpt.setCurrencyId(this.getLong(FRM_CURRENCY_ID));

      this.srcDistributionRpt = srcDistributionRpt;
    } catch (Exception e) {
      srcDistributionRpt = new SrcDistributionRpt();
    }
  }
}
