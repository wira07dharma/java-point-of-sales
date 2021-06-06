/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.transaksi;
import com.dimata.qdep.entity.*;
import java.util.Date;

/**
 *
 * @author Dede
 */
public class DataTransaksi extends Entity {
    
    private long IdAnggota;
    private String CodeTransaksi;
    private Date Tanggal;
    private long JenisTransaksi;
    private double Bunga ;
    private double Potongan;
    private double JumlahTransaksi;
    private double Saldo;

    
    
    /**
     * @return the IdAnggota
     */
    public long getIdAnggota() {
        return IdAnggota;
    }

    /**
     * @param IdAnggota the IdAnggota to set
     */
    public void setIdAnggota(long IdAnggota) {
        this.IdAnggota = IdAnggota;
    }

    /**
     * @return the Tanggal
     */
    public Date getTanggal() {
        return Tanggal;
    }

    /**
     * @param Tanggal the Tanggal to set
     */
    public void setTanggal(Date Tanggal) {
        this.Tanggal = Tanggal;
    }

  
    /**
     * @return the Bunga
     */
    public double getBunga() {
        return Bunga;
    }

    /**
     * @param Bunga the Bunga to set
     */
    public void setBunga(double Bunga) {
        this.Bunga = Bunga;
    }

    /**
     * @return the Potongan
     */
    public double getPotongan() {
        return Potongan;
    }

    /**
     * @param Potongan the Potongan to set
     */
    public void setPotongan(double Potongan) {
        this.Potongan = Potongan;
    }

    /**
     * @return the Saldo
     */
    public double getSaldo() {
        return Saldo;
    }

    /**
     * @param Saldo the Saldo to set
     */
    public void setSaldo(double Saldo) {
        this.Saldo = Saldo;
    }

    /**
     * @return the JumlahTransaksi
     */
    public double getJumlahTransaksi() {
        return JumlahTransaksi;
    }

    /**
     * @param JumlahTransaksi the JumlahTransaksi to set
     */
    public void setJumlahTransaksi(double JumlahTransaksi) {
        this.JumlahTransaksi = JumlahTransaksi;
    }

    /**
     * @return the JenisTransaksi
     */
    public long getJenisTransaksi() {
        return JenisTransaksi;
    }

    /**
     * @param JenisTransaksi the JenisTransaksi to set
     */
    public void setJenisTransaksi(long JenisTransaksi) {
        this.JenisTransaksi = JenisTransaksi;
    }

    /**
     * @return the CodeTransaksi
     */
    public String getCodeTransaksi() {
        return CodeTransaksi;
    }

    /**
     * @param CodeTransaksi the CodeTransaksi to set
     */
    public void setCodeTransaksi(String CodeTransaksi) {
        this.CodeTransaksi = CodeTransaksi;
    }

    /**
     * @return the JumlahSimpanan
     */
 
   
    
    
    
}
