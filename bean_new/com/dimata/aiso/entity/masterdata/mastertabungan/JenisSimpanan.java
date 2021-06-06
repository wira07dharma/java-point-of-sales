/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.mastertabungan;

import com.dimata.qdep.entity.*;
import com.dimata.sedana.common.I_Sedana;
import com.dimata.sedana.session.json.JSONObject;
import java.util.HashMap;

/**
 *
 * @author Dede
 */
public class JenisSimpanan extends Entity implements Cloneable {

    public static final int FREKUENSI_SIMPANAN_BEBAS = 0;
    public static final int FREKUENSI_SIMPANAN_SEKALI = 1;
    public static final int FREKUENSI_SIMPANAN_BERULANG = 2;

    public static final String[] FREKUENSI_SIMPANAN_TITLE = {"Sukarela / Bebas", "Pokok / Sekali", "Wajib / Bulanan"};

    public static final int FREKUENSI_PENARIKAN_BEBAS = 0;
    public static final int FREKUENSI_PENARIKAN_SEKALI = 1;

    public static final String[] FREKUENSI_PENARIKAN_TITLE = {"Bebas", "Sekali"};

    public static final int BUNGA_SALDO_HARIAN = I_Sedana.BUNGA_SALDO_HARIAN;
    public static final int BUNGA_SALDO_RERATA_HARIAN = I_Sedana.BUNGA_SALDO_RERATA_HARIAN;
    public static final int BUNGA_SALDO_TERENDAH = I_Sedana.BUNGA_SALDO_TERENDAH;
    public static final int BUNGA_SALDO_DEPOSITO = I_Sedana.BUNGA_SALDO_DEPOSITO;
    public static final HashMap<Integer, String[]> BUNGA = I_Sedana.BUNGA;
    private String namaSimpanan = "";
    private double setoranMin;
    private double bunga;
    private double provisi;
    private double biayaAdmin;
    private String DescJenisSimpanan = "";
    private int frekuensiSimpanan;
    private int frekuensiPenarikan;
    private double basisHariBunga;
    private int jenisBunga;

    /**
     * @return the namaSimpanan
     */
    public String getNamaSimpanan() {
        return namaSimpanan;
    }

    /**
     * @param namaSimpanan the namaSimpanan to set
     */
    public void setNamaSimpanan(String namaSimpanan) {
        this.namaSimpanan = namaSimpanan;
    }

    /**
     * @return the setoranMin
     */
    public double getSetoranMin() {
        return setoranMin;
    }

    /**
     * @param setoranMin the setoranMin to set
     */
    public void setSetoranMin(double setoranMin) {
        this.setoranMin = setoranMin;
    }

    /**
     * @return the bunga
     */
    public double getBunga() {
        return bunga;
    }

