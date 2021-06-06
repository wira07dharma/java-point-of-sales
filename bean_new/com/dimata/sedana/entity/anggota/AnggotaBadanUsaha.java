/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.anggota;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class AnggotaBadanUsaha extends Entity {

    private String noAnggota = "";
    private String name = "";
    private int sex = 0;
    private String telepon = "";
    private String email = "";
    private String officeAddress = "";
    private long addressOfficeCity;
    private String idCard = "";
    private String noNpwp = "";
    private Date regDate = null;
    private long idJenisTransaksi = 0;

    public long getIdJenisTransaksi() {
        return idJenisTransaksi;
    }

    public void setIdJenisTransaksi(long idJenisTransaksi) {
        this.idJenisTransaksi = idJenisTransaksi;
    }

    public String getNoAnggota() {
        return noAnggota;
    }

    public void setNoAnggota(String noAnggota) {
        this.noAnggota = noAnggota;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public long getAddressOfficeCity() {
        return addressOfficeCity;
    }

    public void setAddressOfficeCity(long addressOfficeCity) {
        this.addressOfficeCity = addressOfficeCity;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNoNpwp() {
        return noNpwp;
    }

    public void setNoNpwp(String noNpwp) {
        this.noNpwp = noNpwp;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

}
