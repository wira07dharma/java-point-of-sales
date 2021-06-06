/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.common.I_Sedana;
import java.util.Date;

public class JadwalAngsuran extends Entity implements I_Sedana {

    public static final int TIPE_ANGSURAN_POKOK = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_POKOK;
    public static final int TIPE_ANGSURAN_BUNGA = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA;
    public static final int TIPE_ANGSURAN_DENDA = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_DENDA;
    public static final int TIPE_ANGSURAN_PENALTI_PELUNASAN_DINI = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_PENALTI_PELUNASAN_DINI;
    public static final int TIPE_ANGSURAN_PENALTI_PELUNASAN_MACET = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_PENALTI_PELUNASAN_MACET;
    public static final int TIPE_ANGSURAN_BUNGA_TAMBAHAN = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA_TAMBAHAN;
    public static final int TIPE_ANGSURAN_DENGAN_BUNGA = I_Sedana.TIPE_DOC_KREDIT_NASABAH_ANGSURAN;
    public static final int TIPE_ANGSURAN_DOWN_PAYMENT = I_Sedana.TIPE_DOC_KREDIT_NASABAH_DOWN_PAYMENT;

    public static final String[] TIPE_ANGSURAN_TITLE = {
        "Angsuran Pokok",
        "Angsuran Bunga",
        "Nilai Denda",
        "Penalti Pelunasan Awal",
        "Penalti Pelunasan Macet",
        "Angsuran Bunga Tambahan",
        "Angsuran",
        "Down Payment"
    };
    
    public static final int[] TIPE_ANGSURAN_VALUE = {
        TIPE_ANGSURAN_POKOK,
        TIPE_ANGSURAN_BUNGA,
        TIPE_ANGSURAN_DENDA,
        TIPE_ANGSURAN_PENALTI_PELUNASAN_DINI,
        TIPE_ANGSURAN_PENALTI_PELUNASAN_MACET,
        TIPE_ANGSURAN_BUNGA_TAMBAHAN,
        TIPE_ANGSURAN_DENGAN_BUNGA,
        TIPE_ANGSURAN_DOWN_PAYMENT
    };

    public static String getTipeAngsuranTitle(int tipe) {
        switch (tipe) {
            case TIPE_ANGSURAN_POKOK:
                return TIPE_ANGSURAN_TITLE[0];
            case TIPE_ANGSURAN_BUNGA:
                return TIPE_ANGSURAN_TITLE[1];
            case TIPE_ANGSURAN_DENDA:
                return TIPE_ANGSURAN_TITLE[2];
            case TIPE_ANGSURAN_PENALTI_PELUNASAN_DINI:
              return TIPE_ANGSURAN_TITLE[3];
            case TIPE_ANGSURAN_PENALTI_PELUNASAN_MACET:
              return TIPE_ANGSURAN_TITLE[4];
            case TIPE_ANGSURAN_BUNGA_TAMBAHAN:
              return TIPE_ANGSURAN_TITLE[5];
            case TIPE_ANGSURAN_DENGAN_BUNGA:
              return TIPE_ANGSURAN_TITLE[6];
            case TIPE_ANGSURAN_DOWN_PAYMENT:
              return TIPE_ANGSURAN_TITLE[7];
            default:
                return "Not defined";
        }
    }
    public static final String[] TIPE_ANGSURAN_RADITYA_TITLE = {
        "Angsuran",
        "Nilai Denda",
        "Penalti Pelunasan Awal",
        "Penalti Pelunasan Macet",
        "Angsuran Bunga Tambahan",
        "Down Payment"
    };

    public static final int[] TIPE_ANGSURAN_RADITYA_VALUE = {

        TIPE_ANGSURAN_DENGAN_BUNGA,
        TIPE_ANGSURAN_DENDA,
        TIPE_ANGSURAN_PENALTI_PELUNASAN_DINI,
        TIPE_ANGSURAN_PENALTI_PELUNASAN_MACET,
        TIPE_ANGSURAN_BUNGA_TAMBAHAN,
        TIPE_ANGSURAN_DOWN_PAYMENT
    };

