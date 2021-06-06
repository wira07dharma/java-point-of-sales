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
 * @author Regen
 */
public class RatePasarBerlian extends Entity {

    private String code = "";
    private String name = "";
    private double rate = 0;
    private String description = "";
    private Date updateDate = null;

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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getLogDetail(Entity prevDoc) {
        RatePasarBerlian prevRatePasarBerlian = (RatePasarBerlian) prevDoc;
        String history = "";
        if (prevDoc == null) {
            history += ""
                    + "Kode : " + this.code + "; "
                    + "Nama : " + this.name + "; "
                    + "Rate : " + this.rate + "; "
                    + "Keterangan : " + this.description + "; "
                    + "Tanggal update : " + Formater.formatDate(this.updateDate, "yyyy-MM-dd HH:mm:ss") + "; "
                    + "";
        } else {
            if (!prevRatePasarBerlian.getCode().equals(this.code)) {
                history += "Change kode from " + prevRatePasarBerlian.getCode() + " to " + this.code + "; ";
            }
            if (!prevRatePasarBerlian.getName().equals(this.name)) {
                history += "Change kode from " + prevRatePasarBerlian.getName() + " to " + this.name + "; ";
            }
            if (prevRatePasarBerlian.getRate() != this.rate) {
                history += "Change kode from " + prevRatePasarBerlian.getRate() + " to " + this.rate + "; ";
            }
            if (!prevRatePasarBerlian.getDescription().equals(this.description)) {
                history += "Change kode from " + prevRatePasarBerlian.getDescription() + " to " + this.description + "; ";
            }
            int compareDate = prevRatePasarBerlian.getUpdateDate().compareTo(this.updateDate);
            if (compareDate != 0) {
                history += "Tanggal update changed from '" + Formater.formatDate(prevRatePasarBerlian.getUpdateDate(), "yyyy-MM-dd HH:mm:ss") + "'"
                        + " to " + Formater.formatDate(this.updateDate, "yyyy-MM-dd HH:mm:ss") + "; ";
            }
        }
        return history;
    }

}
