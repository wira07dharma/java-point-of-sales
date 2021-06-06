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
import com.dimata.aiso.entity.masterdata.anggota.Anggota;
import com.dimata.aiso.entity.masterdata.anggota.PstAnggota;
import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.entity.masterdata.KolektibilitasPembayaran;
import com.dimata.sedana.entity.masterdata.PstKolektibilitasPembayaran;
import com.dimata.sedana.entity.sumberdana.PstSumberDana;
import com.dimata.sedana.entity.sumberdana.SumberDana;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;

public class Pinjaman extends Entity {

    /**
     * @return the categoryPinjaman
     */
    public long getCategoryPinjaman() {
        return categoryPinjaman;
    }

    /**
     * @param categoryPinjaman the categoryPinjaman to set
     */
    public void setCategoryPinjaman(long categoryPinjaman) {
        this.categoryPinjaman = categoryPinjaman;
    }

    /**
     * @return the jumlahBunga
     */
    public double getJumlahBunga() {
        return jumlahBunga;
    }

    /**
     * @param jumlahBunga the jumlahBunga to set
     */
    public void setJumlahBunga(double jumlahBunga) {
        this.jumlahBunga = jumlahBunga;
    }

    /**
     * @return the jangkaWaktuId
     */
    public long getJangkaWaktuId() {
        return jangkaWaktuId;
    }

    /**
     * @param jangkaWaktuId the jangkaWaktuId to set
     */
    public void setJangkaWaktuId(long jangkaWaktuId) {
        this.jangkaWaktuId = jangkaWaktuId;
    }

    public static final int STATUS_DOC_DRAFT = 0;
    public static final int STATUS_DOC_TO_BE_APPROVE = 1;
    public static final int STATUS_DOC_APPROVED = 2;
    public static final int STATUS_DOC_TIDAK_DISETUJUI = 3;
    public static final int STATUS_DOC_BATAL = 4;
    public static final int STATUS_DOC_CAIR = 5;
    public static final int STATUS_DOC_PENANGANAN_MACET = 6;
    public static final int STATUS_DOC_LUNAS = 7;
    public static final int STATUS_DOC_PELUNASAN_DINI = 8;
    public static final int STATUS_DOC_PELUNASAN_MACET = 9;
    public static final int STATUS_DOC_DIKIRIM = 10;
    public static final int STATUS_DOC_DITERIMA = 11;

    public static final String[] STATUS_DOC_TITLE = {
        "Draft",//0
        "Analisa",//1
        "Accept",//2
        "Tidak Accept",//3
        "Batal",//4
        "Done",//5
        "Penanganan Macet",//6
        "Lunas",//7
        "Lunas Awal",//8
        "Lunas Macet",//9
        "Dikirim",//10
        "Diterima"//11
    };

    public static String getStatusDocTitle(int code) {
        switch (code) {
            case STATUS_DOC_DRAFT:
                return STATUS_DOC_TITLE[0];
            case STATUS_DOC_TO_BE_APPROVE:
                return STATUS_DOC_TITLE[1];
            case STATUS_DOC_APPROVED:
                return STATUS_DOC_TITLE[2];
            case STATUS_DOC_TIDAK_DISETUJUI:
                return STATUS_DOC_TITLE[3];
            case STATUS_DOC_BATAL:
                return STATUS_DOC_TITLE[4];
            case STATUS_DOC_CAIR:
                return STATUS_DOC_TITLE[5];
            case STATUS_DOC_PENANGANAN_MACET:
                return STATUS_DOC_TITLE[6];
            case STATUS_DOC_LUNAS:
                return STATUS_DOC_TITLE[7];
            case STATUS_DOC_PELUNASAN_DINI:
                return STATUS_DOC_TITLE[8];
            case STATUS_DOC_PELUNASAN_MACET:
                return STATUS_DOC_TITLE[9];
            default:
                return "Not defined";
        }
    }

