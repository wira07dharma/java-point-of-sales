/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.Anggota;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa
 */
public class FrmAnggota extends FRMHandler implements I_Language, I_FRMInterface, I_FRMType {

    public static final String FRM_ANGGOTA_NAME = "ANGGOTA";

    public static final int FRM_ID_ANGGOTA = 0;
    public static final int FRM_NO_ANGGOTA = 1;
    public static final int FRM_NAME_ANGGOTA = 2;
    public static final int FRM_SEX = 3;
    public static final int FRM_BIRTH_PLACE = 4;
    public static final int FRM_BIRTH_DATE = 5;
    public static final int FRM_VOCATION_ID = 6;
    public static final int FRM_AGENCIES = 7;
    public static final int FRM_OFFICE_ADDRESS = 8;
    public static final int FRM_ADDR_OFFICE_CITY = 9;
    public static final int FRM_POSITION_ID = 10;
    public static final int FRM_ADDR_PERMANENT = 11;
    public static final int FRM_ADDR_CITY_PERMANENT = 12;
    public static final int FRM_ADDR_PROVINCE_ID = 13;
    public static final int FRM_ADDR_PMNT_REGENCY_ID = 14;
    public static final int FRM_ADDR_PMNT_SUBREGENCY_ID = 15;
    public static final int FRM_WARD_ID = 16;
    public static final int FRM_ID_CARD = 17;
    public static final int FRM_EXPIRED_DATE_KTP = 18;
    public static final int FRM_TLP = 19;
    public static final int FRM_HANDPHONE = 20;
    public static final int FRM_EMAIL = 21;

    //Tambahan Hari Selasa, 26 Pebruari 2013
    public static final int FRM_NO_NPWP = 22;
    public static final int FRM_STATUS = 23;

    public static final int FRM_NO_REKENING = 24;
    public static final int FRM_REGRISTATION_DATE = 25;
    public static final int FRM_NO_KARTU_KELUARGA = 26;
    public static final int FRM_FOTO_ANGGOTA = 27;
    public static final int FRM_ASSIGN_LOCATION_ID = 28;

    public static String[] fieldNames = {
        "ID_ANGGOTA",//0
        "NO_ANGGOTA",//1
        "NAME",//2
        "SEX",//3
        "BIRTH_PLACE",//4
        "BIRTH_DATE",//5
        "VOCATION_ID",//6
        "AGENCIES",//7
        "OFFICE_ADDRESS",//8
        "ADDR_OFFICE_CITY", //update dari type string menjadi ID
        "POSITION_ID",//10
        "ADDR_PERMANENT",//11
        "ADDR_CITY_PERMANENT",//12
        "ADDR_PROVINCE_ID",//13
        "ADDR_PMNT_REGENCY_ID",//14
        "ADDR_PMNT_SUBREGENCY_ID",//15
        "WARD_ID",//16
        "ID_CARD",//17
        "EXPIRED_DATE_KTP",//18
        "TLP",//19
        "HANDPHONE",//20
        "EMAIL",//21
        "NO_NPWP",//22
        "STATUS",//23
        "NO_REKENING",//24
        "REGRISTATION_DATE",//25
        "NO_KARTU_KELUARGA",//26
        "FOTO_ANGGOTA", //27
		"ASSIGN_LOCATION_ID"//28
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG, //update tanggal 26 Pebruari 2013 oleh Hadi
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + FORMAT_EMAIL,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
		TYPE_LONG
    };

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final String[][] sexKey = {{"Laki-Laki", "Perempuan"}, {"Male", "Female"}};
    public static final int[] sexValue = {0, 1};

    private Anggota anggota;

    public FrmAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public FrmAnggota(HttpServletRequest req, Anggota anggota) {
        super(new FrmAnggota(anggota), req);
        String xxxx = req.getParameter(""+FrmAnggota.fieldNames[FrmAnggota.FRM_SEX]);
        System.out.print(""+xxxx);
        this.anggota = anggota;
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

    public Anggota getEntityObject() {
        return anggota;
    }

    public void requestEntityObject(Anggota anggota) {
        try {
            this.requestParam();

            anggota.setNoAnggota(this.getString(FRM_NO_ANGGOTA));
            anggota.setName(this.getString(FRM_NAME_ANGGOTA));

            String xxx = this.getString(FRM_SEX);

            anggota.setSex(this.getInt(FRM_SEX));
            anggota.setBirthPlace(this.getString(FRM_BIRTH_PLACE));
            anggota.setBirthDate(this.getDate(FRM_BIRTH_DATE));
            anggota.setVocationId(this.getLong(FRM_VOCATION_ID));
//            anggota.setAgencies(this.getString(FRM_AGENCIES));
            anggota.setOfficeAddress(this.getString(FRM_OFFICE_ADDRESS));
            anggota.setAddressOfficeCity(this.getLong(FRM_ADDR_OFFICE_CITY)); //update tanggal 26 Pebruari 2013 oleh Hadi
            anggota.setPositionId(this.getLong(FRM_POSITION_ID));
            anggota.setAddressPermanent(this.getString(FRM_ADDR_PERMANENT));
//            anggota.setAddressCityPermanentId(this.getLong(FRM_ADDR_CITY_PERMANENT));
//            anggota.setAddressProvinceId(this.getLong(FRM_ADDR_PROVINCE_ID));
//            anggota.setAddressPermanentRegencyId(this.getLong(FRM_ADDR_PMNT_REGENCY_ID));
//            anggota.setAddressPermanentSubRegencyId(this.getLong(FRM_ADDR_PMNT_SUBREGENCY_ID));
//            anggota.setWardId(this.getLong(FRM_WARD_ID));
            anggota.setIdCard(this.getString(FRM_ID_CARD));
            anggota.setExpiredDateKtp(this.getDate(FRM_EXPIRED_DATE_KTP));
            anggota.setTelepon(this.getString(FRM_TLP));
            anggota.setHandPhone(this.getString(FRM_HANDPHONE));
            anggota.setEmail(this.getString(FRM_EMAIL));

            anggota.setNoNpwp(this.getString(FRM_NO_NPWP));
            anggota.setStatus(this.getInt(FRM_STATUS));
            anggota.setNoKartuKeluarga(this.getString(FRM_NO_KARTU_KELUARGA));
            anggota.setAssignLocation(this.getLong(FRM_ASSIGN_LOCATION_ID));
//            anggota.setFotoAnggota(this.getString(FRM_FOTO_ANGGOTA));
//            anggota.setNoRekening(this.getString(FRM_NO_REKENING));
            //anggota.setTanggalRegistrasi(this.getDate(FRM_REGRISTATION_DATE));

            this.anggota = anggota;
        } catch (Exception e) {
            this.anggota = new Anggota();
        }
    }
}
