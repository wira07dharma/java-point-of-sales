/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.sedana.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author Regen
 */
public interface I_Sedana {
  public static final int TIPE_ARUS_KAS_BERTAMBAH = 0; //Penambahan terhadap koperasi.
  public static final int TIPE_ARUS_KAS_BERKURANG = 1; //Pengurangan terhadap koperasi.
  
  public static final String[] TIPE_ARUS_KAS_TITLE = {"Penambahan", "Pengurangan"};
  
  public static final int TIPE_PROSEDUR_BY_SYSTEM = 0; //Aksi dilakukan oleh sistem.
  public static final int TIPE_PROSEDUR_BY_TELLER = 1; //Aksi dilakukan oleh teller/fo.
  
  public static final String[] TIPE_PROSEDUR_TITLE = {"By sistem", "By teller"};
  
  public static final int TUJUAN_PROSEDUR_TABUNGAN = 0; //Transaksi untuk keperluan tabungan.
  public static final int TUJUAN_PROSEDUR_KREDIT = 1; //Transaksi untuk keperluan kredit.
  public static final int TUJUAN_PROSEDUR_DEPOSITO = 2; //Transaksi untuk keperluan deposito.
  
  public static final String[] TUJUAN_PROSEDUR_TITLE = {"Tabungan", "Kredit", "Deposito"};
  
  public static final int TIPE_TRANSAKSI_NORMAL = 0; //perpindahan uang tanpa laba rugi
  public static final int TIPE_TRANSAKSI_PENDAPATAN = 1; //Transaksi pendapatan untuk koperasi misal fee buka rekening
  public static final int TIPE_TRANSAKSI_PENGELUARAN = 2; // misal bunga tabungan nasabah
  
  public static final String[] TIPE_TRANSAKSI_TITLE = {"Normal", "Pendapatan", "Pengeluaran"};
  
  public static final int STATUS_JENIS_TRANSAKSI_TIDAK_AKTIF = 0; // jenis transaksi tidak digunakan (lagi)
  public static final int STATUS_JENIS_TRANSAKSI_AKTIF = 1; // jenis transaksi (masih) digunakan
  
  public static final String[] STATUS_JENIS_TRANSAKSI_TITLE = {"Tidak aktif", "Aktif"};
  
  public final static int TIPE_DOC_TABUNGAN = 0;
  public final static int TIPE_DOC_KREDIT_NASABAH_BIAYA_ASURANSI = 1;
  public final static int TIPE_DOC_KREDIT_NASABAH_BIAYA_UMUM = 2;
  public final static int TIPE_DOC_KREDIT_NASABAH_BIAYA_PERUSAHAAN = 3;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_POKOK = 4;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA = 5;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_DENDA = 6;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_BUNGA = 7;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_DENDA = 8;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_SISA_KEMBALIAN = 12;
  public final static int TIPE_DOC_TABUNGAN_BIAYA_BULANAN = 13;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_PENALTI_PELUNASAN_DINI = 14;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PENDAPATAN_PENALTI_PELUNASAN_MACET = 15;
  public final static int TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA_TAMBAHAN = 16;
  public final static int TIPE_DOC_KREDIT_NASABAH_ANGSURAN = 17;
  public final static int TIPE_DOC_KREDIT_NASABAH_DOWN_PAYMENT = 18;

  public final static String[] TIPE_DOC_TITLE = {
      "Tabungan",
      "Biaya Asuransi",
      "Biaya Kredit Ditanggung Nasabah",
      "Biaya Kredit Ditanggung Perusahaan",
      "Pos Piutang Pokok",
      "Pos Piutang Bunga",
      "Pos Piutang Denda",
      "Pos Pendapatan Bunga",
      "Pos Pendapatan Denda",
      "Pos Pendapatan Denda",
      "Pos Pendapatan Denda",
      "Pos Pendapatan Denda",
      "Pos Pendapatan Sisa Kembalian",
      "Pos Tabungan: Biaya Bulanan",
      "Pos Pendapatan Penalti Pelunasan Lebih Awal",
      "Pos Pendapatan Penalti Pelunasan Macet",
      "Pos Piutang Bunga Tambahan",
	  //Untuk Raditya
      "Angsuran",
	  "Down Payment"
  };