    public static final int TIPE_BUNGA_FLAT = 0;
    public static final int TIPE_BUNGA_MENURUN = 1;
    public static final int TIPE_BUNGA_ANUITAS = 2;

    public static final String[] TIPE_BUNGA_TITLE = {"Menetap", "Menurun", "Anuitas"};
    public static final String[] TIPE_JADWAL_TITLE = {"Berdasarkan Jangka Waktu", "Berdasarkan Pembayaran"};

    public static final int LOKASI_PENAGIHAN_RUMAH = 0;
    public static final int LOKASI_PENAGIHAN_KANTOR = 1;
    public static final String[] LOKASI_PENAGIHAN = {"Rumah", "Tempat Bekerja"};

    public static final int STATUS_DENDA_AKTIF = 0;
    public static final int STATUS_DENDA_NONAKTIF = 1;
    public static final String[] STATUS_DENDA_TITLE = {"Aktif", "Tidak Aktif"};

    private long anggotaId = 0;
    private long tipeKreditId = 0;
    private long kelompokId = 0;
    private Date tglPengajuan = null;
    private Date tglLunas = null;
    private int jangkaWaktu = 0;
    private Date jatuhTempo = null;
    private double jumlahPinjaman = 0;
    private int statusPinjaman = 0;
    private Date tglRealisasi = null;
    private double jumlahAngsuran = 0;
    private long kodeKolektibilitas = 0;
    private String keterangan = "";
    private double sukuBunga = 0;
    private int tipeBunga = 0;
    private String noKredit = "";
    private long assignTabunganId = 0;
    private long idJenisSimpanan = 0;
    private long idJenisTransaksi = 0;
    private long wajibIdJenisSimpanan = 0;
    private double wajibValue = 0D;
    private int wajibValueType = 0;
    private int tipeJadwal = 0;
    private long billMainId = 0;
    private double downPayment = 0;
    private double sisaAngsuran = 0;
    private long jangkaWaktuId = 0;

    private long accountOfficerId = 0;
    private long collectorId = 0;
    private int lokasiPenagihan = 0;
    private double jumlahBunga = 0;
    private long categoryPinjaman = 0;
    private int statusDenda = 0;

    public static final int TIPE_JADWAL_BY_PERIOD = 0;
    public static final int TIPE_JADWAL_ON_PAID = 1;

    public static final int WAJIB_VALUE_TYPE_NOMINAL = 0;
    public static final int WAJIB_VALUE_TYPE_PERSEN = 1;

    public long getIdJenisTransaksi() {
        return idJenisTransaksi;
    }

    public void setIdJenisTransaksi(long idJenisTransaksi) {
        this.idJenisTransaksi = idJenisTransaksi;
    }

    //tambahan untuk join
    private long sumberDanaId = 0;

    public long getSumberDanaId() {
        return sumberDanaId;
    }

    public void setSumberDanaId(long sumberDanaId) {
        this.sumberDanaId = sumberDanaId;
    }

    public long getAssignTabunganId() {
        return assignTabunganId;
    }

    public void setAssignTabunganId(long assignTabunganId) {
        this.assignTabunganId = assignTabunganId;
    }

    public long getIdJenisSimpanan() {
        return idJenisSimpanan;
    }

    public void setIdJenisSimpanan(long idJenisSimpanan) {
        this.idJenisSimpanan = idJenisSimpanan;
    }

    public double getJumlahAngsuran() {
        return jumlahAngsuran;
    }

    public void setJumlahAngsuran(double jumlahAngsuran) {
        this.jumlahAngsuran = jumlahAngsuran;
    }

    public long getKodeKolektibilitas() {
        return kodeKolektibilitas;
    }