    public static String getTipeAngsuranRadityaTitle(int tipe) {
        switch (tipe) {
            case TIPE_ANGSURAN_DENGAN_BUNGA:
              return TIPE_ANGSURAN_RADITYA_TITLE[0];
            case TIPE_ANGSURAN_DENDA:
                return TIPE_ANGSURAN_RADITYA_TITLE[1];
            case TIPE_ANGSURAN_PENALTI_PELUNASAN_DINI:
              return TIPE_ANGSURAN_RADITYA_TITLE[2];
            case TIPE_ANGSURAN_PENALTI_PELUNASAN_MACET:
              return TIPE_ANGSURAN_RADITYA_TITLE[3];
            case TIPE_ANGSURAN_BUNGA_TAMBAHAN:
              return TIPE_ANGSURAN_RADITYA_TITLE[4];
            case TIPE_ANGSURAN_DOWN_PAYMENT:
              return TIPE_ANGSURAN_RADITYA_TITLE[5];
            default:
                return "Not defined";
        }
    }

    public static final int STATUS_CETAK_NOT_PRINTED = 0;
    public static final int STATUS_CETAK_PRINTED = 1;
    
    public static final int CETAK_NORMAL = 0;
    public static final int CETAK_DUPLIKAT = 1;
    
    private long pinjamanId = 0;
    private Date tanggalAngsuran = null;
    private int jenisAngsuran = 0;
    private double jumlahANgsuran = 0;
    //update dewok 2017-09-26
    private long transaksiId = 0;
    private long parentJadwalAngsuranId = 0;
    private Date startDate = null;
    private Date endDate = null;
    private double jumlahAngsuranSeharusnya = 0;
    private double sisa = 0;
	private String noKwitansi = "";
        private int statusCetak = 0;

    public long getPinjamanId() {
        return pinjamanId;
    }

    public void setPinjamanId(long pinjamanId) {
        this.pinjamanId = pinjamanId;
    }

    public Date getTanggalAngsuran() {
        return tanggalAngsuran;
    }

    public void setTanggalAngsuran(Date tanggalAngsuran) {
        this.tanggalAngsuran = tanggalAngsuran;
    }

    public int getJenisAngsuran() {
        return jenisAngsuran;
    }

    public void setJenisAngsuran(int jenisAngsuran) {
        this.jenisAngsuran = jenisAngsuran;
    }

    public double getJumlahANgsuran() {
        return jumlahANgsuran;
    }

    public void setJumlahANgsuran(double jumlahANgsuran) {
        this.jumlahANgsuran = jumlahANgsuran;
    }

    public long getTransaksiId() {
        return transaksiId;
    }

    public void setTransaksiId(long transaksiId) {
        this.transaksiId = transaksiId;
    }

    public long getParentJadwalAngsuranId() {
        return parentJadwalAngsuranId;
    }

    public void setParentJadwalAngsuranId(long parentJadwalAngsuranId) {
        this.parentJadwalAngsuranId = parentJadwalAngsuranId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the jumlahAngsuranSeharusnya
     */
    public double getJumlahAngsuranSeharusnya() {
        return jumlahAngsuranSeharusnya;
    }

    /**
     * @param jumlahAngsuranSeharusnya the jumlahAngsuranSeharusnya to set
     */
    public void setJumlahAngsuranSeharusnya(double jumlahAngsuranSeharusnya) {
        this.jumlahAngsuranSeharusnya = jumlahAngsuranSeharusnya;
    }

    /**
     * @return the sisa
     */
    public double getSisa() {
        return sisa;
    }

    /**
     * @param sisa the sisa to set
     */
    public void setSisa(double sisa) {
        this.sisa = sisa;
    }

	/**
	 * @return the noKwitansi
	 */
	public String getNoKwitansi() {
		return noKwitansi;
}

	/**
	 * @param noKwitansi the noKwitansi to set
	 */
	public void setNoKwitansi(String noKwitansi) {
		this.noKwitansi = noKwitansi;
	}

    /**
     * @return the statusCetak
     */
    public int getStatusCetak() {
        return statusCetak;
    }

    /**
     * @param statusCetak the statusCetak to set
     */
    public void setStatusCetak(int statusCetak) {
        this.statusCetak = statusCetak;
    }
	
	

}
