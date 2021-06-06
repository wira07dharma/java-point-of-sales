/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.kredit;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import static com.dimata.qdep.form.I_FRMType.ENTRY_REQUIRED;
import static com.dimata.qdep.form.I_FRMType.TYPE_DATE;
import static com.dimata.qdep.form.I_FRMType.TYPE_FLOAT;
import static com.dimata.qdep.form.I_FRMType.TYPE_INT;
import static com.dimata.qdep.form.I_FRMType.TYPE_LONG;
import static com.dimata.qdep.form.I_FRMType.TYPE_STRING;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class FrmPinjaman extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_PINJAMAN = "PINJAMAN";

    public static final int FRM_PINJAMAN_ID = 0;
    public static final int FRM_ANGGOTA_ID = 1;
    public static final int FRM_TIPE_KREDIT_ID = 2;
    public static final int FRM_KELOMPOK_ID = 3;
    public static final int FRM_TGL_PENGAJUAN = 4;
    public static final int FRM_TGL_LUNAS = 5;
    public static final int FRM_JANGKA_WAKTU = 6;
    public static final int FRM_JATUH_TEMPO = 7;
    public static final int FRM_JUMLAH_PINJAMAN = 8;
    public static final int FRM_TIPE_KREDIT_NAME = 9;
    public static final int FRM_STATUS_PINJAMAN = 10;
    public static final int FRM_TGL_REALISASI = 11;
    public static final int FRM_JUMLAH_ANGSURAN = 12;
    public static final int FRM_KODE_KOLEKTIBILITAS = 13;
    public static final int FRM_KETERANGAN = 14;
    public static final int FRM_SUKU_BUNGA = 15;
    public static final int FRM_TIPE_BUNGA = 16;
    public static final int FRM_NO_KREDIT = 17;
    public static final int FRM_ASSIGN_TABUNGAN_ID = 18;
    public static final int FRM_ID_JENIS_SIMPANAN = 19;
    public static final int FRM_JENIS_TRANSAKSI_ID = 20;
    public static final int FRM_TIPE_JADWAL = 21;
	public static final int FRM_CASH_BILL_MAIN_ID = 22;
	public static final int FRM_DOWN_PAYMENT = 23;
	public static final int FRM_SISA_ANGSURAN = 24;
	public static final int FRM_ACCOUNT_OFFICER_ID = 25;
	public static final int FRM_COLLECTOR_ID = 26;

    public static String[] fieldNames
            = {
                "FRM_PINJAMAN_ID",//0
                "FRM_ANGGOTA_ID",//1
                "FRM_TIPE_KREDIT_ID",//2
                "FRM_KELOMPOK_ID",//3
                "FRM_TGL_PENGAJUAN",//4
                "FRM_TGL_LUNAS",//5
                "FRM_JANGKA_WAKTU",//6
                "FRM_JATUH_TEMPO",//7
                "FRM_JUMLAH_PINJAMAN",//8
                "FRM_TIPE_KREDIT_NAME",//9
                "FRM_STATUS_PINJAMAN",//10
                "FRM_TGL_REALISASI",//11
                "FRM_JUMLAH_ANGSURAN",//12
                "FRM_KODE_KOLEKTIBILITAS",//13
                "FRM_KETERANGAN",//14
                "FRM_SUKU_BUNGA",//15
                "FRM_TIPE_BUNGA",//16
                "FRM_NO_KREDIT",//17
                "ASSIGN_TABUNGAN_ID",
                "ID_JENIS_SIMPANAN",
                "JENIS_TRANSAKSI_ID",
                "FRM_TIPE_JADWAL",
				"FRM_CASH_BILL_MAIN_ID",
				"FRM_DOWN_PAYMENT",
				"FRM_SISA_ANGSURAN",
				"FRM_ACCOUNT_OFFICER_ID",
				"FRM_COLLECTOR_ID"
            };

    public static int[] fieldTypes
            = {
                TYPE_LONG,//0
                TYPE_LONG + ENTRY_REQUIRED,//1
                TYPE_LONG + ENTRY_REQUIRED,//2
                TYPE_LONG,//3
                TYPE_STRING,//4
                TYPE_STRING,//5
                TYPE_INT,//6
                TYPE_STRING,//7
                TYPE_FLOAT,//8
                TYPE_STRING,//9
                TYPE_INT,//10
                TYPE_STRING,//11
                TYPE_FLOAT,//12
                TYPE_LONG,//13
                TYPE_STRING,//14
                TYPE_FLOAT,//15
                TYPE_INT,//16
                TYPE_STRING,//17
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
				TYPE_LONG,
				TYPE_FLOAT,
				TYPE_FLOAT,
				TYPE_LONG,
				TYPE_LONG
            };
    
    public static String[] fieldQuestion = {
        "",//0
        "",//1
        "Kolom ini disi jenis kredit yang di pilih",//2
        "",//FRM_KELOMPOK_ID",//3
        "Kolom ini berisi, tanggal pengajuan kredit",//FRM_TGL_PENGAJUAN",//4
        "",//"FRM_TGL_LUNAS",//5
        "",//"FRM_JANGKA_WAKTU",//6
        "",//"FRM_JATUH_TEMPO",//7
        "",//"FRM_JUMLAH_PINJAMAN",//8
        "",//"FRM_TIPE_KREDIT_NAME",//9
        "Pilih status pinjaman, untuk kredit yang baru di ajukan statusnya draft",//"FRM_STATUS_PINJAMAN",//10
        "",//"FRM_TGL_REALISASI",//11
        "",//"FRM_JUMLAH_ANGSURAN",//12
        "",//"FRM_KODE_KOLEKTIBILITAS",//13
        "",//"FRM_KETERANGAN",//14
        "Kolom ini diisi nilai suku bunga. Untuk angka decimal gunakan tanda koma",//"FRM_SUKU_BUNGA",//15
        "",//"FRM_TIPE_BUNGA",//16
        "",//"FRM_NO_KREDIT",//17
        "",//"ASSIGN_TABUNGAN_ID",
        "",//"ID_JENIS_SIMPANAN",
        "",//"JENIS_TRANSAKSI_ID"
    };
    
    public static String[] fieldError = {
        "",
        "Kolom ini harus nama kondisi",
        "Kolom ini harus di isi kode core banking",
        "Kolom ini harus di isi kode ojk"
    };
    
    private Pinjaman pinjaman;

    public FrmPinjaman(Pinjaman pinjaman) {
        this.pinjaman = pinjaman;
    }

    public FrmPinjaman(HttpServletRequest request, Pinjaman pinjaman) {
        super(new FrmPinjaman(pinjaman), request);
        this.pinjaman = pinjaman;
    }

    public String getFormName() {
        return FRM_PINJAMAN;
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

    public Pinjaman getEntityObject() {
        return pinjaman;
    }

    public void requestEntityObject(Pinjaman kredit) {
        try {
            this.requestParam();
            String tglPengajuan = this.getString(FRM_TGL_PENGAJUAN);
            String tglLunas = this.getString(FRM_TGL_LUNAS);
            String tglTempo = this.getString(FRM_JATUH_TEMPO);
            String tglRealisasi = this.getString(FRM_TGL_REALISASI);
            Date pengajuan = Formater.formatDate(tglPengajuan, "yyyy-MM-dd");
            Date lunas = Formater.formatDate(tglLunas, "yyyy-MM-dd");
            Date tempo = Formater.formatDate(tglTempo, "yyyy-MM-dd");
            Date realisasi = Formater.formatDate(tglRealisasi, "yyyy-MM-dd");

            pinjaman.setAnggotaId(this.getLong(FRM_ANGGOTA_ID));
            pinjaman.setTipeKreditId(this.getLong(FRM_TIPE_KREDIT_ID));
            pinjaman.setKelompokId(this.getLong(FRM_KELOMPOK_ID));
            pinjaman.setTglPengajuan(pengajuan);
            pinjaman.setTglLunas(lunas);
            pinjaman.setJangkaWaktu(this.getInt(FRM_JANGKA_WAKTU));
            pinjaman.setJatuhTempo(tempo);
            pinjaman.setJumlahPinjaman(this.getDouble(FRM_JUMLAH_PINJAMAN));
            pinjaman.setStatusPinjaman(this.getInt(FRM_STATUS_PINJAMAN));
            pinjaman.setTglRealisasi(realisasi);
            pinjaman.setJumlahAngsuran(this.getDouble(FRM_JUMLAH_ANGSURAN));
            pinjaman.setKodeKolektibilitas(this.getLong(FRM_KODE_KOLEKTIBILITAS));
            pinjaman.setKeterangan(this.getString(FRM_KETERANGAN));
            pinjaman.setSukuBunga(this.getDouble(FRM_SUKU_BUNGA));
            pinjaman.setTipeBunga(this.getInt(FRM_TIPE_BUNGA));
            pinjaman.setNoKredit(this.getString(FRM_NO_KREDIT));
            pinjaman.setAssignTabunganId(this.getLong(FRM_ASSIGN_TABUNGAN_ID));
            pinjaman.setIdJenisSimpanan(this.getLong(FRM_ID_JENIS_SIMPANAN));
            pinjaman.setIdJenisTransaksi(this.getLong(FRM_JENIS_TRANSAKSI_ID));
            pinjaman.setTipeJadwal(this.getInt(FRM_TIPE_JADWAL));
			pinjaman.setBillMainId(this.getLong(FRM_CASH_BILL_MAIN_ID));
			pinjaman.setDownPayment(this.getDouble(FRM_DOWN_PAYMENT));
			pinjaman.setSisaAngsuran(this.getDouble(FRM_SISA_ANGSURAN));
        } catch (Exception e) {
            pinjaman = new Pinjaman();
        }
    }

}