    public void setKodeKolektibilitas(long kodeKolektibilitas) {
        this.kodeKolektibilitas = kodeKolektibilitas;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getSukuBunga() {
        return sukuBunga;
    }

    public void setSukuBunga(double sukuBunga) {
        this.sukuBunga = sukuBunga;
    }

    public int getTipeBunga() {
        return tipeBunga;
    }

    public void setTipeBunga(int tipeBunga) {
        this.tipeBunga = tipeBunga;
    }

    public String getNoKredit() {
        return noKredit;
    }

    public void setNoKredit(String noKredit) {
        this.noKredit = noKredit;
    }

    public long getAnggotaId() {
        return anggotaId;
    }

    public void setAnggotaId(long anggotaId) {
        this.anggotaId = anggotaId;
    }

    public long getTipeKreditId() {
        return tipeKreditId;
    }

    public void setTipeKreditId(long tipeKreditId) {
        this.tipeKreditId = tipeKreditId;
    }

    public long getKelompokId() {
        return kelompokId;
    }

    public void setKelompokId(long kelompokId) {
        this.kelompokId = kelompokId;
    }

    public Date getTglPengajuan() {
        return tglPengajuan;
    }

    public void setTglPengajuan(Date tglPengajuan) {
        this.tglPengajuan = tglPengajuan;
    }

    public Date getTglLunas() {
        return tglLunas;
    }

    public void setTglLunas(Date tglLunas) {
        this.tglLunas = tglLunas;
    }

    public int getJangkaWaktu() {
        return jangkaWaktu;
    }

    public int getJangkaWaktuCalc() {
        return tipeJadwal == TIPE_JADWAL_BY_PERIOD ? jangkaWaktu : 1;
    }

    public void setJangkaWaktu(int jangkaWaktu) {
        this.jangkaWaktu = jangkaWaktu;
    }

    public Date getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(Date jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public double getJumlahPinjaman() {
        return jumlahPinjaman;
    }

    public void setJumlahPinjaman(double jumlahPinjaman) {
        this.jumlahPinjaman = jumlahPinjaman;
    }

    public int getStatusPinjaman() {
        return statusPinjaman;
    }

    public void setStatusPinjaman(int statusPinjaman) {
        this.statusPinjaman = statusPinjaman;
    }

    public Date getTglRealisasi() {
        return tglRealisasi;
    }

    public void setTglRealisasi(Date tglRealisasi) {
        this.tglRealisasi = tglRealisasi;
    }

    /**
     * @return the wajibIdJenisSimpanan
     */
    public long getWajibIdJenisSimpanan() {
        return wajibIdJenisSimpanan;
    }

    /**
     * @param wajibIdJenisSimpanan the wajibIdJenisSimpanan to set
     */
    public void setWajibIdJenisSimpanan(long wajibIdJenisSimpanan) {
        this.wajibIdJenisSimpanan = wajibIdJenisSimpanan;
    }

    /**
     * @return the wajibValue
     */
    public double getWajibValue() {
        return wajibValue;
    }

    /**
     * @param wajibValue the wajibValue to set
     */
    public void setWajibValue(double wajibValue) {
        this.wajibValue = wajibValue;
    }

    /**
     * @return the wajibValueType
     */
    public int getWajibValueType() {
        return wajibValueType;
    }

    /**
     * @param wajibValueType the wajibValueType to set
     */
    public void setWajibValueType(int wajibValueType) {
        this.wajibValueType = wajibValueType;
    }

    public String getLogDetail(Entity prevDoc, Entity newDoc) {
        String history = "";
        Pinjaman prevPinjaman = (Pinjaman) prevDoc;
        Pinjaman newPinjaman = (Pinjaman) newDoc;

        Anggota prevAnggota = new Anggota();
        Anggota newAnggota = new Anggota();
        JenisKredit prevTypeKredit = new JenisKredit();
        JenisKredit newTypeKredit = new JenisKredit();
        SumberDana prevSumberDana = new SumberDana();
        SumberDana newSumberDana = new SumberDana();
        KolektibilitasPembayaran prevKolektibilitas = new KolektibilitasPembayaran();
        KolektibilitasPembayaran newKolektibilitas = new KolektibilitasPembayaran();

        Vector listSumberDana = PstPinjamanSumberDana.list(0, 0, "" + PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_PINJAMAN_ID] + " = '" + newPinjaman.getOID() + "'", "");
        PinjamanSumberDana dana = new PinjamanSumberDana();
        long sumberId = 0;
        if (!listSumberDana.isEmpty()) {
            dana = (PinjamanSumberDana) listSumberDana.get(0);
            sumberId = dana.getSumberDanaId();
        }

        try {
            if (prevPinjaman != null) {
                prevAnggota = PstAnggota.fetchExc(prevPinjaman.getAnggotaId());
                prevTypeKredit = PstTypeKredit.fetchExc(prevPinjaman.getTipeKreditId());
                prevSumberDana = PstSumberDana.fetchExc(sumberId);
                prevKolektibilitas = PstKolektibilitasPembayaran.fetchExc(prevPinjaman.getKodeKolektibilitas());
            }
            newAnggota = PstAnggota.fetchExc(newPinjaman.getAnggotaId());
            newTypeKredit = PstTypeKredit.fetchExc(newPinjaman.getTipeKreditId());
            newSumberDana = PstSumberDana.fetchExc(sumberId);
            newKolektibilitas = PstKolektibilitasPembayaran.fetchExc(newPinjaman.getKodeKolektibilitas());
        } catch (Exception exc) {
            System.out.println(exc);
        }

        //COMPARE
        if (prevPinjaman == null) {
//            history += "<p><b>INSERT : </b>";
            history += "Status dokumen: " + this.getStatusDocTitle(newPinjaman.getStatusPinjaman());
            history += "; Jenis kredit: " + newTypeKredit.getNameKredit();
            history += "; Nomor kredit: " + newPinjaman.getNoKredit();
            history += "; Nomor nasabah: " + newAnggota.getNoAnggota();
            history += "; Tanggal pengajuan: " + Formater.formatDate(newPinjaman.getTglPengajuan(), "yyyy-MM-dd");
            history += "; Jumlah pengajuan: " + Formater.formatNumber(newPinjaman.getJumlahPinjaman(), "#,###.##");
            history += "; Jangka waktu: " + newPinjaman.getJangkaWaktu();
            history += "; Jumlah angsuran: " + Formater.formatNumber(newPinjaman.getJumlahAngsuran(), "#,###.##");
            history += "; Sumber dana: " + newSumberDana.getKodeSumberDana() + "-" + newSumberDana.getJudulSumberDana();
            history += "; Tanggal lunas: " + Formater.formatDate(newPinjaman.getTglLunas(), "yyyy-MM-dd");
            history += "; Tipe bunga: " + this.TIPE_BUNGA_TITLE[newPinjaman.getTipeBunga()];
            history += "; Suku bunga: " + newPinjaman.getSukuBunga();
            history += "; Tanggal jatuh tempo: " + Formater.formatDate(newPinjaman.getJatuhTempo(), "yyyy-MM-dd");
            history += "; Kolektibilitas: " + newKolektibilitas.getJudulKolektibilitas();
            history += "; Tanggal realisasi: " + Formater.formatDate(newPinjaman.getTglRealisasi(), "yyyy-MM-dd");
            history += "; Keterangan: " + newPinjaman.getKeterangan() + "; ";

        } else {
            if (prevPinjaman.getStatusPinjaman() != newPinjaman.getStatusPinjaman()) {
                history += "Status dokumen : " + this.getStatusDocTitle(prevPinjaman.getStatusPinjaman()) + " changed to " + this.getStatusDocTitle(newPinjaman.getStatusPinjaman()) + "; ";
            }
            if (prevPinjaman.getTipeKreditId() != newPinjaman.getTipeKreditId()) {
                history += "Jenis kredit : " + prevTypeKredit.getNameKredit() + " changed to " + newTypeKredit.getNameKredit() + "; ";
            }
            if (!prevPinjaman.getNoKredit().equals(newPinjaman.getNoKredit())) {
                history += "Nomor kredit : " + prevPinjaman.getNoKredit() + " changed to " + newPinjaman.getNoKredit() + "; ";
            }
            if (prevPinjaman.getAnggotaId() != newPinjaman.getAnggotaId()) {
                history += "Nomor nasabah : " + prevAnggota.getNoAnggota() + " changed to " + newAnggota.getNoAnggota() + "; ";
            }
            String prevTglPengajuan = Formater.formatDate(prevPinjaman.getTglPengajuan(), "yyyy-MM-dd");
            String newTglPengajuan = Formater.formatDate(newPinjaman.getTglPengajuan(), "yyyy-MM-dd");
            if (!prevTglPengajuan.equals(newTglPengajuan)) {
                history += "Tanggal pengajuan : " + prevTglPengajuan + " changed to " + newTglPengajuan + "; ";
            }
            if (prevPinjaman.getJumlahPinjaman() != newPinjaman.getJumlahPinjaman()) {
                history += "Jumlah pengajuan : " + Formater.formatNumber(prevPinjaman.getJumlahPinjaman(), "#,###.##") + " changed to " + Formater.formatNumber(newPinjaman.getJumlahPinjaman(), "#,###.##") + "; ";
            }
            if (prevPinjaman.getJangkaWaktu() != newPinjaman.getJangkaWaktu()) {
                history += "Jangka waktu : " + prevPinjaman.getJangkaWaktu() + " changed to " + newPinjaman.getJangkaWaktu() + "; ";
            }
            if (prevPinjaman.getJumlahAngsuran() != newPinjaman.getJumlahAngsuran()) {
                history += "Jumlah angsuran : " + Formater.formatNumber(prevPinjaman.getJumlahAngsuran(), "#,###.##") + " changed to " + Formater.formatNumber(newPinjaman.getJumlahAngsuran(), "#,###.##") + "; ";
            }
            if (prevPinjaman.getSumberDanaId() != newPinjaman.getSumberDanaId()) {
                history += "Sumber dana : " + prevSumberDana.getKodeSumberDana() + "-" + prevSumberDana.getJudulSumberDana() + " changed to " + newSumberDana.getKodeSumberDana() + "-" + newSumberDana.getJudulSumberDana() + "; ";
            }
            String prevTglLunas = Formater.formatDate(prevPinjaman.getTglLunas(), "yyyy-MM-dd");
            String newTglLunas = Formater.formatDate(newPinjaman.getTglLunas(), "yyyy-MM-dd");
            if (!prevTglLunas.equals(newTglLunas)) {
                history += "Tanggal lunas : " + prevTglLunas + " changed to " + newTglLunas + "; ";
            }
            if (prevPinjaman.getTipeBunga() != newPinjaman.getTipeBunga()) {
                history += "Tipe bunga : " + this.TIPE_BUNGA_TITLE[prevPinjaman.getTipeBunga()] + " changed to " + this.TIPE_BUNGA_TITLE[newPinjaman.getTipeBunga()] + "; ";
            }
            if (prevPinjaman.getSukuBunga() != newPinjaman.getSukuBunga()) {
                history += "Suku bunga : " + prevPinjaman.getSukuBunga() + " changed to " + newPinjaman.getSukuBunga() + "; ";
            }
            String prevTglTempo = Formater.formatDate(prevPinjaman.getJatuhTempo(), "yyyy-MM-dd");
            String newTglTempo = Formater.formatDate(newPinjaman.getJatuhTempo(), "yyyy-MM-dd");
            if (!prevTglTempo.equals(newTglTempo)) {
                history += "Tanggal jatuh tempo : " + prevTglTempo + " changed to " + newTglTempo + "; ";
            }
            if (prevPinjaman.getKodeKolektibilitas() != newPinjaman.getKodeKolektibilitas()) {
                history += "Kolektibilitas : " + prevKolektibilitas.getJudulKolektibilitas() + " changed to " + newKolektibilitas.getJudulKolektibilitas() + "; ";
            }
            String prevTglRealisasi = Formater.formatDate(prevPinjaman.getTglRealisasi(), "yyyy-MM-dd");
            String newTglRealisasi = Formater.formatDate(newPinjaman.getTglRealisasi(), "yyyy-MM-dd");
            if (!prevTglRealisasi.equals(newTglRealisasi)) {
                history += "Tanggal realisasi : " + prevTglRealisasi + " changed to " + newTglRealisasi + "; ";
            }
            if (!prevPinjaman.getKeterangan().equals(newPinjaman.getKeterangan())) {
                history += "Keterangan : " + prevPinjaman.getKeterangan() + " changed to " + newPinjaman.getKeterangan() + "; ";
            }
            if (prevPinjaman.getAccountOfficerId() != newPinjaman.getAccountOfficerId()) {
                Anggota tempOld = PstAnggota.fetchExc(prevPinjaman.getAccountOfficerId());
                Anggota tempNew = PstAnggota.fetchExc(newPinjaman.getAccountOfficerId());
                history += "Account Officer : " + tempOld.getName() + " changed to " + tempNew.getName() + "; ";
            }
            if (prevPinjaman.getCollectorId() != newPinjaman.getCollectorId()) {
                Anggota tempOld = PstAnggota.fetchExc(prevPinjaman.getCollectorId());
                Anggota tempNew = PstAnggota.fetchExc(newPinjaman.getCollectorId());
                history += "Collector : " + tempOld.getName() + " changed to " + tempNew.getName() + "; ";
            }
            if (!history.equals("")) {
//                history = "<p><b>UPDATE : </b>" + history;
            }
        }
        return history;
    }

    /**
     * @return the tipeJadwal
     */
    public int getTipeJadwal() {
        return tipeJadwal;
    }

    /**
     * @param tipeJadwal the tipeJadwal to set
     */
    public void setTipeJadwal(int tipeJadwal) {
        this.tipeJadwal = tipeJadwal;
    }

    /**
     * @return the billMainId
     */
    public long getBillMainId() {
        return billMainId;
    }

    /**
     * @param billMainId the billMainId to set
     */
    public void setBillMainId(long billMainId) {
        this.billMainId = billMainId;
    }

    /**
     * @return the downPayment
     */
    public double getDownPayment() {
        return downPayment;
    }

    /**
     * @param downPayment the downPayment to set
     */
    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    /**
     * @return the sisaAngsuran
     */
    public double getSisaAngsuran() {
        return sisaAngsuran;
    }

    /**
     * @param sisaAngsuran the sisaAngsuran to set
     */
    public void setSisaAngsuran(double sisaAngsuran) {
        this.sisaAngsuran = sisaAngsuran;
    }

    /**
     * @return the accountOfficerId
     */
    public long getAccountOfficerId() {
        return accountOfficerId;
    }

    /**
     * @param accountOfficerId the accountOfficerId to set
     */
    public void setAccountOfficerId(long accountOfficerId) {
        this.accountOfficerId = accountOfficerId;
    }

    /**
     * @return the collectorId
     */
    public long getCollectorId() {
        return collectorId;
    }

    /**
     * @param collectorId the collectorId to set
     */
    public void setCollectorId(long collectorId) {
        this.collectorId = collectorId;
    }

    /**
     * @return the lokasiPenagihan
     */
    public int getLokasiPenagihan() {
        return lokasiPenagihan;
}

    /**
     * @param lokasiPenagihan the lokasiPenagihan to set
     */
    public void setLokasiPenagihan(int lokasiPenagihan) {
        this.lokasiPenagihan = lokasiPenagihan;
    }

    /**
     * @return the statusDenda
     */
    public int getStatusDenda() {
        return statusDenda;
    }

    /**
     * @param statusDenda the statusDenda to set
     */
    public void setStatusDenda(int statusDenda) {
        this.statusDenda = statusDenda;
    }

}