  public final static int TIPE_DOC_JUMLAH_MULTIPLE = 0;
  public final static int TIPE_DOC_JUMLAH_SINGLE = 1;

  public final static String[] TIPE_DOC_JUMLAH_TITLE = {"Multiple", "Single"};

  public final static int[] TIPE_DOC_JUMLAH_DATA = {
      TIPE_DOC_JUMLAH_MULTIPLE,
      TIPE_DOC_JUMLAH_MULTIPLE,
      TIPE_DOC_JUMLAH_MULTIPLE,
      TIPE_DOC_JUMLAH_MULTIPLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE,
      TIPE_DOC_JUMLAH_SINGLE
  };

  public final static int TIPE_DOC_INPUT_OPTIONAL = 0;
  public final static int TIPE_DOC_INPUT_REQUIRED = 1;

  public final static String[] TIPE_DOC_INPUT_TITLE = {"Opsional", "Wajib Isi"};

  public final static int[] TIPE_DOC_INPUT_DATA = {
      TIPE_DOC_INPUT_OPTIONAL,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_OPTIONAL,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
      TIPE_DOC_INPUT_REQUIRED,
  };

  public static final int STATUS_DOC_TRANSAKSI_OPEN = 2;
  public static final int STATUS_DOC_TRANSAKSI_CLOSED = 5;
  public static final int STATUS_DOC_TRANSAKSI_POSTED = 7;
  
  public static final String[] STATUS_DOC_TRANSAKSI_TITLE = {"", "", "Open", "", "", "Closed", "", "Posted"};
  
  public static final int STATUS_AKTIF = 1;
  public static final int STATUS_TIDAK_AKTIF = 0;
  public static final String[] STATUS_AKTIF_TITLE = {"Tidak aktif", "Aktif"};
  
  //--- Variable use case type transaksi ---------------------------------------
  public static final int USECASE_TYPE_TABUNGAN_SETORAN = 0;
  public static final int USECASE_TYPE_TABUNGAN_PENARIKAN = 1;
  public static final int USECASE_TYPE_TABUNGAN_BUNGA_PENCATATAN = 2;
  public static final int USECASE_TYPE_TABUNGAN_POTONGAN_ADMIN_PENCATATAN = 3;
  public static final int USECASE_TYPE_TABUNGAN_PENUTUPAN = 4;
  public static final int USECASE_TYPE_KREDIT_PENCAIRAN = 100;
  public static final int USECASE_TYPE_KREDIT_ANGSURAN_PAYMENT = 101;
  public static final int USECASE_TYPE_KREDIT_PENUTUPAN = 102;
  public static final int USECASE_TYPE_KREDIT_PEMUTUSAN = 103;
  public static final int USECASE_TYPE_KREDIT_DENDA_PENCATATAN = 104;
  public static final int USECASE_TYPE_KREDIT_BUNGA_TAMBAHAN_PENCATATAN = 105;
  public static final int USECASE_TYPE_KREDIT_BIAYA = 106;
  public static final int USECASE_TYPE_KREDIT_PENALTY_DINI_PENCATATAN = 150;
  public static final int USECASE_TYPE_KREDIT_PENALTY_MACET_PENCATATAN = 151;
  public static final int USECASE_TYPE_DEPOSITO = 200;
  public static final int USECASE_TYPE_KREDIT_RETURN = 205;
  
