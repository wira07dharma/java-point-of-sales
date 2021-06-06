/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class FrmJenisKredit extends FRMHandler implements I_FRMInterface, I_FRMType {
    public static final String FRM_KREDIT = "KREDIT";
    
    public static final int FRM_TYPE_KREDIT_ID = 0;
    public static final int FRM_NAME_KREDIT = 1;//Update
    public static final int FRM_MIN_KREDIT=2;
    public static final int FRM_MAX_KREDIT=3;
    public static final int FRM_BUNGA_MIN=4;
    public static final int FRM_BUNGA_MAX=5;
    public static final int FRM_BIAYA_ADMIN=6;
    public static final int FRM_PROVISI=7;
    public static final int FRM_DENDA=8;
    public static final int FRM_JANGKA_WAKTU_MIN=9;
    public static final int FRM_JANGKA_WAKTU_MAX=10;
    public static final int FRM_KEGUNAAN=11;
    public static final int FRM_TIPE_BUNGA = 12;
    public static final int FRM_BERLAKU_MULAI = 13;
    public static final int FRM_BERLAKU_SAMPAI = 14;
    //update dewok 2017-10-02
    public static final int FRM_TIPE_DENDA_BERLAKU = 15;
    public static final int FRM_TIPE_PERHITUNGAN_DENDA = 16;
    public static final int FRM_FREKUENSI_DENDA = 17;
    public static final int FRM_SATUAN_FREKUANSI_DENDA = 18;
    //regen 2018-01-23
    public static final int FRM_TIPE_FREKUENSI = 19;
    public static final int FRM_TIPE_MUSIM = 20;
    public static final int FRM_FREKUENSI_N = 21;
    public static final int FRM_FREKUENSI_HARI = 22;
    public static final int FRM_PERSENTASE_WAJIB = 23;
    public static final int FRM_DENDA_TOLERANSI = 24;        
    public static final int FRM_NOMINAL_WAJIB = 25;
    //added by dewok 2018-05-22
    public static final int FRM_VARIABEL_DENDA = 26;
    public static final int FRM_TIPE_VARIABEL_DENDA = 27;
    public static final int FRM_TIPE_FREKUENSI_DENDA = 28;
    
    private HttpServletRequest request = null;

    public static String[] fieldNames =
            {   
                "FRM_TYPE_KREDIT_ID",//0
                "FRM_NAME_KREDIT",//1Update
                "FRM_MIN_KREDIT",//2
                "FRM_MAX_KREDIT",//3
                "FRM_BUNGA_MIN",//4
                "FRM_BUNGA_MAX",//5
                "FRM_BIAYA_ADMIN",//6
                "FRM_PROVISI",//7
                "FRM_DENDA",//8
                "FRM_JANGKA_WAKTU_MIN",//9
                "FRM_JANGKA_WAKTU_MAX",//10
                "FRM_KEGUNAAN",//11
                "FRM_TIPE_BUNGA",//12
                "FRM_BERLAKU_MULAI",//13
                "FRM_BERLAKU_SAMPAI",//14
                "FRM_TIPE_DENDA_BERLAKU",//15
                "FRM_TIPE_PERHITUNGAN_DENDA",//16
                "FRM_FREKUENSI_DENDA",//17
                "FRM_SATUAN_FREKUANSI_DENDA",//18
                "TIPE_FREKUENSI",//19
                "TIPE_MUSIM",//20
                "FREKUENSI_N",//21
                "FREKUENSI_HARI",//22
                "denda",//23
                "TOLERANSI_DENDA",//24
                "NOMINAL_WAJIB",//25
                "FRM_VARIABEL_DENDA",//26
                "FRM_TIPE_VARIABEL_DENDA",
                "FRM_TIPE_FREKUENSI_DENDA"
            };
    
    public static int[] fieldTypes =
            {   
                TYPE_LONG,//0
                TYPE_STRING,//1Update
                TYPE_FLOAT,//2
                TYPE_FLOAT,//3
                TYPE_FLOAT,//4
                TYPE_FLOAT,//5
                TYPE_FLOAT,//6
                TYPE_FLOAT,//7
                TYPE_FLOAT,//8
                TYPE_FLOAT,//9
                TYPE_FLOAT,//10
                TYPE_STRING,//11
                TYPE_INT,//12
                TYPE_STRING,//13
                TYPE_STRING,//14
                TYPE_INT,//15
                TYPE_INT,//16
                TYPE_INT,//17
                TYPE_INT,//18
                TYPE_INT,//19
                TYPE_INT,//20
                TYPE_INT,//21
                TYPE_FLOAT,//22
                TYPE_FLOAT,//23
                TYPE_INT,//24
                TYPE_FLOAT,//25
                TYPE_INT,//26
                TYPE_INT,//27
                TYPE_INT,//28
            };
    
    private JenisKredit jenisKredit;

    public FrmJenisKredit(JenisKredit jenisKredit) {
        this.jenisKredit = jenisKredit;
    }

    public FrmJenisKredit(HttpServletRequest request, JenisKredit jenisKredit) {
        super(new FrmJenisKredit(jenisKredit), request);
        this.jenisKredit = jenisKredit;
        this.request = request;
    }

    public String getFormName() {
        return FRM_KREDIT;
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

    public JenisKredit getEntityObject() {
        return jenisKredit;
    }
    
    public void requestEntityObject(JenisKredit jenisKredit) {        
        try {
            this.requestParam();
            String mulai = this.getString(FRM_BERLAKU_MULAI);
            String sampai = this.getString(FRM_BERLAKU_SAMPAI);
            Date dateMulai = Formater.formatDate(mulai, "yyyy-MM-dd");
            Date dateSampai = Formater.formatDate(sampai, "yyyy-MM-dd");
            
            jenisKredit.setNamaKredit(this.getString(FRM_NAME_KREDIT));//Update
            jenisKredit.setMinKredit(this.getDouble(FRM_MIN_KREDIT));
            jenisKredit.setMaxKredit(this.getDouble(FRM_MAX_KREDIT));
            jenisKredit.setBungaMin(this.getDouble(FRM_BUNGA_MIN));
            jenisKredit.setBungaMax(this.getDouble(FRM_BUNGA_MAX));
            jenisKredit.setBiayaAdmin(this.getDouble(FRM_BIAYA_ADMIN));
            jenisKredit.setProvisi(this.getDouble(FRM_PROVISI));
            jenisKredit.setDenda(this.getDouble(FRM_DENDA));
            jenisKredit.setJangkaWaktuMin(this.getFloat(FRM_JANGKA_WAKTU_MIN));
            jenisKredit.setJangkaWaktuMax(this.getFloat(FRM_JANGKA_WAKTU_MAX));
            jenisKredit.setKegunaan(this.getString(FRM_KEGUNAAN));
            jenisKredit.setBerlakuMulai(dateMulai);
            jenisKredit.setBerlakuSampai(dateSampai);
            jenisKredit.setTipeBunga(getInt(FRM_TIPE_BUNGA));
            //update
            jenisKredit.setTipeDendaBerlaku(getInt(FRM_TIPE_DENDA_BERLAKU));
            jenisKredit.setTipePerhitunganDenda(getInt(FRM_TIPE_PERHITUNGAN_DENDA));
            jenisKredit.setFrekuensiDenda(getInt(FRM_FREKUENSI_DENDA));
            jenisKredit.setSatuanFrekuensiDenda(getInt(FRM_SATUAN_FREKUANSI_DENDA));
            //regen
            jenisKredit.setTipeFrekuensiPokokLegacy(getInt(FRM_TIPE_FREKUENSI));
            jenisKredit.setWeekValues(request.getParameterValues(fieldNames[FRM_FREKUENSI_HARI]));
            jenisKredit.setFrekuensiPokok(getInt(FRM_FREKUENSI_N));
            jenisKredit.setPersentaseWajib(getDouble(FRM_PERSENTASE_WAJIB));
            jenisKredit.setNominalWajib(getDouble(FRM_NOMINAL_WAJIB));
            jenisKredit.setTipeFrekuensiPokok();
            jenisKredit.setDendaToleransi(getInt(FRM_DENDA_TOLERANSI));
            jenisKredit.setVariabelDenda(getInt(FRM_VARIABEL_DENDA));
            jenisKredit.setTipeVariabelDenda(getInt(FRM_TIPE_VARIABEL_DENDA));
            jenisKredit.setTipeFrekuensiDenda(getInt(FRM_TIPE_FREKUENSI_DENDA));
            this.jenisKredit = jenisKredit;
        } catch (Exception e) {
            jenisKredit = new JenisKredit();
        }
    }
    
}
