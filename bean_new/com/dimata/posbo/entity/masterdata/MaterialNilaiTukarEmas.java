/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.qdep.entity.Entity;
import java.util.Date;
import java.util.Vector;

public class MaterialNilaiTukarEmas extends Entity implements I_LogHistory {

    private long kadarId = 0;
    //update by dewok 20180420
    private long colorId = 0;
    private double lokal = 0;
    private double returnLokal = 0;
    private double returnLokalRusak = 0;
    private double asing = 0;
    private double returnAsing = 0;
    private double returnAsingRusak = 0;
    private double pesanan = 0;
    private double tarifRetur = 0;
    private double tarifReturDiatasSetahun = 0;

    //kemungkinan di bawah ini hanya variabel untuk menampung data sementara hasil query
    private String kadarName = "";
    private String warnaName = "";
    private Date lastUpdate;

    public long getKadarId() {
        return kadarId;
    }

    public void setKadarId(long kadarId) {
        this.kadarId = kadarId;
    }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }

    //update by dewok 20180420
    public double getLokal() {
        return lokal;
    }

    public void setLokal(double lokal) {
        this.lokal = lokal;
    }

    public double getReturnLokal() {
        return returnLokal;
    }

    public void setReturnLokal(double returnLokal) {
        this.returnLokal = returnLokal;
    }

    public double getReturnLokalRusak() {
        return returnLokalRusak;
    }

    public void setReturnLokalRusak(double returnLokalRusak) {
        this.returnLokalRusak = returnLokalRusak;
    }

    public double getAsing() {
        return asing;
    }

    public void setAsing(double asing) {
        this.asing = asing;
    }

    public double getReturnAsing() {
        return returnAsing;
    }

    public void setReturnAsing(double returnAsing) {
        this.returnAsing = returnAsing;
    }

    public double getReturnAsingRusak() {
        return returnAsingRusak;
    }

    public void setReturnAsingRusak(double returnAsingRusak) {
        this.returnAsingRusak = returnAsingRusak;
    }

    public double getPesanan() {
        return pesanan;
    }

    public void setPesanan(double pesanan) {
        this.pesanan = pesanan;
    }

    public double getTarifRetur() {
        return tarifRetur;
    }

    public void setTarifRetur(double tarifRetur) {
        this.tarifRetur = tarifRetur;
    }

    public double getTarifReturDiatasSetahun() {
        return tarifReturDiatasSetahun;
    }

    public void setTarifReturDiatasSetahun(double tarifReturDiatasSetahun) {
        this.tarifReturDiatasSetahun = tarifReturDiatasSetahun;
    }

    /**
     * @return the kadarName
     */
    public String getKadarName() {
        return kadarName;
    }

    /**
     * @param kadarName the kadarName to set
     */
    public void setKadarName(String kadarName) {
        this.kadarName = kadarName;
    }

    /**
     * @return the warnaName
     */
    public String getWarnaName() {
        return warnaName;
    }

    /**
     * @param warnaName the warnaName to set
     */
    public void setWarnaName(String warnaName) {
        this.warnaName = warnaName;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Setter for property lastUpdate.
     *
     * @param lastUpdate New value of property lastUpdate.
     *
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLogDetail(Entity prevDoc) {

        MaterialNilaiTukarEmas prevMaterialNilaiTukarEmas = (MaterialNilaiTukarEmas) prevDoc;

        Kadar kadar = new Kadar();
        Color color = new Color();
        Kadar prevKadar = new Kadar();
        Color prevColor = new Color();

        try {
            if (prevMaterialNilaiTukarEmas != null) {
                if (PstKadar.checkOID(prevMaterialNilaiTukarEmas.getKadarId())) {
                    prevKadar = PstKadar.fetchExc(prevMaterialNilaiTukarEmas.getKadarId());
                }
                if (PstColor.checkOID(prevMaterialNilaiTukarEmas.getColorId())) {
                    prevColor = PstColor.fetchExc(prevMaterialNilaiTukarEmas.getColorId());
                }
            }
            if (PstKadar.checkOID(this.getKadarId())) {
                kadar = PstKadar.fetchExc(this.getKadarId());
            }
            if (PstColor.checkOID(this.getColorId())) {
                color = PstColor.fetchExc(this.getColorId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String logDetail = "";

        if (prevMaterialNilaiTukarEmas == null) {
            logDetail += "Adding "
                    + " Kadar : " + kadar.getKadar()
                    + "; Warna : " + color.getColorName()
                    + "; Lokal : " + this.lokal
                    + "; Return lokal : " + this.returnLokal
                    + "; Return lokal rusak : " + this.returnLokalRusak
                    + "; Asing : " + this.asing
                    + "; Return asing : " + this.returnAsing
                    + "; Return asing rusak : " + this.returnAsingRusak
                    + "; Pesanan : " + this.pesanan
                    + "; Tarif retur : " + this.tarifRetur
                    + "; Tarif retur di atas setahun : " + this.tarifReturDiatasSetahun
                    + "";
        } else {
            if (prevMaterialNilaiTukarEmas.getKadarId() != this.getKadarId()) {
                logDetail += " Kadar changed from '" + prevKadar.getKadar() + "' to '" + kadar.getKadar() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getColorId() != this.getColorId()) {
                logDetail += " Warna changed from '" + prevColor.getColorName() + "' to '" + color.getColorName() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getLokal() != this.getLokal()) {
                logDetail += " Lokal changed from '" + prevMaterialNilaiTukarEmas.getLokal() + "' to '" + this.getLokal() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getReturnLokal() != this.getReturnLokal()) {
                logDetail += " Return lokal changed from '" + prevMaterialNilaiTukarEmas.getReturnLokal() + "' to '" + this.getReturnLokal() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getReturnLokalRusak() != this.getReturnLokalRusak()) {
                logDetail += " Return lokal rusak changed from '" + prevMaterialNilaiTukarEmas.getReturnLokalRusak() + "' to '" + this.getReturnLokalRusak() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getAsing() != this.getAsing()) {
                logDetail += " Asing changed from '" + prevMaterialNilaiTukarEmas.getAsing() + "' to '" + this.getAsing() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getReturnAsing() != this.getReturnAsing()) {
                logDetail += " Return asing changed from '" + prevMaterialNilaiTukarEmas.getReturnAsing() + "' to '" + this.getReturnAsing() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getReturnAsingRusak() != this.getReturnAsingRusak()) {
                logDetail += " Return asing rusak changed from '" + prevMaterialNilaiTukarEmas.getReturnAsingRusak() + "' to '" + this.getReturnAsingRusak() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getPesanan() != this.getPesanan()) {
                logDetail += " Pesanan changed from '" + prevMaterialNilaiTukarEmas.getPesanan() + "' to '" + this.getPesanan() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getTarifRetur()!= this.getTarifRetur()) {
                logDetail += " Tarif retur changed from '" + prevMaterialNilaiTukarEmas.getTarifRetur() + "' to '" + this.getTarifRetur() + "'; ";
            }
            if (prevMaterialNilaiTukarEmas.getTarifReturDiatasSetahun()!= this.getTarifReturDiatasSetahun()) {
                logDetail += " Tarif retur di atas setahun changed from '" + prevMaterialNilaiTukarEmas.getTarifReturDiatasSetahun()+ "' to '" + this.getTarifReturDiatasSetahun() + "'; ";
            }
        }

        return logDetail;
    }

}