  public static final HashMap<Integer, String> USECASE_TYPE_TITLE = new HashMap<Integer, String>() {{
    put(USECASE_TYPE_TABUNGAN_SETORAN, "Setoran tabungan");
    put(USECASE_TYPE_TABUNGAN_PENARIKAN, "Penarikan tabungan");
    put(USECASE_TYPE_TABUNGAN_BUNGA_PENCATATAN, "Pencatatan bunga tabungan");
    put(USECASE_TYPE_TABUNGAN_POTONGAN_ADMIN_PENCATATAN, "Pencatatan potongan admin");
    put(USECASE_TYPE_TABUNGAN_PENUTUPAN, "Penutupan Tabungan");
    put(USECASE_TYPE_KREDIT_PENCAIRAN, "Pencairan kredit");
    put(USECASE_TYPE_KREDIT_ANGSURAN_PAYMENT, "Pembayaran angsuran kredit");
    put(USECASE_TYPE_KREDIT_PENUTUPAN, "Penutupan kredit");
    put(USECASE_TYPE_KREDIT_PEMUTUSAN, "Pemutusan kredit");
    put(USECASE_TYPE_KREDIT_DENDA_PENCATATAN, "Pencatatan denda");
    put(USECASE_TYPE_KREDIT_BUNGA_TAMBAHAN_PENCATATAN, "Pencatatan bunga kredit tambahan");
    put(USECASE_TYPE_KREDIT_BIAYA, "Biaya Transaksi");
    put(USECASE_TYPE_KREDIT_PENALTY_DINI_PENCATATAN, "Pencatatan penalti pelunasan dini");
    put(USECASE_TYPE_KREDIT_PENALTY_MACET_PENCATATAN, "Pencatatan penalti pelunasan macet");
    put(USECASE_TYPE_DEPOSITO, "Deposito");
    put(USECASE_TYPE_KREDIT_RETURN, "Return Kredit");
  }};
  
  //===== SETTING VALUE UNTUK PERHITUNGAN DENDA =====
  //ALASAN DIKENAKAN DENDA :
  public static final int TIPE_DENDA_BERLAKU_SEMUA = 0; // dikenakan denda jika semua angsuran belum lunas
  public static final int TIPE_DENDA_BERLAKU_POKOK = 1; // dikenakan denda jika angsuran pokok belum lunas
  public static final int TIPE_DENDA_BERLAKU_BUNGA = 2; // dikenakan denda jika angsuran bunga belum lunas
  public static final String[] TIPE_DENDA_BERLAKU_TITLE = {"Semua angsuran belum lunas","Angsuran pokok belum lunas","Angsuran bunga belum lunas"};
  
  //ATURAN KAPAN AKAN DIKENAKAN DENDA :
  public static final int SATUAN_FREKUENSI_DENDA_BULANAN = 0; //denda dikenakan setiap memasuki bulan kalender baru (misal angsuran bulan januari dibayar setelah bulan januari sudah berakhir)
  public static final int SATUAN_FREKUENSI_DENDA_HARIAN = 1; //denda dikenakan per hari setelah lewat jatuh tempo
  public static final int SATUAN_FREKUENSI_DENDA_JATUH_TEMPO = 2; //denda dikenakan jika angsuran belum dibayar setelah lewat dari tgl jatuh tempo yg sdh ditentukan
  public static final String[] SATUAN_FREKUENSI_DENDA_TITLE = {"Bulan baru","Hari", /* "Jatuh tempo" */};
  
  //VARIABEL YG AKAN DIHITUNG UNTUK MENDAPATKAN NILAI DENDA :
  public static final int VARIABEL_DENDA_SEMUA = 0; //semua nilai angsuran akan dihitung
  public static final int VARIABEL_DENDA_POKOK = 1; //hanya angsuran pokok yang dihitung
  public static final int VARIABEL_DENDA_BUNGA = 2; //hanya angsuran bunga yang dihitung
  public static final String[] VARIABEL_DENDA_TITLE = {"Semua angsuran","Angsuran pokok","Angsuran bunga"};
  
  //APAKAH VARIABEL DIHITUNG FULL NILAI ANGSURAN ATAU SISA NILAI ANGSURAN YG BELUM DIBAYAR :
  public static final int TIPE_PERHITUNGAN_DENDA_SISA = 0; //nilai variabel yg dihitung hanya sisa dari angsuran yg belum dibayar
  public static final int TIPE_PERHITUNGAN_DENDA_FULL = 1; //nilai variabel yg dihitung adalah seluruh nilai angsuran
  public static final String[] TIPE_PERHITUNGAN_DENDA_TITLE = {"Sisa","Full"};
  
