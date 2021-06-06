/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.RatePasarBerlian;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Regen
 */
public class FrmRatePasarBerlian extends FRMHandler implements I_FRMInterface, I_FRMType {
  private RatePasarBerlian entRatePasarBerlian;
  public static final String FRM_NAME_RATEPASARBERLIAN = "FRM_NAME_RATEPASARBERLIAN";
  public static final int FRM_FIELD_RATEPASARID = 0;
  public static final int FRM_FIELD_CODE = 1;
  public static final int FRM_FIELD_NAME = 2;
  public static final int FRM_FIELD_RATE = 3;
  public static final int FRM_FIELD_DESCRIPTION = 4;
  public static final int FRM_FIELD_UPDATEDATE = 5;


public static String[] fieldNames = {
    "FRM_FIELD_RATEPASARID",
    "FRM_FIELD_CODE",
    "FRM_FIELD_NAME",
    "FRM_FIELD_RATE",
    "FRM_FIELD_DESCRIPTION",
    "FRM_FIELD_UPDATEDATE"
};

public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_STRING,
    TYPE_DATE
};

public FrmRatePasarBerlian() {
}

public FrmRatePasarBerlian(RatePasarBerlian entRatePasarBerlian) {
this.entRatePasarBerlian = entRatePasarBerlian;
}

public FrmRatePasarBerlian(HttpServletRequest request, RatePasarBerlian entRatePasarBerlian) {
super(new FrmRatePasarBerlian(entRatePasarBerlian), request);
this.entRatePasarBerlian = entRatePasarBerlian;
}

public String getFormName() {
return FRM_NAME_RATEPASARBERLIAN;
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

public RatePasarBerlian getEntityObject() {
return entRatePasarBerlian;
}

public void requestEntityObject(RatePasarBerlian entRatePasarBerlian) {
try {
this.requestParam();
    entRatePasarBerlian.setOID(getLong(FRM_FIELD_RATEPASARID));
    entRatePasarBerlian.setCode(getString(FRM_FIELD_CODE));
    entRatePasarBerlian.setName(getString(FRM_FIELD_NAME));
    entRatePasarBerlian.setRate(getFloat(FRM_FIELD_RATE));
    entRatePasarBerlian.setDescription(getString(FRM_FIELD_DESCRIPTION));
    entRatePasarBerlian.setUpdateDate(getDate(FRM_FIELD_UPDATEDATE));
} catch (Exception e) {
System.out.println("Error on requestEntityObject : " + e.toString());
}
}

}