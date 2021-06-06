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
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;
import java.util.Date;

public class EmasLantakan extends Entity {

    private double hargaBeli = 0;
    private double hargaJual = 0;
    private double hargaTengah = 0;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private int statusAktif = 0;

    public double getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(double hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public double getHargaTengah() {
        return hargaTengah;
    }

    public void setHargaTengah(double hargaTengah) {
        this.hargaTengah = hargaTengah;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(int statusAktif) {
        this.statusAktif = statusAktif;
    }

    public String getLogDetail(Entity prevDoc) {
        String logDetail = "";
        EmasLantakan prevEmasLantakan = (EmasLantakan) prevDoc;
        String status[] = {"Tidak aktif", "Aktif"};
        try {
            if (prevEmasLantakan == null) {
                logDetail += "Adding Emas Lantakan;";
                logDetail += "Harga Beli : " + FRMHandler.userFormatStringDecimal(this.hargaBeli) + "; ";
                logDetail += "Harga Jual : " + FRMHandler.userFormatStringDecimal(this.hargaJual) + "; ";
                logDetail += "Harga Tengah : " + FRMHandler.userFormatStringDecimal(this.hargaTengah) + "; ";
                logDetail += "Tanggal Awal : " + Formater.formatDate(this.startDate, "yyyy-MM-dd") + "; ";
                logDetail += "Tanggal Akhir : " + Formater.formatDate(this.endDate, "yyyy-MM-dd") + "; ";
                logDetail += "Status Aktif : " + status[this.statusAktif] + "; ";
            } else {
                logDetail += "Change Emas Lantakan;";
                if (prevEmasLantakan.getHargaBeli() != this.hargaBeli) {
                    logDetail += "Change Harga Beli from " + FRMHandler.userFormatStringDecimal(prevEmasLantakan.getHargaBeli()) + " to " + FRMHandler.userFormatStringDecimal(this.hargaBeli) + "; ";
                }
                if (prevEmasLantakan.getHargaJual() != this.hargaJual) {
                    logDetail += "Change Harga Jual from " + FRMHandler.userFormatStringDecimal(prevEmasLantakan.getHargaJual()) + " to " + FRMHandler.userFormatStringDecimal(this.hargaJual) + "; ";
                }
                if (prevEmasLantakan.getHargaTengah() != this.hargaTengah) {
                    logDetail += "Change Harga Tengah from " + FRMHandler.userFormatStringDecimal(prevEmasLantakan.getHargaTengah()) + " to " + FRMHandler.userFormatStringDecimal(this.hargaTengah) + "; ";
                }
                if (!Formater.formatDate(prevEmasLantakan.getStartDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.startDate, "yyyy-MM-dd"))) {
                    logDetail += "Change Tanggal Awal from " + Formater.formatDate(prevEmasLantakan.getStartDate(), "yyyy-MM-dd") + " to " + Formater.formatDate(this.startDate, "yyyy-MM-dd") + "; ";
                }
                if (!Formater.formatDate(prevEmasLantakan.getEndDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.endDate, "yyyy-MM-dd"))) {
                    logDetail += "Change Tanggal Akhir from " + Formater.formatDate(prevEmasLantakan.getEndDate(), "yyyy-MM-dd") + " to " + Formater.formatDate(this.endDate, "yyyy-MM-dd") + "; ";
                }
                if (prevEmasLantakan.getStatusAktif() != this.statusAktif) {
                    logDetail += "Change Status Aktif from " + status[prevEmasLantakan.getStatusAktif()] + " to " + status[this.statusAktif] + "; ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logDetail;
    }

}
