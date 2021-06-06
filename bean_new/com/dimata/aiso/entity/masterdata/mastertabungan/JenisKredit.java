/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.mastertabungan;

import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.common.I_Sedana;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author dw1p4
 */
public class JenisKredit extends Entity implements I_Sedana {

    private double minKredit;
    private double maxKredit;
    private double bungaMin;
    private double bungaMax;
    private double biayaAdmin;
    private double provisi;
    private double denda;
    private float jangkaWaktuMin;
    private float jangkaWaktuMax;
    private String kegunaan = "";
    private int tipeBunga = 0;
    private Date berlakuMulai;
    private Date berlakuSampai;
    private int dendaToleransi = 0;
    private int periodePembayaran = 0;
    private long kategoriKreditId = 0;
    private double persentaseWajib = 0; // Persentase ketika pencairan kredit yang dialokasikan ke tabungan wajib. 0=tidak ada
    private double nominalWajib = 0;

    //update tgl 15 Maret 2013 oleh Hadi
    private String namaKredit = "";
    //update dewok
    private int tipeDendaBerlaku = 0;
    private int tipePerhitunganDenda = 0;
    private int frekuensiDenda = 0;
    private int satuanFrekuensiDenda = 0;
    //regen
    private int tipeFrekuensiPokok = 0;
    private int tipeFrekuensiPokokLegacy = 0;
    private int frekuensiPokok = 0;
    //added by dewok 2018-05-22
    private int variabelDenda = 0;
    //added by dewok 20181026
    private int tipeVariabelDenda = 0;
    private int tipeFrekuensiDenda = 0;
    private HashMap<Integer, Boolean> weekValues = new HashMap<Integer, Boolean>() {
        {
            put(Calendar.SUNDAY, false);
            put(Calendar.MONDAY, false);
            put(Calendar.TUESDAY, false);
            put(Calendar.WEDNESDAY, false);
            put(Calendar.THURSDAY, false);
            put(Calendar.FRIDAY, false);
            put(Calendar.SATURDAY, false);
        }
    };

    public int getTipeDendaBerlaku() {
        return tipeDendaBerlaku;
    }

    public void setTipeDendaBerlaku(int tipeDendaBerlaku) {
        this.tipeDendaBerlaku = tipeDendaBerlaku;
    }

    public int getTipePerhitunganDenda() {
        return tipePerhitunganDenda;
    }

    public void setTipePerhitunganDenda(int tipePerhitunganDenda) {
        this.tipePerhitunganDenda = tipePerhitunganDenda;
    }

    public int getFrekuensiDenda() {
        return frekuensiDenda;
    }

    public void setFrekuensiDenda(int frekuensiDenda) {
        this.frekuensiDenda = frekuensiDenda;
    }

    public int getSatuanFrekuensiDenda() {
        return satuanFrekuensiDenda;
    }

    public void setSatuanFrekuensiDenda(int satuanFrekuensiDenda) {
        this.satuanFrekuensiDenda = satuanFrekuensiDenda;
    }

    /**
     * @return the minKredit
     */
    public double getMinKredit() {
        return minKredit;
    }

    /**
     * @param minKredit the minKredit to set
     */
    public void setMinKredit(double minKredit) {
        this.minKredit = minKredit;
    }

    /**
     * @return the maxKredit
     */
    public double getMaxKredit() {
        return maxKredit;
    }

    /**
     * @param maxKredit the maxKredit to set
     */
    public void setMaxKredit(double maxKredit) {
        this.maxKredit = maxKredit;
    }

    /**
     * @return the bungaMin
     */
    public double getBungaMin() {
        return bungaMin;
    }

    /**
     * @param bungaMin the bungaMin to set
     */
    public void setBungaMin(double bungaMin) {
        this.bungaMin = bungaMin;
    }

    /**
     * @return the bungaMax
     */
    public double getBungaMax() {
        return bungaMax;
    }

    /**
     * @param bungaMax the bungaMax to set
     */
    public void setBungaMax(double bungaMax) {
        this.bungaMax = bungaMax;
    }

    /**
     * @return the biayaAdmin
     */
    public double getBiayaAdmin() {
        return biayaAdmin;
    }

    /**
     * @param biayaAdmin the biayaAdmin to set
     */
    public void setBiayaAdmin(double biayaAdmin) {
        this.biayaAdmin = biayaAdmin;
    }

    /**
     * @return the provisi
     */
    public double getProvisi() {
        return provisi;
    }

    /**
     * @param provisi the provisi to set
     */
    public void setProvisi(double provisi) {
        this.provisi = provisi;
    }

    /**
     * @return the denda
     */
    public double getDenda() {
        return denda;
    }

