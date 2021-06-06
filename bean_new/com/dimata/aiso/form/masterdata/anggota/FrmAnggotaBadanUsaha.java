/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.sedana.entity.anggota.AnggotaBadanUsaha;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmAnggotaBadanUsaha extends FRMHandler implements I_Language, I_FRMInterface, I_FRMType {

    public static final String FRM_ANGGOTA_NAME = "ANGGOTA_BADAN_USAHA";

    public static final int FRM_ID_ANGGOTA = 0;
    public static final int FRM_NO_ANGGOTA = 1;
    public static final int FRM_NAME_ANGGOTA = 2;
    public static final int FRM_SEX = 3;
    public static final int FRM_TLP = 4;
    public static final int FRM_EMAIL = 5;
    public static final int FRM_OFFICE_ADDRESS = 6;
    public static final int FRM_ADDR_OFFICE_CITY = 7;
    public static final int FRM_ID_CARD = 8;
    public static final int FRM_NO_NPWP = 9;
    public static final int FRM_JENIS_TRANSAKSI_ID = 10;

    public static String[] fieldNames = {
        "ID_ANGGOTA",//0
        "NO_ANGGOTA",//1
        "NAME",//2
        "SEX",//3
        "TLP",//4
        "EMAIL",//5
        "OFFICE_ADDRESS",//6
        "ADDR_OFFICE_CITY", //7
        "ID_CARD",//8
        "NO_NPWP",//9
        "JENIS_TRANSAKSI_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,//0
        TYPE_STRING,//1
        TYPE_STRING,//2
        TYPE_INT,//3
        TYPE_STRING,//4        
        TYPE_STRING,//5
        TYPE_STRING,//6
        TYPE_LONG, //7
        TYPE_STRING,//8
        TYPE_STRING,//9
        TYPE_LONG
    };

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final String[][] sexKey = {{"Laki-Laki", "Perempuan"}, {"Male", "Female"}};
    public static final int[] sexValue = {0, 1};

    private AnggotaBadanUsaha anggotaBadanUsaha;

    public FrmAnggotaBadanUsaha(AnggotaBadanUsaha anggotaBadanUsaha) {
        this.anggotaBadanUsaha = anggotaBadanUsaha;
    }

    public FrmAnggotaBadanUsaha(HttpServletRequest req, AnggotaBadanUsaha anggotaBadanUsaha) {
        super(new FrmAnggotaBadanUsaha(anggotaBadanUsaha), req);
        this.anggotaBadanUsaha = anggotaBadanUsaha;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_ANGGOTA_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public AnggotaBadanUsaha getEntityObject() {
        return anggotaBadanUsaha;
    }

    public void requestEntityObject(AnggotaBadanUsaha anggotaBadanUsaha) {
        try {
            this.requestParam();
            anggotaBadanUsaha.setNoAnggota(this.getString(FRM_NO_ANGGOTA));
            anggotaBadanUsaha.setName(this.getString(FRM_NAME_ANGGOTA));
            //anggotaBadanUsaha.setSex(-1);
            anggotaBadanUsaha.setTelepon(this.getString(FRM_TLP));
            anggotaBadanUsaha.setEmail(this.getString(FRM_EMAIL));
            anggotaBadanUsaha.setOfficeAddress(this.getString(FRM_OFFICE_ADDRESS));
            anggotaBadanUsaha.setAddressOfficeCity(this.getLong(FRM_ADDR_OFFICE_CITY));
            anggotaBadanUsaha.setIdCard(this.getString(FRM_ID_CARD));
            anggotaBadanUsaha.setNoNpwp(this.getString(FRM_NO_NPWP));
            anggotaBadanUsaha.setIdJenisTransaksi(this.getLong(FRM_JENIS_TRANSAKSI_ID));
            
            this.anggotaBadanUsaha = anggotaBadanUsaha;
        } catch (Exception e) {
            this.anggotaBadanUsaha = new AnggotaBadanUsaha();
        }
    }
}
