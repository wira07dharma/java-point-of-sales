/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class PerhitunganPoin extends Entity {

    private int materialJenisType = 0;
    private double sellValue = 0;
    private int poinReward = 0;
    private Date updateDate = null;
    private int statusAktif = 0;

    public int getMaterialJenisType() {
        return materialJenisType;
    }

    public void setMaterialJenisType(int materialJenisType) {
        this.materialJenisType = materialJenisType;
    }

    public double getSellValue() {
        return sellValue;
    }

    public void setSellValue(double sellValue) {
        this.sellValue = sellValue;
    }

    public int getPoinReward() {
        return poinReward;
    }

    public void setPoinReward(int poinReward) {
        this.poinReward = poinReward;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(int statusAktif) {
        this.statusAktif = statusAktif;
    }

    public String getLogDetail(Entity prevDoc) {
        PerhitunganPoin prevPerhitunganPoin = (PerhitunganPoin) prevDoc;
        String history = "";
        String status[] = {"Tidak aktif", "Aktif"};
        if (prevDoc == null) {
            history += ""
                    + "Jenis item : " + Material.MATERIAL_TYPE_TITLE[this.getMaterialJenisType()] + "; "
                    + "Nilai penjualan : " + String.format("%,.0f", this.getSellValue()) + ".00; "
                    + "Poin : " + this.poinReward + "; "
                    + "Tanggal update : " + Formater.formatDate(this.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "; "
                    + "Status aktif : " + status[this.getStatusAktif()] + ";";
        } else {
            if (prevPerhitunganPoin.getMaterialJenisType() != this.getMaterialJenisType()) {
                history += "Jenis item changed from '" + Material.MATERIAL_TYPE_TITLE[prevPerhitunganPoin.getMaterialJenisType()] + "'"
                        + " to '" + Material.MATERIAL_TYPE_TITLE[this.getMaterialJenisType()] + "'; ";
            }
            if (prevPerhitunganPoin.getSellValue() != this.getSellValue()) {
                history += "Nilai penjualan changed from '" + String.format("%,.0f", prevPerhitunganPoin.getSellValue()) + ".00'"
                        + " to '" + String.format("%,.0f", this.getSellValue()) + ".00'; ";
            }
            if (prevPerhitunganPoin.getPoinReward() != this.getPoinReward()) {
                history += "Nilai penjualan changed from '" + prevPerhitunganPoin.getPoinReward() + "'"
                        + " to '" + this.getPoinReward() + "'; ";
            }
            int compareDate = prevPerhitunganPoin.getUpdateDate().compareTo(this.getUpdateDate());
            if (compareDate != 0) {
                history += "Tanggal update changed from '" + Formater.formatDate(prevPerhitunganPoin.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "'"
                        + " to '" + Formater.formatDate(this.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "'; ";
            }
            if (prevPerhitunganPoin.getStatusAktif() != this.getStatusAktif()) {
                history += "Tanggal update changed from '" + status[prevPerhitunganPoin.getStatusAktif()] + "'"
                        + " to '" + status[this.getStatusAktif()] + "'; ";
            }
        }
        return history;
    }

}
