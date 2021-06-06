/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 9, 2005
 * Time: 9:24:11 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.entity.search;

import java.util.Date;

public class SrcSellingAktiva {

    private String nomorSelling = "";
    private String nameKonsumen = "";
    private Date tanggalAwal = new Date();
    private Date tanggalakhir = new Date();
    private int type = 0;

    public String getNomorSelling() {
        return nomorSelling;
    }

    public void setNomorSelling(String nomorSelling) {
        this.nomorSelling = nomorSelling;
    }

    public String getNameKonsumen() {
        return nameKonsumen;
    }

    public void setNameKonsumen(String nameKonsumen) {
        this.nameKonsumen = nameKonsumen;
    }

    public Date getTanggalAwal() {
        return tanggalAwal;
    }

    public void setTanggalAwal(Date tanggalAwal) {
        this.tanggalAwal = tanggalAwal;
    }

    public Date getTanggalakhir() {
        return tanggalakhir;
    }

    public void setTanggalakhir(Date tanggalakhir) {
        this.tanggalakhir = tanggalakhir;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
