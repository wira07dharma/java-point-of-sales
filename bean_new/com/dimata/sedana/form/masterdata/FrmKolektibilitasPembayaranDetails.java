/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.masterdata.KolektibilitasPembayaranDetails;
import javax.servlet.http.HttpServletRequest;

public class FrmKolektibilitasPembayaranDetails extends FRMHandler implements I_FRMInterface, I_FRMType {

  private KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails;
  public static final String FRM_NAME_KOLEKTIBILITASPEMBAYARANDETAILS = "FRM_NAME_KOLEKTIBILITASPEMBAYARANDETAILS";
  public static final int FRM_FIELD_KOLEKTIBILITASDETAILID = 0;
  public static final int FRM_FIELD_KOLEKTIBILITASID = 1;
  public static final int FRM_FIELD_TIPEKREIDT = 2;
  public static final int FRM_FIELD_MAXHARITUNGGAKANPOKOK = 3;
  public static final int FRM_FIELD_MAXHARIJUMLAHTUNGGAKANBUNGA = 4;

  public static String[] fieldNames = {
    "FRM_FIELD_KOLEKTIBILITASDETAILID",
    "FRM_FIELD_KOLEKTIBILITASID",
    "FRM_FIELD_TIPEKREIDT",
    "FRM_FIELD_MAXHARITUNGGAKANPOKOK",
    "FRM_FIELD_MAXHARIJUMLAHTUNGGAKANBUNGA"
  };

  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_INT,
    TYPE_INT,
    TYPE_INT,
  };

  public FrmKolektibilitasPembayaranDetails() {
  }

  public FrmKolektibilitasPembayaranDetails(KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails) {
    this.entKolektibilitasPembayaranDetails = entKolektibilitasPembayaranDetails;
  }

  public FrmKolektibilitasPembayaranDetails(HttpServletRequest request, KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails) {
    super(new FrmKolektibilitasPembayaranDetails(entKolektibilitasPembayaranDetails), request);
    this.entKolektibilitasPembayaranDetails = entKolektibilitasPembayaranDetails;
  }

  public String getFormName() {
    return FRM_NAME_KOLEKTIBILITASPEMBAYARANDETAILS;
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

  public KolektibilitasPembayaranDetails getEntityObject() {
    return entKolektibilitasPembayaranDetails;
  }

  public void requestEntityObject(KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails) {
    try {
      this.requestParam();
      entKolektibilitasPembayaranDetails.setKolektibilitasId(getLong(FRM_FIELD_KOLEKTIBILITASID));
      entKolektibilitasPembayaranDetails.setTipeKreidt(getInt(FRM_FIELD_TIPEKREIDT));
      entKolektibilitasPembayaranDetails.setMaxHariTunggakanPokok(getInt(FRM_FIELD_MAXHARITUNGGAKANPOKOK));
      entKolektibilitasPembayaranDetails.setMaxHariJumlahTunggakanBunga(getInt(FRM_FIELD_MAXHARIJUMLAHTUNGGAKANBUNGA));
    } catch (Exception e) {
      System.out.println("Error on requestEntityObject : " + e.toString());
    }
  }

}