    /**
     * @param denda the denda to set
     */
    public void setDenda(double denda) {
        this.denda = denda;
    }

    /**
     * @return the kegunaan
     */
    public String getKegunaan() {
        return kegunaan;
    }

    /**
     * @param kegunaan the kegunaan to set
     */
    public void setKegunaan(String kegunaan) {
        this.kegunaan = kegunaan;
    }

    /**
     * @return the namaKredit
     */
    public String getNamaKredit() {
        return namaKredit;
    }

    /**
     * @return the namaKredit
     */
    public String getNameKredit() {
        return namaKredit;
    }

    /**
     * @param namaKredit the namaKredit to set
     */
    public void setNamaKredit(String namaKredit) {
        if (namaKredit == null) {
            namaKredit = "";
        }
        this.namaKredit = namaKredit;
    }

    /**
     * @param namaKredit the namaKredit to set
     */
    public void setNameKredit(String namaKredit) {
        if (namaKredit == null) {
            namaKredit = "";
        }
        this.namaKredit = namaKredit;
    }

    /**
     * @return the tipeBunga
     */
    public int getTipeBunga() {
        return tipeBunga;
    }

    /**
     * @param tipeBunga the tipeBunga to set
     */
    public void setTipeBunga(int tipeBunga) {
        this.tipeBunga = tipeBunga;
    }

    /**
     * @return the berlakuMulai
     */
    public Date getBerlakuMulai() {
        return berlakuMulai;
    }

    /**
     * @param berlakuMulai the berlakuMulai to set
     */
    public void setBerlakuMulai(Date berlakuMulai) {
        this.berlakuMulai = berlakuMulai;
    }

    /**
     * @return the berlakuSampai
     */
    public Date getBerlakuSampai() {
        return berlakuSampai;
    }

    /**
     * @param berlakuSampai the berlakuSampai to set
     */
    public void setBerlakuSampai(Date berlakuSampai) {
        this.berlakuSampai = berlakuSampai;
    }

    /**
     * @return the jangkaWaktuMin
     */
    public float getJangkaWaktuMin() {
        return jangkaWaktuMin;
    }

    /**
     * @param jangkaWaktuMin the jangkaWaktuMin to set
     */
    public void setJangkaWaktuMin(float jangkaWaktuMin) {
        this.jangkaWaktuMin = jangkaWaktuMin;
    }

    /**
     * @return the jangkaWaktuMax
     */
    public float getJangkaWaktuMax() {
        return jangkaWaktuMax;
    }

    /**
     * @param jangkaWaktuMax the jangkaWaktuMax to set
     */
    public void setJangkaWaktuMax(float jangkaWaktuMax) {
        this.jangkaWaktuMax = jangkaWaktuMax;
    }

    /**
     * @return the tipeFrekuensiPokok
     */
    public int getTipeFrekuensiPokok() {
        return tipeFrekuensiPokok;
    }

    /**
     * @return the frekuensiPokok
     */
    public int getFrekuensiPokok() {
        return frekuensiPokok;
    }

    /**
     * @param frekuensiPokok the frekuensiPokok to set
     */
    public void setFrekuensiPokok(int frekuensiPokok) {
        this.frekuensiPokok = frekuensiPokok;
    }

    /**
     * @return the weekValues
     */
    public HashMap<Integer, Boolean> getWeekValues() {
        return weekValues;
    }

    /**
     * @param weekValues the weekValues to set
     */
    public void setWeekValues(HashMap<Integer, Boolean> weekValues) {
        this.weekValues = weekValues;
    }

    /**
     * @param weekValues the weekValues to set
     */
    public void setWeekValues(String[] values) {
        if (values != null) {
            for (String val : values) {
                this.weekValues.put(Integer.valueOf(val), Boolean.TRUE);
            }
        }
    }

    /**
     * @return the tipeFrekuensiPokokLegeacy
     */
    public int getTipeFrekuensiPokokLegacy() {
        return tipeFrekuensiPokokLegacy;
    }

    /**
     * @param tipeFrekuensiPokokLegeacy the tipeFrekuensiPokokLegeacy to set
     */
    public void setTipeFrekuensiPokokLegacy(int tipeFrekuensiPokokLegeacy) {
        this.tipeFrekuensiPokokLegacy = tipeFrekuensiPokokLegeacy;
    }

