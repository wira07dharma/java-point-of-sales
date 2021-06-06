/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.session.json.JSONObject;
import java.util.ArrayList;

public class MasterTabungan extends Entity implements Cloneable {

    private long masterTabunganId = 0;
    private String kodeTabungan = "";
    private String namaTabungan = "";
    private String ket = "";
    private double denda = 0;
    private int jenisDenda = 0;
    private double minSaldoBunga = 0;

    public static int DENDA_PROSENTASE = 0;
    public static int DENDA_NOMINAL = 1;
    public static ArrayList<String[]> txtJenisDenda = new ArrayList<String[]>() {
        {
            add(0, new String[]{"Prosentase", "Percentage"});
            add(1, new String[]{"Nominal", "Nominal"});
        }
    };

    public long getMasterTabunganId() {
        return masterTabunganId;
    }

    public void setMasterTabunganId(long masterTabunganId) {
        this.masterTabunganId = masterTabunganId;
    }

    public String getKodeTabungan() {
        return kodeTabungan;
    }

    public void setKodeTabungan(String kodeTabungan) {
        this.kodeTabungan = kodeTabungan;
    }

    public String getNamaTabungan() {
        return namaTabungan;
    }

    public void setNamaTabungan(String namaTabungan) {
        this.namaTabungan = namaTabungan;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
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
     * @return the jenisDenda
     */
    public int getJenisDenda() {
        return jenisDenda;
    }

    /**
     * @param jenisDenda the jenisDenda to set
     */
    public void setJenisDenda(int jenisDenda) {
        this.jenisDenda = jenisDenda;
    }

    /**
     * @return the minSaldoBunga
     */
    public double getMinSaldoBunga() {
        return minSaldoBunga;
    }

    /**
     * @param minSaldoBunga the minSaldoBunga to set
     */
    public void setMinSaldoBunga(double minSaldoBunga) {
        this.minSaldoBunga = minSaldoBunga;
    }

    public JSONObject historyCompare(MasterTabungan o) {
        JSONObject j = new JSONObject();
        if (!getKodeTabungan().equals(o.getKodeTabungan())) {
            j.put("Kode Tabungan", "Dari " + getKodeTabungan() + " menjadi " + o.getKodeTabungan());
        }
        if (!getNamaTabungan().equals(o.getNamaTabungan())) {
            j.put("Nama Tabungan", "Dari " + getNamaTabungan() + " menjadi " + o.getNamaTabungan());
        }
        if (!getKet().equals(o.getKet())) {
            j.put("Keterangan", "Dari " + getKet() + " menjadi " + o.getKet());
        }
        if (getDenda() != o.getDenda()) {
            j.put("Denda", "Dari " + getDenda() + " menjadi " + o.getDenda());
        }
        if (getJenisDenda() != o.getJenisDenda()) {
            j.put("Jenis Denda", "Dari " + txtJenisDenda.get(getJenisDenda())[0] + " menjadi " + txtJenisDenda.get(o.getJenisDenda())[0]);
        }
        if (getMinSaldoBunga() != o.getMinSaldoBunga()) {
            j.put("Minimal Saldo Bunga", "Dari " + getMinSaldoBunga() + " menjadi " + o.getMinSaldoBunga());
        }
        return j;
    }

    public JSONObject historyNew() {
        JSONObject j = new JSONObject();
        j.put("OID", getOID());
        j.put("Kode Tabungan", getKodeTabungan());
        j.put("Nama Tabungan", getNamaTabungan());
        j.put("Keterangan", getKet());
        j.put("Denda", getDenda());
        j.put("Jenis Denda", txtJenisDenda.get(getJenisDenda())[0]);
        j.put("Minimal Saldo Bunga", getMinSaldoBunga());
        return j;
    }

    public MasterTabungan clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println(ex);
        }
        return (MasterTabungan) o;
    }
}