  //TIPE VARIABEL YG DIHITUNG APAKAH TOTAL DARI SELURUH JADWAL ANGSURAN ATAU NILAI ANGSURAN PER JADWAL :
  public static final int TIPE_VARIABEL_DENDA_AKUMULASI = 0; //nilai variabel diambil dari total seluruh jadwal
  public static final int TIPE_VARIABEL_DENDA_PER_JADWAL = 1; //nilai variabel diambil dari jadwal yg terlambat
  public static final String[] TIPE_VARIABEL_DENDA_TITLE = {"Akumulasi","Per jadwal"};
  
  //TIPE FREKUENSI DENDA APAKAH JADWAL DENDA DIBUAT HANYA 1 KALI ATAU BERLIPAT SESUAI PERIODE KETERLAMBATAN :
  public static final int TIPE_FREKUENSI_DENDA_BERLIPAT = 0; //jumlah jadwal denda hanya dibuat 1 kali per jadwal
  public static final int TIPE_FREKUENSI_DENDA_TUNGGAL = 1; //jumlah jadwal denda dibuat berlipat sesuai periode terlambat
  public static final String[] TIPE_FREKUENSI_DENDA_TITLE = {"Denda berlipat","Denda tunggal"};
  
  //CONTOH CASE SETTING DENDA:
  /*
  SETTING YG DIPILIH :
  - TIPE_DENDA_BERLAKU_SEMUA > denda dikenakan jika angsuran pokok dan bunga belum di bayar
  - SATUAN_FREKUENSI_DENDA_HARIAN > jika di beri nilai 1 maka denda akan dikenakan setiap 1 hari
  
  - VARIABEL_DENDA_POKOK
  - TIPE_PERHITUNGAN_DENDA_SISA
  - TIPE_VARIABEL_DENDA_AKUMULASI
  = rumus untuk hitung denda : (sisa dari seluruh angsuran pokok yg belum dibayar) * persentase denda
  */
  //===== END SETTING PERHITUNGAN DENDA =====
  
  public static final int JABATAN_PEMILIK_BUKAN_PENGURUS = 0;
  public static final int JABATAN_PEMILIK_MASYARAKAT = 1;
  public static final int JABATAN_PEMILIK_KETUA_UMUM = 2;
  public static final int JABATAN_PEMILIK_KETUA = 3;
  public static final int JABATAN_PEMILIK_SEKRETARIS = 4;
  public static final int JABATAN_PEMILIK_BENDAHARA = 5;
  public static final int JABATAN_PEMILIK_LAINNYA = 6;
  
  public static final String[] JABATAN_PEMILIK_TITLE = {
      "PEMILIK - Pemilik Bukan Pengurus",
      "PEMILIK Masyarakat",
      "PEMILIK - Ketua Umum",
      "PEMILIK Ketua",
      "PEMILIK Sekretaris",
      "PEMILIK - Bendahara",
      "PEMILIK - Lainnya"
  };
  
  public static final int TIPE_PENARIKAN_TABUNGAN_TANGGAL = 1;
  public static final int TIPE_PENARIKAN_TABUNGAN_BULAN = 2;
  
  public static final String[] TIPE_PENARIKAN_TABUNGAN_TITLE = {
      "Tidak Diketahui",
      "Range Tanggal",
      "Range Bulan"
  };
  
  public static int TIPE_KREDIT_HARIAN = 0;
  public static int TIPE_KREDIT_MINGGUAN = 1;
  public static int TIPE_KREDIT_BULANAN = 2;
  public static int TIPE_KREDIT_MUSIMAN = 3;
  public static final ArrayList<String[]> TIPE_KREDIT = new ArrayList<String[]>(){{
    add(new String[]{"Harian", "Daily"});
    add(new String[]{"Mingguan", "Weekly"});
    add(new String[]{"Bulanan", "Monthly"});
    add(new String[]{"Musiman", "Term"});
  }};
  