    /**
     * @param tipeFrekuensiPokok the tipeFrekuensiPokok to set
     */
    public void setTipeFrekuensiPokok(int tipeFrekuensiPokok) {
        this.tipeFrekuensiPokok = tipeFrekuensiPokok;
        String bin = Integer.toBinaryString(tipeFrekuensiPokok);
        if (bin.length() >= 5) {
            int type = Integer.valueOf(bin.substring(1, 5), 2);
            this.tipeFrekuensiPokokLegacy = type;
            switch (type) {
                case I_Sedana.TIPE_KREDIT_HARIAN:
                    String day = bin.substring(5, bin.length());
                    for (int i = 0; i < day.length(); i++) {
                        weekValues.put(i + 1, (day.charAt(i) == '1' ? true : false));
                    }
                    break;
                case I_Sedana.TIPE_KREDIT_MINGGUAN:
                    String frekuensiMingguan = bin.substring(5, bin.length());
                    setFrekuensiPokok(Integer.valueOf(frekuensiMingguan, 2));
                    break;
                case I_Sedana.TIPE_KREDIT_MUSIMAN:
                    String frekuensiMusiman = bin.substring(5, bin.length());
                    setFrekuensiPokok(Integer.valueOf(frekuensiMusiman, 2));
                    break;
            }
        }
    }

    public void setTipeFrekuensiPokok() {
        setTipeFrekuensiPokok(null, null);
    }

    public void setTipeFrekuensiPokok(Integer tipeFrekuensiPokokLegacy, Object freq) {
        int type = (tipeFrekuensiPokokLegacy != null ? tipeFrekuensiPokokLegacy : this.tipeFrekuensiPokokLegacy);
        this.tipeFrekuensiPokokLegacy = type;
        String bin = Integer.toBinaryString(type);
        String nil = "0000";
        bin = "1" + nil.substring(0, (nil.length()) - bin.length()) + bin;
        switch (type) {
            case I_Sedana.TIPE_KREDIT_HARIAN:
                this.weekValues = ((freq != null) ? (HashMap<Integer, Boolean>) freq : this.weekValues);
                String bin2 = "";
                for (int i = 1; i < weekValues.size() + 1; i++) {
                    bin2 += (this.weekValues.get(i) ? "1" : "0");
                }
                bin += bin2;
                break;
            case I_Sedana.TIPE_KREDIT_MINGGUAN:
            case I_Sedana.TIPE_KREDIT_MUSIMAN:
                this.frekuensiPokok = ((freq != null) ? (Integer) freq : this.frekuensiPokok);
                bin += Integer.toBinaryString(this.frekuensiPokok);
                break;
        }
        setTipeFrekuensiPokok(Integer.valueOf(bin, 2));
    }

    public boolean isInSelectedDayOfWeek(int day) {
        return weekValues.get(day);
    }

    /**
     * @return the periodePembayaran
     */
    public int getPeriodePembayaran() {
        return periodePembayaran;
    }

    /**
     * @param periodePembayaran the periodePembayaran to set
     */
    public void setPeriodePembayaran(int periodePembayaran) {
        this.periodePembayaran = periodePembayaran;
    }

    /**
     * @return the kategoriKreditId
     */
    public long getKategoriKreditId() {
        return kategoriKreditId;
    }

    /**
     * @param kategoriKreditId the kategoriKreditId to set
     */
    public void setKategoriKreditId(long kategoriKreditId) {
        this.kategoriKreditId = kategoriKreditId;
    }

    /**
     * @return the persentaseWajib
     */
    public double getPersentaseWajib() {
        return persentaseWajib;
    }

    /**
     * @param persentaseWajib the persentaseWajib to set
     */
    public void setPersentaseWajib(double persentaseWajib) {
        this.persentaseWajib = persentaseWajib;
    }

    /**
     * @return the dendaToleransi
     */
    public int getDendaToleransi() {
        return dendaToleransi;
    }

    /**
     * @param dendaToleransi the dendaToleransi to set
     */
    public void setDendaToleransi(int dendaToleransi) {
        this.dendaToleransi = dendaToleransi;
    }

    public int getVariabelDenda() {
        return variabelDenda;
    }

    public void setVariabelDenda(int variabelDenda) {
        this.variabelDenda = variabelDenda;
    }

    /**
     * @return the nominalWajib
     */
    public double getNominalWajib() {
        return nominalWajib;
    }

    /**
     * @param nominalWajib the nominalWajib to set
     */
    public void setNominalWajib(double nominalWajib) {
        this.nominalWajib = nominalWajib;
    }

    public int getTipeVariabelDenda() {
        return tipeVariabelDenda;
    }

    public void setTipeVariabelDenda(int tipeVariabelDenda) {
        this.tipeVariabelDenda = tipeVariabelDenda;
    }

    public int getTipeFrekuensiDenda() {
        return tipeFrekuensiDenda;
    }

    public void setTipeFrekuensiDenda(int tipeFrekuensiDenda) {
        this.tipeFrekuensiDenda = tipeFrekuensiDenda;
    }

}
