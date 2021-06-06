/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author PC
 */
import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;
import java.util.Date;

public class RateJualBerlian extends Entity {

    private String code = "";
    private String name = "";
    private String description = "";
    private double rate = 0;
    private Date updateDate = null;
    private int statusAktif = 0;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
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
        RateJualBerlian prevRateJualBerlian = (RateJualBerlian) prevDoc;
        String history = "";
        String status[] = {"Tidak aktif", "Aktif"};
        if (prevDoc == null) {
            history += ""
                    + "Rate : " + this.getRate() + "; "
                    + "Keterangan : " + this.getDescription() + "; "
                    + "Tanggal update : " + Formater.formatDate(this.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "; "
                    + "Status aktif : " + status[this.getStatusAktif()] + "; ";
        } else {
            if (prevRateJualBerlian.getRate() != this.getRate()) {
                history += "Rate changed from '" + prevRateJualBerlian.getRate() + "'"
                        + " to '" + this.getRate() + "'; ";
            }
            if (prevRateJualBerlian.getDescription().equals(this.getDescription())) {
                history += "Rate changed from '" + prevRateJualBerlian.getDescription() + "'"
                        + " to '" + this.getDescription() + "'; ";
            }
            int compareDate = prevRateJualBerlian.getUpdateDate().compareTo(this.getUpdateDate());
            if (compareDate != 0) {
                history += "Tanggal update changed from '" + Formater.formatDate(prevRateJualBerlian.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "'"
                        + " to '" + Formater.formatDate(this.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "' ; ";
            }
            if (prevRateJualBerlian.getStatusAktif() != this.getStatusAktif()) {
                history += "Tanggal update changed from '" + status[prevRateJualBerlian.getStatusAktif()] + "'"
                        + " to '" + status[this.getStatusAktif()] + "' ; ";
            }
        }
        return history;
    }

}
