/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 3, 2005
 * Time: 9:37:14 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.entity.search;

public class SrcModulAktiva {
    private String kodeAktiva = "";
    private String namaAktiva = "";
    private long jenisAktivaId = 0;
    private long tipePenyusutanId = 0;
    private long metodepenyusutanId = 0;
    private long orderOid = 0;

    public long getOrderOid() {
        return orderOid;
    }

    public void setOrderOid(long orderOid) {
        this.orderOid = orderOid;
    }

    public String getKodeAktiva() {
        return kodeAktiva;
    }

    public void setKodeAktiva(String kodeAktiva) {
        this.kodeAktiva = kodeAktiva;
    }

    public String getNamaAktiva() {
        return namaAktiva;
    }

    public void setNamaAktiva(String namaAktiva) {
        this.namaAktiva = namaAktiva;
    }

    public long getJenisAktivaId() {
        return jenisAktivaId;
    }

    public void setJenisAktivaId(long jenisAktivaId) {
        this.jenisAktivaId = jenisAktivaId;
    }

    public long getTipePenyusutanId() {
        return tipePenyusutanId;
    }

    public void setTipePenyusutanId(long tipePenyusutanId) {
        this.tipePenyusutanId = tipePenyusutanId;
    }

    public long getMetodepenyusutanId() {
        return metodepenyusutanId;
    }

    public void setMetodepenyusutanId(long metodepenyusutanId) {
        this.metodepenyusutanId = metodepenyusutanId;
    }
}