  public static final HashMap<Integer, String[]> DAYS = new HashMap<Integer, String[]>() {{
    put(Calendar.SUNDAY, new String[]{"Minggu", "Sunday"});
    put(Calendar.MONDAY, new String[]{"Senin", "Monday"});
    put(Calendar.TUESDAY, new String[]{"Selasa", "Tuesday"});
    put(Calendar.WEDNESDAY, new String[]{"Rabu", "Wednesday"});
    put(Calendar.THURSDAY, new String[]{"Kamis", "Thursday"});
    put(Calendar.FRIDAY, new String[]{"Jumat", "Friday"});
    put(Calendar.SATURDAY, new String[]{"Sabtu", "Saturday"});
  }};
  
  public static final int BUNGA_SALDO_HARIAN = 0;
  public static final int BUNGA_SALDO_RERATA_HARIAN = 1;
  public static final int BUNGA_SALDO_TERENDAH = 2;
  public static final int BUNGA_SALDO_DEPOSITO = 3;
  public static final int BUNGA_SALDO_BUNGA_BERBUNGA = 4;
  public static final int BUNGA_SALDO_TERAKHIR = 5;
  public static final int BUNGA_SALDO_PERTAMA = 6;
  public static final HashMap<Integer, String[]> BUNGA = new HashMap<Integer, String[]>() {{
    put(BUNGA_SALDO_HARIAN, new String[] {"Saldo Harian","Saldo Harian"});
    put(BUNGA_SALDO_RERATA_HARIAN, new String[] {"Saldo Rata-rata Harian","Saldo Rata-rata Harian"});
    put(BUNGA_SALDO_TERENDAH,  new String[] {"Saldo Terendah","Saldo Terendah"});
    put(BUNGA_SALDO_DEPOSITO,  new String[] {"Deposito","Deposito"});
    put(BUNGA_SALDO_BUNGA_BERBUNGA, new String[] {"Bunga Berbunga","Bunga Berbunga"});
    put(BUNGA_SALDO_TERAKHIR, new String[] {"Saldo Terakhir","Saldo Terakhir"});
    put(BUNGA_SALDO_PERTAMA, new String[] {"Saldo Pertama","Saldo Pertama"});
  }};
  
  //KODE TRANSAKSI
  public static final String KODE_TRANSAKSI_TABUNGAN_SETORAN = "TS";
  public static final String KODE_TRANSAKSI_TABUNGAN_PENARIKAN = "TP";
  public static final String KODE_TRANSAKSI_TABUNGAN_PENCATATAN_BUNGA = "TB";
  public static final String KODE_TRANSAKSI_TABUNGAN_PENCATATAN_POTONGAN_ADMIN = "TA";
  public static final String KODE_TRANSAKSI_TABUNGAN_PENUTUPAN = "TT";
  public static final String KODE_TRANSAKSI_TABUNGAN_SETORAN_PENGENDAPAN_KREDIT = "TKES";
  public static final String KODE_TRANSAKSI_TABUNGAN_PENARIKAN_PENGENDAPAN_KREDIT = "TKET";
  public static final String KODE_TRANSAKSI_KREDIT_PENCAIRAN = "KP";
  public static final String KODE_TRANSAKSI_KREDIT_PEMBAYARAN_ANGSURAN = "KA";
  public static final String KODE_TRANSAKSI_KREDIT_PEMBAYARAN_BIAYA = "KB";
  public static final String KODE_TRANSAKSI_KREDIT_PENCATATAN_BUNGA_TAMBAHAN = "KBT";
  public static final String KODE_TRANSAKSI_KREDIT_PENCATATAN_DENDA = "KD";
  public static final String KODE_TRANSAKSI_KREDIT_PENCATATAN_PENALTI_MACET = "KLM";
  public static final String KODE_TRANSAKSI_KREDIT_PENCATATAN_PENALTI_LUNAS_DINI = "KLD";
  
}
