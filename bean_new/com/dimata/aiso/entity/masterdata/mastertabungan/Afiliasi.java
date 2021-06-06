package com.dimata.aiso.entity.masterdata.mastertabungan;

/* package java */
import com.dimata.qdep.entity.*;
import com.dimata.sedana.session.json.JSONObject;

public class Afiliasi extends Entity implements Cloneable {

    private double feeKoperasi;
    private String namaAfiliasi = "";
    private String alamatAfiliasi = "";

    /**
     * @return the feeKoperasi
     */
    public double getFeeKoperasi() {
        return feeKoperasi;
    }

    /**
     * @param feeKoperasi the feeKoperasi to set
     */
    public void setFeeKoperasi(double feeKoperasi) {
        this.feeKoperasi = feeKoperasi;
    }

    /**
     * @return the namaAfiliasi
     */
    public String getNamaAfiliasi() {
        return namaAfiliasi;
    }

    /**
     * @param namaAfiliasi the namaAfiliasi to set
     */
    public void setNamaAfiliasi(String namaAfiliasi) {
        this.namaAfiliasi = namaAfiliasi;
    }

    /**
     * @return the alamatAfiliasi
     */
    public String getAlamatAfiliasi() {
        return alamatAfiliasi;
    }

    /**
     * @param alamatAfiliasi the alamatAfiliasi to set
     */
    public void setAlamatAfiliasi(String alamatAfiliasi) {
        this.alamatAfiliasi = alamatAfiliasi;
    }

    public JSONObject historyCompare(Afiliasi o) {
        JSONObject j = new JSONObject();
        if (!getNamaAfiliasi().equals(o.getNamaAfiliasi())) {
            j.put("Nama Afiliasi", "Dari " + getNamaAfiliasi() + " menjadi " + o.getNamaAfiliasi());
        }
        if (getFeeKoperasi() != o.getFeeKoperasi()) {
            j.put("Fee Koperasi", "Dari " + getFeeKoperasi() + " menjadi " + o.getFeeKoperasi());
        }
        if (!getAlamatAfiliasi().equals(o.getAlamatAfiliasi())) {
            j.put("Alamat Afiliasi", "Dari " + getAlamatAfiliasi() + " menjadi " + o.getAlamatAfiliasi());
        }
        return j;
    }

    public JSONObject historyNew() {
        JSONObject j = new JSONObject();
        j.put("OID", getOID());
        j.put("Nama Afiliasi", getNamaAfiliasi());
        j.put("Fee Koperasi", getFeeKoperasi());
        j.put("Alamat Afiliasi", getAlamatAfiliasi());
        return j;
    }

    public Afiliasi clone() {
        Object o = null;
        try {
            o = super.clone();;
        } catch (CloneNotSupportedException ex) {
            System.err.println(ex);
        }
        return (Afiliasi) o;
    }

}