    /**
     * @param bunga the bunga to set
     */
    public void setBunga(double bunga) {
        this.bunga = bunga;
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
     * @return the DescJenisSimpanan
     */
    public String getDescJenisSimpanan() {
        return DescJenisSimpanan;
    }

    /**
     * @param DescJenisSimpanan the DescJenisSimpanan to set
     */
    public void setDescJenisSimpanan(String DescJenisSimpanan) {
        this.DescJenisSimpanan = DescJenisSimpanan;
    }

    /**
     * @return the frekuensiSimpanan
     */
    public int getFrekuensiSimpanan() {
        return frekuensiSimpanan;
    }

    /**
     * @param frekuensiSimpanan the frekuensiSimpanan to set
     */
    public void setFrekuensiSimpanan(int frekuensiSimpanan) {
        this.frekuensiSimpanan = frekuensiSimpanan;
    }

    /**
     * @return the frekuensiPenarikan
     */
    public int getFrekuensiPenarikan() {
        return frekuensiPenarikan;
    }

    /**
     * @param frekuensiPenarikan the frekuensiPenarikan to set
     */
    public void setFrekuensiPenarikan(int frekuensiPenarikan) {
        this.frekuensiPenarikan = frekuensiPenarikan;
    }

    /**
     * @return the basisHariBunga
     */
    public double getBasisHariBunga() {
        return basisHariBunga;
    }

    /**
     * @param basisHariBunga the basisHariBunga to set
     */
    public void setBasisHariBunga(double basisHariBunga) {
        this.basisHariBunga = basisHariBunga;
    }

    /**
     * @return the jenisBunga
     */
    public int getJenisBunga() {
        return jenisBunga;
    }

    /**
     * @param jenisBunga the jenisBunga to set
     */
    public void setJenisBunga(int jenisBunga) {
        this.jenisBunga = jenisBunga;
    }

    public JSONObject historyCompare(JenisSimpanan o) {
        JSONObject j = new JSONObject();
        if (!getNamaSimpanan().equals(o.getNamaSimpanan())) {
            j.put("Nama Item", "Dari " + getNamaSimpanan() + " menjadi " + o.getNamaSimpanan());
        }
        if (getSetoranMin() != o.getSetoranMin()) {
            j.put("Setoran Min.", "Dari " + getSetoranMin() + " menjadi " + o.getSetoranMin());
        }
        if (getBunga() != o.getBunga()) {
            j.put("Bunga", "Dari " + getBunga() + " menjadi " + o.getBunga());
        }
        if (getProvisi() != o.getProvisi()) {
            j.put("Provisi", "Dari " + getProvisi() + " menjadi " + o.getProvisi());
        }
        if (getBiayaAdmin() != o.getBiayaAdmin()) {
            j.put("Biaya Admin", "Dari " + getBiayaAdmin() + " menjadi " + o.getBiayaAdmin());
        }
        if (!getDescJenisSimpanan().equals(o.getDescJenisSimpanan())) {
            j.put("Deskripsi", "Dari " + getDescJenisSimpanan() + " menjadi " + o.getDescJenisSimpanan());
        }
        if (getFrekuensiSimpanan() != o.getFrekuensiSimpanan()) {
            j.put("Frekuensi Simpanan", "Dari " + FREKUENSI_SIMPANAN_TITLE[getFrekuensiSimpanan()] + " menjadi " + FREKUENSI_SIMPANAN_TITLE[o.getFrekuensiSimpanan()]);
        }
        if (getFrekuensiPenarikan() != o.getFrekuensiPenarikan()) {
            j.put("Frekuensi Penarikan", "Dari " + FREKUENSI_PENARIKAN_TITLE[getFrekuensiPenarikan()] + " menjadi " + FREKUENSI_PENARIKAN_TITLE[o.getFrekuensiPenarikan()]);
        }
        if (getBasisHariBunga() != o.getBasisHariBunga()) {
            j.put("Basis Hari Bunga", "Dari " + getBasisHariBunga() + " menjadi " + o.getBasisHariBunga());
        }
        if (getJenisBunga() != o.getJenisBunga()) {
            j.put("Jenis Bunga", "Dari " + JenisSimpanan.BUNGA.get(getJenisBunga())[0] + " menjadi " + JenisSimpanan.BUNGA.get(o.getJenisBunga())[0]);
        }
        return j;
    }

    public JSONObject historyNew() {
        JSONObject j = new JSONObject();
        j.put("OID", getOID());
        j.put("Nama Item", getNamaSimpanan());
        j.put("Setoran Min.", getSetoranMin());
        j.put("Bunga", getBunga());
        j.put("Provisi", getProvisi());
        j.put("Biaya Admin", getBiayaAdmin());
        j.put("Deskripsi", getDescJenisSimpanan());
        j.put("Frekuensi Simpanan", FREKUENSI_SIMPANAN_TITLE[getFrekuensiSimpanan()]);
        j.put("Frekuensi Penarikan", FREKUENSI_PENARIKAN_TITLE[getFrekuensiPenarikan()]);
        j.put("Basis Hari Bunga", getBasisHariBunga());
        j.put("Jenis Bunga", JenisSimpanan.BUNGA.get(getJenisBunga())[0]);
        return j;
    }

    public JenisSimpanan clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println(ex);
        }
        return (JenisSimpanan) o;
    }
}
